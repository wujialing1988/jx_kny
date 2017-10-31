package com.yunda.zb.zbfw.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbfwTrianCenter实体类, 数据表：范围活和车型车号绑定的中间表
 * <li>创建人：林欢
 * <li>创建日期：2016-07-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbfwTrainCenterDTO implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@Id	
	private String idx;
	/* 整备范围名称 */
	@Column(name="FW_Name")
	private String fwName;
	/* 整备范围主键 */
	@Column(name="zbfw_IDX")
	private String zbfwIDX;
	/* 车号 */
	@Column(name="Train_No")
	private String trainNo;
    /* 车型主键 */
    @Column(name="Train_Type_IDX")
    private String trainTypeIDX;
    /* 车型简称 */
    @Column(name="Train_Type_ShortName")
    private String trainTypeShortName;
	
    /* 是否本段 */
    @Column(name="isThisSite")
    private String isThisSite;

    
    public String getFwName() {
        return fwName;
    }

    
    public void setFwName(String fwName) {
        this.fwName = fwName;
    }

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public String getIsThisSite() {
        return isThisSite;
    }

    
    public void setIsThisSite(String isThisSite) {
        this.isThisSite = isThisSite;
    }

    
    public String getTrainNo() {
        return trainNo;
    }

    
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    
    public String getTrainTypeIDX() {
        return trainTypeIDX;
    }

    
    public void setTrainTypeIDX(String trainTypeIDX) {
        this.trainTypeIDX = trainTypeIDX;
    }

    
    public String getTrainTypeShortName() {
        return trainTypeShortName;
    }

    
    public void setTrainTypeShortName(String trainTypeShortName) {
        this.trainTypeShortName = trainTypeShortName;
    }

    
    public String getZbfwIDX() {
        return zbfwIDX;
    }

    
    public void setZbfwIDX(String zbfwIDX) {
        this.zbfwIDX = zbfwIDX;
    }
	
    
}