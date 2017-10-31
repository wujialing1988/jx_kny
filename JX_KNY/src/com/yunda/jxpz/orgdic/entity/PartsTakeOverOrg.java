package com.yunda.jxpz.orgdic.entity;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsTakeOverOrg实体类, 数据表：接收部门装饰类
 * <li>创建人：程梅
 * <li>创建日期：2016年6月2日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class PartsTakeOverOrg implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    /* 接收部门id*/
	private String orgId;
	/* 接收部门名称 */
	private String orgName;
    /* 接收部门序列号 */
    private String orgSeq;
    /* 接收部门类型（1--机构；2--库房） */
    private Integer orgType;
    
    public String getOrgId() {
        return orgId;
    }
    
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
    
    public String getOrgName() {
        return orgName;
    }
    
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
    
    public String getOrgSeq() {
        return orgSeq;
    }
    
    public void setOrgSeq(String orgSeq) {
        this.orgSeq = orgSeq;
    }
    
    public Integer getOrgType() {
        return orgType;
    }
    
    public void setOrgType(Integer orgType) {
        this.orgType = orgType;
    }

}