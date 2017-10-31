package com.yunda.freight.zb.detain.webservice;

import java.io.IOException;

import com.yunda.jx.pjjx.webservice.IService;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 扣车管理接口
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-4-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public interface IDetainTrainService extends IService {
    
    /**
     * <li>说明：申请扣车
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            entityJson: {
                idx:"8a8284f24ab80704014ab891375a0002", // 如果是拒绝后再申请，则需要传
                trainTypeIdx:"8a8284f24ab80704014ab891375a0002",
                trainNo:"0001",
                detainReason:"扣车原因",
                detainTypeCode:"扣车类型编码",
                detainTypeName:"扣车类型名称"
            },
            operatorId: "7"
       }
     * @return
     * @throws IOException
     */
    public String applyDetainTrain(String jsonObject) throws IOException;
    
    /**
     * <li>说明：撤销申请
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            idx:"8a8284f24ab80704014ab891375a0002",
            operatorId: "7"
       }
     * @return
     * @throws IOException
     */
    public String deleteDetainTrain(String jsonObject) throws IOException;
    
    /**
     * <li>说明：查询扣车
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
     *      entityJson: {
                trainTypeIdx:"8a8284f24ab80704014ab891375a0002",
                trainNo:"0001"
            },
            operatorId: "7",
            start:1,
            limit:50, 
            orders:[{
                sort: "seqNum",
                dir: "ASC"
            }]
       }
     * @return
     * @throws IOException
     */
    public String findDetainTrain(String jsonObject) throws IOException;
    
}
