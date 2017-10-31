package com.yunda.zb.trainonsand.manager;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.jxpz.utils.SystemConfigUtil;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountManager;
import com.yunda.zb.common.ZbConstants;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpNode;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpManager;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpNodeManager;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWi;
import com.yunda.zb.trainonsand.entity.ZbglSanding;
import com.yunda.zb.trainonsand.entity.ZbglSandingBean;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglSanding业务类,机车上砂记录
 * <li>创建人：王利成
 * <li>创建日期：2015-01-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="zbglSandingManager")
public class ZbglSandingManager extends JXBaseManager<ZbglSanding, ZbglSanding> {
    
    /**机车出入库台账业务类**/
    @Resource
    private TrainAccessAccountManager  trainAccessAccountManager;
    
    /**员工服务业务类**/
    @Resource
    private OmEmployeeManager omEmployeeManager;
    
    /**流程节点业务类**/
    @Resource    
    private ZbglRdpNodeManager zbglRdpNodeManager;
    
    /**流程节点业务类**/
    @Resource    
    private ZbglRdpManager zbglRdpManager;
    
    private String configValue = ZbConstants.NODENAME_SANDING;
    
    /**
     * <li>说明：上砂查询(已完成上砂列表)
     * <li>创建人：王利成
     * <li>创建日期：2015-2-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 机车整备单bean实体包装类
     * @return 整备单分页列表-临修
     * @throws BusinessException
     */
    public Page<ZbglSanding> findSandingList(SearchEntity<ZbglSanding> searchEntity){
        StringBuilder sbSelect =
            new StringBuilder(
                "select new ZbglSanding(a.idx, a.trainAccessAccountIDX, b.trainTypeIDX, b.trainTypeShortName, b.trainNo, a.startTime, a.endTime, a.sandingTime, a.standardSandingTime, a.dutyPersonId, a.dutyPersonName, a.isOverTime, b.inTime,b.dID,b.dName,b.siteID,b.siteName, a.sandNum)");
        StringBuilder sbFrom = new StringBuilder();
          sbFrom.append(" from ZbglSanding a ,TrainAccessAccount b where a.trainAccessAccountIDX = b.idx ")
          .append(" and a.endTime is not null ");
        ZbglSanding entity = searchEntity.getEntity();
        // 查询条件 - 车型
        if (!StringUtil.isNullOrBlank(entity.getTrainTypeIDX())) {
            sbFrom.append(" AND b.trainTypeIDX = '").append(entity.getTrainTypeIDX()).append("'");
        }
        // 查询条件 - 车号
        if (!StringUtil.isNullOrBlank(entity.getTrainNo())) {
            sbFrom.append(" AND b.trainNo LIKE '%").append(entity.getTrainNo()).append(Constants.LIKE_PIPEI);
        }
        // 查询条件 - 配属段
        if (!StringUtil.isNullOrBlank(entity.getDName())) {
            sbFrom.append(" AND b.dName LIKE '%").append(entity.getDName()).append(Constants.LIKE_PIPEI);
        }
        //查询条件 - 站场
        if (!StringUtil.isNullOrBlank(entity.getSiteName())) {
            sbFrom.append(" AND b.siteName LIKE '%").append(entity.getSiteName()).append(Constants.LIKE_PIPEI);
        }
        //查询条件 - 入段时间(开始)
        if (!StringUtil.isNullOrBlank(entity.getStartDate())) {
            sbFrom.append(" and  to_char(b.inTime,'yyyy-mm-dd')>='").append(entity.getStartDate()).append("'");
        }
        //查询条件 - 入段时间（结束）
        if (!StringUtil.isNullOrBlank(entity.getOverDate())){
            sbFrom.append(" and  to_char(b.inTime,'yyyy-mm-dd')<='").append(entity.getOverDate()).append("'");
        }
        //查询条件 - 是否超时
        if (null != entity.getIsOverTime() && 0 == entity.getIsOverTime().intValue()) {
            sbFrom.append(" AND a.isOverTime IN(").append(entity.getIsOverTime()).append(Constants.BRACKET_R);
        }
        sbFrom.append(" ORDER BY b.inTime DESC");//排序hql字符串
        String hql = sbSelect.append(sbFrom).toString();
        String totalHql = "select count(*) " + sbFrom; 
        return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
    }
    
    /**
     * <li>说明：工位终端上砂查询方法
     * <li>创建人：王利成
     * <li>创建日期：2015-2-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param sandingBean 查询bean参数
     * @param start 开始行
     * @param limit 每页记录数
     * @return Page
     */
    @SuppressWarnings("unchecked")
    public Page querySandingList(ZbglSandingBean sandingBean,int start, int limit){
        String sql = "";
        OmEmployee emp = null;
        Page<ZbglSandingBean> page = null;
        if(!StringUtil.isNullOrBlank(String.valueOf(sandingBean.getOperatorid()))){
           emp = omEmployeeManager.findByOperator(sandingBean.getOperatorid());
        }
        if(!StringUtil.isNullOrBlank(sandingBean.getIsFinnish())){
            sql = getSql(sandingBean.getIsFinnish());  
        }
        if(!StringUtil.isNullOrBlank(sql)){
            StringBuilder sb = new StringBuilder(sql);
            if(emp != null){
              if(!StringUtil.isNullOrBlank(String.valueOf(emp.getEmpid()))){
                sb.append(" and ( xy.duty_personid is null or xy.duty_personid = " ).append(emp.getEmpid()).append(")");
               }
            }
            if(!StringUtil.isNullOrBlank(sandingBean.getTrainTypeIDX())){
                sb.append(" and xy.train_type_idx = '").append(sandingBean.getTrainTypeIDX()).append("'");
            }
            if(!StringUtil.isNullOrBlank(sandingBean.getTrainNo())){
                sb.append(" and xy.train_no = '").append(sandingBean.getTrainNo()).append("'");
            }
            sb.append(" and xy.siteID = '" ).append(EntityUtil.findSysSiteId("")).append("'");
            sb.append(" ORDER BY xy.in_time ASC");
            sql = sb.toString();
            StringBuilder totalSql = new StringBuilder("SELECT COUNT(*) AS ROWCOUNT FROM (").append(sql).append(")");
            page = this.acquirePageList(totalSql.toString(), sql, start, limit, false);
        }
        return page;
    }
       
    
    /**
     * 
     * <li>说明：获取查询sql字符串
     * <li>创建人：王利成
     * <li>创建日期：2015-2-06
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param isFinnish 查询类型（TODO未完成--COMPLETE已完成） 
     * @return 查询对应的sql字符串
     */
    public String getSql(String isFinnish){
        String sql = "";
        if (ZbglRdpWi.STATUS_TODO.equals(isFinnish)) {
            sql = SqlMapUtil.getSql("zb-sanding:querySandingPageList");             
        }else if (ZbglRdpWi.STATUS_HANDLED.equals(isFinnish)) {
            sql = SqlMapUtil.getSql("zb-sanding:querySandedPageList");                       
        }
        return sql;
    }
    
    /**
     * 
     * <li>说明：处理开始上砂任务
     * <li>创建人：王利成
     * <li>创建日期：2015-2-06
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainAccessIdx 机车出入库主键
     * @param operatorid 操作员ID
     * @throws Exception 
     */
    public String startSanding(String trainAccessIdx,Long operatorid) throws Exception{
        if(trainAccessIdx == null || operatorid == null){
            return null;
        }
        //获取入库台账对象
        TrainAccessAccount ta = trainAccessAccountManager.getModelById(trainAccessIdx);
        //获取员工服务对象
        OmEmployee emp = omEmployeeManager.findByOperator(operatorid);
        ZbglSanding newSanding = new ZbglSanding();
        //获取标准上砂时间
        String sandConfig = SystemConfigUtil.getValue("ck.jczb.trainonsand.standardSandingTime");
        if(!StringUtil.isNullOrBlank(sandConfig)){
            int standardSandingTime = Integer.parseInt(sandConfig);
            newSanding.setStandardSandingTime(standardSandingTime);
        } else
            newSanding.setStandardSandingTime(0);
        if(ta != null){ 
            newSanding.setDID(ta.getDID());
            newSanding.setDName(ta.getDName());
            newSanding.setSiteId(ta.getSiteID());
            newSanding.setSiteName(ta.getSiteName());
        }
        if(emp != null){
            newSanding.setDutyPersonId(emp.getEmpid());
            newSanding.setDutyPersonName(emp.getEmpname());
        }
        newSanding.setTrainAccessAccountIDX(trainAccessIdx);
        newSanding.setStartTime(new Date());
        saveOrUpdate(newSanding);
        ZbglRdp rdp = zbglRdpManager.getRdpByAccount(trainAccessIdx);
        if (rdp != null)
            zbglRdpNodeManager.updateNodeForStart(configValue, new Date(), rdp.getIdx(), ZbglRdpNode.STATUS_GOING);
        
        return newSanding.getIdx();
    }
    
    /**
     * 
     * <li>说明：结束上砂任务
     * <li>创建人：王利成
     * <li>创建日期：2015-2-06
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param sandingIdx 上砂记录主键
     * @param sandNum 上砂量
     * @throws Exception 
     */
    public void endSanding(String sandingIdx, String sandNum) throws Exception{
        //获取单个上砂实体
        ZbglSanding oldSanding = this.getModelById(sandingIdx);
        //计算上砂时间
        int sandingTime = getSecondsBetween(oldSanding.getStartTime(),new Date());
        //比较时间
        oldSanding.setIsOverTime(sandingTime < oldSanding.getStandardSandingTime() ? 0: 1);
        oldSanding.setEndTime(new Date());
        oldSanding.setSandingTime(sandingTime);
        oldSanding.setSandNum(StringUtil.nvlTrim(sandNum, ""));
        saveOrUpdate(oldSanding);
        ZbglRdp rdp = zbglRdpManager.getRdpByAccount(oldSanding.getTrainAccessAccountIDX());
        if (rdp != null)
            zbglRdpNodeManager.updateNodeForEnd(configValue, new Date(), rdp.getIdx(), ZbglRdpNode.STATUS_COMPLETE);
    }
    


    /**
     * 
     * <li>说明：计算两个时间相差的秒数
     * <li>创建人：王利成
     * <li>创建日期：2015-2-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param bDate 开始时间
     * @param eDate 结束时间
     * @return 秒数
     */
    private int getSecondsBetween(Date bDate, Date eDate){
        long startTime = bDate.getTime();
        long endTime = eDate.getTime();
        return Integer.parseInt(String.valueOf((endTime - startTime)/1000));
    }
    
    /**
     * <li>说明：查询上砂分页列表
     * <li>创建人：王利成
     * <li>创建日期：2015-2-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param totalSql 查询总记录数的sql语句
     * @param sql 查询语句
     * @param start 开始行
     * @param limit 每页记录数
     * @param isQueryCacheEnabled 是否启用查询缓存
     * @return Page<ZbglSandingBean> 分页查询列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public Page<ZbglSandingBean> acquirePageList(String totalSql, String sql, int start, int limit, Boolean isQueryCacheEnabled) throws BusinessException{
        final int beginIdx = start < 0 ? 0 : start;
        final int pageSize = limit < 0 ? Page.PAGE_SIZE : limit;
        final String total_sql = totalSql;
        final String fSql = sql;
        final Boolean useCached = isQueryCacheEnabled;
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (Page<ZbglSandingBean>) template.execute(new HibernateCallback() {
            public Page<ZbglSandingBean> doInHibernate(Session s) {
                SQLQuery query = null;
                try {
                    query = s.createSQLQuery(total_sql);
                    query.addScalar("rowcount", Hibernate.INTEGER);
                    query.setCacheable(useCached); // 缓存开关
                    int total = ((Integer) query.uniqueResult()).intValue();
                    query.setCacheable(false);
                    int begin = beginIdx > total ? total : beginIdx;
                    query = (SQLQuery) s.createSQLQuery(fSql).addEntity(ZbglSandingBean.class).setFirstResult(begin).setMaxResults(pageSize);
                    query.setCacheable(useCached); //缓存开关
                    return new Page<ZbglSandingBean>(total, query.list());
                } catch (HibernateException e) {
                    throw e;
                } finally {
                    if (query != null)
                        query.setCacheable(false);
                }
            }
        });
    
    }
    
    /**
     * <li>说明：根据机车入段台账获取上砂完成情况
     * <li>创建人：程锐
     * <li>创建日期：2015-3-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainAccessAccountIDX 机车入段台账IDX
     * @return 上砂完成情况
     */
    public String getIsFinish(String trainAccessAccountIDX) {
        ZbglSanding sanding = getEntityByAccountIDX(trainAccessAccountIDX);
        if (sanding == null)
            return "无";
        if (sanding.getEndTime() != null)
            return "完成";
        else
            return "未完成";
    }
    
    /**
     * <li>说明：根据机车入段台账获取上砂实体对象
     * <li>创建人：程锐
     * <li>创建日期：2015-3-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainAccessAccountIDX 机车入段台账IDX
     * @return 上砂实体对象
     */
    @SuppressWarnings("unchecked")
    public ZbglSanding getEntityByAccountIDX(String trainAccessAccountIDX) {
        ZbglSanding entity = new ZbglSanding();
        entity.setTrainAccessAccountIDX(trainAccessAccountIDX);
        List<ZbglSanding> list = daoUtils.getHibernateTemplate().findByExample(entity);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }
}
