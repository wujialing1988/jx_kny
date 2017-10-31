/**
 * 
 */
package com.yunda.jx.jxgc.producttaskmanage.manager;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCardQCCheck;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCardQCCheck.WorkCardQCCheckSearcher;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WorkCardQCCheck业务类，质量检验查询
 * <li>创建人： 何涛
 * <li>创建日期： 2014-11-26 下午02:21:17
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="workCardQCCheckManager")
public class WorkCardQCCheckManager extends JXBaseManager<WorkCardQCCheck, WorkCardQCCheck>{

	/**
	 * <li>说明：分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-26
	 * <li>修改人：何涛
	 * <li>修改日期：2016-04-14
	 * <li>修改内容：代码规范，使用queryPageList进行自定义封装实体的分页查询
	 * @param searchEntity 查询条件封装实体
	 * @param status 检验状态
	 * @param checkWay  检验方式（抽检|必检）
	 * @return 质量检验查询对象的封装实体集合
	 * @throws BusinessException
	 */
	public Page<WorkCardQCCheck> pageList(SearchEntity<WorkCardQCCheckSearcher> searchEntity, String status, String checkWay) throws BusinessException {
		String sql = SqlMapUtil.getSql("jxgc-QCCheck:findPageList");
		
		StringBuilder sb = new StringBuilder(sql);
		sb.append(" WHERE 0 = 0");
		WorkCardQCCheckSearcher entity = searchEntity.getEntity();
		// 查询条件 - 作业工单名称
		if (!StringUtil.isNullOrBlank(entity.getWorkCardName())) {
			sb.append(" AND WORK_CARD_NAME LIKE '%").append(entity.getWorkCardName()).append("%'");
		}
		// 查询条件 - 质量检查人员
		if (!StringUtil.isNullOrBlank(entity.getCheckPersonName())) {
			sb.append(" AND CHECK_PERSON_NAME LIKE '%").append(entity.getCheckPersonName()).append("%'");
		}
		// 查询条件 - 生产任务单
		if (!StringUtil.isNullOrBlank(entity.getRdpIDX())) {
			sb.append(" AND RDP_IDX = '").append(entity.getRdpIDX()).append("'");
		}
		// 查询条件 - 检验状态
		if (!StringUtil.isNullOrBlank(status)) {
			sb.append(" AND STATUS IN (").append(status).append(")");
		}
		// 查询条件 - 检验方式（抽检|必检）
		if (!StringUtil.isNullOrBlank(checkWay)) {
			sb.append(" AND CHECK_WAY IN (").append(checkWay).append(")");
		}
		sql = sb.toString();
		String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sql.substring(sql.indexOf("FROM"));
        return super.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, WorkCardQCCheck.class);
	}

}
