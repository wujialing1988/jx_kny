package com.yunda.freight.base.vehicle.action;

import java.util.HashMap;
import java.util.List;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.JSONUtil;
import com.yunda.freight.base.vehicle.entity.TrainVehicleType;
import com.yunda.freight.base.vehicle.manager.TrainVehicleTypeManager;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 车型action
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-3-21
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public class TrainVehicleTypeAction extends JXBaseAction<TrainVehicleType, TrainVehicleType, TrainVehicleTypeManager> {

    /**  序列  */
    private static final long serialVersionUID = 1L;
    
    /**
     * <li>说明：车型下拉树
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void findTrainTypeForTree() throws Exception {
        String vehicleType = getRequest().getParameter("vehicleType");
        List<HashMap<String, Object>>  children = manager.findTrainTypeForTree(vehicleType);
        JSONUtil.write(getResponse(), children);
    }
    
    
}
