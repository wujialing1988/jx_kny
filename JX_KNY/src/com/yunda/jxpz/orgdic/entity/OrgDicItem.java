package com.yunda.jxpz.orgdic.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：OrgDicItem实体类, 数据表：常用部门字典项
 * <li>创建人：程梅
 * <li>创建日期：2015-09-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
@Entity
@Table(name="JXPZ_ORG_DIC_ITEM")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class OrgDicItem implements java.io.Serializable{
	/** 联合主键 */
    @EmbeddedId
    private OrgDicItemId id;
	
	/* 部门序列 */
	@Column(name="ORG_SEQ")
	private String orgSeq;
	/* 部门名称 */
	@Column(name="ORG_NAME")
	private String orgName;
	/* 机构id，临时字段*/
    @Transient
    private String orgid ;
	/**
	 * @return String 获取部门序列
	 */
	public String getOrgSeq(){
		return orgSeq;
	}
	/**
	 * @param orgSeq 设置部门序列
	 */
	public void setOrgSeq(String orgSeq) {
		this.orgSeq = orgSeq;
	}
	/**
	 * @return String 获取部门名称
	 */
	public String getOrgName(){
		return orgName;
	}
	/**
	 * @param orgName 设置部门名称
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
    
    public OrgDicItemId getId() {
        return id;
    }
    
    public void setId(OrgDicItemId id) {
        this.id = id;
    }
    
    public String getOrgid() {
        return orgid;
    }
    
    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }
    /**
     * <li>标题: 机车检修管理信息系统
     * <li>说明：常用部门字典项实体联合主键
     * <li>创建人：程梅
     * <li>创建日期：2015-09-28
     * <li>修改人: 
     * <li>修改日期：
     * <li>修改内容：
     * <li>版权: Copyright (c) 2008 运达科技公司
     * @author 测控部检修系统项目组
     * @version 1.0
     */
    @Embeddable
    @SuppressWarnings("serial")
    public static class OrgDicItemId implements Serializable {

        /* 字典编码 */
        @Column(name="DICT_TYPE_ID")
        private String dictTypeId;
        /* 部门ID */
        @Column(name="ORG_ID")
        private String orgId;
        
        public String getDictTypeId() {
            return dictTypeId;
        }
        public void setDictTypeId(String dictTypeId) {
            this.dictTypeId = dictTypeId;
        }
        public String getOrgId() {
            return orgId;
        }
        public void setOrgId(String orgId) {
            this.orgId = orgId;
        }
        
    }
}