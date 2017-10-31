package com.yunda.jx.pjjx.partsrdp.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjjx.partsrdp.entity.DetectItemSearch;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdpUnionDevice;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdpUnionDeviceSearch;
import com.yunda.jx.pjjx.partsrdp.manager.DetectItemSearchManager;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpUnionDeviceSearchManager;

/**
 * <li>说明： 设备关联配件兑现单
 * <li>创建人： 张凡
 * <li>创建日期：2015年11月21日
 * <li>成都运达科技股份有限公司
 */
public class PartsRdpUnionDeviceAction extends JXBaseAction<PartsRdpUnionDevice, PartsRdpUnionDevice, PartsRdpUnionDeviceSearchManager> {
    
    Logger logger = Logger.getLogger(this.getClass());
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Resource
    private DetectItemSearchManager detectItemSearchManager;

    /**
     * <li>方法说明： 查询配件兑现单与设备上传数据关联
     * <li>方法名：findPageList
     * @throws JsonMappingException
     * @throws IOException
     * <li>创建人： 张凡
     * <li>创建日期：2015年11月21日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public void findPageList() throws JsonMappingException, IOException{
        Map<String, Object> map = null;
        try{
            PartsRdpUnionDeviceSearch device = null;
            device = JSONUtil.read(getRequest().getParameter("searchJson"), PartsRdpUnionDeviceSearch.class);
            map = manager.findPageList(getStart(), getLimit(), getOrders(), device).extjsStore();
        } catch(Exception e) {
            map = new HashMap<String, Object>();
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>方法说明： 查询检测项与工单关联数据
     * <li>方法名：findDetectItemList
     * @throws JsonMappingException
     * @throws IOException
     * <li>创建人： 张凡
     * <li>创建日期：2015年11月21日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public void findDetectItemList() throws JsonMappingException, IOException{
        Map<String, Object> map = null;
        try{
            DetectItemSearch detect = null;
            detect = JSONUtil.read(getRequest().getParameter("searchJson"), DetectItemSearch.class);
            map = detectItemSearchManager.findPageList(getStart(), getLimit(), getOrders(), detect).extjsStore();
        } catch(Exception e) {
            map = new HashMap<String, Object>();
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
}
