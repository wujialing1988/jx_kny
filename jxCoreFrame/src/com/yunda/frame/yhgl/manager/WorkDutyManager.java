/**
 * 
 */
package com.yunda.frame.yhgl.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.entity.OmDuty;

/**
 * @author Administrator
 *
 */
@Service(value="workDutyManager")
public class WorkDutyManager extends JXBaseManager <OmDuty,OmDuty>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** 职务查询接口 */
	@Resource(name="omDutyManager")
	private IOmDutyManager omDutyManager;
	
	/**
	 * 
	 * <li>说明：获取当前职务/职务组的下级
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-10-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unused")
	public List <Map> getChildNodes(String nodeid,String nodetype) throws BusinessException{
		if(StringUtil.isNullOrBlank(nodeid))	
			return findDictEntryByDutyType(); //构建容器，存放从数据字典中获取的职务类型
		else if( nodeid!=null && !StringUtil.isNullOrBlank(nodetype)){  
			//获取职务
			return findDutyList(nodeid, nodetype);
		}
		return null;
	}
	
	/**
	 * 
	 * <li>说明：从数据字典获取职务类别，并将其按照树结构装配(职务)
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 数据字典实体
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public List <Map> findDutyList(String nodeid, String nodetype) throws BusinessException {
		String hql = null;
		if(!StringUtil.isNullOrBlank(nodetype)&& "dict".equals(nodetype))
			hql = "from OmDuty where dutytype = '"+ nodeid + "' and parentduty is null";
		else if (!StringUtil.isNullOrBlank(nodetype)&& "duty".equals(nodetype))
			hql = "from OmDuty where parentduty = '" + nodeid + "'";
		else 
			return null;
		List <OmDuty> dutyList = this.daoUtils.find(hql);
		if(dutyList == null) return null;
		List <Map> children = new ArrayList();
		Map node = null;
		for (OmDuty duty : dutyList){
			node = new LinkedHashMap();
			node.put("id", String.valueOf(duty.getDutyid())); //职务id
			node.put("text", String.valueOf(duty.getDutyname()));    //职务名称
			node.put("leaf", haveChildNode(String.valueOf(duty.getDutyid()),"duty")); //是否是叶子节点
			node.put("pid", String.valueOf(duty.getParentduty()));
			node.put("dutytype", String.valueOf(duty.getDutytype()));
			node.put("iconCls", "usergrayIcon"); //节点图标
			node.put("nodetype", "duty");//节点类型
			children.add(node);
		}
		return children;
	}
	
	/**
	 * 
	 * <li>说明：根据当前节点类型，构建查询语句，查询是否存在在子项，以决定当前树节点是否可以展开
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 数据字典实体
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	private boolean haveChildNode(String nodeid, String nodetype){
		boolean flag = true;
		if(StringUtil.isNullOrBlank(nodetype)) return flag;
		String hql = "";
		if("dict".equals(nodetype))
			hql = "from OmDuty where dutytype = '"+ nodeid + "' and parentduty is null";
		else if("duty".equals(nodetype))
			hql = "from OmDuty where parentduty = '" + nodeid + "'";
		else return false;
		List <OmDuty> dutyList = this.daoUtils.find(hql);
		if(dutyList != null && dutyList.size() > 0) flag = false;
		return flag;
	}
	
	/**
	 * 
	 * <li>说明：从数据字典获取职务类别，并将其按照树结构装配(职务类别)
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 数据字典实体
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public List <Map> findDictEntryByDutyType() throws BusinessException {
		String hql = "from EosDictEntry where dicttypeid='ABF_DUTYTYPE' and status = 1 order by sortno desc";
		List <EosDictEntry> dictList = this.daoUtils.find(hql); //获取数据字典类型
		if(dictList==null) return null;
		List <Map> children = new ArrayList();
		Map node = null;
		for(EosDictEntry dict : dictList){
			node = new LinkedHashMap();
			node.put("id", String.valueOf(dict.getId().getDictid())); //数据字典id
			node.put("text", String.valueOf(dict.getDictname()));    //数据字典名称
			node.put("leaf", haveChildNode(dict.getId().getDictid(),"dict")); //是否是叶子节点
			node.put("iconCls", "folderUserIcon"); //节点图标
			node.put("nodetype", "dict");//节点类型
			children.add(node);
		}
		return children;
	}
	
	/**
	 * <li>说明：当用户点击职务树的根节点时，查询其下属所有职务类别下的职务
	 *          当用户点击职务树的职务类别节点时，查询其下属所有职务
	 *          当用户点击职务树的职务时，查询其下级职务
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 */
	public Page findDutyList(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer(); //查询条件字符串
		String sql = "select duty.dutyid as \"dutyid\", duty.parentduty as \"parentduty\", duty.dutycode as \"dutycode\", " +
				"duty.dutyname as \"dutyname\", duty.dutylevel as \"dutylevel\", duty.dutyseq as \"dutyseq\", " +
				"duty.dutytype as \"dutytype\", duty.isleaf as \"isleaf\", duty.subcount as \"subcount\", duty.remark as \"remark\", " +
				"parent.dutyname as \"parentdutyname\" " +
				"from Om_Duty duty left join Om_Duty parent on duty.parentduty = parent.dutyid where 1=1";
		String totalsql = "select count(*) as \"rowcount\" from (?)";
		
		//拼接查询参数
		if (whereList != null && whereList.size() > 0) {
			for (Condition condition : whereList) {
				searchParam.append(" and duty.");
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
				searchParam.append(" order by ").append(order.toString());
			}
		}
		return super.findPageList(totalsql.replace("?", sql.concat(searchParam.toString())), sql.concat(searchParam.toString()), start, limit, null,null);
	}
	
	/**
	 * <li>说明：新增/编辑职务信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-26
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param omduty 实体对象
	 * @return void 
	 * @throws BusinessException, NoSuchFieldException
	 */	
	public void saveOrUpdate(OmDuty omduty) throws BusinessException, NoSuchFieldException {
		//当前操作的标识：flag == true 表示新增， flag == false 表示编辑
		boolean flag = false;
		//id不存在，表示当前职务将执行新增操作
		if(omduty.getDutyid()==null){
			flag = true;
			omduty.setIsleaf("y"); //新增的职务，属于叶子节点
			omduty.setSubcount(0L); //新增的职务，没有下级节点
		}
		this.daoUtils.saveOrUpdate(omduty); //执行新增/编辑操作
		if(flag){ //如果当前操作是新增
			if(omduty.getParentduty()==null){  //如果当前职务没有上级职务
				omduty.setDutylevel(1L); //当前职务等级为1
				omduty.setDutyseq("."+omduty.getDutyid()+"."); //设置当前职务的seq
			} else { //如果当前职务有上级职务
				String hql = "from OmDuty where dutyid = "+omduty.getParentduty();
				OmDuty parent = (OmDuty)this.daoUtils.find(hql).iterator().next(); //获取上层职务
				parent.setSubcount(parent.getSubcount()==null||parent.getSubcount()==0?1L:parent.getSubcount()+1); //更新上级职务的子项总数
				parent.setIsleaf("n"); //更新上级职务的状态为非叶子节点
				this.daoUtils.getHibernateTemplate().saveOrUpdate(parent);//更新上级职务实体
				omduty.setDutylevel(parent.getDutylevel()==null?1L:parent.getDutylevel()+1);//当前职务等级=上层职务等级+1
				omduty.setDutyseq(StringUtil.isNullOrBlank(parent.getDutyseq())?("."+omduty.getDutyid()+"."):(parent.getDutyseq()+omduty.getDutyid()+".")); //当前职务的seq = 上级职务seq + 当前职务id
			}
			this.daoUtils.getHibernateTemplate().saveOrUpdate(omduty);//更新当前职务
		}
	}
	
	/**
	 * <li>说明：删除职务信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-26
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 待删除的职务id
	 * @return void 
	 * @throws BusinessException
	 */	
	public void deleteByIds(Serializable... ids) throws BusinessException {
		try {
			for (Serializable id : ids) {
				if(id == null) continue;
				Long dutyid = Long.valueOf(String.valueOf(id));
				OmDuty duty = omDutyManager.getModelById(dutyid);
				if(duty == null) continue;
				iterationDeleteChildDuty(duty);
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}
	
	/**
	 * 递归删除下级职务
	 * @param duty
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private void iterationDeleteChildDuty(OmDuty duty) throws BusinessException {
		String hql = "from OmDuty where parentduty = " + duty.getDutyid();
		List <OmDuty> list = this.daoUtils.find(hql);//查询下级职务
		PositionManager positionManager = (PositionManager)Application.getSpringApplicationContext().getBean("positionManager");
		if(list == null || list.size() <1){
			positionManager.clearDutyColumn(duty.getDutyid()); //调用方法，清除职务与岗位的关联关系
			this.daoUtils.getHibernateTemplate().delete(duty); //【删除当前职务】
			return;
		}
		for(OmDuty _duty:list){
			iterationDeleteChildDuty(_duty);
		}
		positionManager.clearDutyColumn(duty.getDutyid()); //调用方法，清除职务与岗位的关联关系
		this.daoUtils.getHibernateTemplate().delete(duty);//【删除当前职务】
	}
	
	/**
	 * <li>说明：根据职务Id，查询职务详细信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public OmDuty findCurrentDutyInfo(Long dutyid) throws BusinessException {
		String sql = "select duty.dutyid as \"dutyid\", duty.parentduty as \"parentduty\", duty.dutycode as \"dutycode\", " +
		"duty.dutyname as \"dutyname\", duty.dutylevel as \"dutylevel\", duty.dutyseq as \"dutyseq\", " +
		"duty.dutytype as \"dutytype\", duty.isleaf as \"isleaf\", duty.subcount as \"subcount\", duty.remark as \"remark\", " +
		"parent.dutyname as \"parentdutyname\" " +
		"from Om_Duty duty left join Om_Duty parent on duty.parentduty = parent.dutyid where 1=1 and duty.dutyid = "+dutyid;
		List <Object []> list = this.daoUtils.executeSqlQuery(sql);
		OmDuty duty = new OmDuty();
		for(Object [] obj : list){
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[0]))) duty.setDutyid(Long.valueOf(String.valueOf(obj[0])));
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[1]))) duty.setParentduty(Long.valueOf(String.valueOf(obj[1])));
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[2]))) duty.setDutycode(String.valueOf(obj[2]));
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[3]))) duty.setDutyname(String.valueOf(obj[3]));
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[4]))) duty.setDutylevel(Long.valueOf(String.valueOf(obj[4])));
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[5]))) duty.setDutyseq(String.valueOf(obj[5]));
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[6]))) duty.setDutytype(String.valueOf(obj[6]));
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[7]))) duty.setIsleaf(String.valueOf(obj[7]));
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[8]))) duty.setSubcount(Long.valueOf(String.valueOf(obj[8])));
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[9]))) duty.setRemark(String.valueOf(obj[9]));
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[10]))) duty.setParentdutyname(String.valueOf(obj[10]));
			break; //如有重复，只取第一条
		}
		return duty;
	}
	
	/**
	 * <li>说明：返回已配置为当前角色的职务列表
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
	public Page findDutyListByRole(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer();
		String sql = " from OmDuty duty, OmPartyrole partyrole where duty.dutyid = partyrole.id.partyid and partyrole.id.partytype = 'duty' and partyrole.id.roleid = '?'";
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
		String hql = "select duty ".concat(sql); //构建数据查询语句
		return super.findPageList(totalHql, hql, start, limit);
	}
	
	/**
	 * <li>说明：返回未配置为当前角色的职务列表
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
	public Page findDutyListByRole2(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer();
		String sql = " from OmDuty duty where duty.dutyid not in (select id.partyid from OmPartyrole where id.partytype = 'duty' and id.roleid = '?')";
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
		String hql = "select duty ".concat(sql); //构建数据查询语句
		return super.findPageList(totalHql, hql, start, limit);
	}
}
