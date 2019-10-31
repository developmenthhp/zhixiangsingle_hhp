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
    url: "/categoryBase/getCategoryBaseList",
    type: "post",
    data:{},
    dataType: "json",
    timeout: 3000
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
        ajaxFormValidationURL:"/categoryBase/setCategoryBase",
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
                $.each(data.result,function (allInd,allRows) {
                    if(allRows&&allRows.rows!=undefined&&allRows.rows!=null&&allRows.rows.length>0){
                        var tab = '<table id="sort'+allInd+'" class="my-table">' +
                            '<thead>' +
                            '<tr>' +
                            '<td id="topSite'+allInd+'" colspan="2" style="color:red"></td>'+
                            '</tr>'+
                            '<tr>' +
                            '<th><i class="iconfont iconzhanghuquanxian"></i>&nbsp;类别名称</th>' +
                            '<th><i class="fa fa-link"></i>&nbsp;操作</th></tr><thead>' +
                            '<tbody id="data'+allInd+'"></tbody></table>';

                        $("#userListUl").append("<div class='col-md-12 col-sm-12 col-xs-12'>" +
                            "<div class='tpl-table-images-content'>" +tab+
                            "</div>");
                        $.each(allRows.rows,function(ind,val){
                            if (val.pId == 0) {
                                $("#topSite"+allInd).text(val.siteName);
                                $("#data"+allInd).append("<tr id='"+val.id+"-myStyleLi-"+val.sdId+"'>" +
                                    "<td data-label='类别名称' style='text-align: left;'>" +
                                    "<small class='label label-danger' style='font-size: 90%'>"+val.categoryName+"</small>" +
                                    "</td>" +
                                    "<td data-label='操作'>" +
                                    "<div class='tools' style='color: #f56954;display: block'>" +
                                    "<i class='fa fa-plus' data-toggle='tooltip' title='添加子节点' data-original-title='添加子节点' style='cursor: pointer;font-size:1.3em;' onclick='addSun("+val.id+",1,"+val.sdId+")'></i>&nbsp;" +
                                    "<i class='fa fa-edit' data-toggle='tooltip' title='编辑' data-original-title='编辑' style='cursor: pointer;font-size:1.3em;padding-left: 1em;' onclick='editPerm("+val.id+",0,"+val.sdId+")'></i>&nbsp;" +
                                    "<i class='fa fa-trash-o' data-toggle='tooltip' title='删除' data-original-title='删除' style='cursor: pointer;font-size:1.3em;padding-left: 1em;' onclick='delPerm("+val.id+","+val.sdId+",\""+val.categoryName+"\")'></i>" +
                                    "</div>" +
                                    "</td>" +
                                    "</tr>");
                                var sonArray = getParentArry(val.id,allRows.rows);
                                if(sonArray.length>0){
                                    //给这个tr加一个图标
                                    $("#"+val.id+"-myStyleLi-"+val.sdId).find("td:eq(0) small").before("<i class='fa fa-caret-down' data-widget='collapse' style='cursor: pointer' onclick='showOrHideSun("+val.id+","+val.sdId+")'></i>&nbsp;");
                                    $.each(sonArray,function (ind2,val2) {
                                        $("#"+val.id+"-myStyleLi-"+val.sdId).after("<tr id='"+val2.id+"-myStyleLi-"+val2.sdId+"' class='box-body"+val.id+"-"+val.sdId+"'>" +
                                            "<td data-label='类别名称' style='text-align: left;padding-left: 3%;'>" +
                                            "<small class='label label-danger' style='font-size: 90%'>"+val2.categoryName+"</small>" +
                                            "</td>" +
                                            "<td data-label='操作'>" +
                                            "<div class='tools' data-label='"+val2.id+"' style='color: #f56954;display: block'>" +
                                            "<i class='fa fa-plus' data-toggle='tooltip' title='添加子节点' data-original-title='添加子节点' style='cursor: pointer;font-size:1.3em;' onclick='addSun("+val2.id+",1,"+val2.sdId+")'></i>&nbsp;" +
                                            "<i class='fa fa-edit' data-toggle='tooltip' title='编辑' data-original-title='编辑' style='cursor: pointer;font-size:1.3em;padding-left: 1em;' onclick='editPerm("+val2.id+",0,"+val2.sdId+")'></i>&nbsp;" +
                                            "<i class='fa fa-trash-o' data-toggle='tooltip' title='删除' data-original-title='删除' style='cursor: pointer;font-size:1.3em;padding-left: 1em;' onclick='delPerm("+val2.id+","+val2.sdId+",\""+val2.categoryName+"\")'></i>" +
                                            "</div>" +
                                            "</td>" +
                                            "</tr>");
                                        var sonArray2 = getParentArry(val2.id,allRows.rows);
                                        if(sonArray2.length>0){
                                            var gParent = $("#"+val2.id+"-myStyleLi-"+val2.sdId).attr('class');
                                            $("#"+val2.id+"-myStyleLi-"+val2.sdId).find("td:eq(0) small").css("padding-left","0");
                                            //给这个tr加一个图标
                                            $("#"+val2.id+"-myStyleLi-"+val2.sdId).find("td:eq(0) small").before("<i class='fa fa-caret-down' style='cursor: pointer;' onclick='showOrHideSun("+val2.pId+","+val2.sdId+")'></i>&nbsp;");
                                            $.each(sonArray2,function (ind3,val3) {
                                                if(val2.pId!=0){
                                                    $("#"+val2.id+"-myStyleLi-"+val2.sdId).after("<tr id='"+val3.id+"-myStyleLi-"+val3.sdId+"' class='"+gParent+" box-body"+val2.id+"-"+val2.sdId+"'>" +
                                                        "<td data-label='类别名称' style='text-align: left;padding-left: 5%;'>" +
                                                        "<small class='label label-danger' style='font-size: 90%'>"+val3.categoryName+"</small>" +
                                                        "</td>" +
                                                        "<td data-label='操作'>" +
                                                        "<div class='tools' data-label='"+val3.id+"' style='color: #f56954;display: block'>" +
                                                        /*"<i class='fa fa-plus' data-toggle='tooltip' title='添加子节点' data-original-title='添加子节点' style='cursor: pointer;font-size:1.3em;' onclick='addSun("+val3.id+",1,"+val3.sdId+")'></i>&nbsp;" +*/
                                                        "<i class='fa fa-edit' data-toggle='tooltip' title='编辑' data-original-title='编辑' style='cursor: pointer;font-size:1.3em;padding-left: 1em;' onclick='editPerm("+val3.id+",0,"+val3.sdId+")'></i>&nbsp;" +
                                                        "<i class='fa fa-trash-o' data-toggle='tooltip' title='删除' data-original-title='删除' style='cursor: pointer;font-size:1.3em;padding-left: 1em;' onclick='delPerm("+val3.id+","+val3.sdId+",\""+val3.categoryName+"\")'></i>" +
                                                        "</div>" +
                                                        "</td>" +
                                                        "</tr>");
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });
                    }
                });

               /* $("#userListUl").viewer('update');
                $('#userListUl').viewer({
                    url: 'data-original',
                });*/
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
function addSun(pid,flag,curSdId){
    myResetForm();
    if(curSdId!=undefined&&curSdId!=null&&curSdId!=''){
        $("#sdId").val(curSdId);
    }
    if(null!=pid){
        //flag[0:开通权限；1：新增子节点权限]
        //type[0:编辑；1：新增]
        var msg = '';
        if(flag==0){
            if(isManySite){
                $("#form-group-sdId").css("display","block");
            }else{
                $("#form-group-sdId").css("display","none");
            }
            $("#pId").val(0);
            $("#chgBusTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;新增类别");
        }else{
            //设置父id
            $("#pId").val(pid);
            $("#chgBusTitle").html("<i class='iconfont iconshicailiebiao2'></i>&nbsp;新增子类别");
        }
        //赋值类型
        $("#comInfo-modal").modal("show");
    }
}
function editPerm(id,type,curSdId) {
    if(id!=null && !isNaN(id)){
        var subUrl = '/categoryBase/getUpdCategoryBase';
        $("#form-group-sdId").css("display","none");
        //重置清空form
        myResetForm();
        $.post(subUrl,{"id":id,"sdId":curSdId},function(data){
            if(!isKickOut(data)){
                //没有被踢出
                if(data.resultCode==100){
                    $("#id").val(data.result.id);
                    $("#categoryName").val(data.result.categoryName);
                    $("#sdId").val(data.result.sdId);
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
function delPerm(id,delSdId,name){
    if(null!=id&&id!=""){
        var subUrl = "/categoryBase/delCategoryBase";
        $.confirm({
            icon: '#icon-tishi1',
            theme: 'modern',
            title: '提示',
            content: '您确定要'+name+'类别吗？',
            animation: 'news',//动画
            closeAnimation: 'news',//关闭动画
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
                                    showJqueryConfirmWindow("#icon-jinlingyingcaitubiao14",'删除'+name+'类别成功！');
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
function showOrHideSun(did,dSdId) {
    var box = $("#"+did+"-myStyleLi-"+dSdId);
    var bf = box.siblings(".box-body"+did+"-"+dSdId);
    if (!box.hasClass("collapsed-box")) {
        box.addClass("collapsed-box");
        bf.slideUp();
    } else {
        box.removeClass("collapsed-box");
        bf.slideDown();
    }
}
function getParentArry(id, arry) {
    var newArry = new Array();
    for (var x in arry) {
        if (arry[x].pId == id){
            newArry.push(arry[x]);
        }
    }
    return newArry;
}
function myResetForm() {
    //清空隐藏域id
    $("#id").val("");
    $("#pId").val("");
    $("#sdId").val("");
    $("#sdIds").val("");

    $("#selectSites").val("");
    $('#selectSites').selectator('refresh');

    //清空权限数组
    $('#comInfoForm').resetForm();
    $('#comInfoForm').clearForm();
}