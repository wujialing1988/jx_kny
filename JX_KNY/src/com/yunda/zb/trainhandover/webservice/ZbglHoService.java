package com.yunda.zb.trainhandover.webservice;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.frame.yhgl.manager.EmployeeManager;
import com.yunda.webservice.common.WsConstants;
import com.yunda.zb.common.webservice.ITerminalCommonService;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpManager;
import com.yunda.zb.trainhandover.entity.ZbglHoCase;
import com.yunda.zb.trainhandover.entity.ZbglHoCaseItem;
import com.yunda.zb.trainhandover.entity.ZbglHoModelItem;
import com.yunda.zb.trainhandover.entity.ZbglHoModelItemResult;
import com.yunda.zb.trainhandover.manager.ZbglHoCaseManager;
import com.yunda.zb.trainhandover.manager.ZbglHoModelItemManager;
import com.yunda.zb.trainhandover.manager.ZbglHoModelItemResultManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车交接webservice实现类
 * <li>创建人：程梅
 * <li>创建日期：2015-2-11
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "zbglHoService")
public class ZbglHoService implements IZbglHoService {
    
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
     * 机车交接项模板业务对象
     */
    @Autowired
    private ZbglHoModelItemManager zbglHoModelItemManager;
    
    /**
     * 交接项情况业务对象
     */
    @Autowired
    private ZbglHoModelItemResultManager zbglHoModelItemResultManager;
    
    /**
     * 操作员业务类
     */
    @Autowired
    private AcOperatorManager acOperatorManager;
    
    /**
     * 机车交接记录业务类
     */
    @Autowired
    private ZbglHoCaseManager zbglHoCaseManager;
    
    /**
     * 人员信息业务类
     */
    @Autowired
    private EmployeeManager employeeManager;
    
    /**
     * 工位终端共有接口
     */
    @Autowired
    private ITerminalCommonService terminalCommonService;
    
    /**
     * <li>说明：查询机车交接任务列表
     * <li>创建人：程梅
     * <li>创建日期：2015-2-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param operatorid 操作者ID
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 机车整备任务单数据项分页列表
     */
    public String getTrainHandOverTasks(String searchJson, Long operatorid, int start, int limit) {
        try {
            start--;
            terminalCommonService.setAcOperatorById(operatorid);
            Page page = zbglRdpManager.queryRdpListForHO(searchJson, start, limit ,null,null);
            return JSONUtil.write(page.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：查询交接项列表
     * <li>创建人：程梅
     * <li>创建日期：2015-2-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 交接项列表
     */
    public String getHoModelItem() {
        try {
            Page<ZbglHoModelItem> page = zbglHoModelItemManager.getHoModelItemList();
            return JSONUtil.write(page.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：获取与机车交接项关联的机车交接情况模板
     * <li>创建人：程梅
     * <li>创建日期：2015-2-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param handOverItemIDX 交接项id
     * @return 与机车交接项关联的机车交接情况模板
     */
    public String getHoModelItemResultByItemID(String handOverItemIDX) {
        try {
            List<ZbglHoModelItemResult> resultlist = zbglHoModelItemResultManager.getListByModel(handOverItemIDX);
            return JSONUtil.write(resultlist);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：完成交接工作，保存数据
     * <li>创建人：程梅
     * <li>创建日期：2015-2-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpJson 交接单信息
     * @param itemJson 交接项信息
     * @return 操作提示信息
     */
    public String finishHoTask(String rdpJson, String itemJson) {
        try {
            ZbglHoCase hoCase = (ZbglHoCase) JSONUtil.read(rdpJson, ZbglHoCase.class);
            ZbglHoCaseItem[] items = (ZbglHoCaseItem[]) JSONUtil.read(itemJson, ZbglHoCaseItem[].class);
            AcOperator ac = acOperatorManager.getModelById(hoCase.getOperatorid());
            SystemContext.setAcOperator(ac);
            if (null != hoCase.getFromPersonName()) {
                // String str = rdp.getFromPersonName();
                if (!zbglHoCaseManager.isNumeric(hoCase.getFromPersonName())) {
                    zbglHoCaseManager.saveHoCaseAndItems(hoCase, items);
                    return WsConstants.OPERATE_SUCCESS;
                } else {
                    String hql = "from OmEmployee as o where o.userid = '" + hoCase.getFromPersonId() + "'";
                    OmEmployee ome = employeeManager.findSingle(hql);
                    if (ome != null) {
                        hoCase.setFromPersonName(ome.getEmpname());
                        zbglHoCaseManager.saveHoCaseAndItems(hoCase, items);
                        return WsConstants.OPERATE_SUCCESS;
                    } else {
                        return "该工号不存在对应工作人员！";
                    }
                }
            } else
                zbglHoCaseManager.saveHoCaseAndItems(hoCase, items);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
}
