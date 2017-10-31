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

import com.yunda.common.BusinessException;
import com.yunda.jwpt.business.manager.JwptBaseManager;
import com.yunda.jwpt.common.IAdaptable;
import com.yunda.jx.jsgl.jxrb.entity.DailyReport;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JxglJxrb实体类, 数据表：机车检修日报
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
@Table(name = "JWPT_JXRB")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class JwptJxrb implements IAdaptable {
    
    /** 主键 */
    @Id
    @Column(name = "JXRB_ID")
    private String idx;
    
    /** 检修电子合格证ID */
    @Column(name = "JXDZHGZ_ID")
    private String jxdzhgzId;
    
    /** 车型编码（来源于全路基础编码数据库） */
    private String cxbm;
    
    /** 车型简称 */
    private String cxjc;
    
    /** 车号 */
    private String ch;
    
    /** 委修单位编码（来源于全路基础编码数据库） */
    private String wxdbm;
    
    /** 委修单位名称 */
    private String wxdmc;
    
    /** 承修段编码（来源于全路基础编码数据库） */
    private String cxdbm;
    
    /** 承修段名称 */
    private String cxdmc;
    
    /** 上次相应修程后的走行公里（单位：公里） */
    private Double zxgl;
    
    /** 修程 */
    private String rc;
    
    /** 修次 */
    private String rt;
    
    /** 离段日期 */
    @Temporal(TemporalType.TIMESTAMP)
    private Date ldrq;
    
    /** 到厂日期 */
    @Temporal(TemporalType.TIMESTAMP)
    private Date dcrq;
    
    /** 开工日期 */
    @Temporal(TemporalType.TIMESTAMP)
    private Date kgrq;
    
    /** 竣工日期 */
    @Temporal(TemporalType.TIMESTAMP)
    private Date jgrq;
    
    /** 离厂日期 */
    @Temporal(TemporalType.TIMESTAMP)
    private Date lcrq;
    
    /** 回段日期 */
    @Temporal(TemporalType.TIMESTAMP)
    private Date hdrq;
    
    /** 检修状态（待修，检修中，修竣） */
    private String jxzt;
    
    /** 表示此条记录的状态：0为表示未删除；1表示删除，默认设置为0 */
    @Column(name = "RECORD_STATUS")
    private Integer recordStatus;
    
    /** 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME")
    private Date createTime;
    
    /** 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIME")
    private Date updateTime;
    
    /** 备注 */
    private String bz;
    
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
        return "T_JXGL_JXRB";
    }
    
    /**
     * <li>说明：从机车检修日报实体对象进行数据同步
     * <li>创建人：何涛
     * <li>创建日期：2016-06-01
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param args 参数数组，[机车检修日报实体对象, 操作类型]
     * @return 数据同步临时表-机车检修电子合格证（主表）实体对象
     */
    public Object adaptFrom(Object... args) {
        DailyReport obj = (DailyReport) args[0];
        if (null == obj) {
            return null;
        }
        if(null == obj.getWorkPlanID()) {
            throw new BusinessException("检修作业计划ID为空！");
        }
        idx = obj.getIdx();
        jxdzhgzId = obj.getWorkPlanID();
        cxbm = obj.getCxbm();
        cxjc = obj.getCxjc();
        ch = obj.getCh();
        wxdbm = obj.getWxdbm();
        wxdmc = obj.getWxdmc();
        // 报表统计局/段关键字段【cxdbm，cxdmc】
        cxdbm = obj.getCxdbm();
        cxdmc = obj.getCxdmc();
        zxgl = obj.getZxgl();
        rc = obj.getRepairClassName();
        rt = obj.getRepairTimeName();
        ldrq = obj.getLdrq();
        dcrq = obj.getDcrq();
        kgrq = obj.getKgrq();
        jgrq = obj.getJgrq();
        lcrq = obj.getLcrq();
        hdrq = obj.getHdrq();
        jxzt = obj.getJxzt();
        createTime = obj.getCreateTime();
        updateTime = obj.getUpdateTime();
        bz = obj.getBz();
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
    
    public String getCxbm() {
        return cxbm;
    }
    
    public void setCxbm(String cxbm) {
        this.cxbm = cxbm;
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
    
    public String getCxjc() {
        return cxjc;
    }
    
    public void setCxjc(String cxjc) {
        this.cxjc = cxjc;
    }
    
    public Date getDcrq() {
        return dcrq;
    }
    
    public void setDcrq(Date dcrq) {
        this.dcrq = dcrq;
    }
    
    public Date getHdrq() {
        return hdrq;
    }
    
    public void setHdrq(Date hdrq) {
        this.hdrq = hdrq;
    }
    
    public Date getJgrq() {
        return jgrq;
    }
    
    public void setJgrq(Date jgrq) {
        this.jgrq = jgrq;
    }
    
    public String getJxdzhgzId() {
        return jxdzhgzId;
    }
    
    public void setJxdzhgzId(String jxdzhgzId) {
        this.jxdzhgzId = jxdzhgzId;
    }

    @Override
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getJxzt() {
        return jxzt;
    }
    
    public void setJxzt(String jxzt) {
        this.jxzt = jxzt;
    }
    
    public Date getKgrq() {
        return kgrq;
    }
    
    public void setKgrq(Date kgrq) {
        this.kgrq = kgrq;
    }
    
    public Date getLcrq() {
        return lcrq;
    }
    
    public void setLcrq(Date lcrq) {
        this.lcrq = lcrq;
    }
    
    public Date getLdrq() {
        return ldrq;
    }
    
    public void setLdrq(Date ldrq) {
        this.ldrq = ldrq;
    }
    
    public String getRc() {
        return rc;
    }
    
    public void setRc(String rc) {
        this.rc = rc;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public String getRt() {
        return rt;
    }
    
    public void setRt(String rt) {
        this.rt = rt;
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
    
    public Double getZxgl() {
        return zxgl;
    }
    
    public void setZxgl(Double zxgl) {
        this.zxgl = zxgl;
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
        sb.append("(jxrb_id, jxdzhgz_id, cxbm, cxjc, ch, wxdbm, wxdmc, cxdbm, cxdmc, zxgl, rc, rt, ldrq, dcrq, kgrq, jgrq, lcrq, hdrq, jxzt, record_status, create_time, update_time, bz)");
        sb.append("values(");
        sb.append(SINGLE_QUOTE_MARK).append(idx).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(jxdzhgzId).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(cxbm).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(cxjc).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(ch).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(wxdbm).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(wxdmc).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(cxdbm).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(cxdmc).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(null == zxgl ? 0 : zxgl.intValue()).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(rc).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(rt).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(JwptBaseManager.toDate(ldrq)).append(JOINSTR);
        sb.append(JwptBaseManager.toDate(dcrq)).append(JOINSTR);
        sb.append(JwptBaseManager.toDate(kgrq)).append(JOINSTR);
        sb.append(JwptBaseManager.toDate(jgrq)).append(JOINSTR);
        sb.append(JwptBaseManager.toDate(lcrq)).append(JOINSTR);
        sb.append(JwptBaseManager.toDate(hdrq)).append(JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append((null == jxzt ? "" : jxzt)).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append(recordStatus).append(SINGLE_QUOTE_MARK_JOINSTR);
        sb.append(JwptBaseManager.toDate(createTime)).append(JOINSTR);
        sb.append(JwptBaseManager.toDate(updateTime)).append(JOINSTR);
        sb.append(SINGLE_QUOTE_MARK).append((null == bz ? "" : bz)).append(SINGLE_QUOTE_MARK);
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
        sb.append(" set ").append("(jxrb_id, jxdzhgz_id, cxbm, cxjc, ch, wxdbm, wxdmc, cxdbm, cxdmc, zxgl, rc, rt, ldrq, dcrq, kgrq, jgrq, lcrq, hdrq, jxzt, record_status, create_time, update_time, bz)");
        sb.append(" = (select jxrb_id, jxdzhgz_id, cxbm, cxjc, ch, wxdbm, wxdmc, cxdbm, cxdmc, zxgl, rc, rt, ldrq, dcrq, kgrq, jgrq, lcrq, hdrq, jxzt, record_status, create_time, update_time, bz from jwpt_jxrb where jxrb_id ='");
        sb.append(idx);
        sb.append("') where jxrb_id = '").append(idx).append("'");
        return sb.toString();
    }
    
}
