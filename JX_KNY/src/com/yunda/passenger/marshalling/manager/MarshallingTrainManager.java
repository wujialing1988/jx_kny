package com.yunda.passenger.marshalling.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.order.AbstractOrderManager;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.EntityUtil;
import com.yunda.jx.jxgc.processdef.entity.JobProcessNodeDef;
import com.yunda.passenger.marshalling.entity.MarshallingTrain;
import com.yunda.passenger.traindemand.entity.MarshallingTrainDemand;
import com.yunda.util.BeanUtils;


/**
 * <li>标题: 肯尼亚综合管理信息系统
 * <li>说明: 编组车辆业务类
 * <li>创建人：张迪
 * <li>创建日期：2017-4-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部检修系统项目组
 * @version 1.0
 */
@Service("marshallingTrainManager")
public class MarshallingTrainManager extends  JXBaseManager<MarshallingTrain, MarshallingTrain> implements IbaseCombo {

	/**
	 * <li>说明：获取对应编组下的车辆信息数量
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param marshallingCode 编组编号
	 * @return 车辆数量
	 */
	public int findTrainCountByCode(String marshallingCode) {
		return CommonUtil.getListSize(findTrainListByCode(marshallingCode));
	}
	
    /**
     * <li>说明：获取编组下的车辆信息列表
     * <li>创建人：张迪
     * <li>创建日期：2017-4-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param marshallingCode 编组编号
     * @return 车辆列表
     */
    @SuppressWarnings("unchecked")
    public List<MarshallingTrain> findTrainListByCode(String marshallingCode) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put("marshallingCode", marshallingCode);
        return findTrainList(paramMap);
    }
    /**
     * <li>说明：获取编组下的对应顺序的车辆
     * <li>创建人：张迪
     * <li>创建日期：2017-4-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param marshallingCode 编组编号
     * @return 车辆列表
     */
    @SuppressWarnings("unchecked")
    public List<MarshallingTrain> findTrainByCodeAndSeqNo(String marshallingCode, Integer seqNo,String condition) {
    	 StringBuilder hql = new StringBuilder();
         hql.append("from MarshallingTrain where recordStatus = 0 and marshallingCode = '").append(marshallingCode).append("'");
         if(">".equals(condition)){
        	 hql.append(" and seqNo >").append(seqNo); 
         } else{
        	 hql.append(" and seqNo =").append(seqNo); 
         }
         hql.append(" order by seqNo asc ");
         return  daoUtils.find(hql.toString());
    }
   /**
	 * <li>说明：查询车辆信息
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param paramMap 参数
	 * @return 车辆列表
	 */
	@SuppressWarnings("unchecked")
	private List<MarshallingTrain> findTrainList(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from MarshallingTrain where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap)).append(" and recordStatus = 0");
        return daoUtils.find(hql.toString());
    }

	/**
	 * <li>说明：方法实现功能说明
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entityList
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public void updateSort(List<MarshallingTrain> entityList) throws BusinessException, NoSuchFieldException  {
		if (null == entityList || 0 >= entityList.size()) {
			return;
		}
		MarshallingTrain entity = null;
		for (int i = 0; i < entityList.size(); i++) {
			entity = entityList.get(i);
			entity.setSeqNo(entity.getSeqNo()-1);
			super.saveOrUpdate(entity);
		}
	}
	/**
	 * <li>说明：方法实现功能说明
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entityList
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	@Override
	public void logicDelete(Serializable id) throws BusinessException, NoSuchFieldException {
		MarshallingTrain t = this.getModelById(id);
		super.logicDelete(id);
		try {
			updateSort(this.findTrainByCodeAndSeqNo(t.getMarshallingCode(),t.getSeqNo(),">"));
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}
}
