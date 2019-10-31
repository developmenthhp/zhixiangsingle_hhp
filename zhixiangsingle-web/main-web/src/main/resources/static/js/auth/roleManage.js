var tes = 0;
var isShow = true;
var allPage;

var limCount = 10;
var allDataTol;

var initPageplugins = true;

var curPag = 1;

var rolesArray = [];

var sdId;

var isZx;

var addValidateFlag = false;
var updValidateFlag = false;

var ajaxSCJCXXSearchFormOption = {
    beforeSubmit: showSCJCXXSearchRequest,
    success: showSCJCXXSearchResponse,
    url: "/auth/getRoleList",
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
    $("#liParent1").children(":eq(1)").children(":eq(1)").find("a").css("color","#FFD600");

    loadUserList();

    createRoleDiv();

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

    $(window).scroll(function () {
        if($(window).scrollTop() >= 100){
            $('.toTopRocket').fadeIn(300);
        }else{
            $('.toTopRocket').fadeOut(300);
        }
    });
    $("#roleForm").validationEngine({
        validationEventTriggers:"",
        maxErrorsPerField:1,
        inlineValidation: true,
        success :  false,
        promptPosition: "topLeft:100",
        scroll:true,
        focusFirstField:true
    });
    $("#compose-modal").on('hide.bs.modal', function (){
        $('#roleForm').validationEngine('hideAll');
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
function submitRole() {
    var updOrAdd = $("#id").val();
    var subOptions;
    if(updOrAdd!=undefined&&updOrAdd!=null&&updOrAdd!=''){
        subOptions = {
            beforeSubmit: showRequest,
            success: showResponse,
            url: "/auth/setRole",
            type: "post",
            dataType: "json",
            timeout: 10000
        }

    }else{
        subOptions = {
            beforeSubmit: showRequest,
            success: showResponse,
            url: "/auth/addRole",
            type: "post",
            dataType: "json",
            timeout: 3000
        }
    }
    if($('#roleForm').validationEngine('validate')){
        $("#rolePermIds").val(rolesArray.join(","));
        $("#roleForm").ajaxSubmit(subOptions);
        return false;
    };
}
function showRequest(formData, jqForm, options){
    return true;
};
function showResponse(data, status){
    if(data.resultCode==100){
        loadUserList();
        myResetForm();
        $("#compose-modal").modal("hide");
        showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",data.msg);
    }else if(data.resultCode==101){
        //系统接口错误
        showJqueryConfirmWindow("#icon-shangxin1",data.msg);
    }else{
        showJqueryConfirmWindow("#icon-tishi1",data.msg);
    }

}
function initDateTableData(data){
    //没有被踢出
    if(!isKickOut(data)){
        if(data.resultCode==100){
            if(data.result){
                $("#userListUl").html("");
                $.each(data.result,function (ind,val) {
                    $("#userListUl").append("<tr id='"+val.id+"-myStyleLi' onmouseover='showED("+val.id+")' onmouseleave='hideED("+val.id+")'>" +
                        "<td data-label='角色编号' class='comMove'>" +
                        "<span class='handle'>"+val.code+"</span>" +
                        "</td>" +
                        "<td data-label='角色名称' class='comMove'>" +
                        "<small id='"+val.id+"-nmspan' class='label label-danger' style='font-size: 80%'><i class='fa fa-user'></i>&nbsp;"+val.roleName+"</small>" +
                        "</td>" +
                        "<td data-label='角色描述' class='comMove'>" +
                        "<span id='"+val.id+"-unspan' class='text'>"+val.descpt+"</span>" +
                        "</td>" +
                        "<td data-label='添加时间' class='comMove'>" +
                        "<small id='"+val.id+"-atspan' class='label label-danger'><i class='fa fa-clock-o'></i>&nbsp;"+val.insertTime+"</small>" +
                        "</td>" +
                        "<td data-label='操作'>" +
                        "<div class='tools' data-label='"+val.id+"' style='color: #f56954;display: none'>" +
                        "<i class='fa fa-edit' data-toggle='tooltip' title='编辑' data-original-title='编辑' style='cursor: pointer;font-size:1.3em;' onclick='updateRole("+val.id+")'></i>&nbsp;" +
                        "<i class='fa fa-trash-o' data-toggle='tooltip' title='删除' data-original-title='删除' style='cursor: pointer;font-size:1.3em;padding-left: 1em;' onclick='delRole("+val.id+","+"\""+val.roleName+"\")'></i>" +
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
                //加一个字体图标无数据
            }
            $("[data-toggle='tooltip']").tooltip();
        }else if(data.resultCode==101){
            showJqueryConfirmWindow("#icon-shangxin1",data.msg);
        }else{
            showJqueryConfirmWindow("#icon-tishi1",data.msg);
        }


    }
}
function createRoleDiv(){
    $.post("/auth/findPerms",function(data){
        if (data !=null&&data.length>0) {
            $.each(data,function(ind,val){
                $("#rolesDivs").append("<div class='col-md-4 col-sm-4 col-xs-6 menu-w3lsgrids'>" +
                    "<a id='firstARole"+val.id+"' href='javascript:void(0)' data-toggle='tooltip' title='' data-original-title='"+val.name+"' onclick='chooseRoles("+val.id+",this)'>"+val.name+"</a></div>");
            });
        }

    });
}
function showED(cur) {
    $("div[data-label='"+cur+"']").css("display","block");
}
function hideED(cur) {
    $("div[data-label='"+cur+"']").css("display","none");
}
function befShowAddMod(){
    $("#id").val("");
    myResetForm();
    if(sdId=="22"&&isZx=="false"){
        $("#form-group-sdId").css("display","block");
    }else{
        $("#form-group-sdId").css("display","none");
    }
    $("#chgRolManTitle").html("<i class='iconfont iconjiaosefenpei'></i>&nbsp;开通角色");
    $("#compose-modal").modal("show");
}
function updateRole(id) {
    $("#id").val("");
    myResetForm();
    if(id!=null && !isNaN(id)){
        $.post("/auth/getUpdRoles",{"id":id},function(data){
            if(!isKickOut(data)){
                //没有被踢出
                if(data.resultCode==100){
                    $("#roleName").val(data.result.roleName);
                    $("#descpt").val(data.result.descpt);
                    $("#code").val(data.result.code);
                    $("#id").val(data.result.id);
                    $("#sdId").val(data.result.sdId);
                    $.each(data.result.rolePerms,function (ind,val) {
                       $("#firstARole"+val.permitId).click();
                    });

                    $("#chgRolManTitle").html("<i class='iconfont iconjiaoseguanli'></i>&nbsp;更新角色");
                    if(sdId=="22"&&isZx=="false"){
                        $("#form-group-sdId").css("display","block");
                    }else{
                        $("#form-group-sdId").css("display","none");
                    }
                    $("#compose-modal").modal("show");
                }else if(data.resultCode == 101){
                    showJqueryConfirmWindow("#icon-shangxin1",data.msg);
                }else{
                    showJqueryConfirmWindow("#icon-tishi1",data.msg);
                }
            }
        });

    }else{
        showJqueryConfirmWindow("#icon-tishi1","键值获取异常");
    }
}
function delRole(id,name) {

    if(null!=id&&id!=""){
        $.confirm({
            icon: '#icon-tishi1',
            theme: 'modern',
            title: '提示',
            content: '您确定要删除'+name+'角色吗？',
            animation: 'news',
            closeAnimation: 'news',
            buttons: {
                showMyMsg2: {
                    text: '确定',
                    action: function () {
                        $.post("/auth/delRole",{"id":id},function(data){
                            if(!isKickOut(data)){
                                if(data.resultCode==100){
                                    loadUserList();
                                    showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",'删除角色成功！');
                                }else if(data.resultCode==101){
                                    showJqueryConfirmWindow("#icon-shangxin1",'删除角色出错，请您稍后再试！');
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
        showJqueryConfirmWindow("#icon-tishi1","键值获取异常");
    }

}
function chooseRoles(id,curA){

    var curAClass = $(curA).attr("class");

    if(curAClass!=undefined&&curAClass!="undefined"){
        if(curAClass.indexOf("menu-w3lsgrids-click")!=-1){
            //当前已选中，设置不选中，删除数组里的权限id
            // 删除起始下标为1，长度为1的一个值(可以理解为从这个数开始删除几个)，len设置的1，如果为0，则数组不变
            rolesArray.splice($.inArray(id,rolesArray),1);
            $(curA).removeClass("menu-w3lsgrids-click");
        }else{
            rolesArray.push(id);
            //当前未选中，加入class
            $(curA).addClass("menu-w3lsgrids-click");
        }
    }else{
        rolesArray.push(id);
        //当前未选中，加入class
        $(curA).addClass("menu-w3lsgrids-click");
    }

}
function myResetForm(){
    //清空权限数组
    rolesArray = [];
    $("#rolePermIds").val("");
    $('#roleForm').resetForm();
    $('#roleForm').clearForm();
    $("#rolesDivs").find(".menu-w3lsgrids-click").each(function(){
        $(this).removeClass("menu-w3lsgrids-click");
    });
}