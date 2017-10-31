Ext.onReady(function(){
	//定义命名空间
	Ext.namespace("PartsZzjhQuery");
	
	//数据容器
	PartsZzjhQuery.store = new Ext.data.JsonStore({
		autoLoad : true,
		root : "root",
		remoteSort : true,
		totalProperty : "totalProperty",
		fields : [ "idx","partsName", "trainno","repairclass","repairtime","wzmc","wzdm","xcpjbh","jhxcsj","sjxcsj","xcPartsAccountIdx","jhscrq","scPartsAccountIdx","workPlanIdx",
					"sjscrq","jhddrq","sjddrq","scpjbm","jhscsj","sjscsj","ywyy","ywsc","partsidx","traintype","offPartsListIdx","updateTime","updatorName","partsIdx"],
	    url: ctx + "/partsZzjh!pageList.action"
	});


	//选择模式，勾选框可多选
	PartsZzjhQuery.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
	//分页工具
	PartsZzjhQuery.pagingToolbar = Ext.yunda.createPagingToolbar({store: PartsZzjhQuery.store});
	//Grid
	PartsZzjhQuery.grid = new Ext.grid.GridPanel({
	    border: false,loadMask:true,
	    enableColumnMove: true,
	    loadMask: {msg:"正在加载表格数据，请稍等..."},
	    stripeRows: false,
	   	selModel: PartsZzjhQuery.sm,
	     colModel: new Ext.grid.ColumnModel([
	     	PartsZzjhQuery.sm,
	        new Ext.grid.RowNumberer(),
	        {  header:"ID", dataIndex:"idx",hidden:true },			
	        { sortable:true, header:"配件名称", dataIndex:"partsName",width:120},			
	        {  header:"机车号", dataIndex:"trainno" ,width:130,hidden:true,
	        renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
					return record.data.traintype + " " + record.data.trainno;
			}},			
			{  header:"修程修次", width:130,hidden:true,
				renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
					return record.data.repairclass + " " + record.data.repairtime;
			}},
	        {  header:"位置", dataIndex:"wzmc" ,width:80},			
	        {  header:"下车编号", dataIndex:"xcpjbh",width:80,
	           renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
		     		var html = "";	 
		  			html = "<span><a href='#' onclick='PartsAccountOrRdp.showPartsRdpOrDetail(\""+ value + "\",\""+ record.data.xcPartsAccountIdx +"\")'>"+value+"</a></span>";
		      		return value != null? html:"";
		      	}
		    },
	        { header:"上车编号", dataIndex:"scpjbm",width:80,
	       		renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
			     		var html = "";	 
			  			html = "<span><a href='#' onclick='PartsAccountOrRdp.showPartsRdpOrDetail(\""+ value + "\",\""+ record.data.scPartsAccountIdx +"\")'>"+value+"</a></span>";
			      		if(value != null){
				      		return html;
			      		}else{
			      			return null;
			      		}}
			      		},		   
	        { header:"下车计划", dataIndex:"jhxcsj",xtype: "datecolumn",format: "Y-m-d",width:90},
	        { header:"下车实际", dataIndex:"sjxcsj",xtype: "datecolumn",format: "Y-m-d",width:90},
	        { header:"上车计划", dataIndex:"jhscsj",xtype: "datecolumn",format: "Y-m-d",width:90},
	        { header:"上车实际", dataIndex:"sjscsj",xtype: "datecolumn",format: "Y-m-d",width:90},	        
	        { header:"送出计划", dataIndex:"jhscrq",xtype: "datecolumn",format: "Y-m-d",width:90},
	        { header:"送出实际", dataIndex:"sjscrq",xtype: "datecolumn",format: "Y-m-d",width:90},
	        { header:"到段计划", dataIndex:"jhddrq",xtype: "datecolumn",format: "Y-m-d",width:90},
	        { header:"到段实际", dataIndex:"sjddrq",xtype: "datecolumn",format: "Y-m-d",width:90},
	        { header:"延误时长", dataIndex:"ywsc",width:90},
	        { header:"延误原因", dataIndex:"ywyy",width:120,renderer: function(value, metaData, record, rowIndex, colIndex, store){
					metaData.attr = 'style="white-space:normal;"';
					return value;
				}
			},
	        { header:"修改人", dataIndex:"updatorName",width:90},
	        { header:"修改时间", dataIndex:"updateTime",xtype: "datecolumn",format: "Y-m-d",width:90}
	    ]),
	    store: PartsZzjhQuery.store,
	    bbar: PartsZzjhQuery.pagingToolbar   
	});
	// 默认以名称进行升序排序
	PartsZzjhQuery.grid.store.sort('partsName', 'ASC');
	PartsZzjhQuery.grid.store.on('beforeload', function(node){
		var searchParams = {};
		searchParams.workPlanIdx = PartsZzjhQuery.workPlanIdx; 
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
});