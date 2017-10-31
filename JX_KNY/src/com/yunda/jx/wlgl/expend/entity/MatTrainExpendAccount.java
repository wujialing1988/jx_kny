package com.yunda.jx.wlgl.expend.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatTrainExpendAccount实体类, 数据表：机车用料消耗记录
 * <li>创建人： 何涛
 * <li>创建日期： 2014-9-28 下午01:51:43
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "WLGL_MAT_Train_Expend_Account")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatTrainExpendAccount implements java.io.Serializable {
    
    /** 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** 数据来源 - 机车退回入库 */
    public static final String SOURCE_INWH = "机车退回入库";
    
    /** 数据来源 - 出库到机车 */
    public static final String SOURCE_OUTWH = "出库到机车";
    
    /** idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /** 车型编码 */
    @Column(name = "TrainType_IDX")
    private String trainTypeIDX;
    
    /** 车型简称 */
    @Column(name = "TrainType_ShortName")
    private String trainTypeShortName;
    
    /** 车号 */
    private String trainNo;
    
    /** 修程编码 */
    @Column(name = "XC_ID")
    private String xcId;
    
    /** 修程名称 */
    @Column(name = "XC_Name")
    private String xcName;
    
    /** 修次编码 */
    @Column(name = "RT_ID")
    private String rtId;
    
    /** 修次名称 */
    @Column(name = "RT_Name")
    private String rtName;
    
    /** 消耗机构ID */
    @Column(name = "Expend_Org_ID")
    private Long expendOrgId;
    
    /** 消耗机构名称 */
    @Column(name = "Expend_Org")
    private String expendOrg;
    
    /** 消耗机构序列 */
    @Column(name = "Expend_Org_Seq")
    private String expendOrgSeq;
    
    /** 消耗时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Expend_Date")
    private java.util.Date expendDate;
    
    /** 物料编码 */
    @Column(name = "Mat_Code")
    private String matCode;
    
    /** 物料描述 */
    @Column(name = "Mat_Desc")
    private String matDesc;
    
    /** 计量单位 */
    private String unit;
    
    /** 数量 */
    private Integer qty;
    
    /** 单价 */
    private Float price;
    
    /** 登账人 */
    @Column(name = "Regist_Emp")
    private String registEmp;
    
    /** 登帐日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Regist_Date")
    private java.util.Date registDate;
    
    /** 数据来源:出库到机车、机车退回入库 */
    private String dataSource;
    
    /** 站点标识，为了同步数据而使用 */
    @Column(updatable = false)
    private String siteID;
    
    /* 物料类型（技改件、消耗件） */
    @Column(name = "Mat_Type")
    private String matType;
    
    /** 数据来源id */
    @Column(name = "SOURCE_IDX")
    private String sourceIx;
    
    /** 登账人主键 */
    @Column(name = "REGIST_EMP_ID")
    private Long registEmpID;
    
    /** 查询字段 - 维护开始日期 */
    @Transient
    private Date startDate;
    
    /** 查询字段 - 维护结束日期 */
    @Transient
    private Date endDate;
    /**
     * @return 设置数据来源
     */
    public String getDataSource() {
        return dataSource;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    /**
     * @return 获取消耗时间
     */
    public java.util.Date getExpendDate() {
        return expendDate;
    }
    
    /**
     * @return 获取消耗机构名称
     */
    public String getExpendOrg() {
        return expendOrg;
    }
    
    /**
     * @return 获取消耗机构ID
     */
    public Long getExpendOrgId() {
        return expendOrgId;
    }
    
    /**
     * @return 获取消耗机构序列
     */
    public String getExpendOrgSeq() {
        return expendOrgSeq;
    }
    
    /**
     * @return String idx主键
     */
    public String getIdx() {
        return idx;
    }
    
    /**
     * @return String 获取物料编码
     */
    public String getMatCode() {
        return matCode;
    }
    
    /**
     * @return String 获取物料描述
     */
    public String getMatDesc() {
        return matDesc;
    }
    
    /**
     * @return 获取单价
     */
    public Float getPrice() {
        return price;
    }
    
    /**
     * @return Integer 获取数量
     */
    public Integer getQty() {
        return qty;
    }
    
    /**
     * @return 获取登帐日期
     */
    public java.util.Date getRegistDate() {
        return registDate;
    }
    
    /**
     * @return 获取登账人
     */
    public String getRegistEmp() {
        return registEmp;
    }
    
    /**
     * @return 获取修次编码
     */
    public String getRtId() {
        return rtId;
    }
    
    /**
     * @return 获取修次名称
     */
    public String getRtName() {
        return rtName;
    }
    
    /**
     * @return String 获取站点标识
     */
    public String getSiteID() {
        return siteID;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    /**
     * @return 获取车号
     */
    public String getTrainNo() {
        return trainNo;
    }
    
    /**
     * @return 获取车型编码
     */
    public String getTrainTypeIDX() {
        return trainTypeIDX;
    }
    
    /**
     * @return 获取车型简称
     */
    public String getTrainTypeShortName() {
        return trainTypeShortName;
    }
    
    /**
     * @return String 获取计量单位
     */
    public String getUnit() {
        return unit;
    }
    
    /**
     * @return 获取修程编码
     */
    public String getXcId() {
        return xcId;
    }
    
    /**
     * @return 获取修程名称
     */
    public String getXcName() {
        return xcName;
    }
    
    /**
     * @param dataSource 设置数据来源
     */
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    /**
     * @param expendDate 设置消耗时间
     */
    public void setExpendDate(java.util.Date expendDate) {
        this.expendDate = expendDate;
    }
    
    /**
     * @param expendOrg 设置消耗机构名称
     */
    public void setExpendOrg(String expendOrg) {
        this.expendOrg = expendOrg;
    }
    
    /**
     * @param expendOrgId 设置消耗机构ID
     */
    public void setExpendOrgId(Long expendOrgId) {
        this.expendOrgId = expendOrgId;
    }
    
    /**
     * @param expendOrgSeq 设置消耗机构序列
     */
    public void setExpendOrgSeq(String expendOrgSeq) {
        this.expendOrgSeq = expendOrgSeq;
    }
    
    /**
     * @param 设置idx主键
     */
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    /**
     * @param 设置物料编码
     */
    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }
    
    /**
     * @param 设置物料描述
     */
    public void setMatDesc(String matDesc) {
        this.matDesc = matDesc;
    }
    
    /**
     * @param price 设置单价
     */
    public void setPrice(Float price) {
        this.price = price;
    }
    
    /**
     * @param 设置数量
     */
    public void setQty(Integer qty) {
        this.qty = qty;
    }
    
    /**
     * @param registDate 设置登帐日期
     */
    public void setRegistDate(java.util.Date registDate) {
        this.registDate = registDate;
    }
    
    /**
     * @param registEmp 设置登账人
     */
    public void setRegistEmp(String registEmp) {
        this.registEmp = registEmp;
    }
    
    /**
     * @param rtId 设置修次编码
     */
    public void setRtId(String rtId) {
        this.rtId = rtId;
    }
    
    /**
     * @param rtName 设置修次名称
     */
    public void setRtName(String rtName) {
        this.rtName = rtName;
    }
    
    /**
     * @param 设置站点标识
     */
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    /**
     * @param trainNo 设置车号
     */
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }
    
    /**
     * @param trainTypeIDX 设置车型编码
     */
    public void setTrainTypeIDX(String trainTypeIDX) {
        this.trainTypeIDX = trainTypeIDX;
    }
    
    /**
     * @param trainTypeShortName 设置车型简称
     */
    public void setTrainTypeShortName(String trainTypeShortName) {
        this.trainTypeShortName = trainTypeShortName;
    }
    
    /**
     * @param 设置计量单位
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public String getMatType() {
        return matType;
    }
    
    public void setMatType(String matType) {
        this.matType = matType;
    }
    
    public Long getRegistEmpID() {
        return registEmpID;
    }
    
    public void setRegistEmpID(Long registEmpID) {
        this.registEmpID = registEmpID;
    }
    
    public String getSourceIx() {
        return sourceIx;
    }
    
    public void setSourceIx(String sourceIx) {
        this.sourceIx = sourceIx;
    }
    
    /**
     * @param xcId 设置修程编码
     */
    public void setXcId(String xcId) {
        this.xcId = xcId;
    }
    
    /**
     * @param xcName 设置修程名称
     */
    public void setXcName(String xcName) {
        this.xcName = xcName;
    }
}
