package com.yunda.jx.pjwz.partsBase.location.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.pjwz.partsBase.location.entity.Location;
import com.yunda.jxpz.datacollect.entity.DataCollectId;
import com.yunda.jxpz.datacollect.manager.DataCollectManager;
import com.yunda.jxpz.phrasedic.entity.PhraseDicItem;
import com.yunda.jxpz.phrasedic.manager.PhraseDicItemManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：wellPartsRegister业务类,良好配件登记
 * <li>创建人：程梅
 * <li>创建日期：2015-10-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="locationManager")
public class LocationManager extends JXBaseManager<Location, Location>{
    
    /** 常用短语业务类*/
    @Resource
    private PhraseDicItemManager phraseDicItemManager ; 
    
    /** 常用收藏夹业务类*/
    @Resource
    private DataCollectManager dataCollectManager ;
    
    private String dictTypeId = "locationcode" ;//字典编码/收藏数据实体---存放位置
    /**
     * 
     * <li>说明：查询存放位置列表
     * <li>创建人：程梅
     * <li>创建日期：2015-10-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param flag 是否被收藏标示
     * @return List<PhraseDicItem> 存放位置列表
     */
    @SuppressWarnings("unchecked")
    public List<PhraseDicItem>  findLocationList(String flag){
        List<PhraseDicItem> list = phraseDicItemManager.getListByDictTypeId(dictTypeId,flag);
        return list ;
    }
    
    /**
     * <li>说明：查询存放位置列表分页
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param flag
     * @return
     */
    @SuppressWarnings("unchecked")
    public Page<PhraseDicItem>  getPageByDictTypeId(SearchEntity<PhraseDicItem> entity,String flag){
        Page<PhraseDicItem> page = phraseDicItemManager.getPageByDictTypeId(entity,dictTypeId,flag);
        return page ;
    }
    
    /**
     * 
     * <li>说明：收藏存放位置
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id 存放位置id
     * @param emp 人员信息
     * @return String[] 错误提示
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public String[] saveLocationCollect(String id, OmEmployee emp) throws BusinessException, NoSuchFieldException {
        DataCollectId collectId = new DataCollectId();
        collectId.setDataIdx(id) ;
        collectId.setDataEntity(dictTypeId) ; //收藏数据实体---存放位置
        collectId.setCollectEmpId(emp.getEmpid()) ;
        String[] errMsg = dataCollectManager.validateSave(collectId);
        if(null != errMsg && errMsg.length > 0){
            return errMsg ;
        }else {
            dataCollectManager.saveDataCollect(collectId) ;
            return null;
        }
    }
    /**
     * 
     * <li>说明：取消收藏存放位置
     * <li>创建人：程梅
     * <li>创建日期：2015-10-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id 存放位置id
     * @param emp 人员信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void deleteLocationCollect(String id, OmEmployee emp) throws BusinessException, NoSuchFieldException {
        StringBuffer sql = new StringBuffer("delete from JXPZ_DATA_COLLECT where DATA_ENTITY= '").append(dictTypeId).
        append("' and DATA_IDX ='").append(id).append("' and COLLECT_EMP_ID='").append(emp.getEmpid()).append("'") ;
        daoUtils.executeSql(sql.toString()) ;
    }
    /**
     * 
     * <li>说明：保存存放位置
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param locationName 存放位置名称
     * @return String[] 错误提示
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public String[] saveLocation(String locationName) throws BusinessException, NoSuchFieldException {
        PhraseDicItem item = new PhraseDicItem();
        item.setDictItemDesc(locationName) ; //短语描述---存放位置名称
        item.setDictTypeId(dictTypeId); //字典编码---存放位置
        String[] errMsg = this.validateSave(item);
        if(null != errMsg && errMsg.length > 0){
            return errMsg ;
        }else {
            phraseDicItemManager.saveOrUpdate(item) ;
            return null;
        }
    }
    /**
     * 
     * <li>说明：保存存放位置前验证
     * <li>创建人：程梅
     * <li>创建日期：2015-10-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param item 需被验证的存放位置信息
     * @return
     */
    public String[] validateSave(PhraseDicItem item) {
        List<String> errMsg = new ArrayList<String>();
        if(StringUtil.isNullOrBlank(item.getDictItemDesc())){
            errMsg.add("存放位置不能为空！");
        }
        if (errMsg.size() > 0) {
            String[] errArray = new String[errMsg.size()];
            errMsg.toArray(errArray);
            return errArray;
        }
        return null;
    }
}