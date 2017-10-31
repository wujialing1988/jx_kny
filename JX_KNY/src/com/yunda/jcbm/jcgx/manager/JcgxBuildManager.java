package com.yunda.jcbm.jcgx.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.IbaseComboTree;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jcbm.jcgx.entity.JcgxBuild;
import com.yunda.jcbm.jcxtfl.entity.JcxtflBuild;
import com.yunda.jcbm.jcxtfl.manager.JcxtflBuildManager;
import com.yunda.third.poi.excel.ExcelUtil;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JcgxBuild业务类，机车构型
 * <li>创建人：王利成
 * <li>创建日期：2016-5-16
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value = "jcgxBuildManager")
public class JcgxBuildManager extends  JXBaseManager<JcgxBuild, JcgxBuild> implements IbaseComboTree {
    // 根节点的父节点ID
    public final static String GX_PARENT_ID_OF_ROOT = "0";
    
    @Resource
    private JcxtflBuildManager jcxtflBuildManager;

    @Autowired
    private  JcgxBuildOrderManager jcgxBuildOrderManager;
    /**
     * <li>说明：构建机车构型的全位置名称
     * <li>创建人：王利成
     * <li>创建日期：2016-5-19
     * <li>修改人：
     * <li>修改日期：
     * @param shortName 车型简称
     * @param jb 构型实体
     * @return 机车构型的全位置名称
     */
    private String buildWzqm(String shortName, JcgxBuild jb) {
        List<String> list = new ArrayList<String>();
        // 获取机车构型，以及从自身所在节点到根节点的所有机车构型的“分类简称”集合
        this.buildWzqm(jb, list);
        if (null == list || list.isEmpty()) {
            return "";
        }
        list.add(shortName);
        // 反转列表
        Collections.reverse(list);
        StringBuilder sb = new StringBuilder(50);
        for (String wzqm : list) {
            sb.append(JcgxBuild.WZQM_SEPARATOR_CHAR).append(wzqm);
        }
        return sb.substring(1);
    }
    
    /**
     * <li>说明：获取机车构型，以及从自身所在节点到根节点的所有机车构型的“分类简称”集合（递归）
     * <li>创建人：王利成
     * <li>创建日期：2016-5-19
     * <li>修改人：
     * <li>修改日期：
     * @param jb 机车构型实体
     * @param list 从自身所在节点到根节点的所有机车构型的“分类简称”集合
     */
    private void buildWzqm(JcgxBuild jb, List<String> list) {
        if (null == jb) {
            return;
        }
        String fljc = jb.getFljc();
        // 分类简称
        if (!StringUtil.isNullOrBlank(fljc)) {
            list.add(fljc);
        }
        String hql = "From JcgxBuild Where recordStatus = 0 And coID = '" + jb.getFjdID() + "'";
        JcgxBuild build = this.findSingle(hql);
        buildWzqm(build, list);
    }
    
    /**
     * <li>说明：生成UUID
     * <li>创建人：王利成
     * <li>创建日期：2016-5-19
     * <li>修改人：
     * <li>修改日期：
     * @return String
     */
    public String createUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    /**
     * <li>说明：级联删除机车构型及其子构型
     * <li>创建人：王利成
     * <li>创建日期：2016-5-20
     * <li>修改人：
     * <li>修改日期：
     * @param ids 主键序列
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    @SuppressWarnings("unchecked")
    public void deleteByCasecade(Serializable... ids) throws BusinessException, NoSuchFieldException {
        if (null == ids || ids.length <= 0) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            sb.append(Constants.JOINSTR).append(Constants.SINGLE_QUOTE_MARK).append(ids[i]).append(Constants.SINGLE_QUOTE_MARK);
        }
        StringBuffer sql = new StringBuffer("SELECT CO_ID FROM T_JCBM_JCGX WHERE RECORD_STATUS = 0");
        sql.append(" START WITH CO_ID in(" + sb.substring(1) + ") CONNECT BY PRIOR CO_ID = FJD_ID");
        List<Object> list = this.daoUtils.executeSqlQuery(sql.toString());
        
        // 每500条记录执行一次删除，因为sql的in条件中，最大支持1000个关键字
        int size = 500;
        List<Serializable> idList = new ArrayList<Serializable>(size);
        int index = 0;
        while (index < list.size()) {
            idList.add((Serializable) list.get(index));
            index++;
            if (size == idList.size()) {
                jcgxBuildOrderManager.logicDelete(idList.toArray(new Serializable[size]));
//                this.deleteByIds(idList.toArray(new Serializable[size]));
                idList.clear();
            }
        }
        // 如果删除的记录刚好是500的整倍数，则无需执行最后的删除
        if (idList.isEmpty()) {
            return;
        }
        // 删除余下的记录
        jcgxBuildOrderManager.logicDelete(idList.toArray(new Serializable[idList.size()]));
//        this.deleteByIds(idList.toArray(new Serializable[idList.size()]));
    }
    
    /**
     * <li>说明：构型当前节点和子节点
     * <li>创建人：王利成
     * <li>创建日期：2016-5-19
     * <li>修改人：
     * <li>修改日期：
     * @param objList 机车分类
     * @param shortName 车型
     * @param nodeIDX 构型节点主键 void
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    private void insertAllNode(JcxtflBuild[] objList, String shortName, String nodeIDX) throws BusinessException, NoSuchFieldException {
        List<JcxtflBuild> entityList = new ArrayList<JcxtflBuild>();
        for (JcxtflBuild jb : objList) {
            entityList.add(jcxtflBuildManager.getEntityByIDX(jb.getCoID()));
        }
        for (JcxtflBuild jb : entityList) {
            saveAll(jb, shortName, nodeIDX);
        }
    }
    
    /**
     * <li>说明：保存分类节点
     * <li>创建人：王利成
     * <li>创建日期：2016-5-19
     * <li>修改人：
     * <li>修改日期：
     * @param objList 机车分类构型
     * @param type 保存类型（"single"-保存当前节点;"all"-保存当前节点及其子节点）
     * @param shortName 车型
     * @param nodeIDX 构型节点主键
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void insertJcgx(JcxtflBuild[] objList, String type, String shortName, String nodeIDX) throws BusinessException, NoSuchFieldException {
        JcgxBuild jcgx = this.getModelById(nodeIDX);
        if (null != jcgx && JcgxBuild.CO_HAS_CHILD_F == jcgx.getCoHaschild().intValue()) {
            jcgx.setCoHaschild(JcgxBuild.CO_HAS_CHILD_T);
            this.saveOrUpdate(jcgx);
        }
        
        if (type.equals("single")) {
            insertSingleNode(objList, shortName, nodeIDX);
        }
        if (type.equals("all")) {
            insertAllNode(objList, shortName, nodeIDX);
        }
    }
    
    /**
     * <li>说明：构型当前节点
     * <li>创建人：王利成
     * <li>创建日期：2016-5-19
     * <li>修改人：
     * <li>修改日期：
     * @param objList 机车分类
     * @param shortName 车型
     * @param nodeIDX 机车构型节点主键 void
     * @throws NoSuchFieldException
     * @throws BusinessException
     */
    private void insertSingleNode(JcxtflBuild[] objList, String shortName, String nodeIDX) throws BusinessException, NoSuchFieldException {
        
        JcgxBuild jcgx = this.getModelById(nodeIDX);
        String wzqm = null;
        if (null != jcgx) {
            wzqm = jcgx.getWzqm();
        }
        JcgxBuild entity = null;
        for (JcxtflBuild jb : objList) {
            jb = jcxtflBuildManager.getModelById(jb.getCoID());
            entity = new JcgxBuild();
            entity.setCoID(this.createUUID());
            entity.setFjdID(nodeIDX);
            entity.setSycx(shortName);
            entity.setFlbm(jb.getFlbm());
            entity.setFlmc(jb.getFlmc());
            entity.setFljc(jb.getFljc());
            entity.setCoHaschild(JcgxBuild.CO_HAS_CHILD_F);
            
            if (StringUtils.isNotBlank(jb.getZylxID())) {
                entity.setZylx(jb.getZylx());
                entity.setZylxID(jb.getZylxID());
            }
            
            entity.setRecordStatus(0);
            if (StringUtil.isNullOrBlank(wzqm)) {
                entity.setWzqm(shortName + JcgxBuild.WZQM_SEPARATOR_CHAR +jb.getFljc());
            } else {
                entity.setWzqm(wzqm + JcgxBuild.WZQM_SEPARATOR_CHAR + jb.getFljc());
            }          
            // 保存前设置顺序号
            jcgxBuildOrderManager.insertSeqNo(entity);
            jcgxBuildOrderManager.saveOrUpdateSeqNo(entity);
            
        }
//        this.saveOrUpdate(list);
    }
    
    /**
     * <li>说明：获得当前节点下的所有子节点（递归）
     * <li>创建人：王利成
     * <li>创建日期：2016-5-19
     * <li>修改人：
     * <li>修改日期：
     * @param jb 机车构型
     * @param shortName 车型
     * @param nodeIDX 选中节点的主键
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    private void saveAll(JcxtflBuild jb, String shortName, String nodeIDX) throws BusinessException, NoSuchFieldException {
        JcgxBuild jcgx = new JcgxBuild();
        jcgx.setCoID(this.createUUID());
        jcgx.setFjdID(nodeIDX);
        jcgx.setSycx(shortName);
        jcgx.setFlbm(jb.getFlbm());
        jcgx.setFlmc(jb.getFlmc());
        jcgx.setFljc(jb.getFljc());
        
        if (StringUtils.isNotBlank(jb.getZylxID())) {
            jcgx.setZylx(jb.getZylx());
            jcgx.setZylxID(jb.getZylxID());
        }
        
        jcgx.setRecordStatus(0);
        // 设置机车构型的位置全名
        JcgxBuild temp = this.getModelById(nodeIDX);
        if (null != temp && !StringUtil.isNullOrBlank(temp.getWzqm())) {
            jcgx.setWzqm(temp.getWzqm() + JcgxBuild.WZQM_SEPARATOR_CHAR + jb.getFljc());
        } else {
            jcgx.setWzqm(shortName + JcgxBuild.WZQM_SEPARATOR_CHAR + jb.getFljc());
        }
        int coHaschild = JcgxBuild.CO_HAS_CHILD_T;
        List<JcxtflBuild> children = jb.getChildren();
        if (null == children || children.isEmpty()) {
            coHaschild = JcgxBuild.CO_HAS_CHILD_F;
        }
        jcgx.setCoHaschild(coHaschild);
        // 保存前设置顺序号
        jcgxBuildOrderManager.insertSeqNo(jcgx);
        jcgxBuildOrderManager.saveOrUpdateSeqNo(jcgx);
//        this.daoUtils.getHibernateTemplate().save(jcgx);
        if (coHaschild == JcgxBuild.CO_HAS_CHILD_F) {
            return;
        }
        for (JcxtflBuild child : children) {
            saveAll(child, shortName, jcgx.getCoID());
        }
    }
    
    /**
     * <li>说明：根据车型简称查询机车构型树
     * <li>创建人：王利成
     * <li>创建日期：2016-5-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 父节点ID
     * @param shortName 车型简称
     * @return 机车构型树
     */
    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> tree(String parentIDX, String shortName) {
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        String hql = "From JcgxBuild Where fjdID = ? And sycx = ? And recordStatus = " + Constants.NO_DELETE + " Order By seqNo ASC";
        List<JcgxBuild> list = (List<JcgxBuild>) this.daoUtils.find(hql, new Object[] { parentIDX, shortName });
        int i = 0;
        for (JcgxBuild t : list) {
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            nodeMap.put("id", t.getCoID());         // 节点idx主键
            nodeMap.put("text", t.getFljc());       // 树节点显示名称
            nodeMap.put("leaf", (t.getCoHaschild().intValue() == JcgxBuild.CO_HAS_CHILD_F));
            nodeMap.put("flbm", t.getFlbm());       // 分类编码
            nodeMap.put("index", ++i);       // 分类编码
            children.add(nodeMap);
        }
        return children;
    }
    
    /**
     * <li>说明：更新机车构型全位置名称，例如：电力交流/控制及通讯/控制及线上设备
     * <li>创建人：王利成
     * <li>创建日期：2016-5-19
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param shortName 车型简称
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    public void updateWzqm(String shortName) throws BusinessException, NoSuchFieldException {
        // 查询该车型的所有机车构型集合
        String hql = "From JcgxBuild Where sycx = ? and recordStatus = 0 ";
        List<JcgxBuild> jbList = this.daoUtils.find(hql, new Object[] { shortName });
        for (JcgxBuild jb : jbList) {
            // 设置机车构型的全位置名称，例如：电力交流/控制及通讯/控制及线上设备
            jb.setWzqm(this.buildWzqm(shortName, jb));
        }
        this.saveOrUpdate(jbList);
    }

    /**
     * <li>说明：更新机车构型全位置名称，例如：电力交流/控制及通讯/控制及线上设备（递归查询）
     * <li>创建人：何涛
     * <li>创建日期：2016-5-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车构型实体对象
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    private void updateWzqm(JcgxBuild entity) throws BusinessException, NoSuchFieldException {
        // 递归查询该节点及其下属所有子节点的机车构型实体集合
        String sql = "SELECT * FROM T_JCBM_JCGX WHERE RECORD_STATUS = 0 START WITH CO_ID = '" + entity.getCoID() + "' CONNECT BY PRIOR CO_ID = FJD_ID";
        List<JcgxBuild> entityList = this.daoUtils.executeSqlQueryEntity(sql, JcgxBuild.class);
        
        String shortName = entity.getSycx();
        for (JcgxBuild jb : entityList) {
            jb.setWzqm(this.buildWzqm(shortName, jb));
        }
        this.saveOrUpdate(entityList);
    }

    /**
     * <li>说明：通过拖拽的方式调整机车构型节点顺序
     * <li>创建人：何涛
     * <li>创建日期：2016-05-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 被拖拽的节点（idx主键）
     * @param oldParent 旧父节点（idx主键）
     * @param newParent 新父节点（idx主键）
     * @throws Exception 
     * @throws NumberFormatException 
     */
    @SuppressWarnings("unchecked")
    public void updateToMoveNode(String nodeIDX, String oldParent, String newParent,String index) throws NumberFormatException, Exception {
        // 获取被拖拽的节点实体
        JcgxBuild entity = this.getModelById(nodeIDX);
        // 验证节点所在父节点是否已经在后台已经被更新，但页面没有刷新
        if (!oldParent.equals(entity.getFjdID())) {
            throw new BusinessException("操作失败，请刷新后重试！");
        }
        // 更新父节点为新的父节点（idx主键）
        entity.setFjdID(newParent);
        jcgxBuildOrderManager.saveMoveNode(entity, oldParent, newParent,index);
//        this.saveOrUpdate(entity);
        
        // 更新构型位置全名
        this.updateWzqm(entity);
    }

    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：张迪
     * <li>创建日期：2016-9-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param coID
     * @param orderType
     * @return
     * @throws Exception
     */
    public String[] validateMoveOrder(String coID, int orderType) throws Exception {
        return  jcgxBuildOrderManager.validateMoveOrder(coID, orderType);
    }

    /**
     * <li>说明：调整节点顺序
     * <li>创建人：张迪
     * <li>创建日期：2016-9-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param coID 构型主键
     * @param orderType 需调整顺序
     * @throws Exception
     */
    public void updateMoveOrder(String coID, int orderType) throws Exception {
        jcgxBuildOrderManager.updateMoveOrder(coID, orderType);
        
    }
 
/**
     * <li>说明：导入机车构型
     * <li>创建人：何东
     * <li>创建日期：2016-08-31
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param jcgxFile 导入机车构型文件
     * @param shortName 机车简称
     * @throws FileNotFoundException
     * @throws IOException
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public synchronized void saveImport(File jcgxFile, String shortName) throws FileNotFoundException, IOException, BusinessException, NoSuchFieldException {
        POIFSFileSystem poi = null;
        try {
            poi = new POIFSFileSystem(new FileInputStream(jcgxFile));
        } catch (Exception e) {
            throw new BusinessException("Excel文件解析异常，请确认上传的文件格式是否正确！");
        }
        HSSFWorkbook workBook = new HSSFWorkbook(poi);
        int sheetIndex = 0;
        HSSFSheet sheet = workBook.getSheetAt(sheetIndex);
        while (null != sheet) {
            try {
                saveByStencil(sheet, shortName);
                sheet = workBook.getSheetAt(++sheetIndex);
            } catch (Exception e) {
                break;
            }
        }
    }
    
    /**
     * <li>说明：导入单个sheet中的机车构型系统分类
     * <li>创建人：何东
     * <li>创建日期：2016-08-31
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param sheet Excel工作薄中的单个sheet页实例
     * @param shortName 机车简称
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private void saveByStencil(HSSFSheet sheet, String shortName) throws BusinessException, NoSuchFieldException {
        String[][] tableValues = ExcelUtil.getTableValue(sheet, "2A");
        if (null == tableValues || tableValues.length <= 0) {
            return;
        }
        
        List<JcgxBuild> list = parseTableValues(tableValues, shortName);
        
        // 先清空该车型的所有构型数据
        String sql = " delete from T_JCBM_JCGX where SYCX = '" + shortName + "'";
        this.daoUtils.executeSql(sql);
        
        this.daoUtils.bluckInsert(list);
    }
    
    /**
     * <li>说明：解析sheet单元格数据为java对象实体集合
     * <li>创建人：何东
     * <li>创建日期：2016-08-31
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param tableValues sheet单元格数据
     * @param shortName 机车简称
     * @return 机车系统分类实体集合
     */
    @SuppressWarnings("unused")
    private List<JcgxBuild> parseTableValues(String[][] tableValues, String shortName) {
        List<JcgxBuild> list = new ArrayList<JcgxBuild>();
        JcgxBuild jb = null;
        String[] values = null;
        String rootIdx = "";
        // 实体初始化，并判定是否是叶子节点
        for (int i = 0; i < tableValues.length; i++) {
            values = tableValues[i];
            
            // 构型树不需要机车系统分类的根节点
            if (JcxtflBuildManager.PARENT_ID_OF_ROOT.equals(values[1])) {
                rootIdx = values[0];
                continue;
            }
            
            jb = new JcgxBuild();
            jb.setCoID(values[0]);
            
            // 将构型根节点的父ID设置为0
            if (rootIdx.equals(values[1])) {
                jb.setFjdID(GX_PARENT_ID_OF_ROOT);
            } else {
                jb.setFjdID(values[1]);
            }
            
            jb.setFlbm(values[2]);
            jb.setFlmc(values[3]);
            jb.setFljc(values[4]);
            
            // 判断是否是叶子节点
            int leaf = JcgxBuild.CO_HAS_CHILD_F;
            for (int j = 0; j < tableValues.length; j++) {
                if (values[0].equals(tableValues[j][1])) {
                    leaf = JcgxBuild.CO_HAS_CHILD_T;
                    break;
                }
            }
            jb.setCoHaschild(leaf);
            
            // 查询机车系统分类，设置实体的专业类型
            StringBuilder xtflHql = new StringBuilder(" from JcxtflBuild f");
            xtflHql.append(" where '/' || f.sycx || '/' like '%/" + shortName + "/%' and f.flbm = '" + values[2] + "'");
            xtflHql.append(" and f.recordStatus = " + Constants.NO_DELETE);
            JcxtflBuild xtfl = (JcxtflBuild)this.daoUtils.findSingle(xtflHql.toString());
            if (xtfl != null) {
                jb.setZylxID(xtfl.getZylxID());
                jb.setZylx(xtfl.getZylx());
                
                jb.setPyjc(StringUtils.isNotBlank(xtfl.getPyjc()) ? xtfl.getPyjc() : null);
                jb.setLbjbm(StringUtils.isNotBlank(xtfl.getLbjbm()) ? xtfl.getLbjbm() : null);
                jb.setJxzy(xtfl.getJxzy() != null ? xtfl.getJxzy() : null);
                jb.setSfsyDzda(xtfl.getSfsyDzda() != null ? xtfl.getSfsyDzda() : null);
                jb.setSfzbzy(xtfl.getSfzbzy() != null ? xtfl.getSfzbzy() : null);
            }
            
            jb.setSycx(shortName);
            jb.setRecordStatus(Constants.NO_DELETE);
            
            list.add(jb);
        }
        
        genNewId(list, GX_PARENT_ID_OF_ROOT, GX_PARENT_ID_OF_ROOT);
        return list;
    }
    
    /**
     * <li>说明：为构型数据产生新的主键
     * <li>创建人：何东
     * <li>创建日期：2016-08-31
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param list
     * @param parentId
     * @param newParentId
     */
    private void genNewId(List<JcgxBuild> list, String parentId, String newParentId) {
        if (StringUtils.isNotBlank(parentId)) {
            int j = 1;
            for (JcgxBuild gx : list) {
                if (parentId.equals(gx.getFjdID())) {
                    String oldId = gx.getCoID();
                    
                    String newId = createUUID();
                    gx.setCoID(newId);
                    gx.setFjdID(newParentId);
                    gx.setSeqNo(j);
                    
                    j++;
                    
                    if (JcgxBuild.CO_HAS_CHILD_T == gx.getCoHaschild()) {
                        genNewId(list, oldId, newId);
                    }
                }
            }
        }
    }
    
    /**
     * <li>说明：重写新增方法，保存分类简称为分类名称+位置
     * <li>创建人：张迪
     * <li>创建日期：2016-09-17
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param JcgxBuild t 实体对象
     * @throws BusinessException, NoSuchFieldException
     */ 
    @Override
    public void saveOrUpdate(JcgxBuild t) throws BusinessException, NoSuchFieldException {
        if(null != t.getWzmc() && !"".equals(t.getWzmc())){
            t.setFljc(t.getFlmc() + t.getWzmc());
        }        
        super.saveOrUpdate(t);
    }   
    
    /**
     * <li>说明：重写getBaseComboTree，获取下拉树前台store所需List<HashMap>对象（构型下拉树数据查询接口）
     * <li>创建人：何东
     * <li>创建日期：2017-04-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param req 
     * @return List<HashMap> 下拉树前台store所需List<HashMap>对象
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes"})
    public List<HashMap> getBaseComboTree(HttpServletRequest req) throws Exception{
        String queryParams = StringUtil.nvlTrim(req.getParameter("queryParams"), "{}");
        Map queryParamsMap = JSONUtil.read(queryParams, Map.class);
        
        String parentIDX = StringUtil.nvlTrim(req.getParameter("parentIDX"), "0");
        String shortName = (String)queryParamsMap.get("shortName");
        
        return getJcgxBuildTree(parentIDX, shortName);
    }
    
    /**
     * 查询车辆构型树形结构数据
     * @param parentIDX 父ID
     * @param shortName 车型简称
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<HashMap> getJcgxBuildTree(String parentIDX, String shortName) {
        List<HashMap> children = new ArrayList<HashMap>();
        
        if (StringUtils.isBlank(parentIDX)) {
        	parentIDX = "0";
        }
        
        if (StringUtils.isBlank(shortName)) {
            return children;
        }
        
        String hql = "From JcgxBuild Where fjdID = ? And sycx = ? And recordStatus = " + Constants.NO_DELETE + " Order By seqNo ASC";
        List<JcgxBuild> list = (List<JcgxBuild>) this.daoUtils.find(hql, new Object[] { parentIDX, shortName });
        int i = 0;
        for (JcgxBuild t : list) {
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            nodeMap.put("id", t.getCoID());         // 节点idx主键
            nodeMap.put("text", t.getFljc());       // 树节点显示名称
            nodeMap.put("leaf", (t.getCoHaschild().intValue() == JcgxBuild.CO_HAS_CHILD_F));
            nodeMap.put("flbm", t.getFlbm());       // 分类编码
            nodeMap.put("index", ++i);
            children.add(nodeMap);
        }
        
        return children;
    }
}
