package com.yunda.frame.yhgl.manager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.AcFunction;
import com.yunda.frame.yhgl.entity.AcRole;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 系统应用功能管理-应用功能业务类
 * <li>创建人：谭诚
 * <li>创建日期：2013-12-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="sysFunctionManager")
public class SysFunctionManager extends JXBaseManager <AcFunction, AcFunction> implements ISysFunctionManager {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：获取系统功能
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public List <Map> getTreeNodeByApp(Long appid, Long funcgroupid) throws BusinessException{
		//根据入参构建查询条件
		if(funcgroupid == null) return null;
		String hql = "from AcFunction where funcgroupid = " + funcgroupid + " order by funcname";
		
		//获取功能
		List <AcFunction> funclist = this.daoUtils.getHibernateTemplate().find(hql);
		
		//构建treeNode
		List <Map> nodeAry = null;
		if(funclist!=null&&funclist.size()>0){
			nodeAry = new ArrayList();
			Map node = null;
			for(AcFunction func : funclist){
				node = new LinkedHashMap();
				node.put("id", "c_".concat(func.getFunccode())); //ID
				node.put("text", func.getFuncname()); 
				node.put("leaf", true);
				node.put("funccode", func.getFunccode());
				node.put("funcgroupid", func.getFuncgroupid());
				node.put("funcname", func.getFuncname());
				node.put("funcdesc", func.getFuncdesc());
				node.put("funcaction", func.getFuncaction());
				node.put("parainfo", func.getParainfo());
				node.put("ischeck", func.getIscheck());
				node.put("functype", func.getFunctype());
				node.put("ismenu", func.getIsmenu());
				node.put("appid", appid);
				node.put("realid", func.getFunccode()); //真实ID
				node.put("nodetype", "func");//节点类型
				node.put("checked", false);
				node.put("parent", funcgroupid);
				nodeAry.add(node);
			}
			return nodeAry;
		} else {
			return null;
		}
	}
	
	/**
	 * <li>说明：根据功能组ID查询其下辖的功能
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param funcgroupid 功能组ID
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public List <AcFunction> findFuncEntityByGroup(Long funcgroupid) throws BusinessException {
		List <AcFunction> funcList = this.daoUtils.getHibernateTemplate().find("from AcFunction where funcgroupid = " + funcgroupid);
		return funcList;
	}
	
	/**
	 * <li>说明：根据功能组ID删除其下辖的功能
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param funcgroupid 功能组ID
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public void deleteFuncByGroup(Long funcgroupid) throws BusinessException {
		String sql = null;
		List <AcFunction> funcList = findFuncEntityByGroup(funcgroupid);
		for (AcFunction acFunction : funcList){
			this.daoUtils.executeSql("DELETE AC_Rolefunc WHERE funccode = '" + acFunction.getFunccode() + "' and funcgroupid = " + funcgroupid); //级联删除角色-功能关联表数据
			this.daoUtils.getHibernateTemplate().delete(acFunction);
		}
	}
	
	/**
	 * <li>说明：查询全部系统功能，用于角色模块选择功能的控件
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public com.yunda.frame.common.Page<AcFunction> findFunctions(SearchEntity<AcFunction> searchEntity, String funcname) throws BusinessException {
		String head = "SELECT new AcFunction(a.funccode,a.funcname,a.funcaction,a.funcgroupid,b.funcgroupname,c.appid,c.appname)";
		String hql = " FROM AcFunction a,AcFuncgroup b,AcApplication c WHERE a.funcgroupid = b.funcgroupid and b.appid = c.appid";
		if(!StringUtil.isNullOrBlank(funcname)){
			hql += " and a.funcname like '%" + funcname + "%'";
		}
		String total = "select count(*) ";
		return super.findPageList(total + hql, head + hql, searchEntity.getStart(), searchEntity.getLimit());
	}
	
	/**
	 * <li>说明：新增前验证
	 * <li>创建人：谭诚
	 * <li>创建日期：2014-1-7
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param t 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */
	public String[] validateInsert(AcFunction t){
		if(t==null||StringUtil.isNullOrBlank(t.getFunccode())){
			return new String[]{"新增功能信息失败！"};
		}
		String hql = "from AcFunction where funccode = '" + t.getFunccode() + "'";
		AcFunction _t = (AcFunction)this.daoUtils.findSingle(hql);
		if(_t!=null) return new String [] {"系统中已存在["+_t.getFuncname()+"]功能编号！"};
		else return null;
	}
	
	/**
	 * <li>说明：更新前验证
	 * <li>创建人：谭诚
	 * <li>创建日期：2014-1-7
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param t 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */
	public String[] validateUpdate(AcFunction t){
//		if(t==null){
//			return new String[]{"保存功能信息失败！"};
//		}
//		String hql = "from AcFunction where funccode = '" + t.getFunccode() + "'";
//		if(t.getFunccode()!=null && !StringUtil.isNullOrBlank(t.getFunccode())){
//			//编辑验证
//			hql = hql.concat(" and funccode <> '" + t.getFunccode() + "'");
//		}
//		AcFunction _t = (AcFunction)this.daoUtils.findSingle(hql);
//		if(_t!=null) return new String [] {"系统中已存在["+_t.getFuncname()+"]功能名称！"};
//		else return null;
		return null;
	}
	
	/**
	 * <li>说明：根据isAdd参数，判断是否是新增操作
	 * <li>创建人：谭诚
	 * <li>创建日期：2014-01-07
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param T t 实体对象
	 * @param isAdd true新增 false编辑
	 * @return void 
	 * @throws BusinessException, NoSuchFieldException
	 */	
	public void saveOrUpdate(AcFunction t, Boolean isAdd) throws BusinessException, NoSuchFieldException {
		if(t == null) return;
		if(!isAdd){
			//编辑操作
			this.daoUtils.getHibernateTemplate().update(t);
		}
		else {
//			String sql = "select nvl(max(funccode),0)+1 from Ac_Function";
//			List <Object> list = (List<Object>)this.daoUtils.executeSqlQuery(sql);
//			if(list!=null && list.get(0)!=null && !StringUtil.isNullOrBlank(String.valueOf(list.get(0)))){
//				//t.setAppid(Long.valueOf(String.valueOf(list.get(0))));
//				t.setFunccode(String.valueOf(list.get(0)));
//			}
//			新增操作
			this.daoUtils.getHibernateTemplate().save(t);
		}
	}	
	
	/**
	 * <br/><li>说明： 根据角色ID集合，以及系統應用的Code，獲取該系統應用下與入參角色ID對應的應用功能
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2014-6-3
	 * <br/><li>修改人：何涛
	 * <br/><li>修改日期：2015-11-30
	 * <br/><li>修改内容：去掉查询出的重复的应用功能权限
	 * @param roleIdList 角色ID集合
	 * @param appCode 應用系統編號
	 * @return 角色ID集合
	 */
	@SuppressWarnings("unchecked")
	public List<AcFunction> findByOperatorIdAndAppCode(List <Object> roleIdList, String appCode){
		if(roleIdList==null||roleIdList.size()<1||StringUtil.isNullOrBlank(appCode)) return null;
		String hql = SqlMapUtil.getSql(XMLNAME_FUNCTION.concat("findByOperatorIdAndAppCode"));
		Object [] _roleIds = roleIdList.toArray();
		StringBuffer bf = new StringBuffer();
		for(Object _t : _roleIds){
			bf.append("'").append(String.valueOf(_t)).append("'").append(",");
		}
		hql = hql.replaceFirst("[?]", bf.substring(0, bf.length()-1));
		hql = hql.replaceFirst("[?]", appCode);
		List <AcFunction> list = this.daoUtils.find(hql);
        // 去掉查询出的重复的应用功能权限
        List<AcFunction> distinctList = new ArrayList<AcFunction>();
        for (AcFunction af : list) {
            if (distinctList.contains(af)) {
                continue;
            }
            distinctList.add(af);
        }
		return distinctList;
	}
    
    /**
     * <li>说明：查询应用功能权限对应的角色
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param funcname
     * @param appcode
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> findRolesByNameAndAppId(String funcname, String appcode){
        if(StringUtil.isNullOrBlank(funcname)||StringUtil.isNullOrBlank(appcode)) return null;
        String sql = SqlMapUtil.getSql(XMLNAME_FUNCTION.concat("findRolesByNameAndAppId"));
        sql = sql.replaceFirst("[?]", funcname);
        sql = sql.replaceFirst("[?]", appcode);
        List <String> list = this.daoUtils.executeSqlQuery(sql);
        return list;
    }
    
    /** 
     * <li>说明：获取指定系统应用下对应功能名称的应用功能
     * <li>创建人：程锐
     * <li>创建日期：2015-2-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param funcname 应用功能名称
     * @param appcode 应用code
     * @return 应用功能实体
     */
    public AcFunction findByFuncnameAndAppcode(String funcname, String appcode) {
        if (StringUtil.isNullOrBlank(funcname) && StringUtil.isNullOrBlank(appcode))
            return null;
        String hql = SqlMapUtil.getSql(XMLNAME_FUNCTION.concat("findByFuncnameAndAppcode"));
        hql = hql.replaceFirst("[?]", funcname);
        hql = hql.replaceFirst("[?]", appcode);
        return (AcFunction) daoUtils.findSingle(hql);
    }
}
