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
var mainCategoryIdArray = [];
var whouseIdArray;
var suppIdArray;

var isManySite = false;
var ajaxSCJCXXSearchFormOption = {
    beforeSubmit: showSCJCXXSearchRequest,
    success: showSCJCXXSearchResponse,
    url: "/companyHonor/getCompanyHonorList",
    type: "post",
    data:{},
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
        validateNonVisibleFields:false,
        focusFirstField:true,
        ajaxFormValidation: true,
        ajaxFormValidationURL:"/companyHonor/setCompanyHonor",
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

    $("#diningType").selectator({
        labels: {
            search: '选择餐饮类型'
        },
        showAllOptionsOnFocus: true
    });
    $("#diningType").next().css("width","100%");
    $("#diningType").next().find(".selectator_input").css("background","#1b1b1b");

    $("#operatingState").selectator({
        labels: {
            search: '选择营业状态'
        },
        showAllOptionsOnFocus: true
    });
    $("#operatingState").next().css("width","100%");
    $("#operatingState").next().find(".selectator_input").css("background","#1b1b1b");
});
function showSCJCXXSearchRequest(formData, jqForm, options){
    return true;
}
function showSCJCXXSearchResponse(data, status){
    initDateTableData(data);
}

function loadUserList(){
    if($("#userSites").val()){
        $("#alertSdId").val($("#userSites").val().toString());
    }
    $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
    return false;
}
function reloadMyDateTable() {
    if($("#userSites").val()){
        $("#alertSdId").val($("#userSites").val().toString());
    }else{
        $("#alertSdId").val("");
    }
    $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
    return false;
}
function setErrorStyle() {
    setTimeout(function () {
        $("#selectSites").prev().css("top","0px");
        $("#selectSites").prev().css("left","15.9em");
    },1000);
}
function showRequest(formData, jqForm, options){
    if($("#selectSites").val()!=undefined&&$("#selectSites").val()!=null&&$("#selectSites").val()!=''
    &&($("#id").val()==null||$("#id").val()=='')){
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
                        var comInfoTopStr = '公司荣誉';
                        //zhengPaiImg
                        var ibImgPath = '';
                        if(val.certificateImg	&&val.certificateImg!=''){
                            if(val.certificateImg.indexOf("http:")!=-1){
                                ibImgPath = val.certificateImg;
                            }else{
                                ibImgPath = val.picturePrefix+val.certificateImg
                            }
                        }else{
                            ibImgPath = "../../../images/VRPicture.png";
                        }

                        var zhengPaiImg = '';
                        if(val.zhengPaiImg	&&val.zhengPaiImg!=''){
                            if(val.zhengPaiImg.indexOf("http:")!=-1){
                                zhengPaiImg = val.zhengPaiImg;
                            }else{
                                zhengPaiImg = val.picturePrefix+val.zhengPaiImg
                            }
                        }else{
                            zhengPaiImg = "../../../images/VRPicture.png";
                        }

                        $("#userListUl").append("<div class='col-md-3 col-sm-3 col-xs-12'>" +
                            "<div class='tpl-table-images-content'>" +
                            "<div class='tpl-table-images-content-i-time-hhp'>"+comInfoTopStr+"</div>" +
                            /*"<div class='tpl-i-title-hhpHeight' style='height: 6.5em;' data-toggle='tooltip' title='' data-original-title=''>" +
                            /!*"<div>"+registration+"</div>"+
                            "<div>"+legalRepresentative+"</div>"+
                            "<div>"+topStr+"</div>"+
                            "<div>"+address+"</div>"+
                            "<div>"+deadline+"</div>"+*!/
                            "</div>" +*/
                            "<a href='javascript:;' class='tpl-table-images-content-i'>" +
                            "<div class='tpl-table-images-content-i-info' style='width: 40px;height:40px;'>" +
                            "<span class='ico'>" +
                            "<img src='"+val.sitePhoto+"' data-toggle='tooltip' title='"+val.siteName+"' data-original-title='"+val.siteName+"'>" +
                            "</span>" +
                            "</div>" +
                            /*"<span class='tpl-table-images-content-i-shadow'></span>" +*/
                            "<img src='"+ibImgPath+"' data-original='"+ibImgPath+"' style='height: 6em;'>" +
                            "</a>" +
                            "<a href='javascript:;' class='tpl-table-images-content-i'>" +
                            "<div class='tpl-table-images-content-i-info' style='width: 40px;height:40px;'>" +
                            "<span class='ico'>" +
                            "<img src='"+val.sitePhoto+"' data-toggle='tooltip' title='"+val.siteName+"' data-original-title='"+val.siteName+"'>" +
                            "</span>" +
                            "</div>" +
                            /*"<span class='tpl-table-images-content-i-shadow'></span>" +*/
                            "<img src='"+zhengPaiImg+"' data-original='"+zhengPaiImg+"' style='height: 6em;'>" +
                            "</a>" +
                            "<div class='tpl-table-images-content-block'>" +
                            /*"<div class='tpl-i-font' style='height: 3em;' data-toggle='tooltip' title='"+supervisorPhone+"' data-original-title='"+supervisorPhone+"'>" + supervisorPhone +
                            "</div>" +*/
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
                            "<button type='button' class='am-btn am-btn-default am-btn-danger' style='width: 50% !important;' onclick='delRole("+val.id+","+val.sdId+")'><i class='iconfont iconshanchutishi'></i> 删除</button>" +
                            /*"<button type='button' class='am-btn am-btn-default am-btn-warning' style='width: 33% !important;' onclick='exportSingle("+val.id+")'><i class='iconfont iconexport1'></i> 导出</button>" +*/
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            "</div>");
                    }
                });

                $("#userListUl").viewer('update');
                $('#userListUl').viewer({
                    url: 'data-original',
                });
                $("[data-toggle='tooltip']").tooltip();
            }else{
                showJqueryConfirmWindow("#icon-tishi1","未获取公司证照相关数据");
            }
            isShow = true;
        }else if(data.resultCode==101){
            showJqueryConfirmWindow("#icon-shangxin1","获取公司资讯异常！深感抱歉，请联系管理人员为您加急处理");
        }else{
            showJqueryConfirmWindow("#icon-tishi1",data.msg);
        }
    }
}
function befShowAddMod(){
    //清空隐藏域id
    $("#id").val("");
    $("#certificateImg").val("");
    $("#zhengPaiImg").val("");
    $("#sdId").val("");

    if(isManySite){
        $("#form-group-sdId").css("display","block");
    }else{
        $("#form-group-sdId").css("display","none");
    }


    $($(".myImgFirst")[0]).css("display","none");
    $($(".myImgSecond")[0]).css("display","block");
    $($(".myImgFirst")[0]).attr("src",'');

    $($(".myImgFirst")[1]).css("display","none");
    $($(".myImgSecond")[1]).css("display","block");
    $($(".myImgFirst")[1]).attr("src",'');

    //重置清空form
    myResetForm();

    $("#chgBusTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;提交公司资讯");
    $("#comInfo-modal").modal("show");
}

function doMyLogic(myResult) {
    var dataIndex = $(".up-modal-frame .up-btn-ok").attr("data-index");
    $(".myImgFirst").css("display","block");
    $(".myImgSecond").css("display","none");

    switch (dataIndex){
        case '0':
            $("#certificateImg").val(myResult.result);
            $($(".myImgFirst")[0]).attr("src",myResult.obj+myResult.result);
            if($("#zhengPaiImg").val()==null||$("#zhengPaiImg").val()==''){
                $($(".myImgFirst")[1]).css("display","none");
                $($(".myImgSecond")[1]).css("display","block");
                $($(".myImgFirst")[1]).attr("src",'');
            }
            break;
        case '1':
            $("#zhengPaiImg").val(myResult.result);
            $($(".myImgFirst")[1]).attr("src",myResult.obj+myResult.result);
            if($("#certificateImg").val()==null||$("#certificateImg").val()==''){
                $($(".myImgFirst")[0]).css("display","none");
                $($(".myImgSecond")[0]).css("display","block");
                $($(".myImgFirst")[0]).attr("src",'');
            }
            break;
    }
}
function updateRole(id,updSdId,conType) {
    if(id!=null && !isNaN(id)){
        var subUrl = '/companyHonor/getUpdCompanyHonor';
        //清空隐藏域id
        $("#id").val("");
        $("#supervisorImg").val("");
        $("#foodsafeyImg").val("");
        $("#sdId").val("");

        $("#form-group-sdId").css("display","none");

        $($(".myImgFirst")[0]).css("display","none");
        $($(".myImgSecond")[0]).css("display","block");
        $($(".myImgFirst")[0]).attr("src",'');

        $($(".myImgFirst")[1]).css("display","none");
        $($(".myImgSecond")[1]).css("display","block");
        $($(".myImgFirst")[1]).attr("src",'');

        //重置清空form
        myResetForm();
        $.post(subUrl,{"id":id,"sdId":updSdId},function(data){
            if(!isKickOut(data)){
                //没有被踢出
                if(data.resultCode==100){
                    $("#id").val(data.result.id);
                    $("#certificateImg").val(data.result.certificateImg);
                    $("#zhengPaiImg").val(data.result.zhengPaiImg);
                    $("#sdId").val(data.result.sdId);

                    if(data.result.certificateImg&&data.result.certificateImg!=''){
                        if(data.result.certificateImg.indexOf("http:")==-1){
                            $($(".myImgFirst")[0]).attr("src",data.result.picturePrefix+data.result.certificateImg);
                        }else{
                            $($(".myImgFirst")[0]).attr("src",data.result.certificateImg);
                        }
                        $($(".myImgFirst")[0]).css("display","block");
                        $($(".myImgSecond")[0]).css("display","none");
                    }
                    if(data.result.zhengPaiImg&&data.result.zhengPaiImg!=''){
                        if(data.result.zhengPaiImg.indexOf("http:")==-1){
                            $($(".myImgFirst")[1]).attr("src",data.result.picturePrefix+data.result.zhengPaiImg);
                        }else{
                            $($(".myImgFirst")[1]).attr("src",data.result.zhengPaiImg);
                        }
                        $($(".myImgFirst")[1]).css("display","block");
                        $($(".myImgSecond")[1]).css("display","none");
                    }
                    $("#chgBusTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;更新公司荣誉");
                    $("#comInfo-modal").modal("show");
                }else if(data.resultCode==101){
                    showJqueryConfirmWindow("#icon-shangxin1","获取信息异常！深感抱歉，请联系管理人员为您加急处理");
                }else{
                    showJqueryConfirmWindow("#icon-tishi1",data.msg);
                }
            }
        });
    }else{
        showJqueryConfirmWindow("#icon-tishi1","主键获取异常！深感抱歉，请联系管理人员为您加急处理");
    }
}
function delRole(id,delSdId) {
    if(null!=id&&id!=""){
        var subUrl = "/companyHonor/delCompanyHonor";
        $.confirm({
            icon: '#icon-tishi1',
            theme: 'modern',
            title: '提示',
            content: '您确定要删除公司荣誉信息吗？',
            animation: 'news',//动画
            closeAnimation: 'news',//关闭动画
            /*autoClose: 'showMyMsg2|3000',*/
            buttons: {
                showMyMsg2: {
                    text: '确定',
                    action: function () {
                        $.post(subUrl,{"id":id,"sdId":delSdId},function(data){
                            if(!isKickOut(data)){
                                //没有被踢出
                                if(data.resultCode==100){
                                    //刷新数据
                                    loadUserList();
                                    showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",'删除公司荣誉信息成功！');
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
function showOpenColor() {
    $(".search").css("background","rgba(0, 0, 0, 0.68)");
    $(".search").css("overflow-y","scroll");
}
function setOpenColor() {
    $(".search").css("background","none");
    $(".search").css("overflow-y","hidden");
}
function myResetForm() {
    //清空隐藏域id
    $("#id").val("");
    $("#certificateImg").val("");
    $("#zhengPaiImg").val("");
    $("#sdId").val("");

    $($(".myImgFirst")[0]).css("display","none");
    $($(".myImgSecond")[0]).css("display","block");
    $($(".myImgFirst")[0]).attr("src",'');

    $($(".myImgFirst")[1]).css("display","none");
    $($(".myImgSecond")[1]).css("display","block");
    $($(".myImgFirst")[1]).attr("src",'');

    $("#selectSites").val("");
    $('#selectSites').selectator('refresh');

    //清空权限数组
    $('#comInfoForm').resetForm();
    $('#comInfoForm').clearForm();
}