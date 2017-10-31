package com.yunda.jx.pjjx.partsrdp.recordinst.manager;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.persistence.Column;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.pjjx.base.qcitemdefine.entity.QCEmp;
import com.yunda.jx.pjjx.base.qcitemdefine.entity.QCEmpOrg;
import com.yunda.jx.pjjx.base.qcitemdefine.entity.QCItem;
import com.yunda.jx.pjjx.base.qcitemdefine.manager.QCEmpManager;
import com.yunda.jx.pjjx.base.qcitemdefine.manager.QCEmpOrgManager;
import com.yunda.jx.pjjx.base.qcitemdefine.manager.QCItemManager;
import com.yunda.jx.pjjx.partsrdp.IPartsRdpStatus;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpManager;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpWorkerManager;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQCParticipant;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQR;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQRBackRepairLog;
import com.yunda.jx.pjjx.partsrdp.qcinst.manager.PartsRdpQCParticipantManager;
import com.yunda.jx.pjjx.partsrdp.qcinst.manager.PartsRdpQRBackRepairLogManager;
import com.yunda.jx.pjjx.partsrdp.qcinst.manager.PartsRdpQRManager;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordCard;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordDI;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordRI;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordRIAndDI;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordCard.PartsRdpRecordCardSearch;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordRIManager.RecordDIBean;
import com.yunda.jx.pjjx.partsrdp.wpinst.manager.PartsRdpNodeManager;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpRecordCard业务类,配件检修记录卡实例
 * <li>创建人：何涛
 * <li>创建日期：2014-12-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsRdpRecordCardManager")
public class PartsRdpRecordCardManager extends JXBaseManager<PartsRdpRecordCard, PartsRdpRecordCard>{
    
//  通过资源文件获取url
    private static String url;
    
    private static final String PARTSCHECKITEMDATA = "partsCheckItemData";
    
    static {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("webservice.properties");
        Properties p = new Properties();
        try {
            p.load(is);
            is.close();
        } catch (IOException e) {
            throw new BusinessException("获取../src/webservice.properties文件异常，请检查配置文件！");
        }
        url = p.getProperty(PARTSCHECKITEMDATA).trim();
        if (StringUtil.isNullOrBlank(url)) {
            throw new BusinessException("未读取到检测项数据配置项，请检查webservice.properties文件是否正确！");
        }
    }
    
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** PartsRdpRecordRI业务类,配件检修检测项实例 */
	@Resource
	private PartsRdpRecordRIManager partsRdpRecordRIManager;
	
	/** PartsRdpWorker业务类,作业人员 */
	@Resource
	private PartsRdpWorkerManager partsRdpWorkerManager;
	
	/** PartsRdpQCParticipant业务类,质量可检查人员 */
	@Resource
	private PartsRdpQCParticipantManager partsRdpQCParticipantManager;
	
	/** QCItem业务类 质量检查项 */
	@Resource
	private QCItemManager qCItemManager;
	
	/** QCItem业务类 质量检查项 */
	@Resource
	private QCEmpManager qCEmpManager;
	
	/** QCEmpOrg业务类 */
	@Resource
	private QCEmpOrgManager qCEmpOrgManager;
	
	/** PartsRdpNode业务类,配件检修作业节点 */
	@Resource
	private PartsRdpNodeManager partsRdpNodeManager;
	
	/** PartsRdpQR业务类,质量检查结果 */
	@Resource
	private PartsRdpQRManager partsRdpQRManager;
		
	/** PartsRdp业务类,配件检修作业 */
	@Resource
	private PartsRdpManager partsRdpManager;
	
	/** partsRdpRecordDI业务类,配件检修检测数据项 */
	@Resource
	private PartsRdpRecordDIManager partsRdpRecordDIManager;
    
  
	/**
	 * <li>说明：批量撤销领活
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 配件检修记录工单主键数组
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void giveUpBatchJob(String[] ids) throws BusinessException, NoSuchFieldException {
		for (String idx : ids) {
			this.giveUpJob(getModelById(idx));
		}
	}
	
	/**
	 * <li>说明：撤销领活
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 记录工单实体
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	private void giveUpJob(PartsRdpRecordCard entity) throws BusinessException, NoSuchFieldException {
		entity.setStatus(IPartsRdpStatus.CONST_STR_STATUS_DLQ);		// 状态
		entity.setRemarks(null);									// 检修情况描述
		entity.setHandleEmpID(null);								// 领活人
		entity.setHandleEmpName(null);								// 领活人名称
		entity.setWorkEmpID(null);									// 领活人
		entity.setWorkEmpName(null);								// 领活人名称
		entity.setWorkStartTime(null);								// 开工时间
		entity.setWorkEndTime(null);								// 开工时间
		this.saveOrUpdate(entity);
		
		// 删除已指派的质量检查人员信息
		this.partsRdpQCParticipantManager.logicDeleteByRdpRecordCardIDX(entity.getIdx());
		
		// 撤销检修检测项的处理历史记录
		List<PartsRdpRecordRI> riList = this.partsRdpRecordRIManager.getModels(entity.getIdx());
		if (null != riList && riList.size() > 0) {
			this.partsRdpRecordRIManager.giveUpJob(riList);
		}
	}

	/**
	 * <li>说明：处理排序字段，因为后台对排序字段默认是以java实体的属性名进行的封装，对于使用SQL查询要将其转换为对应的数据表字段名称
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 查询对象实体
	 * @param sb 用以接收排序信息的可变字符串对象
	 * @throws NoSuchFieldException
	 */
	@SuppressWarnings("unused")
    private void appendOrders(SearchEntity<PartsRdpRecordCard> searchEntity, StringBuilder sb) throws NoSuchFieldException {
		Order[] orders = searchEntity.getOrders();
		if (null != orders) {
			String sOrder = null;
			for (Order order : orders) {
				sOrder = order.toString();
				// 获取字段名称
				String fieldName = sOrder.substring(0, sOrder.indexOf(" "));
				// 获取排序类型
				String orderType = sOrder.substring(sOrder.indexOf(" ") + 1).toUpperCase();
				// 利用反射获取java对象的属性字段
				Field field = PartsRdpRecordCard.class.getDeclaredField(fieldName);
				field.setAccessible(true);
				Column column = field.getAnnotation(Column.class);
				if (null == column) {
					sb.append(" ORDER BY A.").append(sOrder.toUpperCase());
				} else {
					sb.append(" ORDER BY A.").append(column.name().toUpperCase()).append(" ").append(orderType);
				}
            }
		}
	}

	/**
	 * <li>说明：批量销活
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人：何涛
	 * <li>修改日期：2015-01-19
	 * <li>修改内容：批量销活时，不对记录工单的下属检修检测项等进行处理
     * 
	 * @param ids 进行消耗处理的实体的idx主键数组
	 * @param tempEntity 包含业务处理信息的临时实体对象
	 * @param qcEmps 通过指派方式指定的质量检查人员数组
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void finishBatchJob(String[] ids, PartsRdpRecordCard tempEntity, PartsRdpQCParticipant.QCEmp[] qcEmps) throws BusinessException, NoSuchFieldException {
		PartsRdpRecordCard entity;
		for (String idx : ids) {
			entity = this.getModelById(idx);
			
			//  处理【配件检修工序实例】
//			List<PartsRdpRecordRI> entityList = partsRdpRecordRIManager.getModels(entity.getIdx());
//			if (null != entityList && entityList.size() > 0) {
//				partsRdpRecordRIManager.finishBatchRI(entityList);
//			}
			this.finish(entity, tempEntity, qcEmps);
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
	 * @param tempEntity 包含业务处理信息的临时实体对象
     * @param qcEmps 通过指派方式指定的质量检查人员数组
     * @return PartsRdpRecordCard 配件检修记录卡实例
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public PartsRdpRecordCard completeJob(PartsRdpRecordCard tempEntity, PartsRdpQCParticipant.QCEmp[] qcEmps) throws BusinessException, NoSuchFieldException {
		PartsRdpRecordCard entity = this.getModelById(tempEntity.getIdx());
		// 批量处理无必填项的检修检测项
        batchUpdateRecordRI(entity.getIdx());
        daoUtils.flush();
		// 销活
		this.finish(entity, tempEntity, qcEmps);
		return entity;
	}
    
	/**
     * <li>说明：完成配件检修记录单【销活】
     * <li>创建人：程锐
     * <li>创建日期：2016-2-1
     * <li>修改人：何涛
     * <li>修改日期：2016-05-13
     * <li>修改内容：修改以前将是否暂存使用类静态变量isTemporary进行标示会导致异常的bug，将是否暂存isTemporary的标示变量重构到方法体上
     * @param tempEntity 包含业务处理信息的临时实体对象
     * @param qcEmps 通过指派方式指定的质量检查人员数组
     * @param riAndDis 配件检修检测项及数据项的封装对象数组
     * @return 配件检修记录单对象
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
	 */
	public PartsRdpRecordCard completeJob(PartsRdpRecordCard tempEntity, PartsRdpQCParticipant.QCEmp[] qcEmps, PartsRdpRecordRIAndDI[] riAndDis) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        // 批量保存配件检修检测项和数据项
        batchUpdateRIAndDI(riAndDis, false);
        // 销活
        PartsRdpRecordCard entity = this.getModelById(tempEntity.getIdx());
        this.finish(entity, tempEntity, qcEmps);
        return entity;
	}
	
    /**
     * <li>说明：修改已处理的配件检修记录单
     * <li>创建人：程锐
     * <li>创建日期：2016-2-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param tempEntity 包含业务处理信息的临时实体对象
     * @param riAndDis 配件检修检测项及数据项的封装对象数组
     * @return 配件检修记录单对象
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
	public PartsRdpRecordCard updateJob(PartsRdpRecordCard tempEntity, PartsRdpRecordRIAndDI[] riAndDis) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
		PartsRdpRecordCard entity = this.getModelById(tempEntity.getIdx());			      
		batchUpdateRIAndDI(riAndDis, false);
		updateJob(entity, tempEntity);	
		return entity;
	}
	
	/**
     * <li>说明：销活功能的统一接口，其他对外暴露的【销活】功能均必须调用该方法，销活时要检验工单下属工序状态是否都是“已处理”
     * <li>创建人：何涛
     * <li>创建日期：2014-12-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 被销活工单的实例对象
     * @param tempEntity 包含销活信息的封装的临时对象
     * @param qcEmps 通过指派方式指定的质量检查人员数组
     * @throws NoSuchFieldException
     * @throws BusinessException
     */
    private void finish(PartsRdpRecordCard entity, PartsRdpRecordCard tempEntity, PartsRdpQCParticipant.QCEmp[] qcEmps) throws BusinessException,
        NoSuchFieldException {
        // 检验“开工时间”是否填写,如果没有填写则提示
        if (null != tempEntity.getWorkStartTime()) {
            entity.setWorkStartTime(tempEntity.getWorkStartTime());
        }
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getRecordCardNo()).append(Constants.BRACKET_L).append(entity.getRecordCardDesc()).append(Constants.BRACKET_R);
        if (null == entity.getWorkStartTime()) {
            throw new NullPointerException(sb.append("开工时间未设置！").toString());
        }
        // 检验“完工时间”是否填写，如果没有填写则设置为系统当前时间
        if (null != tempEntity.getWorkEndTime()) {
            entity.setWorkEndTime(tempEntity.getWorkEndTime());
        }
        if (null == entity.getWorkEndTime()) {
            entity.setWorkEndTime(new Date());
        }        
        
        // 验证记录工单下属检修检测项状态是否均为“已处理”
        if (!this.validateFinish(entity)) {
            throw new BusinessException(sb.append("还有未被处理的检修检测项，不能进行销活！").toString());
        }
        
        entity.setWorkEmpID(this.partsRdpWorkerManager.formatWorkerID(tempEntity.getWorkEmpID())); // 其他作业人员ID
        entity.setWorkEmpName(this.partsRdpWorkerManager.formatWorkerName(tempEntity.getWorkEmpName())); // 其他作业人员名称
        if (null != tempEntity.getRemarks()) { // 检修情况描述
            entity.setRemarks(tempEntity.getRemarks());
        }
        entity.setIsBack(IPartsRdpStatus.CONST_INT_IS_BACK_NO); // 清除回退标识
        
        // 完工时质量检查参与人员存储
        this.updateQCParticipant(entity, qcEmps);
        // 如果没有质量检验项，则把记录状态设置为“修竣”，否则设置为“质量检验中”
        if (null != entity.getQcContent() && entity.getQcContent().trim().length() > 0) {
            entity.setStatus(IPartsRdpStatus.CONST_STR_STATUS_ZLJYZ);
        } else {
            entity.setStatus(IPartsRdpStatus.CONST_STR_STATUS_XJ);
        }    
        this.saveOrUpdate(entity);
        
        // 销活后，要反向更新该工单所挂属作业节点的状态为“已处理”
        try {
            partsRdpNodeManager.updateFinishedStatus(entity.getRdpNodeIDX());
        } catch (Exception e) {
            throw new BusinessException("更新工单信息出错！");
        }
    }
	
	/**
	 * <li>说明：完工时质量检查参与人员存储
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 记录工单实例
	 * @param qcEmps 通过指派方式制定的质量检查人员对象数组
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	private void updateQCParticipant(PartsRdpRecordCard entity, PartsRdpQCParticipant.QCEmp[] qcEmps) throws BusinessException, NoSuchFieldException {

		// 通过指派方式存储的质量可检查人员的更新
		this.partsRdpQCParticipantManager.saveOrUpdate(entity.getRdpIDX(), entity.getIdx(), qcEmps);
		
		// 获取记录工单的质量检查项
		String qcContent = entity.getQcContent();
		if (null == qcContent || qcContent.trim().length() <= 0) {
			return;
		}
		String[] qcItemNames = qcContent.split("\\|");
		QCItem qcItem = null;
		for (String qcItemName : qcItemNames) {
			// 获取单个已维护的质量检查项实体
			qcItem = this.qCItemManager.getModelByQCItemName(qcItemName);
			// 对“是否指派”为[是]的不作处理
			if (qcItem.getIsAssign() != QCItem.CONST_INT_IS_ASSIGN_Y) {
				// 获取质量检查项配置的质量检查人员
				List<QCEmp> qcEmpList = this.qCEmpManager.getModelsByQCItemIDX(qcItem.getIdx());
				if (null == qcEmpList || qcEmpList.size() <= 0) {
					continue;
				}
				List<QCEmpOrg> qcEmpOrgList = null;
				for (QCEmp qcEmp : qcEmpList) {
					qcEmpOrgList = this.qCEmpOrgManager.getModelsByQCEmpIDX(qcEmp.getIdx());
					// 如果该质量检查人员没有维护可检查的班组信息，则视为，该人员可检查所有的班组
					if (null == qcEmpOrgList || qcEmpOrgList.size() <= 0) {
						this.partsRdpQCParticipantManager.saveOrUpdate(entity, qcItem, qcEmp);
					} else {
						for (QCEmpOrg qcEmpOrg : qcEmpOrgList) {
							if (qcEmpOrg.getCheckOrgID().intValue() == SystemContext.getOmOrganization().getOrgid().intValue()) {
								this.partsRdpQCParticipantManager.saveOrUpdate(entity, qcItem, qcEmp);
							}
						}
					}
				}
			} 
		}
	}

	/**
	 * <li>说明：销活时要检验该工单下属所有工序的状态是否都是“已处理”
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 被销活工单的实例对象
	 * @return 该工单下属所有工序的状态是否都是“已处理”则返回true，否则返回false
	 */
	public boolean validateFinish(PartsRdpRecordCard entity) {
		// 检验下属工序是否都已被处理
		List<PartsRdpRecordRI> entityList = partsRdpRecordRIManager.getModels(entity.getIdx());
		String[] msg = partsRdpRecordRIManager.validateStatus(entityList, PartsRdpRecordRI.CONST_STR_STATUS_YCL);
		if (null == msg) {
			return true;
		}
		return false;
	}
	
	/**
	 * <li>说明：根据“作业主键”获取【配件检修记录工单实体】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpIDX 作业主键
	 * @return List<PartsRdpRecordCard> 配件检修记录工单实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<PartsRdpRecordCard> getModels(String rdpIDX) {
		String hql = "From PartsRdpRecordCard Where recordStatus = 0 And rdpIDX = ?";
		return this.daoUtils.find(hql,  new Object[]{rdpIDX});
	}
	
	/**
	 * <li>说明：根据“作业主键”和“作业节点主键”获取【配件检修记录卡实例】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-08
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpIDX 作业主键
	 * @param rdpNodeIDX 作业流程主键
     * @return List<PartsRdpRecordCard> 配件检修记录工单实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<PartsRdpRecordCard> getModels(String rdpIDX, String rdpNodeIDX) {
		String hql = "From PartsRdpRecordCard Where recordStatus = 0 And rdpIDX = ? And rdpNodeIDX = ?";
		return this.daoUtils.find(hql,  new Object[]{rdpIDX, rdpNodeIDX});
	}
    
	/**
	 * <li>说明：根据“作业主键”获取“作业节点id”为空的所有【配件检修记录卡实例】
	 * <li>创建人：程梅
	 * <li>创建日期：2015-1-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
     * 
     * @param rdpIDX 作业主键
     * @return List<PartsRdpRecordCard> 配件检修记录工单实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<PartsRdpRecordCard> getModelsForStart(String rdpIDX) {
		String hql = "From PartsRdpRecordCard Where recordStatus = 0 And rdpIDX = ? And rdpNodeIDX is null ";
		return this.daoUtils.find(hql,  new Object[]{rdpIDX});
	}
	
	/**
	 * <li>说明：批量领活
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
     * @param ids 配件检修记录工单主键数组
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */		
	public void startUpBatchJob(String[] ids) throws BusinessException, NoSuchFieldException {
		for (String idx : ids) {
			// 领活
			this.startUpJob(idx);
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
     * @param idx 配件检修记录工单主键
     * @return PartsRdpRecordCard 配件检修记录工单实体
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */		
	public PartsRdpRecordCard startUpJob(String idx) throws BusinessException, NoSuchFieldException {
		// 获取当前登录用户的信息
		OmEmployee omEmployee = SystemContext.getOmEmployee();
		PartsRdpRecordCard entity = this.getModelById(idx);
		entity.setHandleEmpID(omEmployee.getEmpid());				// 领活人
		entity.setHandleEmpName(omEmployee.getEmpname());			// 领活人名称
		entity.setStatus(IPartsRdpStatus.CONST_STR_STATUS_DCL);		// 状态
		entity.setWorkStartTime(Calendar.getInstance().getTime());	// 开工时间
		this.saveOrUpdate(entity);
		
		String rdpNodeIDX = entity.getRdpNodeIDX();
		if (StringUtil.isNullOrBlank(rdpNodeIDX)) {
			this.partsRdpManager.updateRealStartTime(entity.getRdpIDX());
		} else {
			// 领活成功后要反向更新该工单所挂属作业节点的实际开始时间（如果该字段还没有设置）
			this.partsRdpNodeManager.updateRealStartTime(entity.getRdpNodeIDX());
		}
		return entity;
	}
	
	/**
	 * <li>说明：暂存【暂存】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param tempEntity  包含业务处理信息的临时实体对象
	 * @param qcEmps 通过指派方式制定的质量检查人员对象数组
	 * @param riAndDis 通过指派方式制定的质量检查人员对象数组
     * @return PartsRdpRecordCard 配件检修记录工单实体
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public PartsRdpRecordCard saveTemporary(PartsRdpRecordCard tempEntity, PartsRdpQCParticipant.QCEmp[] qcEmps, PartsRdpRecordRIAndDI[] riAndDis) throws BusinessException, NoSuchFieldException {
        try {
            // 批量保存配件检修检测项和数据项
            if (null != riAndDis) {
                batchUpdateRIAndDI(riAndDis, true);
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        }
		PartsRdpRecordCard entity = this.getModelById(tempEntity.getIdx());
		
		entity.setWorkStartTime(tempEntity.getWorkStartTime());						// 开工时间
		entity.setWorkEndTime(tempEntity.getWorkEndTime());							// 完工时间
		entity.setWorkEmpID(this.partsRdpWorkerManager.formatWorkerID(tempEntity.getWorkEmpID()));		// 作业人员
		entity.setWorkEmpName(this.partsRdpWorkerManager.formatWorkerName(tempEntity.getWorkEmpName()));// 作业人员名称
		entity.setRemarks(tempEntity.getRemarks());									// 检修情况描述
		
		// 质量检测项“互检”的参与人员存储
		this.partsRdpQCParticipantManager.saveOrUpdate(entity.getRdpIDX(), entity.getIdx(), qcEmps);
		
		this.saveOrUpdate(entity);
		return entity;
	}
	
	/**
	 * <li>说明：功能操作前的记录状态验证
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entityList 配件检修记录工单实体集合
	 * @param status 记录的预期状态
	 * @return String[] 验证消息
	 */
	public String[] validateStatus(List<PartsRdpRecordCard> entityList, String status) {
		List<String> errMsgs = new ArrayList<String>(entityList.size());
		String validateMsg = null;
		for (PartsRdpRecordCard entity : entityList) {
			validateMsg = this.validateStatus(entity, status);
			if (null == validateMsg) {
				continue;
			}
			errMsgs.add(validateMsg);
		}
		if (errMsgs.size() > 0) {
			return errMsgs.toArray(new String[errMsgs.size()]);
		}
		return null;
	}
	
	/**
	 * <li>说明：功能操作前的记录状态验证，如果操作发生时，记录的状态不是预期的状态，则返回相应的状态的提示信息
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx 配件检修记录工单主键
	 * @param status 记录的预期状态
     * @return String 验证消息
	 */
	public String validateStatus(String idx, String status) {
		return this.validateStatus(getModelById(idx), status);
	}

	/**
	 * <li>说明：功能操作前的记录状态验证，如果操作发生时，记录的状态不是预期的状态，则返回相应的状态的提示信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 配件检修记录卡实例
	 * @param status 记录的预期状态
     * @return String 验证消息
	 */
	private String validateStatus(PartsRdpRecordCard entity, String status) {
	    // 验证工单的状态
	    String errMsg = PartsRdpRecordCardManager.checkEntityStatus(status, entity.getStatus());
        if (null == errMsg) {
            return null;
        }
	    StringBuilder sb = new StringBuilder();
	    sb.append(entity.getRecordCardNo()).append(Constants.BRACKET_L).append(entity.getRecordCardDesc()).append(Constants.BRACKET_R);
		return sb.append(errMsg).toString();
	}

    /**
     * <li>说明：验证工单的状态
     * <li>创建人：何涛
     * <li>创建日期：2015-2-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param status 预期的状态值
     * @param entityStatus 工单当前的状态值
     * @return String 验证消息
     */
    public static String checkEntityStatus(String status, String entityStatus) {
        if (status.equals(entityStatus)) {
            return null;
        }
        String errMsg = null;
        if (null == entityStatus || entityStatus.trim().length() <= 0) {
            errMsg = "数据异常，未知的记录工单状态！";
        }
//		if (entityStatus.equals(IPartsRdpStatus.CONST_STR_STATUS_WKF)) {
//            errMsg = "还未开放！";
//		}
//		if (entityStatus.equals(IPartsRdpStatus.CONST_STR_STATUS_DLQ)) {
//		    errMsg = "还未领活！";
//		}
		if (entityStatus.equals(IPartsRdpStatus.CONST_STR_STATUS_DCL)) {
		    errMsg = "已经正在处理中！";
		}
		if (entityStatus.equals(IPartsRdpStatus.CONST_STR_STATUS_XJ)) {
		    errMsg = "已经修竣！";
		}
		if (entityStatus.equals(IPartsRdpStatus.CONST_STR_STATUS_YZZ)) {
		    errMsg = "已经终止！";
		}
		if (entityStatus.equals(IPartsRdpStatus.CONST_STR_STATUS_ZLJYZ)) {
		    errMsg = "已经正在质量检验中！";
		}
        return errMsg;
    }
	
	/**
	 * <li>说明：功能操作前的记录状态验证
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 配件检修记录卡实例 idx主键数组
	 * @param status 记录的预期状态
     * @return String[] 验证消息
	 */
	public String[] validateStatus(String[] ids, String status) {
		List<String> errMsgs = new ArrayList<String>(ids.length);
		PartsRdpRecordCard entity = null;
		String validateMsg = null;
		for (String idx : ids) {
			entity = this.getModelById(idx);
			validateMsg = this.validateStatus(entity, status);
			if (null == validateMsg) {
				continue;
			}
			errMsgs.add(validateMsg);
		}
		if (errMsgs.size() > 0) {
			return errMsgs.toArray(new String[errMsgs.size()]);
		}
		return null;
	}
	
	/**
	 * <li>说明：检验所有记录是否都已处理完成
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entityList 配件检修记录工单实体集合
	 * @param msgList 验证消息集合
	 * @return
	 * @throws BusinessException
	 */
	public void checkFinishStatus(List<PartsRdpRecordCard> entityList, List<String> msgList) throws BusinessException {
		for (PartsRdpRecordCard entity : entityList) {
		    // 检验“检修记录工单”记录工单下属“检修检测项”是否都已处理
		    List<PartsRdpRecordRI> list = partsRdpRecordRIManager.getModels(entity.getIdx());
		    this.partsRdpRecordRIManager.checkFinishStatus(list, msgList);
            if (msgList.size() > 0) {
                return;
            }
			// 检验“检修记录工单”是否修竣
			if (!entity.getStatus().equals(IPartsRdpStatus.CONST_STR_STATUS_XJ)) {
				// 如果不是“修竣”，检验该作业下属质量检查项（必检）都已处理，不考虑抽检
				List<PartsRdpQR> qrList = this.partsRdpQRManager.getModel(entity.getIdx());
				if (null != qrList && qrList.size() > 0) {
					this.partsRdpQRManager.checkFinishStatus(qrList, msgList);
                    if (msgList.size() > 0) {
                        return;
                    }
				}
			}
		}
	}
	
	/**
	 * <li>说明：开放工单，设置状态为“待领取”
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entityList 配件检修记录工单实体集合
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void startUp(List<PartsRdpRecordCard> entityList) throws BusinessException, NoSuchFieldException {
		String[] errMsg = this.validateStatus(entityList, IPartsRdpStatus.CONST_STR_STATUS_WKF);
		if (null != errMsg) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < errMsg.length; i++) {
				sb.append(i).append(". ").append(errMsg[i]).append("<br>");
			}
			throw new BusinessException(sb.toString());
		}
		for (PartsRdpRecordCard entity : entityList) {
			entity.setStatus(IPartsRdpStatus.CONST_STR_STATUS_DCL);
			this.saveOrUpdate(entity);
		}
	}
	
	/**
	 * <li>说明：返修
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-26
	 * <li>修改人：何涛
	 * <li>修改日期：2015-01-24
	 * <li>修改内容：1记录工单返修后，不对下属的检修检测线进行重置，根据最新设计，检修检测项处理完成后，应可再次修改
	 * <ul>
	 * <li>记录工单返修后，不对下属的检修检测线进行重置，根据最新设计，检修检测项处理完成后，应可再次修改
	 * <li>返修后，如果该工单挂接在作业节点上，应更新作业节点状态
	 * </ul>
	 * 
	 * @param idx 工单主键
	 * @throws Exception 
	 */
	public void updateToBack(String idx) throws Exception {
		PartsRdpRecordCard entity = this.getModelById(idx);
		// 如果已经修竣，则不允许回退
		if (IPartsRdpStatus.CONST_STR_STATUS_XJ.equals(entity.getStatus())) {
			throw new BusinessException("工单[" + entity.getRecordCardDesc() + "]已经修竣，不能执行回退！");
		}
		entity.setStatus(IPartsRdpStatus.CONST_STR_STATUS_DCL);		// 更新工单姿态为“待处理”
		entity.setIsBack(IPartsRdpStatus.CONST_INT_IS_BACK_YES);	// 更新回退标识
		Integer backCount = entity.getBackCount();					// 更新回退次数
		if (null == backCount) {
			entity.setBackCount(1);
		} else {
			entity.setBackCount(backCount + 1);
		}
       
		this.saveOrUpdate(entity);
		
//		// 返修后，开放所有检修检测项，即设置检修检测项的状态为“未处理”
//		List<PartsRdpRecordRI> riList = this.partsRdpRecordRIManager.getModels(entity.getIdx());
//		if (null != riList && riList.size() > 0) {
//			this.partsRdpRecordRIManager.updateToBack(riList);
//		}
//		
//		// 返修后，删除所有质量检查参与人员
//		this.partsRdpQCParticipantManager.logicDeleteByRdpRecordCardIDX(idx);
		
		// 返修后，如果该工单挂接在作业节点上，应更新作业节点状态
		this.partsRdpNodeManager.updateToBack(entity.getRdpNodeIDX());
		
	}
	
	/**
	 * <li>说明：查询指定系统登录用户的待检验工单
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-07
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 查询实体
	 * @param checkWay 检验方式（抽检\必检），抽检：1，必检：2
	 * @param empId 人员ID
	 * @return Page<PartsRdpRecordCard> 配件检修记录工单实体集合
	 */
	public Page<PartsRdpRecordCard> findPageListForQC(SearchEntity<PartsRdpRecordCard> searchEntity, String checkWay, Long empId) {
		StringBuilder sb = new StringBuilder("From PartsRdpRecordCard Where recordStatus = 0");
        // 作业主键
        sb.append(" And rdpIDX = '").append(searchEntity.getEntity().getRdpIDX()).append("' ");
		
		// 只查询状态为“质量检验中”的工单
		sb.append("And status = '").append(IPartsRdpStatus.CONST_STR_STATUS_ZLJYZ).append("'");
		
		// 查询指定人员所需检查的工单
		sb.append(" And idx in (").append("Select rdpRecordCardIDX From PartsRdpQCParticipant Where recordStatus = 0 And qCEmpID = ").append(empId).append(")");
		
		// 查询指定的质量检查项
		sb.append(" And idx in (").append("Select rdpRecordCardIDX From PartsRdpQR Where recordStatus = 0 And checkWay = ").append(checkWay).append(" And status = '").append(PartsRdpQR.CONST_STR_STATUS_DCL).append("')");
		
		// 处理排序字段
		Order[] orders = searchEntity.getOrders();
		if (null != orders && orders.length > 0) {
			sb.append(" Order By ").append(orders[0]);
			for (int i = 1; i < orders.length; i++) {
				sb.append(", ").append(orders[i]);
			}
		}
		
		String hql = sb.toString();
		String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
		
		return this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}
	
	/**
	 * <li>说明：查询当前系统登录用户的待检验工单
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 查询实体
	 * @param checkWay 检验方式（抽检\必检），抽检：1，必检：2
     * @return Page<PartsRdpRecordCard> 配件检修记录工单实体集合
	 */
	public Page<PartsRdpRecordCard> findPageListForQC(SearchEntity<PartsRdpRecordCard> searchEntity, String checkWay) {
		OmEmployee omEmployee = SystemContext.getOmEmployee();
		return this.findPageListForQC(searchEntity, checkWay, omEmployee.getEmpid());
	}

	/**
	 * <li>说明：更新作业工单状态
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entityList 作业工单实体集合
	 * @param status 要更新的作业工单状态
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public void updateStatus(List<PartsRdpRecordCard> entityList, String status) throws BusinessException, NoSuchFieldException {
		List<PartsRdpRecordCard> list = new ArrayList<PartsRdpRecordCard>();
		for (PartsRdpRecordCard entity : entityList) {
			// 如果被更新对象的状态已经是指定状态，则不作处理
			if (status.equals(entity.getStatus())) {
				continue;
			}
			entity.setStatus(status);
			list.add(entity);
		}
		this.saveOrUpdate(list);
	}
    
    /**
     * <li>说明：统计指定配件检修作业下检修记录的处理情况
     * <li>创建人：何涛
     * <li>创建日期：2015-10-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 配件检修作业主键
     * @param status 配件检修记录状态，未处理："wcl"；质检中："zjz"；已处理："ycl"
     * @param rdpNodeIDX 配件检修节点IDX
     * @return 指定状态的检修记录数
     */
    public int getCount(String rdpIDX, String status, String rdpNodeIDX) {
        StringBuilder sb = new StringBuilder("From PartsRdpRecordCard Where recordStatus = 0");
        if (!StringUtil.isNullOrBlank(rdpIDX)) {
            sb.append(" And rdpIDX = '").append(rdpIDX).append("'");
        }
        if (!StringUtil.isNullOrBlank(rdpNodeIDX)) {
            sb.append(" And rdpNodeIDX = '").append(rdpNodeIDX).append("'");
        }
        if (IPartsRdpStatus.STATUS_WCL.equals(status)) {
            sb.append(" And status In ('")
                .append(IPartsRdpStatus.CONST_STR_STATUS_WKF).append("','")
                .append(IPartsRdpStatus.CONST_STR_STATUS_DLQ).append("','")
                .append(IPartsRdpStatus.CONST_STR_STATUS_DCL).append("')");
        }
        if (IPartsRdpStatus.STATUS_ZJZ.equals(status)) {
            sb.append(" And status = '").append(IPartsRdpStatus.CONST_STR_STATUS_ZLJYZ).append("'");
        }
        if (IPartsRdpStatus.STATUS_YCL.equals(status)) {
            sb.append(" And status = '").append(IPartsRdpStatus.CONST_STR_STATUS_XJ).append("'");
        }
        return this.daoUtils.getCount(sb.toString());
    }
    
    /**
     * <li>说明：修改已处理的检修记录单
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param tempEntity 包含业务处理信息的临时实体对象
     * @return 修改后的检修记录单实体
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public PartsRdpRecordCard updateJob(PartsRdpRecordCard tempEntity) throws BusinessException, NoSuchFieldException {
        PartsRdpRecordCard entity = this.getModelById(tempEntity.getIdx());
        this.updateJob(entity, tempEntity);
        return entity;
    }
    
    /**
     * <li>说明：修改已处理的检修记录单
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 已处理的检修记录单实体
     * @param tempEntity 包含业务处理信息的临时实体对象
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private void updateJob(PartsRdpRecordCard entity, PartsRdpRecordCard tempEntity) throws BusinessException, NoSuchFieldException {
        if (null != tempEntity.getWorkStartTime()) {
            entity.setWorkStartTime(tempEntity.getWorkStartTime());
        } else
            entity.setWorkStartTime(new Date());
        // 检验“完工时间”是否填写，如果没有填写则设置为系统当前时间
        if (null != tempEntity.getWorkEndTime()) {
            entity.setWorkEndTime(tempEntity.getWorkEndTime());
        }else {
            entity.setWorkEndTime(new Date());
        }
        entity.setWorkEmpID(this.partsRdpWorkerManager.formatWorkerID(tempEntity.getWorkEmpID())); // 其他作业人员ID
        entity.setWorkEmpName(this.partsRdpWorkerManager.formatWorkerName(tempEntity.getWorkEmpName())); // 其他作业人员名称
        if (null != tempEntity.getRemarks()) { // 检修情况描述
            entity.setRemarks(tempEntity.getRemarks());
        }
        this.saveOrUpdate(entity);
    }
    
    /**
     * <li>说明：批量处理无必填项的检修检测项
     * <li>创建人：程锐
     * <li>创建日期：2016-2-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param recordCardIDX 配件检修记录单IDX
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private void batchUpdateRecordRI(String recordCardIDX) throws BusinessException, NoSuchFieldException {
    	List<PartsRdpRecordRI> riList = partsRdpRecordRIManager.getModels(recordCardIDX);
        List<PartsRdpRecordRI> riSaveList = new ArrayList<PartsRdpRecordRI>();
        for (PartsRdpRecordRI recordRI : riList) {
			if (StringUtil.isNullOrBlank(recordRI.getRepairResult()))
				continue;
			int isNotBlankCount = partsRdpRecordDIManager.getIsNotBlankCountByRdpRecordRIIDX(recordRI.getIdx());
			if (isNotBlankCount != 0)
				continue;
			recordRI.setStatus(PartsRdpRecordRI.CONST_STR_STATUS_YCL);
			riSaveList.add(recordRI);
		}
        partsRdpRecordRIManager.saveOrUpdate(riSaveList);
    }
    
    /**
     * <li>说明：批量保存配件检修检测项和数据项
     * <li>创建人：程锐
     * <li>创建日期：2016-2-1
     * <li>修改人：何涛
     * <li>修改日期：2016-03-09
     * <li>修改内容：修改方法声明，增加是否暂存的参数（isTempSave），如果是暂存，则不更新检修检测项的状态
     * @param riAndDis 配件检修检测项及数据项的封装对象数组 
     * @param isTempSave 如果是暂存，则不更新检修检测项的状态
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private void batchUpdateRIAndDI(PartsRdpRecordRIAndDI[] riAndDis, boolean isTempSave) throws IllegalAccessException, InvocationTargetException, BusinessException, NoSuchFieldException {
    	for (PartsRdpRecordRIAndDI riAndDi : riAndDis) {
			PartsRdpRecordRI partsRdpRecordRI = new PartsRdpRecordRI();
			BeanUtils.copyProperties(partsRdpRecordRI, riAndDi);
			PartsRdpRecordDI[] partsRdpRecordDIs = null;
			List<RecordDIBean> diList = riAndDi.getDiList();
			if (diList != null && diList.size() > 0) {
				partsRdpRecordDIs = new PartsRdpRecordDI[diList.size()];
				for (int i = 0; i < diList.size(); i++) {
					PartsRdpRecordDI di = new PartsRdpRecordDI();
					di.setIdx(diList.get(i).getIdx());
					di.setDataItemResult(diList.get(i).getDataItemResult());
					partsRdpRecordDIs[i] = di;
				}
			}
			partsRdpRecordRIManager.saveTemporary(partsRdpRecordRI, partsRdpRecordDIs, isTempSave);
		}		
    }
    
    /**
     * <li>说明：分页联合查询，查询出相应记录工单下未处理的检修检测项记录数[riCounts]
     * <li>创建人：何涛
     * <li>创建日期：2016-3-9
     * <li>修改人：何涛
     * <li>修改日期：2016-05-16
     * <li>修改内容：增加对质量检查信息的查询
     * @param whereList 查询条件集合
     * @param start 查询开始索引
     * @param limit 查询结束索引
     * @return 分页查询结果集
     */
    public Page<PartsRdpRecordCardSearch> queryPageList(List<Condition> whereList, Integer start, Integer limit) {
        StringBuilder sb = new StringBuilder("SELECT T.*, (select count(*) from pjjx_parts_rdp_record_ri ri where ri.rdp_record_card_idx=t.idx and ri.record_status=0 and ri.status='01') AS \"RICOUNTS\" FROM PJJX_PARTS_RDP_RECORD_CARD T WHERE T.RECORD_STATUS=0");
        for (Condition con : whereList) {
            if (Condition.EQ == con.getCompare()) {
                sb.append(" AND ").append(con.getPropName()).append(" = ").append("'").append(con.getPropValue()).append("'");
            }
            if (Condition.IN == con.getCompare()) {
                Object[] propValues = con.getPropValues();
                if (propValues.length <= 0) {
                    continue;
                }
                StringBuilder temp = new StringBuilder();
                for (Object obj : propValues) {
                    temp.append("'").append(obj).append("',");
                }
                sb.append(" AND ").append(con.getPropName()).append(" IN (").append(temp.substring(0, temp.length() - 1)).append(")");
            }
        }
        sb.append(" ORDER BY T.SEQ_NO");
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
        Page<PartsRdpRecordCardSearch> page = super.queryPageList(totalSql, sb.toString(), start, limit, false, PartsRdpRecordCardSearch.class);
        List<PartsRdpRecordCardSearch> list = page.getList();
        // 设置检修记录下属的质量检查信息
        for (PartsRdpRecordCardSearch card : list) {
            card.setQrList(this.partsRdpQRManager.getQREmpInfo(card.getIdx()));
        }
        return page;
    }

    /**
     * <li>说明：通过检修记录单id查询记录卡
     * <li>创建人：张迪
     * <li>创建日期：2016-6-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpRecordIDX 记录单id
     * @return 记录卡集合
     */
    @SuppressWarnings("unchecked")
    public List<PartsRdpRecordCard> getModelsByRecordIDX(String rdpRecordIDX) {
        String hql = "From PartsRdpRecordCard Where recordStatus = 0 And rdpRecordIDX = ? Order By seqNo";
        return this.daoUtils.find(hql, new Object[]{rdpRecordIDX});
    }
    
    /**
     * <li>说明：通过检修记录单id查询记录卡，检测项，质量检查
     * <li>创建人：张迪
     * <li>创建日期：2016-6-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpRecordIDX 记录单id
     * @return 记录卡集合
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    public String integrateQueryCardList(String rdpRecordIDX) throws JsonGenerationException, JsonMappingException, IOException {
        List<PartsRdpRecordCard> partsRdpRecordCardList = getModelsByRecordIDX(rdpRecordIDX);
        OmEmployee emp = SystemContext.getOmEmployee();
        if(null!=partsRdpRecordCardList && !partsRdpRecordCardList.isEmpty()){
            for(PartsRdpRecordCard partsRdpRecordCard: partsRdpRecordCardList){
                //查询检测项结果集
                List<PartsRdpRecordRI> partsRdpRecordRiList = partsRdpRecordRIManager.getModels(partsRdpRecordCard.getIdx());
                if(null!=partsRdpRecordRiList && !partsRdpRecordRiList.isEmpty()){
                    partsRdpRecordCard.setPartsRdpRecordRiList(partsRdpRecordRiList);
                    for(PartsRdpRecordRI partsRdpRecordRI: partsRdpRecordRiList ){
                        // 查询数据项    
                        List<PartsRdpRecordDI> diList = partsRdpRecordDIManager.getModelByRdpRecordRIIDX(partsRdpRecordRI.getIdx()); 
                        if(null!=diList && !diList.isEmpty()){
                           partsRdpRecordRI.setPartsRdpRecordDiList(diList);
                        }               
                    }
                }  
                //查询质量检查集合
                List<PartsRdpQR> partsRdpQRList = partsRdpQRManager.getModel(partsRdpRecordCard.getIdx());
                for (PartsRdpQR rdpQR : partsRdpQRList) {
                    if(rdpQR.getStatus().equals(PartsRdpQR.CONST_STR_STATUS_DCL)){
                        // 判断当前用户是否能够做对应检验项 
                        PartsRdpQCParticipant participant = partsRdpQCParticipantManager.getModelByRdpRecordCardIDX(partsRdpRecordCard.getIdx(), rdpQR.getQCItemNo(), emp.getEmpid());
                        if(participant != null){
                            rdpQR.setIsCanSubmit("true");
                        }
                    }
                }
                partsRdpRecordCard.setPartsRdpQRList(partsRdpQRList);
            }  
            return JSONTools.toJSONList(partsRdpRecordCardList); 
        }
        return null;
    }
    
    /**
     * <li>说明：配件质量检查-根据检修记录单查询检修记录卡
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpRecordIDX 检修记录单实例ID
     * @param qCItemNo 检测项编码 （互检、质检）
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public String integrateQueryCardZljcList(String rdpRecordIDX,String qCItemNo) throws JsonGenerationException, JsonMappingException, IOException {
        List<PartsRdpRecordCard> partsRdpRecordCardList = getModelsByRecordIDX(rdpRecordIDX);
        if(null!=partsRdpRecordCardList && !partsRdpRecordCardList.isEmpty()){
            for(PartsRdpRecordCard partsRdpRecordCard: partsRdpRecordCardList){
                //查询检测项结果集
                List<PartsRdpRecordRI> partsRdpRecordRiList = partsRdpRecordRIManager.getModels(partsRdpRecordCard.getIdx());
                if(null!=partsRdpRecordRiList && !partsRdpRecordRiList.isEmpty()){
                    partsRdpRecordCard.setPartsRdpRecordRiList(partsRdpRecordRiList);
                    for(PartsRdpRecordRI partsRdpRecordRI: partsRdpRecordRiList ){
                        // 查询数据项    
                        List<PartsRdpRecordDI> diList = partsRdpRecordDIManager.getModelByRdpRecordRIIDX(partsRdpRecordRI.getIdx()); 
                        if(null!=diList && !diList.isEmpty()){
                           partsRdpRecordRI.setPartsRdpRecordDiList(diList);
                        }               
                    }
                }  
                //查询质量检查集合
                List<PartsRdpQR> partsRdpQRList = partsRdpQRManager.getModel(partsRdpRecordCard.getIdx(),qCItemNo,PartsRdpQR.CONST_STR_STATUS_DCL);
                partsRdpRecordCard.setPartsRdpQRList(partsRdpQRList);
            }  
            return JSONTools.toJSONList(partsRdpRecordCardList); 
        }
        return null;
    }
    
   
    /**
     * <li>说明：根据“作业主键”和“作业节点主键”获取下级节点的【配件检修记录卡实例】
        * <li>创建人：张迪
     * <li>创建日期：2017-5-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 作业主键
     * @param rdpNodeIDX 作业流程主键
     * @return List<PartsRdpRecordCard> 配件检修记录工单实体集合
     */
    @SuppressWarnings("unchecked")
    public List<PartsRdpRecordCard> getModelByNodeIDX(String rdpIDX, String rdpNodeIDX) {
        String sql = "select * From PJJX_Parts_Rdp_Record_Card Where record_Status = 0 And Rdp_IDX = '" + rdpIDX + "' And Rdp_Node_IDX IN("
                    + "SELECT IDX  FROM PJJX_Parts_Rdp_Node N WHERE RECORD_STATUS = 0 START WITH IDX = '" + rdpNodeIDX
                    +  "' CONNECT BY  PRIOR IDX = (SELECT IDX FROM PJJX_Parts_Rdp_Node WHERE WP_NODE_IDX = N.PARENT_WP_NODE_IDX AND  N.RDP_IDX = RDP_IDX )  ) " ;
        return daoUtils.executeSqlQueryEntity(sql, PartsRdpRecordCard.class);
    }
    /**
     * <li>说明：通过检修节点idX查询记录卡，检测项，质量检查
     * <li>创建人：张迪
     * <li>创建日期：2017-5-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpRecordIDX 记录单id
     * @return 记录卡集合
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    public String findCardListByNodeIDX(String rdpIDX, String rdpNodeIDX) throws JsonGenerationException, JsonMappingException, IOException {
        List<PartsRdpRecordCard> partsRdpRecordCardList = getModelByNodeIDX(rdpIDX, rdpNodeIDX);
        OmEmployee emp = SystemContext.getOmEmployee();
        if(null!=partsRdpRecordCardList && !partsRdpRecordCardList.isEmpty()){
            for(PartsRdpRecordCard partsRdpRecordCard: partsRdpRecordCardList){
                //查询检测项结果集
                List<PartsRdpRecordRI> partsRdpRecordRiList = partsRdpRecordRIManager.getModels(partsRdpRecordCard.getIdx());
                if(null!=partsRdpRecordRiList && !partsRdpRecordRiList.isEmpty()){
                    partsRdpRecordCard.setPartsRdpRecordRiList(partsRdpRecordRiList);
                    for(PartsRdpRecordRI partsRdpRecordRI: partsRdpRecordRiList ){
                        // 查询数据项    
                        List<PartsRdpRecordDI> diList = partsRdpRecordDIManager.getModelByRdpRecordRIIDX(partsRdpRecordRI.getIdx()); 
                        if(null!=diList && !diList.isEmpty()){
                           partsRdpRecordRI.setPartsRdpRecordDiList(diList);
                        }               
                    }
                }  
                //查询质量检查集合
                List<PartsRdpQR> partsRdpQRList = partsRdpQRManager.getModel(partsRdpRecordCard.getIdx());
                for (PartsRdpQR rdpQR : partsRdpQRList) {
                    if(rdpQR.getStatus().equals(PartsRdpQR.CONST_STR_STATUS_DCL)){
                        // 判断当前用户是否能够做对应检验项 
                        PartsRdpQCParticipant participant = partsRdpQCParticipantManager.getModelByRdpRecordCardIDX(partsRdpRecordCard.getIdx(), rdpQR.getQCItemNo(), emp.getEmpid());
                        if(participant != null){
                            rdpQR.setIsCanSubmit("true");
                        }
                    }
                }
                partsRdpRecordCard.setPartsRdpQRList(partsRdpQRList);
            }  
            return JSONTools.toJSONList(partsRdpRecordCardList); 
        }
        return null;
    }
    /**
     * <li>说明：获取配件检修记录单列表后对接可视化数据
     * <li>创建人：林欢   
     * <li>创建日期：2016-06-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param query 封装的分页查询条件
     * @return void
     * @throws Exception 
     * @throws Exception
     */ 
    public Page<PartsRdpRecordCard> pageQuery(QueryCriteria<PartsRdpRecordCard> query) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Page<PartsRdpRecordCard> page = this.findPageList(query);
        //通过配建检修计划id查询配件编号，配件计划实际开始时间，配件计划实际结算时间
        String rdpIDX = (String) query.getWhereList().get(0).getPropValue();
        PartsRdp partsRdp = partsRdpManager.getModelById(rdpIDX);
        
        String partID = partsRdp.getPartsNo();
        String startTime = DateUtil.yyyy_MM_dd_HH_mm_ss_SSS.format(partsRdp.getRealStartTime());
        String endTime;
        
        if (partsRdp.getRealEndTime() == null) {
            endTime = DateUtil.yyyy_MM_dd_HH_mm_ss_SSS.format(new Date());
        }else {
            endTime = DateUtil.yyyy_MM_dd_HH_mm_ss_SSS.format(partsRdp.getRealEndTime());
        }
        
        //partsRdpRecordDIManager.doSyn(partID, startTime, endTime, rdpIDX, map, url);
        
        return page;
    }
}
