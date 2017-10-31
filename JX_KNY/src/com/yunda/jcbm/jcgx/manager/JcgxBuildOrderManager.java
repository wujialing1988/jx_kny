package com.yunda.jcbm.jcgx.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.order.AbstractOrderManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.jcbm.jcgx.entity.JcgxBuild;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JcgxBuild业务类，机车构型顺序号排序业务类
 * <li>创建人：张迪
 * <li>创建日期：2016-9-1
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value = "jcgxBuildOrderManager")
public class JcgxBuildOrderManager extends AbstractOrderManager<JcgxBuild, JcgxBuild> {
    
    /**
     * <li>说明：判断顺序号是否超出范围
     * <li>创建人：张迪
     * <li>创建日期：2016-8-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity  机车构型实体类
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveOrUpdateSeqNo(JcgxBuild entity) throws BusinessException ,NoSuchFieldException {
        // 在选择节点前插入节点的功能
        try {
            int seqNo = super.getSeqNo(entity);
            if (0 < seqNo) {
                int count = count(entity);
                if (seqNo > count + 1) {
                    throw new BusinessException("顺序号[" + seqNo + "]超出最大范围" + count);
                }
                // 在指定顺序号处插入记录时，同步更新排序范围内指定顺序号之后的所有记录（顺序号依次加一） 
               updateAfterModels(entity);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        this.saveOrUpdate(entity);
    }
    /**
     * <li>说明：保存之后更新同级其它节点的顺序号
     * <li>创建人：张迪
     * <li>创建日期：2016-8-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车构型实体类
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateAfterModels(JcgxBuild t) throws BusinessException, NoSuchFieldException {
        // 获取该作业节点之后的所有同级作业节点
        List<JcgxBuild> list = null;
        try {
            list = findAllBySN(t);
            if (null == list || 0 >= list.size()) {
                return;
            }
            for (JcgxBuild entity : list) {
                this.setSeqNo(entity, this.getSeqNo(entity) + 1);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        super.saveOrUpdate(list);
    }
    
    /**
     * <li>说明：查询插入顺序号之前的同一级的所有节点
     * <li>创建人：张迪
     * <li>创建日期：2016-8-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车构型实体类
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    public List<JcgxBuild> findAllBySN(JcgxBuild t) throws Exception {
        String hql ;
        hql =  " from  JcgxBuild Where recordStatus = 0 And " + seqNoFN + " >= ? And SYCX = ? And fjdID = ? ";
        return this.daoUtils.find(hql, new Object[]{this.getSeqNo(t),t.getSycx(), t.getFjdID()});
    }
    
    /**
     * <li>说明：保存前插入顺序号
     * <li>创建人：张迪
     * <li>创建日期：2016-8-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车构型实体类
     */
    public void insertSeqNo(JcgxBuild entity) {
        try {
            // （新增）操作
            // 获取数据表的最大排序号（即：记录总数）
            int maxSeqNo = count(entity);
            if (0 == maxSeqNo) {
                // 如果当前数据表没有记录，则将排序号设置为：1
                super.setSeqNo(entity, 1);
            } else {
                // 如果排序号为0,设置排序号为：最大排序号 + 1
                if (super.getSeqNo(entity) <= 0) {
                    super.setSeqNo(entity, maxSeqNo + 1);
                }
            }
        } catch (Exception e) {
            throw new BusinessException( e.getMessage() );
        }
    }
    /**
     * <li>说明: 父类方法判断了idx,重写保存方法
     * <li>创建人：张迪
     * <li>创建日期：2016-8-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t  机车构型实体类
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @Override
    public void saveOrUpdate(JcgxBuild t) throws BusinessException, NoSuchFieldException {
        t = EntityUtil.setSysinfo(t);
        //设置逻辑删除字段状态为未删除
        t = EntityUtil.setNoDelete(t);
        this.daoUtils.getHibernateTemplate().saveOrUpdate(t);
    }   
    
    /**
     * <li>说明：统计该父节点下所有子节点数量
     * <li>创建人：张迪
     * <li>创建日期：2016-8-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车构型实体类
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @Override
    public int count(JcgxBuild entity) {
        StringBuilder sb = new StringBuilder(" from JcgxBuild WHERE  recordStatus=0 and SYCX = '");
        sb.append(entity.getSycx()).append("' ");
        //如果该实体类存在逻辑删除字段，过滤逻辑删除记录
        if(null != entity.getFjdID()){
           sb.append(" and fjdID = '").append(entity.getFjdID()).append("' ");
        }        
         return this.daoUtils.getCount(sb.toString());
    }

    /**
     * <li>说明：获取同车型同级的构型节点
     * <li>创建人：张迪
     * <li>创建日期：2016-09-01
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param processIDX 作业流程idx主键
     * @param parentIDX 父节点主键
     * @return List<JobProcessNodeDef> 作业流程节点实体集
     */
    @SuppressWarnings("unchecked")
    public List<JcgxBuild> getModels(String sycx, String fjdID) {
        String hql = "From JcgxBuild Where recordStatus = 0 And sycx = ? And fjdID = ? Order By seqNo";
        return this.daoUtils.find(hql, new Object[] { sycx, fjdID });
    }
    /**
     * <li>说明：写该方法 获取排序范围内的同类型的所有记录(对于同一表中存在多个排序序列的情况)
     * <li>创建人：张迪
     * <li>创建日期：2016-9-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容： 
     * @param   entity   实体对象
     * @return  List<JcgxBuild> 结果集
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<JcgxBuild> findAll(JcgxBuild entity) {
        String hql = "From JcgxBuild Where recordStatus = 0 And sycx = ? And fjdID = ? Order By seqNo";
        return this.daoUtils.find(hql, new Object[] { entity.getSycx(), entity.getFjdID()});
    }
    
    /**
     * <li>说明：拖动节点修改顺序
     * <li>创建人：张迪
     * <li>创建日期：2016-9-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 构型实体类
     * @param oldParent 原父节点
     * @param newParent  新父节点
     * @param index 调整索引
     * @throws NumberFormatException
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void saveMoveNode(JcgxBuild entity, String oldParent, String newParent, String index) throws NumberFormatException, Exception {       
        // 被拖拽的节点旧的顺序号
        int oldSeqNo = entity.getSeqNo().intValue();
        // 被拖拽的节点新的顺序号
        int newSeqNo = Integer.parseInt(index) + 1;
        
        String hql = null;
        List<JcgxBuild> list = null;
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
            hql = "From JobProcessNodeDef Where recordStatus = 0 And seqNo >= ? And seqNo <= ? And sycx = ? And fjdID = ?";
            list = this.daoUtils.find(hql, new Object[]{startIndex, endIndex, entity.getSycx(), newParent});
            // 更新顺序号
            for (JcgxBuild buildDef : list) {
                if (buildDef.getCoID().equals(entity.getCoID())) {
                    buildDef.setSeqNo(newSeqNo);
                    continue;
                }
                buildDef.setSeqNo(buildDef.getSeqNo() + offset);
            }
            this.saveOrUpdate(list);
        // 不同级下的节点进行拖拽
        } else {
            // 插入到新的父节点下后，更新顺序号
            hql = "From JcgxBuild Where recordStatus = 0 And seqNo >= ? And sycx = ? And fjdID = ?";
            list = this.daoUtils.find(hql, new Object[]{newSeqNo, entity.getSycx(), newParent});
            if (list.size() > 0) {
                for (JcgxBuild nodeDef : list) {
                    nodeDef.setSeqNo(nodeDef.getSeqNo() + 1);
                }
                super.saveOrUpdate(list);
            }
            
            // 更新父节点为新的父节点（idx主键）
            entity.setFjdID(newParent);
            // 更新顺序号
            entity.setSeqNo(newSeqNo);
            this.saveOrUpdate(entity);
            // 重排旧父节点下的节点顺序
            list = this.getModels(entity.getSycx(), oldParent);
            if (null != list && list.size() > 0) {
                super.updateSort(list);
            }
        }
    }
    
    /**
     * <li>说明：置顶(重写该方法)
     * <li>创建人：张迪
     * <li>创建日期：2016-9-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param coID 主键
     * @throws Exception
     */
    @Override
    @SuppressWarnings("unchecked")
    public void updateMoveTop(String coID) throws Exception {
        JcgxBuild entity = this.getModelById(coID);
        // 获取该对象的排序号
        int seqNo = this.getSeqNo(entity);
        String hql ;
        //如果该实体类存在逻辑删除字段，过滤逻辑删除记录
        if(EntityUtil.contains(entity.getClass(), EntityUtil.RECORD_STATUS)){
            //获取被【置顶】记录被置顶前，在其前的所有记录
            hql = "  from JcgxBuild Where recordStatus = 0 And " + seqNoFN + " < ? And sycx = ? And fjdID = ? ";
        }else{
            //获取被【置顶】记录被置顶前，在其前的所有记录
            hql =  "from JcgxBuild Where " + seqNoFN + " < ? And sycx = ? And fjdID = ? ";
        }
        
        List<JcgxBuild> list = this.daoUtils.find(hql, new Object[]{seqNo,entity.getSycx(), entity.getFjdID()});
        // 设置被【置顶】记录的排序号为1
        this.setSeqNo(entity, 1);
        List<JcgxBuild> entityList = new ArrayList<JcgxBuild>();
        entityList.add(entity);
        // 设置其后的所有记录的排序后一次加一
        for (JcgxBuild t : list) {
            this.setSeqNo(t, this.getSeqNo(t) + 1);
            entityList.add(t);
        }
        this.saveOrUpdate(entityList);
 
    }
    /**
     * <li>说明：上移(重写该方法)
     * <li>创建人：张迪
     * <li>创建日期：2016-9-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
       * @param coID 主键
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateMoveUp(String coID) throws Exception {
        JcgxBuild entity = this.getModelById(coID);
        // 获取该对象的排序号
        int seqNo = this.getSeqNo(entity);
        String hql ;
        //如果该实体类存在逻辑删除字段，过滤逻辑删除记录
        if(EntityUtil.contains(entity.getClass(), EntityUtil.RECORD_STATUS)){
            //如果记录总数为1或者被【上移】记录以及处于置顶状态，则返回
            hql = " from JcgxBuild  Where recordStatus = 0 And " + seqNoFN + " = ? And sycx = ? And fjdID = ? ";
        }else{
            //如果记录总数为1或者被【上移】记录以及处于置顶状态，则返回
            hql = " from JcgxBuild  Where " + seqNoFN + " = ? And sycx = ? And fjdID = ? ";
        }
        
        // 获取被【上移】记录被上移移前，紧随其前的记录
        JcgxBuild nextEntity = (JcgxBuild)this.daoUtils.findSingle(hql, new Object[]{seqNo - 1,entity.getSycx(), entity.getFjdID()});
        List<JcgxBuild> entityList = new ArrayList<JcgxBuild>(2);
        // 设置被【上移】记录的排序号-1
        this.setSeqNo(entity, seqNo - 1);
        entityList.add(entity);
        // 设置被【上移】记录前的记录的排序号+1
        this.setSeqNo(nextEntity, this.getSeqNo(nextEntity) + 1);
        entityList.add(nextEntity);
        this.saveOrUpdate(entityList);
    }
    /**
     * <li>说明：置底(需要重写该方法)
     * * <li>创建人：张迪
     * <li>创建日期：2016-9-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
       * @param coID 主键
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateMoveBottom(String coID) throws Exception {
        JcgxBuild entity = this.getModelById(coID);
        int count = this.count(entity);    
        // 获取该对象的排序号
        int seqNo = this.getSeqNo(entity);
        // 获取被【置底】记录被置底前，在其后的所有记录
        StringBuilder sb = new StringBuilder();
        //如果该实体类存在逻辑删除字段，过滤逻辑删除记录
        if(EntityUtil.contains(entity.getClass(), EntityUtil.RECORD_STATUS)){
            sb.append(Constants.FROM).append(" JcgxBuild Where recordStatus = 0 And ");
        }else sb.append(Constants.FROM).append(" JcgxBuild Where 1=1 And ");
        
        sb.append(seqNoFN).append(" > ? And sycx = ? And fjdID = ? ");
        String hql =  sb.toString();
        List<JcgxBuild> list = this.daoUtils.find(hql, new Object[]{seqNo,entity.getSycx(), entity.getFjdID()});
        // 设置被【置底】记录的排序号为当前记录总数
        this.setSeqNo(entity, count);
        List<JcgxBuild> entityList = new ArrayList<JcgxBuild>();
        entityList.add(entity);
        // 设置其后的所有记录的排序后依次减一
        for (JcgxBuild t : list) {
            this.setSeqNo(t, this.getSeqNo(t) - 1);
            entityList.add(t);
        }
        this.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：下移(需要重写该方法)
     * <li>创建人：张迪
     * <li>创建日期：2016-9-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
      * @param coID 主键
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateMoveDown(String idx) throws Exception {
        JcgxBuild entity = this.getModelById(idx);
        Class clazz = entity.getClass();
        // 获取该对象的排序号
        int seqNo = this.getSeqNo(entity);
        String hql ;
        //如果该实体类存在逻辑删除字段，过滤逻辑删除记录
        if(EntityUtil.contains(entity.getClass(), EntityUtil.RECORD_STATUS)){
            //如果记录总数为1或者被【下移】记录以及处于置底状态，则返回
            hql = Constants.FROM + clazz.getSimpleName() + " Where recordStatus = 0 And " + seqNoFN + " = ?";
        }else{
            // 如果记录总数为1或者被【下移】记录以及处于置底状态，则返回
            hql = Constants.FROM + clazz.getSimpleName() + " Where " + seqNoFN + " = ?";
        }
        hql += " And sycx = ? And fjdID = ? "; 
        // 获取被【下移】记录被下移前，紧随其后的记录
        JcgxBuild nextEntity = (JcgxBuild)this.daoUtils.findSingle(hql, new Object[]{seqNo + 1,entity.getSycx(), entity.getFjdID()});
        List<JcgxBuild> entityList = new ArrayList<JcgxBuild>(2);
        // 设置被【下移】记录的排序号+1
        this.setSeqNo(entity, seqNo + 1);
        entityList.add(entity);
        // 设置被【下移】记录后的记录的排序号-1
        this.setSeqNo(nextEntity, this.getSeqNo(nextEntity) - 1);
        entityList.add(nextEntity);
        this.saveOrUpdate(entityList);
    }
    
}
