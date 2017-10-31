/**
 * 
 */
package com.yunda.webservice.trainbase;

import java.io.IOException;

import com.yunda.jx.pjjx.webservice.IService;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：车型车号接口
 * <li>创建人： 何涛
 * <li>创建日期： 2015-1-24 下午01:34:16
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface ITBTrainTypeService extends IService {
	
	/**
	 * <li>说明：车型分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-1-24
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容： 
	 * @param searchEntityJson {
	 		entityJson:{
	 		},
	 		isCx:true,										// 是否是本单位承修
	 		start:1,	
	 		limit:20
	 }
	 * @return JSON列表（数组）{
	 		count:100,
	 		list:[{
	 			typeID:"207G",								// 车型主键
	 			typeName:"韶山4G",							// 车型名称
	 			shortName:"SS4G"							// 车型简称
	 		}]
	 	}
	 * @throws IOException
	 */
	public String pageListForCX(String searchEntityJson) throws IOException;
	
	/**
	 * <li>说明：修程分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-1-24
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容： 
	 * @param searchEntityJson {
	 		entityJson:{
	 			xcName:""									// 修程名称
	 		},
	 		start:1,
	 		limit:20
	 }
	 * @return JSON列表（数组）{
	 		count:100,
	 		list:[{
	 			xcID:"39",									// 修程编码					
	 			xcName:"半年检",								// 修程名称
	 		}]
	 	}
	 * @throws IOException
	 */
	public String pageListForXC(String searchEntityJson) throws IOException;
    
    /**
     * <li>说明：根据车型ID获取车型简称
     * <li>创建人：程锐
     * <li>创建日期：2015-11-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param typeID 车型ID
     * @return 车型简称
     * @throws IOException
     */
    public String getTrainNameByID(String typeID) throws IOException;

}
