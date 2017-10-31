package com.yunda.jx.pjjx.partsrdp.recordinst.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordDI;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordRI;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordRIManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpRecordRI控制器, 配件检修检测项实例
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class PartsRdpRecordRIAction extends JXBaseAction<PartsRdpRecordRI, PartsRdpRecordRI, PartsRdpRecordRIManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：暂存
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws IOException
	 */
	public void saveTemporary() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		try {
			String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
			PartsRdpRecordRI partsRdpRecordRI = JSONUtil.read(entityJson, PartsRdpRecordRI.class);
			String isTemporary = StringUtil.nvl(req.getParameter("isTemporary"), "true");
			PartsRdpRecordDI[] partsRdpRecordDIs = JSONUtil.read(req, PartsRdpRecordDI[].class);
			// 验证【配件检修检测项实例】是否已经被处理，即：状态不为“未处理”
			this.manager.saveTemporary(partsRdpRecordRI, partsRdpRecordDIs, Boolean.parseBoolean(isTemporary));
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			map.put(Constants.SUCCESS, false);
			map.put(Constants.ERRMSG, e.getMessage());
			ExceptionUtil.process(e, logger);
		} finally {
			JSONUtil.write(getResponse(),map);
		}
	}
    
    /**
     * <li>说明：检修检测项分页查询
     * <li>创建人：程锐
     * <li>创建日期：2015-10-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException
     */
    public void pageListForRI() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String entityJson = StringUtil.nvl(getRequest().getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
            PartsRdpRecordRI entity = JSONUtil.read(entityJson, PartsRdpRecordRI.class);
            
            SearchEntity<PartsRdpRecordRI> searchEntity = new SearchEntity<PartsRdpRecordRI>(entity, start, limit, null);            
            Page<PartsRdpRecordRI> page = manager.findPageList(searchEntity);
            map = page.extjsResult();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：检修检测项查询，并将查询结果以HTML形式进行返回，以返回结果在页面进行显示
     * <li>创建人：何涛
     * <li>创建日期：2016-01-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException
     */
    public void queryInHTML() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String rdpRecordCardIDX = this.getRequest().getParameter("rdpRecordCardIDX");
            String html = this.manager.queryInHTML(rdpRecordCardIDX);
            map.put("html", html);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：动态获取检修检测项的页面输入组件
     * <li>创建人：何涛
     * <li>创建日期：2016-02-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException
     */
    public void createInHTML() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String rdpRecordCardIDX = this.getRequest().getParameter("rdpRecordCardIDX");
            map = this.manager.createInHTML(rdpRecordCardIDX);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
}