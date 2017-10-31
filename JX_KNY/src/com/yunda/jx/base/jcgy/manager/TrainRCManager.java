package com.yunda.jx.base.jcgy.manager;
 
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.jx.base.jcgy.entity.TrainRC;
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
@Service(value="trainRCManager")
public class TrainRCManager extends JXBaseManager<TrainRC, TrainRC> implements IbaseCombo{
	private final String XC_NAME = "xcName";
    private final String PERCENT_SIGN = "%'";
    private final String AND = " and ";
    /** 组织机构业务类：OmOrganizationManager */
     private OmOrganizationManager omOrganizationManager;
     public OmOrganizationManager getOmOrganizationManager() {
		return omOrganizationManager;
	}
	public void setOmOrganizationManager(OmOrganizationManager omOrganizationManager) {
		this.omOrganizationManager = omOrganizationManager;
	}
    /**
     * <li>说明：获取下拉列表前台store所需Map对象
     * <li>创建人：程梅
     * <li>创建日期：2012-10-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param queryParams 查询参数Map对象
     * @param start 开始行
     * @param limit 每页记录数
     * @param queryHql 查询Hql字符串
     * @param undertakeTrainTypeIDX 车型
     * @param orgid 机构ID
     * @return Map<String,Object> 下拉列表前台store所需Map对象
     * @throws ClassNotFoundException
     */
    public Map<String, Object> page(Map<String, String> queryParams, int start, int limit, String queryHql,
    		String undertakeTrainTypeIDX,Long orgid) throws ClassNotFoundException {
        StringBuffer hql = new StringBuffer();
        String flagSql = "";
        String key = "" ;
        String value = "" ;
        // 拼接查询sql，如queryHql配置项不为空则直接执行queryHql
        if (!StringUtil.isNullOrBlank(queryHql)) {
            hql.append(queryHql);
        } else { // queryHql配置项为空则按queryParams配置项拼接Hql
            if (!queryParams.isEmpty()) {
                hql.append(" from TrainRC where 1=1 and undertakeOrgId=").append(orgid);
                Set<Entry<String, String>> set = queryParams.entrySet();
                Iterator<Entry<String, String>> it = set.iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                     key = entry.getKey();
                     value = entry.getValue(); 
                    if (!StringUtil.isNullOrBlank(value)) {
                        if(XC_NAME.equals(key)){
                            hql.append(AND).append(key).append(" like '%").append(value).append(PERCENT_SIGN);
                        }else{
                            hql.append(AND).append(key).append(" = '").append(value).append("'");
                        }
                    }
                    if(!StringUtil.isNullOrBlank(undertakeTrainTypeIDX)){
                        flagSql = " and xcID not in (select nvl(rcIDX,'-1') from UndertakeTrainTypeRC " +
                                " where recordStatus=0 and undertakeTrainTypeIDX='"+undertakeTrainTypeIDX+"')";
                        hql.append(flagSql);
                    }
                }
            }else{
                hql = new StringBuffer();
                hql.append(" from XC where 1=1");
            }
        }
        List<?> list = daoUtils.find(hql.toString());
        if(list==null||list.size()==0){
            hql = new StringBuffer();
            hql.append(" from XC where 1=1 "); 
            if(!StringUtil.isNullOrBlank(key) && XC_NAME.equals(key)){
                hql.append(" and xcName like '%").append(value).append(PERCENT_SIGN);
            }
            if(!"".equals(undertakeTrainTypeIDX)){
                hql.append(flagSql); 
            }
        }
        //根据hql构造totalHql
        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*)");
        totalHql.append(hql.toString().substring(beginPos));
        Page page = findPageList(totalHql.toString(), hql.toString(), start, limit);
        return page.extjsStore();
    }

    /**
     * <li>说明：重写getBaseComboData，获取下拉列表前台store所需Map对象
     * <li>创建人：程锐
     * <li>创建日期：2014-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     *  FIXME 方法内容与page方法几乎相同，重构此方法，去掉重复代码，调用page  edit by cheung van(zhangfan)
     * @param req request
     * @param start 开始行
     * @param limit 每页记录数
     * @return Map<String,Object> 下拉列表前台store所需Map对象
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit) throws Exception{
        String queryParams = req.getParameter("queryParams");
        Map queryParamsMap = new HashMap();
        if (!StringUtil.isNullOrBlank(queryParams)) {
            queryParamsMap = JSONUtil.read(queryParams, Map.class);
        }
        StringBuffer hql = new StringBuffer();
        hql.append(" from XC where 1=1 "); 
        // 修程名称
        String xcName = String.valueOf(queryParamsMap.get("xcName"));
        if(!StringUtil.isNullOrBlank(xcName)){
            hql.append(" and xcName like '%").append(xcName).append(PERCENT_SIGN);
        }
        // 客货类型
        String vehicleType = String.valueOf(queryParamsMap.get("vehicleType"));
        if(!StringUtil.isNullOrBlank(vehicleType)){
            hql.append(" and vehicleType = '").append(vehicleType).append("'");
        }
        //根据hql构造totalHql
        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*)");
        totalHql.append(hql.toString().substring(beginPos));
        Page page = findPageList(totalHql.toString(), hql.toString(), start, limit);
        return page.extjsStore();
    }
}