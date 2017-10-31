package com.yunda.jx.jcll.manager;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jcll.entity.TrainRecord;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车履历业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-10-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value="trainRecordManager")
public class TrainRecordManager extends JXBaseManager<TrainRecord, TrainRecord> {
    /**
     * <li>说明：查询机车履历
     * <li>创建人：张迪
     * <li>创建日期：2016-11-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 包装了实体类查询条件的对象
     * @return 分页查询列表
     * @throws BusinessException
     */
    @Override
    public Page<TrainRecord> findPageList(SearchEntity<TrainRecord> searchEntity) throws BusinessException{
        TrainRecord entity = searchEntity.getEntity();
        StringBuilder sb = new StringBuilder(" From TrainRecord Where 1=1");
        
        // 根据“车型”“车号”模糊匹配查询
        String trainNo = StringUtil.nvl(entity.getTrainNo(), "").trim();
        if (!StringUtil.isNullOrBlank(trainNo)) {
            sb.append(" And trainTypeShortName || trainNo Like '%").append(trainNo.toUpperCase()).append("%'");
        }
        
        // 客货类型 10 货车 20 客车
        String vehicleType = StringUtil.nvl(entity.getVehicleType(), "").trim();
        if (!StringUtil.isNullOrBlank(vehicleType)) {
            sb.append(" And vehicleType = '").append(vehicleType).append("'");
        }
        
        // 以“计划开始日期”进行升序排序
        sb.append(" Order By trainTypeShortName ");
        String hql = sb.toString();
        String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
        Page<TrainRecord> page = this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
      
        return page;
    }
}
