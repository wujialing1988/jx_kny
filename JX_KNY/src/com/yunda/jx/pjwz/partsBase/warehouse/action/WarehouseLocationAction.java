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
import com.yunda.jx.pjwz.partsBase.warehouse.entity.WarehouseLocation;
import com.yunda.jx.pjwz.partsBase.warehouse.manager.WarehouseLocationManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WarehouseLocation控制器, 库位的基本信息表
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-08-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class WarehouseLocationAction extends JXBaseAction<WarehouseLocation, WarehouseLocation, WarehouseLocationManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	//定义状态修改标志
	private String flag ;
	
	/**
	 * /jsp/jx/pjwz/stockbase/WarehouseLocation.js
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
	public void pageList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
			String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
			WarehouseLocation entity = (WarehouseLocation)JSONUtil.read(searchJson, entitySearch.getClass());
			SearchEntity<WarehouseLocation> searchEntity = new SearchEntity<WarehouseLocation>(entity, getStart(), getLimit(), null);
			
			String status = "";
			map = this.manager.findPageList(searchEntity,status).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
	 * /jsp/jx/pjwz/stockbase/WarehouseLocation.js
	 * <li>说明：启用作废状态修改
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-10-16
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
	 * /jsp/jx/js/component/pjwz/WarehouseLocationCombo.js
	 * <li>说明：查询库位下拉控件信息
	 * <li>创建人：程梅
	 * <li>创建日期：2013-10-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
    public void pageListForCombo() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String queryHql = req.getParameter("queryHql");
            String queryParams = req.getParameter("queryParams");
            Map queryParamsMap = new HashMap();
            if (!StringUtil.isNullOrBlank(queryParams)) {
                queryParamsMap = JSONUtil.read(queryParams, Map.class);
            }
//          query参数是获取EXTJS的combox控件捕获的键盘输入文字
            String queryValue = StringUtil.nvlTrim(req.getParameter("query"), "");
            map = this.manager.page(queryValue,queryParamsMap, getStart(), getLimit(), queryHql);
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}