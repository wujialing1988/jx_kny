package com.yunda.jx.wlgl.partsBase.action; 

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.wlgl.partsBase.entity.WhOrg;
import com.yunda.jx.wlgl.partsBase.manager.WhOrgManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WhOrg控制器, 库房与班组关系维护（用于消耗即扣库的模式）
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
public class WhOrgAction extends JXBaseAction<WhOrg, WhOrg, WhOrgManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * 
	 * <li>说明：查询列表
	 * <li>创建人：程梅
	 * <li>创建日期：2014-9-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void findWhOrgList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
        	HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            map = this.manager.page( searchJson, getStart(), getLimit(), getOrders()).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
	/**
	 * 
	 * <li>说明：保存
	 * <li>创建人：程梅
	 * <li>创建日期：2014-9-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void saveFromOrg() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String orgid = getRequest().getParameter("orgid"); //组织机构id
            WhOrg[] detailList = (WhOrg[])JSONUtil.read(getRequest(), WhOrg[].class);
            this.manager.saveFromOrg(orgid,detailList);
            map.put("success", "true");
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
}