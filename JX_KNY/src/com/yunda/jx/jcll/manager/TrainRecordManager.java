package com.yunda.jx.jcll.manager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.freight.zb.gztp.entity.Gztp;
import com.yunda.freight.zb.gztp.manager.GztpManager;
import com.yunda.freight.zb.plan.manager.ZbglRdpPlanManager;
import com.yunda.jx.jcll.entity.TrainRecord;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicket;
import com.yunda.jx.jxgc.tpmanage.manager.FaultTicketManager;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.entity.WorkPlanRepairActivity;
import com.yunda.jx.jxgc.workplanmanage.manager.TrainWorkPlanManager;
import com.yunda.jx.jxgc.workplanmanage.manager.WorkPlanRepairActivityManager;
import com.yunda.jx.pjwz.fixparts.entity.PartsFixRegister;
import com.yunda.jx.pjwz.fixparts.manager.PartsFixRegisterManager;
import com.yunda.jx.pjwz.unloadparts.entity.PartsUnloadRegister;
import com.yunda.jx.pjwz.unloadparts.manager.PartsUnloadRegisterManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车履历业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-10-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value="trainRecordManager")
public class TrainRecordManager extends JXBaseManager<TrainRecord, TrainRecord> {
	
	/**
	 * 车辆检修计划业务类
	 */
	@Resource
	private TrainWorkPlanManager trainWorkPlanManager ;
	
	/**
	 * 车辆检修计划业务类
	 */
	@Resource
	private WorkPlanRepairActivityManager workPlanRepairActivityManager ;
	
	/**
	 *车辆提票业务类
	 */
	@Resource
	private FaultTicketManager faultTicketManager ;
	
	/**
	 *上车配件业务类
	 */
	@Resource
	private PartsFixRegisterManager partsFixRegisterManager ;
	
	/**
	 *下车配件业务类
	 */
	@Resource
	private PartsUnloadRegisterManager partsUnloadRegisterManager ;
	
	/**
	 * 车辆列检业务类
	 */
	@Resource
	private ZbglRdpPlanManager zbglRdpPlanManager ;
	
	/**
	 * 运用故障登记业务类
	 */
	@Resource
	private GztpManager gztpManager ;
	
	
    /**
     * <li>说明：查询机车履历
     * <li>创建人：张迪
     * <li>创建日期：2016-11-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 包装了实体类查询条件的对象
     * @return 分页查询列表
     * @throws BusinessException
     */
    @Override
    public Page<TrainRecord> findPageList(SearchEntity<TrainRecord> searchEntity) throws BusinessException{
        TrainRecord entity = searchEntity.getEntity();
        StringBuilder sb = new StringBuilder(" From TrainRecord Where 1=1");
        
        // 根据“车型”“车号”模糊匹配查询
        String trainNo = StringUtil.nvl(entity.getTrainNo(), "").trim();
        if (!StringUtil.isNullOrBlank(trainNo)) {
            sb.append(" And trainTypeShortName || trainNo Like '%").append(trainNo.toUpperCase()).append("%'");
        }
        
        // 客货类型 10 货车 20 客车
        String vehicleType = StringUtil.nvl(entity.getVehicleType(), "").trim();
        if (!StringUtil.isNullOrBlank(vehicleType)) {
            sb.append(" And vehicleType = '").append(vehicleType).append("'");
        }
        
        // 以“计划开始日期”进行升序排序
        sb.append(" Order By trainTypeShortName ");
        String hql = sb.toString();
        String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
        Page<TrainRecord> page = this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
      
        return page;
    }

    /**
     * <li>说明：获取机车检修履历
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-02
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX 车型
     * @param trainNo 车号
     * @param vehicleType 客货类型 10 货车 20 客车 30 柴油发电机组
     * @return
     * @throws BusinessException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> findWorkPlanTree(String trainTypeIDX, String trainNo,String vehicleType) {
    	// 总根目录
    	List<Map> result = new ArrayList<Map>();
    	Map map = null;
    	// 检修
		List<Map> childrenJx = new ArrayList<Map>();
		List<TrainWorkPlan> trainWorkPlans = trainWorkPlanManager.findTrainWorkPlanListByTrain(trainTypeIDX, trainNo, vehicleType);
		for (TrainWorkPlan plan : trainWorkPlans) {
            map = new LinkedHashMap();
            map.put("id", plan.getIdx());          //检修计划ID
            String displanName = plan.getBeginTime() != null ? DateUtil.date2String(DateUtil.yyyy_MM_dd, plan.getBeginTime()) : "" ;			   //节点显示名称
            displanName += " " + plan.getRepairClassName() + " " + plan.getRepairtimeName();
			map.put("text", "<span style=\"color:#3A5A82;\" title=\"" + displanName + "\">" + displanName + "</span>"); // menulabel 菜单显示名称
			map.put("leaf", true);
			List<Map> jxchildrenMaps = getJXChildMaps(plan.getIdx());
			if(jxchildrenMaps != null){
				map.put("iconCls", "");	
				map.put("leaf", false);
				map.put("children", jxchildrenMaps);  
			}
			childrenJx.add(map);
		}
		Map jxMap = new LinkedHashMap();
		jxMap.put("id", "jx");          //检修
		jxMap.put("leaf", false);
		jxMap.put("text", "检修("+trainWorkPlans.size()+")");
		jxMap.put("children", childrenJx);  
        result.add(jxMap);
        
		// 运用
        List<Map> childrenYY = new ArrayList<Map>();
        List<Map<String, Object>> zbglRdpPlanList = zbglRdpPlanManager.findZbglRdpListByRecord(trainTypeIDX, trainNo, vehicleType);
        for (Map<String, Object> plan : zbglRdpPlanList) {
            map = new LinkedHashMap();
            map.put("id", plan.get("rdpIdx")+"");          //检修计划ID
            String displanName = plan.get("planStartTime") + " " + plan.get("railwayTime") ;  //节点显示名称
			map.put("text", "<span style=\"color:#3A5A82;\" title=\"" + displanName + "\">" + displanName + "</span>"); // menulabel 菜单显示名称
			map.put("leaf", true);
			List<Map> yychildrenMaps = getYYChildMaps(plan.get("recordIdx")+"");
			if(yychildrenMaps != null){
				map.put("iconCls", "");	
				map.put("leaf", false);
				map.put("children", yychildrenMaps);  
			}
			childrenYY.add(map);
        }
        
		Map yyMap = new LinkedHashMap();
		yyMap.put("id", "yy");          //检修
		yyMap.put("leaf", false);
		yyMap.put("text", "10".equals(vehicleType) ? "作业写实("+zbglRdpPlanList.size()+")" : "库列检("+zbglRdpPlanList.size()+")");
		yyMap.put("children", childrenYY);  
        result.add(yyMap);
        
		return result;
	}
    
    /**
     * <li>说明：组装检修记录的数据
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-03
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param activities 检修记录列表
     * @return
     * @throws BusinessException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<Map> getJXChildMaps(String workPlanIdx){
    	// 查询机车检修记录
    	List<WorkPlanRepairActivity> activities = workPlanRepairActivityManager.findWorkPlanRepairActivityByPlan(workPlanIdx);
    	List<Map> children = new ArrayList<Map>();
    	Map map = null;
    	if(activities != null){
    		for (WorkPlanRepairActivity activity : activities) {
        		map = new LinkedHashMap();
        		map.put("id", activity.getIdx());          // 检修记录单主键
        		map.put("repairProjectIDX", activity.getRepairProjectIDX()); // 检修记录单报表模板
        		map.put("parentID", workPlanIdx);          // 检修计划主键
        		map.put("text", activity.getActivityName());          // 检修记录单主键
        		map.put("type", "10");					// 类型 10 检修记录 20故障记录 30  下车配件 40 上车配件
    			map.put("iconCls", "jsglIcon");		
    			map.put("leaf", true);
    			children.add(map);
    		}
    	}
    
    	// 查询机车提票信息
    	List<FaultTicket> faultTickets = faultTicketManager.getFaultTicketListByWorkPlanIDX(workPlanIdx);
		map = new LinkedHashMap();
		map.put("id", "20");          // 检修记录单主键
		map.put("parentID", workPlanIdx);          // 检修计划主键
		map.put("text", "车辆故障("+faultTickets.size()+")");          // 检修记录单主键
		map.put("type", "20");					// 类型 10 检修记录 20故障记录 30 下车配件 40 上车配件
		map.put("iconCls", "pjglIcon");		
		map.put("leaf", true);
		children.add(map);
    	
        // 配件下车
    	List<PartsUnloadRegister> registers = partsUnloadRegisterManager.findPartsUnloadRegisterByWorkPlanIdx(workPlanIdx);
		map = new LinkedHashMap();
		map.put("id", "30");          // 检修记录单主键
		map.put("parentID", workPlanIdx);          // 检修计划主键
		map.put("text", "下车配件清单("+registers.size()+")");          // 检修记录单主键
		map.put("type", "30");					// 类型 10 检修记录 20故障记录 30 下车配件 40 上车配件
		map.put("iconCls", "templatesIcon");		
		map.put("leaf", true);
		children.add(map);
    	// 配件上车
    	List<PartsFixRegister> fixregisters = partsFixRegisterManager.findPartsFixRegisterByWorkPlanIdx(workPlanIdx);
		map = new LinkedHashMap();
		map.put("id", "40");          // 检修记录单主键
		map.put("parentID", workPlanIdx);          // 检修计划主键
		map.put("text", "上车配件清单("+fixregisters.size()+")");          // 检修记录单主键
		map.put("type", "40");					// 类型 10 检修记录 20故障记录 30 下车配件  40 上车配件
		map.put("iconCls", "templatesIcon");		
		map.put("leaf", true);
		children.add(map);
    	return children; 
    }
    
    
    /**
     * <li>说明：组装运用记录的数据
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-03
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return
     * @throws BusinessException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<Map> getYYChildMaps(String recordIdx){
    	// 查询机车检修记录
    	List<Map> children = new ArrayList<Map>();
    	Map map = null;
		map = new LinkedHashMap();
		map.put("id", "50");          // 检修记录单主键
		map.put("parentID", recordIdx);          // 检修计划主键
		map.put("text", "作业情况");          // 检修记录单主键
		map.put("type", "50");					// 类型 10 检修记录 20故障记录 30  下车配件 40 上车配件 50 运用基本信息 60 运用故障信息
		map.put("iconCls", "templatesIcon");		
		map.put("leaf", true);
		children.add(map);
    
    	// 查询机车提票信息
		List<Gztp> zgtps = gztpManager.findGztpListByRecord(recordIdx);
		map = new LinkedHashMap();
		map.put("id", "60");          // 检修记录单主键
		map.put("parentID", recordIdx);          // 检修计划主键
		map.put("text", "车辆故障("+zgtps.size()+")");          // 检修记录单主键
		map.put("type", "60");					// 类型 10 检修记录 20故障记录 30 下车配件 40 上车配件 50 运用基本信息 60 运用故障信息
		map.put("iconCls", "pjglIcon");		
		map.put("leaf", true);
		children.add(map);
    	return children; 
    }
    

    
}
