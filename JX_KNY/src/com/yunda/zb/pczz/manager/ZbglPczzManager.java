package com.yunda.zb.pczz.manager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.Column;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.third.poi.excel.ColumnPattern;
import com.yunda.third.poi.excel.ExportExcelDTO;
import com.yunda.zb.pczz.entity.ZbglPczz;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglPczz业务类,普查整治计划
 * <li>创建人：王利成
 * <li>创建日期：2015-03-04
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */ 
@Service(value="zbglPczzManager")
public class ZbglPczzManager extends JXBaseManager<ZbglPczz, ZbglPczz>{
    
    
    /**ZbglPczzItemToTraininfoManager业务类,普查整治项中保存的机车信息**/
    @Resource
    private ZbglPczzItemToTraininfoManager zbglPczzItemToTraininfoManager;
    
    /**ZbglPczzItem业务类,普查整治计划项**/
    @Resource
    private ZbglPczzItemManager zbglPczzItemManager;

  /**
 * <li>说明：发布 
 * <li>创建人：王利成
 * <li>创建日期：2015-3-11
 * <li>修改人： 
 * <li>修改日期：
 * <li>修改内容：
 * @param ids 主键
 * @throws BusinessException
 * @throws NoSuchFieldException
 */
public void release(String[] ids) throws BusinessException, NoSuchFieldException{
      this.updateStatus(ids, ZbglPczz.STATUS_RELEASED);
  }
  
  /**
 * <li>说明：归档
 * <li>创建人：王利成
 * <li>创建日期：2015-3-11
 * <li>修改人： 
 * <li>修改日期：
 * <li>修改内容：
 * @param ids 主键
 * @throws BusinessException
 * @throws NoSuchFieldException
 */
public void backOrder(String[] ids) throws BusinessException, NoSuchFieldException{
      this.updateStatus(ids, ZbglPczz.STATUS_COMPLETE);
  }
    /**
     * <li>说明：批量更新普查整治单状态
     * <li>创建人：王利成
     * <li>创建日期：2015-3-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 普查整治单主键数组
     * @param status 要更新的状态
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateStatus(String[] ids, String status) throws BusinessException, NoSuchFieldException {
        for (String idx : ids) {
            ZbglPczz entity = this.getModelById(idx);
            
            //如果是发布，跟新发布时间
            if (ZbglPczz.STATUS_RELEASED.equals(status)) {
                entity.setReleaseDate(new Date());
            }
            
            this.updateStatus(entity, status);
        }
    }
    
    /**
     * <li>说明：更新普查整治单状态
     * <li>创建人：王利成
     * <li>创建日期：2015-3-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 普查整治单实体
     * @param status 要更新的状态
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    private void updateStatus(ZbglPczz entity, String status) throws BusinessException, NoSuchFieldException {
        if (status.equals(entity.getStatus())) {
            return;
        }
        if (ZbglPczz.STATUS_RELEASED.equals(status) && !ZbglPczz.STATUS_TORELEASE.equals(entity.getStatus())) {
            return;
        }
        if (ZbglPczz.STATUS_COMPLETE.equals(status) && !ZbglPczz.STATUS_RELEASED.equals(entity.getStatus())) {
            return;
        }
        entity.setStatus(status);
        this.saveOrUpdate(entity);
    }
    
    /**
     * <li>说明：统计进行中的普查整治数据
     * <li>创建人：王利成
     * <li>创建日期：2015-3-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx 车型主键
     * @param trainNo 车号
     * @return Integer
     */
    @SuppressWarnings("unchecked")
    public Integer getPczzOngingCount(String trainTypeIdx,String trainNo){
        String sql = "";
        StringBuilder sb = new StringBuilder(); 
        sql= SqlMapUtil.getSql("zb-pczz:queryPczzPageList");
        sb.append(sql);
        if(!StringUtil.isNullOrBlank(trainTypeIdx)){
            sb.append(" and train_type_idx = '").append(trainTypeIdx).append("'");
        }
        if(!StringUtil.isNullOrBlank(trainNo)){
            sb.append(" and train_no = '").append(trainNo).append("'");
        }
       
        StringBuilder totalSql = new StringBuilder("SELECT COUNT(*) FROM (").append(sb).append(")");
        List<BigDecimal> list = daoUtils.executeSqlQuery(totalSql.toString());
        return list.get(0).intValue();
    }
    
    
    /**
     * <li>说明：统计已经完成的普查整治数据
     * <li>创建人：王利成
     * <li>创建日期：2015-3-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx 车型主键
     * @param trainNo 车号
     * @return Integer
     */
    @SuppressWarnings("unchecked")
    public Integer getPczzCompleteCount(String trainTypeIdx,String trainNo){
        String sql = "";
        StringBuilder sb = new StringBuilder(); 
        sql = SqlMapUtil.getSql("zb-pczz:queryPczzingPageList"); 
        sb.append(sql);
        if(!StringUtil.isNullOrBlank(trainTypeIdx)){
            sb.append(" and train_type_idx = '").append(trainTypeIdx).append("'");
        }
        if(!StringUtil.isNullOrBlank(trainNo)){
            sb.append(" and train_no = '").append(trainNo).append("'");
        }
        StringBuilder totalSql = new StringBuilder("SELECT COUNT(*) FROM (").append(sb).append(")");
        List<BigDecimal> list = daoUtils.executeSqlQuery(totalSql.toString());
        return list.get(0).intValue();
    }

    /**
     * <li>说明：根据范围活idx查询普查整治list
     * <li>创建人：林欢
     * <li>创建日期：2016-8-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param zbglRdpWiIDX 范围活idx
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public List<ZbglPczz> getZbglPczzByZbglRdpWiIDX(String zbglRdpWiIDX) {
        StringBuffer sb = new StringBuffer();
        sb.append(" from ZbglPczz a where a.recordStatus = 0 and a.idx = '").append(zbglRdpWiIDX).append("'");
        return (List<ZbglPczz>) find(sb.toString());
    }
    
    /**
     * 
     * <li>说明：普查整治查询（作业记录/归档记录）
     * <li>创建人：林欢
     * <li>创建日期：2016-8-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询对象封装
     * @return Page<ZbglPczz> 分页查询结果
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     * @throws Exception
     */
    public Page<ZbglPczz> findZbglPczzPageList(SearchEntity<ZbglPczz> searchEntity) throws SecurityException, NoSuchFieldException {
        ZbglPczz entity = searchEntity.getEntity();
        
        StringBuilder sb = new StringBuilder();
        
        sb.append(" select a.* ");
        sb.append(" from zb_zbgl_pczz a where a.record_status = 0 ");
        
        //普查计划名称
        if (StringUtils.isNotBlank(entity.getPczzName())) {
            sb.append(" and a.pczz_name like '%").append(entity.getPczzName()).append("%'");
        }
        
        //普查发布开始时间
        if (entity.getReleaseStartDate() != null) {
            sb.append(" and to_char(a.release_date,'yyyy-mm-dd')>='").append(DateUtil.yyyy_MM_dd.format(entity.getReleaseStartDate())).append("'");
        }
        
        //普查发布结束时间
        if (entity.getReleaseEndDate() != null){
            sb.append(" and to_char(a.release_date,'yyyy-mm-dd')<='").append(DateUtil.yyyy_MM_dd.format(entity.getReleaseEndDate())).append("'");
        }
        
        //普查开始时间
        if (entity.getStartDate() != null) {
            sb.append(" and to_char(a.start_date,'yyyy-mm-dd')>='").append(DateUtil.yyyy_MM_dd.format(entity.getStartDate())).append("'");
        }
        
        //普查结束时间
        if (entity.getEndDate() != null){
            sb.append(" and to_char(a.start_date,'yyyy-mm-dd')<='").append(DateUtil.yyyy_MM_dd.format(entity.getEndDate())).append("'");
        }
        
        //作业情况
        if (StringUtils.isNotBlank(entity.getWorkStatusString())) {
            sb.append(" and a.work_status in (").append(entity.getWorkStatusString()).append(")");
        }
        
        //任务状态
        if (StringUtils.isNotBlank(entity.getStatus())) {
            sb.append(" and a.status in ('").append(entity.getStatus().replace(",", "','")).append("')");
        }
        
        Object[] orders = searchEntity.getOrders();
        // 排序处理
        if (null != orders && orders.length > 0) {
            String[] order = orders[0].toString().split(" ");
            String sort = order[0];

            //前台传递过来的排序方式 desc或者asc
            String dir = order[1];
            Class clazz = ZbglPczz.class;
            //通过传递过来需要排序的字段反射字段对象
            Field field = clazz.getDeclaredField(sort);
            //获取字段上，标签上的列名
            Column annotation = field.getAnnotation(Column.class);
            if (null != annotation) {
                sb.append(" order by a.").append(annotation.name()).append(" ").append(dir);
            } else {
                sb.append(" order by a.").append(sort).append(" ").append(dir);
            }
        } 
        //此处的总数别名必须是ROWCOUNT，封装方法有规定
        String totalSql = " select count(*) as rowcount " + sb.substring(sb.indexOf(" from "));
        return this.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, ZbglPczz.class);
    }

    /**
     * 
     * <li>说明：通过idx查询发布的普查整治计划
     * <li>创建人：林欢
     * <li>创建日期：2016-8-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param zbglPczzIDX 普查整治计划idx
     * @return ZbglPczz 普查整治计划对象
     */
    public ZbglPczz getReleasedZbglPczzByZbglPczzIDX(String zbglPczzIDX) {
        StringBuffer sb = new StringBuffer();
        sb.append(" from ZbglPczz a where a.recordStatus = 0 and a.status = '").append(ZbglPczz.STATUS_RELEASED).append("'");
        
        sb.append(" and a.startDate <= sysdate ");//开始时间比当前时间小
        sb.append(" and nvl(a.endDate,sysdate) >= sysdate ");//结束时间比当前时间大
        
        return this.findSingle(sb.toString());
    }

	
    
	/**
     * <li>说明：查询需要做普查整治任务单的总数（未做和未完成）（业务类）
     * <li>创建人：刘国栋	
     * <li>创建日期：2016-08-25
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws Exception 
     */
	@SuppressWarnings("unchecked")
    public int getPCZZCount(String searchJson) throws Exception, JsonMappingException, IOException {
		 Map<String, String> queryMap = new HashMap<String, String>();        
	        if (!StringUtil.isNullOrBlank(searchJson)) {
	            queryMap = JSONUtil.read(searchJson, Map.class); 
	        }
	        StringBuffer fromSb = new StringBuffer();
	        
	        fromSb.append(CommonUtil.buildParamsHql(queryMap));
	        fromSb.append(" order by updateTime desc ");
	        String totalHql = "select count(idx) from ZbglPczzWI where 1=1 and wIStatus <> 'CHECKED' and recordStatus = 0 ".concat(fromSb.toString());
	        
	        List list = daoUtils.find(totalHql);
	        return Integer.parseInt(list.get(0).toString());
	}

    /**
     * <li>说明：查询整备普查整治导出
     * <li>创建人：林欢
     * <li>创建日期：2016-8-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param zbglPczz 查询参数
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     */
    @SuppressWarnings("unchecked")
    public List<ExportExcelDTO> exportZbglPczzListByParm(ZbglPczz zbglPczz) throws SecurityException, NoSuchFieldException {
        List<ExportExcelDTO> list = new ArrayList<ExportExcelDTO>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("zbglPczzIDX", zbglPczz.getIdx());
        
        //获取普查整治中间表的所有车型
        List<String> trainTypeShortNameList = zbglPczzItemToTraininfoManager.findAllTrainTpye(map);
        for (String trainTpyeShortName : trainTypeShortNameList) {
            
            ExportExcelDTO dto = new ExportExcelDTO();
            
            //通过车型获取每个车型的动态普查整治计划项
            List<String> zbglPczzItemNameList = zbglPczzItemManager.findAllZbglPczzItem(trainTpyeShortName);
            //当前车型的head
            List<ColumnPattern> columnPatternThisList = new ArrayList<ColumnPattern>();
            columnPatternThisList.add(new ColumnPattern("车型","trainTypeShortname"));
            columnPatternThisList.add(new ColumnPattern("车号","trainNo"));
            //拼装动态的head普查整治任务项
            Integer num = 0;
            for (String string : zbglPczzItemNameList) {
                columnPatternThisList.add(new ColumnPattern(string,string));
                num++;
            }
            columnPatternThisList.add(new ColumnPattern("普查时间","itemTime"));
            columnPatternThisList.add(new ColumnPattern("区域班组","handleOrgname"));
            columnPatternThisList.add(new ColumnPattern("普查人","handlePersonName"));
            columnPatternThisList.add(new ColumnPattern("确认人","checkPersonName"));
            
            //根据普查整治计划idx和车型查询该车型下所有车的普查整治计划项完成情况(数据)
            List<Map<String, String>> dataList = zbglPczzItemManager.findDynamicZbglPczzWiItemList(zbglPczzItemNameList,trainTpyeShortName,zbglPczz.getIdx());
        
            dto.setPages(new Page<Map<String, String>>(dataList.size(),dataList));//数据
            dto.setPattern(columnPatternThisList.toArray(new ColumnPattern[]{}));//表头格式
            dto.setSheetName(trainTpyeShortName);//sheet名称
            list.add(dto);
        }
        return list;
        
    }
}