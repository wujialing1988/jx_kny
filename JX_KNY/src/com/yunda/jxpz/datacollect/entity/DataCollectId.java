package com.yunda.jxpz.datacollect.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：收藏夹实体联合主键
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
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class DataCollectId implements Serializable {
    /* 收藏数据实体*/
    @Column(name="DATA_ENTITY")
    private String dataEntity;
    /* 收藏数据主键 */
    @Column(name="DATA_IDX")
    private String dataIdx;
    /* 收藏者主键 */
    @Column(name="COLLECT_EMP_ID")
    private Long collectEmpId;
    /**
     * @return String 获取收藏数据实体
     */
    public String getDataEntity(){
        return dataEntity;
    }
    /**
     * @param dataEntity 设置收藏数据实体
     */
    public void setDataEntity(String dataEntity) {
        this.dataEntity = dataEntity;
    }
    /**
     * @return String 获取收藏数据主键
     */
    public String getDataIdx(){
        return dataIdx;
    }
    /**
     * @param dataIdx 设置收藏数据主键
     */
    public void setDataIdx(String dataIdx) {
        this.dataIdx = dataIdx;
    }
    /**
     * @return Long 获取收藏者主键
     */
    public Long getCollectEmpId(){
        return collectEmpId;
    }
    
    public void setCollectEmpId(Long collectEmpId) {
        this.collectEmpId = collectEmpId;
    }
    
}