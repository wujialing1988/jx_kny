package com.yunda.jx.pjjx.partsrdp.wpinst.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjjx.partsrdp.wpinst.entity.PartsNodeRe;
import com.yunda.jx.pjjx.partsrdp.wpinst.manager.PartsNodeReManager;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: PartsNodeRe控制器, 返修节点原因
 * <li>创建人：何涛
 * <li>创建日期：2016-2-23
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 3.2.4
 */
@SuppressWarnings("serial")
public class PartsNodeReAction extends JXBaseAction<PartsNodeRe, PartsNodeRe, PartsNodeReManager> {
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass());

    /**
     * <li>说明：获取节点返修原因数组
     * <li>创建人：何涛
     * <li>创建日期：2016-2-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getRebackCause() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String rdpNodeIDX = getRequest().getParameter("rdpNodeIDX");
            List<String> rebackCause = this.manager.getRebackCause(rdpNodeIDX);
            map.put("rebackCause", rebackCause);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger , map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
}
