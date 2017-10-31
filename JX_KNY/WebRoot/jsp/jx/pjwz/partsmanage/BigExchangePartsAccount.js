/**
 * 
 * 区间总走行公里维护js
 * 
 */

Ext.onReady(function() {
	Ext.namespace('BigExchangePartsAccount');
	
	/** 获取最近的一年 */
	var dateNow = new Date();
	var month = dateNow.getMonth();
	var year = dateNow.getFullYear();
	dateNow.setFullYear(year - 1);
//	if (month == 0) {
//		dateNow.setFullYear(year - 1);
//		dateNow.setMonth(11);
//	} else {
//		dateNow.setMonth(month-1);
//	}
	var lastMonth = dateNow.format('Y-m-d');
	
	/** ************** 定义全局变量开始 ************** */
	BigExchangePartsAccount.searchParams = {};
	BigExchangePartsAccount.labelWidth = 100;
	BigExchangePartsAccount.fieldWidth = 222;
	
	BigExchangePartsAccount.STC_TYPE_MONTH = 'month';		// 统计类型-月度
	BigExchangePartsAccount.STC_TYPE_YEAR = 'year';			// 统计类型-年度
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 【统计】按钮点击触发的函数
	BigExchangePartsAccount.statisticsFn = function() {
		var form = BigExchangePartsAccount.searchForm.getForm();
		if (!form.isValid()) {
			return;
		}
		// 清除日期过滤条件元素的任何无效标志样式与信息
		Ext.getCmp('startDate_d').clearInvalid();
		Ext.getCmp('endDate_d').clearInvalid();
//		Ext.getCmp('partsName').enable();
		var data = form.getValues();
//		Ext.getCmp('partsName').disable();
//		MyExt.Msg.alert(data.partsType + " - " + data.partsName);
		var hasPartsExtendNo = false;
		Ext.Ajax.request({
            url: ctx + '/partsExtendNo!hasPartsExtendNo.action',
            params : {partsTypeIdx : data.partsTypeIdx},
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.result == "yes") {
					var reportUrl = "/pjwz/partsmanage/BigExchangePartsAccount_Ext.cpt";
					var dataUrl = reportUrl + "&partsTypeIdx=" + data.partsTypeIdx + "&startDate=" + data.startDate + "&endDate=" + data.endDate + "&partsType=" + data.partsType + "&partsName=" + data.partsName
			//		MyExt.Msg.alert(data.startDate + " - " + data.endDate);
					var url = getReportEffectivePath(dataUrl);
					var h = jQuery("#report").height();
					document.getElementById("report").innerHTML = "<iframe style='width:100%;height:"+h+"px;overflow:auto;' frameborder='0' src='" + url + "'></iframe>";
                } else if(result.result == "no") {
					var reportUrl = "/pjwz/partsmanage/BigExchangePartsAccount.cpt";
					var dataUrl = reportUrl + "&partsTypeIdx=" + data.partsTypeIdx + "&startDate=" + data.startDate + "&endDate=" + data.endDate + "&partsType=" + data.partsType + "&partsName=" + data.partsName
					var url = getReportEffectivePath(dataUrl);
					var h = jQuery("#report").height();
					document.getElementById("report").innerHTML = "<iframe style='width:100%;height:"+h+"px;overflow:auto;' frameborder='0' src='" + url + "'></iframe>";
                }
            },
            failure: function(response, options){
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
		});
	};
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义查询表单开始 ************** */
	BigExchangePartsAccount.searchForm = new Ext.form.FormPanel({
		labelWidth: BigExchangePartsAccount.labelWidth,
		layout: 'column', baseCls: "x-plain", border: false,
		style:'padding: 15px; ',
		items:[{
			// 查询表单第1列
			columnWidth: .3,
			layout:'form',
			baseCls: "x-plain",
			items:[{
				xtype:"PartsTypeTreeSelect",fieldLabel: '配件规格型号',
				width: BigExchangePartsAccount.fieldWidth,
				name: 'partsType',
			  	editable:false,
			  	allowBlank: false,
			  	returnFn: function(node, e) {
			  		Ext.getCmp('partsName').setValue(node.attributes["partsName"]);
			  		Ext.getCmp('partsTypeIdx').setValue(node.attributes["id"]);
			  		BigExchangePartsAccount.searchForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
			  	}
			}, {xtype: 'hidden', id: 'partsTypeIdx', fieldLabel:'配件规格型号id'}]
		}, {
			// 查询表单第2列
			columnWidth: .3,
			layout:'form',
			baseCls: "x-plain",
			items:[{
				id: 'partsName',
				xtype:"textfield",fieldLabel: '配件名称',
				width: BigExchangePartsAccount.fieldWidth, style: 'border: none; background: none',
			  	name: 'partsName', readOnly: true
			}]
		}, {
			// 查询表单第3列
			columnWidth: .3,
			layout:'form',
			baseCls: "x-plain",
			items:[{
				xtype: 'compositefield', fieldLabel: '日期', allowBlank: false, combineErrors: false,
				items: [{
					xtype:'my97date', name: 'startDate', id: 'startDate_d', format:'Y-m-d', value: lastMonth, width: 100, allowBlank: false,
					// 日期校验器
					validator: function() {
						var startDate =new Date(Ext.getCmp('startDate_d').getValue());
						var endDate = new Date(Ext.getCmp('endDate_d').getValue());
						if (startDate > endDate) {
							return "开始日期不能大于结束日期";
						}
					}
				}, {
					xtype: 'label',
					text: '至',
					style: 'height: 23px;line-height:23px;'
				}, {
					xtype:'my97date', name: 'endDate', id: 'endDate_d', format:'Y-m-d', width: 100, allowBlank: false,
					// 日期校验器
					validator: function() {
						var startDate =new Date(Ext.getCmp('startDate_d').getValue());
						var endDate = new Date(Ext.getCmp('endDate_d').getValue());
						if (startDate > endDate) {
							return "结束日期不能小于开始日期";
						}
					}
				}]
			}]
		}],
		buttonAlign: 'center', 
		buttons:[{
			text: '统计',
			iconCls: 'queryIcon', 
			handler: BigExchangePartsAccount.statisticsFn
		}, {
			text: '重置',
			iconCls: 'resetIcon', 
			handler: function() {BigExchangePartsAccount.searchForm.getForm().reset()}
		}]
	});
	/** ************** 定义查询表单结束 ************** */
	
	// 页面自适应布局
	BigExchangePartsAccount.viewport = new Ext.Viewport({
		layout: 'border',
		items:[{
			// 查询表单区域
			title: '查询', frame: true,
			region: 'north',
			height: 130,
			border: true,
			collapsible: true,
			collapseMode:'mini',
			split: true,
			items: [BigExchangePartsAccount.searchForm]
		}, {
			// 统计报表结果显示区域
			id:"report",
			region : 'center', layout : 'fit', bodyBorder: false, 
			split:true,
			items:[]
		}]
	});
	
});