package com.yunda.jcbm.jcxtfl.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JcxtflBuild实体类, 数据表：机车系统分类
 * <li>创建人：王利成
 * <li>创建日期：2016-5-16
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class JczcBuildBean implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
  
    /* idx主键 */
	@Id
    @Column(name = "CO_ID")
    private String coID;
    
    /* 父节点ID */
    @Column(name = "FJD_ID")
    private String fjdID;
    

    
    /* 分类编码 */
    @Column(name = "FLBM")
    private String flbm;
    
    /* 分类名称 */
    @Column(name = "FLMC")
    private String flmc;
   
    /* 拼音简称 */
    @Column(name = "PYJC")
    private String pyjc;
    
    /* 是否有子节点 */
    @Column(name = "CO_HASCHILD")
    private Integer coHaschild;
 
    /* 顺序号 */
    @Column(name="Seq_No")
    private Integer seqNo;
    
    
    public Integer getSeqNo() {
        return seqNo;
    }


    
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }


    public Integer getCoHaschild() {
        return coHaschild;
    }

    
    public void setCoHaschild(Integer coHaschild) {
        this.coHaschild = coHaschild;
    }

    public String getFjdID() {
        return fjdID;
    }
    
    public void setFjdID(String fjdID) {
        this.fjdID = fjdID;
    }
    
    public String getFlbm() {
        return flbm;
    }
    
    public void setFlbm(String flbm) {
        this.flbm = flbm;
    }
    
    public String getFlmc() {
        return flmc;
    }
    
    public void setFlmc(String flmc) {
        this.flmc = flmc;
    }
    
    public String getCoID() {
        return coID;
    }
    
    public void setCoID(String coID) {
        this.coID = coID;
    }
    
  
    
    public String getPyjc() {
        return pyjc;
    }
    
    public void setPyjc(String pyjc) {
        this.pyjc = pyjc;
    }
    
  
 
}
