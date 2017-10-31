package com.yunda.jx.jxgc.producttaskmanage.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkTaskResultView;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResult;
import com.yunda.jx.jxgc.repairrequirement.entity.QualityControl;
import com.yunda.jx.jxgc.repairrequirement.manager.WorkSeqManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkTaskResultViewManager业务类,作业任务
 * <li>创建人：程锐
 * <li>创建日期：2012-12-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="workTaskResultViewManager")
public class WorkTaskResultViewManager extends JXBaseManager<WorkTaskResultView, WorkTaskResultView>{
    
	/**
	 * <li>说明：查询作业项对应的三检一验人员信息
	 * <li>创建人：王治龙
	 * <li>创建日期：2013-5-8
	 * <li>修改人： 程锐
	 * <li>修改日期：2014-12-25
	 * <li>修改内容：查询三检一验人员信息时取QCResult的信息
	 * @param searchEntity 查询包装类实体
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public List<Map<String,String>> pageListForTaskQR(SearchEntity<WorkTaskResultView> searchEntity){
		List<Map<String,String>>  listMap = new ArrayList<Map<String,String>>();
		List<WorkTaskResultView> taskList = this.findPageList(searchEntity).getList();
		for (WorkTaskResultView task : taskList) {
			Map<String,String> tsakMap = new HashMap<String, String>(); //用Map动态的模拟一条记录
			tsakMap.put("idx", task.getIdx()); //作业项主键
			tsakMap.put("workCardIDX", task.getWorkCardIDX()); //作业卡主键
			tsakMap.put("workTaskCode", task.getWorkTaskCode()); //作业任务代码
			tsakMap.put("workTaskName", task.getWorkTaskName()); //作业任务名称
			tsakMap.put("repairStandard", task.getRepairStandard());//作业任务标准
			tsakMap.put("resultName", task.getResultName());       //作业结果
			tsakMap.put("remarks", task.getRemarks());       //作业结果
			List<QualityControl> qcList = WorkSeqManager.getWorkSeqQualityControl("null"); //查询有多少三检一验项目
			for(QualityControl qc : qcList){ //循环三检一验项目 动态组装出一列数据
				List<QCResult> personList = this.getCheckPerson(task.getIdx(),qc.getCheckItemCode());
				String personName = "——" ; 
				if(personList != null){ //不等于空说明 当前的作业任务有对应的检验项目，否则没有
					personName = personList.get(0).getQcEmpName(); //获取对应三检一验人员名称
				}
				tsakMap.put(qc.getCheckItemCode(), personName);
			}
			listMap.add(tsakMap);
		}
		return listMap ;
	}
	/**
	 * <li>说明：通过作业项主键获取三检一验人员名称
	 * <li>创建人：王治龙
	 * <li>创建日期：2013-5-8
	 * <li>修改人： 程锐
	 * <li>修改日期：2014-12-25
	 * <li>修改内容：查询三检一验人员信息时取QCResult的信息
	 * @param workTaskIdx 作业项主键
	 * @param checkItemCode 检验项编码
	 * @return List
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public List<QCResult> getCheckPerson(String workTaskIdx ,String checkItemCode) throws BusinessException{
		String sqlStr = SqlMapUtil.getSql("jxgc-workseq:findWorkTaskPerson");
//		查询作业项对应的三检一验人员
		sqlStr = sqlStr + " and q.relation_idx='"+workTaskIdx+"' and q.check_item_code ='"+checkItemCode+"'"; 
		String[] cloumns = {"idx","relationIDX","checkItemCode","checkItemName","qcEmpName"};
		List list = this.daoUtils.findPageSQL(new QCResult(), cloumns, sqlStr, 0, 100);
		if(list.size() > 0){
			return list;
		}
		return null;
	}

}