package com.yunda.jx.jxgc.repairrequirement.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkTaskResultViewManager;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResult;
import com.yunda.jx.jxgc.repairrequirement.entity.QualityControl;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：QualityControl业务类,质量控制
 * <li>创建人：王治龙
 * <li>创建日期：2012-12-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="qualityControlManager")
public class QualityControlManager extends JXBaseManager<QualityControl, QualityControl>{
	/**
	 * 作业任务视图操作类
	 */
	@Resource
	public WorkTaskResultViewManager workTaskResultViewManager;
    /**
     * <li>说明：查询工序卡(质量记录单)或作业项（检测/检修项目）质量控制使用情况
     * <li>创建人：王治龙
     * <li>创建日期：2012-12-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workSeqIDX：工序卡(质量记录单)主键或作业项（检测/检修项目）主键
     * @return List<QualityControl> 返回工序卡(质量记录单)或作业项（检测/检修项目）质量控制集合
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<QualityControl> findWorkSeqQC(final String workSeqIDX) throws BusinessException{
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (List<QualityControl>)template.execute(new HibernateCallback(){
            public List<QualityControl> doInHibernate(Session s) {
                StringBuffer sql = new StringBuffer();
                sql.append(" select D.QC_ITEM_NO checkItemCode,");
                sql.append(" D.QC_ITEM_NAME checkItemName,");
                sql.append(" q.check_way_code checkWayCode,");
                sql.append(" q.check_way_name checkWayName,");
                sql.append(" q.relation_idx relationIDX,");
                sql.append(" q.idx");
                sql.append(" from JXGC_JCQC_ITEM_DEFINE D");
                sql.append(" left join JXGC_Quality_Control q");
                sql.append(" on D.QC_ITEM_NO = q.check_item_code ");
                sql.append(" and q.relation_idx='").append(workSeqIDX).append("'");
                sql.append(" where D.RECORD_STATUS =0");
                sql.append(" order by D.SEQ_NO asc "); //通过数据字典的排序号实现，grid列表上的字段顺序
                System.out.println(sql);
                //获取总记录数
                org.hibernate.SQLQuery query = s.createSQLQuery(sql.toString());
                query.addScalar("checkItemCode", Hibernate.STRING)
                .addScalar("checkItemName", Hibernate.STRING)
                .addScalar("checkWayCode", Hibernate.STRING)
                .addScalar("checkWayName", Hibernate.STRING)
                .addScalar("relationIDX", Hibernate.STRING)
                .addScalar("idx", Hibernate.STRING)
                .setResultTransformer(Transformers.aliasToBean(QualityControl.class));
                return (List<QualityControl>)query.list();
            }
        });
    }
    /**
     * 
     * <li>说明：查询工序卡(质量记录单)或作业项（检测/检修项目）质量控制使用情况【配件质量检查项】
     * <li>创建人：程梅
     * <li>创建日期：2013-11-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param dicttypeid：业务字典id
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    @SuppressWarnings("unchecked")
	public List<QualityControl> findWorkSeqQC(final String workSeqIDX,final String dicttypeid) throws BusinessException{
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (List<QualityControl>)template.execute(new HibernateCallback(){
            public List<QualityControl> doInHibernate(Session s) {
                StringBuffer sql = new StringBuffer();
                sql.append(" select d.dictid checkItemCode,");
                sql.append(" d.dictname checkItemName,");
                sql.append(" q.check_way_code checkWayCode,");
                sql.append(" q.check_way_name checkWayName,");
                sql.append(" q.relation_idx relationIDX,");
                sql.append(" q.idx");
                sql.append(" from eos_dict_entry d");
                sql.append(" left join JXGC_Quality_Control q");
                sql.append(" on d.dictid = q.check_item_code ");
                sql.append(" and q.relation_idx='").append(workSeqIDX).append("'");
                sql.append(" where d.dicttypeid='").append(dicttypeid).append("'");
                sql.append(" order by d.sortno asc "); //通过数据字典的排序号实现，grid列表上的字段顺序
                //获取总记录数
                org.hibernate.SQLQuery query = s.createSQLQuery(sql.toString());
                query.addScalar("checkItemCode", Hibernate.STRING)
                .addScalar("checkItemName", Hibernate.STRING)
                .addScalar("checkWayCode", Hibernate.STRING)
                .addScalar("checkWayName", Hibernate.STRING)
                .addScalar("relationIDX", Hibernate.STRING)
                .addScalar("idx", Hibernate.STRING)
                .setResultTransformer(Transformers.aliasToBean(QualityControl.class));
                return (List<QualityControl>)query.list();
            }
        });
    }
    /**
     * <li>说明：通过关联外键查询质量控制情况
     * <li>创建人：王治龙
     * <li>创建日期：2012-12-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String relationIDX：关联外键ID 
     * @return List<QualityControl> 质量控制列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<QualityControl> getModelList(String relationIDX) throws BusinessException {
        StringBuffer hql = new StringBuffer();
        hql.append("from QualityControl t where t.recordStatus=0 ");
        if(!StringUtil.isNullOrBlank(relationIDX)){
            hql.append(" and t.relationIDX = '").append(relationIDX).append("'");
        }
        return this.daoUtils.find(hql.toString());
    }
    
    /**
     * <li>说明：通过关联外键删除质量控制情况（此处采用物理删除）
     * <li>创建人：王治龙
     * <li>创建日期：2012-12-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String relationIDX：关联外键ID ；
     * @return void
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public void deleteModelList(String relationIDX) throws BusinessException {
        StringBuffer hql = new StringBuffer();
        hql.append("from QualityControl t where t.relationIDX= '").append(relationIDX).append("'"); 
        List<QualityControl> list = this.daoUtils.find(hql.toString());
        if(list != null && list.size() > 0){
            this.daoUtils.getHibernateTemplate().deleteAll(list);
        }
    }
    /**
     * 
     * <li>说明：根据作业卡id查询作业工单的质量检查信息
     * <li>创建人：程梅
     * <li>创建日期：2013-8-8
     * <li>修改人： 程锐
	 * <li>修改日期：2014-12-25
	 * <li>修改内容：查询三检一验人员信息时取QCResult的信息
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    public List<Map<String,String>> pageListForCardQR(String workCardId){
		List<Map<String,String>>  listMap = new ArrayList<Map<String,String>>();
		List<QualityControl> qcList = WorkSeqManager.getWorkSeqQualityControl("null"); //查询有多少三检一验项目
		Map<String,String> quaMap = new HashMap<String, String>(); //用Map动态的模拟一条记录
		for(QualityControl qc : qcList){ //循环三检一验项目 动态组装出一列数据
			List<QCResult> personList = workTaskResultViewManager.getCheckPerson(workCardId,qc.getCheckItemCode());
			String personName = "——" ; 
			if(personList != null){ //不等于空说明 当前的作业任务有对应的检验项目，否则没有
				personName = personList.get(0).getQcEmpName(); //获取对应三检一验人员名称
			}
			quaMap.put(qc.getCheckItemCode(), personName);
		}
		listMap.add(quaMap);
		return listMap ;
	}
}