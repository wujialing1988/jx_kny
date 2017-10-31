/**
 * 
 */
package com.yunda.jx.pjjx.webservice;

import java.io.IOException;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：检修记录工单接口
 * <li>创建人： 何涛
 * <li>创建日期： 2015-1-7 下午02:56:04
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IPartsRdpRecordCardService extends IService {

	/**
	 * <li>说明：施修工单分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-07
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param searchEnityJson {
			entityJson: {
				rdpIDX:"8a8284f24ab80704014ab891375a0004",
				status:"03"
			},
			start:0,
			limit:50, 
			orders:[{
				sort: "idx",
				dir: "ASC"
			}]
		}
	 * @return JSON列表（数组）
	 * @throws IOException
	 */
	public String pageList (String searchEnityJson) throws IOException;
	
	/**
	 * <li>说明：待检验工单分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-07
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param searchEnityJson {
            entityJson:{
                rdpIDX: "772D7520EF5945B695CDB79F2E6CC1E7"
            }, 
            start:0,
            limit:50, 
            orders:[{
                sort: "idx",
                dir: "ASC"
            }],
            operatorId: "10902",
            qcItemNo: "GZJ"
        }
	 * @return JSON列表（数组）
	 * @throws IOException
	 */
	public String findPageListForQC (String searchEnityJson) throws IOException;
	
	/**
	 * <li>说明：领活
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-07
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
			idx: "8a8284f249abf9720149ac1f0f380005",
			operatorId: 800109
		}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 * @throws IOException
	 */
	public String startUpJob (String jsonObject) throws IOException;
	
	/**
	 * <li>说明：批量领活
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-07
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
			ids: ["8a8284f249abf9720149ac1f0f380005", "8a8284f2493af21101493b17bb1d0023 "],
			operatorId: 800109
		}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 */
	public String startUpBatchJob (String jsonObject) throws IOException;
	
	/**
	 * <li>说明：批量撤销领活
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-08
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
			ids: ["8a8284f249abf9720149ac1f0f380005", "8a8284f2493af21101493b17bb1d0023 "],
			operatorId: 800109
		}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 */
	public String giveUpBatchJob (String jsonObject) throws IOException;
	
	/**
	 * <li>说明：暂存
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-08
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
		entityJson : {
			"idx":"025962890654441FAF66909501484778",
			"workStartTime":"2015-01-05 11:38",
			"workEndTime":"2015-01-05 12:00",
			"workEmpName":"张三;李四",
			"workEmpID":"101;102",
			"remarks":"test",
			"qcEmpID":"",
			"qcEmpName":""
		},
		operatorId: 800109,
		qcEmpJson: [{empName:'王谦', empId:109, qcItemNo:'HJ'}]
	}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 */
	public String saveTemporary (String jsonObject) throws IOException;
	
	/**
	 * <li>说明：销活
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-08
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
		entityJson : {
			"idx":"025962890654441FAF66909501484778",
			"workStartTime":"2015-01-05 11:38",
			"workEndTime":"2015-01-05 12:00",
			"workEmpName":"张三;李四",
			"workEmpID":"101;102",
			"qcEmpName":"白宝强",
			"qcEmpID":"35",
			"remarks":"test"
		},
		operatorId: 800109,
		qcEmpJson: [{empName:'王谦', empId:109, qcItemNo:'HJ'}]
	}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 * @throws IOException
	 */
	public String finishJob(String jsonObject) throws IOException;
	
	/**
	 * <li>说明：批量销活
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-08
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
		ids： ["8a8284f249abf9720149ac1f0f380005", "8a8284f2493af21101493b17bb1d0023 "],
		entityJson：{
			"workEmpName":"张三;李四",
			"workEmpID":"101;102",
			"qcEmpName":"白宝强",
			"qcEmpID":"35"
		},
		operatorId: 800109,
		qcEmpJson: [{empName:'王谦', empId:109, qcItemNo:'HJ'}]
	}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 * @throws IOException
	 */
	public String finishBatchJob (String jsonObject) throws IOException;
	
	/**
	 * <li>说明：检修检测项分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param searchEnityJson {
			entityJson: {
				rdpRecordCardIDX:"8a8284f24ab80704014ab891375a0004",
				status:"01"
			},
			start:0,
			limit:50, 
			orders:[{
				sort: "idx",
				dir: "ASC"
			}]
		}
	 * @return JSON列表（数组）
	 * @throws IOException
	 */
	public String pageListForRI (String searchEnityJson) throws IOException;
	
	/**
	 * <li>说明：检测项查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
			rdpRecordRIIDX :"461C0B6A1D8446A1B8456ECCC1DBBDFF"
		}
	 * @return JSON列表（数组）
	 * @throws IOException
	 */
	public String findListForDI (String jsonObject) throws IOException;
	
	/**
	 * <li>说明：检修检测项处理，包括“提交”和“暂存
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
			partsRdpRecordRI:{
				idx:"E1AC1965F5F647C3A98F1C261366F5F5",
				status:"01",
				repairResult:"合格",
				remarks:"备注"
			},
			partsRdpRecordDIs:[{
				idx:"9069B8786F7842B5BC9DC6E0023214CE",
				dataItemResult:"合格"
			}],
			isTemporary:true,			// 如果是暂存则设置为true,否则设置为false
      		empId:109
		}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 * @throws IOException
	 */
	public String savePartsRdpRecordRI (String jsonObject) throws IOException;
	
	/**
	 * <li>说明：获取记录工单指派的质量检查人员
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-20
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
	 		rdpRecordCardIDX:"00577E4CDC2641BAA824508FD65919F5"
		}
	 * @return JSON列表（数组）
	 * @throws IOException
	 */
	public String getQCEmpsForAssign(String jsonObject) throws IOException;
    
    /**
     * <li>说明：获取检修检测结果的数据字典项
     * <li>创建人：何涛
     * <li>创建日期：2015-2-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return JSON数组 ["合格", "良好"]
     * @throws IOException
     */
    public String getDicForRIRepairResult() throws IOException;
    
    /**
     * <li>说明：修改已处理的检修记录单实体
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
        entityJson : {
            "idx":"025962890654441FAF66909501484778",
            "workStartTime":"2015-01-05 11:38",
            "workEndTime":"2015-01-05 12:00",
            "workEmpName":"张三;李四",
            "workEmpID":"101;102"
            "remarks":"test"
        },
        operatorId: 800109
        }
     * @return 操作成功与否
     * @throws IOException
     */
    public String updateJob(String jsonObject) throws IOException;
    
    /**
     * <li>说明：获取配件检修检测项及数据项列表
     * <li>创建人：程锐
     * <li>创建日期：2016-2-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
                            rdpRecordCardIDX:"8a8284f249abf9720149ac1f0f380005"
                        }
     * @return 配件检修检测项及数据项列表
     * @throws IOException
     */
    public String getListForRIAndDI(String jsonObject) throws IOException;
    
    /**
     * <li>说明：配件检修检测项及数据项列表
     * <li>创建人：程锐
     * <li>创建日期：2016-2-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
                    entityJson: {
                    "idx":"025962890654441FAF66909501484778",
                    "workStartTime":"2015-01-05 11:38",
                    "workEndTime":"2015-01-05 12:00",
                    "workEmpName":"张三;李四",
                    "workEmpID":"101;102",
                    "remarks":"test"
                    },
                    operatorId: 800109,
                    qcEmpJson: [{empName: "王谦", empId:109, qcItemNo: "HJ"}],
                        riAndDi:[{
                                    idx:"E1AC1965F5F647C3A98F1C261366F5F5",
                                    status:"01",
                                    repairResult:"合格",
                                    remarks:"备注",           
                                    diList:[{
                                            idx:"9069B8786F7842B5BC9DC6E0023214CE",
                                            dataItemResult:"合格"
                                     }]
                            }],
                            isTemporary:true
                }
     * @return 操作成功与否
     * @throws IOException
     */
    public String batchCompleteJob(String jsonObject) throws IOException;
    
    /**
     * <li>说明：修改已处理的配件记录单
     * <li>创建人：程锐
     * <li>创建日期：2016-2-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
                entityJson: {
                "idx":"025962890654441FAF66909501484778",
                "workStartTime":"2015-01-05 11:38",
                "workEndTime":"2015-01-05 12:00",
                "workEmpName":"张三;李四",
                "workEmpID":"101;102",
                "remarks":"test"
                },
                operatorId: 800109,
                    riAndDi:[{
                                idx:"E1AC1965F5F647C3A98F1C261366F5F5",
                                status:"01",
                                repairResult:"合格",
                                remarks:"备注",           
                                diList:[{
                                        idx:"9069B8786F7842B5BC9DC6E0023214CE",
                                        dataItemResult:"合格"
                                 }]
                        }]
            }
     * @return 操作成功与否
     * @throws IOException
     */
    public String batchUpdateJob(String jsonObject) throws IOException;
    
    /**
     * <li>说明：调用接口方法同步可视化获取到的数据，并更新对应的检测项结果值
     * <li>创建人：林欢
     * <li>创建日期：2016-6-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 传入参数
     * {
     *  partsID:"9999",//配件二维码
     *  startTime:"2015-01-05 12:00 999",//配件检修实际开始时间，精确到毫秒
     *  endTime:"2015-01-05 12:00 999",//配件检修实际结束时间，精确到毫秒
     *  rdpRecordCardRdpIDX:"2323asdfsf"//获取配件检修单idx
     * }
     * @return 操作成功与否
     * @throws IOException
     */
    public String synPartsCheckItemDataAndSavePartsRdpRecordDI(String jsonObject) throws IOException;
}
