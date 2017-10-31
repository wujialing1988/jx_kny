package com.yunda.frame.yhgl.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OrganizationForImport;
import com.yunda.third.poi.excel.ExcelUtil;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 导入机构人员
 * <li>创建人：何东
 * <li>创建日期：2016-09-01
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="orgEmpImportManager")
public class OrgEmpImportManager extends JXBaseManager<OrganizationForImport,OrganizationForImport>{
	/** 机构导入excel sheet名称 */
	public final static String ORG_IMPORT_SHEET_NAME = "机构";
	/** 人员导入excel sheet名称 */
	public final static String EMP_IMPORT_SHEET_NAME = "人员";
	/** 字典数据状态 */
	public final static String DICT_STATUS = "1";
	/** 字典数据类型：机构等级 */
	public final static String DICT_TYPE_ABF_ORGDEGREE = "ABF_ORGDEGREE";
	/** 字典数据类型：机构类型 */
	public final static String DICT_TYPE_ABF_ORGTYPE = "ABF_ORGTYPE";
	/** 字典数据类型：机构状态 */
	public final static String DICT_TYPE_ABF_ORGSTATUS = "ABF_ORGSTATUS";
	/** 字典数据类型：性别 */
	public final static String DICT_TYPE_ABF_GENDER = "ABF_GENDER";
	/** 字典数据类型：人员状态 */
	public final static String DICT_TYPE_ABF_EMPSTATUS = "ABF_EMPSTATUS";
	/** 字典数据类型：政治面貌 */
	public final static String DICT_TYPE_ABF_PARTYVISAGE = "ABF_PARTYVISAGE";
	/** 字典数据类型：职级 */
	public final static String DICT_TYPE_ABF_EMPZC = "ABF_EMPZC";
	/** 字典数据类型：证件类型 */
	public final static String DICT_TYPE_ABF_CARDTYPE = "ABF_CARDTYPE";
	
	/** 人员维护管理 */
	@Resource(name="employeeManager")
	private EmployeeManager employeeManager;
	
	/**
     * <li>说明：判断组织表中是否存在数据，有数据不允许导入
     * <li>创建人：何东
     * <li>创建日期：2016-09-01
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     */
	public boolean isCanImport() {
		String sql = " select count(ORGID) from OM_ORGANIZATION";
		List list = this.daoUtils.executeSqlQuery(sql);
		if (!CollectionUtils.isEmpty(list)) {
			int count = Integer.valueOf(list.get(0).toString());
			if (count > 0) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
     * <li>说明：导入机构人员
     * <li>创建人：何东
     * <li>创建日期：2016-09-01
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param orgEmpImport 导入机构人员文件
     * @throws FileNotFoundException
     * @throws IOException
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public synchronized void saveImport(File orgEmpImport) throws FileNotFoundException, IOException, BusinessException, NoSuchFieldException {
        POIFSFileSystem poi = null;
        try {
            poi = new POIFSFileSystem(new FileInputStream(orgEmpImport));
        } catch (Exception e) {
            throw new BusinessException("Excel文件解析异常，请确认上传的文件格式是否正确！");
        }
        
        HSSFWorkbook workBook = new HSSFWorkbook(poi);
    	
        try {
    		int sheetCount = workBook.getNumberOfSheets();
    		HSSFSheet sheet = null;
    		for (int i = 0; i < sheetCount; i++) {
        		sheet = workBook.getSheetAt(i);
        		
            	if (ORG_IMPORT_SHEET_NAME.equals(sheet.getSheetName())) {
            		if (isCanImport()) {
            			saveOrgs(sheet);
            		}
            	} else if (EMP_IMPORT_SHEET_NAME.equals(sheet.getSheetName())) {
            		saveEmps(sheet);
            	}
    		}
        } catch (Exception e) {
        	throw new BusinessException(e.getMessage());
        }
    }
    
    /**
     * <li>说明：导入机构数据
     * <li>创建人：何东
     * <li>创建日期：2016-09-01
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param sheet Excel工作薄中的单个sheet页实例
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private void saveOrgs(HSSFSheet sheet) throws BusinessException, NoSuchFieldException {
        String[][] tableValues = ExcelUtil.getTableValue(sheet, "2A");
        if (null == tableValues || tableValues.length <= 0) {
            return;
        }
        
        List<OrganizationForImport> list = new ArrayList<OrganizationForImport>();
        
        // 首先将excel中的数据封装到对象集合
        OrganizationForImport org = null;
        OrganizationForImport root = null;
        try {
            for (String[] values : tableValues) {
            	org = new OrganizationForImport();
            	org.setOrgid(Long.parseLong(values[0]));
            	org.setOrgcode(values[2]);
            	org.setOrgname(values[3]);
            	
            	if (StringUtils.isNotBlank(values[4])) {
            		org.setOrgdegree(queryDict(DICT_TYPE_ABF_ORGDEGREE, values[4]));
            	} else {
            		throw new BusinessException("机构等级不能为空，请确认数据内容是否正确！");
            	}
            	
            	if (StringUtils.isNotBlank(values[5])) {
            		org.setStatus(queryDict(DICT_TYPE_ABF_ORGSTATUS, values[5]));
            	} else {
            		throw new BusinessException("机构状态不能为空，请确认数据内容是否正确！");
            	}
            	
            	if (StringUtils.isNotBlank(values[6])) {
            		org.setOrgtype(queryDict(DICT_TYPE_ABF_ORGTYPE, values[6]));
            	}
            	
            	if (StringUtils.isNotBlank(values[7])) {
            		org.setSortno(Long.parseLong(values[7]));
            	}
            	
            	if (StringUtils.isNotBlank(values[1])) {
            		org.setParentorgid(Long.parseLong(values[1]));
            		list.add(org);
            	} else {
            		root = org;
            	}
            }
		} catch (Exception e) {
			throw new BusinessException("Excel文件解析异常，请确认数据内容是否正确！");
		}
		
		// 从根开始，递归保存各层数据
		saveOrderByParentId(list, root);
		
    }
    
    /**
     * <li>说明：导入机构数据
     * <li>创建人：何东
     * <li>创建日期：2016-09-01
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param list 机构集合
     */
    private void saveOrderByParentId(List<OrganizationForImport> list, OrganizationForImport org){
		// 首先保存自己（父节点）
    	if (org != null) {
    		saveOrg(org);
    	}
    	
    	// 然后保存子节点
    	for (OrganizationForImport organization : list) {
			if (organization.getParentorgid() != null && organization.getParentorgid().compareTo(org.getOrgid()) == 0) {
				saveOrderByParentId(list, organization);
			}
		}
    }
    
    /**
	 * <li>说明：导入时插入机构数据到数据库
	 * <li>创建人：何东
	 * <li>创建日期：2016-09-01
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param T t 实体对象
	 * @return void 
	 * @throws BusinessException, NoSuchFieldException
	 */	
	public void saveOrg(OrganizationForImport org) {
		Date now = new Date();  
		OrganizationForImport porg = null;
		
		org.setIsleaf("y"); //新增数据为叶子级别机构
		org.setCreatetime(now); //创建时间
		org.setOrglevel(1L);//初始设置orgLevel
		org.setSubcount(0L);//新增机构没有子机构
		if(org.getParentorgid()!=null){
			porg = this.getModelById(org.getParentorgid());//获取父级机构实体对象
			if(porg!=null){
				porg.setIsleaf("n");
				Long count = porg.getSubcount()==null?0L:porg.getSubcount();
				porg.setSubcount(count+1); //父级机构的总子机构数+1
				this.daoUtils.update(porg); //更新父机构
				org.setOrglevel(porg.getOrglevel() == null ? 1L : (porg.getOrglevel()+1)); //更新子机构的orglevel = 父机构orglevel+1
			} else {
				org.setOrglevel(1L); //更新子机构的orglevel，没有父机构orglevel，则子机构从1级开始
			}
		}
		this.daoUtils.getHibernateTemplate().save(org);
		//当子机构保存之后，获取其ID
		if(porg!=null){
			String porgseq = StringUtil.isNullOrBlank(porg.getOrgseq())?"":porg.getOrgseq();
			org.setOrgseq(porgseq.concat(String.valueOf(org.getOrgid())).concat(".")); //根据父机构orgseq，产生子机构的orgseq
		} else {
			org.setOrgseq("."+org.getOrgid()+".");
		}
		
		org.setLastupdate(now); //最后更新时间
		org.setUpdator(SystemContext.getAcOperator().getOperatorid()); //当前操作人ID
		this.daoUtils.update(org);
	}
    
    /**
     * <li>说明：业务字典查询
     * <li>创建人：何东
     * <li>创建日期：2016-09-01
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param dictTypeId 业务字典类型编号
     * @param dictname 业务字典名称
     * @return 业务字典ID
     */
    @SuppressWarnings("unchecked")
    public String queryDict(String dictTypeId, String dictname) {
        StringBuilder queryString = new StringBuilder();
        queryString.append(" From EosDictEntry Where status = '" + DICT_STATUS + "' and id.dicttypeid='" + dictTypeId + "'");
        queryString.append(" and dictname = '" + dictname + "'");
        
        List<EosDictEntry> list = getDaoUtils().find(queryString.toString());
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0).getId().getDictid();
        }
        
        return null;
    }
    
    /**
     * <li>说明：导入人员数据
     * <li>创建人：何东
     * <li>创建日期：2016-09-01
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param sheet Excel工作薄中的单个sheet页实例
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    private void saveEmps(HSSFSheet sheet) throws Exception {
        String[][] tableValues = ExcelUtil.getTableValue(sheet, "2A");
        if (null == tableValues || tableValues.length <= 0) {
            return;
        }
        
        OmEmployee emp = null;
        for (String[] values : tableValues) {
        	if (StringUtils.isBlank(values[0]))
        		throw new BusinessException("人员代码不能为空，请确认数据内容是否正确！");
        	
        	// 如果存在进行更新
        	StringBuilder hql = new StringBuilder(" from OmEmployee e");
        	hql.append(" where e.empcode = '");
        	hql.append(values[0]);
        	hql.append("'");
        	
        	List<OmEmployee> list = this.daoUtils.find(hql.toString());
        	if (!CollectionUtils.isEmpty(list)) {
        		emp = list.get(0);
            } else {
            	emp = new OmEmployee();
            }
        	
        	emp.setEmpcode(values[0]);
        	emp.setEmpname(values[1]);
        	
        	if (StringUtils.isNotBlank(values[2]))
        		emp.setGender(queryDict(DICT_TYPE_ABF_GENDER, values[2]));
        	
        	// 通过机构名称查询机构ID
        	StringBuilder orgHql = new StringBuilder(" from OrganizationForImport o");
        	orgHql.append(" where o.orgname = '");
        	orgHql.append(values[3]);
        	orgHql.append("'");
        	
        	List<OrganizationForImport> orgList = this.daoUtils.find(orgHql.toString());
        	if (!CollectionUtils.isEmpty(orgList))
        		emp.setOrgid(orgList.get(0).getOrgid());

        	if (StringUtils.isNotBlank(values[4]))
        		emp.setBirthdate(DateUtil.parse(values[4]));
        	
        	emp.setOtel(StringUtils.isNotBlank(values[5]) ? values[5] : null);
        	emp.setOemail(StringUtils.isNotBlank(values[6]) ? values[6] : null);
        	emp.setMobileno(StringUtils.isNotBlank(values[7]) ? values[7] : null);
        	emp.setPemail(StringUtils.isNotBlank(values[8]) ? values[8] : null);
        	
        	if (StringUtils.isNotBlank(values[9]))
        		emp.setEmpstatus(queryDict(DICT_TYPE_ABF_EMPSTATUS, values[9]));
        	
        	if (StringUtils.isNotBlank(values[10]))
            	emp.setParty(queryDict(DICT_TYPE_ABF_PARTYVISAGE, values[10]));
        	
        	if (StringUtils.isNotBlank(values[11]))
            	emp.setDegree(queryDict(DICT_TYPE_ABF_EMPZC, values[11]));
        	
        	if (StringUtils.isNotBlank(values[12]))
            	emp.setCardtype(queryDict(DICT_TYPE_ABF_CARDTYPE, values[12]));
        	
        	emp.setCardno(StringUtils.isNotBlank(values[13]) ? values[13] : null);
        	
        	if (StringUtils.isNotBlank(values[14]))
        		emp.setIndate(DateUtil.parse(values[14]));
        	
        	if (StringUtils.isNotBlank(values[15]))
        		emp.setOutdate(DateUtil.parse(values[15]));
        	
        	employeeManager.saveOrUpdate(emp);
		}
    }
}
