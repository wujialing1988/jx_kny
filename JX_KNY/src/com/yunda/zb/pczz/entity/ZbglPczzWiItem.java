package com.yunda.zb.pczz.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglPczzWiItem实体类, 数据表：普查整治任务项
 * <li>创建人：王利成
 * <li>创建日期：2015-03-04
 * <li>修改人: 林欢
 * <li>修改日期：2016-08-18
 * <li>修改内容：普查整治功能重做
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="ZB_ZBGL_PCZZ_wi_Item")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbglPczzWiItem implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
    
    /* 普查整治任务单主键 */
    @Column(name="ZBGL_PCZZ_wi_IDX")
    private String zbglPczzWiIDX;
    
    /* 项目名称 */
    @Column(name="Item_Name")
    private String itemName;
    
    /* 项目内容 */
    @Column(name="Item_Content")
    private String itemContent;
    
    /* 更新时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Update_Time")
    private Date updateTime;
    
    /* 项目完成情况/结果 */
    @Column(name="item_resualt")
    private String itemResualt;
    
	/* 项目完成状态 (1==完毕 0==未完毕 2==已检查)*/
	@Column(name="item_status")
	private Integer itemStatus;
    
    /* 项目完成时间*/
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="item_time")
    private Date itemTime;
    
    /* 作业班组idx */
    @Column(name="Handle_OrgID")
    private Long handleOrgID;
    
    /* 作业班组名称 */
    @Column(name="Handle_OrgName")
    private String handleOrgName;
    
    /* 作业人idx */
    @Column(name="Handle_Person_IDX")
    private Long handlePersonIDX;
    
    /* 作业人名称 */
    @Column(name="Handle_Person_Name")
    private String handlePersonName;
    
    /* 领活时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Fetch_Time")
    private Date fetchTime;
    
    /* 领活时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Handle_Time")
    private Date handleTime;
    
    /* 检查时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="check_date")
    private Date checkDate;
    
    /* 检查人idx */
    @Column(name="check_Person_IDX")
    private Long checkPersonIDX;
    
    /* 检查人名称 */
    @Column(name="check_Person_Name")
    private String checkPersonName;

    
    public Date getCheckDate() {
        return checkDate;
    }

    
    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    
    public Long getCheckPersonIDX() {
        return checkPersonIDX;
    }

    
    public void setCheckPersonIDX(Long checkPersonIDX) {
        this.checkPersonIDX = checkPersonIDX;
    }

    
    public String getCheckPersonName() {
        return checkPersonName;
    }

    
    public void setCheckPersonName(String checkPersonName) {
        this.checkPersonName = checkPersonName;
    }

    
    public Date getFetchTime() {
        return fetchTime;
    }

    
    public void setFetchTime(Date fetchTime) {
        this.fetchTime = fetchTime;
    }

    
    public Long getHandleOrgID() {
        return handleOrgID;
    }

    
    public void setHandleOrgID(Long handleOrgID) {
        this.handleOrgID = handleOrgID;
    }

    
    public String getHandleOrgName() {
        return handleOrgName;
    }

    
    public void setHandleOrgName(String handleOrgName) {
        this.handleOrgName = handleOrgName;
    }

    
    public Long getHandlePersonIDX() {
        return handlePersonIDX;
    }

    
    public void setHandlePersonIDX(Long handlePersonIDX) {
        this.handlePersonIDX = handlePersonIDX;
    }

    
    public String getHandlePersonName() {
        return handlePersonName;
    }

    
    public void setHandlePersonName(String handlePersonName) {
        this.handlePersonName = handlePersonName;
    }

    
    public Date getHandleTime() {
        return handleTime;
    }

    
    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public String getItemContent() {
        return itemContent;
    }

    
    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }

    
    public String getItemName() {
        return itemName;
    }

    
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    
    public String getItemResualt() {
        return itemResualt;
    }

    
    public void setItemResualt(String itemResualt) {
        this.itemResualt = itemResualt;
    }

    
    public Integer getItemStatus() {
        return itemStatus;
    }

    
    public void setItemStatus(Integer itemStatus) {
        this.itemStatus = itemStatus;
    }

    
    public Date getUpdateTime() {
        return updateTime;
    }

    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    
    public String getZbglPczzWiIDX() {
        return zbglPczzWiIDX;
    }

    
    public void setZbglPczzWiIDX(String zbglPczzWiIDX) {
        this.zbglPczzWiIDX = zbglPczzWiIDX;
    }


    
    public Date getItemTime() {
        return itemTime;
    }


    
    public void setItemTime(Date itemTime) {
        this.itemTime = itemTime;
    }
    
    
}