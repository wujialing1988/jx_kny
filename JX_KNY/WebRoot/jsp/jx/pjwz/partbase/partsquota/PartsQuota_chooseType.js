Ext.onReady(function(){
	//定义命名空间
	Ext.namespace("PartsType");
	
	//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
	PartsType.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	//表单组件高宽等设置
	PartsType.labelWidth = 100;
	PartsType.fieldWidth = 180;
	//查询参数对象
	PartsType.searchParam = {};
    //计量单位下拉数据
    PartsType.statueStates = [
        ['1', '台'],
        ['2', '个'],
        ['3', '张'],
        ['4', '条'],
        ['5', '斤']
    ];
	
	//数据容器
	PartsType.store = new Ext.data.JsonStore({
		id:"idx", root:"root", totalProperty:"totalProperty", autoLoad:true,
	    url: ctx + "/partsType!findListForQuota.action",
	    fields: [ "specificationModel","partsName","unit","timeLimit","limitKm","limitYears","status","recordStatus","siteID","creator","createTime","updator","updateTime","idx","partsClassIdx","className" ]
	});
	//选择模式，勾选框可多选
	PartsType.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
	
	//分页工具
	PartsType.pagingToolbar = Ext.yunda.createPagingToolbar({store: PartsType.store});
//	PartsType.store.on("beforeload", function(){
//		this.baseParams.entityJson = Ext.util.JSON.encode({'status':'1'});  
//	});
	PartsType.grid = new Ext.grid.GridPanel({
	    border: false,
	    //随着窗口（父容器）大小自动调整,true表示不出现滚动条，列宽会自动压缩
	//    viewConfig: {forceFit: true},
	    //该高度设置在IE、Google浏览器显示正常，在Opera显示不正常
	    height: document.documentElement.scrollHeight,
	    //可移动列
	    enableColumnMove: true,
	//    loadMask: {msg:"正在加载表格数据，请稍等..."},
	    //偶数行变色
	    stripeRows: true,
	    //多选行
	    selModel: PartsType.sm,
	    colModel: new Ext.grid.ColumnModel([
	        PartsType.sm,
	        new Ext.grid.RowNumberer(),
	        { sortable:true, header:"规格型号", dataIndex:"specificationModel" },			
	        { sortable:true, header:"配件名称", dataIndex:"partsName" },			
	        { sortable:true, header:"计量单位", dataIndex:"unit" },			
	        //{ sortable:true, header:"最大库存期限", dataIndex:"timeLimit" },			
	        //{ sortable:true, header:"最大走行公里", dataIndex:"limitKm" },			
	        //{ sortable:true, header:"最大使用年限", dataIndex:"limitYears" },			
	        { sortable:true, header:"业务状态", 
	          dataIndex:"status", 
	          renderer : function(value, metaData, record, rowIndex, colIndex, store){
	          	if(value==0) return "新增";
	          	if(value==1) return "启用";
	          	if(value==2) return "作废";
	          }
	        }		
	    ]),
	    store: PartsType.store,
	    //工具栏
//	    tbar: [{
//	        text:"刷新", iconCls:"refreshIcon",
//	        handler: function(){
//			    PartsType.store.load({
//			        params: {
//			        start: 0,    limit: PartsType.pagingToolbar.pageSize,
//			        partsClassIdx: PartsType.partsClassIdx,statue:PartsType.statue}
//			    });
//	        }
//		}],
	     tbar: [{
        xtype:"label", text:"  配件名称： " 
    },{
        xtype: "textfield",id:"PartsType_partsName",name:"partsName"
    },{
        text:"搜索", iconCls:"searchIcon", handler: function(){
            PartsType.searchParam = {'partsName':Ext.get("PartsType_partsName").getValue()};
		    PartsType.store.load({
		        params: {
		        start: 0,    limit: PartsType.pagingToolbar.pageSize,
		        entityJson: Ext.util.JSON.encode(PartsType.searchParam)}
		    });
        }        
    },"-",{
        text:"确定", iconCls:"saveIcon", handler: function(){
            var sm = PartsType.grid.getSelectionModel();
            if (sm.getCount() < 1) {
                MyExt.Msg.alert("尚未选择一条记录！");
                return;
            }
            Ext.Msg.confirm("提示  ", "确定添加所选项？  ", function(btn){
                if(btn == "yes"){
                    var data = sm.getSelections();
                    var ids = new Array();
                    for (var i = 0; i < data.length; i++){
                        ids.push(data[ i ].get("idx"));
                    }
                    //PartsQuota.win.hide();
                    PartsQuota.loadMask.show();
                    Ext.Ajax.request({
                        url: ctx + "/partsQuota!saveFromPartsType.action",
                        params: {ids: ids},
                        success: function(response, options){
                        	PartsQuota.loadMask.hide();
                            var result = Ext.util.JSON.decode(response.responseText);
                            if (result.errMsg == null) {
                                alertSuccess();
                                PartsQuota.store.reload(); 
                                PartsType.searchParam = {'partsName':Ext.get("PartsType_partsName").getValue()};
							    PartsType.store.load({
							        params: {
							        start: 0,    limit: PartsType.pagingToolbar.pageSize,
							        entityJson: Ext.util.JSON.encode(PartsType.searchParam)}
							    });
                            } else {
                                alertFail(result.errMsg);
                            }
                        },
                        failure: function(response, options){
                            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                        }
                    });
                }
            });
        }
    }],
	    bbar: PartsType.pagingToolbar
	});
});
