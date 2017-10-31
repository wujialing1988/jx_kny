/// <reference path="../../../Scripts/jquery.d.ts" />
/// <reference path="../../../Dlls/JS/LinQ/LINQ.ts" />
/// <reference path="../../../Dlls/JS/Pollution/Pollution.ts" />
var TrackLine = (function () {
    function TrackLine() {
        this.S = new Svg();
        this.LightList = new Array();
        this.TrainList = new Array();
        this.GraphicsPen = "";
        this.GraphicsName = "";
        this.GraphicsType = "";
        this.Guid = "";
        this.GraphicsCode = "";
    }
    TrackLine.prototype.GraphicsRead = function (Track) {
        this.GraphicsPen = Track.attributes["图形画笔的颜色"].value;
        this.PenThickness = Track.attributes["图形画笔的宽度"].value;
        this.GraphicsName = Track.attributes["图形的名字"].value;
        this.Guid = Track.attributes["图形的唯一标识符"].value;
        this.GraphicsCode = Track.attributes["图形的编号"].value;
        this.TextPoint = Track.attributes["字体的坐标"].value;
        this.PointList = new Array();
        if (Track.attributes["图形是否有折点"].value == "true") {
            this.PointList.push($($(Track).find("PointList")[0]).text());
            this.PointList.push($($(Track).find("PointList")[1]).text());
            this.PointList.push($($(Track).find("PointList")[2]).text());
        }
        else {
            var ddd = $(Track).find("PointList").get(0);
            this.PointList.push($($(Track).find("PointList")[0]).text());
            this.PointList.push($($(Track).find("PointList")[1]).text());
        }
    };
    TrackLine.prototype.Render = function () {
        var SvgList = new Array();
        if (this.PointList.length == 3) {
            var Polyline = this.S.GetSvgPolyline();
            Polyline["Graphics"] = this;
            Polyline.style.stroke = "white";
            Polyline.style.strokeWidth = this.PenThickness;
            if (this.GraphicsCode == "250") {
                Polyline.style.strokeDasharray = "5";
            }
            var points = Polyline.points;
            var point = this.S.GetSVGPoint();
            point.x = parseInt(this.PointList[0].split(",")[0]);
            point.y = parseInt(this.PointList[0].split(",")[1]);
            points.appendItem(point);
            var point2 = this.S.GetSVGPoint();
            point2.x = parseInt(this.PointList[2].split(",")[0]);
            point2.y = parseInt(this.PointList[2].split(",")[1]);
            points.appendItem(point2);
            var point1 = this.S.GetSVGPoint();
            point1.x = parseInt(this.PointList[1].split(",")[0]);
            point1.y = parseInt(this.PointList[1].split(",")[1]);
            points.appendItem(point1);
            Polyline.points = points;
            SvgList.push(Polyline);
            var Text = this.S.GetSvgText();
            Text["Graphics"] = this;
            Text.textContent = this.GraphicsName;
            Text.style.fontSize = "12px";
            Text.style.fill = "rgba(80,255,255,255)";
            var W = (12 * this.GraphicsName.length) / 2;
            var f = point2["GetDistance"](point2, point1) / 2 - W;
            var Point = point2["GetPath"](point2, point2["GetSlope"](point2, point1), f)[1];
            if (((Math.atan2(point2.y - point1.y, point2.x - point1.x) * 180) / Math.PI) >= 90) {
                Text.setAttribute("transform", "rotate(" + (((Math.atan2(point2.y - point1.y, point2.x - point1.x) * 180) / Math.PI) - 180) + " " + this.TextPoint.split(",")[0] + "," + this.TextPoint.split(",")[1] + ")");
            }
            else if (((Math.atan2(point2.y - point1.y, point2.x - point1.x) * 180) / Math.PI) <= 90) {
                Text.setAttribute("transform", "rotate(" + (((Math.atan2(point2.y - point1.y, point2.x - point1.x) * 180) / Math.PI) + 180) + " " + this.TextPoint.split(",")[0] + "," + this.TextPoint.split(",")[1] + ")");
            }
            else {
                Text.setAttribute("transform", "rotate(" + ((Math.atan2(point2.y - point1.y, point2.x - point1.x) * 180) / Math.PI) + " " + this.TextPoint.split(",")[0] + "," + this.TextPoint.split(",")[1] + ")");
            }
            Text.setAttribute("x", this.TextPoint.split(",")[0]);
            Text.setAttribute("y", (parseInt(this.TextPoint.split(",")[1]) + 12).toString());
            SvgList.push(Text);
            return SvgList;
        }
        else {
            var line = this.S.GetSVGLine();
            line["Graphics"] = this;
            line.style.fill = "white";
            line.style.stroke = "white";
            line.style.strokeWidth = this.PenThickness;
            if (this.GraphicsCode == "250") {
                line.style.strokeDasharray = "5";
            }
            var point = this.S.GetSVGPoint();
            point.x = parseInt(this.PointList[0].split(",")[0]);
            point.y = parseInt(this.PointList[0].split(",")[1]);
            var point1 = this.S.GetSVGPoint();
            point1.x = parseInt(this.PointList[1].split(",")[0]);
            point1.y = parseInt(this.PointList[1].split(",")[1]);
            line.x1.baseVal.value = parseInt(this.PointList[0].split(",")[0]);
            line.y1.baseVal.value = parseInt(this.PointList[0].split(",")[1]);
            line.x2.baseVal.value = parseInt(this.PointList[1].split(",")[0]);
            line.y2.baseVal.value = parseInt(this.PointList[1].split(",")[1]);
            SvgList.push(line);
            var Text = this.S.GetSvgText();
            Text["Graphics"] = this;
            Text.textContent = this.GraphicsName;
            Text.style.fontSize = "12px";
            Text.style.fill = "rgba(80,255,255,255)";
            var W = (12 * this.GraphicsName.length) / 2;
            var f = point["GetDistance"](point, point1) / 2 - W;
            var Point = point["GetPath"](point, point["GetSlope"](point, point1), f)[1];
            if (((Math.atan2(point.y - point1.y, point.x - point1.x) * 180) / Math.PI) >= 90) {
                Text.setAttribute("transform", "rotate(" + (((Math.atan2(point.y - point1.y, point.x - point1.x) * 180) / Math.PI) - 180) + " " + this.TextPoint.split(",")[0] + "," + this.TextPoint.split(",")[1] + ")");
            }
            else if (((Math.atan2(point.y - point1.y, point.x - point1.x) * 180) / Math.PI) <= 90) {
                Text.setAttribute("transform", "rotate(" + (((Math.atan2(point.y - point1.y, point.x - point1.x) * 180) / Math.PI) + 180) + " " + this.TextPoint.split(",")[0] + "," + this.TextPoint.split(",")[1] + ")");
            }
            else {
                Text.setAttribute("transform", "rotate(" + ((Math.atan2(point.y - point1.y, point.x - point1.x) * 180) / Math.PI) + " " + this.TextPoint.split(",")[0] + "," + this.TextPoint.split(",")[1] + ")");
            }
            Text.setAttribute("x", this.TextPoint.split(",")[0]);
            Text.setAttribute("y", (parseInt(this.TextPoint.split(",")[1]) + 12).toString());
            SvgList.push(Text);
            return SvgList;
        }
    };
    return TrackLine;
})();
//# sourceMappingURL=TrackLine.js.map