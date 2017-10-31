package com.yunda.jx.pjjx.partsrdp.wpinst.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.pjjx.base.wpdefine.entity.WPNode;
import com.yunda.jx.pjjx.partsrdp.wpinst.entity.PartsRdpNode;
import com.yunda.jx.pjjx.partsrdp.wpinst.entity.PartsRdpNodeBean;
import com.yunda.jx.pjjx.partsrdp.wpinst.entity.PartsRdpNodeSeq;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 配件检修作业节点查询业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-9-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.3
 */
@Service(value="partsRdpNodeQueryManager")
public class PartsRdpNodeQueryManager extends JXBaseManager<PartsRdpNode, PartsRdpNode>{
    
    /** PartsRdpNodeSeq业务类,作业流程节点前后置关系 */
    @Resource
    private PartsRdpNodeSeqManager partsRdpNodeSeqManager;
    
    private static final String RDPIDX = "rdpIDX";
    
    /**
     * <li>说明：查询当前人员可以处理的检修作业节点任务
     * <li>创建人：程锐
     * <li>创建日期：2015-9-28
     * <li>修改人：何涛
     * <li>修改日期：2015-03-11
     * <li>修改内容：重构，优化代码
     * <li>修改人：何涛
     * <li>修改日期：2015-03-21
     * <li>修改内容：增加查询参数“下车车型号”和“名称规格型号”，统一“配件识别码”和“配件编号”使用同一个字段进行匹配匹配
     * @param searchEntity 配件检修作业节点实体包装类对象
     * @return 当前人员可以处理的检修作业节点任务分页对象
     * @throws BusinessException
     */
    public Page<PartsRdpNodeBean> queryNodeListByUser(SearchEntity<PartsRdpNodeBean> searchEntity) throws BusinessException {        
        StringBuilder sb = new StringBuilder("SELECT * FROM (");     
        sb.append(SqlMapUtil.getSql("pjjx-partsRdpNode:selectAll"));
        sb.append(") WHERE 0 = 0");
        
        PartsRdpNodeBean bean = searchEntity.getEntity();
        
        // 未处理的检修作业节点，含（未处理、处理中、返修）
        if ("1".equals(bean.getStatus())) {
            sb.append(" AND STATUS IN ('");
            sb.append(PartsRdpNode.CONST_STR_STATUS_WCL).append("', '");         // 未处理
            sb.append(PartsRdpNode.CONST_STR_STATUS_CLZ).append("', '");         // 处理中
            sb.append(PartsRdpNode.CONST_STR_STATUS_FX);                         // 返修
            sb.append("')");
        // 已处理的检修作业节点
        } else if ("2".equals(bean.getStatus())) {
            sb.append(" AND STATUS = '").append(PartsRdpNode.CONST_STR_STATUS_YCL).append("'");
        }
        
        // 查询条件 - 工位
        if (!StringUtil.isNullOrBlank(bean.getWorkStationIDX())) {
            sb.append(" AND WORK_STATION_IDX LIKE '%").append(bean.getWorkStationIDX()).append("%'");
        }
        
        // 配件识别码和配件编号使用同一个字段进行匹配时的处理
        String identificationCode = bean.getIdentificationCode();
        if (!StringUtil.isNullOrBlank(bean.getPartsNo()) && !StringUtil.isNullOrBlank(identificationCode)) {
            sb.append(" AND (");
            sb.append(" (IDENTIFICATION_CODE LIKE '%").append(bean.getPartsNo());
            sb.append("%' OR PARTS_ACCOUNT_IDX IN (SELECT PARTS_ACCOUNT_IDX FROM PJJX_RecordCode_Bind where Record_CODE LIKE '%");
            sb.append(identificationCode).append("%')) ");
            sb.append(" OR");
            sb.append(" PARTS_NO LIKE '%").append(identificationCode).append("%'");
            sb.append(" )");
        }
        
        // 查询条件 - 下车车型号
        if (!StringUtil.isNullOrBlank(bean.getUnloadTrainType())) {
            sb.append(" AND LOWER(UNLOAD_TRAINTYPE || UNLOAD_TRAINNO) LIKE '%").append(bean.getUnloadTrainType().toLowerCase()).append("%'");
        }
        // 查询条件 - 名称规格型号
        if (!StringUtil.isNullOrBlank(bean.getSpecificationModel())) {
            sb.append(" AND LOWER(PARTS_NAME || SPECIFICATION_MODEL) LIKE '%").append(bean.getSpecificationModel().toLowerCase()).append("%'");
        }
        
//        sb.append(" ORDER BY SEQ_NO ASC");
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
        return this.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, PartsRdpNodeBean.class);        
    }
    
    /**
     * <li>说明：查询当前人员可以处理的检修作业节点任务的数量
     * <li>创建人：程锐
     * <li>创建日期：2015-10-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 配件检修作业节点实体包装类对象
     * @return 当前人员可以处理的检修作业节点任务数量
     * @throws BusinessException
     */
    public int queryNodeCountByUser(SearchEntity<PartsRdpNodeBean> searchEntity) throws BusinessException{
        return this.queryNodeListByUser(searchEntity).getTotal();
    }
    
    /**
     * <li>说明：根据作业流程IDX查询其关联的节点任务列表
     * <li>创建人：程锐
     * <li>创建日期：2015-9-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 作业流程IDX
     * @return 作业流程IDX关联的节点任务列表
     */
    public List<PartsRdpNode> queryNodeListByRdp(String rdpIDX) {
        List<PartsRdpNode> firstChildList = getDirectChildren(rdpIDX, "ROOT_0");
        if (firstChildList == null || firstChildList.size() < 1)
            return null;
        return buildListByChild(new LinkedList<PartsRdpNode>(), firstChildList, rdpIDX);        
    }
    

    
    /**
     * <li>说明：获取【配件检修作业节点】，数据表中，应该能根据“作业主键”和“流程节点主键”唯一表示一个【配件检修作业节点】
     * <li>创建人：何涛
     * <li>创建日期：2014-12-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param rdpIDX 作业主键
     * @param wpNodeIDX 流程节点主键
     * @return PartsRdpNode 配件检修作业节点实体
     */
    @SuppressWarnings("unchecked")
    public PartsRdpNode getModel(String rdpIDX, String wpNodeIDX) {
        String hql = "From PartsRdpNode Where recordStatus = 0 And rdpIDX = ? And wpNodeIDX = ?";
        List list = this.daoUtils.find(hql, new Object[]{rdpIDX, wpNodeIDX});
        if (null != list && list.size() > 0) {
            return (PartsRdpNode) list.get(0);
        }
        return null;
    }
    
    /**
     * <li>获取【配件检修作业节点】所有直接“子节点”
     * <li>创建人：何涛
     * <li>创建日期：2014-12-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param rdpIDX 作业主键
     * @param wpNodeIDX 流程节点主键 
     * @return List<PartsRdpNode> 配件检修作业节点实体集合
     */
    @SuppressWarnings("unchecked")
    public List<PartsRdpNode> getDirectChildren(String rdpIDX, String wpNodeIDX) {
        String hql = "From PartsRdpNode Where recordStatus = 0 And rdpIDX = ? And parentWPNodeIDX = ? order by seqNo";
        return this.daoUtils.find(hql, new Object[]{rdpIDX, wpNodeIDX});
    }
    
    /**
     * <li>说明：获取配件检修作业计划中第一层的无前置节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-11-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 配件检修任务单主键
     * @return 配件检修作业计划中第一层的无前置节点列表
     */ 
    @SuppressWarnings("unchecked")
    public List<PartsRdpNode> getFirstNodeListByWorkPlan(String rdpIDX) {
        String hql = SqlMapUtil.getSql("pjjx-rdpNode:getFirstNodeList");
        return daoUtils.find(hql, new Object[] {"ROOT_0", rdpIDX, rdpIDX});
    }
    
    /**
     * <li>说明：得到配件检修作业计划下节点的前置关系列表
     * <li>创建人：程锐
     * <li>创建日期：2015-11-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 配件检修任务单主键
     * @return 配件检修作业计划下节点的前置关系列表
     */
    @SuppressWarnings("unchecked")
    public List<PartsRdpNodeSeq> getRelListByWorkPlan(String rdpIDX) {
        String hql = SqlMapUtil.getSql("pjjx-rdpNode:getRelListByWorkPlan");
        return daoUtils.find(hql, new Object[] {rdpIDX});
    }
    
    /**
     * <li>获取【配件检修作业节点】所有直接“后置节点”
     * <li>创建人：何涛
     * <li>创建日期：2014-12-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param rdpIDX 作业主键
     * @param wpNodeIDX 流程节点主键 
     * @return List<PartsRdpNode> 配件检修作业节点实体集合
     */
    @SuppressWarnings("unchecked")
    public List<PartsRdpNode> getDirectAfters(String rdpIDX, String wpNodeIDX) {
        // 获取所有直接“后置节点”关系
        List<PartsRdpNodeSeq> aftersSeq = this.partsRdpNodeSeqManager.getDirectAfters(rdpIDX, wpNodeIDX);
        if (null == aftersSeq && aftersSeq.size() <= 0) {
            return null;
        }
        List<PartsRdpNode> entityList = new ArrayList<PartsRdpNode>();
        // 变量“后置节点”关系列表，查询所有的“后置”节点
        for (PartsRdpNodeSeq seq : aftersSeq) {
            PartsRdpNode entity = this.getModel(seq.getRdpIDX(), seq.getWPNodeIDX());
            if (null == entity) {
                throw new BusinessException("数据异常-不能通过【作业节点前后置关系】rdpIDX[" + seq.getRdpIDX() +
                        "] wPNodeIDX[" + seq.getWPNodeIDX() +
                        "]查询到【配件检修作业节点】");
            }
            entityList.add(entity);
        }
        return entityList;
    }
    
    /**
     * <li>说明：循环构建节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-9-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param list 节点列表
     * @param childList 下级节点列表
     * @param rdpIDX 作业流程IDX
     * @return 节点列表
     */
    private List<PartsRdpNode> buildListByChild(List<PartsRdpNode> list, List<PartsRdpNode> childList, String rdpIDX) {
        for (PartsRdpNode node : childList) {
            list.add(node);
            List<PartsRdpNode> childNodeList = getDirectChildren(rdpIDX, node.getWpNodeIDX());
            if (childList == null || childList.size() < 1)
                continue; 
            list = buildListByChild(list, childNodeList, rdpIDX);
        }
        return list;
    }
    
    /**
     * <li>说明：根据节点完成情况判断能否完成配件检修作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 配件检修作业计划IDX
     * @return true 能完成配件检修作业计划 false 不能
     */
    public boolean canCompleteRdp(String rdpIDX) {
        int allNodeCount = getAllNodeCountByRdp(rdpIDX);
        int completeNodeCount = getCompleteNodeCountByRdp(rdpIDX);
        if (allNodeCount == completeNodeCount)
            return true;
        return false;
    }
    
    /**
     * <li>说明：得到配件检修计划的所有已完成或已终止的节点数量
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 作业计划IDX
     * @return 配件检修计划的所有已完成或已终止的节点数量
     */
    public int getCompleteNodeCountByRdp(String rdpIDX) {
        return CommonUtil.getListSize(getCompleteNodeListByRdp(rdpIDX));
    }
    
    /**
     * <li>说明：获取作业计划的完成或终止的节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 作业计划IDX
     * @return 作业计划的完成或终止的节点列表
     */
    @SuppressWarnings("unchecked")
    public List<PartsRdpNode> getCompleteNodeListByRdp(String rdpIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put(RDPIDX, rdpIDX);
        paramMap.put("status", "in'".concat(PartsRdpNode.CONST_STR_STATUS_YCL).concat("','").concat(PartsRdpNode.CONST_STR_STATUS_YZZ).concat("'"));
        return getNodeList(paramMap);
    }
    
    /**
     * <li>说明：得到配件检修计划的所有节点数量
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 作业计划IDX
     * @return 机车检修计划的所有节点数量
     */
    public int getAllNodeCountByRdp(String rdpIDX) {
        return CommonUtil.getListSize(getNodeListByRdp(rdpIDX));
    }
    
    /**
     * <li>说明：获取配件作业计划的所有节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 作业计划IDX
     * @return 作业计划的所有节点列表
     */
    @SuppressWarnings("unchecked")
    public List<PartsRdpNode> getNodeListByRdp(String rdpIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put(RDPIDX, rdpIDX);
        return getNodeList(paramMap);
    }
    
    /**
     * <li>说明：获取配件作业计划的所有父节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 作业计划IDX
     * @return 作业计划的所有子节点列表
     */
    @SuppressWarnings("unchecked")
    public List<PartsRdpNode> getParentNodeListByRdp(String rdpIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put(RDPIDX, rdpIDX);
        paramMap.put("isLeaf", WPNode.CONST_INT_IS_LEAF_NO + "");
        return getNodeList(paramMap);
    }    
    
    /**
     * <li>说明：获取配件作业计划的所有子节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 作业计划IDX
     * @return 作业计划的所有子节点列表
     */
    @SuppressWarnings("unchecked")
    public List<PartsRdpNode> getLeafNodeListByRdp(String rdpIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put(RDPIDX, rdpIDX);
        paramMap.put("isLeaf", WPNode.CONST_INT_IS_LEAF_YES + "");
        return getNodeList(paramMap);
    }
    
    /**
     * <li>说明：获取节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 节点列表
     */
    @SuppressWarnings("unchecked")
    public List<PartsRdpNode> getNodeList(Map paramMap) {
        return daoUtils.find(getNodeHql(paramMap));
    }
    
    /**
     * <li>说明：查询流程节点hql
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 查询流程节点hql
     */
    private String getNodeHql(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from PartsRdpNode where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap)).append(" and recordStatus = 0");
        return hql.toString();
    }
    
    /**
     * <li>说明：配件检修作业节点树
     * <li>创建人：程锐
     * <li>创建日期：2015-12-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 上级wpNodeIDX
     * @param rdpIDX 配件作业计划IDX
     * @return 节点树列表
     */
    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> tree(String parentIDX, String rdpIDX) {
        String wpNodeIDX = "";
        if ("ROOT_0".equals(parentIDX)) {
            wpNodeIDX = parentIDX;
        } else {
            PartsRdpNode parentObj = getModel(rdpIDX, parentIDX);
            wpNodeIDX = parentObj.getWpNodeIDX();
        }
        List<PartsRdpNode> list = getDirectChildren(rdpIDX, wpNodeIDX);
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        for (PartsRdpNode t : list) {
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            nodeMap.put("id", t.getIdx());                      // 节点idx主键
            nodeMap.put("wpNodeIDX", t.getWpNodeIDX());                      
            nodeMap.put("text", t.getWpNodeName());          // 树节点显示名称
            nodeMap.put("leaf", t.getIsLeaf() == 0 ? false : true);                     // 是否是叶子节点 0:否；1：是
            nodeMap.put("wPNodeName", t.getWpNodeName());       // 节点名称
            nodeMap.put("wPNodeDesc", t.getWpNodeDesc());       // 节点描述
            nodeMap.put("seqNo", t.getSeqNo());                 // 顺序号
            children.add(nodeMap);
        }
        return children;
    }
    
    /**
     * <li>说明：获取配件检修作业计划下级节点的最小开工时间和最大完工时间
     * <li>创建人：程锐
     * <li>创建日期：2015-12-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 配件检修作业计划IDX
     * @return 配件检修作业计划下级节点的最小开工时间和最大完工时间
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getMinBeginAndMaxEndTimeByNode(String rdpIDX) {
        String sql = SqlMapUtil.getSql("pjjx-rdpNode:getMinBeginAndMaxEndTimeByNode")
                               .replace("#rdpIDX#", rdpIDX);
        return daoUtils.executeSqlQuery(sql);
    }
}
