//项目面板
Edo.project.Project.showProjectSelectWindow = function(callback, loadFn, delFn){
    var win = this.projectSelectWindow;
    if(!win){
         win = this.projectSelectWindow = Edo.create({
            title: '选择项目', type: 'window',shadow: false, verticalGap: 0,width: 450,minHeight: 230,render: document.body,            
            titlebar:[
                {
                    cls: 'e-titlebar-close',
                    onclick: function(e){
                        Edo.managers.PopupManager.removePopup(win);
                    }
                }
            ],
            children: [
                {
                    type: 'box', border: 0, padding: [0,0,0,5], width: '100%', layout:'horizontal', verticalAlign: 'middle',
                    children: [                        
                        {type: 'label', text: '名称：'},
                        {type: 'text', width: 130, id: 'edo_project_selectproject_name'},
                        {type: 'button', text: '项目查询',
                            onclick: function(e){
                                pager.change(0);
                            }    
                        },
                        {type: 'split'},
                        {type: 'button', text: '高级查询', enableToggle: true, 
                            ontoggle: function(e){
                                edo_project_selectproject_adsearch.set('visible', this.pressed);
                            }
                        },
                        {
                            type: 'space',width: '100%'
                        },
                        {
                            type: 'button', text: '删除项目', width: 70,
                            onclick: function(){
                                var sel = projectlist.getSelected();
                                if(sel){
                                    if(confirm("确定删除项目 \""+sel.Name+"\"?")){
                                        if(delFn) delFn(sel.UID
                                        , function(){
                                            pager.change();
                                        });                                        
                                    }
                                    
                                }else{
                                    alert("请先选中要删除的项目");
                                }                                
                            }
                        }
                    ]
                },
                {
                    id: 'edo_project_selectproject_adsearch',type: 'fieldset', width: '100%', legend: '日期范围', visible: false, layout: 'horizontal',horizontalAlign: 'center',
                    children: [
                        {type: 'label', text: '开始日期'},
                        {id: 'edo_project_selectproject_startdate',type: 'date', valueFormat: true},
                        {type: 'label', text: '完成日期'},
                        {id: 'edo_project_selectproject_finishdate',type: 'date', valueFormat: true}
                    ]
                },
                {
                    name: 'projectlist',type: 'table',width: '100%',height: '100%',editAction: 'mousedown',showHeader: true,rowSelectMode: 'single',
                    autoExpandColumn: 'name',                
                    //autoColumns: true,
                    columns:[
                        Edo.lists.Table.createSingleColumn(),
                        {
                            id:'name', headerText: '项目名称', dataIndex: 'Name'
                        },
                        {                            
                            headerText: '创建日期',dataIndex: 'CreationDate',
                            renderer: function(v){
                                return v ? v.format('Y-m-d H:i') : '-';
                            }
                        }    
                    ],
                    data: new Edo.data.DataTable()
                },
                {
                    type: 'ct',width: '100%',height: 26,layout: 'horizontal',verticalAlign: 'bottom',horizontalAlign: 'left',cls: 'e-dialog-toolbar',
                    children:[
                        {
                            name: 'pager',type: 'pagingbar',width: '100%',infoVisible: false,
                            onpaging: function(e){
                            
                                if(loadFn) {
                                    var datelimit = edo_project_selectproject_adsearch.visible;
                                    loadFn({
                                        index: this.index,
                                        size: this.size,
                                        name: edo_project_selectproject_name.text,
                                        startDate: datelimit ? edo_project_selectproject_startdate.getValue() : null,
                                        finishDate: datelimit ? edo_project_selectproject_finishdate.getValue() : null
                                    }, function(data){
                                        pager.refreshView(data.result.length);
                                        projectlist.data.load(data.result);
                                    }, function(code){
                                        alert("项目加载失败, 错误编码:" + code);
                                    });
                                }
                            }
                        },{
                            type: 'button', text: '确定', width: 70,height: 22,
                            onclick: onOk
                        },{
                            type: 'button', text: '取消', width: 70,height: 22,
                            onclick: function(e){
                                Edo.managers.PopupManager.removePopup(win);
                            }
                        }
                    ]
                }            
            ]
        });            
    }    
    function onOk(){        
        var sel = projectlist.getSelected();
        if(callback && sel) callback(sel);
        Edo.managers.PopupManager.removePopup(win);
    }    
        
    var pager = Edo.getByName('pager', win)[0];
    var projectlist = Edo.getByName('projectlist', win)[0];
    pager.change();
    
    Edo.managers.PopupManager.createPopup({
        target: win,
        //modal: true,
        x: 'center',
        y: 'middle'
    });
    return win;
}