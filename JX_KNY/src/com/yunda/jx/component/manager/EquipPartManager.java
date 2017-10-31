package com.yunda.jx.component.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.component.entity.EquipPart;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：EquipPart业务类,
 * <li>创建人：程锐
 * <li>创建日期：2012-11-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="equipPartManager")
public class EquipPartManager extends JXBaseManager<EquipPart, EquipPart>{
    
    /**
     * <li>说明：通过名称找到EquipPart对象
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partName 名称
     * @return EquipPart 实体
     */
    @SuppressWarnings("unchecked")
    public EquipPart getEquipPartByName(String partName){
        String hql = " From EquipPart t where t.partName = '"+partName+"'" ;
        List<EquipPart> parts = (List<EquipPart>)this.find(hql);
        if(parts != null && parts.size() > 0){
            return parts.get(0);
        }
        return null ;
    }
}