package com.yunda.jx.pjjx.base.qcitemdefine.manager;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.pjjx.base.qcitemdefine.entity.QCEmpView;
/**
 * <li>标题: 机车配件检修管理信息系统
 * <li>说明: QCEmpView业务类
 * <li>创建人： 何涛
 * <li>创建日期： 2014-11-12 下午01:37:06
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="qCEmpViewManager")
public class QCEmpViewManager extends JXBaseManager<QCEmpView, QCEmpView>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
}