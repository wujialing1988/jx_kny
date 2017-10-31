
Ext.onReady(function(){
	
	Ext.namespace('ImgDataView');//定义命名空间
	
	ImgDataView.ImgDataViewStore = new Ext.data.JsonStore({
	    url: 'get-images.php',
	    root: 'images',
	    fields: [
	        'name', 'url',
	        {name:'size', type: 'float'},
	        {name:'lastmod', type:'date', dateFormat:'timestamp'}
	    	]
	});

	ImgDataView.ImgDataViewTpl = new Ext.XTemplate(
	    '<tpl for=".">',
	        '<div class="thumb-wrap" id="{name}">',
	        '<div class="thumb"><img src="{url}" title="{name}"></div>',
	        '<span class="x-editable">{shortName}</span></div>',
	    '</tpl>',
	    '<div class="x-clear"></div>'
	);

    ImgDataView.DataItems = new Ext.DataView({
        store: ImgDataView.ImgDataViewStore,
        tpl: ImgDataView.ImgDataViewTpl,
        autoHeight:true,
        multiSelect: true,
        overClass:'x-view-over',
        itemSelector:'div.thumb-wrap',
        emptyText: 'No images to display'
    });
});