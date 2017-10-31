package com.yunda.baseapp.workcalendar.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.baseapp.workcalendar.entity.WorkCalendarBean;
import com.yunda.baseapp.workcalendar.entity.WorkCalendarDetail;
import com.yunda.baseapp.workcalendar.entity.WorkCalendarInfo;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.entity.EosDictEntryId;
import com.yunda.frame.yhgl.manager.EosDictEntrySelectManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：谭诚
 * <li>创建日期：2013-7-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="workCalendarInfoManager")
public class WorkCalendarInfoManager extends JXBaseManager<WorkCalendarInfo, WorkCalendarInfo>{
	/** 日志工具 */
//	@SuppressWarnings("unused")
//	private Logger logger = Logger.getLogger(getClass().getName());
    /*
     * 日历明细业务类
     */
    @Resource
    private WorkCalendarDetailManager workCalendarDetailManager ;
	/**
	 * 
	 * <li>说明：根据工作日历模板表Idx及用户所选的某一天,查找其明细信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-6-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param mainTableIdx 主表Idx
     * 		  gzrl 用户选择的工作日历日期
	 * @return List<ProfessionalType> 返回集合
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
    public WorkCalendarInfo findDefaultWorkCalendarInfo(String mainTableIdx){
        String hql ;
        List <WorkCalendarInfo> list = new ArrayList<WorkCalendarInfo>();
        //mainTableIdx为空，查询默认日历信息
        if(StringUtil.isNullOrBlank(mainTableIdx)){
            hql = SqlMapUtil.getSql("basic-org:findDefaultWorkCalendarInfo");
            list = daoUtils.getHibernateTemplate().find(hql);
        }else{
        //mainTableIdx不为空，查询特定工作日历信息
            hql = SqlMapUtil.getSql("basic-org:findWorkCalendarInfoByIdx");
            Object[] param = new Object[]{mainTableIdx};//工作日历主表Idx
            list = daoUtils.getHibernateTemplate().find(hql, param);
        }
		if(list == null||list.size()<1){
			return null;
		}
		WorkCalendarInfo info = list.get(0);
        info = transEndTime(info);
		return info;
	}
    /**
     * 
     * <li>说明：查询工作日历列表
     * <li>创建人：程梅
     * <li>创建日期：2015-4-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询字段
     * @param start 开始行
     * @param limit 每页记录数
     * @param orders 排序对象
     * @return Page<WorkCalendarInfo>
     */
    @SuppressWarnings("unchecked")
    public Page<WorkCalendarInfo> findCalendarInfoList(String searchJson,int start,int limit,Order[] orders){
        
        String sql=SqlMapUtil.getSql("basic-org:queryCalendarInfoList");
        String totalSql = "select count(1) " + sql.substring(sql.indexOf("from"));
        return super.findPageList(totalSql, sql, start , limit, searchJson, orders);
    }
    /**
     * 
     * <li>说明：级联删除日历主表和明细信息
     * <li>创建人：程梅
     * <li>创建日期：2015-4-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids  日历id
     */
    public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
        List<WorkCalendarInfo> entityList = new ArrayList<WorkCalendarInfo>();
        for (Serializable id : ids) {
            //逻辑删除日历明细
            List<WorkCalendarDetail> detailList = workCalendarDetailManager.getModeList(id.toString());
            if(detailList != null && detailList.size() > 0){
                detailList = EntityUtil.setDeleted(detailList);
                detailList = EntityUtil.setSysinfo(detailList);
                this.daoUtils.getHibernateTemplate().saveOrUpdateAll(detailList);
            }
            WorkCalendarInfo t = getModelById(id);
            t = EntityUtil.setSysinfo(t);
//          设置逻辑删除字段状态为已删除
            t = EntityUtil.setDeleted(t);
            entityList.add(t);
        }
        this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
    }
    
    /**
     * <li>说明：重写新增方法
     * <li>创建人：程锐
     * <li>创建日期：2015-7-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t 工作日历实体  
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveOrUpdate(WorkCalendarInfo t) throws BusinessException, NoSuchFieldException {
        t = changeEndTime(t);
        super.saveOrUpdate(t);
    }
    
    /**
     * <li>说明：前台显示时将23:59:59转换为00:00:00
     * <li>创建人：程锐
     * <li>创建日期：2015-7-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t 工作日历实体
     * @return 工作日历实体
     */
    public WorkCalendarInfo transEndTime(WorkCalendarInfo t) {
        return workCalendarDetailManager.transEndTime(t);
    }
    
    /**
     * <li>说明：设定为00:00:00的结束时间为23:59:59存入数据库
     * <li>创建人：程锐
     * <li>创建日期：2015-7-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t 工作日历实体
     * @return 工作日历实体
     */
    private WorkCalendarInfo changeEndTime(WorkCalendarInfo t) {
        String ed1 = t.getPeriod1End();
        String ed2 = t.getPeriod2End();
        String ed3 = t.getPeriod3End();
        String ed4 = t.getPeriod4End();
        String sd2 = t.getPeriod2Begin();
        String sd3 = t.getPeriod3Begin();
        String sd4 = t.getPeriod4Begin();
        if (WorkCalendarBean.ZEROTIME.equals(ed1)) {
            t.setPeriod1End(WorkCalendarBean.TWENTYFOURTIME);
        } else if (!WorkCalendarBean.ZEROTIME.equals(sd2) && WorkCalendarBean.ZEROTIME.equals(ed2)) {
            t.setPeriod2End(WorkCalendarBean.TWENTYFOURTIME);
        } else if (!WorkCalendarBean.ZEROTIME.equals(sd3) && WorkCalendarBean.ZEROTIME.equals(ed3)) {
            t.setPeriod3End(WorkCalendarBean.TWENTYFOURTIME);
        } else if (!WorkCalendarBean.ZEROTIME.equals(sd4) && WorkCalendarBean.ZEROTIME.equals(ed4)) {
            t.setPeriod4End(WorkCalendarBean.TWENTYFOURTIME);
        }
        return t;
    }
    
    /**
     * <li>说明： 设置指定的工作日历为默认工作日历
     * <li>创建人：何涛
     * <li>创建日期：2015-8-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 工作日历idx主键
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateToDefault(String idx) throws BusinessException, NoSuchFieldException {
        // 取消以前的默认工作日历
        String hql = "From WorkCalendarInfo Where recordStatus = 0 And isDefault = ?";
        WorkCalendarInfo oldDefaultWCI = (WorkCalendarInfo) this.daoUtils.findSingle(hql, new Object[]{ WorkCalendarInfo.CONST_STR_IS_DEFAULT_YES });
        if(null != oldDefaultWCI){
            oldDefaultWCI.setIsDefault(WorkCalendarInfo.CONST_STR_IS_DEFAULT_NO);
            this.saveOrUpdate(oldDefaultWCI);
        }
        // 设置已选择的工作日历为默认
        WorkCalendarInfo entity = this.getModelById(idx);
        entity.setIsDefault(WorkCalendarInfo.CONST_STR_IS_DEFAULT_YES);
        this.saveOrUpdate(entity);
     
    }
    
    /**
     * <li>说明：获取工作日历所有记录
     * <li>创建人：张迪
     * <li>创建日期：2017-1-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 工作日历信息
     * @throws Exception
     */
    public static String getAllRecordJSON() throws Exception {
        WorkCalendarInfoManager manager =
            (WorkCalendarInfoManager) Application.getSpringApplicationContext().getBean("workCalendarInfoManager");
        List<WorkCalendarInfo> entryList = manager.getAll();
        if (entryList == null || entryList.size() < 1) {
            return "[]";
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (WorkCalendarInfo entry : entryList) {
            Map<String, Object> entryMap = new HashMap<String, Object>();
            entryMap.put("idx", entry.getIdx());
            entryMap.put("calendarName", entry.getCalendarName());
            list.add(entryMap);
        }
        return JSONUtil.write(list);
    }
}
