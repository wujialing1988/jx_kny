package com.yunda.zb.trainhandover.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.zb.trainhandover.entity.ZbglHoCaseItem;
import com.yunda.zb.trainhandover.entity.ZbglHoModelItem;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：ZbglHoCaseItem业务类,机车交接单项
 * <li>创建人：程梅
 * <li>创建日期：2015-02-07
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "zbglHoCaseItemManager")
public class ZbglHoCaseItemManager extends JXBaseManager<ZbglHoCaseItem, ZbglHoCaseItem> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * 交接项业务类
     */
    @Resource
    private ZbglHoModelItemManager zbglHoModelItemManager;
    
    /**
     * <li>说明：根据机车交接单保存机车交接单项信息
     * <li>创建人：程梅
     * <li>创建日期：2015-2-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param handOverRdpIDX 交接单id
     * @param items 交接项信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveItemsByCaseId(String handOverRdpIDX, ZbglHoCaseItem[] items) throws BusinessException, NoSuchFieldException {
        List<ZbglHoCaseItem> itemList = new ArrayList<ZbglHoCaseItem>();
        ZbglHoCaseItem newItem;
        for (ZbglHoCaseItem item : items) {
            newItem = new ZbglHoCaseItem();
            newItem.setHandOverRdpIDX(handOverRdpIDX);
            newItem.setHandOverItemModelIDX(item.getIdx());
            newItem.setHandOverResultDesc(StringUtil.nvlTrim(item.getHandOverResultDesc(), ""));
            newItem.setHandOverItemStatus(StringUtil.nvlTrim(item.getHandOverItemStatus(), ""));
            
            ZbglHoModelItem model = zbglHoModelItemManager.getModelById(item.getIdx());
            newItem.setHandOverItemName(model.getHandOverItemName());
            newItem.setParentIDX(model.getParentIDX());
            newItem.setParentItemName(zbglHoModelItemManager.getModelById(model.getParentIDX()).getHandOverItemName());
            itemList.add(newItem);
        }
        saveOrUpdate(itemList);
    }
}
