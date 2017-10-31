package com.yunda.jwpt.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jwpt.datasyncentertable.entity.JwptDataSynchronizationCenterTable;
import com.yunda.util.GenericsUtils;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 总公司机务平台数据同步-业务更新接口抽象实现类
 * <li>创建人：何涛
 * <li>创建日期：2016-06-01
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @param <T> 检修业务实体泛型
 * @param <E> 数据同步实体泛型
 * @version 1.0
 */
public abstract class AbstractBaseBusinessDataUpdateJob<T, E extends IAdaptable> implements IBaseBusinessDataUpdateJob {
    
    /** 检修业务实体类Class对象 */
    protected Class<T> businessEntityClass;
    
    /** 数据同步临时实体类Class对象 */
    protected Class<E> syncTempEntityClass;
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：构造方法，初始化泛型class对象
     * <li>创建人：何涛
     * <li>创建日期：2016-6-1
     * <li>修改人： 
     * <li>修改日期：
     */
    @SuppressWarnings("unchecked")
    public AbstractBaseBusinessDataUpdateJob(){
        businessEntityClass = GenericsUtils.getSuperClassGenricType(this.getClass(), 0);
        syncTempEntityClass = GenericsUtils.getSuperClassGenricType(this.getClass(), 1);
    }
    
    /**
     * <li>说明：获取检修业务实体业务管理器
     * <li>创建人：何涛
     * <li>创建日期：2016-6-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 检修业务实体管理器
     */
    protected abstract JXBaseManager<T, T> getTManager();
    
    /**
     * <li>说明：获取数据同步实体业务管理器，本地临时表
     * <li>创建人：何涛
     * <li>创建日期：2016-6-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 数据同步实体管理器
     */
    protected abstract JXBaseManager<E, E> getEManager();
    
    /**
     * <li>说明：同步机车检修作业计划业务数据到本地数据同步临时表
     * <li>创建人：何涛
     * <li>创建日期：2014-12-08
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param dsctList 针对某一业务表的数据操作记录集合，集合可能包含多个业务记录的数据操作记录
     * @return List 数据操作记录集合
     */
    @Override
    public List<JwptDataSynchronizationCenterTable> updateDataByBusiness(List<JwptDataSynchronizationCenterTable> dsctList) {
        Map<String, List<JwptDataSynchronizationCenterTable>> map = new HashMap<String, List<JwptDataSynchronizationCenterTable>>();
        List<JwptDataSynchronizationCenterTable> list = null;
        // 将待更新的数据按“业务表主键进行分类”，每一个业务主键决定一次临时表的数据操作
        for (JwptDataSynchronizationCenterTable t : dsctList) {
            String businessIDX = t.getBusinessIDX();
            list = map.get(businessIDX);
            if (null == list) {
                list = new ArrayList<JwptDataSynchronizationCenterTable>();
                map.put(businessIDX, list);
            }
            list.add(t);
        }
        Set<Entry<String, List<JwptDataSynchronizationCenterTable>>> set = map.entrySet();
        // 声明操作成功后的返回对象集合
        List<JwptDataSynchronizationCenterTable> successList = new ArrayList<JwptDataSynchronizationCenterTable>();
        // 遍历待操作数据记录，完成业务数据到系统临时表的数据同步
        for (Iterator<Entry<String, List<JwptDataSynchronizationCenterTable>>> i = set.iterator(); i.hasNext();) {
            Entry<String, List<JwptDataSynchronizationCenterTable>> entry = i.next();
            String businessIDX = entry.getKey();        // 业务主键
            list = entry.getValue();                    // 该业务主键管理的数据操作
            // 根据具体的某一个业务（主键）产生的数据操作记录集合，验证最终读写系统业务临时数据表的操作类型
            int operateType = getOperateType(list);
            try {
                switch (operateType) {
                    case IBaseBusinessDataUpdateJob.INSERT:
                        this.insert(businessIDX);
                        break;
                    case IBaseBusinessDataUpdateJob.UPDATE:
                        this.update(businessIDX);
                        break;
                    case IBaseBusinessDataUpdateJob.DELETE:
                        this.delete(businessIDX);
                        break;
                    default:
                        break;
                }
                // 操作成功后的返回结果集
                successList.addAll(list);
            } catch (Exception e) {
                // 如果某一业务数据同步异常，打印日志信息便于查错，但不影响后续记录的同步操作（continue）
                logger.error("机务平台数据同步异常：[table:" + dsctList.get(0).getBusinessTableName() + ", businessIDX:" + businessIDX + "]", e);
                continue;
            }
        }
        return successList;
    }
    

    /**
     * <li>说明：根据具体的某一个业务（主键）产生的数据操作记录集合，验证最终读写系统业务临时数据表的操作类型
     * <li>创建人：何涛
     * <li>创建日期：2016-05-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param list 具体的某一个业务（主键）产生的数据操作记录集合
     * @return 操作类型，-1：无效操作，3：删除操作，5：更新操作，9：插入操作
     */
    private int getOperateType(List<JwptDataSynchronizationCenterTable> list) {
        if (null == list || 0 >= list.size()) {
            return -1;
        }
        // 如果操作记录集合只有一条数据，则返回该条数据的操作类型
        if (1 == list.size()) {
            return Integer.parseInt(list.get(0).getOperat());
        }
        // 如果操作记录集合有多条数据，则需判断这些记录最终的操作类型
        Collections.sort(list);
        JwptDataSynchronizationCenterTable temp0 = list.get(0);
        JwptDataSynchronizationCenterTable temp1 = list.get(list.size() - 1);
        // 验证操作记录集合是否以“插入”开始
        if (IBaseBusinessDataUpdateJob.INSERT == Integer.parseInt(temp1.getOperat())) {
            // 如果刚开始有插入操作，结尾又是删除操作，则此系列操作为无效操作
            if (IBaseBusinessDataUpdateJob.DELETE == Integer.parseInt(temp0.getOperat())) {
                return -1;
            // 否则则此系列操作为插入操作
            } else {
                return IBaseBusinessDataUpdateJob.INSERT;
            }
        } 
        // 验证操作记录集合是否以“更新”开始
        if (IBaseBusinessDataUpdateJob.UPDATE == Integer.parseInt(temp1.getOperat())) {
            // 如果刚开始有更新操作，结尾又是删除操作，则此系列操作为删除操作
            if (IBaseBusinessDataUpdateJob.DELETE == Integer.parseInt(temp0.getOperat())) {
                return IBaseBusinessDataUpdateJob.DELETE;
            } else {
                return IBaseBusinessDataUpdateJob.UPDATE;
            }
        }
        return IBaseBusinessDataUpdateJob.DELETE;
    }

    /**
     * <li>说明：从临时表中删除同步数据
     * <li>创建人：何涛
     * <li>创建日期：2016-05-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param businessIDX 业务表主键idx
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    @SuppressWarnings("unchecked")
    private void delete(String businessIDX) throws BusinessException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        E entity = this.getEManager().getModelById(businessIDX);
        // 验证临时表中是否还存在未同步（未删除）的数据记录
        if (null == entity) {
            // 如果之前的记录已经被同步，那么删除操作在临时表中应增加一条操作记录
            entity = syncTempEntityClass.newInstance();
            entity.adaptFrom(this.getTManager().getModelById(businessIDX), DELETE);
            this.getEManager().insert(entity);
        } else if (UPDATE == entity.getOperateType()) {
            // 如果之前的记录还未被同步，并且该记录是一条更新操作，那么应将之前的更新操作记录修改为一条类型为删除的操作记录
            entity.setOperateType(DELETE);
            this.getEManager().saveOrUpdate(entity);
        } else if (INSERT == entity.getOperateType()) {
            // 如果之前的记录还未被同步，并且该记录是一条插入操作，那么之前的操作为一次无效操作，应直接删除，无需同步
            this.getEManager().getDaoUtils().remove(entity);
        }
    }

    /**
     * <li>说明：插入同步数据到临时表
     * <li>创建人：何涛
     * <li>创建日期：2016-05-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param businessIDX 业务表主键idx
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    @SuppressWarnings("unchecked")
    private void insert(String businessIDX) throws BusinessException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        T obj = this.getTManager().getModelById(businessIDX);
        E entity = this.getEManager().getModelById(businessIDX);
        if (null == entity) {
            entity = syncTempEntityClass.newInstance();
            entity.adaptFrom(obj, INSERT);
            this.getEManager().insert(entity);
        } else {
            entity.adaptFrom(obj, INSERT);
            this.getEManager().saveOrUpdate(entity);
        }
    }

    /**
     * <li>说明：更新同步数据到临时表
     * <li>创建人：何涛
     * <li>创建日期：2016-05-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param businessIDX 业务表主键idx
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    @SuppressWarnings("unchecked")
    private void update(String businessIDX) throws BusinessException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        T obj = this.getTManager().getModelById(businessIDX);
        E entity = this.getEManager().getModelById(businessIDX);
        // 验证临时表中是否还存在未同步（未删除）的数据记录
        if (null == entity) {
            // 如果之前的记录已经被同步，那么更新操作在临时表中应该是插入操作
            entity = syncTempEntityClass.newInstance();
            entity.adaptFrom(obj, UPDATE);
            this.getEManager().insert(entity);
        } else {
            // 如果之前的记录还未被同步，那么更新操作在临时表中应该是更新操作
            entity.adaptFrom(obj, UPDATE);
            this.getEManager().saveOrUpdate(entity);
        }
    }
    
}
