package com.yunda.jx.jxgc.workplanmanage.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.CommonUtil;
import com.yunda.jx.jxgc.processdef.entity.JobNodeExtConfigDef;
import com.yunda.jx.jxgc.workplanmanage.entity.JobNodeExtConfig;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.util.BeanUtils;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 流程节点扩展配置业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-5-6
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
@Service(value = "jobNodeExtConfigManager")
public class JobNodeExtConfigManager extends JXBaseManager<JobNodeExtConfig, JobNodeExtConfig> {
    
    /**
     * <li>说明：复制节点定义的扩展配置至流程节点的扩展配置
     * <li>创建人：程锐
     * <li>创建日期：2015-5-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeList 作业计划关联的所有节点列表
     * @throws Exception
     */
    public void saveByNode(List<JobProcessNode> nodeList) throws Exception {
        List<JobNodeExtConfig> list = new ArrayList<JobNodeExtConfig>();
        for (JobProcessNode node : nodeList) {
            List<JobNodeExtConfigDef> configDefList = getListByNodeIDX(node.getNodeIDX());
            if (configDefList == null || configDefList.isEmpty())
                continue;
            for (JobNodeExtConfigDef def : configDefList) {
                JobNodeExtConfig config = new JobNodeExtConfig();
                BeanUtils.copyProperties(config, def);
                config.setIdx("");
                config.setNodeIDX(node.getIdx());
                list.add(config);
            }
        }
        saveOrUpdate(list);
    }
    
    /**
     * <li>说明：查询节点的机车状态配置
     * <li>创建人：程锐
     * <li>创建日期：2015-5-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 节点的机车状态配置
     */
    @SuppressWarnings("unchecked")
    public JobNodeExtConfig getTrainStatusConfig(String nodeIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put("nodeIDX", nodeIDX);
        paramMap.put("configName", JobNodeExtConfigDef.EXT_TRAIN_STATUS);
        return getConfig(paramMap);
    }
    
    /**
     * <li>说明：查询节点的质量检查卡控配置
     * <li>创建人：程锐
     * <li>创建日期：2015-5-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 节点的质量检查卡控配置
     */
    @SuppressWarnings("unchecked")
    public JobNodeExtConfig getQCConfig(String nodeIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put("nodeIDX", nodeIDX);
        paramMap.put("configName", JobNodeExtConfigDef.EXT_CHECK_CONTROL);
        return getConfig(paramMap);
    }
    
    /**
     * <li>说明：查询节点的检查提票卡控配置
     * <li>创建人：张迪
     * <li>创建日期：2016-10-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 节点的检查提票卡控配置
     */
    @SuppressWarnings("unchecked")
    public JobNodeExtConfig getTicketConfig(String nodeIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put("nodeIDX", nodeIDX);
        paramMap.put("configName", JobNodeExtConfigDef.EXT_CHECK_TICKET);
        return getConfig(paramMap);
    }
    
    /**
     * <li>说明：根据流程节点的节点定义IDX获取相关扩展配置定义列表
     * <li>创建人：程锐
     * <li>创建日期：2015-5-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 流程节点的节点定义IDX
     * @return 扩展配置定义列表
     */
    @SuppressWarnings("unchecked")
    private List<JobNodeExtConfigDef> getListByNodeIDX(String nodeIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put("nodeIDX", nodeIDX);
        StringBuilder hql = new StringBuilder();
        hql.append("from JobNodeExtConfigDef where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap));
        return daoUtils.find(hql.toString());
    }
    
    /**
     * <li>说明：查询流程节点扩展配置对象
     * <li>创建人：程锐
     * <li>创建日期：2015-5-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 流程节点扩展配置对象
     */
    private JobNodeExtConfig getConfig(Map paramMap) {
        List<JobNodeExtConfig> list = getConfigList(paramMap);
        if (list == null || list.size() < 1)
            return null;
        return list.get(0);
    }
    
    /**
     * <li>说明：查询流程节点扩展配置对象列表
     * <li>创建人：程锐
     * <li>创建日期：2015-5-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 流程节点扩展配置对象列表
     */
    @SuppressWarnings("unchecked")
    private List<JobNodeExtConfig> getConfigList(Map paramMap) {
        return daoUtils.find(getConfigHql(paramMap));
    }
    
    /**
     * <li>说明：查询流程节点扩展配置hql
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 查询流程节点hql
     */
    private String getConfigHql(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from JobNodeExtConfig where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap));
        return hql.toString();
    }
}
