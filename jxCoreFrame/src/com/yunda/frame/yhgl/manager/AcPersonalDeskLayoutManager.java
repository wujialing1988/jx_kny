package com.yunda.frame.yhgl.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.AcFunction;
import com.yunda.frame.yhgl.entity.AcPersonalDesk;
import com.yunda.frame.yhgl.entity.AcPersonalDeskLayout;

/**
 * <li>标题: 自定义桌面布局Manger
 * <li>说明: 业务类
 * <li>创建人：林欢
 * <li>创建日期：2016-4-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
@Service(value = "acPersonalDeskLayoutManager")
public class AcPersonalDeskLayoutManager extends JXBaseManager<AcPersonalDeskLayout, AcPersonalDeskLayout> {
    
    public static final String PERSONALDK= "PersonalDK";
    public static final String ID = "id";
    public static final String TEXT = "text";
    public static final String LEAF = "leaf";
    
    /** 桌面布局查询接口 */
    @Resource(name="acPersonalDeskManager")
    private AcPersonalDeskManager acPersonalDeskManager;
    
    /** 功能定义表 */
    @Resource(name="sysFunctionManager")
    private SysFunctionManager sysFunctionManager;
    
    /**
     * 角色查询功能接口
     */
    @Resource(name="acRoleManager")
    private AcRoleManager acRoleManager;

    /**
     * <li>说明：通过当前登录人员ID查询自定义桌面布局信息
     * <li>创建人：林欢
     * <li>创建日期：2016-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 登陆人ID
     * @return AcPersonalDeskLayout 自定义桌面信息
     * @throws Exception
     */ 
    public AcPersonalDeskLayout findAcPersonalDeskLayout(Long operatorid) {
        String hql = " from AcPersonalDeskLayout where operatorid = ? ";
        AcPersonalDeskLayout acPersonalDeskLayout = (AcPersonalDeskLayout) daoUtils.findSingle(hql, new Object[]{operatorid}); 
        return acPersonalDeskLayout;
    }
    
    /**
     * <li>说明：通过当前登录人员ID查询自定义桌面信息
     * <li>创建人：林欢
     * <li>创建日期：2016-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 登陆人ID
     * @return List<AcPersonalDesk> 自定义桌面信息
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     * @throws Exception
     */ 
    public List<AcPersonalDesk> findAcPersonalDeskList(Long operatorid) throws BusinessException, NoSuchFieldException {
        List<AcPersonalDesk> list = acPersonalDeskManager.findAcPersonalDeskListByoperatorID(operatorid);
        
        for (AcPersonalDesk desk : list) {
            AcFunction acFunction = sysFunctionManager.getModelById(desk.getFunccode());
            desk.setFuncName(acFunction.getFuncname());
        }
        
        return list;
    }
    
    /**
     * <li>说明：保存当前登录人员自定义桌面信息
     * <li>创建人：林欢
     * <li>创建日期：2016-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param acPersonalDesk 自定义桌面信息
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     * @throws Exception
     */ 
    public void saveAcPersonalDesk(final AcPersonalDesk acPersonalDesk) throws BusinessException, NoSuchFieldException {
        daoUtils.getHibernateTemplate().execute(new HibernateCallback(){
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                session.save(acPersonalDesk);
                return null;
            }
        });
    }
    
    /**
     * <li>说明：删除当前登录人员数据库中自定义桌面信息
     * <li>创建人：林欢
     * <li>创建日期：2016-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param acPersonalDesk 自定义桌面信息
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     * @throws Exception
     */ 
    public void deleteAcPersonalDesk(final AcPersonalDesk acPersonalDesk) throws BusinessException, NoSuchFieldException {
        
        daoUtils.getHibernateTemplate().execute(new HibernateCallback(){
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                session.delete(acPersonalDesk);
                return null;
            }
        });
    }

    /**
     * <li>说明：通过写死的应用代码和当前登录人，查询权限下当前用户应用功能树
     * <li>创建人：林欢
     * <li>创建日期：2016-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 登陆人ID
     * @return List<AcPersonalDesk> 自定义桌面信息
     * @throws Exception
     */ 
    public List<HashMap<String, Object>> getRoleFuncByAppCode(Long operatorid) {
        
        //通过操作人员ID查询对应的权限
        List <Object> roleIdList = acRoleManager.findRoleIdByOperatorId(operatorid);
        //根据权限查询可以看到的应用List
        List<AcFunction> functionList = sysFunctionManager.findByOperatorIdAndAppCode(roleIdList, PERSONALDK);
        
        //构建应用功能树
        List<HashMap<String, Object>> childTree = new ArrayList<HashMap<String, Object>>();
        
        //判断是否有应用功能配置权限
        if (functionList != null && functionList.size() > 0) {
            for (AcFunction t : functionList) {
                HashMap<String, Object> childTreeMap = new HashMap<String, Object>();
                childTreeMap.put(ID, t.getFunccode()); // 节点idx主键
                childTreeMap.put(TEXT, t.getFuncname()); // 树节点显示名称
                childTreeMap.put(LEAF, true); // 是否是叶子节点 false:否；true：是
                childTreeMap.put("funcgroupid", t.getFuncgroupid()); // 功能组编号
                childTreeMap.put("funcdesc", t.getFuncdesc()); // 功能描述
                childTreeMap.put("funcaction", t.getFuncaction()); // 功能调用入口
                childTreeMap.put("parainfo", t.getParainfo()); //输入参数
                childTreeMap.put("functype", t.getFunctype()); //功能类型
                childTreeMap.put("funccode", t.getFunccode()); // 功能编号
                childTreeMap.put("funcname", t.getFuncname()); // 功能名称
                childTree.add(childTreeMap);
            }
        }
        
        //写死根节点
        List<HashMap<String, Object>> functionTree = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> functionTreeMap = new HashMap<String, Object>();
        functionTreeMap.put(ID, PERSONALDK); // 节点idx主键
        functionTreeMap.put(TEXT, PERSONALDK); // 树节点显示名称
        functionTreeMap.put(LEAF, false); // 是否是叶子节点 false:否；true：是
        functionTreeMap.put("children", childTree);
        functionTree.add(functionTreeMap);
        
        return functionTree;
    }

    /**
     * <li>说明：通过当前登录人员ID查询自定义桌面布局信息
     * <li>创建人：林欢
     * <li>创建日期：2016-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作人ID
     * @return Map<String, Object> 封装了columnNum列数，objMapList，panelNum面板个数
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> findAcPersonalDeskLayoutInfo(Long operatorid) throws BusinessException, NoSuchFieldException {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> objMapList = new ArrayList<Map<String,String>>();

        AcPersonalDeskLayout acPersonalDeskLayout = this.findAcPersonalDeskLayout(operatorid);
        map.put("columnNum", Integer.valueOf(acPersonalDeskLayout.getColumNum()));
        
        //通过登录月员获取桌面信息
        List<AcPersonalDesk> acPersonalDeskList = findAcPersonalDeskList(operatorid);
        for (AcPersonalDesk desk : acPersonalDeskList) {
            //通过funccode获取url信息
           AcFunction acFunction = this.sysFunctionManager.getModelById(desk.getFunccode());
           Map<String, String> objMapInfo = new HashMap<String, String>();
           objMapInfo.put("showname", desk.getShowname());
           objMapInfo.put("url", acFunction.getFuncaction());
           objMapList.add(objMapInfo);
        }
        map.put("objMapList", objMapList);
        map.put("panelNum", objMapList.size());
        return map;
    }

    /**
     * <li>说明：通过传入数据库中保存的AcPersonalDesk list过滤无权查看的应用，同时同步数据库信息
     * <li>创建人：林欢
     * <li>创建日期：2016-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param list 数据库List<AcPersonalDesk>
     * @param operatorid 操作员ID
     * @return List<AcPersonalDesk> 匹配权限过后的List<AcPersonalDesk>
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */ 
    public List<AcPersonalDesk> updateAcPersonalDeskListByRoleFunc(List<AcPersonalDesk> list,Long operatorid) throws BusinessException, NoSuchFieldException {
        List<AcPersonalDesk> listRetrun = new ArrayList<AcPersonalDesk>();
//      通过操作人员ID查询对应的权限
        List <Object> roleIdList = acRoleManager.findRoleIdByOperatorId(operatorid);
        //根据权限查询可以看到的应用List
        List<AcFunction> functionList = sysFunctionManager.findByOperatorIdAndAppCode(roleIdList, AcPersonalDeskLayoutManager.PERSONALDK);
        
        Boolean deleteFlag = true;
//      匹配保存的表中的应用中是否有权限
        for (AcPersonalDesk desk : list) {
            AcFunction acFunction = sysFunctionManager.getModelById(desk.getFunccode());
            desk.setFuncName(acFunction.getFuncname());
            
            for (AcFunction function : functionList) {
                if (function.getFunccode().equals(desk.getFunccode())) {
                    listRetrun.add(desk);
                    
                    //匹配成功，不删除
                    deleteFlag = false;
                    break;
                }else {
                    //当前循环下，匹配不成功，计划删除
                    deleteFlag = true;
                }
            }
            
            if (deleteFlag) {
                //如果是true，删除该条数据
                //维护AcPersonalDesk，防止页面数据部同步问题
                this.deleteAcPersonalDesk(desk);
            }
            
        }
        
        return listRetrun;
    }

    /**
     * <li>说明：通过当前登录人员ID查询自定义桌面布局信息
     * <li>创建人：林欢
     * <li>创建日期：2016-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return AcPersonalDeskLayout 自定义桌面信息
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     * @throws Exception
     */
    public Map<String, Object> findAcPersonalDeskLayoutMap() throws BusinessException, NoSuchFieldException {
        Map<String, Object> map = new HashMap<String, Object>();
//      获取当前操作员ID
        Long operatorid =SystemContext.getAcOperator().getOperatorid();
        List<Map<String, String>> objMapList = new ArrayList<Map<String,String>>();

        AcPersonalDeskLayout acPersonalDeskLayout = findAcPersonalDeskLayout(operatorid);
        if (acPersonalDeskLayout != null) {
            map.put("columnNum", Integer.valueOf(acPersonalDeskLayout.getColumNum()));
        }else {
            map.put("columnNum", 1);
        }
        
        //通过登录月员获取桌面信息
//      权限同步过滤
        List<AcPersonalDesk> list = findAcPersonalDeskList(operatorid);
        List<AcPersonalDesk> acPersonalDeskList = null;
        if (list != null && list.size() > 0) {
            acPersonalDeskList = updateAcPersonalDeskListByRoleFunc(list, operatorid);
        }else {
            acPersonalDeskList = new ArrayList<AcPersonalDesk>();
        }
        
        for (AcPersonalDesk desk : acPersonalDeskList) {
            //通过funccode获取url信息
           AcFunction acFunction = this.sysFunctionManager.getModelById(desk.getFunccode());
           Map<String, String> objMapInfo = new HashMap<String, String>();
           objMapInfo.put("showname", desk.getShowname());
           objMapInfo.put("url", acFunction.getFuncaction());
           objMapList.add(objMapInfo);
        }
        map.put("objMapList", objMapList);
        map.put("panelNum", objMapList.size());
        
        map.put("success", true);
        return map;
    }

    /**
     * <li>说明：保存用户自定义桌面信息
     * <li>创建人：林欢
     * <li>创建日期：2016-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param columNum 列数
     * @param acPersonalDeskArry 桌面配置数组
     * @return Map<String, Object> 结果
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     * @throws Exception
     */ 
    public Map<String, Object> saveAcPersonalDeskInfo(String columNum, AcPersonalDesk[] acPersonalDeskArry) throws BusinessException, NoSuchFieldException {
        Map<String, Object> map = new HashMap<String,Object>();
        //获取当前操作员ID
        Long operatorid =SystemContext.getAcOperator().getOperatorid();
//      第一次使用的时候为保持，以后就是更新AcPersonalDeskLayout
        AcPersonalDeskLayout acPersonalDeskLayout = findAcPersonalDeskLayout(operatorid);
        //第一次使用的时候，为空
        if (acPersonalDeskLayout == null) {
            acPersonalDeskLayout = new AcPersonalDeskLayout();
            acPersonalDeskLayout.setColumNum("1");
        }
        acPersonalDeskLayout.setColumNum(columNum);
        acPersonalDeskLayout.setOperatorid(operatorid);
        save(acPersonalDeskLayout);
        
        //保存AcPersonalDesk，先删除以前的，然后保存
        //获取数据库中数据
        //此处无需做权限匹配updateAcPersonalDeskListByRoleFunc，目的是为了清空数据库，保存数据
        List<AcPersonalDesk> list = findAcPersonalDeskList(operatorid);
        
        if (list != null && list.size() > 0) {
            for (AcPersonalDesk desk : list) {
                deleteAcPersonalDesk(desk);
            }
        }
        
        //保存前台传递过来的数据
        for (AcPersonalDesk a : acPersonalDeskArry) {
            a.setOperatorid(operatorid);
            saveAcPersonalDesk(a);
        }
        
        map.put("success", true);
        return map;
    }
}
