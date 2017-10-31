package com.yunda.jx.jxgc.dispatchmanage.action; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.dispatchmanage.entity.RepairLine;
import com.yunda.jx.jxgc.dispatchmanage.manager.RepairLineManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：RepairLine控制器, 检修流水线
 * <li>创建人：程锐
 * <li>创建日期：2012-12-07
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class RepairLineAction extends JXBaseAction<RepairLine, RepairLine, RepairLineManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * /jsp/jx/jxgc/dispatchmanage/RepairLine.js
     * /jsp/jx/jxgc/dispatchmanage/RepairLine_Parts.js
     * <li>说明：分页查询，返回实体类的分页列表对象
     * <li>创建人：程锐
     * <li>创建日期：2012-12-9
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
            String status = req.getParameter("status");
            String repairLineType = req.getParameter("repairLineType");
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            RepairLine entity = (RepairLine)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<RepairLine> searchEntity = new SearchEntity<RepairLine>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findPageList(searchEntity, status, repairLineType).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    /**
     * /jsp/jx/jxgc/dispatchmanage/RepairLine.js
     * /jsp/jx/jxgc/dispatchmanage/RepairLine_Parts.js
     * <li>说明：接受请求更新流水线记录的业务状态，可变更为启用或废弃状态
     * <li>创建人：程锐
     * <li>创建日期：2012-12-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("all")
    public void updateStatus() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            int status = Integer.parseInt(getRequest().getParameter("status"));
            String[] errMsg = this.manager.validateUpdateStatus(status, ids);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.updateStatus(status, this.ids);
                map.put(Constants.SUCCESS, "true");
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
     * /jsp/jx/jxgc/dispatchmanage/RepairLine.js
     * /jsp/jx/jxgc/dispatchmanage/RepairLine_Parts.js
     * <li>说明：保存或更新流水线，更新流水线时同步更新流水线对应工位的流水线名称
     * <li>创建人：程锐
     * <li>创建日期：2013-12-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
	public void saveRepairLine() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			RepairLine t = (RepairLine)JSONUtil.read(getRequest(), entity.getClass());
			String[] errMsg = this.manager.validateUpdate(t);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.saveRepairLine(t);
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
     * <li>说明：流水线及下属工位树
     * <li>创建人：何涛
     * <li>创建日期：2015-9-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception 抛出异常列表
     */
    @SuppressWarnings("unchecked")
    public void tree() throws Exception{
        List<HashMap<String, Object>> children = null;
        HttpServletRequest req = getRequest();
        try {
            String parentIDX = req.getParameter("parentIDX");              // 上级工位主键
            // 树节点(工位)是否支持多选的标识
            String checkedTree = StringUtil.nvl(req.getParameter("checkedTree"), "true");
            children = manager.tree(parentIDX, Boolean.parseBoolean(checkedTree));
        } catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
        } finally{
            JSONUtil.write(getResponse(), children);
        }
    }
    
}