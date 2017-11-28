/**   
 * 多功能树过滤插件，为TreePanel的tbar添加一个具备拼音和汉字双重过滤功能的textfield   
 *    
 * 依赖：QM.ux.TreeFilter、QM.lib   
 *    
 * 注意：tbar是无法动态创建的，请确保构造树的时候配置了tbar属性   
 *   
 * v1.1:修复3.1下tbar无法显示的问题   
 *    
 */   
Ext.ns('QM.plugin')
QM.plugin.MutilTreeFilter = Ext.extend(Object, {    
    index: 0,//输入域插入tbar的位置索引    
    clearAction:'expand',//树节点全部显示时状态，默认全部展开    
    width:120,//输入域宽度    
    enableButtons:true,//是否添加收缩和展开按钮    
    ignoreFolder:false,//true:过滤时忽略父节点,false:过滤父节点    
        
    constructor:function(config){    
        Ext.apply(this,config);     
    },    
    init: function(tree){    
       tree.on('afterrender', this.onRender, this);    
       this.filter =  new QM.ux.TreeFilter(tree, {    
              clearAction: this.clearAction,    
              ignoreFolder: this.ignoreFolder    
        });    
    },    
    onRender: function(tree){    
        var tbar = tree.getTopToolbar();                
        var field = new Ext.form.TextField({    
            width: this.width,    
            emptyText: i18n.common.tip.QuickSearch,    
            enableKeyEvents: true,    
            listeners: {    
                keyup: {//添加键盘点击监听    
                    fn: function(t, e){    
                        this.filter.filter(t.getValue());    
                    },    
                    buffer: 350,    
                    scope:this   
                }    
            }    
        });    
        if (this.enableButtons) {    
            tbar.insertButton(this.index, [' ', ' ', {    
                iconCls: 'icon-expand-all',    
                tooltip: '全部展开',    
                handler: function(){    
                    tree.root.expand(true);    
                }    
            }, '-', {    
                iconCls: 'icon-collapse-all',    
                tooltip: '全部收缩',    
                handler: function(){    
                    tree.root.collapse(true);    
                }    
            }]);    
        }    
        tbar.insert(this.index,field);    
                tbar.doLayout();    
    }    
});    
Ext.preg('multifilter', QM.plugin.MutilTreeFilter);  
