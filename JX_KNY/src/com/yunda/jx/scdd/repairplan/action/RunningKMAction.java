package com.yunda.jx.scdd.repairplan.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.FileUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.scdd.repairplan.entity.RunningKM;
import com.yunda.jx.scdd.repairplan.entity.RunningKMHistory;
import com.yunda.jx.scdd.repairplan.manager.RunningKMHistoryManager;
import com.yunda.jx.scdd.repairplan.manager.RunningKMManager;
import com.yunda.jx.util.Fastjson;

/**
 * <li>说明：走行公里
 * <li>创建人： 张凡
 * <li>创建日期：2015年11月4日
 * <li>成都运达科技股份有限公司
 */
public class RunningKMAction extends
		JXBaseAction<RunningKM, RunningKM, RunningKMManager> {

	Logger logger = Logger.getLogger(this.getClass());
	
	private File importFile;
	
	/**
	 * 上传文件是否在导入中
	 */
	private static boolean importing = false;
	
	
	@Resource(name="runningKMHistoryManager")
	private RunningKMHistoryManager historyManager;
	
	/**
	 * <li>方法说明：上传文件
	 * <li>方法名：uploadDataFile
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-29
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 */
	public void uploadDataFile() throws IOException{
		if(importing){
			throw new RuntimeException("正在执行导入处理，请稍后再试！");
		}
		importing = true;
		File xls = new File(importFile.getPath() + ".xls");
		InputStream is = null;
		try{
			FileUtil.copyFile(importFile, xls);
			is = new FileInputStream(xls);
			manager.handleImportData(is);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
			getResponse().setStatus(500);
		} finally {
			if(is != null)
				is.close();
			if(xls != null)
				xls.delete();
			importing = false;
		}
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * <li>方法说明： 根据机车查询走行公里
	 * <li>方法名：findRunningKM
	 * @throws IOException
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-31
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public void findRunningKM() throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			String trainTypeIdx = getRequest().getParameter("trainTypeIdx");
			String trainNo = getRequest().getParameter("trainNo");
			map.put("entity", manager.findByTrain(trainTypeIdx, trainNo));
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}finally{
			Fastjson.writer(getResponse(), map);
		}
	}
	
	/**
	 * <li>方法说明：修后清零
	 * <li>方法名：clear
	 * @throws IOException
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-31
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public void clear() throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			String idx = getRequest().getParameter("idx");
			String index = getRequest().getParameter("index");
			int result = this.manager.updateClear(idx, Integer.parseInt(index));
			if(result == 1){
				map.put("errMsg", "数据未做任何变更！");
			}else{
				map.put("success", true);
				map.put("result", result);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}finally{
			Fastjson.writer(getResponse(), map);
		}
	}

	/**
	 * <li>方法说明： 撤销
	 * <li>方法名：undo
	 * @throws IOException
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-31
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public void undo() throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			String idx = getRequest().getParameter("idx");
			int result = this.manager.updateUndo(idx);
			if(result == 0){
				map.put("errMsg", "已无历史记录！");
			}else{
				map.put("success", true);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}finally{
			Fastjson.writer(getResponse(), map);
		}
	}
	
	/**
	 * <li>方法说明：重新计算下次修程
	 * <li>方法名：reCompute
	 * @throws IOException
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-31
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public void reCompute() throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			String repairType = getRequest().getParameter("repairType");
			this.manager.updateComputeNextRepair(repairType);
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
			e.printStackTrace();
		}finally{
			Fastjson.writer(getResponse(), map);
		}
	}
	
	public File getImportFile() {
		return importFile;
	}

	public void setImportFile(File importFile) {
		this.importFile = importFile;
	}
	
	
	/**
	 * <li>方法说明： 走行历史pageQuery
	 * <li>方法名：findHistoryPageQuery
	 * @throws JsonMappingException
	 * @throws IOException
	 * <li>创建人： 张凡
	 * <li>创建日期：2015年11月23日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public void findHistoryPageQuery() throws JsonMappingException, IOException{
	    Map<String, Object> map = new HashMap<String,Object>();
        try {
            QueryCriteria<RunningKMHistory> query = new QueryCriteria<RunningKMHistory>(RunningKMHistory.class,
                getWhereList(), getOrderList(), getStart(), getLimit());
            map = this.historyManager.findPageList(query).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
	}
    
    /**
     * <li>说明：设置走行公里
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void setRunningKm() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errMsg = "";
        try {
            String[] ids = (String[]) JSONUtil.read(getRequest().getParameter("ids"), String[].class);
            String newRunningKm = getRequest().getParameter("newRunningKm");
            errMsg = this.manager.setRunningKm(ids,newRunningKm);
            if (errMsg == null)
                map.put(Constants.SUCCESS, true);
            else {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map, errMsg);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：同步车辆信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void synchronizeData() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errMsg = "";
        try {
            errMsg = this.manager.synchronizeData();
            if (errMsg == null)
                map.put(Constants.SUCCESS, true);
            else {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map, errMsg);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    
}
