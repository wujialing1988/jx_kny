package com.yunda.jx.wlgl.partsBase.action; 

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.wlgl.partsBase.entity.WhMatQuota;
import com.yunda.jx.wlgl.partsBase.manager.WhMatQuotaManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WhMatQuota控制器, 库房保有量
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-09-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class WhMatQuotaAction extends JXBaseAction<WhMatQuota, WhMatQuota, WhMatQuotaManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	private String[] matCodes ;
	/**
	 * 
	 * <li>说明：批量保存保有量信息
	 * <li>创建人：程梅
	 * <li>创建日期：2014-9-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void saveWhMatQuotaBatch() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String whIdx = getRequest().getParameter("whIdx");//库房id
			WhMatQuota[] quotaList = (WhMatQuota[])JSONUtil.read(getRequest(), WhMatQuota[].class);
			this.manager.validateBatch(whIdx, quotaList);
			this.manager.saveWhMatQuotaBatch(whIdx, quotaList);
			map.put("success", "true");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
    
    /**
     * <li>说明：保存站点物料信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveWhMatQuotaBatchForWorkPlace() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String workplaceCode = getRequest().getParameter("workplaceCode");// 站点编码
            String workplaceName = getRequest().getParameter("workplaceName");// 站点名称
            WhMatQuota[] quotaList = (WhMatQuota[])JSONUtil.read(getRequest(), WhMatQuota[].class);
            this.manager.validateBatchForWorkPlace(workplaceCode, quotaList);
            this.manager.saveWhMatQuotaBatchForWorkPlace(workplaceCode,workplaceName, quotaList);
            map.put("success", "true");
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
	public String[] getMatCodes() {
		return matCodes;
	}
	public void setMatCodes(String[] matCodes) {
		this.matCodes = matCodes;
	}
}