package com.yunda.jx.pjjx.partsrdp.worktime.action; 

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjjx.partsrdp.worktime.entity.PartsRdpWorkTime;
import com.yunda.jx.pjjx.partsrdp.worktime.manager.PartsRdpWorkTimeManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpWorkTime控制器, 工时
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class PartsRdpWorkTimeAction extends JXBaseAction<PartsRdpWorkTime, PartsRdpWorkTime, PartsRdpWorkTimeManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：平均分配工时
     * <li>创建人：程梅
     * <li>创建日期：2015-2-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateWorkTimeAverage() throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.getManager().updateWorkTimeAverage(ids);
            map.put("success", true);
        } catch (Exception e) {
            map.put("success", false);
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
}