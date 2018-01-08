package com.yunda.freight.zb.detain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 扣车提票信息
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-04-20 17:26:28
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "K_DETAIN_GZTP")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class DetainGztp implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
            
    /* 车辆扣车ID */ 
    @Column(name = "DETAIN_IDX")
    private java.lang.String detainIdx;            
            
    /* 故障名称 */ 
    @Column(name = "GZTP_NAME")
    private java.lang.String gztpName;            
            
    /* 故障描述 */ 
    @Column(name = "GZTP_DESC")
    private java.lang.String gztpDesc;

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public java.lang.String getDetainIdx() {
		return detainIdx;
	}

	public void setDetainIdx(java.lang.String detainIdx) {
		this.detainIdx = detainIdx;
	}

	public java.lang.String getGztpName() {
		return gztpName;
	}

	public void setGztpName(java.lang.String gztpName) {
		this.gztpName = gztpName;
	}

	public java.lang.String getGztpDesc() {
		return gztpDesc;
	}

	public void setGztpDesc(java.lang.String gztpDesc) {
		this.gztpDesc = gztpDesc;
	}           
           
}

