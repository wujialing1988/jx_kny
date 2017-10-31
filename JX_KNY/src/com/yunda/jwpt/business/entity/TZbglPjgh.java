package com.yunda.jwpt.business.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车整备电子合格证- JT6提票（配件更换）(机务平台数据同步官方模型)
 * <li>创建人：林欢
 * <li>创建日期：2016-5-16
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */

@Entity
@Table(name = "T_ZBGL_PJGH")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TZbglPjgh implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    @Column(name = "PJGH_ID")
    private String pjghID;
    
    /** JT6ID 来源于表T_ZBGL_JT6中的主键 */
    @Column(name = "PJGH_JT6ID")
    private String pjghJt6ID;
    
    /** 机车构型码（来源于基础编码表） */
    @Column(name = "PJGH_JCGXBM")
    private String pjghJcgxbm;
    
    /** 机车部件名称 */
    @Column(name = "PJGH_JCBJMC")
    private String pjghJcbjmc;
    
    /** 下车部件编码 */
    @Column(name = "PJGH_XCBJBM")
    private String pjghXcbjbm;
    
    /** 更换部件编码 */
    @Column(name = "PJGH_GHBJBM")
    private String pjghGhbjbm;
    
    /** 记录状态 */
    @Column(name = "RECORD_STATUS")
    private Integer recordStatus;
    
    /** 创建人 */
    @Column(name = "CREATOR")
    private Integer creator;
    
    /** 创建时间 */
    @Column(name = "CREATE_TIME")
    private Date createTime;
    
    /** 修改人 */
    @Column(name = "UPDATOR")
    private Integer updator;
    
    /** 修改时间 */
    @Column(name = "UPDATE_TIME")
    private Date updateTime;
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Integer getCreator() {
        return creator;
    }
    
    public void setCreator(Integer creator) {
        this.creator = creator;
    }
    
    public String getPjghGhbjbm() {
        return pjghGhbjbm;
    }
    
    public void setPjghGhbjbm(String pjghGhbjbm) {
        this.pjghGhbjbm = pjghGhbjbm;
    }
    
    public String getPjghID() {
        return pjghID;
    }
    
    public void setPjghID(String pjghID) {
        this.pjghID = pjghID;
    }
    
    public String getPjghJcgxbm() {
        return pjghJcgxbm;
    }
    
    public void setPjghJcgxbm(String pjghJcgxbm) {
        this.pjghJcgxbm = pjghJcgxbm;
    }
    
    public String getPjghJcbjmc() {
        return pjghJcbjmc;
    }
    
    public void setPjghJcbjmc(String pjghJcbjmc) {
        this.pjghJcbjmc = pjghJcbjmc;
    }
    
    public String getPjghJt6ID() {
        return pjghJt6ID;
    }
    
    public void setPjghJt6ID(String pjghJt6ID) {
        this.pjghJt6ID = pjghJt6ID;
    }
    
    public String getPjghXcbjbm() {
        return pjghXcbjbm;
    }
    
    public void setPjghXcbjbm(String pjghXcbjbm) {
        this.pjghXcbjbm = pjghXcbjbm;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public Integer getUpdator() {
        return updator;
    }
    
    public void setUpdator(Integer updator) {
        this.updator = updator;
    }
    
}
