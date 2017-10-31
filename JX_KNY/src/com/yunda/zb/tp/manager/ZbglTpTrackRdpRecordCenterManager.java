package com.yunda.zb.tp.manager;

import java.lang.reflect.Field;

import javax.annotation.Resource;
import javax.persistence.Column;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.zb.tp.entity.ZbglTp;
import com.yunda.zb.tp.entity.ZbglTpTrackRdpRecord;
import com.yunda.zb.tp.entity.ZbglTpTrackRdpRecordCenter;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglTpTrackRdpRecordCenterManager业务类,提票跟踪记录单关联表
 * <li>创建人：程锐
 * <li>创建日期：2015-03-10
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "zbglTpTrackRdpRecordCenterManager")


public class ZbglTpTrackRdpRecordCenterManager extends JXBaseManager<ZbglTpTrackRdpRecordCenter, ZbglTpTrackRdpRecordCenter> {

	/**
     * <li>说明：通过提票跟踪单idx查询与之关联的jt6提票
     * <li>创建人：林欢
     * <li>创建日期：2016-8-9
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 分页查询条件
     * @return Page<ZbglTp> 分页提票记录
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
     * @throws Exception
     */ 
	
	
	/** ZbglTpTrackRdpRecordManager业务类,提票跟踪记录单 */
	@Resource
	private ZbglTpTrackRdpRecordManager ZbglTpTrackRdpRecordManager;

	public Page<ZbglTp> findZbglTpByTrackRdpIDX(SearchEntity<ZbglTp> searchEntity, String zbglTpTrackIDX) throws SecurityException, NoSuchFieldException {
		
		//通过跟踪单的idx找到最新的一次记录单的idx
		Integer max = ZbglTpTrackRdpRecordManager.findMaxCountByZbglTrackRdpIDX(zbglTpTrackIDX);
		StringBuilder sb = new StringBuilder();
        
        sb.append(" select a.* ");
        sb.append(" from zb_zbgl_jt6                  a, ");
        sb.append(" zb_zbgl_jt6_record_center    b");
        sb.append(" where a.record_status = 0 ");
        sb.append(" and a.idx = b.jt6_idx ");
        
        //通过跟踪单idx查询记录单idx
        ZbglTpTrackRdpRecord zbglTpTrackRdpRecord = ZbglTpTrackRdpRecordManager.findZbglTrackRdpRecordByZbglTrackRdpIDX(zbglTpTrackIDX, max);
		if(zbglTpTrackRdpRecord!= null){
			String recordIDX = zbglTpTrackRdpRecord.getIdx();
			if (StringUtils.isNotBlank(recordIDX)) {
	        	sb.append(" and b.track_rdp_record_idx = '").append(recordIDX).append("'");
			}
		}
        // 排序处理
        Order[] orders = searchEntity.getOrders();
        if (null != orders && orders.length > 0) {
            String[] order = orders[0].toString().split(" ");
            String sort = order[0];
            //前台传递过来的排序方式 desc或者asc
            String dir = order[1];
            Class clazz = ZbglTp.class;
            //通过传递过来需要排序的字段反射字段对象
            Field field = clazz.getDeclaredField(sort);
            //获取字段上，标签上的列名
            Column annotation = field.getAnnotation(Column.class);
            if (null != annotation) {
                sb.append(" order by a.").append(annotation.name()).append(" ").append(dir);
            } else {
                sb.append(" order by a.").append(sort).append(" ").append(dir);
            }
        } else {
            sb.append(" order by a.update_time desc ");
        }
        //此处的总数别名必须是ROWCOUNT，封装方法有规定
        String totalSql = "select count(*) as rowcount " + sb.substring(sb.indexOf("from"));
        return this.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, ZbglTp.class);
	}

	
	/**
     * <li>说明：通过跟踪单idx，获得和它关联的最后生成的一张跟踪提票记录单的idx主键（业务类）
     * <li>创建人：刘国栋	
     * <li>创建日期：2016-08-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trackIDX 跟踪单的idx主键
     * @return String 最近一张跟踪记录单idx
     */
	@Resource 
	private ZbglTpTrackRdpRecordManager zbglTpTrackRdpRecordManager;
	public String getRecordByTrackIDX(String trackIDX) {
		//获得最大记录次数，及最近一张跟踪记录单
		Integer maxCount = zbglTpTrackRdpRecordManager.findMaxCountByZbglTrackRdpIDX(trackIDX);
		if(maxCount > 0 ){
		ZbglTpTrackRdpRecord zbglTpTrackRdpRecord = zbglTpTrackRdpRecordManager.findZbglTrackRdpRecordByZbglTrackRdpIDX(trackIDX, maxCount);
		//通过跟踪记录单实体对象，获得它的idx
		String trackRdpRecordIDX = zbglTpTrackRdpRecord.getIdx();
		return trackRdpRecordIDX;
		}else{
			return null;
		}
	}

	
	/**
     * <li>说明：通过新生成的jt6提票单，获取它的idx主键（业务类）
     * <li>创建人：刘国栋	
     * <li>创建日期：2016-08-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ZbglRdp rdp 新增的jt6提票单
     * @return String 新增jt6提票单idx 
     */
	public String getNewJt6IDXByRdp(ZbglTp tp) {
		//通过rdp实体对象，获得它的idx
		String tpIDX = tp.getIdx();
		return tpIDX;
	}

	
	
	/**
     * <li>说明：保存新增jt6的idx和最近一张跟踪记录单的idx到ZbglTpTrackRdpRecordCenter中间关系表中（业务类）
     * <li>创建人：刘国栋	
     * <li>创建日期：2016-08-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ZbglRdp rdp 新增的jt6提票单
     * @param String trackIDX 跟踪单idx
     */
	public void saveToTrackRdpRecordCenter(String trackIDX, ZbglTp tp) {
		String tpIDX = this.getNewJt6IDXByRdp(tp);
		String trackRdpRecordIDX = this.getRecordByTrackIDX(trackIDX);
		ZbglTpTrackRdpRecordCenter zbglTpTrackRdpRecordCenter = new ZbglTpTrackRdpRecordCenter();
		zbglTpTrackRdpRecordCenter.setJt6IDX(tpIDX);
		zbglTpTrackRdpRecordCenter.setTrackRdpRecordIDX(trackRdpRecordIDX);
		this.save(zbglTpTrackRdpRecordCenter);
	}


	
	/**
     * <li>说明：通过提票跟踪记录单idx查询与之关联的jt6提票
     * <li>创建人：刘国栋
     * <li>创建日期：2016-8-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 分页查询条件
     * @return Page<ZbglTp> 分页提票记录
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
     */ 
	public Page<ZbglTp> findZbglTpByTrackRdpRecordIDX(SearchEntity<ZbglTp> searchEntity, String recordIDX) throws SecurityException, NoSuchFieldException {
		
		StringBuilder sb = new StringBuilder();
        
        sb.append(" select a.* ");
        sb.append(" from zb_zbgl_jt6                  a, ");
        sb.append(" zb_zbgl_jt6_record_center    b");
        sb.append(" where a.record_status = 0 ");
        sb.append(" and a.idx = b.jt6_idx ");
        sb.append(" and b.track_rdp_record_idx = '").append(recordIDX).append("'");
        
        // 排序处理
        Order[] orders = searchEntity.getOrders();
        if (null != orders && orders.length > 0) {
            String[] order = orders[0].toString().split(" ");
            String sort = order[0];
            //前台传递过来的排序方式 desc或者asc
            String dir = order[1];
            Class clazz = ZbglTp.class;
            //通过传递过来需要排序的字段反射字段对象
            Field field = clazz.getDeclaredField(sort);
            //获取字段上，标签上的列名
            Column annotation = field.getAnnotation(Column.class);
            if (null != annotation) {
                sb.append(" order by a.").append(annotation.name()).append(" ").append(dir);
            } else {
                sb.append(" order by a.").append(sort).append(" ").append(dir);
            }
        } else {
            sb.append(" order by a.update_time desc ");
        }
        //此处的总数别名必须是ROWCOUNT，封装方法有规定
        String totalSql = "select count(*) as rowcount " + sb.substring(sb.indexOf("from"));
        return this.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, ZbglTp.class);
	}
    
}
