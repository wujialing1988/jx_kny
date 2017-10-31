package com.yunda.freight.zb.gztp.webservice;

import com.yunda.jx.pjjx.webservice.IService;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 故障登记webservice接口
 * <li>创建人：何东
 * <li>创建日期：2017-4-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public interface IGztpService extends IService {
    
    /**
     * <li>说明：查询故障登记列表数据
     * <li>创建人：何东
     * <li>创建日期：2017-4-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 参考接口文档《JX_KNY_V1.0内部接口规范》2.5.2.1 查询故障登记集合
     * @return
     */
    public String findGztpList(String jsonObject);
    
    /**
     * <li>说明：保存/修改故障登记
     * <li>创建人：何东
     * <li>创建日期：2017-4-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 参考接口文档《JX_KNY_V1.0内部接口规范》2.5.2.2 保存/修改故障登记
     * @return
     */
    public String saveOrUpdate(String jsonObject);

    /**
     * <li>说明：删除故障登记
     * <li>创建人：何东
     * <li>创建日期：2017-4-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 参考接口文档《JX_KNY_V1.0内部接口规范》2.5.2.3 删除故障登记
     * @return
     */
    public String delete(String jsonObject);
    
    /**
     * <li>说明：获取车辆构型下拉树数据
     * <li>创建人：何东
     * <li>创建日期：2017-4-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 参考接口文档《JX_KNY_V1.0内部接口规范》2.5.2.4 获取车辆构型下拉树数据
     * @return
     */
    public String getJcgxBuildComboTree(String jsonObject);
    
    /**
     * <li>说明：获取车辆范围活下拉树数据
     * <li>创建人：何东
     * <li>创建日期：2017-4-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 参考接口文档《JX_KNY_V1.0内部接口规范》2.5.2.5 获取车辆范围活下拉树数据
     * @return
     */
    public String getScopeWorkComboTree(String jsonObject);
    
    /**
     * <li>说明：获取物料清单列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-7-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject{
            entityJson: {
                matCodes:"8a8284c35b233e87015b23416fb40002,8a8284c35b233e87015b23416fb40003,8a8284c35b233e87015b23416fb40004"
            },
            start:1,
            limit:50, 
            orders:[{
                sort: "seqNum",
                dir: "ASC"
            }]
       }
     * @return
     */
    public String findMatTypeList(String jsonObject);
    
    /**
     * <li>说明：获取物料消耗列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-7-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject{
            entityJson: {
                gztpIdx:"8a8284c35b233e87015b23416fb40002"
            },
            start:1,
            limit:50, 
            orders:[{
                sort: "seqNum",
                dir: "ASC"
            }]
       }
     * @return
     */
    public String findMatTypeUse(String jsonObject);
}
