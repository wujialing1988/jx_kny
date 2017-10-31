var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
/// <reference path="../../../Scripts/jquery.d.ts" />
/// <reference path="../../../Dlls/JS/LinQ/LINQ.ts" />
/// <reference path="../../../Dlls/JS/Pollution/Pollution.ts" />
var OpenAngle;
(function (OpenAngle) {
    OpenAngle[OpenAngle["小角度"] = 0] = "小角度";
    OpenAngle[OpenAngle["大角度"] = 1] = "大角度";
})(OpenAngle || (OpenAngle = {}));
var State;
(function (State) {
    State[State["定位"] = 0] = "定位";
    State[State["反位"] = 1] = "反位";
    State[State["中位"] = 2] = "中位";
})(State || (State = {}));
var Sw;
(function (Sw) {
    Sw[Sw["开"] = 0] = "开";
    Sw[Sw["断"] = 1] = "断";
    Sw[Sw["关"] = 2] = "关";
})(Sw || (Sw = {}));
//道岔的动作
var SwitchesAction = (function () {
    function SwitchesAction() {
    }
    //道岔向上的动作
    SwitchesAction.prototype.Up = function (sw, Point, OpenAngle) {
        switch (sw.LeftRight.toString()) {
            case "左":
                return this.LeftUp(sw, Point, OpenAngle);
            case "右":
                return this.RightUp(sw, Point, OpenAngle);
            default:
                break;
        }
        return null;
    };
    //道岔向下的动作
    SwitchesAction.prototype.Down = function (sw, Point, OpenAngle) {
        switch (sw.LeftRight.toString()) {
            case "左":
                return this.LeftDown(sw, Point, OpenAngle);
            case "右":
                return this.RightDown(sw, Point, OpenAngle);
            default:
                break;
        }
        return null;
    };
    //道岔向左的动作
    SwitchesAction.prototype.Left = function (sw, Point, OpenAngle) {
        switch (sw.UpDown.toString()) {
            case "上":
                return this.LeftUp(sw, Point, OpenAngle);
            case "下":
                return this.LeftDown(sw, Point, OpenAngle);
            default:
                break;
        }
        return null;
    };
    //道岔向右的动作
    SwitchesAction.prototype.Right = function (sw, Point, OpenAngle) {
        switch (sw.UpDown.toString()) {
            case "上":
                return this.RightUp(sw, Point, OpenAngle);
            case "下":
                return this.RightDown(sw, Point, OpenAngle);
            default:
                break;
        }
        return null;
    };
    return SwitchesAction;
})();
// 道岔的开状态
var SwitchesOpen = (function (_super) {
    __extends(SwitchesOpen, _super);
    function SwitchesOpen() {
        _super.call(this);
        this.S = new Svg();
    }
    // 左上的情况
    SwitchesOpen.prototype.LeftUp = function (sw, Point, OpenAngle) {
        var SvgList = new Array();
        var MinRect = this.S.GetSVGRect();
        MinRect.x.baseVal.value = Point.x - sw.Width / 2;
        MinRect.y.baseVal.value = Point.y - sw.Height / 2;
        MinRect.width.baseVal.value = sw.Width;
        MinRect.height.baseVal.value = sw.Height;
        var MaxRect = this.S.GetSVGRect();
        MaxRect.x.baseVal.value = Point.x - sw.Width / 2;
        MaxRect.y.baseVal.value = Point.y - sw.Height / 2;
        MaxRect.width.baseVal.value = sw.Width;
        MaxRect.height.baseVal.value = sw.Height + 5;
        var Polyline = this.S.GetSvgPolyline();
        Polyline["Graphics"] = this;
        Polyline.style.stroke = "white";
        Polyline.style.strokeWidth = sw.PenThickness;
        var points = Polyline.points;
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value;
        point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
        points.appendItem(point);
        var point = this.S.GetSVGPoint();
        point.x = (MinRect.width.baseVal.value / 3) + MinRect.x.baseVal.value;
        point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
        points.appendItem(point);
        sw.NeatenPoint = this.S.GetSVGPoint();
        sw.NeatenPoint.x = (MinRect.width.baseVal.value / 3) + MinRect.y.baseVal.value;
        sw.NeatenPoint.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
        if (OpenAngle.toString() == "小角度") {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 2) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value;
            points.appendItem(point);
        }
        else {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 1.3) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value;
            points.appendItem(point);
        }
        Polyline.points = points;
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);
        var Polyline = this.S.GetSvgPolyline();
        Polyline["Graphics"] = this;
        Polyline.style.stroke = "white";
        Polyline.style.strokeWidth = sw.PenThickness;
        var points = Polyline.points;
        var point = this.S.GetSVGPoint();
        point.x = (MinRect.x.baseVal.value + MinRect.width.baseVal.value) - 10;
        point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
        points.appendItem(point);
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value + MinRect.width.baseVal.value;
        point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
        points.appendItem(point);
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);
        return SvgList;
    };
    // 左下的情况
    SwitchesOpen.prototype.LeftDown = function (sw, Point, OpenAngle) {
        var SvgList = new Array();
        var MinRect = this.S.GetSVGRect();
        MinRect.x.baseVal.value = Point.x - sw.Width / 2;
        MinRect.y.baseVal.value = Point.y - sw.Height / 2;
        MinRect.width.baseVal.value = sw.Width;
        MinRect.height.baseVal.value = sw.Height;
        var MaxRect = this.S.GetSVGRect();
        MaxRect.x.baseVal.value = Point.x - sw.Width / 2;
        MaxRect.y.baseVal.value = Point.y - sw.Height / 2;
        MaxRect.width.baseVal.value = sw.Width;
        MaxRect.height.baseVal.value = sw.Height + 5;
        var Polyline = this.S.GetSvgPolyline();
        Polyline["Graphics"] = this;
        Polyline.style.stroke = "white";
        Polyline.style.strokeWidth = sw.PenThickness;
        var points = Polyline.points;
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value;
        point.y = MinRect.y.baseVal.value;
        points.appendItem(point);
        var point = this.S.GetSVGPoint();
        point.x = (MinRect.width.baseVal.value / 3) + MinRect.x.baseVal.value;
        point.y = MinRect.y.baseVal.value;
        points.appendItem(point);
        sw.NeatenPoint = this.S.GetSVGPoint();
        sw.NeatenPoint.x = (MinRect.width.baseVal.value / 3) + MinRect.y.baseVal.value;
        sw.NeatenPoint.y = MinRect.y.baseVal.value;
        if (OpenAngle.toString() == "小角度") {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 2) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
            points.appendItem(point);
        }
        else {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 1.3) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
            points.appendItem(point);
        }
        Polyline.points = points;
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);
        var Polyline = this.S.GetSvgPolyline();
        Polyline["Graphics"] = this;
        Polyline.style.stroke = "white";
        Polyline.style.strokeWidth = sw.PenThickness;
        var points = Polyline.points;
        var point = this.S.GetSVGPoint();
        point.x = (MinRect.x.baseVal.value + MinRect.width.baseVal.value) - 10;
        point.y = MinRect.y.baseVal.value;
        points.appendItem(point);
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value + MinRect.width.baseVal.value;
        point.y = MinRect.y.baseVal.value;
        points.appendItem(point);
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);
        return SvgList;
    };
    // 右上的情况
    SwitchesOpen.prototype.RightUp = function (sw, Point, OpenAngle) {
        var SvgList = new Array();
        var MinRect = this.S.GetSVGRect();
        MinRect.x.baseVal.value = Point.x - sw.Width / 2;
        MinRect.y.baseVal.value = Point.y - sw.Height / 2;
        MinRect.width.baseVal.value = sw.Width;
        MinRect.height.baseVal.value = sw.Height;
        var MaxRect = this.S.GetSVGRect();
        MaxRect.x.baseVal.value = Point.x - sw.Width / 2;
        MaxRect.y.baseVal.value = Point.y - sw.Height / 2;
        MaxRect.width.baseVal.value = sw.Width;
        MaxRect.height.baseVal.value = sw.Height + 5;
        var Polyline = this.S.GetSvgPolyline();
        Polyline["Graphics"] = this;
        Polyline.style.stroke = "white";
        Polyline.style.strokeWidth = sw.PenThickness;
        var points = Polyline.points;
        if (OpenAngle.toString() == "小角度") {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 2) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value;
            points.appendItem(point);
        }
        else {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 3.5) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value;
            points.appendItem(point);
        }
        var point = this.S.GetSVGPoint();
        point.x = (MinRect.width.baseVal.value + MinRect.x.baseVal.value) - (MinRect.width.baseVal.value / 3);
        point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
        points.appendItem(point);
        sw.NeatenPoint = this.S.GetSVGPoint();
        sw.NeatenPoint.x = (MinRect.width.baseVal.value + MinRect.x.baseVal.value) - (MinRect.width.baseVal.value / 3);
        sw.NeatenPoint.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
        var point = this.S.GetSVGPoint();
        point.x = MinRect.width.baseVal.value + MinRect.x.baseVal.value;
        point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
        points.appendItem(point);
        Polyline.points = points;
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);
        var Polyline = this.S.GetSvgPolyline();
        Polyline["Graphics"] = this;
        Polyline.style.stroke = "white";
        Polyline.style.strokeWidth = sw.PenThickness;
        var points = Polyline.points;
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value + 10;
        point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
        points.appendItem(point);
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value;
        point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
        points.appendItem(point);
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);
        return SvgList;
    };
    // 右下的情况
    SwitchesOpen.prototype.RightDown = function (sw, Point, OpenAngle) {
        var SvgList = new Array();
        var MinRect = this.S.GetSVGRect();
        MinRect.x.baseVal.value = Point.x - sw.Width / 2;
        MinRect.y.baseVal.value = Point.y - sw.Height / 2;
        MinRect.width.baseVal.value = sw.Width;
        MinRect.height.baseVal.value = sw.Height;
        var MaxRect = this.S.GetSVGRect();
        MaxRect.x.baseVal.value = Point.x - sw.Width / 2;
        MaxRect.y.baseVal.value = (Point.y - sw.Height / 2) - 5;
        MaxRect.width.baseVal.value = sw.Width;
        MaxRect.height.baseVal.value = sw.Height + 5;
        var Polyline = this.S.GetSvgPolyline();
        Polyline["Graphics"] = this;
        Polyline.style.stroke = "white";
        Polyline.style.strokeWidth = sw.PenThickness;
        var points = Polyline.points;
        if (OpenAngle.toString() == "小角度") {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 2) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
            points.appendItem(point);
        }
        else {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 3.5) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
            points.appendItem(point);
        }
        var point = this.S.GetSVGPoint();
        point.x = (MinRect.width.baseVal.value + MinRect.x.baseVal.value) - (MinRect.width.baseVal.value / 3);
        point.y = MinRect.y.baseVal.value;
        points.appendItem(point);
        sw.NeatenPoint = this.S.GetSVGPoint();
        sw.NeatenPoint.x = (MinRect.width.baseVal.value + MinRect.x.baseVal.value) - (MinRect.width.baseVal.value / 3);
        sw.NeatenPoint.y = MinRect.y.baseVal.value;
        var point = this.S.GetSVGPoint();
        point.x = MinRect.width.baseVal.value + MinRect.x.baseVal.value;
        point.y = MinRect.y.baseVal.value;
        points.appendItem(point);
        Polyline.points = points;
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);
        var Polyline = this.S.GetSvgPolyline();
        Polyline["Graphics"] = this;
        Polyline.style.stroke = "white";
        Polyline.style.strokeWidth = sw.PenThickness;
        var points = Polyline.points;
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value + 10;
        point.y = MinRect.y.baseVal.value;
        points.appendItem(point);
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value;
        point.y = MinRect.y.baseVal.value;
        points.appendItem(point);
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);
        return SvgList;
    };
    return SwitchesOpen;
})(SwitchesAction);
// 道岔的关状态
var SwitchesClose = (function (_super) {
    __extends(SwitchesClose, _super);
    function SwitchesClose() {
        _super.call(this);
        this.S = new Svg();
    }
    // 左上的情况
    SwitchesClose.prototype.LeftUp = function (sw, Point, OpenAngle) {
        var SvgList = new Array();
        var MinRect = this.S.GetSVGRect();
        MinRect.x.baseVal.value = Point.x - sw.Width / 2;
        MinRect.y.baseVal.value = Point.y - sw.Height / 2;
        MinRect.width.baseVal.value = sw.Width;
        MinRect.height.baseVal.value = sw.Height;
        var MaxRect = this.S.GetSVGRect();
        MaxRect.x.baseVal.value = Point.x - sw.Width / 2;
        MaxRect.y.baseVal.value = Point.y - sw.Height / 2;
        MaxRect.width.baseVal.value = sw.Width;
        MaxRect.height.baseVal.value = sw.Height + 5;
        var Polyline = this.S.GetSvgPolyline();
        Polyline["Graphics"] = this;
        Polyline.style.stroke = "white";
        Polyline.style.strokeWidth = sw.PenThickness;
        var points = Polyline.points;
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value;
        point.y = (MinRect.y.baseVal.value + MinRect.height.baseVal.value) - 5;
        points.appendItem(point);
        var a = point;
        var point = this.S.GetSVGPoint();
        point.x = (MinRect.width.baseVal.value / 2.8) + MinRect.x.baseVal.value;
        point.y = a.y;
        points.appendItem(point);
        sw.NeatenPoint = this.S.GetSVGPoint();
        sw.NeatenPoint.x = (MinRect.width.baseVal.value / 2.8) + MinRect.x.baseVal.value;
        sw.NeatenPoint.y = a.y;
        if (OpenAngle.toString() == "小角度") {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 2) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value;
            points.appendItem(point);
        }
        else {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 1.3) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value;
            points.appendItem(point);
        }
        Polyline.points = points;
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);
        var Polyline = this.S.GetSvgPolyline();
        Polyline["Graphics"] = this;
        Polyline.style.stroke = "white";
        Polyline.style.strokeWidth = sw.PenThickness;
        var points = Polyline.points;
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value;
        point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
        points.appendItem(point);
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value + MinRect.width.baseVal.value;
        point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
        points.appendItem(point);
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);
        return SvgList;
    };
    // 左下的情况
    SwitchesClose.prototype.LeftDown = function (sw, Point, OpenAngle) {
        var SvgList = new Array();
        var MinRect = this.S.GetSVGRect();
        MinRect.x.baseVal.value = Point.x - sw.Width / 2;
        MinRect.y.baseVal.value = Point.y - sw.Height / 2;
        MinRect.width.baseVal.value = sw.Width;
        MinRect.height.baseVal.value = sw.Height;
        var MaxRect = this.S.GetSVGRect();
        MaxRect.x.baseVal.value = Point.x - sw.Width / 2;
        MaxRect.y.baseVal.value = (Point.y - sw.Height / 2) - 5;
        MaxRect.width.baseVal.value = sw.Width;
        MaxRect.height.baseVal.value = sw.Height + 5;
        var Polyline = this.S.GetSvgPolyline();
        Polyline["Graphics"] = this;
        Polyline.style.stroke = "white";
        Polyline.style.strokeWidth = sw.PenThickness;
        var points = Polyline.points;
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value;
        point.y = MinRect.y.baseVal.value + 5;
        points.appendItem(point);
        var a = point;
        var point = this.S.GetSVGPoint();
        point.x = (MinRect.width.baseVal.value / 2.8) + MinRect.x.baseVal.value;
        point.y = a.y;
        points.appendItem(point);
        sw.NeatenPoint = this.S.GetSVGPoint();
        sw.NeatenPoint.x = (MinRect.width.baseVal.value / 2.8) + MinRect.x.baseVal.value;
        sw.NeatenPoint.y = a.y;
        if (OpenAngle.toString() == "小角度") {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 2) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
            points.appendItem(point);
        }
        else {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 1.3) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
            points.appendItem(point);
        }
        Polyline.points = points;
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);
        var Polyline = this.S.GetSvgPolyline();
        Polyline["Graphics"] = this;
        Polyline.style.stroke = "white";
        Polyline.style.strokeWidth = sw.PenThickness;
        var points = Polyline.points;
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value;
        point.y = MinRect.y.baseVal.value;
        points.appendItem(point);
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value + MinRect.width.baseVal.value;
        point.y = MinRect.y.baseVal.value;
        points.appendItem(point);
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);
        return SvgList;
    };
    // 右上的情况
    SwitchesClose.prototype.RightUp = function (sw, Point, OpenAngle) {
        var SvgList = new Array();
        var MinRect = this.S.GetSVGRect();
        MinRect.x.baseVal.value = Point.x - sw.Width / 2;
        MinRect.y.baseVal.value = Point.y - sw.Height / 2;
        MinRect.width.baseVal.value = sw.Width;
        MinRect.height.baseVal.value = sw.Height;
        var MaxRect = this.S.GetSVGRect();
        MaxRect.x.baseVal.value = Point.x - sw.Width / 2;
        MaxRect.y.baseVal.value = Point.y - sw.Height / 2;
        MaxRect.width.baseVal.value = sw.Width;
        MaxRect.height.baseVal.value = sw.Height + 5;
        var Polyline = this.S.GetSvgPolyline();
        Polyline["Graphics"] = this;
        Polyline.style.stroke = "white";
        Polyline.style.strokeWidth = sw.PenThickness;
        var points = Polyline.points;
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value + MinRect.width.baseVal.value;
        point.y = (MinRect.y.baseVal.value + MinRect.height.baseVal.value) - 5;
        points.appendItem(point);
        var a = point;
        var point = this.S.GetSVGPoint();
        point.x = (MinRect.width.baseVal.value + MinRect.x.baseVal.value) - (MinRect.width.baseVal.value / 2.8);
        point.y = a.y;
        points.appendItem(point);
        sw.NeatenPoint = this.S.GetSVGPoint();
        sw.NeatenPoint.x = (MinRect.width.baseVal.value + MinRect.x.baseVal.value) - (MinRect.width.baseVal.value / 2.8);
        sw.NeatenPoint.y = a.y;
        if (OpenAngle.toString() == "小角度") {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 2) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value;
            points.appendItem(point);
        }
        else {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 3.5) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value;
            points.appendItem(point);
        }
        Polyline.points = points;
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);
        var Polyline = this.S.GetSvgPolyline();
        Polyline["Graphics"] = this;
        Polyline.style.stroke = "white";
        Polyline.style.strokeWidth = sw.PenThickness;
        var points = Polyline.points;
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value;
        point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
        points.appendItem(point);
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value + MinRect.width.baseVal.value;
        point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
        points.appendItem(point);
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);
        return SvgList;
    };
    // 右下的情况
    SwitchesClose.prototype.RightDown = function (sw, Point, OpenAngle) {
        var SvgList = new Array();
        var MinRect = this.S.GetSVGRect();
        MinRect.x.baseVal.value = Point.x - sw.Width / 2;
        MinRect.y.baseVal.value = Point.y - sw.Height / 2;
        MinRect.width.baseVal.value = sw.Width;
        MinRect.height.baseVal.value = sw.Height;
        var MaxRect = this.S.GetSVGRect();
        MaxRect.x.baseVal.value = Point.x - sw.Width / 2;
        MaxRect.y.baseVal.value = (Point.y - sw.Height / 2) - 5;
        MaxRect.width.baseVal.value = sw.Width;
        MaxRect.height.baseVal.value = sw.Height + 5;
        var Polyline = this.S.GetSvgPolyline();
        Polyline["Graphics"] = this;
        Polyline.style.stroke = "white";
        Polyline.style.strokeWidth = sw.PenThickness;
        var points = Polyline.points;
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value + MinRect.width.baseVal.value;
        point.y = MinRect.y.baseVal.value + 5;
        points.appendItem(point);
        var a = point;
        var point = this.S.GetSVGPoint();
        point.x = (MinRect.width.baseVal.value + MinRect.x.baseVal.value) - (MinRect.width.baseVal.value / 2.8);
        point.y = a.y;
        points.appendItem(point);
        sw.NeatenPoint = this.S.GetSVGPoint();
        sw.NeatenPoint.x = (MinRect.width.baseVal.value + MinRect.x.baseVal.value) - (MinRect.width.baseVal.value / 2.8);
        sw.NeatenPoint.y = a.y;
        if (OpenAngle.toString() == "小角度") {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 2) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
            points.appendItem(point);
        }
        else {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 3.5) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
            points.appendItem(point);
        }
        Polyline.points = points;
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);
        var Polyline = this.S.GetSvgPolyline();
        Polyline["Graphics"] = this;
        Polyline.style.stroke = "white";
        Polyline.style.strokeWidth = sw.PenThickness;
        var points = Polyline.points;
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value;
        point.y = MinRect.y.baseVal.value;
        points.appendItem(point);
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value + MinRect.width.baseVal.value;
        point.y = MinRect.y.baseVal.value;
        points.appendItem(point);
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);
        return SvgList;
    };
    return SwitchesClose;
})(SwitchesAction);
//道岔绘制类
var Switches = (function () {
    function Switches() {
        this.S = new Svg();
    }
    Switches.prototype.Open = function (sw, Point) {
        var SwitchesAction = new SwitchesOpen();
        switch (sw.UpDown.toString()) {
            case "上":
                return SwitchesAction.Up(sw, Point, sw.OpenAngle);
            case "下":
                return SwitchesAction.Down(sw, Point, sw.OpenAngle);
        }
        switch (sw.LeftRight.toString()) {
            case "左":
                return SwitchesAction.Left(sw, Point, sw.OpenAngle);
            case "右":
                return SwitchesAction.Right(sw, Point, sw.OpenAngle);
        }
        return null;
    };
    // 道岔的关状态
    Switches.prototype.Close = function (sw, Point) {
        var SwitchesAction = new SwitchesClose();
        switch (sw.UpDown.toString()) {
            case "上":
                return SwitchesAction.Up(sw, Point, sw.OpenAngle);
            case "下":
                return SwitchesAction.Down(sw, Point, sw.OpenAngle);
        }
        switch (sw.LeftRight.toString()) {
            case "左":
                return SwitchesAction.Left(sw, Point, sw.OpenAngle);
            case "右":
                return SwitchesAction.Right(sw, Point, sw.OpenAngle);
        }
        return null;
    };
    Switches.prototype.GraphicsRead = function (Switches) {
        this.GraphicsPen = Switches.attributes["图形画笔的颜色"].value;
        this.PenThickness = Switches.attributes["图形画笔的宽度"].value;
        this.GraphicsName = Switches.attributes["图形的名字"].value;
        this.Guid = Switches.attributes["图形的唯一标识符"].value;
        this.GraphicsCode = Switches.attributes["图形的编号"].value;
        this.Width = parseInt(Switches.attributes["图形的宽度"].value);
        this.Height = parseInt(Switches.attributes["图形的高度"].value);
        this.State = Switches.attributes["图形的定反位状态"].value;
        this.Segment = Switches.attributes["图形分节的颜色"].value;
        this.SwitchesPoint = Switches.attributes["图形绘制用的坐标"]["ParsePoint"](Switches.attributes["图形绘制用的坐标"].value);
        this.switche = Switches.attributes["图形的开关状态"].value;
        this.UpDown = Switches.attributes["图形的上下状态"].value;
        this.LeftRight = Switches.attributes["图形的左右状态"].value;
        this.OpenAngle = Switches.attributes["图形的开角"].value;
        this.PastSwitche = this.switche;
        try {
            this.TextPoint = Switches.attributes["字体的坐标"]["ParsePoint"](Switches.attributes["字体的坐标"].value);
            this.A = parseInt(Switches.attributes["A"].value);
            this.Move = Switches.attributes["移动坐标"]["ParsePoint"](Switches.attributes["移动坐标"].value);
            this.angle = parseInt(Switches.attributes["图形的角度"].value);
            this.Revolve = Switches.attributes["旋转坐标"]["ParsePoint"](Switches.attributes["旋转坐标"].value);
            this.GraphicsTransForm = "matrix(" + Switches.attributes["M11"].value + "," + Switches.attributes["M12"].value + "," + Switches.attributes["M21"].value + "," + Switches.attributes["M22"].value + "," + Switches.attributes["OffsetX"].value + "," + Switches.attributes["OffsetY"].value + ")";
        }
        catch (e) { }
    };
    Switches.prototype.GetText = function () {
        if (this.angle >= 90) {
            this.angle = this.angle - 180;
        }
        if (this.angle <= -90) {
            this.angle = this.angle + 180;
        }
        var Text = this.S.GetSvgText();
        Text["Graphics"] = this;
        Text.textContent = this.GraphicsName;
        Text.style.fontSize = "12px";
        Text.style.fill = "rgba(80,255,255,255)";
        var s = "rotate(" + this.angle + " " + (this.TextPoint.x + 7).toString() + "," + (this.TextPoint.y + 12).toString() + ")";
        Text.setAttribute("transform", s);
        Text.setAttribute("x", (this.TextPoint.x + 7).toString());
        Text.setAttribute("y", (this.TextPoint.y + 12).toString());
        return Text;
    };
    Switches.prototype.Render = function () {
        switch (this.switche.toString()) {
            case "开":
                var s = this.Open(this, this.SwitchesPoint);
                s.push(this.GetText());
                return s;
                break;
            case "关":
                var s = this.Close(this, this.SwitchesPoint);
                s.push(this.GetText());
                return s;
                break;
            default:
                return null;
                break;
        }
    };
    return Switches;
})();
//# sourceMappingURL=Switches.js.map