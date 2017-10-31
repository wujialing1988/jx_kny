package com.yunda.jx.jxgc.workplanmanage.manager;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jxgc.workplanmanage.entity.WSGroup;
import com.yunda.jx.jxgc.workplanmanage.entity.WSGroupItem;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WSGroup业务类, 工位组
 * <li>创建人：何涛
 * <li>创建日期：2015-4-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="wSGroupManager")
public class WSGroupManager extends JXBaseManager<WSGroup, WSGroup> {
    
    /** WSGroupItem业务类, 工位组明细 */
    @Resource
    private WSGroupItemManager wGroupItemManager;
    
    /**
     * <li>说明：验证工位组名称不能重复
     * <li>创建人：何涛
     * <li>创建日期：2015-4-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t 被更新（新增）的实体对象
     * @return 错误的验证消息
     */
    @SuppressWarnings("unchecked")
    @Override
    public String[] validateUpdate(WSGroup t) {
        List<WSGroup> all = this.daoUtils.getAll(WSGroup.class);
        for (WSGroup entity : all) {
            if (entity.getIdx().equals(t.getIdx())) {
                continue;
            }
            if (entity.getName().equals(t.getName())) {
                return new String[]{"工位组名称【" +t.getName() + "】已经存在，不能重复添加！"};
            }
        }
        return super.validateUpdate(t);
    }
    
    
    /**
     * <li>级联删除工作组明细
     * <li>创建人：何涛
     * <li>创建日期：2015-4-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 要删除的工作组实体idx主键数组
     */
    @Override
    public void deleteByIds(Serializable... ids) throws BusinessException {
        for (Serializable idx : ids) {
            List<WSGroupItem> models = this.wGroupItemManager.getModels((String)idx);
            String[] items = new String[models.size()];
            int i = 0;
            for (WSGroupItem item : models) {
                items[i++] = item.getIdx();
            }
           this.daoUtils.removeObject(items, WSGroupItem.class);
        }
        super.deleteByIds(ids);
    }
}
