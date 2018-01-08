package com.yunda.freight.base.report.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.freight.base.report.entity.ReportView;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 报表统计业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-12-27 11:36:09
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("reportViewManager")
public class ReportViewManager extends JXBaseManager<ReportView, ReportView> implements IbaseCombo {
    
	/**
     * <li>说明：树形列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param reportType 报表分类
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> findReportViewForTree(String reportType) {
        StringBuilder hql = new StringBuilder();
        hql.append(" From ReportView t where t.recordStatus = 0");
        if(!StringUtil.isNullOrBlank(reportType)){
        	hql.append(" and reportType = '"+reportType+"'");
        }
        hql.append(" order by seqNo ");
        boolean isLeaf = true;
        List<ReportView> list = this.daoUtils.find(hql.toString());
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        for (ReportView report : list) {           
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            nodeMap.put("id",  report.getIdx());
            nodeMap.put("text", report.getDisplayName());
            nodeMap.put("url", report.getReportUrl());
            nodeMap.put("leaf", isLeaf);
            nodeMap.put("iconCls", "groupCheckedIcon");
            children.add(nodeMap);
        }
        return children;
    }
   
}
