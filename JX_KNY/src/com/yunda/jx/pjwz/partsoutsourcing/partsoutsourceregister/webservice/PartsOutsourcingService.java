
package com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.webservice;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.entity.PartsOutsourcing;
import com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.entity.PartsOutsourcingBack;
import com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.entity.PartsOutsourcingOut;
import com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.manager.PartsOutsourcingManager;
import com.yunda.util.BeanUtils;
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
@Service("partsOutsourcingWS")
public class PartsOutsourcingService implements IPartsOutsourcingService {
    
    /** 配件委外登记业务类 */
    @Resource
    private PartsOutsourcingManager partsOutsourcingManager;
    /**
     * 
     * <li>说明：查询配件委外登记记录
     * <li>创建人：程梅
     * <li>创建日期：2016-6-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 已委外配件JSON数组
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public String getOutPartsOutsourcing() throws IOException, IllegalAccessException, InvocationTargetException {
        List<PartsOutsourcing> list = partsOutsourcingManager.getOutPartsOutsourcing();
        List<PartsOutsourcingOut> outList = new ArrayList<PartsOutsourcingOut>();
        for(PartsOutsourcing o : list){
            PartsOutsourcingOut out = new PartsOutsourcingOut();
            BeanUtils.copyProperties(out, o);
            outList.add(out);
        }
        return JSONTools.toJSONList(outList);
    }
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
    public String getBackPartsOutsourcing() throws IOException, IllegalAccessException, InvocationTargetException {
        List<PartsOutsourcing> list = partsOutsourcingManager.getBackPartsOutsourcing();
        List<PartsOutsourcingBack> backList = new ArrayList<PartsOutsourcingBack>();
        for(PartsOutsourcing o : list){
            PartsOutsourcingBack back = new PartsOutsourcingBack();
            BeanUtils.copyProperties(back, o);
            backList.add(back);
        }
        return JSONTools.toJSONList(backList);
    }
}
