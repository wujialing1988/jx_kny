package com.yunda.jx.jxgc.repairrequirement.manager;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.repairrequirement.entity.DetectItem;
import com.yunda.jx.jxgc.repairrequirement.entity.QualityControl;
import com.yunda.jx.jxgc.repairrequirement.entity.WorkStep;
import com.yunda.jx.jxgc.repairrequirement.entity.WorkStepResult;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkStep业务类,工步
 * <li>创建人：王治龙
 * <li>创建日期：2012-12-19
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "workStepManager")
public class WorkStepManager extends JXBaseManager<WorkStep, WorkStep> {
    
    /** 定义质量控制业务类 */
    @Resource
    private QualityControlManager qualityControlManager;
    
    /** 定义检测项业务类 */
    @Resource
    private DetectItemManager detectItemManager;
    
    /** 检测/检修结果业务类 */
    @Resource
    private WorkStepResultManager workStepResultManager;
    
    /**
     * <li>说明：删除检测/检修项目及级联删除检测/检修结果、检测项、质量控制记录
     * <li>创建人：程锐
     * <li>创建日期：2013-5-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 检测/检修项目实体类主键idx数组
     * @throws BusinessException, NoSuchFieldException
     */
    public void logicDeleteWorkStep(Serializable... ids) throws BusinessException, NoSuchFieldException {
        List<WorkStep> entityList = new ArrayList<WorkStep>();
        for (Serializable id : ids) {
            qualityControlManager.deleteModelList(id.toString()); // 删除外键关联的质量控制数据
            List<DetectItem> itemList = detectItemManager.getModelList(id.toString());
            detectItemManager.logicDelete(itemList); // 删除检测项
            List<WorkStepResult> resultList = workStepResultManager.getListByWorkStep(id.toString());
            workStepResultManager.logicDelete(resultList);// 删除检测/检修结果
            WorkStep t = getModelById(id);
            t = EntityUtil.setSysinfo(t);
            // 设置逻辑删除字段状态为已删除
            t = EntityUtil.setDeleted(t);
            entityList.add(t);
        }
        this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
    }
    
    /**
     * <li>说明：通过工序主键查询工步
     * <li>创建人：王治龙
     * <li>创建日期：2012-12-25
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param String workStepIDX：关联外键ID ；
     * @return List<DetectItem> 检测项列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<WorkStep> getModelList(String workSeqIDX) throws BusinessException {
        StringBuffer hql = new StringBuffer();
        hql.append("from WorkStep t where t.recordStatus=0 ");
        if (!StringUtil.isNullOrBlank(workSeqIDX)) {
            hql.append(" and t.workSeqIDX = '").append(workSeqIDX).append("'");
        }
        return this.daoUtils.find(hql.toString());
    }
    
    /**
     * <li>说明：保存检测/检修项目及质量控制记录
     * <li>创建人：程锐
     * <li>创建日期：2013-5-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 检测/检修项目实体对象
     * @param qualityControlList 质量控制记录数组
     * @throws BusinessException, NoSuchFieldException
     */
    public void saveWorkStep(WorkStep entity, QualityControl[] qualityControlList) throws BusinessException, NoSuchFieldException {
        this.saveOrUpdate(entity);
        List<QualityControl> entityList = new ArrayList<QualityControl>();
        if (qualityControlList.length < 1) {
            qualityControlManager.deleteModelList(entity.getIdx());
        }
        for (QualityControl obj : qualityControlList) {
            if (!StringUtil.isNullOrBlank(entity.getIdx())) { // 不等于空说明是修改
                qualityControlManager.deleteModelList(entity.getIdx()); // 修改时将数据删除后再创建
            }
            obj.setRelationIDX(entity.getIdx()); // 设置关联的作业项（检测/检修项目）主键
            entityList.add(obj);
        }
        this.qualityControlManager.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：保存检测/检修项目
     * <li>创建人：程锐
     * <li>创建日期：2014-9-19
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveWorkStep_QrKey(WorkStep entity) throws BusinessException, NoSuchFieldException {
        // entity.setStatus(WorkSeq.STATUS_USE);
        this.saveOrUpdate(entity);
    }
    
    /**
     * <li>说明：根据质量记录单主键获取其检测/修项目中的最大作业顺序
     * <li>创建人：程锐
     * <li>创建日期：2013-5-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workSeqIDX 质量记录单主键
     * @return 最大作业顺序
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public int getMaxSeq(String workSeqIDX) throws Exception {
        String sql = "select max(work_step_seq) from JXGC_WORK_STEP where WORK_SEQ_IDX = '" + workSeqIDX + "' and record_status = 0";
        int seq = 1;
        List list = daoUtils.executeSqlQuery(sql);
        if (list != null && list.size() > 0) {
            if (list.get(0) != null && list.get(0) instanceof BigDecimal) {
                BigDecimal maxSeq = (BigDecimal) list.get(0);
                seq = Integer.parseInt(maxSeq.toString()) + 1;
            }
        }
        return seq;
    }
    
}
