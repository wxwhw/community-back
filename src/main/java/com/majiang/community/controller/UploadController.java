package com.majiang.community.controller;

import com.majiang.community.base.ResultInfo;
import com.majiang.community.service.UploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * Author wxh
 * 2023/4/11 15:08
 */
@RestController
@RequestMapping("upload")
public class UploadController {

    @Resource
    private UploadService uploadService;

    @PostMapping("uploadAvatar")
    public ResultInfo uploadAvatar(MultipartFile file,Long id){

        return uploadService.uploadAvatar(file,id);
    }

    @PostMapping("uploadImg")
    public ResultInfo uploadImg(MultipartFile file){
        return uploadService.uploadImg(file);
    }
}
