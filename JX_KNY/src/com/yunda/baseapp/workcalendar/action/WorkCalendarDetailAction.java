package com.yunda.baseapp.workcalendar.action; 

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.baseapp.workcalendar.entity.WorkCalendarDetail;
import com.yunda.baseapp.workcalendar.entity.WorkCalendarInfo;
import com.yunda.baseapp.workcalendar.manager.WorkCalendarDetailManager;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkCalendarDetail控制器, 
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-06-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class WorkCalendarDetailAction extends JXBaseAction<WorkCalendarDetail, WorkCalendarDetail, WorkCalendarDetailManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * 
	 * <li>说明：测试结束日期算法
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-6-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @throws 抛出异常列表
	 */
	public void testGetWorkEndDate(){
//		//由开始和工时推算结束
//		Format format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
//		Date first = null;
//		try {
//			first = (java.util.Date) format.parseObject("20130503 02:00:00");
//		} catch (ParseException e) {
//			ExceptionUtil.process(e, logger);
//		}
//		long start = first.getTime();
//		start = Long.valueOf("1372059000000");
//		System.out.println("DateUtil-Date:"+DateUtil.getDateByMillSeconds(start));
//		System.out.println("DateUtil-String:"+DateUtil.getDateByMillSeconds(start,"yyyyMMdd HH:mm:ss"));
//		long end = 14400000;
//		this.manager.getFinalTime(start,end);
		//由开始和结束推算工时
		Format formats = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date beg = null;
		Date beg1 = null;
		
		try {
			beg = (java.util.Date) formats.parseObject("2013-06-24 11:41:33");
			beg1 = (java.util.Date) formats.parseObject("2013-06-24 11:56:50");
		} catch (ParseException e) {
			ExceptionUtil.process(e, logger);
		}
		this.manager.getRealWorkminutes(beg,beg1,null);
	}
	
	/**
     * 
     * <li>说明：根据选中日期, 读取其工作时间设置等内容
     * <li>创建人：谭诚
     * <li>创建日期：2013-6-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
	public void getWorkCalendarDetail() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
        	String mainTableIdx = getRequest().getParameter("infoIdx");//工作日历主表idx        	
            String gzrl = getRequest().getParameter("calDate");//所选择的工作日历日期
            WorkCalendarDetail detail = null;
            WorkCalendarInfo defInfo = null;
            if(!StringUtil.isNullOrBlank(gzrl)){
            	detail = this.manager.findWorkCalendarDetail(mainTableIdx, gzrl);
            	defInfo = this.manager.findDefaultWorkCalendarInfo(mainTableIdx);
            }
            map.put("entity", detail);
            map.put("defInfo", defInfo);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }        
    }
	
	/**
     * 
     * <li>说明：根据填报的表单内容,更新每个工作日的具体工作时间 
     * <li>创建人：谭诚
     * <li>创建日期：2013-6-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
	public void saveOrUpdate() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			WorkCalendarDetail t = (WorkCalendarDetail)JSONUtil.read(getRequest(), entity.getClass());
			String[] errMsg = this.manager.validateUpdate(t);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.saveOrUpdate(t);
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
     * 
     * <li>说明：用以颜色在日历上的回显
     * <li>创建人：谭诚
     * <li>创建日期：2013-6-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
	public void getCurrentMonthCalendar() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
        	String mainTableIdx = getRequest().getParameter("infoIdx");//工作日历主表idx
        	String yearAndMonth = getRequest().getParameter("yearAndMonth");
           
            List <WorkCalendarDetail> list = null;
            if(!StringUtil.isNullOrBlank(yearAndMonth)){
            	list = this.manager.findCurrentCalendar(mainTableIdx, yearAndMonth);
            }
            map.put("entity", list);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }        
    }
    
    /**
     * <li>说明：批量设置非工作为工作日
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-1-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void setVolumeType() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        Map<String, Object> params = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            WorkCalendarDetail detail = (WorkCalendarDetail)JSONUtil.read(req, entity.getClass());
            String volumeStart = req.getParameter("volumeStart");
            String volumeEnd = req.getParameter("volumeEnd");
            params.put("detail", detail); // 日历ID
            params.put("volumeStart", volumeStart); // 修改开始时间
            params.put("volumeEnd", volumeEnd); // 修改结束时间
            this.manager.setVolumeType(params);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
}