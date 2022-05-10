package com.chat.im.im_fileserver.service.impl;

import com.chat.im.im_fileserver.service.FileService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * @author: chovychan in 2022/5/8
 */
@Service
@Log4j2
public class FileServiceImpl implements FileService {
    private static final String CLASS_NAME = "FileServiceImpl";
    private static final String BUCKET_NAME = "imchatserver-1306179590";
    private static final String SECRET_ID = "AKIDjIYYNvCCvNYS5SYMtAAtMZVAYfW5NO9d";
    private static final String SECRET_KEY = "HI2WjgMltkKdfQt50e16vSCfrte8LHwK";
    // 限制文件大小5M;
    private static final Integer FILE_SIZE = 10;
    private static final FILE_UNIT FILE_TYPE = FILE_UNIT.MB;
    private static final Map<FILE_UNIT, Long> CHECK_FILE_MAP = new HashMap<>() {{
        put(FILE_UNIT.B, FILE_UNIT.B.size);
        put(FILE_UNIT.KB, FILE_UNIT.KB.size);
        put(FILE_UNIT.MB, FILE_UNIT.MB.size);
        put(FILE_UNIT.GB, FILE_UNIT.GB.size);
        put(FILE_UNIT.TB, FILE_UNIT.TB.size);
    }};
    private static final Long FILE_LIMIT = FILE_SIZE * CHECK_FILE_MAP.get(FILE_TYPE);


    @Override
    public UploadResult upload(MultipartFile file, String path) {
        try (InputStream inputStream = file.getInputStream()) {
            if (checkFileSize(file.getSize())) throw Error("文件超过上传大小限制, 上传限制为: " + FILE_LIMIT);

            String finalName = getFileName(path, file);
            int size = 0, cnt = 0;
            while (size == 0) {
                size = inputStream.available();
                if (++cnt == 10) throw Error("尝试获取流长度失败, 或上传文件为空");
            }
            log.info("<< ServerProvider::FileServiceImpl::INFO >> info: fileName [{}], streamSize={}", finalName, size);
            return uploadFile(finalName, inputStream, size);
        } catch (IOException e) {
            throw Error(e.getMessage());
        } catch (CosClientException | InterruptedException e) {
            throw Error("COS错误: " + e.getMessage());
        }
    }

    @Override
    public String preview(String key) {
        return Optional.ofNullable(createCOSClient().getObjectUrl(BUCKET_NAME, key))
                .map(URL::toString).orElseThrow(() -> {
                    throw Error("preview: result error");
                });
    }

    private boolean checkFileSize(Long fileSize) {
        return fileSize > FILE_LIMIT;
    }

    private String getFileName(String path, MultipartFile file) {
        return Optional.ofNullable(file.getOriginalFilename()).map(fileName -> {
            var fileSize = fileName.length();
            var fileSuf = fileName.substring(fileName.lastIndexOf("."), fileSize);
            // 这里生成的是文件名 + 随机数, 也是存入COS的key
            fileName = path + "/" + Base64.getEncoder().encodeToString(fileName.getBytes(StandardCharsets.UTF_8));
            // log.info("<< ServerProvider::FileServiceImpl::INFO >> info: file[{}] upload to COS ", fileName);
            return fileName + fileSuf;
        }).orElseThrow(() -> {
            throw Error("上传文件名称异常");
        });
    }

    private UploadResult uploadFile(String fileName, InputStream in, int size) throws InterruptedException {
        var manager = createTransferManager();
        var objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, fileName, in, objectMetadata);
        UploadResult uploadResult = manager.upload(request).waitForUploadResult();
        shutdownTransferManager(manager);
        return uploadResult;
    }

    private void shutdownTransferManager(TransferManager transferManager) {
        transferManager.shutdownNow(false);
    }

    private TransferManager createTransferManager() {
        COSClient cosClient = createCOSClient();
        ExecutorService threadPool = Executors.newFixedThreadPool(32);
        TransferManager transferManager = new TransferManager(cosClient, threadPool);
        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
        transferManagerConfiguration.setMultipartUploadThreshold(FILE_LIMIT);
        transferManagerConfiguration.setMinimumUploadPartSize(FILE_UNIT.MB.size);
        transferManager.setConfiguration(transferManagerConfiguration);
        return transferManager;
    }

    private COSClient createCOSClient() {
        COSCredentials cred = new BasicCOSCredentials(SECRET_ID, SECRET_KEY);
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setRegion(new Region("ap-guangzhou"));
        clientConfig.setHttpProtocol(HttpProtocol.https);
        clientConfig.setSocketTimeout(30 * 1000);
        clientConfig.setConnectionTimeout(30 * 1000);
        return new COSClient(cred, clientConfig);
    }

    private RuntimeException Error(String msg) {
        log.error("{} ERROR msg: {}", CLASS_NAME, msg);
        return new RuntimeException(msg);
    }
}
