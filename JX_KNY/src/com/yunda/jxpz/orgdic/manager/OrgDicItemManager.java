package com.yunda.jxpz.orgdic.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jxpz.orgdic.entity.OrgDicItem;
import com.yunda.jxpz.orgdic.entity.OrgDicItem.OrgDicItemId;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：OrgDicItem业务类,常用部门字典项
 * <li>创建人：程梅
 * <li>创建日期：2015-09-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="orgDicItemManager")
public class OrgDicItemManager extends JXBaseManager<OrgDicItem, OrgDicItem> implements IbaseCombo{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：根据字典编码查询字典项列表
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @param dictTypeId 字典编码
     * @return Page 字典项分页列表对象
     * @throws BusinessException
     */
    public Page<OrgDicItem> pageListByTypeId(final SearchEntity<OrgDicItem> searchEntity, String dictTypeId ) throws BusinessException {
        StringBuilder hql = new StringBuilder();
        hql.append("from OrgDicItem where 1=1 ");
        if (!StringUtil.isNullOrBlank(dictTypeId)) {
            hql.append(" and id.dictTypeId ='").append(dictTypeId).append("'");
        }
        Order[] orders = searchEntity.getOrders();
        hql.append(HqlUtil.getOrderHql(orders));
        String totalHql = "select count(*) " + hql.toString();
        return findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    /**
     * 
     * <li>说明：保存前验证唯一性
     * <li>创建人：程梅
     * <li>创建日期：2015-10-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param dictTypeId 字典编码
     * @param items 需验证的字典项信息
     * @return String[] 错误提示
     */
    public String[] validateSave(String dictTypeId , OrgDicItem[] items) {
        List<String> errMsg = new ArrayList<String>();
        for(OrgDicItem item : items){
            String hql = "Select count(*) From OrgDicItem where id.dictTypeId='"+dictTypeId+"' and id.orgId='"+item.getOrgid()+"'";
            int count = this.daoUtils.getCount(hql);
            if(count > 0){
                errMsg.add("部门名称【"+ item.getOrgName() +"】已存在！");
            }
        }
        if (errMsg.size() > 0) {
            String[] errArray = new String[errMsg.size()];
            errMsg.toArray(errArray);
            return errArray;
        }
        return null;
    }
    /**
     * 
     * <li>说明：保存前验证唯一性【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param dictTypeId 字典编码
     * @param item 需验证的字典项信息
     * @return String[] 错误提示
     */
    public String[] validateSingle(String dictTypeId , OrgDicItem item) {
        List<String> errMsg = new ArrayList<String>();
        String hql = "Select count(*) From OrgDicItem where id.dictTypeId='"+dictTypeId+"' and id.orgId='"+item.getOrgid()+"'";
        int count = this.daoUtils.getCount(hql);
        if(count > 0){
            errMsg.add("部门名称【"+ item.getOrgName() +"】已存在！");
        }
        if (errMsg.size() > 0) {
            String[] errArray = new String[errMsg.size()];
            errMsg.toArray(errArray);
            return errArray;
        }
        return null;
    }
    /**
     * 
     * <li>说明：保存常用部门字典项信息
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param dictTypeId 字典编码
     * @param items 需验证的字典项信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveItems(String dictTypeId , OrgDicItem[] items) throws BusinessException, NoSuchFieldException {
        if(null != items && items.length > 0){
            for(OrgDicItem item : items){
                OrgDicItemId itemId = new OrgDicItemId() ;
                itemId.setDictTypeId(dictTypeId);
                itemId.setOrgId(item.getOrgid());
                item.setId(itemId);
                this.daoUtils.getHibernateTemplate().save(item);
            }
        }
    }
    /**
     * 
     * <li>说明：保存常用部门字典项信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param dictTypeId 字典编码
     * @param item 需保存的字典项信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveSingleItem(String dictTypeId , OrgDicItem item) throws BusinessException, NoSuchFieldException {
            OrgDicItemId itemId = new OrgDicItemId() ;
            itemId.setDictTypeId(dictTypeId);
            itemId.setOrgId(item.getOrgid());
            item.setId(itemId);
            this.daoUtils.getHibernateTemplate().save(item);
    }
    /**
     * 
     * <li>说明：根据部门id和字典编码查询字典项
     * <li>创建人：程梅
     * <li>创建日期：2015-10-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param dictTypeId 字典编码
     * @param orgId 部门id
     * @return OrgDicItem 字典项对象
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public OrgDicItem getSingleItem(String dictTypeId ,String orgId) throws BusinessException {
        StringBuilder hql = new StringBuilder();
        hql.append(" From OrgDicItem where id.dictTypeId ='").append(dictTypeId).append("' and id.orgId='").append(orgId).append("'");
        List<OrgDicItem> list = this.daoUtils.find(hql.toString());
        if(null != list && list.size() > 0){
           return list.get(0); 
        }else return null;
    }
    /**
     * <li>说明：物理删除记录，根据制定的实体身份标示数组进行批量删除实体
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param dictTypeId 字典编码
     * @param ids 实体类主键idx数组
     */ 
    public void delete(String dictTypeId,Serializable... ids) throws BusinessException, NoSuchFieldException {
        List<OrgDicItem> entityList = new ArrayList<OrgDicItem>();
        for (Serializable id : ids) {
            OrgDicItem item = getSingleItem(dictTypeId , id.toString());
            entityList.add(item);
        }
        this.daoUtils.getHibernateTemplate().deleteAll(entityList);
    }
    /**
     * 
     * <li>说明：根据常用部门字典类型id查询字典项
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param dictTypeId 字典编码
     * @return List 字典项列表
     */
    @SuppressWarnings("unchecked")
    public List<OrgDicItem> getListByDictTypeId(String dictTypeId){
        String hql = "From OrgDicItem where id.dictTypeId='"+dictTypeId+"'";
        List<OrgDicItem> list = daoUtils.find(hql);
        return list ;
    }
    /**
     * 
     * <li>说明：获取配件周转常用部门列表【配件周转常用部门编码（字典编码）为“accountorg”】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return Page<OrgDicItem> 部门列表
     * @throws BusinessException
     */
    public Page<OrgDicItem> getAccountOrgList(final SearchEntity<OrgDicItem> searchEntity ) throws BusinessException {
        StringBuilder hql = new StringBuilder();
        hql.append("from OrgDicItem where id.dictTypeId ='accountorg' ");
        Order[] orders = searchEntity.getOrders();
        if(orders != null && orders.length > 0){            
            hql.append(HqlUtil.getOrderHql(orders));
        }else{
            hql.append(" order by orgName ");
        }
        String totalHql = "select count(*) " + hql.toString();
        return findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    
    
    /**
     * <li>说明：获取配件周转常用部门列表【配件周转常用部门编码（字典编码）为“accountorg”】_【Base_Comb】
     * <li>创建人：王利成
     * <li>创建日期：2015-11-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param req 请求
     * @param start 开始页
     * @param limit 最大数
     * @throws Exception
     * @return Map<String, Object>
     */
    @Override
    @SuppressWarnings("unchecked")
	public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit) throws Exception{
    	StringBuilder sb = new StringBuilder("From OrgDicItem Where 0 = 0");
    	String queryParams = StringUtil.nvl(req.getParameter("queryParams"), "{}");
    	
    	JSONObject jo = JSONObject.parseObject(queryParams);
    	String dictTypeId = jo.getString("dictTypeId");
    	
    	if (!StringUtil.isNullOrBlank(dictTypeId)) {
    		sb.append(" And id.dictTypeId = '").append(dictTypeId).append("'");
    	}
    	sb.append(" Order By orgName");
    	String totalHql = "Select count(*) As rowcount " + sb.substring(sb.indexOf("From"));
    	Page<OrgDicItem> page = this.findPageList(totalHql, sb.toString(), start, limit);
    	List<OrgDicItem> list = page.getList();
    	for (OrgDicItem odi : list) {
    		odi.setOrgid(odi.getId().getOrgId());
    	}
		return page.extjsStore();
    }
}