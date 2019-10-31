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
    url: "/ingredientBase/getIngredientBaseList",
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
            //$("#selectSites").append("<option value='"+val.sdId+"' data-subtitle='"+val.address+"' data-left='<img src="+sPho+">'>"+val.name+"</option>");
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
    /*$("#selectSites").selectator({
        labels: {
            search: '选择站点'
        },
        showAllOptionsOnFocus: true
    });
    $("#selectSites").next().css("width","100%");*/

    $("#userSites").next().css("width","100%");
    $("#userSites").next().css("min-height","3em");
    $("#userSites").next().find(".selectator_input").css("background","#1b1b1b");
    $("#userSites").on("change",function (e) {
        $(".search--open").css("background","rgba(0, 0, 0, 0.68)");
    });
    loadClassification();

    /*$("#userSites").next().css("min-height","3em");
    $("#userSites").next().find(".selectator_input").css("background","#1b1b1b");*/
    $("#smallCatogory").selectator({
        labels: {
            search: '选择二级分类'
        },
        showAllOptionsOnFocus: true
    });
    $("#smallCatogory").next().css("width","100%");

    $("#smallCategoryId").selectator({
        labels: {
            search: '选择二级分类'
        },
        showAllOptionsOnFocus: true
    });
    $("#smallCategoryId").next().css("width","100%");
    $("#smallCategoryId").next().find(".selectator_input").css("background","#1b1b1b");
    /*$("#userSites").next().css("min-height","3em");
    $("#userSites").next().find(".selectator_input").css("background","#1b1b1b");*/
    $.extend($.validationEngineLanguage.allRules,{ "isNameSdIdExist":{
            "url": "/ingredientBase/isNameExist",
            /*"extraDataDynamic": ['#user_id', '#user_email'],*/
            "extraDataDynamic": [['#sdId', '#id']],
            "alertText": "该食材已存在",
            "alertTextOk": "食材名可以使用",
            "alertTextLoad": "正在验证，请稍等。。。"
    }
    });
    //为用户加站点角色的时候需要判断角色为长度为1时，需要判断设置的角色站点id是否和
    $("#ingBaseForm").validationEngine({
        binded:false,
        maxErrorsPerField:1,
        promptPosition: "topLeft:100",
        scroll:true,
        showOneMessage:true,
        validateNonVisibleFields:true,
        focusFirstField:true,
        ajaxFormValidation: true,
        ajaxFormValidationURL:"/ingredientBase/setIngredientBase",
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

    $("#ratedTermDW").selectator({
        labels: {
            search: '选择保质期单位'
        },
        showAllOptionsOnFocus: true
    });
    $("#ratedTermDW").next().css("width","100%");
    $("#ratedTermDW").next().find(".selectator_input").css("background","#1b1b1b");

    $("#checkStatus").selectator({
        labels: {
            search: '选择检测状态'
        },
        showAllOptionsOnFocus: true
    });
    $("#checkStatus").next().css("width","100%");
    $("#checkStatus").next().find(".selectator_input").css("background","#1b1b1b");

    $("#needCleaning").selectator({
        labels: {
            search: '选择是否需要清洗'
        },
        showAllOptionsOnFocus: true
    });
    $("#needCleaning").next().css("width","100%");
    $("#needCleaning").next().find(".selectator_input").css("background","#1b1b1b");



    $("#bomType").selectator({
        labels: {
            search: '选择BOM类别'
        },
        showAllOptionsOnFocus: true
    });
    $("#bomType").next().css("width","100%");
    $("#bomType").next().find(".selectator_input").css("background","#1b1b1b");
    loadMeteringUnit();
    loadSupper();
    loadWarehouse();
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
    if($("#userSites").val()!=null&&$("#userSites").val()!=''){
        if($("#userSites").val().length==1){
            if($("#userSites").val()[0]!=$("#mainCatogory").find("option:selected").attr("data-siteId")){
                createJBox('yellow',['right','bottom'],'提示:系统检测到选择的站点与食材分类不一致，系统已默认搜寻食材分类所属站点',4600);
            }
        }else{
            createJBox('yellow',['right','bottom'],'提示:系统检测到您已选择多个站点，系统以食材分类所属站点为您努力搜寻',4600);
        }
    }
    curPag = 1;
    ajaxSCJCXXSearchFormOption.data={page:curPag,limit:limCount};
    $("#scjcxxSearchForm").ajaxSubmit(ajaxSCJCXXSearchFormOption);
    return false;
}
function setErrorStyle() {

    setTimeout(function () {
        $("#meteringName").prev().css("top","0px");
        $("#meteringName").prev().css("left","15.9em");

        $("#mainCategoryId").prev().css("top","0px");
        $("#mainCategoryId").prev().css("left","15.9em");

        $("#ratedTermDW").prev().css("top","0px");
        $("#ratedTermDW").prev().css("left","15.9em");

        $("#whouseId").prev().css("top","0px");
        $("#whouseId").prev().css("left","15.9em");

        $("#suppId").prev().css("top","0px");
        $("#suppId").prev().css("left","15.9em");

        $("#bomType").prev().css("top","0px");
        $("#bomType").prev().css("left","15.9em");

    },1000);



}
function showRequest(formData, jqForm, options){
    if($("#bomType").val()!=undefined&&$("#bomType").val()!=null){
        $("#boomTypeStr").val($("#bomType").val().join(","));
    }
    if($("#checkStatus").val()==undefined||$("#checkStatus").val()==null||$("#checkStatus").val()==''){
        $("#checkStatus").val("2");
    }
    if($("#needCleaning").val()==undefined||$("#needCleaning").val()==null||$("#needCleaning").val()==''){
        $("#needCleaning").val("1");
    }
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
                    var iBDate;
                    if(val.createDate){
                        iBDate = val.createDate;
                    }
                    var ingredientName = '';
                    var topStr = '';
                    if(val.ingredientName){
                        topStr = val.ingredientName;
                        ingredientName = val.ingredientName;
                    }

                    if(val.mainCategoryName&&val.mainCategoryName!=''){
                        topStr = topStr+"("+val.mainCategoryName;
                    }
                    if(val.smallCategoryName&&val.smallCategoryName!=''){
                        topStr = topStr+"-"+val.smallCategoryName+")";
                    }else{
                        topStr = topStr+")";
                    }
                    if(val.meteringName&&val.meteringName!=''){
                        topStr = topStr + "-" + val.meteringName + "计量";
                    }
                    if(val.basePrice){
                        topStr = topStr + "-价格"+val.basePrice+"元";
                    }
                    var rateTwo = '';
                    if(val.ratedTerm){
                        if(val.ratedTermDW&&val.ratedTermDW!=''){
                            rateTwo = "-保质期"+val.ratedTerm+val.ratedTermDW;
                        }
                    }
                    topStr = topStr + rateTwo;
                    var ibImgPath = '';
                    if(val.scspImgPath&&val.scspImgPath!=''){
                        if(val.scspImgPath.indexOf("http:")!=-1){
                            ibImgPath = val.scspImgPath;
                        }else{
                            ibImgPath = val.picturePrefix+val.scspImgPath;
                        }

                    }else{
                        ibImgPath = "../../../images/VRPicture.png";
                    }
                    var ibBottomStr = '';
                    if(val.checkStatus&&val.checkStatus!=''){
                        switch (val.checkStatus) {
                            case '1':
                                ibBottomStr = "需要检测";
                                break;
                            case '2':
                                ibBottomStr = "不需要检测";
                                break;
                        }
                    }
                    if(val.inventoryLimit){
                        ibBottomStr = ibBottomStr + "并且库存不得低于" + val.inventoryLimit + val.meteringName;
                    }
                    $("#userListUl").append("<div class='col-md-3 col-sm-6 col-xs-12'>" +
                        "<div class='tpl-table-images-content'>" +
                        "<div class='tpl-table-images-content-i-time'>创建时间："+iBDate+"</div>" +
                        "<div class='tpl-i-title' style='height: 4em;' data-toggle='tooltip' title='"+topStr+"' data-original-title='"+topStr+"'>" +topStr+
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
                        "<div class='tpl-i-font' style='height: 4em;' data-toggle='tooltip' title='"+ibBottomStr+"' data-original-title='"+ibBottomStr+"'>" + ibBottomStr +
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
                        "<button type='button' class='am-btn am-btn-default am-btn-danger' style='width: 50% !important;' onclick='delRole("+val.id+","+val.sdId+","+"\""+val.ingredientName+"\")'><i class='iconfont iconshanchutishi'></i> 删除</button>" +
                        /*"<button type='button' class='am-btn am-btn-default am-btn-warning' style='width: 33% !important;' onclick='exportSingle("+val.id+")'><i class='iconfont iconexport1'></i> 导出</button>" +*/
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>");
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
            }else{
                showJqueryConfirmWindow("#icon-tishi1","未获取到食材信息");
            }
            isShow = true;
        }else if(data.resultCode==101){
            showJqueryConfirmWindow("#icon-shangxin1","获取食材信息异常！深感抱歉，请联系管理人员为您加急处理");
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
function befShowAddMod(){
    //清空隐藏域id
    $("#id").val("");
    $("#iconImg").val("");
    $("#ggxhdw").val("");
    $("#sdId").val("");
    $("#boomTypeStr").val("");

    reloadSelectatorJson[0]= {
        id:'meteringName',
        inputText:"选择计量单位",
        curVal:'',
        sdId:'',
        curSelArray:meteringNameArray
    }
    reloadSelectatorJson[1]= {
        id:'whouseId',
        inputText:"选择仓库",
        curVal:'',
        sdId:'',
        curSelArray:whouseIdArray
    }
    reloadSelectatorJson[2]= {
        id:'suppId',
        inputText:"选择供应商",
        curVal:'',
        sdId:'',
        curSelArray:suppIdArray
    }
    reloadSelectatorJson[3]= {
        id:'mainCategoryId',
        inputText:"选择一级分类",
        curVal:'',
        sdId:'',
        curSelArray:mainCategoryIdArray
    }
    reloadSelectatorJson[4]= {
        id:'smallCategoryId',
        inputText:"选择二级分类",
        curVal:'',
        sdId:'',
        curSelArray:smallArray
    }
    reloadMySelectator(reloadSelectatorJson);
    $("#ggxhDWSpan").text('单位');
    $("#ratedTermDW").val('');
    $('#ratedTermDW').selectator('refresh');
    $("#checkStatus").val('2');
    $('#checkStatus').selectator('refresh');
    $("#needCleaning").val('1');
    $('#needCleaning').selectator('refresh');

    $("#bomType").html("");
    $("#bomType").selectator('destroy');
    $("#bomType").append("<option value='1'>主料</option>");
    $("#bomType").append("<option value='2'>辅料</option>");
    $("#bomType").append("<option value='4'>配料</option>");
    $("#bomType").append("<option value='8'>调味品</option>");

    $("#bomType").selectator({
        labels: {
            search: '选择BOM类别'
        },
        showAllOptionsOnFocus: true
    });
    $("#bomType").next().css("width","100%");
    $("#bomType").next().find(".selectator_input").css("background","#1b1b1b");

    $(".myImgFirst").css("display","none");
    $(".myImgSecond").css("display","block");
    $("#up-img-touch").css("display","block");
    $(".myImgFirst").attr("src",'');
    //重置清空form
    myResetForm();

    $("#chgIngBaseTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;新增食材");
    $("#compose-modal").modal("show");
}
function updateRole(id,updSdId) {
    //清空隐藏域id
    $("#id").val("");
    //重置清空form
    myResetForm();
    //isNaN是数字返回false
    if(id!=null && !isNaN(id)){
        $.post("/ingredientBase/getUpdIngredientBase",{"id":id,"sdId":updSdId},function(data){
            if(!isKickOut(data)){
                //没有被踢出
                if(data.resultCode==100){
                    $("#id").val(data.result.id);
                    $("#sdId").val(data.result.sdId);

                    reloadSelectatorJson[0]= {
                        id:'meteringName',
                        inputText:"选择计量单位",
                        curVal:data.result.meteringName,
                        sdId:data.result.sdId,
                        curSelArray:meteringNameArray
                    }
                    reloadSelectatorJson[1]= {
                        id:'whouseId',
                        inputText:"选择仓库",
                        curVal:data.result.whouseId,
                        sdId:data.result.sdId,
                        curSelArray:whouseIdArray
                    }
                    reloadSelectatorJson[2]= {
                        id:'suppId',
                        inputText:"选择供应商",
                        curVal:data.result.suppId,
                        sdId:data.result.sdId,
                        curSelArray:suppIdArray
                    }
                    reloadSelectatorJson[3]= {
                        id:'mainCategoryId',
                        inputText:"选择一级分类",
                        curVal:data.result.mainCategoryId,
                        sdId:data.result.sdId,
                        curSelArray:mainCategoryIdArray
                    }
                    reloadSelectatorJson[4]= {
                        id:'smallCategoryId',
                        inputText:"选择二级分类",
                        curVal:data.result.smallCategoryId,
                        sdId:data.result.sdId,
                        curSelArray:smallArray
                    }
                    reloadMySelectator(reloadSelectatorJson);

                    if(data.result.ingredientName&&data.result.ingredientName!=''){
                        $("#ingredientName").val(data.result.ingredientName);
                    }

                    if(data.result.ratedTerm){
                        $("#ratedTerm").val(data.result.ratedTerm);
                    }
                    if(data.result.ratedTermDW&&data.result.ratedTermDW!=''){
                        $("#ratedTermDW").val(data.result.ratedTermDW);
                        $('#ratedTermDW').selectator('refresh');
                    }
                    if(data.result.nearPeriodValue){
                        $("#nearPeriodValue").val(data.result.nearPeriodValue);
                    }
                    if(data.result.basePrice){
                        $("#basePrice").val(data.result.basePrice);
                    }
                    if(data.result.ggxh&&data.result.ggxh!=''){
                        $("#ggxh").val(data.result.ggxh);
                    }
                    if(data.result.ggxhdw&&data.result.ggxhdw!=''){
                        $("#ggxhdw").val(data.result.ggxhdw);
                        $("#ggxhDWSpan").text(data.result.ggxhdw+"/"+data.result.meteringName);
                    }
                    if(data.result.inventoryLimit){
                        $("#inventoryLimit").val(data.result.inventoryLimit);
                    }
                    if(data.result.checkStatus&&data.result.checkStatus!=''){
                        $("#checkStatus").val(data.result.checkStatus);
                        $('#checkStatus').selectator('refresh');
                    }
                    if(data.result.purchasingStandard&&data.result.purchasingStandard!=''){
                        $("#purchasingStandard").val(data.result.purchasingStandard);
                    }
                    if(data.result.noMinalrating&&data.result.noMinalrating!=''){
                        $("#noMinalrating").val(data.result.noMinalrating);
                    }
                    if(data.result.needCleaning&&data.result.needCleaning!=''){
                        $("#needCleaning").val(data.result.needCleaning);
                        $('#needCleaning').selectator('refresh');
                    }
                    if(data.result.cleanWater&&data.result.cleanWater!=''){
                        $("#cleanWater").val(data.result.cleanWater);
                    }
                    if(data.result.cleaningTime&&data.result.cleaningTime!=''){
                        $("#cleaningTime").val(data.result.cleaningTime);
                    }
                    $("#bomType").html("");
                    $("#bomType").selectator('destroy');
                    $("#bomType").append("<option value='1'>主料</option>");
                    $("#bomType").append("<option value='2'>辅料</option>");
                    $("#bomType").append("<option value='4'>配料</option>");
                    $("#bomType").append("<option value='8'>调味品</option>");

                    if(data.result.boomType){

                        switch (data.result.boomType){
                            case 1:
                                $("#bomType").find("option[value='1']").attr("selected","selected");
                                break;
                            case 2:
                                $("#bomType").find("option[value='2']").attr("selected","selected");
                                break;
                            case 3:
                                $("#bomType").find("option[value='1']").attr("selected","selected");
                                $("#bomType").find("option[value='2']").attr("selected","selected");
                                break;
                            case 4:
                                $("#bomType").find("option[value='4']").attr("selected","selected");
                                break;
                            case 8:
                                $("#bomType").find("option[value='8']").attr("selected","selected");
                                break;
                            case 16:
                                $("#sa_base").prop("checked",true);
                                break;
                            case 32:
                                $("#su_base").prop("checked",true);
                                break;
                        }
                    }
                    $("#bomType").selectator({
                        labels: {
                            search: '选择BOM类别'
                        },
                        showAllOptionsOnFocus: true
                    });
                    $("#bomType").next().css("width","100%");
                    $("#bomType").next().find(".selectator_input").css("background","#1b1b1b");

                    if(data.result.scspImgPath&&data.result.scspImgPath!=""){
                        if(data.result.scspImgPath.indexOf("http:")==-1){
                            $(".myImgFirst").attr("src",data.result.picturePrefix+data.result.scspImgPath);
                        }else{
                            $(".myImgFirst").attr("src",data.result.scspImgPath);
                        }
                        //$("#iconImg").val(data.result.scspImgPath);
                        $(".myImgFirst").css("display","block");
                        $(".myImgSecond").css("display","none");
                        $("#up-img-touch").css("display","none");
                    }else{
                        $(".myImgFirst").css("display","none");
                        $(".myImgSecond").css("display","block");
                        $("#up-img-touch").css("display","block");
                        $(".myImgFirst").attr("src",'');
                    }
                    $("#chgIngBaseTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;更新食材");

                    $("#compose-modal").modal("show");
                }else if(data.resultCode==101){
                    showJqueryConfirmWindow("#icon-shangxin1","获取食材信息异常！深感抱歉，请联系管理人员为您加急处理");
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
            content: '您确定要删除'+name+'食材吗？',
            animation: 'news',//动画
            closeAnimation: 'news',//关闭动画
            /*autoClose: 'showMyMsg2|3000',*/
            buttons: {
                showMyMsg2: {
                    text: '确定',
                    action: function () {

                        $.post("/ingredientBase/delIngredientBase",{"id":id,"sdId":delSdId},function(data){
                            if(!isKickOut(data)){
                                //没有被踢出
                                if(data.resultCode==100){
                                    //刷新数据
                                    loadUserList();
                                    showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",'删除'+name+'食材成功！');
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
function loadClassification() {
    $.post("/categoryBase/getAll",function(data){
        if(!isKickOut(data)){
            //没有被踢出
            if(data.resultCode==100){
                $("#mainCatogory").html("");
                $("#mainCategoryId").html("");
                $("#mainCatogory").append("<option value=''>选择一级分类</option>");
                $("#mainCategoryId").append("<option value=''>选择一级分类</option>");
                $.each(data.result,function (ind,val) {
                    if(val.pId==0){
                        $("#mainCatogory").append("<option value='"+val.id+"' data-siteId='"+val.sdId+"' data-subtitle='"+val.siteName+"' data-left='<img src="+val.sitePhoto+">'>"+val.categoryName+"</option>");
                        //
                        $("#mainCategoryId").append("<option value='"+val.id+"' data-siteId='"+val.sdId+"' data-subtitle='"+val.siteName+"' data-left='<img src="+val.sitePhoto+">'>"+val.categoryName+"</option>");
                        mainCategoryIdArray.push(val);
                    }else{
                        smallArray.push(val);
                        /*var curKey = val.pId+"-"+val.sdId;
                        smallJson[curKey] = {
                            id:val.id,
                            categoryName:val.categoryName,
                            sdId:val.sdId,
                            siteName:val.siteName,
                            sitePhoto:val.sitePhoto
                        };*/
                    }

                });
                /*console.log(smallJson);*/
                $("#mainCatogory").selectator({
                    labels: {
                        search: '选择一级分类'
                    },
                    showAllOptionsOnFocus: true
                });
                $("#mainCatogory").next().css("width","100%");
                $("#mainCatogory").next().find(".selectator_input").css("background","#1b1b1b");

                $("#mainCategoryId").selectator({
                    labels: {
                        search: '选择一级分类'
                    },
                    showAllOptionsOnFocus: true
                });
                $("#mainCategoryId").next().css("width","100%");
                $("#mainCategoryId").next().find(".selectator_input").css("background","#1b1b1b");
            }/*else if(data.resultCode==101){
                showJqueryConfirmWindow("#icon-shangxin1","操作失败！深感抱歉，请联系管理人员为您加急处理");
            }else{
                showJqueryConfirmWindow("#icon-tishi1",data.msg);
            }*/
        }
    });
}
function getCurSmallCatogory() {
    var mainCatId = $("#mainCatogory").val();
    var mainSId = $("#mainCatogory").find("option:selected").attr("data-siteId");
    if(mainCatId!=undefined&&mainCatId!=null&&mainCatId!=''){
        $("#smallCatogory").selectator('destroy');
        $("#smallCatogory").html("");
        $("#smallCatogory").append("<option value=''>选择二级分类</option>");
        $.each(smallArray,function (ind,val) {
            if((mainCatId == val.pId)&&(mainSId==val.sdId)){
                $("#smallCatogory").append("<option value='"+val.id+"' data-siteId='"+val.sdId+"' data-subtitle='"+val.siteName+"' data-left='<img src="+val.sitePhoto+">'>"+val.categoryName+"</option>");
            }
        });
        //$('#smallCatogory').selectator('refresh');
        $("#smallCatogory").selectator({
            labels: {
                search: '选择二级分类'
            },
            showAllOptionsOnFocus: true
        });
        $("#smallCatogory").next().css("width","100%");
        $("#ingSdId").val(mainSId);
    }else{
        $("#smallCatogory").html("");
        $("#ingSdId").val("");
    }
}
function checkCurSiteOption(curId) {
    var mainCatId = $("#"+curId).val();
    var mainSId = $("#"+curId).find("option:selected").attr("data-siteId");
    if(isManySite){
        $("#sdId").val(mainSId);
    }
    switch (curId){
        case 'meteringName':
            var mainCatSId = $("#mainCategoryId").find("option:selected").attr("data-siteId");
            var whouseSId = $("#whouseId").find("option:selected").attr("data-siteId");
            var suppSId = $("#suppId").find("option:selected").attr("data-siteId");

            var dwSpan = $("#ggxhDWSpan").text();
            if(dwSpan!="单位"){
                if(dwSpan.indexOf("/")!=-1){
                    $("#ggxhDWSpan").text(dwSpan.split("/")[0]+"/"+mainCatId);
                }else{
                    $("#ggxhDWSpan").text(dwSpan+"/"+mainCatId);
                }

            }

            if(mainSId!=mainCatSId){
                $("#mainCategoryId").html("");
                $("#mainCategoryId").selectator('destroy');
                $("#mainCategoryId").append("<option value=''>选择一级分类</option>");
                $.each(mainCategoryIdArray,function (ind,val) {
                    if(mainSId==val.sdId){
                        $("#mainCategoryId").append("<option value='"+val.id+"' data-siteId='"+val.sdId+"' data-subtitle='"+val.siteName+"' data-left='<img src="+val.sitePhoto+">'>"+val.categoryName+"</option>");
                    }
                });
                //$('#smallCatogory').selectator('refresh');
                $("#mainCategoryId").selectator({
                    labels: {
                        search: '选择一级分类'
                    },
                    showAllOptionsOnFocus: true
                });
                $("#mainCategoryId").next().css("width","100%");
                $("#mainCategoryId").next().find(".selectator_input").css("background","#1b1b1b");


                $("#smallCategoryId").selectator('destroy');
                $("#smallCategoryId").html("");
                $("#smallCategoryId").append("<option value=''>选择二级分类</option>");

                //$('#smallCatogory').selectator('refresh');
                $("#smallCategoryId").selectator({
                    labels: {
                        search: '选择二级分类'
                    },
                    showAllOptionsOnFocus: true
                });
                $("#smallCategoryId").next().css("width","100%");
                $("#smallCategoryId").next().find(".selectator_input").css("background","#1b1b1b");
            }
            if(mainSId!=whouseSId){

                $("#whouseId").html("");
                $("#whouseId").selectator('destroy');
                $("#whouseId").append("<option value=''>选择仓库</option>");

                $.each(whouseIdArray,function (ind,val) {
                    if(mainSId==val.sdId){
                        $("#whouseId").append("<option value='"+val.id+"' data-siteId='"+val.sdId+"' data-subtitle='"+val.siteName+"' data-left='<img src="+val.sitePhoto+">'>"+val.whName+"</option>");
                    }
                });
                //$('#smallCatogory').selectator('refresh');
                $("#whouseId").selectator({
                    labels: {
                        search: '选择仓库'
                    },
                    showAllOptionsOnFocus: true
                });
                $("#whouseId").next().css("width","100%");
                $("#whouseId").next().find(".selectator_input").css("background","#1b1b1b");
            }
            if(mainSId!=suppSId){

                $("#suppId").html("");
                $("#suppId").selectator('destroy');
                $("#suppId").append("<option value=''>选择供应商</option>");
                $.each(suppIdArray,function (ind,val) {
                    if(mainSId==val.sdId){
                        $("#suppId").append("<option value='"+val.id+"' data-siteId='"+val.sdId+"' data-subtitle='"+val.siteName+"' data-left='<img src="+val.sitePhoto+">'>"+val.supplierName+"</option>");
                    }
                });
                //$('#smallCatogory').selectator('refresh');
                $("#suppId").selectator({
                    labels: {
                        search: '选择供应商'
                    },
                    showAllOptionsOnFocus: true
                });
                $("#suppId").next().css("width","100%");
                $("#suppId").next().find(".selectator_input").css("background","#1b1b1b");
            }
            break;
        case 'mainCategoryId':
            var meteringNameSId = $("#meteringName").find("option:selected").attr("data-siteId");
            var whouseSId = $("#whouseId").find("option:selected").attr("data-siteId");
            var suppSId = $("#suppId").find("option:selected").attr("data-siteId");

            if(mainCatId!=undefined&&mainCatId!=null&&mainCatId!=''){
                $("#smallCategoryId").selectator('destroy');
                $("#smallCategoryId").html("");
                $("#smallCategoryId").append("<option value=''>选择二级分类</option>");
                $.each(smallArray,function (ind,val) {
                    if((mainCatId == val.pId)&&(mainSId==val.sdId)){
                        $("#smallCategoryId").append("<option value='"+val.id+"' data-siteId='"+val.sdId+"' data-subtitle='"+val.siteName+"' data-left='<img src="+val.sitePhoto+">'>"+val.categoryName+"</option>");
                    }
                });
                $("#smallCategoryId").selectator({
                    labels: {
                        search: '选择二级分类'
                    },
                    showAllOptionsOnFocus: true
                });
                $("#smallCategoryId").next().css("width","100%");
                $("#smallCategoryId").next().find(".selectator_input").css("background","#1b1b1b");
            }else{
                $("#smallCatogory").html("");
            }

            if(mainSId!=meteringNameSId){

                $("#meteringName").html("");
                $("#meteringName").selectator('destroy');
                $("#meteringName").append("<option value=''>选择计量单位</option>");
                $.each(meteringNameArray,function (ind,val) {
                    if(mainSId==val.sdId){
                        $("#meteringName").append("<option value='"+val.meteringName+"' data-siteId='"+val.sdId+"' data-subtitle='"+val.siteName+"' data-left='<img src="+val.sitePhoto+">'>"+val.meteringName+"</option>");
                    }
                });
                //$('#smallCatogory').selectator('refresh');
                $("#meteringName").selectator({
                    labels: {
                        search: '选择计量单位'
                    },
                    showAllOptionsOnFocus: true
                });
                $("#meteringName").next().css("width","100%");
                $("#meteringName").next().find(".selectator_input").css("background","#1b1b1b");
            }
            if(mainSId!=whouseSId){

                $("#whouseId").html("");
                $("#whouseId").selectator('destroy');
                $("#whouseId").append("<option value=''>选择仓库</option>");
                $.each(whouseIdArray,function (ind,val) {
                    if(mainSId==val.sdId){
                        $("#whouseId").append("<option value='"+val.id+"' data-siteId='"+val.sdId+"' data-subtitle='"+val.siteName+"' data-left='<img src="+val.sitePhoto+">'>"+val.whName+"</option>");
                    }
                });
                //$('#smallCatogory').selectator('refresh');
                $("#whouseId").selectator({
                    labels: {
                        search: '选择仓库'
                    },
                    showAllOptionsOnFocus: true
                });
                $("#whouseId").next().css("width","100%");
                $("#whouseId").next().find(".selectator_input").css("background","#1b1b1b");
            }
            if(mainSId!=suppSId){

                $("#suppId").html("");
                $("#suppId").selectator('destroy');
                $("#suppId").append("<option value=''>选择供应商</option>");
                $.each(suppIdArray,function (ind,val) {
                    if(mainSId==val.sdId){
                        $("#suppId").append("<option value='"+val.id+"' data-siteId='"+val.sdId+"' data-subtitle='"+val.siteName+"' data-left='<img src="+val.sitePhoto+">'>"+val.supplierName+"</option>");
                    }
                });
                //$('#smallCatogory').selectator('refresh');
                $("#suppId").selectator({
                    labels: {
                        search: '选择供应商'
                    },
                    showAllOptionsOnFocus: true
                });
                $("#suppId").next().css("width","100%");
                $("#suppId").next().find(".selectator_input").css("background","#1b1b1b");
            }
            break;
        case 'whouseId':
            var meteringNameSId = $("#meteringName").find("option:selected").attr("data-siteId");
            var mainCatSId = $("#mainCategoryId").find("option:selected").attr("data-siteId");
            var suppSId = $("#suppId").find("option:selected").attr("data-siteId");
            if(mainSId!=meteringNameSId){

                $("#meteringName").html("");
                $("#meteringName").selectator('destroy');
                $("#meteringName").append("<option value=''>选择计量单位</option>");
                $.each(meteringNameArray,function (ind,val) {
                    if(mainSId==val.sdId){
                        $("#meteringName").append("<option value='"+val.meteringName+"' data-siteId='"+val.sdId+"' data-subtitle='"+val.siteName+"' data-left='<img src="+val.sitePhoto+">'>"+val.meteringName+"</option>");
                    }
                });
                //$('#smallCatogory').selectator('refresh');
                $("#meteringName").selectator({
                    labels: {
                        search: '选择计量单位'
                    },
                    showAllOptionsOnFocus: true
                });
                $("#meteringName").next().css("width","100%");
                $("#meteringName").next().find(".selectator_input").css("background","#1b1b1b");
            }
            if(mainSId!=mainCatSId){
                $("#mainCategoryId").html("");
                $("#mainCategoryId").selectator('destroy');
                $("#mainCategoryId").append("<option value=''>选择一级分类</option>");
                $.each(mainCategoryIdArray,function (ind,val) {
                    if(mainSId==val.sdId){
                        $("#mainCategoryId").append("<option value='"+val.id+"' data-siteId='"+val.sdId+"' data-subtitle='"+val.siteName+"' data-left='<img src="+val.sitePhoto+">'>"+val.categoryName+"</option>");
                    }
                });
                //$('#smallCatogory').selectator('refresh');
                $("#mainCategoryId").selectator({
                    labels: {
                        search: '选择一级分类'
                    },
                    showAllOptionsOnFocus: true
                });
                $("#mainCategoryId").next().css("width","100%");
                $("#mainCategoryId").next().find(".selectator_input").css("background","#1b1b1b");


                $("#smallCategoryId").selectator('destroy');
                $("#smallCategoryId").html("");
                $("#smallCategoryId").append("<option value=''>选择二级分类</option>");

                //$('#smallCatogory').selectator('refresh');
                $("#smallCategoryId").selectator({
                    labels: {
                        search: '选择二级分类'
                    },
                    showAllOptionsOnFocus: true
                });
                $("#smallCategoryId").next().css("width","100%");
                $("#smallCategoryId").next().find(".selectator_input").css("background","#1b1b1b");
            }
            if(mainSId!=suppSId){

                $("#suppId").html("");
                $("#suppId").selectator('destroy');
                $("#suppId").append("<option value=''>选择供应商</option>");
                $.each(suppIdArray,function (ind,val) {
                    if(mainSId==val.sdId){
                        $("#suppId").append("<option value='"+val.id+"' data-siteId='"+val.sdId+"' data-subtitle='"+val.siteName+"' data-left='<img src="+val.sitePhoto+">'>"+val.supplierName+"</option>");
                    }
                });
                //$('#smallCatogory').selectator('refresh');
                $("#suppId").selectator({
                    labels: {
                        search: '选择供应商'
                    },
                    showAllOptionsOnFocus: true
                });
                $("#suppId").next().css("width","100%");
                $("#suppId").next().find(".selectator_input").css("background","#1b1b1b");
            }
            break;
        case 'suppId':
            var meteringNameSId = $("#meteringName").find("option:selected").attr("data-siteId");
            var mainCatSId = $("#mainCategoryId").find("option:selected").attr("data-siteId");
            var whouseSId = $("#whouseId").find("option:selected").attr("data-siteId");
            if(mainSId!=meteringNameSId){

                $("#meteringName").html("");
                $("#meteringName").selectator('destroy');
                $("#meteringName").append("<option value=''>选择一级分类</option>");
                $.each(meteringNameArray,function (ind,val) {
                    if(mainSId==val.sdId){
                        $("#meteringName").append("<option value='"+val.meteringName+"' data-siteId='"+val.sdId+"' data-subtitle='"+val.siteName+"' data-left='<img src="+val.sitePhoto+">'>"+val.meteringName+"</option>");
                    }
                });
                //$('#smallCatogory').selectator('refresh');
                $("#meteringName").selectator({
                    labels: {
                        search: '选择计量单位'
                    },
                    showAllOptionsOnFocus: true
                });
                $("#meteringName").next().css("width","100%");
                $("#meteringName").next().find(".selectator_input").css("background","#1b1b1b");
            }
            if(mainSId!=mainCatSId){
                $("#mainCategoryId").html("");
                $("#mainCategoryId").selectator('destroy');
                $("#mainCategoryId").append("<option value=''>选择一级分类</option>");
                $.each(mainCategoryIdArray,function (ind,val) {
                    if(mainSId==val.sdId){
                        $("#mainCategoryId").append("<option value='"+val.id+"' data-siteId='"+val.sdId+"' data-subtitle='"+val.siteName+"' data-left='<img src="+val.sitePhoto+">'>"+val.categoryName+"</option>");
                    }
                });
                //$('#smallCatogory').selectator('refresh');
                $("#mainCategoryId").selectator({
                    labels: {
                        search: '选择一级分类'
                    },
                    showAllOptionsOnFocus: true
                });
                $("#mainCategoryId").next().css("width","100%");
                $("#mainCategoryId").next().find(".selectator_input").css("background","#1b1b1b");


                $("#smallCategoryId").selectator('destroy');
                $("#smallCategoryId").html("");
                $("#smallCategoryId").append("<option value=''>选择二级分类</option>");

                //$('#smallCatogory').selectator('refresh');
                $("#smallCategoryId").selectator({
                    labels: {
                        search: '选择二级分类'
                    },
                    showAllOptionsOnFocus: true
                });
                $("#smallCategoryId").next().css("width","100%");
                $("#smallCategoryId").next().find(".selectator_input").css("background","#1b1b1b");
            }
            if(mainSId!=whouseSId){

                $("#whouseId").html("");
                $("#whouseId").selectator('destroy');
                $("#whouseId").append("<option value=''>选择仓库</option>");
                $.each(whouseIdArray,function (ind,val) {
                    if(mainSId==val.sdId){
                        $("#whouseId").append("<option value='"+val.id+"' data-siteId='"+val.sdId+"' data-subtitle='"+val.siteName+"' data-left='<img src="+val.sitePhoto+">'>"+val.whName+"</option>");
                    }
                });
                //$('#smallCatogory').selectator('refresh');
                $("#whouseId").selectator({
                    labels: {
                        search: '选择仓库'
                    },
                    showAllOptionsOnFocus: true
                });
                $("#whouseId").next().css("width","100%");
                $("#whouseId").next().find(".selectator_input").css("background","#1b1b1b");
            }
            break;
    }
    $(".selectator_options").css("z-index","999");
    $(".selectator_input").css("z-index","999");

}
function exportSingle() {
    var exportSdIds = '';
    if($("#userSites").val()){
        exportSdIds = "?sdIds="+$("#userSites").val().toString();
    }
    if(exportSdIds!=''){
        exportSdIds = exportSdIds+"&ingredientName="+$("#topIName").val();
    }else{
        exportSdIds = "?ingredientName="+$("#topIName").val();
    }
    var exportMCategoryId = '';
    if($("#mainCatogory").val()&&$("#mainCatogory").val()!=''){
        exportMCategoryId = "mainCategoryId="+$("#mainCatogory").val();
    }
    if(exportSdIds!=''){
        if(exportMCategoryId!=''){
            exportSdIds = exportSdIds+"&"+exportMCategoryId;
        }
    }else{
        if(exportMCategoryId!=''){
            exportSdIds = "?"+exportMCategoryId;
        }
    }
    var exportSCategoryId = '';
    if($("#smallCatogory").val()){
        exportSCategoryId = "smallCategoryId="+$("#smallCatogory").val();
    }
    if(exportSdIds!=''){
        if(exportSCategoryId!=''){
            exportSdIds = exportSdIds+"&"+exportSCategoryId;
        }
    }else{
        if(exportSCategoryId!=''){
            exportSdIds = "?"+exportSCategoryId;
        }
    }
    window.location.href="/ingredientBase/writeIngredientBases"+exportSdIds;
}
function showOpenColor() {
    $(".search").css("background","rgba(0, 0, 0, 0.68)");
    $(".search").css("overflow-y","scroll");
}
function setOpenColor() {
    $(".search").css("background","none");
    $(".search").css("overflow-y","hidden");
}
function loadMeteringUnit() {
    $.post("/meteringUnit/getAll",function(data){
        if(data.resultCode==100){
            $("#meteringName").html();
            $("#meteringName").append("<option value=''>请选择计量单位</option>");

            $.each(data.result,function (ind,val) {
                $("#meteringName").append("<option value='"+val.meteringName+"' data-siteId='"+val.sdId+"' data-subtitle='"+val.siteName+"' data-left='<img src="+val.sitePhoto+">'>"+val.meteringName+"</option>");
            })
            meteringNameArray = data.result;
        }
        $("#meteringName").selectator({
            labels: {
                search: '选择计量单位'
            },
            showAllOptionsOnFocus: true
        });
        $("#meteringName").next().css("width","100%");
        $("#meteringName").next().find(".selectator_input").css("background","#1b1b1b");

        $("#meteringName").prev().css("top","0px");
        $("#meteringName").prev().css("left","15.9em;")
    });

}
function loadSupper() {
    $.post("/ingredientSupp/getAll",function(data){
        if(data.resultCode==100){
            $("#suppId").html();
            $("#suppId").append("<option value=''>请选择供应商</option>");
            $.each(data.result,function (ind,val) {
                $("#suppId").append("<option value='"+val.id+"' data-siteId='"+val.sdId+"' data-subtitle='"+val.siteName+"' data-left='<img src="+val.sitePhoto+">'>"+val.supplierName+"</option>");
            })
            suppIdArray = data.result;
        }
        $("#suppId").selectator({
            labels: {
                search: '选择供应商'
            },
            showAllOptionsOnFocus: true
        });
        $("#suppId").next().css("width","100%");
        $("#suppId").next().find(".selectator_input").css("background","#1b1b1b");
    });

}
function loadWarehouse() {
    $.post("/ingredientWhouse/getAll",function(data){
        if(data.resultCode==100){
            $("#whouseId").html();
            $("#whouseId").append("<option value=''>请选择仓库</option>");
            $.each(data.result,function (ind,val) {
                $("#whouseId").append("<option value='"+val.id+"' data-siteId='"+val.sdId+"' data-subtitle='"+val.siteName+"' data-left='<img src="+val.sitePhoto+">'>"+val.whName+"</option>");
            })
            whouseIdArray = data.result;
        }
        $("#whouseId").selectator({
            labels: {
                search: '选择仓库'
            },
            showAllOptionsOnFocus: true
        });
        $("#whouseId").next().css("width","100%");
        $("#whouseId").next().find(".selectator_input").css("background","#1b1b1b");
    });
}
function doMyLogic(myResult) {
    $("#iconImg").val(myResult.result);
    $(".myImgFirst").attr("src",myResult.obj+myResult.result);
    $(".myImgFirst").css("display","block");
    $(".myImgSecond").css("display","none");
}
function chooseGGXHDWLi(dwStr) {
    var curMet = $("#meteringName").val();
    if(curMet!=undefined&&curMet!=null&&curMet!=''){
        $("#ggxhDWSpan").text(dwStr+"/"+curMet);
    }else{
        $("#ggxhDWSpan").text(dwStr+"/");
    }
    $("#ggxhdw").val(dwStr);

}
function myResetForm(){
    //清空权限数组
    rolesArray = [];
    $('#ingBaseForm').resetForm();
    $('#ingBaseForm').clearForm();
}