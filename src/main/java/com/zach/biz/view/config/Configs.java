package com.zach.biz.view.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value= {"classpath:context.properties"})
public class Configs {

    @Value("${file.basepath}")
    public static String fileBasepath;

    @Value("${file.defaultpath}")
    public static String fileDefaultpath;

    public static String getFileBasepath(){
        return fileBasepath;
    }

    public  static  String getFileDefaultpath(){
        return fileDefaultpath;
    }
}
