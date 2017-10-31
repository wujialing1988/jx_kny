package com.yunda.jx.jczl.undertakemanage.action; 

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jczl.undertakemanage.entity.UndertakeTrainType;
import com.yunda.jx.jczl.undertakemanage.manager.UndertakeTrainTypeManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：UndertakeTrainType控制器, 承修车型
 * <li>创建人：王治龙
 * <li>创建日期：2012-10-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class UndertakeTrainTypeAction extends JXBaseAction<UndertakeTrainType, UndertakeTrainType, UndertakeTrainTypeManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
    /**
     * <li>说明：单表分页查询承修车型对应修程，返回单表分页查询记录的json
     * <li>创建人：王治龙
     * <li>创建日期：2012-10-31
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */ 
    public void pageList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            map = this.manager.findPageLinkList(searchJson, getStart(), getLimit(), getOrders()).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：整备产品化整备范围机车车型查询方法
     * <li>创建人：王利成
     * <li>创建日期：2015-4-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void pageListForZB() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        try{
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"),"{}");
            entity = (UndertakeTrainType)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<UndertakeTrainType> searchEntity = new SearchEntity<UndertakeTrainType>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findTrainForZBList(searchEntity, getStart(), getLimit(), getOrders()).extjsStore();
        }catch(Exception e){
            ExceptionUtil.process(e, logger,map);
        }finally{
            JSONUtil.write(this.getResponse(),map);
        }
    }
    /**
     * <li>说明：接受批量保存或更新记录请求，向客户端返回操作结果（JSON格式），实体类对象必须符合检修系统表设计，主键名必须为idx（字符串uuid）
     * <li>创建人：王治龙
     * <li>创建日期：2012-10-31
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */ 
    public void saveOrUpdateList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            UndertakeTrainType[] objList = (UndertakeTrainType[])JSONUtil.read(getRequest(), UndertakeTrainType[].class);
            String[] errMsg = this.manager.saveOrUpdateList(objList);
            if (errMsg == null || errMsg.length < 1) {
                map.put("success", "true");
            }else{
                map.put("errMsg", errMsg);
            }
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
}