package com.yunda.jx.jxgc.repairrequirement.manager;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jxgc.repairrequirement.entity.RPToWS;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：RPToWS业务类,检修项目工序设置
 * <li>创建人：王治龙
 * <li>创建日期：2012-12-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
//@Service(value="rPToWSManager")
public class RPToWSManager extends JXBaseManager<RPToWS, RPToWS>{
	
//	/**
//	 * <li>说明：批量新增修改保存前的实体对象前的验证业务
//	 * <li>创建人：王治龙
//	 * <li>创建日期：2012-12-27
//	 * <li>修改人： 
//	 * <li>修改日期：
//	 * <li>修改内容：
//	 * @param entity 实体对象
//	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
//	 * @throws BusinessException
//	 */		
//	public String[] validateUpdate(RPToWS[] entityList) throws BusinessException {
//        List<String> errMsg = new ArrayList<String>();
//        for(RPToWS obj : entityList){
//            List<RPToWS> countList = this.getModelList(obj.getWorkSeqIDX(),obj.getRepairProjectIDX());
//            if(countList.size() > 0){
//                errMsg.add("【"+obj.getWorkSeqName()+"】作业工单，已经设置！");
//            }
//            if (errMsg.size() > 0) {
//                String[] errArray = new String[errMsg.size()];
//                errMsg.toArray(errArray);
//                return errArray;
//            }
//        }
//	    return null;
//	}
//    
//    /**
//     * <li>说明：批量设置修程类型对应修程
//     * <li>创建人：王治龙
//     * <li>创建日期：2012-10-30
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param objList：默认工位集合
//     * @return String[] 返回错误信息列表
//     * @throws NoSuchFieldException 
//     * @throws BusinessException 
//     */
//    public String[] saveOrUpdateList(RPToWS[] objList) throws BusinessException, NoSuchFieldException{
//        String[] errMsg = this.validateUpdate(objList);  //验证
//        List<RPToWS> entityList = new ArrayList<RPToWS>();
//        if (errMsg == null || errMsg.length < 1) {
//            for(RPToWS t : objList){ //循环新增是为了验证方便
//                entityList.add(t);
//            }
//            this.saveOrUpdate(entityList);
//        }
//        return errMsg;
//    }
//    /**
//     * <li>说明：通过作业工单主键和检修项目主键过滤检修项目对应的作业工单
//     * <li>创建人：王治龙
//     * <li>创建日期：2012-12-27
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param workSeqIDX：作业工单主键；repairProjectIDX：检修项目主键
//     * @return List<RPToWS> 返回集合
//     * @throws BusinessException
//     */
//    @SuppressWarnings("unchecked")
//    public List<RPToWS> getModelList(String workSeqIDX, String repairProjectIDX) throws BusinessException {
//        StringBuffer hql = new StringBuffer();
//        hql.append("From RPToWS t where t.recordStatus=").append(Constants.NO_DELETE);
//        if (!StringUtil.isNullOrBlank(workSeqIDX)) {
//            hql.append(" and t.workSeqIDX = '").append(workSeqIDX).append("'");
//        }
//        if (!StringUtil.isNullOrBlank(repairProjectIDX)) {
//            hql.append(" and t.repairProjectIDX = '").append(repairProjectIDX).append("'");
//        }
//        return this.daoUtils.find(hql.toString());
//    }
}