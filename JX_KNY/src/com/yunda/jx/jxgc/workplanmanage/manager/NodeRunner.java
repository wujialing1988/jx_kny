package com.yunda.jx.jxgc.workplanmanage.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.yhgl.entity.AcOperator;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 完成流程节点业务线程调度类
 * <li>创建人：程锐
 * <li>创建日期：2015-5-13
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
public class NodeRunner extends Thread {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * 需要检查完成工序的主键
     */
    private static Map<String, String> tecProcessNodeCases = new HashMap<String, String>();
    /**
     * 待检查列表
     */
    private static List<String> checks = new ArrayList<String>();
    /**
     * 转移中临时表
     */
    private static Map<String, String> temp = new HashMap<String, String>();
    /**
     * 标示线程是否在运行中
     */
    private static boolean threadRuning = false;
    /**
     * 是否正在复制检查工序
     */
    private static boolean copying = false;
    /**
     * 待检查的工序主键数量
     */
    private static int count = 0;
    /**
     * 当前检查次数
     */
    private static int scanCount = 0;
    /**
     * 参与者
     */
    private AcOperator acOperator;
    
    /**
     * <li>说明：私有构造方法
     * <li>创建人：程锐
     * <li>创建日期：2015-5-13
     * <li>修改人：
     * <li>修改日期：
     * @param acOperator 当前操作者
     */
    private NodeRunner(AcOperator acOperator){
        this.acOperator = acOperator;
    }
    /**
     * 线程运行方法
     */
    public void run(){
        
        if(threadRuning == false)
            threadRuning = true;
        
        if(tecProcessNodeCases.size() != count){
            /*
             * 如果MAP中的数量与上次检查的数量不一致，则表明增加了要检查的工序
             * 等待十秒钟后再判断一次
             */
            count = tecProcessNodeCases.size();
            
            cout("数量改变轮询");            
            scanCount = 0;  //数量改变扫描次数清0
            mywait();
            run();
        }else if(++scanCount >= 2){ //2 次未变数量
            /*
             * 必须有待检查的工序主键
             */
            if(count > 0){
                cout("开始排队等待执行");
                toWaitCheckList();
            }else{
                checkEnding();  //标识检查结束
            }
        }else{
            /*
             * 等待
             */
            cout("轮询等待");
            mywait();
            run();
        }
    }
    
    /**
     * <li>方法说明：标识检查结束 
     * <li>方法名称：checkEnding
     * <li>
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2014-2-12 下午07:19:16
     * <li>修改人：
     * <li>修改内容：
     */
    private void checkEnding() {
        scanCount = 0;
        threadRuning = false;
        cout("线程结束");
    }

    /**
     * <li>方法说明：线程等待方法 
     * <li>方法名称：mywait
     * <li>
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2014-2-12 下午06:50:22
     * <li>修改人：
     * <li>修改内容：
     */
    private void mywait() {
        try {
            Thread.sleep(1000 * 5);    //等待五秒钟再检查
        } catch (InterruptedException e) {            
            ExceptionUtil.process(e,logger);
        }
    }
    
    /**
     * <li>方法说明：添加待检查工序完成的工序主键 
     * <li>方法名称：push
     * <li>@param tecNodeCaseIdx
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2014-2-12 下午06:46:29
     * <li>修改人：
     * <li>修改内容：
     */
    private static synchronized void push(String tecNodeCaseIdx){
        if(copying){
            temp.put(tecNodeCaseIdx, "");
        }else{
            tecProcessNodeCases.put(tecNodeCaseIdx, "");
        }
    }
    
    /**
     * <li>方法说明：将主键移到待检查列表中
     * <li>方法名称：toWaitCheckList
     * <li>
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2014-2-12 下午07:00:03
     * <li>修改人：
     * <li>修改内容：
     */
    private void toWaitCheckList(){
        
        copying = true;
        cout("准备复制");
        mywait();
        Set<String> keys = tecProcessNodeCases.keySet();
        for(String key : keys){
            checks.add(key);
        }
        startCheckTecNodeCase();
        copying = false;
        cout("复制结束");
        copyingTemp();
    }
    
    /**
     * <li>方法说明：启动线程过工序 
     * <li>方法名称：startCheckTecNodeCase
     * <li>
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2014-2-12 下午07:06:26
     * <li>修改人：
     * <li>修改内容：
     */
    private void startCheckTecNodeCase(){
        if(checks.size() <= 0){
            return;
        } 
        NodeBizRunner biz = new NodeBizRunner(checks, this.acOperator);      
        biz.start();
        checks.clear();
        tecProcessNodeCases.clear();
    }
    
    /**
     * <li>方法说明：复制临时主键 
     * <li>方法名称：copyingTemp
     * <li>
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2014-2-12 下午07:12:42
     * <li>修改人：
     * <li>修改内容：
     */
    private void copyingTemp(){
        scanCount = 0;
        if(temp.size() > 0){
            Set<String> keys = temp.keySet();
            for(String key : keys){
                push(key);
            }
            temp.clear();
        }
        cout("复制临时主键完毕");
        run();
    }
    /**
     * <li>方法说明：执行单线程处理过工序 
     * <li>方法名称：runner
     * <li>@param tecNodeCaseIdx
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2014-2-12 下午06:46:54
     * <li>修改人：
     * <li>修改内容：
     */
    public static void runner(String tecNodeCaseIdx){
        
        push(tecNodeCaseIdx);
        if(threadRuning == false){
            
            NodeRunner runner = new NodeRunner(SystemContext.getAcOperator());
            cout("线程启动");
            runner.start();
        }else{
            cout("线程在运行中");
        }
    }
    
    /**
     * <li>说明：日志记录线程运行状态
     * <li>创建人：程锐
     * <li>创建日期：2015-5-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param text 线程运行情况
     */
    private static void cout(String text){
        Logger.getLogger(NodeRunner.class).info(text);
    }
}
