package com.yunda.jx.pjjx.partsrdp.entity;

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
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpNodeMat实体类, 数据表：物料实例
 * <li>创建人：张迪
 * <li>创建日期：2016-9-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="pjjx_parts_rdp_node_mat")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsRdpNodeMat implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 作业主键 */
	@Column(name="Rdp_IDX")
	private String rdpIDX;
	/* 检修流程节点主键 */
	@Column(name="Rdp_Node_IDX")
	private String rdpNodeIDX;
	/* 检修用料定义主键 */
	@Column(name="WP_MAT_IDX")
	private String wpMatIDX;
    
	/* 物料编码 */
	@Column(name="Mat_Code")
	private String matCode;
	/* 物料描述 */
	@Column(name="Mat_Desc")
	private String matDesc;
	/* 单位 */
	private String unit;
	/* 单价 */
    @Transient
	private Double price;
	/* 额定数量 匹配物料消耗中的额定数量的名称，保持一致*/
	@Column(name="Default_Qty")
	private Long numberRated;
    
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
	/* 创建人 */
	@Column(updatable=false)
	private Long creator;
	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Create_Time",updatable=false)
	private java.util.Date createTime;
	/* 修改人 */
	private Long updator;
	/* 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Update_Time")
	private java.util.Date updateTime;
    @Transient
    private Double qty;
    
    public PartsRdpNodeMat(){ super();};
    /**
     * @param idx idx主键
     * @param rdpNodeIDX 检修节点idx主键 
     * @param matCode 物料编码
     * @param matDesc 物料描述
     * @param unit 计量单位
     * @param qty 数量
     * @param price 联合查询字段 - 单价
     */
    public PartsRdpNodeMat(String idx, String rdpNodeIDX, String matCode, String matDesc, String unit, Long numberRated, Double price) {
        super();
        this.idx = idx;
        this.rdpNodeIDX = rdpNodeIDX;
        this.matCode = matCode;
        this.matDesc = matDesc;
        this.unit = unit;
        this.numberRated = numberRated;
        this.price = price;
       
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
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getMatCode() {
        return matCode;
    }
    
    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }
    
    public String getMatDesc() {
        return matDesc;
    }
    
    public void setMatDesc(String matDesc) {
        this.matDesc = matDesc;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public String getRdpIDX() {
        return rdpIDX;
    }
    
    public void setRdpIDX(String rdpIDX) {
        this.rdpIDX = rdpIDX;
    }
    
    public String getRdpNodeIDX() {
        return rdpNodeIDX;
    }
    
    public void setRdpNodeIDX(String rdpNodeIDX) {
        this.rdpNodeIDX = rdpNodeIDX;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public String getSiteID() {
        return siteID;
    }
    
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
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
    
    public String getWpMatIDX() {
        return wpMatIDX;
    }
    
    public void setWpMatIDX(String wpMatIDX) {
        this.wpMatIDX = wpMatIDX;
    }
    
    public Long getNumberRated() {
        return numberRated;
    }
    
    public void setNumberRated(Long numberRated) {
        this.numberRated = numberRated;
    }
    
    public Double getQty() {
        return qty;
    }
    
    public void setQty(Double qty) {
        this.qty = qty;
    }


}