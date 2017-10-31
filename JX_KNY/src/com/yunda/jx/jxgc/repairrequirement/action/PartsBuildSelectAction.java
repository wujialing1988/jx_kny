package com.yunda.jx.jxgc.repairrequirement.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.repairrequirement.entity.PartsBuildSelect;
import com.yunda.jx.jxgc.repairrequirement.manager.PartsBuildSelectManager;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配件组成选择控件控制器
 * <li>创建人：程锐
 * <li>创建日期：2013-12-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class PartsBuildSelectAction  extends JXBaseAction<PartsBuildSelect, PartsBuildSelect, PartsBuildSelectManager>{
	/** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：获取配件组成型号选择列表
     * <li>创建人：程锐
     * <li>创建日期：2013-12-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void buildUpTypeList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            PartsBuildSelect entity = (PartsBuildSelect)JSONUtil.read(searchJson, entitySearch.getClass());
            String isVirtual = req.getParameter("isVirtual");
            if(!StringUtil.isNullOrBlank(isVirtual)) entity.setIsVirtual(isVirtual);
            SearchEntity<PartsBuildSelect> searchEntity = new SearchEntity<PartsBuildSelect>(entity, getStart(), getLimit(), getOrders());
            String typeIDX = req.getParameter("typeIDX");
            String type = req.getParameter("type");
            map = this.manager.buildUpTypeList(searchEntity, typeIDX, type).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
}
