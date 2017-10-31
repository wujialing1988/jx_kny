package com.yunda.jx.pjjx.partsrdp.station.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.partsrdp.station.entity.WpNodeStationDef;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WpNodeStationDef业务类,关联作业工位定义
 * <li>创建人：程梅
 * <li>创建日期：2015-10-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="wpNodeStationDefManager")
public class WpNodeStationDefManager extends JXBaseManager<WpNodeStationDef, WpNodeStationDef>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：根据流程节点id查询该节点下的作业工位信息
     * <li>创建人：程梅
     * <li>创建日期：2015-10-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return Page<WpNodeStationDef> 节点下的作业工位信息分页列表对象
     */
    @SuppressWarnings("unused")
    public Page<WpNodeStationDef> findPageListForWPNode(SearchEntity<WpNodeStationDef> searchEntity) {
        StringBuilder sb = new StringBuilder();
        sb.append("Select new WpNodeStationDef(a.idx, b.workStationCode, b.workStationName, b.repairLineName) From WpNodeStationDef a, PartsWorkStation b Where a.recordStatus = ").append(Constants.NO_DELETE);
        sb.append(" And b.recordStatus = ").append(Constants.NO_DELETE).append(" And a.workStationIDX = b.idx");
        WpNodeStationDef entity = searchEntity.getEntity();
        // 查询条件 - 作业流程节点
        if (!StringUtil.isNullOrBlank(entity.getNodeIDX())) {
            sb.append(" And a.nodeIDX = '").append(entity.getNodeIDX()).append("'");
        }
        // 排序字段
        if (null != searchEntity.getOrders()) {
            for (Order order : searchEntity.getOrders()) {
                String temp = order.toString();
                if (temp.contains("workStationCode") || temp.contains("workStationName") || temp.contains("repairLineName")) {
                    sb.append(" order by ").append("b." + temp);
                } else {
                    sb.append(" order by ").append("a." + temp);
                }
            }
        }
        String hql = sb.toString();
        String totalHql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
        return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
    }
    /**
     * 
     * <li>说明：判断所选工位是否已存在
     * <li>创建人：程梅
     * <li>创建日期：2015-10-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param defs 所选工位
     * @return String[] 错误提示
     */
    public String[] validateSave(WpNodeStationDef[] defs) {
        List<String> errMsg = new ArrayList<String>();
        for(WpNodeStationDef def : defs){
            String hql = "Select count(*) From WpNodeStationDef where recordStatus=0 and workStationIDX='"+def.getWorkStationIDX()+"' and nodeIDX='"+def.getNodeIDX()+"' ";
            int count = this.daoUtils.getCount(hql);
            if(count > 0){
                errMsg.add("此工位已存在！");
            }
        }
        if (errMsg.size() > 0) {
            String[] errArray = new String[errMsg.size()];
            errMsg.toArray(errArray);
            return errArray;
        }
        return null;
    }
    /**
     * 
     * <li>说明：保存作业节点关联作业工位信息
     * <li>创建人：程梅
     * <li>创建日期：2015-10-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param defs 作业节点关联作业工位信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveStationDefs(WpNodeStationDef[] defs) throws BusinessException, NoSuchFieldException {
        if(null != defs && defs.length > 0){
            List<WpNodeStationDef> list = new ArrayList<WpNodeStationDef>();
            for(WpNodeStationDef def : defs){
                def = EntityUtil.setSysinfo(def);
                def = EntityUtil.setNoDelete(def);
                list.add(def) ;
            }
            this.daoUtils.getHibernateTemplate().saveOrUpdateAll(list);
        }
    }
}