package com.yunda.jx.jxgc.workhis.workcard.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.workhis.workcard.entity.QCResultHis;
import com.yunda.jx.jxgc.workhis.workcard.manager.QCResultHisManager;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: QCResultHis控制类
 * <li>创建人：程梅
 * <li>创建日期：2015-8-21
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class QCResultHisAction extends JXBaseAction<QCResultHis, QCResultHis, QCResultHisManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());		
    /**
     * 
     * <li>说明：查询当前工单的质量检查项列表
     * <li>创建人：程梅
     * <li>创建日期：2015-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findQCResultList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            QCResultHis qCResultHis = (QCResultHis) JSONUtil.read(searchJson, QCResultHis.class);
            SearchEntity<QCResultHis> searchEntity = new SearchEntity<QCResultHis>(qCResultHis, getStart(), getLimit(), getOrders());
            map = this.manager.findQCResultList(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
