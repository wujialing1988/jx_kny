package com.yunda.jxpz.equipinfo.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jxpz.equipinfo.entity.DeviceType;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：DeviceType业务类,设备分类
 * <li>创建人：刘晓斌
 * <li>创建日期：2015-01-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="deviceTypeManager")
public class DeviceTypeManager extends JXBaseManager<DeviceType, DeviceType>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：删除实体对象前的验证业务
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2015-01-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 实体对象的idx主键数组
	 * @return 返回删除操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 */	
	@Override
	public String[] validateDelete(Serializable... ids) {
		return null;
	}
	
	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务
	 * <li>创建人：程梅
     * <li>创建日期：2015-01-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 */		
	@SuppressWarnings("unchecked")
	@Override
	public String[] validateUpdate(DeviceType entity) {
        String[] errMsg = null;
        String idx = entity.getIdx();
        StringBuilder hql = new StringBuilder("from DeviceType where deviceTypeCode = '").append(entity.getDeviceTypeCode()).append("'");
        if(!StringUtil.isNullOrBlank(idx)) hql.append(" and idx != '").append(idx).append("'");
        List<DeviceType> list = daoUtils.find(hql.toString());
        if(list != null && list.size() > 0) {
            errMsg = new String[1];
            errMsg[0] = "设备分类编码不能重复！";
            return errMsg;
        }
		return null;
	}
    /**
     * <li>说明：物理删除记录，根据制定的实体身份标示数组进行批量删除实体
     * <li>创建人：程梅
     * <li>创建日期：2015-01-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 实体类主键idx数组
     * @throws NoSuchFieldException
     */ 
    public void delete(Serializable... ids) throws NoSuchFieldException {
        List<DeviceType> entityList = new ArrayList<DeviceType>();
        for (Serializable id : ids) {
        	DeviceType deviceType = getModelById(id);
            entityList.add(deviceType);
        }
        this.daoUtils.getHibernateTemplate().deleteAll(entityList);
    }
    
    /**
     * <li>方法说明： 设备类别树
     * <li>方法名：deviceClassTree
     * @return
     * <li>创建人： 张凡
     * <li>创建日期：2015年11月24日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> deviceClassTree(){
        
        List<DeviceType> list = daoUtils.find("from DeviceType");
        List<Map<String, Object>> nodes = new ArrayList<Map<String,Object>>();
        for(DeviceType dt : list){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", dt.getDeviceTypeCode());
            map.put("text", dt.getDeviceTypeName());
            map.put("desc", dt.getDeviceTypeDesc());
            map.put("leaf", true);
            nodes.add(map);
        }
        return nodes;
    }
}