package com.yunda.jx.jxgc.buildupmanage.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jxgc.buildupmanage.entity.PlaceFaultMethod;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 故障现象处理方法业务类
 * <li>创建人：程锐
 * <li>创建日期：2013-4-10
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "placeFaultMethodManager")
public class PlaceFaultMethodManager extends JXBaseManager<PlaceFaultMethod, PlaceFaultMethod> {

	/**
	 * <li>说明：批量保存故障现象处理方法
	 * <li>创建人：程锐
	 * <li>创建日期：2013-4-10
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param list 故障现象处理方法列表
	 * @return void
	 * @throws BusinessException, NoSuchFieldException
	 */
	public void saveOrUpdateList(PlaceFaultMethod[] list) throws BusinessException, NoSuchFieldException {
		if(list == null || list.length < 1) return;
		for (PlaceFaultMethod placeFaultMethod : list) {
			placeFaultMethod.setIsDefault(PlaceFaultMethod.NODEFAULT);
			list[0].setIsDefault(PlaceFaultMethod.ISDEFAULT);
			saveOrUpdate(placeFaultMethod);
		}
	}

	/**
	 * <li>说明：获取对应位置故障现象的处理方法列表
	 * <li>创建人：程锐
	 * <li>创建日期：2013-4-10
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param placeFaultIDX 故障现象主键
	 * @return List<PlaceFaultMethod> 处理方法列表
	 */
	@SuppressWarnings("unchecked")
	public List<PlaceFaultMethod> getListByPlaceFaultId(String placeFaultIDX) {
		String hql = "from PlaceFaultMethod where recordStatus = 0 and placeFaultIDX = '" + placeFaultIDX + "'";
		return daoUtils.find(hql);
	}

	/**
	 * <li>说明：设为默认
	 * <li>创建人：程锐
	 * <li>创建日期：2013-4-11
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param placeFaultMethod 处理方法实体
	 * @return void
	 * @throws BusinessException, NoSuchFieldException
	 */
	public void setIsDefault(PlaceFaultMethod placeFaultMethod) throws BusinessException, NoSuchFieldException {
		// 将同一故障现象的处理方法设为非缺省
		List<PlaceFaultMethod> list = getListByPlaceFaultId(placeFaultMethod.getPlaceFaultIDX());
		if (list != null && list.size() > 0) {
			for (PlaceFaultMethod method : list) {
				method.setIsDefault(PlaceFaultMethod.NODEFAULT);
				saveOrUpdate(method);
			}
		}
		// 设置默认
		placeFaultMethod.setIsDefault(PlaceFaultMethod.ISDEFAULT);
		saveOrUpdate(placeFaultMethod);

	}

	/**
	 * <li>方法名称：getDefaultMethodByPlace
	 * <li>方法说明：查询默认处理方法
	 * <li>@param placeIdx
	 * <li>return: PlaceFaultMethod
	 * <li>创建人：张凡
	 * <li>创建时间：2013-4-24 下午03:03:13
	 * <li>修改人：
	 * <li>修改内容：
	 */
	@SuppressWarnings("unchecked")
	public PlaceFaultMethod getDefaultMethodByPlace(String placeIdx) {
		String sql = "select method_id,method_name,method_desc from jxgc_place_fault_method t where place_fault_idx in ("
				+ "select idx from  jxgc_place_fault x where x.buildup_place_idx='" + placeIdx + "' and x.record_status=0) "
				+ "and record_status=0 and is_default=" + PlaceFaultMethod.ISDEFAULT;

		List<Object[]> list = daoUtils.executeSqlQuery(sql);
		if (list == null || list.size() < 1) return null;
		PlaceFaultMethod m = new PlaceFaultMethod();
		m.setMethodID(list.get(0)[0].toString());
		m.setMethodName(list.get(0)[1].toString());
		if (list.get(0)[2] != null) {
			m.setMethodDesc(list.get(0)[2].toString());
		}
		return m;
	}
}
