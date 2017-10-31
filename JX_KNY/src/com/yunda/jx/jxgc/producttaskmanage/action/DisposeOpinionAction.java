package com.yunda.jx.jxgc.producttaskmanage.action; 

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.jx.jxgc.producttaskmanage.entity.DisposeOpinion;
import com.yunda.jx.jxgc.producttaskmanage.manager.DisposeOpinionManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：DisposeOpinion控制器, 处理意见
 * <li>创建人：程锐
 * <li>创建日期：2012-12-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class DisposeOpinionAction extends JXBaseAction<DisposeOpinion, DisposeOpinion, DisposeOpinionManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
     * TODO V3.2.1代码重构
	 * <li>方法名称：newSign
	 * <li>方法说明：新签名 
	 * <li>业务场景：多个人工节点的公用签名页面（/jsp/jx/workflow/Sign.jsp）调用
	 * <li>@throws JsonMappingException
	 * <li>@throws IOException
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2013-3-13 上午10:17:08
	 * <li>修改人：
	 * <li>修改内容：
	 */
//	public void newSign() throws JsonMappingException, IOException{
//	    Map<String, Object> map = new HashMap<String,Object>();
//        try {
//            DisposeOpinion t = (DisposeOpinion)JSONUtil.read(getRequest(), entity.getClass());
//            String[] errMsg = this.manager.validateUpdate(t);
//            if (errMsg == null || errMsg.length < 1) {
//            	String ctrl = getRequest().getParameter("ctrl");
//            	if(Boolean.parseBoolean(ctrl)){
//            		TecProcessNodeCaseManager tecManaget = (TecProcessNodeCaseManager) this.getManager("tecProcessNodeCaseManager");            		
//            		String res = tecManaget.manualControl(t.getRdpIdx());
//            		if(StringUtils.isNotBlank(res)){
//            			map.put("success", false);
//            			map.put("errMsg", res);
//            			return;
//            		}
//            	}
//                this.manager.saveAndFinishWorkItem(t, SystemContext.getAcOperator());
//                map.put("success", true);
//            } else {
//                map.put("success", false);
//                map.put("errMsg", errMsg);
//            }
//        } catch (Exception e) {
//        	ExceptionUtil.process(e, logger, map);
//        } finally {
//            JSONUtil.write(this.getResponse(), map);
//        }
//	}
	
	/**
	 * <li>方法名：newSignForMustCheck
	 * <li>业务场景：检修流程的【机车验交】人工节点页面调用
	 * <li>@throws Exception
	 * <li>返回类型：void
	 * <li>说明：必检项人工签名记录
	 * <li>创建人：袁健
	 * <li>创建日期：2013-11-21
	 * <li>修改人： 
	 * <li>修改日期：
	*/
	public void newSignForMustCheck() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			DisposeOpinion disposeOpinion = new DisposeOpinion();
			String processInstID = getRequest().getParameter("processInstID");
			if(StringUtils.isNotBlank(processInstID)){
				disposeOpinion.setProcessInstID(Long.parseLong(processInstID));
			}
			String activityInstID = getRequest().getParameter("activityInstID");
			if(StringUtils.isNotBlank(activityInstID)){
				disposeOpinion.setActivityInstID(Long.parseLong(activityInstID));
			}
			String workItemID = getRequest().getParameter("workItemID");
			if(StringUtils.isNotBlank(workItemID)){
				disposeOpinion.setWorkItemID(Long.parseLong(workItemID));
			}
			String rdpIdx = getRequest().getParameter("rdpIdx");
			if(StringUtils.isNotBlank(rdpIdx)){
				disposeOpinion.setBusinessIDX(rdpIdx);
			}
			String disposePersonID = getRequest().getParameter("disposePersonID");
			if(StringUtils.isNotBlank(disposePersonID)){
				disposeOpinion.setDisposePersonID(disposePersonID);
			}
			
			String disposePerson = getRequest().getParameter("disposePerson");
			if(StringUtils.isNotBlank(disposePerson)){
				disposeOpinion.setDisposePerson(disposePerson);
			}
			String workItemName = getRequest().getParameter("workItemName");
			if(StringUtils.isNotBlank(workItemName)){
				disposeOpinion.setWorkItemName(workItemName);
			}
			disposeOpinion.setDisposeTime(new Date());
			disposeOpinion.setDisposeOpinion("");
			disposeOpinion.setSignType(DisposeOpinion.SIGN_TYPE_MUST_CHECK);
			this.getManager().saveOrUpdate(disposeOpinion);
		    map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
		    JSONUtil.write(this.getResponse(), map);
		}
	}
	
	/**
	 * <li>方法说明：批量签名 
	 * <li>方法名称：batchSign
	 * <li>业务场景：在流程工单里批量质量检查时调用 TODO 如确定质量检查流程不用则此方法可删除
	 * <li>@throws JsonMappingException
	 * <li>@throws IOException
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2013-7-5 上午11:35:40
	 * <li>修改人：
	 * <li>修改内容：
	 */
	public void batchSign() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            DisposeOpinion[] t = (DisposeOpinion[])JSONUtil.read(getRequest(), DisposeOpinion[].class);
            AcOperator ac = SystemContext.getAcOperator();
            for(int i = 0; i < t.length; i++){
            
                this.manager.saveAndFinishWorkItem(t[i], ac);
            }
            map.put("success", true);            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            e.getStackTrace();
            map.put("errMsg", e.getMessage());
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
	
	/**
	 * <li>方法说明：获取审核意见 
	 * <li>方法名称：getVerifyJudgment
	 * <li>
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2013-8-5 下午01:29:13
	 * <li>修改人：
	 * <li>修改内容：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void getVerifyJudgment() throws JsonMappingException, IOException{
	    Map<String, Object> map = new HashMap<String,Object>();
        try {
            String idx = getRequest().getParameter("idx");
            List<DisposeOpinion> list = this.manager.getVerifyJudgment(idx);
            map.put("success", true);
            map.put("list", list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
	}
	
    /**
     * <li>说明：获取处理意见下拉列表
     * <li>创建人：程锐
     * <li>创建日期：2013-9-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void comboList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String workItemId = req.getParameter("workItemId");
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }

    /**
     * 
     * <li>说明：根据流程实例ID获取处理意见分页列表
     * <li>创建人：程锐
     * <li>创建日期：2014-9-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public void getInfoPageList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String processInstID = getRequest().getParameter("processInstID");			
			map = this.manager.getInfoPageList(processInstID).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
}