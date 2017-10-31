package com.yunda.jx.pjjx.partsrdp.expendmat.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjjx.partsrdp.expendmat.entity.PartsRdpExpendMat;
import com.yunda.jx.pjjx.partsrdp.expendmat.manager.PartsRdpExpendMatManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpExpendMat控制器, 物料消耗记录
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
public class PartsRdpExpendMatAction extends JXBaseAction<PartsRdpExpendMat, PartsRdpExpendMat, PartsRdpExpendMatManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：批量添加检修工艺工单所需物料
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void saveExpendMats() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			PartsRdpExpendMat[] partsRdpExpendMats = JSONUtil.read(getRequest(), PartsRdpExpendMat[].class);
			this.manager.saveExpendMats(partsRdpExpendMats);
			map.put(Constants.SUCCESS,	true);
		} catch (Exception e) {
			map.put(Constants.SUCCESS,	false);
			map.put(Constants.ERRMSG,	e.getMessage());
			ExceptionUtil.process(e, logger);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
        
	}
    
	/**
	 * <li>说明：更新物料消耗数量
	 * <li>创建人：何涛
	 * <li>创建日期：2015-10-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void updateExpendMats() throws JsonMappingException, IOException {
	    Map<String, Object> map = new HashMap<String, Object>();
	    try {
	        PartsRdpExpendMat[] partsRdpExpendMats = JSONUtil.read(getRequest(), PartsRdpExpendMat[].class);
	        this.manager.updateExpendMats(partsRdpExpendMats);
	        map.put(Constants.SUCCESS,	true);
	    } catch (Exception e) {
	        map.put(Constants.SUCCESS,	false);
	        map.put(Constants.ERRMSG,	e.getMessage());
	        ExceptionUtil.process(e, logger);
	    } finally {
	        JSONUtil.write(getResponse(), map);
	    }
    }
    
    /**
     * <li>说明：将物料消耗一键设置为额定数量
     * <li>创建人：何涛
     * <li>创建日期：2015-10-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void updateToDefault() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String rdpIDX = req.getParameter("rdpIDX");
            String rdpTecCardIDX = req.getParameter("rdpTecCardIDX");
            this.manager.updateToDefault(rdpIDX, rdpTecCardIDX);
            map.put(Constants.SUCCESS,  true);
        } catch (Exception e) {
            map.put(Constants.SUCCESS,  false);
            map.put(Constants.ERRMSG,   e.getMessage());
            ExceptionUtil.process(e, logger);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    
    }
    
}