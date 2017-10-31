package com.yunda.webservice.makecard.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.EmployeeManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 制卡、刷卡业务实现类
 * <li>创建人：汪东良
 * <li>创建日期：2014-9-19
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "makeCardServiceManager")
public class MakeCardServiceManager {
    
    @Autowired
    private EmployeeManager employeeManager;
    
    /**
     * <li>方法名：updateMakeCard
     * <li>说明：绑定卡片编号
     * <li>创建人：汪东良
     * <li>创建日期：2014-9-19
     * <li>修改人：何涛
     * <li>修改日期：2015-12-23
     * <li>修改内容：因为employeeManager.saveOrUpdate()方法被重新，重写后的方法涉及到岗位数据更改，所以此次不应调用该方法进行保存，修改为employeeManager.save()方法
     * @param cardNo 卡号
     * @param ome 用户信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    public void updateMakeCard(String cardNo, OmEmployee ome) throws BusinessException, NoSuchFieldException {
        // 将卡号与最新的用户绑定
        if (!cardNo.equals(ome.getCardNum())) {
            ome.setCardNum(cardNo);
            employeeManager.save(ome);
        }
        
        // 取消之前的卡号绑定信息
        List<OmEmployee> list =
            employeeManager.getDaoUtils().find("From OmEmployee Where cardNum = ? And userid <> ?", new Object[] { cardNo, ome.getUserid() });
        if (null == list || 0 >= list.size()) {
            return;
        }
        // 如果此卡号已经绑定，则循环将绑定清除；
        for (OmEmployee emp : list) {
            emp.setCardNum(null);
        }
        this.employeeManager.saveOrUpdate(list);
    }
    
}
