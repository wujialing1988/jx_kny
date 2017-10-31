package com.yunda.frame.common;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 整个检修系统中公用的常量，都应该定义在该类中，便于公共引用
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-8-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class Constants{
    /**  未删除 */
    public static final int NO_DELETE = 0;
    /**  已删除 */
    public static final int DELETED = 1;
    /** 记录业务状态：新增 */
    public static final int STATUS_NEW = 10;
    /** 记录业务状态：启用 */
    public static final int STATUS_USE = 20;
    /** 记录业务状态：作废 */
    public static final int STATUS_INVALID = 30;
    /** 记录业务状态：是 */
    public static final int YES = 1;
    /** 记录业务状态：否 */
    public static final int NO = 0;   
    /** 铁道部名称-作单位树根节点显示用 */
    public static final String ORG_ROOT_NAME = JXConfig.getInstance().getOrgRootName();
    /** 目标类型 人员 */
    public static final int EMP = 1;
    /** 目标类型 组织机构 */
    public static final int ORG = 2;
    /** 目标类型 组 */
    public static final int GROUP = 3;
    /** 目标类型 岗位 */
    public static final int POSITION = 4;
    /** 目标类型 职务 */
    public static final int DUTY = 5;
    /** 目标类型 角色 */
    public static final int ROLE = 6;
    /** 部级 */
    public static final String MINISTERIAL_LEVEL = "hq";
    /** 局级 */
    public static final String BUREAU_LEVEL = "branch";
    /** 段级 */    
    public static final String SEGMENT_LEVEL = "oversea";
    /** 车间级 */    
    public static final String DEPARTMENT_LEVEL = "plant";
    /** 班组级 */    
    public static final String TEAM_LEVEL = "tream";
    /** 统计分析方式 按月*/
    public static final String ANALZETYPE_MONTH = "byMonth";
    /** 统计分析方式 按季*/
    public static final String ANALZETYPE_SEASON = "bySeason";
    /** 统计分析方式 按年*/
    public static final String ANALZETYPE_YEAR = "byYear";
    /** BPS流程实例状态-未启动*/
    public static final int BPS_PROCESSINST_NOTSTART = 1;
    /** BPS流程实例状态-运行*/
    public static final int BPS_PROCESSINST_RUNNING = 2;
    /** BPS流程实例状态-挂起*/
    public static final int BPS_PROCESSINST_SUSPEND = 3;
    /** BPS流程实例状态-完成*/
    public static final int BPS_PROCESSINST_COMPLETE = 7;
    /** BPS流程实例状态-终止*/
    public static final int BPS_PROCESSINST_TERMINATED = 8;
    /** BPS活动实例状态-未启动*/
    public static final int BPS_ACTIVITYINST_NOTSTART = 1;
    /** BPS活动实例状态-运行*/
    public static final int BPS_ACTIVITYINST_RUNNING = 2;
    /** BPS活动实例状态-挂起*/
    public static final int BPS_ACTIVITYINST_SUSPEND = 3;
    /** BPS活动实例状态-完成*/
    public static final int BPS_ACTIVITYINST_COMPLETE = 7;
    /** BPS活动实例状态-待激活*/
    public static final int BPS_ACTIVITYINST_WAITINGFORACTIVATE = 10;
    
    //以下是常用字符串常量
    /** 分页开始行关键字：start */
    public static final String START = "start";
    /** 分页结束行关键字：limit */
    public static final String LIMIT = "limit";
    /** 排序关键字：orders */
    public static final String ORDERS = "orders";
    /** 系统业务表主键关键字：idx */
    public static final String IDX = "idx";
    /** 系统业务表主键数组关键字：ids */
    public static final String IDS = "ids";      
    /** 实体类json字符串key值：entityJson */
    public static final String ENTITY_JSON = "entityJson";
    /** 空JSON对象 */
    public static final String EMPTY_JSON = "{}";
    /** 实体类json字符串key值：entity */
    public static final String ENTITY = "entity";
    /** 操作员ID：operatorId */
    public static final String OPERATOR_ID = "operatorId";
    /** 操作成功的标识 */
    public static final String SUCCESS = "success";    
    /** 操作失败的错误消息标识 */
    public static final String ERRMSG = "errMsg";
    /** SQL语句关键字 - AND */
    public static final String AND = " and ";
    /** SQL语句关键字 - FROM */
    public static final String FROM = " from ";
    /** 空的JSON对象字符串 */
    public static final String EMPTY_JSON_OBJECT = "{}";
    /** 英文状态下的左括号 */
    public static final String BRACKET_L = "(";
    /** 英文状态下的右括号 */
    public static final String BRACKET_R = ")";
    /** 英文状态下的单引号 */
    public static final String SINGLE_QUOTE_MARK = "'";
    /**SQL语句关键字 - % */
    public static final String LIKE_PIPEI = "%'";
    
    public static final String BRACKET_MARK_R = "')";
    
    /**,号常量 */
    public static final String JOINSTR = ",";
    
}