package com.yunda.jx.jxgc.producttaskmanage.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jxpz.utils.SystemConfigUtil;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：作业工单派工业务类
 * <li>创建人：程锐
 * <li>创建日期：2014-11-04
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="dispatcherProcManager")
public class DispatcherProcManager extends JXBaseManager<WorkCard, WorkCard> {
    /** 获取作业工单派工业务类对象 */
	protected Dispatcher4WorkCardManager getDispatcher4WorkCardManager() {
		return (Dispatcher4WorkCardManager) Application.getSpringApplicationContext().getBean("dispatcher4WorkCardManager");
	}
	/**
     * 
     * <li>说明：工长派工-全部批量派工
     * <li>创建人：程锐
     * <li>创建日期：2014-11-04
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询json字符串
     * @param empids 人员主键字符串
     * @param operatorid 操作员id
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public void foremanAllDispatcher(String searchJson, String empids, Long operatorid) throws JsonParseException, JsonMappingException, IOException{
        String sql = getWorkCardSql(searchJson);
        List list = daoUtils.executeSqlQuery(sql);
        getDispatcher4WorkCardManager().updateAllForemanDispater(list, empids, operatorid, sql);
    }
	 /**
     *  
     * <li>说明：获取工长派工-未派工查询sql
     * <li>创建人：程锐
     * <li>创建日期：2014-11-04
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    private String getWorkCardSql(String searchJson) throws JsonParseException, JsonMappingException, IOException{
        String sql = SqlMapUtil.getSql("jxgc-gdgl2:getWorkCardSql")
                                        + WorkCard.STATUS_OPEN +
                                        ("show".equalsIgnoreCase(SystemConfigUtil.getValue("ck.jxgc.producttaskmanage.workcard.foremanShowInit"))
                                            ? "' or t.status = '" + WorkCard.STATUS_NEW : "") + "')";
        //添加工艺节点多选过滤条件
        Map queryMap = JSONUtil.read(searchJson, Map.class); 
        StringBuilder multyAwhere = new StringBuilder();
        if(queryMap.containsKey("nodeCaseIDX")) {
            String nodeCaseString = String.valueOf(queryMap.get("nodeCaseIDX"));
            if(!StringUtil.isNullOrBlank(nodeCaseString)){
                multyAwhere.append(" and n.idx in ('").append(nodeCaseString.replaceAll(";", "','")).append("') ");
            }
        }
        //添加检修活动IDX多选过滤条件
        if(queryMap.containsKey("repairActivityIDX")) {
            String activityString = String.valueOf(queryMap.get("repairActivityIDX"));
            if(!StringUtil.isNullOrBlank(activityString)){
                multyAwhere.append(" and t.repair_activity_idx in ('").append(activityString.replaceAll(";", "','")).append("') ");               
            }
        }
        if(queryMap.containsKey("rdpIDX")) {
            String rdpString = String.valueOf(queryMap.get("rdpIDX"));
            if(!StringUtil.isNullOrBlank(rdpString)){
                multyAwhere.append(" and r.idx = '").append(rdpString).append("' ");                
            }
        }
//        if(queryMap.containsKey("fixPlaceFullName")) {
//            String fullNameString = String.valueOf(queryMap.get("fixPlaceFullName"));
//            if(!StringUtil.isNullOrBlank(fullNameString)){
//                multyAwhere.append(" and t.fixplace_fullname like '%").append(fullNameString).append("%' ");
//            }
//        } 
        
        if(queryMap.containsKey("workCardName")) {
            String fullNameString = String.valueOf(queryMap.get("workCardName"));
            if(!StringUtil.isNullOrBlank(fullNameString)){
                multyAwhere.append(" and t.work_card_name like '%").append(fullNameString).append("%' ");
            }
        }
        if(queryMap.containsKey("haveDefaultPerson")) {
            String personString = String.valueOf(queryMap.get("haveDefaultPerson"));
            if(!StringUtil.isNullOrBlank(personString)){
                multyAwhere.append(" and (t.HAVE_DEFAULT_PERSON = ").append(personString).append(" or t.HAVE_DEFAULT_PERSON IS NULL) ");
            }
        }
        
        if(queryMap.containsKey("workStationBelongTeam")) {
            String teamString = String.valueOf(queryMap.get("workStationBelongTeam"));
            if(!StringUtil.isNullOrBlank(teamString)){
                multyAwhere.append(" and t.work_station_belong_team = ").append(teamString).append(" ");
            }
        }
        sql = sql + multyAwhere.toString();
        return sql;
    }
	
}
