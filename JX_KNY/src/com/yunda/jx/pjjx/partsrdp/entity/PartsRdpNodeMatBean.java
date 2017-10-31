package com.yunda.jx.pjjx.partsrdp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpNodeMatBean实体类, 数据表：物料实例
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-9-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsRdpNodeMatBean implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@Id
	private String idx;
	/* 作业主键 */
	@Column(name="Rdp_IDX")
	private String rdpIDX;
	/* 检修流程节点主键 */
	@Column(name="Rdp_Node_IDX")
	private String rdpNodeIDX;
	/* 物料编码 */
	@Column(name="Mat_Code")
	private String matCode;
	/* 物料描述 */
	@Column(name="Mat_Desc")
	private String matDesc;
	/* 单位 */
    @Column(name="unit")
	private String unit;
	/* 单价 */
    @Column(name="price")
	private Double price;
	/* 额定数量 */
	@Column(name="Default_Qty")
	private Long numberRated;
	
    @Column(name="qty")
    private Double qty;

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public String getMatCode() {
        return matCode;
    }

    
    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }

    
    public String getMatDesc() {
        return matDesc;
    }

    
    public void setMatDesc(String matDesc) {
        this.matDesc = matDesc;
    }

    
    public Long getNumberRated() {
        return numberRated;
    }

    
    public void setNumberRated(Long numberRated) {
        this.numberRated = numberRated;
    }

    
    public Double getPrice() {
        return price;
    }

    
    public void setPrice(Double price) {
        this.price = price;
    }

    
    public Double getQty() {
        return qty;
    }

    
    public void setQty(Double qty) {
        this.qty = qty;
    }

    
    public String getRdpIDX() {
        return rdpIDX;
    }

    
    public void setRdpIDX(String rdpIDX) {
        this.rdpIDX = rdpIDX;
    }

    
    public String getRdpNodeIDX() {
        return rdpNodeIDX;
    }

    
    public void setRdpNodeIDX(String rdpNodeIDX) {
        this.rdpNodeIDX = rdpNodeIDX;
    }

    
    public String getUnit() {
        return unit;
    }

    
    public void setUnit(String unit) {
        this.unit = unit;
    }
}