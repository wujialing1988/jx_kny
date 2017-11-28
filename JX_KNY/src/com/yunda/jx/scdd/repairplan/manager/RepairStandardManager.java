package com.yunda.jx.scdd.repairplan.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.scdd.repairplan.entity.RepairStandard;
import com.yunda.jx.util.MixedUtils;

/**
 * <li>说明：检修标准
 * <li>创建人： 张凡
 * <li>创建日期：2015年11月4日
 * <li>成都运达科技股份有限公司
 */
@Service
public class RepairStandardManager extends JXBaseManager<RepairStandard, RepairStandard> {

	@Override
	public String[] validateUpdate(RepairStandard t) {
		if(isDuplicateData(t)){
			return new String[]{ "【修程/修次】已存在，请修改后再提交" };
		}
		return super.validateUpdate(t);
	}
	
	/**
	 * <li>方法说明：检查是否存在重复数据
	 * <li>方法名：isDuplicateData
	 * @param t 检修标准
	 * @return True/False
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-31
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	private boolean isDuplicateData(RepairStandard t){
		String hql = "select count(*) from RepairStandard where trainTypeIdx = ? and repairClass = ? and repairOrder = ?";
		List<String> params = new ArrayList<String>(4);
		params.add(t.getTrainTypeIdx());
		params.add(t.getRepairClass());
		params.add(t.getRepairOrder());
		if(StringUtil.isNullOrBlank(t.getIdx()) == false){
			hql += " and idx != ?";
			params.add(t.getIdx());
		}
		return MixedUtils.getNumValue(daoUtils.findSingle(hql, params.toArray())) > 0;
	}
}
