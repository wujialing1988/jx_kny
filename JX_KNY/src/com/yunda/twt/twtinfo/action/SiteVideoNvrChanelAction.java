package com.yunda.twt.twtinfo.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.twt.twtinfo.entity.SiteVideoNvrChanel;
import com.yunda.twt.twtinfo.manager.SiteVideoNvrChanelManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: SiteVideoNvrChanel控制器，网络录像机通道
 * <li>创建人：何涛
 * <li>创建日期：2015-6-1
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public class SiteVideoNvrChanelAction extends JXBaseAction<SiteVideoNvrChanel, SiteVideoNvrChanel, SiteVideoNvrChanelManager> {
    
    /** 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass());
    
    /** 导入模板的File实体 */
    private File impStencil; // 上传的文件
    
    /** 导入模板文件名称 */
    private String impStencilFileName; // 文件名称
    
    /** 导入模板文件MIME类型 */
    private String impStencilContentType;
    
    public File getImpStencil() {
        return impStencil;
    }
    
    public void setImpStencil(File impStencil) {
        this.impStencil = impStencil;
    }
    
    public String getImpStencilContentType() {
        return impStencilContentType;
    }
    
    public void setImpStencilContentType(String impStencilContentType) {
        this.impStencilContentType = impStencilContentType;
    }
    
    public String getImpStencilFileName() {
        return impStencilFileName;
    }
    
    public void setImpStencilFileName(String impStencilFileName) {
        this.impStencilFileName = impStencilFileName;
    }
    
    /**
     * <li>说明：初始化通道
     * <li>创建人：何涛
     * <li>创建日期：2015-6-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void init() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String initNum = getRequest().getParameter("initNum");
            String videoNvrIDX = getRequest().getParameter("videoNvrIDX");
            this.manager.init(initNum, videoNvrIDX);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：获取网络录像机通道实例对象
     * <li>创建人：何涛
     * <li>创建日期：2015-6-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getModel() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            SiteVideoNvrChanel queryEntity = JSONUtil.read(getRequest(), entity.getClass());
            SiteVideoNvrChanel model = this.manager.getModel(queryEntity.getSiteID(), queryEntity.getVideoCode());
            if (null != model) {
                map.put(Constants.ENTITY, model);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：批量解除网络录像机通道与台位图上视频监控点的绑定
     * <li>创建人：何涛
     * <li>创建日期：2015-6-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void unBind() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.manager.unBind(ids);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：下载视频通道导入模板
     * <li>创建人：何涛
     * <li>创建日期：2015-6-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     */
    public void downloadImpStencil() {
        HttpServletResponse rsp = getResponse();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("/com/yunda/twt/twtinfo/视频监控配置信息-导入模板.xls");
        try {
            // 设置html头文件类型
            rsp.setContentType("application/x-msdownload");
            rsp.setHeader("Content-Disposition", "attachment;filename=" + StringUtil.toISO("视频监控配置信息-导入模板.xls"));
            OutputStream out = rsp.getOutputStream();
            // 输出文件
            byte[] buf = new byte[1024 * 5];
            int len = is.read(buf);
            while (len > 0) {
                out.write(buf, 0, len);
                len = is.read(buf);
            }
        } catch (Exception e) {
            try {
                rsp.getOutputStream().write("文件不存在或已经被删除！".getBytes());
            } catch (IOException ex) {
                ExceptionUtil.process(ex, logger);
            }
            ExceptionUtil.process(e, logger);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    ExceptionUtil.process(e, logger);
                }
                is = null;
            }
        }
    }
    
    /**
     * <li>说明：导入
     * <li>创建人：何涛
     * <li>创建日期：2015-6-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void saveUpload() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.manager.saveUpload(this.impStencil);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            map.put(Constants.ERRMSG, e.getMessage());
        } finally {
            ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
            ServletActionContext.getResponse().setContentType("text/html");
            ServletActionContext.getResponse().getWriter().write(JSONUtil.write(map));
        }
    }
    
}
