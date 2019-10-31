var tes = 0;
var isShow = true;
var allPage;

var limCount = 10;
var allDataTol;

var initPageplugins = true;

var curPag = 1;

var rolesArray = [];

var siteRolesArray = [];

var jobArray = [];

var messagesJson;

var sdId;

var isZx;

var ajaxSCJCXXSearchFormOption = {
    beforeSubmit: showSCJCXXSearchRequest,
    success: showSCJCXXSearchResponse,
    url: "/user/getUsers",
    type: "post",
    data:{page:curPag,limit:limCount},
    dataType: "json",
    timeout: 3000
};

$(function(){

    sdId = $("#currentUserSdId").text();
    isZx = $("#currentUserIsZX").text();

    var admName = $("#teShiro").html();
    $("#showLAdm").html(admName);
    var s_time = dateFormat($("#teShiro2").html(),'yyyy-MM-dd HH:mm:ss');
    $("#headAdmDate").html(s_time+" 注册");

    var leftMenuStorage = localStorage.getItem("leftMenuStorage");

    $("#permUl").html(leftMenuStorage);
    addActClass($("#liParent1").children(":eq(0)"));
    $("#liParent1").children(":eq(1)").children(":eq(2)").find("a").css("color","#FFD600");

    loadUserList();

    $("#topShowLp").css("display","none");

    var fixHelperModified = function(e, tr) {
            var $originals = tr.children();
            var $helper = tr.clone();
            $helper.children().each(function(index) {
                $(this).width($originals.eq(index).width())
            });
            return $helper;
        },
        updateIndex = function(e, ui) {
            $('td.index', ui.item.parent()).each(function(i) {
                $(this).html(i + 1);
            });
        };
    $("#sort tbody").sortable({
        helper: fixHelperModified,
        stop: updateIndex
    }).disableSelection();

    $("#searchFormI").click(function () {
        $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
        return false;
    });

    function BarrageManager (options) {

        this.opts = {
            url       : '/message/getMessages',
            loadDelay : 10000 ,
        }

        $.extend( this.opts , options);
        this.bc = new BarrageCollection();
    }

    BarrageManager.prototype.load = function () {
        var self = this ;

        if(messagesJson==undefined||messagesJson==''||messagesJson=='undefined'){
            $.post(self.opts.url, {sdId:12}, function(data){

                if(data.code==1000){
                    if(data.data.length>0){

                        messagesJson = JSON.stringify(data);

                        $.each(data.data,function(ind,val){
                            self.bc.add(new Barrage({
                                id:val.id,
                                name:val.fromUserName,
                                text:val.content,
                                icon:val.fromUserIcon ? val.fromUserIcon : '../images/logo1.png'
                            }));
                        });
                        self.loop();
                    }
                }
            });
        }else{

            var jsonDataMessage = JSON.parse(messagesJson);

            $.each(jsonDataMessage.data,function(ind,val){
                self.bc.add(new Barrage({
                    id:val.id,
                    name:val.fromUserName,
                    text:val.content,
                    icon:val.fromUserIcon ? val.fromUserIcon : '../images/logo1.png'
                }));
            });
            self.loop();

        }

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

    function BarrageCollection () {
        this.mq = [] ;
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

    function Barrage (options) {
        this.opts = {
            $el         : null ,
            left        : 0 ,
            bgColor     : [Math.floor(Math.random()*255),Math.floor(Math.random()*255),Math.floor(Math.random()*255)] ,
            offset      : 50 ,
            duration    : 10000 ,
            delayTime   : 1000 ,
        };
        $.extend( this.opts , options);
        this.init();
    }

    Barrage.prototype.init = function () {

        this.opts.$el = $("<span><img src="+this.opts.icon+"><em>"+this.opts.name+":</em>"+this.opts.text+"</span>");

        var top = Math.ceil(Math.random() * 10 );
        this.opts.$el.css({
            top:top * 40 +'px',
            backgroundColor:"rgb("+this.opts.bgColor.join(",")+")"
        });

        var delay = Math.ceil(Math.random()*10);
        this.opts.delayTime *= Math.abs(delay - 5);

        var dur = Math.ceil(Math.random() * 10);
        this.opts.duration += dur * 1000 ;

        $('#barrage-wrapper').append(this.opts.$el);
        this.opts.left = -this.opts.$el.width() - this.opts.offset ;
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

    new BarrageManager().load();

    $("#qsType").selectator({
        labels: {
            search: '请选择审签类别'
        },
        showAllOptionsOnFocus: true
    });
    $("#qsType").next().css("width","100%");
    $("#qsType").next().find(".selectator_input").css("background","#1b1b1b");
    $(window).scroll(function () {
        if($(window).scrollTop() >= 100){
            $('.toTopRocket').fadeIn(300);
        }else{
            $('.toTopRocket').fadeOut(300);
        }
    });
    initMyCutLine();
    $("#userForm").validationEngine({
        binded:false,
        maxErrorsPerField:1,
        promptPosition: "topLeft:100",
        scroll:true,
        validateNonVisibleFields:true,
        focusFirstField:true,
        ajaxFormValidation: true,
        ajaxFormValidationURL:"/user/setUser",
        ajaxFormValidationMethod:"post",
        onBeforeAjaxFormValidation: showRequest,
        onAjaxFormComplete: showResponse
    });
});
function showSCJCXXSearchRequest(formData, jqForm, options){
    return true;
};
function showSCJCXXSearchResponse(data, status){
    initDateTableData(data);
}
function pageChange(i) {
    curPag = accAdd(i,1);
    loadUserList();
    Pagination.Page($(".ht-page"), i, allDataTol, limCount);
}
function loadUserList(){
    $("input[name='foodNameDto']").val($("#foodNameDtoShow").val());
    ajaxSCJCXXSearchFormOption.data={page:curPag,limit:limCount};
    $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
    return false;
}
function reloadMyDateTable() {
    $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
    return false;
}
function showRequest() {
    $("#roleIds").val(rolesArray.join(","));
    $("#siteRoleIds").val(siteRolesArray.join(","));
    if(jobArray&&jobArray.length>0){
        switch (jobArray.length){
            case 1:
                //留样人员
                switch (jobArray[0]){
                    case 0:
                        $("#jobIds").val(1);
                        break;
                    case 1:
                        $("#jobIds").val(2);
                        break;
                    case 2:
                        $("#jobIds").val(4);
                        break;
                }
                break;
            case 2:
                //称重人员

                if($.inArray(0,jobArray)>=0){
                    if($.inArray(1,jobArray)>=0){
                        $("#jobIds").val(3);
                    }
                    if($.inArray(2,jobArray)>=0){
                        $("#jobIds").val(5);
                    }
                }
                if($.inArray(1,jobArray)>=0){
                    if($.inArray(0,jobArray)>=0){
                        $("#jobIds").val(3);
                    }
                    if($.inArray(2,jobArray)>=0){
                        $("#jobIds").val(6);
                    }
                }
                if($.inArray(2,jobArray)>=0){
                    if($.inArray(1,jobArray)>=0){
                        $("#jobIds").val(6);
                    }
                    if($.inArray(0,jobArray)>=0){
                        $("#jobIds").val(5);
                    }
                }
                break;
            case 3:
                $("#jobIds").val(7);
                break;
        }
    }
    return true;
}
function showResponse(status, form,data, options){
    if(!isKickOut(data)){
        if(data.resultCode==100){
            loadUserList(1,limCount);
            myResetForm();
            $("#compose-modal").modal("hide");
            $('#classes').val("");
            $('#classes').selectator('refresh');
            $("#qsType").val('');
            $('#qsType').selectator('refresh');
            showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",data.msg);
        }else if(data.code==101){
            showJqueryConfirmWindow("#icon-shangxin1","更新或新增用户异常！深感抱歉，请联系管理人员为您加急处理");
        }else{
            showJqueryConfirmWindow("#icon-tishi1",data.msg);
        }
    }
}
function initDateTableData(data){
    if(!isKickOut(data)){
        if(data.resultCode==100){
            if(data.result&&data.result.length>0){
                $("#userListUl").html("");
                $.each(data.result,function (ind,val) {
                    //小品40% checkbook myStyleLi
                    $("#userListUl").append("<tr id='"+val.id+"-myStyleLi' onmouseover='showED("+val.id+")' onmouseleave='hideED("+val.id+")'>" +
                        "<td data-label='是否在职' class='comMove'>" +
                        "<input class='tgl tgl-flip' id='"+val.id+"' type='checkbox'>" +
                        "<label id='"+val.id+"-lab' class='ullabStyle tgl-btn' data-tg-off='离职' data-tg-on='在职' for='"+val.id+"' onclick='changeJobStatus("+val.job+","+val.id+","+"\""+val.username+"\")'></label>" +
                        "</td>" +
                        "<td data-label='用户名' class='comMove'>" +
                        "<span id='"+val.id+"-unspan' class='text'>"+val.username+"</span>" +
                        "</td>" +
                        "<td data-label='角色名称' class='comMove'>" +
                        "<small id='"+val.id+"-rlspan' class='label label-danger' style='font-size: 80%'><i class='fa fa-user'></i>&nbsp;"+val.roleNames+"</small>" +
                        "</td>" +
                        "<td data-label='添加时间' class='comMove'>" +
                        "<small id='"+val.id+"-atspan' class='label label-danger' style='font-size: 80%'><i class='fa fa-clock-o'></i>&nbsp;"+val.insertTime+"</small>" +
                        "</td>" +
                        "<td data-label='操作'>" +
                        "<div class='tools' data-label='"+val.id+"' style='color: #f56954;display: none;'>" +
                        "<i class='fa fa-edit' data-toggle='tooltip' title='编辑' data-original-title='编辑' style='cursor: pointer;font-size:1.3em;' onclick='getUserAndRoles("+JSON.stringify(val)+","+val.id+")'></i>" +
                        "<i class='fa fa-trash-o' data-toggle='tooltip' title='删除' data-original-title='删除' style='cursor: pointer;font-size:1.3em;padding-left: 1em;' onclick='delUser("+val.id+","+"\""+val.username+"\")'></i>" +
                        "</div>" +
                        "</td>" +
                        "</tr>");

                    if(!val.job){

                        isShow = false;

                        //改为添加或移除class  checkbox 点击前先将class和check去除
                        $("#"+val.id).prop("checked",true);
                        $("#"+val.id+"-lab").addClass("cll1");

                        $("#"+val.id+"-unspan").removeClass("doneText");
                        $("#"+val.id+"-mbspan").removeClass("doneSmal");
                        $("#"+val.id+"-emspan").removeClass("doneSmal");
                        $("#"+val.id+"-rlspan").removeClass("doneSmal");
                        $("#"+val.id+"-atspan").removeClass("doneSmal");
                    }else{
                        $("#"+val.id+"-unspan").addClass("doneText");
                        $("#"+val.id+"-mbspan").addClass("doneSmal");
                        $("#"+val.id+"-emspan").addClass("doneSmal");
                        $("#"+val.id+"-rlspan").addClass("doneSmal");
                        $("#"+val.id+"-atspan").addClass("doneSmal");
                    }
                });

                allDataTol = data.total;

                if(initPageplugins){
                    Pagination.init($(".ht-page"), pageChange);
                    Pagination.Page($(".ht-page"), 0, allDataTol, limCount);
                    initPageplugins = false;
                }else{
                    Pagination.Page($(".ht-page"), curPag-1, allDataTol, limCount);
                }
            }else{
                $("#userListUl").html("");
                $("#userListUl").append("<tr>" +
                    "<td data-label='用户数据' colspan='6' class='comMove'>" +
                    "<small class='label label-danger' style='font-size: 80%'><i class='iconfont iconweikongtishi'></i>&nbsp;未查找到数据</small>" +
                    "</td>" +
                    "</tr>");
            }
            $("[data-toggle='tooltip']").tooltip();
        }else if(data.resultCode==101){
            showJqueryConfirmWindow("#icon-shangxin1","用户列表查询失败！深感抱歉，请联系管理人员为您加急处理");
        }else{
            showJqueryConfirmWindow("#icon-tishi1",data.msg);
        }

    }
}
function showED(cur) {
    $("div[data-label='"+cur+"']").css("display","block");
}
function hideED(cur) {
    $("div[data-label='"+cur+"']").css("display","none");
}
function getUserAndRoles(obj,id) {
    $("#roleIds").val("");
    $("#id").val("");
    $("#version").val("");
    $("#isZx").val("");
    myResetForm();
    if(obj.job){
        showJqueryConfirmWindow("#icon-tishi1","该用户已经<span style='font-weight:bold;color:red;'>离职</span>，不可进行编辑; 如需编辑，请设置为<span style='font-weight:bold;color:green;'>在职</span>状态。");
    }else{
        $.post("/user/getUserAndRoles",{"id":id},function(data){
            if(!isKickOut(data)){
                if(data.resultCode==100){
                    var existRole='';
                    if(data.result.user.userRoles ){
                        $.each(data.result.user.userRoles, function (index, item) {
                            if(existRole==""||existRole ==null || existRole == undefined){
                                existRole = item.roleId+"";
                            }else{
                                existRole+=','+item.roleId;
                            }
                        });
                    }
                    existRole = existRole.split(",");
                    var existSiteRole='';
                    if(data.result.user.userSiteRoleKeys){
                        $.each(data.result.user.userSiteRoleKeys, function (index, item) {
                            if(existSiteRole==""||existSiteRole ==null || existSiteRole == undefined){
                                existSiteRole = item.siteRoleId+"";
                            }else{
                                existSiteRole+=','+item.siteRoleId;
                            }
                        });
                    }
                    existSiteRole = existSiteRole.split(",");

                    $("#jobDivs").empty();
                    if(data.result.jobData.jobName){
                        $.each(data.result.jobData.jobName.split(","), function (index, item) {
                            $("#jobDivs").append("<div class='col-md-4 col-sm-4 col-xs-6 menu-w3lsgrids'>" +
                                "<a id='raleaJobli"+index+"' href='javascript:void(0)' data-toggle='tooltip' title='' data-id='"+index+"' data-original-title='"+item+"' onclick='chooseJob("+index+",this)'>"+item+"</a></div>");
                        });
                    }
                    if(data.result.user.profession){
                        switch (data.result.user.profession){
                            case 1:
                                //留样人员
                                $("#raleaJobli0").click();
                                break;
                            case 2:
                                //称重人员
                                $("#raleaJobli1").click();
                                break;
                            case 3:
                                //留样人员和称重人员
                                $("#raleaJobli0").click();
                                $("#raleaJobli1").click();
                                break;
                            case 4:
                                //厨师
                                $("#raleaJobli2").click();
                                break;
                            case 5:
                                //留样人员和厨师
                                $("#raleaJobli0").click();
                                $("#raleaJobli2").click();
                                break;
                            case 6:
                                //称重人员和厨师
                                $("#raleaJobli1").click();
                                $("#raleaJobli2").click();
                                break;
                            case 7:
                                //留样人员称重人员厨师
                                $("#raleaJobli0").click();
                                $("#raleaJobli1").click();
                                $("#raleaJobli2").click();
                                break;
                            case 8:
                                break;
                        }
                    }
                    if(data.result.shifts){
                        $("#classes").append("<option value='' data-subtitle='' data-left='' selected>--选择班次--</option>\n" +
                            "<option value='1' data-subtitle='("+data.result.shifts.morningStartTime+"-"+data.result.shifts.morningEndTime+")'>早班</option>\n" +
                            "<option value='2' data-subtitle='("+data.result.shifts.noonStartTime+"-"+data.result.shifts.noonEndTime+")'>中班</option>\n" +
                            "<option value='3' data-subtitle='("+data.result.shifts.eveningStartTime+"-"+data.result.shifts.eveningEndTime+")'>晚班</option>\n" +
                            "<option value='4' data-subtitle='("+data.result.shifts.middleStartTime+"-"+data.result.shifts.middleEndTime+")'>夜班</option>")
                    }
                    $("#classes").selectator({
                        labels: {
                            search: '请选择班次'
                        },
                        showAllOptionsOnFocus: true
                    });
                    $("#classes").next().css("width","100%");
                    $("#classes").next().find(".selectator_input").css("background","#1b1b1b");

                    if(data.result.user.classes){
                        $("#classes").val(data.result.user.classes);
                        $('#classes').selectator('refresh');
                    }
                    $("#id").val(data.result.user.id==null?'':data.result.user.id);
                    $("#userName").val(data.result.user.username==null?'':data.result.user.username);
                    $("#sdId").val(data.result.user.sdid==null?'':data.result.user.sdid);
                    if(data.result.user.qsType){
                        $("#qsType").val(data.result.user.qsType);
                        $('#qsType').selectator('refresh');
                    }

                    $("#rolesDivs").empty();
                    var str = '';
                    $.each(data.result.roles, function (index, item) {
                        $("#rolesDivs").append("<div class='col-md-4 col-sm-4 col-xs-6 menu-w3lsgrids'>" +
                            "<a id='raleaCli"+item.id+"' href='javascript:void(0)' data-toggle='tooltip' title='' data-id='"+item.id+"' data-original-title='"+item.roleName+"' onclick='chooseRoles("+item.id+",this)'>"+item.roleName+"</a></div>");
                        if(existRole){
                            if($.inArray(item.id+"",existRole)>=0){
                                str = str + item.id+",";
                                $("#raleaCli"+item.id).click();
                            }
                        }
                    });
                    $("#siteRolesDivs").empty();
                    $.each(data.result.siteRoles, function (index, item) {
                        $("#siteRolesDivs").append("<div class='col-md-4 col-sm-4 col-xs-6 menu-w3lsgrids'>" +
                            "<a id='raleaSiteCli"+item.id+"' href='javascript:void(0)' data-toggle='tooltip' title='' data-id='"+item.id+"' data-original-title='"+item.roleName+"' onclick='chooseSiteRoles("+item.id+",this)'>"+item.roleName+"</a></div>");
                        if(existSiteRole){
                            if ($.inArray(item.id+"",existSiteRole)>=0){
                                $("#raleaSiteCli"+item.id).click();
                            }

                        }
                    });
                    $("#chgUserTitle").html("<i class='iconfont iconbianjiyonghu'></i>&nbsp;更新用户");
                    if(sdId=="22"&&isZx=="false"){
                        $("#zx_type").html("<input class='tglZX tgl-flipZX' id='cb9' type='checkbox'><label class='tgl-btnZX' data-tg-off='所属智飨:否' data-tg-on='所属智飨:是' for='cb9' style='margin:0 auto;width: 20%;' onclick='changePerType()'></label>");
                        if(!data.result.user.zx){
                            $("#cb9").click();
                        }
                        $("#form-group-sdId").css("display","block");

                    }else{
                        $("#zx_type").html("");
                        $("#form-group-sdId").css("display","none");
                    }

                    $("#compose-modal").modal("show");
                }else if(data.resultCode==101){
                    showJqueryConfirmWindow("#icon-shangxin1","编辑前获取信息失败！深感抱歉，请联系管理人员为您加急处理");
                }else{
                    showJqueryConfirmWindow("#icon-tishi1",data.msg);
                }
            }
        });
    }
}
function delUser(id,name) {
    var currentUser=$("#currentUser").html();

    if(null!=id&&id!=""){
        if(currentUser==id){
            showJqueryConfirmWindow("#icon-tishi1",'对不起，您不能执行删除自己的操作！');
        }else{
            $.confirm({
                icon: '#icon-tishi1',
                theme: 'modern',
                title: '是否删除',
                content: '您确定要删除'+name+'用户吗？',
                animation: 'news',
                closeAnimation: 'news',
                buttons: {
                    showMyMsg2: {
                        text: '确定',
                        action: function () {
                            $.post("/user/delUser",{"id":id},function(data){
                                if(!isKickOut(data)){
                                    if(data.resultCode==100){
                                        loadUserList(1,limCount);
                                        showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",name+"用户已成功删除");
                                    }else if(data.resultCode==101){
                                        showJqueryConfirmWindow("#icon-shangxin1","删除用户失败！深感抱歉，请联系管理人员为您加急处理");
                                    }else{
                                        showJqueryConfirmWindow("#icon-tishi1",data.msg);
                                    }
                                }
                            });

                        }
                    },
                    cancel: {
                        text: "取消"
                    }

                }
            });

        }
    }else{
        showJqueryConfirmWindow("#icon-tishi1","删除前主键获取失败！深感抱歉，请联系管理人员为您加急处理");
    }

}
function changeJobStatus(curJob,curDId,personName){
    $("#"+curDId).prop("checked",false);
    var isJob;
    var cfText;
    if(curJob){
        //该员工已离职
        cfText = '是否确认'+personName+'在职';
        isJob = 0;
    }else{
        //该员工在职
        cfText = '是否确认'+personName+'离职';
        isJob = 1;
    }
    if(isShow){
        $.confirm({
            icon: '#icon-tishi1',
            theme: 'modern',
            title: '是否离职?',
            content: cfText,
            animation: 'news',
            closeAnimation: 'news',
            buttons: {
                logoutUser: {
                    text: '确认',
                    action: function () {
                        $.post("/user/setJobUser",{id:curDId,job:isJob},function(data){

                            if(!isKickOut(data)){
                                if(data.resultCode==100){
                                    $("#18-lab").animate({},function(){
                                    });
                                    loadUserList(curPag,limCount);
                                    showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14","已成功更改该员工在职状态");
                                }else if(data.resultCode==101){
                                    showJqueryConfirmWindow("#icon-shangxin1","更改在职状态失败！深感抱歉，请联系管理人员为您加急处理");
                                }else{
                                    showJqueryConfirmWindow("#icon-tishi1",data.msg);
                                }
                            }
                        });
                    }
                },
                cancel: function () {

                }
            }
        });
    }

    isShow = true;

}
function chooseRoles(id,curA){

    var curAClass = $(curA).attr("class");

    if(curAClass!=undefined&&curAClass!="undefined"){
        if(curAClass.indexOf("menu-w3lsgrids-click")!=-1){
            rolesArray.splice($.inArray(id,rolesArray),1);
            $(curA).removeClass("menu-w3lsgrids-click");
        }else{
            rolesArray.push(id);
            $(curA).addClass("menu-w3lsgrids-click");
        }
    }else{
        rolesArray.push(id);
        $(curA).addClass("menu-w3lsgrids-click");
    }

}
function chooseSiteRoles(id,curA){
    var curAClass = $(curA).attr("class");
    if(curAClass!=undefined&&curAClass!="undefined"){
        if(curAClass.indexOf("menu-w3lsgrids-click")!=-1){
            siteRolesArray.splice($.inArray(id,siteRolesArray),1);
            $(curA).removeClass("menu-w3lsgrids-click");
        }else{
            siteRolesArray.push(id);
            $(curA).addClass("menu-w3lsgrids-click");
        }
    }else{
        siteRolesArray.push(id);
        $(curA).addClass("menu-w3lsgrids-click");
    }
}
function chooseJob(id,curA) {
    var curAClass = $(curA).attr("class");
    if(curAClass!=undefined&&curAClass!="undefined"){
        if(curAClass.indexOf("menu-w3lsgrids-click")!=-1){
            jobArray.splice($.inArray(id,jobArray),1);
            $(curA).removeClass("menu-w3lsgrids-click");
        }else{
            jobArray.push(id);
            $(curA).addClass("menu-w3lsgrids-click");
        }
    }else{
        jobArray.push(id);
        $(curA).addClass("menu-w3lsgrids-click");
    }
}
function createUser() {
    $("#roleIds").val("");
    $("#id").val("");
    $("#isZx").val("");

    myResetForm();

    $("#rolesDivs").empty();
    $("#siteRolesDivs").empty();
    $("#jobDivs").empty();

    /*$('#classes').val("");
    $("#classes").find("option[value='']").attr("selected",'selected');*/

    $.post("/auth/getRoles",function(data){
        if(!isKickOut(data)){
            if(data.resultCode == 100){
                $("#chgUserTitle").html("<i class='iconfont icontianjiayonghu'></i>&nbsp;开通账号");

                if(data.result.roles.length>0){
                    $.each(data.result.roles,function(ind,val){
                        $("#rolesDivs").append("<div class='col-md-4 col-sm-4 col-xs-6 menu-w3lsgrids'>" +
                            "<a href='javascript:void(0)' data-toggle='tooltip' title='' data-id='"+val.id+"' data-original-title='"+val.roleName+"' onclick='chooseRoles("+val.id+",this)'>"+val.roleName+"</a></div>");
                    });
                }
                if(data.result.siteRoles.length>0){
                    $.each(data.result.siteRoles,function(ind,val){
                        $("#siteRolesDivs").append("<div class='col-md-4 col-sm-4 col-xs-6 menu-w3lsgrids'>" +
                            "<a href='javascript:void(0)' data-toggle='tooltip' title='' data-id='"+val.id+"' data-original-title='"+val.roleName+"' onclick='chooseSiteRoles("+val.id+",this)'>"+val.roleName+"</a></div>");
                    });
                }
                if(data.result.jobData.jobName){
                    $.each(data.result.jobData.jobName.split(","),function(ind,val){
                        $("#jobDivs").append("<div class='col-md-4 col-sm-4 col-xs-6 menu-w3lsgrids'>" +
                            "<a href='javascript:void(0)' data-toggle='tooltip' title='' data-id='"+ind+"' data-original-title='"+val+"' onclick='chooseJob("+ind+",this)'>"+val+"</a></div>");
                    });
                }
                if(data.result.shifts){
                    $("#classes").append("<option value='' data-subtitle='' data-left='' selected>--选择班次--</option>\n" +
                        "<option value='1' data-subtitle='("+data.result.shifts.morningStartTime+"-"+data.result.shifts.morningEndTime+")'>早班</option>\n" +
                        "<option value='2' data-subtitle='("+data.result.shifts.noonStartTime+"-"+data.result.shifts.noonEndTime+")'>中班</option>\n" +
                        "<option value='3' data-subtitle='("+data.result.shifts.eveningStartTime+"-"+data.result.shifts.eveningEndTime+")'>晚班</option>\n" +
                        "<option value='4' data-subtitle='("+data.result.shifts.middleStartTime+"-"+data.result.shifts.middleEndTime+")'>夜班</option>")
                }

                if(sdId=="22"&&isZx=="false"){
                    $("#zx_type").html("<input class='tglZX tgl-flipZX' id='cb9' type='checkbox'><label class='tgl-btnZX' data-tg-off='所属智飨:否' data-tg-on='所属智飨:是' for='cb9' style='margin:0 auto;width: 20%;' onclick='changePerType()'></label>");
                    $("#form-group-sdId").css("display","block");
                }else{
                    $("#zx_type").html("");
                    $("#form-group-sdId").css("display","none");
                }

                $("#classes").selectator({
                    labels: {
                        search: '请选择班次'
                    },
                    showAllOptionsOnFocus: true
                });
                $("#classes").next().css("width","100%");
                $("#classes").next().find(".selectator_input").css("background","#1b1b1b");
                $('#classes').val("");
                $('#classes').selectator('refresh');
                $("#compose-modal").modal("show");

            }else if(data.resultCode==101){
                showJqueryConfirmWindow("#icon-shangxin1","添加前查询权限失败！深感抱歉，请联系管理人员为您加急处理");
            }else{
                showJqueryConfirmWindow("#icon-tishi1",data.msg);
            }
        };
    });
}
function changePerType(){
    if($("#cb9").prop('checked')){
        $("#isZx").val(1);
    }else{
        $("#isZx").val(0);
    }

}
function myResetForm(){
    rolesArray = [];
    siteRolesArray = [];
    jobArray = [];
    $('#userForm').resetForm();
    $('#userForm').clearForm();
    $("#rolesDivs").find(".menu-w3lsgrids-click").each(function(){
        $(this).removeClass("menu-w3lsgrids-click");
    });
    $('#classes').val('');
    /*$('#classes').selectator('refresh');*/
    $("#qsType").val('');
    $('#qsType').selectator('refresh');
}