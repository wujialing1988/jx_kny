package com.yunda.jx.base.jcgy.manager;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.base.jcgy.entity.Duty;
/**
 * TODO 未见使用，J_JCGY_DUTY表也被删除，待确定无用后删除
 * <li>标题: 机车检修管理信息系统
 * <li>说明：Duty业务类,责任划分
 * <li>创建人：王治龙
 * <li>创建日期：2012-11-02
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="dutyManager")
public class DutyManager extends JXBaseManager<Duty, Duty>{

}