/**
 * 
 */
package com.yunda.webservice.partsbase;

import java.io.IOException;

import com.yunda.jx.pjjx.webservice.IService;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：配件规格型号接口
 * <li>创建人： 何涛
 * <li>创建日期： 2015-1-24 上午10:28:05
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IPBPartsTypeService extends IService {

	/**
	 * <li>说明：配件规格型号分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-24
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param jsonObject {
			entityJson: {
				matCode:"TGZS500.221",
				status:1
			},
			operatorId: 800109,
			repairOrgID:106,
			trainTypeIDX:207G,
			start:1,
			limit:20
		}
	 * @return JSON列表（数组）{
	 		count:100,
	 		list:[{
	 			idx:"100591",
	 			specificationModel:"TGZS499.223.000\SS4",
	 			partsName:"脉冲放大",
	 			matCode:""
	 		}]
	 	}
	 * @throws IOException
	 */
	public String findPageList(String jsonObject) throws IOException;
}
