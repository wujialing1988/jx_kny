package com.yunda.jxpz.equipinfo.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jxpz.equipinfo.entity.DeviceInfo;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：DeviceInfo业务类,设备信息
 * <li>创建人：刘晓斌
 * <li>创建日期：2015-01-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="deviceInfoManager")
public class DeviceInfoManager extends JXBaseManager<DeviceInfo, DeviceInfo> implements IbaseCombo {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
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
	public String[] validateUpdate(DeviceInfo entity) {
        String[] errMsg = null;
        String idx = entity.getIdx();
        StringBuilder hql = new StringBuilder("from DeviceInfo where deviceInfoCode = '").append(entity.getDeviceInfoCode()).append("'");
        if(!StringUtil.isNullOrBlank(idx)) hql.append(" and idx != '").append(idx).append("'");
        List<DeviceInfo> list = daoUtils.find(hql.toString());
        if(list != null && list.size() > 0) {
            errMsg = new String[1];
            errMsg[0] = "设备编码不能重复！";
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
        List<DeviceInfo> entityList = new ArrayList<DeviceInfo>();
        for (Serializable id : ids) {
        	DeviceInfo deviceInfo = getModelById(id);
            entityList.add(deviceInfo);
        }
        this.daoUtils.getHibernateTemplate().deleteAll(entityList);
    }
    
    /**
     * <li>说明：重写getBaseComboData，获取下拉列表前台store所需Map对象
     * @param req request
     * @param start 开始行
     * @param limit 每页记录数
     * @return Map<String,Object> 下拉列表前台store所需Map对象
     * @throws Exception
     */
    @Override
    public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit) throws Exception {
        String queryParams = req.getParameter("queryParams");
        Map<?, ?> queryParamsMap = null;
        if (!StringUtil.isNullOrBlank(queryParams)) {
            queryParamsMap = JSONUtil.read(queryParams, Map.class);
        }
        
        String queryValue = StringUtil.nvlTrim(req.getParameter("query"), null);
        StringBuffer hql = new StringBuffer("from DeviceInfo where 1=1");
        if(queryValue != null){
            hql.append(" and (deviceInfoCode like '%").append(queryValue).
            append("%' or deviceTypeName like '%").append(queryValue).append("%')");
        }
        
        if (queryParamsMap != null) {
            Set<?> set = queryParamsMap.entrySet();
            Iterator<?> it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<?,?> entry = (Entry<?, ?>) it.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                if ("".equals(value) == false) {
                    hql.append(" and ").append(key).append("='").append(value).append("'");
                }
            }
        }
        String totalHql = "select count(*) " + hql;
        Page<DeviceInfo> page = findPageList(totalHql, hql.toString(), start, limit);
        
        for(DeviceInfo entity : page.getList()){
            entity.setDeviceInfoDesc(entity.getDeviceInfoCode() + ":" + entity.getDeviceInfoName());
        }
        return page.extjsStore();
    }
}