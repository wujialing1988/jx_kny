package com.yunda.jx.scdd.repairplan.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jczl.attachmanage.entity.JczlTrain;
import com.yunda.jx.scdd.repairplan.entity.RepairStandard;
import com.yunda.jx.scdd.repairplan.entity.RepairStandardTime;
import com.yunda.jx.scdd.repairplan.entity.RepairWarningKC;
import com.yunda.jxpz.utils.SystemConfigUtil;
import com.yunda.passenger.marshalling.entity.MarshallingTrain;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: RepairWarningKC业务类，客车修程预警
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-12-13
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value="repairWarningKCManager")
public class RepairWarningKCManager extends JXBaseManager<RepairWarningKC, RepairWarningKC> {
	
	
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
    	StringBuffer hql = new StringBuffer("select t From JczlTrain t where t.recordStatus = 0 and t.vehicleType = '20' ");
    	hql.append(" and not exists ( select 1 from RepairWarningKC h where h.trainTypeIdx = t.trainTypeIDX and h.trainNo = t.trainNo )");
    	List<JczlTrain> jczlTrains = this.daoUtils.find(hql.toString());
        if (CollectionUtils.isEmpty(jczlTrains)) {
            return "没有需要同步的数据！" ;
        }
    	List<RepairWarningKC> kcs = new ArrayList<RepairWarningKC>();
    	RepairWarningKC kc = null ;
    	for (JczlTrain jczlTrain : jczlTrains) {
    		kc = new RepairWarningKC();
    		kc.setTrainTypeIdx(jczlTrain.getTrainTypeIDX());
    		kc.setTrainType(jczlTrain.getTrainTypeShortName());
    		kc.setTrainNo(jczlTrain.getTrainNo());
    		kc.setA1Km(Float.parseFloat("0"));
    		kc.setA2Km(Float.parseFloat("0"));
    		kc.setA3Km(Float.parseFloat("0"));
    		kc.setA4Km(Float.parseFloat("0"));
    		kc.setA5Km(Float.parseFloat("0"));
    		kc.setUpdateTime(new Date());
    		kcs.add(kc);
		}
    	if(kcs != null && kcs.size() > 0){
    		this.saveOrUpdate(kcs);
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
    public Page<Map<String, Object>> findKCRepairWarningList(int start,int limit,String queryInput,Map<String, String> otherParams){
        String sql = SqlMapUtil.getSql("kny-repairwarning:findKCRepairWarningList");
        // 车型车号模糊
        if(!StringUtil.isNullOrBlank(queryInput)){
        	sql += "and (" ;
        	sql += " kc.train_type like '%"+queryInput+"%' or kc.train_no like '%"+queryInput+"%'" ;
        	sql += ")" ;
        }
        // 只看预警
        String repairType = otherParams.get("repairType");
        if(!StringUtil.isNullOrBlank(repairType) && "true".equals(repairType)){
        	sql += " and kc.repair_class is not null  " ;
        }
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
    public void clearKCBeForeDate(String trainTypeIdx,String trainNo,String repairClass,Date beForeDate) throws BusinessException, NoSuchFieldException{
    	RepairWarningKC kc = this.getRepairWarningKC(trainTypeIdx, trainNo);
    	if(kc != null){
    		if("50".equals(repairClass)){
    			// A1
    			kc.setA1Km(Float.parseFloat("0"));
    			kc.setBeforeA1Date(beForeDate);
    		}else if("60".equals(repairClass)){
    			// A2
    			kc.setA1Km(Float.parseFloat("0"));
    			kc.setA2Km(Float.parseFloat("0"));
    			kc.setBeforeA2Date(beForeDate);
    		}else if("70".equals(repairClass)){
    			// A3
    			kc.setA1Km(Float.parseFloat("0"));
    			kc.setA2Km(Float.parseFloat("0"));
    			kc.setA3Km(Float.parseFloat("0"));
    			kc.setBeforeA3Date(beForeDate);
    		}else if("80".equals(repairClass)){
    			// A4
    			kc.setA1Km(Float.parseFloat("0"));
    			kc.setA2Km(Float.parseFloat("0"));
    			kc.setA3Km(Float.parseFloat("0"));
    			kc.setA4Km(Float.parseFloat("0"));
    			kc.setBeforeA4Date(beForeDate);
    		}else if("90".equals(repairClass)){
    			// A5
    			kc.setA1Km(Float.parseFloat("0"));
    			kc.setA2Km(Float.parseFloat("0"));
    			kc.setA3Km(Float.parseFloat("0"));
    			kc.setA4Km(Float.parseFloat("0"));
    			kc.setA5Km(Float.parseFloat("0"));
    			kc.setBeforeA5Date(beForeDate);
    		}
    		// 清空预警修程
    		if(repairClass.equals(kc.getRepairClass())){
    			kc.setRepairClass(null);
    			kc.setRepairClassName(null);
    			kc.setRepairWarningType(null);
    		}
    		this.saveOrUpdate(kc);
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
    public RepairWarningKC getRepairWarningKC(String trainTypeIdx,String trainNo){
    	StringBuffer hql = new StringBuffer(" From RepairWarningKC where trainTypeIdx = ? and trainNo = ? ");
    	return (RepairWarningKC)this.daoUtils.findSingle(hql.toString(), new Object[]{trainTypeIdx,trainNo});
    }

    /**
	 * <li>方法说明：重新计算下次修程
	 * <li>方法名：reCompute
     * @throws Exception 
	 * @throws IOException
	 * <li>创建人：伍佳灵
	 * <li>创建日期：2017-12-14
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public void updateComputeNextRepair() throws Exception {
		List<RepairWarningKC> kcs = this.findRepairWarningKCList();
		List<RepairStandard> standardList = null;
		for (RepairWarningKC kc : kcs) {
			standardList = getTrainRepairStandard(kc.getTrainTypeIdx());
			if(!setNextRepair(kc, standardList)){
				
			}
			// 通过时间维度计算下次各个修程的时间
			setNextRepairDate(kc);
		}
	}
	
	/**
	 * <li>方法说明： 通过时间维度计算下次各个修程的时间
	 * <li>方法名：setNextRepairDate
	 * @param kc 修程提醒实体
	 * @return 
	 * <li>创建人： 伍佳灵
	 * <li>创建日期：2017-12-21
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws Exception 
	 */
	private void setNextRepairDate(RepairWarningKC kc) throws Exception{
		kc.setNextA1Date(getNextDate(kc,getTrainRepairStandardTime(kc.getTrainTypeIdx(), "50"))); // 下次A1
		kc.setNextA2Date(getNextDate(kc,getTrainRepairStandardTime(kc.getTrainTypeIdx(), "60"))); // 下次A1
		kc.setNextA3Date(getNextDate(kc,getTrainRepairStandardTime(kc.getTrainTypeIdx(), "70"))); // 下次A1
		kc.setNextA4Date(getNextDate(kc,getTrainRepairStandardTime(kc.getTrainTypeIdx(), "80"))); // 下次A1
		kc.setNextA5Date(getNextDate(kc,getTrainRepairStandardTime(kc.getTrainTypeIdx(), "90"))); // 下次A1
		
		// 设置预警修程
		String limitValueStr = SystemConfigUtil.getValue("knySys.freightSys.synchronizeKCWarningDataDispatch");
		if(StringUtil.isNullOrBlank(limitValueStr)){
			return ;
		}
		Date beginDate = new Date();
		int limitValue = Integer.parseInt(limitValueStr);
		// A5
		if(StringUtil.isNullOrBlank(kc.getRepairClass()) && kc.getNextA5Date() != null){
		   int betweenDay =	DateUtil.getDaysBetween(beginDate, kc.getNextA5Date());
		   // 如果没有超出检修时间，并且在提醒时间范围内
		   if(kc.getNextA5Date().getTime() > beginDate.getTime() && limitValue >= betweenDay){
			   kc.setRepairClass("90");
			   kc.setRepairClassName("A5");
			   kc.setRepairWarningType("20");
			   return ;
		   }
		}
		
		// A4
		if(StringUtil.isNullOrBlank(kc.getRepairClass()) && kc.getNextA4Date() != null){
		   int betweenDay =	DateUtil.getDaysBetween(beginDate, kc.getNextA4Date());
		   // 如果没有超出检修时间，并且在提醒时间范围内
		   if(kc.getNextA4Date().getTime() > beginDate.getTime() && limitValue >= betweenDay){
			   kc.setRepairClass("80");
			   kc.setRepairClassName("A4");
			   kc.setRepairWarningType("20");
			   return ;
		   }
		}
		
		// A3
		if(StringUtil.isNullOrBlank(kc.getRepairClass()) && kc.getNextA3Date() != null){
		   int betweenDay =	DateUtil.getDaysBetween(beginDate, kc.getNextA3Date());
		   // 如果没有超出检修时间，并且在提醒时间范围内
		   if(kc.getNextA3Date().getTime() > beginDate.getTime() && limitValue >= betweenDay){
			   kc.setRepairClass("70");
			   kc.setRepairClassName("A3");
			   kc.setRepairWarningType("20");
			   return ;
		   }
		}
		
		// A2
		if(StringUtil.isNullOrBlank(kc.getRepairClass()) && kc.getNextA2Date() != null){
		   int betweenDay =	DateUtil.getDaysBetween(beginDate, kc.getNextA2Date());
		   // 如果没有超出检修时间，并且在提醒时间范围内
		   if(kc.getNextA2Date().getTime() > beginDate.getTime() && limitValue >= betweenDay){
			   kc.setRepairClass("60");
			   kc.setRepairClassName("A2");
			   kc.setRepairWarningType("20");
			   return ;
		   }
		}
		
		// A1
		if(StringUtil.isNullOrBlank(kc.getRepairClass()) && kc.getNextA1Date() != null){
		   int betweenDay =	DateUtil.getDaysBetween(beginDate, kc.getNextA1Date());
		   // 如果没有超出检修时间，并且在提醒时间范围内
		   if(kc.getNextA1Date().getTime() > beginDate.getTime() && limitValue >= betweenDay){
			   kc.setRepairClass("50");
			   kc.setRepairClassName("A1");
			   kc.setRepairWarningType("20");
			   return ;
		   }
		}
		
	}
	
	/**
	 * <li>方法说明： 获取下次修程时间
	 * <li>方法名：getNextDate
	 * @param kc 修程提醒实体
	 * @param standardTimes 时间维度
	 * @return 
	 * <li>创建人： 伍佳灵
	 * <li>创建日期：2017-12-21
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	private Date getNextDate(RepairWarningKC kc,List<RepairStandardTime> standardTimes){
		Date nextDate = null ;
		for (RepairStandardTime repairStandardTime : standardTimes) {
			if(repairStandardTime.getRepairClassCompare().equals("50") && kc.getBeforeA1Date() != null){
				nextDate = getNextDateNew(nextDate, kc.getBeforeA1Date(), repairStandardTime.getCompareDay());
			}else if(repairStandardTime.getRepairClassCompare().equals("60") && kc.getBeforeA2Date() != null){
				nextDate = getNextDateNew(nextDate, kc.getBeforeA2Date(), repairStandardTime.getCompareDay());
			}else if(repairStandardTime.getRepairClassCompare().equals("70") && kc.getBeforeA3Date() != null){
				nextDate = getNextDateNew(nextDate, kc.getBeforeA3Date(), repairStandardTime.getCompareDay());
			}else if(repairStandardTime.getRepairClassCompare().equals("80") && kc.getBeforeA4Date() != null){
				nextDate = getNextDateNew(nextDate, kc.getBeforeA4Date(), repairStandardTime.getCompareDay());
			}else if(repairStandardTime.getRepairClassCompare().equals("90") && kc.getBeforeA5Date() != null){
				nextDate = getNextDateNew(nextDate, kc.getBeforeA5Date(), repairStandardTime.getCompareDay());
			}
		}
		return nextDate ;
	}
	
	/**
	 * <li>方法说明： 获取最新的下次修程时间
	 * <li>方法名：getNextDateNew
	 * @param kc 修程提醒实体
	 * @param standardTimes 时间维度
	 * @return 
	 * <li>创建人： 伍佳灵
	 * <li>创建日期：2017-12-21
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	private Date getNextDateNew(Date nextDate,Date beforeDate, int compareDay){
		Date newDate = DateUtil.plusDay(beforeDate, compareDay);
		if(nextDate == null){
			nextDate = newDate ;
		}else{
			// 如果比较后的时间大于前面的时间，则把小的时间赋值过去
			if(nextDate.getTime() > newDate.getTime()){
				nextDate = newDate ;
			}
		}
		return nextDate ;
	}
	
	/**
	 * <li>方法说明： 设置走行下一次修程
	 * <li>方法名：setNextRepair
	 * @param rkm 走行
	 * @param list 检修标准集合
	 * @return 匹配上否
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-31
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	private boolean setNextRepair(RepairWarningKC kc, List<RepairStandard> list){
        if(null!=kc.getA5Km() && matchNextRepair(kc.getA5Km(), kc, list)){
            return true;
        }
        if(null!=kc.getA4Km() && matchNextRepair(kc.getA4Km(), kc, list)){
            return true;
        } 
        if(null!=kc.getA3Km() && matchNextRepair(kc.getA3Km(), kc, list)){
            return true;
        }  
        if(null!=kc.getA2Km() && matchNextRepair(kc.getA2Km(), kc, list)){
            return true;
        } 
        if(null!=kc.getA1Km() && matchNextRepair(kc.getA1Km(), kc, list)){
            return true;
        }         
        kc.setRepairClass(null);
        kc.setRepairClassName(null);
        kc.setRepairWarningType(null);
        return false;
	}

	/**
	 * <li>方法说明：匹配下一次修程
	 * <li>方法名：matchNextRepair
	 * @param km 当前匹配走行
	 * @param rkm 待匹配走行
	 * @param list 待匹配检修标准
	 * @return
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-31
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	private boolean matchNextRepair(float km, RepairWarningKC kc, List<RepairStandard> list) {
	    for(RepairStandard t : list){
            if(t.getMinRunningKm() <= km && t.getMaxRunningKm() >= km){
                setRepair(kc, t);
                return true;
            }
        }
		return false;
	}

	/**
	 * <li>方法说明：设置修程信息
	 * <li>方法名：setRepair
	 * @param rkm 走行实体
	 * @param t 修程信息
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-31
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	private void setRepair(RepairWarningKC kc, RepairStandard t) {
		kc.setRepairClassName(t.getRepairClassName());
		kc.setRepairClass(t.getRepairClass());
		kc.setRepairWarningType("10");
	}
	
    /**
	 * <li>方法说明：查询所有的客车修程
	 * <li>方法名：findRepairWarningKCList
	 * <li>创建人：伍佳灵
	 * <li>创建日期：2017-12-14
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public List<RepairWarningKC> findRepairWarningKCList(){
		StringBuffer hql = new StringBuffer(" From RepairWarningKC where 1=1 ");
		return this.daoUtils.find(hql.toString());
	}
	
	/**
	 * <li>方法说明：查询一种车型检修标准
	 * <li>方法名：getTrainRepairStandard
	 * @param trainTypeIdx 车型主键
	 * @return 检修标准集合
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-31
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	private List<RepairStandard> getTrainRepairStandard(String trainTypeIdx){
		String hql = "from RepairStandard where trainTypeIdx = ? order by minRunningKm desc";
		return daoUtils.find(hql, trainTypeIdx);
	}
	
	/**
	 * <li>方法说明：查询一种车型检修标准
	 * <li>方法名：getTrainRepairStandard
	 * @param trainTypeIdx 车型主键
	 * @param repairClass 修程
	 * @return 检修标准集合
	 * <li>创建人：伍佳灵
	 * <li>创建日期：2017-12-21
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	private List<RepairStandardTime> getTrainRepairStandardTime(String trainTypeIdx,String repairClass){
		String hql = "from RepairStandardTime where trainTypeIdx = ? and repairClass = ? order by repairClassCompare desc";
		return daoUtils.find(hql, trainTypeIdx,repairClass);
	}
	
    /**
	 * <li>方法说明：通过编组任务更新客车走行
	 * <li>方法名：updateKm
	 * <li>创建人：伍佳灵
	 * <li>创建日期：2017-12-14
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
     * @throws NoSuchFieldException 
     * @throws BusinessException 
	 */
	public void updateKm(List<MarshallingTrain> trainList,Integer kilometers) throws BusinessException, NoSuchFieldException{
		for (MarshallingTrain train : trainList) {
			RepairWarningKC kc = this.getRepairWarningKC(train.getTrainTypeIDX(), train.getTrainNo());
			if(kilometers != null){
				kc.setA1Km(kc.getA1Km() + kilometers.longValue());
				kc.setA2Km(kc.getA2Km() + kilometers.longValue());
				kc.setA3Km(kc.getA3Km() + kilometers.longValue());
				kc.setA4Km(kc.getA4Km() + kilometers.longValue());
				kc.setA5Km(kc.getA5Km() + kilometers.longValue());
				kc.setTotalKm((kc.getTotalKm() == null ? 0 : kc.getTotalKm()) + kilometers.longValue());
				this.saveOrUpdate(kc);
			}
		}
	}
	
	
	 /**
     * <li>说明：查询客车基本及修程信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx 车型
     * @param trainNo 车号
     * @param notMarshalling 是否不在编组中
     * @param isJx 是否检修中
     * @return
     */
    public List<Map<String, Object>> findTrainRepairInfosForKC(String trainTypeIdx,String trainNo,boolean notMarshalling,boolean isJx){
        String sql = SqlMapUtil.getSql("kny-repairwarning:findTrainRepairInfosForKC");
        // 车型
        if(!StringUtil.isNullOrBlank(trainTypeIdx)){
        	sql += " and info.train_type_idx = '" + trainTypeIdx + "'" ;
        }
        // 车号
        if(!StringUtil.isNullOrBlank(trainNo)){
        	sql += " and info.train_no = '" + trainNo + "'" ;
        }
        // 是否不在编组中
        if(notMarshalling){
        	sql += " and mt.idx is null " ;
        }
        // 是否在检修
        if(isJx){
        	sql += " and wp.idx is not null " ;
        }
        sql += " order by info.train_type_shortname , info.train_no " ;
        return this.queryListMap(sql);
    }
	
}
