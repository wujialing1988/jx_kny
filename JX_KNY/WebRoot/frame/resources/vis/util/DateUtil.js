/**
 * 获取本周、本季度、本月、上月的开端日期、停止日期
 */
var now = new Date(); // 当前日期
var nowDayOfWeek = now.getDay(); // 今天本周的第几天
var nowDay = now.getDate(); // 当前日
var nowMonth = now.getMonth(); // 当前月
var nowYear = now.getYear(); // 当前年
nowYear += (nowYear < 2000) ? 1900 : 0; //

var lastMonthDate = new Date(); // 上月日期
lastMonthDate.setDate(1);
lastMonthDate.setMonth(lastMonthDate.getMonth() - 1);var lastYear = lastMonthDate.getYear();
var lastMonth = lastMonthDate.getMonth();

// 格局化日期：yyyy-MM-dd
function formatDate(date) {
	var myyear = date.getFullYear();
	var mymonth = date.getMonth() + 1;
	var myweekday = date.getDate();

	if (mymonth < 10) {
		mymonth = "0" + mymonth;
	}
	if (myweekday < 10) {
		myweekday = "0" + myweekday;
	}
	return (myyear + "-" + mymonth + "-" + myweekday);
}

// 获得某月的天数
function getMonthDays(myMonth) {
	var monthStartDate = new Date(nowYear, myMonth, 1);
	var monthEndDate = new Date(nowYear, myMonth + 1, 1);
	var days = (monthEndDate - monthStartDate) / (1000 * 60 * 60 * 24);
	return days;
}

// 获取当天的起始时间00:00:00
function getDayStartTime() {
	var now = new Date();
	now.setHours(0);
	now.setMinutes(0);
	now.setSeconds(0);
	return now;
}

// 获取当天的结束时间23:59:59
function getDayEndTime() {
	var now = new Date();
	now.setHours(23);
	now.setMinutes(59);
	now.setSeconds(59);
	return now;
}

// 获得本季度的开端月份
function getQuarterStartMonth() {
	var quarterStartMonth = 0;
	if (nowMonth < 3) {
		quarterStartMonth = 0;
	}
	if (2 < nowMonth && nowMonth < 6) {
		quarterStartMonth = 3;
	}
	if (5 < nowMonth && nowMonth < 9) {
		quarterStartMonth = 6;
	}
	if (nowMonth > 8) {
		quarterStartMonth = 9;
	}
	return quarterStartMonth;
}

// 获得本周的开端日期
function getWeekStartDate() {
	var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek);
	return formatDate(weekStartDate);
}

// 获得本周的停止日期
function getWeekEndDate() {
	var weekEndDate = new Date(nowYear, nowMonth, nowDay + (7 - nowDayOfWeek));
	return formatDate(weekEndDate);
}

// 获得本月的开端日期
function getMonthStartDate() {
	var monthStartDate = new Date(nowYear, nowMonth, 1);
	return formatDate(monthStartDate);
}

// 获得本月的停止日期
function getMonthEndDate() {
	var monthEndDate = new Date(nowYear, nowMonth, getMonthDays(nowMonth));
	return formatDate(monthEndDate);
}

// 获得上月开端时候
function getLastMonthStartDate() {
	var lastMonthStartDate = new Date(nowYear, lastMonth, 1);
	return formatDate(lastMonthStartDate);
}

// 获得上月停止时候
function getLastMonthEndDate() {
	var lastMonthEndDate = new Date(nowYear, lastMonth, getMonthDays(lastMonth));
	return formatDate(lastMonthEndDate);
}

// 获得本季度的开端日期
function getQuarterStartDate() {
	var quarterStartDate = new Date(nowYear, getQuarterStartMonth(), 1);
	return formatDate(quarterStartDate);
}

// 获得本季度的停止日期
function getQuarterEndDate() {
	var quarterEndMonth = getQuarterStartMonth() + 2;
	var quarterStartDate = new Date(nowYear, quarterEndMonth, getMonthDays(quarterEndMonth));
	return formatDate(quarterStartDate);
}

// 获得本年的开始日期
function getYearStartDate() {
	return new Date(nowYear, 0, 1)
}

// 获得本年的结束日期
function getYearEndDate() {
	return new Date(nowYear, 11, 31)
}

// 获取以当前日期为基础，偏移的日期，例如：昨天（-1），明天（1）
function getDay(offset) {
	var now = new Date();
	if (offset < 0) {
		now.setHours(0);
		now.setMinutes(0);
		now.setSeconds(0);
	} else {
		now.setHours(23);
		now.setMinutes(59);
		now.setSeconds(59);
	}
	return new Date(now.getTime() + 1000 * 60 * 60 * 24 * offset)
}
// 对工时字段（分钟）按#天#小时#分钟进行单位显示
function formatTimeForHours(ratedPeriod, unit) {
		if (Ext.isEmpty(ratedPeriod) || ratedPeriod == 0) {
		return "";
	}
	var ratedPeriod_h = null;										// 时
	var ratedPeriod_m = null;										// 分钟
	if ('m' == unit) { // 单位为分
		ratedPeriod_h = Math.floor(ratedPeriod/60);			// 时
		ratedPeriod_m = Math.floor(ratedPeriod%60);		// 分钟							
	} else if ('h' == unit) {
		ratedPeriod_h = Math.floor(ratedPeriod);					// 时
		ratedPeriod_m = 0;											// 分钟
	}
	var displayValue = "";

	if (ratedPeriod_h != 0) {
		displayValue += ratedPeriod_h + ":";
		if (ratedPeriod_m != 0) {
			displayValue += ratedPeriod_m + "";
		}else{ displayValue += "0"; }
	} else {
		if (ratedPeriod_m != 0) {
			displayValue += ratedPeriod_m + "";
		}else{ displayValue += "0"; }
	}

	return displayValue;
}

// 对工时字段（分钟）按#天#小时#分钟进行单位显示
/**
 * @param {} 时间值
 * @param {} 时间单位, 可选值[d(天), h(时), m(分钟), s(秒)]
 * @return {String} 时间显示样式
 */
function formatTime(ratedPeriod, unit) {
	if (Ext.isEmpty(ratedPeriod) || ratedPeriod == 0) {
		return "";
	}
	var ratedPeriod_d = null;										// 天
	var ratedPeriod_h = null;										// 时
	var ratedPeriod_m = null;										// 分钟
	if ('m' == unit) {
		ratedPeriod_d = Math.floor(ratedPeriod/(24*60));			// 天
		ratedPeriod_h = Math.floor((ratedPeriod%(24*60))/60);		// 时
		ratedPeriod_m = ratedPeriod%60;								// 分钟
	} else if ('h' == unit) {
		ratedPeriod_d = Math.floor(ratedPeriod/24);					// 天
		ratedPeriod_h = Math.floor(ratedPeriod%24);					// 时
		ratedPeriod_m = 0;											// 分钟
	}
	var displayValue = "";
	if (ratedPeriod_d != 0) {
		displayValue += ratedPeriod_d + "天";
		if (ratedPeriod_h != 0) {
			displayValue += ratedPeriod_h + "小时";
			if (ratedPeriod_m != 0) {
				displayValue += ratedPeriod_m + "分钟";
			}
		} else {
			if (ratedPeriod_m != 0) {
				displayValue += ratedPeriod_m + "分钟";
			}
		}
	} else {
		if (ratedPeriod_h != 0) {
			displayValue += ratedPeriod_h + "小时";
			if (ratedPeriod_m != 0) {
				displayValue += ratedPeriod_m + "分钟";
			}
		} else {
			if (ratedPeriod_m != 0) {
				displayValue += ratedPeriod_m + "分钟";
			}
		}
	}
	return displayValue;
}