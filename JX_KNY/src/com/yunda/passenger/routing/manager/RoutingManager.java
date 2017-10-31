package com.yunda.passenger.routing.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jxpz.coderule.manager.CodeRuleConfigManager;
import com.yunda.passenger.routing.entity.Routing;

/**
 * <li>标题: 肯尼亚综合管理信息系统
 * <li>说明: 交路信息业务类
 * <li>创建人：张迪
 * <li>创建日期：2017-4-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部检修系统项目组
 * @version 1.0
 */
@Service("routingManager")
public class RoutingManager extends JXBaseManager<Routing, Routing> implements IbaseCombo {
	/** CodeRuleConfig业务类,业务编码规则配置 */
    @Resource
    private CodeRuleConfigManager codeRuleConfigManager;
    
   // 业务字典配置的 编组编号生产规则 名称
    private static final String K_P_ROUTING_CODE = "K_P_ROUTING_CODE";
    
	/**
	 * <li>说明：保存时添加业务编码规则
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 基础信息实体类
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	@Override
	public void saveOrUpdate(Routing entity) throws BusinessException, NoSuchFieldException {
		 // 根据业务编码规则自动生成“编组号”
		if(StringUtil.isNullOrBlank(entity.getRoutingCode())){
			entity.setRoutingCode(this.codeRuleConfigManager.makeConfigRule(K_P_ROUTING_CODE));
		}
		super.saveOrUpdate(entity);
	 }
}
