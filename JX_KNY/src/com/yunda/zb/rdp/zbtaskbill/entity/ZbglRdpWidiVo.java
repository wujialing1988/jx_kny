package com.yunda.zb.rdp.zbtaskbill.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

import com.yunda.frame.util.DateUtil;

@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglRdpWidiVo implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @Id
    private String idx;
    
    /* 节点ID */
    private String nodeIdx;
    
    /* 节点名称 */
    private String nodeName;
    
    /* 任务单ID */
    private String wiIdx;
    
    /* 任务单名称 */
    private String wiName;
    
    /* 数据项编号 */
    private String diCode;
    
    /* 数据项名称 */
    private String diName;
    
    /* 数据项标准 */
    private String diStandard;
    
    /* 数据类型，字符；数字； */
    private String diClass;
    
    /* 是否必填，是；否； */
    private String isBlank;
    
    /* 顺序号 */
    private Integer seqNo;
    
    /* 数据项结果 */
    private String diResult;
    
    /* 任务状态 */
    private String wiStatus;
    
    /* 作业人编码 */
    private Long handlePersonID;
    
    /* 作业人名称 */
    private String handlePersonName;
    
    /* 是否合格 */
    private String isHg;
    
    /* 销活时间 */
    private String handleTime;
    
    
    /**
     * <li>说明：默认构造方法
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-6
     * <li>修改人： 
     * <li>修改日期：
     */
    public ZbglRdpWidiVo() {
        
    }
    
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-6
     * <li>修改人： 
     * <li>修改日期：
     * @param idx 数据项ID
     * @param diCode 数据项编码
     * @param diName 数据项名称
     * @param diStandard 数据项标准
     * @param diClass 数据类型
     * @param isBlank 是否可为空
     * @param seqNo 序号
     * @param diResult 结果
     * @param wiIdx 作业项目ID
     * @param wiName 作业向名称
     * @param handlePersonID 处理人员ID
     * @param handlePersonName 处理人员名称
     * @param wiStatus 状态
     * @param nodeIdx 节点ID
     * @param nodeName 节点名称
     * @param isHg 是否合格
     * @param handleTime 处理时间
     */
    public ZbglRdpWidiVo(String idx,String diCode,String diName,
                         String diStandard,String diClass,String isBlank,
                         Integer seqNo,String diResult,String wiIdx,String wiName,
                         Long handlePersonID,String handlePersonName,
                         String wiStatus , String isHg,Date handleTime,
                         String nodeIdx,String nodeName) {
        this.idx = idx ;
        this.diCode = diCode ;
        this.diName = diName ;
        this.diStandard = diStandard ;
        this.diClass = diClass ;
        this.isBlank = isBlank ;
        this.seqNo = seqNo ;
        this.diResult = diResult ;
        this.wiIdx = wiIdx ;
        this.wiName = wiName ;
        this.nodeIdx = nodeIdx ;
        this.nodeName = nodeName ;
        String wiStatusName = wiStatus.equals(ZbglRdpWi.STATUS_HANDLED) ? "已处理" : "待处理" ;
        this.wiStatus = wiStatusName ;
        this.handlePersonID = handlePersonID ;
        this.handlePersonName = handlePersonName ;
        this.isHg = isHg ;
        this.handleTime = handleTime == null ? "" : DateUtil.date2String(DateUtil.yyyy_MM_dd_HH_mm, handleTime) ;
    }

    
    public String getDiClass() {
        return diClass;
    }

    
    public void setDiClass(String diClass) {
        this.diClass = diClass;
    }

    
    public String getDiCode() {
        return diCode;
    }

    
    public void setDiCode(String diCode) {
        this.diCode = diCode;
    }

    
    public String getDiName() {
        return diName;
    }

    
    public void setDiName(String diName) {
        this.diName = diName;
    }

    
    public String getDiResult() {
        return diResult;
    }

    
    public void setDiResult(String diResult) {
        this.diResult = diResult;
    }

    
    public String getDiStandard() {
        return diStandard;
    }

    
    public void setDiStandard(String diStandard) {
        this.diStandard = diStandard;
    }

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public String getIsBlank() {
        return isBlank;
    }

    
    public void setIsBlank(String isBlank) {
        this.isBlank = isBlank;
    }

    
    public String getNodeIdx() {
        return nodeIdx;
    }

    
    public void setNodeIdx(String nodeIdx) {
        this.nodeIdx = nodeIdx;
    }

    
    public String getNodeName() {
        return nodeName;
    }

    
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    
    public Integer getSeqNo() {
        return seqNo;
    }

    
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    
    public String getWiIdx() {
        return wiIdx;
    }

    
    public void setWiIdx(String wiIdx) {
        this.wiIdx = wiIdx;
    }

    
    public String getWiName() {
        return wiName;
    }

    
    public void setWiName(String wiName) {
        this.wiName = wiName;
    }

    
    public Long getHandlePersonID() {
        return handlePersonID;
    }

    
    public void setHandlePersonID(Long handlePersonID) {
        this.handlePersonID = handlePersonID;
    }

    
    public String getHandlePersonName() {
        return handlePersonName;
    }

    
    public void setHandlePersonName(String handlePersonName) {
        this.handlePersonName = handlePersonName;
    }

    
    public String getWiStatus() {
        return wiStatus;
    }

    
    public void setWiStatus(String wiStatus) {
        this.wiStatus = wiStatus;
    }

    
    public String getHandleTime() {
        return handleTime;
    }

    
    public void setHandleTime(String handleTime) {
        this.handleTime = handleTime;
    }

    
    public String getIsHg() {
        return isHg;
    }

    
    public void setIsHg(String isHg) {
        this.isHg = isHg;
    }
  
}
