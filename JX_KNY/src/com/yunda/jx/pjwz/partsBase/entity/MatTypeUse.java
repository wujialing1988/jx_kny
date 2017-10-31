package com.yunda.jx.pjwz.partsBase.entity;

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
 * <li>说明: 物料使用表
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-7-4
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "PJWZ_MAT_TYPE_USE")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class MatTypeUse implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;    
    
    /* 物料编码 */
    @Column(name = "MAT_CODE")
    private String matCode;
    
    /* 物料描述 */
    @Column(name = "MAT_DESC")
    private String matDesc;
    
    /* 消耗数量 */
    @Column(name = "MAT_COUNT")
    private Integer matCount ;
    
    /* 物料描述首拼 */
    @Column(name = "MAT_DESC_EN")
    private String matDescEn;
    
    /* 计量单位 */
    private String unit;
    
    /* 计划单价 */
    private Double price;

    /* 故障登记 */
    @Column(name = "GZTP_IDX")
    private String gztpIdx ;
    
    public MatTypeUse() {
        super();
    }
    
    /**
     * @param matCode 物料编码
     * @param matDesc 物料描述
     * @param unit 计量单位
     * @param price 单据
     * @param qty 库存数量
     */
    public MatTypeUse(String matCode, String matDesc, String unit, Double price, Integer qty) {
        super();
        this.matCode = matCode;
        this.matDesc = matDesc;
        this.unit = unit;
        this.price = price;
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

    
    public String getGztpIdx() {
        return gztpIdx;
    }

    
    public void setGztpIdx(String gztpIdx) {
        this.gztpIdx = gztpIdx;
    }

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public Integer getMatCount() {
        return matCount;
    }

    
    public void setMatCount(Integer matCount) {
        this.matCount = matCount;
    }
    
    
    
}
