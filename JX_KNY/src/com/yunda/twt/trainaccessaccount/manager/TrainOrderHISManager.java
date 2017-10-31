package com.yunda.twt.trainaccessaccount.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.twt.trainaccessaccount.entity.TrainOrderHIS;
import com.yunda.twt.trainaccessaccount.entity.TrainPositionHIS;
import com.yunda.zb.common.ZbConstants;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：TrainOrderHIS业务类,机车位置顺序历史信息
 * <li>创建人：程锐
 * <li>创建日期：2015-02-06
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "trainOrderHISManager")
public class TrainOrderHISManager extends JXBaseManager<TrainOrderHIS, TrainOrderHIS> {
    
    /**
     * <li>说明：机车自动入段或更新位置时批量新增机车位置顺序历史和更新下设备时间为空的记录的下设备时间为当前时间
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param positionList 机车位置历史实体列表
     * @param oldPositionIDXSStr 需要变更机车顺序的以,号分隔的机车位置历史IDX的sql字符串
     * @throws Exception
     */
    public void saveOrUpdateListIn(List<TrainPositionHIS> positionList, String oldPositionIDXSStr) throws Exception {
        List<TrainOrderHIS> list = new ArrayList<TrainOrderHIS>();
        for (TrainPositionHIS entity : positionList) {
            if (StringUtil.isNullOrBlank(entity.getEquipOrder()))
                continue;
            TrainOrderHIS order = new TrainOrderHIS();
            order.setIdx("");
            order.setTrainPositionHISIDX(entity.getIdx());
            order.setStartTime(new Date());
            order.setEquipOrder(entity.getEquipOrder());
            list.add(order);
        }
        if (!StringUtil.isNullOrBlank(oldPositionIDXSStr))
            updateListEndTime(oldPositionIDXSStr);        
        saveOrUpdate(list);
    }
    
    /**
     * <li>说明：批量更新下设备时间为空的位置历史的下设备时间为当前时间
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param positionIdxsStr 机车位置历史IDX的sql字符串
     * @throws Exception
     */
    public void updateListEndTime(String positionIdxsStr) throws Exception {
        String sql = SqlMapUtil.getSql("twt-position:updateOrderHIS")
                               .replace("#endTime#", DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace(ZbConstants.IDXS, positionIdxsStr);
        daoUtils.executeSql(sql); 
    }
    
    
    /**
     * <li>说明：批量更新下设备时间为空的位置历史的下设备时间为当前时间
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param accountIdxsStr 机车出入段台账IDX的sql字符串
     * @throws Exception
     */
    public void updateListEndTimeByAccount(String accountIdxsStr) throws Exception {
        String sql = SqlMapUtil.getSql("twt-position:updateOrderHISByAccount")
                               .replace("#endTime#", DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace(ZbConstants.IDXS, accountIdxsStr);
        daoUtils.executeSql(sql); 
    }
    /**
     * <li>说明：同一机车位置历史的机车顺序是否不同
     * <li>创建人：程锐
     * <li>创建日期：2015-4-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainPositionHISIDX 机车位置历史
     * @param equipOrder 机车顺序
     * @return true 不同，false 相同
     */
    @SuppressWarnings("unchecked")
    public boolean isDifferentOrder(String trainPositionHISIDX, String equipOrder) {
        if (StringUtil.isNullOrBlank(trainPositionHISIDX) || StringUtil.isNullOrBlank(equipOrder))
            return false;
        String hql = "from TrainOrderHIS where 1=1 and endTime is null and trainPositionHISIDX = ? and equipOrder = ?";
        List<TrainOrderHIS> list = daoUtils.find(hql, new Object[]{trainPositionHISIDX, equipOrder});
        return list == null || list.size() < 1;
    }
}