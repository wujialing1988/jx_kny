Ext.onReady(function(){
	Ext.namespace('FaultNoticeTimes');                       //定义命名空间
	/** 获取当前月份的第一天和最后一天*/
	FaultNoticeTimes.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var MonthFirstDay=new Date(currentYear,currentMonth,1);
		var MonthNextFirstDay=new Date(currentYear,currentMonth+1,1);
	 	var MonthLastDay=new Date(MonthNextFirstDay-86400000);
	
		if(arg == 'begin'){
			return MonthFirstDay.format('Y-m-d');
		}
		else if (arg == 'end'){
			return MonthLastDay.format('Y-m-d');
		}
	}
	FaultNoticeTimes.labelWidth = 120;
	FaultNoticeTimes.fieldWidth = 130;
	FaultNoticeTimes.baseForm = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "99%" }, autoScroll:true,
	    layout: "form",		border: false,		style: "padding:10px",		labelWidth: FaultNoticeTimes.labelWidth, buttonAlign: "center",
	    items: [{
	        xtype: 'panel',	border:true,  layout:'column',	align:'center', baseCls: "x-plain",
	        items: [
	        {
	            align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
				labelWidth: FaultNoticeTimes.labelWidth,
	            columnWidth: 0.33, 
	            items: [{
	        			xtype: 'combo',
			            fieldLabel: '统计方式',
			            width:FaultNoticeTimes.fieldWidth,
			            hiddenName:'type',
			            store:new Ext.data.SimpleStore({
						    fields: ['v', 't'],
	    					data : [['1','同车型同位置'],['2','同车同位置']]
						}),
						valueField:'v',
						displayField:'t',
						triggerAction:'all',
						mode:'local',
						value:'1',
						editable: false,
						allowBlank: false
					},{
						id:"beginDate", fieldLabel: '故障发现日期(开始)', xtype: "my97date", my97cfg: {dateFmt:"yyyy-MM-dd"}, initNow: false,
						value: FaultNoticeTimes.getCurrentMonth('begin'), width:FaultNoticeTimes.fieldWidth, allowBlank: false
					},{
						fieldLabel: '提票类型',
						xtype: 'EosDictEntry_combo',
						hiddenName: 'faultType',
						dicttypeid:'JCZL_FAULT_TYPE',
						displayField:'dictname',valueField:'dictname',
						hasEmpty: 'false',
						width:100
	            	}
	            ]
	        },{
	           align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
				labelWidth: FaultNoticeTimes.labelWidth,
	            columnWidth: 0.33, 
	            items: [{
	            		xtype: 'radiogroup',
			            fieldLabel: '是否同故障现象',
			            name: 'isSameFault',
			            width:FaultNoticeTimes.fieldWidth,
			            allowBlank: false,
			            items: [
			                { boxLabel: '是',inputValue: '0', name: 'isSameFault' },
			                { boxLabel: '否',inputValue: '1', name: 'isSameFault', checked:true }
			            ]

	            	},
	            	{
						id:"endDate", fieldLabel: '故障发现日期(结束)', xtype: "my97date", my97cfg: {dateFmt:"yyyy-MM-dd"}, initNow: false,
						value: FaultNoticeTimes.getCurrentMonth('end'), width:FaultNoticeTimes.fieldWidth, allowBlank: false
					},{ 
						id:"trainNo_comb",xtype: "TrainNo_combo",	fieldLabel: "车号",
					    hiddenName: "trainNo",width:100,
					    displayField: "trainNo", valueField: "trainNo",
					    pageSize: 20, minListWidth: 200, 
					    editable:true
					  }
	            ]
	        },{
	           align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
				labelWidth: FaultNoticeTimes.labelWidth,
	            columnWidth: 0.33, 
	            items: [{
	            		fieldLabel: '最小发生次数', name: 'leastTimes', allowBlank: false, 
	            		value: 2, xtype: 'numberfield', vtype: "positiveInt", width: 60
	            	},{
	            		xtype: "Base_combo",
			        	business: 'trainType',													
			        	entity:'com.yunda.jx.base.jcgy.entity.TrainType',
						fields:['typeID','shortName'],
						queryParams: {'isCx':'yes'}, 
						fieldLabel: "车型",
						hiddenName: "trainTypeIDX",
						displayField: "shortName", valueField: "typeID",
						pageSize: 20, minListWidth: 60,
						forceSelection: true, editable: true,
						width:100,
						listeners : {   
						        	"select" : function() {   
						            	//重新加载车号下拉数据
						                var trainNo_comb = Ext.getCmp("trainNo_comb");   
						                trainNo_comb.reset();  
						                trainNo_comb.clearValue(); 
						                trainNo_comb.queryParams = {"trainTypeIDX":this.getValue()};
						                trainNo_comb.cascadeStore();
						        	}   
						    	}
	            	},{ xtype: 'hidden'}
	            ]
	        }]
	    }],
	    buttons: [{
	    	text:"统计",width:100, 
    		handler:function(){
    			//表单验证是否通过
		        var form = FaultNoticeTimes.baseForm.getForm(); 
		        if (!form.isValid()) return;
		        var beginDate = Ext.getCmp("beginDate").getValue();
		        var endDate = Ext.getCmp("endDate").getValue();
		        if(beginDate == ""){
		        	MyExt.TopMsg.msg('提示',"故障发现日期(开始)不能为空！", true, 1);
    				return false;
		        }
		         if(endDate == ""){
		        	MyExt.TopMsg.msg('提示',"故障发现日期(结束)不能为空！", true, 1);
    				return false;
		        }
		        var data = form.getValues();
		        if(data.endDate < data.beginDate){
		        	MyExt.TopMsg.msg('提示',"故障发现结束日期不能比故障发现日期开始日期小！", false, 1);
    				return false;
		        }
				var url = "";
				var reportUrl = "/jczl/faultAnalysis/faultTicketTimes1.cpt";
				var ctx_v = ctx.substring(1);
				//同车型同位置不同故障现象
				if(data.type == '1' && data.isSameFault == '1'){
					reportUrl = "/jczl/faultAnalysis/faultTicketTimes1.cpt";
				}
				//同车型同位置同故障现象
				if(data.type == '1' && data.isSameFault == '0'){
					reportUrl = "/jczl/faultAnalysis/faultTicketTimes2.cpt";
				}
				//同车同位置不同故障现象
				if(data.type == '2' && data.isSameFault == '1'){
					reportUrl = "/jczl/faultAnalysis/faultTicketTimes3.cpt";
				}
				//同车同位置同故障现象
				if(data.type == '2' && data.isSameFault == '0'){
					reportUrl = "/jczl/faultAnalysis/faultTicketTimes4.cpt";
				}
				var dataUrl = reportUrl + "&count="+data.leastTimes+"&beginDate="+data.beginDate+"&endDate="+data.endDate+"&ctx="+ctx_v;								
				if(!Ext.isEmpty(data.faultType))
					dataUrl+= "&type=" + data.faultType;
				if(!Ext.isEmpty(data.trainTypeIDX))
					dataUrl+= "&trainTypeIDX="+data.trainTypeIDX;
				if(!Ext.isEmpty(data.trainNo))
					dataUrl+= "&trainNo="+data.trainNo;
				url = getReportEffectivePath(dataUrl);
	    		var h = jQuery("#report").height();
				document.getElementById("report").innerHTML = "<iframe style='width:100%;height:"+h+"px;overflow:auto;' frameborder='0' src='" + url + "'></iframe>";
			}
	    },{
	    	text:"重置",width:100,
			handler: function(){
//				FaultNoticeTimes.baseForm.getForm().findField("leastTimes").setValue("");
				FaultNoticeTimes.baseForm.getForm().findField("faultType").clearValue();
				FaultNoticeTimes.baseForm.getForm().findField("trainTypeIDX").clearValue();
				FaultNoticeTimes.baseForm.getForm().findField("trainNo").clearValue();
			}
	    }]
	});
	//页面自适应布局
	var viewport = new Ext.Viewport({ 
		 layout: "border",
		items:[{
					region:"north",
					layout:"fit",
					height:160,
					split:true,
					frame: true,bodyBorder: false, 
					items:[FaultNoticeTimes.baseForm]
				},{
					id:"report",
					region : 'center', layout : 'fit', bodyBorder: false, 
					split:true,
					items:[]
				}
		]
	 });
});