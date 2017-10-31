class MainForm
{
    SideMenuButton: HTMLButtonElement;
    EnButton: HTMLButtonElement;
    EsButton: HTMLButtonElement;
    ServerButton: HTMLButtonElement;
    SideMenuDiv: HTMLDivElement;
    AnimationButtonBool: Boolean;
    FullscreenButtonBool: Boolean;
    Web: TcpClient;
    Message: MessageClass;
    Map: string;
    CanvasTcp;
    userid;
    operatorid;

    constructor()
    {
        this.AnimationButtonBool = true;
        this.FullscreenButtonBool = true;
      
        //this.Web = new TcpClient("192.168.1.118", "2999");
        
      
        this.SideMenuButton = <HTMLButtonElement>document.getElementById("SideMenuButton");
        this.SideMenuButton["targetThis"] = this;
        this.SideMenuDiv = <HTMLDivElement>document.getElementById("SideMenuDiv");
        this.SideMenuDiv["targetThis"] = this;
        this.EnButton = <HTMLButtonElement>document.getElementById("EnButton");
        this.EnButton.onclick = this.EnButtonClick;
        this.EnButton["targetThis"] = this;

        this.ServerButton = <HTMLButtonElement>document.getElementById("ServerButton");
        this.ServerButton.onclick = this.ServerButtonClick;
        this.ServerButton["targetThis"] = this;

        this.EsButton = <HTMLButtonElement>document.getElementById("EsButton");
        this.EsButton.onclick = this.EsButtonClick;
        this.EsButton["targetThis"] = this;
       
       
       /* (<any>window).Touch ? this.SideMenuButton.ontouchstart = this.SideMenuButtonClick : */this.SideMenuButton.onclick = this.SideMenuButtonClick;
    }
    EsButtonClick()
    {
        var Div = <HTMLDivElement>document.getElementById("ServerForm");
        Div.style.display = "none";
    }
    ServerButtonClick()
    {
        var Div = <HTMLDivElement>document.getElementById("ServerForm");
        Div.style.display = "block";
    }
    EnButtonClick()
    {
        var targetThis = <MainForm>this["targetThis"];
        var select = <HTMLSelectElement>document.getElementById("Select1");
        if (select["SelectItem"] != null)
        {
            var SelectItem = <HTMLOptionElement>select["SelectItem"];

            var IP = SelectItem.value.split("/");
            var ip = IP[2].split(":");
            targetThis.Web = new TcpClient(ip[0], "2999");
            
            var CanvasControlIframe = (<any>document.getElementById("CanvasControlIframe")).contentWindow;
            targetThis.CanvasTcp = CanvasControlIframe.document.getElementById("Main");
            targetThis.CanvasTcp["Web"] = targetThis.Web;
            targetThis.CanvasTcp["basePath"] = (<any>document.getElementById("basePath")).value;
            targetThis.CanvasTcp["userID"] = (<any>document.getElementById("userID")).value;
            targetThis.CanvasTcp["Main"] = document;
            CanvasControlIframe.PassValue();
        }
        var Div = <HTMLDivElement>document.getElementById("ServerForm");
        Div.style.display = "none";
    }
    SideMenuButtonClick()
    {
        var targetThis = <MainForm>this["targetThis"];
        if (targetThis.AnimationButtonBool == true)
        {
            targetThis.SideMenuDiv.style.animation = "TransX 0.5s forwards";
            targetThis.SideMenuDiv.style.webkitAnimation = "TransX 0.5s forwards";
            targetThis.AnimationButtonBool = false;
        }
        else
        {
            targetThis.SideMenuDiv.style.animation = "Trans-X 0.5s forwards";
            targetThis.SideMenuDiv.style.webkitAnimation = "Trans-X 0.5s forwards";
            targetThis.AnimationButtonBool = true;
        }
    }
    SetRoleFunc(URL, PARAMS, MainForm)
    {
        $.ajax({
            type: "post",//请求方式
            url: URL,
            dataType: "text",
            data: PARAMS,
            success: function (result)
            {
                try
                {
                    //alert(result);
                }
                catch (s)
                {
                }

            }
        });
    }
    SetUser(URL, PARAMS, MainForm: MainForm)
    {
        var params = { "userid": PARAMS }
        $.ajax({
            type: "post",//请求方式
            url: URL,
            dataType: "text",
            data: params,
            success: function (result)
            {
                try
                {
                    var s = JSON.parse(result);
                    MainForm.userid = s.userid;
                    MainForm.operatorid = s.operatorid;
                    var PARAMS = { "appCode": "TWTClient", "operatorId": MainForm.operatorid };
                    MainForm.SetRoleFunc((<any>document.getElementById("basePath")).value + "twtLogin!getRoleFuncByAppCode.action", PARAMS, MainForm);
                }
                catch (s)
                {
                }

            }
        });
    }
    SetServer(URL, PARAMS,MainForm)
    {
        $.ajax({
            type: "post",//请求方式
            url: URL,
            dataType: "text",
            data: {},
            success: function (result)
            {
                try
                {
                    var select = <HTMLSelectElement>document.getElementById("Select1");
                    select.onchange = (i) =>
                    {
                        var SelectItem = <HTMLOptionElement>i.srcElement;
                        select["SelectItem"] = SelectItem;
                    }
                    var s = JSON.parse(result);
                    for (var i = 0; i < s.length; i++)
                    {
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
                    var CanvasControlIframe = (<any>document.getElementById("CanvasControlIframe")).contentWindow;
                    MainForm.CanvasTcp = CanvasControlIframe.document.getElementById("Main");
                    MainForm.CanvasTcp["Web"] = MainForm.Web;
                    MainForm.CanvasTcp["basePath"] = (<any>document.getElementById("basePath")).value;
                    MainForm.CanvasTcp["userID"] = (<any>document.getElementById("userID")).value;
                    MainForm.CanvasTcp["Main"] = document;
                    CanvasControlIframe.PassValue();
                }
                catch (s)
                {
                }

            }
        });
    };
    
    SetServerList(ip: string, user: string)
    {
        this.SetServer(ip + "site!getStationMapURL.action", "", this);


        
    }
    reinitIframe()
    {
        var iframe = <HTMLFrameElement>document.getElementById("Iframe");
        var CanvasControlIframe = <HTMLFrameElement>document.getElementById("CanvasControlIframe");
        try
        {
            var height = window.innerHeight ? window.innerHeight : document.body.clientHeight;
            var width = window.innerWidth ? window.innerWidth : document.body.clientWidth;
            CanvasControlIframe.height = height -3;
            CanvasControlIframe.width = width -6;
            iframe.height = height - 28;
        } catch (ex) { }
    }
}

window.onresize = function ()
{
    var mf = new MainForm();

    if (mf != null)
    {
        mf.reinitIframe();     
    }
}
function request(paras)
{
    var url = location.href;
    var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
    var paraObj = {}
    var j;
    for (var i = 0; j = paraString[i]; i++)
    {
        paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
    }
    var returnValue = paraObj[paras.toLowerCase()];
    if (typeof (returnValue) == "undefined")
    {
        return "";
    }
    else
    {
        return returnValue;
    }
}
window.onload = function ()
{
    (<any>document.getElementById("basePath")).value = request("basePath");
    (<any>document.getElementById("userID")).value = request("userID");
    (<any>document.getElementById("basePath")).value = "http://192.168.1.198:8080/CoreFrame/";
    (<any>document.getElementById("userID")).value = "16336";
    var mf = new MainForm();

    if (mf != null)
    {
        mf.SetServerList((<any>document.getElementById("basePath")).value, (<any>document.getElementById("userID")).value);
        mf.SetUser((<any>document.getElementById("basePath")).value + "twtLogin!loginByUserid.action", (<any>document.getElementById("userID")).value, mf);
        mf.reinitIframe();
     
    }
   
    
    
}
