package com.yunda.jx.jxgc.common;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 检修过程中公用的常量，都应该定义在该类中，便于公共引用
 * <li>创建人：程梅
 * <li>创建日期：2013年3月29日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class JxgcConstants{
	/**  系统流程注册：三检一验 */
	public static final String PROCESS_SJYY = "jx.jxgc.quality_control";  
	/**  系统流程注册：检修作业卡实例化流程 */
	public static final String PROCESS_INSTANCE = "jx.jxgc.system.sub_workcard_instance";
	/** 系统流程注册：流程控制流程 */
	public static final String PROCESS_CONTROL = "jx.jxgc.system.sub_wf_control";
	/** 公共提票施修流程 */
	public static final String COMMON_TPSX = "jx.jxgc.common.tpsx";
	/** 临修提票施修流程 */
	public static final String LX_TPSX = "jx.jxgc.lx.tpsx";
	/** 碎修提票施修流程 */
	public static final String SX_TPSX = "jx.jxgc.sx.tpsx";
	
	/** 工长派工列表-已派工 */
	public static final int GZPG_DISPATCHED = 1;
	
	/** 工长派工列表-未派工 */
	public static final int GZPG_NOTDISPATCHED = 0;
	/** 作业工单处理-待领取 */
	public static final int WORKCARDPROC_RECEIVE = 0;
	/** 作业工单处理-待处理 */
	public static final int WORKCARDPROC_HANDLER = 1;
	
	/** 提票调度派工列表-已派工 */
	public static final int TPDDPG_DISPATCHED = 1;
	
	/** 提票调度派工列表-未派工 */
	public static final int TPDDPG_NOTDISPATCHED = 0;
	/** 提票工长派工列表-已派工 */
	public static final int TPGZPG_DISPATCHED = 1;
	
	/** 提票工长派工列表-未派工 */
	public static final int TPGZPG_NOTDISPATCHED = 0;
	
	/** 提票质量检查-必检 */
	public static final int TP_BJ = 0;
	/** 提票质量检查-抽检 */
	public static final int TP_CJ = 1;
    
    /** IDXS的sql替换字符 */
    public static final String IDXS = "#IDXS#";
    
    /** updateTime的sql替换字符 */
    public static final String UPDATETIME = "#updateTime#";
    
    /** updator的sql替换字符 */
    public static final String UPDATOR = "#updator#";
    
    /** 提票附件上传目录 */
    public static final String UPLOADPATH_TP = "faultAtt";
}