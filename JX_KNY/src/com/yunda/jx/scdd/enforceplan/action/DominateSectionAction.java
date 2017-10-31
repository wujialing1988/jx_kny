package com.yunda.jx.scdd.enforceplan.action;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.jx.component.manager.OmOrganizationSelectManager;
import com.yunda.jx.scdd.enforceplan.manager.DominateSectionManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 用于DominateSection_comboTree控件
 * <li>创建人：谭诚
 * <li>创建日期：2013-5-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0 
 *   
 */
@SuppressWarnings(value = "serial")
public class DominateSectionAction extends JXBaseAction<OmOrganization, OmOrganization, OmOrganizationSelectManager> {

	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	String orgCode = JXSystemProperties.OVERSEA_ORGCODE;
	
	/** 支配局\支配段 业务Manager*/
	private DominateSectionManager dominateSectionManager;
	/**
	 * 
	 * <li>说明：方法实现功能说明
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-5-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
    public void customTree() throws Exception {
		String queryHql = StringUtil.nvlTrim(getRequest().getParameter("queryHql"), "");
		String parentIDX = StringUtil.nvlTrim(getRequest().getParameter("parentIDX"), "");
		List<HashMap> children = null;
	    children = this.dominateSectionManager.bureauTree(queryHql,parentIDX);        
	    JSONUtil.write(getResponse(), children);
	}
	
	/**
	 * <li>说明：
	 * <li>返回值： the dominateSectionManager
	 */
	public DominateSectionManager getDominateSectionManager() {
		return dominateSectionManager;
	}
	/**
	 * <li>说明：
	 * <li>参数： dominateSectionManager
	 */
	public void setDominateSectionManager(
			DominateSectionManager dominateSectionManager) {
		this.dominateSectionManager = dominateSectionManager;
	}
	
	
}
