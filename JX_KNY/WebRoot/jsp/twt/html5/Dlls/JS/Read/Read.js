/// <reference path="../../../Scripts/jquery.d.ts" />
/// <reference path="../../../Dlls/JS/LinQ/LINQ.ts" />
/// <reference path="../../../Dlls/JS/Dictionary/Dictionary.ts" />
var Read = (function () {
    function Read() {
    }
    Read.prototype.ReadCanvas = function (FileName, cc, Canvas) {
        var TrackList = new Dictionary();
        var dictionary = new Dictionary();
        var r = "";
        if (FileName != "") {
            var ddd = FileName.replace(/画板/g, "canvas").replace(/图形的绘制点集合/g, "PointList");
            var xmlDoc = $("<xml>" + FileName.replace(/画板/g, "canvas").replace(/图形的绘制点集合/g, "PointList") + "</xml>");
            try {
                Canvas.rect.width.baseVal.value = parseInt(xmlDoc.find("canvas").attr("ActualWidth"));
                Canvas.rect.height.baseVal.value = parseInt(xmlDoc.find("canvas").attr("ActualHeight"));
                xmlDoc.find("TextFrame").each(function (i, j) {
                    var text = j;
                    var TFrame = new TextFrame();
                    var C = text.attributes["图形的编号"].value;
                    if (C == 0) {
                        r = text.attributes["图形的文本"].value;
                        dictionary.Add("name", r);
                    }
                    TFrame.GraphicsRead(text);
                    var s = TFrame.Render();
                    Canvas.Svg.appendChild(s);
                });
                var TrackLineList = new Array();
                xmlDoc.find("TrackLine").each(function (i, j) {
                    var text = j;
                    var tl = new TrackLine();
                    tl.GraphicsRead(text);
                    TrackList.Add(tl.Guid, tl);
                    var s = tl.Render();
                    TrackLineList.push(tl);
                    Canvas.Svg.appendChild(s[0]);
                    Canvas.Svg.appendChild(s[1]);
                });
                dictionary.Add("TrackLineList", TrackLineList);
                xmlDoc.find("Overhaul").each(function (i, j) {
                    var text = j;
                    var Over = new Overhaul();
                    Over.GraphicsRead(text);
                    var s = Over.Render();
                    Canvas.Svg.appendChild(s);
                });
                var SwitchesList = new Array();
                xmlDoc.find("Switches").each(function (i, j) {
                    var text = j;
                    var switches = new Switches();
                    switches.GraphicsRead(text);
                    var s = switches.Render();
                    SwitchesList.push(switches);
                    if (s != null) {
                        for (var i = 0; i < s.length; i++) {
                            Canvas.Svg.appendChild(s[i]);
                        }
                    }
                });
                dictionary.Add("SwitchesList", SwitchesList);
                var PositionList = new Array();
                xmlDoc.find("Position").each(function (i, j) {
                    var text = j;
                    var Position = new PositionClass();
                    Position.GraphicsRead(text);
                    var s = Position.Render();
                    PositionList.push(Position);
                    Canvas.Svg.appendChild(s[0]);
                    Canvas.Svg.appendChild(s[1]);
                });
                dictionary.Add("PositionList", PositionList);
                xmlDoc.find("Insulation").each(function (i, j) {
                    var text = j;
                    var Track = text.attributes["图形附加的轨道"].value;
                    if (TrackList.Count > 0) {
                        var Line = TrackList[Track];
                        if (Line != null) {
                            var LineTrack = Line;
                            if (LineTrack.PointList.length > 2) {
                                LineTrack.Insulation = new Insulation(LineTrack.PointList[2], LineTrack.PointList[1]);
                                LineTrack.Insulation.TrackLine = LineTrack;
                                var s = LineTrack.Insulation.GraphicsRead(text, LineTrack.Insulation);
                                for (var i = 0; i < s.length; i++) {
                                    Canvas.Svg.appendChild(s[i]);
                                }
                            }
                            else {
                                LineTrack.Insulation = new Insulation(LineTrack.PointList[0], LineTrack.PointList[1]);
                                LineTrack.Insulation.TrackLine = LineTrack;
                                var s = LineTrack.Insulation.GraphicsRead(text, LineTrack.Insulation);
                                for (var i = 0; i < s.length; i++) {
                                    Canvas.Svg.appendChild(s[i]);
                                }
                            }
                        }
                    }
                });
                var LightList = new Array();
                xmlDoc.find("Light").each(function (i, j) {
                    var text = j;
                    var Track = text.attributes["图形附加的轨道"].value;
                    if (TrackList.Count > 0) {
                        var Line = TrackList[Track];
                        if (Line != null) {
                            var LineTrack = Line;
                            if (LineTrack.PointList.length > 2) {
                                var light = new Light(LineTrack.PointList[2], LineTrack.PointList[1], LineTrack);
                                LineTrack.LightList.push(light);
                                var s = light.GraphicsRead(text, light);
                                LightList.push(light);
                                for (var i = 0; i < s.length; i++) {
                                    Canvas.Svg.appendChild(s[i]);
                                }
                            }
                            else {
                                var light = new Light(LineTrack.PointList[0], LineTrack.PointList[1], LineTrack);
                                LineTrack.LightList.push(light);
                                var s = light.GraphicsRead(text, light);
                                LightList.push(light);
                                for (var i = 0; i < s.length; i++) {
                                    Canvas.Svg.appendChild(s[i]);
                                }
                            }
                        }
                    }
                });
                dictionary.Add("LightList", LightList);
                return dictionary;
            }
            catch (e) {
                alert(e.message);
            }
        }
    };
    return Read;
})();
//# sourceMappingURL=Read.js.map