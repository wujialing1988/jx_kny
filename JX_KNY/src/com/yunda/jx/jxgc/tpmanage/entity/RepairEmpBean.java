package com.yunda.jx.jxgc.tpmanage.entity;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 作业人员实体类
 * <li>创建人：程锐
 * <li>创建日期：2015-7-13
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
public class RepairEmpBean {
    
    /* 工作人员ID */
    private Long workerID;
    
    /* 工作人员名称 */
    private String workerName;
    
    public Long getWorkerID() {
        return workerID;
    }
    
    public void setWorkerID(Long workerID) {
        this.workerID = workerID;
    }
    
    public String getWorkerName() {
        return workerName;
    }
    
    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }
    
}
