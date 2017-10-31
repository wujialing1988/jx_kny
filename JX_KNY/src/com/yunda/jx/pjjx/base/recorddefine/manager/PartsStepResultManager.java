package com.yunda.jx.pjjx.base.recorddefine.manager;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.base.recorddefine.entity.PartsStepResult;
import com.yunda.jx.pjjx.base.recorddefine.entity.RecordRI;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordRIAndDI;



/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 配件默认检测检修结果
 * <li>创建人：林欢
 * <li>创建日期：2016-5-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
@Service(value="partsStepResultManager")
public class PartsStepResultManager extends JXBaseManager<PartsStepResult, PartsStepResult>{
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** RecordRI业务类,检修检测项 */
    @Resource
    RecordRIManager recordRIManager;
    
    /**
     * <li>说明：保存检测/检修结果
     * <li>创建人：林欢
     * <li>创建日期：2016-5-9
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param partsStepResult 默认检测结果实体对象
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    public void savePartsStepResult(PartsStepResult partsStepResult) throws BusinessException, NoSuchFieldException {
        
//      获取recordRI的对象
        RecordRI recordRI = recordRIManager.getModelById(partsStepResult.getRecordRIIDX());
        
//      如果新增默认结果数据时，判断传递的数据是否是默认，如果是默认，查询当前下是否有默认,有默认修改为否，同时本条数据修改为默认
        if (partsStepResult.getIsDefault() != null && PartsStepResult.ISDEFAULT_YES.equals(partsStepResult.getIsDefault())) {
//            StringBuilder hql = new StringBuilder();
//            hql.append("from PartsStepResult where recordRIIDX = '").append(partsStepResult.getRecordRIIDX()).append("'");
//            hql.append(" and isDefault = '").append(PartsStepResult.ISDEFAULT_YES).append("'");
//            
//            if (!StringUtil.isNullOrBlank(partsStepResult.getIdx())) {
//                hql.append(" and idx != '").append(partsStepResult.getIdx()).append("'");
//            }
//            
            List<PartsStepResult> list = this.getDefaultPartsStepResultByRdpRecordCardIDX(partsStepResult);
            if (list != null && list.size() > 0) {
                
                for (PartsStepResult result : list) {
                    result.setIsDefault(PartsStepResult.ISDEFAULT_NO);
                    saveOrUpdate(result);
                }
            }
            
//          当输入默认的时候维护recordRI的默认结果
            recordRI.setDefaultResult(partsStepResult.getResultName());
            recordRIManager.saveOrUpdate(recordRI);
        }else {
            //查询当新增的是否的时候，是否还有默认的结果，如果没有默认为是的默认结果，修改PJJX_Record_RI表的默认结果为null
            List<PartsStepResult> list = this.getDefaultPartsStepResultByRdpRecordCardIDX(partsStepResult);
            if (StringUtil.isNullOrBlank(partsStepResult.getIdx()) && list == null || list.isEmpty()) {
                recordRI.setDefaultResult("");
                recordRIManager.saveOrUpdate(recordRI);
            }
        }
        
        saveOrUpdate(partsStepResult);
    }
    
    /**
     * <li>说明：通过配件检修检测项实例条件查询默认检测结果实体对象
     * <li>创建人：林欢
     * <li>创建日期：2016-5-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param riDi 配件检修检测项及数据项的封装对象
     * @return List<PartsStepResult> 默认结果集 
     */
    @SuppressWarnings("unchecked")
    public List<PartsStepResult> getPartsStepResultByRdpRecordCardIDX(PartsRdpRecordRIAndDI riDi) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select psr from PartsStepResult psr,RecordRI rr,PartsRdpRecordRI prr where psr.recordRIIDX = rr.idx ");    
        sb.append(" and prr.recordRIIDX = rr.idx ");    
        sb.append(" and prr.idx = '").append(riDi.getIdx()).append("'");
        
        List<PartsStepResult> pList = (List<PartsStepResult>) find(sb.toString());
        
        return pList;
    }
    
    /**
     * <li>说明：根据“配件检修检测项实例idx主键”查询配件检修检测项配置的默认检测结果
     * <li>创建人：何涛
     * <li>创建日期：2016-5-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param riIDX 配件检修检测项实例idx主键
     * @return 配置的配件检测检修想默认处理结果集合
     */
    @SuppressWarnings("unchecked")
    public List<PartsStepResult> getModelsByRiIDX(String riIDX) {
        String hql = "Select a From PartsStepResult a, PartsRdpRecordRI b Where a.recordRIIDX = b.recordRIIDX And b.idx = ?";
        return this.daoUtils.find(hql, new Object[]{ riIDX });
    }
    
    /**
     * <li>说明：根据“配件检修检测项实例idx主键”查询配件检修检测项配置的默认为是的检测结果，
     * <li>创建人：林欢
     * <li>创建日期：2016-5-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param riIDX 配件检修检测项实例idx主键
     * @return 配置的配件检测检修想默认处理结果集合
     */
    @SuppressWarnings("unchecked")
    public List<PartsStepResult> getDefaultPartsStepResultByRdpRecordCardIDX(PartsStepResult partsStepResult) {
        StringBuilder hql = new StringBuilder();
        hql.append("from PartsStepResult where recordRIIDX = '").append(partsStepResult.getRecordRIIDX()).append("'");
        hql.append(" and isDefault = '").append(PartsStepResult.ISDEFAULT_YES).append("'");
        
        if (!StringUtil.isNullOrBlank(partsStepResult.getIdx())) {
            hql.append(" and idx != '").append(partsStepResult.getIdx()).append("'");
        }
        
        List<PartsStepResult> list = daoUtils.find(hql.toString());
        return list;
    }

    /**
     * 
     * <li>说明：删除检测/检修结果
     * <li>创建人：林欢
     * <li>创建日期：2016-5-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 删除id集合
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     * @throws Exception
     */
    public void deletePartsStepResult(String[] ids) throws BusinessException, NoSuchFieldException {
        
        //通过对象查询PJJX_Record_RI主键id
        PartsStepResult psr = this.getModelById(ids[0]);
//      获取recordRI的对象
        RecordRI recordRI = recordRIManager.getModelById(psr.getRecordRIIDX());
        
        //查询是否有默认值，没有默认值的话，修改数据位否
        List<PartsStepResult> list = this.getDefaultPartsStepResultByRdpRecordCardIDX(psr);
        if (list == null || list.isEmpty()) {
            recordRI.setDefaultResult("");
            recordRIManager.saveOrUpdate(recordRI);
        }
        
        //删除检测检修当前数据
        this.deleteByIds(ids);
    }
    
}
