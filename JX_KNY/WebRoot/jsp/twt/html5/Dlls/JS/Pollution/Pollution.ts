/*
返回C点坐标
A:A点坐标
B:B点坐标
L2:B到C点的长度
alpha:C和A的夹角角度
*/ 
Object.prototype["GetC"] = (A: SVGPoint, B: SVGPoint, L2: number, Alpha: number) =>
{
    Alpha = Alpha / 180 * Math.PI;
    var L1 = Math.sqrt((A.x - B.x) * (A.x - B.x) + (A.y - B.y) * (A.y - B.y));
    var LDB = 12 * Math.cos(Alpha);
    var LDA = L1 - 12 * Math.cos(Alpha);
    var LDC = 12 * Math.sin(Alpha);
    var C1, C2, D;
    var Svg = (<SVGSVGElement>document.createElementNS("http://www.w3.org/2000/svg", "svg"));
    C1 = Svg.createSVGPoint();
    C2 = Svg.createSVGPoint();
    D = Svg.createSVGPoint();
    D.y = (A.y * LDB + B.y * LDA) / (LDA + LDB);
    D.x = (A.x * LDB + B.x * LDA) / (LDA + LDB);
    var xlAB = (A.y - B.y) / (A.x - B.x);
    var xlCD;
    if (xlAB == 0)
    {
        var LEC = Math.abs(L2 * Math.sin(Alpha));
        var LEB = Math.abs(L2 * Math.sin(Alpha));
        if (A.x < B.x)
        {
            if (Alpha % (Math.PI * 2) <= Math.PI / 2)
            {
                C1.x = B.x - LEB;
                C1.y = B.y - LEC;
            }
            if (Alpha % (Math.PI * 2) > Math.PI / 2 && Alpha % (Math.PI * 2) <= Math.PI)
            {
                C1.x = B.x + LEB;
                C1.y = B.y - LEC;
            }
            if (Alpha % (Math.PI * 2) > Math.PI && Alpha % (Math.PI * 2) <= Math.PI / 2 * 3)
            {
                C1.x = B.x + LEB;
                C1.y = B.y + LEC;
            }
            if (Alpha % (Math.PI * 2) > Math.PI / 2 * 3 && Alpha % (Math.PI * 2) < Math.PI * 2)
            {
                C1.x = B.x - LEB;
                C1.y = B.y + LEC;
            }
        }
        else
        {
            if (Alpha % (Math.PI * 2) <= Math.PI / 2)
            {
                C1.x = B.x + LEB;
                C1.y = B.y + LEC;
            }

            if (Alpha % (Math.PI * 2) > Math.PI / 2 && Alpha % (Math.PI * 2) <= Math.PI)
            {
                C1.x = B.x - LEB;
                C1.y = B.y + LEC;
            }

            if (Alpha % (Math.PI * 2) > Math.PI && Alpha % (Math.PI * 2) <= Math.PI / 2 * 3)
            {
                C1.x = B.x - LEB;
                C1.y = B.y - LEC;
            }

            if (Alpha % (Math.PI * 2) > Math.PI / 2 * 3 && Alpha % (Math.PI * 2) < Math.PI * 2)
            {
                C1.x = B.x + LEB;
                C1.y = B.y - LEC;
            }
        }
        return C1;
    }
    else
    {
        xlCD = -1 / xlAB;
        var disdc = Math.sqrt(LDC * LDC / (xlCD * xlCD + 1));
        C1.x = D.x + disdc;
        C2.x = D.x - disdc;
        C1.y = xlCD * (C1.x - D.x) + D.y;
        C2.y = xlCD * (C2.x - D.x) + D.y;
    }
    if (Alpha / Math.PI % 2 < 1)
    {
        return C1;
    }
    else
    {
        return C2;
    }
}
/*
取垂直线的斜率
Slope:原始线的斜率 
*/
Object.prototype["GetSlope1"] = (Slope: number) =>
{
    return -1 / Slope;
}
/*
取一条线段的斜率
A:线段A点坐标
B:线段B点坐标
*/
Object.prototype["GetSlope"] = (A: SVGPoint, B: SVGPoint) =>
{
    return <number>((B.y - A.y) / (B.x - A.x));
}
/*
更具某坐标和斜率取坐标
P:原始线段的某点坐标
S:垂直线的斜率
F:两点之间的距离
*/
Object.prototype["GetPath"] = (P: SVGPoint, S: number, F: number) =>
{
    var List = new Array<SVGPoint>();
    if (S == 0)
    {
       S = 1E-20;
    }
    var y = Math.sin(Math.atan(S)) * F;
    var x = y / S;
    var PointA = (<SVGSVGElement>document.createElementNS("http://www.w3.org/2000/svg", "svg")).createSVGPoint();
  
    PointA.x = P.x - x;
    PointA.y = P.y - y;
    var PointB = (<SVGSVGElement>document.createElementNS("http://www.w3.org/2000/svg", "svg")).createSVGPoint();
    PointB.x = P.x + x;
    PointB.y = P.y + y;
    List.push(PointA);
    List.push(PointB);
    return List;
}
/*
更具某坐标和斜率取坐标
P:原始线段的某点坐标
S:垂直线的斜率
F:两点之间的距离
*/
Object.prototype["GetPath1"] = (P: SVGPoint, S: number, F: number) =>
{
    var List = new Array<SVGPoint>();
    if (S == 0)
    {
        S = 1E-20;
    }
    var y = Math.sin(Math.atan(S)) * F;
    var x = y / S;
    var PointA = (<SVGSVGElement>document.createElementNS("http://www.w3.org/2000/svg", "svg")).createSVGPoint();
    if (y < 0)
    {
        y = y - y - y;
    }

    PointA.x = P.x - x;
    PointA.y = P.y - y;
    var PointB = (<SVGSVGElement>document.createElementNS("http://www.w3.org/2000/svg", "svg")).createSVGPoint();
    PointB.x = P.x + x;
    PointB.y = P.y + y;
    List.push(PointA);
    List.push(PointB);
    return List;
}
/*
计算两点之间的距离
A:A点坐标
B:B点坐标
*/
Object.prototype["GetDistance"] = (A: SVGPoint, B: SVGPoint) =>
{
    return <number>Math.sqrt((A.x - B.x) * (A.x - B.x) + (A.y - B.y) * (A.y - B.y));
}

/*
取一条线段的中心点坐标
A:线段A点坐标
B:移动点
*/
Object.prototype["GetCentralPoint"] = (A: SVGPoint, B: SVGPoint) =>
{
    var Point = (<SVGSVGElement>document.createElementNS("http://www.w3.org/2000/svg", "svg")).createSVGPoint();
    Point.x = A.x + (B.x - A.x) / 2;
    Point.y = A.y + (B.y - A.y) / 2;
    return Point;
}

/*
取一条线段的中心点坐标
A:线段A点坐标
B:线段B点坐标
*/
Object.prototype["CentralPoint"] = (A: SVGPoint, B: SVGPoint) =>
{
    var Point = (<SVGSVGElement>document.createElementNS("http://www.w3.org/2000/svg", "svg")).createSVGPoint();
    Point.x = (A.x + B.x) / 2;
    Point.y = (A.y + B.y) / 2;
    return Point;
}
/*
生成SVGPoint
*/
Object.prototype["ParsePoint"] = (Value: string) =>
{
    var Point = (<SVGSVGElement>document.createElementNS("http://www.w3.org/2000/svg", "svg")).createSVGPoint();
    var str = Value.split(',');
    var p1 = 0;
    var p2 = 0;
    if (str.length == 2)
    {
        p1 = parseInt(str[0]);
        p2 = parseInt(str[1]);
    }
    Point.x = p1;
    Point.y = p2;
    return Point;
}
