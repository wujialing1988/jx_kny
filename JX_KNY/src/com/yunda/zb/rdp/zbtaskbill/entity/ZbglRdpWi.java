package com.yunda.zb.rdp.zbtaskbill.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglRdpWi实体类, 数据表：机车整备任务单
 * <li>创建人：程锐
 * <li>创建日期：2015-01-21
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "ZB_ZBGL_RDP_WI")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglRdpWi implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* 状态-待领活 */
    public static final String STATUS_TODO = "TODO";
    
    /* 状态-待销活、处理中 */
    public static final String STATUS_HANDLING = "ONGOING";
    
    /* 状态-销活 */
    public static final String STATUS_HANDLED = "COMPLETE";
    
    /* 任务单类型-正常整备范围 */
    public static final String WICLASS_ZBFW = "1";
    
    public static final String WICLASS_ZBFW_CH = "范围活";
    
    /* 任务单类型-技术指令及措施 */
    public static final String WICLASS_ZLCS = "2";
    
    public static final String WICLASS_ZLCS_CH = "技术指令措施任务单";
    
    /* 任务单类型-预警 */
    public static final String WICLASS_YJ = "3";
    
    public static final String WICLASS_YJ_CH = "检测预警检查任务单";
    

    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 整备单ID */
    @Column(name = "RDP_IDX")
    private String rdpIDX;
    
    /* 任务单类型,1：正常整备范围；3：预警 */
    @Column(name = "WI_Class")
    private String wiClass;
    
    /* 作业项目ID */
    @Column(name = "WI_IDX")
    private String wiIDX;
    
    /* 任务编号 */
    @Column(name = "WI_Code")
    private String wiCode;
    
    /* 任务名称 */
    @Column(name = "WI_Name")
    private String wiName;
    
    /* 任务描述 */
    @Column(name = "WI_Desc")
    private String wiDesc;
    
    /* 作业班组代码 */
    @Column(name = "Handle_OrgID")
    private Long handleOrgID;
    
    /* 作业班组名称 */
    @Column(name = "Handle_OrgName")
    private String handleOrgName;
    
    /* 作业人编码 */
    @Column(name = "Handle_Person_ID")
    private Long handlePersonID;
    
    /* 作业人名称 */
    @Column(name = "Handle_Person_Name")
    private String handlePersonName;
    
    /* 领活时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Fetch_Time")
    private java.util.Date fetchTime;
    
    /* 销活时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Handle_Time")
    private java.util.Date handleTime;
    
    /* 顺序号 */
    @Column(name = "Seq_No")
    private Integer seqNo;
    
    /* 任务单状态 */
    @Column(name = "WI_Status")
    private String wiStatus;
    
    /* 记录状态，1：删除；0：未删除； */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 最新更新时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
    
    /* 是否合格 */
    @Column(name = "IS_HG")
    private String isHg;

    /* 节点主键 */
    @Column(name = "Node_IDX")
    private String nodeIDX;
    
    /* 联合作业人员 */
    @Column(name = "worker")
    private String worker;
    
    /* 机车号 */
    @Transient
    private String trainTypeAndNo;
    
    /* 车型主键 */
    @Transient
    private String trainTypeIDX;
    
    /* 车号 */
    @Transient
    private String trainNo;
    
    /* 任务单类型,正常整备范围；预警 */
    @Transient
    private String wiClassName;
    
    /* 入段时间 */
    @Transient
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date inTime;
    
    /* 入段时间 */
    @Transient
    private String inTimeStr;
    
    /* 领活时间 */
    @Transient
    private String fetchTimeStr;
    
    /* 车型简称 */
    @Transient
    private String trainTypeShortName;
    
    /* 配属段编码 */
    @Transient
    private String dID;
    
    /* 配属段名称 */
    @Transient
    private String dName;
    
    /* 是否照相核实，1：核实；0：未核实； */
    @Column(name="isCheckPicture")
    private Integer isCheckPicture;
    
    /* 页面核实照片状态 0：未核实不显示颜色；1：核实，颜色红色即未上传附件 2：核实，颜色绿色即已经上传附件；*/
    @Transient
    private Integer checkPictureStatus;
    
    /* 数据项列表 */
    @Transient
    private List<ZbglRdpWidi> zbglRdpWidis = new ArrayList<ZbglRdpWidi>();
    
    
    /**
     * @return String 获取整备单ID
     */
    public String getRdpIDX() {
        return rdpIDX;
    }
    
    /**
     * @param rdpIDX 设置整备单ID
     */
    public void setRdpIDX(String rdpIDX) {
        this.rdpIDX = rdpIDX;
    }
    
    /**
     * @return String 获取任务单类型
     */
    public String getWiClass() {
        return wiClass;
    }
    
    /**
     * @param wiClass 设置任务单类型
     */
    public void setWiClass(String wiClass) {
        this.wiClass = wiClass;
    }
    
    /**
     * @return String 获取作业项目ID
     */
    public String getWiIDX() {
        return wiIDX;
    }
    
    /**
     * @param wiIDX 设置作业项目ID
     */
    public void setWiIDX(String wiIDX) {
        this.wiIDX = wiIDX;
    }
    
    /**
     * @return String 获取任务编号
     */
    public String getWiCode() {
        return wiCode;
    }
    
    /**
     * @param wiCode 设置任务编号
     */
    public void setWiCode(String wiCode) {
        this.wiCode = wiCode;
    }
    
    /**
     * @return String 获取任务名称
     */
    public String getWiName() {
        return wiName;
    }
    
    /**
     * @param wiName 设置任务名称
     */
    public void setWiName(String wiName) {
        this.wiName = wiName;
    }
    
    /**
     * @return String 获取任务描述
     */
    public String getWiDesc() {
        return wiDesc;
    }
    
    /**
     * @param wiDesc 设置任务描述
     */
    public void setWiDesc(String wiDesc) {
        this.wiDesc = wiDesc;
    }
    
    /**
     * @return Long 获取作业班组代码
     */
    public Long getHandleOrgID() {
        return handleOrgID;
    }
    
    /**
     * @param handleOrgID 设置作业班组代码
     */
    public void setHandleOrgID(Long handleOrgID) {
        this.handleOrgID = handleOrgID;
    }
    
    /**
     * @return String 获取作业班组名称
     */
    public String getHandleOrgName() {
        return handleOrgName;
    }
    
    /**
     * @param handleOrgName 设置作业班组名称
     */
    public void setHandleOrgName(String handleOrgName) {
        this.handleOrgName = handleOrgName;
    }
    
    /**
     * @return Long 获取作业人编码
     */
    public Long getHandlePersonID() {
        return handlePersonID;
    }
    
    /**
     * @param handlePersonID 设置作业人编码
     */
    public void setHandlePersonID(Long handlePersonID) {
        this.handlePersonID = handlePersonID;
    }
    
    /**
     * @return String 获取作业人名称
     */
    public String getHandlePersonName() {
        return handlePersonName;
    }
    
    /**
     * @param handlePersonName 设置作业人名称
     */
    public void setHandlePersonName(String handlePersonName) {
        this.handlePersonName = handlePersonName;
    }
    
    /**
     * @return java.util.Date 获取领活时间
     */
    public java.util.Date getFetchTime() {
        return fetchTime;
    }
    
    /**
     * @param fetchTime 设置领活时间
     */
    public void setFetchTime(java.util.Date fetchTime) {
        this.fetchTime = fetchTime;
    }
    
    /**
     * @return java.util.Date 获取销活时间
     */
    public java.util.Date getHandleTime() {
        return handleTime;
    }
    
    /**
     * @param handleTime 设置销活时间
     */
    public void setHandleTime(java.util.Date handleTime) {
        this.handleTime = handleTime;
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
    
    /**
     * @return String 获取任务单状态
     */
    public String getWiStatus() {
        return wiStatus;
    }
    
    /**
     * @param wiStatus 设置任务单状态
     */
    public void setWiStatus(String wiStatus) {
        this.wiStatus = wiStatus;
    }
    
    /**
     * @return Integer 获取记录状态
     */
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    /**
     * @param recordStatus 设置记录状态
     */
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    /**
     * @return java.util.Date 获取最新更新时间
     */
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    
    /**
     * @param updateTime 设置最新更新时间
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
     * @param idx 设置idx主键
     */
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public java.util.Date getInTime() {
        return inTime;
    }
    
    public void setInTime(java.util.Date inTime) {
        this.inTime = inTime;
    }
    
    public String getTrainTypeAndNo() {
        return trainTypeAndNo;
    }
    
    public void setTrainTypeAndNo(String trainTypeAndNo) {
        this.trainTypeAndNo = trainTypeAndNo;
    }
    
    public void setWiClassName(String wiClassName) {
        this.wiClassName = wiClassName;
    }
    
    public String getWiClassName() {
        return wiClassName;
    }
    
    public String getTrainNo() {
        return trainNo;
    }
    
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }
    
    public String getTrainTypeIDX() {
        return trainTypeIDX;
    }
    
    public void setTrainTypeIDX(String trainTypeIDX) {
        this.trainTypeIDX = trainTypeIDX;
    }
    
    public String getFetchTimeStr() {
        return fetchTimeStr;
    }
    
    public void setFetchTimeStr(String fetchTimeStr) {
        this.fetchTimeStr = fetchTimeStr;
    }
    
    public String getInTimeStr() {
        return inTimeStr;
    }
    
    public void setInTimeStr(String inTimeStr) {
        this.inTimeStr = inTimeStr;
    }
    
    public String getDID() {
        return dID;
    }
    
    public void setDID(String did) {
        dID = did;
    }
    
    public String getDName() {
        return dName;
    }
    
    public void setDName(String name) {
        dName = name;
    }
    
    public String getTrainTypeShortName() {
        return trainTypeShortName;
    }
    
    public void setTrainTypeShortName(String trainTypeShortName) {
        this.trainTypeShortName = trainTypeShortName;
    }

    /**
     * <li>说明：获取整备任务单类型的中文含义
     * <li>创建人：程锐
     * <li>创建日期：2015-2-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param wiClass 整备任务单类型
     * @return 整备任务单类型的中文含义
     */
    public static String getWiClassName(String wiClass) {
        if (WICLASS_ZBFW.equals(wiClass)) {
            return WICLASS_ZBFW_CH;
        } else if (WICLASS_YJ.equals(wiClass)) {
            return WICLASS_YJ_CH;
        } else if (WICLASS_ZLCS.equals(wiClass)) {
            return WICLASS_ZLCS_CH;
        }
        return "";
    }
    
    /**
     * <li>说明：默认构造方法
     * <li>创建人：程锐
     * <li>创建日期：2015-2-2
     * <li>修改人：
     * <li>修改日期：
     */
    public ZbglRdpWi() {
    }
    
    /**
     * <li>说明：机车整备任务单列表
     * <li>创建人：程锐
     * <li>创建日期：2015-2-2
     * <li>修改人：
     * <li>修改日期：
     * @param idx 机车整备任务单IDX
     * @param trainTypeAndNo 机车号
     * @param wiClass 整备任务单类型
     * @param wiName 整备任务单名称
     * @param wiDesc 整备任务单描述
     * @param inTime 入段时间
     * @param fetchTime 领活时间
     * @param trainTypeIDX 车型主键
     * @param trainNo 车号
     * @param seqNo 排序号
     * @param trainTypeShortName 车型简称
     * @param dID 配属段ID
     * @param dName 配属段名称
     */
    public ZbglRdpWi(String idx, 
                     String trainTypeAndNo, 
                     String wiClass, 
                     String wiName, 
                     String wiDesc, 
                     Date inTime,
                     Date fetchTime,
                     String trainTypeIDX,
                     String trainNo,
                     int seqNo,
                     String trainTypeShortName,
                     String dID,
                     String dName,
                     String isHg,
                     String worker) {
        super();
        this.idx = idx;
        this.trainTypeAndNo = trainTypeAndNo;
        this.wiClass = wiClass;
        this.wiName = wiName;
        this.wiDesc = wiDesc;
        this.inTime = inTime;
        this.fetchTime = fetchTime;
        this.trainTypeIDX = trainTypeIDX;
        this.trainNo = trainNo;
        this.seqNo = seqNo;
        this.trainTypeShortName = trainTypeShortName;
        this.dID = dID;
        this.dName = dName;
        this.isHg = isHg;
        this.worker = worker ;
    }

    
    public String getIsHg() {
        return isHg;
    }

    
    public void setIsHg(String isHg) {
        this.isHg = isHg;
    }

    
    public String getNodeIDX() {
        return nodeIDX;
    }

    
    public void setNodeIDX(String nodeIDX) {
        this.nodeIDX = nodeIDX;
    }

    
    public String getWorker() {
        return worker;
    }

    
    public void setWorker(String worker) {
        this.worker = worker;
    }

	public Integer getIsCheckPicture() {
		return isCheckPicture;
	}

	public void setIsCheckPicture(Integer isCheckPicture) {
		this.isCheckPicture = isCheckPicture;
	}

	public Integer getCheckPictureStatus() {
		return checkPictureStatus;
	}

	public void setCheckPictureStatus(Integer checkPictureStatus) {
		this.checkPictureStatus = checkPictureStatus;
	}

    
    public List<ZbglRdpWidi> getZbglRdpWidis() {
        return zbglRdpWidis;
    }

    
    public void setZbglRdpWidis(List<ZbglRdpWidi> zbglRdpWidis) {
        this.zbglRdpWidis = zbglRdpWidis;
    }
}