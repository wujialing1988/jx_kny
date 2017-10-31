package com.yunda.jx.jxgc.workhis.workplan.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jxgc.workhis.workplan.entity.JobProcessNodeHis;
import com.yunda.jx.jxgc.workhis.workplan.entity.JobProcessNodeRelHis;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：JobProcessNode业务类,机车检修计划流程节点
 * <li>创建人：程梅
 * <li>创建日期：2015年8月17日
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 3.2
 */
@Service(value = "jobProcessNodeHisManager")
public class JobProcessNodeHisManager extends JXBaseManager<JobProcessNodeHis, JobProcessNodeHis> implements IbaseCombo{
        
    private static final String NODEIDX = "#nodeIDX#";
    
    /**
     * <li>说明：获取下级节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 下级节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNodeHis> getChildNodeList(String nodeIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put("parentIDX", nodeIDX);
        return getNodeList(paramMap);
    }
    
    /**
     * <li>说明：获取节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNodeHis> getNodeList(Map paramMap) {
        return daoUtils.find(getNodeHql(paramMap));
    }
    
    /**
     * <li>说明：获取本节点在内的所有第一层下级节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 本节点在内的所有第一层下级节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNodeHis> getAllFirstChildNodeList(String nodeIDX) {
        String sql = SqlMapUtil.getSql("jxgc-processNodeHis:getAllFirstChildNodeList").replace(NODEIDX, nodeIDX);
        return daoUtils.executeSqlQueryEntity(sql, JobProcessNodeHis.class);
    }
    
    /**
     * <li>说明：获取本节点在内的所有父级节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 本节点在内的所有父级节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNodeHis> getAllParentNodeList(String nodeIDX) {
        String sql = SqlMapUtil.getSql("jxgc-processNodeHis:getAllParentNodeList")
                               .replace(NODEIDX, nodeIDX);
        return daoUtils.executeSqlQueryEntity(sql, JobProcessNodeHis.class);
    }
    /**
     * <li>说明：获取流程节点的前置节点实体列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @param workPlanIDX 作业计划IDX
     * @return 流程节点的前置节点实体列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNodeHis> getPreNodeCaseList(String nodeIDX, String workPlanIDX) {
        List<JobProcessNodeHis> nodeCaseList = new ArrayList<JobProcessNodeHis>();
        StringBuilder hql = new StringBuilder("from JobProcessNodeRelHis where recordStatus = 0 and nodeIDX = '")
                                      .append(nodeIDX).append("'")
                                      .append(" and nodeIDX in (select idx from JobProcessNodeHis where workPlanIDX = '")
                                      .append(workPlanIDX)
                                      .append("' and recordStatus = 0) order by preNodeIDX");
        List<JobProcessNodeRelHis> sequenceList = daoUtils.find(hql.toString());
        if (sequenceList == null || sequenceList.size() < 1)
            return nodeCaseList;
        for (JobProcessNodeRelHis sequence : sequenceList) {
            if (StringUtil.isNullOrBlank(sequence.getPreNodeIDX()))
                continue;
            JobProcessNodeHis nodeCase = getModelById(sequence.getPreNodeIDX());
            if (nodeCase != null)
                nodeCaseList.add(nodeCase);
        }
        return nodeCaseList;
    }
    
    /**
     * <li>说明：查询流程节点hql
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 查询流程节点hql
     */
    private String getNodeHql(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from JobProcessNodeHis where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap)).append(" and recordStatus = 0");
        return hql.toString();
    }
}
