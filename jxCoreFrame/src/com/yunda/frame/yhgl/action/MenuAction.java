package com.yunda.frame.yhgl.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.base.filter.LoginPurviewCheckFilter;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcMenu;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.manager.MenuManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 操作员action
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-12-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class MenuAction extends JXBaseAction<AcMenu, AcMenu, MenuManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：当前登录人员权限树
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return void
	 * @throws Exception
	 */	
	public void authorityMenuTree() throws Exception {
		List<Map> children = new ArrayList<Map>();
		try {
			AcOperator acOperator = (AcOperator) getSession().getAttribute(LoginPurviewCheckFilter.USERS_SESSION_NAME);//从Session中获取当前登录用户
			String menuid = getRequest().getParameter("menuid");
			menuid = StringUtil.isNullOrBlank(menuid) || "ROOT_0".equals(menuid) ? null : menuid;
			children = this.manager.authorityMenuTree(acOperator.getOperatorid(), menuid);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
		} finally {
			JSONUtil.write(getResponse(), children);
		}	
	}
    
    /**
     * <li>说明：获取当前节点下所有子节点集合
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-11-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void authorityMenuTreeAll() throws Exception {
        List<Map> children = new ArrayList<Map>();
        try {
            AcOperator acOperator = (AcOperator) getSession().getAttribute(LoginPurviewCheckFilter.USERS_SESSION_NAME);//从Session中获取当前登录用户
            String menuid = getRequest().getParameter("menuid");
            menuid = StringUtil.isNullOrBlank(menuid) || "ROOT_0".equals(menuid) ? null : menuid;
            children = this.manager.authorityMenuTreeAll(acOperator.getOperatorid(), menuid);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
        } finally {
            JSONUtil.write(getResponse(), children);
        }   
    }
	
	/**
	 * <li>说明：根据树节点id返回该节点的所有子节点信息(符合extjs.TreeNode的JSON字符串)
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-12-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return void
	 * @throws Exception
	 */		
	public void tree() throws Exception{
		List<HashMap> children = new ArrayList<HashMap>();
		try {
			children = manager.getTree(StringUtil.nvlTrim(getRequest().getParameter("parentsid"), null));
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
		} finally {
			JSONUtil.write(getResponse(), children);
		}		
	}
	/**
	 * <li>说明：根据ids级联删除菜单节点（及子节点）
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-12-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return void
	 * @throws Exception
	 */
	public String deleteByIds() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			if(ids == null || ids.length < 1)	throw new Exception("删除操作失败，参数ids为空！");
			String idxs = StringUtil.join(ids, ",");
			idxs = manager.getCascadeIdx("'"+idxs.replaceAll(",", "','")+"'");
			this.manager.deleteByIds(idxs.replaceAll(",", "','"));			
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
		
		try {
			String idxs=null;
			if (ids.length == 1) {
				idxs = ids[0];
			}
			if(idxs!=null){
				idxs=manager.getCascadeIdx("'"+idxs.replaceAll(",", "','")+"'");
				this.manager.deleteByIds(idxs.replaceAll(",", "','"));
				refreshFlag = "refresh";
				this.ajaxMessage(MSG_DELETE_SUCCESS);
			}else{
				this.ajaxMessage("【删除】失败！");				
			}
		} catch (Exception ex) {
			this.ajaxMessage(ex.getMessage());
		}
		return null;
	}	
	
	/**
	 * 
	 * <li>说明：重写基类保存方法，获取页面传入的参数isAdd，用以判断是否新增操作
	 * <li>创建人：谭诚
	 * <li>创建日期：2014-1-7
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unused")
	public void saveOrUpdate() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			AcMenu t = (AcMenu)JSONUtil.read(getRequest(), entity.getClass());
			String isAdd = getRequest().getParameter("isAdd");
			String[] errMsg = this.manager.validateUpdate(t,isAdd);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.saveOrUpdate(t,isAdd);
				//返回记录保存成功的实体对象
				map.put("entity", t);  
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
}
