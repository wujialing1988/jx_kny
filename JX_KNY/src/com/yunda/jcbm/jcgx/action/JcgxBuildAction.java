package com.yunda.jcbm.jcgx.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jcbm.jcgx.entity.JcgxBuild;
import com.yunda.jcbm.jcgx.manager.JcgxBuildManager;
import com.yunda.jcbm.jcxtfl.entity.JcxtflBuild;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JcgxBuild控制器，机车构型
 * <li>创建人： 王利成
 * <li>创建日期： 2016-5-16
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class JcgxBuildAction extends JXBaseAction<JcgxBuild, JcgxBuild, JcgxBuildManager> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 机车构型导入文件的File实体 */
    private File jcgx;
    
    /** 机车构型导入文件名称 */
    private String jcgxFileName;
    
    /** 机车简称 */
    private String shortName;
    
    /**
     * <li>说明：根据车型简称查询机车构型树
     * <li>创建人：王利成
     * <li>创建日期：2016-5-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unused")
    public void tree() throws Exception {
        List<HashMap<String, Object>> children = null;
        try {
            String parentIDX = getRequest().getParameter("parentIDX");
            String shortName = getRequest().getParameter("shortName");
            children = manager.tree(parentIDX, shortName);
        } catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
        } finally {
            JSONUtil.write(getResponse(), children);
        }
    }
    
    /**
     * <li>说明： 更新机车构型
     * <li>创建人： 王利成
     * <li>创建日期： 2016-5-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateJcgxBuild() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            JcgxBuild t = (JcgxBuild) JSONUtil.read(getRequest(), entity.getClass());
            String[] errMsg = this.manager.validateUpdate(t);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveOrUpdate(t);
                map.put(Constants.ENTITY, t);
                map.put(Constants.SUCCESS, true);
            } else {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明： 更新机车构型全位置名称，例如：电力交流/控制及通讯/控制及线上设备
     * <li>创建人： 王利成
     * <li>创建日期： 2016-5-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateWzqm() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String shortName = getRequest().getParameter("shortName");
            this.manager.updateWzqm(shortName);
            map.put(Constants.SUCCESS, true);
        } catch (RuntimeException e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明： 级联删除机车构型及其子构型
     * <li>创建人： 王利成
     * <li>创建日期： 2016-5-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void deleteNode() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.manager.deleteByCasecade(ids);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：插入构型树
     * <li>创建人：王利成
     * <li>创建日期：2016-5-20
     * <li>修改人：
     * <li>修改日期：
     * @throws Exception void
     */
    public void insertJcgx() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String type = getRequest().getParameter("type");
            String shortName = getRequest().getParameter("shortName");
            String nodeIDX = getRequest().getParameter("nodeIDX");
            JcxtflBuild[] objList = (JcxtflBuild[]) JSONUtil.read(getRequest(), JcxtflBuild[].class);
            this.manager.insertJcgx(objList, type, shortName, nodeIDX);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    

    /**
     * <li>说明：通过拖拽的方式调整机车构型节点顺序
     * <li>创建人：何涛
     * <li>创建日期：2016-5-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateToMoveNode() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        HttpServletRequest req = getRequest();
        try {
            // 被拖拽的节点（coid主键）
            String node = req.getParameter("node");
            // 旧父节点（coid主键）
            String oldParent = req.getParameter("oldParent");
            // 新父节点（coid主键）
            String newParent = req.getParameter("newParent");
            // 节点（index主键）
            String index = req.getParameter("index");
            this.manager.updateToMoveNode(node, oldParent, newParent,index);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    /**
     * <li>说明：调整节点顺序（上移，下移，置顶）
     * <li>创建人：张迪
     * <li>创建日期：2016-8-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public final void moveOrder() throws JsonMappingException, IOException {
        HttpServletRequest req = getRequest();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // 被排序记录的idx主键
            String coID = req.getParameter("coID");
            // 记录的排序方式
            String orderType =  req.getParameter("orderType");
            String[] errMsg = this.manager.validateMoveOrder(coID, Integer.parseInt(orderType));
            if (errMsg == null || errMsg.length < 1) {
                this.manager.updateMoveOrder(coID, Integer.parseInt(orderType));
                map.put("success", "true");
            }else{
                map.put("success", "false");
                map.put("errMsg", errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
/**
     * <li>说明：导入机车构型
     * <li>创建人：何东
     * <li>创建日期：2016-08-31
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void saveImport() throws JsonMappingException, IOException {
        if (StringUtils.isBlank(shortName)) {
        	return;
        }
    	
    	Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (null == jcgxFileName || !jcgxFileName.endsWith(".xls")) {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, "该功能仅支持 Excel2003（*.xls） 版本文件！");
            } else {
                this.manager.saveImport(this.jcgx, shortName);
                map.put("success", true);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
             ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
             ServletActionContext.getResponse().setContentType("text/html");
             ServletActionContext.getResponse().getWriter().write(JSONUtil.write(map));
        }
    }

	public File getJcgx() {
		return jcgx;
	}

	public void setJcgx(File jcgx) {
		this.jcgx = jcgx;
	}

	public String getJcgxFileName() {
		return jcgxFileName;
	}

	public void setJcgxFileName(String jcgxFileName) {
		this.jcgxFileName = jcgxFileName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
}
