package com.yunda.jx.pjwz.partsBase.partstype.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjwz.partsBase.partstype.entity.PartsExtendNo;
import com.yunda.jx.pjwz.partsBase.partstype.entity.PartsExtendNoBean;
import com.yunda.jx.pjwz.partsBase.partstype.manager.PartsExtendNoManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsExtendNo控制器, 配件扩展编号
 * <li>创建人：程锐
 * <li>创建日期：2014-08-14
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class PartsExtendNoAction extends JXBaseAction<PartsExtendNo, PartsExtendNo, PartsExtendNoManager> {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * <li>说明：保存配件扩展编号
	 * <li>创建人：程锐
	 * <li>创建日期：2014-8-15
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	public void saveExtendNo() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String itemDataArray = StringUtil.nvlTrim(getRequest().getParameter("itemDataArray"), "[]");
			PartsExtendNo[] items = (PartsExtendNo[]) JSONUtil.read(itemDataArray, PartsExtendNo[].class);
			this.manager.saveExtendNo(items);
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
			map.put("success", false);
		}
		JSONUtil.write(this.getResponse(), map);
	}
	/**
	 * 
	 * <li>说明：配件扩展编号列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-8-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void getExtendNoList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String partsTypeIDX = getRequest().getParameter("partsTypeIDX");
			map = this.manager.getExtendNoList(partsTypeIDX).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	/**
	 * 
	 * <li>说明：根据配件规格型号获取配件扩展编号json实体对象列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-8-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	public void getUsedJsonByPartsType() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		List<PartsExtendNoBean> partsExtendNoBeanList = new ArrayList<PartsExtendNoBean>();
		try {
			String partsTypeIDX = getRequest().getParameter("partsTypeIDX");
			partsExtendNoBeanList = this.manager.getUsedJsonByPartsType(partsTypeIDX);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), partsExtendNoBeanList);
		}
	}
	
	/**
	 * 
	 * <li>说明：验证一种规格型号的配件是否有扩展编号
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-03
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	public void hasPartsExtendNo() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String partsTypeIdx = getRequest().getParameter("partsTypeIdx");  //规格型号表主键
			if(this.manager.hasPartsExtendNo(partsTypeIdx)){
				map.put("result", "yes");
	        }else{
	            map.put("result", "no");
	        }
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
}