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
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.entity.OmPosition;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机构业务处理类
 * <li>创建人：谭诚
 * <li>创建日期：2013-10-30
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="organizationManager")
public class OrganizationManager  extends JXBaseManager<OmOrganization,OmOrganization>{
	
	/** 组织机构查询接口 */
	@Resource(name="omOrganizationManager")
	private IOmOrganizationManager omOrganizationManager;
	
	/** 岗位查询接口 */
	@Resource(name="omPositionManager")
	private IOmPositionManager omPositionManager;
	
	/** 岗位业务类 */
	@Resource(name="positionManager")
	private PositionManager positionManager;
	
	/** 人员查询接口 */
	@Resource(name="omEmployeeManager")
	private IOmEmployeeManager omEmployeeManager;
		
	/** 人员-机构关联业务类 */
	@Resource(name="empOrgManager")
	private EmpOrgManager empOrgManager;
	
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
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
		List <OmOrganization> childNodeOrgList = null; //组织机构
		List <OmPosition> childNodePosList = null;     //组织机构直属岗位
		List <OmEmployee> childNodeEmpList = null;     //组织机构直属人员
		List <Map> list = new ArrayList<Map>();        //实例化一个空的List,传入后需函数中装载数据
		/* * * * * * * * * * * * * * * * * * * * * * * * * * * */
		/*     如果当前节点层级是机构类型，则通过查询接口获取其下级机构   */
		/* * * * * * * * * * * * * * * * * * * * * * * * * * * */
		if(nodetype.equals("org") && nodeid == null){
			//当nodeid为空时，查询
			return buildExtTreeModeByOrg(omOrganizationManager.findRoot(),list); //如orgid==null，则调用接口获取机构的根
		} else if(nodetype.equals("org")) {
			childNodeOrgList = omOrganizationManager.findAllChildsById(nodeid); //根据orgid,调用接口查询获取其下级机构
			list = buildExtTreeModeByOrg(childNodeOrgList,list);
		}
		//因机构下可能有下级机构，也可能会有岗位，所以此处将通过查询接口获取下级岗位及直属人员
		if(nodetype.equals("org")||nodetype.equals("pos")){
			if(nodetype.equals("org")){
				childNodePosList = omPositionManager.findPertainToOrg(nodeid);   //根据orgid,调用接口查询获取其直属岗位
				list = buildExtTreeModeByPos(childNodePosList,list);
				childNodeEmpList = omEmployeeManager.findNoPositionByOrgId(nodeid);		//根据orgid,调用接口查询获取其直属人员
				list = buildExtTreeModeByEmp(childNodeEmpList, list, "org");
			}
			if(nodetype.equals("pos")){
				childNodePosList = omPositionManager.findChildPosition(nodeid);   //根据岗位id,调用接口查询获取其直属岗位
				list = buildExtTreeModeByPos(childNodePosList,list);
				childNodeEmpList = omEmployeeManager.findByPosition(nodeid);		//根据岗位id,调用接口查询获取其直属人员
				list = buildExtTreeModeByEmp(childNodeEmpList, list,"pos");
			}
		}
		
		return list;
	}
	
	/**
	 * <li>说明：根据数据，构建组织机构树（机构部分）
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-10-31
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param list 组织机构数据集合
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	private List <Map> buildExtTreeModeByOrg(List <OmOrganization> list, List <Map> orgList){
		if (list == null) return null;
		List<Map> children = orgList;
		Map node = null;
		for(OmOrganization org : list){
			node = new LinkedHashMap();
			node.put("id", "o_"+String.valueOf(org.getOrgid())); //机构ID
			node.put("text", org.getOrgname());    //机构名称
			
			if(!StringUtil.isNullOrBlank(org.getIsleaf())&&"N".equals(org.getIsleaf().toUpperCase())){
				node.put("leaf", false); //当前机构含有子项
			} else {
				//查询其是否存在下级岗位
				List <OmPosition> childPosition = omPositionManager.findPertainToOrg(org.getOrgid());   //根据orgid,调用接口查询获取其直属岗位
				if(childPosition!=null&&childPosition.size()>0){
					node.put("leaf", false); //当前机构含有子项
				} else {
					List <OmEmployee> childEmployee = omEmployeeManager.findNoPositionByOrgId(org.getOrgid()); //根据orgid,调用接口查询获取其直属人员
					if(childEmployee!=null&&childEmployee.size()>0){
						node.put("leaf", false);
					} else {
						node.put("leaf", true); //当前机构下即无岗位又无人员
					}
				}
				
			}
			//node.put("leaf", !StringUtil.isNullOrBlank(org.getIsleaf())&&"N".equals(org.getIsleaf().toUpperCase())?false:true); //是否叶子节点
			node.put("iconCls", findOrgDegreeIcon(org.getOrgdegree())); //根据组织等级构建树的节点图标
			//=======================//
			node.put("orgid", String.valueOf(org.getOrgid())); //机构ID
			node.put("parentorgid", String.valueOf(org.getParentorgid())); //父Id
			node.put("orgcode", org.getOrgcode()); //机构代码
			node.put("orgname", org.getOrgname()); //机构名称
			node.put("orglevel", org.getOrglevel()); //机构层次
			node.put("orgdegree", org.getOrgdegree()); //机构等级
			node.put("orgseq", org.getOrgseq()); //机构序列
			node.put("orgtype", org.getOrgtype()); // 机构类型
			node.put("status", org.getStatus()); //机构状态
			node.put("isleaf", org.getIsleaf()); //是否叶子节点
			node.put("nodetype", "org");   //存入标识,该节点是机构
			//======================//
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
	 * <li>说明：根据数据，构建组织机构树（人员部分）
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
	private List <Map> buildExtTreeModeByEmp(List <OmEmployee> list, List<Map> empList, String type){
		if(list == null) return null;
		List <Map> children = empList;
		Map node = null;
		for(OmEmployee emp : list){
			node = new LinkedHashMap();
			if(!StringUtil.isNullOrBlank(type)&&"org".equals(type))
			node.put("id", "e_"+String.valueOf(emp.getEmpid()));
			else node.put("id", "h_"+String.valueOf(emp.getEmpid()));
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
	 * 
	 * <li>说明：方法实现功能说明
	 * tream    --班组
	 * hq       --部
	 * plant    --车间
	 * oversea  --段
	 * branch   --局
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-10-31
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param orgDegree 机构级别
	 * @return 返回值说明
	 */
	private String findOrgDegreeIcon(String orgDegree){
		if(StringUtil.isNullOrBlank(orgDegree)){
			return "";
		} 
		//部
		if("hq".equals(orgDegree)){
			return "judge1Icon";
		}
		//局
		if("branch".equals(orgDegree)){
			return "judge2Icon";
		}
		//段
		if("oversea".equals(orgDegree)){
			return "judge3Icon";
		}
		//车间
		if("plant".equals(orgDegree)){
			return "silver1Icon";
		}
		//班组
		if("tream".equals(orgDegree)){
			return "silver2Icon";
		}
		//科室
		if("office".equals(orgDegree)){
			return "silver3Icon";
		}
		return "";
	}
	
	/**
	 * <li>说明：新增/编辑机构信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-12
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param T t 实体对象
	 * @return void 
	 * @throws BusinessException, NoSuchFieldException
	 */	
	public void saveOrUpdate(OmOrganization org) throws BusinessException, NoSuchFieldException {
		Date now = new Date();  
		OmOrganization porg = null;
		if(org.getOrgid()==null){
			org.setIsleaf("y"); //新增数据为叶子级别机构
			org.setCreatetime(now); //创建时间
			org.setOrglevel(1L);//初始设置orgLevel
			org.setSubcount(0L);//新增机构没有子机构
			if(org.getParentorgid()!=null){
				porg = this.getModelById(org.getParentorgid());//获取父级机构实体对象
				if(porg!=null){
					porg.setIsleaf("n");
					Long count = porg.getSubcount()==null?0L:porg.getSubcount();
					porg.setSubcount(count+1); //父级机构的总子机构数+1
					this.daoUtils.update(porg); //更新父机构
					org.setOrglevel(porg.getOrglevel() == null ? 1L : (porg.getOrglevel()+1)); //更新子机构的orglevel = 父机构orglevel+1
				} else {
					org.setOrglevel(1L); //更新子机构的orglevel，没有父机构orglevel，则子机构从1级开始
				}
			}
			this.daoUtils.getHibernateTemplate().saveOrUpdate(org);
			//当子机构保存之后，获取其ID
			if(porg!=null){
				String porgseq = StringUtil.isNullOrBlank(porg.getOrgseq())?"":porg.getOrgseq();
				org.setOrgseq(porgseq.concat(String.valueOf(org.getOrgid())).concat(".")); //根据父机构orgseq，产生子机构的orgseq
			} else {
				org.setOrgseq("."+org.getOrgid()+".");
			}
		}
		org.setLastupdate(now); //最后更新时间
		org.setUpdator(SystemContext.getAcOperator().getOperatorid()); //当前操作人ID
		this.daoUtils.update(org);
	}	
	
	/**
	 * 删除组织机构下的所有关联数据信息
	 */
	public void deleteByIds(Serializable... ids) throws BusinessException {
		try {
			if(ids == null) return;
			Long upperOrgid = null;
			//循环删除选中机构
			for(Serializable id : ids){
				if(StringUtil.isNullOrBlank(String.valueOf(id))) continue;
				Long orgid = Long.valueOf(String.valueOf(id));
				OmOrganization orgObj = omOrganizationManager.getModelById(orgid); //根据ID获取需要删除的机构实体
				if(orgObj == null) continue;
				upperOrgid = orgObj.getParentorgid(); //记录待删除机构的上级机构ID
				iterationDeleteChildOrg(orgObj); //调用递归方法删除所有子机构
				/* 更新当前删除机构的上层机构的subcount */
				if(upperOrgid == null) continue;
				OmOrganization porg = omOrganizationManager.getModelById(upperOrgid); //获取上层机构实体
				String hql = "from OmOrganization where parentorgid = " + upperOrgid;
				List <OmOrganization> plist = this.daoUtils.find(hql); //根据上层岗位ID查询其子岗位总数
				if( plist == null || plist.size()<1 ){
					porg.setSubcount(0L);
					porg.setIsleaf("y");
				} else {
					porg.setSubcount(Long.valueOf(String.valueOf(plist.size())));
					porg.setIsleaf("n");
				}
				this.daoUtils.update(porg); //更新subcount
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}
	
	/**
	 * <li>说明：递归方法，根据入参的机构，迭代删除其下属所有子机构
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-4
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param org 机构实体
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	private void iterationDeleteChildOrg(OmOrganization org) throws BusinessException{
		String hql = "from OmOrganization where parentorgid = " + org.getOrgid();
		List <OmOrganization> childOrg = this.daoUtils.find(hql); //获取入参机构的下级子机构
		if(childOrg==null||childOrg.size()<1) {
			positionManager.deleteByIds(positionManager.getChildPosIds(org.getOrgid()));//【调用方法删除机构下的岗位】
			empOrgManager.deleteByOrganizationId(org.getOrgid()); //【删除机构下的直属人员】
			this.daoUtils.getHibernateTemplate().delete(org); //【执行删除机构】
			return; //如果未找到子项，跳出递归
		}
		for(OmOrganization _o : childOrg){
			iterationDeleteChildOrg(_o); //递归调用，继续向下层查找
		}
		positionManager.deleteByIds(positionManager.getChildPosIds(org.getOrgid()));//【调用方法删除机构下的岗位】
		empOrgManager.deleteByOrganizationId(org.getOrgid());//【删除机构下的直属人员】
		this.daoUtils.getHibernateTemplate().delete(org); //【执行删除机构】
	}
	
	/**
	 * <li>说明：查询入参中各机构的子机构orgid，并拼装为逗号分隔的字符串后返回
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param orgids 逗号分隔的orgid字符串
	 * @return 包含入参orgid的各机构及其下属机构的orgid字符串
	 * @throws 抛出异常列表
	 */
	public String getChildOrgIds(String orgids){
		StringBuffer childOrgidAry = null;
		if(!StringUtil.isNullOrBlank(orgids)) {
			childOrgidAry = new StringBuffer();
			//使用oracle内置函数，递归查询入参中各机构的下属各级机构的orgid
			String sql = "SELECT ORGID FROM OM_ORGANIZATION START WITH ORGID IN ("+orgids+") CONNECT BY PRIOR ORGID = PARENTORGID";
			List _t = daoUtils.executeSqlQuery(sql);
			for(Object obj : _t){
				childOrgidAry.append(obj.toString()).append(",");
			}
			return childOrgidAry.toString().substring(0, childOrgidAry.toString().lastIndexOf(","));
		}
		return null;
	}
	
	/**
	 * <li>说明：根据机构Id，查询机构明细信息，改方法用于机构人员管理中的当前机构信息表单，因需要显示机构主管岗位，所以关联岗位表进行查询
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
	public OmOrganization findCurrentOrgInfo(Long orgid) throws BusinessException {
		String hql = "select org.orgid as \"orgid\", org.parentorgid as \"parentorgid\", org.orgcode as \"orgcode\", " +
		" org.orgname as \"orgname\", org.orglevel as \"orglevel\", org.orgdegree as \"orgdegree\", " +
		" org.orgseq as \"orgseq\", org.orgtype as \"orgtype\", org.orgaddr as \"orgaddr\", " +
		" org.zipcode as \"zipcode\", org.manaposition as \"manaposition\", org.managerid as \"managerid\"," +
		" org.orgmanager as \"orgmanager\", org.linkman as \"linkman\", org.linktel as \"linktel\", " +
		" org.email as \"email\", org.weburl as \"weburl\", org.startdate as \"startdate\"," +
		" org.enddate as \"enddate\", org.status as \"status\", org.area as \"area\", org.createtime as \"createtime\", " +
		" org.lastupdate as \"lastupdate\", org.updator as \"updator\"," +
		" org.sortno as \"sortno\", org.isleaf as \"isleaf\", org.subcount as \"subcount\", org.remark as \"remark\"," +
		" posi.posiname as \"posiname\" " +
		" from Om_Organization org left join Om_Position posi on org.manaposition = posi.positionid where org.orgid = '"+orgid+"'";
	
		List <Object []> list = this.daoUtils.executeSqlQuery(hql);
		OmOrganization org = new OmOrganization();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(Object [] obj : list){
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[0]))) org.setOrgid(Long.valueOf(String.valueOf(obj[0])));//orgid
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[1]))) org.setParentorgid(Long.valueOf(String.valueOf(obj[1])));//parentorgid
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[2]))) org.setOrgcode(String.valueOf(obj[2]));//orgcode
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[3]))) org.setOrgname(String.valueOf(obj[3])); //orgname
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[4]))) org.setOrglevel(Long.valueOf(String.valueOf(obj[4])));//orglevel
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[5]))) org.setOrgdegree(String.valueOf(obj[5]));//orgdegree
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[6]))) org.setOrgseq(String.valueOf(obj[6]));//orgseq
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[7]))) org.setOrgtype(String.valueOf(obj[7]));//orgtype
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[8]))) org.setOrgaddr(String.valueOf(obj[8]));//orgaddr
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[9]))) org.setZipcode(String.valueOf(obj[9]));//zipcode
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[10]))) org.setManaposition(Long.valueOf(String.valueOf(obj[10])));//manaposition
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[11]))) org.setManagerid(Long.valueOf(String.valueOf(obj[11])));//managerid
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[12]))) org.setOrgmanager(String.valueOf(obj[12]));//orgmanager
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[13]))) org.setLinkman(String.valueOf(obj[13]));//linkman
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[14]))) org.setLinktel(String.valueOf(obj[14]));//linktel
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[15]))) org.setEmail(String.valueOf(obj[15]));//email
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[16]))) org.setWeburl(String.valueOf(obj[16]));//weburl
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[19]))) org.setStatus(String.valueOf(obj[19]));//status
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[20]))) org.setArea(String.valueOf(obj[20]));//area
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[23]))) org.setUpdator(Long.valueOf(String.valueOf(obj[23])));//updator
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[24]))) org.setSortno(Long.valueOf(String.valueOf(obj[24])));//sortno
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[25]))) org.setIsleaf(String.valueOf(obj[25])); //isleaf
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[26]))) org.setSubcount(Long.valueOf(String.valueOf(obj[26])));//subcount
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[27]))) org.setRemark(String.valueOf(obj[27]));//remark
			if(!StringUtil.isNullOrBlank(String.valueOf(obj[28]))) org.setPosiname(String.valueOf(obj[28]));//posiname
			try {
				if(!StringUtil.isNullOrBlank(String.valueOf(obj[17]))) org.setStartdate(sdf.parse(String.valueOf(obj[17])));//startdate
				if(!StringUtil.isNullOrBlank(String.valueOf(obj[18]))) org.setEnddate(sdf.parse(String.valueOf(obj[18])));//enddate
				if(!StringUtil.isNullOrBlank(String.valueOf(obj[21]))) org.setCreatetime(sdf.parse(String.valueOf(obj[21])));//createtime
				if(!StringUtil.isNullOrBlank(String.valueOf(obj[22]))) org.setLastupdate(sdf.parse(String.valueOf(obj[22])));//lastupdate
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			break; //如有重复，只取第一条
		}
		return org;
	}
	
	/**
	 * <li>说明：当用户点击机构TreeNode时，调用该方法获取所选机构列表信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 */
	public Page findOrgList(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer(); //查询条件字符串
		String sql = "select org.orgid as \"orgid\", org.parentorgid as \"parentorgid\", org.orgcode as \"orgcode\", " +
		" org.orgname as \"orgname\", org.orglevel as \"orglevel\", org.orgdegree as \"orgdegree\", " +
		" org.orgseq as \"orgseq\", org.orgtype as \"orgtype\", org.orgaddr as \"orgaddr\", " +
		" org.zipcode as \"zipcode\", org.manaposition as \"manaposition\", org.managerid as \"managerid\"," +
		" org.orgmanager as \"orgmanager\", org.linkman as \"linkman\", org.linktel as \"linktel\", " +
		" org.email as \"email\", org.weburl as \"weburl\", org.startdate as \"startdate\"," +
		" org.enddate as \"enddate\", org.status as \"status\", org.area as \"area\", org.createtime as \"createtime\", " +
		" org.lastupdate as \"lastupdate\", org.updator as \"updator\"," +
		" org.sortno as \"sortno\", org.isleaf as \"isleaf\", org.subcount as \"subcount\", org.remark as \"remark\"," +
		" posi.posiname as \"posiname\" " +
		" from Om_Organization org left join Om_Position posi on org.manaposition = posi.positionid where 1=1";
		String totalsql = "select count(*) as \"rowcount\" from (?)";
		
		//拼接查询参数
		if (whereList != null && whereList.size() > 0) {
			for (Condition condition : whereList) {
				searchParam.append(" and ");
				String sign = "";
				//如果匹配参数为空或者为"1" , 则转为 "=" 条件 
				if(StringUtil.isNullOrBlank(String.valueOf(condition.getCompare()))||"1".equals(String.valueOf(condition.getCompare()))){
					searchParam.append(condition.getPropName()).append(" ");
					sign = Condition.getCompare(1) + " '?' " ;  //转为 "=" 条件 
				} else if ("8".equals(String.valueOf(condition.getCompare()))){
					searchParam.append(condition.getPropName()).append(" ");
					sign = Condition.getCompare(8) + " '%?%' "; //转为 "like" 条件 
				} else if ("21".equals(String.valueOf(condition.getCompare()))){
					searchParam.append(condition.getPropName()).append(" ");
					sign = Condition.getCompare(21);            //转为 is null 条件
				}else if ("30".equals(String.valueOf(condition.getCompare()))){
					sign = condition.getSql();					//转为 sql 条件
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
	 * 
	 * <li>说明：获取当前机构的下级(人员管理-机构调动-机构树)
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public List <Map> getChildNodesForWidget(Long nodeid,String nodetype, String widgetType) throws BusinessException{
		List <OmOrganization> childNodeOrgList = null; //组织机构
		List <OmPosition> childNodePosList = null;     //组织机构直属岗位
		List <Map> list = new ArrayList<Map>();        //实例化一个空的List,传入后需函数中装载数据
		/* * * * * * * * * * * * * * * * * * * * * * * * * * * */
		/*     如果当前节点层级是机构类型，则通过查询接口获取其下级机构   */
		/* * * * * * * * * * * * * * * * * * * * * * * * * * * */
		if(!StringUtil.isNullOrBlank(widgetType)&&("1".equals(widgetType)||"2".equals(widgetType))){
			if(nodetype.equals("org") && nodeid == null){
				//当nodeid为空时，查询
				return buildOrgTreeByWidget(omOrganizationManager.findRoot(),list,widgetType); //如orgid==null，则调用接口获取机构的根
			} else if(nodetype.equals("org")) {
				childNodeOrgList = omOrganizationManager.findAllChildsById(nodeid); //根据orgid,调用接口查询获取其下级机构
				list = buildOrgTreeByWidget(childNodeOrgList,list,widgetType);
			}
		}
		if(!StringUtil.isNullOrBlank(widgetType)&&"2".equals(widgetType)){
			//因机构下可能有下级机构，也可能会有岗位，所以此处将通过查询接口获取下级岗位及直属人员
			if(nodetype.equals("org")||nodetype.equals("pos")){
				if(nodetype.equals("org")){
					childNodePosList = omPositionManager.findPertainToOrg(nodeid);   //根据orgid,调用接口查询获取其直属岗位
					list = buildPosiTreeByWidget(childNodePosList,list,widgetType);
				}
				if(nodetype.equals("pos")){
					childNodePosList = omPositionManager.findChildPosition(nodeid);   //根据岗位id,调用接口查询获取其直属岗位
					list = buildPosiTreeByWidget(childNodePosList,list,widgetType);
				}
			}
		}
		return list;
	}
	
	/**
	 * 
	 * <li>说明：获取机构树，但不包含当前机构的所有下级子机构(机构调整)
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param nodeid 当前机构id
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public List <Map> getOrgTreeForAdjust(Long nodeid, String [] orgids) throws BusinessException{
		List <Map> orgList = new ArrayList<Map>();
		List <OmOrganization> list = null;
		if(nodeid == null) list = omOrganizationManager.findRoot();
		else list = omOrganizationManager.findAllChildsById(nodeid);
		Map map = null;
		for(OmOrganization org : list){
			map = new LinkedHashMap();
			map.put("id", org.getOrgid());
			map.put("text", org.getOrgname());
			map.put("leaf", StringUtil.isNullOrBlank(org.getIsleaf())||"y".equals(org.getIsleaf().toLowerCase())?true:false);
			for(int i=0;i<orgids.length;i++){
				if(org.getOrgseq().indexOf("."+orgids[i]+".")!=-1)
					map.put("disabled", true);
			}
			map.put("orgid", org.getOrgid());
			map.put("children", getOrgTreeForAdjust(org.getOrgid(),orgids));
			orgList.add(map);
		}
		return orgList;
	}
	
	/**
	 * <li>说明：机构迁移业务类， 将一个或多个机构移动到目标机构下，即修改其parentorgid，同时，更新被移动机构的源归属机构和目标机构的机构属性
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param orgid 目标机构
	 * @param ids 待更新机构id序列
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public Boolean updateOrgAdjust(Long orgid, Serializable... ids) throws BusinessException {
		if(ids == null) return false; //入参异常
		List <OmOrganization> childOrg = null; //子机构列表
		Long _tempPid = null; //原上级机构ID
		OmOrganization target = null; //目标机构实体
		for(Serializable id : ids){
			if(orgid!=null){ //如目标机构不是根，则需要更新目标机构的属性
				/** 更新目标机构的属性 */
				childOrg = omOrganizationManager.findAllChildsById(orgid);
				target = omOrganizationManager.getModelById(orgid); //获取目标机构实体
				target.setIsleaf("n"); //因为有子机构迁入，所以目标机构一定不会是叶子机构
				target.setSubcount(target.getSubcount()==null?1L:target.getSubcount()+1);//新的子机构迁入，则子机构数目+1
				this.daoUtils.getHibernateTemplate().update(target); //【执行更新】
			}
			/** 更新被调整机构的属性 */
			OmOrganization org = omOrganizationManager.getModelById(Long.valueOf(String.valueOf(id))); //根据ID获取实体
			if(org == null) continue;
			_tempPid = org.getParentorgid(); //提取机构原所属上级机构
			org.setParentorgid(orgid);//设定新机构归属
			org.setOrgseq(target != null?(target.getOrgseq()+org.getOrgid()+"."):("."+org.getOrgid()+".")); //更新机构的seq
			org.setOrglevel(target == null || target.getOrglevel()==null?1L:target.getOrglevel()+1); //更新机构level
			this.daoUtils.getHibernateTemplate().update(org); //【执行更新】
			/** 递归更新被拖动机构的下属各级机构seq信息 */
			iterationUpdateChildOrg(org);
			
			/** 更新原上级机构的属性， 包括isleaf， subcount等字段值 */
			if(_tempPid==null) continue;
			OmOrganization original = omOrganizationManager.getModelById(_tempPid); //获取原上级机构实体
			if(original == null) continue;
			childOrg = omOrganizationManager.findAllChildsById(_tempPid);
			if(childOrg != null && childOrg.size()>0){
				original.setSubcount(Long.valueOf(String.valueOf(childOrg.size()))); //更新子机构数
			} else {
				original.setSubcount(0L);
				original.setIsleaf("y");
			}
			this.daoUtils.getHibernateTemplate().update(original);//【执行更新】
		}
		return null;
	}
	
	/**
	 * <li>说明：根据数据，构建组织机构树（机构部分）
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-10-31
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param list 组织机构数据集合
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	private List <Map> buildOrgTreeByWidget(List <OmOrganization> list, List <Map> orgList, String widgetType){
		if (list == null) return null;
		List<Map> children = orgList;
		Map node = null;
		for(OmOrganization org : list){
			node = new LinkedHashMap();
			node.put("id", "o_"+String.valueOf(org.getOrgid())); //机构ID
			node.put("text", org.getOrgname());    //机构名称
			if(!StringUtil.isNullOrBlank(widgetType)&&"2".equals(widgetType)){
				node.put("disabled", true);
			}
			if(!StringUtil.isNullOrBlank(org.getIsleaf())&&"N".equals(org.getIsleaf().toUpperCase())){
				node.put("leaf", false); //当前机构含有子项
			} else {
				if(!StringUtil.isNullOrBlank(widgetType)&&"2".equals(widgetType)){
					//查询其是否存在下级岗位
					List <OmPosition> childPosition = omPositionManager.findPertainToOrg(org.getOrgid());   //根据orgid,调用接口查询获取其直属岗位
					if(childPosition!=null&&childPosition.size()>0){
						node.put("leaf", false); //当前机构含有子项
					} else {
						node.put("leaf", true);
					}
				} else {
					node.put("leaf", true); //当前机构没有子项
				}
			}
			//node.put("leaf", !StringUtil.isNullOrBlank(org.getIsleaf())&&"N".equals(org.getIsleaf().toUpperCase())?false:true); //是否叶子节点
			node.put("iconCls", findOrgDegreeIcon(org.getOrgdegree())); //根据组织等级构建树的节点图标
			//=======================//
			node.put("orgid", String.valueOf(org.getOrgid())); //机构ID
			node.put("parentorgid", String.valueOf(org.getParentorgid())); //父Id
			node.put("orgcode", org.getOrgcode()); //机构代码
			node.put("orgname", org.getOrgname()); //机构名称
			node.put("orglevel", org.getOrglevel()); //机构层次
			node.put("orgdegree", org.getOrgdegree()); //机构等级
			node.put("orgseq", org.getOrgseq()); //机构序列
			node.put("orgtype", org.getOrgtype()); // 机构类型
			node.put("status", org.getStatus()); //机构状态
			node.put("isleaf", org.getIsleaf()); //是否叶子节点
			node.put("nodetype", "org");   //存入标识,该节点是机构
			//======================//
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
	private List <Map> buildPosiTreeByWidget(List <OmPosition> list, List <Map> posList, String widgetType){
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
				node.put("leaf", true);
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
	 * <li>说明：递归方法，根据入参的机构，查询其下级机构，并更新下级机构的orgseq
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-4
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param org 机构实体
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	private void iterationUpdateChildOrg(OmOrganization org) throws BusinessException{
		String hql = "from OmOrganization where parentorgid = " + org.getOrgid();
		List <OmOrganization> childOrg = this.daoUtils.find(hql); //获取入参机构的下级子机构
		if(childOrg==null||childOrg.size()<1) return; //如果未找到子项，跳出递归
		for(OmOrganization _o : childOrg){
			String upperOrgSeq = org.getOrgseq();  //从入参中获取上层orgseq
			_o.setOrgseq(upperOrgSeq+_o.getOrgid()+"."); //构建当前机构的新orgseq
			Long upperOrgLevel = org.getOrglevel(); //从入参中获取上层level
			_o.setOrglevel(upperOrgLevel==null?1L:(upperOrgLevel+1));
			this.daoUtils.update(_o);  //更新当前机构
			iterationUpdateChildOrg(_o); //递归调用，继续向下层查找
		}
	}
	
	/**
	 * <li>说明：返回已配置为当前角色的机构列表
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
	public Page findOrganizationListByRole(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer();
		String sql = " from OmOrganization org, OmPartyrole paryrole where org.orgid = paryrole.id.partyid and paryrole.id.partytype = 'organization' and paryrole.id.roleid = '?'";
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
		String hql = "select org ".concat(sql); //构建数据查询语句
		return super.findPageList(totalHql, hql, start, limit);
	}
	
	/**
	 * <li>说明：返回未配置为当前角色的机构列表
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
	public Page findOrganizationListByRole2(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer();
		String sql = " from OmOrganization org where org.orgid not in (select id.partyid from OmPartyrole where id.partytype = 'organization' and id.roleid = '?')";
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
		String hql = "select org ".concat(sql); //构建数据查询语句
		return super.findPageList(totalHql, hql, start, limit);
	}
}
