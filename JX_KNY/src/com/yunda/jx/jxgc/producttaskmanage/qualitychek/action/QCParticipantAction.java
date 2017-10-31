package com.yunda.jx.jxgc.producttaskmanage.qualitychek.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCParticipant;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCParticipantManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: QCParticipant控制器, 机车检修质量检验参与者
 * <li>创建人：何涛
 * <li>创建日期：2016-4-12
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class QCParticipantAction extends JXBaseAction<QCParticipant, QCParticipant, QCParticipantManager> {

    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：获取作业工单指派的质量检查人员
     * <li>创建人：何涛
     * <li>创建日期：2016-04-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getQCEmpsForAssign() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        map.put(Constants.SUCCESS, false);      
        try {
            String workCardIDX = getRequest().getParameter("workCardIDX");
            List<Map<String, Object>> list = this.manager.getQCEmpsForAssign(workCardIDX);
            map = Page.extjsStore("checkItemCode", list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
