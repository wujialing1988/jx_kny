package com.yunda.twt.trainaccessaccount.manager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.base.jcgy.entity.TrainType;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicket;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdpBean;
import com.yunda.jxpz.utils.SystemConfigUtil;
import com.yunda.twt.trainaccessaccount.entity.InTrainDispatchCountBean;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.webservice.util.ITWTUtil;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车出入段台账查询业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-2-10
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "trainAccessAccountQueryManager")
public class TrainAccessAccountQueryManager extends JXBaseManager<TrainAccessAccount, TrainAccessAccount> {
    
    /** 台位图通用业务类 */
    @Autowired
    private ITWTUtil twtUtil;
    
    /** 车型常量 */    
    public static final String TRAINTYPESHORTNAME = "trainTypeShortName"; 
    
    /** 车号常量 */    
    public static final String TRAINNO = "trainNo"; 
    
    
    /**
     * <li>说明：根据机车简称和车号获取未出段的机车入段台账实体
     * <li>创建人：程锐
     * <li>创建日期：2015-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeShortName 机车简称
     * @param trainNo 车号
     * @return 未出段的机车入段台账实体
     */
    @SuppressWarnings("unchecked")
    public TrainAccessAccount findInAccountByTrainName(String trainTypeShortName, String trainNo) {
        if (StringUtil.isNullOrBlank(trainTypeShortName) && StringUtil.isNullOrBlank(trainNo))
            return null;
        Map paramMap = new HashMap<String, String>();
        paramMap.put(TRAINTYPESHORTNAME, trainTypeShortName);
        paramMap.put(TRAINNO, trainNo);
        return findInAccount(paramMap);
    }
    
    /**
     * <li>说明：根据机车GUID获取未出段的机车入段台账实体
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainGUID 机车GUID
     * @return 未出段的机车入段台账实体
     */
    @SuppressWarnings("unchecked")
    public TrainAccessAccount findInAccountByTrainGUID(String trainGUID) {
        if (StringUtil.isNullOrBlank(trainGUID))
            return null;
        Map paramMap = new HashMap<String, String>();
        paramMap.put("trainGUID", trainGUID);
        return findInAccount(paramMap);
    }
    
    /**
     * <li>说明：根据机车别名获取未出段的机车入段台账实体
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainAliasName 机车别名
     * @return 未出段的机车入段台账实体
     */
    @SuppressWarnings("unchecked")
    public TrainAccessAccount findInAccountByTrainAliasName(String trainAliasName) {
        if (StringUtil.isNullOrBlank(trainAliasName))
            return null;
        Map paramMap = new HashMap<String, String>();
        paramMap.put("trainAliasName", trainAliasName);
        return findInAccount(paramMap);
    }
        
    /**
     * <li>说明：根据机车ID和车号获取未出段的机车入段台账实体
     * <li>创建人：程锐
     * <li>创建日期：2015-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX 车型主键
     * @param trainNo 车号
     * @param idx 机车出入段台账ID
     * @return 未出段的机车入段台账实体
     */
    public TrainAccessAccount findInAccountByTrainIDX(String trainTypeIDX, String trainNo, String idx) {
        String hql = "from TrainAccessAccount where trainTypeIDX = ? and trainNo = ? and outTime = null and recordStatus = 0";
        if (!StringUtil.isNullOrBlank(idx)) {
            hql += " and idx != ?";
            return (TrainAccessAccount) daoUtils.findSingle(hql, trainTypeIDX, trainNo, idx);
        } else {
            return (TrainAccessAccount) daoUtils.findSingle(hql, trainTypeIDX, trainNo);
        }
    }
        
    /**
     * <li>说明：根据机车名称和车号获取未出段的机车入段台账实体
     * <li>创建人：程锐
     * <li>创建日期：2015-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeShortName 车型名称
     * @param trainNo 车号
     * @param idx 机车出入段台账ID
     * @return 未出段的机车入段台账实体
     */
    public TrainAccessAccount findInAccountByTrainName(String trainTypeShortName, String trainNo, String idx) {
        String hql = "from TrainAccessAccount where trainTypeShortName = ? and trainNo = ? and outTime = null and recordStatus = 0";
        if (!StringUtil.isNullOrBlank(idx)) {
            hql += " and idx != ?";
            return (TrainAccessAccount) daoUtils.findSingle(hql, trainTypeShortName, trainNo, idx);
        } else {
            return (TrainAccessAccount) daoUtils.findSingle(hql, trainTypeShortName, trainNo);
        }
    }
    
    /**
     * <li>说明：获取同一站点的在段机车列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param siteID 站场标示
     * @return 在段机车列表
     */
    @SuppressWarnings("unchecked")
    public List<TrainAccessAccount> getInTrainListBySiteID(String siteID) {
        if (StringUtil.isNullOrBlank(siteID))
            return null;
        Map paramMap = new HashMap<String, String>();
        paramMap.put("siteID", siteID);
        return findInAccountList(paramMap);
    }
    
    /**
     * <li>说明：得到【出段后时间】在默认设置时间内的已出段台账实体
     * <li>创建人：程锐
     * <li>创建日期：2015-2-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 入段实体对象
     * @return 在默认设置时间内的已出段台账实体
     */
    public TrainAccessAccount getUpdateOutedAccount(TrainAccessAccount entity) {
        TrainAccessAccount account = null;
        if (!StringUtil.isNullOrBlank(entity.getTrainGUID())) {
            account = getLatestOutedAccountByGUID(entity.getTrainGUID());
            if (account != null && isClearOutInfo(account.getOutTime().getTime(), new Date().getTime()))
                return account;
        }
        if (!StringUtil.isNullOrBlank(entity.getTrainAliasName())) {
            account = getLatestOutedAccountByAlias(entity.getTrainAliasName());
            if (account != null && isClearOutInfo(account.getOutTime().getTime(), new Date().getTime()))
                return account;                
        }
        if (!StringUtil.isNullOrBlank(entity.getTrainTypeShortName()) && !StringUtil.isNullOrBlank(entity.getTrainNo())) {
            account = getLatestOutedAccountByTrainName(entity.getTrainTypeShortName(), entity.getTrainNo());
            if (account != null && isClearOutInfo(account.getOutTime().getTime(), new Date().getTime()))
                return account;              
        }
        return null;
    }
    
    /**
     * <li>说明：根据机车GUID获取最近出段的机车出入段台账实体
     * <li>创建人：程锐
     * <li>创建日期：2015-2-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainGUID 机车GUID
     * @return 最近出段的机车出入段台账实体
     */
    @SuppressWarnings("unchecked")
    public TrainAccessAccount getLatestOutedAccountByGUID(String trainGUID) {
        if (StringUtil.isNullOrBlank(trainGUID))
            return null;
        Map paramMap = new HashMap<String, String>();
        paramMap.put("trainGUID", trainGUID);
        return getLatestOutedAccount(paramMap);
    }
    
    /**
     * <li>说明：根据机车别名获取最近出段的机车出入段台账实体
     * <li>创建人：程锐
     * <li>创建日期：2015-2-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainAliasName 机车别名
     * @return 最近出段的机车出入段台账实体
     */
    @SuppressWarnings("unchecked")
    public TrainAccessAccount getLatestOutedAccountByAlias(String trainAliasName) {
        if (StringUtil.isNullOrBlank(trainAliasName))
            return null;
        Map paramMap = new HashMap<String, String>();
        paramMap.put("trainAliasName", trainAliasName);
        return getLatestOutedAccount(paramMap);
    }
    
    /**
     * <li>说明：根据车型车号获取最近出段的机车出入段台账实体
     * <li>创建人：程锐
     * <li>创建日期：2015-1-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX 车型ID
     * @param trainNo 车号
     * @param trainAliasName 机车简称
     * @return 最近出段的机车出入段台账实体
     */
    @SuppressWarnings("unchecked")
    public TrainAccessAccount getLatestOutedAccountByTrainIDX(String trainTypeIDX, String trainNo, String trainAliasName) {
        if (StringUtil.isNullOrBlank(trainTypeIDX) || StringUtil.isNullOrBlank(trainNo)) {
        	if (!StringUtil.isNullOrBlank(trainAliasName)) {
        		Map paramMap = new HashMap<String, String>();
                paramMap.put("trainAliasName", trainAliasName);
                return getLatestOutedAccount(paramMap);
        	} else
        		return null;
        }
        Map paramMap = new HashMap<String, String>();
        paramMap.put("trainTypeIDX", trainTypeIDX);
        paramMap.put(TRAINNO, trainNo);
        return getLatestOutedAccount(paramMap);
    }
    
    /**
     * <li>说明：根据车型车号获取最近出段的机车出入段台账实体
     * <li>创建人：程锐
     * <li>创建日期：2015-2-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeShortName 机车名称
     * @param trainNo 车号
     * @return 最近出段的机车出入段台账实体
     */
    @SuppressWarnings("unchecked")
    public TrainAccessAccount getLatestOutedAccountByTrainName(String trainTypeShortName, String trainNo) {
        if (StringUtil.isNullOrBlank(trainTypeShortName) && StringUtil.isNullOrBlank(trainNo))
            return null;
        Map paramMap = new HashMap<String, String>();
        paramMap.put(TRAINTYPESHORTNAME, trainTypeShortName);
        paramMap.put(TRAINNO, trainNo);
        return getLatestOutedAccount(paramMap);
    }
    
    /**
     * <li>说明：根据系统配置的机车出段超时时间，判断是否在此时间内再入段
     * <li>创建人：程锐
     * <li>创建日期：2015-1-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param outTime 出段时间
     * @param inTime 入段时间
     * @return true 在此时间内再入段， false 不在此时间内再入段
     */
    public boolean isClearOutInfo(long outTime, long inTime) {
        String trainAccessTimeOut = StringUtil.nvlTrim(SystemConfigUtil.getValue("ck.jczb.trainAccessAccount.trainAccessTimeOut"), "0");        
        long timeOut = Integer.parseInt(trainAccessTimeOut) * 60 * 1000;// 系统配置的机车出段超时时长（分钟）
        return (outTime + timeOut) > inTime;
    }
    
    /**
     * <li>说明：根据台位图服务端传递的机车信息JSON字符串获取机车出入段实体
     * <li>创建人：程锐
     * <li>创建日期：2015-2-9
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainInfo 机车信息JSON字符串
     * @return 机车出入段实体
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public TrainAccessAccount getAccountByTrainInfo(String trainInfo) throws JsonParseException, JsonMappingException, IOException {
        TrainAccessAccount entity = JSONUtil.read(trainInfo, TrainAccessAccount.class);
        if (entity == null)
            return null;
        TrainAccessAccount account = findInAccountByTrainGUID(entity.getTrainGUID());
        if (account != null)
            return account;
        account = findInAccountByTrainAliasName(entity.getTrainAliasName());
        if (account != null)
            return account;
        Map<String, String> map = analysisTrainInfo(entity.getTrainAliasName());
        if (map != null) {
            account = findInAccountByTrainName(map.get(TRAINTYPESHORTNAME), map.get(TRAINNO));
            if (account != null)
                return account;
        }
            
        account = findInAccountByTrainName(entity.getTrainTypeShortName(), entity.getTrainNo());
        if (account != null)
            return account;
        TrainType trainType = twtUtil.getTrainTypeByTrainInfo(entity.getTrainAliasName());
        if (trainType == null)
            return null;
        return findInAccountByTrainName(trainType.getShortName(), trainType.getTrainNo());
    }

    /**
     * <li>说明：台位图传过来的机车别名有可能是完整的车型+车号，需要解析成map
     * <li>创建人：程锐
     * <li>创建日期：2015-4-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainAiasName 机车别名
     * @return map
     */
    public Map<String, String> analysisTrainInfo(String trainAiasName) {
        if (StringUtil.isNullOrBlank(trainAiasName)) 
            return null;
        if (trainAiasName.length() < 5)
            return null;
        String trainNo = trainAiasName.substring(trainAiasName.length() - 4);
        String trainTypeShortName = trainAiasName.substring(0, trainAiasName.length() - 4);
        Map<String, String> map = new HashMap<String, String>();
        map.put(TRAINTYPESHORTNAME, trainTypeShortName);
        map.put(TRAINNO, trainNo);
        return map;
    }
    
    /**
     * <li>说明：获取最近出段的机车出入段台账实体
     * <li>创建人：程锐
     * <li>创建日期：2015-1-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 最近出段的机车出入段台账实体
     */    
    @SuppressWarnings("unchecked")
    private TrainAccessAccount getLatestOutedAccount(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from TrainAccessAccount where 1 = 1 ")
           .append(CommonUtil.buildParamsHql(paramMap))   
           .append(" and recordStatus = 0 and outTime != null order by outTime desc");
        List<TrainAccessAccount> list = daoUtils.find(hql.toString());
        if (list == null || list.size() < 1)
            return null;
        return list.get(0);
    } 
    
    /**
     * <li>说明：查询已入段的机车出入段台账实体
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 已入段的机车出入段台账实体
     */
    private TrainAccessAccount findInAccount(Map paramMap) {
        return (TrainAccessAccount) daoUtils.findSingle(getInAccountHql(paramMap));
    }
    
    /**
     * <li>说明：查询已入段的机车出入段台账实体列表
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 已入段的机车出入段台账实体列表
     */
    @SuppressWarnings("unchecked")
    private List<TrainAccessAccount> findInAccountList(Map paramMap) {
        return daoUtils.find(getInAccountHql(paramMap));
    }
    
    /**
     * <li>说明：获取查询已入段的机车出入段台账hql字符串
     * <li>创建人：程锐
     * <li>创建日期：2015-3-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 已入段的机车出入段台账hql字符串
     */
    private String getInAccountHql(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from TrainAccessAccount where 1 = 1 ")
           .append(CommonUtil.buildParamsHql(paramMap))
           .append(" and outTime = null and recordStatus = 0 order by updateTime desc");
        return hql.toString();
    }
    
    /**
     * <li>说明：获取在段机车车型车号信息
     * <li>创建人：张迪
     * <li>创建日期：2016-9-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return 车型车号信息
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     */
    public Page getInTrainAccessAccount(SearchEntity<TrainAccessAccount> searchEntity) throws SecurityException, NoSuchFieldException {
        StringBuffer sb =new StringBuffer();
        String siteID = EntityUtil.findSysSiteId(""); // 肯尼亚先去掉站点
        sb.append("From  (select * from ( SELECT T.*,ROW_NUMBER() OVER (PARTITION BY  T.TRAIN_TYPE_IDX, T.TRAIN_NO ORDER BY T.UPDATE_TIME desc)RV fROM TWT_TRAIN_ACCESS_ACCOUNT T where T.Record_Status = 0 ")
           .append(") where RV=1 and out_time is null) a  where 1=1 ");        

        TrainAccessAccount bean = searchEntity.getEntity();      
        // 查询条件 - 车型id
        if (!StringUtil.isNullOrBlank(bean.getTrainTypeIDX())) {
            sb.append(" and Train_Type_IDX = '").append(bean.getTrainTypeIDX()).append("'"); 
        }
        // 查询条件 - 车型简称
        if (!StringUtil.isNullOrBlank(bean.getTrainTypeShortName())) {
            sb.append(" and Train_Type_ShortName || Train_No like '%").append(bean.getTrainTypeShortName()).append("%'"); 
        }
        
        // 客货类型
        if (!StringUtil.isNullOrBlank(bean.getVehicleType())) {
            sb.append(" and T_VEHICLE_TYPE = '").append(bean.getVehicleType()).append("'"); 
        }        
        
        
//      拼接排序参数
        Order[] orders = searchEntity.getOrders();
        if (null != orders && orders.length > 0) {
            String[] order = orders[0].toString().split(" ");
            String sort = order[0];
            String dir = order[1];
            Class clazz = PartsRdpBean.class;
            Field field = clazz.getDeclaredField(sort);
            Column annotation = field.getAnnotation(Column.class);
            if (null != annotation) {
                sb.append(" ORDER BY a.").append(annotation.name()).append(" ").append(dir);
            } else {
                sb.append(" ORDER BY a.").append(sort).append(" ").append(dir);
            }
        }
        
        StringBuffer totalSql = new StringBuffer(" SELECT COUNT(*) AS ROWCOUNT ").append(sb.toString());
        StringBuffer sql = new StringBuffer(" select a.* ").append(sb.toString());
        
        return super.queryPageList(totalSql.toString(), sql.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, TrainAccessAccount.class);
    }
    /**
     * <li>说明：获取在段机车车型车号，提票未派工数量信息
     * <li>创建人：张迪
     * <li>创建日期：2016-9-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return 车型车号信息
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     */
    public Page getInTrainForNotDispatch(SearchEntity<InTrainDispatchCountBean> searchEntity) throws SecurityException, NoSuchFieldException {
        StringBuffer sb =new StringBuffer();
        String siteID = EntityUtil.findSysSiteId("");
        sb.append(" From  (select * from ( SELECT T.*,ROW_NUMBER() OVER (PARTITION BY  T.TRAIN_TYPE_IDX, T.TRAIN_NO ORDER BY T.UPDATE_TIME desc)RV " +
                "fROM TWT_TRAIN_ACCESS_ACCOUNT T where T.Record_Status = 0 and T.SiteID ='")
        .append(siteID).append("')  where RV=1 and out_time is null) a   " +
                "inner join (select TRAIN_TYPE_IDX, TRAIN_NO, count(TRAIN_TYPE_IDX) as not_Dispatch_Count from JXGC_Fault_Ticket" +
                " where RECORD_Status = 0 and REPAIR_TEAM IS NULL  and status in (").append(FaultTicket.STATUS_DRAFT).append(",").append(FaultTicket.STATUS_OPEN)
        .append(") group by TRAIN_TYPE_IDX, TRAIN_NO) m " +
                "on a.TRAIN_TYPE_IDX = m.TRAIN_TYPE_IDX     and a.TRAIN_NO = m.TRAIN_NO where 1=1 ");        
        
        InTrainDispatchCountBean bean = searchEntity.getEntity();      
        // 查询条件 - 车型id
        if (!StringUtil.isNullOrBlank(bean.getTrainTypeIDX())) {
            sb.append(" and a.Train_Type_IDX = '").append(bean.getTrainTypeIDX()).append("'"); 
        }
        // 查询条件 - 车型简称车号
        if (!StringUtil.isNullOrBlank(bean.getTrainTypeShortName())) {
            sb.append(" and a.Train_Type_ShortName || a.Train_No like '%").append(bean.getTrainTypeShortName()).append("%'"); 
        }
//      拼接排序参数
        Order[] orders = searchEntity.getOrders();
        if (null != orders && orders.length > 0) {
            String[] order = orders[0].toString().split(" ");
            String sort = order[0];
            String dir = order[1];
            Class clazz = PartsRdpBean.class;
            Field field = clazz.getDeclaredField(sort);
            Column annotation = field.getAnnotation(Column.class);
            if (null != annotation) {
                sb.append(" ORDER BY a.").append(annotation.name()).append(" ").append(dir);
            } else {
                sb.append(" ORDER BY a.").append(sort).append(" ").append(dir);
            }
        }
        
        StringBuffer totalSql = new StringBuffer(" SELECT COUNT(*) AS ROWCOUNT ").append(sb.toString());
        StringBuffer sql = new StringBuffer(" SELECT a.idx,a.TRAIN_TYPE_IDX, a.train_type_shortname, a.Train_No,  m.not_Dispatch_Count  ").append(sb.toString());
        
        return super.queryPageList(totalSql.toString(), sql.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, InTrainDispatchCountBean.class);
    }
}
