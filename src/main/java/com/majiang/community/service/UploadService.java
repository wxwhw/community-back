package com.majiang.community.service;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.majiang.community.base.ResultInfo;
import com.majiang.community.exceptions.ParamsException;
import com.majiang.community.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;


/**
 * Author wxh
 * 2023/4/11 15:13
 */
@Service
public class UploadService {

    @Resource
    private UserService userService;

    @Value("${myUpload.Url}")
    private String absolutePreUrl;

    public ResultInfo uploadAvatar(MultipartFile file, Long id) {
        ResultInfo resultInfo = new ResultInfo();

        HashMap<String, Object> map = new HashMap<>();
        try {
            /*判断文件大小，非空*/
            checkFileParams(file);

            /*获取文件名，转换成新的文件名*/
            String fileName = file.getOriginalFilename();
            String UUID = cn.hutool.core.lang.UUID.randomUUID().toString().replace("-", "");
            String fileExt = "";
            if (fileName != null && fileName.contains(".")) {
                fileExt = fileName.substring(fileName.lastIndexOf("."));
            }

            /*判断上传文件的类型*/
            String[] extList = {".jpg", ".png", ".webp", ".jpeg"};
            AssertUtil.isTrue(!ArrayUtil.contains(extList, fileExt), "不支持的文件类型！");

            /*新的全局唯一文件名*/
            String newFileName = UUID + fileExt;
            ApplicationHome applicationHome = new ApplicationHome(this.getClass());
            String pre = applicationHome.getDir().getParentFile().getParentFile().getAbsolutePath() + "\\src\\main\\resources\\static\\upload\\images\\";
            String absoluPath = pre + newFileName;
            String relativePath = "image/" + newFileName;
            /*将文件上传到本地绝对路径*/
            /*file.transferTo(new File(path));*/
            uploadFile(file, absoluPath);


            userService.uploadAvatarById(relativePath, id);

            /*返回前端一个相对路径*/
            map.put("path", relativePath);
            resultInfo.setMsg("上传成功！");
            resultInfo.setResult(map);

        } catch (ParamsException p) {
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("上传失败，请重试！");
        }

        return resultInfo;
    }

    public ResultInfo uploadImg(MultipartFile file) {

        ResultInfo resultInfo = new ResultInfo();

        HashMap<String, Object> map = new HashMap<>();
        try {
            /*判断文件大小，非空*/
            checkFileParams(file);

            /*获取文件后缀名*/
            String fileName = file.getOriginalFilename();
            String UUID = cn.hutool.core.lang.UUID.randomUUID().toString().replace("-", "");
            String fileExt = "";
            if (fileName != null && fileName.contains(".")) {
                fileExt = fileName.substring(fileName.lastIndexOf("."));
            }

            /*判断上传文件的类型*/
            String[] extList = {".jpg", ".png", ".webp", ".jpeg"};
            AssertUtil.isTrue(!ArrayUtil.contains(extList, fileExt), "不支持的文件类型！");

            /*将UUID作为文件夹名称,并创建一个文件夹*/
            File folder = new File(absolutePreUrl + UUID);
            String imgPreUrl = "";
            if (!folder.exists() && folder != null) {
                boolean result = folder.mkdir();
                if (result) {
                    /*文件夹创建成功*/
                    imgPreUrl = absolutePreUrl + UUID + "/";
                } else {
                    /*文件夹创建失败*/
                }
            } else {
                /*文件夹已存在*/
                System.out.println("失败");
            }

            String absoluPath = imgPreUrl + fileName;
            String relativePath = "imgUrl/" + UUID + "/" + fileName;

            /*将文件上传到本地绝对路径*/
            uploadFile(file, absoluPath);

            /*返回前端一个相对路径*/
            map.put("url", relativePath);
            resultInfo.setMsg("上传成功！");
            resultInfo.setResult(map);

        } catch (ParamsException p) {
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("上传失败，请重试！");
        }

        return resultInfo;
    }


    @Async
    public void uploadFile(MultipartFile file, String path) {
        try {
            //将文件上传到本地绝对路径
            File Ffile = new File(path);
            file.transferTo(Ffile);
            if (Ffile.exists()) {
                System.out.println("上传完成！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkFileParams(MultipartFile file) {


        AssertUtil.isTrue(file.isEmpty(), "文件不能为空！");

        /*文件大于2M*/
        AssertUtil.isTrue(file.getSize() > 2 * 1024 * 1024, "文件大小不超过2M！");
    }
}
