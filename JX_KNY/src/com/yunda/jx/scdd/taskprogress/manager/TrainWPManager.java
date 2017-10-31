package com.yunda.jx.scdd.taskprogress.manager;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.scdd.taskprogress.entity.TrainWP;
import com.yunda.jx.scdd.taskprogress.entity.TrainWPDetail;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainWP业务类,机车作业进度
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-12-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="trainWPManager")
public class TrainWPManager extends JXBaseManager<TrainWP, TrainWP>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * 作业进度项业务类
	 */
	private TrainWPDetailManager trainWPDetailManager;


	public TrainWPDetailManager getTrainWPDetailManager() {
		return trainWPDetailManager;
	}


	public void setTrainWPDetailManager(TrainWPDetailManager trainWPDetailManager) {
		this.trainWPDetailManager = trainWPDetailManager;
	}


	/**
     * TODO V3.2.1代码重构
	 * <li>说明：生产任务启动后，自动录入作业进度信息
	 * <li>创建人：程梅
	 * <li>创建日期：2013-12-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param rdpIDX 机车检修作业计划idx
	 * @return void
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
    public void saveTrainWP(String rdpIDX) throws Exception{
//		TrainWP wp = new TrainWP();
//		wp.setRdpIDX(rdpIDX);
//		List wplist = this.daoUtils.getHibernateTemplate().findByExample(wp);
//		if(wplist != null && wplist.size() > 0)
//			return;//已有对应的作业进度则不保存作业进度
//		TrainEnforcePlanRdp rdp = trainEnforcePlanRdpManager.getModelById(rdpIDX);
//		wp = new TrainWP();
//		wp.setBShortName(rdp.getBshortName());
//		wp.setDShortName(rdp.getDshortName());
//		wp.setDelegateDShortname(rdp.getDelegateDShortName());
//		wp.setTrainTypeIDX(rdp.getTrainTypeIDX());
//		wp.setTrainTypeShortName(rdp.getTrainTypeShortName());
//		wp.setTrainNo(rdp.getTrainNo());
//		wp.setRdpIDX(rdp.getIdx());
//		this.saveOrUpdate(wp);
	}
	
	/**
	 * 
	 * <li>说明：手动录入作业进度信息和进度项信息
	 * <li>创建人：程梅
	 * <li>创建日期：2013-12-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void saveTrainWPAndDetail(TrainWP entity, TrainWPDetail[] trainWPDetailList) throws BusinessException, NoSuchFieldException{
//	      if(StringUtil.isNullOrBlank(entity.getIdx())&&StringUtil.isNullOrBlank(entity.getStatus())){
//	    	  entity.setStatus(WorkTask.STATUS_INIT);
//	      }
	      this.saveOrUpdate(entity);
	      List<TrainWPDetail> entityList = new ArrayList<TrainWPDetail>();
	      //如果Length>0, 代表至少选择了一项
	      if(trainWPDetailList!=null&&trainWPDetailList.length>0){
		      for (TrainWPDetail obj : trainWPDetailList) {
		          if(!StringUtil.isNullOrBlank(entity.getIdx())){  //不等于空说明是修改
		        	  trainWPDetailManager.deleteModelList(entity.getIdx()); //修改时将数据删除后再创建
		          }
		          obj.setTrainWPIDX(entity.getIdx()); //设置作业进度主键
		          entityList.add(obj);
		      }
	      } else {
	    	  //如果页面上的时间都没录入数据, 则删除所有
	    	  trainWPDetailManager.deleteModelList(entity.getIdx());
	      }
	      trainWPDetailManager.saveOrUpdate(entityList);
	   }
	/**
	 * 
	 * <li>说明：根据所选作业计划新增作业进度信息
	 * <li>创建人：程梅
	 * <li>创建日期：2013-12-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public String[] saveOrUpdateList(TrainWP[] objList) throws BusinessException, NoSuchFieldException, ParseException{
//        String[] errMsg = this.validateUpdate(objList);  //验证
        List<TrainWP> entityList = new ArrayList<TrainWP>();
//        if (errMsg == null || errMsg.length < 1) {
            for(TrainWP t : objList){ //循环新增是为了验证方便
                entityList.add(t);
            }
            this.saveOrUpdate(entityList);
//        }
        return null;
    }
	/**
	 * 
	 * <li>说明：逻辑删除作业进度和进度项信息
	 * <li>创建人：程梅
	 * <li>创建日期：2013-12-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
		List<TrainWP> entityList = new ArrayList<TrainWP>();
		List<TrainWPDetail> detailList = new ArrayList<TrainWPDetail>();
		for (Serializable id : ids) {
			TrainWP t = getModelById(id);
			t = EntityUtil.setSysinfo(t);
//			设置逻辑删除字段状态为已删除
			t = EntityUtil.setDeleted(t);
			entityList.add(t);
			List<TrainWPDetail> list = trainWPDetailManager.getModelList(id.toString());
			list = EntityUtil.setSysinfo(list);
			list = EntityUtil.setDeleted(list);
			detailList.addAll(list);
		}
		this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
		this.daoUtils.getHibernateTemplate().saveOrUpdateAll(detailList);
	}
	/**
	 * 
	 * <li>说明：根据兑现单id查询该兑现单对应的作业进度信息
	 * <li>创建人：程梅
	 * <li>创建日期：2014-2-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public TrainWP findByRdpid(String rdpId){
		String hql = " from TrainWP where recordStatus = 0 and rdpIDX='"+rdpId+"'";
		return (TrainWP) daoUtils.findSingle(hql);
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
	public String[] validateUpdate(TrainWP entity) throws BusinessException {
		return null;
	}
}