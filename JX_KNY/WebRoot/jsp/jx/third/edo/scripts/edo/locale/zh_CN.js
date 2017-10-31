Edo.apply(Edo.controls.DatePicker.prototype, {
    weeks: ['日','一','二','三','四','五','六'],
    yearFormat: 'Y年',
    monthFormat: 'm月',
    todayText: '今天',
    clearText: '清楚'
});
Edo.apply(Edo.MessageBox, {
    buttonText : {
        ok : "确定",
        cancel : "取消",   
        yes : "是",
        no : "否"
    },
    saveText: '保存中...'
});

if(Edo.data.DataGantt){
    Edo.apply(Edo.data.DataGantt, {
        PredecessorLinkType: [
            {ID: 0, Name: '完成-完成(FF)', EName: 'FF', Date: ['Finish', 'Finish']},
            {ID: 1, Name: '完成-开始(FS)', EName: 'FS', Date: ['Finish', 'Start']},
            {ID: 2, Name: '开始-完成(SF)', EName: 'SF', Date: ['Start', 'Finish']},
            {ID: 3, Name: '开始-开始(SS)', EName: 'SS', Date: ['Start', 'Start']}
        ],
        PredecessorLinkTypeMap: {
            FF: 0,
            FS: 1,
            SF: 2,
            SS: 3
        },
        dontUpgrade: '所选定的任务已经是最高级别大纲,不能再升级了',
        dontDowngrade: '不能降级'
    });
}
if(Edo.lists.Gantt){
    Edo.apply(Edo.lists.Gantt, {
        yearText: '',
        monthText: '',
        weekText: '',
        dayText: '',
        hourText: '',
        
        scrollDateFormat: 'Y-m-d 星期l',
        
        No: '标识号：',
        name: '名称：',   
        
        summaryText: '摘要',
        milestoneText: '里程碑',
        criticalText: '关键',
        taskText: '计划任务',
        baselineText: '实际任务',
        percentCompleteText: '进度',
        
        startText: '开始日期',
        finishText: '截止日期',
        tipDateFormat: 'Y年m月d日 H时i分',
        
        linktaskText: '任务链接',
        delaytimeText: '延隔时间',
        fromText: '从',
        toText: '到',        
        
        hourFormat: 'Y-m-d 星期l',
        weekFormat: 'Y-m-d 星期l',
        monthFormat: 'Y年m月',
        quarterFormat: 'Y年m月 - ',
        yearFormat: 'Y年',
        
        quarterformat2: '{0}年 第{1}季度',
        monthFormat2: 'Y年 - m月',
        
        gotoTask: '转到任务',
        upgradeTaskText: '升级',
        downgradeTaskText: '降级',
        addTask: '新增任务',
        editTask: '修改任务',
        deleteTask: '删除任务',
        
        trackText: '跟踪',
        progressLine: '进度线',    
        createbaseline: '设置比较基准',
        clearbaseline: '清除比较基准',
        viewAreaText: '视图显示区',
        showTreeAndGantt: '任务树和条形图',
        showTreeOnly: '只显示任务树',
        showGanttOnly: '只显示条形图',
        
        timeLine: '日期模式',
        defaultDateText: '日期 : 周/天',
        yearQuarter: '年/季',
        yearMonth: '年/月',
        yearWeek: '年/周',
        yearDay: '年/天',
        quarterMonth: '季/月',
        quarterWeek: '季/周',
        quarterDay: '季/天',
        monthWeek: '月/周',
        monthDay: '月/天',
        weekDay: '周/天',
        weekHour: '周/时',
        dayHour: '日/时',
        hourMinute: '时/分',
        
        viewText: '视图',
        ganttView: '甘特图',
        trackView: '跟踪甘特图',
        
        selectTask: '请先选择一个任务'    
    });
    Edo.apply(Edo.lists.Gantt.prototype, {
        weeks: ['日','一','二','三','四','五','六']
    });
}
if(Edo.project && Edo.project.Project){
    Edo.apply(Edo.project.Project,  {   
        statusColumnText1: '此任务在 {0} 完成',
        statusColumnText2: '此任务有 \"{0}\" 限制,日期 {1}',
        statusColumnText3: '备注',
    
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
        predecessorLinkIDInvalid: '前置任务ID号无效',
        
        //列
        nameColumn: '任务名称',
        durationColumn: '工期',
        workColumn: '工时',
        percentCompleteColumn: '进度',
        startColumn: '开始时间',
        finishColumn: '完成时间',
        predecessorLinkColumn: '前置任务',
        resourceColumn: '资源名称',
        
        departmentColumn: '部门',
        principlColumn: '负责人',
        actualdurationColumn: '实际工期',
        actualstartColumn: '实际开始日期',
        actualfinishColumn: '实际完成日期',
        weightColumn: '权重',
        plan: '计划',
        actual: '实际'
    });
}
if(Edo.project && Edo.data.DataProject){
    Edo.apply(Edo.data.DataProject, {
        yearText: '年',
        monthText: '月',
        weekText: '周',
        dayText: '日',
        hourText: '时',
        
        standardCalendar : '标准',
        
        //任务类型
        TaskType: [
            {ID: 0, Name: '固定单位'},
            {ID: 1, Name: '固定工期'},
            {ID: 2, Name: '固定工时'}
        ],
        //任务限制类型
        ConstraintType: [
            {ID: 0, Name: '越早越好'},
            {ID: 1, Name: '越晚越好'},
            {ID: 2, Name: '必须开始于'},
            {ID: 3, Name: '必须完成于'},    
            {ID: 4, Name: '不得早于...开始'},
            {ID: 5, Name: '不得晚于...开始'},
            {ID: 6, Name: '不得早于...完成'},
            {ID: 7, Name: '不得晚于...完成'}
        ],
        //资源类型
        ResourceType: [
            {ID: 0, Name: '材料'},
            {ID: 1, Name: '工时'},
            {ID: 2, Name: '成本'}
        ]
    });
}