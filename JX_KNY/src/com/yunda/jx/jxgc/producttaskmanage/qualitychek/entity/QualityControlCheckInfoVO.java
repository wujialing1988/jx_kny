package com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 质量检验结果信息实体类
 * <li>创建人：程锐
 * <li>创建日期：2014-12-12
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class QualityControlCheckInfoVO implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* 检验人id */
    private String checkPersonIdx;
    
    /* 检验人名称 */
    private String checkPersonName;
    
    /* 检验时间 */
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date checkTime;
    
    /* 备注 */
    private String remarks;
    
    public String getCheckPersonIdx() {
        return checkPersonIdx;
    }
    
    public void setCheckPersonIdx(String checkPersonIdx) {
        this.checkPersonIdx = checkPersonIdx;
    }
    
    public String getCheckPersonName() {
        return checkPersonName;
    }
    
    public void setCheckPersonName(String checkPersonName) {
        this.checkPersonName = checkPersonName;
    }
    
    public java.util.Date getCheckTime() {
        return checkTime;
    }
    
    public void setCheckTime(java.util.Date checkTime) {
        this.checkTime = checkTime;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
}
