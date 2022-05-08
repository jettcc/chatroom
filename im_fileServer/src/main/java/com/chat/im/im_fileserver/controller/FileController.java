package com.chat.im.im_fileserver.controller;

import com.chat.im.im_common.utils.SystemMsgJsonResponse;
import com.chat.im.im_fileserver.service.FileService;
import com.qcloud.cos.model.UploadResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file/common")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    @ApiOperation(value = "[文件] - 上传文件接口", httpMethod = "POST", response = UploadResult.class)
    public SystemMsgJsonResponse upload(@RequestParam("file") MultipartFile file,
                                        @RequestParam("path") String path) {
        return SystemMsgJsonResponse.success(fileService.upload(file, path));
    }

    @GetMapping("/preview")
    @ApiOperation(value = "[文件] - 预览文件接口", httpMethod = "GET", response = String.class)
    public SystemMsgJsonResponse preview(@RequestParam("fileKey") String key) {
        return SystemMsgJsonResponse.success(fileService.preview(key));
    }
}
