package com.yunda.frame.report.manager;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.report.entity.ReportToBusiness;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: ReportToBusines业务类,报表业务关联
 * <li>创建人：何涛
 * <li>创建日期：2015-2-5
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="reportToBusinessManager")
public class ReportToBusinessManager extends JXBaseManager<ReportToBusiness, ReportToBusiness>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：根据“报表打印模板主键”和“业务主键”获取【报表业务关联实体】
     * <li>创建人：何涛
     * <li>创建日期：2015-2-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param printerModuleIDX 报表打印模板主键
     * @param businessIDX 业务主键
     * @return ReportToBusiness 报表业务关联实体
     */
    public ReportToBusiness getModel(String printerModuleIDX, String businessIDX) {
        String hql = "From ReportToBusiness Where recordStatus = 0 And printerModuleIDX = ? And businessIDX = ?";
        return (ReportToBusiness)this.daoUtils.findSingle(hql, new Object[]{printerModuleIDX, businessIDX});
    }
	
}