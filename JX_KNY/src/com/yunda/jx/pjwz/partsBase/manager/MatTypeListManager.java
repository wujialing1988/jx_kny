package com.yunda.jx.pjwz.partsBase.manager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.baseapp.ChineseCharToEn;
import com.yunda.baseapp.ChineseCharToEn.Sensitive;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.manager.EosDictEntrySelectManager;
import com.yunda.jx.pjwz.partsBase.entity.MatTypeList;
import com.yunda.jx.wlgl.partsBase.manager.WhMatQuotaManager;
import com.yunda.jx.wlgl.stockmanage.manager.MatStockQueryManager;
import com.yunda.third.poi.excel.ExcelReader;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatTypeList业务层, 物料清单
 * <li>创建人：程锐
 * <li>创建日期：2013-10-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@Service(value="matTypeListManager")
public class MatTypeListManager extends JXBaseManager<MatTypeList, MatTypeList>{
    //库存台账查询业务类
    @Resource
    private MatStockQueryManager matStockQueryManager;
    //保有量业务类
    @Resource
    private WhMatQuotaManager whMatQuotaManager ;
	/**
	 * <li>说明：保存验证-物料编码唯一 and 物料描述唯一
	 * <li>创建人：程锐
	 * <li>创建日期：2014-2-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 物料清单实体对象
	 * @return 错误验证信息
	 */
	public void validateSaveOrUpdate(MatTypeList entity) {
        if(!StringUtil.isNullOrBlank(entity.getMatCode())){ //新增时验证
            String hql = "Select count(*) From MatTypeList where matCode='" + entity.getMatCode() + "' ";
            int count = this.daoUtils.getCount(hql);
            if(count > 0){
                throw new BusinessException("物料编码【"+ entity.getMatCode() +"】已存在！");
            }
        }
       if(!StringUtil.isNullOrBlank(entity.getMatDesc())){
          String hqlV = "Select count(*) From MatTypeList where matDesc='" + entity.getMatDesc() + "'";
          int countV = this.daoUtils.getCount(hqlV);
          if(countV > 0){
              throw new BusinessException("物料描述【"+ entity.getMatDesc() +"】已存在！");
          } 
       }
	}
	/**
	 * 
	 * <li>说明：解析excel
	 * <li>创建人：程锐
	 * <li>创建日期：2014-2-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param filePath
	 * @param sheetName
	 * @param startCellX
	 * @param startCellY
	 * @return 二维数组
	 * @throws Exception
	 */
    public String[][] analysisExcel(String filePath, String sheetName, String startCellX,String startCellY) throws Exception{
        try{
            ExcelReader excel = new ExcelReader(filePath);
            String[][] datas = excel.getTableValue(sheetName, startCellX.concat(startCellY));
            excel.close();
            return datas;
        }catch(Exception ex){
            return null;
        }
    }
    /**
     * 
     * <li>说明：是否数字验证方法
     * <li>创建人：程锐
     * <li>创建日期：2014-2-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param str
     * @return 是否数字
     */
    public boolean isNumeric(String str){
//        String a = "^(([0-9]+.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*.[0-9]+)|([0-9]*[1-9][0-9]*))$";
        String a = "^(-?\\d+)(\\.\\d+)?";
        Pattern pattern = Pattern.compile(a);
        return pattern.matcher(str).matches();
    }
    /**
     * 
     * <li>说明：数据验证
     * <li>创建人：程锐
     * <li>创建日期：2014-2-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param datas
     * @param startCellX
     * @param startCellY
     * @return 错误信息
     */
    private List<String> dataVerification (String [][] datas, String startCellX, String startCellY){
        String [] _t = new String []{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R",
                "S","T","U","V","W","X","Y","Z"}; 
        List<String> errInfo = null;
        String key = "";
        if (datas != null && datas.length > 0) {
            for (int i = 0; i < datas.length; i++) {
                for (int j = 0; j < datas[ i ].length-1; j++) {
                    switch(j)
                    {
                        case 0:  //物料编码
                            String matCode = datas[i][j];
                            if(StringUtil.isNullOrBlank(matCode) || matCode.length()>50){
                                if(errInfo == null) errInfo = new ArrayList<String>();
                                key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,物料编码不为空，且长度小于50！";
                                errInfo.add(key);
                            }
                            break;
                        
                        case 1:  //物料描述
                            String matDesc = datas[i][j];
                            if(StringUtil.isNullOrBlank(matDesc) || matDesc.length()>100){
                                if(errInfo == null) errInfo = new ArrayList<String>();
                                key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,物料描述不为空，且长度小于100！";
                                errInfo.add(key);
                            }
                            break;
                        
                        case 2:  //计量单位
                            String unit = datas[i][j];
                            if(StringUtil.isNullOrBlank(unit) || unit.length()>20){
                                if(errInfo == null) errInfo = new ArrayList<String>();
                                key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,计量单位不为空，且长度小于20！";
                                errInfo.add(key);
                            }
                            break;
                        
                        case 3:  //计划单价
                            String price = datas[i][j];
                            if(StringUtils.isNotBlank(price)){
                            	//不是数字
                            	if( !isNumeric(price) || price.length()>12){
                            		 if(errInfo == null) errInfo = new ArrayList<String>();
                                     key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,计划单价栏数据不正确！";
                                     errInfo.add(key);
                            	}
                            }
//                            if(StringUtil.isNullOrBlank(price) || !isNumeric(price) || price.length()>12){
//                                if(errInfo == null) errInfo = new ArrayList<String>();
//                                key = _t[j]+"列"+(Integer.parseInt(startCellX)+i)+"行,计划单价栏数据不正确！";
//                                errInfo.add(key);
//                            }
                            break;                        
                    }
                }
            }
        }
        return errInfo;
    }
    /**
     * 
     * <li>说明：导入
     * <li>创建人：程锐
     * <li>创建日期：2014-2-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param filePath
     * @param sheetName
     * @param startCellX
     * @param startCellY
     * @return 错误信息或null
     * @throws Exception
     */
    public List<String> importData(String filePath, String sheetName, String startCellX, String startCellY) throws Exception{
        List<String> errInfo = null;
        //解析Excel文件，获取数据
        String[][] datas = analysisExcel(filePath, sheetName, startCellX,startCellY);
        if(datas==null){
            errInfo = new ArrayList<String>();
            errInfo.add("未找到名为『"+sheetName+"』的工作簿内的数据，导入失败！");
            return errInfo;
        }
        errInfo = dataVerification(datas, startCellX, startCellY);
        //验证未通过，返回错误信息
        if(errInfo!=null){
            return errInfo;
        } 
        //验证已通过，进行数据存储
        else {
            if (datas != null && datas.length > 0) {
                List<MatTypeList> saveList = new ArrayList<MatTypeList>();  
                for (int i = 0; i < datas.length; i++) {
                    MatTypeList matTypeList = new MatTypeList();
                    for (int j = 0; j < datas[ i ].length-1; j++) {
                        switch(j){
                            case 0:
                                MatTypeList oldMatTypeList = getListByCode(datas[i][j]); //获取已存在物料编码的数据库对象
                                if(oldMatTypeList != null){
                                    matTypeList = oldMatTypeList;                                    
                                } 
                                matTypeList.setMatCode(datas[i][j]);
                                break;
                            case 1:
                                matTypeList.setMatDesc(datas[i][j]);
                                break;
                            
                            case 2:
                            	if(StringUtils.isNotBlank(datas[i][j])){
                            		matTypeList.setUnit(datas[i][j]);
                            	}
                                break;
                            
                            case 3:
                            	if(StringUtils.isNotBlank(datas[i][j])){
                            		matTypeList.setPrice(Double.parseDouble(datas[i][j]));
                            	}
                                break;
                        }
                    }
                    // 保存物料描述首拼
                    matTypeList.setMatDescEn(ChineseCharToEn.getInstance().getAllFirstLetter(matTypeList.getMatDesc(), Sensitive.lower));
                    saveList.add(matTypeList);
                }
                saveOrUpdate(saveList);
            }
            return null;
        }
    }
    /**
     * 
     * <li>说明：根据物料编码获取物料清单
     * <li>创建人：程锐
     * <li>创建日期：2014-2-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param matCode 物料编码
     * @return MatTypeList 物料清单实体对象
     */
    public MatTypeList getListByCode(String matCode){
        String hql = "from MatTypeList where matCode = '" + matCode + "'";
        return (MatTypeList)daoUtils.findSingle(hql);
    }
    
    /**
     * <li>说明：逻辑删除
     * <li>创建人：程锐
     * <li>创建日期：2014-2-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 主键数组
     */
    public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
        List<MatTypeList> entityList = new ArrayList<MatTypeList>();
        for (Serializable id : ids) {
            MatTypeList t = getListByCode(id.toString());
            t = EntityUtil.setSysinfo(t);
//          设置逻辑删除字段状态为已删除
            t = EntityUtil.setDeleted(t);
            entityList.add(t);
        }
        this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
    }
    
    /**
	 * <li>说明：根据物料编码和库房idx主键查询物料清单信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-25
	 * <li>修改人： 何涛
	 * <li>修改日期：2014-09-29
	 * <li>修改内容：增加库房过滤，查询库房已有的物料信息
	 * @param matCode 物料编码
	 * @param whIdx 库房idx主键
	 * @return 物料清单实体集合
	 */
	@SuppressWarnings("unchecked")
	public List getModelByMatCode(String matCode, String whIdx) {
		String hql = null;
		Object[] args = null;
		if (StringUtil.isNullOrBlank(whIdx)) {
			hql = "From MatTypeList Where matCode = ?";
			args = new Object[] { matCode };
		} else {
			hql = "Select new MatTypeList(a.matCode, a.matDesc, a.unit, a.price, b.qty) From MatTypeList a, MatStock b Where a.matCode = b.matCode And b.qty > 0 And b.matCode = ? And b.whIdx = ?";
			args = new Object[] { matCode, whIdx };
		}
		return this.daoUtils.find(hql, args);
	}
	
	 
    /**
	 * <li>说明：根据查询实体和库房idx主键查询物料清单信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchEntity 查询实体封装对象
	 * @param whIdx 库房idx主键
	 * @return 物料清单分页对象
	 * @throws BusinessException
	 */
	public Page<MatTypeList> findPageList(SearchEntity<MatTypeList> searchEntity, String whIdx) throws BusinessException {
		if (StringUtil.isNullOrBlank(whIdx)) {
			return this.findPageList(searchEntity);
		}
		StringBuilder sb = new StringBuilder();
		MatTypeList entity = searchEntity.getEntity();
		// 查询条件 - 库房idx主键
		sb.append("Select new MatTypeList(a.matCode, a.matDesc, a.unit, a.price, b.qty) From MatTypeList a, MatStock b Where a.matCode = b.matCode And b.qty > 0");
		sb.append(" And b.whIdx = '").append(whIdx).append("'");
		// 查询条件 - 物料编码
		if (!StringUtil.isNullOrBlank(entity.getMatCode())) {
			sb.append(" And a.matCode like '%").append(entity.getMatCode()).append("%'");
		}
		// 查询条件 - 物料描述
		if (!StringUtil.isNullOrBlank(entity.getMatDesc())) {
			sb.append(" And a.matDesc like '%").append(entity.getMatDesc()).append("%'");
		}
		// 排序字段
		Order[] orders = searchEntity.getOrders();
		if (null != orders && orders.length > 0) {
			sb.append(" Order By ");
			sb.append("a." + orders[0].toString());
			for (int i = 1; i < orders.length; i++) {
				sb.append(", ");
				sb.append("a." + orders[i].toString());
			}
		} else {
			sb.append(" Order By a.matCode");
		}
		String hql = sb.toString();
		String totalHql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
		return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}
	
	 /**
	 * <li>说明：分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-31
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchEntity 查询实体
	 * @return 物料清单分页对象
	 * @throws BusinessException
	 */
	@Override
	public Page<MatTypeList> findPageList(SearchEntity<MatTypeList> searchEntity) throws BusinessException {
		StringBuilder sb = new StringBuilder();
		MatTypeList entity = searchEntity.getEntity();
			sb.append("From MatTypeList Where 1=1 ");
		// 查询条件 - 物料编码
		if (!StringUtil.isNullOrBlank(entity.getMatCode())) {
			sb.append(" And matCode like '%").append(entity.getMatCode()).append("%'");
		}
		// 查询条件 - 物料描述
		if (!StringUtil.isNullOrBlank(entity.getMatDesc())) {
			sb.append(" And matDesc like '%").append(entity.getMatDesc()).append("%'");
		}
		// 查询条件 - 物料编码的唯一性
		if (!StringUtil.isNullOrBlank(entity.getRdpNodeStr())) {
		    sb.append(" And matCode not in(").append(entity.getRdpNodeStr()).append(")");
		}
		// 排序字段
		Order[] orders = searchEntity.getOrders();
		if (null != orders && orders.length > 0) {
			sb.append(" Order By ");
			sb.append(orders[0].toString());
			for (int i = 1; i < orders.length; i++) {
				sb.append(", ");
				sb.append(orders[i].toString());
			}
		} else {
			sb.append(" Order By matCode");
		}
		String hql = sb.toString();
		String totalHql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
		return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}
	
	/**
	 * <li>说明：查询班组用料消耗的物料清单信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchEntity 查询条件封装实体
	 * @param orgId 班组id
	 * @return 物料清单分页对象
	 */
	public Page<MatTypeList> findPageListForExpend(SearchEntity<MatTypeList> searchEntity, String orgId) {
		// 根据班组id获取该班组所维护的库房idx主键
		String sql = "SELECT T.WH_IDX FROM WLGL_WH_ORG T WHERE T.RECORD_STATUS = 0 AND T.ORG_ID ='" + orgId + "'";
		List list = this.daoUtils.executeSqlQuery(sql);
		String whIdx = "###";
		if (null != list && list.size() > 0) {
			whIdx = (String) list.get(0);
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("Select new MatTypeList(a.matCode, a.matDesc, a.unit, a.price, b.qty) From MatTypeList a, MatStock b Where a.matCode = b.matCode And b.qty > 0");
		
		MatTypeList entity = searchEntity.getEntity();
		// 查询条件 - 库房idx主键
		sb.append(" And b.whIdx = '").append(whIdx).append("'");
		// 查询条件 - 物料编码
		if (!StringUtil.isNullOrBlank(entity.getMatCode())) {
			sb.append(" And a.matCode like '%").append(entity.getMatCode()).append("%'");
		}
		// 查询条件 - 物料描述
		if (!StringUtil.isNullOrBlank(entity.getMatDesc())) {
			sb.append(" And a.matDesc like '%").append(entity.getMatDesc()).append("%'");
		}
		// 排序字段
		Order[] orders = searchEntity.getOrders();
		if (null != orders && orders.length > 0) {
			sb.append(" Order By ");
			sb.append(orders[0].toString());
			for (int i = 1; i < orders.length; i++) {
				sb.append(", ");
				sb.append(orders[i].toString());
			}
		}
		String hql = sb.toString();
		String totalHql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
		return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}
	
	/**
	 * <li>说明：根据物料编码和班组id查询物料清单信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param matCode 物料编码
	 * @param orgId 班组id
	 * @return 物料清单实体集合
	 */
	public List getModelByMatCodeForExpend(String matCode, String orgId) {
		//根据班组id获取该班组所维护的库房idx主键
		String sql = "SELECT T.WH_IDX FROM WLGL_WH_ORG T WHERE T.ORG_ID ='" + orgId + "'";
		List list = this.daoUtils.executeSqlQuery(sql);
		String whIdx = "###";
		if (null != list && list.size() > 0) {
			whIdx = (String) list.get(0);
		}

		String hql = "Select new MatTypeList(a.matCode, a.matDesc, a.unit, a.price, b.qty) From MatTypeList a, MatStock b Where a.recordStatus = 0 And b.recordStatus = 0 And a.matCode = b.matCode And b.qty > 0 And b.matCode = ? And b.whIdx = ?";
		Object[] args = new Object[] { matCode, whIdx };
		return this.daoUtils.find(hql, args);
	}
    /**
     * 
     * <li>说明：查询物料类型【物料类型在数据字典中维护】
     * <li>创建人：程梅
     * <li>创建日期：2016-5-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 物料类型list
     */
    public static List<EosDictEntry> getMatType() {
        EosDictEntrySelectManager eosDictEntrySelectManager = (EosDictEntrySelectManager) Application.getSpringApplicationContext().getBean("eosDictEntrySelectManager");
        return eosDictEntrySelectManager.findByDicTypeID("WLGL_MAT_CLASS");
    }
    public MatStockQueryManager getMatStockQueryManager() {
        return matStockQueryManager;
    }
    
    public void setMatStockQueryManager(MatStockQueryManager matStockQueryManager) {
        this.matStockQueryManager = matStockQueryManager;
    }
    public WhMatQuotaManager getWhMatQuotaManager() {
        return whMatQuotaManager;
    }
    
    public void setWhMatQuotaManager(WhMatQuotaManager whMatQuotaManager) {
        this.whMatQuotaManager = whMatQuotaManager;
    }
    
    /**
     * <li>说明：重写保存方法，增加对物料描述首拼字段的存储
     * <li>创建人：何涛
     * <li>创建日期：2016-5-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t 物料清单实体对象
     */
    @Override
    public void saveOrUpdate(MatTypeList t) throws BusinessException, NoSuchFieldException {
        t.setMatDescEn(ChineseCharToEn.getInstance().getAllFirstLetter(t.getMatDesc(), Sensitive.lower));
        super.saveOrUpdate(t);
    }
    
    /**
    * <li>说明：重写保存方法，增加对物料描述首拼字段的存储
    * <li>创建人：何涛
    * <li>创建日期：2016-5-19
    * <li>修改人： 
    * <li>修改日期：
    * <li>修改内容：
    * @param entityList 物料清单实体集合
    */
    @Override
    public void saveOrUpdate(List<MatTypeList> entityList) throws BusinessException, NoSuchFieldException {
        for (MatTypeList mtl : entityList) {
            mtl.setMatDescEn(ChineseCharToEn.getInstance().getAllFirstLetter(mtl.getMatDesc(), Sensitive.lower));
        }
        super.saveOrUpdate(entityList);
    }
    
    /**
     * 物料选择下拉框
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit) throws Exception {
        StringBuffer hql = new StringBuffer(" select t from MatTypeList t where 1=1 ") ;
        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*)");
        totalHql.append(hql.toString().substring(beginPos));
        Page page = findPageList(totalHql.toString(), hql.toString(), start, limit);
        return page.extjsStore();
    }
    
}