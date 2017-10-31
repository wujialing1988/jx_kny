package com.yunda.jx.jxgc.workplanmanage.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.order.AbstractOrderAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.workplanmanage.entity.WSGroupItem;
import com.yunda.jx.jxgc.workplanmanage.manager.WSGroupItemManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WSGroupItem控制器, 工位组明细
 * <li>创建人：何涛
 * <li>创建日期：2015-4-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class WSGroupItemAction extends AbstractOrderAction<WSGroupItem, WSGroupItem, WSGroupItemManager> {
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：批量保存新增的【工位】
     * <li>创建人：何涛
     * <li>创建日期：2015-04-27
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
            WSGroupItem[] array = JSONUtil.read(getRequest(), WSGroupItem[].class);
            for (WSGroupItem item : array) {
                String[] errMsg = this.manager.validateUpdate(item);
                if (null == errMsg || errMsg.length <= 0) {
                    this.manager.saveOrUpdate(item);
                } else {
                    map.put(Constants.SUCCESS, false);
                    map.put(Constants.ERRMSG, errMsg);
                }
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
}
