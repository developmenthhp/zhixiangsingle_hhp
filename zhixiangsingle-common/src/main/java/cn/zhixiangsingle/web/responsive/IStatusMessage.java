package cn.zhixiangsingle.web.responsive;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.web.responsive
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/21 19:01
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IStatusMessage {
    String getCode();
    String getMessage();
    public enum SystemStatus implements IStatusMessage {
        //请求成功
        SUCCESS("100",WebMassage.INTERFACE_STATUS_SUCCESS),
        //请求失败
        ERROR("101",WebMassage.INTERFACE_STATUS_ERROR),
        //参数缺失
        PARAMETERS_LOSE("102",WebMassage.PARAMETERS_LOSE),
        //参数缺失
        FILE_TYPE_NOT_SUPPORT("103",WebMassage.FILE_TYPE_NOT_SUPPORT),
        //子权限未删除
        SUN_PERMISSION_NOT_DELETE("104",WebMassage.SUN_PERMISSION_NOT_DELETE),
        //临期时间未设置
        SITE_IS_NOT_SET_WARNING_OVER_TIME("105",WebMassage.SITE_IS_NOT_SET_WARNING_OVER_TIME),
        //名称已存在
        THE_NAME_EXIST("106",WebMassage.THE_NAME_EXIST),
        //该账号已在别处登录
        THE_ACCOUNT_HAS_BEEN_REGISTERED_ELSEWHERE("107",WebMassage.THE_ACCOUNT_HAS_BEEN_REGISTERED_ELSEWHERE),
        //文件为空
        FILE_IS_EMPTY("108",WebMassage.FILE_IS_EMPTY),
        //参数异常
        PARAM_ERROR("109",WebMassage.PARAM_ERROR),
        //权限为空
        ROLE_IS_EMPTY("110",WebMassage.ROLE_IS_EMPTY),
        //站点角色为空
        SITE_ROLE_IS_EMPTY("111",WebMassage.SITE_ROLE_IS_EMPTY),
        //用户未登录
        USER_NOT_LOGIN("112",WebMassage.USER_NOT_LOGIN),
        //角色未授权
        PERMISSION_IS_EMPTY("113",WebMassage.PERMISSION_IS_EMPTY),
        //用户不存在
        USER_NOT_EXIST("114",WebMassage.USER_NOT_EXIST),
        //用户已离职
        USER_HAS_QUIT("115",WebMassage.USER_HAS_QUIT),
        //用户名或密码错误
        NAME_OR_PASSWORD_WRONG("116",WebMassage.NAME_OR_PASSWORD_WRONG),
        //账户已锁定
        ACCOUNT_IS_LOCKED("117",WebMassage.ACCOUNT_IS_LOCKED),
        //登录验证已失效5次，账户锁定
        ACCOUINT_INVALIDATE_OVER_FIVE_TOTAL("118",WebMassage.ACCOUINT_INVALIDATE_OVER_FIVE_TOTAL),
        //用户登录失败次数
        USER_WRONG_LOGIN_COUNT("119",WebMassage.USER_WRONG_LOGIN_COUNT),
        //规格型号单位缺失
        GGXHDW_LOSE("120",WebMassage.GGXHDW_LOSE),
        //清洗时间缺失
        CLEAN_TIME_LOSE("121",WebMassage.CLEAN_TIME_LOSE),
        //清洗水位缺失
        CLEAN_WATER_LOSE("122",WebMassage.CLEAN_WATER_LOSE),
        //清洗时间、水位缺失
        CLEAN_TIME_WATER_LOSE("123",WebMassage.CLEAN_TIME_WATER_LOSE),
        //bom类型问题
        BOM_TYPE_TOTAL_ERROR("124",WebMassage.BOM_TYPE_TOTAL_ERROR),
        //站点缺失
        SITE_ID_LOSE("125",WebMassage.SITE_ID_LOSE),
        //数据已存在
        DATA_EXITS("126",WebMassage.DATA_EXITS),
        //参数过大
        PARAM_BIG("127",WebMassage.PARAM_BIG);

        private String code;
        private String message;
        private SystemStatus(String code,String message){
            this.code = code;
            this.message = message;
        }
        public String getCode(){
            return this.code;
        }
        public String getMessage(){
            return this.message;
        }
    }
}
