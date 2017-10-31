package com.yunda.jx.pjwz.turnover.entity;
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
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 大部件周转计划
 * <li>创建人：曾雪
 * <li>创建日期：2016-7-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="PJWZ_PARTS_ZZJH")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsZzjh implements Serializable{

    /**  类型：long  */
    private static final long serialVersionUID = 1L;

    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 作业计划兑现单主键 */
    @Column(name = "WORK_PLAN_IDX")
    private String workPlanIdx;
    
    /* 车型名称 */
    @Column(name = "TRAIN_TYPE")
    private String traintype;
    
    /* 车号 */
    @Column(name = "TRAIN_NO")
    private String trainno;
    
    /*修程*/
    @Column(name = "REPAIR_CLASS")
    private String repairclass;
    
    /*修次*/
    @Column(name = "REPAIR_TIME")
    private String repairtime;
    
    /*下车配件清单ID*/
    @Column(name = "OFF_PARTS_LIST_IDX")
    private String offPartsListIdx;
    
    /*零部件名称主键*/
    @Column(name = "PARTS_IDX")
    private String partsIdx;
    
    /*零部件名称*/
    @Column(name = "PARTS_NAME")
    private String partsName;
    
    /*位置名称*/
    @Column(name = "WZMC")
    private String wzmc;

    /*位置代码*/
    @Column(name = "WZDM")
    private String wzdm;

    /*下车配件周转台账主键*/
    @Column(name = "XC_PARTS_ACCOUNT_IDX")
    private String xcPartsAccountIdx;
    
    /*下车配件编码*/
    @Column(name = "XCPJBH")
    private String xcpjbh;
    
    /* 计划下车时间 */
    @Column(name = "JHXCSJ")
    @Temporal(TemporalType.DATE)
    private Date jhxcsj;
    
    /* 实际下车时间 */
    @Column(name = "SJXCSJ")
    @Temporal(TemporalType.DATE)
    private Date sjxcsj;
    
    /* 责任部门 */
    @Column(name = "ZRBM")
    private String zrbm;
    
    /* 计划送出日期 */
    @Column(name = "JHSCRQ")
    @Temporal(TemporalType.DATE)
    private Date jhscrq;
    
    /* 实际送出日期 */
    @Column(name = "SJSCRQ")
    @Temporal(TemporalType.DATE)
    private Date sjscrq;
    
    /* 计划到段日期 */
    @Column(name = "JHDDRQ")
    @Temporal(TemporalType.DATE)
    private Date jhddrq;
    
    /* 实际到段日期 */
    @Column(name = "SJDDRQ")
    @Temporal(TemporalType.DATE)
    private Date sjddrq;
    
    /*  下车配件周转台账主键 */
    @Column(name = "SC_PARTS_ACCOUNT_IDX")
    private String scPartsAccountIdx;
    
    /* 上车配件编码 */
    @Column(name = "SCPJBM")
    private String scpjbm;
    
    /* 计划上车时间 */
    @Column(name = "JHSCSJ")
    @Temporal(TemporalType.DATE)
    private Date jhscsj;

    /* 实际上车时间 */
    @Column(name = "SJSCSJ")
    @Temporal(TemporalType.DATE)
    private Date sjscsj;

    /* 延误原因 */
    @Column(name = "YWYY")
    private String ywyy;

    /* 延误时长 */
    @Column(name = "YWSC")
    private Integer ywsc;

    /* 修改人 */
    @Column(name = "Updator")
    private Long updator;
    
    /* 修改人名称 */
    @Column(name = "UpdatorName")
    private Long updatorName;
    
    /* 修改时间 */
    @Temporal(TemporalType.DATE)
    @Column(name="Update_Time")
    private java.util.Date updateTime;
    
    /* 创建人 */
	@Column(updatable=false)
	private Long creator;
	
    /* 修改人名称 */
    @Column(name = "CreatorName")
    private Long creatorName;

	
	/* 创建时间 */
	@Temporal(TemporalType.DATE)
	@Column(name="Create_Time",updatable=false)
	private java.util.Date createTime;


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


	public Long getCreatorName() {
		return creatorName;
	}


	public void setCreatorName(Long creatorName) {
		this.creatorName = creatorName;
	}


	public String getIdx() {
		return idx;
	}


	public void setIdx(String idx) {
		this.idx = idx;
	}


	public Date getJhddrq() {
		return jhddrq;
	}


	public void setJhddrq(Date jhddrq) {
		this.jhddrq = jhddrq;
	}


	public Date getJhscrq() {
		return jhscrq;
	}


	public void setJhscrq(Date jhscrq) {
		this.jhscrq = jhscrq;
	}


	public Date getJhscsj() {
		return jhscsj;
	}


	public void setJhscsj(Date jhscsj) {
		this.jhscsj = jhscsj;
	}


	public Date getJhxcsj() {
		return jhxcsj;
	}


	public void setJhxcsj(Date jhxcsj) {
		this.jhxcsj = jhxcsj;
	}


	public String getOffPartsListIdx() {
		return offPartsListIdx;
	}


	public void setOffPartsListIdx(String offPartsListIdx) {
		this.offPartsListIdx = offPartsListIdx;
	}


	public String getPartsIdx() {
		return partsIdx;
	}


	public void setPartsIdx(String partsIdx) {
		this.partsIdx = partsIdx;
	}


	public String getPartsName() {
		return partsName;
	}


	public void setPartsName(String partsName) {
		this.partsName = partsName;
	}


	public String getRepairclass() {
		return repairclass;
	}


	public void setRepairclass(String repairclass) {
		this.repairclass = repairclass;
	}


	public String getRepairtime() {
		return repairtime;
	}


	public void setRepairtime(String repairtime) {
		this.repairtime = repairtime;
	}


	public String getScPartsAccountIdx() {
		return scPartsAccountIdx;
	}


	public void setScPartsAccountIdx(String scPartsAccountIdx) {
		this.scPartsAccountIdx = scPartsAccountIdx;
	}


	public String getScpjbm() {
		return scpjbm;
	}


	public void setScpjbm(String scpjbm) {
		this.scpjbm = scpjbm;
	}


	public Date getSjddrq() {
		return sjddrq;
	}


	public void setSjddrq(Date sjddrq) {
		this.sjddrq = sjddrq;
	}


	public Date getSjscrq() {
		return sjscrq;
	}


	public void setSjscrq(Date sjscrq) {
		this.sjscrq = sjscrq;
	}


	public Date getSjscsj() {
		return sjscsj;
	}


	public void setSjscsj(Date sjscsj) {
		this.sjscsj = sjscsj;
	}


	public Date getSjxcsj() {
		return sjxcsj;
	}


	public void setSjxcsj(Date sjxcsj) {
		this.sjxcsj = sjxcsj;
	}


	public String getTrainno() {
		return trainno;
	}


	public void setTrainno(String trainno) {
		this.trainno = trainno;
	}


	public String getTraintype() {
		return traintype;
	}


	public void setTraintype(String traintype) {
		this.traintype = traintype;
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


	public Long getUpdatorName() {
		return updatorName;
	}


	public void setUpdatorName(Long updatorName) {
		this.updatorName = updatorName;
	}


	public String getWorkPlanIdx() {
		return workPlanIdx;
	}


	public void setWorkPlanIdx(String workPlanIdx) {
		this.workPlanIdx = workPlanIdx;
	}


	public String getWzdm() {
		return wzdm;
	}


	public void setWzdm(String wzdm) {
		this.wzdm = wzdm;
	}


	public String getWzmc() {
		return wzmc;
	}


	public void setWzmc(String wzmc) {
		this.wzmc = wzmc;
	}


	public String getXcPartsAccountIdx() {
		return xcPartsAccountIdx;
	}


	public void setXcPartsAccountIdx(String xcPartsAccountIdx) {
		this.xcPartsAccountIdx = xcPartsAccountIdx;
	}


	public String getXcpjbh() {
		return xcpjbh;
	}


	public void setXcpjbh(String xcpjbh) {
		this.xcpjbh = xcpjbh;
	}


	public Integer getYwsc() {
		return ywsc;
	}


	public void setYwsc(Integer ywsc) {
		this.ywsc = ywsc;
	}


	public String getYwyy() {
		return ywyy;
	}


	public void setYwyy(String ywyy) {
		this.ywyy = ywyy;
	}


	public String getZrbm() {
		return zrbm;
	}


	public void setZrbm(String zrbm) {
		this.zrbm = zrbm;
	}
    
    
    

}
