package com.yunda.passenger.marshalling.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.passenger.marshalling.entity.Marshalling;
import com.yunda.passenger.marshalling.manager.MarshallingManager;

/**
 * <li>标题: 肯尼亚综合管理信息系统
 * <li>说明: 编组基础信息业务类
 * <li>创建人：张迪
 * <li>创建日期：2017-4-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部检修系统项目组
 * @version 1.0
 */
public class MarshallingAction extends JXBaseAction<Marshalling, Marshalling, MarshallingManager> {

    /**  序列  */
    private static final long serialVersionUID = 1L;
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：张迪
     * <li>创建日期：2017-4-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void updateMarshalling() throws  JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
        	Marshalling marshalling = JSONUtil.read(getRequest().getParameter("data"), Marshalling.class);
//        	List<MarshallingTrain> marshallingTrainList = Arrays.asList(marshallingTrains);
//        	List<MarshallingTrain> marshallingTrainList = JSONObject.parseObject(getRequest().getParameter("data"), new TypeReference<List<MarshallingTrain>>(){});
            this.manager.updateMarshalling(marshalling);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
   
	/**
     * <li>说明：查询客车编组主表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param params 查询条件
     * @return
     */  
    public void findMarshallingList() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            Map<String, String> params = new HashMap<String, String>();
            List<Map<String, Object>> list = this. manager.findMarshallingList(params);
            map.put("list", list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }                 
    }
    
	/**
     * <li>说明：查询客车备用车列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param params 查询条件
     * @return
     */  
    public void findNotMarshallingList() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            Map<String, String> params = new HashMap<String, String>();
            List<Map<String, Object>> list = this. manager.findNotMarshallingList(params);
            map.put("list", list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }                 
    }
    
	/**
     * <li>说明：查询客车检修车车列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param params 查询条件
     * @return
     */  
    public void findJxMarshallingList() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            Map<String, String> params = new HashMap<String, String>();
            List<Map<String, Object>> list = this. manager.findJxMarshallingList(params);
            map.put("list", list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }                 
    }
    
    
}
