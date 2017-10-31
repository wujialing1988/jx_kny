package com.yunda.jx.jxgc.repairrequirement.manager;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.repairrequirement.entity.RepairProject;
import com.yunda.jx.jxgc.repairrequirement.entity.WorkSeq;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：RepairProject业务类,检修项目
 * <li>创建人：王治龙
 * <li>创建日期：2012-12-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="repairProjectManager")
public class RepairProjectManager extends JXBaseManager<RepairProject, RepairProject>{
//    /**定义检修项目对应的工序卡业务类*/
//	@Resource
//    private RPToWSManager rPToWSManager ;
    
    /** WorkSeq业务类,工序卡 */
    @Resource
    private WorkSeqManager workSeqManager;
	/**
     * 
     * <li>说明：查询符合条件的检修项目列表信息，用于工艺节点中选择检修项目
     * <li>创建人：程梅
     * <li>创建日期：2012-12-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param processIdx:工艺流程id   
     * @return 返回值说明
     * @throws 抛出异常列表
	 */
     @SuppressWarnings("unchecked")
    public Page findListForTecProcessNodePro(String repairProjectName, String processIdx,final SearchEntity<RepairProject> searchEntity,String repairProjectType,String trainTypeIDX, String partsTypeIDX)  throws BusinessException {
            StringBuilder hql = new StringBuilder();
            hql.append("from RepairProject where recordStatus = 0 and idx not in (select projectIdx from TecProcessNodePro where recordStatus=0 ");
            hql.append(" and tecProcessNodeIdx in (select idx from TecProcessNode where recordStatus=0 and status=20 and tecProcessIdx='").append(processIdx).append("'))");
            
            //程锐修改-2013-5-6修改不以组成型号关联检修项目 而以车型主键关联检修项目
            if(!StringUtil.isNullOrBlank(trainTypeIDX)){
                hql.append(" and pTrainTypeIdx = '").append(trainTypeIDX).append("'");
            }
//          程锐修改-2013-5-6修改不以组成型号关联检修项目 而以配件主键关联检修项目
            if(!StringUtil.isNullOrBlank(partsTypeIDX)){
                hql.append(" and pPartsTypeIdx = '").append(partsTypeIDX).append("'");
            }
            
            if(!StringUtil.isNullOrBlank(repairProjectName)){
                hql.append(" and repairProjectName like '%").append(repairProjectName).append("%'");
            }
            String totalHql = "select count(*) " + hql.toString();
            return findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
        }
    /**
     * <li>说明：删除检修项目并且级联删除检修项目关联的工序卡和施修规则等
     * <li>创建人：王治龙
     * <li>创建日期：2012-12-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 实体类主键idx数组
     * @return void
     * @throws BusinessException, NoSuchFieldException
     */ 
    public void logicDeleteCascade(Serializable... ids) throws BusinessException, NoSuchFieldException {
//        List<RepairProject> entityList = new ArrayList<RepairProject>();
//        for (Serializable id : ids) {
//            RepairProject t = getModelById(id);
//            List<RPToWS> workSeqList = rPToWSManager.getModelList(null, id.toString());
//            if(workSeqList.size() > 0){
//                rPToWSManager.logicDelete(workSeqList); //删除检修项目对应的工序卡
//            }
//            t = EntityUtil.setSysinfo(t);
////          设置逻辑删除字段状态为已删除
//            t = EntityUtil.setDeleted(t);
//            entityList.add(t);
//        }
//        this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
    }
    
    /**
     * <li>说明：删除检修项目并且级联删除检修项目关联的工序卡
     * <li>创建人：林欢
     * <li>创建日期：2016-6-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws NoSuchFieldException 
     * @throws Exception
     */
    public void deleteRepairProjectAndWorkSeq(String[] ids) throws NoSuchFieldException {
        
        //循环前台传递过来的检修项目IDX数组（多条数据删除）
        for (Serializable id : ids) {
            //通过检修项目idx查询关联的工序卡(record=0)
            List<WorkSeq> workSeqList = workSeqManager.findWorkSeqByRepairProjectIDX(id);
            //循环删除工序卡
            for (WorkSeq seq : workSeqList) {
                //级联删除工序卡下面的工步，质量检查数据
                workSeqManager.deleteWorkSeq(seq.getIdx());
            }
            
            //更新检修项目record状态为1
            RepairProject repairProject = this.getModelById(id);
            repairProject = EntityUtil.setSysinfo(repairProject);
//          设置逻辑删除字段状态为已删除
            repairProject = EntityUtil.setDeleted(repairProject);
            //更新检修项目
            this.save(repairProject);
        }
    }
}