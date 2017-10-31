package com.yunda.jcbm.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jcbm.jcgx.entity.JcgxBuild;
import com.yunda.jcbm.jcgx.entity.JcxtflFault;
import com.yunda.jcbm.jcgx.manager.JcgxBuildQueryManager;
import com.yunda.jcbm.jcgx.manager.JcxtflFaultManager;
import com.yunda.jcbm.jcxtfl.entity.JcxtflBuild;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：机车组成
 * <li>http://localhost:8080/CoreFrame/ydservices/JcgxBuildService?wsdl
 * <li>创建人： 何东
 * <li>创建日期： 2016-5-19 上午11:10:16
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 */
@Service(value="jcgxBuildWS")
public class JcgxBuildService implements IJcgxBuildService {
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());

	/** 机车构型查询业务类 */
	@Resource
	private JcgxBuildQueryManager jcgxBuildQueryManager;
	
    /** 分类编码故障现象业务类 */
	@Resource
	private JcxtflFaultManager jcxtflFaultManager;
	
	/**
	 * <li>方法名：通过分类编码获取故障现象数据
	 * <li>@param jsonObject json对象参数
	 * <li>@return
	 * <li>返回类型：String
	 * <li>说明：
	 * <li>创建人：何东
	 * <li>创建日期：2016-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public String getFlbmFault(String jsonObject) throws Exception {
		List<Map<String, Object>> faultList = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		String treeStr = "";
		try {
            String searchJson = StringUtil.nvlTrim(jsonObject, "{}");
            JcxtflFault entity = JSONUtil.read(searchJson, JcxtflFault.class);
          
            if (entity != null) {
        	    String flbm = entity.getFlbm();
    		
                faultList = jcgxBuildQueryManager.getFlbmFault(flbm);
            }
        }catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
            map.put("flag", false);
            map.put("message", "操作失败！");
        } finally{
        	if (map.get("flag") != null && !(Boolean)map.get("flag")) {
        		treeStr = JSONUtil.write(map);
        	}
        	else {
        		treeStr = JSONUtil.write(faultList);
        	}
        }
		return treeStr;
	}

	/**
	 * <li>方法名：根据车型及机车构型主键获取下级树节点列表
	 * <li>@param jsonObject json对象参数
	 * <li>@return
	 * <li>返回类型：String
	 * <li>说明：
	 * <li>创建人：何东
	 * <li>创建日期：2016-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public String getJcgxBuildTree(String jsonObject) throws Exception {
		List<Map<String, Object>> children = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		String treeStr = "";
		try {
            String searchJson = StringUtil.nvlTrim(jsonObject, "{}");
            JcgxBuild entity = JSONUtil.read(searchJson, JcgxBuild.class);
          
            //JcgxBuild entity = entities.length > 0 ? entities[0] : null; // 取第一条提票信息
            if (entity != null) {
        	    String fjdID = entity.getFjdID();
                String sycx = entity.getSycx();
                String useType = entity.getUseType();
    		
                children = jcgxBuildQueryManager.getJcgxBuildTree(fjdID, sycx, useType);
            }
        }catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
            map.put("flag", false);
            map.put("message", "操作失败！");
        } finally{
            if (map.get("flag") != null && !(Boolean)map.get("flag")) {
        		treeStr = JSONUtil.write(map);
        	}
        	else {
        		treeStr = JSONUtil.write(children);
        	}
        }
		return treeStr;
	}

	/**
	 * <li>方法名：保存分类编码的故障现象
	 * <li>@param jsonObject json对象参数
	 * <li>@return
	 * <li>返回类型：String
	 * <li>说明：
	 * <li>创建人：何东
	 * <li>创建日期：2016-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public String saveFlbmFault(String jsonObject) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String treeStr = "";
		try {
            String searchJson = StringUtil.nvlTrim(jsonObject, "{}");
            JcxtflFault[] entitys = JSONUtil.read(searchJson, JcxtflFault[].class);
          
            if (entitys != null && entitys.length > 0) {
        	    map = jcxtflFaultManager.saveFlbmFault(entitys);
            }
        }catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
            map.put("flag", false);
            map.put("message", "操作失败！");
        } finally{
        	if (map.get("success") != null && (Boolean)map.get("success")) {
        		map.put("flag", true);
                map.put("message", "操作成功！");
        	}
        	else {
        		map.put("flag", false);
                map.put("message", map.get("errMsg"));
        	}
        	
        	map.remove("errMsg");
        	map.remove("success");
        	treeStr = JSONUtil.write(map);
        }
		return treeStr;
	}
    

    /**
     * <li>说明：根据车型及机车构型获取机车组成列表
     * <li>创建人：张迪
     * <li>创建日期：2016-9-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象参数
     * @return 分类名称树节点列表
     * @throws Exception
     */
    public String getJczcmcBuildTree(String jsonObject) throws Exception {
        List<Map<String, Object>> children = new ArrayList<Map<String,Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        String treeStr = "";
        try {
            String searchJson = StringUtil.nvlTrim(jsonObject, "{}");
            JcxtflBuild entity = JSONUtil.read(searchJson, JcxtflBuild.class);
            if (entity != null) {        
                children = jcgxBuildQueryManager.getJczcmcBuildTreeAll(entity);
            }
        }catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
            map.put("flag", false);
            map.put("message", "操作失败！");
        } finally{
            if (map.get("flag") != null && !(Boolean)map.get("flag")) {
                treeStr = JSONUtil.write(map);
            }
            else {
                treeStr = JSONUtil.write(children);
            }
        }
        return treeStr;
    }
    
    /**
     * <li>说明：获取上级部件分类简称
     * <li>创建人：张迪
     * <li>创建日期：2016-9-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象参数
     * @return 上级部件分类简称
     * @throws Exception
     */
    public String getSjbjList(String jsonObject) throws Exception {
        List<Map<String, Object>> sjbjList = new ArrayList<Map<String,Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        String treeStr = "";
        try {
            String searchJson = StringUtil.nvlTrim(jsonObject, "{}");
            JcgxBuild entity = JSONUtil.read(searchJson, JcgxBuild.class);        
            sjbjList = jcgxBuildQueryManager.getSjbjList(entity);
        }catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
            map.put("flag", false);
            map.put("message", "操作失败！");
        } finally{
            if (map.get("flag") != null && !(Boolean)map.get("flag")) {
                treeStr = JSONUtil.write(map);
            }
            else {
                treeStr = JSONUtil.write(sjbjList);
            }
        }
        return treeStr;
    }
    
    /**
     * <li>说明：获取所选部件位置分类简称
     * <li>创建人：张迪
     * <li>创建日期：2016-9-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象参数
     * @return 部件分类简称
     * @throws Exception
     */
    public String getBjwzList(String jsonObject) throws Exception {
        List<Map<String, Object>> sjbjList = new ArrayList<Map<String,Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        String treeStr = "";
        try {
            String searchJson = StringUtil.nvlTrim(jsonObject, "{}");
            JcgxBuild[] entityList = JSONUtil.read(searchJson, JcgxBuild[].class);        
            sjbjList = jcgxBuildQueryManager.getBjwzList(entityList);
        }catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
            map.put("flag", false);
            map.put("message", "操作失败！");
        } finally{
            if (map.get("flag") != null && !(Boolean)map.get("flag")) {
                treeStr = JSONUtil.write(map);
            }
            else {
                treeStr = JSONUtil.write(sjbjList);
            }
        }
        return treeStr;
    }
}
