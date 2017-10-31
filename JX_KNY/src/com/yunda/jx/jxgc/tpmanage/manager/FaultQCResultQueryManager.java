package com.yunda.jx.jxgc.tpmanage.manager;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;
import com.yunda.jx.jxgc.base.tpqcitemdefine.entity.TPQCItemDefine;
import com.yunda.jx.jxgc.tpmanage.entity.FaultQCListBean;
import com.yunda.jx.jxgc.tpmanage.entity.FaultQCResult;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicket;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 提票质检查询业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-7-14
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
@Service(value = "faultQCResultQueryManager")
public class FaultQCResultQueryManager extends JXBaseManager<FaultQCListBean, FaultQCListBean> {
    
    /**
     * <li>说明：查询当前人员的提票质量检查项列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param empid 用户id
     * @param start 开始行
     * @param limit 本页记录数
     * @param mode 提票质检类型
     * @param queryString 查询字符串
     * @return 当前人员的提票质量检查项列表
     * @throws Exception
     */
    public Page getQCPageList(Long empid, int start, int limit, String mode, String queryString) throws Exception {        
        String querySql = getQuerySql(empid, mode, queryString, "getFaultQCList");
        String totalSql = "select count(1) from (" + querySql + ")";
        Page<FaultQCListBean> page = findPageList(totalSql, querySql, start, limit, null, null);
        return page;
        
    }
    
    /**
     * <li>说明：根据查询条件获取查询sql
     * <li>创建人：程锐
     * <li>创建日期：2015-7-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param empid 用户id
     * @param mode 质检类型
     * @param queryString 查询字符串
     * @param sqlName 查询sql名称
     * @return 查询sql
     * @throws Exception
     */
    public String getQuerySql(Long empid, String mode, String queryString, String sqlName) throws Exception {
        String checkString = "";
        if((TPQCItemDefine.CONST_INT_CHECK_WAY_BJ + "").equals(mode)) {          
            checkString = " AND CHECK_WAY = " + TPQCItemDefine.CONST_INT_CHECK_WAY_BJ ;
        } else if((TPQCItemDefine.CONST_INT_CHECK_WAY_CJ + "").equals(mode)) {
            checkString = " AND CHECK_WAY = " + TPQCItemDefine.CONST_INT_CHECK_WAY_CJ ;
        }
        String selectSql = SqlMapUtil.getSql("jxgc-tp:" + sqlName);
        String fromSql = SqlMapUtil.getSql("jxgc-tp:getFaultQCListFrom")
                                   .replace("#STATUS_DCL#", FaultQCResult.STATUS_DCL + "")
                                   .replace("#CHECKWAY#", checkString)
                                   .replace("#STATUS_HANDLING#", TrainWorkPlan.STATUS_HANDLING)
                                   .replace("#STATUS_OVER#", FaultTicket.STATUS_OVER + "")
                                   .replace("#CURRENTEMPID#", empid + "");
        StringBuilder multyAwhere = new StringBuilder();
        if (!StringUtil.isNullOrBlank(queryString)) {
            Map queryMap = JSONUtil.read(queryString, Map.class); 
            
            if(queryMap.containsKey("trainTypeShortName")) {
                String trainTypeShortName = String.valueOf(queryMap.get("trainTypeShortName"));
                if(!StringUtil.isNullOrBlank(trainTypeShortName)){
                    multyAwhere.append(" AND C.TRAIN_TYPE_SHORTNAME = '").append(trainTypeShortName).append("' ");
                }
            }
            if(queryMap.containsKey("trainNo")) {
                String trainNo = String.valueOf(queryMap.get("trainNo"));
                if(!StringUtil.isNullOrBlank(trainNo)){
                    multyAwhere.append(" AND C.TRAIN_NO LIKE '%").append(trainNo).append("%' ");
                }
            }
            if(queryMap.containsKey("ticketCode")) {
                String ticketCode = String.valueOf(queryMap.get("ticketCode"));
                if(!StringUtil.isNullOrBlank(ticketCode)){
                    multyAwhere.append(" AND D.TICKET_CODE LIKE '%").append(ticketCode).append("%' ");
                }
            }
            if(queryMap.containsKey("checkItemName")) {
                String checkItemName = String.valueOf(queryMap.get("checkItemName"));
                if(!StringUtil.isNullOrBlank(checkItemName)){
                    multyAwhere.append(" AND A.CHECK_ITEM_NAME LIKE '%").append(checkItemName).append("%' ");
                }
            }
            // 客货类型
            if(queryMap.containsKey("vehicleType")) {
                String vehicleType = String.valueOf(queryMap.get("vehicleType"));
                if(!StringUtil.isNullOrBlank(vehicleType)){
                    multyAwhere.append(" AND D.T_VEHICLE_TYPE = '").append(vehicleType).append("' ");
                }
            }
            
        }
        
        return selectSql + " " + fromSql + multyAwhere.toString();
    }
    
    /**
     * <li>说明：获取提票质量检验数量
     * <li>创建人：程锐
     * <li>创建日期：2015-7-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @return 提票质量检验数量
     * @throws Exception
     */
    public int queryQCCount(long operatorid) throws Exception {
        OmEmployee emp = getOmEmployeeSelectManager().findEmpByOperator(operatorid);
        String querySql = getQuerySql(emp.getEmpid(), "", "", "getFaultQCList");
        List list = daoUtils.executeSqlQuery(querySql);
        return CommonUtil.getListSize(list);
    }
    
    protected OmEmployeeSelectManager getOmEmployeeSelectManager() {
        return (OmEmployeeSelectManager) Application.getSpringApplicationContext().getBean("omEmployeeSelectManager");
    }
}
