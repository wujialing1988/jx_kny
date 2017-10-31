package com.yunda.freight.zb.qualitycontrol.entity;

import java.io.Serializable;

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
 * <li>说明：ZbglQualityControlItemDefine实体类, 数据表：检查项基础配置
 * <li>创建人：林欢
 * <li>创建日期：2016-10-11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */

@Entity
@Table(name="k_qc_item_define")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbglQualityControlItemDefine implements Serializable{

	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	/** 是否默认 - 否 */
	public static final int IS_DEFAULT_YES = 1;
	/** 是否默认 - 是 */
	public static final int IS_DEFAULT_NO = 0;
	
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	
	/* 检查项编码 */
	@Column(name="qc_item_no")
	private String qcItemNo;
	
	/* 检查项名称 */
	@Column(name="qc_item_name")
	private String qcItemName;
	
	/* 顺序号 */
	@Column(name="seq_no")
	private Integer seqNo;
	
	/* 站点标识 */
	@Column(name="siteid")
	private String siteID;
	
	/* 业务编码 */
	@Column(name="business_code")
	private String businessCode;
	
	/** 是否默认 */
	@Column(name = "is_Default")
	private Integer isDefault;
    
    /* 客货类型 10 货车 20 客车*/
    @Column(name = "T_VEHICLE_TYPE")
    private String vehicleType;
	
	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getQcItemName() {
		return qcItemName;
	}

	public void setQcItemName(String qcItemName) {
		this.qcItemName = qcItemName;
	}

	public String getQcItemNo() {
		return qcItemNo;
	}

	public void setQcItemNo(String qcItemNo) {
		this.qcItemNo = qcItemNo;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getSiteID() {
		return siteID;
	}

	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}

    
    public String getVehicleType() {
        return vehicleType;
    }

    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
	
	
}
