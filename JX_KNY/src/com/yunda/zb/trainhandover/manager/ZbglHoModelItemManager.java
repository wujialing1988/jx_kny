package com.yunda.zb.trainhandover.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.zb.trainhandover.entity.ZbglHoModelItem;
import com.yunda.zb.trainhandover.entity.ZbglHoModelItemResult;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：ZbglHoModelItem业务类,机车交接项模板—交接项
 * <li>创建人：程梅
 * <li>创建日期：2015-02-07
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "zbglHoModelItemManager")
public class ZbglHoModelItemManager extends JXBaseManager<ZbglHoModelItem, ZbglHoModelItem> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 机车交接情况模板业务类 */
    @Resource
    private ZbglHoModelItemResultManager zbglHoModelItemResultManager;
    
    /**
     * <li>说明：交接项树
     * <li>创建人：程梅
     * <li>创建日期：2015-2-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 父节点id
     * @return 交接项list
     * @throws Exception
     */
    public List<HashMap<String, Object>> getTree(String parentIDX) throws Exception {
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        List<ZbglHoModelItem> childList = getChildList(parentIDX);
        if (childList == null || childList.size() < 1)
            return children;
        for (ZbglHoModelItem model : childList) {
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            nodeMap.put("id", model.getIdx());
            nodeMap.put("text", model.getHandOverItemName());
            nodeMap.put("handOverItemStatus", model.getHandOverItemStatus());
            nodeMap.put("parentIDX", model.getParentIDX());
            nodeMap.put("leaf", !hasChild(model.getIdx()));// 是否为叶子节点
            children.add(nodeMap);
        }
        return children;
    }
    
    /**
     * <li>说明：获取子节点列表
     * <li>创建人：程梅
     * <li>创建日期：2015-2-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 父节点id
     * @return 子节点列表
     */
    @SuppressWarnings("unchecked")
    private List<ZbglHoModelItem> getChildList(String parentIDX) {
        String hql = "from ZbglHoModelItem where recordStatus = 0 and parentIDX = '" + parentIDX + "' order by seqNo ";
        return daoUtils.find(hql);
    }
    
    /**
     * <li>说明：是否为父节点
     * <li>创建人：程梅
     * <li>创建日期：2015-2-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 父节点id
     * @return true or false
     */
    private boolean hasChild(String parentIDX) {
        List<ZbglHoModelItem> childList = getChildList(parentIDX);
        if (childList != null && childList.size() > 0)
            return true;
        return false;
    }
    
    /**
     * <li>说明：逻辑删除机车交接项模板及其所有子节点及其关联的机车交接情况模板
     * <li>创建人：程梅
     * <li>创建日期：2015-2-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 交接项id
     * @throws NoSuchFieldException
     */
    public void deleteModel(Serializable... ids) throws NoSuchFieldException {
        for (Serializable id : ids) {
            logicDeleteAllChildForModel(String.valueOf(id));
            zbglHoModelItemResultManager.logicDeleteAllResultForModel(String.valueOf(id));
        }
    }
    
    /**
     * <li>说明：逻辑删除机车交接项及其所有子节点
     * <li>创建人：程梅
     * <li>创建日期：2015-2-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param handOverItemIDX 交接项id
     */
    private void logicDeleteAllChildForModel(String handOverItemIDX) {
        String sql =
            SqlMapUtil.getSql("zb-trainHandOver:logicDeleteAllChildForModel").replace("#updator#",
                String.valueOf(SystemContext.getOmEmployee().getEmpid()));
        sql += "(" + SqlMapUtil.getSql("zb-trainHandOver:findAllChildIDXForModel").replace("#modelIDX#", handOverItemIDX) + ")";
        daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：新增前验证
     * <li>创建人：程梅
     * <li>创建日期：2015-2-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param model 实体对象
     * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
     * @throws BusinessException
     */
    public String[] validateUpdate(ZbglHoModelItem model) {
        List<ZbglHoModelItemResult> resultList = zbglHoModelItemResultManager.getListByModel(model.getParentIDX());
        if (resultList != null && resultList.size() > 0) {
            String[] errMsg = new String[1];
            errMsg[0] = "父交接项已关联有机车交接情况，不能再添加子交接项！";
            return errMsg;
        }
        return null;
    }
    
    /**
     * <li>说明：查询交接项列表
     * <li>创建人：程梅
     * <li>创建日期：2015-2-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 交接项列表分页对象
     */
    public Page<ZbglHoModelItem> getHoModelItemList() {
        String selectSql = SqlMapUtil.getSql("zb-trainHandOver:hoModelItemList-select");
        String fromSql = SqlMapUtil.getSql("zb-trainHandOver:hoModelItemList-from");
        String querySql = selectSql + " " + fromSql;
        String totalSql = " select count(1) " + fromSql;
        Page<ZbglHoModelItem> pageList = findPageList(totalSql, querySql, 0, 10000, null, null);
        List<ZbglHoModelItem> list = new ArrayList<ZbglHoModelItem>();
        if (pageList.getList() != null && pageList.getList().size() > 0) {
            for (ZbglHoModelItem model : pageList.getList()) {
                model.setHandOverResultDesc("");
                list.add(model);
            }
        }
        return new Page<ZbglHoModelItem>(list.size(), list);
    }

    /**
     * <li>说明：通过父节点名称获取其下列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param parentName
     * @return
     */
    public List<ZbglHoModelItem> findItemList(String parentName) {
        StringBuffer hql = new StringBuffer(" from ZbglHoModelItem where recordStatus = 0 and handOverItemName = ? order by seqNo");
        ZbglHoModelItem item = (ZbglHoModelItem)this.daoUtils.findSingle(hql.toString(), new Object[]{parentName});
        if(item != null){
            return this.getChildList(item.getIdx());
        }
        return null;
    }
}
