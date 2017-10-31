package com.yunda.freight.base.trainInspection.manager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.freight.base.trainInspection.entity.TrainInspection;
import com.yunda.freight.base.vehicle.entity.TrainVehicleType;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 列检所维护业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-04-10 10:13:18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings({"unchecked", "unused"})
@Service("trainInspectionManager")
public class TrainInspectionManager extends JXBaseManager<TrainInspection, TrainInspection> implements IbaseCombo {
    
    /**
     * 验证列检所编码唯一
     */
    @Override
    public String[] validateUpdate(TrainInspection t) {
        String[] errorMsg = super.validateUpdate(t);
        if (null != errorMsg) {
            return errorMsg;
        }
        String hql = "From TrainInspection Where recordStatus = 0 And inspectionCode = ?";
        TrainInspection entity = (TrainInspection) this.daoUtils.findSingle(hql, new Object[]{ t.getInspectionCode() });
        if (null != entity && !entity.getIdx().equals(t.getIdx())) {
            return new String[]{"列检所编码：" + t.getInspectionCode() + "已经存在，不能重复添加！"};
        }
        return null;
    }
    
    /**
     * 列检所下拉框
     */
    @Override
    public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit) throws Exception {
        
        String queryParams = req.getParameter("queryParams");
        
        Map queryParamsMap = new HashMap();
        if (!StringUtil.isNullOrBlank(queryParams)) {
            queryParamsMap = JSONUtil.read(queryParams, Map.class);
        }
        StringBuffer hql = new StringBuffer(" select t from TrainInspection t where t.recordStatus = 0 " );
        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*)");
        totalHql.append(hql.toString().substring(beginPos));
        totalHql.append("  order by t.idx");
        hql.append(" order by t.idx");
        Page page = findPageList(totalHql.toString(), hql.toString(), start, limit);
        return page.extjsStore();
    }
   
}
