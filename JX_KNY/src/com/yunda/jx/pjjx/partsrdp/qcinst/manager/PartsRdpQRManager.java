package com.yunda.jx.pjjx.partsrdp.qcinst.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.jxgc.common.JxgcConstants;
import com.yunda.jx.pjjx.base.qcitemdefine.entity.QCItem;
import com.yunda.jx.pjjx.partsrdp.IPartsRdpStatus;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpManager;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsQRBean;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQCParticipant;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQR;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQRBackRepairLog;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordCard;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordCardManager;
import com.yunda.jx.pjjx.partsrdp.wpinst.manager.PartsRdpNodeManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpQR业务类,质量检查结果
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsRdpQRManager")
public class PartsRdpQRManager extends JXBaseManager<PartsRdpQR, PartsRdpQR>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** PartsRdpRecordCard业务类,配件检修记录卡实例 */
	@Resource
	private PartsRdpRecordCardManager partsRdpRecordCardManager;
    
    @Resource
    private PartsRdpQRQueryManager partsRdpQRQueryManager;
    
    /** PartsRdpQCParticipant业务类,质量可检查人员 */
    @Resource
    private PartsRdpQCParticipantManager partsRdpQCParticipantManager;
    
    @Resource
    private PartsRdpManager partsRdpManager;
    
    @Resource
    private PartsRdpNodeManager partsRdpNodeManager;
    /** partsRdpRecordDI业务类,返修日志 */
    @Resource
    private   PartsRdpQRBackRepairLogManager partsRdpQRBackRepairLogManager;
	/**
	 * <li>说明：根据“记录卡实例主键”和“质量检查项编码”查询质量检查结果实体
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpRecordCardIDX 记录卡实例主键
	 * @param qCItemNo 质量检查项编码
	 * @return PartsRdpQR 质量检查结果实体
	 */
	public PartsRdpQR getModel(String rdpRecordCardIDX, String qCItemNo) {
		String hql = "From PartsRdpQR Where recordStatus = 0 And rdpRecordCardIDX = ? And qCItemNo = ?";
		return (PartsRdpQR) this.daoUtils.findSingle(hql, new Object[]{rdpRecordCardIDX, qCItemNo});
	}
    
    
    /**
     * <li>说明：根据“记录卡实例主键”和“质量检查项编码”和“状态”查询质量检查结果实体
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpRecordCardIDX
     * @param qCItemNo
     * @param status
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<PartsRdpQR> getModel(String rdpRecordCardIDX, String qCItemNo,String status) {
        String hql = "From PartsRdpQR Where recordStatus = 0 And rdpRecordCardIDX = ? And qCItemNo = ? And status = ?";
        return this.daoUtils.find(hql, new Object[]{rdpRecordCardIDX , qCItemNo , status});
    }
    
	
	/**
	 * <li>说明：根据“记录卡实例主键”查询实体
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpRecordCardIDX 记录卡实例主键
	 * @return List<PartsRdpQR> 质量检查结果实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<PartsRdpQR> getModel(String rdpRecordCardIDX) {
		String hql = "From PartsRdpQR Where recordStatus = 0 And rdpRecordCardIDX = ?";
		return this.daoUtils.find(hql, new Object[]{rdpRecordCardIDX});
	}
	
	/**
	 * <li>说明：检验指定的配件检修记录卡实例的所有质量检查是否都已处理
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpRecordCardIDX 配件检修记录工单主键
	 * @return true:所有质量检查项都已处理 否则返回false
	 */
	public boolean isFinishedAll(String rdpRecordCardIDX) {
		List<PartsRdpQR> entiList = this.getModel(rdpRecordCardIDX);
		for (PartsRdpQR entity : entiList) {
			if (QCItem.CONST_INT_CHECK_WAY_BJ == entity.getCheckWay() && PartsRdpQR.CONST_STR_STATUS_DCL.equals(entity.getStatus())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * <li>说明：签名提交
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpRecordCardIDXs 记录卡实例主键数组 
	 * @param qcItemNo 质量检查项编码
	 * @param qrResult 质量检查项结果
	 * @throws Exception 
	 */
	public void signAndSubmit(String[] rdpRecordCardIDXs, String qcItemNo, String qrResult) throws Exception {
		List<String> list = new ArrayList<String>();
		for (String idx : rdpRecordCardIDXs) {
			if (!list.contains(idx)) {
				list.add(idx);
			}
		}
		this.finish(list, qcItemNo, qrResult);
	}
	
	/**
	 * <li>说明：签名提交
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpRecordCardIDXs 记录卡实例主键数组 
	 * @param qcItemNo 质量检查项编码
	 * @param qrResult 质量检查项结果
	 * @throws Exception 
	 */
	private void finish(List<String> rdpRecordCardIDXs, String qcItemNo, String qrResult) throws Exception {
		for (String rdpRecordCardIDX : rdpRecordCardIDXs) {
			PartsRdpQR entity = this.getModel(rdpRecordCardIDX, qcItemNo);
			if (null == entity) {
				throw new NullPointerException("数据异常-未查询到【质量检查结果】-rdpRecordCardIDX[" + rdpRecordCardIDX + "]qCItemNo[" + qcItemNo + "]");
			}
			
			if (PartsRdpQR.CONST_STR_STATUS_YCL.equals(entity.getStatus())) {
				continue;
			}
			OmEmployee omEmployee = SystemContext.getOmEmployee();	// 设置质量人员信息
			entity.setQREmpID(omEmployee.getEmpid());			
			entity.setQREmpName(omEmployee.getEmpname());
			
			entity.setQRResult(qrResult); 							// 质量检查结果
			entity.setStatus(PartsRdpQR.CONST_STR_STATUS_YCL);		// 设置姿态为“已处理”
			entity.setQRTime(Calendar.getInstance().getTime());		// 质量检查时间
			
			this.saveOrUpdate(entity);
			// 检验该记录卡实例的所有质量检查项是否都已处理，如果都已处理，则返回去更新记录卡实例的状态为“修竣”
			if (this.isFinishedAll(rdpRecordCardIDX)) {
				PartsRdpRecordCard rc = this.partsRdpRecordCardManager.getModelById(rdpRecordCardIDX);
				// 更新状态为“修竣”
				rc.setStatus(IPartsRdpStatus.CONST_STR_STATUS_XJ);
				this.partsRdpRecordCardManager.saveOrUpdate(rc);
				String validateMsg = partsRdpNodeManager.validateFinishedStatus(rc.getRdpNodeIDX());
                if (validateMsg == null)
                	partsRdpNodeManager.updateFinishedStatus(rc.getRdpNodeIDX());
			}
		}
	}
    
    /**
     * <li>说明：签名并提交
     * <li>创建人：程锐
     * <li>创建日期：2015-10-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idxs 质检主键数组
     * @param qrResult 质检结果
     * @throws Exception 
     */
    public void signAndSubmitByIDX(String[] idxs, String qrResult) throws Exception {
        List<String> list = new ArrayList<String>();
        for (String idx : idxs) {
            if (!list.contains(idx)) {
                list.add(idx);
            }
        }
        this.finishByIDX(list, qrResult);
    }
    
    /**
     * <li>说明：全部签名提交
     * <li>创建人：程锐
     * <li>创建日期：2015-10-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param checkWay 质检类型
     * @param searchEntity 质检实体包装类
     * @param qrResult 质检结果
     * @throws Exception
     */
    public void allSignAndSubmit(String checkWay, SearchEntity<PartsQRBean> searchEntity, String qrResult) throws Exception {
        OmEmployee omEmployee = SystemContext.getOmEmployee();
        
        String querySql = partsRdpQRQueryManager.getQuerySql(omEmployee.getEmpid(), checkWay, searchEntity, "getQCList");
        List list = daoUtils.executeSqlQuery(querySql);
        querySql = partsRdpQRQueryManager.getQuerySql(omEmployee.getEmpid(), checkWay, searchEntity, "getAllQCList");
        String sql = SqlMapUtil.getSql("pjjx-qc:updateAllFinishQCResult")
                               .replace("#STATUS_YCL#", PartsRdpQR.CONST_STR_STATUS_YCL)
                               .replace("#empid#", omEmployee.getEmpid() + "")
                               .replace("#empname#", omEmployee.getEmpname())
                               .replace("#qrResult#", StringUtil.isNullOrBlank(qrResult) ? "" : qrResult)
                               .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                               .replace("#STATUS_DCL#", PartsRdpQR.CONST_STR_STATUS_DCL )
                               .replace("#checkWay#", checkWay + "")
                               .replace("#idx#", "idx in (" + querySql + ")")
                               .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss));
        daoUtils.executeSql(sql);
        Set<String> rdpRecordCardIDXSet = new HashSet<String>();
        
        for (int i = 0; i < list.size(); i++) {
            Object[] objs = (Object[]) list.get(i);
            rdpRecordCardIDXSet.add(objs[5].toString());            
        }
        for (String rdpRecordCardIDX : rdpRecordCardIDXSet) {
            if (this.isFinishedAll(rdpRecordCardIDX)) {
                PartsRdpRecordCard rc = this.partsRdpRecordCardManager.getModelById(rdpRecordCardIDX);
                // 更新状态为“修竣”
                rc.setStatus(IPartsRdpStatus.CONST_STR_STATUS_XJ);
                this.partsRdpRecordCardManager.saveOrUpdate(rc);
                String validateMsg = partsRdpNodeManager.validateFinishedStatus(rc.getRdpNodeIDX());
                if (validateMsg == null)
                	partsRdpNodeManager.updateFinishedStatus(rc.getRdpNodeIDX());
            }
        }
    }
    
    /**
     * <li>说明：签名提交
     * <li>创建人：程锐
     * <li>创建日期：2015-10-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idxs 质检主键列表
     * @param qrResult 质检结果
     * @throws Exception 
     */
    private void finishByIDX(List<String> idxs, String qrResult) throws Exception {
        for (String idx : idxs) {
            PartsRdpQR entity = getModelById(idx);
            if (null == entity) {
                throw new NullPointerException("数据异常-未查询到【质量检查结果】");
            }
            
            if (PartsRdpQR.CONST_STR_STATUS_YCL.equals(entity.getStatus())) {
                continue;
            }
            OmEmployee omEmployee = SystemContext.getOmEmployee();  // 设置质量人员信息
            entity.setQREmpID(omEmployee.getEmpid());           
            entity.setQREmpName(omEmployee.getEmpname());
            
            entity.setQRResult(qrResult);                           // 质量检查结果
            entity.setStatus(PartsRdpQR.CONST_STR_STATUS_YCL);      // 设置姿态为“已处理”
            entity.setQRTime(Calendar.getInstance().getTime());     // 质量检查时间
            
            this.saveOrUpdate(entity);
            // 检验该记录卡实例的所有质量检查项是否都已处理，如果都已处理，则返回去更新记录卡实例的状态为“修竣”
            if (this.isFinishedAll(entity.getRdpRecordCardIDX())) {
                PartsRdpRecordCard rc = this.partsRdpRecordCardManager.getModelById(entity.getRdpRecordCardIDX());
                // 更新状态为“修竣”
                rc.setStatus(IPartsRdpStatus.CONST_STR_STATUS_XJ);
                this.partsRdpRecordCardManager.saveOrUpdate(rc);
                //如果节点下所有工单都已处理则完成节点
                String validateMsg = partsRdpNodeManager.validateFinishedStatus(rc.getRdpNodeIDX());
                if (validateMsg == null)
                	partsRdpNodeManager.updateFinishedStatus(rc.getRdpNodeIDX());
            }
        }
    }
	
	/**
	 * <li>说明：返修
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpRecordCardIDXs  记录卡实例主键数组 
	 * @param qcItemNo 质量检查项编码(暂时未使用该参数)
	 * @throws Exception 
	 */
	public void updateToBack(String[] rdpRecordCardIDXs, String qcItemNo) throws Exception {
		// 增加一个临时列表，用于记录已返修的记录卡主键，防止多次返修
		List<String> list = new ArrayList<String>();
        PartsRdpRecordCard entity = null;
        if (rdpRecordCardIDXs == null || rdpRecordCardIDXs.length < 1)
            return;
        entity = partsRdpRecordCardManager.getModelById(rdpRecordCardIDXs[0]);
		for (String rdpRecordCardIDX : rdpRecordCardIDXs) {
			if (!list.contains(rdpRecordCardIDX)) {
				list.add(rdpRecordCardIDX);                
				this.partsRdpRecordCardManager.updateToBack(rdpRecordCardIDX);
                PartsRdpQRBackRepairLog partsRdpQRBackRepairLog = new PartsRdpQRBackRepairLog();
                partsRdpQRBackRepairLog.setRdpRecordCardIDX(entity.getIdx());
                partsRdpQRBackRepairLog.setQCItemNo(qcItemNo);
                partsRdpQRBackRepairLog.setRdpIDX(entity.getRdpIDX());
                partsRdpQRBackRepairLogManager.saveOrUpdate(partsRdpQRBackRepairLog);  // 保存返修记录日志       
			}
		}
        if (entity != null && !StringUtil.isNullOrBlank(entity.getRdpIDX()))
            partsRdpManager.updateStatus(PartsRdp.STATUS_JXZ, new String[]{entity.getRdpIDX()});
	}

	/**
	 * <li>说明：检验所有必检项都已处理完成
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param qrList 质量检查结果实体集合
	 * @param msgList 验证消息集合
	 */
	public void checkFinishStatus(List<PartsRdpQR> qrList, List<String> msgList) {
		for (PartsRdpQR entity : qrList) {
			if (QCItem.CONST_INT_CHECK_WAY_BJ == entity.getCheckWay() && PartsRdpQR.CONST_STR_STATUS_DCL.equals(entity.getStatus())) {
				msgList.add("该配件检修作业任务下还有必检项【" + entity.getQCItemName() + "】未完成");
				return;
			}
		}
	}
	
    /**
     * <li>说明：终止同一配件检修作业任务的未处理的质检项
     * <li>创建人：程锐
     * <li>创建日期：2015-11-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 配件检修作业任务单IDX
     * @throws Exception
     */
    public void updateTerminateQCResult(String rdpIDX) throws Exception {
        String sql = SqlMapUtil.getSql("pjjx-qc:updateTerminateQCResult")
                               .replace("#STATUS_YZZ#", PartsRdpQR.CONST_STR_STATUS_YZZ)
                               .replace("#STATUS_DCL#", PartsRdpQR.CONST_STR_STATUS_DCL)                           
                               .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                               .replace("#rdpIDX#", rdpIDX);
        daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：根据记录单IDX获取质检人信息
     * <li>创建人：程锐
     * <li>创建日期：2015-11-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpRecordCardIDX 配件检修记录单IDX
     * @return 配件检修包装类列表
     * @throws Exception
     */
    public List<PartsQRBean> getQREmpInfoByRecordCardIDX(String rdpRecordCardIDX) throws Exception {
        List<PartsQRBean> list = new ArrayList<PartsQRBean>();
        List<PartsRdpQR> qrList = getModel(rdpRecordCardIDX);
        List<PartsRdpQCParticipant> participantList = partsRdpQCParticipantManager.getModelByRdpRecordCardIDX(rdpRecordCardIDX);
        for (PartsRdpQR rdpQR : qrList) {
            PartsQRBean qrBean = new PartsQRBean();
            qrBean.setQcItemName(rdpQR.getQCItemName());
            StringBuilder empNames = new StringBuilder();
            for (PartsRdpQCParticipant participant : participantList) {
                if (participant.getQCItemNo().equals(rdpQR.getQCItemNo())) {
                    empNames.append(participant.getQCEmpName()).append(Constants.JOINSTR);
                }
            }
            if (empNames.length() > 1)
                empNames.deleteCharAt(empNames.length() - 1);
            qrBean.setQcEmpNames(empNames.toString());
            qrBean.setStatus(rdpQR.getStatus());
            list.add(qrBean);
        }
        return list;
    }
    
    /**
     * <li>说明：根据配件检修记录工单主键获取质量检查信息；
     * 对于质量检查人员字段的特殊处理，如果质量检查还未处理，则查询可以执行质量检查的人员信息（如果有多个人，则以逗号分隔）
     * <li>创建人：何涛
     * <li>创建日期：2016-5-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpRecordCardIDX 记录卡实例主键
     * @return 质量检查信息集合 
     */
    @SuppressWarnings("unchecked")
    public List<PartsRdpQR> getQREmpInfo(String rdpRecordCardIDX) {
        String sql = "SELECT IDX, REL_IDX, RDP_RECORD_CARD_IDX, QC_ITEM_IDX, QC_ITEM_NO, QC_ITEM_NAME, CHECK_WAY, IS_ASSIGN, SEQ_NO, QR_EMPID, QR_RESULT, QR_TIME, STATUS, RECORD_STATUS, SITEID, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME, NVL(QR_EMPNAME, QC_EMPNAME) AS \"QR_EMPNAME\" FROM (SELECT T.*, (SELECT TO_CHAR(WM_CONCAT(P.QC_EMPNAME)) FROM PJJX_PARTS_RDP_QC_PARTICIPANT P WHERE P.RDP_RECORD_CARD_IDX = T.RDP_RECORD_CARD_IDX AND P.QC_ITEM_NO = T.QC_ITEM_NO AND P.RECORD_STATUS = 0) AS \"QC_EMPNAME\" FROM PJJX_PARTS_RDP_QR T WHERE T.RECORD_STATUS = 0 AND RDP_RECORD_CARD_IDX = '" + rdpRecordCardIDX +"')";
        return this.daoUtils.executeSqlQueryEntity(sql, PartsRdpQR.class);
    }
}