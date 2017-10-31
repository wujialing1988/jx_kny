var c1c6_val = 'C1C6', fxdx_val = '辅修大修';
var c1c6clear, fxdxclear;
var uploadAction = "/runningKM!uploadDataFile.action";
var uploadTitle = "走行公里数据导入"
var _ = {
    $:function (el){
       return typeof el=="string" ? document.getElementById(el) : el;
    },
    on:function (el, type, fn){
        el = this.$(el);
        if(el.attachEvent){        	
        	el.attachEvent('on'+type, function(){
        		fn.call(el,event);
        	});
        }else{        	
        	el.addEventListener(type, fn, false);
        }
        return this
    },
    stopUp:function (e){
        e = e || window.event;
        e.stopPropagation && e.stopPropagation();
        e.cancelBubble = true;
    },
    onblur:function (el, fn){
        el = this.$(el);
        _.on(el, 'click', function (e){
        	_.stopUp(e)
        }).on(document, 'click', function(e){
        	e = e || window.event;
        	var dom = e.target || e.srcElement;
        	if(dom.className === 'clear'){
        		return;
        	}
        	fn.call(el, e);
        });
    }
};


var template = {
	text: "下载导入模板",
	iconCls: "page_excelIcon",
	handler: function(){
		location.href = encodeURI('走行公里数据导入模板.xls');
	}
};

var statistics = {
	text: "按年统计",
	iconCls: "theme2Icon",
	handler: function(){
		Ext.Msg.alert("提示", "功能未实现！");
	}
};

/**
 * 计算修程
 */
function computeXC(grid, repairType){
	var me = this;
	Ext.Ajax.request({
		url: ctx + "/runningKM!reCompute.action",
		params: {repairType:repairType},
		success:function(r){
			var rlt = Ext.util.JSON.decode(r.responseText);
			if(rlt.success){
				alertSuccess();
				grid.store.reload();
				me.findBy
			}else{
				alertFail(rlt.errMsg);
			}
		},
		failure: function(){
			alertFail("计算修程连接超时！");
		}
	});
}


/**
 * 按钮查找grid（查询表格）
 */
function buttonFindGrid(callback){
	var tab = this.findParentByType("tabpanel");
	if(tab){
		var panel = tab.getActiveTab().items.items[0].items.items[1];
		if(panel){
			panel.findBy(function(){
				var grid = arguments[0];
				if(grid && grid.store){
					callback(grid);
				}
			});
		}
	}
}

/**
 * 已启动和未启动查询表单的创建
 * @return
 */
function createSearchForm(xcType){
	
	return defineFormPanel({
		defaultType: "textfield",
		labelWidth: 60,
		rows:[{
			cw: [200, 200, 80, 80],
			cols: [{
				xtype: "Base_combo",
				entity:'com.yunda.jx.base.jcgy.entity.TrainType',
				business:"trainType",
				fields:['typeID','shortName'],
				hiddenName: "trainTypeIdx", 
				displayField: "shortName",
				valueField: "typeID",
				pageSize: 20, minListWidth: 200,
				queryParams: {repairType: xcType},
				fieldLabel: "车型",
				editable:false
			},{
				fieldLabel: "车号",
				name: "trainNo"
			},{
				xtype: "button",
				text: "查询",
				style: "margin-left: 10px",
				iconCls: "searchIcon",
				handler: function(){
					buttonFindGrid.call(this, function(grid){
						var sp = grid.searchForm.getForm().getValues();
						grid.searchFn(sp);
					});
				}
			},{
				xtype: "button",
				text: "重置",
				style: "margin-left: 10px",
				iconCls: "resetIcon",
				handler: function(){
					var form = this.findParentByType("form");
					form.getForm().reset();
					form.findByType("Base_combo")[0].clearValue();
					
					buttonFindGrid.call(this, function(grid){
						var sp = form.getForm().getValues();
						grid.searchFn(sp);
					});
				}
			}]
		}]
	}, {labelAlign: "right"});
}

function undoRequest(grid){
	var record;
	if((record = $yd.getSingleRecord(grid))){
		var idx = record.get("idx");
		Ext.Ajax.request({
			url: ctx + "/runningKM!undo.action",
			params: {idx: idx},
			success: function(r){
				var rlt = Ext.util.JSON.decode(r.responseText);
				if(rlt.success){
					alertSuccess();
					grid.store.reload();
				}else{
					alertFail(rlt.errMsg);
				}
			},
			failure: function(){
				alertFail("撤销请求超时！");
			}
		});
	}
}

function undo(isC1C6){
	var grid = isC1C6 ? C1C6.grid : FXDX.grid;
	Ext.Msg.confirm("提示", "确认撤销数据", function(btn){
		if(btn == 'yes'){
			undoRequest(grid);
		}
	});
}

function clearRKM(isC1C6){
	var e = window.event || event;
	var target = e.srcElement || e.target;
	var dom = target;
	var el = isC1C6 ? c1c6clear : fxdxclear;
	var x = dom.offsetLeft;//e.clientX;
	var y = dom.offsetTop + dom.offsetHeight + 10;
	while(dom = dom.offsetParent){
		x += dom.offsetLeft - dom.scrollLeft;
		y += dom.offsetTop - dom.scrollTop;
	}
	el.style.visibility = 'hidden';
	el.style.display = 'block';
	var left = x + el.offsetWidth;
	var top = y + el.offsetHeight;
	var bodyWidth = document.documentElement.offsetWidth;
	var bodyHeight = document.documentElement.offsetHeight;
	if(left > bodyWidth){
		x = bodyWidth - el.offsetWidth - 10;
	}
	if(top > bodyHeight){
		y = y - el.offsetHeight - target.offsetHeight;
	}
	el.style.left = x + "px";
	el.style.top =  y + "px";
	el.style.visibility = 'visible';
}

function hideClearLayout(){
	c1c6clear.style.display = 'none';
	fxdxclear.style.display = 'none';
}

Ext.onReady(function(){
	c1c6clear = document.querySelector("#c1c6_clear");
	fxdxclear = document.querySelector("#fxdx_clear");
	
	function request(grid, index){
		var record;
		if(record = $yd.getSingleRecord(grid)){
			var idx = record.get("idx");
			Ext.Ajax.request({
				url: ctx + "/runningKM!clear.action",
				params: {idx: idx, index: index + 1},
				success: function(r){
					var rlt = Ext.util.JSON.decode(r.responseText);
					if(rlt.success){
						if(rlt.result == 2 ){
							alertSuccess();
							grid.store.reload();
						}else{
							alertFail("请求参数错误！");
						}
					}else{
						alertFail(rlt.errMsg);
					}
				},
				failure: function(){
					alertFail("修后清零请求超时！");
				}
			});
		}
	}
	
	function each(arr, func){
		for(var i = 0; i < arr.length; i++){
			func.call(arr[i], i, arr);
		}
	}
	
	
	//绑定li hover
	function bindHover(index, colls){
		this.onmouseover = function(){
			for(var i = 0; i < index; i++){
				colls[i].className = "clearLi";
			}
		}
		this.onmouseout = function(){
			for(var i = 0; i < index; i++){
				colls[i].removeAttribute("class");
			}
		}
		this.onclick = function(){
			hideClearLayout();
			var me = this;
			Ext.Msg.confirm("提示", "确认执行清零操作?", function(btn){
				if(btn == 'yes'){
					if(me.innerHTML.charAt(0) === 'C'){
						request(C1C6.grid, index);
					}else{
						request(FXDX.grid, index);
					}
				}
			});
		}
	}
	each(document.querySelectorAll("#c1c6_clear li"), bindHover);
	each(document.querySelectorAll("#fxdx_clear li"), bindHover);
	
	function blur(){
		this.style.display='none';
	}
	//绑定失去焦点
	_.onblur(c1c6clear, blur);
	_.onblur(fxdxclear, blur);
});