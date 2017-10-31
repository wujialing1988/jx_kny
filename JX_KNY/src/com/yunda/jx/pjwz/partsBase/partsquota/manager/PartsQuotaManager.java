package com.yunda.jx.pjwz.partsBase.partsquota.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjwz.partsBase.partsquota.entity.PartsQuota;
import com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType;
import com.yunda.jx.pjwz.partsBase.partstype.manager.PartsTypeManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsQuota业务类,互换配件定额
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-08-30
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "partsQuotaManager")
public class PartsQuotaManager extends JXBaseManager<PartsQuota, PartsQuota> {
    
    /**
     * <li>说明：将从规格型号选取的数据添加到配件定额表中
     * <li>创建人：程梅
     * <li>创建日期：2012-8-31
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：oversea：段级单位id，overseaName：段级单位名称，org：当前操作者所属单位，partsTypeManager：规格型号业务操作类，ids：所选规格型号id集
     * @return 返回值为空
     * @throws 抛出异常列表
     */
    public void saveFromPartsType(Long orgid, String orgname, PartsTypeManager partsTypeManager, Serializable... ids) throws BusinessException,
        NoSuchFieldException {
        List<PartsQuota> partsQuotaList = new ArrayList<PartsQuota>();
        PartsQuota quota;
        for (Serializable id : ids) {
            quota = new PartsQuota();
            PartsType type = partsTypeManager.getModelById(id);
            if (null == type) {
                continue;
            }
            quota.setPartsTypeIDX(type.getIdx());
            quota.setPartsName(type.getPartsName());
            quota.setSpecificationModel(type.getSpecificationModel());
            quota.setUnit(type.getUnit()); // 设置计量单位
            quota.setLimitQuantity(0);// 定额初始化为0
            // // 当前操作者所属单位的段级不存在就 存当前操作者单位
            // if("".equals(oversea)||oversea==null){
            // quota.setOwnerUnit(org.getOrgid());
            // quota.setOwnerUnitName(org.getOrgname());
            // }else{
            quota.setOwnerUnit(orgid);
            quota.setOwnerUnitName(orgname);
            // }
            partsQuotaList.add(quota);
        }
        this.saveOrUpdate(partsQuotaList);
    }
    
    /**
     * <li>说明：保存批量设置定额的信息
     * <li>创建人：程梅
     * <li>创建日期：2013-3-25
     * <li>修改人：何涛
     * <li>修改日期：2016-01-29
     * <li>修改内容：代码重构
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    public void saveOrUpdateList(PartsQuota[] quotaList) throws Exception {
        this.validateUpdate(quotaList); // 验证
        List<PartsQuota> entityList = new ArrayList<PartsQuota>();
        for (PartsQuota t : quotaList) {
            entityList.add(t);
        }
        this.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：新增修改保存前的实体对象前的批量验证业务
     * <li>创建人：程梅
     * <li>创建日期：2012-10-25
     * <li>修改人：何涛
     * <li>修改日期：2016-01-29
     * <li>修改内容：代码重构
     * @param entity 实体对象
     * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
     * @throws BusinessException
     */
    public void validateUpdate(PartsQuota[] quotaList) throws BusinessException {
        String[] errMsg;
        for (PartsQuota entity : quotaList) {
            errMsg = this.validateUpdate(entity);
            if (null != errMsg && errMsg.length > 0) {
                throw new BusinessException(errMsg[0]);
            }
        }
    }
    public Page<PartsQuota> findPageList(final SearchEntity<PartsQuota> searchEntity) {
        StringBuilder sb = new StringBuilder("Select new PartsQuota(a.idx,a.partsTypeIDX,a.partsName,a.specificationModel,a.unit,a.ownerUnit,a.ownerUnitName,a.limitQuantity,a.updateTime,b.operatorname) From PartsQuota a, AcOperator b Where a.updator = b.operatorid");
        sb.append(" And a.recordStatus = 0 ");
        StringBuffer awhere =  new StringBuffer();
        if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getPartsName())){
            awhere.append(" and a.partsName like '%"+searchEntity.getEntity().getPartsName()+Constants.LIKE_PIPEI);
        }
        if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getSpecificationModel())){
            awhere.append(" and a.specificationModel like '%"+searchEntity.getEntity().getSpecificationModel()+Constants.LIKE_PIPEI);
        }
        if(null != searchEntity.getEntity().getLimitQuantity()){
            awhere.append(" and a.limitQuantity = "+searchEntity.getEntity().getLimitQuantity());
        }
        sb.append(awhere) ;
        Order[] orders = searchEntity.getOrders();
        // 排序
        if (null == orders) {
            sb.append(" Order By a.updateTime");
        } else {
            sb.append(" Order By a." + orders[0].toString());
        }
        
        String totalHql = "Select Count(*) As rowcount " + sb.substring(sb.indexOf("From"));
        return super.findPageList(totalHql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
}
