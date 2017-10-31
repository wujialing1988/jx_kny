
package com.yunda.jx.jxgc.buildupmanage.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.buildupmanage.entity.FixBuildUpType;
import com.yunda.jx.jxgc.buildupmanage.manager.FixBuildUpTypeManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：FixBuildUpType控制器, 可安装组成型号
 * <li>创建人：程锐
 * <li>创建日期：2012-10-24
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class FixBuildUpTypeAction extends JXBaseAction<FixBuildUpType, FixBuildUpType, FixBuildUpTypeManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：批量保存可安装组成型号
     * <li>创建人：程锐
     * <li>创建日期：2012-11-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void saveOrUpdateList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();        
        try {
            FixBuildUpType[] list =
                (FixBuildUpType[]) JSONUtil.read(getRequest(), FixBuildUpType[].class); // 获取json数组转换成对象数组
            this.manager.saveOrUpdateList(list);
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * 
     * <li>说明：设置缺省可安装组成型号
     * <li>创建人：程锐
     * <li>创建日期：2012-11-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void setIsDefault() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            FixBuildUpType fixBuildUpType = this.manager.getModelById(id);
            this.manager.setIsDefault(fixBuildUpType);
            map.put("success", "true");            
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
}
