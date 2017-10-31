package com.yunda.jx.jxgc.dispatchmanage.entity;

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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkStation实体类, 数据表：工位
 * <li>创建人：程锐
 * <li>创建日期：2012-12-07
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JXGC_WORK_STATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class WorkStation implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
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
    
    /* 工位编码 */
    @Column(name = "Work_Station_Code")
    private String workStationCode;
    
    /* 工位名称 */
    @Column(name = "Work_Station_Name")
    private String workStationName;
    
    /* 流水线主键 */
    @Column(name = "REPAIR_LINE_IDX")
    private String repairLineIdx;
    
    /* 流水线名称 */
    @Column(name = "Repair_Line_Name")
    private String repairLineName;
    
    /* 所属台位编码 检修V3.2 存台位的guid */
    @Column(name = "Desk_Code")
    private String deskCode;
    
    /* 工作班组编码 */
    @Column(name = "TEAM_ORGID")
    private Long teamOrgId;
    
    /* 工作班组名称 */
    @Column(name = "TEAM_ORGNAME")
    private String teamOrgName;
    
    /* 工作班组序列 */
    @Column(name = "TEAM_ORGSEQ")
    private String teamOrgSeq;
    
    /* 上级工位主键 */
    @Column(name = "PARENT_IDX")
    private String parentIDX;
    
    /* 所属台位 */
    @Column(name = "Desk_Name")
    private String deskName;
    
    /* 所属图 检修V3.2 存siteID */
    private String ownerMap;
    
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
    
    /* 机务设备主键 */
    private String equipIDX;
    
    /* 机务设备名称 */
    private String equipName;
    
    /* 流水线编码 */
    @Transient
    private String repairLineCode;
    
    /* 默认作业人员 */
    @Transient
    private String persons;
    
    /* 流水线类型（机车、配件） */
    @Transient
    private String lineType;
    
    @Transient
    private String nodeIDX;
    
    /**
     * 空构造
     */
    public WorkStation() {
    }
    
    /**
     * HQL查询构造
     * @param idx 主键
     * @param workStationName 工位名称
     */
    public WorkStation(String idx, String workStationName) {
        this.idx = idx;
        this.workStationName = workStationName;
    }
    
    /**
     * HQL查询 Manager.findListForTecProcessNode方法使用
     * @param idx 主键
     * @param workStationCode 工位编码
     * @param workStationName 工位名称
     * @param repairLineName 流水线名称
     */
    public WorkStation(String idx, String workStationCode, String workStationName, String repairLineName) {
        
        this.idx = idx;
        this.workStationCode = workStationCode;
        this.workStationName = workStationName;
        this.repairLineName = repairLineName;
    }
    
    /**
     * <li>说明：HQL查询 Manager.findListForBindWorkStation方法使用
     * <li>创建人：程梅
     * <li>创建日期：2015-5-28
     * <li>修改人：
     * <li>修改日期：
     * @param idx 主键
     * @param workStationCode 工位编码
     * @param workStationName 工位名称
     * @param repairLineIdx 流水线主键
     * @param equipIDX 设备IDX
     * @param equipName 设备名称
     * @param repairLineName 流水线名称
     * @param status 状态
     * @param remarks 备注
     */
    public WorkStation(String idx, String workStationCode, String workStationName, String repairLineIdx, String equipIDX, String equipName,
        String repairLineName, Integer status, String remarks) {
        
        this.idx = idx;
        this.workStationCode = workStationCode;
        this.workStationName = workStationName;
        this.repairLineIdx = repairLineIdx;
        this.equipIDX = equipIDX;
        this.equipName = equipName;
        this.repairLineName = repairLineName;
        this.status = status;
        this.remarks = remarks;
    }
    
    /**
     * <li>说明：HQL查询 Manager.findSelectPageList方法使用
     * <li>创建人：程锐
     * <li>创建日期：2015-9-8
     * <li>修改人： 
     * <li>修改日期：
     * @param idx 主键
     * @param workStationCode 工位编码
     * @param workStationName 工位名称
     * @param repairLineIdx 流水线主键
     * @param repairLineName 流水线名称
     * @param teamOrgId 工作班组编码
     * @param teamOrgName 工作班组名称
     * @param teamOrgSeq 工作班组序列
     */
    public WorkStation(String idx, String workStationCode, String workStationName, String repairLineIdx, String repairLineName, Long teamOrgId, String teamOrgName, String teamOrgSeq) {
        
        this.idx = idx;
        this.workStationCode = workStationCode;
        this.workStationName = workStationName;
        this.repairLineIdx = repairLineIdx;
        this.repairLineName = repairLineName;
        this.teamOrgId = teamOrgId;
        this.teamOrgName = teamOrgName;
        this.teamOrgSeq = teamOrgSeq;
    }

    /**
     * @return String 获取工位编码
     */
    public String getWorkStationCode() {
        return workStationCode;
    }
    
    public void setWorkStationCode(String workStationCode) {
        this.workStationCode = workStationCode;
    }
    
    /**
     * @return String 获取工位名称
     */
    public String getWorkStationName() {
        return workStationName;
    }
    
    public void setWorkStationName(String workStationName) {
        this.workStationName = workStationName;
    }
    
    /**
     * @return String 获取流水线主键
     */
    public String getRepairLineIdx() {
        return repairLineIdx;
    }
    
    public void setRepairLineIdx(String repairLineIdx) {
        this.repairLineIdx = repairLineIdx;
    }
    
    /**
     * @return String 获取流水线名称
     */
    public String getRepairLineName() {
        return repairLineName;
    }
    
    public void setRepairLineName(String repairLineName) {
        this.repairLineName = repairLineName;
    }
    
    /**
     * @return String 获取上级工位主键
     */
    public String getParentIDX() {
        return parentIDX;
    }
    
    /**
     * @param parentIDX 设置上级工位主键
     */
    public void setParentIDX(String parentIDX) {
        this.parentIDX = parentIDX;
    }
    
    /**
     * @return Integer 获取工作班组编码
     */
    public Long getTeamOrgId() {
        return teamOrgId;
    }
    
    /**
     * @param teamOrgId 设置工作班组编码
     */
    public void setTeamOrgId(Long teamOrgId) {
        this.teamOrgId = teamOrgId;
    }
    
    /**
     * @return String 获取工作班组名称
     */
    public String getTeamOrgName() {
        return teamOrgName;
    }
    
    /**
     * @param teamOrgName 设置工作班组名称
     */
    public void setTeamOrgName(String teamOrgName) {
        this.teamOrgName = teamOrgName;
    }
    
    /**
     * @return String 获取工作班组序列
     */
    public String getTeamOrgSeq() {
        return teamOrgSeq;
    }
    
    /**
     * @param teamOrgSeq 设置工作班组序列
     */
    public void setTeamOrgSeq(String teamOrgSeq) {
        this.teamOrgSeq = teamOrgSeq;
    }
    
    /**
     * @return String 获取所属台位编码
     */
    public String getDeskCode() {
        return deskCode;
    }
    
    public void setDeskCode(String deskCode) {
        this.deskCode = deskCode;
    }
    
    /**
     * @return String 获取所属台位
     */
    public String getDeskName() {
        return deskName;
    }
    
    /**
     * @param deskName 设置所属台位
     */
    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }
    
    /**
     * @return Integer 获取状态
     */
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    /**
     * @return String 获取备注
     */
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    /**
     * @return Integer 获取记录状态
     */
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    /**
     * @return String 获取站点标识
     */
    public String getSiteID() {
        return siteID;
    }
    
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    /**
     * @return Long 获取创建人
     */
    public Long getCreator() {
        return creator;
    }
    
    public void setCreator(Long creator) {
        this.creator = creator;
    }
    
    /**
     * @return java.util.Date 获取创建时间
     */
    public java.util.Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    
    /**
     * @return Long 获取修改人
     */
    public Long getUpdator() {
        return updator;
    }
    
    public void setUpdator(Long updator) {
        this.updator = updator;
    }
    
    /**
     * @return java.util.Date 获取修改时间
     */
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
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
    
    public String getOwnerMap() {
        return ownerMap;
    }
    
    public void setOwnerMap(String ownerMap) {
        this.ownerMap = ownerMap;
    }
    
    public String getPersons() {
        return persons;
    }
    
    public void setPersons(String persons) {
        this.persons = persons;
    }
    
    public String getRepairLineCode() {
        return repairLineCode;
    }
    
    public void setRepairLineCode(String repairLineCode) {
        this.repairLineCode = repairLineCode;
    }
    
    public String getEquipIDX() {
        return equipIDX;
    }
    
    public void setEquipIDX(String equipIDX) {
        this.equipIDX = equipIDX;
    }
    
    public String getEquipName() {
        return equipName;
    }
    
    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }
    
    public String getLineType() {
        return lineType;
    }
    
    public void setLineType(String lineType) {
        this.lineType = lineType;
    }
    
    public String getNodeIDX() {
        return nodeIDX;
    }
    
    public void setNodeIDX(String nodeIDX) {
        this.nodeIDX = nodeIDX;
    }
    
}
