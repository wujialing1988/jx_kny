var MainForm = (function () {
    function MainForm() {
        this.AnimationButtonBool = true;
        this.FullscreenButtonBool = true;
        //this.Web = new TcpClient("192.168.1.118", "2999");
        this.SideMenuButton = document.getElementById("SideMenuButton");
        this.SideMenuButton["targetThis"] = this;
        this.SideMenuDiv = document.getElementById("SideMenuDiv");
        this.SideMenuDiv["targetThis"] = this;
        this.EnButton = document.getElementById("EnButton");
        this.EnButton.onclick = this.EnButtonClick;
        this.EnButton["targetThis"] = this;
        this.ServerButton = document.getElementById("ServerButton");
        this.ServerButton.onclick = this.ServerButtonClick;
        this.ServerButton["targetThis"] = this;
        this.EsButton = document.getElementById("EsButton");
        this.EsButton.onclick = this.EsButtonClick;
        this.EsButton["targetThis"] = this;
        /* (<any>window).Touch ? this.SideMenuButton.ontouchstart = this.SideMenuButtonClick : */ this.SideMenuButton.onclick = this.SideMenuButtonClick;
    }
    MainForm.prototype.EsButtonClick = function () {
        var Div = document.getElementById("ServerForm");
        Div.style.display = "none";
    };
    MainForm.prototype.ServerButtonClick = function () {
        var Div = document.getElementById("ServerForm");
        Div.style.display = "block";
    };
    MainForm.prototype.EnButtonClick = function () {
        var targetThis = this["targetThis"];
        var select = document.getElementById("Select1");
        if (select["SelectItem"] != null) {
            var SelectItem = select["SelectItem"];
            var IP = SelectItem.value.split("/");
            var ip = IP[2].split(":");
            targetThis.Web = new TcpClient(ip[0], "2999");
            var CanvasControlIframe = document.getElementById("CanvasControlIframe").contentWindow;
            targetThis.CanvasTcp = CanvasControlIframe.document.getElementById("Main");
            targetThis.CanvasTcp["Web"] = targetThis.Web;
            targetThis.CanvasTcp["basePath"] = document.getElementById("basePath").value;
            targetThis.CanvasTcp["userID"] = document.getElementById("userID").value;
            targetThis.CanvasTcp["Main"] = document;
            CanvasControlIframe.PassValue();
        }
        var Div = document.getElementById("ServerForm");
        Div.style.display = "none";
    };
    MainForm.prototype.SideMenuButtonClick = function () {
        var targetThis = this["targetThis"];
        if (targetThis.AnimationButtonBool == true) {
            targetThis.SideMenuDiv.style.animation = "TransX 0.5s forwards";
            targetThis.SideMenuDiv.style.webkitAnimation = "TransX 0.5s forwards";
            targetThis.AnimationButtonBool = false;
        }
        else {
            targetThis.SideMenuDiv.style.animation = "Trans-X 0.5s forwards";
            targetThis.SideMenuDiv.style.webkitAnimation = "Trans-X 0.5s forwards";
            targetThis.AnimationButtonBool = true;
        }
    };
    MainForm.prototype.SetRoleFunc = function (URL, PARAMS, MainForm) {
        $.ajax({
            type: "post",
            url: URL,
            dataType: "text",
            data: PARAMS,
            success: function (result) {
                try {
                }
                catch (s) {
                }
            }
        });
    };
    MainForm.prototype.SetUser = function (URL, PARAMS, MainForm) {
        var params = { "userid": PARAMS };
        $.ajax({
            type: "post",
            url: URL,
            dataType: "text",
            data: params,
            success: function (result) {
                try {
                    var s = JSON.parse(result);
                    MainForm.userid = s.userid;
                    MainForm.operatorid = s.operatorid;
                    var PARAMS = { "appCode": "TWTClient", "operatorId": MainForm.operatorid };
                    MainForm.SetRoleFunc(document.getElementById("basePath").value + "twtLogin!getRoleFuncByAppCode.action", PARAMS, MainForm);
                }
                catch (s) {
                }
            }
        });
    };
    MainForm.prototype.SetServer = function (URL, PARAMS, MainForm) {
        $.ajax({
            type: "post",
            url: URL,
            dataType: "text",
            data: {},
            success: function (result) {
                try {
                    var select = document.getElementById("Select1");
                    select.onchange = function (i) {
                        var SelectItem = i.srcElement;
                        select["SelectItem"] = SelectItem;
                    };
                    var s = JSON.parse(result);
                    for (var i = 0; i < s.length; i++) {
                        var po = new Option(s[i].mapName, s[i].webAddress);
                        po["siteID"] = s[i].siteID;
                        select.options.add(po);
                    }
                    select.options.remove(0);
                    select.options[0].selected = true;
                    select["SelectItem"] = select.options[0];
                    var IP = select.options[0].value.split("/");
                    var ip = IP[2].split(":");
                    MainForm.Web = new TcpClient(ip[0], "2999");
                    var CanvasControlIframe = document.getElementById("CanvasControlIframe").contentWindow;
                    MainForm.CanvasTcp = CanvasControlIframe.document.getElementById("Main");
                    MainForm.CanvasTcp["Web"] = MainForm.Web;
                    MainForm.CanvasTcp["basePath"] = document.getElementById("basePath").value;
                    MainForm.CanvasTcp["userID"] = document.getElementById("userID").value;
                    MainForm.CanvasTcp["Main"] = document;
                    CanvasControlIframe.PassValue();
                }
                catch (s) {
                }
            }
        });
    };
    ;
    MainForm.prototype.SetServerList = function (ip, user) {
        this.SetServer(ip + "site!getStationMapURL.action", "", this);
    };
    MainForm.prototype.reinitIframe = function () {
        var iframe = document.getElementById("Iframe");
        var CanvasControlIframe = document.getElementById("CanvasControlIframe");
        try {
            var height = window.innerHeight ? window.innerHeight : document.body.clientHeight;
            var width = window.innerWidth ? window.innerWidth : document.body.clientWidth;
            CanvasControlIframe.height = height - 3;
            CanvasControlIframe.width = width - 6;
            iframe.height = height - 28;
        }
        catch (ex) { }
    };
    return MainForm;
})();
window.onresize = function () {
    var mf = new MainForm();
    if (mf != null) {
        mf.reinitIframe();
    }
};
function request(paras) {
    var url = location.href;
    var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
    var paraObj = {};
    var j;
    for (var i = 0; j = paraString[i]; i++) {
        paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
    }
    var returnValue = paraObj[paras.toLowerCase()];
    if (typeof (returnValue) == "undefined") {
        return "";
    }
    else {
        return returnValue;
    }
}
window.onload = function () {
    document.getElementById("basePath").value = request("basePath");
    document.getElementById("userID").value = request("userID");
//    document.getElementById("basePath").value = "http://192.168.1.198:8080/CoreFrame/";
//    document.getElementById("userID").value = "16336";
    var mf = new MainForm();
    if (mf != null) {
        mf.SetServerList(document.getElementById("basePath").value, document.getElementById("userID").value);
        mf.SetUser(document.getElementById("basePath").value + "twtLogin!loginByUserid.action", document.getElementById("userID").value, mf);
        mf.reinitIframe();
    }
};
//# sourceMappingURL=MainForm.js.map