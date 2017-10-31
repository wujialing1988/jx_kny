package com.yunda.jx.pjjx.partsrdp.wpinst.manager;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.jx.jxgc.dispatchmanage.entity.WorkStation;
import com.yunda.jx.pjjx.partsrdp.wpinst.entity.PartsRdpNodeStation;

@Service
public class PartsRdpNodeStationManager extends
		JXBaseManager<PartsRdpNodeStation, PartsRdpNodeStation> {
	
	/**
	 * <li>方法说明： 根据需求单节点查询
	 * <li>方法名：findByWpNode
	 * @param nodeIdx 需求单节点
	 * @param rdpIdx rdpIdx
	 * @param start 
	 * @param limit 
	 * @return
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-21
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public Page<WorkStation> findByWpNode(String nodeIdx, String rdpIdx, int start, int limit){
		
		StringBuilder suffix = new StringBuilder();
		suffix.append(" from pjjx_parts_rdp_node_station t,pjjx_parts_rdp_node n where n.idx = t.rdp_node_idx");
		suffix.append(" and t.record_status=0 and n.record_status=0");
		suffix.append(" and n.wp_node_idx = '").append(nodeIdx).append("' and n.rdp_idx = '").append(rdpIdx).append("'");
		
		String total = "select count(1) " + suffix;
		StringBuilder prefix = new StringBuilder("select t.idx, t.work_station_code, t.work_station_name, t.repair_line_name");
		String sql = prefix.append(suffix).toString() ;
		
		return findPageList(total, sql, start, limit, null, null);
	}

	/**
	 * <li>方法说明：批量保存
	 * <li>方法名：saves
	 * @param stations
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-22
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public void saves(PartsRdpNodeStation[] stations) throws BusinessException, NoSuchFieldException{
		for(int i = 0; i < stations.length; i++){
			saveOrUpdate(stations[i]);
		}
	}
}
