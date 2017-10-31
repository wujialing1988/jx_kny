package com.yunda.jx.jxgc.base.tpqcitemdefine.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 机车检修提票质量检查人员页面显示查询视图实体类
 * <li>创建人：程锐
 * <li>创建日期：2015-6-25
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "V_JXGC_TPQC_QC_EMP")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TPQCEmpView implements java.io.Serializable {
    
    /** 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** 质量检查人员idx主键 */
    @Id
    private String idx;
    
    /** 质量检查项主键 */
    @Column(name = "QC_Item_IDX")
    private String qCItemIDX;
    
    /** 检查人员 */
    @Column(name = "Check_EmpID")
    private Long checkEmpID;
    
    /** 检查人员名称 */
    @Column(name = "Check_EmpName")
    private String checkEmpName;
    
    /** 检查班组的组合字段字面值 */
    @Column(name = "Check_Org")
    private String checkOrg;
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public Long getCheckEmpID() {
        return checkEmpID;
    }
    
    public void setCheckEmpID(Long checkEmpID) {
        this.checkEmpID = checkEmpID;
    }
    
    public String getCheckEmpName() {
        return checkEmpName;
    }
    
    public void setCheckEmpName(String checkEmpName) {
        this.checkEmpName = checkEmpName;
    }
    
    public String getCheckOrg() {
        return checkOrg;
    }
    
    public void setCheckOrg(String checkOrg) {
        this.checkOrg = checkOrg;
    }
    
    public String getQCItemIDX() {
        return qCItemIDX;
    }
    
    public void setQCItemIDX(String itemIDX) {
        qCItemIDX = itemIDX;
    }
    
}
