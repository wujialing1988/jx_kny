package com.yunda.frame.yhgl.manager;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmGroupposi;
import com.yunda.frame.yhgl.entity.OmPosition;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 岗位业务类
 * <li>创建人：谭诚
 * <li>创建日期：2013-10-31
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="positionManager")
public class PositionManager extends JXBaseManager<OmPosition,OmPosition> {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** 岗位综合查询接口 */
	@Resource(name="omPositionManager")
	private IOmPositionManager omPositionManager;
	
	/** 人员业务类*/
	@Resource(name="employeeManager")
	private EmployeeManager employeeManager;
	
	/** 人员-岗位关联业务类*/
	@Resource(name="empPositionManager")
	private EmpPositionManager empPositionManager;
	
	/** 工作组-岗位关联业务类*/
	@Resource(name="groupposiManager")
	private GroupposiManager groupposiManager;
	
	/** 职务业务类 */
	@Resource(name="workDutyManager")
	private WorkDutyManager workDutyManager;
	
	/**
	 * <li>说明：物理删除机构人员树/工作组树中岗位对应的下级人员信息，同时删除该人员与岗位的关联关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 人员id数组
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public void deleteByIds(Serializable... ids) throws BusinessException {
		try {
			if(ids == null) return;
			Long upperpositionid = null;
			//循环删除选中的岗位
			for (Serializable id : ids) {
				if(StringUtil.isNullOrBlank(String.valueOf(id))) continue;
				Long positionid = Long.valueOf(String.valueOf(id));
				OmPosition posi = omPositionManager.getModelById(positionid);
				if(posi == null) continue;
				upperpositionid = posi.getManaposi(); //记录待删除机构的上级岗位ID
				iterationDeleteChildPosi(posi); //调用递归方法删除当前岗位及下属各级岗位
				/* 更新当前删除岗位的上层岗位的subcount */
				if(upperpositionid == null) continue;
				OmPosition pposi = omPositionManager.getModelById(upperpositionid); //获取上层岗位实体
				String hql = "from OmPosition where manaposi = " + upperpositionid;
				List <OmPosition> plist = this.daoUtils.find(hql);
				if( plist == null || plist.size() < 1) {
					pposi.setIsleaf("y");
					pposi.setSubcount(0L);
				} else {
					pposi.setIsleaf("n");
					pposi.setSubcount(Long.valueOf(String.valueOf(plist.size())));
				}
				this.daoUtils.update(pposi); //更新subcount
				
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}
	
	/**
	 * <li>说明：递归方法，根据入参的岗位，迭代删除其下属所有子岗位
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-4
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param posi 岗位实体
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	private void iterationDeleteChildPosi(OmPosition posi) throws BusinessException{
		if(posi == null) return;
		String hql = "from OmPosition where manaposi = " + posi.getPositionid();
		if(!StringUtil.isNullOrBlank(posi.getPositype())&&"organization".equals(posi.getPositype())){ //如果删除的对象是机构下的岗位
			empPositionManager.deleteByPositionId(posi.getPositionid());//调用方法删除当前入参岗位的下属人员与岗位的关联关系，并且物理删除对应的人员信息
		} else { //如果删除的对象是工作组下的岗位
			groupposiManager.deleteByPositionId2(posi.getPositionid());//调用方法删除当前岗位与其对应工作组的关联关系
			empPositionManager.deleteByPositionId2(posi.getPositionid());//调用方法删除当前入参岗位的下属人员与岗位的关联关系
		}
		List <OmPosition> childPosi = this.daoUtils.find(hql); //获取入参机构的下级子岗位
		if(childPosi==null||childPosi.size()<1) {
			this.daoUtils.getHibernateTemplate().delete(posi); //执行删除岗位
			return; //如果未找到子项，跳出递归
		}
		for(OmPosition _o : childPosi){
			iterationDeleteChildPosi(_o); //递归调用，继续向下层查找
		}
		this.daoUtils.getHibernateTemplate().delete(posi); //执行删除岗位
	}
	
	/**
	 * 清除岗位与职务关联信息
	 * @param dutyId
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public void clearDutyColumn(Long dutyId) throws BusinessException{
		String hql = "from OmPosition where dutyid = " + dutyId;
		List <OmPosition> list = this.daoUtils.find(hql);
		for(OmPosition op : list){
			op.setDutyid(null);
			this.daoUtils.update(op);
		}
	}
	
	/**
	 * <li>说明：
	 * <li>返回值： the employeeManager
	 */
	public EmployeeManager getEmployeeManager() {
		return employeeManager;
	}

	/**
	 * <li>说明：
	 * <li>参数： employeeManager
	 */
	public void setEmployeeManager(EmployeeManager employeeManager) {
		this.employeeManager = employeeManager;
	}

	/**
	 * <li>说明：
	 * <li>返回值： the groupposiManager
	 */
	public GroupposiManager getGroupposiManager() {
		return groupposiManager;
	}

	/**
	 * <li>说明：
	 * <li>参数： groupposiManager
	 */
	public void setGroupposiManager(GroupposiManager groupposiManager) {
		this.groupposiManager = groupposiManager;
	}
	
	

	public WorkDutyManager getWorkDutyManager() {
		return workDutyManager;
	}

	public void setWorkDutyManager(WorkDutyManager workDutyManager) {
		this.workDutyManager = workDutyManager;
	}

	/**
	 * <li>说明：根据组织机构id查询其所属岗位ID
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param orgids 以逗号分隔的组织机构id序列
	 * @return 以逗号分隔的岗位id序列
	 * @throws 抛出异常列表
	 */
	public String[] getChildPosIds(Long orgid){
		/* 删除岗位 */
		List <OmPosition> list = omPositionManager.findPertainToOrg(orgid); //调用岗位查询接口，获取机构下属的所有岗位
		if(list!=null && list.size()<1) return null;
		String [] positionids = new String [list.size()];
		for(int i=0;i<list.size();i++){
			positionids[i] = String.valueOf(list.get(i).getPositionid());
		}
		return positionids;
	}
	
	/**
	 * <li>说明：查询入参中各岗位的子岗位positionid，并拼装为逗号分隔的字符串后返回
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param positionids 逗号分隔的positionid字符串
	 * @return 包含入参positionidids的各岗位及其下属机构的positionid字符串
	 * @throws 抛出异常列表
	 */
	public String getChildPosiIds(String positionids){
		StringBuffer childOrgidAry = null;
		if(!StringUtil.isNullOrBlank(positionids)) {
			childOrgidAry = new StringBuffer();
			//使用oracle内置函数，递归查询入参中各工作组的下属各级工作组的groupid
			String sql = "SELECT positionid FROM OM_POSITION START WITH positionid IN ("+positionids+") CONNECT BY PRIOR positionid = manaposi";
			List _t = daoUtils.executeSqlQuery(sql);
			for(Object obj : _t){
				childOrgidAry.append(obj.toString()).append(",");
			}
			return childOrgidAry.toString().substring(0, childOrgidAry.toString().lastIndexOf(","));
		}
		return null;
	}
	
	/**
	 * <li>说明：根据以逗号分隔的工作组id序列，获取这些工作组相关的所有岗位id，并以逗号分隔
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param groupids 逗号分隔的工作组id字符串
	 * @return 包含入参groupids下属岗位的positionid字符串
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public String getChildPosiIdsByGroup(String groupids){
		StringBuffer childOrgidAry = null;
		StringBuffer positionAry = new StringBuffer();
		String _temp = null;
		if(!StringUtil.isNullOrBlank(groupids)) {
			//从Om_Groupposi表中根据工作组id获取对应的岗位id
			String hql = "from OmGroupposi where id.groupid in ("+groupids+")";
			List <OmGroupposi> list = this.daoUtils.find(hql);
			if(list!=null&&list.size()>0){
				for(OmGroupposi _og : list){
					positionAry.append(_og.getId().getPositionid()).append(",");
				}
				_temp = positionAry.toString().substring(0, positionAry.toString().lastIndexOf(","));
				childOrgidAry = new StringBuffer();
				//使用oracle内置函数，递归查询入参中各工作组的下属各级工作组的groupid
				String sql = "SELECT positionid FROM OM_POSITION START WITH positionid IN ("+_temp+") CONNECT BY PRIOR positionid = manaposi";
				List _t = daoUtils.executeSqlQuery(sql);
				for(Object obj : _t){
					childOrgidAry.append(obj.toString()).append(",");
				}
				return childOrgidAry.toString().substring(0, childOrgidAry.toString().lastIndexOf(","));
			} 
		}
		return null;
	}
	
	/**
	 * <li>说明： 保存岗位信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-13
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param omPosition 实体对象
	 * @return void 
	 * @throws BusinessException, NoSuchFieldException
	 */	
	public void saveOrUpdate(OmPosition omPosition) throws BusinessException, NoSuchFieldException {
		boolean isAdd = false; //是否是新增操作
		Date now = new Date();
		Long groupid = 0L; //工作组ID
		if(omPosition!=null&&"workgroup".equals(omPosition.getPositype())){
			//当工作组下创建岗位和子岗位时，orgid字段不会使用，为简化代码，利用该字段传递groupid，此处提取groupid后，将orgid重新置空
			groupid = omPosition.getOrgid(); 
			omPosition.setOrgid(null);
		}
		//判断是否新增操作
		if(omPosition.getPositionid()==null){
			isAdd = true;
			omPosition.setCreatetime(now); //设置创建时间
			AcOperator acOperator = SystemContext.getAcOperator(); //获取当前登录操作员
			omPosition.setUpdator(acOperator.getOperatorid()); //最近更新人员
			omPosition.setIsleaf("y");//新增的节点是叶子节点
			omPosition.setPositionseq("-"); //临时设置一个标识，以解决新增时的非空约束问题
			omPosition.setSubcount(0L);//新增的岗位没有下级，所以其子节点数为0
			this.daoUtils.getHibernateTemplate().saveOrUpdate(omPosition); //insert岗位信息，并产生岗位id
			//如果当前新增岗位的上级岗位id为空
			if(omPosition.getManaposi()==null){
				omPosition.setPositionseq("."+omPosition.getPositionid()+".");//根据新增后产生的id构造岗位序列
				omPosition.setPosilevel(1L); //本级岗位节点的层次
			} else {
				OmPosition pop = omPositionManager.getModelById(omPosition.getManaposi()); //查询上级岗位的seq序列
				omPosition.setPositionseq(pop.getPositionseq()+omPosition.getPositionid()+"."); //根据上级岗位的seq序列+本级岗位的id，构建岗位序列
				omPosition.setPosilevel(pop.getPosilevel()==null?1L:(pop.getPosilevel()+1)); //本级岗位节点的层次=上级岗位节点层次+1
				pop.setSubcount(pop.getSubcount()==null?0L:(pop.getSubcount()+1)); //因新增了岗位，所以父节点的子节点数+1
				pop.setIsleaf("n");//父节点不再是叶子节点
				this.daoUtils.getHibernateTemplate().saveOrUpdate(pop);//更新父岗位
			}
		}
		omPosition.setLastupdate(now); //设置最后更新时间
		this.daoUtils.getHibernateTemplate().saveOrUpdate(omPosition); //更新岗位
		//如上级岗位为空，且岗位类型为workgroup，则说明该岗位属于工作组岗位，调用函数创建或更新工作组-岗位关联
		if(isAdd && omPosition.getManaposi()==null && omPosition.getPositype()!=null && "workgroup".equals(omPosition.getPositype())){
			groupposiManager.addGroupposiCorrelation(groupid, omPosition.getPositionid());
		}
	}
	
	/**
	 * <li>说明：下级岗位列表查询
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-20
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
	public Page findPosiList(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer();
		Long groupid = 0L;
		String sql = "SELECT " +
		"posi.positionid as \"positionid\", posi.dutyid as \"dutyid\", posi.manaposi as \"manaposi\", posi.posicode as \"posicode\", " +
		"posi.posiname as \"posiname\", posi.posilevel as \"posilevel\", posi.orgid as \"orgid\", posi.positionseq as \"positionseq\", " +
		"posi.positype as \"positype\", posi.createtime as \"createtime\", posi.lastupdate as \"lastupdate\", posi.updator as \"updator\", " +
		"posi.startdate as \"startdate\", posi.enddate as \"enddate\", posi.status as \"status\", posi.isleaf as \"isleaf\", " +
		"posi.subcount as \"subcount\", '' as \"orgname\", duty.dutyname  as \"dutyname\" " +
		"FROM OM_Position posi LEFT JOIN OM_DUTY duty on posi.dutyid = duty.dutyid where 1=1";
		//拼接查询参数
		if (whereList != null && whereList.size() > 0) {
			for (Condition condition : whereList) {
				if(!StringUtil.isNullOrBlank(condition.getPropName())&&"groupid".equals(condition.getPropName())){
					sql = sql + " and posi.positionid in (select positionid from Om_Groupposi where groupid = '"+condition.getPropValue()+"')";
				} else {
					searchParam.append(" and posi.");
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
				searchParam.append(" order by posi.").append(order.toString());
			}
		}
		String total = "select count(*) as \"rowcount\" from (".concat(sql.concat(searchParam.toString())).concat(")");
		return super.findPageList(total, sql.concat(searchParam.toString()), start, limit,null,null);
	}
	
	/**
	 * <li>说明：下级岗位列表查询
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-20
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
	public Page findPositionList(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer();
		Long groupid = 0L;
		String hql = "from OmPosition where 1=1";
		//拼接查询参数
		if (whereList != null && whereList.size() > 0) {
			for (Condition condition : whereList) {
				if(!StringUtil.isNullOrBlank(condition.getPropName())&&"groupid".equals(condition.getPropName())){
					hql = hql + " and positionid in (select id.positionid from OmGroupposi where id.groupid = '"+condition.getPropValue()+"')";
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
		String total = "select count(*) ".concat(hql.concat(searchParam.toString()));
		return super.findPageList(total, hql.concat(searchParam.toString()), start, limit);
	}
	
	/**
	 * <li>说明：人员列表选择控件
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchEntity
	 * @param codeOrName 人员编号或姓名
	 * @param empstatus  人员状态
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public Page findPageList(SearchEntity<OmPosition> searchEntity, String posiname, Long empid) throws BusinessException {
		if(empid == null){
			empid = 10098L;
		}
		String sql = "select posi.positionid as \"positionid\", posi.posiname as \"posiname\", dict.dictname as \"positype\"," +
				" ep.ismain as \"isleaf\" from Om_Position posi left join Om_Empposition ep on posi.positionid = ep.positionid " +
				" left join (select dicttypeid, dictid, dictname from eos_dict_entry where dicttypeid = 'ABF_POSITYPE') dict " +
				" on posi.positype = dict.dictid " +
				" where ep.empid = " + empid;
		
		if(!StringUtil.isNullOrBlank(posiname)){
			sql = sql.concat(" and posi.posiname like '%"+posiname+"%'");
		}
		Order[] orders = searchEntity.getOrders();
		sql = sql + HqlUtil.getOrderHql(orders);
    	String total = "select count(*) as \"rowcount\" from (?)".replace("?", sql);
		return super.findPageList(total, sql, searchEntity.getStart(), searchEntity.getLimit(),null,null);
	}
	
	/**
	 * <li>说明：调用职务业务方法， 根据职务ID获取职务名称
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param posi 岗位实体
	 * @return 职务名称
	 * @throws 抛出异常列表
	 */
	public String findDutyInfo(OmPosition posi) throws BusinessException {
		return workDutyManager.getModelById(posi.getDutyid()).getDutyname();
	}
	
	/**
	 * <li>说明：获取当前机构/岗位的下级岗位列表
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-27
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
	public Page findPosiList2(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer();
		String sql = "SELECT " +
		"posi.positionid as \"positionid\", posi.dutyid as \"dutyid\", posi.manaposi as \"manaposi\", posi.posicode as \"posicode\", " +
		"posi.posiname as \"posiname\", posi.posilevel as \"posilevel\", posi.orgid as \"orgid\", posi.positionseq as \"positionseq\", " +
		"posi.positype as \"positype\", posi.createtime as \"createtime\", posi.lastupdate as \"lastupdate\", posi.updator as \"updator\", " +
		"posi.startdate as \"startdate\", posi.enddate as \"enddate\", posi.status as \"status\", posi.isleaf as \"isleaf\", " +
		"posi.subcount as \"subcount\", org.orgname as \"orgname\", duty.dutyname  as \"dutyname\" " +
		"FROM OM_Position posi LEFT JOIN OM_DUTY duty on posi.dutyid = duty.dutyid LEFT JOIN OM_ORGANIZATION org on posi.orgid = org.orgid where 1=1";
		//拼接查询参数
		if (whereList != null && whereList.size() > 0) {
			for (Condition condition : whereList) {
					searchParam.append(" and posi.");
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
				searchParam.append(" order by posi.").append(order.toString());
			}
		}
		String total = "select count(*) as \"rowcount\" from (".concat(sql.concat(searchParam.toString())).concat(")");
		return super.findPageList(total, sql.concat(searchParam.toString()), start, limit,null,null);
	}
	
	/**
	 * <li>说明：根据查询条件，查询匹配的岗位信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-2
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
	public Page findPosiListByEmployee(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer();
		@SuppressWarnings("unused")
		String sql = "select ep.positionid as \"positionid\", op.posiname as \"posiname\", '' as \"empid\", emp.empname as \"empname\", ep.ismain as \"isleaf\" " + 
					" from om_empposition ep left join om_position op on ep.positionid = op.positionid left join om_employee emp on ep.empid = emp.empid " +
					" where (ep.ismain is null or ep.ismain <> 'y') and ep.empid = ? ";
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
	 * <li>说明：返回已配置为当前角色的岗位列表
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
	public Page findPositionListByRole(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer();
		String sql = " from OmPosition posi, OmPartyrole partyrole where posi.positionid = partyrole.id.partyid and partyrole.id.partytype = 'position' and partyrole.id.roleid = '?' ";
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
		String hql = "select posi ".concat(sql); //构建数据查询语句
		return super.findPageList(totalHql, hql, start, limit);
	}
	
	/**
	 * <li>说明：返回未配置为当前角色的岗位列表
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
	public Page findPositionListByRole2(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer();
		String sql = "from OmPosition posi where posi.positionid not in (select id.partyid from OmPartyrole where id.partytype = 'position' and id.roleid = '?')";
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
		String hql = "select posi ".concat(sql); //构建数据查询语句
		return super.findPageList(totalHql, hql, start, limit);
	}
	
	/**
     * <li>说明：验证指定人员在是否是指定班组的特定岗位
     * <li>创建人：黄杨
     * <li>创建日期：2017年5月9日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * @param empid 人员id
     * @param orgid 班组id
     * @param posiname 岗位名称
     * @return true：是，false：否
     */
    public boolean validatePosi(Long empid, Long orgid, String posiname) {
    	List<OmPosition> list = this.findPosiByEmpid(empid);
    	if (null == list || list.isEmpty()) {
    		return false;
    	}
    	for (OmPosition posi : list) {
    		if (orgid.equals(posi.getOrgid()) && posiname.equals(posi.getPosiname())) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * <li>说明：根据人员id获取该人员所有岗位信息
     * <li>创建人：黄杨
     * <li>创建日期：2017年5月9日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * @param empid 人员id
     * @return 岗位实体对象集合
     */
    @SuppressWarnings("unchecked")
	private List<OmPosition> findPosiByEmpid(Long empid) {
    	String hql = "Select A From OmPosition A, OmEmpposition B Where A.positionid = B.id.positionid And B.id.empid = ?";
    	return this.daoUtils.find(hql, empid);
    }
}
