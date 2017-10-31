/**
 * 机车对应整备范围列表查询 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('ZbFwTrainCenter');                       //定义命名空间
	ZbFwTrainCenter.searchParam = {};
	/*** 查询表单 start ***/
	ZbFwTrainCenter.searchLabelWidth = 90;
	ZbFwTrainCenter.searchAnchor = '95%';
	ZbFwTrainCenter.searchFieldWidth = 270;
	ZbFwTrainCenter.searchForm = new Ext.form.FormPanel({
		layout:"form", border:false, style:"padding:10px" ,
		labelWidth: ZbFwTrainCenter.searchLabelWidth, align:'center',baseCls: "x-plain",
		defaults:{anchor:"98%"}, buttonAlign:'center',
		items:[{
			xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
			items:[{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
				fieldWidth: ZbFwTrainCenter.searchFieldWidth, labelWidth: ZbFwTrainCenter.searchLabelWidth, defaults:{anchor:ZbFwTrainCenter.searchAnchor},
				items:[{ 
					fieldLabel: "车型",
					hiddenName: "trainTypeIDX",
					displayField: "shortName", valueField: "typeID",
					pageSize: 0, minListWidth: 200,
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
			                var trainNo_comb = ZbFwTrainCenter.searchForm.getForm().findField("trainNo");   
			                trainNo_comb.reset();  
			                trainNo_comb.clearValue(); 
			                trainNo_comb.queryParams.trainTypeIDX = this.getValue();
			                trainNo_comb.cascadeStore();	
			        	}   
			    	}
				}]
			},{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
				fieldWidth: ZbFwTrainCenter.searchFieldWidth, labelWidth: ZbFwTrainCenter.searchLabelWidth, defaults:{anchor:ZbFwTrainCenter.searchAnchor},
				items:[{
					id: "trainNo_comb_search",
					fieldLabel: "车号",
					hiddenName: "trainNo", 
					displayField: "trainNo", valueField: "trainNo",
					pageSize: 20, minListWidth: 200,
					minChars : 1,
	//				minLength : 4, 
					maxLength : 5,
	//				vtype: "numberInt",
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
					editable:true
				}]
			}]
		}],
		buttons:[{
				text:'查询', iconCls:'searchIcon', 
				handler: function(){ 
					var form = ZbFwTrainCenter.searchForm.getForm();	
			        var searchParam = form.getValues();
			        if (!Ext.isEmpty(Ext.get("trainNo_comb_search").dom.value)) {
						searchParam.trainNo = Ext.get("trainNo_comb_search").dom.value;
					}
	                searchParam = MyJson.deleteBlankProp(searchParam);
					ZbFwTrainCenter.grid.searchFn(searchParam); 
				}
			},{
	            text: "重置", iconCls: "resetIcon", handler: function(){ 
	            	var form = ZbFwTrainCenter.searchForm;
	            	form.getForm().reset();
	            	//清空自定义组件的值
	                var componentArray = ["Base_combo"];
	                for (var j = 0; j < componentArray.length; j++) {
	                	var component = form.findByType(componentArray[j]);
	                	if (!Ext.isEmpty(component) && Ext.isArray(component)) {
							for (var i = 0; i < component.length; i++) {
								component[i].clearValue();
							}						
						}	                    
	                }
	                var trainNo_comb = ZbFwTrainCenter.searchForm.getForm().findField("trainNo");   
	                delete trainNo_comb.queryParams.trainTypeIDX;
	                trainNo_comb.cascadeStore();	
	            	searchParam = {};
	            	ZbFwTrainCenter.grid.searchFn(searchParam);
	            }
			}]
	});
	
	/*** 查询表单 end ***/
	
	/*** 提票列表 start ***/
	ZbFwTrainCenter.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/zbfwTrainCenter!pageQuery.action',                 //装载列表数据的请求URL
	    viewConfig: null,
	    tbar:['refresh'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'整备范围名称', dataIndex:'fwName',width : 200
		},{
			header:'整备范围主键', dataIndex:'zbfwIDX', hidden:true
		},{
			header:'车型主键', dataIndex:'trainTypeIDX', hidden:true,width : 80
		},{
			header:'车型简称', dataIndex:'trainTypeShortName', width : 80
		},{
			header:'车号', dataIndex:'trainNo', width : 80
		},{
			header:'机车来源', dataIndex:'isThisSite', width : 80,
	        renderer: function(v){
	            switch(v){            	
	                case BD:
	                    return '本段';
	                case FBD:
	                    return '非本段';
	                default:
	                    return v;
	            }
	        }
		}],
	    searchFn: function(searchParam){
	    	ZbFwTrainCenter.searchParam = searchParam;
	    	this.store.load();
	    },
	    toEditFn: function(grid, rowIndex, e){
	    
	    	//获取当前选中行
	    	var record = grid.store.getAt(rowIndex);
	    	//传递车型
	    	EditZbFwWin.trainTypeIDX = record.data.trainTypeIDX
	    	//传递车号
			EditZbFwWin.trainNo = record.data.trainNo
			//传递中间表idx
			EditZbFwWin.zbfwTrianCenterIDX = record.data.idx
			
			//为lable标签赋值
			Ext.getCmp("trainNoShow").setText(EditZbFwWin.trainNo);
			Ext.getCmp("trainTypeShortNameShow").setText(record.data.trainTypeShortName);
			EditZbFwWin.grid.store.load();
			EditZbFwWin.selectWin.show();
	    }, 
	});
	ZbFwTrainCenter.grid.store.on("beforeload", function(){	
		var searchParam = ZbFwTrainCenter.searchParam;
		delete searchParam.status ;
		var whereList = [] ;
		for (prop in searchParam) {			
	        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	/*** 界面布局 start ***/
	ZbFwTrainCenter.panel = {
	    xtype: "panel", layout: "border", 
	    items: [{
	        region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
	        collapsible:true, height: 172, bodyBorder: false,
	        items:[ZbFwTrainCenter.searchForm], frame: true, title: "查询"
	    },{
	        region : 'center', layout : 'fit', bodyBorder: false, items : [ ZbFwTrainCenter.grid ]
	    }]
	};
	
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:ZbFwTrainCenter.panel });
});