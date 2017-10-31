package com.yunda.zb.pczz.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.zb.pczz.entity.ZbglPczzItemToTraininfo;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglPczzItemToTraininfoManager业务类,普查整治项中保存的机车信息
 * <li>创建人：林欢
 * <li>创建日期：2016-06-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */ 
@Service(value="zbglPczzItemToTraininfoManager")
public class ZbglPczzItemToTraininfoManager extends JXBaseManager<ZbglPczzItemToTraininfo, ZbglPczzItemToTraininfo>{
    
    /**
     * 
     * <li>说明：批量更新车型
     * <li>创建人：林欢
     * <li>创建日期：2016-6-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 批量更新IDX
     * @param zbglPczzItemToTraininfos 普查整治项保存的车号信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePczzItemTrainNos(String[] ids, ZbglPczzItemToTraininfo[] zbglPczzItemToTraininfos) throws BusinessException, NoSuchFieldException{
        for (String idx : ids) {
            
            Map<String, String> map = new HashMap<String, String>();
            map.put("idx", idx);
			//通过普查整治项idx查询与其对应的车号
            List<ZbglPczzItemToTraininfo> zbglPczzItemToTraininfoList = this.findZbglPczzItemToTraininfoListByPczzItemID(map);
            
//            //清空这些关联车号
//            for (ZbglPczzItemToTraininfo traininfo : zbglPczzItemToTraininfoList) {
//				this.daoUtils.remove(traininfo);
//			}
            
            //重新更新对应车号关系
            for (ZbglPczzItemToTraininfo zpitt : zbglPczzItemToTraininfos) {
            	ZbglPczzItemToTraininfo z = new ZbglPczzItemToTraininfo();
            	z.setTrainTypeIDX(zpitt.getTrainTypeIDX());
            	z.setTrainNo(zpitt.getTrainNo());
            	z.setTrainTypeShortName(zpitt.getTrainTypeShortName());
            	z.setZbglPczzIDX(zpitt.getZbglPczzIDX());
            	z.setZbglPczzItemIDX(idx);
            	this.save(z);
			}
        }
    }

    /**
     * 
     * <li>说明：根据普查整治idx查询与其关联的车号
     * <li>创建人：林欢
     * <li>创建日期：2016-8-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param map 查询条件
     * @return List<ZbglPczzItemToTraininfo> 返回list 
     */
	public List<ZbglPczzItemToTraininfo> findZbglPczzItemToTraininfoListByPczzItemID(Map<String, String> map) {
		StringBuffer sb = new StringBuffer();
		
		sb.append(" from ZbglPczzItemToTraininfo a where 1=1 ");
        
        //过滤普查整治计划项idx
        if (StringUtils.isNotBlank(map.get("idx"))) {
            sb.append(" and a.zbglPczzItemIDX = '").append(map.get("idx")).append("'");
        }
		
		//过滤车型简称
		if (StringUtils.isNotBlank(map.get("trainTypeShortName"))) {
			sb.append(" and a.trainTypeShortName = '").append(map.get("trainTypeShortName")).append("'");
		}
		
		//过滤车号
		if (StringUtils.isNotBlank(map.get("trainNo"))) {
			sb.append(" and a.trainNo = '").append(map.get("trainNo")).append("'");
		}
        
		//过滤车号
		if (StringUtils.isNotBlank(map.get("trainNoLike"))) {
		    sb.append(" and a.trainNo like '%").append(map.get("trainNoLike")).append("%'");
		}
		return this.daoUtils.find(sb.toString());
	}

    /**
     * <li>说明：查询整备普查整治所有的车型
     * <li>创建人：林欢
     * <li>创建日期：2016-8-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return List<String> 车型list
     */
    public List<String> findAllTrainTpye(Map<String, String> map) {
        StringBuffer sb = new StringBuffer();
        
        sb.append(" select a.train_type_shortname from zb_zbgl_pczz_item_to_traininfo a where 1 = 1");
//      过滤普查整治计划idx
        if (StringUtils.isNotBlank(map.get("zbglPczzIDX"))) {
            sb.append(" and a.zbgl_pczz_idx = '").append(map.get("zbglPczzIDX")).append("'");
        }
        sb.append(" group by a.train_type_shortname  ");
        
        return this.getDaoUtils().executeSqlQuery(sb.toString());
    }
}