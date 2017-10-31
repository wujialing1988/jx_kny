package com.yunda.jx.pjwz.partsBase.outsourcinglist.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.pjwz.partsBase.outsourcinglist.entity.PartsOutsourcingCatalog;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: PartsOutsourcingCatalog业务类
 * <li>创建人：程梅
 * <li>创建日期：2016-7-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value="partsOutsourcingCatalogManager")
public class PartsOutsourcingCatalogManager extends JXBaseManager<PartsOutsourcingCatalog, PartsOutsourcingCatalog> {

    
    /**
     * 
     * <li>说明：新增前验证
     * <li>创建人：程梅
     * <li>创建日期：2016-7-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t 需验证的委外修目录对象
     */
    public void validateSave(PartsOutsourcingCatalog t){
        String hql = "From PartsOutsourcingCatalog Where jcpjbm = ? ";
        PartsOutsourcingCatalog entity = (PartsOutsourcingCatalog) this.daoUtils.findSingle(hql, new Object[]{ t.getJcpjbm()});
        if (null != entity && !entity.getIdx().equals(t.getIdx())) {
            throw new BusinessException("不可以添加重复的数据！");
        }
        
    }
    /**
     * 
     * <li>说明：新增委外修目录
     * <li>创建人：程梅
     * <li>创建日期：2016-7-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param catalogs 需新增的委外修目录
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveNewList(PartsOutsourcingCatalog[] catalogs) throws BusinessException, NoSuchFieldException  {
        if (null == catalogs && catalogs.length <= 0) {
            return;
        }
        List<PartsOutsourcingCatalog> catalogList = new ArrayList<PartsOutsourcingCatalog>();
        OmEmployee emp = SystemContext.getOmEmployee();
        
        for (PartsOutsourcingCatalog catalog : catalogs) {
            if(StringUtil.isNullOrBlank(catalog.getIdx())) this.validateSave(catalog);
            catalog = EntityUtil.setSysinfo(catalog);
            if (emp != null) {
                catalog.setCreatorName(emp.getEmpname());
                catalog.setUpdatorName(emp.getEmpname());
            }
            catalogList.add(catalog);
        }
        this.daoUtils.getHibernateTemplate().saveOrUpdateAll(catalogList);
    }
    /**
     * 
     * <li>说明：设置委修厂家
     * <li>创建人：程梅
     * <li>创建日期：2016-7-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idxs 需修改的委外修目录idx
     * @param factoryId 厂家id
     * @param factoryName 厂家名称
     * @return 修改条数
     */
    public int updateSetMadeFactory(String idxs, String factoryId, String factoryName){
        idxs = "'" + idxs.replace(",", "','") + "'" ;
        OmEmployee emp = SystemContext.getOmEmployee();
        StringBuffer sql = new StringBuffer("update pjwz_parts_outsourcing_catalog set made_factory_idx ='")
        .append(factoryId).append("', made_factory_name = '").append(factoryName).append("',updator = ")
        .append(emp.getOperatorid()).append(",updator_name='").append(emp.getEmpname())
        .append("', update_time = sysdate where idx in (").append(idxs).append(")");
        
        return daoUtils.executeSql(sql.toString());
    }
    /**
     * 
     * <li>说明：重写保存方法，为了保存修改人名称
     * <li>创建人：程梅
     * <li>创建日期：2016-7-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t 需修改的委外修目录对象
     */
    public void saveOrUpdate(PartsOutsourcingCatalog t) throws BusinessException, NoSuchFieldException {
        t = EntityUtil.setSysinfo(t);
        OmEmployee emp = SystemContext.getOmEmployee();
        t.setUpdatorName(emp.getEmpname());
        this.daoUtils.getHibernateTemplate().saveOrUpdate(t);
    }
    /**
     * 
     * <li>说明：设置送出周期
     * <li>创建人：程梅
     * <li>创建日期：2016-7-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idxs 需修改的委外修目录idx
     * @param outCycle 送出周期
     * @return 修改条数
     */
    public int updateSetOutCycle(String idxs, String outCycle){
        idxs = "'" + idxs.replace(",", "','") + "'" ;
        OmEmployee emp = SystemContext.getOmEmployee();
        StringBuffer sql = new StringBuffer("update pjwz_parts_outsourcing_catalog set out_cycle =")
        .append(outCycle).append(", updator = ")
        .append(emp.getOperatorid()).append(",updator_name='").append(emp.getEmpname())
        .append("', update_time = sysdate where idx in (").append(idxs).append(")");
        
        return daoUtils.executeSql(sql.toString());
    }
    /**
     * 
     * <li>说明：设置检修周期
     * <li>创建人：程梅
     * <li>创建日期：2016-7-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idxs 需修改的委外修目录idx
     * @param repairCycle 检修周期
     * @return 修改条数
     */
    public int updateSetRepairCycle(String idxs, String repairCycle){
        idxs = "'" + idxs.replace(",", "','") + "'" ;
        OmEmployee emp = SystemContext.getOmEmployee();
        StringBuffer sql = new StringBuffer("update pjwz_parts_outsourcing_catalog set repair_cycle =")
        .append(repairCycle).append(", updator = ")
        .append(emp.getOperatorid()).append(",updator_name='").append(emp.getEmpname())
        .append("', update_time = sysdate where idx in (").append(idxs).append(")");
        
        return daoUtils.executeSql(sql.toString());
    }
}
