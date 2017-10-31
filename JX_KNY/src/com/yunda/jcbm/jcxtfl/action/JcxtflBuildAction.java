package com.yunda.jcbm.jcxtfl.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jcbm.jcxtfl.entity.JcxtflBuild;
import com.yunda.jcbm.jcxtfl.manager.JcxtflBuildManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JcxtflBuild控制器，机车系统分类
 * <li>创建人：王利成
 * <li>创建日期：2016-5-16
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class JcxtflBuildAction extends JXBaseAction<JcxtflBuild, JcxtflBuild, JcxtflBuildManager> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 机车构型系统分类导入文件的File实体 */
    private File jcxtfl;
    
    /** 机车构型系统分类导入文件名称 */
    private String jcxtflFileName;
    
    /**
     * <li>说明：查询树类型树
     * <li>创建人：王利成
     * <li>创建日期：2016-5-20
     * <li>修改人：
     * <li>修改日期：
     * @throws Exception void
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
     * <li>说明：导入机车构型分类
     * <li>创建人：何涛
     * <li>创建日期：2016-5-23
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void saveImport() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (null == jcxtflFileName || !jcxtflFileName.endsWith(".xls")) {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, "该功能仅支持 Excel2003（*.xls） 版本文件！");
            } else {
                this.manager.saveImport(this.jcxtfl);
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
    
    /**
     * 
     * <li>说明：级联删除机车系统分类及子分类
     * <li>创建人：曾雪
     * <li>创建日期：2016-7-8
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
     * <li>说明：机车构型模板导出
     * <li>创建人：何东
     * <li>创建日期：2016-08-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void exportClassification() throws Exception {
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	String shortName = getRequest().getParameter("shortName");
    	
		try {
			HSSFWorkbook wb = manager.exportClassificationByShortName(shortName);
			HttpServletResponse response = getResponse();
	        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
	        response.setHeader("Content-disposition", "attachment;filename=\""
	        		+ new String((shortName + "机车构型模板.xls").getBytes("GBK"), "ISO8859_1") + "\"");
	        OutputStream ouputStream = response.getOutputStream();
	        wb.write(ouputStream);
	        ouputStream.flush();
	        ouputStream.close();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}
    }
    
    public File getJcxtfl() {
        return jcxtfl;
    }
    
    public void setJcxtfl(File jcxtfl) {
        this.jcxtfl = jcxtfl;
    }
    
    public String getJcxtflFileName() {
        return jcxtflFileName;
    }
    
    public void setJcxtflFileName(String jcxtflFileName) {
        this.jcxtflFileName = jcxtflFileName;
    }
    
}
