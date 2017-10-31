package com.yunda.jxpz.systemchar.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jxpz.systemchar.entity.SystemChar;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：SystemChar业务类,特殊字符
 * <li>创建人：程锐
 * <li>创建日期：2013-07-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="systemCharManager")
public class SystemCharManager extends JXBaseManager<SystemChar, SystemChar>{
	
	/**
	 * <li>说明：确定该业务类是否使用查询缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return true使用查询缓存，false不使用
	 */
	@Override
	protected boolean enableCache(){
		return true;
	}	
	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务，验证特殊字符不能重复
	 * <li>创建人：程锐
	 * <li>创建日期：2013-07-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@SuppressWarnings("unchecked")
	@Override
	public String[] validateUpdate(SystemChar entity) throws BusinessException {
        String[] errMsg = null;
        StringBuilder hql = new StringBuilder("from SystemChar where specialChar = '")
                                            .append(entity.getSpecialChar()).append("'");
        if(!StringUtil.isNullOrBlank(entity.getId())){
            hql.append(" and id != '").append(entity.getId()).append("'");            
        }
        List<SystemChar> list = daoUtils.find(hql.toString());
        if(list != null && list.size() > 0){
            errMsg = new String[1];
            errMsg[0] = "特殊字符不能重复！";
            return errMsg;
        } 
		return null;
	}
    /**
     * <li>说明：物理删除记录，根据制定的实体身份标示数组进行批量删除实体
     * <li>创建人：程锐
     * <li>创建日期：2013-07-18
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 实体类主键idx数组
     * @return void
     * @throws BusinessException, NoSuchFieldException
     */ 
    public void delete(Serializable... ids) throws BusinessException, NoSuchFieldException {
        List<SystemChar> entityList = new ArrayList<SystemChar>();
        for (Serializable id : ids) {
            SystemChar systemChar = getModelById(id);
            entityList.add(systemChar);
        }
        this.daoUtils.getHibernateTemplate().deleteAll(entityList);
    }
}