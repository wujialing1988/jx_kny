package com.yunda.jx.pjjx.partsrdp.rdpnotice.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.partsrdp.IPartsRdpStatus;
import com.yunda.jx.pjjx.partsrdp.rdpnotice.entity.PartsRdpNotice;
import com.yunda.jx.pjjx.partsrdp.rdpnotice.manager.PartsRdpNoticeManager;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordCardManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpNotice控制器, 提票单
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class PartsRdpNoticeAction extends JXBaseAction<PartsRdpNotice, PartsRdpNotice, PartsRdpNoticeManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：批量领活
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void receiveBatchJob() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String[] errMsg = this.manager.validateStatus(ids, IPartsRdpStatus.CONST_STR_STATUS_DLQ);
			if (null == errMsg) {
				this.manager.startUpBatchJob(ids);
				map.put(Constants.SUCCESS, true);
			} else {
				map.put(Constants.SUCCESS, false);
				map.put(Constants.ERRMSG, errMsg);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally { 
			JSONUtil.write(getResponse(), map);
		}
	}
	
	/**
	 * <li>说明：领活
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void receiveJob() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String[] errMsg = this.manager.validateStatus(new String[]{id}, IPartsRdpStatus.CONST_STR_STATUS_DLQ);
			if (null == errMsg) {
				entity = this.manager.startUpJob(id);
				map.put(Constants.SUCCESS, true);
				map.put(Constants.ENTITY, entity);
			} else {
				map.put(Constants.SUCCESS, false);
				map.put(Constants.ERRMSG, errMsg);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally { 
			JSONUtil.write(getResponse(), map);
		}
	}
	
	/**
	 * <li>说明：批量撤销领活
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void cancelBatchJob() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String[] errMsg = this.manager.validateStatus(ids, IPartsRdpStatus.CONST_STR_STATUS_DCL);
			if (null == errMsg) {
				this.manager.giveUpBatchJob(ids);
				map.put(Constants.SUCCESS, true);
			} else {
				map.put(Constants.SUCCESS, false);
				map.put(Constants.ERRMSG, errMsg);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally { 
			JSONUtil.write(getResponse(), map);
		}
	}
	
	/**
	 * <li>说明：批量销活
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void finishBatchJob() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			PartsRdpNotice tempEntity = JSONUtil.read(getRequest(), PartsRdpNotice.class);
			String[] errMsg = this.manager.validateStatus(ids, IPartsRdpStatus.CONST_STR_STATUS_DCL);
			if (null == errMsg) {
				this.manager.finishBatchJob(ids, tempEntity);
				map.put(Constants.SUCCESS, true);
			} else {
				map.put(Constants.SUCCESS, false);
				map.put(Constants.ERRMSG, errMsg);
			}
		} catch (Exception e) {
			map.put(Constants.SUCCESS, false);
			map.put(Constants.ERRMSG, e.getMessage());
			ExceptionUtil.process(e, logger);
		} finally { 
			JSONUtil.write(getResponse(), map);
		}
	}
	
	/**
	 * <li>说明：销活
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void finishJob() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String entityJson = StringUtil.nvl(getRequest().getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
			entity = JSONUtil.read(entityJson, PartsRdpNotice.class);
            
			// 数据库中最新的实体信息
            PartsRdpNotice currentEntity = this.manager.getModelById(entity.getIdx());
            
            // 销活
            if (IPartsRdpStatus.CONST_STR_STATUS_DCL.equals(currentEntity.getStatus())) {
                entity = this.manager.finishJob(entity);
                map.put(Constants.SUCCESS, true);
                map.put(Constants.ENTITY, entity);
            // 修改已经销活的数据
            } else if (IPartsRdpStatus.CONST_STR_STATUS_ZLJYZ.equals(currentEntity.getStatus()) || IPartsRdpStatus.CONST_STR_STATUS_XJ.equals(currentEntity.getStatus())) {
                entity = this.manager.saveTemporary(entity);
                map.put(Constants.SUCCESS, true);
                map.put(Constants.ENTITY, entity);
            } else {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, PartsRdpRecordCardManager.checkEntityStatus(null, entity.getStatus()));
            }
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally { 
			JSONUtil.write(getResponse(), map);
		}
	}
	
	/**
	 * <li>说明：暂存
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void saveTemporary() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String entityJson = StringUtil.nvl(getRequest().getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
			entity = JSONUtil.read(entityJson, PartsRdpNotice.class);
            
			// 数据库中最新的实体信息
            PartsRdpNotice currentEntity = this.manager.getModelById(entity.getIdx());
            
            // 暂存
            if (IPartsRdpStatus.CONST_STR_STATUS_DCL.equals(currentEntity.getStatus())
                    || IPartsRdpStatus.CONST_STR_STATUS_ZLJYZ.equals(currentEntity.getStatus()) 
                    || IPartsRdpStatus.CONST_STR_STATUS_XJ.equals(currentEntity.getStatus())
            ) {
                entity = this.manager.saveTemporary(entity);
                map.put(Constants.SUCCESS, true);
                map.put(Constants.ENTITY, entity);
            // 修改已经销活的数据
            } else {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, PartsRdpRecordCardManager.checkEntityStatus(null, entity.getStatus()));
            }
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally { 
			JSONUtil.write(getResponse(), map);
		}
	}
	
	/**
	 * <li>说明：提票
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void submitNotice() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		try {
			PartsRdpNotice entity = JSONUtil.read(req, PartsRdpNotice.class);
			
			String[] errMsg = this.manager.validateUpdate(entity);
			if (null != errMsg) {
				map.put(Constants.SUCCESS, false);
				map.put(Constants.ERRMSG, errMsg);
			} else {
				this.manager.submitNotice(entity);
				map.put(Constants.SUCCESS, true);
			}
		} catch (Exception e) {
			map.put(Constants.SUCCESS, false);
			map.put(Constants.ERRMSG, e.getMessage());
		} finally { 
			JSONUtil.write(getResponse(), map);
		}
	}
	
    /**
     * <li>说明：提票（手持PDA）
     * <li>创建人：程锐
     * <li>创建日期：2015-11-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
	public void submitNoticeForPDA() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		try {
			String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
			PartsRdpNotice entity = JSONUtil.read(entityJson, PartsRdpNotice.class);
			
			String[] errMsg = this.manager.validateUpdate(entity);
			if (null != errMsg) {
				map.put(Constants.SUCCESS, false);
				map.put(Constants.ERRMSG, errMsg);
			} else {
				this.manager.submitNotice(entity);
				map.put(Constants.SUCCESS, true);
			}
		} catch (Exception e) {
			map.put(Constants.SUCCESS, false);
			map.put(Constants.ERRMSG, e.getMessage());
		} finally { 
			JSONUtil.write(getResponse(), map);
		}
	}
	
}