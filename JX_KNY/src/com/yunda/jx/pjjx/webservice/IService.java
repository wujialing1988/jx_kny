package com.yunda.jx.pjjx.webservice;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：配件检修webservice基础接口，定义了webservice接口所用到的共用常量
 * <li>创建人： 何涛
 * <li>创建日期： 2015-1-13 下午01:11:35
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IService {
	
	/** 接口参数不能为空！*/
	public static final String MSG_ERROR_ARGS_NULL = "接口参数不能为空！";
	
	/** "接口参数[用户ID]不能为空！*/
	public static final String MSG_ERROR_ARGS_NULL_OPERATOR_ID = "接口参数[用户ID]不能为空！";
	
	/** 没有查询到任何数据！*/
	public static final String MSG_RESULT_IS_EMPTY = "没有查询到任何数据！";
	
	/** JSON对象解析异常！*/
	public static final String MSG_JSON_PARSE_ERROR = "JSON对象解析异常！";
	
	/** JONS对象字段名称 - 返回列表大小【count】 */
	public static final String JSON_FILED_NAME_COUNT = "count";
	
	/** JONS对象字段名称 - 返回列表内容【list】 */
	public static final String JSON_FILED_NAME_LIST = "list";
    
	/** 斜杠[/] */
	public static final String SPRIT_CHAR = "/";
    
	/** 短横线[-] */
	public static final String SHORT_LINE_CHAR = "-";
	
}
