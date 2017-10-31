package com.yunda.jx.jxgc.webservice;

import java.io.IOException;

import com.yunda.jx.pjjx.webservice.IService;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 提票webservice接口
 * <li>创建人：程锐
 * <li>创建日期：2015-7-15
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
public interface IFaultTicketService  extends IService {
    
    /**
     * <li>说明：获取提票类型
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 提票类型列表
     */
    public String getFaultTypes();
    
    /**
     * <li>说明：获取承修车型
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 承修车型列表
     */
    public String getUndertakeTrainType();
    
    /**
     * <li>说明：根据车型获取车号列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx 车型主键
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 车号列表
     */
    public String getTrainNoByTrainType(String trainTypeIdx, int start, int limit);
    
    /**
     * <li>说明：查询车号列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx 车型主键
     * @param vehicleType 客货类型
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return
     */
    public String getTrainNo(String trainTypeIdx,String vehicleType, int start, int limit);
    
    /**
     * <li>说明：根据车型车号获取故障提票组成树根节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx 车型主键
     * @param trainNo 车号
     * @return 故障提票组成树根节点列表
     */
    public String getBuildUpType(String trainTypeIdx, String trainNo);
    
    /**
     * <li>说明：获取故障提票下级树节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 上级父节点idx
     * @param partsBuildUpTypeIdx 所属组成型号idx
     * @param isVirtual 表示下层节点是否是虚拟位置下节点
     * @return 树节点列表
     */
    public String getBuildUpTypeTree(String parentIDX, 
                                     String partsBuildUpTypeIdx, 
                                     String isVirtual);
    
    /**
     * <li>说明：获取部门下所有班组
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作人员ID
     * @return 部门下所有班组列表
     */
    public String getTeamOfDept(Long operatorid);
    
    /**
     * <li>说明：根据班组ID查询班组人员
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param orgid 组织机构ID
     * @return 班组人员列表
     */
    public String findTeamEmps(Long orgid);
    
    /**
     * <li>说明：获取专业类型列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 专业类型列表
     */
    public String getProfessionalType(int start, int limit);
    
    /**
     * <li>说明：保存故障现象
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员ID
     * @param jsonData 故障现象Json集合
     * @return 操作成功与否
     * @throws IOException 
     */
    public String savePlaceFaultList(Long operatorid, String jsonData) throws IOException;
    
    /**
     * <li>说明：通过组成位置主键获取故障现象数据
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param buildUpPlaceIdx 组成位置主键
     * @return 故障现象数据
     */
    public String getPlaceFault(String buildUpPlaceIdx);
    
    /**
     * <li>说明：获取故障字典码表数据集合
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param fixPlaceIdx 故障位置主键
     * @param faultName 故障名称
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 故障字典码表数据集合
     */
    public String findfaultList(String fixPlaceIdx, String faultName, int start, int limit);
    
    /**
     * <li>说明：提票录入处理
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员主键
     * @param jsonData 提票信息Json集合
     * @return 操作成功与否
     */
    public String saveFaultNoticeFromGrid(Long operatorid, String jsonData);
    
    /**
     * <li>说明：查询提票调度派工分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询json字符串
     * @param isDispatch 是否派工（true 已派工，false 未派工）
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 提票调度派工分页列表
     */
    public String queryDdpgList(String searchJson, String isDispatch, int start, int limit);
    
    /**
     * <li>说明：获取提票调度派工班组列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @return 提票调度派工班组列表
     */
    public String getFaultDispatcherTeam(long operatorid);
    
    /**
     * <li>说明：提票调度派工
     * <li>创建人：程锐
     * <li>创建日期：2015-7-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者id
     * @param idxs 以,号分隔的提票单Idx字符串
     * @param orgId 组织机构id
     * @param orgname 组织机构名称
     * @param orgseq 组织机构序列
     * @return 操作成功与否
     */
    public String updateForDdpg(long operatorid, String idxs, String orgId, String orgname, String orgseq);
    
    /**
     * <li>说明：查询提票工长派工分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询json字符串
     * @param operatorid 操作者id
     * @param isDispatch 是否派工（true 已派工，false 未派工）
     * @param start 查询开始页
     * @param limit 查询每页结果数量     
     * @return 提票工长派工分页列表
     */
    public String queryGzpgList(String searchJson, long operatorid, boolean isDispatch, int start, int limit);
    
    /**
     * <li>说明：提票工长派工
     * <li>创建人：程锐
     * <li>创建日期：2015-7-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者id
     * @param idxs 以,号分隔的提票单Idx字符串
     * @param empids 以,号分隔的人员id字符串
     * @return 操作成功与否
     */
    public String updateForGzpg(long operatorid, String idxs, String empids);
    
    /**
     * <li>说明：查询提票处理分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询json字符串
     * @param operatorid 操作者id
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 提票处理分页列表
     */
    public String queryHandleList(String searchJson, long operatorid, int start, int limit);
    
    /**
     * <li>说明：获取提票信息
     * <li>创建人：程锐
     * <li>创建日期：2015-7-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param faultIdx 提票idx
     * @return 提票信息
     */
    public String getFaultInfo(String faultIdx);
    
    /**
     * <li>说明：获取故障处理方法分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 故障处理方法分页列表
     */
    public String findFaultMethod(int start, int limit);
    
    /**
     * <li>说明：根据提票单idx获取需要指派的提票质量检查项列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param faultIDX 提票单IDX
     * @return 需要指派的提票质量检查项列表
     */
    public String getIsAssignCheckItems(String faultIDX);
    
    /**
     * <li>说明：获取提票处理其他作业人员列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param faultIDX 提票单IDX
     * @param operatorid 操作者ID
     * @return 提票处理其他作业人员列表
     */
    public String getOtherWorkerByTP(String faultIDX, long operatorid);
    
    /**
     * <li>说明：销票
     * <li>创建人：程锐
     * <li>创建日期：2015-7-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param tpData 提票实体JSON字符串
     * @param qcResults 提票质检项列表：[{ checkItemCode 质检项编码 qcEmpID 质检人员id }]
     * @param operatorid 操作者ID
     * @return 操作成功与否
     */
    public String updateForHandle(String tpData, String qcResults, long operatorid);
    
    /**
     * <li>说明：获取提票质量检验分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件Json字符串
     * @param mode 抽检/必检（1/2）
     * @param operatorid 操作者ID
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 提票质量检验分页列表
     */
    public String queryFaultQCList(String searchJson, String mode, long operatorid, int start, int limit);
    
    /**
     * <li>说明：完成提票质量检验项
     * <li>创建人：程锐
     * <li>创建日期：2015-7-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param qcResults 提票质检项列表JSON字符串：[{ idx 质检项IDX qcEmpID 质检人编码 qcEmpName 质检人员名称 qcTime 质检时间 remarks 备注 }]
     * @param operatorid 操作者id
     * @return 操作成功与否
     */
    public String updateFinishQCResult(String qcResults, long operatorid) throws IOException;
    
    /**
     * <li>说明：根据查询条件查询提票
     * <li>创建人：谭诚
     * <li>创建日期：2013-9-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件
     * @param start 开始行
     * @param limit 每页显示量
     * @return String
     */
    public String searchFaultByParam(String searchJson, int start, int limit);
    
    /**
     * <li>方法说明：获取段机构树 
     * <li>方法名称：getSegmentOrgTree
     * <li>@param orgid
     * <li>@param operatorid
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-9-24 下午03:47:02
     * <li>修改人：
     * <li>修改内容：
     * @throws IOException 
     */
    public String getSegmentOrgTree(Long orgid, Long operatorid) throws IOException;
    /**
     * <li>说明：获取原因分析
     * <li>创建人：张迪
     * <li>创建日期：2016-8-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 原因分析列表
     */
    public String getReasonAnalysis(String parentIDX);
    /**
     * <li>说明：获取车型车号信息
     * <li>创建人：张迪
     * <li>创建日期：2016-9-1
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 获取车型车号信息列表
     */
    public String getTrainTypeAndTrainNo(String jsonObject);
    /**
     * <li>说明：获取在段机车车型车号，提票未派工数量信息
     * <li>创建人：张迪
     * <li>创建日期：2017-5-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 查询参数
     * @return 车型车号信息列表
     */
    public String getInTrainForNotDispatch(String jsonObject);
    /**
     * <li>说明：删除当前提票人的票活
     * <li>创建人：张迪
     * <li>创建日期：2016-9-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return 错误提示信息
     * @throws IOException
     */
    public String deleteFaultTickets(String jsonObject) throws IOException;
    
    /**
     * <li>说明：指派责任人
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 提票数组
     * @return
     * @throws IOException
     */
    public String assignResponser(String jsonObject) throws IOException;
}
