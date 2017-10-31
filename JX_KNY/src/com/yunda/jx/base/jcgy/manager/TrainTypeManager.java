package com.yunda.jx.base.jcgy.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.IbaseComboTree;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.jx.base.jcgy.entity.TrainType;
import com.yunda.jx.component.manager.OmOrganizationSelectManager;
import com.yunda.jx.webservice.stationTerminal.base.entity.UndertakeTrainTypeBean;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainType业务类,机车车型编码
 * <li>创建人：王治龙
 * <li>创建日期：2012-10-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="trainTypeManager")
public class TrainTypeManager extends JXBaseManager<TrainType, TrainType> implements IbaseCombo, IbaseComboTree{
	private static final String PERCENT_END = "%'";

	private static final String YES = "yes";

	/** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
     /** 组织机构业务类：OmOrganizationManager */
    @Resource
    private OmOrganizationManager omOrganizationManager;
	
	/**
	 * <li>说明：分页查询，返回实体类的分页列表对象，基于单表实体类分页查询
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-10-24
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchEntity 包装了实体类查询条件的对象 ; 
     * @param rcTypeId 修程类型主键（过滤修程类型可用车型）
     * @param undertakeOrgId 承修单位主键（过滤承修车型可用车型）
	 * @return Page<E> 分页查询列表
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Page findPageTrainList(final SearchEntity<TrainType> searchEntity ,final String rcTypeId, final String undertakeOrgId) throws BusinessException{
		HibernateTemplate template = this.daoUtils.getHibernateTemplate();
//		this.daoUtils.getHibernateTemplate().get(TrainType.class, trainType.getTypeID());
		return (Page)template.execute(new HibernateCallback(){
			public Page doInHibernate(Session s){
				try {
                    StringBuffer sql = new StringBuffer();
                    sql.append("1=1")  ;  //过滤修程类型可用车型
                    StringBuffer sql2 = new StringBuffer(); 
                    sql2.append("1=1") ; //过滤承修车型可用车型
                    if(!"".equals(rcTypeId)){
                        sql.append(" and T_TYPE_ID not in (select nvl(t.train_type_idx,'-1') from JCZL_RC_TYPE_TRAIN_TYPE t");
                        sql.append(" where t.record_status=0 and t.rc_type_id = '").append(rcTypeId).append("')");
                                
                    }
                    if(!"".equals(undertakeOrgId)){
                        sql2.append(" and T_TYPE_ID not in (select nvl(t.train_type_idx,'-1') from JCZL_UNDERTAKE_TRAIN_TYPE t");
                        sql2.append(" where t.record_status=0 and t.undertake_orgid  = '").append(undertakeOrgId).append("')");
                    }
                    TrainType entity = searchEntity.getEntity();
					Example exp = Example.create(entity)
						.enableLike().enableLike(MatchMode.ANYWHERE);
					//查询总记录数
					int total = ((Integer)s.createCriteria(entity.getClass())
						.add(exp)
                        .add(Restrictions.sqlRestriction(sql.toString()))
                        .add(Restrictions.sqlRestriction(sql2.toString()))
						.setProjection(Projections.rowCount())
						.uniqueResult())
						.intValue();
					//分页列表
					Criteria criteria = s.createCriteria(entity.getClass()).add(exp)
                        .add(Restrictions.sqlRestriction(sql.toString()))
                        .add(Restrictions.sqlRestriction(sql2.toString()))
						.setFirstResult(searchEntity.getStart())
						.setMaxResults(searchEntity.getLimit());
					//设置排序规则
					Order[] orders = searchEntity.getOrders();
					if(orders != null){
						for (Order order : orders) {
							criteria.addOrder(order);
						}					
					}
                    criteria.addOrder(Order.asc("typeID"));
					return new Page(total, criteria.list());
				} catch (Exception e) {
					ExceptionUtil.process(e,logger);
				}
				return null;
			}
		});
    }
    /**
     * 
     * <li>说明：获取下拉列表前台store所需Map对象
     * <li>创建人：程梅
     * <li>创建日期：2012-10-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param queryValue combo文本框输入的字符串
     * @param queryParams 查询参数Map对象
     * @param start 开始行
     * @param limit 每页记录数
     * @param queryHql 查询Hql字符串
     * @param isCx 是否查询承修车型
     * @param orgseq 机构序列
     * @return Map<String,Object> 下拉列表前台store所需Map对象
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> page(String queryValue, Map queryParams, int start, int limit, String queryHql,String isCx, String orgseq) throws ClassNotFoundException {
        StringBuffer hql = new StringBuffer();
        // 拼接查询sql，如queryHql配置项不为空则直接执行queryHql
        if (!StringUtil.isNullOrBlank(queryHql)) {
            hql.append(queryHql);
        } else { 
            //查询承修车型
            if(YES.equals(isCx)){
               hql.append(getHql(orgseq));
            }else if("no".equals(isCx)){
                //查询所有车型
                hql.append("select t from TrainType t where 1=1 ");
            }
            if (!StringUtil.isNullOrBlank(queryValue)) {
                hql.append(" and t.shortName like '%").append(queryValue).append(PERCENT_END);
            }
//          queryHql配置项为空则按queryParams配置项拼接Hql
            if (!queryParams.isEmpty()) {
                Set<Map.Entry<String, String>> set = queryParams.entrySet();
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (!StringUtil.isNullOrBlank(value)) {
                        hql.append(" and t.").append(key).append(" = '").append(value).append("'");
                    }
                }
            }
        }
        //根据hql构造totalHql
        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*)");
        totalHql.append(hql.toString().substring(beginPos));
        totalHql.append("  order by t.shortName");
        hql.append(" order by t.shortName");
        Page page = findPageList(totalHql.toString(), hql.toString(), start, limit);
        return page.extjsStore();
    }
    /**
     * 
     * <li>说明：查询承修车型hql
     * <li>创建人：程锐
     * <li>创建日期：2013-2-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param orgseq 组织序列
     * @return 查询承修车型hql字符串
     */
    private String getHql(String orgseq){
        StringBuilder hql = new StringBuilder();
        hql.append("select t from TrainType t,UndertakeTrainType u where 1=1 and t.typeID = u.trainTypeIDX and u.recordStatus=0 ");
        if (!StringUtil.isNullOrBlank(orgseq)) {
            hql.append(" and u.undertakeOrgId in (select orgid from OmOrganization where orgseq like '").append(orgseq).append(
                "%' and status='running')");
        }
        return hql.toString();
    }
    /**
     * <li>说明：重写getBaseComboData，获取下拉列表前台store所需Map对象
     * <li>创建人：程锐
     * <li>创建日期：2014-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param req request   
     * @param start 开始行
     * @param limit 每页记录数
     * @return Map<String,Object> 下拉列表前台store所需Map对象
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit) throws Exception{
    	String orgCode = JXSystemProperties.OVERSEA_ORGCODE;
        OmOrganization org = omOrganizationManager.findOrgForCode(orgCode);
        String queryHql = req.getParameter("queryHql");
        String queryParams = req.getParameter("queryParams");
        Map queryParamsMap = new HashMap();
        if (!StringUtil.isNullOrBlank(queryParams)) {
            queryParamsMap = JSONUtil.read(queryParams, Map.class);
        }
        String isCx = YES;
        if(queryParamsMap.get("isCx") != null){
        	isCx = String.valueOf(queryParamsMap.get("isCx"));
            queryParamsMap.remove("isCx");
        }        
//      query参数是获取EXTJS的combox控件捕获的键盘输入文字
        String queryValue = StringUtil.nvlTrim(req.getParameter("query"), "");
    	StringBuffer hql = new StringBuffer();
        // 拼接查询sql，如queryHql配置项不为空则直接执行queryHql
        if (!StringUtil.isNullOrBlank(queryHql)) {
            hql.append(queryHql);
        } else { 
            //查询承修车型
            if(YES.equals(isCx)){
               hql.append(getHql(org.getOrgseq()));
            }else if("no".equals(isCx)){
                //查询所有车型
                hql.append("select t from TrainType t where 1=1 ");
            }
            if (!StringUtil.isNullOrBlank(queryValue)) {
                hql.append(" and t.shortName like '%").append(queryValue).append(PERCENT_END);
            }
//          queryHql配置项为空则按queryParams配置项拼接Hql
            if (!queryParamsMap.isEmpty()) {
                Set<Map.Entry<String, String>> set = queryParamsMap.entrySet();
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (!StringUtil.isNullOrBlank(value)) {
                        hql.append(" and t.").append(key).append(" = '").append(value).append("'");
                    }
                }
            }
        }
        //根据hql构造totalHql
        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*)");
        totalHql.append(hql.toString().substring(beginPos));
        totalHql.append("  order by t.shortName");
        hql.append(" order by t.shortName");
        Page page = findPageList(totalHql.toString(), hql.toString(), start, limit);
        return page.extjsStore();
    }
    /**
     * 
     * <li>说明：分页查询列表
     * <li>创建人：程锐
     * <li>创建日期：2012-11-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 车型实体对象包装类
     * @param orgseq 组织序列
     * @return 分页查询类表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public Page pageList(final SearchEntity<TrainType> searchEntity, String orgseq) throws BusinessException{
        StringBuffer hql = new StringBuffer();
        hql.append(getHql(orgseq));
        TrainType trainType = searchEntity.getEntity();
        if(!StringUtil.isNullOrBlank(trainType.getTypeName())){
            hql.append(" and t.typeName like '%").append(trainType.getTypeName().toUpperCase()).append(PERCENT_END);
        }
        if(!StringUtil.isNullOrBlank(trainType.getShortName())){
            hql.append(" and t.shortName like '%").append(trainType.getShortName().toUpperCase()).append(PERCENT_END);
        }
        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*)");
        totalHql.append(hql.toString().substring(beginPos));
        return findPageList(totalHql.toString(), hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    /**
     * 
     * <li>说明：获取车型列表
     * <li>创建人：程锐
     * <li>创建日期：2013-1-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return List<TrainType> 车型列表
     */
    @SuppressWarnings("unchecked")
    public List<TrainType> getTrainTypeList(){
        String orgCode = JXSystemProperties.OVERSEA_ORGCODE;
        OmOrganization org = omOrganizationManager.findOrgForCode(orgCode);
        String hql = getHql(org.getOrgseq());
        return daoUtils.find(hql);
    }
    
    /**
     * <li>方法名：getTrainType
     * <li>@param shortname
     * <li>@return
     * <li>返回类型：TrainType
     * <li>说明：根据车型简称获取车型的信息
     * <li>创建人：罗鑫
     * <li>创建日期：2012-8-6
     * <li>修改人： 
     * <li>修改日期：
     */
    public TrainType getTrainType(String shortname){
        return (TrainType)daoUtils.findSingle("from TrainType where shortname='"+shortname+"'");
    }
   
    /**
     * 
     * <li>说明：获取承修车型列表
     * <li>创建人：程锐
     * <li>创建日期：2014-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 分页对象
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public Page queryUndertakeTrainTypeList() throws Exception{
    	OmOrganization org = OmOrganizationSelectManager.getOrgByOrgcode(JXSystemProperties.OVERSEA_ORGCODE);
        Map map = page("",JSONUtil.read("{}", Map.class), 0, 100, "",YES, org.getOrgseq());
        List<UndertakeTrainTypeBean> list = new ArrayList<UndertakeTrainTypeBean>();
        list = BeanUtils.copyListToList(UndertakeTrainTypeBean.class, (List)map.get("root"));
        return new Page((Integer)(map.get("totalProperty")),list);
    }
    
    /**
     * <li>方法说明：树查询车型
     * <li>方法名：findTrainTypeForTree
     * @return
     * <li>创建人： 张凡
     * <li>创建日期：2015-10-23
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> findTrainTypeForTree(){
    	String hql = "select t from TrainType t, UndertakeTrainType u where u.trainTypeIDX = t.typeID and u.recordStatus=0";
    	List<TrainType> list = daoUtils.find(hql);
    	List<Map<String, Object>> nodes = new ArrayList<Map<String,Object>>(list.size());
    	for(TrainType t : list){
    		Map<String, Object> m = new HashMap<String, Object>();
    		m.put("id", t.getTypeID());
    		m.put("text", t.getShortName());
    		m.put("trainTypeIdx", t.getTypeID());
    		m.put("trainTypeName", t.getTypeName());
    		m.put("trainShortName", t.getShortName());
    		m.put("repairType", t.getRepairType());
    		m.put("leaf", true);
    		nodes.add(m);
    	}
    	return nodes;
    }
  
   
    /**
     * <li>说明：重写getBaseComboTree，获取下拉树前台store所需List<HashMap>对象
     * <li>创建人：张迪
     * <li>创建日期：2016-9-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param req 请求
     * @return List<HashMap> 下拉树前台store所需List<HashMap>对象
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<HashMap> getBaseComboTree(HttpServletRequest req) throws Exception{
        String orgCode = JXSystemProperties.OVERSEA_ORGCODE;
        OmOrganization org = omOrganizationManager.findOrgForCode(orgCode);
        String queryHql = req.getParameter("queryHql");
        String queryParams = req.getParameter("queryParams");
        String isChecked = req.getParameter("isChecked");
        String isCx = req.getParameter("isCx");
        Map queryParamsMap = new HashMap();
        if (!StringUtil.isNullOrBlank(queryParams)) {
            queryParamsMap = JSONUtil.read(queryParams, Map.class);
        }
//      query参数是获取EXTJS的combox控件捕获的键盘输入文字
        String queryValue = StringUtil.nvlTrim(req.getParameter("query"), "");
        StringBuffer hql = new StringBuffer();
        // 拼接查询sql，如queryHql配置项不为空则直接执行queryHql
        if (!StringUtil.isNullOrBlank(queryHql)) {
            hql.append(queryHql);
        } else { 
            //查询承修车型
            if(YES.equals(isCx)){
               hql.append(getHql(org.getOrgseq()));
            }else if("no".equals(isCx)){
                //查询所有车型
                hql.append("select t from TrainType t where 1=1 ");
            }
            if (!StringUtil.isNullOrBlank(queryValue)) {
                hql.append(" and t.shortName like '%").append(queryValue).append(PERCENT_END);
            }
            // queryHql配置项为空则按queryParams配置项拼接Hql
            if (!queryParamsMap.isEmpty()) {
                Set<Map.Entry<String, String>> set = queryParamsMap.entrySet();
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (!StringUtil.isNullOrBlank(value)) {
                        hql.append(" and t.").append(key).append(" = '").append(value).append("'");
                    }
                }
            }
        }
        List<TrainType> list = daoUtils.find(hql.toString());
        List<HashMap> children = new ArrayList<HashMap>();
        for(TrainType t : list){
            HashMap nodeMap = new HashMap();
            nodeMap.put("id", t.getTypeID());
            nodeMap.put("text", t.getShortName());
            nodeMap.put("trainTypeIdx", t.getTypeID());
            nodeMap.put("trainTypeName", t.getTypeName());
            nodeMap.put("trainShortName", t.getShortName());
            nodeMap.put("repairType", t.getRepairType());
            nodeMap.put("leaf", true);
            if(!StringUtil.isNullOrBlank(isChecked))
                nodeMap.put("checked", false);
            children.add(nodeMap);
        }
        return children;
    }
}