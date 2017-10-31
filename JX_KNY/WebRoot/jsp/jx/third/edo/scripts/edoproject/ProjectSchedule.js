/*
	e对象的属性描述:
    {
        action:         本次任务面板的操作类型,新增或编辑.new/edit
        dataProject:    甘特图数据对象  
        selectedTask:   当前选中的任务对象
        task:           创建的任务对象/或者是当前选中的任务对象
    }
    
    当通过任务面板修改数据后,需要经过如下验证:    
    1.开始日期必须小于完成日期.
    2.任务相关性验证
*/
function onTaskWindowCallback(e){
    var task = e.task, selectedTask = e.selectedTask, dataProject = e.dataProject;
    var d = dataProject.Tasks;
    
    var ep = new ProjectSchedule(dataProject);
    ep.dataProject = dataProject;
    
    d.beginChange();
    
//    if(task.Finish.getTime() < task.Start.getTime()){
//        alert("完成日期不能小于开始日期");
//        return false;
//    }
    if(!dataProject.validTaskPredecessorLinks(task)){
        alert("任务相关性出错");
        return false;
    }
    
    if(e.action == 'new'){                //处理新增    
        return ep.onTaskAdd({
            type: 'taskadd',
            task: task,
            targetTask: selectedTask
        });
    }else if(e.action == 'edit'){         //处理编辑:只需要更新当前选中行即可
        //1)        
        
        //首先校验日期是否正确
        if(!ep.canSyncTaskDate(selectedTask, task.Start, task.Finish)) return false;
        
        var oldPercentComplete = selectedTask.PercentComplete;
        var r = selectedTask;
        for(var p in task){
            r[p] = task[p];
        }
        
        task = selectedTask;
        
        task.Weight = dataProject.getLimitWeight(task);
        
        ep.syncData(task, task.Start);
        
        if(oldPercentComplete != selectedTask.PercentComplete){
            dataProject.syncPercentComplete(task);          //同步完成百分比
        }
        
        dataProject.tryEditTaskComplete(task);          //激发taskeditcomplete事件
        
        //经过上面的更新后, 刷新甘特图界面
        dataProject.refresh(true);
        
        if(typeof(project) != 'undefined'){
            setTimeout(function(){
                project.gantt.viewRow(task);
                project.tree.viewRow(task);
                project.tree.select(task);
            }, 100);
        } 
        
        
    }
}
/**
    @name ProjectSchedule
    @class     
    @description EdoProject逻辑调度插件:实现一个类似微软Project的项目管理功能逻辑.同时, 此组件需要日期/工期等基础数据插件的支持.
*/
ProjectSchedule = function(){
    ProjectSchedule.superclass.constructor.call(this);
}
ProjectSchedule.extend(Edo.core.Component, {
    init: function(dataProject){
        this.dataProject = dataProject;
             
        dataProject.on('taskadd', this.onTaskAdd, this);                       //新增
        dataProject.on('taskremove', this.onTaskRemove, this);                 //删除
        dataProject.on('taskedit', this.onTaskEdit, this);                     //修改
        
        dataProject.on('taskchange', this.onTaskChange, this);                 //任务属性改变(处理数据逻辑)
        
        dataProject.on('taskgradechange', this.onTaskGradeChange, this);       //升级/降级任务
        dataProject.on('taskmove', this.onTaskMove, this);                     //任务移动调整
        
    },
    //将新增任务插入到选中任务之前
//    onTaskAdd: function(e){
//    
//        var dataProject = this.dataProject;
//        var tasks = dataProject.Tasks;                
//        
//        var task =  e.task || dataProject.createTask();    //创建一个空白的任务,具备了各个必要属性.
//        
//        var parentTask = tasks, index = tasks.children.length;
//        
//        //如果有目标行,则自己控制如果加入: 1)加在前面; 2)加在后面; 3)加在里面,最后一个...等等   
//        //这里的逻辑参考的是微软Project,加在选中任务的前面
//        if(e.targetTask){
//            parentTask = tasks.findParent(e.targetTask);
//            if(parentTask){
//                index = parentTask.children.indexOf(e.targetTask);
//            }
//        }
//        
//        dataProject.Tasks.beginChange();
//                
//        tasks.insert(index, task, parentTask);       
//        task.Weight = dataProject.getLimitWeight(task);
//        if(this.syncData(task, task.Start) === false){
//            tasks.remove(task);
//            return false;
//        }else{
//            dataProject.refresh(true);
//        }        
//        
//    },
    //将新增任务插入到选中任务的子任务中
    onTaskAdd: function(e){
    
        var dataProject = this.dataProject;
        var tasks = dataProject.Tasks;                
        
        var task =  e.task || dataProject.createTask();    //创建一个空白的任务,具备了各个必要属性.
        
        var parentTask = tasks, index = tasks.children.length;
        
        //如果有目标行,则自己控制如果加入: 1)加在前面; 2)加在后面; 3)加在里面,最后一个...等等   
        //这里的逻辑参考的是微软Project,加在选中任务的前面
        if(e.targetTask){
            parentTask = e.targetTask;            
        }
        
        dataProject.Tasks.beginChange();
                
        tasks.add(task, parentTask);       //加入到最后一个位置
        //task.insert(0, task, parentTask);   //加入到第一个位置
        task.Weight = dataProject.getLimitWeight(task);
        
        if(this.syncData(task, task.Start) === false){
            tasks.remove(task);
            return false;
        }else{
            dataProject.refresh(true);
        }        
        
    },    

    onTaskRemove: function(e){
        var dataProject = this.dataProject;
        
        if(confirm("确认删除 \""+e.task.Name+"\" 任务吗?")){  
            
            dataProject.Tasks.beginChange();
            
            //父任务
            var parentTask = dataProject.Tasks.findParent(e.task);
            //后置任务
            
            var successorTasks = dataProject.getSuccessorTasks(e.task);
            
            dataProject.Tasks.remove(e.task);
            
            dataProject.syncTasks();
            
            if(parentTask && parentTask.Start){
                this.syncData(parentTask, parentTask.Start);
            }
            successorTasks.each(function(t){
                this.syncData(t, t.Start);
            }, this);
            
            dataProject.refresh(true);
        }
        
    },
    onTaskEdit: function(e){            
        var dataProject = this.dataProject;
        Edo.project.Project.showTaskWindow('edit',e.task, dataProject, onTaskWindowCallback);  
    },
    onTaskChange: function(e){
        var dataProject = this.dataProject;
        dataProject.Tasks.beginChange();    
        var property = e.property, value = e.value, task = e.task, tasks = dataProject.Tasks;
                
        dataProject._taskChanged(task);
        switch(property){
            case 'Weight':          //权重: 摘要任务直属下级任务的权重之和不能大于100
                task.Weight = value;
                task.Weight = dataProject.getLimitWeight(task);
                dataProject.syncPercentComplete(task);
                dataProject.refresh();
            break;
            case 'ActualDuration':  //实际工期            
                task.ActualDuration = value;
                if(task.ActualStart){                    
                    task.ActualFinish = dataProject.getFinishByCalendar(task.ActualStart, value);
                }
                dataProject.refresh();
            break;
            case 'ActualStart':     //实际开始
                start = dataProject.getWorkTime(value, true, true);
                task.ActualStart = start;
                if(task.ActualDuration || task.ActualDuration === 0){
                    task.ActualFinish = dataProject.getFinishByCalendar(start, task.ActualDuration);                    
                }
                dataProject.refresh();
            break;
            case 'ActualFinish':    //实际完成
              
                var finish = dataProject.getWorkTime(value, false, false);                    
                task.ActualFinish = finish;
                if(task.ActualStart){                    
                    task.ActualDuration = dataProject.getDuratonByCalendar(task.ActualStart, finish);                    
                }
                dataProject.refresh();
            break;
            case 'Duration':           
                var duration = task.Duration;
                task.Duration = value.Duration;
                
                if(dataProject.enableDurationLimit && value.Duration != 0){  //限定任务的工期必须是一个工作时间区间duration整数
                    task.Duration = dataProject.getDurationByStartLimit(task.Start, task.Duration);                    
                    
                    value.Duration = task.Duration;
                }                
                
                //如果工期,日期数据是逻辑允许的, 则更新好日期数据.
                if(this.syncData(task, task.Start) !== false){                               
                    Edo.apply(task, value);
                }else{
                    task.Duration = duration;
                }
                dataProject.refresh();
            break;
            case 'Start':
                start = dataProject.getWorkTime(value, true, true);     //工作时间的估算时间, 而不是精确时间
              
                //start = dataProject.getWorkingDay(value, true);
                
                finish = dataProject.getFinishByCalendar(start, task.Duration);    //根据开始日期和工期,重新计算完成日期        
                function fn1(){
                    
                    task.ConstraintType = 4; //不得早于...开始
                    task.ConstraintDate = start.clone();
                    
                    this.syncData(task, start);  //调整开始日期,工期是保持不变的    
                    dataProject.refresh();
                }
                
                this.syncConstraint(task, start, finish, true, fn1.bind(this));
            break;
            case 'Finish':                
                 //得到工作日中的完成日期
           
                 var finish = dataProject.getWorkTime(value, false, false);
        
                 //完成日期不能小于开始日期
//                 if(finish.getTime() < task.Start.getTime()){
//                    alert("完成日期不能小于开始日期");
//                    break;
//                 }
                    
                 function fn2(){
                    
                    task.Duration = dataProject.getDuratonByCalendar(task.Start, finish);                   
                                        
                    this.syncData(task, task.Start);             
                    this.syncDuration(task);                         //根据实际情况,同步工期
                    
                    dataProject.refresh();
                 }
                 
                 this.syncConstraint(task, task.Start, finish, false, fn2.bind(this));
            break;
            default:
                var _property = task[property];
                task[property] = value;
                
                if(property == 'PredecessorLink' && dataProject.validTaskPredecessorLinks(task) !== true){
                    alert("任务关联性出错");
                    task[property] = _property;
                    return;                            
                }
                if(property == 'PredecessorLink'){
                    this.syncData(task, task.Start);
                }
                
                if(property == 'PercentComplete'){
                
                    dataProject.syncPercentComplete(task);
                }
                
                if(this.syncData(task, task.Start) === false){
                    task[property] = _property;
                }
                
                dataProject.refresh();
            break;
        }
        
    },
    //升级/降级
    onTaskGradeChange: function (e){
        var dataProject = this.dataProject;
        var task = e.task;                
        
        dataProject.Tasks.beginChange();
        
        if(e.action == 'upgrade'){                                                
            dataProject.Tasks.upgrade(task);            
        }else{                             
            dataProject.Tasks.downgrade(task);
        }
        
        var parentTask = dataProject.Tasks.findParent(task);
        if(parentTask && parentTask.Start){
            this.syncData(parentTask, parentTask.Start);
        }else{
            this.syncData(task, task.Start);
        }
                
        dataProject.refresh();
    },
    //任务位置调整
    onTaskMove: function(e){
        var dataProject = this.dataProject;
        var task = e.task;
        
        dataProject.Tasks.beginChange();
        
        var p1 = dataProject.Tasks.findParent(task);
        dataProject.Tasks.move(e.task, e.targetTask, e.action);
        var p2 = dataProject.Tasks.findParent(task);
        
        if(p1 && p1.Start){
            this.syncData(p1, p1.Start);
        }
        if(p2 && p2.Start){
            this.syncData(p2, p2.Start);
        }else{
            this.syncData(task, task.Start);
        }
        
        dataProject.refresh();
    },
    //-------------------------- 数据联动逻辑处理(优化版) -------------------------//
    syncData: function(task, start, finish){
    
        var dataProject = this.dataProject;
        if(!this.canSyncTaskDate(task, start, finish)) return false;
        
        dataProject.orderTaskByStart(task, start);
         
    },
    //当一个任务日期改变时,首先判断此任务是否可变日期
    canSyncTaskDate: function(task, start, finish){
        
        var dataProject = this.dataProject;
        
        start = dataProject.getWorkingDay(start, true);                   
        finish = dataProject.getFinishByCalendar(start, task.Duration);
        
        var sd = task.Start;
        var ed = task.Finish;
        
        task.Start = start;
        task.Finish = finish;
        
        return true;        //不强制判断错误,自动有效调节
        
        //本任务作为"后续任务"
        var startTime = start.getTime(), finishTime = finish.getTime();
        
        var lks = task.PredecessorLink;
        for(var i=0,l=lks.length; i<l; i++){
            var lk = lks[i];
            var tt = dataProject.getTask(lk.PredecessorUID);
            //与前置任务的限制逻辑
            var ptype = Edo.data.DataGantt.PredecessorLinkType[lk.Type];
            
            var preStartTime = tt.Start.getTime();
            var preFinishTime = tt.Finish.getTime();
            
            switch(parseInt(lk.Type)){
            case 0:         //完成-完成(FF)
                if(finishTime < preFinishTime){
                    task.Start = sd;
                    task.Finish = ed;
                    alert("此任务的完成日期不能早于任务 \""+tt.Name+"\" 的完成日期");
                    return false;
                }
            break;
            case 1:         //FS:完成-开始
                if(startTime < preFinishTime){
                    task.Start = sd;
                    task.Finish = ed;
                    alert("此任务的开始日期不能早于任务 \""+tt.Name+"\" 的完成日期");
                    return false;
                }
            break;
            case 2:         //开始-完成(SF)
                if(finishTime < preStartTime){
                    task.Start = sd;
                    task.Finish = ed;
                    alert("此任务的完成日期不能早于任务 \""+tt.Name+"\" 的开始日期");
                    return false;
                }
            break;
            case 3:         //开始-开始(SS)
                if(startTime < preStartTime){
                    task.Start = sd;
                    task.Finish = ed;
                    alert("此任务的开始日期不能早于任务 \""+tt.Name+"\" 的开始日期");
                    return false;
                }
            break;
            }
            
        }
        
        task.Start = sd;
        task.Finish = ed;
        
        return true;
    },
    
    syncDuration: function (task){
        var dataProject = this.dataProject;
        var duration = dataProject.getDuratonByCalendar(task.Start, task.Finish);             
        
        task['Duration'] = duration;
        
        var parentTask = dataProject.Tasks.findParent(task);
        if(parentTask && parentTask.Start) this.syncDuration(parentTask);
    },
    syncConstraint: function (task, start, finish, isStart, callback){
        if(this.dataProject.enableConstraint !== true){
            if(callback) callback();
            return;
        }
        
        var ctype = task.ConstraintType;
        var cdate = task.ConstraintDate;
        
        var cdateTime = cdate ? cdate.clone().clearTime().getTime() : null;
        var sdateTime = start.clone().clearTime().getTime();
        var fdateTime = finish.clone().clearTime().getTime();
        
        var startStr = start.format('Y-m-d');
        var finishStr = finish.format('Y-m-d');
        var cdateStr = cdate ? cdate.format('Y-m-d') : '';
        
        
        var cdesc = Edo.data.DataProject.ConstraintType[ctype];
        var cString = cdesc ? cdesc.Name : '';
        
        if(ctype == 0 || ctype == 1){
    //        if(isStart){
    //            task.ConstraintType = 4;
    //            task.ConstraintDate = start;
    //        }else{
    //            task.ConstraintType = 6;
    //            task.ConstraintDate = finish;
    //        }
            
        }else{
            if(ctype == 2 && sdateTime != cdateTime){     //必须开始于
                var text = '您将任务 '+task.Name+' 的开始日期移到 '+startStr+' ,这位于本任务限制的日期<br/>('+cdateStr+')之外';
                Edo.project.Project.showConstraintWindow('规划向导', text, 
                    [
                        {text: '继续，移动任务并修改限制日期', value: 1},
                        {text: '继续，移动任务并取消限制', value: 2},
                        {text: '取消，不要移动任务', value: 0}
                    ], 
                    function(selected){
                        switch(parseInt(selected.value)){
                            case 0:
                                
                            break;
                            case 1:
                                task.ConstraintDate = start;
                                callback();
                            break;
                            case 2:
                                task.ConstraintType = 0;
                                task.ConstraintDate = null;
                                callback();
                            break;
                        }
                        
                    }
                );    
                
                return;
            }
            if(ctype == 3 && fdateTime != cdateTime){     //必须完成于
                var text = '您将任务 '+task.Name+' 的完成日期移到 '+finishStr+' ,这位于本任务限制的日期<br/>('+cdateStr+')之外';
                Edo.project.Project.showConstraintWindow('规划向导', text, 
                    [
                        {text: '继续，移动任务并修改限制日期', value: 1},
                        {text: '继续，移动任务并取消限制', value: 2},
                        {text: '取消，不要移动任务', value: 0}
                    ], 
                    function(selected){
                        switch(parseInt(selected.value)){
                            case 0:
                                
                            break;
                            case 1:
                                task.ConstraintDate = finish;
                                callback();
                            break;
                            case 2:
                                task.ConstraintType = 0;
                                task.ConstraintDate = null;
                                callback();
                            break;
                        }
                        
                    }
                );    
                return;
            }
            if(ctype == 4 && sdateTime < cdateTime){     //不得早于...开始
                var text = '您将任务 '+task.Name+' 的开始日期移到 '+startStr+' ,这位于本任务限制的日期<br/>('+cdateStr+')之前';
                Edo.project.Project.showConstraintWindow('规划向导', text, 
                    [
                        {text: '继续，移动任务并修改限制日期', value: 1},
                        {text: '继续，移动任务并取消限制', value: 2},
                        {text: '取消，不要移动任务', value: 0}
                    ], 
                    function(selected){
                        switch(parseInt(selected.value)){
                            case 0:
                                
                            break;
                            case 1:
                                task.ConstraintDate = start;
                                callback();
                            break;
                            case 2:
                                task.ConstraintType = 0;
                                task.ConstraintDate = null;
                                callback();
                            break;
                        }
                        
                    }
                );            
                return;
            }
            if(ctype == 5 && sdateTime > cdateTime){     //不得晚于...开始
                var text = '您将任务 '+task.Name+' 的开始日期移到 '+startStr+' ,这位于本任务限制的日期<br/>('+cdateStr+')之后';
                Edo.project.Project.showConstraintWindow('规划向导', text, 
                    [
                        {text: '继续，移动任务并修改限制日期', value: 1},
                        {text: '继续，移动任务并取消限制', value: 2},
                        {text: '取消，不要移动任务', value: 0}
                    ], 
                    function(selected){
                        switch(parseInt(selected.value)){
                            case 0:
                                
                            break;
                            case 1:
                                task.ConstraintDate = start;
                                callback();
                            break;
                            case 2:
                                task.ConstraintType = 0;
                                task.ConstraintDate = null;
                                callback();
                            break;
                        }
                        
                    }
                );      
                return;
            }
            if(ctype == 6 && fdateTime < cdateTime){     //不得早于...完成            
                var text = '您将任务 '+task.Name+' 的完成日期移到 '+finishStr+' ,这位于本任务限制的日期<br/>('+cdateStr+')之前';
                Edo.project.Project.showConstraintWindow('规划向导', text, 
                    [
                        {text: '继续，移动任务并修改限制日期', value: 1},
                        {text: '继续，移动任务并取消限制', value: 2},
                        {text: '取消，不要移动任务', value: 0}
                    ], 
                    function(selected){
                        switch(parseInt(selected.value)){
                            case 0:
                                
                            break;
                            case 1:
                                task.ConstraintDate = finish;
                                callback();
                            break;
                            case 2:
                                task.ConstraintType = 0;
                                task.ConstraintDate = null;
                                callback();
                            break;
                        }
                        
                    }
                );    
                return;
            }
            if(ctype == 7 && fdateTime > cdateTime){     //不得晚于...完成
                var text = '您将任务 '+task.Name+' 的完成日期移到 '+finishStr+' ,这位于本任务限制的日期<br/>('+cdateStr+')之后';
                Edo.project.Project.showConstraintWindow('规划向导', text, 
                    [
                        {text: '继续，移动任务并修改限制日期', value: 1},
                        {text: '继续，移动任务并取消限制', value: 2},
                        {text: '取消，不要移动任务', value: 0}
                    ], 
                    function(selected){
                        switch(parseInt(selected.value)){
                            case 0:
                                
                            break;
                            case 1:
                                task.ConstraintDate = finish;
                                callback();
                            break;
                            case 2:
                                task.ConstraintType = 0;
                                task.ConstraintDate = null;
                                callback();
                            break;
                        }
                        
                    }
                );
                return;
            }
        }
        if(callback) callback();
    }    
});