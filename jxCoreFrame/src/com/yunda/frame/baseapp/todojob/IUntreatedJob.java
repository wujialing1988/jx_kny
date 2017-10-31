package com.yunda.frame.baseapp.todojob;

import com.yunda.frame.baseapp.todojob.entity.TodoJob;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明： 待办项接口
 * <li>创建人：谭诚
 * <li>创建日期：2014-01-10
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IUntreatedJob {
    
    String FUNC_ZYGD_NAME = "作业工单";//不用权限控制
    
    String FUNC_GZPG_NAME = "工长派工";
    
    String FUNC_GDZLJC_KC_NAME = "客车检修质量检验";//不用权限控制
    
    String FUNC_GDZLJC_HC_NAME = "货车检修质量检验";//不用权限控制
    
    String FUNC_GXYW_NAME = "工序延误";
    
    String FUNC_TPDDPG_NAME = "提票调度派工";
    
    String FUNC_TPGZPG_NAME = "提票工长派工";
    
    String FUNC_TPCL_NAME = "提票处理";//不用权限控制
    
    String FUNC_TPZLJC_NAME = "提票质量检查";//不用权限控制
    
    String FUNC_ZBTP_NAME = "机车提票";
    
    String FUNC_ZBHP_NAME = "机车回票";
    
    String FUNC_LSXTPJC_NAME = "机车临碎修提票质量检查";
    
    String FUNC_JGDDPG_NAME = "技改调度派工";
    
    String FUNC_ZBHGJY_NAME = "整备合格交验";

    String FUNC_ZBHGLXJY_NAME = "整备合格临修交验";

    String FUNC_ENFORCE_NAME = "检修月计划";//不用权限控制
    
    String FUNC_ENGINE_OIL_NAME = "机车上油";
    
    String FUNC_ZBRDP_SX_TO_LX = "整备转临修";
    
    String FUNC_ZBRDP_TP_QR_NAME = "提票待确认";
    
    String FUNC_ZBRDP_LX_PG_NAME = "临修待派工";
    
    String FUNC_PARTS_LIMIT_WARN = "配件配属量预警";

    String FUNC_ZBQXTP_NAME = "整备抢修活处理";
    
    /************ 设备系统 *********/
	String FUNC_TPDDPG_NAME2 = "设备提票调度派工";
	String FUNC_TPGZPG_NAME2 = "设备提票工长派工";
	String FUNC_SBJXRW_NAME = "设备检修任务";
	String FUNC_SBXJRW_NAME = "设备巡检任务";
	String FUNC_SYRQR_NAME = "使用人确认";
	String FUNC_GZCL_NAME = "故障处理";
	String FUNC_GZQR_NAME = "维修工长确认";
	String FUNC_YSYQR_NAME = "主管确认";		// 验收员确认
	String FUNC_GZTP_NAME = "故障提票";
    /**
     * <br/>
     * <li>说明：获取待办事宜 <br/>
     * <li>创建人：谭诚 <br/>
     * <li>创建日期：2014-01-10 <br/>
     * <li>修改人： <br/>
     * <li>修改日期： <br/>
     * <li>修改内容：
     * @param operatorid 操作员ID
     * @return 待办事宜
     */
    public TodoJob getJob(String operatorid);
}
