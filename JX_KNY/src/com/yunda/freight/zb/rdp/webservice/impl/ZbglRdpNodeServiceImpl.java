package com.yunda.freight.zb.rdp.webservice.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.freight.zb.rdp.webservice.IZbglRdpNodeService;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpNode;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpNodeManager;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 列检节点接口实现类
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-4-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("zbglRdpNodeServiceWS")
public class ZbglRdpNodeServiceImpl implements IZbglRdpNodeService {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass());
    
    /** 列检节点服务 **/
    @Resource
    private ZbglRdpNodeManager zbglRdpNodeManager ;
    
    /**
     * <li>说明：查询任务节点列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            entityJson: {
                rdpIDX:"8a8284c35b276ca1015b2789390c0010",
                nodeName:""
            },
            start:1,
            limit:50, 
            orders:[{
                sort: "seqNo",
                dir: "ASC"
            }]
       }
     * @return
     * @throws IOException
     */
    public String findZbglRdpNodeList(String jsonObject) throws IOException {
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            
            // 获取查询条件实体对象
            String entityJson = jo.getString(Constants.ENTITY_JSON);
            ZbglRdpNode entity = JSONUtil.read(entityJson, ZbglRdpNode.class);
            
            // 查询工艺开始索引
            int start = jo.getIntValue(Constants.START);
            
            // 查询工艺条数
            int limit = jo.getIntValue(Constants.LIMIT);
            start = limit * (start - 1);
            
            // 排序字段
            JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
            List<Order> orderList = new ArrayList<Order>();
            if (null != jArray) {
                Order[] orders = JSONTools.getOrders(jArray);
                for (Order order : orders) {
                    orderList.add(order);
                }
            }
            
            // 查询条件
            List<Condition> whereList = new ArrayList<Condition>();
            Condition con = null;
            // 查询条件 - 整备任务单
            if(!StringUtil.isNullOrBlank(entity.getRdpIDX())){ 
                con = new Condition("rdpIDX", Condition.EQ, entity.getRdpIDX());
                con.setStringLike(false);
                whereList.add(con);
            }
            QueryCriteria<ZbglRdpNode> query = new QueryCriteria<ZbglRdpNode>(ZbglRdpNode.class, whereList, orderList, start, limit);
            Page<ZbglRdpNode> page = this.zbglRdpNodeManager.findPageList(query);
            List<ZbglRdpNode> list = page.getList();
            if (null == list || list.size() <= 0) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
            }
            return JSONTools.toJSONList(page.getTotal(), list);
        }catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
}
