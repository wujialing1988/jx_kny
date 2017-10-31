/// <reference path="../../../Scripts/jquery.d.ts" />
/// <reference path="../../../Dlls/JS/LinQ/LINQ.ts" />
var TextFrame = (function () {
    function TextFrame() {
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
    TextFrame.prototype.GraphicsRead = function (TextFrame) {
        this.GraphicsPen = "white";
        this.GraphicsCode = TextFrame.attributes["图形的编号"].value;
        this.GraphicsText = TextFrame.attributes["图形的文本"].value;
        this.Guid = TextFrame.attributes["图形的唯一标识符"].value;
        this.GraphicsStyle = TextFrame.attributes["图形样式"].value;
        this.GraphicsPoint = TextFrame.attributes["图形的坐标"].value;
        this.GraphicsSize = TextFrame.attributes["图形大小"].value;
    };
    TextFrame.prototype.Render = function () {
        var Text = this.S.GetSvgText();
        Text["Graphics"] = this;
        if (this.GraphicsCode != "1") {
            Text.style.fill = "white";
        }
        else {
            Text.style.fill = "rgba(100,100,0,0)";
        }
        Text.style.fontSize = this.GraphicsSize + "px";
        Text.setAttribute("x", this.GraphicsPoint.split(",")[0]);
        Text.setAttribute("y", (parseInt(this.GraphicsPoint.split(",")[1]) + parseInt(this.GraphicsSize)).toString());
        Text.textContent = this.GraphicsText;
        if (this.GraphicsStyle != "横向显示") {
            var s = "rotate(90 " + this.GraphicsPoint.split(",")[0] + "," + (parseInt(this.GraphicsPoint.split(",")[1]) + parseInt(this.GraphicsSize)).toString() + ")";
            Text.setAttribute("transform", "rotate(90 " + this.GraphicsPoint.split(",")[0] + "," + (parseInt(this.GraphicsPoint.split(",")[1]) + parseInt(this.GraphicsSize)).toString() + ")");
        }
        return Text;
    };
    return TextFrame;
})();
//# sourceMappingURL=TextFrame.js.map