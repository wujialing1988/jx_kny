package com.yunda.jx.pjjx.partsrdp.wpinst.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjjx.partsrdp.wpinst.entity.PartsRdpNode;
import com.yunda.jx.pjjx.partsrdp.wpinst.manager.PartsRdpNodeManager;
import com.yunda.jx.pjjx.partsrdp.wpinst.manager.PartsRdpNodeManager.Node;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpNode控制器, 配件检修作业节点
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
public class PartsRdpNodeAction extends JXBaseAction<PartsRdpNode, PartsRdpNode, PartsRdpNodeManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
    /**
     * <li>说明：启动节点
     * <li>创建人：程锐
     * <li>创建日期：2015-10-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void startUp() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String idx = getRequest().getParameter("idx");
            this.manager.startUp(idx);
            map.put(Constants.SUCCESS, "true");
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * <li>说明：节点修竣提交
     * <li>创建人：程锐
     * <li>创建日期：2015-10-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void finishPartsNode() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String idx = getRequest().getParameter("idx");
            this.manager.updateFinishedStatus(idx);
            map.put(Constants.SUCCESS, "true");
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * <li>说明：查询作业流程节点树（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-10-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void tree() throws Exception{
        List<Node> children = null ;
        try {
            String rdpIDX = getRequest().getParameter("rdpIDX");                // 作业流程主键
            children = this.manager.tree(null, rdpIDX);
        } catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
        } finally{
            JSONUtil.write(getResponse(), children);
        }
    }
    
    /**
     * <li>说明：配件检修验收返修，返工修（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-11-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void updateBack() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
        	// 返修原因
            String cause = getRequest().getParameter("cause");
            this.manager.updateBack(id, cause);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * <li>说明：通过配件idx查询对应的配件检修流程
     * <li>创建人：张迪
     * <li>创建日期：2017-3-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getEntityByPartIDX() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String partsTypeIDX = getRequest().getParameter("partsTypeIDX");
            String wpIDX = getRequest().getParameter("wpIDX");
            String workPlanIDX = getRequest().getParameter("workPlanIDX");
            List<Object> nodes = this.manager.getEntityByPartIDX(partsTypeIDX, wpIDX, workPlanIDX);
            map.put("entity", nodes);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：张迪
     * <li>创建日期：2017-4-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getModelByIDX() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String idx = getRequest().getParameter("idx");
            PartsRdpNode node =  this.manager.getModelByIDX(idx);
            map.put("entity", node);
            map.put(Constants.SUCCESS, "true");
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
}