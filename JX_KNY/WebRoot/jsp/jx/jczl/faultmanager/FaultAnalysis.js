/**
 * 机车施修任务兑现单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('FaultQualityAnalyse');                       //定义命名空间
/** 获取当前月份的第一天和最后一天*/
FaultQualityAnalyse.getCurrentMonth = function(arg){
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
FaultQualityAnalyse.baseForm = new Ext.form.FormPanel({
    baseCls: "x-plain", align: "center",    defaultType: "textfield",   defaults: { anchor: "99%" }, autoScroll:true,
    layout: "form",     border: false,      style: "padding:4px",       labelWidth: 67, labelAlign : 'right',
    items: [{
            xtype: 'panel', border:true,  layout:'column',  align:'center', baseCls: "x-plain",
            items: [
        {
           align:'center',  defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
            columnWidth: 0.3, 
            items: [
                {
                    id : '_analzeKind',
                    fieldLabel:'统计方式',
                    xtype: 'combo',
                    hiddenName: 'analzeKind',
                    allowBlank:false,
                    store:new Ext.data.SimpleStore({
                        fields: ['v', 't'],
                        data : [
                                [byMonth,"月度统计"],
                                [bySeason,"季度统计"]
                               ]
                    }),
                    valueField:'v',
                    displayField:'t',
                    triggerAction:'all',
                    value:byMonth,
                    mode:'local',
                    width: 100
                }
            ]
        },{
           align:'center',  defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
            columnWidth: 0.3, 
            items: [
                {
                    id:'faultType_',
                    fieldLabel:'提票类型',
                    hiddenName: 'faultType',
                    allowBlank:false,
                    xtype: 'EosDictEntry_combo',
                    dicttypeid:'JCZL_FAULT_TYPE',
                    returnField: [{widgetId:"faultTypeName",propertyName:"dictname"}],
                    displayField:'dictname',valueField:'dictname',
                    width: 100
                }
            ]
        },{
           align:'center',  defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
            columnWidth: 0.3, 
            items: [
                {
                    id:"years",
                    fieldLabel:"统计年度",
                    hiddenName: 'years', 
                    xtype:'my97date',
                    format: "Y",
                    my97cfg: {dateFmt:"yyyy"}
                }
            ]
        },{
               align:'center',  defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
                labelWidth: 100,
                columnWidth: 0.1, 
                items: [
                    { xtype:'button',text:"重置",width:60,
                    handler:function(){
                        var form = FaultQualityAnalyse.baseForm.getForm(); 
                        form.reset();
                        //Ext.getCmp("_analzeKind").clearValue();//清空统计方式查询条件
                        Ext.getCmp("faultType_").clearValue();//清空提票类型查询条件
                        //Ext.getCmp("years").setValue("");       //清空统计年份查询条件
                        Ext.getCmp("plantOrgId_Id").clearValue();//清空所属车间查询条件
                        Ext.getCmp("comboTree_bId").clearValue();//清空配属局查询条件
                        Ext.getCmp("comboTree_dId").clearValue();//清空配属段查询条件
                        //document.getElementsByName('bId')[0].value='';
                        //document.getElementsByName('dId')[0].value='';
                    }}
                ]
            }
        ]
    },{
	        xtype: 'panel', border:true,  layout:'column',  align:'center', baseCls: "x-plain",
	        items: [{
	           align:'center',  defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
	            columnWidth: 0.3, 
	            items: [
	                {
	                	id: 'plantOrgId_Id', 
						fieldLabel: '所属车间', 
						xtype: 'OmOrganization_Win',
						hiddenName: 'plantOrgId', returnField: [{widgetId:"textId",propertyName:"orgid"}],
						allowBlank:false,
						rootId: 0, rootText: '所属车间',
						fullNameDegree: 'tream',
						valueField: 'id', displayField: 'orgname',
						width: 100, editable: false
			        }, {
			        	xtype: 'hidden', id : 'textId'
			        }
	            ]
	        },{
               align:'center',  defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
                columnWidth: 0.3, 
                items: [
                    {
			            id: "comboTree_bId", xtype: "BureauSelect_comboTree",
			            hiddenName: "bId", fieldLabel: "配&nbsp;属&nbsp;局", 
			            selectNodeModel: "leaf",
			            allowBlank : true,
			            width: 100,
			            listeners : {
			                "select" : function() {
			                    Ext.getCmp("comboTree_dId").reset();
			                    Ext.getCmp("comboTree_dId").clearValue();
			                    Ext.getCmp("comboTree_dId").orgid = this.getValue();
			                    Ext.getCmp("comboTree_dId").orgname = this.lastSelectionText;
			                }
			              }
			          }
                ]
            },{
               align:'center',  defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
                columnWidth: 0.3, 
                items: [
                    {
			            id: "comboTree_dId", xtype: "DeportSelect_comboTree",
			            hiddenName: "dId", fieldLabel: "配&nbsp;属&nbsp;段", 
			            selectNodeModel: "leaf" ,
			            allowBlank : true,
			            width: 100,
			            listeners : {
			                "beforequery" : function(){
			                    //选择段前先选局
			                    var comboTree_bId =  Ext.getCmp("comboTree_bId").getValue();
			                    if(comboTree_bId == "" || comboTree_bId == null){
			                        MyExt.Msg.alert("请先选择配属局！");
			                        return false;
			                    }
			                }
			            }
			        }
                ]
            },{
	           align:'center',  defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
	            columnWidth: 0.1, 
	            items: [
	                {
	                    xtype:'button',text:"统计",width:60,
	                    handler:function(){
	                        //表单验证是否通过
	                        var form = FaultQualityAnalyse.baseForm.getForm(); 
	                        var formValues = form.getValues();
                            //console.dir(formValues);
                            /*alert("dId__"+formValues.dId+"__");
                            alert("dId__"+formValues.dId.trim()+"__");
                            alert("bId__"+formValues.bId+"__");
                            alert("bId__"+formValues.bId.trim()+"__");
                            return;*/
	                        if (!form.isValid()) return;
	                        var _analzeKind = Ext.getCmp('_analzeKind').getValue();
	                        var ctx_v = ctx.substring(1);
	                        var orgName = Ext.getCmp("orgName").getValue();
	                        var faultTypeName = Ext.getCmp("faultTypeName").getValue();
	                        var orgids = formValues.plantOrgId.split(";");
	                        var plantOrgId = '';
	                        if (orgids.length > 0) {
	                        	plantOrgId += orgids[0];
	                        }
	                        for (var i = 1; i < orgids.length; i++) {
	                        	plantOrgId += ",";
	                        	plantOrgId += orgids[i];
	                        }
	                        /*var analzeKind = formValues.analzeKind;
	                        var faultType = formValues.faultType;
	                        var years = formValues.years;*/
	                        /*var data = form.getValues();
	                        if(data.endDate < data.beginDate){
	                            MyExt.TopMsg.msg('提示',"落成结束日期不能比落成开始日期小！", false, 1);
	                            return false;
	                        }*/
	                        //MyExt.TopMsg.msg('提示', url, false, 1);
	                        var h = jQuery("#report").height();
	                        if(_analzeKind=='byMonth'){
	                            var url = getReportEffectivePath("/jczl/faultAnalysis/faultAnalysis_byMonth.cpt&analzeKind="+formValues.analzeKind+
	                                "&bId="+formValues.bId.trim()+"&dId="+formValues.dId.trim()+"&faultType="+formValues.faultType+"&years="+formValues.years+"&plantOrgId="+plantOrgId+"&orgName="+orgName+"&faultTypeName="+faultTypeName+"&ctx="+ctx_v);
	                        }
	                        if(_analzeKind=='bySeason'){
	                            var url = getReportEffectivePath("/jczl/faultAnalysis/faultAnalysis_bySeason.cpt&analzeKind="+formValues.analzeKind+
	                                "&bId="+formValues.bId.trim()+"&dId="+formValues.dId.trim()+"&faultType="+formValues.faultType+"&years="+formValues.years+"&plantOrgId="+plantOrgId+"&orgName="+orgName+"&faultTypeName="+faultTypeName+"&ctx="+ctx_v);
	                        }
	                        document.getElementById("report").innerHTML = "<iframe style='width:100%;height:"+h+"px;overflow:auto;' frameborder='0' src='" + url + "'></iframe>";
	                        
	                    }
	               }
	            ]
	        }
        ]
    },{
            xtype: 'panel', border:true,  layout:'column',  align:'center', baseCls: "x-plain",
            items: [{
               align:'center',  defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
                items: [
                    {
                    id :'orgName',
                    hidden : true
                   },
                    {
                    id :'faultTypeName',
                    hidden : true
                   }
                ]
            }]
    }
    ]
});
//页面自适应布局
var viewport = new Ext.Viewport({ 
     layout: "border",
    items:[{
                    region:"north",
                    layout:"fit",
                    height:90,
                    split:true,
                    maxSize:100,
                    minSize:80,
                    frame: true,bodyBorder: false, 
                    items:[FaultQualityAnalyse.baseForm]
                },{
                    id:"report",
                    region : 'center', layout : 'fit', bodyBorder: false, 
                    items:[]
                }
    ]
     });
FaultQualityAnalyse.ss = function(){
    Ext.getCmp("tecProcess_comb").getStore().on("load",function(){ 
        if(this.getTotalCount() > 0){
            Ext.getCmp("tecProcess_comb").setDisplayValue(this.getAt(0).get('idx'),this.getAt(0).get('processName'));    
            }
        });
    Ext.getCmp("tecProcess_comb").getStore().load();    
}
//FaultQualityAnalyse.ss();
});