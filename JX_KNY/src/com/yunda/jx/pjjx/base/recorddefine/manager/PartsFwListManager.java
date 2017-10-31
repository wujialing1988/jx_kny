package com.yunda.jx.pjjx.base.recorddefine.manager;

import java.util.Arrays;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.base.recorddefine.entity.PartsFwList;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 配件清单业务类
 * <li>创建人：程锐
 * <li>创建日期：2016-2-3
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2
 */
@Service(value="partsFwListManager")
public class PartsFwListManager extends JXBaseManager<PartsFwList, PartsFwList> {
	
	/**
     * <li>说明：配件清单列表
     * <li>创建人：程锐
     * <li>创建日期：2016-1-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询包装类
     * @return 配件清单分页列表对象
     * @throws BusinessException
     */
	public Page<PartsFwList> findPartsList(final SearchEntity<PartsFwList> searchEntity) throws BusinessException{
		PartsFwList partsList = searchEntity.getEntity() ;
        StringBuilder selectSql = new StringBuilder("select t.*,type.Parts_NAME, type.Specification_Model, type.unit  ");
        StringBuffer fromSql = new StringBuffer(" from PJJX_Parts_List t ,PJWZ_Parts_Type type where type.RECORD_STATUS=0 and t.PARTS_TYPE_IDX = type.idx ");
        StringBuffer awhere =  new StringBuffer();
        if(!StringUtil.isNullOrBlank(partsList.getRelationIDX())){
            awhere.append(" and t.Relation_IDX='").append(partsList.getRelationIDX()).append("' ") ;
        }
        Order[] orders = searchEntity.getOrders();
        if(orders != null && orders.length > 0){            
            awhere.append(HqlUtil.getOrderHql(orders));
        }else{
            awhere.append(" order by type.Specification_Model");
        }
        StringBuilder totalSql = new StringBuilder("select count(*) ").append(fromSql).append(awhere);
        StringBuilder sql = selectSql.append(fromSql).append(awhere);
        return this.findPageList(totalSql.toString(), sql.toString(), searchEntity.getStart(), searchEntity.getLimit(),null,searchEntity.getOrders());
    }
	
    /**
     * <li>说明：批量保存新增的配件清单
     * <li>创建人：程锐
     * <li>创建日期：2016-1-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsLists 配件清单实体数组
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveOrUpdate(PartsFwList[] partsLists) throws BusinessException, NoSuchFieldException {
    	if (partsLists == null || partsLists.length < 1)
    		return;
    	saveOrUpdate(Arrays.asList(partsLists));        
    }
}
