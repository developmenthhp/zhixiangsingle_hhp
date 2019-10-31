var tes = 0;
var isShow = true;
var allPage;

var limCount = 15;
var allDataTol;

var initPageplugins = true;

var curPag = 1;

var rolesArray = [];

var sdId;

var isZx;

var ajaxSCJCXXSearchFormOption = {
    beforeSubmit: showSCJCXXSearchRequest,
    success: showSCJCXXSearchResponse,
    url: "/site/getSiteRoleList",
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
    $("#liParent1").children(":eq(1)").children(":eq(4)").find("a").css("color","#FFD600");

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

    $("#roleForm").validationEngine({
        binded:false,
        maxErrorsPerField:1,
        promptPosition: "topLeft:100",
        scroll:true,
        validateNonVisibleFields:true,
        focusFirstField:true,
        ajaxFormValidation: true,
        ajaxFormValidationURL:"/site/setSiteRole",
        ajaxFormValidationMethod:"post",
        onBeforeAjaxFormValidation: showRequest,
        onAjaxFormComplete: showResponse
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
}
function showSCJCXXSearchResponse(data, status){
    initDateTableData(data);
}
function pageChange(i) {
    curPag = accAdd(i,1);
    loadUserList();
    Pagination.Page($(".ht-page"), i, allDataTol, limCount);
}
function loadUserList(){
    ajaxSCJCXXSearchFormOption.data={page:curPag,limit:limCount};
    $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
    return false;
}
function reloadMyDateTable() {
$("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
return false;
}
function showRequest(formData, jqForm, options){
    $("#rolePermIds").val(rolesArray.join(","));
    return true;
};
function showResponse(status, form,data, options){
    if(!isKickOut(data)){
        if(data.resultCode==100){
            loadUserList();
            myResetForm();
            $("#compose-modal").modal("hide");
            showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",data.msg);
        }else if(data.resultCode==101){
            showJqueryConfirmWindow("#icon-shangxin1","操作失败！深感抱歉，请联系管理人员为您加急处理");
        }else{
            showJqueryConfirmWindow("#icon-tishi1",data.msg);
        }
    }
}
function initDateTableData(data){
    if(!isKickOut(data)){
        if(data.resultCode==100){
            if(data.result!=null){
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
                        "<i class='fa fa-edit' style='cursor: pointer;font-size:1.3em;' onclick='updateRole("+val.id+")'></i>&nbsp;" +
                        "<i class='fa fa-trash-o' style='cursor: pointer;font-size:1.3em;padding-left: 1em;' onclick='delRole("+val.id+","+"\""+val.roleName+"\")'></i>" +
                        "</div>" +
                        "</td>" +
                        "</tr>");

                    if(!val.job){
                        isShow = false;
                        /*$("#"+val.id+"-lab").trigger("click");*/
                        //改为添加或移除class  checkbox 点击前先将class和check去除
                        $("#"+val.id).prop("checked",true);
                        $("#"+val.id+"-lab").addClass("cll1");

                        $("#"+val.id+"-unspan").removeClass("doneText");
                        $("#"+val.id+"-mbspan").removeClass("doneText");
                        $("#"+val.id+"-emspan").removeClass("doneText");
                        $("#"+val.id+"-rlspan").removeClass("doneSmal");
                        $("#"+val.id+"-atspan").removeClass("doneSmal");
                    }else{
                        $("#"+val.id+"-unspan").addClass("doneText");
                        $("#"+val.id+"-mbspan").addClass("doneText");
                        $("#"+val.id+"-emspan").addClass("doneText");
                        $("#"+val.id+"-rlspan").addClass("doneSmal");
                        $("#"+val.id+"-atspan").addClass("doneSmal");
                    }
                });
                allDataTol = data.total;
                if(initPageplugins){
                    /*
                     * 初始化插件
                     * @param  object:翻页容器对象
                     * @param  function:回调函数
                     * */
                    Pagination.init($(".ht-page"), pageChange);

                    /*
                    * 首次调用
                    * @param  object:翻页容器对象
                    * @param  number:当前页
                    * @param  number:总页数
                    * @param  number:每页数据条数
                    * */
                    Pagination.Page($(".ht-page"), 0, allDataTol, limCount);
                    initPageplugins = false;
                }else{
                    Pagination.Page($(".ht-page"), curPag-1, allDataTol, limCount);
                }
                /*$("#userListUl").viewer('update');
                $('#userListUl').viewer({
                    url: 'data-original',
                });*/
            }else{
                showJqueryConfirmWindow("#icon-tishi1","未获取到该站点角色数据");
            }
            isShow = true;
        }else if(data.resultCode==101){
            showJqueryConfirmWindow("#icon-shangxin1","获取站点角色信息异常！深感抱歉，请联系管理人员为您加急处理");
        }else{
            showJqueryConfirmWindow("#icon-tishi1",data.msg);
        }
    }
}
//将所有权限查出来
function createRoleDiv(){
    $.post("/site/findSites",function(data){

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
    //清空隐藏域id
    $("#id").val("");
    //重置清空form
    myResetForm();
    if(sdId=="22"&&isZx=="false"){
        $("#form-group-sdId").css("display","block");
    }else{
        $("#form-group-sdId").css("display","none");
    }
    $("#chgUpdTitle").text("新增站点角色");
    $("#compose-modal").modal("show");
}
function updateRole(id) {
    //清空隐藏域id
    $("#id").val("");
    //重置清空form
    myResetForm();
    //isNaN是数字返回false
    if(id!=null && !isNaN(id)){
        $.post("/site/getUpdSiteRoles",{"id":id},function(data){
            if(!isKickOut(data)){
                //没有被踢出
                if(data.resultCode==100){
                    $("#roleName").val(data.result.roleName);
                    $("#descpt").val(data.result.descpt);
                    $("#code").val(data.result.code);
                    $("#id").val(data.result.id);
                    $("#sdId").val(data.result.sdId);
                    $.each(data.result.siteRolePerms,function (ind,val) {
                       $("#firstARole"+val.siteId).click();
                    });
                    $("#chgUpdTitle").text("更新站点角色");
                    if(sdId=="22"&&isZx=="false"){
                        $("#form-group-sdId").css("display","block");
                    }else{
                        $("#form-group-sdId").css("display","none");
                    }
                    $("#compose-modal").modal("show");
                }else if(data.resultCode==101){
                    showJqueryConfirmWindow("#icon-shangxin1","获取站点角色信息异常！深感抱歉，请联系管理人员为您加急处理");
                }else{
                    showJqueryConfirmWindow("#icon-tishi1",data.msg);
                }
            }
        });
    }else{
        showJqueryConfirmWindow("#icon-tishi1","主键获取异常！深感抱歉，请联系管理人员为您加急处理");
    }
}
function delRole(id,name) {
    if(null!=id&&id!=""){
        $.confirm({
            icon: '#icon-tishi1',
            theme: 'modern',
            title: '提示',
            content: '您确定要删除'+name+'角色吗？',
            animation: 'news',//动画
            closeAnimation: 'news',//关闭动画
            /*autoClose: 'showMyMsg2|3000',*/
            buttons: {
                showMyMsg2: {
                    text: '确定',
                    action: function () {

                        $.post("/site/delRole",{"id":id},function(data){
                            if(!isKickOut(data)){
                                //没有被踢出
                                if(data.resultCode==100){
                                    //刷新数据
                                    loadUserList();
                                    showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",'删除'+name+'角色成功！');
                                }else if(data.resultCode==101){
                                    showJqueryConfirmWindow("#icon-shangxin1","操作失败！深感抱歉，请联系管理人员为您加急处理");
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