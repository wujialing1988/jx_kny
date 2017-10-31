package com.yunda.baseapp.workcalendar.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.baseapp.workcalendar.entity.WorkCalendarInfo;
import com.yunda.baseapp.workcalendar.manager.WorkCalendarInfoManager;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：谭诚
 * <li>创建日期：2013-7-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class WorkCalendarInfoAction extends JXBaseAction<WorkCalendarInfo, WorkCalendarInfo, WorkCalendarInfoManager>  {
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
     * 
     * <li>说明：读取默认工作时间设置等内容
     * <li>创建人：谭诚
     * <li>创建日期：2013-6-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
	public void getWorkCalendarInfo() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
        	//String mainTableIdx = "402880f23eab2bed013eac082ffb000c";
            String mainTableIdx = getRequest().getParameter("infoIdx");//工作日历主表idx
            WorkCalendarInfo defInfo = this.manager.findDefaultWorkCalendarInfo(mainTableIdx);
            map.put("defInfo", defInfo);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }        
    }
	
	/**
     * <li>说明：新增及更新默认工作日历
     * <li>创建人：谭诚
     * <li>创建日期：2013-7-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
	public void saveOrUpdate() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			WorkCalendarInfo t = (WorkCalendarInfo)JSONUtil.read(getRequest(), entity.getClass());
			String[] errMsg = this.manager.validateUpdate(t);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.saveOrUpdate(t);
                t = this.manager.transEndTime(t);
				//返回记录保存成功的实体对象
				map.put("entity", t);  
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
     * <li>说明：查询工作日历列表
     * <li>创建人：程梅
     * <li>创建日期：2015-4-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void findCalendarInfoList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            map = this.manager.findCalendarInfoList(searchJson ,getStart(), getLimit(), getOrders()).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：设置指定的工作日历为默认工作日历
     * <li>创建人：何涛
     * <li>创建日期：2015-8-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateToDefault() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            this.manager.updateToDefault(id);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
}
