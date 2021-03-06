package com.yunda.webservice.device.manager;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.webservice.device.entity.CallLog;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：CallLog业务类,设备接口-工单维护
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-01-06
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="callLogManager")
public class CallLogManager extends JXBaseManager<CallLog, CallLog>{
}