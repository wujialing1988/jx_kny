package com.yunda.jxpz.orgdic.action; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jxpz.orgdic.entity.OrgDicItem;
import com.yunda.jxpz.orgdic.manager.OrgDicItemManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：OrgDicItem控制器, 常用部门字典项
 * <li>创建人：程梅
 * <li>创建日期：2015-09-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class OrgDicItemAction extends JXBaseAction<OrgDicItem, OrgDicItem, OrgDicItemManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    private String dictTypeIdx = "dictTypeId" ;//字典编码
    /**
     * 
     * <li>说明：根据字典编码查询字典项列表
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void pageListByTypeId() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String dictTypeId = getRequest().getParameter(dictTypeIdx);  //字典编码
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            OrgDicItem objEntity = (OrgDicItem)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<OrgDicItem> searchEntity = new SearchEntity<OrgDicItem>(objEntity, getStart(), getLimit(), getOrders());
            map = this.manager.pageListByTypeId(searchEntity, dictTypeId).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    /**
     * 
     * <li>说明：保存常用部门字典项
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveItems() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            //字典编码
            String dictTypeId = getRequest().getParameter(dictTypeIdx) ;
            OrgDicItem[] itemList = (OrgDicItem[])JSONUtil.read(getRequest(), OrgDicItem[].class);
            String[] errMsg = this.manager.validateSave(dictTypeId, itemList);
            
          if (errMsg == null || errMsg.length < 1) {
              this.manager.saveItems(dictTypeId , itemList);
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
     * 
     * <li>说明：保存常用部门字典项信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveSingleItem() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            //字典编码
            String dictTypeId = req.getParameter(dictTypeIdx) ;
            String itemJson = StringUtil.nvlTrim( req.getParameter("itemData"), "{}" );
            OrgDicItem item = (OrgDicItem)JSONUtil.read(itemJson, OrgDicItem.class);
            String[] errMsg = this.manager.validateSingle(dictTypeId , item);
          if (errMsg == null || errMsg.length < 1) {
              this.manager.saveSingleItem(dictTypeId , item);
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
     * <li>说明：接受物理删除记录请求，向客户端返回操作结果（JSON格式）
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */ 
    @SuppressWarnings("all")
    public void delete() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String[] errMsg = this.manager.validateDelete(ids);
            String dictTypeId = getRequest().getParameter(dictTypeIdx) ;
            if (errMsg == null || errMsg.length < 1) {
                this.manager.delete(dictTypeId, ids);
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
     * 
     * <li>说明：获取配件周转常用部门列表
     * <li>创建人：程梅
     * <li>创建日期：2015-10-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getAccountOrgList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            OrgDicItem objEntity = (OrgDicItem)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<OrgDicItem> searchEntity = new SearchEntity<OrgDicItem>(objEntity, getStart(), getLimit(), getOrders());
            List<OrgDicItem> itemList = this.manager.getAccountOrgList(searchEntity).getList();
            for(OrgDicItem item : itemList){
                item.setOrgid(item.getId().getOrgId());
            }
            map = Page.extjsStore("orgid", itemList);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
}