/// <reference path="../../../Scripts/jquery.d.ts" />
/// <reference path="../../../Dlls/JS/LinQ/LINQ.ts" />
/// <reference path="../../../Dlls/JS/Pollution/Pollution.ts" />
///机车绘制类
class Train
{
    ///当前轨道的A坐标
    A: SVGPoint;
    ///当前轨道的B坐标
    B: SVGPoint;
    ///画笔的颜色
    GraphicsPen: string;
    ///画笔的宽度
    PenThickness: string;
    ///图形绘制点集合
    PointList: Array<string>;
    ///图形的类型
    GraphicsType: string;
    ///图形的Guid
    TrainGuid: string;
    ///图形的宽度
    GraphicsWidth: number;
    ///图形的高度
    GraphicsHeight: number;
    ///图形的矩阵变换
    GraphicsTransForm: string;
    ///机车所属的轨道
    TrackLine: TrackLine;
    ///机车号
    TrainNumber: string;
    ///机车的状态
    TrainStateName: string;
    ///图形所属的画板
    CanvasControl: SVGSVGElement
    ///绘制机车用的坐标点
    Location: SVGPoint;
    ///图形的角度
    Angle: number;
    ///用于生成SVG对象
    S: Svg;
    ///机车上设备时间
    TrainDevicesTime: string;

    constructor(_width: number, _location: SVGPoint, _TrackLine: TrackLine, _CanvasControl: SVGSVGElement, _TrainNumber: string, _TrainDevicesTime: string, b: boolean)
    {
        this.S = new Svg();
        this.GraphicsPen = "";
        this.GraphicsType = "";
        this.TrainGuid = this.Guid();
        this.GraphicsTransForm = "";
        this.GraphicsWidth = _width;
        this.TrainDevicesTime = _TrainDevicesTime;
        this.TrackLine = _TrackLine;
        if (this.TrackLine.PointList.length == 3)
        {
            this.A = this.TrackLine.PointList[0]["ParsePoint"](this.TrackLine.PointList[0]);
            this.B = this.TrackLine.PointList[2]["ParsePoint"](this.TrackLine.PointList[2]);
        }
        else
        {
            this.A = this.TrackLine.PointList[0]["ParsePoint"](this.TrackLine.PointList[0]);
            this.B = this.TrackLine.PointList[1]["ParsePoint"](this.TrackLine.PointList[1]);
        }
        this.CanvasControl = _CanvasControl;
        this.GraphicsHeight = 25;
        this.Location = _location;
        this.Angle = ((Math.atan2(this.B.y - this.A.y, this.B.x - this.A.x) * 180) / Math.PI);
        this.TrainNumber = _TrainNumber;

    }
    OnRender()
    {
        var SvgList = new Array<SVGElement>();
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
        if (W > this.GraphicsWidth)
        {
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
        if (W > this.GraphicsWidth)
        {
            Text.textContent = (Text.textContent.replace(/(\s)*([a-zA-Z0-9]+|\W)(\.\.\.)?$/, "..."));
        }
        Text.setAttribute("transform", "rotate(" + this.Angle + " " + this.Location.x + "," + this.Location.y + ")");
        Text.setAttribute("x", (this.Location.x + 5).toString());
        Text.setAttribute("y", (this.Location.y + 20.5).toString());
        SvgList.push(Text);

        return SvgList;
    }
    Guid()
    {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,(c) =>
        {
            var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    }
}