/**
 * 
 */
package com.yunda.jx.pjjx.webservice;

import java.io.IOException;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：配件检修作业接口
 * <li>创建人： 何涛
 * <li>创建日期： 2015-1-7 下午01:03:01
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IPartsRdpService extends IService {
	
	
    /**
     * <li>说明：配件任务查看
     * <li>创建人：何涛
     * <li>创建日期：2015-01-07
     * <li>修改人: 何涛
     * <li>修改日期：2016-03-21
     * <li>修改内容：1、修改接口参数结构，将查询条件封装到entityJson里；
                   2、增加查询字段：下车车型（unloadTrainType），组合匹配下车车型、下车车号，
                   3、增加查询字段：名称规格型号（specificationModel），组合匹配配件规格型号、配件名称
     * @param jsonObject {
        operatorId: "800109",
        start:0,
        limit:50, 
        orders:[{
            sort: "idx",
            dir: "ASC"
        }], 
        entityJson: {
            partsNo: "25202",
            identificationCode："25202"
            unloadTrainType: "HXD10002",
            specificationModel: "TGZS500.221.000\\SS4",
        }
     }
     * @return Json对象列表（数组）
	 */
	public String findPageList(String jsonObject) throws IOException;
	
	/**
	 * <li>说明：查询与指定用户协作的其他处理人员
	 * <li>创建人： 何涛
	 * <li>创建日期： 2015-01-15
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容： 
	 * 
	 * @param jsonObject {
		operatorId: "800109",
		rdpIDX: "8a8284f24aeb9ab1014aeb9e33fd0001"
	 }
	 * @return jsonObject {
	 	"count":2,
	 	"workerEmpIds":"10046;113",
	 	"workerEmpNames":"杨晓彭;刘蓉"
	 }
	 */
	public String findWorkerList(String jsonObject) throws IOException;
	

	/**
	 * <li>说明：获取质量检查项的基础配置
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-19
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
	 	isAssign:1		// 是否指派(整形) 是：1，否：0
	 }
	 * @return 返回质量检查基础配置的JSON数组
	 * @throws IOException
	 */
	public String getQcItems(String jsonObject) throws IOException;
	
	/**
	 * <li>说明：根据操作员ID获取组织机构名称
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-20
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
	 	operatorId: "800109"
	 }
	 * @return String 组织机构名称/上级组织机构名称，例如：“电检一组/检修设备车间”
	 * @throws IOException
	 */
	public String getOrgNameByOperator(String jsonObject) throws IOException;
    /**
     * 
     * <li>说明：根据操作员id查询当前班组id
     * <li>创建人：程梅
     * <li>创建日期：2015-9-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
        operatorId: "800109"
     }
     * @return String 当前班组id
     * @throws IOException
     */
    public String getOrgIdByOperator(String jsonObject) throws IOException;
	/**
	 * <li>说明：物料消耗情况分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-23
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param searchEnityJson {
	 	entityJson: {
	 		rdpIDX: "8a8284f24b113d15014b1140e64d0012"
	 	},
	 	start:0,
		limit:50, 
		orders:[{
			sort: "idx",
			dir: "ASC"
		}]
	 }
	 * @return Json对象列表（数组）
	 * @throws IOException
	 */
	public String pageListForExpendMat(String searchEnityJson) throws IOException;
	
	/**
	 * <li>说明：配件检修作业 完工 包含【修竣提交】和【无法修复】，无法修复必须填写“检修结果描述”
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-23
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
	 	idx: 8a8284f24b113d15014b1140e64d0012,
	 	repairResultDesc:"无法修复原因",
		status:"0402"
	 }
	 * @return Json对象列表（数组）
	 * @throws IOException
	 */
	public String finishPartsRdp (String jsonObject) throws IOException;
    /**
     * 
     * <li>说明：兑现生成任务单
     * <li>创建人：程梅
     * <li>创建日期：2015-8-28
     * <li>修改人：何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：由于功能变更，该接口已经无用
     * @param jsonObject {"operatorId":"800109","partsAccountIDX":"8a8284644f16cfc1014f1b3d057900a1","wpIdx":"8a8283a84f723434014f7256caed002f"}
     *                     操作员id  配件id   需求单id
     * @param rdpData  {"dutyEmpName":"王谦","dutyEmpID":"109","repairOrgID":"106","repairOrgName":"电检一组","planStartTime":"2015-08-31 13:06"} 负责人、计划开始时间
     * @param workerList [{"workEmpID":"109","workEmpName":"王谦"}] 施修人员信息
     * @return msg 错误提示
     * @throws IOException
     */
   /* public String savePartsRdp(String jsonObject,String rdpData, String workerList) throws IOException ;*/
    /**
     * 
     * <li>说明：查询自修目录配件中检修班组为当前班组的配件规格型号
     * <li>创建人：程梅
     * <li>创建日期：2015-8-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {"operatorId":"800109","entityJson":{"repairOrgID":"106"}}
     * @return Json对象列表（数组）
     * @throws IOException
     */
    public String findRepairListPartsTypeTree(String jsonObject) throws IOException ;
    /**
     * 
     * <li>说明：查询自修目录配件中检修班组为当前班组并且不在检修中的配件
     * <li>创建人：程梅
     * <li>创建日期：2015-8-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject  {"operatorId":"800109","teamOrgId":"106","isNewParts":"旧','新","start":0,"limit":50,"entityJson":{}}
     * @return Json对象列表（数组）
     * @throws IOException
     */
    public String findPartsAccountList(String jsonObject) throws IOException ;
    /**
     * 
     * <li>说明：查询某配件规格型号对应的检修作业流程列表
     * <li>创建人：程梅
     * <li>创建日期：2015-8-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {"operatorId":"800109","partsTypeIdx":"100496","start":0,"limit":50}
     * @return Json对象列表（数组）
     * @throws IOException
     */
    public String findWPList(String jsonObject) throws IOException ;
    /**
     * 
     * <li>说明：查询配件状态列表
     * <li>创建人：程梅
     * <li>创建日期：2015-8-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {"operatorId":"800109","isChecked":"false","dicttypeid":"PJWZ_PARTS_ACCOUNT_STATUS"}
     * @return Json对象列表（数组）
     * @throws IOException
     */
    public String findPartsStatusForTree(String jsonObject) throws IOException ;
    
    /**
     * <li>说明：根据识别码获取当前操作类型
     * <li>创建人：程锐
     * <li>创建日期：2015-9-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * {
     *   identityCode 配件识别码,
     *   operatorId 施修人员ID
     * }
     * @return 操作类型 1配件任务处理 2生成配件检修任务 3启动生产 ""未找到配件
     */
    @Deprecated
    public String getOpTypeByIdentity(String jsonObject);
    
    /**
     * <li>说明：根据配件编号获取当前操作类型
     * <li>创建人：程锐
     * <li>创建日期：2015-9-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * {
     *   partsNo 配件编号,
     *   operatorId 施修人员ID
     * }
     * @return 操作类型 1配件任务处理 2生成配件检修任务 3启动生产 ""未找到配件
     */
    @Deprecated
    public String getOpTypeByPartsNo(String jsonObject);
    
    /**
     * <li>说明：根据“配件信息主键”“配件识别码”或者“配件编号”查询配件当前的可操作类型，例如：1、生成配件检修任务单；2、启动生产
     * <li>创建人：何涛   
     * <li>创建日期：2016-03-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * {
     *    "partsAccountIDX": "8a8284f250e9e6430150ea3931340030",
     *    "identificationCode": "13134345", 
     *    "partsNo": "ox89245" 
     * }
     * @return 操作类型 1:配件任务处理 2:生成配件检修任务 3:启动生产 ""未找到配件
     */
    public String getOperateType(String jsonObject);
    
    /**
     * <li>说明：根据识别码获取配件信息
     * <li>创建人：程锐
     * <li>创建日期：2015-9-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * {
     *   identityCode 配件识别码
     * } 
     * @return 配件信息
     */
    public String getPartsAccountByIdentity(String jsonObject);
    
    /**
     * <li>说明：根据配件编号获取配件信息
     * <li>创建人：程锐
     * <li>创建日期：2015-9-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * {
     *   partsNo 配件编号
     * } 
     * @return 配件信息
     */
    public String getPartsAccountByPartsNo(String jsonObject);
    
    /**
     * <li>说明：配件检修任务单-兑现生成任务单
     * <li>创建人：程锐
     * <li>创建日期：2015-9-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {"operatorId":"800109","partsAccountIDX":"8a8284644f16cfc1014f1b3d057900a1","wpIdx":"8a8283a84f723434014f7256caed002f"} 
     * @return 操作成功与否
     */
    public String saveForPartsRdp (String jsonObject );
    
    /**
     * <li>说明：配件检修任务单-启动生产
     * <li>创建人：程锐
     * <li>创建日期：2015-9-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * {
     *   idx 配件检修任务单IDX
     * }
     * @return 操作成功与否
     */
    public String startPartsRdp (String jsonObject);
    
    /**
     * <li>说明：终止配件作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-10-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * {
     *   idx 配件检修任务单IDX,
     *   operatorId 操作员ID
     * }
     * @return 操作成功与否
     */
    public String terminatePartsRdp (String jsonObject);
    
    /**
     * <li>方法说明：配件合格验收
     * <li>方法名：acceptanceParts
     * @param json 请求处理JSON字符串
     * @return 处理结果
     * <li>创建人： 张凡
     * <li>创建日期：2015-10-16
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public String acceptanceParts(String json);
    
    /**
     * <li>方法说明：查询检修需求单
     * <li>方法名：wpList
     * @param partsTypeIdx 配件型号主键
     * @return
     * <li>创建人： 张凡
     * <li>创建日期：2015-11-3
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public String wpList(String partsTypeIdx);

    /**
     * <li>说明：根据配件识别码获取配件作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 
     * {
     *   identityCode 配件识别码
     * }  
     * @return 配件作业计划
     * @throws IOException
     */
    public String getPartsRdpByIdentity(String jsonObject) throws IOException;
    
    /**
     * <li>说明：根据配件编号获取配件作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 
     * {
     *   partsNo 配件编号
     * }  
     * @return 配件作业计划
     * @throws IOException
     */
    public String getPartsRdpByPartsNo(String jsonObject) throws IOException;
    
    /**
     * <li>说明：生成配件检修任务时-选择工作日历列表
     * <li>创建人：程锐
     * <li>创建日期：2015-11-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 工作日历列表
     */
    public String getCalendarList();
    
    /**
     * <li>说明：根据配件作业计划IDX获取配件作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 
     * {
     *   rdpIDX 配件作业计划IDX
     * }  
     * @return 配件作业计划
     * @throws IOException
     */
    public String getPartsRdpByIDX(String jsonObject) throws IOException;
    
    /**
     * <li>说明：根据配件作业计划主键获取未处理的记录单、工艺工单、提票单数量
     * <li>创建人：程锐
     * <li>创建日期：2016-2-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
     *              rdpIDX 配件作业计划IDX
     *              rdpNodeIDX 配件作业节点IDX
     *        }
     * @return 未处理的记录单、工艺工单、提票单数量
     * {
     *      recordCard: 2, 
     *      tecCard: 4,
     *      notice:  1
     * }
     * @throws IOException
     */
    public String getWCLCountByIDX(String jsonObject) throws IOException;
    /**
     * 
     * <li>说明：车型车号列表【从处理中的机车检修作业计划中获取】
     * <li>创建人：程梅
     * <li>创建日期：2016-3-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return JSON列表（数组）{
            count:100,
            list:[{
                typeID:"207G",                              // 车型主键
                typeName:"SS4G0004",                            // 车型车号
                shortName:"SS4G"                            // 车型简称
            }]
        }
     * @throws IOException
     */
    public String getTrainListFromWorkPlan() throws IOException ;
    
    /**
     * 
     * <li>说明：自修配件清单列表
     * <li>创建人：程梅
     * <li>创建日期：2016-3-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntityJson
     * @return
     * @throws IOException
     */
    public String findPartsRepairListPageList(String searchEntityJson) throws IOException;
    
    /**
     * <li>说明：分页查询，查询当前系统操作人员所在班组可以承修的配件型号
     * <li>创建人：何涛
     * <li>创建日期：2016-3-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容： 
     * @param jsonObject {
        operatorId: "800109",
        start:0,
        limit:50, 
        orders:[{
            sort: "idx",
            dir: "ASC"
        }]
     }
     * @return Json对象列表（数组）
     * @throws IOException
     */
    public String findPageForPartsType(String jsonObject) throws IOException;
    
    /**
     * <li>说明：根据配件检修计划idx同步更新可视化系统检测项数据
     * <li>创建人：林欢
     * <li>创建日期：2016-6-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象 
     * @return 返回提示信息
     */
    public String synPartsCheckItemDataAndSavePartsRdpRecordDI(String jsonObject);
}
