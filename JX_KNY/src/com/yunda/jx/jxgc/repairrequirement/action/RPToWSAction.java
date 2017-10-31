package com.yunda.jx.jxgc.repairrequirement.action; 

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.jx.jxgc.repairrequirement.entity.RPToWS;
import com.yunda.jx.jxgc.repairrequirement.manager.RPToWSManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：RPToWS控制器, 检修项目工序设置
 * <li>创建人：王治龙
 * <li>创建日期：2012-12-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class RPToWSAction extends JXBaseAction<RPToWS, RPToWS, RPToWSManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
//    /**
//     * <li>说明：批量新增检修项目对应的作业工单
//     * <li>创建人：王治龙
//     * <li>创建日期：2012-12-27
//     * <li>修改人：
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param 
//     * @return void
//     * @throws Exception
//     */ 
//    public void saveOrUpdateList() throws Exception{
//        Map<String, Object> map = new HashMap<String,Object>();
//        try {
//            RPToWS[] seqs = (RPToWS[])JSONUtil.read(getRequest(), RPToWS[].class);
//            String[] errMsg = this.manager.saveOrUpdateList(seqs); 
//            if (errMsg == null || errMsg.length < 1) {
////              返回记录保存成功的实体对象
//                map.put("success", true);
//            } else {
//                map.put("success", false);
//                map.put("errMsg", errMsg);
//            }
//        } catch (Exception e) {
//        	ExceptionUtil.process(e, logger, map);
//        } finally {
//            JSONUtil.write(this.getResponse(), map);
//        }       
//    }
}