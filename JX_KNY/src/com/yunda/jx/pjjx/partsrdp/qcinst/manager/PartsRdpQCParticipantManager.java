package com.yunda.jx.pjjx.partsrdp.qcinst.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.pjjx.base.qcitemdefine.entity.QCItem;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQCParticipant;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQR;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQCParticipant.QCEmp;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordCard;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpQCParticipant业务类,质量可检查人员
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsRdpQCParticipantManager")
public class PartsRdpQCParticipantManager extends JXBaseManager<PartsRdpQCParticipant, PartsRdpQCParticipant>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** PartsRdpQR业务类,质量检查结果 */
	@Resource
	private PartsRdpQRManager partsRdpQRManager;
	
	/**
	 * <li>说明：非指派方式的质量可检测人员查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-06-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpRecordCardIDX 记录卡实例主键
	 * @param qCItemNo 检查项编码
	 * @param qCEmpID 检验人员
	 * @return PartsRdpQCParticipant
	 */
	@SuppressWarnings("unchecked")
	public PartsRdpQCParticipant getModelByRdpRecordCardIDX(String rdpRecordCardIDX, String qCItemNo, Long qCEmpID) {
		String hql = "From PartsRdpQCParticipant Where recordStatus = 0 And rdpRecordCardIDX = ? And qCItemNo = ? And qCEmpID = ?";
		return (PartsRdpQCParticipant) this.daoUtils.findSingle(hql, new Object[]{rdpRecordCardIDX, qCItemNo, qCEmpID});
	}
    
    /**
     * <li>说明：指派方式的质量可检测人员查询
     * <li>创建人：何涛
     * <li>创建日期：2014-12-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param rdpRecordCardIDX 记录卡实例主键
     * @param qCItemNo 检查项编码
     * @return PartsRdpQCParticipant 质量可检查人员实体
     */
    @SuppressWarnings("unchecked")
    public PartsRdpQCParticipant getModelByRdpRecordCardIDX(String rdpRecordCardIDX, String qCItemNo) {
        String hql = "From PartsRdpQCParticipant Where recordStatus = 0 And rdpRecordCardIDX = ? And qCItemNo = ?";
        return (PartsRdpQCParticipant) this.daoUtils.findSingle(hql, new Object[]{rdpRecordCardIDX, qCItemNo});
    }
	
	/**
	 * <li>说明：根据“记录卡实例主键”查询【质量可检查人员】
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpRecordCardIDX 记录卡实例主键
	 * @return List<PartsRdpQCParticipant> 质量可检查人员实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<PartsRdpQCParticipant> getModelByRdpRecordCardIDX(String rdpRecordCardIDX) {
		String hql = "Select new PartsRdpQCParticipant(a.idx, a.rdpIDX, a.rdpRecordCardIDX, a.qCItemNo, a.qCEmpID, a.qCEmpName, b.qCItemName) From PartsRdpQCParticipant a, QCItem b Where a.qCItemNo = b.qCItemNo And a.recordStatus = 0 And b.recordStatus = 0 And a.rdpRecordCardIDX = ?";
		return this.daoUtils.find(hql, new Object[]{rdpRecordCardIDX});
	}

	/**
	 * <li>说明：通过指派方式存储的质量可检查人员的更新
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpIDX 作业主键
	 * @param rdpRecordCardIDX 记录工单主键
	 * @param qcEmps 需要指派的质量检查项 的质量检查人员对象 数组 
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void saveOrUpdate(String rdpIDX, String rdpRecordCardIDX, QCEmp[] qcEmps) throws BusinessException, NoSuchFieldException {
		PartsRdpQCParticipant entity = null;
		List<PartsRdpQCParticipant> entityList = new ArrayList<PartsRdpQCParticipant>();
		for (QCEmp qcEmp : qcEmps) {
			
			// 验证记录工单是否有该质量检查项
			PartsRdpQR qr = partsRdpQRManager.getModel(rdpRecordCardIDX, qcEmp.getQcItemNo());
			if (null == qr) {
				continue;
			}
			
            entity = this.getModelByRdpRecordCardIDX(rdpRecordCardIDX, qcEmp.getQcItemNo());
			if (null == entity) {
				if (null != qcEmp.getEmpId()) {
					entity = new PartsRdpQCParticipant();
					entity.setQCEmpID(qcEmp.getEmpId());						// 检验人员
					entity.setQCEmpName(qcEmp.getEmpName());					// 检验人员名称
					entity.setRdpIDX(rdpIDX);									// 作业主键
					entity.setRdpRecordCardIDX(rdpRecordCardIDX);				// 记录卡实例主键
					entity.setQCItemNo(qcEmp.getQcItemNo());					// 质量检验项编码
					entityList.add(entity);
				}
			} else {
				entity.setQCEmpID(qcEmp.getEmpId());							// 检验人员
				entity.setQCEmpName(qcEmp.getEmpName());						// 检验人员名称
				entityList.add(entity);
			}
		}
		this.saveOrUpdate(entityList);
	}
	
	/**
	 * <li>说明：根据“记录卡实例主键”删除
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpRecordCardIDX 记录卡实例主键
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void logicDeleteByRdpRecordCardIDX(String rdpRecordCardIDX) throws BusinessException, NoSuchFieldException {
		List<PartsRdpQCParticipant> entityList = this.getModelByRdpRecordCardIDX(rdpRecordCardIDX);
		if (null != entityList && entityList.size() > 0) {
			this.logicDelete(entityList);
		}
	}
	
	/**
	 * <li>说明：通过非指派方式存储的质量可检查人员的更新
	 * <li>创建人：何涛
	 * <li>创建日期：2015-02-01
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 记录工单实体
	 * @param qcItem 质量检查项实体
	 * @param qcEmp 质量检查人员实体
	 * @throws NoSuchFieldException
	 */
	public void saveOrUpdate(PartsRdpRecordCard entity, QCItem qcItem, com.yunda.jx.pjjx.base.qcitemdefine.entity.QCEmp qcEmp) throws NoSuchFieldException {
        PartsRdpQCParticipant qcParticipant = this.getModelByRdpRecordCardIDX(entity.getIdx(), qcItem.getQCItemNo(), qcEmp.getCheckEmpID());
        if (null != qcParticipant) {
            return;
        }
		qcParticipant = new PartsRdpQCParticipant();
		qcParticipant.setQCEmpID(qcEmp.getCheckEmpID());			// 检验人员
		qcParticipant.setQCEmpName(qcEmp.getCheckEmpName());		// 检验人员名称
		qcParticipant.setQCItemNo(qcItem.getQCItemNo());			// 检查项编码
		qcParticipant.setRdpIDX(entity.getRdpIDX());				// 作业主键
		qcParticipant.setRdpRecordCardIDX(entity.getIdx());			// 记录卡实例主键
		this.saveOrUpdate(qcParticipant);
	}

	/**
	 * <li>说明：获取记录工单指派的质量检查人员
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-20
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param rdpRecordCardIDX 记录卡实例主键
	 * @return List
	 */
	public List<QCEmp> getQCEmpsForAssign(String rdpRecordCardIDX) {
		List<PartsRdpQCParticipant> entityList = this.getModelByRdpRecordCardIDX(rdpRecordCardIDX);
		List<QCEmp> list = new ArrayList<QCEmp>();
		QCEmp qcEmp = null;
        String status ;
		for (PartsRdpQCParticipant entity : entityList) {
			PartsRdpQR qr = this.partsRdpQRManager.getModel(rdpRecordCardIDX, entity.getQCItemNo());
//			if (qr.getIsAssign() == QCItem.CONST_INT_IS_ASSIGN_N) {
//				continue;
//			}
			qcEmp = new QCEmp();
            status = "未处理";
			qcEmp.setEmpId(entity.getQCEmpID());
			qcEmp.setEmpName(entity.getQCEmpName());
			qcEmp.setQcItemNo(entity.getQCItemNo());
			qcEmp.setQcItemName(qr.getQCItemName());
            if(null != qr.getQREmpID() && qr.getQREmpID() == entity.getQCEmpID()) status = "已处理";
            qcEmp.setStatus(status) ;
			list.add(qcEmp);
		}
		return list;
	}
	
}