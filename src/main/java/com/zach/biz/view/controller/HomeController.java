package com.zach.biz.view.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/index")
    public String index(){
        System.out.println("index request");
        return "index page";
    }

}
