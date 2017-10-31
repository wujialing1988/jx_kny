package com.yunda.frame.yhgl.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.manager.EosDictEntrySelectManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 业务字典选择控件控制类
 * <li>创建人：程锐
 * <li>创建日期：2012-9-5
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class EosDictEntrySelectAction extends JXBaseAction<EosDictEntry, EosDictEntry, EosDictEntrySelectManager> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：业务字典选择控件下拉列表
     * <li>创建人：程锐
     * <li>创建日期：2012-9-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void combolist() throws Exception {
        String jsonStr = "";
        HttpServletRequest req = this.getRequest();
        HttpServletResponse res = this.getResponse();
        try {
            String status = StringUtil.nvlTrim(req.getParameter("status"), "0");
            String dictTypeId = StringUtil.nvlTrim(req.getParameter("dicttypeid"), "");
            String queryWhere = StringUtil.nvlTrim(req.getParameter("queryWhere"), "");
            String hasEmpty = StringUtil.nvlTrim(req.getParameter("hasEmpty"), "false");
            
            if (!StringUtil.isNullOrBlank(dictTypeId)) {
                jsonStr = this.manager.combolist(status, dictTypeId, queryWhere, Boolean.parseBoolean(hasEmpty));
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
        } finally {
            res.setContentType("text/json;charset=UTF-8");
            res.getWriter().print(jsonStr);
        }
        
    }
    
    /**
     * <li>说明：数据字典下拉树（使用BaseComboTree通用下拉树控件时调用）
     * <li>创建人：程锐
     * <li>创建日期：2014-5-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void tree() throws Exception {
        HttpServletRequest req = this.getRequest();
        List<HashMap> children = manager.findEosDictTreeData(req);
        JSONUtil.write(getResponse(), children);
    }
    
    /**
     * <li>说明：获取数据字典列表
     * <li>创建人：程锐
     * <li>创建日期：2014-10-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void queryDictList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String dictTypeID = getRequest().getParameter("dictTypeID");
            map = this.manager.queryDictList(dictTypeID).extjsResult();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：数据字典下拉树（重写父节点不能被选中）
     * <li>创建人：张迪
     * <li>创建日期：2016-8-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void queryChildTree() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
        	HttpServletRequest req = this.getRequest();
            List<HashMap> children = manager.findEosDictTreeChildData(req);
            JSONUtil.write(getResponse(), children);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            JSONUtil.write(this.getResponse(), map);
        } 
    }
    
    /**
     * <li>说明：获取数据字典中的默认值（默认排序选序号最小的）[只能支持非树形结构的数据字典]
     * <li>创建人：林欢
     * <li>创建日期：2017-3-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getDeafultValue() throws Exception {
    	Map<String, Object> map = new HashMap<String, Object>();
        try {
        	HttpServletRequest req = this.getRequest();
        	
        	String dicttypeid = req.getParameter("dicttypeid");//获取字典idx
        	
        	//封装后台查询参数
        	Map<String, String> paramMap = new HashMap<String, String>();
        	paramMap.put("dicttypeid", dicttypeid);
        	
            Map<String, String> defaultObj = manager.getDeafultValue(paramMap);
            
            //封装返回参数
            map.put("defaultObj", defaultObj);//返回的默认对象
            map.put("success", true);//返回成功标示
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
        	JSONUtil.write(this.getResponse(), map);
        }
    }
}
