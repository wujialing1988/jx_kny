package com.yunda.jx.jxgc.dispatchmanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：RepairLine实体类, 数据表：检修流水线
 * <li>创建人：程锐
 * <li>创建日期：2012-12-07
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JXGC_REPAIR_LINE")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class RepairLine implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* 修车流水线 */
    public static final int TYPE_TRAIN = 10;
    
    /* 修配件流水线 */
    public static final int TYPE_PARTS = 20;
    
    /* 新增状态 */
    public static final int NEW_STATUS = 10;
    
    /* 启用状态 */
    public static final int USE_STATUS = 20;
    
    /* 作废状态 */
    public static final int NULLIFY_STATUS = 30;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 流水线编码 */
    @Column(name = "Repair_Line_Code")
    private String repairLineCode;
    
    /* 流水线名称 */
    @Column(name = "Repair_Line_Name")
    private String repairLineName;
    
    /* 10：修车流水线，20：修配件流水线 */
    @Column(name = "Repair_Line_Type")
    private Integer repairLineType;
    
    /* 所属股道编码 */
    @Column(name = "Track_Code")
    private String trackCode;
    
    /* 所属股道 */
    @Column(name = "Track_Name")
    private String trackName;
    
    /* 所属车间ID */
    @Column(name = "Plant_ORGID")
    private Long plantOrgId;
    
    /* 所属车间名称 */
    @Column(name = "Plant_ORGNAME")
    private String plantOrgName;
    
    /* 所属车间序列 */
    @Column(name = "Plant_ORGSEQ")
    private String plantOrgSeq;
    
    /* 状态，10：新增；20：启用；30：作废 */
    private Integer status;
    
    /* 备注 */
    private String remarks;
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "RECORD_STATUS")
    private Integer recordStatus;
    
    /* 站点标识，为了同步数据而使用 */
    @Column(updatable = false)
    private String siteID;
    
    /* 创建人 */
    @Column(updatable = false)
    private Long creator;
    
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME", updatable = false)
    private java.util.Date createTime;
    
    /* 修改人 */
    private Long updator;
    
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIME")
    private java.util.Date updateTime;
    
    /**
     * 空构造
     */
    public RepairLine(){}
    
    /**
     * hql查询构造
     * @param idx
     * @param repairLineName
     * @param repairLineCode
     */
    public RepairLine(String idx, String repairLineName, String repairLineCode){
        this.idx = idx;
        this.repairLineName = repairLineName;
        this.repairLineCode = repairLineCode;
    }
    
    /**
     * @return String 获取流水线编码
     */
    public String getRepairLineCode() {
        return repairLineCode;
    }
    
    /**
     * @param 设置流水线编码
     */
    public void setRepairLineCode(String repairLineCode) {
        this.repairLineCode = repairLineCode;
    }
    
    /**
     * @return String 获取流水线名称
     */
    public String getRepairLineName() {
        return repairLineName;
    }
    
    /**
     * @param 设置流水线名称
     */
    public void setRepairLineName(String repairLineName) {
        this.repairLineName = repairLineName;
    }
    
    /**
     * @return Integer 获取流水线类型
     */
    public Integer getRepairLineType() {
        return repairLineType;
    }
    
    /**
     * @param 设置流水线类型
     */
    public void setRepairLineType(Integer repairLineType) {
        this.repairLineType = repairLineType;
    }
    
    /**
     * @return String 获取所属股道编码
     */
    public String getTrackCode() {
        return trackCode;
    }
    
    /**
     * @param 设置所属股道编码
     */
    public void setTrackCode(String trackCode) {
        this.trackCode = trackCode;
    }
    
    /**
     * @return String 获取所属股道
     */
    public String getTrackName() {
        return trackName;
    }
    
    /**
     * @param 设置所属股道
     */
    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }
    
    /**
     * @return Long 获取所属车间ID
     */
    public Long getPlantOrgId() {
        return plantOrgId;
    }
    
    /**
     * @param 设置所属车间ID
     */
    public void setPlantOrgId(Long plantOrgId) {
        this.plantOrgId = plantOrgId;
    }
    
    /**
     * @return String 获取所属车间名称
     */
    public String getPlantOrgName() {
        return plantOrgName;
    }
    
    /**
     * @param 设置所属车间名称
     */
    public void setPlantOrgName(String plantOrgName) {
        this.plantOrgName = plantOrgName;
    }
    
    /**
     * @return String 获取所属车间序列
     */
    public String getPlantOrgSeq() {
        return plantOrgSeq;
    }
    
    /**
     * @param 设置所属车间序列
     */
    public void setPlantOrgSeq(String plantOrgSeq) {
        this.plantOrgSeq = plantOrgSeq;
    }
    
    /**
     * @return Integer 获取状态
     */
    public Integer getStatus() {
        return status;
    }
    
    /**
     * @param 设置状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    /**
     * @return String 获取备注
     */
    public String getRemarks() {
        return remarks;
    }
    
    /**
     * @param 设置备注
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    /**
     * @return Integer 获取记录状态
     */
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    /**
     * @param 设置记录状态
     */
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    /**
     * @return String 获取站点标识
     */
    public String getSiteID() {
        return siteID;
    }
    
    /**
     * @param 设置站点标识
     */
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    /**
     * @return Long 获取创建人
     */
    public Long getCreator() {
        return creator;
    }
    
    /**
     * @param 设置创建人
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
     * @param 设置创建时间
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
     * @param 设置修改人
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
     * @param 设置修改时间
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
     * @param 设置idx主键
     */
    public void setIdx(String idx) {
        this.idx = idx;
    }
}
