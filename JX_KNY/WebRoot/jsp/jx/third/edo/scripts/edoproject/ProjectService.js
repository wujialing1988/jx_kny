var ProjectServiceUrl = 'EdoService/EdoProject.jsp';
var ProjectService = {
    dataUrl: ProjectServiceUrl,
    
    get: function(params, successFn, failFn){
        params = Edo.apply({
            method: 'get',
            project: 1
        }, params);
        
        Edo.util.Ajax.request({            
            url: this.dataUrl,
            params: params,
            onSuccess: function(text){  
                var o = Edo.util.JSON.decode(text);
                if(o.error == 0){
                    if(successFn) successFn(o.result);
                }else{
                    if(failFn) failFn(o.errormsg, o.error);    
                }                
            },
            onFail: function(err){
                if(failFn) failFn('网络错误', err);
            }
        });
    },
    set: function(params, successFn, failFn){
        params = Edo.apply({
            method: 'set'
        }, params);
        
        Edo.util.Ajax.request({            
            url: this.dataUrl,
            type: 'post',
            params: params,
            onSuccess: function(text){
                var o = Edo.util.JSON.decode(text);
                
                if(o.error == 0){
                    if(successFn) successFn(o.result);
                }else{
                    if(failFn) failFn(o.errormsg, o.error);    
                }     
            },
            onFail: function(err){
                if(failFn) failFn(err);
            }
        });
    }
};
function loadDataProject(data, project){
    var dataProject = new Edo.data.DataProject(data);   
    dataProject.set('plugins', [
        new ProjectSchedule()                       
    ]);
    project.set('data', dataProject);
}
function loadProject(ProjectUID, project){
    Edo.MessageBox.loading('正在加载项目数据，请稍后......');
    ProjectService.get(
        {
            project: ProjectUID     //传递一个项目ID(数据库内已保存的)
        },  
        function(data){
            var dataProject = new Edo.data.DataProject(data);               
            
            dataProject.set('plugins', [
                new ProjectSchedule()                       
            ]);
                        
            project.set('data', dataProject);            
            Edo.MessageBox.hide();
        }, 
        function(msg, err){
            //alert("甘特图数据加载失败!");
            alert(msg);
            
            Edo.MessageBox.hide();
        }
    );
}
function loadXML(url, project, callback){
    Edo.MessageBox.loading('正在加载项目数据，请稍后......');
    Edo.util.Ajax.request({
        url: url,
        onSuccess: function(text){                                                    
            var jsObj = Edo.util.XML.decode(text);
            var data = jsObj.Project;
            
            var dataProject = new Edo.data.DataProject(data);
            dataProject.set('plugins', [
                new ProjectSchedule()                       
            ]);                
            
            project.set('data', dataProject);
                                    
            if(callback) callback();
            
            Edo.MessageBox.hide();                
            
                 
        },
        onFail: function(err){
            alert("导入失败,错误码:"+err);
            Edo.MessageBox.hide();
        }
    });  
}
function loadJSON(url, project, callback){
    Edo.MessageBox.loading('正在加载项目数据，请稍后......');
    Edo.util.Ajax.request({
        url: url,
        onSuccess: function(text){                                                    
            var jsObj = Edo.util.JSON.decode(text);
            var data = jsObj.result;
            
            var dataProject = new Edo.data.DataProject(data);
            dataProject.set('plugins', [
                new ProjectSchedule()                       
            ]);                
            
            project.set('data', dataProject);
                                    
            if(callback) callback();
            
            Edo.MessageBox.hide();            
        },
        onFail: function(err){
            alert("导入失败,错误码:"+err);
            Edo.MessageBox.hide();
        }
    });  
}

function saveProject(dataProject, callback){
    Edo.MessageBox.saveing('保存', '正在保存项目数据,请稍等...');
                                            
    var json = dataProject.toJson();
    
    ProjectService.set({
        project: json
    },function(text){
        //更新成功,则将返回的ProjectUID设置给project.data
        dataProject.UID = text;
        Edo.MessageBox.hide();
        
        if(callback) callback(dataProject);
    }, function(msg, code){
        alert(msg);
        Edo.MessageBox.hide();
    });
}
function deleteProject(ProjectUID, success, fail){
    var o = {
        projectUID: ProjectUID,
        method: 'deleteproject'
    }
    Edo.util.Ajax.request({
        url: ProjectServiceUrl,
        params: o,
        onSuccess: function(text){        
            var data = Edo.util.JSON.decode(text);
            if(success) success(data);            
        },
        onFail: function(code){
            if(fail) fail(code);
        }
    });
}
function checkSaveProject(dataProject, callback){
    /*if(!project.data.UID){
        if(confirm("当前项目未保存,是否保存到数据库?")){
            saveProject(project.data, function(){
                if(callback) callback();
            });            
        }
    }else if(project.data.Tasks.changed){
        if(confirm("当前项目已被修改,是否保存到数据库?")){
            saveProject(project.data, function(){
                if(callback) callback();
            });
        } 
    }*/
    return true;
}
function loadProjects(o, success, fail){
    o.method = 'getprojects';
    Edo.util.Ajax.request({        
        url: ProjectServiceUrl,
        params: o,
        onSuccess: function(text){        
            var data = Edo.util.JSON.decode(text);
            if(success) success(data);            
        },
        onFail: function(code){
            if(fail) fail(code);
        }
    });
}
function downloadProject(o, success, fail){
    Edo.MessageBox.saveing('下载', '正在下载项目文件,请稍等...');
    
    o.method = 'savefile';
    o.filetype = o.filetype || 'mpp';
    
    var url = '';
    
    Edo.util.Ajax.request({        
        type: 'post',
        async: false,
        url: ProjectServiceUrl,
        params: o,        
        onSuccess: function(text){
            url = ProjectServiceUrl+"?method=download&file="+text;
            Edo.MessageBox.hide();
        },
        onFail: function(code){            
            alert("下载失败:"+code);            
            Edo.MessageBox.hide();
        }
    });
    var win = window.open(url,'_blank', 'height=1,width=1,top=200,left=300'); 
    setTimeout(function(){
        win.close();
    }, 1500);
}

function loadDynamicProject(projectUID, project){
    //给动态甘特图, 增加数据操作插件(服务端交互)
    var dynamicSchedule = new DynamicProjectSchedule();
    dynamicSchedule.project = project;
    project.data.set('plugins', [
        dynamicSchedule
    ]);
    //动态甘特图显示服务URL地址
    project.loadParams.url = 'EdoService/DynamicProject.jsp';
    //项目UID
    project.loadParams.ProjectUID = projectUID;
    //如果需要传递更多参数, 可以扩展loadParams属性
    
    project.load();
}