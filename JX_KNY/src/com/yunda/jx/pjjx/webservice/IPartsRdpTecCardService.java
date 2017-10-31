/**
 * 
 */
package com.yunda.jx.pjjx.webservice;

import java.io.IOException;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：检修工艺工单接口
 * <li>创建人： 何涛
 * <li>创建日期： 2015-1-12 下午04:51:51
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IPartsRdpTecCardService extends IService {
	
	/**
	 * <li>说明：施修工单分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
			entityJson: {
				rdpIDX:"8a8284f24ab80704014ab891375a0004",
				status:"03"
			},
			start:1,
			limit:50, 
			orders:[{
				sort: "idx",
				dir: "ASC"
			}]
		}
	 * @return JSON列表（数组）
	 * @throws IOException
	 */
	public String pageList (String jsonObject) throws IOException;
	
	/**
	 * <li>说明：领活
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
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
	 * <li>创建日期：2015-01-12
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
	 * <li>创建日期：2015-01-12
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
				"workEmpID":"101;102"
			},
			operatorId: 800109
		}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 */
	public String saveTemporary (String jsonObject) throws IOException;
	
	/**
	 * <li>说明：销活
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
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
				"workEmpID":"101;102"
			},
			operatorId: 800109
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
	 * <li>创建日期：2015-01-12
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
		ids： ["8a8284f249abf9720149ac1f0f380005", "8a8284f2493af21101493b17bb1d0023 "],
		entityJson：{
			"workEmpName":"张三;李四",
			"workEmpID":"101;102"
		},
		operatorId: 800109
	}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 * @throws IOException
	 */
	public String finishBatchJob (String jsonObject) throws IOException;
	
	/**
	 * <li>说明：工艺工序分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
			entityJson: {
				rdpTecCardIDX:"7C9131712E214B59B6F13A2E4271773F",
				status:"01"
			},
			start:1,
			limit:50, 
			orders:[{
				sort: "idx",
				dir: "ASC"
			}]
		}
	 * @return JSON列表（数组）
	 * @throws IOException
	 */
	public String pageListForWS (String jsonObject) throws IOException;
	
	/**
	 * <li>说明：配件检修工艺工序批量处理
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
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
	public String finishBatchWS (String jsonObject) throws IOException;
	
	/**
	 * <li>说明：所需物料分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
			entityJson: {
				tecCardIDX:" 8a8284f249a2054a0149a2074ed50002",
				matCode:"",
				matDesc:""
			},
			start:1,
			limit:50, 
			orders:[{
				sort: "idx",
				dir: "ASC"
			}]
		}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 */
	public String pageListForMat (String jsonObject)  throws IOException;
	
	/**
	 * <li>说明：新增物料消耗
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-14
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
		 	partsRdpExpendMats: [{ 
				rdpIDX:"8a8284f24ae27846014ae27ca8340002",
				rdpTecCardIDX:"9E2273FF52EB49B790269A14017F7573",
				matCode:"20003",
				matDesc:"20003",
				qty:12,
				unit:"20003",
				price:999.9
			}],
			operatorId: 800109
		}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 */
	public String saveExpendMats (String jsonObject) throws IOException;
	
	/**
	 * <li>说明：更新物料消耗数量，不能更新其他人员的物料消耗
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-14
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
		 	entityJson: [{ 
				idx:"8a8284f24ae61e67014ae6206b7d0001",
				qty:13
			}],
			operatorId: 800109
		}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 */
	public String updateExpendMat (String jsonObject) throws IOException;
	
	/**
	 * <li>说明：删除物料消耗
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-22
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
		 	ids: [
		 		"8a8284f24b1054cf014b1058bc2a0002",
		 		"8a8284f24b1054cf014b1059764c0009"
	 		],
			operatorId: 800109
		}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 */
	public String deleteExpendMats (String jsonObject) throws IOException;
	
	/**
	 * <li>说明：消耗物料分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-21
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
            entityJson: {
                rdpIDX:"8a8284f25146a0d8015146a7670d0003",
                rdpNodeIDX:"3512EA13DFD342CEBD7484B17800F96B",
                status:"03"
            },
            start:1,
            limit:50, 
            orders:[{
                sort: "idx",
                dir: "ASC"
            }]
        }
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 */
	public String pageListForExpendMat (String jsonObject) throws IOException;
	
    /**
     * <li>说明：修改已处理的检修工艺工单
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
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
                "workEmpID":"101;102"
            },
            operatorId: 800109
        }
     * @return 
     * <li>"{'flag':'true','message':'操作成功！'}";
     * <li>"{'flag':'false','message':'操作失败！'}"
     * @throws IOException
     */
    public String updateJob(String jsonObject) throws IOException;
}
