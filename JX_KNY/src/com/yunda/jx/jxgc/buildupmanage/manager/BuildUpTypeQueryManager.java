package com.yunda.jx.jxgc.buildupmanage.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.buildupmanage.entity.BuildUpPlace;
import com.yunda.jx.jxgc.buildupmanage.entity.BuildUpToPlace;
import com.yunda.jx.jxgc.buildupmanage.entity.BuildUpType;
import com.yunda.jx.jxgc.buildupmanage.entity.FixBuildUpType;
import com.yunda.jx.jxgc.repairrequirement.entity.PartsTypeByBuildSelect;
import com.yunda.jx.jxgc.repairrequirement.manager.PartsTypeByBuildSelectManager;
import com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType;
import com.yunda.jx.pjwz.partsBase.partstype.manager.PartsTypeManager;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车组成查询业务类
 * <li>创建人：程锐
 * <li>创建日期：2014-10-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "buildUpTypeQueryManager")
public class BuildUpTypeQueryManager extends JXBaseManager<BuildUpType, BuildUpType> {
	/** 组成位置业务类 */
	@Resource
	private BuildUpPlaceManager buildUpPlaceManager;
	/** 可安装组成业务类 */
	@Resource
	private FixBuildUpTypeManager fixBuildUpTypeManager;
	
	/** 配件组成型号选择业务类 */
	@Resource
	private PartsTypeByBuildSelectManager partsTypeByBuildSelectManager;
	
	/** 组成型号和位置关系维护视图实体业务类 */
	@Resource
	private BuildUpToPlaceManager buildUpToPlaceManager;
	
	/** 配件规格型号业务类 */
	@Resource
	private PartsTypeManager partsTypeManager;
	
	/** 组成业务类 */
	@Resource
	private BuildUpTypeManager buildUpTypeManager;
    
    private static final String ISPARTSBUILDUP = "isPartsBuildUp";
    private static final String ISVIRTUAL = "isVirtual";
    private static final String ICON = "icon";
    private static final String PARTSBUILDUPTYPEIDX = "partsBuildUpTypeIdx";
    private static final String PARTSTYPEIDX = "partsTypeIDX";
    private static final String TRUE = "true";
    private static final String FALSE = "false";
	
	/**	 
     * <li>说明：组成全位置树（供查看使用）
     * <li>创建人：程锐
     * <li>创建日期：2013-3-1
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param parentIDX 上级父节点idx
     * @param partsBuildUpTypeIdx 所属组成型号idx
     * @param ctx 应用程序根目录
     * @param parentPartsAccountIdx 上级安装配件信息主键
     * @param isVirtual 表示下层节点是否是虚拟位置下节点
     * @param buildUpPlaceFullCode 位置编码全名（从树节点获取）
     * @return List<HashMap> 返回树的集合
     * @throws BusinessException
     */
    public List<HashMap<String, Object>> allTreeForQuery(String parentIDX, 
    													 String partsBuildUpTypeIdx, 
    													 String ctx,
    													 String parentPartsAccountIdx, 
    													 String isVirtual, 
    													 String buildUpPlaceFullCode) throws BusinessException {
        // 获取下级位置列表
        List<BuildUpToPlace> buildPlaceList = this.buildUpPlaceManager.childPlaceList(partsBuildUpTypeIdx, parentIDX);
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        if (buildPlaceList == null || buildPlaceList.size() < 1) return children;
        for (BuildUpToPlace buildUpToPlace : buildPlaceList) {
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            Boolean isLeaf = true;
            // 查询此位置节点是否有下级位置
            List<BuildUpToPlace> childPlaceList = buildUpPlaceManager.childPlaceList(buildUpToPlace.getBuildUpTypeIdx(), 
            																		 buildUpToPlace.getBuildUpPlaceIdx());
            if (childPlaceList != null && childPlaceList.size() > 0) {
                isLeaf = false;
            }
            nodeMap.put(ISPARTSBUILDUP, FALSE);// 是否下挂组成
            nodeMap.put(ISVIRTUAL, isVirtual);// 表示下层节点是否是虚拟位置下节点
            // 此位置为虚拟位置，找出其安装的虚拟组成并根据组成获取组成的下级位置列表以判断是否为叶子节点
            if (buildUpToPlace.getPlaceType() == BuildUpPlace.TYPE_VIRTUAL) {
                List<FixBuildUpType> fixBuildUpTypeList = fixBuildUpTypeManager.getFixBuildList(buildUpToPlace.getBuildUpPlaceIdx());
                nodeMap.put(ICON, ctx + "/jsp/jx/images/builduptree/leaf.gif");
                if (fixBuildUpTypeList == null || fixBuildUpTypeList.size() < 1) continue;
                List<BuildUpToPlace> placeList = buildUpPlaceManager.childPlaceList(fixBuildUpTypeList.get(0).getBuildUpTypeIdx(), 
																					fixBuildUpTypeList.get(0).getBuildUpTypeIdx());
				if (placeList != null && placeList.size() > 0) {
					isLeaf = false;
					nodeMap.put(ISPARTSBUILDUP, TRUE);// 是否下挂组成
					nodeMap.put(ISVIRTUAL, TRUE);// 表示下层节点是虚拟位置下节点
				}
				nodeMap.put(PARTSBUILDUPTYPEIDX, fixBuildUpTypeList.get(0).getBuildUpTypeIdx());// 虚拟组成主键                
            } 
            //有下级节点的结构位置
            else if (buildUpToPlace.getPlaceType() == BuildUpPlace.TYPE_STRUCTURE && isLeaf) {
                nodeMap.put(ICON, ctx + "/jsp/jx/images/builduptree/folder.gif");
            } 
            //安装位置
            else if (buildUpToPlace.getPlaceType() == BuildUpPlace.TYPE_FIX) {
                nodeMap.put(ICON, ctx + "/jsp/jx/images/builduptree/place.png");
                // 获取缺省安装组成型号
                String defaultBuildIdx = fixBuildUpTypeManager.getDefaultBuildIdx(buildUpToPlace.getBuildUpPlaceIdx());
                if (StringUtil.isNullOrBlank(defaultBuildIdx)) continue;
                List<BuildUpToPlace> buildUpToPlaceList = buildUpToPlaceManager.findPlaceList(defaultBuildIdx);
                // 如缺省安装组成型号有下级位置
                if (buildUpToPlaceList != null && buildUpToPlaceList.size() > 0) {
                    isLeaf = false;
                    nodeMap.put(ISPARTSBUILDUP, TRUE);// 是否挂接配件组成型号-是
                    nodeMap.put(PARTSBUILDUPTYPEIDX, defaultBuildIdx);// 缺省可安装组成主键
                    nodeMap.put(ISVIRTUAL, FALSE);// 表示下层节点不是虚拟位置下节点
                }
                BuildUpType build = getModelById(defaultBuildIdx);
                if(build != null){
                    nodeMap.put(PARTSTYPEIDX, build.getPartsTypeIDX());// 配件型号主键
                }                              
            }
            nodeMap.put("buildUpPlaceIdx", buildUpToPlace.getBuildUpPlaceIdx());// 组成位置主键
            nodeMap.put("text", buildUpToPlace.getBuildUpPlaceName());// 节点显示名称
            nodeMap.put("buildUpPlaceCode", buildUpToPlace.getBuildUpPlaceCode());// 组成位置编码
            nodeMap.put("buildUpPlaceShortName", buildUpToPlace.getBuildUpPlaceShortName());// 组成位置简称
            nodeMap.put("chartNo", buildUpToPlace.getChartNo());// 图号
            nodeMap.put("trainTypeIDX", buildUpToPlace.getTrainTypeIDX());// 车型主键
            
            nodeMap.put("professionalTypeIDX", buildUpToPlace.getProfessionalTypeIDX());// 专业类型主键
            nodeMap.put("professionalTypeName", buildUpToPlace.getProfessionalTypeName());// 专业类型名称
            nodeMap.put("partID", buildUpToPlace.getPartID());// 位置主键
            nodeMap.put("partName", buildUpToPlace.getPartName());// 位置名称
            nodeMap.put("placeType", buildUpToPlace.getPlaceType());// 位置类型
            nodeMap.put("leaf", isLeaf);// 是否为叶子节点
            nodeMap.put("buildUpTypeIdx", buildUpToPlace.getBuildUpTypeIdx());// 组成主键
            nodeMap.put("buildUpPlaceFullName", buildUpToPlace.getBuildUpPlaceFullName());// 组成位置全名
            nodeMap.put("buildUpPlaceSEQ", StringUtil.nvlTrim(buildUpToPlace.getBuildUpPlaceSEQ(), "0"));// 组成位置序号
            
            children.add(nodeMap);
        }
        return children;
    }
    
    /**
     * <li>说明：组成和配置全位置树（供提票使用）
     * <li>创建人：程锐
     * <li>创建日期：2013-2-4
     * <li>修改人：程锐	
     * <li>修改日期：2014-1-20
     * <li>修改内容：去除与配置管理有关的代码
     * 
     * @param parentIDX 上级父节点idx
     * @param partsBuildUpTypeIdx 所属组成型号idx
     * @param ctx 应用程序根目录
     * @param isVirtual 表示下层节点是否是虚拟位置下节点
     * @return List<HashMap> 返回树的集合
     * @throws BusinessException
     */
    public List<HashMap<String, Object>> allTree(String parentIDX, 
    											 String partsBuildUpTypeIdx, 
    											 String ctx, 
    											 String isVirtual) throws BusinessException {        
        List<BuildUpToPlace> buildPlaceList = buildUpPlaceManager.childPlaceList(partsBuildUpTypeIdx, parentIDX);//获取下级位置列表
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        if (buildPlaceList == null || buildPlaceList.size() < 1) return children;
        for (BuildUpToPlace buildUpToPlace : buildPlaceList) {
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            Boolean isLeaf = true;
            // 查询此位置节点是否有下级位置
            List<BuildUpToPlace> childPlaceList = buildUpPlaceManager.childPlaceList(buildUpToPlace.getBuildUpTypeIdx(), 
            																		 buildUpToPlace.getBuildUpPlaceIdx());
            if (childPlaceList != null && childPlaceList.size() > 0) {
                isLeaf = false;
            }
            nodeMap.put(ISPARTSBUILDUP, FALSE);// 是否下挂组成
            nodeMap.put(ISVIRTUAL, isVirtual);// 表示下层节点是否是虚拟位置下节点
            // 此位置为虚拟位置，找出其安装的虚拟组成并根据组成获取组成的下级位置列表以判断是否为叶子节点
            if (buildUpToPlace.getPlaceType() == BuildUpPlace.TYPE_VIRTUAL) {
                List<FixBuildUpType> fixBuildUpTypeList = fixBuildUpTypeManager.getFixBuildList(buildUpToPlace.getBuildUpPlaceIdx());
                nodeMap.put(ICON, ctx + "/jsp/jx/images/builduptree/leaf.gif");
                if (fixBuildUpTypeList == null || fixBuildUpTypeList.size() < 1) continue;
                List<BuildUpToPlace> placeList = buildUpPlaceManager.childPlaceList(fixBuildUpTypeList.get(0).getBuildUpTypeIdx(), 
																					fixBuildUpTypeList.get(0).getBuildUpTypeIdx());
				if (placeList != null && placeList.size() > 0) {
					isLeaf = false;
					nodeMap.put(ISPARTSBUILDUP, TRUE);// 是否下挂组成
					nodeMap.put(ISVIRTUAL, TRUE);// 表示下层节点是虚拟位置下节点
				}
				nodeMap.put(PARTSBUILDUPTYPEIDX, fixBuildUpTypeList.get(0).getBuildUpTypeIdx());// 虚拟组成主键
            } else {
                String placeOfBuildIdx = buildUpToPlace.getBuildUpTypeIdx();//该位置所属组成ID
                PartsTypeByBuildSelect partsTypeByBuildSelect = partsTypeByBuildSelectManager.getModelById(placeOfBuildIdx);
                //有下级节点的结构位置
                if (buildUpToPlace.getPlaceType() == BuildUpPlace.TYPE_STRUCTURE && isLeaf) {
                    nodeMap.put(ICON, ctx + "/jsp/jx/images/builduptree/folder.gif");
                    if(partsTypeByBuildSelect != null){
                        nodeMap.put(PARTSTYPEIDX, partsTypeByBuildSelect.getPartsTypeIDX());// 配件型号主键
                        nodeMap.put("partsName", partsTypeByBuildSelect.getPartsName());//配件名称
                        nodeMap.put("specificationModel", partsTypeByBuildSelect.getSpecificationModel());//配件规格型号
                    }
                }
                //安装位置
                if (buildUpToPlace.getPlaceType() == BuildUpPlace.TYPE_FIX) {
                    nodeMap.put(ICON, ctx + "/jsp/jx/images/builduptree/place.png");
                    String defaultBuildIdx = fixBuildUpTypeManager.getDefaultBuildIdx(buildUpToPlace.getBuildUpPlaceIdx());//获取缺省安装组成型号
                    //有缺省可安装组成型号
                    if (!StringUtil.isNullOrBlank(defaultBuildIdx)) {
                        List<BuildUpToPlace> buildUpToPlaceList = buildUpToPlaceManager.findFixPlaceList(defaultBuildIdx);
                        // 如缺省安装组成型号有下级位置
                        if (buildUpToPlaceList != null && buildUpToPlaceList.size() > 0) {
                            isLeaf = false;
                            nodeMap.put(ISPARTSBUILDUP, TRUE);// 是否挂接配件组成型号-是
                            nodeMap.put(PARTSBUILDUPTYPEIDX, defaultBuildIdx);// 缺省可安装组成主键
                            nodeMap.put(ISVIRTUAL, FALSE);// 表示下层节点不是虚拟位置下节点
                        }
                        BuildUpType build = getModelById(defaultBuildIdx);
                        if(build != null){
                            nodeMap.put(PARTSTYPEIDX, build.getPartsTypeIDX());// 配件型号主键
                            //根据配件型号主键获取配件名称、配件规格型号
                            if(!StringUtil.isNullOrBlank(build.getPartsTypeIDX())){
                                PartsType partsType = partsTypeManager.getModelById(build.getPartsTypeIDX());
                                if(partsType != null){
                                    nodeMap.put("partsName", partsType.getPartsName());//配件名称
                                    nodeMap.put("specificationModel", partsType.getSpecificationModel());//配件规格型号
                                }
                            }
                            
                        }
                        
                    }
                    //无缺省安装组成型号
                    else if (partsTypeByBuildSelect != null) {
                        nodeMap.put(PARTSTYPEIDX, partsTypeByBuildSelect.getPartsTypeIDX());// 配件型号主键
                        nodeMap.put("partsName", partsTypeByBuildSelect.getPartsName());//配件名称
                        nodeMap.put("specificationModel", partsTypeByBuildSelect.getSpecificationModel());//配件规格型号
                    }
                }
            }
            nodeMap.put("buildUpPlaceIdx", buildUpToPlace.getBuildUpPlaceIdx());// 组成位置主键
            nodeMap.put("text", buildUpToPlace.getBuildUpPlaceName());// 节点显示名称
            nodeMap.put("buildUpPlaceCode", buildUpToPlace.getBuildUpPlaceCode());// 组成位置编码
            nodeMap.put("buildUpPlaceShortName", buildUpToPlace.getBuildUpPlaceShortName());// 组成位置简称
            nodeMap.put("chartNo", buildUpToPlace.getChartNo());// 图号
            nodeMap.put("trainTypeIDX", buildUpToPlace.getTrainTypeIDX());// 车型主键
            
            nodeMap.put("professionalTypeIDX", buildUpToPlace.getProfessionalTypeIDX());// 专业类型主键
            nodeMap.put("professionalTypeName", buildUpToPlace.getProfessionalTypeName());// 专业类型名称
            nodeMap.put("partID", buildUpToPlace.getPartID());// 位置主键
            nodeMap.put("partName", buildUpToPlace.getPartName());// 位置名称
            nodeMap.put("placeType", buildUpToPlace.getPlaceType());// 位置类型
            nodeMap.put("leaf", isLeaf);// 是否为叶子节点
            nodeMap.put("buildUpTypeIdx", buildUpToPlace.getBuildUpTypeIdx());// 组成主键
            nodeMap.put("buildUpPlaceFullName", buildUpToPlace.getBuildUpPlaceFullName());// 组成位置全名
            nodeMap.put("buildUpPlaceSEQ", StringUtil.nvlTrim(buildUpToPlace.getBuildUpPlaceSEQ(), "0"));// 组成位置序号
            nodeMap.put("buildUpPlaceFullCode", buildUpToPlace.getBuildUpPlaceFullCode());//位置全路径编码                
            children.add(nodeMap);
        }
        return children;
    }
    
    /**
     * <li>说明：根据车型车号查询组成位置根节点列表
     * <li>创建人：程锐
     * <li>创建日期：2013-5-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX 车型主键
     * @param trainNo 车号
     * @return List<HashMap> 返回树的集合
     */
    @SuppressWarnings("unchecked")
    public List<HashMap> getRootBuildUp(String trainTypeIDX, String trainNo) {
		BuildUpType buildUpType = buildUpTypeManager.getBuildByTrain(trainTypeIDX, trainNo);
		return getRootBuildUp(buildUpType);
	}
    
    /**
	 * <li>说明：功能与getRootBuildUp(String,String)相同，该方法用在工位终端的工长派工查询表单中的位置控件上
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-10-24
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param buildUpType 组成型号
	 * @return 位置实体集合
	 * @throws 抛出异常列表
	 */
    @SuppressWarnings("unchecked")
	public List<HashMap> getRootBuildUp(BuildUpType buildUpType) {
		List<HashMap> children = new ArrayList<HashMap>();
		if (buildUpType != null) {
			HashMap nodeMap = new HashMap();
			nodeMap.put("id", "ROOT_0");
			nodeMap.put("leaf", false);
			nodeMap.put("text", buildUpType.getBuildUpTypeName());
			nodeMap.put("buildUpPlaceCode", buildUpType.getBuildUpTypeCode());
			nodeMap.put("parentIDX", "ROOT_0");
			nodeMap.put(PARTSBUILDUPTYPEIDX, buildUpType.getIdx());
			children.add(nodeMap);
		}
		return children;
	}
}
