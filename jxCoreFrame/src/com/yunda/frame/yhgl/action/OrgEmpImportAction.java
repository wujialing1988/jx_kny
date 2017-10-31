package com.yunda.frame.yhgl.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.yhgl.entity.OrganizationForImport;
import com.yunda.frame.yhgl.manager.OrgEmpImportManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 导入机构人员
 * <li>创建人：何东
 * <li>创建日期：2016-09-01
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class OrgEmpImportAction extends JXBaseAction<OrganizationForImport,OrganizationForImport,OrgEmpImportManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** 机构人员导入文件的File实体 */
    private File orgEmpImport;
    
    /** 机构人员导入文件名称 */
    private String orgEmpImportFileName;
	
    /**
	 * <li>说明：判断组织表中是否存在数据，有数据不允许导入
	 * <li>创建人：何东
	 * <li>创建日期：2016-09-01
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unused")
	public void isCanImport() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			Boolean isCanImport = this.manager.isCanImport();
			map.put("success", true);
			map.put("isCanImport", isCanImport);
		} catch (Exception e){
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
    
	/**
     * <li>说明：导入机构人员
     * <li>创建人：何东
     * <li>创建日期：2016-09-01
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void saveImport() throws JsonMappingException, IOException {
    	Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (null == orgEmpImportFileName || !orgEmpImportFileName.endsWith(".xls")) {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, "该功能仅支持 Excel2003（*.xls） 版本文件！");
            } else {
                this.manager.saveImport(this.orgEmpImport);
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

	public File getOrgEmpImport() {
		return orgEmpImport;
	}

	public void setOrgEmpImport(File orgEmpImport) {
		this.orgEmpImport = orgEmpImport;
	}

	public String getOrgEmpImportFileName() {
		return orgEmpImportFileName;
	}

	public void setOrgEmpImportFileName(String orgEmpImportFileName) {
		this.orgEmpImportFileName = orgEmpImportFileName;
	}
}
