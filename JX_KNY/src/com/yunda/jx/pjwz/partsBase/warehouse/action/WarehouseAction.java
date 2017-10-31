package com.yunda.jx.pjwz.partsBase.warehouse.action; 

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjwz.partsBase.warehouse.entity.Warehouse;
import com.yunda.jx.pjwz.partsBase.warehouse.manager.WarehouseManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：Warehouse控制器, 库房基本信息
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-08-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class WarehouseAction extends JXBaseAction<Warehouse, Warehouse, WarehouseManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	//定义一个修改标志
	private String flag ;
	//定义规格型号的数组
	private String[] specificationModel ;
	/**
	 * <li>说明：分页查询库位
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-8-31
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void pageList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
			String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
			String flag = StringUtil.nvlTrim( req.getParameter("flag"), "0" );
//			String specificationModel = StringUtil.nvlTrim( req.getParameter("specificationModel"));//用于确认申请时使用
			Warehouse entity = (Warehouse)JSONUtil.read(searchJson, entitySearch.getClass());
			SearchEntity<Warehouse> searchEntity = new SearchEntity<Warehouse>(entity, getStart(), getLimit(), null);
			
			String status = StringUtil.nvlTrim(this.getRequest().getParameter("status"));
			if ("".equals(status)) {
				status = "1";
			}
			map = this.manager.findPageList(searchEntity,status,flag,specificationModel).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	/**
	 * <li>说明：启用
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-8-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */
	public void updateStart() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			this.getManager().updateStatus(flag, (Serializable[])ids);
			map.put("success", "true");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	/**
	 * 
	 * <li>说明：库房下拉组件数据获取方法
	 * <li>创建人：程锐
	 * <li>创建日期：2012-9-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void combolist() throws Exception{		
		String jsonStr = "";
		try{			
			jsonStr = this.manager.combolist();		
		}catch(Exception e){
			ExceptionUtil.process(e, logger);
		}finally {
			this.getResponse().setContentType("text/json;charset=UTF-8");
			this.getResponse().getWriter().print(jsonStr);
		}		
		
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String[] getSpecificationModel() {
		return specificationModel;
	}
	public void setSpecificationModel(String[] specificationModel) {
		this.specificationModel = specificationModel;
	}

}