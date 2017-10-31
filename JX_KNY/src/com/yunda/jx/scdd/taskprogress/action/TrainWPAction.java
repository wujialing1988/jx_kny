package com.yunda.jx.scdd.taskprogress.action; 

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.scdd.taskprogress.entity.TrainWP;
import com.yunda.jx.scdd.taskprogress.entity.TrainWPDetail;
import com.yunda.jx.scdd.taskprogress.manager.TrainWPManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainWP控制器, 机车作业进度
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-12-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class TrainWPAction extends JXBaseAction<TrainWP, TrainWP, TrainWPManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * 
	 * <li>说明：手动录入作业进度信息和进度项信息
	 * <li>创建人：程梅
	 * <li>创建日期：2013-12-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void saveTrainWPAndDetail() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
        	TrainWP wp = (TrainWP)JSONUtil.read(getRequest(), entity.getClass());
        	String trainWPDetails = StringUtil.nvlTrim( getRequest().getParameter("trainWPDetails"), "{}" );
        	TrainWPDetail[] trainWPDetailList = (TrainWPDetail[])JSONUtil.read(trainWPDetails, TrainWPDetail[].class);
            String[] errMsg = this.manager.validateUpdate(wp);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveTrainWPAndDetail(wp,trainWPDetailList);
//              返回记录保存成功的实体对象
                map.put("entity", wp);  
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
	 * <li>说明：根据所选作业计划新增作业进度信息
	 * <li>创建人：程梅
	 * <li>创建日期：2013-12-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	 public void saveOrUpdateList() throws Exception{
		 Map<String, Object> map = new HashMap<String,Object>();
		 try {
			TrainWP[] objList = (TrainWP[]) JSONUtil.read(getRequest(),	TrainWP[].class);
			String[] errMsg = this.manager.saveOrUpdateList(objList);
			if (errMsg == null || errMsg.length < 1) {
				map.put("success", "true");
			} else {
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
	 * <li>说明：生产任务启动后，自动录入作业进度信息
	 * <li>创建人：程锐
	 * <li>创建日期：2014-2-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param rdpIDX
	 */
	public void saveTrainWP(String rdpIDX) {        
		try {
			((TrainWPManager)getManager("trainWPManager")).saveTrainWP(rdpIDX);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
		}
	}
}