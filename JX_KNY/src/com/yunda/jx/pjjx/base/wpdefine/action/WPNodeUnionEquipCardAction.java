package com.yunda.jx.pjjx.base.wpdefine.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjjx.base.wpdefine.entity.WPNodeUnionEquipCard;
import com.yunda.jx.pjjx.base.wpdefine.manager.WPNodeUnionEquipCardManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WPNodeUnionEquipCard控制器, 作业节点所挂机务设备工单
 * <li>创建人：程梅
 * <li>创建日期：2014-11-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class WPNodeUnionEquipCardAction extends JXBaseAction<WPNodeUnionEquipCard, WPNodeUnionEquipCard, WPNodeUnionEquipCardManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * 
	 * <li>说明：查询需求单对应机务设备作业工单列表信息
	 * <li>创建人：程梅
	 * <li>创建日期：2015-1-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @throws 抛出异常列表
	 */
	public void findPageQuery() throws Exception{
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			String wPIDX = getRequest().getParameter("wPIDX");//作业流程主键
			map=this.manager.findPageQuery(wPIDX, getStart(), getLimit()).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}finally{
			JSONUtil.write(getResponse(), map);
		}
	}
	/**
	 * 
	 * <li>说明：批量保存新增的【作业流程所用设备作业工单】
	 * <li>创建人：程梅
	 * <li>创建日期：2015-1-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @throws 抛出异常列表
	 */
	public void save() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			WPNodeUnionEquipCard[] cards = JSONUtil.read(getRequest(), WPNodeUnionEquipCard[].class);
			String[] errMsg = this.manager.validateUpdate(cards);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.saveOrUpdate(cards);
				map.put(Constants.SUCCESS, true);
			} else {
				map.put(Constants.SUCCESS, false);
				map.put(Constants.ERRMSG, errMsg);
			}			
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
}