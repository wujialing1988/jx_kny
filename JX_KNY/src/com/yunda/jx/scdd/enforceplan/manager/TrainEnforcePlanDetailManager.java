package com.yunda.jx.scdd.enforceplan.manager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.component.manager.OmOrganizationSelectManager;
import com.yunda.jx.jczl.attachmanage.entity.JczlTrain;
import com.yunda.jx.jczl.attachmanage.manager.JczlTrainManager;
import com.yunda.jx.jczl.undertakemanage.entity.UndertakeTrain;
import com.yunda.jx.jczl.undertakemanage.entity.UndertakeTrainType;
import com.yunda.jx.jczl.undertakemanage.manager.UndertakeTrainManager;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.scdd.enforceplan.entity.TrainEnforcePlan;
import com.yunda.jx.scdd.enforceplan.entity.TrainEnforcePlanDetail;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainEnforcePlanDetail业务类,机车施修计划明细
 * <li>创建人：程锐
 * <li>创建日期：2012-12-06
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="trainEnforcePlanDetailManager")
public class TrainEnforcePlanDetailManager extends JXBaseManager<TrainEnforcePlanDetail, TrainEnforcePlanDetail>{
	
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	/** 计划主表Manager */
	@Resource
	private TrainEnforcePlanManager trainEnforcePlanManager;
	
	/** 机车信息维护 */
	@Resource
	private JczlTrainManager jczlTrainManager;
	
	/** 机车承修业务类实体 */
	@Resource
	private UndertakeTrainManager undertakeTrainManager;

	/**
	 * <li>说明：删除实体对象前的验证业务
	 * <li>创建人：程锐
	 * <li>创建日期：2012-12-06
	 * <li>修改人： 谭诚
	 * <li>修改日期： 2013-5-4 
	 * <li>修改内容： 删除前的验证操作, 对于已经兑现或者完成的数据,不可以执行逻辑删除动作
	 * @param ids 实体对象的idx主键数组
	 * @return 返回删除操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */	
	@SuppressWarnings("unchecked")
    @Override
	public String[] validateDelete(Serializable... ids) throws BusinessException {
		List list = null;
		String[] message = null;
		try{
			for(int i = 0; i<ids.length; i++) {
				//逻辑删除操作前,需根据待删除信息Id,判断其是否已经处于兑现或者检修完成状态,这两种状态不允许修改计划内容
				String hql = SqlMapUtil.getSql("scdd:isExistNotAllowUpdateData");
				Object[] param = new Object[]{ids[i].toString(),                                   //计划明细主键
											  TrainEnforcePlanDetail.STATUS_REDEMPTION,     //已兑现
											  TrainEnforcePlanDetail.STATUS_COMPLETE};      //已完成
				list = daoUtils.getHibernateTemplate().find(hql,param);

				if(list!=null&&list.size()>0){
					String count = String.valueOf(list.get(0));
					if(Integer.parseInt(count)>0){ //如果存在相应的数据
						message = new String[]{"存在已兑现或检修完成的计划明细数据,不能执行删除操作!"};
						break;
					} 
				}
			}
		} catch (Exception e) {
			message = new String[]{"删除验证出错!"};
		}
		return message;
	}
	
	/**
	 * <li>说明：接受保存或更新计划明细记录请求，向客户端返回操作结果（JSON格式），实体类对象必须符合检修系统表设计，主键名必须为idx（字符串uuid）
	 * <li>     新增调用保存方法前,先验证是否在同一计划单下已存在相同车型和车号的计划明细信息, 如果存在则提示用户"该车已存在计划,请勿重复编制!"
	 * <li>     更新调用保存方法前,先验证该数据的状态(planStatus: 10,20,30)是否已经是兑现(20)或者完成(30)状态, 这两种状态不可以修改数据.
	 * <li>创建人：程锐
	 * <li>创建日期：2012-12-06
	 * <li>修改人：谭诚 
	 * <li>修改日期：2013-5-4
	 * <li>修改内容：修改验证方法,避免重复添加同车型\车号的数据,并且防止删除已经兑现或检修完成的计划数据
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@Override
	public String[] validateUpdate(TrainEnforcePlanDetail entity) throws BusinessException {
		String[] message = null;
		//参数实体未能成功获取
		if( entity==null ) { 
			message = new String[]{"参数无效!"};
			return message;
		}
		//根据IDX主键是否已经产生,判断是新增还是编辑操作 true-新增; false-编辑
		boolean isAdd = entity.getIdx()==null||"".equals(entity.getIdx())?true:false;
		if(isAdd){
			/**
			 * 新增操作需要作以下判断:
			 * 1. 在所有计划明细中,如存在与当前新增数据的车型ID\车号完全相同,且状态并非已完成的数据,则不允许新增计划明细
			 */
			message = this.getExistRepairPlanList(entity);
			if(message!=null){
				message[0] = "在<font style='font-weight:bold;'>\""+message[0]+"\"</font>中已存在相同的计划信息!";
			}
			return message;//返回与当前计划明细相同的明细数据所属的计划主表的计划名称
		} else {
			/**
			 * 修改操作需要作以下判断:
			 * 1. 修改的当前数据意外, 是否还存在与待存储数据相同车型和车号的数据,且状态并非已完成,不符合验证不允许保存
			 */
			message = this.getExistPlanDetailList(entity);
			if(message!=null){
				message[0] = "在<font style='font-weight:bold;'>\""+message[0]+"\"</font>中已存在相同的计划信息!";
			}
			return message;
		}
	}
    /**
     * 
     * <li>说明：分页查询，返回实体类的分页列表对象
     * <li>创建人：程锐
     * <li>创建日期：2012-12-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 机车施修计划明细对象包装类
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param orderString 排序字符串
     * @return Page 分页查询列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public Page findPageList(final SearchEntity<TrainEnforcePlanDetail> searchEntity, String startDate,
        String endDate, String orderString)  throws BusinessException {
    	
    	TrainEnforcePlanDetail trainEnforcePlanDetail = searchEntity.getEntity();
        StringBuilder selectSql = new StringBuilder();
        selectSql.append(SqlMapUtil.getSql("scdd:planDetailListForRdp-select"));
        StringBuilder fromSql = new StringBuilder();
        fromSql.append(SqlMapUtil.getSql("scdd:planDetailList-from"));
        
        
        if(!StringUtil.isNullOrBlank(trainEnforcePlanDetail.getTrainEnforcePlanIDX())){
        	fromSql.append(" and a.Train_Enforce_Plan_IDX = '").append(trainEnforcePlanDetail.getTrainEnforcePlanIDX()).append("'");
        }
        if(!StringUtil.isNullOrBlank(trainEnforcePlanDetail.getTrainTypeShortName())){
        	fromSql.append(" and a.TRAIN_TYPE_SHORTNAME like '%").append(trainEnforcePlanDetail.getTrainTypeShortName()).append(Constants.LIKE_PIPEI);
        }
        if(!StringUtil.isNullOrBlank(trainEnforcePlanDetail.getTrainNo())){
        	fromSql.append(" and a.TRAIN_NO like '%").append(trainEnforcePlanDetail.getTrainNo()).append(Constants.LIKE_PIPEI);
        }        
//      修程
        if(!StringUtil.isNullOrBlank(trainEnforcePlanDetail.getRepairClassName())){
        	fromSql.append(" and a.REPAIR_CLASS_NAME like '%").append(trainEnforcePlanDetail.getRepairClassName()).append(Constants.LIKE_PIPEI);
        }
        //修次
        if(!StringUtil.isNullOrBlank(trainEnforcePlanDetail.getRepairtimeName())){
        	fromSql.append(" and a.Repair_time_Name like '%").append(trainEnforcePlanDetail.getRepairtimeName()).append(Constants.LIKE_PIPEI);
        }
        //配属局 bShortName
        if(!StringUtil.isNullOrBlank(trainEnforcePlanDetail.getBName())){
        	fromSql.append(" and a.b_Name like '%").append(trainEnforcePlanDetail.getBName()).append(Constants.LIKE_PIPEI);
        }
        //配属段 dShortName
        if(!StringUtil.isNullOrBlank(trainEnforcePlanDetail.getDNAME())){
        	fromSql.append(" and a.d_NAME like '%").append(trainEnforcePlanDetail.getDNAME()).append(Constants.LIKE_PIPEI);
        }
        if (!StringUtil.isNullOrBlank(startDate)) {
        	fromSql.append(" and to_char(a.Real_Start_Date,'YYYY-MM-DD') >= '").append(startDate).append("'");
        }
        if (!StringUtil.isNullOrBlank(endDate)) {
        	fromSql.append(" and to_char(a.Real_End_Date,'YYYY-MM-DD') <= '").append(endDate).append("'");
        }
        if(!StringUtil.isNullOrBlank(String.valueOf(trainEnforcePlanDetail.getPlanStatus()))){
        	fromSql.append(" and a.Plan_Status = " + trainEnforcePlanDetail.getPlanStatus());
        }
        String querySql = selectSql.toString() + " " + fromSql.toString() + " " + orderString ;
        String totalSql = "select count(distinct(a.idx)) " + fromSql.toString();
        return findPageList(totalSql, querySql, searchEntity.getStart(), searchEntity.getLimit(), null, searchEntity.getOrders());
    }
    
    /**
     * <li>说明：根据计划主键获取计划明细
     * <li>创建人：程锐
     * <li>创建日期：2013-1-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param planIdx 计划主键
     * @return List<TrainEnforcePlanDetail> 计划明细列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<TrainEnforcePlanDetail> getDetailByPlan(Serializable planIdx) throws BusinessException {
        String hql = "from TrainEnforcePlanDetail where recordStatus = 0 and trainEnforcePlanIDX = '" + planIdx + "'";
        return daoUtils.find(hql);
    }
    
    /**
     * 
     * <li>说明：该函数被本类的validateUpdate()调用,通过查询是否存在与当前新增数据的车型ID\车号完全相同,且状态并非已完成的数据
     * <li>创建人：谭诚
     * <li>创建日期：2013-5-5
     * <li>修改人： 王斌
     * <li>修改日期：2014-7-1
     * <li>修改内容：增加配件检修计划主键查询条件
     * @param entity 计划明细实体
     * @return 返回计划明细实体列表
     */
    @SuppressWarnings("unchecked")
    private List <TrainEnforcePlanDetail> getExistRepairPlanDetail(TrainEnforcePlanDetail entity){
    	String hql = SqlMapUtil.getSql("scdd:isExistRepairPlanDetail");
		Object[] param = new Object[]{entity.getTrainTypeIDX(),//车型主键
				 					  entity.getTrainNo(),     //车号
				 					  TrainEnforcePlanDetail.STATUS_PLAN,	//处理状态:新编
				 					  TrainEnforcePlanDetail.STATUS_REDEMPTION,//处理状态:已兑现
				 					  entity.getTrainEnforcePlanIDX()};//机车施修计划
		List <TrainEnforcePlanDetail> list = daoUtils.getHibernateTemplate().find(hql,param);
		if(list == null||list.size()<1){
			return null;
		}
		return list;
    }
    
    /**
     * <li>说明：获取存在的机车计划明细
     * <li>创建人：谭诚
     * <li>创建日期：2013-5-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车计划明细实体
     * @return 提示信息
     */
    public String [] getExistRepairPlanList(TrainEnforcePlanDetail entity){
    	if(entity == null) return null; //如果参数为空,则直接返回
    	//调用本类函数, 获得与当前实体的车型\车号相同的计划明细实体(正常情况下应当只有最多一个)
    	List <TrainEnforcePlanDetail> planDetailEntity = this.getExistRepairPlanDetail(entity); 
    	if(planDetailEntity==null) return null; //planDetailEntity为空,则说明没有重复的计划明细数据,直接返回
    	List <TrainEnforcePlan> planEntity = this.trainEnforcePlanManager.getTrainEnforcePlanList(planDetailEntity.get(0).getTrainEnforcePlanIDX());
    	if(planEntity==null||planEntity.size()<1) return null; //planEntity为空,说明虽然存在重复的计划明细数据,但计划主表信息因为某种原因丢失了,直接返回
    	return new String[]{planEntity.get(0).getPlanName()};//因List正常情况下只会存在最多一条数据,所以返回索引0位置的计划名称以提示用户
    }
    
    /**
     * <li>说明：编辑保存时由验证方法调用,防止用户通过编辑修改功能保存与现有车号-车型同时相等的数据信息
     * <li>创建人：谭诚
     * <li>创建日期：2013-5-6
     * <li>修改人： 王斌
     * <li>修改日期：2014-6-30
     * <li>修改内容：增加机车施修计划主键查询条件，（同一施修计划中不能出现相同的车）
     * @param entity 机车计划明细实体
     * @return 提示信息
     */
    @SuppressWarnings("unchecked")
	public String [] getExistPlanDetailList(TrainEnforcePlanDetail entity){
    	if(entity == null) return null; //如果参数为空,则直接返回
    	//------------1. 首先在所有计划明细数据中查询是否存在满足条件的数据--------------
    	String hql = SqlMapUtil.getSql("scdd:getExistPlanDetailList");
		Object[] param = new Object[]{entity.getTrainTypeIDX(),//车型主键
				 					  entity.getTrainNo(),     //车号
				 					  entity.getIdx(),			//当前实体主键
				 					  TrainEnforcePlanDetail.STATUS_PLAN,	//处理状态:新编
									  TrainEnforcePlanDetail.STATUS_REDEMPTION,//处理状态:已兑现
									  entity.getTrainEnforcePlanIDX()};//机车施修计划主键
		List <TrainEnforcePlanDetail> list = daoUtils.getHibernateTemplate().find(hql,param);
		if(list == null || list.size()<1) return null; //如果没有查询到重复数据,则直接返回
		//------------2. 在计划主表中根据计划明细Id,找到其主表信息----------------------
		List <TrainEnforcePlan> planEntity = this.trainEnforcePlanManager.getTrainEnforcePlanList(list.get(0).getTrainEnforcePlanIDX());
		if(planEntity==null||planEntity.size()<1) return null; //planEntity为空,说明虽然存在重复的计划明细数据,但计划主表信息因为某种原因丢失了,直接返回
		return new String[]{planEntity.get(0).getPlanName()};//因List正常情况下只会存在最多一条数据,所以返回索引0位置的计划名称以提示用户;
    }    
    
    /**
     * <li>说明：由兑现单模块调用, 更新计划明细的状态(已兑现->已完成)
     * <li>创建人：谭诚
     * <li>创建日期：2013-5-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainEnforcePlanDetailIdx 计划明细Idx主键
     * @return 返回值说明 是否更新成功
     */
    public boolean updatePlanDetailStatus(String trainEnforcePlanDetailIdx){
    	boolean flag = false;
    	TrainEnforcePlanDetail t = getModelById(trainEnforcePlanDetailIdx);
    	if(t == null) {
    		return flag;
    	} else {
    		t.setPlanStatus(TrainEnforcePlanDetail.STATUS_COMPLETE);
    		this.daoUtils.getHibernateTemplate().saveOrUpdate(t);
    		flag = true;
    	}
    	return flag;
    }
    
    /**
     * <li>说明： 覆盖父类的保存/更新方法,加入新业务: 在保存计划明细时,同步更新机车信息维护表的数据,
     * <li>     如果当前计划中的车型\车号在机车信息维护表中不存在,则新增一条信息;
     * <li>     如果当前计划中的车型\车号在机车信息维护表中已经存在,则不增加/更新维护表数据
     * <li>创建人：谭诚
     * <li>创建日期：2013-5-11
     * <li>修改人： 张迪
     * <li>修改日期：
     * <li>修改内容：添加上次修程修次
     * @param t 计划明细实体
     * @throws BusinessException,NoSuchFieldException
     */
	public void saveOrUpdate(TrainEnforcePlanDetail t) throws BusinessException, NoSuchFieldException {
		if(t == null) return;
		
		//调用本类方法,对待添加进机车信息维护表中的数据进行验证,验证通过后执行新增操作
		this.saveJczlTrainService(t);
		
		//调用本类方法,对待添加进承修机车维护表中的数据进行验证,验证通过后执行新增操作
		this.saveUndertakeTrain(t);
        
		//调用本类方法,对待添加进承修机车维护表中的数据进行验证,验证通过后执行新增操作
		this.saveLastRepairClassTime(t);
		
		//调用本类方法,对已入厂待修机车的承修部门和工作号进行更新
//		this.saveTrainInFactory(t);
		/**
		 * 计划明细信息保存/更新
		 */
		t = EntityUtil.setSysinfo(t);
		//设置逻辑删除字段状态为未删除
		t = EntityUtil.setNoDelete(t);
		this.daoUtils.getHibernateTemplate().saveOrUpdate(t);
	}
	
	/**
	 * <li>说明：设置机车生产计划明细机车上次修程修次
	 * <li>创建人：张迪
	 * <li>创建日期：2016-10-31
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 机车生产计划明细
	 */
    @SuppressWarnings("unchecked")
	private void saveLastRepairClassTime(TrainEnforcePlanDetail entity) {
	    if(entity == null) return;
        
        //根据计划明细实体中的车型主键\车型名称\承修单位主键, 在承修机车类型表中找到对应承修机车的主键信息
        String hql = SqlMapUtil.getSql("scdd:getTrainWorkPlan");
        Object[] param = new Object[]{entity.getTrainTypeIDX(),// 车型主键
                                      entity.getTrainTypeShortName(),  // 车型简称
                                      entity.getTrainNo()};  // 车号
        List <TrainWorkPlan> list = daoUtils.getHibernateTemplate().find(hql,param);
        //如果没有找到匹配的数据,则直接返回
        if(list == null||list.size()<1) {
            return;
        }
        TrainWorkPlan trainWorkPlanEntity = (TrainWorkPlan)list.get(0);//获取符合查询条件的第一个机车兑现计划类型

        entity.setLastRepairClassIDX(trainWorkPlanEntity.getRepairClassIDX());      //设置上次修程主键
        entity.setLastRepairClassName(trainWorkPlanEntity.getRepairClassName());    //设置上次修程
        entity.setLastRepairtimeIDX(trainWorkPlanEntity.getRepairtimeIDX());         //设置上次修次主键
        entity.setLastRepairtimeName(trainWorkPlanEntity.getRepairtimeName());               //设置上次修次
    }

    /**
	 * <li>说明：根据计划明细数据,构建机车信息维护实体,继而进行数据验证,验证通过则向机车信息维护表中追加数据
	 * <li>		如未通过验证,则不更新机车信息维护业务表
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-5-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 计划明细实体
	 */
	@SuppressWarnings("unchecked")
    public void saveJczlTrainService(TrainEnforcePlanDetail t){
		if(t == null) return;

		JczlTrain jczlTrain = new JczlTrain();
		jczlTrain.setTrainTypeIDX(t.getTrainTypeIDX());//车型主键
		jczlTrain.setTrainTypeShortName(t.getTrainTypeShortName());//车型简称
		jczlTrain.setTrainNo(t.getTrainNo());	//车号
		jczlTrain.setBId(t.getBid());  //配属局
		jczlTrain.setDId(t.getDid()); //配属段
		jczlTrain.setUseDId(t.getUsedDId());//支配段Id
		
		/**
		 * 调用机车信息维护业务类的验证方法,如果通过验证,则向机车信息维护表中追加数据,
		 * 如未通过验证,则不更新机车信息维护业务表
		 */
		String [] errMsg = this.jczlTrainManager.validateUpdate(jczlTrain);
		//通过机车信息维护的验证方法,验证同车型和车号的数据是否已经存在,如果不存在(无错误消息),则执行新增操作
		if (errMsg == null || errMsg.length < 1) {
//			try {
//				this.jczlTrainManager.saveOrUpdateTransfer(jczlTrain);
//			} catch (BusinessException e) {
//				ExceptionUtil.process(e,logger);
//			} catch (NoSuchFieldException e) {
//				ExceptionUtil.process(e,logger);
//			} catch (IllegalAccessException e) {
//				ExceptionUtil.process(e,logger);
//			} catch (InvocationTargetException e) {
//				ExceptionUtil.process(e,logger);
//			}
		} 
		/*
		 * 如果同车型和车号的数据已经存在,那么通过查询,获取其实体,对比计划中选择的配属局和配属段这两个属性是否有更改,
		 * 如果存在更改,则更新实体对象并据此修改机车信息维护表中的数据 getTrainJCJXEntity
		 * 1. 车型主键Idx  2.车型简称  3.车号  4.资产状态(10使用中，20报废)  5.机车状态(10检修，20运用，30备用)
		 */
		else {
			String hql = SqlMapUtil.getSql("scdd:getTrainJCJXEntity");
			Object[] param = new Object[]{t.getTrainTypeIDX(),//车型主键
										  t.getTrainTypeShortName(),  //车型简称
										  t.getTrainNo(),//车号
										  JczlTrain.TRAIN_ASSET_STATE_USE, //资产状态-使用中
										  JczlTrain.TRAIN_STATE_USE}; //机车状态-运用
			List <JczlTrain> list = daoUtils.getHibernateTemplate().find(hql,param);
			if(list == null || list.size()<1){
				return;
			}
			jczlTrain = (JczlTrain)list.get(0);
			boolean flag = false;
			try {
				if (jczlTrain != null){
					if(!StringUtil.isNullOrBlank(t.getBid())){
						jczlTrain.setBId(t.getBid());//配属局
						flag = true;
					}
					if(!StringUtil.isNullOrBlank(t.getDid())){
						jczlTrain.setDId(t.getDid());//配属段
						flag = true;
					}
					if(!StringUtil.isNullOrBlank(t.getUsedDId())){
						jczlTrain.setUseDId(t.getUsedDId()); //支配单位
						flag = true;
					}
					
				}
			} catch (Exception ex){
				ex.printStackTrace();
				flag = false;
			}
			//通过机车信息维护的验证方法,验证同车型和车号的数据其配属局和配属段是否与计划不一致,如果不一致则执行更新操作
			if (flag) {
//				try {
//					this.jczlTrainManager.saveOrUpdateTransfer(jczlTrain);
//				} catch (BusinessException e) {
//					ExceptionUtil.process(e,logger);
//				} catch (NoSuchFieldException e) {
//					ExceptionUtil.process(e,logger);
//				} catch (IllegalAccessException e) {
//					ExceptionUtil.process(e,logger);
//				} catch (InvocationTargetException e) {
//					ExceptionUtil.process(e,logger);
//				}
			} 
		}
	}
	
	/**
	 * <li>说明：根据计划明细数据,构建承修机车信息维护实体,继而进行数据验证,验证通过则向承修机车信息维护表中追加数据
	 * <li>		如未通过验证,则不更新承修机车信息维护业务表
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-5-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 计划明细实体
	 */
	@SuppressWarnings("unchecked")
    public void saveUndertakeTrain(TrainEnforcePlanDetail entity){
		if(entity == null) return;
		
		//根据计划明细实体中的车型主键\车型名称\承修单位主键, 在承修机车类型表中找到对应承修机车的主键信息
		String hql = SqlMapUtil.getSql("scdd:getUndertakeTrainTypeIdx");
		Object[] param = new Object[]{entity.getTrainTypeIDX(),//车型主键
									  entity.getTrainTypeShortName(),  //车型简称
									  OmOrganizationSelectManager.getOrgByOrgcode(JXSystemProperties.OVERSEA_ORGCODE).getOrgid()};
		List <UndertakeTrainType> list = daoUtils.getHibernateTemplate().find(hql,param);
		//如果没有找到匹配的数据,则直接返回
		if(list == null||list.size()<1) {
			return;
		}
		UndertakeTrainType underTakeTrainTypeEntity = (UndertakeTrainType)list.get(0);//获取符合查询条件的第一个承修单位类型
		String underTakeTrainTypeIdx = underTakeTrainTypeEntity.getIdx();//承修单位类型主键idx
		
		//构建承修机车关联表实体
		UndertakeTrain underTakeTrainEntity = new UndertakeTrain();
		underTakeTrainEntity.setUndertakeTrainTypeIDX(underTakeTrainTypeIdx);//设置承修机车类型外键
		underTakeTrainEntity.setTrainTypeIDX(entity.getTrainTypeIDX());      //设置承修机车主键
		underTakeTrainEntity.setTrainTypeShortName(entity.getTrainTypeShortName());//设置承修机车简称
		underTakeTrainEntity.setTrainNo(entity.getTrainNo());				//设置车号
		
		//为重用机车承修模块的验证和保存方法,构建一个机车承修实体的数组
		UndertakeTrain [] underTakeTrainEntityAry = new UndertakeTrain[]{underTakeTrainEntity};
		String[] errMsg =  this.undertakeTrainManager.validateUpdate(underTakeTrainEntityAry);
		if (errMsg == null || errMsg.length < 1) {
            try {
				this.undertakeTrainManager.saveFromTrain(underTakeTrainEntityAry);
			} catch (BusinessException e) {
				ExceptionUtil.process(e,logger);
			} catch (NoSuchFieldException e) {
				ExceptionUtil.process(e,logger);
			}
		}
	}
    
    /**
     * <li>说明：生成作业计划时选择生产计划列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询实体包装类
     * @return 生产计划分页列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public Page findPageListForRdp(final SearchEntity<TrainEnforcePlanDetail> searchEntity)  throws BusinessException {
        TrainEnforcePlanDetail trainEnforcePlanDetail = searchEntity.getEntity();
        StringBuilder selectSql = new StringBuilder();
        selectSql.append(SqlMapUtil.getSql("scdd:planDetailListForRdp-select"));
        StringBuilder fromSql = new StringBuilder();
        fromSql.append(SqlMapUtil.getSql("scdd:planDetailListForRdp-from")
                                  .replace("#P_PLAN_STATUS#", String.valueOf(TrainEnforcePlan.STATUS_AUDITED))
                                  .replace("#STATUS_PLAN#", String.valueOf(TrainEnforcePlanDetail.STATUS_PLAN))
                                  .replace("#INITIALIZE#", TrainWorkPlan.STATUS_NEW)
                                  .replace("#ONGOING#", TrainWorkPlan.STATUS_HANDLING));
        
        if(!StringUtil.isNullOrBlank(trainEnforcePlanDetail.getTrainTypeShortName())){
            fromSql.append(" and a.TRAIN_TYPE_SHORTNAME like '%").append(trainEnforcePlanDetail.getTrainTypeShortName()).append(Constants.LIKE_PIPEI);
        }
        if(!StringUtil.isNullOrBlank(trainEnforcePlanDetail.getTrainNo())){
            fromSql.append(" and a.TRAIN_NO like '%").append(trainEnforcePlanDetail.getTrainNo()).append(Constants.LIKE_PIPEI);
        }
        // 客货类型
        if(!StringUtil.isNullOrBlank(trainEnforcePlanDetail.getVehicleType())){
            fromSql.append(" and a.T_VEHICLE_TYPE = '").append(trainEnforcePlanDetail.getVehicleType()).append("'");
        }
        String querySql = selectSql.toString() + " " + fromSql.toString() ;
        String totalSql = "select count(distinct(a.idx)) " + fromSql.toString();
        return findPageList(totalSql, querySql, searchEntity.getStart(), searchEntity.getLimit(), null, searchEntity.getOrders());
    }
    
    /**
     * <li>说明：更新生产计划明细的状态
     * <li>创建人：程锐
     * <li>创建日期：2015-7-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id 生产计划明细IDX
     * @param workPlanStatus 作业计划状态
     * @throws NoSuchFieldException
     */
    public void updatePlanDetialForRdp(Serializable id, String workPlanStatus) throws NoSuchFieldException{
        TrainEnforcePlanDetail t = getModelById(id);
        t = EntityUtil.setSysinfo(t);
        //设置计划明细状态为已兑现  
        if(TrainWorkPlan.STATUS_NEW.equals(workPlanStatus) || TrainWorkPlan.STATUS_HANDLING.equals(workPlanStatus)){
            t.setPlanStatus(TrainEnforcePlanDetail.STATUS_REDEMPTION);
        }
        //设置计划明细状态为已完成
        else if(TrainWorkPlan.STATUS_HANDLED.equals(workPlanStatus)){
            t.setPlanStatus(TrainEnforcePlanDetail.STATUS_COMPLETE);
        } else if(TrainWorkPlan.STATUS_NULLIFY.equals(workPlanStatus)){
            t.setPlanStatus(TrainEnforcePlanDetail.STATUS_PLAN);
        }
        this.daoUtils.getHibernateTemplate().saveOrUpdate(t);
    }
    
    /**
     * <li>方法说明：批量保存
     * <li>方法名：saveForBatch
     * @param entitys 实体
     * @throws BusinessException
     * @throws NoSuchFieldException
     * <li>创建人： 张凡
     * <li>创建日期：2015年11月5日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public void saveForBatch(TrainEnforcePlanDetail[] entitys) throws BusinessException, NoSuchFieldException{
        for(int i = 0; i < entitys.length; i++){
            if(validateUpdate(entitys[i]) == null){
                saveOrUpdate(entitys[i]);
            }
        }
    }
    
    /**
     * <li>方法说明：根据车型车号获取月计划详情
     * <li>方法名：getTrainEnforcePlanDetailListByTrain
     * @param trainTypeIdx 车型主键
     * @param trainNo 车号
     * @throws BusinessException
     * <li>创建人： 伍佳灵
     * <li>创建日期：2016年10月19日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    @SuppressWarnings("unchecked")
    public List<TrainEnforcePlanDetail> getTrainEnforcePlanDetailListByTrain(String trainTypeIdx,String trainNo) throws BusinessException{
        List<TrainEnforcePlanDetail> reuslt = null ;
        if(!StringUtil.isNullOrBlank(trainTypeIdx) && !StringUtil.isNullOrBlank(trainNo)){
            StringBuffer hql = new StringBuffer();
            hql.append(" From TrainEnforcePlanDetail  where recordStatus = 0 and trainTypeIDX = ? and trainNo = ? order by updateTime asc ");
            Object[] param = new Object[]{trainTypeIdx,trainNo};
            reuslt = daoUtils.getHibernateTemplate().find(hql.toString(),param);
        }
        return reuslt ;
    }
    
    /**
     * <li>说明：月计划兑现情况查看
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-11-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    public List<Map<String, Object>> findMonthRateStatistics(String year){
        String sql = SqlMapUtil.getSql("zb-tp:findMonthRateStatistics").replaceAll("#year#", year);
        return this.queryListMap(sql);
    }  
    
}