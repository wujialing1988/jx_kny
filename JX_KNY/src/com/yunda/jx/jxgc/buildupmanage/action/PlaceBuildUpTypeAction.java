package com.yunda.jx.jxgc.buildupmanage.action; 

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.buildupmanage.entity.PlaceBuildUpType;
import com.yunda.jx.jxgc.buildupmanage.manager.PlaceBuildUpTypeManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PlaceBuildUpType控制器, 组成位置关系
 * <li>创建人：程锐
 * <li>创建日期：2013-01-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class PlaceBuildUpTypeAction extends JXBaseAction<PlaceBuildUpType, PlaceBuildUpType, PlaceBuildUpTypeManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：保存配件位置和虚拟位置的组成位置关系
     * <li>创建人：程锐
     * <li>创建日期：2013-1-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void saveOrUpdateList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();        
        try {
            HttpServletRequest req = getRequest();            
            PlaceBuildUpType[] placeBuildUpTypeList =
                (PlaceBuildUpType[]) JSONUtil.read(req, PlaceBuildUpType[].class); // 获取json数组转换成对象数组
            this.manager.saveOrUpdateList(placeBuildUpTypeList);
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * 
     * <li>说明：删除组成位置关系，级联删除与之关联的结构组成位置
     * <li>创建人：程锐
     * <li>创建日期：2013-1-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    @SuppressWarnings("all")
    public void deleteStructure() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String[] errMsg = this.manager.validateDelete(ids);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.deleteStructure(ids);
                map.put("success", true);
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
     * 
     * <li>说明：删除组成位置关系，级联删除与之关联的安装位置或虚拟位置
     * <li>创建人：程锐
     * <li>创建日期：2013-4-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    @SuppressWarnings("all")
    public void deletePlace() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String[] errMsg = this.manager.validateDelete(ids);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.deletePlace(ids);
                map.put("success", true);
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
     * 
     * <li>说明：复制标准组成
     * <li>创建人：程锐
     * <li>创建日期：2013-2-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void copyBuildUp() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String typeIDX = getRequest().getParameter("typeIDX");//车型主键或配件类型主键
            int type = Integer.parseInt(getRequest().getParameter("type"));//组成类型
            String buildUpTypeIDX = getRequest().getParameter("buildUpTypeIDX");//组成主键
            String[] errMsg = this.manager.validateHasDefaultBuild(typeIDX, type, buildUpTypeIDX);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveBuildUpByDefault(typeIDX, type, buildUpTypeIDX);
                map.put("success", true);
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
}