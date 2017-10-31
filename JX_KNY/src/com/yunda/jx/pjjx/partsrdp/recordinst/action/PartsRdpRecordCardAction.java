package com.yunda.jx.pjjx.partsrdp.recordinst.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.partsrdp.IPartsRdpStatus;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQCParticipant.QCEmp;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordCard;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordRIAndDI;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordCardManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpRecordCard控制器, 配件检修记录卡实例
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
public class PartsRdpRecordCardAction extends JXBaseAction<PartsRdpRecordCard, PartsRdpRecordCard, PartsRdpRecordCardManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    
	/**
	 * <li>说明：批量领活
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
    @Deprecated
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
			map.put(Constants.SUCCESS, false);
			map.put(Constants.ERRMSG, e.getMessage());
			ExceptionUtil.process(e, logger);
		} finally { 
			JSONUtil.write(getResponse(), map);
		}
	}
	
	/**
	 * <li>说明：领活
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
    @Deprecated
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
			map.put(Constants.SUCCESS, false);
			map.put(Constants.ERRMSG, e.getMessage());
			ExceptionUtil.process(e, logger);
		} finally { 
			JSONUtil.write(getResponse(), map);
		}
	}
	
	/**
	 * <li>说明：批量撤销领活
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
    @Deprecated
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
			map.put(Constants.SUCCESS, false);
			map.put(Constants.ERRMSG, e.getMessage());
			ExceptionUtil.process(e, logger);
		} finally { 
			JSONUtil.write(getResponse(), map);
		}
	}
	
	/**
	 * <li>说明：批量销活
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
    @Deprecated
	public void finishBatchJob() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String entityJson = StringUtil.nvl(getRequest().getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
			entity = JSONUtil.read(entityJson, PartsRdpRecordCard.class);
			// 指派的质量检查人员
			String qcEmpJson = StringUtil.nvl(getRequest().getParameter("qcEmpJson"), "[]");
			QCEmp[] qcEmps = JSONUtil.read(qcEmpJson, QCEmp[].class);
			
			String[] errMsg = this.manager.validateStatus(ids, IPartsRdpStatus.CONST_STR_STATUS_DCL);
			if (null == errMsg) {
				this.manager.finishBatchJob(ids, entity, qcEmps);
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
	 * <li>创建日期：2014-12-13
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
            entity = JSONUtil.read(entityJson, PartsRdpRecordCard.class);
			// 指派的质量检查人员
			String qcEmpJson = StringUtil.nvl(getRequest().getParameter("qcEmpJson"), "[]");
			QCEmp[] qcEmps = JSONUtil.read(qcEmpJson, QCEmp[].class);
            
            // 批量保存配件检修检测项和数据项
            String riAndDi = StringUtil.nvl(getRequest().getParameter("riAndDi"), "[]");
            PartsRdpRecordRIAndDI[] riAndDis = JSONUtil.read(riAndDi, PartsRdpRecordRIAndDI[].class);
            
            // 数据库中最新的实体信息
            PartsRdpRecordCard currentEntity = this.manager.getModelById(entity.getIdx());
            
            // 销活
            if (IPartsRdpStatus.CONST_STR_STATUS_DCL.equals(currentEntity.getStatus())) {
                entity = this.manager.completeJob(entity, qcEmps, riAndDis);
                map.put(Constants.SUCCESS, true);
                map.put(Constants.ENTITY, entity);
            // 修改已经销活的数据
            } else if (IPartsRdpStatus.CONST_STR_STATUS_ZLJYZ.equals(currentEntity.getStatus()) || IPartsRdpStatus.CONST_STR_STATUS_XJ.equals(currentEntity.getStatus())) {
                entity = this.manager.updateJob(entity, riAndDis);
                map.put(Constants.SUCCESS, true);
                map.put(Constants.ENTITY, entity);
            } else {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, PartsRdpRecordCardManager.checkEntityStatus(null, entity.getStatus()));
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
	 * <li>说明：暂存
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
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
			entity = JSONUtil.read(entityJson, PartsRdpRecordCard.class);
			// 指派的质量检查人员
			String qcEmpJson = StringUtil.nvl(getRequest().getParameter("qcEmpJson"), "[]");
			QCEmp[] qcEmps = JSONUtil.read(qcEmpJson, QCEmp[].class);
            
            // 批量保存配件检修检测项和数据项
            String riAndDi = StringUtil.nvl(getRequest().getParameter("riAndDi"), "[]");
            PartsRdpRecordRIAndDI[] riAndDis = JSONUtil.read(riAndDi, PartsRdpRecordRIAndDI[].class);
            
            // 数据库中最新的实体信息
            PartsRdpRecordCard currentEntity = this.manager.getModelById(entity.getIdx());
            
			// 暂存
            if (IPartsRdpStatus.CONST_STR_STATUS_DCL.equals(currentEntity.getStatus())) {
                entity = this.manager.saveTemporary(entity, qcEmps, riAndDis);
                map.put(Constants.SUCCESS, true);
                map.put(Constants.ENTITY, entity);
            // 修改已经销活的数据
            } else if (IPartsRdpStatus.CONST_STR_STATUS_ZLJYZ.equals(currentEntity.getStatus()) || IPartsRdpStatus.CONST_STR_STATUS_XJ.equals(currentEntity.getStatus())) {
                entity = this.manager.updateJob(entity, riAndDis);
                map.put(Constants.SUCCESS, true);
                map.put(Constants.ENTITY, entity);
            } else {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, PartsRdpRecordCardManager.checkEntityStatus(null, entity.getStatus()));
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
     * <li>说明：分页联合查询，查询出相应记录工单下未处理的检修检测项记录数[riCounts]（PAD专用）
     * <li>创建人：何涛
     * <li>创建日期：2016-3-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void queryPageList() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = this.manager.queryPageList(getWhereList(), getStart(), getLimit()).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        }  finally { 
            JSONUtil.write(getResponse(), map);
        }
    }
	
    /**
     * <li>说明：查询检修记录卡详情
     * <li>创建人：张迪
     * <li>创建日期：2016-6-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException
     */
    public void integrateQueryCardList() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String rdpRecordIDX = this.getRequest().getParameter("rdpRecordIDX");
            String partsRdpRecordCardList  = this.manager.integrateQueryCardList(rdpRecordIDX);
            map.put("partsRdpRecordCardList", partsRdpRecordCardList);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    /**
     * <li>说明：通过节点查询检修记录卡详情
     * <li>创建人：张迪
     * <li>创建日期：2017-5-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException
     */
    public void findCardListByNodeIDX() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String rdpNodeIDX = this.getRequest().getParameter("rdpNodeIDX");
            String rdpIDX = this.getRequest().getParameter("rdpIDX");
            String partsRdpRecordCardList  = this.manager.findCardListByNodeIDX(rdpIDX, rdpNodeIDX);
            map.put("partsRdpRecordCardList", partsRdpRecordCardList);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：获取配件检修记录单列表后对接可视化数据
     * <li>创建人：林欢   
     * <li>创建日期：2016-06-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */ 
/*    @SuppressWarnings("unchecked")
    public void pageQuery() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            QueryCriteria<PartsRdpRecordCard> query = new QueryCriteria<PartsRdpRecordCard>(getQueryClass(),getWhereList(), getOrderList(), getStart(), getLimit());
            Page<PartsRdpRecordCard> page = this.manager.pageQuery(query);
            map = page.extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }*/
    
}