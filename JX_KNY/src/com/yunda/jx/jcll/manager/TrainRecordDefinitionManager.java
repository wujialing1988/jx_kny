package com.yunda.jx.jcll.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jcll.entity.TrainRecordDefinition;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车履历主要部件定义业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-10-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value="trainRecordDefinitionManager")
public class TrainRecordDefinitionManager extends JXBaseManager<TrainRecordDefinition, TrainRecordDefinition> {
    
    /**
     * <li>说明：通过车型主键获取主要部件定义列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx
     * @return 主要部件定义列表
     */
    public List getTrainRecordDefinitionByTrainTypeIdx(String trainTypeIdx){
        if(StringUtil.isNullOrBlank(trainTypeIdx)){
            return null ;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(" From TrainRecordDefinition where recordStatus = 0 and trainTypeIDX = ?");
        return this.daoUtils.find(sb.toString(), new Object[]{trainTypeIdx});
    }
    
}
