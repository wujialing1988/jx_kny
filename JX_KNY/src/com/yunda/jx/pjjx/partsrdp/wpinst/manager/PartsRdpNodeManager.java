package com.yunda.jx.pjjx.partsrdp.wpinst.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.EosDictEntrySelectManager;
import com.yunda.jx.pjjx.base.wpdefine.entity.WPNode;
import com.yunda.jx.pjjx.partsrdp.IPartsRdpStatus;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpManager;
import com.yunda.jx.pjjx.partsrdp.qcinst.manager.PartsRdpQRQueryManager;
import com.yunda.jx.pjjx.partsrdp.rdpnotice.entity.PartsRdpNotice;
import com.yunda.jx.pjjx.partsrdp.rdpnotice.manager.PartsRdpNoticeManager;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordCard;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordCardManager;
import com.yunda.jx.pjjx.partsrdp.tecinst.entity.PartsRdpTecCard;
import com.yunda.jx.pjjx.partsrdp.tecinst.manager.PartsRdpTecCardManager;
import com.yunda.jx.pjjx.partsrdp.wpinst.entity.PartsNodeRe;
import com.yunda.jx.pjjx.partsrdp.wpinst.entity.PartsRdpNode;
import com.yunda.jx.pjjx.partsrdp.wpinst.entity.PartsRdpNodeFlowSheetBean;
import com.yunda.jx.pjwz.partsBase.repairlist.manager.PartsRepairListManager;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpNode业务类,配件检修作业节点
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsRdpNodeManager")
public class PartsRdpNodeManager extends JXBaseManager<PartsRdpNode, PartsRdpNode>{
	
	/** PartsRdpNodeSeq业务类,作业流程节点前后置关系 */
	@Resource
	private PartsRdpNodeSeqManager partsRdpNodeSeqManager;
	
	/** PartsRdpTecCard业务类,配件检修工艺工单 */
	@Resource
	private PartsRdpTecCardManager partsRdpTecCardManager;
	
	/** PartsRdpRecordCard业务类,配件检修记录卡实例 */
	@Resource
	private PartsRdpRecordCardManager partsRdpRecordCardManager;
	
	/** PartsRdp业务类,配件检修作业 */
	@Resource
	private PartsRdpManager partsRdpManager;
	
    /** partsRdpNodeQueryManager业务类,作业流程节点查询业务类 */
    @Resource
    private PartsRdpNodeQueryManager partsRdpNodeQueryManager;
    
    /** PartsRepairListManager业务类,配件自修目录业务类 */
    @Resource
    private PartsRepairListManager partsRepairListManager;

    /** PartsRdpNoticeManager业务类,配件提票业务类 */
    @Resource
    private PartsRdpNoticeManager partsRdpNoticeManager;

    /** PartsRdpQRQueryManager业务类,配件质检查询业务类 */
    @Resource
    private PartsRdpQRQueryManager partsRdpQRQueryManager;

    /** PartsAccountManager业务类,配件信息业务类 */
    @Resource
    private PartsAccountManager partsAccountManager;
    /** PartsAccountManager业务类,数据字典业务类 */
    @Resource
    private  EosDictEntrySelectManager eosDictEntrySelectManager;
	/**
	 * <li>检验【配件检修作业节点】的完成状态
	 * <ol>
	 * <li>如果该节点是父节点，要检验所有直接“子节点”状态是否都是“已处理”
	 * <li>如果该节点是叶子节点，要检验该节点所挂“工艺工单”和“记录工单”状态是否都是“修竣”
	 * <li>此处的检验不再需要对节点的“后置节点”进行单独验证，因为如果该节点是父节点，在对其下属子节点的验证中，就已经包含了对“后置节点”的状态验证
	 * </ol>
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-24
	 * <li>修改人： 程锐
	 * <li>修改日期：2015-10-9
	 * <li>修改内容：修改返回类型，返回验证信息
	 * 
	 * @param entity 【配件检修作业节点】实体
	 * @return true: 所有直接“子节点”和直接“后置节点”都已处理， 否则返回false
	 */
	public String validateFinishedStatus(PartsRdpNode entity) {
		List<PartsRdpNode> entityList;
		// 如果是父节点，要检验所有直接“子节点”状态是否都是“已处理”
		if (entity.getIsLeaf() == WPNode.CONST_INT_IS_LEAF_NO) {
			// 获取所有直接“子节点”列表
			entityList = partsRdpNodeQueryManager.getDirectChildren(entity.getRdpIDX(), entity.getWpNodeIDX());
			// 检验所有直接“子节点”的状态是否都是“已处理”【递归】
			if (null != entityList && entityList.size() > 0) {
				for (PartsRdpNode childNode : entityList) {
					// 递归检验“子节点”状态是否都是“已处理”
					if (null == childNode.getStatus() || !PartsRdpNode.CONST_STR_STATUS_YCL.equals(childNode.getStatus())) {
						return "子节点未全部处理";
					}
				}
			}
		// 如果是叶子节点，要检验该节点所挂“工业工单”和“记录工单”状态是否都是“修竣”或者“质量检验中”
		} else {
			// 获取节点所挂“工艺工单”
			List<PartsRdpTecCard> listTecCard = partsRdpTecCardManager.getModels(entity.getRdpIDX(), entity.getIdx());
			if (null != listTecCard && listTecCard.size() > 0) {
				// 检验节点所挂“工艺工单”的状态是否都是“修竣”
				for (PartsRdpTecCard tecCard : listTecCard) {
					if (!IPartsRdpStatus.CONST_STR_STATUS_XJ.equals(tecCard.getStatus())) {
						return "作业工单未全部处理";
					}
				}
			}
			// 获取节点所挂“记录工单”
			List<PartsRdpRecordCard> listRecordCard = partsRdpRecordCardManager.getModels(entity.getRdpIDX(), entity.getIdx());
			if (null != listRecordCard && listRecordCard.size() > 0) {
				// 检验节点所挂“记录工单”的状态是否都是“修竣”
				for (PartsRdpRecordCard recordCard : listRecordCard) {
					if (!IPartsRdpStatus.CONST_STR_STATUS_XJ.equals(recordCard.getStatus()) && !IPartsRdpStatus.CONST_STR_STATUS_ZLJYZ.equals(recordCard.getStatus())) {
						return "记录工单未全部处理";
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * <li>更新【配件检修作业节点】的状态为“已处理”
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 配件检修作业节点实体
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
    private void updateFinishedStatus(PartsRdpNode entity) throws Exception {
		
		entity.setStatus(PartsRdpNode.CONST_STR_STATUS_YCL); 		// 设置状态为“已处理”
		entity.setRealEndTime(Calendar.getInstance().getTime()); 	// 设置实际结束时间为当前时间
		// 如果实际开始时间为空，则对该字段进行更新（设置为当前系统时间）
		if (null == entity.getRealStartTime()) {
			// 设置实际开始时间
			entity.setRealStartTime(Calendar.getInstance().getTime());
		}
		OmOrganization org = SystemContext.getOmOrganization();
		if (org != null) {
			entity.setHandleOrgID(org.getOrgid());
			entity.setHandleOrgName(org.getOrgname());
		}
		
		// 保存
		this.saveOrUpdate(entity);		
		
		// 如果该节点的父节点下的所有子节点都已完成则需完成对其父节点状态的更新
		PartsRdpNode parentWPNode = partsRdpNodeQueryManager.getModel(entity.getRdpIDX(), entity.getParentWPNodeIDX());
		if (null != parentWPNode) {
			// 检验【配件检修作业节点】的完成状态
			if (validateFinishedStatus(parentWPNode) == null) {
				// 递归更新父节点的状态
				updateFinishedStatus(parentWPNode);
			}
		}
		// 程锐 2015-11-16 屏蔽此功能：流程驱动 - 开放该节点的直接后置节点
		 /*else {
			for(PartsRdpNode node : directAfters) {
				this.startUp(node);
			}
		}*/
        if (partsRdpNodeQueryManager.canCompleteRdp(entity.getRdpIDX())) {
        	PartsRdp rdp = partsRdpManager.getModelById(entity.getRdpIDX());
        	if (partsRepairListManager.isHgysByPartsType(rdp.getPartsTypeIDX()))
        		partsRdpManager.finishPartsRdp(entity.getRdpIDX(), "", PartsRdp.STATUS_DYS);
        	else {
        		//工单、质检完成则配件检修完成
        		List entityList = partsRdpTecCardManager.getModels(entity.getRdpIDX());
        		List<String> msgList = new ArrayList<String>();
        	    if (null != entityList && entityList.size() > 0) {
        	        partsRdpTecCardManager.checkFinishStatus(entityList, msgList);
        	        if (msgList.size() > 0) {
        	            return;
        	        }
        	    }
        	    
                entityList = partsRdpRecordCardManager.getModels(entity.getRdpIDX());
        	    if (null != entityList && entityList.size() > 0) {
        	        partsRdpRecordCardManager.checkFinishStatus(entityList, msgList);
                    if (msgList.size() > 0) {
                        return;
                    }
        	    }
        		
        		entityList = partsRdpNoticeManager.getModels(entity.getRdpIDX());
        		if (null != entityList && entityList.size() > 0) {
        			partsRdpNoticeManager.checkFinishStatus(entityList, msgList);
        			if (msgList.size() > 0) {
                        return;
                    }
        		}
        		if (partsRdpQRQueryManager.hasDCLQCByRdp(entity.getRdpIDX()))
        			return;
        		rdp.setStatus(PartsRdp.STATUS_JXHG);										
        		rdp.setRealEndTime(Calendar.getInstance().getTime());
        		rdp.setRepairResultDesc("修竣提交");
        		partsRdpManager.saveOrUpdate(rdp);
        		PartsAccount partsAccount = new PartsAccount();
				partsAccount = partsAccountManager.getModelById(rdp.getPartsAccountIDX());
                //配件状态为【检修中】状态，配件检修作业“合格验收”后，设置配件状态为“良好不在库” 
                if (partsAccount != null && partsAccount.getPartsStatus().contains(PartsAccount.PARTS_STATUS_JXZ)){
                    //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
                	partsRdpManager.updatePartsAccountStatus(partsAccount, PartsAccount.PARTS_STATUS_LH, partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_LH, PartsAccount.PARTS_STATUS_LH_CH));
                }
        	}
        		
        }
	}
	
	/**
	 * <li>修竣提交节点前的验证
	 * <li>创建人：程锐
	 * <li>创建日期：2014-12-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idx 节点主键
	 * @return 验证信息
	 */	
	public String validateFinishedStatus(String idx) {
		if (null == idx || idx.trim().length() <= 0) {
			throw new BusinessException("配件检修作业节点主键为空");
		}
		PartsRdpNode entity = this.getModelById(idx);
		if (null == entity) {
			throw new BusinessException("数据异常-未查询到【配件检修作业节点】对象 - idx[" + idx + "]");
		}
		// 检验【配件检修作业节点】的完成状态
        String validateFinishedStatus = validateFinishedStatus(entity);
        return validateFinishedStatus;
	}

	/**
	 * <li>更新【配件检修作业节点】的状态为“已处理”
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-24
	 * <li>修改人：何涛
	 * <li>修改日期：2015-01-23
	 * <li>修改内容：规避工单未挂接在节点上可能引发的错误
	 * 
	 * @param idx 配件检修作业节点主键
	 * @throws Exception 
	 */
	public void updateFinishedStatus(String idx) throws Exception {
		PartsRdpNode entity = this.getModelById(idx);
		this.updateFinishedStatus(entity);
	}
	
	/**
	 * <li>该作业节点下有“领活”操作发生时，将该作业节点的“实际开始时间”字段设置为“领活”操作发生时的实际时间
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-24
	 * <li>修改人：何涛
	 * <li>修改日期：2015-01-23
	 * <li>修改内容：规避工单未挂接在节点上可能引发的错误
	 * 
	 * @param idx 配件检修作业节点主键
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void updateRealStartTime(String idx) throws BusinessException, NoSuchFieldException {
		if (null == idx || idx.trim().length() <= 0) {
			return;
		}
		PartsRdpNode entity = this.getModelById(idx);
		if (null == entity) {
			throw new BusinessException("数据异常-未查询到【配件检修作业节点】对象 - idx[" + idx + "]");
		}
		this.updateRealStartTime(entity);
	}
	
	/**
	 * <li>该作业节点下有“领活”操作发生时，将该作业节点的“实际开始时间”字段设置为“领活”操作发生时的实际时间
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 配件检修作业节点实体
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	private void updateRealStartTime(PartsRdpNode entity) throws BusinessException, NoSuchFieldException {
		if (null != entity.getRealStartTime()) {
			return;
		}
		// 设置“实际开始时间”为当前时间
		entity.setRealStartTime(Calendar.getInstance().getTime());
		this.saveOrUpdate(entity);
		// 更新作业的“实际开始时间”字段
		partsRdpManager.updateRealStartTime(entity.getRdpIDX());
	}
	
	/**
	 * <li>检验【配件检修作业节点】的状态是否为指定的预期状态
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 配件检修作业节点实体
	 * @param status 预期的状态
	 * @return String 验证消息
	 */
	public String validateStatus(PartsRdpNode entity, String status) {
		if (status.equals(entity.getStatus())) {
			return null;
		}
        StringBuilder sb = new StringBuilder();
        String startMsg = "数据异常-【配件检修作业节点】idx[";
        String endMsg = ")";
		if (null == entity.getStatus()) {
            return sb.append(startMsg).append(entity.getIdx()).append("]状态为空").toString();
		}
		if (entity.getStatus().equals(PartsRdpNode.CONST_STR_STATUS_WCL)) {
		    return sb.append(startMsg).append(entity.getIdx()).append("]当前状态为未处理（").append(PartsRdpNode.CONST_STR_STATUS_WCL).append(endMsg).toString();
		}
		if (entity.getStatus().equals(PartsRdpNode.CONST_STR_STATUS_CLZ)) {
		    return sb.append(startMsg).append(entity.getIdx()).append("]当前状态为处理中（").append(PartsRdpNode.CONST_STR_STATUS_CLZ).append(endMsg).toString();
		}
		if (entity.getStatus().equals(PartsRdpNode.CONST_STR_STATUS_YCL)) {
		    return sb.append(startMsg).append(entity.getIdx()).append("]当前状态为已处理（").append(PartsRdpNode.CONST_STR_STATUS_YCL).append(endMsg).toString();
		}
        return sb.append(startMsg).append(entity.getIdx()).append("]状态为（").append(entity.getStatus()).append(endMsg).toString();
	}
    
    /**
     * <li>说明：单纯的更新父节点的状态（递归）
     * <li>创建人：何涛
     * <li>创建日期：2015-11-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 当前作业流程节点
     * @param status 要更新的目标状态
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    private void updateParentNodeStatus(PartsRdpNode entity, String status) throws BusinessException, NoSuchFieldException {
        String hql = "From PartsRdpNode Where recordStatus = 0 And rdpIDX = ? And wpNodeIDX = ?";
        PartsRdpNode parentNode = (PartsRdpNode) this.daoUtils.findSingle(hql, new Object[]{ entity.getRdpIDX(), entity.getParentWPNodeIDX()});
        if (null == parentNode) {
            return;
        }
        parentNode.setStatus(status);
        this.saveOrUpdate(parentNode);
        // 递归逐渐更新所有父节点
        this.updateParentNodeStatus(parentNode, status);
    }
	
	/**
	 * <li>流程驱动 - 开放指定【配件检修作业节点】下的所有工单：设置“检修工艺工单”、“检修记录工单”的状态为【待领取】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-24
	 * <li>修改人：何涛
	 * <li>修改日期：2014-12-31
	 * <li>修改内容：
	 * <ol>
	 * <li>删除对“提票工单”的开放操作，因为根据需求“提票工单”不和作业节点有关联
	 * <li>如果节点为叶子节点，并且没有挂载任何工单，则将该节点状态设置为“已处理”
	 * <li>如果节点为父节点，并且没有子节点，则将该节点状态设置为“已处理”
	 * </ol>
	 * 
	 * @param entity 配件检修作业节点实体
	 * @throws Exception 
	 */
	private void startUp(PartsRdpNode entity) throws Exception {
		// 验证【配件检修作业节点】的当前状态是否为“未处理”
		String errMsg = this.validateStatus(entity, PartsRdpNode.CONST_STR_STATUS_WCL);
		if (null != errMsg) {
			throw new BusinessException(errMsg);
		}
		// 将【配件检修作业节点】的状态设置为“处理中”
		entity.setStatus(PartsRdpNode.CONST_STR_STATUS_CLZ);
        // 设置作业节点的实际开始时间
        if (null == entity.getRealStartTime()) {
            entity.setRealStartTime(Calendar.getInstance().getTime());
        }
		this.saveOrUpdate(entity);
        // 逐级更新父节点状态为“处理中”
        this.updateParentNodeStatus(entity, PartsRdpNode.CONST_STR_STATUS_CLZ);
		// 如果作业节点是叶子节点，则开放该节点所挂的所有工单
		if (entity.getIsLeaf() == WPNode.CONST_INT_IS_LEAF_YES) {
			// 如果节点下没有挂载任何工单，则直接将该节点状态设置为“已处理”
			// 检验【配件检修作业节点】的完成状态
			if (validateFinishedStatus(entity) == null) {
				this.updateFinishedStatus(entity);
			} else {
				// 获取该作业节点下的所有“检修工艺工单”
				List<PartsRdpTecCard> tecCardList = this.partsRdpTecCardManager.getModels(entity.getRdpIDX(), entity.getIdx());
				// 开放“检修工艺工单”
				if(null != tecCardList && tecCardList.size() > 0) {
					partsRdpTecCardManager.startUp(tecCardList);
				}
				// 获取该作业节点下的所有“检修记录工单”
				List<PartsRdpRecordCard> recordCardList = this.partsRdpRecordCardManager.getModels(entity.getRdpIDX(), entity.getIdx());
				// 开放“检修记录工单”
				if(null != recordCardList && recordCardList.size() > 0) {
					partsRdpRecordCardManager.startUp(recordCardList);
				}
			}
		// 如果作业节点是父节点，要开放其子作业节点，其中，如果其子节点存在前后置关系，只开放没有前置节点的子节点【递归】
		} else {
			List<PartsRdpNode> directChildren = partsRdpNodeQueryManager.getDirectChildren(entity.getRdpIDX(), entity.getWpNodeIDX());
			// 如果该父节点下没有直接子节点，则更新该父节点的状态为已处理
			if (null == directChildren || directChildren.size() <= 0) {
				this.updateFinishedStatus(entity);
			} else {
				for (PartsRdpNode node : directChildren) {
					// 如果没有直接“前置节点”，则递归开放该节点
					if (!this.partsRdpNodeSeqManager.hasDirectBefores(node.getRdpIDX(), node.getWpNodeIDX())) {
						startUp(node);
					}
				}
			}
		}
	}
	
	/**
	 * <li>流程驱动 - 开放指定【配件检修作业节点】下的所有工单：设置“检修工艺工单”、“检修记录工单”和“提票工单”的状态为【待领取】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx 配件检修作业节点主键
	 * @throws Exception 
	 */
	public void startUp(String idx) throws Exception {
		PartsRdpNode entity = this.getModelById(idx);
		if (null == entity) {
			throw new BusinessException("数据异常-未查询到【配件检修作业节点】对象 - idx[" + idx + "]");
		}
		this.startUp(entity);
	}
	
	/**
	 * <li>获取指定“上级作业节点”下的第一级可开放的（没有后置作业节点）作业节点
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpIDX 作业主键 
	 * @param parentWPNodeIDX 上级作业节点，第一级的上级作业节点为“ROOT_0”
	 * @return List<PartsRdpNode> 配件检修作业节点实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<PartsRdpNode> getFirstLevelRdpNode(String rdpIDX, String parentWPNodeIDX) {
		String hql = "From PartsRdpNode Where recordStatus = 0 And rdpIDX = ? And parentWPNodeIDX = ? And wpNodeIDX Not In(Select wPNodeIDX From PartsRdpNodeSeq Where recordStatus = 0 And rdpIDX = ?)";
		return this.daoUtils.find(hql, new Object[]{rdpIDX, parentWPNodeIDX, rdpIDX});
	}

	/**
	 * <li>说明：返修后，如果该工单挂接在作业节点上，应更新作业节点状态
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-24
	 * <li>修改人：何涛
	 * <li>修改日期：2016-03-04
	 * <li>修改内容：修改节点返修后，节点的状态为“返修”，而非处理中
	 * 
	 * @param rdpNodeIDX 作业节点主键
	 * @throws Exception 
	 */
	public void updateToBack(String rdpNodeIDX) throws Exception {
		if (StringUtil.isNullOrBlank(rdpNodeIDX)) {
			return;
		}
		PartsRdpNode entity = this.getModelById(rdpNodeIDX);
		if (entity == null)
            return;
        // Modified by hetao on 2016-03-04 11:40
		// 如果节点的状态为“已处理”，则应更新其状态为“返修”
		if (PartsRdpNode.CONST_STR_STATUS_YCL.equals(entity.getStatus())) {
			entity.setStatus(PartsRdpNode.CONST_STR_STATUS_FX);
		}
		
		// 如果作业节点还有上级作业节点，还应更新上级作业节点的状态为“返修”
		if(!"ROOT_0".equals(entity.getParentWPNodeIDX())) {
			PartsRdpNode parentEntity = partsRdpNodeQueryManager.getModel(entity.getRdpIDX(), entity.getParentWPNodeIDX());
			if (null != parentEntity) {
				updateToBack(parentEntity.getIdx());
			}
		}
		saveOrUpdate(entity);		
	}
	
	/**
	 * <li>方法说明：保存回退节点记录
	 * <li>方法名：saveBackRepairRecord
	 * @param rdpNodeIdx 节点主键
	 * @param cause 原因
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-30
	 * <li>修改人：何涛
	 * <li>修改内容：2016-03-4
	 * <li>修改日期：重构
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	private void saveBackRepairRecord(String rdpNodeIdx, String cause) throws BusinessException, NoSuchFieldException{
        PartsNodeRe re = new PartsNodeRe();
        re.setRdpNodeIDX(rdpNodeIdx);   // 返修作业节点主键
        re.setRebackCause(cause);       // 返修原因
        PartsNodeReManager partsNodeReManager = (PartsNodeReManager) Application.getSpringApplicationContext().getBean("partsNodeReManager");
        partsNodeReManager.saveOrUpdate(re);
	}
	
	/**
	 * <li>方法说明： 回退节点操作
	 * <li>方法名：updateBack
	 * @param rdpIdx 作业主键
	 * @param cause 原因
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-30
	 * <li>修改人：张迪 
	 * <li>修改内容：添加返修提票操作,不关联节点
	 * <li>修改日期：
	 * @throws Exception 
	 */
	public void updateBack(String rdpIdx, String cause) throws Exception{
        // 保存节点返修原因
//		saveBackRepairRecord(rdpNodeIdx, cause);
//		updateToBack(rdpNodeIdx);
//        PartsRdpNode entity = this.getModelById(rdpNodeIdx);
//        if (entity == null)
//            return;
        // 仅封装了“作业主键”和“返修类型”字段值的提票单对象数组
        PartsRdpNotice notice = new PartsRdpNotice();
        notice.setRdpIDX(rdpIdx);
        notice.setNoticeDesc(cause);       
        String[] validateMsg = partsRdpNoticeManager.validateUpdate(notice);
        if (null != validateMsg) {
            return;
        }
        // 通过数据字典获取返修提票类型名称
        EosDictEntry eos = eosDictEntrySelectManager.getEosDictEntry("PJJX_Parts_Rdp_Notice_type",PartsRdpNotice.type_fgx);        
        if(null != eos){
            notice.setType(eos.getDictname());
        } else{
            notice.setType("返工修");
        }
        partsRdpNoticeManager.submitNotice(notice);
//        partsRdpManager.updateStatus(PartsRdp.STATUS_JXZ, new String[]{entity.getRdpIDX()});
	}
    
    /**
     * <li>说明：递归查询配件检修作业计划下属所有流程节点
     * <li>创建人：何涛
     * <li>创建日期：2015-10-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 上级主键
     * @param rdpIDX 作业主键
     * @return 树层次结构的所有节点
     */
    @SuppressWarnings("unchecked")
    public List<Node> tree(String parentIDX, String rdpIDX) {
        String hql = "From PartsRdpNode Where recordStatus = 0 And parentWPNodeIDX = ? And rdpIDX = ? Order By seqNo";
        if (StringUtil.isNullOrBlank(parentIDX)) {
            parentIDX = "ROOT_0";
        }
        List<PartsRdpNode> list = this.daoUtils.find(hql, new Object[]{ parentIDX, rdpIDX });
        List<Node> nodeList = new ArrayList<Node>();
        Node node = null;
        for (PartsRdpNode rdpNode : list) {
            node = new Node();
            node.setId(rdpNode.getWpNodeIDX());
            node.setRdpNodeIDX(rdpNode.getIdx());
            // 修改树节点按不同节点状态显示为不同颜色的字体
            String text = "?";
            // “未处理”的以灰色字体显示
            if (PartsRdpNode.CONST_STR_STATUS_WCL.equals(rdpNode.getStatus())) {
                text = "<div style='color: gray;'>?</div>";
            }
            // “返修的”以淡红色字体显示
            if (PartsRdpNode.CONST_STR_STATUS_FX.equals(rdpNode.getStatus())) {
                text = "<div style='color: red;'>?</div>";
            }
            node.setText(text.replace("?", rdpNode.getWpNodeName()));
            /* 是否叶子节点,0:否；1：是 */
            node.setLeaf(rdpNode.getIsLeaf() == 1);
            // 非叶子节点查询其下属所有子节点（递归）
            if (!node.isLeaf()) {
                node.setChildren(this.tree(node.getId(), rdpIDX));
            }
            nodeList.add(node);
        }
        return nodeList;
    }
    
    /**
     * <li>说明：通过配件idx与机车兑现单idx查询节点列表
     * <li>创建人：张迪
     * <li>创建日期：2017-3-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partIDX 配件idx
     * @param workPlanIDX 机车兑现单idx
     * @return 节点列表
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Object> getEntityByPartIDX(String partsTypeIDX, String wpIDX, String workPlanIDX) throws Exception {
        List<Object> objectList = new ArrayList<Object>();
        // 查询配件所对应的兑现记录单idx
        String sql = " select idx  from PJJX_Parts_Rdp pr" +
            " where pr.parts_account_idx in (select pp.parts_account_idx  from pjwz_parts_unload_register pp  " +
            " where pp.rdp_idx = '" + workPlanIDX + "') and pr.parts_type_idx = '" +   partsTypeIDX +
            "' and   pr.wp_idx = '"+   wpIDX + "' and  pr.STATUS != '" + PartsRdp.STATUS_YZZ + "'";
        List partRdpIDXs = daoUtils.executeSqlQuery(sql);
        if(partRdpIDXs == null || partRdpIDXs.size() <= 0){
            return null;
        }
        String nodesql = "select  N.*, S.PRE_NODE_IDX, PR.parts_type_idx, PR.SPECIFICATION_MODEL, PR.PARTS_NAME, PR.PARTS_NO , PR.PARTS_ACCOUNT_IDX, PR.IDENTIFICATION_CODE" +
                " FROM PJJX_Parts_Rdp_Node N   left join  " +
                " (select N1.IDX AS PRE_NODE_IDX, S1.WP_NODE_IDX, N1.RDP_IDX FROM PJJX_Parts_Rdp_Node N1 left join PJJX_Parts_Rdp_Node_Seq S1 on N1.WP_NODE_IDX = S1.pRE_WP_NODE_IDX " +
                " AND N1.RDP_IDX = S1.RDP_IDX AND S1.RECORD_STATUS = 0) S ON N.WP_NODE_IDX = S.WP_NODE_IDX AND N.RDP_IDX = S.RDP_IDX " +
                " left join  PJJX_Parts_Rdp PR on n.rdp_idx = pr.idx and pr.record_status = 0 " +
                " WHERE  N.RDP_IDX ='#rdpIDX#' AND (N.PARENT_WP_NODE_IDX Is Null   Or N.PARENT_WP_NODE_IDX ='ROOT_0')" +
                " AND N.RECORD_STATUS = 0 Order By  N.PLAN_STARTTIME, N.SEQ_NO ASC ";
        
        for(Object rdpIDX: partRdpIDXs){
            String  nodesqlStr = nodesql.replace("#rdpIDX#", rdpIDX.toString());           
            List<PartsRdpNodeFlowSheetBean> nodeBeanList= daoUtils.executeSqlQueryEntity(nodesqlStr, PartsRdpNodeFlowSheetBean.class); 
            objectList.add(nodeBeanList);
        }       
        return  objectList; 
    }
    

    /**
     * <li>说明：通过节点idx查询节点详情及配件检修记录
     * <li>创建人：张迪
     * <li>创建日期：2017-4-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 节点idx
     * @return 节点
     */
    public PartsRdpNode getModelByIDX(String idx) {
        PartsRdpNode node = this.getModelById(idx);
        if(node == null)  return null; 
        node.setPartsRdp(partsRdpManager.getModelById(node.getRdpIDX()));
        return node;
    }
    /**
     * <li>标题: 机车检修管理信息系统
     * <li>说明: sencha touch (nestedlist)组件所需数据源的封装实体
     * <li>创建人：何涛
     * <li>创建日期：2015-10-16
     * <li>修改人: 
     * <li>修改日期：
     * <li>修改内容：
     * <li>版权: Copyright (c) 2008 运达科技公司
     * @author 信息系统事业部整备系统项目组
     * @version 1.0
     */
    @JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
    public static class Node {
        
        private String id;
        
        private String rdpNodeIDX;
        
        private String text;
        
        private boolean leaf;
        
        private List<Node> children;
        
        public List<Node> getChildren() {
            return children;
        }
        
        public void setChildren(List<Node> children) {
            this.children = children;
        }
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getRdpNodeIDX() {
            return rdpNodeIDX;
        }

        public void setRdpNodeIDX(String rdpNodeIDX) {
            this.rdpNodeIDX = rdpNodeIDX;
        }

        public boolean isLeaf() {
            return leaf;
        }
        
        public void setLeaf(boolean leaf) {
            this.leaf = leaf;
        }
        
        public String getText() {
            return text;
        }
        
        public void setText(String text) {
            this.text = text;
        }
        
    }

	
}