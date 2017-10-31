package com.yunda.jxpz.trainshort.manager;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jxpz.trainshort.entity.TrainShort;


@Service(value="trainShortManager")
public class TrainShortManager extends JXBaseManager<TrainShort, TrainShort> {
   
	/**
	 * <li>˵��:���ݳ��Ͷ̳Ʋ�ѯ���Ͷ�Ӧ��
	 * <li>�����ˣ������
	 * <li>�������ڣ�2016-07-20
	 * <li>�޸��ˣ�
	 * <li>�޸����ڣ�
	 * <li>�޸����ݣ�
	 * 
	 * @param shortName ���Ͷ̳�
	 */
	public TrainShort getTrainShortByShort(String shortName){
		String hql = " From TrainShort where shortName = ? " ;
    	return (TrainShort)this.daoUtils.findSingle(hql, new Object[]{shortName});
    }
}
