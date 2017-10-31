package com.yunda.zb.tp.manager;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.zb.tp.entity.ZbglTp;
import com.yunda.zb.tp.entity.ZbglTpRepair;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglTpRepair业务类
 * <li>创建人：刘国栋
 * <li>创建日期：2016-08-08
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "zbglTpRepairManager")
public class ZbglTpRepairManager extends JXBaseManager<ZbglTpRepair, ZbglTpRepair>{

    /** 整备提票业务类 **/
    @Resource
    private ZbglTpManager zbglTpManager ;
    
    /**
     * <li>说明：保存提票返修
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 提票返修实体
     */
    public void saveTpRepair(ZbglTpRepair entity) throws BusinessException, NoSuchFieldException {
        // 查询该提票已有的提票返修信息，并将【返修状态】修改为返修完成
        if(entity == null || StringUtil.isNullOrBlank(entity.getJt6IDX())){
            return ;
        }
        //获得提票对象
        ZbglTp tp = zbglTpManager.getModelById(entity.getJt6IDX());
        
        int times = 0 ; // 返修次数
        List<ZbglTpRepair> repairs = this.getRepairByJt6IDX(entity.getJt6IDX());
        times = repairs == null ? 0 : repairs.size() + 1 ;
        for (ZbglTpRepair repair : repairs) {
            repair.setStatus(ZbglTpRepair.STATUS_COMPLETE);
        }
        this.saveOrUpdate(repairs);
        this.daoUtils.flush();
        // 保存提票返修信息
        entity.setStatus(ZbglTpRepair.STATUS_ONGOING);
        entity.setRepairTimes(times);
        entity.setFaultFixFullName(tp.getFaultFixFullName());
        entity.setFaultReason(tp.getFaultReason());
        entity.setMethodDesc(tp.getMethodDesc());
        entity.setProfessionalTypeName(tp.getProfessionalTypeName());
        entity.setRepairDesc(tp.getRepairDesc());
        entity.setCheckTime(DateUtil.yyyy_MM_dd_HH_mm_ss.format(new Date()));//检查时间
        this.save(entity);
        // 修改提票状态为【待销活】
        
        if(tp != null){
            tp.setFaultNoticeStatus(ZbglTp.STATUS_OPEN);      
            tp.setRepairTimes(times);
            tp.setHandlePersonId(null); 
            zbglTpManager.saveOrUpdate(tp);
        }
    }
    
    /**
     * <li>说明：通过T6提票主键获取返修列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jt6IDX t6提票主键
     * @return List<ZbglTpRepair>
     */
    @SuppressWarnings("unchecked")
    public List<ZbglTpRepair> getRepairByJt6IDX(String jt6IDX)throws BusinessException, NoSuchFieldException {
        List<ZbglTpRepair> result = null ;
        if(!StringUtil.isNullOrBlank(jt6IDX)){
            StringBuffer sb = new StringBuffer();
            sb.append(" From ZbglTpRepair z where z.jt6IDX = '"+jt6IDX+"'");
            sb.append(" order by repairTimes desc ");
            result = this.daoUtils.find(sb.toString());
        }
        return  result;
    }

    /**
     * <li>说明：修改提票返修处理状态 检查点击确定的时候
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param zbglTpArray 提票信息列表
     * @throws Exception 
     */
    public void updateRepairStatus(ZbglTp[] zbglTpArray) throws Exception {
        StringBuilder idx = new StringBuilder();
        for (ZbglTp tp : zbglTpArray) {
            idx.append(tp.getIdx()).append(Constants.JOINSTR);
        }
        if (idx.toString().endsWith(Constants.JOINSTR))
            idx.deleteCharAt(idx.length() - 1);
        String idxsStr = CommonUtil.buildInSqlStr(idx.toString());
        StringBuffer sql = new StringBuffer("UPDATE ZB_ZBGL_JT6_REPAIR SET status = ")
        .append(ZbglTpRepair.STATUS_COMPLETE) 
        .append(" WHERE Jt6_IDX IN ").append(idxsStr) ;
        daoUtils.executeSql(sql.toString());
    }
    
}
