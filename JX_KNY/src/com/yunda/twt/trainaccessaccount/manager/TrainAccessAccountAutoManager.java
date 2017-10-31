package com.yunda.twt.trainaccessaccount.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.manager.IEosDictEntryManager;
import com.yunda.jx.base.jcgy.entity.TrainType;
import com.yunda.jxpz.utils.SystemConfigUtil;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.webservice.client.ITWTTrainStatusService;
import com.yunda.twt.webservice.util.ITWTUtil;
import com.yunda.webservice.common.util.DefaultUserUtilManager;
import com.yunda.zb.common.ZbConstants;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车出入段台账自动出入段业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-2-9
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "trainAccessAccountAutoManager")
public class TrainAccessAccountAutoManager extends JXBaseManager<TrainAccessAccount, TrainAccessAccount> {
    
    /** 日志工具 */
    Logger logger = Logger.getLogger(this.getClass());
    
    /** 机车出入段台账查询业务类 */
    @Resource
    private TrainAccessAccountQueryManager trainAccessAccountQueryManager;
    
    /** 台位图通用业务类 */
    @Autowired
    private ITWTUtil twtUtil;
    
    /** 机车位置历史业务类 */
    @Resource
    private TrainPositionHISManager trainPositionHISManager;
    
    /** 机车出入段台账业务类 */
    @Resource
    private TrainAccessAccountManager trainAccessAccountManager;
    
    /** 机车状态历史业务类 */
    @Resource
    private TrainStatusHisManager trainStatusHisManager;
    
    /** 机车状态管理客户端业务类 */
    @Autowired
    private ITWTTrainStatusService twtTrainStatusService;
    
    /** 机车状态管理客户端业务类 */
    @Resource
    private IEosDictEntryManager iEosDictEntryManager;
    
    /**
     * <li>说明：机车自动入段（台位图）
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：汪东良
     * <li>修改日期：2016-07-21
     * <li>修改内容：此方法不推荐使用，后期会删除被@see TrainAccessInAndOutManager.saveOrUpdateTrainAccessIn(TrainAccessAccount trainAccessAccount)替换；
     * @param entityArray 机车入段台账实体数组
     * @return 机车入段实体列表
     * @throws Exception
     */
    @Deprecated
    public synchronized List<TrainAccessAccount> saveOrUpdateListIn(TrainAccessAccount[] entityArray) throws Exception {
    	entityArray = filterDuplicateTrain(entityArray);
    	if (entityArray == null || entityArray.length < 1)
    		return new ArrayList<TrainAccessAccount>();
        List<TrainAccessAccount> list = new ArrayList<TrainAccessAccount>();
        DefaultUserUtilManager.setDefaultOperator();
        //CRIF  2015-05-29  汪东良 高   存在性能问题，由于一次入段的机车比较多，且跟多的是进行数据匹配操作，建议是根据站点一次将在段机车查询出来后再做匹配。
        for (TrainAccessAccount entity : entityArray) {
            //CRIF  2015-05-20  汪东良 中   存在性能问题，根据GUID、别名和车型车号，可以一次查出来。而不需要分别查询三次。且请写注释。 回复：分别查是区别GUID、别名和车型车号可能符合多条记录，那这样就无法准确找到哪条记录来更新了 
            TrainAccessAccount account = trainAccessAccountQueryManager.findInAccountByTrainGUID(entity.getTrainGUID());
            if (account != null) {
                list = updateInAccount(account, entity, list);
                continue;
            }
            account = trainAccessAccountQueryManager.findInAccountByTrainAliasName(entity.getTrainAliasName());
            if (account != null) {
                list = updateInAccount(account, entity, list);
                continue;
            }
            account = trainAccessAccountQueryManager.findInAccountByTrainName(entity.getTrainTypeShortName(), entity.getTrainNo());
            if (account != null) {
                list = updateInAccount(account, entity, list);
                continue;
            }
            TrainType trainType = twtUtil.getTrainTypeByTrainInfo(entity.getTrainAliasName());
            if (trainType == null) {
                list = saveInAccount(entity, list);
                continue;
            }
            //处理机车别名解析出的车型车号存在于在段机车的情况：机车别名相同则更新，不同则不保存车型车号新增机车           
            account = trainAccessAccountQueryManager.findInAccountByTrainName(trainType.getShortName(), trainType.getTrainNo());
            if (account != null) {
                if (!StringUtil.isNullOrBlank(account.getTrainAliasName())) {
                    if (account.getTrainAliasName().equals(entity.getTrainAliasName())) {
                        list = updateInAccount(account, entity, list);
                        continue;
                    } else {
                        //手动入段机车未保存机车别名,台位图取到该数据时，会以车型加车号作为机车别名保存到数据库(如SS4G0101),当自动入段机车（别名为4G0101）时,解析出来车型、车号相同，这时判断数据库中记录的别名是否是车型加车号（说明是手动入段），如是则修改该别名为新传入的别名
                        if ((trainType.getShortName() + trainType.getTrainNo()).equals(account.getTrainAliasName())) {
                            list = updateInAccount(account, entity, list);
                            continue;
                        } else {
                            list = saveInAccount(entity, list);
                            continue;
                        }
                    }
                } else {
                    list = updateInAccount(account, entity, list);
                    continue;
                }
            }
            //处理在同一时间内入段的不同机车别名但解析成同一车型车号的情况：后一辆车不保存车型车号
            boolean isSameTrainTypeAndNo = false;
            for (TrainAccessAccount saveAccount : list) {
                if (StringUtil.isNullOrBlank(saveAccount.getTrainTypeShortName()) || StringUtil.isNullOrBlank(saveAccount.getTrainNo())) 
                    continue;
                if (saveAccount.getTrainTypeShortName().equals(trainType.getShortName())
                    && saveAccount.getTrainNo().equals(trainType.getTrainNo())) {
                    list = saveInAccount(entity, list);
                    isSameTrainTypeAndNo = true;
                    break;
                }
            }
            if (isSameTrainTypeAndNo) 
                continue;
            entity.setTrainTypeShortName(trainType.getShortName());
            entity.setTrainNo(trainType.getTrainNo());
            account = trainAccessAccountQueryManager.getUpdateOutedAccount(entity);
            if (account != null) {
                list = updateOutedAccount(account, entity, list);
                continue;
            }
            entity = trainAccessAccountManager.setInEntityProperty(trainType.getTrainNo(), trainType, "", entity);
            trainAccessAccountManager.saveJczlAndUndertakeTrain(entity);
            list = saveInAccount(entity, list);
        }        
        
        if (list == null || list.size() < 1)
            return list;
        daoUtils.getHibernateTemplate().saveOrUpdateAll(list);
        logger.info("-----------------------自动入段：入段机车数：" + list.size());
        daoUtils.flush();
        List<TrainAccessAccount> updatePositionList = trainPositionHISManager.saveOrUpdateListIn(list);
        if (updatePositionList != null && updatePositionList.size() > 0) {
            daoUtils.getHibernateTemplate().saveOrUpdateAll(updatePositionList);
            logger.info("-----------------------自动入段：更新位置机车数：" + updatePositionList.size());
        }
        trainStatusHisManager.saveOrUpdateListIn(list);
        daoUtils.flush();
        updateTWTStatusByAccountList(list);
        logger.info("-----------------------自动入段：更新状态机车数：" + list.size());
        return list;
    }

    /**
     * <li>说明：机车自动出段（台位图）)
     * <li>创建人：程锐
     * <li>创建日期：2015-4-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entityArray 机车入段台账实体数组
     * @return 错误信息
     * @throws Exception
     */
    public String saveOrUpdateListOut(TrainAccessAccount[] entityArray) throws Exception {
        List<TrainAccessAccount> updateList = new ArrayList<TrainAccessAccount>();
        for (TrainAccessAccount entity : entityArray) {
            TrainAccessAccount account = trainAccessAccountQueryManager.findInAccountByTrainGUID(entity.getTrainGUID());
            if (account != null) {
                updateList = updateOutAccount(account, entity, updateList);
                continue;
            }
            account = trainAccessAccountQueryManager.findInAccountByTrainAliasName(entity.getTrainAliasName());
            if (account != null) {
                updateList = updateOutAccount(account, entity, updateList);
                continue;
            }
            Map<String, String> map = trainAccessAccountQueryManager.analysisTrainInfo(entity.getTrainAliasName());
            if (map != null) {
                account = trainAccessAccountQueryManager.findInAccountByTrainName(map.get("trainTypeShortName"), map.get("trainNo"));
                if (account != null) {
                    updateList = updateOutAccount(account, entity, updateList);
                    continue;
                }
            }
            account = trainAccessAccountQueryManager.findInAccountByTrainName(entity.getTrainTypeShortName(), entity.getTrainNo());
            if (account != null) {
                updateList = updateOutAccount(account, entity, updateList);
                continue;
            }
//            如果机车别名解析出两个相同车型车号的车，则可能会误操作车出段
//            TrainType trainType = twtUtil.getTrainTypeByTrainInfo(entity.getTrainAliasName());
//            if (trainType == null) {
//                continue;
//            }
//            account = trainAccessAccountQueryManager.findInAccountByTrainName(trainType.getShortName(), trainType.getTrainNo());
//            if (account != null) {
//                updateList = updateOutAccount(account, entity, updateList);
//                continue;
//            }
        }
        if (updateList == null || updateList.size() < 1)
            return "无相应机车出段";
        daoUtils.getHibernateTemplate().saveOrUpdateAll(updateList);
        daoUtils.flush();
        trainPositionHISManager.saveOrUpdateListOut(updateList);
        trainStatusHisManager.saveOrUpdateListOut(updateList);
        logger.info("-----------------------自动出段：出段机车数：" + updateList.size());
        return null;
    }

    /**
     * <li>说明：更新机车位置（台位图）
     * <li>创建人：程锐
     * <li>创建日期：2015-4-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entityArray 机车入段台账实体数组
     * @return 错误信息
     * @throws Exception
     */
    public String updateListPosition(TrainAccessAccount[] entityArray) throws Exception {
        List<TrainAccessAccount> list = new ArrayList<TrainAccessAccount>();
        for (TrainAccessAccount entity : entityArray) {
            TrainAccessAccount account = trainAccessAccountQueryManager.findInAccountByTrainGUID(entity.getTrainGUID());
            if (account != null) {
                list = updatePositionAccount(account, entity, list);
                continue;
            }
            account = trainAccessAccountQueryManager.findInAccountByTrainAliasName(entity.getTrainAliasName());
            if (account != null) {
                list = updatePositionAccount(account, entity, list);
                continue;
            }
            Map<String, String> map = trainAccessAccountQueryManager.analysisTrainInfo(entity.getTrainAliasName());
            if (map != null) {
                account = trainAccessAccountQueryManager.findInAccountByTrainName(map.get("trainTypeShortName"), map.get("trainNo"));
                if (account != null) {
                    list = updatePositionAccount(account, entity, list);
                    continue;
                }
            }
            account = trainAccessAccountQueryManager.findInAccountByTrainName(entity.getTrainTypeShortName(), entity.getTrainNo());
            if (account != null) {
                list = updatePositionAccount(account, entity, list);
                continue;
            }
            TrainType trainType = twtUtil.getTrainTypeByTrainInfo(entity.getTrainAliasName());
            if (trainType == null) {
                list = saveInAccount(entity, list);
                continue;
            }
            //处理机车别名解析出的车型车号存在于在段机车的情况：机车别名相同则更新，不同则不保存车型车号新增机车
            account = trainAccessAccountQueryManager.findInAccountByTrainName(trainType.getShortName(), trainType.getTrainNo());
            if (account != null) {
                if (!StringUtil.isNullOrBlank(account.getTrainAliasName())) {
                    if (account.getTrainAliasName().equals(entity.getTrainAliasName())) {
                        list = updatePositionAccount(account, entity, list);
                        continue;
                    } else {
                        list = saveInAccount(entity, list);
                        continue;
                    }
                } else {
                    list = updatePositionAccount(account, entity, list);
                    continue;
                }
            }
            //解决在同一时间内入段的不同机车别名但解析成同一车型车号的情况：后一辆车不保存车型车号
            boolean isSameTrainTypeAndNo = false;
            for (TrainAccessAccount saveAccount : list) {
                if (StringUtil.isNullOrBlank(saveAccount.getTrainTypeShortName()) || StringUtil.isNullOrBlank(saveAccount.getTrainNo())) 
                    continue;
                if (saveAccount.getTrainTypeShortName().equals(trainType.getShortName())
                    && saveAccount.getTrainNo().equals(trainType.getTrainNo())) {
                    list = saveInAccount(entity, list);
                    isSameTrainTypeAndNo = true;
                    break;
                }
            }
            if (isSameTrainTypeAndNo) 
                continue;
            
            entity.setTrainTypeShortName(trainType.getShortName());
            entity.setTrainNo(trainType.getTrainNo());
            account = trainAccessAccountQueryManager.getUpdateOutedAccount(entity);
            if (account != null) {
                list = updateOutedAccount(account, entity, list);
                continue;
            }
            entity = trainAccessAccountManager.setInEntityProperty(trainType.getTrainNo(), trainType, "", entity);
            trainAccessAccountManager.saveJczlAndUndertakeTrain(entity);
            list = saveInAccount(entity, list);            
        }
        if (list == null || list.size() < 1)
            return "无相应机车更新位置";
        daoUtils.getHibernateTemplate().saveOrUpdateAll(list);
        logger.info("-----------------------更新位置：更新及新增机车数：" + list.size());
        daoUtils.flush();
        List<TrainAccessAccount> updatePositionList = trainPositionHISManager.saveOrUpdateListIn(list);
        if (updatePositionList != null && updatePositionList.size() > 0) {
            daoUtils.getHibernateTemplate().saveOrUpdateAll(updatePositionList);
            logger.info("-----------------------更新位置：更新位置历史机车数：" + updatePositionList.size());
        }
            
        trainStatusHisManager.saveOrUpdateListIn(list);
        logger.info("-----------------------更新位置：更新状态机车数：" + list.size());
        return null;
    }
    
    /**
     * 
     * <li>说明：更新所有在轨道上的在段机车的设备位置信息
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trackGUIDS 以,分隔的轨道GUID字符串
     * @throws Exception
     */
    public void updatePostionByTrackGUID(String trackGUIDS) throws Exception {            
        String trackGUIDSql = CommonUtil.buildInSqlStr(trackGUIDS);
        if (StringUtil.isNullOrBlank(trackGUIDSql))
            throw new BusinessException("以,号分隔的轨道GUID字符串为空");
        String sql = SqlMapUtil.getSql("twt-position:updateOrderHISByTrackGUID")
                               .replace("#endTime#", DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace("#trackGUIDS#", trackGUIDSql);
        daoUtils.executeSql(sql);
        sql = SqlMapUtil.getSql("twt-position:updatePostionHISByTrackGUID")
                               .replace("#endTime#", DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace("#trackGUIDS#", trackGUIDSql);
        daoUtils.executeSql(sql);
        sql = SqlMapUtil.getSql("twt-position:updatePostionByTrackGUID")
                               .replace(ZbConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace("#trackGUIDS#", trackGUIDSql);
        daoUtils.executeSql(sql);          
    }
    
    /**
     * <li>说明：机车自动入段时对更新的机车入段实体的操作
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param account 现数据库中机车入段实体对象
     * @param entity 台位图传递的入段实体
     * @param updateList 机车入段实体对象列表
     * @return 机车入段实体对象列表
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private List<TrainAccessAccount> updateInAccount(TrainAccessAccount account, TrainAccessAccount entity, List<TrainAccessAccount> updateList)
        throws IllegalAccessException, InvocationTargetException {
        account = copyProperties(account, entity);
        account = trainAccessAccountManager.setDeportInfo(account);
        updateList.add(account);
        return updateList;
    }
    
    /**
     * <li>说明：机车自动入段时对新增的机车入段实体操作
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 台位图传递的入段实体
     * @param saveList 机车入段实体对象列表
     * @return 机车入段实体对象列表
     * @throws NoSuchFieldException
     */
    private List<TrainAccessAccount> saveInAccount(TrainAccessAccount entity, List<TrainAccessAccount> saveList) throws NoSuchFieldException {
        entity = trainAccessAccountManager.buildTrainInEntity(entity);
        entity = EntityUtil.setSysinfo(entity);
        entity = EntityUtil.setNoDelete(entity);
        saveList.add(entity);
        return saveList;
    }
    
    /**
     * <li>说明：机车自动入段时对在默认设置时间内出段的出段实体操作
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param account 现数据库中机车出段实体对象
     * @param entity 台位图传递的入段实体
     * @param updateOutedList 机车入段实体对象列表
     * @return 机车入段实体对象列表
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private List<TrainAccessAccount> updateOutedAccount(TrainAccessAccount account, TrainAccessAccount entity, List<TrainAccessAccount> updateOutedList)
        throws IllegalAccessException, InvocationTargetException {
        account = copyProperties(account, entity);
        account = trainAccessAccountManager.setClearOutEntity(account);
        updateOutedList.add(account);
        return updateOutedList;
    }
    
    /**
     * <li>说明：机车自动出段时对机车出段实体的操作
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param account 现数据库中机车出段实体对象
     * @param entity 台位图传递的出段实体
     * @param updateList 机车出段实体对象列表
     * @return 机车出段实体对象列表
     * @throws NoSuchFieldException
     */
    private List<TrainAccessAccount> updateOutAccount(TrainAccessAccount account, TrainAccessAccount entity, List<TrainAccessAccount> updateList)
        throws NoSuchFieldException {
        account = trainAccessAccountManager.buildTrainOutEntity(account, entity);
        account = EntityUtil.setSysinfo(account);
        updateList.add(account);
        return updateList;
    }
    
    /**
     * <li>说明：更新机车位置时对机车入段实体的操作
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param account 现数据库中机车入段实体对象
     * @param entity 台位图传递的入段实体
     * @param updateList 机车入段实体对象列表
     * @return 机车入段实体对象列表
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private List<TrainAccessAccount> updatePositionAccount(TrainAccessAccount account, TrainAccessAccount entity, List<TrainAccessAccount> updateList)
        throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        account = copyProperties(account, entity);
        account = EntityUtil.setSysinfo(account);
        updateList.add(account);
        return updateList;
    }
    
    /**
     * <li>说明：复制机车出入段属性值
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param account 现数据库中机车入段实体对象
     * @param entity 台位图传递的入段实体
     * @return 现数据库中机车入段实体对象
     */
    private TrainAccessAccount copyProperties(TrainAccessAccount account, TrainAccessAccount entity) {
        account.setEquipClass(entity.getEquipClass());
        account.setSiteID(entity.getSiteID());
        account.setEquipNo(entity.getEquipNo());
        account.setEquipName(entity.getEquipName());
        account.setOnEquipTime(entity.getOnEquipTime());
        account.setTrainAliasName(entity.getTrainAliasName());
        account.setTrainGUID(entity.getTrainGUID());
        account.setEquipGUID(entity.getEquipGUID());
        account.setEquipOrder(entity.getEquipOrder());
        return account;
    }
    
    /**
     * <li>说明：机车自动入段后调用台位图服务接口，更新台位图机车状态
     * <li>创建人：程锐
     * <li>创建日期：2015-3-31
     * <li>修改人： 程锐
     * <li>修改日期：2015-5-25
     * <li>修改内容：机车自动入段时查询数据字典中的入段去向，如只有一条记录则取出其在系统配置中的状态值为机车状态，如机车无状态则设置为配置的机车状态；如有状态但不等于配置的机车状态，则设置为配置的机车状态
     * @param list 机车出入段台账列表
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    private void updateTWTStatusByAccountList(List<TrainAccessAccount> list) throws BusinessException, NoSuchFieldException {
        String hql = "from EosDictEntry where status = '1' and id.dicttypeid='TWT_TRAIN_ACCESS_ACCOUNT_TOGO' order by id.dictid, sortno";
        List<EosDictEntry> eosdyList = iEosDictEntryManager.findToList(hql);
        String trainStatus = "";
        if (eosdyList != null && eosdyList.size() == 1) {
            EosDictEntry dict = eosdyList.get(0);
            if (dict.getId().getDictid().startsWith(TrainAccessAccount.TRAINTOGO_ZB_TYPE)) {
                trainStatus = SystemConfigUtil.getValue("ck.twt.trainStatus.".concat(TrainAccessAccount.TRAINTOGO_ZB_TYPE));
            } else if (dict.getId().getDictid().startsWith(TrainAccessAccount.TRAINTOGO_JX_TYPE)) {
                trainStatus = SystemConfigUtil.getValue("ck.twt.trainStatus.".concat(TrainAccessAccount.TRAINTOGO_JX_TYPE));
            } else if (dict.getId().getDictid().startsWith(TrainAccessAccount.TRAINTOGO_BZB)) {
                trainStatus = dict.getDictname();
            }                
        }          
        if (StringUtil.isNullOrBlank(trainStatus))
            return;
        for (TrainAccessAccount account : list) {
            if (StringUtil.isNullOrBlank(account.getToGo()))
                continue;
            if (StringUtil.isNullOrBlank(account.getTrainStatus())) {
                account.setTrainStatus(trainStatus);
                saveOrUpdate(account);
                twtTrainStatusService.updateTrainStatus(account, trainStatus);
            } else if (!trainStatus.equals(account.getTrainStatus())) {
                twtTrainStatusService.updateTrainStatus(account, trainStatus);
            }            
        }
        
    }
    
    /**
     * <li>说明：机车自动入段时从台位图传来的数据中过滤重复的车型车号
     * <li>创建人：程锐
     * <li>创建日期：2016-6-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entityArray 机车入段台账实体数组
     * @return 过滤后的机车入段台账实体数组
     * @throws Exception 
     */
    public TrainAccessAccount[] filterDuplicateTrain(TrainAccessAccount[] entityArray) throws Exception {
    	if (entityArray.length == 0)
    		return null;
    	List<TrainAccessAccount> filterList = new ArrayList<TrainAccessAccount>();
    	for (TrainAccessAccount account : entityArray) {    		
			String trainName = account.getTrainTypeShortName();
			String trainNo = account.getTrainNo();
			String trainAliasName = account.getTrainAliasName();
			boolean hasThisTrain = false;
			if (filterList != null && filterList.size() > 0) {
				for (TrainAccessAccount account2 : filterList) {
					String trainName1 = account2.getTrainTypeShortName();
					String trainNo1 = account2.getTrainNo();
					String trainAliasName1 = account2.getTrainAliasName();
					// 车型车号都相同则过滤掉
					if (!StringUtil.isNullOrBlank(trainName) && !StringUtil.isNullOrBlank(trainNo) 
							&& !StringUtil.isNullOrBlank(trainName1) && !StringUtil.isNullOrBlank(trainNo1)) {
						if (trainName.equals(trainName1) && trainNo.equals(trainNo1)) {
							hasThisTrain = true;
							break;
						}
					} 
					// 车型简称相同则过滤掉
					else if (!StringUtil.isNullOrBlank(trainAliasName) && !StringUtil.isNullOrBlank(trainAliasName1) ) {
						if (trainAliasName.equals(trainAliasName1)) {
							hasThisTrain = true;
							break;
						}
					}
				}
			}
			if (!hasThisTrain)
				filterList.add(account);
		}
    	entityArray = new TrainAccessAccount[filterList.size()];
    	int i = 0;
    	for (TrainAccessAccount account : filterList) {
    		entityArray[i] = account;
    		i++;
		}
    	return entityArray;
    }
}
