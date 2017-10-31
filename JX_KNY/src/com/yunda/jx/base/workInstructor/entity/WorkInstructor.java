package com.yunda.jx.base.workInstructor.entity;

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
 * <li>说明: 作业指导书实体
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-05-15 14:10:03
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "K_WORK_INSTRUCTOR")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class WorkInstructor implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
            
    /* 标题名 */ 
    @Column(name = "TITLE")
    private java.lang.String title;            
            
    /* 内容 */ 
    @Column(name = "CONTENT")
    private java.lang.String content;            
            
    /* 开始页码 */ 
    @Column(name = "START_PAGE")
    private java.lang.Long startPage;            
            
    /* 结束页面 */ 
    @Column(name = "END_PAGE")
    private java.lang.Long endPage;            
            
    /* 排序 */ 
    @Column(name = "SEQ_NO")
    private Integer seqNo;            

    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public java.lang.String getTitle() {
        return this.title;
    }
    
    public void setTitle(java.lang.String value) {
        this.title = value;
    }
    public java.lang.String getContent() {
        return this.content;
    }
    
    public void setContent(java.lang.String value) {
        this.content = value;
    }
    public java.lang.Long getStartPage() {
        return this.startPage;
    }
    
    public void setStartPage(java.lang.Long value) {
        this.startPage = value;
    }
    public java.lang.Long getEndPage() {
        return this.endPage;
    }
    
    public void setEndPage(java.lang.Long value) {
        this.endPage = value;
    }
    public Integer getSeqNo() {
        return this.seqNo;
    }
    
    public void setSeqNo(Integer value) {
        this.seqNo = value;
    }
    
}

