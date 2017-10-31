package com.yunda.zb.zbfw.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.zb.zbfw.entity.ZbFw;
import com.yunda.zb.zbfw.manager.ZbFwManager;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbFw控制器, 整备范围
 * <li>创建人：王利成
 * <li>创建日期：2015-01-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class ZbFwAction extends JXBaseAction<ZbFw, ZbFw, ZbFwManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    
    
    /**
     * <li>说明：保存列检范围
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveZbFw() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            ZbFw fw = (ZbFw)JSONUtil.read(getRequest(), entity.getClass());
            String[] errMsg = this.manager.validateUpdate(fw);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveZbFw(fw);
//              返回记录保存成功的实体对象
                map.put("entity", fw);  
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
    
	/**
	 * 
	 * <li>说明：获得机车整备范围信息列表
	 * <li>创建人：王利成
	 * <li>创建日期：2015-1-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	public void getZbFwList() throws Exception{
	    Map<String,Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
			String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
			entity = (ZbFw)JSONUtil.read(searchJson, entitySearch.getClass());
			map = this.manager.getZbFwList(entity, searchJson, getStart(), getLimit(),getOrders()).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
    
	/**
	 * 
	 * <li>说明：根据车型车号过滤整备范围流程
	 * <li>创建人：林欢
	 * <li>创建日期：2016-8-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	public void findZbFwNotInTrainNoAndTrainTypeIDX() throws Exception{
	    Map<String,Object> map = new HashMap<String,Object>();
	    try {
	        HttpServletRequest req = getRequest();
	        String trainNo = req.getParameter("trainNo");
	        String trainTypeIDX = req.getParameter("trainTypeIDX");
            Map<String, Object> paramsMap = new HashMap<String, Object>();
            paramsMap.put("trainNo", trainNo);
            paramsMap.put("trainTypeIDX", trainTypeIDX);
	        map = this.manager.findZbFwNotInTrainNoAndTrainTypeIDX(paramsMap, getStart(), getLimit(),getOrders()).extjsStore();
	    } catch (Exception e) {
	        ExceptionUtil.process(e, logger, map);
	    } finally {
	        JSONUtil.write(this.getResponse(), map);
	    }	
	}
    
    /**
     * 
     * <li>说明：根据车型查询范围流程
     * <li>创建人：林欢
     * <li>创建日期：2018-8-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getZbFwByTrain() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String trainTypeIDX = req.getParameter("trainTypeIDX");
            
            List<ZbFw> zbFwList = this.manager.getZbFwByTrain(trainTypeIDX);
            map.put("zbFwList", zbFwList);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：设置适用车型
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException
     */
    public void setTrainVehicle() throws IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String trianCodes = getRequest().getParameter("trianCodes");
            String trianTypes = getRequest().getParameter("trianTypes");
            String idxs = getRequest().getParameter("idxs");
            this.manager.setTrainVehicle(idxs, trianCodes,trianTypes); 
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}