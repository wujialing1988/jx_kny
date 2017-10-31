package com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.action;

import java.io.IOException;
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
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.entity.PartsOutsourcing;
import com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.entity.PartsOutsourcingOutBean;
import com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.manager.PartsOutsourcingManager;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配件委外业务控制类
 * <li>创建人：王斌
 * <li>创建日期：2014-5-21
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class PartsOutsourcingAction extends JXBaseAction<PartsOutsourcing, PartsOutsourcing,PartsOutsourcingManager> {
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    
	/**
	 * <li>说明：配件委外登记
	 * <li>创建人：王斌
	 * <li>创建日期：2014-5-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @throws 抛出异常列表
	 */
	public void savePartsOutBill() throws Exception{
		Map<String,Object> map=new HashMap<String, Object>();
		try {
			String form=StringUtil.nvlTrim(getRequest().getParameter("outForm"), Constants.EMPTY_JSON_OBJECT );
			PartsOutsourcing outsourcing= JSONUtil.read(form, PartsOutsourcing.class);
			PartsOutsourcing[] partsOutsourcings=JSONUtil.read(getRequest(), PartsOutsourcing[].class);
			OmEmployee emp=(OmEmployee) getSession().getAttribute("emp");
			this.manager.savePartsOutBill(outsourcing, partsOutsourcings,emp);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}finally{
			JSONUtil.write(getResponse(), map);
		}
	}
    
	/**
	 * <li>说明：获取状态树（配件委外中配件状态专用方法）
	 * <li>创建人：王斌
	 * <li>创建日期：2014-7-3
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public void statusTree() throws Exception{
		HttpServletRequest req=getRequest();
		Map<String, Object> paramMap=new HashMap<String, Object>();
		List<HashMap> children=null;
		try {//parentIDX,queryParams,queryHql
			String parentIDX = StringUtil.nvlTrim(req.getParameter("parentIDX"), "ROOT_0");
			Map<String, String> map = JSONUtil.read(req.getParameter("queryParams"), Map.class);
			String dicttypeid = "";
			if(map != null && !map.isEmpty() && map.containsKey("dicttypeid")) {
				dicttypeid = map.get("dicttypeid");
			}
			paramMap.put("parentIDX", parentIDX);
			paramMap.put("dicttypeid", dicttypeid);
			children=this.manager.getStatusByEosDicEntity(paramMap);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
		}finally{
			 JSONUtil.write(getResponse(), children);
		}
	}
    
	/**
	 * <li>说明：根据配件编号，查询委外的配件
	 * <li>创建人：王斌
	 * <li>创建日期：2014-5-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @throws 抛出异常列表
	 */
	public void findPartsOutforNo() throws Exception{
		Map<String, Object> map=new HashMap<String, Object>();
		HttpServletRequest req=getRequest();
		try {
			String partsNo=req.getParameter("billId");
			String partsStatus=req.getParameter("status");
			Map<String, Object> paramMap=new HashMap<String, Object>();
			if(!StringUtil.isNullOrBlank(partsNo)) paramMap.put("partsNo", partsNo);
			if(!StringUtil.isNullOrBlank(partsStatus)) paramMap.put("partsStatus", partsStatus);
			Object outsourcing=this.manager.findPartsOutforNo(paramMap);
			if(outsourcing!=null){
				map.put("entity", outsourcing);
                map.put(Constants.SUCCESS, true);
			}else{
                map.put(Constants.SUCCESS, false);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}finally{
			JSONUtil.write(getResponse(), map);
		}
	}
    
	/**
	 * <li>说明：获取委外配件信息列表
	 * <li>创建人：王斌
	 * <li>创建日期：2014-5-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @throws 抛出异常列表
	 */
	public void findPageQuery() throws Exception{
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			map=this.manager.findPageListForOutSource(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}finally{
			JSONUtil.write(getResponse(), map);
		}
	}
    
    /**
     * <li>说明：分页查询【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findPageList () throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        HttpServletRequest req = getRequest();
        try {
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), Constants.EMPTY_JSON_OBJECT );
            PartsOutsourcing objEntity = (PartsOutsourcing)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<PartsOutsourcing> searchEntity = new SearchEntity<PartsOutsourcing>(objEntity, getStart(), getLimit(), getOrders());
            Page<PartsOutsourcing> page = this.manager.findPageList(searchEntity);
            
            map = page.extjsResult();

        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：委外配件登记【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void savePartsOutsourcing() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String outsourcingJson = StringUtil.nvlTrim( req.getParameter("outsourcingData"), Constants.EMPTY_JSON_OBJECT );
            PartsOutsourcing outsourcing = (PartsOutsourcing)JSONUtil.read(outsourcingJson, PartsOutsourcing.class);
            if(StringUtil.isNullOrBlank(outsourcing.getIdx()))  this.manager.savePartsOutsourcing(outsourcing);
            else this.manager.updatePartsOutsourcing(outsourcing);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * <li>说明：撤销委外
     * <li>创建人：程梅
     * <li>创建日期：2015-10-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateOutsourcingForCancel() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            this.manager.updateOutsourcingForCancel(id);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * <li>说明：批量委外撤销
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-11-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateOutsourcingForCancelBatch() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            this.manager.updateOutsourcingForCancelBatch(ids);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    
    /**
     * <li>说明：根据配件编号和规格型号查询最新的【待修】配件周转台账信息
     * <li>创建人：程梅
     * <li>创建日期：2015-10-27
     * <li>修改人：何涛
     * <li>修改日期：2016-04-08
     * <li>修改内容：审查规范：代码分层，action层、或者webservice接口方法，只用于接收参数，将业务逻辑处理放到manager。
     * @throws Exception
     */
    public void getPartsAccount() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        map.put(Constants.SUCCESS, false);
        try {
            String searchJson = StringUtil.nvlTrim(getRequest().getParameter("searchJson"), Constants.EMPTY_JSON_OBJECT);
            PartsAccount account = (PartsAccount)JSONUtil.read(searchJson, PartsAccount.class);
            
            account = this.manager.getPartsAccount(account);
            map.put("account", account) ;
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    /**
     * 
     * <li>说明：根据配件编号和规格型号查询【待修、待报废】配件周转台账信息列表
     * <li>创建人：程梅
     * <li>创建日期：2016-8-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getPartsAccountList() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String searchJson = StringUtil.nvlTrim( getRequest().getParameter("searchJson"), "{}" );
            PartsAccount account = (PartsAccount)JSONUtil.read(searchJson, PartsAccount.class);
            List<PartsAccount> accountList = this.manager.getPartsAccountList(account);
            map.put("accountList", accountList) ;
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        } 
    }
    /**
     * <li>说明：配件委外登记【web端】
     * <li>创建人：程锐
     * <li>创建日期：2015-11-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveBatchPartsOutsourcing() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String registerData = StringUtil.nvlTrim( getRequest().getParameter("registerDatas"), "[]" );
            PartsOutsourcing[] register = (PartsOutsourcing[])JSONUtil.read(registerData, PartsOutsourcing[].class);
            this.manager.saveBatchPartsOutsourcing(register);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    /**
     * <li>说明：委外配件回段【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016-4-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updatePartsOutsourcingForBack() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String outsourcingJson = StringUtil.nvlTrim( req.getParameter("outsourcingData"), Constants.EMPTY_JSON_OBJECT );
            PartsOutsourcing outsourcing = (PartsOutsourcing)JSONUtil.read(outsourcingJson, PartsOutsourcing.class);
            this.manager.updatePartsOutsourcingForBack(outsourcing);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    /**
     * <li>说明：撤销委外回段【手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016-4-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateOutsourcingForCancelBack() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            this.manager.updateOutsourcingForCancelBack(id);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    /**
     * 
     * <li>说明：返回识别码唯一性验证
     * <li>创建人：程梅
     * <li>创建日期：2016-4-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findIdentificationCodeValidate() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String outsourcingJson = StringUtil.nvlTrim( req.getParameter("outsourcingData"), Constants.EMPTY_JSON_OBJECT );
            PartsOutsourcing outsourcing = (PartsOutsourcing)JSONUtil.read(outsourcingJson, PartsOutsourcing.class);
            this.manager.findIdentificationCodeValidate(outsourcing);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    /**
     * <li>说明：批量撤销回段【web段】
     * <li>创建人：程梅
     * <li>创建日期：2016-4-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateForCancelBackBatch() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            this.manager.updateForCancelBackBatch(ids);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }

    /**
     * <li>说明：批量委外配件回段【用于web端】
     * <li>创建人：张迪
     * <li>创建日期：2016-11-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updatePartsOutsourcingForBackNew() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            PartsOutsourcing[] outsourcing = (PartsOutsourcing[]) JSONUtil.read(getRequest(), PartsOutsourcing[].class);
            this.manager.updatePartsOutsourcingForBackNew(outsourcing);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }

    
    
    /**
     * <li>说明：配件委外登记查询（新）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-11-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findPartsoutsourcingOutAll() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String workPlanId = req.getParameter("workPlanId");// 计划ID
            map = this.manager.findPartsoutsourcingOutAll(getStart(),getLimit(),workPlanId).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        } 
    }
    
    /**
     * <li>说明：配件委外登记 pad
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-1-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findPartsoutsourcingOutAllForPad() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String type = req.getParameter("type"); // "1":未登记；"2":已登记
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), Constants.EMPTY_JSON_OBJECT );
            PartsOutsourcingOutBean objEntity = (PartsOutsourcingOutBean)JSONUtil.read(searchJson, PartsOutsourcingOutBean.class);
            SearchEntity<PartsOutsourcingOutBean> searchEntity = new SearchEntity<PartsOutsourcingOutBean>(objEntity, getStart(), getLimit(), getOrders());
            Page<PartsOutsourcingOutBean> page = this.manager.findPartsoutsourcingOutAllForPad(searchEntity,type);
            map = page.extjsResult();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        } 
    }
   
    /**
     * <li>说明：委外回段查询
     * <li>创建人：张迪
     * <li>创建日期：2016-11-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void findpageQuery() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            QueryCriteria<PartsOutsourcing> query = new QueryCriteria<PartsOutsourcing>(getQueryClass(),getWhereList(), getOrderList(), getStart(), 1000);
            map = this.manager.findPageList(query).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }   
}
