package com.yunda.jx.pjjx.partsrdp.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdpBean;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdp控制器, 配件检修作业
 * <li>创建人：程梅
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class PartsRdpAction extends JXBaseAction<PartsRdp, PartsRdp, PartsRdpManager>{
	private static final String TRUE_STR = "true";
    /** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：查询当前系统操作人员需要检修的配件信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-08
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void partsTree() throws JsonMappingException, IOException {
		List<Map<String, Object>> children = null;
		try {
			children = this.manager.partsTree();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
		} finally {
			JSONUtil.write(getResponse(), children);
		}
		
	}
	
	/**
	 * <li>方法说明：保存兑现单
	 * <li>方法名：newSave
	 * @throws JsonMappingException
	 * @throws IOException
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-20
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public void batchSavePartsRdp() throws JsonMappingException, IOException{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			PartsRdp[] arr = JSONUtil.read(getRequest(), PartsRdp[].class);
			boolean pass = true;
			for(int i = 0; i < arr.length; i++){
				String[] msg = this.manager.validateSave(arr[i].getPartsAccountIDX());
				if(msg != null){
					map.put(Constants.SUCCESS, false);
	                map.put(Constants.ERRMSG, msg);
	                pass = false;
	                break;
				}
			}
			if(pass){
				manager.savePartsRdpMulti(arr);
				map.put(Constants.SUCCESS, TRUE_STR);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	
	/**
	 * <li>说明： 配件检修作业 完工
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void finishPartsRdp() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String,Object>();
		HttpServletRequest req = getRequest();
		try {
			String repairResultDesc = req.getParameter("repairResultDesc");	 // 检修结果描述
			String status = req.getParameter("status");						 // 状态 
			
			String[] errMsg = this.manager.validateFinishPartsRdp(id, status);
			if (null == errMsg) {
				this.manager.finishPartsRdp(id, repairResultDesc, status);
				map.put(Constants.SUCCESS, true);
			} else {
				map.put(Constants.SUCCESS, false);
				map.put(Constants.ERRMSG, errMsg);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
 	}
	/**
	 * 
	 * <li>说明：flag为PartsRdp.STATUS_YZZ---终止任务单【级联终止检修作业节点、检修工艺工单、检修记录卡实例、提票单】
	 * 			flag为PartsRdp.STATUS_JXHG----验收【更新任务单状态和配件状态】
	 * <li>创建人：程梅
	 * <li>创建日期：2014-12-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @throws Exception
	 */
	public void updateStatus() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			//终止或者验收标示
			String flag = getRequest().getParameter("flag");
			String[] errMsg = this.manager.validateUpdate(flag,ids);
            if (errMsg == null || errMsg.length < 1) {  
				this.manager.updateStatus(flag,ids);
				map.put(Constants.SUCCESS, TRUE_STR);
            } else {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, errMsg);
            }
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
	 * <li>说明： 配件检修质量检查 查询
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void findPartRdpQCItems() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String,Object>();
		HttpServletRequest req = getRequest();
		try {
			String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
			// 默认查询必检项
			String checkWay = StringUtil.nvl(req.getParameter("checkWay"), "");
			// 质量检查项过滤条件
			String qcContent = req.getParameter("qcContent");
			entity = JSONUtil.read(entityJson, PartsRdp.class);
			SearchEntity<PartsRdp> searchEntity = new SearchEntity<PartsRdp>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.findPartRdpQCItems(searchEntity, checkWay, qcContent).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
	}
	
	/**
	 * <li>说明：Mobile接口 - 查询当前登录用户负责检修的作业任务信息列表
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void findList () throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			List<?> entityList = this.manager.findList();
			
			map.put("id", EntityUtil.IDX);
			map.put("root", entityList);
			map.put("totalProperty", entityList.size());
			map.put(Constants.SUCCESS, true);
			map.put(Constants.ERRMSG, "");

		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
	}
    
    /**
     * <li>说明：启动生产
     * <li>创建人：程锐
     * <li>创建日期：2015-10-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void startPartsRdp() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String idx = getRequest().getParameter("idx");
            this.manager.updateStartPartsRdp(idx);
            map.put(Constants.SUCCESS, TRUE_STR);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    /**
     * <li>说明：获取配件检修作业信息（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-10-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getModel() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        HttpServletResponse response = getResponse();
        try {
            PartsRdp objEntity = this.manager.getModelById(id);
            map.put(Constants.ENTITY, objEntity);
            // 获取配件检修作业统计信息，包含【检修记录】、【作业工单】、【回修提票】的处理情况
            map.put("statisticInfo", this.manager.getStatisticInfo(id));
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            ObjectMapper om = new ObjectMapper();
            om.getSerializationConfig().setDateFormat(DateUtil.yyyy_MM_dd_HH_mm_ss);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/json");
            om.writeValue(response.getWriter(), map);
        }
    }
    
    /**
     * <li>方法说明： 编辑保存计划
     * <li>方法名：editPartsRdpPlan
     * @throws JsonMappingException
     * @throws IOException
     * <li>创建人： 张凡
     * <li>创建日期：2015-10-22
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public void editPartsRdpPlan() throws JsonMappingException, IOException{
    	Map<String, Object> map = new HashMap<String,Object>();
        try {
            PartsRdp entity = (PartsRdp)JSONUtil.read(getRequest(), entitySearch.getClass());
            manager.updatePartsRdpPlan(entity);
            map.put(SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>方法说明：终止任务单
     * <li>方法名：terminationPlan
     * @throws JsonMappingException
     * @throws IOException
     * <li>创建人： 张凡
     * <li>创建日期：2015-10-23
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public void terminationPlan() throws JsonMappingException, IOException{
    	Map<String, Object> map = new HashMap<String,Object>();
        try {
            String rdpIdx = getRequest().getParameter("rdpIdx");
            manager.updateTerminationPlan(rdpIdx);
            map.put(SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：生成任务单(iPad应用)
     * <li>创建人：何涛
     * <li>创建日期：2015-10-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void saveForPartsRdp() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        HttpServletRequest req = getRequest();
        try {
            PartsRdp rdp = JSONUtil.read(req, PartsRdp.class);
            // 兑现单信息
            PartsRdp partsRdp = this.manager.getEntityByWpIdxAndPaIdx(rdp);
            this.manager.savePartsRdpSingle(partsRdp, SystemContext.getAcOperator());
            map.put(Constants.ENTITY, partsRdp);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据配件拆卸登记情况查询配件检修作业任务树
     * <li>创建人：何涛
     * <li>创建日期：2016-01-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @throws Exception 抛出异常列表
     */
    @SuppressWarnings("unchecked")
    public void tree() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        List<Map<String, Object>> children = null ;
        try {
            String parentIDX = getRequest().getParameter("parentIDX");
            children = manager.tree(parentIDX);
        } catch (RuntimeException e) {
            ExceptionUtil.process(e, logger, map);
        } finally{
            JSONUtil.write(getResponse(), children);
        }
    }
    
    /**
     * <li>方法说明：查询配件检修结果查询List
     * <li>创建人： 林欢
     * <li>创建日期：2016-5-12
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * 
     * @throws JsonMappingException
     * @throws IOException
     */
    public void pagePartsRdpRdpInfoQuery() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String searchJson = StringUtil.nvlTrim(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
            //封装返回数据的对象，非model
            PartsRdpBean entity = JSONUtil.read(searchJson, PartsRdpBean.class);
            SearchEntity<PartsRdpBean> searchEntity = new SearchEntity<PartsRdpBean>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.pagePartsRdpRdpInfoQuery(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：通过配件编号获取配件信息
     * <li>创建人：张迪
     * <li>创建日期：2016-6-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findPartsRdpByPartsNo() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String partsNo = getRequest().getParameter("partsNo"); 
            PartsRdp objEntity = this.manager.findPartsRdpByPartsNo(partsNo);
            map.put(Constants.ENTITY, objEntity);
           
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}