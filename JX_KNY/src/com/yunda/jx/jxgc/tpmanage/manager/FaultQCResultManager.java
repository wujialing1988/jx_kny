package com.yunda.jx.jxgc.tpmanage.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.IOmEmployeeManager;
import com.yunda.frame.yhgl.manager.IOmOrganizationManager;
import com.yunda.jx.jxgc.base.tpqcitemdefine.entity.TPQCItemDefine;
import com.yunda.jx.jxgc.common.JxgcConstants;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResult;
import com.yunda.jx.jxgc.tpmanage.entity.FaultQCResult;
import com.yunda.jx.jxgc.tpmanage.entity.FaultQCResultVO;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明：FaultQCResult业务类,提票质量检查
 * <li>创建人：程锐
 * <li>创建日期：2015-06-25
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.1
 */
@Service(value = "faultQCResultManager")
public class FaultQCResultManager extends JXBaseManager<FaultQCResult, FaultQCResult> {
    
    /** 提票质量检查参与者业务类对象 */
    @Resource
    private FaultQCParticipantManager faultQCParticipantManager;
    
    /** 组织机构业务类 */
    @Resource
    private IOmOrganizationManager omOrganizationManager;
    
    /** 人员业务类 */
    @Resource
    private IOmEmployeeManager omEmployeeManager;
    
    /**
     * <li>说明：根据提票单idx获取需要指派的质量检查项列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 提票单IDX
     * @return 需要指派的质量检查项列表
     */
    @SuppressWarnings("unchecked")
    public List<FaultQCResult> getIsAssignCheckItems(String idx) {
        Map paramMap = new HashMap<String, String>();   
        paramMap.put("relationIDX", idx);
        paramMap.put("isAssign", TPQCItemDefine.CONST_INT_IS_ASSIGN_Y + "");
        paramMap.put("status", FaultQCResult.STATUS_WKF + "");
        return getTpQCList(paramMap);
    }
    
    /**
     * <li>说明：指派提票质量检查项参与者并开放质量检查项
     * <li>处理逻辑： 1 有指派：保存指派的质量检查参与者
     * <li> 2 根据作业班组和质检项配置保存质检项参与者
     * <li> 3 根据作业卡IDX开放质量检查项
     * <li>创建人：程锐
     * <li>创建日期：2015-7-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param tpIDX 提票单IDX
     * @param result 提票质量检验结果信息对象数组
     * @param empId 人员ID
     * @throws Exception
     */
    public void updateOpenQCResultByTp(String tpIDX, FaultQCResultVO[] result, Long empId) throws Exception {
        OmOrganization org = omOrganizationManager.findByEmpId(empId);
        String checkOrgID = org != null ? org.getOrgid() + "" : "";
        if (StringUtil.isNullOrBlank(checkOrgID)) throw new BusinessException("质量检查项指派参与者时无对应组织机构！");
        if ((result != null && result.length > 0)) {
        for (FaultQCResultVO resultVO : result) {            
            faultQCParticipantManager.saveIsAssignParticiant(tpIDX, 
                                                             resultVO.getQcEmpID() + "", 
                                                             omEmployeeManager.getModelById(resultVO.getQcEmpID()).getEmpname(), 
                                                             resultVO.getCheckItemCode());
        }   
        
        }
        faultQCParticipantManager.saveNotAssignParticiant(tpIDX, checkOrgID);
        updateOpenQCResultByTp(tpIDX);
    }
    
    /**
     * <li>说明：根据提票单IDX开放提票质量检查项
     * <li>创建人：程锐
     * <li>创建日期：2015-7-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param tpIDX 提票单IDX
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateOpenQCResultByTp(String tpIDX) throws Exception {        
        String sql = SqlMapUtil.getSql("jxgc-tp:updateOpenQCResultByTp")
                                .replace("#tpIDX#", tpIDX)
                                .replace("#STATUS_DCL#", FaultQCResult.STATUS_DCL + "");
        daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：完成提票质检结果
     * <li>创建人：程锐
     * <li>创建日期：2015-7-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param resultVO 提票质量检查项实体数组
     * @throws Exception
     */
    public void updateFinishQCResult(FaultQCResultVO[] resultVO) throws Exception {
        if (resultVO == null || resultVO.length < 1) 
            throw new BusinessException("未选择提票质量检验项！");
        OmEmployee emp = omEmployeeManager.findByOperator(SystemContext.getAcOperator().getOperatorid());
        Long qcEmpID = resultVO[0].getQcEmpID() == null ? emp.getEmpid() : resultVO[0].getQcEmpID();
        String qcEmpName = StringUtil.isNullOrBlank(resultVO[0].getQcEmpName()) ? emp.getEmpname() : resultVO[0].getQcEmpName();
        String remarks = StringUtil.nvlTrim(resultVO[0].getRemarks(), "");
        Date qcTime = resultVO[0].getQcTime() == null ? new Date() : resultVO[0].getQcTime();
        StringBuilder idx = new StringBuilder();
        for (FaultQCResultVO result : resultVO) {
            idx.append(result.getIdx()).append(Constants.JOINSTR);
        }
        if (idx.toString().endsWith(Constants.JOINSTR))
            idx.deleteCharAt(idx.length() - 1);
        String idxsStr = CommonUtil.buildInSqlStr(idx.toString());
        String sql = SqlMapUtil.getSql("jxgc-tp:updateFinishQCResult")
                               .replace("#STATUS_YCL#", QCResult.STATUS_YCL + "")
                               .replace("#empid#", qcEmpID + "")
                               .replace("#empname#", qcEmpName)
                               .replace("#qcTime#", DateUtil.yyyy_MM_dd_HH_mm_ss.format(qcTime))
                               .replace("#remarks#", StringUtil.nvlTrim(remarks, ""))
                               .replace(JxgcConstants.IDXS, idxsStr);
        daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：获取提票质检列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 提票质检列表
     */
    @SuppressWarnings("unchecked")
    private List<FaultQCResult> getTpQCList(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from FaultQCResult where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap));
        return daoUtils.find(hql.toString());
    }
}
