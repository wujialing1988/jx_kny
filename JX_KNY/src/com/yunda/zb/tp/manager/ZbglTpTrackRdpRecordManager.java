package com.yunda.zb.tp.manager;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.zb.tp.entity.ZbglTpTrackRdp;
import com.yunda.zb.tp.entity.ZbglTpTrackRdpRecord;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglTpTrackRdpRecordManager业务类,提票跟踪记录单
 * <li>创建人：程锐
 * <li>创建日期：2015-03-10
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "zbglTpTrackRdpRecordManager")
public class ZbglTpTrackRdpRecordManager extends JXBaseManager<ZbglTpTrackRdpRecord, ZbglTpTrackRdpRecord> {
	
	/** ZbglTpTrackRdpManager业务类,提票跟踪单 */
	@Resource
	private ZbglTpTrackRdpManager zbglTpTrackRdpManager;

	/**
	 * <li>说明:根据提票跟踪单idx查询提票跟踪记录单中最大的跟踪次数
	 * <li>创建人：林欢
	 * <li>创建日期：2016-08-08
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param zbglTrackRdpIDX 提票跟踪单idx
	 * @return Integer 提票跟踪记录单中最大的跟踪次数
	 */
	public Integer findMaxCountByZbglTrackRdpIDX(String zbglTrackRdpIDX) {
		BigDecimal maxCount = new BigDecimal(0);
		StringBuffer sb = new StringBuffer();
		sb.append(" select nvl(max(a.track_count), 0) from zb_zbgl_jt6_track_rdp_record a where a.track_rdp_idx = '").append(zbglTrackRdpIDX).append("'");
		maxCount = (BigDecimal) this.getDaoUtils().executeSqlQuery(sb.toString()).get(0);
		return maxCount.intValue();
	}
	
	/**
	 * <li>说明:根据提票跟踪单idx查询提票跟踪记录单条数
	 * <li>创建人：林欢
	 * <li>创建日期：2016-08-08
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param zbglTrackRdpIDX 提票跟踪单idx
	 * @return Integer 提票跟踪记录单中最大的跟踪次数
	 */
	public Integer findCountsByZbglTrackRdpIDX(String zbglTrackRdpIDX) {
		BigDecimal counts = new BigDecimal(0);
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(1) from zb_zbgl_jt6_track_rdp_record a where a.track_rdp_idx = '").append(zbglTrackRdpIDX).append("'");
		counts = (BigDecimal) this.getDaoUtils().executeSqlQuery(sb.toString()).get(0);
		return counts.intValue();
	}
	
	/**
	 * <li>说明:根据提票跟踪单idx查询提票跟踪记录单最新的一条
	 * <li>创建人：林欢
	 * <li>创建日期：2016-08-08
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param zbglTrackRdpIDX 提票跟踪单idx
	 * @return ZbglTrackRdpRecord 提票跟踪记录单中对象
	 */
	public ZbglTpTrackRdpRecord findZbglTrackRdpRecordByZbglTrackRdpIDX(String zbglTrackRdpIDX,Integer maxCount) {
		StringBuffer sb = new StringBuffer();
		
		sb.append(" from ZbglTpTrackRdpRecord a where a.trackRdpIDX = '").append(zbglTrackRdpIDX).append("'");
		sb.append(" and a.trackCount = ").append(maxCount);
		
		return this.findSingle(sb.toString());
	}
    
	
	
	
	 /**
    * <li>说明：保存跟踪记录单
    * <li>创建人：刘国栋	
    * <li>创建日期：2016-08-09
    * <li>修改人：
    * <li>修改日期：
    * <li>修改内容：
    * @param trackRdpIDX 跟踪单idx
    * @param trackReason 本次跟踪意见
    * @param status 状态
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
    */
	public void saveTrackingRecord(String trackRdpIDX, String trackReason,Integer status) throws BusinessException, NoSuchFieldException{

		//继续跟踪，更新选择的跟踪单的跟踪次数
		ZbglTpTrackRdp zbglTpTrackRdp = zbglTpTrackRdpManager.getModelById(trackRdpIDX);
		//		根据跟踪单idx查询记录单条数
		Integer count = findCountsByZbglTrackRdpIDX(trackRdpIDX);
		//条数
		zbglTpTrackRdp.setRecordCount(count);
		
		
		//获取最大次数
		Integer maxCount = findMaxCountByZbglTrackRdpIDX(trackRdpIDX);
		//根据跟踪单idx和最大次数查询唯一记录单信息
		ZbglTpTrackRdpRecord trackRecord = findZbglTrackRdpRecordByZbglTrackRdpIDX(trackRdpIDX, maxCount);
		

		
		if (trackRecord != null) {
//			状态
			trackRecord.setStatus(status);
			//提票跟踪单idx
			trackRecord.setTrackRdpIDX(trackRdpIDX);
			//跟踪原因
			trackRecord.setTrackReason(trackReason);
			//跟踪记录单时间
			trackRecord.setTrackDate(new Date());
			//获取最大次数
			trackRecord.setTrackCount(maxCount);
			
			//获取人员信息
			AcOperator acOperator = SystemContext.getAcOperator();
			trackRecord.setTrackPersonIDX(acOperator.getOperatorid().toString());
			trackRecord.setTrackPersonName(acOperator.getOperatorname());
			this.saveOrUpdate(trackRecord);
		}else {
			throw new BusinessException("数据错误!");
		}
	}
	
	/**
     * <li>说明：保存跟踪记录单,并结束跟踪（业务类）
		* <li>创建人：刘国栋	
		* <li>创建日期：2016-08-09
		* <li>修改人：
		* <li>修改日期：
		* <li>修改内容：
		* @param trackRdpIDX 跟踪单idx
		* @param trackReason 本次跟踪意见
		* @param 
		* @return 
	    * @throws NoSuchFieldException 
	    * @throws BusinessException 
     */
	public void saveTrackedRecord(String trackRdpIDX, String trackReason,Integer status) throws BusinessException, NoSuchFieldException{
		//修改跟踪记录单状态
		saveTrackingRecord(trackRdpIDX, trackReason, status);
		
		//结束跟踪单
		ZbglTpTrackRdp zbglTpTrackRdp = zbglTpTrackRdpManager.getModelById(trackRdpIDX);
		
		
		//根据跟踪单idx查询记录单条数
		Integer count = findCountsByZbglTrackRdpIDX(trackRdpIDX);
		//条数
		zbglTpTrackRdp.setRecordCount(count);
		//状态
		zbglTpTrackRdp.setStatus(status);
		
		zbglTpTrackRdpManager.saveOrUpdate(zbglTpTrackRdp);
	}



	/**
     * <li>说明：提票跟踪记录装填修改
     * <li>创建人：刘国栋	
     * <li>创建日期：2016-08-09
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trackRdpIDX 提票跟踪单idx
     * @param trackReason 跟踪原因
     * @param flag 标识符 1==结束跟踪 0==继续跟踪
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
     * @throws IOException 
     * @throws JsonMappingException 
     */
	
    
	public void saveOrUpdateTrackingRecord(String trackRdpIDX, String trackReason, String flag) throws BusinessException, NoSuchFieldException {
		
		ZbglTpTrackRdp zbglTpTrackRdp = zbglTpTrackRdpManager.getModelById(trackRdpIDX);
		zbglTpTrackRdp.setSingleStatus(1);
		zbglTpTrackRdpManager.saveOrUpdate(zbglTpTrackRdp);
		
		//1==结束跟踪 0==继续跟踪
		//继续跟踪
		if ("0".equals(flag)) {
			saveTrackingRecord(trackRdpIDX, trackReason, 1);
		}
		//结束跟踪
		if ("1".equals(flag)) {
			saveTrackedRecord(trackRdpIDX, trackReason, 1);
		}
	}
}
