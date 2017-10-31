package com.yunda.webservice.fingerprint.manager;

import java.io.Serializable;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.webservice.fingerprint.entity.Fingerprint;

/**
 * <li>标题: 指纹识别
 * <li>说明：Fingerprint 业务类
 * <li>创建人： 何涛
 * <li>创建日期： 2014-11-10 下午02:56:04
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="fingerprintManager")
public class FingerprintManager extends JXBaseManager<Fingerprint, Fingerprint> {

	@Override
	protected void checkDelete(Serializable id) throws BusinessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void checkUpdate(Fingerprint entity) throws BusinessException {
		// TODO Auto-generated method stub
	}

}
