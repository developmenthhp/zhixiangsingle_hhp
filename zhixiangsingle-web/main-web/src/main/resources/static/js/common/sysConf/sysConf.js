var basePath;

$(function(){

    basePath = getContextPath();

});

function getContextPath(){
    var pathName = document.location.pathname;
    var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0,index+1);
    return result;
}

function isKickOut(data){
    var flag = false;
    if(data){
        if(data.resultCode==107||data.resultCode==112){
            //被踢出
            flag = true;
            $.confirm({
                icon: '#icon-tishi1',
                theme: 'modern',
                title: '登录异常',
                content: data.msg,
                animation: 'news',
                closeAnimation: 'news',
                autoClose: 'showMyMsg|3000',
                buttons: {
                    showMyMsg: {
                        text: '关闭',
                        action: function () {
                            window.location.href="/login";
                        }
                    }
                }
            });
        }
    }

    return flag;
}