
package com.yunda.jx.jxgc.buildupmanage.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.buildupmanage.entity.BuildUpType;
import com.yunda.jx.jxgc.buildupmanage.manager.BuildUpTypeManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：BuildUpType控制器, 组成型号
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
@SuppressWarnings(value = "serial")
public class BuildUpTypeAction extends JXBaseAction<BuildUpType, BuildUpType, BuildUpTypeManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：单表分页查询，返回单表分页查询记录的json
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-08-07
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param
     * @return void
     * @throws Exception
     */
    @Override
    public void pageList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            BuildUpType entity = (BuildUpType) JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<BuildUpType> searchEntity = new SearchEntity<BuildUpType>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findPage(searchEntity).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * <li>说明：单表分页查询组成型号，返回单表分页查询记录的json
     * <li>创建人：王治龙
     * <li>创建日期：2012-12-19
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param
     * @return void
     * @throws Exception
     */
    public void pageListType() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String entityJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            BuildUpType entity = (BuildUpType) JSONUtil.read(entityJson, entitySearch.getClass());
            SearchEntity<BuildUpType> searchEntity = new SearchEntity<BuildUpType>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findPageBuildType(searchEntity).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：接受请求添加默认组成记录，记录类型：BuildUpType.TYPE_TRAIN机车组成型号,BuildUpType.TYPE_PARTS配件组成型号
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-11-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param
     * @return void
     * @throws Exception
     */
    public void addDefault() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            int type = Integer.parseInt(getRequest().getParameter("type"));
            String[] errMsg = this.manager.validateHasBuildUpType(id, type);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.addDefault(type, this.id);
                map.put("success", "true");
            } else {
                map.put("success", false);
                map.put("errMsg", errMsg);
            }
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * <li>说明：获取默认组成
     * <li>创建人：王治龙
     * <li>创建日期：2013-2-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param
     * @return void
     * @throws Exception
     */
    public void findDefaultBuildType() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            
            String trainTypeIDX = StringUtil.nvlTrim(getRequest().getParameter("trainTypeIDX"), "");
            BuildUpType  build = this.manager.getDefaultBuildByTrain(trainTypeIDX);
            map.put("entity", build);
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * 
     * <li>说明：按组成类型批量添加标准组成记录
     * <li>创建人：程锐
     * <li>创建日期：2013-1-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void addAllDefault() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            int type = Integer.parseInt(getRequest().getParameter("type"));
            this.manager.addAllDefault(type);
            map.put("success", "true");            
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * <li>说明：接受请求更新组成型号记录的业务状态，可变更为启用或废弃状态
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-11-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param
     * @return void
     * @throws Exception
     */
    @SuppressWarnings("all")
    public void updateStatus() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            int status = Integer.parseInt(getRequest().getParameter("status"));
            this.manager.updateStatus(status, this.ids);
            map.put("success", "true");
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：设置标准组成
     * <li>创建人：程锐
     * <li>创建日期：2012-11-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param
     * @return void
     * @throws Exception
     */
    public void setIsDefault() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String[] errMsg = this.manager.validateDefault(id);
            if (errMsg == null || errMsg.length < 1) {
                BuildUpType buildUpType = this.manager.getModelById(id);
                this.manager.setIsDefault(buildUpType);
                map.put("success", "true");
            } else {
                map.put("success", "false");
                map.put("errMsg", errMsg);
            }
            
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据安装位置获取默认配件组成型号信息
     * <li>创建人：程锐
     * <li>创建日期：2012-10-31
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @return void
     * @throws Exception
     */
    public void getBuildUpTypeByFixPlace() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            // 安装位置主键
            String fixPlaceIdx = req.getParameter("fixPlaceIdx");
            BuildUpType buildUpType = new BuildUpType();
            if (!StringUtil.isNullOrBlank(fixPlaceIdx)) {
                buildUpType = this.manager.getDefaultBuildUpTypeByFixPlace(fixPlaceIdx);
            }
            map.put("entity", buildUpType);
            map.put("success", "true");
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据安装组成主键获取配件组成型号信息
     * <li>创建人：程锐
     * <li>创建日期：2012-11-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param
     * @return void
     * @throws Exception
     */
    public void getBuildUpTypeByIdx() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            // 组成主键
            String partsBuildUpTypeIdx = req.getParameter("partsBuildUpTypeIdx");
            BuildUpType buildUpType = new BuildUpType();
            if (!StringUtil.isNullOrBlank(partsBuildUpTypeIdx)) {
                buildUpType = this.manager.getModelById(partsBuildUpTypeIdx);
            }
            map.put("entity", buildUpType);
            map.put("success", "true");
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据组成类型和（车型或配件主键）获取默认组成信息
     * <li>创建人：程锐
     * <li>创建日期：2012-11-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param
     * @return void
     * @throws Exception
     */
    public void getBuildUpTypeByTypeIDX() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            // 组成类型
            String type = req.getParameter("type");
            // 车型或配件主键
            String typeIDX = req.getParameter("typeIDX");
            BuildUpType buildUpType = new BuildUpType();
            if (!StringUtil.isNullOrBlank(typeIDX)) {
                buildUpType = this.manager.getBuildUpTypeByTypeIDX(type, typeIDX);
                map.put("entity", buildUpType);
                map.put("success", true);
            }else{
                map.put("success", false);
            }            
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：构建组成位置树
     * <li>创建人：程锐
     * <li>创建日期：2012-11-12
     * <li>修改人：程锐
     * <li>修改日期：2013-1-17
     * <li>修改内容：修改组成树的构造方法
     * 
     * @param
     * @return void
     * @throws Exception
     */
    public void tree() throws Exception {
        String partsBuildUpTypeIdx = StringUtil.nvlTrim(getRequest().getParameter("partsBuildUpTypeIdx"), "");
        String parentIDX = StringUtil.nvlTrim(getRequest().getParameter("parentIDX"), BuildUpType.PARENT_IDX);
        String isPartsBuildUp = StringUtil.nvlTrim(getRequest().getParameter("isPartsBuildUp"), "");
        String ctx = getRequest().getContextPath();
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        if (!StringUtil.isNullOrBlank(partsBuildUpTypeIdx)) {
            children = manager.tree(parentIDX, partsBuildUpTypeIdx, ctx, isPartsBuildUp);
        }
        JSONUtil.write(getResponse(), children);
    }
    /**
     * TODO 配件检修业务变化，待删除
     * <li>说明：构建组成树（供工艺流程节点挂接检修项目使用）
     * <li>创建人：程锐
     * <li>创建日期：2013-1-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param
     * @return void
     * @throws Exception
     */
    public void findBuildUpTypeTree() throws Exception {
        String partsBuildUpTypeIdx = StringUtil.nvlTrim(getRequest().getParameter("partsBuildUpTypeIdx"), "");
        String parentIDX = StringUtil.nvlTrim(getRequest().getParameter("parentIDX"), BuildUpType.PARENT_IDX);        
        String ctx = getRequest().getContextPath();
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        if (!StringUtil.isNullOrBlank(partsBuildUpTypeIdx)) {
            children = manager.findBuildUpTypeTree(parentIDX, partsBuildUpTypeIdx, ctx);
        }
        JSONUtil.write(getResponse(), children);
    }
    
    /**
     * <li>说明：根据安装位置查出可安装组成型号列表
     * <li>创建人：程锐
     * <li>创建日期：2012-11-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param
     * @return void
     * @throws Exception
     */
    public void list() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            // 安装位置主键
            String fixPlaceIdx = req.getParameter("fixPlaceIdx");
            map = this.manager.list(getStart(), getLimit(), fixPlaceIdx).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据安装位置idx过滤上级组成型号及已添加可安装组成型号
     * <li>创建人：程锐
     * <li>创建日期：2012-11-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param
     * @return void
     * @throws Exception
     */
    public void fixBuildUpTypeList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>(); 
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            BuildUpType entity = (BuildUpType) JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<BuildUpType> searchEntity = new SearchEntity<BuildUpType>(entity, getStart(), getLimit(), getOrders());
            // 安装位置主键
            String fixPlaceIdx = req.getParameter("fixPlaceIdx");    
            //组成类型 
            String type = StringUtil.nvlTrim(req.getParameter("type"), "0");
            String trainTypeIDX = req.getParameter("trainTypeIDX");//车型主键
            String partsTypeIDX = req.getParameter("partsTypeIDX");//配件主键
            map = this.manager.fixBuildUpTypeList(searchEntity, fixPlaceIdx, type, trainTypeIDX, partsTypeIDX).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    
    
    /**
     * <li>方法说明：查询组成关联配件型号 
     * <li>方法名称：findBuildUpJoinPartsType
     * <li>@throws JsonMappingException
     * <li>@throws IOException
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-8-29 下午04:51:51
     * <li>修改人：
     * <li>修改内容：
     */
    public void findBuildUpJoinPartsType() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String queryHql = req.getParameter("queryHql");
            String partsname = StringUtil.nvlTrim(req.getParameter("partsname"), "");
            String specificationModel = StringUtil.nvlTrim(req.getParameter("specificationModel"), "");
            String buildUpTypeName = StringUtil.nvlTrim(req.getParameter("nameplateNo"), "");
            map = this.manager.findBuildUpJoinPartsType(getStart(), getLimit(), queryHql, partsname, specificationModel, buildUpTypeName, getOrders());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
