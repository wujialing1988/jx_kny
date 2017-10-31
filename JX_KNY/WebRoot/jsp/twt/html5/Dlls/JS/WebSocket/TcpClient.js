var TcpClient = (function () {
    function TcpClient(_IP, _Port) {
        this.IP = _IP;
        this.Port = _Port;
        this.ReceiveEvent = Events["Instance"];
        this.OpenEvent = Events["Instance"];
        this.CloseEvent = Events["Instance"];
        this.ErrorEvent = Events["Instance"];
    }
    TcpClient.prototype.GetThis = function (This) {
        this.ReceiveEvent["this"] = This;
        this.OpenEvent["this"] = This;
        this.CloseEvent["this"] = This;
        this.ErrorEvent["this"] = This;
    };
    TcpClient.prototype.Open = function () {
        var _this = this;
        this.WebSocket = new WebSocket("ws://" + this.IP + ":" + this.Port);
        this.WebSocket.onopen = function (i) { return _this.OpenEvent.EventTrigger(_this, i); };
        this.WebSocket.onclose = function (i) { return _this.CloseEvent.EventTrigger(_this, i); };
        this.WebSocket.onerror = function (i) { return _this.ErrorEvent.EventTrigger(_this, i); };
        this.WebSocket.onmessage = function (i) {
            if (i.data == "开始") {
                _this.Receive = "";
            }
            else if (i.data.indexOf("结束") > -1) {
                var strlist = i.data.split(":");
                _this.ReceiveEvent.EventTrigger(strlist[1], _this.Receive);
            }
            else {
                _this.Receive += i.data;
            }
        };
    };
    TcpClient.prototype.Send = function (json) {
        if (this.WebSocket != null) {
            this.WebSocket.send(json);
        }
    };
    return TcpClient;
})();
//# sourceMappingURL=TcpClient.js.map