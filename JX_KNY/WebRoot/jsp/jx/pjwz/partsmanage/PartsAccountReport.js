Ext.onReady(function(){
	Ext.namespace('PartsAccountReport');                       //定义命名空间
	/** 获取最近一年*/
	PartsAccountReport.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var currentDay = Nowdate.getDate();//获取当前日
		var MonthFirstDay=new Date(currentYear-1,currentMonth,currentDay);
		return MonthFirstDay.format('Y-m-d');
	}
	PartsAccountReport.labelWidth = 90;
	PartsAccountReport.fieldWidth = 140;
	//规格型号选择控件赋值函数
	PartsAccountReport.callReturnFn=function(node,e){
		PartsAccountReport.baseForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
		PartsAccountReport.baseForm.find("name","partsName")[0].setValue(node.attributes["partsName"]);
		PartsAccountReport.baseForm.find("name","partsTypeIDX")[0].setValue(node.attributes["id"]);
	}
	
	/* ------------------查询条件 -------------- */
	// -----------------第1列开始--------------- //
	// 周转台账分类
	var _turnOverStandBookType = {
		align:'center',	
		defaultType:'textfield', 
		border:true, 
		baseCls: "x-plain",
		layout:"form",
	    items: [
	    	{
       			xtype: 'combo',
	            fieldLabel: '周转台账分类',
	            width:PartsAccountReport.fieldWidth,
	            hiddenName:'type',
	            store:new Ext.data.SimpleStore({
				    fields: ['v', 't'],
   					data : [['1','自修配件'],['2','委外配件']]
				}),
				valueField:'v',
				displayField:'t',
				triggerAction:'all',
				mode:'local',
				value:'1',
				editable: false,
				allowBlank: false
			}
	   ]
	};
	// 状态修改日期-开始
	var _statusModifidDate = {
		xtype: 'compositefield', 
		fieldLabel : '状态修改日期', 
		combineErrors: false,
		items: [			
            {
	            id:"beginDate",
	            name: "beginDate", 
	            allowBlank: false, 
	            xtype: "my97date",
	            format: "Y-m-d",  
				value : PartsAccountReport.getCurrentMonth(),
				width: PartsAccountReport.fieldWidth
			}
		]
	}
	// 第一列包含两行
	var _column1 = {
		columnWidth: .33,
		layout:'form',
		items:[_turnOverStandBookType, _statusModifidDate]
	};
	// -----------------第1列结束--------------- //
	// -----------------第2列开始--------------- //
	// 配件规格型号
	var _partsStandardModel = {
		align:'center',	
		defaultType:'textfield', 
		border:true, 
		//baseCls: "x-plain",
		layout:"form",
	    items: [
	    	{
	         	xtype:"PartsTypeTreeSelect",
	         	fieldLabel: '配件规格型号',
	         	id:'PartsTypeTreeSelect_select',
	         	allowBlank:false,
				hiddenName: 'specificationModel', 
				editable:false,
				width:PartsAccountReport.fieldWidth,
				returnFn: PartsAccountReport.callReturnFn
	         }, { 
	         	id:"partsTypeIDX", 
	         	name:"partsTypeIDX", 
	         	fieldLabel:"规格型号id",
	         	hidden:true
	         }
	    ]
	};
	// 状态修改日期-结束
	var	_statusModifidDateEnd = {
		xtype: 'compositefield', 
		fieldLabel : '结束', 
		combineErrors: false,
		items: [			
            {
	            id:"endDate",
	            name: "endDate", 
	            allowBlank: false, 
	            xtype: "my97date",
	            format: "Y-m-d",  
				width: PartsAccountReport.fieldWidth
			}
		]
	};
	// 第2列包含3行--其中包含1个隐藏字段
	var _column2 = {
		columnWidth: .33,
		layout:'form',
		items:[_partsStandardModel, _statusModifidDateEnd]
	};
	// -----------------第2列结束--------------- //
	// -----------------第3列开始--------------- //
	// 配件名称
	var _partsName = {
		align:'center',	
		defaultType:'textfield', 
		border:true, 
		baseCls: "x-plain",
		layout:"form",
		items: [
			{ 
				id:"partsName", 
				name:"partsName", 
				fieldLabel:"配件名称",
				maxLength:100 ,
				disabled:true,
				width:PartsAccountReport.fieldWidth 
			}
	    ]
	};
	// 统计按钮
	var _btnStatistic = {
			xtype:'button',
			text:"统计",
			width:120, 
			handler:function(){
    			//表单验证是否通过
		        var form = PartsAccountReport.baseForm.getForm(); 
		        if (!form.isValid()) return;
				var url = "";
				var reportUrl = "";
   				var ctx_v = ctx.substring(1);
   				var data = form.getValues();
		        if(data.endDate < data.beginDate){
		        	MyExt.TopMsg.msg('提示',"状态修改结束日期不能开始日期小！", false, 1);
    				return false;
		        }
   				//自修配件
				if(data.type == '1'){
					reportUrl = "/pjwz/partsmanage/PartsAccountReportZX.cpt";
				}
				//委外配件
				if(data.type == '2'){
					reportUrl = "/pjwz/partsmanage/PartsAccountReportWW.cpt";
				}
				var dataUrl = reportUrl + "&beginDate="+data.beginDate+"&endDate="+data.endDate+"&partsTypeId="+data.partsTypeIDX+"&ctx="+ctx_v;								
//					if(!Ext.isEmpty(data.faultType))
//						dataUrl+= "&type=" + data.faultType;
				url = getReportEffectivePath(dataUrl);
	    		var h = jQuery("#report").height();
	    		document.getElementById("report").innerHTML = "<iframe style='width:100%;height:"+h+"px;overflow:auto;' frameborder='0' src='" + url + "'></iframe>";
			}
		};
	// 第3列包含两行
	var _column3 = {
		columnWidth:.33,
		layout:'form',
		items:[_partsName]
	};
	// -----------------第3列结束--------------- //
	
	// 统计查询条件表单区域
	PartsAccountReport.baseForm = new Ext.form.FormPanel({
	    baseCls: "x-plain", 
	    align: "center",	
	    defaultType: "textfield",	
	    defaults: { anchor: "99%" }, 
	    autoScroll:true,
	    border: false,		
	    style: "padding:15px 10px",		
	    labelWidth: PartsAccountReport.labelWidth,
		items:[
			{
				xtype: 'panel',	
				border:true,  
				layout:'column',	
				align:'center', 
				baseCls: "x-plain",
				items:[_column1, _column2, _column3]
			}
		],
		buttonAlign:'center',
		buttons:[_btnStatistic]
	});
	
	/*jx.pjwz.PartsTypeSelect.createWin = function(){
	    if(jx.pjwz.PartsTypeSelect.panel == null)  jx.pjwz.PartsTypeSelect.createPanels();
	    if(jx.pjwz.PartsTypeSelect.win == null){
		    jx.pjwz.PartsTypeSelect.win = new Ext.Window({
		        title:'规格型号选择', closeAction:"hide", width:300, height:500, layout:"fit", resizable:false, modal:true, 
	            items:[ new jx.pjwz.PartsTypeSelect.partsTypeTree()  ]
		    });
	    }
	}*/
	//页面自适应布局
	var viewport = new Ext.Viewport({ 
		layout: "border",
		items:[
			{
				region:"north",
				layout:"fit",
				height:145,
				split:true,
				frame: true,bodyBorder: false, 
				items:[PartsAccountReport.baseForm]
			},{
				id:"report",
				region : 'center', layout : 'fit', bodyBorder: false, 
				split:true,
				items:[]
			}
		]
	 });
});