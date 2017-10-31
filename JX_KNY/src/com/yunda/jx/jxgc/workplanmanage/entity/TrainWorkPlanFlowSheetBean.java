package com.yunda.jx.jxgc.workplanmanage.entity;

import java.util.List;

import com.yunda.jx.pjwz.turnover.entity.OffPartListFlowSheetBean;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：张迪
 * <li>创建日期：2017-3-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public class TrainWorkPlanFlowSheetBean {
    
  
    private String idx;// 机车作业计划主键idx

    /*记录当前计划的所有一级节点*/
    private List<JobProcessNodeFlowSheetBean> fisrtNodes;
    
    /*记录当前计划的所有下车配件 */
    private List<OffPartListFlowSheetBean> offPartList;
      
    
    public List<JobProcessNodeFlowSheetBean> getFisrtNodes() {
        return fisrtNodes;
    }

    
    public void setFisrtNodes(List<JobProcessNodeFlowSheetBean> fisrtNodes) {
        this.fisrtNodes = fisrtNodes;
    }

    
    public List<OffPartListFlowSheetBean> getOffPartList() {
        return offPartList;
    }

    
    public void setOffPartList(List<OffPartListFlowSheetBean> offPartList) {
        this.offPartList = offPartList;
    }

    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
}
