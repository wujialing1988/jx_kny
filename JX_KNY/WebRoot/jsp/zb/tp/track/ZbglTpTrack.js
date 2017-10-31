Ext.onReady(function(){
	Ext.namespace('ZbglTpTrack');                       //定义命名空间
	ZbglTpTrack.searchParam = {};
	ZbglTpTrack.zbglTpTrackIDX = "";
	/*** 查询表单 start ***/
	ZbglTpTrack.searchLabelWidth = 90;
	ZbglTpTrack.searchAnchor = '95%';
	ZbglTpTrack.searchFieldWidth = 270;
	
	ZbglTpTrack.recordIDX;
	ZbglTpTrack.trackIDX = "";
	
	//最近一个月
	ZbglTpTrack.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var currentDay = Nowdate.getDate();//获取当前日
		var MonthFirstDay=new Date(currentYear,currentMonth-2,currentDay);
		return MonthFirstDay.format('Y-m-d');
	}
	ZbglTpTrack.searchForm = new Ext.form.FormPanel({
		layout:"form", border:false, style:"padding:10px" ,
		labelWidth: ZbglTpTrack.searchLabelWidth, align:'center',baseCls: "x-plain",
		defaults:{anchor:"98%"}, buttonAlign:'center',
		items:[{
			xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
			items:[{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
				fieldWidth: ZbglTpTrack.searchFieldWidth, labelWidth: ZbglTpTrack.searchLabelWidth, defaults:{anchor:ZbglTpTrack.searchAnchor},
				items:[{ 
					fieldLabel: "车型",
					hiddenName: "trainTypeIDX",
					displayField: "shortName", valueField: "typeID",
					pageSize: 0, minListWidth: 50,
					editable:true,
					forceSelection: true,
					xtype: "Base_combo",
		        	business: 'trainType',													
					fields:['typeID','shortName'],
					queryParams: {'isCx':'yes'},
					entity:'com.yunda.jx.base.jcgy.entity.TrainType',
					listeners : {   
			        	"select" : function() {   
			            	//重新加载车号下拉数据
			                var trainNo_comb = ZbglTpTrack.searchForm.getForm().findField("trainNo");   
			                trainNo_comb.reset();  
			                trainNo_comb.clearValue(); 
			                trainNo_comb.queryParams.trainTypeIDX = this.getValue();
			                trainNo_comb.cascadeStore();	
			        	}   
			    	}
				}]
			},{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
				fieldWidth: ZbglTpTrack.searchFieldWidth, labelWidth: ZbglTpTrack.searchLabelWidth, defaults:{anchor:ZbglTpTrack.searchAnchor},
				items:[{
					id: "trainNo_comb_search",
					fieldLabel: "车号",
					hiddenName: "trainNo", 
					displayField: "trainNo", valueField: "trainNo",
					pageSize: 20, minListWidth: 50,
					minChars : 1,
					vtype: "numberInt",
					anchor: "95%", 				
					xtype: "Base_combo",
					business: 'trainNo',
					entity:'com.yunda.jx.jczl.attachmanage.entity.jczlTrain',
					fields:["trainNo","makeFactoryIDX","makeFactoryName",
					{name:"leaveDate", type:"date", dateFormat: 'time'},
					"buildUpTypeIDX","buildUpTypeCode","buildUpTypeName","trainUse","trainUseName",
					"bId","dId","bName","dName","bShortName","dShortName"],
					queryParams:{'isCx':'no','isIn':'false','isRemoveRun':'false'},
					isAll: 'yes',
					editable:true,
					returnField: [
//						              {widgetId:"buildUpTypeIDXId",propertyName:"buildUpTypeIDX"},
//						              {widgetId:"buildUpTypeCodeId",propertyName:"buildUpTypeCode"},
//						              {widgetId:"buildUpTypeNameId",propertyName:"buildUpTypeName"},
						              {widgetId:"dID",propertyName:"dId"},
						              {widgetId:"dNameId",propertyName:"dName"}]
				},
				{ id:'dID', xtype:'hidden', name: 'dId' },
				  { id:'dNameId', xtype:'hidden', name: 'dName' }]
			},{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
				fieldWidth: ZbglTpTrack.searchFieldWidth, labelWidth: ZbglTpTrack.searchLabelWidth, defaults:{anchor:ZbglTpTrack.searchAnchor},
				items:[{
					xtype: 'compositefield', fieldLabel : '跟踪时间', combineErrors: false,
		        	items: [
		            { id:"startTimeID",name: "startTime",  xtype: "my97date",format: "Y-m-d",value : ZbglTpTrack.getCurrentMonth(),width: 100},
					{xtype:'label', text: '至：'},
					{ id:"EndTimeID",name: "EndTime", xtype: "my97date",format: "Y-m-d", width: 100}]
				},{ id:"faultNameID",fieldLabel: "跟踪原因", name: 'trackReason', xtype: 'textfield'}]
			},{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
				fieldWidth: ZbglTpTrack.searchFieldWidth, labelWidth: ZbglTpTrack.searchLabelWidth, defaults:{anchor:ZbglTpTrack.searchAnchor},
				items:[{
					xtype: 'compositefield', fieldLabel : '跟踪次数', combineErrors: false,
		        	items: [
		            { id:"greater",name: "greater", xtype:'textfield', width: 100},
					{xtype:'label', text: '~'},
					{ id:"less",name: "less", xtype:'textfield', width: 100}]
				}]
			}]
		}],
		buttons:[{
				text:'查询', iconCls:'searchIcon', 
				handler: function(){
				
					var form = ZbglTpTrack.searchForm.getForm();	
			        var searchParam = form.getValues();
			        if (Ext.get("trainNo_comb_search").dom.value && Ext.get("trainNo_comb_search").dom.value != '') {
						searchParam.trainNo = Ext.get("trainNo_comb_search").dom.value;
					}
			        searchParam = MyJson.deleteBlankProp(searchParam);
			        //正在跟踪状态
					searchParam.status = TRACKING
					//刷新正在跟踪列表
					ZbglTpTrack.trackIngGrid.store.load({
						params: { entityJson: Ext.util.JSON.encode(searchParam) }       
					});
					//结束跟踪状态
					searchParam.status = TRACKOVER
					//刷新结束跟踪列表
					ZbglTpTrack.trackEndGrid.store.load({
						params: { entityJson: Ext.util.JSON.encode(searchParam) }       
					});
					//清空跟踪记录单list
					ZbglTpTrack.deteilgrid.store.removeAll();
				}
			},{
	            text: "重置条件", iconCls: "resetIcon", 
	            handler: function(){
	            	var form = ZbglTpTrack.searchForm
	                form.getForm().reset();
	                var components = "Base_combo";
                    var component = form.findByType(components);
                        if (!Ext.isEmpty(component) && Ext.isArray(component)) {
						    for (var i = 0; i < component.length; i++) {
						        component[i].clearValue();
						    }						
					    }	
				ZbglTpTrack.trackIngGrid.store.load();
				ZbglTpTrack.trackEndGrid.store.load();
	            }
			}]
	});
	/*** 提票明细列表***/
	ZbglTpTrack.trackIngGrid = new Ext.yunda.Grid({
	    loadURL: ctx + '/zbglTpTrackRdp!findZbglTpTrackRdpPageList.action',//装载列表数据的请求URL
	    singleSelect: true,
	    viewConfig: null,
	    tbar:[{
	    	text:"本次跟踪提票", iconCls:"editIcon",
	    	handler: function() {
	    		//在开始跟踪操作前，重载表格，
	    		ZbglTpTrack.trackIngGrid.store.load();
	    		Ext.Msg.confirm("提示  ", "是否开始本次跟踪提票？  ", function(btn){
                if(btn != 'yes')    return;
	    		var sm = ZbglTpTrack.trackIngGrid.getSelectionModel();
				if (sm.getCount() <= 0) {
					MyExt.Msg.alert('尚未选择一条记录！');
					return;
				}
				if (sm.getCount() > 1) {
					MyExt.Msg.alert('请只选择一条查看！');
					return;
				}
				var record = sm.getSelections()[0];
				var color = record.get('color');
				if(!color){
					MyExt.Msg.alert('跟踪的机车还未入段！');
					return;
				}
                var singleStatus = record.get('singleStatus');
                if(singleStatus != 0){
                    MyExt.Msg.alert('请选择正在跟踪的单据进行提票！');
					return;
                }
				//本次跟踪提票界面 8.11
				trainNo = record.get('trainNo');
	            trainTypeShortName = record.get('trainTypeShortName');
	            trainTypeIDX = record.get('trainTypeIDX');
	            trackIDX = record.get('idx');
	            jt6IDX = record.get('jt6IDX');
	            dId = record.get('dId');  
	            dName = record.get('dName');
	           
				    ZbglTpTrackTp.initSaveWin();
	                ZbglTpTrack.infoWin.show();
				//提票界面打开
	            });
	    	}
	    },{
	    	text:"开始本次跟踪", iconCls:"addIcon",
	    	handler: function() {
	    		//在开始跟踪操作前，重载表格，
	    		//ZbglTpTrack.trackIngGrid.store.load();	
	    		Ext.Msg.confirm("提示  ", "是否开始跟踪操作？  ", function(btn){
                if(btn != 'yes')    return;
	    	    var sm = ZbglTpTrack.trackIngGrid.getSelectionModel();
				if (sm.getCount() <= 0) {
					MyExt.Msg.alert('尚未选择一条记录！');
					return;
				}
				if (sm.getCount() > 1) {
					MyExt.Msg.alert('请只选择一条查看！');
					return;
				}
				var record = sm.getSelections()[0];

				ZbglTpTrack.trackIDX = record.get('idx');
				var rdpIDX = record.get('rdpIDX');
				var inTime = record.get('inTime');
				var time = new Date(inTime).format('Y-m-d H:i:s');
				var color = record.get('color');
				if(!color){
					MyExt.Msg.alert('请选择入段的机车进行跟踪操作！');
					return;
				}
				if (record.get('singleStatus') == "0") {
					MyExt.Msg.alert('已经开始跟踪');
					return;
				}
				Ext.Ajax.request({
                    url: ctx + '/zbglTpTrackRdp!createRecordAndStartTrack.action',  
                    params: {
                             trackIDX: ZbglTpTrack.trackIDX,
                             rdpIDX: rdpIDX,
                             inTime: time,
                             singleStatus: "0"
                             },
                    success: function(respinse, options){
                   //   var result = Ext.util.JSON.decode(response.responseText);
                        MyExt.Msg.alert("操作成功！开始本次跟踪");
                        ZbglTpTrack.trackIngGrid.store.load(); 
                        var searchParam = {};
                        searchParam.trackRdpIDX = ZbglTpTrack.trackIDX;
                        ZbglTpTrack.deteilgrid.store.load({
					        params: { entityJson: Ext.util.JSON.encode(searchParam) }     
				        });
				        ZbglTpTrack.deteilgrid.store.reload();
	                    ZbglTpTrack.trackIDX = "";
                    },
                    failure: function(respinse, options){
                        MyExt.Msg.alert("");
                    }
                });
                });
	    	}
	    }],
		fields: [
			//隐藏字段
			{
				header:'', layout:"fit",width:100,
			    renderer :function(a,b,c,d){
			    	var str = "<button type='button' class='x-btn-small x-btn-icon-small-left x-btn-text x-btn x-btn-text-icon x-btn-over ' onclick='ZbglTpTrack.editZbglRdpRecordInfo(\"" + c.data.idx + "\")'>本次跟踪意见</button>";
			    	return str;  	
			    }
			},
			{header:'主键idx', dataIndex:'idx', hidden:true},
			{header:'jt6IDX', dataIndex:'jt6IDX', hidden:true},
			{header:'rdpIDX', dataIndex:'rdpIDX', hidden:true},
			{header:'车型idx', dataIndex:'trainTypeIDX', hidden:true},
			{header:'提报跟踪人员idx', dataIndex:'trackPersonIDX', hidden:true},
			{header:'系统分类编码', dataIndex:'faultFixFullCode', hidden:true},
			{header:'故障idx', dataIndex:'faultID', hidden:true},
			{header:'配属段idx', dataIndex:'dId', hidden:true},
			{header:'配属段名称', dataIndex:'dName', hidden:true},
			{header:'入段时间', dataIndex:'inTime', xtype:'datecolumn', format: "Y-m-d H:i:s",hidden:true},
			//显示字段
			{header:'车型', dataIndex:'trainTypeShortName',width:50},
			{header:'车号', dataIndex:'trainNo',width:50},
			{
				header:'原票活信息', dataIndex:'faultNoticeCode',
				renderer :function(a,b,c,d){
		            var html = "<span><a href='#' onclick='ZbglTpTrack.openZbglTpInfo(\""+c.data.jt6IDX+"\")'>"+a+"</a></span>";
		            return html;
			    }
			},
			{header:'跟踪状态', dataIndex:'status', hidden:true},
			{header:'原故障位置', dataIndex:'faultFixFullName',width:250},
			{header:'原故障现象', dataIndex:'faultName',width:100},
			{header:'原处理情况', dataIndex:'repairResult',width:180},
			{header:'原处理情况描述', dataIndex:'repairDesc',width:180},
			{header:'提报跟踪原因', dataIndex:'trackReason',width:180},
			{header:'已跟踪次数', dataIndex:'recordCount',width:80},
			{header:'提报跟踪人员', dataIndex:'trackPersonName'},
			{header:'提报跟踪时间', dataIndex:'trackDate', xtype:'datecolumn', format: "Y-m-d H:i:s"},
			{header:'color', dataIndex:'color', hidden:true},
			{header:'本次跟踪操作状态', dataIndex:'singleStatus', hidden:true,
				renderer: function(v){
		            switch(v){
		            	case 1:
		                    return "本次跟踪结束";
		                case 0:
		                    return "本次跟踪开始";
		                default:
		                    return "还未开始跟踪";
		            }
		        }
			}
		],
		listeners:{  
			//单击  
	       	rowclick:function(grid,row){  
		        var searchParam = {};
			    var recordVal  = grid.store.getAt(row);
			    searchParam.trackRdpIDX = recordVal.data.idx;
				//跟踪次数倒排序
       		    ZbglTpTrack.deteilgrid.store.setDefaultSort('trackCount', "DESC"),
	          	ZbglTpTrack.deteilgrid.store.load({
					params: { entityJson: Ext.util.JSON.encode(searchParam) }       
				});
	      	}
   		},
		toEditFn : function(){}
	});
	
	ZbglTpTrack.trackIngGrid.store.on("beforeload", function(){	
		var form = ZbglTpTrack.searchForm.getForm();	
        var searchParam = form.getValues();
        
        if (Ext.get("trainNo_comb_search").dom.value && Ext.get("trainNo_comb_search").dom.value != '') {
			searchParam.trainNo = Ext.get("trainNo_comb_search").dom.value;
		}
        searchParam = MyJson.deleteBlankProp(searchParam);
		searchParam.status = TRACKING
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	
	ZbglTpTrack.trackIngGrid.store.on("load",function(r){
		var girdcount = 0;
		this.each(function(r){		
			if (!Ext.isEmpty(r.get('color'))){
				ZbglTpTrack.trackIngGrid.getView().getRow(girdcount).style.backgroundColor = r.get('color');
			}
			girdcount = girdcount + 1;
		});								
	});
	
	/*** 提票明细列表***/
	ZbglTpTrack.trackEndGrid = new Ext.yunda.Grid({
	    loadURL: ctx + '/zbglTpTrackRdp!findZbglTpTrackRdpPageList.action',//装载列表数据的请求URL
	    singleSelect: true,
	    viewConfig: null,
	    tbar:[],
	    isEdit: false,
		fields: [
			{header:'主键idx', dataIndex:'idx', hidden:true},
			{header:'jt6IDX', dataIndex:'jt6IDX', hidden:true},
			{header:'rdpIDX', dataIndex:'rdpIDX', hidden:true},
			{header:'车型idx', dataIndex:'trainTypeIDX', hidden:true},
			{header:'提报跟踪人员idx', dataIndex:'trackPersonIDX', hidden:true},
			{header:'系统分类编码', dataIndex:'faultFixFullCode', hidden:true},
			{header:'故障idx', dataIndex:'faultID', hidden:true},
			{header:'配属段idx', dataIndex:'dId', hidden:true},
			{header:'配属段名称', dataIndex:'dName', hidden:true},
			//显示字段
			{header:'车型', dataIndex:'trainTypeShortName',width:50},
			{header:'车号', dataIndex:'trainNo',width:50},
			{
				header:'原票活信息', dataIndex:'faultNoticeCode',
				renderer :function(a,b,c,d){
		            var html = "<span><a href='#' onclick='ZbglTpTrack.openZbglTpInfo(\""+c.data.jt6IDX+"\")'>"+a+"</a></span>";
		            return html;
			    }
			},
			{header:'跟踪状态', dataIndex:'status', hidden:true},
			{header:'原故障位置', dataIndex:'faultFixFullName',width:250},
			{header:'原故障现象', dataIndex:'faultName',width:100},
			{header:'原处理情况', dataIndex:'repairResult',width:180},
			{header:'原处理情况描述', dataIndex:'repairDesc',width:180},
			{header:'提报跟踪原因', dataIndex:'trackReason',width:180},
			{header:'已跟踪次数', dataIndex:'recordCount',width:80},
			{header:'提报跟踪人员', dataIndex:'trackPersonName'},
			{header:'提报跟踪时间', dataIndex:'trackDate', xtype:'datecolumn', format: "Y-m-d H:i:s"},
			{header:'color', dataIndex:'color', hidden:true},
			{header:'本次跟踪操作状态', dataIndex:'singleStatus', hidden:true,
				renderer: function(v){
		            switch(v){
		            	case 1:
		                    return "本次跟踪结束";
		                case 0:
		                    return "本次跟踪开始";
		                default:
		                    return "还未开始跟踪";
		            }
		        }
			}
		],
		listeners:{  
			//单击  
	       	rowclick:function(grid,row){  
	       		var searchParam = {};
			    var recordVal  = grid.store.getAt(row);
			    searchParam.trackRdpIDX = recordVal.data.idx; 
		    	//跟踪次数倒排序
        	 	ZbglTpTrack.deteilgrid.store.setDefaultSort('trackCount', "DESC"),
	          	//将trackRdpIDX装载到ZbglTpTrack.deteilgrid表格的store中，ZbglTpTrack.deteilgrid的Ajax将store传到所映射的控制器
	          	ZbglTpTrack.deteilgrid.store.load({
	          		//参数装载的格式
					params: { entityJson: Ext.util.JSON.encode(searchParam) }       
				});
	      	}
   		},
		//不需要双击显示
		toEditFn : function(){}
	});
	
	ZbglTpTrack.trackEndGrid.store.on("beforeload", function(){	
		//获得表单的所有内容
		var form = ZbglTpTrack.searchForm.getForm();	
		//取得表单内容的值
        var searchParam = form.getValues();
        //判断表单某一input中有无值（通过idx定位找到），若有值，取出值添加到要传输的参数格式中
        if (Ext.get("trainNo_comb_search").dom.value && Ext.get("trainNo_comb_search").dom.value != '') {
			searchParam.trainNo = Ext.get("trainNo_comb_search").dom.value;
		}
        searchParam = MyJson.deleteBlankProp(searchParam);
		searchParam.status = TRACKOVER
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	
	/*** 提票跟踪记录单列表***/
	ZbglTpTrack.deteilgrid = new Ext.yunda.Grid({
	    loadURL: ctx + '/zbglTpTrackRdpRecord!list.action',//装载列表数据的请求URL
	    singleSelect: true,
	    page: false,
	    viewConfig: null,
	    tbar:[],
	    storeAutoLoad: false,
		fields: [
		    	{header:'查看记录单提票', dataIndex:'对应实体字段名',editor:{ maxLength:50 ,allowBlank:false},
		     renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
                var html = "";
                //点击超链接调用的函数
                html = "<span><a href='#' onclick='ZbglTpTrack.openRecordTpInfo(\""+record.data.idx+"\")'>"+"查看提票单"+"</a></span>";
                return html;}
			},
			{header:'跟踪状态', dataIndex:'status',
				renderer: function(v){
		            switch(v){
		            	case ALREADY:
		                    return "已处理";
		                    break;
		                case UNREADY:
		                    return "处理中";
		                    break;
		                default:
		                    return v;
		            }
		        }
			},
			{header:'本次入段时间', dataIndex:'inTime',xtype:'datecolumn', format: "Y-m-d H:i:s"},
			{header:'跟踪次数', dataIndex:'trackCount'},
			{header:'整备单idx', dataIndex:'rdpIDX', hidden:true},
			{header:'本次记录单idx', dataIndex:'idx', hidden:true},
			{header:'跟踪人员idx', dataIndex:'trackPersonIDX', hidden:true},
			{header:'跟踪人员姓名', dataIndex:'trackPersonName'},
			{header:'本次跟踪结束时间', dataIndex:'trackDate', xtype:'datecolumn', format: "Y-m-d H:i:s", width:180},
			{header:'跟踪意见', dataIndex:'trackReason', width:500}
		],
		//不需要双击显示
		toEditFn : function(){}
	});
	
	//编辑选项卡列表
	ZbglTpTrack.tabs = new Ext.TabPanel({
	    activeTab: 0, enableTabScroll:true, border:false,
	    items:[{
	            title: "正在跟踪查看", layout: "fit", border: false, items: [ ZbglTpTrack.trackIngGrid ]
	        },{
	            title: "结束跟踪查看", layout: "fit", border: false, items: [ ZbglTpTrack.trackEndGrid ]
	        }]
	});
	
	/*** 界面布局 start ***/
	ZbglTpTrack.panel = {
	    xtype: "panel", layout: "border", 
	    items: [{
	        region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
	        collapsible:true, height: 212, bodyBorder: false,
	        items:[ZbglTpTrack.searchForm], frame: true, title: "查询"
	    },{
	        title: "提票跟踪单",region : 'center', layout : 'fit', bodyBorder: false, items : [ ZbglTpTrack.tabs ]
	    },{
	        title: "提票跟踪记录单",region : 'south', layout : 'fit',collapsible:true,frame: true, height: 172, bodyBorder: false, items:[ ZbglTpTrack.deteilgrid ]
	    }]
	};

	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:ZbglTpTrack.panel });
	
	ZbglTpTrack.editZbglRdpRecordInfo = function(zbglTpTrackIDX){
		ZbglTpTrack.trackIngGrid.store.load();
	    //将跟踪单的idx附到全局变量中
	    ZbglTpTrackThisTime.zbglTpTrackIDX = zbglTpTrackIDX;
	    
	    //如果机车没有入段就没有背景颜色，通过有无背景颜色判断机车是否入段，能否被操作
	    var sm = ZbglTpTrack.trackIngGrid.getSelectionModel();
	    var record = sm.getSelections()[0]; 
	    var color = record.get('color');
			if(!color){
				MyExt.Msg.alert('当前选择的机车还没有入段');
				return;
			}
		//如果还没有开始跟踪，提示，不能结束本次跟踪
			var singleStatus = record.get('singleStatus');
			if(singleStatus == "1"){
				MyExt.Msg.alert('当前机车还没有开始本次跟踪操作!');
				return;
			}
			
		//点击出现本次跟踪信息窗口
		ZbglTpTrackThisTime.trackWin.show();
	}
	
	ZbglTpTrack.openZbglTpInfo = function(jt6IDX){
		ZbglTpInfoFormWin.showWin(jt6IDX);
		ZbglTpTrack.trackIngGrid.store.load();
	}
	
	//本次跟踪提票编辑界面  8.11
	ZbglTpTrack.infoWin = new Ext.Window({
		title: "故障提票编辑", maximizable:false, layout: "fit", 
		closeAction: "hide", modal: true, maximized: true , buttonAlign:"center",
		items: [ZbglTpTrackTp.panel]
	});
	


	
    //显示和记录单关联的jt6
	ZbglTpTrack.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/zbglTpTrackRdpRecordCenter!findZbglTpByTrackRdpRecordIDX.action',//装载列表数据的请求URL
	    singleSelect: true,
	    viewConfig: null,
	    tbar:[],
		fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true
	},{
		header:'提票单号', dataIndex:'faultNoticeCode'
	},{
		header:'车型编码', dataIndex:'trainTypeIDX', hidden:true
	},{
		header:'车型', dataIndex:'trainTypeShortName',  width : 55
	},{
		header:'车号', dataIndex:'trainNo',  width : 50
	},{
		header:'提票人', dataIndex:'noticePersonName', width : 80
	},{
		header:'提票时间', dataIndex:'noticeTime', xtype:'datecolumn', format: "Y-m-d H:i:s", width : 150
	},{
		header:'发现人', dataIndex:'discover',  width : 80
	},{
		header:'故障部件', dataIndex:'faultFixFullName',  width : 300
	},{
		header:'故障现象', dataIndex:'faultName'
	},{
		header:'故障描述', dataIndex:'faultDesc'
	},{
		header:'故障原因', dataIndex:'faultReason',width : 200
	},{
		header:'故障发生日期', dataIndex:'faultOccurDate', xtype:'datecolumn', format: "Y-m-d", width : 120
	},{
		header:'提票状态', dataIndex:'faultNoticeStatus', 
        renderer: function(v){
            switch(v){
            	case STATUS_INIT:
                    return STATUS_INIT_CH;
                case STATUS_DRAFT:
                    return STATUS_DRAFT_CH;
                case STATUS_OPEN:
                    return STATUS_OPEN_CH;
                case STATUS_OVER:
                    return STATUS_OVER_CH;
                case STATUS_CHECK:
                    return STATUS_CHECK_CH;
                default:
                    return v;
            }
        }
	},{
		header:'配属段编码', dataIndex:'dID', hidden:true
	},{
		header:'配属段名称', dataIndex:'dName', hidden:true
	},{
		header:'故障部件编码', dataIndex:'faultFixFullCode', hidden:true
	},{
		header:'故障ID', dataIndex:'faultID', hidden:true
	},{
		header:'专业类型ID', dataIndex:'professionalTypeIdx', hidden:true
	},{
		header:'专业类型名称', dataIndex:'professionalTypeName', hidden:true
	},{
		header:'提票来源', dataIndex:'noticeSource', hidden:true
	},{
		header:'提票人编码', dataIndex:'noticePersonId', hidden:true
	},{
		header:'提票站场名称', dataIndex:'siteName', hidden:true
	},{
		header:'处理班组编码', dataIndex:'revOrgID', hidden:true
	},{
		header:'处理班组名称', dataIndex:'revOrgName', hidden:true
	},{
		header:'处理班组序列', dataIndex:'revOrgSeq', hidden:true
	},{
		header:'接票人编码', dataIndex:'revPersonId', hidden:true
	},{
		header:'接票人', dataIndex:'revPersonName'
	},{
		header:'接票时间', dataIndex:'revTime', xtype:'datecolumn', format: "Y-m-d H:i:s", width : 150
	},{
		header:'施修方法', dataIndex:'methodDesc'
	},{
		header:'处理结果', dataIndex:'repairResult',
		renderer: function(value, metaData, record, rowIndex, colIndex, store){
			if (Ext.isEmpty(value))
				return "";
            if (record.get("repairClass") == REPAIRCLASS_SX)
            	return EosDictEntry.getDictname("JCZB_TP_REPAIRRESULT",value);
            else if (record.get("repairClass") == REPAIRCLASS_lX)
            	return EosDictEntry.getDictname("JCZB_LXTP_REPAIRRESULT",value);
        }
	},{
		header:'处理结果描述', dataIndex:'repairDesc',width : 250
	},{
		header:'销票人编码', dataIndex:'handlePersonId', hidden:true
	},{
		header:'销票人', dataIndex:'handlePersonName'
	},{
		header:'销票时间', dataIndex:'handleTime', xtype:'datecolumn', format: "Y-m-d H:i:s", width : 150
	},{
		header:'处理人', dataIndex:'repairEmp'
	},{
		header:'销票站场', dataIndex:'handleSiteID', hidden:true
	},{
		header:'验收人编码', dataIndex:'accPersonId', hidden:true
	},{
		header:'验收人名称', dataIndex:'accPersonName', hidden:true
	},{
		header:'验收时间', dataIndex:'accTime', xtype:'datecolumn', hidden:true
	},{
		header:'整备单ID', dataIndex:'rdpIDX', hidden:true
	},{
		header:'检修类型', dataIndex:'repairClass',
        renderer: function(v){
            switch(v){
                case REPAIRCLASS_SX:
                    return "碎修";
                case REPAIRCLASS_lX:
                    return "临修";
                default:
                    return v;
            }
        }
	},{
		header:'是否跟踪', dataIndex:'isTracked', hidden:true,
        renderer: function(v){
            switch(v){
                case ISTRACKED_YES:
                    return "跟踪";
                case ISTRACKED_NO:
                    return "未跟踪";
                default:
                    return v;
            }
        }
	},{
		header:'返修次数', dataIndex:'repairTimes', width: 140
	}],
		toEditFn : function(){}
	});
	
	ZbglTpTrack.grid.store.on("beforeload", function(){	
        var searchParam = {};
        searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
		this.baseParams.recordIDX = ZbglTpTrack.recordIDX;
	});

		//超链接显示的窗口
	ZbglTpTrack.win = new Ext.Window({
		title:"跟踪单提票", 
	    layout: 'fit',
		modal: true, maximized: true ,
		items:ZbglTpTrack.grid,
		closable : true,
		plain:true,
		closeAction:"hide"
		//buttonAlign: 'center'
	});
	
		//提票跟踪记录单列表添加超链接，显示每次记录单所关联的jt6信息，将点击行的记录单idx传递到后台
	ZbglTpTrack.openRecordTpInfo = function(recordIDX){
		ZbglTpTrack.recordIDX = recordIDX;
		ZbglTpTrack.win.show();
		ZbglTpTrack.grid.store.load();
	}
});
