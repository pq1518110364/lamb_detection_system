package com.limen2023.lamb.controller;

import com.limen2023.lamb.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/data-center")
public class Data_CenterController {

    @PostMapping("/uploadExcel")
    public Result list(@RequestParam("file") MultipartFile file) throws Exception {
        System.out.println(file.isEmpty());
        System.out.println("Received file: " + file.getOriginalFilename());
        //返回数据
        return Result.ok();
    }
}
