package com.yunda.jcbm.jcgx.action; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jcbm.jcgx.entity.JcgxBuild;
import com.yunda.jcbm.jcgx.entity.JcxtflFault;
import com.yunda.jcbm.jcgx.manager.JcgxBuildQueryManager;
import com.yunda.jcbm.jcxtfl.entity.JcxtflBuild;

/**
 * <li>类型名称：机车构型查询控制类
 * <li>说明：
 * <li>创建人： 何东
 * <li>创建日期：2016-5-16
 * <li>修改人： 
 * <li>修改日期：
 */
@SuppressWarnings(value="serial")
public class JcgxBuildQueryAction extends JXBaseAction<JcgxBuild, JcgxBuild, JcgxBuildQueryManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>方法名：根据车型及机车构型主键获取下级树节点列表
	 * <li>@throws Exception
	 * <li>返回类型：void
	 * <li>说明：
	 * <li>创建人：何东
	 * <li>创建日期：2016-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unused")
	public void getJcgxBuildTree() throws Exception {
		List<Map<String, Object>> children = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
            String searchJson = StringUtil.nvlTrim(getRequest().getParameter("entityJson"), "{}");
            JcgxBuild entity = JSONUtil.read(searchJson, JcgxBuild.class);
          
            //JcgxBuild entity = entities.length > 0 ? entities[0] : null; // 取第一条提票信息
            if (entity != null) {
        	    String fjdID = entity.getFjdID();
                String sycx = entity.getSycx();
                String useType = entity.getUseType();
    		
                children = manager.getJcgxBuildTree(fjdID, sycx, useType);
            }
        }catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
            map.put("success", false);
            map.put("errMsg", "获取机车构型树数据失败");
        } finally{
            if (map.get("success") != null && !(Boolean)map.get("success")) {
        		JSONUtil.write(getResponse(), map);
        	}
        	else {
        		JSONUtil.write(getResponse(), children);
        	}
        }
	}
	
	/**
	 * <li>方法名：通过分类编码获取故障现象数据
	 * <li>@throws Exception
	 * <li>返回类型：void
	 * <li>说明：
	 * <li>创建人：何东
	 * <li>创建日期：2016-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unused")
	public void getFlbmFault() throws Exception {
		List<Map<String, Object>> faultList = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
            String searchJson = StringUtil.nvlTrim(getRequest().getParameter("entityJson"), "{}");
            JcxtflFault entity = JSONUtil.read(searchJson, JcxtflFault.class);
          
            if (entity != null) {
        	    String flbm = entity.getFlbm();
    		
                faultList = manager.getFlbmFault(flbm);
            }
        }catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
            map.put("success", false);
            map.put("errMsg", "通过分类编码获取故障现象失败");
        } finally{
        	if (map.get("success") != null && !(Boolean)map.get("success")) {
        		JSONUtil.write(getResponse(), map);
        	}
        	else {
        		JSONUtil.write(getResponse(), faultList);
        	}
        }
	}
    
    /**
     * <li>说明：获取机车组成下拉树
     * <li>创建人：张迪
     * <li>创建日期：2016-9-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void getJczcmcBuildTree() throws Exception {
        List<Map<String,Object>> children = new ArrayList<Map<String,Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        String fjdID = StringUtil.nvlTrim(getRequest().getParameter("fjdID"),"");
        String sycx = StringUtil.nvlTrim(getRequest().getParameter("sycx"), "");
        String useType = StringUtil.nvlTrim(getRequest().getParameter("useType"), "");
        JcxtflBuild entity = new JcxtflBuild();
        entity.setFjdID(fjdID);
        entity.setSycx(sycx);
        entity.setUseType(useType);
//            entity.setFlmc(flmc);
        try {
            children = manager.getJczcmcBuildTree(entity);
        }catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
            map.put("success", false);
            map.put("errMsg", "获取机车构型树数据失败");
        } finally{
            if (map.get("success") != null && !(Boolean)map.get("success")) {
                JSONUtil.write(getResponse(), map);
            }
            else {
                JSONUtil.write(getResponse(), children);
            }
        }
     }
}