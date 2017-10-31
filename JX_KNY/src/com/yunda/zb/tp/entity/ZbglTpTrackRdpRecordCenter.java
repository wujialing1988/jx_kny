package com.yunda.zb.tp.entity;

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
 * <li>说明：ZbglTpTrackRdpRecordCenter实体类, 数据表：提票跟踪记录单关联表
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
@Table(name = "zb_zbgl_jt6_record_center")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglTpTrackRdpRecordCenter implements java.io.Serializable {

	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/* idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/* 提票跟踪记录单idx */
	@Column(name = "track_rdp_record_idx")
	private String trackRdpRecordIDX;

	/* jt6提票主键idx */
	@Column(name = "jt6_idx")
	private String jt6IDX;

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getJt6IDX() {
		return jt6IDX;
	}

	public void setJt6IDX(String jt6IDX) {
		this.jt6IDX = jt6IDX;
	}

	public String getTrackRdpRecordIDX() {
		return trackRdpRecordIDX;
	}

	public void setTrackRdpRecordIDX(String trackRdpRecordIDX) {
		this.trackRdpRecordIDX = trackRdpRecordIDX;
	}

}
