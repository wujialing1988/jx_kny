package com.yunda.zb.trainclean.webservice;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.frame.yhgl.manager.IEosDictEntryManager;
import com.yunda.webservice.common.WsConstants;
import com.yunda.zb.common.webservice.ITerminalCommonService;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpManager;
import com.yunda.zb.trainclean.entity.ZbglCleaning;
import com.yunda.zb.trainclean.manager.ZbglCleaningManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车保洁webservice实现类
 * <li>创建人：程梅
 * <li>创建日期：2015-3-1
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "zbglCleaningService")
public class ZbglCleaningService implements IZbglCleaningService {
    
    /**
     * 日志对象
     */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * 机车整备单业务对象
     */
    @Autowired
    private ZbglRdpManager zbglRdpManager;
    
    /**
     * 数据字典接口查询功能
     */
    @Autowired
    private IEosDictEntryManager iEosDictEntryManager;
    
    /**
     * 操作员业务类
     */
    @Autowired
    private AcOperatorManager acOperatorManager;
    
    /**
     * 机车保洁记录业务类
     */
    @Autowired
    private ZbglCleaningManager zbglCleaningManager;
    
    /**
     * 工位终端共有接口
     */
    @Autowired
    private ITerminalCommonService terminalCommonService;
    
    /**
     * <li>说明：查询机车保洁任务列表
     * <li>创建人：程梅
     * <li>创建日期：2015-3-1
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param operatorid 操作者ID
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 机车整备任务单数据项分页列表
     */
    public String getTrainCleaningTasks(String searchJson, Long operatorid, int start, int limit) {
        try {
            start--;
            terminalCommonService.setAcOperatorById(operatorid);
            Page page = zbglRdpManager.queryRdpListForClean(searchJson, start, limit,null,null);
            return JSONUtil.write(page.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：获取机车等级数据字典
     * <li>创建人：程梅
     * <li>创建日期：2015-3-1
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 机车等级数据字典
     */
    public String getTrainLevel() {
        try {
            String dicttypeid = "JCZB_JC_LEVEL";
            List<EosDictEntry> eosdyList = iEosDictEntryManager.findCacheEntry(dicttypeid);
            return JSONUtil.write(eosdyList);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：获取保洁等级数据字典
     * <li>创建人：程梅
     * <li>创建日期：2015-3-1
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 保洁等级数据字典
     */
    public String getCleaningLevel() {
        try {
            String dicttypeid = "JCZB_CLEAN_LEVEL";
            List<EosDictEntry> eosdyList = iEosDictEntryManager.findCacheEntry(dicttypeid);
            return JSONUtil.write(eosdyList);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：完成机车保洁工作
     * <li>创建人：王利成
     * <li>创建日期：2014-4-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param json 机车保洁处理字符串
     * @return 操作提示信息
     */
    public String finishTrainCleaningTask(String json) {
        try {
            ZbglCleaning clean = (ZbglCleaning) JSONUtil.read(json, ZbglCleaning.class);
            AcOperator ac = acOperatorManager.getModelById(clean.getOperatorid());
            SystemContext.setAcOperator(ac);
            zbglCleaningManager.savaTrainCleaning(clean);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
}
