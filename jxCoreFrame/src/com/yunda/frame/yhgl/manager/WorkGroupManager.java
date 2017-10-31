package com.yunda.frame.yhgl.manager;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmpgroup;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmEmpposition;
import com.yunda.frame.yhgl.entity.OmGroup;
import com.yunda.frame.yhgl.entity.OmPosition;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 工作组业务类
 * <li>创建人：谭诚
 * <li>创建日期：2013-11-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="workGroupManager")
public class WorkGroupManager extends JXBaseManager <OmGroup,OmGroup>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** 工作组查询接口 */
	@Resource(name="omGroupManager")
	private IOmGroupManager omGroupManager;
	
	/** 岗位查询接口 */
	@Resource(name="omPositionManager")
	private IOmPositionManager omPositionManager;
	
	/** 岗位业务类 */
	@SuppressWarnings("unused")
	@Resource(name="positionManager")
	private PositionManager positionManager;
	
	/** 工作组-岗位业务类 */
	@Resource(name="groupposiManager")
	private GroupposiManager groupposiManager;
	
	/** 人员查询接口 */
	@Resource(name="omEmployeeManager")
	private IOmEmployeeManager omEmployeeManager;
	
	/** 人员-工作组业务类 */
	@Resource(name="empgroupManager")
	private EmpgroupManager empgroupManager;
	
	/** 人员-岗位业务类 */
	@Resource(name="empPositionManager")
	private EmpPositionManager empPositionManager;
	
	/**
	 * <br/><li>说明： 查找入参groupid的下一级工作组信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-12-6
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param groupId 工作组Id
	 * @return 工作组实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmGroup> findChildsById(Long groupId) {
		String hql = "from OmGroup where parentgroupid = ?";
		hql = hql.replace("?", String.valueOf(groupId));
		return daoUtils.getHibernateTemplate().find(hql);

	}
	
	/**
	 * 
	 * <li>说明：获取当前机构的下级
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-10-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public List <Map> getChildNodes(Long nodeid,String nodetype) throws BusinessException{
		List <OmGroup> childNodeGopList = null; //工作组
		List <OmPosition> childNodePosList = null;     //组织机构直属岗位
		List <OmEmployee> childNodeEmpList = null;     //组织机构直属人员
		List <Map> list = new ArrayList<Map>();        //实例化一个空的List,传入后需函数中装载数据	
		if(nodetype.equals("gop") && nodeid == null){
			//当nodeid为空时，查询根层工作组
			return buildExtTreeModeByGroup(omGroupManager.findRoot(),list); //如groupid==null，则调用接口获取工作组的根
		} else if(nodetype.equals("gop")) {
			childNodeGopList = findChildsById(nodeid);  //根据groupid,调用接口查询获取其下级工作组
			list = buildExtTreeModeByGroup(childNodeGopList,list);
		}
		//因工作组下可能有下级工作组，也可能会有岗位和人员，所以此处将通过查询接口获取下级岗位及直属人员
		if(nodetype.equals("gop")||nodetype.equals("pos")){
			if(nodetype.equals("gop")){
				childNodePosList = omPositionManager.findPertainToWorkGroup(nodeid);   //根据groupid,调用接口查询获取其直属岗位
				list = buildExtTreeModeByPos(childNodePosList,list);
				childNodeEmpList = omEmployeeManager.findNoPositionByGroupId(nodeid);	//根据groupid,调用接口查询获取其直属人员
				if(childNodeEmpList!=null&&childNodeEmpList.size()>0)
				list = buildExtTreeModeByEmp(childNodeEmpList, list, nodeid); //因在不同的工作组或者岗位下，可以添加相同的人员，这些人员在同一个树上出现，如使用人员id作为tree的id，会产生bug，所以将人员的上级节点id，即工作组或者岗位id作为参数传入，与人员id一起作为tree的id
			}
			if(nodetype.equals("pos")){
				childNodePosList = omPositionManager.findChildPosition(nodeid);   //根据positionid,调用接口查询获取其直属岗位
				list = buildExtTreeModeByPos(childNodePosList,list);
				childNodeEmpList = omEmployeeManager.findByPosition(nodeid);		//根据positionid,调用接口查询获取其直属人员
				if(childNodeEmpList!=null&&childNodeEmpList.size()>0)
				list = buildExtTreeModeByEmp(childNodeEmpList, list, nodeid); //因在不同的工作组或者岗位下，可以添加相同的人员，这些人员在同一个树上出现，如使用人员id作为tree的id，会产生bug，所以将人员的上级节点id，即工作组或者岗位id作为参数传入，与人员id一起作为tree的id
			}
		}
		return list;
	}
	
	
	/**
	 * <li>说明：根据数据，构建工作组树（工作组部分）
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param list 工作组数据集合
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	private List <Map> buildExtTreeModeByGroup(List<OmGroup> list, List <Map> gopList){
		if (list == null) return null;
		List<Map> children = gopList;
		Map node = null;
		for(OmGroup og : list){
			node = new LinkedHashMap();
			node.put("id", "g_"+String.valueOf(og.getGroupid())); //工作组ID
			node.put("text", og.getGroupname());    //工作组名称
			//如果当前工作组没有下级工作组，则进一步检索其是否存在下级岗位或人员
			if(!StringUtil.isNullOrBlank(og.getIsleaf())&&"N".equals(og.getIsleaf().toUpperCase())){
				node.put("leaf", false); //当前工作组含有子项
			} else {
				//查询其是否存在下级岗位
				List <OmPosition> childPosition = omPositionManager.findPertainToWorkGroup(og.getGroupid());
				if(childPosition!=null&&childPosition.size()>0){
					node.put("leaf", false); //如果存在下级岗位，则该工作组可以展开
				} else {
					//如当前工作组没有下级岗位，则继续查看其是否拥有直属人员
					List <OmEmployee> childEmployee = omEmployeeManager.findNoPositionByGroupId(og.getGroupid());
					if(childEmployee!=null&&childEmployee.size()>0){
						node.put("leaf", false);
					} else {
						node.put("leaf", true);//当前工作组下即无岗位又无人员
					}
				}
			}
			node.put("iconCls", "silver2Icon"); //根据组织等级构建树的节点图标
			//=================
			node.put("groupid", og.getGroupid());  //工作组ID
			node.put("parentgroupid", og.getParentgroupid()); //父工作组
			node.put("orgid", og.getOrgid()); //隶属机构编号
			node.put("grouplevel", og.getGrouplevel()); //工作组层次
			node.put("groupname", og.getGroupname());//工作组名称
			node.put("groupdesc", og.getGroupdesc());//工作组描述
			node.put("grouptype", og.getGrouptype());//工作组类型
			node.put("groupseq", og.getGroupseq());//工作组路径序列
			node.put("startdate", og.getStartdate());//工作组有效开始日期
			node.put("enddate", og.getEnddate());//工作组有效截止日期
			node.put("groupstatus", og.getGroupstatus());//工作组状态
			node.put("manager", og.getManager());//负责人
			node.put("createtime", og.getCreatetime());//创建时间
			node.put("lastupdate", og.getLastupdate());//最近更新时间
			node.put("updator", og.getUpdator());//最近更新人员
			node.put("isleaf", og.getIsleaf());//是否叶子节点
			node.put("subcount", og.getSubcount());//子节点数
			node.put("nodetype", "gop");//节点类型（工作组or岗位）
			children.add(node);
		}
		return children;
	}
	
	/**
	 * <li>说明：根据数据，构建工作组树（岗位部分）
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-10-31
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	private List <Map> buildExtTreeModeByPos(List <OmPosition> list, List <Map> posList){
		if (list == null) return null;
		List<Map> children = posList;
		Map node = null;
		for(OmPosition pos : list){
			node = new LinkedHashMap();
			node.put("id", "p_"+pos.getPositionid());  //岗位ID
			node.put("text", pos.getPosiname());  //岗位名称
			if(!StringUtil.isNullOrBlank(pos.getIsleaf())&&"n".equals(pos.getIsleaf().toLowerCase())){
				node.put("leaf",false);
			} else {
				//查询其下是否存在人员
				List <OmEmployee> childEmployee = omEmployeeManager.findByPosition(pos.getPositionid());
				if(childEmployee!=null&&childEmployee.size()>0){
					node.put("leaf", false);
				} else {
					node.put("leaf", true); //当前机构下无人员
				}
			}
//			node.put("leaf", !StringUtil.isNullOrBlank(pos.getIsleaf())&&"y".equals(pos.getIsleaf().toLowerCase())?true:false);//是否叶子节点
			node.put("iconCls", "buildingIcon");			  //岗位图标
			//=======================//
			node.put("positionid", pos.getPositionid());//岗位ID
			node.put("dutyid", pos.getDutyid());//职务ID
			node.put("manaposi", pos.getManaposi());//上级岗位
			node.put("posicode", pos.getPosicode());//岗位代码
			node.put("posiname", pos.getPosiname());//岗位名称
			node.put("posilevel", pos.getPosilevel());//岗位层次
			node.put("orgid", pos.getOrgid());//组织机构ID
			node.put("positionseq", pos.getPositionseq());//岗位序列
			node.put("positype", pos.getPositype());//岗位类别
			node.put("isleaf", pos.getIsleaf());//是否叶子节点
			node.put("nodetype", "pos");//存入标识,该节点是岗位
			//=======================//
			children.add(node);
		}
		return children;
	}
	
	/**
	 * <li>说明：根据数据，构建工作组树（人员部分）
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-10-31
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	private List <Map> buildExtTreeModeByEmp(List <OmEmployee> list, List<Map> empList, Long pnodeid){
		if(list == null) return null;
		List <Map> children = empList;
		Map node = null;
		for(OmEmployee emp : list){
			node = new LinkedHashMap();
			node.put("id", pnodeid+"_"+emp.getEmpid());
			node.put("text", emp.getEmpname());
			node.put("leaf", true);
			//对于性别图标，如性别未指定或者既不是男(m)又不是女(f)则显示userIcon图标，否则显示对应图标
			node.put("iconCls", 
					StringUtil.isNullOrBlank(emp.getGender())||(!"m".equals(emp.getGender())&&!"f".equals(emp.getGender()))?
							"userIcon":
								("m".equals(emp.getGender())?"userSuitIcon":"userfemaleIcon"));
			//=======================//
			node.put("empid", emp.getEmpid());//人员编号
			node.put("empcode", emp.getEmpcode());//人员代码
			node.put("operatorid", emp.getOperatorid());//操作员编号
			node.put("userid", emp.getUserid());//操作员登录号
			node.put("empname", emp.getEmpname());//人员姓名
			node.put("realname", emp.getRealname());//人员全名
			node.put("gender", emp.getGender());//性别
			node.put("position", emp.getPosition());//基本岗位
			node.put("empstatus", emp.getEmpstatus());//状态
			node.put("orgid", emp.getOrgid());//主机构编号
			node.put("nodetype", "emp");//存入标识,该节点是人员
			//=======================//			
			children.add(node);
		}
		return children;
	}
	
	/**
	 * <li>说明：新增/编辑工作组信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-18
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param T t 实体对象
	 * @return void 
	 * @throws BusinessException, NoSuchFieldException
	 */	
	public void saveOrUpdate(OmGroup og) throws BusinessException, NoSuchFieldException {
		OmGroup pGroup = null;
		String findPNodeHql = "from OmGroup where groupid = " + og.getParentgroupid();
		if(og.getParentgroupid()!=null){
			pGroup = (OmGroup)this.daoUtils.find(findPNodeHql).iterator().next(); //获取父工作组对象
		}
		Date now = new Date();
		if(og.getGroupid()==null){
			og.setCreatetime(now);//设置创建时间
			og.setIsleaf("y"); //新增节点均为叶子节点
			og.setSubcount(0L);//新增的岗位没有下级，所以其子节点数为0
			
			if(pGroup!=null){
				pGroup.setIsleaf("n");
				pGroup.setSubcount((pGroup.getSubcount()==null?0:pGroup.getSubcount())+1); //父节点的子节点总数+1
				og.setGrouplevel((pGroup.getGrouplevel()==null?0:pGroup.getGrouplevel())+1); //子节点的层级=父节点层级+1
				daoUtils.saveOrUpdate(pGroup); //更新父节点
			} else {
				og.setGrouplevel(1L); //当前工作组没有上级工作组时，它的Level为1
			}
		}
		AcOperator acOperator = SystemContext.getAcOperator(); //获取当前登录操作员
		og.setUpdator(acOperator.getOperatorid()); //最近更新人员
		og.setLastupdate(now);//设置最后更新时间
		this.daoUtils.getHibernateTemplate().saveOrUpdate(og);
		//因当前工作组id需在保存后才产生，所以保存后再次更新其Seq
		if(og.getParentgroupid()!=null){
			og.setGroupseq(pGroup.getGroupseq()+og.getGroupid()+"."); //当前工作组的seq = 父节点seq+当前工作组id+"."
		} else {
			og.setGroupseq("."+og.getGroupid()+".");
		}
		this.daoUtils.getHibernateTemplate().saveOrUpdate(og);
	}
	
	/**
	 * <li>说明：物理删除工作组时，级联删除相关子项，并更新关联数据
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 人员id数组
	 * @throws 抛出异常列表
	 */
	public void deleteByIds(Serializable... ids) throws BusinessException {
		if(ids == null) return;
		try {
			if(ids == null) return;
			Long upperGroupid = null; //上级工作组ID
			//循环删除选中工作组
			for(Serializable id : ids){
				if(StringUtil.isNullOrBlank(String.valueOf(id))) continue;
				Long groupid = Long.valueOf(String.valueOf(id));
				OmGroup groupObj = omGroupManager.getModelById(groupid); //根据ID获取需要删除的机构实体
				if(groupObj == null) continue;
				upperGroupid = groupObj.getParentgroupid(); //记录待删除工作组的上级工作组ID
				iterationDeleteChildGroup(groupObj); //调用递归方法删除所有子工作组
				/* 更新当前删除工作组的上层工作组的subcount */
				if(upperGroupid == null) continue;
				OmGroup pgroup = omGroupManager.getModelById(upperGroupid); //获取上层工作组实体
				String hql = "from OmGroup where parentgroupid = " + upperGroupid;
				List <OmGroup> plist = this.daoUtils.find(hql); //根据上层工作组ID查询其子工作组总数
				if( plist == null || plist.size()<1 ){
					pgroup.setSubcount(0L);
					pgroup.setIsleaf("y");
				} else {
					pgroup.setSubcount(Long.valueOf(String.valueOf(plist.size())));
					pgroup.setIsleaf("n");
				}
				this.daoUtils.update(pgroup); //更新subcount
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}
	
	/**
	 * <li>说明：递归方法，根据入参的工作组，迭代删除其下属所有子工作组
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-4
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param group 工作组实体
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	private void iterationDeleteChildGroup(OmGroup group) throws BusinessException{
		String hql = "from OmGroup where parentgroupid = " + group.getGroupid();
		List <OmGroup> childGroup = this.daoUtils.find(hql); //获取入参工作组的下级子工作组
		if(childGroup==null||childGroup.size()<1) {
			groupposiManager.deleteByPositionId(group.getGroupid());//【调用方法删除工作组下的岗位及工作组-岗位的关联信息】
			empgroupManager.deleteByGroupId(group.getGroupid());   //【调用方法删除工作组下所有直属人员的关联关系】
			this.daoUtils.getHibernateTemplate().delete(group); //【执行删除工作组】
			return; //如果未找到子项，跳出递归
		}
		for(OmGroup _o : childGroup){
			iterationDeleteChildGroup(_o); //递归调用，继续向下层查找
		}
		groupposiManager.deleteByPositionId(group.getGroupid());//【调用方法删除工作组下的岗位及工作组-岗位的关联信息】
		empgroupManager.deleteByGroupId(group.getGroupid());   //【调用方法删除工作组下所有直属人员的关联关系】
		this.daoUtils.getHibernateTemplate().delete(group); //【执行删除工作组】
	}
	
	/**
	 * <li>说明：查询入参中各工作组的子工作组groupid，并拼装为逗号分隔的字符串后返回
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param orgids 逗号分隔的orgid字符串
	 * @return 包含入参orgid的各机构及其下属机构的orgid字符串
	 * @throws 抛出异常列表
	 */
	public String getChildGroupIds(String groupids){
		StringBuffer childOrgidAry = null;
		if(!StringUtil.isNullOrBlank(groupids)) {
			childOrgidAry = new StringBuffer();
			//使用oracle内置函数，递归查询入参中各工作组的下属各级工作组的groupid
			String sql = "SELECT GROUPID FROM OM_GROUP START WITH GROUPID IN ("+groupids+") CONNECT BY PRIOR groupid = parentgroupid";
			List _t = daoUtils.executeSqlQuery(sql);
			for(Object obj : _t){
				childOrgidAry.append(obj.toString()).append(",");
			}
			return childOrgidAry.toString().substring(0, childOrgidAry.toString().lastIndexOf(","));
		}
		return null;
	}
	
	
	/**
	 * <li>说明：下级工作组列表查询
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param whereList 查询参数
	 * @param orderList 排序参数
	 * @param start 起始条数
	 * @param limit 每页条数
	 * @return 人员信息分页列表
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unused")
	public Page findGroupList(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer();
		@SuppressWarnings("unused")
		String sql = "SELECT gop.groupid as \"groupid\", gop.parentgroupid as \"parentgroupid\", " +
				"gop.orgid as \"orgid\", gop.grouplevel as \"grouplevel\", gop.groupname as \"groupname\", " +
				"gop.groupdesc as \"groupdesc\", gop.grouptype as \"grouptype\", gop.groupseq as \"groupseq\", " +
				"gop.startdate as \"startdate\", gop.enddate as \"enddate\", gop.groupstatus as \"groupstatus\", " +
				"gop.manager as \"manager\", gop.createtime as \"createtime\", gop.lastupdate as \"lastupdate\", " +
				"gop.updator as \"updator\", gop.isleaf as \"isleaf\", gop.subcount as \"subcount\", emp.empname as \"empname\" " +
				"from Om_Group gop left join Om_Employee emp on gop.manager = emp.empid where 1=1";
		String totalsql = "select count(*) as \"rowcount\" from (?)";
		//拼接查询参数
		if (whereList != null && whereList.size() > 0) {
			for (Condition condition : whereList) {
				searchParam.append(" and gop.");
				searchParam.append(condition.getPropName()).append(" ");
				String sign = "";
				//如果匹配参数为空或者为"1" , 则转为 "=" 条件 
				if(StringUtil.isNullOrBlank(String.valueOf(condition.getCompare()))||"1".equals(String.valueOf(condition.getCompare()))){
					sign = Condition.getCompare(1) + " '?' " ;  //转为 "=" 条件 
				} else if ("8".equals(String.valueOf(condition.getCompare()))){
					sign = Condition.getCompare(8) + " '%?%' "; //转为 "like" 条件 
				} else if ("21".equals(String.valueOf(condition.getCompare()))){
					sign = Condition.getCompare(21);            //转为 is null 条件
				}
				sign = sign.replace("?", String.valueOf(condition.getPropValue()));
				searchParam.append(sign);
			}
		}
		//拼接排序参数
		if(orderList != null && orderList.size()>0) {
			for(Order order : orderList){
				searchParam.append(" order by gop.").append(order.toString());
			}
		}
		return super.findPageList(totalsql.replace("?", sql.concat(searchParam.toString())), sql.concat(searchParam.toString()), start, limit, null,null);
	}
	
	/**
	 * <li>说明：根据工作组ID，获取其实体（当前工作组表单使用）
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-19
	 * <li>修改人： 
	 * <li>修改日期： executeSqlQueryEntity
	 * <li>修改内容：
	 */
	@SuppressWarnings("unchecked")
	public OmGroup getModelById(Serializable id) throws BusinessException {
		String sql = "SELECT gop.groupid as \"groupid\", gop.parentgroupid as \"parentgroupid\", " +
		"gop.orgid as \"orgid\", gop.grouplevel as \"grouplevel\", gop.groupname as \"groupname\", " +
		"gop.groupdesc as \"groupdesc\", gop.grouptype as \"grouptype\", gop.groupseq as \"groupseq\", " +
		"gop.startdate as \"startdate\", gop.enddate as \"enddate\", gop.groupstatus as \"groupstatus\", " +
		"gop.manager as \"manager\", gop.createtime as \"createtime\", gop.lastupdate as \"lastupdate\", " +
		"gop.updator as \"updator\", gop.isleaf as \"isleaf\", gop.subcount as \"subcount\", emp.empname as \"empname\" " +
		"from Om_Group gop left join Om_Employee emp on gop.manager = emp.empid where groupid = " + id;
		List <Object []> list = this.daoUtils.executeSqlQuery(sql);
		OmGroup group = new OmGroup();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(Object [] obj : list) {
			group.setGroupid(Long.valueOf(String.valueOf(obj[0])));
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[1])))
				group.setParentgroupid(Long.valueOf(String.valueOf(obj[1])));
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[2])))
				group.setOrgid(Long.valueOf(String.valueOf(obj[2])));
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[3])))
				group.setGrouplevel(Long.valueOf(String.valueOf(obj[3])));
			group.setGroupname(String.valueOf(obj[4]));
			group.setGroupdesc(StringUtil.isNullOrBlank(String.valueOf(obj[5]))?"":String.valueOf(obj[5]));
			group.setGrouptype(StringUtil.isNullOrBlank(String.valueOf(obj[6]))?"":String.valueOf(obj[6]));
			group.setGroupseq(StringUtil.isNullOrBlank(String.valueOf(obj[7]))?"":String.valueOf(obj[7]));
			group.setGroupstatus(StringUtil.isNullOrBlank(String.valueOf(obj[10]))?"":String.valueOf(obj[10]));
			group.setManager(StringUtil.isNullOrBlank(String.valueOf(obj[11]))?"":String.valueOf(obj[11]));
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[14])))
				group.setUpdator(Long.valueOf(String.valueOf(obj[14])));
			group.setIsleaf(StringUtil.isNullOrBlank(String.valueOf(obj[15]))?"":String.valueOf(obj[15]));
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[16])))
				group.setSubcount(Long.valueOf(String.valueOf(obj[16])));
			group.setEmpname(StringUtil.isNullOrBlank(String.valueOf(obj[17]))?"":String.valueOf(obj[17]));
			try {
				if(!StringUtil.isNullOrBlank(String.valueOf(obj[8])))  group.setStartdate(sdf.parse(String.valueOf(obj[8])));
				if(!StringUtil.isNullOrBlank(String.valueOf(obj[9])))  group.setEnddate(sdf.parse(String.valueOf(obj[9])));
				if(!StringUtil.isNullOrBlank(String.valueOf(obj[12]))) group.setCreatetime(sdf.parse(String.valueOf(obj[12])));
				if(!StringUtil.isNullOrBlank(String.valueOf(obj[13]))) group.setLastupdate(sdf.parse(String.valueOf(obj[13])));
			} catch (ParseException e) {
				e.printStackTrace();
			} 
		}
		return group;
	}
	
	/**
	 * 
	 * <li>说明：获取当前工作组的下级
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param nodeid 节点id
	 * @param nodetype 节点类型
	 * @param widgetType 控件类型
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public List <Map> getChildNodesForWidget(Long nodeid,String nodetype, String widgetType, String empid) throws BusinessException{
		List <OmGroup> childNodeGopList = null; //工作组
		List <OmPosition> childNodePosList = null;     //组织机构直属岗位
		List <OmEmpgroup> temp = null;     
		List <OmEmpposition> temp2 = null;
		List <Map> list = new ArrayList<Map>();        //实例化一个空的List,传入后需函数中装载数据	
		if(!StringUtil.isNullOrBlank(widgetType)&&("1".equals(widgetType)||"2".equals(widgetType))){
			if("1".equals(widgetType)&&!StringUtil.isNullOrBlank(empid)){
				//获取该人员当前所属的工作组
				temp = empgroupManager.findEmpGroupEntity(Long.valueOf(empid));
			}
			if(nodetype.equals("gop") && nodeid == null){
				//当nodeid为空时，查询根层工作组
				return buildGroupTreeByWidget(omGroupManager.findRoot(),list, widgetType,temp); //如groupid==null，则调用接口获取工作组的根
			} else if(nodetype.equals("gop")) {
				childNodeGopList = findChildsById(nodeid);  //根据groupid,调用接口查询获取其下级工作组
				list = buildGroupTreeByWidget(childNodeGopList,list, widgetType,temp);
			}
		}
		if(!StringUtil.isNullOrBlank(widgetType)&&"2".equals(widgetType)){
			//因工作组下可能有下级工作组，也可能会有岗位和人员，所以此处将通过查询接口获取下级岗位及直属人员
			if(nodetype.equals("gop")||nodetype.equals("pos")){
				if(nodetype.equals("gop")){
					childNodePosList = omPositionManager.findPertainToWorkGroup(nodeid);   //根据groupid,调用接口查询获取其直属岗位
					list = buildPosiTreeByWidget(childNodePosList,list, widgetType,temp2);
				}
				if(nodetype.equals("pos")){
					if(!StringUtil.isNullOrBlank(empid)){
						temp2 = empPositionManager.findEmpPosiEntity(Long.valueOf(empid), "n"); //获取当前人员所属的岗位
					}
					childNodePosList = omPositionManager.findChildPosition(nodeid);   //根据positionid,调用接口查询获取其直属岗位
					list = buildPosiTreeByWidget(childNodePosList,list, widgetType, temp2);
				}
			}
		}
		return list;
	}
	
	/**
	 * <li>说明：根据数据，构建工作组树（工作组部分）
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param list 工作组数据集合
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	private List <Map> buildGroupTreeByWidget(List<OmGroup> list, List <Map> gopList, String widgetType, List <OmEmpgroup>temp){
		if (list == null) return null;
		List<Map> children = gopList;
		Map node = null;
		for(OmGroup og : list){
			node = new LinkedHashMap();
			node.put("id", String.valueOf(og.getGroupid())); //工作组ID
			node.put("text", og.getGroupname());    //工作组名称
			//构建工作组Node时，检查工作组下是否已存在指定工作组节点信息，已存在则disabled，让其不可选
			if(!StringUtil.isNullOrBlank(widgetType)&&"1".equals(widgetType)){
				if(temp!=null&&temp.size()>0){
					for(OmEmpgroup oeg:temp){
						//如果当前工作组下已经存在该人员的关联信息，则不显示该节点
						if(String.valueOf(og.getGroupid()).equals(String.valueOf(oeg.getId().getGroupid()))){
							node.put("disabled", true);
							break;
						}
					}
				}
			}
			else if(!StringUtil.isNullOrBlank(widgetType)&&"2".equals(widgetType)){
				node.put("disabled", true);
			}
			//如果当前工作组没有下级工作组，则进一步检索其是否存在下级岗位或人员
			if(!StringUtil.isNullOrBlank(og.getIsleaf())&&"N".equals(og.getIsleaf().toUpperCase())){
				node.put("leaf", false); //当前工作组含有子项
			} else {
				if(!StringUtil.isNullOrBlank(widgetType)&&"2".equals(widgetType)){
					//查询其是否存在下级岗位
					List <OmPosition> childPosition = omPositionManager.findPertainToWorkGroup(og.getGroupid());
					if(childPosition!=null&&childPosition.size()>0){
						node.put("leaf", false); //如果存在下级岗位，则该工作组可以展开
					} else {
						node.put("leaf", true); //当前机构没有子项
					}
				} else {
					node.put("leaf", true); //当前机构没有子项
				}
			}
			node.put("iconCls", "silver2Icon"); //根据组织等级构建树的节点图标
			//=================
			node.put("groupid", og.getGroupid());  //工作组ID
			node.put("parentgroupid", og.getParentgroupid()); //父工作组
			node.put("orgid", og.getOrgid()); //隶属机构编号
			node.put("grouplevel", og.getGrouplevel()); //工作组层次
			node.put("groupname", og.getGroupname());//工作组名称
			node.put("groupdesc", og.getGroupdesc());//工作组描述
			node.put("grouptype", og.getGrouptype());//工作组类型
			node.put("groupseq", og.getGroupseq());//工作组路径序列
			node.put("startdate", og.getStartdate());//工作组有效开始日期
			node.put("enddate", og.getEnddate());//工作组有效截止日期
			node.put("groupstatus", og.getGroupstatus());//工作组状态
			node.put("manager", og.getManager());//负责人
			node.put("createtime", og.getCreatetime());//创建时间
			node.put("lastupdate", og.getLastupdate());//最近更新时间
			node.put("updator", og.getUpdator());//最近更新人员
			node.put("isleaf", og.getIsleaf());//是否叶子节点
			node.put("subcount", og.getSubcount());//子节点数
			node.put("nodetype", "gop");//节点类型（工作组or岗位）
			children.add(node);
		}
		return children;
	}
	
	/**
	 * <li>说明：根据数据，构建组织机构树（岗位部分）
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-10-31
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	private List <Map> buildPosiTreeByWidget(List <OmPosition> list, List <Map> posList, String widgetType, List <OmEmpposition> temp2){
		if (list == null) return null;
		List<Map> children = posList;
		Map node = null;
		for(OmPosition pos : list){
			node = new LinkedHashMap();
			node.put("id", pos.getPositionid());  //岗位ID
			node.put("text", pos.getPosiname());  //岗位名称
			if(!StringUtil.isNullOrBlank(pos.getIsleaf())&&"n".equals(pos.getIsleaf().toLowerCase())){
				node.put("leaf",false);
			} else {
				node.put("leaf", true);
			}
			//构建工作组Node时，检查工作组下是否已存在指定工作组节点信息，已存在则disabled，让其不可选
			if(!StringUtil.isNullOrBlank(widgetType)&&"2".equals(widgetType)){
				if(temp2!=null&&temp2.size()>0){
					for(OmEmpposition oep:temp2){
						//如果当前工作组下已经存在该人员的关联信息，则不显示该节点
						if(String.valueOf(pos.getPositionid()).equals(String.valueOf(oep.getId().getPositionid()))){
							node.put("disabled", true);
							break;
						}
					}
				}
			}
			node.put("iconCls", "buildingIcon");			  //岗位图标
			//=======================//
			node.put("positionid", pos.getPositionid());//岗位ID
			node.put("dutyid", pos.getDutyid());//职务ID
			node.put("manaposi", pos.getManaposi());//上级岗位
			node.put("posicode", pos.getPosicode());//岗位代码
			node.put("posiname", pos.getPosiname());//岗位名称
			node.put("posilevel", pos.getPosilevel());//岗位层次
			node.put("orgid", pos.getOrgid());//组织机构ID
			node.put("positionseq", pos.getPositionseq());//岗位序列
			node.put("positype", pos.getPositype());//岗位类别
			node.put("isleaf", pos.getIsleaf());//是否叶子节点
			node.put("nodetype", "pos");//存入标识,该节点是岗位
			//=======================//
			children.add(node);
		}
		return children;
	}
	
	/**
	 * <li>说明：根据查询条件，查询匹配的工作组信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param whereList 查询参数
	 * @param orderList 排序参数
	 * @param start 起始条数
	 * @param limit 每页条数
	 * @return 工作组信息分页列表
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unused")
	public Page findGroupListByEmployee(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer();
		@SuppressWarnings("unused")
		String sql = "select '' as \"empid\", emp.empname as \"empname\", eg.groupid as \"groupid\"," +
				"gop.groupname as \"groupname\" from om_empgroup eg left join om_employee emp on eg.empid = emp.empid " +
				"left join om_group gop on eg.groupid = gop.groupid where eg.empid = ? " ;
		String totalsql = "select count(*) as \"rowcount\" from (?)";
		//拼接查询参数
		if (whereList != null && whereList.size() > 0) {
			for (Condition condition : whereList) {
				if(!StringUtil.isNullOrBlank(condition.getPropName())&&"empid".equals(condition.getPropName())){
					sql = sql.replace("?", String.valueOf(condition.getPropValue()));
				} else {
					searchParam.append(" and ");
					searchParam.append(condition.getPropName()).append(" ");
					String sign = "";
					//如果匹配参数为空或者为"1" , 则转为 "=" 条件 
					if(StringUtil.isNullOrBlank(String.valueOf(condition.getCompare()))||"1".equals(String.valueOf(condition.getCompare()))){
						sign = Condition.getCompare(1) + " '?' " ;  //转为 "=" 条件 
					} else if ("8".equals(String.valueOf(condition.getCompare()))){
						sign = Condition.getCompare(8) + " '%?%' "; //转为 "like" 条件 
					} else if ("21".equals(String.valueOf(condition.getCompare()))){
						sign = Condition.getCompare(21);            //转为 is null 条件
					}
					sign = sign.replace("?", String.valueOf(condition.getPropValue()));
					searchParam.append(sign);
				}
			}
		}
		//拼接排序参数
		if(orderList != null && orderList.size()>0) {
			for(Order order : orderList){
				searchParam.append(" order by ").append(order.toString());
			}
		}
		return super.findPageList(totalsql.replace("?", sql.concat(searchParam.toString())), sql.concat(searchParam.toString()), start, limit, null,null);
	}
	
	/**
	 * <li>说明：返回已配置为当前角色的工作组列表
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param whereList 查询参数
	 * @param orderList 排序参数
	 * @param start 起始条数
	 * @param limit 每页条数
	 * @return 人员信息分页列表
	 * @throws 抛出异常列表
	 */
	public Page findGroupListByRole(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer();
		String sql = " from OmGroup group, OmPartyrole partyrole where group.groupid = partyrole.id.partyid and partyrole.id.partytype = 'workgroup' and partyrole.id.roleid = '?' ";
		//拼接查询参数
		if (whereList != null && whereList.size() > 0) {
			for (Condition condition : whereList) {
				if(!StringUtil.isNullOrBlank(condition.getPropName())&&"roleid".equals(condition.getPropName())){
					sql = sql.replace("?", String.valueOf(condition.getPropValue()));
				} 
				else {
					//构建查询条件
					searchParam.append(" and ");
					searchParam.append(condition.getPropName()).append(" ");
					String sign = StringUtil.isNullOrBlank(String.valueOf(condition.getCompare()))||"1".equals(String.valueOf(condition.getCompare()))
					? Condition.getCompare(1)+" '?' " : Condition.getCompare(8)+" '%?%' ";
					sign = sign.replace("?", String.valueOf(condition.getPropValue()));
					searchParam.append(sign);
				}
			}
		}
		//拼接排序参数
		if(orderList != null && orderList.size()>0) {
			for(Order order : orderList){
				searchParam.append(" order by ").append(order.toString());
			}
		}
		sql = sql.concat(searchParam.toString()); //拼装查询条件
		String totalHql = "select count(*) ".concat(sql); //构建总条数统计语句
		String hql = "select group ".concat(sql); //构建数据查询语句
		return super.findPageList(totalHql, hql, start, limit);
	}
	
	/**
	 * <li>说明：返回未配置为当前角色的工作组列表
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param whereList 查询参数
	 * @param orderList 排序参数
	 * @param start 起始条数
	 * @param limit 每页条数
	 * @return 人员信息分页列表
	 * @throws 抛出异常列表
	 */
	public Page findGroupListByRole2(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer();
		String sql = " from OmGroup group where group.groupid not in (select id.partyid from OmPartyrole where id.partytype = 'workgroup' and id.roleid = '?')";
		//拼接查询参数
		if (whereList != null && whereList.size() > 0) {
			for (Condition condition : whereList) {
				if(!StringUtil.isNullOrBlank(condition.getPropName())&&"roleid".equals(condition.getPropName())){
					sql = sql.replace("?", String.valueOf(condition.getPropValue()));
				} 
				else {
					//构建查询条件
					searchParam.append(" and ");
					searchParam.append(condition.getPropName()).append(" ");
					String sign = StringUtil.isNullOrBlank(String.valueOf(condition.getCompare()))||"1".equals(String.valueOf(condition.getCompare()))
					? Condition.getCompare(1)+" '?' " : Condition.getCompare(8)+" '%?%' ";
					sign = sign.replace("?", String.valueOf(condition.getPropValue()));
					searchParam.append(sign);
				}
			}
		}
		//拼接排序参数
		if(orderList != null && orderList.size()>0) {
			for(Order order : orderList){
				searchParam.append(" order by ").append(order.toString());
			}
		}
		sql = sql.concat(searchParam.toString()); //拼装查询条件
		String totalHql = "select count(*) ".concat(sql); //构建总条数统计语句
		String hql = "select group ".concat(sql); //构建数据查询语句
		return super.findPageList(totalHql, hql, start, limit);
	}


	public EmpgroupManager getEmpgroupManager() {
		return empgroupManager;
	}


	public void setEmpgroupManager(EmpgroupManager empgroupManager) {
		this.empgroupManager = empgroupManager;
	}


	public EmpPositionManager getEmpPositionManager() {
		return empPositionManager;
	}


	public void setEmpPositionManager(EmpPositionManager empPositionManager) {
		this.empPositionManager = empPositionManager;
	}

	public GroupposiManager getGroupposiManager() {
		return groupposiManager;
	}

	public void setGroupposiManager(GroupposiManager groupposiManager) {
		this.groupposiManager = groupposiManager;
	}

}
