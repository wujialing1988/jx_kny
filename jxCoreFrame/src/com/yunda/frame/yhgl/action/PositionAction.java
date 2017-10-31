package com.yunda.frame.yhgl.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmPosition;
import com.yunda.frame.yhgl.manager.PositionManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 岗位action
 * <li>创建人：谭诚
 * <li>创建日期：2013-10-31
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class PositionAction extends JXBaseAction<OmPosition,OmPosition,PositionManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：根据nodeid获取对应的岗位信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void findCurrentPosInfo() throws Exception {
		String positionid = getRequest().getParameter("nodeid");
		if(!StringUtil.isNullOrBlank(positionid)){
			OmPosition a = this.getManager().getModelById(positionid);
			if(a!=null&&a.getDutyid()!=null) 
				a.setDutyname(this.getManager().findDutyInfo(a)); //通过职务ID获取职务名称
			JSONUtil.write(this.getResponse(), a);
		}
	}
	
	/**
	 * <li>说明：根据查询条件，查询匹配的岗位信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-20
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void findPositionList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			map = this.manager.findPosiList(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}

	
	 /**
	 * <li>说明：岗位列表选择控件
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public void findPosiListToWidget() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpServletRequest req = getRequest();
            String posiname = StringUtil.nvlTrim(req.getParameter("posiname"), "");  //查询条件： 岗位名称 
            String empid = StringUtil.nvlTrim(req.getParameter("empid"), "");  //查询条件： 主管人员id
            SearchEntity<OmPosition> searchEntity = new SearchEntity<OmPosition>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findPageList(searchEntity, posiname, Long.valueOf(empid)).extjsStore();
		} catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
	}
	
	/**
	 * <li>说明：获取当前机构/岗位的下级岗位列表
	 * <li>创建人：谭诚
	 * <li>创建日期：2012-12-27
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void posiQuery() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			map = this.manager.findPosiList2(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
	/**
	 * <li>说明：根据查询条件，查询匹配的岗位信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-29
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void findPosiListByEmployee() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			map = this.manager.findPosiListByEmployee(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
	/**
	 * <li>说明：返回已配置/未配置为当前角色的岗位列表
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-17
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void findPositionListByRole() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String isRel = getRequest().getParameter("relevance"); //接收表示， 根据该参数决定查询
			if(StringUtil.isNullOrBlank(isRel)){
				//查询已分配角色的岗位
				map = this.manager.findPositionListByRole(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
			} else {
				//查询未分配角色的岗位
				map = this.manager.findPositionListByRole2(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
}
