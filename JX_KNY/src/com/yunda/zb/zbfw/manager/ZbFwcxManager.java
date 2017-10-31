package com.yunda.zb.zbfw.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.zb.zbfw.entity.ZbFwcx;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbFwcx业务类,整备范围适用车型
 * <li>创建人：王利成
 * <li>创建日期：2015-01-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */ 
@Service(value="zbFwcxManager")
public class ZbFwcxManager extends JXBaseManager<ZbFwcx, ZbFwcx>{
	/**
	 * 
	 * <li>说明：整备范围车型模糊查询
	 * <li>创建人：王利成
	 * <li>创建日期：2015-1-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchEntity 机车整备单bean实体包装类
	 * @return 整备单分页列表
	 */
    public Page<ZbFwcx> getZbFwcxList(SearchEntity<ZbFwcx> searchEntity){
		 String selectHql = "select new ZbFwcx(a.idx, a.zbfwIDX, a.cXBM, a.cXPYM)";
			String fromHql = " from ZbFwcx a, ZbFw b where a.zbfwIDX = b.idx and a.recordStatus = 0 and b.recordStatus = 0";
			String totalHql = "select count(*) " + fromHql;		
			String hql = selectHql + fromHql;
			return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	 } 
    /**
	 * <li>说明：批量设置承修车型
	 * <li>创建人：王利成
	 * <li>创建日期：2015-01-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param objList 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException,NoSuchFieldException
	 */	
    public String[] saveOrUpdateList(ZbFwcx[] objList) throws BusinessException, NoSuchFieldException{
        String[] errMsg = this.validateUpdate(objList);  //验证
        List<ZbFwcx> entityList = new ArrayList<ZbFwcx>();
        if (errMsg == null || errMsg.length < 1) {
            for(ZbFwcx t : objList){ //循环新增是为了验证方便
                entityList.add(t);
            }
            this.saveOrUpdate(entityList);
        }
        return errMsg;
    }
    
    /**
	 * <li>说明：新增修改保存前的实体对象前的批量验证业务
	 * <li>创建人：王利成
	 * <li>创建日期：2015-01-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entityList 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	public String[] validateUpdate(ZbFwcx[] entityList) throws BusinessException {
        List<String> errMsg = new ArrayList<String>();
        for(ZbFwcx obj : entityList){
            List<ZbFwcx> countList = this.getModelList(obj.getCXBM(),obj.getZbfwIDX());
            if(countList.size() > 0){
                errMsg.add("【"+obj.getCXPYM()+"】车型，已经存在！");
            }
            if (errMsg.size() > 0) {
                String[] errArray = new String[errMsg.size()];
                errMsg.toArray(errArray);
                return errArray;
            }
        }
	    return null;
	}
    /**
     * <li>说明：通过车型主键
     * <li>创建人：王利成
     * <li>创建日期：2015-01-23
     * <li>修改人： 
     * <li>修改日期
     * <li>修改内容：
     * @param trainTypeIDX 车型主键
     * @param zbfwidx  车型主键
     * @return List<ZbFwcx> 返回承修车型集合
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<ZbFwcx> getModelList(String trainTypeIDX,String zbfwidx) throws BusinessException {
        StringBuffer hql = new StringBuffer();
        hql.append("From ZbFwcx t where t.recordStatus=").append(Constants.NO_DELETE);
        hql.append(" and t.cXBM = ? ");
        hql.append(" and t.zbfwIDX = ? ");
        return daoUtils.find(enableCache(), hql.toString(), new Object[]{trainTypeIDX,zbfwidx});
    }
}