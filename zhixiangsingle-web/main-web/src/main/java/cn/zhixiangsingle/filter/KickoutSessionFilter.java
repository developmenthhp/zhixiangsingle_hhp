package cn.zhixiangsingle.filter;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.shiro.ShiroFilterUtils;
import cn.zhixiangsingle.web.responsive.IStatusMessage;
import cn.zhixiangsingle.web.responsive.ResponseResult;
import cn.zhixiangsingle.web.responsive.ResultBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.filter
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 8:52
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class KickoutSessionFilter extends AccessControlFilter {
    private static final Logger logger = LoggerFactory
            .getLogger(KickoutSessionFilter.class);
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private String kickoutUrl;
    private boolean kickoutAfter = false;
    private int maxSession = 1;
    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;
    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }
    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }
    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }
    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro-activeSessionCache");
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param request
     * @param response
     * @param mappedValue
     * @date: 2019/8/23 9:04
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request,
                                      ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingsingle
     * @Description:
     * @author: hhp
     * @param:  * @param request
     * @param response
     * @date: 2019/8/23 9:03
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request,
                                     ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            ResultBean resultBean = new ResultBean();
            if (ShiroFilterUtils.isAjax(request) ) {
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.USER_NOT_LOGIN.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.USER_NOT_LOGIN.getMessage());
                out(response, resultBean);
                return false;
            }else{
                return true;
            }
        }
        HttpServletRequest req=(HttpServletRequest) request;
        String path = req.getRequestURI();
        if(path.equals("/toLogin")){
            return true;
        }
        Session session = subject.getSession();
        try {
            User user = (User) subject.getPrincipal();
            String username = user.getUserName();
            Serializable sessionId = session.getId();
            Deque<Serializable> deque = cache.get(username);
            if (deque == null) {
                deque = new ArrayDeque<Serializable>();
            }
            if (!deque.contains(sessionId)
                    && session.getAttribute("kickout") == null) {
                deque.push(sessionId);
                cache.put(username, deque);
            }
            while (deque.size() > maxSession) {
                Serializable kickoutSessionId = null;
                if (kickoutAfter) {
                    kickoutSessionId = deque.removeFirst();
                } else {
                    kickoutSessionId = deque.removeLast();
                }
                cache.put(username, deque);
                try {
                    Session kickoutSession = sessionManager
                            .getSession(new DefaultSessionKey(kickoutSessionId));
                    if (kickoutSession != null) {
                        kickoutSession.setAttribute("kickout", true);
                    }
                } catch (Exception e) {
                }
            }
            if ((Boolean) session.getAttribute("kickout") != null
                    && (Boolean) session.getAttribute("kickout") == true) {
                try {
                    subject.logout();
                } catch (Exception e) { // ignore
                }
                saveRequest(request);
                return isAjaxResponse(request,response);
            }
            return true;
        } catch (Exception e) {
            return isAjaxResponse(request,response);
        }
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param response
     * @param result
     * @date: 2019/8/23 9:04
     */
    public static void out(ServletResponse response, ResultBean result){
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            out = response.getWriter();
            out.println(objectMapper.writeValueAsString(result));
        } catch (Exception e) {
        }finally{
            if(null != out){
                out.flush();
                out.close();
            }
        }
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param request
     * @param response
     * @date: 2019/8/23 9:05
     */
    private boolean isAjaxResponse(ServletRequest request,
                                   ServletResponse response) throws IOException {
        ResultBean resultBean = new ResultBean();
        if (ShiroFilterUtils.isAjax(request) ) {
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.THE_ACCOUNT_HAS_BEEN_REGISTERED_ELSEWHERE.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.THE_ACCOUNT_HAS_BEEN_REGISTERED_ELSEWHERE.getMessage());
            out(response, resultBean);
        }else{
            WebUtils.issueRedirect(request, response, kickoutUrl);
        }
        return false;
    }
}
