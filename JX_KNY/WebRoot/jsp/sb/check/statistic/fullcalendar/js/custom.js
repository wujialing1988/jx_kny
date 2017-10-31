$(window).resize(function() {
	// chosen resize bug
	"use strict";
	$('.chzn-container').each(function() {
		$(this).css('width', $('.chzn-container').parent().width()+'px');
		$(".chzn-drop").css('width', ($('.chzn-container').parent().width()-2)+'px');
		$(".chzn-search input").css('width', ($('.chzn-container').parent().width()-37)+'px');
	});
});
$(document).ready(function() {
	"use strict";
	// ------ DO NOT CHANGE ------- //
	$(".flot-bar,.flot-pie,.flot,.flot-multi").bind("plothover", function (event, pos, item) {
		if (item) {
			var y;
			if(event.currentTarget.className === 'flot flot-bar'){
				y = Math.round(item.datapoint[1]);
			} else if(event.currentTarget.className === 'flot flot-pie') {
				y = Math.round(item.datapoint[0])+"%";
			} else if(event.currentTarget.className === 'flot flot-line'){
				y = (Math.round(item.datapoint[1] * 1000)/1000);
			} else {
				y = (Math.round(item.datapoint[1]*1000)/1000)+"€";
			}
			$("#tooltip").remove();
			showTooltip(pos.pageX, pos.pageY,"Value = "+y);
		}
		else {
			$("#tooltip").remove();         
		}
	});

	function showTooltip(x, y, contents) {
		$('<div id="tooltip">' + contents + '</div>').css( {
			top: y + 5,
			left: x + 10
		}).appendTo("body").show();
	}
	$('.deleteRow').click(function(e){
		e.preventDefault();
		$(this).parents('tr').fadeOut();
	});

	$(".animateRow").click(function(e){
		e.preventDefault();
		var el = $(this).parents('tr');
		var target = $($(this).data('target'));
		var defaultColor = target.find('a.dropdown-toggle').css('color');
		var titleindex = parseInt($(this).data('title'), 10)-1;
		var userindex = parseInt($(this).data('user'), 10)-1;
		var dateindex = parseInt($(this).data('date'), 10)-1;
		var title = el.find('td:eq('+titleindex+')').html();
		var user = el.find('td:eq('+userindex+') a').html();
		var userContent = el.find('td:eq('+userindex+') a').data('content');
		var date = el.find('td:eq('+dateindex+')').html();
		el.css({
			position:'absolute',
			left:el.position().left,
			top:el.position().top
		});
		el.animate({
			left:target.position().left,
			top:target.position().top,
			width:target.width(),
			height:target.height()
		}, 1000, function(){
			el.hide();
			var value = parseInt(target.find('a.dropdown-toggle .label').html(), 10);
			if(isNaN(value)){
				value = 0;
			}
			target.find('a.dropdown-toggle .label').html(value+1);
			if(target.find('.label').is(":hidden")){
				target.find('.label').show();
			}
			target.find('a.dropdown-toggle').stop().animate({
				backgroundColor:target.find('a.dropdown-toggle .label').css('backgroundColor'),
				color:'#fff'
			},300, function(){
				target.find('a.dropdown-toggle').animate({
				backgroundColor:target.css('background-color'),
				color:defaultColor
			}, 200, function(){
				target.find('a.dropdown-toggle').css('background-color', '').css('color', '');
			});
			});
		});
		target.find('.dropdown-menu').append('<li class="custom"><div class="title">'+title+'<span>'+date+' by <a href="#" class="pover" data-title="'+user+'" data-content="'+userContent+'">'+user+'</a></span></div><div class="action"><div class="btn-group"><a href="#" class="tip btn btn-mini" title="Show order"><img src="img/icons/fugue/magnifier.png" alt=""></a><a href="#" class="tip btn btn-mini" title="Delete order"><img src="img/icons/fugue/cross.png" alt=""></a></div></div></li>');
		$(".pover").popover();$(".tip").tooltip();
	});
	$(".mini > li > a").hover(function(e){
	e.stopPropagation();
	if(!$(this).parent().hasClass("open")){
		$(this).find(".label").stop().animate({
			top: '-10px'
		},200, function(){
			$(this).stop().animate({top: '-6px'},100);
		});
	}
	}, function(){});


	$('.main-nav > li.active > a').click(function(e){
		if($(window).width() <= 767){
			e.preventDefault();
			if($(this).hasClass('open') && (!$(this).hasClass('toggle-collapsed'))){
				$(this).removeClass('open');
				$(this).parents('.main-nav').find('li').each(function(){
					$(this).find('.collapsed-nav').addClass('closed');
					$(this).hide();
				});
				$(this).parent().show();
				$(this).parent().removeClass('open');
			} else {
				if($(this).hasClass('toggle-collapsed')){
					$(this).parent().addClass('open');
				}
				if($(this).hasClass("open")){
					$(this).parents('.main-nav').find('li').not('.active').each(function(){
						$(this).find('.collapsed-nav').addClass('closed');
						$(this).hide();
					});
					$(this).removeClass("open");
				} else {
					$(this).addClass('open');
					$(this).parents('.main-nav').find('li').show();
				}
			}
		}
	});

	$('.toggle-collapsed').each(function(){
		if($(this).hasClass('on-hover')){
			$(this).click(function(e){e.preventDefault();});
			$(this).parent().hover(function(){
				$(this).addClass("open");
				$(this).find('.collapsed-nav').slideDown();
				$(this).find('img').attr("src", 'img/toggle-subnav-up-white.png');
			}, function(){
				$(this).removeClass("open");
				$(this).find('.collapsed-nav').slideUp();
				$(this).find('img').attr("src", 'img/toggle-subnav-down.png');
			});
		} else {
			$(this).click(function(e){
				e.preventDefault();
				if($(this).parent().find('.collapsed-nav').is(":visible")){
					$(this).parent().removeClass("open");
					$(this).parent().find('.collapsed-nav').slideUp();
					$(this).find('img').attr("src", 'img/toggle-subnav-down.png');
				} else {
					$(this).parent().addClass("open");
					$(this).parent().find('.collapsed-nav').slideDown();
					$(this).find('img').attr("src", 'img/toggle-subnav-up-white.png');
				}
			});
		}
	});

	$('.collapsed-nav li a').hover(function(){
		if(!$(this).parent().hasClass('active')){
			$(this).stop().animate({
				marginLeft: '5px'
			}, 300);
		}
	}, function(){
	$(this).stop().animate({
			marginLeft: '0'
		}, 100);
	});
	$('a.preview').live('mouseover mouseout mousemove click',function(e){
			if(e.type === 'mouseover'){
				$('body').append('<div id="image_preview"><img src="'+$(this).attr('href')+'" width="150"></div>');
				$("#image_preview").fadeIn();
			} else if(e.type === 'mouseout') {
				$("#image_preview").remove();
			} else if(e.type === 'mousemove'){
				$("#image_preview").css({
					top:e.pageY+10+"px",
					left:e.pageX+10+"px"
				});
			} else if(e.type === 'click'){
				$("#image_preview").remove();
			}
		});

	$('.sel_all').click(function(){
		$(this).parents('table').find('.selectable-checkbox').attr('checked', this.checked);
	});
	// ------ END DO NOT CHANGE ------- //

	// ------ PLUGINS ------- //
	// - CALENDAR
//	var date = new Date();
//	var d = date.getDate();
//	var m = date.getMonth();
//	var y = date.getFullYear();

	var yearAndMonth = queryDate.split('-');
	
	if($('.calendar').length > 0){
		$('.calendar').fullCalendar({
			header: {
				//left: 'prev,next,today',
				left: '',
				center: 'title',
				//right: 'month,agendaWeek,agendaDay'
				right: ''
			},
			titleFormat:{month:"yyyy年MM月",week:"yyyy年MM月d[yyyy]{ '&#8212;' [MM]d日}",day:"dddd, yyyy年MM月dd日"},
			monthNames:["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"],
			monthNamesShort:["一","二","三","四","五","六","七","八","九","十","十一","十二"],
			dayNames:["星期天","星期一","星期二","星期三","星期四","星期五","星期六"],
			dayNamesShort:["周日","周一","周二","周三","周四","周五","周六"],
//			buttonText:{
//				today:'今天',
//				month:'月',
//				agendaWeek:'周',
//				agendaDay:'天'
//			},
			height:360,
			//contentHeight:400,
			//width:100,
			//aspectRatio:0.8, // 宽与高的比例
			weekMode:'variable',
			//weekends:false, //是否显示周末
			editable: false,
			/*events: [
			{
				id: 888,
				title: '整天都是会议',
				start: new Date(y, m, 1)
			},
			{
				title: '4天都是会议',
				start: new Date(y, m, d-5),
				end: new Date(y, m, d-2)
			},
			{
				id: 999,
				title: 'Repeating Event',
				start: new Date(y, m, d-3, 16, 0),
				allDay: false
			},
			{
				id: 999,
				title: 'Repeating Event',
				start: new Date(y, m, d+4, 16, 0),
				allDay: false
			},
			{
				title: '会议中',
				start: new Date(y, m, d, 10, 30),
				allDay: false
			},
			{
				title: '会议名称长了是怎么显示的呢',
				start: new Date(y, m, d, 12, 0),
				end: new Date(y, m, d, 14, 0),
				allDay: false
			},
			{
				title: 'Birthday Party',
				start: new Date(y, m, d+1, 19, 0),
				end: new Date(y, m, d+1, 22, 30),
				allDay: false
			},
			{
				title: '可以加链接',
				start: '2016-04-25',
				end: new Date(y, m, 29),
				url: '../../google.com/default.htm'
			}
			]*/
			year: yearAndMonth[0],
			month: yearAndMonth[1] - 1,
			events: ctx + '/pointCheck!queryToStatistic.action?equipmentCode=' + equipmentCode + '&queryDate=' + queryDate,
			eventClick: function (event) {
				if (!event.url) {
					//console.log(event.id);
					
				}
			},
			eventAfterRender:function(event, element, view){//debugger;
				//$(element).css('width', '50px');
				//view.setWidth(400);
			}
		});
	}
});