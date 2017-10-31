package com.yunda.jx.jxgc.processdef.manager;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jxgc.dispatchmanage.entity.WorkStation;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkStation业务类,工位 用于机车检修流程维护时，流程节点关联工位的查询
 * <li>创建人：何涛
 * <li>创建日期：2015-04-17
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="workStationForJobManager")
public class WorkStationForJobManager extends JXBaseManager<WorkStation, WorkStation> {
    
}
