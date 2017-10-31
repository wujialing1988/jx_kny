/**
 * 检测项 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){

	rowEditor = new Ext.ux.grid.RowEditor({
    });

	Ext.namespace('DetectItemSearch');                       //定义命名空间
	DetectItemSearch.grid = new Ext.yunda.RowEditorGrid({
	    loadURL: ctx + '/detectResult!pageQuery.action',                 //装载列表数据的请求URL        
	    tbar:[{
		    	text:"刷新",iconCls:"refreshIcon" ,handler: function(){
		    		DetectItemSearch.grid.store.reload();
		    	}
		    }],
	    page: false,
	    storeAutoLoad: false,
	    singleSelect:true,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'工步主键', dataIndex:'workTaskIDX',hidden:true
		},{
			header:'检测项编码', dataIndex:'detectItemCode',width:50, editor:{disabled: true}
		},{
			header:'检测内容', dataIndex:'detectItemContent', editor:{disabled: true}
		},{
			header:'检测标准', dataIndex:'detectItemStandard',searcher: {disabled: true}, editor:{disabled: true}
		},{
			header:'值类型', dataIndex:'detectResulttype', width:50, editor:{disabled: true}
		},{
			header:'最小范围值', dataIndex:'minResult',  editor:{disabled: true}		
		},{
			header:'最大范围值', dataIndex:'maxResult',  editor:{disabled: true}		
		},{
			header:'是否必填', dataIndex:'isNotBlank', width:50, 
			renderer : function(v){if(v==isNotBlank_yes)return "必填";else if(v == isNotBlank_no) return "非必填"; else return ""},
			editor:{
				disabled: true,
				xtype: 'combo',
	            fieldLabel: '是否必填',
	            hiddenName:'isNotBlank',
	            store:new Ext.data.SimpleStore({
				    fields: ['v', 't'],
					data : [[isNotBlank_yes,'必填'],[isNotBlank_no,'非必填']]
				}),
				valueField:'v',
				displayField:'t',
				triggerAction:'all',
				mode:'local',
				value:isNotBlank_yes,
				editable: false,
				allowBlank: false
			}
	    }],
	    rowEditor:rowEditor
	});
	DetectItemSearch.grid.store.setDefaultSort('updateTime', 'DESC');//设置默认排序
	
	//添加过滤默认过滤信息
	DetectItemSearch.grid.store.on('beforeload',function(){
		var searchParam = {};
		searchParam.workTaskIDX = WorkStepEditSearch.idx ; //检修工序卡主键
		var whereList = [] ;
		for (prop in searchParam) {		
	        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});

});