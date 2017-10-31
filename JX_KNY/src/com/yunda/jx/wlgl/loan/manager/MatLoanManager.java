package com.yunda.jx.wlgl.loan.manager;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.wlgl.inwh.entity.MatInWHNew;
import com.yunda.jx.wlgl.inwh.manager.MatInWHNewManager;
import com.yunda.jx.wlgl.loan.entity.MatLoan;
import com.yunda.jx.wlgl.outwh.entity.MatOutWH;
import com.yunda.jx.wlgl.outwh.manager.MatOutWHManager;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatLoan业务类,借出归还
 * <li>创建人：程梅
 * <li>创建日期：2016-05-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="matLoanManager")
public class MatLoanManager extends JXBaseManager<MatLoan, MatLoan>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    @Resource
    private   MatOutWHManager matOutWHManager;
    @Resource
    private   MatInWHNewManager matInWHNewManager;
    
    /**
     * <li>说明：物料借出
     * <li>创建人：张迪
     * <li>创建日期：2016-5-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param matLoan 借出实体
     * @throws IllegalAccessException
     * @throws BusinessException
     * @throws Exception
     */
    public void matLoanOut(MatLoan matLoan) throws IllegalAccessException, BusinessException, Exception{   
        //封装出库数据
        MatOutWH matOutWHNew = new MatOutWH();
        BeanUtils.copyProperties(matOutWHNew, matLoan);
        matOutWHNew.setIdx(null);
        matOutWHNew.setQty(matLoan.getLoanQty());
        matOutWHNew.setWHType(MatOutWH.TYPE_JC); 
        //查找借出信息
        MatLoan matLoanOut  = getMatLoanOut(matLoan.getMatType(),matLoan.getMatCode() ,matLoan.getLoanOrg());
        //  保存借出信息
        if(null == matLoanOut){
            this.saveOrUpdate(matLoan); 
        }else{
            matLoanOut.setLoanQty(matLoanOut.getLoanQty()+ matLoan.getLoanQty());
            this.saveOrUpdate(matLoanOut);  
        }
        //保存出库信息
        matOutWHManager.saveMatOutWH(matOutWHNew);
                 
      }
    
    /**
     * <li>说明：物料归还
     * <li>创建人：张迪
     * <li>创建日期：2016-5-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param matLoan 归还实体
     * @throws IllegalAccessException
     * @throws BusinessException
     * @throws Exception
     */
    public void matLoanBack(MatLoan matLoan) throws IllegalAccessException, BusinessException, Exception{   
        //封装入库数据
        MatInWHNew matInWHNew = new MatInWHNew();
        BeanUtils.copyProperties(matInWHNew, matLoan);
        matInWHNew.setIdx(null);
        matInWHNew.setQty(matLoan.getLoanQty());
        matInWHNew.setInWhType(MatInWHNew.TYPE_GH); 
        //保存入库信息
        matInWHNewManager.saveMatInWHNew(matInWHNew);
        //查找借出信息
        MatLoan matLoanOut  = getMatLoanOut(matLoan.getMatType(),matLoan.getMatCode() ,matLoan.getLoanOrg());
        if(null == matLoanOut){
            throw new BusinessException("物料没有借出信息，请重新选择");
        }
        if(matLoanOut.getLoanQty()- matLoan.getLoanQty() < 0){
            throw new BusinessException("归还数量超出借出数量");
        }
        matLoanOut.setLoanQty(matLoanOut.getLoanQty()- matLoan.getLoanQty());
        //保存归还信息    
        this.saveOrUpdate(matLoanOut);           
    }
    
    /**
     * <li>说明：通过物料类型，物料编码，借出单位查找对应借出信息
     * <li>创建人：张迪
     * <li>创建日期：2016-5-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param matType 物料类型
     * @param matCode 物料编码
     * @param loanOrg 借出单位
     * @return 借出信息
     */
    public MatLoan getMatLoanOut(String matType, String matCode, String loanOrg){
        String hql = "from MatLoan Where matType = ? And matCode = ? And loanOrg = ? ";
        return (MatLoan)this.daoUtils.findSingle(hql, new Object[]{ matType, matCode, loanOrg});
    }
    
}