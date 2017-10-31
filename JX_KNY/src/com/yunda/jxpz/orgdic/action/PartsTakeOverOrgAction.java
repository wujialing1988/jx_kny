package com.yunda.jxpz.orgdic.action; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jxpz.orgdic.entity.PartsTakeOverOrg;
import com.yunda.jxpz.orgdic.manager.PartsTakeOverOrgManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsTakeOverOrg控制器, 接收部门
 * <li>创建人：程梅
 * <li>创建日期：2016年6月2日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class PartsTakeOverOrgAction extends JXBaseAction<PartsTakeOverOrg, PartsTakeOverOrg, PartsTakeOverOrgManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：查询接收部门列表【包含配件周转常用部门和库房列表】
     * <li>创建人：程梅
     * <li>创建日期：2016-6-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
     public void getTakeOverOrgList() throws Exception{
            Map<String, Object> map = new HashMap<String,Object>();
            try {
                List<PartsTakeOverOrg> orgList = this.manager.findTakeOverOrgList(getStart(), getLimit());
                map = Page.extjsStore("orgId", orgList);
            } catch (Exception e) {
                ExceptionUtil.process(e, logger, map);
            } finally {
                JSONUtil.write(this.getResponse(), map);
            }   
        }
}