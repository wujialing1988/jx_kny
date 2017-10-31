package com.yunda.jx.pjwz.partsBase.exchangeboundary.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjwz.partsBase.exchangeboundary.entity.TrainTypeToParts;
import com.yunda.jx.pjwz.partsBase.exchangeboundary.manager.TrainTypeToPartsManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainTypeToParts控制器, 车型对应规格型号
 * <li>创建人：王治龙
 * <li>创建日期：2013-05-23
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class TrainTypeToPartsAction extends JXBaseAction<TrainTypeToParts, TrainTypeToParts, TrainTypeToPartsManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * 
	 * <li>说明：配件互换配件对应车型下配件互换页面加载时调用
	 * <li>创建人：王斌
	 * <li>创建日期：2014-5-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void findPageQuery() throws Exception{
		Map<String,Object> map=new HashMap<String, Object>();
		try {
			map=this.manager.findPageListForTrain(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}finally{
			JSONUtil.write(getResponse(), map);
		}
	}
	/**
	 * 
	 * <li>说明：配件互换配件维护页面对应修程配件选择窗口加载时调用
	 * <li>创建人：王斌
	 * <li>创建日期：2014-5-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void findPartsTypeForRcClass() throws Exception{
		Map<String,Object> map=new HashMap<String, Object>();
		try {
			map=this.manager.findPartsTypeForRcClass(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}finally{
			JSONUtil.write(getResponse(), map);
		}
	}
	
	 /**
	  * /jsp/jx/pjwz/base/PartsClassNew.js 调用
	  * /jsp/jx/pjwz/base/PartsClassNewSearch.js 调用
	  * <li>说明：配件分类树
	  * <li>创建人：王治龙
	  * <li>创建日期：2013-5-24
	  * <li>修改人： 
	  * <li>修改日期：
	  * <li>修改内容：
	  * @return void
	  * @throws Exception
	  */
	@SuppressWarnings("unchecked")
    public void tree() throws Exception{
	    String id = getRequest().getParameter("parentIDX");  //父节点ID
        List<HashMap> children=manager.findPartsClassTreeData(id);
        JSONUtil.write(getResponse(), children);
    }
	/**
	 * <li>说明：查询承修车型列表
	 * <li>创建人：王治龙
	 * <li>创建日期：2013-5-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return void
	 * @throws Exception
	 */
	
	@SuppressWarnings("unchecked")
    public void getTrains() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		List list = manager.getTrains();
		map.put("list", list);
		JSONUtil.write(getResponse(), map);
	}
	
	/**
	 * /jsp/jx/pjwz/base/PartsClassNew.js 调用
     * <li>说明：选择规格型号批量生成车型对应的规格型号
     * <li>创建人：王治龙
     * <li>创建日期：2013-5-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */ 
    public void saveOrUpdateList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
        	TrainTypeToParts[] objList = (TrainTypeToParts[])JSONUtil.read(getRequest(), TrainTypeToParts[].class);
            String[] errMsg = this.manager.saveOrUpdateList(objList);
            if (errMsg == null || errMsg.length < 1) {
                map.put("success", "true");
            }else{
                map.put("errMsg", errMsg);
            }
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    /**
	 * /jsp/jx/pjwz/searchparts/TrainTypeToPartsWin.js调用
	 * <li>说明：查询规格型号对应的试用车型信息
	 * <li>创建人：程梅
	 * <li>创建日期：2013-5-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void findPageListForPartsTypeSearch() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
			String partsTypeIDX = req.getParameter("partsTypeIDX");  //规格型号id
			map = this.manager.getListForPartsTypeSearch(partsTypeIDX).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
    /**
     * 
     * <li>说明：根据下车信息和规格型号查询车型对应规格型号信息
     * <li>创建人：程梅
     * <li>创建日期：2015-4-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
	public void findListByTrainTypeIDX() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            
            HttpServletRequest req = getRequest();
            String trainTypeIDX = req.getParameter("trainTypeIDX");//车型id
            String partsTypeIDX = req.getParameter("partsTypeIDX");//规格型号id
            String trainNo = req.getParameter("trainNo");//车号
            String repairClassId = req.getParameter("repairClassId");//修程id
            map = this.manager.findListByTrainTypeIDX(trainTypeIDX, partsTypeIDX, trainNo ,repairClassId, getStart(), getLimit(), getOrders()).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);          
        } finally {
            JSONUtil.write(this.getResponse(), map);
        } 
    }
}