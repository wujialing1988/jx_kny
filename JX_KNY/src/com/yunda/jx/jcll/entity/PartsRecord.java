package com.yunda.jx.jcll.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;



/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配件履历履历实体
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-10-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsRecord implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** 配件编号 */
    @Id
    @Column(name = "parts_no")
    private String partsNo;
    
    /** 配件名称 */
    @Column(name = "parts_name")
    private String partsName;
    
    /** 兑现数目 */
    @Column(name = "rdp_count")
    private Integer rdpCount ;

    
    public String getPartsName() {
        return partsName;
    }

    
    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }

    
    public String getPartsNo() {
        return partsNo;
    }

    
    public void setPartsNo(String partsNo) {
        this.partsNo = partsNo;
    }

    
    public Integer getRdpCount() {
        return rdpCount;
    }

    
    public void setRdpCount(Integer rdpCount) {
        this.rdpCount = rdpCount;
    }
    
}
