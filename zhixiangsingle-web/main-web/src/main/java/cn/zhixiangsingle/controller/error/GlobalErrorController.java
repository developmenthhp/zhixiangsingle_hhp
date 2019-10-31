package cn.zhixiangsingle.controller.error;

import cn.zhixiangsingle.myEnum.ExceptionEnum;
import cn.zhixiangsingle.web.responsive.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.controller.error
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 14:00
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("error")
public class GlobalErrorController extends AbstractErrorController {
    private static final Logger logger = LoggerFactory
            .getLogger(GlobalErrorController.class);
    private static final String ERROR_PATH = "error";
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param errorAttributes
     * @date: 2019/8/23 14:03
     */
    public GlobalErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/8/23 14:03
     */
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param request
     * @param response
     * @date: 2019/8/23 15:24
     */
    @RequestMapping(produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request,
                                  HttpServletResponse response) {
        logger.debug("统一异常处理【" + getClass().getName()
                + ".errorHtml】text/html=普通请求：request=" + request);
        ModelAndView mv = new ModelAndView(ERROR_PATH);
        Map<String, Object> model = getErrorAttributes(request,
                isIncludeStackTrace(request, MediaType.TEXT_HTML));
        HttpStatus httpStatus = getStatus(request);
        // 2 返回错误提示
        ExceptionEnum ee = getMessage(httpStatus);
        ResultBean resultBean = new ResultBean();
        resultBean.setResultCode(ee.getCode());
        resultBean.setMsg(ee.getMsg());
        resultBean.setObj(ee.getType());
        // 3 将错误信息放入mv中
        mv.addObject("result", resultBean);
        resultBean = null;
        logger.info("统一异常处理【" + getClass().getName()
                + ".errorHtml】统一异常处理!错误信息mv：" + mv);
        return mv;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param request
     * @date: 2019/8/23 15:24
     */
    @RequestMapping(value="/toMyErrorPage",produces = "text/html")
    public ModelAndView toMyErrorHtml(HttpServletRequest request) {
        logger.debug("跳转至自定义错误页面 request=" + request);
        // 1 获取错误状态码（也可以根据异常对象返回对应的错误信息）
        HttpStatus httpStatus = getStatus(request);
        String errorPage = "/error/"+httpStatus;
        ModelAndView mv = new ModelAndView(errorPage);
        return mv;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param request
     * @param e
     * @date: 2019/8/23 15:24
     */
    @RequestMapping
    @ResponseBody
    //设置响应状态码为：200，结合前端约定的规范处理。也可不设置状态码，前端ajax调用使用error函数进行控制处理
    @ResponseStatus(value=HttpStatus.OK)
    public ResultBean error(HttpServletRequest request, Exception e) {
        logger.info("统一异常处理【" + getClass().getName()
                + ".error】text/html=普通请求：request=" + request );
        /** model对象包含了异常信息 */
        Map<String, Object> model = getErrorAttributes(request,
                isIncludeStackTrace(request, MediaType.TEXT_HTML));
        logger.info("统一异常处理【" + getClass().getName()
                + ".error】统一异常处理：model=" + model);
        // 1 获取错误状态码（也可以根据异常对象返回对应的错误信息）
        HttpStatus httpStatus = getStatus(request);
        logger.debug("统一异常处理【" + getClass().getName()
                + ".error】统一异常处理!错误状态码httpStatus：" + httpStatus);
        // 2 返回错误提示
        ExceptionEnum ee = getMessage(httpStatus);
        ResultBean resultBean = new ResultBean();
        resultBean.setResultCode(ee.getCode());
        resultBean.setMsg(ee.getMsg());
        resultBean.setObj(ee.getType());
        // 3 将错误信息返回
        //		ResponseEntity
        logger.info("统一异常处理【" + getClass().getName()
                + ".error】统一异常处理!错误信息result：" + resultBean);
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param httpStatus
     * @date: 2019/8/23 15:23
     */
    @SuppressWarnings("static-access")
    private ExceptionEnum getMessage(HttpStatus httpStatus) {
        if (httpStatus.is4xxClientError()) {
            // 4开头的错误状态码
            if ("400".equals(httpStatus.BAD_REQUEST)) {
                return ExceptionEnum.BAD_REQUEST;
            } else if ("403".equals(httpStatus.FORBIDDEN)) {
                return ExceptionEnum.BAD_REQUEST;
            } else if ("404".equals(httpStatus.NOT_FOUND)) {
                return ExceptionEnum.NOT_FOUND;
            }
        } else if (httpStatus.is5xxServerError()) {
            // 5开头的错误状态码
            if ("500".equals(httpStatus.INTERNAL_SERVER_ERROR)) {
                return ExceptionEnum.SERVER_EPT;
            }
        }
        // 统一返回：未知错误
        return ExceptionEnum.UNKNOW_ERROR;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param request
     * @param produces
     * @date: 2019/8/23 15:23
     */
    private boolean isIncludeStackTrace(HttpServletRequest request,
                                        MediaType produces) {
        ErrorProperties.IncludeStacktrace include = getErrorProperties().getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }

    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/8/23 15:23
     */
    protected ErrorProperties getErrorProperties() {
        return new ErrorProperties();
    }
}
