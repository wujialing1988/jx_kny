package com.yunda.jx.pjjx.base.equipdefine.manager;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.base.equipdefine.entity.EquipCard;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：EquipCard业务类,机务设备工单定义
 * <li>创建人：程梅
 * <li>创建日期：2015-01-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="equipCardManager")
public class EquipCardManager extends JXBaseManager<EquipCard, EquipCard>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务
	 * <li>创建人：程梅
	 * <li>创建日期：2015-01-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@SuppressWarnings("unchecked")
	@Override
	public String[] validateUpdate(EquipCard entity) throws BusinessException {
		String[] errMsg = null;
        String idx = entity.getIdx();
        StringBuilder hql = new StringBuilder("from EquipCard where equipCardNo = '").append(entity.getEquipCardNo()).append("' and recordStatus=0");
        if(!StringUtil.isNullOrBlank(idx)) hql.append(" and idx != '").append(idx).append("'");
        List<EquipCard> list = daoUtils.find(hql.toString());
        if(list != null && list.size() > 0) {
            errMsg = new String[1];
            errMsg[0] = "工单编码【"+entity.getEquipCardNo()+"】已存在！";
            return errMsg;
        }
		return null;
	}
}