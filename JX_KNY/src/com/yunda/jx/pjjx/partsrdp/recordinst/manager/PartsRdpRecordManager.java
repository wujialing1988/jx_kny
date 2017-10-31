package com.yunda.jx.pjjx.partsrdp.recordinst.manager;

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecord;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordBean;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpRecord业务类,配件检修记录单实例
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "partsRdpRecordManager")
public class PartsRdpRecordManager extends JXBaseManager<PartsRdpRecord, PartsRdpRecord> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：分页查询，因为配件拆卸，拆卸下来的配件还可能会被继续拆卸，此方法使用了数据库递归查询
     * <li>创建人：何涛
     * <li>创建日期：2016-1-26
     * <li>修改人：何涛
     * <li>修改日期：2016-01-29
     * <li>修改内容：增加递归查询，增加排序
     * @param searchEntity 查询实体
     * @return PartsRdpRecord查询封装实体列表
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     */
    public Page<PartsRdpRecordManager.PartsRdpRecord> queryPageList(
        SearchEntity<com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecord> searchEntity) throws SecurityException, NoSuchFieldException {
        com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecord entity = searchEntity.getEntity();
        StringBuilder sb = new StringBuilder("SELECT * FROM (");
        sb.append("SELECT RECORD.*, RDP.PARTS_NAME, RDP.PARTS_NO, RDP.IDENTIFICATION_CODE, RDP.PARTS_ACCOUNT_IDX, RDP.PARENT_PARTS_ACCOUNT_IDX, ");
        
        sb.append(" (case when  ");
        //所有项个数
        sb.append(" (select count(1) from pjjx_record_card prc, pjjx_parts_rdp_record_card pprrc  where prc.record_card_no = pprrc.record_card_no and pprrc.rdp_idx = RECORD.Rdp_Idx and prc.record_idx = RECORD.RECORD_IDX) = 0 ");
        sb.append(" then '100%' else  ");
//      单条项已经是竣工状态个数
        sb.append(" (select count(1) from pjjx_record_card prc, pjjx_parts_rdp_record_card pprrc where prc.record_card_no = pprrc.record_card_no and pprrc.rdp_idx = RECORD.Rdp_Idx and status = 0402 and prc.record_idx = RECORD.RECORD_IDX)  ");
        sb.append(" / ");
        //所有项个数
        sb.append(" (select count(1) from pjjx_record_card prc, pjjx_parts_rdp_record_card pprrc  where prc.record_card_no = pprrc.record_card_no and pprrc.rdp_idx = RECORD.Rdp_Idx and prc.record_idx = RECORD.RECORD_IDX) ");
        sb.append(" * 100 || '%' end) as completPercent ");
        
        sb.append(" FROM PJJX_PARTS_RDP_RECORD RECORD, (SELECT A.*, B.PARENT_PARTS_ACCOUNT_IDX ");
        sb.append(" FROM (SELECT * FROM PJJX_PARTS_RDP T WHERE T.RECORD_STATUS = 0) A LEFT JOIN (SELECT T.PARTS_ACCOUNT_IDX, T.PARENT_PARTS_ACCOUNT_IDX FROM PJWZ_PARTS_DISMANTLE_REGISTER T WHERE T.RECORD_STATUS = 0) B ON A.PARTS_ACCOUNT_IDX = B.PARTS_ACCOUNT_IDX) RDP WHERE RECORD.RECORD_STATUS = 0 AND RDP.RECORD_STATUS = 0 AND RECORD.RDP_IDX = RDP.IDX  ");
        sb.append(")");
        sb.append(" START WITH RDP_IDX = '").append(entity.getRdpIDX()).append("'");
        sb.append(" CONNECT BY PRIOR PARTS_ACCOUNT_IDX = PARENT_PARTS_ACCOUNT_IDX");
        // 排序处理
        Order[] orders = searchEntity.getOrders();
        if (null != orders && orders.length > 0) {
            String[] order = orders[0].toString().split(" ");
            String sort = order[0];
//            if ("partsName".equals(sort) || "partsNo".equals(sort) || "identificationCode".equals(sort)) {
//                return null;
//            }
            //前台传递过来的排序方式 desc或者asc
            String dir = order[1];
            Class clazz = PartsRdpRecord.class;
            //通过传递过来需要排序的字段反射字段对象
            Field field = clazz.getDeclaredField(sort);
            //获取字段上，标签上的列名
            Column annotation = field.getAnnotation(Column.class);
            if (null != annotation) {
                sb.append(" ORDER BY ").append(annotation.name()).append(" ").append(dir);
            } else {
                sb.append(" ORDER BY ").append(sort).append(" ").append(dir);
            }
        } else {
            sb.append(" ORDER BY RECORD_NO");
        }
        //此处的总数别名必须是ROWCOUNT，封装方法有规定
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
        return this.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, PartsRdpRecordManager.PartsRdpRecord.class);
    }
    
    
    /**
     * <li>说明：分页查询，不查子配件
     * <li>创建人：张迪
     * <li>创建日期：2016-7-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity
     * @return
     * @throws SecurityException
     * @throws NoSuchFieldException
     */
    public Page<PartsRdpRecordBean> queryRecordPageList(
        SearchEntity<com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecord> searchEntity) throws SecurityException, NoSuchFieldException {
        com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecord entity = searchEntity.getEntity();
        StringBuilder sb = new StringBuilder("SELECT * FROM (");
        sb.append("SELECT RECORD.*, RDP.PARTS_NAME, RDP.PARTS_NO, RDP.IDENTIFICATION_CODE, RDP.PARTS_ACCOUNT_IDX, ");
        
        sb.append(" (case when  ");
        //所有项个数
        sb.append(" (select count(1) from pjjx_record_card prc, pjjx_parts_rdp_record_card pprrc  where prc.record_card_no = pprrc.record_card_no and pprrc.rdp_idx = RECORD.Rdp_Idx and prc.record_idx = RECORD.RECORD_IDX) = 0 ");
        sb.append(" then '100%' else  ");
//      单条项已经是竣工状态个数
        sb.append(" round((select count(1) from pjjx_record_card prc, pjjx_parts_rdp_record_card pprrc where prc.record_card_no = pprrc.record_card_no and pprrc.rdp_idx = RECORD.Rdp_Idx and status = 0402 and prc.record_idx = RECORD.RECORD_IDX)  ");
        sb.append(" / ");
        //所有项个数
        sb.append(" (select count(1) from pjjx_record_card prc, pjjx_parts_rdp_record_card pprrc  where prc.record_card_no = pprrc.record_card_no and pprrc.rdp_idx = RECORD.Rdp_Idx and prc.record_idx = RECORD.RECORD_IDX) ");
        sb.append(" * 100,2) || '%' end) as completPercent ");
        
        sb.append(" FROM PJJX_PARTS_RDP_RECORD RECORD inner join PJJX_PARTS_RDP RDP ON RECORD.RDP_IDX =RDP.IDX ");
        
        sb.append("WHERE  RECORD.RECORD_STATUS = 0 AND RDP.RECORD_STATUS = 0 AND  RECORD.RDP_IDX = '").append(entity.getRdpIDX()).append("')");
        // 排序处理
        Order[] orders = searchEntity.getOrders();
        if (null != orders && orders.length > 0) {
            String[] order = orders[0].toString().split(" ");
            String sort = order[0];

            //前台传递过来的排序方式 desc或者asc
            String dir = order[1];
            Class clazz = PartsRdpRecord.class;
            //通过传递过来需要排序的字段反射字段对象
            Field field = clazz.getDeclaredField(sort);
            //获取字段上，标签上的列名
            Column annotation = field.getAnnotation(Column.class);
            if (null != annotation) {
                sb.append(" ORDER BY ").append(annotation.name()).append(" ").append(dir);
            } else {
                sb.append(" ORDER BY ").append(sort).append(" ").append(dir);
            }
        } else {
            sb.append(" ORDER BY RECORD_NO");
        }
        //此处的总数别名必须是ROWCOUNT，封装方法有规定
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
        return this.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, PartsRdpRecordBean.class);
    }
    
    /**
     * <li>标题: PartsRdpRecord查询封装实体类
     * <li>说明: 类的功能描述
     * <li>创建人：何涛
     * <li>创建日期：2016-1-26
     * <li>修改人: 
     * <li>修改日期：
     * <li>修改内容：
     * <li>版权: Copyright (c) 2008 运达科技公司
     * @author 信息系统事业部检修系统项目组
     * @version 3.2.4
     */
    @Entity
    @JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
    public static final class PartsRdpRecord {
        
        /* idx主键 */
        @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
        @Id
        @GeneratedValue(generator = "uuid_id_generator")
        private String idx;
        
        /* 作业主键 */
        @Column(name = "Rdp_IDX")
        private String rdpIDX;
        
        /* 记录单主键 */
        @Column(name = "Record_IDX")
        private String recordIDX;
        
        /* 配件主键 */
        @Column(name = "PARTS_ACCOUNT_IDX")
        private String partsAccountIDX;
        
        /* 上级配件主键 */
        @Column(name = "PARENT_PARTS_ACCOUNT_IDX")
        private String parentPartsAccountIDX;
        
        /* 记录单编号 */
        @Column(name = "Record_No")
        private String recordNo;
        
        /* 记录单名称 */
        @Column(name = "Record_Name")
        private String recordName;
        
        /* 记录单描述 */
        @Column(name = "Record_Desc")
        private String recordDesc;
        
        /* 表示此条记录的状态：0为表示未删除；1表示删除 */
        @Column(name = "Record_Status")
        private Integer recordStatus;
        
        /* 报表主键 */
        @Column(name = "ReportTmpl_Manage_IDX")
        private String reportTmplManageIDX;
        
        /* 配件名称 */
        @Column(name = "PARTS_NAME")
        private String partsName;/* 配件编号 */
        
        @Column(name = "PARTS_NO")
        private String partsNo;
        
        /* 配件识别码 */
        @Column(name = "IDENTIFICATION_CODE")
        private String identificationCode;
        
        /* 站点标识，为了同步数据而使用 */
        @Column(updatable = false)
        private String siteID;
        
        /* 创建人 */
        @Column(updatable = false)
        private Long creator;
        
        /* 创建时间 */
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "Create_Time", updatable = false)
        private java.util.Date createTime;
        
        /* 修改人 */
        private Long updator;
        
        /* 修改时间 */
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "Update_Time")
        private java.util.Date updateTime;
        
        /* 完成百分比 */
        @Column(name = "completPercent")
        private String completPercent;
        
        public String getCompletPercent() {
            return completPercent;
        }

        
        public void setCompletPercent(String completPercent) {
            this.completPercent = completPercent;
        }

        public java.util.Date getCreateTime() {
            return createTime;
        }
        
        public void setCreateTime(java.util.Date createTime) {
            this.createTime = createTime;
        }
        
        public Long getCreator() {
            return creator;
        }
        
        public void setCreator(Long creator) {
            this.creator = creator;
        }
        
        public String getIdentificationCode() {
            return identificationCode;
        }
        
        public void setIdentificationCode(String identificationCode) {
            this.identificationCode = identificationCode;
        }
        
        public String getIdx() {
            return idx;
        }
        
        public void setIdx(String idx) {
            this.idx = idx;
        }
        
        public String getPartsName() {
            return partsName;
        }
        
        public void setPartsName(String partsName) {
            this.partsName = partsName;
        }
        
        public String getPartsNo() {
            return partsNo;
        }
        
        public void setPartsNo(String partsNo) {
            this.partsNo = partsNo;
        }
        
        public String getRdpIDX() {
            return rdpIDX;
        }
        
        public void setRdpIDX(String rdpIDX) {
            this.rdpIDX = rdpIDX;
        }
        
        public String getRecordDesc() {
            return recordDesc;
        }
        
        public void setRecordDesc(String recordDesc) {
            this.recordDesc = recordDesc;
        }
        
        public String getRecordIDX() {
            return recordIDX;
        }
        
        public void setRecordIDX(String recordIDX) {
            this.recordIDX = recordIDX;
        }
        
        public String getParentPartsAccountIDX() {
            return parentPartsAccountIDX;
        }
        
        public void setParentPartsAccountIDX(String parentPartsAccountIDX) {
            this.parentPartsAccountIDX = parentPartsAccountIDX;
        }
        
        public String getPartsAccountIDX() {
            return partsAccountIDX;
        }
        
        public void setPartsAccountIDX(String partsAccountIDX) {
            this.partsAccountIDX = partsAccountIDX;
        }
        
        public String getRecordName() {
            return recordName;
        }
        
        public void setRecordName(String recordName) {
            this.recordName = recordName;
        }
        
        public String getRecordNo() {
            return recordNo;
        }
        
        public void setRecordNo(String recordNo) {
            this.recordNo = recordNo;
        }
        
        public Integer getRecordStatus() {
            return recordStatus;
        }
        
        public void setRecordStatus(Integer recordStatus) {
            this.recordStatus = recordStatus;
        }
        
        public String getReportTmplManageIDX() {
            return reportTmplManageIDX;
        }
        
        public void setReportTmplManageIDX(String reportTmplManageIDX) {
            this.reportTmplManageIDX = reportTmplManageIDX;
        }
        
        public String getSiteID() {
            return siteID;
        }
        
        public void setSiteID(String siteID) {
            this.siteID = siteID;
        }
        
        public java.util.Date getUpdateTime() {
            return updateTime;
        }
        
        public void setUpdateTime(java.util.Date updateTime) {
            this.updateTime = updateTime;
        }
        
        public Long getUpdator() {
            return updator;
        }
        
        public void setUpdator(Long updator) {
            this.updator = updator;
        }
        
    }
    
}
