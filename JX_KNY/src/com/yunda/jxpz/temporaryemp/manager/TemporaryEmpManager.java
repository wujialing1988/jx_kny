package com.yunda.jxpz.temporaryemp.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jxpz.temporaryemp.entity.TemporaryEmp;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：temporaryEmp业务类,临时人员设置表
 * <li>创建人：程梅
 * <li>创建日期：2015-04-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="temporaryEmpManager")
public class TemporaryEmpManager extends JXBaseManager<TemporaryEmp, TemporaryEmp>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
     * 
     * <li>说明：新增验证
     * <li>创建人：程梅
     * <li>创建日期：2015-4-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param empList 需验证的临时人员信息
     * @return 提示信息
     * @throws BusinessException
	 */
    public String[] validateUpdateList(TemporaryEmp[] empList) throws BusinessException {
        List<String> errMsg = new ArrayList<String>();
        for(TemporaryEmp entity : empList){
            if( "".equals(entity.getIdx()) && entity.getEmpid() != null ){ //新增时验证
                String hql = "Select count(*) From TemporaryEmp where empId ='"+
                entity.getEmpid()+"' and temporaryTreamId='"+entity.getTemporaryTreamId()+"' and recordStatus=0";
                int count = this.daoUtils.getCount(hql);
                if(count > 0){
                    errMsg.add("人员【"+ entity.getEmpname() +"】在此机构中已存在！");
                }
            }
        }
        if (errMsg.size() > 0) {
            String[] errArray = new String[errMsg.size()];
            errMsg.toArray(errArray);
            return errArray;
        }
        return null;
    }
}