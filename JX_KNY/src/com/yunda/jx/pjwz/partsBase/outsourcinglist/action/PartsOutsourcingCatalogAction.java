package com.yunda.jx.pjwz.partsBase.outsourcinglist.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjwz.partsBase.outsourcinglist.entity.PartsOutsourcingCatalog;
import com.yunda.jx.pjwz.partsBase.outsourcinglist.manager.PartsOutsourcingCatalogManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsOutsourcingCatalog控制器, 配件委外修目录
 * <li>创建人：程梅
 * <li>创建日期：2016年7月19日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
public class PartsOutsourcingCatalogAction extends JXBaseAction<PartsOutsourcingCatalog, PartsOutsourcingCatalog, PartsOutsourcingCatalogManager> {

    private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    
    /**
     * 
     * <li>说明：新增委外修目录
     * <li>创建人：程梅
     * <li>创建日期：2016-7-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param catalogs
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void newList() throws JsonMappingException, IOException{
        
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            PartsOutsourcingCatalog[] catalogList = (PartsOutsourcingCatalog[]) JSONUtil.read(getRequest(), PartsOutsourcingCatalog[].class);
            this.manager.saveNewList(catalogList);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * 
     * <li>说明：设置委修厂家
     * <li>创建人：程梅
     * <li>创建日期：2016-7-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void setMadeFactory() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String idx = req.getParameter("idxs");
            String factoryId = req.getParameter("factoryId");
            String factoryName = req.getParameter("factoryName");
            if(manager.updateSetMadeFactory(idx, factoryId, factoryName) > 0)
                map.put(Constants.SUCCESS, true);
            else
                map.put("errMsg", "设置委修厂家失败!");
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * 
     * <li>说明：设置送出周期
     * <li>创建人：程梅
     * <li>创建日期：2016-7-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void setOutCycle() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String idx = req.getParameter("idxs");
            String outCycle = req.getParameter("outCycle");
            if(manager.updateSetOutCycle(idx, outCycle) > 0)
                map.put(Constants.SUCCESS, true);
            else
                map.put("errMsg", "设置送出周期失败!");
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * 
     * <li>说明：设置检修周期
     * <li>创建人：程梅
     * <li>创建日期：2016-7-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void setRepairCycle() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String idx = req.getParameter("idxs");
            String repairCycle = req.getParameter("repairCycle");
            if(manager.updateSetRepairCycle(idx, repairCycle) > 0)
                map.put(Constants.SUCCESS, true);
            else
                map.put("errMsg", "设置检修周期失败!");
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
