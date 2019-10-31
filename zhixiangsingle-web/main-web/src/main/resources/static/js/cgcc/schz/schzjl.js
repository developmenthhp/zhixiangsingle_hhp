var isShow = true;
var allPage;

var limCount = 12;
var allDataTol;

var initPageplugins = true;

var curPag = 1;

var sdId;
var isZx;

var mainCategoryIdArray = [];
var smallArray = [];

var isManySite = false;

var lpJson = {};

var ajaxSCJCXXSearchFormOption = {
    beforeSubmit: showSCJCXXSearchRequest,
    success: showSCJCXXSearchResponse,
    url: "/libraryChange/getLibraryChangeList",
    type: "post",
    data:{page:curPag,limit:limCount},
    dataType: "json",
    timeout: 12000
}

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

    loadClassification();

    $("#smallCatogory").selectator({
        labels: {
            search: '选择二级分类'
        },
        showAllOptionsOnFocus: true
    });
    $("#smallCatogory").next().css("width","100%");
    $("#smallCatogory").next().find(".selectator_input").css("background","#1b1b1b");

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
        ajaxFormValidationURL:"/libraryChange/setMorningMeeting",
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

    //初始化时间选择插件
    $("#dtBox").DateTimePicker({
        language:'zh-CN',
        defaultDate: new Date(),
        animationDuration:200,
        buttonsToDisplay: [ "SetButton", "ClearButton"],
        clearButtonContent: "取消"

    });
});

function showSCJCXXSearchRequest(formData, jqForm, options){
    return true;
}
function showSCJCXXSearchResponse(data, status){
    initDateTableData(data);
}
function setErrorStyle() {
    setTimeout(function () {
        /*$("#ingredientId").prev().css("top","0px");
        $("#ingredientId").prev().css("left","15.9em");*/
    },1000);
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
    $("#snapPicture").val(img);

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
                        var cgId = '';
                        var cgIdStr = '';
                        if(val.id){
                            cgId = 'CG'+val.id+"-";
                            cgIdStr = 'CG'+val.id;
                        }
                        var ingredientName = '';
                        if(val.ingredientName&&val.ingredientName!=''){
                            ingredientName = "采购"+val.ingredientName;
                        }

                        var mainCategoryName = '';
                        if(val.mainCategoryName&&val.mainCategoryName!=''){
                            mainCategoryName = val.mainCategoryName;
                        }
                        var smallCategoryName = '';
                        if(val.smallCategoryName&&val.smallCategoryName!=''){
                            smallCategoryName = val.smallCategoryName;
                        }
                        var cName = '';
                        if(mainCategoryName!=''&&smallCategoryName!=''){
                            cName = "("+mainCategoryName+"-"+smallCategoryName+")";
                        }else if(mainCategoryName==''&&smallCategoryName==''){

                        }else if(smallCategoryName==''){
                            cName = "("+mainCategoryName+")";
                        }else if(mainCategoryName==''){
                            cName = "("+smallCategoryName+")";
                        }
                        var purchCount = '';
                        if(val.purchCount){
                            if(ingredientName == ''){
                                purchCount = "采购"+val.purchCount;
                            }else{
                                purchCount = val.purchCount;
                            }
                        }
                        var meteringName = '';
                        if(val.meteringName&&val.meteringName!=''){
                            meteringName = val.meteringName;
                        }
                        var lowestUnitPrice = '';
                        if(val.lowestUnitPrice){
                            lowestUnitPrice = val.lowestUnitPrice;
                        }
                        var highestUnitPrice = '';
                        if(val.highestUnitPrice){
                            highestUnitPrice = val.highestUnitPrice;
                        }
                        var price = '';
                        if(lowestUnitPrice!=''&&highestUnitPrice!=''){
                            price = "-预算价格在"+lowestUnitPrice+"元~"+highestUnitPrice+"元波动";
                        }else if(lowestUnitPrice==''&&highestUnitPrice=='') {

                        }else if(lowestUnitPrice==''){
                            price = "-预算价格不超过"+highestUnitPrice+"元";
                        }else if(highestUnitPrice==''){
                            price = "-预算价格不低于"+lowestUnitPrice+"元";
                        }
                        var authorizedPrice = '';
                        if(val.authorizedPrice){
                            authorizedPrice = "，经核准,价格为"+val.authorizedPrice+"元";
                        }
                        comInfoTopStr = cgId+ingredientName+cName+purchCount+meteringName+price+authorizedPrice;
                        var purchPeople = '';
                        if(val.purchPeople&&val.purchPeople!=''){
                            purchPeople = "由"+val.purchPeople+"于";
                        }
                        var purchTime = '';
                        if(val.purchTime&&val.purchTime!=''){
                            purchTime = val.purchTime;
                        }
                        var purchStatus = '';
                        var editBtn = '';
                        switch (val.purchStatus) {
                            case '1':
                                purchStatus = "已制单";
                                editBtn = "<button type='button' class='am-btn am-btn-default am-btn-warning' style='width: 100% !important;' onclick='delRole("+val.id+","+val.sdId+","+val.purchStatus+","+"\""+cgIdStr+"\")'><i class='iconfont iconbianji3'></i> 作废</button>";
                                break;
                            case '2':
                                purchStatus = "已配送";
                                break;
                            case '3':
                                purchStatus = "已入库";
                                break;
                            case '4':
                                purchStatus = "已作废";
                                editBtn = "<button type='button' class='am-btn am-btn-default am-btn-danger' style='width: 100% !important;' onclick='delRole("+val.id+","+val.sdId+","+val.purchStatus+","+"\""+cgIdStr+"\")'><i class='iconfont iconshanchutishi'></i> 删除</button>";
                                break;
                        }
                        var bottomStr = purchPeople+purchTime+purchStatus;
                        var ibImgPath = getManyPicture(val.scspImgPath,val.sitePhoto,val.siteName,val.picturePrefix);
                        var indexData = val.sdId+"-"+val.id;
                        var s = "";
                        if(lpJson[val.sdId]!=undefined&&lpJson[val.sdId]!=null&&lpJson[val.sdId]!=''){
                            if(lpJson[val.sdId].name!=undefined&&lpJson[val.sdId].name!=null&&lpJson[val.sdId].name!=''){
                                var curIds = lpJson[val.sdId].name.split(",");
                                if($.inArray(val.id+"",curIds)!=-1){
                                    s = "block";
                                }else{
                                    s = "none";
                                }
                            }else{
                                s = "none";
                            }
                        }else{
                            s = "none";
                        }
                        /*if(chackIBIdArray.length>0){
                            if($.inArray(indexData,chackIBIdArray)!=-1){
                                s = "block";
                            }else{
                                s = "none";
                            }
                        }else{
                            s = "none";
                        }*/
                        $("#userListUl").append("<div class='col-md-3 col-sm-3 col-xs-12' onclick='checkPurchase(this)'>" +
                            "<div class='tpl-table-images-content' style='position: relative;overflow: hidden;'>" +
                            "<label class='btn-delete-item delete-label cursor-select' data-single='"+val.id+"-"+val.sdId+"-"+val.purchCount+"-"+val.purchStatus+"' data-id='"+val.sdId+"-"+val.id+"' style='display: "+s+";'>" +
                         "<i class='delete-icon iconfont iconxuanze1'></i></label>"+
                            "<div class='tpl-table-images-content-i-time-hhp' style='height: 4.4em;-webkit-line-clamp:3;' data-toggle2='tooltip' title='"+comInfoTopStr+"' data-original-title='"+comInfoTopStr+"'>"+comInfoTopStr+"</div>" +
                            /*"<div class='tpl-i-title-hhpHeight' style='height: 6.5em;' data-toggle='tooltip' title='' data-original-title=''>" +
                            /!*"<div>"+registration+"</div>"+
                            "<div>"+legalRepresentative+"</div>"+
                            "<div>"+topStr+"</div>"+
                            "<div>"+address+"</div>"+
                            "<div>"+deadline+"</div>"+*!/
                            "</div>" +*/

                            ibImgPath+
                            "<div class='tpl-table-images-content-block'>" +
                            "<div class='tpl-i-font' style='height: 3em;' data-toggle2='tooltip' title='"+bottomStr+"' data-original-title='"+bottomStr+"'>" + bottomStr +
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
                            editBtn +
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
                showJqueryConfirmWindow("#icon-tishi1","未获取到采购信息");
            }
            isShow = true;
        }else if(data.resultCode==101){
            showJqueryConfirmWindow("#icon-shangxin1","获取采购信息异常！深感抱歉，请联系管理人员为您加急处理");
        }else{
            showJqueryConfirmWindow("#icon-tishi1",data.msg);
        }
    }
}
function delRole(id,delSdId,editOrDel,name) {
    if(null!=id&&id!=""){
        var url = '';
        var contentMsg = '';
        var resultMsg = '';
        if(editOrDel=='1'){
            url = "/libraryChange/setLibraryPurchase";
            contentMsg = "您确定要作废"+name+"单吗？";
            resultMsg = "作废";
        }else if(editOrDel=='4'){
            url = "/libraryChange/delLibraryPurchase";
            contentMsg = "您确定要删除"+name+"单吗？";
            resultMsg = "删除";
        }
        $.confirm({
            icon: '#icon-tishi1',
            theme: 'modern',
            title: '提示',
            content: contentMsg,
            animation: 'news',//动画
            closeAnimation: 'news',//关闭动画
            /*autoClose: 'showMyMsg2|3000',*/
            buttons: {
                showMyMsg2: {
                    text: '确定',
                    action: function () {
                        $.post(url,{"id":id,"sdId":delSdId,"purchStatus":4},function(data){
                            if(!isKickOut(data)){
                                //没有被踢出
                                if(data.resultCode==100){
                                    //刷新数据
                                    loadUserList();
                                    showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",resultMsg+name+'单成功！');
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
function exportSingle() {
    if($.isEmptyObject(lpJson)){
        showJqueryConfirmWindow("#icon-tishi1","请至少选择一条记录导出");
        return;
    }
    var purchPeople = '';
    $.each(lpJson,function (ind,val) {
        if(purchPeople==""){
            purchPeople = ind+"_"+val.name;
        }else{
            purchPeople = purchPeople+"-"+ind+"_"+val.name;
        }
    });
    window.location.href="/libraryChange/writeLibraryPurchases?purchPeople="+purchPeople;
    lpJson = {};
    //刷新数据
    loadUserList();
}
function showOpenColor() {
    $(".search").css("background","rgba(0, 0, 0, 0.68)");
    $(".search").css("overflow-y","scroll");
}
function setOpenColor() {
    $(".search").css("background","none");
    $(".search").css("overflow-y","hidden");
}
function checkPurchase(curNode) {
    var dataDisplay = $(curNode).children("div:first-child").find("label:eq(0)").css("display");
    var dataId = $(curNode).children("div:first-child").find("label:eq(0)").attr("data-id");
    var dataStr = $(curNode).children("div:first-child").find("label:eq(0)").attr("data-single");
    var dataArr = dataStr.split("-");
    if(dataArr[3]=="4"){
        showJqueryConfirmWindow("#icon-tishi1","该单已作废，请选择其他采购单");
        return;
    }
    if(dataDisplay=="none"){
        $(curNode).children("div:first-child").find("label:eq(0)").css("display","block");
        if(lpJson[dataArr[1]]!=undefined&&lpJson[dataArr[1]]!=null&&lpJson[dataArr[1]]!=''){
            if(lpJson[dataArr[1]].name!=undefined&&lpJson[dataArr[1]].name!=null&&lpJson[dataArr[1]].name!=''){
                lpJson[dataArr[1]].name = lpJson[dataArr[1]].name +","+ dataArr[0];
            }else{
                lpJson[dataArr[1]].name = dataArr[0];
            }
        }else{
            lpJson[dataArr[1]] = {
                name : dataArr[0]
            }
        }
        //chackIBIdArray.push(dataId);
    }else if(dataDisplay=="block"){
        //inArray 查找下标，splice(1,2,3) 1要替换或删除的index,2要删除替换几个，3替换的元素（不传则删除）
        //chackIBIdArray.splice($.inArray(dataId,chackIBIdArray),1);
        var curIds = lpJson[dataArr[1]].name.split(",");
        curIds.splice($.inArray(dataArr[0],curIds),1);
        if(curIds.length==0){
            delete lpJson[dataArr[1]];
        }else{
            lpJson[dataArr[1]].name = curIds.toString();
        }
        $(curNode).children("div:first-child").find("label:eq(0)").css("display","none");
    }
}
function changeLibraryPruchase() {
    if($.isEmptyObject(lpJson)){
        showJqueryConfirmWindow("#icon-tishi1","请至少选择一条记录转换为入库单");
        return;
    }
    $.post("/iwareCount/transLPurchase",{"wareIds":JSON.stringify(lpJson)},function(data){
        if(!isKickOut(data)){
            //没有被踢出
            if(data.resultCode==100){
                lpJson = {};
                //刷新数据
                loadUserList();
                showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",'转换入库单成功！');
            }else if(data.resultCode==101){
                showJqueryConfirmWindow("#icon-shangxin1","操作失败！深感抱歉，请联系管理人员为您加急处理");
            }else{
                showJqueryConfirmWindow("#icon-tishi1",data.msg);
            }
        }
    });
}
function chosePriceExcel() {
    $.confirm({
        icon: '#icon-tishi1',
        theme: 'modern',
        title: '提示',
        content: '您确定要导入当前EXCEL内的核准单价吗?',
        animation: 'news',//动画
        closeAnimation: 'news',//关闭动画
        /*autoClose: 'showMyMsg2|3000',*/
        buttons: {
            showMyMsg2: {
                text: '确定',
                action: function () {
                    $.post(url,{"id":id,"sdId":delSdId,"purchStatus":4},function(data){
                        if(!isKickOut(data)){
                            //没有被踢出
                            if(data.resultCode==100){
                                //刷新数据
                                loadUserList();
                                showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",resultMsg+name+'单成功！');
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
function getCreatPage() {
    window.location.href="/libraryChange/getCreatePage";
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