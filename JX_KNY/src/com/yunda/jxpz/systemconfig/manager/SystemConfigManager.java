package com.yunda.jxpz.systemconfig.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jxpz.systemconfig.entity.SystemConfig;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: SystemConfig业务类    系统配置项
 * <li>创建人：程梅
 * <li>创建日期：2013-7-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="systemConfigManager")
public class SystemConfigManager extends JXBaseManager<SystemConfig, SystemConfig>{
	
    private static Map<String, String> configs = null; //配置项
	/**
	 * <li>说明：系统配置树
	 * <li>创建人：程梅
	 * <li>创建日期：2013-7-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> tree(String parentKey){
		String hql = "from SystemConfig where parentKey='"+parentKey+"'";
		List<SystemConfig> list = daoUtils.find(hql);
		List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
		if(list !=null && list.size()>0){
			for(SystemConfig config : list){
				HashMap<String, Object> nodeMap = new HashMap<String, Object>();
				boolean isLeaf = findSubConfig(config.getKey());
				nodeMap.put("id", config.getKey());
				nodeMap.put("text", config.getConfigName());
				nodeMap.put("leaf", isLeaf);
				nodeMap.put("record",config);
				children.add(nodeMap);
			}
		}
		return children;
	}
	
	
	/**
	 * 
	 * <li>说明：查询该配置项下有无子配置项
	 * <li>创建人：程梅
	 * <li>创建日期：2013-7-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
    public boolean findSubConfig(String id){
		List list = findSubConfigList(id);
		boolean isLeaf = true;
		if(list!=null && list.size()>0){
			isLeaf = false;
		}
		return isLeaf ;
	}
    
    /**
     * <li>说明：查询子配置项
     * <li>创建人：程锐
     * <li>创建日期：2015-5-25
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentKey 配置项key
     * @return 子配置项列表
     */
    @SuppressWarnings("unchecked")
    public List<SystemConfig> findSubConfigList(String parentKey) {
        String hql = "from SystemConfig where parentKey = ?";
        return daoUtils.find(hql, new Object[] {parentKey});
    }
    
    /**
     * <li>说明：获取系统配置项的值
     * <li>创建人：程锐
     * <li>创建日期：2015-5-25
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentKey 父key
     * @param id 编码
     * @return 系统配置项的值
     */
    public String getKeyValue(String parentKey, String id) {
        SystemConfig config = findSubConfig(parentKey, id);
        if (config == null)
            return "";
        return config.getKeyValue();
    }
    
    /**
     * <li>说明：查询子配置项
     * <li>创建人：程锐
     * <li>创建日期：2015-5-25
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentKey 配置项key
     * @param id 编码
     * @return 子配置项
     */
    @SuppressWarnings("unchecked")
    public SystemConfig findSubConfig(String parentKey, String id) {
        String hql = "from SystemConfig where parentKey = ? and id = ?";
        return (SystemConfig) daoUtils.findSingle(hql, new Object[] {parentKey, id});
    }
	/* 
	 * 
	 * <li>说明：根据键查询键值
	 * <li>创建人：程梅
	 * <li>创建日期：2013-7-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	/*public String getKeyValueByKey(String key){
		String hql = "select keyValue from SystemConfig where key='"+key+"'";
		String keyValue = (String)daoUtils.findSingle(hql);
		return keyValue ;
	}*/
	/**
     * <li>说明：级联删除系统配置项信息
     * <li>创建人：程梅
     * <li>创建日期：2013年7月21日
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     */
	public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
//        daoUtils.removeByIds(ids, entityClass, getModelIdName(entityClass));// 按ID删除系统配置项信息
        for(Serializable id : ids){
	        String sql = " delete from JXPZ_SYSTEM_CONFIG c where c.key in (select t.key from JXPZ_SYSTEM_CONFIG t" +
			" start with t.key='"+id+"'" +
			" connect by t.PARENTKEY = prior t.key)";
			this.daoUtils.executeSql(sql);
			
			removeConfig(id);
		}
    }

	/**
	 * <li>方法说明：移除配置 
	 * <li>方法名称：removeConfig
	 * <li>@param id
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2014-4-25 下午04:39:02
	 * <li>修改人：
	 * <li>修改内容：
	 */
    private void removeConfig(Serializable id) {
        if(!configIsNull()){ //当集合不为NULL时，操作配置MAP
            
            configs.remove(id);  //移除配置项
        }
    }
	
	/**
	 * <li>方法说明：
	 * <li>方法名称：saveOrUpdate
	 * <li>@param t
	 * <li>@throws BusinessException
	 * <li>@throws NoSuchFieldException
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2013-7-29 上午10:24:31
	 * <li>修改人：
	 * <li>修改内容：
	 */
	public void saveOrUpdate(SystemConfig t) throws BusinessException, NoSuchFieldException {
	    
        super.saveOrUpdate(t);
        
        updateConfigsMap(t);
    }

	/**
	 * <li>方法说明：更新配置HashMap 
	 * <li>方法名称：updateConfigsMap
	 * <li>@param t
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2014-4-25 下午04:37:13
	 * <li>修改人：
	 * <li>修改内容：
	 */
    private void updateConfigsMap(SystemConfig t) {
        if(!configIsNull()){ //当集合不为NULL时，操作配置MAP
            
            configs.put(t.getKey(), t.getKeyValue());//新增或修改时更新配置项
        }
    }
	
	
	/**
	 * <li>方法说明：初始化配置项 
	 * <li>方法名称：fillConfig
	 * <li>
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2013-7-29 上午10:06:48
	 * <li>修改人：
	 * <li>修改内容：
	 */
    public void fillConfig(){
	    if(configIsNull()){
    	    List<SystemConfig> config = getConfigs();
    	    configs = new HashMap<String, String>();
    	    putConfig(config);
	    }
	}

    /**
     * <li>方法说明：查询配置数据 
     * <li>方法名称：getConfigs
     * <li>@return
     * <li>return: List<SystemConfig>
     * <li>创建人：张凡
     * <li>创建时间：2014-4-25 下午04:35:06
     * <li>修改人：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    private List<SystemConfig> getConfigs() {
        String hql = "select new SystemConfig(key, keyValue) from SystemConfig";
        List<SystemConfig> config = daoUtils.find(hql);
        return config;
    }

	/**
	 * <li>方法说明：往集合中添加数据 
	 * <li>方法名称：putConfig
	 * <li>@param config
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2014-4-25 下午04:34:26
	 * <li>修改人：
	 * <li>修改内容：
	 */
    private void putConfig(List<SystemConfig> config) {
        for(SystemConfig cfg : config){
            configs.put(cfg.getKey(), cfg.getKeyValue());
        }
    }
	
	/**
	 * <li>方法说明：获取配置集合是否为NULL 
	 * <li>方法名称：configIsNull
	 * <li>@return
	 * <li>return: boolean
	 * <li>创建人：张凡
	 * <li>创建时间：2013-7-29 上午10:14:28
	 * <li>修改人：
	 * <li>修改内容：
	 */
	public static boolean configIsNull(){
	    return  configs == null;
	}
	
	/**
	 * <li>方法说明：获取配置值 
	 * <li>方法名称：getValue
	 * <li>@param key
	 * <li>@return
	 * <li>return: String
	 * <li>创建人：张凡
	 * <li>创建时间：2013-7-29 上午10:14:48
	 * <li>修改人：
	 * <li>修改内容：
	 */
	public static String getValue(String key){
	    
	    return configs.get(key);
	}
}