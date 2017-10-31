package com.yunda.jx.pjjx.partsrdp.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdpNodeMat;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpNodeMatManager;
import com.yunda.jx.pjwz.partsBase.entity.MatTypeList;
import com.yunda.jx.pjwz.partsBase.manager.MatTypeListManager;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 物料消耗action
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-10-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public class PartsRdpNodeMatAction extends JXBaseAction<PartsRdpNodeMat, PartsRdpNodeMat, PartsRdpNodeMatManager> {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    @Resource
    private MatTypeListManager matTypeListManager;
    
    @Resource
    private PartsRdpNodeMatManager partsRdpNodeMatManager;
    
    /**
     * <li>说明：查询物料信息列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void pageListForMatList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String entityJson = StringUtil.nvlTrim(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
            MatTypeList entity = JSONUtil.read(entityJson, MatTypeList.class);
            // 节点
            String rdpNodeIDX = req.getParameter("rdpNodeIDX");
            if(null != rdpNodeIDX && !"".equals(rdpNodeIDX)){
                String rdpNodeStr = " select matCode from PartsRdpNodeMat where rdpNodeIDX = '"+ rdpNodeIDX + "' ";
                entity.setRdpNodeStr(rdpNodeStr);
            }     
            SearchEntity<MatTypeList> searchEntity = new SearchEntity<MatTypeList>(entity, getStart(), getLimit(), getOrders());
            map = this.matTypeListManager.findPageList(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：新增物料信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveNodeMatList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            PartsRdpNodeMat[] partsRdpNodeMat = JSONUtil.read(getRequest(), PartsRdpNodeMat[].class);
            this.partsRdpNodeMatManager.saveNodeMatList(partsRdpNodeMat);
            map.put(Constants.SUCCESS, "true");
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * <li>说明：添加列表查询方法
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findListForMat() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String entityJson = StringUtil.nvlTrim(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
            PartsRdpNodeMat entity = JSONUtil.read(entityJson, PartsRdpNodeMat.class);
            SearchEntity<PartsRdpNodeMat> searchEntity = new SearchEntity<PartsRdpNodeMat>(entity, getStart(), getLimit(), getOrders());
            map = this.partsRdpNodeMatManager.findPageForMat(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    

}
