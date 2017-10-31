/// <reference path="../../../Scripts/jquery.d.ts" />
/// <reference path="../../../Dlls/JS/LinQ/LINQ.ts" />
var PositionClass = (function () {
    function PositionClass() {
        this.S = new Svg();
        this.GraphicsPen = "";
        this.GraphicsName = "";
        this.GraphicsType = "";
        this.Guid = "";
        this.GraphicsCode = "";
    }
    PositionClass.prototype.GraphicsRead = function (Position) {
        this.GraphicsPen = Position.attributes["图形画笔的颜色"].value;
        this.PenThickness = Position.attributes["图形画笔的宽度"].value;
        this.GraphicsName = Position.attributes["图形的名字"].value;
        this.Guid = Position.attributes["图形的唯一标识符"].value;
        this.GraphicsCode = Position.attributes["图形的编号"].value;
        this.RectWidth = Position.attributes["图形的宽度"].value;
        this.RectHeight = Position.attributes["图形的高度"].value;
        this.Move = Position.attributes["移动坐标"].value;
        this.PointList = new Array();
        var ddd = $(Position).find("PointList").get(0);
        this.PointList.push($($(Position).find("PointList")[0]).text());
        this.Matrix = this.S.GetSVGMatrix();
        this.Matrix.a = Position.attributes["M11"].value;
        this.Matrix.b = Position.attributes["M21"].value;
        this.Matrix.c = Position.attributes["M12"].value;
        this.Matrix.d = Position.attributes["M22"].value;
        this.Matrix.e = Position.attributes["OffsetX"].value;
        this.Matrix.f = Position.attributes["OffsetY"].value;
    };
    PositionClass.prototype.Render = function () {
        var SvgList = new Array();
        var Rect = this.S.GetSVGRect();
        Rect["Graphics"] = this;
        Rect.x.baseVal.value = parseInt(this.PointList[0].split(",")[0]) - parseInt(this.RectWidth) / 2;
        Rect.y.baseVal.value = parseInt(this.PointList[0].split(",")[1]) + 3;
        Rect.width.baseVal.value = parseInt(this.RectWidth);
        Rect.height.baseVal.value = parseInt(this.RectHeight) / 2;
        var PointList = new Array();
        var PointA = this.S.GetSVGPoint();
        PointA.x = parseInt(this.PointList[0].split(",")[0]) - parseInt(this.RectWidth) / 2 + 3;
        PointA.y = parseInt(this.PointList[0].split(",")[1]) + 12 + 3;
        var PointB = this.S.GetSVGPoint();
        PointB.x = parseInt(this.PointList[0].split(",")[0]) - parseInt(this.RectWidth) / 2 + 3;
        PointB.y = parseInt(this.PointList[0].split(",")[1]) + 12 + 3;
        PointList.push(PointA.matrixTransform(this.Matrix));
        PointList.push(PointB.matrixTransform(this.Matrix));
        Rect.transform.baseVal.appendItem(Rect.transform.baseVal.createSVGTransformFromMatrix(this.Matrix));
        Rect.style.fill = "rgba(0,0,0,0.0)";
        Rect.style.stroke = "white";
        Rect.style.strokeWidth = this.PenThickness;
        SvgList.push(Rect);
        var Text = this.S.GetSvgText();
        Text["Graphics"] = this;
        Text.textContent = this.GraphicsName;
        Text.style.fontSize = "12px";
        Text.style.fill = "rgba(80,255,255,255)";
        Text.setAttribute("transform", "rotate(" + ((Math.atan2(PointList[1].y - PointList[0].y, PointList[1].x - PointList[0].x) * 180) / Math.PI) + ")");
        Text.setAttribute("x", PointList[0].x.toString());
        Text.setAttribute("y", PointList[0].y.toString());
        SvgList.push(Text);
        return SvgList;
    };
    return PositionClass;
})();
//# sourceMappingURL=Position.js.map