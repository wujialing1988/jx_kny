ProjectToolBar = function(){
    ProjectToolBar.superclass.constructor.call(this);
};   
ProjectToolBar.extend(Edo.containers.Box, {
    project: null,
    init: function(){
        ProjectToolBar.superclass.init.call(this);
        var project = this.project;
        this.set('children', [
            {
                type: 'box',width: '100%',cls: 'e-toolbar',horizontalGap: 0,verticalAlign: 'middle',layout: 'horizontal',
                padding:2,
                children: [
                    {
                    	type: 'button',id: 'dateviewBtn1',text: '显示方式：只显示任务树',arrowMode: 'menu',
                        menu: [{
                        	type: 'button',text: '只显示任务树',
                            onclick: function(e){ 
//                                project.tree.set('autoColumns', true);
						        project.gantt.parent.set('visible', false);    
						        project.tree.set('verticalScrollPolicy', 'auto');						        
						        project.tree.parent.set('visible', true);
						        dateviewBtn.set('visible', false);
						        dateviewBtn1.set('text', '显示方式：'+this.text + "&nbsp;&nbsp;&nbsp;&nbsp;");
                            }
                        
                        },{
                        	type: 'button',text: '只显示条形图',
                            onclick: function(e){ 
                                project.tree.parent.set('visible', false);        
//						        project.tree.set('autoColumns', false);
						        project.gantt.parent.set('visible', true);    
						        project.tree.set('verticalScrollPolicy', 'off');
						        dateviewBtn.set('visible', true);
						        dateviewBtn1.set('text', '显示方式：'+this.text + "&nbsp;&nbsp;&nbsp;&nbsp;");
                            }
                        
                        },{
                        	type: 'button',text: '全部显示',
                            onclick: function(e){ 
//                                project.tree.set('autoColumns', false);
						        project.gantt.parent.set('visible', true);  						        
						        project.tree.parent.set('visible', true);
						        dateviewBtn.set('visible', true);
						        dateviewBtn1.set('text', '显示方式：'+this.text + "&nbsp;&nbsp;&nbsp;&nbsp;");
                            }
                        
                        }]
                    },
                	{
                        type: 'button',id: 'dateviewBtn',text: '日期 ：天/时',arrowMode: 'menu', visible: false,
                        menu: [
                            {
                                type: 'button',text: '年/季',
                                onclick: function(e){
                                    project.gantt.set('dateView', 'year-quarter');
                                    dateviewBtn.set('text', '日期 : '+this.text + "&nbsp;&nbsp;&nbsp;&nbsp;");
                                }
                            },{
                                type: 'button',text: '年/月',
                                onclick: function(e){                                
                                    project.gantt.set('dateView', 'year-month');                                    
                                    dateviewBtn.set('text', '日期 : '+this.text + "&nbsp;&nbsp;&nbsp;&nbsp;");
                                }
                            },{
                                type: 'button',text: '年/天',
                                onclick: function(e){
                                    project.gantt.set('dateView', 'year-day');
                                    dateviewBtn.set('text', '日期 : '+this.text + "&nbsp;&nbsp;&nbsp;&nbsp;");
                                }
                            },{
                                type: 'button',text: '月/天',
                                onclick: function(e){
                                    project.gantt.set('dateView', 'month-day');
                                    dateviewBtn.set('text', '日期 : '+this.text + "&nbsp;&nbsp;&nbsp;&nbsp;");
                                }
                            },{
                                type: 'button',text: '周/天',
                                onclick: function(e){
                                    project.gantt.set('dateView', 'week-day');
                                    dateviewBtn.set('text', '日期 : '+this.text + "&nbsp;&nbsp;&nbsp;&nbsp;");
                                }
                            },{
                                type: 'button',text: '天/时',
                                onclick: function(e){
                                    project.gantt.set('dateView', 'day-hour');
                                    dateviewBtn.set('text', '日期 : '+this.text + "&nbsp;&nbsp;&nbsp;&nbsp;");
                                }
                            }
                        ]
                    }
                ]
            }
        ]);
    }
});