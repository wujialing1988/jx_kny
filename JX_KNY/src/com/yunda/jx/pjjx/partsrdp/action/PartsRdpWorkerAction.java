package com.yunda.jx.pjjx.partsrdp.action; 

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdpWorker;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpWorkerManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpWorker控制器, 作业人员
 * <li>创建人：程梅
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class PartsRdpWorkerAction extends JXBaseAction<PartsRdpWorker, PartsRdpWorker, PartsRdpWorkerManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * 
	 * <li>说明：批量派工
	 * <li>创建人：程梅
	 * <li>创建日期：2014-12-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @throws Exception
	 */
	public void saveBatch() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
				//施修人员信息
				PartsRdpWorker[] workerList = (PartsRdpWorker[])JSONUtil.read(getRequest(), PartsRdpWorker[].class);
				this.manager.saveBatch(workerList,(Serializable[])ids);
				map.put(Constants.SUCCESS, "true");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
     * 
     * <li>说明：baseComboTree和baseMultyComboTree调用后台方法
     * <li>创建人：王治龙
     * <li>创建日期：2014-1-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void getBaseComboTree() throws Exception{
	    Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
//			 如果设置了queryHql参数，则queryHql查询优先
			String queryHql = req.getParameter("queryHql");
			String entityJson = StringUtil.nvl(req.getParameter("queryParams"), Constants.EMPTY_JSON_OBJECT);
			map = this.manager.findWorkerList(queryHql, entityJson).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
    }
    
    /**
     * <li>说明：获取其他处理人员
     * <li>创建人：程锐
     * <li>创建日期：2015-10-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void findWorkerList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String rdpIDX = StringUtil.nvlTrim( req.getParameter("rdpIDX"), "");            
            List list = this.manager.findWorkerList(rdpIDX);
            if (null != list || list.size() > 0) {
                map = Page.extjsStore("idx", list);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
}