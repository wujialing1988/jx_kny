/**
 * 
 */
package com.yunda.webservice.partsbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType;
import com.yunda.jx.pjwz.partsBase.partstype.manager.PartsTypeManager;
import com.yunda.webservice.common.entity.OperateReturnMessage;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：配件规格型号接口
 * <li>http://localhost:8080/CoreFrame/ydservices/PBPartsTypeService?wsdl
 * <li>创建人： 何涛
 * <li>创建日期： 2015-1-24 上午10:44:19
 * <li>修改人: 何涛
 * <li>修改日期：2016-03-31
 * <li>修改内容：优化代码
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="pbPartsTypeWS")
public class PBPartsTypeService implements IPBPartsTypeService {
	
	@Resource
	private PartsTypeManager partsTypeManager;

	/**
	 * <li>说明：配件规格型号分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-24
	 * <li>修改人: 何涛
	 * <li>修改日期：2016-03-28
	 * <li>修改内容：修改不规范的异常处理（测试通过）
	 * 
	 * @param jsonObject {
			entityJson: {
				matCode:"TGZS500.221",          
				status:1
			},
			operatorId: 800109,
			repairOrgID:106,
			trainTypeIDX:"207G",
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
	public String findPageList(String jsonObject) throws IOException {
		// 解析查询条件JSON实体
		JSONObject jo = JSONObject.parseObject(jsonObject);
		
		PartsType entity = JSONUtil.read(jo.getString("entityJson"), PartsType.class);
		
		// 查询开始索引
		int start = jo.getIntValue("start");
		// 查询条数
		int limit = jo.getIntValue("limit");
		start = limit * (start - 1);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("From PartsType Where recordStatus = 0");
		
		if (null != entity) {
			// 查询条件 - 物料编码（既匹配物料编码，又匹配规格型号，还匹配配件名称）
			if (!StringUtil.isNullOrBlank(entity.getMatCode())) {
                String mc = entity.getMatCode().toUpperCase();
                sb.append(" And (");
                sb.append(" Upper(matCode) Like '%").append(mc).append("%'");
                sb.append(" Or");
                sb.append(" Upper(specificationModel) Like '%").append(mc).append("%'");
                sb.append(" Or");
                sb.append(" Upper(partsName) Like '%").append(mc).append("%'");
                sb.append(" )");
			}
			// 查询条件 - 配件状态 新增：0，启用：1，作废：2
			if (null != entity.getStatus()) {
				sb.append(" And status = '").append(entity.getStatus()).append("'");
			} else {
				sb.append(" And status = '").append(PartsType.STATUS_USE).append("'");
			}
		}
		
		// 查询承修班组为当前班组的配件信息
		Long operatorId = jo.getLong("operatorId");
		if (null != operatorId) {
			// 设置系统用户信息
			SystemContextUtil.setSystemInfoByOperatorId(operatorId);
			sb.append(" And idx In( Select partsTypeIDX From PartsRepairList Where recordStatus = 0 And repairOrgID ='").append(SystemContext.getOmOrganization().getOrgid()).append("')");
		}
		
		// 按承修班组查询
		String repairOrgID = jo.getString("repairOrgID");
		if (!StringUtil.isNullOrBlank(repairOrgID)) {
			sb.append(" And idx In( Select partsTypeIDX From PartsRepairList Where recordStatus = 0 And repairOrgID ='").append(repairOrgID).append("')");
		}
		
		// 按车型查询
		String trainTypeIDX = jo.getString("trainTypeIDX");
		if (!StringUtil.isNullOrBlank(trainTypeIDX)) {
			sb.append(" And idx In( Select partsTypeIDX From TrainTypeToParts Where recordStatus = 0 And trainTypeIDX ='").append(trainTypeIDX).append("')");
		}
		
		String totalHql = "Select Count(*) As rowcount " + sb.substring(sb.indexOf("From"));
		
		Page<PartsType> page = this.partsTypeManager.findPageList(totalHql, sb.toString(), start, limit);
		List<PartsType> list = page.getList();
		if (null == list || list.size() <= 0) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
		}
        List<Map<String, Object>> entityList = new ArrayList<Map<String,Object>>(list.size());
        Map<String, Object> typeJson = null;
		for (PartsType partsType : list) {
		    typeJson = new HashMap<String, Object>();
            typeJson.put("idx", partsType.getIdx());                                    // 配件规格型号主键            
            typeJson.put("specificationModel", partsType.getSpecificationModel());      // 配件规格型号
            typeJson.put("partsName", partsType.getPartsName());                        // 配件名称
            typeJson.put("matCode", partsType.getMatCode());                            // 物料编码
		    entityList.add(typeJson);
		}
		return JSONTools.toJSONList(page.getTotal(), entityList);
	}
	
}
