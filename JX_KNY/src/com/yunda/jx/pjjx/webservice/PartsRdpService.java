package com.yunda.jx.pjjx.webservice;

import java.io.IOException;
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
import com.yunda.base.context.SystemContext;
import com.yunda.baseapp.workcalendar.entity.WorkCalendarInfo;
import com.yunda.baseapp.workcalendar.manager.WorkCalendarInfoManager;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.EosDictEntrySelectManager;
import com.yunda.frame.yhgl.manager.IEosDictEntryManager;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.manager.TrainWorkPlanQueryManager;
import com.yunda.jx.pjjx.base.qcitemdefine.entity.QCItem;
import com.yunda.jx.pjjx.base.qcitemdefine.manager.QCItemManager;
import com.yunda.jx.pjjx.base.wpdefine.entity.WP;
import com.yunda.jx.pjjx.base.wpdefine.manager.WPManager;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdpBean;
import com.yunda.jx.pjjx.partsrdp.expendmat.entity.PartsRdpExpendMat;
import com.yunda.jx.pjjx.partsrdp.expendmat.manager.PartsRdpExpendMatManager;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpManager;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpQueryManager;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpWorkerManager;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsCheckItemDataManager;
import com.yunda.jx.pjjx.util.HttpClientUtils;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.jx.pjjx.wsbean.WPWSBean;
import com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType;
import com.yunda.jx.pjwz.partsBase.partstype.manager.PartsTypeManager;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.util.BeanUtils;
import com.yunda.webservice.common.entity.OperateReturnMessage;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：配件检修作业接口
 * <li>http://localhost:8080/CoreFrame/ydservices/PartsRdpService?wsdl
 * <li>创建人： 何涛
 * <li>创建日期： 2015-1-7 下午01:09:58 
 * <li>修改人: 何涛
 * <li>修改日期：2016-03-31
 * <li>修改内容：修改不规范的异常处理
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service("partsRdpWS")
public class PartsRdpService implements IPartsRdpService {
	
	private static final String IDX_STR = "idx";
    
	private static final String PARTSNO = "partsNo";

    /** 日志工具 */
	private Logger logger = Logger.getLogger(getClass());

	/** PartsRdp业务类,配件检修作业 */
	@Resource
	private PartsRdpManager partsRdpManager;
	
	/** PartsRdpWorker业务类,作业人员 */
	//@Resource
	//private PartsRdpWorkerManager partsRdpWorkerManager;
	
	/** OmOrganization业务类,机构服务类 */
	@Resource
	private OmOrganizationManager omOrganizationManager;
	@Resource(name="omEmployeeSelectManager")
	private OmEmployeeSelectManager orgManager;
	
	/** PartsRdpExpendMat业务类,物料消耗记录 */
	@Resource
	private PartsRdpExpendMatManager partsRdpExpendMatManager;
    
    /** PartsTypeManager业务类,配件规格型号 */
    @Resource
    private PartsTypeManager partsTypeManager;
    
    /** PartsAccountManager业务类,配件周转台账 */
    @Resource
    private PartsAccountManager partsAccountManager;
    
    /** WPManager业务类,检修作业流程 */
    @Resource
    private WPManager wPManager;
    
    @Autowired
    private IEosDictEntryManager dictManager;
    /** EosDictEntrySelectManager业务类,业务字典选择控件 */
    @Resource
    private EosDictEntrySelectManager eosDictEntrySelectManager;
    
    /** 配件检修作业查询 */
    @Resource
    private PartsRdpQueryManager partsRdpQueryManager;
    
    /** 工作日历 */
    @Resource
    private WorkCalendarInfoManager workCalendarInfoManager;

    /** 车检修作业计划查询业务类 */
    @Resource
    private TrainWorkPlanQueryManager trainWorkPlanQueryManager ;
    
    /** PartsCheckItemData业务类,可视化数据采集结果 */
    @Resource
    private PartsCheckItemDataManager partsCheckItemDataManager ;

    /** 日期格式 yyyy-MM-dd HH:mm */
    private final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    
	/**
	 * <li>说明：配件任务查看
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-07
	 * <li>修改人: 何涛
	 * <li>修改日期：2016-03-21
	 * <li>修改内容：1、修改接口参数结构，将查询条件封装到entityJson里；
                   2、增加查询字段：下车车型（unloadTrainType），组合匹配下车车型、下车车号，
                   3、增加查询字段：名称规格型号（specificationModel），组合匹配配件规格型号、配件名称
     * <li>修改人: 何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理（测试通过）
	 * @param jsonObject {
		operatorId:"800109",
		start:1,
		limit:50, 
		orders:[{
			sort:"idx",
			dir:"ASC"
		}], 
        entityJson:{
            partsNo:"25202",
            identificationCode:"25202",
            unloadTrainType:"HXD10002",
            specificationModel:"TGZS500.221.000\\SS4"
        }
	 }
	 * @return Json对象列表（数组）
	 */
    @Override
	public String findPageList(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(IService.MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        // 查询记录开始索引
        int start = jo.getIntValue(Constants.START);
        // 查询记录条数
        int limit = jo.getIntValue(Constants.LIMIT);
        start = limit * (start - 1);
        
        String entityJson = jo.getString("entityJson");
        PartsRdp rdp = JSONUtil.read(StringUtil.nvl(entityJson, Constants.EMPTY_JSON_OBJECT), PartsRdp.class);
        
        // 排序字段
        JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
        Order[] orders = JSONTools.getOrders(jArray);
        Page<PartsRdpBean> page = partsRdpQueryManager.queryPageList(new SearchEntity<PartsRdp>(rdp, start, limit, orders));
        return JSONTools.toJSONList(page);
	}
	
	/**
	 * <li>说明：查询与指定用户协作的其他处理人员
	 * <li>创建人： 何涛
	 * <li>创建日期： 2015-01-15
     * <li>修改人: 何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理,修改接口参数机返回结果注释，因接口实现变更（测试通过）
	 * 
	 * @param jsonObject {
		operatorId: "800109",
	 }
	 * @return jsonObject {
	 	{"count":2,"workerEmps":";112:陈威;124:崔云鹏;"}
	 }
	 */
    @Override
	public String findWorkerList(String jsonObject) throws IOException {
        if(StringUtil.isNullOrBlank(jsonObject)) {
            throw new NullPointerException(MSG_ERROR_ARGS_NULL);
        }
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        //code by zhangfan 2015/10/26
        List<?> list = orgManager.findTeamEmpList(SystemContext.getOmOrganization().getOrgid());
        StringBuilder empList = new StringBuilder();
        for (Object o : list) {
            Object[] obj = (Object[]) o;
            empList.append(PartsRdpWorkerManager.CONST_STR_WORKER_SEPARATOR).append(obj[0]).append(":").append(obj[2]);
        }
        JSONObject entityJson = new JSONObject();
        entityJson.put(IService.JSON_FILED_NAME_COUNT, list.size());
        entityJson.put("workerEmps", empList);
        
        return entityJson.toJSONString();
        //不知道有没有使用到以下注释代码，如果有以后再开一个接口方法
        /*String rdpIDX = jo.getString("rdpIDX");
		List<?> list = this.partsRdpWorkerManager.findWorkerList(rdpIDX);
		if (null == list || list.size() <= 0) {
			throw new NullPointerException(MSG_RESULT_IS_EMPTY);
		} else {
			StringBuilder empNames = new StringBuilder();
			for (int i = 0; i < list.size(); i++) {
				PartsRdpWorker worker = (PartsRdpWorker) list.get(i);
				empNames.append(PartsRdpWorkerManager.CONST_STR_WORKER_SEPARATOR).append(worker.getWorkEmpID() + ":" + worker.getWorkEmpName());
			}
			String workerEmps = empNames.substring(1);
			
			JSONObject entityJson = new JSONObject();
			entityJson.put(IService.JSON_FILED_NAME_COUNT, list.size());
			entityJson.put("workerEmps", workerEmps);
			
			return entityJson.toJSONString();
		}*/
	}
	
	/**
	 * <li>说明：获取质量检查项的基础配置
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-19
     * <li>修改人: 何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理（测试通过）
	 * @param jsonObject {
	 	isAssign:1		// 是否指派(整形) 是：1，否：0
	 }
	 * @return 返回质量检查基础配置的JSON数组
	 * @throws IOException
	 */
    @Override
	public String getQcItems(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        //  是否指派(整形) 是：1，否：0
        Integer isAssign = jo.getInteger("isAssign");
        List<QCItem> list = QCItemManager.getQCContent(isAssign);
        return JSONTools.toJSONList(list);
	}
	
	/**
	 * <li>说明：根据操作员ID获取组织机构名称
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-20
     * <li>修改人: 何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理（测试通过）
	 * @param jsonObject {
	 	operatorId: "800109"
	 }
	 * @return String 组织机构名称/上级组织机构名称，例如：“电检一组/检修设备车间”
	 * @throws IOException
	 */
    @Override
	public String getOrgNameByOperator(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        // 当前作业处理人员ID
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        
        OmOrganization org = this.omOrganizationManager.findByOperator(operatorId);
        if (null == org) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
        } 
        
        // 获取上级组织机构
        Long parentorgid = org.getParentorgid();
        if (null == parentorgid) {
            return org.getOrgname();
        }
        OmOrganization parentOrg = this.omOrganizationManager.getModelById(parentorgid);
        if (null == parentOrg) {
            return org.getOrgname();
        }
        return new StringBuilder(50).append(org.getOrgname()).append(SPRIT_CHAR).append(parentOrg.getOrgname()).toString();
	}
    
    /**
     * <li>说明：根据操作员id查询当前班组信息
     * <li>创建人：程梅
     * <li>创建日期：2015-9-9
     * <li>修改人: 何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理（测试通过）
     * @param jsonObject {
        operatorId: "800109"
     }
     * @return String 当前班组信息
     * @throws IOException
     */
    @Override
    public String getOrgIdByOperator(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        // 当前作业处理人员ID
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        
        OmOrganization org = this.omOrganizationManager.findByOperator(operatorId);
        if (null == org) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
        } 
        return JSONUtil.write(org) ;
    }
    
	/**
	 * <li>说明：物料消耗情况分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-23
     * <li>修改人: 何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理（测试通过）
	 * @param jsonObject {
	 	entityJson: {
	 		rdpIDX: "8a8284f24b113d15014b1140e64d0012"
	 	},
	 	start:1,
		limit:50, 
		orders:[{
			sort: "idx",
			dir: "ASC"
		}]
	 }
	 * @return Json对象列表（数组）
	 * @throws IOException
	 */
    @Override
	public String pageListForExpendMat(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        // 获取查询条件实体对象
        String entityJson = jo.getString(Constants.ENTITY_JSON);
        PartsRdpExpendMat entity = JSONUtil.read(entityJson, PartsRdpExpendMat.class);
        
        // 查询开始索引
        int start = jo.getIntValue(Constants.START);
        // 查询结束条数
        int limit = jo.getIntValue(Constants.LIMIT);
        start = limit * (start - 1);
        // 排序字段
        JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
        Order[] orders = JSONTools.getOrders(jArray);
        
        SearchEntity<PartsRdpExpendMat> searchEntity = new SearchEntity<PartsRdpExpendMat>(entity, start, limit, orders);
        Page<PartsRdpExpendMat> page = this.partsRdpExpendMatManager.findPageList(searchEntity);
        return JSONTools.toJSONList(page);
	}
	
	/**
	 * <li>说明：配件检修作业 完工 包含【修竣提交】和【无法修复】，无法修复必须填写“检修结果描述”
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-23
	 * <li>修改人: 何涛
	 * <li>修改日期：2016-03-31
	 * <li>修改内容：删除无必要的参数验证处理
	 * 
	 * @param jsonObject {
	 	idx: 8a8284f24b113d15014b1140e64d0012,
	 	repairResultDesc:"无法修复原因",
		status:"0402"
	 }
	 * @return Json对象列表（数组）
	 * @throws IOException
	 */
    @Override
	public String finishPartsRdp (String jsonObject) throws IOException {
		OperateReturnMessage msg = new OperateReturnMessage();
		try {
			JSONObject jo = JSONObject.parseObject(jsonObject);
			// 配件检修作业主键
			String idx = jo.getString(IDX_STR);
			// 检修结果描述
			String repairResultDesc = jo.getString("repairResultDesc");
			// 完工类型，一般分“修竣待验收(03)”和“无法修复（0402）”两种类型
			String status = jo.getString("status");
			
			String[] validateMsg = this.partsRdpManager.validateFinishPartsRdp(idx, status);
			if (null != validateMsg) {
				msg.setFaildFlag(validateMsg);
			} else {
				this.partsRdpManager.finishPartsRdp(idx, repairResultDesc, status);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
			msg.setFaildFlag(e.getMessage());
		}
		return JSONUtil.write(msg);
	}
    
    /**
     * <li>说明：查询自修目录配件中检修班组为当前班组的配件规格型号
     * <li>创建人：程梅
     * <li>创建日期：2015-8-31
     * <li>修改人: 何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理（测试通过）
     * @param jsonObject {
           "operatorId":"800109",
           "entityJson":{
               "repairOrgID":"106"
           }
        }
     * @return Json对象列表（数组）
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Override
    public String findRepairListPartsTypeTree(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        //获取查询条件实体对象
        String entityJson = jo.getString(Constants.ENTITY_JSON);
        Map<String, String> params = JSONUtil.read(entityJson, Map.class);
        List<HashMap> list = this.partsTypeManager.findRepairListPartsType(params);
        if (null == list || list.size() <= 0) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
        }
        return JSONUtil.write(list);
    }
    
    /**
     *TODO 待确认代码是否还有用
     * <li>说明：查询自修目录配件中检修班组为当前班组并且不在检修中的配件
     * <li>创建人：程梅
     * <li>修改人: 何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理（测试未通过）
     * @param jsonObject {
            "operatorId":"800109",
            "teamOrgId":"106",
            "isNewParts":"旧','新",
            "specificationModel":"\80006912000350\HXD1",
            "start":0,
            "limit":50,
            "entityJson":{
                "unloadTrainTypeIdx":"",
                "unloadTrainNo":"",
                "unloadRepairClassIdx":"",
                "partsStatus":"",
                "partsNo":""
            }
        }
     * @return Json对象列表（数组）
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Override
    public String findPartsAccountList(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        //查询开始索引
        int start = jo.getIntValue(Constants.START);
        // 查询工艺条数
        int limit = jo.getIntValue(Constants.LIMIT);
        start = limit * (start - 1);
        //排序字段
        JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
        Order[] orders = JSONTools.getOrders(jArray);
        //获取查询条件实体对象
        String entityJson = StringUtil.nvlTrim(jo.getString(Constants.ENTITY_JSON), "{}" );
        String repairOrgId = jo.getString("teamOrgId");//检修班组id--当前班组
        String isNewParts = jo.getString("isNewParts");//配件是否新品
        String specificationModel = jo.getString("specificationModel");//规格型号
        PartsAccount account = JSONUtil.read(entityJson, PartsAccount.class);
        SearchEntity<PartsAccount> searchEntity = new SearchEntity<PartsAccount>(account, start, limit, orders);
        Page<PartsAccount> page = null;
        try {
            page = this.partsAccountManager.getListForPartsRdp(searchEntity,repairOrgId,isNewParts,specificationModel);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
        return JSONTools.toJSONList(page);
    }
    
    /**
     * TODO 待确认代码是否还有用
     * <li>说明：查询某配件规格型号对应的检修作业流程列表
     * <li>创建人：程梅
     * <li>创建日期：2015-8-31
     * <li>修改人: 何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理（测试通过）
     * @param jsonObject {
            "operatorId":"800109",
            "partsTypeIdx":"100496",
            "start":0,
            "limit":50,
        }
     * @return Json对象列表（数组）
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Override
    public String findWPList(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        //查询开始索引
        int start = jo.getIntValue(Constants.START);
        // 查询工艺条数
        int limit = jo.getIntValue(Constants.LIMIT);
        start = limit * (start - 1);
        //排序字段
        JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
        Order[] orders = JSONTools.getOrders(jArray);
        //获取查询条件
        String partsTypeIdx = jo.getString("partsTypeIdx");//配件规格型号id
        Page<WP> page = null;
        try {
            page = this.wPManager.getListForPartsRdp(partsTypeIdx,start, limit, orders);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
        return JSONTools.toJSONList(page);
    }
    
    /**
     * TODO 待确认代码是否还有用
     * <li>说明：查询配件状态列表
     * <li>创建人：程梅
     * <li>创建日期：2015-8-31
     * <li>修改人: 何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理（测试通过）
     * @param jsonObject {
          "operatorId":"800109",
          "isChecked":"false",
          "dicttypeid":"PJWZ_PARTS_ACCOUNT_STATUS",
          "parentIDX":"01"
      }
     * @return Json对象列表（数组）
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Override
    public String findPartsStatusForTree(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        //业务字典id
        String dicttypeid = jo.getString("dicttypeid");
        //父节点id
        String parentIDX = jo.getString("parentIDX");   // 父节点id，初始化默认为在册--01
        Boolean isChecked = jo.getBoolean("isChecked");   // 是否被选中
        List <EosDictEntry> list = new ArrayList<EosDictEntry>();
        List<HashMap> children = new ArrayList<HashMap>();
        if(parentIDX.equals("ROOT_0")) {
            list = dictManager.findRoots(dicttypeid);
        } else {
            list = dictManager.findChildsByIds(dicttypeid, parentIDX);          
        }
        HashMap nodeMap = null;
        for (EosDictEntry dict : list) {
            Boolean isLeaf = eosDictEntrySelectManager.isLeaf(dict.getId().getDictid(),dicttypeid);
            nodeMap = new HashMap();
            nodeMap.put("id", dict.getId().getDictid());
            nodeMap.put("text", dict.getDictname());
            nodeMap.put("leaf", isLeaf);
            nodeMap.put("parentid", dict.getParentid());
            nodeMap.put("checked", isChecked);
            children.add(nodeMap);
        }
        
        if (null == children || children.size() <= 0) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
        }
        return JSONUtil.write(children);
    }
    
    /**
     * <li>说明：兑现生成任务单
     * <li>创建人：程梅
     * <li>创建日期：2015-8-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {"operatorId":"800109","partsAccountIDX":"8a8284644f16cfc1014f1b3d057900a1","wpIdx":"8a8283a84f723434014f7256caed002f"}
     *                     操作员id  配件id   需求单id
     * @param rdpData  {"dutyEmpName":"王谦","dutyEmpID":"109","repairOrgID":"106","repairOrgName":"电检一组","planStartTime":"2015-08-31 13:06"} 负责人、计划开始时间
     * @param workerList [{"workEmpID":"109","workEmpName":"王谦"}] 施修人员信息
     * @return Json对象列表（数组）
     * @throws IOException
     * CRIF 2015-10-27 汪东良 高  配件检修兑现方式更改，此接口服务需要重新优化； 
     *//*
    @Override
    public String savePartsRdp(String jsonObject,String rdpData, String workerList) throws IOException {
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            if(StringUtil.isNullOrBlank(jsonObject)) {
                throw new NullPointerException(MSG_ERROR_ARGS_NULL);
            }
            JSONObject jo = JSONObject.parseObject(jsonObject);
            
            Long operatorId = jo.getLong(Constants.OPERATOR_ID);
            if (null == operatorId) {
                throw new NullPointerException(MSG_ERROR_ARGS_NULL_OPERATOR_ID);
            }
            // 设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            
            // 配件id
            String partsAccountIDX = jo.getString("partsAccountIDX");
            // 检修需求单id
            String wpIdx = jo.getString("wpIdx");
            //负责人、计划开始时间信息
            PartsRdp partsRdp = (PartsRdp)JSONUtil.read(rdpData, PartsRdp.class);
            //施修人员信息
            PartsRdpWorker[] workers = (PartsRdpWorker[])JSONUtil.read(workerList, PartsRdpWorker[].class);
            //由于修改配件检修兑现单后报错因此暂时注释
           // partsRdpManager.savePartsRdp(partsRdp, workers, wpIdx, partsAccountIDX);
            
        } catch (Exception e) {
            msg.setFaildFlag(e.getMessage());
        }
        return JSONObject.toJSONString(msg);
    }*/    

    /**
     * <li>说明：根据识别码获取当前操作类型
     * <li>创建人：程锐
     * <li>创建日期：2015-9-29
     * <li>修改人：何涛
     * <li>修改日期：2016-03-18
     * <li>修改内容：该方法定制化太强，建议使用getOperateType()方法
     * <li>修改人：何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理（测试通过）
     * @param jsonObject {
             "identityCode": "SBM0003"
         }
     * @return 操作类型 1配件任务处理 2生成配件检修任务 3启动生产 ""未找到配件
     */
    @Deprecated
    @Override
    public String getOpTypeByIdentity(String jsonObject) {
        jsonObject = jsonObject.replace("identityCode", "identificationCode");
        return this.getOperateType(jsonObject);
    }
    
    /**
     * <li>说明：根据配件编号获取当前操作类型
     * <li>创建人：程锐
     * <li>创建日期：2015-9-29
     * <li>修改人：何涛
     * <li>修改日期：2016-03-18
     * <li>修改内容：该方法定制化太强，建议使用getOperateType()方法
     * <li>修改人：何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理（测试通过）
     * @param jsonObject {
         "partsNo": "X00003"
       }
     * @return 操作类型 1配件任务处理 2生成配件检修任务 3启动生产 ""未找到配件
     */
    @Deprecated
    @Override
    public String getOpTypeByPartsNo(String jsonObject) {
        return this.getOperateType(jsonObject);
    }
    
    /**
     * <li>说明：根据“配件信息主键”“配件识别码”或者“配件编号”查询配件当前的可操作类型，例如：1、生成配件检修任务单；2、启动生产等
     * <li>创建人：何涛   
     * <li>创建日期：2016-03-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * {
     *    "partsAccountIDX": "8a8284f250e9e6430150ea3931340030",
     *    "identificationCode": "13134345", 
     *    "partsNo": "ox89245" 
     * }
     * @return 操作类型 1:配件任务处理 2:生成配件检修任务 3:启动生产 ""未找到配件
     */
    @Override
    public String getOperateType(String jsonObject) {
        PartsRdp partsRdp = null;
        try {
            // 解析JSON对象
            partsRdp = JSONUtil.read(jsonObject, PartsRdp.class);
        } catch (Exception e) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_JSON_PARSE_ERROR + ": " + jsonObject));
        }
        Object type = partsRdpQueryManager.findPartsProMap(partsRdp).get("type");
        return null == type ? "" : type.toString();
    }

    /**
     * <li>说明：根据识别码获取配件信息
     * <li>创建人：程锐
     * <li>创建日期：2015-9-29
     * <li>修改人：何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理（测试通过）
     * @param jsonObject {
             "identityCode": "SBM0003"
         }
     * @return 配件信息
     */
    @Override
    public String getPartsAccountByIdentity(String jsonObject) {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        String identityCode = jo.getString("identityCode");
        
        PartsAccount account = partsAccountManager.getAccountByIdCode(identityCode);
        if (account == null) {
            return "";
        }
        account.setPartsStatus(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", account.getPartsStatus(), ""));            
        try {
            return JSONUtil.write(account);
        } catch (IOException e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
    }
    
    /**
     * <li>说明：根据配件编号获取配件信息
     * <li>创建人：程锐
     * <li>创建日期：2015-9-29
     * <li>修改人：何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理（测试通过）
     * @param jsonObject {
         "partsNo": "X00003"
       }
     * @return 配件信息
     */
    @Override
    public String getPartsAccountByPartsNo(String jsonObject) {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        String partsNo = jo.getString(PARTSNO);
        
        PartsAccount account = partsAccountManager.getAccountByPartsNo(partsNo);
        if (account == null) {
            return "";
        }
        account.setPartsStatus(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", account.getPartsStatus(), ""));
        try {
            return JSONUtil.write(account);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
    }
    
    /**
     * <li>说明：配件检修任务单-兑现生成任务单
     * <li>创建人：程锐
     * <li>创建日期：2015-9-29
     * <li>修改人：何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理
     * @param jsonObject {
             "operatorId":"800109",
             "partsAccountIDX":"8a8284644f16cfc1014f1b3d057900a1",
             "wpIdx":"8a8283a84f723434014f7256caed002f",
             "planStartTime":"2015-11-10 15:50"
         }
     * @return 操作成功与否
     */
    @Override
    public String saveForPartsRdp (String jsonObject) {
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            
            Long operatorId = jo.getLong(Constants.OPERATOR_ID);
            if (null == operatorId) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
            }
            // 设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            
            // 配件id
            String partsAccountIDX = jo.getString("partsAccountIDX");
            // 检修需求单id
            String wpIdx = jo.getString("wpIdx");
            String planStartTime = jo.getString("planStartTime");
            String calendarIdx = jo.getString("calendarIdx");
            String calendarName = jo.getString("calendarName");
            PartsRdp rdp = new PartsRdp();
            rdp.setPartsAccountIDX(partsAccountIDX);
            rdp.setWpIDX(wpIdx);
            if (!StringUtil.isNullOrBlank(planStartTime)) {
                rdp.setPlanStartTime(DateUtil.parse(planStartTime, YYYY_MM_DD_HH_MM));
            }
            rdp.setCalendarIdx(calendarIdx);
            rdp.setCalendarName(calendarName);
            rdp = partsRdpManager.getEntityByWpIdxAndPaIdx(rdp);
            partsRdpManager.saveAndStartPartsRdpSingle(rdp, SystemContext.getAcOperator());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag("网络连接失败，请联系管理员");
        }
        return JSONObject.toJSONString(msg);
    }
    
    /**
     * <li>说明：配件检修任务单-启动生产
     * <li>创建人：程锐
     * <li>创建日期：2015-9-30
     * <li>修改人：何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理
     * @param jsonObject
     * {
     *   idx 配件检修任务单IDX,
     *   operatorId 操作员ID
     * }
     * @return 操作成功与否
     */
    @Override
    public String startPartsRdp (String jsonObject) {
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            Long operatorId = jo.getLong(Constants.OPERATOR_ID);
            if (null == operatorId) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
            }
            // 设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            String idx = jo.getString(IDX_STR);
            partsRdpManager.updateStartPartsRdp(idx);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONObject.toJSONString(msg); 
    }
    
    /**
     * <li>说明：终止配件作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-10-12
     * <li>修改人：何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理
     * @param jsonObject
     * {
     *   idx 配件检修任务单IDX,
     *   operatorId 操作员ID
     * }
     * @return 操作成功与否
     */
    @Override
    public String terminatePartsRdp (String jsonObject) {
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            Long operatorId = jo.getLong(Constants.OPERATOR_ID);
            if (null == operatorId) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
            }
            // 设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            String idx = jo.getString(IDX_STR);
            String [] ids = new String[1];
            ids[0] = idx;
            partsRdpManager.updateStatus(PartsRdp.STATUS_YZZ, ids);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONObject.toJSONString(msg); 
    }

    /**
     * <li>方法说明：配件合格验收
     * <li>方法名：acceptanceParts
     * @param json {operatorId:000, idx: "xxx"}
     * @return 处理结果
     * <li>创建人： 张凡
     * <li>创建日期：2015-10-16
     * <li>修改人：程锐
     * <li>修改内容：合格验收时，终止所有未完成的质量检查项（包括必检和抽检）
     * <li>修改日期：2015-11-12
     */
	@Override
	public String acceptanceParts(String json) {
		OperateReturnMessage msg = new OperateReturnMessage();
		try{
			JSONObject jo = JSONObject.parseObject(json);
			String[] ids = new String[]{ jo.getString(IDX_STR) };
			String[] result = partsRdpManager.validateUpdate(PartsRdp.STATUS_JXHG, ids);
			
			if(result == null){
				SystemContextUtil.setSystemInfoByOperatorId(jo.getLong(Constants.OPERATOR_ID));                                
				partsRdpManager.updateStatus(PartsRdp.STATUS_JXHG,ids);
			}else{
				msg.setFaildFlag(result);
			}
		} catch (Exception e) {
            ExceptionUtil.process(e, logger);
			msg.setFaildFlag(e.getMessage());
		}
		return JSONObject.toJSONString(msg);
	}
	
    /**
     * <li>方法说明：获取配件检修需求单列表(用于工位终端上选择检修需求单列表)
     * <li>方法名：wpList
     * <li>创建人： 张凡
     * <li>创建日期：2015-10-16
     * <li>修改人：何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理（测试通过）
     * @param partsTypeIdx 配件型号主键
     * @return 配件检修需求单列表
     */
    @Override
    public String wpList(String partsTypeIdx) {
        try {
            Page<WP> wps = wPManager.getListForPartsRdp(partsTypeIdx, 0, 100, null);
            if (wps.getTotal() == 0) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
            }
            return JSONTools.toJSONList(wps.getTotal(), BeanUtils.copyListToList(WPWSBean.class, wps.getList()));
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
    }

    /**
     * TODO 待确定是否有用
     * <li>说明：根据配件识别码获取配件作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-10-10
     * <li>修改人：何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理（测试通过）
     * @param jsonObject {
         "identificationCode": "PJ-20151102005"
       }  
     * @return 配件作业计划
     * @throws IOException
     */
    @Override
    public String getPartsRdpByIdentity(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        String identityCode = jo.getString("identityCode");            
//        String identityCode = jo.getString("identificationCode");            
        return JSONObject.toJSONString(partsRdpQueryManager.getRdpByIdentityCode(identityCode));
    }
    
    /**
     * TODO 待确定是否有用
     * <li>说明：根据配件编号获取配件作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-10-10
     * <li>修改人：何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理（测试通过）
     * @param jsonObject {
         "partsNo": "3333"
       }
     * @return 配件作业计划
     * @throws IOException
     */
    @Override
    public String getPartsRdpByPartsNo(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        String partsNo = jo.getString(PARTSNO);
        return JSONObject.toJSONString(partsRdpQueryManager.getRdpByPartsNo(partsNo));
    }
	
    /**
     * <li>说明：生成配件检修任务时-选择工作日历列表
     * <li>创建人：程锐
     * <li>创建日期：2015-11-24
     * <li>修改人：何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理（测试通过）
     * @return 工作日历列表
     */
    @Override
    public String getCalendarList() {
        SearchEntity<WorkCalendarInfo> searchEntity = new SearchEntity<WorkCalendarInfo>(new WorkCalendarInfo(), 0, 100, null);
        Page<WorkCalendarInfo> wcInfo = workCalendarInfoManager.findPageList(searchEntity);
        try {
            return JSONTools.toJSONList(wcInfo);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
    }
    
    /**
     * <li>说明：根据配件作业计划IDX获取配件作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-10-10
     * <li>修改人：何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理（测试通过）
     * @param jsonObject {
             "rdpIDX": "8a8284f25307e8f2015308131b8b000c"
         }  
     * @return 配件作业计划
     * @throws IOException
     */
    @Override
    public String getPartsRdpByIDX(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        String rdpIDX = jo.getString("rdpIDX");        
        PartsRdp partsRdp = partsRdpQueryManager.getModelById(rdpIDX);
        if (partsRdp == null) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
        }
        // TODO 不建议使用此种方式转换日期类型字段，可以参考JSONTools.toJSON(Object object, DateFormat format)方法
        Date planStartTime = partsRdp.getPlanStartTime();
        Date planEndTime = partsRdp.getPlanEndTime();
        if (planStartTime != null) {
            partsRdp.setPlanStartTimeStr(DateUtil.getDateByMillSeconds(planStartTime.getTime(), YYYY_MM_DD_HH_MM));
        }
        if (planEndTime != null) {
            partsRdp.setPlanEndTimeStr(DateUtil.getDateByMillSeconds(planEndTime.getTime(), YYYY_MM_DD_HH_MM));
        }
        return JSONObject.toJSONString(partsRdp);
    }
    
    /**
     * <li>说明：根据配件作业计划主键获取未处理的记录单、工艺工单、提票单数量
     * <li>创建人：程锐
     * <li>创建日期：2016-2-3
     * <li>修改人：何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：修改不规范的异常处理（测试通过）
     * @param jsonObject {
            "rdpIDX": "8a8284f25146a0d8015146a7670d0003",
            "rdpNodeIDX": "8a8284f250e9e6430150ea1f1d60001d"
         }
     * @return 未处理的记录单、工艺工单、提票单数量
     * {
     *      recordCard: 2, 
     *      tecCard: 4,
     *      notice:  1
     * }
     * @throws IOException
     */
    @Override
    public String getWCLCountByIDX(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        // 配件检修作业主键
        String rdpIDX = jo.getString("rdpIDX");        
        // 配件检修作业流程节点主键
        String rdpNodeIDX = jo.getString("rdpNodeIDX");        
        Map<String, Integer> map = partsRdpQueryManager.getWCLCountByIDX(rdpIDX, rdpNodeIDX);
        return JSONObject.toJSONString(map);
    }
    
    /**
     * <li>说明：车型车号列表【从处理中的机车检修作业计划中获取】
     * <li>创建人：程梅
     * <li>创建日期：2016-3-16
     * <li>修改人：何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：重构（测试通过）
     * @return JSON列表（数组）{
            count:100,
            list:[{
                typeID:"207G",                              // 车型主键
                typeName:"SS4G0004",                        // 车型车号
                shortName:"SS4G"                            // 车型简称
            }]
        }
     * @throws IOException
     */
    @Override
    public String getTrainListFromWorkPlan() throws IOException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("workPlanStatus", Condition.IN_STR.concat(Constants.SINGLE_QUOTE_MARK.concat(TrainWorkPlan.STATUS_HANDLING).concat(Constants.SINGLE_QUOTE_MARK)));
        List<TrainWorkPlan> workPlanList = trainWorkPlanQueryManager.getTrainWorkPlanList(paramMap);
        if (null == workPlanList || workPlanList.size() <= 0) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
        }
        
        List<Map<String, Object>> entityList = new ArrayList<Map<String,Object>>(workPlanList.size());
        Map<String, Object> typeJson = null;
        for (TrainWorkPlan workPlan : workPlanList) {
            typeJson = new HashMap<String, Object>();
            typeJson.put("typeID", workPlan.getTrainTypeIDX());                                 // 车型代码
            typeJson.put("typeName", workPlan.getTrainTypeShortName()+workPlan.getTrainNo());   // 车型名称+车号
            typeJson.put("shortName", workPlan.getTrainTypeShortName());                        // 简称
            entityList.add(typeJson);
        }
        return JSONTools.toJSONList(workPlanList.size(), entityList);
    }
    
    /**
     * <li>说明：配件规格型号输入控件数据查询
     * <li>创建人：程梅
     * <li>创建日期：2016-3-16
     * <li>修改人：何涛 
     * <li>修改日期：2016-03-21
     * <li>修改内容：查询结果集错误，修改
     * @param jsonObject {
        operatorId: "800109",
        start:1,
        limit:50, 
        orders:[{
            sort: "partsName",
            dir: "ASC"
        }]
     }
     * @return Json对象列表（数组）
     * @throws IOException
     */
    @Override
    public String findPartsRepairListPageList(String jsonObject) throws IOException {
        if (StringUtil.isNullOrBlank(jsonObject)) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL));
        }
        Page<PartsType> page = findRepairPartsType(jsonObject);
        return JSONTools.toJSONList(page.getTotal(), page.getList());
    }
    
    /**
     * <li>说明：配件检修启动任务，配件规格型号列表数据查询
     * <li>创建人：何涛
     * <li>创建日期：2016-3-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容： 
     * @param jsonObject {
        operatorId: "800109",
        start:1,
        limit:50, 
        orders:[{
            sort: "partsName",
            dir: "ASC"
        }]
     }
     * @return Json对象列表（数组）
     * @throws IOException
     */
    @Override
    public String findPageForPartsType(String jsonObject) throws IOException {
        if(StringUtil.isNullOrBlank(jsonObject)) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL));
        } 
        Page<PartsType> page = findRepairPartsType(jsonObject);
        return JSONTools.toJSONList(page);
    }

    /**
     * <li>说明：分页查询，查询当前系统操作人员所在班组可以承修的配件型号
     * <li>创建人：何涛
     * <li>创建日期：2016-3-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
        operatorId: "800109",
        start:1,
        limit:50, 
        orders:[{
            sort: "partsName",
            dir: "ASC"
        }]
     }
     * @return 分页列表
     */
    private Page<PartsType> findRepairPartsType(String jsonObject) {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            throw new NullPointerException(MSG_ERROR_ARGS_NULL_OPERATOR_ID);
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        //查询开始索引
        int start = jo.getIntValue(Constants.START);
        // 查询工艺条数
        int limit = jo.getIntValue(Constants.LIMIT);
        start = limit * (start - 1);
        //排序字段
        JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
        Order[] orders = JSONTools.getOrders(jArray);
        
        Page<PartsType> page = partsTypeManager.findPageForPartsType(start, limit, orders);
        List<PartsType> list = page.getList();
        for (PartsType repairList : list) {
            // 配件名称+规格型号
            repairList.setPartsName(repairList.getPartsName() + "(" + repairList.getSpecificationModel() + ")");
        }
        return page;
    }
    
    /**
     * <li>说明：根据配件检修计划idx同步更新可视化系统检测项数据
     * <li>创建人：林欢
     * <li>创建日期：2016-6-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象 
     * @return 返回提示信息
     */
    public String synPartsCheckItemDataAndSavePartsRdpRecordDI(String jsonObject){
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        if(StringUtil.isNullOrBlank(jsonObject)) {
            throw new NullPointerException(MSG_ERROR_ARGS_NULL);
        }
        
        JSONObject jo = JSONObject.parseObject(jsonObject);
        String rdpRecordCardRdpIDX = jo.getString("rdpRecordCardRdpIDX");
        
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            map = partsCheckItemDataManager.doSyn(rdpRecordCardRdpIDX, map, HttpClientUtils.instence.getProperties().getProperty("partsCheckItemData"));
            if ("false".equals(map.get(Constants.SUCCESS))) {
                if (map.get(Constants.ERRMSG) !=null) {
                    msg.setFaildFlag(map.get(Constants.ERRMSG).toString());
                }
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONObject.toJSONString(msg);   
    }
}