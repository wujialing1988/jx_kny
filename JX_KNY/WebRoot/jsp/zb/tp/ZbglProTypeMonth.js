/**
 * 按专业月度统计表 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('proTypeMonth');   //定义命名空间
	
		var nowDate = new Date();
		var currentYear1 = nowDate.getFullYear();//获取年度
		var currentMonth1 = nowDate.getMonth();//获取当前月度
		var currentDate1 = nowDate.getDate();//获取当前日期
		var MonthStartDay = new Date(currentYear1,currentMonth1-1,currentDate1);
	
	
	// 获取时间
	proTypeMonth.getCurrentMonth = function(args){
		 var startDate = new Date();
		if(1 == args){
			startDate = Ext.getCmp("month").getValue();
		}   
		var currentYear = startDate.getFullYear();//获取年度
		var currentMonth = startDate.getMonth();//获取当前月度
		var currentDate= startDate.getDate();//获取当前日期
		var MonthEndDay=new Date(currentYear,currentMonth+1,currentDate-1);
		 return MonthEndDay.format('Y-m-d')
	}
	
	proTypeMonth.baseForm = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "99%" }, autoScroll:true,
	    layout: "form",		border: false,		style: "padding:4px",		labelWidth: 120,
	    items: [ {
	        xtype: 'panel',	border:true,  layout:'column',	align:'center', baseCls: "x-plain",
	        items: [{
	            align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
				labelWidth: 70,
	            columnWidth: 0.3, 
	            items: [
	        		{	
			        	fieldLabel:"处理部门",
			        	id:"depart",
				    	xtype: 'OmOrganizationCustom_comboTree',
	//					queryHql:"[degree]tream",
						hiddenName:"org",
						returnField:[{widgetId:"orgseq",propertyName:"orgseq"}],
						fullNameDegree:"plant",
						selectNodeModel:"exceptRoot",
						width: 150},
						
			        { fieldLabel:"机构序列", id:"orgseq",xtype: 'hidden',hiddenName: 'orgseq'}
	            ]
	        }, {
	            align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
				labelWidth: 100,
	            columnWidth: 0.3, 
	            items: [{	
		            xtype: 'compositefield', fieldLabel:"统计时间区间", combineErrors: false, 
		            items:[{
							fieldLabel:"统计时间区间",
				            id:"month",
					    	xtype:'my97date', initNow:false, value: MonthStartDay,
					    	hiddenName: 'month',
					    	format: "Y-m-d", 
					    	my97cfg: {dateFmt:"yyyy-MM-dd"},
					    	width: 140,
					    	listeners : { 
					    		"blur" : function() {   
					    			Ext.getCmp("end_month").setValue(proTypeMonth.getCurrentMonth(1));
					    		}
					    	}
					    	
				        },{	xtype: 'label', text: '-', style: 'height:23px; line-height:23px;'
				        },{
				            id:"end_month",disabled: true,
					    	xtype:'my97date',
					    	hiddenName: 'end_month', value:proTypeMonth.getCurrentMonth(0),
					    	format: "Y-m-d",initNow:false,
					    	width: 140
				        }]
	        	}]
	        }]	
	        },{
		        xtype: 'panel',	border:true,  layout:'column',	align:'center', baseCls: "x-plain",
		        items: [        
	       {
	            align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
				labelWidth: 100,
	            columnWidth: 0.3, 
	            items: [{ fieldLabel:"专业类型", id:"professionalTypeID",xtype: 'ProfessionalType_comboTree',hiddenName: 'professionalTypeIDX',
							allowBlank:false,width:300,
							selectNodeModel:'exceptRoot'}
	            ]
	        },{
	           align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
				labelWidth: 70,
	            columnWidth: 0.1, 
	            items: [{ xtype:'button',text:"统计",width:60,
	        		handler:function(){
	        			//表单验证是否通过
				        var form = proTypeMonth.baseForm.getForm(); 
				        if (!form.isValid()) return;
				        var data = form.getValues();
				        var ctx_v = ctx.substring(1);
						var url = getReportEffectivePath("/zb/tp/ZbglProTypeMonth.cpt&date=" + data.month +
						"&protype="+data.professionalTypeIDX+"&orgseq="+data.orgseq+"&orgid="+data.org+"&ctx="+ctx_v);
	//					window.open(url);
			    		var h = jQuery("#report").height();
			    		document.getElementById("report").innerHTML = "<iframe style='width:100%;height:"+h+"px;overflow:auto;' frameborder='0' src=" + url + "></iframe>";
					}}
	            ]
	        }
	        ,
	        {
	           align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
				labelWidth: 100,
	            columnWidth: 0.1, 
	            items: [
					{ xtype:'button',text:"清空",width:60,
	        		handler:function(){
				        var form = proTypeMonth.baseForm.getForm(); 
				        form.reset();
				        Ext.getCmp("professionalTypeID").clearValue();
				        Ext.getCmp("depart").clearValue();
				        Ext.getCmp("month").setValue("");
				        Ext.getCmp("end_month").setValue("");
					}}
	            ]
	        }
	        ]
	    }
	    ]
	});
	
	//页面自适应布局
	var viewport = new Ext.Viewport({ 
		 layout: "border",
		items:[{
						region:"north",
						layout:"fit",
						height:100,
						split:true,
						maxSize:70,
						minSize:70,
						frame: true,bodyBorder: false, 
						items:[proTypeMonth.baseForm]
					},{
						id:"report",
						region : 'center', layout : 'fit', bodyBorder: false, 
						items:[]
					}
		]
	});
});