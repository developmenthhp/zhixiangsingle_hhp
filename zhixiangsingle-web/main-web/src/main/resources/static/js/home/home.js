var pathUri=window.location.href;

//橙色预警弹幕
var messagesJson0,messagesJson1,messagesJson2,messagesJson3;

//消息推送弹幕
var msg;
var msgArray = new Array(msg,msg,msg,msg);

var messagesJson;

var infoWin;
var tableDom;

var siteData;

var myMsgChagre;

var myImgPrefix;

var choseLength = 0;

var sdIdArray = [];

var fruits;

var fileMaxSize = accMul(10,1024);

var myChart;

var allDate = [];

var dataTow2 = [];

var allFoodName = [];

var dataViewBottom;
var oldDateBottom = [];
$(function(){

    $(".jvectormap-container").removeAttr("background-color");
    $(".jvectormap-container").css("background-color","transparent");

    if($("#teShiro").html()==""||$("#teShiro").html()==null){
        window.location.href="/login";
    }

    var admName = $("#teShiro").html();
    $("#showLAdm").html(admName);
    var s_time = dateFormat($("#teShiro2").html(),'yyyy-MM-dd HH:mm:ss');
    $("#headAdmDate").html(s_time+" 注册");

    $('#chatA').slimScroll({
        height: '250px'
    });
    $.post("/auth/getUserPerms",{},function(data){
        localStorage.setItem("roles",JSON.stringify(data));
        if(data.resultCode==100){
            getMenus(data);
            var paramUlHtml = $("#permUl").html();
            localStorage.setItem("leftMenuStorage",paramUlHtml);
            $("#permUl").children(":eq(0)").find("a").css("color","#FFD600");
        }else if(data.resultCode==101){
            showJqueryConfirmWindow("#icon-shangxin1",data.msg);
        }else{
            $.confirm({
                icon: 'fa fa-question',
                theme: 'modern',
                title: '权限数据获取异常',
                content: data.msg,
                animation: 'news',
                closeAnimation: 'news',
                autoClose: 'logoutUser|8',
                buttons: {
                    logoutUser: {
                        text: '重新登录',
                        action: function () {
                            //退出
                            window.location.href="/logout";
                        }
                    }/*,
                    cancel: function () {
                        //退出
                        window.location.href="/logout";
                    }*/
                }
            });
        }
    });

    var fixHelperModified = function(e, tr) {
            //children() 方法返回返回被选元素的所有直接子元素
            var $originals = tr.children();
            //clone() 方法生成被选元素的副本，包含子节点、文本和属性
            var $helper = tr.clone();
            //each() 方法规定为每个匹配元素规定运行的函数
            $helper.children().each(function(index) {
                //width() 方法返回或设置匹配元素的宽度
                //eq() 方法将匹配元素集缩减值指定 index 上的一个
                $(this).width($originals.eq(index).width())
            });
            return $helper;
        },
        updateIndex = function(e, ui) {
            //ui.item - 表示当前拖拽的元素
            //parent() 获得当前匹配元素集合中每个元素的父元素，使用选择器进行筛选是可选的
            $('td.index', ui.item.parent()).each(function(i) {
                //html() 方法返回或设置被选元素的内容 (inner HTML)
                $(this).html(i + 1);
            });
        };
    $("#sortCSYJ tbody").sortable({
        //设置是否在拖拽元素时，显示一个辅助的元素。可选值：'original', 'clone'
        helper: fixHelperModified,
        //当排序动作结束时触发此事件。
        stop: updateIndex
    }).disableSelection();

    var allDat = getUserSites();
    siteData = allDat.result;
    myImgPrefix = allDat.obj;

    dataViewBottom = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
    oldDateBottom = dataViewBottom;
    loadMyAlertMsg(false);

    writeTopLibPur();
    writeTopWarning();
    //加载待审签单数
    loadTrialSigning();
    //加载从业违规
    loadEmploymentViolation();
    //加载当前区域违规
    loadZoneAlarm();
    //加载食材快检数据
    loadFoodInspection();

    //加载菜谱
    loadMyDateTable();

    $("#emailFile").on("change",function () {
        var filePath=$("#emailFile").val();
        if(filePath){
            var arr=filePath.split('\\');
            var fileName=arr[arr.length-1];
            $("#myFileShowName").text(fileName);
        }
    });

    //监听窗口，显示隐藏move top
    $(window).scroll(function () {
        if($(window).scrollTop() >= 100){
            $('.toTopRocket').fadeIn(300);
        }else{
            $('.toTopRocket').fadeOut(300);
        }
    });

});

function consoleMyLog(params){
    if(params.deselected!=undefined){
        //删除某个站点了
        //替换起始下标为1，长度为1的一个值为‘ttt’
        /*var mySelArray = $("#alertSdId").val().split(",");
        var currentHideVal = mySelArray.splice(mySelArray.indexOf(params.deselected),1);
        $("#menuTypeId").find("option[value='"+params.deselected+"']").removeAttr("selected");
        $("#alertSdId").val(currentHideVal);*/
    }/*else if(params.selected!=undefined){
            //选择某个站点了
            alert(typeof params.selected);
        }*/
    var choseSiteIds = $("#menuTypeId").val();
    fruits = choseSiteIds;
    if(choseSiteIds!=undefined&&choseSiteIds!=null&&choseSiteIds!=""){
        var curSelVal = $("#menuTypeId").val().join(",");
        $("#alertSdId").val(curSelVal);
    }else if(choseSiteIds==null){
        $("#alertSdId").val("");
    }

}
//消息弹幕
function BarrageManager (options) {

    this.opts = {
        url       : '/fireSafetyInspection/getByMonth',
        loadDelay : 20000 ,  // 轮询时间间隔
    }

    $.extend( this.opts , options);
    this.bc = new BarrageCollection();
}

function BarrageCollection () {
    this.mq = [] ;
}

BarrageManager.prototype.loop = function () {
    var len = this.bc.mq.length , self = this  ;
    while (len--) {
        this.bc.mq[len].start(this.bc , len);
    }

    setTimeout(function () {
        self.load();
    } , this.opts.loadDelay);
}



BarrageCollection.prototype.add = function (barrage) {
    this.mq.push(barrage);
}

BarrageCollection.prototype.remove = function (barrage) {
    var index = this.mq.findIndex(function (item) {
        return barrage.opts.id == item.opts.id ;
    });
    if(index != -1) {
        this.mq.splice(index , 1);
    }
    barrage.opts.$el.remove();
}

function Barrage (options,k) {
    this.opts = {
        $el         : null ,
        left        : 0 ,
        bgColor     : [Math.floor(Math.random()*255),Math.floor(Math.random()*255),Math.floor(Math.random()*255)] ,
        offset      : 50 ,      // 使弹幕完全移出屏幕外
        duration    : 10000 ,   // 弹幕从右往左移动的时间
        delayTime   : 6000 ,    // 弹幕延迟移动时间
    };
    $.extend( this.opts , options);
    this.init(k);
}

Barrage.prototype.init = function (k) {

    this.opts.$el = $("<span onclick='moreLPH()' style='cursor: pointer;'><img src="+this.opts.icon+" style='width:45px;'>"+this.opts.text+"<em>"+this.opts.name+"</em></span>");

    var top = Math.ceil(Math.random() * 10 );
    this.opts.$el.css({
        top:top * 25 +'px',
        backgroundColor:"rgb("+this.opts.bgColor.join(",")+")"
    });



    var delay = Math.ceil(Math.random()*10);
    this.opts.delayTime *= Math.abs(delay - 5);

    var dur = Math.ceil(Math.random() * 10);
    this.opts.duration += dur * 1000 ;
    $('#barrage-msg'+k).append(this.opts.$el);

    this.opts.left = -this.opts.$el.width() - this.opts.offset ;
    //改版
    //this.opts.left = -this.parent().width() - this.opts.offset ;
}

Barrage.prototype.start = function (bc , index) {
    var self = this ;
    bc.mq.splice(index , 1);
    setTimeout(function () {
        self.move(bc);
    }, this.opts.delayTime);
}

Barrage.prototype.move = function (bc) {
    var self = this ;
    this.opts.$el.animate({
        left:this.opts.left+'px'
    } , this.opts.duration ,"linear" ,  function () {
        bc.remove(self);
    });
}
//加载安全检查数据
BarrageManager.prototype.load = function () {

    var self = this ;
    var sdIdMsg = $("#sdIdMsg").val();

    if(messagesJson==undefined||messagesJson==''||messagesJson=='undefined'){
        $.post(self.opts.url,{sdId:sdIdMsg}, function(data){

            if(data.code==1000){
                if(data.rows.length>0){
                    messagesJson = JSON.stringify(data);
                    $.each(data.rows,function(ind,val){
                        var cleanIsQualified = '';
                        if(val.cleanIsQualified==1){
                            cleanIsQualified = '清洁卫生合格、';
                        }else if(val.cleanIsQualified==2){
                            cleanIsQualified = '清洁卫生不合格、';
                        }
                        var isPlaceNeatly = '';
                        if(val.isPlaceNeatly==1){
                            isPlaceNeatly = '摆放整齐合格、';
                        }else if(val.isPlaceNeatly==2){
                            isPlaceNeatly = '摆放整齐不合格、';
                        }
                        var fireEngineAccess = '';
                        if(val.fireEngineAccess==1){
                            fireEngineAccess = '消防通道合格、';
                        }else if(val.fireEngineAccess==2){
                            fireEngineAccess = '消防通道不合格、';
                        }
                        var fireApplianceSarevalId = '';
                        if(val.fireApplianceSarevalId==1){
                            fireApplianceSarevalId = '消防器具合格、';
                        }else if(val.fireApplianceSarevalId==2){
                            fireApplianceSarevalId = '消防器具不合格、';
                        }
                        var fireBlanket = '';
                        if(val.fireBlanket==1){
                            fireBlanket = '灭火毯合格、';
                        }else if(val.fireBlanket==2){
                            fireBlanket = '灭火毯不合格、';
                        }
                        var theGasSwitch = '';
                        if(val.theGasSwitch==1){
                            theGasSwitch = '瓦斯开关合格、';
                        }else if(val.theGasSwitch==2){
                            theGasSwitch = '瓦斯开关不合格、';
                        }
                        var gasMonitor = '';
                        if(val.gasMonitor==1){
                            gasMonitor = '瓦斯监测合格';
                        }else if(val.gasMonitor==2){
                            gasMonitor = '瓦斯监测不合格';
                        }
                        var mySubContext = val.checkTime+" "+val.checkCategories+"："+cleanIsQualified+isPlaceNeatly+fireEngineAccess+fireApplianceSarevalId+fireBlanket+theGasSwitch+gasMonitor;
                        self.bc.add(new Barrage({
                            id:val.id,
                            name:"-"+val.checkPerson+"("+val.siteName+")",
                            text:mySubContext,
                            icon:val.sitePhoto ? val.sitePhoto : myDefaultImg+'/images/logo1.png'
                        },0));
                    });
                    /*$("#libPurHeadAllContext").viewer('update');
                    $('#libPurHeadAllContext').viewer({
                        url: 'data-original',
                    });*/
                    /*for(var k=0;k<4;k++){
                        $.each(data.data,function(ind,val){
                            self.bc.add(new Barrage({
                                id:val.id,
                                name:val.fromUserName,
                                text:val.content,
                                icon:val.fromUserIcon ? val.fromUserIcon : '../images/logo1.png'
                            },k));
                        });

                    }*/
                    self.loop();
                }else{

                }
            }
        });

    }else{

        var jsonDataMessage = JSON.parse(messagesJson);

        $.each(jsonDataMessage.rows,function(ind,val){
            var cleanIsQualified = '';
            if(val.cleanIsQualified==1){
                cleanIsQualified = '清洁卫生合格、';
            }else if(val.cleanIsQualified==2){
                cleanIsQualified = '清洁卫生不合格、';
            }
            var isPlaceNeatly = '';
            if(val.isPlaceNeatly==1){
                isPlaceNeatly = '摆放整齐合格、';
            }else if(val.isPlaceNeatly==2){
                isPlaceNeatly = '摆放整齐不合格、';
            }
            var fireEngineAccess = '';
            if(val.fireEngineAccess==1){
                fireEngineAccess = '消防通道合格、';
            }else if(val.fireEngineAccess==2){
                fireEngineAccess = '消防通道不合格、';
            }
            var fireApplianceSarevalId = '';
            if(val.fireApplianceSarevalId==1){
                fireApplianceSarevalId = '消防器具合格、';
            }else if(val.fireApplianceSarevalId==2){
                fireApplianceSarevalId = '消防器具不合格、';
            }
            var fireBlanket = '';
            if(val.fireBlanket==1){
                fireBlanket = '灭火毯合格、';
            }else if(val.fireBlanket==2){
                fireBlanket = '灭火毯不合格、';
            }
            var theGasSwitch = '';
            if(val.theGasSwitch==1){
                theGasSwitch = '瓦斯开关合格、';
            }else if(val.theGasSwitch==2){
                theGasSwitch = '瓦斯开关不合格、';
            }
            var gasMonitor = '';
            if(val.gasMonitor==1){
                gasMonitor = '瓦斯监测合格';
            }else if(val.gasMonitor==2){
                gasMonitor = '瓦斯监测不合格';
            }
            var mySubContext = val.checkTime+" "+val.checkCategories+"："+cleanIsQualified+isPlaceNeatly+fireEngineAccess+fireApplianceSarevalId+fireBlanket+theGasSwitch+gasMonitor;
            self.bc.add(new Barrage({
                id:val.id,
                name:"-"+val.checkPerson+"("+val.siteName+")",
                text:mySubContext,
                icon:val.sitePhoto ? val.sitePhoto : myDefaultImg+'/images/logo1.png'
            },0));
        });
        self.loop();
    }

}

/* 判断有没有权限查看其他 */
function loadMyAlertMsg(loadFlag) {

    if(loadFlag==true){
        messagesJson = undefined;
    }

    if(myMsgChagre==undefined){
        myMsgChagre = new BarrageManager();
    }

    $("#barrage-msg0").empty();

    myMsgChagre.load();

    //消息弹幕完
}

var getMenus=function(data){
    //回显选中
    var appUl = "";
    data = data.result;
    for(var i=0;i < data.length;i++){

        var node=data[i];

        if( node.isType==0){

            if(node.pid==0){
                var curIcon = "fa fa-asterisk";
                if(node.icon){
                    curIcon = "iconfont " + node.icon;
                }
                var ul=$("<li id='liParent"+node.id+"' class='treeview' style='border: none;'><a href='javascript:void(0)' onclick='addActClass(this)'><i class='"+curIcon+"'></i> <span>"+node.name+"</span><i class='fa fa-angle-left pull-right'></i></a></li>");
                //父级无page
                var secUl = $("<ul id='ulParent"+node.id+"' class='treeview-menu'></ul>");
                //获取子节点
                var childArry = getParentArry(node.id, data);
                if(childArry.length>0){

                    for (var y in childArry) {
                        var curSunIcon = "fa fa-angle-double-right";
                        if(childArry[y].icon){
                            curSunIcon = "iconfont " + childArry[y].icon;
                        }
                        var thirdLi = $("<li id='liParent"+childArry[y].id+"' style='border: none;'><a href='"+childArry[y].page+"'><i class='"+curSunIcon+"'></i>"+childArry[y].name+"</a></li>");

                        secUl.append(thirdLi);

                    }

                    ul.append(secUl);
                }

                $("#permUl").append(ul);
                appUl = appUl + ul.prop("outerHTML");


            }else{

                //获取子节点
                var childArry = getParentArry(node.id, data);

                if(childArry.length>0){
                    var ul=$("#liParent"+node.id);
                    var urlA = ul.find("a").eq(0);
                    urlA.attr("href","javascript:void(0)");
                    urlA.attr('onclick','showSecondHref('+node.id+')');
                    var secUl = $("<ul class='treeview-menu'></ul>");
                    $.each(childArry,function(ind,val){
                        var curSunSunIcon = "fa fa-stack-overflow";
                        if(val.icon){
                            curSunSunIcon = "iconfont " + val.icon;
                        }
                        var thirdLi = $("<li id='liParent"+val.id+"' style='border: none;'><a href='"+val.page+"'><i class='"+curSunSunIcon+"'></i>"+val.name+"</a></li>");
                        secUl.append(thirdLi);
                    });

                    ul.append(secUl);
                }

            }
        }
        //localStorage.setItem("leftMenuStorage",appUl);
        //$("#permUl").append(ul);
    }

    //console.log($("#permUl").html());
    //$("#permUl").append(ul);
}
/*function getAlert(){
    //获取橙色预警数据
    $.post("/alertSummary/getFirstAlert",{sdId:$("#alertSdId").val(),type:"android"},function(data){
        if(data.rows!=null){
            $("#firstAlertCSYJ").html("");
            $.each(data.rows,function(ind,val){
                $("#firstAlertCSYJ").append("<tr id='"+val.id+"-myStyleLi-"+val.sdId+"' onmouseover='showED("+val.id+","+val.sdId+")' onmouseleave='hideED("+val.id+","+val.sdId+")'>" +
                    "<td data-label='' class='comMove'>" +
                    "<span id='"+val.id+"-time-"+val.sdId+"' class='text'>"+val.time+"</span>" +
                    "</td>" +
                    "<td data-label='' class='comMove'>" +
                    "<span id='"+val.id+"-context-"+val.sdId+"'' class='text'>"+val.context+"</span>" +
                    "</td>" +
                    "<td data-label='操作'>" +
                    "<div class='tools' data-label='"+val.id+"-"+val.sdId+"' style='color: #f56954;display: none'>" +
                    "<i class='fa fa-edit' style='cursor: pointer' onclick='dealAlert("+val.sdId+","+val.id+")'></i>&nbsp;" +
                    "</div>" +
                    "</td>" +
                    "</tr>");
            });

        }
    });
}*/

function showED(cur,sdId) {
    $("div[data-label='"+cur+"-"+sdId+"']").css("display","block");
}

function hideED(cur,sdId) {
    $("div[data-label='"+cur+"-"+sdId+"']").css("display","none");
}

//根据菜单主键id获取下级菜单
//id：菜单主键id
//arry：菜单数组信息
function getParentArry(id, arry) {

    var newArry = new Array();

    for (var x in arry) {

        if (arry[x].pid == id)
            newArry.push(arry[x]);
    }
    return newArry;
}
function updateUsePwd(){
    layer.open({
        type:1,
        title: "修改密码",
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['450px'],
        content:$('#useDetail')
    });
}


function dealAlert(sdId,id){

}

//加载未审签单总数
function loadTrialSigning() {

    $.post("/trialSigning/getTotalTrialSigning",{},function(data){
        if(data.resultCode==100){
            //刷新数据
            $("#trialSigning").text(data.data);
            //showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",data.msg);
        }
    });
}

//加载从业违规
function loadEmploymentViolation(){
    $.post("/employmentViolation/getTotalEmploymentViolation",{},function(data){
        if(data.code==1000){
            //刷新数据
            $("#employmentViolation").text(data.data);
            //showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",data.msg);
        }
    });
}

//加载当日警报
function loadZoneAlarm(){
    $.post("/zoneAlarm/getTotalZoneAlarm",{},function(data){
        if(data.code==1000){
            //刷新数据
            $("#zoneAlarm").text(data.data);
            //showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",data.msg);
        }
    });
}

//加载本周食材检测记录数据
function loadFoodInspection() {
    $.post("/foodInspection/getByWeek",{},function(data){
        if(data.code==1000){
            /*$("#foodInspection").html("");*/

            $.each(data.rows,function (ind,val) {
                var handleContext = '';
                var checkStatus = '';
                var myTextStyle = '';
                var smallStyle = '';
                if(val.status==2){
                    //数据库未更新，更新后修改回来。。。。
                    /*handleContext = ',已由'+val.handler+'于'+val.handlingTime+"处理";*/
                    checkStatus = 'checked';
                    myTextStyle = 'doneText';
                    smallStyle = 'doneSmal';
                }
                var id = "myFInsLi"+ind;//'由'+val.supplierName+
                var showContext ='供应的'+val.foodStuffName+'检测'+val.inspectionName+'结果呈'+val.mstContext+handleContext+'-'+val.siteName+'&nbsp;&nbsp;'+val.mstDate;
               $("#foodInspection").append('<li id='+id+' style="white-space:nowrap;overflow:hidden;text-overflow:ellipsis;" data-toggle="tooltip" title="'+showContext+'">' +
                   '<input class="myDisabledCheckBox" type="checkbox" '+checkStatus+'/>' +
                   '<span class="text '+myTextStyle+'">由供应的'+val.foodStuffName+'检测'+val.inspectionName+'结果呈'+val.mstContext+handleContext+'-'+val.siteName+'</span>' +
                   '<small class="label label-danger '+smallStyle+'"><i class="fa fa-clock-o"></i>'+val.mstDate+'</small>' +
                   '</li>');
                if(ind<3){
                    $("#"+id).tooltip({
                        effect:'toggle',
                        cancelDefault: true,
                        placement:'bottom'
                    });
                }else{
                    $("#"+id).tooltip({
                        effect:'toggle',
                        cancelDefault: true
                    });
                }

            });

            //重新加载样式
            $(".myDisabledCheckBox").iCheck({
                checkboxClass: 'icheckbox_minimal',
                radioClass: 'iradio_minimal'
            });
            $(".myDisabledCheckBox").iCheck('disable');

            $("#foodInspectionDiv").Scroll({line:1,speed:500,timer:3000,up:"but_up",down:"but_down"});
        }
    });
}

function loadMyDateTable() {
    //Date for the calendar events (dummy data)
    var date = new Date();
    var d = date.getDate(),
        m = date.getMonth(),
        y = date.getFullYear();

    //Calendar
    $('#calendar').fullCalendar({
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month,basicWeek,basicDay'
        },
        navLinks: true, // can click day/week names to navigate views
        editable: true,
        eventLimit: true, // allow "more" link when too many events
        monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        monthNamesShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        dayNames: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
        dayNamesShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
        today: ["今天"],
        buttonText: {//This is to add icons to the visible buttons
            prev: "<span class='fa fa-caret-left'></span>",
            next: "<span class='fa fa-caret-right'></span>",
            today: '今天',
            month: '月',
            week: '周',
            day: '日'
        },
        events:  function(start, end ,  callback){
            var fstart  = $.fullCalendar.formatDate(start, "yyyy-MM-dd");
            var fend  = $.fullCalendar.formatDate(end, "yyyy-MM-dd");
            $.post("/foodMenu/getFoodMenus", {startDate:fstart,endDate:fend}, function(data){
                if(data.code==1000){
                    callback(data.rows);
                }else{
                    //系统接口错误
                }
            });
        },
        dayClick: function(date, allDay, jsEvent, view) {
            //alert("sldkjflaskdjflaksjdflkjasdlfkjasldkf");
            var selectdate = $.fullCalendar.formatDate(date, "yyyy-MM-dd");
            /*$( "#reservebox" ).dialog({
                autoOpen: false,
                height: 450,
                width: 400,
                title: '建立记事簿 ' + selectdate,
                modal: true,
                position: "center",
                draggable: false,
                beforeClose: function(event, ui) {
                    $.validationEngine.closePrompt("#thingsType");
                    $.validationEngine.closePrompt("#start");
                    $.validationEngine.closePrompt("#end");
                },
                buttons: {
                    "取消": function() {
                        $( this ).dialog( "close" );
                    },
                    "添加": function() {
                        // alert("这就是点击了reserve的方法")
                        /!* if($("#reserveformID").validationEngine({returnIsValid:true})){*!/
                        var startdatestr = $("#start").val();
                        var enddatestr = $("#end").val();
                        var confid = $("#thingsType").val();
                        var repweeks = $("#repweeks").val();
                        if(repweeks==null){
                            repweeks=0;
                        }
                        var startdate =  $.fullCalendar.parseDate(selectdate+"T"+startdatestr);
                        var enddate =  $.fullCalendar.parseDate(selectdate+"T"+enddatestr);
                        var schdata = {startdate:startdate, enddate:enddate, confid:confid, repweeks:repweeks};
                        var title = $("#title").val();
                        var content = $("#details").val();

                        //alert("startdatestr=="+startdatestr+"enddatestr=="+enddatestr+"enddatestr=="+enddatestr+"confid=="+confid+"repweeks=="+repweeks+"startdate=="+startdate+"enddate=="+enddate+"schdata=="+schdata)
                        $.ajax({
                            type : "post",
                            data :{
                                title : title,
                                content : content,
                                starTime : selectdate+" "+startdatestr+":00",
                                endTime : selectdate+" "+enddatestr+":00",
                                repeatWeeks :repweeks,
                                thingsType : confid
                            },
                            url : "${ctx}/memo/addNotepad",
                            success : function(data) {
                                //alert("添加数据完成"+data)
                                //closewait();
                                window.location.reload();
                                //若执行成功的话，则隐藏进度条提示
                                if (data != null && data != 'undefined'
                                    && data == 1) {
                                    layer.msg('记事本添加成功!', {icon: 6,time:1000});
                                    parent.flushParent();
                                    layer_close();
                                    window.location.reload();
                                }else if (data == -1) {
                                    layer.msg('记事本名称已经存在!', {icon: 5,time:1000});
                                }else if (data == 0) {
                                    layer.msg('很抱歉！添加失败!', {icon: 5,time:1000});
                                }else{
                                    layer.msg('系统异常！请与系统管理员联系!', {icon: 5,time:1000});
                                }
                            }
                        });
                    }
                }
            });
            $( "#reservebox" ).dialog( "open" );*/
            return false;
        },
        timeFormat: 'HH:mm{ - HH:mm}',
        eventClick: function(event) {
            myEvent = event;
            myEvenClick(myEvent);
            return false;
        },
        loading: function(bool) {
            //日历加载事件
            /*if (bool) $('#loading').show();
            else $('#loading').hide();*/
        },
        eventMouseover: function(calEvent, jsEvent, view) {
            var dateTitle = '';
            switch (calEvent.menuOrder) {
                case '1':{
                    dateTitle = calEvent.siteName+"-早餐-"+calEvent.title;
                }
                    break;
                case '2':{
                    dateTitle = calEvent.siteName+"-中餐-"+calEvent.title;
                }
                    break;
                case '3':{
                    dateTitle = calEvent.siteName+"-晚餐-"+calEvent.title;
                }
                    break;
                case '4':{
                    dateTitle = calEvent.siteName+"-夜宵-"+calEvent.title;
                }
                    break;
            }
            var fstart  = $.fullCalendar.formatDate(calEvent.start, "yyyy-MM-dd");
            $(this).attr('title', fstart +"-"+ dateTitle);
            $(this).css('font-weight', 'normal');
            $(this).tooltip({
                effect:'toggle',
                cancelDefault: true
            });
        },
        eventMouseout: function(calEvent, jsEvent, view) {
            $(this).css('font-weight', 'normal');
        },
        eventRender: function(event, element) {
            var fstart  = $.fullCalendar.formatDate(event.start, "yyyy-MM-dd");
            var fend  = $.fullCalendar.formatDate(event.end, "yyyy-MM-dd");
            // Bug in IE8
            //element.html('<a href=#>' + fstart + "-" +  fend + '<div style=color:#E5E5E5>' +  event.title + "</div></a>");
        },
        eventAfterRender : function(event, element, view) {
            //读取数据之后
            var fstart  = $.fullCalendar.formatDate(event.start, "yyyy-MM-dd");
            var fend  = $.fullCalendar.formatDate(event.end, "yyyy-MM-dd");
            var dateTitle = '';
            var allRGB = event.backgroundColor.split("(")[1].split(")")[0].split(",");
            //var myTextColor = "rgb("+accSub(255,allRGB[0])+","+accSub(255,allRGB[1])+","+accSub(255,allRGB[2])+")";
            var myTextColor = "rgb(207,209,169)";
            switch (event.menuOrder) {
                case '1':{
                    dateTitle = event.siteName+"-早餐-"+event.title;
                    /*checkStatusColor = '#d9534f';
                    jcIcon = 'iconzuoyexuanzhongzhuangtai';
                    transColor = '#7fc6a6';*/
                }
                    break;
                case '2':{
                    dateTitle = event.siteName+"-中餐-"+event.title;
                    /*checkStatusColor = '#7fc6a6';
                    jcIcon = 'iconzuoyeweixuanzhong';
                    transColor = '#d9534f';*/

                }
                    break;
                case '3':{
                    dateTitle = event.siteName+"-晚餐-"+event.title;
                    /*checkStatusColor = '#7fc6a6';
                    jcIcon = 'iconzuoyeweixuanzhong';
                    transColor = '#d9534f';*/

                }
                    break;
                case '4':{
                    dateTitle = event.siteName+"-夜宵-"+event.title;
                    /*checkStatusColor = '#7fc6a6';
                    jcIcon = 'iconzuoyeweixuanzhong';
                    transColor = '#d9534f';*/

                }
                    break;
            }
            if(view.name=="month"){
                var evtcontent = '<div class="fc-event-vert"><a>';
                evtcontent = evtcontent + '<span style="color: '+myTextColor+'">' +  dateTitle + '</span>';
                evtcontent = evtcontent + '</a><div class="ui-resizable-handle ui-resizable-e"><img src="'+event.photo+'"/></div></div>';
                element.html(evtcontent);
            }else if(view.name=="basicWeek"){
                var evtcontent = '';
                evtcontent = evtcontent + '<span style="color: '+myTextColor+'">' +  dateTitle + '</span>';
                evtcontent = evtcontent + '<span class="ui-icon ui-icon-arrowthick-2-n-s"><div class="ui-resizable-handle ui-resizable-s"></div></span>';
                element.html(evtcontent);
            }else if(view.name=="basicDay"){
                var evtcontent = '';
                evtcontent = evtcontent + '<span style="color: '+myTextColor+'">' +  dateTitle + '</span>';
                evtcontent = evtcontent + '<span class="ui-icon ui-icon-arrow-2-n-s"><div class="ui-resizable-handle ui-resizable-s"></div></span>';
                element.html(evtcontent);
            }
        },
        eventDragStart: function( event, jsEvent, ui, view ) {
            ui.helper.draggable("option", "revert", true);
        },
        eventDragStop: function( event, jsEvent, ui, view ) {
        },
        eventDrop: function( event, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view ) {
            console.log("进行时间拖拽的时候触发的事件");
            //拖拽的时候进行时间的改变
            var start = $.fullCalendar.formatDate(event.start, "yyyy-MM-dd HH:mm:ss");
            var end = $.fullCalendar.formatDate(event.end, "yyyy-MM-dd HH:mm:ss");
            /*$.ajax({
                type : "post",
                data :{
                    id :event.id,
                    start :start,
                    end :end
                },
                url : "${ctx}/memo/updateNotepad",
                success : function(data) {
                    if (data == 1){
                        console.log("拖拽事件成功")
                    }else {
                        console.log("拖拽事件失败")
                    }
                }
            });

            if(1==1||2==event.uid){
                var schdata = {startdate:event.start, enddate:event.end, confid:event.confid, sid:event.sid};
            }else{
                revertFunc();
            }*/

        },
        eventResizeStart:  function( event, jsEvent, ui, view ) {

            //alert('resizing');

        },
        eventResize: function(event,dayDelta,minuteDelta,revertFunc) {

            if(1==1||2==event.uid){
                var schdata = {startdate:event.start, enddate:event.end, confid:event.confid, sid:event.sid};

            }else{
                revertFunc();
            }

        }

    });
}

function loadMyLChar() {
    var myChart = echarts.init(document.getElementById('bar-main'));
    myChart.setOption({
        /*title: {
                    text: '未来一周气温变化', //标题
                    subtext: '纯属虚构' //子标题
                },*/
        tooltip: {//提示框，鼠标悬浮交互时的信息提示
            trigger: 'axis'//值为axis显示该列下所有坐标轴对应数据，值为item时只显示该点数据
        },
        legend: { //图例，每个图表最多仅有一个图例
            icon: 'rect', //设置类别样式
            textStyle:{//图例文字的样式
                color:'#fff'
            },
            data: ['不明人员', '不戴口罩']
        },
        toolbox: {
            show : true,
            y: 'bottom',
            feature : {
                mark : {
                    show: true
                },
                dataView : {//数据视图
                    show: true,
                    readOnly: false
                },
                magicType : {//切换图表
                    show: true,
                    type: ['line', 'bar', 'stack', 'tiled']
                },
                restore : {//还原原始图表
                    show: true
                },
                saveAsImage : {//保存图片
                    show: true
                }
            }
        },
        toolbox: {//工具栏
            show: true,
            feature: {
                mark: {
                    show: true,
                    iconStyle: {
                        borderColor: '#fff'
                    },
                    emphasis:{
                        iconStyle: {
                            borderColor: '#fff'
                        }
                    }
                },
                dataView: { //数据视图
                    show: true,
                    readOnly: false,//是否只读
                    iconStyle: {
                        borderColor: '#fff'
                    },
                    emphasis:{
                        iconStyle: {
                            borderColor: '#fff'
                        }
                    }
                },
                magicType: {//切换图表
                    show: true,
                    type: ['line', 'bar', 'stack', 'tiled'],//图表
                    iconStyle: {
                        borderColor: '#fff'
                    },
                    emphasis:{
                        iconStyle: {
                            borderColor: '#fff'
                        }
                    }
                },
                restore: {//还原原始图表
                    show: true,
                    iconStyle: {
                        borderColor: '#fff'
                    },
                    emphasis:{
                        iconStyle: {
                            borderColor: '#fff'
                        }
                    }
                },
                saveAsImage: {
                    show: true,
                    iconStyle: {
                        borderColor: '#fff'
                    },
                    emphasis:{
                        iconStyle: {
                            borderColor: '#fff'
                        }
                    }
                }
            }
        },
        calculable: true,//是否启用拖拽重计算特性
        xAxis: [{
            type: 'category',  //坐标轴类型，横轴默认为类目型'category'
            boundaryGap: false,
            axisLabel: {
                show: true,
                textStyle: {
                    color: '#fff'   //这里用参数代替了
                }
            },
            data: dataViewBottom//数据项
        }],
        yAxis: [{
            type: 'value', //坐标轴类型，纵轴默认为数值型'value'
            axisLabel: {
                //formatter: '{value} °C', //加上单位
                formatter: '{value}', //加上单位
                show: true,
                textStyle: {
                    color: '#fff'   //这里用参数代替了
                }
            }
        }],
        dataZoom: [
            {
                id: 'dataZoomX',
                type: 'slider',
                xAxisIndex: [0],
                filterMode: 'filter'
            }/*,
            {
                id: 'dataZoomY',
                type: 'slider',
                yAxisIndex: [0],
                filterMode: 'empty'
            }*/
        ]
    });


    var myNewDateBM = dataViewBottom;
    var myNewDateBDK = get_day($.fullCalendar.formatDate(new Date(), "yyyy-MM-dd"),'2');
    var bmCount = 0;
    var bdkCount = 0;
    $.post("/abnormalSnapshot/getAbnormalSnapshot",{},function(data){
        var myBMData = [];
        if(data.code==1000){
            //刷新数据
            //不戴口罩
            $.each(data.rows,function (ind,val) {
                //inArray 查找下标，splice(1,2,3) 1要替换或删除的index,2要删除替换几个，3替换的元素（不传则删除）
                myNewDateBM.splice($.inArray(val.createTime,dataViewBottom),1,val.dataCount);
                bdkCount = accAdd(bdkCount,val.dataCount);
            });
            //不明人员
            $.each(data.data,function (ind,val) {
                //inArray 查找下标，splice(1,2,3) 1要替换或删除的index,2要删除替换几个，3替换的元素（不传则删除）
                myNewDateBDK.splice($.inArray(val.createTime,dataViewBottom),1,val.dataCount);
                bmCount = accAdd(bmCount,val.dataCount);
            });

            var allCount = accAdd(bdkCount,bmCount);
            var perBDK = accMul(accDiv(bdkCount,allCount),100).toFixed(2)+"%";
            var perBM = accMul(accDiv(bmCount,allCount),100).toFixed(2)+"%";
            $("#smBDKCount").text(perBDK);
            $("#smBDKCountDiv").css("width",perBDK);
            $("#smBMCount").text(perBM);
            $("#smBMCountDiv").css("width",perBM);
            for(var i=0;i<myNewDateBM.length;i++){
                if(myNewDateBM[i].toString().indexOf("-")>0){
                    myNewDateBM.splice(i,1,0);
                }
                if(myNewDateBDK[i].toString().indexOf("-")>0){
                    myNewDateBDK.splice(i,1,0);
                }
            }
            //不明人员
            var option = {
                series: [{//设置图表数据
                    name: '不明人员', //系列名称，如果启用legend，该值将被legend.data索引相关
                    type: 'bar',//图表类型
                    data: myNewDateBDK,
                    itemStyle: {
                        normal: {
                            color:'red'
                        }
                    },
                    markPoint: {  //系列中的数据标注内容
                        data: [{
                            type: 'max',
                            name: '最大值'
                        },
                            {
                                type: 'min',
                                name: '最小值'
                            }]
                    },
                    markLine: {//系列中的数据标线内容
                        data: [{
                            type: 'average',
                            name: '平均值'
                        }]
                    }
                },
                    {
                        name: '不戴口罩',
                        type: 'bar',
                        data: myNewDateBM,
                        itemStyle: {
                            normal: {
                                color:'green'
                            }
                        },
                        markPoint: {  //系列中的数据标注内容
                            data: [{
                                type: 'max',
                                name: '最大值'
                            },
                                {
                                    type: 'min',
                                    name: '最小值'
                                }]
                        },
                        markLine: {//系列中的数据标线内容
                            data: [{
                                type: 'average',
                                name: '平均值'
                            }]
                        }
                        /*markPoint: {
                            data: [{
                                name: '周最低',
                                value: -2,
                                xAxis: 1,
                                yAxis: -1.5
                            }]
                        },
                        markLine: {
                            data: [{
                                type: 'average',
                                name: '平均值'
                            }]
                        }*/
                    }]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        }
    });
}


/* 地图形式显示站点 */
function showMap(sdIdId){

    $("#my-map2").empty();

    // 百度地图API功能
    var map = new BMap.Map("my-map2");
    map.centerAndZoom(new BMap.Point(120.578308,30.60934),6);

    map.enableScrollWheelZoom(true);

    map.clearOverlays();

    var markers = [];
    $("#menuTypeId").html("");
    $.each(siteData,function(ind,val){

        $("#menuTypeId").append("<option value='"+val.sdId+"'>"+val.name+"</option>");

        if(fruits!=null&&fruits!=undefined){
            if($.inArray(val.sdId+"", fruits)!=-1){
                $("#menuTypeId").find("option[value='"+val.sdId+"']").attr("selected",'selected');
                $("#menuTypeId").trigger("chosen:updated");
            }
        }

        if(val.pid==0){
            //map.setCenter();
            setTimeout(function () {
                map.setCenter(new BMap.Point(val.lng,val.lat));
            } ,1000 * 0.5);

        }

        if(val.lng!=null&&val.lat!=null){

            var sContent =
                "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>"+val.name+"</h4>" +
                "<img style='float:right;margin:4px' id='imgDemo' src='"+myImgPrefix+val.photo+"' width='139' height='104' title='"+val.name+"'/>" +
                "<p style='margin:0;line-height:1.5;font-size:13px;text-indent:2em'>"+val.address+"</p>" +
                "</div>";

            var infoWindow = new BMap.InfoWindow(sContent);  // 创建信息窗口对象

            if(val.photo!=undefined&&val.photo!=null&&val.photo!=""){
                //创建自己的图标
                var pp = new BMap.Point(val.lng, val.lat);

                var myIcon = new BMap.Icon(
                    myImgPrefix+val.photo, // 上传的站点图标
                    new BMap.Size(35,45), // 视窗大小
                    {
                        imageSize: new BMap.Size(144,92), // 引用图片实际大小
                        imageOffset:new BMap.Size(-56,-10)  // 图片相对视窗的偏移
                    }
                );
                var marker2 = new BMap.Marker(pp,{icon:myIcon});  // 创建标注
                map.addOverlay(marker2);              // 将标注添加到地图中
                //marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画

                markers.push(marker2);

                /*marker2.addEventListener("click", function(){
                    setCurrentSdId(sdIdId,val.sdId);
                    marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
                });*/

                //双击取消站点选择
                marker2.addEventListener("dblclick", function(){
                    marker2.setAnimation(null);
                });

                marker2.addEventListener("mouseover", function(){
                    this.openInfoWindow(infoWindow);
                    //图片加载完毕重绘infowindow
                    document.getElementById('imgDemo').onload = function (){
                        infoWindow.redraw();   //防止在网速较慢，图片未加载时，生成的信息框高度比图片的总高度小，导致图片部分被隐藏
                    }
                });

                marker2.addEventListener("mouseout", function(){
                    this.closeInfoWindow();
                });

            }else{
                //创建百度地图默认小狐狸图标
                var pp = new BMap.Point(val.lng, val.lat);
                var myIcon = new BMap.Icon("http://lbsyun.baidu.com/jsdemo/img/fox.gif", new BMap.Size(300,157));
                var marker2 = new BMap.Marker(pp,{icon:myIcon});  // 创建标注
                map.addOverlay(marker2);              // 将标注添加到地图中
                //marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画

                markers.push(marker2);

                /*marker2.addEventListener("click", function(){
                    //alert(val.lng+",,,"+val.lat+",,,,"+val.name+",,,"+val.address);
                    setCurrentSdId(sdIdId,val.sdId);
                    marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
                });*/

                //双击取消站点选择
                marker2.addEventListener("dblclick", function(){
                    //alert(val.lng+",,,"+val.lat+",,,,"+val.name+",,,"+val.address);
                    /*setCurrentSdId(sdIdId,val.sdid);
                    marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画*/
                    marker2.setAnimation(null);
                });

                marker2.addEventListener("mouseover", function(){
                    this.openInfoWindow(infoWindow);
                    //图片加载完毕重绘infowindow
                    document.getElementById('imgDemo').onload = function (){
                        infoWindow.redraw();   //防止在网速较慢，图片未加载时，生成的信息框高度比图片的总高度小，导致图片部分被隐藏
                    }
                });

                marker2.addEventListener("mouseout", function(){
                    this.closeInfoWindow();
                });
            }


        }
    });
    //更新chose
    $("#menuTypeId").trigger("chosen:updated");

    //点聚合
    //最简单的用法，生成一个marker数组，然后调用markerClusterer类即可。
    var markerClusterer = new BMapLib.MarkerClusterer(map, {markers:markers});


    // 覆盖区域图层测试
    map.addTileLayer(new BMap.PanoramaCoverageLayer());

    var stCtrl = new BMap.PanoramaControl(); //构造全景控件
    stCtrl.setOffset(new BMap.Size(20, 20));
    map.addControl(stCtrl);//添加全景控件

    // 添加带有定位的导航控件
    var navigationControl = new BMap.NavigationControl({
        // 靠左上角位置
        anchor: BMAP_ANCHOR_TOP_LEFT,
        // LARGE类型
        type: BMAP_NAVIGATION_CONTROL_LARGE,
        // 启用显示定位
        enableGeolocation: true
    });
    map.addControl(navigationControl);


    // 添加定位控件
    var geolocationControl = new BMap.GeolocationControl();
    geolocationControl.addEventListener("locationSuccess", function(e){
        // 定位成功事件
        var address = '';
        address += e.addressComponent.province;
        address += e.addressComponent.city;
        address += e.addressComponent.district;
        address += e.addressComponent.street;
        address += e.addressComponent.streetNumber;

    });
    geolocationControl.addEventListener("locationError",function(e){
        // 定位失败事件
        alert(e.message);
    });
    map.addControl(geolocationControl);


    var cr = new BMap.CopyrightControl({anchor: BMAP_ANCHOR_TOP_RIGHT});   //设置版权控件位置
    map.addControl(cr); //添加版权控件

    var bs = map.getBounds();   //返回地图可视区域
    cr.addCopyright({id: 1, content: "Copyright © 2019.Company name All rights reserved.More Templates 智飨云 - Collect from 智飨云<a href='#'>智飨科技</a>", bounds: bs});
    //Copyright(id,content,bounds)类作为CopyrightControl.addCopyright()方法


    var geoc = new BMap.Geocoder();

    //单击获取点击的经纬度
    map.addEventListener("click",function(e){

        //点击获取试点id

    });

    var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
        {"input" : "suggestId"
            ,"location" : map
        });

    ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
        var str = "";
        var _value = e.fromitem.value;
        var value = "";
        if (e.fromitem.index > -1) {
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

        value = "";
        if (e.toitem.index > -1) {
            _value = e.toitem.value;
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
        G("searchResultPanel").innerHTML = str;
    });

    var myValue;
    ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
        var _value = e.item.value;
        myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;

        setPlace(map,myValue);
    });

    $("#compose-modal").modal("show");
}

// 百度地图API功能
function G(id) {
    return document.getElementById(id);
}

function setPlace(map,myValue){
    map.clearOverlays();    //清除地图上所有覆盖物
    function myFun(){
        var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
        map.centerAndZoom(pp, 18);
        //map.addOverlay(new BMap.Marker(pp));    //添加标注
    }
    var local = new BMap.LocalSearch(map, { //智能搜索
        onSearchComplete: myFun
    });
    local.search(myValue);
}

function setCurrentSdId(sdIdId,sdId){
    if($("#"+sdIdId).val()!=undefined&&$("#"+sdIdId).val()!=""&&$("#"+sdIdId).val()!=null&&$("#"+sdIdId).val()!="undefined"){
        var beforeVal = $("#"+sdIdId).val();
        var arrayList = beforeVal.split(",");
        if($.inArray(sdId+"", arrayList)==-1){
            $("#"+sdIdId).val(beforeVal+","+sdId);
            $("#menuTypeId").find("option[value='"+sdId+"']").attr("selected",'selected');
            $("#menuTypeId").trigger("chosen:updated");
        }
    }else{
        $("#"+sdIdId).val(sdId);
        $("#menuTypeId").find("option[value='"+sdId+"']").attr("selected",'selected');
        $("#menuTypeId").trigger("chosen:updated");
    }
}

function myResetForm(){
    $('#userForm').resetForm();
    $('#userForm').clearForm();
}
