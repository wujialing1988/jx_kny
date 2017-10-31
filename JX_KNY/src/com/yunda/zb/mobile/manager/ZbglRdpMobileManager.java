package com.yunda.zb.mobile.manager;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.StringUtil;
import com.yunda.zb.mobile.entity.ZbglRdpMobileDTO;
import com.yunda.zb.mobile.entity.ZbglRdpWiDiMobileDTO;
import com.yunda.zb.mobile.entity.ZbglRdpWiMobileDTO;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdpNode;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpNodeManager;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpQueryManager;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWi;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWidi;
import com.yunda.zb.rdp.zbtaskbill.manager.ZbglRdpWiManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 该类暂时只用于ZbglRdpMobileManager注入，方便访问DAO
 * <li>创建人：林欢
 * <li>创建日期：2016-7-8
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="zbglRdpMobileManager")
public class ZbglRdpMobileManager extends JXBaseManager<ZbglRdp, ZbglRdp>{
    
    @Resource
    private ZbglRdpNodeManager zbglRdpNodeManager;
    
    @Resource
    private ZbglRdpQueryManager zbglRdpQueryManager;
    
    @Resource
    private ZbglRdpWiManager zbglRdpWiManager;

	/**
     * <li>说明：分页查询当前登录人机车整备单列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-7-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param start 分页开始条数
     * @param limit 分页限制条数
     * @param operatIDX 登录人ID
     * @param queryStr 查询字段
     * @param workStationIDX 工位id
     * @return Page<ZbglRdpMobileDTO> 整备单
     */
    public Page<ZbglRdpMobileDTO> queryZbglRdpList(int start, int limit,String operatIDX,String queryStr,String workStationIDX){
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from ( ");
        sb.append(" select rdp.idx as ZBGL_RDP_IDX, rdp.train_type_shortname as TRAIN_TYPE_SHORTNAME , rdp.train_no as TRAIN_NO , to_char(acc.in_time,'yyyy-MM-dd HH24:mi') as IN_TIME, rdp.train_type_idx as TRAIN_TYPE_IDX, ");
        sb.append(" sum(case when wi.wi_status = 'ONGOING' then 1 else 0 end ) as WAIT_ONGOING_COUNT, ");
        sb.append(" sum(case when wi.wi_status = 'COMPLETE' then 1 else 0 end ) as COMPLET_COUNT, ");
        sb.append(" sum(case when wi.wi_status = 'TODO' then 1 else 0 end ) as WAIT_TODO_COUNT ");
        sb.append(" FROM ZB_ZBGL_RDP rdp ");
        sb.append(" left join ZB_ZBGL_RDP_WI wi on rdp.idx = wi.rdp_idx ");
        sb.append(" left join TWT_Train_Access_Account acc on acc.idx = rdp.train_access_account_idx ");
        sb.append(" left join ZB_ZBGL_Job_Process_Node n on rdp.idx = n.rdp_idx and wi.node_idx = n.idx ");
        sb.append(" left join JXGC_WORK_STATION ws on ws.idx = n.work_station_idx ");
        sb.append(" left join ZBGL_WORK_STATION_BINDING bing on bing.work_station_idx = ws.idx ");
        sb.append(" where rdp.record_status = 0 and wi.Record_Status = 0 ");
        sb.append(" and rdp.rdp_status <> 'COMPLETE' and n.rdp_idx is not null ");
        sb.append(" and bing.empid = '"+operatIDX+"' ");
        if(!StringUtil.isNullOrBlank(queryStr)){
            sb.append(" and (rdp.train_type_shortname like '%"+queryStr+"%' or rdp.train_no like '%"+queryStr+"%')");
        }
        if(!StringUtil.isNullOrBlank(workStationIDX)){
            sb.append(" and ws.idx = '"+workStationIDX+"'");
        }
        sb.append(" group by rdp.idx , rdp.train_type_shortname , rdp.train_no , acc.in_time , rdp.train_type_idx ");
        sb.append(" order by acc.in_time desc ");
        sb.append(" ) where WAIT_TODO_COUNT <> 0 or WAIT_ONGOING_COUNT <> 0 ");
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT From (" + sb.toString() + ")";
        return this.queryPageList(totalSql, sb.toString(), start, limit, false, ZbglRdpMobileDTO.class); 
    }

    /**
     * <li>说明：查询整备任务和数据项
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-7-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param start 分页开始条数
     * @param limit 分页限制条数
     * @param operatIDX 当前登录人
     * @param zbRdpId 整备单id
     * @return Page<ZbglRdpWiMobileDTO> 整备单和工位查询整备活项
     */
    @SuppressWarnings("unchecked")
    public Page<ZbglRdpWiMobileDTO> queryZbglRdpWiListByRDPIDandWSid(int start, int limit, String operatIDX, String zbRdpId ,String workStationIDX) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select wi.idx as ZBGL_RDP_WI_IDX ,wi.isCheckPicture, wi.wi_name as WI_NAME ,n.node_name as NODE_NAME, wi.wi_desc as WI_DESC,wi.worker as WORKER , wi.wi_status as WI_STATUS ");
        sb.append(" FROM ZB_ZBGL_RDP_WI wi ");
        sb.append(" left join ZB_ZBGL_Job_Process_Node n on  wi.node_idx = n.idx ");
        sb.append(" left join JXGC_WORK_STATION ws on ws.idx = n.work_station_idx ");
        sb.append(" left join ZBGL_WORK_STATION_BINDING bing on bing.work_station_idx = ws.idx ");
        sb.append(" where wi.Record_Status = 0 and wi.wi_status <> 'COMPLETE' ");
        sb.append(" and bing.empid = '"+operatIDX+"' ");
        sb.append(" and wi.rdp_idx = '"+zbRdpId+"' ");
        sb.append(" and n.rdp_idx = '"+zbRdpId+"' ");
        if(!StringUtil.isNullOrBlank(workStationIDX)){
            sb.append(" and ws.idx = '"+workStationIDX+"'");
        }
        sb.append(" order by wi.seq_no ");
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
        Page<ZbglRdpWiMobileDTO> result = this.queryPageList(totalSql, sb.toString(), start, limit, false, ZbglRdpWiMobileDTO.class);
        for (ZbglRdpWiMobileDTO dto : result.getList()) {
            // 组装数据项
            StringBuffer hql = new StringBuffer();
            hql.append(" From ZbglRdpWidi where rdpWiIDX = '"+dto.getZbglRdpWiIDX()+"'");
            List<ZbglRdpWidi> list = (List<ZbglRdpWidi>)this.find(hql.toString());
            for (ZbglRdpWidi widi : list) {
                ZbglRdpWiDiMobileDTO di = new ZbglRdpWiDiMobileDTO();
                buildEntityToDIDto(widi,di);
                dto.getZbglRdpWiDiList().add(di);
            }
        }
        return result;
    }

    
    /**
     * <li>说明：数据项实体与DTO转换
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-7-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param widi
     * @param di
     */
    private void buildEntityToDIDto(ZbglRdpWidi widi, ZbglRdpWiDiMobileDTO di) {
        di.setZbglRdpWiDiIDX(widi.getIdx());
        di.setDiName(widi.getDiName());
        di.setDiCode(widi.getDiCode());
        di.setDiResult(widi.getDiResult());
        di.setDiStandard(widi.getDiStandard());
        di.setIsBlank(widi.getIsBlank());
        di.setSeqNo(widi.getSeqNo() == null ? "0" : widi.getSeqNo().toString());
    }

    /**
     * <li>说明：机车整备任务单完工
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-7-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 整备任务单
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    @SuppressWarnings("unchecked")
    public void completZbglRdpWi(ZbglRdpWiMobileDTO entity) throws BusinessException, NoSuchFieldException {
        // 当前操作员信息
        // OmEmployee employee = SystemContext.getOmEmployee(); 
        if(StringUtil.isNullOrBlank(entity.getZbglRdpWiIDX())){
            return ;
        }
        ZbglRdpWi wi = (ZbglRdpWi)this.getDaoUtils().get(entity.getZbglRdpWiIDX(), ZbglRdpWi.class);
        if(wi != null){
        	wi.setWiDesc(entity.getWiDesc());
        	wi.setWorker(entity.getWorker());
            wi.setWiStatus(ZbglRdpWi.STATUS_HANDLED);
            wi.setHandleTime(new Date());
            wi.setUpdateTime(new Date());
            this.getDaoUtils().update(wi);
        }
        if(entity.getZbglRdpWiDiList() != null && entity.getZbglRdpWiDiList().size() > 0){
            for (ZbglRdpWiDiMobileDTO dto : entity.getZbglRdpWiDiList()) {
                if(StringUtil.isNullOrBlank(dto.getZbglRdpWiDiIDX())){
                    continue ;
                }
                ZbglRdpWidi di = (ZbglRdpWidi)this.getDaoUtils().get(dto.getZbglRdpWiDiIDX(), ZbglRdpWidi.class);
                di.setDiResult(dto.getDiResult());
                this.getDaoUtils().update(di);
            }
        }
        // 销活调用end改变节点状态
        ZbglRdp rdp = zbglRdpQueryManager.getModelById(wi.getRdpIDX());
        if (rdp != null) {
            if (!StringUtil.isNullOrBlank(wi.getNodeIDX())) {
                ZbglRdpNode node = zbglRdpNodeManager.getModelById(wi.getNodeIDX());
                if (node != null) {
                     //判断，该节点下的所有工单是否完成，完成才能调用修改节点方法
                	//通过节点idx查询节点idx下的所有工单
                	List<ZbglRdpWi> zbglRdpWiList = (List<ZbglRdpWi>)zbglRdpWiManager.find(" from ZbglRdpWi a where a.recordStatus = 0 and a.wiStatus in ('" + ZbglRdpWi.STATUS_HANDLING + "','" + ZbglRdpWi.STATUS_TODO + "') and a.rdpIDX = '" + wi.getRdpIDX() + "' and a.nodeIDX = '" + node.getIdx() + "'");
                	if (zbglRdpWiList != null && zbglRdpWiList.size() > 0) {
						
					}else {
						zbglRdpNodeManager.updateNodeForEnd(node.getNodeName(), new Date(), rdp.getIdx(), ZbglRdpNode.STATUS_COMPLETE);   
					}
                }
            }
        }
        this.getDaoUtils().flush();
        // 检查任务单是否全部处理完成，如果全部处理完成，则修改整备单状态为【完成】 ZbglRdp
        List<ZbglRdpWi> wis = (List<ZbglRdpWi>)this.find(" From ZbglRdpWi wi where rdpIDX != '"+wi.getRdpIDX()+"' and wiStatus = '"+ZbglRdpWi.STATUS_HANDLED + "' and recordStatus = 0");
        if(wis == null){
            if(rdp != null){
                rdp.setRdpStatus(ZbglRdp.STATUS_HANDLED);
                this.getDaoUtils().update(rdp);
            }
        }
    }

	/**
     * <li>说明：分页查询当前登录人机车整备单列表（全部）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-7-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param start 分页开始条数
     * @param limit 分页限制条数
     * @param operatIDX 登录人ID
     * @param queryStr 查询字段
     * @param workStationIDX 工位id
     * @return Page<ZbglRdpMobileDTO> 整备单
     */
	public Page<ZbglRdpMobileDTO> queryZbglRdpAllList(Integer start, Integer limit, String queryStr, String workStationIDX) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from ( ");
        sb.append(" select rdp.idx as ZBGL_RDP_IDX, rdp.train_type_shortname as TRAIN_TYPE_SHORTNAME , rdp.train_no as TRAIN_NO , to_char(acc.in_time,'yyyy-MM-dd HH24:mi') as IN_TIME, rdp.train_type_idx as TRAIN_TYPE_IDX, ");
        sb.append(" sum(case when wi.wi_status = 'ONGOING' then 1 else 0 end ) as WAIT_ONGOING_COUNT, ");
        sb.append(" sum(case when wi.wi_status = 'COMPLETE' then 1 else 0 end ) as COMPLET_COUNT, ");
        sb.append(" sum(case when wi.wi_status = 'TODO' then 1 else 0 end ) as WAIT_TODO_COUNT ");
        sb.append(" FROM ZB_ZBGL_RDP rdp ");
        sb.append(" left join ZB_ZBGL_RDP_WI wi on rdp.idx = wi.rdp_idx ");
        sb.append(" left join TWT_Train_Access_Account acc on acc.idx = rdp.train_access_account_idx ");
        sb.append(" where rdp.record_status = 0 and wi.Record_Status = 0 ");
        sb.append(" and rdp.rdp_status <> 'COMPLETE' ");
        if(!StringUtil.isNullOrBlank(queryStr)){
            sb.append(" and (rdp.train_type_shortname like '%"+queryStr+"%' or rdp.train_no like '%"+queryStr+"%')");
        }
        sb.append(" group by rdp.idx , rdp.train_type_shortname , rdp.train_no , acc.in_time , rdp.train_type_idx ");
        sb.append(" order by acc.in_time desc ");
        sb.append(" ) where WAIT_TODO_COUNT <> 0 or WAIT_ONGOING_COUNT <> 0 ");
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT From (" + sb.toString() + ")";
        return this.queryPageList(totalSql, sb.toString(), start, limit, false, ZbglRdpMobileDTO.class); 
	}

	   /**
     * <li>说明：查询整备任务和数据项（全部）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-7-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param start 分页开始条数
     * @param limit 分页限制条数
     * @param operatIDX 当前登录人
     * @param zbRdpId 整备单id
     * @return Page<ZbglRdpWiMobileDTO> 整备单和工位查询整备活项
     */
    @SuppressWarnings("unchecked")
    public Page<ZbglRdpWiMobileDTO> queryZbglRdpWiListByRDPIDandWSidAll(int start, int limit, String zbRdpId ,String workStationIDX) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select wi.idx as ZBGL_RDP_WI_IDX ,wi.isCheckPicture, wi.wi_name as WI_NAME ,wi.wi_desc as WI_DESC,wi.worker as WORKER , '' as NODE_NAME , wi.wi_status as WI_STATUS ");
        sb.append(" FROM ZB_ZBGL_RDP_WI wi ");
        sb.append(" where wi.Record_Status = 0 and wi.wi_status <> 'COMPLETE' ");
        sb.append(" and wi.rdp_idx = '"+zbRdpId+"' ");
        sb.append(" order by wi.seq_no ");
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
        Page<ZbglRdpWiMobileDTO> result = this.queryPageList(totalSql, sb.toString(), start, limit, false, ZbglRdpWiMobileDTO.class);
        for (ZbglRdpWiMobileDTO dto : result.getList()) {
            // 组装数据项
            StringBuffer hql = new StringBuffer();
            hql.append(" From ZbglRdpWidi where rdpWiIDX = '"+dto.getZbglRdpWiIDX()+"'");
            List<ZbglRdpWidi> list = (List<ZbglRdpWidi>)this.find(hql.toString());
            for (ZbglRdpWidi widi : list) {
                ZbglRdpWiDiMobileDTO di = new ZbglRdpWiDiMobileDTO();
                buildEntityToDIDto(widi,di);
                dto.getZbglRdpWiDiList().add(di);
            }
        }
        return result;
    }
}