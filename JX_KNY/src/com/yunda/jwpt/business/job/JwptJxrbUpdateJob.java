package com.yunda.jwpt.business.job;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jwpt.business.entity.JwptJxrb;
import com.yunda.jwpt.business.manager.JwptJxrbManager;
import com.yunda.jwpt.common.AbstractBaseBusinessDataUpdateJob;
import com.yunda.jx.jsgl.jxrb.entity.DailyReport;
import com.yunda.jx.jsgl.jxrb.manager.DailyReportManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车检修日报 更新业务类
 * <li>创建人：何涛
 * <li>创建日期：2016-06-01
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value = "jsgl_jxrb")
public class JwptJxrbUpdateJob extends AbstractBaseBusinessDataUpdateJob<DailyReport, JwptJxrb> {
    
    /** JxglJxrb业务类,机车检修日报 */
    @Resource
    private JwptJxrbManager jxglJxrbManager;
    
    /** DailyReport业务类,生产调度—机车检修日报 */
    @Resource
    private DailyReportManager dailyReportManager;

    /**
     * <li>说明：获取检修业务实体业务管理器
     * <li>创建人：何涛
     * <li>创建日期：2016-6-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 检修业务实体管理器
     */
    @Override
    protected JXBaseManager<JwptJxrb, JwptJxrb> getEManager() {
        return jxglJxrbManager;
    }
    
    /**
     * <li>说明：获取数据同步实体业务管理器
     * <li>创建人：何涛
     * <li>创建日期：2016-6-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 数据同步实体管理器
     */
    @Override
    protected JXBaseManager<DailyReport, DailyReport> getTManager() {
        return dailyReportManager;
    }

}
