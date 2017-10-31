
package com.yunda.jx.jxgc.buildupmanage.manager;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.buildupmanage.entity.BuildUpPlace;
import com.yunda.jx.jxgc.buildupmanage.entity.BuildUpToPlace;
import com.yunda.jx.jxgc.buildupmanage.entity.FixBuildUpType;
import com.yunda.jx.jxgc.buildupmanage.entity.PlaceBuildUpType;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：BuildUpPlace业务类,组成位置
 * <li>创建人：程锐
 * <li>创建日期：2013-01-15
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "buildUpPlaceManager")
public class BuildUpPlaceManager extends JXBaseManager<BuildUpPlace, BuildUpPlace> {
    /** 组成位置关系业务类 */
	@Resource
    private PlaceBuildUpTypeManager placeBuildUpTypeManager;
    
    /** 组成型号和位置关系维护视图实体业务类 */
	@Resource
    private BuildUpToPlaceManager buildUpToPlaceManager;
    /** 可安装组成型号业务类 */
	@Resource
    private FixBuildUpTypeManager fixBuildUpTypeManager;
    
    /**
     * <li>说明：新增修改保存前的实体对象前的验证业务,验证安装位置编码是否唯一
     * <li>创建人：程锐
     * <li>创建日期：2013-01-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param entity 实体对象
     * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public String[] validateUpdate(BuildUpPlace entity, String buildUpTypeIDX) throws BusinessException {
        String[] errs = null;
        List<BuildUpPlace> list = null;
        String fixPlaceIdx = entity.getIdx();
        // 验证安装位置编码是否唯一
        if (!StringUtil.isNullOrBlank(fixPlaceIdx)) {
            String hql =
                "from BuildUpPlace where recordStatus = 0 and idx != '" + fixPlaceIdx + "' and buildUpPlaceCode = '"
                    + entity.getBuildUpPlaceCode() + "'";
            list = daoUtils.find(hql);
        } else {
            String hql =
                "from BuildUpPlace where recordStatus = 0  and buildUpPlaceCode = '" + entity.getBuildUpPlaceCode()
                    + "'";
            list = daoUtils.find(hql);
        }
        if (list != null && list.size() > 0) {
            errs = new String[1];
            errs[0] = "已存在安装位置编码为【" + entity.getBuildUpPlaceCode() + "】的安装位置信息！";
            return errs;
        }
        //Integer placeType = entity.getPlaceType();
        // 验证安装位置名称在同一车型或同一配件中（同一位置类型中）是否唯一
        // 验证结构位置名称在同一组成中是否唯一
        //2013-3-2取消对安装位置是否唯一的验证
        /*StringBuilder hql = new StringBuilder();
        hql.append("from BuildUpPlace where recordStatus = 0 and buildUpPlaceName = '")
           .append(entity.getBuildUpPlaceName())           
           .append("' and placeType = ")
           .append(placeType);
//           .append(" and placeType != ")
//           .append(BuildUpPlace.TYPE_STRUCTURE);
        if (!StringUtil.isNullOrBlank(fixPlaceIdx)) {                
            hql.append(" and idx != '")
               .append(fixPlaceIdx)
               .append("'");                
        }
        //如是结构位置则判断在同一组成中是否存在相同位置名称
        if(placeType == BuildUpPlace.TYPE_STRUCTURE){
            hql.append(" and idx in (select buildUpPlaceIdx from BuildUpToPlace where buildUpTypeIdx = '")
               .append(buildUpTypeIDX)
               .append("')");
        }
        String trainTypeIDX = entity.getTrainTypeIDX();
        if (!StringUtil.isNullOrBlank(trainTypeIDX)) {
            hql.append(" and trainTypeIDX = '")
               .append(trainTypeIDX)
               .append("'");
            list = daoUtils.find(hql.toString());
        }
        String partsTypeIDX = entity.getPartsTypeIDX();
        if (!StringUtil.isNullOrBlank(partsTypeIDX)) {
            hql.append(" and partsTypeIDX = '")
            .append(partsTypeIDX)
            .append("'");
            list = daoUtils.find(hql.toString());
        }
        if (list != null && list.size() > 0) {
            errs = new String[1];
            switch(placeType){
                case BuildUpPlace.TYPE_STRUCTURE:
                    errs[0] = "该组成已存在结构位置名称为【" + entity.getBuildUpPlaceName() + "】的结构位置信息！";
                break;
                case BuildUpPlace.TYPE_FIX:
                    errs[0] = "已存在配件位置名称为【" + entity.getBuildUpPlaceName() + "】的配件位置信息！";
                break;
                case BuildUpPlace.TYPE_VIRTUAL:
                    errs[0] = "已存在虚拟位置名称为【" + entity.getBuildUpPlaceName() + "】的虚拟位置信息！";
                break;
            }
            return errs;
        }*/
        return null;
    }
    /**
     * 
     * <li>说明：验证是否有位置名称重复
     * <li>创建人：程锐
     * <li>创建日期：2013-3-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 组成位置实体对象
     * @param buildUpTypeIDX 组成主键
     * @return void
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public String[] checkReduplicateName(BuildUpPlace entity, String buildUpTypeIDX) throws BusinessException {
        String[] errs = null;
//      验证安装位置名称在同一车型或同一配件中（同一位置类型中）是否唯一
        // 验证结构位置名称在同一组成中是否唯一
        //2013-3-2取消对安装位置是否唯一的验证
        List<BuildUpPlace> list = null;
        StringBuilder hql = new StringBuilder();
        hql.append("from BuildUpPlace where recordStatus = 0 and buildUpPlaceName = '")
           .append(entity.getBuildUpPlaceName())           
           .append("' and placeType = ")
           .append(entity.getPlaceType());
        if (!StringUtil.isNullOrBlank(entity.getIdx())) {                
            hql.append(" and idx != '")
               .append(entity.getIdx())
               .append("'");                
        }
        //如是结构位置则判断在同一组成中是否存在相同位置名称
        if(entity.getPlaceType() == BuildUpPlace.TYPE_STRUCTURE){
            hql.append(" and idx in (select buildUpPlaceIdx from BuildUpToPlace where buildUpTypeIdx = '")
               .append(buildUpTypeIDX)
               .append("')");
        }
        String trainTypeIDX = entity.getTrainTypeIDX();
        if (!StringUtil.isNullOrBlank(trainTypeIDX)) {
            hql.append(" and trainTypeIDX = '")
               .append(trainTypeIDX)
               .append("'");
            list = daoUtils.find(hql.toString());
        }
        String partsTypeIDX = entity.getPartsTypeIDX();
        if (!StringUtil.isNullOrBlank(partsTypeIDX)) {
            hql.append(" and partsTypeIDX = '")
            .append(partsTypeIDX)
            .append("'");
            list = daoUtils.find(hql.toString());
        }
        if (list != null && list.size() > 0) {
            errs = new String[1];
            switch(entity.getPlaceType()){
                case BuildUpPlace.TYPE_STRUCTURE:
                    errs[0] = "该组成已存在结构位置名称为【" + entity.getBuildUpPlaceName() + "】的结构位置信息！";
                break;
                case BuildUpPlace.TYPE_FIX:
                    errs[0] = "已存在配件位置名称为【" + entity.getBuildUpPlaceName() + "】的配件位置信息！";
                break;
                case BuildUpPlace.TYPE_VIRTUAL:
                    errs[0] = "已存在虚拟位置名称为【" + entity.getBuildUpPlaceName() + "】的虚拟位置信息！";
                break;
            }
            return errs;
        }
        return null;
    }
    /**
     * <li>说明：获取组成位置列表
     * <li>创建人：程锐
     * <li>创建日期：2013-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param searchEntity 组成位置实体对象
     * @param placeTypes 位置类型组合
     * @param buildUpTypeIdx 组成位置主键
     * @return Page<BuildUpPlace> 组成位置分页对象
     * @throws BusinessException
     */
    public Page<BuildUpPlace> placeList(final SearchEntity<BuildUpPlace> searchEntity, String placeTypes,
        String buildUpTypeIdx, String isFix) throws BusinessException {
        StringBuilder hql = new StringBuilder();
        hql.append("from BuildUpPlace where recordStatus = 0");
        BuildUpPlace buildUpPlace = searchEntity.getEntity();
        if (!StringUtil.isNullOrBlank(placeTypes)) {
            hql.append(" and placeType in (").append(placeTypes).append(")");
        }
        if (!StringUtil.isNullOrBlank(buildUpPlace.getTrainTypeIDX())) {
            hql.append(" and trainTypeIDX = '").append(buildUpPlace.getTrainTypeIDX()).append("'");
        }
        if (!StringUtil.isNullOrBlank(buildUpPlace.getPartsTypeIDX())) {
            hql.append(" and partsTypeIDX = '").append(buildUpPlace.getPartsTypeIDX()).append("'");
        }
        if (!StringUtil.isNullOrBlank(isFix)) {
            hql.append(" and placeType in (").append(isFix).append(")");
        }
        if (!StringUtil.isNullOrBlank(buildUpPlace.getBuildUpPlaceName())) {
            hql.append(" and buildUpPlaceName like '%").append(buildUpPlace.getBuildUpPlaceName()).append("%'");
        }
        if (!StringUtil.isNullOrBlank(buildUpPlace.getChartNo())) {
            hql.append(" and chartNo like '%").append(buildUpPlace.getChartNo()).append("%'");
        }
        
        // 如果组成主键不为空，过滤【选择位置列表】数据
        if (!StringUtil.isNullOrBlank(buildUpTypeIdx)) {
            // 同一组成下安装位置唯一
            hql.append(" and idx not in (select buildUpPlaceIdx from BuildUpToPlace where buildUpTypeIdx = '").append(
                buildUpTypeIdx).append("')")
            // 排除虚拟位置下安装与此虚拟位置对应的虚拟组成一致的对应位置，避免死循环，即不能往下安装自身虚拟组成对应的虚拟位置；例如【司机室】和【司机室1】都对应【虚拟组成1】，则【司机室】下不能安装【司机室1】
                .append(" and idx not in (select fixPlaceIdx from FixBuildUpType where buildUpTypeIdx = '").append(
                    buildUpTypeIdx).append("' and recordStatus = 0)");
        }
        Order[] orders = searchEntity.getOrders();
        hql.append(HqlUtil.getOrderHql(orders));
        String totalHql = "select count(*) " + hql.toString();
        return findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    
    /**
     * <li>说明：获取下级位置列表
     * <li>创建人：程锐
     * <li>创建日期：2013-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param start 开始行
     * @param limit 每页记录数
     * @param placeTypes 位置类型组合
     * @param buildUpTypeIDX 组成主键
     * @param parentPlaceIdx 上级位置主键
     * @param orders 排序对象
     * @return Page 组成位置及位置关系分页对象
     * @throws BusinessException
     */
    
    public Page<BuildUpPlace> childPlaceList(int start, int limit, String placeTypes, String buildUpTypeIDX, String parentIdx, Order[] orders)
        throws BusinessException {
        StringBuilder hql = new StringBuilder();
        hql.append(" from BuildUpToPlace ").append(" where buildUpTypeIdx = '").append(buildUpTypeIDX).append("'")
            .append(" and parentIdx = '").append(parentIdx).append("'").append(" and placeType in (")
            .append(placeTypes).append(") ");
        if(orders != null && orders.length > 0){
            hql.append(HqlUtil.getOrderHql(orders));
        }else{
            hql.append(" order by buildUpPlaceSEQ");
        }
        String total_sql = " select count(idx) " + hql.toString();
        return findPageList(total_sql, hql.toString(), start, limit);
    }
    
    /**
     * <li>说明：根据组成主键和上级位置主键获取下级位置列表
     * <li>创建人：程锐
     * <li>创建日期：2013-1-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param buildUpTypeIDX 组成主键
     * @param parentPlaceIdx 上级位置主键
     * @return 下级位置列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<BuildUpToPlace> childPlaceList(String buildUpTypeIDX, String parentIdx) throws BusinessException {
        StringBuilder hql = new StringBuilder();
        hql.append("from BuildUpToPlace where buildUpTypeIdx = '")
           .append(buildUpTypeIDX).append("'")
           .append(" and parentIdx = '")
           .append(parentIdx).append("'")
           .append(" order by buildUpPlaceSEQ");
        return daoUtils.find(hql.toString());
    }
    
    /**
     * <li>说明：保存结构位置及组成位置关系
     * <li>创建人：程锐
     * <li>创建日期：2013-1-17
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param buildUpPlace 结构位置实体对象
     * @param buildUpTypeIDX 组成主键
     * @param parentIdx 上级位置主键
     * @param placeIDX 原位置id用于判断是否是编辑
     * @return void
     * @throws BusinessException, NoSuchFieldException
     */
    public void saveStructure(BuildUpPlace buildUpPlace, String buildUpTypeIDX, String parentIdx, String placeIDX)
        throws BusinessException, NoSuchFieldException {
        //机车主键和配件主键只能存一个
        if(!StringUtil.isNullOrBlank(buildUpPlace.getPartsTypeIDX())){
            buildUpPlace.setTrainTypeIDX("");
        }
        // 保存结构位置
        this.saveOrUpdate(buildUpPlace);
        PlaceBuildUpType placeBuildUpType = new PlaceBuildUpType();
        // 如果为编辑
        if (!StringUtil.isNullOrBlank(placeIDX)) {
            BuildUpToPlace buildUpToPlace = this.buildUpToPlaceManager.findBuildUpToPlace(placeIDX, buildUpTypeIDX);
            if (buildUpToPlace != null) {
                placeBuildUpType = this.placeBuildUpTypeManager.getModelById(buildUpToPlace.getIdx());
                // 位置及编码全名
                String buildUpPlaceFullName =
                    buildUpToPlaceManager.getPlaceFullName(buildUpPlace.getBuildUpPlaceName(), buildUpToPlace
                        .getParentIdx(), buildUpTypeIDX);
                String buildUpPlaceFullCode =
                    buildUpToPlaceManager.getPlaceFullCode(buildUpPlace.getBuildUpPlaceCode(), buildUpToPlace
                        .getParentIdx(), buildUpTypeIDX);
                placeBuildUpType.setBuildUpPlaceFullName(buildUpPlaceFullName);
                placeBuildUpType.setBuildUpPlaceFullCode(buildUpPlaceFullCode);
                this.placeBuildUpTypeManager.saveOrUpdate(placeBuildUpType);
                updateChildPlaceList(buildUpToPlace.getBuildUpPlaceIdx(), buildUpToPlace.getBuildUpTypeIdx());
            }
        } else {
            // 保存组成位置关系
            placeBuildUpType.setBuildUpPlaceIdx(buildUpPlace.getIdx());
            // 位置及编码全名
            String buildUpPlaceFullName =
                buildUpToPlaceManager.getPlaceFullName(buildUpPlace.getBuildUpPlaceName(), parentIdx, buildUpTypeIDX);
            String buildUpPlaceFullCode =
                buildUpToPlaceManager.getPlaceFullCode(buildUpPlace.getBuildUpPlaceCode(), parentIdx, buildUpTypeIDX);
            placeBuildUpType.setBuildUpPlaceFullName(buildUpPlaceFullName);
            placeBuildUpType.setBuildUpPlaceFullCode(buildUpPlaceFullCode);
            placeBuildUpType.setBuildUpTypeIdx(buildUpTypeIDX);
            placeBuildUpType.setParentIdx(parentIdx);
            this.placeBuildUpTypeManager.saveOrUpdate(placeBuildUpType);
        }
    }
    /**
     * 
     * <li>说明：保存安装位置或虚拟位置及组成位置关系
     * <li>创建人：程锐
     * <li>创建日期：2013-4-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param buildUpPlace 位置实体对象
     * @param buildUpTypeIDX 组成主键
     * @param parentIdx 上级位置主键
     * @param placeIDX 原位置id用于判断是否是编辑
     * @return void
     * @throws BusinessException, NoSuchFieldException
     */
    public void savePlace(BuildUpPlace buildUpPlace, String buildUpTypeIDX, String parentIdx, String placeIDX)
        throws BusinessException, NoSuchFieldException {
        //机车主键和配件主键只能存一个
        if(!StringUtil.isNullOrBlank(buildUpPlace.getPartsTypeIDX())){
            buildUpPlace.setTrainTypeIDX("");
        }
        // 保存配件或虚拟位置
        this.saveOrUpdate(buildUpPlace);
        PlaceBuildUpType placeBuildUpType = new PlaceBuildUpType();
        // 如果为编辑
        if (!StringUtil.isNullOrBlank(placeIDX)) {
            BuildUpToPlace buildUpToPlace = this.buildUpToPlaceManager.findBuildUpToPlace(placeIDX, buildUpTypeIDX);
            if (buildUpToPlace != null) {
                placeBuildUpType = this.placeBuildUpTypeManager.getModelById(buildUpToPlace.getIdx());
                // 位置及编码全名
                String buildUpPlaceFullName =
                    buildUpToPlaceManager.getPlaceFullName(buildUpPlace.getBuildUpPlaceName(), buildUpToPlace
                        .getParentIdx(), buildUpTypeIDX);
                String buildUpPlaceFullCode =
                    buildUpToPlaceManager.getPlaceFullCode(buildUpPlace.getBuildUpPlaceCode(), buildUpToPlace
                        .getParentIdx(), buildUpTypeIDX);
                placeBuildUpType.setBuildUpPlaceFullName(buildUpPlaceFullName);
                placeBuildUpType.setBuildUpPlaceFullCode(buildUpPlaceFullCode);
                this.placeBuildUpTypeManager.saveOrUpdate(placeBuildUpType);
//                updateChildPlaceList(buildUpToPlace.getBuildUpPlaceIdx(), buildUpToPlace.getBuildUpTypeIdx());
            }
        } else {
            // 保存组成位置关系
            placeBuildUpType.setBuildUpPlaceIdx(buildUpPlace.getIdx());
            // 位置及编码全名
            String buildUpPlaceFullName =
                buildUpToPlaceManager.getPlaceFullName(buildUpPlace.getBuildUpPlaceName(), parentIdx, buildUpTypeIDX);
            String buildUpPlaceFullCode =
                buildUpToPlaceManager.getPlaceFullCode(buildUpPlace.getBuildUpPlaceCode(), parentIdx, buildUpTypeIDX);
            placeBuildUpType.setBuildUpPlaceFullName(buildUpPlaceFullName);
            placeBuildUpType.setBuildUpPlaceFullCode(buildUpPlaceFullCode);
            placeBuildUpType.setBuildUpTypeIdx(buildUpTypeIDX);
            placeBuildUpType.setParentIdx(parentIdx);
            this.placeBuildUpTypeManager.saveOrUpdate(placeBuildUpType);
        }
    }
    
    /**
     * <li>说明：级联更新下级位置的位置全名和位置编码全名
     * <li>创建人：程锐
     * <li>创建日期：2013-1-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param buildUpPlaceIdx 结构位置主键
     * @param buildUpTypeIdx 组成主键
     * @return void
     * @throws BusinessException, NoSuchFieldException
     */
    public void updateChildPlaceList(String buildUpPlaceIdx, String buildUpTypeIdx) throws BusinessException,
        NoSuchFieldException {
        List<PlaceBuildUpType> childPlaceBuildUpList =
            placeBuildUpTypeManager.getChildPlaceList(buildUpPlaceIdx, buildUpTypeIdx);
        if (childPlaceBuildUpList != null && childPlaceBuildUpList.size() > 0) {
            for (PlaceBuildUpType placeBuildUpType : childPlaceBuildUpList) {
                BuildUpPlace buildUpPlace = getModelById(placeBuildUpType.getBuildUpPlaceIdx());
                // 位置及编码全名
                String buildUpPlaceFullName = "";
                String buildUpPlaceFullCode = "";
                if (buildUpPlace != null) {
                    buildUpPlaceFullName =
                        buildUpToPlaceManager.getPlaceFullName(buildUpPlace.getBuildUpPlaceName(), placeBuildUpType
                            .getParentIdx(), buildUpTypeIdx);
                    buildUpPlaceFullCode =
                        buildUpToPlaceManager.getPlaceFullCode(buildUpPlace.getBuildUpPlaceCode(), placeBuildUpType
                            .getParentIdx(), buildUpTypeIdx);
                }
                placeBuildUpType.setBuildUpPlaceFullName(buildUpPlaceFullName);
                placeBuildUpType.setBuildUpPlaceFullCode(buildUpPlaceFullCode);
                this.placeBuildUpTypeManager.saveOrUpdate(placeBuildUpType);
            }
        }
    }
    /**
     * 
     * <li>说明：逻辑删除安装位置或虚拟位置并级联删除关联的可安装组成型号、删除其关联的组成位置关系
     * <li>创建人：程锐
     * <li>创建日期：2013-4-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 安装位置主键数组
     * @return void
     * @throws BusinessException, NoSuchFieldException
     */
    public void deletePlace(Serializable... ids) throws BusinessException, NoSuchFieldException{
        for(Serializable id : ids){            
            List<FixBuildUpType> fixBuildList = fixBuildUpTypeManager.getFixBuildList(id.toString());//删除安装位置或虚拟位置关联的可安装组成型号
            fixBuildUpTypeManager.logicDelete(fixBuildList);
            List<PlaceBuildUpType> placeBuildList = placeBuildUpTypeManager.getListByPlace(id.toString());//删除安装位置或虚拟位置关联的组成位置关系
            placeBuildUpTypeManager.logicDelete(placeBuildList);
        }
        logicDelete(ids);
    }
    /**
     * 
     * <li>说明：逻辑删除安装位置或虚拟位置并级联删除关联的可安装组成型号、删除其关联的组成位置关系
     * <li>创建人：程锐
     * <li>创建日期：2013-4-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 安装位置主键
     * @return void
     * @throws BusinessException, NoSuchFieldException
     */
    public void deletePlace(Serializable id) throws BusinessException, NoSuchFieldException{
        List<FixBuildUpType> fixBuildList = fixBuildUpTypeManager.getFixBuildList(id.toString());//删除安装位置或虚拟位置关联的可安装组成型号
        fixBuildUpTypeManager.logicDelete(fixBuildList);
        List<PlaceBuildUpType> placeBuildList = placeBuildUpTypeManager.getListByPlace(id.toString());//删除安装位置或虚拟位置关联的组成位置关系
        placeBuildUpTypeManager.logicDelete(placeBuildList);
        logicDelete(id);
    }
}
