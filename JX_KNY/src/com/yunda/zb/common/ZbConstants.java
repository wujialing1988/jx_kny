package com.yunda.zb.common;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 整备系统中公用的常量
 * <li>创建人：程锐
 * <li>创建日期：2015-1-27
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public class ZbConstants {
    
    /** 检修类型：碎修 */
    public static final String REPAIRCLASS_SX = "10";
    
    /** 检修类型：临修 */
    public static final String REPAIRCLASS_LX = "20";
    
    /** IDXS的sql替换字符 */
    public static final String IDXS = "#IDXS#";
    
    /** updateTime的sql替换字符 */
    public static final String UPDATETIME = "#updateTime#";
    
    /** 提票附件上传目录 */
    public static final String UPLOADPATH_TP = "tpAtt";
    
    /** 普查整治计划附件上传目录 */
    public static final String UPLOADPATH_PCZZ = "pczzAtt";
    
    /** 提票图片上传目录 */
    public static final String UPLOADPATH_TP_IMG = "tpImgAtt";
    
    /** 提票音频上传目录 */
    public static final String UPLOADPATH_TP_AUDIO = "tpAudioAtt";
    
    /** 机车履历图片上传目录 */
    public static final String UPLOADPATH_TRAINRECORD_IMG = "trImgAtt";
    
    //8.26, 提票功能的两个作业节点
    /** 销票节点时上传附件 */
    public static final String UPLOADPATH_NODE_XP = "nodeXpAtt";
    
    /** 提票节点时上传附件 */
    public static final String UPLOADPATH_NODE_TP = "nodeTpAtt";
    
    
    /** 整备合格交验-任务类型-机车上砂 */
    public static final String TASKTYPE_SANDING = "trainonsand";
    
    /** 整备合格交验-任务类型-机车交接 */
    public static final String TASKTYPE_HANDOVER = "trainhandover";
    
    /** 整备合格交验-任务类型-整备任务单 */
    public static final String TASKTYPE_RDP = "rdpbill";
    
    /** 整备合格交验-任务类型-提票活 */
    public static final String TASKTYPE_TP = "tp";
    
    /** 整备合格交验-任务类型-例外放行 */
    public static final String TASKTYPE_LWFX = "lwfx";
    
    /** 整备合格交验-任务类型-普查整治 */
    public static final String TASKTYPE_PCZZ = "pczz";
    
    /** 整备合格交验-任务类型-机车保洁 */
    public static final String TASKTYPE_CLEAN = "trainclean";
    
    /** 台位图公共类服务名 */    
    public static final String TWTUTIL_SERVICENAME = "twtUtil";
    
    /** JCZB_ZBFW数据字典配置项，必须与下面一致 */
    
    public static final String NODENAME_CLEANING = "机车洗车";
    
    public static final String NODENAME_SANDING = "机车上砂";
    
    public static final String NODENAME_HANDOVER = "机车交接";
    
    public static final String NODENAME_FWH = "范围活";
    
    public static final String NODENAME_JY = "机车交验";
    
    /** 整备机车在段倒计时 */
    public static final String ZB_LAST_TIME = "90";
    
    
}
