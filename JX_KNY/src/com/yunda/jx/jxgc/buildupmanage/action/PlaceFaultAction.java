package com.yunda.jx.jxgc.buildupmanage.action; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.buildupmanage.entity.PlaceFault;
import com.yunda.jx.jxgc.buildupmanage.manager.PlaceFaultManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PlaceFault控制器, 故障现象
 * <li>创建人：程锐
 * <li>创建日期：2013-01-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class PlaceFaultAction extends JXBaseAction<PlaceFault, PlaceFault, PlaceFaultManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     * <li>说明：批量保存故障现象(工位终端同时调用方法)
     * <li>创建人：程锐
     * <li>创建日期：2013-1-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void saveOrUpdateList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();        
        try {
            PlaceFault[] list =
                (PlaceFault[]) JSONUtil.read(getRequest(), PlaceFault[].class); // 获取json数组转换成对象数组
            this.manager.saveOrUpdateList(list);
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：通过组成位置主键查询故障现象
     * <li>创建人：王治龙
     * <li>创建日期：2013-2-6
     * <li>修改人：张凡
     * <li>修改日期：流程任务完工时选择故障现象，因用ComboBox控件，增加了假分页的代码
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */ 
    public void findFault() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String buildUpPlaceIdx = StringUtil.nvlTrim( req.getParameter("buildUpPlaceIdx"), "null" );
            List<PlaceFault>  faultList =   this.manager.findFaultList(buildUpPlaceIdx);
            int size = faultList.size();
            
            if(req.getParameter("start") != null){
            	//start不为空代表从完工界面而来，需要分页
            	int limit = Integer.valueOf(req.getParameter("limit"));
            	int start = Integer.valueOf(req.getParameter("start"));
            	List<PlaceFault> tempList = new ArrayList<PlaceFault>();//假分页截取的数据
            	//之所以要减1是因为要添加一个其它，所以要少一个
            	if(size> limit + start - 1){
            		for(int i=start; i < start + limit -1; i++){
            			tempList.add(faultList.get(i));
            		}
            	}else{
            		for(int i=start; i < size; i++){
            			tempList.add(faultList.get(i));
            		}
            	}
            	faultList = tempList;
            }
            
            PlaceFault other = new PlaceFault();
            other.setFaultId(PlaceFault.OTHERID);
            other.setFaultName("其它");
            faultList.add(faultList.size(), other);
            if(faultList != null){
                map.put("id", "faultId");
                map.put("root", faultList);
                map.put("totalProperty", size + 1);
            }
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    /**
     * 
     * <li>说明：删除位置和组成上的故障现象级联删除其处理方法
     * <li>创建人：程锐
     * <li>创建日期：2013-4-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    @SuppressWarnings("all")
    public void deletePlaceFault() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String[] errMsg = this.manager.validateDelete(ids);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.deletePlaceFault(ids);
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
}