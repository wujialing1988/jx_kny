package com.yunda.jx.pjwz.partsBase.exchangeboundary.manager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjwz.partsBase.exchangeboundary.entity.RcClassToParts;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：RcClassToParts业务类,修程配件分类对应车型规格型号
 * <li>创建人：王治龙
 * <li>创建日期：2013-05-23
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="rcClassToPartsManager")
public class RcClassToPartsManager extends JXBaseManager<RcClassToParts, RcClassToParts>{
	private TrainTypeToPartsManager typeToPartsManager;
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
	public String[] validateUpdate(RcClassToParts[] entityList) throws BusinessException {
		List<String> errMsg = new ArrayList<String>();
        for(RcClassToParts entity : entityList){
            List<RcClassToParts> trains = getModelList(entity.getRepairClassIDX(), entity.getTrainTypeToPartsIDX());
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
     * <li>说明：选择规格型号批量生成修程配件分类对应车型规格型号
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
    public String[] saveOrUpdateList(RcClassToParts[] objList) throws BusinessException, NoSuchFieldException, ParseException{
        String[] errMsg = this.validateUpdate(objList);  //验证
        List<RcClassToParts> entityList = new ArrayList<RcClassToParts>();
        if (errMsg == null || errMsg.length < 1) {
            for(RcClassToParts t : objList){ //循环新增是为了验证方便
                entityList.add(t);
            }
            this.saveOrUpdate(entityList);
        }
        return errMsg;
    }
    /**
     * 
     * <li>说明：配件互换配件维护对应修程的配给互换页面加载时调用
     * <li>创建人：王斌
     * <li>创建日期：2014-5-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    public Page<RcClassToParts> findPageQueryForRcClass(List<Condition> whereList,List<Order> orderList,Integer start,Integer limit) throws BusinessException{
    	StringBuffer searchParam=new StringBuffer();
    	if(whereList!=null && whereList.size()>0){
    		for(Condition condition:whereList){
    			if(!StringUtil.isNullOrBlank(condition.getPropName())){
    				String propName=String.valueOf(condition.getPropName());
    				String propValue=String.valueOf(condition.getPropValue());
    				if("trainTypeIDX".equals(propName)){
    					searchParam.append(" and t.").append(propName).append(" = ").append("'"+propValue+"'");
    				}else if("repairClassIDX".equals(propName)){
    					searchParam.append(" and r.").append(propName).append(" = ").append("'"+propValue+"'");
    				}else{
    					searchParam.append(" and p.").append(propName).append(" like ").append("'%"+propValue+"%'");
    				}
    			}
    		}
    	}
    	if(orderList!=null && orderList.size()>0){
    		searchParam.append(" order by ");
    		for (Order order : orderList) {
				if(String.valueOf(order).indexOf("trainTypeShortName")!=-1){
					searchParam.append(" t.").append(order);
				}else if(String.valueOf(order).indexOf("standardQty")!=-1){
					searchParam.append(" t.").append(order);
				}else{
					searchParam.append(" p.").append(order);
				}
			}
    	}
    	String form_hql=" FROM RcClassToParts r,TrainTypeToParts t,PartsType p  where r.recordStatus=0 and t.recordStatus=0" +
    			        "  and r.trainTypeToPartsIDX=t.idx and t.partsTypeIDX=p.idx ".concat(searchParam.toString());
    	
    	String hql="select new RcClassToParts(r.idx,t.trainTypeIDX,t.trainTypeShortName,t.partsTypeIDX,r.repairClassIDX,r.repairClassName," +
    					"r.trainTypeToPartsIDX,p.matCode,p.partsName," +
    					" p.specificationModel,p.specificationModelCode,p.unit,t.standardQty) "+form_hql;
    	String total_hql="select count(*) "+form_hql;
    	
    	return super.findPageList(total_hql, hql, start, limit);
    }
    
    /**
     * <li>说明：根据车型查询车号是否存在查询机车是否存在
     * <li>创建人：王治龙
     * <li>创建日期：2012-12-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param repairClassIDX：修程主键 ；trainTypeToPartsIDX：车型对应配件型号
     * @return List<RcClassToParts> 机车实体对象集合
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<RcClassToParts> getModelList(String repairClassIDX,String trainTypeToPartsIDX) throws BusinessException{
        StringBuffer hql = new StringBuffer();
        hql.append("From RcClassToParts t where t.recordStatus=0 ");
        if(!StringUtil.isNullOrBlank(repairClassIDX)){
            hql.append(" and t.repairClassIDX = '").append(repairClassIDX).append("'");
        }
        if(!StringUtil.isNullOrBlank(trainTypeToPartsIDX)){
            hql.append(" and t.trainTypeToPartsIDX = '").append(trainTypeToPartsIDX).append("'");
        }
        return (List<RcClassToParts>)this.daoUtils.find(hql.toString());
    }

	public TrainTypeToPartsManager getTypeToPartsManager() {
		return typeToPartsManager;
	}

	public void setTypeToPartsManager(TrainTypeToPartsManager typeToPartsManager) {
		this.typeToPartsManager = typeToPartsManager;
	}
    
}