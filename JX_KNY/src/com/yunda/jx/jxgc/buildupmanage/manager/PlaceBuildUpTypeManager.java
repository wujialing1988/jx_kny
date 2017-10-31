package com.yunda.jx.jxgc.buildupmanage.manager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.TransformerEntity;
import com.yunda.jx.jxgc.buildupmanage.entity.BuildUpPlace;
import com.yunda.jx.jxgc.buildupmanage.entity.BuildUpToPlace;
import com.yunda.jx.jxgc.buildupmanage.entity.BuildUpType;
import com.yunda.jx.jxgc.buildupmanage.entity.PlaceBuildUpType;
import com.yunda.jxpz.utils.CodeRuleUtil;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PlaceBuildUpType业务类,组成位置关系
 * <li>创建人：程锐
 * <li>创建日期：2013-01-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="placeBuildUpTypeManager")
public class PlaceBuildUpTypeManager extends JXBaseManager<PlaceBuildUpType, PlaceBuildUpType>{
    /** 组成位置业务类 */
	@Resource
    private BuildUpPlaceManager buildUpPlaceManager;
    /** 组成型号和位置关系维护视图实体业务类 */
	@Resource
    private BuildUpToPlaceManager buildUpToPlaceManager;
    /** 组成型号业务类 */
	@Resource
    private BuildUpTypeManager buildUpTypeManager;
	
    /**
     * <li>说明：验证是否可复制标准组成
     *         此机车（配件）无对应标准组成或标准组成无对应组成位置关系、此组成已有对应组成位置关系均不能做复制标准组成操作
     * <li>创建人：程锐
     * <li>创建日期：2013-2-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param typeIDX 机车（配件）类型主键
     * @param type 组成类型
     * @param buildUpTypeIDX 组成主键
     * @return 返回复制标准组成操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
     * @throws BusinessException
     */
    public String[] validateHasDefaultBuild(String typeIDX, int type, String buildUpTypeIDX) throws BusinessException {
        String[] errs = null;
        List<PlaceBuildUpType> trainBuildList = null;
        List<PlaceBuildUpType> partsBuildList = null;
        switch (type) {
            case BuildUpType.TYPE_TRAIN:
                trainBuildList = getTrainBuildList(typeIDX);
                break;
            case BuildUpType.TYPE_PARTS:
                partsBuildList = getPartsBuildList(typeIDX);
                break;
            default:
                errs = new String[1];
                errs[0] = "组成类型值为" + type + "，不是合法范围内的值。";
                return errs;
        }
        if (trainBuildList == null && type == BuildUpType.TYPE_TRAIN) {
			errs = new String[1];
			errs[0] = "此车型标准组成没有设置对应的组成位置关系，不能复制标准组成！";
			return errs;
		}
		if (partsBuildList == null && type == BuildUpType.TYPE_PARTS) {
			errs = new String[1];
			errs[0] = "此配件标准组成没有设置对应的组成位置关系，不能复制标准组成！";
			return errs;
		}
		List<PlaceBuildUpType> list = getListByBuildUpType(buildUpTypeIDX);
		if (list != null && list.size() > 0) {
			errs = new String[1];
			errs[0] = "此组成已设置对应的组成位置关系，不能复制标准组成！";
			return errs;
		}
        return null;
    }
    /**
	 * <li>说明：根据车型主键获取其标准组成的组成位置配置情况
	 * <li>创建人：程锐
	 * <li>创建日期：2013-2-1
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param trainTypeIDX 车型主键
	 * @return List<PlaceBuildUpType> 组成位置关系列表
	 */
    @SuppressWarnings(value = "unchecked")
    public List<PlaceBuildUpType> getTrainBuildList(String trainTypeIDX){        
        BuildUpType buildUpType = buildUpTypeManager.getDefaultBuildByTrain(trainTypeIDX);
        if(buildUpType == null) return null;
        StringBuilder hql = new StringBuilder();
        hql.append(" from PlaceBuildUpType where recordStatus = 0 and buildUpTypeIdx = '");
        hql.append(buildUpType.getIdx());
        hql.append("'");            
        return daoUtils.find(hql.toString());
    }
    /**
     * 
     * <li>说明：根据配件类型主键获取其标准组成的组成位置配置情况
     * <li>创建人：程锐
     * <li>创建日期：2013-2-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsTypeIDX 配件类型主键
     * @return List<PlaceBuildUpType> 组成位置关系列表
     */
    @SuppressWarnings(value = "unchecked")
    public List<PlaceBuildUpType> getPartsBuildList(String partsTypeIDX){        
        BuildUpType buildUpType = buildUpTypeManager.getDefaultBuildByParts(partsTypeIDX);
        if(buildUpType == null) return null;
        StringBuilder hql = new StringBuilder();
        hql.append(" from PlaceBuildUpType where recordStatus = 0 and buildUpTypeIdx = '");
        hql.append(buildUpType.getIdx());
        hql.append("'");
        return daoUtils.find(hql.toString());
    }
    /**
     * 
     * <li>说明：获取该车型的标准组成配置关系
     * <li>创建人：程锐
     * <li>创建日期：2013-2-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX 车型主键
     * @return List<BuildUpToPlace> 组成位置关系视图
     */
    @SuppressWarnings(value = "unchecked")
    public List<BuildUpToPlace> getDefaultTrainBuildList(String trainTypeIDX){        
        BuildUpType buildUpType = buildUpTypeManager.getDefaultBuildByTrain(trainTypeIDX);
        if(buildUpType == null) return null;
        StringBuilder hql = new StringBuilder();
        hql.append(" from BuildUpToPlace where buildUpTypeIdx = '");
        hql.append(buildUpType.getIdx());
        hql.append("'");            
        return daoUtils.find(hql.toString());
    }
    /**
     * 
     * <li>说明：获取该配件的标准组成配置关系
     * <li>创建人：程锐
     * <li>创建日期：2013-2-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsTypeIDX 配件主键
     * @return List<BuildUpToPlace> 组成位置关系视图
     */
    @SuppressWarnings(value = "unchecked")
    public List<BuildUpToPlace> getDefaultPartsBuildList(String partsTypeIDX){        
        BuildUpType buildUpType = buildUpTypeManager.getDefaultBuildByParts(partsTypeIDX);
        if(buildUpType == null) return null;
        StringBuilder hql = new StringBuilder();
        hql.append(" from BuildUpToPlace where buildUpTypeIdx = '");
        hql.append(buildUpType.getIdx());
        hql.append("'");            
        return daoUtils.find(hql.toString());
    }
    /**
     * 
     * <li>说明：根据组成主键获取其对应组成位置关系
     * <li>创建人：程锐
     * <li>创建日期：2013-2-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param buildUpTypeIDX 组成主键
     * @return List<PlaceBuildUpType> 组成位置关系列表     
     */
    @SuppressWarnings(value = "unchecked")
    public List<PlaceBuildUpType> getListByBuildUpType(String buildUpTypeIDX){
        StringBuilder hql = new StringBuilder();
        hql.append(" from PlaceBuildUpType where recordStatus = 0 and buildUpTypeIdx = '")
           .append(buildUpTypeIDX)
           .append("'");
        return daoUtils.find(hql.toString());
    }
    /**
     * 
     * <li>说明：复制标准组成
     * <li>创建人：程锐
     * <li>创建日期：2013-2-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param typeIDX 机车（配件）类型主键
     * @param type 组成类型
     * @param buildUpTypeIDX 组成主键
     * @return void
     * @throws Exception 
     */
    public void saveBuildUpByDefault(String typeIDX, int type, String buildUpTypeIDX) throws Exception {
        List<BuildUpToPlace> trainBuildList = null;
        List<BuildUpToPlace> partsBuildList = null;
        switch (type) {
            case BuildUpType.TYPE_TRAIN:
                trainBuildList = getDefaultTrainBuildList(typeIDX);
                Map<String, String> map = saveList(trainBuildList, buildUpTypeIDX);
                List<PlaceBuildUpType> list = getListByBuildUpType(buildUpTypeIDX);
                updatePlaceType(list, map);
                break;
            case BuildUpType.TYPE_PARTS:
                partsBuildList = getDefaultPartsBuildList(typeIDX);
                Map<String, String> partsMap = saveList(partsBuildList, buildUpTypeIDX);
                List<PlaceBuildUpType> partsList = getListByBuildUpType(buildUpTypeIDX);
                updatePlaceType(partsList, partsMap);
                break;
            default:
        }
    }
    /**
     * 
     * <li>说明：复制标准组成
     * <li>创建人：程锐
     * <li>创建日期：2013-2-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param list 标准组成-组成位置视图关系列表
     * @param buildUpTypeIDX 组成主键
     * @return Map<String,String> 旧结构位置和新结构位置的映射关系
     * @throws Exception 
     */
    public Map<String,String> saveList(List<BuildUpToPlace> list, String buildUpTypeIDX) throws Exception{
        Map<String,String> map = new HashMap<String, String>();
        if(list == null || list.size() < 1) return map;
        for (BuildUpToPlace placeBuildUpType : list) {
			// 复制组成位置关系
			PlaceBuildUpType newPlaceBuild = new PlaceBuildUpType();
			newPlaceBuild.setIdx("");
			newPlaceBuild.setBuildUpPlaceIdx(placeBuildUpType.getBuildUpPlaceIdx());
			newPlaceBuild.setParentIdx(placeBuildUpType.getParentIdx());
			newPlaceBuild.setBuildUpTypeIdx(buildUpTypeIDX);
			BuildUpPlace buildUpPlace = buildUpPlaceManager.getModelById(placeBuildUpType.getBuildUpPlaceIdx());
			// 更新位置及编码全名
			String buildUpPlaceFullName = "";
			String buildUpPlaceFullCode = "";
			if (buildUpPlace != null) {
				String partsBuildUpTypeCode = buildUpTypeManager.getModelById(buildUpTypeIDX).getBuildUpTypeCode();
				String partsBuildUpTypeName = buildUpTypeManager.getModelById(buildUpTypeIDX).getBuildUpTypeName();
				buildUpPlaceFullName = partsBuildUpTypeName
						+ placeBuildUpType.getBuildUpPlaceFullName().substring(placeBuildUpType.getBuildUpPlaceFullName().indexOf("/"));
				buildUpPlaceFullCode = partsBuildUpTypeCode
						+ placeBuildUpType.getBuildUpPlaceFullCode().substring(placeBuildUpType.getBuildUpPlaceFullCode().indexOf("/"));
			}
			newPlaceBuild.setBuildUpPlaceFullCode(buildUpPlaceFullCode);
			newPlaceBuild.setBuildUpPlaceFullName(buildUpPlaceFullName);
			saveOrUpdate(newPlaceBuild);
			// 为非标准组成的新增对应结构位置位置并更新相应结构位置关联的关系
			if (placeBuildUpType.getPlaceType() == BuildUpPlace.TYPE_STRUCTURE) {
				BuildUpPlace newPlace = new BuildUpPlace();
				BeanUtils.copyProperties(newPlace, buildUpPlace);
				newPlace.setIdx("");
				newPlace.setBuildUpPlaceCode(CodeRuleUtil.getRuleCode("JXGC_FIX_PLACE_Code"));
				buildUpPlaceManager.saveOrUpdate(newPlace);
				map.put(placeBuildUpType.getBuildUpPlaceIdx(), newPlace.getIdx());
			}
		}    
        return map;
    }
    /**
	 * <li>说明：因位置主键改变，更新结构位置关联的关系
	 * <li>创建人：程锐
	 * <li>创建日期：2013-2-26
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param list 该组成的组成位置关系列表
	 * @param map 需更新的结构位置主键map数组
	 * @return void
	 * @throws BusinessException, NoSuchFieldException
	 */
    public void updatePlaceType(List<PlaceBuildUpType> list, Map<String, String> map) throws BusinessException, NoSuchFieldException{
    	if(list == null || list.size() < 1) return;
        for(PlaceBuildUpType placeBuildUpType : list){
            //更新组成位置关系中的位置主键字段
            if(map.containsKey(placeBuildUpType.getBuildUpPlaceIdx())){
                placeBuildUpType.setBuildUpPlaceIdx(map.get(placeBuildUpType.getBuildUpPlaceIdx()));
            }
            //更新组成位置关系中的上级位置主键字段
            if(map.containsKey(placeBuildUpType.getParentIdx())){
                placeBuildUpType.setParentIdx(map.get(placeBuildUpType.getParentIdx()));
            }
            saveOrUpdate(placeBuildUpType);
        }    
    }
    /**
     * 
     * <li>说明：保存配件位置和虚拟位置的组成位置关系
     * <li>创建人：程锐
     * <li>创建日期：2013-1-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
   
     * @param placeBuildUpTypeList 组成位置关系列表
     * @return void
     * @throws BusinessException, NoSuchFieldException
     */
    public void saveOrUpdateList(PlaceBuildUpType[] placeBuildUpTypeList) throws BusinessException, NoSuchFieldException{
    	if(placeBuildUpTypeList == null || placeBuildUpTypeList.length < 1) return;
        for (PlaceBuildUpType placeBuildUpType : placeBuildUpTypeList) {
			BuildUpPlace buildUpPlace = buildUpPlaceManager.getModelById(placeBuildUpType.getBuildUpPlaceIdx());
			// 位置及编码全名
			String buildUpPlaceFullName = "";
			String buildUpPlaceFullCode = "";
			if (buildUpPlace != null) {
				buildUpPlaceFullName = buildUpToPlaceManager.getPlaceFullName(buildUpPlace.getBuildUpPlaceName(), 
																			  placeBuildUpType.getParentIdx(),
																			  placeBuildUpType.getBuildUpTypeIdx());
				buildUpPlaceFullCode = buildUpToPlaceManager.getPlaceFullCode(buildUpPlace.getBuildUpPlaceCode(), 
																			  placeBuildUpType.getParentIdx(),
																			  placeBuildUpType.getBuildUpTypeIdx());
			}
			placeBuildUpType.setBuildUpPlaceFullName(buildUpPlaceFullName);
			placeBuildUpType.setBuildUpPlaceFullCode(buildUpPlaceFullCode);
			saveOrUpdate(placeBuildUpType);
		}    
    }
    /**
	 * <li>说明：删除组成位置关系，级联删除与之关联的结构组成位置及下级组成位置关系
	 * <li>创建人：程锐
	 * <li>创建日期：2013-1-21
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 实体类主键idx数组
	 * @return void
	 * @throws BusinessException, NoSuchFieldException
	 */
    public void deleteStructure(Serializable... ids) throws BusinessException, NoSuchFieldException{
        for (Serializable id : ids) {
			PlaceBuildUpType placeBuildUpType = getModelById(id);
			this.buildUpPlaceManager.logicDelete(placeBuildUpType.getBuildUpPlaceIdx());// 删除结构组成位置
			deleteChildPlace(placeBuildUpType.getBuildUpPlaceIdx(), placeBuildUpType.getBuildUpTypeIdx());// 删除下级组成位置关系
		}
        logicDelete(ids);
    }
    /**
     * 
     * <li>说明：删除组成位置关系，如此位置没有关联其他组成则级联删除与之关联的安装位置或虚拟位置、级联删除与之关联的可安装组成型号信息
     * <li>创建人：程锐
     * <li>创建日期：2013-4-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 实体类主键idx数组
     * @return void
     * @throws BusinessException, NoSuchFieldException
     */
    public void deletePlace(Serializable... ids) throws BusinessException, NoSuchFieldException{
        for (Serializable id : ids) {
			PlaceBuildUpType placeBuildUpType = getModelById(id);
			List<PlaceBuildUpType> otherPlaceBuildList = getOtherListByPlace(placeBuildUpType.getBuildUpPlaceIdx(), 
																			 placeBuildUpType.getBuildUpTypeIdx());
			// 如此位置没有关联其他组成，则删除此位置;如关联其他组成，则只删除与本组成的关联关系
			if (otherPlaceBuildList == null || (otherPlaceBuildList != null && otherPlaceBuildList.size() < 1)) {
				buildUpPlaceManager.deletePlace(placeBuildUpType.getBuildUpPlaceIdx());
			}
		}
        logicDelete(ids);
    }
    /**
	 * <li>说明：级联逻辑删除结构位置下所有位置的组成位置关系
	 * <li>创建人：程锐
	 * <li>创建日期：2013-1-30
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param buildUpPlaceIdx 结构位置主键
	 * @param buildUpTypeIdx 组成主键
	 * @return void
	 * @throws BusinessException, NoSuchFieldException
	 */
    public void deleteChildPlace(String buildUpPlaceIdx, String buildUpTypeIdx) throws BusinessException, NoSuchFieldException{
        List<PlaceBuildUpType> childPlaceBuildUpList = getChildPlaceList(buildUpPlaceIdx, buildUpTypeIdx);
        if(childPlaceBuildUpList == null || childPlaceBuildUpList.size() < 1) return;
        for(PlaceBuildUpType placeBuildUpType : childPlaceBuildUpList){
            List<PlaceBuildUpType> otherPlaceBuildList = getOtherListByPlace(placeBuildUpType.getBuildUpPlaceIdx(), buildUpTypeIdx);
            //如此下级位置没有关联其他组成，则删除此下级位置;如关联其他组成，则只删除与本组成的关联关系
            if(otherPlaceBuildList == null || (otherPlaceBuildList != null && otherPlaceBuildList.size() < 1)) {
                buildUpPlaceManager.deletePlace(placeBuildUpType.getBuildUpPlaceIdx());
            }else {
                logicDelete(placeBuildUpType.getIdx());
            }
        }    
    }
    /**
     * 
     * <li>说明：根据位置主键获取其与其他组成的关联关系列表
     * <li>创建人：程锐
     * <li>创建日期：2013-4-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param buildUpPlaceIdx 位置主键
     * @param buildUpTypeIdx 组成主键
     * @return List<PlaceBuildUpType> 组成位置关系列表
     */
    @SuppressWarnings("unchecked")
    public List<PlaceBuildUpType> getOtherListByPlace(String buildUpPlaceIdx, String buildUpTypeIdx){
        String hql = "from PlaceBuildUpType where recordStatus = 0 and buildUpPlaceIdx = '" + buildUpPlaceIdx + "' and buildUpTypeIdx != '" + buildUpTypeIdx + "'" ;
        return daoUtils.find(hql);
    }
    /**
     * 
     * <li>说明：根据位置主键获取其与组成的所有关联关系列表
     * <li>创建人：程锐
     * <li>创建日期：2013-4-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param buildUpPlaceIdx 位置主键
     * @return List<PlaceBuildUpType> 组成位置关系列表
     */
    @SuppressWarnings("unchecked")
    public List<PlaceBuildUpType> getListByPlace(String buildUpPlaceIdx){
        String hql = "from PlaceBuildUpType where recordStatus = 0 and buildUpPlaceIdx = '" + buildUpPlaceIdx + "'" ;
        return daoUtils.find(hql);
    }
    /**
     * 
     * <li>说明：递归获取结构位置下级所有位置信息
     * <li>创建人：程锐
     * <li>创建日期：2013-1-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param buildUpPlaceIdx 位置主键
     * @param buildUpTypeIdx 组成主键
     * @return List<PlaceBuildUpType> 组成位置关系列表
     */
    @SuppressWarnings(value = "unchecked")
    public List<PlaceBuildUpType> getChildPlaceList(final String buildUpPlaceIdx, final String buildUpTypeIdx){
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (List<PlaceBuildUpType>) template.execute(new HibernateCallback() {
            
            public List<PlaceBuildUpType> doInHibernate(Session s) {
                AbstractEntityPersister meta =
                    (AbstractEntityPersister) getDaoUtils().getSessionFactory().getClassMetadata(PlaceBuildUpType.class);
                String query_sql =
                    	"select idx, BuildUp_Place_Idx as buildUpPlaceIdx, "
                        + "BUILDUP_TYPE_IDX as buildUpTypeIdx,  BUILDUPPLACE_FULLCODE as buildUpPlaceFullCode,BUILDUPPLACE_FULLNAME as buildUpPlaceFullName,"
                        + "Parent_Idx as parentIdx, record_status as recordStatus"
                        + " FROM jxgc_place_buildup_type WHERE RECORD_STATUS = 0 " 
                        + " and BUILDUP_TYPE_IDX = '" + buildUpTypeIdx + "' and BUILDUP_PLACE_IDX != '" + buildUpPlaceIdx + "' "
                        + "START WITH BUILDUP_PLACE_IDX = '"
                        + buildUpPlaceIdx
                        + "' CONNECT BY PRIOR BUILDUP_PLACE_IDX = PARENT_IDX";
                Query query = s.createSQLQuery(query_sql).setResultTransformer(new TransformerEntity(PlaceBuildUpType.class, meta));
                return query.list();
            }
        });
    }
}