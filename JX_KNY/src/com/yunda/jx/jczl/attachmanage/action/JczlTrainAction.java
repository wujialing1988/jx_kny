package com.yunda.jx.jczl.attachmanage.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.jx.jczl.attachmanage.entity.JczlTrain;
import com.yunda.jx.jczl.attachmanage.manager.JczlTrainManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：JczlTrain控制器, 机车信息
 * <li>创建人：王治龙
 * <li>创建日期：2012-10-23
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class JczlTrainAction extends JXBaseAction<JczlTrain, JczlTrain, JczlTrainManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	private OmOrganizationManager omOrganizationManager;
	
	
    public OmOrganizationManager getOmOrganizationManager() {
        return omOrganizationManager;
    }
    
    public void setOmOrganizationManager(OmOrganizationManager omOrganizationManager) {
        this.omOrganizationManager = omOrganizationManager;
    }
    /**
	 * 
	 * <li>方法名称：searchPageList
	 * <li>方法说明：查询数据列表
	 * @throws Exception
	 * return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2012-10-25 下午03:53:55
	 * <li>修改人：
	 * <li>修改内容：
	 */
	@SuppressWarnings("unchecked")
	public void searchPageList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
            String flag = StringUtil.nvlTrim(req.getParameter("flag"));
			map = this.manager.findPageDataList(req.getParameter("entityJson"), getStart(), getLimit(), getOrders(),flag).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}	
    /**
     * 
     * <li>说明：根据车型id查询车号组件所需信息
     * <li>创建人：程梅
     * <li>创建日期：2012-10-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：解决输入过滤只能对当前页过滤的问题
     * @return 返回值为空
     * @throws 抛出异常列表
     */
    @SuppressWarnings("unchecked")
    public void pageListForTNCombo() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String orgCode = JXSystemProperties.OVERSEA_ORGCODE;
            OmOrganization org = omOrganizationManager.findOrgForCode(orgCode);
            String queryHql = req.getParameter("queryHql");
            String queryParams = req.getParameter("queryParams");
            String isAll = req.getParameter("isAll");
            String isCx = req.getParameter("isCx");
            String isIn = req.getParameter("isIn");
            String isRemoveRun = req.getParameter("isRemoveRun");
            if("true".equals(isIn)){//查询调入机车
                Map queryParamsMap = new HashMap();
                if (!StringUtil.isNullOrBlank(queryParams)) {
                    queryParamsMap = JSONUtil.read(queryParams, Map.class);
                }
                map = this.manager.findPageDataList("{'trainTypeIDX':'" + queryParamsMap.get("trainTypeIDX") + "'}", getStart(), getLimit(), getOrders(),"moveIn").extjsStore();
            }else{
                Map queryParamsMap = new HashMap();
                if (!StringUtil.isNullOrBlank(queryParams)) {
                    queryParamsMap = JSONUtil.read(queryParams, Map.class);
                }
    //          query参数是获取EXTJS的combox控件捕获的键盘输入文字
                String queryValue = StringUtil.nvlTrim(req.getParameter("query"), "");
                map = this.manager.page(queryValue,queryParamsMap, getStart(), getLimit(), queryHql,isAll,isCx,org.getOrgseq(),isRemoveRun);
            }
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>方法名称：tree
     * <li>方法说明：机车履历查询树
     * @throws Exception
     * return: void
     * <li>创建人：张凡
     * <li>创建时间：2012-12-3 下午05:01:32
     * <li>修改人：
     * <li>修改内容：
     * */
    public void tree() throws Exception{
	    String trainNo = getRequest().getParameter("trainNo");
	    String id = getRequest().getParameter("id");
        List<HashMap<String,Object>> children=manager.findJczlTrainTreeData(trainNo, id);
        JSONUtil.write(getResponse(), children);
    }
	
	/**
	 * <li>方法说明：配属机车查询树结构 
	 * <li>方法名称：jczlTrainSearchTree
	 * <li>
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2013-8-8 下午02:35:33
	 * <li>修改人：
	 * <li>修改内容：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void jczlTrainSearchTree() throws JsonMappingException, IOException{
	    String id = getRequest().getParameter("id");
        List<HashMap<String,Object>> children=manager.findJczlTrainSearchTree(id);
        JSONUtil.write(getResponse(), children);
	}
	
    /**
     * <li>说明：单表分页查询录入了新造试验的机车信息，返回单表分页查询记录的json
     * <li>创建人：王治龙
     * <li>创建日期：2012-11-1
     * <li>修改人：程梅
     * <li>修改日期：2013年3月5日
     * <li>修改内容：过滤已在承修机车里配置过的机车
     * @param 
     * @return void
     * @throws Exception
     */ 
    @SuppressWarnings("unchecked")
    public void pageListTrian() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            String flag = StringUtil.nvlTrim( req.getParameter("flag"), "" );
            String undertakeTrainTypeIDX = req.getParameter("undertakeTrainTypeIDX");   //承修车型主键
            JczlTrain entity = (JczlTrain)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<JczlTrain> searchEntity = new SearchEntity<JczlTrain>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findPageTrainList(searchEntity,flag,undertakeTrainTypeIDX).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }

    /**
     * <li>方法名称：pageTrainStatistics
     * <li>方法说明：机车类型数量统计查询方法 
     * @throws JsonMappingException
     * @throws IOException
     * return: void
     * <li>创建人：张凡
     * <li>创建时间：2012-11-28 下午04:17:49
     * <li>修改人：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    public void pageTrainStatistics() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            JczlTrain entity = (JczlTrain)JSONUtil.read(searchJson, JczlTrain.class);
                map = this.manager.trainStatistics(entity,searchJson, getStart(), getLimit(),getOrders()).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        } 
    }
    
    /**
     * 
     * <li>方法名称：searchTrainList
     * <li>方法说明：查询机车明细列表
     * @throws Exception
     * return: void
     * <li>创建人：张凡
     * <li>创建时间：2012-11-29 上午10:06:33
     * <li>修改人：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    public void searchTrainList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            JczlTrain entity = (JczlTrain)JSONUtil.read(searchJson, JczlTrain.class);
            
            
            map = this.manager.findTrainList(entity,searchJson, getStart(), getLimit(), getOrders()).extjsStore();
            
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    } 
      
    /**
     * <li>说明：机车信息维护列表（天津基地）
     * <li>创建人：程锐
     * <li>创建日期：2013-4-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    public void jczlTrainList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            map = this.manager.jczlTrainList(req.getParameter("entityJson"), getStart(), getLimit(), getOrders()).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
	/**
     * <li>说明：保存机车信息
     * <li>创建人：程锐
     * <li>创建日期：2013-4-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
    */
    public void saveOrUpdateTransfer() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            JczlTrain jczlTrain = (JczlTrain)JSONUtil.read(getRequest(), entity.getClass());
            String[] errMsg = this.manager.validateUpdate(jczlTrain);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveOrUpdateTransfer(jczlTrain);
//              返回记录保存成功的实体对象
                map.put("entity", jczlTrain);  
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
     * 
     * <li>说明：根据车型车号获取机车信息
     * <li>创建人：程锐
     * <li>创建日期：2014-4-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getEntityByTrainTypeAndNo() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
        	String trainTypeIDX = getRequest().getParameter("trainTypeIDX");
        	String trainNo = getRequest().getParameter("trainNo");
        	List<JczlTrain> jczlTrainList =  this.manager.getModelList(trainTypeIDX, trainNo, null);
        	if(jczlTrainList != null && jczlTrainList.size() > 0){
	//          返回记录保存成功的实体对象
	            map.put("entity", jczlTrainList.get(0));
        	}       
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    /**
     * 
     * <li>说明：查询车号列表
     * <li>创建人：程锐
     * <li>创建日期：2014-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public void queryTrainNoByTrainTypeList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String trainTypeIdx = getRequest().getParameter("trainTypeIdx");
            String trainNo = getRequest().getParameter("trainNo");
			map = this.manager.queryTrainNoByTrainTypeList(trainTypeIdx,trainNo, getStart(), getLimit()).extjsResult();
			map.put("id", "trainNo");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
    
    /**
     * <li>说明：根据车型车号查询机车配属信息
     * <li>创建人：何涛
     * <li>创建日期：2015-07-07
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getModel() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String trainTypeIDX = getRequest().getParameter("trainTypeIDX");
            String trainNo = getRequest().getParameter("trainNo");
            JczlTrain entity =  this.manager.getModel(trainTypeIDX, trainNo);
            if(entity != null){
                map.put("entity", entity);
            }       
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * <li>方法说明：根据车型（s）/车号（s）查询机车列表
     * <li>方法名：findTrainByTrain
     * @throws JsonMappingException
     * @throws IOException
     * <li>创建人： 张凡
     * <li>创建日期：2015-11-3
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public void findTrainByTrain() throws JsonMappingException, IOException{
    	Map<String, Object> map = new HashMap<String,Object>();
        try {
        	String trainTypes = getRequest().getParameter("types");
        	String trainNos = getRequest().getParameter("nos");
            map.put("list", manager.findTrainByTrain(trainTypes, trainNos));
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：获取所有机车信息
     * <li>创建人：林欢
     * <li>创建日期：2016-6-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findJczlTrainInfoList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            
            String entityJson = req.getParameter("entityJson");
            
            List<JczlTrain> list = this.manager.findJczlTrainInfoList(entityJson, getStart(), getLimit(), getOrders());
            map = new Page<JczlTrain>(list.size(), list).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：根据配属段获取所有机车信息
     * <li>创建人：林欢
     * <li>创建日期：2016-6-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findJczlTrainInfoPageListByholdOrg() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            //1==本段 2==非本段
            String flag = req.getParameter("flag");//判断前台是查询本段还是非本段
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            JczlTrain objEntity = (JczlTrain)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<JczlTrain> searchEntity = new SearchEntity<JczlTrain>(objEntity, getStart(), getLimit(), getOrders());
            map = this.manager.findJczlTrainInfoPageListByholdOrg(searchEntity,flag).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：车辆信息维护列表
     * <li>创建人：张迪
     * <li>创建日期：2017-4-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public void findjczlTrainList() throws Exception{
    	Map<String, Object> map = new HashMap<String,Object>();
    	try {
    		HttpServletRequest req = getRequest();
    		map = this.manager.findjczlTrainList(req.getParameter("entityJson"), getStart(), getLimit(), getOrders()).extjsStore();
    	} catch (Exception e) {
    		ExceptionUtil.process(e, logger, map);
    	} finally {
    		JSONUtil.write(this.getResponse(), map);
    	}   
    }
   
    /**
     * <li>说明：构造车种下拉树
     * <li>创建人：张迪
     * <li>创建日期：2017-4-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void findVehicleKindTree() throws Exception {
        String vehicleType = getRequest().getParameter("vehicleType");
        List<HashMap<String, Object>>  children = manager.findVehicleKindTree(vehicleType);
        JSONUtil.write(getResponse(), children);
    }
    
}