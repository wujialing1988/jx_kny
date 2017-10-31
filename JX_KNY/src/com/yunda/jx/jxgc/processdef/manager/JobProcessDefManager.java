package com.yunda.jx.jxgc.processdef.manager;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jxgc.processdef.entity.JobNodeUnionWorkSeq;
import com.yunda.jx.jxgc.processdef.entity.JobProcessDef;
import com.yunda.jx.jxgc.processdef.entity.JobProcessNodeDef;
import com.yunda.jx.jxgc.processdef.entity.JobProcessNodeRelDef;
import com.yunda.jx.jxgc.producttaskmanage.manager.GanttManager;
import com.yunda.jx.third.edo.entity.PredecessorLink;
import com.yunda.jx.third.edo.entity.Result;
import com.yunda.jx.third.edo.entity.Task;
import com.yunda.jx.third.edo.entity.WorkingTime;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JobProcessDef业务类,检修作业流程
 * <li>创建人：何涛
 * <li>创建日期：2015-4-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="jobProcessDefManager")
public class JobProcessDefManager extends JXBaseManager<JobProcessDef, JobProcessDef> {
    
    /** 甘特图展示公共业务类 */
    @Resource
    private GanttManager ganttManager;
    
    /** JobProcessNodeDef业务类 */
    @Resource
    private JobProcessNodeDefManager jobProcessNodeDefManager;
    
    /** JobProcessNodeRelDef业务类,节点前后置关系 */
    @Resource
    private JobProcessNodeRelDefManager jobProcessNodeRelDefManager;

    /** 业务类 作业节点所挂记录卡 */
    @Resource
    private JobNodeUnionWorkSeqManager jobNodeUnionWorkSeqManager;
    
    /** 甘特图特定日期格式 */
    public static final DateFormat DATE_FMT_GANTT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    
    /**
     * <li>说明：新增时作业流程编码不能重复
     * <li>创建人：何涛
     * <li>创建日期：2015-4-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t 被更新（新增）的实体对象
     * @return 错误的验证消息
     */
    @Override
    public String[] validateUpdate(JobProcessDef t) {
        List<JobProcessDef> entityList = this.finAll();
        if (null != entityList && entityList.size() > 0) {
            for (JobProcessDef pm : entityList) {
                if (pm.getIdx().equals(t.getIdx())) {
                    continue;
                }
                if (null != pm.getProcessCode() && pm.getProcessCode().equals(t.getProcessCode())) {
                    return new String[]{"作业流程编码【" +t.getProcessCode() + "】已经存在，不能重复添加！"};
                }
                // 对已作废的记录不做验证
                if (JobProcessDef.CONST_INT_STATUS_ZF == pm.getStatus().intValue()) {
                    continue;
                }
//                if (pm.getTrainTypeIDX().equals(t.getTrainTypeIDX()) && pm.getRcIDX().equals(t.getRcIDX())) {
//                    return new String[]{"不可以添加相同【车型】和【修程】的重复记录"};
//                }
            }
        }
        return null;
    }
    
    /**
     * <li>说明：获取所有有效的作业流程实体集合
     * <li>创建人：何涛
     * <li>创建日期：2015-4-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 所有有效的作业流程实体集合
     */
    private List<JobProcessDef> finAll() {
        JobProcessDef searchEntity = new JobProcessDef();
        searchEntity.setRecordStatus(Constants.NO_DELETE);
        return this.findList(searchEntity);
    }
    
    /**
     * <li>说明：只可删除状态为新增的作业流程
     * <li>创建人：何涛
     * <li>创建日期：2015-4-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 要删除de实体主键数组
     * @return String[] 错误消息数组
     */
    @Override
    public String[] validateDelete(Serializable... ids) {
        List<String> errMsg = new ArrayList<String>();
        for (Serializable idx : ids) {
            JobProcessDef entity = this.getModelById(idx);
            if (entity.getStatus().intValue() != JobProcessDef.CONST_INT_STATUS_XZ) {
                errMsg.add("作业流程[" + entity.getProcessName() + "]不可以删除；");
            }
            errMsg.add("请刷新后重试！");
        }
        if (errMsg.size() > 1) {
            return errMsg.toArray(new String[errMsg.size()]);
        }
        return super.validateDelete(ids);
    }
    
    /**
     * <li>说明：更新作业流程状态，包含【启用】【作废】
     * <li>创建人：何涛
     * <li>创建日期：2015-4-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 要更新实体主键数组
     * @param status 要更新的作业流程状态
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void updateStatus(String[] ids, int status) throws BusinessException, NoSuchFieldException {
        List<JobProcessDef> entityList = new ArrayList<JobProcessDef>();
        for (String idx : ids) {
            JobProcessDef entity = this.getModelById(idx);
            // 如果状态已经被其他人更新，则不进行处理
            int statusValue = entity.getStatus().intValue();
            if (statusValue == status) {
                continue;
            }
            // 只能启用当前状态为【新增】的流程
            if (JobProcessDef.CONST_INT_STATUS_QY == status && statusValue == JobProcessDef.CONST_INT_STATUS_ZF) {
                throw new BusinessException("作业流程[" + entity.getProcessName() + "]已经作废，请刷新后重试！");
            }
            // 只能作废当前状态为【启用】的流程
            if (JobProcessDef.CONST_INT_STATUS_ZF == status && statusValue == JobProcessDef.CONST_INT_STATUS_XZ) {
                throw new BusinessException("作业流程[" + entity.getProcessName() + "]还未启用，请刷新后重试！");
            }
            // 更新状态
            entity.setStatus(status);
            entityList.add(entity);
        }
        this.saveOrUpdate(entityList);
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
        super.logicDelete(ids);
        for (Serializable idx : ids) {
            // 级联删除下属作业流程节点
            List<JobProcessNodeDef> nodeList = this.jobProcessNodeDefManager.getModelsByProcessIDX(idx);
            if (null != nodeList && nodeList.size() > 0) {
                this.jobProcessNodeDefManager.logicDelete(nodeList);
            }
        }
    }

    /**
     * <li>说明：机车检修作业计划甘特图展示
     * <li>创建人：何涛
     * <li>创建日期：2015-5-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 机车检修作业流程主键
     * @return Result
     * @throws Exception 
     */
    public Result planOrderGantt(String idx) throws Exception {
        JobProcessDef entity = this.getModelById(idx);
        if (null == entity) {
            return null;
        }
        return this.planOrderGantt(entity);
    }
    
    /**
     * <li>说明：甘特图展示
     * <li>创建人：何涛
     * <li>创建日期：2015-5-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 检修作业流程
     * @return Result
     * @throws Exception
     */
    public Result planOrderGantt(JobProcessDef entity) throws Exception {
        Result result = new Result();
        WorkingTime[] workingTimes = ganttManager.getWorkingTimes();
        com.yunda.jx.third.edo.entity.Calendar[] calendars = ganttManager.getCalendars(workingTimes);
        Task[] tasks = getTasks(entity);                // 预计划排程任务数组
        result.setTasks(tasks);
        result.setStartDate(tasks[0].getStart());
        result.setFinishDate(tasks[0].getFinish());
        result.setUID("227");
        result.setName("检修作业计划");
        result.setWeekStartDay(2);
        result.setCalendars(calendars);
        result.setEnableDurationLimit(true);
        return result;
    }

    /**
     * <li>说明：构造甘特图-任务实体对象数组
     * <li>创建人：何涛
     * <li>创建日期：2015-5-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 检修作业流程
     * @return Task[]
     * @throws ParseException 
     */
    private Task[] getTasks(JobProcessDef entity) throws ParseException {
        Date now = java.util.Calendar.getInstance().getTime();
        List<Task> list = new ArrayList<Task>();
        Task task = new Task();
        task.setUID(1 + "");                             // UID
        task.setName(entity.getProcessName());           // 任务名称
        task.setOutlineNumber(1 + "");                   // 大纲字段:任务层次以及顺序
        list.add(task);
        // 获取机车检修作业流程的一层作业节点
        List<JobProcessNodeDef> models = this.jobProcessNodeDefManager.getModels(entity.getIdx(), "ROOT_0");
        Map<String, Task> map = new HashMap<String, Task>();
        for (int i = 0; i < models.size(); i++) {
            String outlineNumber = 1 + "." + (i + 1);
            this.jobProcessNodeDefManager.getTasks(models.get(i), outlineNumber, list, now);
            this.jobProcessNodeDefManager.getTasks(models.get(i), outlineNumber, map);
        }
        // 查询设置节点的前置节点信息
        PredecessorLink pLink = null;
        List<PredecessorLink> pList = new ArrayList<PredecessorLink>();
        // 计算检修作业流程的总工期
        int totalWorkDate = 0;
        for (int j = 1; j < list.size(); j++) {
            Task t = list.get(j);
            // 计算总工期
            if (t.getIsLastLevel().intValue() == JobProcessNodeDef.CONST_INT_IS_LEAF_YES) {
                totalWorkDate += t.getWorkDate().intValue();
            }
            // 查询设置节点的前置节点信息
            List<JobProcessNodeRelDef> nodeRelDefList = this.jobProcessNodeRelDefManager.getModelsByWPNodeIDX(t.getNodeIDX());
            if (null == nodeRelDefList || 0 >= nodeRelDefList.size()) {
                continue;
            }
            pList = new ArrayList<PredecessorLink>();
            int delayTime = 0;              // 前置任务的延搁时间
            for (JobProcessNodeRelDef nodeRelDef : nodeRelDefList) {
                pLink = new PredecessorLink();
                pLink.setType(1);
                pLink.setPredecessorUID(map.get(nodeRelDef.getPreNodeIDX()).getUID());
                pList.add(pLink);
                // 前置任务工期
                delayTime += map.get(nodeRelDef.getPreNodeIDX()).getWorkDate().intValue();
                // 前置任务延搁时间
                delayTime += 0;
                // 总工期需要加上各个节点前置节点的延搁时间值
                totalWorkDate += 0;
            }
            t.setPredecessorLink(pList.toArray(new PredecessorLink[pList.size()]));
            if (delayTime > 0) {
                Date s = DATE_FMT_GANTT.parse(t.getStart());
                Date f = DATE_FMT_GANTT.parse(t.getFinish());
                t.setStart(DATE_FMT_GANTT.format(calculateFinishDate(s, Double.valueOf(delayTime))));
                t.setFinish(DATE_FMT_GANTT.format(calculateFinishDate(f, Double.valueOf(delayTime))));
            }
        }
        list.get(0).setWorkDate(BigDecimal.valueOf(totalWorkDate));
        // 开始时间
        task.setStart(DATE_FMT_GANTT.format(now));                         
        // 结束时间
        task.setFinish(DATE_FMT_GANTT.format(calculateFinishDate(now, Double.valueOf(totalWorkDate))));       
        return list.toArray(new Task[list.size()]);
    }
    
    /**
     * <li>说明：根据开始时间和工期获取结束时间
     * <li>创建人：何涛
     * <li>创建日期：2015-5-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param startDate 开始时间
     * @param ratedWorkMinutes 额定工期（分钟）
     * @return 结束时间
     */
    public static Date calculateFinishDate(Date startDate, Double ratedWorkMinutes) {
        if (null == ratedWorkMinutes || ratedWorkMinutes.intValue() <= 0) {
            return startDate;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.MINUTE, ratedWorkMinutes.intValue());
        return calendar.getTime();
    }

    /**
     * <li>说明：保存机车作业流程节点定义
     * 业务逻辑：如果是修改车型，那么就会把该流程下所有节点下的工单全部清除（因为节点下的作业卡是根据车型选择出来的）
     * <li>创建人：林欢   
     * <li>创建日期：2016-7-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param jobProcessDef 流程定义idx
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     * @throws IOException
     */
    public void saveJobProcessDefInfo(JobProcessDef jobProcessDef) throws BusinessException, NoSuchFieldException {
        
        //业务逻辑：只有在修改流程并且修改车型的时候，才会删除流程下所有节点下挂的工序卡
        //判断是否修改
        String jobProcessDefIDX = jobProcessDef.getIdx();
        if (StringUtils.isNotBlank(jobProcessDefIDX)) {
            //通过流程idx查询数据库中车型idx
            String dbTrainTpyeIDX = this.findJobProcessDefTrainTpyeIDX(jobProcessDefIDX);
            //如果车型不相等，说明修改了车型，那么清除工单
            if (!dbTrainTpyeIDX.equals(jobProcessDef.getTrainTypeIDX())) {
                //通过流程idx查询所有节点下挂的工序卡
                List<JobNodeUnionWorkSeq> list = jobNodeUnionWorkSeqManager.findWorkSeqListByJobProcessDefIDX(jobProcessDefIDX);
                //删除中间表关系
                for (JobNodeUnionWorkSeq seq : list) {
                    jobNodeUnionWorkSeqManager.getDaoUtils().remove(seq);
                }
            }
        }
        this.saveOrUpdate(jobProcessDef);
        
    }

    /**
     * <li>说明：保存机车作业流程节点定义
     * 业务逻辑：如果是修改车型，那么就会把该流程下所有节点下的工单全部清除（因为节点下的作业卡是根据车型选择出来的）
     * <li>创建人：林欢   
     * <li>创建日期：2016-7-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param jobProcessDefIDX 流程idx
     * @return String 车型idx
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     * @throws IOException
     */
    private String findJobProcessDefTrainTpyeIDX(String jobProcessDefIDX) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select a.train_type_idx from jxgc_job_process_def a where a.idx = '").append(jobProcessDefIDX).append("'");
        return (String) this.getDaoUtils().executeSqlQuery(sb.toString()).get(0);
    }
    
    /**
     * <li>说明：TODO 方法实现功能说明
     * <li>创建人：张迪
     * <li>创建日期：2016-8-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX 车型idx
     * @param rcIDX 修程idx
     * @return 返回启用的流程
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessDef> getModelsByTrainTypeIDXAndRcIDX(String trainTypeIDX, String rcIDX) {
        String hql = "From JobProcessDef Where recordStatus = 0 And  status = 1 and trainTypeIDX = ? And rcIDX = ?";
        return this.daoUtils.find(hql, trainTypeIDX, rcIDX);
    }
    
}