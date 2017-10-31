/// <reference path="../../../Scripts/jquery.d.ts" />
/// <reference path="../../../Dlls/JS/LinQ/LINQ.ts" />
/// <reference path="../../../Dlls/JS/Pollution/Pollution.ts" />
var Light = (function () {
    function Light(StartPoint, EndPoint, TrackLine) {
        this.EllipseRadius = 2;
        this.S = new Svg();
        this.startPoint = this.S.GetSVGPoint();
        this.startPoint.x = parseInt(StartPoint.split(",")[0]);
        this.startPoint.y = parseInt(StartPoint.split(",")[1]);
        this.endPoint = this.S.GetSVGPoint();
        this.endPoint.x = parseInt(EndPoint.split(",")[0]);
        this.endPoint.y = parseInt(EndPoint.split(",")[1]);
        this.TrackLine = TrackLine;
        this.GraphicsPen = "";
        this.GraphicsName = "";
        this.GraphicsType = "";
        this.Guid = "";
        this.GraphicsCode = "";
    }
    Light.prototype.GraphicsRead = function (Light, TrackLight) {
        TrackLight.GraphicsPen = Light.attributes["图形画笔的颜色"].value;
        TrackLight.PenThickness = Light.attributes["图形画笔的宽度"].value;
        TrackLight.GraphicsName = Light.attributes["图形的名字"].value;
        TrackLight.Guid = Light.attributes["图形的唯一标识符"].value;
        TrackLight.GraphicsCode = Light.attributes["图形的编号"].value;
        TrackLight.LightColor = Light.attributes["图形的颜色"].value;
        TrackLight.UpDown = Light.attributes["图形上下方位"].value;
        TrackLight.HighLow = Light.attributes["图形高低方位"].value;
        TrackLight.LeftRight = Light.attributes["图形左右方位"].value;
        TrackLight.ShowTwoLights = Light.attributes["图形是否双型号灯"].value;
        var f = this.startPoint["GetSlope"](this.startPoint, this.endPoint);
        var l = f["GetSlope1"](f);
        var ltepm = l;
        this.PointList = this.endPoint["GetPath"](this.endPoint, l, 10);
        this.PointList = this.PointList.concat(this.endPoint["GetPath"](this.endPoint, l, 26));
        this.PointList = this.PointList.concat(this.endPoint["GetPath"](this.endPoint, l, 34));
        this.convertPointList = this.endPoint["GetPath"](this.endPoint, l, 18);
        f = this.convertPointList[1]["GetSlope"](this.convertPointList[1], this.convertPointList[0]);
        l = f["GetSlope1"](f);
        this.PointList = this.PointList.concat(this.convertPointList[0]["GetPath"](this.convertPointList[0], l, 12));
        f = this.convertPointList[0]["GetSlope"](this.convertPointList[0], this.convertPointList[1]);
        l = f["GetSlope1"](f);
        this.PointList = this.PointList.concat(this.convertPointList[1]["GetPath"](this.convertPointList[1], l, 12));
        this.convertPointList = this.endPoint["GetPath"](this.endPoint, ltepm, 26);
        f = this.convertPointList[1]["GetSlope"](this.convertPointList[1], this.convertPointList[0]);
        l = f["GetSlope1"](f);
        this.PointList = this.PointList.concat(this.convertPointList[0]["GetPath"](this.convertPointList[0], l, 12));
        f = this.convertPointList[0]["GetSlope"](this.convertPointList[0], this.convertPointList[1]);
        l = f["GetSlope1"](f);
        this.PointList = this.PointList.concat(this.convertPointList[1]["GetPath"](this.convertPointList[1], l, 12));
        var Render = TrackLight.Render(TrackLight);
        return Render;
    };
    Light.prototype.Render = function (Light) {
        var SvgList = new Array();
        var pointList = new Array();
        var Color = "";
        if (Light.LightColor.toString() == "白") {
            Color = "white";
        }
        else if (Light.LightColor.toString() == "蓝") {
            Color = "blue";
        }
        else {
            Color = "orange";
        }
        switch (Light.UpDown.toString()) {
            case "下":
                if (Light.LeftRight.toString() == "右") {
                    if (Light.HighLow.toString() == "矮") {
                        var Ellipse = Light.S.GetSVGEllipsee();
                        Ellipse["Graphics"] = Light;
                        Ellipse.style.stroke = Color;
                        Ellipse.style.strokeWidth = Light.PenThickness;
                        Ellipse.style.fill = Color;
                        Ellipse.rx.baseVal.value = 7;
                        Ellipse.ry.baseVal.value = 7;
                        Ellipse.cx.baseVal.value = Light.PointList[6].x;
                        Ellipse.cy.baseVal.value = Light.PointList[6].y;
                        SvgList.push(Ellipse);
                        var Ellipse1 = Light.S.GetSVGEllipsee();
                        Ellipse1["Graphics"] = Light;
                        Ellipse1.style.stroke = "white";
                        Ellipse1.style.strokeWidth = "2";
                        Ellipse1.style.fill = "rgba(100,100,0,0)";
                        Ellipse1.rx.baseVal.value = 8;
                        Ellipse1.ry.baseVal.value = 8;
                        Ellipse1.cx.baseVal.value = Light.PointList[6].x;
                        Ellipse1.cy.baseVal.value = Light.PointList[6].y;
                        SvgList.push(Ellipse1);
                        var line = Light.S.GetSVGLine();
                        line["Graphics"] = Light;
                        line.style.stroke = "white";
                        line.style.strokeWidth = Light.PenThickness;
                        line.x1.baseVal.value = Light.PointList[0].x;
                        line.y1.baseVal.value = Light.PointList[0].y;
                        line.x2.baseVal.value = Light.PointList[2].x;
                        line.y2.baseVal.value = Light.PointList[2].y;
                        SvgList.push(line);
                        var Text = Light.S.GetSvgText();
                        Text["Graphics"] = Light;
                        Text.textContent = Light.GraphicsName;
                        Text.style.fontSize = "12px";
                        Text.style.fill = "rgba(80,255,255,255)";
                        var Origin = Light.PointList[0].y <= Light.PointList[2].y ? Light.PointList[0] : Light.PointList[2];
                        var W = (12 * Light.GraphicsName.length) / 2;
                        if (((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) >= 90) {
                            Text.setAttribute("transform", "rotate(" + (((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) - 180) + " " + Origin.x + "," + Origin.y + ")");
                        }
                        else {
                            Text.setAttribute("transform", "rotate(" + ((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) + " " + Origin.x + "," + Origin.y + ")");
                        }
                        Text.setAttribute("x", (Origin.x + 5).toString());
                        Text.setAttribute("y", (Origin.y + 12).toString());
                        SvgList.push(Text);
                    }
                    else {
                        var Ellipse = Light.S.GetSVGEllipsee();
                        Ellipse["Graphics"] = Light;
                        Ellipse.style.stroke = Color;
                        Ellipse.style.strokeWidth = Light.PenThickness;
                        Ellipse.style.fill = Color;
                        Ellipse.rx.baseVal.value = 7;
                        Ellipse.ry.baseVal.value = 7;
                        Ellipse.cx.baseVal.value = Light.PointList[10].x;
                        Ellipse.cy.baseVal.value = Light.PointList[10].y;
                        SvgList.push(Ellipse);
                        var Ellipse1 = Light.S.GetSVGEllipsee();
                        Ellipse1["Graphics"] = Light;
                        Ellipse1.style.stroke = "white";
                        Ellipse1.style.strokeWidth = "2";
                        Ellipse1.style.fill = "rgba(100,100,0,0)";
                        Ellipse1.rx.baseVal.value = 8;
                        Ellipse1.ry.baseVal.value = 8;
                        Ellipse1.cx.baseVal.value = Light.PointList[10].x;
                        Ellipse1.cy.baseVal.value = Light.PointList[10].y;
                        SvgList.push(Ellipse1);
                        var line = Light.S.GetSVGLine();
                        line["Graphics"] = Light;
                        line.style.stroke = "white";
                        line.style.strokeWidth = Light.PenThickness;
                        line.x1.baseVal.value = Light.PointList[0].x;
                        line.y1.baseVal.value = Light.PointList[0].y;
                        line.x2.baseVal.value = Light.PointList[4].x;
                        line.y2.baseVal.value = Light.PointList[4].y;
                        SvgList.push(line);
                        var Text = Light.S.GetSvgText();
                        Text["Graphics"] = Light;
                        Text.textContent = Light.GraphicsName;
                        Text.style.fontSize = "12px";
                        Text.style.fill = "rgba(80,255,255,255)";
                        var Origin = Light.PointList[0].y <= Light.PointList[4].y ? Light.PointList[0] : Light.PointList[4];
                        var W = (12 * Light.GraphicsName.length) / 2;
                        if (((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) >= 90) {
                            Text.setAttribute("transform", "rotate(" + (((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) - 180) + " " + Origin.x + "," + Origin.y + ")");
                        }
                        else {
                            Text.setAttribute("transform", "rotate(" + ((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) + " " + Origin.x + "," + Origin.y + ")");
                        }
                        Text.setAttribute("x", (Origin.x + 5).toString());
                        Text.setAttribute("y", (Origin.y + 12).toString());
                        SvgList.push(Text);
                    }
                }
                else {
                    if (Light.HighLow.toString() == "矮") {
                        var Ellipse = Light.S.GetSVGEllipsee();
                        Ellipse["Graphics"] = Light;
                        Ellipse.style.stroke = Color;
                        Ellipse.style.strokeWidth = Light.PenThickness;
                        Ellipse.style.fill = Color;
                        Ellipse.rx.baseVal.value = 7;
                        Ellipse.ry.baseVal.value = 7;
                        Ellipse.cx.baseVal.value = Light.PointList[7].x;
                        Ellipse.cy.baseVal.value = Light.PointList[7].y;
                        SvgList.push(Ellipse);
                        var Ellipse1 = Light.S.GetSVGEllipsee();
                        Ellipse1["Graphics"] = Light;
                        Ellipse1.style.stroke = "white";
                        Ellipse1.style.strokeWidth = "2";
                        Ellipse1.style.fill = "rgba(100,100,0,0)";
                        Ellipse1.rx.baseVal.value = 8;
                        Ellipse1.ry.baseVal.value = 8;
                        Ellipse1.cx.baseVal.value = Light.PointList[7].x;
                        Ellipse1.cy.baseVal.value = Light.PointList[7].y;
                        SvgList.push(Ellipse1);
                        var line = Light.S.GetSVGLine();
                        line["Graphics"] = Light;
                        line.style.stroke = "white";
                        line.style.strokeWidth = Light.PenThickness;
                        line.x1.baseVal.value = Light.PointList[0].x;
                        line.y1.baseVal.value = Light.PointList[0].y;
                        line.x2.baseVal.value = Light.PointList[2].x;
                        line.y2.baseVal.value = Light.PointList[2].y;
                        SvgList.push(line);
                        var Text = Light.S.GetSvgText();
                        Text["Graphics"] = Light;
                        Text.textContent = Light.GraphicsName;
                        Text.style.fontSize = "12px";
                        Text.style.fill = "rgba(80,255,255,255)";
                        var Origin = Light.PointList[0].y <= Light.PointList[2].y ? Light.PointList[0] : Light.PointList[2];
                        var W = (12 * Light.GraphicsName.length) / 2;
                        if (((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) >= 90) {
                            Text.setAttribute("transform", "rotate(" + (((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) - 180) + " " + Origin.x + "," + Origin.y + ")");
                        }
                        else {
                            Text.setAttribute("transform", "rotate(" + ((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) + " " + Origin.x + "," + Origin.y + ")");
                        }
                        Text.setAttribute("x", (Origin.x - W - 9).toString());
                        Text.setAttribute("y", (Origin.y + 12).toString());
                        SvgList.push(Text);
                    }
                    else {
                        var Ellipse = Light.S.GetSVGEllipsee();
                        Ellipse["Graphics"] = Light;
                        Ellipse.style.stroke = Color;
                        Ellipse.style.strokeWidth = Light.PenThickness;
                        Ellipse.style.fill = Color;
                        Ellipse.rx.baseVal.value = 7;
                        Ellipse.ry.baseVal.value = 7;
                        Ellipse.cx.baseVal.value = Light.PointList[11].x;
                        Ellipse.cy.baseVal.value = Light.PointList[11].y;
                        SvgList.push(Ellipse);
                        var Ellipse1 = Light.S.GetSVGEllipsee();
                        Ellipse1["Graphics"] = Light;
                        Ellipse1.style.stroke = "white";
                        Ellipse1.style.strokeWidth = "2";
                        Ellipse1.style.fill = "rgba(100,100,0,0)";
                        Ellipse1.rx.baseVal.value = 8;
                        Ellipse1.ry.baseVal.value = 8;
                        Ellipse1.cx.baseVal.value = Light.PointList[11].x;
                        Ellipse1.cy.baseVal.value = Light.PointList[11].y;
                        SvgList.push(Ellipse1);
                        var line = Light.S.GetSVGLine();
                        line["Graphics"] = Light;
                        line.style.stroke = "white";
                        line.style.strokeWidth = Light.PenThickness;
                        line.x1.baseVal.value = Light.PointList[0].x;
                        line.y1.baseVal.value = Light.PointList[0].y;
                        line.x2.baseVal.value = Light.PointList[4].x;
                        line.y2.baseVal.value = Light.PointList[4].y;
                        SvgList.push(line);
                        var Text = Light.S.GetSvgText();
                        Text["Graphics"] = Light;
                        Text.textContent = Light.GraphicsName;
                        Text.style.fontSize = "12px";
                        Text.style.fill = "rgba(80,255,255,255)";
                        var Origin = Light.PointList[0].y <= Light.PointList[4].y ? Light.PointList[0] : Light.PointList[4];
                        var W = (12 * Light.GraphicsName.length) / 2;
                        if (((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) >= 90) {
                            Text.setAttribute("transform", "rotate(" + (((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) - 180) + " " + Origin.x + "," + Origin.y + ")");
                        }
                        else {
                            Text.setAttribute("transform", "rotate(" + ((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) + " " + Origin.x + "," + Origin.y + ")");
                        }
                        Text.setAttribute("x", (Origin.x + 5).toString());
                        Text.setAttribute("y", (Origin.y + 12).toString());
                        SvgList.push(Text);
                    }
                }
                break;
            case "上":
                if (Light.LeftRight.toString() == "右") {
                    if (Light.HighLow.toString() == "矮") {
                        var Ellipse = Light.S.GetSVGEllipsee();
                        Ellipse["Graphics"] = Light;
                        Ellipse.style.stroke = Color;
                        Ellipse.style.strokeWidth = Light.PenThickness;
                        Ellipse.style.fill = Color;
                        Ellipse.rx.baseVal.value = 7;
                        Ellipse.ry.baseVal.value = 7;
                        Ellipse.cx.baseVal.value = Light.PointList[8].x;
                        Ellipse.cy.baseVal.value = Light.PointList[8].y;
                        SvgList.push(Ellipse);
                        var Ellipse1 = Light.S.GetSVGEllipsee();
                        Ellipse1["Graphics"] = Light;
                        Ellipse1.style.stroke = "white";
                        Ellipse1.style.strokeWidth = "2";
                        Ellipse1.style.fill = "rgba(100,100,0,0)";
                        Ellipse1.rx.baseVal.value = 8;
                        Ellipse1.ry.baseVal.value = 8;
                        Ellipse1.cx.baseVal.value = Light.PointList[8].x;
                        Ellipse1.cy.baseVal.value = Light.PointList[8].y;
                        SvgList.push(Ellipse1);
                        var line = Light.S.GetSVGLine();
                        line["Graphics"] = Light;
                        line.style.stroke = "white";
                        line.style.strokeWidth = Light.PenThickness;
                        line.x1.baseVal.value = Light.PointList[1].x;
                        line.y1.baseVal.value = Light.PointList[1].y;
                        line.x2.baseVal.value = Light.PointList[3].x;
                        line.y2.baseVal.value = Light.PointList[3].y;
                        SvgList.push(line);
                        var Text = Light.S.GetSvgText();
                        Text["Graphics"] = Light;
                        Text.textContent = Light.GraphicsName;
                        Text.style.fontSize = "12px";
                        Text.style.fill = "rgba(80,255,255,255)";
                        var Origin = Light.PointList[1].y <= Light.PointList[3].y ? Light.PointList[1] : Light.PointList[3];
                        var W = (12 * Light.GraphicsName.length) / 2;
                        if (((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) >= 90) {
                            Text.setAttribute("transform", "rotate(" + (((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) - 180) + " " + Origin.x + "," + Origin.y + ")");
                        }
                        else {
                            Text.setAttribute("transform", "rotate(" + ((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) + " " + Origin.x + "," + Origin.y + ")");
                        }
                        Text.setAttribute("x", (Origin.x + 5).toString());
                        Text.setAttribute("y", (Origin.y + 12).toString());
                        SvgList.push(Text);
                    }
                    else {
                        var Ellipse = Light.S.GetSVGEllipsee();
                        Ellipse["Graphics"] = Light;
                        Ellipse.style.stroke = Color;
                        Ellipse.style.strokeWidth = Light.PenThickness;
                        Ellipse.style.fill = Color;
                        Ellipse.rx.baseVal.value = 7;
                        Ellipse.ry.baseVal.value = 7;
                        Ellipse.cx.baseVal.value = Light.PointList[12].x;
                        Ellipse.cy.baseVal.value = Light.PointList[12].y;
                        SvgList.push(Ellipse);
                        var Ellipse1 = Light.S.GetSVGEllipsee();
                        Ellipse1["Graphics"] = Light;
                        Ellipse1.style.stroke = "white";
                        Ellipse1.style.strokeWidth = "2";
                        Ellipse1.style.fill = "rgba(100,100,0,0)";
                        Ellipse1.rx.baseVal.value = 8;
                        Ellipse1.ry.baseVal.value = 8;
                        Ellipse1.cx.baseVal.value = Light.PointList[12].x;
                        Ellipse1.cy.baseVal.value = Light.PointList[12].y;
                        SvgList.push(Ellipse1);
                        var line = Light.S.GetSVGLine();
                        line["Graphics"] = Light;
                        line.style.stroke = "white";
                        line.style.strokeWidth = Light.PenThickness;
                        line.x1.baseVal.value = Light.PointList[1].x;
                        line.y1.baseVal.value = Light.PointList[1].y;
                        line.x2.baseVal.value = Light.PointList[5].x;
                        line.y2.baseVal.value = Light.PointList[5].y;
                        SvgList.push(line);
                        var Text = Light.S.GetSvgText();
                        Text["Graphics"] = Light;
                        Text.textContent = Light.GraphicsName;
                        Text.style.fontSize = "12px";
                        Text.style.fill = "rgba(80,255,255,255)";
                        var Origin = Light.PointList[1].y <= Light.PointList[5].y ? Light.PointList[1] : Light.PointList[5];
                        var W = (12 * Light.GraphicsName.length) / 2;
                        if (((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) >= 90) {
                            Text.setAttribute("transform", "rotate(" + (((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) - 180) + " " + Origin.x + "," + Origin.y + ")");
                        }
                        else {
                            Text.setAttribute("transform", "rotate(" + ((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) + " " + Origin.x + "," + Origin.y + ")");
                        }
                        Text.setAttribute("x", (Origin.x + 5).toString());
                        Text.setAttribute("y", (Origin.y + 12).toString());
                        SvgList.push(Text);
                    }
                }
                else {
                    if (Light.HighLow.toString() == "矮") {
                        var Ellipse = Light.S.GetSVGEllipsee();
                        Ellipse["Graphics"] = Light;
                        Ellipse.style.stroke = Color;
                        Ellipse.style.strokeWidth = Light.PenThickness;
                        Ellipse.style.fill = Color;
                        Ellipse.rx.baseVal.value = 7;
                        Ellipse.ry.baseVal.value = 7;
                        Ellipse.cx.baseVal.value = Light.PointList[9].x;
                        Ellipse.cy.baseVal.value = Light.PointList[9].y;
                        SvgList.push(Ellipse);
                        var Ellipse1 = Light.S.GetSVGEllipsee();
                        Ellipse1["Graphics"] = Light;
                        Ellipse1.style.stroke = "white";
                        Ellipse1.style.strokeWidth = "2";
                        Ellipse1.style.fill = "rgba(100,100,0,0)";
                        Ellipse1.rx.baseVal.value = 8;
                        Ellipse1.ry.baseVal.value = 8;
                        Ellipse1.cx.baseVal.value = Light.PointList[9].x;
                        Ellipse1.cy.baseVal.value = Light.PointList[9].y;
                        SvgList.push(Ellipse1);
                        var line = Light.S.GetSVGLine();
                        line["Graphics"] = Light;
                        line.style.stroke = "white";
                        line.style.strokeWidth = Light.PenThickness;
                        line.x1.baseVal.value = Light.PointList[1].x;
                        line.y1.baseVal.value = Light.PointList[1].y;
                        line.x2.baseVal.value = Light.PointList[3].x;
                        line.y2.baseVal.value = Light.PointList[3].y;
                        SvgList.push(line);
                        var Text = Light.S.GetSvgText();
                        Text["Graphics"] = Light;
                        Text.textContent = Light.GraphicsName;
                        Text.style.fontSize = "12px";
                        Text.style.fill = "rgba(80,255,255,255)";
                        var Origin = Light.PointList[1].y <= Light.PointList[3].y ? Light.PointList[1] : Light.PointList[3];
                        var W = (12 * Light.GraphicsName.length) / 2;
                        if (((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) >= 90) {
                            Text.setAttribute("transform", "rotate(" + (((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) - 180) + " " + Origin.x + "," + Origin.y + ")");
                        }
                        else {
                            Text.setAttribute("transform", "rotate(" + ((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) + " " + Origin.x + "," + Origin.y + ")");
                        }
                        Text.setAttribute("x", (Origin.x - W - 10).toString());
                        Text.setAttribute("y", (Origin.y + 12).toString());
                        SvgList.push(Text);
                    }
                    else {
                        var Ellipse = Light.S.GetSVGEllipsee();
                        Ellipse["Graphics"] = Light;
                        Ellipse.style.stroke = Color;
                        Ellipse.style.strokeWidth = Light.PenThickness;
                        Ellipse.style.fill = Color;
                        Ellipse.rx.baseVal.value = 7;
                        Ellipse.ry.baseVal.value = 7;
                        Ellipse.cx.baseVal.value = Light.PointList[13].x;
                        Ellipse.cy.baseVal.value = Light.PointList[13].y;
                        SvgList.push(Ellipse);
                        var Ellipse1 = Light.S.GetSVGEllipsee();
                        Ellipse1["Graphics"] = Light;
                        Ellipse1.style.stroke = "white";
                        Ellipse1.style.strokeWidth = "2";
                        Ellipse1.style.fill = "rgba(100,100,0,0)";
                        Ellipse1.rx.baseVal.value = 8;
                        Ellipse1.ry.baseVal.value = 8;
                        Ellipse1.cx.baseVal.value = Light.PointList[13].x;
                        Ellipse1.cy.baseVal.value = Light.PointList[13].y;
                        SvgList.push(Ellipse1);
                        var line = Light.S.GetSVGLine();
                        line["Graphics"] = Light;
                        line.style.stroke = "white";
                        line.style.strokeWidth = Light.PenThickness;
                        line.x1.baseVal.value = Light.PointList[1].x;
                        line.y1.baseVal.value = Light.PointList[1].y;
                        line.x2.baseVal.value = Light.PointList[5].x;
                        line.y2.baseVal.value = Light.PointList[5].y;
                        SvgList.push(line);
                        var Text = Light.S.GetSvgText();
                        Text["Graphics"] = Light;
                        Text.textContent = Light.GraphicsName;
                        Text.style.fontSize = "12px";
                        Text.style.fill = "rgba(80,255,255,255)";
                        var Origin = Light.PointList[1].y <= Light.PointList[5].y ? Light.PointList[1] : Light.PointList[5];
                        var W = (12 * Light.GraphicsName.length) / 2;
                        if (((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) >= 90) {
                            Text.setAttribute("transform", "rotate(" + (((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) - 180) + " " + Origin.x + "," + Origin.y + ")");
                        }
                        else {
                            Text.setAttribute("transform", "rotate(" + ((Math.atan2(Light.endPoint.y - Light.startPoint.y, Light.endPoint.x - Light.startPoint.x) * 180) / Math.PI) + " " + Origin.x + "," + Origin.y + ")");
                        }
                        Text.setAttribute("x", (Origin.x - W - 10).toString());
                        Text.setAttribute("y", (Origin.y + 12).toString());
                        SvgList.push(Text);
                    }
                }
                break;
        }
        return SvgList;
    };
    return Light;
})();
var HighLow;
(function (HighLow) {
    HighLow[HighLow["高"] = 0] = "高";
    HighLow[HighLow["矮"] = 1] = "矮";
})(HighLow || (HighLow = {}));
var LightColor;
(function (LightColor) {
    LightColor[LightColor["白"] = 0] = "白";
    LightColor[LightColor["蓝"] = 1] = "蓝";
})(LightColor || (LightColor = {}));
//# sourceMappingURL=Light.js.map