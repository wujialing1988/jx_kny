package com.yunda.zb.zbfw.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.zb.zbfw.entity.ZbfwTrainCenter;
import com.yunda.zb.zbfw.entity.ZbfwTrainCenterDTO;
import com.yunda.zb.zbfw.manager.ZbfwTrainCenterManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbfwTrainCenterAction控制器, 整备范围和车型车号中间表绑定
 * <li>创建人：王利成
 * <li>创建日期：2015-01-18
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class ZbfwTrainCenterAction extends JXBaseAction<ZbfwTrainCenter, ZbfwTrainCenter, ZbfwTrainCenterManager> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：：获取绑定车型信息
     * <li>创建人：林欢
     * <li>创建日期：2016-7-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findZbfwTrainInfo() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            ZbfwTrainCenter entity = (ZbfwTrainCenter)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<ZbfwTrainCenter> searchEntity = new SearchEntity<ZbfwTrainCenter>(entity, getStart(), getLimit(), getOrders());
            Page<ZbfwTrainCenterDTO> page = this.manager.findZbfwTrainInfo(searchEntity);
            map = page.extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：保存范围和车型车号中间表关系
     * <li>创建人：林欢
     * <li>创建日期：2016-07-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void saveOrUpdateZbfwTrainCenterInfo() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            // 中间表数据对象
            ZbfwTrainCenter[] zbfwTrainCenterArray = JSONUtil.read(getRequest(), ZbfwTrainCenter[].class);
            
            // 保存数据
            this.manager.saveOrUpdateZbfwTrainCenterInfo(zbfwTrainCenterArray);
            map.put(Constants.SUCCESS, true);
        } catch (Exception ex) {
            ExceptionUtil.process(ex, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * <li>说明：更新范围和车型车号中间表关系
     * <li>创建人：林欢
     * <li>创建日期：2016-08-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void updateZbfwTrainCenterInfo() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            // 范围活idx
            String zbfwIDX = getRequest().getParameter("zbfwIDX");
            // 范围活名称
            String fwName = getRequest().getParameter("fwName");
            //中间表idx
            String zbfwTrianCenterIDX = getRequest().getParameter("zbfwTrianCenterIDX");
            
            // 保存数据
            this.manager.updateZbfwTrainCenterInfo(zbfwIDX,zbfwTrianCenterIDX,fwName);
            map.put(Constants.SUCCESS, true);
        } catch (Exception ex) {
            ExceptionUtil.process(ex, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * <li>说明：根据车型车号查询中间表对象
     * <li>创建人：林欢
     * <li>创建日期：2016-08-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findZbfwTrainCenterByTrainNoAndTrainTypeIDX() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            // 范围活idx
            String trainNo = getRequest().getParameter("trainNo");
            // 范围活名称
            String trainTypeIDX = getRequest().getParameter("trainTypeIDX");
            
            // 保存数据
            ZbfwTrainCenter zbfwTrainCenter = this.manager.findZbfwTrainCenterByTrainNoAndTrainTypeIDX(trainNo,trainTypeIDX);
            if (zbfwTrainCenter != null) {
                map.put("zbfwTrainCenter", zbfwTrainCenter);
            }
            map.put(Constants.SUCCESS, true);
        } catch (Exception ex) {
            ExceptionUtil.process(ex, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
