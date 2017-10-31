package com.yunda.jx.wlgl.partsBase.manager;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.wlgl.partsBase.entity.UsedMatDetail;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：UsedMatDetail业务类,常用物料清单明细
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-10-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="usedMatDetailManager")
public class UsedMatDetailManager extends JXBaseManager<UsedMatDetail, UsedMatDetail>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * 
	 * <li>说明：根据常用物料清单id查询明细列表
	 * <li>创建人：程梅
	 * <li>创建日期：2014-10-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public List<UsedMatDetail> getModeList(String usedMatIdx){
		String hql = "From UsedMatDetail where recordStatus=0 and usedMatIdx='"+usedMatIdx+"'";
		List<UsedMatDetail> list = daoUtils.find(hql);
		return list ;
	}
	/**
	 * <li>说明：删除实体对象前的验证业务
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2014-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 实体对象的idx主键数组
	 * @return 返回删除操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */	
	@Override
	public String[] validateDelete(Serializable... ids) throws BusinessException {
		return null;
	}
	
	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2014-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@Override
	public String[] validateUpdate(UsedMatDetail entity) throws BusinessException {
		return null;
	}
}