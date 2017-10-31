package com.yunda.zb.oilconsumption.manager;

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
import com.yunda.zb.oilconsumption.entity.Jyzl;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：Jyzl业务类,机油种类编码
 * <li>创建人：王利成
 * <li>创建日期：2015-01-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */ 
@Service(value="jyzlManager")
public class JyzlManager extends JXBaseManager<Jyzl, Jyzl> implements IbaseCombo{
	/**
	 * 
	 * <li>说明：基于sql查询语句的分页查询
	 * <li>创建人：程锐
	 * <li>创建日期：2015-1-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param req 查询请求实体
	 * @param start 开始行
	 * @param limit 每页记录数
	 * @return  Map<String, Object> 查询结果集
	 * @throws BusinessException
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
                hql.append(" from Jyzl where 1=1");
                Set<Map.Entry<String, String>> set = queryParamsMap.entrySet();
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                     key = entry.getKey();
                     value = entry.getValue(); 
                    if (!StringUtil.isNullOrBlank(value)) {
                        if("jyName".equals(key)){
                            hql.append(" and ").append(key).append(" like '%").append(value).append("%'");
                        }else{
                            hql.append(" and ").append(key).append(" = '").append(value).append("'");
                        }
                    }
                }
            }else{
                hql = new StringBuffer();
                hql.append(" from Jyzl where 1=1");
            }
        }
        List list = daoUtils.find(hql.toString());
        if(list==null||list.size()==0){
            hql = new StringBuffer();
            hql.append(" from Jyzl where 1=1 "); 
            if(!StringUtil.isNullOrBlank(key) && "jyName".equals(key)){
                hql.append(" and jyName like '%").append(value).append("%'");
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