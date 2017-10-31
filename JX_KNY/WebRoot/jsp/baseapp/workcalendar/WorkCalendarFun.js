Ext.onReady(function(){
	//定义命名空间
	Ext.namespace("workCalendar");

	var defaultZero = "00:00:00";
	//点击日历某天,查询其工作时间信息
	workCalendar.findWorkCalendarDetail = function (arg1,arg2){
		//去掉所有复选框的选中
		workCalendar.periodGroup("All",["",defaultZero,defaultZero,true,true]);

		Ext.Ajax.request({
			url: ctx + "/workCalendarDetail!getWorkCalendarDetail.action",
			
			params: {infoIdx : arg1, // 工作日历主表Idx
					 calDate : arg2},// 工作日历日期
					  
			success: function(response, options){
			       var result = Ext.util.JSON.decode(response.responseText);
			       if (result.errMsg == null) {
			       		if(result.entity!=null&&result.entity.idx!=null){
			       			//选中单选按钮
			       			var radioObj = document.getElementsByName("calendarSetUp");
			       			
			       			for(var i = 0; i<radioObj.length;i++){
			       				if(radioObj[i].value == result.entity.calDateType){
			       					radioObj[i].checked="checked";
			       				}
			       			}
			       			//主键等
			       			document.getElementById("idx").value = result.entity.idx;
			       			document.getElementById("infoIdx").value = result.entity.infoIdx;
	       				
			       			//根据内容, 判断哪些时间被勾选
			       			if(result.entity.period1End!="00:00:00"){
			       				var period1End = result.entity.period1End == TWENTYFOURTIME ? ZEROTIME : result.entity.period1End;
			       				workCalendar.periodGroup("period1",["checked",result.entity.period1Begin,period1End,false,false]);
			       			} else {
			       				workCalendar.periodGroup("period1",["",defaultZero,defaultZero,true,true]);
			       			}
			       			if(result.entity.period2Begin!="00:00:00"&&result.entity.period2End!="00:00:00"){
			       				var period2End = result.entity.period2End == TWENTYFOURTIME ? ZEROTIME : result.entity.period2End;
			       				workCalendar.periodGroup("period2",["checked",result.entity.period2Begin,period2End,false,false]);
			       			} else {
			       				workCalendar.periodGroup("period2",["",defaultZero,defaultZero,true,true]);
			       			}
			       			if(result.entity.period3Begin!="00:00:00"&&result.entity.period3End!="00:00:00"){
			       				var period3End = result.entity.period3End == TWENTYFOURTIME ? ZEROTIME : result.entity.period3End;
			       				workCalendar.periodGroup("period3",["checked",result.entity.period3Begin,period3End,false,false]);
			       			} else {
			       				workCalendar.periodGroup("period3",["",defaultZero,defaultZero,true,true]);
			       			}
			       			if(result.entity.period4Begin!="00:00:00"&&result.entity.period4End!="00:00:00"){
			       				var period4End = result.entity.period4End == TWENTYFOURTIME ? ZEROTIME : result.entity.period4End;
			       				workCalendar.periodGroup("period4",["checked",result.entity.period4Begin,period4End,false,false]);
			       			} else {
			       				workCalendar.periodGroup("period4",["",defaultZero,defaultZero,true,true]);
			       			}
			       		} else {
			       			//选中单选按钮
			       			var radioObj = document.getElementsByName("calendarSetUp");
			       			radioObj[0].checked="checked";
			       			workCalendar.typeOption();
			       			//清空主键
			       			document.getElementById("idx").value = "";
		       				
			       			workCalendar.periodGroup("All",["",defaultZero,defaultZero,true,true]);
			       		}
			       		//默认的工作日历信息
			       		if(result.defInfo!=null&&result.defInfo.idx!=null){
		       				var a = "<center>● "+result.defInfo.period1Begin+" ~ "+result.defInfo.period1End;
		       				a += "<br/>● "+result.defInfo.period2Begin+" ~ "+result.defInfo.period2End;
		       				a += "<br/>● "+result.defInfo.period3Begin+" ~ "+result.defInfo.period3End;
		       				a += "<br/>● "+result.defInfo.period4Begin+" ~ "+result.defInfo.period4End+"</center>";
			       			document.getElementById("showDefaultInfo").innerHTML = a;
			       		}
			       } else {
			              alertFail(result.errMsg);
			       }
			},
			failure: function(response, options){
			       MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	};
	
	//点击日历某天,查询其工作时间信息
	workCalendar.findCurrentMonthCalendar = function (yearAndMonth){
		var arg1 = infoIdx;
		Ext.Ajax.request({
			url: ctx + "/workCalendarDetail!getCurrentMonthCalendar.action",
			
			params: {infoIdx : arg1, // 工作日历主表Idx
					 yearAndMonth  : yearAndMonth},// 工作日历日期
					  
			success: function(response, options){
			       var result = Ext.util.JSON.decode(response.responseText);
			       if (result.errMsg == null) {
			       		if(result.entity!=null){
			       			workCalendar.colorShow(result.entity);
			       		} 
			       } else {
			              alertFail(result.errMsg);
			       }
			},
			failure: function(response, options){
			       MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	};
	
	workCalendar.colorShow = function (objAry) {
		for(var i=0;i<objAry.length;i++){
			var obj = objAry[i];
			var day = parseInt(obj.calDate.substring(6,7))>0?obj.calDate.substring(6,8):obj.calDate.substring(7,8);
			for(var j=0;j<42;j++){
			
				if(document.getElementById("SD"+j).innerText == day){
					switch (obj.calDateType)
					{
						case "0": //非默认工作日
							document.getElementById("SD"+j).style.color="blue";
						break;
						
						case "1": //非工作日
							document.getElementById("SD"+j).style.color="red";
						break;
						
						case "2": //默认工作日
							document.getElementById("SD"+j).style.color="black";
						break;
					}
				} 
			}
		}
	}
	
	workCalendar.validator = function (){
		//如果复选框被勾选,则时间范围必须填写
		if(Ext.get("period1").dom.checked == true){
			var bg = Ext.getCmp("period1Begin").value;
			var ed = Ext.getCmp("period1End").value;
			if( bg == null || bg == ""  ) {
				MyExt.Msg.alert("请正确填写第一项的开始时间");
				return false;
			}
			if( ed == null || ed == "" ) {
				MyExt.Msg.alert("请正确填写第一项的结束时间");
				return false;
			}
			if( (ed != "00:00:00" || bg != "00:00:00") && bg >= ed) {
				MyExt.Msg.alert("第一项的结束时间早于开始时间");
				return false;
			}
		} 
		if(Ext.get("period2").dom.checked == true){
			var bg = Ext.getCmp("period2Begin").value;
			var ed = Ext.getCmp("period2End").value;
			if( bg == null || bg == "" || bg == "00:00:00" ) {
				MyExt.Msg.alert("请正确填写第二项的开始时间");
				return false;
			}
			if( ed == null || ed == "" ) {
				MyExt.Msg.alert("请正确填写第二项的结束时间");
				return false;
			}
			if( ed != "00:00:00" && ed < bg) {
				MyExt.Msg.alert("第二项的结束时间早于开始时间");
				return false;
			}
		} 
		if(Ext.get("period3").dom.checked == true){
			var bg = Ext.getCmp("period3Begin").value;
			var ed = Ext.getCmp("period3End").value;
			if( bg == null || bg == "" || bg == "00:00:00" ) {
				MyExt.Msg.alert("请正确填写第三项的开始时间");
				return false;
			}
			if( ed == null || ed == ""  ) {
				MyExt.Msg.alert("请正确填写第三项的结束时间");
				return false;
			}
			if( ed != "00:00:00" && ed < bg) {
				MyExt.Msg.alert("第三项的结束时间早于开始时间");
				return false;
			}
		}
		if(Ext.get("period4").dom.checked == true){
			var bg = Ext.getCmp("period4Begin").value;
			var ed = Ext.getCmp("period4End").value;
			if( bg == null || bg == "" || bg == "00:00:00" ) {
				MyExt.Msg.alert("请正确填写第四项的开始时间");
				return false;
			}
			if( ed == null || ed == "" ) {
				MyExt.Msg.alert("请正确填写第四项的结束时间");
				return false;
			}
			if( ed != "00:00:00" && ed < bg) {
				MyExt.Msg.alert("第四项的结束时间早于开始时间");
				return false;
			}
		}
		var ed1 = Ext.getCmp("period1End").value;		
		var bg2 = Ext.getCmp("period2Begin").value;
		var ed2 = Ext.getCmp("period2End").value;
		var bg3 = Ext.getCmp("period3Begin").value;
		var ed3 = Ext.getCmp("period3End").value;
		var bg4 = Ext.getCmp("period4Begin").value;
		if(Ext.get("period2").dom.checked == true && bg2 < ed1){
			MyExt.Msg.alert("第二项的开始时间不能早于第一项的结束时间");
			return false;
		}
		if(Ext.get("period3").dom.checked == true && bg3 < ed2){
			MyExt.Msg.alert("第三项的开始时间不能早于第二项的结束时间");
			return false;
		}
		if(Ext.get("period4").dom.checked == true && bg4 < ed3){
			MyExt.Msg.alert("第四项的开始时间不能早于第三项的结束时间");
			return false;
		}
		if (Ext.get("period2").dom.checked == true && (bg2 != "00:00:00" && ed1 == "00:00:00")) {
			MyExt.Msg.alert("第二项的开始时间不能早于第一项的结束时间");
			return false;
		}
		if (Ext.get("period3").dom.checked == true && bg3 != "00:00:00") {
			if (ed2 == "00:00:00") {
				MyExt.Msg.alert("第三项的开始时间不能早于第二项的结束时间");
				return false;
			}
			if (ed1 == "00:00:00") {
				MyExt.Msg.alert("第三项的开始时间不能早于第一项的结束时间");
				return false;
			}
		}
		if (Ext.get("period4").dom.checked == true && bg4 != "00:00:00") {
			if (ed1 == "00:00:00") {
				MyExt.Msg.alert("第四项的开始时间不能早于第一项的结束时间");
				return false;
			}
			if (ed2 == "00:00:00") {
				MyExt.Msg.alert("第四项的开始时间不能早于第二项的结束时间");
				return false;
			}
			if (ed3 == "00:00:00") {
				MyExt.Msg.alert("第四项的开始时间不能早于第三项的结束时间");
				return false;
			}
		}
		return true;
	}
	
	workCalendar.validatorDefaultInfo = function (){
		var bg1 = Ext.getCmp("defPeriod1Begin").value; 
		var ed1 = Ext.getCmp("defPeriod1End").value;
		var bg2 = Ext.getCmp("defPeriod2Begin").value;
		var ed2 = Ext.getCmp("defPeriod2End").value;
		var bg3 = Ext.getCmp("defPeriod3Begin").value;
		var ed3 = Ext.getCmp("defPeriod3End").value;
		var bg4 = Ext.getCmp("defPeriod4Begin").value;
		var ed4 = Ext.getCmp("defPeriod4End").value;
		var calendarName = Ext.getCmp("calendarName").getValue();
		//判断工作日历名称不能为空
		if(calendarName == null || calendarName == "")   { MyExt.Msg.alert("工作日历名称不能为空");			return false; } 
		//首先判断第一组时间的任意一项不能为空,且开始时间必须早于且不等于结束时间
		if(bg1 == null || bg1 == "")   { MyExt.Msg.alert("请正确填写默认时段1的开始时间");			return false; } 
		if(ed1 == null || ed1 == "")   { MyExt.Msg.alert("请正确填写默认时段1的结束时间");			return false; } 
		if((ed1 != "00:00:00" || bg1 != "00:00:00") && bg1 >= ed1)  				{ MyExt.Msg.alert("默认时段1的结束时间不应早于或等于开始时间");	return false; }
		
		//判断第二组时间的任意一项不能为空,且开始时间必须早于且不等于结束时间
		if(bg2 == null || bg2 == "")   { MyExt.Msg.alert("请正确填写默认时段2的开始时间");			return false; } 
		if(ed2 == null || ed2 == "")   { MyExt.Msg.alert("请正确填写默认时段2的结束时间");			return false; } 
		if(ed2 != "00:00:00" && bg2 >= ed2)                 { MyExt.Msg.alert("默认时段2的结束时间不应早于或等于开始时间");	return false; }
		
		//判断第三组时间的任意一项不能为空,且开始时间必须早于且不等于结束时间
		if(bg3 == null || bg3 == "")   { MyExt.Msg.alert("请正确填写默认时段3的开始时间");			return false; } 
		if(ed3 == null || ed3 == "")   { MyExt.Msg.alert("请正确填写默认时段3的结束时间");			return false; } 
		if(ed3 != "00:00:00" && bg3 >= ed3)                 { MyExt.Msg.alert("默认时段3的结束时间不应早于或等于开始时间");	return false; }
		
		//判断第四组时间的任意一项不能为空,且开始时间必须早于且不等于结束时间
		if(bg4 == null || bg4 == "")   { MyExt.Msg.alert("请正确填写默认时段4的开始时间");			return false; } 
		if(ed4 == null || ed4 == "")   { MyExt.Msg.alert("请正确填写默认时段4的结束时间");			return false; } 
		if(ed4 != "00:00:00" && bg4 >= ed4)                 { MyExt.Msg.alert("默认时段4的结束时间不应早于或等于开始时间");	return false; }
		
		if(bg2 != "00:00:00" && bg2 < ed1){
			MyExt.Msg.alert("默认时段2的开始时间不能早于默认时段1的结束时间");
			return false;
		}
		if(bg3 != "00:00:00" && bg3 < ed2){
			MyExt.Msg.alert("默认时段3的开始时间不能早于默认时段2的结束时间");
			return false;
		}
		if(bg4 != "00:00:00" && bg4 < ed3){
			MyExt.Msg.alert("默认时段4的开始时间不能早于默认时段3的结束时间");
			return false;
		}
		if (bg2 != "00:00:00" && ed1 == "00:00:00") {
			MyExt.Msg.alert("默认时段2的开始时间不能早于默认时段1的结束时间");
			return false;
		}
		if (bg3 != "00:00:00") {
			if (ed2 == "00:00:00") {
				MyExt.Msg.alert("默认时段3的开始时间不能早于默认时段2的结束时间");
				return false;
			}
			if (ed1 == "00:00:00") {
				MyExt.Msg.alert("默认时段3的开始时间不能早于默认时段1的结束时间");
				return false;
			}
		}
		if (bg4 != "00:00:00") {
			if (ed1 == "00:00:00") {
				MyExt.Msg.alert("默认时段4的开始时间不能早于默认时段1的结束时间");
				return false;
			}
			if (ed2 == "00:00:00") {
				MyExt.Msg.alert("默认时段4的开始时间不能早于默认时段2的结束时间");
				return false;
			}
			if (ed3 == "00:00:00") {
				MyExt.Msg.alert("默认时段4的开始时间不能早于默认时段3的结束时间");
				return false;
			}
		}
		return true;
	}
	
	//新增或更新日历的工作时间
	workCalendar.saveOrUpdate = function (){
		var dataAry = new Array();
		var data = {};
		var radioObj = document.getElementsByName("calendarSetUp");
		var radioVal = "";
		for(var i = 0; i<radioObj.length;i++){
			if(radioObj[i].checked == true){
				radioVal = radioObj[i].value;
			}
		}
		data.idx          = document.getElementById("idx").value; 
//		data.infoIdx      = document.getElementById("infoIdx").value;
		data.infoIdx      = infoIdx ;
		data.calDate      = document.getElementById("currentDay").value2;
		data.calDateType  = radioVal;  /* 是否工作日 */
		//只有当选择为"非默认工作时间时,才有必要验证"
		if(radioVal == "0"){
			if(!workCalendar.validator()){return;} //未能通过验证
		}
		data.period1Begin = Ext.getCmp("period1Begin").value;	/* 开工时间一 */
		data.period1End   = Ext.getCmp("period1End").value;     /* 完工时间一 */
		data.period2Begin = Ext.getCmp("period2Begin").value;   /* 开工时间二 */
		data.period2End   = Ext.getCmp("period2End").value;		/* 完工时间二 */
		data.period3Begin = Ext.getCmp("period3Begin").value;   /* 开工时间三 */
		data.period3End   = Ext.getCmp("period3End").value;     /* 完工时间三 */
		data.period4Begin = Ext.getCmp("period4Begin").value;   /* 开工时间四 */
		data.period4End   = Ext.getCmp("period4End").value;      /* 完工时间四 */

		Ext.Ajax.request({
			url: ctx + "/workCalendarDetail!saveOrUpdate.action",
			jsonData: data,
			success: function(response, options){
			       var result = Ext.util.JSON.decode(response.responseText);
			       if (result.errMsg == null) {
			       		alertSuccess();
			       		if(result.entity!=null&&result.entity.idx!=null){
			       			document.getElementById("idx").value = result.entity.idx;
			       			document.getElementById("infoIdx").value = result.entity.infoIdx;
			       		}
			       		if(typeof LevTargetTdObj!="undefined"&&LevTargetTdObj!=null)
			       		document.getElementById(LevTargetTdObj.id).style.color="black";
						workCalendar.findCurrentMonthCalendar(data.calDate.substring(0,6));
			       } else {
			           alertFail(result.errMsg);
			       }
			},
			failure: function(response, options){
			       MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
		
	};


	/**
	 * 测试算法
	 */
	workCalendar.test = function(){
		Ext.Ajax.request({
	        url: ctx + "/workCalendarDetail!testGetWorkEndDate.action",
	       // params: {idx:'123'},
	       success: function(response, options){
			       var result = Ext.util.JSON.decode(response.responseText);
			       if (result.errMsg == null) {
			       		alert('成功!');
			       } else {
			              alertFail(result.errMsg);
			       }
			},
	        failure: function(response, options){
	            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	        }
	    }); 
	}
	/**
	 * 改变日历状态时, 触发
	 */
	workCalendar.typeOption = function (){
		var radioObj = document.getElementsByName("calendarSetUp");
		var radioVal = "";
		for(var i = 0; i<radioObj.length;i++){
			if(radioObj[i].checked == true){
				radioVal = radioObj[i].value;
			}
		}
		switch(radioVal)
		{
			//使用默认设置, 禁用所有时间文本框,并重置值为0
			case "2":
			    //workCalendar.colAnyElement("00:00:00",true);//禁用页面中的时间文本框,时间归零
			    workCalendar.periodGroup("All",["",defaultZero,defaultZero,true,true]);//禁用页面中的时间文本框,时间归零
			break;
			
			//非默认工作时间
			case "0":
//				workCalendar.periodGroup("period1",["checked","","",false,false]);
				//workCalendar.periodGroup("All",["checked","","",false,false]);
				workCalendar.chooseNoDefault();
			break;
			
			//非工作日
			case "1":
				workCalendar.periodGroup("All",["",defaultZero,defaultZero,true,true]);//禁用页面中的时间文本框,时间归零
			break;
		}
	}
	

	/**
	 * 复选框改变状态时触发
	 */
	workCalendar.changeCheckBox = function(){
		var radioObj = document.getElementsByName("calendarSetUp");
		var _name = event.srcElement.name;
		if(_name.substr(6,7)!=1){
			if(Ext.get("period"+(_name.substr(6,7)-1)).dom.checked == false){
				event.srcElement.checked = "";
				return;
			}
		} else {
			if(event.srcElement.checked == ""){
				radioObj[0].checked = "checked";
			} else {
				radioObj[2].checked = "checked";
			}
		}
		if(event.srcElement.checked == true){
			workCalendar.periodGroup(event.srcElement.name,["checked",defaultZero,defaultZero,false,false]);//启用选中的时间文本框,时间归零
		} else if(_name.substr(6,7)==1 && event.srcElement.checked == ""){
			workCalendar.periodGroup("All",["",defaultZero,defaultZero,true,true]);//禁用选中的时间文本框,时间归零
		} else{
			workCalendar.periodGroup(event.srcElement.name,["",defaultZero,defaultZero,true,true]);//禁用选中的时间文本框,时间归零
		}
	}
	
	//第一组时间
	workCalendar.periodGroup = function(target,args){

		if( target == "period1" || target == "All") {
			document.getElementById("period1").checked=args[0];
			if(args[1]!="")
				Ext.getCmp("period1Begin").setValue(args[1]);//document.getElementById("period1Begin").value = args[1];   
			if(args[2]!="")
				Ext.getCmp("period1End").setValue(args[2]);  //document.getElementById("period1End").value = args[2];		
			Ext.getCmp("period1Begin").setDisabled(args[3]);
			Ext.getCmp("period1End").setDisabled(args[4]);
		} 
		if( target == "period2" || target == "All") {
			document.getElementById("period2").checked=args[0];
			if(args[1]!="")
				Ext.getCmp("period2Begin").setValue(args[1]); //document.getElementById("period2Begin").value = args[1]; 
			if(args[2]!="")
				Ext.getCmp("period2End").setValue(args[2]);   //document.getElementById("period2End").value = args[2];		
			Ext.getCmp("period2Begin").setDisabled(args[3]);
			Ext.getCmp("period2End").setDisabled(args[4]);
		}
		if( target == "period3" || target == "All") {
			document.getElementById("period3").checked=args[0];
			if(args[1]!="")
				Ext.getCmp("period3Begin").setValue(args[1]); //document.getElementById("period3Begin").value = args[1]; 
			if(args[2]!="")
				Ext.getCmp("period3End").setValue(args[2]);   //document.getElementById("period3End").value = args[2];		
			Ext.getCmp("period3Begin").setDisabled(args[3]);
			Ext.getCmp("period3End").setDisabled(args[4]);
		} 
		if( target == "period4" || target == "All") {
			document.getElementById("period4").checked=args[0];
			if(args[1]!="")
				Ext.getCmp("period4Begin").setValue(args[1]); //document.getElementById("period4Begin").value = args[1];
			if(args[2]!="")
				Ext.getCmp("period4End").setValue(args[2]);   //document.getElementById("period4End").value = args[2];		
			Ext.getCmp("period4Begin").setDisabled(args[3]);
			Ext.getCmp("period4End").setDisabled(args[4]);
		} 
	}
	var _y = document.getElementById("choYear").value;
	var _m = document.getElementById("choMonth").value;
	if(parseInt(_m)<10) _m = 0+String(_m);
	workCalendar.findCurrentMonthCalendar(_y+_m);
	
	//点击日历面板中的日期单元格时触发
	workCalendar.getChooseDay = function(obj){
		targetTdObj = obj;
		if(LevTargetTdObj!=null&&LevTargetTdObj!=""){
			if(LevTargetTdObj.style.background == "yellow"){
				LevTargetTdObj.style.backgroundColor="transparent";
			}
		}
		/**
		 *  如果当前点击的单元格在当月日期之外,则禁止表单填报操作
		 */
		if(obj.innerText == ""){
			return;
		}
		obj.style.background = "yellow";
		var _y = document.getElementById("choYear").value;
		var _m = document.getElementById("choMonth").value;
		var _d = obj.childNodes[0].innerText; //日历表格的每个单元格中存在多个元素, 例如数字的日期和农历的表述, 这里只取数字日期, 所以取childNodes[0]
		if(_y != "" && _m != "" && _d != ""){
			document.getElementById("currentDay").value = _y+"年"+_m+"月"+_d+"日";
			var _a = formatDay(_y,_m,_d);
			document.getElementById("currentDay").value2 = _a;
			workCalendar.findWorkCalendarDetail(infoIdx,_a);
		}
		LevTargetTdObj = targetTdObj;
	}
	
	workCalendar.timefieldwidth = 80;//下拉宽度
	workCalendar.timefieldformat = 'H:i:s';//格式
	workCalendar.timefieldminValue = '00:00';//最小时间
	workCalendar.timefieldmaxValue = '23:50';//最小时间
	workCalendar.timefieldincrement = 30;//递增步长
	workCalendar.timefieldDefault = '00:00:00';
	//第一组时间组件
	workCalendar.period1Begin = new Ext.form.TimeField({
		id:'period1Begin',  format: workCalendar.timefieldformat,  minValue: workCalendar.timefieldminValue, value:workCalendar.timefieldDefault,
    	maxValue: workCalendar.timefieldmaxValue, width:workCalendar.timefieldwidth, increment: workCalendar.timefieldincrement
	});
	workCalendar.period1End = new Ext.form.TimeField({
		id:'period1End',  format: workCalendar.timefieldformat,  minValue: workCalendar.timefieldminValue, value:workCalendar.timefieldDefault,
    	maxValue: workCalendar.timefieldmaxValue, width:workCalendar.timefieldwidth, increment: workCalendar.timefieldincrement
	});
	//第二组时间组件
	workCalendar.period2Begin = new Ext.form.TimeField({
		id:'period2Begin',  format: workCalendar.timefieldformat,  minValue: workCalendar.timefieldminValue, value:workCalendar.timefieldDefault,
    	maxValue: workCalendar.timefieldmaxValue, width:workCalendar.timefieldwidth, increment: workCalendar.timefieldincrement
	});
	workCalendar.period2End = new Ext.form.TimeField({
		id:'period2End',  format: workCalendar.timefieldformat,  minValue: workCalendar.timefieldminValue, value:workCalendar.timefieldDefault,
    	maxValue: workCalendar.timefieldmaxValue, width:workCalendar.timefieldwidth, increment: workCalendar.timefieldincrement
	});
	//第三组时间组件
	workCalendar.period3Begin = new Ext.form.TimeField({
		id:'period3Begin',  format: workCalendar.timefieldformat,  minValue: workCalendar.timefieldminValue, value:workCalendar.timefieldDefault,
    	maxValue: workCalendar.timefieldmaxValue, width:workCalendar.timefieldwidth, increment: workCalendar.timefieldincrement
	});
	workCalendar.period3End = new Ext.form.TimeField({
		id:'period3End',  format: workCalendar.timefieldformat,  minValue: workCalendar.timefieldminValue, value:workCalendar.timefieldDefault,
    	maxValue: workCalendar.timefieldmaxValue, width:workCalendar.timefieldwidth, increment: workCalendar.timefieldincrement
	});
	//第四组时间组件
	workCalendar.period4Begin = new Ext.form.TimeField({
		id:'period4Begin',  format: workCalendar.timefieldformat,  minValue: workCalendar.timefieldminValue, value:workCalendar.timefieldDefault,
    	maxValue: workCalendar.timefieldmaxValue, width:workCalendar.timefieldwidth, increment: workCalendar.timefieldincrement
	});
	workCalendar.period4End = new Ext.form.TimeField({
		id:'period4End',  format: workCalendar.timefieldformat,  minValue: workCalendar.timefieldminValue, value:workCalendar.timefieldDefault,
    	maxValue: workCalendar.timefieldmaxValue, width:workCalendar.timefieldwidth, increment: workCalendar.timefieldincrement
	});
	workCalendar.period1Begin.render(Ext.get("period1BeginDiv"));
	workCalendar.period1End.render(Ext.get("period1EndDiv"));
	workCalendar.period2Begin.render(Ext.get("period2BeginDiv"));
	workCalendar.period2End.render(Ext.get("period2EndDiv"));
	workCalendar.period3Begin.render(Ext.get("period3BeginDiv"));
	workCalendar.period3End.render(Ext.get("period3EndDiv"));
	workCalendar.period4Begin.render(Ext.get("period4BeginDiv"));
	workCalendar.period4End.render(Ext.get("period4EndDiv"));
	//页面显示时,自动查询本日的工作时间安排,并显示在表单中
	workCalendar.findWorkCalendarDetail(infoIdx,formatDay(myDate.getFullYear(),(myDate.getMonth()+1),myDate.getDate()));
	
	/**
	 * 修改默认工作时间页面
	 */
	workCalendar.fieldWidth = 180;
	workCalendar.anchor = '95%';
	workCalendar.defaultForm = new Ext.form.FormPanel({
		layout:"form", border:true, labelAlign: "right",style:"padding:10px",
		labelWidth: workCalendar.labelWidth, align:'center',baseCls: "x-plain",
		defaultType:'textfield',defaults:{anchor:"98%"}, 
		items:[{
			xtype: 'panel',	border:true,  layout:'column',	align:'center', baseCls: "x-plain",
			items:[{
				align:'center',	defaultType:'textfield', border:false, baseCls: "x-plain",layout:"form",
				labelWidth: workCalendar.labelWidth,	columnWidth:1.0,	defaults:{anchor:workCalendar.anchor},
				items:[
					{ id:"idx",fieldLabel:'idx主键', name:'idx',hidden:true,width:workCalendar.fieldWidth},
					{ id:"calendarName",fieldLabel:'工作日历名称',name:'calendarName',allowBlank: false,maxLength:32,width:190}
				]
			}]
		},{
			xtype: 'panel',	border:true,  layout:'column',	align:'center', baseCls: "x-plain",
			items:[{
				align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
				labelWidth: workCalendar.labelWidth,	columnWidth:0.5,	defaults:{anchor:workCalendar.anchor},
				items:[
					{ id:"defPeriod1Begin",xtype:"timefield",fieldLabel:'默认时段1-始',name:'defPeriod1Begin',
					  format: workCalendar.timefieldformat,  minValue: workCalendar.timefieldminValue, 
					  value:workCalendar.timefieldDefault,maxValue: workCalendar.timefieldmaxValue, 
					  width:workCalendar.timefieldwidth, increment: workCalendar.timefieldincrement},
					{ id:"defPeriod2Begin",xtype:"timefield",fieldLabel:'默认时段2-始',name:'defPeriod2Begin',
  					  format: workCalendar.timefieldformat,  minValue: workCalendar.timefieldminValue, 
					  value:workCalendar.timefieldDefault,maxValue: workCalendar.timefieldmaxValue, 
					  width:workCalendar.timefieldwidth, increment: workCalendar.timefieldincrement},
					{ id:"defPeriod3Begin",xtype:"timefield",fieldLabel:'默认时段3-始',name:'defPeriod3Begin',
					  format: workCalendar.timefieldformat,  minValue: workCalendar.timefieldminValue, 
					  value:workCalendar.timefieldDefault,maxValue: workCalendar.timefieldmaxValue, 
					  width:workCalendar.timefieldwidth, increment: workCalendar.timefieldincrement},
					{ id:"defPeriod4Begin",xtype:"timefield",fieldLabel:'默认时段4-始',name:'defPeriod4Begin',
					  format: workCalendar.timefieldformat,  minValue: workCalendar.timefieldminValue, 
					  value:workCalendar.timefieldDefault,maxValue: workCalendar.timefieldmaxValue, 
					  width:workCalendar.timefieldwidth, increment: workCalendar.timefieldincrement} 
				]
			},{
				align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
				labelWidth: workCalendar.labelWidth,	columnWidth:0.5,	defaults:{anchor:workCalendar.anchor},
				items:[
					{ id:"defPeriod1End",xtype:"timefield",fieldLabel:'默认时段1-终',name:'defPeriod1End',
					  format: workCalendar.timefieldformat,  minValue: workCalendar.timefieldminValue, 
					  value:workCalendar.timefieldDefault,maxValue: workCalendar.timefieldmaxValue, 
					  width:workCalendar.timefieldwidth, increment: workCalendar.timefieldincrement},
					{ id:"defPeriod2End",xtype:"timefield",fieldLabel:'默认时段2-终',name:'defPeriod2End',
  					  format: workCalendar.timefieldformat,  minValue: workCalendar.timefieldminValue, 
					  value:workCalendar.timefieldDefault,maxValue: workCalendar.timefieldmaxValue, 
					  width:workCalendar.timefieldwidth, increment: workCalendar.timefieldincrement},
					{ id:"defPeriod3End",xtype:"timefield",fieldLabel:'默认时段3-终',name:'defPeriod3End',
					  format: workCalendar.timefieldformat,  minValue: workCalendar.timefieldminValue, 
					  value:workCalendar.timefieldDefault,maxValue: workCalendar.timefieldmaxValue, 
					  width:workCalendar.timefieldwidth, increment: workCalendar.timefieldincrement},
					{ id:"defPeriod4End",xtype:"timefield",fieldLabel:'默认时段4-终',name:'defPeriod4End',
					  format: workCalendar.timefieldformat,  minValue: workCalendar.timefieldminValue, 
					  value:workCalendar.timefieldDefault,maxValue: workCalendar.timefieldmaxValue, 
					  width:workCalendar.timefieldwidth, increment: workCalendar.timefieldincrement}
				]
			}]
		},{
			xtype: 'panel',	border:true,  layout:'column',	align:'center', baseCls: "x-plain",
			items:[{
				align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
				labelWidth: workCalendar.labelWidth,	columnWidth:1.0,	defaults:{anchor:workCalendar.anchor},
				items:[
					{ id:"remarks",fieldLabel:'备注',name:'remarks',
					xtype:'textarea', height:80, maxLength:400,
					disabled:false,width:workCalendar.fieldWidth}
				]
			}]
		}]
	});
	/**
	 * 设置默认工作时间win
	 */
	workCalendar.defaultWin = new Ext.Window({
		title: "设置默认工作时间", maximizable:false, width:480, height:305, layout: "fit", 
		closeAction: "hide", modal: true, buttonAlign:"center",
		items: [workCalendar.defaultForm],
		buttons: [{
		    id:"saveInfo",text: "保存", iconCls:"saveIcon",handler:function(){
		    	var data = {};
				if(!workCalendar.validatorDefaultInfo()){return;} //未能通过验证

				data.idx          = workCalendar.defaultForm.getForm().findField("idx").getValue();  //日历Idx
				data.calendarName = workCalendar.defaultForm.getForm().findField("calendarName").getValue(); //日历名称
				data.period1Begin = workCalendar.defaultForm.getForm().findField("defPeriod1Begin").getValue();	/* 开工时间一 */
				data.period1End   = workCalendar.defaultForm.getForm().findField("defPeriod1End").getValue();     /* 完工时间一 */
				data.period2Begin = workCalendar.defaultForm.getForm().findField("defPeriod2Begin").getValue();   /* 开工时间二 */
				data.period2End   = workCalendar.defaultForm.getForm().findField("defPeriod2End").getValue();		/* 完工时间二 */
				data.period3Begin = workCalendar.defaultForm.getForm().findField("defPeriod3Begin").getValue();   /* 开工时间三 */
				data.period3End   = workCalendar.defaultForm.getForm().findField("defPeriod3End").getValue();     /* 完工时间三 */
				data.period4Begin = workCalendar.defaultForm.getForm().findField("defPeriod4Begin").getValue();   /* 开工时间四 */
				data.period4End   = workCalendar.defaultForm.getForm().findField("defPeriod4End").getValue();      /* 完工时间四 */
				data.remark       = workCalendar.defaultForm.getForm().findField("remarks").getValue(); //备注
				data.isDefault    = "0";

				Ext.Ajax.request({
					url: ctx + "/workCalendarInfo!saveOrUpdate.action",
					jsonData: data,
					success: function(response, options){
					       var result = Ext.util.JSON.decode(response.responseText);
					       if (result.errMsg == null) {
					       		alertSuccess();
					       		//默认的工作日历信息
					       		if(result.entity!=null&&result.entity.idx!=null){
				       				var a = "<center>● "+result.entity.period1Begin+" ~ "+result.entity.period1End;
				       				a += "<br/>● "+result.entity.period2Begin+" ~ "+result.entity.period2End;
				       				a += "<br/>● "+result.entity.period3Begin+" ~ "+result.entity.period3End;
				       				a += "<br/>● "+result.entity.period4Begin+" ~ "+result.entity.period4End+"</center>";
					       			document.getElementById("showDefaultInfo").innerHTML = a;
					       		}
					       } else {
					           alertFail(result.errMsg);
					       }
					},
					failure: function(response, options){
					       MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
					}
				});
		    }
		},{
			text: "关闭", iconCls:"closeIcon", handler:function(){
				workCalendar.defaultWin.hide();
			}
		}]
	});
	
	workCalendar.showWin = function (){
		workCalendar.defaultWin.show();
		Ext.Ajax.request({
					url: ctx + "/workCalendarInfo!getWorkCalendarInfo.action",
					params: {infoIdx:infoIdx},
					success: function(response, options){
					     var result = Ext.util.JSON.decode(response.responseText);
			       			if (result.errMsg == null) {
					       		if(result.defInfo!=null&&result.defInfo.idx!=null){
					       			workCalendar.defaultForm.getForm().findField("idx").setValue(result.defInfo.idx);
					       			workCalendar.defaultForm.getForm().findField("calendarName").setValue(result.defInfo.calendarName);
					       			workCalendar.defaultForm.getForm().findField("defPeriod1Begin").setValue(result.defInfo.period1Begin);
					       			workCalendar.defaultForm.getForm().findField("defPeriod1End").setValue(result.defInfo.period1End);
					       			workCalendar.defaultForm.getForm().findField("defPeriod2Begin").setValue(result.defInfo.period2Begin);
					       			workCalendar.defaultForm.getForm().findField("defPeriod2End").setValue(result.defInfo.period2End);
					       			workCalendar.defaultForm.getForm().findField("defPeriod3Begin").setValue(result.defInfo.period3Begin);
					       			workCalendar.defaultForm.getForm().findField("defPeriod3End").setValue(result.defInfo.period3End);
					       			workCalendar.defaultForm.getForm().findField("defPeriod4Begin").setValue(result.defInfo.period4Begin);
					       			workCalendar.defaultForm.getForm().findField("defPeriod4End").setValue(result.defInfo.period4End);
					       			workCalendar.defaultForm.getForm().findField("remarks").setValue(result.defInfo.remark);
					       		}
					       } else {
					           alertFail(result.errMsg);
					       }
					},
					failure: function(response, options){
					       MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
					}
				});
//				if(isDefault == '1'){
//					Ext.getCmp("saveInfo").disable();
//				}else{
//					Ext.getCmp("saveInfo").enable();
//				}
	}
	
	workCalendar.chooseNoDefault = function (){
		Ext.Ajax.request({
					url: ctx + "/workCalendarInfo!getWorkCalendarInfo.action",
					params: {infoIdx:infoIdx},
					success: function(response, options){
					     var result = Ext.util.JSON.decode(response.responseText);
			       			if (result.errMsg == null) {
					       		if(result.defInfo!=null&&result.defInfo.idx!=null){
		       			
					       			//根据内容, 判断哪些时间被勾选
					       			if(result.defInfo.period1Begin!="00:00:00" || result.defInfo.period1End!="00:00:00"){
					       				workCalendar.periodGroup("period1",["checked",result.defInfo.period1Begin,result.defInfo.period1End,false,false]);
					       			} else {
					       				workCalendar.periodGroup("period1",["",defaultZero,defaultZero,true,true]);
					       			}
					       			if(result.defInfo.period2Begin!="00:00:00" || result.defInfo.period2End!="00:00:00"){
					       				workCalendar.periodGroup("period2",["checked",result.defInfo.period2Begin,result.defInfo.period2End,false,false]);
					       			} else {
					       				workCalendar.periodGroup("period2",["",defaultZero,defaultZero,true,true]);
					       			}
					       			if(result.defInfo.period3Begin!="00:00:00" || result.defInfo.period3End!="00:00:00"){
					       				workCalendar.periodGroup("period3",["checked",result.defInfo.period3Begin,result.defInfo.period3End,false,false]);
					       			} else {
					       				workCalendar.periodGroup("period3",["",defaultZero,defaultZero,true,true]);
					       			}
					       			if(result.defInfo.period4Begin!="00:00:00" || result.defInfo.period4End!="00:00:00"){
					       				workCalendar.periodGroup("period4",["checked",result.defInfo.period4Begin,result.defInfo.period4End,false,false]);
					       			} else {
					       				workCalendar.periodGroup("period4",["",defaultZero,defaultZero,true,true]);
					       			}
					       		}
					       } else {
					           alertFail(result.errMsg);
					       }
					},
					failure: function(response, options){
					       MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
					}
				});
	}
	
	
	// 批量设置工作日Form
	workCalendar.VolumeTypeForm = new Ext.form.FormPanel({
		layout:"form", border:true, labelAlign: "right",style:"padding:10px",
		labelWidth: workCalendar.labelWidth, align:'center',baseCls: "x-plain",
		defaultType:'textfield',defaults:{anchor:"98%"}, 
		items:[{
			xtype: 'panel',	border:true,  layout:'column',	align:'center', baseCls: "x-plain",
			items:[{
				align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
				labelWidth: workCalendar.labelWidth,	columnWidth:0.5,	defaults:{anchor:workCalendar.anchor},
				items:[
					{ id:"volumeStart", xtype:'my97date',fieldLabel:'开始时间',format:'yyyy-MM-dd', my97cfg: {dateFmt:'yyyy-MM-dd'}, allowBlank: false }
				]
			},{
				align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
				labelWidth: workCalendar.labelWidth,	columnWidth:0.5,	defaults:{anchor:workCalendar.anchor},
				items:[
					{ id:"volumeEnd", xtype:'my97date',fieldLabel:'结束时间', my97cfg: {dateFmt:'yyyy-MM-dd'}, allowBlank: false }
				]
			}]
		}]
	});

	// 批量设置工作日窗口
	workCalendar.VolumeTypeWin = new Ext.Window({
		title: "批量设置工作日历", maximizable:false, width:480, height:195, layout: "fit", 
		closeAction: "hide", modal: true, buttonAlign:"center",
		items: [workCalendar.VolumeTypeForm],
		buttons: [{
		    text: "保存", iconCls:"saveIcon",handler:function(){
				if(!workCalendar.VolumeTypeForm.getForm().isValid()){return;} //未能通过验证
				
				var data = {};
				var radioObj = document.getElementsByName("calendarSetUp");
				var radioVal = "";
				for(var i = 0; i<radioObj.length;i++){
					if(radioObj[i].checked == true){
						radioVal = radioObj[i].value;
					}
				}
				
				data.idx          = document.getElementById("idx").value; 
		//		data.infoIdx      = document.getElementById("infoIdx").value;
				data.infoIdx      = infoIdx ;
				data.calDate      = document.getElementById("currentDay").value2;
				data.calDateType  = radioVal;  /* 是否工作日 */
				//只有当选择为"非默认工作时间时,才有必要验证"
				if(radioVal == "0"){
					if(!workCalendar.validator()){return;} //未能通过验证
				}
				data.period1Begin = Ext.getCmp("period1Begin").value;	/* 开工时间一 */
				data.period1End   = Ext.getCmp("period1End").value;     /* 完工时间一 */
				data.period2Begin = Ext.getCmp("period2Begin").value;   /* 开工时间二 */
				data.period2End   = Ext.getCmp("period2End").value;		/* 完工时间二 */
				data.period3Begin = Ext.getCmp("period3Begin").value;   /* 开工时间三 */
				data.period3End   = Ext.getCmp("period3End").value;     /* 完工时间三 */
				data.period4Begin = Ext.getCmp("period4Begin").value;   /* 开工时间四 */
				data.period4End   = Ext.getCmp("period4End").value;      /* 完工时间四 */
				
	    		var volumeStart = Ext.getCmp('volumeStart').getValue();
	    		var volumeEnd = Ext.getCmp('volumeEnd').getValue();
				Ext.Ajax.request({
					url: ctx + "/workCalendarDetail!setVolumeType.action",
					jsonData: data,
					params: {
						volumeStart  : volumeStart,
						volumeEnd : volumeEnd
					},
					success: function(response, options){
					       var result = Ext.util.JSON.decode(response.responseText);
					       if (result.errMsg == null) {
					       		alertSuccess();
					       		workCalendar.findCurrentMonthCalendar(data.calDate.substring(0,6));
					       } else {
					           alertFail(result.errMsg);
					       }
					},
					failure: function(response, options){
					       MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
					}
				});
		    }
		},{
			text: "关闭", iconCls:"closeIcon", handler:function(){
				workCalendar.VolumeTypeWin.hide();
			}
		}]
	});
	
	workCalendar.showVolumeTypeWin = function(){
		workCalendar.VolumeTypeWin.show();
	}
});