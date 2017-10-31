package com.yunda.jx.wlgl.movewh.entity;

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
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatMoveWH实体类, 数据表：移库单
 * <li>创建人：程梅
 * <li>创建日期：2016-05-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="WLGL_Mat_Move_WH")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class MatMoveWH implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    /** 移库状态 - 待确认移入*/
    public static final String STATUS_WAITCHECK = "待确认移入";
    /** 移库状态 - 退回原库待确认*/
    public static final String STATUS_BACKWAITCHECK = "退回原库待确认";
    /** 移库状态 - 确认入库*/
    public static final String STATUS_CHECKED = "确认入库";
    /** 移库状态 - 退回原库入库*/
    public static final String STATUS_BACKCHECKED = "退回原库入库";
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 出库单id */
	@Column(name="out_wh_idx")
	private String outWhIdx;
	/* 接收库房主键 */
	@Column(name="Get_WH_IDX")
	private String getWhIDX;
	/* 接收库房名称 */
	@Column(name="Get_WH_Name")
	private String getWhName;
	/* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
	/* 移库状态（待确认移入、退回原库待确认、确认入库、退回原库入库） */
	@Column(name="move_status")
	private String movestatus;
	/* 处理日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="check_DATE")
	private java.util.Date checkDate;
	/* 处理人主键 */
	@Column(name="check_EMP_ID")
	private Long checkEmpID;
	/* 处理人名称 */
	@Column(name="check_EMP")
	private String checkEmp;

	/**
	 * @return String 获取出库单id
	 */
	public String getOutWhIdx(){
		return outWhIdx;
	}
	/**
	 * @param outWhIdx 设置出库单id
	 */
	public void setOutWhIdx(String outWhIdx) {
		this.outWhIdx = outWhIdx;
	}
	/**
	 * @return String 获取接收库房主键
	 */
	public String getGetWhIDX(){
		return getWhIDX;
	}
	/**
	 * @param getWhIDX 设置接收库房主键
	 */
	public void setGetWhIDX(String getWhIDX) {
		this.getWhIDX = getWhIDX;
	}
	/**
	 * @return String 获取接收库房名称
	 */
	public String getGetWhName(){
		return getWhName;
	}
	/**
	 * @param getWhName 设置接收库房名称
	 */
	public void setGetWhName(String getWhName) {
		this.getWhName = getWhName;
	}
	/**
	 * @return String 获取站点标识
	 */
	public String getSiteID(){
		return siteID;
	}
	/**
	 * @param siteID 设置站点标识
	 */
	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}
	/**
	 * @return String 获取移库状态
	 */
	public String getMovestatus(){
		return movestatus;
	}
	/**
	 * @param movestatus 设置移库状态
	 */
	public void setMovestatus(String movestatus) {
		this.movestatus = movestatus;
	}
	/**
	 * @return java.util.Date 获取处理日期
	 */
	public java.util.Date getCheckDate(){
		return checkDate;
	}
	/**
	 * @param checkDate 设置处理日期
	 */
	public void setCheckDate(java.util.Date checkDate) {
		this.checkDate = checkDate;
	}
	/**
	 * @return Long 获取处理人主键
	 */
	public Long getCheckEmpID(){
		return checkEmpID;
	}
	/**
	 * @param checkEmpID 设置处理人主键
	 */
	public void setCheckEmpID(Long checkEmpID) {
		this.checkEmpID = checkEmpID;
	}
	/**
	 * @return String 获取处理人名称
	 */
	public String getCheckEmp(){
		return checkEmp;
	}
	/**
	 * @param checkEmp 设置处理人名称
	 */
	public void setCheckEmp(String checkEmp) {
		this.checkEmp = checkEmp;
	}
	/**
	 * @return String idx主键
	 */
	public String getIdx() {
		return idx;
	}
	/**
	 * @param idx 设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}
}