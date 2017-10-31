package com.yunda.jx.wlgl.loan.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.wlgl.loan.entity.MatLoan;
import com.yunda.jx.wlgl.loan.manager.MatLoanManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatLoan控制器, 借出归还单
 * <li>创建人：程梅
 * <li>创建日期：2016-05-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class MatLoanAction extends JXBaseAction<MatLoan, MatLoan, MatLoanManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：物料借出确认
     * <li>创建人：张迪
     * <li>创建日期：2016-5-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void matLoanOut() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            MatLoan matLoan = (MatLoan)JSONUtil.read(getRequest(), entity.getClass());
            String[] errMsg = this.manager.validateUpdate(matLoan);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.matLoanOut(matLoan);
                map.put("success", true);
            } else {
                map.put("success", false);
                map.put("errMsg", errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    /**
     * <li>说明：物料归还确认
     * <li>创建人：张迪
     * <li>创建日期：2016-5-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void matLoanBack() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            MatLoan matLoan = (MatLoan)JSONUtil.read(getRequest(), entity.getClass());
            String[] errMsg = this.manager.validateUpdate(matLoan);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.matLoanBack(matLoan);
                map.put("success", true);
            } else {
                map.put("success", false);
                map.put("errMsg", errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * <li>说明：通过物料类型，物料编码，借出单位查找对应借出信息
     * <li>创建人：张迪
     * <li>创建日期：2016-5-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getMatLoanOut() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String matCode = getRequest().getParameter("matCode");  //物料编码
            String matType = getRequest().getParameter("matType");//物料类型
            String loanOrg = getRequest().getParameter("loanOrg");//单位
            MatLoan matLoan = this.manager.getMatLoanOut(matType, matCode, loanOrg);
            if (null != matLoan) {
                map.put("matLoan", matLoan);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}