package com.yunda.freight.base.vehicle.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.IbaseComboTree;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.freight.base.vehicle.entity.TrainVehicleType;

/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 车型业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-3-21
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("trainVehicleTypeManager")
public class TrainVehicleTypeManager extends JXBaseManager<TrainVehicleType, TrainVehicleType> implements IbaseCombo, IbaseComboTree {
    
    /**
     * 验证车型编码唯一
     */
    @Override
    public String[] validateUpdate(TrainVehicleType t) {
        String[] errorMsg = super.validateUpdate(t);
        if (null != errorMsg) {
            return errorMsg;
        }
        String hql = "From TrainVehicleType Where recordStatus = 0 And typeCode = ?";
        TrainVehicleType type = (TrainVehicleType) this.daoUtils.findSingle(hql, new Object[]{ t.getTypeCode() });
        if (null != type && !type.getIdx().equals(t.getIdx())) {
            return new String[]{"车型代码：" + t.getTypeCode() + "已经存在，不能重复添加！"};
        }
        return null;
    }
    
    /**
     * 车型下拉框
     */
    @Override
    @SuppressWarnings("unchecked")  
    public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit) throws Exception {
        
        String queryParams = req.getParameter("queryParams");
        
        Map queryParamsMap = new HashMap();
        if (!StringUtil.isNullOrBlank(queryParams)) {
            queryParamsMap = JSONUtil.read(queryParams, Map.class);
        }
        StringBuffer hql = new StringBuffer(" select t from TrainVehicleType t where t.recordStatus = 0 ") ;
        // query参数是获取EXTJS的combox控件捕获的键盘输入文字
        String queryValue = StringUtil.nvlTrim(req.getParameter("query"), "");   
        if(!StringUtil.isNullOrBlank(queryValue)){
            hql.append(" and t.typeCode like '%").append(queryValue).append("%'");
        }
        
        // 客货类型
        String vehicleType = String.valueOf(queryParamsMap.get("vehicleType"));
        if(!StringUtil.isNullOrBlank(vehicleType)){
            hql.append(" and t.vehicleType = '").append(vehicleType).append("'");
        }
        
        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*)");
        totalHql.append(hql.toString().substring(beginPos));
        totalHql.append("  order by t.typeCode");
        hql.append(" order by t.typeCode");
        Page page = findPageList(totalHql.toString(), hql.toString(), start, limit);
        return page.extjsStore();
    }

    /**
     * <li>说明：车型下拉树
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param vehicleType 客货类型 10 货车 20 客车
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> findTrainTypeForTree(String vehicleType) {
        StringBuilder hql = new StringBuilder();
        hql.append(" From TrainVehicleType t where t.recordStatus = 0 and vehicleType = ? ");
        boolean isLeaf = true;
        List<TrainVehicleType> list = this.daoUtils.find(hql.toString(), new Object[]{vehicleType});
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        for (TrainVehicleType trainType : list) {           
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            nodeMap.put("id",  trainType.getIdx());
            nodeMap.put("text", trainType.getTypeCode());
            nodeMap.put("leaf", isLeaf);
            children.add(nodeMap);
        }
        return children;
    }


}
