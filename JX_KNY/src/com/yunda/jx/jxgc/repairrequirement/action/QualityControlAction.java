package com.yunda.jx.jxgc.repairrequirement.action; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.repairrequirement.entity.QualityControl;
import com.yunda.jx.jxgc.repairrequirement.manager.QualityControlManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：QualityControl控制器, 质量控制
 * <li>创建人：王治龙
 * <li>创建日期：2012-12-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class QualityControlAction extends JXBaseAction<QualityControl, QualityControl, QualityControlManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
    /**
     * 
     * <li>说明：查询检测/检修项目质量控制使用情况
     * <li>创建人：程锐
     * <li>创建日期：2013-5-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void findWorkStepQC() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String idx = StringUtil.nvlTrim( req.getParameter("idx"), null);
            List<QualityControl> entityList = this.manager.getModelList(idx);
            map.put("entityList", entityList);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    /**
     * 
     * <li>说明：根据作业卡id查询作业工单的质量检查信息
     * <li>创建人：程梅
     * <li>创建日期：2013-8-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    public void pageListForCardQR() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
			String workCardId = req.getParameter("workCardId");
			List<Map<String,String>> mapRecord = this.manager.pageListForCardQR(workCardId);
			map.put("id", EntityUtil.IDX);
			map.put("root", mapRecord);
			map.put("totalProperty", mapRecord == null ? 0 : mapRecord.size() );
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
}