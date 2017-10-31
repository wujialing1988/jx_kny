package com.yunda.freight.jx.classwarning.manager;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.freight.jx.classwarning.entity.RepairClassWarning;
import com.yunda.freight.jx.classwarning.entity.RepairClassWarningBean;
import com.yunda.jxpz.utils.SystemConfigUtil;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 修程预警业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-05-04 15:14:11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings({"unchecked", "unused"})
@Service("repairClassWarningManager")
public class RepairClassWarningManager extends JXBaseManager<RepairClassWarning, RepairClassWarning> {
    
    
    /**
     * <li>说明：同步客车预警数据
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    public synchronized void synchronizeKCWarningData(){
        String limitValue = SystemConfigUtil.getValue("knySys.freightSys.synchronizeKCWarningDataDispatch");
        if(!StringUtil.isNullOrBlank(limitValue)){
            String insertKCRepairClassWarningSql = SqlMapUtil.getSql("kny-base:insertKCRepairClassWarning").replace("#LIMIT_VALUE#", limitValue);
            daoUtils.executeSql(insertKCRepairClassWarningSql);
        }
    }
    
    
    
    /**
     * <li>说明：查询客车修程预警
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity
     * @return
     */
    public Page<RepairClassWarningBean> findKCRepairClassWarningList(SearchEntity<RepairClassWarningBean> searchEntity){
        RepairClassWarningBean entity = searchEntity.getEntity();
        String sql = SqlMapUtil.getSql("kny-base:findKCRepairClassWarningList");
        // 全匹配模糊
        if(!StringUtil.isNullOrBlank(entity.getTrainNo())){
            sql += " and ( w.train_type like '%"+entity.getTrainNo()+"%'" ;
            sql += " or w.train_no like '%"+entity.getTrainNo()+"%' )";
        }
        sql += " order by s.max_running_km - (k.new_running_km + k.recently_running_km)" ;
        String totalSql = "Select count(*) as rowcount "+ sql.substring(sql.indexOf("from"));
        return this.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, RepairClassWarningBean.class);
    }
    
    
    /**
     * <li>说明：同步货车预警数据
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    public synchronized void synchronizeHCWarningData(){
        String limitValue = SystemConfigUtil.getValue("knySys.freightSys.synchronizeHCWarningDataDispatch");
        if(!StringUtil.isNullOrBlank(limitValue)){
            String insertKCRepairClassWarningSql = SqlMapUtil.getSql("kny-base:insertHCRepairClassWarning").replace("#LIMIT_VALUE#", limitValue);
            daoUtils.executeSql(insertKCRepairClassWarningSql);
        }
    }
    
    /**
     * <li>说明：查询客车修程预警
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity
     * @return
     */
    public Page<RepairClassWarningBean> findHCRepairClassWarningList(SearchEntity<RepairClassWarningBean> searchEntity){
        RepairClassWarningBean entity = searchEntity.getEntity();
        String sql = SqlMapUtil.getSql("kny-base:findHCRepairClassWarningList");
        // 全匹配模糊
        if(!StringUtil.isNullOrBlank(entity.getTrainNo())){
            sql += " and ( w.train_type like '%"+entity.getTrainNo()+"%'" ;
            sql += " or w.train_no like '%"+entity.getTrainNo()+"%' )";
        }
        sql += " order by s.max_running_km - (trunc(sysdate) - trunc(t.leave_date))" ;
        String totalSql = "Select count(*) as rowcount "+ sql.substring(sql.indexOf("from"));
        return this.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, RepairClassWarningBean.class);
    }
    
   
}
