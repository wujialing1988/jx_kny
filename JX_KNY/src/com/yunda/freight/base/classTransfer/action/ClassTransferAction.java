package com.yunda.freight.base.classTransfer.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.freight.base.classTransfer.entity.ClassTransfer;
import com.yunda.freight.base.classTransfer.manager.ClassTransferDetailsManager;
import com.yunda.freight.base.classTransfer.manager.ClassTransferManager;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 班次交接Action
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-04-18 11:35:19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public class ClassTransferAction extends JXBaseAction<ClassTransfer, ClassTransfer, ClassTransferManager> {

    /**  序列  */
    private static final long serialVersionUID = 1L;
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * 交接项详情业务类
     */
    @Resource
    private ClassTransferDetailsManager classTransferDetailsManager ;
    

    /**
     * <li>说明：保存方法
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveClassTransfer() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            ClassTransfer classTransfer = (ClassTransfer)JSONUtil.read(getRequest(), entity.getClass());
            String[] errMsg = this.manager.validateUpdate(classTransfer);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveClassTransfer(classTransfer);
//              返回记录保存成功的实体对象
                map.put("entity", classTransfer);  
                map.put("success", true);
            } else {
                map.put("success", false);
                map.put("errMsg", errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
   
    
    /**
     * <li>说明：查询方法
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void pageClassTransferList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            getRequest().getParameter("idx");
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            ClassTransfer objEntity = (ClassTransfer)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<ClassTransfer> searchEntity = new SearchEntity<ClassTransfer>(objEntity, getStart(), getLimit(), getOrders());
            Page<ClassTransfer> page = this.manager.findPageList(searchEntity);
            for (ClassTransfer entity : page.getList()) {
                entity.setDetails(classTransferDetailsManager.findClassTransferDetailsByTransfer(entity.getIdx()));
            }
            map = page.extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    
}
