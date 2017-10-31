package com.yunda.passenger.marshalling.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 肯尼亚综合管理信息系统
 * <li>说明: 编组基本信息
 * <li>创建人：张迪
 * <li>创建日期：2017-4-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "K_MARSHALLING")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class Marshalling implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 编组编号 */ 
    @Column(name = "MARSHALLING_CODE")
    private String marshallingCode;       
    
    /* 编组名称 */ 
    @Column(name = "MARSHALLING_NAME")
    private String marshallingName;   
    
    /* 备注 */ 
    private String remark;            
            
    /* 车辆数量 */ 
    @Column(name = "TRAIN_COUNT")
    private Integer trainCount;   
    
    @Transient
    private String marshallingTrainCountStr;
    
    @Transient
    private List<MarshallingTrain> marshallingTrainList;
    
    /* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 创建人 */
    @Column(updatable = false)
    private Long creator;
    
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Time", updatable = false)
    private java.util.Date createTime;
    
    /* 修改人 */
    private Long updator;
    
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
    
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public java.util.Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    
    public Long getCreator() {
        return creator;
    }
    
    public void setCreator(Long creator) {
        this.creator = creator;
    }
    
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public Long getUpdator() {
        return updator;
    }
    
    public void setUpdator(Long updator) {
        this.updator = updator;
    }

	public String getSiteID() {
		return siteID;
	}

	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}

	public String getMarshallingName() {
		return marshallingName;
	}

	public void setMarshallingName(String marshallingName) {
		this.marshallingName = marshallingName;
	}

	public String getMarshallingCode() {
		return marshallingCode;
	}

	public void setMarshallingCode(String marshallingCode) {
		this.marshallingCode = marshallingCode;
	}

	public java.lang.String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getTrainCount() {
		return trainCount;
	}

	public void setTrainCount(Integer trainCount) {
		this.trainCount = trainCount;
	}

	public List<MarshallingTrain> getMarshallingTrainList() {
		return marshallingTrainList;
	}

	public void setMarshallingTrainList(List<MarshallingTrain> marshallingTrainList) {
		this.marshallingTrainList = marshallingTrainList;
	}

	public String getMarshallingTrainCountStr() {
		return marshallingTrainCountStr;
	}

	public void setMarshallingTrainCountStr(String marshallingTrainCountStr) {
		this.marshallingTrainCountStr = marshallingTrainCountStr;
	}
    
    
}

