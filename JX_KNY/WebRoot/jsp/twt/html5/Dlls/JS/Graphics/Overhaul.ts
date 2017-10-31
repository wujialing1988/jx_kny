
/// <reference path="../../../Scripts/jquery.d.ts" />
/// <reference path="../../../Dlls/JS/LinQ/LINQ.ts" />
class Overhaul
{
    ///画笔的颜色
    GraphicsPen: string;
    ///画笔的宽度
    PenThickness: string;
    ///图形绘制点集合
    PointList: Array<string>;
    ///图形的类型
    GraphicsType: string;
    ///图形的Guid
    Guid: string;
    ///图形的宽度
    GraphicsWidth: string;
    ///图形的高度
    GraphicsHeight: string;
    ///图形的矩阵变换
    GraphicsTransForm: string;
    S: Svg;
    constructor()
    {
        this.S = new Svg();
        this.GraphicsPen = "";
        this.GraphicsType = "";
        this.Guid = "";
        this.GraphicsTransForm = "";
        this.GraphicsWidth = "";
        this.GraphicsHeight = "";
    }
    GraphicsRead(Overhaul: Element)
    {
        this.GraphicsPen = "white";
        this.PenThickness = Overhaul.attributes["图形画笔的宽度"].value;
        this.GraphicsWidth = Overhaul.attributes["图形宽度"].value;
        this.GraphicsHeight = Overhaul.attributes["图形高度"].value;
        this.Guid = Overhaul.attributes["图形的唯一标识符"].value;
        this.GraphicsTransForm = "matrix(1,0,0,1," + Overhaul.attributes["OffsetX"].value + "," + Overhaul.attributes["OffsetY"].value+")";
        this.PointList = new Array<string>();
        this.PointList.push($(Overhaul).find("PointList").text());
    }
    Render(): SVGElement
    {
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
    }
}