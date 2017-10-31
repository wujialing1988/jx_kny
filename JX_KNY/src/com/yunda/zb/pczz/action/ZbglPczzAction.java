package com.yunda.zb.pczz.action; 

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.third.poi.excel.ExcelExport;
import com.yunda.third.poi.excel.ExportExcelDTO;
import com.yunda.zb.pczz.entity.ZbglPczz;
import com.yunda.zb.pczz.manager.ZbglPczzManager;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglPczz控制器, 普查整治单
 * <li>创建人：王利成
 * <li>创建日期：2015-03-04
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class ZbglPczzAction extends JXBaseAction<ZbglPczz, ZbglPczz, ZbglPczzManager>{
    
    Logger logger = Logger.getLogger(this.getClass());
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * <li>说明：批量更新整治单状态
     * <li>创建人：王利成
     * <li>创建日期：2015-3-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void updateStatus() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            String status = getRequest().getParameter("status");
            this.manager.updateStatus(ids, status);
            map.put(Constants.SUCCESS, true);
            map.put(Constants.ERRMSG, "数据变更或未发布");
        }catch(Exception e){
            ExceptionUtil.process(e, logger, map);
        }finally{
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * 
     * <li>说明：普查整治查询（作业记录/归档记录）
     * <li>创建人：林欢
     * <li>创建日期：2016-8-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findZbglPczzPageList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            getRequest().getParameter("idx");
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            ZbglPczz objEntity = (ZbglPczz)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<ZbglPczz> searchEntity = new SearchEntity<ZbglPczz>(objEntity, getStart(), getLimit(), getOrders());
            map = this.manager.findZbglPczzPageList(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：查询整备普查整治导出
     * <li>创建人：林欢
     * <li>创建日期：2016-8-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings({ "unused", "unchecked" })
    public void exportZbglPczzListByParm() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            
            HttpServletRequest req = getRequest();
            String zbglPczzIDX = getRequest().getParameter("zbglPczzIDX");//普查计划idx
            
            //查询条件
            ZbglPczz zbglPczz = new ZbglPczz();
            zbglPczz.setIdx(zbglPczzIDX);
            
            //获取表格内容
            List<ExportExcelDTO> list = this.manager.exportZbglPczzListByParm(zbglPczz);
            //到处数据
            ExcelExport.synExportExcel(list, DateUtil.yyyyMMddHHmmss.format(new Date()) + "普查整治", getResponse());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            getResponse().getWriter().print("导出普查整治文件发生错误！");
        }
    }
}