package com.yunda.jx.pjwz.turnover.manager;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jcbm.jcpjzd.manager.JcpjzdBuildManager;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.manager.JobProcessNodeManager;
import com.yunda.jx.pjwz.fixparts.entity.PartsFixRegister;
import com.yunda.jx.pjwz.fixparts.manager.PartsFixRegisterManager;
import com.yunda.jx.pjwz.partsBase.outsourcinglist.entity.PartsOutsourcingCatalog;
import com.yunda.jx.pjwz.partsBase.outsourcinglist.manager.PartsOutsourcingCatalogManager;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.entity.PartsOutsourcing;
import com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.manager.PartsOutsourcingManager;
import com.yunda.jx.pjwz.turnover.entity.OffPartList;
import com.yunda.jx.pjwz.turnover.entity.PartsFixUnloadBean;
import com.yunda.jx.pjwz.turnover.entity.PartsZzjh;
import com.yunda.jx.pjwz.unloadparts.entity.PartsUnloadRegister;
import com.yunda.jx.pjwz.unloadparts.manager.PartsUnloadRegisterManager;

/**
 * 
 * <li>标题: PartsZzjhManager.java
 * <li>说明: 类的功能描述
 * <li>创建人：曾雪
 * <li>创建日期：2016-7-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="partsZzjhManager")
public class PartsZzjhManager extends JXBaseManager<PartsZzjh, PartsZzjh> implements IbaseCombo{

	//配件周转台账
	public PartsAccountManager partsAccountManager;
	
	//下车配件登记单
	public PartsUnloadRegisterManager partsUnloadRegisterManager;
	
	//上车配件登记单
	public PartsFixRegisterManager partsFixRegisterManager;
	
	public JcpjzdBuildManager  jcpjzdBuildManager;
	
	public PartsOutsourcingCatalogManager partsOutsourcingCatalogManager;
	
	public PartsOutsourcingManager partsOutsourcingManager;
	
	private OffPartListManager offPartListManager;
	
	private JobProcessNodeManager jobProcessNodeManager;
	
	public String partsAccountIdx = "";
	

	/**
	 * 
	 * <li>说明：生成周转计划
	 * <li>创建人：曾雪
	 * <li>创建日期：2016-7-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids:兑现单ID
	 * @param repairClassName：修程
	 * @param repairtimeName：修次
	 * @param workPlanIdx
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void saveOffPartList(String[] ids,String repairClassName,String repairtimeName,String workPlanIdx) throws BusinessException, NoSuchFieldException{
//		List<OffPartList> OffPartList = new ArrayList<OffPartList>();
		List<PartsZzjh> partsZzjhList = new ArrayList<PartsZzjh>();
			for (String id : ids) {
				OffPartList offPart = offPartListManager.getModelById(id);	             
				PartsZzjh zzjh = new PartsZzjh();
				zzjh.setPartsName(offPart.getPartsName());
				zzjh.setPartsIdx(offPart.getPartsIDX());
				zzjh.setTrainno(offPart.getTrainNo());
				zzjh.setWzmc(offPart.getWzmc());
				zzjh.setWzdm(offPart.getWzdm());
				zzjh.setTraintype(offPart.getTrainTypeShortName());
				zzjh.setRepairclass(repairClassName);
				zzjh.setRepairtime(repairtimeName);
				zzjh.setOffPartsListIdx(offPart.getIdx());
				zzjh.setWorkPlanIdx(workPlanIdx);
                partsZzjhList.add(zzjh);
			}
			this.saveOrUpdate(partsZzjhList);
	}
	
	/**
	 * 
	 * <li>说明：更新上下车计划时间
	 * <li>创建人：曾雪
	 * <li>创建日期：2016-7-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 配件周围实体
	 * @throws ParseException 
	 */
	public void updatePlanTime(PartsZzjh entity) throws ParseException{
        if(null == entity.getOffPartsListIdx()){
            throw new BusinessException("请手动填写计划时间");
        }
		OffPartList off = offPartListManager.getModelById(entity.getOffPartsListIdx());
		if(off.getOffNodeCaseIDX() != null && !"".equals(off.getOffNodeCaseIDX())){
			JobProcessNode offNode = jobProcessNodeManager.getModelById(off.getOffNodeCaseIDX());
            entity.setJhxcsj(offNode.getPlanBeginTime());
		}
		if(off.getOnNodeCaseIDX() != null && !"".equals(off.getOnNodeCaseIDX())){
			JobProcessNode onNode = jobProcessNodeManager.getModelById(off.getOnNodeCaseIDX());
            entity.setJhscsj(onNode.getPlanBeginTime());
		}
        if(entity.getJhscsj() != null && !"".equals(entity.getJhscsj())){
            updatePlanSendTime(entity); //计划送出日期
            updatePlanArrTime (entity); //计划到段日期
         }
	}
    
    
	/**
     * 
     * <li>说明：获取在修机车，返回显示字段为车型车号修程修次
     * <li>创建人：曾雪
     * <li>创建日期：2016-7-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
	@Override
    @SuppressWarnings({ "unchecked", "deprecation" })
    public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit) throws Exception {
    	Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		String totalHql = "";		
		hql = "from TrainWorkPlan where workPlanStatus = 'ONGOING'";
		totalHql = " select count (*) " + hql;
		Page page = this.findPageList(totalHql, hql, start, limit);
		List<TrainWorkPlan> PartsZzjhlist = new ArrayList<TrainWorkPlan>();
		PartsZzjhlist = page.getList();
		for (TrainWorkPlan plan : PartsZzjhlist) {
			plan.setTrainTypeShortName(plan.getTrainTypeShortName() + "|" + plan.getTrainNo() + " " +plan.getRepairClassName() + plan.getRepairtimeName());
		}
		page.setList(PartsZzjhlist);
		map = page.extjsStore();
		return map;
	}
	
	/**
	 * 
	 * <li>说明：更新实际时间，上下车时间，送出时间
	 * <li>创建人：曾雪
	 * <li>创建日期：2016-7-18
	 * <li>修改人： 张迪
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws ParseException 
	 * @throws NoSuchFieldException 
	 */
	public void UpdateRealTime(String[] ids) throws BusinessException, ParseException, NoSuchFieldException{ 
        if(null == ids){
            throw new BusinessException("数据异常，请重试!");
        }
        List<PartsZzjh> partsZzjhList = new ArrayList<PartsZzjh>();
        for (String id : ids) {
            PartsZzjh partsZzjh= this.getModelById(id);  
            if(null == partsZzjh) break;
            // 更新实际下车时间
            if(partsZzjh.getSjxcsj() == null || "".equals(partsZzjh.getSjxcsj())){
                updateRealUnloadTime(partsZzjh);
            }           
            // 更新实际送出时间
            if(partsZzjh.getSjscrq() == null || "".equals(partsZzjh.getSjscrq())){
                updateRealSendTime(partsZzjh);//实际送出日期
            }
            // 更新实际送到段时间
            if(partsZzjh.getSjddrq() == null || "".equals(partsZzjh.getSjddrq())){
                updateRealArrTime(partsZzjh);//实际到段日期
            }
            // 更新实际上车时间
            if(null == partsZzjh.getSjscsj() || "".equals(partsZzjh.getSjscsj())){
                updateRealFixTime(partsZzjh);
            }      
            partsZzjhList.add(partsZzjh);
        }
       super.saveOrUpdate(partsZzjhList);
	}
      /**
     * <li>说明：更新实际下车时间
     * <li>创建人：张迪
     * <li>创建日期：2016-8-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsZzjh 大部件周转实体
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public void updateRealUnloadTime (PartsZzjh partsZzjh) throws ParseException{
        StringBuilder sqlforunload = new StringBuilder();//下车
        String sqlforspecification = "select tt.specification_model from pjwz_parts_type tt where tt.jcpjbm = '" + partsZzjh.getPartsIdx()+"'";
        sqlforunload.append("select t.* from pjwz_parts_unload_register t where t.specification_model in (" + sqlforspecification + ")");
    
        //下车
        if( partsZzjh.getWzdm() != null && !"".equals(partsZzjh.getWzdm())){
            sqlforunload.append(" and t.unload_place = '" + partsZzjh.getWzmc() + "'");
        }else{
            sqlforunload.append(" and t.unload_place is null ");
        }
        if( partsZzjh.getWorkPlanIdx() != null && !"".equals(partsZzjh.getWorkPlanIdx())){
            sqlforunload.append(" and t.rdp_idx = '" + partsZzjh.getWorkPlanIdx() + "'");
        }else{
            sqlforunload.append(" and t.rdp_idx is null ");
        }   
        sqlforunload.append(" order by t.CREATE_TIME desc ");
        List<PartsUnloadRegister> list = partsUnloadRegisterManager.getDaoUtils().executeSqlQueryEntity(sqlforunload.toString(),PartsUnloadRegister.class);//下车
             
        //下车日期和下车配件编码
        if(null!=list && list.size() > 0){
            PartsUnloadRegister register = list.get(0);
            PartsAccount partsAccount = partsAccountManager.getModelById(register.getPartsAccountIDX());
//            partsAccountIdx = register.getPartsAccountIDX();
            partsZzjh.setSjxcsj(partsAccount.getUnloadDate());
            partsZzjh.setXcpjbh(partsAccount.getPartsNo());
            partsZzjh.setXcPartsAccountIdx(partsAccount.getIdx());
        }               
     }
    /**
     * <li>说明：更新实际上车时间
     * <li>创建人：张迪
     * <li>创建日期：2016-8-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsZzjh 大部件周转实体
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public void updateRealFixTime (PartsZzjh partsZzjh) throws ParseException{
        StringBuilder sqlforfixload = new StringBuilder();
        String sqlforspecification = "select tt.specification_model from pjwz_parts_type tt where tt.jcpjbm = '" +partsZzjh.getPartsIdx()+"'";
        sqlforfixload.append("select t.* from PJWZ_Parts_FIX_Register t where t.RECORD_STATUS = 0 and t.specification_model in (" + sqlforspecification + ")");
               
        //上车
        if( partsZzjh.getWzdm() != null && !"".equals(partsZzjh.getWzdm())){
            sqlforfixload.append(" and t.ABOARD_Place = '" + partsZzjh.getWzmc() + "'");
        }else{
            sqlforfixload.append(" and t.ABOARD_Place is null ");
        }
        if( partsZzjh.getWorkPlanIdx() != null && !"".equals(partsZzjh.getWorkPlanIdx())){
            sqlforfixload.append(" and t.rdp_idx = '" + partsZzjh.getWorkPlanIdx() + "'");
        }else{
            sqlforfixload.append(" and t.rdp_idx is null ");
        }   
        sqlforfixload.append(" order by ABOARD_DATE desc ");
        List<PartsFixRegister> partsFixRegisterlist = partsFixRegisterManager.getDaoUtils().executeSqlQueryEntity(sqlforfixload.toString(),PartsFixRegister.class);//上车             
         //上车日期和上车配件编码
         if(null != partsFixRegisterlist && partsFixRegisterlist.size() > 0){
            PartsFixRegister register = partsFixRegisterlist.get(0);
            PartsAccount partsAccount = partsAccountManager.getModelById(register.getPartsAccountIDX());
            partsZzjh.setSjscsj(partsAccount.getAboardDate());
            partsZzjh.setScpjbm(partsAccount.getPartsNo());
            partsZzjh.setScPartsAccountIdx(partsAccount.getIdx());
        }            
     }
  
	/**
	 * 
	 * <li>说明：更新计划送出时间
	 * <li>创建人：曾雪
	 * <li>创建日期：2016-7-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param o
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
    public void updatePlanSendTime (PartsZzjh o) throws ParseException{
//		JcpjzdBuild jcpjzdBuild = jcpjzdBuildManager.getModelById(o.getPartsIdx());
//		if(jcpjzdBuild.getJcpjmc() != null && !"".equals(jcpjzdBuild.getJcpjmc())){
			String sql = "select t.* from PJWZ_Parts_Outsourcing_Catalog t where t.jcpjbm = '" + o.getPartsIdx() + "'";
			List<PartsOutsourcingCatalog> list =  partsOutsourcingCatalogManager.getDaoUtils().executeSqlQueryEntity(sql,PartsOutsourcingCatalog.class); 
			if(null !=list && list.size() > 0){
				PartsOutsourcingCatalog partsOutsourcingCatalog = list.get(0);
				Calendar cal = Calendar.getInstance();
				cal.setTime(o.getJhxcsj());
				cal.add(Calendar.HOUR, (null == partsOutsourcingCatalog.getOutCycle())?0:partsOutsourcingCatalog.getOutCycle().intValue());
				o.setJhscrq(cal.getTime());
//			}
		}
	}
	
	/**
	 * 
	 * <li>说明：更新实际送出时间
	 * <li>创建人：曾雪
	 * <li>创建日期：2016-7-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param o
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
    public void updateRealSendTime (PartsZzjh o) throws ParseException{
//		String sqlforjcpjbm = "select jcpj.jcpjbm from t_jcbm_jcpjzd jcpj where jcpj.co_id  = '" + o.getPartsIdx() + "'";
		String sqlforspecification = "select tt.specification_model from pjwz_parts_type tt where tt.jcpjbm = '" +o.getPartsIdx()+"'";    
			StringBuilder sql = new StringBuilder(); 
			sql.append("select t.* from PJWZ_PARTS_OUTSOURCING t where SPECIFICATION_MODEL in (" + sqlforspecification + ")" );
//			if(!StringUtil.isNullOrBlank(partsAccountIdx) && partsAccountIdx != null){
				sql.append(" and PARTS_ACCOUNT_IDX = '" + o.getXcPartsAccountIdx() + "'");
//			}
			List<PartsOutsourcing> list =  partsOutsourcingManager.getDaoUtils().executeSqlQueryEntity(sql.toString(),PartsOutsourcing.class); 
			if(null!=list && list.size() > 0){
				PartsOutsourcing partsOutsourcing = list.get(0);
				o.setSjscrq(partsOutsourcing.getOutsourcingDate());
				o.setZrbm(partsOutsourcing.getOutsourcingFactory());
			}
		
	}
	
	/**
	 * 
	 * <li>说明：更新实际到段日期
	 * <li>创建人：曾雪
	 * <li>创建日期：2016-7-19
	 * <li>修改人： 张迪
	 * <li>修改日期：2016-8-13
	 * <li>修改内容：
	 * @param o
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
    public void updateRealArrTime (PartsZzjh o) throws ParseException{
//		String sqlforjcpjbm = "select jcpj.jcpjbm from t_jcbm_jcpjzd jcpj where jcpj.co_id  = '" + o.getPartsIdx() + "'";
		String sqlforspecification = "select tt.specification_model from pjwz_parts_type tt where tt.jcpjbm ='" +o.getPartsIdx()+"'";
//		JcpjzdBuild jcpjzdBuild = jcpjzdBuildManager.getModelById(o.getPartsIdx());
//		if(jcpjzdBuild.getJcpjbm() != null && !"".equals(jcpjzdBuild.getJcpjbm())){
			StringBuilder sql = new StringBuilder();
			sql.append("select t.* from PJWZ_PARTS_OUTSOURCING t where SPECIFICATION_MODEL in (" + sqlforspecification + ")") ;
//			if(partsAccountIdx != null &&!"".equals(partsAccountIdx)){
				sql.append(" and t.PARTS_ACCOUNT_IDX = '" + o.getXcPartsAccountIdx() + "'");
//			}
			List<PartsOutsourcing> list =  partsOutsourcingManager.getDaoUtils().executeSqlQueryEntity(sql.toString(),PartsOutsourcing.class); 
			if(null!=list && list.size() > 0){
				PartsOutsourcing partsOutsourcing = list.get(0);
				o.setSjddrq(partsOutsourcing.getBackDate());
			}
//		}
	}
	
	/**
	 * 
	 * <li>说明：更新计划到段时间
	 * <li>创建人：曾雪
	 * <li>创建日期：2016-7-18
	 * <li>修改人： 张迪
     * <li>修改日期：2016-8-13
	 * <li>修改内容：
	 * @param o
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
    public void updatePlanArrTime (PartsZzjh o) throws ParseException{
			String sql = "select t.* from PJWZ_Parts_Outsourcing_Catalog t where t.jcpjbm = '" + o.getPartsIdx() + "'";
			List<PartsOutsourcingCatalog> list =  partsOutsourcingCatalogManager.getDaoUtils().executeSqlQueryEntity(sql,PartsOutsourcingCatalog.class); 
			if(null!=list && list.size() > 0){
				PartsOutsourcingCatalog partsOutsourcingCatalog = list.get(0);
				Calendar cal = Calendar.getInstance();
				cal.setTime(o.getJhscrq());
				cal.add(Calendar.HOUR, (null == partsOutsourcingCatalog.getRepairCycle())?0:partsOutsourcingCatalog.getRepairCycle().intValue());
				o.setJhddrq(cal.getTime());
			}
	}

    /**
     * <li>说明：查询配件上下车列表
     * <li>创建人：张迪
     * <li>创建日期：2016-10-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询封装实体
     * @return 配件上下车分页集合
     */
    public Page<PartsFixUnloadBean> findFixUnloadList(SearchEntity<PartsFixUnloadBean> searchEntity) {
        PartsFixUnloadBean entity = searchEntity.getEntity();
        StringBuffer sb = new StringBuffer();
        sb.append("Select * From PJWZ_PARTS_FIX_UPLOAD_VIEW where record_status = 0 ");
        if(!StringUtil.isNullOrBlank(entity.getWorkPlanIDX())){
            sb.append(" and Work_Plan_IDX = '").append(entity.getWorkPlanIDX()).append("'");
        }
        String sql = sb.toString();
        String totalSql = "Select count(*) as rowcount " + sql.substring(sql.indexOf("From"));
      
        return  this.queryPageList(totalSql, sql , searchEntity.getStart(), searchEntity.getLimit(), false, PartsFixUnloadBean.class); 
    }
    
	
	public JcpjzdBuildManager getJcpjzdBuildManager() {
		return jcpjzdBuildManager;
	}

	public void setJcpjzdBuildManager(JcpjzdBuildManager jcpjzdBuildManager) {
		this.jcpjzdBuildManager = jcpjzdBuildManager;
	}

	public PartsAccountManager getPartsAccountManager() {
		return partsAccountManager;
	}

	public void setPartsAccountManager(PartsAccountManager partsAccountManager) {
		this.partsAccountManager = partsAccountManager;
	}

	
	public PartsFixRegisterManager getPartsFixRegisterManager() {
		return partsFixRegisterManager;
	}

	public void setPartsFixRegisterManager(
			PartsFixRegisterManager partsFixRegisterManager) {
		this.partsFixRegisterManager = partsFixRegisterManager;
	}

	public PartsUnloadRegisterManager getPartsUnloadRegisterManager() {
		return partsUnloadRegisterManager;
	}

	public void setPartsUnloadRegisterManager(
			PartsUnloadRegisterManager partsUnloadRegisterManager) {
		this.partsUnloadRegisterManager = partsUnloadRegisterManager;
	}

	public PartsOutsourcingCatalogManager getPartsOutsourcingCatalogManager() {
		return partsOutsourcingCatalogManager;
	}

	public void setPartsOutsourcingCatalogManager(
			PartsOutsourcingCatalogManager partsOutsourcingCatalogManager) {
		this.partsOutsourcingCatalogManager = partsOutsourcingCatalogManager;
	}

	public PartsOutsourcingManager getPartsOutsourcingManager() {
		return partsOutsourcingManager;
	}

	public void setPartsOutsourcingManager(
			PartsOutsourcingManager partsOutsourcingManager) {
		this.partsOutsourcingManager = partsOutsourcingManager;
	}

	public OffPartListManager getOffPartListManager() {
		return offPartListManager;
	}

	public void setOffPartListManager(OffPartListManager offPartListManager) {
		this.offPartListManager = offPartListManager;
	}

	public JobProcessNodeManager getJobProcessNodeManager() {
		return jobProcessNodeManager;
	}

	public void setJobProcessNodeManager(JobProcessNodeManager jobProcessNodeManager) {
		this.jobProcessNodeManager = jobProcessNodeManager;
	}
	
}
