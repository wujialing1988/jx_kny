package com.yunda.zb.rdp.zbbill.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.CommonUtil;
import com.yunda.util.BeanUtils;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpNode;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpNodeExtConfig;
import com.yunda.zb.zbfw.entity.ZbglJobNodeExtConfigDef;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 机车整备作业节点扩展配置业务类
 * <li>创建人：程锐
 * <li>创建日期：2016-4-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2
 */
@Service(value = "zbglRdpNodeExtConfigManager")
public class ZbglRdpNodeExtConfigManager extends JXBaseManager<ZbglRdpNodeExtConfig, ZbglRdpNodeExtConfig>{

    /**
     * <li>说明：复制节点定义的扩展配置至流程节点的扩展配置
     * <li>创建人：程锐
     * <li>创建日期：2016-4-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeList 整备单关联的所有节点列表
     * @param rdpIDX   整备单主键
     * @throws Exception
     */
    public void saveByNode(List<ZbglRdpNode> nodeList, String rdpIDX) throws Exception {
        List<ZbglRdpNodeExtConfig> list = new ArrayList<ZbglRdpNodeExtConfig>();
        for (ZbglRdpNode node : nodeList) {
            List<ZbglJobNodeExtConfigDef> configDefList = getListByNodeIDX(node.getNodeDefIDX());
            if (configDefList == null || configDefList.isEmpty())
                continue;
            for (ZbglJobNodeExtConfigDef def : configDefList) {
                ZbglRdpNodeExtConfig config = new ZbglRdpNodeExtConfig();
                BeanUtils.copyProperties(config, def);
                config.setIdx("");
                config.setRdpIDX(rdpIDX);
                config.setNodeIDX(node.getIdx());
                list.add(config);
            }
        }
        saveOrUpdate(list);
    }
    
    /**
     * <li>说明：根据流程节点的节点定义IDX获取相关扩展配置定义列表
     * <li>创建人：程锐
     * <li>创建日期：2016-4-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 流程节点的节点定义IDX
     * @return 扩展配置定义列表
     */
    @SuppressWarnings("unchecked")
    private List<ZbglJobNodeExtConfigDef> getListByNodeIDX(String nodeIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put("nodeIDX", nodeIDX);
        StringBuilder hql = new StringBuilder();
        hql.append("from ZbglJobNodeExtConfigDef where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap));
        return daoUtils.find(hql.toString());
    }
    
    /**
     * <li>说明：根据机车整备单获取相关的扩展配置列表
     * <li>创建人：程锐
     * <li>创建日期：2016-4-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @param configName 扩展配置名称
     * @return 扩展配置列表
     */
    @SuppressWarnings("unchecked")
    public List<ZbglRdpNodeExtConfig> getFwListByRdp(String rdpIDX, String configName) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put("rdpIDX", rdpIDX);
        paramMap.put("configName", configName);
        StringBuilder hql = new StringBuilder();
        hql.append("from ZbglRdpNodeExtConfig where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap));
        return daoUtils.find(hql.toString());
    }
}
