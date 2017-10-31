package com.yunda.jx.wlgl.movewh.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.wlgl.inwh.entity.MatInWHNew;
import com.yunda.jx.wlgl.inwh.manager.MatInWHNewManager;
import com.yunda.jx.wlgl.movewh.entity.MatMoveInConfirm;
import com.yunda.jx.wlgl.movewh.entity.MatMoveWH;
import com.yunda.jx.wlgl.movewh.entity.MoveInSplin;
import com.yunda.util.BeanUtils;

@Service(value="matMoveInConfirmManager")
public class MatMoveInConfirmManager extends  JXBaseManager<MatMoveInConfirm, MatMoveInConfirm>{
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    @Resource
    private  MatInWHNewManager  matInWHNewManager;
    @Resource
    private  MatMoveWHManager  matMoveWHManager;
    
   /**
     * <li>说明：待移入确认入库
     * <li>创建人：张迪
     * <li>创建日期：2016-5-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param matMoveInConfirm 移库确认单
     * @param locationList 拆分库位列表
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void saveMoveInMat(MatMoveInConfirm matMoveInConfirm, MoveInSplin[] locationList) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException{
       OmEmployee emp = SystemContext.getOmEmployee();    
       //封装入库数据
       MatInWHNew matInWHNew = new MatInWHNew();
       BeanUtils.copyProperties(matInWHNew, matMoveInConfirm);
       matInWHNew.setWhIDX(matMoveInConfirm.getGetWhIDX());
       matInWHNew.setWhName(matMoveInConfirm.getGetWhName());
       matInWHNew.setInWhType(MatInWHNew.TYPE_YK);
       if(null == locationList || 0 >= locationList.length){
           matInWHNew.setIdx(null);
           matInWHNew.setLocationIdx(matMoveInConfirm.getGetLocationIDX());
           matInWHNew.setLocationName(matMoveInConfirm.getGetLocationName()); 
           matInWHNew.setStatus(matMoveInConfirm.getStatus()); 
           matInWHNew.setQty(matMoveInConfirm.getQty());
           // 保存入库信息
           matInWHNewManager.saveMatInWHNew(matInWHNew);
       }
       else{
           for(MoveInSplin location: locationList){
               MatInWHNew matInWHNewTemp = new MatInWHNew();
               BeanUtils.copyProperties(matInWHNewTemp,matInWHNew);
               matInWHNewTemp.setIdx(null);
               matInWHNewTemp.setLocationIdx(location.getLocationIdx());
               matInWHNewTemp.setLocationName(location.getLocationName());
               matInWHNewTemp.setStatus(location.getStatus());
               matInWHNewTemp.setQty(location.getQty());
               // 保存入库信息
               matInWHNewManager.saveMatInWHNew(matInWHNewTemp);       
           }
       }
      //  修改移库单状态
       MatMoveWH matMoveWH = new MatMoveWH();
       matMoveWH = matMoveWHManager.getModelById(matMoveInConfirm.getIdx());
       if(null != matMoveWH){
           matMoveWH.setMovestatus(MatMoveWH.STATUS_CHECKED);
           matMoveWH.setCheckDate(new Date());
           if (null != emp){
               matMoveWH.setCheckEmp(emp.getEmpname());
               matMoveWH.setCheckEmpID(emp.getEmpid());
           }
           matMoveWHManager.saveOrUpdate(matMoveWH);
       }      
   }


   
}
