package com.yunda.jx.jxgc.producttaskmanage.manager;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.jx.component.manager.OmOrganizationSelectManager;
import com.yunda.jx.jxgc.common.JxgcConstants;
import com.yunda.jx.jxgc.producttaskmanage.entity.DesignateRecord;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkTask;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jxpz.utils.SystemConfigUtil;

/**
 * <li>标题：作业工单派工业务类
 * <li>说明：
 * <li>创建人：张凡
 * <li>创建时间：2014-3-27
 * <li>修改人：
 * <li>修 改时间：
 * <li>修改内容：
 * @author PEAK-CHEUNG
 *
 */
@Service(value="dispatcher4WorkCardManager")
public class Dispatcher4WorkCardManager extends JXBaseManager<WorkCard, WorkCard> {
    
    /** 派工记录业务类 */
    @Resource
    private DesignateRecordManager designateRecordManager;
    /** 最后一次派工记录业务类 */
    @Resource
    private WorkLastDispatchManager workLastDispatchManager;
    /** 机构业务类 */
    @Resource
    private OmOrganizationSelectManager omOrganizationSelectManager;
    
    private static final String DOUBLE_QUOTE_MARK = "','";
    
    private static final String QUOTE_BRACKET_R = "')";
    
    private static final String QUOTE_MARK = "\\,";
    
    private static final String EMPID = "人员ID";
    
    private static final String WORKSTATIONIDX = "#workStationIDX#";
    
    private static final String WORKSTATIONNAME = "#workStationName#";
    
    private static final String WORKSTATIONBELONGTEAM = "#workStationBelongTeam#";
    
    private static final String WORKSTATIONBELONGTEAMNAME = "#workStationBelongTeamName#";
    
    private static final String NODECASEIDX = "#nodeCaseIDX#";
    
    private static final String STATUS_OPEN = "#STATUS_OPEN#";
    
    private static final String STATUS_HANDLING = "#STATUS_HANDLING#";
    
    private static final String STATUS_NEW = "#STATUS_NEW#";

    /**
     * <li>说明：工长派工-全部批量派工
     * <li>创建人：程锐
     * <li>创建日期：2013-11-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询json字符串
     * @param empids 人员主键字符串
     * @param operatorid 操作员id
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public void foremanAllDispatcher(String searchJson, String empids, Long operatorid) throws JsonParseException, JsonMappingException, IOException{
        String sql = getWorkCardSql(searchJson);
        List list = daoUtils.executeSqlQuery(sql);
        updateAllForemanDispater(list, empids, operatorid, sql);
    }

    /**
     * <li>说明：全部批量派工
     * <li>创建人：程锐
     * <li>创建日期：2015-7-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param list 派工列表
     * @param empids 以,号分隔的派工人员ID字符串
     * @param operatorid 操作者ID
     * @param workCardSql 获取派工的作业工单IDX的sql字符串
     */
    @SuppressWarnings("unchecked")
    public void updateAllForemanDispater(List list, String empids, Long operatorid, String workCardSql){        
        if(list != null && list.size() > 0){
            deleteWorkerByWorkCard(workCardSql);
            batchInsertWorker(empids, workCardSql);   
            updateWorkCardIsHavePerson(workCardSql);
            StringBuilder ids = getIds(list);
            designateRecordManager.saveWorkRecord(DesignateRecord.TYPE_HEADMAN, ids.toString().split(QUOTE_MARK));//新增派工记录
            WorkLastDispatchManager workLastDispatchManager = (WorkLastDispatchManager)Application.getSpringApplicationContext().getBean("workLastDispatchManager");
            workLastDispatchManager.saveLastDispatcher("'" + ids.toString().replace(Constants.JOINSTR, DOUBLE_QUOTE_MARK) + "'", empids,operatorid);
        }
    }
    
    /**
     * <li>说明：工长派工
     * <li>创建人：程锐
     * <li>创建日期：2015-7-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIdx 以,号分隔的作业工单IDX字符串
     * @param empids 以,号分隔的派工人员ID字符串
     * @param operatorid 操作者ID
     */
    public void updateForemanDispater(String workCardIdx, String empids, Long operatorid){    	
        String ids = buildWorkCardIdx(workCardIdx) ; //用于新增派工记录
        workCardIdx = ids.replace(Constants.JOINSTR, DOUBLE_QUOTE_MARK);
        updateBatchForemanDispater(workCardIdx, empids, ids, operatorid);
    }
    
    /**
     * <li>说明：默认上次派工
     * <li>创建人：张凡
     * <li>创建日期：2013-7-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idxs 以,号分隔的作业工单IDX字符串
     * @return 错误信息
     */
    @SuppressWarnings("unchecked")
    public String updateDefaultLastTimeWorker(String idxs){        
        String[] idx = idxs.split(QUOTE_MARK);
        if (idx == null || idx.length < 1)
            return "未选择作业工单";
        idxs = idxs.replace(Constants.JOINSTR, DOUBLE_QUOTE_MARK);
        OmEmployee empx = SystemContext.getOmEmployee();
        // 删除存在的作业人员,程锐添加2014-06-11避免重复派工
        deleteWorkerByWorkCard("'" + idxs + "'");        
        //查询上次作业人员及工单
        List<Object[]> list = findLastDispatch("'" + idxs + "'", empx);        
        OmOrganization org = omOrganizationSelectManager.getModelById(empx.getOrgid());
        if (list == null || list.size() < 1) {
        	return "所选数据无上次派工记录！";			
		}
        //循环新增作业人员
        for(Object[] obj : list){
            String[] empid = StringUtil.nvl(obj[0]).split(QUOTE_MARK);            
            for(int i = 0; i < empid.length; i++){
                insertDefaultLastTimeWorker(obj, org, empid[i]);
            }
        }        
        //修改作业卡是否有默认人状态
        updateWorkCardIsHavePerson("'" + idxs + "'");
        /*
         * 本来就是默认上次，所以不需要再往最后派工记录表里面新增数据
         */
        designateRecordManager.saveWorkRecord(DesignateRecord.TYPE_HEADMAN, idx);//新增派工记录
        return null;
    }
    
    /**
     * <li>说明：默认上次派工-全部派工
     * <li>创建人：程锐
     * <li>创建日期：2015-7-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询json字符串
     * @return 错误信息
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public String updateAllDefaultLastTimeWorker(String searchJson) throws JsonParseException, JsonMappingException, IOException{
        String workCardSql = getWorkCardSql(searchJson);
        List idxList = daoUtils.executeSqlQuery(workCardSql);        
        if (idxList == null || idxList.size() < 1)
            return "未选择作业工单";
        String[] idx = new String[idxList.size()];
        for (int i = 0; i < idxList.size(); i++) {
            idx[i] = idxList.get(i).toString();
        }
        OmEmployee empx = SystemContext.getOmEmployee();
        deleteWorkerByWorkCard(workCardSql);        
        //查询上次作业人员及工单
        List<Object[]> list = findLastDispatch(workCardSql, empx);        
        OmOrganization org = omOrganizationSelectManager.getModelById(empx.getOrgid());
        if (list == null || list.size() < 1) {
            return "所选数据无上次派工记录";          
        }
        StringBuilder workCardIDXS = new StringBuilder();
        //循环新增作业人员
        for(Object[] obj : list){
            String[] empid = StringUtil.nvl(obj[0]).split(QUOTE_MARK);   
            workCardIDXS.append(obj[1]).append(Constants.JOINSTR);
            for(int i = 0; i < empid.length; i++){
                insertDefaultLastTimeWorker(obj, org, empid[i]);
            }
        }        
        workCardIDXS.deleteCharAt(workCardIDXS.length() - 1);
        //修改作业卡是否有默认人状态
        updateWorkCardIsHavePerson("'" + workCardIDXS.toString().replace(Constants.JOINSTR, DOUBLE_QUOTE_MARK) + "'");
        /*
         * 本来就是默认上次，所以不需要再往最后派工记录表里面新增数据
         */
        designateRecordManager.saveWorkRecord(DesignateRecord.TYPE_HEADMAN, idx);//新增派工记录
        return null;
    }
    
    /**
     * <li>说明：对指定作业工单默认派工
     * <li>创建人：程锐
     * <li>创建日期：2015-5-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param node 节点对象
     * @param idsBuilder 作业工单IDX
     * @param workCardIDXSStr 作业工单IDX的sql字符串
     * @param ids 作业工单IDX数组
     */
    public void updateWorkCardDispatchByCard(JobProcessNode node, StringBuilder idsBuilder, String workCardIDXSStr, String[] ids) {
        int dispatchCount = 0;
        WorkCard workCard = buildWorkCard(node);
        if (node.getWorkStationBelongTeam() != null && !StringUtil.isNullOrBlank(node.getWorkStationBelongTeamName())) {
            workCard.setWorkStationBelongTeam(node.getWorkStationBelongTeam());
            workCard.setWorkStationBelongTeamName(node.getWorkStationBelongTeamName());
            // 如派工班组不为空则生成默认派工人员
            if (node.getWorkStationBelongTeam() != null) {
                dispatchCount = updateWorkCardForDispatch(workCard, workCardIDXSStr);//派工-更新作业工单的工位、承修班组信息
                String sql = "delete from jxgc_worker where work_card_idx in " + workCardIDXSStr;//更新作业工单的工位、承修班组信息之后删除作业人员
                daoUtils.executeSql(sql);
                int insertCount = insertWorkerForDispatchByWorkCard(workCard, workCardIDXSStr); // 生成作业工单的默认作业人员
                if (insertCount > 0)// 有新增的派工记录才新增工长派工记录
                    designateRecordManager.saveWorkRecord(DesignateRecord.TYPE_HEADMAN, ids);// 新增工长派工记录
            } else {
                dispatchCount = updateWorkStationForDispatch(workCard, workCardIDXSStr);//派工-更新作业工单的工位信息
            }        
            updateWorkTaskStatus(idsBuilder);//清除作业工单下的作业任务已处理字段数据、已派工字段信息  
            if(dispatchCount > 0)//有新增的派工记录才新增调度派工记录
                designateRecordManager.saveWorkRecord(DesignateRecord.TYPE_DISPATCHER, ids);//新增调度派工记录
        }
    }
    
    /**
     * <li>说明：判断是否需要根据第二个节点重派工
     * <li>创建人：程锐
     * <li>创建日期：2015-5-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param oldNode 节点1
     * @param newNode 节点2
     * @return true 不需要根据第二个节点重派工， false 需要
     */
    public boolean isSameDispatch(JobProcessNode oldNode, JobProcessNode newNode) {
        String oldStation = oldNode.getWorkStationIDX();
        String newStation = newNode.getWorkStationIDX();
        Long oldTeam = oldNode.getWorkStationBelongTeam();
        Long newTeam = newNode.getWorkStationBelongTeam(); 
        return isSameDispatch(oldStation, newStation, oldTeam, newTeam);
    }
    
    /**
     * <li>说明：判断是否需要根据第二次派工信息重派工
     * <li>创建人：程锐
     * <li>创建日期：2015-5-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param oldStation 第一次的工位IDX
     * @param newStation 第二次的工位IDX
     * @param oldTeam 第一次的班组id
     * @param newTeam 第二次的班组id
     * @return true 不需要根据第二次派工信息重派工，false 需要
     */
    public boolean isSameDispatch(String oldStation, String newStation, Long oldTeam, Long newTeam) {
        if (oldTeam != null && newTeam != null && String.valueOf(oldTeam).equals(String.valueOf(newTeam)))
            return true;        
        if (!StringUtil.isNullOrBlank(oldStation) && !StringUtil.isNullOrBlank(newStation) && newStation.equals(oldStation)) {
            if (oldTeam != null && newTeam == null)
                return true;
            if (oldTeam == null && newTeam == null)
                return true;
        }
        return false;
    }
    
    /**
     * <li>说明：构造workCard实体对象
     * <li>创建人：程锐
     * <li>创建日期：2015-5-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param node 节点实体对象
     * @return workCard实体对象
     */
    public WorkCard buildWorkCard(JobProcessNode node) {
        WorkCard workCard = new WorkCard();
        workCard.setRdpIDX(node.getWorkPlanIDX());
        workCard.setNodeCaseIDX(node.getIdx());
//        workCard.setTecProcessCaseIDX(node.getProcessIDX());
        workCard.setWorkStationIDX(node.getWorkStationIDX());
        workCard.setWorkStationName(node.getWorkStationName());
        workCard.setWorkStationBelongTeam(node.getWorkStationBelongTeam());
        workCard.setWorkStationBelongTeamName(node.getWorkStationBelongTeamName());
        workCard.setNodeStatus(node.getStatus());
        return workCard;
    }
    
    /**
     * <li>说明：根据工艺节点实例主键对作业工单默认派工
     * <li>创建人：程锐
     * <li>创建日期：2013-7-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCard 作业工单实体对象 
     *          { rdpIDX //作业计划主键 nodeCaseIDX //流程节点主键 tecProcessCaseIDX //流程主键 
     *            workStationIDX //工位主键 workStationName //工位名称 
     *           workStationBelongTeam //作业班组 workStationBelongTeamName //作业班组名称 nodeStatus 所属节点状态
     *          }
     * @return 错误信息
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public String[] updateWorkCardByNode(WorkCard workCard) throws Exception{  
        String nodeCaseIDXS = CommonUtil.buildInSqlStr(workCard.getNodeCaseIDX());
    	List list = getInitOpenCardIDXListByNodes(nodeCaseIDXS);//查询处理中、已开放、初始化状态的作业卡
        StringBuilder ids = getIds(list);
        if (ids.length() < 1) {
			String[] errMsg = new String[1];
			errMsg[0] = "流程节点IDX：" + workCard.getNodeCaseIDX() + ",无对应作业工单或工单已处理，不能派工";
			return errMsg;
		}        
        String idx[] = ids.toString().split(Constants.JOINSTR);  
        int dispatchCount = 0;
        //如派工班组不为空则生成默认派工人员
        if (workCard.getWorkStationBelongTeam() != null) {
			dispatchCount = updateWorkCardForDispatchByNode(workCard);//派工-更新作业工单的工位、承修班组信息
			deleteWorker(nodeCaseIDXS);//更新作业工单的工位、承修班组信息之后删除作业人员
			int insertCount = insertWorkerForDispatchByNode(workCard); // 生成作业工单的默认作业人员
			if (insertCount > 0)// 有新增的派工记录才新增工长派工记录
				designateRecordManager.saveWorkRecord(DesignateRecord.TYPE_HEADMAN, idx);// 新增工长派工记录
		} else if (workCard.getWorkStationIDX() != null) {
			dispatchCount = updateWorkStationForDispatchByNode(workCard);//派工-更新作业工单的工位信息
		}        
        updateWorkTaskStatus(ids);//清除作业工单下的作业任务已处理字段数据、已派工字段信息
        if(dispatchCount > 0)//有新增的派工记录才新增调度派工记录
            designateRecordManager.saveWorkRecord(DesignateRecord.TYPE_DISPATCHER, idx);//新增调度派工记录
        return null;
    }    
    
    /**
     * <li>说明：派工-更新作业工单的工位、承修班组信息
     * <li>创建人：程锐
     * <li>创建日期：2015-1-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCard 作业工单实体对象 
     *          { rdpIDX //作业计划主键 nodeCaseIDX //流程节点主键 tecProcessCaseIDX //流程主键 
     *            workStationIDX //工位主键 workStationName //工位名称 
     *           workStationBelongTeam //作业班组 workStationBelongTeamName //作业班组名称 nodeStatus 所属节点状态
     *          }
     * @return 更新条数
     */
    public int updateWorkCardForDispatchByNode(WorkCard workCard) {
    	String sql = SqlMapUtil.getSql("jxgc-workcard:updateWorkCardForDispatchByNode")
                                .replace(WORKSTATIONIDX, workCard.getWorkStationIDX())
						        .replace(WORKSTATIONNAME, workCard.getWorkStationName())
						        .replace(WORKSTATIONBELONGTEAM, String.valueOf(workCard.getWorkStationBelongTeam()==null?"":workCard.getWorkStationBelongTeam()))
						        .replace(WORKSTATIONBELONGTEAMNAME, StringUtil.isNullOrBlank(workCard.getWorkStationBelongTeamName())?"":workCard.getWorkStationBelongTeamName())
						        .replace(NODECASEIDX, workCard.getNodeCaseIDX())
						        .replace(STATUS_OPEN, WorkCard.STATUS_OPEN)
						        .replace(STATUS_HANDLING, WorkCard.STATUS_HANDLING)
						        .replace(STATUS_NEW, WorkCard.STATUS_NEW)
                                .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                                .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss));       
    	return daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：生成作业工单的默认作业人员
     * <li>创建人：程锐
     * <li>创建日期：2015-1-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCard 作业工单实体对象 
     *          { rdpIDX //作业计划主键 nodeCaseIDX //流程节点主键 tecProcessCaseIDX //流程主键 
     *            workStationIDX //工位主键 workStationName //工位名称 
     *           workStationBelongTeam //作业班组 workStationBelongTeamName //作业班组名称 nodeStatus 所属节点状态
     *          }
     * @return 新增条数
     */
    public int insertWorkerForDispatchByNode(WorkCard workCard) {
    	String sql = SqlMapUtil.getSql("jxgc-workcard:insertWorkerForDispatchByNode")
                                .replace(WORKSTATIONIDX, workCard.getWorkStationIDX()) 
						        .replace(WORKSTATIONBELONGTEAM, String.valueOf(workCard.getWorkStationBelongTeam()))
						        .replace(WORKSTATIONBELONGTEAMNAME, workCard.getWorkStationBelongTeamName())
						        .replace("#workStationBelongTeamSeq#", StringUtil.isNullOrBlank(workCard.getWorkStationBelongTeamSeq()) ? "" : workCard.getWorkStationBelongTeamSeq())						        
						        .replace(NODECASEIDX, workCard.getNodeCaseIDX())
						        .replace(STATUS_OPEN, WorkCard.STATUS_OPEN) 
						        .replace(STATUS_HANDLING, WorkCard.STATUS_HANDLING) 
						        .replace(STATUS_NEW, WorkCard.STATUS_NEW);  
    	return daoUtils.executeSql(sql); 
    }
    
    /**
     * <li>说明：派工-更新作业工单的工位信息
     * <li>创建人：程锐
     * <li>创建日期：2015-1-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCard 作业工单实体对象 
     *          { rdpIDX //作业计划主键 nodeCaseIDX //流程节点主键 tecProcessCaseIDX //流程主键 
     *            workStationIDX //工位主键 workStationName //工位名称 
     *           workStationBelongTeam //作业班组 workStationBelongTeamName //作业班组名称 nodeStatus 所属节点状态
     *          }
     * @return 更新条数
     */
    public int updateWorkStationForDispatchByNode(WorkCard workCard) {
    	String sql = SqlMapUtil.getSql("jxgc-workcard:updateWorkStationForDispatchByNode")
                                .replace(WORKSTATIONIDX, workCard.getWorkStationIDX())
						        .replace(WORKSTATIONNAME, workCard.getWorkStationName())                                                                              
						        .replace(NODECASEIDX, workCard.getNodeCaseIDX())
						        .replace(STATUS_OPEN, WorkCard.STATUS_OPEN)
                                .replace(STATUS_HANDLING, WorkCard.STATUS_HANDLING)
                                .replace(STATUS_NEW, WorkCard.STATUS_NEW)
                                .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                                .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss));     
    	return daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：对多个关联相同工位的工艺节点默认派工
     * <li>创建人：程锐
     * <li>创建日期：2014-6-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCard 作业工单实体对象
     * json对象
     * {
     *     rdpIDX                       //兑现单主键
     *     nodeCaseIDX                  //多个以,分隔的工艺节点实例主键
     *     tecProcessCaseIDX            //工艺实例主键
     *     workStationIDX               //工位主键
     *     workStationName              //工位名称
     *     workStationCode              //工位编码
     *     repairLineIDX                //流水线主键
     *     workStationBelongTeam        //作业班组
     *     workStationBelongTeamName    //作业班组名称
     *     workStationBelongTeamSeq     //作业班组序列
     * }
     * @return 错误信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public String[] updateWorkCardByNodes(WorkCard workCard) throws BusinessException, NoSuchFieldException {
    	String nodeCaseIDXS = "('" + workCard.getNodeCaseIDX().replaceAll(Constants.JOINSTR, DOUBLE_QUOTE_MARK) + QUOTE_BRACKET_R;
        List list = getInitOpenCardIDXListByNodes(nodeCaseIDXS);//查询处理开放的初始化状态的作业卡
        StringBuilder ids = getIds(list);
        if(ids.length() < 1) {
            String[] errMsg = new String[1];
            errMsg[0] = "无对应作业工单或工单已处理，不能派工";
            return errMsg;
        }        
        String idx[] = ids.toString().split(Constants.JOINSTR);
        
        int dispatchCount = 0;
        dispatchCount = updateWorkCardForBatchDispatch(workCard, nodeCaseIDXS);//派工-更新作业工单的工位、承修班组信息
        deleteWorker(nodeCaseIDXS);//更新作业工单的工位、承修班组信息之后删除作业人员
        int insertCount = insertWorkerForDispatchByNodes(workCard, nodeCaseIDXS);//生成作业工单的默认作业人员
        if(insertCount > 0)//有新增的派工记录才新增工长派工记录
            designateRecordManager.saveWorkRecord(DesignateRecord.TYPE_HEADMAN, idx);
        updateWorkTaskStatus(ids);//清除作业工单下的作业任务已处理字段数据、已派工字段信息
        if(dispatchCount > 0)//有新增的派工记录才新增调度派工记录
            designateRecordManager.saveWorkRecord(DesignateRecord.TYPE_DISPATCHER, idx);
        return null;
    }
    
    /**
     * <li>说明：更新作业工单的工位和班组信息
     * <li>创建人：程锐
     * <li>创建日期：2015-5-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCard 作业工单实体
     * @param workCardIDXSStr 作业工单IDX的sql字符串
     * @return 更新数量
     */
    public int updateWorkCardForDispatch(WorkCard workCard, String workCardIDXSStr) {
        String sql = SqlMapUtil.getSql("jxgc-workcard:updateWorkCardForDispatch")
                                .replace(WORKSTATIONIDX, workCard.getWorkStationIDX())
                                .replace(WORKSTATIONNAME, workCard.getWorkStationName())
                                .replace(WORKSTATIONBELONGTEAM, String.valueOf(workCard.getWorkStationBelongTeam()==null?"":workCard.getWorkStationBelongTeam()))
                                .replace(WORKSTATIONBELONGTEAMNAME, StringUtil.isNullOrBlank(workCard.getWorkStationBelongTeamName())?"":workCard.getWorkStationBelongTeamName())
                                .replace(JxgcConstants.IDXS, workCardIDXSStr)
                                .replace(STATUS_OPEN, WorkCard.STATUS_OPEN)
                                .replace(STATUS_NEW, WorkCard.STATUS_NEW)
                                .replace(STATUS_HANDLING, WorkCard.STATUS_HANDLING)
                                .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                                .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss));       
        return daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：生成作业工单的默认作业人员
     * <li>创建人：程锐
     * <li>创建日期：2015-5-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCard 作业工单实体
     * @param workCardIDXSStr 作业工单IDX的sql字符串
     * @return 生成数量
     */
    public int insertWorkerForDispatchByWorkCard(WorkCard workCard, String workCardIDXSStr) {
        String sql = SqlMapUtil.getSql("jxgc-workcard:insertWorkerForDispatchByWorkCard")
                                .replace(WORKSTATIONIDX, workCard.getWorkStationIDX())
                                .replace(WORKSTATIONBELONGTEAM, String.valueOf(workCard.getWorkStationBelongTeam()))
                                .replace(WORKSTATIONBELONGTEAMNAME, workCard.getWorkStationBelongTeamName())
                                .replace("#workStationBelongTeamSeq#", StringUtil.isNullOrBlank(workCard.getWorkStationBelongTeamSeq()) ? "" : workCard.getWorkStationBelongTeamSeq())
                                .replace(JxgcConstants.IDXS, workCardIDXSStr)
                                .replace(STATUS_OPEN, WorkCard.STATUS_OPEN) 
                                .replace(STATUS_NEW, WorkCard.STATUS_NEW)
                                .replace(STATUS_HANDLING, WorkCard.STATUS_HANDLING); 
        return daoUtils.executeSql(sql); 
    }
    
    /**
     * <li>说明：派工-更新作业工单的工位信息
     * <li>创建人：程锐
     * <li>创建日期：2015-1-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCard 作业工单实体
     * @param workCardIDXSStr 作业工单IDX的sql字符串
     * @return 更新条数
     */
    public int updateWorkStationForDispatch(WorkCard workCard, String workCardIDXSStr) {
        String sql = SqlMapUtil.getSql("jxgc-workcard:updateWorkStationForDispatch")
                                .replace(WORKSTATIONIDX, workCard.getWorkStationIDX())
                                .replace(WORKSTATIONNAME, workCard.getWorkStationName())                                                                              
                                .replace(NODECASEIDX, workCard.getNodeCaseIDX())
                                .replace(JxgcConstants.IDXS, workCardIDXSStr)
                                .replace(STATUS_OPEN, WorkCard.STATUS_OPEN)
                                .replace(STATUS_NEW, WorkCard.STATUS_NEW)
                                .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                                .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss));      
        return daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：派工-更新作业工单的工位、承修班组信息之后删除作业人员
     * <li>创建人：程锐
     * <li>创建日期：2014-6-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeCaseIDXS 节点IDX的sql字符串
     */
    public void deleteWorker(String nodeCaseIDXS) {
        String sql = "delete from jxgc_worker where work_card_idx in (select idx from jxgc_work_card where record_status = 0 " 
                     + "and node_case_idx  in " 
                     + nodeCaseIDXS 
                     + " and status in ('" 
                     + WorkCard.STATUS_OPEN 
                     + DOUBLE_QUOTE_MARK 
                     + WorkCard.STATUS_NEW 
                     + DOUBLE_QUOTE_MARK 
                     + WorkCard.STATUS_HANDLING
                     + "'))";
        daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：清除作业工单下的作业任务已处理字段数据、已派工字段信息
     * <li>创建人：程锐
     * <li>创建日期：2014-6-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 以,分隔的ids字符串
     */
    public void updateWorkTaskStatus(StringBuilder ids) {
        String sql = "UPDATE JXGC_WORK_TASK SET STATUS = '" 
                     + WorkTask.STATUS_WAITINGFORGET 
                     + "' WHERE WORK_CARD_IDX IN ('" 
                     + ids.toString().replaceAll(Constants.JOINSTR, DOUBLE_QUOTE_MARK) 
                     + QUOTE_BRACKET_R;        
        daoUtils.executeSql(sql);
    }
    
    /**
     * <li>方法说明：统计工长派工任务数量（工位终端） 
     * <li>方法名称：findForemanCount
     * <li>@param orgid
     * <li>@return
     * <li>return: int[] int[0]=未派工, int[1]=已派工
     * <li>创建人：张凡
     * <li>创建时间：2013-5-31 下午08:29:40
     * <li>修改人：
     * <li>修改内容：
     */
    public Integer[] findForemanCount(Long orgid){
        try{
            String where = WorkCard.STATUS_OPEN + "' or status = '" + WorkCard.STATUS_HANDLING + "'";
            if("show".equalsIgnoreCase(SystemConfigUtil.getValue("ck.jxgc.producttaskmanage.workcard.foremanShowInit"))){
                where += " or status = '" + WorkCard.STATUS_NEW + "'";
            }
            
            String sql = SqlMapUtil.getSql("jxgc-gdgl:statisticsForeman").replace("TEAM", orgid.toString())
                                                                         .replace("STATUS", where);
            
            BigDecimal count = (BigDecimal)daoUtils.executeSqlQuery(sql).iterator().next();
            return new Integer[]{count.intValue()};
        }catch(Exception ex){
            return new Integer[]{0,0};
        }
    }
    
    /**
     * <li>说明：获取工长派工-未派工查询sql
     * <li>创建人：程锐
     * <li>创建日期：2013-12-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @return 工长派工-未派工查询sql
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    private String getWorkCardSql(String searchJson) throws JsonParseException, JsonMappingException, IOException{
        String sql = SqlMapUtil.getSql("jxgc-gdgl2:getWorkCardSql")
                                        + WorkCard.STATUS_OPEN + "' or t.status = '" + WorkCard.STATUS_HANDLING  +
                                        ("show".equalsIgnoreCase(SystemConfigUtil.getValue("ck.jxgc.producttaskmanage.workcard.foremanShowInit"))
                                            ? "' or t.status = '" + WorkCard.STATUS_NEW : "") + QUOTE_BRACKET_R;
        //添加工艺节点多选过滤条件
        Map queryMap = JSONUtil.read(searchJson, Map.class); 
        StringBuilder multyAwhere = new StringBuilder();
        if(queryMap.containsKey("nodeCaseIDX")) {
            String nodeCaseString = String.valueOf(queryMap.get("nodeCaseIDX"));
            if(!StringUtil.isNullOrBlank(nodeCaseString)){
                multyAwhere.append(" and n.idx in ('").append(nodeCaseString.replaceAll(";", DOUBLE_QUOTE_MARK)).append("') ");
            }
        }
        //添加检修活动名称多选过滤条件
        if(queryMap.containsKey("activity_name")) {
            String activityString = String.valueOf(queryMap.get("activity_name"));
            if(!StringUtil.isNullOrBlank(activityString)){
                multyAwhere.append(" and t.activity_name in ('").append(activityString.replaceAll(";", DOUBLE_QUOTE_MARK)).append("') ");               
            }
        }
        if(queryMap.containsKey("workPlanIDX")) {
            String rdpString = String.valueOf(queryMap.get("workPlanIDX"));
            if(!StringUtil.isNullOrBlank(rdpString)){
                multyAwhere.append(" and r.idx = '").append(rdpString).append("' ");                
            }
        }
        if(queryMap.containsKey("rdpIDX")) {
            String rdpString = String.valueOf(queryMap.get("rdpIDX"));
            if(!StringUtil.isNullOrBlank(rdpString)){
                multyAwhere.append(" and r.idx = '").append(rdpString).append("' ");                
            }
        }
        if(queryMap.containsKey("fixplace_fullname")) {
            String fullNameString = String.valueOf(queryMap.get("fixplace_fullname"));
            if(!StringUtil.isNullOrBlank(fullNameString)){
                multyAwhere.append(" and t.fixplace_fullname like '%").append(fullNameString).append("%' ");
            }
        }
        //工位终端
        else if(queryMap.containsKey("fixPlaceFullName")) {
            String fullNameString = String.valueOf(queryMap.get("fixPlaceFullName"));
            if(!StringUtil.isNullOrBlank(fullNameString)){
                multyAwhere.append(" and t.fixplace_fullname like '%").append(fullNameString).append("%' ");
            }
        }       
        
        if(queryMap.containsKey("workCardName")) {
            String fullNameString = String.valueOf(queryMap.get("workCardName"));
            if(!StringUtil.isNullOrBlank(fullNameString)){
                multyAwhere.append(" and t.work_card_name like '%").append(fullNameString).append("%' ");
            }
        }
        if(queryMap.containsKey("haveDefaultPerson")) {
            String personString = String.valueOf(queryMap.get("haveDefaultPerson"));
            if(!StringUtil.isNullOrBlank(personString)){
                multyAwhere.append(" and (t.HAVE_DEFAULT_PERSON = ").append(personString).append(" or t.HAVE_DEFAULT_PERSON IS NULL) ");
            }
        }
        
        if(queryMap.containsKey("workStationBelongTeam")) {
            String teamString = String.valueOf(queryMap.get("workStationBelongTeam"));
            if(!StringUtil.isNullOrBlank(teamString)){
                multyAwhere.append(" and t.work_station_belong_team = ").append(teamString).append(" ");
            }
        }
        sql = sql + multyAwhere.toString();
        return sql;
    }
    
    /**
     * <li>说明：查询多个工艺节点实例id关联的处理开放的初始化状态的作业卡id列表
     * <li>创建人：程锐
     * <li>创建日期：2014-6-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeCaseIDXS 节点IDX的sql字符串
     * @return 作业卡id列表
     */
    private List getInitOpenCardIDXListByNodes(String nodeCaseIDXS) {    	
		String sql = "SELECT IDX FROM JXGC_WORK_CARD  WHERE NODE_CASE_IDX  IN " 
					 + nodeCaseIDXS 
        			 + " AND RECORD_STATUS = 0 AND STATUS IN ('" 
        			 + WorkCard.STATUS_OPEN 
        			 + DOUBLE_QUOTE_MARK 
        			 + WorkCard.STATUS_NEW 
                     + DOUBLE_QUOTE_MARK 
                     + WorkCard.STATUS_HANDLING 
        			 + QUOTE_BRACKET_R;
    	return daoUtils.executeSqlQuery(sql);
    }
    
    /**
     * <li>说明：批量插入作业人员信息
     * <li>创建人：程锐
     * <li>创建日期：2015-1-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param empids 以,号分隔的派工人员ID字符串
     * @param workCardSql 获取派工的作业工单IDX的sql字符串
     */
    private void batchInsertWorker(String empids, String workCardSql) {
        String sql = SqlMapUtil.getSql("jxgc-gdgl2:batchInsertWorker")
                               .replace("作业工单sql", workCardSql)
                               .replace(EMPID, empids);
        daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：根据list列表获取以,分隔的ids字符串
     * <li>创建人：程锐
     * <li>创建日期：2014-6-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param list id列表
     * @return 以,分隔的ids字符串
     */
    private StringBuilder getIds(List list) {
    	StringBuilder ids = new StringBuilder();
        if(list != null && list.size() > 0) {
            for(int i = 0; i < list.size();i++) {
                ids.append(list.get(i).toString()).append(Constants.JOINSTR);
            }
            ids.deleteCharAt(ids.length()-1);
        }
        return ids;
    }
    
    /**
     * <li>说明：去除workCardIdx字符串前后可能出现的,
     * <li>创建人：程锐
     * <li>创建日期：2014-8-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIdx 以,号分隔的作业工单IDX字符串
     * @return 去除workCardIdx字符串前后可能出现的,后的workCardIdx
     */
    private String buildWorkCardIdx(String workCardIdx) {
        if(workCardIdx.endsWith(Constants.JOINSTR))
            workCardIdx = workCardIdx.substring(0, workCardIdx.length()-1);
        if(workCardIdx.startsWith(Constants.JOINSTR))
            workCardIdx = workCardIdx.substring(1, workCardIdx.length());
        return workCardIdx;
    }
    
    /**
     * <li>说明：批量工长派工
     * <li>创建人：程锐
     * <li>创建日期：2014-8-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIdx 以,号分隔的作业工单IDX字符串
     * @param empids 以,号分隔的派工人员ID字符串
     * @param ids 去除workCardIdx字符串前后可能出现的,后的workCardIdx 
     * @param operatorid 操作者ID
     */
    private void updateBatchForemanDispater(String workCardIdx,
                                            String empids,
                                            String ids,
                                            Long operatorid) {
        //删除作业人员
        deleteWorkerByWorkCard("'" + workCardIdx + "'");
        
        //新增作业人员
        insertWorker(workCardIdx, empids);
        //修改是否派工状态        
        updateWorkCardIsHavePerson("'" + workCardIdx + "'");
        
        //新增派工记录
        designateRecordManager.saveWorkRecord(DesignateRecord.TYPE_HEADMAN, ids.split(QUOTE_MARK));
        
        //存储最后一次派工记录 
        workLastDispatchManager.saveLastDispatcher("'" + workCardIdx + "'", empids, operatorid);
    }
    
    /**
     * <li>说明：插入作业人员信息
     * <li>创建人：程锐
     * <li>创建日期：2015-1-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIdx 以,号分隔的作业工单IDX字符串
     * @param empids 以,号分隔的派工人员ID字符串
     */
    private void insertWorker(String workCardIdx,
                              String empids) {
        String sql = SqlMapUtil.getSql("jxgc-gdgl2:insertWorker")
                                .replace("作业工单主键", "'" + workCardIdx + "'")
                                .replace(EMPID, empids);
        daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：派工-更新作业工单的工位、承修班组信息
     * <li>创建人：程锐
     * <li>创建日期：2014-6-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCard 作业工单实体对象
     * json对象
     * {
     *     rdpIDX                       //兑现单主键
     *     nodeCaseIDX                  //多个以,分隔的工艺节点实例主键
     *     tecProcessCaseIDX            //工艺实例主键
     *     workStationIDX               //工位主键
     *     workStationName              //工位名称
     *     workStationCode              //工位编码
     *     repairLineIDX                //流水线主键
     *     workStationBelongTeam        //作业班组
     *     workStationBelongTeamName    //作业班组名称
     *     workStationBelongTeamSeq     //作业班组序列
     * }
     * @param nodeCaseIDXS 节点IDX的sql字符串
     * @return 更新条数
     */
    private int updateWorkCardForBatchDispatch(WorkCard workCard, String nodeCaseIDXS) {
    	String sql = SqlMapUtil.getSql("jxgc-workcard:updateWorkCardForBatchDispatch")
                               .replace(WORKSTATIONIDX, workCard.getWorkStationIDX())
							   .replace(WORKSTATIONNAME, workCard.getWorkStationName())
						       .replace(WORKSTATIONBELONGTEAM, String.valueOf(workCard.getWorkStationBelongTeam()==null?"":workCard.getWorkStationBelongTeam()))
							   .replace(WORKSTATIONBELONGTEAMNAME, StringUtil.isNullOrBlank(workCard.getWorkStationBelongTeamName())?"":workCard.getWorkStationBelongTeamName())
							   .replace("#nodeCaseIDXS#", nodeCaseIDXS)
							   .replace(STATUS_OPEN, WorkCard.STATUS_OPEN)
                               .replace(STATUS_HANDLING, WorkCard.STATUS_HANDLING) 
					           .replace(STATUS_NEW, WorkCard.STATUS_NEW);       
		return daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：派工-生成作业卡的默认作业人员
     * <li>创建人：程锐
     * <li>创建日期：2014-6-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCard 作业工单实体对象
     * json对象
     * {
     *     rdpIDX                       //兑现单主键
     *     nodeCaseIDX                  //多个以,分隔的工艺节点实例主键
     *     tecProcessCaseIDX            //工艺实例主键
     *     workStationIDX               //工位主键
     *     workStationName              //工位名称
     *     workStationCode              //工位编码
     *     repairLineIDX                //流水线主键
     *     workStationBelongTeam        //作业班组
     *     workStationBelongTeamName    //作业班组名称
     *     workStationBelongTeamSeq     //作业班组序列
     * }
     * @param nodeCaseIDXS 节点IDX的sql字符串
     * @return 生成作业人员记录条数
     */
    private int insertWorkerForDispatchByNodes(WorkCard workCard, String nodeCaseIDXS) {
    	String sql = SqlMapUtil.getSql("jxgc-workcard:insertWorkerForDispatchByNodes")
                                .replace(WORKSTATIONIDX, workCard.getWorkStationIDX())
						        .replace(WORKSTATIONBELONGTEAM, String.valueOf(workCard.getWorkStationBelongTeam()))
						        .replace(WORKSTATIONBELONGTEAMNAME, workCard.getWorkStationBelongTeamName())
						        .replace("#workStationBelongTeamSeq#", StringUtil.isNullOrBlank(workCard.getWorkStationBelongTeamSeq()) ? "" : workCard.getWorkStationBelongTeamSeq())
						        .replace("#nodeCaseIDXS#", nodeCaseIDXS)
						        .replace(STATUS_OPEN, WorkCard.STATUS_OPEN)
                                .replace(STATUS_HANDLING, WorkCard.STATUS_HANDLING) 
						        .replace(STATUS_NEW, WorkCard.STATUS_NEW); 
		return daoUtils.executeSql(sql); 
    }
    
    /**
     * <li>说明：删除指定作业工单的作业人员
     * <li>创建人：程锐
     * <li>创建日期：2015-1-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDXS 作业工单IDX的sql字符串
     */
    private void deleteWorkerByWorkCard(String workCardIDXS) {
    	String sql = "DELETE FROM JXGC_WORKER WHERE WORK_CARD_IDX IN (" + workCardIDXS + ")";
        daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：修改作业工单是否有默认人状态
     * <li>创建人：程锐
     * <li>创建日期：2015-1-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDXS 作业工单IDX的sql字符串
     */
    private void updateWorkCardIsHavePerson(String workCardIDXS) {
    	String sql = "UPDATE JXGC_WORK_CARD  SET HAVE_DEFAULT_PERSON = '1' WHERE IDX IN (" 
      	  			 + workCardIDXS  + ") AND (HAVE_DEFAULT_PERSON != '1' OR HAVE_DEFAULT_PERSON IS NULL)";
        daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：查询上次作业人员及工单
     * <li>创建人：程锐
     * <li>创建日期：2015-1-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idxs 以,号分隔的作业工单IDX字符串
     * @param empx 人员对象
     * @return 上次作业人员及工单
     */
    @SuppressWarnings("unchecked")
    private List<Object[]> findLastDispatch(String idxs, OmEmployee empx) {
        String sql = SqlMapUtil.getSql("jxgc-gdgl2:findLastDispatch")
                               .replace("作业工单主键", idxs)
                               .replace("施修班组", empx.getOrgid().toString());
        return daoUtils.executeSqlQuery(sql);
    }
    
    /**
     * <li>说明：根据上次派工人员新增作业人员
     * <li>创建人：程锐
     * <li>创建日期：2015-1-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param obj 上次作业人员对象数组
     * @param org 组织机构对象
     * @param empid 作业人员id
     */
    private void insertDefaultLastTimeWorker(Object[] obj, OmOrganization org, String empid) {
        String sql = SqlMapUtil.getSql("jxgc-gdgl2:insertDefaultLastTimeWorker")
                                .replace("作业工单主键", obj[1].toString())
                                .replace("班组ID", org.getOrgid().toString())
                                .replace("班组名称", org.getOrgname())
                                .replace("班组系列", org.getOrgseq())
                                .replace(EMPID, empid);                
        daoUtils.executeSql(sql);
    }
}
