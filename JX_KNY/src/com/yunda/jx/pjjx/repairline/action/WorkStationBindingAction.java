package com.yunda.jx.pjjx.repairline.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.repairline.entity.WorkStationBinding;
import com.yunda.jx.pjjx.repairline.manager.WorkStationBindingManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkStationBinding控制器, 配件检修人员绑定工位
 * <li>创建人：程梅
 * <li>创建日期：2015-10-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class WorkStationBindingAction extends JXBaseAction<WorkStationBinding, WorkStationBinding, WorkStationBindingManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：保存人员绑定工位信息
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveStationBinding() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String bindingJson = StringUtil.nvlTrim( req.getParameter("bindingData"), "{}" );
            WorkStationBinding binding = (WorkStationBinding)JSONUtil.read(bindingJson, WorkStationBinding.class);
            String[] errMsg = this.manager.validateSave(binding);
          if (errMsg == null || errMsg.length < 1) {
              this.manager.saveStationBinding(binding);
              map.put("success", "true");
          }else{
              map.put("errMsg", errMsg);
          }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * <li>说明：解除人员绑定工位信息（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-10-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void saveStationUnbinding() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        HttpServletRequest req = getRequest();
        try {
            WorkStationBinding wsb = JSONUtil.read(req, WorkStationBinding.class);
            this.manager.saveStationUnbinding(wsb);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
}