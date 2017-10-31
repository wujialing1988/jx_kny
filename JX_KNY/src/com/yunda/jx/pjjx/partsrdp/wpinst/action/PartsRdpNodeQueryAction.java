package com.yunda.jx.pjjx.partsrdp.wpinst.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.partsrdp.wpinst.entity.PartsRdpNode;
import com.yunda.jx.pjjx.partsrdp.wpinst.entity.PartsRdpNodeBean;
import com.yunda.jx.pjjx.partsrdp.wpinst.manager.PartsRdpNodeQueryManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 配件检修节点查询控制器
 * <li>创建人：程锐
 * <li>创建日期：2015-10-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.3
 */
@SuppressWarnings(value="serial")
public class PartsRdpNodeQueryAction extends JXBaseAction<PartsRdpNode, PartsRdpNode, PartsRdpNodeQueryManager>{
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：查询当前人员可以处理的检修作业节点任务
     * <li>创建人：程锐
     * <li>创建日期：2015-10-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void queryNodeListByUser() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT );
            PartsRdpNodeBean entity = (PartsRdpNodeBean)JSONUtil.read(searchJson, PartsRdpNodeBean.class);
            SearchEntity<PartsRdpNodeBean> searchEntity = new SearchEntity<PartsRdpNodeBean>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.queryNodeListByUser(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：根据作业流程IDX查询其关联的节点任务列表
     * <li>创建人：程锐
     * <li>创建日期：2015-10-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void queryNodeListByRdp() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String rdpIDX = StringUtil.nvlTrim( req.getParameter("rdpIDX"), "");
            map = Page.extjsStore("idx", this.manager.queryNodeListByRdp(rdpIDX));
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：查询当前人员可以处理的检修作业节点任务的数量
     * <li>创建人：程锐
     * <li>创建日期：2015-10-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void queryNodeCountByUser() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT );
            PartsRdpNodeBean entity = (PartsRdpNodeBean)JSONUtil.read(searchJson, PartsRdpNodeBean.class);
            SearchEntity<PartsRdpNodeBean> searchEntity = new SearchEntity<PartsRdpNodeBean>(entity, 1, 1, null);
            int count = this.manager.queryNodeCountByUser(searchEntity);
            map.put("count", count);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：获取配件作业计划的所有子节点列表(PDA、PAD调用：配件检修合格验收-返修-选返修节点列表)
     * <li>创建人：程锐
     * <li>创建日期：2015-11-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void getLeafNodeListByRdp() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String rdpIDX = StringUtil.nvlTrim( req.getParameter("rdpIDX"), "");
            map = Page.extjsStore("idx", this.manager.getLeafNodeListByRdp(rdpIDX));
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：配件检修作业节点树
     * <li>创建人：程锐
     * <li>创建日期：2015-12-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void tree() throws Exception{
        List<HashMap<String, Object>> children = null ;
        try {
            String parentIDX = getRequest().getParameter("parentIDX");      // 上级作业节点主键
            String rdpIDX = getRequest().getParameter("rdpIDX");              // 作业流程主键
            children = manager.tree(parentIDX, rdpIDX);
        } catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
        } finally{
            JSONUtil.write(getResponse(), children);
        }
    }
}
