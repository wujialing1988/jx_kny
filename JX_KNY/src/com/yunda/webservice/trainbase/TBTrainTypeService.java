/**
 * 
 */
package com.yunda.webservice.trainbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.jx.base.jcgy.entity.TrainType;
import com.yunda.jx.base.jcgy.entity.XC;
import com.yunda.jx.base.jcgy.manager.TrainTypeManager;
import com.yunda.jx.base.jcgy.manager.XCManager;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.webservice.IService;
import com.yunda.webservice.common.entity.OperateReturnMessage;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：车型车号接口 
 * <li>http://localhost:8080/CoreFrame/ydservices/TBTrainTypeService?wsdl
 * <li>创建人：何涛
 * <li>创建日期：2015-1-24 下午02:09:10
 * <li>修改人: 何涛
 * <li>修改日期：2016-03-31
 * <li>修改内容：优化代码，规范异常处理
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="tbTrainTypeWS")
public class TBTrainTypeService implements ITBTrainTypeService {

    /** TrainType业务类,机车车型编码 */
	@Resource
	private TrainTypeManager trainTypeManager;
	
    /** XC业务类,修程编码 */
	@Resource
	private XCManager xCManager;
	
    /** 机构服务类 */
	@Resource
	private OmOrganizationManager omOrganizationManager;
    
	/**
	 * <li>说明：车型分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-1-24
     * <li>修改人: 何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：优化不规范的异常处理（测试通过）
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
	public String pageListForCX(String searchEntityJson) throws IOException {
		// 解析查询条件JSON实体
		JSONObject jo = JSONObject.parseObject(searchEntityJson);
		
		// 查询开始索引
		int start = jo.getIntValue("start");
		// 查询条数
		int limit = jo.getIntValue("limit");
		start = limit * (start - 1);
		
		StringBuilder sb = new StringBuilder(1024);
		sb.append("From TrainType Where 0 = 0");
		
		// 按承修机构
		Boolean isCx = jo.getBoolean("isCx");
		if (null != isCx && isCx) {
			String orgCode = JXSystemProperties.OVERSEA_ORGCODE;
	        OmOrganization org = this.omOrganizationManager.findOrgForCode(orgCode);
			
			sb.append(" And typeID In( Select trainTypeIDX From UndertakeTrainType Where recordStatus = 0 And undertakeOrgId In (").append("Select orgid From OmOrganization Where orgseq Like '").append(org.getOrgseq()).append("%'))");
		}
		
		String totalHql = "Select Count(*) As rowcount " + sb.substring(sb.indexOf("From"));
		
		Page<TrainType> page = this.trainTypeManager.findPageList(totalHql, sb.toString(), start, limit);
		List<TrainType> list = page.getList();
		
		if (null == list || list.size() <= 0) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(IService.MSG_RESULT_IS_EMPTY));
		}
		List<Map<String, Object>> entityList = new ArrayList<Map<String,Object>>(list.size());
        Map<String, Object> typeJson = null;
		for (TrainType trainType : list) {
		    typeJson = new HashMap<String, Object>();
            typeJson.put("typeID", trainType.getTypeID());           // 车型代码
            typeJson.put("typeName", trainType.getTypeName());       // 车型名称
            typeJson.put("shortName", trainType.getShortName());     // 简称
		    entityList.add(typeJson);
		}
		return JSONTools.toJSONList(page.getTotal(), entityList);
	}
	
	/**
	 * <li>说明：修程分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-1-24
     * <li>修改人: 何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：优化不规范的异常处理（测试通过）
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
	public String pageListForXC(String searchEntityJson) throws IOException {
		StringBuilder sb = new StringBuilder(1024);
		sb.append("From XC Where 0 = 0");
		
		// 解析查询条件JSON实体
		JSONObject jo = JSONObject.parseObject(searchEntityJson);
		
		String entityJson = jo.getString("entityJson");
		if (null != entityJson) {
		    XC entity = JSONUtil.read(jo.getString("entityJson"), XC.class);
		    if (null != entity) {
		        if (!StringUtil.isNullOrBlank(entity.getXcName())) {
		            sb.append(" And xcName Like '%").append(entity.getXcName()).append("%'");
		        }
		    }
		}
		
		// 查询开始索引
		int start = jo.getIntValue("start");
		// 查询条数
		int limit = jo.getIntValue("limit");
		start = limit * (start - 1);
		
		String totalHql = "Select Count(*) As rowcount " + sb.substring(sb.indexOf("From"));
		
		Page<XC> page = this.xCManager.findPageList(totalHql, sb.toString(), start, limit);
		return JSONTools.toJSONList(page);
	}
    
    /**
     * <li>说明：根据车型ID获取车型简称
     * <li>创建人：程锐
     * <li>创建日期：2015-11-19
     * <li>修改人: 何涛
     * <li>修改日期：2016-03-31
     * <li>修改内容：优化不规范的异常处理（测试通过）
     * @param typeID 车型ID
     * @return 车型简称
     * @throws IOException
     */
    public String getTrainNameByID(String typeID) throws IOException {
        TrainType train = trainTypeManager.getModelById(typeID);
        if (train == null) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(IService.MSG_RESULT_IS_EMPTY));
        }
        return train.getShortName();
    }

}
