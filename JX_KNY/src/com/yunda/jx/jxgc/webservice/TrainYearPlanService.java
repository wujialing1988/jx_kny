package com.yunda.jx.jxgc.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.webservice.entity.TrainYearPlanAndClassBean;
import com.yunda.jx.jxgc.workplanmanage.manager.TrainWorkPlanManager;
import com.yunda.util.BeanUtils;
import com.yunda.webservice.common.WsConstants;

@Service(value = "trainYearPlanServiceWS")
public class TrainYearPlanService implements ITrainYearPlanService {
    
    /**
     * 日志对象
     */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /* 机车检修作业计划业务类 */
    @Resource
    private TrainWorkPlanManager trainWorkPlanManager;
    
    /**
     * <li>说明：根据年份获取修程中机车计划台数、进车台数、竣车台数
     * <li>创建人：陈志刚
     * <li>创建日期：2016-11-09
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param year 年份
     * @return 修程中机车计划数量、进车数量、竣车数量的集合
     */
    public String getClassAndWorkPlan(String year) {
        try {
            Page<TrainYearPlanAndClassBean> page = trainWorkPlanManager.searchClassAndWorkPlan(year);
            List<TrainYearPlanAndClassBean> list = new ArrayList<TrainYearPlanAndClassBean>();
            list = BeanUtils.copyListToList(TrainYearPlanAndClassBean.class, page.getList());
            return JSONUtil.write(list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
}
