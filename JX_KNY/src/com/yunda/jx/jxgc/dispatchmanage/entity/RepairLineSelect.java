package com.yunda.jx.jxgc.dispatchmanage.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 流水线切换选择视图
 * <li>创建人：程锐
 * <li>创建日期：2013-5-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="V_JXGC_REPAIRLINE")
public class RepairLineSelect {
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;   
    /* idx主键 */
    @Id
    private String idx;
    /* 流水线编码 */
    private String repairLineCode;
    
    /* 流水线名称 */
    private String repairLineName;
    
    /* 10：修车流水线，20：修配件流水线 */
    private Integer repairLineType;
    
    /* 所属股道编码 */
    private String trackCode;
    
    /* 所属股道 */
    private String trackName;
    
    /* 所属车间ID */
    private Long plantOrgId;
    
    /* 所属车间名称 */
    private String plantOrgName;
    
    /* 所属车间序列 */
    private String plantOrgSeq;
    
    /* 工位数 */
    private String workStationCount;
    
    /* 工艺节点实例主键 */
    private String nodeCaseIdx;
    
    /* 工位主键 */
    private String workStationIDX;
    
    /* 工位编码 */
    private String workStationCode;
    
    /* 工位名称 */
    private String workStationName;
    
    /* 工艺实例主键 */
    private String tecProcessCaseIDX;
    
    /* 相关工艺节点数 */
    private Integer nodeCaseCount;

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public Long getPlantOrgId() {
        return plantOrgId;
    }

    
    public void setPlantOrgId(Long plantOrgId) {
        this.plantOrgId = plantOrgId;
    }

    
    public String getPlantOrgName() {
        return plantOrgName;
    }

    
    public void setPlantOrgName(String plantOrgName) {
        this.plantOrgName = plantOrgName;
    }

    
    public String getPlantOrgSeq() {
        return plantOrgSeq;
    }

    
    public void setPlantOrgSeq(String plantOrgSeq) {
        this.plantOrgSeq = plantOrgSeq;
    }

    
    public String getRepairLineCode() {
        return repairLineCode;
    }

    
    public void setRepairLineCode(String repairLineCode) {
        this.repairLineCode = repairLineCode;
    }

    
    public String getRepairLineName() {
        return repairLineName;
    }

    
    public void setRepairLineName(String repairLineName) {
        this.repairLineName = repairLineName;
    }

    
    public Integer getRepairLineType() {
        return repairLineType;
    }

    
    public void setRepairLineType(Integer repairLineType) {
        this.repairLineType = repairLineType;
    }

    
    public String getTrackCode() {
        return trackCode;
    }

    
    public void setTrackCode(String trackCode) {
        this.trackCode = trackCode;
    }

    
    public String getTrackName() {
        return trackName;
    }

    
    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    
    public String getWorkStationCount() {
        return workStationCount;
    }

    
    public void setWorkStationCount(String workStationCount) {
        this.workStationCount = workStationCount;
    }


    
    public String getNodeCaseIdx() {
        return nodeCaseIdx;
    }


    
    public void setNodeCaseIdx(String nodeCaseIdx) {
        this.nodeCaseIdx = nodeCaseIdx;
    }


    
    public String getWorkStationIDX() {
        return workStationIDX;
    }


    
    public void setWorkStationIDX(String workStationIDX) {
        this.workStationIDX = workStationIDX;
    }


    
    public String getTecProcessCaseIDX() {
        return tecProcessCaseIDX;
    }


    
    public void setTecProcessCaseIDX(String tecProcessCaseIDX) {
        this.tecProcessCaseIDX = tecProcessCaseIDX;
    }


    
    public String getWorkStationCode() {
        return workStationCode;
    }


    
    public void setWorkStationCode(String workStationCode) {
        this.workStationCode = workStationCode;
    }


    
    public String getWorkStationName() {
        return workStationName;
    }


    
    public void setWorkStationName(String workStationName) {
        this.workStationName = workStationName;
    }


    
    public Integer getNodeCaseCount() {
        return nodeCaseCount;
    }


    
    public void setNodeCaseCount(Integer nodeCaseCount) {
        this.nodeCaseCount = nodeCaseCount;
    }
    
}
