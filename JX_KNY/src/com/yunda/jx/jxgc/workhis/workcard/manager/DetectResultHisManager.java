package com.yunda.jx.jxgc.workhis.workcard.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jxgc.workhis.workcard.entity.DetectResultHis;
import com.yunda.jx.webservice.stationTerminal.base.entity.DataItemBean;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：DetectResultHis业务类,检测结果历史
 * <li>创建人：程锐
 * <li>创建日期：2012-12-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="detectResultHisManager")
public class DetectResultHisManager extends JXBaseManager<DetectResultHis, DetectResultHis>{

    /**
     * <li>说明：通过检测项id查询检测结果
     * <li>创建人：张迪
     * <li>创建日期：2016-9-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workTaskIDX 检测项idx
     * @return 检测结果
     */
    @SuppressWarnings("unchecked")
    public List<DataItemBean> getListByWorkTask(String workTaskIDX) {
        String hql = "select new DataItemBean(a.idx, a.detectItemContent, a.isNotBlank, a.detectResulttype, a.detectResult, a.detectItemStandard, a.minResult, a.maxResult )" +
                " from DetectResultHis a  where recordStatus = 0 and workTaskIDX = '" + workTaskIDX + "'";
        return daoUtils.find(hql);
    }
}