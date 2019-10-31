var tes = 0;
var isShow = true;
var allPage;
var allPageModel;

var limCount = 12;
var allDataTol;
var allDataTolModel;
var allDataTolModelPrint;

var initPageplugins = true;
var initPagepluginsModel = true;
var initPagepluginsModelPrint = true;

var curPag = 1;
var curPagModel = 1;
var curPagModelPrint = 1;

var chackIBIdArray = [];

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
    url: "/library/getLibraryList",
    type: "post",
    data:{page:curPag,limit:limCount},
    dataType: "json",
    timeout: 9000
};
var ajaxWDetailsSearchFormOption = {
    beforeSubmit: showWDetailsSearchRequest,
    success: showWDetailsSearchResponse,
    url: "/warehouseDetails/getByIngId",
    type: "post",
    data:{page:curPagModel,limit:limCount},
    dataType: "json",
    timeout: 9000
};
var ajaxPrintSearchFormOption = {
    beforeSubmit: showPrintSearchRequest,
    success: showPrintSearchResponse,
    url: "/warehouseDetails/getPrintQRCodeData",
    type: "post",
    data:{page:curPagModelPrint,limit:limCount},
    dataType: "json",
    timeout: 9000
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
        ajaxFormValidationURL:"/library/setLibrary",
        ajaxFormValidationMethod:"post",
        onBeforeAjaxFormValidation: showRequest,
        onAjaxFormComplete: showResponse,
        onFieldFailure:setErrorStyle
    });
    $("#detailsEditForm").validationEngine({
        binded:false,
        maxErrorsPerField:1,
        promptPosition: "topLeft:100",
        scroll:true,
        showOneMessage:true,
        validateNonVisibleFields:true,
        focusFirstField:true,
        ajaxFormValidation: true,
        ajaxFormValidationURL:"/libraryChange/addByDetails",
        ajaxFormValidationMethod:"post",
        onBeforeAjaxFormValidation: showEditDetailsRequest,
        onAjaxFormComplete: showEditDetailsResponse,
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

    $('#print-modal').on('hide.bs.modal', function () {
        chackIBIdArray = [];
    });
    /*$('#example').on('show.bs.modal', function () {
        alert('嘿，我听说您喜欢模态框...');
    })*/

});

function showSCJCXXSearchRequest(formData, jqForm, options){
    return true;
}
function showSCJCXXSearchResponse(data, status){
    initDateTableData(data);
}
function showWDetailsSearchRequest(formData, jqForm, options){
    return true;
}
function showPrintSearchRequest(formData, jqForm, options){
    return true;
}
function showWDetailsSearchResponse(data, status){
    initWDetailsData(data);
}
function showPrintSearchResponse(data, status){
    initPrintData(data);
}
function pageChange(i) {
    curPag = accAdd(i,1);
    loadUserList();
    Pagination.Page($($(".ht-page")[0]), i, allDataTol, limCount);
}
function pageChangeModel(i) {
    curPagModel = accAdd(i,1);
    loadWDetailsList();
    Pagination.Page($($(".ht-page")[1]), i, allDataTolModel, limCount);
}
function pageChangeModelPrint(i) {
    curPagModelPrint = accAdd(i,1);
    loadPrintList();
    Pagination.Page($($(".ht-page")[2]), i, allDataTolModelPrint, limCount);
}
function loadPrintList() {
    ajaxPrintSearchFormOption.data={page:curPagModelPrint,limit:limCount};
    $("#printForm").ajaxSubmit(ajaxPrintSearchFormOption);
    return false;
}
function loadWDetailsList(){
    ajaxWDetailsSearchFormOption.data={page:curPagModel,limit:limCount};
    $("#wDetailsForm").ajaxSubmit(ajaxWDetailsSearchFormOption);
    return false;
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

function showEditDetailsRequest(formData, jqForm, options){
    return true;
}
function showEditDetailsResponse(status, form,data, options){
    if(!isKickOut(data)){
        if(data.resultCode==100){
            loadUserList();
            loadWDetailsList();
            myDetailsEditResetForm();
            $("#detailsAddDel-modal").modal("hide");
            showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",data.msg);
        }else if(data.resultCode==101){
            showJqueryConfirmWindow("#icon-shangxin1","操作失败！深感抱歉，请联系管理人员为您加急处理");
        }else{
            showJqueryConfirmWindow("#icon-tishi1",data.msg);
        }
    }
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
}
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
                        var ingredientName = '';
                        if(val.ingredientName&&val.ingredientName!=''){
                            ingredientName = val.ingredientName;
                        }
                        var mainCategoryName = '';
                        if(val.mainCategoryName&&val.mainCategoryName!=''){
                            mainCategoryName = val.mainCategoryName;
                        }
                        var smallCategoryName = '';
                        if(val.smallCategoryName&&val.smallCategoryName!=''){
                            smallCategoryName = val.smallCategoryName;
                        }
                        var jdId=null;
                        if(val.id){
                            jdId = 'JD'+val.id;
                        }

                        if(ingredientName!=''){
                            comInfoTopStr = ingredientName;
                            if(mainCategoryName!=''){
                                comInfoTopStr = comInfoTopStr +"("+mainCategoryName;
                                if(smallCategoryName!=''){
                                    comInfoTopStr = comInfoTopStr +"-"+smallCategoryName+")";
                                }else{
                                    comInfoTopStr = comInfoTopStr +")";
                                }
                            }
                        }else{
                            if(mainCategoryName!=''){
                                comInfoTopStr = mainCategoryName;
                                if(smallCategoryName!=''){
                                    comInfoTopStr = comInfoTopStr +"-"+smallCategoryName;
                                }else{
                                    comInfoTopStr = comInfoTopStr;
                                }
                            }
                        }
                        comInfoTopStr = comInfoTopStr + '-'+jdId;
                        var ibImgPath = getManyPicture(val.scspImgPath,val.sitePhoto,val.siteName,val.picturePrefix);

                        var meteringName = '';
                        if(val.meteringName&&val.meteringName!=''){
                            meteringName = val.meteringName;
                        }
                        var bottomStr = '';
                        if(val.librarySurplus){
                            bottomStr = "库存存量:"+val.librarySurplus+meteringName;
                        }
                        if(bottomStr!=''){
                            if(val.libraryExpired){
                                bottomStr = bottomStr+",批次过期总数:"+val.libraryExpired+meteringName;
                            }
                        }else{
                            if(val.libraryExpired){
                                bottomStr = "批次过期总数:"+val.libraryExpired+meteringName;
                            }
                        }
                        if(bottomStr!=''){
                            if(val.librarySurplus||val.libraryExpired){
                                if(val.inventoryLimit){
                                    bottomStr = bottomStr+",库存阀值:"+val.inventoryLimit+meteringName;
                                }
                            }
                        }else{
                            if(val.inventoryLimit){
                                bottomStr = "库存阀值:"+val.inventoryLimit+meteringName;
                            }
                        }

                        if(bottomStr!=''){
                            var str = '';
                            if(val.librarySurplus||val.libraryExpired||val.inventoryLimit){
                                if(val.replenishmentStatus){
                                    switch(val.replenishmentStatus){
                                        case '1':
                                            str = "生产进货单(手动)";
                                            break;
                                        case '2':
                                            str = "补货正常";
                                            break;
                                    }
                                    bottomStr = bottomStr+","+str;
                                }
                            }
                        }else{
                            var str = '';
                            if(val.inventoryLimit){
                                switch(val.replenishmentStatus){
                                    case '1':
                                        str = "生产进货单(手动)";
                                        break;
                                    case '2':
                                        str = "补货正常";
                                        break;
                                }
                                bottomStr = str;
                            }
                        }

                        if(bottomStr!=''){
                            var str = '';
                            if(val.librarySurplus||val.libraryExpired||val.inventoryLimit||val.libraryStatus){
                                if(val.libraryStatus){
                                    switch(val.libraryStatus){
                                        case '1':
                                            str = "正常";
                                            break;
                                        case '2':
                                            str = "过期";
                                            break;
                                        case '3':
                                            str = "即将过期";
                                            break;
                                    }
                                    bottomStr = bottomStr+",存储"+str;
                                }
                            }
                        }else{
                            var str = '';
                            if(val.inventoryLimit){
                                switch(val.replenishmentStatus){
                                    case '1':
                                        str = "生产进货单(手动)";
                                        break;
                                    case '2':
                                        str = "补货正常";
                                        break;
                                }
                                bottomStr = "存储"+str;
                            }
                        }

                     $("#userListUl").append("<div class='col-md-3 col-sm-3 col-xs-12'>" +
                            "<div class='tpl-table-images-content' style='position: relative; overflow:hidden;'>" +
                         /*"<label class='btn-delete-item delete-label cursor-select'>" +
                         "<i class='delete-icon iconfont iconxuanze1'></i></label>"+*/
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
                            "<div class='tpl-i-font' style='height: 4.5em;' data-toggle2='tooltip' title='"+bottomStr+"' data-original-title='"+bottomStr+"'>" + bottomStr +
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
                            "<button type='button' class='am-btn am-btn-default am-btn-secondary' style='width: 50% !important;' onclick='updateRole("+val.id+","+val.sdId+","+val.ingredientBaseId+")'><i class='iconfont iconbianji3'></i> 明细内容</button>" +
                            "<button type='button' class='am-btn am-btn-default am-btn-danger' style='width: 50% !important;' onclick='delRole("+val.id+","+val.sdId+","+val.ingredientBaseId+")'><i class='iconfont iconshanchutishi'></i> 条码打印中心</button>" +
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
                    Pagination.init($($(".ht-page")[0]), pageChange);

                    /*
                    * 首次调用
                    * @param  object:翻页容器对象
                    * @param  number:当前页
                    * @param  number:总页数
                    * @param  number:每页数据条数
                    * */
                    Pagination.Page($($(".ht-page")[0]), 0, allDataTol, limCount);
                    initPageplugins = false;
                }else{
                    Pagination.Page($($(".ht-page")[0]), curPag-1, allDataTol, limCount);
                }
                $("#userListUl").viewer('update');
                $('#userListUl').viewer({
                    url: 'data-original',
                });

                $("[data-toggle='tooltip']").tooltip();
                $("[data-toggle2='tooltip']").tooltip({
                    placement:"top"
                });
            }else{
                showJqueryConfirmWindow("#icon-tishi1","未获取到食材库存信息");
            }
            isShow = true;
        }else if(data.resultCode==101){
            showJqueryConfirmWindow("#icon-shangxin1","获取食材库存信息异常！深感抱歉，请联系管理人员为您加急处理");
        }else{
            showJqueryConfirmWindow("#icon-tishi1",data.msg);
        }
    }
}
function initWDetailsData(data){
    if(!isKickOut(data)){
        if(data.resultCode==100){
            if(data.result!=null){
                $("#WDetailsListUl").html("");
                $.each(data.result,function (ind,val) {
                    if(val) {
                        var ingredientName = '';
                        if(val.ingredientName&&val.ingredientName!=''){
                            ingredientName = val.ingredientName;
                        }
                        var meteringName = '';
                        var mnUpd = '';
                        if(val.meteringName&&val.meteringName!=''){
                            meteringName = "("+val.meteringName+")";
                            mnUpd = val.meteringName;
                        }
                        var ratedTerm = '';
                        if(val.ratedTerm){
                            ratedTerm = val.ratedTerm;
                        }
                        var ratedTermDW = '';
                        if(val.ratedTermDW&&val.ratedTermDW!=''){
                            ratedTermDW = val.ratedTermDW;
                        }
                        var productionDate = '';
                        if(val.productionDate&&val.productionDate!=''){
                            productionDate = val.productionDate;
                        }
                        var warehouseCount = '';
                        if(val.warehouseCount){
                            warehouseCount = val.warehouseCount;
                        }
                        var unitPrice = '';
                        if(val.unitPrice){
                            unitPrice = val.unitPrice+"元";
                        }
                        var inventoryBalance = '';
                        if(val.inventoryBalance){
                            inventoryBalance = val.inventoryBalance;
                        }
                        //
                        var inventoryStatus = "";
                        var colorStyle= "";
                        switch (val.inventoryStatus){
                            case '1':
                                colorStyle = "#13ce66";
                                inventoryStatus = "存储良好";
                                break;
                            case '2':
                                inventoryStatus = "存储过期";
                                break;
                            case '3':
                                colorStyle = "yellow";
                                inventoryStatus = "即将过期";
                                break;
                        }
                        $("#WDetailsListUl").append("<tr>" +
                            /*"<td data-label='选择' style='text-align: left'>" +
                            "<input type='checkbox' class='pull-left list-check' value='"+val.id+"'/>" +
                            "</td>" +*/
                            "<td data-label='批次'>" +
                            "<small class='label label-danger' style='font-size: 90%'>"+val.batchNum+"</small>" +
                            "</td>" +
                            "<td data-label='食材'>" +
                            "<span class='handle'>"+ingredientName+meteringName+"</span>" +
                            "</td>" +
                            "<td data-label='存储质保'>" +
                            "<span class='handle'>"+ratedTerm+ratedTermDW+"</span>" +
                            "</td>" +
                            "<td data-label='标注生产日期'>" +
                            "<span class='handle'>"+productionDate+"</span>" +
                            "</td>" +
                            "<td data-label='批次入库量'>" +
                            "<span class='handle'>"+warehouseCount+meteringName+"</span>" +
                            "</td>" +
                            "<td data-label='单价(元)'>" +
                            "<span class='handle'>"+unitPrice+"</span>" +
                            "</td>" +
                            "<td data-label='批次现存量'>" +
                            "<span class='handle'>"+inventoryBalance+meteringName+"</span>" +
                            "</td>" +
                            "<td data-label='存储状态'>" +
                            "<small class='label label-danger' style='font-size: 90%; background-color: "+colorStyle+";'>"+inventoryStatus+"</small>" +
                            "</td>" +
                            "<td data-label='操作'>" +
                            "<div class='tools' style='color: #f56954;'>" +
                            "<i class='iconfont iconicon-test1' data-toggle='tooltip' title='货品存量增加' data-original-title='货品存量增加' style='cursor: pointer;font-size:1.3em;' onclick='addSubWDetails("+val.id+",0,"+val.sdId+","+val.ingredientId+","+"\""+mnUpd+"\")'></i>&nbsp;" +
                            "<i class='iconfont iconplus-minus' data-toggle='tooltip' title='货品存量减少' data-original-title='货品存量减少' style='cursor: pointer;font-size:1.3em;padding-left: 1em;' onclick='addSubWDetails("+val.id+",1,"+val.sdId+","+val.ingredientId+","+"\""+mnUpd+"\")'></i>" +
                            "</div>" +
                            "</td>" +
                            "</tr>");
                    }
                });
                allDataTolModel = data.total;
                if(initPagepluginsModel){
                    Pagination.init($($(".ht-page")[1]), pageChangeModel);
                    Pagination.Page($($(".ht-page")[1]), 0, allDataTolModel, limCount);
                    initPagepluginsModel = false;
                }else{
                    Pagination.Page($($(".ht-page")[1]), curPagModel-1, allDataTolModel, limCount);
                }
                /*$('.list-check').iCheck({
                    checkboxClass: 'icheckbox_minimal',
                    radioClass: 'iradio_minimal',
                    increaseArea: '20%'
                });
                $('.list-check').parent().css("left","36%");*/
                $("[data-toggle='tooltip']").tooltip();
                $("[data-toggle2odel='tooltip']").tooltip({
                    placement:"top"
                });
            }else{
                showJqueryConfirmWindow("#icon-tishi1","未获取到明细信息");
            }
        }else if(data.resultCode==101){
            showJqueryConfirmWindow("#icon-shangxin1","获取明细信息异常！深感抱歉，请联系管理人员为您加急处理");
        }else{
            showJqueryConfirmWindow("#icon-tishi1",data.msg);
        }
    }
}
function initPrintData(data) {
    if(!isKickOut(data)){
        if(data.resultCode==100){
            if(data.result!=null){
                $("#printListUl").html("");
                $.each(data.result,function (ind,val) {
                    if(val) {
                        var ingredientId = '';
                        if(val.ingredientId){
                            ingredientId = val.ingredientId;
                        }
                        var ingredientName = '';
                        if(val.ingredientName&&val.ingredientName!=''){
                            ingredientName = val.ingredientName;
                        }
                        var meteringName = '';
                        var mnUpd = '';
                        if(val.meteringName&&val.meteringName!=''){
                            meteringName = "("+val.meteringName+")";
                            mnUpd = val.meteringName;
                        }
                        var productionDate = '';
                        if(val.productionDate&&val.productionDate!=''){
                            productionDate = val.productionDate;
                        }
                        var inventoryBalance = '';
                        if(val.inventoryBalance){
                            inventoryBalance = val.inventoryBalance;
                        }
                        var s = "";
                        if(chackIBIdArray.length>0){
                            if($.inArray(val.productionDate,chackIBIdArray)!=-1){
                                s = "checked";
                            }
                        }
                        $("#printListUl").append("<tr>" +
                            "<td data-label='选择' style='text-align: left'>" +
                            "<input type='checkbox' class='pull-left list-check' name='chooseInput' "+s+" data-single='"+JSON.stringify(val)+"' value='"+val.productionDate+"'/>" +
                            "</td>" +
                            "<td data-label='食材编号'>" +
                            "<small class='label label-danger' style='font-size: 90%'>SC"+ingredientId+"</small>" +
                            "</td>" +
                            "<td data-label='食材'>" +
                            "<span class='handle'>"+ingredientName+meteringName+"</span>" +
                            "</td>" +
                            "<td data-label='标注生产日期'>" +
                            "<span class='handle'>"+productionDate+"</span>" +
                            "</td>" +
                            "<td data-label='批次现存量'>" +
                            "<span class='handle'>"+inventoryBalance+"</span>" +
                            "</td>" +
                            "<td data-label='计量单位'>" +
                            "<span class='handle'>"+mnUpd+"</span>" +
                            "</td>" +
                            "</tr>");
                    }
                });
                allDataTolModelPrint = data.total;

                if(initPagepluginsModelPrint){
                    Pagination.init($($(".ht-page")[2]), pageChangeModelPrint);
                    Pagination.Page($($(".ht-page")[2]), 0, allDataTolModelPrint, limCount);
                    initPagepluginsModelPrint = false;
                }else{
                    Pagination.Page($($(".ht-page")[2]), curPagModelPrint-1, allDataTolModelPrint, limCount);
                }
                $('.list-check').iCheck({
                    checkboxClass: 'icheckbox_minimal',
                    radioClass: 'iradio_minimal',
                    increaseArea: '20%'
                });
                $('.list-check').parent().css("left","36%");
                $("[data-toggle='tooltip']").tooltip();
                $("[data-toggle2odel='tooltip']").tooltip({
                    placement:"top"
                });

                $("input:checkbox[name='chooseInput']").on('ifChanged', function(event){
                    if($(this).is(':checked')){
                        chackIBIdArray.push($(this).val());
                    }else{
                        //inArray 查找下标，splice(1,2,3) 1要替换或删除的index,2要删除替换几个，3替换的元素（不传则删除）
                        chackIBIdArray.splice($.inArray($(this).val(),chackIBIdArray),1);
                    }
                });
            }else{
                showJqueryConfirmWindow("#icon-tishi1","未获取到信息");
            }
        }else if(data.resultCode==101){
            showJqueryConfirmWindow("#icon-shangxin1","获取信息异常！深感抱歉，请联系管理人员为您加急处理");
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
    $("#chgBusTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;新增食材库存信息");
    $("#comInfo-modal").modal("show");
}
function addSubWDetails(id,addOrSub,updSdId,updIBId,updMeterName) {
    //清空隐藏域id
    $("#detailsEditId").val("");
    $("#detailsEditSdId").val("");
    $("#changeMode").val("");
    $("#ingredientBaseId").val("");
    $("#changeBatch").val("");

    //重置清空form
    myDetailsEditResetForm();

    if(addOrSub==0){
        $("#addDelTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;核准货品存量增加");
        $("#changeMode").val(1);
        $("#changeNumber").prev().text("校准增量数量:");
        $("#changeNumber").attr("placeholder","校准增量数量(必填)");
        $("#changePrice").prev().text("校准增量数量:");
        $("#changePrice").attr("placeholder","校准增量价格(必填)");
    }else if(addOrSub==1){
        $.extend($.validationEngineLanguage.allRules,{ "myMax":{
                "func": function(field,rules,i,options){
                    if(accSub($("#bitchCount").val(),$("#changeNumber").val())<0){
                        return false;
                    }
                    return true;
                },
                "alertText": "校准减量不可大于结余数量，请修改！"
            }
        });

        $("#changeMode").val(2);
        $("#changeNumber").prev().text("校准减量数量:");
        $("#changeNumber").attr("placeholder","校准减量数量(必填,不可大于结余数量)");
        $("#changePrice").prev().text("校准减量价格:");
        $("#changePrice").attr("placeholder","校准减量价格(必填)");
        $("#addDelTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;核准货品存量减少");
    }
    $("#bitchCount").next().text(updMeterName);
    $("#changeNumber").next().find("span:eq(0)").text(updMeterName);
    $("#changeBatch").val(id);

    $("#ingredientBaseId").val(updIBId);

    //isNaN是数字返回false
    if(id!=null && !isNaN(id)){
        $.post("/warehouseDetails/getUpdWarehouseDetails",{"id":id,"sdId":updSdId},function(data){
            if(!isKickOut(data)){
                //没有被踢出
                if(data.resultCode==100){
                    $("#detailsEditSdId").val(data.result.sdId);
                    $("#bitchCount").val(data.result.inventoryBalance);
                    $("#changePrice").val(data.result.unitPrice);
                    $("#detailsAddDel-modal").modal("show");
                }else if(data.resultCode==101){
                    showJqueryConfirmWindow("#icon-shangxin1","获取库存明细异常！深感抱歉，请联系管理人员为您加急处理");
                }else{
                    showJqueryConfirmWindow("#icon-tishi1",data.msg);
                }
            }
        });
    }else{
        showJqueryConfirmWindow("#icon-tishi1","主键获取异常！深感抱歉，请联系管理人员为您加急处理");
    }
}
function delRole(id,updSdId,ingredientId) {
    //重置清空form
    $("#printSdId").val(updSdId);
    $("#printId").val(ingredientId);
    curPagModelPrint = 1;
    $("#printTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;打印二维码");
    chackIBIdArray = [];
    loadPrintList();
    $("#print-modal").modal("show");
}
function updateRole(id,delSdId,ingredientId) {
    //重置清空form
    $("#wDetailsSdId").val(delSdId);
    $("#iBId").val(ingredientId);
    curPagModel = 1;
    $("#detailsTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;食材存储明细");
    loadWDetailsList();
    $("#details-modal").modal("show");
}

function exportSingle() {
    window.location.href="/ingredientBase/writeIngredientBases";
}
function showOpenColor() {
    $(".search").css("background","rgba(0, 0, 0, 0.68)");
    $(".search").css("overflow-y","scroll");
}
function setOpenColor() {
    $(".search").css("background","none");
    $(".search").css("overflow-y","hidden");
}

function printData() {
    $.each(chackIBIdArray,function (ind,val) {
        var jsonStr = $("input[value='"+val+"']").attr("data-single");
        console.log(JSON.parse(jsonStr));
        CreatePrintPage(JSON.parse(jsonStr));
        LODOP.PRINT();
    });
}
function CreatePrintPage(obj) {
    LODOP=getLodop(document.getElementById('LODOP1'),document.getElementById('LODOP_EM1'));
    var obstr = obj.sdId+','+obj.ingredientId+','+obj.productionDate+','+obj.ingredientName+','+obj.meteringName;
    LODOP.ADD_PRINT_BARCODE(40,35,126,180,"QRCode",obstr);
    LODOP.ADD_PRINT_TEXT(8,50,106,27,obj.ingredientName);
    LODOP.SET_PRINT_STYLEA(2,"FontSize",14);
    var scrq = obj.productionDate;
    LODOP.ADD_PRINT_TEXT(150,10,195,35,"生产日期:"+scrq);
    LODOP.SET_PRINT_STYLEA(3,"FontSize",12);
    LODOP.ADD_PRINT_TEXT(170,10,195,35,"计量单位:"+obj.meteringName);
    LODOP.SET_PRINT_STYLEA(4,"FontSize",12);
    LODOP.ADD_PRINT_TEXT(190,10,195,35,"当前存量:"+obj.inventoryBalance+" 袋");
    LODOP.SET_PRINT_STYLEA(5,"FontSize",12);
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
function myDetailsEditResetForm(){
    //清空权限数组
    $("#detailsEditId").val("");
    $("#detailsEditSdId").val("");
    $("#changeMode").val("");
    $("#ingredientBaseId").val("");
    $("#changeBatch").val("");


    $("#changeNumber").val("");
    $("#changePrice").val("");
    $("#changeRemarks").val("");
    /*$('#detailsEditForm').resetForm();
    $('#detailsEditForm').clearForm();*/
}