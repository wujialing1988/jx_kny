package com.yunda.jx.jxgc.tpmanage.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.tpmanage.entity.FaultQCResult;
import com.yunda.jx.jxgc.tpmanage.entity.FaultQCResultVO;
import com.yunda.jx.jxgc.tpmanage.manager.FaultQCResultManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明：FaultQCResult控制器, 提票质量检查
 * <li>创建人：程锐
 * <li>创建日期：2015-06-25
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.1
 */
@SuppressWarnings(value = "serial")
public class FaultQCResultAction extends JXBaseAction<FaultQCResult, FaultQCResult, FaultQCResultManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：完成提票质检结果
     * <li>创建人：程锐
     * <li>创建日期：2015-7-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateFinishQCResult() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            FaultQCResultVO[] resultVO = (FaultQCResultVO[]) JSONUtil.read(getRequest(), FaultQCResultVO[].class);
            this.manager.updateFinishQCResult(resultVO);
            map.put("success", true);            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
