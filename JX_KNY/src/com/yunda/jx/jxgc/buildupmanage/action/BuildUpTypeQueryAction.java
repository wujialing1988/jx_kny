package com.yunda.jx.jxgc.buildupmanage.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.buildupmanage.entity.BuildUpType;
import com.yunda.jx.jxgc.buildupmanage.manager.BuildUpTypeQueryManager;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 组成查询控制器类
 * <li>创建人：程锐
 * <li>创建日期：2014-10-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class BuildUpTypeQueryAction extends JXBaseAction<BuildUpType, BuildUpType, BuildUpTypeQueryManager> {
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());	
	/**
	 * 
	 * <li>说明：查询组成树
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void queryBuildPlaceList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		try {
			String parentIDX = getRequest().getParameter("parentIDX");
			String partsBuildUpTypeIdx = getRequest().getParameter("partsBuildUpTypeIdx");
			
			String isPartsBuildUp = StringUtil.nvlTrim(getRequest().getParameter("isPartsBuildUp"), "");
			String buildUpPlaceIdx = getRequest().getParameter("buildUpPlaceIdx");
			String buildUpTypeIdx = getRequest().getParameter("buildUpTypeIdx");
			String isVirtual = StringUtil.nvlTrim(getRequest().getParameter("isVirtual"), "");
			if (!StringUtil.isNullOrBlank(isPartsBuildUp)) {
				if ("false".equals(isPartsBuildUp)) {
					parentIDX = buildUpPlaceIdx;
					partsBuildUpTypeIdx = buildUpTypeIdx;
				} else if ("true".equals(isPartsBuildUp)) {
					parentIDX = partsBuildUpTypeIdx;
				}
			}
			list = this.manager.allTree(parentIDX, 
										partsBuildUpTypeIdx, 
										"", 
										isVirtual);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), list);
		}
	}
	/**
     * 
     * <li>说明：组成全位置查看树
     * <li>创建人：程锐
     * <li>创建日期：2013-3-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void allTreeForQuery() throws Exception {
        String partsBuildUpTypeIdx = StringUtil.nvlTrim(getRequest().getParameter("partsBuildUpTypeIdx"), "");
        String parentIDX = StringUtil.nvlTrim(getRequest().getParameter("parentIDX"), BuildUpType.PARENT_IDX);
        
        String ctx = getRequest().getContextPath();
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        String parentPartsAccountIdx = StringUtil.nvlTrim(getRequest().getParameter("parentPartsAccountIdx"), "");
        String isVirtual = StringUtil.nvlTrim(getRequest().getParameter("isVirtual"), "");
        String buildUpPlaceFullCode = StringUtil.nvlTrim(getRequest().getParameter("buildUpPlaceFullCode"), "");
        if (!StringUtil.isNullOrBlank(partsBuildUpTypeIdx)) {
            children = manager.allTreeForQuery(parentIDX, 
            								   partsBuildUpTypeIdx, 
            								   ctx, 
            								   parentPartsAccountIdx, 
            								   isVirtual, 
            								   buildUpPlaceFullCode);
        }
        JSONUtil.write(getResponse(), children);
    }
    /**
     * 
     * <li>说明：组成和配置全位置树（供提票使用）
     * <li>创建人：程锐
     * <li>创建日期：2013-2-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void allTree() throws Exception {
        String partsBuildUpTypeIdx = StringUtil.nvlTrim(getRequest().getParameter("partsBuildUpTypeIdx"), "");
        String parentIDX = StringUtil.nvlTrim(getRequest().getParameter("parentIDX"), BuildUpType.PARENT_IDX);
        
        String ctx = getRequest().getContextPath();
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
//        String parentPartsAccountIdx = StringUtil.nvlTrim(getRequest().getParameter("parentPartsAccountIdx"), "");
        String isVirtual = StringUtil.nvlTrim(getRequest().getParameter("isVirtual"), "");
//        String buildUpPlaceFullCode = StringUtil.nvlTrim(getRequest().getParameter("buildUpPlaceFullCode"), "");
//        String trainNo = getRequest().getParameter("trainNo");
        if (!StringUtil.isNullOrBlank(partsBuildUpTypeIdx)) {
            children = manager.allTree(parentIDX, 
            						   partsBuildUpTypeIdx, 
            						   ctx, 
            						   isVirtual);
        }
        JSONUtil.write(getResponse(), children);
    }
    /**
     * 
     * <li>说明：根据车型车号查询组成位置根节点列表
     * <li>创建人：程锐
     * <li>创建日期：2014-10-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public void queryRootBuildByTrain() throws Exception {
    	Map<String, Object> map = new HashMap<String,Object>();
    	List<HashMap> list = new ArrayList<HashMap>();
		try {			
			String trainTypeIDX = getRequest().getParameter("trainTypeIDX");
			String trainNo = getRequest().getParameter("trainNo");
			list = this.manager.getRootBuildUp(trainTypeIDX, trainNo);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), list);
		}
    }
    /**
     * 
     * <li>说明：根据组成型号查询组成位置根节点列表
     * <li>创建人：程锐
     * <li>创建日期：2014-10-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public void queryRootBuildByBuildType() throws Exception {
    	Map<String, Object> map = new HashMap<String,Object>();
    	List<HashMap> list = new ArrayList<HashMap>();
		try {			
			String buildUpTypeIDX = getRequest().getParameter("buildUpTypeIDX");
			BuildUpType buildUpType = this.manager.getModelById(buildUpTypeIDX);
			if (BuildUpType.USE_STATUS == buildUpType.getStatus())
				list = this.manager.getRootBuildUp(buildUpType);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), list);
		}
    }
}
