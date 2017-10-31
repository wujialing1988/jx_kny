package com.yunda.jx.scdd.repairplan.manager;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.base.jcgy.entity.TrainType;
import com.yunda.jx.base.jcgy.manager.TrainTypeManager;
import com.yunda.jx.scdd.repairplan.entity.RepairStandard;
import com.yunda.jx.scdd.repairplan.entity.RunningKM;
import com.yunda.jx.scdd.repairplan.entity.RunningKMHistory;
import com.yunda.jx.util.MixedUtils;
import com.yunda.passenger.traindemand.entity.TrainDemand;
import com.yunda.passenger.traindemand.manager.TrainDemandManager;
import com.yunda.util.BeanUtils;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: RunningKM业务类，走行公里
 * <li>创建人：张凡
 * <li>创建日期：2015-10-28
 * <li>修改人: 何涛
 * <li>修改日期：2016-04-30
 * <li>修改内容：对接北京博飞运安系统走行公里数据
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value="runningKMManager")
public class RunningKMManager extends JXBaseManager<RunningKM, RunningKM> {

	@Resource
	private TrainTypeManager trainTypeManager;
    
    /**
     * 客车编组需求
     */
    @Resource
    private TrainDemandManager trainDemandManager ;
	
	private Pattern pattern = Pattern.compile("^\\d{4}$");
	
	private Pattern trainNoPattern = Pattern.compile("^\\d{4}[AB]?$");
	
	/**
	 * <li>方法说明： 处理导入数据
	 * <li>方法名：handleImportData
	 * @param is 输入流
	 * @throws IOException
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-30
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public void handleImportData(InputStream is) throws IOException, BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException{
		HSSFWorkbook wb = new HSSFWorkbook(is);
		HSSFSheet sheet =  wb.getSheetAt(0);
		int rownum = 0;
		Map<String, TrainType> trainTypeMap = new HashMap<String, TrainType>();
		
		String trainType, trainNo, year, km, month;
		TrainType train;
		List<RunningKM> rkmList = new ArrayList<RunningKM>();
		Date regDate = new Date();
		Calendar calendar = Calendar.getInstance();
		HSSFRow row = sheet.getRow(rownum++);
		if("车型".equals(getCellValue(row, 0)) == false)
			throw new RuntimeException("走行公里导入数据文件验证异常");
		
		while(true){
			row = sheet.getRow(rownum++);
			if(row == null || getZeroCell(row).equals("")){
				break;
			}
			trainType = getCellValue(row, 0);
			train = trainTypeMap.get(trainType);
			
			if(train == null){
				train = trainTypeManager.getTrainType(trainType);
				if(train == null || StringUtil.nvl(train.getRepairType()).equals("")){
					trainTypeMap.put(trainType, new TrainType());
					continue;
				}
				trainTypeMap.put(trainType, train);
			} else if (train.getTypeID().equals("")) {
				continue;
			} 
			
			trainNo = getCellValue(row, 1);
			if(trainNoPattern.matcher(trainNo).matches() ==  false){
				continue;
			}
			km = getCellValue(row, 2);
			
			
			year = getCellValue(row, 3);
			if(pattern.matcher(year).matches() == false){
				continue;
			}
			month = getCellValue(row, 4);
			
			try{
				calendar.set(Calendar.YEAR, Integer.parseInt(year));
				calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				
				RunningKM rkm = new RunningKM(); 
				rkm.setTrainType(trainType);
				rkm.setTrainTypeIdx(train.getTypeID());
				rkm.setRegDate(regDate);
				rkm.setTrainNo(trainNo);
				rkm.setRecentlyRunningKm(Float.valueOf(km));
				rkm.setRepairType(train.getRepairType());
				rkm.setBeginDate(calendar.getTime());
				calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
				rkm.setEndDate(calendar.getTime());
				rkmList.add(rkm);
			}catch(Exception e){
				continue;
			}
		}
		Collections.sort(rkmList, getComparator());
		updateBatchSave(rkmList);
	}

	/**
	 * <li>方法说明：获取排序对象
	 * <li>方法名：getComparator
	 * @return Comparator<RunningKM>对象
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-31
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	private Comparator<RunningKM> getComparator() {
		return new Comparator<RunningKM>(){
			@Override
			public int compare(RunningKM o1, RunningKM o2) {
				
				if(o1.getBeginDate().after(o2.getBeginDate())){
					return 1;
				}
				return 0;
			}
		};
	}
	
	/**
	 * <li>方法说明：获取行第1列数据
	 * <li>方法名：getZeroCell
	 * @param row 行
	 * @return
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-30
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	private String getZeroCell(HSSFRow row){
		return getCellValue(row, 0);
	}
	
	/**
	 * <li>方法说明：获取单元格数据
	 * <li>方法名：getCellValue
	 * @param row 行
	 * @param cellIndex 列索引
	 * @return
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-30
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	private String getCellValue(HSSFRow row, int cellIndex){
		return StringUtil.nvl(row.getCell((short)cellIndex));
	}
	
	/**
	 * <li>方法说明：批量保存导入数据
	 * <li>方法名：updateBatchSave
	 * @param rkmList 走行集合
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-30
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	private void updateBatchSave(List<RunningKM> rkmList) throws BusinessException, NoSuchFieldException,
													IllegalAccessException, InvocationTargetException{
		
		Map<String, List<RepairStandard>> map = getAllTrainRepairStandard();
		List<RepairStandard> clips = null;
		for(RunningKM rkm : rkmList){
			handleData(rkm);
			
			clips = map.get(rkm.getTrainTypeIdx());
			if(MixedUtils.listIsEmpty(clips) == false){
				setNextRepair(rkm, clips);
			}
			super.saveOrUpdate(rkm);
		}
	}
	
	/**
	 * <li>方法说明：处理数据
	 * <li>方法名：handleData
	 * @param rkm 走行公里
	 * @return 操作结果(1有历史，0无历史)
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-30
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	private int handleData(RunningKM rkm) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException{
		RunningKM history = findEntityByTrainInfo(rkm.getTrainTypeIdx(), rkm.getTrainNo());
		if(history == null){
			rkm.setNewRunningKm(rkm.getRecentlyRunningKm());
			rkm.setC1(rkm.getRecentlyRunningKm());
			rkm.setC2(rkm.getRecentlyRunningKm());
			rkm.setC3(rkm.getRecentlyRunningKm());
			rkm.setC4(rkm.getRecentlyRunningKm());
			rkm.setC5(rkm.getRecentlyRunningKm());
			rkm.setC6(rkm.getRecentlyRunningKm());
			return 0;
		}
		rkm.setNewRunningKm(history.getNewRunningKm() + rkm.getRecentlyRunningKm());
		rkm.setC1(history.getC1() + rkm.getRecentlyRunningKm());
		rkm.setC2(history.getC2() + rkm.getRecentlyRunningKm());
		rkm.setC3(history.getC3() + rkm.getRecentlyRunningKm());
		rkm.setC4(history.getC4() + rkm.getRecentlyRunningKm());
		rkm.setC5(history.getC5() + rkm.getRecentlyRunningKm());
		rkm.setC6(history.getC6() + rkm.getRecentlyRunningKm());
		updateToHistory(history);
		return 1;
	}
	
	/**
	 * <li>方法说明：根据车查询走行
	 * <li>方法名：findByTrain
	 * @param trainTypeIdx 车型
	 * @param trainNo 车号
	 * @return
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-27
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public RunningKM findByTrain(String trainTypeIdx, String trainNo){
		RunningKM rkm = findEntityByTrainInfo(trainTypeIdx, trainNo);
		if(rkm == null) return null;
		RunningKM newKM = new RunningKM();
		newKM.setNewRunningKm(rkm.getNewRunningKm());
		newKM.setC1(null == rkm.getC1()?0:rkm.getC1());
		newKM.setC2(null == rkm.getC2()?0:rkm.getC2());
		newKM.setC3(null == rkm.getC3()?0:rkm.getC3());
		newKM.setC4(null == rkm.getC4()?0:rkm.getC4());
		newKM.setC5(null == rkm.getC5()?0:rkm.getC5());
		newKM.setC6(null == rkm.getC6()?0:rkm.getC6());
		newKM.setRecentlyRunningKm(rkm.getRecentlyRunningKm());
		return newKM;
	}

	/**
	 * <li>方法说明：根据车信息查询走行实体
	 * <li>方法名：findEntityByTrainInfo
	 * @param trainTypeIdx 车型
	 * @param trainNo 车号
	 * @return
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-28
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	private RunningKM findEntityByTrainInfo(String trainTypeIdx, String trainNo) {
		return (RunningKM) daoUtils.findSingle("from RunningKM where trainTypeIdx = ? and trainNo = ?", trainTypeIdx, trainNo);
	}
	
	
/*	@Override
	public void saveOrUpdate(RunningKM t) throws BusinessException,
			NoSuchFieldException {
		if(StringUtil.isNullOrBlank(t.getIdx())){
			t.setRegDate(new Date());
		}
		toHistory(t);
		setNextRepair(t, getTrainRepairStandard(t.getTrainTypeIdx()));
		//保存新走行
		super.saveOrUpdate(t);
	}*/
	
	/**
	 * <li>方法说明：往历史里存
	 * <li>方法名：toHistory
	 * @param t 实体
	 * <li>创建人： 张凡
	 * <li>创建日期：2015年11月10日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
    private void toHistory(RunningKM t) {
        RunningKM rkm = findEntityByTrainInfo(t.getTrainTypeIdx(), t.getTrainNo());
		if(rkm != null){			
			try {
				updateToHistory(rkm);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
    }

	/**
	 * <li>方法说明：新增历史，删除当前数据
	 * <li>方法名：updateToHistory
	 * @param rkm 走行公里
	 * @throws NoSuchFieldException
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-29
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	private void updateToHistory(RunningKM rkm) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
		
		RunningKMHistory story = new RunningKMHistory();
		//复制历史
		BeanUtils.copyProperties(story, rkm);
		story.setIdx(null);
		//保存历史
		MixedUtils.saveOrUpdate(story, daoUtils);
		//删除走行数据
		// daoUtils.remove(rkm);
	}
	
	/**
	 * <li>方法说明：修后清零
	 * <li>方法名：updateClear
	 * @param idx 走行主键
	 * @param index 清零索引
	 * @return 操作结果（0 索引为空，1数据无改变，2清零成功）
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-29
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public int updateClear(String idx, int index) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(index == 0) return 0;
		RunningKM rkm = getModelById(idx);
		if(rkm == null) return 0;
		int sum = 0;		
		RunningKM newRkm = new RunningKM();
		BeanUtils.copyProperties(newRkm, rkm);
		newRkm.setIdx(null);
		for(int i = 1; i <= index; i++){
			String fstr = "c" + i;
			Field field = RunningKM.class.getDeclaredField(fstr);
			field.setAccessible(true);
			Float value = (Float)field.get(rkm);
			field.set(newRkm, Float.valueOf(0));
			if(value != null) sum += value;
		}
		if(sum == 0) return 1;
		else{
			updateToHistory(rkm);
			super.saveOrUpdate(newRkm);
			return 2;
		}
	}
	
	/**
	 * <li>方法说明：查询最后一笔未删除的历史数据
	 * <li>方法名：findLastHistory
	 * @param trainTypeIdx 车型主键
	 * @param trainNo 车号
	 * @return
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-29
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	private RunningKMHistory findLastHistory(String trainTypeIdx, String trainNo){
		String hql = "from RunningKMHistory where trainTypeIdx = ? and trainNo = ? and recordStatus=0 order by createTime desc";
		return (RunningKMHistory) daoUtils.findSingle(hql, trainTypeIdx, trainNo);
	}
	
	/**
	 * <li>方法说明：撤销操作
	 * <li>方法名：updateUndo
	 * @param idx 主键
	 * @return 无用的结果返回
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-29
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public int updateUndo(String idx) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException{
		RunningKM rkm = getModelById(idx);
		if(rkm == null) throw new RuntimeException("数据变更，请刷新后重试！");
		RunningKMHistory story = deleteHistory(rkm.getTrainTypeIdx(), rkm.getTrainNo());
		if(story == null) return 0;
		BeanUtils.copyProperties(rkm, story);
		rkm.setIdx(idx);
		super.saveOrUpdate(rkm);
		return 1;
	}

	/**
	 * <li>方法说明：逻辑删除最新的一笔历史数据
	 * <li>方法名：deleteHistory
	 * @param trainTypeIdx 车型主键
	 * @param trainNo 车号
	 * @return 走行历史
	 * @throws NoSuchFieldException
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-29
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	private RunningKMHistory deleteHistory(String trainTypeIdx, String trainNo)
			throws NoSuchFieldException {
		RunningKMHistory story = findLastHistory(trainTypeIdx, trainNo);
		if(story == null) return null;
		EntityUtil.setSysinfo(story);
		EntityUtil.setDeleted(story);
		daoUtils.update(story);
		return story;
	}
	
	/**
	 * <li>方法说明：查询全部检修标准
	 * <li>方法名：getAllTrainRepairStandard
	 * @return 检修标准集合
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-31
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	private Map<String, List<RepairStandard>> getAllTrainRepairStandard(){
		String hql = "from RepairStandard order by trainTypeIdx, minRunningKm desc";
		String trainTypeIdx = null;
		List<RepairStandard> clips = null;
		List<RepairStandard> list = daoUtils.find(hql);
		Map<String, List<RepairStandard>> map = new HashMap<String, List<RepairStandard>>();
		for(RepairStandard t : list){
			if(StringUtil.isNullOrBlank(t.getTrainTypeIdx()))
				continue;
			if(t.getTrainTypeIdx().equals(trainTypeIdx)){
				clips.add(t);
			}else{
				clips = new ArrayList<RepairStandard>();
				clips.add(t);
				trainTypeIdx = t.getTrainTypeIdx();
				map.put(trainTypeIdx, clips);
			}
		}
		return map;
	}
	/**
	 * <li>方法说明：查询一种车型检修标准
	 * <li>方法名：getTrainRepairStandard
	 * @param trainTypeIdx 车型主键
	 * @return 检修标准集合
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-31
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	private List<RepairStandard> getTrainRepairStandard(String trainTypeIdx){
		String hql = "from RepairStandard where trainTypeIdx = ? order by minRunningKm desc";
		return daoUtils.find(hql, trainTypeIdx);
	}
	
	/**
	 * <li>方法说明： 设置走行下一次修程
	 * <li>方法名：setNextRepair
	 * @param rkm 走行
	 * @param list 检修标准集合
	 * @return 匹配上否
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-31
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	private boolean setNextRepair(RunningKM rkm, List<RepairStandard> list){
	    if(RunningKM.TYPE_C1C6.equals(rkm.getRepairType())){
            if(null!=rkm.getC6() && matchNextRepair(rkm.getC6(), rkm, list)){
                return true;
            }
            if(null!=rkm.getC5() && matchNextRepair(rkm.getC5(), rkm, list)){
                return true;
            }
        }
	    if(null!=rkm.getC4() && matchNextRepair(rkm.getC4(), rkm, list)){
            return true;
        }
	    
	    if(RunningKM.TYPE_FXDX.equals(rkm.getRepairType())){
	        if(matchNextRepair(rkm.getC3(), rkm, list)){
	            return true;
	        }
	    }
	    rkm.setRepairClass(null);
	    rkm.setRepairClassName(null);
	    rkm.setRepairOrder(null);
	    rkm.setRepairOrderName(null);
        return false;
	}

	/**
	 * <li>方法说明：匹配下一次修程
	 * <li>方法名：matchNextRepair
	 * @param km 当前匹配走行
	 * @param rkm 待匹配走行
	 * @param list 待匹配检修标准
	 * @return
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-31
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	private boolean matchNextRepair(float km, RunningKM rkm, List<RepairStandard> list) {
	    for(RepairStandard t : list){
            if(t.getMinRunningKm() <= km && t.getMaxRunningKm() >= km){
                setRepair(rkm, t);
                return true;
            }
        }
		return false;
	}

	/**
	 * <li>方法说明：设置修程信息
	 * <li>方法名：setRepair
	 * @param rkm 走行实体
	 * @param t 修程信息
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-31
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	private void setRepair(RunningKM rkm, RepairStandard t) {
		rkm.setRepairClassName(t.getRepairClassName());
		rkm.setRepairClass(t.getRepairClass());
		rkm.setRepairOrder(t.getRepairOrder());
		rkm.setRepairOrderName(t.getRepairOrderName());
	}
	
	/**
	 * <li>方法说明：计算下次修程
	 * <li>方法名：updateComputeNextRepair
	 * @param repairType 修程类型
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-31
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public void updateComputeNextRepair(String repairType) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException{
		List<RunningKM> list = daoUtils.find("from RunningKM where repairType = ?", repairType);
		Map<String, List<RepairStandard>> map = new HashMap<String, List<RepairStandard>>();
		List<RepairStandard> standardList = null;
		for(RunningKM t : list){
		    if(map.containsKey(t.getTrainTypeIdx())){
		        standardList = map.get(t.getTrainTypeIdx());
		    }else{
		        standardList = getTrainRepairStandard(t.getTrainTypeIdx());
		        map.put(t.getTrainTypeIdx(), standardList);
		    }
		    setNextRepair(t, standardList); //匹配成功，设置修程，匹配未成功，清除修程信息
		    super.saveOrUpdate(t);
		}
	}

    /**
     * <li>说明：批量设置走行公里
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids
     * @param newRunningKm
     * @return
     */
    public String setRunningKm(String[] ids, String newRunningKm) {
        String idxs = "" ;
        for (String idx : ids) {
            idxs += "'" + idx + "'," ;
        }
        if(!StringUtil.isNullOrBlank(idxs)){
            idxs = idxs.substring(0, idxs.length() - 1);
        }
        String sql = " update JCJX_RUNNING_KM k set k.NEW_RUNNING_KM = "+newRunningKm+" where IDX in ("+idxs+")" ;
        this.daoUtils.executeSql(sql);
        return null;
    }

    /**
     * <li>说明：同步车辆信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public String synchronizeData() throws BusinessException, NoSuchFieldException {
       StringBuffer sql = new StringBuffer(" select t.idx,t.train_type_idx,t.train_type_shortname,t.train_no from JCZL_TRAIN t where t.record_status = 0 and t.t_vehicle_type = '20' ");
       sql.append(" and not exists (select 1 from JCJX_RUNNING_KM k where k.train_type_idx = t.train_type_idx and k.train_no = t.train_no) ");
       List<Object[]> list = this.daoUtils.executeSqlQuery(sql.toString());
       if (CollectionUtils.isEmpty(list)) {
           return "没有需要同步的数据！" ;
       }
       for (int i = 0; i < list.size(); i++) {
            Object[] obj = list.get(i);
            String trainTypeIdx = obj[1].toString();
            String trainTypeShortname = obj[2].toString();
            String trainNo = obj[3].toString();
            RunningKM rk = new RunningKM();
            rk.setTrainTypeIdx(trainTypeIdx);
            rk.setTrainType(trainTypeShortname);
            rk.setTrainNo(trainNo);
            rk.setNewRunningKm(Float.parseFloat("0"));
            rk.setRecentlyRunningKm(Float.parseFloat("0"));
            this.saveOrUpdate(rk);
       }
       return null ;
    }
    
    /**
     * <li>说明：更新走形公里并保存历史
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx
     * @param trainType
     * @param trainNo
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     * @throws InvocationTargetException 
     * @throws Exception 
     */
    public void updateRunningKM(String trainTypeIdx,String trainType,String trainNo) throws Exception{
        RunningKM runningKM = this.getRunningKMByTrain(trainTypeIdx, trainNo);
        if(runningKM == null){
            runningKM = this.saveRunningKM(trainTypeIdx, trainType, trainNo);
        }
        TrainDemand demand = trainDemandManager.findTrainDemandByTrain(trainTypeIdx, trainNo);
        runningKM.setRegDate(new Date());
        Float km = (demand == null || demand.getKilometers() == null) ? 0f :demand.getKilometers();
        runningKM.setRecentlyRunningKm(runningKM.getRecentlyRunningKm()+ km);
        updateToHistory(runningKM); // 保存历史
        this.update(runningKM);
    }
    
    /**
     * <li>说明：新建走形公里
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx
     * @param trainType
     * @param trainNo
     * @return
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private RunningKM saveRunningKM(String trainTypeIdx,String trainType,String trainNo) throws BusinessException, NoSuchFieldException{
        RunningKM runningKM = new RunningKM();
        runningKM.setRegDate(new Date());
        runningKM.setTrainTypeIdx(trainTypeIdx);
        runningKM.setTrainType(trainType);
        runningKM.setTrainNo(trainNo);
        runningKM.setRecentlyRunningKm(0f);
        runningKM.setNewRunningKm(0f);
        this.saveOrUpdate(runningKM);
        return runningKM;
    }
    
    /**
     * <li>说明：根据车型车号获取车型公里
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx 车型ID
     * @param trainNo 车号
     * @return
     */
    public RunningKM getRunningKMByTrain(String trainTypeIdx,String trainNo){
        StringBuffer hql = new StringBuffer(" from RunningKM where trainTypeIdx = ? and trainNo = ?");
        return (RunningKM)this.daoUtils.findSingle(hql.toString(), new Object[]{trainTypeIdx,trainNo});
    }
}
