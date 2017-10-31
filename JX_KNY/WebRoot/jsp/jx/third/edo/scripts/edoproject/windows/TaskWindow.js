//任务面板
Edo.project.Project.showTaskWindow = function(type, task, dataProject, callback){   

//Edo.data.DataProject.ConstraintType
    //如果需要一个页面有多个任务面板示例, 请修改windowName和Edo.project.Project.showTaskWindow函数名即可
    var windowName = 'taskWindow';  
    var isInit = !Edo.project.Project[windowName];
    
    function get(name){
        return Edo.getByName(name, Edo.project.Project[windowName])[0];
    }
    
    function validPredecessorLink(task, links){
    
        var links = links || [];
        var uid = task.UID;
        var phash = {};
        var dataProject = win.dataProject;
        var link2 = [];
        for(var i=0,l=links.length; i<l; i++){
            var link = links[i];
            var preTask = dataProject.getTask(link.PredecessorUID);
            //必须填入前置任务
            if(!link.PredecessorUID){
                continue;
            }
            //不能是自己
            if(link.PredecessorUID == uid){
                alert("不能将任务自身作为前置任务");
                return false;
            }
            //不能重复
            if(phash[link.PredecessorUID]){
                alert("不能重复设置前置任务");
                return false;
            }
            //不能有父子关系
            if(Edo.data.DataTree.isAncestor(task, preTask)){
                alert("前置任务不能是子任务");
                return false;
            }
            if(Edo.data.DataTree.isAncestor(preTask, task)){
                alert("前置任务不能是父任务");
                return false;
            }
            
            phash[link.PredecessorUID] = link;
            
            link2.add(link);
        }
        
        //将前置任务数组, 假设性加入任务中, 并验证任务相关性是否出现死循环
        var _links = task.PredecessorLink;
        task.PredecessorLink = link2;
        if(!win.dataProject.validTaskPredecessorLinks(task)){
            alert("任务相关性出错");
            task.PredecessorLink = _links;
            return false;
        }
        task.PredecessorLink = _links;
        
        return true;
    }

    if(!Edo.project.Project[windowName]){
        isInit = true;
        Edo.project.Project[windowName] = Edo.create({
            type: 'window',shadow: false, verticalGap: 0,width: 530,minHeight: 300,render: document.body,
            titlebar:[
                {
                    cls: 'e-titlebar-close',
                    onclick: function(e){
                        Edo.managers.PopupManager.removePopup(win);
                    }
                }
            ],
            onkeydown: function(e){                
                if(e.keyCode == 13){
                    onOk();                        
                }else if(e.keyCode == 27){
                    Edo.managers.PopupManager.removePopup(win);
                }
            },
            children: [
                {
                    type: 'tabbar',name: 'edo_project_tabbar',horizontalGap: 5, selectedIndex: 0,border: [0,0,1,0],
                    children:[
                        {type: 'button',text: Edo.project.Project.basicTab, name: 'basic_tab'},
                        {type: 'button',text: Edo.project.Project.predecessorLinkTab, name: 'predecessorlink_tab'},
                        {type: 'button',text: Edo.project.Project.resourceTab, name: 'resource_tab'},
                        {type: 'button',text: Edo.project.Project.highTab, name: 'high_tab'},
                        {type: 'button',text: Edo.project.Project.noteTab, name: 'note_tab'}
                    ],                    
                    onselectionchange: function(e){                    
                        var a = get('edo_project_taskpanel_view');
                        a.set('selectedIndex', e.index);                            
                    }
                },
                {
                    type: 'box', border: [0,1,1,1],width: '100%',height: '100%', name: 'edo_project_taskpanel',
                    children: [
                        {
                            type: 'formitem',label: Edo.project.Project.tasknameText,labelWidth: 70,width: '100%',layout: 'horizontal',                            
                            children:[
                                {type: 'text',name: 'edo_project_task',width: '100%'},
                                {type: 'space'},
                                {type: 'label',width: 40,style: 'text-align:center;',text: Edo.project.Project.principlColumn+': '},
                                {type: 'multicombo', name: 'edo_project_principal', displayField: 'Name', valueField: 'UID',readOnly: true, width: 150}
                            ]
                        },
                        {
                            type: 'formitem',label: Edo.project.Project.percentCompleteText,labelWidth: 70,width: '100%',layout: 'horizontal',                            
                            children:[                               
                                new Edo.controls.PercentSpinner().set({
                                    name: 'edo_project_percentcomplete', width: 180
                                }),                                                
                                {type: 'space', width: '100%'},
                                {type: 'label',width: 40,style: 'text-align:center;',text: Edo.project.Project.departmentColumn+': '},
                                {type: 'multicombo', name: 'edo_project_department', displayField: 'Name', valueField: 'UID',readOnly: true, width: 150}
                            ]
                        },                   
                        {
                            type: 'ct',name: 'edo_project_taskpanel_view',layout: 'viewstack',width: '100%',height: '100%',
                            children:[        
                                //常规                        
                                {
                                    type: 'ct',width: '100%',height: '100%', verticalGap: 3,
                                    children:[
                                        {
                                            type: 'formitem',label: Edo.project.Project.weightColumn+': ', labelWidth: 70,width: '100%',layout: 'horizontal',
                                            children:[                                                
                                                {type: 'spinner',  name: 'edo_project_weight',minValue: 0,maxValue: 100,incrementValue: 10,alternateIncrementValue: 20, width: 180},
                                                {
                                                    type: 'formitem', visible: false,
                                                    label: Edo.project.Project.priorityText,
                                                    children:[
                                                        {type: 'spinner',  name: 'edo_project_priority',minValue: 0,maxValue: 1000,incrementValue: 20,alternateIncrementValue: 50,minWidth: 107}
                                                    ]
                                                }
                                            ]
                                        },{
                                            type: 'fieldset',padding:0,width: '100%',legend: Edo.project.Project.plan,
                                            children:[
                                                {
                                                    type: 'box',border:0,padding: [0,5,0,5],width: '100%',layout: 'horizontal',
                                                    children:[
                                                        {
                                                            type: 'formitem',label: Edo.project.Project.startText,labelWidth: 63,width: '100%',
                                                            children:[
                                                                {type: 'date',readOnly: true,name: 'edo_project_start',minWidth: 50,width: '100%'
                                                                }
                                                            ]
                                                        },{
                                                            type: 'formitem',label: Edo.project.Project.finishText,labelWidth: 70,width: '100%',
                                                            children:[
                                                                {type: 'date',readOnly: true,minWidth: 50,name: 'edo_project_finish',width: '100%', useEndDate: true                                                                   
                                                                }
                                                            ]
                                                        }
                                                    ]
                                                },
                                                {
                                                    type: 'box',border:0,padding: [0,5,5,5],width: '100%',layout: 'horizontal',
                                                    children:[
                                                        {
                                                            type: 'formitem',label: Edo.project.Project.durationText,labelWidth: 63,width: '100%',
                                                            children:[
                                                                new Edo.controls.DurationSpinner().set({
                                                                    name: 'edo_project_duration',width: 180,minWidth: 40,
                                                                    enable: false
                                                                }),
                                                                {
                                                                    type: 'check',name: 'edo_project_estimated',text: Edo.project.Project.estimatedText, visible: false,
                                                                    oncheckedchange: function(e){
                                                                        var d = get('edo_project_duration');
                                                                        var v = d.value;
                                                                        v = {
                                                                            Duration: v.Duration,
                                                                            DurationFormat: v.DurationFormat,
                                                                            Estimated: e.checked ? 1 : 0
                                                                        }
                                                                        d.set('value', v);
                                                                    }
                                                                }
                                                            ]
                                                        }
                                                    ]
                                                }
                                            ]
                                        },{
                                            type: 'fieldset',padding:0,width: '100%',legend: Edo.project.Project.actual,
                                            children: [
                                                {
                                                    type: 'box',border:0,padding: [0,5,0,5],width: '100%',layout: 'horizontal',
                                                    children: [
                                                        {
                                                            type: 'formitem',label: Edo.project.Project.startText,labelWidth: 63,width: '100%',
                                                            children:[
                                                                {type: 'date',readOnly: true,name: 'edo_project_actualstart',minWidth: 50,width: '100%', required: false}
                                                            ]
                                                        },
                                                        {
                                                            type: 'formitem',label: Edo.project.Project.finishText,labelWidth: 70,width: '100%',
                                                            children:[
                                                                {type: 'date',readOnly: true,name: 'edo_project_actualfinish',minWidth: 50,width: '100%', required: false, useEndDate: true}
                                                            ]
                                                        }
                                                    ]
                                                },
                                                {
                                                    type: 'box',border:0,padding: [0,5,5,5],width: '100%',layout: 'horizontal',
                                                    children: [
                                                        {
                                                            type: 'formitem',label: '实际工期:',labelWidth: 63,width: '100%',
                                                            children:[
                                                                {type: 'spinner', name: 'edo_project_actualduration',minWidth: 50,width: 180, minValue: 0, maxValue: 100000}
                                                            ]
                                                        }
                                                    ]
                                                }
                                            ]
                                        }
                                    ]
                                },    
                                //前置任务                          
                                {
                                    type: 'fieldset',
                                    padding:0,
                                    legend: Edo.project.Project.predecessorLinkText,
                                    width: '100%',
                                    height: '100%',
                                    verticalGap: 2,
                                    //enableCollapse: true,
                                    children: [
                                        {
                                            type: 'ct',
                                            layout: 'horizontal',
                                            width: '100%',
                                            //cls: 'e-toolbar e-toolbar-over',
                                            children:[
                                                {
                                                    type: 'space'                                                        
                                                },
                                                {
                                                    type: 'button',
                                                    icon: 'e-icon-add',
                                                    onclick: function(e){
                                                        
                                                        var r = win.task;
                                                        
                                                        var index = win.dataProject.Tasks.indexOf(r) - 1;
                                                        
                                                        if(index<0) index = 0;
                                                        var pre = win.dataProject.Tasks.getAt(index);
                                                        
                                                        var d = edo_project_predecessorlink.data;
                                                        
                                                        d.insert(0, {
                                                            //PredecessorUID: pre.UID,
                                                            //TaskUID: r.UID,
                                                            Type: 1,    //默认是FS
                                                            LinkLag: 0,
                                                            LagFormat: 7//d
                                                        });
                                                        
                                                        edo_project_predecessorlink.beginEdit(0, 0);
                                                    }
                                                },{
                                                    type: 'button',
                                                    icon: 'e-icon-delete',
                                                    onclick: function(e){
                                                        var d = edo_project_predecessorlink.data;
                                                        var r = edo_project_predecessorlink.getSelected();
                                                        if(r){
                                                            d.remove(r);
                                                        }                                                            
                                                    }
                                                }
                                            ]
                                        },
                                        {
                                            name: 'edo_project_predecessorlink',
                                            type: 'table',
                                            style: 'border-left:0;border-right:0;border-bottom:0;',
                                            width: '100%',
                                            height: '100%',
                                            minHeight: 50,
                                            editAction: 'click',
                                            autoExpandColumn: 'name',
                                            
                                            onbeforesubmitedit: function(e){
                                                if(e.field == 'PredecessorUID'){     
                                                    var _PredecessorUID = e.record.PredecessorUID;
                                                    e.record.PredecessorUID = e.value;
                                                                                                        
                                                    var ps = this.data.getSource();                                                    
                                                    var valid = validPredecessorLink(win.task, ps);
                                                    e.record.PredecessorUID = _PredecessorUID;                                     
                                                    if(!valid) {
                                                        return false;
                                                    }
                                                }
                                            },
                                            columns:[
                                                {headerText: Edo.project.Project.idText,width: 60,dataIndex: 'PredecessorUID',
                                                    editor:{
                                                        type: 'text',
                                                        onbeforeeditstart: function(e){
                                                        
                                                            var task = win.dataProject.getTask(e.data);
                                                            if(task){
                                                                e.data = task.ID;
                                                            }
                                                        },
                                                        oneditcomplete: function(e){            
                                                       
                                                            if(!e.data || e.data == e.editdata) {                                                    
                                                                e.data = e.editdata;
                                                                return ;
                                                            }                                                                                                                                
                                                            var id = parseInt(e.data);
                                                            
                                                            var task = win.dataProject.getTaskByID(id);
                                                            if(!task){
                                                                alert(Edo.project.Project.predecessorLinkIDInvalid);
                                                                e.data = e.editdata;
                                                            }else{
                                                                e.data = task.UID;                                                                
                                                            }                                                                
                                                        }
                                                    },
                                                    renderer: function(v, r){
                                                        var task = win.dataProject.getTask(v);
                                                        if(task){
                                                            return task.ID;
                                                        }else{
                                                            return '';
                                                        }
                                                    }
                                                },
                                                {id: 'name', headerText: Edo.project.Project.tasknameText,width: 100,dataIndex: 'PredecessorUID',
                                                    editor:{
                                                        //name: 'edo_',
                                                        type: 'combo',
                                                        //readOnly: true,
                                                        displayField: 'Name',                                                            
                                                        valueField: 'UID',
                                                        popupWidth: '100%',
                                                        popupHeight: 120,
                                                        tableConfig:{
                                                            type: 'supergrid',
                                                            autoColumns: false,
                                                            autoExpandColumn: 'name',
                                                            horizontalLine: true,
                                                            verticalLine: true,
                                                            headerVisible: true,
                                                            columns:[
                                                                {headerText: 'ID', dataIndex: 'ID', width: 35,headerAlign:'center', align: 'center'},
                                                                {id: 'name', headerText: Edo.project.Project.tasknameText, dataIndex: 'Name',width: 250}
                                                            ]
                                                        },
                                                        onbeforeeditstart: function(e){
                                                            this.set('data', win.dataProject.Tasks.source);                                                            
                                                        }
                                                    },
                                                    renderer: function(v, r){
                                                        var task = win.dataProject.getTask(v);
                                                        if(task){
                                                            return task.Name;
                                                        }else{
                                                            return '';
                                                        }
                                                    }
                                                },
                                                {headerText: Edo.project.Project.predecessorLinkTypeText,width: 100,dataIndex: 'Type',
                                                    editor:{
                                                        type: 'combo',
                                                        readOnly: true,
                                                        displayField: 'Name',
                                                        valueField: 'ID',
                                                        popupWidth: '100%',
                                                        data: Edo.data.DataGantt.PredecessorLinkType
                                                    },
                                                    renderer: function(v, r){
                                                        return Edo.data.DataGantt.PredecessorLinkType[v].Name;                                                            
                                                    }
                                                },
                                                {headerText: Edo.project.Project.linkLagText, width: 80,dataIndex: '*', 
                                                    editor: {
                                                        type: 'durationspinner',
                                                        mustInt: false,
                                                        onbeforeeditstart: function(e){
                                                            this.durationFormat = win.dataProject.DurationFormat;
                                                            var record = this.owner.getEditRecord();
                                                            this.setValue({
                                                                Duration: record.LinkLag/600,
                                                                DurationFormat: record.LagFormat
                                                            });
                                                        },
                                                        oneditcomplete: function(e){
                                                            e.data.Duration = parseInt(e.data.Duration);                                                            
                                                            e.data.LinkLag = e.data.Duration * 600;
                                                            e.data.LagFormat = e.data.DurationFormat;
                                                        }
                                                    },
                                                    renderer: function(v, r){
                                                        return  win.dataProject.getLagFormatTime(r.LinkLag, r.LagFormat);
                                                    }
                                                }
                                            ],
                                            data: new Edo.data.DataTable()
                                        }
                                    ]
                                },
                                //资源
                                {
                                    type: 'fieldset',padding:0,legend: Edo.project.Project.resourceText,width: '100%',height: '100%',verticalGap: 2,
                                    id: 'edoproject_resource_panel',
                                    children: [
                                        {
                                            type: 'ct',layout: 'horizontal',width: '100%',
                                            children:[
                                                {type: 'space'},
                                                {
                                                    type: 'button',icon: 'e-icon-add',
                                                    onclick: function(e){                                                        
                                                        var rs = win.dataProject.Resources;       
                                                                                                         
                                                        if(!rs || rs.isEmpty()){
                                                        
                                                            Edo.MessageBox.alert(Edo.project.Project.alertText, Edo.project.Project.resourceText2);
                                                            return;
                                                        }
                                                        
                                                        //var resource = win.dataProject.Resources.getAt(0);
                                                        var d = edo_project_assignment.data;
                                                        d.insert(0, {
                                                            UID: UUID(),
                                                            Units: 1
                                                        }); 
                                                        
                                                        edo_project_assignment.beginEdit(0, 0);
                                                    }
                                                },{
                                                    type: 'button',icon: 'e-icon-delete',
                                                    onclick: function(e){
                                                        var d = edo_project_assignment.data;
                                                        var r = edo_project_assignment.getSelected();
                                                        if(r){
                                                            d.remove(r);
                                                        }                                                            
                                                    }
                                                }
                                            ]
                                        },
                                        {
                                            name: 'edo_project_assignment',type: 'table',style: 'border-left:0;border-right:0;border-bottom:0;',width: '100%',height: '100%',minHeight: 50,editAction: 'click',autoExpandColumn: 'name',
                                            columns:[
                                                {id: 'name',headerText: Edo.project.Project.resourceNameText,width: 60,dataIndex: 'ResourceUID',
                                                    editor:{
                                                        type: 'combo',readOnly: true,displayField: 'Name',valueField: 'UID',popupWidth: '100%',                                                        
                                                        onbeforeeditstart: function(e){
                                                        
                                                            this.set('data', win.dataProject.Resources);
                                                        }
                                                    },
                                                    renderer: function(v, r){                  
                                                        var resource = win.dataProject.getResource(v);                                  
                                                        if(resource) return resource.Name;
                                                        else return '';
                                                    }
                                                },                                                                                                    
                                                {headerText: Edo.project.Project.resourceUnitsText,width: 120,dataIndex: 'Units',                                                        
                                                    renderer: function(v, r){
                                                        if(v === undefined || v === null) v = 1;
                                                        return (v * 100) +'%';
                                                        //return v + '%';
                                                    },
                                                    editor: {type: 'percentspinner', incrementValue: 1,
                                                        onbeforeeditstart: function(e){
                                                            e.data = e.data * 100;
                                                        },
                                                        oneditcomplete: function(e){                                                        
                                                            e.data = e.data / 100;
                                                        }
                                                    }
                                                },
                                                {headerText: Edo.project.Project.resourceTypeText, width: 120,dataIndex: 'ResourceUID',                                                        
                                                    renderer: function(v, r){
                                                        var resource = win.dataProject.getResource(v);
                                                        if(resource){
                                                            return Edo.data.DataProject.ResourceType[resource.Type].Name;
                                                        }else{
                                                            return  '';
                                                        }
                                                    }
                                                }
                                            ],
                                            data: new Edo.data.DataTable()
                                        }
                                    ]
                                },
                                //高级
                                {
                                    type: 'fieldset',padding:0,legend: Edo.project.Project.constraintText,width: '100%',height: '100%',
                                    children: [
                                        {
                                            type: 'box',border: 0,width: '100%',height: '100%',
                                            children:[
                                            {
                                                type: 'ct',width: '100%',layout: 'horizontal',horizontalGap: 0,
                                                children:[                                                    
                                                    {   type: 'formitem',label: Edo.project.Project.constraintTypeText,labelWidth: 65,width: '100%',
                                                        children: [
                                                            {type: 'combo',data: Edo.data.DataProject.ConstraintType,displayField: 'Name',readOnly: true,name: 'edo_project_constrainttype',width: '90%',
                                                                onselectionchange: function(e){
                                                                    var sel = this.selectedItem;
                                                                    if(sel){                                                                        
                                                                        var date = win.dataProject.getConstraintDate(sel.ID, win.task);
                                                                        get("edo_project_constraintdate").set('date', date);
                                                                    }
                                                                }
                                                            }
                                                        ]
                                                    },
                                                    {
                                                        type: 'formitem',label: Edo.project.Project.constraintDateText,labelWidth: 65,width: '100%',
                                                        children: [
                                                            {name: 'edo_project_constraintdate',type: 'date',width: '90%'}
                                                        ]
                                                    }
                                                ]
                                            },{
                                                type: 'formitem',label: Edo.project.Project.taskTypeText,labelWidth: 65,width: '50%',                                                
                                                children:[
                                                    {name: 'edo_project_type',width: '90%',type: 'combo',displayField: 'Name',readOnly: true,data: Edo.data.DataProject.TaskType}
                                                ]
                                            },
                                            {name: 'edo_project_milestone',type: 'check',text: Edo.project.Project.milestoneText}
                                        ]
                                        }
                                    ]
                                },
                                //备注
                                {
                                    type: 'fieldset',padding:0,legend: Edo.project.Project.noteText,width: '100%',height: '100%',
                                    children: [
                                        {name: 'edo_project_notes',type: 'textarea',width: '100%',height: '100%',minHeight: 50}
                                    ]                                                
                                } 
                            ]
                        }
                    ]
                },
                {
                    type: 'ct',layout: 'horizontal',cls: 'e-dialog-toolbar',width: '100%',verticalAlign: 'bottom',horizontalAlign: 'right',horizontalGap: 10,height: 26,                                    
                    children:[                      
                        /*{type: 'button',text: Edo.project.Project.helpText,width: 70 },*/
                        {type: 'space',width: '100%'},
                        {
                            type: 'button',text: Edo.project.Project.okText,width: 70,height:22,
                            onclick: function(){
                                onOk(type);
                            }
                        },
                        {
                            type: 'button',text: Edo.project.Project.cancelText,width: 70,height:22,
                            onclick: function(){
                                Edo.managers.PopupManager.removePopup(win);
                            }
                        }
                    ]
                }   
            ]
        });        
    }
    
    function initEvents(){        
        edo_project_start.on('datechange',function(e){               
            if(win.CanChange){                
                win.CanChange = false;
                
                var d = get('edo_project_duration'), sd = get('edo_project_start'), ed = get('edo_project_finish');
                //确保选择的开始日期, 是一天的最开始
                var sdate = sd.date;
                
                if(sdate){
                    var finish = win.dataProject.getFinishByCalendar(sdate, d.value.Duration);
                    ed.set('date', finish);
                    
                    //改动开始日期, 设置"不得早于...开始"显示
                    var sindex = edo_project_constrainttype.data.findIndex({ID: 4});
                    edo_project_constrainttype.set('selectedIndex', sindex);
                    edo_project_constraintdate.set('date', sdate.clone());
                }
                
                win.CanChange = true;
            }            
        });
        edo_project_finish.on('datechange', function(e){
            if(win.CanChange){
                var d = get('edo_project_duration'), sd = get('edo_project_start'), ed = get('edo_project_finish');                                            
                win.CanChange = false;
                
                //当完成日期改变的时候, 修改工期, 开始日期不变
                var v = d.value;
                
                //确保选择的完成日期, 是一天的最末
                var edate = ed.date;                
                if(sd.date){
                    var duration = win.dataProject.getDuratonByCalendar(sd.date, edate);
                    if(v.Duration != duration){
                        v.Duration = duration;
                        d.set('value', v);
                    }
                    
                    //改动开始日期, 设置"不得早于...开始"显示
                    var sindex = edo_project_constrainttype.data.findIndex({ID: 4});
                    edo_project_constrainttype.set('selectedIndex', sindex);
                    edo_project_constraintdate.set('date', sd.date.clone());
                }
                win.CanChange = true;
            }                                                                        
        });
        edo_project_duration.on('valuechange', function(e){
            get('edo_project_estimated').set('checked', !!e.value.Estimated);
            
            //当工期改变的时候, 修改完成日期, 开始日期不变
            if(win.CanChange){
                var d = get('edo_project_duration'), sd = get('edo_project_start'), ed = get('edo_project_finish');                                            
                win.CanChange = false;
                if(sd.date){
                    var finish = win.dataProject.getFinishByCalendar(sd.date, d.value.Duration);
                    ed.set('date', finish);
                }
                win.CanChange = true;
            }
        });
        ////////////////////////////
        //实际工期改变
        edo_project_actualduration.on('valuechange', function(e){
            if(win.CanChange){
            
                win.CanChange = false;
                
                var start = edo_project_actualstart.date;
                if(start){
                    var finish = win.dataProject.getFinishByCalendar(start, edo_project_actualduration.value * 8);
                    edo_project_actualfinish.set('date', finish);
                }
                
                win.CanChange = true;
            }            
        });    
        //实际开始日期改变
        edo_project_actualstart.on('datechange', function(e){
            if(win.CanChange){
            
                win.CanChange = false;
                var start = edo_project_actualstart.date;
                var duration = edo_project_actualduration.value * 8;
                if(start){
                    var finish = win.dataProject.getFinishByCalendar(edo_project_actualstart.date, duration);
                    edo_project_actualfinish.set('date', finish);                
                }
                
                win.CanChange = true;
            }            
        });
        
        //实际完成日期改变
        edo_project_actualfinish.on('datechange', function(e){
            if(win.CanChange){
            
                win.CanChange = false;
                //
                var start = edo_project_actualstart.date;
                var finish = edo_project_actualfinish.date;                
                if(start && finish){
                    if(finish < start){
                        finish = win.dataProject.getFinishByCalendar(start, 0);
                        edo_project_actualfinish.set('date', finish);
                    }else{
                        var duration = win.dataProject.getDuratonByCalendar(start, finish);
                        edo_project_actualduration.set('value', parseInt(duration / 8));
                    }
                }   
                
                win.CanChange = true;
            }            
        });  
        
        //部门
        edo_project_department.on('selectionchange', function(e){
            if(win.CanChange){
                win.CanChange = false;
                var departmentUID = edo_project_department.getValue();
                
                var puid = edo_project_principal.getValue();
                edo_project_principal.set('data', win.dataProject.getPrincipalsByDepartment(departmentUID));
                edo_project_principal.setValue(puid);
                
                win.CanChange = true;
            }
        });
    }
    
    var win = Edo.project.Project[windowName];
    win.action = type;
    win.dataProject = dataProject;        
    
    win.CanChange = false; //实际日期调整开关变量
    
    function onOk(){    
        if(win.action == 'view'){        
            Edo.managers.PopupManager.removePopup(win);
            return;
        }
        row = win.action == 'new' ? dataProject.createTask() : win.task;
        var _start = row.Start;
        var _row = Edo.clone(row);      //保存任务的旧有属性                
        
        row = Edo.apply(row, {
            
            Name: edo_project_task.text,
            
            Duration: edo_project_duration.value.Duration,
            DurationFormat: edo_project_duration.value.DurationFormat,
            Estimated: edo_project_estimated.checked ? 1 : 0,
            
            PercentComplete: edo_project_percentcomplete.value,
            Priority: edo_project_priority.value,
            
            Start: edo_project_start.date,
            Finish: edo_project_finish.date,
            //备注
            Notes: edo_project_notes.text,
            //前置任务
            PredecessorLink: edo_project_predecessorlink.data.source.clone(),
            //资源分配
            Assignments: edo_project_assignment.data.source.clone(),
            //限制类型
            ConstraintType: edo_project_constrainttype.selectedItem.ID,
            ConstraintDate: edo_project_constraintdate.date,
            //里程碑
            Milestone: edo_project_milestone.checked ? 1: 0,
            //任务类型
            Type: edo_project_type.selectedItem.ID,
            CreateDate: new Date(),
            
            //权重
            Weight: edo_project_weight.value,
            
            //部门, 负责人
            Department: edo_project_department.getValue(),
            Principal: edo_project_principal.getValue(),
            
            //实际日期
            ActualDuration: edo_project_actualduration.value * 8,
            ActualStart: edo_project_actualstart.date,
            ActualFinish: edo_project_actualfinish.date
        });
        
        //
        var PredecessorLink = [];
        row.PredecessorLink.each(function(o){
            o.TaskUID = row.UID;
            if(o.PredecessorUID){
                PredecessorLink.add(o);
            }
        });
        row.PredecessorLink = PredecessorLink;
        
        var Assignments = [];
        row.Assignments.each(function(o){
            o.TaskUID = row.UID;
            if(o.ResourceUID){
                Assignments.add(o);
            }
        });
        row.Assignments = Assignments;
        
        if(!row.Name){
            alert(Edo.project.Project.notblankTaskName);
            return;
        }                
        
        var valid = validPredecessorLink(win.task, row.PredecessorLink);
        if(valid == false){            
            var predecessorlink_tab = get("predecessorlink_tab");
            predecessorlink_tab.parent.set('selectedItem', predecessorlink_tab);
            return;
        }        
        
        if(callback) {
            var ret = callback({
                action: win.action,
                dataProject: win.dataProject,
                selectedTask: win.selected,
                task: row
            });
            if(ret === false) {                
                Edo.apply(row, _row);   //还原任务的属性
                return;
            }
        }
        
        Edo.managers.PopupManager.removePopup(win);
    }
    
    //使用name来代替原来的id构建组件, 可以创建多个任务面板, 而不会有重复ID的冲突.
    var edo_project_taskpanel_view = get('edo_project_taskpanel_view');
    
    var edo_project_task = get('edo_project_task');
    var edo_project_duration = get('edo_project_duration');
    var edo_project_estimated = get('edo_project_estimated');
    var edo_project_percentcomplete = get('edo_project_percentcomplete');
    var edo_project_priority = get('edo_project_priority');
    var edo_project_start = get('edo_project_start');
    var edo_project_finish = get('edo_project_finish');
    var edo_project_predecessorlink = get('edo_project_predecessorlink');
    var edo_project_assignment = get('edo_project_assignment');
    var edo_project_constrainttype = get('edo_project_constrainttype');
    var edo_project_constraintdate = get('edo_project_constraintdate');
    var edo_project_type = get('edo_project_type');
    var edo_project_milestone = get('edo_project_milestone');
    var edo_project_notes = get('edo_project_notes');
    var edo_project_tabbar = get('edo_project_tabbar');
    
    var edo_project_actualduration = get('edo_project_actualduration');
    var edo_project_actualstart = get('edo_project_actualstart');
    var edo_project_actualfinish = get('edo_project_actualfinish');
    var edo_project_principal = get('edo_project_principal');
    var edo_project_department = get('edo_project_department');
    var edo_project_weight = get('edo_project_weight');
    
    if(isInit) initEvents();
    
    win.selected = task;    //传递进来的task任务对象
    
    get('edo_project_taskpanel').set('enable', true);
    //置标题和图标
    
    var title, icon;
    if(type == 'new'){
        edo_project_tabbar.set('selectedIndex', 0);
    
        title = Edo.project.Project.newTaskText;
        icon = 'e-icon-new';
        
        task = dataProject.createTask();
        //////..添加要初始化的属性
        
    }else if(type=='edit'){
        title = Edo.project.Project.editTaskText;
        icon = 'e-icon-edit';
    }else if(type == 'view'){        
        get('edo_project_taskpanel').set('enable', false);
        title = '查看任务（只读）';
    }
    if(task.enableEdit === false){
        get('edo_project_taskpanel').set('enable', false);
    }
    //1)面板    
    win.task = task;
    
    edo_project_task.set('text', task.Name);
            
    edo_project_estimated.set('checked', !!task.Estimated);                        
    edo_project_percentcomplete.set('value', task.PercentComplete);
    edo_project_priority.set('value', task.Priority);
    edo_project_start.set('date', task.Start);
    edo_project_finish.set('date', task.Finish);
    
    //
    edo_project_duration.set('durationFormat', dataProject.DurationFormat);    
    
    edo_project_duration.set('value', {
        Duration: task.Duration,
        DurationFormat: task.DurationFormat,
        Estimated: task.Estimated
    });
    
    //前置任务
    edo_project_predecessorlink.data.load(Edo.clone(task.PredecessorLink));
    //资源
    
    edo_project_assignment.data.load(Edo.clone(task.Assignments));
    
    //高级
    
    var sindex = edo_project_constrainttype.data.findIndex({ID: task.ConstraintType});
    edo_project_constrainttype.set('selectedIndex', sindex);
    
    edo_project_constraintdate.set('required', false);
    edo_project_constraintdate.set('date', task.ConstraintDate);
    edo_project_constraintdate.set('required', true);
    
    var tindex = edo_project_type.data.findIndex({ID: task.Type});
    edo_project_type.set('selectedIndex', tindex);
    edo_project_milestone.set('checked', !!task.Milestone);
    //备注
    edo_project_notes.set('text', task.Notes || '');
    
    //
    var enableEdit = !task.Summary;
    if(!dataProject.enableSyncDate){
        enableEdit = true;
    }
    edo_project_start.set('enable', enableEdit);
    edo_project_finish.set('enable', enableEdit);
    edo_project_estimated.set('enable', true);
    edo_project_duration.set('enable', enableEdit);
    
    //部门, 负责人, 实际开始/完成/工期, 权重
    edo_project_department.set('multiSelect', dataProject.multiSelectDepartment);
    edo_project_department.set('data', dataProject.Departments);
    edo_project_department.setValue(task.Department);
    edo_project_principal.set('data', dataProject.getPrincipalsByDepartment(task.Department));
    edo_project_principal.setValue(task.Principal);
    
    edo_project_actualduration.set('value', task.ActualDuration/8);
    edo_project_actualstart.set('date', task.ActualStart);
    edo_project_actualfinish.set('date', task.ActualFinish);
    
    edo_project_weight.set('value', task.Weight);
    
    edo_project_actualfinish.set('enable', enableEdit);
    edo_project_actualstart.set('enable', enableEdit);    
    edo_project_actualduration.set('enable', enableEdit);
        
    win.set({
        title: title,
        titleIcon: icon
    });
        
    Edo.managers.PopupManager.createPopup({
        target: win,
        modal: true,
        x: 'center',
        y: 'middle'
    });   
    //Edo.managers.ResizeManager.reg({target: win});
    
    win.CanChange = true;
    
    return win;
}
//TaskWindow
Edo.apply(Edo.project.Project,  {   
    basicTab: '常规',
    predecessorLinkTab: '前置任务',
    resourceTab: '资源',
    highTab: '高级',
    noteTab: '备注',
    
    tasknameText: '任务名称:',
    durationText: '工期:',
    estimatedText: '估计',
    percentCompleteText: '完成百分比:',
    priorityText: '优先级:',
    dateText: '日期:',
    
    startText: '开始日期:',
    finishText: '截止日期:',
    predecessorLinkText: '前置任务:',
    idText: '标识号',
    predecessorLinkTypeText: '类型',
    linkLagText: '延隔时间',
    
    resourceText: '资源:',
    alertText: '提示',
    resourceText2: '没有资源可分配',
    resourceNameText: '资源名称',
    resourceUnitsText: '单位',
    resourceTypeText: '资源类型',
    
    constraintText: '任务限制:',
    constraintTypeText: '限制类型:',
    constraintDateText: '限制日期:',
    taskTypeText: '任务类型:',
    milestoneText: '标记为里程碑',
    noteText: '备注:',
    helpText: '帮助',
    okText: '确定',
    cancelText: '取消',
    
    newTaskText: '创建任务',
    editTaskText: '编辑任务',
    notblankTaskName: "必须填写任务名称",
    predecessorLinkIDInvalid: '前置任务ID号无效'
});
