/// <reference path="../../../Scripts/jquery.d.ts" />
/// <reference path="../../../Dlls/JS/LinQ/LINQ.ts" />
var Overhaul = (function () {
    function Overhaul() {
        this.S = new Svg();
        this.GraphicsPen = "";
        this.GraphicsType = "";
        this.Guid = "";
        this.GraphicsTransForm = "";
        this.GraphicsWidth = "";
        this.GraphicsHeight = "";
    }
    Overhaul.prototype.GraphicsRead = function (Overhaul) {
        this.GraphicsPen = "white";
        this.PenThickness = Overhaul.attributes["图形画笔的宽度"].value;
        this.GraphicsWidth = Overhaul.attributes["图形宽度"].value;
        this.GraphicsHeight = Overhaul.attributes["图形高度"].value;
        this.Guid = Overhaul.attributes["图形的唯一标识符"].value;
        this.GraphicsTransForm = "matrix(1,0,0,1," + Overhaul.attributes["OffsetX"].value + "," + Overhaul.attributes["OffsetY"].value + ")";
        this.PointList = new Array();
        this.PointList.push($(Overhaul).find("PointList").text());
    };
    Overhaul.prototype.Render = function () {
        var Rect = this.S.GetSVGRect();
        Rect["Graphics"] = this;
        Rect.style.fill = "rgba(100,100,0,0.4)";
        Rect.style.stroke = "rgba(100,100,100,0.4)";
        Rect.style.strokeDasharray = "3 3";
        Rect.style.strokeWidth = this.PenThickness;
        Rect.x.baseVal.value = parseInt(this.PointList[0].split(",")[0]) - parseInt(this.GraphicsWidth) / 2;
        Rect.y.baseVal.value = parseInt(this.PointList[0].split(",")[1]) + 3;
        Rect.width.baseVal.value = parseInt(this.GraphicsWidth);
        Rect.height.baseVal.value = parseInt(this.GraphicsHeight);
        Rect.setAttribute("transform", this.GraphicsTransForm);
        return Rect;
    };
    return Overhaul;
})();
//# sourceMappingURL=Overhaul.js.map