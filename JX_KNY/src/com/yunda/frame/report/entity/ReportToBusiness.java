package com.yunda.frame.report.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: ReportToBusines实体类, 数据表：报表业务关联
 * <li>创建人：何涛
 * <li>创建日期：2015-2-5
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="R_Report_To_Business")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ReportToBusiness implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 报表打印模板主键 */
	@Column(name="Printer_Module_IDX")
	private String printerModuleIDX;
	/* 业务主键 */
	@Column(name="Business_IDX")
	private String businessIDX;
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/**
	 * @return 获取表示此条记录的状态
	 */
	public Integer getRecordStatus() {
		return recordStatus;
	}
	/**
	 * @param recordStatus 设置表示此条记录的状态
	 */
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
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
     * @return 获取业务主键
     */
    public String getBusinessIDX() {
        return businessIDX;
    }
    /**
     * @param businessIDX 设置业务主键
     */
    public void setBusinessIDX(String businessIDX) {
        this.businessIDX = businessIDX;
    }
    /**
     * @return 获取打印模板主键
     */
    public String getPrinterModuleIDX() {
        return printerModuleIDX;
    }
    /**
     * @param printerModuleIDX 设置打印模板主键
     */
    public void setPrinterModuleIDX(String printerModuleIDX) {
        this.printerModuleIDX = printerModuleIDX;
    }
	
}