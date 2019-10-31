var tes = 0;
var isShow = true;

var limCount = 10;

var maxLen = 0;

$(function(){
    var admName = $("#teShiro").html();
    $("#showLAdm").html(admName);
    var s_time = dateFormat($("#teShiro2").html(),'yyyy-MM-dd HH:mm:ss');
    $("#headAdmDate").html(s_time+" 注册");
    var leftMenuStorage = localStorage.getItem("leftMenuStorage");
    $("#permUl").html(leftMenuStorage);
    //添加样式
    addActClass($("#liParent1").children(":eq(0)"));
    $("#liParent1").children(":eq(1)").children(":eq(0)").find("a").css("color","#FFD600");
    loadUserList();
    $("#topShowLp").css("display","none");
    var ajaxFormOption = {
        beforeSubmit: showRequest,
        success: showResponse,
        url: "/auth/setPerm",
        type: "post",
        dataType: "json",
        timeout: 3000
    }
    $("#myFormButton").click(function () {
        $("#permForm").ajaxSubmit(ajaxFormOption);
        return false;
    });
    $("#perType").append("<input class='tgl tgl-flip' id='cb9' type='checkbox'><label class='tgl-btn' data-tg-off='菜单' data-tg-on='功能' for='cb9' style='margin:0 auto;' onclick='changePerType()'></label>");
    $("#icon").selectator({
        labels: {
            search: '请选择菜单图标'
        },
        showAllOptionsOnFocus: true
    });
    $("#icon").next().css("width","100%");
    $(window).scroll(function () {
        if($(window).scrollTop() >= 100){
            $('.toTopRocket').fadeIn(300);
        }else{
            $('.toTopRocket').fadeOut(300);
        }
    });
});
function showRequest(formData, jqForm, options){
    return true;
};
function showResponse(data, status){
    if(data.resultCode==100){
        loadUserList(1,limCount);
        myResetForm();
        $("#compose-modal").modal("hide");
        showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",data.msg);
    }else if(data.code==101){
        //系统接口错误
        showJqueryConfirmWindow("#icon-shangxin1",data.msg);
    }else{
        showJqueryConfirmWindow("#icon-tishi1",data.msg);
    }

}
/**
 * 加载数据
 * */
function loadUserList(){
    $.post("/auth/getPermList",function(data){
        if(!isKickOut(data)){
            if(data.resultCode==100){
                $("#userListUl").html("");
                $.each(data.result,function(ind,val){
                    if( val.isType==0) {
                        if (val.pid == 0) {
                            $("#userListUl").append("<tr id='"+val.id+"-myStyleLi' onmouseover='showED("+val.id+")' onmouseleave='hideED("+val.id+")'>" +
                                "<td data-label='权限名称' style='text-align: left'>" +
                                "<span class='handle'>"+val.name+"</span>" +
                                "</td>" +
                                "<td data-label='权限分类'>" +
                                "<small id='"+val.id+"-nmspan' class='label label-danger' style='font-size: 90%'></small>" +
                                "</td>" +
                                "<td data-label='权限路径'>" +
                                "<small id='"+val.id+"-unspan' class='label label-danger' style='font-size: 90%'><i class='fa fa-link'></i>&nbsp;"+val.page+"</small>" +
                                "</td>" +
                                "<td data-label='权限图标'>" +
                                "<small id='"+val.id+"-atspan' class='label label-danger' style='font-size: 90%'><i class='iconfont "+val.icon+"'></i></small>" +
                                "</td>" +
                                "<td data-label='优先级'>" +
                                "<small id='"+val.id+"-atspan2' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val.zindex+"</small>" +
                                "</td>" +
                                "<td data-label='操作'>" +
                                "<div class='tools' data-label='"+val.id+"' style='color: #f56954;display: none'>" +
                                "<i class='fa fa-plus' data-toggle='tooltip' title='添加子节点' data-original-title='添加子节点' style='cursor: pointer;font-size:1.3em;' onclick='addSun("+val.id+",1"+")'></i>&nbsp;" +
                                "<i class='fa fa-edit' data-toggle='tooltip' title='编辑' data-original-title='编辑' style='cursor: pointer;font-size:1.3em;padding-left: 1em;' onclick='editPerm("+val.id+",0"+")'></i>&nbsp;" +
                                "<i class='fa fa-trash-o' data-toggle='tooltip' title='删除' data-original-title='删除' style='cursor: pointer;font-size:1.3em;padding-left: 1em;' onclick='delPerm("+val.id+","+"\""+val.name+"\")'></i>" +
                                "</div>" +
                                "</td>" +
                                "</tr>");
                            if(val.isType==0){
                                //菜单
                                $("#"+val.id+"-nmspan").html("<i class='fa fa-bars'></i>&nbsp;菜单");
                            }else if(val.isType==1) {
                                //功能
                                $("#" + val.id + "-nmspan").removeClass("label-danger").addClass("label-success");
                                $("#" + val.id + "-nmspan").html("<i class='fa fa-gears'></i>&nbsp;功能");
                            }
                            var sonArray = getParentArry(val.id,data.result);
                            if(sonArray.length>0){
                                //给这个tr加一个图标
                                $("#"+val.id+"-myStyleLi").find("td:eq(0) span").before("<i class='fa fa-caret-down' data-widget='collapse' style='cursor: pointer' onclick='showOrHideSun("+val.id+")'></i>&nbsp;");
                                $.each(sonArray,function (ind2,val2) {
                                    $("#"+val.id+"-myStyleLi").after("<tr id='"+val2.id+"-myStyleLi' class='box-body"+val.id+"' onmouseover='showED("+val2.id+")' onmouseleave='hideED("+val2.id+")'>" +
                                        "<td data-label='权限名称' style='text-align: left'>" +
                                        "<span class='handle' style='padding-left: 15%;'>"+val2.name+"</span>" +
                                        "</td>" +
                                        "<td data-label='权限分类'>" +
                                        "<small id='"+val2.id+"-nmspan' class='label label-danger' style='font-size: 90%'></small>" +
                                        "</td>" +
                                        "<td data-label='权限路径'>" +
                                        "<small id='"+val2.id+"-unspan' class='label label-danger' style='font-size: 90%'><i class='fa fa-link'></i>&nbsp;"+val2.page+"</small>" +
                                        "</td>" +
                                        "<td data-label='权限图标'>" +
                                        "<small id='"+val2.id+"-atspan' class='label label-danger' style='font-size: 90%'><i class='iconfont "+val2.icon+"'></i></small>" +
                                        "</td>" +
                                        "<td data-label='优先级'>" +
                                        "<small id='"+val2.id+"-atspan2' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val2.zindex+"</small>" +
                                        "</td>" +
                                        "<td data-label='操作'>" +
                                        "<div class='tools' data-label='"+val2.id+"' style='color: #f56954;display: none'>" +
                                        "<i class='fa fa-plus' data-toggle='tooltip' title='添加子节点' data-original-title='添加子节点' style='cursor: pointer;font-size:1.3em;' onclick='addSun("+val2.id+",1"+")'></i>&nbsp;" +
                                        "<i class='fa fa-edit' data-toggle='tooltip' title='编辑' data-original-title='编辑' style='cursor: pointer;font-size:1.3em;padding-left: 1em;' onclick='editPerm("+val2.id+",0"+")'></i>&nbsp;" +
                                        "<i class='fa fa-trash-o' data-toggle='tooltip' title='删除' data-original-title='删除' style='cursor: pointer;font-size:1.3em;padding-left: 1em;' onclick='delPerm("+val2.id+","+"\""+val2.name+"\")'></i>" +
                                        "</div>" +
                                        "</td>" +
                                        "</tr>");
                                    if(val2.isType==0){
                                        //菜单
                                        $("#"+val2.id+"-nmspan").html("<i class='fa fa-bars'></i>&nbsp;菜单");
                                    }else if(val2.isType==1){
                                        //功能
                                        $("#"+val2.id+"-nmspan").removeClass("label-danger").addClass("label-success");
                                        $("#"+val2.id+"-nmspan").html("<i class='fa fa-gears'></i>&nbsp;功能");
                                    }
                                });
                            }
                        }else{
                            var sonArray = getParentArry(val.id,data.result);
                            if(sonArray.length>0){
                                var gParent = $("#"+val.id+"-myStyleLi").attr('class');

                                $("#"+val.id+"-myStyleLi").find("td:eq(0) span").css("padding-left","0");
                                //给这个tr加一个图标
                                $("#"+val.id+"-myStyleLi").find("td:eq(0) span").before("<i class='fa fa-caret-down' style='cursor: pointer;padding-left: 16%;' onclick='showOrHideSun("+val.id+")'></i>&nbsp;");
                                $.each(sonArray,function (ind2,val2) {

                                    $("#"+val.id+"-myStyleLi").after("<tr id='"+val2.id+"-myStyleLi' class='"+gParent+" box-body"+val.id+"' onmouseover='showED("+val2.id+")' onmouseleave='hideED("+val2.id+")'>" +
                                        "<td data-label='权限名称' style='text-align: left'>" +
                                        "<span class='handle' style='padding-left: 23%'>"+val2.name+"</span>" +
                                        "</td>" +
                                        "<td data-label='权限分类'>" +
                                        "<small id='"+val2.id+"-nmspan' class='label label-danger' style='font-size: 90%'></small>" +
                                        "</td>" +
                                        "<td data-label='权限路径'>" +
                                        "<small id='"+val2.id+"-unspan' class='label label-danger' style='font-size: 90%'><i class='fa fa-link'></i>&nbsp;"+val2.page+"</small>" +
                                        "</td>" +
                                        "<td data-label='权限图标'>" +
                                        "<small id='"+val2.id+"-atspan' class='label label-danger' style='font-size: 90%'><i class='iconfont "+val2.icon+"'></i></small>" +
                                        "</td>" +
                                        "<td data-label='优先级'>" +
                                        "<small id='"+val2.id+"-atspan2' class='label label-danger' style='font-size: 90%'><i class='fa fa-list-ol'></i>&nbsp;"+val2.zindex+"</small>" +
                                        "</td>" +
                                        "<td data-label='操作'>" +
                                        "<div class='tools' data-label='"+val2.id+"' style='color: #f56954;display: none'>" +
                                        "<i class='fa fa-plus' data-toggle='tooltip' title='添加子节点' data-original-title='添加子节点' style='cursor: pointer;font-size:1.3em;' onclick='addSun("+val2.id+",1"+")'></i>&nbsp;" +
                                        "<i class='fa fa-edit' data-toggle='tooltip' title='编辑' data-original-title='编辑' style='cursor: pointer;font-size:1.3em;padding-left: 1em;' onclick='editPerm("+val2.id+",0"+")'></i>&nbsp;" +
                                        "<i class='fa fa-trash-o' data-toggle='tooltip' title='删除' data-original-title='删除' style='cursor: pointer;font-size:1.3em;padding-left: 1em;' onclick='delPerm("+val2.id+","+"\""+val2.name+"\")'></i>" +
                                        "</div>" +
                                        "</td>" +
                                        "</tr>");

                                    if(val2.isType==0){
                                        //菜单
                                        $("#"+val2.id+"-nmspan").html("<i class='fa fa-bars'></i>&nbsp;菜单");
                                    }else if(val2.isType==1){
                                        //功能
                                        $("#"+val2.id+"-nmspan").removeClass("label-danger").addClass("label-success");
                                        $("#"+val2.id+"-nmspan").html("<i class='fa fa-gears'></i>&nbsp;功能");
                                    }

                                });
                            }

                        }
                    }
                });
                $("[data-toggle='tooltip']").tooltip();
            }else if(data.code==101){
                showJqueryConfirmWindow("#icon-shangxin1",data.msg);
            }else{
                showJqueryConfirmWindow("#icon-tishi1",data.msg);
            }
        }
    });
}
function showED(cur) {
    $("div[data-label='"+cur+"']").css("display","block");
}
function hideED(cur) {
    $("div[data-label='"+cur+"']").css("display","none");
}
function showOrHideSun(did) {
    var box = $("#"+did+"-myStyleLi");
    var bf = box.siblings(".box-body"+did);
    if (!box.hasClass("collapsed-box")) {
        box.addClass("collapsed-box");
        bf.slideUp();
    } else {
        box.removeClass("collapsed-box");
        bf.slideDown();
    }
}

function addSun(pid,flag){

    $("#type").val("");
    $("#id").val("");
    $("#pid").val("");

    myResetForm();

    if(null!=pid){
        //flag[0:开通权限；1：新增子节点权限]
        //type[0:编辑；1：新增]
        var msg = '';
        if(flag==0){
            $("#type").val(1);
            $("#pid").val(0);
            msg = "<i class='iconfont icondrxx08'></i>&nbsp;新增权限";

        }else{
            //设置父id
            $("#type").val(1);
            $("#pid").val(pid);
            msg = "<i class='iconfont icondrxx08'></i>&nbsp;新增子权限";
        }
        //赋值类型
        if($("#cb9").prop('checked')){
            $("#istype").val(1);
        }else{
            $("#istype").val(0);
        }

        $("#chgPerTitle").html(msg);

        $("#compose-modal").modal("show");
    }
}
function editPerm(id,type) {

    $("#type").val("");
    $("#id").val("");
    $("#pid").val("");

    myResetForm();

    if(null!=id){
        $("#type").val(type);
        $("#id").val(id);
        $.post("/auth/getPerm",{"id":id},function(data) {
            if(!isKickOut(data)){
                if(data.resultCode==100){
                    data = data.result;
                    $("input[name='name']").val(data.name);
                    $("input[name='code']").val(data.code);
                    $("input[name='page']").val(data.page);
                    $("input[name='zindex']").val(data.zindex);
                    $("textarea[name='descpt']").val(data.descpt);
                    $("#pid").val(data.pid);
                    data.isType==0?$("#cb9").prop('checked',false):$("#cb9").prop('checked',true);
                    //选中是功能  0是菜单，1是功能 即选中是1
                    $("#chgPerTitle").html("<i class='iconfont icondrxx08'></i>&nbsp;更新权限");
                    $("#compose-modal").modal("show");
                }else if(data.resultCode==101){
                    showJqueryConfirmWindow("#icon-shangxin1",data.msg);
                }else{
                    showJqueryConfirmWindow("#icon-tishi1",data.msg);
                }
            }
        });
    }

}

function delPerm(id,name){
    if(null!=id){
        $.confirm({
            icon: '#icon-tishi1',
            theme: 'modern',
            title: '提示',
            content: '您确定要删除'+name+'权限吗？',
            animation: 'news',//动画
            closeAnimation: 'news',//关闭动画
            buttons: {
                showMyMsg2: {
                    text: '确定',
                    action: function () {

                        $.post("/auth/del",{"id":id},function(data){
                            if(!isKickOut(data)){
                                //没有被踢出
                                if(data.resultCode==100){
                                    //刷新数据
                                    loadUserList(1,limCount);
                                    showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",data.msg);
                                }else if(data.code==101){
                                    //系统接口错误
                                    showJqueryConfirmWindow("#icon-shangxin1",data.msg);
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
        showJqueryConfirmWindow("#icon-tishi1","获取改权限键值失败");
    }

}
function changePerType(){
    if($("#cb9").prop('checked')){
        $("#istype").val(0);
    }else{
        $("#istype").val(1);
    }
}

function getParentArry(id, arry) {

    var newArry = new Array();

    for (var x in arry) {

        if (arry[x].pid == id){
            newArry.push(arry[x]);
        }

    }

    return newArry;
}
function myResetForm(){

    $('#permForm').resetForm();
    $('#permForm').clearForm();

}