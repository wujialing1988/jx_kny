package com.yunda.jx.jxgc.dispatchmanage.action;

import java.util.ArrayList;
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
import com.yunda.jx.jxgc.dispatchmanage.entity.WorkStation;
import com.yunda.jx.jxgc.dispatchmanage.manager.WorkStationManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkStation控制器, 工位
 * <li>创建人：程锐
 * <li>创建日期：2012-12-07
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class WorkStationAction extends JXBaseAction<WorkStation, WorkStation, WorkStationManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * /jsp/jx/jxgc/dispatchmanage/WorkStation.js
     * <li>说明：分页查询，返回实体类的分页列表对象
     * <li>创建人：程锐
     * <li>创建日期：2012-12-9
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void findPageList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String status = req.getParameter("status");
            String searchJson = StringUtil.nvlTrim(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
            WorkStation entity = (WorkStation) JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<WorkStation> searchEntity = new SearchEntity<WorkStation>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findPageList(searchEntity, status).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * /jsp/jx/jxgc/dispatchmanage/WorkStation.js
     * <li>说明：接受请求更新工位记录的业务状态，可变更为启用或废弃状态
     * <li>创建人：程锐
     * <li>创建日期：2012-12-9
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容
     * @throws Exception
     */
    @SuppressWarnings("all")
    public void updateStatus() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            int status = Integer.parseInt(getRequest().getParameter("status"));
            this.manager.updateStatus(status, this.ids);
            map.put("success", "true");
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * /jsp/jx/js/component/jxgc/WorkStationSelect.js
     * <li>说明：工位选择控件
     * <li>创建人：程锐
     * <li>创建日期：2013-3-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void pageListForCmp() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String queryHql = req.getParameter("queryHql");
            String searchJson = StringUtil.nvlTrim(req.getParameter(Constants.ENTITY_JSON), "{}");
            WorkStation entity = JSONUtil.read(searchJson, WorkStation.class);
            SearchEntity<WorkStation> searchEntity = new SearchEntity<WorkStation>(entity, start, limit, null);
            map = this.manager.page(searchEntity, getStart(), getLimit(), queryHql);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：工长默认派工页面
     * <li>创建人：程锐
     * <li>创建日期：2013-7-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void workStationPageList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
            WorkStation entity = (WorkStation) JSONUtil.read(searchJson, entitySearch.getClass());
            String orderString = " order by a.update_Time desc";
            SearchEntity<WorkStation> searchEntity = new SearchEntity<WorkStation>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.workStationPageList(searchEntity, orderString).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：查询工位信息用于工艺节点关联工位时选择
     * <li>创建人：程梅
     * <li>创建日期：2013-7-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findListForTecProcessNode() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String tecProcessNodeIdx = req.getParameter("tecProcessNodeIdx"); // 工艺节点id
            String searchJson = StringUtil.nvlTrim(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
            WorkStation entity = (WorkStation) JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<WorkStation> searchEntity = new SearchEntity<WorkStation>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findListForTecProcessNode(searchEntity, tecProcessNodeIdx).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * jsp/jx/js/component/jxgc/JobNodeStationDefSelect.js
     * <li>说明：工位选择查询控件方法
     * <li>创建人：王利成
     * <li>创建日期：2015-4-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void findSelectPageList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
            WorkStation entity = (WorkStation) JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<WorkStation> searchEntity = new SearchEntity<WorkStation>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findSelectPageList(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据输入的工位编码实现台位绑定工位
     * <li>创建人：程梅
     * <li>创建日期：2015-5-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveStationByCode() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String workStationCode = getRequest().getParameter("workStationCode"); // 工位编码
            String deskCode = getRequest().getParameter("deskCode"); // 台位编码
            String deskName = getRequest().getParameter("deskName"); // 台位名称
            String mapcode = getRequest().getParameter("mapcode"); // 所属图
            List list = this.manager.saveStationByCode(deskCode, deskName, mapcode, workStationCode);
            if (list != null && list.size() > 0) {
                map.put("list", list);
            } else {
                String str = "没有找到编号为【" + workStationCode + "】的工位！</br> 或者 </br> 该工位已绑定了台位！";
                map.put("errMsg", str);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据所选工位信息实现台位绑定工位
     * <li>创建人：程梅
     * <li>创建日期：2015-5-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveStationBySelect() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String deskCode = getRequest().getParameter("deskCode"); // 台位编码
            String deskName = getRequest().getParameter("deskName"); // 台位名称
            String mapcode = getRequest().getParameter("mapcode"); // 所属图
            WorkStation[] stationList = (WorkStation[]) JSONUtil.read(getRequest(), WorkStation[].class); // 所选工位信息
            List<WorkStation> list = new ArrayList<WorkStation>(stationList.length);
            for (WorkStation station : stationList) {
                list.add(station);
            }
            this.manager.saveStation(deskCode, deskName, mapcode, list);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：解除绑定
     * <li>创建人：程梅
     * <li>创建日期：2015-5-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void logicUpdate() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.manager.logicUpdate(ids);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：查询工位列表【台位绑定工位中选择工位】
     * <li>创建人：程梅
     * <li>创建日期：2015-5-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findListForBindWorkStation() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
            WorkStation entity = (WorkStation) JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<WorkStation> searchEntity = new SearchEntity<WorkStation>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findListForBindWorkStation(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：工位树
     * <li>创建人：何涛
     * <li>创建日期：2015-09-06
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception 抛出异常列表
     */
    @SuppressWarnings("unchecked")
    public void tree() throws Exception {
        List<HashMap<String, Object>> children = null;
        HttpServletRequest req = getRequest();
        try {
            String parentIDX = req.getParameter("parentIDX"); // 上级工位主键
            String repairLineIdx = req.getParameter("repairLineIdx"); // 流水线主键
            // 树节点(工位)是否支持多选的标识
            String checkedTree = StringUtil.nvl(req.getParameter("checkedTree"), "false");
            children = manager.tree(parentIDX, repairLineIdx, Boolean.parseBoolean(checkedTree));
        } catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
        } finally {
            JSONUtil.write(getResponse(), children);
        }
    }
    
}
