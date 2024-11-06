package com.teenet.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.teenet.handler.TitleHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @Description 来源网络
 * @Author threedong
 * @Date 2020/11/13 15:56
 */
public class ExcelUtil {

    public static <T> AnalysisEventListener<T> getListen(Consumer<List<T>> consumer, int threshold) {

        return new AnalysisEventListener<T>() {
            private LinkedList<T> linkedList = new LinkedList<>();

            @Override
            public void invoke(T t, AnalysisContext analysisContext) {
                linkedList.add(t);
                if (linkedList.size() == threshold) {
                    consumer.accept(linkedList);
                    linkedList.clear();
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                if (linkedList.size() > 0) {
                    consumer.accept(linkedList);
                }
            }
        };
    }

    public static <T> AnalysisEventListener<T> getListen(Consumer<List<T>> consumer) {
        return getListen(consumer, 10);
    }


    public static void exportExcelByTemplate(HttpServletResponse response,
                                             String templateFileName,
                                             List data,
                                             InputStream inputStream) {
        try (
                OutputStream out = response.getOutputStream();
                BufferedOutputStream bos = new BufferedOutputStream(out)) {

            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment; filename= " + new String(templateFileName.getBytes("utf-8"), "iso-8859-1") + ".xlsx");

            //读取Excel
            ExcelWriter excelWriter = EasyExcel.write(bos).withTemplate(inputStream).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            excelWriter.fill(data, writeSheet);
            excelWriter.finish();
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void templateWithPullDown(HttpServletResponse response,
                                            String templateFileName,
                                            Map<Integer, String[]> dropDownMap,
                                            Map<String, List<String>> dropDownMap2,
                                            InputStream inputStream,
                                            Collection<?> data,
                                            Integer rowSize) {
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.setDateHeader("Expires", -1);
            //设置响应头部信息，格式为附件，以及文件名
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + templateFileName + ".xlsx");
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(inputStream).build();
            WriteSheet writeSheet = EasyExcel.writerSheet(0, templateFileName)
                    .registerWriteHandler(new TitleHandler(dropDownMap, dropDownMap2, rowSize))
                    .build();
            excelWriter.fill(data, writeSheet);
            excelWriter.finish();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
