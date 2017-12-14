package com.yunda.jcbm.jcgx.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jcbm.jcgx.entity.JcgxBuild;
import com.yunda.jcbm.jcgx.entity.JcxtflFault;
import com.yunda.jcbm.jcxtfl.entity.JcxtflBuild;
import com.yunda.jcbm.jcxtfl.entity.JczcBuildBean;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车构型查询业务类
 * <li>创建人：何东
 * <li>创建日期：2016-5-16
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value = "jcgxBuildQueryManager")
public class JcgxBuildQueryManager extends JXBaseManager<JcgxBuild, JcgxBuild> {
    
    /** 使用类型_电子档案专用 */
    public static String USETYPE_DZDA = "dzda";
    
    /** 使用类型_检修专用 */
    public static String USETYPE_JX = "jx";
    
    /** 使用类型_整备专用 */
    public static String USETYPE_ZB = "zb";
    
    /** 使用类型_状态：1 表示该类型被使用 */
    public static Integer USETYPE_STUTAS = 1;
    
    /**
     * <li>说明：根据车型及机车构型主键获取下级树节点列表
     * <li>创建人：何东
     * <li>创建日期：2016-5-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param fjdID 父节点ID
     * @param sycx 车型简称
     * @param useType 使用类型
     * @return 机车构型树
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getJcgxBuildTree(String fjdID, String sycx, String useType) {
        String hql = "From JcgxBuild Where fjdID = ? And sycx = ? And recordStatus = " + Constants.NO_DELETE;
        // 判断父节点ID是否为空，为空则获取根节点
        if (StringUtil.isNullOrBlank(fjdID)) {
            fjdID = JcgxBuild.ROOT_FJD_ID;
        }
        
        // 判断使用类型，不为空时分别判断是否为对应的类型并加入查询条件
        if (USETYPE_DZDA.equals(useType)) {
            hql += " And sfsyDzda = " + USETYPE_STUTAS;
        } else if (USETYPE_JX.equals(useType)) {
            hql += " And jxzy = " + USETYPE_STUTAS;
        } else if (USETYPE_ZB.equals(useType)) {
            hql += " And sfzbzy = " + USETYPE_STUTAS;
        }
        
        List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
        // 查询数据
        List<JcgxBuild> list = (List<JcgxBuild>) this.daoUtils.find(hql, new Object[] { fjdID, sycx });
        if (null == list || 0 >= list.size()) {
            return children;
        }
        
        Map<String, Object> nodeMap = null;
        for (JcgxBuild t : list) {
            nodeMap = new HashMap<String, Object>();
            nodeMap.put("id", t.getCoID());             // 节点idx主键
            nodeMap.put("coID", t.getCoID());           // 节点idx主键
            nodeMap.put("flbm", t.getFlbm());           // 分类编码
            nodeMap.put("lbjbm", t.getLbjbm());         // 零部件名称编码
            nodeMap.put("pyjc", t.getPyjc());           // 拼音简称
            nodeMap.put("fjdID", t.getFjdID());         // 父节点
            nodeMap.put("text", StringUtils.isNotBlank(t.getFljc()) ? t.getFljc() : t.getSycx()); // 树节点显示名称
            nodeMap.put("sycx", t.getSycx());           // 车型简称
            
            // CoHaschild属性为空时，进一步判断是否有子节点
            if (t.getCoHaschild() != null) {
                nodeMap.put("leaf", (t.getCoHaschild() == 0));
            } else {
                nodeMap.put("leaf", !hasChildren(t.getCoID()));
            }
            nodeMap.put("zylxID", t.getZylxID());       // 专业类型主键
            nodeMap.put("zylx", t.getZylx());           // 专业类型名称
            nodeMap.put("fljc", t.getFljc());           // 分类简称
            nodeMap.put("wzqm", t.getWzqm());           // 位置全名
            nodeMap.put("gxwzbm", t.getGxwzbm());       // 位置全名编码
            children.add(nodeMap);
        }
        return children;
    }
    
    /**
     * <li>说明：CoHaschild属性为空时，进一步判断是否有子节点
     * <li>创建人：何东
     * <li>创建日期：2016-5-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param fjdID 父节点id
     * @return 是否有子节点，true:有子节点（父节点），false:没有子节点（叶子节点）
     */
    @SuppressWarnings("unchecked")
    private boolean hasChildren(String fjdID) {
        // 判断父节点ID是否为空
        if (!StringUtils.isNotBlank(fjdID)) {
            return false;
        }
        String hql = "From JcgxBuild Where recordStatus = " + Constants.NO_DELETE + " and fjdID = ?";
        // 查询数据
        List<JcgxBuild> list = (List<JcgxBuild>) this.daoUtils.find(hql, new Object[] { fjdID });
        // 判断是否存在子节点，存在返回true
        return (null != list && list.size() > 0);
    }
    
    /**
     * <li>说明：通过分类编码获取故障现象数据
     * <li>创建人：何东
     * <li>创建日期：2016-5-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param flbm 分类编码
     * @return 取故障现象数据
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getFlbmFault(JcxtflFault entity) {
        List<Map<String, Object>> faultList = new ArrayList<Map<String, Object>>();
        
        String flbm = entity.getFlbm();
        // 判断分类编码，不为空则加入查询条件
        if (StringUtils.isNotBlank(flbm)) {
            String hql = "From JcxtflFault Where flbm = ?";
            
            // 按照故障编码和故障名称进行模糊查询
            if(!StringUtil.isNullOrBlank(entity.getFaultName())){
            	hql += " and (faultId like '%"+ entity.getFaultName() + "%' or faultName like '%"+entity.getFaultName()+"%')";
            }
            
            hql += " order by seqNo " ;
            
            // 查询数据
            List<JcxtflFault> list = (List<JcxtflFault>) this.daoUtils.find(hql, new Object[] { flbm });
            
            Map<String, Object> nodeMap = null;
            for (JcxtflFault fault : list) {
                nodeMap = new HashMap<String, Object>();
                nodeMap.put("seqNo", fault.getSeqNo());     // 顺序号
                nodeMap.put("faultId", fault.getFaultId());     // 故障编号
                nodeMap.put("faultName", fault.getFaultName()); // 故障名称
                nodeMap.put("faultTypeID", fault.getFaultTypeID());     // 故障类别编码
                nodeMap.put("faultTypeName", fault.getFaultTypeName()); // 故障类别名称
                faultList.add(nodeMap);
            }
        }
        return faultList;
    }

    /**
     * <li>说明：根据车型及机车构型,机车组成获取下级树节点列表
     * <li>创建人：张迪
     * <li>创建日期：2016-9-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
      * @param fjdID 父节点ID
     * @param sycx 车型简称
     * @param useType 使用类型
     * @return 分类名称编码树
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getJczcmcBuildTree(JcxtflBuild entity) {
//        String fjdID = entity.getFjdID();
//        String sycx = entity.getSycx();
//        String useType = entity.getUseType();  
//        String flmc = entity.getFlmc();  
////        String pyjc = entity.getPyjc();  
//        String hql = "from JcxtflBuild where  '/' || sycx || '/' Like '%/"+ sycx + "/%' and flbm in(select distinct flbm from JcgxBuild  where recordStatus = " + Constants.NO_DELETE 
//        + " and sycx =? ) "; 
//        if (!StringUtil.isNullOrBlank(flmc)) {   // 判断是否有查询条件                 
//             hql += " and flmc || pyjc like '%" + flmc + "%'";
//             if (!StringUtil.isNullOrBlank(fjdID)) {
//                 hql +=  " and fjdID = '" + fjdID + "'";
//             }            
//        }else {
//            if (!StringUtil.isNullOrBlank(fjdID)) {
//                hql +=  " and fjdID = '" + fjdID + "'";
//            } else{
//                hql +=  " and fjdID = '1'";  // 判断父节点ID是否为空，为空则获取根节点 
//            }
//        }
//        // 判断使用类型，不为空时分别判断是否为对应的类型并加入查询条件
//        if (USETYPE_DZDA.equals(useType)) {
//            hql += " And sfsyDzda = " + USETYPE_STUTAS;
//        } else if (USETYPE_JX.equals(useType)) {
//            hql += " And jxzy = " + USETYPE_STUTAS;
//        } else if (USETYPE_ZB.equals(useType)) {
//            hql += " And sfzbzy = " + USETYPE_STUTAS;
//        }
//        
//        List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
//        // 查询数据
//        List<JcxtflBuild> list = (List<JcxtflBuild>) this.daoUtils.find(hql, new Object[] {sycx});
//        if (null == list || 0 >= list.size()) {
//            return children;
//        }
        String fjdID = entity.getFjdID();
        String sycx = entity.getSycx();
        String flmc = entity.getFlmc();      
        StringBuffer sql = new StringBuffer();
       sql.append(" from t_jcbm_jcxtfl A ,(SELECT C.flbm,C.seq_no From(SELECT T.*,ROW_NUMBER() OVER (PARTITION BY  t.flbm ORDER BY T.Seq_No ASC)RV fROM t_jcbm_jcgx T where t.record_status=0 " 
            + "and sycx ='")
            .append(sycx)
            .append( "' )C  WHERE RV=1 )B   where   A.FLBM = B.FLBM     and  '/' || sycx || '/' Like '%/")
            .append(sycx)
            .append("/%'");
   
        // 判断是否有查询条件
        if (!StringUtil.isNullOrBlank(flmc)) {                   
            sql.append(" and A.flmc || A.pyjc like '%").append(flmc.toLowerCase()).append("%'");
            if (!StringUtil.isNullOrBlank(fjdID)) {
                sql.append(" and  A.Fjd_Id  = '").append(fjdID).append("'");
            }            
        }else {
            if (!StringUtil.isNullOrBlank(fjdID)) {
                sql.append(" and  A.Fjd_Id  = '").append(fjdID).append("'");
            } else{
                sql.append(" and  A.Fjd_Id  = '1'");  // 判断父节点ID是否为空，为空则获取根节点 
            }
        }
        sql.append(" ORDER BY B.Seq_No ASC ");
        List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
        // 查询数据
        StringBuffer totalSql = new StringBuffer(" SELECT COUNT(*) AS ROWCOUNT ").append(sql.toString());
        StringBuffer sb = new StringBuffer(" select  A.CO_ID, A.FJD_ID, A.FLBM, A.FLMC, A.PYJC, A.Co_Haschild,B.SEQ_NO ").append(sql.toString());
        List<JczcBuildBean> list = super.queryPageList(totalSql.toString(), sb.toString(), 0, 1000, false, JczcBuildBean.class).getList();      
        if (null == list || 0 >= list.size()) {
            return children;
        }
        
        Map<String, Object> nodeMap = null;
        for (JczcBuildBean t : list) {
            nodeMap = new HashMap<String, Object>();
            nodeMap.put("id", t.getCoID());             // 节点idx主键
            nodeMap.put("coID", t.getCoID());           // 节点idx主键
            nodeMap.put("flbm", t.getFlbm());           // 分类编码
            nodeMap.put("flmc", t.getFlmc());         // 部件名称
            nodeMap.put("pyjc", t.getPyjc());           // 拼音简称
            nodeMap.put("fjdID", t.getFjdID());         // 父节点
            nodeMap.put("text", t.getFlmc()); // 树节点显示名称
            nodeMap.put("sycx", sycx);           // 车型简称
            // CoHaschild属性为空时，进一步判断是否有子节点
            if (t.getCoHaschild() != null) {
                nodeMap.put("leaf", (t.getCoHaschild() == 0));
            } else {
                nodeMap.put("leaf", !hasChildren(t.getCoID()));
            }
            children.add(nodeMap);
        }
        return children;
    }
    
    /**
     * <li>说明：根据车型及机车构型,机车组成获取下级树节点列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-17
     * <li>修改人： 张迪
     * <li>修改日期：2016-10-27
     * <li>修改内容：
      * @param fjdID 父节点ID
     * @param sycx 车型简称
     * @param useType 使用类型
     * @return 分类名称编码树
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getJczcmcBuildTreeAll(JcxtflBuild entity) {
        String fjdID = entity.getFjdID();
        String sycx = entity.getSycx();
        String flmc = entity.getFlmc();      
        StringBuffer sql = new StringBuffer();
       sql.append(" from t_jcbm_jcxtfl A ,(SELECT C.flbm,C.seq_no From(SELECT T.*,ROW_NUMBER() OVER (PARTITION BY  t.flbm ORDER BY T.Seq_No)RV fROM t_jcbm_jcgx T where t.record_status=0 " 
            + "and sycx ='")
            .append(sycx)
            .append( "' )C  WHERE RV=1 )B   where   A.FLBM = B.FLBM     and  '/' || A.sycx || '/' Like '%/")
            .append(sycx)
            .append("/%'");
   
        // 判断是否有查询条件
        if (!StringUtil.isNullOrBlank(flmc)) {                   
            sql.append(" and A.flmc || A.pyjc like '%").append(flmc.toLowerCase()).append("%'");
            if (!StringUtil.isNullOrBlank(fjdID)) {
                sql.append(" and A.Fjd_Id  = '").append(fjdID).append("'");
            }            
        }else {
            if (!StringUtil.isNullOrBlank(fjdID)) {
                sql.append(" and A.Fjd_Id  = '").append(fjdID).append("'");
            } else{
                sql.append(" and A.Fjd_Id  = '1'");  // 判断父节点ID是否为空，为空则获取根节点 
            }
        }
        sql.append(" ORDER BY B.Seq_No ASC ");
        List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
        // 查询数据
        StringBuffer totalSql = new StringBuffer(" SELECT COUNT(*) AS ROWCOUNT ").append(sql.toString());
        StringBuffer sb = new StringBuffer(" select  A.CO_ID, A.FJD_ID, A.FLBM, A.FLMC, A.PYJC, A.Co_Haschild,B.SEQ_NO ").append(sql.toString());
        List<JczcBuildBean> list = super.queryPageList(totalSql.toString(), sb.toString(), 0, 1000, false, JczcBuildBean.class).getList();
       
        if (null == list || 0 >= list.size()) {
            return children;
        }
        
        Map<String, Object> nodeMap = null;
        for (JczcBuildBean t : list) {
            nodeMap = new HashMap<String, Object>();
            nodeMap.put("id", t.getCoID());             // 节点idx主键
            nodeMap.put("coID", t.getCoID());           // 节点idx主键
            nodeMap.put("flbm", t.getFlbm());           // 分类编码
            nodeMap.put("flmc", t.getFlmc());         // 部件名称
            nodeMap.put("pyjc", t.getPyjc());           // 拼音简称
            nodeMap.put("fjdID", t.getFjdID());         // 父节点
            nodeMap.put("text", t.getFlmc()); // 树节点显示名称
            nodeMap.put("sycx", sycx);           // 车型简称
            // CoHaschild属性为空时，进一步判断是否有子节点
            if (t.getCoHaschild() != null) {
                nodeMap.put("leaf", (t.getCoHaschild() == 0));
                // 如果有子节点，则继续遍历
                if(t.getCoHaschild() != 0){
                    JcxtflBuild entityChild = new JcxtflBuild();
                    entityChild.setFjdID(t.getCoID());
                    entityChild.setSycx(entity.getSycx());
                    entityChild.setUseType(entity.getUseType());  
                    entityChild.setFlmc(entity.getFlmc());  
                    nodeMap.put("childrens", getJczcmcBuildTreeAll(entityChild));
                }
            } else {
                nodeMap.put("leaf", !hasChildren(t.getCoID()));
                if(hasChildren(t.getCoID())){
                    JcxtflBuild entityChild = new JcxtflBuild();
                    entityChild.setFjdID(t.getCoID());
                    entityChild.setSycx(entity.getSycx());
                    entityChild.setUseType(entity.getUseType());  
                    entityChild.setFlmc(entity.getFlmc());  
                    nodeMap.put("childrens", getJczcmcBuildTreeAll(entityChild));
                }
            }
            children.add(nodeMap);
        }
        return children;
    }

    /**
     * <li>说明：获取上级部件分类简称
     * <li>创建人：张迪
     * <li>创建日期：2016-9-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车构型实体
     * @return 上级部件分类简称列表
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getSjbjList(JcgxBuild entity) {
        List<Map<String, Object>> faultList = new ArrayList<Map<String, Object>>();
        // 判断分类编码，不为空则加入查询条件
        if (entity == null) { 
            throw new BusinessException("请先选择机车组成");
         }       
        String hql = "from JcgxBuild  where recordStatus = " + Constants.NO_DELETE +" and coID in (select fjdID from JcgxBuild  where recordStatus = " + Constants.NO_DELETE 
                    + " and sycx = ? and flbm=? )  order by coID, seqNo ";
        // 查询数据
        List<JcgxBuild> list = (List<JcgxBuild>) this.daoUtils.find(hql, new Object[] { entity.getSycx(),entity.getFlbm()});
        
        Map<String, Object> nodeMap = null;
        for (JcgxBuild shbj : list) {
            nodeMap = new HashMap<String, Object>();
            nodeMap.put("coID", shbj.getCoID());     // 上级部件id
            nodeMap.put("flbm", shbj.getFlbm());     // 分类编码
            nodeMap.put("fljc", shbj.getFljc()); // 上级部件名称
            faultList.add(nodeMap);
        }
        return faultList;
    }
    /**
     * <li>说明：获取所选部件分类简称
     * <li>创建人：张迪
     * <li>创建日期：2016-9-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车构型实体
     * @return 部件分类简称列表
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBjwzList(JcgxBuild[] entityList) {
        List<Map<String, Object>> faultList = new ArrayList<Map<String, Object>>();
        String hql = "";
        // 判断分类编码，不为空则加入查询条件
        if (entityList == null || entityList.length <=0) { 
            throw  new BusinessException("请先选择车型及机车组成 ！");
//        }
//         else  if (1 == entityList.length  && StringUtil.isNullOrBlank(entityList[0].getFjdID())) { 
//           hql = "from JcgxBuild  where recordStatus = " + Constants.NO_DELETE +" and sycx = ? and flbm=? order by fjdID, seqNo ";          
         } else{
            String fjdIDStr = "'-1'";
            for(JcgxBuild entity: entityList){
                if(StringUtil.isNullOrBlank(entity.getFjdID()) || JcgxBuild.ROOT_FJD_ID.equals(entity.getFjdID()) ){
                    fjdIDStr += ",'"+ JcgxBuild.ROOT_FJD_ID +"'";
                }
                else {
                    fjdIDStr += ",'"+  entity.getFjdID()+"'";
                }
            }
            hql = "from JcgxBuild  where recordStatus = " + Constants.NO_DELETE +" and fjdID in("
            + fjdIDStr +") and sycx = ? and flbm=? order by fljc, seqNo ";
         }
        // 查询数据
        List<JcgxBuild> list = (List<JcgxBuild>) this.daoUtils.find(hql, new Object[] { entityList[0].getSycx(), entityList[0].getFlbm()});      
        Map<String, Object> nodeMap = null;
        for (JcgxBuild shbj : list) {
            nodeMap = new HashMap<String, Object>();
            nodeMap.put("coID", shbj.getCoID());     // 部件id
            nodeMap.put("flbm", shbj.getFlbm());     // 分类编码
            nodeMap.put("fljc", shbj.getFljc()); // 分类简称名称
            nodeMap.put("wzqm", shbj.getWzqm()); // 构建位置全名
            nodeMap.put("fjdID", shbj.getFjdID()); // 父节点id
            nodeMap.put("gxwzbm", shbj.getGxwzbm()); // 构建位置编码
            faultList.add(nodeMap);
        }
        return faultList;
    }

}
