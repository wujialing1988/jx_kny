/**
 * 机车整备任务单--机车整备合格交验 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglRdpWi');                       //定义命名空间

ZbglRdpWi.imgStore = new Ext.data.ArrayStore({
    autoLoad: false,
    url:ctx + '/zbglRdpWi!getRdpWiDiImgs.action',
    fields: [
       {name: 'idx', type: 'string', mapping:'idx'},
       {name: 'attachmentSaveName', type: 'string', mapping:'attachmentSaveName'},
       {name: 'url', type: 'string'}
    ]
});

ZbglRdpWi.imgScale = function(ThisPic){
    var RePicWidth = 120; //这里修改为您想显示的宽度值

    //============以下代码请勿修改==================================

    var TrueWidth = ThisPic.width;    //图片实际宽度
    var TrueHeight = ThisPic.height;  //图片实际高度
    var Multiple = TrueWidth / RePicWidth;  //图片缩小(放大)的倍数

    ThisPic.width = RePicWidth;  //图片显示的可视宽度
    ThisPic.height = TrueHeight / Multiple;  //图片显示的可视高度
}

ZbglRdpWi.addImg = function(){
	var imgAtt = Ext.getCmp('imgAtt');
	
	ZbglRdpWi.imgStore.load({
		params : {
			wiIdx : ZbglRdpWidi.rdpWiIDX
		},
		callback : function(records) {
			imgAtt.removeAll();
			
			var a_marks = '';
			if (records.length > 0) {
				for (var i = 0; i < records.length; i++) {
					//records[i].set('url', ctx + '/attachment!whowImg.action?id=' + records[i].get('idx'));
					a_marks += '<a style="padding: 5px;" href="'+ ctx +'/attachment!whowImg.action?id=' + records[i].get('idx') + '" data-lightbox="imgSet_' + ZbglRdpWidi.rdpWiIDX + '"><img onload="ZbglRdpWi.imgScale(this);" src="'+ ctx +'/attachment!whowImg.action?id=' + records[i].get('idx') + '" /></a>';
				}
			}
			else {
				a_marks = '<span>没有照片</span>';
			}
			
			imgAtt.update(
			    '<div style="float: left;">'
			    + a_marks
			    + '</div>'
			);
		}
	});
}

ZbglRdpWi.playAudio = function() {
	var audioPanel = Ext.getCmp('audioPanel');
    Ext.Ajax.request({
	    url: ctx + '/zbglRdpWi!getRdpWiDiAudio.action',
	    params: { wiIdx : ZbglRdpWidi.rdpWiIDX },
	    success: function(response){
    		audioPanel.removeAll();
    		
    		var html = '';
	    	var result = Ext.util.JSON.decode(response.responseText);
	    	if (result && result.length > 0) {
	    		html = '<img src="' + ctx 
                + '/frame/resources/images/toolbar/control_play_blue.png" border="0" style="cursor:hand" onclick="ZbglRdpWiAudio.play(\'' + result[0].idx + '\');">';
	    	}
	    	else {
	    		html = '<span>没有录音</span>';
	    	}
	    	
	    	audioPanel.update(html);
	    }
	});
}

ZbglRdpWi.searchParam = {};
ZbglRdpWi.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglRdpWi!findPageQueryForWeb.action',                 //装载列表数据的请求URL
    storeAutoLoad: false,
    singleSelect: true,
    tbar:[],
    viewConfig: { 
    	getRowClass : function(record, rowIndex, p, store){ 
    		if(record.get('checkPictureStatus') == 1){
				return 'RdpWi_row_red';
    		}else if(record.get('checkPictureStatus') == 2){
				return 'RdpWi_row_green';
    		}
        } 
    },
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'整备单ID', dataIndex:'rdpIDX', hidden:true, editor:{  maxLength:50 }
	},{
		header:'任务单状态', dataIndex:'wiStatus', editor:{  maxLength:20 },
        renderer: function(v){
            switch(v){
            	case RDPWI_STATUS_TODO:
                    return "未处理";
                case RDPWI_STATUS_HANDLING:
                    return "处理中";
                case RDPWI_STATUS_HANDLED:
                    return "已处理";
                default:
                    return v;
            }
        }
	},{
		header:'任务单类型', dataIndex:'wiClass', editor:{  maxLength:20 },
        renderer: function(v){
            switch(v){
            	case WICLASS_ZBFW:
                    return WICLASS_ZBFW_CH;
                case WICLASS_ZLCS:
                    return WICLASS_ZLCS_CH;
                case WICLASS_YJ:
                    return WICLASS_YJ_CH;
                default:
                    return v;
            }
        }, width: 150
	},{
		header:'作业项目ID', dataIndex:'wiIDX', hidden:true, editor:{  maxLength:50 }
	},{
		header:'任务编号', dataIndex:'wiCode', hidden:true, editor:{  maxLength:50 }
	},{
		header:'任务单名称', dataIndex:'wiName', editor:{  maxLength:100 }, width: 150
	},{
		header:'任务描述', dataIndex:'wiDesc', editor:{  maxLength:500 }, width: 300
	},{
		header:'作业班组代码', dataIndex:'handleOrgID', hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
	},{
		header:'作业班组名称', dataIndex:'handleOrgName', hidden:true, editor:{  maxLength:200 }
	},{
		header:'作业人编码', dataIndex:'handlePersonID', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'作业人员', dataIndex:'handlePersonName', editor:{  maxLength:25 }
	},{
		header:'联合作业人员', dataIndex:'worker', editor:{  maxLength:25 }
	},{
		header:'领活时间', dataIndex:'fetchTime', xtype:'datecolumn', format: "Y-m-d H:i:s", width: 150
	},{
		header:'销活时间', dataIndex:'handleTime', xtype:'datecolumn', format: "Y-m-d H:i:s", width: 150
	},{
		header:'顺序号', dataIndex:'seqNo', hidden:true, editor:{ xtype:'numberfield', maxLength:3 }
	},{
		header:'是否核实照片', dataIndex:'isCheckPicture', hidden:true
	},
	// 0：未核实不显示颜色；1：核实，颜色红色即未上传附件 2：核实，颜色绿色即已经上传附件；
	{
		header:'照片核实状态', dataIndex:'checkPictureStatus', hidden:true
	}],
	toEditFn: function(grid, rowIndex, e) {
		var record = grid.getStore().getAt(rowIndex);
		if(!ZbglRdpWi.ZbglRdpWidiWin){
			ZbglRdpWi.ZbglRdpWidiWin = new Ext.Window({
	            title:"整备工单",width:800, height:600, plain:true, closeAction:"hide",  maximizable:true,modal:true,
	            layout:'border',
	            defaults:{border:false},
	            items:[{
	            	region:'north',height: 372, layout:'border',xtype: 'panel',border:false,
	            	items:[{
		            	region:'north',title:'照片列表', height: 320, layout:'fit', frame:true ,xtype: 'panel' ,id:'imgAtt',autoScroll:true
		            }, {
		            	region:'center',title:'录音', height: 50, layout:'fit', frame:true ,xtype: 'panel' ,id:'audioPanel'
	            }]
	            }, {
	            	region:'center',  layout:'fit', items: ZbglRdpWidi.grid
	            }]
	     });
		}
		
	    ZbglRdpWidi.rdpWiIDX = record.get('idx');
		ZbglRdpWidi.grid.store.load();
		ZbglRdpWi.ZbglRdpWidiWin.show();
		
		ZbglRdpWi.addImg();
		ZbglRdpWi.playAudio();
	},
    searchFn: function(searchParam){
    	ZbglRdpWi.searchParam = searchParam;
    	this.store.load();
    }
});
ZbglRdpWi.grid.store.on("beforeload", function(){	
	var searchParam = ZbglRdpWi.searchParam;
	var whereList = [] ;
	for (prop in searchParam) {			
        if(prop == 'rdpIDX'){
			whereList.push({propName:prop, propValue: searchParam[prop], stringLike: false}) ;
		}else{
        	whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
	}
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});
});
