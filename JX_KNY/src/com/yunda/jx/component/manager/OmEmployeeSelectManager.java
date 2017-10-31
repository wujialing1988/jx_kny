package com.yunda.jx.component.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.baseapp.ChineseCharToEn;
import com.yunda.baseapp.ChineseCharToEn.Sensitive;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.IAcOperatorManager;
import com.yunda.frame.yhgl.manager.IOmEmployeeManager;
import com.yunda.frame.yhgl.manager.IOmOrganizationManager;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicket;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 人员选择控件业务类
 * <li>创建人：程锐
 * <li>创建日期：2012-9-2
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "omEmployeeSelectManager")
public class OmEmployeeSelectManager extends JXBaseManager<OmEmployee, OmEmployee> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
	/** 组织机构数据查询功能接口 */
	@Resource
	private IOmOrganizationManager omOrganizationManager;
	
	/** 人员业务类：omEmployeeManager */
	@Resource(name="omEmployeeManager")
	private IOmEmployeeManager omEmployeeManager;
	
	/** 操作员业务类：acOperatorManager */
	@Resource(name="acOperatorManager")
	IAcOperatorManager acOperatorManager;
    
    /**
     * <li>说明：分页查询人员列表
     * <li>创建人：程锐
     * <li>创建日期：2012-9-2
     * <li>修改人：何涛
     * <li>修改日期：2016-05-20
     * <li>修改内容：重构，增加使用名字首拼进行过滤查询的功能，删除无用的参数parentOrgId和queryHql
     * @param searchEntity 人员对象包装类
     * @param empname 人员姓名
     * @param orgseq 组织机构序列
     * @return Page 分页查询列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public Page<com.yunda.jx.component.entity.OmEmployee> findPageList(SearchEntity<OmEmployee> searchEntity, String empname, String orgseq)
        throws BusinessException {
        String sql = SqlMapUtil.getSql("om_employee:findPageList");
        // 使用人员名称进行查询
        if (!StringUtil.isNullOrBlank(empname)) {
            sql += " ORDER BY EMPNAME";         // 按人员名称升序排序
            Page<com.yunda.jx.component.entity.OmEmployee> page = new Page<com.yunda.jx.component.entity.OmEmployee>();
            // 系统所有人员信息的缓存集合allEmps
            List<com.yunda.jx.component.entity.OmEmployee> allEmps = this.daoUtils.executeSqlQueryEntity(sql, com.yunda.jx.component.entity.OmEmployee.class);
            if (null == allEmps || allEmps.isEmpty()) {
                return page;
            }
            // 所有匹配查询条件的人员实体对象集合
            List<com.yunda.jx.component.entity.OmEmployee> list = new ArrayList<com.yunda.jx.component.entity.OmEmployee>();
            empname = empname.toLowerCase();
            for (com.yunda.jx.component.entity.OmEmployee emp : allEmps) {
                String name = emp.getEmpname();
                // 匹配人员名称和首拼
                String nameSP = ChineseCharToEn.getInstance().getAllFirstLetter(name, Sensitive.lower);
                if (name.contains(empname) || nameSP.contains(empname)) {
                    list.add(emp);
                }
            }
            // 使用内存数据集合实现分页效果
            Integer start = searchEntity.getStart();
            Integer limit = searchEntity.getLimit();
            int startIndex = start * limit;
            int endIndex = start * limit + limit;
            if (endIndex >= list.size()) {
                endIndex = list.size();
            }
            List<com.yunda.jx.component.entity.OmEmployee> entityList = list.subList(startIndex, endIndex);
            page = new Page<com.yunda.jx.component.entity.OmEmployee>(list.size(), entityList);
            return page;
        }
        // 使用组织机构进行查询
        StringBuilder sb = new StringBuilder(sql);
        sb.append(" WHERE 0 = 0");
        if (!StringUtil.isNullOrBlank(orgseq)) {
            sb.append(" AND ORGSEQ LIKE '").append(orgseq).append("%'");
        }
        sb.append(" ORDER BY EMPNAME");
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
        return super.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false,
            com.yunda.jx.component.entity.OmEmployee.class);
    }
    
    /**
     * <li>说明：查询可添加为库管员的人员信息
     * <li>创建人：王治龙
     * <li>创建日期：2012-9-19
     * <li>修改人：谭诚
     * <li>修改日期：2013-08-02 
     * <li>修改内容：将人员与组织机构表的直接关联改为通过第三方表间接关联
     * @param searchEntity 人员对象包装类
     * @param parentOrgId 单位id
     * @param empname 人员名称
     * @param keeper 库管员
     * @param wareHouseIDX 库IDX
     * @return Page 分页查询列表
     * @throws BusinessException
     */
    public Page<OmEmployee> findPageForKeeperList(SearchEntity<OmEmployee> searchEntity, Long parentOrgId, String empname,
        String keeper, String wareHouseIDX) throws BusinessException {
        String totalHql = "select count(*) from OmEmployee  ";
        String hql = "from OmEmployee ";
        StringBuilder awhere = new StringBuilder();
        awhere.append(" where 1=1 ");
        if (parentOrgId != 0) {
            //awhere.append(" and orgid = ").append(parentOrgId);
        	//谭诚于20130802修改hql语句, 将人员与组织机构表的直接关联改为通过第三方表间接关联
        	awhere.append(" and empid in (").append("select id.empid from OmEmporg where id.orgid = ").append(parentOrgId).append(") ");
        }
        if (!"".equals(empname)) {
            awhere.append(" and empname like '%").append(empname).append("%'");
        }
        if ("1".equals(keeper)) {
            awhere.append(" and empid not in (select empID From StoreKeeper where recordStatus=0 and warehouseIDX='")
                .append(wareHouseIDX).append("')");
        }
        totalHql += awhere.toString();
        hql += awhere.toString();
        return super.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
    }
    
    
    /**
     * <li>说明：获取工位对应车间（班组）下的人员列表
     * <li>创建人：程锐
     * <li>创建日期：2012-12-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param searchEntity 人员对象包装类
     * @param empname 人员姓名
     * @param orgseq 组织机构序列
     * @param workStationIDX 工位主键
     * @return Page 分页查询列表
     * @throws BusinessException
     */
    public Page<OmEmployee> pageListForWorkStation(SearchEntity<OmEmployee> searchEntity, String empname, String orgseq,
        String workStationIDX) throws BusinessException {
        String totalHql = "select count(*) from OmEmployee  ";
        String hql = "from OmEmployee ";
        StringBuilder awhere = new StringBuilder();
        awhere.append(" where 1=1 ");
        if (!StringUtil.isNullOrBlank(orgseq)) {
            awhere.append(" and empid in (select id.empid from OmEmporg ");
            awhere.append(" where id.orgid in (select orgid from OmOrganization where orgseq like '").append(orgseq).append(
                "%' and status='running'))");
        }
        if (!"".equals(empname)) {
            awhere.append(" and empname like '%").append(empname).append("%'");
        }
        if (!StringUtil.isNullOrBlank(workStationIDX)) {
            awhere.append(" and empid not in (select workerID from WorkStationWorker where recordStatus = 0 and workStationIDX ='")
                  .append(workStationIDX).append("')");
        }
        Order[] orders = searchEntity.getOrders();
        if (orders != null) {
            for (Order order : orders) {
                awhere.append(" order by ").append(order);
            }
        }
        totalHql += awhere.toString();
        hql += awhere.toString();
        return super.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
    }
    
    /**
     * <li>方法名称：findDispatcherPageList 1
     * <li>方法说明：查询工长派工人员列表
     * <li>@param cardIdx 作业卡
     * <li>@param orgseq 作业卡所属班组系列
     * <li>@param isDispatcher 是否已派工
     * <li>@param start
     * <li>@param limit
     * <li>@param orders
     * <li>@return
     * <li>@throws BusinessException
     * <li>return: Page
     * <li>创建人：张凡
     * <li>创建时间：2013-1-9 上午10:53:18
     * <li>修改人：程梅
     * <li>修改内容：未派工人员列表加入临时人员
     */
    public Page<OmEmployee> findDispatcherPageList(String cardIdx,String orgseq,boolean isDispatcher,int start,int limit, Order[] orders) throws BusinessException{
        if(cardIdx == null){
            return new Page<OmEmployee>(0,null);
        }
        String q_sql ;
        String fromSql;
        if(isDispatcher && cardIdx != null){
            q_sql = "select e.empid as \"empid\",e.empcode as \"empcode\",e.empname as \"empname\"";//已派工人员查询人员作业状态
            fromSql = " from jxgc_work_card t, jxgc_worker w " +
                        "left join om_employee e on e.empid = w.worker_id and w.worker_tream_idx = e.orgid " +
                        "inner join om_emporg o on o.empid = e.empid " +
                         "    where w.work_card_idx = t.idx " +  
                          "  and t.idx = '" + cardIdx + "'";
        }else{
            q_sql = "select t.empid as \"empid\",t.empcode as \"empcode\",t.empname as \"empname\" " ;
            fromSql = " from (select e.empid ,e.empcode ,e.empname from  om_employee e inner join om_emporg o on o.empid = e.empid inner join om_organization ox on ox.orgid=o.orgid    " + 
                    "     where 1=1 " +
                    (cardIdx != null && !"".equals(cardIdx) ?"and e.empid not in (select w.worker_id from jxgc_worker w where w.work_card_idx = '" + cardIdx + "')" : "") + 
                    " and ox.orgseq like '" + orgseq + "%'  " +
                    " union" +
                    " select empid ,empcode ,empname from jxpz_temporary_emp where TEMPORARY_TREAM_SEQ like '" + orgseq + "%' and RECORD_STATUS=0 " +
                    (cardIdx != null && !"".equals(cardIdx) ?"and empid not in (select w.worker_id from jxgc_worker w where w.work_card_idx = '" + cardIdx + "')" : "") +
                    ") t order by t.empname" ;
        }        
        String totalSql = "select count(1) " + fromSql;
        String sql =q_sql+ fromSql;
        limit = 1000;
        return super.findPageList(totalSql, sql, start , limit, null, orders);
    } 
    
    
    /**
     * <li>说明：列检车辆派工
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param cardIdx
     * @param orgseq
     * @param isDispatcher
     * @param start
     * @param limit
     * @param orders
     * @return
     * @throws BusinessException
     */
    public Page<OmEmployee> findListForZbPlanRecordEmpSelect(String cardIdx,String orgseq,boolean isDispatcher,int start,int limit, Order[] orders) throws BusinessException{
        if(cardIdx == null){
            return new Page<OmEmployee>(0,null);
        }
        String q_sql ;
        String fromSql;
        if(isDispatcher && cardIdx != null){
            q_sql = "select e.empid as \"empid\",e.empcode as \"empcode\",e.empname as \"empname\"";//已派工人员查询人员作业状态
            fromSql = " from ZB_ZBGL_PLAN_WORKER w " +
                        "left join om_employee e on e.empid = w.WORK_PERSON_IDX " +
                        "inner join om_emporg o on o.empid = e.empid " +
                         "    where w.RDP_Record_IDX = " + "'" + cardIdx + "'";
        }else{
            q_sql = "select t.empid as \"empid\",t.empcode as \"empcode\",t.empname as \"empname\" " ;
            fromSql = " from (select e.empid ,e.empcode ,e.empname from  om_employee e inner join om_emporg o on o.empid = e.empid inner join om_organization ox on ox.orgid=o.orgid    " + 
                    "     where 1=1 " +
                    (cardIdx != null && !"".equals(cardIdx) ?"and e.empid not in (select w.WORK_PERSON_IDX from ZB_ZBGL_PLAN_WORKER w where w.RDP_Record_IDX = '" + cardIdx + "')" : "") + 
                    " and ox.orgseq like '" + orgseq + "%'  " +
                    " union" +
                    " select empid ,empcode ,empname from jxpz_temporary_emp where TEMPORARY_TREAM_SEQ like '" + orgseq + "%' and RECORD_STATUS=0 " +
                    (cardIdx != null && !"".equals(cardIdx) ?"and empid not in (select w.WORK_PERSON_IDX from ZB_ZBGL_PLAN_WORKER w where w.RDP_Record_IDX = '" + cardIdx + "')" : "") +
                    ") t order by t.empname" ;
        }        
        String totalSql = "select count(1) " + fromSql;
        String sql =q_sql+ fromSql;
        limit = 1000;
        return super.findPageList(totalSql, sql, start , limit, null, orders);
    } 
    
    
    /**
     * <li>说明：查询提票工长派工列表
     * <li>创建人：程锐
     * <li>创建日期：2013-3-13
     * <li>修改人： 程梅
     * <li>修改日期：2015-4-23
     * <li>修改内容：未派工人员列表加入临时人员
     * @param faultIdx 提票处理主键
     * @param orgid 班组主键
     * @param isDispatcher 是否派工
     * @param start 开始行
     * @param limit 每页记录数
     * @param orders 排序对象
     * @return Page 分页对象
     * @throws BusinessException
     */
    public Page<OmEmployee> findTpDataList(String faultIdx,String orgid, boolean isDispatcher,int start,int limit, Order[] orders) throws BusinessException{
        if (faultIdx == null) {
            return new Page<OmEmployee>(0, null);
        }
        String hql = "from FaultTicket where idx = ? and recordStatus = 0";
        FaultTicket tp = (FaultTicket) daoUtils.findSingle(hql, new Object[] {faultIdx});
        String repairEmpID = tp != null && !StringUtil.isNullOrBlank(tp.getRepairEmpID()) ? tp.getRepairEmpID() : "";
            
        String q_sql ;
        StringBuilder fromSql = new StringBuilder();
        if(isDispatcher && faultIdx != null){
            q_sql = "select t.empid as \"empid\",t.empcode as \"empcode\",t.empname as \"empname\" ";
            fromSql.append("from (select e.empid ,e.empcode ,e.empname from  om_employee e ");
            if (!StringUtil.isNullOrBlank(repairEmpID)) {
                fromSql.append("where e.empid in (")
                       .append(repairEmpID)
                       .append(")");
            } else {
                fromSql.append("where e.empid is null ");
            }
            fromSql.append(") t order by t.empname");
                      
        } else {
            q_sql = "select t.empid as \"empid\",t.empcode as \"empcode\",t.empname as \"empname\" ";
            fromSql.append("from (select e.empid ,e.empcode ,e.empname from  om_employee e ")
                   .append("inner join om_emporg oe on oe.empid = e.empid where oe.orgid = ")
                   .append(orgid);
            if (!StringUtil.isNullOrBlank(repairEmpID)) {
                fromSql.append(" and e.empid not in (")
                       .append(repairEmpID)
                       .append(")");
            }
            fromSql.append(" union select empid ,empcode ,empname from jxpz_temporary_emp where TEMPORARY_TREAM_ID = ")
                   .append(orgid)
                   .append(" and RECORD_STATUS=0 ");
            if (!StringUtil.isNullOrBlank(repairEmpID)) {
                fromSql.append(" and empid not in (")
                       .append(repairEmpID)
                       .append(")");
            }
            fromSql.append(") t order by t.empname");            
        }        
        String totalSql = "select count(1) " + fromSql;
        String sql = q_sql + fromSql;
        return super.findPageList(totalSql, sql, start , limit, null, orders);
    }
    /**
     * <li>说明：通过操作员ID查询对应的人员信息
     * <li>创建人：王治龙
     * <li>创建日期：2013-5-8
     * <li>修改人： 刘晓斌
     * <li>修改日期：2013-12-1
     * <li>修改内容：使用人员查询接口
     * @param operatorid：操作员ID主键
     * @return OmEmployee 人员对象
     */
	public OmEmployee getByOperatorid(Long operatorid){
    	return omEmployeeManager.findByOperator(operatorid);
    }
    
    /**
     * <li>方法名称：saveCardNo
     * <li>方法说明：保存人员卡号 
     * <li>@param userId
     * <li>@param cardNo
     * <li>@return
     * <li>return: int
     * <li>创建人：张凡
     * <li>创建时间：2013-5-20 下午08:23:52
     * <li>修改人：
     * <li>修改内容：
     */
    public int saveCardNo(String userId, String cardNo){
        String hql = "update OmEmployee set cardNum = '" + cardNo + "' where userid='" + userId + "'";
        
        return daoUtils.executUpdateOrDelete(hql);
    }
    
    /**
     * <li>方法名称：getByField
     * <li>方法说明：自定义字段查询 
     * <li>@param field
     * <li>@param value
     * <li>@return
     * <li>return: List<OmEmployee>
     * <li>创建人：张凡
     * <li>创建时间：2013-5-20 下午08:26:57
     * <li>修改人：
     * <li>修改内容：
     */
    public List<OmEmployee> getByField(String field, String value){
    	return omEmployeeManager.findByField(field, value);
    }
    /**
     * <li>方法名称：loginByCardNo
     * <li>方法说明：卡号登陆 
     * <li>@param cardNo
     * <li>@return
     * <li>return: AcOperator
     * <li>创建人：张凡
     * <li>创建时间：2013-5-24 下午09:15:38
     * <li>修改人：
     * <li>修改内容：
     */
    public AcOperator loginByCardNo(String cardNo){
    	List <AcOperator> list = acOperatorManager.findByCardNo(cardNo);
    	if(list!=null&&list.size()>0)
    		return list.get(0);
    	return null;
    }

    /**
     * <li>方法说明：根据操作员ID查询EMP基本（部份）信息
     * <li>方法名称：findEmpByOperator
     * <li>@param operatorid
     * <li>@return
     * <li>return: OmEmployee
     * <li>创建人：张凡
     * <li>创建时间：2013-5-31 下午04:46:44
     * <li>修改人：
     * <li>修改内容：
     */
    public OmEmployee findEmpByOperator(Long operatorid){
    	return omEmployeeManager.findByOperator(operatorid);
    }
    /**
     * <li>说明：根据orgid查找返回所有人员记录
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-6-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    @SuppressWarnings("unchecked")
	public List<OmEmployee> findByOrgid(long orgid){
    	OmEmployee emp = new OmEmployee();
    	emp.setOrgid(orgid);
    	List<OmEmployee> empList = daoUtils.getHibernateTemplate().findByExample(emp);
    	return empList;
    }

	/**
	 * <li>说明：获取该组织机构（orgid）及子机构下所有人员
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-6-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public Set<OmEmployee> findByOrgidAndSuborg(long orgid){
		OmOrganization org = (OmOrganization)daoUtils.get(orgid, OmOrganization.class);
		List<OmOrganization> orgList = omOrganizationManager.findAllChilds(org.getOrgid(), true);
		if(orgList == null || orgList.size() < 1)	return null;
		Set<Long> orgidSet = new HashSet<Long>();
		for (OmOrganization orgn : orgList) {
			orgidSet.add(orgn.getOrgid());
		}
		if(orgidSet == null || orgidSet.size() < 1)	return null;
//		获取组织机构及子机构下所有人员
		Set<OmEmployee> empSet = new HashSet<OmEmployee>();
		QueryCriteria<OmEmployee> query = new QueryCriteria<OmEmployee>(OmEmployee.class);
		Condition c = new Condition( "orgid", Condition.IN, orgidSet.toArray(new Long[ orgidSet.size() ]) );
		query.addCondition(c);
		List<OmEmployee> empList = findList(query);
		if(empList == null || empList.size() < 1)	return null;
		
		empSet.addAll(empList);
		return empSet;
	}
	
	/**
     * <li>方法名称：findPageDataList
     * <li>方法说明：查询工长派工人员列表
     * <li>@param cardIdx 作业卡
     * <li>@param orgseq 作业卡所属班组系列
     * <li>@param isDispatcher 是否已派工
     * <li>@param start
     * <li>@param limit
     * <li>@param orders
     * <li>@return
     * <li>@throws BusinessException
     * <li>return: Page
     * <li>创建人：张凡
     * <li>创建时间：2013-1-9 上午10:53:18
     * <li>修改人：
     * <li>修改内容：
     */
    public Page<OmEmployee> findPageDataList(String cardIdx,String orgseq,boolean isDispatcher,int start,int limit, Order[] orders) throws BusinessException{
        if(cardIdx == null){
            return new Page<OmEmployee>(0,null);
        }
        String q_sql = "select e.empid as \"empId\",e.empcode as \"empcode\",e.empname as \"empname\" ";
            
        String fromSql;
        if(isDispatcher && cardIdx != null){
            q_sql += ",w.status as \"status\" ";//已派工人员查询人员作业状态
            fromSql = " from jxgc_work_card t, jxgc_worker w " +
                        "left join om_employee e on e.empid = w.worker_id and w.worker_tream_idx = e.orgid " +
                        "inner join om_emporg o on o.empid = e.empid " +
                         "    where w.work_card_idx = t.idx " +  
                          "  and t.idx = '" + cardIdx + "'";
        }else{
            fromSql = "from  om_employee e inner join om_emporg o on o.empid = e.empid inner join om_organization ox on ox.orgid=o.orgid    " + 
                    "     where 1=1 " +
                    (cardIdx != null && !"".equals(cardIdx) ?"and e.empid not in (select w.worker_id from jxgc_worker w where w.work_card_idx = '" + cardIdx + "')" : "") + 
                    " and ox.orgseq like '" + orgseq + "%'  order by e.empname";
        }        
        String totalSql = "select count(1) " + fromSql;
        String sql =q_sql+ fromSql;
        return super.findPageList(totalSql, sql, start , limit, null, orders);
    } 
    /**
     * 
     * <li>说明：查询技术改造工长派工列表【质量】;工位终端方法
     * <li>创建人：王治龙
     * <li>创建日期：2013-9-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param techReformCaseIdx：技术改造工单主键；orgseq：单位序列；isDispatcher：是否派工标识
     * @return Page 返回分页列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public Page findJsgzPersonList(String techReformCaseIdx,String orgseq,boolean isDispatcher,int start,int limit, Order[] orders) throws BusinessException{
    	if(techReformCaseIdx == null){
    		return new Page<OmEmployee>(0,null);
    	}
    	String q_sql = "select e.empid as \"empid\",e.empcode as \"empcode\",e.empname as \"empname\" ";
    	
    	String fromSql;
    	if(isDispatcher && techReformCaseIdx != null){
    		fromSql = "from jczl_train_tech_reform_account t, jxgc_worker w " +
    		"left join om_employee e on e.empid = w.worker_id and w.worker_tream_idx = e.orgid " +
    		"inner join om_emporg o on o.empid = e.empid " +
    		"    where w.work_card_idx = t.idx " +  
    		"  and t.idx = '" + techReformCaseIdx + "'";
    	}else{
    		fromSql = "from  om_employee e inner join om_emporg o on o.empid = e.empid inner join om_organization ox on ox.orgid=o.orgid " + 
    		"     where 1=1 " +
    		(techReformCaseIdx != null && !"".equals(techReformCaseIdx) ?"and e.empid not in (select w.worker_id from jxgc_worker w where w.work_card_idx = '" + techReformCaseIdx + "' and w.worker_id is not null)" : "") + 
    		" and ox.orgseq like '" + orgseq + "%'";
    	}        
    	String totalSql = "select count(1) " + fromSql;
    	String sql =q_sql+ fromSql;
    	return super.findPageList(totalSql, sql, start , limit, null, orders);
    } 
    /**
     * 
     * <li>说明：查询普查整治工长派工列表【质量】
     * <li>创建人：程梅
     * <li>创建日期：2013-8-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    @SuppressWarnings("unchecked")
    public Page findPczzPersonList(String planCaseIDX,String orgseq,boolean isDispatcher,int start,int limit, Order[] orders) throws BusinessException{
        if(planCaseIDX == null){
            return new Page<OmEmployee>(0,null);
        }
        String q_sql = "select e.empid as \"empid\",e.empcode as \"empcode\",e.empname as \"empname\" ";
            
        String fromSql;
        if(isDispatcher && planCaseIDX != null){
            fromSql = "from JCZL_Inspect_Plan_Case t, JCZL_Inspect_Plan_Case_Worker w " +
                        "left join om_employee e on e.empid = w.Worker_ID " +
                         "    where w.Plan_Case_IDX = t.idx " +  
                          "  and t.idx = '" + planCaseIDX + "'";
        }else{
            fromSql = "from  om_employee e inner join om_emporg o on o.empid = e.empid inner join om_organization ox on ox.orgid=o.orgid " + 
                    "     where 1=1 " +
                    (planCaseIDX != null && !"".equals(planCaseIDX) ?"and e.empid not in (select w.Worker_ID from JCZL_Inspect_Plan_Case_Worker w where w.Plan_Case_IDX = '" + planCaseIDX + "' and w.Worker_ID is not null)" : "") + 
                    " and ox.orgseq like '" + orgseq + "%'";
        }        
        String totalSql = "select count(1) " + fromSql;
        String sql =q_sql+ fromSql;
        return super.findPageList(totalSql, sql, start , limit, null, orders);
    } 
    /**
     * <li>方法说明：查询派工人员的名称 
     * <li>方法名称：findEmpName
     * <li>@param empids
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-12-18 下午03:58:56
     * <li>修改人：
     * <li>修改内容：
     *  
     */
    public String findEmpName(String empids) {
        String sql = "select to_char(wm_concat(empname)) from om_employee where empid in(" + empids + ")";
        String empname = daoUtils.executeSqlQuery(sql).iterator().next().toString();//查询作业人员名称
        return empname;
    }
    /**
     * 
     * <li>说明：根据机构id查询该班组下的人员，包括临时人员【工位终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-5-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param orgId  班组id
     * @return 人员列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<OmEmployee> findTeamEmpList(Long orgId) throws BusinessException {
        StringBuffer buffList = new StringBuffer();
        buffList.append("select t.empid as \"empid\",t.empcode as \"empcode\",t.empname as \"empname\",t.gender as \"gender\" ");
        StringBuffer buff = new StringBuffer();
        buff.append(" from (select e.empid ,e.empcode ,e.empname,e.gender from  om_employee e inner join om_emporg o on o.empid = e.empid inner join om_organization ox on ox.orgid=o.orgid    ");
        buff.append(" where 1=1 " );
        if (null != orgId) {
            buff.append(" and ox.orgid = ").append(orgId).append(" and ox.status = 'running'");
        }
        buff.append(" union " );
        buff.append("select empid ,empcode ,empname,gender from jxpz_temporary_emp where 1=1 ");
        if (null != orgId) {
            buff.append(" and TEMPORARY_TREAM_ID = ").append(orgId).append(" and RECORD_STATUS=0 ");
        }
        buff.append(" ) t order by t.empname ASC" );
        buffList.append(buff);
        List<OmEmployee> list = (List<OmEmployee>)daoUtils.executeSqlQuery(buffList.toString());
        return list;
    }
    /**
     * 
     * <li>说明：根据部门序列查询人员列表
     * <li>创建人：程梅
     * <li>创建日期：2015-10-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询条件
     * @param orgseq 部门序列
     * @return Page<OmEmployee> 人员列表分页对象
     * @throws BusinessException
     */
    public Page<OmEmployee> findEmpListByOrgId(SearchEntity<OmEmployee> searchEntity, String orgseq)
            throws BusinessException {
        //  谭诚修改: 解决人员选择控件与"基础管理-机构人员管理"中部分部门人员不一致的问题
            StringBuffer buffTotal = new StringBuffer();
            buffTotal.append("select count(*) ");
            StringBuffer buffList = new StringBuffer();
            buffList.append("select t.empid as \"empid\",t.empcode as \"empcode\",t.empname as \"empname\",t.gender as \"gender\",t.orgid as \"orgid\",t.orgname as \"orgname\",t.orgseq as \"orgseq\"   ");
            StringBuffer buff = new StringBuffer();
            buff.append(" from (select e.empid ,e.empcode ,e.empname,e.gender,ox.orgid,ox.orgname,ox.orgseq from  om_employee e inner join om_emporg o on o.empid = e.empid inner join om_organization ox on ox.orgid=o.orgid    ");
            buff.append(" where 1=1 " );
            if (!StringUtil.isNullOrBlank(orgseq)) {
                buff.append(" and ox.orgseq like '").append(orgseq).append("%' and ox.status = 'running'");
            }
            buff.append(" ) t " );
            Order[] orders = searchEntity.getOrders();
            if(orders != null && orders.length > 0){            
                buff.append(HqlUtil.getOrderHql(orders));
            }else{
                buff.append(" order by t.empname ASC");
            }       
            buffTotal.append(buff);
            buffList.append(buff);
            return super.findPageList(buffTotal.toString(), buffList.toString(), searchEntity.getStart(), searchEntity.getLimit(), null, orders);
        }
    /**
     * 
     * <li>说明：查询配件周转常用部门下所有人员列表
     * <li>创建人：程梅
     * <li>创建日期：2015-11-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return Page<OmEmployee> 人员列表分页对象
     * @throws BusinessException
     */
    public Page<OmEmployee> findEmpListByAccountOrgId(SearchEntity<OmEmployee> searchEntity)
            throws BusinessException {
            StringBuffer buffTotal = new StringBuffer();
            OmEmployee emp = searchEntity.getEntity() ;
            buffTotal.append("select count(*) ");
            StringBuffer buffList = new StringBuffer();
            buffList.append("select t.empid as \"empid\",t.empcode as \"empcode\",t.empname as \"empname\",t.gender as \"gender\",t.orgid as \"orgid\",t.orgname as \"orgname\" ,t.orgseq as \"orgseq\" ");
            StringBuffer buff = new StringBuffer();
            buff.append(" from (select e.empid ,e.empcode ,e.empname,e.gender,ox.orgid,ox.orgname,ox.orgseq from  om_employee e inner join om_emporg o on o.empid = e.empid inner join om_organization ox on ox.orgid=o.orgid    ");
            buff.append(" where 1=1 and ox.orgid in (select i.ORG_ID from JXPZ_ORG_DIC_ITEM i where i.DICT_TYPE_ID='accountorg') and ox.status = 'running' ) t " );
            if(!StringUtil.isNullOrBlank(emp.getEmpname())){
                buff.append(" where t.empname like '%").append(emp.getEmpname()).append("%' or t.orgname like '%").append(emp.getEmpname()).append("%' ");
            }
            Order[] orders = searchEntity.getOrders();
            if(orders != null && orders.length > 0){            
                buff.append(HqlUtil.getOrderHql(orders));
            }else{
                buff.append(" order by t.empname ASC");
            }       
            buffTotal.append(buff);
            buffList.append(buff);
            return super.findPageList(buffTotal.toString(), buffList.toString(), searchEntity.getStart(), searchEntity.getLimit(), null, orders);
        }
}
