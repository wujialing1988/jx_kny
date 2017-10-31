/**
 * 
 */
package com.yunda.frame.yhgl.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.entity.EosDictEntryId;
import com.yunda.frame.yhgl.manager.SysEosDictEntryManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 系统业务字典项(即明细)控制类
 * <li>创建人：谭诚
 * <li>创建日期：2013-12-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class SysEosDictEntryAction extends JXBaseAction <EosDictEntry, EosDictEntry, SysEosDictEntryManager> {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * 
	 * <li>说明：获得业务字典项树
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unused")
	public void tree() throws Exception{
		String dicttypeid = getRequest().getParameter("dicttypeid");//业务字典类型id
		String dictid = getRequest().getParameter("dictid");		//当前业务字典项id
		String isAll = getRequest().getParameter("isAll");	// 是否将全部树形查询出来
		if(StringUtil.isNullOrBlank(dictid)||"ROOT_0".equals(dictid)){
			dictid = "ROOT_0";
		}
		List<Map> childNodeList = this.manager.getChildNodes(dicttypeid, dictid,isAll);
		JSONUtil.write(getResponse(), childNodeList);
	}

	/**
	 * <li>说明：单表分页查询，返回单表分页查询记录的json
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-13
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void pageQuery() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			map = this.manager.findEosDictEntryList(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}		
	
	/**
	 * <li>说明：接受物理删除记录请求，向客户端返回操作结果（JSON格式）
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-14
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("all")
	public void delete() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		String dicttypeid = getRequest().getParameter("dicttypeid");
		try {
			String[] errMsg = this.manager.validateDelete(ids);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.deleteByIds(dicttypeid,ids);
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
	 * <li>说明： 调用检查函数检查是否业务字典项ID已存在
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	public void validateEntryID() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String dicttypeid = getRequest().getParameter("dicttypeid");
			String dictid = getRequest().getParameter("dictid");
			if(StringUtil.isNullOrBlank(dicttypeid)||StringUtil.isNullOrBlank(dictid)) return;
            //修改 2015-02-02 汪东良 将id 局部变量修改成obj开头，保证与属性变量命名不同。
            //将参数构建为实体对象
			EosDictEntryId objId = new EosDictEntryId();
			EosDictEntry entry = new EosDictEntry();
            objId.setDictid(dictid);
            objId.setDicttypeid(dicttypeid);
			entry.setId(objId);
			//调用验证方法
			String[] errMsg = this.manager.validateUpdate(entry);
			if (!(errMsg == null || errMsg.length < 1)) {
				map.put("errMsg", errMsg);
			} else {
				map.put("success", false);
			}
		} catch (Exception e){
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
	/**
	 * <li>说明： 重写父类方法， 去掉验证方法的调用，对于字典项ID的验证，交给页面js调用处理
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-14
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void saveOrUpdate() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			EosDictEntry t = (EosDictEntry)JSONUtil.read(getRequest(), entity.getClass());
			String[] errMsg = null;//this.manager.validateUpdate(t);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.saveOrUpdate(t);
				//返回记录保存成功的实体对象
				map.put("entity", t);  
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
