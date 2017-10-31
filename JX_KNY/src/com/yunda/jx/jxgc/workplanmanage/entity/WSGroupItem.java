package com.yunda.jx.jxgc.workplanmanage.entity;

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
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WSGroupItem实体 数据表：工位组明细
 * <li>创建人：何涛
 * <li>创建日期：2015-4-26
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JXGC_WS_Group_Item")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class WSGroupItem {
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 工位主键 */
    @Column(name = "WS_IDX")
    private String wsIDX;
    
    /* 工位组主键 */
    @Column(name = "WS_Group_IDX")
    private String wsGroupIDX;
    
    /* 顺序号 */
    @Column(name = "Seq_No")
    private Integer seqNo;
    
    /* 工位编码 */
    @Transient
    private String workStationCode;
    
    /* 工位名称 */
    @Transient
    private String workStationName;
    
    /* 流水线主键 */
    @Transient
    private String repairLineIdx;
    
    /* 流水线名称 */
    @Transient
    private String repairLineName;
    
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：何涛
     * <li>创建日期：2015-4-27
     * <li>修改人： 
     * <li>修改日期：
     * @param idx idx主键
     * @param wsIDX 工位主键
     * @param wsGroupIDX 工位组主键
     * @param seqNo 顺序号
     * @param workStationCode 工位编码
     * @param workStationName 工位名称
     * @param repairLineIdx 流水线主键
     * @param repairLineName 流水线名称
     */
    public WSGroupItem(String idx, String wsIDX, String wsGroupIDX, Integer seqNo, String workStationCode, String workStationName, String repairLineIdx, String repairLineName) {
        super();
        this.idx = idx;
        this.wsIDX = wsIDX;
        this.wsGroupIDX = wsGroupIDX;
        this.seqNo = seqNo;
        this.workStationCode = workStationCode;
        this.workStationName = workStationName;
        this.repairLineIdx = repairLineIdx;
        this.repairLineName = repairLineName;
    }
    
    /**
     * default constructor
     */
    public WSGroupItem() {
        super();
    }

    /**
     * @return String 获取工位编码
     */
    public String getWorkStationCode() {
        return workStationCode;
    }
    
    /**
     * @param workStationCode 设置工位编码
     */
    public void setWorkStationCode(String workStationCode) {
        this.workStationCode = workStationCode;
    }
    
    /**
     * @return String 获取工位名称
     */
    public String getWorkStationName() {
        return workStationName;
    }
    
    /**
     * @param workStationName 设置工位名称
     */
    public void setWorkStationName(String workStationName) {
        this.workStationName = workStationName;
    }
    
    /**
     * @return String 获取流水线主键
     */
    public String getRepairLineIdx() {
        return repairLineIdx;
    }
    
    /**
     * @param repairLineIdx 设置流水线主键
     */
    public void setRepairLineIdx(String repairLineIdx) {
        this.repairLineIdx = repairLineIdx;
    }
    
    /**
     * @return String 获取流水线名称
     */
    public String getRepairLineName() {
        return repairLineName;
    }
    
    /**
     * @param repairLineName 设置流水线名称
     */
    public void setRepairLineName(String repairLineName) {
        this.repairLineName = repairLineName;
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
    
    /**
     * @return 获取工位组主键
     */
    public String getWsGroupIDX() {
        return wsGroupIDX;
    }
    
    /**
     * @param wsGroupIDX 设置工位组主键
     */
    public void setWsGroupIDX(String wsGroupIDX) {
        this.wsGroupIDX = wsGroupIDX;
    }
    
    /**
     * @return 获取工位主键
     */
    public String getWsIDX() {
        return wsIDX;
    }
    
    /**
     * @param wsIDX 设置工位主键
     */
    public void setWsIDX(String wsIDX) {
        this.wsIDX = wsIDX;
    }
    
    /**
     * @return Integer 获取顺序号
     */
    public Integer getSeqNo() {
        return seqNo;
    }
    
    /**
     * @param seqNo 设置顺序号
     */
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
    
}
