package com.yunda.baseapp.workcalendar.manager;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.baseapp.workcalendar.entity.WorkCalendarBean;
import com.yunda.baseapp.workcalendar.entity.WorkCalendarDetail;
import com.yunda.baseapp.workcalendar.entity.WorkCalendarInfo;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.util.IWorkCalendar;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkCalendarDetail业务类,
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-06-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="workCalendarDetailManager")
public class WorkCalendarDetailManager extends JXBaseManager<WorkCalendarDetail, WorkCalendarDetail> implements IWorkCalendar{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
//	private long workBeginDay = 0;//起始时间
	
	//private List list = null;
	
	private long _beginWorkDateForMillisecond = 0;//工作开始时间毫秒类型
	private long _endWorkDateForMillisecond = 0;//工作结束时间毫秒类型
	private Date _beginWorkDateForDateType = null; //工作开始时间Date类型
	//private String _beginWorkDateForStringType = null; //工作开始时间String类型  //未使用的变量
	private Object []  _defaultEveryDayWorkTime = null; //默认的每日工作时间,即工作日历主表中的时间参数
	@SuppressWarnings("unchecked")
    private List _searchScopeList = null; //在工作日历明细表中检索的时间范围,即从Y年M月D日~X年Y月N日的所有工作日期的时间安排
	private Date _lastOverTime = new Date(); //最终完成日期
	private long _thisWheelEndTime = 0; //本轮工作的结束时间
	private long _countWrokTime = 0;//总工时
    
    /**
     * 日历服务
     */
    @Resource
    private WorkCalendarInfoManager workCalendarInfoManager;
	
	/**
	 * <li>说明：新增或更新一组实体对象，该方法只适用于检修系统v2.0的数据模型实体类，统一设置业务无关的五个字段（创建人、
	 * 创建时间、修改人、修改时间、站点标识）
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-08-07
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 实体对象
	 */	
	public void delWorkTime(WorkCalendarDetail t) throws BusinessException, NoSuchFieldException {
		t = EntityUtil.setSysinfo(t);
		//设置逻辑删除字段状态为未删除
		t = EntityUtil.setDeleted(t);
		this.daoUtils.getHibernateTemplate().saveOrUpdate(t);
	}
    
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
	 */
	@SuppressWarnings("unchecked")
    public WorkCalendarDetail findWorkCalendarDetail(String mainTableIdx, String gzrl){
		String hql = SqlMapUtil.getSql("basic-org:findWorkCalendarDetail");
		Object[] param = new Object[]{mainTableIdx,//工作日历主表Idx
									  gzrl};     //当前用户所选择的日期
				 					  
		List <WorkCalendarDetail> list = daoUtils.getHibernateTemplate().find(hql,param);
		if(list == null||list.size()<1){
			return null;
		}
		WorkCalendarDetail detail = list.get(0);
		
		return detail;
	}
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
		String hql = SqlMapUtil.getSql("basic-org:findWorkCalendarInfoByIdx");
		Object[] param = new Object[]{mainTableIdx};//工作日历主表Idx
				 					  
		List <WorkCalendarInfo> list = daoUtils.getHibernateTemplate().find(hql,param);
		if(list == null||list.size()<1){
			return null;
		}
		WorkCalendarInfo info = list.get(0);
		info = transEndTime(info);
		return info;
	}
	/**
	 * 
	 * <li>说明：根据工作日历模板表中的年和月信息, 查询该月所有日期的数据
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-6-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param mainTableIdx 主表Idx
     * 		  gzrl 用户选择的工作日历日期
	 * @return List<ProfessionalType> 返回集合
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
    public List<WorkCalendarDetail> findCurrentCalendar(String mainTableIdx, String yearAndMonth){
		StringBuilder hql = new StringBuilder();
		hql.append("from WorkCalendarDetail where infoIdx = '");
		hql.append(mainTableIdx);
		hql.append("' and calDate like '");
		hql.append(yearAndMonth.concat("%"));
		hql.append("' and recordStatus = 0 order by calDate");
		return daoUtils.find(hql.toString());
	}
	/**************************************************************************
	 *                  工作日历算法: 通过开始时间和工期推算结束日期
	 **************************************************************************/
	
	/**
     * 
     * <li>说明：根据开始时间及标准工期,计算工作日历的结束时间
     * <li>创建人：谭诚
     * <li>创建日期：2013-6-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param procNme 存储过程名称
     * @param startTime 开始日期
     * @param timeInterval 标准工期
     * @return 结束时间的long型数据
     */
    public long getFinalTime(long startTime, long timeInterval){
    	//调用函数,设置各项初始参数
    	this.initAnyParameters(startTime, timeInterval, null);
    	this.mainJs(this._beginWorkDateForDateType, timeInterval);
    	return this._lastOverTime.getTime();
    }
    
    private void mainJs(Date thisDate,long timeInterval){
    	long s = 0;
    	int nextDay = 0; //下一个工作日是几天以后
    	//1. 将这一天Date类型转换为String类型,且按照yyyyMMdd格式转换,只保留年月日部分
    	String jt = dateConvertToString(thisDate,"yyyyMMdd");
    	Object [] jl = null;
    	//在工作日历中搜索这一天的工作安排
    	for(int i=0;i<this._searchScopeList.size();i++){
    		Object [] obj = (Object [])this._searchScopeList.get(i);
    		//如果在工作记录明细时间安排表中找到了与该日对应的记录,则开始计算本日工时
    		if(!StringUtil.isNullOrBlank(String.valueOf(obj[0]))&&String.valueOf(obj[0]).equals(jt)){
    			jl = obj;
    			break;
    		} 
    	}
    	//如果在工作记录明细时间安排表中找到了与该日对应的记录,则开始计算本日工时
    	if(jl != null){
    		s = gsjs(thisDate,jl,timeInterval);
    		//是否已经完成了
    		if(timeInterval>s){
    			nextDay = 1; //未完成,明天继续
    		} else { 
    			nextDay = 0; //今日已完成
    		}
    	} 
    	//如果在工作记录明细时间安排表中没有找到与该日对应的记录,则分为了多种情况
    	else {
    		//周末和节日放在检索list之后,虽然会降低效率,但是可以防止节日加班的情况发生
    		//======判断周末=======验证该日是否为周末,如果是周末,则跳过
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(thisDate);
    		int p = cal.get(Calendar.DAY_OF_WEEK);
    		//======判段节日=======
    		Calendar today = Calendar.getInstance();
    		today.setTime(thisDate);// 加载当前日期
    		Lunar lunar = new Lunar(today);
    		Festival ft = new Festival();// 创建节日对象。
//    		//验证该日是否为节假日(公历),如果是,则跳过
    		String jrNameGL = ft.showSFtv(today.get(Calendar.MONTH)+1, today.get(Calendar.DATE));
    		//验证该日是否为节假日(农历),如果是,则跳过
    		String jrNameNL = lunar.LunarFestival();
    		/**
    		 * 判断周末节日或者是默认工作时间
    		 */
    		//节日
    		if(!StringUtil.isNullOrBlank(jrNameNL)&&
    				("端午节".equals(jrNameNL)||"中秋节".equals(jrNameNL)||"除夕".equals(jrNameNL))||"春节".equals(jrNameNL)){
    			if("端午节".equals(jrNameNL)||"中秋节".equals(jrNameNL)||"除夕".equals(jrNameNL)){
    				//端午\中秋\除夕放假一天
	    			nextDay = 1;
    			}else if("春节".equals(jrNameNL)){
    				//春节放假三天
    				nextDay = 3;
    			}
    		} 
    		else if(!StringUtil.isNullOrBlank(jrNameGL)&&
    				("元旦".equals(jrNameGL)||"国际劳动节".equals(jrNameGL)||"国庆节".equals(jrNameGL))){
	    		if("元旦".equals(jrNameGL)||"国际劳动节".equals(jrNameGL)){
	    			//元旦节\劳动节放假一天
					nextDay = 1;
	    		} else if("国庆节".equals(jrNameGL)){
	    			//国庆节放假三天
	    			nextDay = 3;
	    		}
    		}
    		// 1是星期日  7是星期六
    		else if(p==7||p==1){
    			nextDay = 1;
    		} 
    		else {
    			//以上均不满足,则根据默认工作日历安排
    			s = gsjs(thisDate,this._defaultEveryDayWorkTime,timeInterval);
    			//是否已经完成了
        		if(timeInterval>s){
        			nextDay = 1; //未完成,明天继续
        		} else { 
        			nextDay = 0; //今日已完成
        		}
    		}
    	}
    	//是否还需要下一个工作日继续执行
    	if(nextDay>0){
    		//参数1:求得下一天<这里需要将thisDate转为00:00:00>, 参数2:总共时减去本日工时
    		Format format = new SimpleDateFormat("yyyy-MM-dd");
    		Date h = null;
			try {
				h = (java.util.Date) format.parseObject(dateConvertToString(thisDate,"yyyy-MM-dd"));
			} catch (ParseException e) {
				ExceptionUtil.process(e, logger);
				h = thisDate;
			}
    		
//    		System.out.println("今日"+dateConvertToString(thisDate,"yyyy-MM-dd HH:mm:ss")+"完成:"+s+"\t剩余:"+(timeInterval-s));
    		mainJs(afterNDay(h,nextDay),timeInterval-s); 
    	} else {
    		//本日能够完成所有工作任务.
    		return;
    	}
    }
    
    
    /**
     * 
     * <li>说明：方法实现功能说明
     * <li>创建人：谭诚
     * <li>创建日期：2013-6-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param date 工作当日
     *        syTimeInterval 剩余的总工作时间(毫秒)
     *        obj 工作当日的工作日历时间安排
     * @return 返回值说明 返回本日总工作时间(毫秒)
     * @throws 抛出异常列表
     */
    @SuppressWarnings("unchecked")
    private long gsjs(Date date, Object [] obj,long syTimeInterval){
      	long s = 0;
      	long temp = syTimeInterval;
    	Map map = new HashMap();
    	List list = new ArrayList();
    	String day = String.valueOf(obj[0]==null?dateConvertToString(date,"yyyyMMdd"):obj[0]);
    	//如果第一组工作时间的开始时间和结束时间都不等于空,并且开始时间与结束时间不相等
    	if(!StringUtil.isNullOrBlank(String.valueOf(obj[2]))
    			&&!StringUtil.isNullOrBlank(String.valueOf(obj[3]))
    			&&!String.valueOf(obj[2]).equals(String.valueOf(obj[3]))){
    		map = new HashMap();
    		map.put("bg", stringConvertToMillisecond(day.concat(" ").concat(String.valueOf(obj[2])),"yyyyMMdd HH:mm:ss"));
    		map.put("ed", transEndTime(day, String.valueOf(obj[3])));
    		map.put("period", obj[10]);//第一组工作时间的总工时
    		list.add(map);
    	}
    	//如果第二组工作时间的开始时间和结束时间都不等于空,并且开始时间与结束时间不相等
    	if(!StringUtil.isNullOrBlank(String.valueOf(obj[4]))
    			&&!StringUtil.isNullOrBlank(String.valueOf(obj[5]))
    			&&!String.valueOf(obj[4]).equals(String.valueOf(obj[5]))){
    		map = new HashMap();
    		map.put("bg", stringConvertToMillisecond(day.concat(" ").concat(String.valueOf(obj[4])),"yyyyMMdd HH:mm:ss"));
    		map.put("ed", transEndTime(day, String.valueOf(obj[5])));
    		map.put("period", obj[11]);//第二组工作时间的总工时
    		list.add(map);
    	}
    	//如果第三组工作时间的开始时间和结束时间都不等于空,并且开始时间与结束时间不相等
    	if(!StringUtil.isNullOrBlank(String.valueOf(obj[6]))
    			&&!StringUtil.isNullOrBlank(String.valueOf(obj[7]))
    			&&!String.valueOf(obj[6]).equals(String.valueOf(obj[7]))){
    		map = new HashMap();
    		map.put("bg", stringConvertToMillisecond(day.concat(" ").concat(String.valueOf(obj[6])),"yyyyMMdd HH:mm:ss"));
    		map.put("ed", transEndTime(day, String.valueOf(obj[7])));
    		map.put("period", obj[12]);//第三组工作时间的总工时
    		list.add(map);
    	}
    	//如果第四组工作时间的开始时间和结束时间都不等于空,并且开始时间与结束时间不相等
    	if(!StringUtil.isNullOrBlank(String.valueOf(obj[8]))
    			&&!StringUtil.isNullOrBlank(String.valueOf(obj[9]))
    			&&!String.valueOf(obj[8]).equals(String.valueOf(obj[9]))){
    		map = new HashMap();
    		map.put("bg", stringConvertToMillisecond(day.concat(" ").concat(String.valueOf(obj[8])),"yyyyMMdd HH:mm:ss"));
    		map.put("ed", transEndTime(day, String.valueOf(obj[9])));
    		map.put("period", obj[13]);//第一组工作时间的总工时
    		list.add(map);
    	}
    	Map m2 = new HashMap();
    	for(int i=0;i<list.size();i++){
    		m2 = (HashMap)list.get(i);
    		//如果时间早于本阶段工作开始时间,并且本阶段尚不能完成剩余工作,则累加本时间段工时
    		if(date.getTime()<= Long.parseLong(String.valueOf(m2.get("bg")))
    				&&temp>Long.parseLong(String.valueOf(m2.get("period")))){
    			s += Long.parseLong(String.valueOf(m2.get("period"))); //累加至本日工时
    			temp -= Long.parseLong(String.valueOf(m2.get("period"))); //更新剩余总工时
    		} 
    		//如果时间恰好处于本阶段工作开始和结束时间范围内,且本阶段尚不能完成剩余工作,则累加本阶段截止时间与开始时间的差
    		else if(date.getTime()>=Long.parseLong(String.valueOf(m2.get("bg")))
    				&&date.getTime()<=Long.parseLong(String.valueOf(m2.get("ed")))
    				//&&temp>Long.parseLong(String.valueOf(m2.get("period")))
    				&&temp> (Long.parseLong(String.valueOf(m2.get("ed")))- date.getTime())
    				){
    			s+= (Long.parseLong(String.valueOf(m2.get("ed")))-date.getTime()); //本阶段结束时间-当前开始时间后累计至本日总工时
    			temp -= (Long.parseLong(String.valueOf(m2.get("ed")))-date.getTime()); //更新剩余总工时
    		} 
    		//如果时间晚于本阶段工作结束时间,则跳过本轮循环
    		else if(date.getTime()>=Long.parseLong(String.valueOf(m2.get("ed")))){
    			continue;
    		} 
    		//以上条件均不满足,说明本轮工作时间足够完成剩余工时的任务,则直接累加
    		else {
    			s+= temp;
    			//如果本轮开工时间早于当前时间,说明是中途开始的, 最终完成时间应为当前时间+剩余总工时  
    			Long.parseLong(String.valueOf(m2.get("ed")));
    			if(Long.parseLong(String.valueOf(m2.get("bg")))<date.getTime()){
    				this._lastOverTime = DateUtil.getDateByMillSeconds(date.getTime()+temp);
    			} else {
    				this._lastOverTime = DateUtil.getDateByMillSeconds((Long.parseLong(String.valueOf(m2.get("bg")))+temp));
    			}
    			temp-=temp;
    		}
    		
    		if(temp == 0){
    			break;
    		}
    	}
		return s;
    }
    /**
     * 
     * <li>说明：初始化各项属性
     * <li>创建人：谭诚
     * <li>创建日期：2013-6-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param startDate 开始日期
     * @param timeInterval 工期（分钟）
     * @param infoIdx 工作日历id
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    @SuppressWarnings({ "static-access", "unchecked" })
    private void initAnyParameters(long startTime, long timeInterval, String infoIdx){
    	this._beginWorkDateForMillisecond = startTime; //将工作开始时间(毫秒)赋值到全局
    	this._beginWorkDateForDateType = DateUtil.getDateByMillSeconds(startTime);//将工作开始时间转为Date类型后赋值到全局
        
//      2015-6-3 程锐修改为从缓存Map中获取这个时间段范围内的所有日历记录
        WorkCalendarInfoUtil wcInfoUtil = WorkCalendarInfoUtil.getInstance(this._beginWorkDateForDateType);
        if (wcInfoUtil.wcInfoMap.isEmpty())
            wcInfoUtil.buildMap();
        if (wcInfoUtil.wcInfoMap.isEmpty()) {
            this._defaultEveryDayWorkTime = getDefaultEveryDayWorkTime(infoIdx);
            Date maybeEndDate = afterNDay(this._beginWorkDateForDateType,
                getPreliminaryEstimateEndDate(timeInterval,Long.parseLong(String.valueOf(this._defaultEveryDayWorkTime[14]))));
            this._searchScopeList = getStartToEndWorkTimeInfo(dateConvertToString(this._beginWorkDateForDateType,"yyyyMMdd"),
                                                              dateConvertToString(maybeEndDate,"yyyyMMdd"), infoIdx);            
            return;
        }
        WorkCalendarBean wcBean = wcInfoUtil.wcInfoMap.get(infoIdx);
        if (wcBean == null && !StringUtil.isNullOrBlank(infoIdx)) {
            this._defaultEveryDayWorkTime = getDefaultEveryDayWorkTime(infoIdx);
            Date maybeEndDate = afterNDay(this._beginWorkDateForDateType,
                getPreliminaryEstimateEndDate(timeInterval,Long.parseLong(String.valueOf(this._defaultEveryDayWorkTime[14]))));
            this._searchScopeList = getStartToEndWorkTimeInfo(dateConvertToString(this._beginWorkDateForDateType,"yyyyMMdd"),
                                                              dateConvertToString(maybeEndDate,"yyyyMMdd"), infoIdx); 
            return;
        }
        if (wcBean.getDefaultEveryDayWorkTime() == null || wcBean.getDefaultEveryDayWorkTime().length < 1)
            this._defaultEveryDayWorkTime = getDefaultEveryDayWorkTime(infoIdx);
        else
            this._defaultEveryDayWorkTime = wcBean.getDefaultEveryDayWorkTime();
        Date maybeEndDate = afterNDay(this._beginWorkDateForDateType,
            getPreliminaryEstimateEndDate(timeInterval,Long.parseLong(String.valueOf(this._defaultEveryDayWorkTime[14]))));
        List<Object[]> searchScopeList = wcBean.getSearchScopeList();
//        if (searchScopeList == null || searchScopeList.size() < 1) {
//            this._searchScopeList = getStartToEndWorkTimeInfo(dateConvertToString(this._beginWorkDateForDateType,"yyyyMMdd"),
//                                                              dateConvertToString(maybeEndDate,"yyyyMMdd"), infoIdx); 
//        } else {
//            String beginDateStr = dateConvertToString(this._beginWorkDateForDateType,"yyyyMMdd");
//            String endDateStr = dateConvertToString(maybeEndDate,"yyyyMMdd");
//            this._searchScopeList = new ArrayList<Object[]>();
//            for (int i = 0; i < searchScopeList.size(); i++) {
//                Object[] obj = searchScopeList.get(i);
//                String calDate = obj[0] != null ? obj[0].toString() : "";
//                if (StringUtil.isNullOrBlank(calDate))
//                    continue;
//                if (beginDateStr.compareTo(calDate) <= 0 && endDateStr.compareTo(calDate) >= 0)
//                    this._searchScopeList.add(obj);
//            }
//        }
        String beginDateStr = dateConvertToString(this._beginWorkDateForDateType,"yyyyMMdd");
        String endDateStr = dateConvertToString(maybeEndDate,"yyyyMMdd");
        this._searchScopeList = new ArrayList<Object[]>();
        for (int i = 0; i < searchScopeList.size(); i++) {
            Object[] obj = searchScopeList.get(i);
            String calDate = obj[0] != null ? obj[0].toString() : "";
            if (StringUtil.isNullOrBlank(calDate))
                continue;
            if (beginDateStr.compareTo(calDate) <= 0 && endDateStr.compareTo(calDate) >= 0)
                this._searchScopeList.add(obj);
        }
    }
    
    
    
    /**
	 * <li>说明： 读取工作日历主表,获得默认的每日工时安排
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-6-22
	 * <li>修改人： 程梅
	 * <li>修改日期：2015年4月17日14:30:31
	 * <li>修改内容：当infoIdx为空时，查询默认的工时安排，不为空时查询该工作日历下的每日工时安排
	 * @param infoIdx 工作日历id
	 * @return 默认每日工作时间毫秒数
	 */
    @SuppressWarnings("unchecked")
    public Object [] getDefaultEveryDayWorkTime(String infoIdx){
        
        String sql = "";
        if(StringUtil.isNullOrBlank(infoIdx)) {
            sql = SqlMapUtil.getSql("basic-org:defaultEveryDayWorkTime");
        }else {
            sql = SqlMapUtil.getSql("basic-org:getEveryDayWorkTimeByInfoIdx").replaceAll("#INFOIDX#", infoIdx);
        }
    	List list = daoUtils.executeSqlQuery(sql);
    	Object [] obj = null;
    	if(list!=null&&list.size()>0){
    		obj = (Object [])list.get(0);
    	}
    	return obj;
    }
	
    /**
	 * <li>说明：	 跟据总剩余工时(毫秒数),除以默认每日工作工时,获得一个查询的时间范围,即大约需要多少个工作日,
   	 * <li>      假设为Y: 那么中间考虑加上周末双休的日期为 (Y/5)*2+Y
   	 * <li>		 为了把法定假日等考虑进去,则每超过一季度,则增加15天,不足一季度按一季度算, (取整(x/90)+1)*15+x
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-6-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param countSurplusWorkTime 总剩余工时
	 *        defaultWorkTimeForEveryDay 默认每日工作时间
	 * @return 默认每日工作时间毫秒数
	 */
    public int getPreliminaryEstimateEndDate(long countSurplusWorkTime,long defaultWorkTimeForEveryDay){
    	double estimateCountDays =  Math.ceil(countSurplusWorkTime/defaultWorkTimeForEveryDay);
    	estimateCountDays = Math.ceil(estimateCountDays/5)*2+estimateCountDays;
    	estimateCountDays = (Math.ceil(estimateCountDays/90)+1)*15+estimateCountDays;
    	return (int)estimateCountDays;
    }
    
    /**
	 * <li>说明： 读取工作日历明细表中,预定的每日默认工作时间, 该时间以毫秒格式返回
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-6-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param beginDate 开始时间
     * @param endDate  结束时间
     * @param infoIdx  工作日历id
	 * @return 默认每日工作时间毫秒数
	 */
    @SuppressWarnings("unchecked")
    public List getStartToEndWorkTimeInfo(String beginDate, String endDate, String infoIdx){
    	String sql = "";
        if(StringUtil.isNullOrBlank(infoIdx)) {
            sql = SqlMapUtil.getSql("basic-org:getStartToEndWorkTimeInfo").replaceAll("#BEGINDATE#", beginDate).replaceAll("#ENDDATE#", endDate);
        }else {
            sql = SqlMapUtil.getSql("basic-org:getStartToEndWorkTimeInfoByInfoIdx").replaceAll("#BEGINDATE#", beginDate).replaceAll("#ENDDATE#", endDate).replaceAll("#INFOIDX#", infoIdx);
        }
    	List list = daoUtils.executeSqlQuery(sql);
    	return list;
    }   
    
    /**
     * 
     * <li>说明：获得指定日期之后的第N天的日期
     * <li>创建人：谭诚
     * <li>创建日期：2013-6-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param beginDate：开始日期
     * 		  addDate : 多少天后
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    private Date afterNDay(Date beginDate, int addDate){
    	Calendar cal = Calendar.getInstance();
    	//DateFormat df = new SimpleDateFormat("yyyy-MM-dd");//未使用的变量
    	cal.setTime(beginDate);
    	cal.add(Calendar.DATE,addDate);
    	Date d2 = cal.getTime();
    	return d2;
    }
    
    /**
     * 
     * <li>说明：转换date类型为string类型,只保留参数指定的类型, 例如 'yyyy-MM-dd' 或者 'yyyyMMdd'
     * <li>创建人：谭诚
     * <li>创建日期：2013-6-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    public String dateConvertToString(Date arg1, String arg2){
    	SimpleDateFormat format = new SimpleDateFormat(arg2);
    	String resultStr = format.format(arg1);
    	return resultStr;
    }
    /**************************************************************************
	 *                      End
	 **************************************************************************/
    
    /**
     * 转换String类型的时间为毫秒数
     * arg1 待转换的String时间
     * arg2 转换通配符
     */
    private long stringConvertToMillisecond(String arg1,String arg2){
    	Format format = new SimpleDateFormat(arg2);
		Date first = null;
		try {
			
			first = (java.util.Date) format.parseObject(arg1);
		} catch (ParseException e) {
			ExceptionUtil.process(e, logger);
		}
    	return first.getTime();
    }


    /* *
     * 根据日期,获得这一天是星期几
     * 参数: 日期
     * 返回值: 周
     */
    //未使用的方法
    /*private int getDay(Date date){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	return cal.get(Calendar.DAY_OF_WEEK);
    }*/
    
	/**************************************************************************
	 *                 工作日历算法: 通过开始时间和结束时间推算工期【+工作日历id 程梅】
	 **************************************************************************/
    public long getRealWorkminutes(Date realStartDate, Date realEndDate, String infoIdx){
//        WorkCalendarInfoQueryManager workCalendarInfoQueryManager =
//            (WorkCalendarInfoQueryManager) Application.getSpringApplicationContext().getBean("workCalendarInfoQueryManager");
//        if (!workCalendarInfoQueryManager.isUseWorkCalendar(infoIdx)) {
//            try {
//                return DateUtil.getRealWorkminutes(realStartDate, realEndDate);
//            } catch (Exception e) {
//                ExceptionUtil.process(e, logger);
//            }
//        }
    	this.initAnyParameters(realStartDate, realEndDate, infoIdx);//计算准备
    	this.mainJs(this._beginWorkDateForMillisecond, this._endWorkDateForMillisecond);//调用递归
//    	System.out.println("最终,共计有效工作时间为:"+this._countWrokTime);
    	return this._countWrokTime;
    }
    
    /**
     * 
     * <li>说明：计算准备
     * <li>创建人：谭诚
     * <li>创建日期：2013-6-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    @SuppressWarnings({ "static-access", "unchecked" })
    private void initAnyParameters(Date realStartDate, Date realEndDate, String infoIdx){
    	this._thisWheelEndTime = 0;
    	this._countWrokTime = 0;
    	this._beginWorkDateForMillisecond = realStartDate.getTime();//开始时间的毫秒数
    	this._endWorkDateForMillisecond   = realEndDate.getTime();//结束时间的毫秒数
        this._beginWorkDateForDateType = realStartDate;//将工作开始时间转为Date类型后赋值到全局
        //2015-6-3 程锐修改为从缓存Map中获取这个时间段范围内的所有日历记录
        WorkCalendarInfoUtil wcInfoUtil = WorkCalendarInfoUtil.getInstance(this._beginWorkDateForDateType);
        if (wcInfoUtil.wcInfoMap.isEmpty())
            wcInfoUtil.buildMap();
        if (wcInfoUtil.wcInfoMap.isEmpty()) {
            this._searchScopeList = this.getStartToEndWorkTimeInfo(DateUtil.getDateByMillSeconds(this._beginWorkDateForMillisecond,"yyyyMMdd"),DateUtil.getDateByMillSeconds(this._endWorkDateForMillisecond,"yyyyMMdd"),infoIdx);
            this._defaultEveryDayWorkTime = getDefaultEveryDayWorkTime(infoIdx);
            return;
        }
        WorkCalendarBean wcBean = wcInfoUtil.wcInfoMap.get(infoIdx);
        if (wcBean == null) {
            this._searchScopeList = this.getStartToEndWorkTimeInfo(DateUtil.getDateByMillSeconds(this._beginWorkDateForMillisecond,"yyyyMMdd"),DateUtil.getDateByMillSeconds(this._endWorkDateForMillisecond,"yyyyMMdd"),infoIdx);
            this._defaultEveryDayWorkTime = getDefaultEveryDayWorkTime(infoIdx);
            return;
        }
        List<Object[]> searchScopeList = wcBean.getSearchScopeList();
//        if (searchScopeList == null || searchScopeList.size() < 1) {
//            this._searchScopeList = this.getStartToEndWorkTimeInfo(DateUtil.getDateByMillSeconds(this._beginWorkDateForMillisecond,"yyyyMMdd"),DateUtil.getDateByMillSeconds(this._endWorkDateForMillisecond,"yyyyMMdd"),infoIdx);
//        } else {
//            String beginDateStr = DateUtil.getDateByMillSeconds(this._beginWorkDateForMillisecond,"yyyyMMdd");
//            String endDateStr = DateUtil.getDateByMillSeconds(this._endWorkDateForMillisecond,"yyyyMMdd");
//            this._searchScopeList = new ArrayList<Object[]>();
//            for (int i = 0; i < searchScopeList.size(); i++) {
//                Object[] obj = searchScopeList.get(i);
//                String calDate = obj[0] != null ? obj[0].toString() : "";
//                if (StringUtil.isNullOrBlank(calDate))
//                    continue;
//                if (beginDateStr.compareTo(calDate) <= 0 && endDateStr.compareTo(calDate) >= 0)
//                    this._searchScopeList.add(obj);
//            }
//        }
        String beginDateStr = DateUtil.getDateByMillSeconds(this._beginWorkDateForMillisecond,"yyyyMMdd");
        String endDateStr = DateUtil.getDateByMillSeconds(this._endWorkDateForMillisecond,"yyyyMMdd");
        this._searchScopeList = new ArrayList<Object[]>();
        for (int i = 0; i < searchScopeList.size(); i++) {
            Object[] obj = searchScopeList.get(i);
            String calDate = obj[0] != null ? obj[0].toString() : "";
            if (StringUtil.isNullOrBlank(calDate))
                continue;
            if (beginDateStr.compareTo(calDate) <= 0 && endDateStr.compareTo(calDate) >= 0)
                this._searchScopeList.add(obj);
        }
        if (wcBean.getDefaultEveryDayWorkTime() == null || wcBean.getDefaultEveryDayWorkTime().length < 1)
            this._defaultEveryDayWorkTime = getDefaultEveryDayWorkTime(infoIdx);
        else
            this._defaultEveryDayWorkTime = wcBean.getDefaultEveryDayWorkTime();
        
    }
      
    private void mainJs(long currentDay,long endDay){
    	long s = 0;
    	int nextDay = 1;
    	//开始时间不应当大于结束时间
    	if(currentDay>endDay){
//    		System.out.println("开始时间小于了结束时间");
    		return;
    	}
    	
    	//1. 将这一天Date类型转换为String类型,且按照yyyyMMdd格式转换,只保留年月日部分
    	String jt = DateUtil.getDateByMillSeconds(currentDay,"yyyyMMdd");
    	Object [] jl = null;
    	//在工作日历中搜索这一天的工作安排
    	for(int i=0;i<this._searchScopeList.size();i++){
    		Object [] obj = (Object [])this._searchScopeList.get(i);
    		//如果在工作记录明细时间安排表中找到了与该日对应的记录,则开始计算本日工时
    		if(!StringUtil.isNullOrBlank(String.valueOf(obj[0]))&&String.valueOf(obj[0]).equals(jt)){
    			jl = obj;
    			break;
    		} 
    	}
    	//工作日历中存在该日记录
    	if(jl!=null){
    		s = gsjs(DateUtil.getDateByMillSeconds(currentDay),jl,DateUtil.getDateByMillSeconds(endDay));
    		this._countWrokTime += s;
//    		if(this._thisWheelEndTime<endDay){
//    			nextDay = 1;
//    		}
    	}
    	//工作日历中不存在该日记录
    	else{
//    		周末和节日放在检索list之后,虽然会降低效率,但是可以防止节日加班的情况发生
    		//======判断周末=======验证该日是否为周末,如果是周末,则跳过
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(DateUtil.getDateByMillSeconds(currentDay));
    		int p = cal.get(Calendar.DAY_OF_WEEK);
    		//======判段节日=======
    		Calendar today = Calendar.getInstance();
    		today.setTime(DateUtil.getDateByMillSeconds(currentDay));// 加载当前日期
    		Lunar lunar = new Lunar(today);
    		Festival ft = new Festival();// 创建节日对象。
//    		//验证该日是否为节假日(公历),如果是,则跳过
    		String jrNameGL = ft.showSFtv(today.get(Calendar.MONTH)+1, today.get(Calendar.DATE));
    		//验证该日是否为节假日(农历),如果是,则跳过
    		String jrNameNL = lunar.LunarFestival();
    		/**
    		 * 判断周末节日或者是默认工作时间
    		 */
    		//节日
    		if(!StringUtil.isNullOrBlank(jrNameNL)&&
    				("端午节".equals(jrNameNL)||"中秋节".equals(jrNameNL)||"除夕".equals(jrNameNL))||"春节".equals(jrNameNL)){
    			if("端午节".equals(jrNameNL)||"中秋节".equals(jrNameNL)||"除夕".equals(jrNameNL)){
    				//端午\中秋\除夕放假一天
	    			//mainJs(afterNDay(thisDate,1),timeInterval);
	    			nextDay = 1;
    			}else if("春节".equals(jrNameNL)){
    				//春节放假三天
    				//mainJs(afterNDay(thisDate,3),timeInterval);
    				nextDay = 3;
    			}
    		} 
    		else if(!StringUtil.isNullOrBlank(jrNameGL)&&
    				("元旦".equals(jrNameGL)||"国际劳动节".equals(jrNameGL)||"国庆节".equals(jrNameGL))){
	    		if("元旦".equals(jrNameGL)||"国际劳动节".equals(jrNameGL)){
	    			//元旦节\劳动节放假一天
	    			//mainJs(afterNDay(thisDate,1),timeInterval);
					nextDay = 1;
	    		} else if("国庆节".equals(jrNameGL)){
	    			//国庆节放假三天
	    			//mainJs(afterNDay(thisDate,3),timeInterval);
	    			nextDay = 3;
	    		}
    		}
    		// 1是星期日  7是星期六
    		else if(p==7||p==1){
    			//mainJs(afterNDay(thisDate,1),timeInterval);
    			nextDay = 1;
    		} 
    		else {
                if (this._defaultEveryDayWorkTime != null)
                    //以上均不满足,则根据默认工作日历安排 
                    s = gsjs(DateUtil.getDateByMillSeconds(currentDay),this._defaultEveryDayWorkTime,DateUtil.getDateByMillSeconds(endDay));
    			this._countWrokTime += s;
    		}
    	}
//    	System.out.println("今日的结束工作时间为:"+DateUtil.getDateByMillSeconds(this._thisWheelEndTime,"yyyy-MM-dd HH:mm:ss"));
    	//如果当前工作执行的最终时间早于最终任务完成时间,则说明还需要下一个工作日继续进行
    	if(this._thisWheelEndTime<endDay){
    		//参数1:求得下一天<这里需要将thisDate转为00:00:00>, 参数2:总共时减去本日工时
    		Format format = new SimpleDateFormat("yyyy-MM-dd");
    		Date h = null;
			try {
				h = (java.util.Date) format.parseObject(dateConvertToString(DateUtil.getDateByMillSeconds(currentDay),"yyyy-MM-dd"));
			} catch (ParseException e) {
				ExceptionUtil.process(e, logger);
				h = DateUtil.getDateByMillSeconds(currentDay);
			}
    		
//    		System.out.println("今日"+dateConvertToString(DateUtil.getDateByMillSeconds(currentDay),"yyyy-MM-dd HH:mm:ss")+"完成:"+s+"\t总计完成:"+this._countWrokTime+"\n\n");
    		mainJs(afterNDay(h,nextDay).getTime(),endDay); 
    	} else {
    		//已到达结束日期
//    		System.out.println("已到达结束日期");
//    		System.out.println("今日"+dateConvertToString(DateUtil.getDateByMillSeconds(currentDay),"yyyy-MM-dd HH:mm:ss")+"完成:"+s+"\t总计完成:"+this._countWrokTime+"\n\n");
    		return;
    	}
    }
    
    @SuppressWarnings("unchecked")
    private long gsjs(Date beDate,Object [] obj, Date edDate){
    	long s = 0;
    	Map map = null;
    	List list = new ArrayList();
        String day = dateConvertToString(beDate,"yyyyMMdd");
        if (obj != null)
            day = String.valueOf(obj[0]==null?dateConvertToString(beDate,"yyyyMMdd"):obj[0]);
    	//如果第一组工作时间的开始时间和结束时间都不等于空,并且开始时间与结束时间不相等
    	if(!StringUtil.isNullOrBlank(String.valueOf(obj[2]))
    			&&!StringUtil.isNullOrBlank(String.valueOf(obj[3]))
    			&&!String.valueOf(obj[2]).equals(String.valueOf(obj[3]))){
    		map = new HashMap();
    		map.put("bg", stringConvertToMillisecond(day.concat(" ").concat(String.valueOf(obj[2])),"yyyyMMdd HH:mm:ss"));
    		map.put("ed", transEndTime(day, String.valueOf(obj[3])));
    		map.put("period", obj[10]);//第一组工作时间的总工时
    		list.add(map);
    	}
    	//如果第二组工作时间的开始时间和结束时间都不等于空,并且开始时间与结束时间不相等
    	if(!StringUtil.isNullOrBlank(String.valueOf(obj[4]))
    			&&!StringUtil.isNullOrBlank(String.valueOf(obj[5]))
    			&&!String.valueOf(obj[4]).equals(String.valueOf(obj[5]))){
    		map = new HashMap();
    		map.put("bg", stringConvertToMillisecond(day.concat(" ").concat(String.valueOf(obj[4])),"yyyyMMdd HH:mm:ss"));
    		map.put("ed", transEndTime(day, String.valueOf(obj[5])));
    		map.put("period", obj[11]);//第二组工作时间的总工时
    		list.add(map);
    	}
    	//如果第三组工作时间的开始时间和结束时间都不等于空,并且开始时间与结束时间不相等
    	if(!StringUtil.isNullOrBlank(String.valueOf(obj[6]))
    			&&!StringUtil.isNullOrBlank(String.valueOf(obj[7]))
    			&&!String.valueOf(obj[6]).equals(String.valueOf(obj[7]))){
    		map = new HashMap();
    		map.put("bg", stringConvertToMillisecond(day.concat(" ").concat(String.valueOf(obj[6])),"yyyyMMdd HH:mm:ss"));
    		map.put("ed", transEndTime(day, String.valueOf(obj[7])));
    		map.put("period", obj[12]);//第三组工作时间的总工时
    		list.add(map);
    	}
    	//如果第四组工作时间的开始时间和结束时间都不等于空,并且开始时间与结束时间不相等
    	if(!StringUtil.isNullOrBlank(String.valueOf(obj[8]))
    			&&!StringUtil.isNullOrBlank(String.valueOf(obj[9]))
    			&&!String.valueOf(obj[8]).equals(String.valueOf(obj[9]))){
    		map = new HashMap();
    		map.put("bg", stringConvertToMillisecond(day.concat(" ").concat(String.valueOf(obj[8])),"yyyyMMdd HH:mm:ss"));
    		map.put("ed", transEndTime(day, String.valueOf(obj[9])));
    		map.put("period", obj[13]);//第一组工作时间的总工时
    		list.add(map);
    	}
    	Map m2 = null;
    	for(int i=0;i<list.size();i++){
    		
    		m2 = (HashMap)list.get(i);
    		
    		//如果时间早于本阶段工作开始时间,并且本阶段的完成时间小于总完工时间,则累加本时间段工时
    		if(beDate.getTime()<= Long.parseLong(String.valueOf(m2.get("bg")))
    				&&edDate.getTime()>Long.parseLong(String.valueOf(m2.get("ed")))){
    			s += Long.parseLong(String.valueOf(m2.get("period"))); //累加至本日工时
    			this._thisWheelEndTime = Long.parseLong(String.valueOf(m2.get("ed")));//记录本轮的结束时间
//    			System.out.println("本轮完成:"+s+"最终完工时间:"+dateConvertToString(edDate,"yyyy-MM-dd HH:mm:ss")+",本轮最终完工时间:"+DateUtil.getDateByMillSeconds(Long.parseLong(String.valueOf(m2.get("ed"))),"yyyy-MM-dd HH:mm:ss"));
    		} 
    		//如果时间恰好处于本阶段工作开始和结束时间范围内,且本阶段完成时间小于总完工时间,则累加本阶段截止时间与当前时间的差
    		else if(beDate.getTime()>=Long.parseLong(String.valueOf(m2.get("bg")))
    				&&beDate.getTime()<=Long.parseLong(String.valueOf(m2.get("ed")))
    				&&edDate.getTime()>Long.parseLong(String.valueOf(m2.get("ed")))){
    			s+= (Long.parseLong(String.valueOf(m2.get("ed")))-beDate.getTime()); //本阶段结束时间-当前时间后累计至本日总工时
    			this._thisWheelEndTime = Long.parseLong(String.valueOf(m2.get("ed")));//记录本轮的结束时间
//    			System.out.println("本轮完成:"+s+"最终完工时间:"+dateConvertToString(edDate,"yyyy-MM-dd HH:mm:ss")+",本轮最终完工时间:"+DateUtil.getDateByMillSeconds(Long.parseLong(String.valueOf(m2.get("ed"))),"yyyy-MM-dd HH:mm:ss"));
    		} 
    		//如果时间晚于本阶段工作结束时间,或者最终结束时间小于或者等于本轮开始时间则跳过本轮循环
    		else if(beDate.getTime()>=Long.parseLong(String.valueOf(m2.get("ed")))
    				||edDate.getTime()<=Long.parseLong(String.valueOf(m2.get("bg")))){
    			continue;
    		} 
    		//以上条件均不满足,说明本轮工作时间能够达到或者超过最终完工时间,即完工时间在本轮任务的开始和结束时间之间
    		else {
    			if(beDate.getTime()>Long.parseLong(String.valueOf(m2.get("bg")))
    					&&beDate.getTime()<=Long.parseLong(String.valueOf(m2.get("ed")))){
    				//如果开始时间晚于本轮开始时间,并且结束时间早于或者等于本轮结束时间,则直接使用结束-开始
    				s+= (edDate.getTime()-beDate.getTime());
    			} else {
	    			//本轮工时 = 完工时间-本轮开工时间
	    			s+= (edDate.getTime()-Long.parseLong(String.valueOf(m2.get("bg"))));
    			}
//    			System.out.println("*本日完成:"+s+"已最终完工!!最终完工时间:"+dateConvertToString(edDate,"yyyy-MM-dd HH:mm:ss")+",本轮最终完工时间:"+DateUtil.getDateByMillSeconds(Long.parseLong(String.valueOf(m2.get("ed"))),"yyyy-MM-dd HH:mm:ss"));
    			this._thisWheelEndTime = edDate.getTime();
    			break;
    		}
    	}
//    	System.out.println("%本日完成:"+s+"最终完工时间:"+dateConvertToString(edDate,"yyyy-MM-dd HH:mm:ss"));
    	//返回值代表本日完成的所有工时累加
    	return s;
    }
    
    /**
     * 
     * <li>说明：根据开始日期，工期（分钟）及默认工作日历返回结束日期
     * <li>创建人：程锐
     * <li>创建日期：2013-6-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param startDate 开始日期
     * @param ratedWorkMinutes 工期（分钟）
     * @return 结束日期
     * @throws Exception
     */
    public Date getFinalDate(Date startDate, Double ratedWorkMinutes) throws Exception {
        long timeInterval = 0l; 
        long startTime = 0l;
        Date endDate = null;
        if(startDate != null){
            startTime = startDate.getTime();
            Double ratedMinutes = ratedWorkMinutes*60*1000;//将工期（分钟）转换成毫秒数
            timeInterval = ratedMinutes.longValue();
            long endTime = getFinalTime(startTime, timeInterval);
            endDate = DateUtil.getDateByMillSeconds(endTime);
        }
        return endDate;
    }
    /**
     * 
     * <li>说明：根据开始日期，工期（分钟）及特定工作日历返回结束日期
     * <li>创建人：程梅
     * <li>创建日期：2015-4-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param startDate 开始日期
     * @param ratedWorkMinutes 工期（分钟）
     * @param infoIdx 工作日历id
     * @return 结束日期
     * @throws Exception
     */
    public Date getFinalDate(Date startDate, Double ratedWorkMinutes, String infoIdx) throws Exception {
        long timeInterval = 0l; 
        long startTime = 0l;
        Date endDate = null;
        if(startDate != null){
            startTime = startDate.getTime();
            Double ratedMinutes = ratedWorkMinutes*60*1000;//将工期（分钟）转换成毫秒数
            timeInterval = ratedMinutes.longValue();
            long endTime = getFinalTime(startTime, timeInterval, infoIdx);
            endDate = DateUtil.getDateByMillSeconds(endTime);
        }
        return endDate;
    }
    /**
     * 
     * <li>说明：根据开始时间及标准工期,计算工作日历的结束时间
     * <li>创建人：程梅
     * <li>创建日期：2015-4-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param startTime 开始日期
     * @param timeInterval 标准工期
     * @param infoIdx 工作日历id
     * @return 结束时间的long型数据
     */
    public long getFinalTime(long startTime, long timeInterval, String infoIdx) throws Exception {
//        WorkCalendarInfoQueryManager workCalendarInfoQueryManager =
//            (WorkCalendarInfoQueryManager) Application.getSpringApplicationContext().getBean("workCalendarInfoQueryManager");
//        if (!workCalendarInfoQueryManager.isUseWorkCalendar(infoIdx)) 
//            return DateUtil.getFinalTime(startTime, timeInterval);
//      调用函数,设置各项初始参数
        this.initAnyParameters(startTime, timeInterval, infoIdx);
        this.mainJs(this._beginWorkDateForDateType, timeInterval);
        return this._lastOverTime.getTime();
    }
    /**
     * 
     * <li>说明：根据日历主表id查询日历明细信息
     * <li>创建人：程梅
     * <li>创建日期：2015-4-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param infoIdx 日历主表id
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<WorkCalendarDetail> getModeList(String infoIdx){
        String hql = "From WorkCalendarDetail where recordStatus=0 and infoIdx='"+infoIdx+"'";
        List<WorkCalendarDetail> list = daoUtils.find(hql);
        return list ;
    } 
    /**
     * <li>说明：重写新增方法
     * <li>创建人：程锐
     * <li>创建日期：2015-7-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t 工作日历明细实体  
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveOrUpdate(WorkCalendarDetail t) throws BusinessException, NoSuchFieldException {
        if ("0".equals(t.getCalDateType()))//0 非默认工作时间时才作结束时间转换 
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
        String ed1 = t.getPeriod1End();
        String ed2 = t.getPeriod2End();
        String ed3 = t.getPeriod3End();
        String ed4 = t.getPeriod4End();
        t.setPeriod1End(WorkCalendarBean.TWENTYFOURTIME.equals(ed1) ? WorkCalendarBean.ZEROTIME : ed1);
        t.setPeriod2End(WorkCalendarBean.TWENTYFOURTIME.equals(ed2) ? WorkCalendarBean.ZEROTIME : ed2);
        t.setPeriod3End(WorkCalendarBean.TWENTYFOURTIME.equals(ed3) ? WorkCalendarBean.ZEROTIME : ed3);
        t.setPeriod4End(WorkCalendarBean.TWENTYFOURTIME.equals(ed4) ? WorkCalendarBean.ZEROTIME : ed4);
        return t;
    }
    
    /**
     * <li>说明：设定为00:00:00的结束时间为23:59:59存入数据库
     * <li>创建人：程锐
     * <li>创建日期：2015-7-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t 工作日历明细实体
     * @return 工作日历实体
     */
    private WorkCalendarDetail changeEndTime(WorkCalendarDetail t) {
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
     * <li>说明：转换结束时间生成的毫秒数
     * <li>创建人：程锐
     * <li>创建日期：2015-7-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param day 日期字符串
     * @param endTime 结束时间字符串
     * @return 结束时间生成的毫秒数
     */
    private long transEndTime(String day, String endTime) {
        long millisecond = stringConvertToMillisecond(day.concat(" ").concat(endTime),"yyyyMMdd HH:mm:ss");
        return WorkCalendarBean.TWENTYFOURTIME.equals(endTime) ? (millisecond + 1000) : millisecond;
    }
    
    /**
     * <li>说明：设定非工作日为工作日 （20170207修改：设置该时间段的工作时间为”日历“的默认时间段，类型为【非默认工作日】）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-1-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param params
     * @throws Exception 
     */
    public void setVolumeType(Map<String, Object> params) throws Exception {
        WorkCalendarDetail entity = (WorkCalendarDetail)params.get("detail");
        String volumeStart = (String)params.get("volumeStart");
        String volumeEnd = (String)params.get("volumeEnd");
        if(entity == null || StringUtil.isNullOrBlank(entity.getInfoIdx()) || StringUtil.isNullOrBlank(volumeStart) || StringUtil.isNullOrBlank(volumeEnd)){
            return ;
        }
        String infoIdx = entity.getInfoIdx();
        
        Date volumeStartTime = DateUtil.parse(volumeStart.substring(0,volumeStart.indexOf('T')));
        Date volumeEndTime = DateUtil.parse(volumeEnd.substring(0,volumeEnd.indexOf('T')));
        
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(volumeStartTime);
        
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(volumeEndTime);
        endCal.add(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        while (startCal.before(endCal)) {
            // 如果是周末或节假日，则设置为非节假日 , 修改为一段时间内全部设定
            //if(isGLORNL(startCal.getTime())){ }
            String gzrl = sdf.format(startCal.getTime());
            WorkCalendarDetail detail = findWorkCalendarDetail(infoIdx, gzrl);
            if(detail == null){
                detail = new WorkCalendarDetail();
            }
            detail.setCalDate(gzrl);
            detail.setCalDateType(entity.getCalDateType());// 设置为默认工作时间
            detail.setInfoIdx(infoIdx);
            setDetailPeriodInfo(detail, entity);
            this.saveOrUpdate(detail);
            startCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        
    }
    
    
    /**
     * <li>说明：设置工作时间 通过日历默认时间设置
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-2-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param detail
     * @param info
     */
    private void setDetailPeriodInfo(WorkCalendarDetail detail,WorkCalendarDetail entity){
        detail.setPeriod1Begin(entity.getPeriod1Begin());
        detail.setPeriod1End(entity.getPeriod1End());
        detail.setPeriod2Begin(entity.getPeriod2Begin());
        detail.setPeriod2End(entity.getPeriod2End());
        detail.setPeriod3Begin(entity.getPeriod3Begin());
        detail.setPeriod3End(entity.getPeriod3End());
        detail.setPeriod4Begin(entity.getPeriod4Begin());
        detail.setPeriod4End(entity.getPeriod4End());
    }
    
    
    /**
     * <li>说明：判断传入日期是否为周末或者节假日
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-1-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param thisDate
     * @return
     */
    public Boolean isGLORNL(Date thisDate){
        //======判断周末=======验证该日是否为周末,如果是周末,则跳过
        Calendar cal = Calendar.getInstance();
        cal.setTime(thisDate);
        int p = cal.get(Calendar.DAY_OF_WEEK);
        //======判段节日=======
        Calendar today = Calendar.getInstance();
        today.setTime(thisDate);// 加载当前日期
        Lunar lunar = new Lunar(today);
        Festival ft = new Festival();// 创建节日对象。
        //验证该日是否为节假日(公历),如果是,则跳过
        String jrNameGL = ft.showSFtv(today.get(Calendar.MONTH)+1, today.get(Calendar.DATE));
        //验证该日是否为节假日(农历),如果是,则跳过
        String jrNameNL = lunar.LunarFestival();
        // 判断周末
        if(p==7||p==1){
            return true ;
        }
        return false ;   
    }
    
}