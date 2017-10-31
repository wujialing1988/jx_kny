package com.yunda.freight.base.stationTrack.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.alibaba.druid.mapping.Entity;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.freight.base.stationTrack.entity.StationTrack;
import com.yunda.freight.base.trainInspection.entity.TrainInspection;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 股道维护业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-04-10 11:36:09
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings({"unchecked", "unused"})
@Service("stationTrackManager")
public class StationTrackManager extends JXBaseManager<StationTrack, StationTrack> implements IbaseCombo {
    
    /**
     * 验证列检所编码唯一
     */
    @Override
    public String[] validateUpdate(StationTrack t) {
        String[] errorMsg = super.validateUpdate(t);
        if (null != errorMsg) {
            return errorMsg;
        }
        String hql = "From StationTrack Where recordStatus = 0 And trackCode = ? and inspectionIdx = ? ";
        StationTrack entity = (StationTrack) this.daoUtils.findSingle(hql, new Object[]{ t.getTrackCode(),t.getInspectionIdx() });
        if (null != entity && !entity.getIdx().equals(t.getIdx())) {
            return new String[]{"股道编码：" + t.getTrackCode() + "已经存在，不能重复添加！"};
        }
        return null;
    }
    
    /**
     * 股道下拉框
     */
    @Override
    public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit) throws Exception {
        
        String queryParams = req.getParameter("queryParams");
        
        Map queryParamsMap = new HashMap();
        if (!StringUtil.isNullOrBlank(queryParams)) {
            queryParamsMap = JSONUtil.read(queryParams, Map.class);
        }
        // 站点ID
        String siteID = String.valueOf(queryParamsMap.get("siteID"));
        siteID = EntityUtil.findSysSiteId(siteID);
        if(StringUtil.isNullOrBlank(siteID)){
            return null ;
        }
        StringBuffer hql = new StringBuffer(" select t from StationTrack t,TrainInspection k where k.idx = t.inspectionIdx " +
                "and t.recordStatus = 0 and k.recordStatus = 0 and k.inspectionCode = '"+siteID+"'") ;
        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*)");
        totalHql.append(hql.toString().substring(beginPos));
        totalHql.append("  order by t.seqNo");
        hql.append(" order by t.seqNo");
        Page page = findPageList(totalHql.toString(), hql.toString(), start, limit);
        return page.extjsStore();
    }
   
}
