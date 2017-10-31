package com.yunda.sb.repair.process.manager;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.baseapp.upload.manager.AttachmentManager;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.sb.repair.process.entity.RepairScopeCase;
import com.yunda.sb.repair.process.entity.RepairWorkOrder;
import com.yunda.sb.repair.process.entity.RepairWorkOrderBean;
import com.yunda.sb.repair.process.entity.RepairWorkOrderStuff;
import com.yunda.sb.repair.scope.entity.RepairScopeDetails;
import com.yunda.sb.repair.scope.manager.RepairScopeDetailsManager;


/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairWorkOrder管理器，数据表：E_REPAIR_WORK_ORDER
 * <li>创建人：何涛
 * <li>创建日期：2016年7月7日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Service
public class RepairWorkOrderManager extends JXBaseManager<RepairWorkOrder, RepairWorkOrder> {
	
	/** RepairScopeDetails管理器，数据表：E_REPAIR_SCOPE_DETAILS */
	@Resource
	private RepairScopeDetailsManager repairScopeDetailsManager;
	
	/** RepairScopeCase管理器，数据表：E_REPAIR_SCOPE_CASE */
	@Resource
	private RepairScopeCaseManager repairScopeCaseManager;

	/** RepairWorkOrderStuff管理器，数据表：E_REPAIR_WORK_ORDER_STUFF */
	@Resource
	private RepairWorkOrderStuffManager repairWorkOrderStuffManager;
	
	/** 附件管理 */
	@Resource
	private AttachmentManager attachmentManager;
	
	/**
	 * <li>说明：设备检修作业工单联合分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2017年4月24日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询封装实体
	 * @return 设备巡检记录分页集合
	 */
	public Page<RepairWorkOrderBean> queryPageList(SearchEntity<RepairWorkOrderBean> searchEntity) {
		String sql = SqlMapUtil.getSql(String.format("repair%cprocess%crepair_work_order:queryPageList", File.separatorChar, File.separatorChar));
		StringBuilder sb = new StringBuilder(sql);
		RepairWorkOrderBean entity = searchEntity.getEntity();

		// 查询条件 - 巡检设备idx主键
		sb.append(" AND T.SCOPE_CASE_IDX = '").append(entity.getScopeCaseIdx()).append("'");
		// 排序
		this.processOrdersInSql(searchEntity, sb);
		String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
		return this.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, RepairWorkOrderBean.class);
	}
	
    /**
     * <li>说明：完成设备检修作业工单
     * <li>创建人：何涛
     * <li>创建日期：2016年6月29日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * @param idx 设备检修作业工单idx主键
     * @param otherWorkerName 其他作业人员名称 
     * @param repairRecord 实修记录
     * @param filePathArray 附件（照片）在服务器磁盘上的存放路径数组
     * @throws NoSuchFieldException
     */
    public void updateFinish(String idx, String otherWorkerName, String repairRecord, String[] filePathArray) throws NoSuchFieldException {
    	RepairWorkOrder entity = this.getModelById(idx);
    	entity.setRepairRecord(repairRecord);							// 实修记录
    	entity.setOtherWorkerName(otherWorkerName);
    	 OmEmployee user = SystemContext.getOmEmployee();					// 其它作业人员
    	if (null != user) {
    		entity.setWorkerId(user.getEmpid());						// 处理人id
    		entity.setWorkerName(user.getEmpname());					// 处理人名称
    	}
    	
    	entity.setOrderStatus(RepairWorkOrder.ORDER_STATUS_YCL);		// 工单状态，已处理（代码：3）
    	entity.setProcessTime(Calendar.getInstance().getTime());		// 处理时间
    	this.saveOrUpdate(entity);
    	
    	// 在作业范围下所有工单均已处理完成后，自动更新作业范围状态为已处理
    	List<RepairWorkOrder> entityList = this.getModelsByScopeCaseIdx(entity.getScopeCaseIdx(), RepairWorkOrder.ORDER_STATUS_WCL);
    	if (null == entityList || entityList.isEmpty()) {
    		this.repairScopeCaseManager.updateFinish(entity.getScopeCaseIdx());
    	}
    	
    	if (null == filePathArray || 0 >= filePathArray.length) {
			return;
		}
		// 保存附件信息
		this.attachmentManager.insert(idx, "e_repair_work_order", filePathArray);
    }
    
    /**
     * <li>说明：设备检修作业工单处理 - web
     * <li>创建人：何涛
     * <li>创建日期：2016年12月9日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * @param idx 设备检修作业工单idx主键
     * @param otherWorkerName 其他作业人员名称 
     * @param repairRecord 实修记录
     * @throws NoSuchFieldException
     */
    public RepairWorkOrder updateFinished(String idx, String otherWorkerName, String repairRecord) throws NoSuchFieldException {
    	RepairWorkOrder entity = this.getModelById(idx);
    	// Modified by hetao on 2017-02-20 增加设备检修任务撤销功能后，在处理工单时增加数据异常验证
    	if (Constants.DELETED == entity.getRecordStatus().intValue()) {
    		throw new BusinessException("数据异常，或该检修任务已被撤销，请刷新后重试！");
    	}
    	entity.setRepairRecord(repairRecord);							// 实修记录
    	entity.setOtherWorkerName(otherWorkerName);
    	// 如果是未处理过的设备检修作业工单，则更新作业工单状态为：已处理，该标识用于是否更新范围活状态
    	boolean isStatusChange = false;
    	if (!StringUtil.isNullOrBlank(repairRecord) 		// 必须设置施修记录
    			&& (null == entity.getOrderStatus() || RepairWorkOrder.ORDER_STATUS_YCL != entity.getOrderStatus().intValue())) {
    		isStatusChange = true;
    		entity.setOrderStatus(RepairWorkOrder.ORDER_STATUS_YCL);		// 工单状态，已处理（代码：3）
        	entity.setProcessTime(Calendar.getInstance().getTime());		// 处理时间
        	// 取当前系统登录人员为检修工单处理人员
        	entity.setWorkerId(SystemContext.getOmEmployee().getEmpid());		
        	entity.setWorkerName(SystemContext.getOmEmployee().getEmpname());
    	}
    	this.saveOrUpdate(entity);
    	if (isStatusChange) {
    		// 在作业范围下所有工单均已处理完成后，自动更新作业范围状态为已处理
        	List<RepairWorkOrder> entityList = this.getModelsByScopeCaseIdx(entity.getScopeCaseIdx(), RepairWorkOrder.ORDER_STATUS_WCL);
        	if (null == entityList || entityList.isEmpty()) {
        		this.repairScopeCaseManager.updateFinish(entity.getScopeCaseIdx());
        	}
    	}
    	return entity;
    }
    
    /**
	 * <li>说明：根据范围实例主键获取设备检修作业工单实体集合
	 * <li>创建人：何涛
	 * <li>创建日期：2017年2月20日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param scopeCaseIdx 范围实例主键
	 * @return 设备检修作业工单实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<RepairWorkOrder> getModelsByScopeCaseIdx(String scopeCaseIdx) {
		String hql = "From RepairWorkOrder Where recordStatus = 0 And scopeCaseIdx = ?";
		return this.daoUtils.find(hql, scopeCaseIdx);
	}

	/**
     * <li>说明：根据范围实例主键获取设备检修作业工单实体集合
     * <li>创建人：何涛
     * <li>创建日期：2016年7月8日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * @param scopeCaseIdx 范围实例主键
     * @param orderStatus 工单状态，1：未处理，3：已处理
     * @return 设备检修作业工单实体集合
     */
    @SuppressWarnings("unchecked")
	public List<RepairWorkOrder> getModelsByScopeCaseIdx(String scopeCaseIdx, Integer orderStatus) {
    	String hql = "From RepairWorkOrder Where recordStatus = 0 And scopeCaseIdx = ? And orderStatus = ?";
    	return this.daoUtils.find(hql, scopeCaseIdx, orderStatus);
    }
    
	/**
	 * <li>说明：生成设备检修作业工单（来源设备检修范围维护的作业内容项）
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param entity 设备检修范围实例
	 * @throws NoSuchFieldException 
	 */
	public void insert4Publish(RepairScopeCase repairScopeCase) throws NoSuchFieldException {
		List<RepairScopeDetails> rsDetails = this.repairScopeDetailsManager.getModelsByScopeIdx(repairScopeCase.getScopeDefineIdx());
		// 根据检修范围作业内容生成设备检修作业工单
		RepairWorkOrder entity = null;
		if (null == rsDetails || rsDetails.isEmpty()) {
			entity = new RepairWorkOrder();
			entity.scopeCaseIdx(repairScopeCase.getIdx())				// 范围实例主键
				.seqNo(1)												// 序号
				.workContent(repairScopeCase.getRepairItemName());		// 作业内容
			entity.setOrderStatus(RepairWorkOrder.ORDER_STATUS_WCL);
			this.saveOrUpdate(entity);
		}else{
			List<RepairWorkOrder> entityList = new ArrayList<RepairWorkOrder>(rsDetails.size());
			for (RepairScopeDetails details : rsDetails) {
				if(null == details) {}else{
					entity = new RepairWorkOrder();
					entity.defineIdx(details.getIdx()) 					// 作业项定义主键
					.scopeCaseIdx(repairScopeCase.getIdx())				// 范围实例主键
					.seqNo(details.getSeqNo())							// 序号
					.workContent(details.getWorkContent())				// 作业内容
					.processStandard(details.getProcessStandard());		// 范工艺标准
					entity.setOrderStatus(RepairWorkOrder.ORDER_STATUS_WCL);
					entityList.add(entity);
				}
			}
			this.saveOrUpdate(entityList);
		}
	}

	/**
	 * <li>说明：获取设备检修作业工单历史实修记录
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月9日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param defineIdx 设备检修作业范围作业项定义主键
	 * @return 设备检修作业工单历史实修记录
	 */
	@SuppressWarnings("unchecked")
	public List<Object> findHistoryRepairRecords(String defineIdx) {
		String sql = "SELECT DISTINCT REPAIR_RECORD FROM E_REPAIR_WORK_ORDER WHERE RECORD_STATUS = 0 AND REPAIR_RECORD IS NOT NULL AND DEFINE_IDX = "
			+ defineIdx +" AND ORDER_STATUS = "+ RepairWorkOrder.ORDER_STATUS_YCL +"";
		return this.daoUtils.executeSqlQuery(sql);
	}
	
	/**
     * <li>说明：批量完成设备检修作业工单
     * <li>创建人：黄杨
     * <li>创建日期：2016年8月13日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * @param workOrders 批量的作业工单
     * @param repairRecord 实修记录
     * @throws NoSuchFieldException
     */
	public void updateFinishBatch(Map<String, List<RepairWorkOrder>> workOrders, String repairRecord) throws NoSuchFieldException {
		Set<Entry<String, List<RepairWorkOrder>>> entries = workOrders.entrySet();
		if(null == entries){
			return;
		}
		Entry<String, List<RepairWorkOrder>> entry = null;
		List<RepairWorkOrder> repairWorkOrders = null;
		Iterator<Entry<String, List<RepairWorkOrder>>> iterator = entries.iterator();
		if (StringUtil.isNullOrBlank(repairRecord)) {
			repairRecord = RepairWorkOrder.DEFAULT_REPAIR_RECORD;
		}
		while(iterator.hasNext()){
			entry = iterator.next();
			repairWorkOrders = entry.getValue();
			// 只选择作业范围时，将该作业范围下所有作业工单查询出来
			if (null == repairWorkOrders || repairWorkOrders.isEmpty()) {
				repairWorkOrders = this.getModelsByScopeCaseIdx(entry.getKey(), RepairWorkOrder.ORDER_STATUS_WCL);
			}
			
			if (null == repairWorkOrders || repairWorkOrders.isEmpty()) {
				continue;
			}
			// 处理工单，如果该作业范围下所有工单都处理完毕，将该作业范围状态改为已处理
			for (RepairWorkOrder rwo : repairWorkOrders) {
				this.updateFinish(rwo.getIdx(), null, repairRecord, null);
			}
		}
	}
	
	 /**
     * <li>说明：级联删除检修用料
     * <li>创建人：何涛
     * <li>创建日期：2017年2月20日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * @param ids 设备检修作业工单idx主键数组
     */
	@Override
	public void deleteByIds(Serializable... ids) {
		Serializable[] array = null;
		List<RepairWorkOrderStuff> list = null;
		for (Serializable idx : ids) {
			list = this.repairWorkOrderStuffManager.getModelsByRepairWorkOrderIdx((String) idx);
			if (null == list || list.isEmpty()) {
				continue;
			}
			array = new String[list.size()];
			for (int i = 0; i < array.length; i++) {
				array[i] = list.get(i).getIdx();
			}
			this.repairWorkOrderStuffManager.deleteByIds(array);
		}
		super.deleteByIds(ids);
	}
	
}
