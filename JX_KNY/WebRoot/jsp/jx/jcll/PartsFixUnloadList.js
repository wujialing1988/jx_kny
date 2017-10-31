Ext.onReady(function(){
	//定义命名空间
	Ext.namespace("PartsFixUnloadList");

	// 加载上下车配件信息
	PartsFixUnloadList.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsZzjh!findFixUnloadList.action',                 //装载列表数据的请求URL
	    storeAutoLoad:false, viewConfig: null,
	    singleSelect: true,
	    enableColumnMove: true,
     	tbar: [{text : "刷新", iconCls : "refreshIcon",  width: 40,
				handler : function(){
					PartsFixUnloadList.grid.store.load();
				}
			}],
		fields: [{  
			header:"ID", dataIndex:"idx",hidden:true },			
	        {  header:"配件名称", dataIndex:"partsName",width:180},				  			  
	        {  header:"位置编码", dataIndex:"wzdm", hidden:true, width:80},			
	        {  header:"位置", dataIndex:"wzmc" ,width:100},	
          	{  header:"下车配件规格型号", dataIndex:"unloadSpecificationModel",width:180},	
	        {  header:"下车编号", dataIndex:"unloadPartsNo",width:130,
	        	renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
		     		var html = "";	 
		  			html = "<span><a href='#' onclick='PartsAccountOrRdp.showPartsRdpOrDetail(\""+ value + "\",\""+ record.data.unloadPartsAccountIdx +"\")'>"+value+"</a></span>";
			      	return value != null? html:"";
		    }},
	    
	        { header:"下车时间", dataIndex:"unloadDate",xtype: "datecolumn",format: "Y-m-d",width:150},
	     	{  header:"上车配件规格型号", dataIndex:"aboardSpecificationModel",width:180},	
	        { header:"上车编号", dataIndex:"aboardPartsNo",width:130,
	       		renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
			     		var html = "";	 
			  			html = "<span><a href='#' onclick='PartsAccountOrRdp.showPartsRdpOrDetail(\""+ value + "\",\""+ record.data.aboardPartsAccountIdx +"\")'>"+value+"</a></span>";
			      		if(value != null){
				      		return html;
			      		}else{
			      			return null;
			      		}}
			      	
	    
	   	},{ header:"上车时间", dataIndex:"aboardDate",xtype: "datecolumn",format: "Y-m-d",width:150
	   	},{ header:"类型", dataIndex:"partsStatus",width:150
        },{ header:"上车配件台账", dataIndex:"aboardPartsAccountIdx",hidden:true 
	    },{	header:"下车配件台账", dataIndex:"unloadPartsAccountIdx",hidden:true 	
	    }],
	    toEditFn: Ext.emptyFn
	});
	
	PartsFixUnloadList.grid.store.on('beforeload', function(node){
		var searchParams = {};
		searchParams.workPlanIDX = PartsFixUnloadList.rpdIdx; 
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
});