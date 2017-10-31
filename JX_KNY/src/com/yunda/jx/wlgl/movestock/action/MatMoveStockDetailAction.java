package com.yunda.jx.wlgl.movestock.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.wlgl.movestock.entity.MatMoveStockDetail;
import com.yunda.jx.wlgl.movestock.manager.MatMoveStockDetailManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatMoveStockDetail控制器, 物料移库明细
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-09-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class MatMoveStockDetailAction extends JXBaseAction<MatMoveStockDetail, MatMoveStockDetail, MatMoveStockDetailManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * <li>说明：分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-16
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void findPageList() throws JsonMappingException, IOException {
		HttpServletRequest req = getRequest();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 查询实体
			String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
			entity = (MatMoveStockDetail)JSONUtil.read(searchJson, entity.getClass());
			SearchEntity<MatMoveStockDetail> searchEntity = new SearchEntity<MatMoveStockDetail>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.findPageList(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
	}
}