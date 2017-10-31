package com.yunda.third.poi.excel;


/**
 * <p>Title: xp 组件库</p>
 *
 * <p>Description: 对Excel文件读取操作的类，在读操作完毕后应该调用执行该类的close()方法释放资</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: L. YF</p>
 *
 * @author liuxb
 * @version 1.0
 */
import java.io.InputStream;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelReader extends ExcelLoader{
    /**
     * 构造方法：根据文件路径装载Excel文件对象，该路径应该是绝对路径
     * @param fileUrl String
     * @throws Exception
     */
    public ExcelReader(String fileUrl) throws Exception {
        super(fileUrl);
    }
    /**
     * 构造方法：从输入流装载Excel文件对象
     * @param input InputStream
     * @throws Exception
     */
    public ExcelReader(InputStream input) throws Exception {
        super(input);
    }
    /**
     * 构造方法：使用参数对象实例化Excel文件对象
     * @param workbook HSSFWorkbook
     */
    public ExcelReader(HSSFWorkbook workbook) throws Exception {
        super(workbook);
    }

    /**
     * 获取Excel指定工作薄中某个单元格的值,coordinate为单元格坐标
     * @param sheet HSSFSheet
     * @param coordinate String 参见Excel左上角，如：C3
     * @return String
     */
    public String getCellValue(String sheetName, String coordinate) {
      return ExcelUtil.getCellValue(super.workbook.getSheet(sheetName), coordinate);
    }
    /**
     * 获取Excel指定工作薄中,根据xy坐标，将该行的数据存入一个数组返回，coordinate为起始单元格坐标
     * @param sheet HSSFSheet
     * @param coordinate String 参见Excel左上角，如：C3
     * @return String[]
     */
    public String[] getRowValue(String sheetName, String coordinate) {
      return ExcelUtil.getRowValue(super.workbook.getSheet(sheetName), coordinate);
    }
    /**
     * 根据xy坐标，读取从该行一直到最后行的数据存入一个二维数组返回，coordinate为起始单元格坐标
     * @param sheet HSSFSheet
     * @param coordinate String 参见Excel左上角，如：C3
     * @return String[][]
     */
    public String[][] getTableValue(String sheetName, String coordinate) {
      return ExcelUtil.getTableValue(super.workbook.getSheet(sheetName), coordinate);
    }
    /**
     * 根据xy坐标，读取从该行一直到最后行的数据存入一个二维数组返回，coordinate为起始单元格坐标
     * 读取Excel返回的数组可能长度不一，强制将每行数组长度扩展指定的列宽colWidth，只当该行数组长度小于colWidth时有效
     * @param sheetName 
     * @param coordinate 参见Excel左上角，如：C3
     * @param colWidth 指定列宽
     * @return
     */
    public String[][] getTableValue(String sheetName, String coordinate, int colWidth) {
      return ExcelUtil.getTableValue(super.workbook.getSheet(sheetName), coordinate, colWidth);
    }        
    /**
     * 根据xy坐标，读取从该行一直到最后行的数据存入一个map数组，xy为起始单元格坐标
     * xy为起始坐标，该起始行应该是列头标题，并且列标题做为map的键值
     * @param sheetName String
     * @param coordinate String 参见Excel左上角，如：C3
     * @return Map[]
     */
    @SuppressWarnings("unchecked")
	public Map[] getMapValue(String sheetName, String coordinate) {
      return ExcelUtil.getMapValue(super.workbook.getSheet(sheetName), coordinate);
    }
    /**
     * 获取Excel指定工作薄中某个单元格的值,coordinate为起始单元格坐标
     * @param sheetIndex int
     * @param coordinate String 参见Excel左上角，如：C3
     * @return String
     */
    public String getCellValue(int sheetIndex, String coordinate) {
      return ExcelUtil.getCellValue(super.workbook.getSheetAt(sheetIndex), coordinate);
    }
    /**
     * 根据xy坐标，读取从该行一直到最后行的数据存入一个二维数组返回，coordinate为起始单元格坐标
     * 读取Excel返回的数组可能长度不一，强制将每行数组长度扩展指定的列宽colWidth，只当该行数组长度小于colWidth时有效
     * @param sheetIndex int
     * @param coordinate 参见Excel左上角，如：C3
     * @param colWidth 指定列宽
     * @return
     */
    public String[][] getTableValue(int sheetIndex, String coordinate, int colWidth) {
      return ExcelUtil.getTableValue(super.workbook.getSheetAt(sheetIndex), coordinate, colWidth);
    }      
    /**
     * 获取Excel指定工作薄中,根据xy坐标，读取将该行的数据存入一个数组返回，coordinate为起始单元格坐标
     * @param sheetIndex int
     * @param coordinate String 参见Excel左上角，如：C3
     * @return String[]
     */
    public String[] getRowValue(int sheetIndex, String coordinate) {
      return ExcelUtil.getRowValue(super.workbook.getSheetAt(sheetIndex), coordinate);
    }
    /**
     * 根据xy坐标，读取从该行一直到最后行的数据存入一个二维数组返回，coordinate为起始单元格坐标
     * @param sheetIndex int
     * @param coordinate String 参见Excel左上角，如：C3
     * @return String[][]
     */
    public String[][] getTableValue(int sheetIndex, String coordinate) {
      return ExcelUtil.getTableValue(super.workbook.getSheetAt(sheetIndex), coordinate);
    }
    /**
     * 根据xy坐标，读取从该行一直到最后行的数据存入一个map数组，xy为起始单元格坐标
     * xy为起始坐标，该起始行应该是列头标题，并且列标题做为map的键值
     * @param sheetIndex int
     * @param coordinate String 参见Excel左上角，如：C3
     * @return Map[]
     */
    @SuppressWarnings("unchecked")
	public Map[] getMapValue(int sheetIndex, String coordinate) {
      return ExcelUtil.getMapValue(super.workbook.getSheetAt(sheetIndex), coordinate);
    }

    /**
     * 测试方法
     * @param args String[]
     */
	public static void main(String[] args) {
        try {
//        	ExcelReader excel = new ExcelReader("D:\\工程系统\\葛洲坝右岸坝肩以上开挖工程09年12月计量(右岸）2.xls");
//        	String[][] datas = excel.getTableValue("中间计量汇总表(支7)", "A1");
//        	if (datas != null && datas.length > 0) {
//				for (int i = 0; i < datas.length; i++) {
//					for (int j = 0; j < datas[ i ].length; j++) {
//						System.out.println(datas[ i ][ j ]);
//					}
//				}
//			}
//        	Map[] maps = excel.getMapValue("中间计量汇总表(支7)", "A4");
//        	if (null != maps && maps.length > 0) {
//        		for (int i = 0; i < maps.length; i++){
//        			System.out.println("承包人申报:" + maps[ i ].get("承包人申报"));
//        		}
//        	}
//        	String[] row27 = excel.getRowValue("中间计量汇总表(支7)", "A27");
//        	for (int i = 0; i < row27.length; i++) {
//				System.out.println(row27[ i ]);
//			}
//        	System.out.println(excel.getRowValue("中间计量汇总表(支7)", "A77"));
//        	System.out.println(excel.getCellValue("中间计量汇总表(支7)", "F27"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
