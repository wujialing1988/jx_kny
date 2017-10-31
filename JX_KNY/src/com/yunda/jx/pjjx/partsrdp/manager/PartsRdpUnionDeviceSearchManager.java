package com.yunda.jx.pjjx.partsrdp.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdpUnionDevice;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdpUnionDeviceSearch;
import com.yunda.jx.util.MixedUtils;

/**
 * <li>说明：配件兑现单关联上传数据设备查询业务类
 * <li>创建人： 张凡
 * <li>创建日期：2015年11月21日
 * <li>成都运达科技股份有限公司
 */
@Service
public class PartsRdpUnionDeviceSearchManager extends JXBaseManager<PartsRdpUnionDevice, PartsRdpUnionDevice> implements IbaseCombo  {
    
    /**
     * 字尾%'标识
     */
    private static final String SUFFIX_TOKEN = "%'";
    /**
     * 右括号
     */
    private static final String RIGHT_BRACKET = ")";
    /**
     * 统计条件SQL前部分
     */
    private static final String SELECT_COUNT_1_FROM = "select count(1) from (";

    /**
     * <li>方法说明： 查询配件兑现单关联上传数据设备列表
     * <li>方法名：findPageList
     * @param start start
     * @param limit limit
     * @param orders orders
     * @param filter 过滤对象
     * @return
     * <li>创建人： 张凡
     * <li>创建日期：2015年11月21日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public Page<PartsRdpUnionDevice> findPageList(int start, int limit, Order[] orders, PartsRdpUnionDeviceSearch filter){       
        
        StringBuilder sql = new StringBuilder("select b.parts_name, b.parts_no, b.specification_model , b.unload_traintype,");
        sql.append("b.unload_trainno, b.unload_repair_class, b.unload_repair_time, de.*");
        sql.append(" from pjjx_parts_rdp b,(select distinct rdp_idx, i.device_info_code, i.device_info_name");
        sql.append(" from pjjx_parts_rdp_equip_card c, jxpz_device_info i where i.device_info_code = c.device_info_code");
        if(filter.getWorkOrderDesc() != null){
            sql.append(" and c.equip_card_desc like '%").append(filter.getWorkOrderDesc()).append(SUFFIX_TOKEN);
        }
        //检测项表过滤
        String clip = " and exists (select 1 from pjjx_parts_rdp_equip_di di where di.rdp_equip_card_idx = c.idx";
        if(filter.getDetectItemName() != null) {
            sql.append(clip);
            sql.append(" and di.data_item_name like '%").append(filter.getDetectItemName()).append(SUFFIX_TOKEN);
            clip = null;
        }
        if(filter.getDetectItemValue() != null){
            if(clip != null)
                sql.append(clip);
            sql.append(" and di.item_value like '%").append(filter.getDetectItemValue()).append(SUFFIX_TOKEN);
            clip = null;
        }
        
        if(clip == null) {  //clip等于空表示做了exists sql追加
            sql.append(RIGHT_BRACKET);
        }
        if(filter.getDeviceInfoCode() != null){
            sql.append(" and i.device_info_code='").append(filter.getDeviceInfoCode()).append("'");
        }
        sql.append(" and c.rdp_idx is not null) de");
        sql.append(" where de.rdp_idx = b.idx and b.plan_starttime > to_date('");
        sql.append(filter.getStartTime()).append("','yyyy-MM-dd HH24:mi:ss')");
        sql.append(" and b.plan_starttime < to_date('").append(filter.getEndTime()).append("','yyyy-MM-dd HH24:mi:ss')");
        
        if(filter.getPartsNo() != null)
            sql.append(" and b.parts_no='").append(filter.getPartsNo()).append("'");
        
        if(filter.getSpecificationModel() != null)
            sql.append(" and b.specification_model like '%").append(filter.getSpecificationModel()).append(SUFFIX_TOKEN);
        if(filter.getTrainNo() != null)
            sql.append(" and b.unload_trainno = '").append(filter.getTrainNo()).append("'");
        if(filter.getTrainType() != null)
            sql.append(" and b.unload_traintype_idx = '").append(filter.getTrainType()).append("'");
        sql.append(" order by de.device_info_code");
                
        
        
        String totalSQL = SELECT_COUNT_1_FROM + sql + RIGHT_BRACKET ;
        String querySQL = sql.toString();
        return super.findPageList(totalSQL, querySQL, start, limit, null, orders);
    }
    
    /**
     * <li>方法说明：下拉查询
     * <li>方法名称：getBaseComboData
     * @param req HttpServletRequest对象
     * @param start 开始页
     * @param limit 页长
     * @return page转换的map
     */
    @Override
    public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit) throws Exception {
        String mod = req.getParameter("queryHql");
        if("1".equals(mod))
            return getRdpParts(req, start, limit);
        else if("2".equals(mod))
            return getRdpUnloadTrainType(req, start, limit);
        else if("3".equals(mod))
            return getRdpUnloadTrainNo(req, start, limit);
        return null;
    }

    /**
     * <li>方法说明： 查询兑现单配件
     * <li>方法名：getRdpParts
     * @param req request
     * @param start start
     * @param limit limit
     * @return
     * <li>创建人： 张凡
     * <li>创建日期：2015年11月17日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    private Map<String, Object> getRdpParts(HttpServletRequest req, int start, int limit) {
        String queryValue = StringUtil.nvlTrim(req.getParameter("query"), null);
        StringBuffer sql = new StringBuffer("SELECT DISTINCT r.parts_name, r.parts_no FROM pjjx_parts_rdp r where ");
        sql.append("exists (select 1 from pjjx_parts_rdp_equip_card c where r.idx=c.rdp_idx and c.record_status = 0)");
        sql.append(" and r.record_status = 0");
        if(queryValue != null){
            sql.append(" and (parts_name like '%").append(queryValue).
            append("%' or parts_no like '%").append(queryValue).append("%')");
        }
        
        String totalSQL = SELECT_COUNT_1_FROM + sql + RIGHT_BRACKET;
        
        Page<Object[]> page = findPartsByRdp(totalSQL, sql.toString(), start, limit);
        
        List<PartsRdp> rdpList = new ArrayList<PartsRdp>(page.getList().size());
        
        for(Object[] o : page.getList()){
            PartsRdp rdp = new PartsRdp();
            rdp.setPartsName(o[1] + ":" + o[0]);
            rdp.setPartsNo(String.valueOf(o[1]));
            rdpList.add(rdp);
        }
        return new Page<PartsRdp>(page.getTotal(), rdpList).extjsStore();
    }
    
    /**
     * <li>方法说明：兑现单下车车型
     * <li>方法名：getRdpUnloadTrainType
     * @param req request
     * @param start start
     * @param limit limit
     * @return
     * <li>创建人： 张凡
     * <li>创建日期：2015年11月17日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    private Map<String, Object> getRdpUnloadTrainType(HttpServletRequest req, int start, int limit) {
        String queryValue = StringUtil.nvlTrim(req.getParameter("query"), null);
        StringBuffer sql = new StringBuffer("SELECT DISTINCT r.unload_traintype, r.unload_traintype_idx FROM pjjx_parts_rdp r where ");
        sql.append("exists (select 1 from pjjx_parts_rdp_equip_card c where r.idx=c.rdp_idx and c.record_status = 0)");
        sql.append(" and r.record_status = 0");
        if(queryValue != null){
            sql.append(" and unload_traintype like '%").append(queryValue).append(SUFFIX_TOKEN);
        }
        
        String totalSQL = SELECT_COUNT_1_FROM + sql + RIGHT_BRACKET;
        
        Page<Object[]> page = findPartsByRdp(totalSQL, sql.toString(), start, limit);
        
        List<PartsRdp> rdpList = new ArrayList<PartsRdp>(page.getList().size());
        
        for(Object[] o : page.getList()){
            PartsRdp rdp = new PartsRdp();
            rdp.setUnloadTrainType(String.valueOf(o[0]));
            rdp.setUnloadTrainTypeIdx(String.valueOf(o[1]));
            rdpList.add(rdp);
        }
        return new Page<PartsRdp>(page.getTotal(), rdpList).extjsStore();
    }
    
    /**
     * <li>方法说明：兑现单下车车号
     * <li>方法名：getRdpUnloadTrainNo
     * @param req request
     * @param start start
     * @param limit limit
     * @return
     * <li>创建人： 张凡
     * <li>创建日期：2015年11月17日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    private Map<String, Object> getRdpUnloadTrainNo(HttpServletRequest req, int start, int limit) {
        String queryValue = StringUtil.nvlTrim(req.getParameter("query"), null);
        StringBuffer sql = new StringBuffer("SELECT DISTINCT unload_trainno FROM pjjx_parts_rdp r where ");
        sql.append("exists (select 1 from pjjx_parts_rdp_equip_card c where r.idx=c.rdp_idx and c.record_status = 0)");
        sql.append(" and r.record_status = 0");
        if(queryValue != null){
            sql.append(" and unload_trainno like '%").append(queryValue).append(SUFFIX_TOKEN);
        }
        
        String totalSQL = SELECT_COUNT_1_FROM + sql + RIGHT_BRACKET;
        
        Page<Object[]> page = findPartsByRdp(totalSQL, sql.toString(), start, limit);
        
        List<PartsRdp> rdpList = new ArrayList<PartsRdp>(page.getList().size());
        
        for(Object o : page.getList()){
            PartsRdp rdp = new PartsRdp();
            rdp.setUnloadTrainNo(String.valueOf(o));
            rdpList.add(rdp);
        }
        return new Page<PartsRdp>(page.getTotal(), rdpList).extjsStore();
    }
    
    /**
     * <li>方法说明： 查询配件
     * <li>方法名：findPartsByRdp
     * @param totalSQL 总条数SQL
     * @param sql 查询SQL
     * @param beginIdx 开始页
     * @param pageSize 页条数
     * @return
     * <li>创建人： 张凡
     * <li>创建日期：2015年11月17日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    @SuppressWarnings("unchecked")
    public Page<Object[]> findPartsByRdp(final String totalSQL, final String sql, final int beginIdx, final int pageSize){
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (Page<Object[]>)template.execute(new HibernateCallback(){
            public Page<Object[]> doInHibernate(Session s) {
                Query q = null;
                try {
                    q = s.createSQLQuery(totalSQL);
                    int total = MixedUtils.getNumValue(q.uniqueResult());
                    q.setCacheable(false);
                    int begin = beginIdx > total ? total : beginIdx;
                    q = s.createSQLQuery(sql).setFirstResult(begin).setMaxResults(pageSize);
                    return new Page<Object[]>(total, q.list());
                } catch (HibernateException e) {
                    throw e;
                } finally{
                    if(q != null)   q.setCacheable(false);
                }
            }
        });
    }
}
