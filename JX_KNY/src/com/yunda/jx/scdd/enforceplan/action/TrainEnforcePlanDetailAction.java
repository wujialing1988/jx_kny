package com.yunda.jx.scdd.enforceplan.action; 

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.criterion.Order;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.scdd.enforceplan.entity.PlanDetailSearchView;
import com.yunda.jx.scdd.enforceplan.entity.TrainEnforcePlanDetail;
import com.yunda.jx.scdd.enforceplan.manager.TrainEnforcePlanDetailManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainEnforcePlanDetail控制器, 机车施修计划明细
 * <li>创建人：程锐
 * <li>创建日期：2012-12-06
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class TrainEnforcePlanDetailAction extends JXBaseAction<TrainEnforcePlanDetail, TrainEnforcePlanDetail, TrainEnforcePlanDetailManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：机车月计划明细报表下载
	 * <li>创建人：王利成
	 * <li>创建日期：2014-4-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 */
	public void download() {
		HttpServletResponse rsp = getResponse();
		InputStream in = null;
		try {
			String filename = "TrainEnforcePlanDetail-Template.xls";
			in = this.getClass().getResourceAsStream(
					"/com/yunda/jx/scdd/enforceplan/tmpl/".concat(filename));
			rsp.setContentType("application/x-msdownload");
			rsp.setHeader("Content-Disposition", "attachment;filename="
					+ StringUtil.toISO("机车月度计划明细-导入模版.xls"));
			OutputStream out = rsp.getOutputStream();
			byte[] b = new byte[1024 * 5];
			int len = in.read(b);
			while (len > 0) {
				out.write(b, 0, len);
				len = in.read(b);
			}
		} catch (Exception e) {
			try {
				rsp.getOutputStream().write("文件不存在或已经被删除！".getBytes());
			} catch (IOException ex) {
				ExceptionUtil.process(ex,logger);
			}
			ExceptionUtil.process(e,logger);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					ExceptionUtil.process(e,logger);
				}
				in = null;
			}
		}
	}
	
	/**
	 * <li>说明：机车生产计划明细选择控件 请求方法，目前供配件生产计划使用
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-5-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	public void trainPlanDetailSelect() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			QueryCriteria<TrainEnforcePlanDetail> query = new QueryCriteria<TrainEnforcePlanDetail>(getQueryClass(),getWhereList(), getOrderList(), getStart(), getLimit());
			map = this.manager.findPageList(query).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	/**
	 * <li>说明：重构父类排序方法,接受多Order条件排序
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-5-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return List<Order> 排序规则列表
	 */
	protected List<Order> getOrderListOver(){
		//根据请求参数设置排序规则
		HttpServletRequest req = getRequest();
		String sort = StringUtil.nvlTrim(req.getParameter("sort"), null);
		String dir = StringUtil.nvlTrim(req.getParameter("dir"), null);
		List<Order> orderList = new ArrayList<Order>();
		if (sort != null && dir != null) {
			if ("ASC".equalsIgnoreCase(dir)) {
				String [] sorts = sort.split(",");
				for(int i=0;i<sorts.length;i++)
					orderList.add(Order.asc(sorts[i]));
			} else if("DESC".equalsIgnoreCase(dir)){
				String [] sorts = sort.split(",");
				for(int i=0;i<sorts.length;i++)
					orderList.add(Order.desc(sorts[i]));
			}
		}
		return orderList;
	}	
    
    /**
     * <li>说明：生成调度-生产计划页面分页查询，返回实体类的分页列表对象
     * <li>创建人：谭诚
     * <li>创建日期：2013-05-01
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
	public void searchPlanDetail() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			QueryCriteria<PlanDetailSearchView> query = 
				new QueryCriteria<PlanDetailSearchView>(PlanDetailSearchView.class,getWhereList(), getOrderListOver(), getStart(), getLimit());
			Page<PlanDetailSearchView> page = manager.getDaoUtils().findPageByQC(query);
			map = page.extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
    /**
     * <li>说明：分页查询，返回实体类的分页列表对象
     * <li>创建人：谭诚
     * <li>创建日期：2013-5-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void findPageList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            String startDate = StringUtil.nvlTrim(req.getParameter("startDate"));
            String overDate = StringUtil.nvlTrim(req.getParameter("overDate"));
            TrainEnforcePlanDetail entity = (TrainEnforcePlanDetail)JSONUtil.read(searchJson, entitySearch.getClass());
            List<Order> orderList = getOrderList();
            String orderString = "";
            //特殊处理入厂时间的远程排序，现在排序有bug
            if (orderList != null && orderList.size() > 0) {
                for (Order order : orderList) {
                    if(order.toString().equals("realWarehousingTime asc")){
                        orderString = "order by \"realWarehousingTime\" asc";
                    }else if(order.toString().equals("realWarehousingTime desc")){
                        orderString = "order by \"realWarehousingTime\" desc";
                    }
                }
            }
            SearchEntity<TrainEnforcePlanDetail> searchEntity = new SearchEntity<TrainEnforcePlanDetail>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findPageList(searchEntity, startDate, overDate ,orderString).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
	 * <li>说明：接受保存或更新计划明细记录请求，向客户端返回操作结果（JSON格式），实体类对象必须符合检修系统表设计，主键名必须为idx（字符串uuid）
	 * <li>     新增调用保存方法前,先验证是否在同一计划单下已存在相同车型和车号的计划明细信息, 如果存在则提示用户"该车已存在计划,请勿重复编制!"
	 * <li>     更新调用保存方法前,先验证该数据的状态(planStatus: 10,20,30)是否已经是兑现(20)或者完成(30)状态, 这两种状态不可以修改数据.
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-05-04
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */	
	public void saveOrUpdate() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			TrainEnforcePlanDetail t = (TrainEnforcePlanDetail)JSONUtil.read(getRequest(), entity.getClass());
			String[] errMsg = this.manager.validateUpdate(t);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.saveOrUpdate(t);
//				返回记录保存成功的实体对象
				map.put("entity", t);  
				map.put(Constants.SUCCESS, true);
			} else {
				map.put(Constants.SUCCESS, false);
				map.put("errMsg", errMsg);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
	 * <li>方法说明：批量保存
	 * <li>方法名：batchSave
	 * @throws JsonMappingException
	 * @throws IOException
	 * <li>创建人： 张凡
	 * <li>创建日期：2015年11月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public void batchSave() throws JsonMappingException, IOException{
	    Map<String, Object> map = new HashMap<String,Object>();
        try {
            TrainEnforcePlanDetail[] entitys = (TrainEnforcePlanDetail[])JSONUtil.read(getRequest(), TrainEnforcePlanDetail[].class);
            manager.saveForBatch(entitys);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
	}
	
	/**
	 * <li>说明：接受逻辑删除记录请求，向客户端返回操作结果（JSON格式）
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-05-04
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */	
	@SuppressWarnings("all")
	public void logicDelete() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String[] errMsg = this.manager.validateDelete(ids);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.logicDelete(ids);
				map.put(Constants.SUCCESS, true);
			} else {
				map.put(Constants.SUCCESS, false);
				map.put("errMsg", errMsg);
			}			
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	    
    /**
     * <li>说明：生成作业计划时选择生产计划列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void findPageListForRdp() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            TrainEnforcePlanDetail entity = (TrainEnforcePlanDetail) JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<TrainEnforcePlanDetail> searchEntity = new SearchEntity<TrainEnforcePlanDetail>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findPageListForRdp(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}