package com.yunda.zb.pczz.action; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.zb.pczz.entity.ZbglPczzItemToTraininfo;
import com.yunda.zb.pczz.manager.ZbglPczzItemToTraininfoManager;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglPczzItemToTraininfoAction控制器, 普查整治项中保存的机车信息
 * <li>创建人：林欢
 * <li>创建日期：2016-06-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class ZbglPczzItemToTraininfoAction extends JXBaseAction<ZbglPczzItemToTraininfo, ZbglPczzItemToTraininfo, ZbglPczzItemToTraininfoManager>{
    
    Logger logger = Logger.getLogger(this.getClass());
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * <li>说明：更新普查整治任务项车型
     * <li>创建人：王利成
     * <li>创建日期：2015-3-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updatePczzItemTrainNo() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
        	ZbglPczzItemToTraininfo[] trainTypes = (ZbglPczzItemToTraininfo[])JSONUtil.read(getRequest(), ZbglPczzItemToTraininfo[].class);
            this.manager.updatePczzItemTrainNos(ids, trainTypes);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * <li>说明：查询当前普查整治项关联机车信息
     * <li>创建人：林欢
     * <li>创建日期：2016-3-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findZbglPczzItemToTraininfoListByPczzItemID() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        
        JSONObject ob = JSONObject.parseObject(getRequest().getParameter("entityJson"));
        
        String idx = ob.getString("idx");//普查整治项idx
        String trainTypeShortName = ob.getString("trainTypeShortName");//车型简称
        String trainNo = ob.getString("trainNo");//车号
        try {
        	Map<String, String> paramMap = new HashMap<String, String>();
        	paramMap.put("idx", idx);
        	paramMap.put("trainNoLike", trainNo);
        	paramMap.put("trainTypeShortName", trainTypeShortName);
        	
			List<ZbglPczzItemToTraininfo> list = this.manager.findZbglPczzItemToTraininfoListByPczzItemID(paramMap);
            map = new Page<ZbglPczzItemToTraininfo>(list.size(), list).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}