package com.yunda.jx.jxgc.processdef.manager;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.IbaseComboTree;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.order.AbstractOrderManager;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jxgc.processdef.entity.JobNodeStationDef;
import com.yunda.jx.jxgc.processdef.entity.JobProcessNodeDef;
import com.yunda.jx.jxgc.processdef.entity.JobProcessNodeRelDef;
import com.yunda.jx.third.edo.entity.Task;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JobProcessNodeDef业务类,检修作业流程节点
 * <li>创建人：何涛
 * <li>创建日期：2015-4-14
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "jobProcessNodeDefManager")
public class JobProcessNodeDefManager extends AbstractOrderManager<JobProcessNodeDef, JobProcessNodeDef> implements IbaseComboTree{
    
    /** JobNodeStationDef业务类,关联作业工位 */
    @Resource
    private JobNodeStationDefManager jobNodeStationDefManager;
    
    /** JobNodeProjectDef业务类,关联作业项目 */
    @Resource
    private JobNodeProjectDefManager jobNodeProjectDefManager;
    
    /** JobProcessNodeRelDef业务类,节点前后置关系 */
    @Resource
    private JobProcessNodeRelDefManager jobProcessNodeRelDefManager;
    /**
     * <li>说明：记录新增（更新）时的验证，验证节点编码字段不能重复
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param t 作业节点实体对象
     * @return String[] 错误消息
     */
    @Override
    public String[] validateUpdate(JobProcessNodeDef t) {
        // 节点编码唯一性验证
        JobProcessNodeDef entity = this.getModelByNodeCode(t.getNodeCode());
        if (null != entity && !entity.getIdx().equals(t.getIdx())) {
            return new String[]{"节点编码【" + t.getNodeCode() + "】已经存在，不能重复添加！"};
        }
        // 验证临修任务唯一性
        return super.validateUpdate(t);
    }
    
    /**
     * <li>说明：根据节点编码（全局唯一）获取节点实例对象
     * <li>创建人：何涛
     * <li>创建日期：2015-5-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeCode 节点编码
     * @return 节点实例对象
     */
    private JobProcessNodeDef getModelByNodeCode(String nodeCode) {
        String hql = "From JobProcessNodeDef Where recordStatus = 0 And nodeCode = ?";
        return (JobProcessNodeDef) this.daoUtils.findSingle(hql, new Object[]{nodeCode});
    }
    
    /**
     * <li>说明：查询当前对象在数据库中的记录总数
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param t 作业节点实体对象
     * @return int 记录总数
     */
    @Override
    public int count(JobProcessNodeDef t) {
        String hql = "Select Count(*) From JobProcessNodeDef Where recordStatus = 0 And processIDX = ? And parentIDX = ?";
        return this.daoUtils.getCount(hql, new Object[] { t.getProcessIDX(), t.getParentIDX() });
    }
    
    /**
     * <li>说明：获取排序范围内的同类型的所有记录
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param t 实体对象
     * @return List<UnionNode> 结果集
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<JobProcessNodeDef> findAll(JobProcessNodeDef t) {
        return getModels(t.getProcessIDX(), t.getParentIDX());
    }
    
    /**
     * <li>说明：置底
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 主键
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateMoveBottom(String idx) throws Exception {
        JobProcessNodeDef entity = this.getModelById(idx);
        int count = this.count(entity);
        // 获取被【置底】记录被置底前，在其后的所有记录
        String hql = "From JobProcessNodeDef Where recordStatus = 0 And seqNo > ? And processIDX = ? And parentIDX = ?";
        List<JobProcessNodeDef> list = this.daoUtils.find(hql, new Object[] { entity.getSeqNo(), entity.getProcessIDX(), entity.getParentIDX() });
        // 设置被【置底】记录的排序号为当前记录总数
        entity.setSeqNo(count);
        List<JobProcessNodeDef> entityList = new ArrayList<JobProcessNodeDef>();
        entityList.add(entity);
        // 设置其后的所有记录的排序后依次减一
        for (JobProcessNodeDef recordCard : list) {
            recordCard.setSeqNo(recordCard.getSeqNo() - 1);
            entityList.add(recordCard);
        }
        super.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：下移
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 主键
     * @throws Exception
     */
    public void updateMoveDown(String idx) throws Exception {
        JobProcessNodeDef entity = this.getModelById(idx);
        String hql = "From JobProcessNodeDef Where recordStatus = 0 And seqNo = ? And processIDX = ? And parentIDX = ?";
        // 获取被【下移】记录被下移前，紧随其后的记录
        JobProcessNodeDef nextEntity =
            (JobProcessNodeDef) this.daoUtils.findSingle(hql, new Object[] { entity.getSeqNo() + 1, entity.getProcessIDX(), entity.getParentIDX() });
        List<JobProcessNodeDef> entityList = new ArrayList<JobProcessNodeDef>(2);
        // 设置被【下移】记录的排序号+1
        entity.setSeqNo(entity.getSeqNo() + 1);
        entityList.add(entity);
        // 设置被【下移】记录后的记录的排序号-1
        nextEntity.setSeqNo(nextEntity.getSeqNo() - 1);
        entityList.add(nextEntity);
        super.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：置顶
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 主键
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateMoveTop(String idx) throws Exception {
        JobProcessNodeDef entity = this.getModelById(idx);
        // 获取被【置顶】记录被置顶前，在其前的所有记录
        String hql = "From JobProcessNodeDef Where recordStatus = 0 And seqNo < ? And processIDX = ? And parentIDX = ?";
        List<JobProcessNodeDef> list = this.daoUtils.find(hql, new Object[] { entity.getSeqNo(), entity.getProcessIDX(), entity.getParentIDX() });
        // 设置被【置顶】记录的排序号为1
        entity.setSeqNo(1);
        List<JobProcessNodeDef> entityList = new ArrayList<JobProcessNodeDef>();
        entityList.add(entity);
        // 设置其后的所有记录的排序后一次加一
        for (JobProcessNodeDef recordCard : list) {
            recordCard.setSeqNo(recordCard.getSeqNo() + 1);
            entityList.add(recordCard);
        }
        super.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：上移
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 主键
     * @throws Exception
     */
    public void updateMoveUp(String idx) throws Exception {
        JobProcessNodeDef entity = this.getModelById(idx);
        String hql = "From JobProcessNodeDef Where recordStatus = 0 And seqNo = ? And processIDX = ? And parentIDX = ?";
        // 获取被【上移】记录被上移移前，紧随其前的记录
        JobProcessNodeDef nextEntity =
            (JobProcessNodeDef) this.daoUtils.findSingle(hql, new Object[] { entity.getSeqNo() - 1, entity.getProcessIDX(), entity.getParentIDX() });
        List<JobProcessNodeDef> entityList = new ArrayList<JobProcessNodeDef>(2);
        // 设置被【上移】记录的排序号-1
        entity.setSeqNo(entity.getSeqNo() - 1);
        entityList.add(entity);
        // 设置被【上移】记录前的记录的排序号+1
        nextEntity.setSeqNo(nextEntity.getSeqNo() + 1);
        entityList.add(nextEntity);
        super.saveOrUpdate(entityList);
    }
    
    /**
     * <li>获取已知作业节点之后的所有同级作业节点
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param t 作业节点实体对象
     * @return 实体集合
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<JobProcessNodeDef> findAllBySN(JobProcessNodeDef t) throws Exception {
        String hql = "From JobProcessNodeDef Where recordStatus = 0 And seqNo >= ? And processIDX = ? And parentIDX = ?";
        return this.daoUtils.find(hql, new Object[] { t.getSeqNo(), t.getProcessIDX(), t.getParentIDX() });
    }
    
    /**
     * <li>说明：获取同一个作业流程、同级的作业节点
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param processIDX 作业流程idx主键
     * @param parentIDX 父作业节点主键
     * @return List<JobProcessNodeDef> 作业流程节点实体集
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNodeDef> getModels(String processIDX, String parentIDX) {
        String hql = "From JobProcessNodeDef Where recordStatus = 0 And processIDX = ? And parentIDX = ? Order By seqNo";
        return this.daoUtils.find(hql, new Object[] { processIDX, parentIDX });
    }
    
    /**
     * <li>说明：获取同一个作业流程的所有作业节点
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param processIDX 作业流程idx主键
     * @return List<JobProcessNodeDef> 作业流程节点实体集
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNodeDef> getModelsByProcessIDX(Serializable processIDX) {
        String hql = "From JobProcessNodeDef Where recordStatus = 0 And processIDX = ? Order By createTime";
        return this.daoUtils.find(hql, new Object[] { processIDX });
    }
    
    /**
     * <li>说明：作业节点树
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 上级节点idx主键
     * @param processIDX 作业主键
     * @return List<HashMap<String, Object>> 实体集合
     */
    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> tree(String parentIDX, String processIDX) {
        String idx = "";
        if ("ROOT_0".equals(parentIDX)) {
            idx = parentIDX;
        } else {
            JobProcessNodeDef parentObj = this.getModelById(parentIDX); // 获取父类对象
            idx = parentObj.getIdx();
        }
        String hql = "from JobProcessNodeDef where parentIDX = ? And processIDX = ? And recordStatus = " + Constants.NO_DELETE + " order by seqNo";
        List<JobProcessNodeDef> list = (List<JobProcessNodeDef>) this.daoUtils.find(hql, new Object[] { idx, processIDX });
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        for (JobProcessNodeDef t : list) {
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            nodeMap.put("id", t.getIdx()); // 节点idx主键
            nodeMap.put("text", formatDisplayInfo(t)); // 树节点显示名称
            nodeMap.put("leaf", t.getIsLeaf() == 0 ? false : true); // 是否是叶子节点 0:否；1：是
            nodeMap.put("nodeName", t.getNodeName()); // 节点名称
            nodeMap.put("nodeDesc", t.getNodeDesc()); // 节点描述
            nodeMap.put("seqNo", t.getSeqNo()); // 顺序号
            nodeMap.put("editable ", true); // 顺序号
            children.add(nodeMap);
        }
        return children;
    }
    
    /**
     * <li>说明：判断一个检修作业流程节点是否是叶子节点
     * <li>创建人：何涛
     * <li>创建日期：2015-5-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t 检修作业流程节点实例
     * @return true 是叶子节点，false 不是叶子节点
     */
    @SuppressWarnings("unused")
    private boolean isLeaf(JobProcessNodeDef t) {
        List<JobProcessNodeDef> list = this.getModels(t.getProcessIDX(), t.getIdx());
        if (null == list || 0 >= list.size()) {
            return true;
        }
        return false;
        
    }
    
    /**
     * <li>说明：递归获取作业节点的顺序号列
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 当前节点的【作业节点】
     * @param list 顺序号集合
     */
    private void listParentsSeqNo(JobProcessNodeDef entity, List<Integer> list) {
        list.add(entity.getSeqNo());
        if (!entity.getParentIDX().equals("ROOT_0")) {
            listParentsSeqNo(this.getModelById(entity.getParentIDX()), list);
        }
    }
    
    /**
     * <li>说明：格式化作业节点顺序号显示
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 当前节点的
     * @return 实体界面显示文本
     */
    private String formatDisplayInfo(JobProcessNodeDef entity) {
        List<Integer> list = new ArrayList<Integer>();
        this.listParentsSeqNo(entity, list);
        int length = list.size();
        StringBuilder sb = new StringBuilder();
        for (int i = length - 1; i >= 0; i--) {
            sb.append(list.get(i));
            if(i == 0) {
                sb.append("、");
            } else {
                sb.append(".");
            }
        }
        // 节点名称
        sb.append(entity.getNodeName());
        return sb.toString();
    }
    
    /**
     * <li>说明：获取设置指定节点主键的直接后置节点
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 作业节点主键
     * @return List<JobProcessNodeDef> 作业节点实体集合
     */
    @SuppressWarnings("unchecked")
    private List<JobProcessNodeDef> getAfterWPNodes(String nodeIDX) {
        String hql =
            "From JobProcessNodeDef Where idx In (Select nodeIDX From JobProcessNodeRelDef Where preNodeIDX = ? And recordStatus = 0) And recordStatus = 0";
        return this.daoUtils.find(hql, new Object[] { nodeIDX });
    }
    
    /**
     * <li>说明：获取设置指定节点主键的直接前置节点
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 作业节点主键
     * @return List<JobProcessNodeDef> 作业节点实体集合
     */
    @SuppressWarnings("unchecked")
    private List<JobProcessNodeDef> getBeforeWPNodes(String nodeIDX) {
        String hql =
            "From JobProcessNodeDef Where idx In (Select preNodeIDX From JobProcessNodeRelDef Where nodeIDX = ? And recordStatus = 0) And recordStatus = 0";
        return this.daoUtils.find(hql, new Object[] { nodeIDX });
    }
    
    /**
     * <li>说明：递归获取设置指定节点主键的所有直接和间接后置节点
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 作业节点主键
     * @param entityList 作业节点实体集合
     */
    public void listAfterNodes(String nodeIDX, List<JobProcessNodeDef> entityList) {
        List<JobProcessNodeDef> afterWPNodes = this.getAfterWPNodes(nodeIDX);
        if (null != afterWPNodes && 0 < afterWPNodes.size()) {
            for (JobProcessNodeDef wpNode : afterWPNodes) {
                if (!entityList.contains(wpNode)) {
                    entityList.add(wpNode);
                }
                listAfterNodes(wpNode.getIdx(), entityList);
            }
        }
    }
    
    /**
     * <li>说明：递归获取设置指定节点主键的所有直接和间接前置节点
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 作业节点主键
     * @param entityList 作业节点实体集合
     */
    public void listBeforeNodes(String nodeIDX, List<JobProcessNodeDef> entityList) {
        List<JobProcessNodeDef> beforeWPNodes = this.getBeforeWPNodes(nodeIDX);
        if (null != beforeWPNodes && 0 < beforeWPNodes.size()) {
            for (JobProcessNodeDef wpNode : beforeWPNodes) {
                if (!entityList.contains(wpNode)) {
                    entityList.add(wpNode);
                }
                listBeforeNodes(wpNode.getIdx(), entityList);
            }
        }
    }
    
    /**
     * <li>说明：查询作业节点的所有父节点，并以从叶子到根的顺序存入list
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 上级作业节点主键
     * @param list 作业节点实体集合
     */
    public void listParentsWPNodes(String parentIDX, List<JobProcessNodeDef> list) {
        JobProcessNodeDef node = this.getModelById(parentIDX);
        if (null != node) {
            list.add(node);
            listParentsWPNodes(node.getParentIDX(), list);
        }
    }
    
    /**
     * <li>说明：获取检修作业流程节点的最长工期
     * <li>创建人：何涛
     * <li>创建日期：2015-5-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t 检修作业流程节点对象实例
     * @return Double 检修作业流程节点的最长工期
     */
    private Double getMaxRatedWorkMinutes(JobProcessNodeDef t) {
        // 如果是叶子节点则直接返回该节点的最大工期
        if (JobProcessNodeDef.CONST_INT_IS_LEAF_YES == t.getIsLeaf().intValue()) {
            return this.getRatedWorkMinutes(t);
        }
        // 如果是父节点，则计算该节点下所有子节点的最大工期
        Map<String, Double> map = new HashMap<String, Double>();
        List<JobProcessNodeDef> entityList = this.getModels(t.getProcessIDX(), t.getIdx());
        if (null == entityList || 0 >= entityList.size()) {
            return 0D;
        }
        for (JobProcessNodeDef node : entityList) {
            map.put(node.getIdx(), this.getRatedWorkMinutes(node));
        }
        // 获取最大的工期值
        Collection<Double> values = map.values();
        Double ratedWorkMinutes = 0D;
        for (Double d : values) {
            if (null == d || 0D >= d) {
                continue;
            }
            if (d > ratedWorkMinutes) {
                ratedWorkMinutes = d;
            }
        }
        return ratedWorkMinutes;
    }
    
    /**
     * <li>说明：获取单个机车检修作业流程节点的最长工期，涉及前置节点的计算，
     * <li>创建人：何涛
     * <li>创建日期：2015-5-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t 作业流程节点对象实例
     * @return Double 单个机车检修作业流程节点的最大工期
     */
    private Double getRatedWorkMinutes(JobProcessNodeDef t) {
        // 获取该节点的前置节点关系
        List<JobProcessNodeRelDef> nodeRelList = this.jobProcessNodeRelDefManager.getModelsByWPNodeIDX(t.getIdx());
        if (null == nodeRelList || 0 >= nodeRelList.size()) {
            return t.getRatedWorkMinutes();
        }
        Map<String, Double> map = new HashMap<String, Double>();
        for (JobProcessNodeRelDef nodeRel : nodeRelList) {
            // 获取额定工期（分钟）
            Double ratedWorkMinutes = null == t.getRatedWorkMinutes()? 0D : t.getRatedWorkMinutes();
            
            // 递归获取前置节点的最大工期
            JobProcessNodeDef preNodeDef = this.getModelById(nodeRel.getPreNodeIDX());
            if (null == preNodeDef) {
                continue;
            }
            Double temp = this.getRatedWorkMinutes(preNodeDef);
            // 加上前置节点对象的额定工期
            if (null != temp) {
                ratedWorkMinutes += temp;
            }
            // 加上前置节点的延搁时间（暂时不考虑前置节点类型，当前统一处理为“完成-开始(FS)”）
            if (null != nodeRel.getDelayTime()) {
                ratedWorkMinutes += nodeRel.getDelayTime();
            }
            map.put(nodeRel.getIdx(), ratedWorkMinutes);
        }
        // 获取最大的工期值
        Collection<Double> values = map.values();
        Double temp = 0D;
        for (Double d : values) {
            if (temp < d) {
                temp = d;
            }
        }
        return temp;
    }
    
    /**
     * <li>在保存叶子节点时，同步更新所有父节点的“工期（分钟）”字段值
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：何涛
     * <li>修改日期：2015-08-21
     * <li>修改内容：如果父节点“计划模式”为自动，则根据子节点工期情况更新父节点工期，反之则不进行更新
     * <li>修改人：张迪
     * <li>修改日期：2016-07-16
     * <li>修改内容：不根据启动模式判断是否更新父节点工期
     * @param parentIDX 上级节点对象idx主键
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateParentWPNodes(String parentIDX) throws BusinessException, NoSuchFieldException {
        if (StringUtil.isNullOrBlank(parentIDX) || "ROOT_0".equals(parentIDX)) {
            return;
        }
        // 获取父节点对象实例
        JobProcessNodeDef parent = this.getModelById(parentIDX);
        if (JobProcessNodeDef.CONST_INT_IS_LEAF_NO != parent.getIsLeaf().intValue()) {
            parent.setIsLeaf(JobProcessNodeDef.CONST_INT_IS_LEAF_NO);
        }
//        // 如果父节点“计划模式”为自动，则根据子节点工期情况更新父节点工期
//        if (JobProcessNodeDef.PLANMODE_AUTO.equals(parent.getPlanMode())) {
            // 获取该节点下所有节点的最长工期
            parent.setRatedWorkMinutes(this.getMaxRatedWorkMinutes(parent));
//        }
        // 调用该方法递归更新父节点的父节点对象
        this.saveOrUpdate(parent);
    }
    
    /**
     * <li>说明：重写保存方法
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param t 作业节点实体
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @Override
    public void saveOrUpdate(JobProcessNodeDef t) throws BusinessException, NoSuchFieldException {
        // 如果在子节点下增加下级，则在保存新增的节点时要更新其上级节点为父节点
        if (null == t.getIdx() || t.getIdx().trim().length() <= 0) {
            t.setIdx(null);
        }
        super.saveOrUpdate(t);
        // 更新父节点相关信息（递归）
        this.updateParentWPNodes(t.getParentIDX());
    }
    
    /**
     * <li>说明：关联查询节点前置节点
     * <li>创建人：何涛
     * <li>创建日期：2014-11-25
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param searchEntity 查询对象实体
     * @return Page<UnionWPNode> 实体集合
     */
    @SuppressWarnings("unchecked")
    public Page<UnionNode> acquirePageList(SearchEntity<JobProcessNodeDef> searchEntity) {
        String sql = SqlMapUtil.getSql("jxgc-processNode:findUnionNode");
        StringBuilder sb = new StringBuilder(sql);
        JobProcessNodeDef entity = searchEntity.getEntity();
        if (!StringUtil.isNullOrBlank(entity.getProcessIDX())) {
            sb.append(" AND PROCESS_IDX ='").append(entity.getProcessIDX()).append("'");
        }
        if (!StringUtil.isNullOrBlank(entity.getParentIDX())) {
            sb.append(" AND PARENT_IDX ='").append(entity.getParentIDX()).append("'");
        }
        sb.append(" ORDER BY SEQ_NO ASC");
        sql = sb.toString();
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sql.substring(sql.indexOf("FROM"));
        return this.acquirePageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false);
    }
    
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：何涛
     * <li>创建日期：2015-4-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param totalSql 查询记录总数sql
     * @param sql 查询记录sql
     * @param start 查询开始索引
     * @param limit 查询结束索引
     * @param isQueryCacheEnabled 是否使用缓存
     * @return Page<UnionNode>
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public Page<UnionNode> acquirePageList(String totalSql, String sql, int start, int limit, Boolean isQueryCacheEnabled) throws BusinessException{
        final int beginIdx = start < 0 ? 0 : start;
        final int pageSize = limit < 0 ? Page.PAGE_SIZE : limit;
        final String total_sql = totalSql;
        final String fSql = sql;
        final Boolean useCached = isQueryCacheEnabled;
        final Class clazz = UnionNode.class;
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (Page<UnionNode>)template.execute(new HibernateCallback(){
            public Page<UnionNode> doInHibernate(Session s) {
                SQLQuery query = null;
                try {
                    query = s.createSQLQuery(total_sql);
                    query.addScalar("rowcount", Hibernate.INTEGER);
                    query.setCacheable(useCached); //缓存开关
                    int total = ((Integer)query.uniqueResult()).intValue();
                    query.setCacheable(false);
                    int begin = beginIdx > total ? total : beginIdx;
                    query = (SQLQuery)s.createSQLQuery(fSql).addEntity(clazz).setFirstResult(begin).setMaxResults(pageSize);
                    query.setCacheable(useCached); //缓存开关
                    return new Page<UnionNode>(total, query.list());
                } catch (HibernateException e) {
                    throw e;
                } finally{
                    if(query != null)   query.setCacheable(false);
                }
            }
        });
    }
    
    /**
     * <li>说明：逻辑删除
     * <li>创建人：何涛
     * <li>创建日期：2015-5-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 要删除的作业流程节点实体
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private void logicDelete(JobProcessNodeDef entity) throws BusinessException, NoSuchFieldException {
        super.logicDelete(entity.getIdx());
        // 级联删除关联的作业工位
        List<JobNodeStationDef> stationList = this.jobNodeStationDefManager.getModelByNodeIDX(entity.getIdx());
        if (null != stationList && stationList.size() > 0) {
            this.jobNodeStationDefManager.logicDelete(stationList);
        }
//        // 级联删除关联的作业项目
//        List<JobNodeProjectDef> projectList = this.jobNodeProjectDefManager.getModelByNodeIDX(entity.getIdx());
//        if (null != projectList && projectList.size() > 0) {
//            this.jobNodeProjectDefManager.logicDelete(projectList);
//        }
        // 级联节点参与的所有前后置关系
        List<JobProcessNodeRelDef> nodeRelList = this.jobProcessNodeRelDefManager.listModels(entity.getIdx());
        if (null != nodeRelList && nodeRelList.size() > 0) {
            this.jobProcessNodeRelDefManager.logicDelete(nodeRelList);
        }
        // 删除下级节点
        List<JobProcessNodeDef> list = this.getModels(entity.getProcessIDX(), entity.getIdx());
        if (null == list || 0 >= list.size()) {
            return;
        }
        for (JobProcessNodeDef node : list) {
            this.logicDelete(node);
        }
    }
    
    /**
     * <li>说明：逻辑删除
     * <li>创建人：何涛
     * <li>创建日期：2015-4-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 要删除的实体主键数组
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    @Override
    public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
        for (Serializable idx : ids) {
            JobProcessNodeDef entity = this.getModelById(idx);
            // 逻辑删除
            this.logicDelete(this.getModelById(idx));
            if (null != entity) {
                // 同步更新父节点对象实例的工期
                this.updateParentWPNodes(entity.getParentIDX());
            }
        }
    }
    
    /**
     * <li>说明：逻辑删除
     * <li>创建人：何涛
     * <li>创建日期：2015-4-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entityList 要删除的实体集
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    @Override
    public void logicDelete(List<JobProcessNodeDef> entityList) throws BusinessException, NoSuchFieldException {
        super.logicDelete(entityList);
        for (JobProcessNodeDef node : entityList) {
            // 级联删除关联的作业工位
            List<JobNodeStationDef> stationList = this.jobNodeStationDefManager.getModelByNodeIDX(node.getIdx());
            if (null != stationList && stationList.size() > 0) {
                this.jobNodeStationDefManager.logicDelete(stationList);
            }
//            // 级联删除关联的作业项目
//            List<JobNodeProjectDef> projectList = this.jobNodeProjectDefManager.getModelByNodeIDX(node.getIdx());
//            if (null != projectList && projectList.size() > 0) {
//                this.jobNodeProjectDefManager.logicDelete(projectList);
//            }
            // 级联节点参与的所有前后置关系
            List<JobProcessNodeRelDef> nodeRelList = this.jobProcessNodeRelDefManager.listModels(node.getIdx());
            if (null != nodeRelList && nodeRelList.size() > 0) {
                this.jobProcessNodeRelDefManager.logicDelete(nodeRelList);
            }
        }
    }
    
    /**
     * <li>标题：机车检修管理信息系统
     * <li>说明：用于关联查询节点前置节点主键和前置节点名称
     * <li>创建人： 何涛
     * <li>创建日期： 2014-11-25 上午11:48:00
     * <li>修改人:
     * <li>修改日期：
     * <li>修改内容：
     * <li>版权: Copyright (c) 2008 运达科技公司
     * @author 测控部检修系统项目组
     * @version 1.0
     */
    @Entity
    private static class UnionNode {
        
        /* idx主键 */
        @Id
        private String idx;
        
        /* 作业流程主键 */
        @Column(name = "PROCESS_IDX")
        private String processIDX;
        
        /* 上级作业节点主键 */
        @Column(name = "PARENT_IDX")
        private String parentIDX;
        
        /* 节点名称 */
        @Column(name = "Node_Name")
        private String nodeName;
        
        /* 节点编码 */
        @Column(name = "Node_Code")
        private String nodeCode;
        
        /* 节点描述 */
        @Column(name = "Node_Desc")
        private String nodeDesc;
        
        /* 工期 */
        @Column(name = "Rated_WorkMinutes")
        private Double ratedWorkMinutes;
        
        /* 顺序号 */
        @Column(name = "Seq_No")
        private Integer seqNo;
        
        /* 是否叶子节点,0:否；1：是 */
        @Column(name = "IS_LEAF")
        private Integer isLeaf;
        
        /* 前置节点主键 */
        @Column(name = "Pre_Node_IDX")
        private String preNodeIDX;
        
        /* 前置节点名称 */
        @Column(name = "Pre_Node_Name")
        private String preNodeName;
        
        /* 前置节点序号 */
        @Column(name = "PRE_NODE_SEQ_NO")
        private String preNodeSeqNo;
        
        /**
         * Default Constructor
         */
        public UnionNode() {
            super();
        }
        
        public String getIdx() {
            return idx;
        }
        
        public void setIdx(String idx) {
            this.idx = idx;
        }
        
        public Integer getIsLeaf() {
            return isLeaf;
        }
        
        public void setIsLeaf(Integer isLeaf) {
            this.isLeaf = isLeaf;
        }
        
        public String getNodeDesc() {
            return nodeDesc;
        }
        
        public void setNodeDesc(String nodeDesc) {
            this.nodeDesc = nodeDesc;
        }
        
        public String getNodeName() {
            return nodeName;
        }
        
        public void setNodeName(String nodeName) {
            this.nodeName = nodeName;
        }
        
        public String getNodeCode() {
            return nodeCode;
        }
        
        public void setNodeCode(String nodeCode) {
            this.nodeCode = nodeCode;
        }

        public String getParentIDX() {
            return parentIDX;
        }
        
        public void setParentIDX(String parentIDX) {
            this.parentIDX = parentIDX;
        }
        
        public String getPreNodeIDX() {
            return preNodeIDX;
        }
        
        public void setPreNodeIDX(String preNodeIDX) {
            this.preNodeIDX = preNodeIDX;
        }
        
        public String getPreNodeName() {
            return preNodeName;
        }
        
        public void setPreNodeName(String preNodeName) {
            this.preNodeName = preNodeName;
        }
        
        public String getPreNodeSeqNo() {
            return preNodeSeqNo;
        }
        
        public void setPreNodeSeqNo(String preNodeSeqNo) {
            this.preNodeSeqNo = preNodeSeqNo;
        }
        
        public String getProcessIDX() {
            return processIDX;
        }
        
        public void setProcessIDX(String processIDX) {
            this.processIDX = processIDX;
        }
        
        public Double getRatedWorkMinutes() {
            return ratedWorkMinutes;
        }
        
        public void setRatedWorkMinutes(Double ratedWorkMinutes) {
            this.ratedWorkMinutes = ratedWorkMinutes;
        }
        
        public Integer getSeqNo() {
            return seqNo;
        }
        
        public void setSeqNo(Integer seqNo) {
            this.seqNo = seqNo;
        }
        
    }
    
    /**
     * <li>说明：获取项目进度的数据，任务实体类集合
     * <li>创建人：何涛
     * <li>创建日期：2015-5-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 检修作业流程节点实体
     * @param outlineNumber 甘特图大纲字段:任务层次以及顺序
     * @param list 项目进度的数据，任务实体类集合
     * @param defautlStart 默认开始时间
     */
    public void getTasks(JobProcessNodeDef entity, String outlineNumber, List<Task> list, Date defautlStart) {
        Task task = new Task();
        task.setUID(outlineNumber);                             // UID
        task.setName(entity.getNodeName());                     // 任务名称
        task.setOutlineNumber(outlineNumber);                   // 大纲字段:任务层次以及顺序
        if (null != entity.getRatedWorkMinutes()) {
            task.setWorkDate(BigDecimal.valueOf(entity.getRatedWorkMinutes()));
        } else {
            task.setWorkDate(BigDecimal.valueOf(0));
        }
        task.setParentIdx(entity.getParentIDX());
        task.setIsLastLevel(entity.getIsLeaf());
        task.setNodeIDX(entity.getIdx());
        // 计划开始时间
        task.setStart(JobProcessDefManager.DATE_FMT_GANTT.format(defautlStart));
        // 计划结束时间
        Date finishdate = JobProcessDefManager.calculateFinishDate(defautlStart, entity.getRatedWorkMinutes());
        task.setFinish(JobProcessDefManager.DATE_FMT_GANTT.format(finishdate));
        list.add(task);
        if (JobProcessNodeDef.CONST_INT_IS_LEAF_NO == entity.getIsLeaf().intValue()) {
            List<JobProcessNodeDef> models = this.getModels(entity.getProcessIDX(), entity.getIdx());
            for (int i = 0; i < models.size(); i++) {
                String outlineNum = outlineNumber + "." + (i + 1);
                this.getTasks(models.get(i), outlineNum, list, defautlStart);
            }
        }
    }
    
    /**
     * <li>说明：获取项目进度的数据，任务实体类Map集合
     * <li>创建人：何涛
     * <li>创建日期：2015-5-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 检修作业流程节点实体
     * @param outlineNumber 甘特图大纲字段:任务层次以及顺序
     * @param map 项目进度的数据，任务实体类Map集合
     */
    public void getTasks(JobProcessNodeDef entity, String outlineNumber, Map<String, Task> map) {
        Task task = new Task();
        task.setUID(outlineNumber);                             // UID
        task.setName(entity.getNodeName());                     // 任务名称
        task.setOutlineNumber(outlineNumber);                   // 大纲字段:任务层次以及顺序
        if (null != entity.getRatedWorkMinutes()) {
            task.setWorkDate(BigDecimal.valueOf(entity.getRatedWorkMinutes()));
        }
        task.setParentIdx(entity.getParentIDX());
        task.setIsLastLevel(entity.getIsLeaf());
        task.setNodeIDX(entity.getIdx());
        map.put(entity.getIdx(), task);
        if (JobProcessNodeDef.CONST_INT_IS_LEAF_NO == entity.getIsLeaf().intValue()) {
            List<JobProcessNodeDef> models = this.getModels(entity.getProcessIDX(), entity.getIdx());
            for (int i = 0; i < models.size(); i++) {
                String outlineNum = outlineNumber + "." + (i + 1);
                this.getTasks(models.get(i), outlineNum, map);
            }
        }
    }
    
    /**
     * <li>说明：分页查询，因为unix系统下的oracle不支持WM_CONCAT函数，所以单独处理了对前置节点的查询
     * <li>创建人：何涛
     * <li>创建日期：2015-5-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询实体
     * @return Page 
     */
    @Override
    public Page<JobProcessNodeDef> findPageList(SearchEntity<JobProcessNodeDef> searchEntity) throws BusinessException {
        StringBuilder sb = new StringBuilder("From JobProcessNodeDef Where recordStatus = 0");
        JobProcessNodeDef entity = searchEntity.getEntity();
        // 查询条件 - 流程主键
        sb.append(" And processIDX = '").append(entity.getProcessIDX()).append("'");
        // 查询条件 - 上级主键
        sb.append(" And parentIDX = '").append(entity.getParentIDX()).append("'");
        // 排序
        if (null != searchEntity.getOrders()) {
            String order = HqlUtil.getOrderHql(searchEntity.getOrders());
            if (!order.contains("preNodeSeqNo")) {
                sb.append(order);
            }
        } else {
            sb.append(" Order By seqNo ASC");
        }
        String hql = sb.toString();
        String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
        Page<JobProcessNodeDef> page = this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
        List<JobProcessNodeDef> list = page.getList();
        if (null != list || list.size() > 0) {
            for (JobProcessNodeDef nodeDef : list) {
                nodeDef.setPreNodeSeqNo(this.jobProcessNodeRelDefManager.getPreNodeSeqNo(nodeDef.getIdx()));
            }
        }
        return page;
    }

    /**
     * <li>说明：通过拖拽的方式调整检修作业流程节点顺序
     * <li>创建人：何涛
     * <li>创建日期：2015-9-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 被拖拽的节点（idx主键）
     * @param oldParent 旧父节点（idx主键）
     * @param newParent 新父节点（idx主键）
     * @param index 索引值
     * @throws Exception 
     * @throws NumberFormatException 
     */
    @SuppressWarnings("unchecked")
    public void moveNode(String nodeIDX, String oldParent, String newParent, String index) throws NumberFormatException, Exception {
        // 获取被拖拽的节点实体
        JobProcessNodeDef entity = this.getModelById(nodeIDX);
        // 验证节点所在父节点是否已经在后台已经被更新，但页面没有刷新
        if (!oldParent.equals(entity.getParentIDX())) {
            throw new BusinessException("操作失败，请刷新后重试！");
        }
        
        // 被拖拽的节点旧的顺序号
        int oldSeqNo = entity.getSeqNo().intValue();
        // 被拖拽的节点新的顺序号
        int newSeqNo = Integer.parseInt(index) + 1;
        
        String hql = null;
        List<JobProcessNodeDef> list = null;
        // 同级下的节点进行拖拽
        if (oldParent.equals(newParent)) {
            // 根据被拖拽后节点的新旧顺序号计算偏移量
            int offset = 0;
            if (newSeqNo < oldSeqNo) {
                // 上移
                offset++;
            } else {
                // 下移
                offset--;
            }
            // 获取被拖拽的节点原顺序号与新顺序号之间的节点实体集合
            int startIndex = newSeqNo < oldSeqNo ? newSeqNo : oldSeqNo;
            int endIndex =  newSeqNo < oldSeqNo ? oldSeqNo : newSeqNo;
            hql = "From JobProcessNodeDef Where recordStatus = 0 And seqNo >= ? And seqNo <= ? And processIDX = ? And parentIDX = ?";
            list = this.daoUtils.find(hql, new Object[]{startIndex, endIndex, entity.getProcessIDX(), newParent});
            // 更新顺序号
            for (JobProcessNodeDef nodeDef : list) {
                if (nodeDef.getIdx().equals(nodeIDX)) {
                    nodeDef.setSeqNo(newSeqNo);
                    continue;
                }
                nodeDef.setSeqNo(nodeDef.getSeqNo() + offset);
            }
            this.saveOrUpdate(list);
        // 不同级下的节点进行拖拽
        } else {
            // 插入到新的父节点下后，更新顺序号
            hql = "From JobProcessNodeDef Where recordStatus = 0 And seqNo >= ? And processIDX = ? And parentIDX = ?";
            list = this.daoUtils.find(hql, new Object[]{newSeqNo, entity.getProcessIDX(), newParent});
            if (list.size() > 0) {
                for (JobProcessNodeDef nodeDef : list) {
                    nodeDef.setSeqNo(nodeDef.getSeqNo() + 1);
                }
                this.saveOrUpdate(list);
            }
            
            // 更新父节点为新的父节点（idx主键）
            entity.setParentIDX(newParent);
            // 更新顺序号
            entity.setSeqNo(newSeqNo);
            this.saveOrUpdate(entity);
            // 重排旧父节点下的节点顺序
            list = this.getModels(entity.getProcessIDX(), oldParent);
            if (null != list && list.size() > 0) {
                super.updateSort(list);
            }
            
            // 不同层次间的拖拽完成后，清空可能存在的节点前后置关系
            this.jobProcessNodeRelDefManager.logicDeleteByMoveNode(nodeIDX);
        }
    }
    
    /**
     * <li>说明：流程节点名称选择控件
     * <li>创建人：张迪
     * <li>创建日期：2016-7-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param processIdx 流程idx
     * @param parentIDX 父结点id
     * @param isChecked 有无勾选
     * @return List<HashMap> 返回下拉树列表 
     */
    @SuppressWarnings("unchecked")
    public List<HashMap> getBaseComboTree(HttpServletRequest req) throws Exception{
        String parentIDX = req.getParameter("parentIDX");// 上级节点IDX
        parentIDX = (StringUtil.isNullOrBlank(parentIDX) || parentIDX.startsWith("ROOT_0")) ? "ROOT_0" : parentIDX;
        String queryMaps = req.getParameter("queryParams");
        Map queryParamsMap = JSONUtil.read(queryMaps, Map.class);
        if (!queryParamsMap.isEmpty()) {
            String processIdx = String.valueOf(queryParamsMap.get("processIdx"));        
//            String isChecked = StringUtil.nvlTrim(req.getParameter("isChecked"), null);
            return getNodeTree(processIdx, parentIDX);
        }
        return null;
    }
    
    /**
     * <li>说明：查询流程节点下拉树
     * <li>创建人：张迪
     * <li>创建日期：2016-7-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param processIdx 流程idx
     * @param parentIDX 父结点id
     * @param isChecked 有无勾选
     * @return List<HashMap> 返回下拉树列表 
     */
    @SuppressWarnings("unchecked")
    public List<HashMap> getNodeTree(String processIdx, String parentIDX){
        String idx ="";
        if ("ROOT_0".equals(parentIDX)) {
            idx = parentIDX;
        } else {
            JobProcessNodeDef parentObj = this.getModelById(parentIDX); // 获取父类对象
            idx = parentObj.getIdx();
        }
        String hql = "from JobProcessNodeDef where parentIDX = ? And processIDX = ? And recordStatus = " + Constants.NO_DELETE + " order by seqNo";
        List<JobProcessNodeDef> list = (List<JobProcessNodeDef>) this.daoUtils.find(hql, new Object[] { idx, processIdx });
        List<HashMap> children = new ArrayList<HashMap>();
        for (JobProcessNodeDef t : list) {
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            nodeMap.put("id", t.getIdx()); // 节点idx主键
            nodeMap.put("text", t.getNodeName()); // 树节点显示名称
            nodeMap.put("leaf", t.getIsLeaf() == 0 ? false : true); // 是否是叶子节点 0:否；1：是
            nodeMap.put("nodeName", t.getNodeName()); // 节点名称
            nodeMap.put("nodeIdx", t.getIdx()); // 节点idx
            nodeMap.put("seqNo", t.getSeqNo()); // 顺序号
//            nodeMap.put("editable ", true); //    
            children.add(nodeMap);
        }
        return children;
 
    }

}
