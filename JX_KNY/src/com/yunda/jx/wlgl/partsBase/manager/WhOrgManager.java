package com.yunda.jx.wlgl.partsBase.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjwz.partsBase.warehouse.entity.Warehouse;
import com.yunda.jx.wlgl.partsBase.entity.WhOrg;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WhOrg业务类,库房与班组关系维护（用于消耗即扣库的模式）
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-09-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="whOrgManager")
public class WhOrgManager extends JXBaseManager<WhOrg, WhOrg>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * 
	 * <li>说明：查询列表
	 * <li>创建人：程梅
	 * <li>创建日期：2014-9-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public Page page(String searchJson,int start,int limit, Order[] orders) throws ClassNotFoundException {
    	
        StringBuffer selectSql = new StringBuffer("select t.idx as \"idx\",w.idx as \"whIdx\", w.warehouse_name as \"wareHouseName\", o.orgname as \"orgname\", t.update_time as\"updateTime\" ");
        StringBuffer fromSql = new StringBuffer(" FROM PJWZ_WAREHOUSE w ")
        .append("left join WLGL_WH_ORG t on t.wh_idx = w.idx and t.record_status = 0 ")
        .append("left join om_organization o on t.org_id = o.orgid ")
        .append("where w.record_status = 0 and w.status=").append(Warehouse.STATUS_USE);
        StringBuffer totalSql = new StringBuffer("select count(1) ");
        StringBuffer sql = new StringBuffer();
        totalSql.append(fromSql);
        sql.append(selectSql).append(fromSql);
        return super.findPageList(totalSql.toString(), sql.toString(), start , limit, searchJson, orders);
    }
	/**
	 * 
	 * <li>说明：保存
	 * <li>创建人：程梅
	 * <li>创建日期：2014-9-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void saveFromOrg(String orgid,WhOrg[] detailList) throws BusinessException, NoSuchFieldException {
        List<WhOrg> whOrgList = new ArrayList<WhOrg>();
        for (WhOrg whOrg : detailList) {
        	WhOrg obj = new WhOrg();
        	if(!StringUtil.isNullOrBlank(whOrg.getIdx())){
        		obj = this.getModelById(whOrg.getIdx());
        	}
        	obj.setWhIdx(whOrg.getWhIdx());
        	obj.setOrgId(Long.parseLong(orgid));
        	whOrgList.add(obj);
        }
        this.saveOrUpdate(whOrgList);
    }
	/**
	 * <li>说明：删除实体对象前的验证业务
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2014-09-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 实体对象的idx主键数组
	 * @return 返回删除操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */	
	@Override
	public String[] validateDelete(Serializable... ids) throws BusinessException {
		return null;
	}
	
	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2014-09-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@Override
	public String[] validateUpdate(WhOrg entity) throws BusinessException {
		return null;
	}
	
	/**
	 * <li>说明：根据组织机构id查询该机构已维护的库房信息idx主键
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param orgId
	 * @return
	 */
	public String getWhIdxByOrgId(Long orgId) {
		String hql = "From WhOrg Where recordStatus = 0 And orgId = ?";
		List list = this.daoUtils.find(hql, new Object[]{ orgId });
		if (null != list && list.size() > 0) {
			return ((WhOrg) list.get(0)).getWhIdx();
		}
		return null;
	}
	
}