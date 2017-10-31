package com.yunda.jx.scdd.taskprogress.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.scdd.taskprogress.entity.TrainWPDetail;
import com.yunda.jx.scdd.taskprogress.manager.TrainWPDetailManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainWPDetail控制器, 机车作业进度项
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
public class TrainWPDetailAction extends JXBaseAction<TrainWPDetail, TrainWPDetail, TrainWPDetailManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * 
	 * <li>说明：查询进度项信息
	 * <li>创建人：程梅
	 * <li>创建日期：2013-12-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void findPageProgressList() throws JsonMappingException, IOException{
		Map<String, Object> map = new HashMap<String,Object>();
		try{
			String trainWPIDX = getRequest().getParameter("trainWPIDX"); 
			List<TrainWPDetail> list = this.manager.findPageProgressList(trainWPIDX);
			Page<TrainWPDetail> page = new Page<TrainWPDetail>(list.size(),list);
			map = page.extjsStore();
		} catch (Exception e) {
	    	ExceptionUtil.process(e, logger, map);
	    } finally {
	        JSONUtil.write(this.getResponse(), map);
	    }
	}
	/**
	 * 
	 * <li>说明：行编辑保存进度项信息
	 * <li>创建人：程梅
	 * <li>创建日期：2013-12-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void saveTrainWPDetail() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
        	TrainWPDetail wpDetail = (TrainWPDetail)JSONUtil.read(getRequest(), entity.getClass());
            String[] errMsg = this.manager.validateUpdate(wpDetail);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveTrainWPDetail(wpDetail);
//              返回记录保存成功的实体对象
                map.put("entity", wpDetail);  
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