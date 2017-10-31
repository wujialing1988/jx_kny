var ScriptList = (function () {
    function ScriptList() {
        this.HeadElement = document.getElementById("Head");
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
    ScriptList.prototype.SetScript = function (src) {
        var Script = document.createElement("script");
        Script.src = src;
        return Script;
    };
    return ScriptList;
})();
var Sl = new ScriptList();
//# sourceMappingURL=ScriptList.js.map