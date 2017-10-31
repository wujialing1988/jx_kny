package com.yunda.jx.scdd.enforceplan.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jcgy.entity.JgyjcBureau;
import com.yunda.jcgy.entity.JgyjcDeport;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 用于DominateSection_comboTree控件
 * <li>创建人：谭诚
 * <li>创建日期：2013-5-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 * 
 */
@Service(value = "dominateSectionManager")
public class DominateSectionManager extends JXBaseManager<JgyjcDeport, JgyjcDeport> {
	/**
	 * 
	 * <li>说明：段或局的选择树
	 * <li>创建人：谭诚
	 * <li>创建日期：2014-3-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param queryHql
	 * @param parentIDX
	 * @return
	 * @throws BusinessException
	 */
	 @SuppressWarnings("unchecked")
	 public List<HashMap> bureauTree(String queryHql,String parentIDX) throws BusinessException {
		 String hql = "";
		 List<HashMap> children = null;
		 if("".equals(parentIDX)||"0".equals(parentIDX)){
			 hql = "FROM JgyjcBureau WHERE bId != '00' order by sort ";
			 List<JgyjcBureau> orgList = (List<JgyjcBureau>) getDaoUtils().find(hql);
			 children = new ArrayList<HashMap>();
	         for (JgyjcBureau org : orgList) {
	             HashMap nodeMap = new HashMap();
	             nodeMap.put("id", org.getBId());
	             nodeMap.put("text", org.getBName());
	             nodeMap.put("orgname", org.getShortName());
	             nodeMap.put("isleaf", false);
	             nodeMap.put("leaf", false);
	             nodeMap.put("code", org.getCode());
	             nodeMap.put("disabled", true);
	             children.add(nodeMap);
	         }
		 }else{
			 hql = "FROM JgyjcDeport WHERE attribute = '1' and ownBureau = '"+parentIDX+"'";
			 List<JgyjcDeport> orgList = (List<JgyjcDeport>) getDaoUtils().find(hql);
			 children = new ArrayList<HashMap>();
	         for (JgyjcDeport org : orgList) {
	             HashMap nodeMap = new HashMap();
	             nodeMap.put("id", org.getDId());
	             nodeMap.put("text", org.getDName());
	             nodeMap.put("orgname", org.getShortName());
	             nodeMap.put("isleaf", true);
	             nodeMap.put("leaf", true);
	             nodeMap.put("code", "");
	             children.add(nodeMap);
	         }
		 }
//	     // 优先按查询Hql查询结果
//         if (!StringUtil.isNullOrBlank(queryHql)) {
//             hql = queryHql;
//         }
         
         return children;
	 }
	
}
