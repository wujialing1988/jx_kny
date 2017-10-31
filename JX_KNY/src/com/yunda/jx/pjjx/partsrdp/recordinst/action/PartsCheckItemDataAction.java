package com.yunda.jx.pjjx.partsrdp.recordinst.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsCheckItemData;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsCheckItemDataManager;
import com.yunda.jx.pjjx.util.HttpClientUtils;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: PartsCheckItemData控制器, 可视化数据采集结果
 * <li>创建人：林欢
 * <li>创建日期：2016-6-12
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：占时取消该方法和可视化对接的方案，如果以后需要使用，需要调整
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
public class PartsCheckItemDataAction extends JXBaseAction<PartsCheckItemData, PartsCheckItemData, PartsCheckItemDataManager>{
    
//  通过资源文件获取url
    private static String url;
    
    private static final String PARTSCHECKITEMDATA = "partsCheckItemData";
    
    static {
        url = HttpClientUtils.instence.getProperties().getProperty(PARTSCHECKITEMDATA).trim();
        if (StringUtil.isNullOrBlank(url)) {
            throw new BusinessException("未读取到检测项数据配置项，请检查webservice.properties文件是否正确！");
        }
        
    }
    
	private static final long serialVersionUID = 1L;
	/** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：动态通过http获取可视化检测结果数据，并且同步更新到对应的检测项中
     * <li>创建人：林欢
     * <li>创建日期：2016-06-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException
     */
    public void synPartsCheckItemDataAndSavePartsRdpRecordDI() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Constants.SUCCESS, false);
        map.put(Constants.ERRMSG, "可视化系统请求失败！");
        try {
            
            //获取基本http查询条件
            
            //获取配件检计划idx
            String rdpRecordCardRdpIDX = this.getRequest().getParameter("rdpRecordCardRdpIDX");
            
            map = this.manager.doSyn(rdpRecordCardRdpIDX,map,url);
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
}
