package com.yunda.jx.pjjx.base.qcitemdefine.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.base.qcitemdefine.entity.QCEmp;
import com.yunda.jx.pjjx.base.qcitemdefine.manager.QCEmpManager;
/**
 * <li>标题: 机车配件检修管理信息系统
 * <li>说明: QCItem控制器
 * <li>创建人： 何涛
 * <li>创建日期： 2014-11-12 下午01:38:09
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class QCEmpAction extends JXBaseAction<QCEmp, QCEmp, QCEmpManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明： 批量保存人员信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-12
	 * <li>修改人: 
	 * <li>修改日期：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void save() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			QCEmp[] objects = (QCEmp[])JSONUtil.read(getRequest(), QCEmp[].class);
			String[] errMsg = this.manager.validateUpdate(objects);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.saveOrUpdate(objects);
				// 返回记录保存成功的实体对象
				map.put("entity", objects);  
				map.put(Constants.SUCCESS, true);
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
     * <li>说明：质量检查人员分页查询（用于解决unix系统下，WN_CONCAT函数不能使用的错误）
     * <li>创建人：何涛
     * <li>创建日期：2015-8-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void queryPageList() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            // 查询实体
            QCEmp objEntity = JSONUtil.read(searchJson, QCEmp.class);
            SearchEntity<QCEmp> searchEntity = new SearchEntity<QCEmp>(objEntity, getStart(), getLimit(), getOrders());
            map = this.manager.queryPageList(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
	
//	/**
//	 * <li>说明： 批量删除人员信息
//	 * <li>创建人：何涛
//	 * <li>创建日期：2014-11-13
//	 * <li>修改人: 
//	 * <li>修改日期：
//	 * 
//	 * @throws JsonMappingException
//	 * @throws IOException
//	 */
//	public void delete() throws JsonMappingException, IOException {
//		Map<String, Object> map = new HashMap<String,Object>();
//		try {
//			String[] errMsg = this.manager.validateDeleteByEmpIds(ids);
//			if (errMsg == null || errMsg.length < 1) {
//				this.manager.logicDeleteByEmpIds(ids);
//				// 返回记录保存成功的实体对象
//				map.put(Constants.SUCCESS, true);
//			} else {
//				map.put(Constants.SUCCESS, false);
//				map.put(Constants.ERRMSG, errMsg);
//			}
//		} catch (Exception e) {
//			ExceptionUtil.process(e, logger, map);
//		} finally {
//			JSONUtil.write(this.getResponse(), map);
//		}		
//	}
	
}