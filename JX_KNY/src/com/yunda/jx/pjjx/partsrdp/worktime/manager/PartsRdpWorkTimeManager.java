package com.yunda.jx.pjjx.partsrdp.worktime.manager;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpManager;
import com.yunda.jx.pjjx.partsrdp.worktime.entity.PartsRdpWorkTime;
import com.yunda.jx.util.MixedUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpWorkTime业务类,工时
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsRdpWorkTimeManager")
public class PartsRdpWorkTimeManager extends JXBaseManager<PartsRdpWorkTime, PartsRdpWorkTime>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    /*
     * 配件检修作业 业务类
     */
    @Resource
    private PartsRdpManager partsRdpManager ;
	/**
	 * 
	 * <li>说明：平均分配工时
	 * <li>创建人：程梅
	 * <li>创建日期：2015-2-3
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param rdpIdx 任务单id
	 * @param siteID 站点标示
	 * @param operaterId 操作员id
	 */
	public void saveWorkTimeAuto(String rdpIdx, String siteID, Long operaterId){
   	 String sql = SqlMapUtil.getSql("pjjx-worktime:insertPartsRdpWorkTime")
     .replace("站点", siteID)
     .replace("创建人", operaterId.toString())
     .replace("创建时间", MixedUtils.dateToStr(null, 2))
     .replace("任务单id", rdpIdx);
   	 this.updateSql(sql);    //更新提票人员工时数据
   }
    /**
     * 
     * <li>说明：平均分配工时
     * <li>创建人：程梅
     * <li>创建日期：2015-2-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 任务单id
     */
    public void updateWorkTimeAverage(String[] ids) {
        PartsRdp rdp ;
        StringBuffer deleteHql ;
        //获取当前登录者信息
        AcOperator ac = SystemContext.getAcOperator();
        for(String id : ids){
            rdp = new PartsRdp();
            rdp = partsRdpManager.getModelById(id);
            deleteHql = new StringBuffer("delete from PartsRdpWorkTime where rdpIDX='").append(id).append("'");
            //删除工时记录
            daoUtils.executUpdateOrDelete(deleteHql.toString());
            //平均分配工时
            saveWorkTimeAuto(rdp.getIdx(), rdp.getSiteID(), ac.getOperatorid());
        }
    }
}