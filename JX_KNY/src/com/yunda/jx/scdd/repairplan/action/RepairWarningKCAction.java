package com.yunda.jx.scdd.repairplan.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.scdd.repairplan.entity.RepairWarningKC;
import com.yunda.jx.scdd.repairplan.manager.RepairWarningKCManager;
import com.yunda.jx.util.Fastjson;

/**
 * <li>说明：客车修程预警
 * <li>创建人： 伍佳灵
 * <li>创建日期：2017年12月14日
 * <li>成都运达科技股份有限公司
 */
public class RepairWarningKCAction extends JXBaseAction<RepairWarningKC, RepairWarningKC, RepairWarningKCManager> {

	/**
	 * 日志
	 */
	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	 /**
     * <li>说明：同步车辆信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void synchronizeData() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errMsg = "";
        try {
            errMsg = this.manager.synchronizeData();
            if (errMsg == null)
                map.put(Constants.SUCCESS, true);
            else {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map, errMsg);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    
    /**
     * <li>说明：查询货车修程提醒
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param start 开始条数
     * @param limit 限制条数
     * @param queryInput 车型车号模糊查询
     * @param planDayFx 辅修年月
     * @param planDayDx 段修年月
     * @return
     */   
    public void findKCRepairWarningList() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String queryInput = req.getParameter("queryInput");
            Map<String, String> otherParams = new HashMap<String, String>();
            otherParams.put("repairType", req.getParameter("repairType"));
            map = this. manager.findKCRepairWarningList(getStart(),getLimit(),queryInput,otherParams).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }                 
    }
    
    
    /**
	 * <li>方法说明：重新计算下次修程
	 * <li>方法名：reCompute
	 * @throws IOException
	 * <li>创建人：伍佳灵
	 * <li>创建日期：2017-12-14
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public void reCompute() throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			this.manager.updateComputeNextRepair();
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
			e.printStackTrace();
		}finally{
			Fastjson.writer(getResponse(), map);
		}
	}
	
}
