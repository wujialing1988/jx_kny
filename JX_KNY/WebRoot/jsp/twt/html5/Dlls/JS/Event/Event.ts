/*
 * 用于自定义事件的类,可以附加到自定义类里,给自定义类提供自定义事件支持.
 */
class Events
{
    /*
     * 当前事件对应处理函数列表.
     */
    EventCallbackFunctionList: Array<Function>;

    /*
     * 用于自定义事件的类,可以附加到自定义类里,给自定义类提供自定义事件支持.
     */
    constructor()
    {
        this.EventCallbackFunctionList = new Array<Function>();
    }
      /**
     * 事件调用方法,用于在自定义类里触发事件,当本函数被调用的时候,当前事件注册的方法都会被调用.
     * @param {Sender} _Sender -this的应用调用.
     * @param {TEventArgs} _TEventArgs - 触发事件时发送的事件信息类.
     * @returns  无返回值.
   */
    EventTrigger(_Sender: Object, _TEventArgs: any)
    {
        this.EventCallbackFunctionList.forEach(i =>
        {
            i.apply(this, [_Sender,_TEventArgs]);
        });
    }
     /**
     * 事件的回调函数注册方法,用于在自定义类外部注册回调方法.
     * [回调包含两个参数,A参为EventTrigger方法_Sender参数,B参为EventTrigger方法_TEventArgs参数]
     * @param {Callback} _Callback -回调方法.
     * @returns  是否成功.
   */
    AddEventCallback(_Callback: Function): boolean
    {
        if (this.EventCallbackFunctionList.lastIndexOf(_Callback)== -1)
        {
            this.EventCallbackFunctionList.push(_Callback);
            return true;
        }
        return false;
    }
      /**
     * 事件的回调函数删除方法,用于在自定义类外部删除回调方法.
     * @param {Callback} _Callback -回调方法.
     * @returns  是否成功.
   */
    RemoveEventCallback(_Callback: Function): boolean
    {
        var Index = this.EventCallbackFunctionList.lastIndexOf(_Callback)
        if (Index > -1)
        {
            this.EventCallbackFunctionList.splice(Index,1);
            return true;
        }
        return false;
    }
}
  /**
     * 给Events类附加Instance方法.
   */
Events["Instance"] = new Events();
 /**
     * 使用方式.
     * MyClass.prototype.MyEvent = Events.Instance;
   */
Object.defineProperty(Events, "Instance", { get: () => { return new Events(); } });