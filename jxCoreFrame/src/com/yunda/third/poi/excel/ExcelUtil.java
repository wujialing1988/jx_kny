package com.yunda.third.poi.excel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.yunda.frame.util.StringUtil;

/**
 * <p>Title: xp 组件库</p>
 * <p>Description: 一些常用的开发组件</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: L. YF</p>
 * @author liuxb
 * @version 1.0
 */
public class ExcelUtil {
    /** 默认的日期格式 */
    private static final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

    private ExcelUtil() {}

    /**
     * 设置单元格内容及字符编码
     * @param cell HSSFCell
     * @param cellVal String
     */
    public static void setCell(HSSFCell cell, HSSFCellStyle style, String cellVal) {
      //设置单元格的字符编码
      //poi3.0版本默认UTF-16，解决了中文乱码问题
//      cell.setEncoding(HSSFWorkbook.ENCODING_UTF_16);
      // 定义单元格为字符串类型
      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
      //填写单元格内容
      //poi3.0版本推荐采用这个方法
      cell.setCellValue(new HSSFRichTextString(StringUtil.nvlTrim(cellVal)));
//      cell.setCellValue(cellVal);
      if (null != style) {
        cell.setCellStyle(style);
      }
    }

    /**
     * 设置一行数据,根据Coordinate
     * @param row HSSFRow
     * @param rowVal Object[]
     */
    public static void setRow(HSSFSheet sheet, Coordinate coor, HSSFCellStyle style) {
        Object[] rowVal = (Object[])coor.getValue();
        if (null == rowVal || rowVal.length < 1)	return;
        int colIndex = coor.getX();
        HSSFRow row = sheet.getRow(coor.getY()) == null ? sheet.createRow(coor.getY()) : sheet.getRow(coor.getY());
        style = style == null ? row.getCell((short)colIndex).getCellStyle() : style;
        for (short index = 0; index < rowVal.length; index++) {
          HSSFCell cell = row.getCell((short)colIndex++);
          if (cell == null) {
              row.createCell((short)colIndex++);
          }
          setCell(cell, style, rowVal[index].toString());
      }
    }
    /**
     * 设置一行数据
     * @param row
     * @param style
     * @param rowVal
     */
    public static void setRow(HSSFRow row, HSSFCellStyle style, Object[] rowVal) {
      if (null == rowVal || rowVal.length < 1 || row == null)	return;
      for (short index = 0; index < rowVal.length; index++) {
        HSSFCell cell = row.getCell(index) == null ? row.createCell(index) : row.getCell(index);
        setCell(cell, style, rowVal[index].toString());
      }
    }
    /**
     * 设置表的内容
     * @param sheet HSSFSheet
     * @param index int
     * @param tableVal Object[][]
     */
    public static int setRow(HSSFSheet sheet, HSSFCellStyle style, int rowIndex, Object[][] tableValues) {
      if (null == sheet || tableValues == null || tableValues.length < 1)	return rowIndex;
      for (int i = 0; i < tableValues.length; i++) {
        setRow(sheet.createRow(rowIndex++), style, tableValues[i]);
      }
      return rowIndex;
    }

    /**
     * 根据参数数组返回Map, Map是Coordinate对象构成的集合
     * 目前只适用于Excel模版中的参数
     * @param params String[]
     * @return Map
     */
    @SuppressWarnings("unchecked")
	public static Map parseParams(String[] params) {
      if (params == null || params.length < 1)	return null;
      int length = params.length;
      Map map = new HashMap();
      for (int i = 0; i < length; i++) {
        int index1 = params[i].indexOf("-");
        int index2 = params[i].indexOf(",");
        if (index1 != -1 && index2 != -1) {
          String value = params[i].substring(0, index1);
          String x = params[i].substring(index2 - 1, index2);
          String y = params[i].substring(index2 + 1);
          map.put(value, new Coordinate(Integer.parseInt(x), Integer.parseInt(y), value));
        }
      }
      return map;
    }

    /**
     * 根据参数数组返回Map, Map是Coordinate对象构成的集合
     * 目前只适用于Excel模版中的参数
     * @param params String
     * @return Map
     */
    @SuppressWarnings("unchecked")
	public static Map parseParams(String params) {
      return parseParams(params.split(";"));
    }

    /**
     * 解析数据模型，返回坐标对象
     * @param coor String
     * @return Coordinate
     */
    @SuppressWarnings("unchecked")
	public static List parseExcelCoor(Map model) {
      List list = new ArrayList();
      Set keySet = model.keySet();
      for (Iterator iter = keySet.iterator(); iter.hasNext(); ) {
        String item = iter.next().toString();
        Coordinate c = Coordinate.parseExcelCoor(item);
        c.setValue(model.get(item));
        list.add(c);
      }
      return list;
    }

    /**
     * 数据填充单元格
     * @param cell HSSFCell
     * @param key String
     * @param map Map
     */
    @SuppressWarnings("unchecked")
	public void setCell(HSSFCell cell, String key, Map map) {
      Object value = map.get(key);
      value = (null == value ) ? "  " : value;
      setCell(cell, null, value.toString() );
    }


    /**
     * 获取Excel指定工作薄中某个单元格的值,coordinate为起始单元格坐标
     * @param sheet HSSFSheet
     * @param coordinate String
     * @return String
     */
    public static String getCellValue(HSSFSheet sheet, String coordinate) {
      Coordinate coor = Coordinate.parseExcelCoor(coordinate);
      return getCellValue(sheet, coor.getY(), (short)coor.getX());
    }
    /**
     * 获取Excel指定工作薄中,根据xy坐标，将该行的数据存入一个数组返回，coordinate为起始单元格坐标
     * @param sheet HSSFSheet
     * @param coordinate String
     * @return String[]
     */
    public static String[] getRowValue(HSSFSheet sheet, String coordinate) {
      Coordinate coor = Coordinate.parseExcelCoor(coordinate);
      return getRowValue(sheet, coor.getY(), (short)coor.getX());
    }
    /**
     * 根据xy坐标，从该行一直到最后行的数据存入一个二维数组返回，coordinate为起始单元格坐标
     * @param sheet HSSFSheet
     * @param coordinate String
     * @return String[][]
     */
    public static String[][] getTableValue(HSSFSheet sheet, String coordinate) {
        Coordinate coor = Coordinate.parseExcelCoor(coordinate);
        return getTableValue(sheet, coor.getY(), (short)coor.getX());
    }
    /**
     * 根据xy坐标，从该行一直到最后行的数据存入一个二维数组返回，coordinate为起始单元格坐标
     * 读取Excel返回的数组可能长度不一，强制将每行数组长度扩展指定的列宽colWidth，只当该行数组长度小于colWidth时有效
     * @param sheet
     * @param coordinate 参见Excel左上角，如：C3
     * @param colWidth 指定列宽
     * @return
     */
    public static String[][] getTableValue(HSSFSheet sheet, String coordinate, int colWidth) {
        Coordinate coor = Coordinate.parseExcelCoor(coordinate);
        return getTableValue(sheet, coor.getY(), (short)coor.getX(), colWidth);
    }    
    /**
     * 根据xy坐标，从该行一直到最后行的数据存入一个map数组，xy为起始单元格坐标
     * xy为起始坐标，该起始行应该是列头标题，并且列标题做为map的键值
     * @param sheet HSSFSheet
     * @param coordinate String
     * @return Map[]
     */
    @SuppressWarnings("unchecked")
	public static Map[] getMapValue(HSSFSheet sheet, String coordinate) {
        Coordinate coor = Coordinate.parseExcelCoor(coordinate);
        return getMapValue(sheet, coor.getY(), (short)coor.getX());
    }
    /**
     * 根据xy坐标获得该单元格的值
     * @param sheet HSSFSheet
     * @param x int
     * @param y int
     * @return String
     */
    public static String getCellValue(HSSFSheet sheet, int x, short y) {
      HSSFRow row = sheet.getRow(x);
      if (row == null) {
        return "";
      }
      return getCellValue(row.getCell((short)y));
    }
    /**
     * 根据xy坐标，将该行的数据存入一个数组返回，xy为起始单元格坐标
     * @param sheet HSSFSheet
     * @param x int
     * @param y int
     * @return String[]
     */
    @SuppressWarnings("unchecked")
	public static String[] getRowValue(HSSFSheet sheet, int x, short y) {
      HSSFRow row = sheet.getRow(x);
      if(row == null)	return null;
      short lastNum = row.getLastCellNum();
      List values = new ArrayList();
      for ( short startNum = (short)y; startNum <= lastNum; startNum++) {
          values.add(getCellValue(row.getCell(startNum)));
      }
      String[] rowValue = null;
      if (null != values && values.size() > 0) {
          rowValue = new String[ values.size() ];
          values.toArray(rowValue);
      }
      return rowValue;
    }
    /**
     * 根据xy坐标，将该行的数据存入一个数组返回，xy为起始单元格坐标
     * 读取Excel返回的数组可能长度不一，强制将每行数组长度扩展指定的列宽colWidth，只当该行数组长度小于colWidth时有效
     * @param sheet HSSFSheet
     * @param x int
     * @param y int
     * @param colWidth int 
     * @return String[]
     */
    @SuppressWarnings("unchecked")
	public static String[] getRowValue(HSSFSheet sheet, int x, short y, int colWidth) {
      HSSFRow row = sheet.getRow(x);
      if(row == null)	return new String[ colWidth ];
      short lastNum = row.getLastCellNum();
      if(lastNum < colWidth)	lastNum = (short)colWidth;
      List values = new ArrayList();
      for ( short startNum = (short)y; startNum <= lastNum; startNum++) {
          values.add(getCellValue(row.getCell(startNum)));
      }
      String[] rowValue = null;
      if (null != values && values.size() > 0) {
          rowValue = new String[ values.size() ];
          values.toArray(rowValue);
      }
      return rowValue;
    }    
    /**
     * 根据xy坐标，从该行一直到最后行的数据存入一个二维数组返回，xy为起始单元格坐标
     * @param sheet HSSFSheet
     * @param x int
     * @param y int
     * @return String[]
     */
    @SuppressWarnings("unchecked")
	public static String[][] getTableValue(HSSFSheet sheet, int x, short y) {
      int startRowNum = x;
      int lastRowNum = sheet.getLastRowNum();
      List values = new ArrayList();
      for(; startRowNum <= lastRowNum; startRowNum++) {
         values.add(getRowValue(sheet, startRowNum, y));
      }
      String[][] tableValue = null;
      if (null != values && values.size() > 0) {
          tableValue = new String[ values.size() ][];
          values.toArray(tableValue);
      }
      return tableValue;
    }
    /**
     * 根据xy坐标，从该行一直到最后行的数据存入一个二维数组返回，xy为起始单元格坐标
     * 读取Excel返回的数组可能长度不一，强制将每行数组长度扩展指定的列宽colWidth，只当该行数组长度小于colWidth时有效
     * @param sheet HSSFSheet
     * @param x int
     * @param y int
     * @param colWidth int 指定列宽
     * @return String[]
     */
    @SuppressWarnings("unchecked")
	public static String[][] getTableValue(HSSFSheet sheet, int x, short y, int colWidth) {
      int startRowNum = x;
      int lastRowNum = sheet.getLastRowNum();
      List values = new ArrayList();
      for(; startRowNum <= lastRowNum; startRowNum++) {
         values.add(getRowValue(sheet, startRowNum, y, colWidth));
      }
      String[][] tableValue = null;
      if (null != values && values.size() > 0) {
          tableValue = new String[ values.size() ][];
          values.toArray(tableValue);
      }
      return tableValue;
    }    
    /**
     * 根据xy坐标，从该行一直到最后行的数据存入一个map数组，xy为起始单元格坐标
     * xy为起始坐标，该起始行应该是列头标题，并且列标题做为map的键值
     * @param sheet HSSFSheet
     * @param x int
     * @param y short
     * @return Map[]
     */
    @SuppressWarnings("unchecked")
	public static Map[] getMapValue(HSSFSheet sheet, int x, short y) {
      //获取列标题头
      String[] colsHead = getRowValue(sheet, x, y);
      if (null == colsHead || colsHead.length < 1)	return null;
      int start = x + 1;
      int last = sheet.getLastRowNum();
      List values = new ArrayList();
      for(; start <= last; start++) {
         String[] bodys = getRowValue(sheet, start, y);
         if (bodys != null && bodys.length <= colsHead.length) {
             Map map = new HashMap();
             for (int i = 0; i < bodys.length; i++) {
                 //使用列标题头作为键值，压入数据
                 map.put(StringUtil.nvlTrim(colsHead[ i ]), bodys[ i ]);
             }
             values.add(map);
         }
      }
      Map[] mapValues = null;
      if (null != values && values.size() > 0) {
          mapValues = new Map[ values.size() ];
          values.toArray(mapValues);
      }
      return mapValues;
    }
    /**
     * 根据单元格值域类型返回相应的值
     * @param cell HSSFCell
     * @return String
     */
    public static String getCellValue(HSSFCell cell) {
      String value = null;
      try {
        if (cell != null) {
          switch (cell.getCellType()) {
            //数字型
            case HSSFCell.CELL_TYPE_NUMERIC:
                //如果单元格的值是日期型的话，返回日期字符串
              if (HSSFDateUtil.isCellDateFormatted(cell)) {
                  value = dateformat.format(cell.getDateCellValue());
              } else {
            	  //value = String.valueOf(cell.getNumericCellValue());
	        	  	/*
	        	     * <li>说明：修改出现大数据时使用POI读取出现科学计数的问题, 如5.0E7
	        	     * <li>修改人： 袁健
	        	     * <li>修改日期：2013-12-26
	        	     */
            	  value = new DecimalFormat("0").format(cell.getNumericCellValue());
              }
              break;
            //字符型
            case HSSFCell.CELL_TYPE_STRING:
              value = cell.getRichStringCellValue().getString();
              break;
            //公式
            case HSSFCell.CELL_TYPE_FORMULA:
//              value = cell.getCellFormula();
             	//当单元格为公式时，使用上面的方法不能获取公式计算后的值，并且会抛出异常
            	//当单元格为公式时，按照数字型方式处理
	            //如果单元格的值是日期型的话，返回日期字符串
	            if (HSSFDateUtil.isCellDateFormatted(cell)) {
	                value = dateformat.format(cell.getDateCellValue());
	            } else {
	            	
	                value = String.valueOf(cell.getNumericCellValue());
	            }            
	            break;
            //空白
            case HSSFCell.CELL_TYPE_BLANK:
              break;
            //布尔型
            case HSSFCell.CELL_TYPE_BOOLEAN:
              value = String.valueOf(cell.getBooleanCellValue());
              break;
            //错误的类型
            case HSSFCell.CELL_TYPE_ERROR:
              throw new RuntimeException("获取的单元格数据类型错误。");
            default:
          }
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      return value;
  }
}
