//全局图片常量
var myDefaultImg = "http://121.43.35.181:85";

/*
 旧版move top
$(function () {
    $().UItoTop({ easingType: 'easeOutQuart' });
});*/

//线条 start
function initMyCutLine() {
    cvs = document.getElementById("canvas");
    ctx = cvs.getContext("2d");

    cvs2 = document.getElementById("canvas2");
    ctx2 = cvs2.getContext("2d");
    resizeCanvas(cvs,cvs2);

    for (var i = 0; i < 3; i++) {
        var temp = new wave(colours[i],1,5);
    }

    setInterval(update,16);

}
function update(array) {

    var fill = window.getComputedStyle(document.querySelector(".myCutLineHeader"),null).getPropertyValue("background-color");
    ctx.fillStyle = fill;
    ctx.globalCompositeOperation = "source-over";
    ctx.fillRect(0,0,cvs.width,cvs.height);
    ctx.globalCompositeOperation = "screen";

    ctx2.fillStyle = fill;
    ctx2.globalCompositeOperation = "source-over";
    ctx2.fillRect(0,0,cvs2.width,cvs2.height);
    ctx2.globalCompositeOperation = "screen";
    for (var i = 0; i < waves.length; i++) {
        for (var j = 0; j < waves[i].nodes.length; j++) {
            bounce(waves[i].nodes[j]);
        }
        drawWave(waves[i]);
    }
    ctx.globalCompositeOperation = "hue";
    ctx.fillStyle = fill;

    ctx2.globalCompositeOperation = "hue";
    ctx2.fillStyle = fill;
}

function wave(colour,lambda,nodes) {
    this.colour = colour;
    this.lambda = lambda;
    this.nodes = [];
    var tick = 1;
    for (var i = 0; i <= nodes+2; i++) {
        var temp = [(i-1)*cvs.width/nodes,0,Math.random()*200,.3];//this.speed*plusOrMinus
        this.nodes.push(temp);
    }
    waves.push(this);
}

function bounce(node) {
    node[1] = waveHeight/2*Math.sin(node[2]/20)+cvs.height/2;
    node[2] = node[2] + node[3];

}

function drawWave (obj) {
    var diff = function(a,b) {
        return (b - a)/2 + a;
    }
    ctx.fillStyle = obj.colour;
    ctx.beginPath();
    ctx.moveTo(0,cvs.height);
    ctx.lineTo(obj.nodes[0][0],obj.nodes[0][1]);

    ctx2.fillStyle = obj.colour;
    ctx2.beginPath();
    ctx2.moveTo(0,cvs2.height);
    ctx2.lineTo(obj.nodes[0][0],obj.nodes[0][1]);
    for (var i = 0; i < obj.nodes.length; i++) {
        if (obj.nodes[i+1]) {
            ctx.quadraticCurveTo(
                obj.nodes[i][0],obj.nodes[i][1],
                diff(obj.nodes[i][0],obj.nodes[i+1][0]),diff(obj.nodes[i][1],obj.nodes[i+1][1])
            );

            ctx2.quadraticCurveTo(
                obj.nodes[i][0],obj.nodes[i][1],
                diff(obj.nodes[i][0],obj.nodes[i+1][0]),diff(obj.nodes[i][1],obj.nodes[i+1][1])
            );
        }else{
            ctx.lineTo(obj.nodes[i][0],obj.nodes[i][1]);
            ctx.lineTo(cvs.width,cvs.height);

            ctx2.lineTo(obj.nodes[i][0],obj.nodes[i][1]);
            ctx2.lineTo(cvs2.width,cvs2.height);
        }

    }
    ctx.closePath();
    ctx.fill();

    ctx2.closePath();
    ctx2.fill();
}

function resizeCanvas(canvas,canvas2) {
    canvas.height = waveHeight;
    canvas2.height = waveHeight;
}
//线条 end

//js乘法运算 避免出现N位小数
function accMul(arg1,arg2){
    var m=0,s1=arg1.toString(),
        s2=arg2.toString();
    try{
        m+=s1.split(".")[1].length}catch(e){}
    try{
        m+=s2.split(".")[1].length}catch(e){}
    return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
}
//js除法函数 避免出现N位小数
function accDiv(arg1,arg2){
    var t1=0,t2=0,r1,r2;
    try{
        t1=arg1.toString().split(".")[1].length}catch(e){
    }try{
        t2=arg2.toString().split(".")[1].length}catch(e){}
    with(Math){
        r1=Number(arg1.toString().replace(".",""));
        r2=Number(arg2.toString().replace(".",""));
        return (r1/r2)*pow(10,t2-t1);
    }
}
//加法函数
function accAdd(arg1,arg2){
    var r1,r2,m;
    try{
        r1=arg1.toString().split(".")[1].length
    }catch(e){
        r1=0} try{
        r2=arg2.toString().split(".")[1].length}catch(e){r2=0} m=Math.pow(10,Math.max(r1,r2))
    return (arg1*m+arg2*m)/m
}
//减法函数
function accSub(arg1, arg2) {
    var r1, r2, m, n;
    try {
        r1 = arg1.toString().split(".")[1].length;
    }
    catch (e) {
        r1 = 0;
    }
    try {
        r2 = arg2.toString().split(".")[1].length;
    }
    catch (e) {
        r2 = 0;
    }
    m = Math.pow(10, Math.max(r1, r2)); //last modify by deeka //动态控制精度长度
    n = (r1 >= r2) ? r1 : r2;
    return ((arg1 * m - arg2 * m) / m).toFixed(n);
}

/* 3秒自动关闭，关闭无其他事件 */
function showJqueryConfirmWindow(icon,msg){
    /**
     * jquery.confirm.js 使用阿里彩色图标
     * 修改源码，搜索<i  找到找到第二个，
     * i标签替换成<svg class="icon" aria-hidden="true"><use xlink:href="'+this.icon+'"></use></svg>即可，
     * */
    $.confirm({
        /*icon: 'glyphicon glyphicon-heart',*/
        /*icon: 'fa fa-bug',*/
        icon: icon,
        theme: 'modern',
        title: '提示',
        content: msg,
        animation: 'news',//动画
        closeAnimation: 'news',//关闭动画
        autoClose: 'showMyMsg|3000',
        buttons: {
            showMyMsg: {
                text: '关闭',
                action: function () {

                }
            }
        }
    });
}

/* 手动清除form */
function commonResetForm(formId){
    $("#"+formId).resetForm();
    $("#"+formId).clearForm();
}

/* 清除input hidded里的值 */
function cleanHiddenInputIds(inputHiddenIds){
    $.each(inputHiddenIds,function (ind,val) {
       $("#"+val).val("");
    });
}

/* 清除chosen 选中值 */
/* 清除chosen里的值 */
function cleanSelect2Val(select2Ids){
    $.each(select2Ids,function (ind,val) {
        $("#"+val).val("");
        $("#"+val).trigger("chosen:updated");
    });

}

//得到000类似数据
function getZeros(len){
    var zeros="";
    for(var i=0;i<len;i++){
        zeros = zeros + "0";
    }
    return zeros;
}

//得到用户所拥有的站点
function getUserSites(){
    var userSites;
    $.ajaxSettings.async = false;
    $.post("/site/getUserSites",function(data){
        if(data.resultCode=='100'){
            userSites = data;
        }
    });
    $.ajaxSettings.async = true;
    return userSites;
}

//跳转至查看所有站点提交的预采购单
function moreLPH(){
    window.location.href="/mainLibraryPurchase/mainLibraryPurchasePage";
}

//得到默认
function getMyDefaultImg() {
    return "http://121.43.35.181:85";
}

//获取头部未处理预采购单---改用平台获取推送信息
function writeTopLibPur(){
    $.post("/noticeCement/getTopNoticeCements",{},function(data){
        if(data.code==1000){
            if(data.rows.length>0){
                $("#msgContentTop").html("");
                $(".myBGMSGCount").text(data.rows.length);
                $.each(data.rows,function(ind,val){
                    $("#msgContentTop").append("<div class='media'>" +
                        "                        <div class='pull-left'>" +
                        "                            <img width='40' src='"+val.sitePhoto+"' alt='' title='"+val.siteName+"'>" +
                        "                        </div>" +
                        "                        <div class='media-body'>" +
                        "                            <small class='text-muted'>"+val.siteName+"-"+val.noticeTitle+"-"+val.noticeType+"-"+val.noticeTime+"</small><br>" +
                        "                            <a class='t-overflow' href=''>"+val.noticeContent+"</a>" +
                        "                        </div>" +
                        "                    </div>");
                });
            }else{
                $("#msgContentTop").html("");
                $(".myBGMSGCount").css("display","none");
                $(".myBGMSGCount").text(0);
            }
        }
    });
}

//获取平台证件预警信息
function writeTopWarning(){
    $.post("/certificateWarning/getTopCertificateWarning",{},function(data){
        if(data.code==1000){
            if(data.rows.length>0){
                $("#yjTopMsg").html("");
                $(".yjTopImgStyle").text(data.rows.length);
                $.each(data.rows,function(ind,val){
                    $("#yjTopMsg").append("<div class='media'>" +
                        "                        <div class='pull-left'>" +
                        "                            <img width='40' src='"+val.sitePhoto+"' alt='' title='"+val.siteName+"'>" +
                        "                        </div>" +
                        "                        <div class='media-body'>" +
                        "                            <small class='text-muted'>"+val.siteName+"-"+val.title+"</small><br>" +
                        "                            <a class='t-overflow' href=''>"+val.content+"</a>" +
                        "                        </div>" +
                        "                    </div>");
                });
            }else{
                $("#yjTopMsg").html("");
                $(".yjTopImgStyle").css("display","none");
                $(".yjTopImgStyle").text(0);
            }
        }
    });
}

//阻止dropdownmenu点击隐藏事件
function notHideDropDownMenu(e) {
    e.stopPropagation();
}

//产生随机数,minNum-maxNum之间的随机整数
function randomize(minNum,maxNum){
    return parseInt(Math.random() * (maxNum - minNum + 1) + minNum);
}

//在Jquery里格式化Date日期时间数据
function timeStamp3String(time){
    var datetimeFor = new Date();
    datetimeFor.setTime(time);
    var year = datetimeFor.getFullYear();
    var month = datetimeFor.getMonth() + 1 < 10 ? "0" + (datetimeFor.getMonth() + 1) : datetimeFor.getMonth() + 1;
    var date = datetimeFor.getDate() < 10 ? "0" + datetimeFor.getDate() : datetimeFor.getDate();
    var hour = datetimeFor.getHours()< 10 ? "0" + datetimeFor.getHours() : datetimeFor.getHours();
    var minute = datetimeFor.getMinutes()< 10 ? "0" + datetimeFor.getMinutes() : datetimeFor.getMinutes();
    var second = datetimeFor.getSeconds()< 10 ? "0" + datetimeFor.getSeconds() : datetimeFor.getSeconds();
    return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second;
}

//导航栏样式
function addActClass(cur){

    var menu = $(cur).parent().children(".treeview-menu").first();
    var isActive = $(cur).parent().hasClass('active');

    //initialize already active menus
    if (isActive) {
        menu.show();
        $(cur).children(".fa-angle-left").first().removeClass("fa-angle-left").addClass("fa-angle-down");
    }

    //e.preventDefault();
    if (isActive) {
        //Slide up to close menu
        menu.slideUp();
        isActive = false;
        $(cur).children(".fa-angle-down").first().removeClass("fa-angle-down").addClass("fa-angle-left");
        $(cur).parent("li").removeClass("active");
    } else {
        //Slide down to open menu
        menu.slideDown();
        isActive = true;
        $(cur).children(".fa-angle-left").first().removeClass("fa-angle-left").addClass("fa-angle-down");
        $(cur).parent("li").addClass("active");
    }
}

//显示邮件弹出框
function showMyTopEmailModel() {
    $("#myTopEmail-model").modal("show");
}

//发送邮件
function sendMyEmail(){

    var myFileSizeKB = 0;
    if($("#emailFile")[0].files[0]!=undefined&&$("#emailFile")[0].files[0]!=null){
        var fileSize = $("#emailFile")[0].files[0].size;
        myFileSizeKB = Math.ceil(fileSize / 1024);
    }
    if(myFileSizeKB<fileMaxSize){
        //传一个mailBean对象
        var formData = new FormData($("#emailForm")[0]);
        $.ajax({
            //接口地址
            url: '/email/sendMailAttachment' ,
            type: 'POST',
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                //成功的回调
                if(data.code == 1000){
                    $("#myTopEmail-model").modal("hide");
                    $("#myFileShowName").text("");
                    $("#emailTextArea").val("");
                    $('#emailForm').resetForm();
                    $('#emailForm').clearForm();
                    showJqueryConfirmWindow('#icon-jinlingyingcaitubiao14',data.msg);
                    //清空form表单
                }else if(data.code == 1001){
                    showJqueryConfirmWindow('#icon-shangxin1',data.msg);
                }else{
                    showJqueryConfirmWindow('#icon-tishi1',data.msg);
                }

            },
            error: function (data) {
                //请求异常的回调
                // modals.warn("网络访问失败，请稍后重试!");
                showJqueryConfirmWindow('#icon-shangxin1',"数据传输异常");
            }
        });
    }else{
        showJqueryConfirmWindow('#icon-tishi1',"文件过大，请上传少于10MB的文件");
    }

}

/**
 * 获取当月天数
 * @param $date
 * @param $rtype 1天数 2具体日期数组
 * @return
 */
function get_day( myDate ,myType) {
    var myDateArray = [];
    var tem = myDate.split("-");    //切割日期 得到年份和月份
    var myYear = tem[0];
    var myMonth = tem[1];
    var text;
    var myArray = [ "1", "3", "5", "7","8", "01", "03", "05", "07", "08", "10", "12"];
    var returnData;
    if($.inArray(myMonth,myArray)>0){
        // $text = $year.'年的'.$month.'月有31天';
        text = '31';
    }else if( myMonth == '02' ){
        if (accDiv(myYear,400) == 0 || (accDiv(myYear,4) == 0 && accDiv(myYear,100) !== 0) ){
            // $text = $year.'年的'.$month.'月有29天';
            text = '29';
        }else{
            // $text = $year.'年的'.$month.'月有28天';
            text = '28';
        }
    }else{
        // $text = $year.'年的'.$month.'月有30天';
        text = '30';
    }

    if (myType == '2') {
        for (var i= 1; i <= text ; i ++ ) {
            var trueI = '';
            if(i<10){
                trueI="0"+i;
            }else{
                trueI = i;
            }
            myDateArray.push(myYear+"-"+myMonth+"-"+trueI);
        }
        returnData = myDateArray;
    }else {
        returnData = text;
    }
    return returnData;
}

//菜单栏 第一个子标签下还有标签
function showSecondHref(nodeId){
    var curElement = $("#liParent"+nodeId).find("a").eq(0);
    var iClass = $(curElement).find("i").attr("class").indexOf("fa-angle-double-right");

    if(iClass!=-1){
        $(curElement).find("i").removeClass("fa-angle-double-right").addClass("fa-angle-double-down");
    }else{
        $(curElement).find("i").removeClass("fa-angle-double-down").addClass("fa-angle-double-right");
    }

    var menu = $(curElement).parent().children(".treeview-menu").first();
    var isActive = $(curElement).parent().hasClass('active');

    //initialize already active menus
    if (isActive) {
        menu.show();
        $(curElement).children(".fa-angle-left").first().removeClass("fa-angle-left").addClass("fa-angle-down");
    }

    //e.preventDefault();
    if (isActive) {
        //Slide up to close menu
        menu.slideUp();
        isActive = false;
        $(curElement).children(".fa-angle-down").first().removeClass("fa-angle-down").addClass("fa-angle-left");
        $(curElement).parent("li").removeClass("active");
    } else {
        //Slide down to open menu
        menu.slideDown();
        isActive = true;
        $(curElement).children(".fa-angle-left").first().removeClass("fa-angle-left").addClass("fa-angle-down");
        $(curElement).parent("li").addClass("active");
    }
}

//生成x-y的随机整数
function myRandomData(x,y) {
    return parseInt(Math.random() * (y - x + 1) + x);
}

/* 返回顶部 start */

//返回顶部click
$('.toTopRocket').click(function(){
    $('html,body').animate({scrollTop: '0px'}, 800);
});

/* 地图形式显示站点 */
function showMap(map,modalId){

    $("#my-map2").empty();
    // 百度地图API功能
    map = new BMap.Map("my-map2");
    map.centerAndZoom(new BMap.Point(120.578308,30.60934),6);
    //设置主题样式
    //map.setMapStyle({style:'midnight'});

    map.enableScrollWheelZoom(true);

    map.clearOverlays();
    var allDat = getUserSites();
    var siteData = allDat.rows;
    var myImgPrefix = allDat.obj;
    var markers = [];
    if(modalId!=''&&modalId!=null){
        //初始化之前选择的值
        var bReloadSel = $('#select1').val();
        $('#select1').empty();
    }else{
        $("#menuTypeId").html("");
        $('#select1').empty();
        $("#select1").append("<option value=''>--请选择--</option>");
    }

    $.each(siteData,function(ind,val){

        if(modalId!=''&&modalId!=null){
            if($.inArray(val.sdId.toString(),bReloadSel)>=0){
                //加载所有站点数据下拉列表
                $("#select1").append("<option value='"+val.sdId+"' data-lng='"+val.lng+"' data-lat='"+val.lat+"' data-subtitle='"+val.address+"' data-left='<img src="+myImgPrefix+val.photo+">' data-right='"+val.zindex+"' selected>"+val.name+"</option>")
            }else{
                //加载所有站点数据下拉列表
                $("#select1").append("<option value='"+val.sdId+"' data-lng='"+val.lng+"' data-lat='"+val.lat+"' data-subtitle='"+val.address+"' data-left='<img src="+myImgPrefix+val.photo+">' data-right='"+val.zindex+"'>"+val.name+"</option>")
            }
        }else{
            //加载所有站点数据下拉列表
            $("#select1").append("<option value='"+val.id+"' data-lng='"+val.lng+"' data-lat='"+val.lat+"' data-subtitle='"+val.address+"' data-left='<img src="+myImgPrefix+val.photo+">' data-right='"+val.zindex+"'>"+val.name+"</option>")

            $("#menuTypeId").append("<option value='"+val.sdId+"'>"+val.name+"</option>");
            if(fruits!=null&&fruits!=undefined){
                if($.inArray(val.sdId+"", fruits)!=-1){
                    $("#menuTypeId").find("option[value='"+val.sdId+"']").attr("selected",'selected');
                    $("#menuTypeId").trigger("chosen:updated");
                }
            }
        }

        if(val.pid==0){
            //map.setCenter();
            setTimeout(function () {
                map.setCenter(new BMap.Point(val.lng,val.lat));
            } ,1000 * 0.5);

        }

        if(val.lng!=null&&val.lat!=null){

            var sContent =
                "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>"+val.name+"</h4>" +
                "<img style='float:right;margin:4px' id='imgDemo' src='"+myImgPrefix+val.photo+"' width='139' height='104' title='"+val.name+"'/>" +
                "<p style='margin:0;line-height:1.5;font-size:13px;text-indent:2em'>"+val.address+"</p>" +
                "</div>";

            var infoWindow = new BMap.InfoWindow(sContent);  // 创建信息窗口对象

            if(val.photo!=undefined&&val.photo!=null&&val.photo!=""){
                //创建自己的图标
                var pp = new BMap.Point(val.lng, val.lat);
                //原本小图标 myImgPrefix+val.photo --》 当前使用智飨logo
                var myIcon = new BMap.Icon(
                    '../../images/logo1.png', // 上传的站点图标
                    new BMap.Size(38, 47), // 视窗大小
                    {
                        imageSize: new BMap.Size(50,57), // 引用图片实际大小
                        imageOffset:new BMap.Size(0,0)  // 图片相对视窗的偏移
                    }
                );
                var marker2 = new BMap.Marker(pp, {
                    icon : myIcon
                });  // 创建标注
                marker2.setOffset(new BMap.Size(0,-20));
                map.addOverlay(marker2);              // 将标注添加到地图中
                //marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
                markers.push(marker2);

                /*marker2.addEventListener("click", function(){
                    setCurrentSdId(sdIdId,val.sdId);
                    marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
                });*/
                marker2.addEventListener("click", function(e) {    //鼠标点击下拉列表后的事件
                    map.clearOverlays();    //清除地图上所有覆盖物
                    map.centerAndZoom(this.point, 18);
                });
                //双击取消站点选择
                marker2.addEventListener("dblclick", function(){
                    marker2.setAnimation(null);
                });

                marker2.addEventListener("mouseover", function(){
                    this.openInfoWindow(infoWindow);
                    //图片加载完毕重绘infowindow
                    document.getElementById('imgDemo').onload = function (){
                        infoWindow.redraw();   //防止在网速较慢，图片未加载时，生成的信息框高度比图片的总高度小，导致图片部分被隐藏
                    }
                });

                marker2.addEventListener("mouseout", function(){
                    this.closeInfoWindow();
                });

            }else{
                //创建百度地图默认小狐狸图标
                var pp = new BMap.Point(val.lng, val.lat);
                var myIcon = new BMap.Icon("http://lbsyun.baidu.com/jsdemo/img/fox.gif", new BMap.Size(300,157));
                var marker2 = new BMap.Marker(pp,{icon:myIcon});  // 创建标注
                map.addOverlay(marker2);              // 将标注添加到地图中
                //marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画

                markers.push(marker2);

                /*marker2.addEventListener("click", function(){
                    //alert(val.lng+",,,"+val.lat+",,,,"+val.name+",,,"+val.address);
                    setCurrentSdId(sdIdId,val.sdId);
                    marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
                });*/
                /*marker2.addEventListener("click", function() {    //鼠标点击下拉列表后的事件
                    alert("lsjdflkjasdf");
                    var _value = e.item.value;
                    myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
                    G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
                    alert(_value);
                    setPlace(map,myValue);
                });*/
                //双击取消站点选择
                marker2.addEventListener("dblclick", function(){
                    //alert(val.lng+",,,"+val.lat+",,,,"+val.name+",,,"+val.address);
                    /*setCurrentSdId(sdIdId,val.sdid);
                    marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画*/
                    marker2.setAnimation(null);
                });

                marker2.addEventListener("mouseover", function(){

                    this.openInfoWindow(infoWindow);
                    //图片加载完毕重绘infowindow
                    document.getElementById('imgDemo').onload = function (){
                        infoWindow.redraw();   //防止在网速较慢，图片未加载时，生成的信息框高度比图片的总高度小，导致图片部分被隐藏
                    }
                });

                marker2.addEventListener("mouseout", function(){
                    this.closeInfoWindow();
                });
            }


        }
    });

    if(modalId!=''&&modalId!=null){
        if ($('#select1').data('selectator') === undefined) {
            //初始化下拉插件
            $('#select1').selectator({
                labels: {
                    search: '请输入站点'
                },
                showAllOptionsOnFocus: true//初始化复选
            });
        } else {
            $('#select1').selectator('destroy');
        }
    }else{
        //初始化下拉插件
        $('#select1').selectator({
            labels: {
                search: '请输入站点'
            }
        });
    }

    $('#select1').next().css("width","100%");

    //点聚合
    //最简单的用法，生成一个marker数组，然后调用markerClusterer类即可。
    var markerClusterer = new BMapLib.MarkerClusterer(map, {markers:markers});


    // 覆盖区域图层测试
    map.addTileLayer(new BMap.PanoramaCoverageLayer());

    var stCtrl = new BMap.PanoramaControl(); //构造全景控件
    stCtrl.setOffset(new BMap.Size(20, 20));
    map.addControl(stCtrl);//添加全景控件

    // 添加带有定位的导航控件
    var navigationControl = new BMap.NavigationControl({
        // 靠左上角位置
        anchor: BMAP_ANCHOR_TOP_LEFT,
        // LARGE类型
        type: BMAP_NAVIGATION_CONTROL_LARGE,
        // 启用显示定位
        enableGeolocation: true
    });
    map.addControl(navigationControl);


    // 添加定位控件
    var geolocationControl = new BMap.GeolocationControl();
    geolocationControl.addEventListener("locationSuccess", function(e){
        // 定位成功事件
        var address = '';
        address += e.addressComponent.province;
        address += e.addressComponent.city;
        address += e.addressComponent.district;
        address += e.addressComponent.street;
        address += e.addressComponent.streetNumber;

    });
    geolocationControl.addEventListener("locationError",function(e){
        // 定位失败事件
        alert(e.message);
    });
    map.addControl(geolocationControl);


    var cr = new BMap.CopyrightControl({anchor: BMAP_ANCHOR_TOP_RIGHT});   //设置版权控件位置
    map.addControl(cr); //添加版权控件

    var bs = map.getBounds();   //返回地图可视区域
    cr.addCopyright({id: 1, content: "Copyright © 2019.Company name All rights reserved.More Templates 智飨云 - Collect from 智飨云<a href='#'>智飨科技</a>", bounds: bs});
    //Copyright(id,content,bounds)类作为CopyrightControl.addCopyright()方法


    var geoc = new BMap.Geocoder();

    //单击获取点击的经纬度
    map.addEventListener("click",function(e){

        //点击获取试点id

    });

    var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
        {"input" : "suggestId"
            ,"location" : map
        });

    ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
        var str = "";
        var _value = e.fromitem.value;
        var value = "";
        if (e.fromitem.index > -1) {
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

        value = "";
        if (e.toitem.index > -1) {
            _value = e.toitem.value;
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
        G("searchResultPanel").innerHTML = str;
    });

    var myValue;
    ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
        var _value = e.item.value;
        myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;

        setPlace(map,myValue);
    });

    if(modalId!=undefined&&modalId!=''&&modalId!=null){
        $("#"+modalId).modal("show");
    }

    return map;
}

// 百度地图API功能
function G(id) {
    return document.getElementById(id);
}

function setPlace(map,myValue){
    map.clearOverlays();    //清除地图上所有覆盖物
    function myFun(){
        var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
        map.centerAndZoom(pp, 18);
        //map.addOverlay(new BMap.Marker(pp));    //添加标注
    }
    var local = new BMap.LocalSearch(map, { //智能搜索
        onSearchComplete: myFun
    });
    local.search(myValue);
}

/*
function clickMapSelect(selectId,map) {
    alert("sljdflasdf"+",,,"+map);
    var curMapSelect = $("#"+selectId).val();
    if(curMapSelect!=undefined&&curMapSelect!=null&&curMapSelect!=""){
        var curVal = curMapSelect[curMapSelect.length-1];
        var lng = $("#select1 option[value="+curVal+"]").attr("data-lng");
        var lat = $("#select1 option[value="+curVal+"]").attr("data-lat");
        map.clearOverlays();    //清除地图上所有覆盖物
        map.centerAndZoom(new BMap.Point(lng,lat), 18);
    }
    //做其他事的标识 otherThing

}*/

//生成二维码
function createCode(QrId,QrUrl,foodImgId) {
    $("#"+QrId).qrcode({
        render: "canvas", //也可以替换为table
        minVersion: 1,       // version range somewhere in 1 .. 40
        maxVersion: 40,
        ecLevel: 'H',        //识别度  'L', 'M', 'Q' or 'H'
        left: 0,
        top: 0,
        size: 140,           //尺寸
        /*fill: '#fff',        //二维码颜色*/
        background: null,    //背景色
        text: QrUrl,     //二维码内容
        radius: 0.1,         // 0.0 .. 0.5
        quiet: 2,            //边距
        mode: 4,
        mSize: 0.2,          //图片大小
        mPosX: 0.5,
        mPosY: 0.5,
        label: 'jQuery.qrcode',
        fontname: 'sans',
        fontcolor: '#000',
        image: $('#'+foodImgId)[0]
    });
}

//设置年份（不大于当前年份）
function setYearChosen(id,curYear) {
    for(var i=curYear;i>=2017;i--){
        $("#"+id).append("<option value='"+i+"'>"+i+"年</option>");
    }
    //初始化下拉插件
    $("#"+id).selectator({
        labels: {
            search: '请输入年份'
        },
        showAllOptionsOnFocus: true//初始化复选
    });
    $("#"+id).next().css("width","100%");
}

//生成随机颜色 start
function bg2() {
    return '#' + Math.floor(Math.random() * 0xffffff).toString(16);
}
function rancolors(len) {
    var color = [];
    for (var i = 0; i <= len; i++) {
        var sjys = bg2();
        color.push(sjys);
        for (var i = 0; i < color.length; i++) {
            if (color[i] = sjys) {
                color[i] = bg2();
            }
        }
    }
    return color;
}
//生成随机颜色 end

//生成提示 start
//right left center bottom
function createJBox(msgColor,msgXY,msg,msgTime){
    new jBox('Notice', {
        attributes: {
            x: msgXY[0],
            y: msgXY[1]
        },
        animation: {
            open: 'tada',
            close: 'zoomIn'
        },
        color: msgColor,
        autoClose: msgTime,
        content: msg,
        delayOnHover: true,
        showCountdown: true,
        closeButton: true
    });
}
//生成提示 end

//重载selectator start
function reloadMySelectator(relaodJson){
    $.each(relaodJson,function (ind,val) {
        $("#"+val.id).selectator('destroy');
        $("#"+val.id).html("");
        $("#"+val.id).append("<option value=''>"+val.inputText+"</option>");
        switch (val.id){
            case 'meteringName':
                $.each(val.curSelArray,function (ind2,val2) {
                    $("#"+val.id).append("<option value='"+val2.meteringName+"' data-siteId='"+val2.sdId+"' data-subtitle='"+val2.siteName+"' data-left='<img src="+val2.sitePhoto+">'>"+val2.meteringName+"</option>");
                });
                if(val.sdId!=''&&val.sdId!=null&&val.sdId!=undefined){
                    $("#"+val.id).find("option[value='"+val.curVal+"'][data-siteId='"+val.sdId+"']").attr("selected","selected");
                }
                break;
            case 'whouseId':
                $.each(val.curSelArray,function (ind2,val2) {
                    if(val.sdId!=''&&val.sdId!=null&&val.sdId!=undefined){
                        if(val.sdId==val2.sdId){
                            $("#"+val.id).append("<option value='"+val2.id+"' data-siteId='"+val2.sdId+"' data-subtitle='"+val2.siteName+"' data-left='<img src="+val2.sitePhoto+">'>"+val2.whName+"</option>");
                        }
                    }else{
                        $("#"+val.id).append("<option value='"+val2.id+"' data-siteId='"+val2.sdId+"' data-subtitle='"+val2.siteName+"' data-left='<img src="+val2.sitePhoto+">'>"+val2.whName+"</option>");
                    }

                });
                if(val.sdId!=''&&val.sdId!=null&&val.sdId!=undefined){
                    $("#"+val.id).val(val.curVal);
                }
                break;
            case 'suppId':
                $.each(val.curSelArray,function (ind2,val2) {
                    if(val.sdId!=''&&val.sdId!=null&&val.sdId!=undefined){
                        if(val.sdId==val2.sdId){
                            $("#"+val.id).append("<option value='"+val2.id+"' data-siteId='"+val2.sdId+"' data-subtitle='"+val2.siteName+"' data-left='<img src="+val2.sitePhoto+">'>"+val2.supplierName+"</option>");
                        }
                    }else{
                        $("#"+val.id).append("<option value='"+val2.id+"' data-siteId='"+val2.sdId+"' data-subtitle='"+val2.siteName+"' data-left='<img src="+val2.sitePhoto+">'>"+val2.supplierName+"</option>");
                    }
                });
                if(val.sdId!=''&&val.sdId!=null&&val.sdId!=undefined){
                    $("#"+val.id).val(val.curVal);
                }
                break;
            case 'mainCategoryId':
                $.each(val.curSelArray,function (ind2,val2) {
                    if(val.sdId!=''&&val.sdId!=null&&val.sdId!=undefined){
                        if(val.sdId==val2.sdId){
                            $("#"+val.id).append("<option value='"+val2.id+"' data-siteId='"+val2.sdId+"' data-subtitle='"+val2.siteName+"' data-left='<img src="+val2.sitePhoto+">'>"+val2.categoryName+"</option>");
                        }
                    }else{
                        $("#"+val.id).append("<option value='"+val2.id+"' data-siteId='"+val2.sdId+"' data-subtitle='"+val2.siteName+"' data-left='<img src="+val2.sitePhoto+">'>"+val2.categoryName+"</option>");
                    }
                });
                if(val.sdId!=''&&val.sdId!=null&&val.sdId!=undefined){
                    $("#"+val.id).val(val.curVal);
                }
                break;
            case 'smallCategoryId':
                $.each(val.curSelArray,function (ind2,val2) {
                    if(val.sdId!=''&&val.sdId!=null&&val.sdId!=undefined){
                        if(($("#mainCategoryId").val() == val2.pId)&&(val.sdId==val2.sdId)){
                            $("#"+val.id).append("<option value='"+val2.id+"' data-siteId='"+val2.sdId+"' data-subtitle='"+val2.siteName+"' data-left='<img src="+val2.sitePhoto+">'>"+val2.categoryName+"</option>");
                        }
                    }else{
                        //$("#"+val.id).append("<option value='"+val2.id+"' data-siteId='"+val2.sdId+"' data-subtitle='"+val2.siteName+"' data-left='<img src="+val2.sitePhoto+">'>"+val2.categoryName+"</option>");
                    }
                });
                if(val.sdId!=''&&val.sdId!=null&&val.sdId!=undefined){
                    if(val.curVal!=undefined&&val.curVal!=null&&val.curVal!=''){
                        $("#"+val.id).val(val.curVal);
                    }
                }
                break;
        }

        $("#"+val.id).selectator({
            labels: {
                search: val.inputText
            },
            showAllOptionsOnFocus: true
        });
        $("#"+val.id).next().css("width","100%");
        $("#"+val.id).next().find(".selectator_input").css("background","#1b1b1b");
    });
}
//重载selectator end
//获取多图片
function getManyPicture(pictureAddress,sitePhoto,siteName,picturePrefix) {
    var ibImgPath = '';
    if(pictureAddress	&&pictureAddress!=''){
        var allImg = pictureAddress.split(",");
        $.each(allImg,function (ind2,val2) {
            if(allImg.length==1){
                if(val2.indexOf("http:")!=-1){
                    ibImgPath = ibImgPath + "<a href='javascript:;' class='tpl-table-images-content-i'>" +
                        "<div class='tpl-table-images-content-i-info' style='width: 40px;height:40px;'>" +
                        "<span class='ico'>" +
                        "<img src='"+sitePhoto+"' data-toggle='tooltip' title='"+siteName+"' data-original-title='"+siteName+"'>" +
                        "</span>" +
                        "</div>" +
                        "<span class='ico'></span>" +
                        "<img src='"+val2+"' data-original='"+val2+"' style='height: 12em;'></a>" ;
                }else{
                    ibImgPath = ibImgPath + "<a href='javascript:;' class='tpl-table-images-content-i'>" +
                        "<div class='tpl-table-images-content-i-info' style='width: 40px;height:40px;'>" +
                        "<span class='ico'>" +
                        "<img src='"+sitePhoto+"' data-toggle='tooltip' title='"+siteName+"' data-original-title='"+siteName+"'>" +
                        "</span>" +
                        "</div>" +
                        "<span class='ico'></span>" +
                        "<img src='"+picturePrefix+val2+"' data-original='"+picturePrefix+val2+"' style='height: 12em;'></a>" ;
                }
            }else{
                if(ind2<2){
                    if(val2.indexOf("http:")!=-1){
                        ibImgPath = ibImgPath + "<a href='javascript:;' class='tpl-table-images-content-i'>" +
                            "<div class='tpl-table-images-content-i-info' style='width: 40px;height:40px;'>" +
                            "<span class='ico'>" +
                            "<img src='"+sitePhoto+"' data-toggle='tooltip' title='"+siteName+"' data-original-title='"+siteName+"'>" +
                            "</span>" +
                            "</div>" +
                            "<span class='ico'></span>" +
                            "<img src='"+val2+"' data-original='"+val2+"' style='height: 6em;'></a>" ;
                    }else{
                        ibImgPath = ibImgPath + "<a href='javascript:;' class='tpl-table-images-content-i'>" +
                            "<div class='tpl-table-images-content-i-info' style='width: 40px;height:40px;'>" +
                            "<span class='ico'>" +
                            "<img src='"+sitePhoto+"' data-toggle='tooltip' title='"+siteName+"' data-original-title='"+siteName+"'>" +
                            "</span>" +
                            "</div>" +
                            "<span class='ico'></span>" +
                            "<img src='"+picturePrefix+val2+"' data-original='"+picturePrefix+val2+"' style='height: 6em;'></a>" ;
                    }
                }
            }
        });

    }else{
        ibImgPath = ibImgPath + "<a href='javascript:;' class='tpl-table-images-content-i'>" +
            "<div class='tpl-table-images-content-i-info' style='width: 40px;height:40px;'>" +
            "<span class='ico'>" +
            "<img src='"+sitePhoto+"' data-toggle='tooltip' title='"+siteName+"' data-original-title='"+siteName+"'>" +
            "</span>" +
            "</div>" +
            "<span class='ico'></span>" +
            "<img src='../../../images/VRPicture.png' data-original='' style='height: 12em;'></a>" ;
    }
    return ibImgPath;
}