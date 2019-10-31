document.writeln("<!-- 头部logo 未读消息 任务通知 header logo: style can be found in header.less -->");
document.writeln("        <header class=\'header\'>");
document.writeln("            <a href=\'/home\' class=\'logo\'>");
document.writeln("                <!-- Add the class icon to your logo image or logo icon to add the margining -->");
document.writeln("                <!-- nav logo -->");
document.writeln("                <img src=\'../../images/logo1.png\' class=\'img-circle\' style=\'width: 25%;height: 100%;\'/>");
document.writeln("            </a>");
document.writeln("            <!-- Header Navbar: style can be found in header.less -->");
document.writeln("            <nav class=\'navbar navbar-static-top\' role=\'navigation\'>");
document.writeln("                <!-- Sidebar toggle button-->");
document.writeln("                <a href=\'javascript:void(0)\' class=\'navbar-btn sidebar-toggle\' data-toggle=\'offcanvas\' role=\'button\'>");
document.writeln("                    <span class=\'sr-only\'>展开菜单</span>");
document.writeln("                    <span class=\'icon-bar\'></span>");
document.writeln("                    <span class=\'icon-bar\'></span>");
document.writeln("                    <span class=\'icon-bar\'></span>");
document.writeln("                </a>");




document.writeln("    <div class=\'media-body\'>");
document.writeln("        <div class=\'media\' id=\'top-menu\'>");
document.writeln("            <div class=\'pull-left tm-icon myHideMsg\'>");
document.writeln("                <a data-drawer=\'messages\' class=\'drawer-toggle\' href=\'\'>");
document.writeln("                    <i class=\'sa-top-message\'></i>");
document.writeln("                    <i class=\'n-count animated myBGMSGCount\'></i>");
document.writeln("                    <span>消息推送</span>");
document.writeln("                </a>");
document.writeln("            </div>");
document.writeln("            <div class=\'pull-left tm-icon myHideYJ\'>");
document.writeln("                <a data-drawer=\'notifications\' class=\'drawer-toggle\' href=\'\'>");
document.writeln("                    <i class=\'sa-top-updates\'></i>");
document.writeln("                    <i class=\'n-count animated yjTopImgStyle\'></i>");
document.writeln("                    <span>证件预警</span>");
document.writeln("                </a>");
document.writeln("            </div>");
document.writeln("");
document.writeln("            <div id=\'time\' class=\'pull-right\'>");
document.writeln("                <span id=\'hours\'></span>");
document.writeln("                :");
document.writeln("                <span id=\'min\'></span>");
document.writeln("                :");
document.writeln("                <span id=\'sec\'></span>");
document.writeln("            </div>");
document.writeln("");
/*document.writeln("            <div class=\'media-body\'>");
document.writeln("                <input type=\'text\' class=\'main-search\'>");
document.writeln("            </div>");*/
document.writeln("        </div>");
document.writeln("    </div>");
/*

document.writeln("                <div class=\'navbar-right\'>");




document.writeln("                    <ul class=\'nav navbar-nav\'>");
document.writeln("");
document.writeln("");
document.writeln("                        <!--未读消息 Messages: style can be found in dropdown.less-->");
document.writeln("                        <li id='topShowLp' class=\'dropdown messages-menu\'>");
document.writeln("                            <a href=\'#\' class=\'dropdown-toggle\' data-toggle=\'dropdown\'>");
/!*document.writeln("                                <i class=\'fa fa-envelope\'></i>");*!/
document.writeln("                                <i class=\'iconfont iconshenqingcaigou-\'></i>");
document.writeln("                                <span class=\'label label-success libPurHeadTotal\'></span>");
document.writeln("                            </a>");
document.writeln("                            <ul class=\'dropdown-menu\'>");
document.writeln("                                <li class=\'header\'>您有<span class='libPurHeadTotal'></span>条未处理预采购单</li>");
document.writeln("                                <li>");
document.writeln("                                    <!-- inner menu: contains the actual data -->");
document.writeln("                                    <ul class=\'menu\' id='libPurHeadAllContext'>");

document.writeln("                                    </ul>");
document.writeln("                                </li>");
document.writeln("                                <li class=\'footer\'><a href=\'javascript:void(0)\' onclick='moreLPH()'>更多</a></li>");
document.writeln("                            </ul>");
document.writeln("                        </li>");


document.writeln("                        <li>");
document.writeln("                            <a href=\'#\' class=\'dropdown-toggle\' data-toggle=\'dropdown\'>");
/!*document.writeln("                                <i class=\'fa fa-envelope\'></i>");*!/
document.writeln("                                <i class=\'iconfont iconshenqingcaigou-\'></i>");
document.writeln("                                <span class=\'label label-success libPurHeadTotal\'></span>");
document.writeln("                            </a>");
document.writeln("                            <ul class=\'dropdown-menu\'>");
document.writeln("");
document.writeln("        <!-- 通知start -->");
document.writeln("        <div id=\'notifications\' class=\'tile drawer animated\'>");
document.writeln("            <div class=\'listview narrow\'>");
document.writeln("                <div class=\'media\'>");
document.writeln("                    <a href=\'\'>更多预警</a>");
document.writeln("                    <span class=\'drawer-close\'>&times;</span>");
document.writeln("                </div>");
document.writeln("                <div id=\'yjTopMsg\' class=\'overflow\' style=\'height: 254px\'>");
document.writeln("<div class='media'><div class='pull-left'><img width='40' src='http://127.0.0.1:8077/a.jpg' alt='' title='\"+val.siteName+\"'></div><div class='media-body'><small class='text-muted'>ccccc</small><br><a class='t-overflow' href=''>aaaa</a></div></div>");
document.writeln("                </div>");
document.writeln("                <!--<div class=\'media text-center whiter l-100\'>");
document.writeln("                    <a href=\'\'><small>VIEW ALL</small></a>");
document.writeln("                </div>-->");
document.writeln("            </div>");
document.writeln("        </div>");
document.writeln("        <!-- 通知end -->");
document.writeln("                            </ul>");
document.writeln("                        </li>");

document.writeln("                        <!-- 最右侧头像，登入登出功能  User Account: style can be found in dropdown.less -->");
document.writeln("                        <li class=\'dropdown user user-menu\'>");
document.writeln("                            <a href=\'#\' class=\'dropdown-toggle\' data-toggle=\'dropdown\'>");
document.writeln("                                <i class=\'glyphicon glyphicon-user\'></i>");
document.writeln("                                <span id=\'headAdm\'></span>");
document.writeln("                            </a>");
document.writeln("                            <ul class=\'dropdown-menu\'>");
document.writeln("                                <!-- User image -->");
document.writeln("                                <li class=\'user-header bg-light-blue\'>");
document.writeln("                                    <img src=\'http://122.112.248.67:8077/images/logo1.png\' class=\'img-circle\' alt=\'User Image\' />");
document.writeln("                                    <p>");
document.writeln("                                        <span id=\'headAdm2\'></span>");
document.writeln("                                        <small id=\'headAdmDate\'></small>");
document.writeln("                                    </p>");
document.writeln("                                </li>");
document.writeln("                                <!-- Menu Body -->");
/!*document.writeln("                                <li class=\'user-body\'>");
document.writeln("                                    <div class=\'col-xs-4 text-center\'>");
document.writeln("                                        <a href=\'#\'>关注</a>");
document.writeln("                                    </div>");
document.writeln("                                    <div class=\'col-xs-4 text-center\'>");
document.writeln("                                        <a href=\'#\'>文章</a>");
document.writeln("                                    </div>");
document.writeln("                                    <div class=\'col-xs-4 text-center\'>");
document.writeln("                                        <a href=\'#\'>朋友</a>");
document.writeln("                                    </div>");
document.writeln("                                </li>");*!/
document.writeln("                                <!-- Menu Footer-->");
document.writeln("                                <li class=\'user-footer\'>");
/!*document.writeln("                                    <div class=\'pull-left\'>");
document.writeln("                                        <a href=\'#\' class=\'btn btn-default btn-flat\'>个性签名</a>");
document.writeln("                                    </div>");*!/
document.writeln("                                    <div class=\'pull-right\'>");
document.writeln("                                        <a href='/logout' class=\'btn btn-default btn-flat\'>退出</a>");
document.writeln("                                    </div>");
document.writeln("                                </li>");
document.writeln("                            </ul>");
document.writeln("                        </li>");
document.writeln("                    </ul>");
document.writeln("                </div>");*/
document.writeln("            </nav>");
document.writeln("        </header>");