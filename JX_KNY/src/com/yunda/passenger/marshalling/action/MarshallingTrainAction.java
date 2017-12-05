package com.yunda.passenger.marshalling.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.passenger.marshalling.entity.MarshallingTrain;
import com.yunda.passenger.marshalling.manager.MarshallingTrainManager;


/**
 * <li>标题: 肯尼亚综合管理信息系统
 * <li>说明: 编组车辆业务类
 * <li>创建人：张迪
 * <li>创建日期：2017-4-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部检修系统项目组
 * @version 1.0
 */
public class MarshallingTrainAction extends JXBaseAction<MarshallingTrain, MarshallingTrain, MarshallingTrainManager> {

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
    public void saveOrUpdateList() throws  JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
//        	MarshallingTrain[] marshallingTrains = JSONUtil.read(getRequest().getParameter("datas"), MarshallingTrain[].class);
//        	List<MarshallingTrain> marshallingTrainList = Arrays.asList(marshallingTrains);
        	List<MarshallingTrain> marshallingTrainList = JSONObject.parseObject(getRequest().getParameter("datas"), new TypeReference<List<MarshallingTrain>>(){});
            this.manager.saveOrUpdateTrainList(marshallingTrainList);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
   
    
    
}
