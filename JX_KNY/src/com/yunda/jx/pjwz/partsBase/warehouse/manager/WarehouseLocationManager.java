package com.yunda.jx.pjwz.partsBase.warehouse.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjwz.partsBase.warehouse.entity.WarehouseLocation;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WarehouseLocation业务类,库位的基本信息表
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-08-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="warehouseLocationManager")
public class WarehouseLocationManager extends JXBaseManager<WarehouseLocation, WarehouseLocation>{
	
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
	 * <li>说明：新增修改保存前的实体对象前的验证业务,同一库房中的库位名称不能重复，唯一 库位编码唯一
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
	public String[] validateUpdate(WarehouseLocation entity) throws BusinessException {
		List<String> errMsg = new ArrayList<String>();
		WarehouseLocation ware = this.getModelById(entity.getIdx()); //获取当前对象在数据库中的值
		if("".equals(entity.getIdx()) || !ware.getLocationID().equals(entity.getLocationID())){ //新增时、或者库房编码变更时验证
			String hql = "Select count(1) From WarehouseLocation " +
					" where recordStatus = 0 and  locationID='"+entity.getLocationID()+"'";
			int count = this.daoUtils.getCount(hql);
			if(count > 0){
				errMsg.add("库位编码【"+ entity.getLocationID() +"】已存在！");
			}
		}
		if("".equals(entity.getIdx()) || !ware.getLocationName().equals(entity.getLocationName())){
			String hql = "Select count(1) From WarehouseLocation " +
				" where recordStatus = 0 " +
				" and wareHouseIDX ='"+entity.getWareHouseIDX()+"'" +
				" and  locationName='"+entity.getLocationName()+"'" ;
				int count = this.daoUtils.getCount(hql);
				if(count > 0){
					errMsg.add("库房名称【"+ entity.getLocationName() +"】在【"+entity.getWarehouseName()+"】库房下已存在！");
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
	 * <li>说明：分页查询库位
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-8-31
     * <li>修改人： 刘晓斌
     * <li>修改日期：2013-11-17
     * <li>修改内容：根据缓存开关来执行查询
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public Page<WarehouseLocation> findPageList(SearchEntity<WarehouseLocation> searchEntity, String status) throws BusinessException{
		String totalHql = "select count(*) from WarehouseLocation t where t.recordStatus=0 ";
		String hql = "From WarehouseLocation t where t.recordStatus=0 ";
		StringBuffer awhere =  new StringBuffer();
		if(!StringUtil.isNullOrBlank(status)){
			awhere.append(" and t.status in ("+status+")");
		}
		if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getWareHouseIDX())){
			awhere.append(" and t.wareHouseIDX = '"+searchEntity.getEntity().getWareHouseIDX()+"'");
		}
		if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getLocationID())){
			awhere.append(" and t.locationID like '%"+searchEntity.getEntity().getLocationID()+"%'");
		}
		if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getLocationName())){
			awhere.append(" and t.locationName like '%"+searchEntity.getEntity().getLocationName()+"%'");
		}
		if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getLocationAddress())){
			awhere.append(" and t.locationAddress like '%"+searchEntity.getEntity().getLocationAddress()+"%'");
		}
		awhere.append("order by t.updateTime DESC");
		totalHql += awhere;
		hql += awhere;
		if(enableCache()){
			return super.cachePageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
		} else {
			return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
		}
    }
	
	/**
	 * <li>说明：启用，作废状态修改
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-8-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param flag：操作标志（start表示启用）;ids：实体主键集合
	 * @return void
	 * @throws Exception
	 */
	public void updateStatus(String flag,Serializable... ids) throws BusinessException, NoSuchFieldException {
		List<WarehouseLocation> entityList = new ArrayList<WarehouseLocation>();
		for (Serializable id : ids) {
			WarehouseLocation ware = getModelById(id);
			ware = EntityUtil.setSysinfo(ware); //根据IDX主键设置创建人创建时间等基本信息
				ware.setStatus(WarehouseLocation.STATUS_NO);//未满
			entityList.add(ware);
		}
		this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
	}
	/**
	 * 
	 * <li>说明：查询库位下拉控件信息
	 * <li>创建人：程梅
	 * <li>创建日期：2013-10-28
     * <li>修改人： 刘晓斌
     * <li>修改日期：2013-11-17
     * <li>修改内容：根据缓存开关来执行查询
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
    public Map<String, Object> page(String queryValue,Map queryParams, int start, int limit, String queryHql) throws ClassNotFoundException {
        StringBuffer hql = new StringBuffer();
        // 拼接查询sql，如queryHql配置项不为空则直接执行queryHql
        if (!StringUtil.isNullOrBlank(queryHql)) {
            hql.append(queryHql);
        } else { 
        	 hql.append(" From WarehouseLocation where recordStatus=0 ");
             if (!StringUtil.isNullOrBlank(queryValue)) {
                hql.append(" and locationName like '").append(queryValue).append("%'"); //修改默认从首字开始查询匹配
            }
            //queryHql配置项为空则按queryParams配置项拼接Hql
            if (!queryParams.isEmpty()) {
                Set<Map.Entry<String, String>> set = queryParams.entrySet();
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (!StringUtil.isNullOrBlank(value)) {
                        if(value.startsWith("<>")){   //如果查询value值包含不等于符号则，采用不等于查询
                            hql.append(" and ").append(key).append(" <> '").append(value.substring(2)).append("'");
                        }else{
                            hql.append(" and ").append(key).append(" = '").append(value).append("'");
                        }
                    }
                }
            }
        }
        hql.append(" order by updateTime desc");
        //根据hql构造totalHql
//        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*)");
        totalHql.append(hql.toString());
        Page page = null;
        if(enableCache()){
        	page = super.cachePageList(totalHql.toString(), hql.toString(), start, limit);
        } else {
        	page = super.findPageList(totalHql.toString(), hql.toString(), start, limit);
        }
        return page.extjsStore();
    }
	
	/**
	 * 
	 * <li>说明：根据“库房主键”和“库位名称”查询“库位idx主键”
	 * <li>创建人：何涛
	 * <li>创建日期：2014-08-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
	 * @param wHouseId
	 * @param locationName
	 * @return
	 */
	public String getIdxByWHouseIDXAndLocationName(String wHouseId, String locationName) {
		if (null == wHouseId || null == locationName) {
			return null;
		}
		String hql = "From WarehouseLocation Where wareHouseIDX = ? And locationName = ?";
		List list = this.daoUtils.find(hql, new Object[]{wHouseId, locationName});
		if (null != list && 0 < list.size()) {
			return ((WarehouseLocation)list.get(0)).getIdx();
		}
		return null;
	}
	
}