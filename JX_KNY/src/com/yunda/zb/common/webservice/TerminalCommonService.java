package com.yunda.zb.common.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.base.context.SystemContext;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.frame.yhgl.manager.EosDictEntrySelectManager;
import com.yunda.jx.webservice.stationTerminal.base.IStationTerminal;
import com.yunda.jx.webservice.stationTerminal.base.entity.EosDictEntryBean;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountQueryManager;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;
import com.yunda.zb.common.ZbConstants;
import com.yunda.zb.pczz.manager.ZbglPczzManager;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpManager;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWi;
import com.yunda.zb.rdp.zbtaskbill.manager.ZbglRdpWiManager;
import com.yunda.zb.tp.entity.ZbglTp;
import com.yunda.zb.tp.manager.ZbglTpManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 工位终端公用接口实现类
 * <li>创建人：程锐
 * <li>创建日期：2015-3-3
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "terminalCommonService")
public class TerminalCommonService implements ITerminalCommonService {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 机车整备单业务类 */
    @Resource
    private ZbglRdpManager zbglRdpManager;
    
    /** 机车整备任务单业务类 */
    @Resource
    private ZbglRdpWiManager zbglRdpWiManager;
    
    /** 提票业务类 */
    @Resource
    private ZbglTpManager zbglTpManager;
    
    /** 机车出入段台账查询业务类 */
    @Resource
    private TrainAccessAccountQueryManager trainAccessAccountQueryManager;
    
    /** 普查整治单业务类 */
    @Resource
    ZbglPczzManager zbglPczzManager;
    
    /** 系统操作员业务类 */
    @Resource
    AcOperatorManager acOperatorManager;
    
    /**
     * 数据字典业务对象
     */
    @Autowired
    private EosDictEntrySelectManager eosDictEntrySelectManager;
    
    
    /**
     * <li>说明：获取工位终端首页的机车公用信息
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param accountIDX 机车出入段台账IDX
     * @return 工位终端首页的机车公用信息JSON字符串
     * {
            "dynamicInfo" : {
                "jchHandled" : "0",
                "jchTotal" : "1",
                "jchUnHandled" : "1",
                "pczzHandled" : "0",
                "pczzTotal" : "0",
                "pczzUnHandled:" : "0",
                "tpHandled" : "0",
                "tpTotal" : "碎修（0） 临修（0）",
                "tpUnHandled" : "0"
            },
            "trainInfo" : {
                "planOutTime" : "2015-03-20 16:43:43",
                "trainOrder" : "0001",
                "trainTypeAndNo" : "HXD1C0003"
            }
        }
     * @throws Exception
     */
    public String getTerminalData(String accountIDX) throws Exception {
        JSONObject terminalData = new JSONObject();
        try {
            ZbglRdp rdp = zbglRdpManager.getRunningRdpByAccount(accountIDX);
            if (rdp != null) {
                TrainAccessAccount account = trainAccessAccountQueryManager.getModelById(accountIDX);
                
                terminalData.put("trainInfo", buildTrainInfo(account));
                terminalData.put("dynamicInfo", buildDynamicInfo(rdp.getIdx(), account.getTrainTypeIDX(), account.getTrainNo()));
            }            
            return terminalData.toJSONString();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：设置操作员
     * <li>创建人：程锐
     * <li>创建日期：2015-3-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员id
     */
    public void setAcOperatorById(Long operatorid) {
        SystemContext.setAcOperator(acOperatorManager.findLoginAcOprator(operatorid));
    }
    
    /**
     * <li>说明：查询数据字典列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-25
     * <li>修改人： 林欢
     * <li>修改日期：2016-8-4
     * <li>修改内容：重构简单列表代码
     * @param dictTypeID 数据字典项
     * @return 数据字典列表
     */
    public String queryEosDictEntryList(String dictTypeID) {
        try {
//            List<EosDictEntry> list = eosDictEntrySelectManager.findByDicTypeID(dictTypeID);
//            List<EosDictEntryBean> beanlist = new ArrayList<EosDictEntryBean>();
//            for (EosDictEntry eos : list) {
//                EosDictEntryBean bean = new EosDictEntryBean();
//                bean.setDictid(eos.getId().getDictid());
//                bean.setDictname(eos.getDictname());
//                beanlist.add(bean);
//            }
            List<EosDictEntryBean> beanlist = new ArrayList<EosDictEntryBean>();
            beanlist = this.findEosDictEntryBeanByCopy(dictTypeID);
            return JSONUtil.write(beanlist);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    
    /**
     * <li>说明：查询数据字典列表
     * <li>创建人：林欢
     * <li>创建日期：2016-8-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param dictTypeID 数据字典项
     * @return 数据字典列表
     */
    @Override
    public List<EosDictEntryBean> findEosDictEntryBeanByCopy(String dictTypeID) {
        List<EosDictEntry> list = eosDictEntrySelectManager.findByDicTypeID(dictTypeID);
        List<EosDictEntryBean> beanlist = new ArrayList<EosDictEntryBean>();
        for (EosDictEntry eos : list) {
            EosDictEntryBean bean = new EosDictEntryBean();
            bean.setDictid(eos.getId().getDictid());
            bean.setDictname(eos.getDictname());
            beanlist.add(bean);
        }
        
        return beanlist;
    }

    /**
     * <li>说明：获取整备任务和提票活数量
     * <li>创建人：程锐
     * <li>创建日期：2015-4-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param operatorid 操作者ID
     * @return 整备任务和提票活数量
     */
    public String getCount(String searchJson, Long operatorid) {
        JSONObject terminalData = new JSONObject();
        try {
            setAcOperatorById(operatorid);
            int todoRdpCount = zbglRdpWiManager.getRdpWiCount(searchJson, ZbglRdpWi.STATUS_TODO, operatorid);
            int handlingRdpCount = zbglRdpWiManager.getRdpWiCount(searchJson, ZbglRdpWi.STATUS_HANDLING, operatorid);
            int todoSXTpCount = zbglTpManager.getTpCount(searchJson, ZbConstants.REPAIRCLASS_SX, ZbglTp.STATUS_DRAFT, operatorid);
            int handlingSXTpCount = zbglTpManager.getTpCount(searchJson, ZbConstants.REPAIRCLASS_SX, ZbglTp.STATUS_OPEN, operatorid);
            int todoLXTpCount = zbglTpManager.getTpCount(searchJson, ZbConstants.REPAIRCLASS_LX, ZbglTp.STATUS_DRAFT, operatorid);
            int handlingLXTpCount = zbglTpManager.getTpCount(searchJson, ZbConstants.REPAIRCLASS_LX, ZbglTp.STATUS_OPEN, operatorid);
            int zbTpQcLXCount = zbglTpManager.getTpQcCount(searchJson, ZbConstants.REPAIRCLASS_LX, operatorid);
            int zbTpQcSXCount = zbglTpManager.getTpQcCount(searchJson, ZbConstants.REPAIRCLASS_SX, operatorid);
            //8.25,添加需普查整治任务数量
            int needPCZZCount = zbglPczzManager.getPCZZCount(searchJson);
            terminalData.put("todoRdpCount", todoRdpCount);
            terminalData.put("handlingRdpCount", handlingRdpCount);
            terminalData.put("todoSXTpCount", todoSXTpCount);
            terminalData.put("handlingSXTpCount", handlingSXTpCount);
            terminalData.put("todoLXTpCount", todoLXTpCount);
            terminalData.put("handlingLXTpCount", handlingLXTpCount);
            //8.25,将参数封装，传递-
            terminalData.put("needPCZZCount", needPCZZCount);
            terminalData.put("zbTpQcLXCount", zbTpQcLXCount);
            terminalData.put("zbTpQcSXCount", zbTpQcSXCount);
            return terminalData.toJSONString();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：构建机车基本信息业务对象
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param account 机车出入段台账对象
     * @return 机车基本信息业务对象
     */
    private JSONObject buildTrainInfo(TrainAccessAccount account) {
        JSONObject trainInfo = new JSONObject();
        if (!StringUtil.isNullOrBlank(account.getTrainTypeShortName()) && !StringUtil.isNullOrBlank(account.getTrainNo()))
            trainInfo.put("trainTypeAndNo", account.getTrainTypeShortName() + account.getTrainNo());
        else if (!StringUtil.isNullOrBlank(account.getTrainAliasName()))
            trainInfo.put("trainTypeAndNo", account.getTrainAliasName());
        trainInfo.put("trainOrder", account.getArriveOrder());
        trainInfo.put("planOutTime", account.getPlanOutTime() != null ? DateUtil.yyyy_MM_dd_HH_mm_ss.format(account.getPlanOutTime()) : "");
        return trainInfo;
    }
    
    /**
     * <li>说明：构建动态信息列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 机车整备单IDX
     * @param trainTypeIDX 车型IDX
     * @param trainNo 车号
     * @return 动态信息列表
     */
    private JSONObject buildDynamicInfo(String rdpIDX, String trainTypeIDX, String trainNo) {
        JSONObject dynamicInfo = new JSONObject();                
        int sxCount = zbglTpManager.getAllTpCountByRdp(rdpIDX, ZbConstants.REPAIRCLASS_SX);
        int lxCount = zbglTpManager.getAllTpCountByRdp(rdpIDX, ZbConstants.REPAIRCLASS_LX);
        int allCount = zbglTpManager.getAllTpCountByRdp(rdpIDX, "");
        StringBuilder itemValue = new StringBuilder();
        itemValue.append("碎修（").append(sxCount).append("） ").append("临修（").append(lxCount).append("）");
        int handledCount = zbglTpManager.getHandledTpCountByRdp(rdpIDX);
        int unHandledCount = allCount - handledCount;
        dynamicInfo.put("tpTotal", itemValue.toString());
        dynamicInfo.put("tpUnHandled", String.valueOf(unHandledCount));
        dynamicInfo.put("tpHandled", String.valueOf(handledCount));
        
        
        handledCount = zbglPczzManager.getPczzCompleteCount(trainTypeIDX, trainNo);
        unHandledCount = zbglPczzManager.getPczzOngingCount(trainTypeIDX, trainNo);
        allCount = handledCount + unHandledCount;
        
        dynamicInfo.put("pczzTotal", allCount );
        dynamicInfo.put("pczzUnHandled", unHandledCount);
        dynamicInfo.put("pczzHandled", handledCount);
        
        allCount = zbglRdpWiManager.getAllRdpWiCountByRdp(rdpIDX);
        handledCount = zbglRdpWiManager.getHandledRdpWiCountByRdp(rdpIDX);
        unHandledCount = allCount - handledCount;
        
        dynamicInfo.put("jchTotal", allCount);
        dynamicInfo.put("jchUnHandled", unHandledCount);
        dynamicInfo.put("jchHandled", handledCount);
        return dynamicInfo;
    }
    
    /**
     * <li>方法说明：获取工位终端链接配置文件信息
     * <li>方法名称：getstationTerminalInfo
     * <li>创建人：林欢
     * <li>创建时间：2016-5-20 下午02:08:19
     * <li>修改人：
     * <li>修改内容：
     * @return json字符串
     * 格式:{stationTerminalIp : "www.baidu.com",stationTerminalFunctionName:"linhuanTest"}
     */
    @Override
     public synchronized String getstationTerminalInfo() {
        //封装错误信息
        OperateReturnMessage message = new OperateReturnMessage();
        
        //存放配置文件取出的数据
        try {
            
            //通过当前线程读取配置文件
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(IStationTerminal.WEB_SERVICE_PROPERTIES);
            Properties p = new Properties();
            p.load(is);
            
            String stationTerminalFunctionName = (String) p.get(IStationTerminal.STATION_TERMINAL_FUNCTION_NAME);
            String value = new String(stationTerminalFunctionName.getBytes("ISO-8859-1"),"utf-8");
            p.setProperty(IStationTerminal.STATION_TERMINAL_FUNCTION_NAME,value );
            return JSONUtil.write(p);
        } catch (IOException e) {
            ExceptionUtil.process(e, logger);
            message.setFaildFlag(e.getMessage());
            //返回错误信息
            return JSONObject.toJSONString(message);
        } 
    }
}
