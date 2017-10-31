package com.yunda.zb.pczz.entity;

import javax.persistence.*;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglPczzItemToTraininfo实体类, 数据表：普查整治项保存的车号
 * <li>创建人：林欢
 * <li>创建日期：2016-4-24
 * <li>修改人: 林欢
 * <li>修改日期：2016-8-18
 * <li>修改内容：普查整治功能重做
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="ZB_ZBGL_PCZZ_ITEM_TO_TRAININFO")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbglPczzItemToTraininfo implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 普查整治计划项主键 */
	@Column(name="ZBGL_PCZZ_ITEM_IDX")
	private String zbglPczzItemIDX;
	/* 车型简称 */
	@Column(name="Train_Type_ShortName")
	private String trainTypeShortName;
	/* 车型代码 */
	@Column(name="Train_Type_IDX")
	private String trainTypeIDX;
	/* 车号列表 */
	@Column(name="Train_No")
	private String trainNo;
    
    /* 普查整治计划主键 */
    @Column(name="ZBGL_PCZZ_IDX")
    private String zbglPczzIDX;
    
	
    public String getZbglPczzIDX() {
        return zbglPczzIDX;
    }
    
    public void setZbglPczzIDX(String zbglPczzIDX) {
        this.zbglPczzIDX = zbglPczzIDX;
    }
    public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getTrainTypeIDX() {
		return trainTypeIDX;
	}
	public void setTrainTypeIDX(String trainTypeIDX) {
		this.trainTypeIDX = trainTypeIDX;
	}
	public String getTrainTypeShortName() {
		return trainTypeShortName;
	}
	public void setTrainTypeShortName(String trainTypeShortName) {
		this.trainTypeShortName = trainTypeShortName;
	}
	public String getZbglPczzItemIDX() {
		return zbglPczzItemIDX;
	}
	public void setZbglPczzItemIDX(String zbglPczzItemIDX) {
		this.zbglPczzItemIDX = zbglPczzItemIDX;
	}
    
    public String getTrainNo() {
        return trainNo;
    }
    
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }
    
	
	
}