package com.yunda.jx.jxgc.dispatchmanage.webservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.dispatchmanage.entity.WorkStationMobileDTO;
import com.yunda.jx.jxgc.dispatchmanage.manager.ZbglWorkStationBindingManager;
import com.yunda.jx.pjjx.util.SystemContextUtil;

@Service(value = "zbglWorkStationBindingWS")
public class ZbglWorkStationBindingServiceImpl implements
		IZbglWorkStationBindingService {
	
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 *  工位绑定接口
	 */
	@Resource
	private ZbglWorkStationBindingManager zbglWorkStationBindingManager ;

    /**
     * <li>说明：根据操作员ID获取人员绑定的工位信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-7-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatIDX
     * @return
     */
	@SuppressWarnings("unchecked")
	public String getBindingWorkStationByOperatIDX(String operatIDX) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            
//          设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(Long.valueOf(operatIDX));
            //获取员工信息
            String empIDX = SystemContext.getOmEmployee().getEmpid().toString();
            
            List<WorkStationMobileDTO> list = zbglWorkStationBindingManager.getBindingWorkStationByOperatIDX(empIDX);
            map = new Page(list).extjsStore();
            return JSONUtil.write(map);
        } catch (Exception e) {
            logger.error("zbglWorkStationBindingService.getBindingWorkStationByOperatIDX：异常:" + e + "\n原因" + e.getMessage());
            return "error";
        }
	}
}
