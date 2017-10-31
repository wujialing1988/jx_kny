/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：林欢
 * <li>创建日期：2016-5-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */

package com.yunda.jwpt.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.jwpt.datasyncentertable.entity.JwptDataSynchronizationCenterTable;
import com.yunda.jwpt.utils.BaseBusinessDataUpdateJobUtils;
import com.yunda.util.BeanUtils;
import com.yunda.util.DaoUtils;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 业务更新接口基础实现类
 * <li>创建人：林欢
 * <li>创建日期：2016-5-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
abstract public class BaseBusinessDataUpdateJob implements IBaseBusinessDataUpdateJob{
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    protected Map<String, Object> businessObjMap = new HashMap<String, Object>();
    
    /** recordStatus 字符串*/
    public static final String RECORD_STATUS = "recordStatus";
    /** operateType 字符串*/
    public static final String OPERATE_TYPE = "operateType";
    /** businessCenterObj 字符串*/
    public static final String BUSINESS_CENTER_OBJ = "businessCenterObj";
    /** businessObj 字符串*/
    public static final String BUSINESS_OBJ = "businessObj";
    
    /**
     * <li>方法说明：更新对应业务的数据
     * <li>创建人：林欢
     * <li>创建时间：2016-5-14 
     * <li>修改人：
     * <li>修改内容：
     * <LI>重要业务逻辑:
     * 当官方中间表中，当前数据为9=insert:【1】当系统中间表为5=update的时候，依旧是新增，数据为更新的数据
     *                               【2】当系统中间表为3=delete的时候，删除官方中间表当前数据
     *                      5=update:【1】当系统中间表为3=delete的时候，修改状态为删除状态（3=删除）
     * TODO 未处理当系统中间表JwptDataSynchronizationCenterTable 同一个业务ID下，既有insert又有delete的情况
     * @param dsctList 系统中间表list数据
     * @return 成功更新业务后，对应的系统中间表对象，方便统一删除
     * @throws NoSuchFieldException 
     */
    @SuppressWarnings("unchecked")
    public List<JwptDataSynchronizationCenterTable> updateDataByBusiness(List<JwptDataSynchronizationCenterTable> dsctList){
        
//      准备一个执行业务操作后返回系统中间表表DataSynchronizationCenterTable 主键IDX的list
        List<JwptDataSynchronizationCenterTable> returnIDXList = new ArrayList<JwptDataSynchronizationCenterTable>();
        
        //遍历传递过来的，机车出入段台账相关信息
        for (JwptDataSynchronizationCenterTable jdsct : dsctList) {
            
//          设置业务字段的值
            Map<String, Object> businessMap = getBusinessObjMap(jdsct);
            
            //官方中间表对象
            Object businessCenterObj = businessMap.get(BUSINESS_CENTER_OBJ);
            if (businessCenterObj == null) {
                throw new BusinessException("官方中间表对象不能为空！");
            }
            //系统业务表对象
            Object businessObj = businessMap.get(BUSINESS_OBJ);
            if (businessObj == null) {
                throw new BusinessException("系统业务表对象不能为空！");
            }
            
            //系统中间表本条数据操作 3删除 5更新 9新增
            String operat = jdsct.getOperat();
            
            JXBaseManager tZbglZbdzdtManager = getBusinessCenterManager();
            if (businessCenterObj == null) {
                throw new BusinessException("官方中间表manager对象不能为空！");
            }
            DaoUtils daoUtils = tZbglZbdzdtManager.getDaoUtils();
            
            try {
//          当前官方中间表中的操作类型 3删除 5更新 9新增
                if (EntityUtil.contains(businessCenterObj.getClass(), OPERATE_TYPE)) {
                    Integer operateType = (Integer) Integer.valueOf(BeanUtils.forceGetProperty(businessCenterObj, OPERATE_TYPE).toString());
                    
                    if (operateType == IBaseBusinessDataUpdateJob.INSERT) {
                        if (IBaseBusinessDataUpdateJob.UPDATE == Integer.valueOf(operat)) {
                            
//                          设置操作
                            BaseBusinessDataUpdateJobUtils.instence.doSetCommonFiledValue2(businessCenterObj, null, IBaseBusinessDataUpdateJob.INSERT);//记录类型（3删除，5修改，9原始）
                            
                        }else if (Integer.valueOf(operat) == IBaseBusinessDataUpdateJob.DELETE) {
                            //由于该条数据既有9=新增，又有3=删除，在官方中间表中没有存在意义，删除
                            
                            daoUtils.remove(businessCenterObj);
                            
                            //如果需要删除从表，调用方法，让子类自己选择删除，该方法非抽象，子类自己选择是否覆写
                            deleteBusinessCenterFromTable();
                            
                            returnIDXList.add(jdsct);
                            continue;
                        }
                    }else if (operateType == IBaseBusinessDataUpdateJob.UPDATE) {
                        if (Integer.valueOf(operat) == IBaseBusinessDataUpdateJob.DELETE) {
                            //update-delete，修改官方中间表状态为删除
                            
//                          设置记录状态,操作
                            BaseBusinessDataUpdateJobUtils.instence.doSetCommonFiledValue2(businessCenterObj, 1, IBaseBusinessDataUpdateJob.DELETE);//记录类型（3删除，5修改，9原始）
                        }
                    }
//                    else {
                        //delete状态跳过，不会出现删除后更新，或者新增情况
                        //由于数据处理过，删除即便是都是最后一个出现
//                        returnIDXList.add(jdsct);
//                        continue;
//                    }
                }
                
                //设置业务字段的值
                setbusinessCenterObjProperty(businessCenterObj);
                
//              设置公共字段值(创建人，创建时间，修改人，修改时间)
                BaseBusinessDataUpdateJobUtils.instence.doSetCommonFiledValue(businessCenterObj);
                
                daoUtils.getHibernateTemplate().saveOrUpdate(businessCenterObj);
                
//              //如果需要持久化数据到从表，调用方法，让子类自己选择实现，该方法非抽象，子类自己选择是否覆写
                saveBusinessCenterFromTable();
            } catch (Exception e) {
                logger.error("机务平台数据同步框架BaseBusinessDataUpdateJob出错！业务表名称:"+jdsct.getBusinessTableName()+",业务表主键:"+jdsct.getBusinessIDX()+",当次操作:"+jdsct.getOperat()+",数据修改时间:"+jdsct.getUpdateTime()+"错误信息："+e);
                e.printStackTrace();
                continue;
            }
            
            //记录当前操作成功的，系统中间表IDX，方便做统一删除处理
            returnIDXList.add(jdsct);
            
            cleanBusinessObjMap();
        }
        
        return returnIDXList;
    }
    
    /**
     * <li>方法说明：清空业务类缓存map
     * <li>创建人：林欢
     * <li>创建时间：2016-5-14 
     * <li>修改人：
     * <li>修改内容：
     */
    private void cleanBusinessObjMap(){
        businessObjMap.clear();
    }
    
    /**
     * <li>方法说明：获取对应的业务对象(如果businessObjMap中没有官方的中间表对象，新增时候，需要赋值recordStatus，如果有operateType也需要赋值)
     * <li>创建人：林欢
     * <li>创建时间：2016-5-14 
     * <li>修改人：
     * <li>修改内容：初始化的部分状态和操作需要子类实现
     * @param jdsct 系统中间表对象
     * @return 返回需要的业务对象 key=businessCenterObj【官方中间表，不能为空，一定要有个对象，new一个都行】，【businessObj系统业务表】 value=对应的实体对象
     */
    abstract protected Map<String, Object> getBusinessObjMap(JwptDataSynchronizationCenterTable jdsct);
    
    /**
     * <li>方法说明：获取官方中间表manager对象
     * <li>创建人：林欢
     * <li>创建时间：2016-5-14 
     * <li>修改人：
     * <li>修改内容：
     * @return 返回官方中间表业务manager对象 
     */
    abstract protected JXBaseManager getBusinessCenterManager();
    
    /**
     * <li>方法说明：设置业务类自有属性(由于官方中间表的id名称不一致，需要在此方法手动赋值)
     * <li>创建人：林欢
     * <li>创建时间：2016-5-14 
     * <li>修改人：
     * <li>修改内容：
     * @param object 官方中间表业务对象
     */
    abstract protected void setbusinessCenterObjProperty(Object object);
    
    /**
     * <li>方法说明：当需要持久化从表的时候使用，该方法内单独处理更新官方中间表从表信息
     * <li>创建人：林欢
     * <li>创建时间：2016-5-14 
     * <li>修改人：
     * <li>修改内容：
     * @throws NoSuchFieldException 
     */
    protected void saveBusinessCenterFromTable() throws NoSuchFieldException{
        
    }
    
    /**
     * <li>方法说明：当需要删除从表的时候使用，该方法内单独处理更新官方中间表从表信息
     * <li>创建人：林欢
     * <li>创建时间：2016-5-14 
     * <li>修改人：
     * <li>修改内容：
     */
    protected void deleteBusinessCenterFromTable(){
        
    }
}
