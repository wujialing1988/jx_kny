package com.yunda.jx.pjjx.partsrdp;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：IPartsRdpStatus接口类,配件检修工单状态
 * <li>创建人： 何涛
 * <li>创建日期： 2014-12-12 上午11:33:53
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IPartsRdpStatus {
	/** 状态 - 已终止 */
	public static final String CONST_STR_STATUS_YZZ = "10";
	
	/** 配件检修工单状态 - 未开放[01] */
	public static final String CONST_STR_STATUS_WKF = "01";
	/** 配件检修工单状态 - 待领取[02] */
	public static final String CONST_STR_STATUS_DLQ = "02";
	/** 配件检修工单状态 - 待处理[03] */
	public static final String CONST_STR_STATUS_DCL = "03";
	/** 配件检修工单状态 - 已处理[04]（数据库中不存储该状态值） */
	public static final String CONST_STR_STATUS_YCL = "04";
	/** 配件检修工单状态 - 质量检验中[0401] */
	public static final String CONST_STR_STATUS_ZLJYZ = "0401";
	/** 配件检修工单状态 - 修竣[0402] */
	public static final String CONST_STR_STATUS_XJ = "0402";
	
	/** 配件检修作业单计划状态 - 未启动[01] */
	public static final String CONST_STR_STATUS_WQD = "01";
	/** 配件检修作业单计划状态 - 检修中[02] */
	public static final String CONST_STR_STATUS_JXZ = "02";
	/** 配件检修作业单计划状态 - 修竣待验收[03] */
	public static final String CONST_STR_STATUS_JXDYS = "03";
	/** 配件检修作业单计划状态 - 无法修复[0402] */
	public static final String CONST_STR_STATUS_WFXF = "0402";
	/** 配件检修作业单计划状态 - 检修合格[0401] */
	public static final String CONST_STR_STATUS_JXHG = "0401";
	
	/** 回退标识[0] - 否 */
	public static final int CONST_INT_IS_BACK_NO = 0;
	/** 回退标识[1] - 是 */
	public static final int CONST_INT_IS_BACK_YES = 1;
    
    /** 未处理[wcl] */
    public static final String STATUS_WCL = "wcl";
    /** 质检中[zjz] */
    public static final String STATUS_ZJZ = "zjz";
    /** 已处理[ycl] */
    public static final String STATUS_YCL = "ycl";
    
	
}
