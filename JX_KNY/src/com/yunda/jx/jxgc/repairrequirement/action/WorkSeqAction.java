package com.yunda.jx.jxgc.repairrequirement.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.repairrequirement.entity.QualityControl;
import com.yunda.jx.jxgc.repairrequirement.entity.WorkSeq;
import com.yunda.jx.jxgc.repairrequirement.manager.WorkSeqManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkSeq控制器, 工序卡
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
public class WorkSeqAction extends JXBaseAction<WorkSeq, WorkSeq, WorkSeqManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：新增作业工单基本信息
     * <li>创建人：程锐
     * <li>创建日期：2013-5-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容
     * @throws Exception
     */
    public void saveWorkSeq() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            WorkSeq workSeq = (WorkSeq)JSONUtil.read(getRequest(), entity.getClass());            
            String[] errMsg = this.manager.validateUpdate(workSeq);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveWorkSeq(workSeq);
//              返回记录保存成功的实体对象
                map.put("entity", workSeq);  
                map.put(Constants.SUCCESS, true);
            } else {
                map.put(Constants.SUCCESS, false);
                map.put("errMsg", errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * <li>方法名：saveWorkSeqForQrKey
     * <li>@throws Exception
     * <li>返回类型：void
     * <li>说明：保存作业工单时保存质量控制记录数组
     * <li>创建人：袁健
     * <li>创建日期：2013-6-18
     * <li>修改人： 
     * <li>修改日期：
     */
    public void saveWorkSeqForQrKey() throws Exception{
    	Map<String, Object> map = new HashMap<String,Object>();
    	try {
    	    //获取页面提交的工序卡对象
    		WorkSeq workSeq = (WorkSeq)JSONUtil.read(getRequest(), entity.getClass());     
    		String qualityControls = StringUtil.nvlTrim( getRequest().getParameter("qualityControls"), "{}" );
            //获取质量检查项
            QualityControl[] qualityControlList = (QualityControl[])JSONUtil.read(qualityControls, QualityControl[].class);
    		String[] errMsg = this.manager.validateUpdate(workSeq);
    		if (errMsg == null || errMsg.length < 1) {
    			this.manager.saveWorkSeqForQrKey(workSeq, qualityControlList);
//              返回记录保存成功的实体对象
    			map.put("entity", workSeq);  
    			map.put(Constants.SUCCESS, true);
    		} else {
    			map.put(Constants.SUCCESS, false);
    			map.put("errMsg", errMsg);
    		}
    	} catch (Exception e) {
    		ExceptionUtil.process(e, logger, map);
    	} finally {
    		JSONUtil.write(this.getResponse(), map);
    	}       
    }    
    
    /**
     * 
     * <li>说明：删除作业工单并且级联删除质量控制情况、检测/检修项目及项目关联的检测结果、检测项
     * <li>创建人：程锐
     * <li>创建日期：2013-5-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("all")
    public void deleteWorkSeq() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String[] errMsg = this.manager.validateDelete(ids);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.deleteWorkSeq(ids);
                map.put(Constants.SUCCESS, true);
            } else {
                map.put(Constants.SUCCESS, false);
                map.put("errMsg", errMsg);
            }           
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * <li>方法名称：seeWorkFlow
     * <li>业务场景：工序确认、工序延误调用
     * <li>方法说明：查看流程图 
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-3-7 上午10:06:06
     * <li>修改人：
     * <li>修改内容：
     */
    public String seeWorkFlow(){
        String processInstID = getRequest().getParameter("processInstID");
        String rdp = getRequest().getParameter("rdpIdx");
        if(processInstID != null){
            processInstID = this.manager.getParentProcessInstID(processInstID);
            
            return renderJSPPage("/jsp/jx/workflow/DrawWorkflow.jsp?processInstID=" + processInstID +"&rdpIdx="+rdp);            
        }        
        return null;
    } 
    
    /**
     * <li>方法说明：根据检修项目ID查询关联工序卡 
     * <li>方法名称：findWorkSeqByProject
     * <li>
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-9-9 下午04:40:49
     * <li>修改人：
     * <li>修改内容：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void findWorkSeqByProject() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            map = this.manager.findWorkSeqByProject(req.getParameter("entityJson"), getStart(), getLimit(), getOrders()).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>方法说明：作业计划编辑查询新增作业工单 
     * <li>方法名称：searchWorkSeq
     * <li>@throws JsonMappingException
     * <li>@throws IOException
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-9-22 下午04:30:08
     * <li>修改人：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    public void searchWorkSeq() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String activityIdx = req.getParameter("activityIdx");
            map = this.manager.searchWorkSeq(req.getParameter("entityJson"), getStart(), getLimit(), getOrders(), activityIdx).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>方法说明：根据检修节点idx查询该节点下挂的工序卡
     * <li>创建人：林欢
     * <li>创建时间：2016-6-4 下午04:40:49
     * <li>修改人：
     * <li>修改内容：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void findWorkSeqByNodeIDX() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            map = this.manager.findWorkSeqByNodeIDX(req.getParameter("entityJson"), getStart(), getLimit(), getOrders()).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>方法说明：查询当前流程下可以选择的检修工序卡
     * <li>方法名称：findWorkSeqByProject
     * <li>创建人：林欢
     * <li>创建时间：2016-6-8 下午04:40:49
     * <li>修改人：
     * <li>修改内容：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void findWorkCardInfo() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            map = this.manager.findWorkCardInfo(getWhereListJson(), getStart(), getLimit(), getOrders()).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}