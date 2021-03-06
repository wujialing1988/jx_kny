package com.yunda.jcbm.jcgx.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jcbm.jcgx.entity.JcgxBuild;
import com.yunda.jcbm.jcgx.manager.JcgxBuildCopyManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 从机车组成复制机车构型action
 * <li>创建人：程锐
 * <li>创建日期：2016-5-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2
 */
@SuppressWarnings(value = "serial")
public class JcgxBuildCopyAction extends JXBaseAction<JcgxBuild, JcgxBuild, JcgxBuildCopyManager>{
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：根据机车组成复制机车构型
     * <li>创建人：程锐
     * <li>创建日期：2016-5-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
	public void saveByBuildType() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
        	this.manager.generateByBuildType(id);            
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
	
    /**
     * <li>说明：从【位置关联的故障现象】复制【分类编码关联的故障现象】
     * <li>创建人：程锐
     * <li>创建日期：2016-5-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
	public void savePlaceFault() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
        	this.manager.savePlaceFault();            
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
