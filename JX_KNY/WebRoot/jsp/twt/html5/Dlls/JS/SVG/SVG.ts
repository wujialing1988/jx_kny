/* 用于生成一个SVG对象 */
class Svg
{
    /* Svg标示符 */
    private SVG_NS: string = "http://www.w3.org/2000/svg";
    /* Svg对象 */
    public GetSvg(): SVGSVGElement
    {
        
        return <SVGSVGElement>document.createElementNS(this.SVG_NS, "svg");
    }
    /* Svg•折线 */
    public GetSvgPolyline(): SVGPolylineElement
    {
        return <SVGPolylineElement>document.createElementNS(this.SVG_NS, "polyline");
    }
     /* Svg•字体 */
    public GetSvgText(): SVGTextElement
    {
        return <SVGTextElement>document.createElementNS(this.SVG_NS, "text");
    }
    /* Svg•矩形 */
    public GetSVGRect(): SVGRectElement
    {
        return <SVGRectElement>document.createElementNS(this.SVG_NS, "rect");
    }
    /* Svg•圆形 */
    public GetSVGCircle(): SVGCircleElement
    {
        return <SVGCircleElement>document.createElementNS(this.SVG_NS, "circle");
    }
    /* Svg•椭圆 */
    public GetSVGEllipsee(): SVGEllipseElement
    {
        return <SVGEllipseElement>document.createElementNS(this.SVG_NS, "ellipse");
    }
    /* Svg•线 */
    public GetSVGLine(): SVGLineElement
    {
        return <SVGLineElement>document.createElementNS(this.SVG_NS, "line");
    }
    /* Svg•多边形 */
    public GetSVGPolygon(): SVGPolygonElement
    {
        return <SVGPolygonElement>document.createElementNS(this.SVG_NS, "polygon");
    }
    /* Svg•路径 */
    public GetSVGPath(): SVGPathElement
    {
        return <SVGPathElement>document.createElementNS(this.SVG_NS, "path");
    }
    /* Svg•坐标类型 */
    public GetSVGPoint(): SVGPoint
    {
        return (<SVGSVGElement>this.GetSvg()).createSVGPoint();
    }
  
    public GetSVGMatrix(): SVGMatrix
    {
        return (<SVGSVGElement>this.GetSvg()).createSVGMatrix();
    }
  
}