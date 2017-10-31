/**
 * 机车保洁form--机车整备合格交验
 */
Ext.onReady(function(){
  Ext.namespace('ZbglCleanFormJY');
	  ZbglCleanFormJY.fieldWidth = 120;
	  ZbglCleanFormJY.labelWidth = 80;
  
	  ZbglCleanFormJY.form = new Ext.form.FormPanel({
		labelWidth:ZbglCleanFormJY.labelWidth, border: false,
		labelAlign:"left", layout:"column",
		bodyStyle:"padding:10px;",
		defaults:{
			xtype:"container", autoEl:"div", layout:"form", columnWidth:0.5, 
			defaults:{
				style: 'border:none; background:none;', 
				xtype:"textfield", readOnly: true,
				anchor:"100%"
			}
		},
		items:[{
	        items: [
	        	{ fieldLabel:"保洁人员", name:"dutyPersonName"},
	        	{ fieldLabel:"机车等级", name:"trainLevel"}
	        ]
		},{
	        items: [
//	        	{ fieldLabel:"保洁时间", name:"cleaningTime", xtype: 'my97date', my97cfg: {dateFmt:'yyyy-MM-dd HH:mm:ss'}, initNow: false},
	        	{ fieldLabel:"保洁时间", name:"cleaningTime"},
	        	{ fieldLabel:"保洁等级", name:"cleaningLevel" }
	        ]
		}]
	});

});