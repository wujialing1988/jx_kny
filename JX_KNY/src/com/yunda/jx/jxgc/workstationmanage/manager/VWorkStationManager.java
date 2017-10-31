package com.yunda.jx.jxgc.workstationmanage.manager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.workplanmanage.manager.VJobProcessNodeManager;
import com.yunda.jx.jxgc.workstationmanage.entity.VWorkStation;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: VWorkStation业务类, 机车检修台位占用计划Vis视图查询
 * <li>创建人：何涛
 * <li>创建日期：2015-4-21
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="vWorkStationManager")
public class VWorkStationManager extends JXBaseManager<VWorkStation, VWorkStation> {
    
    /** JobProcessNode业务类, 机车检修计划流程节点Vis视图查询 */
    @Resource
    private VJobProcessNodeManager vJobProcessNodeManager;
    
    /**
     * <li>说明：分页查询
     * <li>创建人：何涛
     * <li>创建日期：2015-04-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 包装了实体类查询条件的对象
     * @return Page<VWorkStation> 分页查询列表
     * @throws BusinessException
     */
    @Override
    public Page<VWorkStation> findPageList(SearchEntity<VWorkStation> searchEntity) throws BusinessException {
        // 查询SQL
        VWorkStation entity = searchEntity.getEntity();
        StringBuilder sql =
            new StringBuilder(
                "SELECT C.IDX, C.WORK_STATION_CODE, C.WORK_STATION_NAME, C.REPAIR_LINE_IDX, C.REPAIR_LINE_NAME, B.SEQ_NO FROM JXGC_WS_GROUP A, JXGC_WS_GROUP_ITEM B, JXGC_WORK_STATION C WHERE C.RECORD_STATUS = 0 AND A.IDX = B.WS_GROUP_IDX AND B.WS_IDX = C.IDX");
        if (!StringUtil.isNullOrBlank(entity.getWsGroupIDX())) {
            sql.append(" AND A.IDX = '").append(entity.getWsGroupIDX()).append("'");
        }
        // 默认排序
        sql.append(" ORDER BY B.SEQ_NO ASC");
        // 查询记录总数SQL
        StringBuilder totalSql = new StringBuilder("SELECT COUNT(*) AS ROWCOUNT ");
        totalSql.append(sql.substring(sql.indexOf("FROM")));
        // 分页查询
        Page<VWorkStation> page =
            this.queryPageList(totalSql.toString(), sql.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, VWorkStation.class);
        List<VWorkStation> list = page.getList();
        Date currentTime = Calendar.getInstance().getTime();
        for (VWorkStation station : list) {
            station.setJobProcessNodes(vJobProcessNodeManager.getModelsByWorkStationIDX(station.getIdx(), entity.getWorkPlanStatus(), entity
                .getTrainWorkPlanIDX(), currentTime));
        }
        return page;
    }
    
}
