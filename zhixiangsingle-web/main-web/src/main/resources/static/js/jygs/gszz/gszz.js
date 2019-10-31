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
    url: "/companyLicenses/getCompanyLicensesList",
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
        $("#form-group-sdId2").css("display","block");
        $("#form-group-sdId3").css("display","block");

        //$("#userSites").css("display","block");
        $.each(allUserSites.result,function (ind,val) {
            var curSId = val.sdId;
            var sPho = allUserSites.obj[curSId].photo;
            $("#userSites").append("<option value='"+val.sdId+"' data-subtitle='"+val.address+"' data-left='<img src="+sPho+">'>"+val.name+"</option>");

            $("#selectSites").append("<option value='"+val.sdId+"' data-subtitle='"+val.address+"' data-left='<img src="+sPho+">'>"+val.name+"</option>");
            $("#selectSites2").append("<option value='"+val.sdId+"' data-subtitle='"+val.address+"' data-left='<img src="+sPho+">'>"+val.name+"</option>");
            $("#selectSites3").append("<option value='"+val.sdId+"' data-subtitle='"+val.address+"' data-left='<img src="+sPho+">'>"+val.name+"</option>");
        });
        isManySite = true;
    }else{
        $("#sdId").val(sdId);
        //$("#userSites").css("display","none");
        $("#form-group-sdId").css("display","none");
        $("#form-group-sdId2").css("display","none");
        $("#form-group-sdId3").css("display","none");
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

    $("#selectSites2").selectator({
        labels: {
            search: '选择站点'
        },
        showAllOptionsOnFocus: true
    });
    $("#selectSites2").next().css("width","100%");
    $("#selectSites2").next().find(".selectator_input").css("background","#1b1b1b");

    $("#selectSites3").selectator({
        labels: {
            search: '选择站点'
        },
        showAllOptionsOnFocus: true
    });
    $("#selectSites3").next().css("width","100%");
    $("#selectSites3").next().find(".selectator_input").css("background","#1b1b1b");

    //为用户加站点角色的时候需要判断角色为长度为1时，需要判断设置的角色站点id是否和
    $("#busForm").validationEngine({
        binded:false,
        maxErrorsPerField:1,
        promptPosition: "topLeft:100",
        scroll:true,
        showOneMessage:true,
        validateNonVisibleFields:false,
        focusFirstField:true,
        ajaxFormValidation: true,
        ajaxFormValidationURL:"/business/setBusiness",
        ajaxFormValidationMethod:"post",
        onBeforeAjaxFormValidation: showRequest,
        onAjaxFormComplete: showResponse,
        onFieldFailure:setErrorStyle
    });

    $("#licForm").validationEngine({
        binded:false,
        maxErrorsPerField:1,
        promptPosition: "topLeft:100",
        scroll:true,
        showOneMessage:true,
        validateNonVisibleFields:false,
        focusFirstField:true,
        ajaxFormValidation: true,
        ajaxFormValidationURL:"/license/setLicense",
        ajaxFormValidationMethod:"post",
        onBeforeAjaxFormValidation: showRequest,
        onAjaxFormComplete: showResponse,
        onFieldFailure:setErrorStyle
    });

    $("#cirForm").validationEngine({
        binded:false,
        maxErrorsPerField:1,
        promptPosition: "topLeft:100",
        scroll:true,
        showOneMessage:true,
        validateNonVisibleFields:false,
        focusFirstField:true,
        ajaxFormValidation: true,
        ajaxFormValidationURL:"/circulationCard/setCirculationCard",
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

        $("#selectSites2").prev().css("top","0px");
        $("#selectSites2").prev().css("left","15.9em");

        $("#selectSites3").prev().css("top","0px");
        $("#selectSites3").prev().css("left","15.9em");
    },1000);
}
function showRequest(formData, jqForm, options){
    if($("#selectSites").val()!=undefined&&$("#selectSites").val()!=null&&$("#selectSites").val()!=''
    &&($("#busId").val()==null||$("#busId").val()=='')){
        $("#busSdIds").val($("#selectSites").val().join(","));
    }else{
        $("#busSdIds").val("");
    }

    if($("#selectSites2").val()!=undefined&&$("#selectSites2").val()!=null&&$("#selectSites2").val()!=''
        &&($("#licId").val()==null||$("#licId").val()=='')){
        $("#licSdIds").val($("#selectSites2").val().join(","));
    }else{
        $("#licSdIds").val("");
    }
    if($("#selectSites3").val()!=undefined&&$("#selectSites3").val()!=null&&$("#selectSites3").val()!=''
        &&($("#cirId").val()==null||$("#cirId").val()=='')){
        $("#cirSdIds").val($("#selectSites3").val().join(","));
    }else{
        $("#cirSdIds").val("");
    }
    return true;
};
function showResponse(status, form,data, options){
    if(!isKickOut(data)){
        if(data.resultCode==100){
            loadUserList();
            myBusResetForm();
            myLicResetForm();
            myCirResetForm();
            $("#lic-modal").modal("hide");
            $("#cir-modal").modal("hide");
            $("#bus-modal").modal("hide");
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

                    var businessTopStr = '';
                    if(val.businessDTO.companyName&&val.businessDTO.companyName!=''){
                        businessTopStr = val.businessDTO.companyName+"-"+val.businessDTO.licenseType;
                    }else{
                        businessTopStr = val.businessDTO.licenseType+"<span style='color:red;'>(未上传)</span>";
                    }

                    var registration = '';
                    if(val.businessDTO.registration&&val.businessDTO.registration!=''){
                        registration = "注册号是:"+val.businessDTO.registration;
                    }
                    var legalRepresentative = '';
                    if(val.businessDTO.legalRepresentative&&val.businessDTO.legalRepresentative!=''){
                        if(registration!=''){
                            legalRepresentative = "-法定代表人为:"+val.businessDTO.legalRepresentative;
                        }else{
                            legalRepresentative = "法定代表人为:"+val.businessDTO.legalRepresentative;
                        }
                    }
                    var topStr = '';
                    if(val.businessDTO.mainTypes&&val.businessDTO.mainTypes!=''){
                        topStr = val.businessDTO.mainTypes+"-";
                    }
                    if(val.businessDTO.registeredCapital&&val.businessDTO.registeredCapital!=''){
                        if(topStr!=""){
                            if(legalRepresentative!=''||registration!=''){
                                topStr = '-'+topStr +"-注册资金为:"+val.businessDTO.registeredCapital;
                            }else{
                                topStr = topStr +"注册资金为:"+val.businessDTO.registeredCapital;
                            }
                        }else{
                            if(legalRepresentative!=''||registration!=''){
                                topStr = "-注册资金为:"+val.businessDTO.registeredCapital;
                            }else{
                                topStr = "注册资金为:"+val.businessDTO.registeredCapital;
                            }
                        }
                    }
                    var address = '';
                    if(val.businessDTO.address&&val.businessDTO.address!=''){
                        if(topStr!=''||legalRepresentative!=''||registration!=''){
                            address = "-位于:" + val.businessDTO.address;
                        }else{
                            address = "位于:" + val.businessDTO.address;
                        }
                    }

                    var deadline = '';
                    if(val.businessDTO.deadline&&val.businessDTO.deadline!=''){
                        if(address!=''||topStr!=''||legalRepresentative!=''||registration!=''){
                            deadline = "-营业期限为:"+val.businessDTO.deadline;
                        }else{
                            deadline = "营业期限为:"+val.businessDTO.deadline;
                        }
                    }
                    var businessscope = '';
                    if(val.businessDTO.businessScope&&val.businessDTO.businessScope!=''){
                        if(deadline!=''||address!=''||topStr!=''||legalRepresentative!=''||registration!=''){
                            businessscope = "-经营范围为:"+val.businessDTO.businessScope;
                        }else{
                            businessscope = "经营范围为:"+val.businessDTO.businessScope;
                        }
                    }
                    var ibImgPath = '';
                    if(val.businessDTO.path	&&val.businessDTO.path!=''){
                        if(val.businessDTO.path.indexOf("http:")!=-1){
                            ibImgPath = val.businessDTO.path;
                        }else{
                            ibImgPath = val.businessDTO.picturePrefix+val.businessDTO.path;
                        }
                    }else{
                        ibImgPath = "../../../images/VRPicture.png";
                    }
                    var earlyWarningTime = '';
                    if(val.businessDTO.earlyWarningTime&&val.businessDTO.earlyWarningTime!=''){
                        earlyWarningTime = "系统将为您在"+val.businessDTO.earlyWarningTime+"提醒预警";
                    }
                    var busId = null;
                    var busShowOrHidden = '';
                    if(val.businessDTO.id){
                        busId = val.businessDTO.id;
                    }else{
                        busShowOrHidden = 'visibility: hidden;';
                    }
                    var busDelStr = val.businessDTO.licenseType+"-"+val.businessDTO.siteName;

                    var s = registration + legalRepresentative + topStr + address + deadline + businessscope;
                    var busDelOrUpd = "business";
                    $("#userListUl").append("<div class='col-md-4 col-sm-4 col-xs-12'>" +
                        "<div class='tpl-table-images-content'>" +
                        "<div class='tpl-table-images-content-i-time'>"+businessTopStr+"</div>" +
                        "<div class='tpl-i-title-hhpHeight' style='height: 6.5em;' data-toggle='tooltip' title='"+s+"' data-original-title='"+s+"'>" +s+
                        /*"<div>"+registration+"</div>"+
                        "<div>"+legalRepresentative+"</div>"+
                        "<div>"+topStr+"</div>"+
                        "<div>"+address+"</div>"+
                        "<div>"+deadline+"</div>"+*/
                        "</div>" +
                        "<a href='javascript:;' class='tpl-table-images-content-i'>" +
                        "<div class='tpl-table-images-content-i-info' style='width: 40px;height:40px;'>" +
                        "<span class='ico'>" +
                        "<img src='"+val.businessDTO.sitePhoto+"' data-toggle='tooltip' title='"+val.businessDTO.siteName+"' data-original-title='"+val.businessDTO.siteName+"'>" +
                        "</span>" +
                        "</div>" +
                        /*"<span class='tpl-table-images-content-i-shadow'></span>" +*/
                        "<img src='"+ibImgPath+"' data-original='"+ibImgPath+"' style='height: 6em;'>" +
                        "</a>" +
                        "<div class='tpl-table-images-content-block'>" +
                        "<div class='tpl-i-font' style='height: 1.8em;' data-toggle='tooltip' title='"+earlyWarningTime+"' data-original-title='"+earlyWarningTime+"'>" + earlyWarningTime +
                        "</div>" +
                        "<div class='tpl-i-more'>" +
                        /*"<ul>" +
                        "<li><span class='am-icon-qq am-text-warning'> 100+</span></li>" +
                        "<li><span class='am-icon-weixin am-text-success'> 235+</span></li>" +
                        "<li><span class='am-icon-github font-green'> 600+</span></li>" +
                        "</ul>" +*/
                        "</div>" +
                        "<div class='am-btn-toolbar' style='"+busShowOrHidden+"'>" +
                        "<div class='am-btn-group am-btn-group-xs tpl-edit-content-btn'>" +
                        "<button type='button' class='am-btn am-btn-default am-btn-secondary' style='width: 50% !important;' onclick='updateRole("+busId+","+val.businessDTO.sdId+","+"\""+busDelOrUpd+"\")'><i class='iconfont iconbianji3'></i> 编辑</button>" +
                        "<button type='button' class='am-btn am-btn-default am-btn-danger' style='width: 50% !important;' onclick='delRole("+busId+","+val.businessDTO.sdId+","+"\""+busDelStr+"\""+","+"\""+busDelOrUpd+"\")'><i class='iconfont iconshanchutishi'></i> 删除</button>" +
                        /*"<button type='button' class='am-btn am-btn-default am-btn-warning' style='width: 33% !important;' onclick='exportSingle("+val.id+")'><i class='iconfont iconexport1'></i> 导出</button>" +*/
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>");
                    var licenseTopStr = '';
                    if(val.licenseDTO.name&&val.licenseDTO.name!=''){
                        licenseTopStr = val.licenseDTO.name+"-"+val.licenseDTO.licenseType;
                    }else{
                        licenseTopStr = val.licenseDTO.licenseType+"<span style='color:red;'>(未上传)</span>";
                    }
                    var serialnumber = '';
                    if(val.licenseDTO.serialNumber&&val.licenseDTO.serialNumber!=''){
                        serialnumber = "编号是:"+val.licenseDTO.serialNumber;
                    }
                    var representative = '';
                    if(val.licenseDTO.representative&&val.licenseDTO.representative!=''){
                        if(serialnumber!=''){
                            representative = "-法定代表人为:"+val.licenseDTO.representative;
                        }else{
                            representative = "法定代表人为:"+val.licenseDTO.representative;
                        }

                    }
                    var validtime = '';
                    if(val.licenseDTO.validTime&&val.licenseDTO.validTime!=''){
                        if(representative!=''||serialnumber!=''){
                            validtime = "-营业期限为:"+val.licenseDTO.validTime;
                        }else{
                            validtime = "营业期限为:"+val.licenseDTO.validTime;
                        }

                    }
                    var licenseImgPath = '';
                    if(val.licenseDTO.licenceImg	&&val.licenseDTO.licenceImg!=''){
                        if(val.licenseDTO.licenceImg.indexOf("http:")!=-1){
                            licenseImgPath = val.licenseDTO.licenceImg;
                        }else{
                            licenseImgPath = val.licenseDTO.picturePrefix+val.licenseDTO.licenceImg;
                        }
                    }else{
                        licenseImgPath = "../../../images/VRPicture.png";
                    }
                    var warningdate = '';
                    if(val.licenseDTO.warningDate&&val.licenseDTO.warningDate!=''){
                        warningdate = "系统将为您在"+val.licenseDTO.warningDate+"提醒预警";
                    }
                    var licId = null;
                    var licShowOrHidden = '';
                    if(val.licenseDTO.id){
                        licId = val.licenseDTO.id;
                    }else{
                        licShowOrHidden = 'visibility: hidden;';
                    }
                    var licenStr = serialnumber+representative+validtime;
                    var licenseDelStr = val.licenseDTO.licenseType+"-"+val.licenseDTO.siteName;
                    var licDelOrUpd = "license";
                    $("#userListUl").append("<div class='col-md-4 col-sm-4 col-xs-12'>" +
                        "<div class='tpl-table-images-content'>" +
                        "<div class='tpl-table-images-content-i-time'>"+licenseTopStr+"</div>" +
                        "<div class='tpl-i-title-hhpHeight' style='height: 6.5em;' data-toggle='tooltip' title='"+licenStr+"' data-original-title='"+licenStr+"'>" +licenStr+
                        "</div>" +
                        "<a href='javascript:;' class='tpl-table-images-content-i'>" +
                        "<div class='tpl-table-images-content-i-info' style='width: 40px;height:40px;'>" +
                        "<span class='ico'>" +
                        "<img src='"+val.licenseDTO.sitePhoto+"' data-toggle='tooltip' title='"+val.licenseDTO.siteName+"' data-original-title='"+val.licenseDTO.siteName+"'>" +
                        "</span>" +
                        "</div>" +
                        /*"<span class='tpl-table-images-content-i-shadow'></span>" +*/
                        "<img src='"+licenseImgPath+"' data-original='"+licenseImgPath+"' style='height: 6em;'>" +
                        "</a>" +
                        "<div class='tpl-table-images-content-block'>" +
                        "<div class='tpl-i-font' style='height: 1.8em;' data-toggle='tooltip' title='"+warningdate+"' data-original-title='"+warningdate+"'>" + warningdate +
                        "</div>" +
                        "<div class='tpl-i-more'>" +
                        /*"<ul>" +
                        "<li><span class='am-icon-qq am-text-warning'> 100+</span></li>" +
                        "<li><span class='am-icon-weixin am-text-success'> 235+</span></li>" +
                        "<li><span class='am-icon-github font-green'> 600+</span></li>" +
                        "</ul>" +*/
                        "</div>" +
                        "<div class='am-btn-toolbar' style='"+licShowOrHidden+"'>" +
                        "<div class='am-btn-group am-btn-group-xs tpl-edit-content-btn'>" +
                        "<button type='button' class='am-btn am-btn-default am-btn-secondary' style='width: 50% !important;' onclick='updateRole("+licId+","+val.licenseDTO.sdId+","+"\""+licDelOrUpd+"\")'><i class='iconfont iconbianji3'></i> 编辑</button>" +
                        "<button type='button' class='am-btn am-btn-default am-btn-danger' style='width: 50% !important;' onclick='delRole("+licId+","+val.licenseDTO.sdId+","+"\""+licenseDelStr+"\""+","+"\""+licDelOrUpd+"\")'><i class='iconfont iconshanchutishi'></i> 删除</button>" +
                        /*"<button type='button' class='am-btn am-btn-default am-btn-warning' style='width: 33% !important;' onclick='exportSingle("+val.id+")'><i class='iconfont iconexport1'></i> 导出</button>" +*/
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>");
                    var cirCardTopStr = '';
                    if(val.circulationCardDTO.name&&val.circulationCardDTO.name!=''){
                        cirCardTopStr = val.circulationCardDTO.name+"-"+val.circulationCardDTO.licenseType;
                    }else{
                        cirCardTopStr = val.circulationCardDTO.licenseType+"<span style='color:red;'>(未上传)</span>";
                    }
                    var cirSerialnumber = '';
                    if(val.circulationCardDTO.serialNumber&&val.circulationCardDTO.serialNumber!=''){
                        cirSerialnumber = "编号是:"+val.circulationCardDTO.serialNumber;
                    }
                    var cirRepresentative = '';
                    if(val.circulationCardDTO.representative&&val.circulationCardDTO.representative!=''){
                        if(cirSerialnumber!=''){
                            cirRepresentative = "-法定代表人为:"+val.circulationCardDTO.representative;
                        }else{
                            cirRepresentative = "法定代表人为:"+val.circulationCardDTO.representative;
                        }

                    }

                    var cirValidtime = '';
                    if(val.circulationCardDTO.validTime&&val.circulationCardDTO.validTime!=''){
                        if(cirRepresentative!=''||cirSerialnumber!=''){
                            cirValidtime = "-营业期限为:"+val.circulationCardDTO.validTime;
                        }else{
                            cirValidtime = "营业期限为:"+val.circulationCardDTO.validTime;
                        }

                    }

                    var cirImgPath = '';
                    if(val.circulationCardDTO.circulationCardImg	&&val.licenseDTO.circulationCardImg!=''){
                        if(val.circulationCardDTO.circulationCardImg.indexOf("http:")!=-1){
                            cirImgPath = val.circulationCardDTO.circulationCardImg;
                        }else{
                            cirImgPath = val.circulationCardDTO.picturePrefix+val.circulationCardDTO.circulationCardImg;
                        }
                    }else{
                        cirImgPath = "../../../images/VRPicture.png";
                    }
                    var cirWarningdate = '';
                    if(val.circulationCardDTO.warningDate&&val.circulationCardDTO.warningDate!=''){
                        cirWarningdate = "系统将为您在"+val.circulationCardDTO.warningDate+"提醒预警";
                    }
                    var cirId = null;
                    var cirShowOrHidden = '';
                    if(val.circulationCardDTO.id){
                        cirId = val.circulationCardDTO.id;
                    }else{
                        cirShowOrHidden = 'visibility: hidden;';
                    }
                    var cirStr = cirSerialnumber+cirRepresentative+cirValidtime;
                    var delStr = val.circulationCardDTO.licenseType+"-"+val.circulationCardDTO.siteName;
                    var cirDelOrUpd = "circulationCard";
                    $("#userListUl").append("<div class='col-md-4 col-sm-4 col-xs-12'>" +
                        "<div class='tpl-table-images-content'>" +
                        "<div class='tpl-table-images-content-i-time'>"+cirCardTopStr+"</div>" +
                        "<div class='tpl-i-title-hhpHeight' style='height: 6.5em;' data-toggle='tooltip' title='"+cirStr+"' data-original-title='"+cirStr+"'>" +cirStr+
                        "</div>" +
                        "<a href='javascript:;' class='tpl-table-images-content-i'>" +
                        "<div class='tpl-table-images-content-i-info' style='width: 40px;height:40px;'>" +
                        "<span class='ico'>" +
                        "<img src='"+val.circulationCardDTO.sitePhoto+"' data-toggle='tooltip' title='"+val.circulationCardDTO.siteName+"' data-original-title='"+val.circulationCardDTO.siteName+"'>" +
                        "</span>" +
                        "</div>" +
                        /*"<span class='tpl-table-images-content-i-shadow'></span>" +*/
                        "<img src='"+cirImgPath+"' data-original='"+cirImgPath+"' style='height: 6em;'>" +
                        "</a>" +
                        "<div class='tpl-table-images-content-block'>" +
                        "<div class='tpl-i-font' style='height: 1.8em;' data-toggle='tooltip' title='"+cirWarningdate+"' data-original-title='"+cirWarningdate+"'>" + cirWarningdate +
                        "</div>" +
                        "<div class='tpl-i-more'>" +
                        /*"<ul>" +
                        "<li><span class='am-icon-qq am-text-warning'> 100+</span></li>" +
                        "<li><span class='am-icon-weixin am-text-success'> 235+</span></li>" +
                        "<li><span class='am-icon-github font-green'> 600+</span></li>" +
                        "</ul>" +*/
                        "</div>" +
                        "<div class='am-btn-toolbar' style='"+cirShowOrHidden+"'>" +
                        "<div class='am-btn-group am-btn-group-xs tpl-edit-content-btn'>" +
                        "<button type='button' class='am-btn am-btn-default am-btn-secondary' style='width: 50% !important;' onclick='updateRole("+cirId+","+val.circulationCardDTO.sdId+","+"\""+cirDelOrUpd+"\")'><i class='iconfont iconbianji3'></i> 编辑</button>" +
                        "<button type='button' class='am-btn am-btn-default am-btn-danger' style='width: 50% !important;' onclick='delRole("+cirId+","+val.circulationCardDTO.sdId+","+"\""+delStr+"\""+","+"\""+cirDelOrUpd+"\")'><i class='iconfont iconshanchutishi'></i> 删除</button>" +
                        /*"<button type='button' class='am-btn am-btn-default am-btn-warning' style='width: 33% !important;' onclick='exportSingle("+val.id+")'><i class='iconfont iconexport1'></i> 导出</button>" +*/
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>");
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
            showJqueryConfirmWindow("#icon-shangxin1","获取公司证照信息异常！深感抱歉，请联系管理人员为您加急处理");
        }else{
            showJqueryConfirmWindow("#icon-tishi1",data.msg);
        }
    }
}
function befShowBusAddMod(){
    //清空隐藏域id
    $("#busId").val("");
    $("#busIconImg").val("");
    $("#busSdId").val("");

    if(isManySite){
        $("#form-group-sdId").css("display","block");
    }else{
        $("#form-group-sdId").css("display","none");
    }

    $($(".myImgFirst")[0]).css("display","none");
    $($(".myImgSecond")[0]).css("display","block");

    $($(".myImgFirst")[0]).attr("src",'');

    //重置清空form
    myBusResetForm();
    $(".up-modal-frame .up-btn-ok").attr("url","/uploadOCRImg");
    $(".up-modal-frame .up-btn-ok").attr("data-type","bus");
    $("#chgBusTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;提交营业执照");
    $("#bus-modal").modal("show");
}
function befShowLicAddMod(){
    //清空隐藏域id
    $("#licId").val("");
    $("#licIconImg").val("");
    $("#licSdId").val("");

    if(isManySite){
        $("#form-group-sdId2").css("display","block");
    }else{
        $("#form-group-sdId2").css("display","none");
    }

    $($(".myImgFirst")[1]).css("display","none");
    $($(".myImgSecond")[1]).css("display","block");
    $($(".myImgFirst")[1]).attr("src",'');
    //重置清空form
    myLicResetForm();
    $(".up-modal-frame .up-btn-ok").attr("url","/uploadFileImg");
    $(".up-modal-frame .up-btn-ok").attr("data-type","lic");
    $("#chgLicTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;提交餐饮许可证");
    $("#lic-modal").modal("show");
}
function befShowCirAddMod(){
    //清空隐藏域id
    $("#cirId").val("");
    $("#cirIconImg").val("");
    $("#cirSdId").val("");
    if(isManySite){
        $("#form-group-sdId3").css("display","block");
    }else{
        $("#form-group-sdId3").css("display","none");
    }

    $($(".myImgFirst")[2]).css("display","none");
    $($(".myImgSecond")[2]).css("display","block");
    $($(".myImgFirst")[2]).attr("src",'');
    //重置清空form
    myCirResetForm();
    $(".up-modal-frame .up-btn-ok").attr("url","/uploadFileImg");
    $(".up-modal-frame .up-btn-ok").attr("data-type","cir");
    $("#chgCirTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;提交食品流通证");
    $("#cir-modal").modal("show");
}

function doMyLogic(myResult) {
    $(".myImgFirst").css("display","block");
    $(".myImgSecond").css("display","none");
    var btnUrl = $(".up-modal-frame .up-btn-ok").attr("url");
    var dataType = $(".up-modal-frame .up-btn-ok").attr("data-type");
    if(btnUrl=="/uploadOCRImg"){
        $("#busIconImg").val(myResult.extraResult);
        $($(".myImgFirst")[0]).attr("src",myResult.obj+myResult.extraResult);

        if(myResult.result){
            if(myResult.result.registration&&myResult.result.registration!=''){
                $("#registration").val(myResult.result.registration);
            }else{
                $("#registration").val('');
            }
            if(myResult.result.legalRepresentative&&myResult.result.legalRepresentative!=''){
                $("#legalRepresentative").val(myResult.result.legalRepresentative);
            }else{
                $("#legalRepresentative").val('');
            }
            if(myResult.result.registeredCapital&&myResult.result.registeredCapital!=''){
                $("#registeredCapital").val(myResult.result.registeredCapital);
            }else{
                $("#registeredCapital").val('');
            }
            if(myResult.result.companyName&&myResult.result.companyName!=''){
                $("#companyName").val(myResult.result.companyName);
            }else{
                $("#companyName").val('');
            }
            if(myResult.result.address&&myResult.result.address!=''){
                $("#address").val(myResult.result.address);
            }else{
                $("#address").val('');
            }
            if(myResult.result.deadline&&myResult.result.deadline!=''){
                $("#deadline").val(myResult.result.deadline);
            }else{
                $("#deadline").val('');
            }
            if(myResult.result.earlyWarningTime&&myResult.result.earlyWarningTime!=''){
                $("#earlyWarningTime").val(myResult.result.earlyWarningTime);
            }else{
                $("#earlyWarningTime").val('');
            }
            if(myResult.result.businessScope&&myResult.result.businessScope!=''){
                $("#businessScope").val(myResult.result.businessScope);
            }else{
                $("#businessScope").val('');
            }
            if(myResult.result.mainTypes&&myResult.result.mainTypes!=''){
                $("#mainTypes").val(myResult.result.mainTypes);
            }else{
                $("#mainTypes").val('');
            }
        }
    }else{
        if(dataType=="lic"){
            $("#licIconImg").val(myResult.result);
            $($(".myImgFirst")[1]).attr("src",myResult.obj+myResult.result);
        }else if(dataType=="cir"){
            $("#cirIconImg").val(myResult.result);
            $($(".myImgFirst")[2]).attr("src",myResult.obj+myResult.result);
        }
    }
}
function updateRole(id,updSdId,conType) {
    //isNaN是数字返回false
    if(id!=null && !isNaN(id)){
        var subUrl = '#';
        switch (conType){
            case 'business':
                subUrl = "/business/getUpdBusiness";
                //清空隐藏域id
                $("#busId").val("");
                $("#busIconImg").val("");
                $("#busSdId").val("");

                $("#form-group-sdId").css("display","none");

                $($(".myImgFirst")[0]).css("display","none");
                $($(".myImgSecond")[0]).css("display","block");

                $($(".myImgFirst")[0]).attr("src",'');

                $(".up-modal-frame .up-btn-ok").attr("url","/uploadOCRImg");
                $(".up-modal-frame .up-btn-ok").attr("data-type","bus");

                //重置清空form
                myBusResetForm();
                break;
            case 'license':
                subUrl = "/license/getUpdLicense";
                //清空隐藏域id
                $("#licId").val("");
                $("#licIconImg").val("");
                $("#licSdId").val("");

                $("#form-group-sdId2").css("display","none");

                $($(".myImgFirst")[1]).css("display","none");
                $($(".myImgSecond")[1]).css("display","block");
                $($(".myImgFirst")[1]).attr("src",'');

                $(".up-modal-frame .up-btn-ok").attr("url","/uploadFileImg");
                $(".up-modal-frame .up-btn-ok").attr("data-type","lic");
                //重置清空form
                myLicResetForm();
                break;
            case 'circulationCard':
                subUrl = "/circulationCard/getUpdCirculationCard";
                //清空隐藏域id
                $("#cirId").val("");
                $("#cirIconImg").val("");
                $("#cirSdId").val("");

                $("#form-group-sdId3").css("display","none");

                $($(".myImgFirst")[2]).css("display","none");
                $($(".myImgSecond")[2]).css("display","block");
                $($(".myImgFirst")[2]).attr("src",'');

                $(".up-modal-frame .up-btn-ok").attr("url","/uploadFileImg");
                $(".up-modal-frame .up-btn-ok").attr("data-type","cir");
                //重置清空form
                myCirResetForm();
                break;
        }
        $.post(subUrl,{"id":id,"sdId":updSdId},function(data){
            if(!isKickOut(data)){
                //没有被踢出
                if(data.resultCode==100){
                    switch (conType){
                        case 'business':
                            $("#busId").val(data.result.id);
                            $("#busIconImg").val(data.result.path);
                            $("#busSdId").val(data.result.sdId);

                            if(data.result.path&&data.result.path!=''){
                                if(data.result.path.indexOf("http:")==-1){
                                    $($(".myImgFirst")[0]).attr("src",data.result.picturePrefix+data.result.path);
                                }else{
                                    $($(".myImgFirst")[0]).attr("src",data.result.path);
                                }
                                $($(".myImgFirst")[0]).css("display","block");
                                $($(".myImgSecond")[0]).css("display","none");
                            }
                            if(data.result.registration&&data.result.registration!=''){
                                $("#registration").val(data.result.registration);
                            }
                            if(data.result.legalRepresentative&&data.result.legalRepresentative!=''){
                                $("#legalRepresentative").val(data.result.legalRepresentative);
                            }
                            if(data.result.registeredCapital&&data.result.registeredCapital!=''){
                                $("#registeredCapital").val(data.result.registeredCapital);
                            }
                            if(data.result.companyName&&data.result.companyName!=''){
                                $("#companyName").val(data.result.companyName);
                            }
                            if(data.result.address&&data.result.address!=''){
                                $("#address").val(data.result.address);
                            }
                            if(data.result.deadline&&data.result.deadline!=''){
                                $("#deadline").val(data.result.deadline);
                            }
                            if(data.result.earlyWarningTime&&data.result.earlyWarningTime!=''){
                                $("#earlyWarningTime").val(data.result.earlyWarningTime);
                            }
                            if(data.result.businessScope&&data.result.businessScope!=''){
                                $("#businessScope").val(data.result.businessScope);
                            }
                            if(data.result.mainTypes&&data.result.mainTypes!=''){
                                $("#mainTypes").val(data.result.mainTypes);
                            }
                            $("#chgBusTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;更新营业执照");
                            $("#bus-modal").modal("show");
                            break;
                        case 'license':
                            $("#licId").val(data.result.id);
                            $("#licIconImg").val(data.result.licenseImgPath);
                            $("#licSdId").val(data.result.sdId);
                            if(data.result.licenceImg&&data.result.licenceImg!=''){
                                if(data.result.licenceImg.indexOf("http:")==-1){
                                    $($(".myImgFirst")[1]).attr("src",data.result.picturePrefix+data.result.licenceImg);
                                }else{
                                    $($(".myImgFirst")[1]).attr("src",data.result.licenceImg);
                                }
                                $($(".myImgFirst")[1]).css("display","block");
                                $($(".myImgSecond")[1]).css("display","none");
                            }
                            if(data.result.name&&data.result.name!=''){
                                $("#name").val(data.result.name);
                            }
                            if(data.result.representative&&data.result.representative!=''){
                                $("#representative").val(data.result.representative);
                            }
                            if(data.result.validTime&&data.result.validTime!=''){
                                $("#validTime").val(data.result.validTime);
                            }
                            if(data.result.warningDate&&data.result.warningDate!=''){
                                $("#warningDate").val(data.result.warningDate);
                            }
                            if(data.result.serialNumber&&data.result.serialNumber!=''){
                                $("#serialNumber").val(data.result.serialNumber);
                            }
                            $("#chgLicTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;更新餐饮许可证");
                            $("#lic-modal").modal("show");
                            break;
                        case 'circulationCard':
                            $("#cirId").val(data.result.id);
                            $("#cirIconImg").val(data.result.circulationCardImg);
                            $("#cirSdId").val(data.result.sdId);

                            if(data.result.circulationCardImg&&data.result.circulationCardImg!=''){
                                if(data.result.circulationCardImg.indexOf("http:")==-1){
                                    $($(".myImgFirst")[2]).attr("src",data.result.picturePrefix+data.result.circulationCardImg);
                                }else{
                                    $($(".myImgFirst")[2]).attr("src",data.result.circulationCardImg);
                                }
                                $($(".myImgFirst")[2]).css("display","block");
                                $($(".myImgSecond")[2]).css("display","none");
                            }
                            if(data.result.name&&data.result.name!=''){
                                $("#nameCir").val(data.result.name);
                            }
                            if(data.result.representative&&data.result.representative!=''){
                                $("#representativeCir").val(data.result.representative);
                            }
                            if(data.result.validTime&&data.result.validTime!=''){
                                $("#validTimeCir").val(data.result.validTime);
                            }
                            if(data.result.warningDate&&data.result.warningDate!=''){
                                $("#warningDateCir").val(data.result.warningDate);
                            }
                            if(data.result.serialNumber&&data.result.serialNumber!=''){
                                $("#serialNumberCir").val(data.result.serialNumber);
                            }
                            $("#chgCirTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;更新食品流通证");
                            $("#cir-modal").modal("show");
                            break;
                    }
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
        var subUrl = "#";
        switch (conType){
            case 'business':
                subUrl = "/business/delBusiness";
                break;
            case 'license':
                subUrl = "/license/delLicense";
                break;
            case 'circulationCard':
                subUrl = "/circulationCard/delCirculationCard";
                break;
        }
        $.confirm({
            icon: '#icon-tishi1',
            theme: 'modern',
            title: '提示',
            content: '您确定要删除'+name+'吗？',
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
                                    showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",'删除'+name+'成功！');
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
function myBusResetForm() {
    //清空隐藏域id
    $("#busId").val("");
    $("#busIconImg").val("");
    $("#busSdId").val("");

    $($(".myImgFirst")[0]).css("display","none");
    $($(".myImgSecond")[0]).css("display","block");

    $($(".myImgFirst")[0]).attr("src",'');

    $("#selectSites").val("");
    $('#selectSites').selectator('refresh');
    //清空权限数组
    $('#busForm').resetForm();
    $('#busForm').clearForm();
}
function myLicResetForm(){
    //清空隐藏域id
    $("#licId").val("");
    $("#licIconImg").val("");
    $("#licSdId").val("");

    $($(".myImgFirst")[1]).css("display","none");
    $($(".myImgSecond")[1]).css("display","block");

    $($(".myImgFirst")[1]).attr("src",'');

    $("#selectSites2").val("");
    $('#selectSites2').selectator('refresh');
    $('#licForm').resetForm();
    $('#licForm').clearForm();
}
function myCirResetForm(){
    //清空隐藏域id
    $("#cirId").val("");
    $("#cirIconImg").val("");
    $("#cirSdId").val("");

    $($(".myImgFirst")[2]).css("display","none");
    $($(".myImgSecond")[2]).css("display","block");

    $($(".myImgFirst")[2]).attr("src",'');

    $("#selectSites3").val("");
    $('#selectSites3').selectator('refresh');
    $('#cirForm').resetForm();
    $('#cirForm').clearForm();
}