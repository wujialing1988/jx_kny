package com.yunda.jx.pjwz.partsmanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsManageLog实体类, 数据表：配件管理日志
 * <li>创建人：程梅
 * <li>创建日期：2015-10-16
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "PJWZ_PARTS_MANAGE_LOG")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsManageLog implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** 操作类型 - 良好登记 */
    public static final String EVENT_TYPE_LHDJ = "良好登记";
    /** 操作类型 - 下车登记 */
    public static final String EVENT_TYPE_XCDJ = "下车登记";
    /** 操作类型 - 配件检修 */
    public static final String EVENT_TYPE_PJJX = "配件检修";
    /** 操作类型 - 配件委外 */
    public static final String EVENT_TYPE_PJWW = "配件委外";
    /** 操作类型 - 修竣入库 */
    public static final String EVENT_TYPE_XJRK = "配件入库";
    /** 操作类型 - 配件上车 */
    public static final String EVENT_TYPE_PJSC = "配件上车";
    /** 操作类型 - 配件报废 */
    public static final String EVENT_TYPE_PJBF = "配件报废";
    /** 操作类型 - 配件退库 */
    public static final String EVENT_TYPE_PJTK = "配件退库";
    /** 操作类型 - 配件销账 */
    public static final String EVENT_TYPE_PJXZ = "配件销账";
    /** 操作类型 - 配件调出 */
    public static final String EVENT_TYPE_PJDC = "配件调出";
    /** 操作类型 - 配件出库 */
    public static final String EVENT_TYPE_PJCK = "配件出库";
    /** 操作类型 - 配件拆卸 */
    public static final String EVENT_TYPE_PJCX = "配件拆卸";
    /** 操作类型 - 配件安装 */
    public static final String EVENT_TYPE_PJAZ = "配件安装";
    /** 操作类型 - 委外回段 */
    public static final String EVENT_TYPE_WWHD = "委外回段";
    /** 操作类型 - 配件移库 */
    public static final String EVENT_TYPE_PJYK = "配件移库";
    /** 操作类型 - 配件校验 */
    public static final String EVENT_TYPE_PJJY = "配件校验";
    /** 操作类型 - 配件交接 */
    public static final String EVENT_TYPE_PJJJ = "配件交接";
    /** 操作类型 - 配件售后入段 */
    public static final String EVENT_TYPE_PJSHRD = "配件售后入段";
    /** 操作类型 - 配件售后出段 */
    public static final String EVENT_TYPE_PJSHCD = "配件售后出段";
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 配件信息主键 */
    @Column(name = "PARTS_ACCOUNT_IDX")
    private String partsAccountIdx;
    
    /* 业务操作模块主键 */
    @Column(name = "EVENT_IDX")
    private String eventIdx;
    
    /* 历史配件状态编码 */
    @Column(name = "Parts_Status_His")
    private String partsStatusHis;
    
    /* 历史配件状态名称 */
    @Column(name = "Parts_Status_Name_His")
    private String partsStatusNameHis;
    
    /* 操作类型 新购登记、调入登记、下车登记、配件检修、配件委外、修竣入库、配件上车、配件报废、配件销账等 */
    @Column(name = "EVENT_TYPE")
    private String eventType;
    
    /* 操作描述 */
    @Column(name = "EVENT_DESC")
    private String eventDesc;
    
    /* 操作时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EVENT_TIME")
    private java.util.Date eventTime;
    
    /* 操作人 */
    @Column(name = "OPERATOR_ID")
    private String operatorId;
    /* 历史责任部门id */
    @Column(name="MANAGE_DEPT_ID_His")
    private String manageDeptIdHis;
    /* 历史责任部门 */
    @Column(name="MANAGE_DEPT_His")
    private String manageDeptHis;
    /* 历史责任部门类型 */
    @Column(name="MANAGE_DEPT_Type_His")
    private Integer manageDeptTypeHis;
    /* 历史责任部门序列 */
    @Column(name="MANAGE_DEPT_ORGSEQ_His")
    private String manageDeptOrgseqHis;
    /*历史存放地点 */
    @Column(name="LOCATION_His")
    private String locationHis;
    /* 操作人名称 */
    private String operator;
    
    /**
     * <li>说明：构造方法
     * <li>创建人：何涛
     * <li>创建日期：2015-11-5
     * <li>修改人：
     * <li>修改日期：
     * @param eventType 操作类型
     * @param eventDesc 操作描述
     */
    public PartsManageLog(String eventType, String eventDesc) {
        super();
        this.eventType = eventType;
        this.eventDesc = eventDesc;
    }
    
    /**
     * <li>说明：构造方法
     * <li>创建人：何涛
     * <li>创建日期：2015-11-5
     * <li>修改人：
     * <li>修改日期：
     * @param eventType 操作类型
     * @param eventDesc 操作描述
     * @param eventIdx 业务操作模块主键
     * @param partsAccountIdx 配件信息主键
     */
    public PartsManageLog(String eventType, String eventDesc, String eventIdx, String partsAccountIdx) {
        super();
        this.eventType = eventType;
        this.eventDesc = eventDesc;
        this.eventIdx = eventIdx;
        this.partsAccountIdx = partsAccountIdx;
    }
    
    /**
     * <li>说明：默认构造方法
     * <li>创建人：何涛
     * <li>创建日期：2015-11-5
     * <li>修改人：
     * <li>修改日期：
     */
    public PartsManageLog() {
        super();
    }
    
    /**
     * <li>说明：设置业务操作模块主键
     * <li>创建人：何涛
     * <li>创建日期：2015-11-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param eventIdx 业务操作模块主键
     * @return 配件管理日志实体
     */
    public PartsManageLog eventIdx(String eventIdx) {
        this.eventIdx = eventIdx;
        return this;
    }

    /**
     * <li>说明：设置配件信息主键
     * <li>创建人：何涛
     * <li>创建日期：2015-11-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsAccountIdx 配件信息主键
     * @return 配件管理日志实体
     */
    public PartsManageLog partsAccountIdx(String partsAccountIdx) {
        this.partsAccountIdx = partsAccountIdx;
        return this;
    }

    /**
     * @return String 获取配件信息主键
     */
    public String getPartsAccountIdx() {
        return partsAccountIdx;
    }
    
    /**
     * @param partsAccountIdx 设置配件信息主键
     */
    public void setPartsAccountIdx(String partsAccountIdx) {
        this.partsAccountIdx = partsAccountIdx;
    }
    
    /**
     * @return String 获取业务操作主键
     */
    public String getEventIdx() {
        return eventIdx;
    }
    
    /**
     * @param eventIdx 设置业务操作主键
     */
    public void setEventIdx(String eventIdx) {
        this.eventIdx = eventIdx;
    }
    
    /**
     * @return String 获取历史配件状态编码
     */
    public String getPartsStatusHis() {
        return partsStatusHis;
    }
    
    /**
     * @param partsStatusHis 设置历史配件状态编码
     */
    public void setPartsStatusHis(String partsStatusHis) {
        this.partsStatusHis = partsStatusHis;
    }
    
    /**
     * @return String 获取操作类型
     */
    public String getEventType() {
        return eventType;
    }
    
    /**
     * @param eventType 设置操作类型
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    
    /**
     * @return String 获取操作描述
     */
    public String getEventDesc() {
        return eventDesc;
    }
    
    /**
     * @param eventDesc 设置操作描述
     */
    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }
    
    /**
     * @return java.util.Date 获取操作时间
     */
    public java.util.Date getEventTime() {
        return eventTime;
    }
    
    /**
     * @param eventTime 设置操作时间
     */
    public void setEventTime(java.util.Date eventTime) {
        this.eventTime = eventTime;
    }
    
    /**
     * @return String 获取操作人
     */
    public String getOperatorId() {
        return operatorId;
    }
    
    /**
     * @param operatorId 设置操作人
     */
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
    
    /**
     * @return String 获取操作人名称
     */
    public String getOperator() {
        return operator;
    }
    
    /**
     * @param operator 设置操作人名称
     */
    public void setOperator(String operator) {
        this.operator = operator;
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
    
    public String getPartsStatusNameHis() {
        return partsStatusNameHis;
    }
    
    public void setPartsStatusNameHis(String partsStatusNameHis) {
        this.partsStatusNameHis = partsStatusNameHis;
    }

    
    public String getManageDeptHis() {
        return manageDeptHis;
    }

    
    public void setManageDeptHis(String manageDeptHis) {
        this.manageDeptHis = manageDeptHis;
    }

    
    public String getManageDeptIdHis() {
        return manageDeptIdHis;
    }

    
    public void setManageDeptIdHis(String manageDeptIdHis) {
        this.manageDeptIdHis = manageDeptIdHis;
    }

    
    public String getManageDeptOrgseqHis() {
        return manageDeptOrgseqHis;
    }

    
    public void setManageDeptOrgseqHis(String manageDeptOrgseqHis) {
        this.manageDeptOrgseqHis = manageDeptOrgseqHis;
    }

    
    public Integer getManageDeptTypeHis() {
        return manageDeptTypeHis;
    }

    
    public void setManageDeptTypeHis(Integer manageDeptTypeHis) {
        this.manageDeptTypeHis = manageDeptTypeHis;
    }

    
    public String getLocationHis() {
        return locationHis;
    }

    
    public void setLocationHis(String locationHis) {
        this.locationHis = locationHis;
    }
}
