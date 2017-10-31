package com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.TrainRdpQCBackRepairLog;

@Service(value = "trainRdpQCBackRepairLogManager")
public class TrainRdpQCBackRepairLogManager extends JXBaseManager<TrainRdpQCBackRepairLog, TrainRdpQCBackRepairLog> {
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
    public void saveOrUpdate(TrainRdpQCBackRepairLog entity) throws BusinessException, NoSuchFieldException {
        entity = EntityUtil.setSysinfo(entity);
        // 设置逻辑删除字段状态为未删除
        entity = EntityUtil.setNoDelete(entity);
        OmEmployee omEmployee = SystemContext.getOmEmployee();  // 设置质量人员信息
        entity.setQCEmpID(omEmployee.getEmpid());           
        entity.setQCEmpName(omEmployee.getEmpname());
        //      设置返修时间
        entity.setBackRepairTime(new Date()); 
        this.daoUtils.getHibernateTemplate().saveOrUpdate(entity);
    }   
}
