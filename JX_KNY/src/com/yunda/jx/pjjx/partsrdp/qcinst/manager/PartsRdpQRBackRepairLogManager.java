package com.yunda.jx.pjjx.partsrdp.qcinst.manager;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.pjjx.base.qcitemdefine.manager.QCItemManager;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQRBackRepairLog;
 
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 配件质量检查返修日志业务业务类
 * <li>创建人：张迪
 * <li>创建日期：2016-8-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="partsRdpQRBackRepairLogManager")
public class PartsRdpQRBackRepairLogManager extends JXBaseManager<PartsRdpQRBackRepairLog, PartsRdpQRBackRepairLog>{
    
    /** JCQCItemDefine业务类 */
    @Resource
    private QCItemManager qcItemManager;
    /**
     * <li>说明：记录返修日志
     * <li>创建人：张迪
     * <li>创建日期：2016-8-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 需修改的实体类
     */
    @Override
    public void saveOrUpdate(PartsRdpQRBackRepairLog entity) throws BusinessException, NoSuchFieldException {
        entity = EntityUtil.setSysinfo(entity);
        // 设置逻辑删除字段状态为未删除
        entity = EntityUtil.setNoDelete(entity);
        OmEmployee omEmployee = SystemContext.getOmEmployee();  // 设置质量人员信息
        entity.setQREmpID(omEmployee.getEmpid());           
        entity.setQREmpName(omEmployee.getEmpname());
        //  设置质量检查项名称
        entity.setQCItemName(qcItemManager.getModelByQCItemNo(entity.getQCItemNo()).getQCItemName()); 
//      设置返修时间
        entity.setBackRepairTime(new Date()); 
        this.daoUtils.getHibernateTemplate().saveOrUpdate(entity);
    }   
}