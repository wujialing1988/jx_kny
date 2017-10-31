package com.yunda.zb.pczz.action; 

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.zb.pczz.entity.ZbglPczzItem;
import com.yunda.zb.pczz.entity.ZbglPczzItemToTraininfo;
import com.yunda.zb.pczz.manager.ZbglPczzItemManager;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglPczzItem控制器, 普查整治项
 * <li>创建人：王利成
 * <li>创建日期：2015-03-04
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class ZbglPczzItemAction extends JXBaseAction<ZbglPczzItem, ZbglPczzItem, ZbglPczzItemManager>{
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：逻辑删除普查整治任务项
     * <li>创建人：林欢
     * <li>创建日期：2016-6-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void deleteZbglPczzItem() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            this.manager.deleteZbglPczzItem(ids);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * <li>标题: 机车整备管理信息系统
     * <li>说明: 构造车型主键和车型接收类
     * <li>创建人：王利成
     * <li>创建日期：2015-3-6
     * <li>修改人: 
     * <li>修改日期：
     * <li>修改内容：
     * <li>版权: Copyright (c) 2008 运达科技公司
     * @author 信息系统事业部整备系统项目组
     * @version 1.0
     */
    @JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
    public static class TrainType implements Serializable {
        
        private String trainTypeIDX;
        
        private String trainTypeShortName;
        
        public String getTrainTypeIDX() {
            return trainTypeIDX;
        }
        
        public void setTrainTypeIDX(String trainTypeIDX) {
            this.trainTypeIDX = trainTypeIDX;
        }
        
        public String getTrainTypeShortName() {
            return trainTypeShortName;
        }
        
        public void setTrainTypeShortName(String trainTypeShortName) {
            this.trainTypeShortName = trainTypeShortName;
        }
    }
}
