package com.yunda.jx.jxgc.repairrequirement.manager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import oracle.jdbc.driver.OracleTypes;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.repairrequirement.entity.PartsTypeByBuildSelect;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配件组成型号选择控件业务类
 * <li>创建人：程锐
 * <li>创建日期：2013-5-3
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "partsTypeByBuildSelectManager")
public class PartsTypeByBuildSelectManager  extends JXBaseManager<PartsTypeByBuildSelect, PartsTypeByBuildSelect> {
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 调用存储过程筛选车型下所有安装组成型号
     * <li>说明：获取配件组成型号列表
     * <li>创建人：程锐
     * <li>创建日期：2013-5-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 配件组成型号选择实体包装类
     * @param typeIDX 车型(配件)主键
     * @param type 组成类型 0 机车 1 配件 
     * @return Page<PartsTypeByBuildSelect> 分页对象
     */
    @SuppressWarnings("unchecked")
    public Page<PartsTypeByBuildSelect> buildUpTypeList(SearchEntity<PartsTypeByBuildSelect> searchEntity, String typeIDX, String type){        
        List<PartsTypeByBuildSelect> partsTypeList = findList(searchEntity.getEntity(), searchEntity.getOrders());
        List<PartsTypeByBuildSelect> newPartsTypeList = new LinkedList<PartsTypeByBuildSelect>();
        if(partsTypeList != null && partsTypeList.size() > 0){
            if(!StringUtil.isNullOrBlank(typeIDX) && !StringUtil.isNullOrBlank(type)){
                List list = getBuildUpType(typeIDX, type);//根据车型(配件)、组成类型获取该车型（配件）下的所有可安装组成型号列表
                if(list != null && list.size() > 0){
                    for(PartsTypeByBuildSelect partsType : partsTypeList){                    
                        for(int i = 0 ; i < list.size(); i++){
                            //根据可安装组成型号筛选配件组成型号选择实体列表
                            if(!StringUtil.isNullOrBlank(partsType.getBuildUpTypeIDX()) && partsType.getBuildUpTypeIDX().equals(list.get(i))){
                                newPartsTypeList.add(partsType);
                            }
                        }
                    }
                }
            }
        }
        int start = searchEntity.getStart();
        int limit = searchEntity.getLimit();
        int pageSize = newPartsTypeList.size() > (limit+start)? (limit+start) : newPartsTypeList.size();
        List<PartsTypeByBuildSelect> pageList = new ArrayList<PartsTypeByBuildSelect>();
        for(int i = start; i < pageSize; i++){
            pageList.add(newPartsTypeList.get(i));
        }
        return new Page<PartsTypeByBuildSelect>(newPartsTypeList.size(), pageList);
    }
    /**
     * 
     * <li>说明：根据车型(配件)、组成类型获取该车型（配件）下的所有可安装组成型号列表
     * <li>创建人：程锐
     * <li>创建日期：2013-5-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param typeIDX 车型(配件)主键
     * @param type 组成类型 0 机车 1 配件
     * @return 所有可安装组成型号列表
     */
    @SuppressWarnings("unchecked")
    public List getBuildUpType(String typeIDX, String type){
        String proc = "pkg_jxgc_get_buildtype.sp_get_all_buildtype";
        List list = executeSqlProc(proc, typeIDX, type);
        return list;
    }
    /**
     * 
     * <li>说明：根据车型(配件)、组成类型调用存储过程返回该车型（配件）下的所有可安装组成型号列表
     * <li>创建人：程锐
     * <li>创建日期：2013-5-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param procNme 存储过程名称
     * @param typeIDX 车型(配件)主键
     * @param type 组成类型 0 机车 1 配件
     * @return 所有可安装组成型号列表
     */
    @SuppressWarnings("all")
    public List executeSqlProc(final String procNme, final String typeIDX, final String type){ 
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (List)template.execute(new HibernateCallback(){
            public List doInHibernate(Session s) {
                Connection conn = s.connection();
                ResultSet rs = null;
                CallableStatement stmt = null;
                try {
                    stmt = conn.prepareCall("{call " + procNme + "(?, ?, ?)}"); 
                    stmt.setString(1, typeIDX);
                    stmt.setString(2, type);
                    stmt.registerOutParameter(3, OracleTypes.CURSOR);
                    stmt.execute();
                    rs = (ResultSet)stmt.getObject(3);
                    List list = new ArrayList();
                    while (rs.next()) {
                        list.add(rs.getString(1));
                    }
                    return list;
                } catch (SQLException e) {
                	ExceptionUtil.process(e,logger);
                    return null;
                }
                finally {
                    try {
                        rs.close();
                        stmt.close();
                        conn.close();
                    } catch (SQLException e) {
                    	ExceptionUtil.process(e,logger);
                    }
                    
                }
                /*if(!StringUtil.isNullOrBlank(procNme)){
                    try{
                        String sql = "";                        
                        sql = "{Call "+procNme+"(<?,?,?>)}";
                        SQLQuery query = s.createSQLQuery(sql);                       
                        
                        query.setString(0, typeIDX);
                        query.setString(1, type);
                        query.setInteger(2, OracleTypes.CURSOR);
                        
                        return query.list();
                    }catch(Exception e){
                        ExceptionUtil.process(e,logger);
                        return null;
                    }
                }else{
                    return null;
                }*/
            }
        });
    }
    /**
     *  
     * <li>说明：根据组成主键获取配件与组成关联视图实体类
     * <li>创建人：程锐
     * <li>创建日期：2013-5-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param buildUpTypeIDX 配件组成型号
     * @return PartsTypeByBuildSelect 配件与组成关联视图实体类
     */
    public PartsTypeByBuildSelect getEntityByBuildID(String buildUpTypeIDX){
        PartsTypeByBuildSelect partsTypeByBuildSelect = new PartsTypeByBuildSelect();
        partsTypeByBuildSelect.setBuildUpTypeIDX(buildUpTypeIDX);
        List<PartsTypeByBuildSelect> list = findList(partsTypeByBuildSelect);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }
    /**
     * 
     * <li>说明：根据组成型号获取该组成是否虚拟配件 true 是，false 否
     * <li>创建人：程锐
     * <li>创建日期：2013-6-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param buildUpTypeIDX 组成型号主键
     * @return 是否虚拟配件 true 是，false 否
     */
    public boolean checkVirtual(String buildUpTypeIDX){
        if(!StringUtil.isNullOrBlank(buildUpTypeIDX)){
            PartsTypeByBuildSelect partsTypeByBuildSelect = getEntityByBuildID(buildUpTypeIDX);
            if(partsTypeByBuildSelect != null){
                if(partsTypeByBuildSelect.getIsVirtual().equals("是")) {
                    return true;                    
                }else{
                    return false;
                }
                
            }
        }
        return true;
    }
}
