
/// <reference path="../../../Scripts/jquery.d.ts" />
/// <reference path="../../../Dlls/JS/LinQ/LINQ.ts" />

class TextFrame
{
    ///画笔的颜色
    GraphicsPen: string;
    ///图形的类型
    GraphicsType: string;
    ///图形的Guid
    Guid: string;
    ///图形的宽度
    GraphicsText: string;
    ///图形的高度
    GraphicsPoint: string;
    ///图形的样式
    GraphicsStyle: string;
    ///字体的大小
    GraphicsSize: string;
    ///图形的编号
    GraphicsCode: string;
    S: Svg;
    constructor()
    {
        this.S = new Svg();
        this.GraphicsPen = "";
        this.GraphicsType = "";
        this.Guid = "";
        this.GraphicsText = "";
        this.GraphicsPoint = "";
        this.GraphicsStyle = "";
        this.GraphicsSize = "";
        this.GraphicsCode = "";
    }
    GraphicsRead(TextFrame: Element)
    {
        this.GraphicsPen = "white";
        this.GraphicsCode = TextFrame.attributes["图形的编号"].value;
        this.GraphicsText = TextFrame.attributes["图形的文本"].value;
        this.Guid = TextFrame.attributes["图形的唯一标识符"].value;
        this.GraphicsStyle = TextFrame.attributes["图形样式"].value;
        this.GraphicsPoint = TextFrame.attributes["图形的坐标"].value;
        this.GraphicsSize = TextFrame.attributes["图形大小"].value;
    }
    Render(): SVGElement
    {
        var Text = this.S.GetSvgText();
        Text["Graphics"] = this;
        if (this.GraphicsCode != "1")
        {
            Text.style.fill = "white";
        }
        else
        {
            Text.style.fill = "rgba(100,100,0,0)";
        }
        Text.style.fontSize =  this.GraphicsSize + "px";
        Text.setAttribute("x", this.GraphicsPoint.split(",")[0]);
        Text.setAttribute("y", (parseInt(this.GraphicsPoint.split(",")[1]) + parseInt(this.GraphicsSize)).toString());
        Text.textContent = this.GraphicsText;
        if (this.GraphicsStyle != "横向显示")
        {
            var s = "rotate(90 " + this.GraphicsPoint.split(",")[0] + "," + (parseInt(this.GraphicsPoint.split(",")[1]) + parseInt(this.GraphicsSize)).toString() + ")";
            Text.setAttribute("transform", "rotate(90 " + this.GraphicsPoint.split(",")[0] + "," + (parseInt(this.GraphicsPoint.split(",")[1]) + parseInt(this.GraphicsSize)).toString()+")");
        }
        return Text;
    }
}