package com.yunda.zb.zbfw.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.zb.zbfw.entity.ZbglJobNodeExtConfigDef;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglJobNodeExtConfigDef业务类,整备扩展配置
 * <li>创建人：程梅
 * <li>创建日期：2016年4月7日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */ 
@Service(value="zbglJobNodeExtConfigDefManager")
public class ZbglJobNodeExtConfigDefManager extends JXBaseManager<ZbglJobNodeExtConfigDef, ZbglJobNodeExtConfigDef>{
    /**
     * <li>保存方法前的更新验证
     * <li>创建人：程梅
     * <li>创建日期：2015-7-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param t 实体对象
     * @return String[] 验证消息数组
     */ 
    @Override
    public String[] validateUpdate(ZbglJobNodeExtConfigDef t) {
        if (null != t.getIdx() && t.getIdx().trim().length() <= 0) {
            t.setIdx(null);
        }
        return super.validateUpdate(t);
    }
    /**
     * 
     * <li>说明：获取同一个整备范围的所有节点扩展配置
     * <li>创建人：程梅
     * <li>创建日期：2015-7-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param zbfwIDX 范围id
     * @return List<ZbglJobNodeExtConfigDef> 扩展配置list
     */
    @SuppressWarnings("unchecked")
    public List<ZbglJobNodeExtConfigDef> getModelsByZbfwIDX(String zbfwIDX) {
        String hql = "From ZbglJobNodeExtConfigDef Where nodeIDX in (select idx From ZbglJobProcessNodeDef Where recordStatus = 0 And zbfwIDX = ?)";
        return this.daoUtils.find(hql, new Object[]{zbfwIDX});
    }
}