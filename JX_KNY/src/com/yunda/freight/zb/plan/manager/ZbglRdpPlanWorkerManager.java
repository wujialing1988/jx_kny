package com.yunda.freight.zb.plan.manager;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.freight.zb.plan.entity.ZbglRdpPlanWorker;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 列检车辆作业人员业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-3-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("zbglRdpPlanWorkerManager")
public class ZbglRdpPlanWorkerManager extends JXBaseManager<ZbglRdpPlanWorker, ZbglRdpPlanWorker> {
    
    /**
     * <li>说明：保存列检车辆作业人员
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpRecordIdx 列检车辆ID
     * @param empidArray 作业人员ID数组
     * @param empnameArray 作业人员名称数组
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void saveWorkPersons(String rdpRecordIdx, String[] empidArray, String[] empnameArray) throws BusinessException, NoSuchFieldException {
        //      删除对应的作业人员
        deleteWorkPersonByRecord(rdpRecordIdx);
        // 保存作业人员
        for (int i = 0; i < empidArray.length; i++) {
            String empid = empidArray[i];
            String empname = empnameArray[i];
            ZbglRdpPlanWorker worker = new ZbglRdpPlanWorker();
            worker.setRdpRecordIdx(rdpRecordIdx);
            worker.setWorkPersonIdx(empid);
            worker.setWorkPersonName(empname);
            this.saveOrUpdate(worker);
        }
    }

    /**
     * <li>说明：删除列检车辆对应的作业人员
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpRecordIdx 列检车辆ID
     */
    private void deleteWorkPersonByRecord(String rdpRecordIdx) {
        StringBuffer hql = new StringBuffer(" delete From ZbglRdpPlanWorker where rdpRecordIdx = ? ");
        this.daoUtils.execute(hql.toString(), new Object[]{rdpRecordIdx});
    }
    
    /**
     * <li>说明：通过人员指派车辆
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpRecordIdx 作业车辆集合
     * @param empid 作业人员ID
     * @param empname 作业人员名称
     * @param rdpPlanIdx 列检计划ID
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveWorkPersonsByPerson(String[] rdpRecordIdx, String empid, String empname, String rdpPlanIdx) throws BusinessException, NoSuchFieldException {
        //      删除对应的作业人员
        deleteWorkPersonByPerson(empid,rdpPlanIdx);
        // 保存作业人员
        for (int i = 0; i < rdpRecordIdx.length; i++) {
            String recordIdx = rdpRecordIdx[i];
            ZbglRdpPlanWorker worker = new ZbglRdpPlanWorker();
            worker.setRdpRecordIdx(recordIdx);
            worker.setWorkPersonIdx(empid);
            worker.setWorkPersonName(empname);
            this.saveOrUpdate(worker);
        }
    }
    
    /**
     * <li>说明：删除人员对应的车辆
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param empid 作业人员
     * @param rdpPlanIdx 列检计划ID
     */
    private void deleteWorkPersonByPerson(String empid, String rdpPlanIdx) {
        StringBuffer hql = new StringBuffer(" delete From ZbglRdpPlanWorker w where w.workPersonIdx = ? ");
        hql.append(" and w.rdpRecordIdx in (select idx from ZbglRdpPlanRecord where recordStatus = 0 and rdpPlanIdx = ? ) ");
        this.daoUtils.execute(hql.toString(), new Object[]{empid,rdpPlanIdx});
    }
    
    /**
     * <li>说明：获取车辆下面所有已派工人员
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpRecordIdx
     * @return
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, String> findZbglRdpPlanWorkerByRecord(String rdpRecordIdx){
        HashMap<String, String> map = new HashMap<String, String>();
        String workPersonIdxs = "" ;
        String workPersonNames = "" ;
        StringBuffer hql = new StringBuffer(" From ZbglRdpPlanWorker where rdpRecordIdx = ? ");
        List<ZbglRdpPlanWorker> list = this.daoUtils.find(hql.toString(), new Object[]{rdpRecordIdx});
        for (ZbglRdpPlanWorker worker : list) {
            workPersonIdxs += worker.getWorkPersonIdx() + "," ;
            workPersonNames += worker.getWorkPersonName() + "," ;
        }
        if(!StringUtil.isNullOrBlank(workPersonIdxs)){
            workPersonIdxs = workPersonIdxs.substring(0,workPersonIdxs.length()-1);
        }
        if(!StringUtil.isNullOrBlank(workPersonNames)){
            workPersonNames = workPersonNames.substring(0,workPersonNames.length()-1);
        }
        map.put("workPersonIdxs", workPersonIdxs);
        map.put("workPersonNames", workPersonNames);
        return map ;
    }
    
}
