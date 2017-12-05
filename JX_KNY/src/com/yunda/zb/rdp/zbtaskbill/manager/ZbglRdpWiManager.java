package com.yunda.zb.rdp.zbtaskbill.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.frame.yhgl.manager.IOmEmployeeManager;
import com.yunda.frame.yhgl.manager.IOmOrganizationManager;
import com.yunda.util.BeanUtils;
import com.yunda.zb.common.ZbConstants;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpNode;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpNodeManager;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpQueryManager;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWi;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWiDTO;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWidi;
import com.yunda.zb.tecorder.entity.ZbglTecOrder;
import com.yunda.zb.trainwarning.entity.ZbglWarning;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglRdpWi业务类,机车整备任务单
 * <li>创建人：程锐
 * <li>创建日期：2015-01-21
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "zbglRdpWiManager")
public class ZbglRdpWiManager extends JXBaseManager<ZbglRdpWi, ZbglRdpWi> {
    
    /** 人员业务类 */
    @Resource
    private IOmEmployeeManager omEmployeeManager;
    
    /** 操作者业务类 */
    @Resource
    private AcOperatorManager acOperatorManager;
    
    /** 组织机构业务类 */
    @Resource
    private IOmOrganizationManager omOrganizationManager;
    
    /** 机车整备任务单数据项业务类 */
    @Resource
    private ZbglRdpWidiManager zbglRdpWidiManager;
    
    @Resource
    private ZbglRdpQueryManager zbglRdpQueryManager;
    
    @Resource
    private ZbglRdpNodeManager zbglRdpNodeManager;
    
    /**
     * <li>说明：查询整备任务活分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-2-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param wiStatus 待接活/待销活，见ZbglRdpWi常量STATUS_TODO/STATUS_HANDLING
     * @param operatorid 操作者ID
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 整备任务活分页列表
     */
    @SuppressWarnings("unchecked")
    public Page queryRdpWiList(String searchJson,  
                               String wiStatus,
                               Long operatorid,
                               int start, 
                               int limit) throws Exception {
        StringBuilder selectSb = new StringBuilder();
        selectSb.append("select new ZbglRdpWi(a.idx, b.trainTypeShortName||' '||b.trainNo as trainTypeAndNo, a.wiClass,")
                .append("a.wiName, a.wiDesc, c.inTime, a.fetchTime, b.trainTypeIDX, b.trainNo, a.seqNo, b.trainTypeShortName, b.dID, b.dName,a.isHg,a.worker)");
        StringBuilder fromSb = getQueryRdpWiFromHql(searchJson, wiStatus, operatorid);
        String hql = selectSb.append(fromSb).toString();
        String totalHql = "select count(a.idx) ".concat(fromSb.toString());
        Page<ZbglRdpWi> page = findPageList(totalHql, hql, start, limit);
        List<ZbglRdpWi> newList = new ArrayList<ZbglRdpWi>();
        for (ZbglRdpWi wi : page.getList()) {
            ZbglRdpWi newWi = new ZbglRdpWi();
            BeanUtils.copyProperties(newWi, wi);
            newWi.setWiClassName(ZbglRdpWi.getWiClassName(wi.getWiClass()));
            newWi.setInTimeStr(wi.getInTime() != null ? DateUtil.yyyy_MM_dd_HH_mm_ss.format(wi.getInTime()) : "");
            newWi.setFetchTimeStr(wi.getFetchTime() != null ? DateUtil.yyyy_MM_dd_HH_mm_ss.format(wi.getFetchTime()) : "");
            newList.add(newWi);
        }
        return new Page(page.getTotal(),newList);
    }
    
    /**
     * <li>说明：根据任务单状态查询记录总数
     * <li>创建人：何涛
     * <li>创建日期：2015-7-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param wiStatus 任务单状态 待接活/待销活，见ZbglRdpWi常量STATUS_TODO/STATUS_HANDLING
     * @param operatorid  操作者ID
     * @return int 记录总数
     * @throws Exception
     */
    public int getRdpWiCount(String wiStatus, Long operatorid) throws Exception {
        String fromHql = getQueryRdpWiFromHql(null, wiStatus, operatorid).toString();
        return this.daoUtils.getCount(fromHql);
    }
    
    /**
     * <li>说明：领取整备任务活
     * <li>创建人：程锐
     * <li>创建日期：2015-2-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param idxs 整备任务活idx，多个idx用,分隔
     */
    @SuppressWarnings("unchecked")
    public void receiveRdp(Long operatorid, String idxs) throws Exception{
        String idxsStr = CommonUtil.buildInSqlStr(idxs);
        if (StringUtil.isNullOrBlank(idxsStr))
            throw new BusinessException("以,号分隔的idx字符串为空");
        OmEmployee emp = omEmployeeManager.findByOperator(operatorid);
        OmOrganization org = omOrganizationManager.findByOperator(operatorid);
        String sql = SqlMapUtil.getSql("zb-rdp:receiveRdp")
                               .replace(ZbConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace("#fetchTime#", DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace("#STATUS_HANDLING#", ZbglRdpWi.STATUS_HANDLING)
                               .replace("#handlePersonId#", emp != null ? emp.getEmpid().toString() : null)
                               .replace("#handlePersonName#", emp != null ? emp.getEmpname() : "")
                               .replace("#handleOrgID#", org != null ? org.getOrgid().toString() : null)
                               .replace("#handleOrgName#", org != null ? org.getOrgname() : "")
                               .replace(ZbConstants.IDXS, idxsStr);
        daoUtils.executeSql(sql);  
        
        String hql = "from ZbglRdpWi where idx in ".concat(idxsStr);
        List<ZbglRdpWi> rdpWiList = daoUtils.find(hql);
        for (ZbglRdpWi wi : rdpWiList) {
            ZbglRdp rdp = zbglRdpQueryManager.getModelById(wi.getRdpIDX());
            if (rdp != null) {
            	if (!StringUtil.isNullOrBlank(wi.getNodeIDX())) {
            		ZbglRdpNode node = zbglRdpNodeManager.getModelById(wi.getNodeIDX());
            		if (node != null) {
            			zbglRdpNodeManager.updateNodeForStart(node.getNodeName(), new Date(), rdp.getIdx(), ZbglRdpNode.STATUS_GOING);
            		}
            	}
            }
        }        
    }
    
    /**
     * <li>说明：撤销领取整备任务活
     * <li>创建人：程锐
     * <li>创建日期：2015-2-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param idxs 整备任务活idx，多个idx用,分隔
     */
    @SuppressWarnings("unchecked")
    public void cancelReceivedRdp(Long operatorid, String idxs) throws Exception{
        String idxsStr = CommonUtil.buildInSqlStr(idxs);
        if (StringUtil.isNullOrBlank(idxsStr))
            throw new BusinessException("以,号分隔的idx字符串为空");
        String sql = SqlMapUtil.getSql("zb-rdp:cancelReceivedRdp")
                               .replace(ZbConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace("#STATUS_TODO#", ZbglRdpWi.STATUS_TODO)
                               .replace(ZbConstants.IDXS, idxsStr);
        daoUtils.executeSql(sql);       
        zbglRdpWidiManager.updateForCancelReceivedRdp(idxsStr);
        String hql = "from ZbglRdpWi where idx in ".concat(idxsStr);
        List<ZbglRdpWi> rdpWiList = daoUtils.find(hql);
        for (ZbglRdpWi wi : rdpWiList) {
            ZbglRdp rdp = zbglRdpQueryManager.getModelById(wi.getRdpIDX());
            if (rdp != null) {
            	if (!StringUtil.isNullOrBlank(wi.getNodeIDX())) {
            		ZbglRdpNode node = zbglRdpNodeManager.getModelById(wi.getNodeIDX());
            		if (node != null) {
            			zbglRdpNodeManager.updateNodeForStart(node.getNodeName(), null, rdp.getIdx(), ZbglRdpNode.STATUS_UNSTART);
            		}
            	}
            }                
        }   
    }
    
    /**
     * <li>说明：整备任务活-销活
     * <li>创建人：程锐
     * <li>创建日期：2015-2-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param rdpWiIDX 机车整备任务单IDX
     * @param widiArray 机车整备任务单数据项实体数组
     * @throws Exception
     */
    public void updateForHandleRdp(Long operatorid, String rdpWiIDX, String isHg, ZbglRdpWidi[] widiArray,String worker) throws Exception {
        if (StringUtil.isNullOrBlank(rdpWiIDX))
            throw new BusinessException("整备任务单IDX为空");
        ZbglRdpWi wi = getModelById(rdpWiIDX);
        if (wi == null)
            throw new BusinessException("整备任务单为空");
        wi = buildHandleEntity(operatorid, wi);
        wi.setIsHg(isHg);
        wi.setWorker(worker);
        saveOrUpdate(wi);
        zbglRdpWidiManager.updateForHandleRdp(widiArray);        
        ZbglRdp rdp = zbglRdpQueryManager.getModelById(wi.getRdpIDX());
        if (rdp == null)
        	return;
        if (StringUtil.isNullOrBlank(wi.getNodeIDX()))
        	return;
        ZbglRdpNode node = zbglRdpNodeManager.getModelById(wi.getNodeIDX());
		if (node == null)
			return;
		zbglRdpNodeManager.updateNodeForEnd(node.getNodeName(), new Date(), rdp.getIdx(), ZbglRdpNode.STATUS_COMPLETE);
    }
    
    
    /**
     * 
     * <li>说明：整备任务活-暂存
     * <li>创建人：程锐
     * <li>创建日期：2015-3-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpWiIDX 机车整备任务单IDX
     * @param widiArray 机车整备任务单数据项实体数组
     * @throws Exception
     */
    public void updateForRdpWidi(String rdpWiIDX, ZbglRdpWidi[] widiArray) throws Exception {
        if (StringUtil.isNullOrBlank(rdpWiIDX))
            throw new BusinessException("整备任务单IDX为空");
        ZbglRdpWi wi = getModelById(rdpWiIDX);
        if (wi == null)
            throw new BusinessException("整备任务单为空");
        zbglRdpWidiManager.updateForHandleRdp(widiArray);
    }
    /**
     * <li>说明：获取同一机车整备单的已处理的机车整备任务单数量
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return 同一机车整备单的已处理的机车整备任务单数量
     */
    public int getHandledRdpWiCountByRdp(String rdpIDX) {
        List<ZbglRdpWi> list = getHandledRdpWiListByRdp(rdpIDX);
        return CommonUtil.getListSize(list);
    }
    
    /**
     * <li>说明：获取同一机车整备单的已处理的机车整备任务单列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return 同一机车整备单的已处理的机车整备任务单列表
     */
    @SuppressWarnings("unchecked")
    public List<ZbglRdpWi> getHandledRdpWiListByRdp(String rdpIDX) {
        if (StringUtil.isNullOrBlank(rdpIDX))
            return null;
        Map paramMap = new HashMap<String, String>();
        paramMap.put("rdpIDX", rdpIDX);
        paramMap.put("wiStatus", ZbglRdpWi.STATUS_HANDLED);
        return getRdpWiList(paramMap);
    }
    
    /**
     * <li>说明：获取同一机车整备单的机车整备任务单数量
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return 同一机车整备单的机车整备任务单数量
     */
    public int getAllRdpWiCountByRdp(String rdpIDX) {
        List<ZbglRdpWi> list = getAllRdpWiListByRdp(rdpIDX);
        return CommonUtil.getListSize(list);
    }
    
    /**
     * <li>说明：获取同一机车整备单的机车整备任务单列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @return 同一机车整备单的机车整备任务单列表
     */
    @SuppressWarnings("unchecked")
    public List<ZbglRdpWi> getAllRdpWiListByRdp(String rdpIDX) {
        if (StringUtil.isNullOrBlank(rdpIDX))
            return null;
        Map paramMap = new HashMap<String, String>();
        paramMap.put("rdpIDX", rdpIDX);
        return getRdpWiList(paramMap);
    }
    
    /**
     * <li>说明：机车检测预警-下发班组触发的方法
     * <li>创建人：程锐
     * <li>创建日期：2015-3-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param warningList 机车检测预警列表
     * @return 机车检测预警列表
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public List<ZbglWarning> saveForWarningXfbz(List<ZbglWarning> warningList) throws BusinessException, NoSuchFieldException {
        List<ZbglRdpWi> entityList = new ArrayList<ZbglRdpWi>();
        for (ZbglWarning warning : warningList) {
            if (StringUtil.isNullOrBlank(warning.getRdpIDX()))
                continue;
            ZbglRdpWi wi = new ZbglRdpWi();
            wi.setRdpIDX(warning.getRdpIDX());
            wi.setWiClass(ZbglRdpWi.WICLASS_YJ);
            wi.setWiIDX(warning.getIdx());
            wi.setWiName("机车检测预警");
            wi.setWiDesc(warning.getWarningDesc());
            wi.setWiStatus(ZbglRdpWi.STATUS_TODO);
            wi.setSeqNo(0);
            entityList.add(wi);
        }
        saveOrUpdate(entityList);
        daoUtils.flush();
        for (ZbglWarning entity : warningList) {
            for (ZbglRdpWi wi : entityList) {
                if (wi.getWiIDX().equals(entity.getIdx())) {
                    entity.setRelIDX(wi.getIdx());
                    break;
                }
            }
        }
        return warningList;
    }

    /**
     * <li>说明：查询判断指令作业单是否已经生成
     * <li>创建人：王利成
     * <li>创建日期：2015-3-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 整备单ID
     * @param wiIDX 作业项目ID
     * @return ZbglRdpWi
     */
    public ZbglRdpWi getModel(String rdpIDX, String wiIDX) {
        String hql = "From ZbglRdpWi Where recordStatus = 0 And rdpIDX = ? And wiIDX = ?";
        return (ZbglRdpWi) this.daoUtils.findSingle(hql, new Object[]{rdpIDX, wiIDX});
    }
    
    /**
     * <li>说明：保存技术指令临时任务单
     * <li>创建人：王利成
     * <li>创建日期：2015-3-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 整备单ID
     * @param zbglTecIdx 指令主键
     * @param wiDesc 任务描述
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void  saveTecOrderRdpWi(String rdpIDX, String zbglTecIdx,String wiDesc) throws BusinessException, NoSuchFieldException{
        ZbglRdpWi zbglRdpWi = new ZbglRdpWi();
        zbglRdpWi.setRdpIDX(rdpIDX);
        zbglRdpWi.setWiIDX(zbglTecIdx);
        zbglRdpWi.setWiClass(ZbglRdpWi.WICLASS_ZLCS);
        zbglRdpWi.setWiStatus(ZbglRdpWi.STATUS_TODO);
        zbglRdpWi.setWiName(ZbglTecOrder.ZBGL_TECORDER_WINAME);
        zbglRdpWi.setWiDesc(wiDesc);
        zbglRdpWi.setSeqNo(0);
        saveOrUpdate(zbglRdpWi);
    }
    
    /**
     * <li>说明：获取整备任务活数量
     * <li>创建人：程锐
     * <li>创建日期：2015-4-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param wiStatus 待接活/待销活，见ZbglRdpWi常量STATUS_TODO/STATUS_HANDLING
     * @param operatorid 操作者ID
     * @return 整备任务活数量
     * @throws Exception
     */
    public int getRdpWiCount(String searchJson, String wiStatus, Long operatorid) throws Exception {
        StringBuilder fromSb = getQueryRdpWiFromHql(searchJson, wiStatus, operatorid);
        String totalHql = "select count(a.idx) ".concat(fromSb.toString());
        List list = daoUtils.find(totalHql);
        return Integer.parseInt(list.get(0).toString());
    }
    
    /**
     * <li>说明：销活时构造任务单实体对象
     * <li>创建人：程锐
     * <li>创建日期：2015-2-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param wi 任务单实体对象
     * @return 任务单实体对象
     */
    public ZbglRdpWi buildHandleEntity(Long operatorid, ZbglRdpWi wi) {
        SystemContext.setAcOperator(acOperatorManager.findLoginAcOprator(operatorid));
        OmEmployee employee = SystemContext.getOmEmployee();
        if(employee != null){
            wi.setHandlePersonName(employee.getEmpname());
            wi.setHandlePersonID(employee.getEmpid());
        }
        wi.setHandleTime(new Date());
        wi.setWiStatus(ZbglRdpWi.STATUS_HANDLED);
        wi.setUpdateTime(new Date());
        return wi;
    }
    
    /**
     * <li>说明：获取机车整备任务单列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 机车整备任务单列表
     */
    @SuppressWarnings("unchecked")
    private List<ZbglRdpWi> getRdpWiList(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from ZbglRdpWi where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap)).append(" and recordStatus = 0");
        return daoUtils.find(hql.toString());
    }
    
    /**
     * <li>说明：通过任务单idx和节点ID查询整备单
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIdx
     * @param nodeIdx
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<ZbglRdpWi> getRdpWiListByRdpAndNode(String rdpIdx,String nodeIdx){
        StringBuilder hql = new StringBuilder();
        hql.append("from ZbglRdpWi where 1 = 1 ").append(" and recordStatus = 0");
        hql.append(" and rdpIDX = '"+rdpIdx+"'");
        hql.append(" and nodeIDX = '"+nodeIdx+"'");
        hql.append(" order by seqNo ");
        return daoUtils.find(hql.toString());
    }
    
    /**
     * <li>说明：获取整备任务活查询hql的from部分
     * <li>创建人：程锐
     * <li>创建日期：2015-4-1
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param wiStatus 待接活/待销活，见ZbglRdpWi常量STATUS_TODO/STATUS_HANDLING
     * @param operatorid 操作者ID
     * @return 整备任务活查询hql的from部分
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private StringBuilder getQueryRdpWiFromHql(String searchJson, String wiStatus, Long operatorid) throws Exception {
        StringBuilder fromSb = new StringBuilder();
        fromSb.append(" from ZbglRdpWi a, ZbglRdp b, TrainAccessAccount c where a.recordStatus = 0").append(
            " and a.rdpIDX = b.idx and b.trainAccessAccountIDX = c.idx and b.recordStatus = 0").append(" and b.rdpStatus = '").append(
            ZbglRdp.STATUS_HANDLING).append("'").append(" and b.siteID = '").append(EntityUtil.findSysSiteId("")).append("'");
        
        if (ZbglRdpWi.STATUS_TODO.equals(wiStatus)) {
            fromSb.append(" and a.wiStatus = '").append(ZbglRdpWi.STATUS_TODO).append("' and a.handlePersonID is null");
        } else if (ZbglRdpWi.STATUS_HANDLING.equals(wiStatus)) {
            OmEmployee emp = omEmployeeManager.findByOperator(operatorid);
            if (emp == null)
                throw new BusinessException("operatorid对应的人员为空");
            fromSb.append(" and a.wiStatus = '").append(ZbglRdpWi.STATUS_HANDLING).append("' and a.handlePersonID = ").append(emp.getEmpid());
        }
        Map<String, String> queryMap = new HashMap<String, String>();
        if (!StringUtil.isNullOrBlank(searchJson)) {
            queryMap = JSONUtil.read(searchJson, Map.class);
        }
        StringBuilder multyAwhere = new StringBuilder();
        String trainTypeShortNameString = String.valueOf(queryMap.get("trainTypeShortName"));
        if (!StringUtil.isNullOrBlank(trainTypeShortNameString)) {
            multyAwhere.append(" and b.trainTypeShortName = '").append(trainTypeShortNameString).append("' ");
        }
        String trainNoString = String.valueOf(queryMap.get("trainNo"));
        if (!StringUtil.isNullOrBlank(trainNoString)) {
            multyAwhere.append(" and b.trainNo = '").append(trainNoString).append("' ");
        }
        fromSb.append(multyAwhere);
        return fromSb;
    }   
    
    /**
     * <li>说明：查询机车过滤信息（iPad应用）
     * <li>创建人：姚凯
     * <li>创建日期：2015-8-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param wiStatus 作业工单状态，待零活："TODO"，待销活："ONGOING"
     * @param searchParam 查询参数
     * @return List
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public List getFilterModels(String wiStatus, String searchParam) throws IOException {
        // 当前系统所在站场ID
        String siteId = EntityUtil.findSysSiteId(null);
        StringBuilder sb = new StringBuilder();
        sb.append(" Select  distinct A.trainTypeShortName || A.trainNo, A.trainTypeShortName, A.trainNo From ZbglRdp A, ZbglRdpWi B Where A.idx = B.rdpIDX AND A.rdpStatus = 'ONGOING'");
        // 查询条件 - 站场ID
        sb.append(" AND A.siteID = '").append(siteId).append("'");
        // 查询条件 - 作业工单状态，待零活："TODO"，待销活："ONGOING"
        sb.append(" AND B.wiStatus = '").append(wiStatus).append("'");
        // 页面的筛选
        if (!StringUtil.isNullOrBlank(searchParam)) {
            sb.append(" AND ( A.trainTypeShortName Like '%").append(searchParam.toUpperCase()).append("%'");
            sb.append(" or A.trainNo like '%").append(searchParam.toUpperCase()).append("%')");
        }
        // 根据车型车号排序
        sb.append(" Order By A.trainTypeShortName, A.trainNo");
        String hql = sb.toString();
        List list = this.daoUtils.find(hql);
        List<Map> result = new ArrayList<Map>();
        Map<String, Object> map = null;
        for (int i = 0; i < list.size(); i++) {
            map = new HashMap<String, Object>();
            Object[] array = (Object[]) list.get(i);
            map.put("idx", array[0]);
            map.put("trainTypeShortName", array[1]);
            map.put("trainNo", array[2]);
            result.add(map);
        }
        return result;
    }

    /**
     * <li>说明：通过整备单信息存储过程范围活
     * <li>创建人：林欢
     * <li>创建日期：2016-5-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param param 参数，整备单id
     */
    public void saveZbglRdpWiByProc(String[] param) {
        daoUtils.executeProc("sp_create_zb_wi_fw", param);
        daoUtils.flush();
    }

    /**
     * <li>说明：查询整备任务活分页列表(全部)
     * <li>创建人：林欢
     * <li>创建日期：2015-8-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param wiStatus 待接活/待销活，见ZbglRdpWi常量STATUS_TODO/STATUS_HANDLING
     * @param operatorid 操作者ID
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 整备任务活分页列表JSON字符串
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    @SuppressWarnings("unchecked")
    public Page queryRdpWiListWithOutWorkStation(String searchJson, String wiStatus, Long operatorid, int start, int limit) throws JsonParseException, JsonMappingException, IOException {
        StringBuffer sb = new StringBuffer();
        
        sb.append(" select a.idx, ");
        sb.append(" c.train_type_shortname || ' ' || c.train_no as trainTypeAndNo,a.wi_class,a.wi_name, ");
        sb.append(" a.wi_desc,c.in_time,a.fetch_time,c.train_type_idx, ");
        sb.append(" c.train_no,a.seq_no,c.train_type_shortname,c.d_id,c.d_name,a.worker,a.IS_HG ");
        sb.append(" from zb_zbgl_rdp_wi a ");
        sb.append(" left join zb_zbgl_rdp b on b.idx = a.rdp_idx and b.record_status = 0 ");
        sb.append(" left join twt_train_access_account c on c.idx = b.train_access_account_idx and c.record_status = 0 ");
        sb.append(" where a.record_status = 0 ");
        sb.append(" and b.rdp_status = '").append(ZbglRdp.STATUS_HANDLING).append("'");
        sb.append(" and b.siteid = '").append(EntityUtil.findSysSiteId("")).append("'");
        
        if (ZbglRdpWi.STATUS_TODO.equals(wiStatus)) {
            sb.append(" and a.wi_status = '").append(ZbglRdpWi.STATUS_TODO).append("' and a.handle_person_id is null");
        } else if (ZbglRdpWi.STATUS_HANDLING.equals(wiStatus)) {
            OmEmployee emp = omEmployeeManager.findByOperator(operatorid);
            if (emp == null)
                throw new BusinessException("operatorid对应的人员为空");
            sb.append(" and a.wi_status = '").append(ZbglRdpWi.STATUS_HANDLING).append("' and a.handle_person_id = ").append(emp.getEmpid());
        }
        Map<String, String> queryMap = new HashMap<String, String>();
        if (!StringUtil.isNullOrBlank(searchJson)) {
            queryMap = JSONUtil.read(searchJson, Map.class);
        }
        StringBuilder multyAwhere = new StringBuilder();
        String trainTypeShortNameString = String.valueOf(queryMap.get("trainTypeShortName"));
        if (!StringUtil.isNullOrBlank(trainTypeShortNameString)) {
            multyAwhere.append(" and b.train_type_shortname = '").append(trainTypeShortNameString).append("' ");
        }
        String trainNoString = String.valueOf(queryMap.get("trainNo"));
        if (!StringUtil.isNullOrBlank(trainNoString)) {
            multyAwhere.append(" and b.train_no = '").append(trainNoString).append("'");
        }
        sb.append(multyAwhere);
        
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT From (" + sb.toString() + ")";
        
        Page<ZbglRdpWiDTO> page = this.queryPageList(totalSql, sb.toString(), start, limit, false, ZbglRdpWiDTO.class);
        for (ZbglRdpWiDTO wi : page.getList()) {
            wi.setWiClassName(ZbglRdpWi.getWiClassName(wi.getWiClass()));
            wi.setInTimeStr(wi.getInTime() != null ? DateUtil.yyyy_MM_dd_HH_mm_ss.format(wi.getInTime()) : "");
            wi.setFetchTimeStr(wi.getFetchTime() != null ? DateUtil.yyyy_MM_dd_HH_mm_ss.format(wi.getFetchTime()) : "");
        }
        return page;
    }

    /**
     * <li>说明：查询整备任务活分页列表(和工位挂钩)
     * <li>创建人：林欢
     * <li>创建日期：2015-8-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param wiStatus 待接活/待销活，见ZbglRdpWi常量STATUS_TODO/STATUS_HANDLING
     * @param operatorid 操作者ID
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @param workStationIDX 工位idx
     * @return 整备任务活分页列表JSON字符串
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    @SuppressWarnings("unchecked")
    public Page queryRdpWiListWithWorkStation(String searchJson, String wiStatus, Long operatorid, int start, int limit, String workStationIDX) throws JsonParseException, JsonMappingException, IOException {

        StringBuffer sb = new StringBuffer();
        
        sb.append(" select a.idx, ");
        sb.append(" c.train_type_shortname || ' ' || c.train_no as trainTypeAndNo,a.wi_class,a.wi_name, ");
        sb.append(" a.wi_desc,c.in_time,a.fetch_time,c.train_type_idx, ");
        sb.append(" c.train_no,a.seq_no,c.train_type_shortname,c.d_id,c.d_name,a.worker,a.IS_HG ");
        sb.append(" from zb_zbgl_rdp_wi a ");
        sb.append(" left join zb_zbgl_rdp b on b.idx = a.rdp_idx and b.record_status = 0 ");
        sb.append(" left join twt_train_access_account c on c.idx = b.train_access_account_idx and c.record_status = 0 ");
        sb.append(" left join zb_zbgl_job_process_node d on d.rdp_idx = b.idx and d.idx = a.node_idx and d.record_status = 0 ");
        sb.append(" left join jxgc_work_station e on e.idx = d.work_station_idx and e.record_status = 0 ");
        sb.append(" left join zbgl_work_station_binding f on f.work_station_idx = e.idx ");
        sb.append(" where a.record_status = 0 ");
        sb.append(" and b.rdp_status = '").append(ZbglRdp.STATUS_HANDLING).append("'");
        sb.append(" and b.siteid = '").append(EntityUtil.findSysSiteId("")).append("'");
        
        if (ZbglRdpWi.STATUS_TODO.equals(wiStatus)) {
            sb.append(" and a.wi_status = '").append(ZbglRdpWi.STATUS_TODO).append("' and a.handle_person_id is null");
        } else if (ZbglRdpWi.STATUS_HANDLING.equals(wiStatus)) {
            OmEmployee emp = omEmployeeManager.findByOperator(operatorid);
            if (emp == null)
                throw new BusinessException("operatorid对应的人员为空");
            sb.append(" and a.wi_status = '").append(ZbglRdpWi.STATUS_HANDLING).append("' and a.handle_person_id = ").append(emp.getEmpid());
        }
        Map<String, String> queryMap = new HashMap<String, String>();
        if (!StringUtil.isNullOrBlank(searchJson)) {
            queryMap = JSONUtil.read(searchJson, Map.class);
        }
        StringBuilder multyAwhere = new StringBuilder();
        String trainTypeShortNameString = String.valueOf(queryMap.get("trainTypeShortName"));
        if (!StringUtil.isNullOrBlank(trainTypeShortNameString)) {
            multyAwhere.append(" and b.train_type_shortname = '").append(trainTypeShortNameString).append("' ");
        }
        String trainNoString = String.valueOf(queryMap.get("trainNo"));
        if (!StringUtil.isNullOrBlank(trainNoString)) {
            multyAwhere.append(" and b.train_no = '").append(trainNoString).append("'");
        }
        sb.append(multyAwhere);
        
        //添加工位idx
        sb.append(" and f.work_station_idx = '").append(workStationIDX).append("'");
        
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT From (" + sb.toString() + ")";
        
        Page<ZbglRdpWiDTO> page = this.queryPageList(totalSql, sb.toString(), start, limit, false, ZbglRdpWiDTO.class);
        for (ZbglRdpWiDTO wi : page.getList()) {
            wi.setWiClassName(ZbglRdpWi.getWiClassName(wi.getWiClass()));
            wi.setInTimeStr(wi.getInTime() != null ? DateUtil.yyyy_MM_dd_HH_mm_ss.format(wi.getInTime()) : "");
            wi.setFetchTimeStr(wi.getFetchTime() != null ? DateUtil.yyyy_MM_dd_HH_mm_ss.format(wi.getFetchTime()) : "");
        }
        return page;
    }
    
    /**
     * <li>说明：整备范围活排序
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIdx
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void sortZbglRdpWi(String rdpIdx) throws BusinessException, NoSuchFieldException {
        // 任务单
        List<ZbglRdpWi> sortWilists = new ArrayList<ZbglRdpWi>();
        getSortWiLists(sortWilists,rdpIdx,null);
        if(sortWilists.size() > 0){
            this.saveOrUpdate(sortWilists);
        }
    }

    /**
     * <li>说明：获取排序后列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param wilist
     * @param rdpIdx
     * @param parentId
     * @param sort
     */
    private void getSortWiLists(List<ZbglRdpWi> wilist, String rdpIdx, String parentId) {
        List<ZbglRdpNode> nodes = zbglRdpNodeManager.get1ZbglNodeListByRdpIDX(rdpIdx, parentId);
        if(nodes != null && nodes.size() > 0){
            for (ZbglRdpNode node : nodes) {
                List<ZbglRdpWi> ws = this.getRdpWiListByRdpAndNode(rdpIdx, node.getIdx());
                for (ZbglRdpWi wi : ws) {
                    wi.setSeqNo(wilist.size() + 1);
                    wilist.add(wi);
                }
                getSortWiLists(wilist, rdpIdx, node.getIdx());
            }
        }
    }

    /**
     * <li>说明：查询未完成的任务单
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIdx 任务单ID
     * @return
     */
    public List<ZbglRdpWi> getAllRdpWiNoCompleteListByRdp(String rdpIdx){
        StringBuffer hql = new StringBuffer(" From ZbglRdpWi where recordStatus = 0 and wiStatus <> '"+ZbglRdpWi.STATUS_HANDLED+"' and rdpIDX = ? ");
        return this.daoUtils.find(hql.toString(), new Object[]{rdpIdx});
    }
    
    
    /**
     * <li>说明：获取对应专业的任务单
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIdx
     * @param wiIdx
     * @return
     */
    public ZbglRdpWi getZbglRdpWiByRdp(String rdpIdx,String wiIdx){
        StringBuffer hql = new StringBuffer(" From ZbglRdpWi where recordStatus = 0 and wiStatus <> '"+ZbglRdpWi.STATUS_HANDLED+"' and rdpIDX = ? and wiIDX = ? ");
        return (ZbglRdpWi)this.daoUtils.findSingle(hql.toString(), new Object[]{rdpIdx,wiIdx});
    }

    /**
     * <li>说明：通过整备范围生成
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-01
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdp 整备单rdp
     * @return
     */
	public void saveZbglRdpWiByRdp(ZbglRdp rdp) {
		// 1、查询对应范围的专业数据
		
		
	}
    
    
}
