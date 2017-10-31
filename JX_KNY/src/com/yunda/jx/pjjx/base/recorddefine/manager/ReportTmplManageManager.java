package com.yunda.jx.pjjx.base.recorddefine.manager;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.pjjx.base.recorddefine.entity.ReportTmplManage;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：ReportTmplManage业务类,记录单报表模板管理
 * <li>创建人：何涛
 * <li>创建日期：2014-11-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="reportTmplManageManager")
public class ReportTmplManageManager extends JXBaseManager<ReportTmplManage, ReportTmplManage>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：根据“记录单主键”获取【记录单报表模板管理】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param qrIDX 记录单主键
	 * @return List<ReportTmplManage> 集合
	 */
	@SuppressWarnings("unchecked")
	public List<ReportTmplManage> getModelsByRecordIDX(String qrIDX) {
		String hql = "From ReportTmplManage Where recordStatus = 0 And qrIDX = ?";
		return this.daoUtils.find(hql, new Object[]{qrIDX});
	}
	
}