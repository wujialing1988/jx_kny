package com.yunda.jx.jxgc.dispatchmanage.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.dispatchmanage.entity.RepairLine;
import com.yunda.jx.jxgc.dispatchmanage.entity.WorkStation;
import com.yunda.jx.jxgc.dispatchmanage.entity.WorkStationMobileDTO;
import com.yunda.jx.jxgc.dispatchmanage.entity.ZbglWorkStationBinding;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：ZbglWorkStationBindingManager业务类,整备工位与人员关联
 * <li>创建人：林欢
 * <li>创建日期：2016-07-08
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="zbglWorkStationBindingManager")
public class ZbglWorkStationBindingManager extends JXBaseManager<ZbglWorkStationBinding, ZbglWorkStationBinding>{
    
    /**
     * <li>说明：根据操作员ID获取人员绑定的工位信息 此处主键传人员绑定主键
     * <li>创建人：林欢
     * <li>创建日期：2016-07-08
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatIDX 操作人员ix
     * @return List<WorkStationMobileDTO> 工位信息
     */
    @SuppressWarnings("unchecked")
    public List<WorkStationMobileDTO> getBindingWorkStationByOperatIDX(String operatIDX) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select b.IDX as WORK_STATION_BINDING_IDX , a.IDX as WORK_STATION_IDX,a.work_station_name as WORK_STATION_NAME ");
        sb.append(" from zbgl_work_station_binding b, jxgc_work_station a ,JXGC_REPAIR_LINE line  ");
        sb.append(" where a.record_status = 0  and a.STATUS = "+WorkStation.USE_STATUS + " and line.STATUS = "+RepairLine.USE_STATUS);
        sb.append(" and b.work_station_idx = a.idx and a.REPAIR_LINE_IDX = line.idx  ");
        if(!StringUtil.isNullOrBlank(operatIDX)){
            sb.append(" and b.empid = '").append(operatIDX).append("'");
        }
        return this.daoUtils.executeSqlQueryEntity(sb.toString(), WorkStationMobileDTO.class);
    }

    /**
     * <li>说明：根据操作员ID获取人员未绑定的工位信息 此处主键传工位主键
     * <li>创建人：林欢
     * <li>创建日期：2016-07-08
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatIDX 操作人员ix
     * @return List<WorkStationMobileDTO> 工位信息
     */
    @SuppressWarnings("unchecked")
    public List<WorkStationMobileDTO> getUnBindingWorkStationByOperatIDX(String operatIDX) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select a.IDX as WORK_STATION_BINDING_IDX , a.IDX as WORK_STATION_IDX,a.work_station_name as WORK_STATION_NAME ");
        sb.append(" from jxgc_work_station a , JXGC_REPAIR_LINE line ");
        sb.append(" where a.REPAIR_LINE_IDX = line.idx and a.record_status = 0 and a.STATUS = "+WorkStation.USE_STATUS);
        sb.append(" and line.STATUS = "+RepairLine.USE_STATUS + " and a.idx not in ( select b.work_station_idx from zbgl_work_station_binding b  ");
        if(!StringUtil.isNullOrBlank(operatIDX)){
            sb.append(" where b.empid =  '").append(operatIDX).append("'");
        }
        sb.append(" )");
        return this.daoUtils.executeSqlQueryEntity(sb.toString(), WorkStationMobileDTO.class);
    }

    /**
     * <li>说明：绑定工位
     * <li>创建人：林欢
     * <li>创建日期：2016-07-08
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatIDX 操作人idx
     * @param workStationIDX 工位idx
     * @throws Exception
     */
    public void bindWorkStation(String operatIDX, String workStationIDX) {
       ZbglWorkStationBinding zbglWorkStationBinding = new ZbglWorkStationBinding();
       zbglWorkStationBinding.setEmpID(operatIDX);
       zbglWorkStationBinding.setWorkStationIDX(workStationIDX);
       this.save(zbglWorkStationBinding);
    }

    /**
     * <li>说明：解除绑定工位
     * <li>创建人：林欢
     * <li>创建日期：2016-07-08
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void unBindWorkStation(String workStationBindingIDX) {
        // 直接进行物理删除
        this.daoUtils.execute(" delete From ZbglWorkStationBinding where idx = ? ", workStationBindingIDX);
    }
    
}