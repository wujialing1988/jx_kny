package com.yunda.jxpz.equipinfo.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jxpz.equipinfo.entity.DeviceType;
import com.yunda.jxpz.equipinfo.manager.DeviceTypeManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：DeviceType控制器, 设备分类
 * <li>创建人：刘晓斌
 * <li>创建日期：2015-01-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class DeviceTypeAction extends JXBaseAction<DeviceType, DeviceType, DeviceTypeManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
     * <li>说明：接受物理删除记录请求，向客户端返回操作结果（JSON格式）
     * <li>创建人：程梅
     * <li>创建日期：2015-01-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */ 
    @SuppressWarnings("all")
    public void delete() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String[] errMsg = this.manager.validateDelete(ids);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.delete(ids);
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
     * <li>方法说明：查询设备类别树
     * <li>方法名：tree
     * @throws JsonMappingException
     * @throws IOException
     * <li>创建人： 张凡
     * <li>创建日期：2015年11月24日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public void tree() throws JsonMappingException, IOException{
        List<Map<String, Object>> children = null;
        try {
            children = this.manager.deviceClassTree();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
        } finally {
            JSONUtil.write(getResponse(), children);
        } 
    }
}