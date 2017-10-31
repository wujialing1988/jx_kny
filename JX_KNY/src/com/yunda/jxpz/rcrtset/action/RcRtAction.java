package com.yunda.jxpz.rcrtset.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jxpz.rcrtset.entity.RcRt;
import com.yunda.jxpz.rcrtset.manager.RcRtManager;

@SuppressWarnings(value="serial")
public class RcRtAction  extends JXBaseAction<RcRt, RcRt, RcRtManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * 
	 * <li>说明：批量保存
	 * <li>创建人：程锐
	 * <li>创建日期：2014-2-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	public void saveOrUpdateList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
        	RcRt[] objList = (RcRt[])JSONUtil.read(getRequest(), RcRt[].class);
            String[] errMsg = this.manager.saveOrUpdateList(objList);
            if (errMsg == null || errMsg.length < 1) {
                map.put("success", "true");
            }else{
                map.put("errMsg", errMsg);
            }
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
	/**
	 * 
	 * <li>说明：修程修次列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-2-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
    public void rCRTList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            RcRt entity = JSONUtil.read(searchJson, RcRt.class);
            String repairtimeName = StringUtil.nvlTrim(req.getParameter("repairtimeName"), "");
            // query参数是获取EXTJS的combox控件捕获的键盘输入文字
            String query = StringUtil.nvlTrim(req.getParameter("query"), "");
            String queryHql = req.getParameter("queryHql");
            SearchEntity<RcRt> searchEntity = new SearchEntity<RcRt>(entity, getStart(), getLimit(), null);
            map = this.manager.findPageList(searchEntity, repairtimeName, queryHql, query).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
