package com.yunda.frame.yhgl.manager;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.LoginLog;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: LoginLog业务类，登录日志
 * <li>创建人：程梅
 * <li>创建日期：2016-4-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value="loginLogManager")
public class LoginLogManager extends JXBaseManager <LoginLog,LoginLog>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：新增、更新登录日志
     * <li>创建人：程梅
     * <li>创建日期：2016-4-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param log 登录日志实体对象
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    public void saveOrUpdate(LoginLog log) throws BusinessException, NoSuchFieldException {
        //登出时间不为空，说明是退出系统操作，应更新登录日志信息
        if(null != log.getLoginOutTime()){
            String hql = "From LoginLog Where operatorIdx = ? and loginType = ? and loginClient = ? and loginOutTime is null order by loginInTime desc ";
            List<LoginLog> list = this.daoUtils.find(hql, new Object[] { log.getOperatoridx(), log.getLoginType(), log.getLoginClient() });
            if(null != list && list.size() >0 ){
                //获取同操作员、同登录方式、同登录客户端的最新登录日志记录
                LoginLog logV = list.get(0) ;
                logV.setLoginOutTime(log.getLoginOutTime()) ;
                this.daoUtils.getHibernateTemplate().saveOrUpdate(logV);
            }
        }else this.daoUtils.getHibernateTemplate().saveOrUpdate(log);
    }
}
