package com.yunda.jx.pjwz.partsBase.professionaltype.action; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjwz.partsBase.professionaltype.entity.ProfessionalType;
import com.yunda.jx.pjwz.partsBase.professionaltype.manager.ProfessionalTypeManager;

/**
 * <li>标题: 机车检修管理信息系统</li>
 * <li>说明：ProfessionalType控制器, 专业类型</li>
 * <li>版权: Copyright (c) 2008 运达科技公司</li>
 * <li>创建日期：2012-08-12 
 * <li>修改人: </li>
 * <li>修改日期：</li>
 * <li>修改内容：</li>
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ProfessionalTypeAction extends JXBaseAction<ProfessionalType, ProfessionalType, ProfessionalTypeManager>{
	
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * /jsp/jx/pjwz/partbase/ProfessionalType.js调用
	 * <li>说明：专业类型查询
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-8-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */
	public void list() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
			String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
			ProfessionalType entity = (ProfessionalType)JSONUtil.read(searchJson, entitySearch.getClass());
			SearchEntity<ProfessionalType> searchEntity = new SearchEntity<ProfessionalType>(entity, getStart(), getLimit(), null);
			
			String status = StringUtil.nvlTrim(this.getRequest().getParameter("status"));
			if ("".equals(status)) {
				status = "0,1";
			}
			map = this.manager.findPageList(searchEntity,status).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}

	/**
	 * /jsp/jx/pjwz/partbase/ProfessionalType.js调用
	 * <li>说明：根据树节点id返回该节点的所有子节点信息(符合extjs.TreeNode的JSON字符串)
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-10-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */		
	public void tree() throws Exception{
		String parentIDX = StringUtil.nvlTrim(getRequest().getParameter("parentIDX"), "ROOT_0");
		String status = StringUtil.nvlTrim(getRequest().getParameter("status"),"0,1");
		List<HashMap<String, Object>> children=manager.findProfessionalTree(parentIDX,status);
		JSONUtil.write(getResponse(), children);
	}
	
	/**
	 * /jsp/jx/pjwz/partbase/ProfessionalType.js调用
	 * <li>说明：启用
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-10-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */
	public void startUse() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			this.getManager().updateStartUse(ids);
			map.put("success", "true");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	/**
	 * /jsp/jx/pjwz/partbase/ProfessionalType.js调用
	 * <li>说明：作废
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-10-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */
	public void invalid() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			this.getManager().updateInvalid(ids);
			map.put("success", "true");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	/**
	 * /jsp/jx/pjwz/partbase/ProfessionalType.js调用
	 * <li>说明：级联删除
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-10-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	public void logicDelete() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			this.manager.deleteCascade(ids);
			map.put("success", "true");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
	 * /jx/js/component/pjwz/ProfessionalType.js
	 * <li>方法名：comboTree
	 * <li>@throws Exception
	 * <li>返回类型：void
	 * <li>说明：专业类型下拉树组件
	 * <li>创建人：程锐
	 * <li>创建日期：2012-8-31	 
	 */
	public void comboTree() throws Exception{
		String parentIDX = StringUtil.nvlTrim(getRequest().getParameter("parentIDX"), "ROOT_0");
		String status = StringUtil.nvlTrim(getRequest().getParameter("status"),ProfessionalType.status_start+"");
		List<HashMap<String, Object>> children=manager.findProfessionalTree(parentIDX,status);
		JSONUtil.write(getResponse(), children);		
	}
	/**
	 * /jsp/jx/js/component/pjwz/ProfessionalType_check.js
	 * <li>说明：带复选框的专业类型树
	 * <li>创建人：程梅
	 * <li>创建日期：2013-7-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void comboTreeForCheck() throws Exception{
		String parentIDX = StringUtil.nvlTrim(getRequest().getParameter("parentIDX"), "ROOT_0");
		String status = StringUtil.nvlTrim(getRequest().getParameter("status"),ProfessionalType.status_start+"");
		List<HashMap<String, Object>> children=manager.findProfessionalTreeForCheck(parentIDX,status);
		JSONUtil.write(getResponse(), children);		
	}
	/**
	 * 
	 * <li>说明：查询专业类型列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void queryProfessionalList() throws Exception {
    	Map<String, Object> map = new HashMap<String,Object>();
		try {			
			map = this.manager.queryProfessionalList(getStart(), getLimit()).extjsResult();
			map.put("id", "professionalTypeID");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
    }
}