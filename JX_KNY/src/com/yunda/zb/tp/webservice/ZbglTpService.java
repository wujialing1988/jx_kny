package com.yunda.zb.tp.webservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sun.misc.BASE64Encoder;

import com.alibaba.fastjson.JSONObject;
import com.yunda.base.context.SystemContext;
import com.yunda.frame.baseapp.upload.entity.Attachment;
import com.yunda.frame.baseapp.upload.manager.AttachmentManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.FileUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.frame.yhgl.manager.IOmEmployeeManager;
import com.yunda.jx.base.jcgy.entity.EquipFault;
import com.yunda.jx.base.jcgy.manager.EquipFaultManager;
import com.yunda.jx.component.manager.BaseComboManager;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;
import com.yunda.jx.jxgc.buildupmanage.entity.PlaceFault;
import com.yunda.jx.jxgc.buildupmanage.manager.BuildUpTypeQueryManager;
import com.yunda.jx.jxgc.buildupmanage.manager.PlaceFaultManager;
import com.yunda.jx.jxgc.tpmanage.entity.RepairEmpBean;
import com.yunda.jx.pjwz.partsBase.professionaltype.entity.ProfessionalType;
import com.yunda.jx.pjwz.partsBase.professionaltype.manager.ProfessionalTypeManager;
import com.yunda.jx.webservice.stationTerminal.base.entity.FaultMethodBean;
import com.yunda.twt.trainaccessaccount.webservice.ITrainAccessAccountService;
import com.yunda.util.BeanUtils;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;
import com.yunda.zb.common.ZbConstants;
import com.yunda.zb.common.manager.TrainNoManager;
import com.yunda.zb.common.webservice.ITerminalCommonService;
import com.yunda.zb.tp.entity.ZbglTp;
import com.yunda.zb.tp.entity.ZbglTpException;
import com.yunda.zb.tp.entity.ZbglTpRepair;
import com.yunda.zb.tp.manager.ZbglTpExceptionManager;
import com.yunda.zb.tp.manager.ZbglTpManager;
import com.yunda.zb.tp.manager.ZbglTpRepairManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 临碎修提票webservice实现类
 * <li>创建人：程锐
 * <li>创建日期：2015-1-26
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "zbglTpService")
public class ZbglTpService implements IZbglTpService {
    
    /**
     * 日志对象
     */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * 机车出入段业务对象
     */
    @Autowired
    private ITrainAccessAccountService trainAccessAccountService;
    
    /**
     * 故障现象编码业务对象
     */
    @Autowired
    private EquipFaultManager equipFaultManager;
    
    /**
     * 组成查询业务对象
     */
    @Autowired
    private BuildUpTypeQueryManager buildUpTypeManager;
    
    /**
     * 故障现象业务对象
     */
    @Autowired
    private PlaceFaultManager placeFaultManager;
    
    /**
     * 专业类型业务对象
     */
    @Autowired
    private ProfessionalTypeManager professionalTypeManager;
    
    /** 车号业务类 */
    @Autowired
    private TrainNoManager trainNoManager;
    
    /**
     * 操作者业务对象
     */
    @Autowired
    private AcOperatorManager acOperatorManager;
    
    /**
     * 临碎修提票业务对象
     */
    @Autowired
    private ZbglTpManager zbglTpManager;
    
    /**
     * 附件管理业务对象
     */
    @Autowired
    private AttachmentManager attachmentManager;
    
    /**
     * 通用下拉列表业务对象
     */
    @Autowired
    private BaseComboManager baseComboManager;
    
    /**
     * 人员选择业务对象
     */
    @Autowired
    private OmEmployeeSelectManager omEmployeeSelectManager ;
    /**
     * 工位终端共有接口
     */
    @Autowired
    private ITerminalCommonService terminalCommonService;
    
	/** 人员业务类：omEmployeeManager */
	@Resource(name="omEmployeeManager")
	private IOmEmployeeManager omEmployeeManager;
    
    /** 整备提票返修业务类：zbglTpRepairManager */
    @Autowired
    private ZbglTpRepairManager zbglTpRepairManager;
    
    /** 整备提票遗留活业务类：zbglTpExceptionManager */
    @Autowired
    private ZbglTpExceptionManager zbglTpExceptionManager;
    
    /** 系统出错信息 */
    private static final String MESSAGE_ERROR = "error";
    
    /**
     * <li>说明：获取承修车型
     * <li>创建人：程锐
     * <li>创建日期：2015-1-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 承修车型列表JSON字符串
     */
    public String getUndertakeTrainType() {
        return trainAccessAccountService.getUndertakeTrainType();
    }
    
    /**
     * <li>说明：获取故障字典码表数据列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param flbm 分类编码
     * @param faultName 故障字典名称查询条件
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 故障字典码表数据列表JSON字符串
     */
	public String findfaultList(String flbm, 
								String faultName,
								int start, 
								int limit) { 
		start--;
        try {
            EquipFault entity = new EquipFault();
            if (!StringUtil.isNullOrBlank(faultName)) {
                entity.setFaultName(faultName);
            }
            SearchEntity<EquipFault> searchEntity = new SearchEntity<EquipFault>(entity, start * limit, limit, null);
            Map map = equipFaultManager.faultList(searchEntity, flbm).extjsStore();
            return JSONUtil.write(map);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return MESSAGE_ERROR;
        }
	}
    
	/**
     * <li>说明：根据车型车号获取组成树根节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx 车型主键
     * @param trainNo 车号
     * @return 组成树根节点列表JSON字符串
     */
    public String getBuildUpType(String trainTypeIdx, String trainNo) {
        try {
            List<HashMap> list = buildUpTypeManager.getRootBuildUp(trainTypeIdx, trainNo);
            return JSONUtil.write(list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return MESSAGE_ERROR;
        }
    }
    
    /**
     * TODO 有些逻辑现在是工位终端那边处理，可调整至web端处理，具体逻辑见BuildUpTypeQueryAction.queryBuildPlaceList
     * <li>说明：获取下级组成树节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 上级父节点idx
     * @param partsBuildUpTypeIdx 所属组成型号idx
     * @param parentPartsAccountIdx 上级安装配件信息主键
     * @param isVirtual 表示下层节点是否是虚拟位置下节点
     * @param buildUpPlaceFullCode 位置编码全名（从树节点获取）
     * @param trainNo 车号
     * @return 下级组成树节点列表JSON字符串
     */
	public String getBuildUpTypeTree(String parentIDX, 
									 String partsBuildUpTypeIdx, 
									 String parentPartsAccountIdx, 
									 String isVirtual, 
									 String buildUpPlaceFullCode, 
									 String trainNo) {
		try {
            List<HashMap<String, Object>> list = buildUpTypeManager.allTree(parentIDX, partsBuildUpTypeIdx, "", isVirtual);
            Collections.reverse(list);
            return JSONUtil.write(list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return MESSAGE_ERROR;
        }
    }
    
    /**
     * <li>说明：通过组成位置主键获取故障现象数据
     * <li>创建人：程锐
     * <li>创建日期：2015-1-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param buildUpPlaceIdx 组成位置主键
     * @return 故障现象列表JSON字符串
     */
    public String getPlaceFault(String buildUpPlaceIdx) {
        try {
            List<PlaceFault> faultList = placeFaultManager.findFaultList(buildUpPlaceIdx);
            PlaceFault other = new PlaceFault();
//            other.setFaultId(PlaceFault.OTHERID);
//            other.setFaultName("其它");
            faultList.add(faultList.size(), other);
            return JSONUtil.write(faultList);
        } catch (IOException e) {
            ExceptionUtil.process(e, logger);
            return MESSAGE_ERROR;
        }
    }
    
    /**
     * <li>说明：获取专业类型
     * <li>创建人：程锐
     * <li>创建日期：2015-1-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 专业类型分页列表JSON字符串
     */
    @SuppressWarnings("unchecked")
    public String getProfessionalType(int start, int limit) {
        try {
            start--;
            ProfessionalType type = new ProfessionalType();
            type.setStatus(ProfessionalType.status_start);
            SearchEntity<ProfessionalType> searchEntity = new SearchEntity<ProfessionalType>(type, start * limit, limit, null);
            Page page = professionalTypeManager.findPageList(searchEntity);
            return JSONUtil.write(page.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    
    /**
     * <li>说明：根据父节点查询专业树
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 父节点
     * @return 专业树列表
     * @throws Exception
     */
    public String findProfessionalTree(String jsonObject) throws Exception {
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        String treeStr = "";
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            String parentIDX = StringUtil.nvlTrim(jo.getString("parentIDX"), "ROOT_0");
            if (!StringUtil.isNullOrBlank(parentIDX)) {        
                children = professionalTypeManager.findProfessionalTree(parentIDX, ProfessionalType.status_start+"");
            }
        }catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
            map.put("flag", false);
            map.put("message", "操作失败！");
        } finally{
            if (map.get("flag") != null && !(Boolean)map.get("flag")) {
                treeStr = JSONUtil.write(map);
            }
            else {
                treeStr = JSONUtil.write(children);
            }
        }
        return treeStr;
    }

    /**
     * <li>说明：根据车型获取车号列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：添加车号查询
     * @param trainTypeIdx 车型主键
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 车号列表JSON字符串
     */
    @SuppressWarnings("unchecked")
    public String getTrainNoByTrainType(String trainTypeIdx,String trainNo, int start, int limit) {
        try {
            start--;
            Map queryParamsMap = new HashMap();
            queryParamsMap.put("trainTypeIDX", trainTypeIdx);
            queryParamsMap.put("trainNo", trainNo);
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
     * <li>说明：提票录入处理
     * <li>创建人：程锐
     * <li>创建日期：2015-1-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param jsonData 提票信息Json集合（需要按照ZbglTp提票实体进行数据组装）
     * @param imgFilePath 上传照片地址，多个地址以","分隔
     * @return 操作结果
     */
    public String saveFaultNoticeFromGrid(Long operatorid, String jsonData, String imgFilePath) {
        try {
            AcOperator ac = acOperatorManager.getModelById(operatorid);
            SystemContext.setAcOperator(ac);
            
            ZbglTp[] entities = JSONUtil.read(jsonData, ZbglTp[].class);
            ZbglTp entity = entities.length > 0 ? entities[0] : null;
            if (entity != null) {
                zbglTpManager.saveTpAndInst(entities, entity);
                zbglTpManager.getDaoUtils().flush();
                copyFaultImg(entity.getIdx(), ZbConstants.UPLOADPATH_TP_IMG, operatorid, imgFilePath);
                return "提票成功";
            }
            return "false";
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return MESSAGE_ERROR;
        }
    }
    
    /**
     * <li>说明：上传故障提票照片
     * <li>创建人：程锐
     * <li>创建日期：2015-1-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param imgStr 以Base64编码的图片字符串
     * @return 保存在服务器上的临时文件全路径
     */
    public String uploadFaultImg(String imgStr) {
        if (StringUtil.isNullOrBlank(imgStr))
            return "";
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        String imgFilePath = "";
        try {
            byte[] bytes = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {
                    bytes[i] += 256;
                }
            }
            String saveDir = AttachmentManager.uploadDir(ZbConstants.UPLOADPATH_TP_IMG) + "tmp";
            FileUtil.createDir(saveDir);
            imgFilePath = saveDir + File.separator + DateUtil.yyyyMMddHHmmssSSS.format(new Date()) + ".png";
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
        }
        return imgFilePath;
    }
    
    /**
     * <li>说明：读取故障提票上传的照片
     * <li>创建人：程锐
     * <li>创建日期：2015-3-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param attachmentKeyIDX 提票单IDX
     * @return 以Base64编码的图片字符串
     */
    public String downloadFaultImg(String attachmentKeyIDX) {
        List<Attachment> list = attachmentManager.findListByKey(attachmentKeyIDX, ZbConstants.UPLOADPATH_TP_IMG);
        if (list == null || list.size() < 1)
            return "";
        Attachment att = list.get(0);
        String imgPath = AttachmentManager.uploadFilepath(att);
        
        byte[] data = null;
        try {
            InputStream in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            ExceptionUtil.process(e, logger);
        }
        sun.misc.BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
        
    }
    
    /**
     * <li>说明：批量保存故障现象
     * <li>创建人：王治龙
     * <li>创建日期：2013-5-4
     * <li>修改人： 王治龙
     * <li>修改日期：2013-12-26
     * <li>修改内容：封装返回值
     * @param operatorid 操作员ID
     * @param jsonData 故障现象Json集合（需要按照PlaceFault提票实体进行数据组装）
     * @return 操作结果
     * @throws IOException 
     */
    public String savePlaceFaultList(Long operatorid, String jsonData) throws IOException {
        try {
            AcOperator ac = acOperatorManager.getModelById(operatorid);
            SystemContext.setAcOperator(ac);
            PlaceFault[] entityList = JSONUtil.read(jsonData, PlaceFault[].class);
            placeFaultManager.saveOrUpdateList(entityList);
            return "{'flag':'true','message':'操作成功！'}";
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            OperateReturnMessage message = OperateReturnMessage.newFailsInstance(e.getMessage());
            return JSONUtil.write(message);
        }
    }
    
    /**
     * <li>说明：查询提票活分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param repairClass 票活类型
     * @param faultNoticeStatus 待接活/待销活，见ZbglTp常量STATUS_DRAFT/STATUS_OPEN
     * @param operatorid 操作者ID
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 提票活分页列表JSON字符串
     */
    @SuppressWarnings("unchecked")
    public String queryTpList(String searchJson, 
    						  String repairClass, 
    						  String faultNoticeStatus,
                              Long operatorid,
    						  int start, 
    						  int limit) {
        
        try {
            start--;
            terminalCommonService.setAcOperatorById(operatorid);
            Page<ZbglTp> page = zbglTpManager.queryTpList(searchJson, repairClass, faultNoticeStatus, operatorid, start * limit, limit);
            List<ZbglTpBean> list = new ArrayList<ZbglTpBean>();
            for (ZbglTp tp : page.getList()) {
                ZbglTpBean bean = new ZbglTpBean();
                BeanUtils.copyProperties(bean, tp);
                bean.setNoticeTime(tp.getNoticeTime() != null ? DateUtil.yyyy_MM_dd_HH_mm_ss.format(tp.getNoticeTime()) : "");
                list.add(bean);
            }
            return JSONUtil.write(new Page(page.getTotal(), list).extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：领取提票活
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param idxs 提票活idx，多个idx用,分隔
     * @param repairClass 票活类型
     * @return 领取成功与否
     */
    public String receiveTp(Long operatorid, String idxs, String repairClass) {
        try {
            zbglTpManager.receiveTp(operatorid, idxs, repairClass);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：获取故障施修方法分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 故障施修方法分页列表JSON字符串
     */
    @SuppressWarnings("unchecked")
    public String findFaultMethod(int start, int limit) {
        start--;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String entity = "com.yunda.jx.base.jcgy.entity.FaultMethod";
            Map queryParamsMap = JSONUtil.read("{}", Map.class);
            map = baseComboManager.page(entity, null, null, queryParamsMap, start * limit, limit, null);
            List<FaultMethodBean> beanList = new ArrayList<FaultMethodBean>();
            beanList = BeanUtils.copyListToList(FaultMethodBean.class, (List) map.get("root"));
            Page pageList = new Page((Integer) (map.get("totalProperty")), beanList);
            return JSONUtil.write(pageList.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：查询碎修处理结果列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 处理结果列表JSON字符串
     */
    public String queryRepairResultList() {
        return terminalCommonService.queryEosDictEntryList("JCZB_TP_REPAIRRESULT");
    }
    
    /**
     * <li>说明：销活
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param idxs 提票活idx，多个idx用,分隔
     * @param tpData 处理提票信息JSON字符串
     * @return 销活成功与否
     */
    public String handleTp(Long operatorid, String idxs, String tpData) {
        try {
            ZbglTp tp = JSONUtil.read(StringUtil.nvlTrim(tpData, "{}"), ZbglTp.class);
            zbglTpManager.handleTp(operatorid, idxs, tp);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：撤销领活
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param idxs 提票活idx，多个idx用,分隔
     * @param repairClass 票活类型
     * @return 撤销领活成功与否
     */
    public String cancelReceivedTp(Long operatorid, String idxs, String repairClass) {
        try {
            zbglTpManager.cancelReceivedTp(operatorid, idxs, repairClass);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：查询临修处理结果列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 处理结果列表JSON字符串
     */
    public String queryLXRepairResultList() {
        return terminalCommonService.queryEosDictEntryList("JCZB_LXTP_REPAIRRESULT");
    }
    
    /**
     * <li>说明：复制故障提票图片至附件上传路径并保存附件记录
     * <li>创建人：程锐
     * <li>创建日期：2015-1-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param attachmentKeyIDX 附件业务主键
     * @param attachmentKeyName 附件业务主键名称
     * @param operatorid 操作者ID
     * @param imgFilePath 照片上传路径
     */
    private void copyFaultImg(String attachmentKeyIDX, String attachmentKeyName, Long operatorid, String imgFilePath) {
        try {
            if (StringUtil.isNullOrBlank(imgFilePath))
                return;
            String[] imgFilePaths = StringUtil.tokenizer(imgFilePath, ",");
            for (int i = 0; i < imgFilePaths.length; i++) {
                String extName = "";
                int idx = imgFilePaths[i].lastIndexOf(".");
                if (idx != -1)
                    extName = imgFilePaths[i].substring(idx + 1);
                
                Attachment att = new Attachment();
                att.setAttachmentKeyIDX(attachmentKeyIDX);
                att.setAttachmentKeyName(attachmentKeyName);
                att.setAttachmentRealName("故障提票." + extName);// TODO 照片名称是否需要在上传时设定，目前先写死
                
                Date now = new Date();
                String saveName = att.getAttachmentKeyIDX() + "_" + DateUtil.yyyyMMddHHmmssSSS.format(now) + "." + extName; // 通过对象主键+时间戳生成保存文件名称
                att.setAttachmentSaveName(saveName);
                att.setFileType(extName);
                
                // 获取当前登录操作员
                AcOperator ac = acOperatorManager.getModelById(operatorid);
                SystemContext.setAcOperator(ac);
                OmEmployee employee = SystemContext.getOmEmployee();
                String personName = ac.getOperatorname();
                if (employee != null && employee.getEmpname() != null) {
                    personName = employee.getEmpname();
                }
                att.setUploadPerson(ac.getOperatorid());
                att.setUploadPersonName(personName);
                att.setUploadTime(now);
                // 设置业务无关的附加信息
                att = EntityUtil.setNoDelete(att);
                att = EntityUtil.setSysinfo(att, now);
                
                String saveDir = AttachmentManager.uploadDir(att.getAttachmentKeyName()); // 获取文件保存目录
                FileUtil.createDir(saveDir); // 如果文件保存目录不存在，自动创建
                String attSaveName = att.getAttachmentSaveName();
                long fileSize = FileUtil.copyFile(imgFilePaths[i], saveDir, attSaveName);
                if (fileSize != 0) {
                    FileUtil.deleteFile(imgFilePaths[i]);
                }
                
                att.setAttachmentSize(fileSize);
                attachmentManager.add(att);
            }
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
        }
    }
    /**
     * 
     * <li>说明：获取碎修、临修质量检验分页列表
     * <li>创建人：程梅
     * <li>创建日期：2015-8-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param repairClass 票活类型
     * @param operatorid 操作者ID
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 碎修、临修质量检验分页列表
     */ 
    public String queryZbglTpQCList(String searchJson, String repairClass, long operatorid, int start, int limit) {
        start--;
        Map map = new HashMap();
        try {
            OmEmployee emp = omEmployeeSelectManager.getByOperatorid(operatorid);
            Page page = zbglTpManager.getZbglTpQCPageList(emp.getEmpid(), start * limit, limit, repairClass, searchJson);
            if (page.getTotal() > 0 && page.getList().size() > 0)
                map = page.extjsStore();
            return JSONUtil.write(map);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    /**
     * 
     * <li>说明：完成临碎修提票质量检验项（工位终端）
     * <li>创建人：程梅
     * <li>创建日期：2015-8-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param qcResults 提票质检项列表JSON字符串：[{ idx 质检项IDX accPersonId 验收人编码 accPersonName 验收人员名称 accTime 验收时间}]
     * @param operatorid 操作者id
     * @return 操作成功与否
     */
    public String updateFinishQCResult(String qcResults, long operatorid) {
        try {
            ZbglTp[] tpVO = JSONUtil.read(qcResults, ZbglTp[].class);
            SystemContext.setAcOperator(acOperatorManager.getModelById(operatorid));
            zbglTpManager.updateFinishQCResult(tpVO);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：获取提票处理其他作业人员列表
     * <li>创建人：程锐
     * <li>创建日期：2016-3-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @return 提票处理其他作业人员列表
     */
    public String getOtherWorkerByTP(long operatorid) {
        try {
            OmEmployee emp = omEmployeeSelectManager.getByOperatorid(operatorid);
            List<RepairEmpBean> list = zbglTpManager.getOtherWorkerByTP(emp.getEmpid());
            return JSONUtil.write(list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }

    /**
     * <li>说明：根据工号查询人员信息
     * <li>创建人：林欢
     * <li>创建日期：2016-7-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardID 人员工号
     * @return 人员名词字符串{"empname":"王良廷"}
     */
    public String findOmeployeeByCardID(String workCardID) {
        Map<String, Object> map = new HashMap<String, Object>();
        OperateReturnMessage orm = new OperateReturnMessage();
        try {
            if (StringUtils.isNotBlank(workCardID)) {
                
                //判断传入的是否是数字
                try {
                    Integer.valueOf(workCardID);
                } catch (Exception e) {
                    orm.setFaildFlag("传递的工号不是数字，请确认！");
                    return JSONObject.toJSONString(orm);
                }
                
                OmEmployee emp = omEmployeeManager.findByCode(workCardID);
                if(emp != null){
                    map.put("flag", true);
                    map.put("message", emp.getEmpname());
                }else {
                    map.put("flag", false);
                    map.put("message", "未查询到人员名称！");
                }
            }
            return JSONObject.toJSONString(map);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            orm.setFaildFlag(e.getMessage());
            return JSONObject.toJSONString(orm);
        }
    }

    /**
     * <li>说明：同车同位置同故障现象次数
     * <li>创建人：林欢
     * <li>创建日期：2016-7-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 查询条件
     * {
           trainTypeShortName:'HXD1',
           trainNo:'0024',
           faultFixFullCode:'040202',
           faultID:'2070'
       }
     * @return 同位置同故障现象次数{"count":"1"}
     */
    
    public String getSameTrainNONameTPCount(String jsonObject) {
        Map<String, Object> map = new HashMap<String, Object>();
        OperateReturnMessage orm = new OperateReturnMessage();
        try {
            Map<String, Object> mapParam = new HashMap<String, Object>();
            
            JSONObject ob = JSONObject.parseObject(jsonObject);
            
            //获取数据
            String trainTypeShortNameAndTrainNo = ob.getString("trainTypeShortNameAndTrainNo");//车型车号
            String faultFixFullCode = ob.getString("faultFixFullCode");//故障位置
            String faultID = ob.getString("faultID");//故障现象
            
            //封装查询条件
            ZbglTp zbglTp = new ZbglTp();
            
            mapParam.put("trainTypeShortNameAndTrainNo", trainTypeShortNameAndTrainNo);
            mapParam.put("faultFixFullCode", faultFixFullCode);
            mapParam.put("faultID", faultID);
            mapParam.put("faultNoticeStatus", "'" + ZbglTp.STATUS_OVER + "','" + ZbglTp.STATUS_CHECK + "'");
            
            List<ZbglTp> zbglTpList = zbglTpManager.findZbglTpByParam(mapParam);
            
            //取总数
            if (zbglTpList != null) {
                map.put("message", String.valueOf(zbglTpList.size()));
                map.put("flag", true);
            }else {
                map.put("message", "0");
                map.put("flag", true);
            }
            
            return JSONObject.toJSONString(map);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            orm.setFaildFlag(e.getMessage());
            return JSONObject.toJSONString(orm);
        }
    }


    /**
     * <li>说明：整备提票检查不通过【提票返修】
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json字符串，返修实体
     * @return 操作成功与否
     */
    public String saveTpRepair(String jsonObject) {
        try {
            ZbglTpRepair entity = JSONUtil.read(jsonObject, ZbglTpRepair.class);
            zbglTpRepairManager.saveTpRepair(entity);
            return "{'flag':'true','message':'操作成功！'}";
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return "{'flag':'false','message':'操作失败！'}";
        }
    }
    
    /**
     * <li>说明：整备提票返修记录列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jt6IDX 提票单主键
     * @return 整备提票返修记录列表JSON字符串
     */
    public String getTpRepairList(String jt6IDX) {
        try {
            List<ZbglTpRepair> list = zbglTpRepairManager.getRepairByJt6IDX(jt6IDX);
            return JSONUtil.write(list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * 
     * <li>说明：添加遗留活（工位终端）
     * <li>创建人：张迪
     * <li>创建日期：2015-8-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 遗留活
     * @param operatorid 操作者id
     * @return 操作成功与否
     */
    public String saveForLwfx(String jsonObject, long operatorid) {
        try {
            ZbglTpException[] tpExceptionAry = JSONUtil.read(jsonObject, ZbglTpException[].class);
            SystemContext.setAcOperator(acOperatorManager.getModelById(operatorid));
            zbglTpExceptionManager.saveForLwfx(tpExceptionAry);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
}
