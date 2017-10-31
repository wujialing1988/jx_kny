package com.yunda.twt.twtinfo.entity;

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
 * <li>说明：TrainStatusColors实体类, 数据表：机车状态颜色
 * <li>创建人：程锐
 * <li>创建日期：2015-01-20
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "TWT_Train_Status_Colors")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TrainStatusColors implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 状态名称 */
    private String status;
    
    /* 颜色的RGB */
    private String color;
    
    /* 排序号 */
    @Column(name = "Seq_No")
    private Integer seqNo;
    
    /**
     * @return String 获取状态名称
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * @param status 设置状态名称
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * @return String 获取颜色
     */
    public String getColor() {
        return color;
    }
    
    /**
     * @param color 设置颜色
     */
    public void setColor(String color) {
        this.color = color;
    }
    
    /**
     * @return Integer 获取排序号
     */
    public Integer getSeqNo() {
        return seqNo;
    }
    
    /**
     * @param seqNo 设置排序号
     */
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
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
}
