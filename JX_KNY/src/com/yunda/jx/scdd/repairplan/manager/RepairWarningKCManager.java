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
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jczl.attachmanage.entity.JczlTrain;
import com.yunda.jx.scdd.repairplan.entity.RepairStandard;
import com.yunda.jx.scdd.repairplan.entity.RepairWarningKC;
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
	 * @throws IOException
	 * <li>创建人：伍佳灵
	 * <li>创建日期：2017-12-14
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public void updateComputeNextRepair() {
		List<RepairWarningKC> kcs = this.findRepairWarningKCList();
		List<RepairStandard> standardList = null;
		for (RepairWarningKC kc : kcs) {
			standardList = getTrainRepairStandard(kc.getTrainTypeIdx());
			if(!setNextRepair(kc, standardList)){
				// 如果通过走行匹配不到，则通过时间匹配（原则：走行优先，时间为辅）
			}
		}
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
	
}
