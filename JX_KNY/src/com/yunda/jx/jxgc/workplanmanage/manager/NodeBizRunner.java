package com.yunda.jx.jxgc.workplanmanage.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 完成流程节点业务线程类
 * <li>创建人：程锐
 * <li>创建日期：2015-5-13
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
public class NodeBizRunner extends Thread {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    private List<String> tecIdxList;
    
    private AcOperator acOperator;
    
    /**
     * <li>说明：构造方法
     * <li>创建人：程锐
     * <li>创建日期：2015-5-13
     * <li>修改人：
     * <li>修改日期：
     * @param idxs 流程节点idx列表
     * @param acOperator 当前操作者
     */
    public NodeBizRunner(List<String> idxs, AcOperator acOperator) {
        tecIdxList = new ArrayList<String>();
        for (int i = 0; i < idxs.size(); i++) {
            tecIdxList.add(idxs.get(i));
        }        
        this.acOperator = acOperator;
    }
    
    /**
     * <li>说明：当前线程启动方法
     * <li>创建人：程锐
     * <li>创建日期：2015-5-13
     * <li>修改人：
     * <li>修改日期：
     */
    public void run() {
        JobProcessNodeManager m = (JobProcessNodeManager) Application.getSpringApplicationContext().getBean("jobProcessNodeManager");
        JobProcessNode nodeCase = null;
        SystemContext.setAcOperator(acOperator);
        try {
            for (int i = 0; i < tecIdxList.size(); i++) {
                nodeCase = m.getModelById(tecIdxList.get(i));
                if (nodeCase != null) {
                    m.updateFinishNodeCase(nodeCase);
                }
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
        }
    }
}
