package com.yunda.jx.pjwz.partsBase.exchangeboundary.manager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.pjwz.partsBase.exchangeboundary.entity.TrainTypeToParts;
import com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType;
import com.yunda.jx.pjwz.partsBase.partstype.manager.PartsTypeManager;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainTypeToParts业务类,车型对应规格型号
 * <li>创建人：王治龙
 * <li>创建日期：2013-05-23
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="trainTypeToPartsManager")
public class TrainTypeToPartsManager extends JXBaseManager<TrainTypeToParts, TrainTypeToParts>{
	/**配件规格型号业务类*/
	private PartsTypeManager partsTypeManager ;
	
	
	/**
	 * 
	 * <li>说明：配件互换维护页面加载时调用
	 * <li>创建人：王斌
	 * <li>创建日期：2014-5-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public Page<TrainTypeToParts> findPageListForTrain(List<Condition> whereList,List<Order>  orderList,Integer start,Integer limit) throws BusinessException{
		StringBuffer searchParam=new StringBuffer();
		if(whereList!=null && whereList.size()>0){
			for (Condition condition:whereList) {
				if(!StringUtil.isNullOrBlank(condition.getPropName())&& "trainTypeIDX".equals(condition.getPropName())){
					searchParam.append(" and ");
					searchParam.append(" t.trainTypeIDX ").append(" = '"+condition.getPropValue()+"'");
				}else if(!StringUtil.isNullOrBlank(condition.getPropName())){
					searchParam.append(" and ");
					searchParam.append(" p.").append(condition.getPropName());
					searchParam.append(" like ").append("'%"+condition.getPropValue()+"%'");
				}
			}
		}
		if(orderList!=null && orderList.size()>0){
			searchParam.append(" order by ");
			for(Order order:orderList){
				if(!StringUtil.isNullOrBlank(order.toString())){
					if((order.toString()).indexOf("standardQty")!=-1){
						searchParam.append(" t.").append(order);
					}else{
						searchParam.append(" p.").append(order);
					}
				}
			}
		}
		String form_sql="FROM TrainTypeToParts t,PartsType p where t.partsTypeIDX=p.idx and t.recordStatus=0 and p.recordStatus=0 ";
		String hql=" select new TrainTypeToParts( t.idx,  t.trainTypeIDX,  t.trainTypeShortName,  t.partsTypeIDX, " +
		"t.standardQty,  p.matCode,  p.partsName,  p.specificationModel,  p.specificationModelCode,  p.unit) "+form_sql;
		
		String total_hql="select count(*) "+form_sql;
		return super.findPageList(total_hql.concat(searchParam.toString()), hql.concat(searchParam.toString()), start, limit);
	}
	/**
	 * 
	 * <li>说明：配件互换配件维护页面对应修程配件选择窗口加载时调用
	 * <li>创建人：王斌
	 * <li>创建日期：2014-5-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public Page<TrainTypeToParts> findPartsTypeForRcClass(List<Condition> whereList,List<Order>  orderList,Integer start,Integer limit) throws BusinessException{
		StringBuffer searchParam=new StringBuffer();
		if(whereList!=null && whereList.size()>0){
			for(Condition condtion:whereList){
				if(!StringUtil.isNullOrBlank(condtion.getSql())){
					searchParam.append(" and ").append(condtion.getSql());
				}
				if(!StringUtil.isNullOrBlank(condtion.getPropName())){
					searchParam.append(" and ").append(condtion.getPropName());
					searchParam.append(" like ").append("'%"+condtion.getPropValue()+"%'");
				}
			}
		}
		String form_sql=" from pjwz_traintype_to_parts_type t,pjwz_parts_type p where t.parts_type_idx=p.idx and t.record_status=0 and p.record_status=0";
		String sql="select t.idx idx, t.train_type_idx trainTypeIDX,t.train_type_shortname trainTypeShortName," +
					"t.standard_qty  standardQty, t.parts_type_idx partsTypeIDX,"+
					"p.mat_code matCode,p.parts_name partsName,p.specification_model specificationModel," +
					"p.specification_model_code specificationModelCode,p.unit unit "+form_sql;
		String sql2="select idx,trainTypeIDX,trainTypeShortName,partsTypeIDX,matCode,partsName,specificationModel,specificationModelCode,unit from ("+sql+") where 1=1 ";
		sql2=sql2.concat(searchParam.toString());
		
		System.out.println("======================\r\n"+sql2);
		String total_sql="select count(*) from ("+sql2+")";
		
		return super.findPageList(total_sql, sql2, start, limit, null, null);
	}
	
	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务
	 * <li>创建人：王治龙
	 * <li>创建日期：2013-5-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */
	public String[] validateUpdate(TrainTypeToParts[] entityList) throws BusinessException {
		List<String> errMsg = new ArrayList<String>();
        for(TrainTypeToParts entity : entityList){
        	PartsType partsType = this.partsTypeManager.getModelById(entity.getPartsTypeIDX());
        	if(partsType != null){
        		if(partsType.getStatus() == PartsType.STATUS_INVALID){
        			errMsg.add("【"+entity.getPartsName()+"】的【"+entity.getSpecificationModel()+"】型号已经作废，不能操作！");
        		}
        	}
            List<TrainTypeToParts> trains = getModelList(entity.getTrainTypeIDX(), entity.getPartsTypeIDX());
            if(trains.size() > 0){
                errMsg.add("【"+entity.getPartsName()+"】的【"+entity.getSpecificationModel()+"】型号的配件已经存在！");
            }
            if (errMsg.size() > 0) {
                String[] errArray = new String[errMsg.size()];
                errMsg.toArray(errArray);
                return errArray;
            }
        }
		return null;
	}
	/**
     * <li>说明：选择生产计划明细批量生成作业进度
     * <li>创建人：王治龙
     * <li>创建日期：2013-5-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param objList：对象集合
     * @return String[] 返回错误信息列表
     * @throws NoSuchFieldException 
     * @throws BusinessException 
	 * @throws ParseException 
     */
    public String[] saveOrUpdateList(TrainTypeToParts[] objList) throws BusinessException, NoSuchFieldException, ParseException{
        String[] errMsg = this.validateUpdate(objList);  //验证
        List<TrainTypeToParts> entityList = new ArrayList<TrainTypeToParts>();
        if (errMsg == null || errMsg.length < 1) {
            for(TrainTypeToParts t : objList){ //循环新增是为了验证方便
                entityList.add(t);
            }
            this.saveOrUpdate(entityList);
        }
        return errMsg;
    }
    /**
     * <li>说明：查询承修车型列表
     * <li>创建人：王治龙
     * <li>创建日期：2013-5-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return List集合
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List getTrains()throws BusinessException{
    	String sqlStr = SqlMapUtil.getSql("pjwl_parts_class:findTrainType");
    	return daoUtils.executeSqlQuery(sqlStr);
    }
	/**
	 * <li>说明：配件分类树
	 * <li>创建人：王治龙
	 * <li>创建日期：2013-5-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param id：节点ID
	 * @return List<HashMap> 
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap> findPartsClassTreeData(String id)throws BusinessException{
		String sqlStr = null ;
		boolean isLeaf = true;
	    String idToken = null; //ID标识
	    Map<String,String> params = new HashMap<String,String>(); //查询参数
		if("ROOT_0".equals(id)){//为第一级，查出下级所有承修车型
			sqlStr = SqlMapUtil.getSql("pjwl_parts_class:findTrainType");
			isLeaf = false ;
			idToken = "X_" ; //下一级ID前缀(修程)
		}
		if(id.indexOf("X_") == 0){ //查询修程
			params.put("where", " t.TRAIN_TYPE_IDX='"+id.substring(2)+"'");
			sqlStr = SqlMapUtil.getSql("pjwl_parts_class:findXC",params);
			isLeaf = true ;
			idToken = "P_" ; //下一级ID前缀(配件分类)
		}
		List<Object[]> partsList = daoUtils.executeSqlQuery(sqlStr);
		List<HashMap> children = new ArrayList<HashMap>();
		
		if("ROOT_0".equals(id)){
			for (Object[] obj : partsList) {           
                HashMap nodeMap = new HashMap();
                nodeMap.put("id", idToken + obj[0]);  //车主主键作为树节点ID
                nodeMap.put("text", obj[1]);         //车型简称
                nodeMap.put("trainTypeName", obj[2]); //车型名称
                nodeMap.put("trainTypeIDX", obj[0]); //车型主键
                nodeMap.put("leaf", isLeaf);
                children.add(nodeMap);
            }
		}else if(id.indexOf("X_") == 0){
			for (Object[] obj : partsList) {           
                HashMap nodeMap = new HashMap();
                nodeMap.put("id", idToken + obj[2]+ id); //修程主键作为树节点主键 保证唯一
                nodeMap.put("text", obj[1]);  //修程名称
                nodeMap.put("xcID", obj[2]);  //修程主键
                nodeMap.put("trainTypeIDX", obj[0]);  //主键
                nodeMap.put("leaf", isLeaf);
                children.add(nodeMap);
            }
		}
		return children;
	}
	/**
     * <li>说明：根据车型查询车号是否存在查询机车是否存在
     * <li>创建人：王治龙
     * <li>创建日期：2012-12-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX：车型IDX ；partsTypeIDX：配件型号主键
     * @return List<TrainTypeToParts> 机车实体对象集合
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<TrainTypeToParts> getModelList(String trainTypeIDX,String partsTypeIDX) throws BusinessException{
        StringBuffer hql = new StringBuffer();
        hql.append("From TrainTypeToParts t where t.recordStatus=0 ");
        if(!StringUtil.isNullOrBlank(trainTypeIDX)){
            hql.append(" and t.trainTypeIDX = '").append(trainTypeIDX).append("'");
        }
        if(!StringUtil.isNullOrBlank(partsTypeIDX)){
            hql.append(" and t.partsTypeIDX = '").append(partsTypeIDX).append("'");
        }
        return (List<TrainTypeToParts>)this.daoUtils.find(hql.toString());
    }
    /**
	 * 
	 * <li>说明：查询规格型号对应的试用车型信息
	 * <li>创建人：程梅
	 * <li>创建日期：2013-5-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public Page<TrainTypeToParts> getListForPartsTypeSearch(String partsTypeIDX) throws BusinessException{
		Map params = new HashMap();
		params.put("partsTypeIDX", partsTypeIDX);
        String sql = SqlMapUtil.getSql("pjwl-parts-type:partstypesearch_traintype",params);
//		Object[] objs_train = new Object[]{partsTypeIDX};
//		List<TrainTypeToParts> list = daoUtils.getHibernateTemplate().find(sql, objs_train);
        List<Object[]> list = daoUtils.executeSqlQuery(sql);
        List<TrainTypeToParts> partsList = new ArrayList<TrainTypeToParts>();
        TrainTypeToParts parts ;
        for(Object[] obj : list){
        	parts = new TrainTypeToParts();
        	parts.setIdx(obj[0].toString());
        	parts.setTrainTypeShortName(obj[1].toString());
        	parts.setStandardQty(Integer.parseInt(obj[2].toString()));
        	partsList.add(parts);
        }
		Page page = new Page<TrainTypeToParts>(partsList.size(),partsList);
        return page;
    }
    /**
     * 
     * <li>说明：根据下车信息和规格型号查询车型对应规格型号信息
     * <li>创建人：程梅
     * <li>创建日期：2015-4-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX 车型id
     * @param partsTypeIDX  规格型号id
     * @param trainNo 车号
     * @param repairClassId  修程id
     * @param start 开始行
     * @param limit 每页记录数
     * @param orders 排序对象
     * @return Page<TrainTypeToParts> 分页对象
     * @throws BusinessException
     */
     public Page<TrainTypeToParts> findListByTrainTypeIDX(String trainTypeIDX,String partsTypeIDX ,String trainNo ,String repairClassId ,int start,int limit, Order[] orders) throws BusinessException{
            if(StringUtil.isNullOrBlank(trainTypeIDX)){
                return new Page<TrainTypeToParts>(0,null);
            }
            String sql_select = SqlMapUtil.getSql("pjwl-parts-type:findTraintypeToPartsTypeSelect").replace("trainTypeIDX", trainTypeIDX)
            .replace("trainNo", trainNo).replace("repairClassId", repairClassId).replace("partsStatus", PartsAccount.PARTS_STATUS_ZC + "%");     
            String sql_from = SqlMapUtil.getSql("pjwl-parts-type:findTraintypeToPartsTypeFrom").replace("trainTypeIDX", trainTypeIDX)
            .replace("repairClassId", repairClassId); 
            if(!StringUtil.isNullOrBlank(partsTypeIDX)){
                sql_from = sql_from + " and t.PARTS_TYPE_IDX = '" + partsTypeIDX + "'";
            }
            StringBuffer totalSql = new StringBuffer("select count(1) ").append(sql_from);
            String sql = sql_select + sql_from ;
            return super.findPageList(totalSql.toString(), sql, start , limit, null, orders);
        } 
	public PartsTypeManager getPartsTypeManager() {
		return partsTypeManager;
	}

	public void setPartsTypeManager(PartsTypeManager partsTypeManager) {
		this.partsTypeManager = partsTypeManager;
	}
}