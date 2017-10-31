package com.yunda.zb.trainonsand.webservice;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.Page;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;
import com.yunda.zb.common.webservice.ITerminalCommonService;
import com.yunda.zb.trainonsand.entity.ZbglSandingBean;
import com.yunda.zb.trainonsand.manager.ZbglSandingManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 整备上砂任务处理webservice实现类
 * <li>创建人：王利成
 * <li>创建日期：2015-2-6
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "zbglSandingService")
public class ZbglSandingService implements IZbglSandingService {
    
    /**
     * 日志对象
     */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    
    /**
     * 机车整备任务单数据项业务对象
     */
    @Autowired
    private ZbglSandingManager zbglSandingManager;
    
    
    /**
     * 工位终端共有接口
     */
    @Autowired
    private ITerminalCommonService terminalCommonService;
    
    /**
     * <li>说明：查询整备上砂分页列表
     * <li>创建人：王利成
     * <li>创建日期：2015-2-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 整备上砂分页列表JSON字符串
     */
    public String querySandingList(String searchJson, int start, int limit) {
        OperateReturnMessage objReturnMessage = new OperateReturnMessage();
        try {
            start--;
            ZbglSandingBean sandingBean = JSONUtil.read(searchJson, ZbglSandingBean.class);
            terminalCommonService.setAcOperatorById(sandingBean.getOperatorid());
            Page page = zbglSandingManager.querySandingList(sandingBean, start * limit, limit);
            if(page != null){
                // 设置日期的输出格式为 yyyy-MM-dd HH:mm:ss
                ObjectMapper om = new ObjectMapper();
                om.getSerializationConfig().setDateFormat(DateUtil.yyyy_MM_dd_HH_mm_ss);
                om.getDeserializationConfig().setDateFormat(DateUtil.yyyy_MM_dd_HH_mm_ss);
                return om.writeValueAsString(page.extjsStore());
            }else{
                return JSONUtil.write(objReturnMessage.setFaildFlag("查询参数isFinnish不正确,导致查询异常"));
            }
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：开始整备上砂任务
     * <li>创建人：王利成
     * <li>创建日期：2015-2-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainAccessIdx 机车出入库台账主键
     * @param operatorid 操作者ID
     * @return 操作成功与否
     */
    public String startSanding(String trainAccessIdx, Long operatorid) {
        try {
            zbglSandingManager.startSanding(trainAccessIdx,operatorid);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }

    /**
     * <li>说明：结束整备上砂任务
     * <li>创建人：王利成
     * <li>创建日期：2015-2-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param sandingIdx 上砂记录主键
     * @param sandNum 上砂量
     * @return 操作成功与否
     */
    public String endSanding(String sandingIdx, String sandNum) {
        try {
            zbglSandingManager.endSanding(sandingIdx, sandNum);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
        
    }
 }
