package com.yunda.jx.jxgc.tpmanage.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.tpmanage.entity.FaultQCListBean;
import com.yunda.jx.jxgc.tpmanage.manager.FaultQCResultQueryManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 提票质检查询控制器
 * <li>创建人：程锐
 * <li>创建日期：2015-7-14
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class FaultQCResultQueryAction extends JXBaseAction<FaultQCListBean, FaultQCListBean, FaultQCResultQueryManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：查询当前人员的提票质量检查项列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void getQCPageList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String mode = StringUtil.nvl(getRequest().getParameter("mode"), "1");
            String queryString = StringUtil.nvl(getRequest().getParameter("query"), "");
            map = this.manager.getQCPageList(SystemContext.getOmEmployee().getEmpid(), getStart(), getLimit(), mode, queryString).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
