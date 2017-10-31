class TcpClient
{
    WebSocket: WebSocket;
    ReceiveEvent: Events;
    OpenEvent: Events;
    CloseEvent: Events;
    ErrorEvent: Events;
    IP: string;
    Port: string;
    Receive: string;
    constructor(_IP: string, _Port: string)
    {
        this.IP = _IP;
        this.Port = _Port;

        this.ReceiveEvent = Events["Instance"];

        this.OpenEvent = Events["Instance"];
        this.CloseEvent = Events["Instance"];
        this.ErrorEvent = Events["Instance"];
    }
    GetThis(This)
    {
        this.ReceiveEvent["this"] = This;
        this.OpenEvent["this"] = This;
        this.CloseEvent["this"] = This;
        this.ErrorEvent["this"] = This;
    }
    Open()
    {
        this.WebSocket = new WebSocket("ws://" + this.IP + ":" + this.Port);
        this.WebSocket.onopen = (i) => this.OpenEvent.EventTrigger(this, i);
        this.WebSocket.onclose = (i) => this.CloseEvent.EventTrigger(this, i);
        this.WebSocket.onerror = (i) => this.ErrorEvent.EventTrigger(this, i);
        this.WebSocket.onmessage = (i) =>
        {
            if (i.data == "开始")
            {
                this.Receive = "";
            }
            else if (i.data.indexOf("结束") > -1)
            {
                var strlist = i.data.split(":");

                this.ReceiveEvent.EventTrigger(strlist[1], this.Receive);
            }
            else 
            {
                this.Receive += i.data;
            }
        }
    }
    Send(json: string)
    {
        if (this.WebSocket != null)
        {
            this.WebSocket.send(json);
        }
    }
}