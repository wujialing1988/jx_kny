package com.yunda.freight.zb.detain.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.freight.zb.detain.entity.DetainGztp;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 扣车故障登记业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-04-20 17:26:28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("detainGztpManager")
public class DetainGztpManager extends JXBaseManager<DetainGztp, DetainGztp> {
	
   /**
    * <li>说明：获取扣成提票列表
    * <li>创建人：伍佳灵
    * <li>创建日期：2018-1-5
    * <li>修改人： 
    * <li>修改日期：
    * <li>修改内容：
    * @param ids 主键
    */
	public List<DetainGztp> findDetainGztp(String detainIdx){
		StringBuffer hql = new StringBuffer(" From DetainGztp where detainIdx = ? ");
		return this.daoUtils.find(hql.toString(), new Object[]{ detainIdx });
	}
	
	 /**
     * <li>说明：保存扣车登记下的故障信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2018-01-06
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param gztpsArray 故障信息列表
     * @param detainIdx 扣车登记id
     */
    public void saveDetainGztps(DetainGztp[] gztpsArray, String detainIdx) {
        // 保存登记下的故障情况
        for (DetainGztp zgtp : gztpsArray) {
        	zgtp.setIdx(null);
        	zgtp.setDetainIdx(detainIdx);
            this.save(zgtp);
        }
    }
   
}
