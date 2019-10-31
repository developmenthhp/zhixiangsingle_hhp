var tes = 0;
var isShow = true;

var limCount = 15;

var maxLen = 0;

var imgPrefix;

$(function(){
    var admName = $("#teShiro").html();
    $("#showLAdm").html(admName);
    var s_time = dateFormat($("#teShiro2").html(),'yyyy-MM-dd HH:mm:ss');
    $("#headAdmDate").html(s_time+" 注册");
    var leftMenuStorage = localStorage.getItem("leftMenuStorage");
    $("#permUl").html(leftMenuStorage);
    addActClass($("#liParent1").children(":eq(0)"));
    $("#liParent1").children(":eq(1)").children(":eq(3)").find("a").css("color","#FFD600");
    loadUserList();
    $("#topShowLp").css("display","none");
    $("#file").on("change",function () {
        uploadIcon();
    });
    //监听窗口，显示隐藏move top
    $(window).scroll(function () {
        if($(window).scrollTop() >= 100){
            $('.toTopRocket').fadeIn(300);
        }else{
            $('.toTopRocket').fadeOut(300);
        }
    });
    $("#permForm").validationEngine({
        binded:false,
        maxErrorsPerField:1,
        promptPosition: "topLeft:100",
        scroll:true,
        validateNonVisibleFields:true,
        focusFirstField:true,
        ajaxFormValidation: true,
        ajaxFormValidationURL:"/site/setSite",
        ajaxFormValidationMethod:"post",
        onBeforeAjaxFormValidation: showRequest,
        onAjaxFormComplete: showResponse
    });
});

function showRequest(formData, jqForm, options){
    return true;
};
function showResponse(status, form,data, options){
    if(!isKickOut(data)){
        if(data.resultCode==100){
            loadUserList(1,limCount);
            myResetForm();
            $("#compose-modal").modal("hide");
            showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",data.msg);
        }else if(data.resultCode==101){
            showJqueryConfirmWindow("#icon-shangxin1", "操作失败！深感抱歉，请联系管理人员为您加急处理");
        }else{
            showJqueryConfirmWindow("#icon-tishi1",data.msg);
        }
    }
}

/**
 * 加载数据
 * */
function loadUserList(){
    $.post("/site/getSiteList",function(data){
        if(!isKickOut(data)){
            if(data.resultCode==100){
                $("#userListUl").html("");
                imgPrefix = data.obj;
                $.each(data.result,function(ind,val){
                    $("#userListUl").append("<tr id='"+val.id+"-myStyleLi' onmouseover='showED("+val.id+")' onmouseleave='hideED("+val.id+")'>" +
                        "<td data-label='站点名称' style='text-align: left'>" +
                        "<span class='handle'>"+val.name+"</span>" +
                        "</td>" +
                        "<td data-label='站点图标'>" +
                        "<div class='my-table-imgDiv'><img data-original='"+data.obj+val.photo+"' alt='站点图标' src='"+data.obj+val.photo+"' class='online' style='width:100%;cursor:zoom-in;'/></div>" +
                        "</td>" +
                        "<td data-label='站点地址描述'>" +
                        "<small id='"+val.id+"-descript' class='label label-danger' style='font-size: 90%'><i class='fa fa-link'></i>&nbsp;"+val.address+"</small>" +
                        "</td>" +
                        "<td data-label='操作'>" +
                        "<div class='tools' data-label='"+val.id+"' style='color: #f56954;display: none;'>" +
                        "<i class='fa fa-edit' data-toggle='tooltip' title='编辑' data-original-title='编辑' style='cursor: pointer;font-size:1.3em;' onclick='editPerm("+val.id+",0"+")'></i>&nbsp;" +
                        "<i class='fa fa-trash-o' data-toggle='tooltip' title='' data-original-title='删除' style='cursor: pointer;font-size:1.3em;padding-left: 1em;' onclick='delPerm("+val.id+","+"\""+val.name+"\")'></i>" +
                        "</div>" +
                        "</td>" +
                        "</tr>");
                });
                $("[data-toggle='tooltip']").tooltip();
            }else if(data.resultCode==101){
                showJqueryConfirmWindow("#icon-shangxin1","获取站点数据异常！深感抱歉，请联系管理人员为您加急处理");
            }else{
                showJqueryConfirmWindow("#icon-tishi1",data.msg);
            }
            $('#userListUl').viewer({
                url: 'data-original',
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
function addSun(pid,flag){
    $("#type").val("");
    $("#id").val("");
    $("#iconImg").val("");
    $("#iconImgDiv").remove();
    $(".myImgFirst").attr("src","");
    $(".myImgFirst").css("display","none");
    $(".myImgSecond").css("display","block");
    myResetForm();
    if(null!=pid){
        //flag[0:开通权限；1：新增子节点权限]
        //type[0:编辑；1：新增]
        var msg = '';
        if(flag==0){
            $("#type").val(1);
            $("#pid").val(0);
            msg = '新增站点';
        }else{
            //设置父id
            $("#type").val(1);
            $("#pid").val(pid);
            msg = "新增子站点";
        }
        //赋值类型
        if($("#cb9").prop('checked')){
            $("#istype").val(1);
        }else{
            $("#istype").val(0);
        }
        $("#chgSiteTitle").html("<i class='iconfont iconzhandian'></i>&nbsp;"+msg);
        $("#compose-modal").modal("show");
    }
}
function editPerm(id,type) {
    $("#type").val("");
    $("#id").val("");
    $("#pid").val("");
    $("#iconImg").val("");
    //赋值经纬度
    $("#lng").val("");
    $("#lat").val("");
    $("#iconImgDiv").remove();
    myResetForm();

    if(null!=id){
        $("#type").val(type);
        $("#id").val(id);
        $.post("/site/getSite",{"id":id},function(data) {
            if(!isKickOut(data)){
                if(data.resultCode==100){
                    if(null!=data.result){
                        $("input[name='name']").val(data.result.name);
                        $("input[name='zindex']").val(data.result.zindex);
                        $("input[name='photo']").val(data.result.photo);
                        $("input[name='sdId']").val(data.result.sdId);
                        $("textarea[name='address']").val(data.result.address);
                        if(data.result.warningTime){
                            $("input[name='warningTime']").val(data.warningTime);
                        }
                        if(data.result.overTime){
                            $("input[name='overTime']").val(data.overTime);
                        }
                        //赋值经纬度
                        $("#lng").val(data.result.lng);
                        $("#lat").val(data.result.lat);

                        $("#pid").val(data.result.pid);

                        $("#zd_img").after("<div id='iconImgDiv' class='form-group'><div style='width: 10em;margin:0 auto;'><img alt='站点图标' src='"+data.result.photo+"' class='online' style='width:100%'/></div></div>");
                        if(data.result.photo&&data.result.photo!=""){
                            $(".myImgFirst").attr("src",data.obj+data.result.photo);
                            $(".myImgFirst").css("display","block");
                            $(".myImgSecond").css("display","none");
                        }
                        //选中是功能  0是菜单，1是功能 即选中是1
                        //$("#chgUpdTitle").text("更新站点");
                        $("#chgSiteTitle").html("<i class='iconfont iconzhandian'></i>&nbsp;更新站点");
                        $("#compose-modal").modal("show");
                    }else{
                        showJqueryConfirmWindow("#icon-tishi1","未获取到该站点信息");
                    }
                }else if(data.resultCode==101){
                    showJqueryConfirmWindow("#icon-shangxin1","获取站点数据异常！深感抱歉，请联系管理人员为您加急处理");
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
            content: '您确定要删除'+name+'站点吗？',
            animation: 'news',//动画
            closeAnimation: 'news',//关闭动画
            /*autoClose: 'showMyMsg2|3000',*/
            buttons: {
                showMyMsg2: {
                    text: '确定',
                    action: function () {
                        $.post("/site/del",{"id":id},function(data){
                            if(!isKickOut(data)){
                                //没有被踢出
                                if(data.resultCode==100){
                                    //刷新数据
                                    loadUserList(1,limCount);
                                    showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14","删除站点成功");
                                }else if(data.resultCode==101){
                                    showJqueryConfirmWindow("#icon-shangxin1", "删除站点失败！深感抱歉，请联系管理人员为您加急处理");
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
function doMyLogic(myResult) {
    $("#iconImg").val(myResult.result);
    $(".myImgFirst").attr("src",myResult.obj+myResult.result);
    $(".myImgFirst").css("display","block");
    $(".myImgSecond").css("display","none");
}
function myResetForm(){
    $('#permForm').resetForm();
    $('#permForm').clearForm();
}