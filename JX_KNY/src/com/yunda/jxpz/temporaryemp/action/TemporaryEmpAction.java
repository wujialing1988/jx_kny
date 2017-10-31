package com.yunda.jxpz.temporaryemp.action; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jxpz.temporaryemp.entity.TemporaryEmp;
import com.yunda.jxpz.temporaryemp.manager.TemporaryEmpManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TemporaryEmp控制器, 临时人员设置表
 * <li>创建人：程梅
 * <li>创建日期：2015-04-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class TemporaryEmpAction extends JXBaseAction<TemporaryEmp, TemporaryEmp, TemporaryEmpManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：新增验证
     * <li>创建人：程梅
     * <li>创建日期：2015-4-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveFromEmployee() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            TemporaryEmp[] empList = (TemporaryEmp[])JSONUtil.read(getRequest(), TemporaryEmp[].class);
            String[] errMsg = this.manager.validateUpdateList(empList);
            if (errMsg == null || errMsg.length < 1) {
                List<TemporaryEmp> list = new ArrayList<TemporaryEmp>(empList.length);
                for(TemporaryEmp emp : empList){
                    list.add(emp);
                }
                this.manager.saveOrUpdate(list);
                map.put("success", true);
            } else {
                map.put("success", false);
                map.put("errMsg", errMsg);
            }
         } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
        
    }
}