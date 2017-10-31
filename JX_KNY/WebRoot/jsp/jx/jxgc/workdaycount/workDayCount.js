/**
 * 按专业月度统计表 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('workDayCount');                       //定义命名空间
/** 获取当前月份的第一天和最后一天*/
workDayCount.getCurrentMonth = function(arg){
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
workDayCount.baseForm = new Ext.form.FormPanel({
    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "99%" }, autoScroll:true,
    layout: "form",		border: false,		style: "padding:4px",		labelWidth: 80,
    items: [{
        xtype: 'panel',	border:true,  layout:'column',	align:'center', baseCls: "x-plain",
        items: [
        {
           align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
			labelWidth: 50,
            columnWidth: 0.3, 
            items: [
        		{	
		        	fieldLabel:"班组",
		        	id:"depart",
			    	xtype: 'OmOrganizationCustom_comboTree',
//					queryHql:"[degree]tream",
			    	allowBlank:false,
					hiddenName:"org",width:130,
					returnField:[{widgetId:"orgseq",propertyName:"orgseq"}],
					selectNodeModel:"exceptRoot"},
		        { fieldLabel:"班组序列", id:"orgseq",xtype: 'hidden',hiddenName: 'orgseq'}
            ]
        },{
           align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
			labelWidth: 80,
            columnWidth: 0.3, 
            items: [
        		{ id:"beginDate",fieldLabel:'日期(开始)',name:'beginDate',xtype:'my97date', my97cfg: {dateFmt:"yyyy-MM-dd"},allowBlank:false,
        		value:workDayCount.getCurrentMonth('begin'),width:100}
            ]
        },{
           align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
			labelWidth: 80,
            columnWidth: 0.3, 
            items: [
        		{ id:"endDate",fieldLabel:'日期(截止)',name:'endDate',xtype:'my97date', my97cfg: {dateFmt:"yyyy-MM-dd"},allowBlank:false,
        		value:workDayCount.getCurrentMonth('end'),width:100}
            ]
        },
        {
           align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
			labelWidth: 100,
            columnWidth: 0.1, 
            items: [
        		{ xtype:'button',text:"统计",width:60,
        		handler:function(){
        			//表单验证是否通过
			        var form = workDayCount.baseForm.getForm(); 
			        if (!form.isValid()) return;
//			        var beginDate = Ext.getCmp("beginDate").getValue();
//			        var endDate = Ext.getCmp("endDate").getValue();
//			        if(beginDate == ""){
//			        	MyExt.TopMsg.msg('提示',"日期(开始)不能为空！", true, 1);
//	    				return false;
//			        }
//			         if(endDate == ""){
//			        	MyExt.TopMsg.msg('提示',"日期(截止)不能为空！", true, 1);
//	    				return false;
//			        }
			        var data = form.getValues();
			        if(data.endDate < data.beginDate){
			        	MyExt.TopMsg.msg('提示',"截止日期不能比开始日期小！", false, 1);
	    				return false;
			        }
			        var ctx_v = ctx.substring(1);
					var url = getReportEffectivePath("/jxgc/workDayCount.cpt&beginDate="+data.beginDate+"&endDate="+data.endDate+"&orgseq="+data.orgseq+"&orgid="+data.org+"&ctx="+ctx_v);
//					window.open(url);
		    		var h = jQuery("#report").height();
		    		document.getElementById("report").innerHTML = "<iframe style='width:100%;height:"+h+"px;overflow:auto;' frameborder='0' src=" + url + "></iframe>";
				}}
            ]
        }
//        ,
//        {
//           align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
//			labelWidth: 100,
//            columnWidth: 0.1, 
//            items: [
//				{ xtype:'button',text:"清空",width:60,
//        		handler:function(){
//			        var form = workDayCount.baseForm.getForm(); 
//			        form.reset();
//			        Ext.getCmp("professionalTypeID").clearValue();
//			        Ext.getCmp("type_combo").clearValue();
//			        Ext.getCmp("depart").clearValue();
//			        Ext.getCmp("month").setValue("");
//				}}
//            ]
//        }
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
					height:60,
					split:true,
					maxSize:70,
					minSize:70,
					frame: true,bodyBorder: false, 
					items:[workDayCount.baseForm]
				},{
					id:"report",
					region : 'center', layout : 'fit', bodyBorder: false, 
					items:[]
				}
	]
});
});