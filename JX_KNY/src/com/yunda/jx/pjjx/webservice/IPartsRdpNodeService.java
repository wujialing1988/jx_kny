package com.yunda.jx.pjjx.webservice;

import java.io.IOException;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 用于扩展配件检修节点任务需对接的方法
 * <li>创建人：程锐
 * <li>创建日期：2015-9-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.3
 */
public interface IPartsRdpNodeService  extends IService {
    
    /**
     * <li>说明：查询当前人员可以处理的检修作业节点任务
     * <li>创建人：程锐
     * <li>创建日期：2015-9-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象
     * {
         operatorId: "800109",
         start:0,
         limit:50, 
         orders:[{
                    sort: "idx",
                    dir: "ASC"
                }],
         workStationIDX: "800109",
         status: 1,
         partsNo: "800109",
         identificationCode: "800109"
         unloadTrainType: "HXD10002",
         specificationModel: "转向架"
       }                
     * @return 当前人员可以处理的检修作业节点任务
     */
    public String queryNodeListByUser (String jsonObject);
    
    /**
     * <li>说明：根据作业流程IDX查询其关联的节点任务列表
     * <li>创建人：程锐
     * <li>创建日期：2015-9-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject JSON对象
       {
         rdpIDX: "800109"
       }
     * @return 所属的配件检修流程的节点任务列表
     */
    public String queryNodeListByRdp (String jsonObject);
    
    /**
     * <li>说明：修竣提交节点
     * <li>创建人：程锐
     * <li>创建日期：2015-9-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject JSON对象
     {
        idx: "800109"
     }
     * @return 操作成功与否
     */
    public String finishPartsNode (String jsonObject) throws IOException;
    
    /**
     * <li>说明：配件检修任务单-启动节点
     * <li>创建人：程锐
     * <li>创建日期：2015-9-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * {
     *   idx 配件检修任务节点IDX,
     *   operatorId 操作员ID
     * }
     * @return 操作成功与否
     */
    public String startPartsNode (String jsonObject);
    
    /**
     * <li>方法说明：回退节点
     * <li>方法名：backRepair
     * @param jsonObject { operatorId: 0000, rdpNodeIdx: "xxx", cause: "回退原因" }
     * @return
     * <li>创建人： 张凡
     * <li>创建日期：2015-10-30
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public String backRepair(String jsonObject);
    
    /**
     * <li>方法说明：获取树节点
     * <li>方法名：treeNodes
     * @param jsonObject json字符串
     * @return
     * <li>创建人： 张凡
     * <li>创建日期：2015-10-15
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public String treeNodes(String jsonObject);
    
    /**
     * <li>说明：获取配件作业计划的所有子节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-11-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject JSON对象
       {
         rdpIDX: "800109"
       }
     * @return 配件作业计划的所有子节点列表
     */
    public String getLeafNodeListByRdp (String jsonObject);
    
    /**
     * <li>说明：修竣提交节点前的验证
     * <li>创建人：程锐
     * <li>创建日期：2015-9-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject JSON对象
     {
        idx: "800109"
     }
     * @return 1验证通过：return "{'flag':'true','message':'操作成功！'}"; 
			   2验证发现检修记录单或作业工单未完成，工位终端收到此信息应做提示处理：
						return "{'flag':'true','message':'作业工单未全部处理 '}";
			   3验证时报错：
						return "{'flag':'false','message':错误信息}";
     */
    public String validateFinishedStatus (String jsonObject) throws IOException;    
    
    /**
     * <li>说明：获取返修原因
     * <li>创建人：程锐
     * <li>创建日期：2016-2-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
                rdpNodeIDX: "800109"
              }
     * @return 返修原因
     */
    public String getBackRepairCause(String jsonObject) ;
    
}
