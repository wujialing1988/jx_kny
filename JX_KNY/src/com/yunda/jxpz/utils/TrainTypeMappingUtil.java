package com.yunda.jxpz.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.yunda.Application;
import com.yunda.jx.base.jcgy.entity.TrainType;
import com.yunda.jx.base.jcgy.manager.TrainTypeManager;
import com.yunda.jxpz.trainshort.entity.TrainShort;

/**
 * <li>标题：股道车型简称映射
 * <li>说明：
 * <li>创建人：张凡
 * <li>创建时间：2014-5-5
 * <li>修改人：
 * <li>修 改时间：
 * <li>修改内容：
 * 
 * @author PEAK-CHEUNG
 * 
 */
public class TrainTypeMappingUtil {

	private static Map<String, String> shorts = new HashMap<String, String>();

	private static Map<String, TrainType> types = new HashMap<String, TrainType>();
	// CRIF 2015-05-20 汪东良 高
	// 此类的缓存方式存在的风险是，在系统运行时，在数据库中增加的数据无法查询出来，且只有重启系统后加入的数据才有效。
	static {
		fillMaps();
	}

	/**
	 * <li>方法说明：填充基础数据
	 * <li>方法名称：fillMaps
	 * <li>
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2014-5-6 上午10:37:39
	 * <li>修改人：
	 * <li>修改内容：
	 */
	private static void fillMaps() {
		TrainTypeManager m = (TrainTypeManager) Application.getSpringApplicationContext().getBean("trainTypeManager");
		fillTypes(findTypes(m));
		fillShorts(findShorts(m));
	}

	/**
	 * <li>方法说明：填充机车型号
	 * <li>方法名称：fillTypes
	 * <li>
	 * 
	 * @param list
	 *            <li>return: void
	 *            <li>创建人：张凡
	 *            <li>创建时间：2014-5-6 上午10:37:55
	 *            <li>修改人：
	 *            <li>修改内容：
	 */
	private static void fillTypes(List<TrainType> list) {
		types.clear();
		for (TrainType train : list) {
			types.put(train.getShortName().toUpperCase(), train);
		}
	}

	/**
	 * <li>方法说明：填充机车简称
	 * <li>方法名称：fillShorts
	 * <li>
	 * 
	 * @param list
	 *            <li>return: void
	 *            <li>创建人：张凡
	 *            <li>创建时间：2014-5-6 上午10:38:01
	 *            <li>修改人：
	 *            <li>修改内容：
	 */
	private static void fillShorts(List<TrainShort> list) {
		shorts.clear();
		for (TrainShort shor : list) {
			shorts.put(shor.getShortName().toUpperCase(), shor.getTrainShort());
		}
	}

	/**
	 * <li>说明：查询机车型号
	 * <li>创建人：张凡
	 * <li>创建日期：2014-5-5
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param m
	 *            车型业务类对象
	 * @return 车型列表
	 */
	@SuppressWarnings("unchecked")
	private static List<TrainType> findTypes(TrainTypeManager m) {

		List<TrainType> list = (List<TrainType>) m.find("from TrainType");
		return list;
	}

	/**
	 * <li>方法说明：查询机车简称
	 * <li>方法名称：findShorts
	 * <li>
	 * 
	 * @param m
	 *            车型业务类对象
	 *            <li>
	 * @return
	 *            <li>return: List<TrainShort>
	 *            <li>创建人：张凡
	 *            <li>创建时间：2014-5-6 上午10:38:20
	 *            <li>修改人：
	 *            <li>修改内容：
	 */
	@SuppressWarnings("unchecked")
	private static List<TrainShort> findShorts(TrainTypeManager m) {

		List<TrainShort> list = (List<TrainShort>) m.find("from TrainShort");
		return list;
	}

	/**
	 * <li>方法说明：根据简称或取车型
	 * <li>方法名称：getTrainTypeOfShorName
	 * <li>
	 * 
	 * @param shortTypeName
	 *            <li>
	 * @return
	 *            <li>return: String
	 *            <li>创建人：张凡
	 *            <li>创建时间：2014-5-5 下午04:23:58
	 *            <li>修改人：
	 *            <li>修改内容：
	 */
	public static TrainType getTrainTypeOfShorName(String shortTypeName) {
		String trainType = shorts.get(shortTypeName.toUpperCase());
		if (trainType == null) {
			return getTrainType(shortTypeName.toUpperCase());
		}
		return getTrainType(trainType.toUpperCase());
	}

	/**
	 * <li>方法说明：根据车型全称获取简称
	 * <li>方法名称：getTrainShortName
	 * <li>
	 * 
	 * @param fullName
	 *            <li>
	 * @return
	 *            <li>return: String
	 *            <li>创建人：张凡
	 *            <li>创建时间：2014-5-5 下午04:24:12
	 *            <li>修改人：
	 *            <li>修改内容：
	 */
	public static String getTrainShortName(String fullName) {

		Set<Entry<String, String>> entrys = shorts.entrySet();
		for (Entry<String, String> entry : entrys) {
			if (entry.getValue().equals(fullName.toUpperCase())) {
				return entry.getKey();
			}
		}
		return "";
	}

	/**
	 * 
	 * <li>说明：根据台位图传回的机车信息获取车号
	 * <li>创建人：程锐
	 * <li>创建日期：2014-5-30
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param trainInfo
	 *            台位图传回的机车信息
	 * @return 车号
	 */
	public static String getTrainNO(String trainInfo) {
		String trainNo = trainInfo.substring(2, trainInfo.length());
		String newTrainNo = "";
		for (int i = 0; i < trainNo.length(); i++) {
			char charter = trainNo.charAt(i);
			try {
				Integer.parseInt(String.valueOf(charter));
				newTrainNo = newTrainNo + charter;
			} catch (Exception e) {
				newTrainNo = "";
			}

		}
		trainNo = newTrainNo;

		if (trainNo.length() < 4) {
			int count = 4 - trainNo.length();
			for (int i = 0; i < count; i++) {
				trainNo = "0" + trainNo;
			}
		}
		return trainNo;
	}

	/**
	 * <li>方法说明：获取车型
	 * <li>方法名称：getTrainType
	 * <li>
	 * 
	 * @param trainType
	 *            <li>
	 * @return
	 *            <li>return: TrainType
	 *            <li>创建人：张凡
	 *            <li>创建时间：2014-5-5 下午05:05:25
	 *            <li>修改人：
	 *            <li>修改内容：
	 */
	public static TrainType getTrainType(String trainType) {

		return types.get(trainType);
	}

}
