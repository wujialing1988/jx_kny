package com.yunda.freight.base.classTransfer.entity;

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
 * <li>说明: 交接明细实体
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-04-18 11:38:53
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "K_CLASS_TRANSFER_DETAILS")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ClassTransferDetails implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
            
    /* 交接主表id */ 
    @Column(name = "TRANSFER_IDX")
    private java.lang.String transferIdx;            
            
    /* 交接项目 */ 
    @Column(name = "TRANSFER_ITEM")
    private java.lang.String transferItem;            
            
    /* 交接项目类型 */ 
    @Column(name = "TRANSFER_TYPE")
    private java.lang.String transferType;            
            
    /* 交接项目内容 */ 
    @Column(name = "TRANSFER_CONTENT")
    private java.lang.String transferContent;            
    
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public java.lang.String getTransferIdx() {
        return this.transferIdx;
    }
    
    public void setTransferIdx(java.lang.String value) {
        this.transferIdx = value;
    }
    public java.lang.String getTransferItem() {
        return this.transferItem;
    }
    
    public void setTransferItem(java.lang.String value) {
        this.transferItem = value;
    }
    public java.lang.String getTransferType() {
        return this.transferType;
    }
    
    public void setTransferType(java.lang.String value) {
        this.transferType = value;
    }
    public java.lang.String getTransferContent() {
        return this.transferContent;
    }
    
    public void setTransferContent(java.lang.String value) {
        this.transferContent = value;
    }
    
}

