package com.yunda.webservice.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 用于设置一个临时标识，在业务处理方法中进行业务判断处理。业务必须要在两分钟内完成。
 * <li>创建人：张凡
 * <li>创建日期：2014-9-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 * CRIF  2015-05-26  汪东良 非常高    此类与业务相关的类请放到对应业务的包下。
 */
public class StateRecord{

	private static Map<Long, Long> CurrentState = new HashMap<Long, Long>();
	
	private static boolean ThreadIsRuning = false;
	
	/**
	 * <li>说明：检查是否有状态
	 * <li>创建人：张凡
	 * <li>创建日期：2014-9-1
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public static boolean HasValue(){
		return CurrentState.containsKey(Thread.currentThread().getId());
	}
	
	/**
	 * <li>说明：存储状态
	 * <li>创建人：张凡
	 * <li>创建日期：2014-9-1
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public static void Put(){
		CurrentState.put(Thread.currentThread().getId(), System.currentTimeMillis());
		if(ThreadIsRuning == false){
			StartClearThread();
		}
	}
	
	/**
	 * <li>说明：清除状态
	 * <li>创建人：张凡
	 * <li>创建日期：2014-9-1
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public static void Remove(){
		
		CurrentState.remove(Thread.currentThread().getId());
	}
	
	/**
	 * <li>说明：启动清理线程
	 * <li>创建人：张凡
	 * <li>创建日期：2014-9-1
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return void
	 */
	private static void StartClearThread() {
		Thread thread = new Thread(){
			public void run(){
				
				Set<Entry<Long, Long>> set = CurrentState.entrySet();
				List<Long> IdList = new ArrayList<Long>();
				for(Entry<Long, Long> entry : set){
					
					if(System.currentTimeMillis() - entry.getValue() > 120000){
						IdList.add(entry.getKey());
					}
				}
				
				for(int i = IdList.size() - 1; i >= 0; i--){
					CurrentState.remove(IdList.get(i));
				}
				if(CurrentState.size() > 0){
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					run();
				}else{
					ThreadIsRuning = false;
				}
			}
		};
		thread.start();
		ThreadIsRuning = true;
	}
	/*public static void main(String[] args) throws InterruptedException {
		
		StateRecord.Put();
		Thread.sleep(5000);
		if(StateRecord.HasValue()){
			StateRecord.Remove();
		}
		
		StateRecord.Put();
		Thread.sleep(15000);
		if(StateRecord.HasValue()){
			StateRecord.Remove();
		}
	}*/
}
