/**
 * 出库中查询数量方法 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('MatOutWHFun');                       //定义命名空间
//查询库存信息、保有量信息
MatOutWHFun.selectTotalQty = function(whIdx, matCode, matTypeItems, tab){
	Ext.Ajax.request({
		url: ctx + "/matTypeList!getMatInfo.action",
		params: {whIdx: whIdx , matCode: matCode , matType : matTypeItems},
		success: function(response, options){
	        var result = Ext.util.JSON.decode(response.responseText);
	        if (null != result.matStock) {
	        	if(tab == 0){
	        		Ext.getCmp("totalQty_0").setValue(result.matStock.qty);
	        	}
	        	if(tab == 1){
	        		Ext.getCmp("totalQty_1").setValue(result.matStock.qty);
	        	}
	        	if(tab == 2){
	        		Ext.getCmp("totalQty_2").setValue(result.matStock.qty);
	        	}
			}else{
				if(tab == 0){
	        		Ext.getCmp("totalQty_0").setValue(0);
	        	}
	        	if(tab == 1){
	        		Ext.getCmp("totalQty_1").setValue(0);
	        	}
	        	if(tab == 2){
	        		Ext.getCmp("totalQty_2").setValue(0);
	        	}
			}
			if (null != result.whMatQuota) {
				if(tab == 0){
	        		Ext.getCmp("mixQty_0").setValue(result.whMatQuota.minQty);
				    Ext.getCmp("maxQty_0").setValue(result.whMatQuota.maxQty);
	        	}
	        	if(tab == 1){
	        		Ext.getCmp("mixQty_1").setValue(result.whMatQuota.minQty);
				    Ext.getCmp("maxQty_1").setValue(result.whMatQuota.maxQty);
	        	}
	        	if(tab == 2){
	        		Ext.getCmp("mixQty_2").setValue(result.whMatQuota.minQty);
				    Ext.getCmp("maxQty_2").setValue(result.whMatQuota.maxQty);
	        	}
			}else{
				if(tab == 0){
	        		Ext.getCmp("mixQty_0").setValue("");
				    Ext.getCmp("maxQty_0").setValue("");
	        	}
	        	if(tab == 1){
	        		Ext.getCmp("mixQty_1").setValue("");
				    Ext.getCmp("maxQty_1").setValue("");
	        	}
	        	if(tab == 2){
	        		Ext.getCmp("mixQty_2").setValue("");
				    Ext.getCmp("maxQty_2").setValue("");
	        	}
			}
		},
		failure: function(response, options){
			MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		}
	});
}
//查询库存信息
MatOutWHFun.selectLocationQty = function(whIdx, matCode , matTypeItems, locationName, tab){
	Ext.Ajax.request({
		url: ctx + "/matStockQuery!getModelStockByLocation.action",
		params: {whIdx: whIdx , matCode: matCode , matType : matTypeItems, locationName : locationName},
		success: function(response, options){
	        var result = Ext.util.JSON.decode(response.responseText);
	        if (null != result.matStock) {
	        	if(tab == 0){
	        		 Ext.getCmp("locationQty_0").setValue(result.matStock.qty);
	        	}
	        	if(tab == 1){
	        		 Ext.getCmp("locationQty_1").setValue(result.matStock.qty);
	        	}
	        	if(tab == 2){
	        		 Ext.getCmp("locationQty_2").setValue(result.matStock.qty);
	        	}
			}else{
				if(tab == 0){
	        		 Ext.getCmp("locationQty_0").setValue(0);
	        	}
	        	if(tab == 1){
	        		 Ext.getCmp("locationQty_1").setValue(0);
	        	}
	        	if(tab == 2){
	        		 Ext.getCmp("locationQty_2").setValue(0);
	        	}
			}
		},
		failure: function(response, options){
			MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		}
	});
}
});