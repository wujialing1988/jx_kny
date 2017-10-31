package com.yunda.third.poi.excel;

import java.math.BigInteger;
/**
 * <p>Title: 表示(x,y)坐标</p>
 *
 * <p>Description: 坐标对象(目前适用于Excel中的单位坐标)</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class Coordinate {
  private static final BigInteger RADIX = new BigInteger("26");

  private int x;
  private int y;
  private Object value;
  /**
   * xy坐标构建
   * @param x x水平轴下标，从0开始
   * @param y y垂直轴下标，从0开始
   * @param value 单元格的值，实际应用中应统一为字符串，Object类型方便扩展
   */
  public Coordinate(int x, int y, Object value) {
	setX(x);
	setY(y);
    this.value = value;
  }
  /**
   * Excel单元格下标构建
   * @param excelIdx Excel单元格下标（如：B3，D5）
   * @param value 单元格的值，实际应用中应统一为字符串，Object类型方便扩展
   */
  public Coordinate(String excelIdx, Object value){
	excelIdx = excelIdx.toUpperCase();
    int yCoor = Integer.parseInt( excelIdx.replaceAll("[^0-9]", "")) - 1;
    int xCoor = letter2Idx(excelIdx.replaceAll("[^A-Za-z]", ""));
	setX(xCoor);
	setY(yCoor);
    this.value = value;    
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public Object getValue() {
    return value;
  }

  public void setX(int x) {
	if (x > 255)	x = 255;
	if (x < 0)		x = 0;	
    this.x = x;
  }

  public void setY(int y) {
	if (y > 65535)	y = 65535;
	if (y < 0)		y = 0;	  
    this.y = y;
  }

  public void setValue(Object value) {
    this.value = value;
  }
  /**
   * 返回在文件中实际读取的xy下标
   * @return
   */
  public String getXY(){
	  return "(" + getX() + "," + getY() + ")";
  }
  /**
   * 返回Excel中表示单元格位置的坐标
   * @return
   */
  public String getExcelIdx(){
	  return idx2Letter(getX()) + (getY() + 1);
  }

  /**
   * 解析Excel单元格坐标，返回数字坐标,x水平坐标，y垂直坐标
   * @param coor String
   * @return Coordinate
   */
  public static Coordinate parseExcelCoor(String excelIdx) {
	excelIdx = excelIdx.toUpperCase();
    String y = excelIdx.replaceAll("[^0-9]", "");
    String x = excelIdx.replaceAll("[^A-Za-z]", "");
    int yCoor = Integer.parseInt(y) - 1;
    int xCoor = letter2Idx(x);
    return new Coordinate(xCoor, yCoor, "");
  }

  /**
   * 将字母转换成Excel中x坐标，只保证A-IV（0-255）解析正确
   * @param String x
   * @return int
   */
  public static int letter2Idx(String x) {
    int xCoor = 0;
    int exponent = 0;
    for (int i = x.length() - 1; i >= 0; i--) {
      char ch = x.charAt(i);
      ch = Character.toUpperCase(ch);
      int idx = Character.getNumericValue(ch) - Character.getNumericValue('A');
      idx += 1;
      xCoor += (RADIX.pow(exponent).intValue() * idx);
      exponent++;
    }
    xCoor = xCoor - 1;
    return xCoor;
  }
  /**
   * 将x坐标转换成Excel单元格横向字母坐标，只保证A-IV（0-255）解析正确
   * @param idx
   * @return
   */
  public static String idx2Letter(int idx){
	  if (idx < 0)	return "@";
	  if (idx == 0)	return "A";
	  String yCoor = "";
	  int radix = 'A';
	  int logarithmic = (int) (Math.log(idx) / Math.log(RADIX.doubleValue()));
	  for (int i = logarithmic; i >= 0; i--) {
		  int tmp = (int)(idx / RADIX.pow(i).intValue());
		  idx = idx - RADIX.pow(i).intValue() * tmp;
		  char ch = (char) (radix + (logarithmic == 0 || i != logarithmic ? tmp: tmp - 1));
		  yCoor += ch;
	  }
	  return yCoor;
  }
}
