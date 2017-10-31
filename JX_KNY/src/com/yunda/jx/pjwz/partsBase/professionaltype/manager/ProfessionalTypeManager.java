package com.yunda.jx.pjwz.partsBase.professionaltype.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.IbaseComboTree;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjwz.partsBase.professionaltype.entity.ProfessionalType;
import com.yunda.jx.webservice.stationTerminal.base.entity.ProfessionalTypeBean;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: ProfessionalType业务类,专业类型
 * <li>创建人：王治龙
 * <li>创建日期：2012-8-30
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="professionalTypeManager")
public class ProfessionalTypeManager extends JXBaseManager<ProfessionalType, ProfessionalType> implements IbaseComboTree{

	/**
	 * <li>说明：确定该业务类是否使用查询缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-15
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
	 * <li>说明：校验方法
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-8-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity：ProfessionalType实体对象
	 * @return String[] 返货错误验证信息
	 * @throws BusinessException
	 */
	@Override
	public String[] validateUpdate(ProfessionalType entity) throws BusinessException{
		List<String> errMsg = new ArrayList<String>();
		ProfessionalType type = this.getModelById(entity.getIdx()); //获取当前对象在数据库中的值
		if("".equals(entity.getIdx()) || !type.getProfessionalTypeID().equals(entity.getProfessionalTypeID())){ //新增时验证
			String hql = "Select count(*) From ProfessionalType where recordStatus = 0 and  professionalTypeID='"+entity.getProfessionalTypeID()+"'";
			int count = this.daoUtils.getCount(hql);
			if(count > 0){
				errMsg.add("专业类型编码【"+ entity.getProfessionalTypeID() +"】已存在！");
			}
		}
		if(!"".equals(entity.getIdx())){
			if("0".equals(type.getIsLeaf()) && "1".equals(entity.getIsLeaf())){
				String hql = "Select count(*) From ProfessionalType where recordStatus = 0 and parentIDX='"+entity.getIdx()+"'";
				int count = this.daoUtils.getCount(hql);
				if(count > 0){
					errMsg.add("【"+ entity.getProfessionalTypeName() +"】专业已存在下级节点，不能变更！");
				}
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
	 * <li>说明：得到专业类型树
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-10-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param parentIDX：父节点主键，status：业务状态
	 * @return List<HashMap> 返回树的集合
	 * @throws BusinessException
	 */
	public List<HashMap<String, Object>> findProfessionalTree(String parentIDX,String status) throws BusinessException{
		String hql="from ProfessionalType where parentIDX=? and status in ("+status+") and recordStatus !=1";
		List<ProfessionalType> professionalTypeList = 
			(List<ProfessionalType>)findProByParam(hql, new Object[]{parentIDX});
		List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
		for (ProfessionalType type : professionalTypeList) {
//			TreeNode node = new TreeNode();
//			node.setId(type.getIdx());
//			node.setText(type.getProfessionalTypeName());
//			if("1".equals(type.getIsLeaf())){
//				node.setLeaf(true);
//			}else {
//				node.setLeaf(false);
//			}
//			Boolean IsLeaf = "1".equals(type.getIsLeaf()) ? true : false;
			Boolean IsLeaf = this.isLeaf(type.getIdx(),status);
			HashMap<String, Object> nodeMap = new HashMap<String, Object>();
			nodeMap.put("id", type.getIdx());
			nodeMap.put("text", type.getProfessionalTypeName());
			nodeMap.put("leaf", IsLeaf);
			nodeMap.put("isLeaf", type.getIsLeaf());
			nodeMap.put("status", type.getStatus());
			nodeMap.put("parentIDX", type.getParentIDX());
			nodeMap.put("professionalTypeID", type.getProfessionalTypeID());
			nodeMap.put("proSeq", type.getProSeq());
			
			children.add(nodeMap);
		}
		return children;
	}
	/**
	 * 
	 * <li>说明：带复选框的专业类型树
	 * <li>创建人：程梅
	 * <li>创建日期：2013-7-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public List<HashMap<String, Object>> findProfessionalTreeForCheck(String parentIDX,String status) throws BusinessException{
		String hql="from ProfessionalType where parentIDX=? and status in ("+status+") and recordStatus !=1";
		List<ProfessionalType> professionalTypeList = 
			(List<ProfessionalType>)findProByParam(hql, new Object[]{parentIDX});
		List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
		for (ProfessionalType type : professionalTypeList) {
			Boolean IsLeaf = this.isLeaf(type.getIdx(),status);
			HashMap<String, Object> nodeMap = new HashMap<String, Object>();
			nodeMap.put("id", type.getIdx());
			nodeMap.put("text", type.getProfessionalTypeName());
			nodeMap.put("leaf", IsLeaf);
			nodeMap.put("isLeaf", type.getIsLeaf());
			nodeMap.put("status", type.getStatus());
			nodeMap.put("parentIDX", type.getParentIDX());
			nodeMap.put("professionalTypeID", type.getProfessionalTypeID());
			nodeMap.put("proSeq", type.getProSeq());
			nodeMap.put("checked", false);
			children.add(nodeMap);
		}
		return children;
	}
	/**
	 * <li>说明：查询是否为子节点
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-9-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idx：实体主键；status：业务对象
	 * @return boolean
	 * @throws BusinessException
	 */
	public boolean isLeaf(String idx  , String status) throws BusinessException{
		StringBuffer hql = new StringBuffer();
		hql.append("select count(*) From ProfessionalType t where t.recordStatus = 0 ") ;
		if(!StringUtil.isNullOrBlank(status)){
			hql.append(" and t.status in ("+status+")")	;
		}
		if(!StringUtil.isNullOrBlank(idx)){
			hql.append(" and t.parentIDX='"+idx+"'")	;
		}
		
//		int count = this.daoUtils.getCount(hql.toString());
		int count = daoUtils.getInt(enableCache(), hql.toString());
		return count == 0 ? true : false;
	}
	/**
	 * 
	 * <li>说明：查询专业类型集合
	 * <li>创建人：李虎
	 * <li>创建日期：2012-8-29
	 * <li>修改人： 刘晓斌
	 * <li>修改日期：2013-11-14
	 * <li>修改内容：使用查询缓存
	 * @param hql：查询HQL；params：Object []数组
	 * @return List<ProfessionalType> 返回集合
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List<ProfessionalType> findProByParam(String hql,Object [] params) throws BusinessException{
		return daoUtils.find(enableCache(), hql, params);
//		return getDaoUtils().find(hql, params);
	}
	
	/* *
	 * <li>说明：查询ProfessionalType记录
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-8-29
	 * <li>修改人： 刘晓斌
	 * <li>修改日期：2013-11-14
	 * <li>修改内容：使用查询缓存
	 * @param entity：ProfessionalType实体对象；status：业务对象状态；orders ：Order[]排序数组
	 * @return List<ProfessionalType>专业类型集合
	 * @throws BusinessException
	 * 代码重构/
	/*@SuppressWarnings("unchecked")
	public List<ProfessionalType> findList(ProfessionalType entity,String status, Order[] orders) throws BusinessException{
		try {
			StringBuffer hql =  new StringBuffer();
			hql.append("From ProfessionalType t where t.recordStatus=0");
			if(!StringUtil.isNullOrBlank(status)){
				hql.append(" and t.status in ("+status+")");
			}
			if(!StringUtil.isNullOrBlank(entity.getParentIDX()) && !"ROOT_0".equals(entity.getParentIDX())){
				hql.append(" and t.parentIDX = '"+entity.getParentIDX()+"'");
			}
			return (List<ProfessionalType>)daoUtils.find(enableCache(), hql.toString());
//			return (List<ProfessionalType>)this.daoUtils.find(hql.toString());
		} catch (Exception e) {
			ExceptionUtil.process(e,logger);
		}
		return null;
    }*/
	/**
	 * <li>说明：分页查询专业类型
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-8-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchEntity：查询对象；status：业务状态
	 * @return Page分页列表
	 * @throws BusinessException
	 */
	public Page<ProfessionalType> findPageList(SearchEntity<ProfessionalType> searchEntity, String status) throws BusinessException{
		String totalHql = "select count(*) from ProfessionalType t where t.recordStatus=0 ";
		String hql = "From ProfessionalType t where t.recordStatus=0 ";
		StringBuffer awhere =  new StringBuffer();
		if(!StringUtil.isNullOrBlank(status)){
			awhere.append(" and t.status in ("+status+")");
		}
		if(!StringUtil.isNullOrBlank(searchEntity.getEntity().getParentIDX()) 
				&& !"ROOT_0".equals(searchEntity.getEntity().getParentIDX())){
			awhere.append(" and t.parentIDX = '"+searchEntity.getEntity().getParentIDX()+"'");
		}
		totalHql += awhere;
		hql += awhere;
		if(enableCache()){
			return super.cachePageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
		} else {
			return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
		}
    }
	/**
	 * <li>说明：启用
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-10-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids：实体对象主键数组
	 * @return void
	 * @throws BusinessException
	 */
	public void updateStartUse(String[] ids) throws BusinessException{
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String nowDate = df.format(new Date());
		for(int i=0;i<ids.length;i++)
		{
			String sql = " update pjwz_professional_type p set p.status=1,p.update_time=to_date('"+nowDate+"','yyyy-mm-dd hh24:mi:ss') " +
			" where p.idx in (select t.idx from pjwz_professional_type t" +
			" start with t.idx='"+ids[i]+"'" +
			" connect by prior t.parent_idx =  t.idx)";
			this.daoUtils.executeSql(sql);
		}
	}
	/**
	 * <li>说明：作废
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-10-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids：实体对象主键数组
	 * @return void
	 * @throws BusinessException
	 */
	public void updateInvalid(String[] ids) throws BusinessException{
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String nowDate = df.format(new Date());
		for(int i=0;i<ids.length;i++)
		{
			String sql = " update pjwz_professional_type p set p.status=2,p.update_time=to_date('"+nowDate+"','yyyy-mm-dd hh24:mi:ss') " +
			" where p.idx in (select t.idx from pjwz_professional_type t" +
			" start with t.idx='"+ids[i]+"'" +
			" connect by t.parent_idx = prior t.idx)";
			this.daoUtils.executeSql(sql);
		}
		
	}
	/**
	 * <li>说明：级联删除
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-10-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids：实体对象主键数组
	 * @return void
	 * @throws BusinessException
	 */
	public void deleteCascade(String[] ids) throws BusinessException{
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String nowDate = df.format(new Date());
		for(int i=0;i<ids.length;i++)
		{
			String sql = " update pjwz_professional_type p set p.record_status=1,p.update_time=to_date('"+nowDate+"','yyyy-mm-dd hh24:mi:ss')" +
			" where p.idx in (select t.idx from pjwz_professional_type t" +
			" start with t.idx='"+ids[i]+"'" +
			" connect by t.parent_idx = prior t.idx)";
			this.daoUtils.executeSql(sql);
		}
		
	}
	/**
     * <li>说明：重写getBaseComboTree，获取下拉树前台store所需List<HashMap>对象
     * <li>创建人：程锐
     * <li>创建日期：2014-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param req 
     * @return List<HashMap> 下拉树前台store所需List<HashMap>对象
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public List<HashMap> getBaseComboTree(HttpServletRequest req) throws Exception{
		String parentIDX = StringUtil.nvlTrim(req.getParameter("parentIDX"), "ROOT_0");
		String status = StringUtil.nvlTrim(req.getParameter("status"),ProfessionalType.status_start+"");
		String isChecked = req.getParameter("isChecked");
		String hql="from ProfessionalType where parentIDX=? and status in ("+status+") and recordStatus !=1";
		List<ProfessionalType> professionalTypeList = 
			(List<ProfessionalType>)findProByParam(hql, new Object[]{parentIDX});
		List<HashMap> children = new ArrayList<HashMap>();
		for (ProfessionalType type : professionalTypeList) {
			Boolean IsLeaf = this.isLeaf(type.getIdx(),status);
			HashMap nodeMap = new HashMap();
			nodeMap.put("id", type.getIdx());
			nodeMap.put("text", type.getProfessionalTypeName());
			nodeMap.put("leaf", IsLeaf);
			nodeMap.put("isLeaf", type.getIsLeaf());
			nodeMap.put("status", type.getStatus());
			nodeMap.put("parentIDX", type.getParentIDX());
			nodeMap.put("professionalTypeID", type.getProfessionalTypeID());
			nodeMap.put("proSeq", type.getProSeq());
			if(!StringUtil.isNullOrBlank(isChecked))
				nodeMap.put("checked", false);
			children.add(nodeMap);
		}
		return children;
    }
	/**
	 * 
	 * <li>说明：查询专业类型列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param start
	 * @param limit
	 * @return 专业类型列表
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Page queryProfessionalList(int start, int limit) throws Exception{
		ProfessionalType type = new ProfessionalType();
		type.setStatus(ProfessionalType.status_start);
        // 按照专业类型名称排序
        Order order = org.hibernate.criterion.Order.asc("professionalTypeName");
		SearchEntity<ProfessionalType> searchEntity = new SearchEntity<ProfessionalType>(type, start * limit, limit, new Order[]{order});
		Page page = findPageList(searchEntity);
		List<ProfessionalTypeBean> beanList = new ArrayList<ProfessionalTypeBean>();
		// beanList = BeanUtils.copyListToList(ProfessionalTypeBean.class, page.getList());
		
		for (ProfessionalType proType : (List<ProfessionalType>)page.getList()) {
			beanList.add(new ProfessionalTypeBean(proType.getIdx(), proType.getProfessionalTypeName(), proType.getProSeq()));
		}
		
		return new Page(page.getTotal(),beanList);
	}
}