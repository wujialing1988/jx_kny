package com.yunda.jx.jxgc.producttaskmanage.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jxgc.producttaskmanage.entity.DetectResult;
import com.yunda.jx.jxgc.repairrequirement.entity.DetectItem;
import com.yunda.jx.webservice.stationTerminal.base.entity.DataItemBean;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：DetectResult业务类,检测结果
 * <li>创建人：程锐
 * <li>创建日期：2012-12-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="detectResultManager")
public class DetectResultManager extends JXBaseManager<DetectResult, DetectResult>{
        
    /**
     * <li>方法名称：getNotCompleteCount
     * <li>方法说明：获取未完成数据项数量 
     * <li>@param workTaskIdx
     * <li>@return
     * <li>return: int
     * <li>创建人：张凡
     * <li>创建时间：2013-3-2 下午01:28:36
     * <li>修改人：程锐 2013-7-9
     * <li>修改内容：验证是否有必填的数据项未完成
     */
    public int getNotCompleteCount(String workTaskIdx){
        String sql ="select count(1) from JXGC_Detect_Result where work_task_idx ='" 
                + workTaskIdx + "' and (detect_result is  null and isNotBlank = " 
                + DetectItem.ISNOTBLANK_YES + ") and record_status=0";
        return Integer.valueOf(daoUtils.executeSqlQuery(sql).iterator().next().toString());
    }
    
    /**
     * <li>方法名称：updateDetectResult
     * <li>方法说明：录入数据项 
     * <li>@param idx
     * <li>@param value
     * <li>@param operator
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-4-30 下午04:21:40
     * <li>修改人：
     * <li>修改内容：     
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void updateDetectResult(String idx, String value, Long operator) throws BusinessException, NoSuchFieldException{
        DetectResult result = getModelById(idx);
        result.setDetectResult(value);
        result.setUpdator(operator);
        saveOrUpdate(result);
    }
    
    /**
     * <li>说明：获取某个作业任务下属的所有检测项
     * <li>创建人：何涛
     * <li>创建日期：2015-7-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workTaskIDX 作业任务主键
     * @return  某个作业任务下属的所有检测项
     */
    @SuppressWarnings("unchecked")
    public List<DetectResult> getModelsByworkTaskIDX (String workTaskIDX) {
        String hql = "From DetectResult Where workTaskIDX = ? And recordStatus = 0 Order By sortSeq ASC";
        return this.daoUtils.find(hql, new Object[]{ workTaskIDX });
    }

    /**
     * <li>说明：保存检修检测结果
     * <li>创建人：何涛
     * <li>创建日期：2016-4-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param dataItemList 检测检测结果数据集合
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void save(List<DataItemBean> dataItemList) throws BusinessException, NoSuchFieldException {
        List<DetectResult> entityList = new ArrayList<DetectResult>(dataItemList.size());
        DetectResult entity = null;
        for (DataItemBean bean : dataItemList) {
            entity = this.getModelById(bean.getIdx());
            if (null == entity) {
                continue;
            }
            // 检测结果
            entity.setDetectResult(bean.getDetectResult());
        }
        this.saveOrUpdate(entityList);
    }

    /**
     * <li>说明：检测项结果
     * <li>创建人：张迪
     * <li>创建日期：2016-9-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workTaskIDX 检测项idx
     * @return 检测检测结果数据集合
     */
    @SuppressWarnings("unchecked")
    public List<DataItemBean> findListByWorkTask(String workTaskIDX) {
        String hql = "select new DataItemBean(a.idx, a.detectItemContent, a.isNotBlank, a.detectResulttype, a.detectResult, a.detectItemStandard, a.minResult, a.maxResult )" +
        " from DetectResult a where recordStatus = 0 and workTaskIDX = '" + workTaskIDX + "'";
        return daoUtils.find(hql);
    }
    
}