package com.yunda.zb.zbfw.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: ZbglJobNodeExtConfigDef实体 数据表：整备扩展配置
 * <li>创建人：程梅
 * <li>创建日期：2016年4月7日
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "ZB_ZBGL_JobNode_Ext_Config_Def")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglJobNodeExtConfigDef implements java.io.Serializable {
    
    /** 机车状态设置 */
    public static final String EXT_TRAIN_STATUS = "ext_train_status";
    
    /** 整备范围数据字典 */
    public static final String EXT_DICT_TYPE = "ext_dict_type";
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 节点主键 */
    @Column(name = "Node_IDX")
    private String nodeIDX;
    
    /* 配置名称 */
    @Column(name = "Config_Name")
    private String configName;
    
    /* 配置值 */
    @Column(name = "Config_Value")
    private String configValue;
    
    /* 配置描述 */
    @Column(name = "Config_Desc")
    private String configDesc;
    
    /**
     * @return String 获取节点主键
     */
    public String getNodeIDX() {
        return nodeIDX;
    }
    
    /**
     * @param nodeIDX 设置节点主键
     */
    public void setNodeIDX(String nodeIDX) {
        this.nodeIDX = nodeIDX;
    }
    
    /**
     * @return String 获取配置描述
     */
    public String getConfigDesc() {
        return configDesc;
    }
    
    /**
     * @param configDesc 设置配置描述
     */
    public void setConfigDesc(String configDesc) {
        this.configDesc = configDesc;
    }
    
    /**
     * @return 获取配置名称
     */
    public String getConfigName() {
        return configName;
    }
    
    /**
     * @param configName 设置配置名称
     */
    public void setConfigName(String configName) {
        this.configName = configName;
    }
    
    /**
     * @return 获取配置值
     */
    public String getConfigValue() {
        return configValue;
    }
    
    /**
     * @param configValue 设置配置值
     */
    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }
    
    /**
     * @return String idx主键
     */
    public String getIdx() {
        return idx;
    }
    
    /**
     * @param idx 设置idx主键
     */
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
}
