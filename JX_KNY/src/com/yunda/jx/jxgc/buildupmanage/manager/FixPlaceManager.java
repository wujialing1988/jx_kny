
package com.yunda.jx.jxgc.buildupmanage.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.buildupmanage.entity.FixBuildUpType;
import com.yunda.jx.jxgc.buildupmanage.entity.FixPlace;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：FixPlace业务类,组成型号安装位置
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
@Service(value = "fixPlaceManager")
public class FixPlaceManager extends JXBaseManager<FixPlace, FixPlace> {
    
    /** 组成型号业务类 */
	@Resource
    private BuildUpTypeManager buildUpTypeManager;
    
    /**
     * <li>说明：删除实体对象前的验证业务
     * <li>创建人：程锐
     * <li>创建日期：2012-10-24
     * <li>修改人：程锐
     * <li>修改日期：2012-11-30
     * <li>修改内容：验证如果该位置有下级安装位置则不能删除
     * 
     * @param ids 实体对象的idx主键数组
     * @return 返回删除操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
     * @throws BusinessException
     */
    @Override
    @SuppressWarnings(value="unchecked")
    public String[] validateDelete(Serializable... ids) throws BusinessException {
        List<String> errMsg = new ArrayList<String>();
        List<FixPlace> childList = null;
        for (Serializable id : ids) {
			FixPlace fixPlace = getModelById(id);
			String hql = "from FixPlace where recordStatus = 0 and parentIdx = '" + id + "'";
			childList = daoUtils.find(hql);
			if (childList != null && childList.size() > 0) {
				errMsg.add("安装位置编码为" + fixPlace.getFixPlaceCode() + "的安装位置有下级位置，不能删除！");
			}
		}
        if (errMsg.size() > 0) {
            String[] errArray = new String[errMsg.size()];
            errMsg.toArray(errArray);
            return errArray;
        }
        return null;
    }
    
    /**
     * <li>说明：新增修改保存前的实体对象前的验证业务
     * <li>创建人：程锐
     * <li>创建日期：2012-10-24
     * <li>修改人：程锐
     * <li>修改日期：2012-11-29
     * <li>修改内容：验证安装位置编码是否唯一,
     *              2如果开始为非虚拟位置，且维护了可安装的组成型号，不能在修改为虚拟位置； 
     *               如果开始为虚拟位置，且维护了下阶安装位置，不能再修改为非虚拟位置. 
     * @param entity 实体对象
     * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
     * @throws BusinessException
     */
    @Override
    @SuppressWarnings(value="unchecked")
    public String[] validateUpdate(FixPlace entity) throws BusinessException {
        String[] errs = null;
        List<FixPlace> list = null;
        String fixPlaceIdx = entity.getIdx();
        //验证安装位置编码是否唯一
        if (!StringUtil.isNullOrBlank(fixPlaceIdx)) {
			String hql = "from FixPlace where recordStatus = 0 and idx != '" + fixPlaceIdx + 
						 "' and fixPlaceCode = '" + entity.getFixPlaceCode() + "'";
			list = daoUtils.find(hql);
		} else {
			String hql = "from FixPlace where recordStatus = 0  and fixPlaceCode = '" + entity.getFixPlaceCode() + "'";
			list = daoUtils.find(hql);
		}        
        if (list != null && list.size() > 0) {
			errs = new String[1];
			errs[0] = "已存在安装位置编码为【" + entity.getFixPlaceCode() + "】的安装位置信息！";
			return errs;
		}
        // 验证是否可以修改虚拟位置信息
        if (!StringUtil.isNullOrBlank(fixPlaceIdx)) {
			FixPlace oldFixPlace = getModelById(fixPlaceIdx);
			int isVirtual = oldFixPlace.getIsVirtual();
			// 修改前为虚拟位置节点
			if (isVirtual == FixPlace.ISVIRTUAL && entity.getIsVirtual() == FixPlace.NOTVIRTUAL) {
				String hql = "from FixPlace where recordStatus = 0 and parentIdx = '" + fixPlaceIdx + "'";
				list = daoUtils.find(hql);
				if (list != null && list.size() > 0) {
					errs = new String[1];
					errs[0] = "安装位置编码为【" + entity.getFixPlaceCode() + "】的安装位置已维护下级安装位置，不能修改为非虚拟位置！";
					return errs;
				}
			}
			// 修改前为非虚拟位置节点
			else if (isVirtual == FixPlace.NOTVIRTUAL && entity.getIsVirtual() == FixPlace.ISVIRTUAL) {
				String hql = "from FixBuildUpType where recordStatus = 0  and fixPlaceIdx = '" + fixPlaceIdx + "'";
				List<FixBuildUpType> fixbuildList = daoUtils.find(hql);
				if (fixbuildList != null && fixbuildList.size() > 0) {
					errs = new String[1];
					errs[0] = "安装位置编码为【" + entity.getFixPlaceCode() + "】的安装位置已维护可安装组成型号，不能修改为虚拟位置！";
					return errs;
				}
			}
		}
        return null;
    }    
    /**
	 * <li>说明：根据安装位置名称、上级安装位置主键及组成主键获取安装位置编码全名，用于保存时构造全名
	 * <li>创建人：程锐
	 * <li>创建日期：2012-11-13
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param fixPlaceName 安装位置名称
	 * @param parentIdx 上级安装位置主键
	 * @param partsBuildUpTypeIdx 所属组成型号主键
	 * @return 安装位置全名
	 * @throws Exception
	 */
    public String getPlaceFullName(String fixPlaceName, String parentIdx, String partsBuildUpTypeIdx) throws BusinessException {
        String retStr = "";
        FixPlace fixPlace = getModelById(parentIdx);        
        int circleCount = 0;
        String partsBuildUpTypeName = buildUpTypeManager.getModelById(partsBuildUpTypeIdx).getBuildUpTypeName();
        // 对第一层位置树作特殊处理
        if ("ROOT_0".equals(parentIdx) || parentIdx.equals(partsBuildUpTypeIdx)) {
			return partsBuildUpTypeName.trim() + "/" + fixPlaceName.trim();
		}
        while (fixPlace != null) {            
            if ("".equals(retStr)) {
                retStr = fixPlace.getFixPlaceName().trim() + "/" + fixPlaceName.trim();
            } else {
                retStr = fixPlace.getFixPlaceName().trim() + "/" + retStr.trim();
            }            
            parentIdx = fixPlace.getParentIdx();
            fixPlace = getModelById(parentIdx);
            circleCount++;
            if (circleCount >= 10) {
                retStr = "";
                break;
            }
        }
        return !"".equals(retStr)? partsBuildUpTypeName.trim() + "/" + retStr :"";
    }
    /**
     * 
     * <li>说明：根据安装位置编码、上级安装位置主键及组成主键获取安装位置编码全名,用于保存时构造全名
     * <li>创建人：程锐
     * <li>创建日期：2012-11-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param fixPlaceCode 安装位置编码
     * @param parentIdx 上级安装位置主键
     * @param partsBuildUpTypeIdx 所属组成型号主键
     * @return 安装位置编码全名
     * @throws BusinessException
     */
    public String getPlaceFullCode(String fixPlaceCode, String parentIdx, String partsBuildUpTypeIdx) throws BusinessException {
        String retStr = "";
        FixPlace fixPlace = getModelById(parentIdx);
        int circleCount = 0;
        String partsBuildUpTypeName = buildUpTypeManager.getModelById(partsBuildUpTypeIdx).getBuildUpTypeCode();
        //对第一层位置树作特殊处理
        if ("ROOT_0".equals(parentIdx) || parentIdx.equals(partsBuildUpTypeIdx)) {
			return partsBuildUpTypeName.trim() + "-" + fixPlaceCode.trim();
		}
        while (fixPlace != null) {            
            if ("".equals(retStr)) {
                retStr = fixPlace.getFixPlaceCode().trim() + "-" + fixPlaceCode.trim();
            } else {
                retStr = fixPlace.getFixPlaceCode().trim() + "-" + retStr.trim();
            }            
            parentIdx = fixPlace.getParentIdx();
            fixPlace = getModelById(parentIdx);
            circleCount++;
            if (circleCount >= 10) {
                retStr = "";
                break;
            }
        }
        
        return !"".equals(retStr)? partsBuildUpTypeName.trim() + "-" + retStr :"";
    }
    
    /**
     * <li>说明：获取下级位置列表
     * <li>创建人：程锐
     * <li>创建日期：2012-11-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param searchEntity 安装位置信息实体包装类
     * @param parentIdx 上级位置idx
     * @param partsBuildUpTypeIdx 所属组成型号idx
     * @return void
     * @throws BusinessException
     */
    public Page<FixPlace> childPlaceList(SearchEntity<FixPlace> searchEntity, String parentIdx,
        String partsBuildUpTypeIdx) throws BusinessException {
        String hql =
            "from FixPlace where recordStatus=0 and parentIdx = '" + parentIdx + "' and partsBuildUpTypeIdx = '"
                + partsBuildUpTypeIdx + "' order by fixPlaceSEQ";
        String totalHql = "select count(*) " + hql;
        return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
    }
}
