package com.yunda.jx.pjjx.webservice;

import java.io.IOException;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：配件检修提票工单接口
 * <li>创建人： 何涛
 * <li>创建日期： 2015-1-14 上午10:53:01
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IPartsRdpNoticeService extends IService {

	
	/**
	 * <li>说明：施修工单分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-14
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
	public String pageList(String searchEnityJson) throws IOException;
	
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
	public String startUpJob(String jsonObject) throws IOException;
	
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
				"solution":"test by hetao",
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
				"solution":"test by hetao",
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
	 * <li>说明：提票
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
			entityJson:{
				rdpIDX:"8a8284f249abf9720149ac1f0f380005",
				noticeNo: "TPD20150114028",
				noticeDesc: "施修过程处理不规范，请重新检修",
			},
			operatorId: 800109
		}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 * @throws IOException
	 */
	public String submitNotice (String jsonObject) throws IOException;
      /**
     * <li>说明：获取提票类型
     * <li>创建人：张迪
     * <li>创建日期：2016-8-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 提票类型列表
     */
    public String getNoticeTypes();
}
