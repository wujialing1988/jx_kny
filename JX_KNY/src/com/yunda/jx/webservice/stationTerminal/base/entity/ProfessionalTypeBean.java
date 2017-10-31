package com.yunda.jx.webservice.stationTerminal.base.entity;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于获取专业类型实体包装
 * <li>说明: 用于getProfessionalType接口方法
 * <li>创建人：王治龙
 * <li>创建日期：2013-12-27
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ProfessionalTypeBean implements java.io.Serializable {
    
    private String professionalTypeID; // 类型编号
    
    private String professionalTypeName; // 类型名称
    
    private String proSeq; // 类型序列
    
    public ProfessionalTypeBean () {}
    
    public ProfessionalTypeBean (String professionalTypeID, String professionalTypeName, String proSeq) {
    	this.professionalTypeID = professionalTypeID;
    	this.professionalTypeName = professionalTypeName;
    	this.proSeq = proSeq;
    }
    
    public String getProfessionalTypeID() {
        return professionalTypeID;
    }
    
    public void setProfessionalTypeID(String professionalTypeID) {
        this.professionalTypeID = professionalTypeID;
    }
    
    public String getProfessionalTypeName() {
        return professionalTypeName;
    }
    
    public void setProfessionalTypeName(String professionalTypeName) {
        this.professionalTypeName = professionalTypeName;
    }
    
    public String getProSeq() {
        return proSeq;
    }
    
    public void setProSeq(String proSeq) {
        this.proSeq = proSeq;
    }
    
}
