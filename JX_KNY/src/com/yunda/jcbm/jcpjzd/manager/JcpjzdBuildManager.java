package com.yunda.jcbm.jcpjzd.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jcbm.jcpjzd.entity.JcpjzdBuild;
import com.yunda.third.poi.excel.ExcelUtil;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JcpjzdBuild业务类，机车零部件
 * <li>创建人：程梅
 * <li>创建日期：2016年7月6日
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value = "jcpjzdBuildManager")
public class JcpjzdBuildManager extends JXBaseManager<JcpjzdBuild, JcpjzdBuild> {
    /**
     * 
     * <li>说明：方法实现功能说明
     * <li>创建人：程梅
     * <li>创建日期：2016-7-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param fjdId
     * @return
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> findJcpjzdTree(String fjdId) throws BusinessException{
    	
        String hql="from JcpjzdBuild ";
        if("ROOT_0".equals(fjdId)){
        	hql += " where fjdId = '" + fjdId + "' or fjdId is null";
        }else{
        	hql += "where fjdId = '" + fjdId + "'";
        }
        
        List<JcpjzdBuild> jcpjzdList = (List<JcpjzdBuild>)daoUtils.find(hql);
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        for (JcpjzdBuild jcpjzd : jcpjzdList) {
            Boolean IsLeaf = this.isLeaf(jcpjzd.getJcpjbm());
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            nodeMap.put("id", jcpjzd.getJcpjbm());
            nodeMap.put("text", jcpjzd.getJcpjmc()+"("+jcpjzd.getJcpjbm()+")");
            nodeMap.put("leaf", IsLeaf);
            nodeMap.put("fjdId", jcpjzd.getFjdId());
            nodeMap.put("jcpjbm", jcpjzd.getJcpjbm());
            nodeMap.put("jcpjmc", jcpjzd.getJcpjmc());
            children.add(nodeMap);
        }
        return children;
    }
    /**
     * 
     * <li>说明：方法实现功能说明
     * <li>创建人：程梅
     * <li>创建日期：2016-7-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx
     * @return
     * @throws BusinessException
     */
    public boolean isLeaf(String jcpjbm) throws BusinessException{
        StringBuffer hql = new StringBuffer();
        hql.append("select count(*) From JcpjzdBuild t") ;
        if(!StringUtil.isNullOrBlank(jcpjbm)){
            hql.append(" where t.fjdId='"+jcpjbm+"'")    ;
        }
        
        int count = daoUtils.getCount(hql.toString());
        return count == 0 ? true : false;
    }
 
    
    /**
     * 
     * <li>说明：导入机车零部件
     * <li>创建人：曾雪
     * <li>创建日期：2016-7-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jcxtfl
     * @throws FileNotFoundException
     * @throws IOException
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    
    public void saveImport(File jcxtfl) throws FileNotFoundException, IOException, BusinessException, NoSuchFieldException {
        POIFSFileSystem poi = null;
        try {
            poi = new POIFSFileSystem(new FileInputStream(jcxtfl));
        } catch (Exception e) {
            throw new BusinessException("Excel文件解析异常，请确认上传的文件格式是否正确！");
        }
        HSSFWorkbook workBook = new HSSFWorkbook(poi);
        int sheetIndex = 0;
        HSSFSheet sheet = workBook.getSheetAt(sheetIndex);
        while (null != sheet) {
            try {
                saveByStencil(sheet);
                sheet = workBook.getSheetAt(++sheetIndex);
            } catch (Exception e) {
                break;
            }
        }
    }
    
    /**
     * 
     * <li>说明：导入单个零部件名称
     * <li>创建人：曾雪
     * <li>创建日期：2016-7-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param sheet
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private void saveByStencil(HSSFSheet sheet) throws BusinessException, NoSuchFieldException {
        String[][] tableValues = ExcelUtil.getTableValue(sheet, "2B");
        if (null == tableValues) {
            return;
        }
        List<JcpjzdBuild> list = parseTableValues(tableValues);
        if (null == list || list.size() <= 0) {
            return;
        }
        this.saveOrUpdate(list);
    }
    
    /**
     * 
     * <li>说明：解析sheet单元格数据为java对象实体集合
     * <li>创建人：曾雪
     * <li>创建日期：2016-7-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param tableValues
     * @return
     */
    @SuppressWarnings("unused")
    private List<JcpjzdBuild> parseTableValues(String[][] tableValues) {
        List<JcpjzdBuild> list = new ArrayList<JcpjzdBuild>();
        JcpjzdBuild jb = null;
        String[] values = null;
        Date date = new Date();
        for (int i = 0; i < tableValues.length; i++) {
            values = tableValues[i];
            jb = new JcpjzdBuild();
            jb.setJcpjbm(values[0]);
            jb.setJcpjmc(values[1]);
            jb.setAbl(values[3]);
            jb.setGjhh(values[4]);
            jb.setPym(values[5]);
            jb.setVersion(Long.valueOf(values[11]));
            jb.setFjdId(values[12]);
            list.add(jb);
        }
        return list;
    }
    
    /**
     * 
     * <li>说明：级联删除子节点
     * <li>创建人：曾雪
     * <li>创建日期：2016-7-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids
     */
    public void deleteByCasecade(Serializable... ids) {
        if (null == ids || ids.length <= 0) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            sb.append(Constants.JOINSTR).append(Constants.SINGLE_QUOTE_MARK).append(ids[i]).append(Constants.SINGLE_QUOTE_MARK);
        }
       
        StringBuffer sql = new StringBuffer("SELECT JCPJBM FROM T_JCBM_JCPJZD ");
        sql.append(" START WITH JCPJBM in(" + sb.substring(1) + ") CONNECT BY PRIOR JCPJBM = FJD_ID");
        List<Object> list = this.daoUtils.executeSqlQuery(sql.toString());
        
        // 每500条记录执行一次删除，因为sql的in条件中，最大支持1000个关键字
        int size = 500;
        List<Serializable> idList = new ArrayList<Serializable>(size);
        int index = 0;
        while (index < list.size()) {
            idList.add((Serializable) list.get(index));
            index++;
            if (size == idList.size()) {
                this.deleteByIds(idList.toArray(new Serializable[size]));
                idList.clear();
            }
        }
        // 如果删除的记录刚好是500的整倍数，则无需执行最后的删除
        if (idList.isEmpty()) {
            return;
        }
        // 删除余下的记录
        this.deleteByIds(idList.toArray(new Serializable[idList.size()]));
    }
    
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：张迪
     * <li>创建日期：2017-4-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jcpjmc
     * @return
     */
    public String getJcpjbm(String jcpjmc ){
        String  jcpjbm = "";
        String hql =" from JcpjzdBuild as jt where jt.jcpjmc = '"+jcpjmc+"'";
        JcpjzdBuild entity =(JcpjzdBuild)daoUtils.findSingle(hql);
        jcpjbm = entity.getJcpjbm();
        return jcpjbm;
    }
}
