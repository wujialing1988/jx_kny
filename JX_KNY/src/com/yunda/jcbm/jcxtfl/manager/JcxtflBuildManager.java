package com.yunda.jcbm.jcxtfl.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.yunda.baseapp.ChineseCharToEn;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jcbm.jcgx.entity.JcgxBuild;
import com.yunda.jcbm.jcxtfl.entity.JcxtflBuild;
import com.yunda.third.poi.excel.ExcelUtil;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JcxtflBuild业务类，机车系统分类
 * <li>创建人：王利成
 * <li>创建日期：2016-5-16
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value = "jcxtflBuildManager")
public class JcxtflBuildManager extends JXBaseManager<JcxtflBuild, JcxtflBuild> {
    
	// 根节点的父节点ID
	public final static String PARENT_ID_OF_ROOT = "1";
	
    /**
     * <li>说明：查询机车类别分类树
     * <li>创建人：王利成
     * <li>创建日期：2016-5-20
     * <li>修改人：
     * <li>修改日期：
     * @param parentIDX 父节点
     * @param shortName 车型
     * @return List<HashMap<String,Object>>
     */
    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> tree(String parentIDX, String shortName) {
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        StringBuilder hql = new StringBuilder(" from JcxtflBuild where fjdID = ? ");
        if(!"".equals(shortName)){        	
        	hql.append(" And '/' || sycx || '/' Like '%/").append(shortName).append("/%'");
        }
        hql.append(" And recordStatus = " + Constants.NO_DELETE + " ");
        List<JcxtflBuild> list = (List<JcxtflBuild>) this.daoUtils.find(hql.toString(), new Object[] { parentIDX });
        for (JcxtflBuild t : list) {
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            nodeMap.put("id", t.getCoID());     // 节点idx主键
            nodeMap.put("text", "(" + t.getFlbm() +")" + t.getFljc());   // 树节点显示名称
            nodeMap.put("leaf", (t.getCoHaschild() == JcgxBuild.CO_HAS_CHILD_F));
            children.add(nodeMap);
        }
        return children;
    }
    
    /**
     * <li>说明：根据分类编码和试用车型查询系统机车
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-21
     * <li>修改人：
     * <li>修改日期：
     * @param flbm 父节点
     * @param sycx 车型
     * @return List<HashMap<String,Object>>
     */
    public JcxtflBuild getJcxtflBuildByBmAndSycx(String flbm,String sycx){
        StringBuilder hql = new StringBuilder(" from JcxtflBuild where flbm = ? ");
        hql.append(" And recordStatus = " + Constants.NO_DELETE + " ");
        hql.append(" And '/' || sycx || '/' Like '%/").append(sycx).append("/%'");
        return (JcxtflBuild)this.daoUtils.findSingle(hql.toString(), new Object[] { flbm });
    }
    
    /**
     * <li>说明：递归获取分类树的子节点（递归）
     * <li>创建人：王利成
     * <li>创建日期：2016-5-19
     * <li>修改人：
     * <li>修改日期：
     * @param idx 机车分类构型主键
     * @return JcxtflBuild
     */
    @SuppressWarnings("unchecked")
    public JcxtflBuild getEntityByIDX(String idx) {
        JcxtflBuild entity = this.getModelById(idx);
        String hql = "From JcxtflBuild Where recordStatus = 0 And fjdID = ?";
        List<JcxtflBuild> list = this.daoUtils.find(hql, new Object[] { idx });
        if (null == list || list.isEmpty()) {
            return entity;
        }
        entity.setChildren(list);
        for (JcxtflBuild jb : list) {
            getEntityByIDX(jb.getCoID());
        }
        return entity;
    }
    
    /**
     * <li>说明：导入机车构型分类
     * <li>创建人：何涛
     * <li>创建日期：2016-5-23
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param jcxtfl 导入机车构型分类文件
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
     * <li>说明：导入单个sheet中的机车构型系统分类
     * <li>创建人：何涛
     * <li>创建日期：2016-5-23
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param sheet Excel工作薄中的单个sheet页实例
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private void saveByStencil(HSSFSheet sheet) throws BusinessException, NoSuchFieldException {
        String[][] tableValues = ExcelUtil.getTableValue(sheet, "2B");
        if (null == tableValues) {
            return;
        }
        List<JcxtflBuild> list = parseTableValues(tableValues);
        if (null == list || list.size() <= 0) {
            return;
        }
        this.daoUtils.bluckInsert(list);
//        this.saveOrUpdate(list);
    }
    
    /**
     * <li>说明：解析sheet单元格数据为java对象实体集合
     * <li>创建人：何涛
     * <li>创建日期：2016-5-23
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param tableValues sheet单元格数据
     * @return 机车系统分类实体集合
     */
    @SuppressWarnings("unused")
    private List<JcxtflBuild> parseTableValues(String[][] tableValues) {
        List<JcxtflBuild> list = new ArrayList<JcxtflBuild>();
        JcxtflBuild jb = null;
        String[] values = null;
        for (int i = 0; i < tableValues.length; i++) {
            values = tableValues[i];
            jb = new JcxtflBuild();
            jb.setCoID(values[0]);
            jb.setFjdID(values[1]);
            jb.setFlbm(values[2]);
            jb.setFlmc(values[3]);  // 分类名称【FLMC】
            // 拼音简称【PYJC】
            if (StringUtil.isNullOrBlank(values[4])) {
                jb.setPyjc(ChineseCharToEn.getInstance().getAllFirstLetter(values[3]));
            } else {
                jb.setPyjc(values[4]);
            }
            jb.setLbjbm(values[5]);
            jb.setSycx(values[6]);
            jb.setZylxID(values[7]);
            jb.setZylx(values[8]);
            jb.setCoHaschild(StringUtil.isNullOrBlank(values[9]) ? JcgxBuild.CO_HAS_CHILD_F : Integer.valueOf(values[9].trim()));
            jb.setJxzy(StringUtil.isNullOrBlank(values[10]) ? null : Integer.valueOf(values[10].trim()));
            jb.setSfsyDzda(StringUtil.isNullOrBlank(values[11]) ? null : Integer.valueOf(values[11].trim()));
            jb.setSfzbzy(StringUtil.isNullOrBlank(values[12]) ? null : Integer.valueOf(values[12].trim()));
            jb.setRecordStatus(StringUtil.isNullOrBlank(values[13].trim()) ? null : Integer.valueOf(values[13].trim()));
            jb.setFljc(values[14]);
            list.add(jb);
        }
        return list;
    }
    
    /**
     * 
     * <li>说明：级联删除机车系统分类及子分类
     * <li>创建人：曾雪
     * <li>创建日期：2016-7-8
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
        String fjdId = "";
        for (int i = 0; i < ids.length; i++) {
            sb.append(Constants.JOINSTR).append(Constants.SINGLE_QUOTE_MARK).append(ids[i]).append(Constants.SINGLE_QUOTE_MARK);
            JcxtflBuild jcxtfl = this.getModelById(ids[i]);
            fjdId = jcxtfl.getFjdID();
        }
       
        StringBuffer sql = new StringBuffer("SELECT CO_ID FROM T_JCBM_JCXTFL WHERE RECORD_STATUS = 0");
        sql.append(" START WITH CO_ID in(" + sb.substring(1) + ") CONNECT BY PRIOR CO_ID = FJD_ID");
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
        
        //查询删除记录的父节点是否还存在子节点，如果有，则改变父节点的coHaschild（是否有子节点）字段
        if(!"".equals(fjdId)){
    		JcxtflBuild fjd = this.getModelById(fjdId);
    		if(fjd != null){
    			List<Object> fjdlist = this.daoUtils.executeSqlQuery("SELECT * FROM T_JCBM_JCXTFL WHERE RECORD_STATUS = 0 and FJD_ID = '" + fjd.getCoID() + "'");
    			if(fjdlist.size() == 0){
    				fjd.setCoHaschild(0);
    				this.update(fjd);
    			}
    		}
        }
    }
    
    /**
     * 
     * <li>说明：添加前验证
     * <li>创建人：曾雪
     * <li>创建日期：2016-7-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t
     */
    public void validateSave(JcxtflBuild t){
        String hql = "From JcxtflBuild Where flbm = ? ";
        JcxtflBuild entity = (JcxtflBuild) this.daoUtils.findSingle(hql, new Object[]{ t.getFlbm()});
        if (null != entity && entity.getFlbm().equals(t.getFlbm())) {
            throw new BusinessException("不可以添加编码重复的数据！");
        }
        
    }
    
    /**
     * 
     * <li>说明：重写保存方法，使新增加的机车系统分类ID为UUID，判断保存的数据是否有父节点，如有，则改变
     * 父节点CoHaschild字段
     * <li>创建人：曾雪
     * <li>创建日期：2016-7-20
     * <li>修改人： 何东
     * <li>修改日期：2016-08-30
     * <li>修改内容：更新所有未设定专业类型的子节点的专业类型与当前更新对象一致
     * @param t
     */
    @Override
    public void saveOrUpdate(JcxtflBuild t) throws BusinessException, NoSuchFieldException {
        if (StringUtil.isNullOrBlank(t.getPyjc())) {
            // 拼音简称【PYJC】
            t.setPyjc(ChineseCharToEn.getInstance().getAllFirstLetter(t.getFlmc()));
            // 将数据转成全小写
            if(!StringUtil.isNullOrBlank(t.getPyjc())){
                t.setPyjc(t.getPyjc().toLowerCase());
            }
        }
    	if (StringUtil.isNullOrBlank(t.getCoID())) {
    		t.setCoID(UUID.randomUUID().toString().replace("-", ""));              
    	}
    	super.saveOrUpdate(t);
    	if(!StringUtil.isNullOrBlank(t.getFjdID()) && !"1".equals(t.getFjdID())){
    		JcxtflBuild jcxtfl = this.getEntityByIDX(t.getFjdID());
    		if(jcxtfl != null){
    			if(jcxtfl.getCoHaschild() != null && jcxtfl.getCoHaschild() != 1){
    				jcxtfl.setCoHaschild(1);
    				super.saveOrUpdate(jcxtfl);
    			}
    		}
    	}
    	
    	// 更新所有未设定专业类型的子节点的专业类型与当前更新对象一致
    	if (StringUtils.isNotBlank(t.getZylxID())) {
    		final StringBuilder updateChildren = new StringBuilder(" update T_JCBM_JCXTFL");
    		updateChildren.append(" set ZYLX_ID = ?, ZYLX = ?");
    		updateChildren.append(" where co_id in ");
    		updateChildren.append(" (");
    		updateChildren.append(" select t.co_id from T_JCBM_JCXTFL t");
    		updateChildren.append(" where t.record_status = " + Constants.NO_DELETE + " and t.zylx_id is null and t.co_id <> ?");
    		updateChildren.append(" start with t.co_id = ? connect by t.fjd_id = prior t.co_id");
    		updateChildren.append(")");
    		
    		final String[] values = new String[]{t.getZylxID(), t.getZylx(), t.getCoID(), t.getCoID()};
    		
    		this.daoUtils.getHibernateTemplate().execute(new HibernateCallback(){
	            public Integer doInHibernate(Session s) {
	        		Query query = s.createSQLQuery(updateChildren.toString());
	        		if (values != null && values.length != 0){
	        			for (int i = 0; i < values.length; i++) {
	            			query.setParameter(i, values[i]);
	            		}
	        		}
	        		return query.executeUpdate();
	            }
	        });
    	}
    }
    
    /**
     * <li>说明：导出车型对应分类数据
     * <li>创建人：何东
     * <li>创建日期：2016-08-30
     * <li>修改人：
     * <li>修改日期：
     * @param shortName 车型
     * @return HSSFWorkbook
     */
    @SuppressWarnings("unchecked")
    public HSSFWorkbook exportClassificationByShortName(String shortName) {
    	if(StringUtils.isBlank(shortName)){
        	return null;
        }
    	
        StringBuilder sql = new StringBuilder(" select t.co_id, t.fjd_id, t.flbm, t.flmc, t.fljc");
        sql.append(" from T_JCBM_JCXTFL t");
        sql.append(" where t.record_status = " + Constants.NO_DELETE);
        sql.append(" and '/' || sycx || '/' like '%/" + shortName + "/%'");
        sql.append(" start with t.co_id = ");
        sql.append(" (");
        sql.append(" select co_id from T_JCBM_JCXTFL");
        sql.append(" where record_status = " + Constants.NO_DELETE);
        sql.append(" and fjd_id = '" + PARENT_ID_OF_ROOT + "'");
        sql.append(" and '/' || sycx || '/' like '%/" + shortName + "/%'");
        sql.append(")");
        sql.append(" connect by t.fjd_id = prior t.co_id");
        sql.append(" order by t.co_id, t.fjd_id");
        
        List<Object[]> records = (List<Object[]>)this.daoUtils.executeSqlQuery(sql.toString());
        
        if (!CollectionUtils.isEmpty(records)) {
        	HSSFWorkbook wb = new HSSFWorkbook();
    		HSSFSheet sheet = wb.createSheet(shortName);
    		
    		// 表头行字体
    		HSSFFont headFont = wb.createFont();
    		headFont.setFontName("宋体");
    		headFont.setFontHeightInPoints((short) 10);
    		headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    		headFont.setCharSet(HSSFFont.DEFAULT_CHARSET);
    		
    		// 表头行样式
    		CellStyle headStyle = wb.createCellStyle();
    		headStyle.setAlignment(CellStyle.ALIGN_CENTER);
    		headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    		headStyle.setFont(headFont);
    		headStyle.setFillBackgroundColor(HSSFColor.LIGHT_TURQUOISE.index);
    		headStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
    		headStyle.setBorderBottom(CellStyle.BORDER_THIN);
    		headStyle.setBorderLeft(CellStyle.BORDER_THIN);
    		headStyle.setBorderRight(CellStyle.BORDER_THIN);
    		headStyle.setTopBorderColor(HSSFColor.BLACK.index);
    		headStyle.setBottomBorderColor(HSSFColor.BLACK.index);
    		headStyle.setLeftBorderColor(HSSFColor.BLACK.index);
    		headStyle.setRightBorderColor(HSSFColor.BLACK.index);
    		
    		// 内容行字体
    		HSSFFont contentFont = wb.createFont();
    		contentFont.setFontName("宋体");
    		contentFont.setFontHeightInPoints((short) 10);
    		contentFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
    		contentFont.setCharSet(HSSFFont.DEFAULT_CHARSET);
    		
    		// 内容行样式
    		CellStyle contentStyle = wb.createCellStyle();
    		contentStyle.setAlignment(CellStyle.ALIGN_LEFT);
    		contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    		contentStyle.setFont(contentFont);
    		contentStyle.setBorderTop(CellStyle.BORDER_THIN);
    		contentStyle.setBorderBottom(CellStyle.BORDER_THIN);
    		contentStyle.setBorderLeft(CellStyle.BORDER_THIN);
    		contentStyle.setBorderRight(CellStyle.BORDER_THIN);
    		contentStyle.setTopBorderColor(HSSFColor.BLACK.index);
    		contentStyle.setBottomBorderColor(HSSFColor.BLACK.index);
    		contentStyle.setLeftBorderColor(HSSFColor.BLACK.index);
    		contentStyle.setRightBorderColor(HSSFColor.BLACK.index);
    		//contentStyle.setWrapText(true); // 字段换行
    		
    		String[] excelHeader = {"主键", "父节点ID", "分类编码", "分类名称", "分类简称"};
    		
    		// 单元格列宽
    		int[] excelHeaderWidth = {120, 120, 120, 300, 300};
    		
    		// 设置列宽度（像素）
    		for (int i = 0; i < excelHeaderWidth.length; i++) {
    		    sheet.setColumnWidth(i, 40 * excelHeaderWidth[i]);
    		}
    		
    		// 创建表头行
    		HSSFRow headRow = sheet.createRow(0);
    		headRow.setHeight((short) 400);
    		for (int i = 0; i < excelHeader.length; i++) {
    		    HSSFCell cell = headRow.createCell(i);
    		    cell.setCellValue(excelHeader[i]);
    		    cell.setCellStyle(headStyle);
    		}
    		
			for (int i = 0; i < records.size(); i++) {
				Object[] columns = records.get(i);
				String coId = columns[0].toString();
				String fjdId = columns[1].toString();
				String flbm = columns[2].toString();
				String flmc = columns[3].toString();
				String fljc = columns[4].toString();
				
				HSSFRow contexRow = sheet.createRow(i + 1);
				contexRow.setHeight((short) 380);
				
				HSSFCell coIdCell = contexRow.createCell(0);
				coIdCell.setCellValue(coId);
				coIdCell.setCellStyle(contentStyle);
				
				HSSFCell fjdIdCell = contexRow.createCell(1);
				fjdIdCell.setCellValue(fjdId);
				fjdIdCell.setCellStyle(contentStyle);
				
				HSSFCell flbmCell = contexRow.createCell(2);
				flbmCell.setCellValue(flbm);
				flbmCell.setCellStyle(contentStyle);
				
				HSSFCell flmcCell = contexRow.createCell(3);
				flmcCell.setCellValue(flmc);
				flmcCell.setCellStyle(contentStyle);
				
				HSSFCell fljcCell = contexRow.createCell(4);
				fljcCell.setCellValue(fljc);
				fljcCell.setCellStyle(contentStyle);
			}
    		
    		return wb;
        }
        
        return null;
    }
    
    
    /**
     * <li>说明：验证分类编码与试用车型唯一
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-21
     * <li>修改人：
     * <li>修改日期：
     * @param t 机车构型
     * @return String[] 验证信息
     */
    @Override
    public String[] validateUpdate(JcxtflBuild t) {
        String[] errorMsg = super.validateUpdate(t);
        if (null != errorMsg) {
            return errorMsg;
        }
        String[] sycxs = t.getSycx().split("/");
        if(sycxs != null && sycxs.length > 0){
            for (String sycx : sycxs) {
                JcxtflBuild build = this.getJcxtflBuildByBmAndSycx(t.getFlbm(), sycx);
                // 去除修改时的判断，即ID等于查询出来的ID
                if(build != null && !build.getCoID().equals(t.getCoID())){
                    return new String[]{"分类编码【" + t.getFlbm() + "】中存在试用车型【"+ sycx + "】，不能重复添加！"};
                }
            }
        }
        return null;
    }
    
}
