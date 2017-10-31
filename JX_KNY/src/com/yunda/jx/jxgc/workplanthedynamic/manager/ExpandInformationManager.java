package com.yunda.jx.jxgc.workplanthedynamic.manager;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jxgc.workplanthedynamic.entity.ExpandInformation;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 重要信息业务类
 * <li>创建人：张迪
 * <li>创建日期：2017-3-6
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "expandInformationManager")
public class ExpandInformationManager  extends JXBaseManager<ExpandInformation, ExpandInformation> {
   

    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：张迪
     * <li>创建日期：2017-3-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchDate 生成日期
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void insertTheExpandInformation(String searchDate) throws Exception {
        Long operaterId = SystemContext.getAcOperator().getOperatorid();
        String siteID = EntityUtil.findSysSiteId("");
        this.deleteTodayExpandInformation(searchDate);
        String sql = SqlMapUtil.getSql("jxgc-rdp:generateTheExpandInformation")
                                .replace("#creator#", operaterId.toString())
                                .replace("#dicttypeid#", "WORK_PLAN_TODAY_DYNAMIC")
                                .replace("#siteID#", siteID);
        daoUtils.executeSql(sql);
    }
    /**
     * <li>说明：删除当前时间的重要信息
     * <li>创建人：张迪
     * <li>创建日期：2017-3-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchDate 当前时间
     */
    private void deleteTodayExpandInformation(String searchDate) {
        String sql = "Delete jxgc_expand_information where Plan_Generate_Date = '"+ searchDate +"'";
        daoUtils.executeSql(sql);
        
    }
    /**
     * <li>说明：提交重要信息
     * <li>创建人：张迪
     * <li>创建日期：2017-3-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    public void updateTheExpandInformation() {
        String sql = "Update jxgc_expand_information set save_status = 1";
        daoUtils.executeSql(sql);
        
    }
}
