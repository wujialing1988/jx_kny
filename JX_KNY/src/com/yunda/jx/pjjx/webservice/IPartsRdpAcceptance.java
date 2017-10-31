package com.yunda.jx.pjjx.webservice;

/**
 * <li>说明： 配件修竣合格验收接口
 * <li>创建人： 张凡
 * <li>创建日期：2015-10-14
 * <li>成都运达科技股份有限公司
 */
public interface IPartsRdpAcceptance {

	/**
	 * <li>方法说明：合格验收列表
	 * <li>方法名：acceptanceList
	 * @param jsonObject 处理JSON
	 * @return 操作结果
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-14
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public String acceptanceList(String jsonObject);
	
	/**
	 * <li>方法说明：检修记录列表
	 * <li>方法名：repairRecordList
	 * @param jsonObject 处理JSON
	 * @param rdpIdx 配件兑现单主键
	 * @return 操作结果
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-14
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public String repairRecordList(String jsonObject, String rdpIdx);
	
	/* *
	 * <li>方法说明：检修记录处理信息
	 * <li>方法名：repairHandleInfo
	 * @param jsonObject 处理JSON	 
	 * @return 操作结果
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-14
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	//public String repairHandleInfo(String jsonObject);
	
	/**
	 * <li>方法说明：检修/检测项列表
	 * <li>方法名：repairItemList
	 * @param jsonObject 查询JSON
	 * @param rdpRecordCardIDX 记录卡实例主键
	 * @return
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-14
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public String repairItemList(String jsonObject, String rdpRecordCardIDX);
	
	/**
	 * <li>方法说明：检测项结果列表
	 * <li>方法名：repairItemResultList
	 * @param jsonObject 查询JSON
	 * @param rdpRecordRIIDX 记录卡实例主键
	 * @return
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-15
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public String repairItemResultList(String jsonObject, String rdpRecordRIIDX);
	
	/**
	 * <li>方法说明：作业工单列表
	 * <li>方法名：jobOrderList
	 * @param jsonObject 处理JSON
	 * @param rdpIdx 配件兑现单主键
	 * @return 操作结果
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-14
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public String jobOrderList(String jsonObject, String rdpIdx);
	
	/* *
	 * <li>方法说明：作业工单信息
	 * <li>方法名：jobOrderInfo
	 * @param jsonObject 处理JSON
	 * @return 操作结果
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-14
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	//public String jobOrderInfo(String jsonObject);
	
	/**
	 * <li>方法说明：物料消耗列表
	 * <li>方法名：materialConsumptionList
	 * @param jsonObject 处理JSON
	 * @param rdpIdx 配件兑现单主键
	 * @param rdpTecCardIdx 作业工单主键
	 * @return 操作结果
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-14
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public String materialConsumptionList(String jsonObject, String rdpIdx, String rdpTecCardIdx);
	
	/**
	 * <li>方法说明：作业任务列表
	 * <li>方法名：jobTaskList
	 * @param jsonObject 处理JSON
	 * @param rdpTecCardIdx 作业工单主键
	 * @return 操作结果
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-14
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public String jobTaskList(String jsonObject, String rdpTecCardIdx);
	
	/**
	 * <li>方法说明：回修提票列表
	 * <li>方法名：returnRepairFaultList
	 * @param jsonObject 处理JSON
	 * @param rdpIdx 配件兑现单主键
	 * @return 操作结果
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-14
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public String returnRepairFaultList(String jsonObject, String rdpIdx);
	
	/* *
	 * <li>方法说明：回修提票信息
	 * <li>方法名：returnRepairFaultInfo
	 * @param jsonObject 处理JSON
	 * @return 操作结果
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-14
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	//public String returnRepairFaultInfo(String jsonObject);
    
}
