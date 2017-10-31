package com.yunda.jx.pjwz.partsBase.partstype.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.jx.pjwz.partsBase.partstype.entity.PartsExtendNo;
import com.yunda.jx.pjwz.partsBase.partstype.entity.PartsExtendNoBean;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsExtendNo业务类,配件扩展编号
 * <li>创建人：程锐
 * <li>创建日期：2014-08-14
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "partsExtendNoManager")
public class PartsExtendNoManager extends JXBaseManager<PartsExtendNo, PartsExtendNo> {
	/**
	 * <li>说明：保存配件扩展编号
	 * <li>创建人：程锐
	 * <li>创建日期：2014-8-15
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param items
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void saveExtendNo(PartsExtendNo[] items) throws BusinessException, NoSuchFieldException {
		List<PartsExtendNo> entityList = new ArrayList<PartsExtendNo>();
		for (PartsExtendNo no : items) {
			entityList.add(no);
		}
		saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：配件扩展编号列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-8-15
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param partsTypeIDX
	 * @return 配件扩展编号列表
	 */
	public Page<PartsExtendNo> getExtendNoList(String partsTypeIDX) {
		List<PartsExtendNo> pageList = getListByPartsType(partsTypeIDX);
		List<PartsExtendNo> list = new ArrayList<PartsExtendNo>();
		for (int i = 0; i < 10; i++) {
			PartsExtendNo no = new PartsExtendNo();
			no.setExtendNoField("extendNo" + (i + 1));
			no.setIsUsed(PartsExtendNo.IS_USED);
			no.setPartsTypeIDX(partsTypeIDX);
			if (pageList != null && pageList.size() > 0) {
				for (PartsExtendNo extendNo : pageList) {
					if (extendNo.getExtendNoField().equals("extendNo" + (i + 1))) {
						no = extendNo;
						break;
					}
				}
			}
			list.add(no);
		}
		Page<PartsExtendNo> page = new Page<PartsExtendNo>(list.size(), list);
		return page;
	}

	/**
	 * <li>说明：获取配件规格型号对应的扩展编号列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-8-15
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param partsTypeIDX
	 * @return 配件规格型号对应的扩展编号列表
	 */
	@SuppressWarnings("unchecked")
	public List<PartsExtendNo> getListByPartsType(String partsTypeIDX) {
		PartsExtendNo no = new PartsExtendNo();
		no.setRecordStatus(Constants.NO_DELETE);
		no.setPartsTypeIDX(partsTypeIDX);
		return daoUtils.getHibernateTemplate().findByExample(no);
	}
	/**
	 * <li>说明：获取配件规格型号对应的已启用扩展编号列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-8-15
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param partsTypeIDX
	 * @return 配件规格型号对应的已启用扩展编号列表
	 */
	@SuppressWarnings("unchecked")
	public List<PartsExtendNo> getUsedListByPartsType(String partsTypeIDX) {
//		PartsExtendNo no = new PartsExtendNo();
//		no.setRecordStatus(Constants.NO_DELETE);
//		no.setPartsTypeIDX(partsTypeIDX);
//		no.setIsUsed(PartsExtendNo.IS_USED);
//		return daoUtils.getHibernateTemplate().findByExample(no);
		String hql = "from PartsExtendNo where recordStatus = ? and partsTypeIDX = ? and isUsed = ? order by extendNoField";
		return daoUtils.find(hql, new Object[] {Constants.NO_DELETE, partsTypeIDX, PartsExtendNo.IS_USED});
	}
	/**
	 * 
	 * <li>说明：根据配件规格型号获取配件扩展编号json实体对象列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-8-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param partsTypeIDX
	 * @return 配件扩展编号json实体对象列表
	 * @throws IOException
	 */
	public List<PartsExtendNoBean> getUsedJsonByPartsType(String partsTypeIDX) throws IOException {
		List<PartsExtendNo> list = getUsedListByPartsType(partsTypeIDX);
		List<PartsExtendNoBean> jsonList = new ArrayList<PartsExtendNoBean>();
		if (list != null && list.size() > 0) {
			for (PartsExtendNo no : list) {
				PartsExtendNoBean jsonNo = new PartsExtendNoBean();
				jsonNo.setExtendNoField(no.getExtendNoField());
				jsonNo.setExtendNoName(no.getExtendNoName());
				jsonList.add(jsonNo);
			}
		}
		return jsonList;
	}
	
	/**
	 * 
	 * <li>说明：验证一种规格型号的配件是否有已经启用的扩展编号
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-03
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	public boolean hasPartsExtendNo(String partsTypeIdx) {
		PartsExtendNo no = new PartsExtendNo();
		no.setRecordStatus(Constants.NO_DELETE);
		no.setPartsTypeIDX(partsTypeIdx);
		no.setIsUsed(PartsExtendNo.IS_USED);
		List list = daoUtils.getHibernateTemplate().findByExample(no);
		if (null == list || list.size() <= 0) {
			return false;
		}
		return true;
	}
	
}