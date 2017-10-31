package com.yunda.jx.pjwz.partsBase.repairlist.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjwz.partsBase.repairlist.entity.PartsRepairList;
import com.yunda.jx.pjwz.partsBase.repairlist.manager.PartsRepairListManager;


/**
 * <li>说明：配件自修列表
 * <li>创建人： 张凡
 * <li>创建日期：2015-11-2
 * <li>成都运达科技股份有限公司
 */
public class PartsRepairListAction extends JXBaseAction<PartsRepairList, PartsRepairList, PartsRepairListManager> {

    private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * /jx/jxgc/producttaskmanage/preparatory/RepairPartsType_select.js调用
     * /jx/jxgc/producttaskmanage/preparatory/SelfRepairParts_Select.js调用
     * <li>方法说明：查询自修配件列表 
     * <li>方法名称：findPageList
     * <li>@throws Exception
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-10-23 上午10:36:57
     * <li>修改人：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    public void findPageList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            map = this.manager.findPageDataList(req.getParameter("entityJson"), getStart(), getLimit(), getOrders()).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * /jsp/jx/pjwz/partbase/SelfRepairDir.js调用
     * <li>方法说明：新增委外配件型号目录 
     * <li>方法名称：newList
     * <li>@throws JsonMappingException
     * <li>@throws IOException
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-11-27 下午04:15:28
     * <li>修改人：
     * <li>修改内容：
     */
    public void newList() throws JsonMappingException, IOException{
        
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String idx = req.getParameter("idxs");
            manager.saveNewList(idx);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * /jsp/jx/pjwz/partbase/SelfRepairDir.js调用
     * <li>方法说明：设置施修班组
     * <li>方法名称：setRepairOrg
     * <li>@throws JsonMappingException
     * <li>@throws IOException
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-11-27 下午04:35:17
     * <li>修改人：
     * <li>修改内容：
     */
    public void setRepairOrg() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String idx = req.getParameter("idxs");
            String org = req.getParameter("org");
            manager.updateSetRepairOrg(idx, org);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * 
     * <li>说明：获取承修部门树列表
     * <li>创建人：程锐
     * <li>创建日期：2014-5-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void tree() throws Exception{
        List<Map> children = manager.findPartsRepairOrgTreeData();
        JSONUtil.write(getResponse(), children);
    }
    
    /**
     * <li>方法说明：查询承修班组
     * <li>方法名：repairOrg
     * @throws IOException
     * <li>创建人： 张凡
     * <li>创建日期：2015-10-19
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     */
    public void repairOrg() throws IOException{
    	String partsTypeIDX = getRequest().getParameter("queryName");
    	Map<String, Object> map = manager.findRepairOrg(getStart(), getLimit(), partsTypeIDX).extjsStore();
    	JSONUtil.write(getResponse(), map);
    }
    
    /**
     * 
     * <li>方法名：updateisHgys
     * <li>
     * <li>返回类型：void
     * <li>说明：设置是否合格验收
     * <li>创建人：曾雪
     * <li>创建日期：2016-1-21
     * <li>修改人： 
     * <li>修改日期：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void updateisHgys() throws JsonMappingException, IOException{
    	Map<String, Object> map = new HashMap<String,Object>();
		try {
            String hgysCode = getRequest().getParameter("hgysCode");
            this.manager.updateIsHgys(ids, hgysCode);
		} catch (Exception e) {
            map.put("success", false);
            map.put("errMsg", e.getMessage());
            logger.error("控制层捕获异常：", e);
        } finally {
			JSONUtil.write(this.getResponse(), map);
		}
    }
}
