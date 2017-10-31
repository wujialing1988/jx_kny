//任务相关性面板
Edo.project.Project.showPredecessorWindow = function(task, predecessorTask, dataProject){
    
    if(typeof(task) == 'string') task = dataProject.getTask(task);
    if(typeof(predecessorTask) == 'string') predecessorTask = dataProject.getTask(predecessorTask);
    var link = dataProject.getPredecessorLink(task, predecessorTask);

    var win = this.predecessorWindow;
    if(!win){
         win = this.predecessorWindow = Edo.create({                            
            title: '任务相关性',type: 'window',shadow: false, verticalGap: 0,width: 380,minHeight: 80,render: document.body,
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
                    type: 'box',width: '100%',height: '100%',//style: 'background-color:white;',
                    children: [
                        {type: 'label', id: 'edo_project_predecessortext'},
                        {
                            type: 'ct',width: '100%', layout: 'horizontal',
                            children: [
                                {   type: 'formitem', label: '类型',width: '100%',labelWidth: 50,
                                    children: [
                                        {id: 'edo_project_predecessortype', type: 'combo',width: '100%',readOnly: true, displayField: 'Name', data: Edo.data.DataGantt.PredecessorLinkType}
                                    ]
                                },
                                {   type: 'formitem', label: '延隔时间',width: '80%',labelWidth: 65,
                                    children: [
                                        {id: 'edo_project_predecessortime', type: 'text',width: '100%', readOnly: true}
                                    ]
                                }
                            ]
                        }
                    ]
                },{
                    type: 'ct',layout: 'horizontal',cls: 'e-dialog-toolbar',width: '100%',verticalAlign: 'bottom',horizontalAlign: 'right',horizontalGap: 10,height: 26,                                    
                    children:[                      
                        {type: 'button',text: '删除',width: 70,height: 22,
                            onclick: function(e){                                
                                win.dataProject.removePredecessorLink(win.task, win.predecessorTask);
                                
                                var ep = new ProjectSchedule(win.dataProject);
                                ep.dataProject = win.dataProject;
                                ep.syncData(win.task, win.task.Start);      
                                
                                
                                win.dataProject.refresh();
                                Edo.managers.PopupManager.removePopup(win);
                            }
                         },
                        {type: 'space',width: '100%'},
                        {type: 'button',text: '确定',width: 70,height: 22,onclick: function(){onOk();}},
                        {
                            type: 'button',text: '取消',width: 70,height: 22,
                            onclick: function(){
                                Edo.managers.PopupManager.removePopup(win);
                            }
                        }
                    ]
                } 
            ]
        });
    }
    win.dataProject = dataProject;
    win.task = task;
    win.predecessorTask = predecessorTask;
    win.link = link;
    
    function onOk(){
    
        win.link.Type = edo_project_predecessortype.selectedItem.ID;
    
        win.dataProject.refresh();
        
        Edo.managers.PopupManager.removePopup(win);
    }
    
    var html = '<div>从： '+predecessorTask.Name+'</div><div>到： '+task.Name+'</div>';
    edo_project_predecessortext.set('text', html);
    
    edo_project_predecessortype.set('selectedItem', {ID: link.Type});
    
    edo_project_predecessortime.set('text', dataProject.getLagFormatTime(link.LinkLag, link.LagFormat));
    
    Edo.managers.PopupManager.createPopup({
        target: win,
        modal: true,
        x: 'center',
        y: 'middle'
    });
    return win;
}