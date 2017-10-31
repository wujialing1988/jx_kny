package com.yunda.zb.tp.manager;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.zb.tp.entity.TZbglAndFaultTp;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：TZbglAndFaultTp业务类, 整备修程修提票综合查询
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-10-13
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "tZbglAndFaultTpManager")
public class TZbglAndFaultTpManager extends JXBaseManager<TZbglAndFaultTp, TZbglAndFaultTp> {
    
    private static final String TRAINTYPEIDX = "trainTypeIDX";
    
    private static final String TRAINNO = "trainNo";

    /**
     * <li>说明：查询提票综合统计（根据条件动态显示字段）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws NoSuchMethodException 
     * @throws SecurityException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     * @throws Exception
     * @return List<TZbglAndFaultTp> 返回结果集
     * @param searchEntity 查询条件
     * @param fields 前台多选框传递的值，动态显示查询字段
     */
    @SuppressWarnings("unchecked")
    public List<TZbglAndFaultTp> findTpPageListByParm(SearchEntity<TZbglAndFaultTp> searchEntity, String fields) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        // 查询实体
        TZbglAndFaultTp entity = searchEntity.getEntity();
        List<TZbglAndFaultTp> list = new ArrayList<TZbglAndFaultTp>();
        List<TZbglAndFaultTp> objList = new ArrayList<TZbglAndFaultTp>();
        Map<String, String> map = new HashMap<String, String>();
        final String onString = "on";
        final String zzjString = " zzj.";
        
        // 查询条件 - 车型主键
        if (!StringUtil.isNullOrBlank(entity.getTrainTypeIDX()) && !onString.equals(entity.getTrainTypeIDX())) {
            map.put(TRAINTYPEIDX, entity.getTrainTypeIDX());
        }
        // 查询条件 - 车号
        if (!StringUtil.isNullOrBlank(entity.getTrainNo()) && !onString.equals(entity.getTrainNo())) {
            map.put(TRAINNO, entity.getTrainNo());
        }
        StringBuilder sb = new StringBuilder(" select zzj.trainTypeShortName,");  //From ZbglTp Where recordStatus = 0
        //拼接SQL
        if (StringUtils.isNotBlank(fields)) {
            String fieldsArry[] = fields.split(Constants.JOINSTR);
            int a = 0;
            //遍历数组
            for (String string : fieldsArry) {
                //如果不空，那么拼接字段
                if (StringUtils.isNotBlank(string)) {
                    if (a == fieldsArry.length - 1) {
                        sb.append(zzjString).append(string);
                        break;
                    }else{
                        sb.append(zzjString).append(string).append(",");
                    }
                }
                a++;
            }
        }else {
            sb.append(" zzj.trainNo,zzj.professionalTypeName,zzj.faultFixFullName,zzj.faultName ");
        }
        
        sb.append(" ,count(zzj) from TZbglAndFaultTp zzj where 1=1 ");
        sb.append(CommonUtil.buildParamsHql(map));
        
//      条件筛选模糊查询
        String strPercent = "%'";
        //专业类型
        if (StringUtils.isNotBlank(entity.getProfessionalTypeName()) && !onString.equals(entity.getProfessionalTypeName())) {
            sb.append(" And zzj.professionalTypeName like '%").append(entity.getProfessionalTypeName()).append(strPercent);
        }
        
        //故障位置
        if (StringUtils.isNotBlank(entity.getFaultFixFullName()) && !onString.equals(entity.getFaultFixFullName())) {
            sb.append(" And zzj.faultFixFullName like '%").append(entity.getFaultFixFullName()).append(strPercent);
        }
        
        //故障现象
        if (StringUtils.isNotBlank(entity.getFaultName()) && !onString.equals(entity.getFaultName())) {
            sb.append(" And zzj.faultName like '%").append(entity.getFaultName()).append(strPercent);
        }
        
        //查询条件 - 提票时间(开始)
        if (!StringUtil.isNullOrBlank(entity.getStartDate())) {
            sb.append(" and  to_char(zzj.noticeTime,'yyyy-mm-dd')>='").append(entity.getStartDate()).append("'");
        }
        //查询条件 - 提票时间（结束）
        if (!StringUtil.isNullOrBlank(entity.getOverDate())){
            sb.append(" and  to_char(zzj.noticeTime,'yyyy-mm-dd')<='").append(entity.getOverDate()).append("'");
        }
        
        // 检修类型
        if (entity.getModelType() != null && entity.getModelType() != 0 ){
            sb.append(" and  zzj.modelType=").append(entity.getModelType()).append("");
        }
        
        //准备字段存储数组游标
        int trainNoindex = 999;
        int professionalTypeNameindex = 999;
        int faultFixFullNameindex = 999;
        int faultNameindex = 999;
        
        //分组条件
        sb.append(" group by ");
        if (StringUtils.isNotBlank(fields)) {
            String fieldsArry[] = fields.split(",");
            int b = 0;
            for (String string : fieldsArry) {
                
                //如果这个字段在数组中存在，那么保存该数组游标值
                if("trainNo".equals(string)){
                    trainNoindex = b + 1;
                }else if ("professionalTypeName".equals(string)) {
                    professionalTypeNameindex = b + 1;
                }else if ("faultFixFullName".equals(string)) {
                    faultFixFullNameindex = b + 1;
                }else if ("faultName".equals(string)) {
                    faultNameindex = b + 1;
                }
                
                if (StringUtils.isNotBlank(string)) {
                    if (b == fieldsArry.length - 1) {
                        sb.append(zzjString).append(string);
                        break;
                    }else{
                        sb.append(zzjString).append(string).append(",");
                    }
                }
                b++;
            }
        }else {
            sb.append(" zzj.trainNo,zzj.professionalTypeName,zzj.faultFixFullName,zzj.faultName ");
        }
        sb.append(" ,zzj.trainTypeShortName order by count(zzj) desc");
     
   
        String hql = sb.toString();
        //获取list，由于该list中存放的是object不是bean在转json的时候会转换为数组String而不是bean
        list =  (List<TZbglAndFaultTp>) this.find(hql);
        Object[] objArry = list.toArray();
        for (int i = 0; i < objArry.length; i++) {
            Object[] arr = (Object[]) objArry[i];
            TZbglAndFaultTp z = new TZbglAndFaultTp();
            for (int j = 0; j < arr.length; j++) {
                
                z.setTrainTypeShortName(String.valueOf(arr[0]));
                if(trainNoindex != 999){
                    z.setTrainNo(String.valueOf(arr[trainNoindex]));
                }
                if(professionalTypeNameindex != 999){
                    z.setProfessionalTypeName(String.valueOf(arr[professionalTypeNameindex] == null ? "" : arr[professionalTypeNameindex]));
                }
                if(faultFixFullNameindex != 999){
                    z.setFaultFixFullName(String.valueOf(arr[faultFixFullNameindex] == null ? "" : arr[faultFixFullNameindex]));
                }
                if(faultNameindex != 999){
                    z.setFaultName(String.valueOf(arr[faultNameindex] == null ? "" : arr[faultNameindex]));
                }
                z.setZbthtjCount(String.valueOf(arr[arr.length - 1]));
            }
            objList.add(z);
        }
        return objList;
    }

    /**
     * <li>说明：查询提票综合统计明细(根据所选行)
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws ParseException 
     * @throws Exception
     * @param searchEntity 查询条件
     * @return List<ZbglTp> 返回结果集
     */
    @SuppressWarnings("unchecked")
    public List<TZbglAndFaultTp> findTpDeteailPageListByParm(SearchEntity<TZbglAndFaultTp> searchEntity) throws ParseException {
        List<TZbglAndFaultTp> list = new ArrayList<TZbglAndFaultTp>();
        List<TZbglAndFaultTp> objList = new ArrayList<TZbglAndFaultTp>();
        final String onString = "on";
//      查询实体
        TZbglAndFaultTp entity = searchEntity.getEntity();
        
        Map<String, String> map = new HashMap<String, String>();
        
//      查询条件 - 车型主键
        map.put(TRAINNO, entity.getTrainNo());
        
        StringBuilder sb = new StringBuilder(" select ");  //From ZbglTp Where recordStatus = 0
        //拼接SQL
        sb.append(" zzj.trainTypeShortName, ");
        sb.append(" zzj.trainNo, ");
        sb.append(" zzj.faultNoticeCode, ");
        sb.append(" zzj.faultOccurDate, ");
        sb.append(" zzj.repairClass, ");
        sb.append(" zzj.professionalTypeName, ");
        sb.append(" zzj.noticePersonName, ");
        sb.append(" zzj.faultFixFullName, ");
        sb.append(" zzj.faultName, ");
        sb.append(" zzj.faultDesc, ");
        //销票人 = 施修人
        sb.append(" zzj.handlePersonName, ");
        sb.append(" zzj.repairResult, ");
        sb.append(" zzj.handleTime ");
        sb.append(" from TZbglAndFaultTp zzj where 1=1 ");
        sb.append(CommonUtil.buildParamsHql(map));
        
////      条件筛选模糊查询
//        String strPercent = "%'";
        String strPercent = "'";
        //查询条件 - 提票时间(开始)
        if (!StringUtil.isNullOrBlank(entity.getStartDate())) {
            sb.append(" and  to_char(zzj.noticeTime,'yyyy-mm-dd')>='").append(entity.getStartDate()).append("'");
        }
        //查询条件 - 提票时间（结束）
        if (!StringUtil.isNullOrBlank(entity.getOverDate())){
            sb.append(" and  to_char(zzj.noticeTime,'yyyy-mm-dd')<='").append(entity.getOverDate()).append("'");
        }
        //专业类型
        if ("".equals(entity.getProfessionalTypeName()) && !onString.equals(entity.getProfessionalTypeName())) {
            sb.append(" And zzj.professionalTypeName is null ");
        }else if(entity.getProfessionalTypeName() != null){
//            sb.append(" And zzj.professionalTypeName like '%").append(entity.getProfessionalTypeName()).append(strPercent);
            sb.append(" And zzj.professionalTypeName = '").append(entity.getProfessionalTypeName()).append(strPercent);
        }
        //车型编码
        if ("".equals(entity.getTrainTypeShortName()) && !onString.equals(entity.getTrainTypeShortName())) {
            sb.append(" And zzj.trainTypeShortName is null ");
        }else if(entity.getTrainTypeShortName() != null){
            sb.append(" And zzj.trainTypeShortName = '").append(entity.getTrainTypeShortName()).append(strPercent);
        }
//      故障位置
        if ("".equals(entity.getFaultFixFullName()) && !onString.equals(entity.getFaultFixFullName())){
            sb.append(" And zzj.faultFixFullName is null ");
        }else if (entity.getFaultFixFullName() != null) {
//            sb.append(" And zzj.faultFixFullName like '%").append(entity.getFaultFixFullName()).append(strPercent);
            sb.append(" And zzj.faultFixFullName = '").append(entity.getFaultFixFullName()).append(strPercent);
        }
//      故障现象
        if ("".equals(entity.getFaultName()) && !onString.equals(entity.getFaultName())){
            sb.append(" And zzj.faultName is null ");
        } else if(entity.getFaultName() != null) {
//            sb.append(" And zzj.faultName like '%").append(entity.getFaultName()).append(strPercent);
            sb.append(" And zzj.faultName = '").append(entity.getFaultName()).append(strPercent);
        }
        
        // 检修类型
        if (entity.getModelType() != null && entity.getModelType() != 0 ){
            sb.append(" and  zzj.modelType=").append(entity.getModelType()).append("");
        }
        
        String hql = sb.toString();
        
//      获取list，由于该list中存放的是object不是bean在转json的时候会转换为数组String而不是bean
//      值基本上都是固定的。非动态
        list =  (List<TZbglAndFaultTp>) this.find(hql);
        Object[] objArry = list.toArray();
        for (int i = 0; i < objArry.length; i++) {
            Object[] arr = (Object[]) objArry[i];
            TZbglAndFaultTp z = new TZbglAndFaultTp();
            
            z.setTrainTypeShortName(String.valueOf(arr[0]));
            z.setTrainNo(String.valueOf(arr[1]));
            z.setFaultNoticeCode(String.valueOf(arr[2]));
            z.setFaultOccurDate(DateUtil.parse(String.valueOf(arr[3])));
            z.setRepairClass(String.valueOf(arr[4]));
            z.setProfessionalTypeName(String.valueOf(arr[5] == null ? "" : arr[5]));
            z.setNoticePersonName(String.valueOf(arr[6] == null ? "" : arr[6]));
            z.setFaultFixFullName(String.valueOf(arr[7] == null ? "" : arr[7]));
            z.setFaultName(String.valueOf(arr[8] == null ? "" : arr[8]));
            z.setFaultDesc(String.valueOf(arr[9] == null ? "" : arr[9]));
            z.setHandlePersonName(String.valueOf(arr[10] == null ? "" : arr[10]));
            z.setRepairResult(Integer.valueOf(String.valueOf(arr[11] == null ? "0" : arr[11])));
            z.setHandleTime((arr[12] == null ? null : DateUtil.parse(String.valueOf(arr[12]))));
            
            objList.add(z);
        }

        return objList;
    }
    
}
