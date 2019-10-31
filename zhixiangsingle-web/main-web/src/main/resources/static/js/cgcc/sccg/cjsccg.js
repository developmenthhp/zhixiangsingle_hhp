var isShow = true;
var sdId;
var isZx;

var isManySite = false;

var currentIndex = 1;

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

    loadIngredientBase();

    //为用户加站点角色的时候需要判断角色为长度为1时，需要判断设置的角色站点id是否和
    $("#temForm").validationEngine({
        binded:false,
        maxErrorsPerField:1,
        promptPosition: "topLeft:100",
        scroll:true,
        showOneMessage:true,
        validateNonVisibleFields:true,
        focusFirstField:true,
        ajaxFormValidation: true,
        ajaxFormValidationURL:"#",
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

function showRequest(formData, jqForm, options){
    return true;
}
function showResponse(status, form,data, options){
    var curIndex = $("#curIndex").val();
    var advicePurchCount = $("#advicePurchCount").val();
    if(advicePurchCount||advicePurchCount==''){
        advicePurchCount = 0;
    }
    var whouseCount = $("#whouseCount").val();
    if(whouseCount||whouseCount==''){
        whouseCount = 0;
    }
    var warehouseCount = $("#warehouseCount").val();
    if(warehouseCount||warehouseCount==''){
        warehouseCount = 0;
    }

    if(curIndex&&curIndex!=''){
        $("#tr"+curIndex).find("td:eq(1)").find("span").attr("data-id",$("#ingredientId").val());
        $("#tr"+curIndex).find("td:eq(1)").find("span").text($("#ingredientId").find("option:selected").text());

        $("#tr"+curIndex).find("td:eq(2)").find("span").attr("data-count",$("#advicePurchCount").val());
        $("#tr"+curIndex).find("td:eq(2)").find("span").text(advicePurchCount+$("#meteringName").text());

        $("#tr"+curIndex).find("td:eq(3)").find("span").attr("data-count",$("#whouseCount").val());
        $("#tr"+curIndex).find("td:eq(3)").find("span").text(whouseCount+$("#meteringName").text());

        $("#tr"+curIndex).find("td:eq(4)").find("span").attr("data-count",$("#warehouseCount").val());
        $("#tr"+curIndex).find("td:eq(4)").find("span").text(warehouseCount+$("#meteringName").text());

        $("#tr"+curIndex).find("td:eq(5)").find("span").attr("data-count",$("#purchCount").val());
        $("#tr"+curIndex).find("td:eq(5)").find("span").text($("#purchCount").val()+$("#meteringName").text());

        $("#tr"+curIndex).find("td:eq(6)").find("span").attr("data-count",$("#lowestUnitPrice").val());
        $("#tr"+curIndex).find("td:eq(6)").find("span").text($("#lowestUnitPrice").val()+"元");

        $("#tr"+curIndex).find("td:eq(7)").find("span").attr("data-count",$("#highestUnitPrice").val());
        $("#tr"+curIndex).find("td:eq(7)").find("span").text($("#highestUnitPrice").val()+"元");
    }else{
        var trNode = '<tr id="tr'+currentIndex+'">' +
            '<td data-label="编号">' +
            '<span class="handle">'+currentIndex+'</span>' +
            '</td>' +
            '<td data-label="食材">' +
            '<span class="handle" data-id="'+$("#ingredientId").val()+'">'+$("#ingredientId").find("option:selected").text()+'</span>' +
            '</td>' +
            '<td data-label="建议采购量">' +
            '<span class="handle" data-count="'+$("#advicePurchCount").val()+'">'+advicePurchCount+$("#meteringName").text()+'</span>' +
            '</td>' +
            '<td data-label="库存数量(已减安全量)">' +
            '<span class="handle" data-count="'+$("#whouseCount").val()+'">'+whouseCount+$("#meteringName").text()+'</span>' +
            '</td>' +
            '<td data-label="需求采购量">' +
            '<span class="handle" data-count="'+$("#warehouseCount").val()+'">'+warehouseCount+$("#meteringName").text()+'</span>' +
            '</td>' +
            '<td data-label="实际需求采购量">' +
            '<span class="handle" data-count="'+$("#purchCount").val()+'">'+$("#purchCount").val()+$("#meteringName").text()+'</span>' +
            '</td>' +
            '<td data-label="预算最低价">' +
            '<span class="handle" data-count="'+$("#lowestUnitPrice").val()+'">'+$("#lowestUnitPrice").val()+'元</span>' +
            '</td>' +
            '<td data-label="预算最单价">' +
            '<span class="handle" data-count="'+$("#highestUnitPrice").val()+'">'+$("#highestUnitPrice").val()+'元</span>' +
            '</td>' +
            '<td data-label="操作">' +
            '<div class="tools">' +
            '<i class="iconfont iconbianji3" data-toggle="tooltip" title="更改" data-original-title="更改" style="cursor: pointer;font-size:1.3em;color: rgb(245, 105, 84);" onclick="showCreatModal('+currentIndex+')"></i>' +
            '<i class="iconfont iconshanchutishi" data-toggle="tooltip" title="删除" data-original-title="删除" style="cursor: pointer;font-size:1.3em;padding-left: 1em;color: rgb(245, 105, 84);" onclick="delTr('+currentIndex+')"></i>' +
            '</div>' +
            '</td>' +
            '</tr>';
        $("#userListUl").append(trNode);
        currentIndex = accAdd(currentIndex,1);
    }
    $("#addEdit-modal").modal("hide");
}
function setErrorStyle() {
    setTimeout(function () {
        $("#ingredientId").prev().css("top","0px");
        $("#ingredientId").prev().css("left","15.9em");
    },1000);
}
function showCreatModal(curIndex) {
    myResetForm();
    if(curIndex!=null){
        $("#curIndex").val(curIndex);
        $("#lpTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;更新数据");
        var ibId = $("#tr"+curIndex).find("td:eq(1)").find("span").attr("data-id");
        var advicePurchCount = $("#tr"+curIndex).find("td:eq(2)").find("span").attr("data-count");
        var whouseCount = $("#tr"+curIndex).find("td:eq(3)").find("span").attr("data-count");
        var warehouseCount = $("#tr"+curIndex).find("td:eq(4)").find("span").attr("data-count");
        var purchCount = $("#tr"+curIndex).find("td:eq(5)").find("span").attr("data-count");
        var lowestUnitPrice = $("#tr"+curIndex).find("td:eq(6)").find("span").attr("data-count");
        var highestUnitPrice = $("#tr"+curIndex).find("td:eq(7)").find("span").attr("data-count");
        $("#ingredientId").val(ibId);
        $('#ingredientId').selectator('refresh');

        var meteringName = $('#ingredientId').find("option:selected").attr("data-metering");
        if(meteringName&&meteringName!=''){
            $(".myMN").text(meteringName);
        }

        $("#advicePurchCount").val(advicePurchCount);
        $("#whouseCount").val(whouseCount);
        $("#warehouseCount").val(warehouseCount);
        $("#purchCount").val(purchCount);
        $("#lowestUnitPrice").val(lowestUnitPrice);
        $("#highestUnitPrice").val(highestUnitPrice);
    }else{
        $("#lpTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;添加采购记录");
    }
    $("#addEdit-modal").modal("show");
}
function loadIngredientBase() {
    $.post("/ingredientBase/getAll",function(data){
        if(!isKickOut(data)){
            //没有被踢出
            if(data.resultCode==100){
                $("#ingredientId").html("");
                $.each(data.result,function (ind,val) {
                    var leftImg = '';
                    if(val.scspImgPath	&&val.scspImgPath!=''){
                        var allImg = val.scspImgPath.split(",");
                        $.each(allImg,function (ind2,val2) {
                            if(val2.indexOf("http:")!=-1){
                                leftImg = val2;
                            }else{
                                leftImg = val.picturePrefix+val2;
                            }
                        });
                    }else{
                        leftImg = val.sitePhoto;
                    }
                    $("#ingredientId").append("<option value='"+val.id+"' data-total='"+val.total+"' data-price='"+val.unitPrice+"' data-metering='"+val.meteringName+"' data-siteId='"+val.sdId+"' data-subtitle='"+val.siteName+"' data-left='<img src="+leftImg+">'>"+val.ingredientName+"("+val.meteringName+")</option>");
                });
                $("#ingredientId").selectator({
                    labels: {
                        search: '选择食材'
                    },
                    showAllOptionsOnFocus: true
                });
                $("#ingredientId").next().css("width","100%");
                $("#ingredientId").next().find(".selectator_input").css("background","#1b1b1b");
            }
        }
    });
}

function delTr(curIndex) {
    $("#tr"+curIndex).remove();
}
function resetData(curNode) {
    var total = $(curNode).find("option:selected").attr("data-total");
    var unitPrice = $(curNode).find("option:selected").attr("data-price");
    var meteringName = $(curNode).find("option:selected").attr("data-metering");
    if(total&&total!=''&&total!='null'){
        $("#whouseCount").val(total);
    }
    if(unitPrice&&unitPrice!=''&&unitPrice!='null'){
        $("#lowestUnitPrice").val(unitPrice);
    }
    if(meteringName&&meteringName!=''){
        $(".myMN").text(meteringName);
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
    window.location.href="/libraryPurchase/getCreatePage";
}
function myResetForm(){
    //清空权限数组
    $("#ingredientId").val('');
    $('#ingredientId').selectator('refresh');
    $(".myMN").text("");
    $("#curIndex").val('');
    $('#temForm').resetForm();
    $('#temForm').clearForm();
}