//var num = 1;
var qrcode = null;
var rules;
var _this = null;
var dictionary = new Dictionary();
function Generate(ev)
{
	var numText = document.getElementById("Number");
	if (!numText.value) {
        alert("请输入编码数量");
        numText.focus();
        return;
    }
    if (numText.value == 0 || numText.value > 50) {
    	alert("编码数量为1-50个，请重新输入");
    	numText.value = "";
        numText.focus();
        return;
    }
    document.getElementById("qrcodelist").innerHTML = "";
    rules = null;    
    Ext.getCmp("print").disable();
	jQuery.ajax({
		url: ctx + "/codeRuleConfig!getBatchConfigRule.action",
		type:"post",
		data:{ruleFunction: "PJWZ_PARTS_TEMP_CODE", num: numText.value},//取第一条作业卡的开工时间作为开工时间
		dataType:"json",
		success:function(data){
			if (data.success == true) {
				document.getElementById("qrcodelist").innerHTML = data.rule;
				rules = data.ruleStr;
				 function makeCode() {
					var elText = document.getElementById("Number");
					imp = [];
					if (!elText.value) {
						MyExt.Msg.alert("请输入编码数量");
						elText.focus();
						return;
					}
					if (elText.value > 50) {
						MyExt.Msg.alert("编码数量最多50个，请重新输入");
						elText.value = "";
						elText.focus();
						return;
					}
					
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
		}
	});	
   _this = this;
}

function SetPrint(ev)
{
    var LODOP; //声明为全局变量
    function CreateImage() {
        LODOP = getLodop();
        if (rules) {
        	for (var i = 0; i < dictionary.Count; i=i+2){
        		for (var j = 0; j < num; j++) {
	                LODOP.NewPage();
	                LODOP.SET_PRINT_PAGESIZE(0, "82mm", "30mm", "")
	                LODOP.SET_PRINT_STYLE("FontName", "黑体");
	                LODOP.SET_PRINT_STYLE("FontSize", 10);
	                LODOP.SET_PRINT_STYLE("Bold", 1);
	                LODOP.SET_PRINT_STYLE("Alignment", 2);
	
	                LODOP.ADD_PRINT_TEXT("1.5mm", "0mm", "40mm", "5mm", '上海机车检修段');
            	    LODOP.ADD_PRINT_IMAGE("6mm", "11.5mm", "20mm", "20mm",  imp[i].outerHTML);
                    LODOP.ADD_PRINT_TEXT("25mm", "0mm", "40mm", "5mm", dictionary.Keys[i]);
                    // 打印第二列数据
                    if(i + 1 < dictionary.Count){
	                     LODOP.ADD_PRINT_TEXT("1.5mm", "41.5mm", "40mm", "5mm", '上海机车检修段');
		                 LODOP.ADD_PRINT_IMAGE("6mm", "53.5mm", "20mm", "20mm",  imp[i+1].outerHTML);
	                     LODOP.ADD_PRINT_TEXT("25mm", "41.5mmmm", "40mm", "5mm", dictionary.Keys[i+1]);
                    }           
	                if (j != 0) {
	                    LODOP.ADD_PRINT_TEXT("13mm", "12mm", "40mm", "5mm", j);
	                }
	            }
        	}
        }
    };
    function myPreview1() {
        CreateImage();
        LODOP.PREVIEW();
    };
    myPreview1();

}
function SelectRadio(ex)
{
    var radio = ex.currentTarget;
    if (radio != null && radio.checked == true) {
        num = radio.value;
    }
}
//window.onload = function ()
//{
//    var QRcode = document.getElementById("QRcode");
//    QRcode.onclick = Generate;
//    var Print = document.getElementById("print");
//    Print.onclick = SetPrint;
//    var Radio = document.getElementById("Radio1");
//    Radio.onclick = SelectRadio;
//    var Radio = document.getElementById("Radio2");
//    Radio.onclick = SelectRadio;
//    var Radio = document.getElementById("Radio3");
//    Radio.onclick = SelectRadio;
//    var Radio = document.getElementById("Radio4");
//    Radio.onclick = SelectRadio;
//    var Radio = document.getElementById("Radio5");
//    Radio.onclick = SelectRadio;
//};