package com.yunda.frame.report.action; 

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.report.entity.PrinterModule;
import com.yunda.frame.report.manager.PrinterModuleManager;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PrinterModule控制器, 报表打印模板
 * <li>创建人：何涛
 * <li>创建日期：2015-01-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class PrinterModuleAction extends JXBaseAction<PrinterModule, PrinterModule, PrinterModuleManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：增量部署
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-30
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void deployByIncrement() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			this.manager.deployByIncrement(ids);
			map.put(Constants.SUCCESS, true);
			map.put(Constants.ERRMSG, null);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
	}
    
	/**
	 * <li>说明：全量部署
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-30
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void deployAll() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		try {
            String sDeleteDir = StringUtil.nvl(req.getParameter("deleteDir"), "false");
            boolean deleteDir = Boolean.parseBoolean(sDeleteDir);
			this.manager.deployAll(deleteDir);
			map.put(Constants.SUCCESS, true);
			map.put(Constants.ERRMSG, null);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
	}
    
    /**
     * <li>说明：单个部署
     * <li>创建人：何涛
     * <li>创建日期：2015-02-06
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void deploySingle() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        
        try {
            PrinterModule entity = this.manager.deploySingle(id);
            map.put(Constants.ENTITY, entity);
            map.put(Constants.SUCCESS, true);
            map.put(Constants.ERRMSG, null);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
	/**
	 * <li>说明：将报表服务器上已部署的报表导入到数据库
	 * <li>创建人：何涛
	 * <li>创建日期：2015-02-03
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void insertDeployedReport() throws JsonMappingException, IOException {
	    Map<String, Object> map = new HashMap<String, Object>();
	    
	    try {
	        this.manager.insertDeployedReport();
	        map.put(Constants.SUCCESS, true);
	        map.put(Constants.ERRMSG, null);
	    } catch (Exception e) {
            map.put(Constants.SUCCESS, false);
            map.put(Constants.ERRMSG, e.getMessage());
            e.printStackTrace();
	    } finally {
	        JSONUtil.write(getResponse(), map);
	    }
	}
	
    /**
     * <li>说明：重写保存方法，用于处理相应业务模块的报表模板定制
     * <li>创建人：何涛
     * <li>创建日期：2015-02-05
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    @Override
    public void saveOrUpdate() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            PrinterModule t = (PrinterModule)JSONUtil.read(getRequest(), entity.getClass());
            // 设置打印报表模板的最近更新日期
            t.setLatestUpdateTime(Calendar.getInstance().getTime());
            
            // 验证报表部署名称是否是以“.cpt”结尾
            if (!t.getDeployName().endsWith(com.yunda.frame.report.entity.ReportConstants.CONST_STR_REPORT_FILE_SUFFIX)) {
                t.setDeployName(t.getDeployName() + com.yunda.frame.report.entity.ReportConstants.CONST_STR_REPORT_FILE_SUFFIX);
            }
            
            // 业务主键
            String businessIDX = getRequest().getParameter("businessIDX");
            String[] errMsg = this.manager.validateUpdate(t);
            if (errMsg == null) {
                this.manager.saveOrUpdate(t, businessIDX);
//              返回记录保存成功的实体对象
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
     * <li>说明：根据“业务主键”获取报表打印模板实体
     * <li>创建人：何涛
     * <li>创建日期：2015-2-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getModelByBusinessIDX() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            // 业务主键
            String businessIDX = getRequest().getParameter("businessIDX");
            // 根据“业务主键”获取报表打印模板实体
            PrinterModule printerModule = this.manager.getModelByBusinessIDX(businessIDX);
            map.put(Constants.ENTITY, printerModule);  
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * <li>说明：获取一个可以进行报表打印预览的报表打印模板实体，该实体下已设置报表文件，且已部署到报表服务器上
     * <li>创建人：何涛
     * <li>创建日期：2015-02-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @throws IOException 
     * @throws JsonMappingException
     */
    @SuppressWarnings("unchecked")
    public void getModelForPreview() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // 正常情况下仅会根据idx和businessIDX其中一个查询报表打印模板实体
            String idx = getRequest().getParameter("idx");
            String businessIDX = getRequest().getParameter("businessIDX");
            PrinterModule pm = null;
            if (null != businessIDX) {
                pm = this.manager.getModelForPreview(businessIDX, true);
            } else {
                pm = this.manager.getModelForPreview(idx, false);
            }
            map.put(Constants.ERRMSG, null);
            map.put(Constants.ENTITY, pm);
        } catch (NullPointerException e) {
            map.put(Constants.ERRMSG, e.getMessage());
            map.put(Constants.ENTITY, null);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
	
}