package com.yunda.jx.jxgc.webservice;


/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 作业工单webservice接口
 * <li>创建人：程锐
 * <li>创建日期：2015-7-15
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
public interface IWorkCardService {
    
    /**
     * <li>说明：获取待处理任务
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param operatorid 操作员ID
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 待处理任务列表
     */
    public String waitHandle(String searchJson, Long operatorid, int start, int limit);
    
    /**
     * <li>说明：获取作业工单信息
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 作业工单IDX
     * @param operatorid 操作者ID
     * @param isDisplayInQC 是否在质量检查时显示
     * @return 作业工单信息
     */
    public String workCard(String idx,Long operatorid, Boolean isDisplayInQC);
    
    /**
     * <li>说明：获取作业任务信息
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员主键
     * @param workCardIdx 作业工单主键
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @param mode 标识是未处理还是已处理的作业任务；1表示已处理。
     * @return 作业任务信息
     */
    public String workTask(Long operatorid, String workCardIdx, int start, int limit, String mode);
    
    /**
     * <li>说明：获取数据项
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workTaskIdx 作业任务主键
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 数据项
     */
    public String dataItem(String workTaskIdx, int start, int limit);
    
    /**
     * <li>说明：获取作业结果列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workStepIDX 作业任务主键
     * @return 作业结果列表
     */
    public String getResultList(String workStepIDX);
    
    /**
     * <li>说明：获取系统特殊字符列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 系统特殊字符列表
     */
    public String getSystemCharList(int start, int limit);
    
    /**
     * <li>说明：录入数据项并完成作业任务
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员ID
     * @param data 操作数据数组(字符串数组，字符串格式为“idx值,结果值”，例如：“aa,22”。第一个值是数据项id，第二个值是数据项结果值)
     * @param taskIdx 作业任务主键
     * @param result 检修结果字段值
     * @param remarks 检修备注字段值
     * @return 操作成功与否
     */
    public String inputDataItem(Long operatorid, String[] data, String taskIdx, String result, String remarks);
    
    /**
     * <li>说明：录入单个数据项
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员ID
     * @param idx 数据项主键
     * @param value 数据项录入值
     * @return 操作成功与否
     */
    public String inputSingleDataItem(Long operatorid, String idx, String value);
    
    /**
     * <li>说明：根据作业工单idx获取需要指派的质量检查项列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDXS 作业工单idx，多个idx用,分隔
     * @return 需要指派的质量检查项列表
     */
    public String getIsAssignCheckItems(String workCardIDXS);
    
    /**
     * <li>说明：获取作业工单的其他作业人员列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 单条作业工单idx
     * @param operatorid 操作者ID
     * @return 其他作业人员列表
     */
    public String getOtherWorkerByWorkCard(String workCardIDX, Long operatorid);
    
    /**
     * <li>说明：单条或批量完工
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员ID
     * @param idxs 作业工单主键集合，作业工单主键之间用英文逗号分隔例如：“1,2,3”
     * @param remarks 备注
     * @param qcResults 质检项列表：[{ checkItemCode 质检项编码 qcEmpID 质检人员id }]
     * @param otherWorkerIDS 其他处理人员的id字符串（,分隔的id字符串）
     * @param partsNo 配件编号
     * @return 操作成功与否
     */
    public String finishWorkCard(Long operatorid, String idxs, String remarks, String qcResults, String otherWorkerIDS);
    
    /**
     * <li>说明：作业工单查询条件查询作业计划单
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员ID
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 作业计划单列表
     */
    public String getWorkPlanByWorkQuery(Long operatorid, int start, int limit);
    
    /**
     * <li>说明：作业工单查询条件查询流程节点
     * <li>创建人：程锐
     * <li>创建日期：2015-7-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员ID
     * @param workPlanIDX 作业计划IDX
     * @param parentIdx 父节点主键
     * @return 流程节点列表
     */
    public String getNodeByWorkQuery(Long operatorid, String workPlanIDX, String parentIdx);
    
    /**
     * <li>方法名称：getQCList
     * <li>方法说明：获取质检列表 
     * <li>@param uname
     * <li>@param start
     * <li>@param limit
     * <li>@param mode
     * <li>@param queryString
     *  {
     *      "rdpIDX":""
     *      "workItemName":"",
     *      "taskDepict":""
     *  }
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-5-5 下午06:32:11
     * <li>修改人：程锐
     * <li>修改内容：添加queryString查询字符串按工作项名称、车型、车号、任务描述等模糊查询
     * <li>修改人：王治龙
     * <li>修改内容：封装返回结果 2013年12月24日
     */
    public String getQCList(String uname, int start, int limit, String mode, String queryString);
    
    /**
     * <li>方法名：saveQualityControlCheckInfo
     * <li>
     * @param jsonData 检验信息Json对象 
     * {
     *      checkPersonName
     *      checkPersonIdx operatorid
     *      checkTime
     *      remarks
     * }
     * 
     * <li>
     * @param listBeanData 检验信息Json对象
     * [{
     *      sourceIdx
     *      checkItemCode
     * }]
     * @return
     * <li>返回类型：String
     * <li>说明：三检一验任务确认，保存检验信息
     * <li>创建人：程梅
     * <li>创建日期：2013-5-5
     * <li>修改人： 王治龙
     * <li>修改日期：2013-12-26 封装返回值
     */
    public String saveQualityControlCheckInfo(String jsonData, String listBeanData);
    
    /**
     * <li>方法说明：批量完工三检一验 
     * <li>方法名称：batchAllQC
     * <li>@param operatorid
     * <li>@param remarks
     * <li>@param queryString
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-10-28 下午06:49:58
     * <li>修改人：
     * <li>修改内容：
     */
    public String batchAllQC(Long operatorid, String remarks, String queryString);
    
    /**
     * <li>说明：质量检查-查询生产任务单
     * <li>创建人：张凡
     * <li>创建日期：2013-10-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param start 开始行数
     * @param limit 分页行数
     * @param mode 查询类型
     * @return 流程工单任务兑现单 
     */
    public String getRdpByQC(Long operatorid, int start, int limit, int mode);
    
    /**
     * <li>说明：查询已处理作业工单分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-8-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON
     * @param operatorid 操作员ID
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 已处理作业工单分页列表
     */
    @SuppressWarnings("unchecked")
    public String queryCompleteList(String searchJson, Long operatorid, int start, int limit);
    
    /**
     * <li>说明：作业工单显示检测检修项目List列表
     * <li>创建人：林欢
     * <li>创建日期：2016-4-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员主键
     * @param workCardIdx 作业工单主键
     * @param workTaskIdx 作业任务主键
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 检测检修项目List json对象
     */
    @SuppressWarnings("unchecked")
    public String getWorkTaskAndDetectResultList(Long operatorid, String workCardIdx,String workTaskIdx, int start, int limit);
    
    /**
     * <li>说明：单条提交作业工单
     * <li>创建人：林欢
     * <li>创建日期：2016-4-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param String jsonObject
     * @return 操作成功与否
     */
    public String completeWorkCard(String jsonObject);

    /**
     * <li>说明：单条保存作业工单（暂存）
     * <li>创建人：林欢
     * <li>创建日期：2016-4-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param String jsonObject
     * @return 操作成功与否
     */
    public String saveWorkCard(String jsonObject);
    
    /**
     * <li>说明：获取作业工单指派的质量检查人员
     * <li>创建人：林欢
     * <li>创建日期：2016-4-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容： 
     * @param String jsonObject 作业工单，检验项编码
     * @return 操作成功与否
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    public String getQCEmpsForAssign (String jsonObject);
    
    /**
     * <li>说明：通过作业工单ID返回作业工单的woker值的worker
     * <li>创建人：林欢
     * <li>创建日期：2016-5-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业工单IDX
     * @return String 拼接好的woker
     */
    public String findWorkCardWorkerByIDX(String workCardIDX);
    /**
     * <li>说明：作业工单完工接口（针对机车检修任务处理）
     * <li>创建人：张迪
     * <li>创建日期：2016-9-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象
     * @return 提示信息
     */
    public String completeWorkCardNew(String jsonObject);
}
