package com.yunda.jx.pjwz.partsBase.warehouse.manager;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.pjwz.partsBase.warehouse.entity.Warehouse;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：Warehouse业务类,库房基本信息
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-08-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="warehouseManager")
public class WarehouseManager extends JXBaseManager<Warehouse, Warehouse>{
    
	/**
	 * <li>说明：确定该业务类是否使用查询缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return true使用查询缓存，false不使用
	 */
	@Override
	protected boolean enableCache(){
		return true;
	}	
	/**
	 * <li>说明：1.同一个单位的库房名称不能重复，唯一; 库房编码不能重复，唯一；
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-08-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@Override
	public String[] validateUpdate(Warehouse entity) throws BusinessException {
		List<String> errMsg = new ArrayList<String>();
		Warehouse ware = this.getModelById(entity.getIdx()); //获取当前对象在数据库中的值
		if("".equals(entity.getIdx()) || !ware.getWareHouseID().equals(entity.getWareHouseID())){ //新增时、或者库房编码变更时验证
//			String hql = "Select count(1) From Warehouse " +
//					" where recordStatus = 0 and  wareHouseID='"+entity.getWareHouseID()+"'" +
//					" and orgID = '"+entity.getOrgID()+"'";
			StringBuilder hql = new StringBuilder()
				.append("Select count(*) From Warehouse where recordStatus = 0 and wareHouseID='")
				.append(entity.getWareHouseID())
				.append("' and orgID = '")
				.append(entity.getOrgID())
				.append("'");
			int count = this.daoUtils.getCount(hql.toString());
			if(count > 0){
				errMsg.add("库房编码【"+ entity.getWareHouseID() +"】已存在！");
			}
		}
		if("".equals(entity.getIdx()) || !ware.getWareHouseName().equals(entity.getWareHouseName())){
//			String hql = "Select count(1) From Warehouse " +
//				" where recordStatus = 0 " +
//				" and  wareHouseName='"+entity.getWareHouseName()+"'" +
//				" and orgID = '"+entity.getOrgID()+"'" +
//				" group by orgID";
			StringBuilder hql = new StringBuilder()
				.append("Select count(*) From Warehouse where recordStatus = 0 and wareHouseName='")
				.append(entity.getWareHouseName())
				.append("' and orgID = '")
				.append(entity.getOrgID())
				.append("' group by orgID");
			int count = this.daoUtils.getCount(hql.toString());
			if(count > 0){
				errMsg.add("库房名称【"+ entity.getWareHouseName() +"】在【"+entity.getOrgName()+"】单位下已存在！");
			}
		}
		if (errMsg.size() > 0) {
			String[] errArray = new String[errMsg.size()];
			errMsg.toArray(errArray);
			return errArray;
		}
		return null;
	}
	
	/**
	 * 
	 * <li>说明：分页查询库房
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-8-31
	 * <li>修改人： 刘晓斌
	 * <li>修改日期：2013-11-14
	 * <li>修改内容：使用查询缓存
	 * @param searchEntity：查询参数；status：业务状态；flag：查询标志 1表示查询确认入库单的库房；specificationModel：规格型号数组
	 * @return 分页列表
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Page findPageList(SearchEntity<Warehouse> searchEntity, 
			String status,String flag,String[] specificationModel) throws BusinessException{
		String totalHql = "select count(*) from Warehouse t where t.recordStatus=0 ";
		String hql = "From Warehouse t where t.recordStatus=0 ";
		StringBuffer awhere =  new StringBuffer();
		if(!StringUtil.isNullOrBlank(status)){
			awhere.append(" and t.status in ("+status+")");
		}
		if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getWareHouseID())){
			awhere.append(" and t.wareHouseID like '%"+searchEntity.getEntity().getWareHouseID()+"%'");
		}
		if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getWareHouseName())){
			awhere.append(" and t.wareHouseName like '%"+searchEntity.getEntity().getWareHouseName()+"%'");
		}
		if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getWareHouseAddress())){
			awhere.append(" and t.wareHouseAddress like '%"+searchEntity.getEntity().getWareHouseAddress()+"%'");
		}
		if("1".equals(flag) && specificationModel.length > 0 ){ //确认入库申请查询库房过滤(取交集)
//			String[] sm = specificationModel.split(","); //规格型号集合
			String[] sm = specificationModel; //前台页面选中的规格型号集合（有重复）
			List<String> list = new ArrayList<String>(); //保存不重复的规格型号集合
			for (int i = 0; i < sm.length; i++) {
				if(!list.contains(sm[i])){
					list.add(sm[i]);
				}
			}
			String sfmStr = "'0'" ; //组装之后的规格型号字符串
			if(list.size() > 0 ){
				for (int i = 0; i <list.size(); i++) {
					sfmStr = sfmStr + ",'" + list.get(i)+"'";
				}
			}
			StringBuffer tempHql = new StringBuffer(); //查询规格型号为当前登录人员所管理的库房对应的库存定额的规格型号
			tempHql.append("select s.warehouseIDX From StockQuota q ,StoreKeeper s");
			tempHql.append(" where s.warehouseIDX = q.warehouseIDX and s.recordStatus=0 and q.recordStatus=0 ");
			OmEmployee emp = SystemContext.getOmEmployee() ;
			if(emp != null){
				tempHql.append("and s.empID = '"+emp.getEmpid()+"'");
			}
			tempHql.append(" and q.specificationModel in ("+sfmStr+")") ; //查询出所有规格型号对应的管理库房的集合
			tempHql.append(" group by s.warehouseIDX ");   //分组查询出每个库房下管理的规格型号数量
			tempHql.append(" having count(s.warehouseIDX) >= "+list.size()+""); //通过查询条件中包含的list.size()规格型号数量去对应过滤出完全管理了当前查询的规格型号的库房
			List<String> lists = (List<String>)this.daoUtils.find(tempHql.toString()); //查询过滤出含有当前规格型号的库房的IDX，然后再放到库房中去重
			String warehouseIDXs = "'0'";
			if(lists.size()>0){
				for (int i = 0; i < lists.size(); i++) {
					warehouseIDXs = warehouseIDXs + ",'" + lists.get(i) +"'" ;
				}
			}
			awhere.append(" and t.idx in ("+warehouseIDXs+")");
			 
		}
		awhere.append(" order by t.updateTime DESC");
		totalHql += awhere;
		hql += awhere;
		if(enableCache()){
			return super.cachePageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
		} else {
			return super.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
		}
    }
	
	
	/**
	 * <li>说明：启用，作废状态修改
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-8-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param flag：操作标志（start表示启用）;ids实体主键集合
	 * @return void
	 * @throws Exception
	 */
	public void updateStatus(String flag,Serializable... ids) throws BusinessException, NoSuchFieldException {
		List<Warehouse> entityList = new ArrayList<Warehouse>();
		for (Serializable id : ids) {
			Warehouse ware = getModelById(id);
			ware = EntityUtil.setSysinfo(ware); //根据IDX主键设置创建人创建时间等基本信息
			if("start".equals(flag)){
				ware.setStatus(Warehouse.STATUS_USE);//启用
			}else{
				ware.setStatus(Warehouse.STATUS_INVALID);//作废
			}
			entityList.add(ware);
		}
		this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
	}
	
	/**
	 * 
	 * <li>说明：获取库房下拉组件数据的JSON字符串
	 * <li>创建人：程锐
	 * <li>创建日期：2012-9-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public String combolist() throws IllegalArgumentException, IllegalAccessException{
		String queryString = " from Warehouse where recordStatus = 0";		
		List<Warehouse> list = getDaoUtils().find(queryString);
		StringBuilder jsonStr = new StringBuilder();
		jsonStr.append("[");
		if(list != null && list.size() > 0)
		{
			for(Warehouse warehouse :list)
			{
				jsonStr.append("{");
				/*待扩展，遍历实体类所有字段
				 * for(Field field : storeKeeper.getClass().getDeclaredFields()){
					jsonStr.append(field.getName() + ":'").append("").append("',");					
				}*/
				jsonStr.append("wareHouseID:'").append(warehouse.getWareHouseID()).append("',")
						.append("wareHouseName:'").append(warehouse.getWareHouseName()).append("',")
						.append("wareHouseAddress:'").append(warehouse.getWareHouseAddress()).append("',")
						.append("orgID:'").append(warehouse.getOrgID()).append("',")
						.append("orgCode:'").append(warehouse.getOrgCode()).append("',")
						.append("orgName:'").append(warehouse.getOrgName()).append("',");
				
				jsonStr.deleteCharAt(jsonStr.length()-1);
				jsonStr.append("},");										
			}
			jsonStr.deleteCharAt(jsonStr.length()-1);
		}
		
		jsonStr.append("]");
		return jsonStr.toString();
	}
	
	/**
	 * 
	 * <li>说明：获取所有状态为启用的库房记录
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-5-28
	 * <li>修改人： 刘晓斌
	 * <li>修改日期：2013-11-14
	 * <li>修改内容：使用查询缓存
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public static List<Warehouse> getAllStart(){
		Warehouse w = new Warehouse();
		w.setStatus(Warehouse.STATUS_USE);
		WarehouseManager manager = (WarehouseManager)ContextLoader.getCurrentWebApplicationContext().getBean("warehouseManager");
		return manager.findList(w);
	}
	/**
	 * 
	 * <li>说明：获取所有状态为启用的库房记录的JSON字符串
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-5-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public static String getAllStartJSON(){
		String json = "[]";
		try {
			json = JSONUtil.write(getAllStart());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}
	
}