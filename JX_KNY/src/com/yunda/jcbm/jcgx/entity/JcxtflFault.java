package com.yunda.jcbm.jcgx.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>类型名称：机车分类故障现象
 * <li>说明：
 * <li>创建人： 何东
 * <li>创建日期：2016-5-16
 * <li>修改人： 
 * <li>修改日期：
 */
@Entity
@Table(name="T_JCBM_JCXTFL_FAULT")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class JcxtflFault implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    /** 业务编码规则 - 故障编号 */
    public static final String  FAULT_ID_DEF= "JXGC_FAULT_ID_DEF";
    
	/* 主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	@Column(name="IDX")
	private String idx;
	
	/* 分类编码 */
	@Column(name="FLBM")
	private String flbm;
	
	/* 故障编号 */
	@Column(name="FAULT_ID")
	private String faultId;
	
	/* 故障名称 */
	@Column(name="FAULT_NAME")
	private String faultName;
	
	/* 故障类别编码 */
	@Column(name="FAULT_TYPE_ID")
	private String faultTypeID;
	
	/* 故障类别名称 */
	@Column(name="FAULT_TYPE_NAME")
	private String faultTypeName;
	
      /* 顺序号 */
    @Column(name="Seq_No")
    private Integer seqNo;
    
	
    public Integer getSeqNo() {
        return seqNo;
    }

    
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public String getFaultId() {
		return faultId;
	}

	public void setFaultId(String faultId) {
		this.faultId = faultId;
	}

	public String getFaultName() {
		return faultName;
	}

	public void setFaultName(String faultName) {
		this.faultName = faultName;
	}

	public String getFlbm() {
		return flbm;
	}

	public void setFlbm(String flbm) {
		this.flbm = flbm;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}


	public String getFaultTypeID() {
		return faultTypeID;
	}


	public void setFaultTypeID(String faultTypeID) {
		this.faultTypeID = faultTypeID;
	}


	public String getFaultTypeName() {
		return faultTypeName;
	}


	public void setFaultTypeName(String faultTypeName) {
		this.faultTypeName = faultTypeName;
	}
	
	
	
}