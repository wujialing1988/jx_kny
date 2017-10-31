package com.yunda.jx.pjjx.partsrdp.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdpNodeMat;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdpNodeMatBean;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 物料实例化业务类
 * <li>创建人：张迪
 * <li>创建日期：2016-9-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="partsRdpNodeMatManager")
public class PartsRdpNodeMatManager extends JXBaseManager<PartsRdpNodeMat, PartsRdpNodeMat>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	
	/**
	 * <li>说明：查询列表，联合查询物料信息表，用以查询物料的最新单价
	 * <li>创建人：张迪
	 * <li>创建日期：2016-9-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchEntity 查询条件
	 * @return 物料实例化列表
	 */    
    @SuppressWarnings("unchecked")
    public List<PartsRdpNodeMat> findListForMat(PartsRdpNodeMat entity) {
        StringBuilder sb = new StringBuilder();
        sb.append(" Select t.idx  idx, t.rdp_node_idx rdpNodeIDX, t.mat_code matCode, t.mat_desc matDesc, t.unit, t.Default_Qty numberRated, l.price, m.qty from pjjx_parts_rdp_node_mat t inner join  PJWZ_MAT_TYPE_LIST l on t.mat_code = l.mat_code " +
                "left join  PJJX_Parts_Rdp_Expend_Mat m on  t.mat_code = m.mat_code and t.rdp_node_idx = m.rdp_node_idx where t.record_status = 0 ");
        // 查询条件 - 检修作业节点idx主键
        if (!StringUtil.isNullOrBlank(entity.getRdpNodeIDX())) {
            sb.append(" And t.rdp_node_idx = '").append(entity.getRdpNodeIDX()).append("'");
        }
        // 查询条件 - 物料编码
        if (!StringUtil.isNullOrBlank(entity.getMatCode())) {
            sb.append(" And t.mat_code Like '%").append(entity.getMatCode()).append("%'");
        }
        // 查询条件 - 物料描述
        if (!StringUtil.isNullOrBlank(entity.getMatDesc())) {
            sb.append(" And t.mat_desc Like '%").append(entity.getMatDesc()).append("%'");
        }
        // 排序   
        sb.append(" Order By t.mat_code ");
        final String str= sb.toString();
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (List<PartsRdpNodeMat>)template.execute(new HibernateCallback(){
            public List<PartsRdpNodeMat> doInHibernate(Session s) {
                //获取总记录数
                org.hibernate.SQLQuery query = s.createSQLQuery(str);
                query.addScalar("idx", Hibernate.STRING)
                .addScalar("rdpNodeIDX", Hibernate.STRING)
                .addScalar("matCode", Hibernate.STRING)
                .addScalar("matDesc", Hibernate.STRING)
                .addScalar("unit", Hibernate.STRING)
                .addScalar("numberRated", Hibernate.LONG)
                .addScalar("price", Hibernate.DOUBLE)
                .addScalar("qty", Hibernate.DOUBLE)
                .setResultTransformer(Transformers.aliasToBean(PartsRdpNodeMat.class));
                return (List<PartsRdpNodeMat>)query.list();
            }
        });
    }
    
    /**
     * <li>说明：查询列表，联合查询物料信息表，用以查询物料的最新单价（分页）
     * <li>创建人：张迪
     * <li>创建日期：2016-9-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询条件
     * @return 物料实例化列表
     */   
    @SuppressWarnings("unchecked")
    public Page<PartsRdpNodeMatBean> findPageForMat(SearchEntity<PartsRdpNodeMat> searchEntity) {
        PartsRdpNodeMat entity = searchEntity.getEntity();
        StringBuilder sb = new StringBuilder();
        sb.append(" Select t.idx, t.rdp_node_idx,t.rdp_idx, t.mat_code, t.mat_desc, t.unit, t.Default_Qty, l.price, m.qty from pjjx_parts_rdp_node_mat t inner join  PJWZ_MAT_TYPE_LIST l on t.mat_code = l.mat_code " +
                "left join  PJJX_Parts_Rdp_Expend_Mat m on  t.mat_code = m.mat_code and t.rdp_node_idx = m.rdp_node_idx where t.record_status = 0 ");
        // 查询条件 - 检修作业节点idx主键
        if (!StringUtil.isNullOrBlank(entity.getRdpNodeIDX())) {
            sb.append(" And t.rdp_node_idx = '").append(entity.getRdpNodeIDX()).append("'");
        }
        // 查询条件 - 物料编码
        if (!StringUtil.isNullOrBlank(entity.getMatCode())) {
            sb.append(" And t.mat_code Like '%").append(entity.getMatCode()).append("%'");
        }
        // 查询条件 - 物料描述
        if (!StringUtil.isNullOrBlank(entity.getMatDesc())) {
            sb.append(" And t.mat_desc Like '%").append(entity.getMatDesc()).append("%'");
        }
        // 排序   
        sb.append(" Order By t.mat_code ");
        String sql = sb.toString();
        String totalSql = "Select count(*) as rowcount " + sql.substring(sql.indexOf("from"));
        return queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, PartsRdpNodeMatBean.class);
    }

    /**
     * <li>说明：新增物料信息保存
     * <li>创建人：张迪
     * <li>创建日期：2016-9-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsRdpNodeMats 检修用料实体化实例数组
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveNodeMatList(PartsRdpNodeMat[] partsRdpNodeMats) throws BusinessException, NoSuchFieldException {
        List<PartsRdpNodeMat> entityList = new ArrayList<PartsRdpNodeMat>();
        for (PartsRdpNodeMat mat : partsRdpNodeMats) {
            // 验证”物料编码“是否唯一
            String[] msg = this.validateUpdate(mat);
            if (null != msg) {
                continue;
            }
            if(null == mat.getNumberRated() || 0 >= mat.getNumberRated()) {
                mat.setNumberRated(1L);
            }
            entityList.add(mat);
        }
        if (entityList.size() > 0) {
            this.saveOrUpdate(entityList);
        }
    }
    
}