package com.yunda.jx.pjjx.partsrdp.tecinst.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.partsrdp.tecinst.entity.PartsRdpTecWS;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpTecWS业务类,配件检修工序实例
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsRdpTecWSManager")
public class PartsRdpTecWSManager extends JXBaseManager<PartsRdpTecWS, PartsRdpTecWS>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    
	/**
	 * <li>说明：根据“工艺工单主键”获取配件检修工序实例
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpTecCardIDX 工艺工单主键
	 * @return List<PartsRdpTecWS> 配件检修工序实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<PartsRdpTecWS> getModels(String rdpTecCardIDX) {
		String hql = "From PartsRdpTecWS Where recordStatus = 0 And rdpTecCardIDX = ?";
		return this.daoUtils.find(hql, new Object[]{rdpTecCardIDX});
	}
	
	/**
	 * <li>说明：批量处理
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 配件检修工序主键数组
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public void finishBatchWS(String[] ids) throws BusinessException, NoSuchFieldException {
		List<PartsRdpTecWS> entityList = new ArrayList<PartsRdpTecWS>(ids.length);
		for (String idx : ids) {
			entityList.add(this.getModelById(idx));
		}
		this.finishBatchWS(entityList);
	}
	
	/**
	 * <li>说明：批量处理
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entityList 配件检修工序实体集合
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public void finishBatchWS(List<PartsRdpTecWS> entityList) throws BusinessException, NoSuchFieldException {
		for (PartsRdpTecWS entity : entityList) {
			if (entity.getStatus().equals(PartsRdpTecWS.CONST_STR_STATUS_WCL)) {
				// 设置记录状态为“已处理”
				entity.setStatus(PartsRdpTecWS.CONST_STR_STATUS_YCL);
			}
		}
		this.saveOrUpdate(entityList);
	}
	
	/**
	 * <li>说明：功能操作前的记录状态验证
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
     * @param entityList 配件检修工序实体集合
	 * @param status 记录的预期状态
	 * @return String[] 验证消息
	 */
	public String[] validateStatus(List<PartsRdpTecWS> entityList, String status) {
		List<String> errMsgs = new ArrayList<String>(entityList.size());
		String validateMsg = null;
		for (PartsRdpTecWS entity : entityList) {
			validateMsg = this.validateStatus(entity, status);
			if (null == validateMsg) {
				continue;
			}
			errMsgs.add(validateMsg);
		}
		if (errMsgs.size() > 0) {
			return errMsgs.toArray(new String[errMsgs.size()]);
		}
		return null;
	}

	/**
	 * <li>说明：功能操作前的记录状态验证，如果操作发生时，记录的状态不是预期的状态，则返回相应的状态的提示信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 配件检修工序实例
	 * @param status 记录的预期状态
     * @return String 验证消息
	 */
	public String validateStatus(PartsRdpTecWS entity, String status) {
        // 验证记录的状态
        String errMsg = PartsRdpTecWSManager.checkEntityStatus(status, entity.getStatus());
        if (null == errMsg) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getWsName()).append(Constants.BRACKET_L).append(entity.getWsNo()).append(Constants.BRACKET_R);
		return sb.append(errMsg).toString();
	}
    
    /**
     * <li>说明：验证记录的状态
     * <li>创建人：何涛
     * <li>创建日期：2015-2-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param status 预期的状态值
     * @param entityStatus 记录当前的状态值
     * @return String 验证消息
     */
    public static String checkEntityStatus(String status, String entityStatus) {
        if (status.equals(entityStatus)) {
            return null;
        }
        String errMsg = null;
        if (null == entityStatus || entityStatus.trim().length() <= 0) {
            errMsg = "数据异常，未知的记录工单状态！";
        }
        if (entityStatus.equals(PartsRdpTecWS.CONST_STR_STATUS_YCL)) {
            errMsg = "已经处理！";
        }
        if (entityStatus.equals(PartsRdpTecWS.CONST_STR_STATUS_WCL)) {
            errMsg = "还未处理！";
        }
        return errMsg;
    }
	
	/**
	 * <li>说明：功能操作前的记录状态验证
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 配件检修工序实例 idx主键数组
	 * @param status 记录的预期状态
	 * @return String[] 验证消息
	 */
	public String[] validateStatus(String[] ids, String status) {
		List<String> errMsgs = new ArrayList<String>(ids.length);
		PartsRdpTecWS entity = null;
		String validateMsg = null;
		for (String idx : ids) {
			entity = this.getModelById(idx);
			validateMsg = this.validateStatus(entity, status);
			if (null == validateMsg) {
				continue;
			}
			errMsgs.add(validateMsg);
		}
		if (errMsgs.size() > 0) {
			return errMsgs.toArray(new String[errMsgs.size()]);
		}
		return null;
	}
	
	/**
	 * <li>说明：检验所有记录是否都已处理完成
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
     * @param entityList 配件检修工序实例集合
	 * @param msgList 验证消息集合
	 * @return 
	 * @throws BusinessException
	 */
	public void checkFinishStatus(List<PartsRdpTecWS> entityList, List<String> msgList) throws BusinessException {
		for (PartsRdpTecWS entity : entityList) {
			if (!entity.getStatus().equals(PartsRdpTecWS.CONST_STR_STATUS_YCL)) {
				msgList.add("检修工序：" + entity.getWsNo() + "（" + entity.getWsName() + "）还未处理！");
				return;
			}
		}
	}

	/**
	 * <li>说明：撤销工艺工序的处理历史记录
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 配件检修工序实例
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public void giveUpJob(PartsRdpTecWS entity) throws BusinessException, NoSuchFieldException {
		// 设置工序的状态为“未处理”
		entity.setStatus(PartsRdpTecWS.CONST_STR_STATUS_WCL);
		this.saveOrUpdate(entity);
	}
	/**
	 * <li>说明：撤销工艺工序的处理历史记录
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entityList 配件检修工序实例列表
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public void giveUpJob(List<PartsRdpTecWS> entityList) throws BusinessException, NoSuchFieldException {
		for (PartsRdpTecWS entity : entityList) {
			this.giveUpJob(entity);
		}
	}
	
	/**
	 * <li>说明：查询配件检修工序
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param parentIDX 上级工序idx主键
	 * @param rdpTecCardIDX 配件检修工艺工单主键
	 * @return List<HashMap<String, Object>> 对象集合
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> tree(String parentIDX, String rdpTecCardIDX) {
		String hql = "from PartsRdpTecWS where wsParentIDX = ? And rdpTecCardIDX = ? And recordStatus = "
				+ Constants.NO_DELETE + " order by seqNo";
		List<PartsRdpTecWS> list = (List<PartsRdpTecWS>) this.daoUtils.find(hql, new Object[] { parentIDX, rdpTecCardIDX });
		List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
		for (PartsRdpTecWS t : list) {
			Boolean isLeaf = this.isLeaf(t.getWsIDX());
			HashMap<String, Object> nodeMap = new HashMap<String, Object>();
			nodeMap.put("id", t.getWsIDX());						// 工序idx主键
			nodeMap.put("text", formatDisplayInfo(t)); 			// 工序名称
			nodeMap.put("leaf", isLeaf);
			nodeMap.put("parentIDX", t.getWsParentIDX()); 		// 上级工序idx主键
			nodeMap.put("wPNo", t.getWsNo()); 					// 工序名称
			nodeMap.put("wPDesc", t.getWsDesc()); 				// 工序描述
			nodeMap.put("seqNo", t.getSeqNo());					// 顺序号
			children.add(nodeMap);
		}
		return children;
	}
	
	/**
	 * <li>说明：查询是否为子节点
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param  parentIDX 上级工序idx主键
	 * @return boolean 如果是叶子节点则返回true， 否则返回false
	 * @throws BusinessException
	 */
	public boolean isLeaf(String parentIDX) throws BusinessException {
		StringBuffer hql = new StringBuffer();
		hql.append("Select count(*) From PartsRdpTecWS Where recordStatus = " + Constants.NO_DELETE);
		if (!StringUtil.isNullOrBlank(parentIDX)) {
			hql.append(" And wsParentIDX = '" + parentIDX + "'");
		}
		int count = daoUtils.getInt(enableCache(), hql.toString());
		return count == 0 ? true : false;
	}
	
	/**
	 * <li>说明：格式化【工序】顺序号显示
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 当前节点的【工序】
	 * @return String 页面显示字符串
	 */
	private String formatDisplayInfo(PartsRdpTecWS entity) {
		List<Integer> list = new ArrayList<Integer>();
		this.getParentSeqNo(entity, list);
		int length = list.size();
		StringBuilder sb = new StringBuilder();
		for (int i = length - 1; i >=0; i--) {
			sb.append(list.get(i)).append(".");
		}
		// 工序名称
		sb.append(entity.getWsName());
		return sb.toString();
	}
	

	/**
	 * <li>说明：递归获取配件检修工序的顺序号列
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 当前节点的【工序】
	 * @param list 顺序号集合
	 */
	public void getParentSeqNo(PartsRdpTecWS entity, List<Integer> list) {
		list.add(entity.getSeqNo());
		if (!entity.getWsParentIDX().equals("ROOT_0")) {
			PartsRdpTecWS parentEntity = this.getModelByWsIDX(entity.getWsParentIDX(), entity.getRdpTecCardIDX());
			getParentSeqNo(parentEntity, list);
		}
	}

	/**
	 * <li>说明：通过工序主键获取工序实例
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param wsIDX 工序主键
	 * @param rdpTecCardIDX 配件检修工艺工单主键
	 * @return PartsRdpTecWS 配件检修工艺工序实体
	 */
	@SuppressWarnings("unchecked")
	public PartsRdpTecWS getModelByWsIDX(String wsIDX, String rdpTecCardIDX) {
		String hql = "From PartsRdpTecWS Where recordStatus = 0 And wsIDX = ? And rdpTecCardIDX = ?";
		List<PartsRdpTecWS> list = this.daoUtils.find(hql, new Object[]{wsIDX, rdpTecCardIDX});
		if (null == list || list.size() < 0) {
			return null;
		}
		return list.get(0);
	}
	
}