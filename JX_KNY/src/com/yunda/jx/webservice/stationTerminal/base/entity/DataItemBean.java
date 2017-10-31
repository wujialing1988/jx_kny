package com.yunda.jx.webservice.stationTerminal.base.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于获取数据项实体包装
 * <li>说明: 用于dataItem接口方法
 * <li>创建人：王治龙
 * <li>创建日期：2013-12-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataItemBean implements java.io.Serializable{
    @Id
	private String idx ; //检测项idx
	private String detectItemContent ; //检测内容
	private Integer isNotBlank ; //是否必填（0,1）
	private String detectResulttype ; //数据类型
	private String detectResult ; //检测结果
	private String detectItemStandard ; //检测项标准
	private String detectItemCode ; //检测项标准

    /* 最小范围值 */
    private Double minResult;
    /* 最大范围值 */
    private Double maxResult;
    // 无参构造函数
    public DataItemBean(){super();}
    public DataItemBean(String idx, String detectItemContent, Integer isNotBlank, String detectResulttype, String detectResult, String detectItemStandard, Double minResult,Double maxResult ){
        this.idx = idx;
        this.detectItemContent = detectItemContent;
        this.isNotBlank = isNotBlank;
        this.detectResulttype = detectResulttype;
        this.detectResult = detectResult;
        this.detectItemStandard = detectItemStandard;
        this.minResult = minResult;
        this.maxResult = maxResult;
    }
	public String getDetectItemStandard() {
		return detectItemStandard;
	}
	public void setDetectItemStandard(String detectItemStandard) {
		this.detectItemStandard = detectItemStandard;
	}
	public String getDetectItemContent() {
		return detectItemContent;
	}
	public void setDetectItemContent(String detectItemContent) {
		this.detectItemContent = detectItemContent;
	}
	public String getDetectResult() {
		return detectResult;
	}
	public void setDetectResult(String detectResult) {
		this.detectResult = detectResult;
	}
	public String getDetectResulttype() {
		return detectResulttype;
	}
	public void setDetectResulttype(String detectResulttype) {
		this.detectResulttype = detectResulttype;
	}
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	
	public Integer getIsNotBlank() {
		return isNotBlank;
	}
	public void setIsNotBlank(Integer isNotBlank) {
		this.isNotBlank = isNotBlank;
	}
	public Double getMaxResult() {
		return maxResult;
	}
	public void setMaxResult(Double maxResult) {
		this.maxResult = maxResult;
	}
	public Double getMinResult() {
		return minResult;
	}
	public void setMinResult(Double minResult) {
		this.minResult = minResult;
	}
    
    public String getDetectItemCode() {
        return detectItemCode;
    }
    
    public void setDetectItemCode(String detectItemCode) {
        this.detectItemCode = detectItemCode;
    }
	
}
