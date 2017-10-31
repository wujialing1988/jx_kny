package com.yunda.jxpz.phrasedic.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jxpz.phrasedic.entity.PhraseDicItem;
import com.yunda.jxpz.phrasedic.entity.PhraseDicType;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PhraseDicType业务类,常用短语字典类型
 * <li>创建人：程梅
 * <li>创建日期：2015-09-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="phraseDicTypeManager")
public class PhraseDicTypeManager extends JXBaseManager<PhraseDicType, PhraseDicType>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 常用短语字典项业务类
     */
    @Resource
    private PhraseDicItemManager phraseDicItemManager ;
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
        List<PhraseDicType> entityList = new ArrayList<PhraseDicType>();
        for (Serializable id : ids) {
            //查询该常用短语字典类型对应的字典项
            List<PhraseDicItem> itemList = phraseDicItemManager.getListByDictTypeId(id.toString(),null);
            if(itemList != null && itemList.size() > 0){
                //物理删除字典项
                this.daoUtils.getHibernateTemplate().deleteAll(itemList);
            }
            PhraseDicType type = getModelById(id);
            entityList.add(type);
        }
        this.daoUtils.getHibernateTemplate().deleteAll(entityList);
    }
}