package com.yunda.jx.jxgc.buildupmanage.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.buildupmanage.entity.BuildUpPlace;
import com.yunda.jx.jxgc.buildupmanage.entity.BuildUpToPlace;
import com.yunda.jx.jxgc.buildupmanage.entity.BuildUpType;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 组成型号和位置关系维护视图实体业务类
 * <li>创建人：程锐
 * <li>创建日期：2013-1-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="buildUpToPlaceManager")
public class BuildUpToPlaceManager extends JXBaseManager<BuildUpToPlace, BuildUpToPlace>{
    /** 组成位置业务类*/
	@Resource
    private BuildUpPlaceManager buildUpPlaceManager;
    /** 组成型号业务类*/
	@Resource
    private BuildUpTypeManager buildUpTypeManager;
    
    private String splitStr = "/";
    
    /**
     * <li>说明：根据组成位置主键、组成型号主键获取组成位置关系视图实体
     * <li>创建人：程锐
     * <li>创建日期：2013-1-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param buildUpPlaceIdx 组成位置主键
     * @param buildUpTypeIdx 组成型号主键
     * @return BuildUpToPlace 组成位置关系视图实体对象
     */
    public BuildUpToPlace findBuildUpToPlace(String buildUpPlaceIdx, String buildUpTypeIdx){
        StringBuilder hql = new StringBuilder();
        hql.append("from BuildUpToPlace where buildUpPlaceIdx = '")
           .append(buildUpPlaceIdx)
           .append("' and buildUpTypeIdx = '")
           .append(buildUpTypeIdx)
           .append("'");
        return (BuildUpToPlace)daoUtils.findSingle(hql.toString());
    }
    /**
     * 
     * <li>说明：根据组成位置主键、组成型号主键获取组成位置关系视图实体列表
     * <li>创建人：程锐
     * <li>创建日期：2013-1-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param buildUpPlaceIdx 组成位置主键
     * @param buildUpTypeIdx 组成型号主键
     * @return List<BuildUpToPlace> 组成位置关系视图实体对象列表
     */
    @SuppressWarnings("unchecked")
    public List<BuildUpToPlace> findBuildUpToPlaceList(String buildUpPlaceIdx, String buildUpTypeIdx){
        StringBuilder hql = new StringBuilder();
        hql.append("from BuildUpToPlace where buildUpPlaceIdx = '")
           .append(buildUpPlaceIdx)
           .append("' and buildUpTypeIdx = '")
           .append(buildUpTypeIdx)
           .append("'");
        return daoUtils.find(hql.toString());
    }
    /**
     * 
     * <li>说明：获取位置全名
     * <li>创建人：程锐
     * <li>创建日期：2013-1-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param placeName 位置名称
     * @param parentIdx 上级位置主键
     * @param partsBuildUpTypeIdx 所属组成主键
     * @return 位置全名
     * @throws BusinessException
     */
    public String getPlaceFullName(String placeName, String parentIdx, String partsBuildUpTypeIdx) throws BusinessException {
        String retStr = "";
        int circleCount = 0;
        String partsBuildUpTypeName = buildUpTypeManager.getModelById(partsBuildUpTypeIdx).getBuildUpTypeName();
        BuildUpPlace place = buildUpPlaceManager.getModelById(parentIdx); 
        /**/
//      对第一层位置树作特殊处理
        if(parentIdx.equals(BuildUpType.PARENT_IDX) || parentIdx.equals(partsBuildUpTypeIdx)){
            return partsBuildUpTypeName.trim() + splitStr + placeName.trim();
        }
        while (place != null) {            
            if ("".equals(retStr)) {
                retStr = place.getBuildUpPlaceName().trim() + splitStr + placeName.trim();
            } else {
                retStr = place.getBuildUpPlaceName().trim() + splitStr + retStr.trim();
            }  
            List<BuildUpToPlace> buildUpToPlaceList = findBuildUpToPlaceList(parentIdx, partsBuildUpTypeIdx);
            if(buildUpToPlaceList != null && buildUpToPlaceList.size() > 0){
                parentIdx = buildUpToPlaceList.get(0).getParentIdx();
                place = buildUpPlaceManager.getModelById(parentIdx);
                circleCount++;
                if (circleCount >= 10) {
                    retStr = "";
                    break;
                }
            }
        }
        return !"".equals(retStr)? partsBuildUpTypeName.trim() + splitStr + retStr :"";
    }
    /**
     * 
     * <li>说明：获取位置编码全名
     * <li>创建人：程锐
     * <li>创建日期：2013-1-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param placeCode 位置编码
     * @param parentIdx 上级位置主键
     * @param partsBuildUpTypeIdx 所属组成主键
     * @return 位置编码全名
     * @throws BusinessException
     */
    public String getPlaceFullCode(String placeCode, String parentIdx, String partsBuildUpTypeIdx) throws BusinessException {
        String retStr = "";
        int circleCount = 0;
        String partsBuildUpTypeCode = buildUpTypeManager.getModelById(partsBuildUpTypeIdx).getBuildUpTypeCode();
        BuildUpPlace place = buildUpPlaceManager.getModelById(parentIdx); 
        /**/
//      对第一层位置树作特殊处理
        if(parentIdx.equals(BuildUpType.PARENT_IDX) || parentIdx.equals(partsBuildUpTypeIdx)){
            return partsBuildUpTypeCode.trim() + splitStr + placeCode.trim();
        }
        while (place != null) {            
            if ("".equals(retStr)) {
                retStr = place.getBuildUpPlaceCode().trim() + splitStr + placeCode.trim();
            } else {
                retStr = place.getBuildUpPlaceCode().trim() + splitStr + retStr.trim();
            }  
            List<BuildUpToPlace> buildUpToPlaceList = findBuildUpToPlaceList(parentIdx, partsBuildUpTypeIdx);
            if(buildUpToPlaceList != null && buildUpToPlaceList.size() > 0){
                parentIdx = buildUpToPlaceList.get(0).getParentIdx();
                place = buildUpPlaceManager.getModelById(parentIdx);
                circleCount++;
                if (circleCount >= 10) {
                    retStr = "";
                    break;
                }
            }
        }
        return !"".equals(retStr)? partsBuildUpTypeCode.trim() + splitStr + retStr :"";
    }
    /**
     * 
     * <li>说明：根据组成主键获取该组成下非结构位置实体列表
     * <li>创建人：程锐
     * <li>创建日期：2013-1-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param buildUpTypeIdx 组成主键
     * @return List<BuildUpToPlace>  组成位置关系视图实体对象列表
     */
    @SuppressWarnings("unchecked")
    public List<BuildUpToPlace> findFixPlaceList(String buildUpTypeIdx){
        StringBuilder hql = new StringBuilder();
        hql.append("from BuildUpToPlace where buildUpTypeIdx = '")
           .append(buildUpTypeIdx)
           .append("' and placeType in (")
           .append(BuildUpPlace.TYPE_FIX)
           .append(",")
           .append(BuildUpPlace.TYPE_VIRTUAL)
           .append(") order by placeType");
        return daoUtils.find(hql.toString());
    }
    /**
     * 
     * <li>说明：根据组成主键和组成位置名称获取该组成下非结构位置实体列表
     * <li>创建人：程锐
     * <li>创建日期：2013-1-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param buildUpTypeIdx 组成主键
     * @param placeName 组成位置名称查询字符串
     * @return List<BuildUpToPlace>  组成位置关系视图实体对象列表
     */
    @SuppressWarnings("unchecked")
    public List<BuildUpToPlace> findFixPlaceList(String buildUpTypeIdx, String placeName){
        StringBuilder hql = new StringBuilder();
        hql.append("from BuildUpToPlace where buildUpTypeIdx = '")
           .append(buildUpTypeIdx)           
           .append("' and placeType in (")
           .append(BuildUpPlace.TYPE_FIX)
           .append(",")
           .append(BuildUpPlace.TYPE_VIRTUAL)
           .append(") ");
           if(!StringUtil.isNullOrBlank(placeName)){
               hql.append(" and buildUpPlaceFullName like '%")
                  .append(placeName)
                  .append("%'");
           }
           hql.append(" order by placeType");
        return daoUtils.find(hql.toString());
    }
    /**
     * 
     * <li>说明：根据组成主键获取该组成下所有位置实体列表
     * <li>创建人：程锐
     * <li>创建日期：2013-4-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param buildUpTypeIdx 组成主键
     * @return List<BuildUpToPlace>  组成位置关系视图实体对象列表
     */
    @SuppressWarnings("unchecked")
    public List<BuildUpToPlace> findPlaceList(String buildUpTypeIdx){
        StringBuilder hql = new StringBuilder();
        hql.append("from BuildUpToPlace where buildUpTypeIdx = '")
           .append(buildUpTypeIdx)           
           .append("' order by placeType");
        return daoUtils.find(hql.toString());
    }
    
    /**
     * <li>说明：根据虚拟组成主键获取虚拟组成下级位置列表
     * <li>创建人：程锐
     * <li>创建日期：2013-1-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param buildUpTypeIdx 虚拟组成主键
     * @return List<BuildUpToPlace> 虚拟组成下级位置列表
     */
    @SuppressWarnings("unchecked")
    public List<BuildUpToPlace> findVirtualPlaceList(String buildUpTypeIdx){
        StringBuilder hql = new StringBuilder();
        hql.append("from BuildUpToPlace where parentIdx = '")
           .append(buildUpTypeIdx)
           .append("' order by placeType");
        return daoUtils.find(hql.toString());
    }    
    
    /**
     * <li>说明：查询位置列表
     * <li>创建人：程锐
     * <li>创建日期：2016-5-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 位置列表
     * @throws BusinessException
     */
    public List<BuildUpToPlaceAndFixBuild> getBuildPlaceList() throws BusinessException {
    	String sb = 
		    		"       select t.idx,\n" +
		    		"              t.buildup_place_idx,\n" + 
		    		"              t.buildup_type_idx,\n" + 
		    		"              t.buildupplace_fullcode,\n" + 
		    		"              t.buildupplace_fullname,\n" + 
		    		"              t.parent_idx,\n" + 
		    		"              t.buildupplace_code,\n" + 
		    		"              t.buildupplace_name,\n" + 
		    		"              t.professional_type_idx,\n" + 
		    		"              t.professional_type_name,\n" + 
		    		"              t.part_id,\n" + 
		    		"              t.part_name,\n" + 
		    		"              t.place_type,\n" + 
		    		"              a.buildup_type_idx as fix_build_type_idx,\n" + 
		    		"(case (select count(idx) from JXGC_PLACE_BuildUp_Type where parent_idx = t.buildup_place_idx and record_status = 0)\n" +
		    		"                               when 0\n" + 
		    		"                               then 0\n" + 
		    		"                               else 1 end) as co_haschild\n" +
		    		"         from V_JXGC_BUILDUPPLACE t\n" + 
		    		"    left join (select * from jxgc_fix_buildup_type a where a.is_default = 1 and a.record_status = 0) a\n" + 
		    		"           on t.buildup_place_idx = a.fix_place_idx\n";
        List list = daoUtils.executeSqlQuery(sb);        
    	return buildPlaceList(list);
    }
    
    /**
     * <li>说明：构造位置列表
     * <li>创建人：程锐
     * <li>创建日期：2016-5-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param list 位置列表
     * @return 位置列表
     */
    private List<BuildUpToPlaceAndFixBuild> buildPlaceList(List list) {
    	List<BuildUpToPlaceAndFixBuild> placeList = new ArrayList<BuildUpToPlaceAndFixBuild>();
        for (int i = 0; i < list.size(); i++) {
        	BuildUpToPlaceAndFixBuild build = new BuildUpToPlaceAndFixBuild();
        	Object[] obj = (Object[]) list.get(i);
        	if (obj == null || obj.length < 1)
        		continue;
        	build.setIdx(obj[0] != null ? obj[0].toString() : "");
        	build.setBuildUpPlaceIdx(obj[1] != null ? obj[1].toString() : "");
        	build.setBuildUpTypeIdx(obj[2] != null ? obj[2].toString() : "");
        	build.setBuildUpPlaceFullCode(obj[3] != null ? obj[3].toString() : "");
        	build.setBuildUpPlaceFullName(obj[4] != null ? obj[4].toString() : "");
        	build.setParentIdx(obj[5] != null ? obj[5].toString() : "");
        	build.setBuildUpPlaceCode(obj[6] != null ? obj[6].toString() : "");
        	build.setBuildUpPlaceName(obj[7] != null ? obj[7].toString() : "");
        	build.setProfessionalTypeIDX(obj[8] != null ? obj[8].toString() : "");
        	build.setProfessionalTypeName(obj[9] != null ? obj[9].toString() : "");
        	build.setPartID(obj[10] != null ? obj[10].toString() : "");
        	build.setPartName(obj[11] != null ? obj[11].toString() : "");
        	build.setPlaceType(obj[12] != null ? Integer.parseInt(obj[12].toString()) : BuildUpPlace.TYPE_FIX);
        	build.setFixBuildTypeIDX(obj[13] != null ? obj[13].toString() : "");
        	build.setCoHaschild(Integer.parseInt(obj[14].toString()));
        	placeList.add(build);
		}
    	return placeList;
    }
    
    /**
     * <li>标题: 机车检修整备管理信息系统
     * <li>说明: 位置实体对象
     * <li>创建人：程锐
     * <li>创建日期：2016-5-27
     * <li>修改人: 
     * <li>修改日期：
     * <li>修改内容：
     * <li>版权: Copyright (c) 2008 运达科技公司
     * @author 信息系统事业部检修整备系统项目组
     * @version 3.2
     */
    public static class BuildUpToPlaceAndFixBuild {
    	
        private String idx;
        /* 组成位置主键 */
        private String buildUpPlaceIdx;
        /* 组成型号主键 */
        private String buildUpTypeIdx;
        /* 组成位置编码全名 */
        private String buildUpPlaceFullCode;
        /* 组成位置名称全名 */
        private String buildUpPlaceFullName;
        /* 上级组成位置 */
        private String parentIdx;
        /* 组成位置编码 */
        private String buildUpPlaceCode;
        /* 组成位置名称 */
        private String buildUpPlaceName;
        /* 配件专业类型表主键 */
        private String professionalTypeIDX;
        /* 专业类型名称 */
        private String professionalTypeName;
        /* 位置编码 */
        private String partID;
        /* 位置名称 */
        private String partName;
        /* 位置类型：10：结构位置；20：安装位置；30：虚拟位置 */
        private Integer placeType;
        /* 该位置的默认可安装组成IDX */
        private String fixBuildTypeIDX;
        /* 是否有子节点 */
        private Integer coHaschild;
        

		public String getBuildUpPlaceCode() {
			return buildUpPlaceCode;
		}

		public void setBuildUpPlaceCode(String buildUpPlaceCode) {
			this.buildUpPlaceCode = buildUpPlaceCode;
		}

		public String getBuildUpPlaceFullCode() {
			return buildUpPlaceFullCode;
		}

		public void setBuildUpPlaceFullCode(String buildUpPlaceFullCode) {
			this.buildUpPlaceFullCode = buildUpPlaceFullCode;
		}

		public String getBuildUpPlaceFullName() {
			return buildUpPlaceFullName;
		}

		public void setBuildUpPlaceFullName(String buildUpPlaceFullName) {
			this.buildUpPlaceFullName = buildUpPlaceFullName;
		}

		public String getBuildUpPlaceIdx() {
			return buildUpPlaceIdx;
		}

		public void setBuildUpPlaceIdx(String buildUpPlaceIdx) {
			this.buildUpPlaceIdx = buildUpPlaceIdx;
		}

		public String getBuildUpPlaceName() {
			return buildUpPlaceName;
		}

		public void setBuildUpPlaceName(String buildUpPlaceName) {
			this.buildUpPlaceName = buildUpPlaceName;
		}

		public String getBuildUpTypeIdx() {
			return buildUpTypeIdx;
		}

		public void setBuildUpTypeIdx(String buildUpTypeIdx) {
			this.buildUpTypeIdx = buildUpTypeIdx;
		}

		public String getFixBuildTypeIDX() {
			return fixBuildTypeIDX;
		}

		public void setFixBuildTypeIDX(String fixBuildTypeIDX) {
			this.fixBuildTypeIDX = fixBuildTypeIDX;
		}

		public String getIdx() {
			return idx;
		}

		public void setIdx(String idx) {
			this.idx = idx;
		}

		public String getParentIdx() {
			return parentIdx;
		}

		public void setParentIdx(String parentIdx) {
			this.parentIdx = parentIdx;
		}

		public String getPartID() {
			return partID;
		}

		public void setPartID(String partID) {
			this.partID = partID;
		}

		public String getPartName() {
			return partName;
		}

		public void setPartName(String partName) {
			this.partName = partName;
		}

		public Integer getPlaceType() {
			return placeType;
		}

		public void setPlaceType(Integer placeType) {
			this.placeType = placeType;
		}

		public String getProfessionalTypeIDX() {
			return professionalTypeIDX;
		}

		public void setProfessionalTypeIDX(String professionalTypeIDX) {
			this.professionalTypeIDX = professionalTypeIDX;
		}

		public String getProfessionalTypeName() {
			return professionalTypeName;
		}

		public void setProfessionalTypeName(String professionalTypeName) {
			this.professionalTypeName = professionalTypeName;
		}

		public Integer getCoHaschild() {
			return coHaschild;
		}

		public void setCoHaschild(Integer coHaschild) {
			this.coHaschild = coHaschild;
		}
                
    }
}
