package com.yunda.frame.yhgl.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.yhgl.entity.AcPersonalDesk;
import com.yunda.frame.yhgl.entity.AcPersonalDeskLayout;
import com.yunda.frame.yhgl.manager.AcPersonalDeskLayoutManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 自定义桌面Action
 * <li>创建人：林欢
 * <li>创建日期：2013-11-11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class AcPersonalDeskLayoutAction extends JXBaseAction<AcPersonalDeskLayout,AcPersonalDeskLayout,AcPersonalDeskLayoutManager> {
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：通过当前登录人员ID查询自定义桌面布局信息
     * <li>创建人：林欢
     * <li>创建日期：2016-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void findAcPersonalDeskLayout() throws Exception{
        
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = this.manager.findAcPersonalDeskLayoutMap();
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：通过当前登录人员ID查询自定义桌面信息
     * <li>创建人：林欢
     * <li>创建日期：2016-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */ 
    @SuppressWarnings("unchecked")
    public void findAcPersonalDeskList() throws Exception{
        List<AcPersonalDesk> list = new ArrayList<AcPersonalDesk>();
        List<AcPersonalDesk> listReturn = new ArrayList<AcPersonalDesk>();
//      获取当前操作员ID
        Long operatorid =SystemContext.getAcOperator().getOperatorid();
        try {
            list = this.manager.findAcPersonalDeskList(Long.valueOf(operatorid));
            
            //权限同步过滤
            listReturn = updateAcPersonalDeskListByRoleFunc(list, operatorid);
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
        } finally {
            JSONUtil.write(this.getResponse(), Page.extjsStore(listReturn));
        }   
    }
    
    /**
     * <li>说明：保存用户自定义桌面信息
     * <li>创建人：林欢
     * <li>创建日期：2016-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */ 
    @SuppressWarnings("unchecked")
    public void saveAcPersonalDeskInfo() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        
        //获取数据
        //传递过来的列数
        String columNum = getRequest().getParameter("columNum");
        //传递过来的editGrid数据
        AcPersonalDesk[] acPersonalDeskArry = JSONUtil.read(getRequest(), AcPersonalDesk[].class);
        try {
            map = this.manager.saveAcPersonalDeskInfo(columNum,acPersonalDeskArry);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }     
    }
    
    /**
     * <li>说明：通过写死的应用代码和当前登录人，查询权限下当前用户应用功能树
     * <li>创建人：林欢
     * <li>创建日期：2016-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */ 
    @SuppressWarnings("unchecked")
    public void getRoleFuncByAppCode() throws Exception {
        
        //通过系统方式获取当前的操作人员ID
        Long operatorid =SystemContext.getAcOperator().getOperatorid();
        
        List<HashMap<String, Object>> tree = null;
        try {
            tree = manager.getRoleFuncByAppCode(operatorid);
        } catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
        } finally {
            JSONUtil.write(getResponse(), tree);
        } 
    }
    
    /**
     * <li>说明：通过传入数据库中保存的AcPersonalDesk list过滤无权查看的应用，同时同步数据库信息
     * <li>创建人：林欢
     * <li>创建日期：2016-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param list 数据库List<AcPersonalDesk>
     * @param operatorid 操作员
     * @return List<AcPersonalDesk> 匹配权限过后的List<AcPersonalDesk>
     * @throws Exception
     */ 
    @SuppressWarnings("unchecked")
    private List<AcPersonalDesk> updateAcPersonalDeskListByRoleFunc(List<AcPersonalDesk> list,Long operatorid) throws Exception {
        List<AcPersonalDesk> returnList = manager.updateAcPersonalDeskListByRoleFunc(list,operatorid);
        return returnList;
    }
}
