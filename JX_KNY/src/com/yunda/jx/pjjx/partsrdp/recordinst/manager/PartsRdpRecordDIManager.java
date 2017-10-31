package com.yunda.jx.pjjx.partsrdp.recordinst.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.pjjx.base.recorddefine.entity.RecordDI;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordDI;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordDIDTO;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpRecordDI业务类,配件检修检测数据项
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsRdpRecordDIManager")
public class PartsRdpRecordDIManager extends JXBaseManager<PartsRdpRecordDI, PartsRdpRecordDI>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：根据“检修检测项实例主键”查询检测项
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容：
	 * 
	 * @param rdpRecordRIIDX 检修检测项实例主键
	 * @return List<PartsRdpRecordDI> 配件检修检测数据项实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<PartsRdpRecordDI> getModelByRdpRecordRIIDX(String rdpRecordRIIDX) {
		String hql = "From PartsRdpRecordDI Where rdpRecordRIIDX = ? And recordStatus = 0 Order By seqNo";
		return this.daoUtils.find(hql, new Object[]{rdpRecordRIIDX});
	}
	
	/**
	 * <li>说明：撤销检修检测项的处理历史记录
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 检修检测项实体
	 * @param entity
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public void giveUpJob(PartsRdpRecordDI entity) throws BusinessException, NoSuchFieldException {
		entity.setDataItemResult(null);
		this.saveOrUpdate(entity);
	}
	
    /**
     * <li>说明：获取记录单下必填的数据项数量
     * <li>创建人：程锐
     * <li>创建日期：2016-2-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpRecordRIIDX 记录单IDX
     * @return 记录单下必填的数据项数量
     */
	@SuppressWarnings("unchecked")
	public int getIsNotBlankCountByRdpRecordRIIDX(String rdpRecordRIIDX) {
		String hql = "From PartsRdpRecordDI Where rdpRecordRIIDX = ? and isBlank = ? And recordStatus = 0";
		return this.daoUtils.getCount(hql, new Object[]{rdpRecordRIIDX, RecordDI.CONST_INT_IS_BLANK_YES});
	}

    /**
     * <li>说明：动态通过http获取可视化检测结果数据，并且同步更新到对应的检测项中
     * <li>创建人：林欢
     * <li>创建日期：2016-06-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param url http访问url
     * @param list http访问查询参数
     * @param paramMap 其他查询参数
     * @return Map<String, Object> 返回结果map {success : true} {success : false,errMsg:"操作失败"}
     * @throws HttpException 
     * @throws IOException
     */
    public List<String> findCheckIDByRdpRecordCardIDX(String rdpRecordCardRdpIDX) {
        StringBuffer sb = new StringBuffer();
        
        sb.append(" select c.checkID ");
        sb.append(" from  ");
        sb.append(" PartsRdpRecordCard a, ");
        sb.append(" PartsRdpRecordRI   b, ");
        sb.append(" PartsRdpRecordDI   c ");
        sb.append(" where a.recordStatus = 0 ");
        sb.append(" and b.recordStatus = 0 ");
        sb.append(" and c.recordStatus = 0 ");
        sb.append(" and b.rdpRecordCardIDX = a.idx ");
        sb.append(" and c.rdpRecordRIIDX = b.idx ");
        sb.append(" and a.rdpIDX = ? ");
        sb.append(" group by c.checkID ");
        
        List<String> list = daoUtils.find(sb.toString(), new Object[] {rdpRecordCardRdpIDX});
        return list;
    }

    /**
     * <li>说明：动态通过http获取可视化检测结果数据，并且同步更新到对应的检测项中
     * <li>创建人：林欢
     * <li>创建日期：2016-06-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param url http访问url
     * @param list http访问查询参数
     * @param paramMap 其他查询参数
     * @return Map<String, Object> 返回结果map {success : true} {success : false,errMsg:"操作失败"}
     * @throws HttpException 
     * @throws IOException
     */
    public PartsRdpRecordDI getPartsRdpRecordDIDataItemResult(Map<String, String> paramMap) {
        
        StringBuffer sb = new StringBuffer();
        
        sb.append(" select c.* ");
        sb.append(" from ");
        sb.append(" pjjx_parts_rdp_record_card a, ");
        sb.append(" pjjx_parts_rdp_record_ri   b, ");
        sb.append(" pjjx_parts_rdp_record_di   c ");
        sb.append(" where ");
        sb.append(" a.record_status = 0 ");
        sb.append(" and b.rdp_record_card_idx = a.idx ");
        sb.append(" and c.rdp_record_ri_idx = b.idx ");
        sb.append(" and a.rdp_idx = '").append(paramMap.get("rdpRecordCardRdpIDX")).append("'");
        sb.append(" and c.check_id = '").append(paramMap.get("checkID")).append("'");
        
        List<PartsRdpRecordDI> list = this.daoUtils.executeSqlQueryEntity(sb.toString(), PartsRdpRecordDI.class);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    
    /**
     * <li>说明：根据“检修检测项实例主键”查询检测项
     * <li>创建人：何涛
     * <li>创建日期：2015-01-12
     * <li>修改人: 
     * <li>修改日期： 
     * <li>修改内容：
     * 
     * @param rdpRecordRIIDX 检修检测项实例主键
     * @return List<PartsRdpRecordDI> 配件检修检测数据项实体集合
     */
    @SuppressWarnings("unchecked")
    public List<PartsRdpRecordDIDTO> getSQLModelByRdpRecordRIIDX(String rdpRecordRIIDX) {
        
        StringBuffer sb = new StringBuffer();
        sb.append(" select a.*, ");
        sb.append(" b.parts_id, ");
        sb.append(" b.check_time, ");
        sb.append(" b.check_end_time, ");
        sb.append(" b.check_value ");
        sb.append(" from pjjx_parts_rdp_record_di a ");
        sb.append(" left join pjjx_check_item_data b on a.idx = b.parts_rdp_recorddi_idx ");
        sb.append(" where a.rdp_record_ri_idx = '").append(rdpRecordRIIDX).append("'");
        
        return this.daoUtils.executeSqlQueryEntity(sb.toString(), PartsRdpRecordDIDTO.class);
    }
}