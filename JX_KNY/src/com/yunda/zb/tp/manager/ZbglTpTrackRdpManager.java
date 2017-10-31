package com.yunda.zb.tp.manager;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.Column;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountManager;
import com.yunda.zb.tp.entity.ZbglTp;
import com.yunda.zb.tp.entity.ZbglTpTrackRdp;
import com.yunda.zb.tp.entity.ZbglTpTrackRdpDTO;
import com.yunda.zb.tp.entity.ZbglTpTrackRdpRecord;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglTpTrackRdpManager业务类,提票跟踪单
 * <li>创建人：程锐
 * <li>创建日期：2015-03-10
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "zbglTpTrackRdpManager")
public class ZbglTpTrackRdpManager extends JXBaseManager<ZbglTpTrackRdp, ZbglTpTrackRdp> {
	
	/** ZbglTp业务类,JT6提票 */
    @Resource
    private ZbglTpManager zbglTpManager;
    
    /** TrainAccessAccount业务类,机车出入段台账 */
    @Resource
    private TrainAccessAccountManager trainAccessAccountManager;

	/**
     * <li>说明：根据jt6提票idx查询是否有提票跟踪单
     * <li>创建人：林欢
     * <li>创建日期：2016-8-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param jt6IDX jt6提票表主键idx
     * @param map 返回map
     * @return Map<String, Object> 返回map
     */
	public Map<String, Object> existZbglTpTrackRdpByJT6IDX(String jt6IDX, Map<String, Object> map) {
		
		map.put("success", true);
		map.put("errMsg", "");
		
		//根据jto提票idx查询提票跟踪单对象
		ZbglTpTrackRdp zbglTpTrackRdp = findZbglTpTrackRdpByJT6IDX(jt6IDX);
		if (zbglTpTrackRdp != null) {
			map.put("success", false);
			map.put("errMsg", "该JT6提票已经存在提票跟踪单，请确认！");
		}
		return map;
	}

	/**
     * <li>说明：根据jt6提票idx查询是否有提票跟踪单
     * <li>创建人：林欢
     * <li>创建日期：2016-8-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param jt6IDX jt6提票表主键idx
     * @return ZbglTpTrackRdp 返回的提票跟踪单对象
     */
	private ZbglTpTrackRdp findZbglTpTrackRdpByJT6IDX(String jt6IDX) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from ZbglTpTrackRdp a where a.jt6IDX = '").append(jt6IDX).append("'");
		return this.findSingle(sb.toString());
	}

	/**
     * <li>说明：提票跟踪主查询(正在跟踪/结束跟踪)
     * <li>创建人：林欢
     * <li>创建日期：2016-8-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询实体
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
     */
	public Page<ZbglTpTrackRdpDTO> findZbglTpTrackRdpPageList(SearchEntity<ZbglTpTrackRdp> searchEntity) throws SecurityException, NoSuchFieldException {
		ZbglTpTrackRdp entity = searchEntity.getEntity();
		
        StringBuilder sb = new StringBuilder();
        
        sb.append(" select a.train_type_shortname, ");
        sb.append(" a.train_no, ");
        sb.append(" a.fault_notice_code, ");
        sb.append(" a.status, ");
        sb.append(" b.fault_fix_fullname, ");
        sb.append(" b.fault_name, ");
        sb.append(" b.repair_result, ");
        sb.append(" b.repair_desc, ");
        sb.append(" a.track_person_name, ");
        sb.append(" a.track_date, ");
        sb.append(" a.record_count, ");
        sb.append(" a.track_reason, ");
        
        //隐藏字段
        sb.append(" a.idx, ");
        sb.append(" a.jt6_idx, ");
        sb.append(" a.Train_Type_IDX, ");
        sb.append(" a.track_person_idx, ");
        sb.append(" b.Fault_Fix_FullCode, ");
        sb.append(" b.Fault_ID, ");
        sb.append(" c.did, ");
        sb.append(" c.dname, ");
        sb.append(" d.idx as rdp_idx, ");
        sb.append(" a.single_status, ");
        sb.append(" f.in_time ");
        sb.append(" from zb_zbgl_jt6_track_rdp a ");
        sb.append(" inner join zb_zbgl_jt6 b on a.jt6_idx = b.idx ");
        sb.append(" inner join v_jczl_train c on a.train_no = c.train_no ");
        sb.append(" and a.train_type_idx = c.train_type_idx ");
        sb.append(" left join (select * ");
        sb.append(" from (SELECT T.*, ");
        sb.append(" ROW_NUMBER() OVER(PARTITION BY T.train_no, T.train_type_shortname ORDER BY T.in_time desc) RV ");
        sb.append(" from twt_train_access_account T) ");
        sb.append(" where RV = 1 and record_status = 0) t1 on a.train_no = t1.train_no ");
        sb.append(" and a.train_type_idx = t1.train_type_idx ");
        sb.append(" left join zb_zbgl_rdp d on t1.idx = d.train_access_account_idx and d.record_status = 0 ");
        sb.append(" left join twt_train_access_account f on d.train_access_account_idx = f.idx and d.record_status = 0 ");
        sb.append(" where b.record_status = 0 ");
        
        //判断车型是否传入
        if (StringUtils.isNotBlank(entity.getTrainTypeIDX())) {
        	sb.append(" and a.train_type_idx = '").append(entity.getTrainTypeIDX()).append("'");
		}
        //判断车号是否传入
        if (StringUtils.isNotBlank(entity.getTrainNo())) {
        	sb.append(" and a.train_no like '%").append(entity.getTrainNo()).append("%'");
		}
        //查询条件 - 提票时间(开始)
        if (!StringUtil.isNullOrBlank(entity.getStartTime())) {
            sb.append(" and to_char(a.track_date, 'yyyy-mm-dd hh24:mi:ss') >= '").append(entity.getStartTime()).append("'");
        }
        //查询条件 - 提票时间（结束）
        if (!StringUtil.isNullOrBlank(entity.getEndTime())){
            sb.append(" and to_char(a.track_date,'yyyy-mm-dd')<= '").append(entity.getEndTime()).append("'");
        }
        
        //查询条件 - 小于的跟踪次数
        if (!StringUtil.isNullOrBlank(entity.getLess())) {
            sb.append(" and a.record_count <= ").append(entity.getLess());
        }
//      查询条件 - 大于的跟踪次数
        if (!StringUtil.isNullOrBlank(entity.getGreater())){
            sb.append(" and a.record_count >= ").append(entity.getGreater());
        }
        
        //判断跟踪原因是否传入
        if (!StringUtil.isNullOrBlank(entity.getTrackReason())){
        	sb.append(" and a.track_reason like '%").append(entity.getTrackReason()).append("%'");
        }
        //判断跟踪原因是否传入
        if (entity.getStatus() != null){
        	sb.append(" and a.status = ").append(entity.getStatus());
        }
        
        sb.append(" order by a.track_date desc ");
        //此处的总数别名必须是ROWCOUNT，封装方法有规定
        String totalSql = "select count(*) as rowcount " + sb.substring(sb.indexOf("from"));
        return this.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, ZbglTpTrackRdpDTO.class);
	}

	/**
	 * <li>说明:通过车型车号查询提票跟踪单对象
	 * <li>创建人：林欢
	 * <li>创建日期：2016-8-8
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param trainNo 车号
	 * @param trainTypeIDX 车型
	 * @param status 提票跟踪单状态
	 * @return ZbglTpTrackRdp 提票跟踪单对象
	 */
	public ZbglTpTrackRdp findZbglTpTrackRdpByTrainNoAndTrainTypeIDX(String trainNo, String trainTypeIDX,Integer status) {
		StringBuffer sb = new StringBuffer();
		
		sb.append(" from ZbglTpTrackRdp a where a.trainNo = ").append(trainNo);
		sb.append(" and a.trainTypeIDX = ").append(trainTypeIDX);
		
		//判断状态条件是否传入
		if (status != null) {
			sb.append(" and a.status = ").append(status);
		}
		
		return this.findSingle(sb.toString());
	}

	/**
     * <li>说明：保存提票跟踪单相关信息
     * <li>创建人：林欢
     * <li>创建日期：2016-8-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param zbglTpTrackRdp 提票跟踪单相关信息
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
     * @throws Exception
     */
	public void saveZbglTpTrackRdpInfo(ZbglTpTrackRdp zbglTpTrackRdp) throws BusinessException, NoSuchFieldException {
		this.save(zbglTpTrackRdp);
		//保存后，修改提票状态为跟踪中
		ZbglTp zbglTp = zbglTpManager.getModelById(zbglTpTrackRdp.getJt6IDX());
		zbglTp.setIsTracked(ZbglTp.ISTRACKED_YES);
		zbglTpManager.saveOrUpdate(zbglTp);
		
	}

	/**
     * <li>说明：提票跟踪主查询(正在跟踪/结束跟踪)
     * <li>创建人：林欢
     * <li>创建日期：2016-8-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询参数
     * @return Page<ZbglTpTrackRdpDTO> 返回page对象
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
     * @throws Exception
     */
	public Page<ZbglTpTrackRdpDTO> findColorZbglTpTrackRdpPageList(SearchEntity<ZbglTpTrackRdp> searchEntity) throws SecurityException, NoSuchFieldException {
		//通过findZbglTpTrackRdpPageList()方法的后台sql，查询出一系列的参数，组装成显示页面
		Page<ZbglTpTrackRdpDTO> page = findZbglTpTrackRdpPageList(searchEntity);
		ZbglTpTrackRdp entity = searchEntity.getEntity();
//		判断该车是否入段了，如果入段了，修改color字段值为黄色
        //判断是否是查询正在跟踪的列表
        if (entity != null && ZbglTpTrackRdp.TRACKING.equals(entity.getStatus())) {
        	List<ZbglTpTrackRdpDTO> list = page.getList();
            for (ZbglTpTrackRdpDTO dto : list) {
            	//通过车型车号查询最新的一台车
            	
            	TrainAccessAccount t = new TrainAccessAccount();
            	t.setTrainNo(dto.getTrainNo());
            	t.setTrainTypeShortName(dto.getTrainTypeShortName());
            	
            	List<TrainAccessAccount> accountList = trainAccessAccountManager.getTrainAccessAccountListByEntity(t);
            	
            	if (accountList != null && accountList.size() > 0) {
            		TrainAccessAccount account = accountList.get(0);
            		//入段机车
					if (account.getInTime() != null && account.getOutTime() == null){
                    	dto.setColor("#FFFF00");   
                    	//当要操作本次跟踪时，黄色变成橙红色提示已开始操作
                    	if(dto.getSingleStatus() != 1){
                    		dto.setColor("#FF7F00");
                    	}
                    }
				}
            }
		}
		
		return page;
	}

	
	
	
	
	/**
     * <li>说明：创建跟踪记录单，并修改跟踪单的是否开始跟踪操作的状态
     * <li>创建人：刘国栋
     * <li>创建日期：2016-8-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trackIDX 跟踪单idx
     * @param rdpIDX   整备单idx
     * @param singleStatus  是否开始跟踪操作状态
     * @param String inTime  入段时间
     * @return String 生成的记录单的idx
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 * @throws ParseException
     */
	
	/**跟踪记录单，业务类  */
    @Resource
    private ZbglTpTrackRdpRecordManager zbglTpTrackRdpRecordManager;
    
	public String createRecordAndStartTrack(String trackIDX, String rdpIDX, String singleStatus, String inTime) throws BusinessException, NoSuchFieldException, ParseException {
		//通过传入的跟踪单idx，返回该实体对象
		ZbglTpTrackRdp zbglTpTrackRdp = this.getModelById(trackIDX);
		//调用方法获得该跟踪单的跟踪记录单的数量（跟踪次数）
		Integer counts = zbglTpTrackRdpRecordManager.findCountsByZbglTrackRdpIDX(trackIDX);
		//向该跟踪单装载数据
		zbglTpTrackRdp.setSingleStatus(0);
		zbglTpTrackRdp.setRecordCount(counts);
		//保存更新的操作
		this.saveOrUpdate(zbglTpTrackRdp);
		
		//新增跟踪记录单
		ZbglTpTrackRdpRecord zbglTpTrackRdpRecord = new ZbglTpTrackRdpRecord();
		//获得本次新增的跟踪记录单是第几次跟踪
		Integer in = zbglTpTrackRdpRecordManager.findMaxCountByZbglTrackRdpIDX(trackIDX)+1;
		//向新增的跟踪记录单中装载数据
		zbglTpTrackRdpRecord.setRdpIDX(rdpIDX);
		zbglTpTrackRdpRecord.setTrackRdpIDX(trackIDX);
		//将传过来的String类型的时间，使用DateUtil.yyyy_MM_dd_HH_mm_ss.parse()方法，解析成Date类型
		zbglTpTrackRdpRecord.setInTime(DateUtil.yyyy_MM_dd_HH_mm_ss.parse(inTime));
		zbglTpTrackRdpRecord.setStatus(0);
		zbglTpTrackRdpRecord.setTrackCount(in);
		
		//更新保存跟踪记录单
		zbglTpTrackRdpRecordManager.saveOrUpdate(zbglTpTrackRdpRecord);
		
		//向前台返回跟踪记录单的idx
		return zbglTpTrackRdpRecord.getIdx();
	}
    

}
