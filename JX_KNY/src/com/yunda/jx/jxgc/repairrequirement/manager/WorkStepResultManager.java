package com.yunda.jx.jxgc.repairrequirement.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.repairrequirement.entity.WorkStepResult;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 作业项对应的处理结果 业务类
 * <li>创建人：程锐
 * <li>创建日期：2013-5-1
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "workStepResultManager")
public class WorkStepResultManager extends JXBaseManager<WorkStepResult, WorkStepResult> {
    
    /**
     * <li>说明：设置默认检测/检修结果前作验证，验证该项目是否有默认检测/检修结果
     * <li>创建人：程锐
     * <li>创建日期：2012-11-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param t 检测/检修结果实体对象
     * @return 验证信息
     * @throws BusinessException
     */
    @Override
    public String[] validateUpdate(WorkStepResult t) throws BusinessException {
        String[] errs = null;
        /*StringBuilder hql = new StringBuilder();
        if(t.getIsDefault() != null && t.getIsDefault() == WorkStepResult.ISDEFAULT_YES){
            hql.append("from WorkStepResult where recordStatus = 0 and workStepIDX = '")
               .append(t.getWorkStepIDX()).append("' and isDefault = ").append(WorkStepResult.ISDEFAULT_YES);
            if(!StringUtil.isNullOrBlank(t.getIdx())){
                hql.append(" and idx != '").append(t.getIdx()).append("'");
            }
            List<WorkStepResult> list = daoUtils.find(hql.toString());
            if(list != null && list.size() > 0){
                errs = new String[1];
                errs[0] = "此检测/检修项目已有默认检测/检修结果！请重新选择是否默认。";
            }
        }*/
        return errs;
    }
    
    /**
     * <li>说明：根据检测/检修项目主键获取检测/检修结果列表
     * <li>创建人：程锐
     * <li>创建日期：2013-5-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workStepIDX 检测/检修项目主键
     * @return List<WorkStepResult> 检测/检修结果列表
     */
    public List<WorkStepResult> getListByWorkStep(String workStepIDX) {
        QueryCriteria<WorkStepResult> query = new QueryCriteria<WorkStepResult>();
        query.setEntityClass(WorkStepResult.class);
        List<Condition> whereList = new ArrayList<Condition>();
        Condition c = new Condition("workStepIDX", Condition.EQ, workStepIDX);
        c.setStringLike(false);
        whereList.add(c);
        query.setWhereList(whereList);
        return findList(query);
    }
    
    /**
     * <li>说明：保存检测/检修结果
     * <li>创建人：程锐
     * <li>创建日期：2013-5-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workStepResult 检测结果实体对象
     * @return void
     * @throws BusinessException, NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    public void saveWorkStepResult(WorkStepResult workStepResult) throws BusinessException, NoSuchFieldException {
        // 如果新增默认结果数据时，将以前检修项目关联结果都设为非默认结果
        if (workStepResult.getIsDefault() != null && workStepResult.getIsDefault() == WorkStepResult.ISDEFAULT_YES) {
            StringBuilder hql = new StringBuilder();
            hql.append("from WorkStepResult where recordStatus=0 and workStepIDX = '").append(workStepResult.getWorkStepIDX()).append("'");
            if (!StringUtil.isNullOrBlank(workStepResult.getIdx())) {
                hql.append(" and idx != '").append(workStepResult.getIdx()).append("'");
            }
            List<WorkStepResult> list = daoUtils.find(hql.toString());
            if (list != null && list.size() > 0) {
                for (WorkStepResult result : list) {
                    result.setIsDefault(WorkStepResult.ISDEFAULT_NO);
                    saveOrUpdate(result);
                }
            }
        }
        saveOrUpdate(workStepResult);
    }
    
    /**
     * <li>方法名称：getCurrentValue
     * <li>创建人：张凡
     * <li>创建时间：2013-5-16 下午03:26:30
     * <li>修改人：
     * <li>修改内容：
     * <li>方法说明：获取值
     * @param stepIdx 工步主键
     * @return WorkStepResult
     */
    public WorkStepResult getCurrentValue(String stepIdx) {
        String hql =
            "select new WorkStepResult(resultName) from WorkStepResult where recordStatus=0 and workStepIDX = '" + stepIdx + "' and isDefault = '"
                + WorkStepResult.ISDEFAULT_YES + "'";
        WorkStepResult entity = (WorkStepResult) daoUtils.findSingle(hql);
        return entity;
    }
    
    /**
     * <li>说明：根据机车检修作业任务获取处理结果
     * <li>创建人：何涛
     * <li>创建日期：2016-4-13
     * <li>修改人：何涛
     * <li>修改日期：2016-05-10
     * <li>修改内容：删除查询条件status的过滤，解决页面上不能加载已维护的作业任务默认处理结果列表的问题
     * @param workTaskIDX 作业任务主键
     * @return 作业任务处理结果集合
     */
    @SuppressWarnings("unchecked")
    public List<WorkStepResult> getModelsByWorkTaskIDX(String workTaskIDX) {
        String hql = "From WorkStepResult Where recordStatus = 0 And workStepIDX In (Select workStepIDX From WorkTask Where recordStatus = 0 And idx = ?)";
        return this.daoUtils.find(hql, new Object[]{ workTaskIDX });
    }
    
}
