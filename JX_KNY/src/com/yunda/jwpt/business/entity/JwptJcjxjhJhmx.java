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
import com.yunda.jx.scdd.enforceplan.entity.TrainEnforcePlanDetail;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JxglJcjxjhJhmx实体类, 数据表：机车生产计划明细明细
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
@Table(name = "JWPT_JCJXJH_JHMX")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class JwptJcjxjhJhmx implements IAdaptable {
    
    /** 主键 */
    @Id
    @Column(name = "JHMX_ID")
    private String idx;
    
    /** 机车计划主键 */
    @Column(name = "JCJXJH_ID")
    private String jcjxjhId;
    
    /** 车型主键 */
    @Column(name = "CX_ID")
    private String cxId;
    
    /** 车型英文简称 */
    private String cxjc;
    
    /** 车号 */
    private String ch;
    
    /** 配属段ID */
    @Column(name = "PSD_ID")
    private String psdId;
    
    /** 配属段名称 */
    private String psdmc;
    
    /** 配属段简称 */
    private String psdjc;
    
    /** 修程编码 */
    private String xcbm;
    
    /** 修程名称 */
    private String xcmc;
    
    /** 修次 */
    private String xiucibm;
    
    /** 修次名称 */
    private String xiucimc;
    
    /** 承修段编码 */
    private String cxdbm;
    
    /** 承修段名称 */
    private String cxdmc;
    
    /** 计划进车日期 */
    private Date jcrq;
    
    /** 计划交车日期 */
    private Date jhjcrq;
    
    /** 委修段编码 */
    private String wxdbm;
    
    /** 委修段名称 */
    private String wxdmc;
    
    /** 计划状态，10：编制完成；20：审核中；30：审核完成；40：已经兑现；50：计划完成' */
    private Integer jhzt;
    
    /** 走行公里 */
    private Double zxgl;
    
    /** 备注 */
    private String bz;
    
    /** 检修段编码 */
    @Column(name = "DT_DBM")
    private String dtDbm;
    
    /** '表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "RECORD_STATUS")
    private Integer recordStatus;
    
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
        return "T_JXGL_JCJXJH_JHMX";
    }
    
    /**
     * <li>说明：从机车生产计划明细实体对象进行数据同步
     * <li>创建人：何涛
     * <li>创建日期：2016-06-01
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param args 参数数组，[机车生产计划明细实体对象, 操作类型]
     * @return 数据同步临时表-机车检修电子合格证（主表）实体对象
     */
    public Object adaptFrom(Object... args) {
        TrainEnforcePlanDetail obj = (TrainEnforcePlanDetail) args[0];
        if (null == obj) {
            return null;
        }
        idx = obj.getIdx();
        jcjxjhId = obj.getTrainEnforcePlanIDX();
        cxId = obj.getTrainTypeIDX();
        cxjc = obj.getTrainTypeShortName();
        ch = obj.getTrainNo();
        psdId = obj.getDid();
        psdmc = obj.getDNAME();
        psdjc = obj.getDShortName();
        xcbm = obj.getRepairClassIDX();
        xcmc = obj.getRepairClassName();
        xiucibm = obj.getRepairtimeIDX();
        xiucimc = obj.getRepairtimeName();
        // 报表统计局/段关键字段【cxdbm，cxdmc】
        String did = JXConfig.getInstance().getDid();
        String dname = JXConfig.getInstance().getDname();
        if (null == did || null == dname) {
           throw new NullPointerException("JXConfig.xml配置文件中未配置did或者dname项！"); 
        }
        cxdbm = did;        // 承修段编码
        cxdmc = dname;      // 承修段名称
        jcrq = obj.getPlanStartDate();
        jhjcrq = obj.getPlanEndDate();
        wxdbm = obj.getDelegateDId();
        wxdmc = obj.getDelegateDName();
        jhzt = obj.getPlanStatus();
        zxgl = obj.getRunningKM();
        bz = obj.getRemarks();
        dtDbm = JXConfig.getInstance().getOverseaOrgcode();    // 检修段编码
        createTime = obj.getCreateTime();
        updateTime = obj.getUpdateTime();
        recordStatus = obj.getRecordStatus();
        operateType = (Integer) args[1];
        return this;
    }
    
    public String getBz() {
        return bz;
    }
    
    public void setBz(String bz) {
        this.bz = bz;
    }
    
    public String getCh() {
        return ch;
    }
    
    public void setCh(String ch) {
        this.ch = ch;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public String getCxdbm() {
        return cxdbm;
    }
    
    public void setCxdbm(String cxdbm) {
        this.cxdbm = cxdbm;
    }
    
    public String getCxdmc() {
        return cxdmc;
    }
    
    public void setCxdmc(String cxdmc) {
        this.cxdmc = cxdmc;
    }
    
    public String getCxId() {
        return cxId;
    }
    
    public void setCxId(String cxId) {
        this.cxId = cxId;
    }
    
    public String getCxjc() {
        return cxjc;
    }
    
    public void setCxjc(String cxjc) {
        this.cxjc = cxjc;
    }
    
    public String getDtDbm() {
        return dtDbm;
    }
    
    public void setDtDbm(String dtDbm) {
        this.dtDbm = dtDbm;
    }
    
    public String getJcjxjhId() {
        return jcjxjhId;
    }
    
    public void setJcjxjhId(String jcjxjhId) {
        this.jcjxjhId = jcjxjhId;
    }
    
    public Date getJcrq() {
        return jcrq;
    }
    
    public void setJcrq(Date jcrq) {
        this.jcrq = jcrq;
    }
    
    public Date getJhjcrq() {
        return jhjcrq;
    }
    
    public void setJhjcrq(Date jhjcrq) {
        this.jhjcrq = jhjcrq;
    }

    @Override
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public Integer getJhzt() {
        return jhzt;
    }
    
    public void setJhzt(Integer jhzt) {
        this.jhzt = jhzt;
    }
    
    public Integer getOperateType() {
        return operateType;
    }
    
    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }
    
    public String getPsdId() {
        return psdId;
    }
    
    public void setPsdId(String psdId) {
        this.psdId = psdId;
    }
    
    public String getPsdjc() {
        return psdjc;
    }
    
    public void setPsdjc(String psdjc) {
        this.psdjc = psdjc;
    }
    
    public String getPsdmc() {
        return psdmc;
    }
    
    public void setPsdmc(String psdmc) {
        this.psdmc = psdmc;
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
    
    public String getWxdbm() {
        return wxdbm;
    }
    
    public void setWxdbm(String wxdbm) {
        this.wxdbm = wxdbm;
    }
    
    public String getWxdmc() {
        return wxdmc;
    }
    
    public void setWxdmc(String wxdmc) {
        this.wxdmc = wxdmc;
    }
    
    public String getXcbm() {
        return xcbm;
    }
    
    public void setXcbm(String xcbm) {
        this.xcbm = xcbm;
    }
    
    public String getXcmc() {
        return xcmc;
    }
    
    public void setXcmc(String xcmc) {
        this.xcmc = xcmc;
    }
    
    public String getXiucibm() {
        return xiucibm;
    }
    
    public void setXiucibm(String xiucibm) {
        this.xiucibm = xiucibm;
    }
    
    public String getXiucimc() {
        return xiucimc;
    }
    
    public void setXiucimc(String xiucimc) {
        this.xiucimc = xiucimc;
    }
    
    public Double getZxgl() {
        return zxgl;
    }
    
    public void setZxgl(Double zxgl) {
        this.zxgl = zxgl;
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
        sb.append("(jhmx_id, jcjxjh_id, cx_id, cxjc, ch, psd_id, psdmc, psdjc, xcbm, xcmc, xiucibm, xiucimc, cxdbm, cxdmc, jcrq, jhjcrq, wxdbm, wxdmc, jhzt, zxgl, bz, dt_dbm, record_status, create_time, update_time)");
        sb.append("values(");
        sb.append(SINGLE_QUOTE_MARK).append(idx).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(jcjxjhId).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(cxId).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(cxjc).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(ch).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(psdId).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(psdmc).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(psdjc).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(xcbm).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(xcmc).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(null == xiucibm ? "" : xiucibm).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(null == xiucimc ? "" : xiucimc).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(null == cxdbm ? "" : cxdbm).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(null == cxdmc ? "" : cxdmc).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(JwptBaseManager.toDate(jcrq)).append(JOINSTR);
        sb.append(JwptBaseManager.toDate(jhjcrq)).append(JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(null == wxdbm ? "" : wxdbm).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(null == wxdmc ? "" : wxdmc).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(jhzt).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(null == zxgl ? 0 : zxgl).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(null == bz ? "" : bz).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(dtDbm).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(recordStatus).append(SINGLE_QUOTE_MARK_JOINSTR);
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
        sb.append(" set ").append("(jhmx_id, jcjxjh_id, cx_id, cxjc, ch, psd_id, psdmc, psdjc, xcbm, xcmc, xiucibm, xiucimc, cxdbm, cxdmc, jcrq, jhjcrq, wxdbm, wxdmc, jhzt, zxgl, bz, dt_dbm, record_status, create_time, update_time)");
        sb.append(" = (select jhmx_id, jcjxjh_id, cx_id, cxjc, ch, psd_id, psdmc, psdjc, xcbm, xcmc, xiucibm, xiucimc, cxdbm, cxdmc, jcrq, jhjcrq, wxdbm, wxdmc, jhzt, zxgl, bz, dt_dbm, record_status, create_time, update_time from jwpt_jcjxjh_jhmx where jhmx_id ='");
        sb.append(idx);
        sb.append("') where jhmx_id = '").append(idx).append("'");
        return sb.toString();
    }
    
}
