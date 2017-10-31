package com.yunda.twt.twtinfo.manager;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.twt.twtinfo.entity.TrainStatusColors;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：TrainStatusColors业务类,机车状态颜色
 * <li>创建人：程锐
 * <li>创建日期：2015-01-20
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "trainStatusColorsManager")
public class TrainStatusColorsManager extends JXBaseManager<TrainStatusColors, TrainStatusColors> {
    
    /**
     * <li>说明：更新前验证
     * <li>创建人：程锐
     * <li>创建日期：2015-1-21
     * <li>修改人：
     * <li>修改日期：
     * @param entity 实体对象
     * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
     */
    public String[] validateUpdate(TrainStatusColors entity) {
        TrainStatusColors t = this.getModelByStatus(entity.getStatus());
        if (null != t && !t.getIdx().equals(entity.getIdx())) {
            return new String[]{ "已有相同状态的颜色值存在" };
        }
        return null;
    }
    
    /**
     * <li>说明：根据机车状态获取颜色值
     * <li>创建人：程锐
     * <li>创建日期：2015-1-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param status 机车状态
     * @return 颜色值
     */
    @SuppressWarnings("unchecked")
    public String getColorByStatus(String status) {
        TrainStatusColors entity = this.getModelByStatus(status);
        return null == entity ? null : entity.getColor();
    }
    
    /**
     * <li>说明：根据机车状态获取颜色实体
     * <li>创建人：何涛
     * <li>创建日期：2015-9-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param status 状态名称
     * @return 机车状态获取颜色实体
     */
    public TrainStatusColors getModelByStatus(String status) {
        String hql = "From TrainStatusColors Where status = ?";
        return (TrainStatusColors) this.daoUtils.findSingle(hql, new Object[]{ status });
    }
    
}
