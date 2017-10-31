package com.yunda.zb.common.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.jx.jczl.attachmanage.entity.JczlTrain;
import com.yunda.jx.jczl.attachmanage.manager.JczlTrainManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车车号业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-3-9
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "trainNoManager")
public class TrainNoManager extends JXBaseManager<JczlTrain, JczlTrain> implements IbaseCombo {
    
    /** 组织机构业务类 */
    @Resource
    private OmOrganizationManager omOrganizationManager;
    
    /** 机车信息业务类 */
    @Resource
    private JczlTrainManager jczlTrainManager;
    
    /** 机车ID常量字符串 */
    private static final String TRAINTYPEIDX = "trainTypeIDX";
    
    /** isAll常量字符串 */
    private static final String ISALL = "isAll";
    
    /** queryHql常量字符串 */
    private static final String QUERYHQL = "queryHql";
    
    /**
     * <li>说明：重写getBaseComboData，获取下拉列表前台store所需Map对象
     * <li>创建人：程锐
     * <li>创建日期：2014-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param req HttpServletRequest对象
     * @param start 开始行
     * @param limit 每页记录数
     * @return Map<String,Object> 下拉列表前台store所需Map对象
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();        
        String queryParams = req.getParameter("queryParams");
        Map queryParamsMap = new HashMap();
        if (!StringUtil.isNullOrBlank(queryParams)) {
            queryParamsMap = JSONUtil.read(queryParams, Map.class);
        }
        String isAll = req.getParameter(ISALL);
        queryParamsMap.put(ISALL, isAll);
        String isIn = "false";
        if (queryParamsMap.get("isIn") != null) {
            isIn = String.valueOf(queryParamsMap.get("isIn"));
            queryParamsMap.remove("isIn");
        }
        if ("true".equals(isIn)) {// 查询调入机车
            map = jczlTrainManager.findPageDataList("{'trainTypeIDX':'" + queryParamsMap.get(TRAINTYPEIDX) + "'}", 
                                                   start, 
                                                   limit, 
                                                   null, 
                                                   "moveIn").extjsStore();
        } else {
            // query参数是获取EXTJS的combox控件捕获的键盘输入文字
            String queryValue = req.getParameter("query");
            if (!StringUtil.isNullOrBlank(queryValue))
                queryParamsMap.put("queryValue", queryValue);
            String queryHql = req.getParameter(QUERYHQL);
            if (!StringUtil.isNullOrBlank(queryHql))
                queryParamsMap.put(QUERYHQL, queryHql);
            map = page(queryParamsMap, start, limit);
        }
        return map;
    }
    
    
    /**
     * <li>说明：获取下拉列表前台store所需Map对象
     * <li>创建人：程锐
     * <li>创建日期：2015-3-9
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：添加车号查询
     * @param queryParams 查询参数Map对象
     * @param start 开始行
     * @param limit 每页记录数
     * @return 下拉列表前台store所需Map对象
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> page(Map queryParams, int start, int limit) {               
        String queryHql = CommonUtil.getMapValue(queryParams, QUERYHQL);//自定义hql
        if (!StringUtil.isNullOrBlank(queryHql))
            queryParams.remove(QUERYHQL);
        String isCx = CommonUtil.getMapValue(queryParams, "isCx");//是选择配属机车还是承修机车，yes显示承修机车
        if (!StringUtil.isNullOrBlank(isCx))
            queryParams.remove("isCx");
        String isAll = CommonUtil.getMapValue(queryParams, ISALL);//是否只显示本段机车，yes显示所有机车
        if (!StringUtil.isNullOrBlank(isAll))
            queryParams.remove(ISALL);
        String isRemoveRun = CommonUtil.getMapValue(queryParams, "isRemoveRun");//是否排除在修机车，true排除
        if (StringUtil.isNullOrBlank(isRemoveRun))
            isRemoveRun = "false";
        else
            queryParams.remove("isRemoveRun");
        String queryValue = CommonUtil.getMapValue(queryParams, "queryValue");//获取EXTJS的combox控件捕获的键盘输入值
        if (!StringUtil.isNullOrBlank(queryValue))
            queryParams.remove("queryValue");
        String trainTypeIDX = CommonUtil.getMapValue(queryParams, "trainTypeIDX");
        String trainNo = CommonUtil.getMapValue(queryParams, "trainNo");//车号
        String trainTypeShortName = CommonUtil.getMapValue(queryParams, "trainTypeShortName");
        
        StringBuffer hql = new StringBuffer(); 
        StringBuffer selectHql = new StringBuffer(); 
        
        if (StringUtil.isNullOrBlank(queryHql)) {
            
            //查询页面未根据车型过滤的车号控件，需要过滤重复车号的操作
            if (StringUtil.isNullOrBlank(trainTypeIDX) && StringUtil.isNullOrBlank(trainTypeShortName))
                hql.append("select distinct trainNo ");
            if ("yes".equals(isCx)) { //查询承修机车                
                selectHql.append("from TrainConfigInfo where recordStatus=0 ");
            } else if ("no".equals(isCx)) { // 查询全部机车                
                selectHql.append("from JczlTrainView where recordStatus=0 ");
            }
            if (!"yes".equals(isAll)) {
                String orgCode = JXSystemProperties.OVERSEA_ORGCODE;
                OmOrganization org = omOrganizationManager.findOrgForCode(orgCode);
                String orgseq = org != null ? org.getOrgseq() : "";
                if (!StringUtil.isNullOrBlank(orgseq)) {
                    selectHql.append(" and holdOrgId in (select orgid from OmOrganization where orgseq like '")
                       .append(orgseq).append("%' and status='running')");
                }
            }            
            if ("true".equals(isRemoveRun)) {// isRemoveRun为true代表需排除在段机车
                selectHql.append(" and trainNo not in (select trainNo from TrainAccessAccount where recordStatus=0 and outTime is null and trainNo is not null");
                if (!StringUtil.isNullOrBlank(trainTypeIDX))
                    selectHql.append(" and trainTypeIDX = '").append(trainTypeIDX).append("'");
                if (!StringUtil.isNullOrBlank(trainTypeShortName))
                    selectHql.append(" and trainTypeShortName = '").append(trainTypeShortName).append("'");
                if (!StringUtil.isNullOrBlank(trainNo))
                    selectHql.append(" and trainNo like '%").append(trainNo).append("%'");
                selectHql.append(")");
            }
            if (!StringUtil.isNullOrBlank(queryValue)) {
                selectHql.append(" and trainNo like '").append(queryValue).append("%'"); // 修改默认从首字开始查询匹配
            }
            
            //车号需要like查询，判断是否传递车号
            if (!StringUtil.isNullOrBlank(trainNo)){
                queryParams.remove("trainNo");
                selectHql.append(" and trainNo like '%").append(trainNo).append("%'"); // 修改默认从首字开始查询匹配
            }
            //拼接车号条件
            selectHql.append(CommonUtil.buildParamsHql(queryParams));
            
            
            selectHql.append(" order by trainNo");
            hql.append(selectHql.toString());
        } else 
            hql.append(queryHql);
        
        StringBuffer totalHql = new StringBuffer();
        if (StringUtil.isNullOrBlank(trainTypeIDX) && StringUtil.isNullOrBlank(trainTypeShortName))  {
            totalHql.append(" select count(distinct trainNo) ").append(selectHql.toString());
        } else {
            totalHql.append(" select count(*) ").append(hql.toString());
        }
        Page page = findPageList(totalHql.toString(), hql.toString(), start, limit);
        //查询页面未根据车型过滤的车号控件
        if (StringUtil.isNullOrBlank(trainTypeIDX) && StringUtil.isNullOrBlank(trainTypeShortName)) {
            List<JczlTrain> list = new ArrayList<JczlTrain>();
            List pageList = page.getList();
            JczlTrain train = null;
            for (int i = 0; i < pageList.size(); i++) {
                train = new JczlTrain();
                train.setTrainNo(pageList.get(i).toString());
                list.add(train);
            }
            page = new Page(page.getTotal(), list);
        }
        return page.extjsStore();
    }
}
