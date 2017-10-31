package com.yunda.twt.twtinfo.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.twt.twtinfo.entity.SiteVideoNvr;
import com.yunda.twt.twtinfo.manager.SiteVideoNvrManager;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: SiteVideoNvr控制器，视频监控网络硬盘录像机
 * <li>创建人：何涛
 * <li>创建日期：2015-6-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public class SiteVideoNvrAction extends JXBaseAction<SiteVideoNvr, SiteVideoNvr, SiteVideoNvrManager>{

    /** 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass());
    
    /**
     * <li>说明：树
     * <li>创建人：何涛
     * <li>创建日期：2015-06-02
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception 抛出异常列表
     */
    @SuppressWarnings("unchecked")
    public void tree() throws Exception{
        List<HashMap<String, Object>> children = null ;
        try {
            children = manager.tree();
        } catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
        } finally{
            JSONUtil.write(getResponse(), children);
        }
    }
    
    /**
     * <li>说明：根据idx主键获取视频监控网络硬盘录像机实例对象
     * <li>创建人：何涛
     * <li>创建日期：2015-06-04
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getModel() throws Exception{
    	Map<String, Object> map = new HashMap<String, Object>();
    	try {
    		 SiteVideoNvr model = manager.getModelById(id);
    		 if (null != model) {
    			 map.put(Constants.ENTITY, model);
    		 }
    	} catch (RuntimeException e) {
    		ExceptionUtil.process(e, logger);
    	} finally{
    		JSONUtil.write(getResponse(), map);
    	}
    }
        
}
