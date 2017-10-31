package com.yunda.zb.zbfw.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbFwWi实体类, 数据表：整备作业项目
 * <li>创建人：王利成
 * <li>创建日期：2015-01-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="ZB_ZBFW_WI")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbFwWi implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    
    //已核实 1
    public static final Integer DOCHECK = 1;
    //未核实 0
    public static final Integer DONOTCHECK = 0;
    
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 整备范围主键 */
	@Column(name="ZBFW_IDX")
	private String zbfwIDX;
	/* 编号 */
	@Column(name="WI_Code")
	private String wICode;
	/* 名称 */
	@Column(name="WI_Name")
	private String wIName;
	/* 描述 */
	@Column(name="WI_Desc")
	private String wIDesc;
	/* 顺序号 */
	@Column(name="Seq_No")
	private Integer seqNo;
	/* 记录状态，1：删除；0：未删除； */
	@Column(name="Record_Status")
	private Integer recordStatus;
    /* 节点主键 */
    @Column(name="NODE_IDX")
    private String nodeIDX;
	@Transient
	private String fwidx;
    /* 是否照相核实，1：核实；0：未核实； */
    @Column(name="isCheckPicture")
    private Integer isCheckPicture;
    
    public Integer getIsCheckPicture() {
        return isCheckPicture;
    }
    
    public void setIsCheckPicture(Integer isCheckPicture) {
        this.isCheckPicture = isCheckPicture;
    }
    /**
	 * 
	 * <li>说明：默认构造方法
	 * <li>创建人：王利成
	 * <li>创建日期：2015-1-27
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public ZbFwWi(){}
	/**
	 * 
	 * <li>说明：整备范围车型列表实体构造方法
	 * <li>创建人：王利成
	 * <li>创建日期：2015-1-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param idx 主键
	 * @param zbfwIDX 范围主键
	 * @param wICode 编号
	 * @param wIName 名称
	 * @param wIDesc 描述
	 * @param seqNo 顺序号
	 */
	public ZbFwWi(String idx,String zbfwIDX,String wICode,String wIName,String wIDesc, Integer seqNo){
		super();
		this.idx = idx;
		this.zbfwIDX = zbfwIDX;
		this.wICode = wICode;
		this.wIName = wIName;
		this.wIDesc = wIDesc;
		this.seqNo = seqNo;
	}
	
	

	public String getFwidx() {
		return fwidx;
	}
	public void setFwidx(String fwidx) {
		this.fwidx = fwidx;
	}
	/**
	 * @return String 获取整备范围主键
	 */
	public String getZbfwIDX(){
		return zbfwIDX;
	}
	public void setZbfwIDX(String zbfwIDX) {
		this.zbfwIDX = zbfwIDX;
	}
	/**
	 * @return String 获取编号
	 */
	public String getWICode(){
		return wICode;
	}
	public void setWICode(String wICode) {
		this.wICode = wICode;
	}
	/**
	 * @return String 获取名称
	 */
	public String getWIName(){
		return wIName;
	}
	public void setWIName(String wIName) {
		this.wIName = wIName;
	}
	/**
	 * @return String 获取描述
	 */
	public String getWIDesc(){
		return wIDesc;
	}
	public void setWIDesc(String wIDesc) {
		this.wIDesc = wIDesc;
	}
	/**
	 * @return Integer 获取顺序号
	 */
	public Integer getSeqNo(){
		return seqNo;
	}
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
	/**
	 * @return Integer 获取记录状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}
	/**
	 * @return String idx主键
	 */
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
    
    public String getNodeIDX() {
        return nodeIDX;
    }
    
    public void setNodeIDX(String nodeIDX) {
        this.nodeIDX = nodeIDX;
    }
}