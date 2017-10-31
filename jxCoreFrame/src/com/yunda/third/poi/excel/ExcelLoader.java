package com.yunda.third.poi.excel;

import java.io.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * <p>Title: xp 组件库</p>
 *
 * <p>Description: Excel文件的I0操作抽象类</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: L. YF</p>
 *
 * @author liuxb
 * @version 1.0
 */
public abstract class ExcelLoader {
    protected HSSFWorkbook workbook;
    protected FileInputStream input;
    /**
     * 构造方法：根据文件路径装载Excel文件对象，该路径应该是绝对路径
     * @param fileUrl String
     * @throws Exception
     */
    public ExcelLoader(String fileUrl) throws Exception {
        load(fileUrl);
    }
    /**
     * 构造方法：从输入流装载Excel文件对象
     * @param input InputStream
     * @throws Exception
     */
    public ExcelLoader(InputStream input) throws Exception {
        load(input);
    }
    /**
     * 构造方法：使用参数对象实例化Excel文件对象
     * @param workbook HSSFWorkbook
     */
    public ExcelLoader(HSSFWorkbook workbook) throws Exception {
        if (workbook != null) {
            this.workbook = workbook;
        } else {
            throw new Exception("参数对象为空，Excel文件对象不能被实例化。");
        }
    }
    /**
     * 根据文件路径装载Excel文件对象，该路径应该是绝对路径
     * @param fileUrl String
     * @throws Exception
     */
    private void load(String fileUrl) throws Exception {
        try {
            this.input = new FileInputStream(fileUrl);
            load(this.input);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            throw new Exception( "没有找到文件！" );
        }
    }
    /**
     * 从输入流装载Excel文件对象
     * @param input InputStream
     * @throws Exception
     */
    private void load(InputStream inputStream) throws Exception {
        //修改 2015-02-02 汪东良 将input 局部变量修改成inputStream开头，保证与属性变量命名不同。
        try {
            POIFSFileSystem file = new POIFSFileSystem(inputStream);
            this.workbook = new HSSFWorkbook(file);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new Exception("读取文件错误！");
        }
    }
    /**
     * 关闭输入流
     */
    public void close() {
        try {
            this.workbook = null;
            if (this.input != null) {
                this.input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 当JVM销毁该对象之前调用关闭输入流
     */
    public void finalize() {
        this.close();
    }
}
