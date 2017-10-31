package com.yunda.jx.jczl.undertakemanage.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jczl.undertakemanage.entity.UndertakeTrain;
import com.yunda.jx.jczl.undertakemanage.manager.UndertakeTrainManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：UndertakeTrain控制器, 承修车
 * <li>创建人：程梅
 * <li>创建日期：2013-03-04
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class UndertakeTrainAction extends JXBaseAction<UndertakeTrain, UndertakeTrain, UndertakeTrainManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：查询符合条件的承修机车信息
     * <li>创建人：程梅
     * <li>创建日期：2013-3-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    public void findpageList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            
            map = this.manager.findPageList(searchJson, getStart(), getLimit(), getOrders()).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    /**
     * 
     * <li>说明：保存从机车信息中选取的数据
     * <li>创建人：程梅
     * <li>创建日期：2013-3-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    public void saveFromTrain() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            UndertakeTrain[] objList = (UndertakeTrain[])JSONUtil.read(getRequest(), UndertakeTrain[].class);
            String[] errMsg = this.manager.validateUpdate(objList);  //验证
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveFromTrain(objList);
                map.put("success", "true");
            }else{
                map.put("errMsg", errMsg);
            }
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    /**
     * 
     * <li>说明：查询承修机车列表---新增普查整治对象时所用
     * <li>创建人：程梅
     * <li>创建日期：2013-8-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
   public void findPageListForInspect() throws JsonMappingException, IOException{
	   Map<String, Object> map = new HashMap<String,Object>();
       try {
           HttpServletRequest req = getRequest();
           String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
           String planIDX = req.getParameter("planIDX");
           String projectIDX = req.getParameter("projectIDX"); //技术改造项目主键
           entity = (UndertakeTrain) JSONUtil.read(searchJson, entitySearch
					.getClass());
			SearchEntity<UndertakeTrain> searchEntity = new SearchEntity<UndertakeTrain>(
					entity, getStart(), getLimit(), getOrders());
           map = this.manager.findPageListForInspect(searchEntity, planIDX,projectIDX).extjsStore();
       } catch (Exception e) {
       	ExceptionUtil.process(e, logger, map);
       } finally {
           JSONUtil.write(this.getResponse(), map);
       }   
   }
}