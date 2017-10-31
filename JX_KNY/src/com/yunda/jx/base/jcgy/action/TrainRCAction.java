package com.yunda.jx.base.jcgy.action; 

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.jx.base.jcgy.entity.TrainRC;
import com.yunda.jx.base.jcgy.manager.TrainRCManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainRC控制器, 车型对应的修程
 * <li>创建人：王治龙
 * <li>创建日期：2012-10-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class TrainRCAction extends JXBaseAction<TrainRC, TrainRC, TrainRCManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	/** 组织机构业务类：OmOrganizationManager */
	@Resource
    private OmOrganizationManager omOrganizationManager;
	/**
     * 
     * <li>说明：根据所选车型查询修程组件所需信息
     * <li>创建人：程梅
     * <li>创建日期：2012-10-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 返回值为空
     * @throws 抛出异常列表
     */
    @SuppressWarnings("unchecked")
    public void pageListForRCCombo() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String queryHql = req.getParameter("queryHql");
            String undertakeTrainTypeIDX = req.getParameter("undertakeTrainTypeIDX");
            String queryParams = req.getParameter("queryParams");
            Map queryParamsMap = new HashMap();
            if (!StringUtil.isNullOrBlank(queryParams)) {
                queryParamsMap = JSONUtil.read(queryParams, Map.class);
            }
            String orgCode = JXSystemProperties.OVERSEA_ORGCODE;
            OmOrganization org = omOrganizationManager.findOrgForCode(orgCode);
//            map = this.manager.page(queryParamsMap, start, limit, queryHql).extjsStore();
            map = this.manager.page(queryParamsMap, getStart(), getLimit(), queryHql,undertakeTrainTypeIDX,org.getOrgid());
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}