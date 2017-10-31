package com.yunda.zb.zbfw.action; 

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.zb.zbfw.entity.ZbFwcx;
import com.yunda.zb.zbfw.manager.ZbFwcxManager;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbFwcx控制器, 整备范围适用车型
 * <li>创建人：王利成
 * <li>创建日期：2015-01-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class ZbFwcxAction extends JXBaseAction<ZbFwcx, ZbFwcx, ZbFwcxManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * 
	 * <li>说明：获得整备范围车型信息
	 * <li>创建人：王利成
	 * <li>创建日期：2015-1-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	public void getZbFwcxList() throws Exception{
	       Map<String,Object> map = new HashMap<String,Object>();
			try {
				HttpServletRequest req = getRequest();
				String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
				ZbFwcx entity = (ZbFwcx)JSONUtil.read(searchJson, entitySearch.getClass());
				SearchEntity<ZbFwcx> searchEntity = new SearchEntity<ZbFwcx>(entity, getStart(), getLimit(), getOrders());
				map = this.manager.getZbFwcxList(searchEntity).extjsStore();
			} catch (Exception e) {
				ExceptionUtil.process(e, logger, map);
			} finally {
				JSONUtil.write(this.getResponse(), map);
			}	         
		}
	/**
	 * 
	 * <li>说明：接受批量保存或更新记录请求，向客户端返回操作结果（JSON格式），实体类对象必须符合检修系统表设计，主键名必须为idx（字符串uuid）
	 * <li>创建人：王利成
	 * <li>创建日期：2015-1-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
    public void saveOrUpdateList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
        	ZbFwcx[] objList = (ZbFwcx[])JSONUtil.read(getRequest(), ZbFwcx[].class);
            String[] errMsg = this.manager.saveOrUpdateList(objList);
            if (errMsg == null || errMsg.length < 1) {
                map.put("success", "true");
            }else{
                map.put("errMsg", errMsg);
            }
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
	
}