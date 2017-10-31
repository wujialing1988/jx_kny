//资源面板
Edo.project.Project.showResourceWindow = function(dataProject, callback){
    var win = this.resourceWindow;
    if(!win){        
         win = this.resourceWindow = Edo.create({                            
            title: '项目资源', type: 'window',shadow: false, verticalGap: 0,width: 450,minHeight: 230,render: document.body,            
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
                    name: 'list',type: 'table',width: '100%',height: '100%',editAction: 'mousedown',showHeader: true,rowSelectMode: 'multi',
                    autoExpandColumn: 'name',
                    //autoColumns: true,
                    columns:[
                        Edo.lists.Table.createMultiColumn(),
                        {
                            id:'name', headerText: '资源名称',dataIndex: 'Name', editor: {type: 'text'}
                        },
                        {                            
                            headerText: '资源类型',dataIndex: 'Type',
                            renderer: function(v, r){                                
                                return Edo.data.DataProject.ResourceType[v].Name;
                            },
                            editor:{
                                type: 'combo', valueField: 'ID', displayField: 'Name',
                                data: Edo.data.DataProject.ResourceType
                            }                            
                        },
                        {
                            headerText: '最大单位',dataIndex: 'MaxUnits',
                            renderer: function(v, r){
                                return (v * 100) +'%';
                            }
//                            ,
//                            editor: {
//                                type: 'percentspinner', incrementValue: 1,
//                                oneditstart: function(e){
//                                    e.data = e.data * 100;
//                                },
//                                oneditcomplete: function(e){                                                        
//                                    e.data = e.data / 100;
//                                }
//                            }
                        }
//                        ,
//                        {
//                            headerText: '资源描述',dataIndex: 'Description', editor: {type: 'text'}
//                        }
                    ],
                    data: new Edo.data.DataTable()
                },
                {
                    type: 'ct',width: '100%',height: 26,layout: 'horizontal',verticalAlign: 'bottom',horizontalAlign: 'right',cls: 'e-dialog-toolbar',
                    children:[
                        {
                            type: 'button',
                            icon: 'e-icon-add',
                            onclick: function(e){
                                
                                var d = list.data;
                                
                                d.insert(0, {
                                    UID: UUID(),
                                    Type: 1,
                                    MaxUnits: 1
                                });
                                
                                //list.beginEdit(0, 1);
                            }
                        },{
                            type: 'button',
                            icon: 'e-icon-delete',
                            onclick: function(e){
                                var d = list.data;
                                var rs = list.getSelecteds();                                
                                d.removeRange(rs);                                
                            }
                        },
                        {type:'space', width: '100%'},
                        {
                            type: 'button', text: '确定',width: 70,height: 22,
                            onclick: onOk
                        },{
                            type: 'button', text: '取消',width: 70,height: 22,
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
        win.dataProject.Resources.load(list.data.source);
        
        win.dataProject.refresh(true);
        if(callback) callback(sel);
        Edo.managers.PopupManager.removePopup(win);
    }
    
    win.dataProject = dataProject;
    
    var list = Edo.getByName('list', win)[0];    
    
    list.data.load(Edo.clone(dataProject.Resources.source));
    
    Edo.managers.PopupManager.createPopup({
        target: win,
        modal: true,
        x: 'center',
        y: 'middle'
    });   
    return win;
}