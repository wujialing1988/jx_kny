package com.yunda.jxpz.phrasedic.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jxpz.phrasedic.entity.PhraseDicItem;
import com.yunda.jxpz.phrasedic.manager.PhraseDicItemManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PhraseDicItem控制器, 常用短语字典项
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
public class PhraseDicItemAction extends JXBaseAction<PhraseDicItem, PhraseDicItem, PhraseDicItemManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
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
            if (errMsg == null || errMsg.length < 1) {
                this.manager.delete(ids);
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
     * 
     * <li>说明：保存常用短语字典项
     * <li>创建人：程梅
     * <li>创建日期：2015-10-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveDicItem() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String itemJson = StringUtil.nvlTrim( req.getParameter("itemData"), "{}" );
            PhraseDicItem item = (PhraseDicItem)JSONUtil.read(itemJson, PhraseDicItem.class);
            String[] errMsg = this.manager.validateUpdate(item);
          if (errMsg == null || errMsg.length < 1) {
              this.manager.saveOrUpdate(item);
              map.put("success", "true");
          }else{
              map.put("errMsg", errMsg);
          }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    /**
     * 
     * <li>说明：获取下车原因列表【下车原因编码（字典编码）为“reasoncode”】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findUnloadReasonList () throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String dictTypeId = "reasoncode" ; //下车原因编码为“reasoncode”
            List<PhraseDicItem> list = this.manager.getListByDictTypeId(dictTypeId, null);
            map = Page.extjsStore("dictItemId", list);

        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
}