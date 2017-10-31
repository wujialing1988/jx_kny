package com.yunda.jx.pjwz.partsBase.partsquota.action; 

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.jx.pjwz.partsBase.partsquota.entity.PartsQuota;
import com.yunda.jx.pjwz.partsBase.partsquota.manager.PartsQuotaManager;
import com.yunda.jx.pjwz.partsBase.partstype.manager.PartsTypeManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsQuota控制器, 互换配件定额
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-08-30
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class PartsQuotaAction extends JXBaseAction<PartsQuota, PartsQuota, PartsQuotaManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	private PartsTypeManager partsTypeManager;
    /** 组织机构业务类：OmOrganizationManager */
    private OmOrganizationManager omOrganizationManager;
	
    public OmOrganizationManager getOmOrganizationManager() {
        return omOrganizationManager;
    }
    
    public void setOmOrganizationManager(OmOrganizationManager omOrganizationManager) {
        this.omOrganizationManager = omOrganizationManager;
    }
    /**
	 * 
	 * <li>说明：将从规格型号选取的数据添加到配件定额表中
	 * <li>创建人：程梅
	 * <li>创建日期：2012-8-31
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 返回值为空
	 * @throws 抛出异常列表
	 */
	public void saveFromPartsType() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
//			String oversea = getRequest().getParameter("oversea");
//			String overseaName = getRequest().getParameter("overseaName");
            String orgCode = JXSystemProperties.OVERSEA_ORGCODE;
            OmOrganization org = omOrganizationManager.findOrgForCode(orgCode);
			this.manager.saveFromPartsType(org.getOrgid(),org.getOrgname(),partsTypeManager,ids);
			map.put("success", "true");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
    /**
     * 
     * <li>说明：保存批量设置定额的信息
     * <li>创建人：程梅
     * <li>创建日期：2013-3-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    public void saveOrUpdateList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            PartsQuota[] quotaList = (PartsQuota[])JSONUtil.read(getRequest(), PartsQuota[].class);
            this.manager.saveOrUpdateList(quotaList);
            map.put("success", "true");
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
	public PartsTypeManager getPartsTypeManager() {
		return partsTypeManager;
	}
	public void setPartsTypeManager(PartsTypeManager partsTypeManager) {
		this.partsTypeManager = partsTypeManager;
	}
}