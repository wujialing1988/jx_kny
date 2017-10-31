package com.yunda.jx.jxgc.tpmanage.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicketCheckRole;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 提票过程角色业务类
 * <li>创建人：张迪
 * <li>创建日期：2016-12-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "faultTicketCheckRoleManager")
public class FaultTicketCheckRoleManager extends JXBaseManager<FaultTicketCheckRole, FaultTicketCheckRole> {
    
    
    /**
     * <li>说明：通过类型获取角色集合
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param type 提票类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<FaultTicketCheckRole> findFaultTicketCheckRoleByType(String type){
        StringBuffer sb = new StringBuffer(" From FaultTicketCheckRole where recordStatus = 0 ");
        sb.append(" and faultTicketType = ? ");
        return this.daoUtils.find(sb.toString(),new Object[]{type});
    }

    /**
     * <li>说明：批量保存角色
     * <li>创建人：张迪
     * <li>创建日期：2016-12-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param checkRoles 提票确认角色实体数组
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveCheckRoles(FaultTicketCheckRole[] checkRoles) throws BusinessException, NoSuchFieldException {
        for (FaultTicketCheckRole checkRole : checkRoles) {
            this.saveOrUpdate(checkRole);
        }        
    }
   
}
