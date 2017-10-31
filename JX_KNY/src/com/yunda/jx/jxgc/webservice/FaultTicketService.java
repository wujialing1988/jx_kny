package com.yunda.jx.jxgc.webservice;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.frame.yhgl.manager.EosDictEntrySelectManager;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.frame.yhgl.manager.SysEosDictEntryManager;
import com.yunda.jx.base.jcgy.entity.EquipFault;
import com.yunda.jx.base.jcgy.manager.EquipFaultManager;
import com.yunda.jx.base.jcgy.manager.TrainTypeManager;
import com.yunda.jx.component.manager.BaseComboManager;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;
import com.yunda.jx.component.manager.OmOrganizationSelectManager;
import com.yunda.jx.jczl.attachmanage.manager.JczlTrainManager;
import com.yunda.jx.jxgc.buildupmanage.entity.PlaceFault;
import com.yunda.jx.jxgc.buildupmanage.manager.BuildUpTypeQueryManager;
import com.yunda.jx.jxgc.buildupmanage.manager.PlaceFaultManager;
import com.yunda.jx.jxgc.tpmanage.entity.FaultQCResult;
import com.yunda.jx.jxgc.tpmanage.entity.FaultQCResultVO;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicket;
import com.yunda.jx.jxgc.tpmanage.entity.RepairEmpBean;
import com.yunda.jx.jxgc.tpmanage.manager.FaultQCResultManager;
import com.yunda.jx.jxgc.tpmanage.manager.FaultQCResultQueryManager;
import com.yunda.jx.jxgc.tpmanage.manager.FaultTicketManager;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.jx.pjwz.partsBase.professionaltype.entity.ProfessionalType;
import com.yunda.jx.pjwz.partsBase.professionaltype.manager.ProfessionalTypeManager;
import com.yunda.jx.webservice.stationTerminal.base.StationTerminalWS;
import com.yunda.jx.webservice.stationTerminal.base.entity.EosDictEntryBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.FaultMethodBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.PlaceFaultBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.ProfessionalTypeBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.UndertakeTrainTypeBean;
import com.yunda.twt.trainaccessaccount.entity.InTrainDispatchCountBean;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountQueryManager;
import com.yunda.util.BeanUtils;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;
import com.yunda.zb.common.manager.TrainNoManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 提票webservice实现类
 * <li>创建人：程锐
 * <li>创建日期：2015-7-15
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
@Service(value = "faultTicketService")
public class FaultTicketService implements IFaultTicketService {
    
    /**
     * 日志对象
     */
    private Logger logger = Logger.getLogger(getClass().getName());
    /** 车号业务类 */
    @Autowired
    private TrainNoManager trainNoManager;
    
    /** 机车业务类 */
    @Resource
    private JczlTrainManager jczlTrainManager ;
    
    /** 人员业务类 */
    @Resource
    private OmEmployeeManager omEmployeeManager;
    
    /** 系统出错信息 */
    private static final String MESSAGE_ERROR = "error";
    /**
     * <li>说明：获取提票类型
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 提票类型列表
     */
    public String getFaultTypes() {
        try {
            List<EosDictEntry> list = getEosDictEntrySelectManager().findByDicTypeID("JCZL_FAULT_TYPE");
            List<EosDictEntryBean> beanlist = new ArrayList<EosDictEntryBean>();
            for (EosDictEntry eos : list) {
                EosDictEntryBean bean = new EosDictEntryBean();
                bean.setDictid(eos.getId().getDictid());
                bean.setDictname(eos.getDictname());
                beanlist.add(bean);
            }
            return JSONUtil.write(beanlist);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：获取承修车型
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 承修车型列表
     */
    @SuppressWarnings("unchecked")
    public String getUndertakeTrainType() {
        try {
            OmOrganization org = OmOrganizationSelectManager.getOrgByOrgcode(JXSystemProperties.OVERSEA_ORGCODE);
            Map map = getTrainTypeManager().page("", JSONUtil.read("{}", Map.class), 0, 100, "", "yes", org.getOrgseq());
            List<UndertakeTrainTypeBean> list = new ArrayList<UndertakeTrainTypeBean>();
            list = BeanUtils.copyListToList(UndertakeTrainTypeBean.class, (List) map.get("root"));
            Page pageList = new Page((Integer) (map.get("totalProperty")), list);
            return JSONUtil.write(pageList.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：根据车型获取车号列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx 车型主键
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 车号列表
     */
    @SuppressWarnings("unchecked")
    public String getTrainNoByTrainType(String trainTypeIdx, int start, int limit) {
        start--;
        try {
//            OmOrganization org = OmOrganizationSelectManager.getOrgByOrgcode(JXSystemProperties.OVERSEA_ORGCODE);
//            Map map =
//                getJczlTrainManager().page("", JSONUtil.read("{\"trainTypeIDX\":\"" + trainTypeIdx + "\"}", Map.class), start * limit, limit, "",
//                    "yes", "no", org.getOrgseq(), "false");
//            List<TrainNoBean> list = new ArrayList<TrainNoBean>();
//            list = BeanUtils.copyListToList(TrainNoBean.class, (List) map.get("root"));
//            Page pageList = new Page((Integer) (map.get("totalProperty")), list);
//            return JSONUtil.write(pageList.extjsStore());

            Map queryParamsMap = new HashMap();
            queryParamsMap.put("trainTypeIDX", trainTypeIdx);
            queryParamsMap.put("isAll", "yes");
            queryParamsMap.put("isCx", "no");
            queryParamsMap.put("isRemoveRun", "false");
            Map map = trainNoManager.page(queryParamsMap, start * limit, limit);
            return JSONUtil.write(map);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return MESSAGE_ERROR;
        }
    }
    
    
    /**
     * <li>说明：获取车号列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx
     * @param start
     * @param limit
     * @return
     */
    public String getTrainNo(String trainTypeIdx,String vehicleType, int start, int limit) {
        start--;
        try {
            Map<String, String> queryParamsMap = new HashMap<String, String>();
            queryParamsMap.put("trainTypeIDX", trainTypeIdx);
            queryParamsMap.put("vehicleType", vehicleType);
            Map map = jczlTrainManager.getTrainNoByTrainType(queryParamsMap, start * limit, limit);
            return JSONUtil.write(map);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return MESSAGE_ERROR;
        }
    }
    
    /**
     * <li>说明：根据车型车号获取故障提票组成树根节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx 车型主键
     * @param trainNo 车号
     * @return 故障提票组成树根节点列表
     */
    public String getBuildUpType(String trainTypeIdx, String trainNo) {
        try {
            List<HashMap> list = getBuildUpTypeQueryManager().getRootBuildUp(trainTypeIdx, trainNo);
            return JSONUtil.write(list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：获取故障提票下级树节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 上级父节点idx
     * @param partsBuildUpTypeIdx 所属组成型号idx
     * @param isVirtual 表示下层节点是否是虚拟位置下节点
     * @return 树节点列表
     */
    public String getBuildUpTypeTree(String parentIDX, 
                                     String partsBuildUpTypeIdx, 
                                     String isVirtual) {
        try {
        List<HashMap<String, Object>> list = getBuildUpTypeQueryManager().allTree(parentIDX, 
                                                                                  partsBuildUpTypeIdx, 
                                                                                  "", 
                                                                                  isVirtual);
          return JSONUtil.write(list);
      } catch (Exception e) {
          ExceptionUtil.process(e, logger);
          return WsConstants.OPERATE_FALSE;
      }
  }
    
    /**
     * <li>说明：获取部门下所有班组
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作人员ID
     * @return 部门下所有班组列表
     */
    public String getTeamOfDept(Long operatorid) {
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            OmOrganizationSelectManager m = getOmOrganizationSelectManager();
            OmOrganization org = m.getOrgByAcOperator(operatorid);
            String orgid = m.getTopOrgid(org.getOrgid(), Constants.DEPARTMENT_LEVEL);
            List<Map> listMap = new ArrayList<Map>();
            if (!StringUtil.isNullOrBlank(orgid))
                listMap = m.findSomeLevelOrgTree(orgid, "tream|fullName", Constants.DEPARTMENT_LEVEL, null);
            return JSONUtil.write(listMap);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
            return JSONObject.toJSONString(msg);
        }        
    }
    
    /**
     * <li>说明：根据班组ID查询班组人员
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param orgid 组织机构ID
     * @return 班组人员列表
     */
    public String findTeamEmps(Long orgid) {
        try {
            return getStationTerminalWS().findTeamEmps(orgid);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：获取专业类型列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 专业类型列表
     */
    @SuppressWarnings("unchecked")
    public String getProfessionalType(int start, int limit) {
        try {
            start--;
            ProfessionalType type = new ProfessionalType();
            type.setStatus(ProfessionalType.status_start);
            SearchEntity<ProfessionalType> searchEntity = new SearchEntity<ProfessionalType>(type, start * limit, limit, null);
            Page page = getProfessionalTypeManager().findPageList(searchEntity);
            // 封装返回值
            List<ProfessionalTypeBean> beanList = new ArrayList<ProfessionalTypeBean>();
            beanList = BeanUtils.copyListToList(ProfessionalTypeBean.class, page.getList());
            Page pageList = new Page(page.getTotal(), beanList);
            return JSONUtil.write(pageList.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：保存故障现象
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员ID
     * @param jsonData 故障现象Json集合
     * @return 操作成功与否
     * @throws IOException 
     */
    public String savePlaceFaultList(Long operatorid, String jsonData) throws IOException {
        try {
            AcOperator ac = getAcOperatorManager().getModelById(operatorid);
            SystemContext.setAcOperator(ac);
            PlaceFault[] entityList = JSONUtil.read(jsonData, PlaceFault[].class);
            getPlaceFaultManager().saveOrUpdateList(entityList);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger);
            OperateReturnMessage message = OperateReturnMessage.newFailsInstance(e.getMessage());
            return JSONUtil.write(message);
        }
    }
    
    /**
     * <li>说明：通过组成位置主键获取故障现象数据
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param buildUpPlaceIdx 组成位置主键
     * @return 故障现象数据
     */
    @SuppressWarnings("unchecked")
    public String getPlaceFault(String buildUpPlaceIdx) {
        try {
            List<PlaceFault> faultList = getPlaceFaultManager().findFaultList(buildUpPlaceIdx);
            PlaceFault other = new PlaceFault();
            other.setFaultId(PlaceFault.OTHERID);
            other.setFaultName("其它");
            faultList.add(faultList.size(), other);
            List<PlaceFaultBean> beanLsit = new ArrayList<PlaceFaultBean>();
            beanLsit = BeanUtils.copyListToList(PlaceFaultBean.class, faultList);
            return JSONUtil.write(beanLsit);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：获取故障字典码表数据集合
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param fixPlaceIdx 故障位置主键
     * @param faultName 故障名称
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 故障字典码表数据集合
     */
    @SuppressWarnings("unchecked")
    public String findfaultList(String fixPlaceIdx, String faultName, int start, int limit) {
        start--;
        try {
            EquipFault entity = new EquipFault();
            if (!StringUtil.isNullOrBlank(faultName)) {
                entity.setFaultName(faultName);
            }
            SearchEntity<EquipFault> searchEntity = new SearchEntity<EquipFault>(entity, start * limit, limit, null);
            /** 封装返回值 */
            Page page = getEquipFaultManager().faultList(searchEntity, null);
            List<Object> objList = new ArrayList<Object>(); // 定义返回集合对象
            List<EquipFault> equipList = page.getList();
            Map<String, Object> equipFault = null;
            for (int i = 0; i < equipList.size(); i++) {
                equipFault = new HashMap<String, Object>();
                equipFault.put("FaultID", equipList.get(i).getFaultID());
                equipFault.put("FaultName", equipList.get(i).getFaultName());
                objList.add(equipFault);
            }
            Page pageList = new Page(page.getTotal(), objList);
            return JSONUtil.write(pageList.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：提票录入处理
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员主键
     * @param jsonData 提票信息Json集合
     * @return 操作成功与否
     */
    public String saveFaultNoticeFromGrid(Long operatorid, String jsonData) {
        try {
            AcOperator ac = getAcOperatorManager().getModelById(operatorid);
            SystemContext.setAcOperator(ac);
            FaultTicket[] entityList = JSONUtil.read(jsonData, FaultTicket[].class);
            for (FaultTicket ticket : entityList) {
                if (!StringUtil.isNullOrBlank(ticket.getFaultOccurDateStr()))
                    ticket.setFaultOccurDate(DateUtil.parse(ticket.getFaultOccurDateStr(), "yyyy-MM-dd HH:mm"));
            }
            getFaultTicketManager().saveTpAndInst(entityList, entityList[0]);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：查询提票调度派工分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询json字符串
     * @param isDispatch 是否派工（true 已派工，false 未派工）
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 提票调度派工分页列表
     */
    public String queryDdpgList(String searchJson, String isDispatch, int start, int limit) {
        start--;
        Map map = new HashMap();
        try {
            QueryCriteria<FaultTicket> query = getFaultTicketManager().getDdpgQueryCriteria(isDispatch);            
            List<Condition> whereList = CommonUtil.getWhereList(searchJson, query.getWhereList());
            query.setWhereList(whereList);
            query.setStart(start * limit);
            query.setLimit(limit);
            Page page = getFaultTicketManager().findPageList(query);
            if (page.getTotal() > 0 && page.getList().size() > 0)
                map = page.extjsStore();
            return JSONUtil.write(map);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：获取提票调度派工班组列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @return 提票调度派工班组列表
     */
    @SuppressWarnings("unchecked")
    public String getFaultDispatcherTeam(long operatorid) {
        try {
            SystemContext.setAcOperator(getAcOperatorManager().getModelById(operatorid));
//            String hql = " and orgid in (select orgid from WorkPlaceToOrg where workPlaceCode = '" + siteID + "') and orgdegree = 'tream'";
            List<Map> list = getOmOrganizationSelectManager().queryTeamListBySite();
            Page page = new Page(list.size(), list);
            Map<String, Object> map = page.extjsStore();
            return JSONUtil.write(map);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：提票调度派工
     * <li>创建人：程锐
     * <li>创建日期：2015-7-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者id
     * @param idxs 以.号分隔的提票单Idx字符串
     * @param orgId 组织机构id
     * @param orgname 组织机构名称
     * @param orgseq 组织机构序列
     * @return 操作成功与否
     */
    public String updateForDdpg(long operatorid, String idxs, String orgId, String orgname, String orgseq) {
        try {
            FaultTicket data = new FaultTicket();
            data.setRepairTeam(orgId);
            data.setRepairTeamName(orgname);
            data.setRepairTeamOrgseq(orgseq);
            String[] tpIDXAry = StringUtil.tokenizer(idxs, Constants.JOINSTR);
            getFaultTicketManager().updateForDdpg(data, tpIDXAry);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        } 
    }
    
    /**
     * <li>说明：查询提票工长派工分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询json字符串
     * @param isDispatch 是否派工（true 已派工，false 未派工）
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @param operatorid 操作者id
     * @return 提票工长派工分页列表
     */
    public String queryGzpgList(String searchJson, long operatorid, boolean isDispatch, int start, int limit) {
        start--;
        Map map = new HashMap();
        try {
            QueryCriteria<FaultTicket> query = getFaultTicketManager().getGzpgQueryCriteria(isDispatch, operatorid);            
            List<Condition> whereList = CommonUtil.getWhereList(searchJson, query.getWhereList());
            query.setWhereList(whereList);
            query.setStart(start * limit);
            query.setLimit(limit);
            Page page = getFaultTicketManager().findPageList(query);
            if (page.getTotal() > 0 && page.getList().size() > 0)
                map = page.extjsStore();
            return JSONUtil.write(map);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：提票工长派工
     * <li>创建人：程锐
     * <li>创建日期：2015-7-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者id
     * @param idxs 以,号分隔的提票单Idx字符串
     * @param empids 以,号分隔的人员id字符串
     * @return 操作成功与否
     */
    public String updateForGzpg(long operatorid, String idxs, String empids) {
        try {
            String[] ids = StringUtil.tokenizer(idxs, Constants.JOINSTR);
            String[] empidArray = StringUtil.tokenizer(empids, Constants.JOINSTR);
            getFaultTicketManager().updateForGzpg(empidArray, ids);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        } 
    }
    
    /**
     * <li>说明：查询提票处理分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询json字符串
     * @param operatorid 操作者id
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 提票处理分页列表
     */
    public String queryHandleList(String searchJson, long operatorid, int start, int limit) {
        start--;
        Map map = new HashMap();
        try {
            QueryCriteria<FaultTicket> query = getFaultTicketManager().getHandleQueryCriteria(operatorid);            
            List<Condition> whereList = CommonUtil.getWhereList(searchJson, query.getWhereList());
            query.setWhereList(whereList);
            query.setStart(start * limit);
            query.setLimit(limit);
            Page page = getFaultTicketManager().findPageList(query);
            if (page.getTotal() > 0 && page.getList().size() > 0)
                map = page.extjsStore();
            return JSONUtil.write(map);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：获取提票信息
     * <li>创建人：程锐
     * <li>创建日期：2015-7-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param faultIdx 提票idx
     * @return 提票信息
     */
    public String getFaultInfo(String faultIdx) {
      try {
            FaultTicket tp = getFaultTicketManager().getModelById(faultIdx);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if (tp.getFaultOccurDate() != null)
                tp.setFaultOccurDateStr(sdf.format(tp.getFaultOccurDate()));
            tp.setTrainTypeAndNo(tp.getTrainTypeShortName() + "|" + tp.getTrainNo());
            return JSONUtil.write(tp);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：获取故障处理方法分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 故障处理方法分页列表
     */
    @SuppressWarnings("unchecked")
    public String findFaultMethod(int start, int limit) {
        start--;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String entity = "com.yunda.jx.base.jcgy.entity.FaultMethod";
            Map queryParamsMap = JSONUtil.read("{}", Map.class);
            map = getBaseComboManager().page(entity, null ,null ,queryParamsMap, start * limit, limit, null);
            /**封装返回值*/
            List<FaultMethodBean> beanList = new ArrayList<FaultMethodBean>();
            beanList = BeanUtils.copyListToList(FaultMethodBean.class, (List)map.get("root"));
            Page pageList = new Page((Integer)(map.get("totalProperty")),beanList);
            return JSONUtil.write(pageList.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        } 
    }
    
    /**
     * <li>说明：根据提票单idx获取需要指派的提票质量检查项列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param faultIDX 提票单IDX
     * @return 需要指派的提票质量检查项列表
     */
    public String getIsAssignCheckItems(String faultIDX) {
        try {
            List<FaultQCResult> list = getFaultQCResultManager().getIsAssignCheckItems(faultIDX);
            return JSONUtil.write(list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：获取提票处理其他作业人员列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param faultIDX 提票单IDX
     * @param operatorid 操作者ID
     * @return 提票处理其他作业人员列表
     */
    public String getOtherWorkerByTP(String faultIDX, long operatorid) {
        try {
            OmEmployee emp = getOmEmployeeSelectManager().getByOperatorid(operatorid);
            List<RepairEmpBean> list = getFaultTicketManager().getOtherWorkerByTP(faultIDX, emp.getEmpid() + "");
            return JSONUtil.write(list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：销票
     * <li>创建人：程锐
     * <li>创建日期：2015-7-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param tpData 提票实体JSON字符串
     * @param qcResults 提票质检项列表：[{ checkItemCode 质检项编码 qcEmpID 质检人员id }]
     * @param operatorid 操作者ID
     * @return 操作成功与否
     */
    public String updateForHandle(String tpData, String qcResults, long operatorid) {
        try {
            OmEmployee emp = getOmEmployeeSelectManager().getByOperatorid(operatorid);
            FaultTicket entity = JSONUtil.read(tpData, FaultTicket.class);
            if (!StringUtil.isNullOrBlank(entity.getCompleteTimeStr()))
                entity.setCompleteTime(DateUtil.parse(entity.getCompleteTimeStr(), "yyyy-MM-dd HH:mm:ss"));
            else entity.setCompleteTime(new Date());
            FaultQCResultVO[] qcResult = null;
            if (null != qcResults && qcResults.trim().length() > 0 && !qcResults.trim().equals("null")) {
                qcResult = JSONUtil.read(qcResults, FaultQCResultVO[].class);
            }
            getFaultTicketManager().updateForHandle(entity, qcResult, emp);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：获取提票质量检验分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件Json字符串
     * @param mode 抽检/必检（1/2）
     * @param operatorid 操作者ID
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 提票质量检验分页列表
     */
    public String queryFaultQCList(String searchJson, String mode, long operatorid, int start, int limit) {
        start--;
        Map map = new HashMap();
        try {
            OmEmployee emp = getOmEmployeeSelectManager().getByOperatorid(operatorid);
            Page page = getFaultQCResultQueryManager().getQCPageList(emp.getEmpid(), start * limit, limit, mode, searchJson);
            if (page.getTotal() > 0 && page.getList().size() > 0)
                map = page.extjsStore();
            return JSONUtil.write(map);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：完成提票质量检验项
     * <li>创建人：程锐
     * <li>创建日期：2015-7-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param qcResults 提票质检项列表JSON字符串：[{ idx 质检项IDX qcEmpID 质检人编码 qcEmpName 质检人员名称 qcTime 质检时间 remarks 备注 }]
     * @param operatorid 操作者id
     * @return 操作成功与否
     * @throws IOException 
     */
    public String updateFinishQCResult(String qcResults, long operatorid) throws IOException {
        try {
            FaultQCResultVO[] resultVO = StringUtil.isNullOrBlank(qcResults) ? null :JSONUtil.read(qcResults, FaultQCResultVO[].class);
            SystemContext.setAcOperator(getAcOperatorManager().getModelById(operatorid));
            getFaultQCResultManager().updateFinishQCResult(resultVO);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            if (e instanceof BusinessException) {
                BusinessException bex = (BusinessException) e;
                Map<String, String> map = new HashMap<String, String>();
                map.put("flag", "false");
                map.put("message", bex.getMessage());
                return JSONUtil.write(map);
            }
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：根据查询条件查询提票
     * <li>创建人：谭诚
     * <li>创建日期：2013-9-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件
     * @param start 开始行
     * @param limit 每页显示量
     * @return String
     */
    public String searchFaultByParam(String searchJson, int start, int limit){
        start--;
        Map map = new HashMap();
        try {
            List<Condition> whereList = new ArrayList<Condition>();   
            if(!StringUtil.isNullOrBlank(searchJson)) {             
                JSONObject jo = JSONObject.parseObject(searchJson);  
                Long operatorId = jo.getLong(Constants.OPERATOR_ID);
                if (null != operatorId) {
                    OmEmployee omEmployee = omEmployeeManager.findOmEmployee(operatorId);
                    jo.remove("operatorId");
                    searchJson = jo.toJSONString();
                    whereList.add(new Condition("ticketEmpId", Condition.EQ, omEmployee.getEmpid()));
                }
                String status = jo.getString("status");
                if(!StringUtil.isNullOrBlank(status)){
                    String[] statusList = status.split(",");
                    Integer[] starusInt = new Integer[statusList.length];
                    for(int i=0; i<statusList.length; i++){
                        starusInt[i] = Integer.valueOf(statusList[i]);
                    }
                    whereList.add(new Condition("status", Condition.IN, starusInt));
                    jo.remove("status");
                    searchJson = jo.toJSONString();
                }
                // 如果参数是类型，则用等于
                String type = jo.getString("type");
                if(!StringUtil.isNullOrBlank(type)){
                    Condition c = new Condition("type", Condition.EQ, type);
                    c.setStringLike(false);
                    whereList.add(c);
                    jo.remove("type");
                    searchJson = jo.toJSONString();
                }
                
                // 客货类型
                String vehicleType = jo.getString("vehicleType");
                if(!StringUtil.isNullOrBlank(vehicleType)){
                    Condition c = new Condition("vehicleType", Condition.EQ, vehicleType);
                    c.setStringLike(false);
                    whereList.add(c);
                    jo.remove("vehicleType");
                    searchJson = jo.toJSONString();
                }
                
            }          
            QueryCriteria<FaultTicket> query = new QueryCriteria<FaultTicket>();
            query.setEntityClass(FaultTicket.class);        
            whereList = CommonUtil.getWhereList(searchJson, whereList);        
            query.setWhereList(whereList);
            query.setStart(start * limit);
            query.setLimit(limit);
            Page page = getFaultTicketManager().findPageList(query);
            if (page.getTotal() > 0 && page.getList().size() > 0)
                map = page.extjsStore();
            return JSONUtil.write(map);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>方法说明：获取段机构树 
     * <li>方法名称：getSegmentOrgTree
     * <li>@param orgid
     * <li>@param operatorid
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-9-24 下午03:47:02
     * <li>修改人：
     * <li>修改内容：
     * @throws IOException 
     */
    @SuppressWarnings("unchecked")
    public String getSegmentOrgTree(Long orgid, Long operatorid) throws IOException{
        OmOrganizationSelectManager m = getOmOrganizationSelectManager();
        if(orgid == -1L){            
            OmOrganization org = m.getOrgByAcOperator(operatorid);
            try {
                orgid = m.getUpOrgid(org.getOrgid(), Constants.SEGMENT_LEVEL).getOrgid();
            } catch (Exception e) {
                ExceptionUtil.process(e, logger);
                return WsConstants.OPERATE_FALSE;
            }
        }
        List<Map> list = m.findSomeLevelOrgTree(String.valueOf(orgid), "degree]tream", "", "");
        return JSONUtil.write(list);
    }
    /**
     * <li>说明：故障提票获取原因分析(标签)
     * <li>创建人：张迪
     * <li>创建日期：2016-8-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 父id
     * @return 原因分析列表(标签)
     */
    @SuppressWarnings("unchecked")
    public String getReasonAnalysis( String parentIDX) {
        try {
            // 树形
            List<Map> children = getSysEosDictEntryManager().getChildNodes("JXGC_Fault_Ticket_YYFX", null,"true");
            return JSONUtil.write(children);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }   
    /**
     * <li>说明：获取在段机车车型车号信息
     * <li>创建人：张迪
     * <li>创建日期：2016-9-1
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：获取在段机车信息
     * @return 获取车型车号信息列表
     */
    @SuppressWarnings("unchecked")
    public String getTrainTypeAndTrainNo(String jsonObject) {
        if(StringUtil.isNullOrBlank(jsonObject)) {
            throw new NullPointerException(MSG_ERROR_ARGS_NULL);
        }
        JSONObject jo = JSONObject.parseObject(jsonObject);      
        // 查询记录开始索引
        int start = jo.getIntValue(Constants.START);
        // 查询记录条数
        int limit = jo.getIntValue("limit");
        start = limit * (start - 1);
        // 排序字段
        JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
        Order[] orders = JSONTools.getOrders(jArray);      
        try {    
            // 获取查询条件实体对象
            String entityJson = jo.getString(Constants.ENTITY_JSON);
            TrainAccessAccount entity = JSONUtil.read(entityJson, TrainAccessAccount.class);
            Page pageList = getTrainAccessAccountQueryManager().getInTrainAccessAccount(new SearchEntity<TrainAccessAccount>(entity, start, limit, orders));
            return JSONUtil.write(pageList.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    /**
     * <li>说明：获取在段机车车型车号，未派工数量信息
     * <li>创建人：张迪
     * <li>创建日期：2017-5-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 获取车型车号信息列表
     */
    @SuppressWarnings("unchecked")
    public String getInTrainForNotDispatch(String jsonObject) {
        if(StringUtil.isNullOrBlank(jsonObject)) {
            throw new NullPointerException(MSG_ERROR_ARGS_NULL);
        }
        JSONObject jo = JSONObject.parseObject(jsonObject);      
        // 查询记录开始索引
        int start = jo.getIntValue(Constants.START);
        // 查询记录条数
        int limit = jo.getIntValue("limit");
        start = limit * (start - 1);
        // 排序字段
        JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
        Order[] orders = JSONTools.getOrders(jArray);      
        try {    
            // 获取查询条件实体对象
            String entityJson = jo.getString(Constants.ENTITY_JSON);
            InTrainDispatchCountBean entity = JSONUtil.read(entityJson, InTrainDispatchCountBean.class);
            Page pageList = getTrainAccessAccountQueryManager().getInTrainForNotDispatch(new SearchEntity<InTrainDispatchCountBean>(entity, start, limit, orders));
            return JSONUtil.write(pageList.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    /**
     * <li>说明：删除当前提票人的票活
     * <li>创建人：张迪
     * <li>创建日期：2016-09-20
     * <li>修改人: 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            ids: [
                "8a8284f24b1054cf014b1058bc2a0002",
                "8a8284f24b1054cf014b1059764c0009"
            ],
            operatorId: 800109
        }
     * @return 
     * <li>"{'flag':'true','message':'操作成功！'}";
     * <li>"{'flag':'false','message':'操作失败！'}"
     */
    public String deleteFaultTickets(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        // 删除的当前提票人的票活主键数组
        String ids = jo.getString(Constants.IDS);
        // 当前作业处理人员ID
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        String[] idArray = JSONUtil.read(ids, String[].class);       
        String[] validateMsg = this.getFaultTicketManager().validateDelete(idArray);
        if (null != validateMsg) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(validateMsg[0]));
        }
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            this.getFaultTicketManager().logicDelete(idArray);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    } 
    
    
    /**
     * <li>说明：指派责任人
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 提票数组
     * @return
     * @throws IOException
     */
    public String assignResponser(String jsonObject) throws IOException {
        try {
            if(StringUtil.isNullOrBlank(jsonObject)) {
                throw new NullPointerException(MSG_ERROR_ARGS_NULL);
            }
            FaultTicket[] entityList = JSONUtil.read(jsonObject, FaultTicket[].class);
            getFaultTicketManager().assignResponser(entityList);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    
    
    protected FaultTicketManager getFaultTicketManager() {
        return (FaultTicketManager) Application.getSpringApplicationContext().getBean("faultTicketManager");
    }
    
    protected AcOperatorManager getAcOperatorManager() {
        return (AcOperatorManager) Application.getSpringApplicationContext().getBean("acOperatorManager");
    }
    
    protected EquipFaultManager getEquipFaultManager() {
        return (EquipFaultManager) Application.getSpringApplicationContext().getBean("equipFaultManager");
    }
    
    protected PlaceFaultManager getPlaceFaultManager() {
        return (PlaceFaultManager) Application.getSpringApplicationContext().getBean("placeFaultManager");
    }
    
    protected ProfessionalTypeManager getProfessionalTypeManager() {
        return (ProfessionalTypeManager) Application.getSpringApplicationContext().getBean("professionalTypeManager");
    }
    
    protected OmEmployeeSelectManager getOmEmployeeSelectManager() {
        return (OmEmployeeSelectManager) Application.getSpringApplicationContext().getBean("omEmployeeSelectManager");
    }
    
    protected OmOrganizationSelectManager getOmOrganizationSelectManager() {
        return (OmOrganizationSelectManager) Application.getSpringApplicationContext().getBean("omOrganizationSelectManager");
    }
    
    protected BuildUpTypeQueryManager getBuildUpTypeQueryManager() {
        return (BuildUpTypeQueryManager) Application.getSpringApplicationContext().getBean("buildUpTypeQueryManager");
    }
    
    protected JczlTrainManager getJczlTrainManager() {
        return (JczlTrainManager) Application.getSpringApplicationContext().getBean("jczlTrainManager");
    }
    
    protected TrainAccessAccountQueryManager getTrainAccessAccountQueryManager() {
        return (TrainAccessAccountQueryManager) Application.getSpringApplicationContext().getBean("trainAccessAccountQueryManager");
    }
    
    protected EosDictEntrySelectManager getEosDictEntrySelectManager() {
        return (EosDictEntrySelectManager) Application.getSpringApplicationContext().getBean("eosDictEntrySelectManager");
    }
    
    protected BaseComboManager getBaseComboManager(){
        return (BaseComboManager)Application.getSpringApplicationContext().getBean("baseComboManager");
    }
    
    protected FaultQCResultManager getFaultQCResultManager() {
        return (FaultQCResultManager)Application.getSpringApplicationContext().getBean("faultQCResultManager");
    }
    
    protected FaultQCResultQueryManager getFaultQCResultQueryManager() {
        return (FaultQCResultQueryManager)Application.getSpringApplicationContext().getBean("faultQCResultQueryManager");
    }
    
    protected StationTerminalWS getStationTerminalWS(){
        return (StationTerminalWS)Application.getSpringApplicationContext().getBean("stationTerminalWS");
    }
    protected SysEosDictEntryManager getSysEosDictEntryManager(){
        return (SysEosDictEntryManager)Application.getSpringApplicationContext().getBean("sysEosDictEntryManager");
    }
    protected TrainTypeManager getTrainTypeManager(){
        return (TrainTypeManager)Application.getSpringApplicationContext().getBean("trainTypeManager");
    }

}
