package com.yunda.jx.pjjx.partsrdp.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.IbaseComboTree;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.pjjx.partsrdp.IPartsRdpStatus;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdpWorker;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpWorker业务类,作业人员
 * <li>创建人：程梅
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsRdpWorkerManager")
public class PartsRdpWorkerManager extends JXBaseManager<PartsRdpWorker, PartsRdpWorker> implements IbaseComboTree {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** 多个作业人员的默认分隔符（;） */
	public static final String CONST_STR_WORKER_SEPARATOR = ";";
	
	/**
     * <li>说明：重写getBaseComboTree，获取下拉树前台store所需List<HashMap>对象
     * <li>创建人：何涛
     * <li>创建日期：2014-12-05
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param req http请求实体
     * @return List<HashMap> 下拉树前台store所需List<HashMap>对象
     * @throws Exception
     */
	@Override
	@SuppressWarnings("unchecked")
	public List<HashMap> getBaseComboTree(HttpServletRequest req) throws Exception {
		List<PartsRdpWorker> list = null;
		// 如果设置了queryHql参数，则queryHql查询优先
		String queryHql = req.getParameter("queryHql");
		if (!StringUtil.isNullOrBlank(queryHql)) {
			list = this.daoUtils.find(queryHql);
		} else {
			String entityJson = StringUtil.nvl(req.getParameter("queryParams"), Constants.EMPTY_JSON_OBJECT);
			PartsRdpWorker entity = JSONUtil.read(entityJson, PartsRdpWorker.class);
			// 查询指定配件检修作业主键（rdpIDX）下关联的人员
			StringBuilder sb = new StringBuilder("From PartsRdpWorker Where recordStatus = 0");
			// 必须指定配件检修作业主键（rdpIDX）
			sb.append(" And rdpIDX = '").append(entity.getRdpIDX()).append("'");
			
            String temp = " And workEmpID <> ";
			// 一般不在下拉树中列出当前正在处理的人员
			if (null != entity.getWorkEmpID() && entity.getWorkEmpID() > 0) {
				sb.append(temp).append(entity.getWorkEmpID().intValue());
			} else {
				// 如果页面没有传入当前的作业处理人员，则在后台进行自动过滤
				sb.append(temp).append(SystemContext.getOmEmployee().getEmpid().intValue());
			}
			list = this.daoUtils.find(sb.toString());
		}
		List<HashMap> children = new ArrayList<HashMap>();
		HashMap map = null;
		for (PartsRdpWorker worker : list) {
			map = new HashMap<String, Object>();
			map.put("id", worker.getWorkEmpID());			// 作业人员ID
			map.put("text", worker.getWorkEmpName());		// 作业人员名称
			map.put("leaf", true);							// true 表示是一个叶子节点
			map.put("checked", false);
			children.add(map);
		}
		return children;
	}
	
	/**
	 * <li>说明：重写getBaseComboTree，获取下拉树前台store所需List<HashMap>对象   PAD开发使用
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-05
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param queryHql 查询hql
	 * @param entityJson 封装了查询条件的JSON对象
	 * @return Page<PartsRdpWorker> 作业人员实体集合
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Page<PartsRdpWorker> findWorkerList(String queryHql , String entityJson) throws Exception {
		List<PartsRdpWorker> list = null;
		if (!StringUtil.isNullOrBlank(queryHql)) {
			list = this.daoUtils.find(queryHql);
		} else {
			PartsRdpWorker entity = JSONUtil.read(entityJson, PartsRdpWorker.class);
			// 查询指定配件检修作业主键（rdpIDX）下关联的人员
			StringBuilder sb = new StringBuilder("From PartsRdpWorker Where recordStatus = 0");
			// 必须指定配件检修作业主键（rdpIDX）
			if(entity != null && !StringUtil.isNullOrBlank(entity.getRdpIDX())){
				sb.append(" And rdpIDX = '").append(entity.getRdpIDX()).append("'");
			}
			
			// 一般不在下拉树中列出当前正在处理的人员
			if (null != entity.getWorkEmpID() && entity.getWorkEmpID() > 0) {
				sb.append(" And workEmpID <> ").append(entity.getWorkEmpID().intValue());
			} else {
				// 如果页面没有传入当前的作业处理人员，则在后台进行自动过滤
				sb.append(" And workEmpID <> ").append(SystemContext.getOmEmployee().getEmpid().intValue());
			}
			list = this.daoUtils.find(sb.toString());
		}
		
		return new Page<PartsRdpWorker>(list.size(), list);
	}
	
	/**
     * <li>说明：根据作业主键获取【作业人员】
     * <li>创建人：何涛
     * <li>创建日期：2014-12-08
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
	 * @param rdpIDX 作业主键
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<PartsRdpWorker> getModelsByRdpIDX(String rdpIDX) {
		String hql = "From PartsRdpWorker Where recordStatus = 0 And rdpIDX = ?";
		return this.daoUtils.find(hql, new Object[]{rdpIDX});
	}
	
	/**
	 * <li>说明：组装“作业人”字段值，因为页面没有包含当前用户
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param workEmpID 以分好分割的多个作业人ID
	 * @return String 以分好分割的多个作业人ID，包含当前作业处理人员
	 */
	public String formatWorkerID(String workEmpID) {
		OmEmployee omEmployee = SystemContext.getOmEmployee();
		// 当前登录用户的ID
		String empId = String.valueOf(omEmployee.getEmpid());
		// 如果“作业人”字段值为空，则返回当前登录用户ID
		if (null == workEmpID || workEmpID.trim().length() <= 0) {
			return String.valueOf(empId);
		}
		// 如果“作业人”字段值已经包含了当前用户ID，则直接返回
		if (workEmpID.contains(empId)) {
			return workEmpID;
		}
		StringBuilder sb = new StringBuilder(workEmpID);
		sb.append(CONST_STR_WORKER_SEPARATOR).append(empId);
		return sb.toString();
	}
	
	/**
	 * <li>说明：组装“作业人名称”字段值，因为页面没有包含当前用户
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
     * @param workEmpName 以分好分割的多个作业人名称
     * @return String 以分好分割的多个作业人名称，包含当前作业处理人员
	 */
	public String formatWorkerName(String workEmpName) {
		OmEmployee omEmployee = SystemContext.getOmEmployee();
		// 当前登录用户的名称
		String empName = omEmployee.getEmpname();
		// 如果“作业人名称”字段值为空，则返回当前登录用户名称
		if (null == workEmpName || workEmpName.trim().length() <= 0) {
			return String.valueOf(empName);
		}
		// 如果“作业人名称”字段值已经包含了当前用户名称，则直接返回
		if (workEmpName.contains(empName)) {
			return workEmpName;
		}
		StringBuilder sb = new StringBuilder(workEmpName);
		sb.append(CONST_STR_WORKER_SEPARATOR).append(omEmployee.getEmpname());
		return sb.toString();
	}
    
	/**
     * <li>说明：同一任务单下的施修人员须唯一
     * <li>创建人：程梅
     * <li>创建日期：2014-12-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param entity 作业人员实体
     * @return String[] 验证消息
     */
	@SuppressWarnings("unchecked")
	public String[] validateUpdate(PartsRdpWorker entity) throws BusinessException {
		List<String> errMsg = new ArrayList<String>();
        List<PartsRdpWorker> list = (List<PartsRdpWorker>) daoUtils.find(" from PartsRdpWorker o where o.recordStatus = 0 and " +
            "o.rdpIDX = '" + entity.getRdpIDX() + "' and o.workEmpID=" +entity.getWorkEmpID());
        if(list != null && list.size() > 0){
            errMsg.add("作业人员【"+ entity.getWorkEmpName() +"】在该任务单中已指派，不能重复指派！");
        }
		if (errMsg.size() > 0) {
			String[] errArray = new String[errMsg.size()];
			errMsg.toArray(errArray);
			return errArray;
		}
        return null;
    }
    
	/**
	 * <li>说明：批量派工
	 * <li>创建人：程梅
	 * <li>创建日期：2014-12-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param workerList 施修人员数组
	 * @param ids 作业主键数组
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void saveBatch(PartsRdpWorker[] workerList,Serializable... ids) throws BusinessException, NoSuchFieldException {
		PartsRdpWorker entity = null;
		List<PartsRdpWorker> entityList = new ArrayList<PartsRdpWorker>();
		// 增加批量派工指派的施修人员，只对新增的人员进行操作
		for (PartsRdpWorker partsRdpWorker : workerList) {
			for (Serializable idx : ids) {
				entity = new PartsRdpWorker();
				entity.setRdpIDX((String) idx);
				entity.setWorkEmpID(partsRdpWorker.getWorkEmpID());
				entity.setWorkEmpName(partsRdpWorker.getWorkEmpName());
				PartsRdpWorker temp = (PartsRdpWorker) this.daoUtils.findSingleByEntity(entity);
				if (null == temp) {
					entityList.add(entity);
				} else {
					entityList.add(temp);
				}
			}
		}
		// 删除批量派工时没有包含的的作业施修人员
		for (Serializable idx : ids) {
			List<PartsRdpWorker> models = this.getModelsByRdpIDX((String)idx);
			for (PartsRdpWorker worker : models) {
				if (isDeleteFromPage(workerList, worker)) {
					this.logicDelete(worker.getIdx());
				}
			}
		}
		this.saveOrUpdate(entityList);
	}
	
	/**
	 * <li>说明：检验批量派工时，是否删除了已指派的施修人员
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-31
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param workerList 页面批量派工指定的施修人员
	 * @param entity 当前作业已指定的施修人员
	 * @return boolean如果已经从页面删除则返回true，否则返回false
	 */
	private boolean isDeleteFromPage(PartsRdpWorker[] workerList, PartsRdpWorker entity) {
		for (PartsRdpWorker worker : workerList) {
			if (entity.getWorkEmpID() == worker.getWorkEmpID()) {
				return false;
			}
		}
		return true;
	}
	
	
    /**
     * <li>说明：已处理了工单的人员不能删除
     * <li>创建人：程梅
     * <li>创建日期：2014-12-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param ids 作业人员主键数组
     * @return String[] 验证消息
     */
	public String[] validateDelete(Serializable... ids){
		List<String> errMsg = new ArrayList<String>();
		PartsRdpWorker worker ;
		String sql ;
		List list = new ArrayList();
		for(Serializable id : ids){
			worker = new PartsRdpWorker();
			worker = getModelById(id.toString());
			sql = "select c.work_empid from PJJX_Parts_Rdp_Tec_Card c where c.record_status=0 and c.rdp_idx='"+worker.getRdpIDX()+
			"' and c.work_empid='"+worker.getWorkEmpID()+"' and c.Status not in ('" +IPartsRdpStatus.CONST_STR_STATUS_WKF+"','"+IPartsRdpStatus.CONST_STR_STATUS_YZZ+"') "+
					"union " +
					"select n.work_empid from PJJX_Parts_Rdp_Notice n where n.record_status=0 and n.rdp_idx='"+worker.getRdpIDX()+
					"' and n.work_empid='"+worker.getWorkEmpID()+"' and n.Status not in ('" +IPartsRdpStatus.CONST_STR_STATUS_WKF+"','"+IPartsRdpStatus.CONST_STR_STATUS_YZZ+"') "+
					"union " +
					"select r.work_empid from PJJX_Parts_Rdp_Record_Card r where r.record_status=0 and r.rdp_idx='"+worker.getRdpIDX()+
					"' and r.work_empid='"+worker.getWorkEmpID()+"' and r.Status not in ('" +IPartsRdpStatus.CONST_STR_STATUS_WKF+"','"+IPartsRdpStatus.CONST_STR_STATUS_YZZ+"') ";
			list = daoUtils.executeSqlQuery(sql);
			if(list != null && list.size() > 0){
	            errMsg.add("作业人员【"+ worker.getWorkEmpName() +"】已处理了工单，不能删除！");
	        }
		}
		if (errMsg.size() > 0) {
			String[] errArray = new String[errMsg.size()];
			errMsg.toArray(errArray);
			return errArray;
		}
		return null;
	}
		
	/**
	 * <li>说明：查询与指定作业人员协作的其他处理人员
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpIDX 作业主键
	 * @param empId 指定的人员ID
	 * @return List
	 */
	public List findWorkerList(String rdpIDX, Long empId) {
		String hql = "From PartsRdpWorker Where recordStatus = 0 And rdpIDX = ? And workEmpID <> ?";
		return this.daoUtils.find(hql, new Object[]{rdpIDX, empId});
	}
	
	/**
	 * <li>说明：查询与当前系统登录人员协作的其他处理人员
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rdpIDX 作业主键
	 * @return List
	 */
	public List findWorkerList(String rdpIDX) {
		Long empId = SystemContext.getOmEmployee().getEmpid();
		return this.findWorkerList(rdpIDX, empId);
	}
	
}