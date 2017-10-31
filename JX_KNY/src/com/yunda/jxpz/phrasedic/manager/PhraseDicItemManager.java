package com.yunda.jxpz.phrasedic.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jxpz.phrasedic.entity.PhraseDicItem;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PhraseDicItem业务类,常用短语字典项
 * <li>创建人：程梅
 * <li>创建日期：2015-09-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="phraseDicItemManager")
public class PhraseDicItemManager extends JXBaseManager<PhraseDicItem, PhraseDicItem>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * <li>说明：常用短语字典项
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param item 实体对象
     */ 
    public void saveOrUpdate(PhraseDicItem item) throws BusinessException, NoSuchFieldException {
        String dictItemId = item.getDictItemId();
        dictItemId= StringUtil.nvlTrim(dictItemId, null);
        item.setDictItemId(dictItemId) ;
        this.daoUtils.getHibernateTemplate().saveOrUpdate(item);
    }
    /**
     * <li>说明：物理删除记录，根据制定的实体身份标示数组进行批量删除实体
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 实体类主键idx数组
     */ 
    public void delete(Serializable... ids) throws BusinessException, NoSuchFieldException {
        List<PhraseDicItem> entityList = new ArrayList<PhraseDicItem>();
        for (Serializable id : ids) {
            PhraseDicItem item = getModelById(id);
            entityList.add(item);
        }
        this.daoUtils.getHibernateTemplate().deleteAll(entityList);
    }
    /**
     * 
     * <li>说明：根据常用短语字典类型id查询字典项
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param dictTypeId 字典类型id
     * @param flag 是否收藏
     * @return List 字典项列表
     */
    @SuppressWarnings("unchecked")
    public List<PhraseDicItem> getListByDictTypeId(String dictTypeId, String flag){
        //当前登录用户基本信息
        OmEmployee emp = SystemContext.getOmEmployee();
        StringBuffer hql = null ;
        //flag为TRUE，则查询已收藏的字典项信息
        if("true".equals(flag)){
            hql = new StringBuffer("select i From PhraseDicItem i ,DataCollect c where i.dictTypeId = c.id.dataEntity and i.dictItemId = c.id.dataIdx ")
            .append(" and c.id.collectEmpId = '").append(emp.getEmpid()).append("' and c.id.dataEntity = '").append(dictTypeId).append("'");
        }else if("false".equals(flag)){
            //flag为false，则查询未被收藏的字典项信息
            hql = new StringBuffer("select i From PhraseDicItem i where i.dictTypeId = '").append(dictTypeId).
            append("' and i.dictItemId not in (select c.id.dataIdx from DataCollect c where c.id.collectEmpId = '").append(emp.getEmpid()).
            append("' and c.id.dataEntity = '").append(dictTypeId).append("')");
        }else{
            hql = new StringBuffer("select i From PhraseDicItem i where i.dictTypeId = '").append(dictTypeId).append("'") ;
        }
        hql.append(" order by i.dictItemDesc ") ; //按短语描述排序
        List<PhraseDicItem> list = daoUtils.find(hql.toString()) ;
        return list ;
    }
    
    /**
     * <li>说明：存放位置分页显示
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 分页查询参数实体
     * @param dictTypeId 字典类型id
     * @param flag 是否收藏
     * @return Page<PhraseDicItem> 存放位置分页数据
     * @throws BusinessException 业务异常
     */
    @SuppressWarnings("unchecked")
    public Page<PhraseDicItem> getPageByDictTypeId(SearchEntity<PhraseDicItem> searchEntity, String dictTypeId, String flag) throws BusinessException{
        // 查询条件实体
        PhraseDicItem entity = searchEntity.getEntity();
        // 当前登录用户基本信息
        OmEmployee emp = SystemContext.getOmEmployee();
        StringBuffer hql = null ;
        // flag为TRUE，则查询已收藏的字典项信息
        if("true".equals(flag)){
            hql = new StringBuffer("select i From PhraseDicItem i ,DataCollect c where i.dictTypeId = c.id.dataEntity and i.dictItemId = c.id.dataIdx ")
            .append(" and c.id.collectEmpId = '").append(emp.getEmpid()).append("' and c.id.dataEntity = '").append(dictTypeId).append("'");
        }else if("false".equals(flag)){
            // flag为false，则查询未被收藏的字典项信息
            hql = new StringBuffer("select i From PhraseDicItem i where i.dictTypeId = '").append(dictTypeId).
            append("' and i.dictItemId not in (select c.id.dataIdx from DataCollect c where c.id.collectEmpId = '").append(emp.getEmpid()).
            append("' and c.id.dataEntity = '").append(dictTypeId).append("')");
        }else{
            hql = new StringBuffer("select i From PhraseDicItem i where i.dictTypeId = '").append(dictTypeId).append("'") ;
        }
        
        // 模糊查询条件
        if(!StringUtil.isNullOrBlank(entity.getDictItemDesc())){
            hql.append(" and i.dictItemDesc like '%"+entity.getDictItemDesc()+"%'");
        }
        
        hql.append(" order by i.dictItemDesc ") ; // 按短语描述排序
        String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
        return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    
}