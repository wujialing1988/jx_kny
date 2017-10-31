/// <reference path="../../../Scripts/jquery.d.ts" />
var Canvas = (function () {
    function Canvas() {
        this.S = new Svg();
        var body = document.getElementById("body");
        this.PanEnabled = true;
        this.Svg = this.S.GetSvg();
        this.Svg.setAttribute("width", document.body.clientWidth.toString());
        this.Svg.setAttribute("height", document.body.clientHeight.toString());
        this.Svg.viewBox.baseVal.x = 0;
        this.Svg.viewBox.baseVal.y = 0;
        this.Svg.viewBox.baseVal.width = document.body.clientWidth;
        this.Svg.viewBox.baseVal.height = document.body.clientHeight;
        this.Svg["This"] = this;
        this.currentZoomFactor = 1;
        /*(<any>window).Touch ? this.TouchEventRegistration() :*/ this.MouseEventRegistration();
    }
    Canvas.prototype.ShowTooltip = function (evt) {
        try {
            var s = this["Graphics"];
            var Name = document.getElementById("Name");
            Name.textContent = s.TrainNumber;
            var State = document.getElementById("State");
            if (s.TrainStateName == "") {
                State.textContent = "状态：无";
            }
            else {
                State.textContent = "状态：" + s.TrainStateName;
            }
            var PositionTime = document.getElementById("PositionTime");
            //if (s.TrainDevicesTime == "" || s.TrainDevicesTime == undefined )
            //{
            PositionTime.textContent = "上台位的时间：无";
            //}
            //else
            //{
            //    PositionTime.textContent = "上台位的时间：" + s.TrainDevicesTime;
            //}
            var Position = document.getElementById("Position");
            Position.textContent = "台位：无";
        }
        catch (Exception) {
        }
        var tooltip = document.getElementById("Tooltip");
        tooltip.style.left = evt.clientX + 10 + "px";
        tooltip.style.top = evt.clientY + 30 + "px";
        tooltip.style.display = "block";
    };
    Canvas.prototype.SvgClick = function (evt) {
        if (evt.button == 2) {
            var _this = this["this"];
            var s = document.getElementById("layer");
            var lean_overlay = document.getElementById("lean_overlay");
            lean_overlay.style.display = "block";
            lean_overlay.style.opacity = "0.5";
            s.style.display = "block";
            s.style.position = "fixed";
            s.style.zIndex = "11000";
            s.style.left = ((window.screen.width / 2) - (parseInt(s.style.width) / 2)) + "px";
            //  s.style.marginLeft = (parseInt(s.style.width) / 2) + "px";
            s.style.top = "300px";
            lean_overlay.onclick = function () { _this.PanEnabled = true; s.style.display = "none"; lean_overlay.style.display = "none"; };
        }
    };
    Canvas.prototype.HideTooltip = function (evt) {
        var tooltip = document.getElementById("Tooltip");
        tooltip.style.display = "none";
    };
    Canvas.prototype.PassValue = function () {
        this.MainForm = document.getElementById("Main")["Main"];
        this.CanvasTcp = document.getElementById("Main")["Web"];
        this.Document = document;
        this.Message = new MessageClass();
        this.Message.head = "GetMap";
        this.Message.message = "GetMap";
        this.CanvasTcp.GetThis(this);
        this.CanvasTcp.OpenEvent.AddEventCallback(this.Open);
        this.CanvasTcp.ReceiveEvent.AddEventCallback(this.Receive);
        this.CanvasTcp.CloseEvent.AddEventCallback(this.Close);
        this.CanvasTcp.ErrorEvent.AddEventCallback(this.Error);
        this.CanvasTcp.Open();
    };
    Canvas.prototype.Error = function (Tcp, Event) {
        var text = this["this"].MainForm.getElementById("text");
        text.style.visibility = "visible";
        if (this["this"].Svg != null) {
            try {
                this["this"].Document.body.removeChild(this["this"].Svg);
            }
            catch (e) { }
        }
    };
    Canvas.prototype.Close = function (Tcp, Event) {
        var text = this["this"].MainForm.getElementById("text");
        text.style.visibility = "visible";
        if (this["this"].Svg != null) {
            try {
                this["this"].Document.body.removeChild(this["this"].Svg);
            }
            catch (e) { }
        }
    };
    Canvas.prototype.Open = function (Tcp, Event) {
        var text = this["this"].MainForm.getElementById("text");
        text.style.visibility = "collapse";
        this["this"].CanvasTcp.Send(JSON.stringify(this["this"].Message));
    };
    Canvas.prototype.Receive = function (Tcp, Message) {
        switch (Tcp) {
            case "GetMap":
                this["this"].DrawBackground();
                var read = new Read();
                var dictionary = read.ReadCanvas(Message, null, this["this"]);
                this["this"].TrackLineList = dictionary["TrackLineList"];
                this["this"].PositionList = dictionary["PositionList"];
                this["this"].SwitchesList = dictionary["SwitchesList"];
                this["this"].LightList = dictionary["LightList"];
                var text = this["this"].MainForm.getElementById("Title");
                if (dictionary.ContainsKey("name")) {
                    text.textContent = dictionary["name"];
                }
                else {
                    text.textContent = "电子地图";
                }
                var GetTrainColors = new MessageClass();
                GetTrainColors.head = "GetTrainColors";
                GetTrainColors.message = "GetTrainColors";
                this["this"].CanvasTcp.Send(JSON.stringify(GetTrainColors));
                break;
            case "GetTrainColors":
                this["this"].TrainColorList(Message);
                var GetTrain = new MessageClass();
                GetTrain.head = "GetTrain";
                GetTrain.message = "GetTrain";
                this["this"].CanvasTcp.Send(JSON.stringify(GetTrain));
                break;
            case "GetTrain":
                this["this"].Train(Message);
                break;
            default:
                return null;
                break;
        }
    };
    Canvas.prototype.Train = function (Message) {
        var _this = this;
        if (Message != "") {
            var s = new Array();
            this.Svg.childNodes["ForEach"](function (j) {
                if (j["Graphics"] instanceof Train) {
                    s.push(j);
                }
            }, this);
            s["ForEach"](function (j) { _this.Svg.removeChild(j); j = null; }, this);
            this.Svg.childNodes["ForEach"](function (j) {
                if (j["Graphics"] instanceof TrackLine) {
                    var s = j["Graphics"];
                    s.TrainList = [];
                }
            }, this);
            var TrainList = JSON.parse(Message);
            for (var i = 0; i < TrainList.length; i++) {
                var trackLine = this.TrackLineList["Where"](function (j) { return j.Guid == TrainList[i].所在设备GUID.toString(); }, this);
                var A = this.S.GetSVGPoint();
                var B = this.S.GetSVGPoint();
                if (trackLine.length > 0) {
                    var Track = trackLine[0];
                    if (Track.PointList.length == 3) {
                        A = Track.PointList[0]["ParsePoint"](Track.PointList[0]);
                        B = Track.PointList[2]["ParsePoint"](Track.PointList[2]);
                    }
                    else {
                        A = Track.PointList[0]["ParsePoint"](Track.PointList[0]);
                        B = Track.PointList[1]["ParsePoint"](Track.PointList[1]);
                    }
                    var f = A["GetSlope"](A, B);
                    var l = f["GetSlope1"](f);
                    if (Track.TrainList.length == 0) {
                        var w = (A["GetDistance"](A, B) - A["GetDistance"](A, A["CentralPoint"](A, A["CentralPoint"](A, B))) - 5);
                        if (w > 100) {
                            w = 100;
                        }
                        else if (w <= 0) {
                            w = 1;
                        }
                        var Center = A["CentralPoint"](A, A["CentralPoint"](A, A["CentralPoint"](A, B)));
                        var train = new Train(w, Center["GetPath1"](Center, l, 25)[0], Track, this.Svg, TrainList[i].简写名称, TrainList[i].设备时间, true);
                        train.TrainStateName = TrainList[i].状态;
                        Track.TrainList.push(train);
                        var Train1 = train.OnRender();
                        Train1[0]["this"] = this;
                        Train1[1]["this"] = this;
                        Train1[2]["this"] = this;
                        Train1[0].onmousedown = this.SvgClick;
                        Train1[1].onmousedown = this.SvgClick;
                        Train1[2].onmousedown = this.SvgClick;
                        Train1[0].onmousemove = this.ShowTooltip;
                        Train1[0].onmouseout = this.HideTooltip;
                        Train1[1].onmousemove = this.ShowTooltip;
                        Train1[1].onmouseout = this.HideTooltip;
                        Train1[2].onmousemove = this.ShowTooltip;
                        Train1[2].onmouseout = this.HideTooltip;
                        this.Svg.appendChild(Train1[0]);
                        this.Svg.appendChild(Train1[1]);
                        this.Svg.appendChild(Train1[2]);
                    }
                    else {
                        var w = (A["GetDistance"](A, B) - A["GetDistance"](A, A["CentralPoint"](A, A["CentralPoint"](A, B))) - 5);
                        if (w > 100) {
                            w = 100;
                        }
                        else if (w <= 0) {
                            w = 1;
                        }
                        var train = new Train(w, this.S.GetSVGPoint(), Track, this.Svg, TrainList[i].简写名称, TrainList[i].设备时间, true);
                        train.Angle = ((Math.atan2(B.y - A.y, B.x - A.x) * 180) / Math.PI);
                        if (train.Angle >= 90) {
                            train.Angle = train.Angle - 180;
                        }
                        if (train.Angle <= -90) {
                            train.Angle = train.Angle + 180;
                        }
                        var bb = A["GetC"](A, A["CentralPoint"](A, A["CentralPoint"](A, A["CentralPoint"](A, B))), train.GraphicsWidth * Track.TrainList.length + 1 * Track.TrainList.length, 180);
                        train.Location = bb["GetPath1"](bb, l, 25)[0];
                        train.TrainStateName = TrainList[i].状态;
                        Track.TrainList.push(train);
                        var Train1 = train.OnRender();
                        Train1[0]["this"] = this;
                        Train1[1]["this"] = this;
                        Train1[2]["this"] = this;
                        Train1[0].onmousedown = this.SvgClick;
                        Train1[1].onmousedown = this.SvgClick;
                        Train1[2].onmousedown = this.SvgClick;
                        Train1[0].onmousemove = this.ShowTooltip;
                        Train1[0].onmouseout = this.HideTooltip;
                        Train1[1].onmousemove = this.ShowTooltip;
                        Train1[1].onmouseout = this.HideTooltip;
                        Train1[2].onmousemove = this.ShowTooltip;
                        Train1[2].onmouseout = this.HideTooltip;
                        this.Svg.appendChild(Train1[0]);
                        this.Svg.appendChild(Train1[1]);
                        this.Svg.appendChild(Train1[2]);
                    }
                    this.ChangeTrain(Track);
                }
            }
        }
    };
    Canvas.prototype.ChangeTrain = function (Track) {
        var A = this.S.GetSVGPoint();
        var B = this.S.GetSVGPoint();
        if (Track.PointList.length == 3) {
            A = Track.PointList[0]["ParsePoint"](Track.PointList[0]);
            B = Track.PointList[2]["ParsePoint"](Track.PointList[2]);
        }
        else {
            A = Track.PointList[0]["ParsePoint"](Track.PointList[0]);
            B = Track.PointList[1]["ParsePoint"](Track.PointList[1]);
        }
        var f = A["GetSlope"](A, B);
        var l = f["GetSlope1"](f);
        var width = A["CentralPoint"](A, A["CentralPoint"](A, A["CentralPoint"], B))["GetDistance"](A, B);
        var rectWidth = (width - (Track.TrainList.length + 2) * 1) / (Track.TrainList.length + 1);
        if (rectWidth > 100) {
            rectWidth = 100;
        }
        if (rectWidth < 0) {
            rectWidth = 1;
        }
        for (var i = 0; i < Track.TrainList.length; i++) {
            if (i == 0) {
                this.Svg.childNodes["ForEach"](function (j) {
                    if (j["Graphics"] instanceof Train) {
                        var train = j["Graphics"];
                        if (train.TrainNumber == Track.TrainList[i].TrainNumber) {
                            if (j instanceof SVGRectElement) {
                                var Rect = j;
                                Rect.width.baseVal.value = rectWidth;
                                var Central = A["CentralPoint"](A, A["CentralPoint"](A, A["CentralPoint"](A, B)));
                                Rect.x.baseVal.value = Central["GetPath1"](Central, l, 25)[0].x;
                                Rect.y.baseVal.value = Central["GetPath1"](Central, l, 25)[0].y;
                            }
                            else {
                                var text = j;
                                var Central = A["CentralPoint"](A, A["CentralPoint"](A, A["CentralPoint"](A, B)));
                                var x = Central["GetPath1"](Central, l, 25)[0].x;
                                var y = Central["GetPath1"](Central, l, 25)[0].y;
                                var W = (16 * train.TrainNumber.length);
                                if (text["Name"] == "TrainName") {
                                    if (W > rectWidth) {
                                        text.textContent = (text.textContent.replace(/(\s)*([a-zA-Z0-9]+|\W)(\.\.\.)?$/, "..."));
                                    }
                                    text.setAttribute("transform", "rotate(" + train.Angle + " " + x + "," + y + ")");
                                    text.setAttribute("x", (x + 5).toString());
                                }
                                else if (text["Name"] == "TrainStateName") {
                                    if (W > rectWidth) {
                                        text.textContent = (text.textContent.replace(/(\s)*([a-zA-Z0-9]+|\W)(\.\.\.)?$/, "..."));
                                    }
                                    text.setAttribute("transform", "rotate(" + train.Angle + " " + x + "," + y + ")");
                                    text.setAttribute("x", (x + 5).toString());
                                }
                            }
                        }
                    }
                }, this);
            }
            else {
                this.Svg.childNodes["ForEach"](function (j) {
                    if (j["Graphics"] instanceof Train) {
                        var train = j["Graphics"];
                        if (train.TrainNumber == Track.TrainList[i].TrainNumber) {
                            if (j instanceof SVGRectElement) {
                                var Rect = j;
                                Rect.width.baseVal.value = rectWidth;
                                var Central = A["GetC"](A, A["CentralPoint"](A, A["CentralPoint"](A, A["CentralPoint"](A, B))), rectWidth * i + 1 * i, 180);
                                Rect.x.baseVal.value = Central["GetPath1"](Central, l, 25)[0].x + (rectWidth * i);
                                Rect.y.baseVal.value = Central["GetPath1"](Central, l, 25)[0].y;
                            }
                            else {
                                var text = j;
                                var Central = A["GetC"](A, A["CentralPoint"](A, A["CentralPoint"](A, A["CentralPoint"](A, B))), rectWidth * i + 1 * i, 180);
                                var x = Central["GetPath1"](Central, l, 25)[0].x + (rectWidth * i);
                                var y = Central["GetPath1"](Central, l, 25)[0].y;
                                var W = (16 * train.TrainNumber.length);
                                if (text["Name"] == "TrainName") {
                                    if (W > rectWidth) {
                                        text.textContent = (text.textContent.replace(/(\s)*([a-zA-Z0-9]+|\W)(\.\.\.)?$/, "..."));
                                    }
                                    text.setAttribute("transform", "rotate(" + train.Angle + " " + x + "," + y + ")");
                                    text.setAttribute("x", (x + 5).toString());
                                }
                                else if (text["Name"] == "TrainStateName") {
                                    if (W > rectWidth) {
                                        text.textContent = (text.textContent.replace(/(\s)*([a-zA-Z0-9]+|\W)(\.\.\.)?$/, "..."));
                                    }
                                    text.setAttribute("transform", "rotate(" + train.Angle + " " + x + "," + y + ")");
                                    text.setAttribute("x", (x + 5).toString());
                                }
                            }
                        }
                    }
                }, this);
            }
        }
        return rectWidth;
    };
    Canvas.prototype.TrainColorList = function (Message) {
        var _this = this;
        if (Message != "") {
            var div = this.MainForm.getElementById("TrainColor");
            while (div.hasChildNodes()) {
                div.removeChild(div.firstChild);
            }
            var TrainColor = JSON.parse(Message);
            for (var i = 0; i < TrainColor.length; i++) {
                var frameObj = document.createElement("iframe");
                frameObj.id = TrainColor[i].Status;
                frameObj.src = "Dlls/HTML/TrainColorControl/TrainColorControl.html";
                frameObj.width = "100px";
                frameObj.style.border = "0px";
                var div = this.MainForm.getElementById("TrainColor");
                div.appendChild(frameObj);
                frameObj.onload = function () {
                    for (var i = 0; i < TrainColor.length; i++) {
                        var frame = _this.MainForm.getElementById(TrainColor[i].Status).contentWindow;
                        var SL = frame.document.getElementById("SL");
                        SL.textContent = "0台";
                        var MC = frame.document.getElementById("MC");
                        MC.textContent = TrainColor[i].Status;
                        var YS = frame.document.getElementById("YS");
                        YS.style.background = TrainColor[i].Color;
                    }
                };
            }
        }
        var div = this.MainForm.getElementById("TrainColor");
        var frameObj = document.createElement("iframe");
        frameObj.id = "总计";
        frameObj.src = "Dlls/HTML/TrainColorControl/TrainColorControl.html";
        frameObj.width = "100px";
        frameObj.style.border = "0px";
        div.appendChild(frameObj);
    };
    Canvas.prototype.DrawBackground = function () {
        this.rect = this.S.GetSVGRect();
        this.rect.x.baseVal.value = 0;
        this.rect.y.baseVal.value = 0;
        this.rect.width.baseVal.value = 1024;
        this.rect.height.baseVal.value = 550;
        this.rect.rx.baseVal.value = 10;
        this.rect.ry.baseVal.value = 10;
        this.rect.style.background = "black";
        this.Svg.appendChild(this.rect);
        document.body.appendChild(this.Svg);
    };
    Canvas.prototype.MouseEventRegistration = function () {
        var _this = this;
        this.Svg.onmousedown = this.Press;
        this.Svg.onmousemove = this.Move;
        this.Svg.onmouseup = this.Up;
        this.Svg.ontouchstart = function (i) {
            var s = _this["This"];
            if (s.PanEnabled = true) {
                s.clickPoint = s.S.GetSVGPoint();
                s.clickPoint.x = i.touches.item(0).clientX;
                s.clickPoint.y = i.touches.item(0).clientY;
                s.PanEnabled = false;
            }
        };
        this.Svg.ontouchmove = function (i) {
            var s = _this["This"];
            if (s.PanEnabled == false) {
                var viewBox = s.Svg.getAttribute('viewBox');
                s.viewBoxValues = viewBox.split(' ');
                s.viewBoxValues[0] = parseFloat(s.viewBoxValues[0]).toString();
                s.viewBoxValues[1] = parseFloat(s.viewBoxValues[1]).toString();
                var p = s.S.GetSVGPoint();
                p.x = parseFloat(s.viewBoxValues[0]);
                p.y = parseFloat(s.viewBoxValues[1]);
                s.viewBoxValues[0] = (p.x + ((s.clickPoint.x - i.touches.item(0).clientX) / s.currentZoomFactor)).toString();
                s.viewBoxValues[1] = (p.y + ((s.clickPoint.y - i.touches.item(0).clientY) / s.currentZoomFactor)).toString();
                s.Svg.setAttribute('viewBox', s.viewBoxValues.join(' '));
                s.clickPoint.x = i.touches.item(0).clientX;
                s.clickPoint.y = i.touches.item(0).clientY;
            }
        };
        this.Svg.ontouchend = function (i) {
            var s = _this["This"];
            s.PanEnabled = true;
        };
        this.Svg.addEventListener('mousewheel', this.Wheel, false);
    };
    Canvas.prototype.TouchEventRegistration = function () {
    };
    Canvas.prototype.Wheel = function (i) {
        var _this = this["This"];
        var viewBox = _this.Svg.getAttribute('viewBox');
        var viewBoxValues = viewBox.split(' ');
        viewBoxValues[2] = parseFloat(viewBoxValues[2]);
        viewBoxValues[3] = parseFloat(viewBoxValues[3]);
        if (i.wheelDelta > 0) {
            viewBoxValues[2] /= 1.1;
            viewBoxValues[3] /= 1.1;
            _this.currentZoomFactor *= 1.1;
        }
        else {
            viewBoxValues[2] *= 1.1;
            viewBoxValues[3] *= 1.1;
            _this.currentZoomFactor /= 1.1;
        }
        _this.Svg.setAttribute('viewBox', viewBoxValues.join(' '));
    };
    Canvas.prototype.Up = function (i) {
        var _this = this["This"];
        _this.PanEnabled = true;
    };
    Canvas.prototype.Press = function (i) {
        var _this = this["This"];
        if (_this.PanEnabled = true) {
            _this.clickPoint = _this.S.GetSVGPoint();
            _this.clickPoint.x = i.x;
            _this.clickPoint.y = i.y;
            _this.PanEnabled = false;
        }
    };
    Canvas.prototype.Move = function (i) {
        var _this = this["This"];
        if (_this.PanEnabled == false) {
            var viewBox = _this.Svg.getAttribute('viewBox');
            _this.viewBoxValues = viewBox.split(' ');
            _this.viewBoxValues[0] = parseFloat(_this.viewBoxValues[0]).toString();
            _this.viewBoxValues[1] = parseFloat(_this.viewBoxValues[1]).toString();
            var p = _this.S.GetSVGPoint();
            p.x = parseFloat(_this.viewBoxValues[0]);
            p.y = parseFloat(_this.viewBoxValues[1]);
            _this.viewBoxValues[0] = (p.x + ((_this.clickPoint.x - i.x) / _this.currentZoomFactor)).toString();
            _this.viewBoxValues[1] = (p.y + ((_this.clickPoint.y - i.y) / _this.currentZoomFactor)).toString();
            _this.Svg.setAttribute('viewBox', _this.viewBoxValues.join(' '));
            _this.clickPoint.x = i.x;
            _this.clickPoint.y = i.y;
        }
    };
    return Canvas;
})();
var s;
function PassValue() {
    if (s != null) {
        s.PassValue();
    }
}
function load() {
    s = new Canvas();
}
//# sourceMappingURL=CanvasJS.js.map