package com.yunda.jx.pjjx.base.qcitemdefine.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.order.AbstractOrderAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.base.qcitemdefine.entity.QCItem;
import com.yunda.jx.pjjx.base.qcitemdefine.manager.QCItemManager;
/**
 * <li>标题: 机车配件检修管理信息系统
 * <li>说明: QCItem控制器
 * <li>创建人： 何涛
 * <li>创建日期： 2014-11-12 下午01:37:51
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class QCItemAction extends AbstractOrderAction<QCItem, QCItem, QCItemManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：获取所有已维护的”质量检查项“（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-10-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getQCContent() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String isAssign = StringUtil.nvl(getRequest().getParameter("isAssign"), QCItem.CONST_INT_IS_ASSIGN_Y + "");
            List<QCItem> list = QCItemManager.getQCContent(Integer.valueOf(isAssign));
            map.put("list", list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
	
}