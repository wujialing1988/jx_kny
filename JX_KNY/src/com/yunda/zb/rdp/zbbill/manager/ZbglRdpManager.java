package com.yunda.zb.rdp.zbbill.manager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.Column;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.freight.zb.plan.entity.ZbglRdpPlanRecord;
import com.yunda.jxpz.utils.SystemConfigUtil;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountManager;
import com.yunda.util.BeanUtils;
import com.yunda.zb.common.ZbConstants;
import com.yunda.zb.pczz.manager.ZbglPczzWIManager;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpDTO;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpTempRepair;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWi;
import com.yunda.zb.rdp.zbtaskbill.manager.ZbglRdpWiManager;
import com.yunda.zb.tecorder.entity.ZbglTecOrder;
import com.yunda.zb.tecorder.manager.ZbglTecOrderManager;
import com.yunda.zb.tp.entity.ZbglTp;
import com.yunda.zb.tp.manager.ZbglTpManager;
import com.yunda.zb.trainclean.entity.ZbglCleaning;
import com.yunda.zb.trainclean.manager.ZbglCleaningManager;
import com.yunda.zb.trainonsand.entity.ZbglSanding;
import com.yunda.zb.trainonsand.manager.ZbglSandingManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglRdp业务类,机车整备单
 * <li>创建人：程锐
 * <li>创建日期：2015-01-21
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "zbglRdpManager")
public class ZbglRdpManager extends JXBaseManager<ZbglRdp, ZbglRdp> {
    
    /** 临碎修提票业务对象 */
    @Resource
    ZbglTpManager zbglTpManager;
    
    /** TrainAccessAccount业务类,机车出入段台账 */
    @Resource
    private TrainAccessAccountManager trainAccessAccountManager;
    
    /** 转临修业务对象 */
    @Resource
    ZbglRdpTempRepairManager zbglRdpTempRepairManager;
    
    /** ZbglTecOrder业务类,机车技术指令措施 */
    @Resource
    private ZbglTecOrderManager zbglTecOrderManager;
    
    /** ZbglPczzWI业务类,普查整治任务单 */
    @Resource
    private ZbglPczzWIManager zbglPczzWIManager;
    
    /** ZbglRdpWi业务类,机车整备任务单 */
    @Resource
    private ZbglRdpWiManager zbglRdpWiManager;
    
    /** ZbglSanding业务类,机车上砂记录 */
    @Resource
    private ZbglSandingManager zbglSandingManager;
    
    /** ZbglCleaning业务类,机车保洁记录 */
    @Resource
    private ZbglCleaningManager zbglCleaningManager;
    
    public static final String TRAINNO = "trainNo";
    
    public static final String TRAINTYPEIDX = "trainTypeIDX"; 
    
    /**
     * <li>说明：获取此车的运行中的整备单,因同一车可能有多条运行中的整备单，故取兑现开始时间最近的一条
     * <li>创建人：程锐
     * <li>创建日期：2015-1-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeShortName 车型简称
     * @param trainNo 车号
     * @return 机车整备单实体对象
     */
    @SuppressWarnings("unchecked")
    public ZbglRdp getRunningRdpByTrain(String trainTypeShortName, String trainNo) {
        if (StringUtil.isNullOrBlank(trainTypeShortName) && StringUtil.isNullOrBlank(trainNo))
            return null;
        Map paramMap = new HashMap<String, String>();
        paramMap.put("trainTypeShortName", trainTypeShortName);
        paramMap.put(TRAINNO, trainNo);
//        paramMap.put("rdpStatus", ZbglRdp.STATUS_HANDLING);
        StringBuilder hql = new StringBuilder();
        hql.append("from ZbglRdp where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap)).append(" and recordStatus = 0 order by rdpStartTime desc");
        List<ZbglRdp> list = daoUtils.find(hql.toString());
        if (list == null || list.size() < 1)
        	return null;
        return list.get(0);
    }

    /**
     * <li>说明：转临修处理
     * <li>创建人：程锐
     * <li>创建日期：2015-1-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @param tpIDXAry 提票实体对象IDX数组
     * @param zlxData 转临修实体对象
     * @return 验证错误信息
     * @throws Exception
     */
    public String updateForZLX(String rdpIDX, String[] tpIDXAry, ZbglRdpTempRepair zlxData) throws Exception {
        if (StringUtil.isNullOrBlank(rdpIDX))
            throw new BusinessException("机车整备单IDX为空！");
        ZbglRdp rdp = getModelById(rdpIDX);
        if (rdp == null)
            throw new BusinessException("机车整备单为空！");        
        if (!isCanZLX(rdpIDX))
            return "尚有待销活的碎修提票活要处理，请处理后再刷新页面做转临修处理。";
        rdp.setRepairClass(ZbConstants.REPAIRCLASS_LX);
        saveOrUpdate(rdp);
        zbglTpManager.updateForZLX(tpIDXAry, zlxData);
        zlxData.setRdpIDX(rdpIDX);
        zbglRdpTempRepairManager.saveOrUpdate(zlxData);
        return null;
    }
    /**
     * 
     * <li>说明：查询没有交接单的整备单列表信息
     * <li>创建人：程梅
     * <li>创建日期：2015-2-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件 "trainTypeIDX":"242","trainNo":"阿斯蒂芬"
     * @param start 开始行
     * @param limit 每页记录数
     * @return 整备单列表信息
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Page queryRdpListForHO(String searchJson, int start, int limit,String sort,String dir) throws Exception {
        
        JSONObject ob = JSONObject.parseObject(searchJson);
        
        //获取数据
        String trainTypeIDX = ob.getString("trainTypeIDX");//车型
        String trainNo = ob.getString("trainNo");//车号
        String trainToGo = ob.getString("trainToGo");//入段去向
        
        // PDA模糊查询
        String trainNoAndType = ob.getString("trainNoAndType");//车型车号
        
        //过滤已经有机车交接单的记录
        StringBuffer sb = new StringBuffer();
        
        sb.append(" select * from (select zzr.*,ttaa.in_time,ttaa.to_go as train_to_go ");
        sb.append(" from zb_zbgl_rdp zzr, twt_train_access_account ttaa ");
        sb.append(" where zzr.train_access_account_idx = ttaa.idx ");
        sb.append(" and zzr.record_status = 0 ");
        sb.append(" and ttaa.record_status = 0 ");
        sb.append(" and zzr.idx not in (select rdp_idx from ZB_ZBGL_HO_Case where Record_Status=0) ");
        //查询本站点的信息
        sb.append(" and zzr.siteID = '").append(EntityUtil.findSysSiteId("")).append("'");
        //车号条件
        if (StringUtils.isNotBlank(trainTypeIDX)) {
            sb.append(" and zzr.train_type_idx = '").append(trainTypeIDX).append("'");
        }
        //车型条件
        if (StringUtils.isNotBlank(trainNo)) {
            sb.append(" and zzr.train_no like '%").append(trainNo).append("%'");
        }
        //入段去向条件
        if (StringUtils.isNotBlank(trainToGo)) {
            sb.append(" and ttaa.to_go like '%").append(trainToGo).append("%'");
        }
        
        // PDA查询条件
        if (StringUtils.isNotBlank(trainNoAndType)){
            sb.append(" and ( zzr.train_no like '%").append(trainNoAndType).append("%'");
            sb.append(" or zzr.train_type_shortname like '%").append(trainNoAndType).append("%' )");
        }
        
        //排序
        if (StringUtils.isNotBlank(sort)) {
            if ("inTime".equals(sort)) {
                sb.append(" order by ttaa.in_time ").append(dir);
            }else {
                Class clazz = ZbglRdpDTO.class;
                //通过传递过来需要排序的字段反射字段对象
                Field field = clazz.getDeclaredField(sort);
                //获取字段上，标签上的列名
                Column annotation = field.getAnnotation(Column.class);
                sb.append(" order by zzr.").append(annotation.name()).append(" ").append(dir);
            }
        }else {
            sb.append(" order by zzr.update_time desc ");
        }
        sb.append(") t");
        
//      此处的总数别名必须是ROWCOUNT，封装方法有规定
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("from"));
        return this.queryPageList(totalSql, sb.toString(), start, limit, false, ZbglRdpDTO.class);
        
//        QueryCriteria<ZbglRdp> query = new QueryCriteria<ZbglRdp>();
//        query.setEntityClass(ZbglRdp.class);
//        List<Condition> whereList = new ArrayList<Condition>();
//        Map<String, String> queryMap = new HashMap<String, String>();
//        queryMap = JSONUtil.read(searchJson, Map.class);
//        //车型id
//        if (queryMap.containsKey(TRAINTYPEIDX)) whereList.add(new Condition(TRAINTYPEIDX, Condition.EQ, String.valueOf(queryMap.get(TRAINTYPEIDX))));
//        //车号
//        if (queryMap.containsKey(TRAINNO)) whereList.add(new Condition(TRAINNO, Condition.LIKE, String.valueOf(queryMap.get(TRAINNO))));
//        //过滤已经有机车交接单的记录
//        StringBuffer sql = new StringBuffer(" idx not in (select rdp_idx from ZB_ZBGL_HO_Case where Record_Status=0)");
//        //查询本站点的信息
//        sql.append(" and siteID = '")
//              .append(EntityUtil.findSysSiteId(""))
//              .append("'");
//        whereList.add(new Condition(sql.toString()));
//        query.setWhereList(whereList);
//        query.setStart(start);
//        query.setLimit(limit);
//        
//        
//        
//        return findPageList(query);
        
    }
    /**
     * 
     * <li>说明：查询没有保洁记录的整备单列表信息
     * <li>创建人：程梅
     * <li>创建日期：2015-3-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件
     * @param start 开始行
     * @param limit 每页记录数
     * @return 整备单列表信息
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Page queryRdpListForClean(String searchJson, int start, int limit,String sort,String dir) throws Exception {
        
        JSONObject ob = JSONObject.parseObject(searchJson);
        
        //获取数据
        String trainTypeIDX = ob.getString("trainTypeIDX");//车型
        String trainNo = ob.getString("trainNo");//车号
        String trainToGo = ob.getString("trainToGo");//入段去向
        String startDate = ob.getString("startDate");//入段开始时间
        String endDate = ob.getString("endDate");//入段结束时间
        
        //过滤已经有机车交接单的记录
        StringBuffer sb = new StringBuffer();
        
        sb.append(" select * from (select zzr.*,ttaa.in_time,ttaa.to_go as train_to_go ");
        sb.append(" from zb_zbgl_rdp zzr, twt_train_access_account ttaa ");
        sb.append(" where zzr.train_access_account_idx = ttaa.idx ");
        sb.append(" and zzr.record_status = 0 ");
        sb.append(" and zzr.rdp_status <> 'COMPLETE' ");
        sb.append(" and ttaa.record_status = 0 ");
        sb.append(" and zzr.idx not in (select rdp_idx from ZB_ZBGL_Cleaning ) ");
        //查询本站点的信息
        sb.append(" and zzr.siteID = '").append(EntityUtil.findSysSiteId("")).append("'");
        //车号条件
        if (StringUtils.isNotBlank(trainTypeIDX)) {
            sb.append(" and zzr.train_type_idx = '").append(trainTypeIDX).append("'");
        }
        //车型条件
        if (StringUtils.isNotBlank(trainNo)) {
            sb.append(" and zzr.train_no like '%").append(trainNo).append("%'");
        }
        //入段去向条件
        if (StringUtils.isNotBlank(trainToGo)) {
            sb.append(" and ttaa.to_go like '%").append(trainToGo).append("%'");
        }
//      开始时间
        if (!StringUtil.isNullOrBlank(startDate)) {
            sb.append(" and  to_char(ttaa.in_time,'yyyy-mm-dd hh24:mi:ss')>='").append(startDate).append("'");
        }
//      结束时间
        if (!StringUtil.isNullOrBlank(endDate)) {
            sb.append(" and to_char(ttaa.in_time,'yyyy-mm-dd hh24:mi:ss') <= '").append(endDate).append("'");
        }
        
        //排序
        if (StringUtils.isNotBlank(sort)) {
            if ("inTime".equals(sort)) {
                sb.append(" order by ttaa.in_time ").append(dir);
            }else {
                Class clazz = ZbglRdpDTO.class;
                //通过传递过来需要排序的字段反射字段对象
                Field field = clazz.getDeclaredField(sort);
                //获取字段上，标签上的列名
                Column annotation = field.getAnnotation(Column.class);
                sb.append(" order by zzr.").append(annotation.name()).append(" ").append(dir);
            }
        }else {
            sb.append(" order by zzr.idx ");
        }
        sb.append(") t");
        
//      此处的总数别名必须是ROWCOUNT，封装方法有规定
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("from"));
        return this.queryPageList(totalSql, sb.toString(), start, limit, false, ZbglRdpDTO.class);
        
//        QueryCriteria<ZbglRdp> query = new QueryCriteria<ZbglRdp>();
//        query.setEntityClass(ZbglRdp.class);
//        List<Condition> whereList = new ArrayList<Condition>();
//        Map<String, String> queryMap = new HashMap<String, String>();
//        searchJson = StringUtil.nvlTrim( searchJson, "{}" );
//        queryMap = JSONUtil.read(searchJson, Map.class);
//        //车型id
//        if (queryMap.containsKey(TRAINTYPEIDX)) whereList.add(new Condition(TRAINTYPEIDX, Condition.EQ, String.valueOf(queryMap.get(TRAINTYPEIDX))));
//        //车号
//        if (queryMap.containsKey(TRAINNO)) whereList.add(new Condition(TRAINNO, Condition.LIKE, String.valueOf(queryMap.get(TRAINNO))));
//        //过滤已经有机车保洁信息的记录
//        StringBuffer sql = new StringBuffer(" idx not in (select rdp_idx from ZB_ZBGL_Cleaning )");
//        //查询本站点的信息
//        sql.append(" and siteID = '")
//              .append(EntityUtil.findSysSiteId(""))
//              .append("'");
//        whereList.add(new Condition(sql.toString()));
//        query.setWhereList(whereList);
//        query.setStart(start);
//        query.setLimit(limit);
//        return findPageList(query);
    }
    
    /**
     * <li>说明：获取机车出入段台账对应的机车整备单对象
     * <li>创建人：程锐
     * <li>创建日期：2016-4-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param accountIDX 机车出入段台账IDX
     * @return 机车整备单对象
     */
    @SuppressWarnings("unchecked")
    public ZbglRdp getRdpByAccount(String accountIDX) {
        if (StringUtil.isNullOrBlank(accountIDX))
            return null;
        Map paramMap = new HashMap<String, String>();
        paramMap.put("trainAccessAccountIDX", accountIDX);
        return getRdp(paramMap);
    } 
    
    /**
     * <li>说明：获取机车出入段台账对应的机车整备单对象
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param accountIDX 机车出入段台账IDX
     * @return 机车整备单对象
     */
    @SuppressWarnings("unchecked")
    public ZbglRdp getRunningRdpByAccount(String accountIDX) {
        if (StringUtil.isNullOrBlank(accountIDX))
            return null;
        Map paramMap = new HashMap<String, String>();
        paramMap.put("trainAccessAccountIDX", accountIDX);
        paramMap.put("rdpStatus", ZbglRdp.STATUS_HANDLING);
        return getRdp(paramMap);
    } 
    
    /**
     * <li>说明：根据机车入段台账生成机车整备单
     * <li>创建人：程锐
     * <li>创建日期：2016-4-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param account 机车入段台账
     * @return 机车整备单实体
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public ZbglRdp saveByTrainAccessAccount(TrainAccessAccount account) throws IllegalAccessException, InvocationTargetException, BusinessException, NoSuchFieldException {
        ZbglRdp rdp = new ZbglRdp();
        BeanUtils.copyProperties(rdp, account);
        rdp.setIdx("");
        rdp.setRdpStartTime(new Date());
        rdp.setToGo("");
        rdp.setTrainAccessAccountIDX(account.getIdx());
        rdp.setRdpStatus(ZbglRdp.STATUS_HANDLING);
        rdp.setRepairClass(ZbConstants.REPAIRCLASS_SX);
        saveOrUpdate(rdp);
        return rdp;
    }
    
    /**
     * <li>说明：生成列检计划并返回
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param record 车辆计划实体
     * @return 机车整备单实体
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public ZbglRdp saveByZbglRdpPlanRecord(ZbglRdpPlanRecord record) throws IllegalAccessException, InvocationTargetException, BusinessException, NoSuchFieldException {
        ZbglRdp rdp = new ZbglRdp();
        rdp.setIdx("");
        rdp.setTrainTypeIDX(record.getTrainTypeIdx());
        rdp.setTrainTypeShortName(record.getTrainTypeCode());
        rdp.setTrainNo(record.getTrainNo());
        rdp.setRdpStartTime(new Date());
        rdp.setRdpStatus(ZbglRdp.STATUS_HANDLING);
        saveOrUpdate(rdp);
        return rdp;
    }
    
    /**
     * <li>说明：获取机车整备单对象
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 机车整备单对象
     */
    private ZbglRdp getRdp(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from ZbglRdp where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap)).append(" and recordStatus = 0");
        return (ZbglRdp) daoUtils.findSingle(hql.toString());
    }
    
    /**
     * <li>说明：能否转临修。如果此整备单还有待销活的碎修票需要处理，则不能转临修
     * <li>创建人：程锐
     * <li>创建日期：2015-3-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return true 能转临修 false 不能
     * @throws Exception
     */
    private boolean isCanZLX(String rdpIDX) throws Exception {
        Map<String, String> queryMap = new HashMap<String, String>();  
        queryMap.put("rdpIDX", rdpIDX);
        Page<ZbglTp> page = zbglTpManager.queryTpList(queryMap, ZbConstants.REPAIRCLASS_SX, ZbglTp.STATUS_OPEN, null, 0, 10);
        return page.getTotal() == 0;
    }

    /**
     * <li>说明：判断该整备单车型车号下是否在规定整备单次数中做过范围活，如果没做过，那么范围活必须做
     * <li>创建人：林欢
     * <li>创建日期：2016-5-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数 trainNo,trainTypeShortName,checkZbglRdpNum
     * @return 操作成功与否 {success:ture,isDo:ture}
     */
    public List<ZbglRdp> checkIsDoZbglRdpByNum(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append(" select * ");
        hql.append(" from (select a.* ");
        hql.append(" from zb_zbgl_rdp a ");
        hql.append(" where a.record_status = 0 ");
        hql.append(" and a.train_no = '").append(paramMap.get("trainNo")).append("'");//车号
        hql.append(" and a.train_type_shortname = '").append(paramMap.get("trainTypeShortName")).append("'");//车型
        hql.append(" order by rdp_start_time desc) t ");
        hql.append(" where rownum < ").append(Integer.valueOf(paramMap.get("checkZbglRdpNum").toString()) + 1).append("");//规定前多少次整备单
        hql.append(" order by rownum   ");
        
//      此处的总数别名必须是ROWCOUNT，封装方法有规定
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + hql.substring(hql.indexOf("from"));
        Page<ZbglRdp> page = this.queryPageList(totalSql, hql.toString(), 0, 9999999, false, ZbglRdp.class);
        
        return page.getList();
    }
    
    /**
     * 
     * <li>说明：返回是否能做范围活标志（是否有jt6在处理中，是否在前N次任务单做过范围活）
     * <li>创建人：林欢
     * <li>创建日期：2016-5-30
     * <li>修改人：林欢
     * <li>修改日期：2016-8-15
     * <li>修改内容：由于需求变动，产品化不使用该限制，只有郑州方面在使用,同步涉及修改，移除产品化isStartUsingNum，checkZbglRdpNum，isStartUsingJt6 3个配置项的配置
     * 业务逻辑说明：
     * jt6：如果该车型车号下，有处理中的提票，那么机车交接的时候，必须选择范围活
     * 前N次：假设系统设置，前3次任务单查询。那么查询前3次（不包含本次，本次算第4次），该车型车号，是否做过范围活，如果没有做过，那么机车交接的时候，必须选择做范围活
     * @param paramMap 查询参数 trainNo,trainTypeShortName,checkZbglRdpNum
     * @return 操作成功与否 {success:ture,isDo:ture}
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws InstantiationException 
     * @throws IllegalAccessException 
     */
    /*
    public Page<ZbglRdpDTO> zbglRdpFlagSet(Page<ZbglRdpDTO> page) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        
        //获取list对象
        List<ZbglRdpDTO> returnList = page.getList();
        
        //循环编列
        for (ZbglRdpDTO dto : returnList) {
            dto.setIsDoJt6Flag(false);
            dto.setIsDoNumFlag(true);
//          车号
            String trainNo = dto.getTrainNo();
            // 车型
            String trainTypeShortName = dto.getTrainTypeShortName();                    
            Map paramMap = new HashMap<String, String>();
            paramMap.put("trainNo", trainNo);//车号
            paramMap.put("trainTypeShortName", trainTypeShortName);//车型
            
            //========================判断jt6开始=====================================
//          配置项名称,ck.jczb.isStartUsing
//          当启用的时候，在机车交接时会判断，如果该整备单下有jt6提票，并且是处理中的，那么必须选择，做范围活，如果不启用，该项判断失效
            String isStartUsingJt6 = SystemConfigUtil.getValue("ck.jczb.isStartUsingJt6");
            
            //当为1的时候，表示启用
            if (StringUtils.isNotBlank(isStartUsingJt6) && "1".equals(isStartUsingJt6)) {
                
                String[] faultNoticeStatus = new String[]{ZbglTp.STATUS_DRAFT,ZbglTp.STATUS_OPEN};
                List<ZbglTp> zbList = zbglTpManager.getDoingTpList(paramMap, faultNoticeStatus);
                //说明有jt6提票的信息和整备单关联
                if (zbList != null && zbList.size() > 0) {
                    //判断该整备单车型车号下是否有jt6提票状态是处理中的，如果是，那么范围活必须做
                    dto.setIsDoJt6Flag(true);
                }
            }
            //========================判断jt6结束=====================================
            //========================判断前N次开始=====================================
//          配置项名称,ck.jczb.isStartUsingNum 1=启用
//          当启用的时候，在机车交接时会判断，如果该车型车号下，前N次整备单（不包含本次）中没有做过范围活，那么必须选择，做范围活，如果不启用，该项判断失效
            String isStartUsingNum = SystemConfigUtil.getValue("ck.jczb.isStartUsingNum");
            
            if (StringUtils.isNotBlank(isStartUsingNum) && "1".equals(isStartUsingNum)) {
//              配置项名称,ck.jczb.checkZbglRdpNum 
//              配置前几次判断是否做范围活
                String checkZbglRdpNum = SystemConfigUtil.getValue("ck.jczb.checkZbglRdpNum");
                
                //当为1的时候，表示启用
                if (StringUtils.isNotBlank(checkZbglRdpNum)) {
                    
                    paramMap.put("checkZbglRdpNum", checkZbglRdpNum);//整备单前几次
                    
                    //查询该车型车号N次前的整备单
                    List<ZbglRdp> zbglRdpList = checkIsDoZbglRdpByNum(paramMap);
                    
//                  判断不包含本次总次数是否和配置一致
                    //如果一致，说明进入判断区域，否则不需要判断该车型车号在前N次,是否做过范围活
                    if (zbglRdpList.size() < Integer.valueOf(checkZbglRdpNum)) {
//                      未到检测次数
                        dto.setIsDoNumFlag(false);
                    }else {
                        //i=1开始(i=0为本次，大于0为前N次对象)
                        for (int i = 1; i < zbglRdpList.size() && i <= Integer.valueOf(checkZbglRdpNum); i++) {
//                          做过，本次可以不做
                            if (StringUtils.isNotBlank(zbglRdpList.get(i).getZbfwIDX())) {
                                dto.setIsDoNumFlag(false);
                                break;
                            }
                        }
                    }
                }
            }else {
                //未启用
                dto.setIsDoNumFlag(false);
            }
            
            //========================判断前N次结束=====================================
            
        }
        
        return page;
    }
    */

    /**
     * <li>说明：根据系统获取到的入段去向比较前一次和档次的入段去向，判断是否删除整备单等信息或者生成整备单等信息
     * <li>创建人：林欢
     * <li>创建日期：2016-8-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param inOrLatestOutAccount 前台传递过来的出入段台账对象
     * @param flag 是否需要重置rdp等相关信息
     * @throws Exception 
     */
    public void insertRdpOrInitRdpInfo(TrainAccessAccount inOrLatestOutAccount,Boolean flag) throws Exception {
        
//      根据出入段台账信息获取数据库中的入段去向
        String dbToGo = trainAccessAccountManager.findToGoByTrainAccessAccointIDX(inOrLatestOutAccount.getIdx());   
        if (dbToGo == null) {
            dbToGo = " ";
        }
        if (inOrLatestOutAccount.getToGo() == null) {
            inOrLatestOutAccount.setToGo(" ");
        }
        //如果数据库的入段去向和传递过来的不一致，查询该入段去向是否需要生成整备单
        
        //1.轨道自动化进来，入段去向为null，修改操作，传递过来入段去向不为null
        //2.正常修改，入段去向为null，修改操作，传递过来的入段去向不为null
        //3.null,null  null,有 有,null 有，有
        
        //null null情况
        if (StringUtil.isNullOrBlank(dbToGo) && StringUtil.isNullOrBlank(inOrLatestOutAccount.getToGo())) {
            return;
        }
        
        if ((StringUtils.isNotBlank(dbToGo) && !dbToGo.equals(inOrLatestOutAccount.getToGo())) || (StringUtils.isNotBlank(inOrLatestOutAccount.getToGo()) && !inOrLatestOutAccount.getToGo().equals(dbToGo))) {
            String sysToGo = SystemConfigUtil.getValue("ck.jczb.toGoForZbRdp");
            
            //当确定数据库的入段去向和传递过来的入段去向不一致的情况下，继续判断，这2个去向，是否都是需要生成整备单的，如果是，那么不需要做任何处理
            if (sysToGo.contains(dbToGo) && sysToGo.contains(inOrLatestOutAccount.getToGo())) {
                return;
            }
            
//          当确定数据库的入段去向和传递过来的入段去向不一致的情况下，继续判断，这2个去向，是否都是需要生成整备单的，如果都不是，那么不需要做任何处理
            if (!sysToGo.contains(dbToGo) && !sysToGo.contains(inOrLatestOutAccount.getToGo())) {
                return;
            }
            
//            如果不包含传递过来的，包含数据库的，则逻辑删除作业工单（整备任务单）、整备单、普查整治、技术措施应该不能查询；提票状态更改为初始化，提票已处理时所填写数据均清空；报活预警状态更改为初始化。
            //TODO
            if (StringUtils.isNotBlank(sysToGo) && !sysToGo.contains(inOrLatestOutAccount.getToGo()) && sysToGo.contains(dbToGo) && flag) {
                //通过出入段台账信息获取到整备单对象
                ZbglRdp zbglRdp = this.getRdpByAccount(inOrLatestOutAccount.getIdx());
                String zbglRdpIDX = zbglRdp.getIdx();
                
                //初始化提票状态，并且清除其填写数据
                //如果有临修票，修改状态，清空处理数据
                List<ZbglRdpTempRepair> zbglRdpTempRepairList = zbglRdpTempRepairManager.findZbglRdpTempRepairByZbglRdpIDX(zbglRdpIDX);
                if (zbglRdpTempRepairList != null && zbglRdpTempRepairList.size() > 0) {
                    for (ZbglRdpTempRepair repair : zbglRdpTempRepairList) {
//                        repair.setHandleOrgID(null);//处理班组
//                        repair.setHandleOrgName(null);//处理班组名称
//                        repair.setHandleOrgSeq(null);//处理序列
//                        repair.setHandlePersonID(null);//处理人
//                        repair.setHandlePersonName(null);//处理人姓名
//                        repair.setHandleReason(null);//处理原因
//                        repair.setRdpIDX(null);//整备单idx
//                        zbglRdpTempRepairManager.saveOrUpdate(repair);
                        zbglRdpTempRepairManager.getDaoUtils().remove(repair);
                    }
                }
                
                //如果有碎修票，修改状态，清空处理数据
                List<ZbglTp> zbglTp = zbglTpManager.getAllTpListByRdp(zbglRdpIDX, null);
                if (zbglTp != null && zbglTp.size() > 0) {
                    for (ZbglTp tp : zbglTp) {
                        tp.setRevOrgID(null);//处理班组
                        tp.setRevOrgName(null);//处理班组名称
                        tp.setRevOrgSeq(null);//处理序列
                        tp.setRepairEmpID(null);//处理人
                        tp.setRepairEmp(null);//处理人名称
                        tp.setRevTime(null);//处理时间
                        tp.setMethodDesc(null);//施修方法
                        tp.setRepairResult(null);//处理结果
                        tp.setRepairDesc(null);//处理描述
                        tp.setHandlePersonId(null);//销票人
                        tp.setHandlePersonName(null);//销票人名称
                        tp.setHandleTime(null);//销票时间
                        tp.setHandleSiteID(null);//销票战场
                        tp.setRdpIDX(null);//整备单idx
                        tp.setRevPersonId(null);//接票人idx
                        tp.setRevPersonName(null);//接票人名称
                        tp.setFaultNoticeStatus(ZbglTp.STATUS_INIT);//票活状态
                        tp.setRepairClass("10");
                        tp.setFaultReason(null);//故障原因
                        zbglTpManager.saveOrUpdate(tp);
                    }
                }
                
                //机车保洁处理
                //通过整备单idx查询机车保洁对象
                ZbglCleaning zbglCleaning = zbglCleaningManager.getEntityByRdp(zbglRdpIDX);
                if (zbglCleaning != null) {
                    zbglCleaningManager.getDaoUtils().remove(zbglCleaning);
                }
                
                //机车上砂对象删除
                //通过出入段台账idx查询机车上砂对象
                ZbglSanding zbglSanding = zbglSandingManager.getEntityByAccountIDX(inOrLatestOutAccount.getIdx());
                if (zbglSanding != null) {
                    zbglSandingManager.getDaoUtils().remove(zbglSanding);
                }
                
//              级联删除普查整治任务单以及下面的任务项根据整备单idx
                zbglPczzWIManager.deleteZbglPczzWiInfo(zbglRdpIDX);
                
                //整备范围活逻辑删除
                //通过整备单idx查询范围活list
                List<ZbglRdpWi> zbglRdpWiList = zbglRdpWiManager.getAllRdpWiListByRdp(zbglRdpIDX);
                
                //判断是否生成了范围活
                if (zbglRdpWiList != null && zbglRdpWiList.size() > 0) {
                    for (ZbglRdpWi wi : zbglRdpWiList) {
//                        String zbglRdpWiIDX = wi.getIdx();
                        
                        //技术措施状态修改
                        List<ZbglTecOrder> zbglTecOrderList = zbglTecOrderManager.getZbglTecOrderByZbglRdpWiIDX(wi.getWiIDX());
                        if (zbglTecOrderList != null && zbglTecOrderList.size() > 0 ) {
                            for (ZbglTecOrder order : zbglTecOrderList) {
                                order.setOrderStatus(ZbglTecOrder.STATUS_PUBLISH);
                                zbglTecOrderManager.saveOrUpdate(order);
                            }
                        }
                        
                        //删除范围活
                        wi.setRecordStatus(1);
                        zbglRdpWiManager.saveOrUpdate(wi);
                    }
                }
                
                //整备单逻辑删除
                zbglRdp.setRecordStatus(1);
                this.daoUtils.getHibernateTemplate().saveOrUpdate(zbglRdp);
                daoUtils.flush();
                
            }
            
            if (flag) {
//              判断是否已经有整备单生成了，如果已经生成了，无论入段去向为何都不会生成整备单
                ZbglRdp zbglRdp = this.getRdpByAccount(inOrLatestOutAccount.getIdx());
                if (zbglRdp != null) {
                    return;
                }else {
//                  如果包含传递过来的，不包含数据库的，那么就要生成整备单信息，并且初始化相应的提票信息
                    if (StringUtils.isNotBlank(sysToGo) && !sysToGo.contains(dbToGo) && sysToGo.contains(inOrLatestOutAccount.getToGo())) {
                        if (" ".equals(inOrLatestOutAccount.getToGo())) {
                            inOrLatestOutAccount.setToGo(null);
                        }
                        trainAccessAccountManager.updateBizByToGO(inOrLatestOutAccount);
                    }
                }
            }
        }
    }
    
    /**
     * <li>说明：获取机车整备单列表
     * <li>创建人：林欢
     * <li>创建日期：2016-9-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 机车整备单列表
     */
    public List<ZbglRdp> getRdpList(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from ZbglRdp where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap)).append(" and recordStatus = 0");
        return daoUtils.find(hql.toString());
    }
}
