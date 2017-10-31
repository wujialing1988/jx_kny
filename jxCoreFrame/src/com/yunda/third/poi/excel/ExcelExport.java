package com.yunda.third.poi.excel;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.yunda.frame.common.Page;
/**
 * <li>说明：导出Excel操作类
 * <li>创建人： 张凡
 * <li>创建日期：2015年3月16日
 * <li>成都运达科技股份有限公司
 */
public class ExcelExport {
    
    /**
     * Excel最大行数
     */
    public static final int MAX_ROWS = 65536;
    /**
     * <li>方法说明：导出Excel
     * <li>方法名：exportExcel
     * @param outFileName
     * @param pages
     * @param resp
     * @param patten
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws ScriptException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws NoSuchMethodException
     * <li>创建人： 张凡
     * <li>创建日期：2015年3月16日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    @SuppressWarnings("hiding")
    public static <T> void exportExcel(String outFileName,
        Page<T> pages, HttpServletResponse resp, ColumnPattern[] patten)
            throws NoSuchFieldException, SecurityException, ScriptException,
            IllegalArgumentException, IllegalAccessException, IOException, NoSuchMethodException{
        exportExcel(outFileName, pages, resp, patten, "sheet1");
    }
    
    /**
     * <li>方法说明：执行导出
     * <li>方法名：exportExcel
     * @param outFileName
     * @param pages
     * @param resp
     * @param pattern
     * @param sheetName
     * <li>创建人： 张凡
     * <li>创建日期：2015年3月16日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * @throws SecurityException 
     * @throws NoSuchFieldException 
     * @throws ScriptException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     * @throws IOException 
     * @throws NoSuchMethodException 
     */
    public static <T> void exportExcel(String outFileName,
        Page<T> pages, HttpServletResponse resp, ColumnPattern[] pattern, String sheetName)
            throws NoSuchFieldException, SecurityException, ScriptException, IllegalArgumentException,
            IllegalAccessException, IOException, NoSuchMethodException{
        if(pages.getTotal() == 0){
            resp.getWriter().print("No data can be exported!");
            return;
        }
        
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet(sheetName);
        sheet.createFreezePane(0, 1, 0, 1);
        sheet.setDefaultColumnWidth(15);
        Row row = sheet.createRow(0);
        Cell cell = null;
        CellStyle style = wb.createCellStyle();
        initHeaderCellStyle(style);
        Field field = null;
        List<T> list = pages.getList();
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        Invocable invocable = null;
        Map<String, Field> fields = new HashMap<String, Field>();
        for(int i = 0; i < pattern.length; i++){
            cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(pattern[i].getHeader());
            
            field = list.get(0).getClass().getDeclaredField(pattern[i].getDataIndex());
            field.setAccessible(true);
            fields.put(pattern[i].getDataIndex(), field);
            if(pattern[i].getXconvert() != null){
                engine.eval(pattern[i].getXconvert().replace("function", "function " + pattern[i].getDataIndex()));
                if(invocable == null) invocable = (Invocable)engine;
            }else if(pattern[i].getTranslate() != null){
                engine.eval(getFunction(pattern[i].getDataIndex() , pattern[i].getTranslate()));
                if(invocable == null) invocable = (Invocable)engine;
            }
        }
        Object value = null;
        for(int i = 0; i < list.size() && i < MAX_ROWS; i++){
            row = sheet.createRow(i+1);
            for(int k = 0; k < pattern.length; k++){
                value = fields.get(pattern[k].getDataIndex()).get(list.get(i));
                if(value == null) continue;
                setCellValue(pattern[k], row.createCell(k), invocable, value);
            }
        }
        resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(outFileName, "UTF-8") + ".xls");
        wb.write(resp.getOutputStream());
        wb.close();            
    }
    
    /**
     * <li>方法说明：为HSSFWorkbook添加Sheet
     * <li>方法名：exportExcel
     * @param wb 主要的exl对象
     * @param pages 结果集
     * @param pattern exl中需要展示的字段
     * @param sheetName exl sheet的名称
     * <li>创建人： 林欢
     * <li>创建日期：2016年3月8日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * @throws SecurityException 
     * @throws NoSuchFieldException 
     * @throws ScriptException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     * @throws IOException 
     * @throws NoSuchMethodException 
     */
    @SuppressWarnings("unused")
    private static <T> void addExprotExcelSheet(HSSFWorkbook wb,Page<T> pages, ColumnPattern[] pattern, String sheetName)throws NoSuchFieldException, SecurityException, ScriptException, IllegalArgumentException,
    IllegalAccessException, IOException, NoSuchMethodException{
        Sheet sheet = wb.createSheet(sheetName);
        sheet.createFreezePane(0, 1, 0, 1);
        sheet.setDefaultColumnWidth(15);
        Row row = sheet.createRow(0);
        Cell cell = null;
        CellStyle style = wb.createCellStyle();
        initHeaderCellStyle(style);
        Field field = null;
        List<T> list = pages.getList();
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        Invocable invocable = null;
        Map<String, Field> fields = new HashMap<String, Field>();
        for(int i = 0; i < pattern.length; i++){
            cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(pattern[i].getHeader());
            
            field = list.get(0).getClass().getDeclaredField(pattern[i].getDataIndex());
            field.setAccessible(true);
            fields.put(pattern[i].getDataIndex(), field);
            if(pattern[i].getXconvert() != null){
                engine.eval(pattern[i].getXconvert().replace("function", "function " + pattern[i].getDataIndex()));
                if(invocable == null) invocable = (Invocable)engine;
            }else if(pattern[i].getTranslate() != null){
                engine.eval(getFunction(pattern[i].getDataIndex() , pattern[i].getTranslate()));
                if(invocable == null) invocable = (Invocable)engine;
            }
        }
        Object value = null;
        for(int i = 0; i < list.size() && i < MAX_ROWS; i++){
            row = sheet.createRow(i+1);
            for(int k = 0; k < pattern.length; k++){
                value = fields.get(pattern[k].getDataIndex()).get(list.get(i));
                if(value == null) continue;
                setCellValue(pattern[k], row.createCell(k), invocable, value);
            }
        }
    }
    
    /**
     * <li>方法说明：执行导出
     * <li>方法名：exportExcel
     * <li>创建人： 林欢
     * <li>创建日期：2016年3月8日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * @throws NoSuchMethodException 
     * @throws IOException 
     * @throws IllegalAccessException 
     * @throws ScriptException 
     * @throws NoSuchFieldException 
     * @throws IllegalArgumentException 
     * @throws SecurityException 
     * @throws SecurityException 
     * @throws NoSuchFieldException 
     * @throws ScriptException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     * @throws IOException 
     * @throws NoSuchMethodException 
     * @param outFileName 输出文件的名称
     * @param list 结果集list
     * @param resp response对象
     */
    @SuppressWarnings("unchecked")
    public static <T> void exportExcel(List<ExportExcelDTO> list,String outFileName,HttpServletResponse resp) throws SecurityException, IllegalArgumentException, NoSuchFieldException, ScriptException, IllegalAccessException, IOException, NoSuchMethodException{
        
        HSSFWorkbook wb = new HSSFWorkbook();
        for (ExportExcelDTO excelDTO : list) {
            
            if(excelDTO.getPages().getTotal() == 0){
                resp.getWriter().print("No data can be exported!");
                return;
            }
            addExprotExcelSheet(wb, excelDTO.getPages(), excelDTO.getPattern(), excelDTO.getSheetName());
        }
        resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(outFileName, "UTF-8") + ".xls");
        wb.write(resp.getOutputStream());
        wb.close();
        
    }

    /**
     * <li>方法说明：设置单元格值
     * <li>方法名：setCellValue
     * @param pattern
     * @param cell
     * @param invocable
     * @param value
     * <li>创建人： 张凡
     * <li>创建日期：2015年3月16日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * @throws ScriptException 
     * @throws NoSuchMethodException 
     */
    private static void setCellValue(ColumnPattern pattern, Cell cell, Invocable invocable, Object value) throws NoSuchMethodException, ScriptException {
        if(value instanceof Date){
            if(pattern.getDatePattern() == null)
                cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(value));
            else
                cell.setCellValue(new SimpleDateFormat(pattern.getDatePattern()).format(value));
        }else{
            if(pattern.getTranslate() != null || pattern.getXconvert() != null){
                cell.setCellValue(invocable.invokeFunction(pattern.getDataIndex(), value).toString());
            }else{
                cell.setCellValue(value.toString());
            }
        }
    }
    
    /**
     * <li>方法说明：获取翻译执行函数体
     * <li>方法名：getFunction
     * <li>创建人： 张凡
     * <li>创建日期：2015年3月16日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * @param functionName
     * @param translate
     * @return String
     */
    private static String getFunction(String functionName, String[] translate){
        StringBuilder sb = new StringBuilder("function ");
        sb.append(functionName);
        sb.append("(v){");
        for(int i = 0; i < translate.length; i++){
            sb.append("if(v == '" + translate[i++] + "') return '" + translate[i] + "'; ");
        }
        sb.append("return '';}");
        return sb.toString();
    }
    
    /**
     * <li>方法说明：Header单元格样式定义
     * <li>方法名：initHeaderCellStyle
     * @param style
     * <li>创建人： 张凡
     * <li>创建日期：2015年3月16日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    private static void initHeaderCellStyle(CellStyle style) {
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    }
    
    
    /**
     * <li>方法说明：自定义动态导出（map封装对象）
     * <li>方法名：exportExcel
     * <li>创建人： 林欢
     * <li>创建日期：2016年3月8日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * @param outFileName 输出文件的名称
     * @param list 结果集list
     * @param resp response对象
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws NoSuchFieldException
     * @throws ScriptException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws NoSuchMethodException
     */
    @SuppressWarnings("unchecked")
    public static void synExportExcel(List<ExportExcelDTO> list,String outFileName,HttpServletResponse resp) throws SecurityException, IllegalArgumentException, NoSuchFieldException, ScriptException, IllegalAccessException, IOException, NoSuchMethodException{
        
        HSSFWorkbook wb = new HSSFWorkbook();
        
        //判断是否有数据需要导出
        if (list != null && list.size() > 0) {
            for (ExportExcelDTO excelDTO : list) {
                createSheet(wb,excelDTO);
            }
        }
        
        resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(outFileName, "UTF-8") + ".xls");
        wb.write(resp.getOutputStream());
        wb.close();
        
    }

    /**
     * <li>说明：创建sheet
     * <li>创建人：林欢
     * <li>创建日期：2016-8-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param wb exl对象
     * @param excelDTO 封装好的数据和表头
     */
    @SuppressWarnings("unchecked")
    private static void createSheet(HSSFWorkbook wb,ExportExcelDTO excelDTO) {
        
        Page<Map<String, String>> pages = excelDTO.getPages();//导出数据
        ColumnPattern[] pattern = excelDTO.getPattern();//
        String sheetName = excelDTO.getSheetName();//sheet名称
        
        //创建sheet对象
        Sheet sheet = wb.createSheet(sheetName);
        sheet.createFreezePane(0, 1, 0, 1);
        sheet.setDefaultColumnWidth(15);
        //创建第一行（表头）
        Row row = sheet.createRow(0);
        Cell cell = null;
        //设置单元格样式
        CellStyle style = wb.createCellStyle();
        initHeaderCellStyle(style);
        //数据list
        List<Map<String, String>> list = pages.getList();
        //循环设置表头
        for(int i = 0; i < pattern.length; i++){
            cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(pattern[i].getHeader());
        }
        
        //循环设置表格值
        for(int i = 0; i < list.size() && i < MAX_ROWS; i++){
            Map<String, String> dataMap = list.get(i);
            //从第二行开始
            row = sheet.createRow(i+1);
            for(int k = 0; k < pattern.length; k++){
                //如果不是第一列，显示具体数据
                row.createCell(k).setCellValue(dataMap.get(pattern[k].getDataIndex()));
            }
        }
    }

}
