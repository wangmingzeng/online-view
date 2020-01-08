package com.zach.biz.view.controller;


import com.zach.biz.view.config.Configs;
import com.zach.biz.view.mode.Constant;
import com.zach.biz.view.mode.Result;
import com.zach.biz.view.mode.ReturnCode;
import com.zach.biz.view.utils.StringUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

@RestController
@RequestMapping("/file")
public class FileController {

    @RequestMapping(value = "/upload", method = {RequestMethod.POST})
    public Result upload(@RequestParam("file") final MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return Result.fail(ReturnCode.PARAMS_ERROR, "文件为空");
        }
        String[] fileNameTemp = file.getOriginalFilename().split("\\.");
        if (fileNameTemp.length != 2) {
            return Result.fail(ReturnCode.PARAMS_ERROR, "文件格式不对");
        }
        String ext = fileNameTemp[1];
        if(!Constant.IMG_SUFFIX.contains(ext)) {
            return Result.fail(ReturnCode.PARAMS_ERROR, "文件类型不对");
        }
        //文件夹，该参数如果存在，则存入该目录中，不存在则存入默认目录
        String folder = request.getParameter("folder");
        if(StringUtil.isEmpty(folder)) {
            folder = getDatePath(Configs.getFileDefaultpath());
        }
        //根路径
        String basepath = Configs.getFileBasepath();
        String answer = null;
        try {
            String folderPath = basepath + folder;
            File dir = new File(folderPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String filename = System.currentTimeMillis() + Constant.POINT_STR + ext;	//文件名
            String filePath = folderPath + File.separator + filename;
            file.transferTo(new File(filePath));
            //文件的相对路径
            answer = folder + File.separator + filename;
        } catch (IOException e) {
            System.out.println("保存文件到本地错误" + e.getMessage());
            return Result.fail(ReturnCode.UNKNOWN_SYSTEM_ERROR, "文件保存异常");
        }
        return Result.success(answer);
    }

    /**
     * 获取默认文件存储路径
     * @param pre	前缀
     * @return
     */
    private String getDatePath(String pre) {
        if(StringUtil.isEmpty(pre)) {
            pre = "/file";
        }
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) +1;
        int day = cal.get(Calendar.DATE);
        return pre + File.separator + year + File.separator + month + File.separator + day;
    }
}
