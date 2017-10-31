Object.prototype["Aggregate"] = function (func, thisArg) {
    var _this = this;
    if (this instanceof Array || this instanceof String) {
        var workingSentence;
        this.ForEach(function (i, j) {
            if (j == 0) {
                workingSentence = i;
                workingSentence = func.call(thisArg, workingSentence, _this[j + 1]);
                if (workingSentence == undefined) {
                    throw new Error("回调函数必须有返回值,当处理完第一个值与第二个值后,返回处理结果,本处理结果会被当成参数一在传给本回调.");
                }
            }
            else {
                if (j + 1 < _this.length) {
                    workingSentence = func.call(thisArg, workingSentence, _this[j + 1]);
                }
            }
        }, this);
        return workingSentence;
    }
    else if (this instanceof Object) {
        var workingSentence;
        var array = new Array();
        this.ForEach(function (i, j) { array.push(j); });
        for (var i = 0; i < array.length; i++) {
            if (i == 0) {
                workingSentence = this[array[i]];
                workingSentence = func.call(thisArg, workingSentence, this[array[i + 1]]);
                if (workingSentence == undefined) {
                    throw new Error("回调函数必须有返回值,当处理完第一个值与第二个值后,返回处理结果,本处理结果会被当成参数一在传给本回调.");
                }
            }
            else {
                if (i + 1 < array.length) {
                    workingSentence = func.call(thisArg, workingSentence, this[array[i + 1]]);
                }
            }
        }
        return workingSentence;
    }
};
Object.prototype["AggregateSeed"] = function (seed, func, thisArg) {
    var workingSentence = seed;
    this.ForEach(function (i, j) {
        workingSentence = func.call(thisArg, workingSentence, i);
        if (workingSentence == undefined) {
            throw new Error("回调函数必须有返回值,当处理完第一个值与第二个值后,返回处理结果,本处理结果会被当成参数一在传给本回调.");
        }
    }, this);
    return workingSentence;
};
Object.prototype["All"] = function (func, thisArg) {
    var Return = false;
    var result = true;
    this.ForEach(function (i, j) {
        Return = func.call(thisArg, i);
        if (!(Return == undefined) && typeof (Return) == "boolean") {
            if (Return == false) {
                result = false;
                return false;
            }
        }
        else {
            throw new Error("回调函数必须有返回值,且返回值必须为boolean");
        }
    });
    return result;
};
Object.prototype["Any"] = function () {
    var result = false;
    this.ForEach(function (i, j) { result = true; });
    return result;
};
Object.prototype["Contains"] = function (Value) {
    var result = false;
    this.ForEach(function (i, j) {
        if (i === Value) {
            result = true;
        }
    });
    return result;
};
Object.prototype["Count"] = function (func, thisArg) {
    if (func == undefined) {
        if (this instanceof Array || this instanceof String) {
            return this.length;
        }
        else {
            var i = 0;
            this.ForEach(function (i, j) {
                i++;
            });
            return i;
        }
    }
    else {
        var Return = false;
        var i = 0;
        this.ForEach(function (i, j) {
            Return = func.call(thisArg, i);
            if (!(Return == undefined) && typeof (Return) == "boolean") {
                if (Return == true) {
                    i++;
                }
            }
            else {
                throw new Error("回调函数必须有返回值,且返回值必须为boolean");
            }
        });
        return i;
    }
};
Object.prototype["DefaultIfEmpty"] = function (Value) {
    var array = new Array();
    this.ForEach(function (i, j) { array.push(i); });
    if (array.length > 0) {
        return array;
    }
    else {
        return Value;
    }
};
Object.prototype["Equals"] = function (Obj) {
    if (Obj["GetHashCode"]() === this.GetHashCode()) {
        return true;
    }
    else {
        return false;
    }
};
Array.prototype["Distinct"] = function (Comparer) {
    if (Comparer == undefined) {
        Comparer = function (i) { return i.GetHashCode(); };
    }
    else if (typeof Comparer != "function") {
        throw new Error("回调必须为函数,且必须有返回值(返回需要对比数据的HashCode),返回值类型为int");
    }
    var arr = new Array();
    var HashCodeArr = new Object();
    this.ForEach(function (i, j) {
        var hashCode = Comparer(i) & 0x7FFFFFFF;
        if (HashCodeArr[hashCode + ""] == undefined) {
            HashCodeArr[hashCode + ""] = hashCode;
            arr.push(i);
        }
        else if (HashCodeArr[hashCode + ""] != hashCode) {
            arr.push(i);
        }
    });
    return arr;
};
Object.prototype["ElementAt"] = function (index) {
    for (var i = 0; i < this.length; i++) {
        if (i == index) {
            return this[i];
        }
    }
};
Object.prototype["Except"] = function (arr, Comparer) {
    if (Comparer == undefined) {
        Comparer = function (i) { return i.GetHashCode(); };
    }
    else if (typeof Comparer != "function") {
        throw new Error("回调必须为函数,且必须有返回值(返回需要对比数据的HashCode),返回值类型为int");
    }
    if (arr instanceof Array && this instanceof String) {
        var array = new Array();
        for (var i = 0; i < this.Count(); i++) {
            var flag = true;
            for (var j = 0; j < arr.length; j++) {
                if (Comparer(this[i]) == Comparer(arr[j]))
                    flag = false;
            }
            if (flag)
                array.push(this[i]);
        }
        return array;
    }
    else if (arr instanceof Object && this instanceof Object) {
        var obj = new Object();
        this.ForEach(function (i, j) {
            var flag = true;
            arr.ForEach(function (h, k) {
                if (Comparer(i) == Comparer(h) && j == k)
                    flag = false;
            });
            if (flag)
                obj[j] = i;
        });
        return obj;
    }
    else {
        throw new Error("参数与调用类必须为数组或者类型,且必须类型相同");
    }
};
Object.prototype["ElementAtOrDefault"] = function (index, Value) {
    for (var i = 0; i < this.length; i++) {
        if (i == index) {
            return this[i];
        }
    }
    return Value;
};
Object.prototype["Serialize"] = function () {
    var serializedCode = "";
    if (this instanceof Number) {
        serializedCode += "[" + typeof this + ":" + this + "]";
    }
    else if (this instanceof Object) {
        var element;
        for (element in this) {
            if (this[element] instanceof Function) {
                serializedCode += "[" + typeof this[element] + ":" + this[element].toString().GetHashCode() + "]";
            }
            else {
                serializedCode += "[" + typeof this[element] + ":" + element + ":" + this[element] + "]";
            }
        }
    }
    else if (this instanceof Function) {
        serializedCode += "[" + typeof this + ":" + this.toString().GetHashCode() + "]";
    }
    else {
        serializedCode += "[" + typeof this + ":" + this + "]";
    }
    return serializedCode.replace(/\s/g, "");
};
Object.prototype["GetHashCode"] = function () {
    var hash = 0, str;
    if (this instanceof Number) {
        return this;
    }
    else if (!(this instanceof String)) {
        str = this.Serialize();
    }
    else {
        str = this;
    }
    if (str.length == 0)
        return hash;
    for (var i = 0; i < str.length; i++) {
        var char = str.charCodeAt(i);
        hash = ((hash << 8) - hash) + char;
        hash = hash & hash;
    }
    return hash;
};
Object.prototype["ForEach"] = function (func, thisArg) {
    if (this instanceof Array || this instanceof String) {
        if (this.forEach) {
            this.forEach(func);
        }
        else {
            for (var i = 0; i < this.length; i++) {
                func.call(thisArg, this[i], i, this);
            }
        }
    }
    else if (this instanceof Object) {
        if (this.forEach) {
            this.forEach(func);
        }
        else {
            for (var s in this) {
                if (typeof this[s] != "function") {
                    func.call(thisArg, this[s], s, this);
                }
            }
        }
    }
    else {
        throw new Error("无法处理的类型");
    }
};
Object.prototype["First"] = function (Comparer) {
    if (Comparer == undefined) {
        Comparer = function (i) { return i === i; };
    }
    else if (typeof Comparer != "function") {
        throw new Error("回调必须为函数,且必须有返回值(返回需要对比数据的HashCode),返回值类型为int");
    }
    if (this instanceof Array || this instanceof String) {
        for (var i = 0; i < this.length; i++) {
            if (Comparer(this[i])) {
                return this[i];
            }
        }
    }
    else if (this instanceof Object) {
        for (var s in this) {
            if (Comparer(this[s])) {
                return this[s];
            }
        }
    }
};
Object.prototype["FirstOrDefault"] = function (value, Comparer) {
    var Value = this.First(Comparer);
    if (Value == undefined) {
        return value;
    }
    else {
        return Value;
    }
};
Object.prototype["Intersect"] = function (arr, Comparer) {
    if (Comparer == undefined) {
        Comparer = function (i) { return i.GetHashCode(); };
    }
    else if (typeof Comparer != "function") {
        throw new Error("回调必须为函数,且必须有返回值(返回需要对比数据的HashCode),返回值类型为int");
    }
    if (arr instanceof Array && this instanceof Array) {
        var array = new Array();
        for (var i = 0; i < this.length; i++) {
            var tTemp = this[i];
            for (var j = 0; j < arr.length; j++) {
                var aTemp = arr[j];
                if (Comparer(tTemp) == Comparer(aTemp)) {
                    array.push(this[i]);
                }
            }
        }
        return array;
    }
    else if (arr instanceof Object && this instanceof Object) {
        var obj = new Object();
        this.ForEach(function (i, j) {
            var flag = false;
            arr.ForEach(function (h, k) {
                if (Comparer(i) == Comparer(h) && j == k)
                    flag = true;
            });
            if (flag) {
                obj[j] = i;
            }
        });
        return obj;
    }
    else {
        throw new Error("参数与调用类必须为数组或者类型,且必须类型相同");
    }
};
Object.prototype["Union"] = function (arr, Comparer) {
    if (Comparer == undefined) {
        Comparer = function (i) { return i.GetHashCode(); };
    }
    else if (typeof Comparer != "function") {
        throw new Error("回调必须为函数,且必须有返回值(返回需要对比数据的HashCode),返回值类型为int");
    }
    if (arr instanceof Array && this instanceof Array) {
        //建一个新的数组，存放合并后的元素 
        var array = new Array();
        array = this;
        for (var i = 0; i < arr.length; i++) {
            var aTemp = arr[i];
            for (var j = 0; j < this.length; j++) {
                var tTemp = this[j];
                if (Comparer(tTemp) != Comparer(aTemp)) {
                    array.push(aTemp);
                }
                break;
            }
        }
        return array;
    }
    else if (arr instanceof Object && this instanceof Object) {
        var obj = new Object();
        this.ForEach(function (i, j) {
            obj[j] = i;
            arr.ForEach(function (h, k) {
                if (Comparer(i) == Comparer(h) && j == k) {
                    obj[j] = j;
                }
            });
        });
        return obj;
    }
    else {
        throw new Error("参数与调用类必须为数组或者类型,且必须类型相同");
    }
};
Object.prototype["GroupBy"] = function (keySelector, elementSelector) {
    if (keySelector == undefined) {
        throw new Error("条件函数必须传入,函数返回需要分组用的key(如:function (i) {return i};)");
    }
    if (elementSelector == undefined) {
        elementSelector = function (i) { return i; };
    }
    else if (typeof elementSelector != "function") {
        throw new Error("处理参必须为函数,返回值任意");
    }
    var HashCodeArr = new Object();
    this.ForEach(function (i, j) {
        var Key = keySelector(i);
        var hashCode = Key.GetHashCode() & 0x7FFFFFFF;
        if (HashCodeArr[hashCode + ""] == undefined) {
            var List = [];
            var Grouping = new Object();
            List.push(elementSelector(i));
            Grouping["Key"] = Key;
            Grouping["Value"] = List;
            HashCodeArr[hashCode + ""] = [hashCode, Grouping];
        }
        else {
            if (HashCodeArr[hashCode + ""][1]["Key"] == Key) {
                HashCodeArr[hashCode + ""][1]["Value"]["push"](elementSelector(i));
            }
        }
    });
    var arr = new Array();
    HashCodeArr["ForEach"](function (i, j) {
        arr.push(i[1]);
    });
    return arr;
};
Object.prototype["Skip"] = function (Count) {
    if (this instanceof Array) {
        var array = new Array();
        if (Count < 0) {
            Count = 0;
        }
        for (var i = Count; i < this.length; i++) {
            array.push(this[i]);
        }
        return array;
    }
    else if (this instanceof Object) {
        var obj = new Object();
        var Temp = 0;
        this.ForEach(function (i, j) {
            if (Temp >= Count) {
                obj[j] = i;
            }
            Temp++;
        });
        return obj;
    }
};
Object.prototype["Take"] = function (Count) {
    if (this instanceof Array) {
        var array = new Array();
        if (Count > this.length) {
            Count = this.length;
        }
        for (var i = 0; i < Count; i++) {
            array.push(this[i]);
        }
        return array;
    }
    else if (this instanceof Object) {
        var obj = new Object();
        var Temp = 0;
        this.ForEach(function (i, j) {
            if (Temp < Count) {
                obj[j] = i;
            }
            else {
                return;
            }
            Temp++;
        });
        return obj;
    }
};
Array.prototype["Reverse"] = function () {
    var array = new Array();
    for (var i = this.length - 1; i >= 0; i--) {
        array.push(this[i]);
    }
    return array;
};
Object.prototype["Last"] = function (Comparer) {
    if (Comparer == undefined) {
        Comparer = function (i) { return i === i; };
    }
    else if (typeof Comparer != "function") {
        throw new Error("回调必须为函数,且必须有返回值(返回需要对比数据的HashCode),返回值类型为int");
    }
    if (this instanceof Array || this instanceof String) {
        for (var i = this.length - 1; i >= 0; i--) {
            if (Comparer(this[i])) {
                return this[i];
            }
        }
    }
    else if (this instanceof Object) {
        var s;
        this.ForEach(function (i, j) {
            s = j;
        });
        return this[s];
    }
};
Object.prototype["LastOrDefault"] = function (value, Comparer) {
    var Value = this.Last(Comparer);
    if (Value == undefined) {
        return value;
    }
    else {
        return Value;
    }
};
Object.prototype["GroupJoin"] = function (inner, outerKeySelector, innerKeySelector, resultSelector) {
    if (inner == undefined) {
        throw new Error("用于连接的参数不能为空");
    }
    if (outerKeySelector == undefined || innerKeySelector == undefined || typeof outerKeySelector != "function" || typeof innerKeySelector != "function") {
        throw new Error("条件必须传入且必须为函数,函数返回需要分组用的key(如:function (i) {return i};)");
    }
    if (resultSelector == undefined) {
        resultSelector = function (i) { return i; };
    }
    else if (typeof resultSelector != "function") {
        throw new Error("处理参必须为函数,返回值任意");
    }
    var array = new Array();
    this.ForEach(function (i, j) {
    });
};
Object.prototype["Concat"] = function (second) {
    var _this = this;
    if (second == undefined) {
        throw new Error("用于连接的参数不能为空");
    }
    if (this instanceof Array || this instanceof String) {
        second.ForEach(function (i, j) {
            if (_this instanceof Array) {
                _this.push(i);
            }
            else if (_this instanceof String) {
                _this.link(i);
            }
        });
    }
    else if (this instanceof Object && second instanceof Array) {
        this["Array"] = second;
    }
    return this;
};
Object.prototype["SkipWhile"] = function (Count, func, thisArg) {
    if (func == undefined) {
        if (this instanceof Array || this instanceof String) {
            var array = new Array();
            if (Count < 0) {
                Count = 0;
            }
            for (var i = Count; i < this.length; i++) {
                array.push(this[i]);
            }
            return array;
        }
        else {
            var array = new Array();
            var Return = false;
            if (Count < 0) {
                Count = 0;
            }
            for (var i = Count; i < this.length; i++) {
                Return = func.call(thisArg, i);
                if (Return) {
                    array.push(this[i]);
                }
            }
        }
    }
    else {
        var Return = false;
        var obj = new Object();
        var Temp = 0;
        this.ForEach(function (i, j) {
            Return = func.call(thisArg, i);
            if (!(Return == undefined) && typeof (Return) == "boolean") {
                if (Return == true && Temp >= Count) {
                    obj[j] = i;
                }
            }
            else {
                throw new Error("回调函数必须有返回值,且返回值必须为boolean");
            }
            Temp++;
        });
        return obj;
    }
};
Object.prototype["TakeWhile"] = function (Count, func, thisArg) {
    if (func == undefined) {
        if (this instanceof Array || this instanceof String) {
            var array = new Array();
            if (Count > this.length) {
                Count = this.length;
            }
            for (var i = 0; i < Count; i++) {
                array.push(this[i]);
            }
            return array;
        }
        else {
            var array = new Array();
            var Return = false;
            if (Count > this.length) {
                Count = this.length;
            }
            for (var i = 0; i < Count; i++) {
                Return = func.call(thisArg, i);
                if (Return) {
                    array.push(this[i]);
                }
            }
            return array;
        }
    }
    else {
        var Return = false;
        var obj = new Object();
        var Temp = 0;
        var Temp = 0;
        this.ForEach(function (i, j) {
            Return = func.call(thisArg, i);
            if (!(Return == undefined) && typeof (Return) == "boolean") {
                if (Return == true) {
                    if (Temp < Count) {
                        obj[j] = i;
                    }
                    else {
                        return;
                    }
                }
            }
            else {
                throw new Error("回调函数必须有返回值,且返回值必须为boolean");
            }
            Temp++;
        });
        return obj;
    }
};
Array.prototype["Range"] = function (Start, Count) {
    var array = new Array();
    for (var i = 0; i < Count; i++) {
        array.push(Start + i);
    }
    return array;
};
Object.prototype["Repeat"] = function (Element, Count) {
    if (this instanceof Array) {
        var array = new Array();
        for (var i = 0; i < Count; i++) {
            array.push(Element);
        }
        return array;
    }
    else if (this instanceof Object) {
        var obj = new Object();
        var Temp = 0;
        for (var i = 0; i < Count; i++) {
            obj[i] = Element;
        }
        return obj;
    }
};
Object.prototype["Where"] = function (func, thisArg) {
    if (this instanceof Array) {
        var array = new Array();
        var Return = false;
        for (var i = 0; i < this.length; i++) {
            Return = func.call(thisArg, this[i]);
            if (Return) {
                array.push(this[i]);
            }
        }
        return array;
    }
    else if (this instanceof Object) {
        var Return = false;
        var obj = new Object();
        this.ForEach(function (i, j) {
            Return = func.call(thisArg, i);
            if (!(Return == undefined) && typeof (Return) == "boolean") {
                if (Return == true) {
                    obj[j] = i;
                }
            }
            else {
                throw new Error("回调函数必须有返回值,且返回值必须为boolean");
            }
        });
        return obj;
    }
};
Array.prototype["OrderBy"] = function (fun, thisArg) {
    if (fun == undefined) {
        fun = function (i) { return i.GetHashCode(); };
    }
    else if (typeof fun != "function") {
        throw new Error("回调必须为函数,且必须有返回值(返回需要对比数据的HashCode),返回值类型为int");
    }
    if (this instanceof Array) {
        for (var i = 0; i < this.length - 1; i++) {
            //内层循环，反复交换，将最小值移到A[i]处
            for (var j = this.length - 1; j > i; j--) {
                //当a[j]<a[j-1]时，交换元素次序，实现递增排序
                var s1 = fun.call(this, this[j]);
                var s2 = fun.call(this, this[j - 1]);
                if ((s1.GetHashCode() & 0x7FFFFFFF) < (s2.GetHashCode() & 0x7FFFFFFF)) {
                    var temp;
                    temp = this[j - 1];
                    this[j - 1] = this[j];
                    this[j] = temp;
                }
            }
        }
        return this;
    }
};
Array.prototype["OrderByDescending"] = function (fun, thisArg) {
    if (fun == undefined) {
        fun = function (i) { return i.GetHashCode(); };
    }
    else if (typeof fun != "function") {
        throw new Error("回调必须为函数,且必须有返回值(返回需要对比数据的HashCode),返回值类型为int");
    }
    if (this instanceof Array) {
        for (var i = 0; i < this.length - 1; i++) {
            //内层循环，反复交换，将最大值移到A[i]处
            for (var j = this.length - 1; j > i; j--) {
                //当a[j]>a[j-1]时，交换元素次序，实现递增排序
                var s1 = fun.call(this, this[j]);
                var s2 = fun.call(this, this[j - 1]);
                if ((s1.GetHashCode() & 0x7FFFFFFF) > (s2.GetHashCode() & 0x7FFFFFFF)) {
                    var temp;
                    temp = this[j - 1];
                    this[j - 1] = this[j];
                    this[j] = temp;
                }
            }
        }
        return this;
    }
};
Object.prototype["Join"] = function (inner, outerKeySelector, innerKeySelector, resultSelector) {
    if (inner == undefined) {
        throw new Error("用于连接的参数不能为空");
    }
    if (outerKeySelector == undefined || innerKeySelector == undefined || typeof outerKeySelector != "function" || typeof innerKeySelector != "function") {
        throw new Error("条件必须传入且必须为函数,函数返回需要分组用的key(如:function (i) {return i};)");
    }
    if (resultSelector == undefined) {
        resultSelector = function (i) { return i; };
    }
    else if (typeof resultSelector != "function") {
        throw new Error("处理参必须为函数,返回值任意");
    }
};
//# sourceMappingURL=LINQ.js.map