package com.yunda.zb.tp.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.third.poi.excel.ColumnPattern;
import com.yunda.third.poi.excel.ExcelExport;
import com.yunda.third.poi.excel.ExportExcelDTO;
import com.yunda.zb.common.ZbConstants;
import com.yunda.zb.tp.entity.ZbglTp;
import com.yunda.zb.tp.manager.ZbglTpManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglTp控制器, JT6提票
 * <li>创建人：程锐
 * <li>创建日期：2015-01-21
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class ZbglTpAction extends JXBaseAction<ZbglTp, ZbglTp, ZbglTpManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
     
    /**
     * <li>说明：单表分页查询，返回单表分页查询记录的json
     * <li>创建人：刘晓斌
     * <li>创建日期：2016-8-18
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
            // 添加站点ID过滤
            List<Condition> whereListNew = new ArrayList<Condition>();
            List<Condition> whereList = getWhereList();
            String synSiteID = EntityUtil.findSysSiteId(null);
            if (null != synSiteID && synSiteID.trim().length() > 0) {
                Condition condition = new Condition();
                condition.setPropName("siteID");
                condition.setPropValue(synSiteID);
                whereListNew.add(condition);
            }
            for (Condition condition : whereList) {
                whereListNew.add(condition);
            }
            QueryCriteria<ZbglTp> query = new QueryCriteria<ZbglTp>(getQueryClass(),whereListNew, getOrderList(), getStart(), getLimit());
            Page<ZbglTp> page = this.manager.findPageList(query);
            List<ZbglTp> list = page.getList();
            if(null != list && list.size() > 0){
                List<String> tpIdxs = new ArrayList<String>();
                for (ZbglTp tp : list) {
                    tpIdxs.add(tp.getIdx());
                }
                Map<String,String> audioIdxMap = this.manager.findAudioIdx(tpIdxs);
                if(null != audioIdxMap && audioIdxMap.size() > 0){
                    for (ZbglTp tp : list) {
                        tp.setAudioAttIdx(audioIdxMap.get(tp.getIdx()));
                    }
                }
                page.setList(list);
            }
            map = page.extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    /**
     * <li>说明：检查有无同车同位置同故障现象的未处理的碎修票
     * <li>创建人：程锐
     * <li>创建日期：2015-1-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void checkData() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ZbglTp entity = (ZbglTp) JSONUtil.read(getRequest(), ZbglTp.class);
            if (manager.checkData(entity)) {
                map.put(Constants.SUCCESS, true);
            } else {
                map.put(Constants.SUCCESS, false);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：保存并实例化提票
     * <li>创建人：程锐
     * <li>创建日期：2015-1-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveTpAndInst() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter(Constants.ENTITY_JSON), "{}");
            ZbglTp[] entities = JSONUtil.read(searchJson, ZbglTp[].class);
            ZbglTp entity = entities.length > 0 ? entities[0] : null; // 取第一条提票信息
            if (entity != null) {
               this.manager.saveTpAndInst(entities, entity);
                map.put(Constants.SUCCESS, true);
            }
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：临修票调度派工
     * <li>创建人：程锐
     * <li>创建日期：2015-1-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateForLxDdpg() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ZbglTp data = (ZbglTp) JSONUtil.read(getRequest(), entity.getClass());
            String[] ids = (String[]) JSONUtil.read(getRequest().getParameter("ids"), String[].class);
            this.manager.updateForLxDdpg(data, ids);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：逻辑删除提票及关联的附件
     * <li>创建人：程锐
     * <li>创建日期：2015-3-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void logicDeleteTP() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            List<String> errMsg = this.manager.logicDeleteTP(ids);
            if (errMsg == null || errMsg.size() < 1) {
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
     * <li>说明：临碎修票活分页查询（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-7-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void findPageQuery() throws JsonMappingException, IOException  {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String search = getRequest().getParameter("search");
            String entityJson = StringUtil.nvl(getRequest().getParameter(Constants.ENTITY_JSON), "{}");
            ZbglTp entity = JSONUtil.read(entityJson, ZbglTp.class);
            SearchEntity<ZbglTp> searchEntity = new SearchEntity<ZbglTp>(entity, getStart(), getLimit(), getOrders());
            Page<ZbglTp> page = this.manager.findPageQuery(searchEntity,search);
            map = page.extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            ObjectMapper om = new ObjectMapper();
            // 格式化日期
            om.getSerializationConfig().setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            om.writeValue(this.getResponse().getWriter(), map);
        }   
    }
    
    /**
     * <li>说明：临碎修票活领取（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-7-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void updateForReceive() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            // 当前操作员信息
            OmEmployee employee = SystemContext.getOmEmployee(); 
            OmOrganization organization = SystemContext.getOmOrganization();
            List<ZbglTp> entityList = new ArrayList<ZbglTp>();
            Date date = Calendar.getInstance().getTime();       // 同一接活时间
            ZbglTp tp = null;
            for (String idx: ids) {
                tp = this.manager.getModelById(idx);
                if (null != tp.getRevPersonId()) {
                    throw new BusinessException("票活【" + tp.getFaultNoticeCode() + "】已经被" + tp.getRevPersonName() + "领取，请刷新后重试！");
                }
                tp.setRevPersonId(employee.getEmpid());         // 接活人id
                tp.setRevPersonName(employee.getEmpname());     // 接活人名称
                tp.setRevTime(date);                            // 接活日期
                tp.setFaultNoticeStatus(ZbglTp.STATUS_OPEN);    // 业务状态：待销活
                // 如果是碎修票，还需设置接活人班组信息
                if (ZbConstants.REPAIRCLASS_SX.equals(tp.getRepairClass())) {
                    tp.setRevOrgID(organization.getOrgid());
                    tp.setRevOrgName(organization.getOrgname());
                    tp.setRevOrgSeq(organization.getOrgseq());
                }
                entityList.add(tp);
            }
            this.manager.saveOrUpdate(entityList);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：临碎修票活撤销领取（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-7-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void updateForRevoke() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            List<ZbglTp> entityList = new ArrayList<ZbglTp>();
            ZbglTp tp = null;
            for (String idx: ids) {
                tp = this.manager.getModelById(idx);
                tp.setRevPersonId(null);                            // 清空接活人id
                tp.setRevPersonName(null);                          // 清空接活人名称
                tp.setRevTime(null);                                // 清空接活日期
                tp.setFaultNoticeStatus(ZbglTp.STATUS_DRAFT);       // 业务状态：待接活
                // 如果是碎修票，还需清空接活人班组信息
                if (ZbConstants.REPAIRCLASS_SX.equals(tp.getRepairClass())) {
                    tp.setRevOrgID(null);
                    tp.setRevOrgName(null);
                    tp.setRevOrgSeq(null);
                }
                entityList.add(tp);
            }
            this.manager.saveOrUpdate(entityList);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：销活（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-7-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void handleTp() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        HttpServletRequest req = getRequest();
        try {
            // 操作者ID
            Long operatorid = Long.valueOf(req.getParameter("operatorid"));
            // 提票活idx，多个idx用,分隔
            String idxs = req.getParameter("idxs");
            /* 提票处理信息实体
             * {
             *    "methodDesc " : "配装",
             *    "repairResult " : 4,
             *    "repairDesc " : "良好"
             * }
             */
            String entityJson = req.getParameter(Constants.ENTITY_JSON);
            ZbglTp tpData = JSONUtil.read(entityJson, ZbglTp.class);
            this.manager.handleTp(operatorid, idxs, tpData);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：获取碎修、临修质量检验分页列表（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-8-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getZbglTpQCPageList() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            String entityJson = req.getParameter(Constants.ENTITY_JSON);
            String queryString = req.getParameter("queryString");
            ZbglTp objEntity = JSONUtil.read(entityJson, ZbglTp.class);
            // 当前系统操作人员
            Long empid = SystemContext.getOmEmployee().getEmpid();
            map = this.manager.getZbglTpQCPageList(empid, start, limit, objEntity.getRepairClass(), queryString).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            ObjectMapper om = new ObjectMapper();
            // 格式化日期
            om.getSerializationConfig().setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            om.writeValue(this.getResponse().getWriter(), map);
        }
    }
    
    
    /**
     * <li>说明：完成临碎修提票质量检验项（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-8-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void updateFinishQCResult() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            // 批量检验
            String accJson = req.getParameter("accJson");
            ZbglTp accEntity = null;
            if (null != accJson && accJson.trim().length() > 0) {
                accEntity = JSONUtil.read(accJson, ZbglTp.class);
            }
            this.manager.updateFinishQCResult(ids, accEntity);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：查询提票综合统计（根据条件动态显示字段）
     * <li>创建人：林欢
     * <li>创建日期：2016-3-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findTpPageListByParm() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        List<ZbglTp> list = new LinkedList<ZbglTp>();
        try {
            String fields = StringUtil.nvlTrim(getRequest().getParameter("fields"), "");
            list = getTpZhTjMapByFlag(1,null,fields);
            map = new Page<ZbglTp>(list.size(), list).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：查询提票综合统计明细(根据所选行)
     * <li>创建人：林欢
     * <li>创建日期：2016-3-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findTpDeteailPageListByParm() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        List<ZbglTp> list = new LinkedList<ZbglTp>();
        try {
            list = getTpZhTjMapByFlag(2,null,"");
            map = new Page<ZbglTp>(list.size(), list).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：查询提票综合统计导出(根据所选行)
     * <li>创建人：林欢
     * <li>创建日期：2016-3-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings({ "unused", "unchecked" })
    public void exportTpListByParm() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        List<ZbglTp> list = new LinkedList<ZbglTp>();
        List<ZbglTp> listDeteail = new LinkedList<ZbglTp>();
        List<ExportExcelDTO> expList = new ArrayList<ExportExcelDTO>();
        try {
            
            String trainTypeIDX = getRequest().getParameter("trainTypeIDX");
            String trainNo = getRequest().getParameter("trainNo");
            String professionalTypeName = getRequest().getParameter("professionalTypeName");
            String faultFixFullName = getRequest().getParameter("faultFixFullName");
            String faultName = getRequest().getParameter("faultName");
            String startDate = getRequest().getParameter("startDate");
            String overDate = getRequest().getParameter("overDate");
            String fields = StringUtil.nvlTrim(getRequest().getParameter("fields"), "");
            ColumnPattern[] col = JSONUtil.read(getRequest().getParameter("patterns"), ColumnPattern[].class);
            ColumnPattern[] deteailcol = JSONUtil.read(getRequest().getParameter("deteailpatterns"), ColumnPattern[].class);
            String flag = getRequest().getParameter("flag");
            ZbglTp z = new ZbglTp();
            z.setTrainTypeIDX(trainTypeIDX);
            z.setTrainNo(trainNo);
            z.setProfessionalTypeName(professionalTypeName);
            z.setFaultFixFullName(faultFixFullName);
            z.setFaultName(faultName);
            z.setStartDate(startDate);
            z.setOverDate(overDate);
            
            ExportExcelDTO dto1 = new ExportExcelDTO();
            list = getTpZhTjMapByFlag(1,z,fields);
            dto1.setPages(new Page<ZbglTp>(list.size(), list));
            dto1.setSheetName("主列表信息");
            dto1.setPattern(col);
            expList.add(dto1);
            
            //如果为TRUE才生成新的sheet如果没有那么就不生成
            if (Boolean.valueOf(flag)) {
                ExportExcelDTO dto2 = new ExportExcelDTO();
                listDeteail = getTpZhTjMapByFlag(2,z,"");
                Page<ZbglTp> page = new Page<ZbglTp>(listDeteail.size(), listDeteail);
                dto2.setPages(page);
                dto2.setSheetName("选定主表行数据明细列表信息");
                dto2.setPattern(deteailcol);
                expList.add(dto2);
            }
            
            ExcelExport.exportExcel(expList, "提票综合统计", getResponse());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            getResponse().getWriter().print("导出文件发生错误！");
        }
    }
    
    /**
     * <li>说明：根据flag标示返回对应提票List flag = 1 表示主表 flag = 2表示选择当前主表信息的明细
     * <li>创建人：林欢
     * <li>创建日期：2016-3-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param flag 标示，flag = 1 表示主表 flag = 2表示选择当前主表信息的明细
     * @param z ZbglTp对象，里面其实就是封装了查询参数
     * @param f 传入的字段，用于前台展示
     * @return List<ZbglTp> 结果集
     * @throws Exception
     */
    private List<ZbglTp> getTpZhTjMapByFlag(Integer flag,ZbglTp z,String f) throws Exception {
        List<ZbglTp> list = new LinkedList<ZbglTp>();
        HttpServletRequest req = getRequest();
        if (z == null) {
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            entity = (ZbglTp) JSONUtil.read(searchJson, entitySearch.getClass());
        }else {
            entity = z;
        }
        SearchEntity<ZbglTp> searchEntity = new SearchEntity<ZbglTp>(entity, 0, 999999999, getOrders());
        if (flag == 1) {
            //传入分组字段，也是返回字段
            list = this.manager.findTpPageListByParm(searchEntity,f);
        }else if (flag == 2) {
            list = this.manager.findTpDeteailPageListByParm(searchEntity);
        }
        return list;
    }
    
    
    
    /**
     * <li>说明：通过jt6整备单的主键idx，定位获取到这个idx代表的整备单的实体类对象，（控制器）
     * <li>创建人：刘国栋	
     * <li>创建日期：2016-08-08
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @param 
     * @param 
     * @return 
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void getZbglTpById() throws JsonMappingException, IOException{
    	
    	Map<String, Object> map = new HashMap<String, Object>();
        String jt6IDX = getRequest().getParameter("jt6IDX");
        try {
        	ZbglTp zbglTp = (ZbglTp)this.manager.getZbglTpById(jt6IDX);
        	map.put("tp",zbglTp);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    
    
    /**
     * <li>说明：保存并实例化提票，并将跟踪单idx主键传递给业务类
     * <li>创建人：刘国栋
     * <li>创建日期：2016-8-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveTpAndInstAndTrack() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String trackIDX = req.getParameter("trackIDX");
            String searchJson = StringUtil.nvlTrim(req.getParameter(Constants.ENTITY_JSON), "{}");
            ZbglTp[] entities = JSONUtil.read(searchJson, ZbglTp[].class);
            ZbglTp entity = entities.length > 0 ? entities[0] : null; // 取第一条提票信息
            if (entity != null) {
               this.manager.saveTpAndInstAndTrack(entities, entity, trackIDX);
                map.put(Constants.SUCCESS, true);
            }
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    
    /**
     * <li>说明：查询状态是TODO，待接活的所有提票
     * <li>创建人：刘国栋
     * <li>创建日期：2016-9-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findTpWhenToDo() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Page<ZbglTp> page = new Page<ZbglTp>();
        try {
        	String entityJson = StringUtil.nvl(getRequest().getParameter("entityJson"), "{}");
            ZbglTp entity = JSONUtil.read(entityJson, ZbglTp.class);
            SearchEntity<ZbglTp> searchEntity = new SearchEntity<ZbglTp>(entity, getStart(), getLimit(), getOrders());
            String startDate = StringUtil.nvlTrim(getRequest().getParameter("startDate"));
            String overDate = StringUtil.nvlTrim(getRequest().getParameter("overDate"));
            page = this.manager.findTpWhenToDo(searchEntity, startDate, overDate);
            map = page.extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    
    
    /**
     * <li>说明：查询状态是TODO和ONGOING，的所有提票
     * <li>创建人：刘国栋
     * <li>创建日期：2016-9-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findTpWhenToDoAndOnGoing() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Page<ZbglTp> page = new Page<ZbglTp>();
        try {
        	String entityJson = StringUtil.nvl(getRequest().getParameter("entityJson"), "{}");
            ZbglTp entity = JSONUtil.read(entityJson, ZbglTp.class);
            SearchEntity<ZbglTp> searchEntity = new SearchEntity<ZbglTp>(entity, getStart(), getLimit(), getOrders());
            String startDate = StringUtil.nvlTrim(getRequest().getParameter("startDate"));
            String overDate = StringUtil.nvlTrim(getRequest().getParameter("overDate"));
            page = this.manager.findTpWhenToDoAndOnGoing(searchEntity, startDate, overDate);
            map = page.extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    
}
