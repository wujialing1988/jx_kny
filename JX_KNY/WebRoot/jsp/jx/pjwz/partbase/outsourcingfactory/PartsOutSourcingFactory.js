/**
 * 配件生产厂家 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsOutSourcingFactory');                       //定义命名空间
PartsOutSourcingFactory.searchParam = {};
//查询参数对象
PartsOutSourcingFactory.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsOutSourcingFactory!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/partsOutSourcingFactory!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/partsOutSourcingFactory!logicDelete.action',            //删除数据的请求URL
    storeId:'id',
	fields: [{
		header:'委外厂家编码', dataIndex:'id', editor:{id:"ID",allowBlank:false, maxLength:5 },searcher:{disabled:true}
	},{
		header:'委外厂家名称', dataIndex:'factoryName', editor:{ allowBlank:false, maxLength:50 }
	},{
		header:'委外厂家简称', dataIndex:'factoryShortname', editor:{ allowBlank:false, maxLength:20 }
	}],
	tbar: ['search','add','delete','refresh'],
    afterShowSaveWin: function(){
    	var url = ctx + "/codeRuleConfig!getConfigRule.action";
		Ext.Ajax.request({
            url: url,
            params: {ruleFunction: "PJWZ_PARTS_OUTSOURCING_FACTORY_ID"},
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                	var rule = result.rule;
                	if (rule.length > 5) {
                		rule = rule.substring(rule.length - 5, rule.length - 1)
                	}
                    Ext.getCmp("ID").setValue(rule);
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
//    beforeSaveFn: function(data){ 
//    	var store = PartsOutSourcingFactory.grid.store;
//    	for (var i = 0; i < store.getCount(); i++) {
//    		var record = store.getAt(i);
//    		if (record.get('id') == data.id) {
//    			MyExt.Msg.alert('委外厂家编码【<span style="color:red;">' + data.id + '</span>】已经存在，不可以重复添加！');
//    			return false;
//    		}
//    	}
//		return true; 
//    }
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:PartsOutSourcingFactory.grid });
});