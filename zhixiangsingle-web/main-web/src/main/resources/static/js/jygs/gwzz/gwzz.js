var tes = 0;
var isShow = true;
var allPage;

var limCount = 12;
var allDataTol;

var initPageplugins = true;

var curPag = 1;

var rolesArray = [];

var sdId;

var isZx;

var smallJson={};
var reloadSelectatorJson={};
var smallArray = [];
var meteringNameArray;
var displayAreaArray;
var whouseIdArray;
var suppIdArray;

var isManySite = false;
var ajaxSCJCXXSearchFormOption = {
    beforeSubmit: showSCJCXXSearchRequest,
    success: showSCJCXXSearchResponse,
    url: "/jobResponsibilities/getJobResponsibilitiesList",
    type: "post",
    data:{page:curPag,limit:limCount},
    dataType: "json",
    timeout: 12000
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

    var allUserSites = getUserSites();
    if(allUserSites.result&&allUserSites.result.length>1){
        $("#form-group-sdId").css("display","block");
        //$("#userSites").css("display","block");
        $.each(allUserSites.result,function (ind,val) {
            var curSId = val.sdId;
            var sPho = allUserSites.obj[curSId].photo;
            $("#userSites").append("<option value='"+val.sdId+"' data-subtitle='"+val.address+"' data-left='<img src="+sPho+">'>"+val.name+"</option>");
            //
            $("#selectSites").append("<option value='"+val.sdId+"' data-subtitle='"+val.address+"' data-left='<img src="+sPho+">'>"+val.name+"</option>");
        });
        isManySite = true;
    }else{
        $("#sdId").val(sdId);
        //$("#userSites").css("display","none");
        $("#form-group-sdId").css("display","none");
    }
    $("#userSites").selectator({
        labels: {
            search: '选择站点'
        },
        showAllOptionsOnFocus: true
    });
    $("#userSites").next().css("width","100%");
    $("#userSites").next().css("min-height","3em");
    $("#userSites").next().find(".selectator_input").css("background","#1b1b1b");
    $("#userSites").on("change",function (e) {
        $(".search--open").css("background","rgba(0, 0, 0, 0.68)");
    });
    $("#selectSites").selectator({
        labels: {
            search: '选择站点'
        },
        showAllOptionsOnFocus: true
    });
    $("#selectSites").next().css("width","100%");
    $("#selectSites").next().find(".selectator_input").css("background","#1b1b1b");
    //为用户加站点角色的时候需要判断角色为长度为1时，需要判断设置的角色站点id是否和
    $("#comInfoForm").validationEngine({
        binded:false,
        maxErrorsPerField:1,
        promptPosition: "topLeft:100",
        scroll:true,
        showOneMessage:true,
        validateNonVisibleFields:true,
        focusFirstField:true,
        ajaxFormValidation: true,
        ajaxFormValidationURL:"/jobResponsibilities/setJobResponsibilities",
        ajaxFormValidationMethod:"post",
        onBeforeAjaxFormValidation: showRequest,
        onAjaxFormComplete: showResponse,
        onFieldFailure:setErrorStyle
    });

    $(window).scroll(function () {
        if($(window).scrollTop() >= 100){
            $('.toTopRocket').fadeIn(300);
        }else{
            $('.toTopRocket').fadeOut(300);
        }
    });

    $(".selectator_options").css("z-index","999");
    $(".selectator_input").css("z-index","999");

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
    if($("#userSites").val()){
        $("#alertSdId").val($("#userSites").val().toString());
    }
    ajaxSCJCXXSearchFormOption.data={page:curPag,limit:limCount};
    $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
    return false;
}
function reloadMyDateTable() {
    if($("#userSites").val()){
        $("#alertSdId").val($("#userSites").val().toString());
    }else{
        $("#alertSdId").val("");
    }
    curPag = 1;
    ajaxSCJCXXSearchFormOption.data={page:curPag,limit:limCount};
    $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
    return false;
}
function setErrorStyle() {
    setTimeout(function () {

    },1000);
}
function showRequest(formData, jqForm, options){
    var img = '';
    $.each($(".myImgFirst"),function(ind,val){
        var dataImg = $(val).attr("data-img");
        if(dataImg!=undefined&&dataImg!=null&&dataImg!=''){
            if(img==''){
                img = dataImg;
            }else{
                img = img+","+dataImg;
            }
        }
    });
    $("#imgPath").val(img);

    if($("#selectSites").val()!=undefined&&$("#selectSites").val()!=null&&$("#selectSites").val()!=''){
        $("#sdIds").val($("#selectSites").val().join(","));
    }else{
        $("#sdIds").val("");
    }
    return true;
};
function showResponse(status, form,data, options){
    if(!isKickOut(data)){
        if(data.resultCode==100){
            loadUserList();
            myResetForm();
            $("#comInfo-modal").modal("hide");
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
                    if(val){
                        var comInfoTopStr = '';
                        var addTime = '';
                        if(val.addTime&&val.addTime!=''){
                            addTime = val.addTime;
                        }
                        var jobTitle = '';
                        if(val.jobTitle&&val.jobTitle!=''){
                            jobTitle = val.jobTitle;
                        }
                        if(jobTitle!=''){
                            if(addTime!=''){
                                comInfoTopStr = jobTitle+"-"+addTime;
                            }else{
                                comInfoTopStr = jobTitle;
                            }
                        }else{
                            if(addTime!=''){
                                comInfoTopStr = addTime;
                            }
                        }
                        var jobResponsibilities = '';
                        if(val.jobResponsibilities&&val.jobResponsibilities!=''){
                            jobResponsibilities = val.jobResponsibilities;
                        }
                        var ibImgPath = getManyPicture(val.imgPath,val.sitePhoto,val.siteName,val.picturePrefix);

                        $("#userListUl").append("<div class='col-md-3 col-sm-3 col-xs-12'>" +
                            "<div class='tpl-table-images-content'>" +
                            "<div class='tpl-table-images-content-i-time-hhp' style='height: 3.2em;'>"+comInfoTopStr+"</div>" +
                            /*"<div class='tpl-i-title-hhpHeight' style='height: 6.5em;' data-toggle='tooltip' title='' data-original-title=''>" +
                            /!*"<div>"+registration+"</div>"+
                            "<div>"+legalRepresentative+"</div>"+
                            "<div>"+topStr+"</div>"+
                            "<div>"+address+"</div>"+
                            "<div>"+deadline+"</div>"+*!/
                            "</div>" +*/

                            ibImgPath+
                            "<div class='tpl-table-images-content-block'>" +
                            "<div class='tpl-i-font' style='height: 4.5em;' data-toggle2='tooltip' title='"+jobResponsibilities+"' data-original-title='"+jobResponsibilities+"'>" + jobResponsibilities +
                            "</div>" +
                            "<div class='tpl-i-more'>" +
                            /*"<ul>" +
                            "<li><span class='am-icon-qq am-text-warning'> 100+</span></li>" +
                            "<li><span class='am-icon-weixin am-text-success'> 235+</span></li>" +
                            "<li><span class='am-icon-github font-green'> 600+</span></li>" +
                            "</ul>" +*/
                            "</div>" +
                            "<div class='am-btn-toolbar'>" +
                            "<div class='am-btn-group am-btn-group-xs tpl-edit-content-btn'>" +
                            "<button type='button' class='am-btn am-btn-default am-btn-secondary' style='width: 50% !important;' onclick='updateRole("+val.id+","+val.sdId+")'><i class='iconfont iconbianji3'></i> 编辑</button>" +
                            "<button type='button' class='am-btn am-btn-default am-btn-danger' style='width: 50% !important;' onclick='delRole("+val.id+","+val.sdId+","+"\""+jobTitle+"\")'><i class='iconfont iconshanchutishi'></i> 删除</button>" +
                            /*"<button type='button' class='am-btn am-btn-default am-btn-warning' style='width: 33% !important;' onclick='exportSingle("+val.id+")'><i class='iconfont iconexport1'></i> 导出</button>" +*/
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            "</div>");

                        //只取了部分，不合适
                        //$("#areaId").find("option[value='"+val.areaId+"'][data-siteId='"+val.sdId+"']").attr("data-right","已提交实景");
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
                $("#userListUl").viewer('update');
                $('#userListUl').viewer({
                    url: 'data-original',
                });

                $("[data-toggle='tooltip']").tooltip();
                $("[data-toggle2='tooltip']").tooltip({
                    placement:"bottom"
                });
            }else{
                showJqueryConfirmWindow("#icon-tishi1","未获取到岗位职责信息");
            }
            isShow = true;
        }else if(data.resultCode==101){
            showJqueryConfirmWindow("#icon-shangxin1","获取岗位职责信息异常！深感抱歉，请联系管理人员为您加急处理");
        }else{
            showJqueryConfirmWindow("#icon-tishi1",data.msg);
        }
    }
}
function befShowAddMod(){
    //清空隐藏域id
    $("#id").val("");
    $("#imgPath").val("");
    $("#sdId").val("");
    $("#sdIds").val("");
    $.each($(".myImgFirst"),function(ind,val){
        $(val).parent().remove();
    });
    $("#selectSites").val("");
    $("#selectSites").selectator('refresh');
    $("#form-group-sdId").css("display","block");
    $("#form-group-sdId").before("<div class='up-img-cover' style='width:100%;text-align: center;'>" +
        "<img class='am-circle myImgFirst' src='../../../images/VRPicture.png' onclick='showMyCropperImgModel(0)' style='display: none;width: 8em;height:70px;margin: 0 auto;border-radius: 0;transform: translateY(-4%);'/>" +
        "<i id='up-img-touch999' onclick='showMyCropperImgModel(0)' class='iconfont iconVRtupian myImgSecond' style='display:block;font-size: 6em;line-height: 110px;'></i></div>");

    //重置清空form
    myResetForm();
    $("#chgBusTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;新增岗位职责");
    $("#comInfo-modal").modal("show");
}
function updateRole(id,updSdId) {
    //清空隐藏域id
    $("#id").val("");
    $("#imgPath").val("");
    $("#sdId").val("");
    $("#sdIds").val("");
    $("#selectSites").val("");
    $("#selectSites").selectator('refresh');
    $("#form-group-sdId").css("display","none");
    $.each($(".myImgFirst"),function(ind,val){
        $(val).parent().remove();
    });
    //重置清空form
    myResetForm();
    //isNaN是数字返回false
    if(id!=null && !isNaN(id)){
        $.post("/jobResponsibilities/getUpdJobResponsibilities",{"id":id,"sdId":updSdId},function(data){
            if(!isKickOut(data)){
                //没有被踢出
                if(data.resultCode==100){
                    $("#id").val(data.result.id);
                    $("#sdId").val(data.result.sdId);
                    $("#imgPath").val(data.result.imgPath);
                    $("#jobTitle").val(data.result.jobTitle);
                    $("#jobResponsibilities").val(data.result.jobResponsibilities);
                    if(data.result.imgPath&&data.result.imgPath!=""){
                        var imgs = data.result.imgPath.split(",");
                        $.each(imgs,function (ind,val) {
                            if(val.indexOf("http:")==-1){
                                $("#form-group-sdId").before("<div class='up-img-cover' style='width:100%;text-align: center;'>" +
                                    "<img class='am-circle myImgFirst' src='"+data.result.picturePrefix+val+"' onclick='showMyCropperImgModel("+ind+")' data-img='"+val+"' style='display: block;width: 8em;height:70px;margin: 0 auto;border-radius: 0;transform: translateY(-4%);'/>" +
                                    "<i id='up-img-touch"+ind+"' onclick='showMyCropperImgModel("+ind+")' class='iconfont iconVRtupian myImgSecond' style='display:none;font-size: 6em;line-height: 110px;'></i></div>");
                            }else{
                                $("#form-group-sdId").before("<div class='up-img-cover' style='width:100%;text-align: center;'>" +
                                    "<img class='am-circle myImgFirst' src='"+val+"' onclick='showMyCropperImgModel("+ind+")' data-img='"+val+"' style='display: block;width: 8em;height:70px;margin: 0 auto;border-radius: 0;transform: translateY(-4%);'/>" +
                                    "<i id='up-img-touch"+ind+"' onclick='showMyCropperImgModel("+ind+")' class='iconfont iconVRtupian myImgSecond' style='display:none;font-size: 6em;line-height: 110px;'></i></div>");
                            }
                        });
                    }
                    $("#chgBusTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;更新岗位职责");

                    $("#comInfo-modal").modal("show");
                }else if(data.resultCode==101){
                    showJqueryConfirmWindow("#icon-shangxin1","获取岗位职责信息异常！深感抱歉，请联系管理人员为您加急处理");
                }else{
                    showJqueryConfirmWindow("#icon-tishi1",data.msg);
                }
            }
        });
    }else{
        showJqueryConfirmWindow("#icon-tishi1","主键获取异常！深感抱歉，请联系管理人员为您加急处理");
    }
}
function delRole(id,delSdId,name) {
    if(null!=id&&id!=""){
        $.confirm({
            icon: '#icon-tishi1',
            theme: 'modern',
            title: '提示',
            content: '您确定要删除'+name+'职责吗？',
            animation: 'news',//动画
            closeAnimation: 'news',//关闭动画
            /*autoClose: 'showMyMsg2|3000',*/
            buttons: {
                showMyMsg2: {
                    text: '确定',
                    action: function () {

                        $.post("/jobResponsibilities/delJobResponsibilities",{"id":id,"sdId":delSdId},function(data){
                            if(!isKickOut(data)){
                                //没有被踢出
                                if(data.resultCode==100){
                                    //刷新数据
                                    loadUserList();
                                    showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",'删除'+name+'职责成功！');
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

function exportSingle() {
    var exportSdIds = '';
    if($("#userSites").val()){
        exportSdIds = $("#userSites").val().toString();
    }
    window.location.href="/jobResponsibilities/writeJobResponsibilities?sdIds="+exportSdIds+"&jobTitle="+$("#topJTitle").val();
}
function showOpenColor() {
    $(".search").css("background","rgba(0, 0, 0, 0.68)");
    $(".search").css("overflow-y","scroll");
}
function setOpenColor() {
    $(".search").css("background","none");
    $(".search").css("overflow-y","hidden");
}

function doMyLogic(myResult) {
    var dataIndex = $(".up-modal-frame .up-btn-ok").attr("data-index");
    $(".myImgFirst").css("display","none");
    $(".myImgSecond").css("display","block");
    $.each($(".myImgFirst"),function(ind,val){
        if(dataIndex == ind){
            $(val).attr("src",myResult.obj+myResult.result);
            $(val).css("display","block");
            $($(".myImgSecond")[dataIndex]).css("display","none");
            $(val).attr("data-img",myResult.result);
            /*if($("#img").val()!=null&&$("#img").val()!=''){
                var curMyImg = $("#img").val().split(",");
                $("#img").val($("#img").val()+","+myResult.result);
            }*/
        }else{
            var oldSrc = $(val).attr("src");
            if(oldSrc!='../../../images/VRPicture.png'){
                $(val).css("display","block");
                $($(".myImgSecond")[ind]).css("display","none");
            }
        }
    });
}

function createImgDiv() {
    var currentImgIndex = $(".myImgFirst").length;
    $("#form-group-sdId").before("<div class='up-img-cover' style='width:100%;text-align: center;'>" +
        "<img class='am-circle myImgFirst' src='../../../images/VRPicture.png' onclick='showMyCropperImgModel("+currentImgIndex+")' style='display: none;width: 8em;height:70px;margin: 0 auto;border-radius: 0;transform: translateY(-4%);'/>" +
        "<i id='up-img-touch999' onclick='showMyCropperImgModel("+currentImgIndex+")' class='iconfont iconVRtupian myImgSecond' style='display:block;font-size: 6em;line-height: 110px;'></i></div>");
}
function myResetForm(){
    //清空权限数组
    $("#id").val("");
    $("#imgPath").val("");
    $("#sdId").val("");
    $("#sdIds").val("");

    $('#comInfoForm').resetForm();
    $('#comInfoForm').clearForm();
}