package com.yunda.jx.jxgc.processdef.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.jx.jxgc.processdef.entity.JobNodeMatDef;
import com.yunda.jx.jxgc.processdef.entity.JobProcessNodeDef;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车检修用料业务类
 * <li>创建人：张迪
 * <li>创建日期：2016-9-2
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="jobNodeMatDefManager")
public class JobNodeMatDefManager extends JXBaseManager<JobNodeMatDef, JobNodeMatDef>{
    @Resource
    private JobProcessNodeDefManager JobProcessNodeDefManager;
    /**
     * <li>说明：检修用料保存
     * <li>创建人：张迪
     * <li>创建日期：2016-9-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param list 检修用料集合
     * @return 提示信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public String[] saveNodeMats(JobNodeMatDef[] list) throws BusinessException, NoSuchFieldException {
        List<JobNodeMatDef> entityList = new ArrayList<JobNodeMatDef>();
        if(null == list[0].getNodeIDX()){
            return new String[]{"请先选择节点树!"};
        }
        JobProcessNodeDef node =  JobProcessNodeDefManager.getModelById(list[0].getNodeIDX());
        for(JobNodeMatDef mat : list) {
            // 验证”物料编码“是否唯一
            String[] msg = this.validateUpdate(mat);
            if (null != msg) {
                return msg;
            }
            mat.setParentNodeIDX(node.getParentIDX());
            mat.setNodeName(node.getNodeName());
            mat.setProcessIDX(node.getProcessIDX());
            entityList.add(mat);
        }
        if (entityList.size() > 0) {
            this.saveOrUpdate(entityList);
        }
        return null;
    }
  
    
   
    /**
     * <li>说明：批量保存实体时的验证方法
     * <li>创建人：张迪
     * <li>创建日期：2016-9-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param list
     * @return
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    public String[] validateUpdate(JobNodeMatDef mat) {
        String hql = "From JobNodeMatDef Where recordStatus = 0 And nodeIDX = ?";
        List<JobNodeMatDef> list = this.daoUtils.find(hql, new Object[]{mat.getNodeIDX()});
        if (null == list || list.size() <= 0) {
            return null;
        }
        for (JobNodeMatDef entity : list) {
            if (entity.getIdx().equals(mat.getIdx())) {
                continue;
            }
            if (mat.getMatCode().equals(entity.getMatCode())) {
                return new String[]{"物料编码：" + mat.getMatCode() + "已经存在，不能重复添加！"};
            }           
        }
        return null;
    }
    

    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：张迪
     * <li>创建日期：2016-9-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX
     * @param workStationIdxs
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void logicDelete(String nodeIDX, String[] workStationIdxs) throws BusinessException, NoSuchFieldException {
        for (String workStationIDX : workStationIdxs) {
          
        }
    }

    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：程锐
     * <li>创建日期：2016-9-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jnmd
     * @return
     * @throws NoSuchFieldException 
     */
    public String[] Update(JobNodeMatDef[] jnmd) throws NoSuchFieldException{ 
        
        if(jnmd.length==0){
            return new String[]{"未进行数据更新"};
        }
        List<JobNodeMatDef> list = new ArrayList<JobNodeMatDef>();
        for (JobNodeMatDef t : jnmd) {
            t = EntityUtil.setSysinfo(t);
            //设置逻辑删除字段状态为未删除
            t = EntityUtil.setNoDelete(t);
            list.add(t);
        }
        this.daoUtils.getHibernateTemplate().saveOrUpdateAll(list);
        return null;
    }

}