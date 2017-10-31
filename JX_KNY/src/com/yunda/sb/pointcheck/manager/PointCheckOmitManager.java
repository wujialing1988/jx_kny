package com.yunda.sb.pointcheck.manager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.sb.pointcheck.entity.PointCheckOmit;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: PointCheckOmit管理器，数据表：SBJX_POINT_CHECK_OMIT
 * <li>创建人：黄杨
 * <li>创建日期：2017年5月22日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Service(value = "pointCheckOmitManager")
public class PointCheckOmitManager extends JXBaseManager<PointCheckOmit, PointCheckOmit> {
	
	/** 日志工具 */
	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * <li>说明：记录当日设备漏检情况，生成设备漏检记录
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月22日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public void insertPointCheckOmit() {
		logger.info("**** **** **** **** **** **** **** **** **** **** **** **** **** ****");
		logger.info(String.format("设备漏检记录定时器启动，定时器执行时间：%s", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())));
		logger.info("**** **** **** **** **** **** **** **** **** **** **** **** **** ****");
		String sql = SqlMapUtil.getSql(String.format("check%cpoint_check_omit:insertPointCheckOmit", File.separatorChar, File.separatorChar));
		this.daoUtils.executeSql(sql);
	}
	
	/**
	 * <li>说明：删除设备漏检记录
	 * <li>创建人：何涛
	 * <li>创建日期：2017年5月18日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param equipmentIdx 设备idx主键
	 * @param uncheckDate 漏检日期
	 */
	public void deleteOmit(String equipmentIdx, Date uncheckDate) {
		String hql = "From PointCheckOmit Where equipmentIdx = ? And uncheckDate = ?";
		PointCheckOmit t = (PointCheckOmit) this.daoUtils.findSingle(hql, equipmentIdx, uncheckDate);
		if (null == t) {
			return;
		}
		this.daoUtils.getHibernateTemplate().delete(t);
	}

}
