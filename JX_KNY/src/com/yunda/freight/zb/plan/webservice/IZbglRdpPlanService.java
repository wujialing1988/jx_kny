package com.yunda.freight.zb.plan.webservice;

import java.io.IOException;
import com.yunda.jx.pjjx.webservice.IService;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 列检计划接口
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-3-31
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public interface IZbglRdpPlanService extends IService {
    
    /**
     * <li>说明：查询列检计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            entityJson: {
                rdpPlanStatus:"ONGOING"
            },
            operatorId: "800109",
            filterByOperatorId: "false",
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
    public String findZbglRdpPlanList(String jsonObject) throws IOException;
    
    
    /**
     * <li>说明：查询列检车辆计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            entityJson: {
                rdpPlanIdx:"8a8284f24ab80704014ab891375a0004",
                rdpRecordStatus:""
            },
            operatorId: "800109",
            filterByOperatorId: "false",
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
    public String findZbglRdpRecordList(String jsonObject) throws IOException;

    /**
     * <li>说明：完成车辆计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-04-01
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            ids:["8a8284c35ced2f47015ced3bd0e30003","8a8284c35ced2f47015ced3bd0e30004"],
            operatorId: "800109"
       }
     * @return
     * @throws IOException
     */
    public String bacthCompleteRecordHC(String jsonObject) throws IOException;
    
    /**
     * <li>说明：填写车号
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            entityJson: {
                idx:"8a8284f24ab80704014ab891375a0004",
                trainNo:"0055"
            },
            operatorId: "800109"
       }
     * @return
     * @throws IOException
     */
    public String writeTrainNo(String jsonObject) throws IOException;
    
    /**
     * <li>说明：客车库列检专业查询（按专业）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject{
            planIdx:"8a8284c35cf1f528015cf261c39400f8",
            operatorId: "1039"
       }
     * @return
     * @throws IOException
     */
    public String findRdpWis(String jsonObject) throws IOException ;
    
    
    /**
     * <li>说明：库列检查询车辆（按专业）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject{
            planIdx:"8a8284c35cf1f528015cf261c39400f8",
            wiIdx:"8a8284c35cf16978015cf1b67ca50026",
            operatorId: "1039"
       }
     * @return
     * @throws IOException
     */
    public String findRecordByWi(String jsonObject) throws IOException ;
    
    /**
     * <li>说明：专业下批量保存车辆数据（按专业）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject{
            records:["8a8284c35cf1f528015cf261c39d00f9","8a8284c35cf1f528015cf261c39f00fa"],
            wiIdx:"8a8284c35cf16978015cf1b67ca50026",
            operatorId: "1039"
       }
     * @return
     * @throws IOException
     */
    public String saveZbglRdpWiDatas(String jsonObject) throws IOException ;
    
    /**
     * <li>说明：同一车辆下批量保存专业数据（按车辆）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-7-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject{
            recordIdx:"8a8284c35cf16978015cf1b67ca50026",
            wiIdxs:["8a8284c35cf1f528015cf261c39d00f9","8a8284c35cf1f528015cf261c39f00fa"],
            operatorId: "1039"
       }
     * @return
     * @throws IOException
     */
    public String saveZbglRecordDatas(String jsonObject) throws IOException ;
    
    /**
     * <li>说明：客车库列检车辆业查询（按车辆）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject{
            planIdx:"8a8284c35cf1f528015cf261c39400f8",
            operatorId: "1039"
       }
     * @return
     * @throws IOException
     */
    public String findRecordsForKC(String jsonObject) throws IOException ;
    
}
