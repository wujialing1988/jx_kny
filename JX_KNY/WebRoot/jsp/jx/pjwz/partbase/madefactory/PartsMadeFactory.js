/**
 * 配件生产厂家 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsMadeFactory');                       //定义命名空间
PartsMadeFactory.searchParam = {};
//查询参数对象
PartsMadeFactory.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsMadeFactory!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/partsMadeFactory!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/partsMadeFactory!logicDelete.action',            //删除数据的请求URL
    storeId:'id',
	fields: [{
		header:'生产厂家编码', dataIndex:'id', editor:{id:"ID",allowBlank:false, maxLength:5 },searcher:{disabled:true}
	},{
		header:'生产厂家名称', dataIndex:'madeFactoryName', editor:{ allowBlank:false, maxLength:50 }
	},{
		header:'生产厂家简称', dataIndex:'madeFactoryShortname', editor:{ allowBlank:false, maxLength:20 }
	}],
	tbar: ['search','add','delete','refresh'],
    afterShowSaveWin: function(){
    	var url = ctx + "/codeRuleConfig!getConfigRule.action";
		Ext.Ajax.request({
            url: url,
            params: {ruleFunction: "PJWZ_PARTS_MADE_FACTORY_ID"},
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    Ext.getCmp("ID").setValue(result.rule);
                    Ext.getCmp("ID").enable();
                }
            },
            failure: function(response, options){
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });
    
    }, 
    beforeGetFormData: function(){
    	Ext.getCmp("ID").enable();
    },
    afterGetFormData: function(){
    	Ext.getCmp("ID").disable();
    },
    afterShowEditWin: function(record, rowIndex){
    	Ext.getCmp("ID").disable();
    },
    afterSaveSuccessFn: function(result, response, options){
        this.store.reload();
        alertSuccess();
        this.saveWin.hide();
    }
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:PartsMadeFactory.grid });
});