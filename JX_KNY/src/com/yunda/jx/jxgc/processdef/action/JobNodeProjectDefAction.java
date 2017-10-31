package com.yunda.jx.jxgc.processdef.action; 

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.jx.jxgc.processdef.entity.JobNodeProjectDef;
import com.yunda.jx.jxgc.processdef.manager.JobNodeProjectDefManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JobNodeProjectDef控制器
 * <li>创建人：何涛
 * <li>创建日期：2015-4-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class JobNodeProjectDefAction extends JXBaseAction<JobNodeProjectDef, JobNodeProjectDef, JobNodeProjectDefManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    
//    /**
//     * <li>说明：批量保存新增的【作业节点所挂作业项目】
//     * <li>创建人：何涛
//     * <li>创建日期：2015-04-15
//     * <li>修改人：
//     * <li>修改日期：
//     * <li>修改内容：
//     * 
//     * @throws JsonMappingException
//     * @throws IOException
//     */
//    public void save() throws JsonMappingException, IOException {
//        Map<String, Object> map = new HashMap<String,Object>();
//        try {
//            JobNodeProjectDef[] array = JSONUtil.read(getRequest(), JobNodeProjectDef[].class);
//            String[] errMsg = this.manager.validateUpdate(array);
//            if (errMsg == null || errMsg.length < 1) {
//                List<JobNodeProjectDef> entityList = new ArrayList<JobNodeProjectDef>();
//                for (JobNodeProjectDef jnpd : array) {
//                    if (null != jnpd) {
//                        entityList.add(jnpd);
//                    }
//                }
//                this.manager.saveOrUpdate(entityList);
//                map.put(Constants.SUCCESS, true);
//            } else {
//                map.put(Constants.SUCCESS, false);
//                map.put(Constants.ERRMSG, errMsg);
//            }           
//        } catch (Exception e) {
//            ExceptionUtil.process(e, logger, map);
//        } finally {
//            JSONUtil.write(this.getResponse(), map);
//        }       
//    }
//    
//    /**
//     * <li>说明：批量删除【作业节点所挂作业项目】
//     * <li>创建人：何涛
//     * <li>创建日期：2015-04-15
//     * <li>修改人：
//     * <li>修改日期：
//     * <li>修改内容：
//     * 
//     * @throws JsonMappingException
//     * @throws IOException
//     */
//    public void delete() throws JsonMappingException, IOException {
//        Map<String, Object> map = new HashMap<String,Object>();
//        try {
//            HttpServletRequest req = getRequest();
//            String nodeIDX = req.getParameter("nodeIDX");
//            String[] projectIdxs = JSONUtil.read(req, String[].class);
//            this.manager.logicDelete(nodeIDX, projectIdxs);
//        } catch (Exception e) {
//            ExceptionUtil.process(e, logger, map);
//        } finally {
//            JSONUtil.write(this.getResponse(), map);
//        }       
//    }
	
}