package com.limen2023.lamb.controller;

import com.limen2023.lamb.utils.Result;
import com.limen2023.lamb.utils.SFTPUtil;
import com.limen2023.lamb.utils.UploadFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("/api/oss/file")
public class OSSController {

    @Resource
    public UploadFileUtil uploadFileUtil;

    /**
     * 文件上传
     * @param file
     * @param module
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file, String module) throws MalformedURLException, InterruptedException {
        Result result = uploadFileUtil.uploadFile(file, module);
        Long i =1000L;
        // 开始时间
        long stime = System.currentTimeMillis();
        // 执行时间（1s）
        extracted((String) result.getData(),i);
        // 结束时间
        long etime = System.currentTimeMillis();
        // 计算执行时间
        long l = (etime - stime) ;
        System.out.printf("执行时长：%d 毫秒.", l);
        if(l>30000){
            return Result.error("图片上传超时");
        }
        System.out.println(l);
        return result;
    }

    private static void extracted(String path,long i) throws MalformedURLException, InterruptedException {
        URL url=new URL(path);
        boolean isExist = new UrlResource(url).exists();
        if (i>20000){
            return;
        }
        if (!isExist){
            Thread.sleep(1000);
            i+=1000;
            extracted(path,i);
        }
    }

    @Autowired
    private SFTPUtil sftpUtil;
    /**
     * 上传文件到服务器
     *
     * @param file 图片
     * @return
     */
    @PostMapping("/images")
    public Result<List<String>> file(MultipartFile file) throws Exception {
        List<String> paths = sftpUtil.uploadMultipartFilesToServer(file);
        return Result.ok(paths);
    }
}
