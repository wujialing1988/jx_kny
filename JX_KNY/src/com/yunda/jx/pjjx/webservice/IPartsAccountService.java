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
public interface IPartsAccountService extends IService{
    
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
     */
    public String findPageForRepair(String jsonObject) throws IOException;
}
