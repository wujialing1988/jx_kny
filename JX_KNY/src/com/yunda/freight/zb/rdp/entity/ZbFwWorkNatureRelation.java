package com.yunda.freight.zb.rdp.entity;

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
 * <li>说明: 列检范围适用作业性质
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-3-30
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "ZB_FW_NATURE_RELATION")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbFwWorkNatureRelation implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 列检范围idx */
    @Column(name = "ZBFW_IDX")
    private String zbfwIdx;
    
    /* 适用作业性质编码 */
    @Column(name = "WORK_NATURE_CODE")
    private String workNatureCode;
    
    /* 适用作业性质名称 到达作业、始发作业、中转作业、通过作业 */
    @Column(name = "WORK_NATURE")
    private String workNature;

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public String getWorkNature() {
        return workNature;
    }


    
    public void setWorkNature(String workNature) {
        this.workNature = workNature;
    }


    
    public String getWorkNatureCode() {
        return workNatureCode;
    }


    
    public void setWorkNatureCode(String workNatureCode) {
        this.workNatureCode = workNatureCode;
    }


    public String getZbfwIdx() {
        return zbfwIdx;
    }


    
    public void setZbfwIdx(String zbfwIdx) {
        this.zbfwIdx = zbfwIdx;
    }
    
    

  
}
