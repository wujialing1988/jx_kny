/// <reference path="../../../Scripts/jquery.d.ts" />
/// <reference path="../../../Dlls/JS/LinQ/LINQ.ts" />
/// <reference path="../../../Dlls/JS/Pollution/Pollution.ts" />
var Insulation = (function () {
    function Insulation(startPoint, endPoint) {
        this.S = new Svg();
        var StartPoint = this.S.GetSVGPoint();
        StartPoint.x = parseInt(startPoint.split(",")[0]);
        StartPoint.y = parseInt(startPoint.split(",")[1]);
        var EndPoint = this.S.GetSVGPoint();
        EndPoint.x = parseInt(endPoint.split(",")[0]);
        EndPoint.y = parseInt(endPoint.split(",")[1]);
        var f = StartPoint["GetSlope"](StartPoint, EndPoint);
        var l = f["GetSlope1"](f);
        this.PointList = EndPoint["GetPath"](EndPoint, l, 6);
        this.GraphicsPen = "";
        this.GraphicsName = "";
        this.GraphicsType = "";
        this.Guid = "";
        this.GraphicsCode = "";
    }
    Insulation.prototype.GraphicsRead = function (Insulation, TrackInsulation) {
        TrackInsulation.GraphicsPen = Insulation.attributes["图形画笔的颜色"].value;
        TrackInsulation.PenThickness = Insulation.attributes["图形画笔的宽度"].value;
        TrackInsulation.GraphicsName = Insulation.attributes["图形的名字"].value;
        TrackInsulation.Guid = Insulation.attributes["图形的唯一标识符"].value;
        TrackInsulation.GraphicsCode = Insulation.attributes["图形的编号"].value;
        TrackInsulation.InsulationType = Insulation.attributes["图形的样式"].value;
        TrackInsulation.UpDown = Insulation.attributes["图形的上下方位"].value;
        TrackInsulation.BeforeAfter = Insulation.attributes["图形的前后方位"].value;
        TrackInsulation.LeftRight = Insulation.attributes["图形的左右方位"].value;
        this.BeforeAfterBool(TrackInsulation);
        var Render = TrackInsulation.Render();
        return Render;
    };
    Insulation.prototype.BeforeAfterBool = function (i) {
        if (i.BeforeAfter.toString() == "轨道末端") {
            var f;
            //计算斜率，使绝缘节与轨道区段垂直
            if (i.TrackLine.PointList.length > 2) {
                var StartPoint = this.S.GetSVGPoint();
                StartPoint.x = parseInt(i.TrackLine.PointList[2].split(",")[0]);
                StartPoint.y = parseInt(i.TrackLine.PointList[2].split(",")[1]);
                var EndPoint = this.S.GetSVGPoint();
                EndPoint.x = parseInt(i.TrackLine.PointList[1].split(",")[0]);
                EndPoint.y = parseInt(i.TrackLine.PointList[1].split(",")[1]);
                f = StartPoint["GetSlope"](StartPoint, EndPoint);
            }
            else {
                var StartPoint = this.S.GetSVGPoint();
                StartPoint.x = parseInt(i.TrackLine.PointList[0].split(",")[0]);
                StartPoint.y = parseInt(i.TrackLine.PointList[0].split(",")[1]);
                var EndPoint = this.S.GetSVGPoint();
                EndPoint.x = parseInt(i.TrackLine.PointList[1].split(",")[0]);
                EndPoint.y = parseInt(i.TrackLine.PointList[1].split(",")[1]);
                f = StartPoint["GetSlope"](StartPoint, EndPoint);
            }
            var l = f.GetSlope1(f);
            var Point = this.S.GetSVGPoint();
            Point.x = parseInt(i.TrackLine.PointList[1].split(",")[0]);
            Point.y = parseInt(i.TrackLine.PointList[1].split(",")[1]);
            i.PointList = Point["GetPath"](Point, l, 6);
        }
        else {
            var f;
            //计算斜率，使绝缘节与轨道区段垂直
            if (i.TrackLine.PointList.length > 2) {
                var StartPoint = this.S.GetSVGPoint();
                StartPoint.x = parseInt(i.TrackLine.PointList[2].split(",")[0]);
                StartPoint.y = parseInt(i.TrackLine.PointList[2].split(",")[1]);
                var EndPoint = this.S.GetSVGPoint();
                EndPoint.x = parseInt(i.TrackLine.PointList[0].split(",")[0]);
                EndPoint.y = parseInt(i.TrackLine.PointList[0].split(",")[1]);
                f = StartPoint["GetSlope"](StartPoint, EndPoint);
            }
            else {
                var StartPoint = this.S.GetSVGPoint();
                StartPoint.x = parseInt(i.TrackLine.PointList[1].split(",")[0]);
                StartPoint.y = parseInt(i.TrackLine.PointList[1].split(",")[1]);
                var EndPoint = this.S.GetSVGPoint();
                EndPoint.x = parseInt(i.TrackLine.PointList[0].split(",")[0]);
                EndPoint.y = parseInt(i.TrackLine.PointList[0].split(",")[1]);
                f = StartPoint["GetSlope"](StartPoint, EndPoint);
            }
            var l = f.GetSlope1(f);
            var Point = this.S.GetSVGPoint();
            Point.x = parseInt(i.TrackLine.PointList[0].split(",")[0]);
            Point.y = parseInt(i.TrackLine.PointList[0].split(",")[1]);
            i.PointList = Point["GetPath"](Point, l, 6);
        }
    };
    Insulation.prototype.Render = function () {
        var SvgList = new Array();
        var pointList = new Array();
        if (this.InsulationType.toString() == "样式一") {
            var Polyline = this.S.GetSvgPolyline();
            Polyline["Graphics"] = this;
            Polyline.style.stroke = "white";
            Polyline.style.strokeWidth = this.PenThickness;
            var points = Polyline.points;
            this.CenterPoint = this.PointList[0]["CentralPoint"](this.PointList[0], this.PointList[1]);
            points.appendItem(this.PointList[0]);
            points.appendItem(this.PointList[1]);
            Polyline.points = points;
            SvgList.push(Polyline);
        }
        else if (this.InsulationType.toString() == "样式二") {
            var f = this.PointList[0]["GetSlope"](this.PointList[0], this.PointList[1]);
            var l = f["GetSlope1"](f);
            var Polyline = this.S.GetSvgPolyline();
            Polyline["Graphics"] = this;
            Polyline.style.stroke = "white";
            Polyline.style.strokeWidth = this.PenThickness;
            var points = Polyline.points;
            this.CenterPoint = this.PointList[0]["CentralPoint"](this.PointList[0], this.PointList[1]);
            points.appendItem(this.PointList[0]);
            points.appendItem(this.PointList[1]);
            Polyline.points = points;
            SvgList.push(Polyline);
            if (this.LeftRight.toString() == "右") {
                pointList = this.PointList[0]["GetPath"](this.PointList[0], l, 11);
                var line = this.S.GetSVGLine();
                line["Graphics"] = this;
                line.style.stroke = "white";
                line.style.strokeWidth = this.PenThickness;
                line.x1.baseVal.value = this.PointList[0].x;
                line.y1.baseVal.value = this.PointList[0].y;
                line.x2.baseVal.value = pointList[1].x;
                line.y2.baseVal.value = pointList[1].y;
                SvgList.push(line);
                pointList = this.PointList[1]["GetPath"](this.PointList[1], l, 11);
                var line = this.S.GetSVGLine();
                line["Graphics"] = this;
                line.style.stroke = "white";
                line.style.strokeWidth = this.PenThickness;
                line.x1.baseVal.value = this.PointList[1].x;
                line.y1.baseVal.value = this.PointList[1].y;
                line.x2.baseVal.value = pointList[1].x;
                line.y2.baseVal.value = pointList[1].y;
                SvgList.push(line);
            }
            else {
                pointList = this.PointList[0]["GetPath"](this.PointList[0], l, 11);
                var line = this.S.GetSVGLine();
                line["Graphics"] = this;
                line.style.stroke = "white";
                line.style.strokeWidth = this.PenThickness;
                line.x1.baseVal.value = this.PointList[0].x;
                line.y1.baseVal.value = this.PointList[0].y;
                line.x2.baseVal.value = pointList[0].x;
                line.y2.baseVal.value = pointList[0].y;
                SvgList.push(line);
                pointList = this.PointList[1]["GetPath"](this.PointList[1], l, 11);
                var line = this.S.GetSVGLine();
                line["Graphics"] = this;
                line.style.stroke = "white";
                line.style.strokeWidth = this.PenThickness;
                line.x1.baseVal.value = this.PointList[1].x;
                line.y1.baseVal.value = this.PointList[1].y;
                line.x2.baseVal.value = pointList[0].x;
                line.y2.baseVal.value = pointList[0].y;
                SvgList.push(line);
            }
        }
        else if (this.InsulationType.toString() == "样式三") {
            var Polyline = this.S.GetSvgPolyline();
            Polyline["Graphics"] = this;
            Polyline.style.stroke = "white";
            Polyline.style.strokeWidth = this.PenThickness;
            var points = Polyline.points;
            this.CenterPoint = this.PointList[0]["CentralPoint"](this.PointList[0], this.PointList[1]);
            points.appendItem(this.PointList[0]);
            points.appendItem(this.PointList[1]);
            Polyline.points = points;
            SvgList.push(Polyline);
            var Ellipse = this.S.GetSVGEllipsee();
            Ellipse["Graphics"] = this;
            Ellipse.style.stroke = "red";
            Ellipse.style.strokeWidth = this.PenThickness;
            Ellipse.style.fill = "rgba(100,100,0,0)";
            Ellipse.rx.baseVal.value = 6;
            Ellipse.ry.baseVal.value = 6;
            Ellipse.cx.baseVal.value = this.CenterPoint.x;
            Ellipse.cy.baseVal.value = this.CenterPoint.y;
            SvgList.push(Ellipse);
        }
        else if (this.InsulationType.toString() == "样式四") {
            var Polyline = this.S.GetSvgPolyline();
            Polyline["Graphics"] = this;
            Polyline.style.stroke = "white";
            Polyline.style.strokeWidth = this.PenThickness;
            var points = Polyline.points;
            this.CenterPoint = this.PointList[0]["CentralPoint"](this.PointList[0], this.PointList[1]);
            points.appendItem(this.PointList[0]);
            points.appendItem(this.PointList[1]);
            Polyline.points = points;
            SvgList.push(Polyline);
            var f = this.PointList[0]["GetSlope"](this.PointList[0], this.PointList[1]);
            var l = f["GetSlope1"](f);
            if (this.UpDown.toString() == "下") {
                pointList = this.PointList[0]["GetPath"](this.PointList[0], l, 11);
                if (this.LeftRight.toString() == "右") {
                    var line = this.S.GetSVGLine();
                    line["Graphics"] = this;
                    line.style.stroke = "white";
                    line.style.strokeWidth = this.PenThickness;
                    line.x1.baseVal.value = this.PointList[0].x;
                    line.y1.baseVal.value = this.PointList[0].y;
                    line.x2.baseVal.value = pointList[1].x;
                    line.y2.baseVal.value = pointList[1].y;
                    SvgList.push(line);
                }
                else if (this.LeftRight.toString() == "左") {
                    var line = this.S.GetSVGLine();
                    line["Graphics"] = this;
                    line.style.stroke = "white";
                    line.style.strokeWidth = this.PenThickness;
                    line.x1.baseVal.value = this.PointList[0].x;
                    line.y1.baseVal.value = this.PointList[0].y;
                    line.x2.baseVal.value = pointList[0].x;
                    line.y2.baseVal.value = pointList[0].y;
                    SvgList.push(line);
                }
            }
            else if (this.UpDown.toString() == "上") {
                pointList = this.PointList[1]["GetPath"](this.PointList[1], l, 11);
                if (this.LeftRight.toString() == "右") {
                    var line = this.S.GetSVGLine();
                    line["Graphics"] = this;
                    line.style.stroke = "white";
                    line.style.strokeWidth = this.PenThickness;
                    line.x1.baseVal.value = this.PointList[1].x;
                    line.y1.baseVal.value = this.PointList[1].y;
                    line.x2.baseVal.value = pointList[1].x;
                    line.y2.baseVal.value = pointList[1].y;
                    SvgList.push(line);
                }
                else if (this.LeftRight.toString() == "左") {
                    var line = this.S.GetSVGLine();
                    line["Graphics"] = this;
                    line.style.stroke = "white";
                    line.style.strokeWidth = this.PenThickness;
                    line.x1.baseVal.value = this.PointList[1].x;
                    line.y1.baseVal.value = this.PointList[1].y;
                    line.x2.baseVal.value = pointList[0].x;
                    line.y2.baseVal.value = pointList[0].y;
                    SvgList.push(line);
                }
            }
        }
        return SvgList;
    };
    return Insulation;
})();
var InsulationType;
(function (InsulationType) {
    InsulationType[InsulationType["样式一"] = 0] = "样式一";
    InsulationType[InsulationType["样式二"] = 1] = "样式二";
    InsulationType[InsulationType["样式三"] = 2] = "样式三";
    InsulationType[InsulationType["样式四"] = 3] = "样式四";
})(InsulationType || (InsulationType = {}));
var UpDown;
(function (UpDown) {
    UpDown[UpDown["上"] = 0] = "上";
    UpDown[UpDown["下"] = 1] = "下";
})(UpDown || (UpDown = {}));
var BeforeAfter;
(function (BeforeAfter) {
    BeforeAfter[BeforeAfter["轨道前端"] = 0] = "轨道前端";
    BeforeAfter[BeforeAfter["轨道末端"] = 1] = "轨道末端";
})(BeforeAfter || (BeforeAfter = {}));
var LeftRight;
(function (LeftRight) {
    LeftRight[LeftRight["左"] = 0] = "左";
    LeftRight[LeftRight["右"] = 1] = "右";
})(LeftRight || (LeftRight = {}));
//# sourceMappingURL=Insulation.js.map