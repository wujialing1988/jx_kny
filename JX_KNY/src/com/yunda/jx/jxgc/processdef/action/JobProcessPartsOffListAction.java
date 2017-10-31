package com.yunda.jx.jxgc.processdef.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.processdef.entity.JobProcessPartsOffList;
import com.yunda.jx.jxgc.processdef.manager.JobProcessPartsOffListManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车作业流程维护-下车配件清单配置
 * <li>创建人：张迪
 * <li>创建日期：2016-7-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class JobProcessPartsOffListAction extends JXBaseAction<JobProcessPartsOffList, JobProcessPartsOffList, JobProcessPartsOffListManager>{
        /** 日志工具 */
        private Logger logger = Logger.getLogger(getClass().getName());
        /** 机车构型导入文件的File实体 */
        private File parts;
        
        /** 机车构型导入文件名称 */
        private String fileName;
        
        /** 流程idx */
        private String processIDX;
        
        /**
         * <li>说明：下车配件清单-保存部件信息
         * <li>创建人：张迪
         * <li>创建日期：2016-7-13
         * <li>修改人： 
         * <li>修改日期：
         * <li>修改内容：
         * @throws Exception
         */
        public void savePartsOffList() throws Exception{
            Map<String, Object> map = new HashMap<String,Object>();
            try {
                String processIdx = getRequest().getParameter("processIdx");//流程id
                JobProcessPartsOffList[] buildList = (JobProcessPartsOffList[])JSONUtil.read(getRequest(), JobProcessPartsOffList[].class);           
                this.manager.savePartsOffList(processIdx, buildList);
                map.put("success", "true");
            } catch (Exception e) {
                ExceptionUtil.process(e, logger, map);
            } finally {
                JSONUtil.write(this.getResponse(), map);
            }       
        }
        
        /**
         * <li>说明：下车配件清单-批量设置节点
         * <li>创建人：张迪
         * <li>创建日期：2016-7-13
         * <li>修改人： 
         * <li>修改日期：
         * <li>修改内容：
         * @throws Exception
         */
        public void saveNodeList() throws Exception{
            Map<String, Object> map = new HashMap<String,Object>();
            try {
                String nodeIdx = getRequest().getParameter("nodeIdx");//流程id
                String nodeName = getRequest().getParameter("nodeName");//流程id
                String flag = getRequest().getParameter("flag");//判断是设置上车还是下车节点
                JobProcessPartsOffList[] jobProcessPartsOffList = (JobProcessPartsOffList[])JSONUtil.read(getRequest(), JobProcessPartsOffList[].class);           
                this.manager.saveNodeList(nodeIdx,nodeName,flag, jobProcessPartsOffList);
                map.put("success", "true");
            } catch (Exception e) {
                ExceptionUtil.process(e, logger, map);
            } finally {
                JSONUtil.write(this.getResponse(), map);
            }       
        }
        /**
         * <li>说明：导入
         * <li>创建人：张迪
         * <li>创建日期：2017-4-27
         * <li>修改人： 
         * <li>修改日期：
         * <li>修改内容：
         * @throws JsonMappingException
         * @throws IOException
         */
        public void saveImport() throws JsonMappingException, IOException {
            Map<String, Object> map = new HashMap<String, Object>();
            if (StringUtils.isBlank(processIDX) || "###".equals(processIDX)) {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, "流程未找到！");
            }
            try {
               
//                if (null == fileName || !fileName.endsWith(".xls")) {
//                    map.put(Constants.SUCCESS, false);
//                    map.put(Constants.ERRMSG, "该功能仅支持 Excel2003（*.xls） 版本文件！");
//                } else {
                    List<String> list = new ArrayList<String>();
                    list =  this.manager.saveImport(this.parts, processIDX);
                    map.put(Constants.SUCCESS, true);
                    map.put("errMsg", list);
//                }
            } catch (Exception e) {
                map.put(Constants.SUCCESS, false);
                ExceptionUtil.process(e, logger, map);
            } finally {
                 ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
                 ServletActionContext.getResponse().setContentType("text/html");
                 ServletActionContext.getResponse().getWriter().write(JSONUtil.write(map));
            }
        }
        /**
         * <li>说明：模板导出
         * <li>创建人：张迪
         * <li>创建日期：2017-4-27
         * <li>修改人： 
         * <li>修改日期：
         * <li>修改内容：
         * @throws Exception
         */
        public void download() {
            HttpServletResponse rsp = getResponse();
            InputStream in = null;
            try {
                String filename = "partsOffList-excel.xls";
                in = this.getClass().getResourceAsStream(
                        "/com/yunda/jx/jxgc/processdef/temp/".concat(filename));
                rsp.setContentType("application/x-msdownload");
                rsp.setHeader("Content-Disposition", "attachment;filename="
                        + StringUtil.toISO("下车配件清单-导入模版.xls"));
                OutputStream out = rsp.getOutputStream();
                byte[] b = new byte[1024 * 5];
                int len = in.read(b);
                while (len > 0) {
                    out.write(b, 0, len);
                    len = in.read(b);
                }
            } catch (Exception e) {
                try {
                    rsp.getOutputStream().write("文件不存在或已经被删除！".getBytes());
                } catch (IOException ex) {
                    ExceptionUtil.process(ex,logger);
                }
                ExceptionUtil.process(e,logger);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        ExceptionUtil.process(e,logger);
                    }
                    in = null;
                }
            }
        }

        
        public String getFileName() {
            return fileName;
        }

        
        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        
        public String getProcessIDX() {
            return processIDX;
        }

        
        public void setProcessIDX(String processIDX) {
            this.processIDX = processIDX;
        }

        
        public File getParts() {
            return parts;
        }

        
        public void setParts(File parts) {
            this.parts = parts;
        }
        
}
