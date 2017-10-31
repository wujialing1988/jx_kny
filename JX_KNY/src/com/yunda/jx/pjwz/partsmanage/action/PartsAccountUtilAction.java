package com.yunda.jx.pjwz.partsmanage.action;

import java.util.HashMap;
import java.util.List;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountUtilManager;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配件信息通用控制器
 * <li>创建人：程锐
 * <li>创建日期：2014-6-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class PartsAccountUtilAction extends JXBaseAction<PartsAccount, PartsAccount, PartsAccountUtilManager>{
	/**  类型：long  */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 * <li>说明：树型列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-6-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
    public void tree() throws Exception {
	    String id = getRequest().getParameter("parentIDX");  //父节点ID
	    String orgid = getRequest().getParameter("orgid");
        List<HashMap> children = manager.getTree(id, orgid);
        JSONUtil.write(getResponse(), children);
    }
}
