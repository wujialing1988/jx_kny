
package com.yunda.jx.jxgc.buildupmanage.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jxgc.buildupmanage.entity.FixBuildUpType;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：FixBuildUpType业务类,可安装组成型号
 * <li>创建人：程锐
 * <li>创建日期：2012-10-24
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "fixBuildUpTypeManager")
public class FixBuildUpTypeManager extends JXBaseManager<FixBuildUpType, FixBuildUpType> {
    /**
     * 
     * <li>说明：设置缺省
     * <li>创建人：程锐
     * <li>创建日期：2012-11-14
     * <li>修改人：程锐
     * <li>修改日期：2013-2-27
     * <li>修改内容：设置缺省前将同一安装位置的可安装组成型号记录设为非缺省
     * @param id 可安装组成型号主键
     * @return void
     * @throws BusinessException, NoSuchFieldException
     */
    public void setIsDefault(FixBuildUpType fixBuildUpType) throws BusinessException, NoSuchFieldException {
        //将同一安装位置的可安装组成型号记录设为非缺省
        List<FixBuildUpType> list = getFixBuildList(fixBuildUpType.getFixPlaceIdx(), fixBuildUpType.getBuildUpTypeIdx());
        if(list != null && list.size() > 0){
            for(FixBuildUpType fBuildUpType: list){
                fBuildUpType.setIsDefault(FixBuildUpType.NODEFAULT);
                saveOrUpdate(fBuildUpType);
            }
        }
        //设置缺省
        fixBuildUpType.setIsDefault(FixBuildUpType.DEFAULT);        
        saveOrUpdate(fixBuildUpType);
        
    }
    /**
     * 
     * <li>说明：判断该位置节点是否有可安装组成型号
     * <li>创建人：程锐
     * <li>创建日期：2012-11-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param fixPlaceIdx 安装位置节点
     * @return boolean 有可安装组成返回true，没有返回false
     */
    public boolean hasFixBuildByFixPlaceIdx(String fixPlaceIdx){
        FixBuildUpType fixBuildUpType = new FixBuildUpType();
        fixBuildUpType.setFixPlaceIdx(fixPlaceIdx);
        List<FixBuildUpType> list = findList(fixBuildUpType, null);
        return list != null && list.size() > 0;
    }
    
    /**
     * 
     * <li>说明：批量保存
     * <li>创建人：程锐
     * <li>创建日期：2012-11-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param list 可安装组成型号对象数组
     * @return void
     * @throws BusinessException, NoSuchFieldException
     */
    public void saveOrUpdateList(FixBuildUpType[] list) throws BusinessException, NoSuchFieldException {
    	if(list == null || list.length < 1) return;
        int i = 0;
        for(FixBuildUpType fixBuildUpType : list) {
            fixBuildUpType.setIsDefault(FixBuildUpType.NODEFAULT);
            //如该位置节点无可安装组成信息则设置第一条新增记录为缺省可安装组成型号
            if (i == 0 && !hasFixBuildByFixPlaceIdx(fixBuildUpType.getFixPlaceIdx())) {
				fixBuildUpType.setIsDefault(FixBuildUpType.DEFAULT);
			}
            saveOrUpdate(fixBuildUpType);
            i++;
        }    
    } 
    /**
     * 
     * <li>说明：根据虚拟位置获取虚拟组成型号主键
     * <li>创建人：程锐
     * <li>创建日期：2013-1-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param fixPlaceIdx 虚拟位置主键
     * @return 虚拟组成型号主键
     * @throws Exception
     */
    public String getVirtualBuildIdx(String fixPlaceIdx){
        String virtualBuildIdx = "";
        FixBuildUpType fixBuildUpType = new FixBuildUpType();
        fixBuildUpType.setFixPlaceIdx(fixPlaceIdx);
        List<FixBuildUpType> fixBuildUpTypeList = findList(fixBuildUpType, null);
        if (fixBuildUpTypeList != null && fixBuildUpTypeList.size() > 0) {
			virtualBuildIdx = fixBuildUpTypeList.get(0).getBuildUpTypeIdx();
		}
        return virtualBuildIdx;
    }
    /**
     * 
     * <li>说明：根据安装位置主键获取缺省可安装组成主键
     * <li>创建人：程锐
     * <li>创建日期：2013-2-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param fixPlaceIdx 安装位置主键
     * @return 缺省可安装组成主键
     */
    public String getDefaultBuildIdx(String fixPlaceIdx){
        String defaultBuildIdx = "";
        String hql = "from FixBuildUpType where fixPlaceIdx ='" + fixPlaceIdx + "' and recordStatus = 0 and isDefault = " + FixBuildUpType.DEFAULT;
        FixBuildUpType fixBuildUpType = (FixBuildUpType)daoUtils.findSingle(hql);
        if (fixBuildUpType != null) {
			defaultBuildIdx = fixBuildUpType.getBuildUpTypeIdx();
		}
        return defaultBuildIdx;
    }
    /**
     * 
     * <li>说明：根据安装位置主键获取可安装组成列表
     * <li>创建人：程锐
     * <li>创建日期：2013-4-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param fixPlaceIdx 安装位置主键 
     * @return List<FixBuildUpType> 可安装组成列表
     */
    @SuppressWarnings("unchecked")
    public List<FixBuildUpType> getFixBuildList(String fixPlaceIdx){
        String hql = "from FixBuildUpType where fixPlaceIdx ='" + fixPlaceIdx + "' and recordStatus = 0 ";
        return daoUtils.find(hql);
    }
    /**
     * 
     * <li>说明：根据安装位置主键获取可安装组成列表
     * <li>创建人：程锐
     * <li>创建日期：2013-4-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param fixPlaceIdx 安装位置主键 
     * @param buildUpTypeIdx 组成主键
     * @return List<FixBuildUpType> 可安装组成列表
     */
    @SuppressWarnings("unchecked")
    public List<FixBuildUpType> getFixBuildList(String fixPlaceIdx, String buildUpTypeIdx){
        String hql = "from FixBuildUpType where fixPlaceIdx ='" + fixPlaceIdx + "' and recordStatus = 0 and buildUpTypeIdx != '" + buildUpTypeIdx + "'";
        return daoUtils.find(hql);
    }
}
