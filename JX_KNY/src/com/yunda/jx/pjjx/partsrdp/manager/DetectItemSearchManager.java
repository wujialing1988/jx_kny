package com.yunda.jx.pjjx.partsrdp.manager;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.jx.pjjx.partsrdp.entity.DetectItemSearch;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdpUnionDevice;

/**
 * <li>说明：检测项查询业务类
 * <li>创建人： 张凡
 * <li>创建日期：2015年11月21日
 * <li>成都运达科技股份有限公司
 */
@Service
public class DetectItemSearchManager extends JXBaseManager<DetectItemSearch, DetectItemSearch> {
    
    /**
     * <li>方法说明：查询检测项
     * <li>方法名：findPageList
     * @param start start
     * @param limit limit
     * @param orders orders
     * @param search 过滤对象
     * @return
     * <li>创建人： 张凡
     * <li>创建日期：2015年11月17日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public Page<PartsRdpUnionDevice> findPageList(int start, int limit, Order[] orders, DetectItemSearch search){
        
        StringBuilder sql = new StringBuilder("select t.equip_card_desc, d.data_item_name, d.data_item_desc, d.unit, d.item_value");
        sql.append(" from pjjx_parts_rdp_equip_card t, pjjx_parts_rdp_equip_di d where d.rdp_equip_card_idx = t.idx");


        if(search.getEquipCardDesc() != null){
            sql.append(" and equip_card_desc like '%").append(search.getEquipCardDesc()).append("%'");
        }
        
        if(search.getItemValue() != null){
            sql.append(" and item_value like '%").append(search.getItemValue()).append("%'");
        }
        if(search.getDataItemName() != null){
            sql.append(" and data_item_name like '%").append(search.getDataItemName()).append("%'");
        }
        if(search.getRdpIdx() != null){
            sql.append(" and rdp_idx = '").append(search.getRdpIdx()).append("'");
        }
        String totalSQL = "select count(1) from (" + sql + ")" ;
        String querySQL = sql.toString();
        return super.findPageList(totalSQL, querySQL, start, limit, null, orders);
    }
}
