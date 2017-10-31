package com.yunda.jx.jxgc.repairrequirement.manager;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.repairrequirement.entity.PartsList;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsList业务类,配件清单
 * <li>创建人：程梅
 * <li>创建日期：2016-1-7
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsListManager")
public class PartsListManager extends JXBaseManager<PartsList, PartsList>{
	/** 日志工具 */
	@SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：配件清单列表
     * <li>创建人：程梅
     * <li>创建日期：2016-1-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return Page<PartsList> 分页列表
     * @throws BusinessException
     */
    public Page<PartsList> findPartsList(final SearchEntity<PartsList> searchEntity) throws BusinessException{
        PartsList partsList = searchEntity.getEntity() ;
        StringBuilder selectSql = new StringBuilder("select t.*,type.Parts_NAME, type.Specification_Model, type.unit  ");
        StringBuffer fromSql = new StringBuffer(" from JXGC_Parts_List t ,PJWZ_Parts_Type type where type.RECORD_STATUS=0 and t.PARTS_TYPE_IDX = type.idx ");
        StringBuffer awhere =  new StringBuffer();
        if(!StringUtil.isNullOrBlank(partsList.getRelationIdx())){
            awhere.append(" and t.Relation_IDX='").append(partsList.getRelationIdx()).append("' ") ;
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
     * 
     * <li>说明：批量保存实体时的验证方法
     * <li>创建人：程梅
     * <li>创建日期：2016-1-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsLists 配件清单实体数组
     * @return String[] 验证消息
     */
    public String[] validateUpdate(PartsList[] partsLists) {
        for (PartsList entity : partsLists) {
            String[] errorMsg = this.validateUpdate(entity);
            if (null == errorMsg) {
                continue;
            }
            return errorMsg;
        }
        return null;
    }
    /**
     * 
     * <li>说明：批量保存新增的配件清单
     * <li>创建人：程梅
     * <li>创建日期：2016-1-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsLists 配件清单实体数组
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveOrUpdate(PartsList[] partsLists) throws BusinessException, NoSuchFieldException {
        for (PartsList entity : partsLists) {
            this.saveOrUpdate(entity);
        }
    }
}