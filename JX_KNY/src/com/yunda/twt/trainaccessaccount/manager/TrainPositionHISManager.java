package com.yunda.twt.trainaccessaccount.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.entity.TrainPositionHIS;
import com.yunda.util.BeanUtils;
import com.yunda.zb.common.ZbConstants;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：TrainPositionHIS业务类,机车位置历史信息
 * <li>创建人：程锐
 * <li>创建日期：2015-02-06
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */ 
@Service(value = "trainPositionHISManager")
public class TrainPositionHISManager extends JXBaseManager<TrainPositionHIS, TrainPositionHIS> {
    
    /** 机车位置顺序历史信息业务类 */
    @Resource
    private TrainOrderHISManager trainOrderHISManager;
    
    /**
     * <li>说明：机车自动入段或更新位置时批量新增或更新机车位置及位置顺序历史信息
     * <li>创建人：程锐
     * <li>创建日期：2015-2-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param accountList 机车入段实体对象列表
     * @return 机车入段实体对象列表
     * @throws Exception
     */
    public List<TrainAccessAccount> saveOrUpdateListIn(List<TrainAccessAccount> accountList) throws Exception {
        List<TrainPositionHIS> positionList = new ArrayList<TrainPositionHIS>();
        List<TrainPositionHIS> oldPositionList = new ArrayList<TrainPositionHIS>();
        StringBuilder accountIDXS = new StringBuilder();
        StringBuilder oldPositionIDXS = new StringBuilder();
        for (TrainAccessAccount entity : accountList) {            
            if (StringUtil.isNullOrBlank(entity.getEquipGUID()) && StringUtil.isNullOrBlank(entity.getEquipName()))
                continue;
            TrainPositionHIS position = new TrainPositionHIS();            
            //机车位置未变化:如对应的顺序有变化则记录原有的位置历史信息以更新顺序
            if (isSameEquip(entity.getIdx(), entity.getEquipName())) {
                if (trainOrderHISManager.isDifferentOrder(entity.getTrainPositionHISIDX(), entity.getEquipOrder())) {
                    TrainPositionHIS oldPosition = getModelById(entity.getTrainPositionHISIDX());
                    oldPosition.setEquipOrder(entity.getEquipOrder());
                    oldPositionList.add(oldPosition);
                    oldPositionIDXS.append(entity.getTrainPositionHISIDX()).append(Constants.JOINSTR);                    
                }
                continue;
            }                
            //机车位置有变化时记录原有的位置历史信息以更新顺序
            if (!StringUtil.isNullOrBlank(entity.getTrainPositionHISIDX()) && !StringUtil.isNullOrBlank(entity.getEquipOrder())) 
                oldPositionIDXS.append(entity.getTrainPositionHISIDX()).append(Constants.JOINSTR);
            positionList.add(buildEntity(position, entity));
            accountIDXS.append(entity.getIdx()).append(Constants.JOINSTR);
        }
        if (accountList != null && accountList.size() > 0 && accountIDXS.toString().endsWith(Constants.JOINSTR))
            accountIDXS.deleteCharAt(accountIDXS.length() - 1);
        String accountIdxsStr = CommonUtil.buildInSqlStr(accountIDXS.toString());
        if (!StringUtil.isNullOrBlank(accountIdxsStr))
            updateListEndTime(accountIdxsStr);
         
        saveOrUpdate(positionList);        
        daoUtils.flush();
        List<TrainPositionHIS> list = new ArrayList<TrainPositionHIS>();
        list.addAll(positionList);
        list.addAll(oldPositionList);
        if (oldPositionList != null && oldPositionList.size() > 0 && oldPositionIDXS.toString().endsWith(Constants.JOINSTR))
            oldPositionIDXS.deleteCharAt(oldPositionIDXS.length() - 1);
        String oldPositionIDXSStr = CommonUtil.buildInSqlStr(oldPositionIDXS.toString());
        trainOrderHISManager.saveOrUpdateListIn(list, oldPositionIDXSStr);
        if (!StringUtil.isNullOrBlank(accountIdxsStr))
            return intoAccountPostionHisIDX(accountList, positionList); 
        else 
            return null;
    }
    
    /**
     * <li>说明：机车自动出段时批量更新机车位置及位置顺序历史信息
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
            accountIDXS.append(entity.getIdx()).append(Constants.JOINSTR);
        }
        if (accountList != null && accountList.size() > 0)
            accountIDXS.deleteCharAt(accountIDXS.length() - 1);
        String accountIdxsStr = CommonUtil.buildInSqlStr(accountIDXS.toString());
        if (StringUtil.isNullOrBlank(accountIdxsStr))
            return ;
        updateListEndTime(accountIdxsStr);
        trainOrderHISManager.updateListEndTimeByAccount(accountIdxsStr);
    }
    
    /**
     * <li>说明：批量更新下设备时间为空的位置历史的下设备时间为当前时间
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param accountIdxsStr 机车出入段实体IDX的sql字符串
     * @throws Exception
     */
    public void updateListEndTime(String accountIdxsStr) throws Exception {
        String sql = SqlMapUtil.getSql("twt-position:updatePostionHIS")
                               .replace("#endTime#", DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace(ZbConstants.IDXS, accountIdxsStr);
        daoUtils.executeSql(sql);  
    }
    
    /**
     * <li>说明：位置是否相同
     * <li>创建人：程锐
     * <li>创建日期：2015-4-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param accountIDX 机车出入段台账IDX
     * @param equipName 位置名称
     * @return true 相同 false 不同
     */
    @SuppressWarnings("unchecked")
    private boolean isSameEquip(String accountIDX, String equipName) {
        String hql = "from TrainPositionHIS where 1=1 and endTime is null and trainAccessAccountIDX = ? and equipName = ?";
        List<TrainPositionHIS> list = daoUtils.find(hql, new Object[]{accountIDX, equipName});
        return list != null && list.size() > 0;
    }
    
    /**
     * 
     * <li>说明：将机车位置历史主键反写入对应的机车出入段台账并返回机车出入段台账列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param accountList 机车出入段台账列表
     * @param positionList 机车位置历史列表
     * @return 机车出入段台账列表
     */
    private List<TrainAccessAccount> intoAccountPostionHisIDX(List<TrainAccessAccount> accountList, List<TrainPositionHIS> positionList) {
        List<TrainAccessAccount> newAccountList = new ArrayList<TrainAccessAccount>(); 
        for (TrainAccessAccount entity : accountList) {
            for (TrainPositionHIS position : positionList) {
                if (position.getTrainAccessAccountIDX().equals(entity.getIdx())) {
                    entity.setTrainPositionHISIDX(position.getIdx());
                    newAccountList.add(entity);
                    break;
                }
            }
        }
        return newAccountList;
    }
    
    /**
     * <li>说明：构建机车位置历史新增实体
     * <li>创建人：程锐
     * <li>创建日期：2015-4-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param position 机车位置历史实体
     * @param entity 机车出入段实体
     * @return 机车位置历史新增实体
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private TrainPositionHIS buildEntity(TrainPositionHIS position, TrainAccessAccount entity) throws IllegalAccessException, InvocationTargetException {
        BeanUtils.copyProperties(position, entity);
        position.setIdx("");
        position.setTrainAccessAccountIDX(entity.getIdx());
        position.setStartTime(new Date());
        return position;
    }
}