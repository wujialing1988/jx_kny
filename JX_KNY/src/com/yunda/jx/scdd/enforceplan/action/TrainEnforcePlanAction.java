package com.yunda.jx.scdd.enforceplan.action; 

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.common.BusinessException;
import com.yunda.flow.snaker.entity.SnakerApprovalRecord;
import com.yunda.flow.snaker.manager.SnakerApprovalRecordManager;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.scdd.enforceplan.entity.TrainEnforcePlan;
import com.yunda.jx.scdd.enforceplan.manager.TrainEnforcePlanManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainEnforcePlan控制器, 机车施修计划
 * <li>创建人：程锐
 * <li>创建日期：2012-12-06
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class TrainEnforcePlanAction extends JXBaseAction<TrainEnforcePlan, TrainEnforcePlan, TrainEnforcePlanManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 流程审批记录 **/
    @Resource
    private SnakerApprovalRecordManager snakerApprovalRecordManager ;
    
	/**
     * 
     * <li>说明：分页查询，返回实体类的分页列表对象
     * <li>创建人：程锐
     * <li>创建日期：2012-12-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public void findPageList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            Map queryMap = JSONUtil.read(searchJson, Map.class); //将获取的字符串转换成map对象
              map = this.manager.findPageList(queryMap, getStart(), getLimit(),getOrders());
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>方法名称：getDateTree
     * <li>方法说明：生产计划日期段树 
     * <li>@throws BusinessException
     * <li>@throws ParseException
     * <li>@throws JsonMappingException
     * <li>@throws IOException
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-4-18 上午10:06:43
     * <li>修改人：
     * <li>修改内容：
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws ParseException 
     */
    @SuppressWarnings("unchecked")
    public void getDateTree() throws BusinessException, JsonMappingException, IOException, ParseException{
        String id = getRequest().getParameter("id");
        String month = getRequest().getParameter("month");
        String year = getRequest().getParameter("year");
        List<HashMap> children=manager.getDateTreeData(year, month, id);
        JSONUtil.write(getResponse(), children);
    }

    /**
	 * <li>说明：逻辑删除记录请求，向客户端返回操作结果（JSON格式）
	 * <li>创建人：袁健
	 * <li>创建日期：2013-08-09
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@Override
	public void logicDelete() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String[] errMsg = this.manager.validateDelete((Serializable[])ids);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.logicDelete((Serializable[])ids);
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
     * <li>说明：发起流程
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void sendProcess() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String idx = StringUtil.nvlTrim( req.getParameter("idx"), "" );
            this.manager.sendProcess(idx);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：流程审批
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void approvalProcess() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String idx = StringUtil.nvlTrim( req.getParameter("idx"), "" );
            String processInstID = StringUtil.nvlTrim( req.getParameter("processInstID"), "" );
            String workId = StringUtil.nvlTrim( req.getParameter("workId"), "" );
            String workName = StringUtil.nvlTrim( req.getParameter("workName"), "" );
            String opinions = StringUtil.nvlTrim( req.getParameter("opinions"), "" );
            String opinionsResult = StringUtil.nvlTrim( req.getParameter("opinionsResult"), "" );
            // 通过不同的处理结果调用不同的方法
            if(opinionsResult.equals(SnakerApprovalRecord.OPINION_TYPE_APPROVE)){
                // 同意
                this.manager.approvalProcess(idx,processInstID,workId);
                snakerApprovalRecordManager.createApprovalRecord(idx, processInstID, workId, workName, SnakerApprovalRecord.OPINION_TYPE_APPROVE , opinions);
            }else if(opinionsResult.equals(SnakerApprovalRecord.OPINION_TYPE_REFUSE)){
                // 不同意
                this.manager.refuseProcess(idx,processInstID,workId);
                snakerApprovalRecordManager.createApprovalRecord(idx, processInstID, workId, workName, SnakerApprovalRecord.OPINION_TYPE_REFUSE , opinions);
            }else if(opinionsResult.equals(SnakerApprovalRecord.OPINION_TYPE_APPROVE)){
                // 驳回
                this.manager.rejectProcess(idx,processInstID,workId);
                snakerApprovalRecordManager.createApprovalRecord(idx, processInstID, workId, workName, SnakerApprovalRecord.OPINION_TYPE_REJECT , opinions);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：获取待办计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getProcessWork() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            Map queryMap = JSONUtil.read(searchJson, Map.class); //将获取的字符串转换成map对象
            map = this.manager.getProcessWork(queryMap, getStart(), getLimit(),getOrders()).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
}