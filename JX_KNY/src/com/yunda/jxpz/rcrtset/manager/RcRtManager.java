package com.yunda.jxpz.rcrtset.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jxpz.rcrtset.entity.RcRt;

@Service(value="rcRtManager")
public class RcRtManager extends JXBaseManager<RcRt, RcRt>  implements IbaseCombo{
	/**
	 * 
	 * <li>说明：新增修改保存前的实体对象前的批量验证业务
	 * <li>创建人：程锐
	 * <li>创建日期：2014-2-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entityList
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
    public String[] validateUpdate(RcRt[] entityList) throws BusinessException {
        List<String> errMsg = new ArrayList<String>();
        for(RcRt obj : entityList){
        	RcRt rcRt = new RcRt();
        	rcRt.setRcIDX(obj.getRcIDX());
        	rcRt.setRepairtimeIDX(obj.getRepairtimeIDX());
            //同时查询的是未删除状态的数据
            rcRt.setRecordStatus(0);
            List<RcRt> countList = daoUtils.getHibernateTemplate().findByExample(rcRt);
            if(countList != null && countList.size() > 0){
                errMsg.add("【"+obj.getRepairtimeName()+"】修次，已经使用！");
            }
            if (errMsg.size() > 0) {
                String[] errArray = new String[errMsg.size()];
                errMsg.toArray(errArray);
                return errArray;
            }
        }
	    return null;
	}
	/**
	 * 
	 * <li>说明：批量保存
	 * <li>创建人：程锐
	 * <li>创建日期：2014-2-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param objList
	 * @return
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public String[] saveOrUpdateList(RcRt[] objList) throws BusinessException, NoSuchFieldException{
        String[] errMsg = this.validateUpdate(objList);  //验证
        List<RcRt> entityList = new ArrayList<RcRt>();
        if (errMsg == null || errMsg.length < 1) {
            for(RcRt t : objList){ 
                entityList.add(t);
            }
            this.saveOrUpdate(entityList);
        }
        return errMsg;
    }
	/**
     * 
     * <li>说明：获取下拉列表前台store所需Map对象
     * <li>创建人：程梅
     * <li>创建日期：2012-10-31
     * <li>修改人： 刘晓斌
     * <li>修改日期：2013-11-17
     * <li>修改内容：根据缓存开关来执行查询
     * @param queryParams 查询参数Map对象
     * @param start 开始行
     * @param limit 每页记录数
     * @param queryHql 查询Hql字符串
     * @return Map<String,Object> 下拉列表前台store所需Map对象
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> page(Map queryParams, int start, int limit, String queryHql) throws ClassNotFoundException {
        StringBuffer hql = new StringBuffer();
        // 拼接查询sql，如queryHql配置项不为空则直接执行queryHql
        if (!StringUtil.isNullOrBlank(queryHql)) {
            hql.append(queryHql);
        } else { // queryHql配置项为空则按queryParams配置项拼接Hql
            hql.append(" from RcRt where 1=1 and recordStatus=0");
            if (!queryParams.isEmpty()) {
                Set<Map.Entry<String, String>> set = queryParams.entrySet();
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (!StringUtil.isNullOrBlank(value)) {
                        hql.append(" and ").append(key).append(" = '").append(value).append("'");
                    }
                }
            }
            hql.append(" order by repairtimeSeq");
        }
        //根据hql构造totalHql
        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*)");
        totalHql.append(hql.toString().substring(beginPos));
        Page page = null;
        if(enableCache()){
        	page = super.cachePageList(totalHql.toString(), hql.toString(), start, limit);
        } else {
        	page = super.findPageList(totalHql.toString(), hql.toString(), start, limit);
        }
        return page.extjsStore();
    }
	/**
     * <li>说明：重写getBaseComboData，获取下拉列表前台store所需Map对象
     * <li>创建人：程锐
     * <li>创建日期：2014-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param req     
     * @param start 开始行
     * @param limit 每页记录数
     * @return Map<String,Object> 下拉列表前台store所需Map对象
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit) throws Exception{
    	String queryHql = req.getParameter("queryHql");
        String queryParams = req.getParameter("queryParams");
        Map queryParamsMap = new HashMap();
        if (!StringUtil.isNullOrBlank(queryParams)) {
            queryParamsMap = JSONUtil.read(queryParams, Map.class);
        }
        return page(queryParamsMap, start, limit, queryHql);
    }
    /**
     * 
     * <li>说明：修程修次列表
     * <li>创建人：程锐
     * <li>创建日期：2014-2-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity
     * @param repairtimeName
     * @param queryHql
     * @param query
     * @return 分页列表
     * @throws BusinessException
     */
    public Page<RcRt> findPageList(SearchEntity<RcRt> searchEntity, String repairtimeName, String queryHql, String query)	throws BusinessException {
    	RcRt entity = searchEntity.getEntity();
	    String totalHql = "select count(*) ";
	    String hql = " select new RcRt(t.idx,t.rcIDX,x.xcName,t.repairtimeIDX,t.repairtimeName,t.repairtimeSeq) ";
	    StringBuilder awhere = new StringBuilder();
	    awhere.append(" from RcRt t,XC x where t.rcIDX=x.xcID and t.recordStatus=0 ") ;
	    // 拼接查询sql，如queryHql配置项不为空则直接执行queryHql
	    if (!StringUtil.isNullOrBlank(queryHql)) {
	        hql = queryHql;
	        int beginPos = hql.toLowerCase().indexOf("from");
	        totalHql = " select count (*) " + hql.substring(beginPos);
	    }
	    if (!"".equals(repairtimeName)) {
	        awhere.append(" and t.repairtimeName like '%").append(repairtimeName).append("%'");
	    }
	    // 关键字查询
	    if (!"".equals(query)) {
	        awhere.append(" and t.repairtimeName like '%").append(query).append("%'");
	    }
	    if(entity != null){
	        if(!StringUtil.isNullOrBlank(entity.getRcIDX())){
	            awhere.append(" and t.rcIDX = '").append(entity.getRcIDX()).append("'");
	        }
	        if(!StringUtil.isNullOrBlank(entity.getRcName())){
	            awhere.append(" and x.xcName = '").append(entity.getRcName()).append("'");
	        }
	    }
	    awhere.append(" order by t.repairtimeName desc ");
	    totalHql +=  awhere.toString();
	    hql += awhere.toString();
	    if(enableCache()){
	    	return super.cachePageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	    } else {
	    	return super.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	    }        
    
    }
}
