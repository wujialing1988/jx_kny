package com.yunda.jxpz.systemchar.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jxpz.systemchar.entity.SystemChar;
import com.yunda.jxpz.systemchar.manager.SystemCharManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：SystemChar控制器, 特殊字符
 * <li>创建人：程锐
 * <li>创建日期：2013-07-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class SystemCharAction extends JXBaseAction<SystemChar, SystemChar, SystemCharManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * <li>说明：接受物理删除记录请求，向客户端返回操作结果（JSON格式）
     * <li>创建人：程锐
     * <li>创建日期：2013-07-18
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
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
}