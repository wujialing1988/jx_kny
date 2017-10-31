package com.yunda.jx.pjwz.partsmanage.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jx.pjwz.partsmanage.manager.PartsManageLogManager;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: PartsAccount控制器，配件周转台账---配件信息
 * <li>创建人：程梅
 * <li>创建日期：2014-5-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class PartsAccountAction extends JXBaseAction<PartsAccount, PartsAccount, PartsAccountManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    
    private String mapList = "list";
    private String partsNoStr = "partsNo";
    
	/**
	 * <li>说明：根据配件编号+库房+配件状态查询配件信息，用于配件领旧、配件上车领用
	 * <li>创建人：程梅
	 * <li>创建日期：2014-5-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
     * @throws Exception
	 */
	public void getPartsAccountByPartsNoAndWH() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
        HttpServletRequest req=this.getRequest();
		try {
			String partsNo = req.getParameter(partsNoStr);  //配件编号
			String whIDX = req.getParameter("whIDX"); //库房主键
			String partsStatus = req.getParameter("partsStatus"); //配件状态
			List list = this.manager.getPartsAccountByPartsNoAndWH(partsNo, whIDX, partsStatus);
			if(list != null && list.size() > 0){
				map.put(mapList, list);
	        }else{
	            String str = "库房中没有找到编号为【"+partsNo+"】的配件！";
                map.put(Constants.ERRMSG, str);
	        }
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
    
	/**
     * <li>说明：根据配件编号查找配件
     * <li>创建人：王斌
     * <li>创建日期：2014-5-19
     * <li>修改人：何涛
     * <li>修改日期：2016-03-11
     * <li>修改内容：重构
	 * @throws IOException 
	 * @throws JsonMappingException
     * //TODO 有以下页面调用了该方法，可能后续需要重构 
     * /JX_YoGa/WebRoot/jsp/jx/pjwz/partscancel/PartsCanceledDetail.js
     * /JX_YoGa/WebRoot/jsp/jx/pjwz/partsscrap/PartsScrapDetailForBill.js
     */
    public void findPartsForNo() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = this.getRequest();
        try {
            String billId = req.getParameter("billId");// 配件编号
            String partsStatus = req.getParameter("status");// 配件状态
            PartsAccount account = this.manager.findPartsForNo(billId, partsStatus);
            if (null != account) {
                map.put(Constants.SUCCESS, true);
                map.put("entity", account);
            } else {
                map.put(Constants.SUCCESS, false);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
	
	/**
	 * <li>说明：根据配件编号查询配件信息，用于修竣配件入库【状态为自修包含的所有配件】
	 * <li>创建人：程梅
	 * <li>创建日期：2014-5-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
     * @throws JsonMappingException
     * @throws IOException
	 */
	public void getPartsAccountByPartsNo() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String partsNo = getRequest().getParameter(partsNoStr);  //配件编号
			String partsStatus = PartsAccount.PARTS_STATUS_ZX ;//自修配件
			List list = this.manager.getPartsAccountByPartsNo(partsNo,partsStatus);
			if(list != null && list.size() > 0){
				map.put(mapList, list);
	        }else{
	            String str = "没有找到编号为【"+partsNo+"】的配件！";
                map.put(Constants.ERRMSG, str);
	        }
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
	 * <li>说明：新品检验责任部门树列表
	 * <li>创建人：何涛
	 * <li>创建日期：2014-08-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
    public void deptTree() throws Exception{
		Map<String, String> params = JSONUtil.read(getRequest().getParameter("searchParams"), Map.class);
        List<HashMap> children = manager.deptTree(params);
        JSONUtil.write(getResponse(), children);
    }
	
	/**
     * <li>说明：分页查询
     * <li>创建人：何涛
     * <li>创建日期：2014-08-15
     * <li>修改人：何涛
     * <li>修改日期：2016-03-11
     * <li>修改内容：新增PartsAccountBean查询实体
     * 
	 * @throws Exception
	 */
	public void queryPageList() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		try {
			String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), Constants.EMPTY_JSON_OBJECT);
			entity = (PartsAccount) JSONUtil.read(searchJson, entity.getClass());
			SearchEntity<PartsAccount> searchEntity = new SearchEntity<PartsAccount>(
					entity, getStart(), getLimit(), getOrders());
			map = this.manager.queryPageList(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
    
	/**
	 * <li>说明：查询配件信息用于退库
	 * <li>创建人：程梅
	 * <li>创建日期：2014-9-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
     * @throws JsonMappingException
     * @throws IOException
	 */
	public void getPartsAccountByForBack() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String partsNo = getRequest().getParameter(partsNoStr);  //配件编号
			String whIDX = getRequest().getParameter("whIDX"); //库房主键
			String getOrgId = getRequest().getParameter("getOrgId"); //领件部门
			String partsStatus = getRequest().getParameter("partsStatus"); //配件状态
			String whName = getRequest().getParameter("whName"); //库房名称
			String getOrg = getRequest().getParameter("getOrg"); //领件部门名称
			List list = this.manager.getPartsAccountByForBack(partsNo, whIDX, partsStatus,getOrgId);
			if(list != null && list.size() > 0){
				map.put(mapList, list);
	        }else{
	            String str = "找不到"+getOrg+"从"+whName+"领取配件【"+partsNo+"】的上车记录！";
                map.put(Constants.ERRMSG, str);
	        }
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
	 * <li>方法说明： 查询待修配件
	 * <li>方法名：findAwaitRepairParts
	 * @throws JsonMappingException
	 * @throws IOException
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-22
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public void findAwaitRepairParts() throws JsonMappingException, IOException{
		Map<String, Object> map = new HashMap<String,Object>();
        HttpServletRequest req = getRequest();
		try {
			String partsNo = req.getParameter(partsNoStr);  //配件编号
			String partsType = req.getParameter("partsType");
			map.put(mapList, manager.findAwaitRepairParts(partsType, partsNo));
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	
    /**
     * FIXME 代码审查[何涛2016-04-08]：代码分层，action层、或者webservice接口方法，只用于接收参数，将业务逻辑处理放到manager。
     * <li>说明：根据识别码查询配件周转台账信息和日志列表
     * <li>创建人：程梅
     * <li>创建日期：2015-10-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getAccountLogByIdCode() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        map.put(Constants.SUCCESS, false);
        try {
            String searchJson = StringUtil.nvlTrim( getRequest().getParameter("searchJson"), Constants.EMPTY_JSON_OBJECT);
            PartsAccount pa = (PartsAccount)JSONUtil.read(searchJson, PartsAccount.class);
            PartsManageLogManager partsManageLogManager = (PartsManageLogManager) getManager("partsManageLogManager");
            
            PartsAccount account = this.manager.getAccount(pa);
            if (null == account) {
                pa.setPartsNo(pa.getIdentificationCode());
                pa.setIdentificationCode(null);
                account = this.manager.getAccount(pa);
            }
            if (null == account) {
                map.put(Constants.ERRMSG, "此配件未登记！");
                return;
            }
            List<PartsManageLog> logList = partsManageLogManager.getLogListByIdx(account.getIdx()) ;//查询该配件的周转日志列表
            map.put("account", account) ;
            map.put("logList", logList) ;
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：根据配件识别码获取配件信息（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-10-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getAccountByIdCode() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String identificationCode = getRequest().getParameter("identificationCode"); 
            map.put("entity", manager.getAccountByIdCode(identificationCode));
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：查询在册配件列表
     * <li>创建人：程梅
     * <li>创建日期：2015-11-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findPageListByStatus() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            entity = (PartsAccount) JSONUtil.read(searchJson, entity.getClass());
            SearchEntity<PartsAccount> searchEntity = new SearchEntity<PartsAccount>(
                    entity, getStart(), getLimit(), getOrders());
            map = this.manager.findPageListByStatus(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：配件识别码绑定
     * <li>创建人：程梅
     * <li>创建日期：2015-11-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateAccountForBinding() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String dataJson = StringUtil.nvlTrim( getRequest().getParameter("dataJson"), "{}" );
            PartsAccount account = (PartsAccount)JSONUtil.read(dataJson, PartsAccount.class);
            String[] errMsg = this.manager.updateAccountForBinding(account);
            if (errMsg == null || errMsg.length < 1) {
                map.put(Constants.SUCCESS, true);
            }else{
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
     * <li>说明：分页查询，根据当前系统操作人员，查询当前班组可以修理的配件信息（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2016-3-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findPageForRepair() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
            PartsAccount objEntity = JSONUtil.read(searchJson, PartsAccount.class);
            SearchEntity<PartsAccount> searchEntity = new SearchEntity<PartsAccount>(objEntity, getStart(), getLimit(), getOrders());
            // Modified by hetao on 2016-04-05 与工位终端保持一致,模糊匹配出多个待修配件时,查询的配件需是当前班组承修的配件
            map = this.manager.findPageForRepair(searchEntity, true).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：通过id获取配件信息
     * <li>创建人：张迪
     * <li>创建日期：2016-6-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getModel() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            PartsAccount objEntity = this.manager.getModelById(id);
            map.put(Constants.ENTITY, objEntity);
           
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：查询所有上车配件信息
     * <li>创建人：张迪
     * <li>创建日期：2016-10-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findPartsForAboard() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
            PartsAccount objEntity = JSONUtil.read(searchJson, PartsAccount.class);
            SearchEntity<PartsAccount> searchEntity = new SearchEntity<PartsAccount>(objEntity, getStart(), getLimit(), getOrders());
            map = this.manager.findPartsForAboard(searchEntity, true).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
}