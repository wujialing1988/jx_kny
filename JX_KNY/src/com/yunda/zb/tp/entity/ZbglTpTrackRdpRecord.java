package com.yunda.zb.tp.entity;

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
 * <li>说明：ZbglTpTrackRdpRecord实体类, 数据表：J提票跟踪记录单
 * <li>创建人：林欢
 * <li>创建日期：2016-8-5
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "zb_zbgl_jt6_track_rdp_record")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglTpTrackRdpRecord implements java.io.Serializable {

	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/* 已处理 */
	public static final Integer ALREADY = 1;

	/* 处理中 */
	public static final Integer UNREADY = 0;

	/* idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/* 提票跟踪单idx */
	@Column(name = "track_rdp_idx")
	private String trackRdpIDX;

	/* 跟踪意见 */
	@Column(name = "track_reason")
	private String trackReason;

	/* 跟踪状态(1==已处理 0==处理中) */
	@Column(name = "status")
	private Integer status;

	/* 跟踪次数 */
	@Column(name = "track_count")
	private Integer trackCount;

	/* 跟踪人员 */
	@Column(name = "track_person_idx")
	private String trackPersonIDX;
	
	/* 跟踪人员 */
	@Column(name = "track_person_name")
	private String trackPersonName;
	
	/* 跟踪单对应的整备单 */
	@Column(name = "rdp_idx")
	private String rdpIDX;

	/* 跟踪时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "track_date")
	private java.util.Date trackDate;

	/* 本次入段时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "in_time")
	private java.util.Date inTime;
	
	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getTrackCount() {
		return trackCount;
	}

	public void setTrackCount(Integer trackCount) {
		this.trackCount = trackCount;
	}

	public java.util.Date getTrackDate() {
		return trackDate;
	}

	public void setTrackDate(java.util.Date trackDate) {
		this.trackDate = trackDate;
	}

	public String getTrackPersonIDX() {
		return trackPersonIDX;
	}

	public void setTrackPersonIDX(String trackPersonIDX) {
		this.trackPersonIDX = trackPersonIDX;
	}

	public String getTrackPersonName() {
		return trackPersonName;
	}

	public void setTrackPersonName(String trackPersonName) {
		this.trackPersonName = trackPersonName;
	}

	public String getTrackRdpIDX() {
		return trackRdpIDX;
	}

	public void setTrackRdpIDX(String trackRdpIDX) {
		this.trackRdpIDX = trackRdpIDX;
	}

	public String getTrackReason() {
		return trackReason;
	}

	public void setTrackReason(String trackReason) {
		this.trackReason = trackReason;
	}

	public String getRdpIDX() {
		return rdpIDX;
	}

	public void setRdpIDX(String rdpIDX) {
		this.rdpIDX = rdpIDX;
	}

	public java.util.Date getInTime() {
		return inTime;
	}

	public void setInTime(java.util.Date inTime) {
		this.inTime = inTime;
	}

}
