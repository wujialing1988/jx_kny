/**
 * 选择施修人员，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('Emp');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	Emp.searchParams = {};
	/** ************** 定义全局变量结束 ************** */
	Emp.store = new Ext.data.JsonStore({
	    id:"idx", root:"root", totalProperty:"totalProperty", autoLoad:false, pruneModifiedRecords: true,
	    fields: [ "empid","empcode","empname" ]
	});
	Emp.grid=new Ext.grid.GridPanel({
      border: false, 
      enableColumnMove: true, 
      stripeRows: true,
      loadMask:true,
      viewConfig: {forceFit: true},
       colModel: new Ext.grid.ColumnModel( [
                  new Ext.grid.RowNumberer(),
       	         {
					header:'idx主键', dataIndex:'empid', hidden:true,editor: { xtype:'hidden' }
				},{
					header:'人员代码', dataIndex:'empcode', editor:{  maxLength:50 }
				},{
					header:'人员名称', dataIndex:'empname', editor:{  maxLength:50 }
				},{
					header:'操作', dataIndex:'idx', width:20, renderer:function(value, metaData, record, rowIndex, colIndex, store){			
						return "<img src='" + imgpathx + "' alt='删除' style='cursor:pointer' onclick='Emp.deleteFn(\"" + rowIndex + "\")'/>";
					}, sortable:false
				}]),
			   store: Emp.store
//	           tbar: [
//	           	{ text:'删除', iconCls:'deleteIcon', handler:function(){
//		        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
//		        var data = Emp.grid.selModel.getSelections();
//		        if(data.length == 0 ){
//		        	MyExt.Msg.alert("尚未选择一条记录！");
//		            return;
//		        }
//		        var records = Emp.grid.store.getModifiedRecords();
//			    for (var i = 0; i < data.length; i++){
//			        Emp.grid.store.remove(data[i]);
//			    }
//			    Emp.grid.getView().refresh(); 
//        		}}
//        ],
//	           bbar: []
    });
    //删除函数
    Emp.deleteFn = function(rowIndex) {
		Emp.grid.store.removeAt(rowIndex); 
		Emp.grid.getView().refresh(); 
	}
	/** ************** 定义质量检查人员所辖机构表格结束 ************** */
	
	/** ************** 定义组织机构树开始 ************** */
	Emp.tree =  new Ext.tree.TreePanel({
		tbar : new Ext.Toolbar(),
		plugins : ['multifilter'],
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/organization!tree.action"
		}),
		root: new Ext.tree.TreeLoader({
			text : teamOrgName,
			disabled : false,
			id : 'ROOT_0',
			nodetype : 'org',
			leaf : false,
			iconCls : 'chart_organisationIcon'
		}),
		rootVisible : true,
    	autoScroll : true,
    	animate : false,
    	useArrows : false,
    	border : false,
    	collapsed : false,
//    	enableDrag:true,
	    listeners: {
    		render : function() {
    			Emp.tree.root.reload();
			    Emp.tree.getRootNode().expand();
    		},
	        dblclick: function(node) {
	        	if (node.attributes.nodetype == 'emp'){
	        		var count=Emp.grid.store.getCount();
        			//判断是否是第一次添加，如果不是，则判断当前添加的人员是否在列表中存在，如果存在，则不允许重复添加
   	          		if(count!=0){
   	          			for(var i=0;i<count;i++){
   	          				var record_v=Emp.grid.store.getAt(i);
   	          				if(node.attributes.empid==record_v.get('empid')){
   	          					MyExt.Msg.alert("所选人员已添加，不可重复添加！");
   	          					return ;
   	          				}
   	          			}
   	          		}
	        		var record = new Ext.data.Record();
	        		record.set("empid",node.attributes.empid);
	        		record.set("empcode",node.attributes.empcode);
	        		record.set("empname",node.attributes.empname);
	        		Emp.grid.store.insert(0, record); 
					Emp.grid.getView().refresh(); 
    			}
	        },
	        beforeload: function(node){
		        var tempid;
				if(node.id=='ROOT_0') tempid = teamOrgId;
				else tempid = node.id.substring(2,node.id.length);
		    	this.loader.dataUrl = ctx + '/organization!tree.action?nodeid='+tempid+'&nodetype='+node.attributes.nodetype;}
		    }    
	});
	/** ************** 定义组织机构树结束 ************** */
	
});