package com.yunda.jx.base.jcgy.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.base.jcgy.entity.EquipFault;
import com.yunda.jx.base.jcgy.manager.EquipFaultManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：EquipFault控制器, 故障现象编码
 * <li>创建人：王治龙
 * <li>创建日期：2012-11-11
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class EquipFaultAction extends JXBaseAction<EquipFault, EquipFault, EquipFaultManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>方法名称：searchByFaultName
     * <li>方法说明：根据故障现象名称查询故障现象，故障现象名称为空则查询所有
     * @throws JsonMappingException
     * @throws IOException return: void
     *             <li>创建人：张凡
     *             <li>创建时间：2012-11-30 上午10:55:09
     *             <li>修改人：
     *             <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    public void searchByFaultName() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            Map queryParamsMap = new HashMap();
            // query参数是获取EXTJS的combox控件捕获的键盘输入文字
            String queryValue = StringUtil.nvlTrim(req.getParameter("query"), "");
            map = this.manager.page(queryValue, queryParamsMap, getStart(), getLimit());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：获取故障现象列表
     * <li>创建人：程锐
     * <li>创建日期：2013-1-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param
     * @throws Exception
     */
    public void faultList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            EquipFault entity = (EquipFault) JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<EquipFault> searchEntity = new SearchEntity<EquipFault>(entity, getStart(), getLimit(), getOrders());
            // 分类编码
            String fixPlaceIdx = req.getParameter("fixPlaceIdx");
            map = this.manager.faultList(searchEntity, fixPlaceIdx).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：查询故障现象列表
     * <li>创建人：程锐
     * <li>创建日期：2014-10-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void queryFaultList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String faultName = StringUtil.nvlTrim(getRequest().getParameter("faultName"), "");
            map = this.manager.queryFaultList(faultName, getStart(), getLimit()).extjsResult();
            map.put("id", "FaultID");
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
}
