package com.yunda.jx.base.jcgy.action; 

import java.io.IOException;
import java.util.HashMap;
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
import com.yunda.jx.base.jcgy.entity.TrainType;
import com.yunda.jx.base.jcgy.manager.TrainTypeManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainType控制器, 机车车型编码
 * <li>创建人：王治龙
 * <li>创建日期：2012-10-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class TrainTypeAction extends JXBaseAction<TrainType, TrainType, TrainTypeManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
    /** 组织机构业务类：OmOrganizationManager */
    private OmOrganizationManager omOrganizationManager;
	
    public OmOrganizationManager getOmOrganizationManager() {
        return omOrganizationManager;
    }
    
    public void setOmOrganizationManager(OmOrganizationManager omOrganizationManager) {
        this.omOrganizationManager = omOrganizationManager;
    }
    /**
	 * <li>说明：单表分页查询，返回单表分页查询记录的json
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-10-24
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void pageList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
			String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
			String rcTypeId = StringUtil.nvlTrim( req.getParameter("rcTypeId"), "" ); //修程类型主键（过滤修程类型对应的车型）
			String undertakeOrgId = StringUtil.nvlTrim( req.getParameter("undertakeOrgId"), "" ); //承修单位主键（过滤承修车型对应的车型）
			TrainType entity = (TrainType)JSONUtil.read(searchJson, entitySearch.getClass());
			SearchEntity<TrainType> searchEntity = new SearchEntity<TrainType>(entity, getStart(), getLimit(), getOrders());
			Page page = this.manager.findPageTrainList(searchEntity , rcTypeId , undertakeOrgId);
			map.put("id", "typeID");
			map.put("root", page.getList());
			map.put("totalProperty", page.getTotal() == null ? page.getList().size() : page.getTotal());
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
     /**
     * 
     * <li>说明：查询车型组件所需信息
     * <li>创建人：程梅
     * <li>创建日期：2012-10-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 返回值为空
     * @throws 抛出异常列表
     */
    @SuppressWarnings("unchecked")
    public void pageListForTTCombo() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String orgCode = JXSystemProperties.OVERSEA_ORGCODE;
            OmOrganization org = omOrganizationManager.findOrgForCode(orgCode);
            String queryHql = req.getParameter("queryHql");
            String queryParams = req.getParameter("queryParams");
            String isCx = req.getParameter("isCx");
            Map queryParamsMap = new HashMap();
            if (!StringUtil.isNullOrBlank(queryParams)) {
                queryParamsMap = JSONUtil.read(queryParams, Map.class);
            }
//          query参数是获取EXTJS的combox控件捕获的键盘输入文字
            String queryValue = StringUtil.nvlTrim(req.getParameter("query"), "");
            map = this.manager.page(queryValue, queryParamsMap, getStart(), getLimit(), queryHql,isCx,org.getOrgseq());
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * 
     * <li>说明：查询车型列表
     * <li>创建人：程锐
     * <li>创建日期：2012-11-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return void
     * @throws Exception
     */
    @SuppressWarnings(value="unchecked")
    public void findPageList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String orgCode = JXSystemProperties.OVERSEA_ORGCODE;
            OmOrganization org = omOrganizationManager.findOrgForCode(orgCode);
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            entity = (TrainType)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<TrainType> searchEntity = new SearchEntity<TrainType>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.pageList(searchEntity, org.getOrgseq()).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    /**
     * 
     * <li>说明：查询承修车型列表
     * <li>创建人：程锐
     * <li>创建日期：2014-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public void queryUndertakeTrainTypeList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			map = this.manager.queryUndertakeTrainTypeList().extjsResult();
			map.put("id", "typeID");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
    
    /**
     * <li>方法说明： 树控件查询车型
     * <li>方法名：findTrainTypeForTree
     * @throws JsonMappingException
     * @throws IOException
     * <li>创建人： 张凡
     * <li>创建日期：2015-10-23
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public void findTrainTypeForTree() throws JsonMappingException, IOException{
		JSONUtil.write(this.getResponse(), manager.findTrainTypeForTree());
    }
}