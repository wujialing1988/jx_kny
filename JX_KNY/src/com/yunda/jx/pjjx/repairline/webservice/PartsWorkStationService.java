package com.yunda.jx.pjjx.repairline.webservice;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjjx.repairline.entity.PartsRepairLine;
import com.yunda.jx.pjjx.repairline.entity.PartsWorkStation;
import com.yunda.jx.pjjx.repairline.manager.PartsRepairLineManager;
import com.yunda.jx.pjjx.repairline.manager.PartsWorkStationManager;
import com.yunda.webservice.common.entity.OperateReturnMessage;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配件检修工位接口
 * <li>创建人：程梅
 * <li>创建日期：2015-10-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value = "partsWorkStationService")
public class PartsWorkStationService implements IPartsWorkStationService {
    
    /** 日志工具 */
    Logger logger = Logger.getLogger(this.getClass());
    
    /** 工位业务类 */
    @Autowired
    private PartsWorkStationManager partsWorkStationManager ;
    
    /** 配件检修流水线业务类 */
    @Autowired
    private PartsRepairLineManager partsRepairLineManager ;
   /**
     * 
     * <li>说明：查询配件检修工位列表
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
            PartsWorkStation station = JSONUtil.read(searchJson, PartsWorkStation.class);
            SearchEntity<PartsWorkStation> searchEntity = new SearchEntity<PartsWorkStation>(station, start * limit, limit, null);
            Page page = partsWorkStationManager.findPageList(searchEntity);
            return JSONUtil.write(page.extjsStore());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("PartsWorkStationService.getPageList：异常:" + e + "\n原因" + e.getMessage());
            return "error";
        }
    }
    /**
     * 
     * <li>说明：查询配件检修流水线列表
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
    public String getPartsRepairLineList(String searchJson, int start, int limit) {
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            start--;
            PartsRepairLine line = JSONUtil.read(searchJson, PartsRepairLine.class);
            SearchEntity<PartsRepairLine> searchEntity = new SearchEntity<PartsRepairLine>(line, start * limit, limit, null);
            Page page = partsRepairLineManager.findPageList(searchEntity);
            return JSONUtil.write(page.extjsStore());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("PartsWorkStationService.getPartsRepairLineList：异常:" + e + "\n原因" + e.getMessage());
            msg.setFaildFlag(e.getMessage());
        }
        return JSONObject.toJSONString(msg);
    }
}
