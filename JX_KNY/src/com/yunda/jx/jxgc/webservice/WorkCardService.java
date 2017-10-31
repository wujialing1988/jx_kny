package com.yunda.jx.jxgc.webservice;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;
import com.yunda.jx.jxgc.base.jcqcitemdefine.entity.JCQCItemDefine;
import com.yunda.jx.jxgc.producttaskmanage.entity.DetectResult;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCardHandle;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkTask;
import com.yunda.jx.jxgc.producttaskmanage.entity.Worker;
import com.yunda.jx.jxgc.producttaskmanage.manager.DetectResultManager;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkCardManager;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkTaskManager;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkerManager;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCParticipant;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResult;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResultVO;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QualityControlCheckInfoVO;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCParticipantManager;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCResultManager;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCResultQueryManager;
import com.yunda.jx.jxgc.repairrequirement.entity.WorkStepResult;
import com.yunda.jx.jxgc.repairrequirement.manager.WorkStepResultManager;
import com.yunda.jx.jxgc.workplanmanage.manager.JobProcessNodeQueryManager;
import com.yunda.jx.jxgc.workplanmanage.manager.TrainWorkPlanQueryManager;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.jx.webservice.stationTerminal.base.StationTerminalWS;
import com.yunda.jx.webservice.stationTerminal.base.entity.DataItemBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.ProcessTaskListBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.RdpBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.SystemCharBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.WaitHandleBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.WorkCardBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.WorkTaskBean;
import com.yunda.jxpz.systemchar.entity.SystemChar;
import com.yunda.jxpz.systemchar.manager.SystemCharManager;
import com.yunda.util.BeanUtils;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 作业工单webservice接口实现类
 * <li>创建人：程锐
 * <li>创建日期：2015-7-15
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
@Service(value = "workCardService")
public class WorkCardService implements IWorkCardService {
    
    /**
     * 日志对象
     */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：获取待处理任务
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param operatorid 操作员ID
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 待处理任务列表
     */
    @SuppressWarnings("unchecked")
    public String waitHandle(String searchJson, Long operatorid, int start, int limit) {
        start--;// 传过来的第一页为1，而查询需要从0开始
        OmEmployee emp = getOmEmployeeManager().findOmEmployee(operatorid);
        try {
            String json = "{\"status\":\"#[t]#" + WorkCard.STATUS_HANDLING + "\"" + searchJson + "}";
            Page<WorkCard> page = getWorkCardManager().findWorkCardByWork(json, start * limit, limit, null, emp.getEmpid());
            List<WaitHandleBean> list = new ArrayList<WaitHandleBean>();
            list = BeanUtils.copyListToList(WaitHandleBean.class, page.getList());
            Page pageList = new Page(page.getTotal(), list);
            return JSONUtil.write(pageList.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：获取作业工单信息
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 作业工单IDX
     * @param operatorid 操作者ID
     * @param isDisplayInQC 是否在质量检查时显示
     * @return 作业工单信息
     */
    public String workCard(String idx,Long operatorid, Boolean isDisplayInQC) {
        try {
            WorkCardManager m = getWorkCardManager();
            WorkCard workCard = m.getModelById(idx);
            String[] val = m.getInfo(idx);
            workCard.setTransinTimeStr(val[0]);
            workCard.setPlanTrainTimeStr(val[1]);
            workCard.setRepairActivityTypeName("检修活动");
            workCard.setRepairActivityName(val[3]);
            workCard.setNodeCaseName(val[3]);
            if (!isDisplayInQC) {
//              设置系统用户信息
                SystemContextUtil.setSystemInfoByOperatorId(operatorid);
                
                //由于工位终端无法过滤当前登录人员
                //获取系统当前人员的emp对象
                OmEmployee emp = SystemContext.getOmEmployee();
                String workIDArray = workCard.getWorkerID();
                if (StringUtils.isNotBlank(workIDArray)) {
                    
//                  判断是否是有逗号
                    String a = String.valueOf(emp.getEmpid() + ",");
                    if (workIDArray.contains(a)) {
                        workCard.setWorkerID(workIDArray.replace(a, ""));
                    }else if(workIDArray.contains(String.valueOf(emp.getEmpid()))){
                        workCard.setWorkerID(workIDArray.replace(String.valueOf(emp.getEmpid()), ""));
                    }
                    
                }
                String workNameArray = workCard.getWorker();
                if (StringUtils.isNotBlank(workNameArray)) {
                    //判断是否是有逗号
                    String b = String.valueOf(emp.getEmpname() + ",");
                    if (workNameArray.contains(b)) {
                        workCard.setWorker(workNameArray.replace(b, ""));
                    }else if(workNameArray.contains(String.valueOf(emp.getEmpname()))){
                        workCard.setWorker(workNameArray.replace(String.valueOf(emp.getEmpname()), ""));
                    }
                }
            }
            

            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            
            if (workCard.getRealBeginTime() != null) {
                workCard.setRealBeginTimeStr(sdf.format(workCard.getRealBeginTime()));
            }
            if (workCard.getRealEndTime() != null) {
                workCard.setRealEndTimeStr(sdf.format(workCard.getRealEndTime()));
            }
            /** 封装方法： */
            WorkCardBean workCardBean = new WorkCardBean();
            BeanUtils.copyProperties(workCardBean, workCard);
            workCardBean.setNodeCaseName(val[3]);
            return JSONUtil.write(workCardBean);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：获取作业任务信息
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：何涛
     * <li>修改日期：2016-04-14
     * <li>修改内容：优化代码
     * @param operatorid 操作员主键
     * @param workCardIdx 作业工单主键
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @param mode 标识是未处理还是已处理的作业任务；1表示已处理。
     * @return 作业任务信息
     */
    @SuppressWarnings("unchecked")
    public String workTask(Long operatorid, String workCardIdx, int start, int limit, String mode) {
        start--;    // 传过来的第一页为1，而查询需要从0开始
        try {
            String[] statusArray = new String[] { WorkTask.STATUS_WAITINGFORGET, WorkTask.STATUS_INIT };
            if ("1".equals(mode)) {
                // 查询已处理
                statusArray = new String[] { WorkTask.STATUS_HANDLED };
            }
            /** 返回结果包装：将查询对象page中的集合数据用指定的WorkTaskBean对象进行封装 */
            Page<WorkTaskBean> page = getWorkTaskManager().pageQuery2(workCardIdx, statusArray, start * limit, limit, null);
            return JSONUtil.write(page.extjsStore()); // 返回构造结果
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：获取数据项
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workTaskIdx 作业任务主键
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 数据项
     */
    @SuppressWarnings("unchecked")
    public String dataItem(String workTaskIdx, int start, int limit) {
        start--;
        try {
            if (workTaskIdx == null) {
                workTaskIdx = "-1";
            }
            DetectResult entity = new DetectResult();
            entity.setWorkTaskIDX(workTaskIdx);
            entity.setRecordStatus(0);
            SearchEntity<DetectResult> searchEntity = new SearchEntity<DetectResult>(entity, start * limit, limit, null);
            Page<DetectResult> page = getDetectResultManager().findPageList(searchEntity);
            List<DataItemBean> dataItemList = new ArrayList<DataItemBean>();
            dataItemList = BeanUtils.copyListToList(DataItemBean.class, page.getList());
            Page<DataItemBean> pageList = new Page<DataItemBean>(page.getTotal(), dataItemList);
            return JSONUtil.write(pageList.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：获取作业结果列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workStepIDX 作业任务主键
     * @return 作业结果列表
     */
    public String getResultList(String workStepIDX) {
        try {
            List<WorkStepResult> list = getWorkStepResultManager().getListByWorkStep(workStepIDX);
            return JSONUtil.write(list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：获取系统特殊字符列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 系统特殊字符列表
     */
    @SuppressWarnings("unchecked")
    public String getSystemCharList(int start, int limit) {
        start--;// 传过来的第一页为1，而查询需要从0开始
        Map map = new HashMap();
        try {
            QueryCriteria<SystemChar> query = new QueryCriteria<SystemChar>();
            query.setEntityClass(SystemChar.class);
            List<Condition> whereList = new ArrayList<Condition>();
            query.setWhereList(whereList);
            query.setStart(start * limit);
            query.setLimit(limit);
            Page page = getSystemCharManager().findPageList(query);
            /** 返回值封装 */
            List<SystemCharBean> systemCharList = new ArrayList<SystemCharBean>();
            if (page.getTotal() > 0 && page.getList().size() > 0) {
                systemCharList = BeanUtils.copyListToList(SystemCharBean.class, page.getList());
                Page<SystemCharBean> systemPage = new Page<SystemCharBean>(page.getTotal(), systemCharList);
                map = systemPage.extjsStore();
            }
            return JSONUtil.write(map);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：录入数据项并完成作业任务
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员ID
     * @param data 操作数据数组(字符串数组，字符串格式为“idx值,结果值”，例如：“aa,22”。第一个值是数据项id，第二个值是数据项结果值)
     * @param taskIdx 作业任务主键
     * @param result 检修结果字段值
     * @param remarks 检修备注字段值
     * @return 操作成功与否
     */
    public String inputDataItem(Long operatorid, String[] data, String taskIdx, String result, String remarks) {
        try {
            String[] item = null;
            AcOperator ac = getAcOperatorManager().getModelById(operatorid);
            SystemContext.setAcOperator(ac);
            for (int i = 0; i < data.length; i++) {
                item = data[i].split("\\,", 2);
                getDetectResultManager().updateDetectResult(item[0], item[1], operatorid);
            }
            WorkTaskManager m = getWorkTaskManager();
            WorkTask t = m.getModelById(taskIdx);
            t.setRepairResult(result);
            t.setRemarks(remarks);
            t.setUpdator(operatorid);
            t.setStatus(WorkTask.STATUS_HANDLED);
            t.setUpdateTime(new Date());
            m.update(t);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：录入单个数据项
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员ID
     * @param idx 数据项主键
     * @param value 数据项录入值
     * @return 操作成功与否
     */
    public String inputSingleDataItem(Long operatorid, String idx, String value) {
        try {
            getDetectResultManager().updateDetectResult(idx, value, operatorid);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：根据作业工单idx获取需要指派的质量检查项列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDXS 作业工单idx，多个idx用,分隔
     * @return 需要指派的质量检查项列表
     */
    public String getIsAssignCheckItems(String workCardIDXS) {
        try {
            List<QCResult> list = getQCResultManager().getIsAssignCheckItems(workCardIDXS);
            return JSONUtil.write(list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：获取作业工单的其他作业人员列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 单条作业工单idx
     * @param operatorid 操作者ID
     * @return 其他作业人员列表
     */
    public String getOtherWorkerByWorkCard(String workCardIDX, Long operatorid) {
        try {
            OmEmployee emp = getOmEmployeeManager().findOmEmployee(operatorid);
            List<Worker> list = getWorkerManager().getOtherWorkerByWorkCard(workCardIDX, emp.getEmpid() + "");
            return JSONUtil.write(list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：单条或批量完工
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员ID
     * @param idxs workCardIDX，作业工单主键之间用英文逗号分隔例如：“1,2,3”
     * @param remarks 备注
     * @param qcResults 质检项列表：[{ checkItemCode 质检项编码 qcEmpID 质检人员id }]
     * @param otherWorkerIDS 其他处理人员的id字符串（,分隔的id字符串）
     * @param partsNo 配件编号
     * @return 操作成功与否
     */
    public String finishWorkCard(Long operatorid, String idxs, String remarks, String qcResults, String otherWorkerIDS) {
        try {
            if (StringUtil.isNullOrBlank(idxs)) {
                return "{'flag':'false','message':'未选择作业工单！'}";
            }
            OmEmployee emp = getOmEmployeeManager().findOmEmployee(operatorid);
            AcOperator ac = getAcOperatorManager().getModelById(operatorid);
            WorkCardManager m = getWorkCardManager();
            idxs = m.filterWorkCard(idxs, emp.getEmpid().toString()); // 过滤不能完工的作业工单
            if (StringUtil.isNullOrBlank(idxs)) {
                return "{'flag':'false','message':'当前无可批量完工的工单！'}";
            }
            SystemContext.setAcOperator(ac);
            QCResultVO[] result = null;
            if (!StringUtil.isNullOrBlank(qcResults))
                result = JSONUtil.read(qcResults, QCResultVO[].class);
            WorkCardHandle workCardEntity = new WorkCardHandle();
            workCardEntity.setIdx(idxs);
            workCardEntity.setRealEndTime(new Date());
            workCardEntity.setWorkerID(otherWorkerIDS);
            m.updateCompleteWorkCard(workCardEntity, emp.getEmpid(), result);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：作业工单查询条件查询作业计划单
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员ID
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 作业计划单列表
     */
    @SuppressWarnings("unchecked")
    public String getWorkPlanByWorkQuery(Long operatorid, int start, int limit) {
        OmEmployee emp = getOmEmployeeSelectManager().findEmpByOperator(operatorid);
        SystemContext.setOmEmployee(emp);
        start--;
        Map<String, Object> map = getTrainWorkPlanQueryManager().getComboDataByWorkCardQuery(start, limit);
        // 封装返回值
        List<RdpBean> beanList = new ArrayList<RdpBean>();
        try {
            beanList = BeanUtils.copyListToList(RdpBean.class, (List) map.get("root"));
            Page pageList = new Page((Integer) (map.get("totalProperty")), beanList);
            return JSONUtil.write(pageList.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：作业工单查询条件查询流程节点
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员ID
     * @param workPlanIDX 作业计划IDX
     * @param parentIdx 父节点主键
     * @return 流程节点列表
     */
    public String getNodeByWorkQuery(Long operatorid, String workPlanIDX, String parentIdx) {
        try {
            OmEmployee emp = getOmEmployeeSelectManager().findEmpByOperator(operatorid);
            SystemContext.setOmEmployee(emp);
            List<HashMap> list = getJobProcessNodeQueryManager().findNodeTreeByWorkQuery(parentIdx, workPlanIDX);        
            return JSONUtil.write(list);
        } catch (IOException e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>方法名称：getQCList
     * <li>方法说明：获取质检列表  
     * <li>@param uname
     * <li>@param start
     * <li>@param limit
     * <li>@param mode
     * <li>@param queryString
     *  {
     *      "rdpIDX":""
     *      "workItemName":"",
     *      "taskDepict":""
     *  }
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-5-5 下午06:32:11
     * <li>修改人：程锐
     * <li>修改内容：添加queryString查询字符串按工作项名称、车型、车号、任务描述等模糊查询
     * <li>修改人：王治龙
     * <li>修改内容：封装返回结果 2013年12月24日
     */
    public String getQCList(String uname, int start, int limit, String mode, String queryString){
        start--;
        try {
            return JSONUtil.write(getQCResultQueryManager().getQCPageList(uname, start * limit, limit, mode, queryString).extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>方法名：saveQualityControlCheckInfo
     * <li>
     * @param jsonData 检验信息Json对象 
     * {
     *      checkPersonName
     *      checkPersonIdx operatorid
     *      checkTime
     *      remarks
     * }
     * 
     * <li>
     * @param listBeanData 检验信息Json对象
     * [{
     *      sourceIdx
     *      checkItemCode
     * }]
     * @return
     * <li>返回类型：String
     * <li>说明：三检一验任务确认，保存检验信息
     * <li>创建人：程梅
     * <li>创建日期：2013-5-5
     * <li>修改人： 王治龙
     * <li>修改日期：2013-12-26 封装返回值
     */
    public String saveQualityControlCheckInfo(String jsonData, String listBeanData) {       
        QualityControlCheckInfoVO checkInfo;
        try {            checkInfo = JSONUtil.read(jsonData, QualityControlCheckInfoVO.class);
            OmEmployee emp = getOmEmployeeManager().findOmEmployee(Long.parseLong(checkInfo.getCheckPersonIdx()));
            AcOperator ac = getAcOperatorManager().getModelById(Long.parseLong(checkInfo.getCheckPersonIdx()));
            SystemContext.setAcOperator(ac);
            checkInfo.setCheckPersonIdx(emp.getEmpid().toString());
            ProcessTaskListBean[] listBean = JSONUtil.read(listBeanData, ProcessTaskListBean[].class);
            getQCResultManager().updateFinishQCResult(checkInfo, listBean);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        } 
    }
    
    /**
     * <li>方法说明：批量完工质量检验
     * <li>方法名称：batchAllQC
     * <li>@param operatorid
     * <li>@param remarks
     * <li>@param queryString
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-10-28 下午06:49:58
     * <li>修改人：王斌
     * <li>修改时间：2013-12-25 下午13:43:34
     * <li>修改内容：修改返回值格式为json格式
     */
    public String batchAllQC(Long operatorid, String remarks, String queryString){
        try {
            AcOperator ac = getAcOperatorManager().findLoginAcOprator(operatorid);
            OmEmployee emp = getOmEmployeeManager().findOmEmployee(operatorid);
            SystemContext.setAcOperator(ac);            
            List<ProcessTaskListBean> listBean = getQCResultQueryManager().getQCList(emp.getEmpid(), JCQCItemDefine.CONST_INT_CHECK_WAY_BJ + "", queryString);         
            getQCResultManager().updateAllFinishQCResult(emp.getEmpid().toString(), emp.getEmpname(), new Date(), remarks, queryString, listBean);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>方法说明：质量检查-查询生产任务单 
     * <li>方法名称：getRdpByQC
     * <li>@param operatorid
     * <li>@param start
     * <li>@param limit
     * <li>@param mode
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-10-30 下午04:04:07
     * <li>修改人： 王治龙
     * <li>修改内容：封装返回值 2013-12-30
     */
    @SuppressWarnings("unchecked")
    public String getRdpByQC(Long operatorid, int start, int limit, int mode){
        AcOperator ac = getAcOperatorManager().findLoginAcOprator(operatorid);
        SystemContext.setAcOperator(ac);
        start--;
        try {
            Map<String, Object> map = null;
            if(mode == JCQCItemDefine.CONST_INT_CHECK_WAY_BJ){    //工单质检
                map = getQCResultQueryManager().getRdpOfQuery(getStationTerminalWS().getEmployee(operatorid), JCQCItemDefine.CONST_INT_CHECK_WAY_BJ + "", start, limit);
            }else if(mode == JCQCItemDefine.CONST_INT_CHECK_WAY_CJ){    //工单异步质检
                map = getQCResultQueryManager().getRdpOfQuery(getStationTerminalWS().getEmployee(operatorid), JCQCItemDefine.CONST_INT_CHECK_WAY_CJ + "", start, limit);
            }
            //封装返回值
            List<RdpBean> beanList = new ArrayList<RdpBean>();
            beanList = BeanUtils.copyListToList(RdpBean.class, (List)map.get("root"));
            Page pageList = new Page((Integer)(map.get("totalProperty")),beanList);
            return JSONUtil.write(pageList.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e,logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：查询已处理作业工单分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-8-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON
     * @param operatorid 操作员ID
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 已处理作业工单分页列表
     */
    @SuppressWarnings("unchecked")
    public String queryCompleteList(String searchJson, Long operatorid, int start, int limit) {
        start--;// 传过来的第一页为1，而查询需要从0开始
        
        //保存系统登录信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorid);
        
        try {
            
            SearchEntity<WorkCard> searchEntity = new SearchEntity<WorkCard>(null, limit * start, limit, null);
            Page<com.yunda.jx.jxgc.producttaskmanage.entity.WorkCardBean> page = getWorkCardManager().findFinishedWorkCard(searchEntity);
           
            List<WaitHandleBean> list = new ArrayList<WaitHandleBean>();
            
            for (com.yunda.jx.jxgc.producttaskmanage.entity.WorkCardBean wcb : page.getList()) {
                WaitHandleBean whb = new WaitHandleBean();
                
                whb.setIdx(wcb.getIdx());
                whb.setTrainSortName(wcb.getTrainSortName());
                whb.setTrainNo(wcb.getTrainNo());
//                whb.setFixPlaceFullName(wcb.getFixPlaceFullName());
                whb.setRepairClassRepairTime(wcb.getRepairClassRepairTime());
                whb.setNodeCaseName(wcb.getNodeCaseName());
                whb.setWorkCardName(wcb.getWorkCardName());
                
                list.add(whb);
            }
            
//            String json = "{\"status\":\"#[t]#" + WorkCard.STATUS_HANDLED + "\"" + searchJson + "}";
//            Page<WorkCard> page = getWorkCardManager().findWorkCardByWork(json, start * limit, limit, null, emp.getEmpid());
//            list = BeanUtils.copyListToList(WaitHandleBean.class, page.getList());
            Page pageList = new Page(page.getTotal(), list);
            return JSONUtil.write(pageList.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：作业工单显示检测检修项目List列表
     * <li>创建人：林欢
     * <li>创建日期：2016-4-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员主键
     * @param workCardIdx 作业工单主键
     * @param workTaskIdx 作业任务主键
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 检测检修项目List json对象
     * Json数据模拟：

       [{
            idx:’FB2F86C0C566487C8A5D65C89EE6D253’,
            workTaskName:尺寸检查2,
            repairStandard:检查防跳作用并测量前端防跳间隙；测量前端车钩中心距轨面的高度；测量前端车钩开度；测量钩尾间隙；检查尾框厚度尺寸,
            workStepIDX:402886814d8c3bbe014d8f7cbe8f0bdd,
            repairResult:null,
            remarks:null,
            repairContent:null,
            repairMethod:null,
            status:’INITIALIZE’,
            workTaskCode:’ZYX-000583’，
            dataItemList:[{
                detectItemContent:’eee’,
                isNotBlank:’0’,
                detectResulttype:”sd”,
                detectResult:”22”,
                Idx:”8a8283a84f4e3c1b014f4e418d090003”,
                detectItemStandard:”xcvxcvcvxvxc”,
                minResult:”null”,
                maxResult:”null”
             },{
                detectItemContent:’eee’,
                isNotBlank:’0’,
                detectResulttype:”sd”,
                detectResult:”22”,
                Idx:”8a8283a84f4e3c1b014f4e418d090003”,
                detectItemStandard:”xcvxcvcvxvxc”,
                minResult:”null”,
                maxResult:”null”
            }]
        },{
            idx:’FB2F86C0C566487C8A5D65C89EE6D253’,
            workTaskName:尺寸检查2,
            repairStandard:检查防跳作用并测量前端防跳间隙；测量前端车钩中心距轨面的高度；测量前端车钩开度；测量钩尾间隙；检查尾框厚度尺寸,
            workStepIDX:402886814d8c3bbe014d8f7cbe8f0bdd,
            repairResult:null,
            remarks:null,
            repairContent:null,
            repairMethod:null,
            status:’INITIALIZE’,
            workTaskCode:’ZYX-000583’，
            dataItemList:[{
                detectItemContent:’eee’,
                isNotBlank:’0’,
                detectResulttype:”sd”,
                detectResult:”22”,
                Idx:”8a8283a84f4e3c1b014f4e418d090003”,
                detectItemStandard:”xcvxcvcvxvxc”,
                minResult:”null”,
                maxResult:”null”
             },{
                detectItemContent:’eee’,
                isNotBlank:’0’,
                detectResulttype:”sd”,
                detectResult:”22”,
                Idx:”8a8283a84f4e3c1b014f4e418d090003”,
                detectItemStandard:”xcvxcvcvxvxc”,
                minResult:”null”,
                maxResult:”null”
                maxResult:”23”
            }]
        }]
     */
    @SuppressWarnings("unchecked")
    public String getWorkTaskAndDetectResultList(Long operatorid, String workCardIdx,String workTaskIdx, int start, int limit) {
        OperateReturnMessage message = new OperateReturnMessage();
        try {
            Page page = this.getWorkCardManager().getWorkTaskAndDetectResultList(operatorid,workCardIdx,workTaskIdx,start,limit);
            return JSONUtil.write(page.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            message.setFaildFlag(e.getMessage());
            return JSONObject.toJSONString(message);
        }
    }
    
    /**
     * (已处理)FIXME 代码审查[何涛2016-04-12]：过分定制的方法参数，无法实现多个客户端调用的方法复用
     * <li>说明：单条提交数据
     * <li>创建人：林欢
     * <li>创建日期：2016-4-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param String jsonObject
     * 
     * json:{
     *  operatorid : "1212121",
     *  idxs:"asdfs11212",
     *  qcResults："[{ checkItemCode 质检项编码 qcEmpID 质检人员id }]",
     *  otherWorkerIDS:"asfasdf,innxhd"
     *  dataList:[{
            idx:’FB2F86C0C566487C8A5D65C89EE6D253’,
            workTaskName:尺寸检查2,
            repairStandard:检查防跳作用并测量前端防跳间隙；测量前端车钩中心距轨面的高度；测量前端车钩开度；测量钩尾间隙；检查尾框厚度尺寸,
            workStepIDX:402886814d8c3bbe014d8f7cbe8f0bdd,
            repairResult:null,
            remarks:null,
            repairContent:null,
            repairMethod:null,
            status:’INITIALIZE’,
            workTaskCode:’ZYX-000583’，
            dataItemList:[{
                detectItemContent:’eee’,
                isNotBlank:’0’,
                detectResulttype:”sd”,
                detectResult:”22”,
                Idx:”8a8283a84f4e3c1b014f4e418d090003”,
                detectItemStandard:”xcvxcvcvxvxc”,
                minResult:”null”,
                maxResult:”null”
             },{
                detectItemContent:’eee’,
                isNotBlank:’0’,
                detectResulttype:”sd”,
                detectResult:”22”,
                Idx:”8a8283a84f4e3c1b014f4e418d090003”,
                detectItemStandard:”xcvxcvcvxvxc”,
                minResult:”null”,
                maxResult:”null”
            }]
        },{
            idx:’FB2F86C0C566487C8A5D65C89EE6D253’,
            workTaskName:尺寸检查2,
            repairStandard:检查防跳作用并测量前端防跳间隙；测量前端车钩中心距轨面的高度；测量前端车钩开度；测量钩尾间隙；检查尾框厚度尺寸,
            workStepIDX:402886814d8c3bbe014d8f7cbe8f0bdd,
            repairResult:null,
            remarks:null,
            repairContent:null,
            repairMethod:null,
            status:’INITIALIZE’,
            workTaskCode:’ZYX-000583’，
            dataItemList:[{
                detectItemContent:’eee’,
                isNotBlank:’0’,
                detectResulttype:”sd”,
                detectResult:”22”,
                Idx:”8a8283a84f4e3c1b014f4e418d090003”,
                detectItemStandard:”xcvxcvcvxvxc”,
                minResult:”null”,
                maxResult:”null”
             },{
                detectResult:”22”,
                Idx:”8a8283a84f4e3c1b014f4e418d090003”,
            }]
        }]
     * }
     * 
     * @param operatorid 操作员ID
     * @param idxs 作业工单主键
     * @param qcResults 质检项列表：[{ checkItemCode 质检项编码 qcEmpID 质检人员id }]
     * @param otherWorkerIDS 其他处理人员的id字符串（,分隔的id字符串）
     * @return 操作成功与否
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    public String completeWorkCard(String jsonObject) {
        
        OperateReturnMessage message = new OperateReturnMessage();
        try {
            Map<String, Object> map = WorkCardSaveOrCompStringToObejct(jsonObject);
            this.getWorkCardManager().finishWorkCard((WorkCardHandle)map.get("entity"), (QCResultVO[])map.get("result"), (WorkTaskBean[])map.get("workTaskAndDetects"));
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            message.setFaildFlag(e.getMessage());
        }
        return JSONObject.toJSONString(message);
        
        
    }
    
    /**
     * (已处理)FIXME 代码审查[何涛2016-04-12]：过分定制的方法参数，无法实现多个客户端调用的方法复用
     * <li>说明：单条缓存数据
     * <li>创建人：林欢
     * <li>创建日期：2016-4-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param String jsonObject
     * 
     * json:{
     *  operatorid : "1212121",
     *  idxs:"asdfs11212",
     *  qcResults："[{ checkItemCode 质检项编码 qcEmpID 质检人员id }]",
     *  otherWorkerIDS:"asfasdf,innxhd"
     *  dataList:[{
            idx:’FB2F86C0C566487C8A5D65C89EE6D253’,
            workTaskName:尺寸检查2,
            repairStandard:检查防跳作用并测量前端防跳间隙；测量前端车钩中心距轨面的高度；测量前端车钩开度；测量钩尾间隙；检查尾框厚度尺寸,
            workStepIDX:402886814d8c3bbe014d8f7cbe8f0bdd,
            repairResult:null,
            remarks:null,
            repairContent:null,
            repairMethod:null,
            status:’INITIALIZE’,
            workTaskCode:’ZYX-000583’，
            dataItemList:[{
                detectItemContent:’eee’,
                isNotBlank:’0’,
                detectResulttype:”sd”,
                detectResult:”22”,
                Idx:”8a8283a84f4e3c1b014f4e418d090003”,
                detectItemStandard:”xcvxcvcvxvxc”,
                minResult:”null”,
                maxResult:”null”
             },{
                detectItemContent:’eee’,
                isNotBlank:’0’,
                detectResulttype:”sd”,
                detectResult:”22”,
                Idx:”8a8283a84f4e3c1b014f4e418d090003”,
                detectItemStandard:”xcvxcvcvxvxc”,
                minResult:”null”,
                maxResult:”null”
            }]
        },{
            idx:’FB2F86C0C566487C8A5D65C89EE6D253’,
            workTaskName:尺寸检查2,
            repairStandard:检查防跳作用并测量前端防跳间隙；测量前端车钩中心距轨面的高度；测量前端车钩开度；测量钩尾间隙；检查尾框厚度尺寸,
            workStepIDX:402886814d8c3bbe014d8f7cbe8f0bdd,
            repairResult:null,
            remarks:null,
            repairContent:null,
            repairMethod:null,
            status:’INITIALIZE’,
            workTaskCode:’ZYX-000583’，
            dataItemList:[{
                detectItemContent:’eee’,
                isNotBlank:’0’,
                detectResulttype:”sd”,
                detectResult:”22”,
                Idx:”8a8283a84f4e3c1b014f4e418d090003”,
                detectItemStandard:”xcvxcvcvxvxc”,
                minResult:”null”,
                maxResult:”null”
             },{
                detectResult:”22”,
                Idx:”8a8283a84f4e3c1b014f4e418d090003”,
            }]
        }]
     * }
     * 
     * @param operatorid 操作员ID
     * @param idxs 作业工单主键
     * @param qcResults 质检项列表：[{ checkItemCode 质检项编码 qcEmpID 质检人员id }]
     * @param otherWorkerIDS 其他处理人员的id字符串（,分隔的id字符串）
     * @return 操作成功与否
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    public String saveWorkCard(String jsonObject) {
        OperateReturnMessage message = new OperateReturnMessage();
        try {
            Map<String, Object> map = WorkCardSaveOrCompStringToObejct(jsonObject);
            this.getWorkCardManager().saveWorkCard((WorkCardHandle)map.get("entity"), (QCResultVO[])map.get("result"), (WorkTaskBean[])map.get("workTaskAndDetects"));
//            this.getWorkCardManager().saveWorkCard(entity, result, workTaskAndDetects);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            message.setFaildFlag(e.getMessage());
        }
        return JSONObject.toJSONString(message);
    }
    
    /**
     * <li>说明：单条提交作业工单json数据封装
     * <li>创建人：林欢
     * <li>创建日期：2016-4-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param String jsonObject json对象
     * @return 操作成功与否
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     * @throws ParseException 
     */
    private Map<String,Object> WorkCardSaveOrCompStringToObejct(String jsonObject) throws JsonParseException, JsonMappingException, IOException, ParseException{
        Map<String, Object> map = new HashMap<String, Object>();
        
        JSONObject ob = JSONObject.parseObject(jsonObject);
        
        //获取数据
        String idxs = ob.getString("idxs");//
		Long operatorid = ob.getLong("operatorid");
        String qcResults = ob.getString("qcResults");
        String otherWorkerIDS = ob.getString("otherWorkerIDS");
        String dataList = ob.getString("dataList");
        String remarks = ob.getString("remarks");
        String date = ob.getString("realBeginTime");
        String workerID = ob.getString("otherWorkerIDS");
        
        
        Date realBeginTime = null;
        //实际开工时间可能为空
        if (StringUtils.isNotBlank(date)) {
            
            try {
                realBeginTime = DateUtil.parse(date, "yyyy-MM-dd HH:mm");
            } catch (Exception e) {
                realBeginTime = DateUtil.parse(date, "yyyy/MM/dd HH:mm");
            }
            
        }else {
            realBeginTime = new Date();
        }
        
        
//        //为上下文设置操作员，否则保存的时候可能会报错
//        AcOperator ac = getAcOperatorManager().getModelById(operatorid);
//        SystemContext.setAcOperator(ac);
        
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorid);
        
        
        //质量检验结果值对象，用于记录质量检验结果信息；
        QCResultVO[] result = null;
        if (!StringUtil.isNullOrBlank(qcResults) && !"{}".equals(qcResults))
            try {
                result = JSONUtil.read(qcResults, QCResultVO[].class);
                map.put("result", result);
            } catch (Exception e) {
                throw new BusinessException("解析qcResults失败！");
            }
        
        //作业工单处理实体
        WorkCardHandle entity = new WorkCardHandle();
        entity.setIdx(idxs);
        entity.setRealEndTime(new Date());
        entity.setRealBeginTime(realBeginTime);
        entity.setWorkerID(otherWorkerIDS);
        entity.setRemarks(remarks);
        
        //添加其他处理人员
        entity.setWorkerID(workerID);
        
        map.put("entity", entity);
        
        if (StringUtil.isNullOrBlank(dataList)) {
            throw new BusinessException("检测检修项List不能为空！");
        }
        
        //作业任务信息
        WorkTaskBean[] workTaskAndDetects = null;
        try {
            
            //处理dataList字符串问题
            workTaskAndDetects = JSONUtil.read(dataList, WorkTaskBean[].class);
            map.put("workTaskAndDetects", workTaskAndDetects);
        } catch (Exception e) {
            throw new BusinessException("解析dataList失败！");// 
        }
        
        return map;
        
    }
    
    /**
     * <li>说明：获取作业工单指派的质量检查人员
     * <li>创建人：林欢
     * <li>创建日期：2016-4-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param String jsonObject 作业工单
     * @return 操作成功与否
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    public String getQCEmpsForAssign (String jsonObject){
        OperateReturnMessage message = new OperateReturnMessage();
        
        //获取数据
        try {
            //只获取已经指派过的，也就是工位终端选择了的
            List<Map<String, Object>>  list = new ArrayList<Map<String,Object>>();
            List<Map<String, Object>> listReturn = this.getQCParticipantManager().getQCEmpsForAssign(jsonObject);
            
            //获取页面显示的质检项
            List<QCResult> listQCResult = getQCResultManager().getIsAssignCheckItems(jsonObject);
            for (QCResult result : listQCResult) {
                for (Map<String, Object> map : listReturn) {
                    if (result.getCheckItemCode().equals(map.get("checkItemCode"))) {
                        
                        QCParticipant qcParticipant = this.getQCParticipantManager().getModel(jsonObject,result.getCheckItemCode());
                        map.put("checkPersonName", qcParticipant.getCheckPersonName());
                        list.add(map);
                    }
                }
            }
            return JSONUtil.write(list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            message.setFaildFlag(e.getMessage());
            return JSONObject.toJSONString(message);
        }
    }
    
    /**
     * <li>说明：通过作业工单ID返回作业工单的woker值的worker
     * <li>创建人：林欢
     * <li>创建日期：2016-5-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业工单IDX
     * @return String 拼接好的woker
     */
    public String findWorkCardWorkerByIDX(String workCardIDX) {
        OperateReturnMessage message = new OperateReturnMessage();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String wokerNames = getWorkCardManager().findWorkCardWorkerByIDX(workCardIDX);
            map.put("wokerNames", wokerNames);
            return JSONUtil.write(map);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            message.setFaildFlag(e.getMessage());
            return JSONObject.toJSONString(message);
        }
    }
    
    /**
     * <li>说明：作业工单完工接口（针对机车检修任务处理）
     * <li>创建人：张迪
     * <li>创建日期：2016-9-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象
     * @return 提示信息
     */
    public String completeWorkCardNew(String jsonObject) {       
        OperateReturnMessage message = new OperateReturnMessage();
        try {
            Map<String, Object> map = WorkCardSaveOrCompStringToObejct(jsonObject);
            this.getWorkCardManager().finishWorkCardNew((WorkCardHandle)map.get("entity"), (QCResultVO[])map.get("result"), (WorkTaskBean[])map.get("workTaskAndDetects"));
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            message.setFaildFlag(e.getMessage());
        }
        return JSONObject.toJSONString(message);
        
        
    }
    
    protected TrainWorkPlanQueryManager getTrainWorkPlanQueryManager() {
        return (TrainWorkPlanQueryManager) Application.getSpringApplicationContext().getBean("trainWorkPlanQueryManager");
    }
    
    protected QCParticipantManager getQCParticipantManager() {
        return (QCParticipantManager) Application.getSpringApplicationContext().getBean("qCParticipantManager");
    }
    
    protected JobProcessNodeQueryManager getJobProcessNodeQueryManager() {
        return (JobProcessNodeQueryManager) Application.getSpringApplicationContext().getBean("jobProcessNodeQueryManager");
    }
    
    protected OmEmployeeSelectManager getOmEmployeeSelectManager() {
        return (OmEmployeeSelectManager) Application.getSpringApplicationContext().getBean("omEmployeeSelectManager");
    }
    
    protected OmEmployeeManager getOmEmployeeManager() {
        return (OmEmployeeManager) Application.getSpringApplicationContext().getBean("omEmployeeManager");
    }
    
    protected AcOperatorManager getAcOperatorManager() {
        return (AcOperatorManager) Application.getSpringApplicationContext().getBean("acOperatorManager");
    }
    
    protected WorkCardManager getWorkCardManager() {
        return (WorkCardManager) Application.getSpringApplicationContext().getBean("workCardManager");
    }
    
    protected WorkerManager getWorkerManager() {
        return (WorkerManager) Application.getSpringApplicationContext().getBean("workerManager");
    }
    
    protected QCResultManager getQCResultManager() {
        return (QCResultManager) Application.getSpringApplicationContext().getBean("qCResultManager");
    }
    
    protected WorkTaskManager getWorkTaskManager() {
        return (WorkTaskManager) Application.getSpringApplicationContext().getBean("workTaskManager");
    }
    
    protected DetectResultManager getDetectResultManager() {
        return (DetectResultManager) Application.getSpringApplicationContext().getBean("detectResultManager");
    }
    
    protected SystemCharManager getSystemCharManager() {
        return (SystemCharManager) Application.getSpringApplicationContext().getBean("systemCharManager");
    }
    
    protected WorkStepResultManager getWorkStepResultManager() {
        return (WorkStepResultManager) Application.getSpringApplicationContext().getBean("workStepResultManager");
    }
    
    protected QCResultQueryManager getQCResultQueryManager(){
        return (QCResultQueryManager)Application.getSpringApplicationContext().getBean("qCResultQueryManager");
    }
    
    protected StationTerminalWS getStationTerminalWS(){
        return (StationTerminalWS)Application.getSpringApplicationContext().getBean("stationTerminalWS");
    }
}
