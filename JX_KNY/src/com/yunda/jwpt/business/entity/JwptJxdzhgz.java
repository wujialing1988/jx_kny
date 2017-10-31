package com.yunda.jwpt.business.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

import com.yunda.frame.common.JXConfig;
import com.yunda.jwpt.business.manager.JwptBaseManager;
import com.yunda.jwpt.common.IAdaptable;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: Jxdzhgz实体类, 数据表：数据同步临时表-机车检修电子合格证（主表）
 * <li>创建人：何涛
 * <li>创建日期：2016-05-31
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JWPT_JXDZHGZ")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class JwptJxdzhgz implements IAdaptable {
    
    /** 状态-未启动 */
    public static final String STATUS_WQD = "01";
    /** 状态-处理中 */
    public static final String STATUS_CLZ = "02";
    /** 状态-已处理 */
    public static final String STATUS_YCL = "03";
    
    /** 主键 */
    @Id
    @Column(name = "JXDZHGZ_ID")
    private String idx;
    
    /** 车型编码 */
    @Column(name = "JXDZHGZ_CXBM")
    private String jxdzhgzCxbm;
    
    /** 车型简称 */
    @Column(name = "JXDZHGZ_CXJC")
    private String jxdzhgzCxjc;
    
    /** 车号 */
    @Column(name = "JXDZHGZ_CH")
    private String jxdzhgzCh;
    
    /** 配属段编码 */
    @Column(name = "JXDZHGZ_PSDBM")
    private String jxdzhgzPsdbm;
    
    /** 配属段名称 */
    @Column(name = "JXDZHGZ_PSDMC")
    private String jxdzhgzPsdmc;
    
    /** 承修段编码 */
    @Column(name = "JXDZHGZ_CXDBM")
    private String jxdzhgzCxdbm;
    
    /** 承修段名称 */
    @Column(name = "JXDZHGZ_CXDMC")
    private String jxdzhgzCxdmc;
    
    /** 检修计划ID */
    @Column(name = "JXDZHGZ_JHMXID")
    private String jxdzhgzJhmxid;
    
    /** 走行公里 */
    @Column(name = "JXDZHGZ_ZXGL")
    private String jxdzhgzZxgl;
    
    /** 修程 */
    @Column(name = "JXDZHGZ_RC")
    private String jxdzhgzRc;
    
    /** 修次 */
    @Column(name = "JXDZHGZ_RT")
    private String jxdzhgzRt;
    
    /** 计划开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "JXDZHGZ_JHKSSJ")
    private Date jxdzhgzJhkssj;
    
    /** 计划完成时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "JXDZHGZ_JHJSSJ")
    private Date jxdzhgzJhjssj;
    
    /** 修理开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "JXDZHGZ_XLKSSJ")
    private Date jxdzhgzXlkssj;
    
    /** 修理结束时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "JXDZHGZ_XLJSSJ")
    private Date jxdzhgzXljssj;
    
    /** 计划生成时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "JXDZHGZ_JHSCSJ")
    private Date jxdzhgzJhscsj;
    
    /** 验收时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "JXDZHGZ_YSSJ")
    private Date jxdzhgzYssj;
    
    /** 交车时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "JXDZHGZ_JCSJ")
    private Date jxdzhgzJcsj;
    
    /** 检修段编码 */
    @Column(name = "DT_DBM")
    private String dtDbm;
    
    /** 状态:(01:未启动，02:处理中、03:已处理) */
    private String zt;
    
    /** 版本号 */
    private Long version;
    
    /** 记录状态 */
    @Column(name = "RECORD_STATUS")
    private Integer recordStatus;
    
    /** 创建人 */
    private Long creator;
    
    /** 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME")
    private Date createTime;
    
    /** 修改人 */
    private Long updator;
    
    /** 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
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
        return "T_JXGL_JXDZHGZ";
    }
    
    /**
     * <li>说明：从机车检修作业计划实体对象进行数据同步
     * <li>创建人：何涛
     * <li>创建日期：2016-05-31
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param args 参数数组，[机车检修作业计划实体对象, 操作类型]
     * @return 数据同步临时表-机车检修电子合格证（主表）实体对象
     */
    public JwptJxdzhgz adaptFrom(Object... args) {
        TrainWorkPlan obj = (TrainWorkPlan) args[0];
        if (null == obj) {
            return null;
        }
        idx = obj.getIdx();
        jxdzhgzCxbm = obj.getTrainTypeIDX();
        jxdzhgzCxjc = obj.getTrainTypeShortName();
        jxdzhgzCh = obj.getTrainNo();
        jxdzhgzPsdbm = obj.getDID();
        jxdzhgzPsdmc = obj.getDNAME();
        jxdzhgzCxdbm = obj.getDelegateDID();
        jxdzhgzCxdmc = obj.getDelegateDName();
        jxdzhgzJhmxid = obj.getProcessIDX();
        // jxdzhgzZxgl = obj.get            //TODO 走行公里（无对应项）
        jxdzhgzRc = obj.getRepairClassName();
        jxdzhgzRt = obj.getRepairtimeName();
        jxdzhgzJhkssj = obj.getPlanBeginTime();
        jxdzhgzJhjssj = obj.getPlanEndTime();
        jxdzhgzXlkssj = obj.getBeginTime();
        jxdzhgzXljssj = obj.getEndTime();
        jxdzhgzJhscsj = obj.getWorkPlanTime();
        jxdzhgzYssj = obj.getEndTime();     //TODO 验收时间（无对应项，使用实际完成时间）
        jxdzhgzJcsj = obj.getEndTime();     //TODO 交车时间（无对应项，使用实际完成时间）
        dtDbm = JXConfig.getInstance().getOverseaOrgcode(); // 检修段编码
        zt = obj.getWorkPlanStatus();
        version = 1L;                       // 版本号（默认为：1）
        creator = obj.getCreator();
        createTime = obj.getCreateTime();
        updator = obj.getUpdator();
        updateTime = obj.getUpdateTime();
        recordStatus = obj.getRecordStatus();
        this.operateType = (Integer) args[1];
        return this;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Long getCreator() {
        return creator;
    }
    
    public void setCreator(Long creator) {
        this.creator = creator;
    }
    
    public String getDtDbm() {
        return dtDbm;
    }
    
    public void setDtDbm(String dtDbm) {
        this.dtDbm = dtDbm;
    }
    
    public String getJxdzhgzCh() {
        return jxdzhgzCh;
    }
    
    public void setJxdzhgzCh(String jxdzhgzCh) {
        this.jxdzhgzCh = jxdzhgzCh;
    }
    
    public String getJxdzhgzCxbm() {
        return jxdzhgzCxbm;
    }
    
    public void setJxdzhgzCxbm(String jxdzhgzCxbm) {
        this.jxdzhgzCxbm = jxdzhgzCxbm;
    }
    
    public String getJxdzhgzCxdbm() {
        return jxdzhgzCxdbm;
    }
    
    public void setJxdzhgzCxdbm(String jxdzhgzCxdbm) {
        this.jxdzhgzCxdbm = jxdzhgzCxdbm;
    }
    
    public String getJxdzhgzCxdmc() {
        return jxdzhgzCxdmc;
    }
    
    public void setJxdzhgzCxdmc(String jxdzhgzCxdmc) {
        this.jxdzhgzCxdmc = jxdzhgzCxdmc;
    }
    
    public String getJxdzhgzCxjc() {
        return jxdzhgzCxjc;
    }
    
    public void setJxdzhgzCxjc(String jxdzhgzCxjc) {
        this.jxdzhgzCxjc = jxdzhgzCxjc;
    }

    @Override
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public Date getJxdzhgzJcsj() {
        return jxdzhgzJcsj;
    }
    
    public void setJxdzhgzJcsj(Date jxdzhgzJcsj) {
        this.jxdzhgzJcsj = jxdzhgzJcsj;
    }
    
    public Date getJxdzhgzJhjssj() {
        return jxdzhgzJhjssj;
    }
    
    public void setJxdzhgzJhjssj(Date jxdzhgzJhjssj) {
        this.jxdzhgzJhjssj = jxdzhgzJhjssj;
    }
    
    public Date getJxdzhgzJhkssj() {
        return jxdzhgzJhkssj;
    }
    
    public void setJxdzhgzJhkssj(Date jxdzhgzJhkssj) {
        this.jxdzhgzJhkssj = jxdzhgzJhkssj;
    }
    
    public String getJxdzhgzJhmxid() {
        return jxdzhgzJhmxid;
    }
    
    public void setJxdzhgzJhmxid(String jxdzhgzJhmxid) {
        this.jxdzhgzJhmxid = jxdzhgzJhmxid;
    }
    
    public Date getJxdzhgzJhscsj() {
        return jxdzhgzJhscsj;
    }
    
    public void setJxdzhgzJhscsj(Date jxdzhgzJhscsj) {
        this.jxdzhgzJhscsj = jxdzhgzJhscsj;
    }
    
    public String getJxdzhgzPsdbm() {
        return jxdzhgzPsdbm;
    }
    
    public void setJxdzhgzPsdbm(String jxdzhgzPsdbm) {
        this.jxdzhgzPsdbm = jxdzhgzPsdbm;
    }
    
    public String getJxdzhgzPsdmc() {
        return jxdzhgzPsdmc;
    }
    
    public void setJxdzhgzPsdmc(String jxdzhgzPsdmc) {
        this.jxdzhgzPsdmc = jxdzhgzPsdmc;
    }
    
    public String getJxdzhgzRc() {
        return jxdzhgzRc;
    }
    
    public void setJxdzhgzRc(String jxdzhgzRc) {
        this.jxdzhgzRc = jxdzhgzRc;
    }
    
    public String getJxdzhgzRt() {
        return jxdzhgzRt;
    }
    
    public void setJxdzhgzRt(String jxdzhgzRt) {
        this.jxdzhgzRt = jxdzhgzRt;
    }
    
    public Date getJxdzhgzXljssj() {
        return jxdzhgzXljssj;
    }
    
    public void setJxdzhgzXljssj(Date jxdzhgzXljssj) {
        this.jxdzhgzXljssj = jxdzhgzXljssj;
    }
    
    public Date getJxdzhgzXlkssj() {
        return jxdzhgzXlkssj;
    }
    
    public void setJxdzhgzXlkssj(Date jxdzhgzXlkssj) {
        this.jxdzhgzXlkssj = jxdzhgzXlkssj;
    }
    
    public Date getJxdzhgzYssj() {
        return jxdzhgzYssj;
    }
    
    public void setJxdzhgzYssj(Date jxdzhgzYssj) {
        this.jxdzhgzYssj = jxdzhgzYssj;
    }
    
    public String getJxdzhgzZxgl() {
        return jxdzhgzZxgl;
    }
    
    public void setJxdzhgzZxgl(String jxdzhgzZxgl) {
        this.jxdzhgzZxgl = jxdzhgzZxgl;
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
    
    public Long getUpdator() {
        return updator;
    }
    
    public void setUpdator(Long updator) {
        this.updator = updator;
    }
    
    public Long getVersion() {
        return version;
    }
    
    public void setVersion(Long version) {
        this.version = version;
    }
    
    public String getZt() {
        return zt;
    }
    
    public void setZt(String zt) {
        this.zt = zt;
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
        sb.append("(jxdzhgz_id, jxdzhgz_cxbm, jxdzhgz_cxjc, jxdzhgz_ch, jxdzhgz_psdbm, jxdzhgz_psdmc, jxdzhgz_cxdbm, jxdzhgz_cxdmc, jxdzhgz_jhmxid, jxdzhgz_zxgl, jxdzhgz_rc, jxdzhgz_rt, jxdzhgz_jhkssj, jxdzhgz_jhjssj, jxdzhgz_xlkssj, jxdzhgz_xljssj, jxdzhgz_jhscsj, jxdzhgz_yssj, jxdzhgz_jcsj, dt_dbm, zt, version, record_status, creator, create_time, updator, update_time)");
        sb.append("values(");
        sb.append(SINGLE_QUOTE_MARK).append(idx).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(jxdzhgzCxbm).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(jxdzhgzCxjc).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(jxdzhgzCh).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(jxdzhgzPsdbm).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(jxdzhgzPsdmc).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(jxdzhgzCxdbm).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(jxdzhgzCxdmc).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(jxdzhgzJhmxid).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(null == jxdzhgzZxgl ? "" : jxdzhgzZxgl).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(jxdzhgzRc).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(jxdzhgzRt).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(JwptBaseManager.toDate(jxdzhgzJhkssj)).append(JOINSTR);
        sb.append(JwptBaseManager.toDate(jxdzhgzJhjssj)).append(JOINSTR);
        sb.append(JwptBaseManager.toDate(jxdzhgzXlkssj)).append(JOINSTR);
        sb.append(JwptBaseManager.toDate(jxdzhgzXljssj)).append(JOINSTR);
        sb.append(JwptBaseManager.toDate(jxdzhgzJhscsj)).append(JOINSTR);
        sb.append(JwptBaseManager.toDate(jxdzhgzYssj)).append(JOINSTR);
        sb.append(JwptBaseManager.toDate(jxdzhgzJcsj)).append(JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(dtDbm).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(this.convertZt()).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(version).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(recordStatus).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(creator).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(JwptBaseManager.toDate(createTime)).append(JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(updator).append(SINGLE_QUOTE_MARK_JOINSTR);
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
        sb.append(" set ").append("(jxdzhgz_id, jxdzhgz_cxbm, jxdzhgz_cxjc, jxdzhgz_ch, jxdzhgz_psdbm, jxdzhgz_psdmc, jxdzhgz_cxdbm, jxdzhgz_cxdmc, jxdzhgz_jhmxid, jxdzhgz_zxgl, jxdzhgz_rc, jxdzhgz_rt, jxdzhgz_jhkssj, jxdzhgz_jhjssj, jxdzhgz_xlkssj, jxdzhgz_xljssj, jxdzhgz_jhscsj, jxdzhgz_yssj, jxdzhgz_jcsj, dt_dbm, zt, version, record_status, creator, create_time, updator, update_time)");
        sb.append(" = (select jxdzhgz_id, jxdzhgz_cxbm, jxdzhgz_cxjc, jxdzhgz_ch, jxdzhgz_psdbm, jxdzhgz_psdmc, jxdzhgz_cxdbm, jxdzhgz_cxdmc, jxdzhgz_jhmxid, jxdzhgz_zxgl, jxdzhgz_rc, jxdzhgz_rt, jxdzhgz_jhkssj, jxdzhgz_jhjssj, jxdzhgz_xlkssj, jxdzhgz_xljssj, jxdzhgz_jhscsj, jxdzhgz_yssj, jxdzhgz_jcsj, dt_dbm, zt, version, record_status, creator, create_time, updator, update_time from jwpt_jxdzhgz where jxdzhgz_id ='");
        sb.append(idx);
        sb.append("') where jxdzhgz_id = '").append(idx).append("'");
        return sb.toString();
    }
    
    /**
     * <li>说明：转换机车检修作业计划状态字段字面值
     * <li>创建人：何涛
     * <li>创建日期：2016-6-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 状态:(01:未启动，02:处理中、03:已处理)
     */
    private String convertZt() {
        if (TrainWorkPlan.STATUS_NEW.equals(zt)) { 
            return STATUS_WQD;
        }
        if (TrainWorkPlan.STATUS_HANDLING.equals(zt)) { 
            return STATUS_CLZ;
        }
        if (TrainWorkPlan.STATUS_HANDLED.equals(zt)) { 
            return STATUS_YCL;
        }
        return "";
    }
    
}
