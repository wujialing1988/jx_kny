package com.yunda.jx.jxgc.repairrequirement.action; 

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.repairrequirement.entity.RepairProject;
import com.yunda.jx.jxgc.repairrequirement.manager.RepairProjectManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：RepairProject控制器, 检修项目
 * <li>创建人：王治龙
 * <li>创建日期：2012-12-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class RepairProjectAction extends JXBaseAction<RepairProject, RepairProject, RepairProjectManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：查询符合条件的检修项目列表信息，用于工艺节点中选择检修项目
     * <li>创建人：程梅
     * <li>创建日期：2012-12-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws 抛出异常列表
     */
    @SuppressWarnings("unchecked")
    public void findListForTecProcessNodePro() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            String repairProjectType = req.getParameter("type");
            String processIdx = req.getParameter("processIdx");     //工艺流程id
            String repairProjectName = req.getParameter("repairProjectName");     //检修项目名称
            String trainTypeIDX = req.getParameter("trainTypeIDX");//车型主键
            String partsTypeIDX = req.getParameter("partsTypeIDX");//配件主键
            RepairProject entity = (RepairProject)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<RepairProject> searchEntity = new SearchEntity<RepairProject>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findListForTecProcessNodePro(repairProjectName,processIdx,searchEntity,repairProjectType, trainTypeIDX, partsTypeIDX).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：删除检修项目并且级联删除检修项目关联的工序卡和施修规则等
     * <li>创建人：王治龙
     * <li>创建日期：2012-12-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("all")
    public void logicDeleteCascade() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String[] errMsg = this.manager.validateDelete(ids);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.logicDeleteCascade(ids);
                map.put("success", true);
            } else {
                map.put("success", false);
                map.put("errMsg", errMsg);
            }           
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }    
    
    /**
     * <li>说明：删除检修项目并且级联删除检修项目关联的工序卡
     * <li>创建人：林欢
     * <li>创建日期：2016-6-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("all")
    public void deleteRepairProjectAndWorkSeq() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String[] errMsg = this.manager.validateDelete(ids);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.deleteRepairProjectAndWorkSeq(ids);
                map.put("success", true);
            } else {
                map.put("success", false);
                map.put("errMsg", errMsg);
            }           
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }    
}