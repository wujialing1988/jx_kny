package com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.jx.jxgc.base.jcqcitemdefine.entity.JCQCItemDefine;
import com.yunda.jx.jxgc.base.jcqcitemdefine.entity.JCQCItemEmpDefine;
import com.yunda.jx.jxgc.base.jcqcitemdefine.entity.JCQCItemEmpOrgDefine;
import com.yunda.jx.jxgc.base.jcqcitemdefine.manager.JCQCItemDefineManager;
import com.yunda.jx.jxgc.base.jcqcitemdefine.manager.JCQCItemEmpDefineManager;
import com.yunda.jx.jxgc.base.jcqcitemdefine.manager.JCQCItemEmpOrgDefineManager;
import com.yunda.jx.jxgc.common.JxgcConstants;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCParticipant;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResult;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResultVO;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车检修质量检验参与者管理类
 * <li>创建人：汪东良
 * <li>创建日期：2014-11-22
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "qCParticipantManager")
public class QCParticipantManager extends JXBaseManager<QCParticipant, QCParticipant> {
    
    /** 员工服务类 */
    @Resource
    private OmEmployeeManager omEmployeeManager;
    
    /** 质量检验结果管理类 */
    @Resource
    private QCResultManager qCResultManager;
    
    /** 机车检修质量检查基础配置业务类 */
    @Resource
    private JCQCItemDefineManager jCQCItemDefineManager;
    
    /** 机车检修质量检查人员基础配置业务类 */
    @Resource
    private JCQCItemEmpDefineManager jCQCItemEmpDefineManager;
    
    /** 机车检修质量检查人员可检查班组基础配置业务类 */
    @Resource
    private JCQCItemEmpOrgDefineManager jCQCItemEmpOrgDefineManager;
    
    /**
     * <li>说明：保存指派的质量检查参与者
     * <li>创建人：何涛
     * <li>创建日期：2016-4-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCard 作业工单实体
     * @param result 质量检验结果对象数组，形如： [{
     *      checkItemCode: "mutualCheckPerson",
     *      qcEmpID: "106"
     * }]
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void saveIsAssignParticiant(WorkCard workCard, QCResultVO[] result) throws BusinessException, NoSuchFieldException {
        if (null == result || 0 >= result.length) {
            return;
        }
        List<QCParticipant> entityList = new ArrayList<QCParticipant>(result.length);
        QCParticipant entity = null;
        for (QCResultVO qc : result) {
            // 验证该作业工单是否有正在保存的质量检查项
            QCResult qcResult = this.qCResultManager.getModel(workCard.getIdx(), qc.getCheckItemCode());
            if (null == qcResult) {
                continue;
            }
            // 首先查询是否已经保存了该工单指定质量检查项的质量检查人员信息，用于更新操作
            entity = this.getModel(workCard.getIdx(), qc.getCheckItemCode());
            if (null == entity) {
                entity = new QCParticipant();
            }
            entity.setRdpIDX(workCard.getRdpIDX());                     // 兑现单主键
            entity.setWorkCardIDX(workCard.getIdx());                   // 作业工单主键
            entity.setCheckItemCode(qc.getCheckItemCode());             // 质量检查项编码
            OmEmployee emp = omEmployeeManager.getModelById(qc.getQcEmpID());
            if (null != emp) {
                entity.setCheckPersonID(String.valueOf(emp.getEmpid()));    // 质量检查参与者id
                entity.setCheckPersonName(emp.getEmpname());                // 质量检查参与者名称
            }
            entityList.add(entity);
        }
        this.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：保存无需指派的质量检查参与者
     * <li>创建人：何涛
     * <li>创建日期：2016-4-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCard 作业工单实体
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveNotAssignParticiant(WorkCard workCard) throws BusinessException, NoSuchFieldException {
        // 查询作业工单下不需要指派的质量检查项
        List<QCResult> qcResults = qCResultManager.getModels(workCard.getIdx(), JCQCItemDefine.CONST_INT_IS_ASSIGN_N);
        if (null == qcResults || qcResults.isEmpty()) {
            return;
        }
        for (QCResult qcResult : qcResults) {
            // 根据质量检查项编码查询机车检修质量检查项基础配置信息
            JCQCItemDefine qcItem = this.jCQCItemDefineManager.getModelByQCItemNo(qcResult.getCheckItemCode());
            if (null == qcItem) {
                continue;
            }
            // 查询该质量检查项下可以检查的人员信息
            List<JCQCItemEmpDefine> qcItemEmps = this.jCQCItemEmpDefineManager.getModelsByQCItemIDX(qcItem.getIdx());
            if (null == qcItemEmps || qcItemEmps.isEmpty()) {
                continue;
            }
            
            for (JCQCItemEmpDefine qcEmp : qcItemEmps) {
                List<JCQCItemEmpOrgDefine> qcEmpOrgs = this.jCQCItemEmpOrgDefineManager.getModelsByQCEmpIDX(qcEmp.getIdx());
                // 如果该人员下没有维护班组信息，则表示该人员可以检查所有班组的工单
                if (null == qcEmpOrgs || qcEmpOrgs.isEmpty()) {
                    saveOrUpdate(workCard, qcItem, qcEmp);
                // 如果维护了可以检查的班组信息，则当前作业人员所在班组被包含时，表示该人员可以检查
                } else {
                    for (JCQCItemEmpOrgDefine qcEmpOrg : qcEmpOrgs) {
                        // Modified by hetao on 2016-05-11 修改条件判断的语法错误，对于基础类型的封装类型的比较不能使用等于（==）比较符
                        if (qcEmpOrg.getCheckOrgID().equals(SystemContext.getOmOrganization().getOrgid())) {
                            saveOrUpdate(workCard, qcItem, qcEmp);
                        }
                    }
                }
            }
        }
    }

    /**
     * <li>说明：保存无需指派的质量检查人员
     * <li>创建人：何涛
     * <li>创建日期：2016-4-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCard 作业工单实体
     * @param qcItem 质量检查项基础配置信息
     * @param qcEmp 质量检查人员基础配置信息
     * @throws NoSuchFieldException
     */
    private void saveOrUpdate(WorkCard workCard, JCQCItemDefine qcItem, JCQCItemEmpDefine qcEmp) throws NoSuchFieldException {
        // Modified by hetao on 2016-05-11 同一质量检查项可能存在有多个人可以检查的情况
        QCParticipant entity = new QCParticipant();
        entity.setRdpIDX(workCard.getRdpIDX());
        entity.setWorkCardIDX(workCard.getIdx());
        entity.setCheckItemCode(qcItem.getQCItemNo());
        entity.setCheckPersonID(String.valueOf(qcEmp.getCheckEmpID()));    // 质量检查参与者id
        entity.setCheckPersonName(qcEmp.getCheckEmpName());                // 质量检查参与者名称
        this.saveOrUpdate(entity);
    }
    
    /**
     * <li>说明：通过记录卡，检测类型及检测人员查询可测人员实体
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业工单主键
     * @param checkItemCode 质量检查项编码
     * @param checkPersonID 质量检查项检测人员ID
     * @return QCParticipant 可测人员实体
     */
    public QCParticipant getModel(String workCardIDX, String checkItemCode,String checkPersonID) {
        String hql = "From QCParticipant Where recordStatus = 0 And workCardIDX = ? And checkItemCode = ? And checkPersonID = ?";
        return (QCParticipant) this.daoUtils.findSingle(hql, new Object[]{ workCardIDX, checkItemCode,checkPersonID});
    }
    
    /**
     * <li>说明：查询指定工单下指定质量检查项的质量检查参与者对象
     * <li>创建人：何涛
     * <li>创建日期：2016-4-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业工单主键
     * @param checkItemCode 质量检查项编码
     * @return 机车检修质量检验参与者实体对象
     */
    public QCParticipant getModel(String workCardIDX, String checkItemCode) {
        String hql = "From QCParticipant Where recordStatus = 0 And workCardIDX = ? And checkItemCode = ?";
        return (QCParticipant) this.daoUtils.findSingle(hql, new Object[]{ workCardIDX, checkItemCode});
    }
    /**
     * <li>说明：查询指定工单下的质量检查参与者对象
     * <li>创建人：何涛
     * <li>创建日期：2016-4-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业工单主键
     * @return 机车检修质量检验参与者实体对象
     */
    @SuppressWarnings("unchecked")
    public List<QCParticipant> getModels(String workCardIDX) {
        String hql = "From QCParticipant Where recordStatus = 0 And workCardIDX = ?";
        return this.daoUtils.find(hql, new Object[]{ workCardIDX });
    }
    
	/**
     * <li>说明：保存指派的质量检查参与者
     * <li>创建人：程锐
     * <li>创建日期：2014-11-24
     * <li>修改人：何涛
     * <li>修改日期：2016-04-12 
     * <li>修改内容：增加Deprecated注解，不再建议使用该方法，建议使用saveIsAssignParticiant(WorkCard workCard, QCResultVO[] result)方法
     * @param workCardIDX 作业工单主键
     * @param checkPersonID 检查人ID
     * @param checkPersonName 检查人名称
     * @param checkItemCode 检查项编码
     */
    @Deprecated
	public void saveIsAssignParticiant(String workCardIDX, 
									   String checkPersonID, 
									   String checkPersonName, 
									   String checkItemCode) {
		String sql = SqlMapUtil.getSql("jxgc-qc:saveIsAssignParticiant")
								.replace("#WORKCARDIDX#", workCardIDX)
								.replace("#CHECKPERSONID#", checkPersonID)
								.replace("#CHECKPERSONNAME#", checkPersonName)
								.replace("#SITEID#", JXSystemProperties.SYN_SITEID)
								.replace("#OPERATORID#", String.valueOf(SystemContext.getAcOperator().getOperatorid()))
								.replace("#CONST_INT_IS_ASSIGN_Y#", JCQCItemDefine.CONST_INT_IS_ASSIGN_Y + "")
								.replace("#STATUS_WKF#", QCResult.STATUS_WKF + "")
								.replace("#CHECKITEMCODE#", checkItemCode)
								.replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss));
		daoUtils.executeSql(sql);
	}
    
	/**
     * <li>说明：根据作业班组和质检项配置保存质检项参与者
     * <li>创建人：程锐
     * <li>创建日期：2014-11-24
     * <li>修改人：何涛
     * <li>修改日期：2016-04-12 
     * <li>修改内容：增加Deprecated注解，不再建议使用该方法，建议使用saveNotAssignParticiant(WorkCard workCard)方法
     * @param workCardIDX 作业工单主键
     * @param checkOrgID 检查组织机构ID
     */
    @Deprecated
	public void saveNotAssignParticiant(String workCardIDX, String checkOrgID) {
		String sql = SqlMapUtil.getSql("jxgc-qc:saveNotAssignParticiant")
								.replace("#WORKCARDIDX#", workCardIDX)
								.replace("#SITEID#", JXSystemProperties.SYN_SITEID)
								.replace("#OPERATORID#", String.valueOf(SystemContext.getAcOperator().getOperatorid()))
								.replace("#CONST_INT_IS_ASSIGN_N#", JCQCItemDefine.CONST_INT_IS_ASSIGN_N + "")
								.replace("#STATUS_WKF#", QCResult.STATUS_WKF + "")
								.replace("#CHECKORGID#", checkOrgID)
                                .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss));
		daoUtils.executeSql(sql);
	}

    /**
     * <li>说明：获取作业工单指派的质量检查人员
     * <li>创建人：何涛
     * <li>创建日期：2016-4-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业工单主键
     * @return 质量检查人员集合
     */
    public List<Map<String, Object>> getQCEmpsForAssign(String workCardIDX) {
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        
        List<QCParticipant> entityList = this.getModels(workCardIDX);
        Map<String, Object> map = null;
        for (QCParticipant participant : entityList) {
            map = new HashMap<String, Object>();
            map.put("checkItemCode", participant.getCheckItemCode());       // 质量检查编码
            map.put("checkPersonID", participant.getCheckPersonID());       // 质量人员ID
            list.add(map);
        }
        return list;
    }
    
    /**
     * <li>说明：查询指定工单下指定质量检查项的质量检查参与者对象并且checkItemCode不在QCresult中出现过
     * <li>创建人：林欢
     * <li>创建日期：2016-4-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业工单主键
     * @return 机车检修质量检验参与者实体对象
     */
    @SuppressWarnings("unchecked")
    public List<QCParticipant> getModelListNotInQCResultByWorkCardIDX(String workCardIDX) {
        String hql = "From QCParticipant a Where a.recordStatus = 0 And a.workCardIDX = ? ";
        hql += " and a.workCardIDX not in (select b.workCardIDX from QCResult b where a.checkItemCode = b.checkItemCode and qcEmpID is not null)";
        
        return this.daoUtils.find(hql, new Object[]{ workCardIDX});
    }
    
    /**
     * <li>说明：查询指定工单下的质量检查Code checkItemCode
     * <li>创建人：林欢
     * <li>创建日期：2016-4-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业工单主键
     * @return 机车检修质量检验参与者实体对象
     */
    @SuppressWarnings("unchecked")
    public List<String> getCheckItemCodeGroupBy(String workCardIDX) {
        String hql = "select checkItemCode From QCParticipant Where recordStatus = 0 And workCardIDX = ? group by checkItemCode";
        return this.daoUtils.find(hql, new Object[]{ workCardIDX });
    }
}