window.onload = function(){

    <!-- 防止调试js ..简单 -->
    setInterval(function() {
        check()
    }, 4000);
    var check = function() {
        function doCheck(a) {
            if (("" + a / a)["length"] !== 1 || a % 20 === 0) {
                (function() {}
                    ["constructor"]("debugger")())
            } else {
                (function() {}
                    ["constructor"]("debugger")())
            }
            doCheck(++a)
        }
        try {
            doCheck(0)
        } catch (err) {}
    };
    check();
}