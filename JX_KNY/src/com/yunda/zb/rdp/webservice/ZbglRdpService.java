package com.yunda.zb.rdp.webservice;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.webservice.common.WsConstants;
import com.yunda.zb.common.webservice.ITerminalCommonService;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWi;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWidi;
import com.yunda.zb.rdp.zbtaskbill.manager.ZbglRdpWiManager;
import com.yunda.zb.rdp.zbtaskbill.manager.ZbglRdpWidiManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 整备任务活处理webservice实现类
 * <li>创建人：程锐
 * <li>创建日期：2015-2-2
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "zbglRdpService")
public class ZbglRdpService implements IZbglRdpService {
    
    /**
     * 日志对象
     */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * 机车整备任务单业务对象
     */
    @Autowired
    private ZbglRdpWiManager zbglRdpWiManager;
    
    /**
     * 机车整备任务单数据项业务对象
     */
    @Autowired
    private ZbglRdpWidiManager zbglRdpWidiManager;
    
    /**
     * 工位终端共有接口
     */
    @Autowired
    private ITerminalCommonService terminalCommonService;
    
    /**
     * <li>说明：查询整备任务活分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-2-2
     * <li>修改人：林欢
     * <li>修改日期：2015-8-16
     * <li>修改内容：查询传递工位参数，根据工位查询整备任务活
     * @param searchJson 查询条件JSON字符串
     * @param wiStatus 待接活/待销活，见ZbglRdpWi常量STATUS_TODO/STATUS_HANDLING
     * @param operatorid 操作者ID
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @param workStationIDX 工位idx
     * @return 整备任务活分页列表JSON字符串
     */
    public String queryRdpWiList(String searchJson,  
                                 String wiStatus,
                                 Long operatorid,
                                 int start, 
                                 int limit,
                                 String workStationIDX) {
        try {
            start--;
            terminalCommonService.setAcOperatorById(operatorid);
            
//            Page page = zbglRdpWiManager.queryRdpWiList(searchJson, wiStatus, operatorid, start * limit, limit);
            Page page = null;
            //判断工位是否传入，如果没有传入，查询所有整备任务活
            if (!StringUtils.isNotBlank(workStationIDX)) {
//              如果没有传递工位，那么查询条件为所有
                page = zbglRdpWiManager.queryRdpWiListWithOutWorkStation(searchJson, wiStatus, operatorid, start * limit, limit);
            }else {
                page = zbglRdpWiManager.queryRdpWiListWithWorkStation(searchJson, wiStatus, operatorid, start * limit, limit,workStationIDX);
            }
            
            return JSONUtil.write(page.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：领取整备任务活
     * <li>创建人：程锐
     * <li>创建日期：2015-2-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param idxs 整备任务活idx，多个idx用,分隔
     * @return 领取成功与否
     */
    public String receiveRdp(Long operatorid, String idxs) {
        try {
            zbglRdpWiManager.receiveRdp(operatorid, idxs);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：撤销领取整备任务活
     * <li>创建人：程锐
     * <li>创建日期：2015-2-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param idxs 整备任务活idx，多个idx用,分隔
     * @return 领取成功与否
     */
    public String cancelReceivedRdp(Long operatorid, String idxs) {
        try {
            zbglRdpWiManager.cancelReceivedRdp(operatorid, idxs);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：查询机车整备任务单数据项分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-2-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 机车整备任务单数据项分页列表
     */
    public String queryRdpWidiList(String searchJson, int start, int limit) {
        try {
            start--;
            Page page = zbglRdpWidiManager.queryRdpWiList(searchJson, start * limit, limit);
            return JSONUtil.write(page.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：整备任务活-销活
     * <li>创建人：程锐
     * <li>创建日期：2015-2-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param rdpWiIDX 机车整备任务单IDX
     * @param isHg 是否合格
     * @param widiDatas 机车整备任务单数据项实体数组JSON字符串
     * @param worker 联合作业人员
     * @return 销活成功与否
     */
    public String handleRdp(Long operatorid, String rdpWiIDX, String isHg, String widiDatas,String worker) {
        try {
            ZbglRdpWidi[] widiArray = JSONUtil.read(StringUtil.nvlTrim(widiDatas, "[]"), ZbglRdpWidi[].class);
            zbglRdpWiManager.updateForHandleRdp(operatorid, rdpWiIDX, isHg, widiArray,worker);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：整备任务活-暂存
     * <li>创建人：程锐
     * <li>创建日期：2015-2-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpWiIDX 机车整备任务单IDX
     * @param isHg 是否合格
     * @param widiDatas 机车整备任务单数据项实体数组JSON字符串
     * @return 暂存成功与否
     */
    public String updateForRdpWidi(String rdpWiIDX, String widiDatas, String isHg,String worker) {
        try {
            
        	//          保存是否合格
                if (StringUtil.isNullOrBlank(rdpWiIDX))
                    throw new BusinessException("整备任务单IDX为空");
                ZbglRdpWi wi = zbglRdpWiManager.getModelById(rdpWiIDX);
                if (wi == null)
                    throw new BusinessException("整备任务单为空");
                //更新表数据
                if(!StringUtil.isNullOrBlank(isHg)){
                	wi.setIsHg(isHg);
                }
                wi.setWorker(worker);
                zbglRdpWiManager.saveOrUpdate(wi);
            
            ZbglRdpWidi[] widiArray = JSONUtil.read(StringUtil.nvlTrim(widiDatas, "[]"), ZbglRdpWidi[].class);
            
            //保存列表
            zbglRdpWiManager.updateForRdpWidi(rdpWiIDX, widiArray);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
}
