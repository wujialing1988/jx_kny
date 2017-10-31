package com.yunda.jx.jxgc.processdef.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.processdef.entity.JobNodeUnionWorkSeq;
import com.yunda.jx.jxgc.processdef.manager.JobNodeUnionWorkSeqManager;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: JobNodeUnionWorkSeqAction 控制器
 * <li>创建人：林欢
 * <li>创建日期：2016-6-4
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
public class JobNodeUnionWorkSeqAction extends JXBaseAction< JobNodeUnionWorkSeq,  JobNodeUnionWorkSeq,  JobNodeUnionWorkSeqManager>{
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：批量保存新增的【作业节点所挂记录卡】
     * <li>创建人：林欢
     * <li>创建日期：2016-6-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @throws JsonMappingException
     * @throws IOException
     */
    public void save() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            //获取前台传递过来的工单数组中间对象
            JobNodeUnionWorkSeq[] array = JSONUtil.read(getRequest(), JobNodeUnionWorkSeq[].class);
            
            //判断该节点下，是否有作业工单挂钩
            String[] errMsg = this.manager.validateUpdate(array);

            if (errMsg == null || errMsg.length < 1) {
                List<JobNodeUnionWorkSeq> entityList = new ArrayList<JobNodeUnionWorkSeq>();
                for (JobNodeUnionWorkSeq jnpd : array) {
                    if (null != jnpd) {
                        entityList.add(jnpd);
                    }
                }
                this.manager.saveOrUpdate(entityList);
                map.put(Constants.SUCCESS, true);
            } else {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, errMsg);
            }           
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * <li>说明：批量删除【作业节点所挂记录卡】
     * <li>创建人：林欢
     * <li>创建日期：2016-6-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @throws JsonMappingException
     * @throws IOException
     */
    public void deleteJobNodeUnionWorkSeqByRecordCardIDX() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String nodeIDX = req.getParameter("nodeIDX");
            String[] idx = JSONUtil.read(req, String[].class);
            this.manager.deleteJobNodeUnionWorkSeqByRecordCardIDX(idx,nodeIDX);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
}
