package com.yunda.jx.pjwz.partsBase.repairlist.manager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.manager.IOmOrganizationManager;
import com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType;
import com.yunda.jx.pjwz.partsBase.partstype.manager.PartsTypeManager;
import com.yunda.jx.pjwz.partsBase.repairlist.entity.PartsRepairList;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配件自修列表
 * <li>创建人： 张凡
 * <li>创建日期：2015-11-4
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 3.2.4
 */
@Service(value = "partsRepairListManager")
public class PartsRepairListManager extends JXBaseManager<PartsRepairList, PartsRepairList> {
    
    @Autowired
    private IOmOrganizationManager orgManager;
    
    /** 配件规格型号业务类 */
    @Resource
    private PartsTypeManager partsTypeManager;
    
    /**
     * <li>说明：查询自修配件列表
     * <li>创建人：张凡
     * <li>创建时间：2013-10-23 上午10:37:05
     * <li>修改人：何涛
     * <li>修改日期：2016-01-31
     * <li>修改内容：重构，使用自定义封装实体的分页的分页查询
     * @param searchJson 查询条件JSON字符串
     * @param start 查询开始索引
     * @param limit 查询结束索引
     * @param orders 排序
     * @return 自修配件分页列表
     * @throws BusinessException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @throws SecurityException
     * @throws NoSuchFieldException
     */
    public Page<PartsRepairListBean> findPageDataList(String searchJson, int start, int limit, Order[] orders) throws BusinessException,
        JsonParseException, JsonMappingException, IOException, SecurityException, NoSuchFieldException {
        StringBuilder sb =
            new StringBuilder(
                "SELECT * FROM (SELECT A.*, B.ORGNAME, B.ORGSEQ FROM (SELECT L.*, T.PARTS_NAME, T.SPECIFICATION_MODEL FROM PJWZ_PARTS_REPAIR_LIST L, PJWZ_PARTS_TYPE T WHERE L.RECORD_STATUS = 0 AND T.RECORD_STATUS = 0 AND L.PARTS_TYPE_IDX = T.IDX) A LEFT JOIN OM_ORGANIZATION B ON A.REPAIR_ORGID = B.ORGID) WHERE 0 = 0");
        PartsRepairListBean bean = JSONUtil.read(StringUtil.nvl(searchJson, "{}"), PartsRepairListBean.class);
        
        // 查询条件 - 配件名称
        if (!StringUtil.isNullOrBlank(bean.getPartsName())) {
            sb.append(" AND PARTS_NAME LIKE '%").append(bean.getPartsName()).append("%'");
        }
        // 查询条件 - 规格型号
        if (!StringUtil.isNullOrBlank(bean.getSpecificationModel())) {
            sb.append(" AND LOWER(SPECIFICATION_MODEL) LIKE '%").append(bean.getSpecificationModel().toLowerCase()).append("%'");
        }
        // 查询条件 - 检修班组
        if (!StringUtil.isNullOrBlank(bean.getOrgname())) {
            sb.append(" AND ORGNAME LIKE '%").append(bean.getOrgname()).append("%'");
        }
        // 查询条件 - 检修班组id
        if (!StringUtil.isNullOrBlank(bean.getRepairOrgID())) {
            sb.append(" AND REPAIR_ORGID LIKE '%").append(bean.getRepairOrgID()).append("%'");
        }
        // 排序处理
        if (null != orders && orders.length > 0) {
            String[] order = orders[0].toString().split(" ");
            String sort = order[0];
            String dir = order[1];
            Class clazz = PartsRepairListBean.class;
            Field field = clazz.getDeclaredField(sort);
            Column annotation = field.getAnnotation(Column.class);
            if (null != annotation) {
                sb.append(" ORDER BY ").append(annotation.name()).append(" ").append(dir);
            } else {
                sb.append(" ORDER BY ").append(sort).append(" ").append(dir);
            }
        } else {
            sb.append("ORDER BY UPDATE_TIME DESC");
        }
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
        return this.queryPageList(totalSql, sb.toString(), start, limit, false, PartsRepairListBean.class);
    }
    
    /**
     * <li>说明：更新前验证
     * <li>创建人：张凡
     * <li>创建日期：2013-10-23
     * <li>修改人：何涛
     * <li>修改日期：2016-01-30
     * <li>修改内容：代码重构
     * @param t 实体对象
     * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
     * @throws BusinessException
     */
    @Override
    public String[] validateUpdate(PartsRepairList t) {
        if (t.getRepairOrgID() == null || t.getPartsTypeIDX() == null) {
            return new String[] { "数据不完整！" };
        }
        String hql = "From PartsRepairList Where partsTypeIDX = ? and repairOrgID = ?";
        PartsRepairList entity = (PartsRepairList) this.daoUtils.findSingle(hql, new Object[] { t.getPartsTypeIDX(), t.getRepairOrgID() });
        if (null != entity && !entity.getIdx().equals(t.getIdx())) {
            return new String[] { "不可以添加重复的数据！" };
        }
        return super.validateUpdate(t);
    }
    
    /**
     * <li>说明：新增委外配件型号列表
     * <li>创建人：张凡
     * <li>创建时间：2013-11-27 下午04:13:31
     * <li>修改人：何涛
     * <li>修改日期：2016-01-29
     * <li>修改内容：代码重构，摈弃使用sql更新数据库的做法，利用框架已封装好的hibernate方法进行数据库更新
     * <li>修改内容：默认设置是否合格验收为“否”
     * @param idxs 使用逗号（,）分割的已选择的配件型号idx主键数组
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    public void saveNewList(String idxs) throws BusinessException, NoSuchFieldException {
        String hql = "From PartsType Where idx In('" + idxs.replace(",", "','") + "')";
        Collection<PartsType> collection = partsTypeManager.find(hql);
        List<PartsRepairList> entityList = new ArrayList<PartsRepairList>(collection.size());
        PartsRepairList entity = null;
        for (PartsType type : collection) {
            entity = new PartsRepairList();
            entity.setPartsTypeIDX(type.getIdx()); // 设置 配件型号主键
            entity.setIsHgys(PartsRepairList.IS_HGYS_NO); // 默认设置是否合格验收为“否”
            entityList.add(entity);
        }
        this.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：更新施修班组
     * <li>创建人：张凡
     * <li>创建时间：2013-11-27 下午04:43:06
     * <li>修改人：何涛
     * <li>修改日期：2016-01-29
     * <li>修改内容：代码重构，摈弃使用sql更新数据库的做法，利用框架已封装好的hibernate方法进行数据库更新
     * @param idxs 使用逗号（,）分割的配件自修列表idx主键数组
     * @param orgid 生产厂家主键
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    public void updateSetRepairOrg(String idxs, String orgid) throws BusinessException, NoSuchFieldException {
        if (StringUtil.isNullOrBlank(idxs)) {
            return;
        }
        String hql = "From PartsRepairList Where idx In ('" + idxs.replace(",", "','") + "')";
        Collection<PartsRepairList> collection = this.find(hql);
        List<PartsRepairList> entityList = new ArrayList<PartsRepairList>();
        PartsRepairList entity;
        for (Iterator<PartsRepairList> i = collection.iterator(); i.hasNext();) {
            entity = i.next();
            entity.setRepairOrgID(orgid);
            entityList.add(entity);
        }
        this.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：获取承修部门树列表
     * <li>创建人：程锐
     * <li>创建日期：2014-5-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 承修部门树列表
     * @throws BusinessException
     */
    public List<Map> findPartsRepairOrgTreeData() throws BusinessException {
        String queryHql = " and orgid in (select repairOrgID from PartsRepairList where recordStatus = 0)";
        List<Map> children = orgManager.findOrgTree(queryHql, false, null);
        return children;
    }
    
    /**
     * <li>方法说明：查询承修班组
     * <li>方法名：findRepairOrg
     * @param start 开始行
     * @param limit 每页记录数
     * @param partsTypeIdx 配件型号主键
     * @return
     *            <li>创建人： 张凡
     *            <li>创建日期：2015-10-19
     *            <li>修改人：
     *            <li>修改内容：
     *            <li>修改日期：
     */
    public Page<PartsRepairList> findRepairOrg(int start, int limit, String partsTypeIdx) {
        String querySQL = SqlMapUtil.getSql("pjwl-query:findRepairList_select");
        
        String fromSql = SqlMapUtil.getSql("pjwl-query:findRepairList_from");
        fromSql += " and l.parts_type_idx = '" + partsTypeIdx + "'";
        
        StringBuffer sqlOrder = new StringBuffer(" order by t.update_Time DESC");
        String totalSql = "select count(1) " + fromSql;
        String sql = querySQL + fromSql + sqlOrder;
        return super.findPageList(totalSql, sql, start, limit, null, null);
    }
    
    /**
     * <li>方法说明：更新配件自修表是否合格验收字段
     * <li>创建人：曾雪
     * <li>创建日期：2016-1-21
     * <li>修改人：何涛
     * <li>修改日期：2016-1-29
     * <li>修改内容：代码重构，摈弃使用sql更新数据库的做法，利用框架已封装好的hibernate方法进行数据库更新
     * @param ids 配件自修列表idx主键数组
     * @param hgysCode 是否合格验收
     * @throws InterruptedException
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateIsHgys(String[] ids, String hgysCode) throws InterruptedException, BusinessException, NoSuchFieldException {
        List<PartsRepairList> entityList = new ArrayList<PartsRepairList>(ids.length);
        PartsRepairList entity;
        for (String idx : ids) {
            entity = this.getModelById(idx);
            entity.setIsHgys(hgysCode); // 设置 是否合格验收
            entityList.add(entity);
        }
        this.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：查询该配件是否合格验收
     * <li>创建人：程锐
     * <li>创建日期：2016-2-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsTypeIDX 配件型号IDX
     * @return true 是 false 否
     * @throws Exception
     */
    public boolean isHgysByPartsType(String partsTypeIDX) throws Exception {
        String hql = "from PartsRepairList where recordStatus = 0 and partsTypeIDX = ?";
        PartsRepairList partsRepairList = (PartsRepairList) daoUtils.findSingle(hql, new Object[]{partsTypeIDX});
        if (partsRepairList != null && !StringUtil.isNullOrBlank(partsRepairList.getIsHgys())) {
            if (PartsRepairList.IS_HGYS_YES.equals(partsRepairList.getIsHgys()))
                return true;
            else
                return false;
        }
        return true;    
    }
    
    /**
     * <li>标题: 机车检修管理信息系统
     * <li>说明: 自定义分页查询的封装实体
     * <li>创建人：何涛
     * <li>创建日期：2016-1-30
     * <li>修改人:
     * <li>修改日期：
     * <li>修改内容：
     * <li>版权: Copyright (c) 2008 运达科技公司
     * @author 信息系统事业部检修系统项目组
     * @version 3.2.4
     */
    @Entity
    @JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
    public static final class PartsRepairListBean {
        
        @Id
        private String idx;
        
        /* 配件型号主键 */
        @Column(name = "Parts_Type_IDX")
        private String partsTypeIDX;
        
        /* 生产厂家主键 */
        @Column(name = "Repair_OrgID")
        private String repairOrgID;
        
        /* 是否合格验收 */
        @Column(name = "isHgys")
        private String isHgys;
        
        /* 机构名称 */
        private String orgname;
        
        /* 机构系列 */
        private String orgseq;
        
        /* 配件名称 */
        @Column(name = "Parts_Name")
        private String partsName;
        
        /* 规格型号 */
        @Column(name = "Specification_Model")
        private String specificationModel;
        
        public String getIdx() {
            return idx;
        }
        
        public void setIdx(String idx) {
            this.idx = idx;
        }
        
        public String getIsHgys() {
            return isHgys;
        }
        
        public void setIsHgys(String isHgys) {
            this.isHgys = isHgys;
        }
        
        public String getOrgname() {
            return orgname;
        }
        
        public void setOrgname(String orgname) {
            this.orgname = orgname;
        }
        
        public String getOrgseq() {
            return orgseq;
        }
        
        public void setOrgseq(String orgseq) {
            this.orgseq = orgseq;
        }
        
        public String getPartsName() {
            return partsName;
        }
        
        public void setPartsName(String partsName) {
            this.partsName = partsName;
        }
        
        public String getPartsTypeIDX() {
            return partsTypeIDX;
        }
        
        public void setPartsTypeIDX(String partsTypeIDX) {
            this.partsTypeIDX = partsTypeIDX;
        }
        
        public String getRepairOrgID() {
            return repairOrgID;
        }
        
        public void setRepairOrgID(String repairOrgID) {
            this.repairOrgID = repairOrgID;
        }
        
        public String getSpecificationModel() {
            return specificationModel;
        }
        
        public void setSpecificationModel(String specificationModel) {
            this.specificationModel = specificationModel;
        }
        
    }
    
}
