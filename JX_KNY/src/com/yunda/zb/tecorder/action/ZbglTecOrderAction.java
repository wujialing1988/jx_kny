package com.yunda.zb.tecorder.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.zb.tecorder.entity.ZbglTecOrder;
import com.yunda.zb.tecorder.manager.ZbglTecOrderManager;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglTecOrder控制器, 技术指令及措施
 * <li>创建人：王利成
 * <li>创建日期：2015-02-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class ZbglTecOrderAction extends JXBaseAction<ZbglTecOrder, ZbglTecOrder, ZbglTecOrderManager>{
    /**
     * 日志工具
     */
    Logger logger = Logger.getLogger(this.getClass());
    
    /**
     * <li>说明：发布
     * <li>创建人：王利成
     * <li>创建日期：2015-3-2 下午02:34:24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void release() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            if(this.manager.release(ids) > 0){
                map.put(Constants.SUCCESS, true);
            }else{
                map.put(Constants.ERRMSG, "数据变更或已发布");
            }
        }catch(Exception e){
            ExceptionUtil.process(e, logger, map);
        }finally{
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：销号
     * <li>创建人：王利成
     * <li>创建日期：2015-3-2 下午02:34:24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void cancel() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            if(this.manager.cancel(ids) > 0){
                map.put(Constants.SUCCESS, true);
            }else{
                map.put(Constants.ERRMSG, "数据变更或未发布");
            }
        }catch(Exception e){
            ExceptionUtil.process(e, logger, map);
        }finally{
            JSONUtil.write(getResponse(), map);
        }
    }
    /**
     * <li>说明：技术措施处理信息查询 
     * <li>创建人：王利成
     * <li>创建日期：2015-3-3 上午11:24:46
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findTecInfo() throws JsonMappingException, IOException{
        
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String zbglTecIdx = getRequest().getParameter("zbglTecIdx");
            map.put("list", this.manager.findList(zbglTecIdx));
            map.put(Constants.SUCCESS, true);
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }

    /**
     * <li>说明：技术措施生成临时任务单 
     * <li>创建人：王利成
     * <li>创建日期：2015-3-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void createLsRdp() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
           // String zbglTecIdx = getRequest().getParameter("zbglTecIdx");
            String optInfo = this.manager.saveOrUpdateLsRdps(ids);
            map.put("msg", optInfo);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            map.put("msg", e.getMessage());
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}