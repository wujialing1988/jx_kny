//var num = 1;
var qrcode = null;
var rules;
var _this = null;
var records = null ;
var dictionary = new Dictionary();
function Generate(ev)
{
	var sm = JcpjzdBuildCode.grid.getSelectionModel();
	records = sm.getSelections();
	if (sm.getCount() < 1) {
        MyExt.Msg.alert("尚未选择一条记录！");
        return;
	}
	if (sm.getCount() > 10) {
        MyExt.Msg.alert("一次最多打印十条记录！");
        return;
	}
	//JcpjzdBuildCode.store.getModifiedRecords();
    rules = "";    
    Ext.getCmp("print").disable();
    _this = this;
    var sb = "<table class=\"tdlContextTab\" cellpadding=\"0\" cellspacing=\"0\" border=\"1\">" ;       
	for (var i = 0; i < records.length; i++) {
		var r = records[i];
		if(!r.get('xcwzbm') || r.get('xcwzbm') == ''){
			MyExt.Msg.alert("规格型号：【"+r.get('specificationModelCode')+"】未选择下车位置，请选择！");
			return;
		}
		var newRule = "#"+r.get('specificationModelCode')+"#"+r.get('xcwzbm')+"# ";
		sb += "<tr>";
		sb += "<td width=\"*\" style=\"background-color: #ffffff; text-align:center\" >" ;
		sb += newRule;
		sb += "</td>";
		sb += "</tr>";
		// 组装需要生成的二维码标识 加空格标识
		rules += newRule + "," ;
	}
	sb += "</table>" ;
    document.getElementById("qrcodelist").innerHTML = sb;
    if(rules){
    	rules = rules.substring(0,rules.length-1);
    }
	function makeCode() {
		imp = [];
		if (!rules) {
			MyExt.Msg.alert("二维码生成有问题，请重新生成");
			return;
		}
		var ruleArray = rules.split(",");
		dictionary = new Dictionary();
		for (var i = 0; i < ruleArray.length; i++) {
			_this.qrcode = new QRCode(document.getElementById("qrcode"), {
						width : 60,
						height : 60
					});
			_this.qrcode.makeCode(ruleArray[i]);
			dictionary.Add(ruleArray[i], imp);
		}
		Ext.getCmp("print").enable();
	}
  makeCode();
}

function SetPrint(ev)
{
    var LODOP; //声明为全局变量
    function CreateImage() {
        LODOP = getLodop();
        if(!LODOP){
        	return ;
        }
        if (rules) {
        	for (var i = 0; i < dictionary.Count; i++){
        			// 设置显示值
    				var result = dictionary.Keys[i] ;
    				var specificationModel = 　'' ;
    				var partsName = '' ;
					var arr = result.split("#");
					if(arr.length == 4){
						var spec = arr[1];
						var wzbm = arr[2];
						for (var j = 0; j < records.length; j++) {
							if(spec == records[j].get('specificationModelCode')){
								specificationModel = records[j].get('specificationModel');
							}
							if(wzbm == records[j].get('xcwzbm')){
								result = specificationModel + " " + records[j].get('xcwzmc');
							}
							if(spec == records[j].get('specificationModelCode')){
								partsName = records[j].get('partsName');
							}
						}
					}
 	                LODOP.NewPage();
	                LODOP.SET_PRINT_PAGESIZE(0, "40mm", "30mm", "")
	                LODOP.SET_PRINT_STYLE("FontName", "黑体");
	                LODOP.SET_PRINT_STYLE("FontSize", 10);
	                LODOP.SET_PRINT_STYLE("Bold", 1);
	                LODOP.SET_PRINT_STYLE("Alignment", 2);
	                LODOP.ADD_PRINT_TEXT("1.5mm", "0mm", "40mm", "5mm", partsName);
            	    LODOP.ADD_PRINT_IMAGE("6mm", "11.5mm", "20mm", "20mm",  imp[i].outerHTML);
                    LODOP.ADD_PRINT_TEXT("25mm", "0mm", "40mm", "5mm", result);
        	}
        }
    };
    function myPreview1() {
        CreateImage();
        if(LODOP){
        	LODOP.PREVIEW();
        }
    };
    myPreview1();
}
// 获取显示值
function getDisPlayValue(v)
{
	var result = v ;
	var arr = v.split("#");
	if(arr.length == 4){
		var spec = arr[1];
		var wzbm = arr[2];
		for (var i = 0; i < records.length; i++) {
			if(wzbm == records[i].get('xcwzbm')){
				result = spec + " " + records[i].get('xcwzmc');
				return ;
			}
		}
	}
	return result ;
}