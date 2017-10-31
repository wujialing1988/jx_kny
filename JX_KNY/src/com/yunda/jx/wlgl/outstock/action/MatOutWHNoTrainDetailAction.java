package com.yunda.jx.wlgl.outstock.action; 

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.jx.wlgl.outstock.entity.MatOutWHNoTrainDetail;
import com.yunda.jx.wlgl.outstock.manager.MatOutWHNoTrainDetailManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatOutWHNoTrainDetail控制器, 机车外用料明细
 * <li>创建人：何涛
 * <li>创建日期：2014-09-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class MatOutWHNoTrainDetailAction extends JXBaseAction<MatOutWHNoTrainDetail, MatOutWHNoTrainDetail, MatOutWHNoTrainDetailManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-26
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws JsonMappingException
	 * @throws IOException
	 */
//	public void findPageList() throws JsonMappingException, IOException {
//		HttpServletRequest req = getRequest();
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			// 查询实体
//			String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
//			entity = (MatOutWHNoTrainDetail)JSONUtil.read(searchJson, entity.getClass());
//			SearchEntity<MatOutWHNoTrainDetail> searchEntity = new SearchEntity<MatOutWHNoTrainDetail>(entity, getStart(), getLimit(), getOrders());
//			map = this.manager.findPageList(searchEntity).extjsStore();
//		} catch (Exception e) {
//			ExceptionUtil.process(e, logger);
//		} finally {
//			JSONUtil.write(getResponse(), map);
//		}
//	}
	
}