package com.yunda.jx.jxgc.tpmanage.entity;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 提票质量检验结果值对象，用于记录提票质量检验结果信息
 * <li>创建人：程锐
 * <li>创建日期：2015-7-13
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
public class FaultQCResultVO implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* 提票质检项主键 */
    private String idx;
    
    /* 提票单主键 */
    private String tpIDX;
    
    /* 检验项编码 */
    private String checkItemCode;
    
    /* 检验人员ID */
    private Long qcEmpID;
    
    /* 检验人员名称 */
    private String qcEmpName;
    
    /* 检验时间 */
    private java.util.Date qcTime;
    
    /* 备注 */
    private String remarks;
    
    public String getCheckItemCode() {
        return checkItemCode;
    }
    
    public void setCheckItemCode(String checkItemCode) {
        this.checkItemCode = checkItemCode;
    }
    
    public Long getQcEmpID() {
        return qcEmpID;
    }
    
    public void setQcEmpID(Long qcEmpID) {
        this.qcEmpID = qcEmpID;
    }
    
    public String getQcEmpName() {
        return qcEmpName;
    }
    
    public void setQcEmpName(String qcEmpName) {
        this.qcEmpName = qcEmpName;
    }
    
    public java.util.Date getQcTime() {
        return qcTime;
    }
    
    public void setQcTime(java.util.Date qcTime) {
        this.qcTime = qcTime;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public String getTpIDX() {
        return tpIDX;
    }
    
    public void setTpIDX(String tpIDX) {
        this.tpIDX = tpIDX;
    }
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
}
