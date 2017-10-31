class ScriptList
{
    HeadElement: HTMLHeadElement;
    constructor()
    {
        this.HeadElement = <HTMLHeadElement>document.getElementById("Head");
        this.HeadElement.appendChild(this.SetScript("Scripts/jquery-2.1.4.js"));
        this.HeadElement.appendChild(this.SetScript("Dlls/JS/BasicClass/MessageClass.js"));
        this.HeadElement.appendChild(this.SetScript("Dlls/JS/WebSocket/TcpClient.js"));
        this.HeadElement.appendChild(this.SetScript("Dlls/JS/Fullscreen/FullscreenJS.js"));
        this.HeadElement.appendChild(this.SetScript("Dlls/JS/Event/Event.js"));
        //this.HeadElement.appendChild(this.SetScript("Dlls/JS/LinQ/LINQ.js"));
        this.HeadElement.appendChild(this.SetScript("Dlls/JS/WebSocket/WebSocket.js"));
        this.HeadElement.appendChild(this.SetScript("Dlls/JS/BasicClass/BasicClass.js"));
        this.HeadElement.appendChild(this.SetScript("Dlls/JS/MainForm/MainForm.js"));
    }

    SetScript(src: string)
    {
        var Script = document.createElement("script");  
        Script.src = src;
        return Script;
    }
}
var Sl = new ScriptList();