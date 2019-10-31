package cn.zhixiangsingle.web.result;

import cn.zhixiangsingle.web.responsive.IStatusMessage;
import cn.zhixiangsingle.web.responsive.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.web.result
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/31 9:50
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class CommonResultMethod {
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description: 修改或更新返回公共方法
     * @author: hhp
     * @param:  * @param changeTotal mapper返回的更改的条数
    @date: 2019/8/31 9:53
     */
    public static ResultBean getUpdOrInsertResultBean(int changeTotal){
        ResultBean resultBean = new ResultBean();
        if(changeTotal==1){
            resultBean.setSuccess(true);
            resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        }else{
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
}
