package com.yunda.zb.tp.webservice;

import java.io.Serializable;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 临碎修提票bean
 * <li>创建人：程锐
 * <li>创建日期：2015-1-27
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ZbglTpBean implements Serializable {
    
    /* idx主键 */
    private String idx;
    
    /* 机车号 */
    private String trainTypeAndNo;
    
    /* 故障部件名称 */
    private String faultFixFullName;
    
    /* 故障现象 */
    private String faultName;
    
    /* 故障描述 */
    private String faultDesc;
    
    /* 提票单号 */
    private String faultNoticeCode;
    
    /* 提票人名称 */
    private String noticePersonName;
    
    /* 提票时间 */
    private String noticeTime;
    
    /* 专业类型ID */
    private String professionalTypeIdx;
    
    /* 专业类型名称 */
    private String professionalTypeName;
    
    /* 专业类型序列 */
    private String professionalTypeSeq;
    
    /* 录音文件idx */
    private String audioAttIdx;
    
    /* 发现人 */
    private Long discoverID;
    
    /* 发现人名称 */
    private String discover;
    
    /* 故障ID */
    private String faultID;

    /* 系统分类编码 */
    private String faultFixFullCode;
    
    //返修次数
    private Integer repairTimes;
    
    //施修方法
    private String methodDesc;
    
    //处理结果
    private Integer repairResult;
    
    //人员
    private String repairEmp;
    
    //人员id
    private String repairEmpID;
    
    //故障原因
    private String faultReason;
    
    //处理描述
    private String repairDesc;
    
    // 是否是遗留活
    private Boolean isTpException;
    
    
    
    public String getFaultFixFullCode() {
        return faultFixFullCode;
    }

    public void setFaultFixFullCode(String faultFixFullCode) {
        this.faultFixFullCode = faultFixFullCode;
    }


    
    public String getFaultID() {
        return faultID;
    }


    
    public void setFaultID(String faultID) {
        this.faultID = faultID;
    }


    public String getDiscover() {
        return discover;
    }

    
    public void setDiscover(String discover) {
        this.discover = discover;
    }

    
    public Long getDiscoverID() {
        return discoverID;
    }

    
    public void setDiscoverID(Long discoverID) {
        this.discoverID = discoverID;
    }

    public String getFaultDesc() {
        return faultDesc;
    }
    
    public void setFaultDesc(String faultDesc) {
        this.faultDesc = faultDesc;
    }
    
    public String getFaultFixFullName() {
        return faultFixFullName;
    }
    
    public void setFaultFixFullName(String faultFixFullName) {
        this.faultFixFullName = faultFixFullName;
    }
    
    public String getFaultName() {
        return faultName;
    }
    
    public void setFaultName(String faultName) {
        this.faultName = faultName;
    }
    
    public String getFaultNoticeCode() {
        return faultNoticeCode;
    }
    
    public void setFaultNoticeCode(String faultNoticeCode) {
        this.faultNoticeCode = faultNoticeCode;
    }
    
    public String getTrainTypeAndNo() {
        return trainTypeAndNo;
    }
    
    public void setTrainTypeAndNo(String trainTypeAndNo) {
        this.trainTypeAndNo = trainTypeAndNo;
    }
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getNoticePersonName() {
        return noticePersonName;
    }
    
    public void setNoticePersonName(String noticePersonName) {
        this.noticePersonName = noticePersonName;
    }
    
    public String getNoticeTime() {
        return noticeTime;
    }
    
    public void setNoticeTime(String noticeTime) {
        this.noticeTime = noticeTime;
    }
    
    public String getProfessionalTypeIdx() {
        return professionalTypeIdx;
    }
    
    public void setProfessionalTypeIdx(String professionalTypeIdx) {
        this.professionalTypeIdx = professionalTypeIdx;
    }
    
    public String getProfessionalTypeName() {
        return professionalTypeName;
    }
    
    public void setProfessionalTypeName(String professionalTypeName) {
        this.professionalTypeName = professionalTypeName;
    }
    
    public String getProfessionalTypeSeq() {
        return professionalTypeSeq;
    }
    
    public void setProfessionalTypeSeq(String professionalTypeSeq) {
        this.professionalTypeSeq = professionalTypeSeq;
    }

    
    public String getAudioAttIdx() {
        return audioAttIdx;
    }

    
    public void setAudioAttIdx(String audioAttIdx) {
        this.audioAttIdx = audioAttIdx;
    }

    public Integer getRepairTimes() {
        return repairTimes;
    }
    
    public void setRepairTimes(Integer repairTimes) {
        this.repairTimes = repairTimes;
    }

	public String getFaultReason() {
		return faultReason;
	}

	public void setFaultReason(String faultReason) {
		this.faultReason = faultReason;
	}

	public String getMethodDesc() {
		return methodDesc;
	}

	public void setMethodDesc(String methodDesc) {
		this.methodDesc = methodDesc;
	}

	public String getRepairDesc() {
		return repairDesc;
	}

	public void setRepairDesc(String repairDesc) {
		this.repairDesc = repairDesc;
	}

	public String getRepairEmp() {
		return repairEmp;
	}

	public void setRepairEmp(String repairEmp) {
		this.repairEmp = repairEmp;
	}

	public String getRepairEmpID() {
		return repairEmpID;
	}

	public void setRepairEmpID(String repairEmpID) {
		this.repairEmpID = repairEmpID;
	}

	public Integer getRepairResult() {
		return repairResult;
	}

	public void setRepairResult(Integer repairResult) {
		this.repairResult = repairResult;
	}

    
    public Boolean getIsTpException() {
        return isTpException;
    }

    
    public void setIsTpException(Boolean isTpException) {
        this.isTpException = isTpException;
    }
    
}
