package com.yunda.jx.base.workInstructor.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.base.workInstructor.entity.WorkInstructor;
import com.yunda.jx.base.workInstructor.entity.WorkInstructorBean;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 作业指导书业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-05-15 14:10:03
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings({"unchecked", "unused"})
@Service("workInstructorManager")
public class WorkInstructorManager extends JXBaseManager<WorkInstructor, WorkInstructor> {
    
    /**
     * 调用URL
     */
    private static String BASE_URL = "" ;
    
    static{
        Properties p = new Properties();
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream("webservice.properties");
            p.load(is);
            is.close();
        } catch (Exception e) {
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        BASE_URL = p.getProperty("instructor_url");
    }    
    

    /**
     * <li>说明：查询作业指导书
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return
     */
    public WorkInstructorBean findWorkInstructors() {
        WorkInstructorBean bean = new WorkInstructorBean();
        StringBuffer hql  = new StringBuffer(" From WorkInstructor where 1=1 order by seqNo ASC");
        List<WorkInstructor> workInstructors = this.daoUtils.find(hql.toString());
        bean.setWorkInstructors(workInstructors);
        bean.setInstructorUrl(BASE_URL);
        return bean;
    }
    
   
}
