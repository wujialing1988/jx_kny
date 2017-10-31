/**
 * 机车施修任务兑现单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TwtStopTimeAnalyse');                       //定义命名空间
TwtStopTimeAnalyse.Nowdate ;//获取当前date
TwtStopTimeAnalyse.MonthFirstDay ;//最近一个月的第一天
/** 重新加载报表*/
TwtStopTimeAnalyse.reLoadReport = function(flag,siteID){
	var ctx_v = ctx.substring(1);
	var param = "&Nowdate=" + TwtStopTimeAnalyse.Nowdate + "&MonthFirstDay=" + TwtStopTimeAnalyse.MonthFirstDay + "&siteID=" + siteID ;
	var card_h = jQuery("#report").height();
	if("台位" == flag){
		var url_tw = getReportEffectivePath("/zb/twt/TwtStopTimeAnalyseTW.cpt&ctx="+ctx_v + param);
		document.getElementById("report").innerHTML = "<iframe style='width:100%;height:"+card_h+"px;overflow:auto;' frameborder='0' src=" + url_tw + "></iframe>";
	}else if("机车" == flag){
		var url_jc = getReportEffectivePath("/zb/twt/TwtStopTimeAnalyseTrain.cpt&ctx="+ctx_v + param);
		document.getElementById("report").innerHTML = "<iframe style='width:100%;height:"+card_h+"px;overflow:auto;' frameborder='0' src=" + url_jc + "></iframe>";
	}
}
TwtStopTimeAnalyse.baseForm = new Ext.form.FormPanel({
    labelWidth: 80,
	labelAlign:"left",
	layout:"column",
    defaults:{
				layout:"form",
				columnWidth:0.33,
				defaults:{
					xtype:"textfield",anchor:"60%" ,minListWidth: 200
				}
		},
    items: [{
            items: [
                {
                    id : '_type',
                    fieldLabel:'统计方式',
                    xtype: 'combo',
                    hiddenName: 'type',
                    allowBlank:false,
                    store:new Ext.data.SimpleStore({
                        fields: ['v', 't'],
                        data : [
                                ["台位","台位"],
                                ["机车","机车"]
                               ]
                    }),
                    valueField:'v',
                    displayField:'t',
                    triggerAction:'all',
                    value:"台位",
                    mode:'local',
                    width: 100,
                    listeners:{
						"select":function(combo){
							var siteID = Ext.getCmp("site_combo").getValue();
		    				TwtStopTimeAnalyse.reLoadReport(this.getValue(),siteID);
		    			}
			    	}
                }
            ]
        },{
            items: [{
						fieldLabel: '站场',
						id: 'site_combo',
						name: 'siteName',
						xtype:"Base_combo",
						business:'workPlace',
						entity:"com.yunda.jxpz.workplace.entity.WorkPlace",		
						queryHql:"from WorkPlace",
						fields:['workPlaceCode','workPlaceName'],
						displayField:"workPlaceName",
						valueField: "workPlaceCode",
						isAll: 'yes',
	                    listeners:{
							"select":function(combo){
								var flag = Ext.getCmp("_type").getValue();
			    				TwtStopTimeAnalyse.reLoadReport(flag,this.getValue());
			    			}
				    	}
					  }
            ]
        }
    ]
});
//页面自适应布局
var viewport = new Ext.Viewport({ 
     layout: "border",
    items:[{
                    frame:true, title:'查询',
					region: 'north',
					height: 80,
					collapsible: true,
                    items:[TwtStopTimeAnalyse.baseForm]
                },{
                    id:"report",
                    region : 'center', layout : 'fit', bodyBorder: false, 
                    items:[]
                }
    ]
     });
TwtStopTimeAnalyse.loadReport = function(){
	Ext.getCmp("site_combo").setDisplayValue(siteID,siteName);
	var Nowdate = new Date();//获取当前date
	var currentYear = Nowdate.getFullYear();//获取年度
	var currentMonth = Nowdate.getMonth();//获取当前月度
	var currentDay = Nowdate.getDate();//获取当前日
	var MonthFirstDay=new Date(currentYear,currentMonth-1,currentDay);
	TwtStopTimeAnalyse.MonthFirstDay = MonthFirstDay.format('Y-m-d');
	TwtStopTimeAnalyse.Nowdate = Nowdate.format('Y-m-d');
	TwtStopTimeAnalyse.reLoadReport("台位",siteID);
}
TwtStopTimeAnalyse.loadReport();
});