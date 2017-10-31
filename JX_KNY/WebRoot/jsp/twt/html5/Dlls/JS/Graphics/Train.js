/// <reference path="../../../Scripts/jquery.d.ts" />
/// <reference path="../../../Dlls/JS/LinQ/LINQ.ts" />
/// <reference path="../../../Dlls/JS/Pollution/Pollution.ts" />
///机车绘制类
var Train = (function () {
    function Train(_width, _location, _TrackLine, _CanvasControl, _TrainNumber, _TrainDevicesTime, b) {
        this.S = new Svg();
        this.GraphicsPen = "";
        this.GraphicsType = "";
        this.TrainGuid = this.Guid();
        this.GraphicsTransForm = "";
        this.GraphicsWidth = _width;
        this.TrainDevicesTime = _TrainDevicesTime;
        this.TrackLine = _TrackLine;
        if (this.TrackLine.PointList.length == 3) {
            this.A = this.TrackLine.PointList[0]["ParsePoint"](this.TrackLine.PointList[0]);
            this.B = this.TrackLine.PointList[2]["ParsePoint"](this.TrackLine.PointList[2]);
        }
        else {
            this.A = this.TrackLine.PointList[0]["ParsePoint"](this.TrackLine.PointList[0]);
            this.B = this.TrackLine.PointList[1]["ParsePoint"](this.TrackLine.PointList[1]);
        }
        this.CanvasControl = _CanvasControl;
        this.GraphicsHeight = 25;
        this.Location = _location;
        this.Angle = ((Math.atan2(this.B.y - this.A.y, this.B.x - this.A.x) * 180) / Math.PI);
        this.TrainNumber = _TrainNumber;
    }
    Train.prototype.OnRender = function () {
        var SvgList = new Array();
        var Rect = this.S.GetSVGRect();
        Rect["Graphics"] = this;
        Rect.style.fill = "rgba(100,100,0,0.4)";
        Rect.style.stroke = "white";
        Rect.style.strokeWidth = "1px";
        Rect.setAttribute("transform", "rotate(" + this.Angle + " " + this.Location.x + "," + this.Location.y + ")");
        Rect.x.baseVal.value = this.Location.x;
        Rect.y.baseVal.value = this.Location.y;
        Rect.rx.baseVal.value = 5;
        Rect.ry.baseVal.value = 5;
        Rect.width.baseVal.value = this.GraphicsWidth;
        Rect.height.baseVal.value = this.GraphicsHeight;
        SvgList.push(Rect);
        var Text = this.S.GetSvgText();
        Text["Graphics"] = this;
        Text["Name"] = "TrainName";
        Text.textContent = this.TrainNumber;
        Text.style.overflow = "hidden";
        Text.style.textOverflow = "ellipsis";
        Text.style.fontSize = "20px";
        Text.style.fill = "rgba(80,255,255,255)";
        var W = (16 * this.TrainNumber.length);
        Text.style.fontWeight = "bold";
        if (W > this.GraphicsWidth) {
            Text.textContent = (Text.textContent.replace(/(\s)*([a-zA-Z0-9]+|\W)(\.\.\.)?$/, "..."));
        }
        Text.setAttribute("transform", "rotate(" + this.Angle + " " + this.Location.x + "," + this.Location.y + ")");
        Text.setAttribute("x", (this.Location.x + 5).toString());
        Text.setAttribute("y", (this.Location.y - 5).toString());
        SvgList.push(Text);
        var Text = this.S.GetSvgText();
        Text["Graphics"] = this;
        Text["Name"] = "TrainStateName";
        Text.textContent = this.TrainStateName;
        Text.style.overflow = "hidden";
        Text.style.textOverflow = "ellipsis";
        Text.style.fontSize = "20px";
        Text.style.fill = "rgba(80,255,255,255)";
        var W = (16 * this.TrainNumber.length);
        Text.style.fontWeight = "bold";
        if (W > this.GraphicsWidth) {
            Text.textContent = (Text.textContent.replace(/(\s)*([a-zA-Z0-9]+|\W)(\.\.\.)?$/, "..."));
        }
        Text.setAttribute("transform", "rotate(" + this.Angle + " " + this.Location.x + "," + this.Location.y + ")");
        Text.setAttribute("x", (this.Location.x + 5).toString());
        Text.setAttribute("y", (this.Location.y + 20.5).toString());
        SvgList.push(Text);
        return SvgList;
    };
    Train.prototype.Guid = function () {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    };
    return Train;
})();
//# sourceMappingURL=Train.js.map