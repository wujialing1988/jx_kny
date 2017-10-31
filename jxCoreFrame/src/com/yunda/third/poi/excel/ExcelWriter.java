package com.yunda.third.poi.excel;

import java.io.*;
import java.util.*;
import org.apache.poi.hssf.usermodel.*;
/**
 * <p>Title: xp 组件库</p>
 *
 * <p>Description: 对Excel文件写取操作的类，在写操作完毕后应该调用执行该类的close()方法释放资源。
 * 推荐使用一个Excel模板文件实例化该类，可以根据模板随意的修改样式及数据位置。</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: L. YF</p>
 *
 * @author liuxb
 * @version 1.0
 */
public class ExcelWriter extends ExcelLoader {

    /**
     * 构造方法：根据文件路径装载Excel文件模板，该路径应该是绝对路径
     * @param fileUrl String
     * @throws Exception
     */
    public ExcelWriter(String fileUrl) throws Exception {
        super(fileUrl);
    }
    /**
     * 构造方法：从输入流装载Excel文件对象
     * @param input InputStream
     * @throws Exception
     */
    public ExcelWriter(InputStream input) throws Exception {
        super(input);
    }
    /**
     * 构造方法：使用Excel文件对象实例化该类
     * @param workbook HSSFWorkbook
     */
    public ExcelWriter(HSSFWorkbook workbook) throws Exception {
        super(workbook);
    }
    /**
     * 根据数据模型在指定名称的工作薄上构建Excel数据
     * @param model Map 数据模型
     * @param sheetName String 工作薄名称　
     */
    @SuppressWarnings("unchecked")
	public void buildExcelDocument(Map model, String sheetName) throws Exception {
        buildExcelDocument(model, super.workbook.getSheet(sheetName));
    }
    /**
     * 根据数据模型在指定名称的工作薄上构建Excel数据，工作薄索引从０开始
     * @param model Map 数据模型
     * @param sheetIndex int 工作薄名称
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public void buildExcelDocument(Map model, int sheetIndex) throws Exception {
        buildExcelDocument(model, super.workbook.getSheetAt(sheetIndex));
    }
    /**
     * 根据数据模型构建Excel数据
     * @param model Map 数据模型
     * @param wb HSSFWorkbook 工作薄名称
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	private void buildExcelDocument(Map model, HSSFSheet sheet) throws Exception {
      List modelList = ExcelUtil.parseExcelCoor(model);
      List dynamicDataList = new ArrayList();
      int size = modelList.size();
      for (int i = 0; i < size; i++) {
        Coordinate coor = (Coordinate)modelList.get(i);
        Object value = coor.getValue();
        if (value instanceof Object[][]) {
          dynamicDataList.add(coor);
          continue;
        }
        if (value instanceof Object[]) {
          ExcelUtil.setRow(sheet, coor, null);
        } else {
          HSSFCell cell = sheet.getRow(coor.getY()).getCell((short)coor.getX());
          if (null != coor.getValue()) {
            ExcelUtil.setCell(cell, null, coor.getValue().toString());
          }
        }
      }
      /** 插入动态的一唯或二维数据（一维、二维数组） */
      if (dynamicDataList.size() > 0) {
        Collections.sort(dynamicDataList, new CoordinateRowComp());
        size = dynamicDataList.size();
        for ( int i = size - 1; i >= 0; i-- ) {
          Coordinate coor = (Coordinate)dynamicDataList.get(i);
          Object[][] dataList = (Object[][])coor.getValue();
          int offset = dataList.length - 1;
          if ( offset != 0 ) {
              //插入动态的二维数组，需要向后移动单位格
              sheet.shiftRows(coor.getY() + 1, sheet.getLastRowNum(), offset, true, false);
          }

          HSSFCellStyle style = sheet.getRow(coor.getY()).getCell((short)0).getCellStyle();
          ExcelUtil.setRow(sheet, style, coor.getY(), dataList);
        }
      }
  }
  /**
   * 将Excel文件对象写入输出流中，此方法不关闭输出流
   * @param out OutputStream
   * @throws IOException
   */
  public void write(OutputStream out) throws IOException {
	  if(out != null)	super.workbook.write(out);
//      out.write(super.workbook.getBytes());
  }
  /**
   *  将Excel文件对象写入输出流中，并关闭该输出流
   * @param out OutputStream
   * @throws IOException
   */
  public void writeAndClose(OutputStream out) throws IOException {
	  if(out != null)	super.workbook.write(out);
//      out.write(super.workbook.getBytes());
      out.close();
  }
}

/**
 *
 * <p>Title: liuxb 组件库</p>
 *
 * <p>Description: 该类实现了Coordinate的比较器</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: L. YF</p>
 *
 * @author liuxb
 * @version 1.0
 */
@SuppressWarnings("unchecked")
class CoordinateRowComp implements Comparator {
  /**
   * Compares its two arguments for order.
   *
   * @param o1 the first object to be compared.
   * @param o2 the second object to be compared.
   * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater
   *   than the second.
   *
   */
  public int compare(Object o1, Object o2) {
    Coordinate c1 = (Coordinate)o1;
    Coordinate c2 = (Coordinate)o2;
    if (c1.getY() == c2.getY()) {
      return 0;
    } else {
      return c1.getY() < c2.getY() ? -1 : 1;
    }
  }
}
