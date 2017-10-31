package com.yunda.jx.jxgc.workhis.workcard.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jxgc.workhis.workcard.entity.WorkTaskHis;
import com.yunda.jx.webservice.stationTerminal.base.entity.WorkTaskBean;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkTaskHis业务类,作业任务历史
 * <li>创建人：程锐
 * <li>创建日期：2012-12-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="workTaskHisManager")
public class WorkTaskHisManager extends JXBaseManager<WorkTaskHis, WorkTaskHis>{

    /**
     * 
     * <li>说明：根据作业卡ID获取作业任务历史列表
     * <li>创建人：张迪
     * <li>创建日期：2016-6-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业卡ID
     * @return List<WorkTask> 作业任务列表
     */
    @SuppressWarnings("unchecked")
    public List<WorkTaskBean> getListByWorkCard(String workCardIDX){
        String hql = "select new WorkTaskBean(a.idx, a.workTaskName,  a.repairStandard,  a.repairResult) from WorkTaskHis a where recordStatus = 0 and workCardIDX = '" + workCardIDX + "'";
        return daoUtils.find(hql);
    }
}