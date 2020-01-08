package com.zach.biz.view.mode;

import java.util.Arrays;
import java.util.List;

/**
 * 静态常量
 *
 */
public class Constant {
	
	//请求头常量
	public static final String TOKEN = "token";
	public static final String LANGUAGE = "language";
	
	// 数据有效性   0:有效   1:无效
	public static final int DATA_ENABLE = 0;
	public static final int DATA_DISABLE = 1;
	
	//常用字符串
	public static final String POINT_STR = ".";
	public static final String VERTICAL_STR = "|";
	public static final String SLASH_STR = "\\";
	public static final String BACKSLASH_STR = "/";
	public static final String HTTP = "http://";
	public static final String CONTENT_TYPE = "application/json";
	public static final String WEB_INF = "WEB-INF";
	
	// 文件的contentType
	public static final String CHARSETS_UTF_8 = "UTF-8";
	public static final String CHARSETS_ISO_8859_1 = "ISO-8859-1";
	public static final String ContentType_HTML = "text/html;charset=utf-8";
	public static final String ContentType_PDF = "application/pdf;charset=utf-8";
	public static final String ContentType_DOC = "application/msword;charset=utf-8";
	public static final String ContentType_EXCEL = "application/vnd.ms-excel;charset=utf-8";
	
	// 不允许的文件后缀
	public static final List<String> EXECUT_SUFFIX = Arrays.asList("JSP,ASP,JAVA,EXE,PY,PHP,CPP,H,HXX,CXX,C,SSH".split(","));
	// 图片文件后缀
	public static final List<String> IMG_SUFFIX = Arrays.asList("jpg,JPG,png,PNG,jpeg,JPEG,bmp,BMP,gif,GIF".split(","));
	//音频
	public static final List<String> AUDIO_SUFFIX = Arrays.asList("mp3,mp4,midi,wma,rm".split(","));
	//视频
	public static final List<String> VIDEO_SUFFIX = Arrays.asList("avi,,wmv,mpeg,dv,mov".split(","));
	//视频
	public static final List<String> FILE_SUFFIX = Arrays.asList("doc,docx,xls,xlsx,ppt,pdf,txt,html,zip,rar".split(","));
	
	// 文件上传的大小设置 30M
	public static final long MAX_FILE_UPLOAD_SIZE = 3 * 10 * 1024 * 1024L;

	
} 
