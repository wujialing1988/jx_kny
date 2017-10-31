package com.yunda.jx.jxgc.processdef.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.jcbm.jcpjzd.manager.JcpjzdBuildManager;
import com.yunda.jx.component.entity.EquipPart;
import com.yunda.jx.component.manager.EquipPartManager;
import com.yunda.jx.jxgc.processdef.entity.JobProcessDef;
import com.yunda.jx.jxgc.processdef.entity.JobProcessPartsOffList;
import com.yunda.third.poi.excel.ExcelUtil;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车作业流程维护-下车配件清单配置
 * <li>创建人：张迪
 * <li>创建日期：2016-7-12
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="jobProcessPartsOffListManager")
public class JobProcessPartsOffListManager  extends JXBaseManager<JobProcessPartsOffList, JobProcessPartsOffList>{
    @Resource
    JobProcessDefManager jobProcessDefManager;
    @Resource
    JcpjzdBuildManager jcpjzdBuildManager;
   @Resource 
   EquipPartManager equipPartManage;
    /**
     * <li>说明：下车配件清单-保存下车部件信息
     * <li>创建人：张迪
     * <li>创建日期：2016-7-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param processIdx 机车检修流程idx
     * @param buildList 零部件实体列表
     * @throws BusinessException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws NoSuchFieldException 
     */
    public void savePartsOffList(String processIdx, JobProcessPartsOffList[] buildList) throws BusinessException, IllegalAccessException, InvocationTargetException, NoSuchFieldException { 
        JobProcessDef jobProcessDef = jobProcessDefManager.getModelById(processIdx);
        if (null == jobProcessDef) {
            throw new NullPointerException("数据异常，系统没有查询到正在操作的流程信息！");
        }
        AcOperator ac = SystemContext.getAcOperator();
        List<JobProcessPartsOffList> partsList = new ArrayList<JobProcessPartsOffList>();     
        for(JobProcessPartsOffList part : buildList){
            part.setTrainTypeIDX(jobProcessDef.getTrainTypeIDX());
            part.setTrainTypeShortName(jobProcessDef.getTrainTypeShortName());
            part.setCreator(ac.getOperatorid());
            part.setCreateTime(new Date());
            if(null != validateUpdate(part)){
                throw new NullPointerException("配件【"+ part.getPartsName()+"】位置【"+ part.getWzmc()+"】已经存在，请先修改位置或重新选择！"); 
            } 
            partsList.add(part); 
        }
        this.saveOrUpdate(partsList);
    }
    
    /**
     * <li>说明：下车配件清单-批量设置节点
     * <li>创建人：张迪
     * <li>创建日期：2016-7-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIdx 节点id  
     * @param nodeName 节点名称
     * @param flag 判断是上车还是下车节点
     * @param jobProcessPartsOffList  所设置的部件列表
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void saveNodeList(String nodeIdx, String nodeName, String flag, JobProcessPartsOffList[] jobProcessPartsOffList) throws BusinessException, NoSuchFieldException {
        if (null == jobProcessPartsOffList) {
            throw new NullPointerException("数据异常，系统没有查询到正在操作的流程信息！");
        }
        List<JobProcessPartsOffList> partsList = new ArrayList<JobProcessPartsOffList>();
        for(JobProcessPartsOffList jobProcessPartsOff : jobProcessPartsOffList){
            JobProcessPartsOffList entity = this.getModelById(jobProcessPartsOff.getIdx()); //根据id查询配件清单实体
            if("on".equals(flag)){   //设置上车节点
                entity.setOnNodeIdx(nodeIdx);
                entity.setOnNodeName(nodeName);
            }else{   //设置下车节点
                entity.setOffNodeIdx(nodeIdx);
                entity.setOffNodeName(nodeName);
            }
            partsList.add(entity);
        }
        this.saveOrUpdate(partsList);      
    }

    /**
     * <li>说明：通过配件idx,位置代码查询下车配件清单列表
     * <li>创建人：张迪
     * <li>创建日期：2016-7-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param wzdm 位置代码
     * @param partsId 配件idx
     * @return 下车配件清单列表
     */
    public JobProcessPartsOffList getPartOffList(String idx,String wzdm, String partsId,String processIdx) {       
        String hql = "from JobProcessPartsOffList Where ";
        hql  += (null == idx ?" idx is not null ":(" idx != '" + idx + "'"));
        hql  += (null == wzdm || "" == wzdm )?" And wzdm is null":(" And wzdm = '" + wzdm +"'");
        hql += " And partsId = ? And processIdx = ? and recordStatus = 0";
        return (JobProcessPartsOffList)this.daoUtils.findSingle(hql, new Object[]{ partsId, processIdx});
    }
    

    /**
     * <li>说明：更新时验证
     * <li>创建人：张迪
     * <li>创建日期：2016-7-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 下车配件清单实体
     * @return 提示信息
     */
    @Override
    public String[] validateUpdate(JobProcessPartsOffList entity) {
        JobProcessPartsOffList partOffList =  getPartOffList((null==entity.getIdx()?null:entity.getIdx()),(null == entity.getWzdm()?null: entity.getWzdm()),entity.getPartsId(),entity.getProcessIdx());
        if(null != partOffList){ //保存时验证
           return  new String[]{"配件与位置必须唯一，请重新选择位置！"};
        }
        return null;
    }
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：张迪
     * <li>创建日期：2017-4-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jcgxFile
     * @param processIDX
     * @throws FileNotFoundException
     * @throws IOException
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public synchronized List<String> saveImport(File partsFile, String processIDX) throws FileNotFoundException, IOException, BusinessException, NoSuchFieldException {
        POIFSFileSystem poi = null;
        List<String> errInfo = new ArrayList<String>();;
        try {
            poi = new POIFSFileSystem(new FileInputStream(partsFile));
        } catch (Exception e) {
            throw new BusinessException("Excel文件解析异常，请确认上传的文件格式是否正确！");
        }
        HSSFWorkbook workBook = new HSSFWorkbook(poi);
        int sheetIndex = 0;
        HSSFSheet sheet = workBook.getSheetAt(sheetIndex);
        while (null != sheet) {
            try {
                errInfo = saveByStencil(sheet, processIDX);
                sheet = workBook.getSheetAt(++sheetIndex);
            } catch (Exception e) {
                break;
            }
        }
        return errInfo;
    }
    
    /**
     * <li>说明：导入单个sheet中的配件
     * <li>创建人：张迪
     * <li>创建日期：2017-4-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param sheet Excel工作薄中的单个sheet页实例
     * @param processIDX 
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private List<String>  saveByStencil(HSSFSheet sheet, String processIDX) throws BusinessException, NoSuchFieldException {
        List<String> errInfo = new ArrayList<String>();
        String[][] tableValues = ExcelUtil.getTableValue(sheet, "5A");
        if (null == tableValues || tableValues.length <= 0) {
            errInfo.add("未找到名为『下车配件清单』的工作簿内的数据，导入失败！");
            return errInfo;
        }
        List<JobProcessPartsOffList> list = new ArrayList<JobProcessPartsOffList>();
        JobProcessPartsOffList jb = null;
        String[] values = null;
        // 实体初始化，并判定是否是叶子节点
        for (int i = 0; i < tableValues.length; i++) {
            jb = new JobProcessPartsOffList();
            jb.setProcessIdx(processIDX);
            values = tableValues[i];
            String  jcpjbm = jcpjzdBuildManager.getJcpjbm(values[0]);
            if(StringUtil.isNullOrBlank(jcpjbm)){
                errInfo.add("未找到名为『"+values[0]+"』的配件，导入失败！");
                continue;
            }else{
                jb.setPartsId(jcpjbm);
                jb.setPartsName(values[0]);
            }
            EquipPart  wz = equipPartManage.getEquipPartByName(values[1]);
            if(null == wz || StringUtil.isNullOrBlank(wz.getPartId())){
                errInfo.add("未找到名为『"+values[1]+"』的位置，导入失败！");
                continue;
            }
            jb.setWzdm(wz.getPartId());
            jb.setWzmc(values[1]);
            if(null != validateUpdate(jb)){
                errInfo.add("配件【"+ jb.getPartsName()+"】位置为【"+ jb.getWzmc()+ "】已经存在！"); 
                continue;
            } 
            list.add(jb);
        }
        if(list != null && list.size()>0){
            this.saveOrUpdate(list);
        }else{
            errInfo.add("没有可导入的数据！"); 
        }
        return errInfo;
    }

    
}
