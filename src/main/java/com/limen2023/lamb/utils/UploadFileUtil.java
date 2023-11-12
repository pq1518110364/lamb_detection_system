package com.limen2023.lamb.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.InetAddress;
import java.util.UUID;

@Component
public class UploadFileUtil {

    /**
     * 项目端口
     */
    @Value("${server.port}")
    public String port;

    // 项目根路径下的目录  -- SpringBoot static 目录相当于是根路径下（SpringBoot 默认）
    public final static String IMG_PATH_PREFIX = "/static/upload/imgs/";

    public static File getImgDirFile(){

        // 构建上传文件的存放 "文件夹" 路径
        String fileDirPath = new String("src/main/resources/" + IMG_PATH_PREFIX);

        File fileDir = new File(fileDirPath);
        if(!fileDir.exists()){
            // 递归生成文件夹
            fileDir.mkdirs();
        }
        return fileDir;
    }
    /**
     * 上传文件
     *
     * @param multipartFile 文件对象
     * @param dir 上传目录
     * @return
     */
    public Result uploadFile(MultipartFile multipartFile, String dir) {
        try {
            if (multipartFile.isEmpty()) {
                return Result.error("请选择文件");
            }
            // 获取文件的名称
            String originalFilename = multipartFile.getOriginalFilename();
            // 文件后缀 例如：.png
            String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            // uuid 生成文件名
            String uuid = String.valueOf(UUID.randomUUID());
            // 根路径，在 resources/static/upload
            String basePath = ResourceUtils.getURL("classpath:").getPath() + "static/upload/" + (StringUtils.isNotBlank(dir) ? (dir + "/") : "");
            // 新的文件名，使用uuid生成文件名
            String fileName = uuid + fileSuffix;

            // 放上传图片的文件夹
            File fileDir = getImgDirFile();
            // 输出文件夹绝对路径  -- 这里的绝对路径是相当于当前项目的路径而不是“容器”路径
            System.out.println(fileDir.getAbsolutePath());
            // 文件夹不存在，则新建
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            // 构建真实的文件路径
            File newFile = new File(fileDir.getAbsolutePath() + File.separator + fileName);
            System.out.println(newFile.getAbsolutePath());
            // 完成文件的上传
            multipartFile.transferTo(newFile);
            // 返回绝对路径
            return Result.ok("http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port + IMG_PATH_PREFIX + fileName);

            //return Result.ok(newFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("上传失败");
    }
}