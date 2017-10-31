/**
 * 机车细录明细 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('JxgcRecheckDetail');                       //定义命名空间
JxgcRecheckDetail.searchParam = {};
JxgcRecheckDetail.isTestItemMark = false;   //是否试验项目标识

JxgcRecheckDetail.disableAnyElement = function (arg) {
	JxgcRecheckDetail.detailForm.getForm().findField("itemFir").setDisabled(arg);//idx
	JxgcRecheckDetail.detailForm.getForm().findField("itemSec").setDisabled(arg);//idx
	JxgcRecheckDetail.detailForm.getForm().findField("meetingResult").setDisabled(arg);//idx
	JxgcRecheckDetail.detailForm.getForm().findField("remarks").setDisabled(arg);//idx
	Ext.getCmp("saveBut").setDisabled(arg);
}

//ajax查询
JxgcRecheckDetail.getRecheckDetail = function (arg1,arg2,arg3){
	Ext.Ajax.request({
		
		url: ctx + "/jxgcRecheckDetail!getRecheckDetail.action",
		
		params: {recheckDetailIdx : arg1, // 当前选择的树节点的标识
				   reCheckIdx     : arg2, // 细录明细维护主键
				   idx            : arg3},// 当前入厂细录明细的idx
				  
		success: function(response, options){
		       var result = Ext.util.JSON.decode(response.responseText);
		       if (result.errMsg == null) {
		       		if(result.entity!=null&&result.entity.idx!=null&&result.entity.reCheckDetailIdx!=null){
		       			JxgcRecheckDetail.detailForm.getForm().findField("idx").setValue(result.entity.idx);//idx
						JxgcRecheckDetail.detailForm.getForm().findField("isLeaf").setValue(result.entity.isLeaf);//
						JxgcRecheckDetail.detailForm.getForm().findField("reCheckIdx").setValue(result.entity.reCheckIdx);//
						JxgcRecheckDetail.detailForm.getForm().findField("reCheckContent").setValue(result.entity.reCheckContent);//
						JxgcRecheckDetail.detailForm.getForm().findField("isTestItem").setValue(result.entity.isTestItem);//
						JxgcRecheckDetail.detailForm.getForm().findField("reCheckDetailFIdx").setValue(result.entity.reCheckDetailFIdx);//
						JxgcRecheckDetail.detailForm.getForm().findField("reCheckSeq").setValue(result.entity.reCheckSeq);//
						//如果外观检查为空, 则默认设置为"良好"
						if (result.entity.itemFir == null || result.entity.itemFir == "") {
							JxgcRecheckDetail.detailForm.getForm().findField("itemFir").setValue("良好");//外观检查
						} else {
							JxgcRecheckDetail.detailForm.getForm().findField("itemFir").setValue(result.entity.itemFir);//
						}
						//如果该项是试验项目
						if( JxgcRecheckDetail.isTestItemMark ){
							//如果试验项目为空, 则默认为"良好"
							if( result.entity.itemSec == null || result.entity.itemSec == "" ){
								JxgcRecheckDetail.detailForm.getForm().findField("itemSec").setValue("良好");//
							} else {
								JxgcRecheckDetail.detailForm.getForm().findField("itemSec").setValue(result.entity.itemSec);//
							}
						} else {
							JxgcRecheckDetail.detailForm.getForm().findField("itemSec").setValue("");//
						}
						JxgcRecheckDetail.detailForm.getForm().findField("meetingResult").setValue(result.entity.meetingResult);//
						JxgcRecheckDetail.detailForm.getForm().findField("remarks").setValue(result.entity.remarks);//
						JxgcRecheckDetail.detailForm.getForm().findField("dutyPersonId").setValue(result.entity.dutyPersonId);//
						JxgcRecheckDetail.detailForm.getForm().findField("dutyPersonName").setValue(result.entity.dutyPersonName);//
						JxgcRecheckDetail.detailForm.getForm().findField("reCheckDetailIdx").setValue(result.entity.reCheckDetailIdx);//
						JxgcRecheckDetail.detailForm.getForm().findField("dutyPersonId").setValue(empId);
						JxgcRecheckDetail.detailForm.getForm().findField("dutyPersonName").setValue(empname);
		       		}
		       } else {
		              alertFail(result.errMsg);
		       }
		},
		failure: function(response, options){
		       MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		}
	});
};
/*******************************************树**************************************************/
JxgcRecheckDetail.jxgcReCheckIdx = "";
//当前点击的树节点id
JxgcRecheckDetail.currentNodeId = "ROOT_0";
JxgcRecheckDetail.grid = new Ext.yunda.Grid({
	
	saveURL: ctx + '/jxgcRecheckDetail!saveOrUpdate.action',             //保存数据的请求URL
	 /**
     * 新增编辑窗口保存按钮触发的函数，执行数据数据保存动作
     */
    saveFn: function(){
        //表单验证是否通过
        var form = JxgcRecheckDetail.detailForm.getForm(); 
        if (!form.isValid()) return;
        
        //获取表单数据前触发函数
        JxgcRecheckDetail.grid.beforeGetFormData();
        var data = form.getValues();
        //获取表单数据后触发函数
        JxgcRecheckDetail.grid.afterGetFormData();
        
        //调用保存前触发函数，如果返回fasle将不保存记录
        if(!JxgcRecheckDetail.grid.beforeSaveFn(data)) return;
        
        if(self.loadMask)   self.loadMask.show();
        var cfg = {
            scope: this, url: JxgcRecheckDetail.grid.saveURL, jsonData: data,
            success: function(response, options){
                if(self.loadMask)   self.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                	 alertSuccess();
                } else {
                	 MyExt.Msg.alert('保存失败!');
                }
            }
        };
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
    }
});
//树 
JxgcRecheckDetail.tree = new Ext.tree.TreePanel( {
    loader : new Ext.tree.TreeLoader( {
        dataUrl : ctx + "/jxgcRecheckDetail!treeForProcessTask.action?rdpIdx=" + rdpIdx
    }),
    root: new Ext.tree.AsyncTreeNode({
        text: '机车细录明细根节点',
        disabled:true,
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
        	JxgcRecheckDetail.currentNodeId = node.id ;
        	JxgcRecheckDetail.isTestItemMark = false;  //初始先设置为false, 即非试验项目
            if(node.id=="ROOT_0"){
            	JxgcRecheckDetail.tree.root.reload();
            }
			if(node.attributes.isLeaf==1){  //为子类的情况
				//放开所有Disable的框,允许用户填写
            	JxgcRecheckDetail.disableAnyElement(false);
	            //如果当前是实验项目,则允许用户填写实验项目栏,否则disabled
	            if(node.attributes.isTestItem){
	            	JxgcRecheckDetail.isTestItemMark = true;
					JxgcRecheckDetail.detailForm.getForm().findField("itemSec").setDisabled(false);
				}else{
					JxgcRecheckDetail.detailForm.getForm().findField("itemSec").setDisabled(true);
				}
            }
            if(node.attributes.isLeaf==0){ //非子类情况
            	var searchParam = {parentIdx:node.id};
            	if("ROOT_0"==node.id){
            		searchParam = {};
            	}
            	//放开所有Disable的框,禁止用户填写
            	JxgcRecheckDetail.disableAnyElement(true);
            }
            //调用ajax方法,查询明细数据
				JxgcRecheckDetail.getRecheckDetail(node.attributes.reCheckDetailIdx,
													node.attributes.reCheckIdx,
													 node.attributes.idx);
        }
    }    
});
JxgcRecheckDetail.tree.on('beforeload', function(node){
    JxgcRecheckDetail.tree.loader.dataUrl = ctx + '/jxgcRecheckDetail!treeForProcessTask.action?parentIdx=' + node.id+'&rdpIdx='+rdpIdx;
});

JxgcRecheckDetail.fieldWidth = 140;
JxgcRecheckDetail.labelWidth = 80;
JxgcRecheckDetail.anchor = '95%';
JxgcRecheckDetail.detailForm = new Ext.form.FormPanel({
		layout:"form", border:true, labelAlign: "right",style:"padding:10px",
		labelWidth: JxgcRecheckDetail.labelWidth, align:'center',baseCls: "x-plain",
		defaultType:'textfield',defaults:{anchor:"98%"},
		items:[{
			xtype: 'panel',	border:true,  layout:'column',	align:'center', baseCls: "x-plain",
			items:[{
				align:'center',	defaultType:'textfield', border:true, baseCls: "x-plain",layout:"form",
				labelWidth: JxgcRecheckDetail.labelWidth,	columnWidth:1,	defaults:{anchor:JxgcRecheckDetail.anchor},
				items:[
					{ id:"detailIdx",fieldLabel:'idx主键', name:'idx',hidden:true,disabled:false,width:JxgcRecheckDetail.fieldWidth},
					{ id:"reCheckIdx",fieldLabel:'细录单主键',name:'reCheckIdx',hidden:true,disabled:false,width:JxgcRecheckDetail.fieldWidth},
					{ id:"reCheckContent",fieldLabel:'细录内容',name:'reCheckContent',hidden:true,disabled:false,width:JxgcRecheckDetail.fieldWidth},
					{ id:"reCheckSeq",fieldLabel:'序号',name:'reCheckSeq',hidden:true,disabled:false,width:JxgcRecheckDetail.fieldWidth},
					{ id:"itemFir",fieldLabel:'外观检查',name:'itemFir',disabled:true,maxLength:100,width:JxgcRecheckDetail.fieldWidth}, 
					{ id:"isTestItem",fieldLabel:'是否是试验项目',name:'isTestItem',hidden:true,disabled:false,width:JxgcRecheckDetail.fieldWidth}, 
					{ id:"reCheckDetailIdx",fieldLabel:'细录明细维护主键',name:'reCheckDetailIdx',hidden:true,disabled:false,width:JxgcRecheckDetail.fieldWidth},
					{ id:"reCheckDetailFIdx",fieldLabel:'细录明细维护父主键',name:'reCheckDetailFIdx',hidden:true,disabled:false,width:JxgcRecheckDetail.fieldWidth},
					{ id:"isLeaf",fieldLabel:'是否叶子节点',name:'isLeaf',disabled:false,hidden:true,width:JxgcRecheckDetail.fieldWidth},
					{ id:"itemSec",fieldLabel:'试验项目',name:'itemSec',disabled:true,maxLength:100,width:JxgcRecheckDetail.fieldWidth},
					{ id:"meetingResult",fieldLabel:'会议决定',name:'meetingResult',disabled:true,maxLength:200,width:JxgcRecheckDetail.fieldWidth},
					{ id:"dutyPersonId",fieldLabel:'细录人',name:'dutyPersonId',hidden:true,disabled:false,width:JxgcRecheckDetail.fieldWidth},
					{ id:"dutyPersonName",fieldLabel:'细录人',name:'dutyPersonName',hidden:true,disabled:false,width:JxgcRecheckDetail.fieldWidth},
					{ id:"detailRemarks",fieldLabel:'备注',name:'remarks',xtype:'textarea', height:80, maxLength:1000,disabled:true,width:JxgcRecheckDetail.fieldWidth}
				]
			}]
		}]
	});
//覆盖创建的窗口方法
JxgcRecheckDetail.saveWin = new Ext.Panel({
		layout: "fit", plain:true, align:'center',baseCls: "x-plain",layout:"form",
		closeAction: "hide", modal: true, buttonAlign: "center", style:"padding:40,10,10,10",
		items : [ JxgcRecheckDetail.detailForm],
				buttons: [{
		            id:'saveBut',text: "保存", iconCls: "saveIcon", scope: this, handler: JxgcRecheckDetail.grid.saveFn, disabled: true
		        }]
});
parent.confirm = function(){        
        parent.showtip();//启用遮罩
        Ext.Ajax.request({
	        url: ctx + "/jxgcRecheckDetail!confirmTask.action",
	        params: {workitemId: workItemID, rdpIdx: rdpIdx, processInstID:processInstID, activityInstID:actInstId},
	        success: function(response, options){	                  	
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.success == true) {
	                	parent.hidetip();//隐藏遮罩
	                	alertSuccess();
	                	parent.hide();
	                	parent.refreshGrid();
	                }else{
	                	alertFail(result.errMsg);
	                }
	        },
	        failure: function(response, options){
	            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	        }
	    });
	}
//页面布局
var viewport = new Ext.Viewport({
	layout : 'border',
    items : [ {
        iconCls : 'chart_organisationIcon',
        tools : [ {
            id : 'refresh',
            handler : function() {
                JxgcRecheckDetail.tree.root.reload();
                JxgcRecheckDetail.tree.getRootNode().expand();
            }
        } ],
        collapsible : true,
        width : 210,
        split : true,
        region : 'west',
        bodyBorder: false,
        autoScroll : true,
        items : [JxgcRecheckDetail.tree]
    }, {
    	id:'middlePanel',
        region : 'center',
        layout : 'fit',
        baseCls: "x-plain",
        bodyBorder: false,
        items : [JxgcRecheckDetail.saveWin]
    } ]
});
});