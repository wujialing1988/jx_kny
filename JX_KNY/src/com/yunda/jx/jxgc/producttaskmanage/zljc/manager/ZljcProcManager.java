package com.yunda.jx.jxgc.producttaskmanage.zljc.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;
import com.yunda.jx.jxgc.base.jcqcitemdefine.entity.JCQCItemDefine;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QualityControlCheckInfoVO;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCResultManager;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCResultQueryManager;
import com.yunda.jx.webservice.stationTerminal.base.entity.ProcessTaskListBean;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 质量检查处理业务类
 * <li>创建人：程锐
 * <li>创建日期：2014-10-11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="zljcProcManager")
public class ZljcProcManager extends JXBaseManager<Object, Object>{
	
	protected AcOperatorManager getAcOperatorManager(){
        return (AcOperatorManager)Application.getSpringApplicationContext().getBean("acOperatorManager");
    }
	
	protected OmEmployeeManager getOmEmployeeManager() {
		return (OmEmployeeManager) Application.getSpringApplicationContext().getBean("omEmployeeManager");
	}
	
	protected QCResultQueryManager getQCResultQueryManager() {
		return (QCResultQueryManager) Application.getSpringApplicationContext().getBean("qCResultQueryManager");
	}
	
	protected QCResultManager getQCResultManager() {
		return (QCResultManager) Application.getSpringApplicationContext().getBean("qCResultManager");
	}
	
	protected OmEmployeeSelectManager getOmEmployeeSelectManager() {
		return (OmEmployeeSelectManager) Application.getSpringApplicationContext().getBean("omEmployeeSelectManager");
	}
	/**
	 * 
	 * <li>说明：单条处理质量检查信息
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param checkInfo 检验信息Json对象
	 *  标识				数据类型	说明
		checkPersonName	String	检验人名称
		checkPersonIdx	String	检验人id 取operatorid
		checkTime		String	检验日期
		remarks			String	备注

	 * @param listBean 作业工单及质检项对象数组
	 *  标识				数据类型	说明
	 *  sourceIdx		String  作业工单idx 取sourceIdx
	 *  checkItemCode   String  质量检查编码
	 */
	public void saveQualityControlCheckInfo(QualityControlCheckInfoVO checkInfo,
											ProcessTaskListBean[] listBean) throws Exception {
		getQCResultManager().updateFinishQCResult(checkInfo, listBean);
	}
	
	/**
	 * 
	 * <li>说明：全部处理质量检查信息
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid 操作员ID
	 * @param remarks 备注
	 * @param queryString 查询字符串
	 *  标识				数据类型	  说明
		rdpIDX			String	生产任务单IDX
		workItemName	String	工作项名称
		taskDepict		String	任务描述
	 * @throws Exception
	 */
	public void saveAllQualityControlCheckInfo(Long operatorid, String remarks, String queryString) throws Exception {
		AcOperator ac = getAcOperatorManager().findLoginAcOprator(operatorid);
        OmEmployee emp = getOmEmployeeManager().findOmEmployee(operatorid);
        SystemContext.setAcOperator(ac);
        
        List<ProcessTaskListBean> listBean = getQCResultQueryManager().getQCList(emp.getEmpid(), JCQCItemDefine.CONST_INT_CHECK_WAY_BJ + "", queryString);        
        getQCResultManager().updateAllFinishQCResult(emp.getEmpid().toString(), emp.getEmpname(), new Date(), remarks, queryString, listBean);
	}
    
	/**
	 * <li>说明：查询生产任务单列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid 操作员ID
	 * @param mode 质量检查类型
     * @param start 开始行
     * @param limit 每页记录数
	 * @return 生产任务单列表
	 *  标识		数据类型	说明
		idx		String	生产任务单IDX	
		rdpText	String	生产任务单名称
	 * @throws Exception
	 */
	public Map queryRdpList(Long operatorid, String mode, int start, int limit) throws Exception {
		AcOperator ac = getAcOperatorManager().findLoginAcOprator(operatorid);
        SystemContext.setAcOperator(ac);
        Long empId = getOmEmployeeSelectManager().findEmpByOperator(operatorid).getEmpid();
        return getQCResultQueryManager().getRdpOfQuery(empId, mode, start, limit);
	}
	
	/**
	 * 
	 * <li>说明：查询质量检查列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param uid 操作员userID
	 * @param uname 操作员名称
	 * @param mode 质量检查类型 参照JxgcConstants中的质量检查必检、抽检 
	 * @param queryString 查询字符串
	 *  标识				数据类型	  说明
		rdpIDX			String	生产任务单IDX
		workItemName	String	工作项名称
		taskDepict		String	任务描述
     * @param start 开始行
     * @param limit 每页记录数
	 * @return 质量检查列表Map
	 *  标识				数据类型	说明
		idx				String	Idx 工作项ID
		trainType		String	车型（配件型）
		trainNo			String	车号（配件号）
		repairClassName	String	修程
		taskDepict		String	任务描述
		workItemName	String	工单名称
		processInstID	String	流程实例ID
		workItemID		String	工作项ID
		sourceIdx		String	业务主键ID
		token			String	业务类型
		actionType		String	数据查询类型标识
		repairtimeName	String	修次
		activityInstID	Long	活动实例ID
		processInstName	String	流程实例名称
		rdpIdx			String	兑现单主键
		key				String	存放主键 （流程工单主键processTaskIDX）
		parts			boolean	标识是否为配件
	 * @throws Exception
	 */
	public Map queryZljcList(String uid, 
							  String uname, 
							  String mode, 
							  String queryString, 
							  int start, 
							  int limit) throws Exception {		
		return getQCResultQueryManager().getQCPageList(uname, start, limit, mode, queryString).extjsResult();
	}
}
