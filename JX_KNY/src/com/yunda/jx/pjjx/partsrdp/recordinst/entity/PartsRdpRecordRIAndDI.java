package com.yunda.jx.pjjx.partsrdp.recordinst.entity;

import java.util.List;

import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordRIManager.RecordDIBean;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 配件检修检测项及数据项的封装对象
 * <li>创建人：程锐
 * <li>创建日期：2016-2-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.3
 */
public class PartsRdpRecordRIAndDI implements java.io.Serializable{
	
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	private String idx;
	private String rdpRecordCardIDX;
	private String repairItemNo;
	private String repairItemName;
	private String repairStandard;
	private String repairResult;
	private Integer seqNo;
	private String status;	
	
	private List<RecordDIBean> diList;
	List<String> resultList;
	private String remarks;
	private String handleResult;
	
	private Boolean hasDI;//是否有数据项
	public List<RecordDIBean> getDiList() {
		return diList;
	}
	public void setDiList(List<RecordDIBean> diList) {
		this.diList = diList;
	}
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getRdpRecordCardIDX() {
		return rdpRecordCardIDX;
	}
	public void setRdpRecordCardIDX(String rdpRecordCardIDX) {
		this.rdpRecordCardIDX = rdpRecordCardIDX;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getRepairItemName() {
		return repairItemName;
	}
	public void setRepairItemName(String repairItemName) {
		this.repairItemName = repairItemName;
	}
	public String getRepairItemNo() {
		return repairItemNo;
	}
	public void setRepairItemNo(String repairItemNo) {
		this.repairItemNo = repairItemNo;
	}
	public String getRepairResult() {
		return repairResult;
	}
	public void setRepairResult(String repairResult) {
		this.repairResult = repairResult;
	}
	public String getRepairStandard() {
		return repairStandard;
	}
	public void setRepairStandard(String repairStandard) {
		this.repairStandard = repairStandard;
	}
	public Integer getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
	public List<String> getResultList() {
		return resultList;
	}
	public void setResultList(List<String> resultList) {
		this.resultList = resultList;
	}
	public Boolean getHasDI() {
		return hasDI;
	}
	public void setHasDI(Boolean hasDI) {
		this.hasDI = hasDI;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
    public String getHandleResult() {
        return handleResult;
    }
    
    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }
}
