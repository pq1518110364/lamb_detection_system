package com.limen2023.lamb.config.security;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class ImagesUploadConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //获取文件的真实路径
        String path = System.getProperty("D:\\IdeaProjects\\lamb_detection_system")+"\\src\\main\\resources\\static\\imgs\\";
        //uploadFile对应resource下工程目录
        registry.addResourceHandler("/imgs/**").addResourceLocations("file:"+path);
    }
}
