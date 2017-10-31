/**
 * 判断是否显示配件信息 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsAccountOrRdp');                       //定义命名空间
	PartsAccountOrRdp.width = 100;
	PartsAccountOrRdp.partsNo = '';
	PartsAccountOrRdp.partsAccountIDX = '';
	/** **************** 定义全局函数开始 **************** */
	
	/** ****************装载数据 是否有检修记录**************** */	
	PartsAccountOrRdp.storeLoadFn = function(){
		var roomCount = this.getCount();
		//判断是否有检修作业工单，没有则显示配件详情信息，有，则显示配件检修作业信息
		if (0>=roomCount){	
			if(''==PartsAccountOrRdp.partsAccountIDX || null == PartsAccountOrRdp.partsAccountIDX || "null" == PartsAccountOrRdp.partsAccountIDX ){
				 Ext.Msg.alert('提示', "配件信息为空");
				 return ;
			}
			// Ajax请求
			Ext.Ajax.request({
				url: ctx + '/partsAccount!getModel.action',
				params:{id: PartsAccountOrRdp.partsAccountIDX },
				//请求成功后的回调函数
			    success: function(response, options){
			        var result = Ext.util.JSON.decode(response.responseText);
			        if (result.errMsg == null) {       //操作成功
			        	PartsDetail.record = result.entity;  
			        	PartsDetail.win.show();
			        } else {                           //操作失败
			            alertFail(result.errMsg);
			        }
			    },
			    //请求失败后的回调函数
			    failure: function(response, options){
			        if(self.loadMask)    self.loadMask.hide();
			        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			    }
			});
			
		}
		else {
			var  jsonRdp= this.getAt(0).json;
			PartsRdpDetail.record = jsonRdp;
			if (typeof(jsonRdp) == "undefine" || !jsonRdp){
				PartsRdpDetail.record = PartsRdpDetail.record.data;
			}
			PartsRdpDetail.win.show();
		}
		
	}
	
	//查询是否有检修作业	
	PartsAccountOrRdp.showPartsRdpOrDetail = function(partsNo,partsAccountIDX){
		//初始化配件检修作业配件编号，配件识别码
		PartsRdpDetail.partsNo = partsNo;
		
		PartsAccountOrRdp.partsNo =partsNo;
		PartsAccountOrRdp.partsAccountIDX = partsAccountIDX;
		//初始化查询参数
		var params = {};
        params.partsNo = partsNo;
        params.partsAccountIDX = partsAccountIDX;
		//加载检修作业单
		PartsAccountOrRdp.store = new Ext.data.JsonStore({
			id : 'idx',
			root : "root",
			totalProperty : "totalProperty",
			autoLoad : true,
			remoteSort : false,
			url : ctx + '/partsRdp!pageList.action',
			fields : ["idx", "identificationCode", "partsNo","partsName"
						,'specificationModel', "unloadTrainType", "unloadTrainNo"
						,"unloadRepairClass",'unloadRepairTime'
						,"realStartTime",'realEndTime'
						,"repairOrgName",'wpDesc'],
			sortInfo : {
				field : 'idx',
				direction : 'ASC'
			},
			listeners : {
				beforeload : function() {
			 		this.baseParams.entityJson = Ext.util.JSON.encode(params);
				},
				// 数据加载完成后的函数处理
				load : PartsAccountOrRdp.storeLoadFn
			}
		});		
	}
	/** **************** 定义全局函数结束 **************** */	

});