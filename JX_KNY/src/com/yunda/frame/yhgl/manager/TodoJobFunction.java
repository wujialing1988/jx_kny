package com.yunda.frame.yhgl.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.baseapp.todojob.ClassUtils;
import com.yunda.frame.baseapp.todojob.IUntreatedJob;
import com.yunda.frame.baseapp.todojob.entity.TodoJob;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明： 待办项Manager
 * <li>创建人：谭诚
 * <li>创建日期：2014-01-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class TodoJobFunction {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	private static TodoJobFunction instance;
	
	private TodoJobFunction(){}
	
	public static TodoJobFunction getInstance(){
		if(instance != null)	return instance;
		instance = new TodoJobFunction();
		return instance;
	}
	
	/**
	 * <li>说明：获取各业务模块的待办事宜，并返回前台页面显示
	 * <li>创建人：谭诚
	 * <li>创建日期：2014-01-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	public List<TodoJob> getToDoListContext() {
		return getToDoListContext(String.valueOf(SystemContext.getAcOperator().getOperatorid()));
	}
	
    /**
     * <li>说明：根据操作者ID获取各业务模块的待办事宜列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorId 操作者ID
     * @return 各业务模块的待办事宜列表
     * @throws BusinessException
     */
	public List<TodoJob> getToDoListContext(String operatorId) {
        if (com.yunda.frame.util.StringUtil.isNullOrBlank(operatorId))
            return null; // 获取操作员id失败，返回
        List<TodoJob> list = new ArrayList<TodoJob>(); // 定义待办事宜列表        
        IUntreatedJob job = null;
        List<Class> classList = ClassUtils.getAllImpClassesByInterface(IUntreatedJob.class); // 调用函数，获取接口所在package下的所有IUntreatedJob接口的实现类
        for (Class c : classList) {
            /** 分别调用IUntreatedJob实现类的实例获取各业务模块下的待办事宜数据，填充至List当中 */
            try {
                job = (IUntreatedJob) c.newInstance();
                TodoJob tj = job.getJob(operatorId); // 获取各业务模块的待办信息
                if (tj != null) {
                    list.add(tj);// 将各业务模块的待办信息装入List
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
    
//    /**
//     * <li>说明：根据操作者ID获取各业务模块的待办事宜列表
//     * <li>创建人：林欢
//     * <li>创建日期：2016-4-20
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param operatorId 操作者ID
//     * @return 各业务模块的待办事宜列表
//     * @throws BusinessException
//     */
//    public List<TodoJob> getZBToDoListContext(String operatorId) {
//        if (com.yunda.frame.util.StringUtil.isNullOrBlank(operatorId))
//            return null; // 获取操作员id失败，返回
//        List<TodoJob> list = new ArrayList<TodoJob>(); // 定义待办事宜列表
//        IZBUntreatedJob job = null;
//        List<Class> classList = ClassUtils.getAllImpClassesByInterface(IZBUntreatedJob.class); // 调用函数，获取接口所在package下的所有IUntreatedJob接口的实现类
//        for (Class c : classList) {
//            /** 分别调用IUntreatedJob实现类的实例获取各业务模块下的待办事宜数据，填充至List当中 */
//            try {
//                job = (IZBUntreatedJob) c.newInstance();
//                TodoJob tj = job.getJob(operatorId); // 获取各业务模块的待办信息
//                if (tj != null) {
//                    list.add(tj);// 将各业务模块的待办信息装入List
//                }
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//        return list;
//    }
    
//    /**
//     * <li>说明：获取各业务模块的待办事宜，并返回前台页面显示
//     * <li>创建人：林欢
//     * <li>创建日期：2016-4-20
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @throws 抛出异常列表
//     */
//    public List<TodoJob> getZBToDoListContext() {
//        return getZBToDoListContext(String.valueOf(SystemContext.getAcOperator().getOperatorid()));
//    }
}
