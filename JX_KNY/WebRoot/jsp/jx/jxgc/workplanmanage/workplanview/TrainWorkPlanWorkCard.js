/**
 * 作业卡列表 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('RQWorkCard');                       //定义命名空间
RQWorkCard.searchParams = {} ;                    //定义查询条件
RQWorkCard.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/workCard!pageQuery.action',                 //装载列表数据的请求URL
    storeAutoLoad:false,
    searchFormColNum:2,
    fieldWidth: 200,
    singleSelect: true,
    tbar:['search'],
	fields: [
	{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'工序卡主键', dataIndex:'workSeqCardIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'检修活动主键', dataIndex:'repairActivityIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'工艺流程节点实例主键', dataIndex:'nodeCaseIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'施修任务兑现单主键', dataIndex:'rdpIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'组成型号主键', dataIndex:'buildUpTypeIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'组成型号', dataIndex:'buildUpTypeName',hidden:true, editor:{  maxLength:50 }
	},{
		header:'作业编码', dataIndex:'workCardCode', editor:{  maxLength:50 },searcher:{  disabled:true}
	},{
        header:'作业名称', dataIndex:'workCardName',width:200, editor:{  maxLength:50 }
    },{
        header:'位置', dataIndex:'fixPlaceFullName',width:280, editor:{  maxLength:800 }
    },{
		header:'额定工时(分)', dataIndex:'ratedWorkHours',hidden:true, editor:{ xtype:'numberfield', maxLength:8 }
	},{
		header:'作业范围', dataIndex:'workScope',hidden:true, editor:{  maxLength:1000 }
	},{
		header:'安全注意事项', dataIndex:'safeAnnouncements',hidden:true, editor:{  maxLength:1000 }
	},{
		header:'互换配件信息主键', dataIndex:'partsAccountIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'互换配件型号主键', dataIndex:'partsTypeIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'零部件名称', dataIndex:'partsName',hidden:true,  editor:{  maxLength:100 }
	},{
		header:'零部件型号', dataIndex:'specificationModel',hidden:true,  editor:{  maxLength:100 }
	},{
		header:'零部件编号', dataIndex:'nameplateNo',hidden:true,  editor:{  maxLength:50 }
	},{
		header:'配件编号', dataIndex:'partsNo',hidden:true, editor:{  maxLength:50 }
	},{
		header:'作业类型', dataIndex:'workSeqClass',width:70, editor:{ 
			xtype: 'EosDictEntry_combo',
			hiddenName: 'workSeqClass',
			dicttypeid:'JXGC_WORKSEQ_WORKCLASS',
			displayField:'dictname',valueField:'dictid'
		},renderer: function(v){ 
				return EosDictEntry.getDictname('JXGC_WORKSEQ_WORKCLASS',v); //分类：细录、转向架。。
		}
	},{
		header:'质量检查', dataIndex:'qcName', editor:{  maxLength:50 }
	},{
		header:'作业人员', dataIndex:'worker', editor:{  maxLength:50 }
	},{
		header:'安装位置编码', dataIndex:'fixPlaceIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'安装位置编码全名', dataIndex:'fixPlaceFullCode',hidden:true, editor:{  maxLength:200 }
	},{
		header:'工位所属班组名称', dataIndex:'workStationBelongTeamName',hidden:true, editor:{  maxLength:50 }
	},{
		header:'工位所属班组', dataIndex:'workstationbelongteam',hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
	},{
		header:'工位主键', dataIndex:'workStationIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'工位名称', dataIndex:'workStationName',hidden:true, editor:{  maxLength:100 }
	},{
		header:'实际开工时间', dataIndex:'realBeginTime',hidden:true, xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'实际完工时间', dataIndex:'realEndTime',hidden:true, xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'状态', dataIndex:'status',width:70,editor:{  maxLength:64 },
			renderer:function(v){
				switch(v){
		    		case "INITIALIZE":
		    			return '<span style="color:red">未开放</span>';
		    		case "OPENED":
		    			return '<span style="color:red">待处理</span>';
		    		case "ONGOING":
		    			return '<span style="color:red">处理中</span>';
		    		case "COMPLETE":
		    			return "已处理";
		    		case "FINISHED":
		    			return '<span style="color:red">质检中</span>';
		    		case "TERMINATED":
		    			return '<span style="color:red">已终止</span>';
		    		default:
	    				return v;
    			}
			},searcher:{disabled:true}
	},{
		header:'备注', dataIndex:'remarks',hidden:true, editor:{ xtype:'textarea', maxLength:1000 }
	}],
	searchFn: function(searchParam){
		RQWorkCard.searchParams = searchParam ;
		RQWorkCard.grid.store.load();
	},
	toEditFn: function(grid, rowIndex, e){
		var record = grid.store.getAt(rowIndex);
		RQWorkCard.workCardIDX = record.get("idx");  //设置作业卡主键
        RQWorkCard.toWorkCardEditFn(grid, rowIndex, e);
    }
});


//作业工单编辑选项卡列表
RQWorkCard.editTabs = new Ext.TabPanel({
    activeTab: 0, frame:true,
    items:[{
            title: "基本信息", layout: "fit", border: false, items: [ RQWorkCard.layoutPanel ]
        },{
            title: "检测/修项目", layout: "fit", layout: "border",border: false, 
            items: [{
        		 region: 'west', layout: "fit",title:'检测/修项目',
        		 width: 700,minSize: 500, maxSize: 800, split: true, bodyBorder: false, items:[ RQWorkTask.grid ]
            },{
        		 region: 'center', layout: "fit",title:'检测结果',  bodyBorder: false, items : [ QRDetectResult.grid ]
            }]
        },{
            title: "质量检查", layout: "fit", border: false, items: [ QualityControlResult.grid ]            
        }
        ]
});

//作业工单信息查询结果窗口
RQWorkCard.workCardWin = new Ext.Window({
	title: "作业工单查看", maximizable:false, layout: "fit", 
	closeAction: "hide", modal: true, maximized: true , buttonAlign:"center",
	items: [RQWorkCard.editTabs],
	buttons:[{
		text: "关闭", iconCls: "closeIcon", handler: function(){RQWorkCard.workCardWin.hide();}
	}]
});

//加载前过滤
RQWorkCard.grid.store.on('beforeload',function(){
	var whereList = [] ;
	RQWorkCard.searchParams.rdpIDX = QR.rpdIdx ; //施修任务对象单主键
	for(porp in RQWorkCard.searchParams){
		if(porp == "workSeqClass"){
			var workSeqClass = RQWorkCard.searchParams[porp] ; //如果查询条件为分类时
			var sqlStr = " work_seq_class='"+workSeqClass+"'" ;
			whereList.push({sql: sqlStr, compare: Condition.SQL});
		}else{
			whereList.push({propName:porp, propValue: RQWorkCard.searchParams[porp] }) ; 
		}
	}
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	this.baseParams.sort = "workCardCode"; //服务端排序
	this.baseParams.dir = "ASC";
});
});