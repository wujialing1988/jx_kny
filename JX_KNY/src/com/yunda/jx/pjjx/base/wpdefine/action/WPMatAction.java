package com.yunda.jx.pjjx.base.wpdefine.action; 

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjjx.base.wpdefine.entity.WPMat;
import com.yunda.jx.pjjx.base.wpdefine.manager.WPMatManager;
 
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明:  配件检修用料定义控制器
 * <li>创建人：张迪
 * <li>创建日期：2016-8-31
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class WPMatAction extends JXBaseAction<WPMat, WPMat, WPMatManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
    /**
     * <li>说明：批量保存 配件检修用料
     * <li>创建人：陈志刚
     * <li>创建日期：2016-9-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveMatInfor() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            WPMat[] list = (WPMat[])JSONUtil.read(getRequest(), WPMat[].class);
            String[] errMsg = this.manager.saveMatInfor(list);
            if (errMsg == null || errMsg.length < 1) {
                map.put(Constants.SUCCESS, true);
            }else{
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * <li>说明：更新保存检修用料信息
     * <li>创建人：陈志刚
     * <li>创建日期：2016-9-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void matUpdate() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            WPMat[] jnmd = (WPMat[])JSONUtil.read(getRequest(), WPMat[].class);
            String[] errMsg = this.manager.matUpdate(jnmd);
            if (errMsg == null || errMsg.length < 1) {
                map.put(Constants.SUCCESS, true);
            }else{
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, errMsg);
            }   
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
	
}