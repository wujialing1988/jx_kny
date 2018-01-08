package com.yunda.freight.base.classOrganization.entity;

import java.util.Comparator;

import com.yunda.frame.util.StringUtil;

public class ComparatorUser implements Comparator<ClassOrganizationUserVo> {

	@Override
	public int compare(ClassOrganizationUserVo vo1, ClassOrganizationUserVo vo2) {
		// TODO Auto-generated method stub
		if(!StringUtil.isNullOrBlank(vo1.getQueueCode())){
			if(StringUtil.isNullOrBlank(vo2.getQueueCode())){
				return 0;
			}else{
				return vo1.getQueueCode().compareTo(vo2.getQueueCode());
			}
		}
		return 0;
	}

}
