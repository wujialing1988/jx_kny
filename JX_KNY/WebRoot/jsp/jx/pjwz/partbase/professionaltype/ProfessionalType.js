/**
 * 专业类型 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
//定义命名空间
Ext.namespace("ProfessionalType");
//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
ProfessionalType.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
//表单组件高宽等设置
ProfessionalType.labelWidth = 200;
ProfessionalType.fieldWidth = 300;
ProfessionalType.addlabelWidth = 100;
ProfessionalType.addfieldWidth = 180;
//新增信息表单
ProfessionalType.form = new Ext.form.FormPanel({
    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false,		style: "padding:20px",		labelWidth: ProfessionalType.addlabelWidth,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:ProfessionalType.addlabelWidth,
            columnWidth: 1, 
            items: [
				{ id:"professionalTypeIDForm", name:"professionalTypeID", fieldLabel:"类型编码",maxLength: 50, allowBlank:false,width:ProfessionalType.addfieldWidth },
				{ id:"professionalTypeNameForm", name:"professionalTypeName", fieldLabel:"类型名称",maxLength: 100, allowBlank:false,width:ProfessionalType.addfieldWidth },
				{ 
					xtype: 'radiogroup',
		            fieldLabel: '是否子类',
		            width:120,
		            items: [
		                {id:"yForm", boxLabel: '是', name: 'isLeaf', width:30, inputValue: 1, checked : true },
		                {id:"nForm", boxLabel: '否', name: 'isLeaf', width:30, inputValue: 0}
		            ]
				}
            ]
        },
        {xtype:"hidden", id:"parentIDXForm", name:"parentIDX", value:"0"},
        {xtype:"hidden", id:"idxForm", name:"idx"},
        {xtype:"hidden", id:"statusForm", name:"status" },
        {xtype:"hidden", id:"proSeqForm", name:"proSeq" }
        ]
    }]
        
});
//新增编辑窗口
ProfessionalType.win = new Ext.Window({
    title:"新增", maximizable:true, width:380, height:260,
    plain:true, closeAction:"hide", items:ProfessionalType.form,
    buttonAlign:"center",
    buttons: [{
        text:"保存", iconCls:"saveIcon",
        handler:function(){
            var form = ProfessionalType.form.getForm();
            if (!form.isValid()) return;
            var url = ctx + "/professionalType!saveOrUpdate.action";
//            Ext.getCmp("parentIDXForm").setValue(ProfessionalType.currentNodeId);
            var proSeq_v = ProfessionalType.proSeq;
            var data = form.getValues();
            proSeq_v = proSeq_v + data.professionalTypeID + ".";
            data.status = 0 ;  //新增时设置状态值为0
            if(data.parentIDX == ""){
            	data.parentIDX = ProfessionalType.currentNodeId ; 
            }
            if(data.proSeq == ""){
            	data.proSeq = proSeq_v ; //序列为上级序列+编码+“.”
            }
            ProfessionalType.loadMask.show();
            Ext.Ajax.request({
                url: url,
                jsonData: data,
                success: function(response, options){
                  	ProfessionalType.loadMask.hide();
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.errMsg == null) {
                        alertSuccess();
                        ProfessionalType.store.reload();
		                ProfessionalType.tree.root.reload();
		                ProfessionalType.tree.getRootNode().expand();                        
                    } else {
                        alertFail(result.errMsg);
                    }
                },
                failure: function(response, options){
                    ProfessionalType.loadMask.hide();
                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                }
            });
        }
    },{
    	 text:"关闭", iconCls:"closeIcon",handler:function(){
    	 	ProfessionalType.win.hide();
    	 }
    }]
});
//数据容器
ProfessionalType.store = new Ext.data.JsonStore({
	id:"idx", root:"root", totalProperty:"totalProperty", autoLoad:true,
    url: ctx + "/professionalType!list.action",
    fields: [ "professionalTypeID","professionalTypeName","parentIDX","status","isLeaf","idx","proSeq" ]
});
//分页工具
ProfessionalType.pagingToolbar = Ext.yunda.createPagingToolbar({store: ProfessionalType.store});
//选择模式，勾选框可多选
ProfessionalType.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
//人员列表
ProfessionalType.grid = new Ext.grid.GridPanel({
    border: false,loadMask:true,
    //随着窗口（父容器）大小自动调整,true表示不出现滚动条，列宽会自动压缩
    viewConfig: {forceFit: true},
    //该高度设置在IE、Google浏览器显示正常，在Opera显示不正常
    height: document.documentElement.scrollHeight,
    //可移动列
    enableColumnMove: true,
//    loadMask: {msg:"正在加载表格数据，请稍等..."},
    //偶数行变色
    stripeRows: true,
    //多选行
    selModel: ProfessionalType.sm,
    colModel: new Ext.grid.ColumnModel([
        ProfessionalType.sm,
        new Ext.grid.RowNumberer(),
        { sortable:true, header:"类型编码", dataIndex:"professionalTypeID" },			
        { sortable:true, header:"类型名称", dataIndex:"professionalTypeName" },
        { sortable:true, header:"业务状态", 
          dataIndex:"status", 
          renderer : function(value, metaData, record, rowIndex, colIndex, store){
          	if(value==0) return "新增";
          	if(value==1) return "启用";
          	if(value==2) return "作废";
          }
        },
        {header:"是否子类", dataIndex:"isLeaf" , hidden:true}
    ]),
    store: ProfessionalType.store,
    //分页栏
    bbar: ProfessionalType.pagingToolbar,
    //工具栏
    tbar: [{
        text:"新增", iconCls:"addIcon",
        handler: function(){
        	ProfessionalType.searchWin.hide();
            ProfessionalType.form.getForm().reset();
            ProfessionalType.win.setTitle("新增");
            ProfessionalType.win.show();
        }
    },{
        text:"启用", iconCls:"acceptIcon",
        handler: function(){
        	var sm = ProfessionalType.grid.getSelectionModel();
            if (sm.getCount() < 1) {
                MyExt.Msg.alert("尚未选择一条记录！");
                return;
            }
            var data = sm.getSelections();
            var ids = new Array();
            var flag = new Array(); //标记选择项目
            for (var i = 0; i < data.length; i++){
            	if(data[i].data.status==0){
	                ids.push(data[ i ].get("idx"));
            	}else{
            		 flag.push(data[i]);
            	}
            }
            if(ids.length <= 0){
            	MyExt.Msg.alert("只能【启用】新增状态的记录！");
            	return;
            }
			Ext.Msg.confirm('提示',alertOperate(flag,'是否继续启用，新增的项！</br>继续将级联启用父级节点！','start'), function(btn){            	
                if(btn == "yes"){
                    ProfessionalType.win.hide();
                    Ext.Ajax.request({
                        url: ctx + "/professionalType!startUse.action",
                        params: {ids: ids},
                        success: function(response, options){
                            var result = Ext.util.JSON.decode(response.responseText);
                            if (result.errMsg == null) {
                                alertSuccess();
                                ProfessionalType.store.reload();  
                                ProfessionalType.tree.root.reload();
          						ProfessionalType.tree.getRootNode().expand();	
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
    },{
        text:"作废", iconCls:"dustbinIcon",
        handler: function(){
        	var sm = ProfessionalType.grid.getSelectionModel();
            if (sm.getCount() < 1) {
                MyExt.Msg.alert("尚未选择一条记录！");
                return;
            }
            var data = sm.getSelections();
            var ids = new Array();
            var flag = new Array(); //标记选择的项目
            for (var i = 0; i < data.length; i++){
            	if(data[i].data.status==1){
	                ids.push(data[ i ].get("idx"));
            	}else{
            		 flag.push(data[i]);
            	}
            }
            if(ids.length <= 0){
            	MyExt.Msg.alert("只能【作废】启用状态的记录！");
            	return;
            }
			Ext.Msg.confirm('提示',alertOperate(flag,'是否继续作废，启用的项！</br>继续将级联作废下级节点！','invalid'), function(btn){            	
                if(btn == "yes"){
                    ProfessionalType.win.hide();
                    Ext.Ajax.request({
                        url: ctx + "/professionalType!invalid.action",
                        params: {ids: ids},
                        success: function(response, options){
                            var result = Ext.util.JSON.decode(response.responseText);
                            if (result.errMsg == null) {
                                alertSuccess();
                                ProfessionalType.store.reload();  
                                ProfessionalType.tree.root.reload();
          						ProfessionalType.tree.getRootNode().expand();	
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
    },{
        text:"删除", iconCls:"deleteIcon",
        handler: function(){
            var sm = ProfessionalType.grid.getSelectionModel();
            if (sm.getCount() < 1) {
                MyExt.Msg.alert("尚未选择一条记录！");
                return;
            }
            var data = sm.getSelections();
            var ids = new Array();
            var flag = new Array(); //标记选择的项目
            for (var i = 0; i < data.length; i++){
            	if(data[i].data.status==0){
	                ids.push(data[ i ].get("idx"));
            	}else{
            		 flag.push(data[i]);
            	}
            }
            if(ids.length <= 0){
            	MyExt.Msg.alert("只能【删除】新增状态的记录！");
            	return;
            }
//            Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否继续？", function(btn){   //????btn
			Ext.Msg.confirm('提示',alertOperate(flag,'是否继续删除，新增的项！</br>继续将级联删除下级节点！','del'), function(btn){            	
                if(btn == "yes"){
                    ProfessionalType.win.hide();
                    Ext.Ajax.request({
                        url: ctx + "/professionalType!logicDelete.action",
                        params: {ids: ids},
                        success: function(response, options){
                            var result = Ext.util.JSON.decode(response.responseText);
                            if (result.errMsg == null) {
                                alertSuccess();
                                ProfessionalType.store.reload();  
                                ProfessionalType.tree.root.reload();
          						ProfessionalType.tree.getRootNode().expand();	
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
    },{
        text:"刷新", iconCls:"refreshIcon",
        handler: function(){self.location.reload(); }
    },"-","状态： ",
    {   xtype:"checkbox", id:"addStatus", boxLabel:"新增&nbsp;&nbsp;&nbsp;&nbsp;", checked:true ,
    	handler:function(){
    		ProfessionalType.checkQuery();
    	}
    },
    {   xtype:"checkbox", id:"startStatus", boxLabel:"启用&nbsp;&nbsp;&nbsp;&nbsp;", checked:true ,
    	handler:function(){
    		ProfessionalType.checkQuery();
    	}
    },
    {   xtype:"checkbox", id:"trashStatus", boxLabel:"作废" ,
    	handler:function(){
    		ProfessionalType.checkQuery();
    	}
    }
    ],
    listeners: {
        "rowdblclick": {
            fn: function(grid, idx, e){
            	ProfessionalType.searchWin.hide();
                var record = grid.store.getAt(idx);
                if(record.get("status")!=0){
                	MyExt.Msg.alert("只能【修改】新增的记录");
                	return;
                }
                ProfessionalType.win.setTitle("编辑");
                ProfessionalType.win.show();
                ProfessionalType.form.getForm().reset();
                ProfessionalType.form.getForm().loadRecord(record);
            }
        }
    }       
});
//查询参数表单
ProfessionalType.searchForm = new Ext.form.FormPanel({
    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false,		style: "padding:10px",		labelWidth: ProfessionalType.labelWidth,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:ProfessionalType.labelWidth,
            columnWidth: 1, 
            items: [
				{ name:"professionalTypeID", fieldLabel:"类型编码",width: ProfessionalType.fieldWidth },
				{ name:"professionalTypeName", fieldLabel:"类型名称",width: ProfessionalType.fieldWidth }
            ]
        }
        ]
    }]
});
//查询参数对象
ProfessionalType.searchParam = {};
//查询窗口
ProfessionalType.searchWin = new Ext.Window({
    title:"查询数据", items:ProfessionalType.searchForm, iconCls:"searchIcon",
    width: 350, height: 170, plain: true, closeAction: "hide",
    buttons: [{
        id: "searchBtn", text: "查询", iconCls: "searchIcon",
        handler: function(){  
		    ProfessionalType.searchParam = ProfessionalType.searchForm.getForm().getValues();
		    var searchParam = ProfessionalType.searchForm.getForm().getValues();
		    ProfessionalType.store.load({
		        params: {entityJson: Ext.util.JSON.encode(searchParam)}
		    });        
        }
    }, {
        text:"重置", iconCls:"resetIcon",
        handler:function(){	ProfessionalType.searchForm.getForm().reset(); }
    }]
});
//当前点击的树节点id
ProfessionalType.currentNodeId = "ROOT_0";
ProfessionalType.proSeq = ".";     //当前点击树节点的序列
//专业类型树 
ProfessionalType.tree = new Ext.tree.TreePanel( {
    loader : new Ext.tree.TreeLoader( {
        dataUrl : ctx + "/professionalType!tree.action"
    }),
    root: new Ext.tree.AsyncTreeNode({
        text: '专业类型根节点',
        id: "ROOT_0",
        leaf: false
    }),
    rootVisible: true,
    autoScroll : false,
    animate : false,
    useArrows : false,
    border : false,
    listeners: {
        click: function(node, e) {
        	ProfessionalType.currentNodeId = node.id ;
            if(node.id=="ROOT_0"){
            	//初始化动作
				ProfessionalType.tabs.activate("childTypeTab");
				ProfessionalType.tabs.hideTabStripItem("baseinfoTab");
				ProfessionalType.tabs.unhideTabStripItem("childTypeTab");
				ProfessionalType.store.load();
            }else{
            	ProfessionalType.tabs.unhideTabStripItem("baseinfoTab");
				ProfessionalType.tabs.setActiveTab(0);
				ProfessionalType.proSeq = node.attributes.proSeq;
            }
            Ext.getCmp("idx").setValue(node.id);
            Ext.getCmp("professionalTypeID").setValue(node.attributes.professionalTypeID);
            Ext.getCmp("professionalTypeName").setValue(node.text);
            Ext.getCmp("typeStatus").setValue(node.attributes.status);
            Ext.getCmp("parentIDX").setValue(node.attributes.parentIDX);
            Ext.getCmp("proSeq").setValue(node.attributes.proSeq);
            if(node.attributes.status==0){
	            Ext.getCmp("status").setValue("新增");
	            Ext.getCmp("professionalTypeID").enable();
	            Ext.getCmp("professionalTypeName").enable();
	            Ext.getCmp("y").enable();
	            Ext.getCmp("n").enable();
	            Ext.getCmp("submitBtn").setVisible(true);
            }
            if(node.attributes.status==1){
	            Ext.getCmp("status").setValue("启用");
	            Ext.getCmp("professionalTypeID").disable();
	            Ext.getCmp("professionalTypeName").disable();
	            Ext.getCmp("y").disable();
	            Ext.getCmp("n").disable();
	            Ext.getCmp("submitBtn").setVisible(false);
            }
			if(node.attributes.isLeaf==1){  //为子类的情况
            	document.getElementById("y").checked = true;
            	ProfessionalType.tabs.hideTabStripItem("childTypeTab"); //如果是子类则隐藏下级分类
            }
            if(node.attributes.isLeaf==0){ //非子类情况
            	document.getElementById("n").checked = true;
            	ProfessionalType.tabs.unhideTabStripItem("childTypeTab");
//            	document.getElementById("addStatus").checked = true;
//            	document.getElementById("startStatus").checked = true;
//            	document.getElementById("trashStatus").checked = false;
            	var searchParam = {parentIDX:node.id};
            	if("ROOT_0"==node.id){
            		searchParam = {};
            	}
            	ProfessionalType.store.load({
			        params: {entityJson: Ext.util.JSON.encode(searchParam)}
			    });
            }
        }
    }    
});
ProfessionalType.tree.on('beforeload', function(node){
    ProfessionalType.tree.loader.dataUrl = ctx + '/professionalType!tree.action?parentIDX=' + node.id;
});

//baseFrom 表单
ProfessionalType.baseForm = new Ext.form.FormPanel({
    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false,		style: "padding:10px",		labelWidth: ProfessionalType.labelWidth,
    frame:true,
    buttonAlign:"center",
      buttons: [{
        id:"submitBtn", text:"保存", iconCls:"saveIcon",
        handler:function(){
            var form = ProfessionalType.baseForm.getForm();
            if (!form.isValid()) return;
            var url = ctx + "/professionalType!saveOrUpdate.action";
//            Ext.getCmp("parentIDX").setValue(ProfessionalType.currentNodeId);
            var data = form.getValues();
            ProfessionalType.loadMask.show();
            Ext.Ajax.request({
                url: url,
                jsonData: data,
                success: function(response, options){
                  	ProfessionalType.loadMask.hide();
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.errMsg == null) {
                    	if(data.isLeaf==1){
                        	ProfessionalType.tabs.hideTabStripItem("childTypeTab"); //如果是子类则隐藏下级分类
                        }
                        if(data.isLeaf==0){
                        	ProfessionalType.tabs.unhideTabStripItem("childTypeTab"); //如果是子类则显示下级分类
                        	var searchParam = {parentIDX:data.idx};
			            	ProfessionalType.store.load({
						        params: {entityJson: Ext.util.JSON.encode(searchParam)}
						    }); 
                        }
                        alertSuccess();
                        ProfessionalType.store.reload();
		                ProfessionalType.tree.root.reload();
		                ProfessionalType.tree.getRootNode().expand();                        
                    } else {
                        alertFail(result.errMsg);
                    }
                },
                failure: function(response, options){
                    ProfessionalType.loadMask.hide();
                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                }
            });
        }
    }],
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:ProfessionalType.labelWidth,
            columnWidth: 1, 
            items: [
				{ id:"professionalTypeID", name:"professionalTypeID", fieldLabel:"类型编码", allowBlank:false,width:ProfessionalType.fieldWidth },
				{ id:"professionalTypeName", name:"professionalTypeName", fieldLabel:"类型名称", allowBlank:false,width:ProfessionalType.fieldWidth },
				{ 
					xtype: 'radiogroup',
		            fieldLabel: '是否子类',
		            width:120,
		            items: [
		                {id:"y", boxLabel: '是', name: 'isLeaf', inputValue: 1, checked : true},
		                {id:"n", boxLabel: '否', name: 'isLeaf', inputValue: 0}
		            ]
				},
				{ id:"status", fieldLabel: '专业类型状态',disabled:true ,width:ProfessionalType.fieldWidth }
            ]
        },
        {xtype:"hidden", id:"parentIDX", name:"parentIDX", value:"0"},
        {xtype:"hidden", id:"typeStatus", name:"status"},
        {xtype:"hidden", id:"idx", name:"idx"},
        {xtype:"hidden", id:"proSeq", name:"proSeq"}
        
        ]
    }]
        
});

//tab选项卡布局
ProfessionalType.tabs = new Ext.TabPanel({
    activeTab: 0,
    frame:true,
    items:[{  
           id: "baseinfoTab",
           title: '基本信息',
           layout:'fit',
           frame:true,
           items: [ProfessionalType.baseForm]
        },{ 
          id: "childTypeTab",
          title: '下级专业类型',
          layout: 'fit' ,
          items: [ProfessionalType.grid]
       }]
});

//页面布局
var viewport = new Ext.Viewport( {
    layout : 'border',
    items : [ {
        title : '<span style="font-weight:normal">专业类型树</span>',
        iconCls : 'chart_organisationIcon',
        tools : [ {
            id : 'refresh',
            handler : function() {
                ProfessionalType.tree.root.reload();
                ProfessionalType.tree.getRootNode().expand();
            }
        } ],
        collapsible : true,
        width : 210,
        minSize : 160,
        maxSize : 280,
        split : true,
        region : 'west',
        bodyBorder: false,
        autoScroll : true,
//        collapseMode:'mini',
        items : [ ProfessionalType.tree ]
    }, {
        region : 'center',
        layout : 'fit',
        bodyBorder: false,
        items : [ ProfessionalType.tabs ]
    } ]
});
ProfessionalType.tree.getRootNode().expand();

ProfessionalType.tabs.activate("childTypeTab");
ProfessionalType.tabs.hideTabStripItem("baseinfoTab");
ProfessionalType.tabs.unhideTabStripItem("childTypeTab");


/**
 * checkQuery()
 * 检查并获取查询数据
 * */
ProfessionalType.checkQuery =  function (){
	ProfessionalType.status = "-1";
	if(Ext.getCmp("addStatus").checked){
		ProfessionalType.status=ProfessionalType.status+',0';
	}
	if(Ext.getCmp("startStatus").checked){
		ProfessionalType.status=ProfessionalType.status+',1';
	}
	if(Ext.getCmp("trashStatus").checked){
		ProfessionalType.status=ProfessionalType.status+',2';
	}
	ProfessionalType.store.load();
}

//全局的业务状态，用于条件查询参数
ProfessionalType.status = "";
//store载入前查询
ProfessionalType.store.on("beforeload", function(){
	var idx = ProfessionalType.currentNodeId ;
	var searchParam = ProfessionalType.searchParam;
	searchParam.parentIDX = idx;
	if("ROOT_0"==idx){
    	searchParam = {};
    }
	this.baseParams.status = ProfessionalType.status;  
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

});


/**
 * @param infoArray 遍历的数组
 * @param msg 显示的字符串说明
 * */
alertOperate = function(infoArray,msg,status){
	var info = "";
	var msgInfo = "" ;
	var operInfo = "" ; //启用消息
	var invalidInfo = "" ; //作废消息
	if(status=='del'){
		msgInfo = "该操作将不能恢复，是否继续【删除】？";
		operInfo = "不能删除";
		invalidInfo = "不能删除";
	}
	if(status=='start'){
		msgInfo = "确定【启用】选择的项？";
		operInfo = "";
		invalidInfo = "不能启用"
	}
	if(status=='invalid'){
		msgInfo = "确定【作废】选择的项？";
		operInfo = "不能作废";
		invalidInfo = "";
	}
	var titleInfo = "";
	if(infoArray instanceof Array){
		for(var i = 0; i < infoArray.length; i++){
			if(infoArray[ i ].get("status") == 0){
				info += (i + 1) + ". 【" + infoArray[ i ].get("professionalTypeName") + "】为新增"+operInfo+"！</br>";
				msgInfo = msg;
				titleInfo = "选择的记录！";
			}
			if(infoArray[ i ].get("status") == 1){
				info += (i + 1) + ". 【" + infoArray[ i ].get("professionalTypeName") + "】已经启用"+operInfo+"！</br>";
				msgInfo = msg;
				titleInfo = "选择的记录！";
			}
			if(infoArray[ i ].get("status") == 2){
				info += (i + 1) + ". 【" + infoArray[ i ].get("professionalTypeName") + "】已经作废"+invalidInfo+"！</br>";
				msgInfo = msg;
				titleInfo = "选择的记录！";
			}
		}
	} else {
		info = infoArray;
	}
	return   titleInfo + '</br>' + info + '</br>' + msgInfo;
}