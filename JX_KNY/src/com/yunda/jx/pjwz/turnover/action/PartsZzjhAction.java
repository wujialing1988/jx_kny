package com.yunda.jx.pjwz.turnover.action;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.workplanmanage.manager.JobProcessNodeManager;
import com.yunda.jx.pjwz.turnover.entity.PartsFixUnloadBean;
import com.yunda.jx.pjwz.turnover.entity.PartsZzjh;
import com.yunda.jx.pjwz.turnover.manager.OffPartListManager;
import com.yunda.jx.pjwz.turnover.manager.PartsZzjhManager;

/**
 * 
 * <li>标题: PartsZzjhAction.java
 * <li>说明: 大部件周转
 * <li>创建人：曾雪
 * <li>创建日期：2016-7-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class PartsZzjhAction extends JXBaseAction<PartsZzjh, PartsZzjh, PartsZzjhManager>{

	 /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    private OffPartListManager offPartListManager;
    
    private JobProcessNodeManager jobProcessNodeManager;
    public String repairClassName;//修程
    public String repairtimeName;//修次
    public String workPlanIdx;
    
    public OffPartListManager getOffPartListManager() {
		return offPartListManager;
	}


	public void setOffPartListManager(OffPartListManager offPartListManager) {
		this.offPartListManager = offPartListManager;
	}


	public String getRepairClassName() {
		return repairClassName;
	}


	public void setRepairClassName(String repairClassName) {
		this.repairClassName = repairClassName;
	}


	public String getRepairtimeName() {
		return repairtimeName;
	}


	public void setRepairtimeName(String repairtimeName) {
		this.repairtimeName = repairtimeName;
	}


	public JobProcessNodeManager getJobProcessNodeManager() {
		return jobProcessNodeManager;
	}


	public void setJobProcessNodeManager(JobProcessNodeManager jobProcessNodeManager) {
		this.jobProcessNodeManager = jobProcessNodeManager;
	}


	public String getWorkPlanIdx() {
		return workPlanIdx;
	}


	public void setWorkPlanIdx(String workPlanIdx) {
		this.workPlanIdx = workPlanIdx;
	}


	/**
     * 
     * <li>说明：生成周转计划
     * <li>创建人：曾雪
     * <li>创建日期：2016-7-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public void saveOffPartList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			this.manager.saveOffPartList(ids, repairClassName, repairtimeName, workPlanIdx);
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	
	/**
	 * 
	 * <li>说明：更新计划时间
	 * <li>创建人：曾雪
	 * <li>创建日期：2016-7-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 */
	public void updatePlanTime() throws Exception{
		List<PartsZzjh> partsZzjh = new ArrayList<PartsZzjh>();
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			for (String id : ids) {
				PartsZzjh entity = this.getManager().getModelById(id);				
				this.manager.updatePlanTime(entity);
				
				partsZzjh.add(entity);
			}
			this.getManager().saveOrUpdate(partsZzjh);
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	
	
	
	/**
	 * 
	 * <li>说明：更新实际时间
	 * <li>创建人：曾雪
	 * <li>创建日期：2016-7-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	public void updateRealTime() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {						
			this.manager.UpdateRealTime(ids);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	
     /**
     * <li>说明：通过兑现单idx查询上下车配件信息
     * <li>创建人：张迪
     * <li>创建日期：2016-10-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findFixUnloadList() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), Constants.EMPTY_JSON_OBJECT);
            PartsFixUnloadBean objEntity = (PartsFixUnloadBean) JSONUtil.read(searchJson, PartsFixUnloadBean.class);
            SearchEntity<PartsFixUnloadBean> searchEntity = new SearchEntity<PartsFixUnloadBean>(objEntity, getStart(), getLimit(), getOrders());
            Page<PartsFixUnloadBean> page = this.manager.findFixUnloadList(searchEntity);
            map = page.extjsResult();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
}
