package com.yunda.jx.wlgl.movewh.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.wlgl.movewh.entity.MatMoveInConfirm;
import com.yunda.jx.wlgl.movewh.entity.MoveInSplin;
import com.yunda.jx.wlgl.movewh.manager.MatMoveInConfirmManager;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 物料移入确认
 * <li>创建人：张迪
 * <li>创建日期：2016-5-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MatMoveInConfirmAction extends JXBaseAction<MatMoveInConfirm, MatMoveInConfirm, MatMoveInConfirmManager>{
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
   
     
     /**
     * <li>说明：确认入库
     * <li>创建人：张迪
     * <li>创建日期：2016-5-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void saveMoveInMat() throws JsonMappingException, IOException{
         Map<String, Object> map = new HashMap<String,Object>();
         try {
             MatMoveInConfirm matMoveInConfirm = (MatMoveInConfirm)JSONUtil.read(getRequest(), entity.getClass());
             MoveInSplin[] locationList = (MoveInSplin[])JSONUtil.read(getRequest().getParameter("datas"), MoveInSplin[].class);
             String[] errMsg = this.manager.validateUpdate(matMoveInConfirm);
             if (errMsg == null || errMsg.length < 1) {
                 this.manager.saveMoveInMat(matMoveInConfirm, locationList);
                 map.put("success", true);
             } else {
                 map.put("success", false);
                 map.put("errMsg", errMsg);
             }
         } catch (Exception e) {
             ExceptionUtil.process(e, logger, map);
         } finally {
             JSONUtil.write(this.getResponse(), map);
         }       
      }
    
    
       
}
