package com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.webservice;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.yunda.jx.pjjx.webservice.IService;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配件委外接口【与物资配送系统对接时使用】
 * <li>创建人：程梅
 * <li>创建日期：2016-6-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public interface IPartsOutsourcingService extends IService{
    /**
     * 
     * <li>说明：查询配件委外登记记录
     * <li>创建人：程梅
     * <li>创建日期：2016-6-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 已委外配件JSON数组
     */
    public String getOutPartsOutsourcing() throws IOException, IllegalAccessException, InvocationTargetException;
    /**
     * 
     * <li>说明：查询配件委外已返回登记记录
     * <li>创建人：程梅
     * <li>创建日期：2016-6-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 已返回配件JSON数组
     */
    public String getBackPartsOutsourcing() throws IOException, IllegalAccessException, InvocationTargetException;
}
