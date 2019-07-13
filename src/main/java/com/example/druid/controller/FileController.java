package com.example.druid.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class FileController {

    @GetMapping("file")
    public String file(){
        return "file";
    }
    @GetMapping("uploadfile")
    public void uploadfile(){

    }
}
