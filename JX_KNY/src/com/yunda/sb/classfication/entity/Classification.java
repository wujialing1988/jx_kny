package com.yunda.sb.classfication.entity;

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
 * <li>标题: 机车设备管理信息系统
 * <li>说明：设备类别
 * <li>创建人：黄杨
 * <li>创建日期：2017-4-26
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "E_EQUIPMENT_CLASSES")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class Classification implements java.io.Serializable {

	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** 叶子节点：否【0】 */
	public static final int IS_LEAF_NO = 0;

	/** 叶子节点：是【1】 */
	public static final int IS_LEAF_YES = 1;

	/** idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 设备类别编码 */
	@Column(name = "CLASS_CODE")
	private String classCode;

	/** 父设备类别编号 */
	@Column(name = "PARENT_IDX")
	private String parentIdx;

	/** 设备类别名称 */
	@Column(name = "CLASS_NAME")
	private String className;

	/** 叶子节点，0：不是叶子节点，1：是叶子节点 */
	@Column(name = "leaf")
	private Integer leaf;

	/** 规格型号 */
	@Column(name = "SPECIFICATION")
	private String specification;

	/* 规格表达方式 */
	@Column(name = "SPECIFICATION_EXPRESS")
	private String specificationExpress;

	/** 型号表达方式 */
	@Column(name = "MODEL_EXPRESS")
	private String modelExpress;

	/** 预计使用年限 */
	@Column(name = "EXPECT_USE_YEAR")
	private Integer expectUseYear;

	/** 残值率 */
	@Column(name = "RESIDUAL_RATE")
	private Float residualRate;

	/** 折旧率 */
	@Column(name = "DEPRECITION_RATE")
	private Float depercitionRate;

	/** 备注 */
	private String remark;

	/* 数据状态 */
	@Column(name = "record_status")
	private Integer recordStatus;

	/* 创建人 */
	@Column(updatable = false)
	private Long creator;

	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", updatable = false)
	private java.util.Date createTime;

	/* 修改人 */
	private Long updator;

	/* 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time")
	private java.util.Date updateTime;

	/**
	 * @return String 获取规格
	 */
	public String getSpecification() {
		return specification;
	}

	/**
	 * @param specification
	 *            设置规格
	 */
	public void setSpecification(String specification) {
		this.specification = specification;
	}

	/**
	 * @return Integer 获取数据状态
	 */
	public Integer getRecordStatus() {
		return recordStatus;
	}

	/**
	 * @param recordStatus
	 *            设置数据状态
	 */
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	/**
	 * @return Long 获取创建人
	 */
	public Long getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            设置创建人
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}

	/**
	 * @return java.util.Date 获取创建时间
	 */
	public java.util.Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            设置创建时间
	 */
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return Long 获取修改人
	 */
	public Long getUpdator() {
		return updator;
	}

	/**
	 * @param updator
	 *            设置修改人
	 */
	public void setUpdator(Long updator) {
		this.updator = updator;
	}

	/**
	 * @return java.util.Date 获取修改时间
	 */
	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            设置修改时间
	 */
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return String idx主键
	 */
	public String getIdx() {
		return idx;
	}

	/**
	 * @param idx
	 *            设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSpecificationExpress() {
		return specificationExpress;
	}

	public void setSpecificationExpress(String specificationExpress) {
		this.specificationExpress = specificationExpress;
	}

	public String getModelExpress() {
		return modelExpress;
	}

	public void setModelExpress(String modelExpress) {
		this.modelExpress = modelExpress;
	}

	public Integer getExpectUseYear() {
		return expectUseYear;
	}

	public void setExpectUseYear(Integer expectUseYear) {
		this.expectUseYear = expectUseYear;
	}

	public Float getResidualRate() {
		return residualRate;
	}

	public void setResidualRate(Float residualRate) {
		this.residualRate = residualRate;
	}

	public Float getDepercitionRate() {
		return depercitionRate;
	}

	public void setDepercitionRate(Float depercitionRate) {
		this.depercitionRate = depercitionRate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getLeaf() {
		return leaf;
	}

	public void setLeaf(Integer leaf) {
		this.leaf = leaf;
	}

	public String getParentIdx() {
		return parentIdx;
	}

	public void setParentIdx(String parentIdx) {
		this.parentIdx = parentIdx;
	}

}
