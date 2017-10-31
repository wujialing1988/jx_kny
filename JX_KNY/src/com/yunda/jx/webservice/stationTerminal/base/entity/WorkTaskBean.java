package com.yunda.jx.webservice.stationTerminal.base.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：作业任务信息
 * <li>说明: 用于方法workTask
 * <li>创建人：王治龙
 * <li>创建日期：2013-12-20
 * <li>修改人: 王斌
 * <li>修改日期：2014-1-3	
 * <li>修改内容：增加字段mutualCheckPerson、checkPerson、spotCheckPerson
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@SuppressWarnings("serial")
public class WorkTaskBean implements java.io.Serializable{
    @Id
	private String idx ;  //作业任务主键

	private String workTaskName ; //检测项名称
    
	private String repairStandard ; //技术要求或标准规定

	private String workStepIDX ; //检测结果idx

	private String repairResult ; //结果
    
    private String remarks ; //备注
      @Transient
	private String mutualCheckPerson;//互检员
      @Transient
	private String checkPerson;//检查员
      @Transient
	private String spotCheckPerson;//抽检
    
    //新增字段，查询返回
    @Transient
    private List<DataItemBean> dataItemList;//检测检修List列表
    @Transient
    List<String> resultList;
    @Transient
    private String repairContent;//检修内容
    @Transient
    private String repairMethod;//检修方法
    @Transient
    private String status;//状态:待领取、待处理、已处理、终止（来源于数据字典）
    @Transient
    private String workTaskCode;//作业任务编码
    
    public List<String> getResultList() {
        return resultList;
    }
    
    public void setResultList(List<String> resultList) {
        this.resultList = resultList;
    }
    // 无参构造函数
    public WorkTaskBean(){super();}
    public WorkTaskBean(String idx, String workTaskName, String repairStandard, String repairResult){
        this.idx = idx;
        this.workTaskName = workTaskName;
        this.repairStandard = repairStandard;
        this.repairResult = repairResult;
    }
    
    public String getRepairContent() {
        return repairContent;
    }


    
    public void setRepairContent(String repairContent) {
        this.repairContent = repairContent;
    }


    
    public String getRepairMethod() {
        return repairMethod;
    }


    
    public void setRepairMethod(String repairMethod) {
        this.repairMethod = repairMethod;
    }


    
    public String getStatus() {
        return status;
    }


    
    public void setStatus(String status) {
        this.status = status;
    }


    
    public String getWorkTaskCode() {
        return workTaskCode;
    }


    
    public void setWorkTaskCode(String workTaskCode) {
        this.workTaskCode = workTaskCode;
    }


    public List<DataItemBean> getDataItemList() {
        return dataItemList;
    }

    
    public void setDataItemList(List<DataItemBean> dataItemList) {
        this.dataItemList = dataItemList;
    }

    public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public String getWorkStepIDX() {
		return workStepIDX;
	}

	public void setWorkStepIDX(String workStepIDX) {
		this.workStepIDX = workStepIDX;
	}

	public String getWorkTaskName() {
		return workTaskName;
	}

	public void setWorkTaskName(String workTaskName) {
		this.workTaskName = workTaskName;
	}

	public String getCheckPerson() {
		return checkPerson;
	}

	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
	}

	public String getMutualCheckPerson() {
		return mutualCheckPerson;
	}

	public void setMutualCheckPerson(String mutualCheckPerson) {
		this.mutualCheckPerson = mutualCheckPerson;
	}

	public String getSpotCheckPerson() {
		return spotCheckPerson;
	}

	public void setSpotCheckPerson(String spotCheckPerson) {
		this.spotCheckPerson = spotCheckPerson;
	}
	
}
