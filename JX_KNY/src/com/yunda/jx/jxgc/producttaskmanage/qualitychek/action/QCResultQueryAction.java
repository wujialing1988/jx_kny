package com.yunda.jx.jxgc.producttaskmanage.qualitychek.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.alibaba.fastjson.JSONObject;
import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.base.jcqcitemdefine.entity.JCQCItemDefine;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResult;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCResultQueryManager;
import com.yunda.jx.jxgc.webservice.entity.TrainWorkPlanBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.ProcessTaskListBean;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 质量检查查询控制器
 * <li>创建人：程锐
 * <li>创建日期：2014-11-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class QCResultQueryAction extends JXBaseAction<ProcessTaskListBean, ProcessTaskListBean, QCResultQueryManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());	
	/**
	 * 
	 * <li>说明：查询当前人员的质量检查项列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-11-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void getQCList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
		try {
            // 默认查询抽检项
			String mode = StringUtil.nvl(getRequest().getParameter("mode"), JCQCItemDefine.CONST_INT_CHECK_WAY_CJ + "");
	        String queryString = StringUtil.nvl(getRequest().getParameter("query"), "");    
			map = this.manager.getQCPageList(SystemContext.getOmEmployee().getEmpid(), getStart(), getLimit(), mode,queryString).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
    }
	/**
	 * 
	 * <li>说明：查询兑现单
	 * <li>创建人：程锐
	 * <li>创建日期：2014-11-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	public void findRdp() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
            // 默认查询抽检项
			String mode = StringUtil.nvl(getRequest().getParameter("mode"), JCQCItemDefine.CONST_INT_CHECK_WAY_CJ + "");
			map = this.manager.getRdpOfQuery(SystemContext.getOmEmployee().getEmpid(), mode, getStart(), getLimit());
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
    }
	/**
	 * 
	 * <li>说明：查询勾选的质量检查项
	 * <li>创建人：程锐
	 * <li>创建日期：2014-12-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void findCheckQC() throws JsonMappingException, IOException{
	    Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String idx = StringUtil.nvlTrim( req.getParameter("idx"), "");
            List<QCResult> entityList = this.manager.getModelList(idx);
            map.put("entityList", entityList);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
	}
    
    /**
     * <li>说明：获取机车当前人员机车质量检查列表
     * <li>创建人：张迪
     * <li>创建日期：2016-8-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getRdpQCList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );  
            JSONObject ob = JSONObject.parseObject(searchJson);  
            // 获取操作人员         
            Long operatorId = ob.getLong(Constants.OPERATOR_ID);
            //如果有查询参数可以封装查询参数
            TrainWorkPlanBean searchBean = new TrainWorkPlanBean();
            map = this.manager.getRdpQCList(operatorId, new SearchEntity<TrainWorkPlanBean>(searchBean, getStart(), getLimit(), getOrders())).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * <li>说明：获取检修记录单(PAD)
     * <li>创建人：张迪
     * <li>创建日期：2016-8-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getTrainRecordList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String jsonObject = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" ); 
            map = this.manager.getRecordQCList(jsonObject).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }   

}
