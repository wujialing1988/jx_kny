package com.yunda.freight.base.vehicle.webservice;

import java.io.IOException;

import com.yunda.jx.pjjx.webservice.IService;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 车辆车型接口
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-4-5
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public interface ITrainVehicleTypeService extends IService {
    
    /**
     * <li>说明：查询车辆车型列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            entityJson: {
                typeCode:"",
                typeName:"",
                vehicleKindCode:"",
                vehicleType:"10"
            },
            start:1,
            limit:50, 
            orders:[{
                sort: "idx",
                dir: "ASC"
            }]
       }
     * @return
     * @throws IOException
     */
    public String findTrainVehicleTypeList(String jsonObject) throws IOException;
    
}
