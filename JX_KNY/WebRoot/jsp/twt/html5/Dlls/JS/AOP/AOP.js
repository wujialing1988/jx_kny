/**
 * ALLMonitor方法是用的枚举，用于确定回调函数在什么情况下被调用
 */
var BeforeAndAfter;
(function (BeforeAndAfter) {
    /**
     * 运行或赋值前调用
     */
    BeforeAndAfter[BeforeAndAfter["Before"] = 0] = "Before";
    /**
     * 运行或赋值后调用
     */
    BeforeAndAfter[BeforeAndAfter["After"] = 1] = "After";
    /**
     * Around 可以通过一个在函数或者属性赋值执行前后做一些事情的机会，可以决定什么时候，怎么样去执行当前函数或属性赋值操作，甚至可以决定是否真的执行当前函数或给属性赋值的操作.
     * 当Around切入后,会首先运行回调函数,然后给当前调用类提供一个与注册函数同名+Original的函数,以便在回调函数中确定什么时候运行注册函数.
     * 属性的方式与函数方式相同,但会给类注册一个名为(属性名+Assign) 的函数(本函数为确定给属性赋值,只有在回调函数中才有用,普通情况使用返回一个字符串).以便在回掉中运行给属性赋值的操作.
     * (注:有一定危险性的方式,特殊情况使用.)
     */
    BeforeAndAfter[BeforeAndAfter["Around"] = 2] = "Around";
})(BeforeAndAfter || (BeforeAndAfter = {}));
/**
 * 包含函数或者属性监控，监控包括函数运行前，运行后返回前。属性修改前，修改后未返回前
 */
var AOP = (function () {
    /**
     * 默认的构造函数(如果不输入参数则直接实例化本类型，如果输入参数则会自动判断参数是否为类型，如果是类型的实例化，则无需调用注册方法)
     */
    function AOP() {
        this.AOPError = document.createEvent("MutationEvents");
        this.AOPError.initEvent("AOPError", false, false);
        if (arguments.length === 1) {
            var obj = arguments[0];
            var r = typeof (obj);
            if (r === "object") {
                obj["Before"] = this.Before;
                obj["After"] = this.After;
                obj["Around"] = this.Around;
                obj["ALLMonitor"] = this.ALLMonitor;
            }
        }
    }
    /**
     * 手动注册函数，只支持类，如果实例化时未传入参数，则需要使用本方法注册类
     */
    AOP.prototype.AOP = function (_Object) {
        var r = typeof (_Object);
        if (r === "object") {
            _Object["Before"] = this.Before;
            _Object["After"] = this.After;
            _Object["Around"] = this.Around;
            _Object["ALLMonitor"] = this.ALLMonitor;
        }
    };
    /**
     * 监听一个类的全部属性与函数.
     *  @param {Function} _Callback - 自定义的回调函数.
     * [Before回调函数可以取得当前调用传入的参数,通过自定义参(比如调用的函数传入了a,b两个参,回调需要定义最少2个参)或者arguments取得.可以返回给当前调用函数,但是必须返回数组(比如传入参为a,b,在回调中改为b,c.则返回[b,c])]
     * [After回调函数可以取得当前函数的返回值,通过自定义参取得(比如定一个参A,A就是调用函数的返回值)或者arguments取得.可以返回给当前调用函数(修改后的返回值),可以任意返回.]
     * [Around回调函数可以取得当前调用传入的参数,通过自定义参(比如调用的函数传入了a,b两个参,回调需要定义最少2个参)或者arguments取得. * 当Around切入后,会首先运行回调函数,然后给当前调用类提供一个与注册函数同名+Original的函数,以便在回调函数中确定什么时候运行注册函数.属性的方式与函数方式相同,但会给类注册一个名为(属性名+Assign) 的函数(本函数为确定给属性赋值,只有在回调函数中才有用,普通情况使用返回一个字符串).以便在回掉中运行给属性赋值的操作.(注:有一定危险性的方式,特殊情况使用.)]
     *  @param {BeforeAndAfter}  _BeforeAndAfter -用于确定使用什么样的方式监听(切入):Before,After,Around三种方式
     * [Before运行或赋值前调用]
     * [After运行或赋值后调用]
     * [Around 可以通过一个在函数或者属性赋值执行前后做一些事情的机会，可以决定什么时候，怎么样去执行当前函数或属性赋值操作，甚至可以决定是否真的执行当前函数或给属性赋值的操作.当Around切入后,会首先运行回调函数,然后给当前调用类提供一个与注册函数同名+Original的函数,以便在回调函数中确定什么时候运行注册函数.属性的方式与函数方式相同,但会给类注册一个名为(属性名+Assign) 的函数(本函数为确定给属性赋值,只有在回调函数中才有用,普通情况使用返回一个字符串).以便在回掉中运行给属性赋值的操作.(注:有一定危险性的方式,特殊情况使用.)]
     */
    AOP.prototype.ALLMonitor = function (_Callback, _BeforeAndAfter) {
        var r = typeof (_Callback);
        if (r === "function" && !(this instanceof AOP)) {
            switch (_BeforeAndAfter) {
                case BeforeAndAfter.Before:
                    for (var i in this) {
                        if (i != "Before" && i != "After" && i != "Around" && i != "ALLMonitor") {
                            this.Before(i, _Callback);
                        }
                    }
                    return true;
                    break;
                case BeforeAndAfter.After:
                    for (var i in this) {
                        if (i != "Before" && i != "After" && i != "Around" && i != "ALLMonitor") {
                            this.After(i, _Callback);
                        }
                    }
                    return true;
                    break;
                case BeforeAndAfter.Around:
                    for (var i in this) {
                        if (i != "Before" && i != "After" && i != "Around" && i != "ALLMonitor") {
                            this.Around(i, _Callback);
                        }
                    }
                    return true;
                    break;
            }
        }
        else {
            return false;
        }
    };
    //运行或赋值前调用
    AOP.prototype.Before = function (_MethodName, _Callback) {
        var _this = this;
        if (this instanceof AOP) {
            if (typeof _MethodName == "function" && typeof _Callback == "function") {
                var _methodName = _MethodName;
                _MethodName = function () {
                    var Return = _Callback.apply(_this, _MethodName.arguments);
                    if (Return == undefined || !(Return instanceof Array)) {
                        return _methodName.apply(_this, _MethodName.arguments);
                    }
                    else {
                        return _methodName.apply(_this, Return);
                    }
                };
                return _MethodName;
            }
            else if (typeof _MethodName != "function" && typeof _Callback == "function") {
                //this.AOPError["Message"] = "Before方法（目前AOP不支持JS全局属性拦截,请先确定当前属性有附属类,如已有附属类,请通过类的附加Before方法实现当前属性拦截）";
                //document.dispatchEvent(this.AOPError);
                return _MethodName;
            }
            else {
                return _MethodName;
            }
        }
        else if (!(this instanceof AOP)) {
            if (typeof _MethodName == "string" && typeof _Callback == "function") {
                if (_MethodName in this && typeof this[_MethodName] == "function") {
                    var _methodName = this[_MethodName];
                    this[_MethodName] = function () {
                        var Return = _Callback.apply(_this, _this[_MethodName].arguments);
                        if (Return == undefined || !(Return instanceof Array)) {
                            return _methodName.apply(_this, _this[_MethodName].arguments);
                        }
                        else {
                            return _methodName.apply(_this, Return);
                        }
                    };
                }
                else if (_MethodName in this && !(this[_MethodName] instanceof Array)) {
                    var NewValue = this[_MethodName];
                    try {
                        Object.defineProperty(this, _MethodName, {
                            get: function () {
                                return NewValue;
                            },
                            set: function (i) {
                                var _methodName = _MethodName;
                                var Return = _Callback.apply(_this, [i]);
                                if (Return == undefined) {
                                    NewValue = i;
                                }
                                else {
                                    NewValue = Return;
                                }
                            }
                        });
                    }
                    catch (e) { }
                }
                else if (_MethodName in this && this[_MethodName] instanceof Array) {
                }
                else if (!(_MethodName in this)) {
                }
            }
        }
    };
    //运行或赋值后调用
    AOP.prototype.After = function (_MethodName, _Callback) {
        var _this = this;
        if (this instanceof AOP) {
            if (typeof _MethodName == "function" && typeof _Callback == "function") {
                var _methodName = _MethodName;
                _MethodName = function () {
                    var Return = _methodName.apply(_this, _MethodName.arguments);
                    if (Return == undefined) {
                        return _Callback.apply(_this, _MethodName.arguments);
                    }
                    else {
                        var FuncReturn = _Callback.apply(_this, [Return]);
                        if (FuncReturn == undefined) {
                            return Return;
                        }
                        else {
                            return FuncReturn;
                        }
                    }
                };
                return _MethodName;
            }
            else if (typeof _MethodName != "function" && typeof _Callback == "function") {
                //this.AOPError["Message"] = "After方法（目前AOP不支持JS全局属性拦截,请先确定当前属性有附属类,如已有附属类,请通过类的附加Before方法实现当前属性拦截）";
                //document.dispatchEvent(this.AOPError);
                return _MethodName;
            }
            else {
                return _MethodName;
            }
        }
        else if (!(this instanceof AOP)) {
            if (typeof _MethodName == "string" && typeof _Callback == "function") {
                if (_MethodName in this && typeof this[_MethodName] == "function") {
                    var _methodName = this[_MethodName];
                    this[_MethodName] = function () {
                        var Return = _methodName.apply(_this, _this[_MethodName].arguments);
                        if (Return == undefined) {
                            return _Callback.apply(_this, _this[_MethodName].arguments);
                        }
                        else {
                            var FuncReturn = _Callback.apply(_this, [Return]);
                            if (FuncReturn == undefined) {
                                return Return;
                            }
                            else {
                                return FuncReturn;
                            }
                        }
                    };
                }
                else if (_MethodName in this && !(this[_MethodName] instanceof Array)) {
                    var NewValue = this[_MethodName];
                    try {
                        Object.defineProperty(this, _MethodName, {
                            get: function () {
                                var Return = _Callback.apply(_this, NewValue);
                                if (Return == undefined) {
                                    return NewValue;
                                }
                                else {
                                    NewValue = Return;
                                    return NewValue;
                                }
                            },
                            set: function (i) {
                                NewValue = i;
                            }
                        });
                    }
                    catch (e) {
                        alert(e);
                    }
                }
                else if (_MethodName in this && this[_MethodName] instanceof Array) {
                }
                else if (!(_MethodName in this)) {
                }
            }
        }
    };
    //运行或赋值前后都调用
    AOP.prototype.Around = function (_MethodName, _Callback) {
        var _this = this;
        if (this instanceof AOP) {
            if (typeof _MethodName == "function" && typeof _Callback == "function") {
                var _methodName = _MethodName;
                _MethodName = function () {
                    var Array = [];
                    for (var s in _MethodName.arguments) {
                        Array.push(_MethodName.arguments[s]);
                    }
                    Array.push(_methodName);
                    _Callback.apply(_this, Array);
                };
                return _MethodName;
            }
            else if (typeof _MethodName != "function" && typeof _Callback == "function") {
                //this.AOPError["Message"] = "Around方法（目前AOP不支持JS全局属性拦截,请先确定当前属性有附属类,如已有附属类,请通过类的附加Before方法实现当前属性拦截）";
                //document.dispatchEvent(this.AOPError);
                return _MethodName;
            }
            else {
                return _MethodName;
            }
        }
        else if (!(this instanceof AOP)) {
            if (typeof _MethodName == "string" && typeof _Callback == "function") {
                if (_MethodName in this && typeof this[_MethodName] == "function") {
                    this[_MethodName + "Original"] = this[_MethodName];
                    this[_MethodName] = function () {
                        _Callback.apply(_this, _this[_MethodName].arguments);
                    };
                }
                else if (_MethodName in this && !(this[_MethodName] instanceof Array)) {
                    var NewValue = this[_MethodName];
                    this[_MethodName + "Assign"] = function () { return "毕方_ImpactBlue"; };
                    try {
                        Object.defineProperty(this, _MethodName, {
                            get: function () {
                                return NewValue;
                            },
                            set: function (i) {
                                _this[_MethodName + "Assign"] = function () { return NewValue = i; };
                                _Callback.apply(_this, [i]);
                            }
                        });
                    }
                    catch (e) { }
                }
                else if (_MethodName in this && this[_MethodName] instanceof Array) {
                }
                else if (!(_MethodName in this)) {
                }
            }
        }
    };
    return AOP;
})();
//# sourceMappingURL=AOP.js.map