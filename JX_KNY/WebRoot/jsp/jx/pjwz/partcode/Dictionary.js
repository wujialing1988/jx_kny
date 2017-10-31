//定义可设置或检索的键值对。
var KeyValuePair = (function () {
    //用指定的键和值初始化KeyValuePair<TKey,TValue> 结构的新实例。
    function KeyValuePair(key, value) {
        this.Key = key;
        this.Value = value;
    }
    //使用键和值的字符串表示形式返回KeyValuePair<TKey,TValue> 的字符串表示形式。
    KeyValuePair.prototype.ToString = function () {
        return typeof (this.Key) + ":" + typeof (this.Value);
    };
    return KeyValuePair;
})();
//自定义字典，提供字典基础方法
var Dictionary = (function () {
    //构造函数无参的情况默认
    function Dictionary() {
        var _this = this;
        this.DictionaryError = document.createEvent("MutationEvents");
        this.DictionaryError.initEvent("DictionaryError", false, false);
        this.Keys = new Array();
        this.Values = new Array();
        this.DictionaryList = new Array();
        Object.defineProperty(this, "Count", { get: function () { return _this.DictionaryList.length; } });
    }
    /**
     * 添加字典项目
     * [本方法支持document对象事件返回,使用方法为:document.addEventListener("DictionaryError", 事件发生时触发的函数带e参, false);在e.Message中收取函数处理情况详细说明]
     * @param {TKey} _TKey - 要添加的Key.
     * @param {TValue} _TValue - 要添加的Value.
     * @returns {Add}- 返回添加成功与否，成功返回true，不成功返回false.
   */
    Dictionary.prototype.Add = function (_TKey, _TValue) {
        var _this = this;
        if (_TKey != undefined && _TValue != undefined) {
            this[_TKey + ""] = "";
            var NewValue = "";
            Object.defineProperty(this, _TKey + "", {
                get: function () {
                    return NewValue;
                },
                set: function (i) {
                    var s = _this.ContainsKeyIndex(_TKey);
                    if (s[0]) {
                        _this.DictionaryList[s[1]] = new KeyValuePair(_TKey, i);
                        _this.Values[s[1]] = i;
                    }
                    NewValue = i;
                }
            });
            this[_TKey + ""] = _TValue;
            var _KeyValuePair = new KeyValuePair(_TKey, _TValue);
            this.DictionaryList[this.DictionaryList.length] = _KeyValuePair;
            this.Keys[this.Keys.length] = this.DictionaryList[this.DictionaryList.length - 1].Key;
            this.Values[this.Values.length] = this.DictionaryList[this.DictionaryList.length - 1].Value;
            return true;
        }
        else {
            this.DictionaryError["Message"] = "Add方法（Key或者Value不能为空）";
            document.dispatchEvent(this.DictionaryError);
            return false;
        }
    };
    /**
     * 判断当前键是否在字典中
     * @param {TKey} _TKey - 要查询的Key.
     * @returns {ContainsKey}- 返回当前键是否存在，存在返回true，不存在返回false.
   */
    Dictionary.prototype.ContainsKey = function (_TKey) {
        for (var i = 0; i < this.DictionaryList.length; i++) {
            if (this.DictionaryList[i].Key == _TKey) {
                return true;
            }
        }
        return false;
    };
    /**
     * 返回当前键是否在字典中并返回当前键的下标
     * @param {TKey} _TKey - 要查询的Key。
     * @returns {ContainsKeyIndex}- 返回当前键是否存在，存在返回数组【true，键的下标】，不存在返回数组【false，-1】.
   */
    Dictionary.prototype.ContainsKeyIndex = function (_TKey) {
        for (var i = 0; i < this.DictionaryList.length; i++) {
            if (this.DictionaryList[i].Key == _TKey) {
                return [true, i];
            }
        }
        return [false, -1];
    };
    /**
     * 返回当前值是否在字典中
     * @param {TValue} _TValue - 要查询的Value.
     * @returns {ContainsValue}- 返回当前键是否存在，存在返回true，不存在返回false.
   */
    Dictionary.prototype.ContainsValue = function (_TValue) {
        for (var i = 0; i < this.DictionaryList.length; i++) {
            if (this.DictionaryList[i].Value == _TValue) {
                return true;
            }
        }
        return false;
    };
    /**
     * 返回当前值是否在字典中并返回当前值的下标
     * @param {TValue} _TValue - 要查询的Value.
     * @returns {ContainsValueIndex}- 返回当前值是否存在，存在返回数组【true，值的下标】，不存在返回数组【false，-1】.
   */
    Dictionary.prototype.ContainsValueIndex = function (_TValue) {
        for (var i = 0; i < this.DictionaryList.length; i++) {
            if (this.DictionaryList[i].Value == _TValue) {
                return [true, i];
            }
        }
        return [false, -1];
    };
    /**
     * 删除字典里对应的键和值
     * [本方法支持document对象事件返回,使用方法为:document.addEventListener("DictionaryError", 事件发生时触发的函数带e参, false);在e.Message中收取函数处理情况详细说明]
     * @param {TKey} _TKey - 要删除键值对中的键.
     * @returns {Remove}- 返回是否删除成功，成功返回true，不成功返回false.
   */
    Dictionary.prototype.Remove = function (_TKey) {
        var s = this.ContainsKeyIndex(_TKey);
        if (s[0]) {
            delete this[_TKey + ""];
            this.DictionaryList.splice(s[1], 1);
            return true;
        }
        else {
            this.DictionaryError["Message"] = "Remove方法（当前字典中没有传入的键）";
            document.dispatchEvent(this.DictionaryError);
            return false;
        }
    };
    /**
     * 查找当前键是否存在，如果存在则返回当前键对应的值，如果不存在则返回参数二
     * @param {TKey} _TKey - 用于查找的键.
     * @param {TValue} _TValue - 如果没查到找当前键对应的值,则返回本参数.
     * @returns {TryGetValue}- 返回查找到的值,如果未找到当前值则返回参数_TValue.
   */
    Dictionary.prototype.TryGetValue = function (_TKey, _TValue) {
        var s = this.ContainsKeyIndex(_TKey);
        if (s[0]) {
            return this.DictionaryList[s[1]].Value;
        }
        else {
            return _TValue;
        }
    };
    //清空字典里所有的值和键
    Dictionary.prototype.Clear = function () {
        var _this = this;
        this.Keys.forEach(function (i) { delete _this[i + ""]; });
        this.Keys = new Array();
        this.Values = new Array();
        this.DictionaryList = new Array();
    };
    return Dictionary;
})();
//# sourceMappingURL=Dictionary.js.map