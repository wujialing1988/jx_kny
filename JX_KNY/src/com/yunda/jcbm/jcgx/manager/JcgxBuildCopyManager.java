package com.yunda.jcbm.jcgx.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jcbm.jcgx.entity.JcgxBuild;
import com.yunda.jcbm.jcgx.entity.JcxtflFault;
import com.yunda.jx.jxgc.buildupmanage.entity.BuildUpType;
import com.yunda.jx.jxgc.buildupmanage.entity.PlaceFault;
import com.yunda.jx.jxgc.buildupmanage.manager.BuildUpToPlaceManager;
import com.yunda.jx.jxgc.buildupmanage.manager.BuildUpTypeQueryManager;
import com.yunda.jx.jxgc.buildupmanage.manager.PlaceFaultManager;
import com.yunda.jx.jxgc.buildupmanage.manager.BuildUpToPlaceManager.BuildUpToPlaceAndFixBuild;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 从机车组成复制机车构型
 * <li>创建人：程锐
 * <li>创建日期：2016-5-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2
 */
@Service(value="jcgxBuildCopyManager")
public class JcgxBuildCopyManager extends JXBaseManager<JcgxBuild, JcgxBuild>{
	
	@Resource
	private BuildUpTypeQueryManager buildUpTypeQueryManager;
	
	@Resource
	private BuildUpToPlaceManager buildUpToPlaceManager;
	
	@Resource
	private PlaceFaultManager placeFaultManager;
	
	/**
     * <li>说明：根据机车组成复制机车构型
     * <li>创建人：程锐
     * <li>创建日期：2016-5-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param buildUpTypeIDX 机车组成IDX
     * @throws BusinessException
     * @throws NoSuchFieldException
	 */
	public void generateByBuildType(String buildUpTypeIDX) throws BusinessException, NoSuchFieldException {
		if (StringUtil.isNullOrBlank(buildUpTypeIDX))
			throw new BusinessException("前台传递的组成idx为空");
		BuildUpType build = buildUpTypeQueryManager.getModelById(buildUpTypeIDX);
		if (build == null)
			throw new BusinessException("组成为空，无法复制");
		String sql = "delete T_JCBM_JCGX where sycx = '".concat(build.getTrainTypeShortName()).concat("'");
		daoUtils.executeSql(sql);
		daoUtils.flush();
		List<BuildUpToPlaceAndFixBuild> allPlaceList = (List<BuildUpToPlaceAndFixBuild>) buildUpToPlaceManager.getBuildPlaceList();
		Map<String, List<BuildUpToPlaceAndFixBuild>> placeListByBuildMap = new HashMap<String, List<BuildUpToPlaceAndFixBuild>>();
		
		for (BuildUpToPlaceAndFixBuild place : allPlaceList) {
			List<BuildUpToPlaceAndFixBuild> placeList = new ArrayList<BuildUpToPlaceAndFixBuild>();
			if (placeListByBuildMap.containsKey(place.getBuildUpTypeIdx()))
				continue;
			placeList.add(place);
			for (BuildUpToPlaceAndFixBuild place1 : allPlaceList) {
				if (place.getBuildUpTypeIdx().equals(place1.getBuildUpTypeIdx()) && !place.getBuildUpPlaceIdx().equals(place1.getBuildUpPlaceIdx()))
					placeList.add(place1);
			}
			placeListByBuildMap.put(place.getBuildUpTypeIdx(), placeList);
		}
		List<JcgxBuild> jcgxList = new ArrayList<JcgxBuild>();
		
		jcgxList = buildJcgxList(build.getIdx(), placeListByBuildMap, jcgxList, build.getTrainTypeShortName(), "0");	
		daoUtils.getHibernateTemplate().saveOrUpdateAll(jcgxList);
	}
	
    /**
     * <li>说明：生成UUID
     * <li>创建人：程锐
     * <li>创建日期：2016-5-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return UUID字符串
     */
	public String createUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
	
    /**
     * <li>说明：从【位置关联的故障现象】复制【分类编码关联的故障现象】
     * <li>创建人：程锐
     * <li>创建日期：2016-5-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
	@SuppressWarnings("unchecked")
	public void savePlaceFault() throws BusinessException, NoSuchFieldException {
		List<BuildUpToPlaceAndFixBuild> allPlaceList = (List<BuildUpToPlaceAndFixBuild>) buildUpToPlaceManager.getBuildPlaceList();
		Map<String, BuildUpToPlaceAndFixBuild> placeMap = new HashMap<String, BuildUpToPlaceAndFixBuild>();
		for (BuildUpToPlaceAndFixBuild place : allPlaceList) {
			placeMap.put(place.getBuildUpPlaceIdx(), place);
		}
		String hql = "from PlaceFault where recordStatus = 0 and buildUpPlaceIdx is not null";//只复制挂在具体位置上的故障现象
		List<PlaceFault> faultList = (List<PlaceFault>) placeFaultManager.find(hql);
		List<JcxtflFault> newFaultList = new ArrayList<JcxtflFault>();
		for (PlaceFault fault : faultList) {
			JcxtflFault newFault = new JcxtflFault();
//			newFault.setIdx(fault.getIdx());
			newFault.setFaultId(fault.getFaultId());
			newFault.setFaultName(fault.getFaultName());
			BuildUpToPlaceAndFixBuild place = placeMap.get(fault.getBuildUpPlaceIdx());
			String flbm = "";
			if (place != null)
				flbm = place.getBuildUpPlaceCode();
			newFault.setFlbm(flbm);
			newFaultList.add(newFault);
		}
		daoUtils.getHibernateTemplate().saveOrUpdateAll(newFaultList);
	}
	
    /**
     * <li>说明：构建【机车构型】列表
     * <li>创建人：程锐
     * <li>创建日期：2016-5-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param buildUpTypeIDX 组成IDX
     * @param placeListByBuildMap Map<组成IDX,关联此组成IDX的位置列表>
     * @param jcgxList 【机车构型】列表
     * @param trainTypeShortName 车型名称
     * @param fjdID 父节点id
     * @return 【机车构型】列表
     */
	public List<JcgxBuild> buildJcgxList(String buildUpTypeIDX, 
										 Map<String, List<BuildUpToPlaceAndFixBuild>> placeListByBuildMap, 
										 List<JcgxBuild> jcgxList, 
										 String trainTypeShortName,
										 String fjdID) {
		List<BuildUpToPlaceAndFixBuild> buildPlaceList = placeListByBuildMap.get(buildUpTypeIDX);
		if (buildPlaceList == null || buildPlaceList.size() < 1)
			return jcgxList;
		for (BuildUpToPlaceAndFixBuild placeAndFixBuild : buildPlaceList) {
			JcgxBuild jcgx = new JcgxBuild();
			String coID = placeAndFixBuild.getBuildUpPlaceIdx();
			for (JcgxBuild jcgxBuildTemp : jcgxList) {
				if (jcgxBuildTemp.getCoID().equals(coID)) {
					coID = createUUID();
					break;
				}					
			}
			jcgx.setCoID(coID);
			if ("ROOT_0".equals(placeAndFixBuild.getParentIdx()))
				jcgx.setFjdID(fjdID);
			else {
				jcgx.setFjdID(placeAndFixBuild.getParentIdx());
                //如果是挂的可安装组成下的第一级节点
				if (placeAndFixBuild.getBuildUpTypeIdx().equals(placeAndFixBuild.getParentIdx())) {
					jcgx.setFjdID(fjdID);
				}
			}	
				
			jcgx.setSycx(trainTypeShortName);
			jcgx.setWzdm(placeAndFixBuild.getPartID());
			jcgx.setWzmc(placeAndFixBuild.getPartName());
			jcgx.setFlbm(placeAndFixBuild.getBuildUpPlaceCode());
			jcgx.setFlmc(placeAndFixBuild.getBuildUpPlaceName());
			jcgx.setPyjc("");//拼音简称
			jcgx.setLbjbm("");//零部件编码
			jcgx.setZylxID(placeAndFixBuild.getProfessionalTypeIDX());
			jcgx.setZylx(placeAndFixBuild.getProfessionalTypeName());
			Integer coHaschild = placeAndFixBuild.getCoHaschild();
			if (JcgxBuild.CO_HAS_CHILD_F == coHaschild) {
				if (!StringUtil.isNullOrBlank(placeAndFixBuild.getFixBuildTypeIDX())) {
					List<BuildUpToPlaceAndFixBuild> childPlaceList = placeListByBuildMap.get(placeAndFixBuild.getFixBuildTypeIDX());
					if (childPlaceList != null && childPlaceList.size() > 0) {
						coHaschild = JcgxBuild.CO_HAS_CHILD_T;						
					}
					jcgxList = buildFirstChildJcgxList(childPlaceList, placeListByBuildMap, jcgxList, trainTypeShortName, coID);
				}				
			}
			jcgx.setCoHaschild(coHaschild);
			jcgx.setWzqm(placeAndFixBuild.getBuildUpPlaceFullName());
			jcgx.setGxwzbm(placeAndFixBuild.getBuildUpPlaceFullCode());
			jcgx.setRecordStatus(Constants.NO_DELETE);
			String fljc = placeAndFixBuild.getBuildUpPlaceName().concat(!StringUtil.isNullOrBlank(placeAndFixBuild.getPartName())?placeAndFixBuild.getPartName():"");
			jcgx.setFljc(fljc);
			jcgxList.add(jcgx);
		}
		return jcgxList;
	}
	
    /**
     * <li>说明：构建可安装组成下的第一级节点的【机车构型】列表
     * <li>创建人：程锐
     * <li>创建日期：2016-5-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param buildPlaceList 关联可安装组成IDX的位置列表
     * @param placeListByBuildMap Map<组成IDX,关联此组成IDX的位置列表>
     * @param jcgxList 【机车构型】列表
     * @param trainTypeShortName 车型名称
     * @param fjdID 父节点id
     * @return 【机车构型】列表
     */
	public List<JcgxBuild> buildFirstChildJcgxList(List<BuildUpToPlaceAndFixBuild> buildPlaceList, 
											       Map<String, List<BuildUpToPlaceAndFixBuild>> placeListByBuildMap, 
											       List<JcgxBuild> jcgxList, 
											       String trainTypeShortName,
											       String fjdID) {
		if (buildPlaceList == null || buildPlaceList.size() < 1)
			return jcgxList;
		Map<String, List<BuildUpToPlaceAndFixBuild>> childPlaceMap = new HashMap<String, List<BuildUpToPlaceAndFixBuild>>();
		List<BuildUpToPlaceAndFixBuild> firstChildList = new ArrayList<BuildUpToPlaceAndFixBuild>();
		for (BuildUpToPlaceAndFixBuild placeAndFixBuild : buildPlaceList) {
			if (placeAndFixBuild.getBuildUpTypeIdx().equals(placeAndFixBuild.getParentIdx()) )
				firstChildList.add(placeAndFixBuild);
			else {
				List<BuildUpToPlaceAndFixBuild> childList = new ArrayList<BuildUpToPlaceAndFixBuild>();
				if (childPlaceMap.containsKey(placeAndFixBuild.getParentIdx()))
					childList = childPlaceMap.get(placeAndFixBuild.getParentIdx());
				childList.add(placeAndFixBuild);
				childPlaceMap.put(placeAndFixBuild.getParentIdx(), childList);
			}
		}
		for (BuildUpToPlaceAndFixBuild firstBuild : firstChildList) {
			JcgxBuild jcgx = new JcgxBuild();
			String coID = firstBuild.getBuildUpPlaceIdx();
			for (JcgxBuild jcgxBuildTemp : jcgxList) {
				if (jcgxBuildTemp.getCoID().equals(coID)) {
					coID = createUUID();
					break;
				}					
			}
			jcgx.setCoID(coID);
			jcgx.setFjdID(fjdID);
			jcgx.setSycx(trainTypeShortName);
			jcgx.setWzdm(firstBuild.getPartID());
			jcgx.setWzmc(firstBuild.getPartName());
			jcgx.setFlbm(firstBuild.getBuildUpPlaceCode());
			jcgx.setFlmc(firstBuild.getBuildUpPlaceName());
			jcgx.setPyjc("");//拼音简称
			jcgx.setLbjbm("");//零部件编码
			jcgx.setZylxID(firstBuild.getProfessionalTypeIDX());
			jcgx.setZylx(firstBuild.getProfessionalTypeName());
			Integer coHaschild = firstBuild.getCoHaschild();
			if (JcgxBuild.CO_HAS_CHILD_F == coHaschild) {
				if (!StringUtil.isNullOrBlank(firstBuild.getFixBuildTypeIDX())) {
					List<BuildUpToPlaceAndFixBuild> childPlaceList = placeListByBuildMap.get(firstBuild.getFixBuildTypeIDX());
					if (childPlaceList != null && childPlaceList.size() > 0) {
						coHaschild = JcgxBuild.CO_HAS_CHILD_T;						
					}
					jcgxList = buildFirstChildJcgxList(childPlaceList, placeListByBuildMap, jcgxList, trainTypeShortName, coID);
				}				
			} else if (JcgxBuild.CO_HAS_CHILD_T == coHaschild) {
				List<BuildUpToPlaceAndFixBuild> childPlaceList = childPlaceMap.get(firstBuild.getBuildUpPlaceIdx());
				jcgxList = buildChildJcgxList(childPlaceList, placeListByBuildMap, jcgxList, trainTypeShortName, coID, childPlaceMap);
			}
			jcgx.setCoHaschild(coHaschild);
			jcgx.setWzqm(firstBuild.getBuildUpPlaceFullName());
			jcgx.setGxwzbm(firstBuild.getBuildUpPlaceFullCode());
			jcgx.setRecordStatus(Constants.NO_DELETE);
			String fljc = firstBuild.getBuildUpPlaceName().concat(!StringUtil.isNullOrBlank(firstBuild.getPartName())?firstBuild.getPartName():"");
			jcgx.setFljc(fljc);
			jcgxList.add(jcgx);
		}
		return jcgxList;
	}
	
    /**
     * <li>说明：构建可安装组成下节点的子节点的【机车构型】列表
     * <li>创建人：程锐
     * <li>创建日期：2016-5-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param buildPlaceList 子位置节点列表
     * @param placeListByBuildMap Map<组成IDX,关联此组成IDX的位置列表>
     * @param jcgxList 【机车构型】列表
     * @param trainTypeShortName 车型名称
     * @param fjdID 父节点id
     * @param childPlaceMap Map<位置IDX,此位置IDX的子节点列表>
     * @return 【机车构型】列表
     */
	public List<JcgxBuild> buildChildJcgxList(List<BuildUpToPlaceAndFixBuild> buildPlaceList, 
											  Map<String, List<BuildUpToPlaceAndFixBuild>> placeListByBuildMap, 
											  List<JcgxBuild> jcgxList, 
											  String trainTypeShortName,
											  String fjdID,
											  Map<String, List<BuildUpToPlaceAndFixBuild>> childPlaceMap) {
		if (buildPlaceList == null || buildPlaceList.size() < 1)
			return jcgxList;
		for (BuildUpToPlaceAndFixBuild firstBuild : buildPlaceList) {
			JcgxBuild jcgx = new JcgxBuild();
			String coID = firstBuild.getBuildUpPlaceIdx();
			for (JcgxBuild jcgxBuildTemp : jcgxList) {
				if (jcgxBuildTemp.getCoID().equals(coID)) {
					coID = createUUID();
					break;
				}					
			}
			jcgx.setCoID(coID);
			jcgx.setFjdID(fjdID);
			jcgx.setSycx(trainTypeShortName);
			jcgx.setWzdm(firstBuild.getPartID());
			jcgx.setWzmc(firstBuild.getPartName());
			jcgx.setFlbm(firstBuild.getBuildUpPlaceCode());
			jcgx.setFlmc(firstBuild.getBuildUpPlaceName());
			jcgx.setPyjc("");//拼音简称
			jcgx.setLbjbm("");//零部件编码
			jcgx.setZylxID(firstBuild.getProfessionalTypeIDX());
			jcgx.setZylx(firstBuild.getProfessionalTypeName());
			Integer coHaschild = firstBuild.getCoHaschild();
			if (JcgxBuild.CO_HAS_CHILD_F == coHaschild) {
				if (!StringUtil.isNullOrBlank(firstBuild.getFixBuildTypeIDX())) {
					List<BuildUpToPlaceAndFixBuild> childPlaceList = placeListByBuildMap.get(firstBuild.getFixBuildTypeIDX());
					if (childPlaceList != null && childPlaceList.size() > 0) {
						coHaschild = JcgxBuild.CO_HAS_CHILD_T;						
					}
					jcgxList = buildFirstChildJcgxList(childPlaceList, placeListByBuildMap, jcgxList, trainTypeShortName, coID);
				}				
			} else if (JcgxBuild.CO_HAS_CHILD_T == coHaschild) {
				List<BuildUpToPlaceAndFixBuild> childPlaceList = childPlaceMap.get(firstBuild.getBuildUpPlaceIdx());
				jcgxList = buildChildJcgxList(childPlaceList, placeListByBuildMap, jcgxList, trainTypeShortName, coID, childPlaceMap);
			}
			jcgx.setCoHaschild(coHaschild);
			jcgx.setWzqm(firstBuild.getBuildUpPlaceFullName());
			jcgx.setGxwzbm(firstBuild.getBuildUpPlaceFullCode());
			jcgx.setRecordStatus(Constants.NO_DELETE);
			String fljc = firstBuild.getBuildUpPlaceName().concat(!StringUtil.isNullOrBlank(firstBuild.getPartName())?firstBuild.getPartName():"");
			jcgx.setFljc(fljc);
			jcgxList.add(jcgx);
		}
		return jcgxList;
	}
}
