//项目面板
Edo.project.Project.showProjectWindow = function(dataProject){
    var win = this.projectWindow;
    if(!win){
         win = this.projectWindow = Edo.create({                            
            type: 'window',shadow: false, verticalGap: 0,width: 450,minHeight: 230,render: document.body,
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
                        {
                            type: 'formitem',width: '100%',label: '项目名称',labelWidth: 60,
                            children: [
                                {id: 'edo_project_Name',type: 'text',width: '100%'}
                            ]
                        },{
                            type: 'ct',width: '100%',layout: 'horizontal',
                            children:[
                                {
                                    type: 'formitem',width: '100%',label: '开始日期',labelWidth: 60,readOnly: true,
                                    children: [{id: 'edo_project_StartDate',type: 'date',width: '100%'}]
                                },{
                                    type: 'formitem',width: '100%',label: '完成日期',labelAlign: 'right',labelWidth: 90,readOnly: true,
                                    children: [{id: 'edo_project_FinishDate',type: 'date',width: '100%'}]
                                }
                            ]
                        },{
                            type: 'ct',
                            width: '100%',
                            layout: 'horizontal',
                            children:[
                                {   type: 'formitem',width: '100%',label: '创建日期',labelWidth: 60,
                                    children: [{id: 'edo_project_CreationDate',type: 'text',enable: false,width: '100%'}]
                                },{
                                    type: 'formitem',width: '100%',label: '上次保存日期',labelAlign: 'right',labelWidth: 90,
                                    children: [{id: 'edo_project_LastSaved',type: 'text',enable: false,width: '100%'}]
                                }
                            ]
                        },{
                            type: 'fieldset',                            
                            width: '100%',
                            legend: '项目日历&时间',
                            children:[
                                {
                                    type: 'box',border:0,padding: [5,0,5,10],width: '100%',
                                    children: [
                                        {   type: 'ct',width: '100%',layout: 'horizontal',
                                            children:[
                                                {
                                                    type: 'formitem',width: '100%',label: '每周开始于',labelWidth: 85,
                                                    children: [{id: 'edo_project_WeekStartDay',type: 'combo',width: '100%',readOnly: true,
                                                        data: [
                                                            {text: '星期日', value: 0},
                                                            {text: '星期一', value: 1},
                                                            {text: '星期二', value: 2},
                                                            {text: '星期三', value: 3},
                                                            {text: '星期四', value: 4},
                                                            {text: '星期五', value: 5},
                                                            {text: '星期六', value: 6}
                                                        ]
                                                    }]
                                                },{
                                                    type: 'formitem',width: '100%',label: '<b>项目日历</b>',labelWidth: 85,labelAlign: 'right',
                                                    children: [{id: 'edo_project_CalendarUID',type: 'combo',width: '100%',readOnly: true}]
                                                }
                                            ]
                                        },{
                                            type: 'ct',width: '100%',layout: 'horizontal',
                                            children:[
                                                {
                                                    type: 'formitem',width: '100%',label: '默认开始时间',labelWidth: 85,
                                                    children: [{id: 'edo_project_DefaultStartTime',type: 'text',width: '100%'}]
                                                },{
                                                    type: 'formitem',width: '100%',label: '默认结束时间',labelAlign: 'right',labelWidth: 85,
                                                    children: [{id: 'edo_project_DefaultFinishTime',type: 'text',width: '100%'}]
                                                }
                                            ]
                                        },{
                                            type: 'ct',
                                            width: '100%',
                                            layout: 'horizontal',
                                            children:[
                                                {
                                                    type: 'formitem',width: '100%',label: '每日工时',labelWidth: 70,
                                                    children: [{id: 'edo_project_MinutesPerDay',type: 'spinner',width: '100%'}]
                                                },{
                                                    type: 'formitem',width: '100%',label: '每周工时',labelAlign: 'right',labelWidth: 70,
                                                    children: [{id: 'edo_project_MinutesPerWeek',type: 'spinner',width: '100%'}]
                                                },{
                                                    type: 'formitem',width: '100%',label: '每月工作日',labelAlign: 'right',labelWidth: 70,
                                                    children: [{id: 'edo_project_DaysPerMonth',type: 'spinner',width: '100%'}]
                                                }
                                            ]
                                        }
                                    ]                                    
                                }
                            ]
                        }                                            
                    ]
                },{
                    type: 'ct',layout: 'horizontal',cls: 'e-dialog-toolbar',width: '100%',verticalAlign: 'bottom',horizontalAlign: 'right',horizontalGap: 10,height: 26,                                    
                    children:[                      
                        {type: 'button',text: '帮助',width: 70,height: 22 },
                        {
                            type: 'space',width: '100%'                                
                        },
                        {
                            type: 'button',
                            text: '确定',
                            width: 70,height: 22,
                            onclick: function(){
                                onOk();
                            }
                        },
                        {
                            type: 'button',
                            text: '取消',
                            width: 70,height: 22,
                            onclick: function(){
                                Edo.managers.PopupManager.removePopup(win);
                            }
                        }
                    ]
                } 
            ]
        });            
    }    
    function onOk(){
        var dataProject = win.dataProject;
        var o = {
            Name: edo_project_Name.text,
            
            StartDate: edo_project_StartDate.date,
            FinishDate: edo_project_FinishDate.date,
            
            WeekStartDay: edo_project_WeekStartDay.selectedItem ? parseInt(edo_project_WeekStartDay.selectedItem.value) : null,
            CalendarUID: edo_project_CalendarUID.selectedItem ? edo_project_CalendarUID.selectedItem.UID : null,
            
            DefaultStartTime: parseInt(edo_project_DefaultStartTime.text),
            DefaultFinishTime: parseInt(edo_project_DefaultFinishTime.text),
            
            MinutesPerDay: parseInt(edo_project_MinutesPerDay.text),
            MinutesPerWeek: parseInt(edo_project_MinutesPerWeek.text),
            DaysPerMonth: parseInt(edo_project_DaysPerMonth.text)            
        };
        if(!o.Name){
            alert("项目名称不能为空");
            return;
        }
        
        var startDate = dataProject.StartDate;
        
        Edo.apply(dataProject, o);
        
        if(startDate != dataProject.StartDate){
            dataProject.orderProjectByStart(dataProject.StartDate, true);
        }else if(CalendarUID != dataProject.CalendarUID){ //如果改变了日历, 则重新排定
        
            dataProject.markHash();
            dataProject.ProjectCalendar = null;
            
            dataProject.createProjectCalendar();   
            dataProject.orderProjectByStart(dataProject.StartDate, false);
        }
        
        dataProject.refresh();
        
        Edo.managers.PopupManager.removePopup(win);
    }
    
    win.set('title', '"'+dataProject.Name+'" 的项目信息');
    win.dataProject = dataProject;
    //设置项目面板的各个组件值
    var format = 'Y-m-d';
    
    edo_project_Name.set('text', dataProject.Name);
    edo_project_StartDate.set('date', dataProject.StartDate);
    edo_project_FinishDate.set('date', dataProject.FinishDate);
    edo_project_CreationDate.set('text', dataProject.CreationDate.format(format));
    edo_project_LastSaved.set('text', dataProject.LastSaved.format(format));        
    
    edo_project_WeekStartDay.set('selectedItem', {value: dataProject.WeekStartDay});        
    
    edo_project_CalendarUID.set({
        displayField: 'Name',
        valueField: 'UID',
        data: dataProject.Calendars
    });
    edo_project_CalendarUID.set('selectedItem', {UID: dataProject.CalendarUID});
    
    edo_project_DefaultStartTime.set('text', dataProject.DefaultStartTime);
    edo_project_DefaultFinishTime.set('text', dataProject.DefaultFinishTime);
    
    edo_project_MinutesPerDay.set('value', dataProject.MinutesPerDay);
    edo_project_MinutesPerWeek.set('value', dataProject.MinutesPerWeek);
    edo_project_DaysPerMonth.set('value', dataProject.DaysPerMonth);
    
    var CalendarUID = dataProject.CalendarUID;
    
    Edo.managers.PopupManager.createPopup({
        target: win,
        modal: true,
        x: 'center',
        y: 'middle'
    });
    return win;
}