$(document).ready(function(){
    //$("#up-img-touch").click(showMyCropperImgModel);
});
$(function(){
    'use strict';
    var $image=$('#up-img-show');
    $image.cropper({
        //aspectRatio:'1',//裁剪框的纵横比 例如1:1的头像就写1,16:9可写成16 / 9。
        autoCropArea:0.8,//自动显示的裁剪框的大小。因此，数字应当在0~1之间。
        preview:'.up-pre-after',//预览图的位置，用jQuery选择器表示。
        responsive:true,//在更改窗口大小后是否重新渲染cropper。
        zoomOnTouch:true //是否允许在移动端上使用双指触摸缩放原图。 默认true
    });
    var $inputImage=$('.up-modal-frame .up-img-file');
    var URL=window.URL||window.webkitURL;
    var blobURL;
    if(URL){
        $inputImage.change(function(){
            var files=this.files;
            var file;
            if(files&&files.length){
                file=files[0];
                if(/^image\/\w+$/.test(file.type)){
                    blobURL=URL.createObjectURL(file);
                    $image.one('built.cropper',function(){
                        URL.revokeObjectURL(blobURL);
                    }).cropper('reset').cropper('replace',blobURL);$inputImage.val('');
                }else{
                    window.alert('Please choose an image file.');
                }
            }
        });
    }else{
        $inputImage.prop('disabled',true).parent().addClass('disabled');
    }
$('.up-modal-frame .up-btn-ok').on('click',function(){
    $(".myImgSecond").css("display","block");
    var $modal_loading=$('#up-modal-loading');
    var $modal_alert=$('#up-modal-alert');
    var img_src=$image.attr("src");
    if(img_src==""){
        showJqueryConfirmWindow("#icon-tishi1","请选择图片");
        return false;
    }
    var dateIndex = $(this).attr("data-index");
    var url=$(this).attr("url");
    var parameter=$(this).attr("parameter");
    var parame_json=eval('('+parameter+')');
    var width=parame_json.width;
    var height=parame_json.height;
    /*console.log(parame_json.width);
    console.log(parame_json.height);*/
    var canvas=$image.cropper('getCroppedCanvas',{width:width,height:height});
    canvas.toBlob(function (e) {
        /*var timestamp = Date.parse(new Date());
        e.name=timestamp+".jpeg";*/
        var fd = new FormData();
        fd.append("file", e); //fileData为自定义
        $.ajax({
            url:url,
            dataType:'json',
            type:"POST",
            data: fd,
            processData: false,
            contentType: false,
            success:function(data,textStatus) {
                if (!isKickOut(data)) {
                    if (data.resultCode == 100) {
                        doMyLogic(data);
                        $("#up-modal-frame").modal("hide");
                    } else if (data.resultCode == 101) {
                        showJqueryConfirmWindow("#icon-shangxin1", "上传照片异常！深感抱歉，请联系管理人员为您加急处理");
                    } else {
                        showJqueryConfirmWindow("#icon-tishi1", data.msg);
                    }
                }
            },
            error:function(){
                showJqueryConfirmWindow("#icon-tishi1","失败");
            }});
    },'image/jpeg');
});
    $('#up-btn-left').on('click',function(){
        $("#up-img-show").cropper('rotate',90);
    });
    $('#up-btn-right').on('click',function(){
        $("#up-img-show").cropper('rotate',-90);
    });
});
function showMyCropperImgModel(updImgIndex) {
    $(".up-modal-frame .up-btn-ok").attr("data-index",updImgIndex);
    $("#up-modal-frame").modal({});
}
function set_alert_info(content){
    $("#alert_content").html(content);
}