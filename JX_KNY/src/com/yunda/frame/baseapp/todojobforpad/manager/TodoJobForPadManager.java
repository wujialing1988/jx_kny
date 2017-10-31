package com.yunda.frame.baseapp.todojobforpad.manager;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.frame.baseapp.roleproc.manager.RoleProcManager;
import com.yunda.frame.baseapp.todojobforpad.ITodoJobForPad;
import com.yunda.frame.baseapp.todojobforpad.entity.TodoJobForPad;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;
import com.yunda.jx.jxgc.base.jcqcitemdefine.entity.JCQCItemDefine;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkCardManager;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCResultQueryManager;
import com.yunda.jx.jxgc.tpmanage.manager.FaultQCResultQueryManager;
import com.yunda.jx.jxgc.tpmanage.manager.FaultTicketManager;
import com.yunda.jx.jxgc.webservice.entity.TrainWorkPlanBean;
import com.yunda.jx.pjjx.base.qcitemdefine.entity.QCItem;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpManager;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpZljyBean;
import com.yunda.jx.pjjx.partsrdp.qcinst.manager.PartsRdpQRQueryManager;
import com.yunda.jx.pjjx.partsrdp.wpinst.entity.PartsRdpNodeBean;
import com.yunda.jx.pjjx.partsrdp.wpinst.manager.PartsRdpNodeQueryManager;
import com.yunda.webservice.employee.entity.AcFunctionBean;
import com.yunda.zb.common.ZbConstants;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWi;
import com.yunda.zb.rdp.zbtaskbill.manager.ZbglRdpWiManager;
import com.yunda.zb.tp.entity.ZbglTp;
import com.yunda.zb.tp.manager.ZbglTpManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：pad移动终端待办事项业务类
 * <li>创建人：程锐
 * <li>创建日期：2014-12-8
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "todoJobForPadManager")
public class TodoJobForPadManager extends JXBaseManager<TodoJobForPad, TodoJobForPad> implements ITodoJobForPad {
    
    /** 日志工具 */
    Logger logger = Logger.getLogger(getClass());
    
    /** 人员选择业务类对象 */
    @Resource
    OmEmployeeSelectManager omEmployeeSelectManager;
    
    /** pad移动终端权限角色业务类对象 */
    @Resource
    RoleProcManager roleProcManager;
    
    private String strUnit = "条";
    private String strWaiting = "待处理";
    
	private int start = 0;
	private int limit = 20;
    
	/**
	 * <li>说明：查询待办事项统计列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-12-8
	 * <li>修改人：何涛
	 * <li>修改日期：2016-03-21
	 * <li>修改内容：添加查询代办项时的异常处理
     * @param workStationIDX 配件检修工位主键
	 * @return 待办事项统计列表
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TodoJobForPad> queryTodoJobList(String workStationIDX) throws Exception {
		Long operatorid = SystemContext.getAcOperator().getOperatorid();
		OmEmployee emp = omEmployeeSelectManager.findEmpByOperator(operatorid);
		List<AcFunctionBean> roleList = roleProcManager.queryRoleFuncList();
		
		List<TodoJobForPad> list = new LinkedList<TodoJobForPad>();
		for (AcFunctionBean acFunctionBean : roleList) {
            try {
                if (FUNC_LSXTP_MID.equals(acFunctionBean.getFunccode())) {
                    // 临碎修提票
                    list.add(getJobForLsxtp());
                } else if (FUNC_LXHCL_MID.equals(acFunctionBean.getFunccode())) {
                    // 临修活处理
                    list.add(getJobForLxhcl(emp));
                } else if (FUNC_SXHCL_MID.equals(acFunctionBean.getFunccode())) {
                    // 碎修活处理
                    list.add(getJobForSxhcl(emp));
                } else if (FUNC_LSXJY_MID.equals(acFunctionBean.getFunccode())) {
                    // 碎修活处理
                    list.add(getJobForLsxjy(emp));
                } else if(FUNC_ZBZYGD_MID.equals(acFunctionBean.getFunccode())) {
                    // 整备作业工单处理
                    list.add(getJobForZbzygd(emp));
                } else if(FUNC_JXZYGD_MID.equals(acFunctionBean.getFunccode())) {
                    // 机车检修作业工单处理
                    list.add(getJobForZygd(emp));
                } else if(FUNC_ZLJC_MID.equals(acFunctionBean.getFunccode())) {
                    // 质量检查处理
                    list.add(getJobForZljc(emp));
                } else if(FUNC_JCTP_MID.equals(acFunctionBean.getFunccode())) {
                    // 检查提票
                    list.add(getJobForJctp());
                } else if(FUNC_TPDDPG_MID.equals(acFunctionBean.getFunccode())) {
                    // 提票调度派工
                    list.add(getJobForTpddpg());
                } else if(FUNC_TPGZPG_MID.equals(acFunctionBean.getFunccode())) {
                    // 提票工长派工
                    list.add(getJobForTpgzpg());
                } else if(FUNC_JCTPCL_MID.equals(acFunctionBean.getFunccode())) {
                    // 检查提票处理
                    list.add(getJobForJctpcl());
                } else if(FUNC_JCTPZJ_MID.equals(acFunctionBean.getFunccode())) {
                    // 检查提票质量检验
                    list.add(getJobForJctpzj());
                } else if(FUNC_PJJXCL_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件检修处理
                    list.add(getJobForPjjxcl(workStationIDX));
                } else if(FUNC_PJJXZJ_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件检修质检
                    list.add(getJobForPjjxzj());
                } else if(FUNC_PJJXYS_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件检修验收
                    list.add(getJobForPjjxys());
                } else if(FUNC_PJJXJD_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件检修进度
                    list.add(getJobForPjjxjd());
                } else if(FUNC_XCPJDJ_MID.equals(acFunctionBean.getFunccode())) {
                    // 下车配件登记
                    list.add(getJobForXcpjdj());
                } else if(FUNC_SCPJDJ_MID.equals(acFunctionBean.getFunccode())) {
                    // 上车配件登记
                    list.add(getJobForScpjdj());
                } else if(FUNC_LHPJDJ_MID.equals(acFunctionBean.getFunccode())) {
                    // 良好配件登记
                    list.add(getJobForLhpjdj());
                } else if(FUNC_PJWWDJ_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件委外登记
                    list.add(getJobForPjwwdj());
                } else if(FUNC_XJPJRK_MID.equals(acFunctionBean.getFunccode())) {
                    // 修竣配件入库
                    list.add(getJobForXjpjrk());
                } else if(FUNC_PJCKDJ_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件出库
                    list.add(getJobForPjckdj());
                } else if(FUNC_PJBFDJ_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件报废登记
                    list.add(getJobForPjbfdj());
                } else if(FUNC_PJDCDJ_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件调出登记
                    list.add(getJobForPjdcdj());
                } else if(FUNC_PJXXCX_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件信息查询
                    list.add(getJobForPjxxcx());
                } else if(FUNC_PJSBM_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件识别码绑定
                    list.add(getJobForPjsbm());
                } else if(FUNC_PJTKDJ_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件退库登记
                    list.add(getJobForPjtkdj());
                } else if(FUNC_PJXZDJ_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件销账登记
                    list.add(getJobForPjxzdj());
                } else if(FUNC_PJCXDJ_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件拆卸登记
                    list.add(getJobForPjcxdj());
                } else if(FUNC_PJAZDJ_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件安装登记
                    list.add(getJobForPjazdj());
                } else if(FUNC_PJAZDJ_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件安装登记
                    list.add(getJobForPjazdj());
                } else if(FUNC_JXSBM_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件检修记录单识别码绑定
                    list.add(getJobForJxsbmbd());
                } else if(FUNC_PJWWHD_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件委外回段
                    list.add(getJobForPjwwhd());
                } else if(FUNC_PJYKDJ_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件移库登记
                    list.add(getJobForPjykdj());
                } else if(FUNC_PJJY_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件校验
                    list.add(getJobForPjjy());
                } else if(FUNC_PJJJ_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件交接
                    list.add(getJobForPjjj());
                } else if(FUNC_PJSHRD_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件售后入段
                    list.add(getJobForPjshrd());
                } else if(FUNC_PJSHCD_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件售后出段
                    list.add(getJobForPjshcd());
                } else if(FUNC_PJJXZJN_MID.equals(acFunctionBean.getFunccode())) {
                    // 配件检修校验新
                    list.add(getJobForPjjxzjn());
                } else if(FUNC_JCZLJC_MID.equals(acFunctionBean.getFunccode())) {
                    // 机车检修校验新
                    list.add(getJobForJczljc(emp));
                } else if(FUNC_ZBXCPJDJ_MID.equals(acFunctionBean.getFunccode())) {
                    // 整备下车配件登记
                    list.add(getJobForZbxcpjdj());
                } else if(FUNC_ZBSCPJDJ_MID.equals(acFunctionBean.getFunccode())) {
                    // 整备上车配件登记
                    list.add(getJobForZbscpjdj());
                }
            } catch (Exception e) {
                ExceptionUtil.process(e, logger);
                continue;
            }
		}	
		return list;
	}
    
    /**
     * <li>说明：整备下车配件登记
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端整备下车配件登记实体类
     * @throws Exception
     */
    private TodoJobForPad getJobForZbxcpjdj() throws Exception{
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_ZBXCPJDJ_NAME);
        job.setMid(FUNC_ZBXCPJDJ_MID);
        job.setIcon(FUNC_ZBXCPJDJ_ICON);
        return job;
    }
    
    /**
     * <li>说明：整备上车配件登记
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端整备下车配件登记实体类
     * @throws Exception
     */
    private TodoJobForPad getJobForZbscpjdj() throws Exception{
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_ZBSCPJDJ_NAME);
        job.setMid(FUNC_ZBSCPJDJ_MID);
        job.setIcon(FUNC_ZBSCPJDJ_ICON);
        return job;
    }
    
    /**
     * <li>说明：售后配件出段
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-6-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端配件售后配件出段实体类
     * @throws Exception
     */
    private TodoJobForPad getJobForPjshcd() throws Exception{
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_PJSHCD_NAME);
        job.setMid(FUNC_PJSHCD_MID);
        job.setIcon(FUNC_PJSHCD_ICON);
        return job;
    }
    
    /**
     * <li>说明：售后配件入段
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-6-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端配件售后配件入段实体类
     * @throws Exception
     */
    private TodoJobForPad getJobForPjshrd() throws Exception{
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_PJSHRD_NAME);
        job.setMid(FUNC_PJSHRD_MID);
        job.setIcon(FUNC_PJSHRD_ICON);
        return job;
    }
    
    /**
     * <li>说明：配件交接
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-6-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端配件交接实体类
     * @throws Exception
     */
    private TodoJobForPad getJobForPjjj() throws Exception{
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_PJJJ_NAME);
        job.setMid(FUNC_PJJJ_MID);
        job.setIcon(FUNC_PJJJ_ICON);
        return job;
    }
    
    /**
     * <li>说明：配件校验
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-6-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端配件校验实体类
     * @throws Exception
     */
    private TodoJobForPad getJobForPjjy() throws Exception{
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_PJJY_NAME);
        job.setMid(FUNC_PJJY_MID);
        job.setIcon(FUNC_PJJY_ICON);
        return job;
    }
    
    /**
     * <li>说明：查询配件移库登记
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-6-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端配件移库登记实体类
     * @throws Exception
     */
    private TodoJobForPad getJobForPjykdj() throws Exception{
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_PJYKDJ_NAME);
        job.setMid(FUNC_PJYKDJ_MID);
        job.setIcon(FUNC_PJYKDJ_ICON);
        return job;
    }

    /**
     * <li>说明：查询临碎修提票待办事项
     * <li>创建人：何涛
     * <li>创建日期：2015-06-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     * @throws Exception
     */
    private TodoJobForPad getJobForLsxtp() throws Exception{
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_LSXTP_NAME);
        job.setMid(FUNC_LSXTP_MID);
        job.setIcon(FUNC_LSXTP_ICON);
        return job;
    }
    
    /**
     * <li>说明：查询临修活处理待办事项，查询当前系统操作员所在班组已被派工的临碎修提票活项
     * <li>创建人：何涛
     * <li>创建日期：2015-7-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param emp 当前系统登录人员
     * @return TodoJobForPad pad移动终端待办事项实体类
     * @throws Exception 
     */
    private TodoJobForPad getJobForLxhcl(OmEmployee emp) throws Exception {
        ZbglTpManager manager = (ZbglTpManager) Application.getSpringApplicationContext().getBean("zbglTpManager");
        // 当前系统操作员id
        Long operatorid = emp.getOperatorid();
        
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_LXHCL_NAME);
        job.setMid(FUNC_LXHCL_MID);
        job.setIcon(FUNC_LXHCL_ICON);
        
        // 待领活记录条数
        int countForDjh = manager.getTpCount(null, ZbConstants.REPAIRCLASS_LX, ZbglTp.STATUS_DRAFT, operatorid);
        if (countForDjh > 0) {
            job.setInfo1("待领活" + countForDjh + strUnit);
        }
        // 待销活记录条数
        int countForDxh = manager.getTpCount(null, ZbConstants.REPAIRCLASS_LX, ZbglTp.STATUS_OPEN, operatorid);
        if (countForDxh > 0) {
            job.setInfo2("待销活" + countForDxh + strUnit);
        }
        job.setTotalCount(countForDxh <= 0 ? null : countForDxh + "");
        return job;
    }
    
    /**
     * <li>说明：查询范围活处理待办事项
     * <li>创建人：何涛
     * <li>创建日期：2015-7-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param emp 当前系统登录人员
     * @return TodoJobForPad pad移动终端待办事项实体类
     * @throws Exception 
     */
    private TodoJobForPad getJobForSxhcl(OmEmployee emp) throws Exception {
        ZbglTpManager manager = (ZbglTpManager) Application.getSpringApplicationContext().getBean("zbglTpManager");
        
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_SXHCL_NAME);
        job.setMid(FUNC_SXHCL_MID);
        job.setIcon(FUNC_SXHCL_ICON);
        
        // 待领活记录条数
        ZbglTp entity = new ZbglTp();
        entity.setRepairClass(ZbConstants.REPAIRCLASS_SX);
        entity.setFaultNoticeStatus(ZbglTp.STATUS_DRAFT);
        SearchEntity<ZbglTp> searchEntity = new SearchEntity<ZbglTp>(entity, start, limit, null);
//        int countForDjh = manager.getTpCount(null, ZbConstants.REPAIRCLASS_SX, ZbglTp.STATUS_DRAFT, operatorid);
        int countForDjh = manager.findPageQuery(searchEntity, null).getTotal();
        if (countForDjh > 0) {
            job.setInfo1("待领活" + countForDjh + strUnit);
        }
        // 待销活记录条数
        entity.setFaultNoticeStatus(ZbglTp.STATUS_OPEN);
//        int countForDxh = manager.getTpCount(null, ZbConstants.REPAIRCLASS_SX, ZbglTp.STATUS_OPEN, operatorid);
        int countForDxh = manager.findPageQuery(searchEntity, null).getTotal();
        if (countForDxh > 0) {
            job.setInfo2("待销活" + countForDxh + strUnit);
        }
        job.setTotalCount(countForDxh <= 0 ? null : countForDxh + "");
        return job;
    }
    
    
    /**
     * <li>说明：查询临碎修质量检查处理待办事项
     * <li>创建人：何涛
     * <li>创建日期：2015-8-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param emp 当前系统登录人员
     * @return TodoJobForPad pad移动终端待办事项实体类
     * @throws Exception 
     */
    private TodoJobForPad getJobForLsxjy(OmEmployee emp) throws Exception {
        ZbglTpManager manager = (ZbglTpManager) Application.getSpringApplicationContext().getBean("zbglTpManager");

        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_LSXJY_NAME);
        job.setMid(FUNC_LSXJY_MID);
        job.setIcon(FUNC_LSXJY_ICON);
        
        // 临修活质检记录数
        Page page = manager.getZbglTpQCPageList(emp.getEmpid(), start, limit, ZbConstants.REPAIRCLASS_LX, null);
        int countForLx = page.getTotal();
//        if (countForLx > 0) {
//            job.setInfo1("临修活" + countForLx + strUnit);
//        }
        // 范围活质检记录数
        page = manager.getZbglTpQCPageList(emp.getEmpid(), start, limit, ZbConstants.REPAIRCLASS_SX, null);
        int countForSx = page.getTotal();
//        if (countForSx > 0) {
//            job.setInfo2("范围活" + countForSx + strUnit);
//        }
        if (countForLx + countForSx > 0) {
            job.setInfo1(strWaiting + (countForLx + countForSx) + strUnit);
            job.setTotalCount(countForLx + countForSx + "");
        }
        return job;
    }
    
    /**
     * <li>说明：查询整备作业工单处理待办事项
     * <li>创建人：何涛
     * <li>创建日期：2015-7-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param emp 当前系统登录人员
     * @return TodoJobForPad pad移动终端待办事项实体类
     * @throws Exception 
     */
    private TodoJobForPad getJobForZbzygd(OmEmployee emp) throws Exception {
        ZbglRdpWiManager manager = (ZbglRdpWiManager) Application.getSpringApplicationContext().getBean("zbglRdpWiManager");
        // 当前系统操作员id
        Long operatorid = emp.getOperatorid();
        
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_ZBZYGD_NAME);
        job.setMid(FUNC_ZBZYGD_MID);
        job.setIcon(FUNC_ZBZYGD_ICON);
        
        // 待领活记录条数
        int countForDjh = manager.getRdpWiCount(ZbglRdpWi.STATUS_TODO, operatorid);
        if (countForDjh > 0) {
            job.setInfo1("待领活" + countForDjh + strUnit);
        }
        // 待销活记录条数
        int countForDxh = manager.getRdpWiCount(ZbglRdpWi.STATUS_HANDLING, operatorid);
        if (countForDxh > 0) {
            job.setInfo2("待销活" + countForDxh + strUnit);
        }
        job.setTotalCount(countForDxh <= 0 ? null : countForDxh + "");
        return job;
    }
    
    /**
     * <li>说明：查询机车检修作业工单处理待办事项
     * <li>创建人：何涛
     * <li>创建日期：2015-7-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param emp 当前系统登录人员
     * @return TodoJobForPad pad移动终端待办事项实体类
     * @throws Exception
     */
    private TodoJobForPad getJobForZygd(OmEmployee emp) throws Exception{
        WorkCardManager workCardManager = (WorkCardManager) Application.getSpringApplicationContext().getBean("workCardManager");
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_JXZYGD_NAME);
        job.setMid(FUNC_JXZYGD_MID);
        job.setIcon(FUNC_JXZYGD_ICON);
        Integer count = workCardManager.findTaskCount(emp.getEmpid(), emp.getOrgid());
        if (null != count && count.intValue() > 0) {
            job.setInfo1(strWaiting + count.intValue() + strUnit);
            job.setTotalCount(count.toString());
        }
        return job;
    }
    
    /**
     * <li>说明：查询质量检查处理待办事项
     * <li>创建人：何涛
     * <li>创建日期：2015-7-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param emp 当前系统登录人员
     * @return TodoJobForPad pad移动终端待办事项实体类
     * @throws Exception 
     */
    private TodoJobForPad getJobForZljc(OmEmployee emp) throws Exception {
        QCResultQueryManager manager = (QCResultQueryManager) Application.getSpringApplicationContext().getBean("qCResultQueryManager");
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_ZLJC_NAME);
        job.setMid(FUNC_ZLJC_MID);
        job.setIcon(FUNC_ZLJC_ICON);
        
        AcOperator ac = SystemContext.getAcOperator();
        String uName = ac.getOperatorname();
        Page page = manager.getQCPageList(uName, start, limit, JCQCItemDefine.CONST_INT_CHECK_WAY_BJ + "", "");
        int bjCount = page.getTotal();
        if (bjCount > 0) {
            job.setInfo1("必检" + bjCount + strUnit);
        }
        page = manager.getQCPageList(uName, start, limit, JCQCItemDefine.CONST_INT_CHECK_WAY_CJ + "", "");
        int cjCount = page.getTotal();
        if (cjCount > 0) {
            job.setInfo2("抽检" + cjCount + strUnit);
        }
        if (bjCount + cjCount > 0) {
            job.setTotalCount(String.valueOf(bjCount + cjCount));
        }
        return job;
    }
    
    /**
     * <li>说明：查询检查提票待办事项
     * <li>创建人：何涛
     * <li>创建日期：2015-7-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     * @throws Exception
     */
    private TodoJobForPad getJobForJctp() throws Exception{
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_JCTP_NAME);
        job.setMid(FUNC_JCTP_MID);
        job.setIcon(FUNC_JCTP_ICON);
        return job;
    }
    
    /**
     * <li>说明：查询提票调度派工待办事项
     * <li>创建人：何涛
     * <li>创建日期：2015-7-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     * @throws Exception
     */
    private TodoJobForPad getJobForTpddpg() throws Exception{
        FaultTicketManager faultTicketManager = (FaultTicketManager) Application.getSpringApplicationContext().getBean("faultTicketManager");
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_TPDDPG_NAME);
        job.setMid(FUNC_TPDDPG_MID);
        job.setIcon(FUNC_TPDDPG_ICON);
        
        int count = faultTicketManager.getCountForDdpg();
        if (count > 0) {
            job.setTotalCount(count + "");
            job.setInfo1(strWaiting + count + strUnit);
        }
        return job;
    }
    
    /**
     * <li>说明：查询工长调度派工待办事项
     * <li>创建人：何涛
     * <li>创建日期：2015-7-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     * @throws Exception
     */
    private TodoJobForPad getJobForTpgzpg() throws Exception{
        FaultTicketManager faultTicketManager = (FaultTicketManager) Application.getSpringApplicationContext().getBean("faultTicketManager");
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_TPGZPG_NAME);
        job.setMid(FUNC_TPGZPG_MID);
        job.setIcon(FUNC_TPGZPG_ICON);
        
        int count = faultTicketManager.getCountForGzpg();
        if (count > 0) {
            job.setTotalCount(count + "");
            job.setInfo1(strWaiting + count + strUnit);
        }
        return job;
    }
    
    /**
     * <li>说明：查询检查提票处理待办事项
     * <li>创建人：何涛
     * <li>创建日期：2015-7-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     * @throws Exception
     */
    private TodoJobForPad getJobForJctpcl() throws Exception{
        FaultTicketManager faultTicketManager = (FaultTicketManager) Application.getSpringApplicationContext().getBean("faultTicketManager");
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_JCTPCL_NAME);
        job.setMid(FUNC_JCTPCL_MID);
        job.setIcon(FUNC_JCTPCL_ICON);
        
        int count = faultTicketManager.getCountForJctpcl();
        if (count > 0) {
            job.setTotalCount(count + "");
            job.setInfo1(strWaiting + count + strUnit);
        }
        return job;
    }
    
    /**
     * <li>说明：查询检查提票质量检验待办事项
     * <li>创建人：何涛
     * <li>创建日期：2015-7-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     * @throws Exception
     */
    private TodoJobForPad getJobForJctpzj() throws Exception{
        FaultQCResultQueryManager faultQCResultQueryManager = (FaultQCResultQueryManager) Application.getSpringApplicationContext().getBean("faultQCResultQueryManager");
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_JCTPZJ_NAME);
        job.setMid(FUNC_JCTPZJ_MID);
        job.setIcon(FUNC_JCTPZJ_ICON);
        
        Integer countForBj = faultQCResultQueryManager.getQCPageList(SystemContext.getOmEmployee().getEmpid(), 0, 1, "2", null).getTotal();
        Integer countForCj = faultQCResultQueryManager.getQCPageList(SystemContext.getOmEmployee().getEmpid(), 0, 1, "1", null).getTotal();
        int totalCount = 0;
        if (null != countForBj && countForBj > 0) {
            job.setInfo1("必检项" + countForBj + strUnit);
            totalCount += countForBj;
        }
        if (null != countForCj && countForCj > 0) {
            job.setInfo2("抽检项" + countForCj + strUnit);
            totalCount += countForCj;
        }
        job.setTotalCount(totalCount > 0 ? totalCount + "" : null);
        return job;
    }
    
    /**
     * <li>说明：查询配件检修待办事项
     * <li>创建人：何涛
     * <li>创建日期：2015-10-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workStationIDX 配件检修工位主键
     * @return TodoJobForPad pad移动终端待办事项实体类
     */
    private TodoJobForPad getJobForPjjxcl(String workStationIDX) {
        PartsRdpNodeQueryManager partsRdpNodeQueryManager = (PartsRdpNodeQueryManager) Application.getSpringApplicationContext().getBean("partsRdpNodeQueryManager");
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_PJJXCL_NAME);
        job.setMid(FUNC_PJJXCL_MID);
        job.setIcon(FUNC_PJJXCL_ICON);
        PartsRdpNodeBean bean = new PartsRdpNodeBean();
        bean.setWorkStationIDX(workStationIDX);
        bean.setStatus("1");         // 未处理
        SearchEntity<PartsRdpNodeBean> se = new SearchEntity<PartsRdpNodeBean>(bean, 1, 1, null);
        int count = partsRdpNodeQueryManager.queryNodeCountByUser(se);
        if (count > 0) {
            job.setInfo1("待处理" + count + this.strUnit);
            job.setTotalCount(count + "");
        }
//        bean.setStatus("2");
//        count = partsRdpNodeQueryManager.queryNodeCountByUser(se);
//        if (count > 0) {
//            job.setInfo2("已处理" + count + this.strUnit);
//        }
        return job;
    }
    
    /**
     * <li>说明：查询配件检修待办事项
     * <li>创建人：何涛
     * <li>创建日期：2015-10-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     */
    private TodoJobForPad getJobForPjjxzj() {
        PartsRdpManager partsRdpManager = (PartsRdpManager) Application.getSpringApplicationContext().getBean("partsRdpManager");
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_PJJXZJ_NAME);
        job.setMid(FUNC_PJJXZJ_MID);
        job.setIcon(FUNC_PJJXZJ_ICON);
        int countForBj = partsRdpManager.getTotalCount(QCItem.CONST_INT_CHECK_WAY_BJ);
        int countForCj = partsRdpManager.getTotalCount(QCItem.CONST_INT_CHECK_WAY_CJ);
        if (countForBj > 0) {
            job.setInfo1("必检项" + countForBj + strUnit);
        }
        if (countForCj > 0) {
            job.setInfo2("抽检项" + countForCj + strUnit);
        }
        if (countForBj + countForCj > 0) {
            job.setTotalCount(countForBj + countForCj + "");
        }
        return job;
    }
    
    /**
     * <li>说明：查询配件检修验收事项
     * <li>创建人：何涛
     * <li>创建日期：2015-10-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     */
    private TodoJobForPad getJobForPjjxys() {
        PartsRdpManager partsRdpManager = (PartsRdpManager) Application.getSpringApplicationContext().getBean("partsRdpManager");
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_PJJXYS_NAME);
        job.setMid(FUNC_PJJXYS_MID);
        job.setIcon(FUNC_PJJXYS_ICON);
        String hql = "From PartsRdp Where recordStatus = 0 And status = ?";
        int totalCount = partsRdpManager.getDaoUtils().getCount(hql, new Object[]{ PartsRdp.STATUS_DYS });
        if (0 < totalCount) {
            job.setTotalCount(totalCount + "");
            job.setInfo1("待验收" + totalCount + this.strUnit);
        }
        return job;
    }
    
    /**
     * <li>说明：查询配件检修进度事项
     * <li>创建人：何涛
     * <li>创建日期：2015-10-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     */
    private TodoJobForPad getJobForPjjxjd() {
        PartsRdpManager partsRdpManager = (PartsRdpManager) Application.getSpringApplicationContext().getBean("partsRdpManager");
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_PJJXJD_NAME);
        job.setMid(FUNC_PJJXJD_MID);
        job.setIcon(FUNC_PJJXJD_ICON);
        String hql = "From PartsRdp Where recordStatus = 0 And status IN (?, ?, ?)";
        int totalCount = partsRdpManager.getDaoUtils().getCount(hql, new Object[]{ PartsRdp.STATUS_WQD, PartsRdp.STATUS_JXZ, PartsRdp.STATUS_DYS });
        if (0 < totalCount) {
            job.setTotalCount(totalCount + "");
            job.setInfo1("正在处理" + totalCount + this.strUnit);
        }
        return job;
    }
    
    /**
     * <li>说明：查询下车配件登记事项
     * <li>创建人：何涛
     * <li>创建日期：2015-10-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     */
    private TodoJobForPad getJobForXcpjdj() {
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_XCPJDJ_NAME);
        job.setMid(FUNC_XCPJDJ_MID);
        job.setIcon(FUNC_XCPJDJ_ICON);
        return job;
    }
    
    /**
     * <li>说明：配件检修质检新
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return
     */
    private TodoJobForPad getJobForPjjxzjn() {
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_PJJXZJN_NAME);
        job.setMid(FUNC_PJJXZJN_MID);
        job.setIcon(FUNC_PJJXZJN_ICON);
        // 查询代办总数
        PartsRdp entity = new PartsRdp();
        SearchEntity<PartsRdp> searchEntity = new SearchEntity<PartsRdp>(entity, start, limit, null);
        PartsRdpQRQueryManager partsRdpQRQueryManager = (PartsRdpQRQueryManager) Application.getSpringApplicationContext().getBean("partsRdpQRQueryManager");
        Page<PartsRdpZljyBean> page = partsRdpQRQueryManager.findRdpPageList(searchEntity);
        int totalCount = page.getTotal();
        if (0 < totalCount) {
            job.setTotalCount(totalCount + "");
            job.setInfo1("待质检" + totalCount + this.strUnit);
        }
        return job;
    }
    
    
    /**
     * <li>说明：机车检修质检新
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return
     */
    private TodoJobForPad getJobForJczljc(OmEmployee emp) {
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_JCZLJC_NAME);
        job.setMid(FUNC_JCZLJC_MID);
        job.setIcon(FUNC_JCZLJC_ICON);
        // 查询代办总数
        TrainWorkPlanBean entity = new TrainWorkPlanBean();
        SearchEntity<TrainWorkPlanBean> searchEntity = new SearchEntity<TrainWorkPlanBean>(entity, start, limit, null);
        QCResultQueryManager qCResultQueryManager = (QCResultQueryManager) Application.getSpringApplicationContext().getBean("qCResultQueryManager");
        Page<TrainWorkPlanBean> page = qCResultQueryManager.getRdpQCList(emp.getOperatorid(), searchEntity);
        int totalCount = page.getTotal();
        if (0 < totalCount) {
            job.setTotalCount(totalCount + "");
            job.setInfo1("待质检" + totalCount + this.strUnit);
        }
        return job;
    }
    
    
    /**
     * <li>说明：查询上车配件登记事项
     * <li>创建人：何涛
     * <li>创建日期：2015-11-09
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     */
    private TodoJobForPad getJobForScpjdj() {
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_SCPJDJ_NAME);
        job.setMid(FUNC_SCPJDJ_MID);
        job.setIcon(FUNC_SCPJDJ_ICON);
        return job;
    }
    
    /**
     * <li>说明：查询良好配件登记事项
     * <li>创建人：何涛
     * <li>创建日期：2015-11-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     */
    private TodoJobForPad getJobForLhpjdj() {
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_LHPJDJ_NAME);
        job.setMid(FUNC_LHPJDJ_MID);
        job.setIcon(FUNC_LHPJDJ_ICON);
        return job;
    }
	
    /**
     * <li>说明：查询配件委外登记事项
     * <li>创建人：何涛
     * <li>创建日期：2015-11-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     */
    private TodoJobForPad getJobForPjwwdj() {
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_PJWWDJ_NAME);
        job.setMid(FUNC_PJWWDJ_MID);
        job.setIcon(FUNC_PJWWDJ_ICON);
        return job;
    }
    
    /**
     * <li>说明：查询修竣配件入库事项
     * <li>创建人：何涛
     * <li>创建日期：2015-11-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     */
    private TodoJobForPad getJobForXjpjrk() {
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_XJPJRK_NAME);
        job.setMid(FUNC_XJPJRK_MID);
        job.setIcon(FUNC_XJPJRK_ICON);
        return job;
    }
    
    /**
     * <li>说明：查询配件出库事项
     * <li>创建人：何涛
     * <li>创建日期：2015-11-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     */
    private TodoJobForPad getJobForPjckdj() {
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_PJCKDJ_NAME);
        job.setMid(FUNC_PJCKDJ_MID);
        job.setIcon(FUNC_PJCKDJ_ICON);
        return job;
    }
    
    /**
     * <li>说明：查询配件报废登记事项
     * <li>创建人：何涛
     * <li>创建日期：2015-11-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     */
    private TodoJobForPad getJobForPjbfdj() {
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_PJBFDJ_NAME);
        job.setMid(FUNC_PJBFDJ_MID);
        job.setIcon(FUNC_PJBFDJ_ICON);
        return job;
    }
    
    /**
     * <li>说明：查询配件调出登记事项
     * <li>创建人：何涛
     * <li>创建日期：2015-11-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     */
    private TodoJobForPad getJobForPjdcdj() {
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_PJDCDJ_NAME);
        job.setMid(FUNC_PJDCDJ_MID);
        job.setIcon(FUNC_PJDCDJ_ICON);
        return job;
    }
    
    /**
     * <li>说明：查询配件调出登记事项
     * <li>创建人：何涛
     * <li>创建日期：2015-11-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     */
    private TodoJobForPad getJobForPjxxcx() {
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_PJXXCX_NAME);
        job.setMid(FUNC_PJXXCX_MID);
        job.setIcon(FUNC_PJXXCX_ICON);
        return job;
    }
    
    /**
     * <li>说明：查询配件识别码绑定事项
     * <li>创建人：何涛
     * <li>创建日期：2015-11-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     */
    private TodoJobForPad getJobForPjsbm() {
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_PJSBM_NAME);
        job.setMid(FUNC_PJSBM_MID);
        job.setIcon(FUNC_PJSBM_ICON);
        return job;
    }
    
    /**
     * <li>说明：查询配件退库登记事项
     * <li>创建人：何涛
     * <li>创建日期：2015-11-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     */
    private TodoJobForPad getJobForPjtkdj() {
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_PJTKDJ_NAME);
        job.setMid(FUNC_PJTKDJ_MID);
        job.setIcon(FUNC_PJTKDJ_ICON);
        return job;
    }
    
    /**
     * <li>说明：查询配件退库登记事项
     * <li>创建人：何涛
     * <li>创建日期：2015-11-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     */
    private TodoJobForPad getJobForPjxzdj() {
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_PJXZDJ_NAME);
        job.setMid(FUNC_PJXZDJ_MID);
        job.setIcon(FUNC_PJXZDJ_ICON);
        return job;
    }
    
    /**
     * <li>说明：查询配件安装登记事项
     * <li>创建人：何涛
     * <li>创建日期：2016-01-09
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     */
    private TodoJobForPad getJobForPjazdj() {
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_PJAZDJ_NAME);
        job.setMid(FUNC_PJAZDJ_MID);
        job.setIcon(FUNC_PJAZDJ_ICON);
        return job;
    }
    
    /**
     * <li>说明：查询配件拆卸登记事项
     * <li>创建人：何涛
     * <li>创建日期：2016-01-09
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     */
    private TodoJobForPad getJobForPjcxdj() {
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_PJCXDJ_NAME);
        job.setMid(FUNC_PJCXDJ_MID);
        job.setIcon(FUNC_PJCXDJ_ICON);
        return job;
    }
    
    /**
     * <li>说明：查询配件检修记录单识别码绑定事项
     * <li>创建人：何涛
     * <li>创建日期：2016-01-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     */
    private TodoJobForPad getJobForJxsbmbd() {
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_JXSBM_NAME);
        job.setMid(FUNC_JXSBM_MID);
        job.setIcon(FUNC_JXSBM_ICON);
        return job;
    }
    
    /**
     * <li>说明：查询配件委外回段事项
     * <li>创建人：何涛
     * <li>创建日期：2016-05-05
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return TodoJobForPad pad移动终端待办事项实体类
     */
    private TodoJobForPad getJobForPjwwhd() {
        TodoJobForPad job = new TodoJobForPad();
        job.setName(FUNC_PJWWHD_NAME);
        job.setMid(FUNC_PJWWHD_MID);
        job.setIcon(FUNC_PJWWHD_ICON);
        return job;
    }
    
}
