package com.yunda.jx.jxgc.producttaskmanage.qualitychek.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResult;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResultVO;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCResultManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 作业工单质检控制器
 * <li>创建人：程锐
 * <li>创建日期：2015-8-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.1
 */
@SuppressWarnings(value = "serial")
public class QCResultAction extends JXBaseAction<QCResult, QCResult, QCResultManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());		
    
	/**
	 * 
	 * <li>说明：完成质量检验结果
	 * <li>创建人：程锐
	 * <li>创建日期：2014-11-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateFinishQCResult() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			QCResultVO[] resultVO = (QCResultVO[])JSONUtil.read(getRequest(), QCResultVO[].class);
			this.manager.updateFinishQCResult(resultVO);
			map.put("success", true);
			
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
    
	/**
	 * 
	 * <li>说明：查询当前工单的质量检查项列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-11-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	public void findQCResultList() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpServletRequest req = getRequest();
			String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
			QCResult entity = (QCResult) JSONUtil.read(searchJson, QCResult.class);
			SearchEntity<QCResult> searchEntity = new SearchEntity<QCResult>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.findQCResultList(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
    
	/**
	 * <li>说明：根据作业工单idx获取需要指派的质量检查项列表（iPad应用）
	 * <li>创建人：何涛
	 * <li>创建日期：2015-7-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	public void getIsAssignCheckItems() throws Exception {
	    Map<String, Object> map = new HashMap<String, Object>();
	    try {
            // 作业工单idx，多个idx用,分隔
            String workCardIDXS = getRequest().getParameter("workCardIDXS");
	        List<QCResult> qCResults = this.manager.getIsAssignCheckItems(workCardIDXS);
            map.put("list", qCResults);
	    } catch (Exception e) {
	        ExceptionUtil.process(e, logger, map);
	    } finally {
	        JSONUtil.write(this.getResponse(), map);
	    }
	}
    
	/**
	 * <li>说明：联合分页查询，查询查询质量检查参与人员
	 * <li>创建人：何涛
	 * <li>创建日期：2016-05-06
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	public void acquirePageList() throws Exception {
	    Map<String, Object> map = new HashMap<String, Object>();
	    try {
            QCResult entity = JSONUtil.read(getRequest().getParameter(Constants.ENTITY_JSON), QCResult.class);
            SearchEntity<QCResult> searchEntity = new SearchEntity<QCResult>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.acquirePageList(searchEntity).extjsStore();
	    } catch (Exception e) {
	        ExceptionUtil.process(e, logger, map);
	    } finally {
	        JSONUtil.write(this.getResponse(), map);
	    }
	}
    
    /**
     * <li>说明：返修
     * <li>创建人：张迪
     * <li>创建日期：2016-8-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void updateToBack() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String[] rdpWorkCardIDXs = JSONUtil.read(req, String[].class); 
            String qCItemNo = req.getParameter("qCItemNo");     // 质量检查项编码
            this.manager.updateToBack(rdpWorkCardIDXs, qCItemNo);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
}
