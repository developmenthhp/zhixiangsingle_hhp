var formCo = false;
$(function(){
    var path=window.location.href;
    if(path.indexOf("kickout")>0){
        window.location.href="/login";
    }
    //监听窗口，显示隐藏move top
    $(window).scroll(function () {
        if($(window).scrollTop() >= 100){
            $('.toTopRocket').fadeIn(300);
        }else{
            $('.toTopRocket').fadeOut(300);
        }
    });

});
function login(){
    var name = $("input[name='userName']").val().trim();
    var psw = $("input[name='password']").val().trim();
    if((name==null||name=="")&&(psw==null||psw=="")){
        showJqueryConfirmWindow("#icon-tishi1","请输入用户名和密码");
        return false;
    }else if(name==null||name==''){
        showJqueryConfirmWindow("#icon-tishi1","请输入用户名");
        return false;
    }else if(psw==null||psw==''){
        showJqueryConfirmWindow("#icon-tishi1","请输入密码");
        return false;
    }else{
        $.post("/user/login",$('#loginForm').serialize(),function(data){
            console.log(data);
            if(data.resultCode==100){
                window.location.href="/home";
            }else{
                showJqueryConfirmWindow("#icon-tishi1",data.msg);
            }
        });
    }
}
