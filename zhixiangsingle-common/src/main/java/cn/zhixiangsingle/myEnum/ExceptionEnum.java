package cn.zhixiangsingle.myEnum;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.myEnum
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 15:20
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public enum ExceptionEnum {
    UNKNOW_ERROR(-1,"UnknowError","未知错误"),
    USER_NOT_FIND(-101,"UserNotFind","用户不存在"),
    BAD_REQUEST(400,"BadRequest","请求有误"),
    FORBIDDEN(403,"Forbidden","权限不足"),
    NOT_FOUND(404,"NotFound","您所访问的资源不存在"),
    SERVER_EPT(500,"ServerEpt","操作异常，请稍后再试");

    private Integer type;
    private String code;
    private String msg;

    ExceptionEnum(Integer type,String code, String msg) {
        this.type = type;
        this.code = code;
        this.msg = msg;
    }

    public Integer getType() {
        return type;
    }
    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
