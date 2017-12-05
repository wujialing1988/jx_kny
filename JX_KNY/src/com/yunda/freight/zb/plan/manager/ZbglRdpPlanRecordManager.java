package com.yunda.freight.zb.plan.manager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.IbaseComboTree;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.freight.base.classOrganization.entity.ClassOrganizationUser;
import com.yunda.freight.base.vehicle.entity.TrainVehicleType;
import com.yunda.freight.base.vehicle.manager.TrainVehicleTypeManager;
import com.yunda.freight.zb.plan.entity.ZbglRdpPlan;
import com.yunda.freight.zb.plan.entity.ZbglRdpPlanRecord;
import com.yunda.freight.zb.plan.entity.ZbglRdpPlanRecordBean;
import com.yunda.freight.zb.plan.entity.ZbglRdpPlanRecordListBean;
import com.yunda.freight.zb.plan.entity.ZbglRdpPlanWiBean;
import com.yunda.freight.zb.plan.entity.ZbglRdpWorkerVo;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;
import com.yunda.jx.jczl.attachmanage.entity.JczlTrain;
import com.yunda.jx.jczl.attachmanage.manager.JczlTrainManager;
import com.yunda.jx.jczl.attachmanage.manager.TrainStatusChangeManager;
import com.yunda.passenger.traindemand.entity.MarshallingTrainDemand;
import com.yunda.passenger.traindemand.manager.MarshallingTrainDemandManager;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpManager;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpNodeManager;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWi;
import com.yunda.zb.rdp.zbtaskbill.manager.ZbglRdpWiManager;
import com.yunda.zb.tp.manager.ZbglTpManager;
import com.yunda.zb.zbfw.entity.ZbFw;
import com.yunda.zb.zbfw.manager.ZbFwManager;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 车辆列检计划业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-3-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("zbglRdpPlanRecordManager")
public class ZbglRdpPlanRecordManager extends JXBaseManager<ZbglRdpPlanRecord, ZbglRdpPlanRecord> implements IbaseComboTree {
    
    /**
     * 列检车辆作业人员业务类
     */
    @Resource
    private ZbglRdpPlanWorkerManager zbglRdpPlanWorkerManager;
    
    /**
     * 列检计划业务类
     */
    @Resource
    private ZbglRdpPlanManager zbglRdpPlanManager;
    
    
    /**
     * 车型业务类
     */
    @Resource
    private TrainVehicleTypeManager trainVehicleTypeManager;
    
    
    /**
     * 整备范围业务类
     */
    @Resource
    private ZbFwManager zbFwManager;
    
    /**
     * 整备任务单业务类
     */
    @Resource
    private ZbglRdpManager zbglRdpManager;
    
    /**
     * ZbglRdpWi业务类,机车整备任务单
     */
    @Resource
    private ZbglRdpWiManager zbglRdpWiManager;
    
    
    /** 整备节点业务类 */
    @Resource
    private ZbglRdpNodeManager zbglRdpNodeManager;
    
    /** 提票业务类 */
    @Resource
    private ZbglTpManager zbglTpManager ;
    
    /** 人员选择业务类 */
    @Resource
    private OmEmployeeSelectManager omEmployeeSelectManager;
    
    /** 列表需求车辆维护业务类 */
    @Resource
    private MarshallingTrainDemandManager marshallingTrainDemandManager;
    
    /**
     * 车辆状态业务类
     */
    @Resource
    private TrainStatusChangeManager trainStatusChangeManager;
    
    /**
     * 车辆信息表
     */
    @Resource
    private JczlTrainManager jczlTrainManager ;
    
    

    /**
     * <li>说明：通过计划生成车辆计划实体
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param plan 列检计划
     * @param flag true:新建或者作业班组发生改变，直接全部重新添加 ;false:根据改变的数量决定删除多余的还是添加
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void generateRecords(ZbglRdpPlan plan,boolean flag) throws BusinessException, NoSuchFieldException {
        // 如果没有车，则直接重写添加
        if(flag){
            deleteRecordsByPlanId(plan.getIdx());
            // 如果是客车列检，则需要通过编组任务赋值车型车号
            if(plan.getVehicleType().equals("20")){
                    createRecordKY(plan);
            }else{
                for (int i = 0; i < plan.getRdpNum(); i++) {
                    createRecord(i+1, plan.getIdx());
                }    
            }
        }else{
            List<ZbglRdpPlanRecord> planRecords = getZbglRdpPlanRecordListByPlan(plan.getIdx());
            // 增加
            if(plan.getRdpNum() > planRecords.size()){
                for (int i = planRecords.size() ; i < plan.getRdpNum(); i++) {
                    createRecord(i+1, plan.getIdx());
                }
            }
            // 减少
            else{
                deleteRecordsByPlanIdAndSeq(plan.getIdx(), plan.getRdpNum());
            }
        }

    }
    
    /**
     * <li>说明：客车列检生成车辆列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param plan
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    private void createRecordKY(ZbglRdpPlan plan) throws BusinessException, NoSuchFieldException {
       if(StringUtil.isNullOrBlank(plan.getTrainDemandIdx())){
           return ;
       }
       List<MarshallingTrainDemand> list = marshallingTrainDemandManager.getMarshallingTrainDemandListByDemand(plan.getTrainDemandIdx());
       for (MarshallingTrainDemand demand : list) {
           ZbglRdpPlanRecord record = new ZbglRdpPlanRecord(demand.getSeqNo());
           TrainVehicleType vehicleType = trainVehicleTypeManager.getModelById(demand.getTrainTypeIDX());
           record.setTrainTypeIdx(vehicleType.getIdx());
           record.setTrainTypeCode(vehicleType.getTypeCode());
           record.setTrainTypeName(vehicleType.getTypeName());
           record.setVehicleKindCode(vehicleType.getVehicleKindCode());
           record.setVehicleKindName(vehicleType.getVehicleKindName());
           record.setTrainNo(demand.getTrainNo());
           record.setSeqNum(demand.getSeqNo());
           record.setRdpPlanIdx(plan.getIdx());
           record.setRdpRecordStatus(ZbglRdpPlanRecord.STATUS_INITIALIZATION);
           this.saveOrUpdate(record);
       }
    }

    /**
     * <li>说明：新增车辆
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param seqNum
     * @param planIdx
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private void createRecord(Integer seqNum , String planIdx) throws BusinessException, NoSuchFieldException{
        ZbglRdpPlanRecord record = new ZbglRdpPlanRecord(seqNum);
        record.setRdpPlanIdx(planIdx);
        record.setRdpRecordStatus(ZbglRdpPlanRecord.STATUS_INITIALIZATION);
        this.saveOrUpdate(record);
    }
    
    
    /**
     * <li>说明：删除计划下的车辆
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param planIdx
     */
    private void deleteRecordsByPlanId(String planIdx) {
        StringBuffer hql = new StringBuffer(" delete from ZbglRdpPlanRecord where rdpPlanIdx = ? ");
        this.daoUtils.execute(hql.toString(), new Object[]{planIdx});
    }
    
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param planIdx
     * @param seqNum
     */
    private void deleteRecordsByPlanIdAndSeq(String planIdx,Integer seqNum) {
        StringBuffer hql = new StringBuffer(" delete from ZbglRdpPlanRecord where rdpPlanIdx = ? and seqNum > ?");
        this.daoUtils.execute(hql.toString(), new Object[]{planIdx,seqNum});
    }

    /**
     * <li>说明：通过计划ID查询车辆列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param planIdx
     * @return
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    public List<ZbglRdpPlanRecord> getZbglRdpPlanRecordListByPlan(String planIdx) throws BusinessException, NoSuchFieldException{
        StringBuffer hql = new StringBuffer(" From ZbglRdpPlanRecord where recordStatus = 0 and rdpPlanIdx = ? order by seqNum ");
        return this.daoUtils.find(hql.toString(), new Object[]{planIdx});
    }
    
    /**
     * <li>说明：通过车号计划ID返回车辆
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param planIdx   计划ID
     * @param trainNo   车号
     * @return
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public ZbglRdpPlanRecord getZbglRdpPlanRecordListByTrainNo(String planIdx,String trainNo,String recordIdx) throws BusinessException, NoSuchFieldException{
        StringBuffer hql = new StringBuffer(" From ZbglRdpPlanRecord where recordStatus = 0 and rdpPlanIdx = ? and trainNo = ? and idx <> ?  order by seqNum ");
        return (ZbglRdpPlanRecord)this.daoUtils.findSingle(hql.toString(), new Object[]{planIdx,trainNo,recordIdx});
    }
    
    /**
     * <li>说明：查询所有已启动和已完成的车辆计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param planIdx
     * @return
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    public List<ZbglRdpPlanRecord> getZbglRdpPlanRecordComListByPlan(String planIdx) throws BusinessException, NoSuchFieldException{
        StringBuffer hql = new StringBuffer(" From ZbglRdpPlanRecord where recordStatus = 0 and rdpRecordStatus <> 'INITIALIZATION' and rdpPlanIdx = ? order by seqNum ");
        return this.daoUtils.find(hql.toString(), new Object[]{planIdx});
    }
    
    /**
     * <li>说明：根据整备单查询车辆计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIdx 整备单ID
     * @return
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    public ZbglRdpPlanRecord getZbglRdpPlanRecordByRdpIdx(String rdpIdx) throws BusinessException, NoSuchFieldException{
        StringBuffer hql = new StringBuffer(" From ZbglRdpPlanRecord where recordStatus = 0 and rdpIdx = ? order by seqNum ");
        return (ZbglRdpPlanRecord)this.daoUtils.findSingle(hql.toString(), new Object[]{rdpIdx});
    }
    

    /**
     * <li>说明：派工
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idxs 车辆ID集合
     * @param empids 作业人员ID集合
     * @param empnames 作业人员姓名集合
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void dispatcher(String idxs, String empids, String empnames) throws BusinessException, NoSuchFieldException {
        String[] idxArray = idxs.split(",");
        String[] empidArray = empids.split(",");
        String[] empnameArray = empnames.split(",");
        for (String idx : idxArray) {
            ZbglRdpPlanRecord planRecord = this.getModelById(idx);
            if(planRecord != null){
                planRecord.setWorkPersonIdx(empids);
                planRecord.setWorkPersonName(empnames);
                this.saveOrUpdate(planRecord);
                zbglRdpPlanWorkerManager.saveWorkPersons(idx,empidArray,empnameArray);
            }
        }
    }
    
    /**
     * <li>说明：人派车
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param recordIdxs 车辆ID集合
     * @param workPersonIdxs 人员ID集合
     * @param workPersonNames 人员名称集合
     * @param rdpPlanIdx 列检计划ID
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void disrecord(String recordIdxs, String workPersonIdxs, String workPersonNames, String rdpPlanIdx) throws BusinessException, NoSuchFieldException {
        String[] recordIdxArray = recordIdxs.split(",");
        String[] workPersonIdxArray = workPersonIdxs.split(",");
        String[] workPersonNameArray = workPersonNames.split(",");
        for (int i = 0; i < workPersonIdxArray.length; i++) {
            String workPersonIdx = workPersonIdxArray[i];
            String workPersonName = workPersonNameArray[i];
            zbglRdpPlanWorkerManager.saveWorkPersonsByPerson(recordIdxArray, workPersonIdx, workPersonName,rdpPlanIdx);
        }
        // 保存作业车辆对用的人员
        saveWorkPersonByPlan(rdpPlanIdx);
    }

    /**
     * <li>说明：保存计划下车辆的作业人员
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpPlanIdx
     * @throws NoSuchFieldException
     */
    private void saveWorkPersonByPlan(String rdpPlanIdx) throws NoSuchFieldException {
        List<ZbglRdpPlanRecord> list = this.getZbglRdpPlanRecordListByPlan(rdpPlanIdx);
        for (ZbglRdpPlanRecord record : list) {
            if(record != null){
                HashMap<String, String> map = zbglRdpPlanWorkerManager.findZbglRdpPlanWorkerByRecord(record.getIdx());
                record.setWorkPersonIdx(map.get("workPersonIdxs"));
                record.setWorkPersonName(map.get("workPersonNames"));
                this.saveOrUpdate(record);
                
            }
        }
    }
    

    /**
     * <li>说明：车辆列检确认
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity
     */
    public void completeRecord(ZbglRdpPlanRecord entity) {
        if(StringUtil.isNullOrBlank(entity.getIdx())){
            throw new BusinessException("车辆计划ID为空！");
        }
        ZbglRdpPlanRecord record = this.getModelById(entity.getIdx());
        if(record == null){
            throw new BusinessException("未查到车辆计划！");
        }
        if(!record.getRdpRecordStatus().equals(ZbglRdpPlanRecord.STATUS_HANDLING)){
            throw new BusinessException("只能完成【处理中】状态的车辆！");
        }
        record.setRdpRecordStatus(ZbglRdpPlanRecord.STATUS_HANDLED);
        record.setRdpEndTime(new Date());
        // 设置计划启动人
        OmEmployee employee = SystemContext.getOmEmployee();
        if(employee != null){
            record.setCompletePersonIdx(employee.getEmpid()+"");
            record.setCompletePersonName(employee.getEmpname());
        }
        ZbglRdp rdp = zbglRdpManager.getModelById(record.getRdpIdx());
        rdp.setRdpEndTime(new Date());
        rdp.setRdpStatus(ZbglRdp.STATUS_HANDLED);
        zbglRdpManager.update(rdp);
        this.update(record);
        
    }

    /**
     * <li>说明：填写车号
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public ZbglRdpPlanRecord writeTrainNo(ZbglRdpPlanRecord entity) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        if(StringUtil.isNullOrBlank(entity.getIdx())){
            throw new BusinessException("车辆计划ID为空！");
        }
        ZbglRdpPlanRecord record = this.getModelById(entity.getIdx());
        if(record == null){
            throw new BusinessException("未查到车辆计划！");
        }
        JczlTrain train = jczlTrainManager.getHCJczlTrainByNo(entity.getTrainNo());
        if(train == null){
            throw new BusinessException("该车辆未在系统中登记，请核对录入车号！");
        }
        ZbglRdpPlanRecord oldr = getZbglRdpPlanRecordListByTrainNo(record.getRdpPlanIdx(), entity.getTrainNo(),entity.getIdx());
        if(oldr != null){
            throw new BusinessException("第"+oldr.getSeqNum()+"辆车已经登记过该车号，请核对录入车号！");
        }
        record.setTrainNo(train.getTrainNo());
        record.setTrainTypeCode(train.getTrainTypeShortName());
        record.setTrainTypeIdx(train.getTrainTypeIDX());
        record.setTrainTypeName(train.getTrainTypeShortName());
        this.update(record);
        return record ;
    }
    
    

    /**
     * <li>说明：根据作业任务完成-完成车辆计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpWiIDX
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void completeRecordByRdpWiIDX(String rdpWiIDX) throws BusinessException, NoSuchFieldException {
        if(StringUtil.isNullOrBlank(rdpWiIDX)){
            return ;
        }
        ZbglRdpWi wi = zbglRdpWiManager.getModelById(rdpWiIDX);
        if(wi == null){
            return ;
        }
        // 查询未完成的作业单
        List noCompleteList = zbglRdpWiManager.getAllRdpWiNoCompleteListByRdp(wi.getRdpIDX());
        if(noCompleteList != null && noCompleteList.size() > 0){
            return ;
        }
        ZbglRdpPlanRecord record = this.getZbglRdpPlanRecordByRdpIdx(wi.getRdpIDX());
        if(record == null || !record.getRdpRecordStatus().equals(ZbglRdpPlanRecord.STATUS_HANDLING)){
            return ;
        }
        record.setRdpRecordStatus(ZbglRdpPlanRecord.STATUS_HANDLED);
        record.setRdpEndTime(new Date());
        ZbglRdp rdp = zbglRdpManager.getModelById(record.getRdpIdx());
        rdp.setRdpEndTime(new Date());
        rdp.setRdpStatus(ZbglRdp.STATUS_HANDLED);
        zbglRdpManager.update(rdp);
        this.update(record);
    }
    
    /**
     * <li>说明：通过作业班组查询班组人员
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param orgIdx 作业班组
     * @param rdpPlanIdx 列检计划ID
     * @return
     */
    public List<ZbglRdpWorkerVo> findWorkerList(String orgIdx, String rdpPlanIdx) {
        if(StringUtil.isNullOrBlank(orgIdx)){
            return null ;
        }
        List<ZbglRdpWorkerVo> list = new ArrayList<ZbglRdpWorkerVo>();
        List omEmployees = (List<OmEmployee>)omEmployeeSelectManager.findTeamEmpList(Long.parseLong(orgIdx));
        for (int i = 0; i < omEmployees.size(); i++) {
            Object[] employee = (Object[])omEmployees.get(i);
            ZbglRdpWorkerVo vo = new ZbglRdpWorkerVo(employee[0] + "" , employee[0] + "" ,employee[2] + "");
            HashMap<String, String> map = this.getRecordsByPlanAndEmp(rdpPlanIdx,employee[0] + "");
            vo.setRecordIdxs(map.get("recordIdxs"));
            vo.setRecordNames(map.get("recordNames"));
            list.add(vo);
        }
        return list;
    }

    /**
     * <li>说明：返回一个人员对应的车辆数组
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpPlanIdx 计划ID
     * @param empid 人员ID
     * @return HashMap<String, String>
     */
    private HashMap<String, String> getRecordsByPlanAndEmp(String rdpPlanIdx, String empid) {
        HashMap<String, String> map = new HashMap<String, String>() ;
        List<ZbglRdpPlanRecord> recordlist = getWorkerByPlanAndEmp(rdpPlanIdx,empid);
        String recordIdxs = "" ;
        String recordNames = "" ;
        for (ZbglRdpPlanRecord record : recordlist) {
            recordIdxs += record.getIdx() + ",";
            recordNames += "第" + record.getSeqNum() + "辆,";
        }
        if(!StringUtil.isNullOrBlank(recordIdxs)){
            recordIdxs = recordIdxs.substring(0, recordIdxs.length() - 1);
        }
        if(!StringUtil.isNullOrBlank(recordNames)){
            recordNames = recordNames.substring(0, recordNames.length() - 1);
        }
        map.put("recordIdxs", recordIdxs) ;
        map.put("recordNames", recordNames) ;
        return map;
    }

    /**
     * <li>说明：返回人员的作业车辆列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpPlanIdx
     * @param empid
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ZbglRdpPlanRecord> getWorkerByPlanAndEmp(String rdpPlanIdx, String empid) {
        StringBuffer hql = new StringBuffer(" select r From ZbglRdpPlanRecord r, ZbglRdpPlanWorker w,ZbglRdpPlan p where r.recordStatus = 0 and p.recordStatus = 0 ");
        hql.append(" and w.rdpRecordIdx = r.idx and r.rdpPlanIdx = p.idx and p.idx = ? and w.workPersonIdx = ? order by r.seqNum ASC");
        return this.daoUtils.find(hql.toString(), new Object[]{rdpPlanIdx,empid});
    }
    
    /**
     * <li>说明：重写getBaseComboTree，获取下拉树前台store所需List<HashMap>对象（范围活实例树形结构数据）
     * <li>创建人：何东
     * <li>创建日期：2017-04-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param req 
     * @return List<HashMap> 下拉树前台store所需List<HashMap>对象
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<HashMap> getBaseComboTree(HttpServletRequest req) throws Exception{
        String queryParams = StringUtil.nvlTrim(req.getParameter("queryParams"), "{}");
        Map queryParamsMap = JSONUtil.read(queryParams, Map.class);
        String planIdx = (String)queryParamsMap.get("planIdx");
        return getScopeWorkTree(planIdx);
    }
    
    /**
     * <li>说明：获取专业类型选择
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-7-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param planIdx
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<HashMap> getScopeWorkTree(String planIdx){
        List<ZbglRdpPlanWiBean> list = findRdpWis(planIdx).getList();
        List<HashMap> results = new ArrayList<HashMap>();
        HashMap nodeMap = null;
        for (ZbglRdpPlanWiBean bean : list) {
            nodeMap = new HashMap();
            nodeMap.put("id", bean.getWiIdx());
            nodeMap.put("text", bean.getWiName());
            nodeMap.put("leaf", true);
            results.add(nodeMap);
        }
        return results;
    }
    
    
    /**
     * <li>说明：通过车型车号判断该计划下是否已经启动了该车辆
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx
     * @param trainNo
     * @param planIdx
     * @return
     */
    public int getRecordCountsByPlan(String trainTypeIdx,String trainNo,String planIdx){
        StringBuffer hql = new StringBuffer(" select count(*) From ZbglRdpPlanRecord where recordStatus = 0 " +
                "and rdpRecordStatus <> ? and trainTypeIdx = ? and trainNo = ? and rdpPlanIdx = ?  ");
        return this.daoUtils.getCount(hql.toString(), new Object[]{ZbglRdpPlanRecord.STATUS_INITIALIZATION,trainTypeIdx,trainNo,planIdx});
    }

    /**
     * <li>说明：启动全部的车辆计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param plan
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void startRecordsForFreight(ZbglRdpPlan plan) throws BusinessException, NoSuchFieldException {
        Map<String, List<ClassOrganizationUser>> map =  getQueueUserMap(plan);
        // 倒叙获取车辆列表
        List<ZbglRdpPlanRecord> records = getZbglRdpPlanRecordListByPlan(plan.getIdx());
        // 遍历人员分队
        if(map.size() > 0 && records.size() > 0 && records.size()/map.size() > 0){
            int rn = records.size()/map.size() ; // 每队几辆车
            int yn = records.size()%map.size() ; // 还剩余的车辆
            int start = 0 ;
            int end = 0 ;
            for (String key : map.keySet()) {
                List<ClassOrganizationUser> users = map.get(key);
                String empids = "";
                String empnames = "";
                for (ClassOrganizationUser user : users) {
                    empids += user.getWorkPersonIdx() + "," ;
                    empnames += user.getWorkPersonName() + "," ;
                }
                if(!StringUtil.isNullOrBlank(empids)){
                    empids = empids.substring(0, empids.length() - 1) ;
                    empnames = empnames.substring(0, empnames.length() - 1) ;
                }
                // 最后一次遍历，取最大的
                if(yn != 0){
                    end += (rn + 1) ;
                    yn--;
                }else{
                    end += rn ;
                }
                // 自动派人
                dispatcherQueueForRecords(records, start, end, empids, empnames);
                System.err.println("分队："+key+"，车辆：从"+start+"到"+end);
                start = end ;
            }
        }
    }
    
    /**
     * <li>说明：自动派车并且启动车辆检测
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param records
     * @param start
     * @param end
     * @param empids
     * @param empnames
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private void dispatcherQueueForRecords(List<ZbglRdpPlanRecord> records, int start,int end,String empids,String empnames) throws BusinessException, NoSuchFieldException{
        String[] empidArray = empids.split(",");
        String[] empnameArray = empnames.split(",");
        for (int i = start; i < end; i++) {
            ZbglRdpPlanRecord rec = records.get(i);
            if(rec != null){
                rec.setWorkPersonIdx(empids);
                rec.setWorkPersonName(empnames);
                this.saveOrUpdate(rec);
                zbglRdpPlanWorkerManager.saveWorkPersons(rec.getIdx(),empidArray,empnameArray);
            }             
        }      
    }
    
    /**
     * <li>说明：批量启动车辆（货车） 直接启动车辆
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param planIdx
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void batchStartRecordsForHC(ZbglRdpPlan plan) throws BusinessException, NoSuchFieldException{
        List<ZbglRdpPlanRecord> records = getZbglRdpPlanRecordListByPlan(plan.getIdx());
        for (ZbglRdpPlanRecord rec : records) {
            // 启动车辆
            rec.setRdpRecordStatus(ZbglRdpPlanRecord.STATUS_HANDLING);
            rec.setRdpStartTime(new Date());
            this.saveOrUpdate(rec);
        }
    }
    
    /**
     * <li>说明：批量启动车辆（客车） 找到作业范围，并生成相关数据
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param planIdx
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws InvocationTargetException 
     * @throws Exception 
     */
    public void batchStartRecordsForKC(ZbglRdpPlan plan) throws Exception{
        List<ZbglRdpPlanRecord> records = getZbglRdpPlanRecordListByPlan(plan.getIdx());
        for (ZbglRdpPlanRecord record : records) {
            // 启动车辆
            ZbFw fw = zbFwManager.getZbfwByVehicleCode(record.getTrainTypeCode(),plan.getVehicleType());
            if(fw == null){
                record.setRdpRecordStatus(ZbglRdpPlanRecord.STATUS_HANDLED);
                record.setRdpStartTime(new Date());
                record.setRdpEndTime(new Date());
                this.update(record);
                continue ;
            }
            record.setRdpRecordStatus(ZbglRdpPlanRecord.STATUS_HANDLING);
            record.setRdpStartTime(new Date());
            ZbglRdp rdp = zbglRdpManager.saveByZbglRdpPlanRecord(record);
            //整备单idx
            String[] param = { rdp.getIdx() };
            rdp.setZbfwIDX(fw.getIdx());
            zbglRdpManager.saveOrUpdate(rdp);
            zbglRdpNodeManager.saveNodeAndSeq(rdp);
            // 调用存储过程，生产整备范围
            zbglRdpWiManager.saveZbglRdpWiByProc(param);
            String[] paramTp = { rdp.getIdx(),plan.getSiteID() };
            //修改jt6状态
            zbglTpManager.saveZbglTpByProc(paramTp);
            record.setRdpIdx(rdp.getIdx());
            this.update(record);            
            
        }
    }


    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param plan
     */
    @SuppressWarnings("unchecked")
    private Map<String, List<ClassOrganizationUser>> getQueueUserMap(ZbglRdpPlan plan) {
           StringBuffer hql = new StringBuffer(" select u From ClassOrganizationUser u,ClassMaintain c where c.recordStatus = 0 ");
           hql.append(" and u.classOrgIdx = c.idx and u.orgIdx = ? and c.classNo = ? ");
           // 通过上下行判断 上行正序 下行反序
           if("10".equals(plan.getToDirectionNo())){
        	   hql.append(" order by u.queueCode asc");
           }else{
        	   hql.append(" order by u.queueCode desc ");
           }
           List<ClassOrganizationUser> lists = (List<ClassOrganizationUser>)this.daoUtils.find(hql.toString(), new Object[]{plan.getWorkTeamID(),plan.getClassNo()});
           Map<String, List<ClassOrganizationUser>> map = new LinkedHashMap<String, List<ClassOrganizationUser>>();
           for (ClassOrganizationUser user : lists) {
               if(map.containsKey(user.getQueueCode())){
                   map.get(user.getQueueCode()).add(user);
               }else{
                   List<ClassOrganizationUser> list = new ArrayList<ClassOrganizationUser>();
                   list.add(user);
                   map.put(user.getQueueCode(),list);
               }
           }
           return map ;
    }

    /**
     * <li>说明：批量确认货车列检
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idArray
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void bacthCompleteRecordHC(String[] idArray) throws BusinessException, NoSuchFieldException {
        for (String idx : idArray) {
            ZbglRdpPlanRecord record = this.getModelById(idx);
            // 设置计划启动人
            OmEmployee employee = SystemContext.getOmEmployee();
            if(employee != null){
                record.setStartPersonIdx(employee.getEmpid()+"");
                record.setStartPersonName(employee.getEmpname());
            }  
            record.setRdpRecordStatus(ZbglRdpPlanRecord.STATUS_HANDLED);
            record.setRdpEndTime(new Date());
            this.saveOrUpdate(record);
        }
    }

    /**
     * <li>说明：客车库列检专业查询（按专业）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param planIdx 计划ID
     * @return
     */
    public Page<ZbglRdpPlanWiBean> findRdpWis(String planIdx) {
        String sql = SqlMapUtil.getSql("kny-base:findRdpWis")
        .replace("#planIdx#", planIdx);
        String totalSql = "Select count(*) as rowcount "+ sql.substring(sql.indexOf("from"));
        return this.queryPageList(totalSql, sql, 0, 10000, false, ZbglRdpPlanWiBean.class);
    }

    /**
     * <li>说明：客车库根据专业查询车辆查询（按专业）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param planIdx   列检计划
     * @param wiIdx     
     * @return
     */
    public Page<ZbglRdpPlanRecordBean> findRecordByWi(String planIdx, String wiIdx) {
        String sql = SqlMapUtil.getSql("kny-base:findRecordByWi")
        .replace("#planIdx#", planIdx)
        .replace("#wiIdx#", wiIdx);
        String totalSql = "Select count(*) as rowcount "+ sql.substring(sql.indexOf("from"));
        return this.queryPageList(totalSql, sql, 0, 10000, false, ZbglRdpPlanRecordBean.class);
    }

    /**
     * <li>说明：专业下批量保存车辆数据
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idArray 车辆ID数组
     * @param wiIdx 专业ID
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void saveZbglRdpWiDatas(String[] idArray, String wiIdx,Long operatorid) throws BusinessException, NoSuchFieldException {
        for (String idx : idArray) {
            ZbglRdpPlanRecord record = this.getModelById(idx);
            ZbglRdpWi wi = zbglRdpWiManager.getZbglRdpWiByRdp(record.getRdpIdx(), wiIdx);
            if (wi == null){
                continue ;
            }
            wi = zbglRdpWiManager.buildHandleEntity(operatorid, wi);
            zbglRdpWiManager.saveOrUpdate(wi);
            // 如果该车辆下的所有专业都确认完成，则完成车辆
            List noCompleteList = zbglRdpWiManager.getAllRdpWiNoCompleteListByRdp(record.getRdpIdx());
            if(noCompleteList != null && noCompleteList.size() > 0){
                continue ;
            }
            record.setRdpRecordStatus(ZbglRdpPlanRecord.STATUS_HANDLED);
            record.setRdpEndTime(new Date());
            ZbglRdp rdp = zbglRdpManager.getModelById(record.getRdpIdx());
            rdp.setRdpEndTime(new Date());
            rdp.setRdpStatus(ZbglRdp.STATUS_HANDLED);
            zbglRdpManager.update(rdp);
            this.update(record);
        }
    }
    
    /**
     * <li>说明：同一车辆下批量保存专业数据（按车辆）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-7-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject{
            recordIdx:"8a8284c35cf16978015cf1b67ca50026",
            wiIdxs:["8a8284c35cf1f528015cf261c39d00f9","8a8284c35cf1f528015cf261c39f00fa"],
            operatorId: "1039"
       }
     * @return
     * @throws IOException
     */      
    public void saveZbglRecordDatas(String[] idArray, String recordIdx,Long operatorid) throws BusinessException, NoSuchFieldException {
        ZbglRdpPlanRecord record = this.getModelById(recordIdx);
        if(record == null){
            return ;
        }
        for (String idx : idArray) {
            ZbglRdpWi wi = zbglRdpWiManager.getZbglRdpWiByRdp(record.getRdpIdx(), idx);
            if (wi == null){
                continue ;
            }
            wi = zbglRdpWiManager.buildHandleEntity(operatorid, wi);
            zbglRdpWiManager.saveOrUpdate(wi);
        }
        // 如果该车辆下的所有专业都确认完成，则完成车辆
        List noCompleteList = zbglRdpWiManager.getAllRdpWiNoCompleteListByRdp(record.getRdpIdx());
        if(noCompleteList != null && noCompleteList.size() > 0){
            return ;
        }
        record.setRdpRecordStatus(ZbglRdpPlanRecord.STATUS_HANDLED);
        record.setRdpEndTime(new Date());
        ZbglRdp rdp = zbglRdpManager.getModelById(record.getRdpIdx());
        rdp.setRdpEndTime(new Date());
        rdp.setRdpStatus(ZbglRdp.STATUS_HANDLED);
        zbglRdpManager.update(rdp);
        this.update(record);        
    }    
    
    

    /**
     * <li>说明：车辆列表查询（按车辆）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-7-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param planIdx
     * @return
     */
    public Page<ZbglRdpPlanRecordListBean> findRecordsForKC(String planIdx) {
        String sql = SqlMapUtil.getSql("kny-base:findRecordsForKC")
        .replace("#planIdx#", planIdx);
        String totalSql = "Select count(*) as rowcount "+ sql.substring(sql.indexOf("from"));
        return this.queryPageList(totalSql, sql, 0, 10000, false, ZbglRdpPlanRecordListBean.class);
    }
}
