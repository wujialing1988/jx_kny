package com.yunda.jx.jxgc.producttaskmanage.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.producttaskmanage.entity.DetectResult;
import com.yunda.jx.jxgc.producttaskmanage.manager.DetectResultManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：DetectResult控制器, 检测结果
 * <li>创建人：程锐
 * <li>创建日期：2012-12-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class DetectResultAction extends JXBaseAction<DetectResult, DetectResult, DetectResultManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>方法名称：checkRetectResultAllComplete
	 * <li>方法说明：检查数据项是否都完成 
	 * <li>
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2013-3-2 下午01:30:10
	 * <li>修改人：
	 * <li>修改内容：
	 */
	public void checkRetectResultAllComplete(){
	    String workTaskIdx = getRequest().getParameter("workTaskIdx");
	    int count = manager.getNotCompleteCount(workTaskIdx);
	    ajaxMessage("{count:" + count + "}");
	}
    
    /**
     * <li>说明：获取某个作业任务下属的所有检测项
     * <li>创建人：何涛
     * <li>创建日期：2015-7-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getModelsByworkTaskIDX() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // 作业任务主键
            String workTaskIDX = getRequest().getParameter("workTaskIDX");
            List<DetectResult> list = this.manager.getModelsByworkTaskIDX(workTaskIDX);
            map.put("list", list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
	
}