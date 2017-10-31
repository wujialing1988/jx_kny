package com.yunda.jwpt.business.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车整备任务单-整备范围、普查、预警、整治等(主表)(机务平台数据同步官方模型)
 * <li>创建人：林欢
 * <li>创建日期：2016-5-16
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */

@Entity
@Table(name = "T_ZBGL_ZBRWD")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TZbglZbrwd implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @Id
    @GenericGenerator(strategy = "assigned", name = "personalAssigned")
    // @GeneratedValue(generator="uuid_id_generator")
    @Column(name = "ZBRWD_ID")
    private String zbrwdID;
    
    /** 整备电子合格证ID */
    @Column(name = "ZBHGZ_ID")
    private String zbhgzID;
    
    /** 作业任务类型代码:1、正常整备范围；2、普查和专项整治；3、预警； */
    @Column(name = "ZBRWD_ZYRWLXDM")
    private Integer zbrwdZyrwlxdm;
    
    /** 作业项目ID（对应任务类型中不同类型的任务定义的主键） */
    @Column(name = "ZBRWD_ZYXMID")
    private String zbrwdZyxmID;
    
    /** 作业任务: 作业任务名称，1、正常整备范围，对应表T_ZBGL_ZBRWDMB中的ZBRWD_ZYXM字段；2、普查及专项整治范围，对应表T_ZBGL_PCJZXZZ_RWXM中的PC_XM_XMMC3、预警范围，对应表T_ZBGL_JCYJ中的JCYJ_YJX */
    @Column(name = "ZBRWD_ZYRW")
    private String zbrwdZyrw;
    
    /** 作业时间标准 */
    @Column(name = "ZBRWD_ZYSJBZ")
    private Date zbrwdZysjbz;
    
    /** 作业人编码 */
    @Column(name = "ZBRWD_ZYRBM")
    private String zbrwdZyrbm;
    
    /** 作业人名称 */
    @Column(name = "ZBRWD_ZYRMC")
    private String zbrwdZyrmc;
    
    /** 领活时间 */
    @Column(name = "ZBRWD_LHSJ")
    private Date zbrwdLhsj;
    
    /** 销活时间 */
    @Column(name = "ZBRWD_XHSJ")
    private Date zbrwdXhsj;
    
    /** 互检人员编码 */
    @Column(name = "ZBRWD_HJRYBM")
    private String zbrwdHjrybm;
    
    /** 互检人员姓名 */
    @Column(name = "ZBRWD_HJRYXM")
    private String zbrwdHjryxm;
    
    /** 互检时间 */
    @Column(name = "ZBRWD_HJSJ")
    private Date zbrwdHjsj;
    
    /** 验收人编码 */
    @Column(name = "ZBRWD_YSRBM")
    private String zbrwdYsrbm;
    
    /** 验收人姓名 */
    @Column(name = "ZBRWD_YSRMC")
    private String zbrwdYsrmc;
    
    /** 验收时间 */
    @Column(name = "ZBRWD_YSSJ")
    private Date zbrwdYssj;
    
    /** 记录状态 */
    @Column(name = "RECORD_STATUS")
    private Integer recordStatus;
    
    /** 创建人 */
    @Column(name = "CREATOR")
    private Integer creator;
    
    /** 创建时间 */
    @Column(name = "CREATE_TIME")
    private Date createTime;
    
    /** 修改人 */
    @Column(name = "UPDATOR")
    private Integer updator;
    
    /** 修改时间 */
    @Column(name = "UPDATE_TIME")
    private Date updateTime;
    
    /** 记录类型（3删除，5修改，9原始） */
    @Column(name = "OPERATE_TYPE")
    private Integer operateType;
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Integer getCreator() {
        return creator;
    }
    
    public void setCreator(Integer creator) {
        this.creator = creator;
    }
    
    public Integer getOperateType() {
        return operateType;
    }
    
    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public Integer getUpdator() {
        return updator;
    }
    
    public void setUpdator(Integer updator) {
        this.updator = updator;
    }
    
    public String getZbhgzID() {
        return zbhgzID;
    }
    
    public void setZbhgzID(String zbhgzID) {
        this.zbhgzID = zbhgzID == null ? " " : zbhgzID;
    }
    
    public String getZbrwdHjrybm() {
        return zbrwdHjrybm;
    }
    
    public void setZbrwdHjrybm(String zbrwdHjrybm) {
        this.zbrwdHjrybm = zbrwdHjrybm;
    }
    
    public String getZbrwdHjryxm() {
        return zbrwdHjryxm;
    }
    
    public void setZbrwdHjryxm(String zbrwdHjryxm) {
        this.zbrwdHjryxm = zbrwdHjryxm;
    }
    
    public Date getZbrwdHjsj() {
        return zbrwdHjsj;
    }
    
    public void setZbrwdHjsj(Date zbrwdHjsj) {
        this.zbrwdHjsj = zbrwdHjsj;
    }
    
    public String getZbrwdID() {
        return zbrwdID;
    }
    
    public void setZbrwdID(String zbrwdID) {
        this.zbrwdID = zbrwdID;
    }
    
    public Date getZbrwdLhsj() {
        return zbrwdLhsj;
    }
    
    public void setZbrwdLhsj(Date zbrwdLhsj) {
        this.zbrwdLhsj = zbrwdLhsj;
    }
    
    public Date getZbrwdXhsj() {
        return zbrwdXhsj;
    }
    
    public void setZbrwdXhsj(Date zbrwdXhsj) {
        this.zbrwdXhsj = zbrwdXhsj;
    }
    
    public String getZbrwdYsrbm() {
        return zbrwdYsrbm;
    }
    
    public void setZbrwdYsrbm(String zbrwdYsrbm) {
        this.zbrwdYsrbm = zbrwdYsrbm;
    }
    
    public String getZbrwdYsrmc() {
        return zbrwdYsrmc;
    }
    
    public void setZbrwdYsrmc(String zbrwdYsrmc) {
        this.zbrwdYsrmc = zbrwdYsrmc;
    }
    
    public Date getZbrwdYssj() {
        return zbrwdYssj;
    }
    
    public void setZbrwdYssj(Date zbrwdYssj) {
        this.zbrwdYssj = zbrwdYssj;
    }
    
    public String getZbrwdZyrw() {
        return zbrwdZyrw;
    }
    
    public void setZbrwdZyrw(String zbrwdZyrw) {
        this.zbrwdZyrw = zbrwdZyrw;
    }
    
    public Integer getZbrwdZyrwlxdm() {
        return zbrwdZyrwlxdm;
    }
    
    public void setZbrwdZyrwlxdm(Integer zbrwdZyrwlxdm) {
        this.zbrwdZyrwlxdm = zbrwdZyrwlxdm;
    }
    
    public Date getZbrwdZysjbz() {
        return zbrwdZysjbz;
    }
    
    public void setZbrwdZysjbz(Date zbrwdZysjbz) {
        this.zbrwdZysjbz = zbrwdZysjbz;
    }
    
    public String getZbrwdZyrbm() {
        return zbrwdZyrbm;
    }
    
    public void setZbrwdZyrbm(String zbrwdZyrbm) {
        this.zbrwdZyrbm = zbrwdZyrbm;
    }
    
    public String getZbrwdZyrmc() {
        return zbrwdZyrmc;
    }
    
    public void setZbrwdZyrmc(String zbrwdZyrmc) {
        this.zbrwdZyrmc = zbrwdZyrmc;
    }
    
    public String getZbrwdZyxmID() {
        return zbrwdZyxmID;
    }
    
    public void setZbrwdZyxmID(String zbrwdZyxmID) {
        this.zbrwdZyxmID = zbrwdZyxmID;
    }
    
}
