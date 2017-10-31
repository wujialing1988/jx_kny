package com.yunda.jx.pjjx.partsrdp.qcinst.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpManager;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsQRBean;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQR;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQR.PartsRdpSearcher;
import com.yunda.jx.pjjx.partsrdp.qcinst.manager.PartsRdpQRQueryManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 配件检修质检控制器
 * <li>创建人：程锐
 * <li>创建日期：2015-10-12
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.3
 */
@SuppressWarnings("serial")
public class PartsRdpQRQueryAction extends JXBaseAction<PartsRdpQR, PartsRdpQR, PartsRdpQRQueryManager>{
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：配件检修质量检验-待检验工单分页查询
     * <li>创建人：程锐
     * <li>创建日期：2015-10-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void findPageListForQC() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // 记录卡实例主键
            String checkWay = getRequest().getParameter("checkWay");
            String entityJson = StringUtil.nvl(getRequest().getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
            PartsQRBean entity = JSONUtil.read(entityJson, PartsQRBean.class);
            
            SearchEntity<PartsQRBean> searchEntity = new SearchEntity<PartsQRBean>(entity, start, limit, null);
            OmEmployee omEmployee = SystemContext.getOmEmployee();
            Page<PartsQRBean> page = new Page<PartsQRBean>();
            if (!StringUtil.isNullOrBlank(searchEntity.getEntity().getRdpIDX()))
            	page = manager.getQCPageList(omEmployee.getEmpid(), start, limit, checkWay, searchEntity);
            map = page.extjsResult();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：配件检修质量检验-根据配件编号或识别码查询配件计划信息
     * <li>创建人：程锐
     * <li>创建日期：2015-11-29
     * <li>修改人：何涛
     * <li>修改日期：2016-03-10
     * <li>修改内容：重构，删除了PartsRdpQRQueryManager.findPartRdpByQC方法，因为该方法等同于PartsRdpManager.findPartRdpQCItems方法；
     * <li>另外：此接口返回需求是单个检修计划实体，当调用的目标方法却是分页查询，可能存在逻辑上的不严谨
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void findPartRdpByQC() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String checkWay = StringUtil.nvl(getRequest().getParameter("checkWay"), "");
            String qcContet = StringUtil.nvl(getRequest().getParameter("qcContet"), "");
            PartsRdp entity = JSONUtil.read(getRequest().getParameter(Constants.ENTITY_JSON), PartsRdp.class);
			SearchEntity<PartsRdp> searchEntity = new SearchEntity<PartsRdp>(entity, start, limit, null);
            PartsRdpManager partsRdpManager = (PartsRdpManager) Application.getSpringApplicationContext().getBean("partsRdpManager");
            Page<PartsRdpSearcher> page = partsRdpManager.findPartRdpQCItems(searchEntity, checkWay, qcContet);
            List<PartsRdpSearcher> list = page.getList();
            if (list.isEmpty()) {
                map.put("success", false);
                map.put("errMsg", "未查询到相应结果");
                JSONUtil.write(getResponse(), map);
            } else {
                JSONUtil.write(getResponse(), list.get(0));
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            JSONUtil.write(getResponse(), map);
        }
    }
}
