package com.yunda.jwpt.business.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

import com.yunda.frame.common.JXConfig;
import com.yunda.jwpt.business.manager.JwptBaseManager;
import com.yunda.jwpt.common.IAdaptable;
import com.yunda.jx.scdd.enforceplan.entity.TrainEnforcePlan;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JxglJcjxjh实体类, 数据表：机车生产计划
 * <li>创建人：何涛
 * <li>创建日期：2016-06-01
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JWPT_JCJXJH")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class JwptJcjxjh implements IAdaptable {
    
    /** 主键 */
    @Id
    @Column(name = "JCJXJH_ID")
    private String idx;
    
    /** 计划名称 */
    private String jhmc;
    
    /** 计划开始日期 */
    private Date jhksrq;
    
    /** 计划结束日期 */
    private Date jhjsrq;
    
    /** 编制人 */
    @Column(name = "BZR_ID")
    private String bzrId;
    
    /** 编制人名称 */
    private String bzrmc;
    
    /** 编制日期 */
    private Date bzrq;
    
    /** 编制单位ID */
    @Column(name = "BZDW_ID")
    private String bzdwId;
    
    /** 编制单位部门序列 */
    private String bzdwbmxl;
    
    /** 编制单位名称 */
    private String bzdwmc;
    
    /** 审批人 */
    private String spr;
    
    /** 审批单位名称 */
    private String spdwmc;
    
    /** 审批日期 */
    private Date sprq;
    
    /** 计划状态，10：编制完成；20：审核中；30：审核完成；40：已经兑现；50：计划完成' */
    private Integer jhzt;
    
    /** '表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "RECORD_STATUS")
    private Integer recordStatus;
    
    /** 检修段编码 */
    @Column(name = "DT_DBM")
    private String dtDbm;
    
    /** 创建时间 */
    @Column(name = "CREATE_TIME")
    private Date createTime;
    
    /** 修改时间 */
    @Column(name = "UPDATE_TIME")
    private Date updateTime;
    
    /** 操作类型，9 ：新增、5：更新、3：删除 */
    @Column(name = "OPERATE_TYPE")
    private Integer operateType;
    
    /**
     * <li>说明：获取业务临时数据表对应到总公司机务平台的数据表名称
     * <li>创建人：何涛
     * <li>创建日期：2016-6-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 总公司机务平台的数据表名称
     */
    @Override
    public String getSyncTableName() {
        return "T_JXGL_JCJXJH";
    }
    
    /**
     * <li>说明：从机车生产计划实体对象进行数据同步
     * <li>创建人：何涛
     * <li>创建日期：2016-06-01
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param args 参数数组，[机车生产计划实体对象, 操作类型]
     * @return 数据同步临时表-机车检修电子合格证（主表）实体对象
     */
    public Object adaptFrom(Object... args) {
        TrainEnforcePlan obj = (TrainEnforcePlan) args[0];
        if (null == obj) {
            return null;
        }
        idx= obj.getIdx();
        jhmc= obj.getPlanName();
        jhksrq= obj.getPlanStartDate();
        jhjsrq= obj.getPlanEndDate();
        bzrId= null == obj.getPlanPerson() ? "" : obj.getPlanPerson().toString();
        bzrmc= obj.getPlanPersonName();
        bzrq= obj.getPlanTime();
        bzdwId= null == obj.getPlanOrgId() ? "" : obj.getPlanOrgId().toString();
        bzdwbmxl= obj.getPlanOrgSeq();
        bzdwmc= obj.getPlanOrgName();
//        spr= obj.get              //TODO 审批人（无对应项）
//        spdwmc= obj.get           //TODO 审批单位名称（无对应项）
//        sprq= obj.get             //TODO 审批日期（无对应项）
        jhzt= obj.getPlanStatus();
        dtDbm = JXConfig.getInstance().getOverseaOrgcode(); // 检修段编码
        createTime= obj.getCreateTime();
        updateTime= obj.getUpdateTime();
        recordStatus = obj.getRecordStatus();
        this.operateType = (Integer) args[1];
        return this;
    }

    public String getBzdwbmxl() {
        return bzdwbmxl;
    }
    
    public void setBzdwbmxl(String bzdwbmxl) {
        this.bzdwbmxl = bzdwbmxl;
    }
    
    public String getBzdwId() {
        return bzdwId;
    }
    
    public void setBzdwId(String bzdwId) {
        this.bzdwId = bzdwId;
    }
    
    public String getBzdwmc() {
        return bzdwmc;
    }
    
    public void setBzdwmc(String bzdwmc) {
        this.bzdwmc = bzdwmc;
    }
    
    public String getBzrId() {
        return bzrId;
    }
    
    public void setBzrId(String bzrId) {
        this.bzrId = bzrId;
    }
    
    public String getBzrmc() {
        return bzrmc;
    }
    
    public void setBzrmc(String bzrmc) {
        this.bzrmc = bzrmc;
    }
    
    public Date getBzrq() {
        return bzrq;
    }
    
    public void setBzrq(Date bzrq) {
        this.bzrq = bzrq;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public String getDtDbm() {
        return dtDbm;
    }
    
    public void setDtDbm(String dtDbm) {
        this.dtDbm = dtDbm;
    }

    @Override
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public Date getJhjsrq() {
        return jhjsrq;
    }
    
    public void setJhjsrq(Date jhjsrq) {
        this.jhjsrq = jhjsrq;
    }
    
    public Date getJhksrq() {
        return jhksrq;
    }
    
    public void setJhksrq(Date jhksrq) {
        this.jhksrq = jhksrq;
    }
    
    public String getJhmc() {
        return jhmc;
    }
    
    public void setJhmc(String jhmc) {
        this.jhmc = jhmc;
    }
    
    public Integer getJhzt() {
        return jhzt;
    }
    
    public void setJhzt(Integer jhzt) {
        this.jhzt = jhzt;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public String getSpdwmc() {
        return spdwmc;
    }
    
    public void setSpdwmc(String spdwmc) {
        this.spdwmc = spdwmc;
    }
    
    public String getSpr() {
        return spr;
    }
    
    public void setSpr(String spr) {
        this.spr = spr;
    }
    
    public Date getSprq() {
        return sprq;
    }
    
    public void setSprq(Date sprq) {
        this.sprq = sprq;
    }
    
    public Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public Integer getOperateType() {
        return operateType;
    }
    
    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }
    
    /**
     * <li>说明：构建从业务临时数据表到总公司机务平台的数据表的插入sql
     * <li>创建人：何涛
     * <li>创建日期：2016-6-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 数据库插入sql
     */
    public String buildInsertSql() {
        StringBuilder sb = new StringBuilder(200);
        sb.append("insert into ").append(this.getSyncTableName());
        sb.append("(jcjxjh_id, jhmc, jhksrq, jhjsrq, bzr_id, bzrmc, bzrq, bzdw_id, bzdwbmxl, bzdwmc, spr, spdwmc, sprq, jhzt, record_status, dt_dbm, create_time, update_time)");
        sb.append("values(");
        sb.append(SINGLE_QUOTE_MARK).append(idx).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(jhmc).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(JwptBaseManager.toDate(jhksrq)).append(JOINSTR);
        sb.append(JwptBaseManager.toDate(jhjsrq)).append(JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(bzrId).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(bzrmc).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(JwptBaseManager.toDate(bzrq)).append(JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(bzdwId).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(bzdwbmxl).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(bzdwmc).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(spr).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(spdwmc).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(JwptBaseManager.toDate(sprq)).append(JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(jhzt).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(recordStatus).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(dtDbm).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(JwptBaseManager.toDate(createTime)).append(JOINSTR);
        sb.append(JwptBaseManager.toDate(updateTime));
        sb.append(")");
        return sb.toString();
    }
    
    /**
     * <li>说明：构建从业务临时数据表到总公司机务平台的数据表的更新sql
     * <li>创建人：何涛
     * <li>创建日期：2016-6-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 数据库更新sql
     */
    public String buildUpdateSql() {
        StringBuilder sb = new StringBuilder(200);
        sb.append("update ").append(this.getSyncTableName());
        sb.append(" set ").append("(jcjxjh_id, jhmc, jhksrq, jhjsrq, bzr_id, bzrmc, bzrq, bzdw_id, bzdwbmxl, bzdwmc, spr, spdwmc, sprq, jhzt, record_status, dt_dbm, create_time, update_time)");
        sb.append(" = (select jcjxjh_id, jhmc, jhksrq, jhjsrq, bzr_id, bzrmc, bzrq, bzdw_id, bzdwbmxl, bzdwmc, spr, spdwmc, sprq, jhzt, record_status, dt_dbm, create_time, update_time from jwpt_jcjxjh where jcjxjh_id ='");
        sb.append(idx);
        sb.append("') where jcjxjh_id = '").append(idx).append("'");
        return sb.toString();
    }
    
}
