package com.yunda.jx.wlgl.movewh.manager;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.wlgl.movewh.entity.MatMoveWH;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatMoveWH业务类,移库单
 * <li>创建人：程梅
 * <li>创建日期：2016-05-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="matMoveWHManager")
public class MatMoveWHManager extends JXBaseManager<MatMoveWH, MatMoveWH>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());

    /**
     * <li>说明：物料移入确认中退回原库
     * <li>创建人：张迪
     * <li>创建日期：2016-5-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 移库id
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void backOriginal(String[] ids) throws BusinessException, NoSuchFieldException {
        OmEmployee emp = SystemContext.getOmEmployee();       
        for (String id : ids) {
            MatMoveWH  matMoveWH =  super.getModelById(id);
            if(null == matMoveWH) throw new BusinessException("移库单不存存!"); 
            matMoveWH.setMovestatus(MatMoveWH.STATUS_BACKWAITCHECK);
            matMoveWH.setCheckDate(new Date());
            if (null != emp){
                matMoveWH.setCheckEmp(emp.getEmpname());
                matMoveWH.setCheckEmpID(emp.getEmpid());
            }
            this.saveOrUpdate(matMoveWH);
        }
    } 
    
    /**
     * <li>说明：移库移入退回退回
     * <li>创建人：张迪
     * <li>创建日期：2016-5-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 移库id
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void back(String[] ids) throws BusinessException, NoSuchFieldException {
        OmEmployee emp = SystemContext.getOmEmployee();       
        for (String id : ids) {
            MatMoveWH  matMoveWH =  super.getModelById(id);
            if(null == matMoveWH) throw new BusinessException("移库单不存存!"); 
            matMoveWH.setMovestatus(MatMoveWH.STATUS_WAITCHECK);
            matMoveWH.setCheckDate(new Date());
            if (null != emp){
                matMoveWH.setCheckEmp(emp.getEmpname());
                matMoveWH.setCheckEmpID(emp.getEmpid());
            }
            this.saveOrUpdate(matMoveWH);       
        }
    }   
	
}