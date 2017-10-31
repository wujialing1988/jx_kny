package com.yunda.jx.pjjx.partsrdp.qcinst.manager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;
import com.yunda.jx.pjjx.base.qcitemdefine.entity.QCItem;
import com.yunda.jx.pjjx.partsrdp.IPartsRdpStatus;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsQRBean;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsQRRdpResult;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsQRRecordResult;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQR;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpRecordZljcBean;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpZljyBean;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecord;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 配件检修质检查询业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-10-12
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.3
 */
@Service(value = "partsRdpQRQueryManager")
public class PartsRdpQRQueryManager extends JXBaseManager<PartsRdpQR, PartsRdpQR>{
    
    /**
     * <li>说明：查询当前人员的质量检查项列表
     * <li>创建人：程锐
     * <li>创建日期：2015-10-10
     * <li>修改人：何涛
     * <li>修改日期：2015-03-10
     * <li>修改内容：重构
     * @param empid 用户id
     * @param start 开始行
     * @param limit 本页记录数
     * @param mode 质检类型，1：抽检、2：必检
     * @param searchEntity 配件检修质检实体包装类
     * @return 当前人员的质量检查项列表
     * @throws Exception
     */
    public Page getQCPageList(Long empid, int start, int limit, String mode, SearchEntity<PartsQRBean> searchEntity) throws Exception {        
        String querySql = getQuerySql(empid, mode, searchEntity, "getQCList");
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + querySql.substring(querySql.indexOf("FROM"));
        return this.queryPageList(totalSql, querySql, start, limit, false, PartsQRBean.class);
        
    }
    
    /**
     * <li>说明：根据查询条件获取查询sql
     * <li>创建人：程锐
     * <li>创建日期：2015-10-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param empid 用户id
     * @param mode 质检类型，1：抽检、2：必检
     * @param searchEntity 配件检修质检实体包装类
     * @param sqlName 查询sql名称
     * @return 查询sql
     * @throws Exception
     */
    public String getQuerySql(Long empid, String mode, SearchEntity<PartsQRBean> searchEntity, String sqlName) throws Exception {
        String checkString = "";
        String workCardStatus = " STATUS  = '" + IPartsRdpStatus.CONST_STR_STATUS_ZLJYZ + "'";
        if((QCItem.CONST_INT_CHECK_WAY_BJ + "").equals(mode)) {         
            checkString = " AND CHECK_WAY = " + QCItem.CONST_INT_CHECK_WAY_BJ ;
        } else if((QCItem.CONST_INT_CHECK_WAY_CJ + "").equals(mode)) {
            checkString = " AND CHECK_WAY = " + QCItem.CONST_INT_CHECK_WAY_CJ ;
        }
        String selectSql = SqlMapUtil.getSql("pjjx-qc:" + sqlName);
        String fromSql = SqlMapUtil.getSql("pjjx-qc:getQCListFrom")
                                   .replace("#STATUS_DCL#", PartsRdpQR.CONST_STR_STATUS_DCL + "")
                                   .replace("#CHECKWAY#", checkString)
                                   .replace("#STATUS_FINISHED#", workCardStatus)
                                   .replace("#CURRENTEMPID#", empid + "");       
        
        StringBuilder multyAwhere = new StringBuilder();
        if (!StringUtil.isNullOrBlank(searchEntity.getEntity().getRdpIDX()))
            multyAwhere.append(" AND B.RDP_IDX = '").append(searchEntity.getEntity().getRdpIDX()).append("' ");
        if (!StringUtil.isNullOrBlank(searchEntity.getEntity().getQcItemName()))
            multyAwhere.append(" AND A.QC_ITEM_NAME = '").append(searchEntity.getEntity().getQcItemName()).append("' ");
        return selectSql + " " + fromSql + " " + multyAwhere;
    }
    
    /**
     * <li>说明：按操作者查询待处理的质检项数量
     * <li>创建人：程锐
     * <li>创建日期：2016-2-1
     * <li>修改人： 张迪
     * <li>修改日期：2016-7-3
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @return 待处理的质检项数量
     * @throws Exception
     */
    public int queryDCLQCCount(long operatorid) throws Exception {
        OmEmployeeSelectManager omEmployeeSelectManager = 
            (OmEmployeeSelectManager) Application.getSpringApplicationContext().getBean("omEmployeeSelectManager");
        OmEmployee emp = omEmployeeSelectManager.findEmpByOperator(operatorid);
        PartsQRBean entity = new PartsQRBean();
        SearchEntity<PartsQRBean> searchEntity = new SearchEntity<PartsQRBean>(entity, null, null, null);
//      String querySql = getQuerySql(emp.getEmpid(), "", searchEntity, "getQCList");
//      List list = daoUtils.executeSqlQuery(querySql);
      // 修改查询待处理质检项数量方法
        String checkString = "";
        String workCardStatus = " STATUS  = '" + IPartsRdpStatus.CONST_STR_STATUS_ZLJYZ + "'";
        
        String selectSql = SqlMapUtil.getSql("pjjx-qc:" + "getQCList");
        String fromSql = SqlMapUtil.getSql("pjjx-qc:getQCCountFrom")
                                   .replace("#STATUS_DCL#", PartsRdpQR.CONST_STR_STATUS_DCL + "")
                                   .replace("#CHECKWAY#", checkString)
                                   .replace("#STATUS_FINISHED#", workCardStatus)
                                   .replace("#CURRENTEMPID#", emp.getEmpid() + "");       
        
        StringBuilder multyAwhere = new StringBuilder();
        if (!StringUtil.isNullOrBlank(searchEntity.getEntity().getRdpIDX()))
            multyAwhere.append(" AND B.RDP_IDX = '").append(searchEntity.getEntity().getRdpIDX()).append("' ");
        if (!StringUtil.isNullOrBlank(searchEntity.getEntity().getQcItemName()))
            multyAwhere.append(" AND A.QC_ITEM_NAME = '").append(searchEntity.getEntity().getQcItemName()).append("' ");    
        String querySql = selectSql + " " + fromSql + " " + multyAwhere;       
        List list = daoUtils.executeSqlQuery(querySql);
       return CommonUtil.getListSize(list);
    }
    
 

    
    /**
     * <li>说明：节点是否还有未处理的质检项
     * <li>创建人：程锐
     * <li>创建日期：2016-2-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDXS 节点IDX
     * @return true 节点还有未处理的质检项 false 无
     */
    public boolean hasDCLQCByNode(String nodeIDXS) {
        int qcCount = getWClQCCountByNode(nodeIDXS);
        return qcCount > 0;
    }
    
    /**
     * <li>说明：配件检修计划是否还有未处理的质检项
     * <li>创建人：程锐
     * <li>创建日期：2016-2-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 配件检修计划IDX
     * @return true 配件检修计划还有未处理的质检项 false 无
     */
    public boolean hasDCLQCByRdp(String rdpIDX) {
        int qcCount = getWClQCCountByRdp(rdpIDX);
        return qcCount > 0;
    }
    
    /**
     * <li>说明：按配件检修计划查询待处理的质检项数量
     * <li>创建人：程锐
     * <li>创建日期：2016-2-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 配件检修计划IDX
     * @return 按配件检修计划查询待处理的质检项数量
     */
    public int getWClQCCountByRdp(String rdpIDX) {        
        return daoUtils.getCountSQL(getDCLQCListByRdp(rdpIDX));
    }
    
    /**
     * <li>说明：按配件检修节点查询待处理的质检项数量
     * <li>创建人：程锐
     * <li>创建日期：2016-2-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDXS 节点IDX
     * @return 按配件检修节点查询待处理的质检项数量
     */
    public int getWClQCCountByNode(String nodeIDXS) {        
        return daoUtils.getCountSQL(getDCLQCListByNode(nodeIDXS));
    }
    
    /**
     * <li>说明：按配件检修节点查询待处理的质检项列表的hql
     * <li>创建人：程锐
     * <li>创建日期：2016-2-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDXS 节点IDX
     * @return 按配件检修节点查询待处理的质检项列表的hql
     */
    private String getDCLQCListByNode(String nodeIDXS) {
        StringBuilder sb = new StringBuilder();
        String sql = SqlMapUtil.getSql("pjjx-qc:getQCListByNode")
                               .replace("#nodeIDXS#", nodeIDXS);        
        sb.append(sql)
          .append(" AND STATUS IN ('")
          .append(PartsRdpQR.CONST_STR_STATUS_DCL)
          .append("')")
          .append(" AND CHECK_WAY = ")
          .append(QCItem.CONST_INT_CHECK_WAY_BJ);
        return sb.toString();
    }
        
    /**
     * <li>说明：按配件检修计划查询待处理的质检项列表的hql
     * <li>创建人：程锐
     * <li>创建日期：2016-2-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 配件检修计划IDX
     * @return 按配件检修计划查询待处理的质检项列表的hql
     */
    private String getDCLQCListByRdp(String rdpIDX) {
        StringBuilder sb = new StringBuilder();
        String sql = SqlMapUtil.getSql("pjjx-qc:getQCListByRdp")
                               .replace("#rdpIDX#", rdpIDX);        
        sb.append(sql)
          .append(" AND STATUS IN ('")
          .append(PartsRdpQR.CONST_STR_STATUS_DCL)
          .append("')")
          .append(" AND CHECK_WAY = ")
          .append(QCItem.CONST_INT_CHECK_WAY_BJ);
        return sb.toString();
    }

    /**
     * <li>说明：（新）待检验工单汇总
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity
     * @return Page<PartsRdpZljyBean>
     */
    @SuppressWarnings("unchecked")
    public Page<PartsRdpZljyBean> findRdpPageList(SearchEntity<PartsRdp> searchEntity) {
        OmEmployee omEmployee = SystemContext.getOmEmployee();  // 设置质量人员信息
        List<PartsQRRdpResult> list = this.getPartsQRRdpResult(omEmployee.getEmpid()+"");
        String idxs = "";
        HashMap checkedResult = new HashMap<String, ArrayList<PartsQRRdpResult>>();
        ArrayList<String> idxsList = new ArrayList<String>();
        if(list == null && list.size() > 0){
            return null ;
        }
        for (PartsQRRdpResult result : list) {
            String rdpIdx = result.getRdpIdx();
            if(!idxsList.contains(rdpIdx)){
                idxsList.add(rdpIdx);
            }
            if(checkedResult.get(rdpIdx) == null){
                List<PartsQRRdpResult> partsQRRdpResult = new ArrayList<PartsQRRdpResult>();
                partsQRRdpResult.add(result);
                checkedResult.put(rdpIdx, partsQRRdpResult);
            }else{
                List rs = (ArrayList<PartsQRRdpResult>)checkedResult.get(rdpIdx);
                if(!rs.contains(result)){
                    rs.add(result);
                }
            }
        }
        for (String idx : idxsList) {
            idxs += "'"+idx+"',";
        }
        if(!StringUtil.isNullOrBlank(idxs)){
            idxs = idxs.substring(0, idxs.length()-1);
        }else{
            return new Page<PartsRdpZljyBean>(0,null);
        }
        Page<PartsRdpZljyBean> page = findRdpPageListByIdxs(idxs, searchEntity);
        List<PartsRdpZljyBean> rdplist = page.getList();
        for (PartsRdpZljyBean entity : rdplist) {
            String checkItem = "" ;
            // 组装checkItem
            if(checkedResult.get(entity.getIdx()) != null){
                ArrayList<PartsQRRdpResult> rs = (ArrayList<PartsQRRdpResult>)checkedResult.get(entity.getIdx());
                for (PartsQRRdpResult r : rs) {
                    checkItem += r.getQcItemName()+"("+r.getQrCount()+")&nbsp;";
                }
            }
            entity.setCheckItem(checkItem);
        }
        return page;
    }
    
    /**
     * <li>说明：根据idx集合查询配件作业计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idxs
     * @param searchEntity
     * @return
     */
    public Page<PartsRdpZljyBean> findRdpPageListByIdxs(String idxs ,SearchEntity<PartsRdp> searchEntity){
        PartsRdp rdp = searchEntity.getEntity();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT A.* FROM PJJX_PARTS_RDP A, PJWZ_PARTS_ACCOUNT B WHERE A.RECORD_STATUS = 0 AND B.RECORD_STATUS = 0 AND A.PARTS_ACCOUNT_IDX = B.IDX");
        // 过滤掉传入的ID
        sb.append(" AND A.IDX in ("+idxs+")");
        // 作业工单状态【检修中】【待验收】
        if (StringUtil.isNullOrBlank(rdp.getStatus())) {
            sb.append(" AND A.STATUS IN ('");
            sb.append(PartsRdp.STATUS_JXZ).append("','");
            sb.append(PartsRdp.STATUS_DYS);   
            sb.append("')");
        } 
        else {
            sb.append(" AND A.STATUS = '").append(rdp.getStatus()).append("'");
        }
        // 配件识别码和配件编号使用同一个字段进行匹配时的处理
        if(!StringUtil.isNullOrBlank(rdp.getPartsNo()) && !StringUtil.isNullOrBlank(rdp.getIdentificationCode())) {
            sb.append(" AND (A.PARTS_NO LIKE '%").append(rdp.getPartsNo()).append(Constants.LIKE_PIPEI);
            sb.append(" OR (A.IDENTIFICATION_CODE LIKE '%").append(rdp.getIdentificationCode()).append(Constants.LIKE_PIPEI);
            sb.append(" OR A.PARTS_ACCOUNT_IDX IN (SELECT PARTS_ACCOUNT_IDX FROM PJJX_RECORDCODE_BIND WHERE RECORD_CODE LIKE '%").append(rdp.getIdentificationCode()).append("%')");
            sb.append("))");
        }
        // 查询条件 - 下车车型号
        if (!StringUtil.isNullOrBlank(rdp.getUnloadTrainType())) {
            sb.append(" AND LOWER(A.UNLOAD_TRAINTYPE || A.UNLOAD_TRAINNO) Like '%").append(rdp.getUnloadTrainType().toLowerCase()).append("%'");
        }
        // 查询条件 - 名称规格型号
        if (!StringUtil.isNullOrBlank(rdp.getSpecificationModel())) {
            sb.append(" AND LOWER(A.PARTS_NAME || A.SPECIFICATION_MODEL) Like '%").append(rdp.getSpecificationModel().toLowerCase()).append("%'");
        }
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
        Page<PartsRdpZljyBean> page = super.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, PartsRdpZljyBean.class);
        return page;
    }
    
    /**
     * <li>说明：查询配件检修作业检测项集合
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param empId 人员ID
     * @return
     */
    public List getPartsQRRdpResult(String empId){
        String sql = SqlMapUtil.getSql("pjjx-qc:getQRRdpList")
        .replace("#EMP_ID#", empId); 
        return this.daoUtils.executeSqlQueryEntity(sql, PartsQRRdpResult.class);
    }

    /**
     * <li>说明：新）待检验记录单
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity
     * @return
     */
    @SuppressWarnings("unchecked")
    public Page<PartsRdpRecordZljcBean> queryRecordPageList(SearchEntity<PartsRdpRecord> searchEntity,boolean isAll) throws SecurityException, NoSuchFieldException {
        PartsRdpRecord record = searchEntity.getEntity();
        OmEmployee omEmployee = SystemContext.getOmEmployee();  // 设置质量人员信息
        List<PartsQRRecordResult> list = this.getPartsQRRecordResult(omEmployee.getEmpid()+"",record.getRdpIDX());
        String idxs = "";
        HashMap checkedResult = new HashMap<String, ArrayList<PartsQRRecordResult>>();
        ArrayList<String> idxsList = new ArrayList<String>();
        if(list == null && list.size() > 0){
            return null ;
        }
        for (PartsQRRecordResult result : list) {
            String recordIdx = result.getRdpRecordIdx();
            if(!idxsList.contains(recordIdx)){
                idxsList.add(recordIdx);
            }
            if(checkedResult.get(recordIdx) == null){
                List<PartsQRRecordResult> partsQRRdpResult = new ArrayList<PartsQRRecordResult>();
                partsQRRdpResult.add(result);
                checkedResult.put(recordIdx, partsQRRdpResult);
            }else{
                List rs = (ArrayList<PartsQRRecordResult>)checkedResult.get(recordIdx);
                if(!rs.contains(result)){
                    rs.add(result);
                }
            }
        }
        for (String idx : idxsList) {
            idxs += "'"+idx+"',";
        }
        if(!StringUtil.isNullOrBlank(idxs)){
            idxs = idxs.substring(0, idxs.length()-1);
        } 
        Page<PartsRdpRecordZljcBean> page = null ;
        if(isAll){
            page = this.queryRecordPageListByIdxs(searchEntity, "");
        }else{
            page = this.queryRecordPageListByIdxs(searchEntity, idxs);
        }
        List<PartsRdpRecordZljcBean> recordlist = page.getList();
        for (PartsRdpRecordZljcBean entity : recordlist) {
            String checkItem = "" ;
            // 组装checkItem
            if(checkedResult.get(entity.getIdx()) != null){
                List<PartsQRRecordResult> rs = (List<PartsQRRecordResult>)checkedResult.get(entity.getIdx());
                for (PartsQRRecordResult r : rs) {
                    checkItem += r.getQcItemName()+"("+r.getQrCount()+") ";
                }
                entity.setPartsQRRecordResultList(rs);
            }
            entity.setCheckItem(checkItem);
        }
        return page;
    }
    
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 
     * @param idxs
     * @param isAll
     * @return
     * @throws SecurityException
     * @throws NoSuchFieldException
     */
    public Page<PartsRdpRecordZljcBean> queryRecordPageListByIdxs(SearchEntity<PartsRdpRecord> searchEntity,String idxs) throws SecurityException, NoSuchFieldException {
        PartsRdpRecord entity = searchEntity.getEntity();
        StringBuilder sb = new StringBuilder("SELECT * FROM (");
        sb.append("SELECT RECORD.*, RDP.PARTS_NAME, RDP.PARTS_NO, RDP.IDENTIFICATION_CODE, RDP.PARTS_ACCOUNT_IDX, ");
        
        sb.append(" (case when  ");
        //所有项个数
        sb.append(" (select count(1) from pjjx_record_card prc, pjjx_parts_rdp_record_card pprrc  where prc.record_card_no = pprrc.record_card_no and pprrc.rdp_idx = RECORD.Rdp_Idx and prc.record_idx = RECORD.RECORD_IDX) = 0 ");
        sb.append(" then '100%' else  ");
//      单条项已经是竣工状态个数
        sb.append(" round ((select count(1) from pjjx_record_card prc, pjjx_parts_rdp_record_card pprrc where prc.record_card_no = pprrc.record_card_no and pprrc.rdp_idx = RECORD.Rdp_Idx and status = 0402 and prc.record_idx = RECORD.RECORD_IDX)  ");
        sb.append(" / ");
        //所有项个数
        sb.append(" (select count(1) from pjjx_record_card prc, pjjx_parts_rdp_record_card pprrc  where prc.record_card_no = pprrc.record_card_no and pprrc.rdp_idx = RECORD.Rdp_Idx and prc.record_idx = RECORD.RECORD_IDX) ");
        sb.append(" * 100,2) || '%' end) as completPercent ");
        
        sb.append(" FROM PJJX_PARTS_RDP_RECORD RECORD inner join PJJX_PARTS_RDP RDP ON RECORD.RDP_IDX =RDP.IDX ");
        
        sb.append("WHERE  RECORD.RECORD_STATUS = 0 AND RDP.RECORD_STATUS = 0 AND  RECORD.RDP_IDX = '").append(entity.getRdpIDX()).append("'");
        // 如果所传入的idxs不为空则查所传入的ID
        if(!StringUtil.isNullOrBlank(idxs)){
            sb.append(" and RECORD.IDX in ("+idxs+") ");
        }
        sb.append(")");
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
        return this.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, PartsRdpRecordZljcBean.class);
    }
    
    
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param empId 人员id
     * @param rdpIdx 作业计划ID
     * @return
     */
    public List getPartsQRRecordResult(String empId,String rdpIdx){
        String sql = SqlMapUtil.getSql("pjjx-qc:getQRRecordList")
        .replace("#EMP_ID#", empId)
        .replace("#RDP_IDX#", rdpIdx); 
        return this.daoUtils.executeSqlQueryEntity(sql, PartsQRRecordResult.class);
    }
}
