package com.yunda.jx.jxgc.dispatchmanage.webservice;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.dispatchmanage.entity.RepairLine;
import com.yunda.jx.jxgc.dispatchmanage.entity.WorkStation;
import com.yunda.jx.jxgc.dispatchmanage.manager.RepairLineManager;
import com.yunda.jx.jxgc.dispatchmanage.manager.WorkStationManager;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车检修工位接口
 * <li>创建人：程梅
 * <li>创建日期：2015-10-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value = "workStationService")
public class WorkStationService implements IWorkStationService {
    
    /** 日志工具 */
    Logger logger = Logger.getLogger(this.getClass());
    
    /** 工位业务类 */
    @Autowired
    private WorkStationManager workStationManager ;
    
    /** 检修流水线业务类 */
    @Autowired
    private RepairLineManager repairLineManager ;
   /**
     * 
     * <li>说明：查询机车检修工位列表
     * <li>创建人：程梅
     * <li>创建日期：2015-10-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 过滤条件
     * @param start 起始页
     * @param limit 每页条数
     * @return String 列表json
     */
    @SuppressWarnings("unchecked")
    public String getPageList(String searchJson, int start, int limit) {
        try {
            start--;
            WorkStation station = JSONUtil.read(searchJson, WorkStation.class);
            station.setStatus(WorkStation.USE_STATUS);//查询状态为启用的工位
            SearchEntity<WorkStation> searchEntity = new SearchEntity<WorkStation>(station, start * limit, limit, null);
            Page page = workStationManager.findPageList(searchEntity);
            return JSONUtil.write(page.extjsStore());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("WorkStationService.getPageList：异常:" + e + "\n原因" + e.getMessage());
            return "error";
        }
    }
    /**
     * 
     * <li>说明：查询机车检修流水线列表
     * <li>创建人：程梅
     * <li>创建日期：2015-10-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 过滤条件
     * @param start 起始页
     * @param limit 每页条数
     * @return String 列表json
     */
    @SuppressWarnings("unchecked")
    public String getRepairLineList(String searchJson, int start, int limit) {
        try {
            start--;
            RepairLine line = JSONUtil.read(searchJson, RepairLine.class);
            SearchEntity<RepairLine> searchEntity = new SearchEntity<RepairLine>(line, start * limit, limit, null);
            Page page = repairLineManager.findPageList(searchEntity);
            return JSONUtil.write(page.extjsStore());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("WorkStationService.getRepairLineList：异常:" + e + "\n原因" + e.getMessage());
            return "error";
        }
    }
}
