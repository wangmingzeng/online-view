package com.zach.biz.view.poi;

//import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
//import com.artofsolving.jodconverter.DocumentConverter;
//import com.artofsolving.jodconverter.DocumentFormatRegistry;
//import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
//import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
//import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
//import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormatRegistry;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import com.zach.biz.view.utils.StringUtil;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;

/**
 * @desc openoffice转换工具
 */

public class OpenOfficeUtils {

    public static final String LOCAL_HOST = "127.0.0.1";
    public static final int LOCAL_PORT = 8100;

    // Format
    public static DocumentFormatRegistry formatFactory = new DefaultDocumentFormatRegistry();


    /**
     * @param inputFilePath  待转换的文件路径
     * @param outputFilePath 输出文件路径
     */
    public static void convert(String inputFilePath, String outputFilePath) throws ConnectException {
        convert(inputFilePath, outputFilePath, LOCAL_HOST, LOCAL_PORT);
    }

    /**
     * @param inputFilePath  待转换的文件路径
     * @param outputFilePath 输出文件路径
     * @param connectIp      远程调用ip
     * @param connectPort    远程调用端口
     */
    public static void convert(String inputFilePath, String outputFilePath, String connectIp, int connectPort) {
        if (StringUtil.isEmpty(inputFilePath) || StringUtil.isEmpty(outputFilePath)
                || StringUtil.isEmpty(connectIp)) {
            throw new IllegalArgumentException("参数异常！！");
        }

        OpenOfficeConnection connection = new SocketOpenOfficeConnection(connectIp, connectPort);
        System.out.println("准备连接");
        try{
            connection.connect();
            System.out.println("连接成功");
        }catch(ConnectException e){
            e.printStackTrace();
            System.out.println("连接失败");
        }

        DocumentConverter converter = getConverter(connectIp, connection);
        converter.convert(new File(inputFilePath), new File(outputFilePath));
        connection.disconnect();
    }


    /**
     * @param inputStream
     * @param inputFileExtension  待转换文件的扩展名，例如: xls，doc
     * @param outputStream
     * @param outputFileExtension 输出文件扩展名，例如：pdf
     * @desc
     * @auth josnow
     * @date 2017年6月9日 下午4:08:26
     */
    public static void convert(InputStream inputStream, String inputFileExtension, OutputStream outputStream,
                               String outputFileExtension) throws ConnectException {
        convert(inputStream, inputFileExtension, outputStream, outputFileExtension, LOCAL_HOST, LOCAL_PORT);
    }

    /**
     * @param inputStream
     * @param inputFileExtension  待转换文件的扩展名，例如: xls，doc
     * @param outputStream
     * @param outputFileExtension 输出文件扩展名，例如：pdf
     * @param connectIp           远程调用ip
     * @param connectPort         远程调用端口
     * @desc
     * @auth josnow
     * @date 2017年6月9日 下午4:10:21
     */
    public static void convert(InputStream inputStream, String inputFileExtension, OutputStream outputStream,
                               String outputFileExtension, String connectIp, int connectPort) throws ConnectException {

        if (inputStream == null || StringUtil.isEmpty(inputFileExtension) || outputStream == null
                || StringUtil.isEmpty(outputFileExtension) || StringUtil.isEmpty(connectIp)) {
            throw new IllegalArgumentException("参数异常！！");
        }

        OpenOfficeConnection connection = new SocketOpenOfficeConnection(connectIp, connectPort);
        connection.connect();
        DocumentConverter converter = getConverter(connectIp, connection);

        converter.convert(inputStream, formatFactory.getFormatByFileExtension(inputFileExtension), outputStream,
                formatFactory.getFormatByFileExtension(outputFileExtension));
        connection.disconnect();
    }

    private static DocumentConverter getConverter(String connectIp, OpenOfficeConnection connection) {
        DocumentConverter converter = "localhost".equals(connectIp) || "127.0.0.1".equals(connectIp)
                || "0:0:0:0:0:0:0:1".equals(connectIp) ? new MyDocumentConvert(connection)
                : new StreamOpenOfficeDocumentConverter(connection);
        return converter;
    }

    public static void main(String[] args){
        long start = System.currentTimeMillis();
        String inpath = "/opt/test/test.xlsx";
        String outpath = "/opt/test/test123.pdf";
        try {
            convert(inpath, outpath);
        } catch (ConnectException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();


        System.out.println( (end - start)/1000 + "s , " + (end-start) + "ms");
    }
}
