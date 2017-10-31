package com.yunda.jx.pjjx.base.wpdefine.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.jx.pjjx.base.wpdefine.entity.WPMat;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 配件检修用料定义业务类
 * <li>创建人：张迪
 * <li>创建日期：2016-8-31
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="wPMatManager")
public class WPMatManager extends JXBaseManager<WPMat, WPMat>{
    
    /**
     * <li>说明：批量保存 配件检修用料
     * <li>创建人：陈志刚
     * <li>创建日期：2016-9-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param list 配件检修所需物料集合
     * @return String[]保存的验证消息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public String[] saveMatInfor(WPMat[] list) throws BusinessException, NoSuchFieldException {
        List<WPMat> entityList = new ArrayList<WPMat>();
        for(WPMat mat : list) {
            // 验证”物料编码“是否唯一
            String[] msg = this.validateUpdate(mat);
            if (null != msg) {
                return msg;
            }
            entityList.add(mat);
        }
        if (entityList.size() > 0) {
            this.saveOrUpdate(entityList);
        }
        return null;
    }
    
    /**
     * <li>说明：更新保存检修用料信息
     * <li>创建人：陈志刚
     * <li>创建日期：2016-9-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jnmd
     * @return String[]更新的提示信息
     * @throws NoSuchFieldException
     */
    public String[] matUpdate(WPMat[] jnmd) throws NoSuchFieldException{            
            if(jnmd.length==0){
                return new String[]{"未进行数据修改"};
            }          
            for (WPMat t : jnmd) {
                t = EntityUtil.setSysinfo(t);
                //设置逻辑删除字段状态为未删除
                t = EntityUtil.setNoDelete(t);
               this.saveOrUpdate(t);
            }           
            return null;
        }    
    }