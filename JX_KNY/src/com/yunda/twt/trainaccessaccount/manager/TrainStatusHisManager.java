package com.yunda.twt.trainaccessaccount.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.entity.TrainPositionHIS;
import com.yunda.twt.trainaccessaccount.entity.TrainStatusHis;
import com.yunda.zb.common.ZbConstants;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：TrainStatusHis业务类,机车状态历史信息
 * <li>创建人：程锐
 * <li>创建日期：2015-01-26
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "trainStatusHisManager")
public class TrainStatusHisManager extends JXBaseManager<TrainStatusHis, TrainStatusHis> {
    
    /**
     * <li>说明：机车自动入段时批量新增或更新机车状态历史信息
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param accountList 机车入段实体对象列表
     * @throws Exception
     */
    public void saveOrUpdateListIn(List<TrainAccessAccount> accountList) throws Exception {
        List<TrainStatusHis> statusList = new ArrayList<TrainStatusHis>();
        StringBuilder accountIDXS = new StringBuilder();
        for (TrainAccessAccount entity : accountList) {            
            if (isSameTrainStatus(entity.getIdx(), entity.getTrainStatus()))
                continue;
            TrainStatusHis status = new TrainStatusHis();
            status.setTrainAccessAccountIDX(entity.getIdx());
            status.setStartTime(new Date());
            status.setStatus(entity.getTrainStatus());
            statusList.add(status);
            accountIDXS.append(entity.getIdx()).append(",");
        }
        if (accountList != null && accountList.size() > 0 && accountIDXS.toString().endsWith(","))
            accountIDXS.deleteCharAt(accountIDXS.length() - 1);
        String accountIdxsStr = CommonUtil.buildInSqlStr(accountIDXS.toString());
        if (!StringUtil.isNullOrBlank(accountIdxsStr))
            updateListEndTime(accountIdxsStr);        
        daoUtils.getHibernateTemplate().saveOrUpdateAll(statusList);
    }
    
    /**
     * <li>说明：新增及更新机车状态历史信息
     * <li>创建人：程锐
     * <li>创建日期：2015-3-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param accountIDX 机车出入段台账IDX
     * @param trainStatus 机车状态
     * @throws Exception
     */
    public void saveAndUpdateStatusHis(String accountIDX, String trainStatus) throws Exception {
        if (isSameTrainStatus(accountIDX, trainStatus))
            return;
        TrainStatusHis status = new TrainStatusHis();
        status.setTrainAccessAccountIDX(accountIDX);
        status.setStartTime(new Date());
        status.setStatus(trainStatus);
        String accountIdxsStr = CommonUtil.buildInSqlStr(accountIDX);
        if (StringUtil.isNullOrBlank(accountIdxsStr))
            return;
        updateListEndTime(accountIdxsStr);
        saveOrUpdate(status);
    }
    
    /**
     * <li>说明：机车自动出段时更新机车状态历史信息
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param accountList 机车出段实体对象列表
     * @throws Exception
     */
    public void saveOrUpdateListOut(List<TrainAccessAccount> accountList) throws Exception {
        StringBuilder accountIDXS = new StringBuilder();
        for (TrainAccessAccount entity : accountList) {
            accountIDXS.append(entity.getIdx()).append(",");
        }
        if (accountList != null && accountList.size() > 0)
            accountIDXS.deleteCharAt(accountIDXS.length() - 1);
        String accountIdxsStr = CommonUtil.buildInSqlStr(accountIDXS.toString());
        if (StringUtil.isNullOrBlank(accountIdxsStr))
            return ;
        updateListEndTime(accountIdxsStr);
    }
    
    /**
     * <li>说明：机车手动出段时更新机车状态历史信息
     * <li>创建人：程锐
     * <li>创建日期：2015-3-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param accountIDX 机车出入段台账IDX
     * @throws Exception
     */
    public void updateOut(String accountIDX) throws Exception {
        String accountIdxsStr = CommonUtil.buildInSqlStr(accountIDX);
        if (StringUtil.isNullOrBlank(accountIdxsStr))
            return ;
        updateListEndTime(accountIdxsStr);
    }
    
    /**
     * <li>说明：批量更新下设备时间为空的状态历史的下设备时间为当前时间
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param accountIdxsStr 机车出入段实体IDX的sql字符串
     */
    public void updateListEndTime(String accountIdxsStr) {
        String sql = SqlMapUtil.getSql("twt-status:updateStationHIS")
                               .replace("#endTime#", DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace(ZbConstants.IDXS, accountIdxsStr);
        daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：状态是否相同
     * <li>创建人：程锐
     * <li>创建日期：2015-4-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param accountIDX 机车出入段台账IDX
     * @param trainStatus 状态名称
     * @return true 相同 false 不同
     */
    @SuppressWarnings("unchecked")
    private boolean isSameTrainStatus(String accountIDX, String trainStatus) {
        if (StringUtil.isNullOrBlank(accountIDX) || StringUtil.isNullOrBlank(trainStatus))
            return true;
        String hql = "from TrainStatusHis where 1=1 and endTime is null and trainAccessAccountIDX = ? and status = ?";
        List<TrainPositionHIS> list = daoUtils.find(hql, new Object[]{accountIDX, trainStatus});
        return list != null && list.size() > 0;
    }
}
