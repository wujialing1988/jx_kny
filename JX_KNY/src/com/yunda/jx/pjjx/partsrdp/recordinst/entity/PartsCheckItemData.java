package com.yunda.jx.pjjx.partsrdp.recordinst.entity;

import java.io.Serializable;
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
 * <li>说明: 检测项编码（又可视化系统获取）
 * <li>创建人：林欢
 * <li>创建日期：2016-6-12
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */

@Entity
@Table(name = "PJJX_CHECK_ITEM_DATA")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsCheckItemData implements Serializable {

    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;

    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;

    /* 检测项编码 */
    @Column(name = "check_ID")
    private String checkID;

    /* 检测值 */
    @Column(name = "check_Value")
    private String checkValue;

    /* 配件idx */
    @Column(name = "parts_ID")
    private String partID;

    /* 检测时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "check_Time")
    private Date checkTime;
    
    /* 检测结束时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "check_End_Time")
    private Date checkEndTime;
    
    /* 配件检测检修项idx */
    @Column(name = "parts_Rdp_RecordDI_IDX")
    private String partsRdpRecordDIIDX;
    
    public Date getCheckEndTime() {
        return checkEndTime;
    }

    public void setCheckEndTime(Date checkEndTime) {
        this.checkEndTime = checkEndTime;
    }

    public String getPartsRdpRecordDIIDX() {
        return partsRdpRecordDIIDX;
    }

    public void setPartsRdpRecordDIIDX(String partsRdpRecordDIIDX) {
        this.partsRdpRecordDIIDX = partsRdpRecordDIIDX;
    }

    public String getCheckID() {
        return checkID;
    }

    public void setCheckID(String checkID) {
        this.checkID = checkID;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckValue() {
        return checkValue;
    }

    public void setCheckValue(String checkValue) {
        this.checkValue = checkValue;
    }

    public String getPartID() {
        return partID;
    }

    public void setPartID(String partID) {
        this.partID = partID;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

}
