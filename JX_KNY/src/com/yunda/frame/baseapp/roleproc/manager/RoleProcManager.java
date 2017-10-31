package com.yunda.frame.baseapp.roleproc.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcFunction;
import com.yunda.frame.yhgl.manager.AcRoleManager;
import com.yunda.frame.yhgl.manager.SysFunctionManager;
import com.yunda.webservice.employee.entity.AcFunctionBean;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：pad、pda移动终端权限角色业务类
 * <li>创建人：程锐
 * <li>创建日期：2014-12-10
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "roleProcManager")
public class RoleProcManager extends JXBaseManager<Object, Object> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 系统角色服务类 */
    protected AcRoleManager getAcRoleManager() {
        return (AcRoleManager) Application.getSpringApplicationContext().getBean("acRoleManager");
    }
    
    /** 系统应用功能管理-应用功能业务类 */
    protected SysFunctionManager getSysFunctionManager() {
        return (SysFunctionManager) Application.getSpringApplicationContext().getBean("sysFunctionManager");
    }
    
    /**
     * <li>说明：获取移动客户端(PADClient)下此登录人的所有权限列表
     * <li>创建人：程锐
     * <li>创建日期：2014-12-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 移动客户端下此登录人的所有权限列表 标识 数据类型 说明 funccode String 功能编号 funcname String 功能名称 funcaction String 功能描述
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<AcFunctionBean> queryRoleFuncList() throws Exception {
        List<Object> roleIdList = getAcRoleManager().findRoleIdByOperatorId(SystemContext.getAcOperator().getOperatorid());
        List<AcFunction> functionList = getSysFunctionManager().findByOperatorIdAndAppCode(roleIdList, "PADClient");
        
//        /** **************** 新版本2015-07-09 **************** */
//        String[] sortedFuncCodes = {
//            ITodoJobForPad.FUNC_LSXTP_MID,      // 临碎修提票
//            ITodoJobForPad.FUNC_LXHCL_MID,      // 临修活处理
//            ITodoJobForPad.FUNC_SXHCL_MID,      // 碎修活处理
//            ITodoJobForPad.FUNC_LSXJY_MID,      // 临碎修质检处理
//            ITodoJobForPad.FUNC_ZBZYGD_MID,     // 整备作业工单处理
//            ITodoJobForPad.FUNC_JXZYGD_MID,     // 机车检修作业工单处理
//            ITodoJobForPad.FUNC_ZLJC_MID,       // 质量检查处理
//            ITodoJobForPad.FUNC_JCTP_MID,       // 检查提票
//            ITodoJobForPad.FUNC_TPDDPG_MID,     // 提票调度派工
//            ITodoJobForPad.FUNC_TPGZPG_MID,     // 提票工长派工
//            ITodoJobForPad.FUNC_JCTPCL_MID,     // 检查提票处理
//            ITodoJobForPad.FUNC_JCTPZJ_MID,     // 检查提票处理
//            
//            /** 配件检修 */
//            ITodoJobForPad.FUNC_PJJXCL_MID,     // 配件检修任务处理
//            ITodoJobForPad.FUNC_PJJXZJ_MID,     // 配件检修检验
//            ITodoJobForPad.FUNC_PJJXYS_MID,     // 配件检修验收
//            ITodoJobForPad.FUNC_PJJXJD_MID,     // 配件检修进度
//            
//            /** 配件周转 */
//            ITodoJobForPad.FUNC_XCPJDJ_MID,     // 下车配件登记
//            ITodoJobForPad.FUNC_SCPJDJ_MID,     // 上车配件登记
//            ITodoJobForPad.FUNC_LHPJDJ_MID,     // 良好配件登记
//            ITodoJobForPad.FUNC_PJWWDJ_MID,     // 配件委外登记
//            ITodoJobForPad.FUNC_XJPJRK_MID,     // 修竣配件入库
//            ITodoJobForPad.FUNC_PJCKDJ_MID,     // 配件出库登记
//            ITodoJobForPad.FUNC_PJBFDJ_MID,     // 配件报废登记
//            ITodoJobForPad.FUNC_PJDCDJ_MID,     // 配件调出登记
//            ITodoJobForPad.FUNC_PJXXCX_MID,     // 配件信息查询
//            ITodoJobForPad.FUNC_PJSBM_MID       // 配件识别码绑定
//        };// 按此数组的模块顺序排序显示，如后期模块显示做成配置可修改此处
        
        return sortedAcFunctionList(functionList);
    }
    
    /**
     * <li>说明：获取移动客户端(PDAClient)下此登录人的所有权限列表
     * <li>@param clientFlag 移动端标志（检修终端、整备终端）
     * <li>创建人：程锐
     * <li>创建日期：2015-11-6
     * <li>修改人：刘晓斌
     * <li>修改日期：2015-11-25
     * <li>修改内容：增加PDA菜单排序功能
     * @return 移动客户端下此登录人的所有权限列表 标识 数据类型 说明 funccode String 功能编号 funcname String 功能名称 funcaction String 功能描述
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<AcFunctionBean> queryPDARoleFuncList(String clientFlag) throws Exception {
        List<Object> roleIdList = getAcRoleManager().findRoleIdByOperatorId(SystemContext.getAcOperator().getOperatorid());
        List<AcFunction> functionList = getSysFunctionManager().findByOperatorIdAndAppCode(roleIdList, 
        		StringUtils.isNotBlank(clientFlag) ? clientFlag : "PDAClient");
        if(null == functionList)	return null;
        Collections.sort(functionList, new Comparator<AcFunction>(){
			public int compare(AcFunction o1, AcFunction o2) {
				if(null == o1 || StringUtil.isNullOrBlank(o1.getFuncaction()))	return 1;
				if(null == o2 || StringUtil.isNullOrBlank(o2.getFuncaction()))	return -1;
				int order1 = 0;
				int order2 = 0;
				try {
					order1 = Integer.parseInt(o1.getFuncaction());
				} catch (NumberFormatException e) {
					return 1;
				}
				try {
					order2 = Integer.parseInt(o2.getFuncaction());
				} catch (NumberFormatException e) {
					return -1;
				}
				if(order1 < order2)	return -1;
				if(order1 > order2)	return 1;
				return 0;
			}
        });
        
        int size = functionList.size();
        List<AcFunctionBean> sortedList = new ArrayList<AcFunctionBean>(size);
        for (int i = 0; i < size; i++) {
        	AcFunction fun = functionList.get(i);
        	sortedList.add(new AcFunctionBean(fun.getFunccode(), fun.getFuncname(), fun.getFuncaction()));
		}
        return sortedList;
        
//        String[] sortedFuncCodes = { 
//            ITodoJobForPda.FUNC_LHPJ, 
//            ITodoJobForPda.FUNC_HGYS, 
//            ITodoJobForPda.FUNC_PJBF, 
//            ITodoJobForPda.FUNC_PJCK, 
//            ITodoJobForPda.FUNC_PJDC,
//            ITodoJobForPda.FUNC_PJSBM, 
//            ITodoJobForPda.FUNC_PJSC, 
//            ITodoJobForPda.FUNC_PJWY, 
//            ITodoJobForPda.FUNC_PJXXCX, 
//            ITodoJobForPda.FUNC_XCPJ,
//            ITodoJobForPda.FUNC_XJPJRK, 
//            ITodoJobForPda.FUNC_ZLJY
//        };// 按此数组的模块顺序排序显示，如后期模块显示做成配置可修改此处
//        
//        return sortedAcFunctionList(sortedFuncCodes, functionList);
    }

    /**
     * <li>说明：按给定数组的模块顺序排序显示，如后期模块显示做成配置可修改此处
     * <li>创建人：何涛
     * <li>创建日期：2015-11-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param sortedFuncCodes 排序的应用功能模块数组，可以为空
     * @param functionList 功能权限列表
     * @return 排序后的功能权限列表
     */
    @Deprecated
    private List<AcFunctionBean> sortedAcFunctionList(String[] sortedFuncCodes, List<AcFunction> functionList) {
        Map<String, AcFunctionBean> map = new HashMap<String, AcFunctionBean>();
        for (AcFunction acFunc : functionList) {
            map.put(acFunc.getFunccode(), new AcFunctionBean(acFunc.getFunccode(), acFunc.getFuncname(), acFunc.getFuncaction()));
        }
        List<AcFunctionBean> list = new ArrayList<AcFunctionBean>();
        // 如果排序信息为空，则返回查询的自然顺序
        if (null == sortedFuncCodes || 0 >= sortedFuncCodes.length) {
             list.addAll(map.values());
             return list;
        }
        // 按照指定的模块顺序进行排序
        for (String funcCode : sortedFuncCodes) {
            if (null == map.get(funcCode)) {
                continue;
            }
            list.add(map.get(funcCode));
        }
        return list;
    }
    
    /**
     * <li>说明：利用“功能调用入口”进行排序
     * <li>创建人：何涛
     * <li>创建日期：2015-11-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param functionList 功能权限列表
     * @return 排序后的功能权限列表
     */
    private List<AcFunctionBean> sortedAcFunctionList(List<AcFunction> functionList) {
        List<AcFunctionBean> list = new ArrayList<AcFunctionBean>();
        // 类型封装
        for (AcFunction acFunc : functionList) {
            list.add(new AcFunctionBean(acFunc.getFunccode(), acFunc.getFuncname(), acFunc.getFuncaction()));
        }
        // 排序
        Collections.sort(list);
        return list;
    }
    
}
