package com.yunda.freight.base.deskprivilige.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.freight.base.deskprivilige.entity.DeskPrivilige;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 桌面权限
 * <li>创建人：伍佳灵
 * <li>创建日期：2018-01-01 11:36:09
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("deskPriviligeManager")
public class DeskPriviligeManager extends JXBaseManager<DeskPrivilige, DeskPrivilige> implements IbaseCombo {
    
	/**
     * <li>说明：保存桌面权限
     * <li>创建人：伍佳灵
     * <li>创建日期：2018-01-01
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param deskPrivilige 桌面权限实体
     * @return
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
     */
	public void saveDeskPrivilige(DeskPrivilige entity) throws BusinessException, NoSuchFieldException{
		OmEmployee emp = SystemContext.getOmEmployee() ;
		if(emp != null && entity != null && !StringUtil.isNullOrBlank(entity.getDictCode())){
			DeskPrivilige privilige = getDeskPrivilige(entity.getDictCode(), emp.getEmpid()+"");
			if(privilige == null){
				privilige = new DeskPrivilige() ;
				privilige.setEmpId(emp.getEmpid()+"");
				privilige.setEmpName(emp.getEmpname());
				privilige.setDictCode(entity.getDictCode());
				privilige.setDictName(entity.getDictName());
			}
			if(privilige.isShow()){
				privilige.setShow(false);
			}else{
				privilige.setShow(true);
			}
			this.saveOrUpdate(privilige);
		}
	}
	
	/**
     * <li>说明：获取桌面权限
     * <li>创建人：伍佳灵
     * <li>创建日期：2018-01-01
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param dictCode 菜单编码
     * @param empId 人员id
     * @return
     */
	public DeskPrivilige getDeskPrivilige(String dictCode,String empId){
		StringBuffer hql = new StringBuffer(" From DeskPrivilige where dictCode = ? and empId = ? ");
		return (DeskPrivilige)this.daoUtils.findSingle(hql.toString(),new Object[]{dictCode,empId});
	}
	
	/**
     * <li>说明：获取桌面权限实体
     * <li>创建人：伍佳灵
     * <li>创建日期：2018-01-01
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return
     */
	public Map<String, Object> getDeskPriviligeObj(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("isKctxShow", false);
		OmEmployee emp = SystemContext.getOmEmployee() ;
		List<DeskPrivilige> desks = findDeskPriviligeByDictCode("", emp.getEmpid()+"");
		for (DeskPrivilige desk : desks) {
			// 扣车提醒
			if("isKctxHCShow".equals(desk.getDictCode()) && desk.isShow()){
				result.put("isKctxShow", true);
			}
			if("isKctxKCShow".equals(desk.getDictCode()) && desk.isShow()){
				result.put("isKctxShow", true);
			}
			result.put(desk.getDictCode(), desk.isShow());
		}
		return result ;
	}
	
	/**
     * <li>说明：获取桌面权限实体
     * <li>创建人：伍佳灵
     * <li>创建日期：2018-01-01
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param dictCode 菜单编码
     * @param empId 人员id
     * @return
     */
	public List<DeskPrivilige> findDeskPriviligeByDictCode(String dictCode,String empId){
		StringBuffer hql = new StringBuffer(" From DeskPrivilige where 1=1 ");
		if(!StringUtil.isNullOrBlank(dictCode)){
			hql.append(" and dictCode = '"+dictCode+"'");
		}
		
		if(!StringUtil.isNullOrBlank(empId)){
			hql.append(" and empId = '"+empId+"'");
		}
		return this.daoUtils.find(hql.toString());
	}
	
	
    /**
     * <li>说明：查询首页权限设置列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2018-01-01
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return
     */
    public List<Map<String, Object>> findDeskPriviligeList(){
    	OmEmployee emp = SystemContext.getOmEmployee() ;
    	if(emp == null){
    		return null ;
    	}
        String sql = SqlMapUtil.getSql("kny-repairwarning:findDeskPriviligeList").replaceAll("#EMP_ID#", emp.getEmpid()+"");
        return this.queryListMap(sql);
    }
   
}
