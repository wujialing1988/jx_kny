package com.yunda.jx.pjwz.partsBase.partstype.action; 

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType;
import com.yunda.jx.pjwz.partsBase.partstype.manager.PartsTypeManager;
import com.yunda.jxpz.utils.CodeRuleUtil;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：程梅
 * <li>创建日期：2015-11-2
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class PartsTypeAction extends JXBaseAction<PartsType, PartsType, PartsTypeManager>{

	private Logger logger = Logger.getLogger(getClass().getName());
	//	用于标示是启动还是作废操作
	private String flag;
	
	/**
	 * /jsp/jx/pjwz/partbase/PartsType.js调用
	 * <li>方法名：updateStatus
	 * <li>@throws Exception
	 * <li>返回类型：void
	 * <li>说明：更新配件型号状态，用于启用和作废
	 * <li>创建人：程梅
	 * <li>创建日期：2012-8-23
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public void updateStatus() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			//entity = JSONUtil.read(getRequest(), entity.getClass());
			this.manager.updateStatus(flag, (Serializable[])ids);
			map.put("success", "true");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	
	
    /**
     * /jsp/jx/js/component/pjwz/PartsAndMatSelect.js调用
     * /jsp/jx/js/component/pjwz/PartsTypeAndQuotaSelect.js调用
     * <li>说明：查询符合条件的规格型号列表信息
     * <li>创建人：程梅
     * <li>创建日期：2012-9-3
     * <li>修改人： 曾雪
     * <li>修改日期：添加零部件编码查询条件
     * <li>修改内容：
     * @throws 抛出异常列表
     */
	public void findpageList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
			String searchJson = StringUtil.nvlTrim( req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT );
			String partsClassIdx = getRequest().getParameter("partsClassIdx");
			String statue = getRequest().getParameter("statue");
			String jcpjbm = getRequest().getParameter("jcpjbm");
			if ("".equals(statue)|| statue==null) {//修改
				statue = PartsType.STATUS_ADD+","+PartsType.STATUS_USE;
			}
			entity = (PartsType)JSONUtil.read(searchJson, entitySearch.getClass());
			if(partsClassIdx!=null){
				entity.setPartsClassIdx(partsClassIdx);
			}
			if(null != jcpjbm && !"".equals(jcpjbm)){
				entity.setJcpjbm(jcpjbm);
			}
			int start = req.getParameter("start") == null ? 0 : Integer.parseInt(req.getParameter("start"));
			int limit = req.getParameter("limit") == null ? Page.PAGE_SIZE : Integer.parseInt(req.getParameter("limit"));			
			SearchEntity<PartsType> searchEntity = new SearchEntity<PartsType>(entity, start, limit, getOrders());
			
			map = this.manager.findPageList(searchEntity,statue).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	/**
	 * <li>说明：查询某个库房中没有设置定额的（配件/材料）规格型号，只能查询的是已经启用的规格型号
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-9-2
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public void stockQuotaList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String searchJson = getRequest().getParameter("entityJson"); //组装查询条件
			String typeTableName = getRequest().getParameter("tableName"); //定义配件or材料的数据对象表名
			String warehouseIDX = getRequest().getParameter("warehouseIDX");
			entity = (PartsType)JSONUtil.read(searchJson, entitySearch.getClass());
			SearchEntity<PartsType> searchEntity = new SearchEntity<PartsType>(entity, getStart(), getLimit(), null);
			map = this.manager.findList(searchEntity, typeTableName, warehouseIDX).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	/**
	 * <li>说明：查询配件定额中配件规格选择列表
	 * <li>创建人：程梅
	 * <li>创建日期：2012-9-3
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public void findListForQuota() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
			String searchJson = StringUtil.nvlTrim( req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT );
			entity = (PartsType)JSONUtil.read(searchJson, entitySearch.getClass());
			SearchEntity<PartsType> searchEntity = new SearchEntity<PartsType>(entity, getStart(), getLimit(), null);
			
			map = this.manager.findListForQuota(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	/**
	 *
	 * <li>说明：查询用料申请中配件规格选择列表
	 * <li>创建人：程梅
	 * <li>创建日期：2012-9-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	public void findListForMaterialApply() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
			String searchJson = StringUtil.nvlTrim( req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT );
			entity = (PartsType)JSONUtil.read(searchJson, entitySearch.getClass());
			SearchEntity<PartsType> searchEntity = new SearchEntity<PartsType>(entity, getStart(), getLimit(), null);
			String materialApplyIDX = req.getParameter("materialApplyIDX");
			map = this.manager.findListForMaterialApply(searchEntity,materialApplyIDX).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
	/**
	 * /jx/js/component/pjwz/PartsTypeAndQuotaSelect.js调用
	 * <li>说明：查询生成了定额信息的配件规格型号信息
	 * <li>创建人：程梅
	 * <li>创建日期：2012-10-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	public void findListInQuota() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
			String searchJson = StringUtil.nvlTrim( req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT );
			entity = (PartsType)JSONUtil.read(searchJson, entitySearch.getClass());
			SearchEntity<PartsType> searchEntity = new SearchEntity<PartsType>(entity, getStart(), getLimit(), getOrders());
			
			map = this.manager.findListInQuota(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	/**
	 * <li>说明：根据规格型号id查询配件规格型号信息、
	 * <li>创建人：程梅
	 * <li>创建日期：2013-5-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	public void getPartsType(){
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			PartsType type = this.manager.getModelById(id);
			//PartsRegister register = partsRegisterManager.getPartsRegisterByAccountID(partsAccountIdx);
			map.put("type", type);  
			//map.put("register", register); 
			map.put("success", "true");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			try {
				JSONUtil.write(this.getResponse(), map);
			} catch (JsonMappingException e) {
				ExceptionUtil.process(e,logger);
			} catch (IOException e) {
				ExceptionUtil.process(e,logger);
			}
		}
	}
	
	/**
	 * /jsp/jx/jxgc/WorkTask/WorkTask_Handler.js调用
	 * <li>方法说明：检查配件是否带序列号 
	 * <li>方法名称：checkHasSeq
	 * <li>@throws JsonMappingException
	 * <li>@throws IOException
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2013-6-21 下午06:15:06
	 * <li>修改人：
	 * <li>修改内容：
	 */
	public void checkHasSeq() throws JsonMappingException, IOException{
	    Map<String, Object> map = new HashMap<String,Object>();
        try {
            PartsType type = this.manager.getModelById(id);
            if(type.getIsHasSeq() == null || type.getIsHasSeq() == 0){
                map.put("code", CodeRuleUtil.getRuleCode("PJWZ_PARTS_NAMELATENO"));
            }
            map.put("hasSeq", type.getIsHasSeq());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
	}
	/**
	 * 
	 * <li>说明：配件规格型号树列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-5-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
    public void tree() throws Exception{
		Map<String, String> params = JSONUtil.read(getRequest().getParameter("searchParams"), Map.class);
        List<HashMap> children = manager.findPartsTypeTreeData(params);
        JSONUtil.write(getResponse(), children);
    }
	
	/***
	 * 
	 * <li>说明：基于不使用缓存的规格型号选择
	 * <li>创建人：王斌
	 * <li>创建日期：2014-1-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public void pageQueryForType() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			QueryCriteria<PartsType> query = new QueryCriteria<PartsType>(getQueryClass(),getWhereList(), getOrderList(), getStart(), getLimit());
			map = this.manager.findPageListForType(query).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	/**
	 * 
	 * <li>说明：根据物料编码查询规格型号信息
	 * <li>创建人：程梅
	 * <li>创建日期：2014-8-8
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	public void getPartsTypeByMatCode() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String matCode = getRequest().getParameter("matCode");  //物料编码
			List list = this.manager.getPartsTypeByMatCode(matCode);
			if(list != null && list.size() > 0){
				map.put("list", list);
	        }else{
	            String str = "没有找到物料编码为【"+matCode+"】的规格型号！";
	            map.put("errMsg", str);
	        }
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	/**
	 * <li>说明：查询作业流程适用配件
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
     * @throws IOException 
     * @throws JsonMappingException 
	 */
	@SuppressWarnings("unchecked")
	public void findPageListForWP() throws JsonMappingException, IOException {
		HttpServletRequest req = getRequest();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 查询实体
			String searchJson = StringUtil.nvlTrim( req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT );
			// 作业流程主键
			String wPIDX = req.getParameter("wPIDX");
			entity = (PartsType)JSONUtil.read(searchJson, entity.getClass());
			SearchEntity<PartsType> searchEntity = new SearchEntity<PartsType>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.findPageListForWP(searchEntity, wPIDX).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
	}
	
	/**
	 * <li>说明：查询作业流程适用配件 - 候选配件
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	@SuppressWarnings("unchecked")
	public void findPageListForWPSelect() throws JsonMappingException, IOException {
		HttpServletRequest req = getRequest();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 查询实体
			String searchJson = StringUtil.nvlTrim( req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT );
			// 作业流程主键
			String wPIDX = req.getParameter("wPIDX");
			entity = (PartsType)JSONUtil.read(searchJson, entity.getClass());
			SearchEntity<PartsType> searchEntity = new SearchEntity<PartsType>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.findPageListForWPSelect(searchEntity, wPIDX).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
	}
	/**
	 * 
	 * <li>说明：查询自修目录配件中检修班组为当前班组的配件规格型号
	 * <li>创建人：程梅
	 * <li>创建日期：2015-1-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	 @SuppressWarnings("unchecked")
	public void findRepairListPartsTypeTree() throws Exception{
			Map<String, String> params = JSONUtil.read(getRequest().getParameter("searchParams"), Map.class);
	        List<HashMap> children = manager.findRepairListPartsType(params);
	        JSONUtil.write(getResponse(), children);
	    }
     /**
      * 
      * <li>说明：获取所有配件规格型号列表【用于手持终端接口】
      * <li>创建人：程梅
      * <li>创建日期：2015-10-27
      * <li>修改人： 
      * <li>修改日期：
      * <li>修改内容：
      * @throws JsonMappingException
      * @throws IOException
      */
     public void findPartsTypeList () throws JsonMappingException, IOException {
         Map<String, Object> map = new HashMap<String,Object>();
         HttpServletRequest req = getRequest();
         try {
             String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
             PartsType type = (PartsType)JSONUtil.read(searchJson, entitySearch.getClass());
             SearchEntity<PartsType> searchEntity = new SearchEntity<PartsType>(type, getStart(), getLimit(), getOrders());
             Page<PartsType> page = this.manager.findPartsTypeList(searchEntity);
             
             map = page.extjsResult();
    
         } catch (Exception e) {
             ExceptionUtil.process(e, logger, map);
         } finally {
             JSONUtil.write(getResponse(), map);
         }   
        }
         
     /**
      * 
      * <li>说明：更新规格型号的零部件编码
      * <li>创建人：曾雪
      * <li>创建日期：2016-7-13
      * <li>修改人： 
      * <li>修改日期：
      * <li>修改内容：
      * @throws Exception
      */
     public void updateJcpjbm() throws Exception{
 		Map<String, Object> map = new HashMap<String,Object>();
 		HttpServletRequest req = getRequest();
 		try {
 			String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
 			PartsType[] entitys = JSONUtil.read(searchJson, PartsType[].class);
 			String Jcpjbm = req.getParameter("Jcpjbm");
 			if (entitys != null && entitys.length > 0) {
        	    manager.updateJcpjbm(Jcpjbm, entitys);
            }
// 			this.manager.updateJcpjbm(Jcpjbm, (Serializable[])ids);
 			map.put("success", "true");
 		} catch (Exception e) {
 			ExceptionUtil.process(e, logger, map);
 		} finally {
 			JSONUtil.write(this.getResponse(), map);
 		}
 	}
}