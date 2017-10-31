package com.yunda.jx.pjwz.partsBase.outsourcinglist.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjwz.partsBase.outsourcinglist.entity.PartsOutsourcingList;
import com.yunda.jx.pjwz.partsBase.outsourcinglist.manager.PartsOutsourcingListManager;


public class PartsOutsourcingListAction extends JXBaseAction<PartsOutsourcingList, PartsOutsourcingList, PartsOutsourcingListManager> {

    private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * /jsp/jx/pjwz/partbase/OutsourcingDir.js调用
     * <li>方法说明：查询委外修配件列表 
     * <li>方法名称：findPageList
     * <li>@throws Exception
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-10-23 上午10:36:57
     * <li>修改人：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    public void findPageList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            map = this.manager.findPageDataList(req.getParameter("entityJson"), getStart(), getLimit(), getOrders()).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * /jsp/jx/pjwz/partbase/OutsourcingDir.js调用
     * <li>方法说明：新增委外配件型号目录 
     * <li>方法名称：newList
     * <li>@throws JsonMappingException
     * <li>@throws IOException
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-11-27 下午04:15:28
     * <li>修改人：
     * <li>修改内容：
     */
    public void newList() throws JsonMappingException, IOException{
        
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String idx = req.getParameter("idxs");
            if(manager.saveNewList(idx) > 0)
                map.put("success", true);
            else
                map.put("errMsg", "新增失败!");
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * /jsp/jx/pjwz/partbase/OutsourcingDir.js调用
     * <li>方法说明：设置生产厂家 
     * <li>方法名称：setMadeFactory
     * <li>@throws JsonMappingException
     * <li>@throws IOException
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-11-27 下午04:35:17
     * <li>修改人：
     * <li>修改内容：
     */
    public void setMadeFactory() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String idx = req.getParameter("idxs");
            String factory = req.getParameter("factory");
            if(manager.updateSetMadeFactory(idx, factory) > 0)
                map.put("success", true);
            else
                map.put("errMsg", "新增失败!");
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
