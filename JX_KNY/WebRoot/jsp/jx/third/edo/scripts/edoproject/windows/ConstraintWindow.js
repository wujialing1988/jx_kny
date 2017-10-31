//任务限制面板
Edo.project.Project.showConstraintWindow = function(title, content, data, callback){
    var id = 'projectmultiselect' + Edo.id();
    Edo.MessageBox.show({
        padding: 5,
        //height: 250,
        buttons: Edo.MessageBox.OKCANCEL,
        title: '规划向导',        
        callback: function(action){
            if(action == 'ok'){
                var selected = Edo.getCmp(id).selected;
                if(callback) callback(selected);
            }
        },
        children: [
            {
                type: 'box',
                border: 0,
                padding: 0,
                style: 'background-color:Transparent;',
                children: [
                    {
                        type: 'label',
                        text: content
                    },
                    {
                        type: 'label',
                        text: '您可以：'
                    },
                    {
                        id: id,
                        type: 'multiselect',
                        width: '100%',
//                        minHeight: 80,
//                        defaultHeight: 80,
                        rowSelectMode: 'single',
                        data: data
                    }
                ]
            }                    
        ]
    });
    
    Edo.getCmp(id).select(0);
}