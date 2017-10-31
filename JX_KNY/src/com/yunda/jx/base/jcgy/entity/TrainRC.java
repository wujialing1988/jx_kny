package com.yunda.jx.base.jcgy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：视图v_jczl_train_rc的实体类,车型对应的修程
 * <li>创建人：程梅
 * <li>创建日期：2012-10-31
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */

@Entity
@Table(name="v_jczl_train_rc")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class TrainRC implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
    /* 修程编码 */
    @Id
    @Column(name="XC_ID")
    private String xcID;
    /* 车型主键 */
    @Column(name="Train_Type_Idx")
    private String TrainTypeIdx;
	/* 修程名称 */
	@Column(name="XC_NAME")
	private String xcName;
	/* 修程类型 */
	@Column(name="xc_type")
	private String xcType;
	/* 车型承修单位主键 */
	@Column(name="Undertake_ORGID")
	private Long undertakeOrgId;
    
    public String getXcID() {
        return xcID;
    }

    public void setXcID(String xcID) {
        this.xcID = xcID;
    }
    
    public String getXcName() {
        return xcName;
    }

    public void setXcName(String xcName) {
        this.xcName = xcName;
    }

    
    public String getTrainTypeIdx() {
        return TrainTypeIdx;
    }
    
    public void setTrainTypeIdx(String trainTypeIdx) {
        TrainTypeIdx = trainTypeIdx;
    }

	public Long getUndertakeOrgId() {
		return undertakeOrgId;
	}

	public void setUndertakeOrgId(Long undertakeOrgId) {
		this.undertakeOrgId = undertakeOrgId;
	}

	public String getXcType() {
		return xcType;
	}

	public void setXcType(String xcType) {
		this.xcType = xcType;
	}
}