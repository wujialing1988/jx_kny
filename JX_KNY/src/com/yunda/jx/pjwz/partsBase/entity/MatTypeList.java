package com.yunda.jx.pjwz.partsBase.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatTypeList实体类, 数据表：物料清单
 * <li>创建人：程锐
 * <li>创建日期：2013-10-22
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "PJWZ_MAT_TYPE_LIST")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class MatTypeList implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* 物料编码 */
    @Id
    @Column(name = "MAT_CODE")
    private String matCode;
    
    /* 物料描述 */
    @Column(name = "MAT_DESC")
    private String matDesc;
    
    /* 物料描述首拼 */
    @Column(name = "MAT_DESC_EN")
    private String matDescEn;
    
    /* 计量单位 */
    private String unit;
    
    /* 计划单价 */
    private Double price;
    
    @Transient
    /** 库存数量 */
    private Integer qty;
    
    @Transient
    private String rdpNodeStr;
    
    public MatTypeList() {
        super();
    }
    
    /**
     * @param matCode 物料编码
     * @param matDesc 物料描述
     * @param unit 计量单位
     * @param price 单据
     * @param qty 库存数量
     */
    public MatTypeList(String matCode, String matDesc, String unit, Double price, Integer qty) {
        super();
        this.matCode = matCode;
        this.matDesc = matDesc;
        this.unit = unit;
        this.price = price;
        this.qty = qty;
    }
    
    /**
     * @return 获取库存数量
     */
    public Integer getQty() {
        return qty;
    }
    
    /**
     * @param qty 设置库存数量
     */
    public void setQty(Integer qty) {
        this.qty = qty;
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
    
    public String getMatDescEn() {
        return matDescEn;
    }

    public void setMatDescEn(String matDescEn) {
        this.matDescEn = matDescEn;
    }

    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }

    
    public String getRdpNodeStr() {
        return rdpNodeStr;
    }

    
    public void setRdpNodeStr(String rdpNodeStr) {
        this.rdpNodeStr = rdpNodeStr;
    }
    
}
