package com.yunda.util;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import oracle.sql.DATE;
import org.hibernate.Hibernate;
import org.hibernate.type.Type;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import com.yunda.common.SortObject;

/**
 * 
 * <li>类型名称：
 * <li>说明：特有的字符串处理工具。
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-01-14
 * <li>修改人： 
 * <li>修改日期：
 */
public class YDStringUtil {
	
	public static final Integer ASC = 0;
	public static final Integer DESC = 1;
	public static final String ASCKEY = "asc";
	public static final String DESCKEY = "desc";
	public static final String BYKEY = " by ";
	public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
	/**
	 * 
	 * <li>方法名：createJspFileName
	 * <li>@param source
	 * <li>@return
	 * <li>返回类型：String
	 * <li>说明：根据传入的字符串创建用下划线分隔的全小写字母组成的文件名
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static String createJspFileName(String source){
		
		StringBuffer sb = new StringBuffer();		
		sb.append(Character.toLowerCase(source.charAt(0)));
		for(int i = 1; i < source.length(); ++i){
			if(Character.isUpperCase(source.charAt(i))){		
				sb.append('_');
			}
			sb.append(Character.toLowerCase(source.charAt(i)));
		}
		
		return sb.toString();
	}
	
	/**
	 * <li>方法名：Long2Int
	 * <li>@param ldata
	 * <li>@return
	 * <li>返回类型：int
	 * <li>说明：数据类型转换
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-21
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static int Long2Int(long ldata)
	{	
		return Integer.parseInt(String.valueOf(ldata));
	}
	
	/**
	 * <li>方法名：Int2Long
	 * <li>@param ldata
	 * <li>@return
	 * <li>返回类型：int
	 * <li>说明：数据类型转换
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-10
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static Long Int2Long(Integer intS)
	{	
		return Long.parseLong(intS.toString());
	}
	
	/**
	 * <li>方法名：Str2Long
	 * <li>@param str
	 * <li>@return
	 * <li>返回类型：Long
	 * <li>说明：数据类型转换
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-10
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static Long Str2Long(String str){
		return Long.parseLong(str.trim());
	}
	                              
	/**
	 * <li>方法名：string2BigDecimal
	 * <li>@param str
	 * <li>@return
	 * <li>返回类型：BigDecimal
	 * <li>说明：数据类型转换
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-21
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static BigDecimal String2BigDecimal(String str) 
	{
		BigDecimal bigDecimal = null;
		if (str != null && str.trim().length() != 0)
		{
			bigDecimal = new BigDecimal(str);
		}
		return bigDecimal;
	}
	
	/**
	 * <li>方法名：DateParse
	 * <li>@param str
	 * <li>@return
	 * <li>@throws Exception
	 * <li>返回类型：Date
	 * <li>说明：字符格式转换为日期类型
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-25
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static Date DateParse(String str){
		try{
			return format.parse(str);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * <li>方法名：date2str
	 * <li>@param date
	 * <li>@param formatString
	 * <li>@return
	 * <li>返回类型：String
	 * <li>说明：数据类型转换
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-21
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static String Date2Str(java.util.Date date, String formatString)
	{
		return format.format(date);
	}
	
	/**
	 * <li>方法名：DateFormat
	 * <li>@param date
	 * <li>@return
	 * <li>返回类型：String
	 * <li>说明：日期格式化为yyyy-MM-dd形式
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-25
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static String DateFormat(Date date){
		return format.format(date);
	}
	
	/**
	 * <li>方法名：Str2Date
	 * <li>@param str
	 * <li>@return
	 * <li>返回类型：Date
	 * <li>说明：数据类型转换
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-21
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static Date Str2Date(String str){
		Date date = null;
		try{
			if(str != null && !str.trim().equals("") && isDate(str))
				date = format.parse(str);
		}catch(Exception e){
		}
		return date;
	}
	
	
	public static Date Str2Date(String str, String formatString){
		if(isBlank(formatString)){
			formatString = "yyyy-MM-dd";
		}
        
        //修改 2015-02-02 汪东良 将format 局部变量修改成obj开头，保证与属性变量命名不同。
		SimpleDateFormat objFormat=new SimpleDateFormat(formatString.split(";")[0]);
		//System.out.println(formatString.split(";")[0]);
		Date date = null;
		try{
			if(!isBlank(str))
				date = objFormat.parse(str);
		}catch(Exception e){
			e.printStackTrace();
			return new Date();			
		}
		return date;
	}
	
	
	/**
	 * <li>方法名：isDate
	 * <li>@param str
	 * <li>@return
	 * <li>返回类型：boolean
	 * <li>说明：判断是否是日期字符串
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-21
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static boolean isDate(String str){
		try{
			if(!isBlank(str))
				format.parse(str);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	/**
	 * <li>方法名：Str2Int
	 * <li>@param str
	 * <li>@return
	 * <li>返回类型：int
	 * <li>说明：数据类型转换
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-21
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static int Str2Int(String str)
	{
		if (str == null || "".equals(str))
			return 0;
		return Integer.parseInt(str);
	}
	
	/**
	 * <li>方法名：StrFill
	 * <li>@param fillStr 用来补位的字符
	 * <li>@param oldStr 需要补位的字符串
	 * <li>@param length 补位后的总长度
	 * <li>@param place 补位位置:left or right
	 * <li>@return
	 * <li>返回类型：String
	 * <li>说明：用指定的字符为需要补位的字符串补位
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-21
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static String StrFill(String fillStr ,String oldStr ,int length ,String place)
	{
		StringBuffer sb =  new StringBuffer();
		if("right".equals(place)){
			sb.append(oldStr);
		}
		for(int i=0; i < (length - oldStr.length());i++){
			sb.append(fillStr);
		}
		if("left".equals(place)){
			sb.append(oldStr);
		}
		return sb.toString();
	}
	
	/**
	 * <li>方法名：isBlank
	 * <li>@param str
	 * <li>@return
	 * <li>返回类型：boolean
	 * <li>说明：判断字符串是否为空,为空就返回true,不为空返回false
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-26
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static boolean isBlank(String str){
		if(str==null){
			return true;
		}
		if(str.length()<1){
			return true;
		}
		return false;
	}
	
	/**
	 * <li>方法名：getNow
	 * <li>@return
	 * <li>返回类型：Date
	 * <li>说明：获取JVM当前时间
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-24
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static Date getNow(){
		return new Date();
	}
	
	/**
	 * <li>方法名：getNow
	 * <li>@param format
	 * <li>@return
	 * <li>返回类型：String
	 * <li>说明：获取JVM当前时间字符串，并转换为指定的格式
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-24
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static String getNow(String formatstr){
		format  = new SimpleDateFormat(formatstr); 
		return format.format(getNow());
	}
	
	public static List<SortObject> orderString(String s1){
		int i = s1.toLowerCase().indexOf(BYKEY);
		String s = s1.substring(i+4,s1.length());
		
		String []sorts = s.split(",");
				
		List<SortObject> sortList = new ArrayList<SortObject>();
		for (int j = 0; j < sorts.length; j++) {
			sortList.add(singlOrderString(sorts[j]));
		}
		
		return sortList;
	}
	
	public static SortObject singlOrderString(String s){
		int j = s.toLowerCase().lastIndexOf(ASCKEY);
		if(!(j <= -1)){
			String s1 = s.substring(0,j-1);
			SortObject so = new SortObject();
			so.setSortProperty(s1.trim());
			so.setSortType(Integer.valueOf(ASC));
			return so;
		}

		j = s.toLowerCase().lastIndexOf(DESCKEY);
			
		if(j <= -1){
			String s1 = s;
			
			SortObject so = new SortObject();
			so.setSortProperty(s1);
			so.setSortType(Integer.valueOf(ASC));
			return so;
		}else{
			String s1 = s.substring(0,j-1);
			
			SortObject so = new SortObject();
			so.setSortProperty(s1.trim());
			so.setSortType(Integer.valueOf(DESC));
			return so;
		}
	}
	
	
	public static String orderStringClearOrderBy(String s1){
		if(s1 == null || s1.length() <= 0){
			return "";
		}
		
		int i = s1.toLowerCase().indexOf(BYKEY);

		String s = s1.substring(i+4,s1.length());
		return s;
	}

	
	public static String toUTF8(String str){
		if(str == null){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i=0; i< str.length(); i++) {
			char c = str.charAt(i);
			if(c >= 0 && c <= 256){
				sb.append(c);
			}
			else{
				try{
					byte[] b = Character.toString(c).getBytes("UTF-8");
					for (int j = 0; j < b.length; j++) {
						int k = b[j];
						if(k<0){
							k = k + 256;
						}
						sb.append("%" + Integer.toHexString(k).toUpperCase());
					}
				}
				catch (Exception e) {
					System.out.println(e);
				}
			}
		}
		
		return sb.toString();
	}
	
	public static String decodeUTF8(String s) {
		if (s == null)
			return "";

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '+':
				sb.append(' ');
				break;
			case '%':
				try {
					// 将16进制的数转化为十进制
					sb.append((char) Integer.parseInt(
							s.substring(i + 1, i + 3), 16));
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException();
				}
				i += 2;
				break;
			default:
				sb.append(c);
				break;
			}
		}

		String result = sb.toString();
		// 将UTF-8转换为GBK java+%E7%BC%96%E7%A8%8B
		// byte[] inputBytes = result.getBytes("8859_1"); //UTF8
		// return new String(inputBytes,"GB2312");
		try {
			result = new String(result.getBytes("8859_1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 
	 * <li>方法名：toConvertType
	 * 将java.lang.reflect.Type转换为org.hibernate.type.Type
	 * <li>@param t
	 * <li>@return
	 * <li>返回类型：Type
	 * <li>说明：
	 * <li>创建人：李恒飞
	 * <li>创建日期：2011-5-3
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static Type toConvertType(java.lang.reflect.Type t)
	{
		if(t.equals(String.class))
		{
			return Hibernate.STRING;
		}
		if(t.equals(Long.class))
		{
			return Hibernate.LONG;
		}
		if(t.equals(Integer.class))
		{
			return Hibernate.INTEGER;
		}
		if(t.equals(Double.class))
		{
			return Hibernate.DOUBLE;
		}
		if(t.equals(Float.class))
		{
			return Hibernate.FLOAT;
		}
		if(t.equals(Short.class))
		{
			return Hibernate.SHORT;
		}
		if(t.equals(Byte.class))
		{
			return Hibernate.BYTE;
		}
		if(t.equals(Boolean.class))
		{
			return Hibernate.BOOLEAN;
		}
		if(t.equals(BigDecimal.class))
		{
			return Hibernate.BIG_DECIMAL;
		}
		if(t.equals(java.sql.Date.class))
		{
			return Hibernate.DATE;
		}
		if(t.equals(Date.class))
		{
			return Hibernate.DATE;
		}
		if(t.equals(DATE.class))
		{
			return Hibernate.DATE;
		}
		return null;
	}
	/**
	 * 
	 * <li>方法名：StringEmpty
	 * <li>@param str
	 * <li>@return
	 * <li>返回类型：String
	 * <li>说明：如果参数为空返回字符串空，相反返回参数
	 * <li>创建人：李恒飞
	 * <li>创建日期：2011-7-19
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static String StringEmpty(String str)
	{
		if(isBlank(str))
		{
			return "";
		}
		return str;
	}
	/**
	 * 
	 * <li>方法名：xmlToMap
	 * <li>@param xmlstr  xml字符串
	 * <li>@param node  查找的节点
	 * <li>@param key  map的键
	 * <li>@param value  map的值
	 * <li>@return
	 * <li>@throws Exception
	 * <li>返回类型：HashMap
	 * <li>说明：根据str转换成xml并解析，将查找的对象存入map返回
	 * <li>创建人：李恒飞
	 * <li>创建日期：2011-7-20
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static HashMap xmlToMap(String xmlstr, String node, String key, String value) throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		builder  =  factory.newDocumentBuilder();  
		Document doc  =  (Document) builder.parse( new  ByteArrayInputStream(xmlstr.getBytes("utf-8"))); 
		NodeList nl = doc.getElementsByTagName(node);
		for (int i = 0; i < nl.getLength(); i++) {
			map.put(doc.getElementsByTagName(key).item(i).getFirstChild().getNodeValue(),
					doc.getElementsByTagName(value).item(i).getFirstChild().getNodeValue());
		}
		return map;
	}
	
	public static void main(String[] params){
		String str = "您还阿富汗123！";
		System.out.println(str);
		str = toUTF8(str);
		System.out.println(str);
		str = decodeUTF8(str);
		System.out.println(str);
		
		
		System.out.println(YDStringUtil.getNow("yyyy-MM-dd HH:mi:ss"));
	}
}
