package com.yunda.frame.common;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 该类收集常用的java正则表达式字符串，供开发人员参考使用
 * <li>注意：1.开发过程，各位开发人员应共同维护更新该类的正则表达式，将一些经过优化验证的添加该类中。
 * 			2.因时间关系（测试覆盖率不足），收集整理的正则表达式较多，不能保证100%正确，开发人员使用时若发现问题请及时纠正并相互告之。
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-7-8
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class RegexConstants {
	/** 非负整数（正整数 + 0） */
	public static final String 非负整数 = "^\\d+$";
	/** 正整数 */
	public static final String 正整数 = "^[0-9]*[1-9][0-9]*$";
	/** 非正整数（负整数 + 0） */
	public static final String 非正整数 = "^((-\\d+)|(0+))$";
	/** 整数 */
	public static final String 整数 = "^-?\\d+$";
	/** 非负浮点数（正浮点数 + 0） */
	public static final String 非负浮点数 = "^\\d+(\\.\\d+)?$";
	/** 正浮点数 */
	public static final String 正浮点数 = "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";
	/** 非正浮点数（负浮点数 + 0） */
	public static final String 非正浮点数 = "^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$";
	/** 负浮点数 */
	public static final String 负浮点数 = "^(-(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*)))$";
	/** 浮点数 */
	public static final String 浮点数 = "^(-?\\d+)(\\.\\d+)?$";
	/** 整数或者小数 */
	public static final String 整数或小数 = "^[0-9]+\\.{0,1}[0-9]{0,2}$";
	/** 0-9数字 */
	public static final String 零到九数字 = "^[0-9]*$";
	/** n位的数字 */
	public static final String n位的数字 = "^\\d{n}$";
	/** 至少n位的数字 */
	public static final String 至少n位的数字 = "^\\d{n,}$";
	/** m~n位的数字 */
	public static final String m到n位的数字 = "^\\d{m,n}$";
	/** 零和非零开头的数字 */
	public static final String 零和非零开头的数字 = "^(0|[1-9][0-9]*)$";
	/** 两位小数的正实数 */
	public static final String 两位小数的正实数 = "^[0-9]+(.[0-9]{2})?$";
	/** 有1~3位小数的正实数 */
	public static final String 一到三位小数的正实数 = "^[0-9]+(.[0-9]{1,3})?$";
	
	
	/** 由26个英文字母组成的字符串 */
	public static final String 英文字母 = "^[A-Za-z]+$";
	/** 由26个英文字母的大写组成的字符串 */
	public static final String 英文字母大写 = "^[A-Z]+$";
	/** 由26个英文字母的小写组成的字符串  */
	public static final String 英文字母小写 = "^[a-z]+$";
	/** 由数字和26个英文字母组成的字符串 */
	public static final String 英文数字 = "^[A-Za-z0-9]+$";
	/** 由数字、26个英文字母或者下划线组成的字符串 */
	public static final String 英文数字下划线 = "^\\w+$";
	/** 匹配帐号是否合法(字母开头，允许5-16字节，允许字母数字下划线) */
	public static final String 帐号 = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";
	/** 验证用户密码，正确格式为：以字母开头，长度在6~18之间，只能包含字符、数字和下划线 */
	public static final String 用户密码 = "^[a-zA-Z]\\w{5,17}$";
	/** 是否含有^%&',;=?$\"等字符 */
	public static final String 是否含有其他字符 = "[^%&',;=?$\\x22]+";
	/** 匹配首尾空格的正则表达式 */
	public static final String 首尾空格 = "(^\\s*)|(\\s*$)";	

	
	/** 年-月-日 */
	public static final String 年_月_日 = "/^(d{2}|d{4})-((0([1-9]{1}))|(1[1|2]))-(([0-2]([1-9]{1}))|(3[0|1]))$/";
	/** 月/日/年 */
	public static final String 月日年 = "/^((0([1-9]{1}))|(1[1|2]))/(([0-2]([1-9]{1}))|(3[0|1]))/(d{2}|d{4})$/";
	/** 验证一年的12个月，正确格式为："01"～"09"和"1"～"12" */
	public static final String 一年的12个月 = "^(0?[1-9]|1[0-2])$";
	/** 验证一个月的31天，正确格式为："01"～"09"和"1"～"31" */
	public static final String 一个月的31天 = "^((0?[1-9])|((1|2)[0-9])|30|31)$";
	

	/** 匹配中文字符的正则表达式 */
	public static final String 中文字符 = "[\\u4e00-\\u9fa5]";
	/** 匹配双字节字符(包括汉字在内) */
	public static final String 双字节字符 = "[^\\x00-\\xff]";
	/** 匹配空行的正则表达式 */
	public static final String 空行 = "\\n[\\s| ]*\\r";
	/** IP地址  */
	public static final String IP = "^(d{1,2}|1dd|2[0-4]d|25[0-5]).(d{1,2}|1dd|2[0-4]d|25[0-5]).(d{1,2}|1dd|2[0-4]d|25[0-5]).(d{1,2}|1dd|2[0-4]d|25[0-5])$";	
	/** email地址 */
	public static final String EMAIL = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
	/** url地址 */
	public static final String URL = "^[a-zA-z]+://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?$";
	/** 匹配网址URL的正则表达式 */
	public static final String 网址URL = "http://([w-]+.)+[w-]+(/[w- ./?%&=]*)? ";	
	/** 匹配HTML标记的正则表达式，对于复杂的嵌套标记依旧无能为力 */
	public static final String HTML = "/<(.*)>.*<\\/\\1>|<(.*) \\/>/";
	
	
	/** 匹配国内电话号码 */
	public static final String 国内电话号码 = "(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)?\\d{8}";
	/** 电话号码 */
	public static final String 电话号码 = "(d+-)?(d{4}-?d{7}|d{3}-?d{8}|^d{7,8})(-d+)?";	
	/** 匹配国内邮政编码，中国邮政编码为6位数字 */
	public static final String 国内邮政编码 = "[1-9]\\d{5}(?!\\d)";	
	/** 验证身份证号（15位或18位数字） */
	public static final String 身份证 = "^d{15}|d{}18$";
	/** 匹配腾讯QQ号 */
	public static final String QQ = "^[1-9]*[1-9][0-9]*$";

}