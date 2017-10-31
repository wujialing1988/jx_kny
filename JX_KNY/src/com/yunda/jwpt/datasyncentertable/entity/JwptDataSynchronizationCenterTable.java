package com.yunda.jwpt.datasyncentertable.entity;

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
 * <li>说明: 系统临时中间表 模型
 * <li>创建人：林欢
 * <li>创建日期：2016-5-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
@Entity
@Table(name="JWPT_Data_Syn_Center_Table")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class JwptDataSynchronizationCenterTable implements java.io.Serializable, Comparable<JwptDataSynchronizationCenterTable> {

    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy="uuid", name = "uuid_id_generator")
    @Id @GeneratedValue(generator="uuid_id_generator")
    private String idx;
    
    /** 业务表主键IDX */
    @Column(name = "Business_IDX")
    private String businessIDX;
    
    /** 业务表名称 */
    @Column(name = "Business_Table_Name")
    private String businessTableName;
    
    /** 操作名称 操作（9 ：新增，5：更新，3：删除）*/
    @Column(name = "Operat")
    private String operat;
    
    /** 定时器更新时间*/
    @Column(name = "Update_Time")
    private String updateTime;
    
    public String getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getBusinessIDX() {
        return businessIDX;
    }
    
    public void setBusinessIDX(String businessIDX) {
        this.businessIDX = businessIDX;
    }


    public String getBusinessTableName() {
        return businessTableName;
    }

    
    public void setBusinessTableName(String businessTableName) {
        this.businessTableName = businessTableName;
    }

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public String getOperat() {
        return operat;
    }

    
    public void setOperat(String operat) {
        this.operat = operat;
    }

    /**
     * <li>说明：实现排序，按操作类型升序排序，如：3,5,5,9
     * <li>创建人：何涛
     * <li>创建日期：2015-05-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param o 系统临时中间表实体对象
     * @return int 排序比较结果
     */
    public int compareTo(JwptDataSynchronizationCenterTable o) {
        return operat.compareTo(o.operat);
    }
    
}
