package com.yunda.jx.jxgc.repairrequirement.manager;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.buildupmanage.entity.BuildUpType;
import com.yunda.jx.jxgc.buildupmanage.manager.BuildUpTypeManager;
import com.yunda.jx.jxgc.repairrequirement.entity.PartsBuildSelect;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配件组成选择控件 业务类
 * <li>创建人：程锐
 * <li>创建日期：2013-12-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "partsBuildSelectManager")
public class PartsBuildSelectManager   extends JXBaseManager<PartsBuildSelect, PartsBuildSelect>{
	/* 组成型号业务类 */
	@Resource
	private BuildUpTypeManager buildUpTypeManager;
	
	/**
	 * 
	 * <li>说明：获取配件组成型号列表
	 * <li>创建人：程锐
	 * <li>创建日期：2013-12-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchEntity 配件组成型号选择实体包装类
	 * @param typeIDX 车型(配件)主键
     * @param type 组成类型 0 机车 1 配件 
	 * @return Page<PartsBuildSelect> 配件组成分页对象
	 */
	public Page<PartsBuildSelect> buildUpTypeList(SearchEntity<PartsBuildSelect> searchEntity, String typeIDX, String type){ 
		BuildUpType buildUpType = null;
		if(!StringUtil.isNullOrBlank(type) && "0".equals(type)){
			buildUpType = buildUpTypeManager.getDefaultBuildByTrain(typeIDX);
		}
		if(!StringUtil.isNullOrBlank(type) && "1".equals(type)){
			buildUpType = buildUpTypeManager.getDefaultBuildByParts(typeIDX);
		}
		if(buildUpType != null){
			searchEntity.getEntity().setFBuildUpTypeIDX(buildUpType.getIdx());
		}
		return findPageList(searchEntity);
    }
}
