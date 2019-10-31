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
    url: "/companyInformation/getCompanyInformationList",
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
        ajaxFormValidationURL:"/companyInformation/setCompanyInformation",
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
                        var diningType = '';
                        switch (val.diningType){
                            case 1:
                                diningType = "社会餐饮";
                                break;
                            case 2:
                                diningType = "其他餐饮";
                                break;

                        }
                        var operatingState = '';
                        switch (val.operatingState){
                            case 1:
                                operatingState = "正在营业";
                                break;
                            case 2:
                                operatingState = "暂停营业";
                                break;
                        }
                        var comInfoTopStr = '';
                        var companyName = '';
                        if(val.companyName&&val.companyName!=''){
                            if(operatingState!=''){
                                comInfoTopStr = val.companyName+"-"+diningType+"("+operatingState+")";
                            }else{
                                comInfoTopStr = val.companyName+"-"+diningType;
                            }
                            companyName = val.companyName;
                        }else{
                            comInfoTopStr = "<span style='color:red;'>(未上传)</span>";
                        }
                        var businessHours = '';
                        if(val.businessHours&&val.businessHours!=''){
                            businessHours = val.businessHours + "正常营业";
                        }
                        var principal = '';
                        if(val.principal&&val.principal!=''){
                            principal = '如有问题请联系'+val.principal;
                        }
                        var contactWay = '';
                        if(val.contactWay&&val.contactWay!='') {
                            if (principal != '') {
                                contactWay = principal + "(" + val.contactWay + ")";
                            } else {
                                contactWay = '如有问题请拨打' + val.contactWay;
                            }
                        }else{
                            contactWay = principal;
                        }
                        var detailedAddress = '';
                        if(val.detailedAddress&&val.detailedAddress!=''){
                            if(contactWay!=''){
                                detailedAddress = '或直接前往,地址位于'+val.detailedAddress;
                            }else{
                                detailedAddress = '地址位于'+val.detailedAddress;
                            }
                        }else{
                            detailedAddress = contactWay;
                        }

                        var foodsafetyName = '';
                        if(val.foodsafetyName&&val.foodsafetyName!=''){
                            foodsafetyName = '如有安全问题请联系'+val.foodsafetyName;
                        }
                        var foodsafeyPhone = '';
                        if(val.foodsafeyPhone&&val.foodsafeyPhone!=''){
                            if(foodsafetyName!=''){
                                foodsafeyPhone = foodsafetyName +"("+val.foodsafeyPhone+")";
                            }else{
                                foodsafeyPhone = '如有安全问题请联系'+val.foodsafeyPhone;
                            }
                        }else{
                            foodsafeyPhone = foodsafetyName;
                        }

                        var supervisorName = '';
                        if(val.supervisorName&&val.supervisorName!=''){
                            if(foodsafeyPhone!=''){
                                supervisorName = foodsafeyPhone +","+val.supervisorName;
                            }else{
                                if(foodsafetyName!=''){
                                    supervisorName = foodsafetyName +","+val.supervisorName;
                                }else{
                                    supervisorName = '如有安全问题请联系'+val.supervisorName;
                                }
                            }
                        }else{
                            supervisorName = foodsafeyPhone;
                        }
                        var supervisorPhone = '';
                        if(val.supervisorPhone&&val.supervisorPhone!=''){
                            if(supervisorName!=''){
                                supervisorPhone = supervisorName +"("+val.supervisorPhone+")";
                            }else{
                                if(foodsafeyPhone!=''||foodsafetyName!=''){
                                    supervisorPhone = ',监管员('+val.supervisorPhone+")";
                                }else{
                                    supervisorPhone = '如有安全问题请联系'+val.supervisorPhone;
                                }
                            }
                        }else{
                            supervisorPhone = supervisorName;
                        }
                        var ibImgPath = '';
                        if(val.commitmentFoodSafetyImg	&&val.commitmentFoodSafetyImg!=''){
                            if(val.commitmentFoodSafetyImg.indexOf("http:")!=-1){
                                ibImgPath = val.commitmentFoodSafetyImg;
                            }else{
                                ibImgPath = val.picturePrefix+val.commitmentFoodSafetyImg
                            }
                        }else{
                            ibImgPath = "../../../images/VRPicture.png";
                        }
                        var busDelStr = '';

                        $("#userListUl").append("<div class='col-md-3 col-sm-3 col-xs-12'>" +
                            "<div class='tpl-table-images-content'>" +
                            "<div class='tpl-table-images-content-i-time-hhp' style='height: 3.2em;'>"+comInfoTopStr+"</div>" +
                            "<div class='tpl-i-title-hhpHeight' style='height: 6.5em;' data-toggle='tooltip' title='"+detailedAddress+"' data-original-title='"+detailedAddress+"'>" +detailedAddress+
                            /*"<div>"+registration+"</div>"+
                            "<div>"+legalRepresentative+"</div>"+
                            "<div>"+topStr+"</div>"+
                            "<div>"+address+"</div>"+
                            "<div>"+deadline+"</div>"+*/
                            "</div>" +
                            "<a href='javascript:;' class='tpl-table-images-content-i'>" +
                            "<div class='tpl-table-images-content-i-info' style='width: 40px;height:40px;'>" +
                            "<span class='ico'>" +
                            "<img src='"+val.sitePhoto+"' data-toggle='tooltip' title='"+val.siteName+"' data-original-title='"+val.siteName+"'>" +
                            "</span>" +
                            "</div>" +
                            /*"<span class='tpl-table-images-content-i-shadow'></span>" +*/
                            "<img src='"+ibImgPath+"' data-original='"+ibImgPath+"' style='height: 6em;'>" +
                            "</a>" +
                            "<div class='tpl-table-images-content-block'>" +
                            "<div class='tpl-i-font' style='height: 4.5em;' data-toggle='tooltip' title='"+supervisorPhone+"' data-original-title='"+supervisorPhone+"'>" + supervisorPhone +
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
                            "<button type='button' class='am-btn am-btn-default am-btn-danger' style='width: 50% !important;' onclick='delRole("+val.id+","+val.sdId+","+"\""+companyName+"\")'><i class='iconfont iconshanchutishi'></i> 删除</button>" +
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
    $("#supervisorImg").val("");
    $("#foodsafeyImg").val("");
    $("#commitmentFoodSafetyImg").val("");
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

    $($(".myImgFirst")[2]).css("display","none");
    $($(".myImgSecond")[2]).css("display","block");
    $($(".myImgFirst")[2]).attr("src",'');

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
            $("#foodsafeyImg").val(myResult.result);
            $($(".myImgFirst")[0]).attr("src",myResult.obj+myResult.result);
            if($("#supervisorImg").val()==null||$("#supervisorImg").val()==''){
                $($(".myImgFirst")[1]).css("display","none");
                $($(".myImgSecond")[1]).css("display","block");
                $($(".myImgFirst")[1]).attr("src",'');
            }
            if($("#commitmentFoodSafetyImg").val()==null||$("#commitmentFoodSafetyImg").val()==''){
                $($(".myImgFirst")[2]).css("display","none");
                $($(".myImgSecond")[2]).css("display","block");
                $($(".myImgFirst")[2]).attr("src",'');
            }
            break;
        case '1':
            $("#supervisorImg").val(myResult.result);
            $($(".myImgFirst")[1]).attr("src",myResult.obj+myResult.result);
            if($("#foodsafeyImg").val()==null||$("#foodsafeyImg").val()==''){
                $($(".myImgFirst")[0]).css("display","none");
                $($(".myImgSecond")[0]).css("display","block");
                $($(".myImgFirst")[0]).attr("src",'');
            }
            if($("#commitmentFoodSafetyImg").val()==null||$("#commitmentFoodSafetyImg").val()==''){
                $($(".myImgFirst")[2]).css("display","none");
                $($(".myImgSecond")[2]).css("display","block");
                $($(".myImgFirst")[2]).attr("src",'');
            }
            break;
        case '2':
            $("#commitmentFoodSafetyImg").val(myResult.result);
            $($(".myImgFirst")[2]).attr("src",myResult.obj+myResult.result);
            if($("#foodsafeyImg").val()==null||$("#foodsafeyImg").val()==''){
                $($(".myImgFirst")[0]).css("display","none");
                $($(".myImgSecond")[0]).css("display","block");
                $($(".myImgFirst")[0]).attr("src",'');
            }
            if($("#supervisorImg").val()==null||$("#supervisorImg").val()==''){
                $($(".myImgFirst")[1]).css("display","none");
                $($(".myImgSecond")[1]).css("display","block");
                $($(".myImgFirst")[1]).attr("src",'');
            }
            break;
    }
}
function updateRole(id,updSdId,conType) {
    if(id!=null && !isNaN(id)){
        var subUrl = '/companyInformation/getUpdCompanyInformation';
        //清空隐藏域id
        $("#id").val("");
        $("#supervisorImg").val("");
        $("#foodsafeyImg").val("");
        $("#commitmentFoodSafetyImg").val("");
        $("#sdId").val("");

        $("#form-group-sdId").css("display","none");

        $($(".myImgFirst")[0]).css("display","none");
        $($(".myImgSecond")[0]).css("display","block");
        $($(".myImgFirst")[0]).attr("src",'');

        $($(".myImgFirst")[1]).css("display","none");
        $($(".myImgSecond")[1]).css("display","block");
        $($(".myImgFirst")[1]).attr("src",'');

        $($(".myImgFirst")[2]).css("display","none");
        $($(".myImgSecond")[2]).css("display","block");
        $($(".myImgFirst")[2]).attr("src",'');

        //重置清空form
        myResetForm();
        $.post(subUrl,{"id":id,"sdId":updSdId},function(data){
            if(!isKickOut(data)){
                //没有被踢出
                if(data.resultCode==100){
                    $("#id").val(data.result.id);
                    $("#supervisorImg").val(data.result.supervisorImg);
                    $("#foodsafeyImg").val(data.result.foodsafeyImg);
                    $("#commitmentFoodSafetyImg").val(data.result.commitmentFoodSafetyImg);
                    $("#sdId").val(data.result.sdId);

                    if(data.result.foodsafeyImg&&data.result.foodsafeyImg!=''){
                        if(data.result.foodsafeyImg.indexOf("http:")==-1){
                            $($(".myImgFirst")[0]).attr("src",data.result.picturePrefix+data.result.foodsafeyImg);
                        }else{
                            $($(".myImgFirst")[0]).attr("src",data.result.foodsafeyImg);
                        }
                        $($(".myImgFirst")[0]).css("display","block");
                        $($(".myImgSecond")[0]).css("display","none");
                    }
                    if(data.result.supervisorImg&&data.result.supervisorImg!=''){
                        if(data.result.supervisorImg.indexOf("http:")==-1){
                            $($(".myImgFirst")[1]).attr("src",data.result.picturePrefix+data.result.supervisorImg);
                        }else{
                            $($(".myImgFirst")[1]).attr("src",data.result.supervisorImg);
                        }
                        $($(".myImgFirst")[1]).css("display","block");
                        $($(".myImgSecond")[1]).css("display","none");
                    }
                    if(data.result.commitmentFoodSafetyImg&&data.result.commitmentFoodSafetyImg!=''){
                        if(data.result.commitmentFoodSafetyImg.indexOf("http:")==-1){
                            $($(".myImgFirst")[2]).attr("src",data.result.picturePrefix+data.result.commitmentFoodSafetyImg);
                        }else{
                            $($(".myImgFirst")[2]).attr("src",data.result.commitmentFoodSafetyImg);
                        }
                        $($(".myImgFirst")[2]).css("display","block");
                        $($(".myImgSecond")[2]).css("display","none");
                    }

                    if(data.result.companyName&&data.result.companyName!=''){
                        $("#companyName").val(data.result.companyName);
                    }
                    if(data.result.detailedAddress&&data.result.detailedAddress!=''){
                        $("#detailedAddress").val(data.result.detailedAddress);
                    }
                    if(data.result.principal&&data.result.principal!=''){
                        $("#principal").val(data.result.principal);
                    }
                    if(data.result.contactWay&&data.result.contactWay!=''){
                        $("#contactWay").val(data.result.contactWay);
                    }
                    if(data.result.diningType&&data.result.diningType!=''){
                        $("#diningType").val(data.result.diningType);
                        $('#diningType').selectator('refresh');
                    }
                    if(data.result.businessHours&&data.result.businessHours!=''){
                        $("#businessHours").val(data.result.businessHours);
                    }
                    if(data.result.operatingState&&data.result.operatingState!=''){
                        $("#operatingState").val(data.result.operatingState);
                        $('#operatingState').selectator('refresh');
                    }
                    if(data.result.foodsafetyName&&data.result.foodsafetyName!=''){
                        $("#foodsafetyName").val(data.result.foodsafetyName);
                    }
                    if(data.result.foodsafeyPhone&&data.result.foodsafeyPhone!=''){
                        $("#foodsafeyPhone").val(data.result.foodsafeyPhone);
                    }

                    if(data.result.supervisorName&&data.result.supervisorName!=''){
                        $("#supervisorName").val(data.result.supervisorName);
                    }
                    if(data.result.supervisorPhone&&data.result.supervisorPhone!=''){
                        $("#supervisorPhone").val(data.result.supervisorPhone);
                    }
                    $("#chgBusTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;更新公司资讯");
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
function delRole(id,delSdId,name,conType) {
    if(null!=id&&id!=""){
        var subUrl = "/companyInformation/delCompanyInformation";
        $.confirm({
            icon: '#icon-tishi1',
            theme: 'modern',
            title: '提示',
            content: '您确定要删除'+name+'资讯吗？',
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
                                    showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",'删除'+name+'资讯成功！');
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
    $("#supervisorImg").val("");
    $("#foodsafeyImg").val("");
    $("#commitmentFoodSafetyImg").val("");
    $("#sdId").val("");

    $($(".myImgFirst")[0]).css("display","none");
    $($(".myImgSecond")[0]).css("display","block");
    $($(".myImgFirst")[0]).attr("src",'');

    $($(".myImgFirst")[1]).css("display","none");
    $($(".myImgSecond")[1]).css("display","block");
    $($(".myImgFirst")[1]).attr("src",'');

    $($(".myImgFirst")[2]).css("display","none");
    $($(".myImgSecond")[2]).css("display","block");
    $($(".myImgFirst")[2]).attr("src",'');

    $("#selectSites").val("");
    $('#selectSites').selectator('refresh');
    $("#diningType").val("");
    $('#diningType').selectator('refresh');
    $("#operatingState").val("");
    $('#operatingState').selectator('refresh');
    //清空权限数组
    $('#comInfoForm').resetForm();
    $('#comInfoForm').clearForm();
}