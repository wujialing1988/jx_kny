package com.yunda.jx.scdd.repairplan.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jczl.attachmanage.entity.JczlTrain;
import com.yunda.jx.scdd.repairplan.entity.RepairWarningHC;
import com.yunda.frame.common.Page;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: RepairWarningHC业务类，货车修程预警
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-12-13
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value="repairWarningHCManager")
public class RepairWarningHCManager extends JXBaseManager<RepairWarningHC, RepairWarningHC> {
	
	
    /**
     * <li>说明：同步车辆信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public String synchronizeData() throws BusinessException, NoSuchFieldException {
    	StringBuffer hql = new StringBuffer("select t From JczlTrain t where t.recordStatus = 0 and t.vehicleType = '10' ");
    	hql.append(" and not exists ( select 1 from RepairWarningHC h where h.trainTypeIdx = t.trainTypeIDX and h.trainNo = t.trainNo )");
    	List<JczlTrain> jczlTrains = this.daoUtils.find(hql.toString());
        if (CollectionUtils.isEmpty(jczlTrains)) {
            return "没有需要同步的数据！" ;
        }
    	List<RepairWarningHC> hcs = new ArrayList<RepairWarningHC>();
    	RepairWarningHC hc = null ;
    	for (JczlTrain jczlTrain : jczlTrains) {
    		hc = new RepairWarningHC();
    		hc.setTrainTypeIdx(jczlTrain.getTrainTypeIDX());
    		hc.setTrainType(jczlTrain.getTrainTypeShortName());
    		hc.setTrainNo(jczlTrain.getTrainNo());
//    		hc.setBeforeFxDate(jczlTrain.getLeaveDate());
//    		hc.setBeforeDxDate(jczlTrain.getLeaveDate());
//    		hc.setBeforeCxDate(jczlTrain.getLeaveDate());
    		hc.setUpdateTime(new Date());
    		hcs.add(hc);
		}
    	if(hcs != null && hcs.size() > 0){
    		this.saveOrUpdate(hcs);
    	}
        return null ;
    }
    
    /**
     * <li>说明：查询货车修程提醒
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param start 开始条数
     * @param limit 限制条数
     * @param queryInput 车型车号模糊查询
     * @param planDayFx 辅修年月
     * @param planDayDx 段修年月
     * @return
     */
    public Page<Map<String, Object>> findHCRepairWarningList(int start,int limit,String queryInput,String planDayFx,String planDayDx){
        String sql = SqlMapUtil.getSql("kny-repairwarning:findHCRepairWarningList");
        // 车型车号模糊
        if(!StringUtil.isNullOrBlank(queryInput)){
        	sql += "and (" ;
        	sql += " t.trainType like '%"+queryInput+"%' or t.trainNo like '%"+queryInput+"%'" ;
        	sql += ")" ;
        }
        // 厂修年月
        if(!StringUtil.isNullOrBlank(planDayFx)){
        	sql += " and t.nextCxDate = '"+ planDayFx.substring(0, 7) + "'";
        }
        // 段修年月
        if(!StringUtil.isNullOrBlank(planDayDx)){
        	sql += " and t.nextDxDate = '"+ planDayDx.substring(0, 7) + "'";
        }
        sql += " order by t.nextCxDate , t.nextDxDate , t.nextFxDate ";
        String totalSql = "Select count(*) as rowcount "+ sql.substring(sql.indexOf("from"));
        return this.queryPageMap(totalSql, sql, start, limit, false);
    }
    
    /**
     * <li>说明：清空货车修程对应的最新时间
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx 车型id
     * @param trainNo 车号
     * @param repairClass 修程编码
     * @param beForeDate 最新的修程时间
     * @return
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void clearHCBeForeDate(String trainTypeIdx,String trainNo,String repairClass,Date beForeDate) throws BusinessException, NoSuchFieldException{
    	RepairWarningHC hc = this.getRepairWarningHC(trainTypeIdx, trainNo);
    	if(hc != null){
    		if("30".equals(repairClass)){
    			// 辅修
    			hc.setBeforeFxDate(beForeDate);
    		}else if("20".equals(repairClass)){
    			// 段修
    			hc.setBeforeFxDate(beForeDate);
    			hc.setBeforeDxDate(beForeDate);
    		}else if("10".equals(repairClass)){
    			// 厂修
    			hc.setBeforeFxDate(beForeDate);
    			hc.setBeforeDxDate(beForeDate);
    			hc.setBeforeCxDate(beForeDate);
    		}
    		this.saveOrUpdate(hc);
    	}
    }
    
    /**
     * <li>说明：通过车型车号查询货车修程提醒实体
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx 车型id
     * @param trainNo 车号
     * @return
     */
    public RepairWarningHC getRepairWarningHC(String trainTypeIdx,String trainNo){
    	StringBuffer hql = new StringBuffer(" From RepairWarningHC where trainTypeIdx = ? and trainNo = ? ");
    	return (RepairWarningHC)this.daoUtils.findSingle(hql.toString(), new Object[]{trainTypeIdx,trainNo});
    }
    
    /**
     * <li>说明：查询货车基本及修程信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx 车型
     * @param trainNo 车号
     * @return
     */
    public List<Map<String, Object>> findTrainRepairInfosForHC(String trainTypeIdx,String trainNo){
        String sql = SqlMapUtil.getSql("kny-repairwarning:findTrainRepairInfosForHC");
        // 车型
        if(!StringUtil.isNullOrBlank(trainTypeIdx)){
        	sql += " and info.train_type_idx = '" + trainTypeIdx + "'" ;
        }
        // 车号
        if(!StringUtil.isNullOrBlank(trainNo)){
        	sql += " and info.train_no = '" + trainNo + "'" ;
        }
        sql += " order by info.train_type_shortname , info.train_no " ;
        return this.queryListMap(sql);
    }
	
}
