package com.yunda.freight.base.deskprivilige.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.freight.base.deskprivilige.entity.DeskPrivilige;
import com.yunda.freight.base.deskprivilige.manager.DeskPriviligeManager;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 桌面权限Action
 * <li>创建人：伍佳灵
 * <li>创建日期：2018-01-01 11:36:09
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public class DeskPriviligeAction extends JXBaseAction<DeskPrivilige, DeskPrivilige, DeskPriviligeManager> {

    /**  序列  */
    private static final long serialVersionUID = 1L;
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
	/**
     * <li>说明：树形列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param reportType 报表分类
     * @return
     */
	public void saveDeskPrivilige() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			DeskPrivilige entity = (DeskPrivilige)JSONUtil.read(getRequest(), DeskPrivilige.class);
			String[] errMsg = this.manager.validateUpdate(entity);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.saveDeskPrivilige(entity);
				map.put("entity", entity);  
				map.put("success", true);
			} else {
				map.put("success", false);
				map.put("errMsg", errMsg);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
   
	/**
     * <li>说明：获取车辆基本信息（货车）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-06
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findDeskPriviligeList() throws Exception {
    	Map<String, Object> map = new HashMap<String,Object>();
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            list = this.manager.findDeskPriviligeList();
            map.put("root", list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
        } finally {
            JSONUtil.write(getResponse(), map);
        }   
    }
    
	/**
     * <li>说明：获取桌面权限实体
     * <li>创建人：伍佳灵
     * <li>创建日期：2018-01-01
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return
     */
    public void getDeskPriviligeObj() throws Exception {
    	Map<String, Object> map = new HashMap<String,Object>();
        try {
        	map = this.manager.getDeskPriviligeObj();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
        } finally {
            JSONUtil.write(getResponse(), map);
        }   
    }
    
    
    
}
