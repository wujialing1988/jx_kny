package com.yunda.jxpz.workplace.manager;

import java.io.Serializable;
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
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jxpz.workplace.entity.WorkPlace;

@Service(value="workPlaceManager")
public class WorkPlaceManager extends JXBaseManager<WorkPlace, WorkPlace> implements IbaseCombo{
    
    /**
     * 重写逻辑删除方法，实现物理删除
     * 张凡 2014/1/14
     */
    @Override
    public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
    	//物理删除站点
        StringBuilder sql = new StringBuilder("delete from JXPZ_WorkPlace where WorkPlace_Code in(");
        //物理删除站点对应的机构信息
        StringBuilder sql_org = new StringBuilder("delete from JXPZ_WorkPlace_TO_ORG where workplace_code in(");
        for (Serializable id : ids) {
            sql.append("'").append(id).append("',");
        	sql_org.append("'").append(id).append("',");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        daoUtils.executeSql(sql.toString());
        
        sql_org.deleteCharAt(sql_org.length() - 1);
        sql_org.append(")");
        daoUtils.executeSql(sql_org.toString());
    }
    
	/**
     * <li>说明：重写getBaseComboData，获取下拉列表前台store所需Map对象
     * <li>创建人：王利成
     * <li>创建日期：2015-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
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
    	StringBuffer hql = new StringBuffer();
        String key = "" ;
        String value = "" ;
        // 拼接查询sql，如queryHql配置项不为空则直接执行queryHql
        if (!StringUtil.isNullOrBlank(queryHql)) {
            hql.append(queryHql);
        }else{
        	 // queryHql配置项为空则按queryParams配置项拼接Hql
            if (!queryParamsMap.isEmpty()) {
                hql.append(" from WorkPlace where 1=1");
                Set<Map.Entry<String, String>> set = queryParamsMap.entrySet();
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                     key = entry.getKey();
                     value = entry.getValue(); 
                    if (!StringUtil.isNullOrBlank(value)) {
                        if("workPlaceName".equals(key)){
                            hql.append(" and ").append(key).append(" like '%").append(value).append("%'");
                        }else{
                            hql.append(" and ").append(key).append(" = '").append(value).append("'");
                        }
                    }
                }
            }else{
                hql = new StringBuffer();
                hql.append(" from WorkPlace where 1=1");
            }
        }
        List list = daoUtils.find(hql.toString());
        if(list==null||list.size()==0){
            hql = new StringBuffer();
            hql.append(" from WorkPlace where 1=1 "); 
            if(!StringUtil.isNullOrBlank(key) && "jyName".equals(key)){
                hql.append(" and workPlaceName like '%").append(value).append("%'");
            }
        }
        //根据hql构造totalHql
        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*)");
        totalHql.append(hql.toString().substring(beginPos));
        Page page = findPageList(totalHql.toString(), hql.toString(), start, limit);
        return page.extjsStore();
    }
}
