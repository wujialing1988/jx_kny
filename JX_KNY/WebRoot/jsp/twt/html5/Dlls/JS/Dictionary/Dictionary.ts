//定义可设置或检索的键值对。
class KeyValuePair<TKey, TValue>
{
    //获取键值对中的值。
    Value: TValue;
    //获取键值对中的键。
    Key: TKey;
    //用指定的键和值初始化KeyValuePair<TKey,TValue> 结构的新实例。
    constructor(key: TKey, value: TValue)
    {
        this.Key = key;
        this.Value = value;
    }
    //使用键和值的字符串表示形式返回KeyValuePair<TKey,TValue> 的字符串表示形式。
    ToString()
    {
        return typeof (this.Key) + ":" + typeof (this.Value);
    }
}
//自定义字典，提供字典基础方法
class Dictionary<TKey, TValue>
{
    /**
     * 自定义事件用户返回字典的错误
     */
    DictionaryError: Event;
    /**
     * 字典内部结构
     */
    private DictionaryList: Array<KeyValuePair<TKey, TValue>>;
    /**
     * 字典里的键数组 
     */
    Keys: Array<TKey>;
    /**
     * 字典里的值数组 
     */
    Values: Array<TValue>;
    /**
     *返回当前字典的中值的数量 
     */
    Count: number;
    //构造函数无参的情况默认
    constructor()
    {
        this.DictionaryError = document.createEvent("MutationEvents");
        this.DictionaryError.initEvent("DictionaryError", false, false);
        this.Keys = new Array<TKey>();
        this.Values = new Array<TValue>();
        this.DictionaryList = new Array<KeyValuePair<TKey, TValue>>();
        Object.defineProperty(this, "Count", { get: () => { return this.DictionaryList.length; } });
    }
    /**
     * 添加字典项目
     * [本方法支持document对象事件返回,使用方法为:document.addEventListener("DictionaryError", 事件发生时触发的函数带e参, false);在e.Message中收取函数处理情况详细说明]
     * @param {TKey} _TKey - 要添加的Key.
     * @param {TValue} _TValue - 要添加的Value.
     * @returns {Add}- 返回添加成功与否，成功返回true，不成功返回false.
   */
    Add(_TKey: TKey, _TValue: TValue): boolean
    {
        if (_TKey != undefined && _TValue != undefined)
        {
            this[_TKey + ""] = "";
            var NewValue = "";
            Object.defineProperty(this, _TKey + "",
                {
                    get: () =>
                    {
                        return NewValue;
                    },
                    set: i =>
                    {

                        var s = this.ContainsKeyIndex(_TKey);
                        if (s[0])
                        {
                            this.DictionaryList[<number>s[1]] = new KeyValuePair(_TKey, i);
                            this.Values[<number>s[1]] = i;
                        }
                        NewValue = i;
                    }
                });
            this[_TKey + ""] = _TValue;
            var _KeyValuePair = new KeyValuePair<TKey, TValue>(_TKey, _TValue);
            this.DictionaryList[this.DictionaryList.length] = _KeyValuePair;
            this.Keys[this.Keys.length] = this.DictionaryList[this.DictionaryList.length - 1].Key;
            this.Values[this.Values.length] = this.DictionaryList[this.DictionaryList.length - 1].Value;
            return true;

        }
        else
        {
            this.DictionaryError["Message"] = "Add方法（Key或者Value不能为空）";
            document.dispatchEvent(this.DictionaryError);
            return false;
        }
    }
    /**
     * 判断当前键是否在字典中
     * @param {TKey} _TKey - 要查询的Key.
     * @returns {ContainsKey}- 返回当前键是否存在，存在返回true，不存在返回false.
   */
    ContainsKey(_TKey: TKey): boolean
    {
        for (var i = 0; i < this.DictionaryList.length; i++)
        {
            if (this.DictionaryList[i].Key == _TKey)
            {
                return true;
            }
        }
        return false;
    }
    /**
     * 返回当前键是否在字典中并返回当前键的下标
     * @param {TKey} _TKey - 要查询的Key。
     * @returns {ContainsKeyIndex}- 返回当前键是否存在，存在返回数组【true，键的下标】，不存在返回数组【false，-1】.
   */
    private ContainsKeyIndex(_TKey: TKey): any[]
    {
        for (var i = 0; i < this.DictionaryList.length; i++)
        {
            if (this.DictionaryList[i].Key == _TKey)
            {
                return [true, i];
            }
        }
        return [false, -1];
    }
    /**
     * 返回当前值是否在字典中
     * @param {TValue} _TValue - 要查询的Value.
     * @returns {ContainsValue}- 返回当前键是否存在，存在返回true，不存在返回false.
   */
    ContainsValue(_TValue: TValue): boolean
    {
        for (var i = 0; i < this.DictionaryList.length; i++)
        {
            if (this.DictionaryList[i].Value == _TValue)
            {
                return true;
            }
        }
        return false;
    }
    /**
     * 返回当前值是否在字典中并返回当前值的下标
     * @param {TValue} _TValue - 要查询的Value.
     * @returns {ContainsValueIndex}- 返回当前值是否存在，存在返回数组【true，值的下标】，不存在返回数组【false，-1】.
   */
    private ContainsValueIndex(_TValue: TValue): any[]
    {
        for (var i = 0; i < this.DictionaryList.length; i++)
        {
            if (this.DictionaryList[i].Value == _TValue)
            {
                return [true, i];
            }
        }
        return [false, -1];
    }
    /**
     * 删除字典里对应的键和值
     * [本方法支持document对象事件返回,使用方法为:document.addEventListener("DictionaryError", 事件发生时触发的函数带e参, false);在e.Message中收取函数处理情况详细说明]
     * @param {TKey} _TKey - 要删除键值对中的键.
     * @returns {Remove}- 返回是否删除成功，成功返回true，不成功返回false.
   */
    Remove(_TKey: TKey): boolean
    {
        var s = this.ContainsKeyIndex(_TKey);
        if (s[0])
        {
            delete this[_TKey + ""];
            this.DictionaryList.splice(<number>s[1], 1);
            return true;
        }
        else
        {
            this.DictionaryError["Message"] = "Remove方法（当前字典中没有传入的键）";
            document.dispatchEvent(this.DictionaryError);
            return false;
        }
    }
    /**
     * 查找当前键是否存在，如果存在则返回当前键对应的值，如果不存在则返回参数二
     * @param {TKey} _TKey - 用于查找的键.
     * @param {TValue} _TValue - 如果没查到找当前键对应的值,则返回本参数.
     * @returns {TryGetValue}- 返回查找到的值,如果未找到当前值则返回参数_TValue.
   */
    TryGetValue(_TKey: TKey, _TValue: TValue): TValue
    {
        var s = this.ContainsKeyIndex(_TKey);
        if (s[0])
        {
            return this.DictionaryList[<number>s[1]].Value;
        }
        else
        {
            return _TValue;
        }
    }
    //清空字典里所有的值和键
    Clear()
    {
        this.Keys.forEach(i => { delete this[i + ""] });
        this.Keys = new Array<TKey>();
        this.Values = new Array<TValue>();
        this.DictionaryList = new Array<KeyValuePair<TKey, TValue>>();
    }
}


