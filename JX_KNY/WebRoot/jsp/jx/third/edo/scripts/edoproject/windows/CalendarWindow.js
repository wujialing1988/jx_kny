//日历面板
Edo.project.Project.showCalendarWindow = function(dataProject){
    var win = this.calendarWindow;
    if(!win){
         win = this.calendarWindow = Edo.create({                            
            type: 'window',shadow: false, title: '项目日历',verticalGap: 0,width: 450,minHeight: 230,render: document.body,
            titlebar:[
                {
                    cls: 'e-titlebar-close',
                    onclick: function(e){
                        clearCalendars();
                        Edo.managers.PopupManager.removePopup(win);
                    }
                }
            ],
            children: [
                {
                    type: 'box',width: '100%',height: '100%',//style: 'background-color:#ece9d8;',
                    children: [
                        {
                            type: 'ct', layout: 'horizontal', width: '100%',
                            children: [
                                {
                                    type: 'formitem',width: '100%',label: '对于日历',labelWidth: 60,
                                    children: [
                                        {id: 'edo_project_calendarlist',type: 'combo',width: '100%',readOnly: true,valueField: 'UID',displayField: 'Name',
                                            onselectionchange: function(e){
                                                if(e.selectedItem) fillCalendar(e.selectedItem);                                                                                                                                                
                                            }
                                        }
                                    ]
                                },{
                                    type: 'space',width: '50%'
                                },{
                            type: 'button',text: '删除日历',width: 100,
                            onclick: function(){
                                if(edo_project_calendarlist.data.getCount() == 0){
                                    alert("必须要有至少一个项目日历");
                                    return;
                                }
                                var calendar = edo_project_calendarlist.selectedItem;
                                if(calendar.UID == win.dataProject.CalendarUID){
                                    alert("不能删除项目日历");
                                    return;
                                }
                                if(confirm("确认删除日历 \""+calendar.Name+"\" ?")){
                                    edo_project_calendarlist.data.remove(calendar);
                                    edo_project_calendarlist.set('selectedIndex', 0);
                                }
                                
                            }
                        }, {
                                    type: 'button',width: 100,text: '新建日历',
                                    onclick: function(e){
                                        Edo.project.Project.showCreateCalendarWindow(win.dataProject.Calendars, function(calendar){
                                            edo_project_calendarlist.data.add(calendar);
                                            edo_project_calendarlist.set('selectedItem', calendar);
                                        });
                                    }
                                }
                            ]
                        },{
                            type: 'ct', layout: 'horizontal', width: '100%',
                            children: [
                                {type: 'label', width: 100,text:'图例:'},
                                {type: 'ct', width: '100%',
                                    children: [
                                        {type: 'label', text: '单击工作日以查看其工作时间:'},
                                        {id: 'edo_project_calendar_datepicker', type: 'datepicker', footerVisible: false, width: '100%'}
                                    ]
                                },
                                {type: 'ct', width: 118,
                                    children: [
                                        {type: 'div', width: '100%', html:''}
                                    ]
                                }
                            ]
                        },{
                            type: 'ct',width: '100%',verticalGap: 0,
                            children: [
                                {type: 'tabbar', width: '100%',border: 0,selectedIndex: 0,
                                    onselectionchange: function(e){
                                        edo_project_calendar_tab.set('selectedIndex', e.index);
                                    },
                                    children:
                                    [
                                        {type:'button',text:'例外日期'},{type: 'button', text: '工作周'}
                                    ]
                                },
                                {
                                    id: 'edo_project_calendar_tab', type: 'ct', width: '100%', layout: 'viewstack',height: 140,
                                    children:[
                                        {
                                            type: 'box', width: '100%',padding:0,verticalGap: 0,height:'100%',
                                            children: [
                                                {
                                                    type: 'box',layout: 'horizontal',border: 0,
                                                    children: [
                                                        {type: 'button', text: '新增',
                                                            onclick: function(e){                                                            
                                                                var date = edo_project_calendar_datepicker.date.clearTime();
                                                                
                                                                edo_project_calendar_exceptions.data.insert(0,{ 
                                                                    DayType: 0,
                                                                    WorkingTimes: [],
                                                                    TimePeriod: {
                                                                        FromDate: date,//日历默认会有一个选中日期, 作为例外日期的默认时间
                                                                        ToDate: date.add(Date.DAY, 1).add(Date.MINUTE, -1)
                                                                    }
                                                                });
                                                            }
                                                        },
                                                        {type: 'button', text: '删除',
                                                            onclick: function(e){                                                                        
                                                                edo_project_calendar_exceptions.data.removeRange(edo_project_calendar_exceptions.getSelecteds());
                                                            }
                                                        },
                                                        {id: 'edo_project_calendar_exceptions_more',type: 'button', text: '工作时间',
                                                            onclick: function(e){
                                                                var weekday = edo_project_calendar_exceptions.getSelected();
                                                                if(weekday){                                                                    
                                                                    Edo.project.Project.showWorkingTimeWindow(weekday);
                                                                }
                                                            }
                                                        }
                                                    ]
                                                },
                                                {
                                                    autoExpandColumn: 'name',id:'edo_project_calendar_exceptions',type: 'table',width: '100%',style: 'border-width:0;border-top-width:1px;',height:'100%',rowSelectMode: 'multi',
                                                    onbeforesubmitedit: function(e){
                                                        switch(e.field){
                                                        case 'TimePeriod.FromDate':
                                                            e.value = e.value.clearTime();
                                                            if(e.record.TimePeriod.ToDate.getTime() < e.value.getTime()){
                                                                 e.record.TimePeriod.ToDate = e.value.clearTime().add(Date.DAY, 1).add(Date.MINUTE, -1);
                                                            }
                                                        break;
                                                        case 'TimePeriod.ToDate':
                                                            e.value = e.value.clearTime().add(Date.DAY, 1).add(Date.MINUTE, -1);
                                                            if(e.record.TimePeriod.FromDate.getTime() > e.value.getTime()){
                                                                 e.record.TimePeriod.FromDate = e.value.clearTime(true);
                                                            }
                                                        break;
                                                        }
                                                    },
                                                    onselectionchange: function(e){                                                        
                                                        var r = this.getSelected();
                                                        checkExceptionWorkingTime(r);
                                                    },
                                                    columns: [
                                                        Edo.lists.Table.createMultiColumn(),
                                                        {id:'name',header: '名称', width: 150, headerAlign: 'center', dataIndex: 'Name',
                                                            editor: {type: 'text'}
                                                        },
                                                        {header: '开始时间', headerAlign: 'center', dataIndex: 'TimePeriod.FromDate',
                                                            editor: {type: 'date'},
                                                            renderer: function(v, r){
                                                                return r.TimePeriod.FromDate ? r.TimePeriod.FromDate.format('Y-m-d') : '-';
                                                            }
                                                        },
                                                        {header: '完成时间', headerAlign: 'center', dataIndex: 'TimePeriod.ToDate',
                                                            editor: {type: 'date'},
                                                            renderer: function(v, r){
                                                                return r.TimePeriod.ToDate ? r.TimePeriod.ToDate.format('Y-m-d') : '-';
                                                            }
                                                        },
                                                        Edo.lists.Table.createCheckColumn({
                                                            header: '工作日', headerAlign: 'center', dataIndex: 'DayWorking',
                                                            onsubmitedit: function(e){
                                                                var r = e.source.getSelected();
                                                                checkExceptionWorkingTime(r);
                                                            }
                                                        })
                                                    ]
                                                }
                                            ]
                                        },
                                        {
                                            type: 'box', width: '100%',padding:0,verticalGap: 0,layout:'horizontal',height:'100%',
                                            children: [
                                                {
                                                    id:'edo_project_calendar_weeks',type: 'table',width: '100%',height:'100%',style: 'border-width:0;border-right-width:1px;',//headerVisible: false,
                                                    autoExpandColumn: 'week',
                                                    columns: [
                                                        {header: '星期', width: 150, headerAlign: 'center', dataIndex: 'DayType',id:'week',enableSort: false,
                                                            renderer: function(v){
                                                                return weeks[v-1];
                                                            }
                                                        },
                                                        Edo.lists.Table.createCheckColumn({
                                                            header: '工作日', headerAlign: 'center', dataIndex: 'DayWorking', width: 80,enableSort: false,
                                                            onsubmitedit: function(e){                                                            
                                                                var r = e.source.getSelected();
                                                                checkWorkingTime(r);
                                                            }
                                                        })
                                                    ],
                                                    onselectionchange: function(e){                                                        
                                                        var r = this.getSelected();
                                                        checkWorkingTime(r);
                                                    }
                                                },
                                                {
                                                    type: 'ct',width: '100%',height: '100%',verticalGap: 0,
                                                    children: [
                                                        {
                                                            type: 'box',layout: 'horizontal',border: 0,
                                                            children: [
                                                                {type: 'label', text: '工作时间:'},
                                                                {type: 'button', text: '新增', id: 'edo_project_calendar_worktimes_add',
                                                                    onclick: function(e){
                                                                        edo_project_calendar_worktimes.data.add({
                                                                        });
                                                                    }
                                                                },
                                                                {type: 'button', text: '删除', id: 'edo_project_calendar_worktimes_del',
                                                                    onclick: function(e){
                                                                        edo_project_calendar_worktimes.data.remove(edo_project_calendar_worktimes.getSelected());
                                                                    }
                                                                }
                                                            ]
                                                        }
                                                        ,
                                                        {
                                                            id: 'edo_project_calendar_worktimes', type: 'table',width: '100%',height: '100%',style: 'border-width:0;border-left-width:1px;border-top-width:1px;',autoColumns: true,enableDragDrop: true,
                                                            onbeforesubmitedit: function(e){
                                                                var wts = e.source.data.source;
                                                                var record = e.record;
                                                                
                                                                record[e.field] = e.value;
                                                                var valid = validWorkingTimes(wts);
                                                                if(!valid){
                                                                    record[e.field] = e.oldValue;                                                                   
                                                                }else{
                                                                    e.source.refresh(true);
                                                                }
                                                                return valid;
                                                            },
                                                            columns: [
                                                                {id: 'id',header: '',width: 32,align: 'center',style:  'cursor:move;',enableDragDrop: true,enableSort: false,
                                                                    renderer: function(v, r, c, rowIndex){
                                                                        return rowIndex+1;
                                                                    }
                                                                },
                                                                {header: '开始时间', headerAlign: 'center', dataIndex: 'FromTime',enableSort: false,
                                                                    editor: {value: '08:00:00',type: 'timespinner',getValue:function(){return this.value.format('H:i:s');}}
                                                                },
                                                                {header: '结束时间', headerAlign: 'center', dataIndex: 'ToTime', enableSort: false,
                                                                    editor: {value: '17:00:00',type: 'timespinner',getValue:function(){return this.value.format('H:i:s');}}
                                                                }
                                                            ]
                                                        }
                                                    ]                                                    
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
                              
                        {type: 'space',width: '100%'},
                        {type: 'button',text: '确定',width: 70, height: 22,onclick: function(){onOk();}},
                        {
                            type: 'button',text: '取消',width: 70,height: 22,
                            onclick: function(){
                                clearCalendars();
                                Edo.managers.PopupManager.removePopup(win);
                            }
                        }
                    ]
                } 
            ]
        });
        
        win.validWorkingTimes = function(wts){
            var wwts = [];
            for(var i=0,l=wts.length; i<l; i++){
                var o = wts[i];
                
                if(!o.FromTime || !o.ToTime) continue;
                
                var t = o.FromTime.split(':');
                o.FromHour = Math.floor(t[0]) + Math.floor(t[1])/60;            
                var t = o.ToTime.split(':');
                o.ToHour = Math.floor(t[0]) + Math.floor(t[1])/60;
                
                if(o.FromHour > o.ToHour){
                    alert("开始时间不能大于结束时间");
                    return false;
                }
                
                wwts.add(o);
            }
            //下一班次必须比前一半次的完成时间大
            
            for(var i=0,l=wwts.length; i<l; i++){
                var o = wwts[i];
                
                for(var j=0,k=wwts.length; j<k; j++){
                    var ot = wwts[j];
                    
                    if(o == ot) continue;
                    
                    if(
                        (ot.FromHour <= o.FromHour && o.FromHour <= ot.ToHour)
                        || (ot.FromHour <= o.ToHour && o.ToHour <= ot.ToHour)
                        || (o.FromHour <= ot.FromHour && ot.FromHour <= o.ToHour)
                        || (o.FromHour <= ot.ToHour && ot.ToHour <= o.ToHour)
                    ){
                        alert("工作时间不能交叉设置");
                        return false;
                    }
                }            
            }
            
            return true;
        }        
    }          
    
    win.dataProject = dataProject;
    
    var validWorkingTimes = win.validWorkingTimes;
    
    function clearCalendars(){
        var dataProject = win.dataProject;
        var calendars = edo_project_calendarlist.data.source;
        calendars.each(function(calendar){                       
            delete calendar.weeks;
            delete calendar.exceptions;
        });
        edo_project_calendarlist.set('selectedIndex', -1);
        edo_project_calendarlist.set('data', []);
    }
    function onOk(){    
        var dataProject = win.dataProject;
        var calendars = edo_project_calendarlist.data.source;
        
        
        var allNoWorking = true;
        
        calendars.each(function(calendar){        
            if(calendar.weeks){
                calendar.WeekDays = [];
                calendar.WeekDays.addRange(calendar.weeks);
                calendar.WeekDays.addRange(calendar.exceptions);                
            }
            
            calendar.WeekDays.each(function(wd){
                wd.DayWorking = wd.DayWorking ? 1 : 0;
                if(wd.DayWorking){
                    if(!wd.WorkingTimes) wd.WorkingTimes = [];
                }
                
                if(wd.DayType != 0 && wd.DayWorking){
                    allNoWorking = false;
                }
            });
        });
        
        if(allNoWorking){
            alert("日历的工作周不能全部设置为非工作日");
            return;
        }
        
//        calendars.each(function(calendar){                       
//            delete calendar.weeks;
//            delete calendar.exceptions;
//        });
        
        dataProject.Calendars.load(calendars);
        dataProject.markHash();
        dataProject.ProjectCalendar = null;
        dataProject.createProjectCalendar();        
        
        //按新日历, 重新排定项目任务
        
        dataProject.orderProjectByStart(dataProject.StartDate, false);
        
        dataProject.refresh();
        
        Edo.managers.PopupManager.removePopup(win);
        
        clearCalendars();
    }                          
    
    var weeks = ['星期日','星期一','星期二','星期三','星期四','星期五','星期六']    
    function fillCalendar(calendar){        
    
        var dataProject = win.dataProject;
        var weeks = [];
        var exceptions = [];
        function create(){
            if(!calendar) {               
                return;
            }
            if(calendar.weeks) {
                weeks = calendar.weeks;
                exceptions = calendar.exceptions;
                return;
            }
            for(var i=0,l=calendar.WeekDays.length; i<l; i++){
                var wd = calendar.WeekDays[i];
                if(wd.DayType == 0) {
                    exceptions.add(wd);
                }else{
                    weeks.add(wd);
                }
                if(!wd.WorkingTimes) wd.WorkingTimes = [];
            }
            
            weeks = Edo.clone(weeks);
            exceptions = Edo.clone(exceptions);
            calendar.weeks = weeks;
            calendar.exceptions = exceptions;
        }
        create();
        
        edo_project_calendar_exceptions.set('data', exceptions);        
        
        edo_project_calendar_weeks.set('data', weeks);
        
        edo_project_calendar_datepicker.refresh();
        
        //checkWorkingTime();
        edo_project_calendar_weeks.clearSelect();
    }
    function checkExceptionWorkingTime(r){
        var enable = true;
        if(r && r.DayWorking == 1){            
            enable = true;
        }else{
            enable = false;
        }
        edo_project_calendar_exceptions_more.set('enable', enable);
        edo_project_calendar_datepicker.refresh();
    }
    function checkWorkingTime(r){
        var enable = true;
        if(r && r.DayWorking == 1){
            edo_project_calendar_worktimes.data.load(r.WorkingTimes);
            enable = true;
        }else{
            enable = false;
            edo_project_calendar_worktimes.data.clear();
        }
        edo_project_calendar_worktimes_add.set('enable', enable);
        edo_project_calendar_worktimes_del.set('enable', enable);        
        edo_project_calendar_datepicker.refresh();
    }
    /////////////////////////////////////////
    
    edo_project_calendarlist.set({        
        data: Edo.clone(dataProject.Calendars.source)
    });
    edo_project_calendarlist.set('selectedItem', {UID: dataProject.CalendarUID});    
    
    edo_project_calendar_datepicker.set('date', dataProject.StartDate.clone());    
    
    checkWorkingTime();
    checkExceptionWorkingTime();
    
    edo_project_calendar_exceptions.deselect();
    edo_project_calendar_weeks.deselect();
    
    //日历
    edo_project_calendar_datepicker.isWorkingDate = function(date){    
        var calendar = edo_project_calendarlist.selectedItem;
        if(!calendar || !calendar.weeks) return;
        var day = date.getDay();
        
        var iswork = false;
        for(var i=0,l=calendar.weeks.length; i<l; i++){
            var week = calendar.weeks[i];
            if(week.DayType == day+1){
                iswork = week.DayWorking;
                break;
            }
        }
        var dateTime = date.getTime();
        for(var i=0,l=calendar.exceptions.length; i<l; i++){
            var exception = calendar.exceptions[i];
            
            if(exception.TimePeriod.FromDate.getTime() <= dateTime && dateTime <= exception.TimePeriod.ToDate.getTime()){
                iswork = exception.DayWorking;
                break;
            }                
        }
        return iswork;
    }
    
    Edo.managers.PopupManager.createPopup({
        target: win,
        modal: true,
        x: 'center',
        y: 'middle'
    });
    return win;
}

//工作时间设置面板
Edo.project.Project.showWorkingTimeWindow = function(weekDay){
    var validWorkingTimes = this.calendarWindow.validWorkingTimes;

    var win = this.workingTimeWindow;
    if(!win){
         win = this.workingTimeWindow = new Edo.containers.Window();
         win.set({
            title: '设置工作时间',
            titlebar:[
                {
                    cls: 'e-titlebar-close',
                    onclick: function(e){
                        this.parent.owner.hide();
                    }
                }
            ],
            verticalGap: 0,
            children: [
                {
                    type: 'box',width: '100%',height: '100%',verticalGap: 0,padding:0,
                    children: [
                        {
                            type: 'box',layout: 'horizontal',border: 0,
                            children: [                                
                                {type: 'button', text: '新增', id: 'edo_project_worktimes_add',
                                    onclick: function(e){
                                        edo_project_worktimes.data.add({
                                        });
                                    }
                                },
                                {type: 'button', text: '删除', id: 'edo_project_worktimes_del',
                                    onclick: function(e){                                                                        
                                        edo_project_worktimes.data.remove(edo_project_worktimes.getSelected());
                                    }
                                }
                            ]
                        }
                        ,
                        {
                            id: 'edo_project_worktimes', type: 'table',width: '100%',height: '100%',style: 'border-width:0;border-top-width:1px;',autoColumns: true,enableDragDrop: true,
                            onbeforesubmitedit: function(e){
                                var wts = e.source.data.source;
                                var record = e.record;
                                
                                record[e.field] = e.value;
                                var valid = validWorkingTimes(wts);
                                if(!valid){
                                    record[e.field] = e.oldValue;                                                                   
                                }else{
                                    e.source.refresh(true);
                                }
                                return valid;
                            },
                            columns: [
                                {id: 'id',header: '',width: 32,align: 'center',style:  'cursor:move;',enableDragDrop: true,enableSort: false,
                                    renderer: function(v, r, c, rowIndex){
                                        return rowIndex+1;
                                    }
                                },
                                {header: '开始时间', headerAlign: 'center', dataIndex: 'FromTime',enableSort: false,
                                    editor: {value: '08:00:00',type: 'timespinner',getValue:function(){return this.value.format('H:i:s');}}
                                },
                                {header: '结束时间', headerAlign: 'center', dataIndex: 'ToTime', enableSort: false,
                                    editor: {value: '17:00:00',type: 'timespinner',getValue:function(){return this.value.format('H:i:s');}}
                                }
                            ]
                        }
                    ]                                                    
                },{
                    type: 'ct',layout: 'horizontal',cls: 'e-dialog-toolbar',width: '100%',verticalAlign: 'bottom',horizontalAlign: 'right',horizontalGap: 10,height: 26,                                    
                    children:[                                                                  
                        {type: 'space',width: '100%'},
                        {type: 'button',text: '确定',width: 70,onclick: function(){onOk();}},
                        {
                            type: 'button',text: '取消',width: 70,
                            onclick: function(){
                                win.hide();
                            }
                        }
                    ]
                } 
            ]
         });
    }
    function onOk(){
        
        win.weekDay.WorkingTimes = edo_project_worktimes.data.source.clone();
        
        win.hide();
    }
    
    edo_project_worktimes.data.load(Edo.clone(weekDay.WorkingTimes));
    win.weekDay =weekDay;
    win.show('center', 'middle', true);
}

//创建新日历面板
Edo.project.Project.showCreateCalendarWindow = function(calendars, callback){

    var win = this.createCalendarWindow;
    if(!win){
         win = this.createCalendarWindow = new Edo.containers.Window();
         win.set({
            title: '新建日历',
            titlebar:[
                {
                    cls: 'e-titlebar-close',
                    onclick: function(e){
                        this.parent.owner.hide();
                    }
                }
            ],
            width: 300,
            verticalGap: 0,
            children: [
                {
                    type: 'box',width: '100%',height: '100%',verticalGap: 0,
                    children: [
                        {
                            type: 'ct',layout: 'horizontal',width:'100%',
                            children: [                                
                                {type: 'label', text: '名称:'},
                                {type: 'text',  id: 'edo_project_createcalendar_name', width: '100%'}
                            ]
                        },{
                            type: 'ct',layout: 'horizontal',width:'100%',
                            children: [                                
                                {
                                    id:'edo_project_createcalendar_sel',type: 'radiogroup', data: [{text: '默认日历', value: 1},{text: '复制日历, 从', value: 2}]
                                },{
                                    type: 'ct',width:'100%',
                                    children: [
                                        {type: 'space', height: 15},
                                        {
                                            type: 'combo', id: 'edo_project_createcalendar_list',readOnly: true,valueField: 'UID',displayField: 'Name'
                                        }
                                    ]
                                }
                            ]
                        }
                    ]                                                    
                },{
                    type: 'ct',layout: 'horizontal',cls: 'e-dialog-toolbar',width: '100%',verticalAlign: 'bottom',horizontalAlign: 'right',horizontalGap: 10,height: 26,                                    
                    children:[
                        {type: 'space',width: '100%'},
                        {type: 'button',text: '确定',width: 70,onclick: function(){onOk();}},
                        {
                            type: 'button',text: '取消',width: 70,
                            onclick: function(){
                                win.hide();
                            }
                        }
                    ]
                } 
            ]
         });
    }
    function onOk(){        
        var dataProject = new Edo.data.DataProject();
        var calendar = dataProject.Calendars.source[0];        
        if(edo_project_createcalendar_sel.getValue() == 2){            
            var c = edo_project_createcalendar_list.selectedItem;            
            calendar = Edo.clone(c);
        }
        calendar.UID = UUID();
        calendar.Name = edo_project_createcalendar_name.text;
        if(!calendar.Name){
            alert("日历名称不能为空");
            return;
        }
        
        if(callback) callback(calendar);
        win.hide();
    }
    
    edo_project_createcalendar_name.set('text', '');
    edo_project_createcalendar_list.set('data', calendars);
    edo_project_createcalendar_list.set('selectedIndex', 0);
    
    edo_project_createcalendar_sel.setValue(1);
    
    win.show('center', 'middle', true);
}
