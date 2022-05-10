package com.chat.im.im_fileserver.service;

import com.qcloud.cos.model.UploadResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: chovychan in 2022/5/8
 */
public interface FileService {
    long INF = 1024L;

    enum FILE_UNIT {
        B(1L),
        KB(INF),
        MB(INF * INF),
        GB(INF * INF * INF),
        TB(INF * INF * INF * INF);

        public Long size;

        FILE_UNIT(Long size) {
            this.size = size;
        }
    }

    /**
     * 上传文件
     *
     * @param file 文件
     */
    UploadResult upload(MultipartFile file, String path);

    /**
     * 访问文件
     *
     * @param key 文件key
     */
    String preview(String key);
}
