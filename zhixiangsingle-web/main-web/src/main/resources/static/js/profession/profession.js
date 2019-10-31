var tes = 0;
var isShow = true;
var allPage;

var limCount = 10;
var allDataTol;

var initPageplugins = true;

var curPag = 1;

var sdId;

var isZx;

var ajaxSCJCXXSearchFormOption = {
    beforeSubmit: showSCJCXXSearchRequest,
    success: showSCJCXXSearchResponse,
    url: "/profession/getProfessions",
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

    var ajaxFormOption = {
        beforeSubmit: showRequest,
        success: showResponse,
        url: "/profession/setProfessions",
        type: "post",
        dataType: "json",
        timeout: 3000
    }

    $("#myFormButton").click(function () {
        $("#userForm").ajaxSubmit(ajaxFormOption);
        return false;
    });

    $(window).scroll(function () {
        if($(window).scrollTop() >= 100){
            $('.toTopRocket').fadeIn(300);
        }else{
            $('.toTopRocket').fadeOut(300);
        }
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

function showRequest(formData, jqForm, options){
    return true;
}
function showResponse(data, status){
    if(data.resultCode==100){
        loadUserList(1,limCount);
        myResetForm();
        $("#compose-modal").modal("hide");
        showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",data.msg);
    }else if(data.code==101){
        //系统接口错误
        showJqueryConfirmWindow("#icon-shangxin1","更新或新增用户异常！深感抱歉，请联系管理人员为您加急处理");
    }else{
        showJqueryConfirmWindow("#icon-tishi1",data.msg);
    }
}
function initDateTableData(data){

    if(!isKickOut(data)){
        if(data.resultCode==100){
            if(data.result&&data.result.length>0){
                $("#userListUl").html("");
                $.each(data.result,function (ind,val) {
                    $("#userListUl").append("<tr id='"+val.id+"-myStyleLi' onmouseover='showED("+val.id+")' onmouseleave='hideED("+val.id+")'>" +
                        "<td data-label='工种名' class='comMove'>" +
                        "<span id='"+val.id+"-unspan' class='text'>"+val.name+"</span>" +
                        "</td>" +
                        "<td data-label='操作'>" +
                        "<div class='tools' data-label='"+val.id+"' style='color: #f56954;display: none'>" +
                        "<i class='fa fa-edit' style='cursor: pointer' onclick='getProfession("+val.id+","+val.sdId+","+"\""+val.name+"\")'></i>&nbsp;" +
                        "<i class='fa fa-trash-o' style='cursor: pointer' onclick='delProfession("+val.id+","+"\""+val.name+"\")'></i>" +
                        "</div>" +
                        "</td>" +
                        "</tr>");
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
                    "<td data-label='工种数据' colspan='6' class='comMove'>" +
                    "<small class='label label-danger' style='font-size: 80%'><i class='iconfont iconweikongtishi'></i>&nbsp;未查找到数据</small>" +
                    "</td>" +
                    "</tr>");
            }
        }else if(data.resultCode==101){
            showJqueryConfirmWindow("#icon-shangxin1","工种列表查询失败！深感抱歉，请联系管理人员为您加急处理");
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
function getProfession(id,sdIdM,name) {
    myResetForm();
    $("#id").val(id);
    $("#username").val(name);
    $("#chgUpdTitle").text("更新工种");
    if(sdId=="22"&&isZx=="false"){
        $("#sdId").val(sdIdM);
        $("#form-group-sdId").css("display","block");

    }else{
        $("#sdId").val("");
        $("#form-group-sdId").css("display","none");
    }

    $("#compose-modal").modal("show");
}


function delProfession(id,name) {

    var currentUser=$("#currentUser").html();

    if(null!=id&&id!=""){
        $.confirm({
            icon: '#icon-tishi1',
            theme: 'modern',
            title: '是否删除',
            content: '您确定要删除'+name+'工种吗？',
            animation: 'news',
            closeAnimation: 'news',
            buttons: {
                showMyMsg2: {
                    text: '确定',
                    action: function () {
                        $.post("/profession/delProfession",{"id":id},function(data){
                            if(!isKickOut(data)){
                                if(data.resultCode==100){
                                    loadUserList(1,limCount);
                                    showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",name+"工种已成功删除");
                                }else if(data.resultCode==101){
                                    showJqueryConfirmWindow("#icon-shangxin1","删除"+name+"工种失败！深感抱歉，请联系管理人员为您加急处理");
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
    }else{
        showJqueryConfirmWindow("#icon-tishi1","删除前主键获取失败！深感抱歉，请联系管理人员为您加急处理");
    }
}

function changeJobStatus(curJob,curDId){
    $("#"+curDId).prop("checked",false);
    var isJob;
    var cfText;
    if(curJob){
        //该员工已离职
        cfText = '是否确认该员工在职';
        isJob = 0;
    }else{
        //该员工在职
        cfText = '是否确认该员工离职';
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

/*新增用户*/
function createUser() {
    $("#id").val("");
    myResetForm();
    $("#chgUpdTitle").text("创建工种");
    if(sdId=="22"&&isZx=="false"){
        $("#form-group-sdId").css("display","block");
    }else{
        $("#form-group-sdId").css("display","none");
    }
    $("#compose-modal").modal("show");
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
    $('#userForm').resetForm();
    $('#userForm').clearForm();
    $("#rolesDivs").find(".menu-w3lsgrids-click").each(function(){
        $(this).removeClass("menu-w3lsgrids-click");
    });
}