package com.yunda.jcbm.jcpjzd.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jcbm.jcpjzd.entity.JcpjzdBuild;
import com.yunda.jcbm.jcpjzd.manager.JcpjzdBuildManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JcpjzdBuild控制器，机车零部件
 * <li>创建人：程梅
 * <li>创建日期：2016年7月6日
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class JcpjzdBuildAction extends JXBaseAction<JcpjzdBuild, JcpjzdBuild, JcpjzdBuildManager> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 机车零部件导入文件名称 */
    private String jcpjzdFileName;
    
    /** 机车零部件导入文件的File实体 */
    private File jcpjzd;
    /**
     * 
     * <li>说明：方法实现功能说明
     * <li>创建人：程梅
     * <li>创建日期：2016-7-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void tree() throws Exception{
        String fjdId = StringUtil.nvlTrim(getRequest().getParameter("fjdId"), "ROOT_0");
        List<HashMap<String, Object>> children=manager.findJcpjzdTree(fjdId);
        JSONUtil.write(getResponse(), children);
    }
    
    /**
     * 
     * <li>说明：导入机车零部件
     * <li>创建人：曾雪
     * <li>创建日期：2016-7-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void saveImport() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (null == jcpjzdFileName || !jcpjzdFileName.endsWith(".xls")) {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, "该功能仅支持 Excel2003（*.xls） 版本文件！");
            } else {
                this.manager.saveImport(this.jcpjzd);
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
     * <li>说明：删除
     * <li>创建人：曾雪
     * <li>创建日期：2016-7-21
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
    
    
	public File getJcpjzd() {
		return jcpjzd;
	}

	public void setJcpjzd(File jcpjzd) {
		this.jcpjzd = jcpjzd;
	}

	public String getJcpjzdFileName() {
		return jcpjzdFileName;
	}

	public void setJcpjzdFileName(String jcpjzdFileName) {
		this.jcpjzdFileName = jcpjzdFileName;
	}

    
}
