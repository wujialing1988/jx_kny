package com.yunda.jx.pjjx.repairline.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.jx.pjjx.repairline.entity.WorkStationBinding;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkStationBinding业务类,配件检修人员绑定工位
 * <li>创建人：程梅
 * <li>创建日期：2015-10-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="workStationBindingManager")
public class WorkStationBindingManager extends JXBaseManager<WorkStationBinding, WorkStationBinding>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：查询配件检修人员绑定工位列表
     * <li>创建人：程梅
     * <li>创建日期：2015-10-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return Page<WorkStationBinding> 检修人员绑定工位分页列表对象
     */
    public Page<WorkStationBinding> findPageList(SearchEntity<WorkStationBinding> searchEntity) {
            StringBuilder sb = new StringBuilder();
            sb.append("Select new WorkStationBinding(a.idx, b.workStationCode, b.workStationName, b.repairLineName) From WorkStationBinding a, PartsWorkStation b Where b.recordStatus = ").append(Constants.NO_DELETE).append(" And a.workStationIdx = b.idx");
            WorkStationBinding entity = searchEntity.getEntity();
            //查询条件 - 人员主键
            if (null != entity.getEmpId()) {
                sb.append(" And a.empId = '").append(entity.getEmpId()).append("'");
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
     * <li>说明：保存前验证唯一性
     * <li>创建人：程梅
     * <li>创建日期：2015-10-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param binding 需验证的人员绑定工位信息
     * @return String[] 错误提示
     */
    public String[] validateSave(WorkStationBinding binding) {
        List<String> errMsg = new ArrayList<String>();
        if(null != binding){
            String hql = "Select count(*) From WorkStationBinding where workStationIdx='"+binding.getWorkStationIdx()+"' and empId='"+binding.getEmpId()+"'";
            int count = this.daoUtils.getCount(hql);
            if(count > 0){
                errMsg.add("数据重复，请刷新后再试！");
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
     * <li>说明：保存人员绑定工位信息
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param binding 人员绑定工位信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveStationBinding(WorkStationBinding binding) throws BusinessException, NoSuchFieldException {
        if(null != binding){
            this.daoUtils.getHibernateTemplate().save(binding);
        }
    }
    
    /**
     * <li>说明：解除人员绑定工位信息（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-10-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param wsb 人员绑定工位信息
     */
    public void saveStationUnbinding(WorkStationBinding wsb) {
        String hql = "From WorkStationBinding Where workStationIdx = ? And empId = ?";
        Long empId = wsb.getEmpId();
        if (null == empId) {
            empId = SystemContext.getOmEmployee().getEmpid();
        }
        Object obj = this.daoUtils.findSingle(hql, new Object[]{wsb.getWorkStationIdx(), empId});
        if (null == obj) {
            return;
        }
        this.daoUtils.getHibernateTemplate().delete(obj);
    }
    
}