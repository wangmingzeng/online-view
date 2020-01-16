package com.zach.biz.view.controller;

import com.zach.biz.view.poi.OpenOfficeUtils;
import com.zach.biz.view.poi.POIReadExcel;
import com.zach.biz.view.poi.Transform;
import com.zach.biz.view.utils.StringUtil;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.ConnectException;

@RestController
@RequestMapping("/test")
public class TestController {

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    @RequestMapping(value="/do", method = {RequestMethod.GET})
    public String convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long start = System.currentTimeMillis();
        String inpath = "/opt/test/test.xlsx";
        String outpath = "/opt/test/test123.pdf";
        if(StringUtil.isNotEmpty(request.getParameter("inpath")) ){
            inpath = request.getParameter("inpath");
        }
        if(StringUtil.isNotEmpty(request.getParameter("outpath")) ){
            outpath = request.getParameter("outpath");
        }
        try {
            OpenOfficeUtils util = new OpenOfficeUtils();
            util.convert(inpath, outpath);
        } catch (ConnectException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();

        String str = (end - start)/1000 + "s , " + (end-start) + "ms";
        System.out.println( str );
        return str;
    }

    @RequestMapping(value="/show", method = {RequestMethod.GET})
    public void show(HttpServletResponse response) throws IOException {
        try {
            System.out.println("1235");
            HSSFWorkbook wb = new HSSFWorkbook();
            File file = new File("/Users/zengwangming/downloads/test.xlsx");
            if(file.getPath().endsWith(EXCEL_XLS)){
                wb = new HSSFWorkbook(new FileInputStream(file));
            }else if(file.getPath().endsWith(EXCEL_XLSX)){;
                XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
                Transform transform = new Transform();
                transform.transformXSSF(workbook, wb);
            }

            ExcelToHtmlConverter ethc = new ExcelToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
            ethc.setOutputColumnHeaders(false);
            ethc.setOutputRowNumbers(false);
            ethc.processWorkbook(wb);

            Document htmlDocument = ethc.getDocument();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(out);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
            out.close();

            String htmlStr = new String(out.toByteArray(), "UTF-8");

            response.setContentType("text/html;charset=utf-8");
            PrintWriter pw = response.getWriter();
            pw.print(htmlStr);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value="/view", method = {RequestMethod.GET})
    public void view(HttpServletResponse response) throws IOException {
        String path = "/Users/zengwangming/downloads/test.xlsx";
        String htmlStr = POIReadExcel.excelWriteToHtml(path);


        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = response.getWriter();
        pw.print(htmlStr);
        pw.flush();
        pw.close();

    }
}