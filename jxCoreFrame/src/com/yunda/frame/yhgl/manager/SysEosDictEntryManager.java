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

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.entity.EosDictEntryId;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 系统业务字典项(即明细)业务类
 * <li>创建人：谭诚
 * <li>创建日期：2013-12-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="sysEosDictEntryManager")
public class SysEosDictEntryManager   extends JXBaseManager <EosDictEntry, EosDictEntry> {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** 业务字典项查询接口 */
	@Resource(name="eosDictEntryManager")
	private IEosDictEntryManager eosDictEntryManager;
	
	/**
	 * 
	 * <li>说明：获取当前业务字典类别的下级
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param dicttypeid 业务字典类型ID
	 * @param dictid 	业务字典项ID
	 * @return 下级业务字典
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public List <Map> getChildNodes(String dicttypeid,String dictid,String isAll) throws BusinessException{
		List <EosDictEntry> list = null;
		if(StringUtil.isNullOrBlank(dictid)||"ROOT_0".equals(dictid)){
			list = eosDictEntryManager.findRoots(dicttypeid);  //查询根
		} else {
			list = eosDictEntryManager.findChildsByIds(dicttypeid,dictid); //查询下层
		}
		Map node = null;
		List<Map> children = new ArrayList<Map>();
		for(EosDictEntry entry : list){
			node = new LinkedHashMap();
			node.put("id", 		entry.getId().getDictid()); //node.id
			node.put("text", 	entry.getDictname()); //node.text
			List <EosDictEntry> _t = eosDictEntryManager.findChildsByIds(entry.getId().getDicttypeid(), entry.getId().getDictid()); //查询下层是否还有数据
			if(_t!=null && _t.size()>0){
				node.put("leaf", false); //不是叶子节点
				if("true".equals(isAll)){
					node.put("children", getChildNodes(dicttypeid,entry.getId().getDictid(),isAll));
				}
			} else {
				node.put("leaf", true);  //是叶子节点
			}
			node.put("dictid", 		entry.getId().getDictid()); //业务字典项ID
			node.put("dicttypeid", 	entry.getId().getDicttypeid()); //业务字典项名称
			node.put("dictname", 	entry.getDictname());  	//业务字典名称
			node.put("status", 		entry.getStatus());    	//上层业务字典status
			node.put("sortno", 		entry.getSortno());    	//业务字典排序号
			node.put("rank", 		entry.getRank());  		//业务字典层级
			node.put("parentid", 	entry.getParentid());	//业务字典上层ID
			node.put("seqno", 		entry.getSeqno());		//业务字典seq
			node.put("filter1", 	entry.getFilter1());	//业务字典过滤字段1
			node.put("filter2", 	entry.getFilter2());	//业务字典过滤字段2
			children.add(node);
		}
		return children;
	}
	
	/**
	 * <li>说明：当用户点击机构TreeNode时，调用该方法获取所选机构列表信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 */
	public Page findEosDictEntryList(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer(); //查询条件字符串
		String sql = "select entry.dicttypeid as \"tempdicttypeid\", entry.dictid as \"tempdictid\", entry.dictname as \"dictname\"," +
					"entry.status as \"status\", entry.sortno as \"sortno\", entry.rank as \"rank\", entry.parentid as \"parentid\", " +
					"entry.seqno as \"seqno\", entry.filter1 as \"filter1\", entry.filter2 as \"filter2\" " +
					"from Eos_Dict_Entry entry where 1=1 ";
		String totalsql = "select count(*) as \"rowcount\" from (?)";
		
		//拼接查询参数
		if (whereList != null && whereList.size() > 0) {
			for (Condition condition : whereList) {
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
		//拼接排序参数
		if(orderList != null && orderList.size()>0) {
			for(Order order : orderList){
				searchParam.append(" order by ").append(order.toString());
			}
		}
		return super.findPageList(totalsql.replace("?", sql.concat(searchParam.toString())), sql.concat(searchParam.toString()), start, limit, null,null);
	}
	
	/**
	 * <li>说明： 验证是否存在相同的字典ID
	 * <li>创建人： 谭诚
	 * <li>创建日期：2013-12-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param type 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */
	public String[] validateUpdate(EosDictEntry entry){
		if(entry == null) return new String[]{"验证出错，参数未输入!"};
		EosDictEntry exist = eosDictEntryManager.findCacheEntry(entry.getId().getDictid(), entry.getId().getDicttypeid());
		if(exist != null){
			return new String[]{"字典项代码 ["+entry.getId().getDictid()+"] 已存在， 请重新输入!"};
		}
		return null;
	}
	
	
	/**
	 * <li>说明：覆盖基类的新增和更新方法，因业务字典项表为双主键，在验证是新增还是更新操作时， 需同时判断字典项类别ID和字典项ID
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-14
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param T t 实体对象
	 * @return void 
	 * @throws BusinessException, NoSuchFieldException
	 */	
	@SuppressWarnings("unchecked")
	public void saveOrUpdate(EosDictEntry entry) throws BusinessException, NoSuchFieldException {
		if(entry == null) return;
		boolean isAdd = false; 
		String hql = "from EosDictEntry where dicttypeid = ? and dictid = ?" ;
		Object[] param = new Object[] {entry.getTempdicttypeid(),entry.getTempdictid()};
		List <EosDictEntry> list = daoUtils.getHibernateTemplate().find(hql,param); //不使用缓存查询
		if(list == null || list.size() <1) isAdd = true;
		
		if(isAdd){ 			
			EosDictEntryId id = new EosDictEntryId();
			id.setDicttypeid(entry.getTempdicttypeid());
			id.setDictid(entry.getTempdictid());
			entry.setId(id);
			entry.setTempdicttypeid(null);
			entry.setTempdictid(null);
			this.daoUtils.getHibernateTemplate().save(entry);
		} else {
            String sql = "UPDATE Eos_Dict_Entry SET dictname = '" + entry.getDictname() + "', status = "+entry.getStatus() +", sortno = " + entry.getSortno() + ",filter1 = '" + entry.getFilter1() +"'" + ",filter2 = '" + entry.getFilter2()+"'"+ " WHERE dicttypeid = '" + entry.getTempdicttypeid() + "' and dictid = '" + entry.getTempdictid() + "'";
            this.daoUtils.executeSql(sql);
		}
	}	
	
	/**
	 * <li>说明： 重载基类方法，执行选中业务字典项的删除方法，并级联删除子项数据
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-13
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param type 实体对象
	 * @return void 
	 * @throws BusinessException, NoSuchFieldException
	 */	
	public void deleteByIds(String dicttypeid,Serializable... ids) throws BusinessException {
		if(ids == null||StringUtil.isNullOrBlank(dicttypeid)) return;
		try {
			//循环删除选中字典项
			for (Serializable id : ids) {
				if(StringUtil.isNullOrBlank(String.valueOf(id))) continue;
				EosDictEntry entry = eosDictEntryManager.findCacheEntry(String.valueOf(id), dicttypeid);//.getModelById(String.valueOf(id)); //根据dicttypeid获取待删除的业务字典类型实体
				if(entry == null) continue;
				iterationDeleteChildEntry(entry); //调用递归方法删除子项
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}
	
	/**
	 * <li>说明： 根据业务字典类型ID，删除该类型所属的所有业务字典项,主要用于删除业务字典类型时级联删除业务字典项所使用
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-14
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param type 实体对象
	 * @return void 
	 * @throws BusinessException, NoSuchFieldException
	 */
	public void deleteByTypeIds(String dicttypeid) throws BusinessException {
		if(StringUtil.isNullOrBlank(dicttypeid)) return;
		try{
			String sql = "DELETE eos_dict_entry where dicttypeid = '" + dicttypeid + "'";
			this.daoUtils.executeSql(sql);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}
	
	/**
	 * <li>说明：递归方法，根据入参的业务字典，迭代删除其下属所有子项
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param type 业务字典实体
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private void iterationDeleteChildEntry(EosDictEntry entry) throws BusinessException{
		List <EosDictEntry> childEntry = eosDictEntryManager.findChildsByIds(entry.getId().getDicttypeid(),entry.getId().getDictid()); //获取下级业务字典类型列表
		if(childEntry==null||childEntry.size()<1) {
			this.daoUtils.getHibernateTemplate().delete(entry); //【执行删除字典项】
			return; //如果未找到子项，跳出递归
		}
		for(EosDictEntry _o : childEntry){
			iterationDeleteChildEntry(_o); //递归调用，继续向下层查找
		}
		this.daoUtils.getHibernateTemplate().delete(entry); //【执行删除字典项】
	}
	
}
