/* 用于生成一个SVG对象 */
var Svg = (function () {
    function Svg() {
        /* Svg标示符 */
        this.SVG_NS = "http://www.w3.org/2000/svg";
    }
    /* Svg对象 */
    Svg.prototype.GetSvg = function () {
        return document.createElementNS(this.SVG_NS, "svg");
    };
    /* Svg•折线 */
    Svg.prototype.GetSvgPolyline = function () {
        return document.createElementNS(this.SVG_NS, "polyline");
    };
    /* Svg•字体 */
    Svg.prototype.GetSvgText = function () {
        return document.createElementNS(this.SVG_NS, "text");
    };
    /* Svg•矩形 */
    Svg.prototype.GetSVGRect = function () {
        return document.createElementNS(this.SVG_NS, "rect");
    };
    /* Svg•圆形 */
    Svg.prototype.GetSVGCircle = function () {
        return document.createElementNS(this.SVG_NS, "circle");
    };
    /* Svg•椭圆 */
    Svg.prototype.GetSVGEllipsee = function () {
        return document.createElementNS(this.SVG_NS, "ellipse");
    };
    /* Svg•线 */
    Svg.prototype.GetSVGLine = function () {
        return document.createElementNS(this.SVG_NS, "line");
    };
    /* Svg•多边形 */
    Svg.prototype.GetSVGPolygon = function () {
        return document.createElementNS(this.SVG_NS, "polygon");
    };
    /* Svg•路径 */
    Svg.prototype.GetSVGPath = function () {
        return document.createElementNS(this.SVG_NS, "path");
    };
    /* Svg•坐标类型 */
    Svg.prototype.GetSVGPoint = function () {
        return this.GetSvg().createSVGPoint();
    };
    Svg.prototype.GetSVGMatrix = function () {
        return this.GetSvg().createSVGMatrix();
    };
    return Svg;
})();
//# sourceMappingURL=SVG.js.map