/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: TODO
 * <li>创建人：何涛
 * <li>创建日期：2016-3-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */

package com.yunda.jx.pjjx.webservice;

import java.io.IOException;

import javax.annotation.Resource;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.webservice.common.entity.OperateReturnMessage;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配件周转信息台帐接口
 * <li>创建人：何涛
 * <li>创建日期：2016-3-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("partsAccountService")
public class PartsAccountService implements IPartsAccountService {
    
    /** 配件检修作业节点查询业务类 */
    @Resource
    private PartsAccountManager partsAccountManager;
    
    /**
     * <li>说明：分页查询，根据当前系统操作人员，查询当前班组可以修理的配件列表
     * <li>创建人：何涛
     * <li>创建日期：2016-3-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException
     * 
     * @param jsonObject {
        operatorId: "800109",
        start:0,
        limit:50, 
        orders:[{
            sort: "updateTime",
            dir: "DESC"
        }],
        entityJson: {
            unloadTrainType: "HXD3C 0001",
            identificationCode: "PJ-20151102004",
            partsNo: "20151102004",
            partsTypeIDX: "402886814ce249bf014ce4dfb082019d"
        }
     }
     @return 配件周转信息分页列表
     */
    @Override
    public String findPageForRepair(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        // 查询记录开始索引
        int start = jo.getIntValue(Constants.START);
        // 查询记录条数
        int limit = jo.getIntValue(Constants.LIMIT);
        start = limit * (start - 1);
        
        // 排序字段
        JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
        Order[] orders = JSONTools.getOrders(jArray);
        
        // 查询条件封装实体
        String entityJson = StringUtil.nvl(jo.getString(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
        PartsAccount entity = JSONUtil.read(entityJson, PartsAccount.class);
        
        SearchEntity<PartsAccount> searchEntity = new SearchEntity<PartsAccount>(entity, start, limit, orders);
        Page<PartsAccount> page = partsAccountManager.findPageForRepair(searchEntity, true);
        return JSONTools.toJSONList(page);
    }

}
