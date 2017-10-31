package com.yunda.jx.jxgc.recordscan.manager;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jxgc.recordscan.entity.RecordScanQuery;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.entity.WorkPlanRepairActivity;
import com.yunda.jx.jxgc.workplanmanage.manager.TrainWorkPlanManager;
import com.yunda.jx.jxgc.workplanmanage.manager.WorkPlanRepairActivityManager;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpManager;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecord;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 扫描二维码查看检修记录单
 * <li>创建人：张迪
 * <li>创建日期：2016-8-3
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="recordScanQueryManager")
public class RecordScanQueryManager extends JXBaseManager<RecordScanQuery, RecordScanQuery>{
    @Resource
    private PartsRdpRecordManager partsRdpRecordManager;
    @Resource
    private PartsRdpManager partsRdpManager;
    @Resource
    private TrainWorkPlanManager trainWorkPlanManager;
    @Resource
    private WorkPlanRepairActivityManager workPlanRepairActivityManager;
    
    /**
     * <li>说明：根据检修记录单查找检修兑现单
     * <li>创建人：张迪
     * <li>创建日期：2016-8-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 检修记录单id
     */
    public Map<String,Object> findRdpRecord(String idx) {
        Map<String, Object> map = new HashMap<String,Object>();
        PartsRdpRecord partsRecord = partsRdpRecordManager.getModelById(idx);
        if(null != partsRecord){ // 配件检修记录单
            PartsRdp partsRdp =  partsRdpManager.getModelById(partsRecord.getRdpIDX());
            map.put("partsRdp", partsRdp);
            map.put("partsRecord", partsRecord);          
            return map;
        }else {
            WorkPlanRepairActivity trainRecord = workPlanRepairActivityManager.getModelById(idx);
            if(null != trainRecord){ // 机车检修记录单   
                TrainWorkPlan trainRdp =  trainWorkPlanManager.getModelById(trainRecord.getWorkPlanIDX()); 
                map.put("trainRdp", trainRdp);
                map.put("trainRecord", trainRecord);
                return map;
            }else{
                throw new BusinessException(" 未找到对应的检修记录单! ");
            }
        }
    }
    
}
