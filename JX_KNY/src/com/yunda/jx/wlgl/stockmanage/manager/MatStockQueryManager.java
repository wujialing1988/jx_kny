package com.yunda.jx.wlgl.stockmanage.manager;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.wlgl.stockmanage.entity.MatStock;
import com.yunda.jx.wlgl.stockmanage.entity.MatStockQuery;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatStockQuery业务类, 物料库存查询
 * <li>创建人： 何涛
 * <li>创建日期： 2014-10-9 下午04:38:19
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="matStockQueryManager")
public class MatStockQueryManager extends JXBaseManager<MatStockQuery, MatStockQuery> {
	
	/**
	 * <li>说明： 分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-08
	 * <li>修改人： 张迪
	 * <li>修改日期：
	 * <li>修改内容：添加物料类型描述
	 * 
	 * @param searchEntity 查询实体对象
	 * @return 
	 */		
	@Override
	public Page<MatStockQuery> findPageList(SearchEntity<MatStockQuery> searchEntity) {
		MatStockQuery entity = searchEntity.getEntity();
		StringBuilder sb = new StringBuilder();
		sb.append("From MatStockQuery Where qty > 0 AND idx!=null");
		// 查询条件 - 退库库房idx主键
		if (!StringUtil.isNullOrBlank(entity.getWhIdx())) {
			sb.append(" And whIdx ='").append(entity.getWhIdx()).append("'");
		}
		// 查询条件 - 物料编码
		if (!StringUtil.isNullOrBlank(entity.getMatCode())) {
			sb.append(" And matCode Like'%").append(entity.getMatCode()).append("%'");
		}
		// 查询条件 - 物料描述
		if (!StringUtil.isNullOrBlank(entity.getMatDesc())) {
			sb.append(" And matDesc Like'%").append(entity.getMatDesc()).append("%'");
		}
		// 查询条件 - 物料类型
		if (!StringUtil.isNullOrBlank(entity.getMatType())) {
		    sb.append(" And matType Like'%").append(entity.getMatType()).append("%'");
		}
		// 查询条件 - 状态
		Short status = entity.getStatus();
		if (null != status) {
			if (status == MatStockQuery.CONST_INT_STATUS_MAX) {
				sb.append(" And qty > maxQty");
			} else if (status == MatStockQuery.CONST_INT_STATUS_MIN) {
				sb.append(" And qty < minQty");
			} else if (status == MatStockQuery.CONST_INT_STATUS_NOR) {
				sb.append(" And qty Between minQty And maxQty");
			}
		}
		//  排序字段
		Order[] orders = searchEntity.getOrders();
		if (null != orders && orders.length > 0) {
			sb.append(" Order By ");
			sb.append(orders[0].toString());
			for (int i = 1; i < orders.length; i++) {
				sb.append(", ");
				sb.append(orders[i].toString());
			}
		}
		String hql = sb.toString();
		String totalHql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
		return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}
    /**
     * 
     * <li>说明：根据库房id、物料编码、物料类型查询库存台账信息
     * <li>创建人：程梅
     * <li>创建日期：2016-5-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param whIdx 库房id
     * @param matCode 物料编码
     * @param matType 物料类型
     * @return 库存台账信息
     */
    @SuppressWarnings("unchecked")
    public MatStock getModelStock(String whIdx, String matCode, String matType) {
        String hql = "From MatStock Where whIdx = ? And matCode = ? And matType = ? ";
        List<MatStock> list = this.daoUtils.find(hql, new Object[]{ whIdx, matCode, matType });
        if (null != list && list.size() > 0) {
            MatStock matStock = list.get(0);
            int qty = 0 ;
            for(MatStock stock : list){
                qty += stock.getQty() ;
            }
            matStock.setQty(qty) ;
            return matStock ;
        }
        return null;
    }
    /**
     * 
     * <li>说明：根据库房id、物料编码、物料类型、库位查询库存台账信息
     * <li>创建人：程梅
     * <li>创建日期：2016-5-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param whIdx 库房id
     * @param matCode 物料编码
     * @param matType 物料类型
     * @param locationName 库位
     * @return 库存台账信息
     */
    public MatStock getModelStockByLocation(String whIdx, String matCode, String matType, String locationName) {
        String hql = "From MatStock Where whIdx = ? And matCode = ? And matType = ? and locationName = ? ";
        List list = this.daoUtils.find(hql, new Object[]{ whIdx, matCode, matType, locationName});
        if (null != list && list.size() > 0) {
            return (MatStock)list.get(0);
        }
        return null;
    }
}
