package com.yunda.jx.jczl.undertakemanage.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jczl.undertakemanage.entity.UndertakeTrain;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：UndertakeTrain业务类,承修车
 * <li>创建人：程梅
 * <li>创建日期：2013-03-04
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="undertakeTrainManager")
public class UndertakeTrainManager extends JXBaseManager<UndertakeTrain, UndertakeTrain>{
	
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
     * <li>说明：查询符合条件的承修机车信息
     * <li>创建人：程梅
     * <li>创建日期：2013-3-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    public Page<UndertakeTrain> findPageList(String searchJson,int start,int limit, Order[] orders) throws BusinessException{
        String sql = "select t.idx,t.Train_Type_IDX as \"trainTypeIDX\",t.Undertake_Train_Type_IDX as \"undertakeTrainTypeIDX\",t.Train_Type_ShortName as \"trainTypeShortName\",t.Train_No as \"trainNo\"," +
                "b.b_name as \"bName\",d.d_name as \"dName\",nvl(j.make_factory_name,'') as \"makeFactoryName\",j.Leave_Date as \"leaveDate\",u.t_use_name as \"trainUseName\"" +
                " from JCZL_UNDERTAKE_TRAIN t,JCZL_TRAIN j left join j_jcgy_train_use u  on j.train_use = u.t_use_id left join j_gyjc_deport d on j.D_ID=d.D_ID left join j_gyjc_bureau b on j.b_ID=b.b_ID" +
                " where 1=1 and t.Train_Type_IDX=j.Train_Type_IDX and t.train_no = j.train_no " +
                " and t.record_Status=0 and j.record_status = 0 ";
        StringBuffer awhere =  new StringBuffer();
        awhere.append(" order by t.update_Time DESC");
        sql += awhere;
        int beginPos = sql.toString().toLowerCase().indexOf("from");
        StringBuffer totalSql = new StringBuffer(" select count(*) ");
        totalSql.append(sql.toString().substring(beginPos));
        return super.findPageList(totalSql.toString(), sql, start , limit, searchJson, orders);
    }
	/**
     * 
     * <li>说明：保存从机车信息中选取的数据
     * <li>创建人：程梅
     * <li>创建日期：2013-3-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
	 */
     public void saveFromTrain(UndertakeTrain[] objList) throws BusinessException, NoSuchFieldException{
            List<UndertakeTrain> entityList = new ArrayList<UndertakeTrain>();
            for(UndertakeTrain t : objList){ //循环新增是为了验证方便
                entityList.add(t);
            }
            this.saveOrUpdate(entityList);
        }
     
	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务
	 * <li>创建人：程梅
	 * <li>创建日期：2013-03-04
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
     public String[] validateUpdate(UndertakeTrain[] entityList) throws BusinessException {
        List<String> errMsg = new ArrayList<String>();
        for(UndertakeTrain obj : entityList){
            if(!StringUtil.isNullOrBlank(obj.getTrainTypeIDX())
                && !StringUtil.isNullOrBlank(obj.getTrainNo())){
                List<UndertakeTrain> countList = this.getModelList(obj.getTrainTypeIDX(), obj.getTrainNo());
                if(countList.size() > 0 ){
                    errMsg.add("车型为【"+obj.getTrainTypeShortName()+"】车号为【"+obj.getTrainNo()+"】的机车，已经存在！");
                }
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
     * 
     * <li>说明：判断此车型车号是否已有数据
     * <li>创建人：程梅
     * <li>创建日期：2013-3-5
     * <li>修改人： 刘晓斌
     * <li>修改日期：2013-11-17
     * <li>修改内容：根据缓存开关来执行查询
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    @SuppressWarnings("unchecked")
    public List<UndertakeTrain> getModelList(String trainTypeIDX , String trainNo) throws BusinessException{
        StringBuffer hql = new StringBuffer();
        hql.append(" From UndertakeTrain t where t.recordStatus=0 ");
        if(!StringUtil.isNullOrBlank(trainTypeIDX)){
            hql.append("and t.trainTypeIDX = '").append(trainTypeIDX).append("' ");
        }
        if(!StringUtil.isNullOrBlank(trainNo)){
            hql.append("and t.trainNo = '").append(trainNo).append("'");
        }
        return daoUtils.find(enableCache(), hql.toString());
//        return this.daoUtils.find(hql.toString());
    }
    /**
     * <li>说明：根据承修车型主键查询对应的所有承修机车
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-4-22
     * <li>修改人： 刘晓斌
     * <li>修改日期：2013-11-17
     * <li>修改内容：根据缓存开关来执行查询
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    @SuppressWarnings("unchecked")
	public List<UndertakeTrain> findByTrainIDX(String trainIDX) throws BusinessException{
        StringBuffer hql = new StringBuffer();
        hql.append("From UndertakeTrain t where t.recordStatus=0 and t.undertakeTrainTypeIDX = '").append(trainIDX).append("'");
        return daoUtils.find(enableCache(), hql.toString());
//        return (List<UndertakeTrain>)daoUtils.find(hql.toString());
    }
    /**
     * 
     * <li>说明：查询承修机车列表---新增普查整治对象时所用
     * <li>创建人：程梅
     * <li>创建日期：2013-8-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    public Page<UndertakeTrain> findPageListForInspect(SearchEntity<UndertakeTrain> searchEntity, String planIDX,String projectIDX) throws BusinessException{
        String hql = "from UndertakeTrain t where t.recordStatus=0 ";
        StringBuffer awhere =  new StringBuffer();
        if(!StringUtil.isNullOrBlank(planIDX)){
            awhere.append(" and t.trainNo not in (select trainNo from InspectTrain where recordStatus=0 and planIDX='").append(planIDX).append("')");
        }
        if(!StringUtil.isNullOrBlank(projectIDX)){ //查询技术改造车型
        	awhere.append(" and t.trainNo not in (select trainNo from TechReformTrain where recordStatus=0 and projectIDX='").append(projectIDX).append("')");
        }
        if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getTrainNo())){
            awhere.append(" and t.trainNo like '%").append(searchEntity.getEntity().getTrainNo()).append("%'");
        }
        if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getTrainTypeShortName())){
            awhere.append(" and t.trainTypeShortName like '%").append(searchEntity.getEntity().getTrainTypeShortName().toUpperCase()).append("%'");
        }
        if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getTrainTypeIDX())){
            String typeIDXS = searchEntity.getEntity().getTrainTypeIDX();
            awhere.append(" and t.trainTypeIDX in ('-1' ");
            String[] typeIdxlist= typeIDXS.split(";");
            for(int i=0; i< typeIdxlist.length; i++){
                awhere.append(",'").append(typeIdxlist[i]).append("'");
            }
            awhere.append(")");
        }
        Order[] orders = searchEntity.getOrders();
        if(orders != null){
            for (Order order : orders) {
                awhere.append(" order by t."+order);
            }                   
        }else {
            awhere.append(" order by t.trainNo"); 
        }
        hql += awhere;
        StringBuffer totalHql = new StringBuffer(" select count(*) ");
        totalHql.append(hql);
        return super.findPageList(totalHql.toString(), hql, searchEntity.getStart(), searchEntity.getLimit());
    }
}