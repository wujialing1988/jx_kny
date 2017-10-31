/// <reference path="../../../Scripts/jquery.d.ts" />
/// <reference path="../../../Dlls/JS/LinQ/LINQ.ts" />
/// <reference path="../../../Dlls/JS/Pollution/Pollution.ts" />
enum OpenAngle
{
    小角度,
    大角度
}
enum State
{
    定位,
    反位,
    中位
}

enum Sw
{
    开,
    断,
    关
}

//道岔的动作
abstract class SwitchesAction
{
    //左上的情况
    abstract LeftUp(sw: Switches, Point: SVGPoint, OpenAngle: OpenAngle):Array<SVGElement>;
    //左下的情况
    abstract LeftDown(sw: Switches, Point: SVGPoint, OpenAngle: OpenAngle): Array<SVGElement>;
    //右上的情况
    abstract RightUp(sw: Switches, Point: SVGPoint, OpenAngle: OpenAngle): Array<SVGElement>;
    //右下的情况
    abstract RightDown(sw: Switches, Point: SVGPoint, OpenAngle: OpenAngle): Array<SVGElement>;
    //道岔向上的动作
    Up(sw: Switches, Point: SVGPoint, OpenAngle: OpenAngle): Array<SVGElement>
    {
        switch (sw.LeftRight.toString())
        {
            case "左":
                return this.LeftUp(sw, Point, OpenAngle);
            case "右":
                return this.RightUp(sw, Point, OpenAngle);
            default:
                break;
        }
        return null;
    }
    //道岔向下的动作
    Down(sw: Switches, Point: SVGPoint, OpenAngle: OpenAngle): Array<SVGElement>
    {
        switch (sw.LeftRight.toString())
        {
            case "左":
                return this.LeftDown(sw, Point, OpenAngle);
            case "右":
                return this.RightDown(sw, Point, OpenAngle);
            default:
                break;
        }
        return null;
    }
    //道岔向左的动作
    Left(sw: Switches, Point: SVGPoint, OpenAngle: OpenAngle): Array<SVGElement>
    {
        switch (sw.UpDown.toString())
        {
            case "上":
                return this.LeftUp(sw, Point, OpenAngle);
            case "下":
                return this.LeftDown(sw, Point, OpenAngle);
            default:
                break;
        }
        return null;
    }
    //道岔向右的动作
    Right(sw: Switches, Point: SVGPoint, OpenAngle: OpenAngle): Array<SVGElement>
    {
        switch (sw.UpDown.toString())
        {
            case "上":
                return this.RightUp(sw, Point, OpenAngle);
            case "下":
                return this.RightDown(sw, Point, OpenAngle);
            default:
                break;
        }
        return null;
    }
}
// 道岔的开状态
class SwitchesOpen extends SwitchesAction
{
    S: Svg;
    constructor()
    {
        super()
        this.S = new Svg();
    }
    // 左上的情况
    LeftUp(sw: Switches, Point: SVGPoint, OpenAngle: OpenAngle): Array<SVGElement>
    {
        var SvgList = new Array<SVGElement>();
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
        if (OpenAngle.toString() == "小角度")
        {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 2) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value;
            points.appendItem(point);
        }
        else
        {
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
    }
    // 左下的情况
    LeftDown(sw: Switches, Point: SVGPoint, OpenAngle: OpenAngle): Array<SVGElement>
    {
        var SvgList = new Array<SVGElement>();
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

        if (OpenAngle.toString() == "小角度")
        {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 2) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
            points.appendItem(point);
        }
        else
        {
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
        point.y = MinRect.y.baseVal.value ;
        points.appendItem(point);
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value + MinRect.width.baseVal.value;
        point.y = MinRect.y.baseVal.value ;
        points.appendItem(point);
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);

        return SvgList;
    }
    // 右上的情况
    RightUp(sw: Switches, Point: SVGPoint, OpenAngle: OpenAngle): Array<SVGElement>
    {
        var SvgList = new Array<SVGElement>();
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
        if (OpenAngle.toString() == "小角度")
        {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value /2) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value;
            points.appendItem(point);
        }
        else
        {
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
    }
    // 右下的情况
    RightDown(sw: Switches, Point: SVGPoint, OpenAngle: OpenAngle): Array<SVGElement>
    {
        var SvgList = new Array<SVGElement>();
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
        if (OpenAngle.toString() == "小角度")
        {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 2) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
            points.appendItem(point);
        }
        else
        {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 3.5) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
            points.appendItem(point);
        }

        var point = this.S.GetSVGPoint();
        point.x = (MinRect.width.baseVal.value + MinRect.x.baseVal.value) - (MinRect.width.baseVal.value / 3);
        point.y = MinRect.y.baseVal.value ;
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
        point.y = MinRect.y.baseVal.value ;
        points.appendItem(point);
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value;
        point.y = MinRect.y.baseVal.value ;
        points.appendItem(point);
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);

        return SvgList;
    }
}
// 道岔的关状态
class SwitchesClose extends SwitchesAction
{
    S: Svg;
    constructor()
    {
        super()
        this.S = new Svg();
    }
    // 左上的情况
    LeftUp(sw: Switches, Point: SVGPoint, OpenAngle: OpenAngle): Array<SVGElement>
    {
        var SvgList = new Array<SVGElement>();
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

        if (OpenAngle.toString() == "小角度")
        {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 2) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value;
            points.appendItem(point);
        }
        else
        {
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
        point.x = MinRect.x.baseVal.value
        point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
        points.appendItem(point);
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value + MinRect.width.baseVal.value;
        point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
        points.appendItem(point);
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);

        return SvgList;
    }
    // 左下的情况
    LeftDown(sw: Switches, Point: SVGPoint, OpenAngle: OpenAngle): Array<SVGElement>
    {
        var SvgList = new Array<SVGElement>();
        var MinRect = this.S.GetSVGRect();
        MinRect.x.baseVal.value = Point.x - sw.Width / 2;
        MinRect.y.baseVal.value = Point.y - sw.Height / 2;
        MinRect.width.baseVal.value = sw.Width;
        MinRect.height.baseVal.value = sw.Height;
        var MaxRect = this.S.GetSVGRect();
        MaxRect.x.baseVal.value = Point.x - sw.Width / 2;
        MaxRect.y.baseVal.value = (Point.y - sw.Height / 2)-5;
        MaxRect.width.baseVal.value = sw.Width;
        MaxRect.height.baseVal.value = sw.Height + 5;

        var Polyline = this.S.GetSvgPolyline();
        Polyline["Graphics"] = this;
        Polyline.style.stroke = "white";
        Polyline.style.strokeWidth = sw.PenThickness;
        var points = Polyline.points;

        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value;
        point.y = MinRect.y.baseVal.value +5;
        points.appendItem(point);

        var a = point;

        var point = this.S.GetSVGPoint();
        point.x = (MinRect.width.baseVal.value / 2.8) + MinRect.x.baseVal.value;
        point.y = a.y;
        points.appendItem(point);

        sw.NeatenPoint = this.S.GetSVGPoint();
        sw.NeatenPoint.x = (MinRect.width.baseVal.value / 2.8) + MinRect.x.baseVal.value;
        sw.NeatenPoint.y = a.y;

        if (OpenAngle.toString() == "小角度")
        {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 2) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
            points.appendItem(point);
        }
        else
        {
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
        point.x = MinRect.x.baseVal.value
        point.y = MinRect.y.baseVal.value;
        points.appendItem(point);
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value + MinRect.width.baseVal.value;
        point.y = MinRect.y.baseVal.value;
        points.appendItem(point);
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);

        return SvgList;
    }
    // 右上的情况
    RightUp(sw: Switches, Point: SVGPoint, OpenAngle: OpenAngle): Array<SVGElement>
    {
        var SvgList = new Array<SVGElement>();
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

        if (OpenAngle.toString() == "小角度")
        {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 2) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value ;
            points.appendItem(point);
        }
        else
        {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 3.5) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value ;
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
        point.x = MinRect.x.baseVal.value
        point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
        points.appendItem(point);
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value + MinRect.width.baseVal.value;
        point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
        points.appendItem(point);
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);

        return SvgList;
    }
    // 右下的情况
    RightDown(sw: Switches, Point: SVGPoint, OpenAngle: OpenAngle): Array<SVGElement>
    {
        var SvgList = new Array<SVGElement>();
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
        point.y = MinRect.y.baseVal.value+5;
        points.appendItem(point);

        var a = point;

        var point = this.S.GetSVGPoint();
        point.x = (MinRect.width.baseVal.value + MinRect.x.baseVal.value) - (MinRect.width.baseVal.value / 2.8);
        point.y = a.y;
        points.appendItem(point);

        sw.NeatenPoint = this.S.GetSVGPoint();
        sw.NeatenPoint.x = (MinRect.width.baseVal.value + MinRect.x.baseVal.value) - (MinRect.width.baseVal.value / 2.8);
        sw.NeatenPoint.y = a.y;

        if (OpenAngle.toString() == "小角度")
        {
            var point = this.S.GetSVGPoint();
            point.x = (MinRect.width.baseVal.value / 2) + MinRect.x.baseVal.value;
            point.y = MinRect.y.baseVal.value + MinRect.height.baseVal.value;
            points.appendItem(point);
        }
        else
        {
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
        point.x = MinRect.x.baseVal.value
        point.y = MinRect.y.baseVal.value ;
        points.appendItem(point);
        var point = this.S.GetSVGPoint();
        point.x = MinRect.x.baseVal.value + MinRect.width.baseVal.value;
        point.y = MinRect.y.baseVal.value ;
        points.appendItem(point);
        Polyline.setAttribute("transform", sw.GraphicsTransForm);
        SvgList.push(Polyline);

        return SvgList;
    }
}
//道岔绘制类
class Switches
{
    ///画笔的颜色
    GraphicsPen: string;
    ///画笔的宽度
    PenThickness: string;
    ///图形绘制点集合
    PointList: Array<SVGPoint>;
    ///前接项
    AheadGraphics: SVGElement;
    ///后接项
    RearGraphics: SVGElement;
    ///图形的名称
    GraphicsName: string;
    ///图形的类型
    GraphicsType: string;
    ///图形的Guid
    Guid: string;
    ///图形的编号
    GraphicsCode: string;
    ///绝缘节样式
    InsulationType: InsulationType;
    ///绝缘节上下方位
    UpDown: UpDown;
    ///绝缘节前后方位
    BeforeAfter: BeforeAfter;
    /// 绝缘节左右方位
    LeftRight: LeftRight;
    /// 中心点
    CenterPoint: SVGPoint;
    ///图形的高
    Width: number;
    ///图形的宽
    Height: number;
    //用于对齐的坐标点
    NeatenPoint: SVGPoint;
    // 道岔的开口角度
    OpenAngle: OpenAngle;
    //图形的定反位状态
    State: State;
    //图形分节的颜色
    Segment: string;
    //图形绘制用的坐标
    SwitchesPoint: SVGPoint;
    //图形的开关状态
    switche: Sw;
    //字体的坐标
    TextPoint: SVGPoint;
    //A
    A: number;
    //移动坐标
    Move: SVGPoint;
    //图形的角度
    angle: number;
    //旋转坐标
    Revolve: SVGPoint;
    ///图形的矩阵变换
    GraphicsTransForm: string;
    //道岔搬动前的状态
    PastSwitche: Sw;

    S: Svg;
    constructor()
    {
        this.S = new Svg();
    }
    Open(sw: Switches, Point: SVGPoint): Array<SVGElement>
    {
        var SwitchesAction = new SwitchesOpen();
        switch (sw.UpDown.toString())
        {
            case "上":
                return SwitchesAction.Up(sw, Point, sw.OpenAngle);
            case "下":
                return SwitchesAction.Down(sw, Point, sw.OpenAngle);
        }
        switch (sw.LeftRight.toString())
        {
            case "左":
                return SwitchesAction.Left(sw, Point, sw.OpenAngle);
            case "右":
                return SwitchesAction.Right(sw, Point, sw.OpenAngle);
        }
        return null;
    }
    // 道岔的关状态
    Close(sw: Switches, Point: SVGPoint): Array<SVGElement>
    {
        var SwitchesAction = new SwitchesClose();
        switch (sw.UpDown.toString())
        {
            case "上":
                return SwitchesAction.Up(sw, Point, sw.OpenAngle);
            case "下":
                return SwitchesAction.Down(sw, Point, sw.OpenAngle);
        }
        switch (sw.LeftRight.toString())
        {
            case "左":
                return SwitchesAction.Left(sw, Point, sw.OpenAngle);
            case "右":
                return SwitchesAction.Right(sw, Point, sw.OpenAngle);
        }
        return null;
    }
    GraphicsRead(Switches: Element)
    {

        this.GraphicsPen = Switches.attributes["图形画笔的颜色"].value;
        this.PenThickness = Switches.attributes["图形画笔的宽度"].value;
        this.GraphicsName = Switches.attributes["图形的名字"].value;
        this.Guid = Switches.attributes["图形的唯一标识符"].value;
        this.GraphicsCode = Switches.attributes["图形的编号"].value;
        this.Width = parseInt(Switches.attributes["图形的宽度"].value);
        this.Height = parseInt(Switches.attributes["图形的高度"].value);
        this.State = <State>Switches.attributes["图形的定反位状态"].value;
        this.Segment = Switches.attributes["图形分节的颜色"].value;
        this.SwitchesPoint = Switches.attributes["图形绘制用的坐标"]["ParsePoint"](Switches.attributes["图形绘制用的坐标"].value);
        this.switche = <Sw>Switches.attributes["图形的开关状态"].value;
        this.UpDown = <UpDown>Switches.attributes["图形的上下状态"].value;
        this.LeftRight = <LeftRight>Switches.attributes["图形的左右状态"].value;
        this.OpenAngle = <OpenAngle>Switches.attributes["图形的开角"].value;
        this.PastSwitche = this.switche;
        try
        {
            this.TextPoint = Switches.attributes["字体的坐标"]["ParsePoint"](Switches.attributes["字体的坐标"].value);
            this.A = parseInt(Switches.attributes["A"].value);
            this.Move = Switches.attributes["移动坐标"]["ParsePoint"](Switches.attributes["移动坐标"].value);
            this.angle = parseInt(Switches.attributes["图形的角度"].value);
            this.Revolve = Switches.attributes["旋转坐标"]["ParsePoint"](Switches.attributes["旋转坐标"].value);
            this.GraphicsTransForm = "matrix(" + Switches.attributes["M11"].value + "," + Switches.attributes["M12"].value + "," + Switches.attributes["M21"].value + "," + Switches.attributes["M22"].value + "," + Switches.attributes["OffsetX"].value + "," + Switches.attributes["OffsetY"].value + ")";
        }
        catch(e){}
    }
    GetText(): SVGElement
    {
        if (this.angle >= 90)
        {
            this.angle = this.angle - 180;
        }
        if (this.angle <= -90)
        {
            this.angle = this.angle + 180;
        }
        var Text = this.S.GetSvgText();
        Text["Graphics"] = this;
        Text.textContent = this.GraphicsName;
        Text.style.fontSize = "12px";
        Text.style.fill = "rgba(80,255,255,255)";
        var s = "rotate(" + this.angle + " " + (this.TextPoint.x + 7).toString() + "," + (this.TextPoint.y + 12).toString()+")";
        Text.setAttribute("transform", s);
        Text.setAttribute("x", (this.TextPoint.x + 7).toString());
        Text.setAttribute("y", (this.TextPoint.y+12).toString());
        return Text;
    }
    Render(): Array<SVGElement>
    {
        switch (this.switche.toString())
        {
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
    }
}