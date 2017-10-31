package com.yunda.jx.scdd.taskprogress.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.scdd.taskprogress.Progress;
import com.yunda.jx.scdd.taskprogress.ProgressdataConfig;
import com.yunda.jx.scdd.taskprogress.entity.TrainWPDetail;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainWPDetail业务类,机车作业进度项
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-12-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="trainWPDetailManager")
public class TrainWPDetailManager extends JXBaseManager<TrainWPDetail, TrainWPDetail>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * 作业进度业务类
	 */
	private TrainWPManager trainWPManager;
	/**
	 * 
	 * <li>说明：根据作业进度主键删除其对应的作业进度项信息
	 * <li>创建人：程梅
	 * <li>创建日期：2013-12-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void deleteModelList(String trainWPIDX) throws BusinessException {
        StringBuffer hql = new StringBuffer();
        hql.append("delete from TrainWPDetail t where t.trainWPIDX= '").append(trainWPIDX).append("'"); 
        this.daoUtils.executUpdateOrDelete(hql.toString());
//        List<TrainWPDetail> list = this.daoUtils.find(hql.toString());
//        if(list != null && list.size() > 0){
//            this.daoUtils.getHibernateTemplate().deleteAll(list);
//        }
    }
	/**
	 * 
	 * <li>说明：查询进度项信息
	 * <li>创建人：程梅
	 * <li>创建日期：2013-12-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public List<TrainWPDetail> findPageProgressList(String trainWPIDX){
		List<Progress> list = ProgressdataConfig.getInstance().getContent();
		String hql = "from TrainWPDetail where recordStatus=0 and trainWPIDX='"+trainWPIDX+"'";
		List<TrainWPDetail> detailList = daoUtils.find(hql);
		List<TrainWPDetail> returnList = new ArrayList<TrainWPDetail>();
		TrainWPDetail detai ;
		for(Progress progress : list){
			detai = new TrainWPDetail();
			detai.setProgressCode(progress.getKey());
			detai.setProgressName(progress.getName());
			detai.setDataType(progress.getDataType());
			detai.setIdx("abcdef"+progress.getKey());
			for(TrainWPDetail detail : detailList){
				if(progress.getKey().equals(detail.getProgressCode())){
					detai.setIdx(detail.getIdx());
					detai.setProgressValue(detail.getProgressValue());
					detai.setTrainWPIDX(detail.getTrainWPIDX());
					break ;
				}
			}
			returnList.add(detai);
		}
		return returnList;
	}
	/**
	 * 
	 * <li>说明：新增进度项信息
	 * <li>创建人：程梅
	 * <li>创建日期：2013-12-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void saveTrainWPDetail(String trainWPIDX,String key,String name,String value) throws Exception{
		TrainWPDetail wpDetail = new TrainWPDetail();
		wpDetail.setTrainWPIDX(trainWPIDX);
		wpDetail.setProgressCode(key);
		wpDetail.setProgressName(name);
		wpDetail.setProgressValue(value);
		wpDetail.setDataSource(TrainWPDetail.DATASOURCE_A);  //数据来源---自动生成
		this.saveOrUpdate(wpDetail);
		
	}
	/**
	 * 
	 * <li>说明：行编辑保存进度项信息
	 * <li>创建人：程梅
	 * <li>创建日期：2013-12-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void saveTrainWPDetail(TrainWPDetail entity) throws BusinessException, NoSuchFieldException{
		  if(entity.getIdx().indexOf("abcdef") != -1){
			  entity.setIdx(null);
		  }
		  entity.setDataSource(TrainWPDetail.DATASOURCE_M);
	      this.saveOrUpdate(entity);
	   }
	/**
	 * 
	 * <li>说明：根据作业进度id查询作业进度项信息
	 * <li>创建人：程梅
	 * <li>创建日期：2013-12-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public List<TrainWPDetail> getModelList(String trainWPIDX) throws BusinessException {
        StringBuffer hql = new StringBuffer();
        hql.append("from TrainWPDetail t where t.trainWPIDX= '").append(trainWPIDX).append("' and t.recordStatus=0"); 
        List<TrainWPDetail> list = daoUtils.find(hql.toString());
        return list;
    }
	/**
	 * <li>说明：删除实体对象前的验证业务
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-12-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 实体对象的idx主键数组
	 * @return 返回删除操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */	
	@Override
	public String[] validateDelete(Serializable... ids) throws BusinessException {
		return null;
	}
	
	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-12-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@Override
	public String[] validateUpdate(TrainWPDetail entity) throws BusinessException {
		return null;
	}
	public TrainWPManager getTrainWPManager() {
		return trainWPManager;
	}
	public void setTrainWPManager(TrainWPManager trainWPManager) {
		this.trainWPManager = trainWPManager;
	}
}