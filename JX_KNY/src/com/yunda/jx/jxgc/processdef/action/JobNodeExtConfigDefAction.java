package com.yunda.jx.jxgc.processdef.action; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.processdef.entity.JobNodeExtConfigDef;
import com.yunda.jx.jxgc.processdef.manager.JobNodeExtConfigDefManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JobNodeExtConfigDef控制器,扩展配置
 * <li>创建人：何涛
 * <li>创建日期：2015-5-4
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class JobNodeExtConfigDefAction extends JXBaseAction< JobNodeExtConfigDef,  JobNodeExtConfigDef,  JobNodeExtConfigDefManager>{
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：批量保存
     * <li>创建人：何涛
     * <li>创建日期：2015-05-04
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @throws JsonMappingException
     * @throws IOException
     */
    public void save() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
             JobNodeExtConfigDef[] array = JSONUtil.read(getRequest(), JobNodeExtConfigDef[].class);
             List< JobNodeExtConfigDef> entityList = new ArrayList< JobNodeExtConfigDef>();
             for (JobNodeExtConfigDef configDef : array) {
                 String[] errMsg = this.manager.validateUpdate(configDef);
                 if (errMsg == null || errMsg.length < 1) {
                     entityList.add(configDef);
                 } else {
                     map.put(Constants.SUCCESS, false);
                     map.put(Constants.ERRMSG, errMsg);
                 }
             }
             this.manager.saveOrUpdate(entityList);
             map.put(Constants.SUCCESS, true);
             map.put("list", entityList);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
}